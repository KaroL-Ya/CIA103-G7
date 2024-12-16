package com.mall.memOrder;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service("memOrderService")
public class MemOrderService {
	
	@Autowired
	MemOrderRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void updateMemOrder(MemOrderVO memOrderVO) {
		repository.save(memOrderVO);
	}
	
	public List<MemOrderVO> getALl() {
		
		return repository.findAll();
	}
	public List<MemOrderVO> getOrdersByMemId(Integer memId) {
		return repository.findByMemId(memId);
	}

	  // 根據訂單ID查詢單個訂單
    public MemOrderVO getOneOrder(Integer orderId) {
        // 使用repository查詢訂單，並返回Optional
        Optional<MemOrderVO> memOrder = repository.findById(orderId);

        return memOrder.orElse(null);
    }
    
    
    public List<OrderDetailVO> findByOrderId(Integer orderId) {
		return repository.findByOrderId(orderId);
	}
   
    }



