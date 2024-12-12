package com.mall.order;

public class RevenueData {
	private int orderCount;
	private int totalAmount;
	private double platformFee;
	private double revenue;

	// Constructor, Getter and Setter
	public RevenueData(int orderCount, int totalAmount, double platformFee, double revenue) {
		this.setOrderCount(orderCount);
		this.setTotalAmount(totalAmount);
		this.setPlatformFee(platformFee);
		this.setRevenue(revenue);
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getPlatformFee() {
		return platformFee;
	}

	public void setPlatformFee(double platformFee) {
		this.platformFee = platformFee;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

}
