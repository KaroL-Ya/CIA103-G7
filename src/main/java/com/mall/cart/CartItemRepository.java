package com.mall.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItem.CartItemId> {

	@Query(value = "SELECT * FROM cartitem WHERE cart_id = :cartId", nativeQuery = true)
	List<CartItem> findByCartId(@Param("cartId") Integer cartId);

	@Transactional
	@Modifying
	@Query("UPDATE CartItem c SET c.num = :num WHERE c.itemId.cartId = :cartId AND c.itemId.itemId = :itemId")
	void updateNum(@Param("cartId") Integer cartId, @Param("itemId") Integer itemId, @Param("num") Integer num);

	@Transactional
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.itemId.itemId IN :itemIds AND c.itemId.cartId = :cartId")
	Integer deleteByItemIds(@Param("cartId") Integer cartId, @Param("itemIds") List<Integer> itemIds);

	@Query("SELECT COUNT(c) FROM CartItem c WHERE c.itemId.cartId = :cartId")
	long countByCartId(@Param("cartId") Integer cartId);

	// 根據 cartId 和 itemId 查找購物車中的商品
	@Query("SELECT c FROM CartItem c WHERE c.itemId.cartId = :cartId AND c.itemId.itemId IN :itemIds")
	List<CartItem> findByItemId_ItemIdInAndItemId_CartId(@Param("cartId") Integer cartId,
			@Param("itemIds") List<Integer> itemIds);

	@Query("SELECT c FROM CartItem c WHERE c.itemId.cartId = :cartId AND c.itemId.itemId IN :itemIds")
	List<CartItem> findByCartIdAndItemIds(@Param("cartId") Integer cartId, @Param("itemIds") List<Integer> itemIds);

}
