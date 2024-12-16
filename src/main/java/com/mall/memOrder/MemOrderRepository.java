package com.mall.memOrder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemOrderRepository extends JpaRepository<MemOrderVO, Integer>{

	public List<MemOrderVO> findByMemId(Integer memId); 
	
	@Query(value = "from OrderDetailVO where orderId=?1")
	public List<OrderDetailVO> findByOrderId(Integer orderId); 
	


}
