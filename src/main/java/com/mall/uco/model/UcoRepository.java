//package com.mall.uco.model;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface UcoRepository extends JpaRepository<UcoVO, Integer> {
//
//    // 根據會員編號查詢
//    List<UcoVO> findByMemId(Integer memId);
//
//    // 根據優惠券狀態查詢
//    List<UcoVO> findByStatus(Integer status);
//
//    // 根據優惠券類型查詢
//    List<UcoVO> findByType(String type);
//
//    // 根據會員編號和狀態查詢
//    List<UcoVO> findByMemIdAndStatus(Integer memId, Integer status);
//}
package com.mall.uco.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UcoRepository extends JpaRepository<UcoVO, Integer> {

    // 使用 JOIN 查詢會員的優惠券與相關資料
    @Query("SELECT u, p.startDate AS pStartDate, p.endDate AS pEndDate, " +
           "c.startDate AS cStartDate, c.endDate AS cEndDate " +
           "FROM UcoVO u " +
           "LEFT JOIN PcoVO p ON u.pcoId = p.pcoId " +
           "LEFT JOIN CcoVO c ON u.ccoId = c.ccoId " +
           "WHERE u.memId = ?1")
    List<Object[]> findCouponsWithDetailsByMemberId(@Param("memId") Integer memId);
    List<UcoVO> findByPcoId(Integer pcoId); // 根據 PCO_ID 查詢 USER_COUPON
    List<UcoVO> findByCcoId(Integer ccoId); // 根據 CCO_ID 查詢 USER_COUPON
    
//    @Query(value="select * from user_coupon where mem_Id =?1",nativeQuery = true)
//    Integer findCouponByMember(Integer mem_Id); 
}
