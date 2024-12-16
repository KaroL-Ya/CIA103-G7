package com.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.cafe.model.CafeService;
import com.cafe.model.CafeVO;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class CafeFrontController {

    @Autowired
    CafeService cafeSvc;

    @Autowired
    HttpSession session;

    // 首頁
    @GetMapping("/cafe")
    public String frontendIndex(Model model) {
        return "front-end/index1";
    }

    // 登入頁面
    @GetMapping("/cafe/cafeLogin")
    public String cafeLogin(Model model) {
        return "front-end/cafeLogin"; // view
    }

    // 登入按鈕
    @GetMapping("/cafe/cafe_Login")
    public String cafe_Login(Model model) {
        return "front-end/index1"; // view
    }

    // 註冊頁面
    @GetMapping("/cafe/cafeRegister")
    public String addCafe(ModelMap model) {
        CafeVO cafeVO = new CafeVO();
        model.addAttribute("cafeVO", cafeVO);
        return "front-end/cafeRegister";
    }

    // 個人檔案頁面
    @GetMapping("/cafe/cafeProfile")
    public String profile(Model model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId"); // 獲取登錄的咖啡廳ID
        CafeVO cafeVO = cafeSvc.getOneCafe(cafeId);

        // 將圖片轉換為 Base64
        if (cafeVO.getImg() != null) {
            String base64Img = Base64.getEncoder().encodeToString(cafeVO.getImg());
            cafeVO.setBase64Img(base64Img);
        }

        model.addAttribute("cafeVO", cafeVO);
        return "front-end/cafe/cafeprofile";
    }

    // 後台首頁
    @GetMapping("/cafe/index2")
    public String backendIndex(Model model) {
        return "/index2";
    }

    // 後台新增商家
    @GetMapping("/cafe/select_page")
    public String backendSelect(Model model) {
        List<CafeVO> list = cafeSvc.getAll();

        // 將每個商家的圖片轉換為 Base64
        convertImgToBase64(list);

        System.out.println("CafeListData: " + list.size());
        list.forEach(cafe -> System.out.println("商家ID: " + cafe.getCafeId() + ", 商家名稱: " + cafe.getName()));

        model.addAttribute("cafeListData", list);
        return "back-end/cafe/select_page";
    }

    // 將圖片轉換為 Base64 的方法
    private void convertImgToBase64(List<CafeVO> cafes) {
        for (CafeVO cafe : cafes) {
            if (cafe.getImg() != null) {
                String base64Img = Base64.getEncoder().encodeToString(cafe.getImg());
                cafe.setBase64Img(base64Img);
            }
        }
    }
}
