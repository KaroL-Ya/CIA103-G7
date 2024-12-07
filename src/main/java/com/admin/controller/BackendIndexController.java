package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminService;
import com.admin.model.AdminVO;
import com.dept.model.DeptService;
import com.dept.model.DeptVO;
import java.util.*;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class BackendIndexController {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了EmpService --> 供第66使用
	@Autowired
	AdminService adminSvc;
	
	@Autowired
	DeptService deptSvc;
	
    // inject(注入資料) via application.properties
//    @Value("${welcome.message}")
//    private String message;
	
    //=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/admin/backend_index")
	public String backend_index(Model model) {
		return "back-end/admin/backend_index";
	}
    
    @GetMapping("/admin/admin_select_page")
	public String select_page(Model model) {
		return "back-end/admin/admin_select_page";
	}
    
    @GetMapping("/admin/listAllAdmin")
	public String listAllAdmin(Model model) {
		return "back-end/admin/listAllAdmin";
	}
    
    @ModelAttribute("adminListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<AdminVO> referenceListData(Model model) {
    	List<AdminVO> list = adminSvc.getAll();
		return list;
	}
    
//    @GetMapping("/admin/listAllAdmin")
//	public String listAllAdmin(Model model) {
//    	List<AdminVO> list = adminSvc.getAll();
//    	model.addAttribute("adminListData",list);
//		return "back-end/admin/listAllAdmin";
//	}
    
	@ModelAttribute("deptListData") // for select_page.html 第135行用
	protected List<DeptVO> referenceListData_Dept(Model model) {
		model.addAttribute("deptVO", new DeptVO()); // for select_page.html 第133行用
		List<DeptVO> list = deptSvc.getAll();
		return list;
	}

}