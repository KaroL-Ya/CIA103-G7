package com.mall.lookitem;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.mail.FetchProfile.Item;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lookItem")
@Controller
public class LookItemController {
	
	
	
	@Autowired
	private LookItemService lookItemService;

	// Base64
	private void convertCoverImgToBase64(List<LookItemVO> items) {
		for (LookItemVO item : items) {
			if (item.getCoverImg() != null) {
				String base64CoverImg = Base64.getEncoder().encodeToString(item.getCoverImg());
				item.setBase64CoverImg(base64CoverImg); 
			}
		}
	}

	// 全部商品
	@GetMapping("")
	public String getAllLookItems(Model model) {
		List<LookItemVO> items = lookItemService.getAll();
		convertCoverImgToBase64(items); // 
		model.addAttribute("items", items);
		return "front-end/lookitem/lookItem";
	}

	// 詳細資訊
	@GetMapping("/oneItem/{itemId}")
	public String getOneItem(@PathVariable("itemId") Integer itemId, Model model) {
		LookItemVO item = lookItemService.getOneItem(itemId);
		if (item != null && item.getCoverImg() != null) {
			String base64CoverImg = Base64.getEncoder().encodeToString(item.getCoverImg());
			item.setBase64CoverImg(base64CoverImg);
		}
		model.addAttribute("item", item);
		return "front-end/lookItem/oneItem";
	}
	
//	@GetMapping("/oneItem/{itemId}")
//	public String getOneLookItem2(@PathVariable("itemId") Integer itemId, Model model) {
//		System.out.println("-----------------------------------------");
//		System.out.println("itemId="+itemId);
//		System.out.println("-----------------------------------------");
//		LookItemVO item = lookItemService.getOneItem(itemId);
//		if (item != null && item.getCoverImg() != null) {
//			String base64CoverImg = Base64.getEncoder().encodeToString(item.getCoverImg());
//			item.setBase64CoverImg(base64CoverImg);
//		}
//		model.addAttribute("item", item);
//		return "lookItem/oneItem";
//	}

	// 查詢
		@PostMapping("listItem_ByCompositeQuery")
	    public String listAllItem(HttpServletRequest req, Model model){
	        Map<String, String[]> map = req.getParameterMap();  
	        List<LookItemVO> list = lookItemService.getAll(map); 
	        for (LookItemVO lookItem : list) {
	            if (lookItem.getCoverImg() != null) {
	                // Base64
	                String base64CoverImg = Base64.getEncoder().encodeToString(lookItem.getCoverImg());
	                lookItem.setBase64CoverImg(base64CoverImg);
	        }            
	        model.addAttribute("items", list); // 加到模型   
//	        System.out.println("items size: " + list.size());
	    } 
		return "front-end/lookitem/lookItem"; 
		}
	        
	 // 分類
		@GetMapping("/category/{categoryId}")
		public String getItemsByCategory(@PathVariable("categoryId") String categoryId, Model model) {
//		    System.out.println("id:" + categoryId);
			List<LookItemVO> items = lookItemService.getItemsByCategoryId(categoryId);
		    convertCoverImgToBase64(items); 
		    model.addAttribute("items", items);
//		    System.out.println("items size: " + items.size());
		    model.addAttribute("categoryId", categoryId); 
		    return "front-end/lookitem/lookItem"; 
		}


}
