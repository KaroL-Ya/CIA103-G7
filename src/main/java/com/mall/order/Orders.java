package com.mall.order;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ORDERS") // 對應資料表 ORDERS
public class Orders {

	// 訂單編號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成訂單編號
	@Column(name = "ORDER_ID", nullable = false)
	private Integer orderId;

	// 會員編號
	@Column(name = "MEM_ID", nullable = false)
	private Integer memId;

	// 咖啡廳編號
	@Column(name = "CAFE_ID", nullable = false)
	private Integer cafeId;

	// 訂單日期
	@Column(name = "DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	// 總金額
	@Column(name = "AMOUNT", nullable = false)
	private Integer amount;

	// 平台優惠券編號
	@Column(name = "PCO_ID")
	private Integer platformCoupon;

	// 商家優惠券編號
	@Column(name = "CCO_ID")
	private Integer cafeCoupon;

	// 優惠金額
	@Column(name = "DISCOUNT")
	private Integer discount;

	// 實付金額
	@Column(name = "PAID", nullable = false)
	private Integer paid;

	// 訂單備註
	@Column(name = "MEMO", length = 100)
	private String memo;

	// 出貨日期
	@Column(name = "SHIPDATE")
	@Temporal(TemporalType.DATE) // 只會儲存日期部分（年、月、日）
	private Date shipDate;

	// 貨運追蹤號碼
	@Column(name = "TRACKNO", length = 50)
	private String trackNo;

	// 訂單狀態
	@Column(name = "STATUS", nullable = false)
	private Integer status;

	// Constructor
	public Orders() {
		super();
	}

	public Orders(Integer orderId, Integer memId, Integer cafeId, Date date, Integer amount, Integer platformCoupon,
			Integer cafeCoupon, Integer discount, Integer paid, String memo, Date shipDate, String trackNo,
			Integer status) {
		super();
		this.orderId = orderId;
		this.memId = memId;
		this.cafeId = cafeId;
		this.date = date;
		this.amount = amount;
		this.platformCoupon = platformCoupon;
		this.cafeCoupon = cafeCoupon;
		this.discount = discount;
		this.paid = paid;
		this.memo = memo;
		this.shipDate = shipDate;
		this.trackNo = trackNo;
		this.status = status;
	}

	// Getter and Setter
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getCafeId() {
		return cafeId;
	}

	public void setCafeId(Integer cafeId) {
		this.cafeId = cafeId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getPlatformCoupon() {
		return platformCoupon;
	}

	public void setPlatformCoupon(Integer platformCoupon) {
		this.platformCoupon = platformCoupon;
	}

	public Integer getCafeCoupon() {
		return cafeCoupon;
	}

	public void setCafeCoupon(Integer cafeCoupon) {
		this.cafeCoupon = cafeCoupon;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public String getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
