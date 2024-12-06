package com.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.dept.model.DeptVO;
import com.admin.model.AdminService;
import com.admin.model.AdminVO;
import com.dept.model.DeptService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminSvc;

	@Autowired
	DeptService deptSvc;

	@GetMapping("addAdmin")
	public String addAdmin(ModelMap model) {
		AdminVO adminVO = new AdminVO();
		model.addAttribute("adminVO", adminVO);
		return "back-end/admin/addAdmin";
	}

	@PostMapping("insert")
	public String insert(@Valid AdminVO adminVO, BindingResult result, ModelMap model,
			@RequestParam("admin_Img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(adminVO, result, "admin_Img");
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "Admin照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				adminVO.setAdmin_Img(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/admin/addAdmin";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		adminSvc.addAdmin(adminVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<AdminVO> list = adminSvc.getAll();
		model.addAttribute("adminListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/admin/listAllAdmin"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("admin_Id") String admin_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		AdminVO adminVO = adminSvc.getOneAdmin(Integer.valueOf(admin_Id));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("adminVO", adminVO);
		return "back-end/admin/update_admin_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid AdminVO adminVO, BindingResult result, ModelMap model,
			@RequestParam("admin_Img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(adminVO, result, "admin_Img");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] upFiles = adminSvc.getOneAdmin(adminVO.getAdmin_Id()).getAdmin_Img();
			adminVO.setAdmin_Img(upFiles);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				adminVO.setAdmin_Img(upFiles);
			}
		}
		if (result.hasErrors()) {
			return "back-end/admin/update_admin_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		adminSvc.updateAdmin(adminVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		adminVO = adminSvc.getOneAdmin(Integer.valueOf(adminVO.getAdmin_Id()));
		model.addAttribute("adminVO", adminVO);
		return "back-end/admin/listOneAdmin"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("admin_Id") String admin_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		adminSvc.deleteAdmin(Integer.valueOf(admin_Id));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<AdminVO> list = adminSvc.getAll();
		model.addAttribute("adminListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/admin/listAllAdmin"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("deptListData")
	protected List<DeptVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<DeptVO> list = deptSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("deptMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "資訊部");
		map.put(2, "行銷部");
		map.put(3, "業務部");
		map.put(4, "會計部");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(AdminVO adminVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(adminVO, "adminVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listAdmins_ByCompositeQuery")
	public String listAllAdmin(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<AdminVO> list = adminSvc.getAll(map);
		model.addAttribute("adminListData", list); // for listAllEmp.html 第85行用
		return "back-end/admin/listAllAdmin";
	}

}