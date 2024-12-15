package com.member.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import util.MailService;

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
			if(memberSvc.checkEmail(memberVO.getEmail())==false) {
				memberSvc.addMember(memberVO);
				// 生成驗證碼
			    String verificationCode = MailService.vCode(6);
			    session.setAttribute("verificationCode", verificationCode);
			    // 發送驗證碼郵件
			    MailService.sendMail(memberVO.getEmail(), "驗證碼", "您的驗證碼為：" + verificationCode);
			}else {
				model.addAttribute("errorMessage","該信箱已註冊過");
				return "front-end/register";
			}
		}else {
			model.addAttribute("errorMessage","已存在的帳號");
			return "front-end/register";
		}
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		return "redirect:/verifyPage?email=" + memberVO.getEmail();
	}
	
	// 顯示驗證碼頁面
	@GetMapping("/verifyPage")
	public String showVerifyPage(@RequestParam(required = false) String email, Model model) {
	    if (email == null || email.isEmpty()) {
	        return "redirect:/login"; // 將使用者導向另一個網址
	    }
	    model.addAttribute("email", email);
	    return "front-end/verifyPage"; // 對應前端頁面的名稱
	}
	
	
	// 會員補驗證
	@PostMapping("/verifyPage")
	public String reVerifyPage(@RequestParam String email, Model model) {
		System.out.println(email);
		if(memberSvc.checkVerifiedEmail(email)) {
			model.addAttribute("error", "該信箱已完成驗證");
			return "front-end/login";
		}
		if(memberSvc.checkEmail(email)) {
	 		// 生成驗證碼
		    String verificationCode = MailService.vCode(6);
		    session.setAttribute("verificationCode", verificationCode);
		    // 發送驗證碼郵件
		    MailService.sendMail(email, "驗證碼", "您的驗證碼為：" + verificationCode);
			model.addAttribute("email", email);
		    return "front-end/verifyPage"; // 對應前端頁面的名稱
		}
		model.addAttribute("error", "該信箱未註冊");
		return "front-end/login";
	}
	
	@PostMapping("/verifyCode")
	public String verifyCode(@RequestParam String email, 
	                         @RequestParam String verificationCode, 
	                         HttpSession session, 
	                         Model model) {
	    // 從 Session 或其他地方取得之前生成的驗證碼
	    String expectedCode = (String) session.getAttribute("verificationCode");

	    if (expectedCode != null && expectedCode.equals(verificationCode)) {
	    	memberSvc.updateStatus(email);
	        session.removeAttribute("verificationCode"); // 驗證成功後刪除驗證碼
	        return "redirect:/login"; // 驗證成功後跳轉到成功頁面
	    }

	    model.addAttribute("email", email);
	    model.addAttribute("error", "驗證碼錯誤或已過期！");
	    return "front-end/verifyPage"; // 驗證失敗重新顯示驗證頁面
	}
	
	// 顯示驗證碼頁面
	@GetMapping("/sendPTPw")
	public String forgotPage(@RequestParam(required = false) String email, Model model) {
	    if (email == null || email.isEmpty()) {
	        return "redirect:/forgotPw"; // 將使用者導向另一個網址
	    }
	    model.addAttribute("email", email);
	    return "front-end/verifyPage"; // 對應前端頁面的名稱
	}
	
	@PostMapping("/sendPTPw")
	public String sendPTPw(@RequestParam String email,Model model) {
		if(memberSvc.checkVerifiedEmail(email)==true) {
			int length = (int) ((Math.random()*12)+8);
			// 生成臨時密碼寄給會員
		    String PTPw = memberSvc.vCode(length);
			memberSvc.updateForGotPw(PTPw, email);
		    MailService.sendMail(email, "Back廣咖樂-忘記密碼", "您的臨時密碼為：" + PTPw + "收到此信請儘快登入做密碼修改，如非本人操作請儘快聯繫我們(02)1234-5678");
		}else {
		    model.addAttribute("error", "查無此信箱！");
		    return "front-end/forgotPw";
		}
		return "redirect:/login";
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("mem_Id") String mem_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 因無給使用者ID 故無需做錯誤處理
		/*************************** 2.開始查詢資料 *****************************************/
		MemberVO memberVO = memberSvc.getOneMember(Integer.valueOf(mem_Id));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("memberVO", memberVO);
		return "front-end/member/update_member_input"; // 查詢完成後轉交update_member_input.html
	}

	@PostMapping("update")
	public String update(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "img");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
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
		memberSvc.updateMember(memberVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		memberVO = memberSvc.getOneMember(Integer.valueOf(memberVO.getMem_Id()));
		model.addAttribute("memberVO", memberVO);
		return "front-end/member/listOneMember"; // 修改成功後轉交listOneMember.html
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
	
	// -----------------------以下是 後台 會員管理的部分-------------------------------------------
	
    @GetMapping("/back-end/admin/listAllMember")
	public String listAllMember(Model model) {
		return "back-end/admin/listAllMember";
	}
    
    @ModelAttribute("MemberListData")  // for select_page.html 用 // for listAllMember.html 用
	protected List<MemberVO> referenceMListData(Model model) {
    	List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData", list2);
		return list2;
	}
    
	@PostMapping("delete")
	public String delete(@RequestParam("mem_Id") String mem_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		memberSvc.deleteMember(Integer.valueOf(mem_Id));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<MemberVO> list2 = memberSvc.getAll();
		model.addAttribute("memberListData", list2);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/admin/listAllMember"; // 刪除完成後轉交listAllMember.html
	}

	@PostMapping("adminGetOneMember_For_Update")
	public String adminGetOneMember_For_Update(@RequestParam("mem_Id") String mem_Id, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		MemberVO memberVO = memberSvc.getOneMember(Integer.valueOf(mem_Id));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("memberVO", memberVO);
		return "back-end/admin/update_member_input"; // 查詢完成後轉交update_member_input.html
	}

	@PostMapping("adminUpdateMember")
	public String adminUpdateMember(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "img");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			byte[] upFiles = memberSvc.getOneMember(memberVO.getMem_Id()).getImg();
			memberVO.setImg(upFiles);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				memberVO.setImg(upFiles);
			}
		}
		if (result.hasErrors()) {
			return "back-end/admin/update_member_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		memberSvc.updateMember(memberVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		memberVO = memberSvc.getOneMember(Integer.valueOf(memberVO.getMem_Id()));
		model.addAttribute("memberVO", memberVO);
		return "back-end/admin/listOneMember"; // 修改成功後轉交listOneMember.html
	}

}
