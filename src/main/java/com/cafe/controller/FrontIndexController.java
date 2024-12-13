package com.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.cafe.model.CafeService;
import com.cafe.model.CafeVO;

import javax.servlet.http.HttpSession;

@Controller
public class FrontIndexController {

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
    @GetMapping("/cafe/login")
    public String login(Model model) {
        return "front-end/login"; // view
    }

    // 註冊頁面
    @GetMapping("/cafe/register")
    public String addCafe(ModelMap model) {
        CafeVO cafeVO = new CafeVO();
        model.addAttribute("cafeVO", cafeVO);
        return "front-end/register";
    }

    // 個人檔案頁面
    @GetMapping("/cafe/cafeProfile")
    public String profile(Model model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId"); // 獲取登錄的咖啡廳ID
        model.addAttribute("cafeVO", cafeSvc.getOneCafe(cafeId));
        return "front-end/cafe/cafeprofile";
    }
    
    //後台首頁
    @GetMapping("/cafe/index2")
    public String backendIndex(Model model) {
        return "/index2";
    }
    
    //後台新增商家
    @GetMapping("/cafe/select_page")
    public String backendSelect(Model model) {
        return "back-end/cafe/select_page";
    }
}
