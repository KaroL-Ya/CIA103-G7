package com.mall.order;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mall.item.Item;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

	// ● (自訂)條件查詢
	@Query(value = "FROM Orders o WHERE " + "(:orderId IS NULL OR o.orderId = :orderId) AND "
			+ "(:status IS NULL OR o.status = :status) AND "
			+ "((:startDate IS NULL AND :endDate IS NULL) OR (o.date BETWEEN :startDate AND :endDate)) "
			+ "ORDER BY o.orderId")
	List<Orders> findByOthers(@Param("orderId") Integer orderId, @Param("status") Integer status,
			@Param("startDate") java.sql.Timestamp startDate, @Param("endDate") java.sql.Timestamp endDate);

	@Query("SELECT o FROM Orders o WHERE o.cafeId = :cafeId")
	List<Orders> findByCafeId(@Param("cafeId") Integer cafeId);

	@Query("SELECT o FROM Orders o WHERE o.cafeId = :cafeId AND YEAR(o.date) = :year AND MONTH(o.date) = :month")
	List<Orders> findByCafeIdAndMonth(@Param("cafeId") Integer cafeId, @Param("year") Integer year,
			@Param("month") Integer month);

	@Query("SELECT DISTINCT YEAR(o.date) FROM Orders o WHERE o.cafeId = :cafeId ORDER BY YEAR(o.date) ASC")
	List<Integer> findAvailableYears(@Param("cafeId") int cafeId);
	
	//後台
		@Query("SELECT o FROM Orders o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month")
		List<Orders> findByMonth(@Param("year") Integer year,
				@Param("month") Integer month);

		@Query("SELECT DISTINCT YEAR(o.date) FROM Orders o ORDER BY YEAR(o.date) ASC")
		List<Integer> findAllAvailableYears();

	// 根據訂單id查詢商品，確保每間咖啡廳只能看到自己的訂單
//	@Query(value = "from orders where CAFE_ID like?1 order by ORDER_ID")
//	List<Orders> findByOrderId(Integer orderId);

//	 @Query("SELECT DISTINCT FUNCTION('YEAR', o.date) FROM Orders o ORDER BY FUNCTION('YEAR', o.date) DESC")
//	    List<String> findAvailableYears();
//
//	    @Query("SELECT DISTINCT FUNCTION('MONTH', o.date) FROM Orders o ORDER BY FUNCTION('MONTH', o.date) ASC")
//	    List<String> findAvailableMonths();
//
//	    @Query("SELECT o FROM Orders o WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
//	            "AND (:status IS NULL OR o.status = :status) " +
//	            "AND (:year IS NULL OR FUNCTION('YEAR', o.date) = :year) " +
//	            "AND (:month IS NULL OR FUNCTION('MONTH', o.date) = :month) ")
//	    List<Orders> findOrders(String orderId, String status, String year, String month);

}
