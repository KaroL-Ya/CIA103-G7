//package com.mall.uco.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.mall.uco.model.UcoService;
//
//import java.util.List;
//
//@Controller
//@Validated
//@RequestMapping("/uco")
//public class UcoController {
//
//    @Autowired
//    private UcoService ucoSvc;
//
//    // 顯示所有優惠券
//    @GetMapping("coupon_list")
//    public String listAllUco(ModelMap model) {
//        List<?> list = ucoSvc.getAll(); // 獲取所有優惠券資料
//        model.addAttribute("ucoListData", list); // 將資料傳遞給前端
//        return "front-end/uco/coupon_list"; // 顯示優惠券列表的頁面
//    }
//}
package com.mall.uco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mall.uco.model.UcoService;

@Controller
@RequestMapping("/uco")
public class UcoController {

    @Autowired
    private UcoService ucoService;

    @GetMapping("/listCoupons")
    public String listCoupons(@RequestParam("mem_Id")  Integer mem_Id, Model model) {
        // 獲取會員的優惠券
        model.addAttribute("ucoListData", ucoService.getCouponsWithDetailsByMemberId(mem_Id));
        return "front-end/uco/coupon_list";
    }
}
