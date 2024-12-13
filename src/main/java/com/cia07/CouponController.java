package com.cia07;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.mall.pco.model.PcoService;
import com.mall.pco.model.PcoVO;
import com.mall.uco.model.UcoService;
import com.mall.uco.model.UcoVO;
import com.mall.cco.model.CcoService;
import com.mall.cco.model.CcoVO;
import java.util.*;



@Controller
public class CouponController {

    @Autowired
    private PcoService pcoSvc;

    @Autowired
    private CcoService ccoSvc; // 注入 CCO 的服務層
    
    @Autowired
    private UcoService ucoSvc;


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

    @ModelAttribute("pcoListData")
    public List<PcoVO> referencePcoListData() {
        return pcoSvc.getAll();
    }

    // CCO 部分
    @GetMapping("/cco/select_page")
    public String showCcoSelectPage(Model model) {
        model.addAttribute("ccoListData", ccoSvc.getAll());
        return "front-end/cco/select_page";
    }

    @GetMapping("/cco/listAllCco")
    public String listAllCco(Model model) {
        model.addAttribute("ccoListData", ccoSvc.getAll());
        return "front-end/cco/listAllCco";
    }

    @ModelAttribute("ccoListData")
    public List<CcoVO> referenceCcoListData() {
        return ccoSvc.getAll();
    }

    @GetMapping("/uco/coupon_list")
    public String showUcoSelectPage(Model model) {
        model.addAttribute("ucoListData", ucoSvc.getAll());
        return "front-end/uco/coupon_list";
    }
}