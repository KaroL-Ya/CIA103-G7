package com.mall.order;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ORDER_DETAILS")
public class OrderDetails {

	@EmbeddedId
	private OrderDetailsId orderDetailsId; // 使用嵌合鍵作為主鍵

	@Column(name = "SALESNUM", nullable = false)
	private Integer salesNum; // 購買數量

	@Column(name = "PRICE", nullable = false)
	private Integer price; // 單價

	@Column(name = "IS_RETURN", nullable = false)
	private Integer isReturn; // 是否退貨

	@Column(name = "RETURN_REASON")
	private Integer returnReason; // 退貨原因

	@Column(name = "RETURN_IMG")
	private byte[] returnImg; // 退貨照片

	// 默認構造函數
	public OrderDetails() {
	}

	// 帶參構造函數
	public OrderDetails(OrderDetailsId orderDetailsId, Integer salesNum, Integer price, Integer isReturn,
			Integer returnReason, byte[] returnImg) {
		this.orderDetailsId = orderDetailsId;
		this.salesNum = salesNum;
		this.price = price;
		this.isReturn = isReturn;
		this.returnReason = returnReason;
		this.returnImg = returnImg;
	}

	// Getter 和 Setter
	public OrderDetailsId getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(OrderDetailsId orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Integer isReturn) {
		this.isReturn = isReturn;
	}

	public Integer getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(Integer returnReason) {
		this.returnReason = returnReason;
	}

	public byte[] getReturnImg() {
		return returnImg;
	}

	public void setReturnImg(byte[] returnImg) {
		this.returnImg = returnImg;
	}

	// 複合主鍵類別 OrderDetailsId（嵌合鍵）
	public static class OrderDetailsId implements Serializable {

		private static final long serialVersionUID = 1L;

		@Column(name = "ORDER_ID")
		private Integer orderId;

		@Column(name = "ITEM_ID")
		private Integer itemId;

		// 默認構造函數
		public OrderDetailsId() {
		}

		// 帶參構造函數
		public OrderDetailsId(Integer orderId, Integer itemId) {
			this.orderId = orderId;
			this.itemId = itemId;
		}

		// Getter 和 Setter
		public Integer getOrderId() {
			return orderId;
		}

		public void setOrderId(Integer orderId) {
			this.orderId = orderId;
		}

		public Integer getItemId() {
			return itemId;
		}

		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}

		// 覆寫 equals() 和 hashCode() 方法（必須）
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			OrderDetailsId that = (OrderDetailsId) o;
			return Objects.equals(orderId, that.orderId) && Objects.equals(itemId, that.itemId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(orderId, itemId);
		}
	}
}
