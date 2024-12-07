package com.mall.cart;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CartService {
	CartDto getCartDetails(Integer memId); // 獲取購物車詳情

	void updateNum(Integer cartId, Integer itemId, Integer num); // 更新數量
	
}
