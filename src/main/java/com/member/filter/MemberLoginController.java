package com.member.filter;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
public class MemberLoginController {

	@Autowired
	MemberService memberSvc;
	
	@Autowired
	HttpSession session;
	
//	@PostMapping("/login")
//	public String login(@RequestParam String ac
//			,@RequestParam String pw,
//			HttpSession session,ModelMap model) {
////			System.out.println(ac + pw);
////			System.out.println(memberSvc.checkLogin(ac, pw));
//			String msg = (memberSvc.checkLogin(ac, pw));
//			if(msg.equals("查無此帳號")) {
//				if(msg.equals("密碼有錯")) {
//					return "redirect:/login";
//				}
//				return "redirect:/login";
//			}else { // 登入成功
//				session.setAttribute("ac",ac);
//				session.setAttribute("pw",pw);
//				session.setAttribute("role", "member");
//				return "redirect:/index"; // 如果用redirect: 會去找所有controller的Mapping
//			}
//	}
	
	@PostMapping("/login")
	public String login(@RequestParam String ac,@RequestParam String pw,
			HttpSession session,ModelMap model) {
		MemberVO memberVO = memberSvc.login(ac, pw);
		if(memberVO != null) {
			// 登入成功
			session.setAttribute("mem_Id",memberVO.getMem_Id());
			session.setAttribute("ac",ac);
			session.setAttribute("name",memberVO.getName());
			session.setAttribute("email",memberVO.getEmail());
			session.setAttribute("birth",memberVO.getBirth());
			session.setAttribute("sex",memberVO.getSex());
			session.setAttribute("phone",memberVO.getPhone());
			session.setAttribute("city",memberVO.getCity());
			session.setAttribute("disc",memberVO.getDisc());
			session.setAttribute("address",memberVO.getAddress());
			return "redirect:/index";
		}else {
			// 登入失敗
			 model.addAttribute("error", "帳號或密碼錯誤");
	         return "front-end/login";  // 重新導向到登入頁面
		}
		
	}

	
    @GetMapping("/mlogout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除 Session
        return "redirect:/login";
    }
    
    
    
    
}