package com.mall;

import com.member.model.MemberRepository;
import com.member.model.MemberVO;
import com.mall.uco.model.UcoRepository;
import com.mall.uco.model.UcoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponDistributionService {

    @Autowired
    private UcoRepository ucoRepo;

    @Autowired
    private MemberRepository memberRepo;

    public void distributeCouponToAllMembers(UcoVO newCoupon) {
        // 查詢所有會員
        List<MemberVO> allMembers = memberRepo.findAll();

        // 檢查是否有會員
        if (allMembers.isEmpty()) {
            throw new IllegalStateException("沒有會員，無法分發優惠券");
        }

        // 為每個會員新增優惠券
        allMembers.forEach(member -> {
            try {
                UcoVO userCoupon = new UcoVO();
                userCoupon.setMemId(member.getMem_Id());
                userCoupon.setName(newCoupon.getName());
                userCoupon.setDiscount(newCoupon.getDiscount());
                userCoupon.setType(newCoupon.getType());
                userCoupon.setGetDate(LocalDate.now());
                userCoupon.setStatus(0);
                userCoupon.setMinSpend(newCoupon.getMinSpend());
                userCoupon.setPcoId(newCoupon.getPcoId());
                userCoupon.setCcoId(newCoupon.getCcoId());
                ucoRepo.save(userCoupon); // 儲存到資料庫
            } catch (Exception e) {
                System.err.println("無法新增優惠券給會員ID: " + member.getMem_Id());
                e.printStackTrace();
            }
        });
    }}
