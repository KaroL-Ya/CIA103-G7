package com.member.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
public class MemberService {

    @Autowired
    MemberRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addMember(MemberVO memberVO) {
        repository.save(memberVO);
    }

    public void updateMember(MemberVO memberVO) {
        repository.save(memberVO);
    }
    
	public void deleteMember(Integer mem_Id) {
		if (repository.existsById(mem_Id))
			repository.deleteByMem_Id(mem_Id);
	}

    public MemberVO getOneMember(Integer mem_Id) {
        Optional<MemberVO> optional = repository.findById(mem_Id);
//        return optional.get();
        return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }
    
//    public String checkLogin(String ac,String pw) {
//    	String msg = "";
//        List<String> allAc = repository.findByAc();
//        String getOnePw = repository.findGetOneByPw(ac);
//        MemberVO memberVO = repository.useAcFindId(ac);
//        System.out.println(memberVO);
//        for(String pk : allAc) {
//        	if(pk.equals(ac)) {
//        		if(pw.equals(getOnePw)) {
//        			return "符合";
//        		}else {
//        			return "密碼有錯";
//        		}
//        	}else {
//        		return "查無此帳號";
//        	}
//        }
//		return null;	
//
//    }
    public boolean checkAc(String ac) {
    	List<String> allAc = repository.findByAc();
    	for(String pk : allAc) {
    		if(pk.equals(ac)) {
    			return true; 
    		}
    	}
    	return false;
    }
    
    public MemberVO login(String ac,String pw) {
    	System.out.println(repository.getOneByAP(ac,pw));
    	return repository.getOneByAP(ac,pw);
    }
    

    public List<MemberVO> getAll() {
        return repository.findAll();
    }

}