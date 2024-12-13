package com.cafe.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CafeRepository extends JpaRepository<CafeVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "delete from cafe where CAFE_ID =?1", nativeQuery = true)
    void deleteByCafeId(int cafeId);

    // 自訂條件查詢
    @Query(value = "from CafeVO where CAFE_ID=?1 and NAME like ?2 and CITY=?3 order by CAFE_ID")
    List<CafeVO> findByOthers(int cafeId, String name, String city);

    @Query(value = "select AC from cafe", nativeQuery = true)
    List<String> findAllAc();

    @Query(value = "select PW from cafe where AC=?", nativeQuery = true)
    String findPasswordByAc(String ac);

    @Query(value = "from CafeVO where AC=?1")
    CafeVO findByAc(String ac);

    @Query(value = "from CafeVO where AC=?1 and PW=?2")
    CafeVO findByAcAndPw(String ac, String pw);

    @Query(value = "from CafeVO where TAXID=?1")
    CafeVO findByTaxId(String taxId);
}
