package com.mall.cart;

import java.util.List;
import java.util.Map;

public class CartDto {
	private Cart cart;
	private Map<Integer, List<CartItemDto>> groupedItems; // 按咖啡廳分組的商品
	private Integer totalAmount; // 購物車總金額
	private Integer cartItemCount; // 新增屬性

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	// 定義構造函數
	public CartDto(Cart cart, Map<Integer, List<CartItemDto>> groupedItems, Integer totalAmount,
			Integer cartItemCount) {
		this.cart = cart;
		this.groupedItems = groupedItems;
		this.totalAmount = totalAmount;
		this.cartItemCount = cartItemCount;

	}

	// Getter 和 Setter
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Map<Integer, List<CartItemDto>> getGroupedItems() {
		return groupedItems;
	}

	public void setGroupedItems(Map<Integer, List<CartItemDto>> groupedItems) {
		this.groupedItems = groupedItems;
	}

	public Integer getCartItemCount() {
		return cartItemCount;
	}

	public void setCartItemCount(Integer cartItemCount) {
		this.cartItemCount = cartItemCount;
	}

}
