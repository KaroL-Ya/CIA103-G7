package com.mall.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class itemImgController {

	@Autowired
	ItemService itemService;

	@Autowired
	ItemImgService itemImgService;

	@Autowired
	ItemImgRepository itemImgRepository;

	// 點擊修改商品圖片按鈕
	@GetMapping("UpdateItemImg")
	public String UpdateUpdateItemImg(@RequestParam("itemId") String itemId, ModelMap model) {
		Item item = itemService.getOneItem(Integer.valueOf(itemId));
		model.addAttribute("item", item);
		return "back-end/item/updateItemImg";
	}

	// 處理多張商品圖片上傳
	@PostMapping("/item/uploadpicture/{itemId}")
	public String uploadPictures(@PathVariable Integer itemId, @RequestParam("pictures") List<MultipartFile> files,
			ModelMap model) {
		try {
			List<byte[]> pictures = new ArrayList<>();
			for (MultipartFile file : files) {
				pictures.add(file.getBytes());
			}
			itemImgService.savePictures(itemId, pictures);
//			Item item = itemService.getOneItem(Integer.valueOf(itemId));
//			model.addAttribute("item", item);
			return "redirect:/item/UpdateItemImg?itemId=" + itemId;
		} catch (IOException e) {
			return "redirect:/item/UpdateItemImg?itemId=" + itemId;
		}
	}

	// 透過圖片ID找到圖檔
	@GetMapping("/item/picture/{imgId}")
	public ResponseEntity<byte[]> getPicture(@PathVariable Integer imgId) {
		System.out.println("Received imgId: " + imgId);
		byte[] picture = itemImgRepository.findPictureById(imgId);

		if (picture == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
	}

	// 刪除圖片
	@GetMapping("/item/deletePicture")
	public String deletePicture(@RequestParam(value = "imgId") Integer imgId,
			@RequestParam(value = "itemId", required = false, defaultValue = "0") Integer itemId,
			RedirectAttributes redirectAttributes, ModelMap model) {
		try {
			if (itemId == 0) {
				redirectAttributes.addFlashAttribute("errorMessage", "無效的 itemId，請檢查後重試！");
				return "redirect:/back-end/item/updateItemImg";
			}

			itemImgService.deletePictureById(imgId); // 呼叫服務層刪除圖片
			redirectAttributes.addFlashAttribute("message", "圖片已成功刪除！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "圖片刪除失敗，請稍後再試！");
		}

		return "redirect:/item/UpdateItemImg?itemId=" + itemId;

	}

}
