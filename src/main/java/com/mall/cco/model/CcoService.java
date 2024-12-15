//package com.mall.cco.model;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mall.CouponDistributionService;
//import com.mall.uco.model.UcoRepository;
//import com.mall.uco.model.UcoVO;
//
//@Service("ccoService")
//public class CcoService {
//
//    @Autowired
//    private CcoRepository repository;
//    
//    @Autowired
//    private UcoRepository ucoRepository; // 注入 UcoRepository
//
//    @Autowired
//    private CouponDistributionService couponDistributionService; // 注入分發服務
//
//    public void addCco(CcoVO ccoVO) {
//        repository.save(ccoVO);
//
//        if (ccoVO.getCcoId() == null) {
//            throw new IllegalStateException("CCO_ID 未生成，無法分發優惠券");
//        }
//
//        // 將 CcoVO 轉換為 UcoVO
//        UcoVO newCoupon = new UcoVO();
//        newCoupon.setName(ccoVO.getName());
//        newCoupon.setDiscount(ccoVO.getDiscount());
//        newCoupon.setType("C"); // 設定類型為商家優惠券
//        newCoupon.setCcoId(ccoVO.getCcoId()); // 設置 CCO_ID
//        newCoupon.setMinSpend(ccoVO.getMinSpend());
//
//        // 分發優惠券
//        couponDistributionService.distributeCouponToAllMembers(newCoupon);
//    }
//
//    public void updateCco(CcoVO ccoVO) {
//        repository.save(ccoVO);
//    }
//
//    public void deleteCco(Integer ccoId) {
//        // 先刪除 USER_COUPON 表中與該 CCO_ID 相關的記錄
//        List<UcoVO> relatedCoupons = ucoRepository.findByCcoId(ccoId);
//        if (!relatedCoupons.isEmpty()) {
//            ucoRepository.deleteAll(relatedCoupons);
//        }
//
//        // 再刪除 CCO 表中的記錄
//        if (repository.existsById(ccoId)) {
//            repository.deleteById(ccoId);
//        }
//    }
//
//    public CcoVO getOneCco(Integer ccoId) {
//        return repository.findById(ccoId).orElse(null);
//    }
//
//    public List<CcoVO> getAll() {
//        return repository.findAll();
//    }
//}
package com.mall.cco.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.CouponDistributionService;
import com.mall.uco.model.UcoRepository;
import com.mall.uco.model.UcoVO;

@Service("ccoService")
public class CcoService {

    @Autowired
    private CcoRepository repository;

    @Autowired
    private UcoRepository ucoRepository;

    @Autowired
    private CouponDistributionService couponDistributionService;

    // 新增 CCO 優惠券
    public void addCco(CcoVO ccoVO) {
        repository.save(ccoVO);

        if (ccoVO.getCcoId() == null) {
            throw new IllegalStateException("CCO_ID 未生成，無法分發優惠券");
        }

        UcoVO newCoupon = new UcoVO();
        newCoupon.setName(ccoVO.getName());
        newCoupon.setDiscount(ccoVO.getDiscount());
        newCoupon.setType("C");
        newCoupon.setCcoId(ccoVO.getCcoId());
        newCoupon.setMinSpend(ccoVO.getMinSpend());
        couponDistributionService.distributeCouponToAllMembers(newCoupon);
    }

    // 更新 CCO 優惠券
    public void updateCco(CcoVO ccoVO) {
        repository.save(ccoVO);
    }

    // 刪除 CCO 優惠券
    public void deleteCco(Integer ccoId) {
        List<UcoVO> relatedCoupons = ucoRepository.findByCcoId(ccoId);
        if (!relatedCoupons.isEmpty()) {
            ucoRepository.deleteAll(relatedCoupons);
        }

        if (repository.existsById(ccoId)) {
            repository.deleteById(ccoId);
        }
    }

    // 查詢單一 CCO 優惠券
    public CcoVO getOneCco(Integer ccoId) {
        return repository.findById(ccoId).orElse(null);
    }

    // 查詢所有 CCO 優惠券
    public List<CcoVO> getAll() {
        return repository.findAll();
    }

    // 根據 cafeId 查詢所有 CCO 優惠券
    public List<CcoVO> getCouponsByCafeId(Integer cafeId) {
        return repository.findAllByCafeId(cafeId);
    }
    
    public List<CcoVO> findAllByCafeId(Integer cafeId) {
        return repository.findAllByCafeId(cafeId);
    }

}
