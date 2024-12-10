package com.mall.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.cart.CheckoutRequest;
import com.mall.item.ItemRepository;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private ItemRepository itemRepository;

	public OrdersService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public List<Orders> getOrderList(String orderId, String status, String year, String month) {
        return ordersRepository.findOrders(orderId, status, year, month);
    }

    public List<String> getAvailableYears() {
        return ordersRepository.findAvailableYears();
    }

    public List<String> getAvailableMonths() {
        return ordersRepository.findAvailableMonths();
    }

    public void updateOrderStatus(Integer orderId, int status) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("訂單不存在"));
        order.setStatus(status);
        ordersRepository.save(order);
    }

	// 計算訂單總額
	public Integer calculateTotalAmount(List<CheckoutRequest.CartItemRequest> items) {
		int totalAmount = 0;
		for (CheckoutRequest.CartItemRequest item : items) {
			int itemTotalPrice = item.getPrice() * item.getNum(); // 計算單個商品的總價
			totalAmount += itemTotalPrice; // 累加總額
		}
		return totalAmount;
	}


}
