package com.mall.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mall.cart.Cart;
import com.mall.cart.CartItemDto;
import com.mall.cart.CartRepository;
import com.mall.cart.CartServiceImpl;
import com.mall.cart.CheckoutRequest;
import com.mall.item.Item;
import com.mall.item.ItemRepository;
import com.mall.order.OrderDetails.OrderDetailsId;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@RestController
public class EcpayController {

	@Value("${ecpay.hashKey}")
	private String hashKey;

	@Value("${ecpay.hashIV}")
	private String hashIV;

	@Value("${ecpay.merchantID}")
	private String merchantID;

	@Value("${ecpay.returnURL}")
	private String returnURL;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@PostMapping("/cart/submit-order")
	public ResponseEntity<String> submitOrder(HttpSession session, @RequestBody CheckoutRequest request) {

		Integer memId = (Integer) session.getAttribute("mem_Id");
		List<CheckoutRequest.CafeOrder> cafes = request.getCafes();

		if (memId == null || cafes == null || cafes.isEmpty()) {
			return ResponseEntity.badRequest().body("缺少必要的訂單資料");
		}

		Integer totalAmount = 0;
		List<String> itemNames = new ArrayList<>();

		for (CheckoutRequest.CafeOrder cafe : cafes) {
			for (CheckoutRequest.CartItemRequest item : cafe.getItems()) {
				totalAmount += item.getPrice() * item.getNum();
				itemNames.add(item.getItemName() + " * " + item.getNum());
			}
			totalAmount += cafe.getShippingFee();
		}

		// 綠界支付參數
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();

		obj.setMerchantTradeNo(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		obj.setMerchantTradeDate(formatter.format(new Date()));
		obj.setTotalAmount(totalAmount.toString());
		obj.setItemName(String.join("#", itemNames));
		obj.setTradeDesc("購物平台結帳");
		obj.setReturnURL("http://localhost:8080/payment/result"); // post 本地端不能用
		obj.setClientBackURL("http://localhost:8080/payment/success"); // get 到時改為商城路徑
		obj.setPaymentType("aio");
		obj.setNeedExtraPaidInfo("N");
		obj.setEncryptType("1");

		// 返回綠界支付 HTML 表單
		String paymentFormHtml = all.aioCheckOut(obj, null);
		return ResponseEntity.ok(paymentFormHtml);
	}

	@PostMapping("/payment/result")
	public ResponseEntity<?> createOrder(HttpSession session, @RequestBody CheckoutRequest request) {
		Integer memId = (Integer) session.getAttribute("mem_Id");
		List<CheckoutRequest.CafeOrder> cafes = request.getCafes(); // 這裡應該從前端獲取咖啡廳的資料

		List<Integer> itemIds = request.getItemIds();
		System.out.println("Selected Item IDs: " + itemIds); // 檢查選中的商品 ID

		// 獲取會員的購物車
		Cart cart = cartRepository.findByMemId(memId)
				.orElseThrow(() -> new RuntimeException("會員的購物車未找到，會員ID：" + memId));

		// 檢查memId與cafes的資料是否正常
		if (memId == null || cafes == null || cafes.isEmpty()) {
			return ResponseEntity.badRequest().body("缺少必要的訂單資料");
		}

		// 用來儲存每間咖啡廳的訂單
		List<Orders> orders = new ArrayList<>();
		List<OrderDetails> orderDetails = new ArrayList<>();

		// 用來計算整個訂單的總金額
		Integer totalAmount = 0;

		// 遍歷每間咖啡廳，為每間咖啡廳創建一個獨立的訂單
		for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
			// 檢查商品數量
			for (CheckoutRequest.CartItemRequest item : cafeOrder.getItems()) {
				Integer quantity = item.getNum();
				if (quantity == null) {
					return ResponseEntity.badRequest().body("商品數量為空");
				}
			}

			Integer cafeId = cafeOrder.getCafeId();
			List<CartItemDto> items = cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems());

			// 計算每個咖啡廳的總金額
			Integer cafeTotalAmount = ordersService.calculateTotalAmount(cafeOrder.getItems());
			Integer shippingFee = 100; // 假設每個咖啡廳的運費為 100 元
			Integer cafeTotalWithShipping = cafeTotalAmount + shippingFee; // 計算包括運費在內的總金額

			totalAmount += cafeTotalWithShipping; // 將該咖啡廳的金額加到整個訂單的總金額

			// 為每間咖啡廳創建一個新的訂單
			Orders order = new Orders();
			order.setMemId(memId);
			order.setCafeId(cafeId);
			order.setDate(new Date()); // 設定訂單日期
			order.setAmount(cafeTotalWithShipping); // 設定該咖啡廳的總金額（包括運費）
			order.setStatus(0); // 訂單狀態設為預設"已付款"
			order.setPaid(1); // PAID是實付金額不是付款狀態QAQ

			order.setMemo(cafeOrder.getRemark()); // 存儲咖啡廳的備註
			orders.add(order);
		}

		// 保存訂單以讓資料庫生成 ORDER_ID
		orders = ordersRepository.saveAll(orders); // 確保保存後，orderId 已被賦值

		// 生成訂單明細
		for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
			for (CartItemDto item : cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems())) {
				// 查找對應的訂單
				Orders order = orders.stream().filter(o -> o.getCafeId().equals(cafeOrder.getCafeId())) // 根據 cafeId
						.findFirst().orElseThrow(() -> new RuntimeException("訂單未找到"));

				OrderDetails orderDetail = new OrderDetails();
				OrderDetailsId orderDetailId = new OrderDetailsId();
				orderDetailId.setOrderId(order.getOrderId()); // 使用資料庫生成的 orderId
				orderDetailId.setItemId(item.getItemId());
				orderDetail.setOrderDetailsId(orderDetailId);
				orderDetail.setSalesNum(item.getNum());
				orderDetail.setPrice(item.getPrice());
				orderDetail.setIsReturn(0); // 初始設為不退貨
				orderDetail.setReturnReason(0); // 無退貨原因
				orderDetails.add(orderDetail);

				// 減少庫存並檢查是否需要下架商品
				Item itemFromDb = itemRepository.findById(item.getItemId())
						.orElseThrow(() -> new RuntimeException("商品未找到，商品ID：" + item.getItemId()));

				// 訂單中的購買數量
				int purchaseQuantity = item.getNum();

				// 商品表中的當前數量
				int currentQuantity = itemFromDb.getNum();

				// 計算新的商品數量
				int newQuantity = currentQuantity - purchaseQuantity;

				// 檢查商品數量是否足夠
				if (newQuantity < 0) {
					throw new RuntimeException("商品數量不足，商品ID：" + item.getItemId());
				}

				// 更新商品數量
				itemFromDb.setNum(newQuantity);

				// 如果商品數量為 0，將商品狀態設置為下架
				if (newQuantity == 0) {
					itemFromDb.setStatus(0); // 假設 0 表示下架狀態
				}

				// 保存商品更新
				itemRepository.save(itemFromDb);

			}
		}

		// 儲存訂單明細
		orderDetailsRepository.saveAll(orderDetails);

		// 返回訂單 ID 和總金額
		List<Integer> orderIds = new ArrayList<>();
		for (Orders o : orders) {
			orderIds.add(o.getOrderId()); // 收集所有的訂單ID
		}

		// 移除購物車中已購買的商品
		cartServiceImpl.removePurchasedItems(cart.getCartId(), itemIds);

		return ResponseEntity.ok().body(Map.of("orderIds", orderIds, "totalAmount", totalAmount));
	}

//	@PostMapping("/payment/result")
//	public ResponseEntity<?> paymentResult(@RequestParam Map<String, String> params, Model model) {

//		String paymentStatus = params.get("RtnCode"); // 支付狀態碼
//		String customField1 = params.get("CustomField1"); // 來自支付的 memId
//		String customField2 = params.get("CustomField2"); // 來自支付的 cafes 資料
//
//		if (!"1".equals(paymentStatus)) {
//			model.addAttribute("message", "支付失敗");
//			return ResponseEntity.badRequest().body("支付失敗");
//		}
//
//		try {
//			// 還原 memId 和 cafes 的資料
//			Integer memId = Integer.parseInt(customField1);
//			List<CheckoutRequest.CafeOrder> cafes = new ObjectMapper().readValue(customField2, new TypeReference<>() {
//			});
//
//			// 檢查數據合法性
//			if (memId == null || cafes == null || cafes.isEmpty()) {
//				return ResponseEntity.badRequest().body("缺少必要的訂單資料");
//			}
//
//			// 用來儲存每間咖啡廳的訂單和訂單明細
//			List<Orders> orders = new ArrayList<>();
//			List<OrderDetails> orderDetails = new ArrayList<>();
//			Integer totalAmount = 0;
//
//			// 獲取會員的購物車
//			Cart cart = cartRepository.findByMemId(memId)
//					.orElseThrow(() -> new RuntimeException("會員的購物車未找到，會員ID：" + memId));
//
//			for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
//				// 檢查商品數量是否合法
//				for (CheckoutRequest.CartItemRequest item : cafeOrder.getItems()) {
//					if (item.getNum() == null || item.getNum() <= 0) {
//						return ResponseEntity.badRequest().body("商品數量無效");
//					}
//				}
//
//				Integer cafeId = cafeOrder.getCafeId();
//				List<CartItemDto> items = cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems());
//
//				// 計算每個咖啡廳的總金額
//				Integer cafeTotalAmount = items.stream().mapToInt(item -> item.getPrice() * item.getNum()).sum();
//				Integer shippingFee = 100; // 假設每個咖啡廳的運費為 100 元
//				Integer cafeTotalWithShipping = cafeTotalAmount + shippingFee;
//
//				totalAmount += cafeTotalWithShipping; // 更新總金額
//
//				// 創建每間咖啡廳的訂單
//				Orders order = new Orders();
//				order.setMemId(memId);
//				order.setCafeId(cafeId);
//				order.setDate(new Date());
//				order.setAmount(cafeTotalWithShipping);
//				order.setStatus(1); // 支付成功狀態
//				order.setPaid(1); // 已支付
//				order.setMemo(cafeOrder.getRemark());
//				orders.add(order);
//			}
//
//			// 保存訂單
//			orders = ordersRepository.saveAll(orders);
//
//			// 生成訂單明細
//			for (CheckoutRequest.CafeOrder cafeOrder : cafes) {
//				for (CartItemDto item : cartServiceImpl.getCartItemsForCafe(cart, cafeOrder.getItems())) {
//					Orders order = orders.stream().filter(o -> o.getCafeId().equals(cafeOrder.getCafeId())).findFirst()
//							.orElseThrow(() -> new RuntimeException("未找到對應的訂單，咖啡廳 ID：" + cafeOrder.getCafeId()));
//
//					// 創建訂單明細
//					OrderDetails orderDetail = new OrderDetails();
//					OrderDetailsId orderDetailId = new OrderDetailsId();
//					orderDetailId.setOrderId(order.getOrderId());
//					orderDetailId.setItemId(item.getItemId());
//					orderDetail.setOrderDetailsId(orderDetailId);
//					orderDetail.setSalesNum(item.getNum());
//					orderDetail.setPrice(item.getPrice());
//					orderDetail.setIsReturn(0); // 不退貨
//					orderDetail.setReturnReason(0); // 無退貨原因
//					orderDetails.add(orderDetail);
//				}
//			}
//
//			// 保存訂單明細
//			orderDetailsRepository.saveAll(orderDetails);
//
//			// 返回訂單結果
//			List<Integer> orderIds = orders.stream().map(Orders::getOrderId).collect(Collectors.toList());
//			return ResponseEntity.ok(Map.of("orderIds", orderIds, "totalAmount", totalAmount));
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("數據處理失敗：" + e.getMessage());
//		}
//	}

//	@GetMapping("/order/confirmation")
//	public String createPaymentForm(@RequestParam("orderIds") String orderIds,
//			@RequestParam("totalAmount") int totalAmount, @RequestParam("itemName") String itemName,
//			@RequestParam("tradeDesc") String tradeDesc, Model model) {
//		// 生成支付表單
//		String form = createPaymentForm(orderIds, totalAmount, itemName, tradeDesc);
//
//		model.addAttribute("paymentForm", form); // 將支付表單傳遞給前端
//		return "paymentForm"; // 返回到 Thymeleaf 頁面，展示支付表單
//	}

	public String createPaymentForm(String orderIds, int totalAmount, String itemName, String tradeDesc) {
		String formattedDateTime = java.time.LocalDateTime.now().toString(); // 格式化交易時間

		// 生成待加密字串
		String input = "HashKey=" + hashKey + "&ChoosePayment=Credit" + "&EncryptType=1" + "&ItemName=" + itemName
				+ "&MerchantID=" + merchantID + "&MerchantTradeDate=" + formattedDateTime + "&MerchantTradeNo="
				+ orderIds + "&PaymentType=aio" + "&ReturnURL=" + returnURL + "&TotalAmount=" + totalAmount
				+ "&TradeDesc=" + tradeDesc + "&HashIV=" + hashIV;

		// 計算檢查碼
		String checkMacValue = CheckMacValue(input);

		// 使用表單生成支付鏈接
		String form = "<form id='allPayAPIForm' action='https://payment-stage.ecPay.com.tw/Cashier/AioCheckOut/V5' method='post'>";
		form += "<input type='hidden' name='CheckMacValue' value='" + checkMacValue + "' />";
		form += "<input type='hidden' name='ChoosePayment' value='Credit' />";
		form += "<input type='hidden' name='EncryptType' value='1' />";
		form += "<input type='hidden' name='MerchantID' value='" + merchantID + "' />";
		form += "<input type='hidden' name='MerchantTradeDate' value='" + formattedDateTime + "' />";
		form += "<input type='hidden' name='MerchantTradeNo' value='" + orderIds + "' />";
		form += "<input type='hidden' name='PaymentType' value='aio' />";
		form += "<input type='hidden' name='TotalAmount' value='" + totalAmount + "' />";
		form += "<input type='hidden' name='TradeDesc' value='" + tradeDesc + "' />";
		form += "<input type='hidden' name='ItemName' value='" + itemName + "' />";
		form += "<input type='hidden' name='ReturnURL' value='" + returnURL + "' />";
		form += "<input type='submit' value='提交支付' />";
		form += "</form>";

		return form;
	}

	public String CheckMacValue(String data) {
		// URL編碼
		String replacedString = data.replace("%", "%25").replace("~", "%7e").replace("+", "%2b").replace(" ", "+")
				.replace("@", "%40").replace("#", "%23").replace("$", "%24").replace("&", "%26").replace("=", "%3d")
				.replace(";", "%3b").replace("?", "%3f").replace("/", "%2f").replace("\\", "%5c").replace(">", "%3e")
				.replace("<", "%3c").replace("`", "%60").replace("[", "%5b").replace("]", "%5d").replace("{", "%7b")
				.replace("}", "%7d").replace(":", "%3a").replace("'", "%27").replace("\"", "%22").replace(",", "%2c")
				.replace("|", "%7c");

		// 轉小寫
		replacedString = replacedString.toLowerCase();

		// 計算 SHA256
		replacedString = CalculateSHA256(replacedString);

		return replacedString;
	}

	public String CalculateSHA256(String input) {
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			StringBuilder builder = new StringBuilder();
			for (byte b : hashBytes) {
				builder.append(String.format("%02x", b));
			}
			return builder.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not found", e);
		}
	}
}