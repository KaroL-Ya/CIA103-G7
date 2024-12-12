package com.mall.lookitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LookItemRepository extends JpaRepository<LookItemVO, Integer> {

  
    List<LookItemVO> findByNameLike(String name);

    List<LookItemVO> findByCategoryId(String category);

   
	

}

        // 商品狀態查商品（上架或下架商品）
//    List<ItemVO> findByStatus(Byte status);
//    // 價格區間查找商品
//    @Query(value = "SELECT * FROM ITEM WHERE PRICE BETWEEN ?1 AND ?2", nativeQuery = true)
//    List<ItemVO> findItemsByPriceRange(int minPrice, int maxPrice);

    

