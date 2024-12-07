package com.mall.cart;

import java.util.List;

public class CheckoutRequest {
	private Integer memId; // 會員 ID
	private List<Integer> itemIds; // 商品 ID 列表

	// Getter 和 Setter

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	// Getter 和 Setter
	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	// 靜態內部類，表示商品請求
	public static class CartItemRequest {
		private CartItem.CartItemId itemId; // 嵌套的 CartItemId
		private Integer cafeId; // 咖啡廳 ID
		private Integer num; // 數量
		private Integer price; // 單價

		// Getter 和 Setter
		public CartItem.CartItemId getItemId() {
			return itemId;
		}

		public void setItemId(CartItem.CartItemId itemId) {
			this.itemId = itemId;
		}

		public Integer getCafeId() {
			return cafeId;
		}

		public void setCafeId(Integer cafeId) {
			this.cafeId = cafeId;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num = num;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}
	}
}
