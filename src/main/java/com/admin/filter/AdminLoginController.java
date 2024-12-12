package com.admin.filter;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminSvc;

	// 後台登入頁面
	@GetMapping("/back-end")
	public String backend_adminLogin() {
		return "back-end/adminLogin";
	}

	// 處理登入邏輯
	@PostMapping("/adminLogin")
	public String adminLogin(@RequestParam String admin_Ac, @RequestParam String admin_Pw, HttpSession session,
			ModelMap model) {
		AdminVO adminVO = adminSvc.adminLogin(admin_Ac, admin_Pw);

		if (adminVO != null) {
			if (admin_Ac.equals(adminVO.getAdmin_Ac()) && admin_Pw.equals(adminVO.getAdmin_Pw())) {
				if (adminVO.getAdmin_Status() == 1) {
					// 登入成功
//				System.out.println(adminVO);
//				session.setAttribute("admin_Ac",admin_Ac);
					session.setAttribute("admin_Id", adminVO.getAdmin_Id());
					session.setAttribute("admin_Name", adminVO.getAdmin_Name());
					session.setAttribute("role", "admin");
					return "/back-end/admin/backend_index";
				} else {
					// 登入失敗
					model.addAttribute("error", "帳號尚未開通");
					return "back-end/adminLogin";
//				return "redirect:/back-end"; // 重新導向到登入頁面
				}
			} else {
				// 登入失敗
				model.addAttribute("error", "帳號或密碼錯誤");
				return "back-end/adminLogin";
//			return "redirect:/back-end"; // 重新導向到登入頁面
			}
		} else {
			// 登入失敗
			model.addAttribute("error", "帳號或密碼錯誤");
			return "back-end/adminLogin";
		}
	}

	// 登出功能
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("admin_Id");
		session.removeAttribute("admin_Name");
		session.removeAttribute("role");
//		session.invalidate(); // 使 Session 無效
		return "redirect:/back-end"; // 登出後，重新導向到登入頁面
	}

//  // 處理登入邏輯
//  @PostMapping("/back-end/adminLogin")
//  public String adminLogin(@RequestParam String admin_Ac,
//                      @RequestParam String admin_Pw,
//                      HttpSession session, Model model) {
//  	String msg = (adminSvc.checkAdminLogin(admin_Ac, admin_Pw));
//  	
//		if(msg.equals("查無此帳號")) {
//			if(msg.equals("密碼有錯")) {
//				return "redirect:/back-end";
//			}
//			return "redirect:/back-end";
//		}else { // 登入成功		
//			session.setAttribute("admin_Ac",admin_Ac);
////			session.setAttribute("admin_Name",admin_Pw);
//			session.setAttribute("role", "admin");
//			return "redirect:/admin/backend_index"; // 如果用redirect: 會去找所有controller的Mapping
//		}
//  }

}