package com.dept.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.model.AdminVO;


@Service("deptService")
public class DeptService {

	@Autowired
	DeptRepository repository;

	public void addDept(DeptVO deptVO) {
		repository.save(deptVO);
	}

	public void updateDept(DeptVO deptVO) {
		repository.save(deptVO);
	}

	public void deleteDept(Integer deptno) {
		if (repository.existsById(deptno))
			repository.deleteById(deptno);
	}


	public DeptVO getOneDept(Integer deptno) {
		Optional<DeptVO> optional = repository.findById(deptno);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<DeptVO> getAll() {
		return repository.findAll();
	}

	public Set<AdminVO> getEmpsByDeptno(Integer deptno) {
		return getOneDept(deptno).getAdmins();
	}
	
	public boolean checkDname(String dname) {
		List<String> allDname = repository.findByDname();
		for (String dn : allDname) {
			if (dn.equals(dname)) {
				return true;
			}
		}
		return false;
	}

}
