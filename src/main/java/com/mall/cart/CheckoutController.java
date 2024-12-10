package com.mall.cart;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	// 處理結帳請求
	@PostMapping("/cart/checkout")
	public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
		Integer memId = request.getMemId();
		List<Integer> selectedItemIds = request.getItemIds();

		// 獲取購物車
		Cart cart = cartRepository.findByMemId(memId)
				.orElseThrow(() -> new RuntimeException("會員的購物車未找到，會員ID：" + memId));

		// 獲取選中的購物車項目
		List<CartItem> selectedCartItems = cartItemRepository.findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(),
				selectedItemIds);

		// 分組和計算總金額
		Map<String, Object> checkoutDetails = cartServiceImpl.groupAndCalculateTotalAmounts(selectedCartItems);

		// 返回結帳細節
		return ResponseEntity.ok(checkoutDetails);
	}

	@GetMapping("/cart/checkout/page")
	public String showCheckoutPage(@RequestParam Integer memId, @RequestParam List<Integer> selectedItemIds,
			Model model) {
		// 檢查數據
		if (selectedItemIds == null || selectedItemIds.isEmpty()) {
			// 這裡可以選擇重定向回購物車頁面或顯示錯誤提示
			model.addAttribute("error", "請選擇至少一個商品進行結帳！");
			return "redirect:/cart"; // 返回購物車頁面
		}

		// Step 1: 獲取分組的商品數據
		Map<Integer, List<CartItemDto>> groupedItems = cartServiceImpl.getSelectedItems(memId, selectedItemIds);

		// Step 2: 計算每間咖啡廳的總金額
		Map<Integer, Integer> totalAmounts = cartServiceImpl.calculateTotalAmounts(groupedItems);

		// Step 3: 將數據傳遞到前端
		model.addAttribute("groupedItems", groupedItems); // 分組後的商品
		model.addAttribute("totalAmounts", totalAmounts); // 每間咖啡廳的總金額

		return "front-end/cart/checkout"; // 返回 Thymeleaf 模板
	}

}
