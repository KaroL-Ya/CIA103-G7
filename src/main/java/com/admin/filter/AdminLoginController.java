package com.admin.filter;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminRepository;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;

@Controller
public class AdminLoginController {

    @Autowired
    private AdminService adminSvc;
//    @Autowired
//    private ManagerAuthService managerAuthSvc;

    // 顯示登入頁面
    @GetMapping("/back-end")
    public String backend_adminLogin() {
        return "back-end/adminLogin";  // 顯示登入頁面，請確認 Login.html 存在於 resources/templates/login/
    }

    // 處理登入邏輯
    @PostMapping("/back-end/adminLogin")
    public String adminLogin(@RequestParam String admin_Ac,
                        @RequestParam String admin_Pw,
                        HttpSession session, Model model) {
    	String msg = (adminSvc.checkAdminLogin(admin_Ac, admin_Pw));
    	
		if(msg.equals("查無此帳號")) {
			if(msg.equals("密碼有錯")) {
				return "redirect:/back-end";
			}
			return "redirect:/back-end";
		}else { // 登入成功		
			session.setAttribute("admin_Ac",admin_Ac);
//			session.setAttribute("admin_Name",admin_Pw);
			session.setAttribute("role", "admin");
			return "redirect:/admin/backend_index"; // 如果用redirect: 會去找所有controller的Mapping
		}
    }

    // 登出功能
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 使 Session 無效
        return "redirect:/back-end";  // 登出後，重新導向到登入頁面
    }
}