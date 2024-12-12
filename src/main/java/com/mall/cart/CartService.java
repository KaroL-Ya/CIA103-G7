package com.mall.cart;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface CartService {
	CartDto getCartDetails(Integer memId); // 獲取購物車詳情

//	public void createCartForMember(Integer memId);

	void updateNum(Integer cartId, Integer itemId, Integer num); // 更新數量

	public String addItemToCart(Integer memId, Integer itemId, Integer num);

	void removePurchasedItems(Integer cartId, List<Integer> purchasedItemIds);
}
