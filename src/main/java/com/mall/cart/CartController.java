package com.mall.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	@Autowired
	private CartServiceImpl cartServiceImpl;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	// 模擬登入 (開發測試)
	private void simulateLogin(HttpSession session) {
		// 將 memId 設置為 1
		Integer memId = 1; // 假設 memId 是 1
		session.setAttribute("memId", memId); // 將用戶 ID 存儲在 session 中
	}

	// 獲取購物車詳細信息
	@GetMapping("/{memId}")
	public String getCartDetails(@PathVariable Integer memId, Model model, HttpSession session) {

		// 在開發測試階段模擬用戶已經登入
		simulateLogin(session);

		// 從 session 中獲取 memId
		Integer loggedInMemId = (Integer) session.getAttribute("memId");
		if (loggedInMemId == null) {
			return "error"; // 如果 session 中找不到 memId，表示未登入
		}

		CartDto cartDto = cartService.getCartDetails(memId);

		if (cartDto.getCart() == null || cartDto.getGroupedItems().isEmpty()) {
			model.addAttribute("cart", new CartDto(null, Map.of(), 0, 0)); // 返回空的 CartDto
			model.addAttribute("emptyCart", true); // 添加空購物車旗標
		} else {
			model.addAttribute("cart", cartDto);
			model.addAttribute("emptyCart", false); // 設置購物車非空
		}

		return "cart/carttest"; // 返回 Thymeleaf 模板名稱
	}

	// 更新商品數量
	@PostMapping("/{memId}/updateNum")
	public ResponseEntity<?> updateNum(@PathVariable("memId") Integer memId,
			@RequestBody Map<String, Integer> requestData) {
		Integer itemId = requestData.get("itemId");
		Integer num = requestData.get("num");

		// 檢查參數是否有效
		if (itemId == null || num == null || num <= 0) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemId 或 num 無效"));
		}

		// 更新商品數量
		boolean success = updateItemNumInDatabase(memId, itemId, num);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "更新成功"));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "更新失敗"));
		}
	}

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

	// 刪除選中的商品
	@DeleteMapping("/{memId}/removeSelectedItems")
	public ResponseEntity<?> removeSelectedItems(@PathVariable("memId") Integer memId,
			@RequestBody Map<String, List<Integer>> requestData) {
		List<Integer> itemIds = requestData.get("itemIds");

		if (itemIds == null || itemIds.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemIds 不能為空"));
		}

		try {
			// 獲取購物車 ID
			Integer cartId = getCartIdByMemId(memId);
			if (cartId == null) {
				return ResponseEntity.status(404).body(Map.of("success", false, "message", "未找到該會員的購物車"));
			}

			// 批量刪除商品
			Integer deletedCount = cartItemRepository.deleteByItemIds(cartId, itemIds);
			if (deletedCount == 0) {
				return ResponseEntity.status(404).body(Map.of("success", false, "message", "未找到匹配的商品"));
			}

			return ResponseEntity.ok(Map.of("success", true, "message", "批量移除成功", "deletedCount", deletedCount));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "批量移除失敗：" + e.getMessage()));
		}
	}

}
