//package com.mall.cart;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class CheckoutController {
//
//	@Autowired
//	private CartServiceImpl cartServiceImpl;
//
//	@Autowired
//	private CartRepository cartRepository;
//
//	@Autowired
//	private CartItemRepository cartItemRepository;
//
//	// 處理結帳請求
//	@PostMapping("/cart/checkout")
//	public ResponseEntity<?> checkout(HttpSession session, @RequestBody CheckoutRequest request) {
//		Integer memId = (Integer) session.getAttribute("mem_Id");
//		if (memId == null) {
//			return ResponseEntity.status(401).body(Map.of("success", false, "message", "用戶未登入"));
//		}
//
//		List<Integer> selectedItemIds = request.getItemIds();
//
//		// 獲取購物車
//		Cart cart = cartRepository.findByMemId(memId)
//				.orElseThrow(() -> new RuntimeException("會員的購物車未找到，會員ID：" + memId));
//
//		// 獲取選中的購物車項目
//		List<CartItem> selectedCartItems = cartItemRepository.findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(),
//				selectedItemIds);
//
//		// 分組和計算總金額
//		Map<String, Object> checkoutDetails = cartServiceImpl.groupAndCalculateTotalAmounts(selectedCartItems);
//
//		// 返回結帳細節
//		return ResponseEntity.ok(checkoutDetails);
//	}
//
//	@GetMapping("/cart/checkout/page")
//	public String showCheckoutPage(HttpSession session, @RequestParam List<Integer> selectedItemIds, Model model) {
//
//		Integer memId = (Integer) session.getAttribute("mem_Id");
//
//		// 檢查數據
//		if (selectedItemIds == null || selectedItemIds.isEmpty()) {
//			// 這裡可以選擇重定向回購物車頁面或顯示錯誤提示
//			model.addAttribute("error", "請選擇至少一個商品進行結帳！");
//			return "redirect:/cart"; // 返回購物車頁面
//		}
//
//		// Step 1: 獲取分組的商品數據
//		Map<Integer, List<CartItemDto>> groupedItems = cartServiceImpl.getSelectedItems(memId, selectedItemIds);
//
//		// Step 2: 計算每間咖啡廳的總金額
//		Map<Integer, Integer> totalAmounts = cartServiceImpl.calculateTotalAmounts(groupedItems);
//
//		// Step 3: 將數據傳遞到前端
//		model.addAttribute("groupedItems", groupedItems); // 分組後的商品
//		model.addAttribute("totalAmounts", totalAmounts); // 每間咖啡廳的總金額
//
//		return "front-end/cart/checkout"; // 返回 Thymeleaf 模板
//	}
//
//}


//VICKY修改
package com.mall.cart;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mall.uco.model.UcoService;
import com.mall.uco.model.UcoVO;

@Controller
public class CheckoutController {

    @Autowired
    private CartServiceImpl cartServiceImpl;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UcoService ucoService;
    
    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session) {
        Integer memId = (Integer) session.getAttribute("mem_Id"); // 從 Session 中獲取會員 ID
        if (memId == null) {
            return "redirect:/login"; // 若未登入，重定向至登入頁面
        }
        model.addAttribute("memId", memId); // 傳遞會員 ID 至前端
        return "front-end/cart/checkout"; // 返回完整路徑的 Thymeleaf 模板
    }


    @GetMapping("/cart/checkout/page")
    public String showCheckoutPage(HttpSession session, @RequestParam List<Integer> selectedItemIds, Model model) {
        Integer memId = (Integer) session.getAttribute("mem_Id");

        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            model.addAttribute("error", "請選擇至少一個商品進行結帳！");
            return "redirect:/cart";
        }

        Map<Integer, List<CartItemDto>> groupedItems = cartServiceImpl.getSelectedItems(memId, selectedItemIds);
        Map<Integer, Integer> totalAmounts = cartServiceImpl.calculateTotalAmounts(groupedItems);
        int totalCartAmount = totalAmounts.values().stream().mapToInt(Integer::intValue).sum();

        List<UcoVO> availablePcoCoupons = ucoService.getCouponsWithDetailsByMemberId(memId).stream()
                .filter(coupon -> "P".equals(coupon.getType())) // 平台優惠券
                .filter(coupon -> coupon.getStatus() == 0) // 未使用
                .filter(coupon -> totalCartAmount >= coupon.getMinSpend()) // 滿足最低消費額
                .toList();


        Map<Integer, List<UcoVO>> availableCcoCoupons = ucoService.getAvailableCouponsGroupedByCafe(memId, totalAmounts);

        model.addAttribute("groupedItems", groupedItems);
        model.addAttribute("totalAmounts", totalAmounts);
        model.addAttribute("platformCoupons", availablePcoCoupons);
        model.addAttribute("merchantCoupons", availableCcoCoupons);

        return "front-end/cart/checkout";
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<?> checkout(HttpSession session, @RequestBody CheckoutRequest request) {
        Integer memId = (Integer) session.getAttribute("mem_Id");
        if (memId == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "用戶未登入"));
        }

        List<Integer> selectedItemIds = request.getItemIds();
        Integer platformCouponId = request.getPlatformCoupon();
        List<CheckoutRequest.CafeOrder> cafes = request.getCafes();

        Map<Integer, List<CartItemDto>> groupedItems = cartServiceImpl.getSelectedItems(memId, selectedItemIds);
        Map<Integer, Integer> totalAmounts = cartServiceImpl.calculateTotalAmounts(groupedItems);

        int platformDiscount = 0;
        if (platformCouponId != null && platformCouponId != 0) {
            UcoVO platformCoupon = ucoService.getCoupon(platformCouponId);
            if (platformCoupon != null && platformCoupon.getStatus() == 0) { // 確保未使用
                int totalCartAmount = totalAmounts.values().stream().mapToInt(Integer::intValue).sum();
                if (totalCartAmount >= platformCoupon.getMinSpend()) {
                    platformDiscount = Math.min(platformCoupon.getDiscount(), totalCartAmount);
                    ucoService.markCouponAsUsed(platformCouponId);
                }
            }
        }

        Map<Integer, Integer> merchantDiscounts = cafes.stream().collect(Collectors.toMap(
                CheckoutRequest.CafeOrder::getCafeId,
                cafe -> {
                    Integer cafeCouponId = cafe.getCoupon();
                    if (cafeCouponId != null && cafeCouponId != 0) {
                        UcoVO merchantCoupon = ucoService.getCoupon(cafeCouponId);
                        if (merchantCoupon != null && merchantCoupon.getStatus() == 0) {
                            if (totalAmounts.get(cafe.getCafeId()) >= merchantCoupon.getMinSpend()) {
                                ucoService.markCouponAsUsed(cafeCouponId);
                                return merchantCoupon.getDiscount();
                            }
                        }
                    }
                    return 0; // 無折扣
                }
        ));

        int totalDiscount = platformDiscount + merchantDiscounts.values().stream().mapToInt(Integer::intValue).sum();
        int finalTotalAmount = totalAmounts.values().stream().mapToInt(Integer::intValue).sum() - totalDiscount;
        System.out.println("Grouped Items: " + groupedItems);
        System.out.println("商品總金額: " + totalAmounts.values().stream().mapToInt(Integer::intValue).sum());
        System.out.println("總折扣: " + totalDiscount);
        System.out.println("最終金額: " + finalTotalAmount);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "finalTotalAmount", finalTotalAmount,
                "platformDiscount", platformDiscount,
                "merchantDiscounts", merchantDiscounts,
                "totalDiscount", totalDiscount
                
        ));
    }
}  