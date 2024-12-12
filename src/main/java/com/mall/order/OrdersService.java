package com.mall.order;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.cart.CheckoutRequest;
import com.mall.item.Item;
import com.mall.item.ItemRepository;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_CafeOrders;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ItemRepository itemRepository;

	public OrdersService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public List<Orders> findOrders(Integer orderId, Integer status, java.sql.Timestamp startDate,
			java.sql.Timestamp endDate) {
		return ordersRepository.findByOthers(orderId, status, startDate, endDate);
	}

	public List<Orders> getAll() {
		return ordersRepository.findAll();
	}

	public List<Orders> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_CafeOrders.getAllC(map, sessionFactory.openSession());
	}

	public Map<String, Object> getOrderDetailsWithSummary(Integer orderId) {
		// 查詢訂單的詳細內容
		List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrderId(orderId);

		// 構建詳細數據結構
		List<Map<String, Object>> details = new ArrayList<>();
		for (OrderDetails orderDetails : orderDetailsList) {
			Map<String, Object> detailMap = new HashMap<>();
			detailMap.put("quantity", orderDetails.getSalesNum());
			detailMap.put("amount", orderDetails.getPrice() * orderDetails.getSalesNum());

			// 查詢商品名稱
			String itemName = itemRepository.findById(orderDetails.getItemId()).map(Item::getName).orElse("未知商品");
			detailMap.put("itemName", itemName);

			details.add(detailMap);
		}

		// 查詢總計和備註
		Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("訂單不存在！"));
		int totalAmount = order.getAmount(); // 假設 Amount 包含運費
		String memo = order.getMemo(); // 備註

		// 返回結果
		Map<String, Object> result = new HashMap<>();
		result.put("details", details);
		result.put("totalAmount", totalAmount);
		result.put("memo", memo);

		return result;
	}

	public List<Orders> findOrdersByDynamicQuery(Map<String, String[]> parameterMap) {
		Session session = sessionFactory.openSession(); // 假設有 sessionFactory
		return HibernateUtil_CompositeQuery_CafeOrders.getAllC(parameterMap, session);
	}

	public List<Orders> getAllOrders() {
		return ordersRepository.findAll(); // 無條件時返回所有訂單
	}

	// 咖啡廳商家自己的訂單
//	public List<Orders> getAllByCafe(Integer cafeId) {
//		List<Orders> cafeOrderList = ordersRepository.findByCafeId(cafeId);
//		return cafeOrderList;
//	}

//	public List<Orders> getOrderList(String orderId, String status, String year, String month) {
//        return ordersRepository.findOrders(orderId, status, year, month);
//    }

	public void updateOrderStatus(Integer orderId, Integer status) {
		Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("訂單不存在"));
		orders.setStatus(status);
		ordersRepository.save(orders);
	}

	public List<Orders> findOrdersByCafe(Integer cafeId) {
		return ordersRepository.findByCafeId(cafeId);
	}

	public List<Orders> findOrdersByCafeAndMonth(Integer cafeId, Integer year, Integer month) {
		return ordersRepository.findByCafeIdAndMonth(cafeId, year, month);
	}

	public List<Integer> getAvailableYears(int cafeId) {
		return ordersRepository.findAvailableYears(cafeId);
	}
	
	
	//後台
	public List<Orders> findOrdersByMonth(Integer year, Integer month) {
		return ordersRepository.findByMonth(year, month);
	}

	public List<Integer> getAllAvailableYears() {
		return ordersRepository.findAllAvailableYears();
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
