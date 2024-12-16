package com.admin.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/back-end")
public class ManagermentController {
	
	@Autowired
	HttpSession session;
	
    @GetMapping("/cafe/list")
	public String cafe(Model model) {
//    	Integer pm3 = (Integer) session.getAttribute("pm3");
//        System.out.println("pm3: " + pm3);  // 用來檢查 pm3 是否存在
		if(session.getAttribute("pm3")==null) {
			return "back-end/adminLogin";
		}
		return "back-end/cafe/listAllCafe";
	}
	
    @GetMapping("/shop/list")
	public String shop(Model model) {
		if(session.getAttribute("pm4")==null) {
			return "back-end/adminLogin";
		}
		return "back-end/shop/listAllShop";
	}
	
    @GetMapping("/event/list")
	public String event(Model model) {
		if(session.getAttribute("pm5")==null) {
			return "back-end/adminLogin";
		}
		return "back-end/event/listAllEvent";
	}
    
    @GetMapping("/news/list")
	public String news(Model model) {
		if(session.getAttribute("pm6")==null) {
			return "back-end/adminLogin";
		}
		return "back-end/news/listAllNews";
	}
}
