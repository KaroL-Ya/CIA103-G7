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

	@PostMapping("/login")
	public String login(@RequestParam String ac, @RequestParam String pw, HttpSession session, ModelMap model) {
		MemberVO memberVO = memberSvc.login(ac, pw);
		if (memberVO != null) {
			if (ac.equals(memberVO.getAc()) && pw.equals(memberVO.getPw())) {
				if (memberVO.getStatus() == 1) {
					// 登入成功
					// session.setAttribute("memberVO", memberVO);
					session.setAttribute("mem_Id", memberVO.getMem_Id());
					session.setAttribute("ac", ac);
					session.setAttribute("name", memberVO.getName());
					session.setAttribute("email", memberVO.getEmail());
					session.setAttribute("birth", memberVO.getBirth());
					session.setAttribute("sex", memberVO.getSex());
					session.setAttribute("phone", memberVO.getPhone());
					session.setAttribute("city", memberVO.getCity());
					session.setAttribute("disc", memberVO.getDisc());
					session.setAttribute("address", memberVO.getAddress());
					session.setAttribute("memberRole", "member");
					return "redirect:/index";
				} else {
					model.addAttribute("error", "請先信箱驗證成功再登入");
					return "front-end/login"; // 重新導向到登入頁面
				}
			} else {
				// 登入失敗
				model.addAttribute("error", "帳號或密碼錯誤");
				return "front-end/login"; // 重新導向到登入頁面
			}
		} else {
			// 登入失敗 因為空值
			model.addAttribute("error", "帳號或密碼空白");
			return "front-end/login"; // 重新導向到登入頁面
		}
	}

	@PostMapping("/mlogout")
	public String logout(HttpSession session) {
		session.removeAttribute("mem_Id");
		session.removeAttribute("ac");
		session.removeAttribute("name");
		session.removeAttribute("email");
		session.removeAttribute("birth");
		session.removeAttribute("sex");
		session.removeAttribute("phone");
		session.removeAttribute("city");
		session.removeAttribute("disc");
		session.removeAttribute("address");
		session.removeAttribute("memberRole");
//        session.invalidate(); // 清除 Session
		return "redirect:/login";
	}

}