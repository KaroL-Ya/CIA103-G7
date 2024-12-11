package com.admin.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.member.model.MemberVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Admin;

@Service("adminService")
public class AdminService {
	@Autowired
	AdminRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addAdmin(AdminVO  adminVO) {
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
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
//	public Optional<AdminVO> findByAc(String admin_Ac){
//		AdminVO adminVO = null;
//		ResultSet result = repository.findById();
//		for(AdminVO a:adminVO) {
//			
//		}
//		return adminVO == null ? Optional.empty() : Optional.of(adminVO);
//	}

	public List<AdminVO> getAll() {
		return repository.findAll();
	}

	public List<AdminVO> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Admin.getAllC(map,sessionFactory.openSession());
	}
	
	public String checkAdminLogin(String admin_Ac,String admin_Pw) {
    	String msg = "";
        List<String> allAdmin_Ac = repository.findByAdmin_Ac();
        String getOneAdmin_Pw = repository.findGetOneByAdmin_Pw(admin_Ac);
        AdminVO adminVO = repository.useAdmin_AcFindAdmin_Id(admin_Ac);
        System.out.println(adminVO);
        
        for(String pk : allAdmin_Ac) {
        	if(pk.equals(admin_Ac)) {
        		if(admin_Pw.equals(getOneAdmin_Pw)) {
        			return "符合";
        		}else {
        			return "密碼有錯";
        		}
        	}
        }
		return "查無此帳號";

    }
	
}
