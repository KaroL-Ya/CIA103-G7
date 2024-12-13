package com.mall.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	ItemService itemService;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	ItemImgRepository itemImgRepository;

	// 新增商品
	// 點擊新增商品按鈕
	@GetMapping("/addItem")
	public String addItem(ModelMap model) {
		Item item = new Item();
		model.addAttribute("item", item);
		return "back-end/item/addItem";
	}

	// addItem頁面按下送出按鈕，將資料送入資料庫
	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("item") Item item, BindingResult result,
			@RequestParam("coverImg") MultipartFile file, // 接收圖片
			Model model) throws IOException {

		// 去除 BindingResult 中 coverImg 欄位的 FieldError 紀錄
		result = removeFieldError(item, result, "coverImg");

		// 處理圖片驗證
		if (file.isEmpty()) {
			model.addAttribute("coverImgError", "請上傳封面圖片"); // 自定義錯誤訊息
			return "back-end/item/addItem";
		} else {
			try {
				// 將圖片轉換為 byte[]
				item.setCoverImg(file.getBytes());
			} catch (IOException e) {
				model.addAttribute("coverImgError", "圖片上傳失敗，請重新嘗試");
				model.addAttribute("item", item);
				return "back-end/item/addItem";
			}
		}

		// 如果表單有其他欄位驗證失敗
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			return "back-end/item/addItem";
		}

		if (item.getNum() <= 0) {
			item.setStatus(0);
		}

		// 保存到資料庫
		itemService.addItem(item);
		return "redirect:/item";
	}

	private BindingResult removeFieldError(Item item, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(item, "Item");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// 點擊修改商品按鈕
	@GetMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("itemId") String itemId, ModelMap model) {
		Item item = itemService.getOneItem(Integer.valueOf(itemId));

		model.addAttribute("item", item);
		return "back-end/item/updateItem";
	}

	@PostMapping("update")
	public String update(@Valid Item item, BindingResult result, ModelMap model,
			@RequestParam("coverImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(item, result, "coverImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			byte[] coverImg = itemService.getOneItem(item.getItemId()).getCoverImg();
			item.setCoverImg(coverImg);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] coverImg = multipartFile.getBytes();
				item.setCoverImg(coverImg);
			}
		}

		if (result.hasErrors()) {
			return "back-end/item/updateItem"; // 返回修改页面
		}

		/*************************** 檢查商品數量，自動下架處理 *****************************/
		if (item.getNum() <= 0) {
			item.setStatus(0);
		}

		/*************************** 2.開始修改資料 *****************************************/
		itemService.updateItem(item);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "修改成功");
		item = itemService.getOneItem(item.getItemId());
		model.addAttribute("item", item);

		return "redirect:/item";
	}

	// 點擊修改商品圖片按鈕
	@GetMapping("UpdateItemImg")
	public String UpdateUpdateItemImg(@RequestParam("itemId") String itemId, ModelMap model) {
		Item item = itemService.getOneItem(Integer.valueOf(itemId));
		model.addAttribute("item", item);

		// 查詢與該商品相關的圖片 ID 列表
		List<Integer> imgIds = itemImgRepository.getPictureIdsByItemId(Integer.valueOf(itemId));

		System.out.println("imgIds: " + imgIds);

		// 將圖片 ID 傳遞到模板
		model.addAttribute("imgId", imgIds);

		model.addAttribute("itemId", itemId);

		return "back-end/item/updateItemImg";
	}

	@PostMapping("delete")
	public String delete(@RequestParam("itemId") String itemId, ModelMap model) {

		itemImgRepository.deleteAllByItemId(Integer.valueOf(itemId));

		itemService.deleteItem(Integer.valueOf(itemId));

		List<Item> list = itemService.getAll();
		model.addAttribute("itemList", list);
		model.addAttribute("success", "已刪除");
		return "redirect:/item";
	}

}
