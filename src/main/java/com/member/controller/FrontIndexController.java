package com.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminService;
import com.admin.model.AdminVO;
import com.dept.model.DeptService;
import com.dept.model.DeptVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.mysql.cj.Session;

import java.util.*;

import javax.servlet.http.HttpSession;

//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class FrontIndexController {

	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了EmpService --> 供第66使用
	@Autowired
	MemberService memberSvc;
	
	@Autowired
	HttpSession session;

	// inject(注入資料) via application.properties
	@Value("${welcome.message}")
	private String message;

	@GetMapping("/index")
	public String frontend_index(Model model) {
		model.addAttribute("message", message);
		return "front-end/index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "front-end/login"; // view
	}

	@GetMapping("/register")
	public String addMember(ModelMap model) {
		MemberVO memberVO = new MemberVO();
		model.addAttribute("memberVO", memberVO);
		return "front-end/register";
	}

	@PostMapping("profile")
	public String profile(Model model) {
		Integer id =(Integer) session.getAttribute("mem_Id");//id
		model.addAttribute("memberVO",memberSvc.getOneMember(id));
		return "front-end/member/profile";
	}

}