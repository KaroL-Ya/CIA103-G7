package com.dept.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.admin.model.AdminVO;
import com.dept.model.DeptService;
import com.dept.model.DeptVO;

@Controller
@RequestMapping("/back-end/admin")
public class DeptController {
	@Autowired
	DeptService deptSvc;
	
	@GetMapping("/addDept")
	public String addDept(ModelMap model) {
		DeptVO deptVO = new DeptVO();
		model.addAttribute("deptVO", deptVO);
		return "back-end/admin/addDept";
	}
	
	@PostMapping("insertDept")
	public String insertDept(@Valid DeptVO deptVO, BindingResult result, ModelMap model) throws IOException {
		if(deptSvc.checkDname(deptVO.getDname())) {
			model.addAttribute("errorMessage","部門名稱重複");
			return "back-end/admin/addDept";
		}
		if (result.hasErrors()) {
			return "back-end/admin/addDept";
		}
		/*************************** 2.開始新增資料 *****************************************/
		deptSvc.addDept(deptVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<DeptVO> list = deptSvc.getAll();
		model.addAttribute("deptListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/back-end/admin/listAllDept"; // 新增成功後重導至BackendIndexControoler @GetMapping("/listAllAdmin")
	}

	@PostMapping("getOneDept_For_Update")
	public String getOneDept_For_Update(@RequestParam("deptno") String deptno, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		DeptVO deptVO = deptSvc.getOneDept(Integer.valueOf(deptno));
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("deptVO", deptVO);
		return "back-end/admin/update_dept_input"; // 查詢完成後轉交update_admin_input.html
	}

	@PostMapping("updateDept")
	public String updateDept(@Valid DeptVO deptVO, BindingResult result, ModelMap model) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if(deptSvc.checkDname(deptVO.getDname())) {
			model.addAttribute("errorMessage","部門名稱重複");
			return "back-end/admin/update_dept_input";
		}
		if (result.hasErrors()) {
			return "back-end/admin/update_dept_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		deptSvc.updateDept(deptVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		deptVO = deptSvc.getOneDept(Integer.valueOf(deptVO.getDeptno()));
		model.addAttribute("deptVO", deptVO);
		return "redirect:/back-end/admin/listAllDept"; // 修改成功後轉交listOneAdmin.html
	}

	
	@PostMapping("deleteDept")
	public String deleteDept(@RequestParam("deptno") String deptno, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		deptSvc.deleteDept(Integer.valueOf(deptno));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<DeptVO> list = deptSvc.getAll();
		model.addAttribute("deptListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/admin/listAllDept"; // 刪除完成後轉交listAllAdmin.html
	}
}
