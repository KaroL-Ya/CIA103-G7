package com.mall.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	ItemService itemService;

	// 新增商品
	// 點擊新增商品按鈕
	@GetMapping("/addItem")
	public String addItem(ModelMap model) {
		Item item = new Item();
		model.addAttribute("item", item);
		return "item/addItem";
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
			return "item/addItem";
		} else {
			try {
				// 將圖片轉換為 byte[]
				item.setCoverImg(file.getBytes());
			} catch (IOException e) {
				model.addAttribute("coverImgError", "圖片上傳失敗，請重新嘗試");
				model.addAttribute("item", item);
				return "item/addItem";
			}
		}

		// 如果表單有其他欄位驗證失敗
		if (result.hasErrors()) {
			model.addAttribute("item", item);
			return "item/addItem";
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
		return "item/updateItem";
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
			return "item/updateItem"; // 返回修改页面
		}

		/*************************** 2.開始修改資料 *****************************************/
		itemService.updateItem(item);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "修改成功");
		item = itemService.getOneItem(item.getItemId());
		model.addAttribute("item", item);

		return "redirect:/item";
	}

	@PostMapping("delete")
	public String delete(@RequestParam("itemId") String itemId, ModelMap model) {
		// EmpService empSvc = new EmpService();
		itemService.deleteItem(Integer.valueOf(itemId));

		List<Item> list = itemService.getAll();
		model.addAttribute("itemList", list);
		model.addAttribute("success", "已刪除");
		return "item/select_page";
	}

}
