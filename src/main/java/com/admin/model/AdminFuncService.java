package com.admin.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adminFuncService")
public class AdminFuncService {
	@Autowired
	AdminFuncRepository repositoryFunc;

	@Autowired
	private SessionFactory sessionFactoryFunc;
	
	public List<AdminFuncVO> getAll() {
		return repositoryFunc.findAll();
	}
}
