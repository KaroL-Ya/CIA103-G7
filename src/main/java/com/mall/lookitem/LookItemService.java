package com.mall.lookitem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.FetchProfile.Item;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service("lookItemService")
public class LookItemService {

	@Autowired
	LookItemRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	public void addLookItem(LookItemVO lookItemVO) {
		repository.save(lookItemVO);
	}

	public void updateLookItem(LookItemVO lookItemVO) {
		repository.save(lookItemVO);
	}

	public LookItemVO getOneItem(Integer itemId) {
		Optional<LookItemVO> optionalItem = repository.findById(itemId);
		return optionalItem.orElse(null);
	}

	public List<LookItemVO> getAll() {
		return repository.findAll();
	}

	public LookItemVO findById(Integer itemId) {
		Optional<LookItemVO> optional = repository.findById(itemId);
		return optional.orElse(null);
	}

	public List<LookItemVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_item.getAllC(map, sessionFactory.openSession());
	}

	public void uploadImage(MultipartFile file, LookItemVO item) throws IOException {

		byte[] imageBytes = file.getBytes();
		item.setCoverImg(imageBytes);

		repository.save(item);
	}

}