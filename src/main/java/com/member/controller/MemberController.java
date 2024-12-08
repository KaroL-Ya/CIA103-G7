package com.member.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
//@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memberSvc;
	
	@Autowired
	HttpSession session;
	
	@PostMapping("insert")
	public String insert(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "img");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "會員照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				memberVO.setImg(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "front-end/register";
		}
		/*************************** 2.開始新增資料 *****************************************/
		if(memberSvc.checkAc(memberVO.getAc())==false) {
			memberSvc.addMember(memberVO);
		}else {
			model.addAttribute("errorMessage","已存在的帳號");
			return "front-end/register";
//			return "redirect:/register";
		}

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		return "redirect:/login"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
		
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("mem_Id") String mem_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		MemberVO memberVO = memberSvc.getOneMember(Integer.valueOf(mem_Id));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("memberVO", memberVO);
		return "front-end/member/update_member_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "img");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] upFiles = memberSvc.getOneMember(memberVO.getMem_Id()).getImg();
			memberVO.setImg(upFiles);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				memberVO.setImg(upFiles);
			}
		}
		if (result.hasErrors()) {
			return "front-end/member/update_member_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		memberSvc.updateMember(memberVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		memberVO = memberSvc.getOneMember(Integer.valueOf(memberVO.getMem_Id()));
		model.addAttribute("memberVO", memberVO);
		return "front-end/member/listOneMember"; // 修改成功後轉交listOneEmp.html
	}
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MemberVO memberVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(memberVO, "memberVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
    @GetMapping("/admin/listAllMember")
	public String listAllMember(Model model) {
		return "back-end/admin/listAllMember";
	}
    
    @ModelAttribute("MemberListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<MemberVO> referenceMListData(Model model) {
    	List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData", list2);
		return list2;
	}
    
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("mem_Id") String mem_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		memberSvc.deleteMember(Integer.valueOf(mem_Id));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<MemberVO> list2 = memberSvc.getAll();
		model.addAttribute("memberListData", list2);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/admin/listAllMember"; // 刪除完成後轉交listAllEmp.html
	}

}
