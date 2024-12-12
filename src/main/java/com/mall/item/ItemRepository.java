package com.mall.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	Optional<Item> findById(Integer itemId); // 根據商品ID查詢商品

	// 根據咖啡廳id查詢商品，確保每間咖啡廳只能看到自己的商品
	@Query(value = "from Item where CAFE_ID like?1 order by ITEM_ID")
	List<Item> findByCafeId(Integer cafeId);

	// 根據商品id刪除商品
	@Modifying
	@Query(value = "delete from Item where ITEM_ID =?1", nativeQuery = true)
	void deleteByItemId(Integer itemId);

	// 根據商品id抓取商品封面圖片
	@Query("SELECT i.coverImg FROM Item i WHERE i.itemId = :itemId")
	byte[] findCoverImgByItemId(@Param("itemId") Integer itemId);

	@Query("select price from Item where ITEM_ID =?1")
	Integer findPriceByItemId(Integer itemId);

}
