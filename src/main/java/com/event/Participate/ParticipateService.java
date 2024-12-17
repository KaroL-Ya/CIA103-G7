package com.event.Participate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.event.EveModel.*;
import com.member.model.*;

//
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
//

@Service
public class ParticipateService {

    @Autowired
    private ParticipateRepository PartRepo;

    @Autowired
    private EventRepository EveRepo;

    @Autowired
    private MemberRepository MemRepo;
    
    @Autowired
    private MemberService memberService;
    
    //預設
    public ParticipateVO saveParticipate(ParticipateVO participateVO) {
    	
        return PartRepo.save(participateVO);
    }
    
    //活動報名 (傳值PartVO)
    public void addParticipant1(ParticipateVO participantVO) {
		EventVO attend = EveRepo.findById(participantVO.getEveID().getEveID()).orElseThrow();
        participantVO.setEveID(attend);
		PartRepo.save(participantVO);
	}
    
    //活動報名 (傳值EveVO)
    public void addParticipant2(EventVO eventVO) {
		EventVO attend = EveRepo.findById(eventVO.getEveID()).orElseThrow();
		ParticipateVO participantVO = new ParticipateVO();
        participantVO.setEveID(attend);
		PartRepo.save(participantVO);
	}
    
    //活動報名 現在使用
@Transactional
//    public String addParticipant(Integer eventID, HttpSession session) {
    public String addParticipant(Integer eventID) {
        // Retrieve the current event
        EventVO eventVO = EveRepo.findById(eventID).orElseThrow(() -> new IllegalArgumentException(""));

        // 會員獲取session
//        Integer memberId = (Integer) session.getAttribute("mem_id"); 
//        MemberVO memberVO = memberService.getOneMember(memberId); 

        // ID創VO轉存
        ParticipateVO partVO = new ParticipateVO();
        partVO.setEveID(eventVO);
//        partVO.setMemID(memberVO);
        PartRepo.save(partVO);

        // 會員更新
        eventVO.setNum(eventVO.getNum() + 1);
        EveRepo.save(eventVO);

        return "redirect:/event/ShowDetails/" ; // Redirect with success message
//        return "redirect:/event/ShowDetails/" + eventID + "?success=true"; // Redirect with success message
    }

    //所有活動
    public List<ParticipateVO> getall() {
        return PartRepo.findAll();
    }
    //根據報名序號
    public ParticipateVO getOneEvt(Integer esuID) {
        Optional<ParticipateVO> optional = PartRepo.findById(esuID);
        return optional.orElse(null);
    }

    //更新活動
	public void updateParticipant(ParticipateVO participantVO) {
		PartRepo.save(participantVO);
	}
	
	//取消報名
	public void deleteParticipant(Integer esuID) {
		if (PartRepo.existsById(esuID))
			PartRepo.deleteById(esuID);
	}
	


//	public List<ParticipantVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Participant.getAllC(map,sessionFactory.openSession());
//	}
	
    
    //Part service
//    public void registerForEvent(ParticipateVO participantVO) {
//    	EventVO event = eventRepository.findById(eventId).orElse(null);
//    	participantVO.setActVO(managedActVO);
//		repository.save(participantVO);
//	}

//    public Page<ParticipateVO> getAllEvtorders(int page) {
//        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("evtOrderId").descending());
//        return PartRepo.findAll(pageRequest);
//    }
		
    

   
	
}