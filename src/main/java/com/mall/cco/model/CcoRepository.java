package com.mall.cco.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CcoRepository extends JpaRepository<CcoVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CAFE_COUPON WHERE CCO_ID = ?1", nativeQuery = true) // 使用原生 SQL 刪除指定 ID 的 CCO 優惠券
    void deleteByCcoId(int ccoId);

    @Query(value = "FROM CcoVO WHERE ccoId = ?1 AND name LIKE ?2 AND startDate = ?3 ORDER BY ccoId") // 使用 JPQL 查詢符合條件的優惠券
    List<CcoVO> findByOthers(int ccoId, String name, java.sql.Date startDate);
}
