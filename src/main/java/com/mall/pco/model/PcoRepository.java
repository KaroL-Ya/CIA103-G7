//package com.mall.pco.model;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.Transactional;
//
//public interface PcoRepository extends JpaRepository<PcoVO, Integer> {
//
//    @Transactional
//    @Modifying
//    @Query(value = "delete from PLATFORM_COUPON where PCO_ID =?1", nativeQuery = true)
//    void deleteByPcoId(int pcoId);
//
//    @Query(value = "from PcoVO where pcoId=?1 and name like ?2 and startDate=?3 order by pcoId")
//    List<PcoVO> findByOthers(int pcoId, String name, java.sql.Date startDate);
//}

package com.mall.pco.model;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PcoRepository extends JpaRepository<PcoVO, Integer> {

    // 查詢符合條件的優惠券
    @Query("from PcoVO where pcoId = ?1 and name like %?2% and startDate = ?3 order by pcoId")
    List<PcoVO> findByOthers(int pcoId, String name, java.sql.Date startDate);

    // 查詢特定狀態的優惠券
    @Query("from PcoVO where status = ?1")
    List<PcoVO> findByStatus(Integer status);
}

