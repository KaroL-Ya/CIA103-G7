package com.cafe.controller;

import java.io.IOException;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cafe.model.CafeService;
import com.cafe.model.CafeVO;

@Controller
@RequestMapping("/cafe")
public class CafeController {

	@Autowired
	CafeService cafeSvc;

	@Autowired
	HttpSession session;

	// 註冊咖啡廳
	@PostMapping("/insertCafe")
	public String insertCafe(@Valid @ModelAttribute CafeVO cafeVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {

		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(cafeVO, result, "img");

		// 確保至少上傳一張圖片
		if (parts.length == 0 || parts[0].isEmpty()) {
			model.addAttribute("errorMessage", "請上傳至少一張圖片");
			return "front-end/cafeRegister"; // 返回註冊頁面並顯示錯誤訊息
		}

		// 將圖片的 byte[] 設置到 cafeVO 中
		MultipartFile multipartFile = parts[0]; // 只使用第一張圖片
		cafeVO.setImg(multipartFile.getBytes());

		// 檢查帳號是否已存在
		if (cafeSvc.checkAc(cafeVO.getAc())) {
			model.addAttribute("errorMessage", "該帳號已存在");
			return "front-end/cafeRegister"; // 返回註冊頁面並顯示錯誤訊息
		}
		// 檢查表單是否有驗證錯誤
		if (result.hasErrors()) {
			return "front-end/cafeRegister"; // 返回註冊頁面以顯示錯誤
		}
		// 保存咖啡廳資料
		cafeSvc.addCafe(cafeVO);

		return "front-end/cafeLogin"; // 成功後重導向到登入頁面
	}

	// Base64
	private void convertImgToBase64(List<CafeVO> cafes) {
		for (CafeVO cafe : cafes) {
			if (cafe.getImg() != null) {
				String base64Img = Base64.getEncoder().encodeToString(cafe.getImg());
				cafe.setBase64Img(base64Img);
			}
		}
	}

	// 更新咖啡廳資料
	@PostMapping("/updateCafe")
	public String updateCafe(@Valid CafeVO cafeVO, BindingResult result, ModelMap model,
			@RequestParam("img") MultipartFile[] parts) throws IOException {
		result = removeFieldError(cafeVO, result, "img");

		if (parts[0].isEmpty()) {
			byte[] existingImg = cafeSvc.getOneCafe(cafeVO.getCafeId()).getImg();
			cafeVO.setImg(existingImg);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				cafeVO.setImg(buf);
			}
		}
		if (result.hasErrors()) {
			return "front-end/cafe/update_cafe_input";
		}
		cafeSvc.updateCafe(cafeVO);
		model.addAttribute("success", "- (修改成功)");
		cafeVO = cafeSvc.getOneCafe(Integer.valueOf(cafeVO.getCafeId()));
		model.addAttribute("cafeVO", cafeVO);

		return "front-end/cafe/listOneCafe";
	}

	// 刪除咖啡廳
	@PostMapping("/deleteCafe")
	public String deleteCafe(@RequestParam("cafeId") String cafeId, ModelMap model) {
		cafeSvc.deleteCafe(Integer.valueOf(cafeId));
		List<CafeVO> list = cafeSvc.getAll();
		model.addAttribute("cafeListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/cafe/listAllCafe";
	}

	// 查詢單一咖啡廳
	@PostMapping("/getOne_For_Update")
	public String getOneCafeForUpdate(@RequestParam("cafeId") String cafeId, ModelMap model) {
		CafeVO cafeVO = cafeSvc.getOneCafe(Integer.valueOf(cafeId));
		model.addAttribute("cafeVO", cafeVO);
		return "front-end/cafe/update_cafe_input";
	}

	// 後台查詢單一咖啡廳
	@PostMapping("/getOne_Update")
	public String getOneCafeUpdate(@RequestParam("cafeId") String cafeId, ModelMap model) {
		CafeVO cafeVO = cafeSvc.getOneCafe(Integer.valueOf(cafeId));
		model.addAttribute("cafeVO", cafeVO);
		return "back-end/cafe/update_cafe_input";
	}

	// 查詢所有咖啡廳
	@GetMapping("/listAllCafe")
	public String listAllCafe(Model model) {
		List<CafeVO> list = cafeSvc.getAll();

		// 將每個咖啡廳的圖片轉換為 Base64 編碼
		convertImgToBase64(list);

		model.addAttribute("cafeListData", list);
		return "back-end/cafe/listAllCafe";
	}

	// 後台新增咖啡廳
	@GetMapping("/addCafe")
	public String addCafe(Model model) {
		CafeVO cafeVO = new CafeVO();
		model.addAttribute("CafeVO", cafeVO);
		return "back-end/cafe/addCafe";
	}

	@PostMapping("/getOne_For_Display")
	public String getOneForDisplay(@RequestParam("cafeId") Integer cafeId, Model model) {
		CafeVO cafeVO = cafeSvc.getOneCafe(cafeId);
		if (cafeVO == null) {
			model.addAttribute("errorMessage", "找不到對應的商家資料！");
			return "back-end/cafe/select_page"; // 返回選擇頁面
		}

		// 將圖片轉換為 Base64 編碼
		if (cafeVO.getImg() != null) {
			String base64Img = Base64.getEncoder().encodeToString(cafeVO.getImg());
			cafeVO.setBase64Img(base64Img);
		}

		model.addAttribute("cafeVO", cafeVO);
		return "back-end/cafe/listOneCafe"; // 返回顯示商家詳情的頁面
	}

	// 提供所有咖啡廳資料使用
	@ModelAttribute("CafeListData")
	public List<CafeVO> referenceCafeListData(Model model) {
		List<CafeVO> list = cafeSvc.getAll();
		System.out.println("CafeListData: " + list.size());
		list.forEach(cafe -> System.out.println("商家ID: " + cafe.getCafeId() + ", 商家名稱: " + cafe.getName()));
		model.addAttribute("cafeListData", list);
		return list;
	}

	// 移除 BindingResult 中某欄位的 FieldError 記錄
	public BindingResult removeFieldError(CafeVO cafeVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldError -> !fieldError.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(cafeVO, "cafeVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}
