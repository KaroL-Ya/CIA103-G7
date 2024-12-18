//package com.mall.uco.model;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UcoService {
//
//    @Autowired
//    private UcoRepository repository;
//
//    // 根據會員編號查詢優惠券（包含詳細資料）
//    public List<UcoVO> getCouponsWithDetailsByMemberId(Integer memId) {
//        List<Object[]> results = repository.findCouponsWithDetailsByMemberId(memId);
//
//        return results.stream().map(row -> {
//            UcoVO coupon = (UcoVO) row[0];
//            // 平台優惠券起始和結束日期
//            LocalDate pStartDate = (LocalDate) row[1];
//            LocalDate pEndDate = (LocalDate) row[2];
//            // 商家優惠券起始和結束日期
//            LocalDate cStartDate = (LocalDate) row[3];
//            LocalDate cEndDate = (LocalDate) row[4];
//
//            // 選擇有效期間的起始與結束日期
//            coupon.setStartDate(pStartDate != null ? pStartDate : cStartDate);
//            coupon.setEndDate(pEndDate != null ? pEndDate : cEndDate);
//
//            return coupon;
//        }).collect(Collectors.toList());
//    }
//}
//package com.mall.uco.model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mall.cco.model.CcoVO;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class UcoService {
//
//    @Autowired
//    private UcoRepository repository;
//
//    // 根據會員編號查詢優惠券（包含詳細資料）
//    public List<UcoVO> getCouponsWithDetailsByMemberId(Integer memId) {
//    	
//    	 System.out.println("getCouponsWithDetailsByMemberId");
//        // 從資料庫取得結果集
//        List<Object[]> results = repository.findCouponsWithDetailsByMemberId(memId);
//        
//
//        // 將結果轉換為 UcoVO 列表，並直接印出原始資料
//        return results.stream().map(row -> { 
//            // 取得優惠券資訊
//            UcoVO coupon = (UcoVO) row[0];
//
//            // 平台優惠券日期
//            LocalDate pStartDate = (LocalDate) row[1];
//            LocalDate pEndDate = (LocalDate) row[2];
//
//            // 商家優惠券日期
//            LocalDate cStartDate = (LocalDate) row[3];
//            LocalDate cEndDate = (LocalDate) row[4];
//
//            return coupon; // 不對資料進行任何改變
//        }).collect(Collectors.toList());
//    }
//
//
//    // 根據會員編號查詢優惠券，並按咖啡廳分組
//    public Map<Integer, List<UcoVO>> getCouponsGroupedByCafe(Integer memId) {
//        List<UcoVO> coupons = getCouponsWithDetailsByMemberId(memId);
//
//        // 過濾僅商家優惠券，並按商家 ID (ccoId) 分組
//        return coupons.stream()
//                .filter(coupon -> coupon.getCcoId() != null) // 僅商家優惠券
//                .collect(Collectors.groupingBy(UcoVO::getCcoId)); // 按商家 ID 分組
//    }
//
//    // 更新優惠券狀態（例如設為已使用）
//    public void markCouponAsUsed(Integer ucoId) {
//        UcoVO coupon = repository.findById(ucoId)
//                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
//        coupon.setStatus(1); // 設置為已使用
//        repository.save(coupon);
//    }
//
//    // 新增 validateCoupon 方法
//    public boolean validateCoupon(Integer ucoId, Integer cafeId) {
//        UcoVO coupon = repository.findById(ucoId)
//                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
//
//        // 驗證優惠券是否適用於該咖啡廳
//        if (coupon.getCcoId() != null) {
//            if (!coupon.getCcoId().equals(cafeId)) {
//                throw new RuntimeException("優惠券不屬於該咖啡廳，優惠券 ID：" + ucoId);
//            }
//        }
//
//        // 驗證優惠券是否過期
//        LocalDate currentDate = LocalDate.now();
//        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(currentDate)) {
//            throw new RuntimeException("優惠券已過期，優惠券 ID：" + ucoId);
//        }
//
//        // 驗證優惠券是否已使用
//        if (coupon.getStatus() != null && coupon.getStatus() == 1) {
//            throw new RuntimeException("優惠券已使用，優惠券 ID：" + ucoId);
//        }
//
//        // 如果所有驗證通過，則返回 true
//        return true;
//    }
//    
//    public List<UcoVO> getAll() {
//        return repository.findAll();
//    }
//}

//package com.mall.uco.model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class UcoService {
//
//    @Autowired
//    private UcoRepository repository;
//
//    // 根據會員編號查詢優惠券（包含詳細資料）
//    public List<UcoVO> getCouponsWithDetailsByMemberId(Integer memId) {
//        List<Object[]> results = repository.findCouponsWithDetailsByMemberId(memId);
//
//        return results.stream().map(row -> {
//            UcoVO coupon = (UcoVO) row[0];
//            // 平台優惠券起始和結束日期
//            LocalDate pStartDate = (LocalDate) row[1];
//            LocalDate pEndDate = (LocalDate) row[2];
//            // 商家優惠券起始和結束日期
//            LocalDate cStartDate = (LocalDate) row[3];
//            LocalDate cEndDate = (LocalDate) row[4];
//
//            // 選擇有效期間的起始與結束日期
//            coupon.setStartDate(pStartDate != null ? pStartDate : cStartDate);
//            coupon.setEndDate(pEndDate != null ? pEndDate : cEndDate);
//
//            return coupon;
//        }).collect(Collectors.toList());
//    }
//
//    // 根據會員 ID 查詢現有的優惠券 ID
//    public Integer getCouponCountByMemberId(Integer memId) {
//        try {
//            return repository.findCouponByMember(memId);
//        } catch (Exception e) {
//            throw new RuntimeException("查詢會員優惠券失敗，會員ID：" + memId, e);
//        }
//    }
//
//    // 根據會員編號查詢優惠券，並按咖啡廳分組
//    public Map<Integer, List<UcoVO>> getCouponsGroupedByCafe(Integer memId) {
//        List<UcoVO> coupons = getCouponsWithDetailsByMemberId(memId);
//
//        // 過濾僅商家優惠券，並按商家 ID (ccoId) 分組
//        return coupons.stream()
//                .filter(coupon -> coupon.getCcoId() != null) // 僅商家優惠券
//                .collect(Collectors.groupingBy(UcoVO::getCcoId)); // 按商家 ID 分組
//    }
//
//    // 更新優惠券狀態（例如設為已使用）
//    public void markCouponAsUsed(Integer ucoId) {
//        UcoVO coupon = repository.findById(ucoId)
//                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
//        coupon.setStatus(1); // 設置為已使用
//        repository.save(coupon);
//    }
//
//    // 新增 validateCoupon 方法
//    public boolean validateCoupon(Integer ucoId, Integer cafeId) {
//        UcoVO coupon = repository.findById(ucoId)
//                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
//
//        // 驗證優惠券是否適用於該咖啡廳
//        if (coupon.getCcoId() != null) {
//            if (!coupon.getCcoId().equals(cafeId)) {
//                throw new RuntimeException("優惠券不屬於該咖啡廳，優惠券 ID：" + ucoId);
//            }
//        }
//
//        // 驗證優惠券是否過期
//        LocalDate currentDate = LocalDate.now();
//        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(currentDate)) {
//            throw new RuntimeException("優惠券已過期，優惠券 ID：" + ucoId);
//        }
//
//        // 驗證優惠券是否已使用
//        if (coupon.getStatus() != null && coupon.getStatus() == 1) {
//            throw new RuntimeException("優惠券已使用，優惠券 ID：" + ucoId);
//        }
//
//        // 如果所有驗證通過，則返回 true
//        return true;
//    }
//
//    // 獲取所有優惠券
//    public List<UcoVO> getAll() {
//        return repository.findAll();
//    }
//}

package com.mall.uco.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.cco.model.CcoRepository;
import com.mall.cco.model.CcoVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UcoService {

    @Autowired
    private UcoRepository repository;

    @Autowired
    private CcoRepository ccoRepository;
    
    public List<UcoVO> getAll() {
        return repository.findAll();
    }

    public List<UcoVO> getAvailableCouponsByMemberId(Integer memId) {
        List<UcoVO> coupons = getCouponsWithDetailsByMemberId(memId);
        return coupons.stream()
                .filter(coupon -> coupon.getStatus() == 0) // 狀態為未使用
                .filter(coupon -> coupon.getEndDate() == null || !coupon.getEndDate().isBefore(LocalDate.now())) // 未過期
                .toList();
    }

    public List<UcoVO> getCouponsWithDetailsByMemberId(Integer memId) {
        List<Object[]> results = repository.findCouponsWithDetailsByMemberId(memId);
        return results.stream().map(row -> (UcoVO) row[0]).toList();
    }

    public Map<Integer, List<UcoVO>> getAvailableCouponsGroupedByCafe(Integer memId, Map<Integer, Integer> totalAmounts) {
        List<UcoVO> availableCoupons = getAvailableCouponsByMemberId(memId);

        Map<Integer, Integer> ccoToCafeMap = ccoRepository.findAll().stream()
                .collect(Collectors.toMap(CcoVO::getCcoId, CcoVO::getCafeId));

        return availableCoupons.stream()
                .filter(coupon -> coupon.getCcoId() != null) // 商家優惠券
                .filter(coupon -> ccoToCafeMap.containsKey(coupon.getCcoId())) // 有對應的 cafeId
                .filter(coupon -> totalAmounts.getOrDefault(ccoToCafeMap.get(coupon.getCcoId()), 0) >= coupon.getMinSpend()) // 符合最低消費
                .collect(Collectors.groupingBy(coupon -> ccoToCafeMap.get(coupon.getCcoId()))); // 按 cafeId 分組
    }

    public void markCouponAsUsed(Integer ucoId) {
        UcoVO coupon = repository.findById(ucoId)
                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
        coupon.setStatus(1); // 設置為已使用
        repository.save(coupon);
    }

    public UcoVO getCoupon(Integer ucoId) {
        return repository.findById(ucoId)
                .orElseThrow(() -> new RuntimeException("找不到優惠券，ID：" + ucoId));
    }
}

