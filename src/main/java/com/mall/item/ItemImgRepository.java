package com.mall.item;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Integer> {

	// 根據商品ID 查詢圖片ID
	@Query(value = "SELECT imgid FROM item_img WHERE item_id = :itemId", nativeQuery = true)
	List<Integer> getPictureIdsByItemId(@Param("itemId") Integer itemId);

	// 根據商品ID 查詢商品圖片
	@Query("SELECT i.img FROM ItemImg i WHERE i.imgId = :imgId")
	byte[] findPictureById(@Param("imgId") Integer imgId);

	@Modifying
	@Transactional
	@Query("DELETE FROM ItemImg i WHERE i.item.itemId = :itemId")
	void deleteAllByItemId(@Param("itemId") Integer itemId);

}
