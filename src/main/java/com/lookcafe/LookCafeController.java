package com.lookcafe;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cafe.model.CafeVO;



@RequestMapping("/lookCafe")
@Controller
public class LookCafeController {

	@Autowired
	private LookCafeService lookCafeService;

	@Autowired
	private HttpSession session;

	private String introduce;

	// Base64
	private void convertImgToBase64(List<LookCafeVO> cafes) {
		for (LookCafeVO cafe : cafes) {
			if (cafe.getImg() != null) {
				String base64Img = Base64.getEncoder().encodeToString(cafe.getImg());
				cafe.setBase64Img(base64Img);
			}
		}
	}

	//============= 詳細資訊
	@GetMapping("/oneCafe/{cafeId}")
	public String getOneCafe(@PathVariable("cafeId") Integer cafeId, Model model) {
		LookCafeVO cafe = lookCafeService.getOneCafe(cafeId);
		if (cafe != null && cafe.getImg() != null) {
			String base64Img = Base64.getEncoder().encodeToString(cafe.getImg());
			cafe.setBase64Img(base64Img);
		}
		model.addAttribute("cafe", cafe);
		model.addAttribute("cafeId", session.getAttribute("cafeId"));
		return "front-end/lookcafe/oneCafe";
	}
	
//	============商家修改表單
	@GetMapping("/updateCafe/{id}")
	public String getUpdateLookCafe(@PathVariable("id") Integer cafeId, Model model) {
	    LookCafeVO cafe = lookCafeService.getCafeById(cafeId);
	   
	    model.addAttribute("cafe", cafe);
	    return "front-end/lookcafe/updateCafe"; // 返回修改頁面
	}
	
	//========修改完的頁面
	@PostMapping("/updateCafe/{id}")
	public String updateLookCafe(@PathVariable("id") Integer cafeId, @ModelAttribute LookCafeVO cafe) {	   
	    LookCafeVO cafe1 = lookCafeService.getCafeById(cafeId);
	    
	    if (cafe != null) {
	        cafe1.setIntroduce(cafe.getIntroduce());  
	    }
	    // 保存更新後
	    lookCafeService.updateLookCafe(cafe1);
	    return "redirect:/lookCafe/oneCafe/" + cafe1.getCafeId();
	}

    //===========所有咖啡店
    @GetMapping("")
    public String getAllLookCafes(Model model) {
        List<LookCafeVO> cafes = lookCafeService.getAll();
        convertImgToBase64(cafes);
        model.addAttribute("cafes", cafes);
        return "front-end/lookcafe/lookCafe";
    }

}

	

// ============查詢
//    @PostMapping("/listCafe_ByCompositeQuery")
//    public String listAllCafe(HttpServletRequest req, Model model) {
//        Map<String, String[]> map = req.getParameterMap();
//        List<LookCafeVO> list = lookCafeService.getAll(map);

//        convertImgToBase64(list);
//
//        model.addAttribute("cafes", list);
//        return "front-end/cafe/lookCafe"; 
