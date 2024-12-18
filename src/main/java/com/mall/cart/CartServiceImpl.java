//package com.mall.cart;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.mall.item.Item;
//import com.mall.item.ItemRepository;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import javax.transaction.Transactional;
//
//@Service
//@Transactional
//public class CartServiceImpl implements CartService {
//
//	@Autowired
//	private CartRepository cartRepository;
//
//	@Autowired
//	private CartItemRepository cartItemRepository;
//
//	@Autowired
//	private ItemRepository itemRepository;
//
////	@Transactional
////	public void createCartForMember(Integer memId) {
////		Cart cart = new Cart();
////		cart.setMemId(memId);
////		cartRepository.save(cart);
////	}
////
//	@Transactional
//	public String addItemToCart(Integer memId, Integer itemId, Integer num) {
//		// 確保購物車存在，若不存在則創建
//		Cart cart = cartRepository.findByMemId(memId).orElse(null);
//		if (cart == null) {
//			cart = new Cart();
//			cart.setMemId(memId);
//			cartRepository.save(cart);
//		}
//
//		// 確保商品存在
//		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("商品未找到"));
//
//		// 檢查商品庫存是否足夠
//		if (num > item.getNum()) {
//			return "商品庫存不足，無法加入購物車！";
//		}
//
//		// 檢查購物車內是否已存在該商品
//		CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getCartId(), itemId).orElse(null);
//		if (cartItem == null) {
//			// 新增商品
//			cartItem = new CartItem();
//			cartItem.setItemId(new CartItem.CartItemId(cart.getCartId(), itemId));
//			cartItem.setCafeId(item.getCafeId());
//
//			if (num > item.getNum()) {
//				return "加入購物車的數量超過商品庫存！";
//			}
//
//			cartItem.setNum(num);
//			cartItemRepository.save(cartItem);
//		} else {
//			// 更新商品數量
//			int newQuantity = cartItem.getNum() + num;
//
//			if (newQuantity > item.getNum()) {
//				return "加入購物車的數量超過商品庫存！";
//			}
//
//			cartItem.setNum(newQuantity);
//			cartItemRepository.save(cartItem);
//		}
//
//		return "商品已成功加入購物車！";
//	}
//
//	@Transactional
//	public void removePurchasedItems(Integer cartId, List<Integer> purchasedItemIds) {
//		if (purchasedItemIds == null || purchasedItemIds.isEmpty()) {
//			throw new IllegalArgumentException("購買的商品清單不能為空！");
//		}
//
//		// 從 cartitem 表中刪除購買的商品
//		cartItemRepository.deleteByCartIdAndItemIdIn(cartId, purchasedItemIds);
//
//		System.out.println("已從購物車中移除已購買的商品：" + purchasedItemIds);
//	}
//
//	@Override
//	public CartDto getCartDetails(Integer memId) {
//		Cart cart = cartRepository.findByMemId(memId).orElse(null);
//
//		if (cart == null) {
//			return new CartDto(null, Map.of(), 0, 0); // 空購物車返回
//		}
//
//		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
//
//		// 遍歷檢查並移除不符合條件的商品
//		Iterator<CartItem> iterator = cartItems.iterator();
//		while (iterator.hasNext()) {
//			CartItem item = iterator.next();
//
//			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
//					.orElseThrow(() -> new RuntimeException("商品未找到，商品編號：" + item.getItemId().getItemId()));
//
//			// 若商品已下架或庫存不足，將其從購物車中移除
//			if (itemEntity.getStatus() == 0 || itemEntity.getNum() <= 0) {
//				cartItemRepository.delete(item); // 從資料庫刪除
//				iterator.remove(); // 從列表中移除
//			}
//		}
//
//		// 按 CafeId 分組
//		Map<Integer, List<CartItemDto>> groupedItems = cartItems.stream().map(item -> {
//			CartItemDto dto = new CartItemDto();
//			dto.setCafeId(item.getCafeId());
//			dto.setNum(item.getNum());
//
//			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
//					.orElseThrow(() -> new RuntimeException("商品未找到，商品編號：" + item.getItemId().getItemId()));
//
//			dto.setItemId(itemEntity.getItemId());
//			dto.setName(itemEntity.getName());
//			dto.setPrice(itemEntity.getPrice());
//			dto.setTotalPrice(itemEntity.getPrice() * item.getNum());
//			dto.setCoverImg(itemEntity.getCoverImg() != null ? itemEntity.getCoverImg() : new byte[0]);
//			dto.setMaxNum(itemEntity.getNum());
//
//			return dto;
//		}).collect(Collectors.groupingBy(CartItemDto::getCafeId));
//
//		// 計算總金額
//		int totalAmount = groupedItems.values().stream().flatMap(List::stream).mapToInt(CartItemDto::getTotalPrice)
//				.sum();
//
//		// 計算商品總數量
//		int cartItemCount = cartItems.stream().mapToInt(CartItem::getNum).sum();
//
//		return new CartDto(cart, groupedItems, totalAmount, cartItemCount);
//	}
//
//	// 更新商品數量
//	@Override
//	public void updateNum(Integer cartId, Integer itemId, Integer num) {
//		cartItemRepository.updateNum(cartId, itemId, num);
//	}
//
//	// 根據會員ID獲取購物車
//	public Cart getCartByMemberId(Integer memId) {
//		return cartRepository.findByMemId(memId).orElseThrow(() -> new RuntimeException("Cart not found"));
//	}
//
//	// 根據會員ID和勾選商品ID，獲取分組的商品
//	public Map<Integer, List<CartItemDto>> getSelectedItems(Integer memId, List<Integer> selectedItemIds) {
//		// 查詢會員購物車
//		Cart cart = cartRepository.findByMemId(memId).orElseThrow(() -> new RuntimeException("Cart not found"));
//		System.out.println("CartId: " + cart.getCartId());
//		System.out.println("Selected Item IDs: " + selectedItemIds);
//
//		// 查詢選中的商品
//		List<CartItem> selectedItems = cartItemRepository.findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(),
//				selectedItemIds);
//		System.out.println("Selected Items: " + selectedItems);
//
//		if (selectedItems == null || selectedItems.isEmpty()) {
//			throw new RuntimeException("No items found for the given selection.");
//		}
//
//		// 分組：按 CafeId 分組
//		Map<Integer, List<CartItemDto>> groupedItems = selectedItems.stream().map(item -> {
//			CartItemDto dto = new CartItemDto();
//			dto.setCafeId(item.getCafeId());
//			dto.setNum(item.getNum());
//
//			// 查找商品詳細信息
//			Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
//					.orElseThrow(() -> new RuntimeException("Item not found for ID: " + item.getItemId().getItemId()));
//			dto.setItemId(itemEntity.getItemId());
//			dto.setName(itemEntity.getName());
//			dto.setPrice(itemEntity.getPrice());
//			dto.setTotalPrice(itemEntity.getPrice() * item.getNum());
//
//			return dto;
//		}).collect(Collectors.groupingBy(CartItemDto::getCafeId));
//
//		System.out.println("Grouped Items: " + groupedItems);
//		return groupedItems;
//	}
//
//	// 計算分組的總金額
//	public Map<Integer, Integer> calculateTotalAmounts(Map<Integer, List<CartItemDto>> groupedItems) {
//		System.out.println("Grouped Items for Calculation: " + groupedItems);
//
//		if (groupedItems == null || groupedItems.isEmpty()) {
//			throw new RuntimeException("Grouped items cannot be null or empty.");
//		}
//
//		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
//				.collect(Collectors.toMap(Map.Entry::getKey,
//						entry -> entry.getValue().stream().mapToInt(item -> item.getPrice() * item.getNum()).sum()));
//
//		System.out.println("Calculated Total Amounts: " + totalAmounts);
//		return totalAmounts;
//	}
//
//	// 分組並計算總金額（綜合方法）
//	public Map<String, Object> groupAndCalculateTotalAmounts(List<CartItem> cartItems) {
//		Map<Integer, List<CartItem>> groupedItems = cartItems.stream()
//				.collect(Collectors.groupingBy(CartItem::getCafeId));
//
//		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
//				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().mapToInt(item -> {
//					Item itemEntity = itemRepository.findById(item.getItemId().getItemId())
//							.orElseThrow(() -> new RuntimeException("商品未找到"));
//					return itemEntity.getPrice() * item.getNum();
//				}).sum()));
//
//		Map<String, Object> result = new HashMap<>();
//		result.put("groupedItems", groupedItems);
//		result.put("totalAmounts", totalAmounts);
//
//		return result;
//	}
//
//	public ProcessResult processItems(List<CartItemDto> items) {
//		// Step 1: 分組 (按 cafeId 分組)
//		Map<Integer, List<CartItemDto>> groupedItems = items.stream()
//				.collect(Collectors.groupingBy(CartItemDto::getCafeId));
//
//		// Step 2: 計算每個分組的總金額
//		Map<Integer, Integer> totalAmounts = groupedItems.entrySet().stream()
//				.collect(Collectors.toMap(Map.Entry::getKey,
//						entry -> entry.getValue().stream().mapToInt(item -> item.getPrice() * item.getNum()).sum()));
//
//		// Step 3: 封裝結果
//		ProcessResult result = new ProcessResult();
//		result.setGroupedItems(groupedItems);
//		result.setTotalAmounts(totalAmounts);
//
//		return result;
//	}
//
//	public List<CartItemDto> getCartItemsForCafe(Cart cart, List<CheckoutRequest.CartItemRequest> items) {
//		// 确保 items 不为空
//		if (items == null || items.isEmpty()) {
//			throw new RuntimeException("No items provided for cafe.");
//		}
//
//		// 将选中的商品ID收集到一个列表中
//		List<Integer> itemIds = items.stream().map(CheckoutRequest.CartItemRequest::getItemId)
//				.collect(Collectors.toList());
//
//		// 输出日志，确保参数正确
//		System.out.println("Cart ID: " + cart.getCartId());
//		System.out.println("Item IDs: " + itemIds);
//
//		// 查询对应的商品资料
//		List<CartItem> cartItems = cartItemRepository.findByCartIdAndItemIds(cart.getCartId(), itemIds);
//
//		// 输出查询结果的数量
//		System.out.println("Found CartItems: " + cartItems.size());
//		if (cartItems.isEmpty()) {
//			System.out.println("No items found for cartId: " + cart.getCartId() + " and itemIds: " + itemIds);
//			throw new RuntimeException("No items found for the provided cart and item IDs.");
//		}
//
//		// 将商品转换成 CartItemDto 并按咖啡厅ID分组
//		List<CartItemDto> cartItemDtos = cartItems.stream().map(cartItem -> {
//			System.out.println("Processing CartItem: " + cartItem);
//
//			CartItemDto dto = new CartItemDto();
//			dto.setCafeId(cartItem.getCafeId());
//			dto.setNum(cartItem.getNum());
//
//			// 查找商品详细信息
//			Item item = itemRepository.findById(cartItem.getItemId().getItemId()).orElseThrow(
//					() -> new RuntimeException("Item not found for ID: " + cartItem.getItemId().getItemId()));
//
//			System.out.println("Found item: " + item.getName());
//
//			dto.setItemId(item.getItemId());
//			dto.setName(item.getName());
//			dto.setPrice(item.getPrice());
//			dto.setTotalPrice(item.getPrice() * cartItem.getNum());
//			dto.setCoverImg(item.getCoverImg() != null ? item.getCoverImg() : new byte[0]);
//			dto.setMaxNum(item.getNum());
//
//			return dto;
//		}).collect(Collectors.toList());
//
//		return cartItemDtos;
//	}
//
//}

//VICKY修改
package com.mall.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.item.Item;
import com.mall.item.ItemRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
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
    public String addItemToCart(Integer memId, Integer itemId, Integer num) {
        Cart cart = cartRepository.findByMemId(memId).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setMemId(memId);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("商品未找到"));

        if (num > item.getNum()) {
            return "商品庫存不足，無法加入購物車！";
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getCartId(), itemId).orElse(null);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setItemId(new CartItem.CartItemId(cart.getCartId(), itemId));
            cartItem.setCafeId(item.getCafeId());
            cartItem.setNum(num);
            cartItemRepository.save(cartItem);
        } else {
            int newQuantity = cartItem.getNum() + num;
            if (newQuantity > item.getNum()) {
                return "加入購物車的數量超過商品庫存！";
            }
            cartItem.setNum(newQuantity);
            cartItemRepository.save(cartItem);
        }
        return "商品已成功加入購物車！";
    }

    @Override
    public void removePurchasedItems(Integer cartId, List<Integer> purchasedItemIds) {
        if (purchasedItemIds == null || purchasedItemIds.isEmpty()) {
            throw new IllegalArgumentException("購買的商品清單不能為空！");
        }
        cartItemRepository.deleteByCartIdAndItemIdIn(cartId, purchasedItemIds);
    }

    @Override
    public CartDto getCartDetails(Integer memId) {
        Cart cart = cartRepository.findByMemId(memId).orElse(null);
        if (cart == null) {
            return new CartDto(null, Map.of(), 0, 0);
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        Map<Integer, List<CartItemDto>> groupedItems = cartItems.stream().map(this::convertToDto)
                .collect(Collectors.groupingBy(CartItemDto::getCafeId));

        int totalAmount = groupedItems.values().stream()
                .flatMap(List::stream)
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();

        int cartItemCount = cartItems.stream().mapToInt(CartItem::getNum).sum();

        return new CartDto(cart, groupedItems, totalAmount, cartItemCount);
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setCafeId(cartItem.getCafeId());
        dto.setNum(cartItem.getNum());

        Item item = itemRepository.findById(cartItem.getItemId().getItemId())
                .orElseThrow(() -> new RuntimeException("商品未找到"));
        dto.setItemId(item.getItemId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setTotalPrice(item.getPrice() * cartItem.getNum());
        dto.setMaxNum(item.getNum());
        return dto;
    }

    @Override
    public void updateNum(Integer cartId, Integer itemId, Integer num) {
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cartId, itemId).orElseThrow(() -> 
            new RuntimeException("商品未找到於購物車中，Cart ID: " + cartId + ", Item ID: " + itemId));
        cartItem.setNum(num);
        cartItemRepository.save(cartItem);
    }

    public Map<Integer, List<CartItemDto>> getSelectedItems(Integer memId, List<Integer> selectedItemIds) {
        Cart cart = cartRepository.findByMemId(memId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> selectedItems = cartItemRepository
                .findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(), selectedItemIds);
        return selectedItems.stream().map(this::convertToDto)
                .collect(Collectors.groupingBy(CartItemDto::getCafeId));
    }

    public Map<Integer, Integer> calculateTotalAmounts(Map<Integer, List<CartItemDto>> groupedItems) {
        return groupedItems.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToInt(CartItemDto::getTotalPrice)
                                .sum()));
    }

    public List<CartItemDto> getCartItemsForCafe(Cart cart, List<CheckoutRequest.CartItemRequest> cafeItems) {
        List<Integer> itemIds = cafeItems.stream()
                .map(CheckoutRequest.CartItemRequest::getItemId)
                .collect(Collectors.toList());

        List<CartItem> selectedItems = cartItemRepository
                .findByItemId_ItemIdInAndItemId_CartId(cart.getCartId(), itemIds);

        return selectedItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}

