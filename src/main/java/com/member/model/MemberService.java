package com.member.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.MailService;

@Service("memberService")
public class MemberService {

    @Autowired
    MemberRepository repository;

    @Autowired
    private SessionFactory sessionFactory;

    public void addMember(MemberVO memberVO) {
//		System.out.println("隨機驗證碼:"+vCode(6));
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
    
    public boolean checkAc(String ac) {
    	List<String> allAc = repository.findByAc();
    	for(String pk : allAc) {
    		if(pk.equals(ac)) {
    			return true; 
    		}
    	}
    	return false;
    }
    
    public boolean checkEmail(String email) {
    	List<String> allEmail = repository.findByEmail();
    	for(String e : allEmail) {
    		if(e.equals(email)) {
    			return true; 
    		}
    	}
    	return false;
    }
    
    public MemberVO login(String ac,String pw) {
    	return repository.getOneByAP(ac,pw);
    }
    

    public List<MemberVO> getAll() {
        return repository.findAll();
    }
    
    public String vCode(int length) {
    	String s ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	StringBuffer sb = new StringBuffer();
    	for(int i=1;i<=length;i++) {
    		int index  = (int) (Math.random()*s.length());
    		sb.append(s.charAt(index));
    	}
    	return sb.toString();
    }
    
    public void sendMail(String to, String subject, String messageText) {
    	util.MailService.sendMail(to, subject, messageText);
    }
    
    public void updateStatus(String email) {
    	repository.updateStatus(email);
    }
    
    public boolean checkVerifiedEmail(String email) {
    	List<String> allVerifiedEmail = repository.findByVerifiedEmail();
    	for(String ve : allVerifiedEmail) {
    		if(ve.equals(email)) {
    			return true; 
    		}
    	}
    	return false;
    }
    
    public void updateForGotPw(String pw,String email) {
    	repository.updateForGotPw(pw, email);
    }
    
//  public String checkLogin(String ac,String pw) {
//	String msg = "";
//    List<String> allAc = repository.findByAc();
//    String getOnePw = repository.findGetOneByPw(ac);
//    MemberVO memberVO = repository.useAcFindId(ac);
//    System.out.println(memberVO);
//    for(String pk : allAc) {
//    	if(pk.equals(ac)) {
//    		if(pw.equals(getOnePw)) {
//    			return "符合";
//    		}else {
//    			return "密碼有錯";
//    		}
//    	}else {
//    		return "查無此帳號";
//    	}
//    }
//	return null;	
//
//}

}