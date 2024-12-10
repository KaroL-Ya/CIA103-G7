package com.mall.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	
	 @Query("SELECT DISTINCT FUNCTION('YEAR', o.date) FROM Orders o ORDER BY FUNCTION('YEAR', o.date) DESC")
	    List<String> findAvailableYears();

	    @Query("SELECT DISTINCT FUNCTION('MONTH', o.date) FROM Orders o ORDER BY FUNCTION('MONTH', o.date) ASC")
	    List<String> findAvailableMonths();

	    @Query("SELECT o FROM Orders o WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
	            "AND (:status IS NULL OR o.status = :status) " +
	            "AND (:year IS NULL OR FUNCTION('YEAR', o.date) = :year) " +
	            "AND (:month IS NULL OR FUNCTION('MONTH', o.date) = :month) ")
	    List<Orders> findOrders(String orderId, String status, String year, String month);


}
