package com.mall.order;

import com.mall.cart.CheckoutRequest;
import com.mall.cart.CartRepository;
import com.mall.cart.CartServiceImpl;
import com.mall.cart.Cart;
import com.mall.cart.CartItemDto;
import com.mall.cart.CartItemRepository;
import com.mall.order.Orders;
import com.mall.order.OrderDetails;
import com.mall.order.OrderDetails.OrderDetailsId;
import com.mall.order.OrderDetailsRepository;
import com.mall.order.OrdersRepository;
import com.mall.order.OrdersService; // 引入 OrdersService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrdersController {

	@Autowired
	private OrdersService ordersService; // 注入 OrdersService

	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@GetMapping("/seller/orders")
	public String getOrderList(@RequestParam(required = false) String orderId,
			@RequestParam(required = false) String status, @RequestParam(required = false) String yearFilter,
			@RequestParam(required = false) String monthFilter, Model model) {
		List<Orders> orderList = ordersService.getOrderList(orderId, status, yearFilter, monthFilter);
		model.addAttribute("orderList", orderList);
		model.addAttribute("yearList", ordersService.getAvailableYears());
		model.addAttribute("monthList", ordersService.getAvailableMonths());
		return "order-management";
	}

	@GetMapping("/seller/orders/ship/{orderId}")
	public String shipOrder(@PathVariable Integer orderId) {
		ordersService.updateOrderStatus(orderId, 2);
		return "redirect:/seller/orders";
	}

	@GetMapping("/seller/orders/cancel/{orderId}")
	public String cancelOrder(@PathVariable Integer orderId) {
		ordersService.updateOrderStatus(orderId, 5);
		return "redirect:/seller/orders";
	}

//	@PostMapping("/cart/submit-order")
//	public ResponseEntity<?> submitOrder(@RequestBody CheckoutRequest request) {
//		Integer memId = request.getMemId();
//		List<CheckoutRequest.CafeOrder> cafes = request.getCafes(); // 這裡應該從前端獲取咖啡廳的資料
//
//		List<Integer> itemIds = request.getItemIds();
//		System.out.println("Selected Item IDs: " + itemIds); // 檢查選中的商品 ID
//
//		// 獲取會員的購物車
//		Cart cart = cartRepository.findByMemId(memId)
//				.orElseThrow(() -> new RuntimeException("會員的購物車未找到，會員ID：" + memId));
//
//		// 檢查memId與cafes的資料是否正常
//		if (memId == null || cafes == null || cafes.isEmpty()) {
//			return ResponseEntity.badRequest().body("缺少必要的訂單資料");
//		}
//
//		// 用來儲存每間咖啡廳的訂單
//		List<Orders> orders = new ArrayList<>();
//		List<OrderDetails> orderDetails = new ArrayList<>();
//
//		// 用來計算整個訂單的總金額
//		Integer totalAmount = 0;
//
//		// 遍歷每間咖啡廳，為每間咖啡廳創建一個獨立的訂單
//		for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
//			// 檢查商品數量
//			for (CheckoutRequest.CartItemRequest item : cafeOrder.getItems()) {
//				Integer quantity = item.getNum();
//				if (quantity == null) {
//					return ResponseEntity.badRequest().body("商品數量為空");
//				}
//			}
//
//			Integer cafeId = cafeOrder.getCafeId();
//			List<CartItemDto> items = cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems());
//
//			// 計算每個咖啡廳的總金額
//			Integer cafeTotalAmount = ordersService.calculateTotalAmount(cafeOrder.getItems());
//			Integer shippingFee = 100; // 假設每個咖啡廳的運費為 100 元
//			Integer cafeTotalWithShipping = cafeTotalAmount + shippingFee; // 計算包括運費在內的總金額
//
//			totalAmount += cafeTotalWithShipping; // 將該咖啡廳的金額加到整個訂單的總金額
//
//			// 為每間咖啡廳創建一個新的訂單
//			Orders order = new Orders();
//			order.setMemId(memId);
//			order.setCafeId(cafeId);
//			order.setDate(new Date()); // 設定訂單日期
//			order.setAmount(cafeTotalWithShipping); // 設定該咖啡廳的總金額（包括運費）
//			order.setStatus(0); // 訂單狀態設為"未付款"
//			order.setPaid(0); // 設定 PAID 欄位為 0 (未支付)
//			order.setMemo(cafeOrder.getRemark()); // 存儲咖啡廳的備註
//			orders.add(order);
//		}
//
//		// 保存訂單以讓資料庫生成 ORDER_ID
//		orders = ordersRepository.saveAll(orders); // 確保保存後，orderId 已被賦值
//
//		// 生成訂單明細
//		for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
//			for (CartItemDto item : cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems())) {
//				// 查找對應的訂單
//				Orders order = orders.stream().filter(o -> o.getCafeId().equals(cafeOrder.getCafeId())) // 根據 cafeId
//						.findFirst().orElseThrow(() -> new RuntimeException("訂單未找到"));
//
//				OrderDetails orderDetail = new OrderDetails();
//				OrderDetailsId orderDetailId = new OrderDetailsId();
//				orderDetailId.setOrderId(order.getOrderId()); // 使用資料庫生成的 orderId
//				orderDetailId.setItemId(item.getItemId());
//				orderDetail.setOrderDetailsId(orderDetailId);
//				orderDetail.setSalesNum(item.getNum());
//				orderDetail.setPrice(item.getPrice());
//				orderDetail.setIsReturn(0); // 初始設為不退貨
//				orderDetail.setReturnReason(0); // 無退貨原因
//				orderDetails.add(orderDetail);
//			}
//		}
//
//		// 儲存訂單明細
//		orderDetailsRepository.saveAll(orderDetails);
//
//		// 返回訂單 ID 和總金額
//		List<Integer> orderIds = new ArrayList<>();
//		for (Orders o : orders) {
//			orderIds.add(o.getOrderId()); // 收集所有的訂單ID
//		}
//
//		return ResponseEntity.ok().body(Map.of("orderIds", orderIds, "totalAmount", totalAmount));
//	}

//	@GetMapping("/payment/result")
//	public String paymentResult(@RequestParam Map<String, String> params, Model model) {
//		System.out.println("收到的參數：" + params); // 調試訊息
//		String rtnCode = params.get("RtnCode");
//		String rtnMsg = params.get("RtnMsg");
//
//		if (!"1".equals(rtnCode)) {
//			model.addAttribute("message", rtnMsg != null ? rtnMsg : "付款失敗，請重新嘗試。");
//			return "back-end/cart/paymentFailure";
//		} else {
//			return "redirect:/back-end/cart/confirmation";
//		}
//	}

	@GetMapping("/payment/failure")
	public String paymentFailure() {
		return "front-end/cart/paymentFailure"; // 對應的 Thymeleaf 模板名稱
	}

	@GetMapping("/payment/success")
	public String paymentResult() {
		return "front-end/cart/confirmation"; // 對應的 Thymeleaf 模板名稱
	}

}
