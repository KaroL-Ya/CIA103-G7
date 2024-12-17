package com.itemreport;
//
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

@Service
public class ItemReportService {

	@Autowired
	private ItemReportRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	// 用來創建商品檢舉
	public ItemReportVO reportItem(ItemReportVO itemReportVO) {
		itemReportVO.setTime(new Date()); // 設置檢舉時間
		itemReportVO.setState((byte) 0); // 初始狀態為未處理
		return repository.save(itemReportVO);
	}

	// 用來更新檢舉狀態
	public ItemReportVO updateReportState(Integer itemReportNo, Byte state) {
		ItemReportVO itemReportVO = repository.findById(itemReportNo).orElse(null);
		if (itemReportVO != null) {
			itemReportVO.setState(state);
			return repository.save(itemReportVO);
		}
		return null;
	}

	public List<ItemReportVO> getAll() {

		return repository.findAll();
	}
	

}
