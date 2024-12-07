package com.mall.cart;

import java.util.List;
import java.util.Map;

public class ProcessResult {
    private Map<Integer, List<CartItemDto>> groupedItems;
    private Map<Integer, Integer> totalAmounts;
	public Map<Integer, List<CartItemDto>> getGroupedItems() {
		return groupedItems;
	}
	public void setGroupedItems(Map<Integer, List<CartItemDto>> groupedItems) {
		this.groupedItems = groupedItems;
	}
	public Map<Integer, Integer> getTotalAmounts() {
		return totalAmounts;
	}
	public void setTotalAmounts(Map<Integer, Integer> totalAmounts) {
		this.totalAmounts = totalAmounts;
	}

    // Getters and Setters
}
