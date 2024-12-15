package com.admin.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Admin;

@Service("adminService")
public class AdminService {
	@Autowired
	AdminRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	public void addAdmin(AdminVO adminVO) {
		repository.save(adminVO);
	}

	public void updateAdmin(AdminVO adminVO) {
		repository.save(adminVO);
	}

	public void deleteAdmin(Integer admin_Id) {
		if (repository.existsById(admin_Id))
			repository.deleteByAdmin_Id(admin_Id);
	}

	public AdminVO getOneAdmin(Integer admin_Id) {
		Optional<AdminVO> optional = repository.findById(admin_Id);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<AdminVO> getAll() {
		return repository.findAll();
	}

	public List<AdminVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Admin.getAllC(map, sessionFactory.openSession());
	}

	public boolean checkAdmin_Ac(String admin_Ac) {
		List<String> allAc = repository.findByAdmin_Ac();
		for (String pk : allAc) {
			if (pk.equals(admin_Ac)) {
				return true;
			}
		}
		return false;
	}

	public AdminVO adminLogin(String admin_Ac, String admin_Pw) {	
		return repository.getOneByAdmin_AP(admin_Ac, admin_Pw);
	}
	
	public void deleteAdminAuth(Integer admin_Id) {
		repository.deleteAdminAuth(admin_Id);
	}
	
	public void addAdminAuth(Integer adminId, Set<String> func_Id) {
		for(String bo12 : func_Id) {
			repository.addAdminAuth(adminId, Integer.valueOf(bo12));
		}
	}
	

}