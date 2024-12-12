package com.mall.item;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemImgService {

	@Autowired
	ItemImgRepository itemImgRepository;

	@Autowired
	ItemRepository itemRepository;

	// 根據商品ID 查詢所有圖片
	public List<Integer> getPictureIdsByItemId(Integer itemId) {
		return itemImgRepository.getPictureIdsByItemId(itemId);
	}

	// 從資料庫或文件系統讀取圖片數據
	public byte[] getPictureById(Integer itemId) {
		return itemImgRepository.findPictureById(itemId);
	}

	// 新增多張圖片
	public void savePictures(Integer itemId, List<byte[]> pictures) throws IOException {
		for (byte[] picture : pictures) {
			ItemImg itemImg = new ItemImg();

			Item item = itemRepository.findById(itemId)
					.orElseThrow(() -> new RuntimeException("找不到對應的商品，商品ID：" + itemId));

			itemImg.setItem(item);
			itemImg.setImg(picture);
			itemImgRepository.save(itemImg);
		}

	}

	// 根據 imgId 刪除圖片
	public void deletePictureById(Integer imgId) {
		itemImgRepository.deleteById(imgId);
	}

}
