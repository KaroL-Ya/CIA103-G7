package com.cia07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mall.pco.model.PcoService;
import com.mall.uco.model.UcoService;

@Controller
public class CouponController {

    @Autowired
    private PcoService pcoSvc;

    @Autowired
    private UcoService ucoSvc;

    // 測試頁面
    @GetMapping("/test")
    public String index(Model model) {
        return "index"; // 返回 Thymeleaf 模板名稱
    }

    // PCO 部分
    @GetMapping("/pco/select_page")
    public String showPcoSelectPage(Model model) {
        model.addAttribute("pcoListData", pcoSvc.getAll());
        return "back-end/pco/select_page";
    }

    @GetMapping("/pco/listAllPco")
    public String listAllPco(Model model) {
        model.addAttribute("pcoListData", pcoSvc.getAll());
        return "back-end/pco/listAllPco";
    }

    @GetMapping("/uco/coupon_list")
    public String showUcoSelectPage(Model model) {
        model.addAttribute("ucoListData", ucoSvc.getAll());
        return "front-end/uco/coupon_list";
    }
}