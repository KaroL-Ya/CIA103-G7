package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminFuncVO;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;
import com.dept.model.DeptService;
import com.dept.model.DeptVO;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/back-end/admin")
public class BackendIndexController {
 
 @Autowired
 AdminService adminSvc;
 
 @Autowired
 DeptService deptSvc;
    
    @GetMapping("/backend_index")
 public String backend_index(Model model) {
  return "back-end/admin/backend_index";
 }
    
    @GetMapping("/admin_select_page")
 public String select_page(Model model) {
  return "back-end/admin/admin_select_page";
 }
    
    @GetMapping("/listAllAdmin")
 public String listAllAdmin(Model model) {
  return "back-end/admin/listAllAdmin";
 }
    
    @GetMapping("/listAllAdminAuth")
    public String listAllAdminAuth(Model model) {
    	return "back-end/admin/listAllAdminAuth";
    }
    
    @GetMapping("/listAllDept")
	public String listAllDept(Model model) {
		return "back-end/admin/listAllDept";
	}
    
    @ModelAttribute("adminAuthListData")
    public List<AdminVO> getAdminAuthListData() {
        return adminSvc.getAll(); // 直接返回所有 AdminVO 資料
    }
    
    @ModelAttribute("adminListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
 protected List<AdminVO> referenceListData(Model model) {
     List<AdminVO> list = adminSvc.getAll();
  return list;
 }
    
 @ModelAttribute("deptListData") // for select_page.html 第135行用
 protected List<DeptVO> referenceListData_Dept(Model model) {
  model.addAttribute("deptVO", new DeptVO()); // for select_page.html 第133行用
  List<DeptVO> list = deptSvc.getAll();
  return list;
 }

}