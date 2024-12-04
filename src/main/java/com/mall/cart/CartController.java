package com.mall.cart;

import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@GetMapping("/{memId}")
	public String getCartDetails(@PathVariable Integer memId, Model model) {
	    CartDto cartDto = cartService.getCartDetails(memId);

	    if (cartDto.getCart() == null || cartDto.getGroupedItems().isEmpty()) {
	        model.addAttribute("cart", new CartDto(null, Map.of(), 0, 0)); // 返回空的 CartDto
	        model.addAttribute("emptyCart", true); // 添加一個空購物車的旗標
	    } else {
	        model.addAttribute("cart", cartDto);
	        model.addAttribute("emptyCart", false); // 設置購物車非空
	    }

	    return "cart/carttest"; // 返回 Thymeleaf 模板名稱
	}


	@PostMapping("/{memId}/updateNum")
	public ResponseEntity<?> updateNum(@PathVariable("memId") Integer memId,
			@RequestBody Map<String, Integer> requestData) {
		Integer itemId = requestData.get("itemId");
		Integer num = requestData.get("num");

		if (itemId == null || num == null) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "itemId 或 num 不能為空"));
		}

		// 更新邏輯
		boolean success = updateItemNumInDatabase(memId, itemId, num);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "更新成功"));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "更新失敗"));
		}
	}

	@Transactional
	private boolean updateItemNumInDatabase(Integer memId, Integer itemId, Integer num) {
		try {
			// 假設 memId 與 cartId 一對一對應
			Integer cartId = getCartIdByMemId(memId);

			if (cartId == null) {
				throw new RuntimeException("找不到對應的購物車");
			}

			cartItemRepository.updateNum(cartId, itemId, num);
			return true;
		} catch (Exception e) {
			System.err.println("更新失敗：" + e.getMessage());
			return false;
		}
	}

	private Integer getCartIdByMemId(Integer memId) {
		return cartRepository.findByMemId(memId).map(Cart::getCartId).orElse(null);
	}

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
