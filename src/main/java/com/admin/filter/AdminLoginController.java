package com.admin.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminFuncService;
import com.admin.model.AdminFuncVO;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;
import com.dept.model.DeptVO;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminSvc;
	
	@Autowired
	private AdminFuncService adminFuncSvc;

	// 後台登入頁面
	@GetMapping("/back-end")
	public String backend_adminLogin() {
		return "back-end/adminLogin";
	}
	
	@GetMapping("/adminLogin")
	public String adminLogin() {
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
					session.setAttribute("admin_Id", adminVO.getAdmin_Id());
					session.setAttribute("admin_Name", adminVO.getAdmin_Name());
					session.setAttribute("role", "admin");
					//
					List<Integer> funcIds = new ArrayList<>();
					for(AdminFuncVO auth: adminVO.getAdmin_Func()) {
						funcIds.add(auth.getFunc_Id());
					}
					// 定義所有功能編號
	                List<Integer> allFuncIds = Arrays.asList(1, 2, 3, 4, 5, 6,7);
	                //
	                for(Integer funcId : funcIds) {
	                	if(allFuncIds.contains(funcId)) {
	                		// 加入
//	                		System.out.print(adminVO.getAdmin_Name() + "的權限有->" + funcId +" " + funcIds + allFuncIds.contains(funcId) +" \n");
//	                		System.out.println(allFuncIds.contains(funcId));
//	                		System.out.println(funcId);
	                		session.setAttribute("pm"+funcId, funcId);
	                	}
	                }
					return "back-end/backhome";
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
		session.removeAttribute("pm1");
		session.removeAttribute("pm2");
		session.removeAttribute("pm3");
		session.removeAttribute("pm4");
		session.removeAttribute("pm5");
		session.removeAttribute("pm6");
		session.removeAttribute("pm7");
//		session.invalidate(); // 使 Session 無效
		return "redirect:/back-end"; // 登出後，重新導向到登入頁面
	}
	
	// 登出功能
	@GetMapping("/logout")
	public String getLogout(HttpSession session) {
		session.removeAttribute("admin_Id");
		session.removeAttribute("admin_Name");
		session.removeAttribute("role");
		session.removeAttribute("pm1");
		session.removeAttribute("pm2");
		session.removeAttribute("pm3");
		session.removeAttribute("pm4");
		session.removeAttribute("pm5");
		session.removeAttribute("pm6");
		session.removeAttribute("pm7");
//		session.invalidate(); // 使 Session 無效
		return "redirect:/back-end"; // 登出後，重新導向到登入頁面
	}

}