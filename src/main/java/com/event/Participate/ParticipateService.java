package com.event.Participate;
import java.util.List;
import java.util.Optional;

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
    
    //預設
    public ParticipateVO saveParticipate(ParticipateVO participateVO) {
    	
        return PartRepo.save(participateVO);
    }
    //活動報名
    public void addParticipant(ParticipateVO participantVO) {
		EventVO managedActVO = EveRepo.findById(participantVO.getEveID().getEveID()).orElseThrow();
        participantVO.setEveID(managedActVO);
		PartRepo.save(participantVO);
	}
    
    //活動報名 人數無法更新
//    public ParticipateVO attendEvent(ParticipateVO participateVO) {
//        return PartRepo.save(participateVO);
//    }
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
