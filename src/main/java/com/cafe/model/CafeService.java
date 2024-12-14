package com.cafe.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cafeService")
public class CafeService {

    @Autowired
    CafeRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    // 新增咖啡廳
    public void addCafe(CafeVO cafeVO) {
        repository.save(cafeVO);
    }

    // 更新咖啡廳資料
    public void updateCafe(CafeVO cafeVO) {
        repository.save(cafeVO);
    }

    // 刪除咖啡廳
    public void deleteCafe(Integer cafeId) {
        if (repository.existsById(cafeId)) {
            repository.deleteByCafeId(cafeId);
        }
    }

    // 查詢單一咖啡廳
    public CafeVO getOneCafe(Integer cafeId) {
        Optional<CafeVO> optional = repository.findById(cafeId);
        return optional.orElse(null);
    }

    // 檢查帳號是否存在
    public boolean checkAc(String ac) {
        List<String> allAc = repository.findAllAc();
        for (String existingAc : allAc) {
            if (existingAc.equals(ac)) {
                return true;
            }
        }
        return false;
    }

    // 登入檢查
    public CafeVO login(String ac, String pw) {
        return repository.findByAcAndPw(ac, pw);
    }

    // 查詢所有咖啡廳
    public List<CafeVO> getAll() {
        return repository.findAll();
    }

    // 自訂條件查詢
    public List<CafeVO> findByOthers(Integer cafeId, String name, String city) {
        return repository.findByOthers(cafeId, name, city);
    }

    // 根據統一編號查詢咖啡廳
    public CafeVO findByTaxId(String taxId) {
        return repository.findByTaxId(taxId);
    }
}
