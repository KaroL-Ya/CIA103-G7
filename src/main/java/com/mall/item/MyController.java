package com.mall.item;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mall.cart.CartDto;
import com.mall.cart.CartService;
import com.mall.order.Orders;
import com.mall.order.OrdersService;
import com.mall.order.RevenueData;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
public class MyController {

	@Autowired
	private CartService cartService;

	@Autowired
	ItemService itemService;

	@Autowired
	OrdersService ordersService;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	MemberService memberSvc;

	@Autowired
	HttpSession session;

	@GetMapping("/")
	public String home(ModelMap model) {
		model.addAttribute("itemList", itemService.getAll());
		return "home"; // Thymeleaf 頁面名稱
	}

//	@GetMapping("/cart")
//	public String getCart(Model model, HttpSession session) {
//		Integer memId = (Integer) session.getAttribute("mem_Id");
//		if (memId != null) {
//			model.addAttribute("memId", memId);
//			return "front-end/cart/carttest"; // 將 memId 傳遞到對應的前端頁面
//		} else {
//			model.addAttribute("error", "未登入，請先登入！");
//			return "front-end/login"; // 如果未登入，則返回到登入頁面
//		}
//	}

	@GetMapping("/cart")
	public String getCartDetails(HttpSession session, Model model) {
		Integer memId = (Integer) session.getAttribute("mem_Id");

		if (memId == null) {
			model.addAttribute("error", "請先登入");
			return "front-end/login"; // 返回登入頁面
		}

		// 獲取購物車資料
		CartDto cartDto = cartService.getCartDetails(memId);

		if (cartDto == null) {
			// 如果購物車不存在，自動初始化購物車
//			cartService.createCartForMember(memId);
			model.addAttribute("emptyCart", true);
			model.addAttribute("cart", new CartDto(null, Map.of(), 0, 0));
		} else if (cartDto.getGroupedItems().isEmpty()) {
			model.addAttribute("emptyCart", true);
			model.addAttribute("cart", cartDto);
		} else {
			model.addAttribute("emptyCart", false);
			model.addAttribute("cart", cartDto);
		}

		return "front-end/cart/carttest"; // 返回購物車頁面
	}

	@GetMapping("/item")
	public String selectPage(ModelMap model) {
		Integer cafeId = 5;
		model.addAttribute("itemList", itemService.getAllByCafe(cafeId));
		return "back-end/item/select_page"; // Thymeleaf 頁面名稱
	}

	@GetMapping("/cafe_order")
	public String cafe_orderManagement(ModelMap model) {
		Integer cafeId = 5;
//		model.addAttribute("cafeOrderList", ordersService.getAll());
		model.addAttribute("cafeOrderList", ordersService.getAllByCafe(cafeId));
		return "back-end/order/orderManagement_cafe"; // Thymeleaf 頁面名稱
	}

	@GetMapping("/cafe_money")
	public String getCafeMoney(@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month, Model model) {
		// 預設咖啡廳 ID
		Integer cafeId = 5;

		// 設置標題
		String monthlyTitle = (year != null && month != null) ? year + "年" + month + "月營收" : "總營收";
		model.addAttribute("monthlyTitle", monthlyTitle);

		// 查詢訂單數據
		List<Orders> orders = (year != null && month != null)
				? ordersService.findOrdersByCafeAndMonth(cafeId, year, month) // 查詢年月
				: ordersService.findOrdersByCafe(cafeId); // 總營收

		// 計算統計數據
		int orderCount = (int) orders.stream().filter(o -> o.getStatus() == 2).count();
		int totalAmount = orders.stream().filter(o -> o.getStatus() == 2).mapToInt(Orders::getAmount).sum();
		double platformFee = totalAmount * 0.08;
		double revenue = totalAmount - platformFee;

		// 格式化金額（四捨五入）
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedTotalAmount = "$" + decimalFormat.format(totalAmount);
		String formattedPlatformFee = "$" + decimalFormat.format(platformFee);
		String formattedRevenue = "$" + decimalFormat.format(revenue);

		// 獲取年份範圍
		List<Integer> years = ordersService.getAvailableYears(cafeId);
		model.addAttribute("years", years);

		// 當前年份和月份（預設數據）
		int currentYear = LocalDate.now().getYear();
		int currentMonth = LocalDate.now().getMonthValue();
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("currentMonth", currentMonth);

		// 添加數據到模型
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("totalAmount", formattedTotalAmount); // 格式化後的金額
		model.addAttribute("platformFee", formattedPlatformFee); // 格式化後的金額
		model.addAttribute("revenue", formattedRevenue); // 格式化後的金額
		model.addAttribute("orders", orders);

		return "back-end/order/cafe_money";
	}

	// 後台管理
	@GetMapping("/admin_order")
	public String admin_orderManagement(ModelMap model) {
		model.addAttribute("OrderList", ordersService.getAll());
//		model.addAttribute("cafeOrderList", ordersService.getAllByCafe());
		return "back-end/order/orderManagement_admin"; // Thymeleaf 頁面名稱
	}

	@GetMapping("/admin_money")
	public String getAdminMoney(@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month, Model model) {

		// 設置標題
		String monthlyTitle = (year != null && month != null) ? year + "年" + month + "月營收" : "總營收";
		model.addAttribute("monthlyTitle", monthlyTitle);

		// 查詢訂單數據
		List<Orders> orders = (year != null && month != null) ? ordersService.findOrdersByMonth(year, month) // 查詢年月
				: ordersService.getAllOrders(); // 總營收

		// 計算統計數據
		int orderCount = (int) orders.stream().filter(o -> o.getStatus() == 2).count();
		int totalAmount = orders.stream().filter(o -> o.getStatus() == 2).mapToInt(Orders::getAmount).sum();
		double platformFee = totalAmount * 0.08;
		double revenue = totalAmount - platformFee;

		// 格式化金額（四捨五入）
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		String formattedTotalAmount = "$" + decimalFormat.format(totalAmount);
		String formattedPlatformFee = "$" + decimalFormat.format(platformFee);
		String formattedRevenue = "$" + decimalFormat.format(revenue);

		// 獲取年份範圍
		List<Integer> years = ordersService.getAllAvailableYears();
		model.addAttribute("years", years);

		// 當前年份和月份（預設數據）
		int currentYear = LocalDate.now().getYear();
		int currentMonth = LocalDate.now().getMonthValue();
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("currentMonth", currentMonth);

		// 添加數據到模型
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("totalAmount", formattedTotalAmount); // 格式化後的金額
		model.addAttribute("platformFee", formattedPlatformFee); // 格式化後的金額
		model.addAttribute("revenue", formattedRevenue); // 格式化後的金額
		model.addAttribute("orders", orders);

		return "back-end/order/admin_money";
	}

	@GetMapping("/coverImg")
	public ResponseEntity<byte[]> getCoverImage(@RequestParam("itemId") Integer itemId) {

		byte[] imageData = itemRepository.findCoverImgByItemId(itemId);

		if (imageData != null && imageData.length > 0) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // 或 MediaType.IMAGE_PNG
			return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
