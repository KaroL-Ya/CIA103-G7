package com.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.member.model.MemberService;
import com.member.model.MemberVO;

import util.MailService;

import java.util.*;
import javax.servlet.http.HttpSession;

//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class FrontIndexController {

	@Autowired
	MemberService memberSvc;
	
	@Autowired
	HttpSession session;

	@GetMapping("/index")
	public String frontend_index(Model model) {
		return "front-end/index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "front-end/login";
	}

	@GetMapping("/register")
	public String addMember(ModelMap model) {
		MemberVO memberVO = new MemberVO();
		model.addAttribute("memberVO", memberVO);
		return "front-end/register";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		if(session.getAttribute("mem_Id")==null) {
			return "front-end/login";
		}
		Integer id =(Integer) session.getAttribute("mem_Id");//id
		model.addAttribute("memberVO",memberSvc.getOneMember(id));
		return "front-end/member/profile";
	}
	
	@GetMapping("/forgotPw")
	public String forgotPw(Model model) {
		return "front-end/forgotPw";
	}

}