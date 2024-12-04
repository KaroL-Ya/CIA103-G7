package com.mall.cart;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cartitem")
public class CartItem {

	@EmbeddedId
	private CartItemId itemId; // 包含 cartId 和 itemId 的複合主鍵

	@Column(name = "cafe_id", nullable = false)
	private Integer cafeId;

	@Column(name = "num", nullable = false)
	private Integer num;

	// Getters and Setters

	@Embeddable
	public static class CartItemId implements Serializable {
		@Column(name = "cart_id")
		private Integer cartId;

		@Column(name = "item_id")
		private Integer itemId;

		// Getters, Setters, equals(), and hashCode()

		public CartItemId() {
		}

		public CartItemId(Integer cartId, Integer itemId) {
			this.cartId = cartId;
			this.itemId = itemId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			CartItemId that = (CartItemId) o;
			return Objects.equals(cartId, that.cartId) && Objects.equals(itemId, that.itemId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(cartId, itemId);
		}

		public Integer getCartId() {
			return cartId;
		}

		public void setCartId(Integer cartId) {
			this.cartId = cartId;
		}

		public Integer getItemId() {
			return itemId;
		}

		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}

	}

	public CartItemId getItemId() {
		return itemId;
	}

	public void setItemId(CartItemId itemId) {
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

}
