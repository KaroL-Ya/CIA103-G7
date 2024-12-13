//package com.mall.pco.model;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service("pcoService")
//public class PcoService {
//
//    @Autowired
//    private PcoRepository repository;
//
//    public void addPco(PcoVO pcoVO) {
//        repository.save(pcoVO);
//    }
//
//    public void updatePco(PcoVO pcoVO) {
//        repository.save(pcoVO);
//    }
//
//    public void deletePco(Integer pcoId) {
//        if (repository.existsById(pcoId))
//            repository.deleteById(pcoId); // 使用 deleteById 方法
//    }
//
//    public PcoVO getOnePco(Integer pcoId) {
//        Optional<PcoVO> optional = repository.findById(pcoId);
//        return optional.orElse(null);  // 如果值存在就回傳其值，否則回傳 null
//    }
//
//    public List<PcoVO> getAll() {
//        return repository.findAll();
//    }
//}

package com.mall.pco.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mall.CouponDistributionService;
import com.mall.uco.model.UcoRepository;
import com.mall.uco.model.UcoVO;

@Service("pcoService")
public class PcoService {

    @Autowired
    private PcoRepository repository;

    @Autowired
    private CouponDistributionService couponDistributionService; // 注入分發服務
    
    @Autowired
    private UcoRepository ucoRepository; // 注入 UcoRepository

    public void addPco(PcoVO pcoVO) {
        repository.save(pcoVO);
        
        if (pcoVO.getPcoId() == null) {
            throw new IllegalStateException("PCO_ID 未生成，無法分發優惠券");
        }

        // 將 PcoVO 轉換為 UcoVO
        UcoVO newCoupon = new UcoVO();
        newCoupon.setName(pcoVO.getName());
        newCoupon.setDiscount(pcoVO.getDiscount());
        newCoupon.setType("P"); // 設定類型為平台優惠券
        newCoupon.setPcoId(pcoVO.getPcoId()); // 設置 PCO_ID
        newCoupon.setMinSpend(pcoVO.getMinSpend());

        // 分發優惠券
        couponDistributionService.distributeCouponToAllMembers(newCoupon);
    }

    public void updatePco(PcoVO pcoVO) {
        repository.save(pcoVO);
    }

    public void deletePco(Integer pcoId) {
    	// 先刪除與該 PCO_ID 相關的 USER_COUPON 記錄
        List<UcoVO> relatedCoupons = ucoRepository.findByPcoId(pcoId);
        if (!relatedCoupons.isEmpty()) {
            ucoRepository.deleteAll(relatedCoupons);
        }

        // 再刪除 PLATFORM_COUPON 記錄
        if (repository.existsById(pcoId)) {
            repository.deleteById(pcoId);
        }
    }

    public PcoVO getOnePco(Integer pcoId) {
        Optional<PcoVO> optional = repository.findById(pcoId);
        return optional.orElse(null);
    }

    public List<PcoVO> getAll() {
        return repository.findAll();
    }
}
