package com.mall.item;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MyController {

	@Autowired
	ItemService itemService;
	
	@Autowired
	private ItemRepository itemRepository;

	@GetMapping("/")
	public String home(ModelMap model) {
		return "home"; // Thymeleaf 頁面名稱
	}
	

	
	@GetMapping("/cart")
	public String getCart(Model model) {
	    return "cart/carttest";
	}

	
	
	@GetMapping("/item")
	public String selectPage(ModelMap model) {
		model.addAttribute("itemList", itemService.getAll());
		return "item/select_page"; // Thymeleaf 頁面名稱
	}
	
	  @GetMapping("/coverImg")
	    public ResponseEntity<byte[]> getCoverImage(@RequestParam("itemId") Integer itemId) {
	    	
	        byte[] imageData = itemRepository.findCoverImgByItemId(itemId);

	        if (imageData != null && imageData.length > 0) {
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.IMAGE_JPEG); // 或 MediaType.IMAGE_PNG
	            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	    
	    
}
