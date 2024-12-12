package com.mall.cart;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@PostMapping("/addCart")
	public String addItemToCart(HttpSession session, @RequestParam Integer itemId, @RequestParam Integer num,
			Model model) {
		Integer memId = (Integer) session.getAttribute("mem_Id");

		// 驗證會員是否登入
		if (memId == null) {
			return "front-end/login"; // 返回登入頁面
		}

		// 驗證數量是否有效
		if (num == null || num <= 0) {
			model.addAttribute("error", "購買數量必須大於 0！");
			return "redirect:/"; // 返回商品列表頁面
		}

		// 添加商品到購物車，接收返回的提示信息
		String resultMessage = cartService.addItemToCart(memId, itemId, num);

		// 將提示信息返回到前端
		model.addAttribute("message", resultMessage);

		return "redirect:/"; // 返回商品列表頁面
	}

//	@GetMapping("/cart")
//	public String getCartDetails(HttpSession session, Model model) {
//
//		Integer memId = (Integer) session.getAttribute("mem_Id");
//		if (memId == null) {
//			return "front-end/login"; // 如果 session 中沒有 memId，則重定向到錯誤頁面或登入頁面
//		}
//
//		CartDto cartDto = cartService.getCartDetails(memId);
//
//		if (cartDto == null || cartDto.getCart() == null || cartDto.getGroupedItems() == null
//				|| cartDto.getGroupedItems().isEmpty()) {
//			// 返回一個空的購物車物件，避免 Thymeleaf 渲染錯誤
//			model.addAttribute("cart", new CartDto(null, Map.of(), 0, 0));
//			model.addAttribute("emptyCart", true); // 標記購物車為空
//		} else {
//			model.addAttribute("cart", cartDto);
//			model.addAttribute("emptyCart", false); // 標記購物車不為空
//		}
//
//		return "front-end/cart/carttest"; // 返回 Thymeleaf 頁面
//	}

	@PostMapping("/updateNum")
	public ResponseEntity<?> updateNum(HttpSession session, @RequestBody Map<String, Integer> requestData) {
		Integer memId = (Integer) session.getAttribute("mem_Id");
		if (memId == null) {
			return ResponseEntity.status(401).body(Map.of("success", false, "message", "用戶未登入"));
		}

		Integer itemId = requestData.get("itemId");
		Integer num = requestData.get("num");

		if (itemId == null || num == null || num <= 0) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemId 或 num 無效"));
		}

		boolean success = updateItemNumInDatabase(memId, itemId, num);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "更新成功"));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "更新失敗"));
		}
	}

	@DeleteMapping("/removeSelectedItems")
	public ResponseEntity<?> removeSelectedItems(HttpSession session,
			@RequestBody Map<String, List<Integer>> requestData) {
		Integer memId = (Integer) session.getAttribute("mem_Id");
		if (memId == null) {
			return ResponseEntity.status(401).body(Map.of("success", false, "message", "用戶未登入"));
		}

		List<Integer> itemIds = requestData.get("itemIds");

		if (itemIds == null || itemIds.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemIds 不能為空"));
		}

		try {
			Integer cartId = getCartIdByMemId(memId);
			if (cartId == null) {
				return ResponseEntity.status(404).body(Map.of("success", false, "message", "未找到該會員的購物車"));
			}

			Integer deletedCount = cartItemRepository.deleteByItemIds(cartId, itemIds);
			return ResponseEntity.ok(Map.of("success", true, "message", "批量移除成功", "deletedCount", deletedCount));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "批量移除失敗：" + e.getMessage()));
		}
	}

//	// 獲取購物車詳細信息
//	@GetMapping("/{mem_Id}")
//	public String getCartDetails(@PathVariable Integer memId, Model model, HttpSession session) {
//
//		// 從 session 中獲取 memId
//		Integer loggedInMemId = (Integer) session.getAttribute("mem_Id");
//		if (loggedInMemId == null) {
//			return "error"; // 如果 session 中找不到 memId，表示未登入
//		}
//
//		CartDto cartDto = cartService.getCartDetails(memId);
//
//		if (cartDto.getCart() == null || cartDto.getGroupedItems().isEmpty()) {
//			model.addAttribute("cart", new CartDto(null, Map.of(), 0, 0)); // 返回空的 CartDto
//			model.addAttribute("emptyCart", true); // 添加空購物車旗標
//		} else {
//			model.addAttribute("cart", cartDto);
//			model.addAttribute("emptyCart", false); // 設置購物車非空
//		}
//
//		return "front-end/cart/carttest"; // 返回 Thymeleaf 模板名稱
//	}

//	// 更新商品數量
//	@PostMapping("/{memId}/updateNum")
//	public ResponseEntity<?> updateNum(@PathVariable("memId") Integer memId,
//			@RequestBody Map<String, Integer> requestData) {
//		Integer itemId = requestData.get("itemId");
//		Integer num = requestData.get("num");
//
//		// 檢查參數是否有效
//		if (itemId == null || num == null || num <= 0) {
//			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemId 或 num 無效"));
//		}
//
//		// 更新商品數量
//		boolean success = updateItemNumInDatabase(memId, itemId, num);
//		if (success) {
//			return ResponseEntity.ok(Map.of("success", true, "message", "更新成功"));
//		} else {
//			return ResponseEntity.status(500).body(Map.of("success", false, "message", "更新失敗"));
//		}
//	}

	// 更新商品數量的具體邏輯
	@Transactional
	private boolean updateItemNumInDatabase(Integer memId, Integer itemId, Integer num) {
		try {
			// 根據 memId 查找 cartId
			Integer cartId = getCartIdByMemId(memId);
			if (cartId == null) {
				throw new RuntimeException("找不到對應的購物車");
			}

			// 更新商品數量
			cartItemRepository.updateNum(cartId, itemId, num);
			return true;
		} catch (Exception e) {
			System.err.println("更新失敗：" + e.getMessage());
			return false;
		}
	}

	// 根據 memId 查找對應的 cartId
	private Integer getCartIdByMemId(Integer memId) {
		return cartRepository.findByMemId(memId).map(Cart::getCartId).orElse(null);
	}

//	// 刪除選中的商品
//	@DeleteMapping("/{memId}/removeSelectedItems")
//	public ResponseEntity<?> removeSelectedItems(@PathVariable("memId") Integer memId,
//			@RequestBody Map<String, List<Integer>> requestData) {
//		List<Integer> itemIds = requestData.get("itemIds");
//
//		if (itemIds == null || itemIds.isEmpty()) {
//			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemIds 不能為空"));
//		}
//
//		try {
//			// 獲取購物車 ID
//			Integer cartId = getCartIdByMemId(memId);
//			if (cartId == null) {
//				return ResponseEntity.status(404).body(Map.of("success", false, "message", "未找到該會員的購物車"));
//			}
//
//			// 批量刪除商品
//			Integer deletedCount = cartItemRepository.deleteByItemIds(cartId, itemIds);
//			if (deletedCount == 0) {
//				return ResponseEntity.status(404).body(Map.of("success", false, "message", "未找到匹配的商品"));
//			}
//
//			return ResponseEntity.ok(Map.of("success", true, "message", "批量移除成功", "deletedCount", deletedCount));
//		} catch (Exception e) {
//			return ResponseEntity.status(500).body(Map.of("success", false, "message", "批量移除失敗：" + e.getMessage()));
//		}
//	}

}
