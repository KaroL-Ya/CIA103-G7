package com.mall.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.item.Item;
import com.mall.item.ItemRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public CartDto getCartDetails(Integer memId) {
		// 查詢購物車
		Cart cart = cartRepository.findByMemId(memId).orElse(null);

		if (cart == null) {
			return new CartDto(null, Map.of(), 0, 0); // 空購物車返回
		}

		// 查詢購物車項目
		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

		// 分組處理：按 CafeId 分組
		Map<Integer, List<CartItemDto>> groupedItems = cartItems.stream().map(item -> {
			CartItemDto dto = new CartItemDto();
			dto.setCafeId(item.getCafeId());
			dto.setNum(item.getNum());

			// 從 Item 表中抓取商品數據
			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
					.orElseThrow(() -> new RuntimeException("商品未找到，商品編號：" + item.getItemId().getItemId()));

			// 設置商品詳細信息
			dto.setItemId(itemEntity.getItemId());
			dto.setName(itemEntity.getName());
			dto.setPrice(itemEntity.getPrice());
			dto.setTotalPrice(itemEntity.getPrice() * item.getNum());
			dto.setCoverImg(itemEntity.getCoverImg() != null ? itemEntity.getCoverImg() : new byte[0]); // 防止圖片為空
			dto.setMaxNum(itemEntity.getNum()); // 設置商品最大數量（從 Item 表中）
			return dto;
		}).collect(Collectors.groupingBy(CartItemDto::getCafeId)); // 按 CafeId 分組

		// 計算總金額
		int totalAmount = groupedItems.values().stream().flatMap(List::stream).mapToInt(CartItemDto::getTotalPrice)
				.sum();

		// 計算商品總數量
		int cartItemCount = cartItems.stream().mapToInt(CartItem::getNum).sum();

		// 返回組裝好的 CartDto
		return new CartDto(cart, groupedItems, totalAmount, cartItemCount);
	}

	
	@Override
	@Transactional
	public void updateNum(Integer cartId, Integer itemId, Integer num) {
	    cartItemRepository.updateNum(cartId, itemId, num);
	}


}
