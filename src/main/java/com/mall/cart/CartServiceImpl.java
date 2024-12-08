package com.mall.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.item.Item;
import com.mall.item.ItemRepository;

import java.util.*;
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

	// 獲取購物車詳細信息
	@Override
	public CartDto getCartDetails(Integer memId) {
		Cart cart = cartRepository.findByMemId(memId).orElse(null);

		if (cart == null) {
			return new CartDto(null, Map.of(), 0, 0); // 空購物車返回
		}

		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

		// 按 CafeId 分組
		Map<Integer, List<CartItemDto>> groupedItems = cartItems.stream().map(item -> {
			CartItemDto dto = new CartItemDto();
			dto.setCafeId(item.getCafeId());
			dto.setNum(item.getNum());

			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
					.orElseThrow(() -> new RuntimeException("商品未找到，商品編號：" + item.getItemId().getItemId()));

			dto.setItemId(itemEntity.getItemId());
			dto.setName(itemEntity.getName());
			dto.setPrice(itemEntity.getPrice());
			dto.setTotalPrice(itemEntity.getPrice() * item.getNum());
			dto.setCoverImg(itemEntity.getCoverImg() != null ? itemEntity.getCoverImg() : new byte[0]);
			dto.setMaxNum(itemEntity.getNum());

			return dto;
		}).collect(Collectors.groupingBy(CartItemDto::getCafeId));

		// 計算總金額
		int totalAmount = groupedItems.values().stream().flatMap(List::stream).mapToInt(CartItemDto::getTotalPrice)
				.sum();

		// 計算商品總數量
		int cartItemCount = cartItems.stream().mapToInt(CartItem::getNum).sum();

		return new CartDto(cart, groupedItems, totalAmount, cartItemCount);
	}

	// 更新商品數量
	@Override
	public void updateNum(Integer cartId, Integer itemId, Integer num) {
		cartItemRepository.updateNum(cartId, itemId, num);
	}

	// 根據會員ID獲取購物車
	public Cart getCartByMemberId(Integer memId) {
		return cartRepository.findByMemId(memId).orElseThrow(() -> new RuntimeException("Cart not found"));
	}

	// 根據會員ID和勾選商品ID，獲取分組的商品
	public Map<Integer, List<CartItemDto>> getSelectedItems(Integer memId, List<Integer> selectedItemIds) {
		// 查詢會員購物車
		Cart cart = cartRepository.findByMemId(memId).orElseThrow(() -> new RuntimeException("Cart not found"));
		System.out.println("CartId: " + cart.getCartId());
		System.out.println("Selected Item IDs: " + selectedItemIds);

		// 查詢選中的商品
		List<CartItem> selectedItems = cartItemRepository.findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(),
				selectedItemIds);
		System.out.println("Selected Items: " + selectedItems);

		if (selectedItems == null || selectedItems.isEmpty()) {
			throw new RuntimeException("No items found for the given selection.");
		}

		// 分組：按 CafeId 分組
		Map<Integer, List<CartItemDto>> groupedItems = selectedItems.stream().map(item -> {
			CartItemDto dto = new CartItemDto();
			dto.setCafeId(item.getCafeId());
			dto.setNum(item.getNum());

			// 查找商品詳細信息
			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
					.orElseThrow(() -> new RuntimeException("Item not found for ID: " + item.getItemId().getItemId()));
			dto.setItemId(itemEntity.getItemId());
			dto.setName(itemEntity.getName());
			dto.setPrice(itemEntity.getPrice());
			dto.setTotalPrice(itemEntity.getPrice() * item.getNum());

			return dto;
		}).collect(Collectors.groupingBy(CartItemDto::getCafeId));

		System.out.println("Grouped Items: " + groupedItems);
		return groupedItems;
	}

	// 計算分組的總金額
	public Map<Integer, Integer> calculateTotalAmounts(Map<Integer, List<CartItemDto>> groupedItems) {
		System.out.println("Grouped Items for Calculation: " + groupedItems);

		if (groupedItems == null || groupedItems.isEmpty()) {
			throw new RuntimeException("Grouped items cannot be null or empty.");
		}

		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						entry -> entry.getValue().stream().mapToInt(item -> item.getPrice() * item.getNum()).sum()));

		System.out.println("Calculated Total Amounts: " + totalAmounts);
		return totalAmounts;
	}

	// 分組並計算總金額（綜合方法）
	public Map<String, Object> groupAndCalculateTotalAmounts(List<CartItem> cartItems) {
		Map<Integer, List<CartItem>> groupedItems = cartItems.stream()
				.collect(Collectors.groupingBy(CartItem::getCafeId));

		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().mapToInt(item -> {
					Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
							.orElseThrow(() -> new RuntimeException("商品未找到"));
					return itemEntity.getPrice() * item.getNum();
				}).sum()));

		Map<String, Object> result = new HashMap<>();
		result.put("groupedItems", groupedItems);
		result.put("totalAmounts", totalAmounts);

		return result;
	}

	public ProcessResult processItems(List<CartItemDto> items) {
		// Step 1: 分組 (按 cafeId 分組)
		Map<Integer, List<CartItemDto>> groupedItems = items.stream()
				.collect(Collectors.groupingBy(CartItemDto::getCafeId));

		// Step 2: 計算每個分組的總金額
		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						entry -> entry.getValue().stream().mapToInt(item -> item.getPrice() * item.getNum()).sum()));

		// Step 3: 封裝結果
		ProcessResult result = new ProcessResult();
		result.setGroupedItems(groupedItems);
		result.setTotalAmounts(totalAmounts);

		return result;
	}

}
