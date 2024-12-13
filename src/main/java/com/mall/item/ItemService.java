package com.mall.item;

//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("itemService")
public class ItemService {

	@Autowired
	ItemRepository itemRepository;

//	@Autowired
//	private SessionFactory sessionFactory;

	// 新增資料
	@Transactional
	public Item addItem(Item item) {
		return itemRepository.save(item);
	}

	// 更新資料
	@Transactional
	public Item updateItem(Item item) {
		return itemRepository.save(item);
	}

	// 刪除資料
	@Transactional
	public void deleteItem(Integer itemId) {
		if (itemRepository.existsById(itemId))
			itemRepository.deleteByItemId(itemId);
//			itemRepository.deleteById(itemId);
	}

	public Item getOneItem(Integer itemId) {
		Optional<Item> optional = itemRepository.findById(itemId);
		return optional.orElse(null);
	}

	public List<Item> getAll() {

		List<Item> itemList = itemRepository.findAll();
		// 動態設置 coverImgUrl
		itemList.forEach(item -> {
			if (item.getCoverImg() != null) {
				item.setCoverImgUrl("/coverImg?itemId=" + item.getItemId());
			} else {
				item.setCoverImgUrl(null); // 如果沒有圖片，可以設置預設圖片 URL
			}
		});

		return itemList;
	}

	// 咖啡廳商家自己的商品
	public List<Item> getAllByCafe(Integer cafeId) {

		
		List<Item> cafeItemList = itemRepository.findByCafeId(cafeId);
		// 動態設置 coverImgUrl
		cafeItemList.forEach(item -> {
            if (item.getCoverImg() != null) {
                item.setCoverImgUrl("/coverImg?itemId=" + item.getItemId());
            } else {
                item.setCoverImgUrl(null); // 如果沒有圖片，可以設置預設圖片 URL
            }
        });

		return cafeItemList;
	}

	public void saveItemImage(Integer itemId, byte[] imgBytes) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("商品不存在"));
		item.setCoverImg(imgBytes);
		itemRepository.save(item);
	}

	public byte[] getCoverImgByItemId(Integer itemId) {
		return itemRepository.findCoverImgByItemId(itemId);
	}

//	// 插入多張圖片
//	@Transactional
//	public Item saveItemWithImages(Item item, List<byte[]> images) {
//		for (byte[] imgData : images) {
//			ItemImg image = new ItemImg();
//			image.setImg(imgData);
//			image.setItem(item); // 設置關聯
//			item.getImg().add(image); // 添加到商品的圖片列表
//		}
//		return itemRepository.save(item); // 保存商品及圖片
//	}

}
