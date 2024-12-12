package com.mall.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetails.OrderDetailsId> {

	// 根據訂單 ID 查詢所有明細
	@Query("SELECT od FROM OrderDetails od WHERE od.orderDetailsId.orderId = :orderId")
	List<OrderDetails> findByOrderId(@Param("orderId") Integer orderId);
	
	List<OrderDetails> findByOrderDetailsIdOrderId(Integer orderId);

//	@Query("SELECT o FROM OrderDetails o WHERE o.orderDetailsId.orderId = :orderId AND o.orderDetailsId.itemId = :itemId")
//    List<OrderDetails> findByOrderIdAndItemId(@Param("orderId") Integer orderId, @Param("itemId") Integer itemId);
}
