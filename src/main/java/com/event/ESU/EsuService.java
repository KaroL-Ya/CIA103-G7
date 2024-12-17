package com.event.ESU;

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
public class EsuService {

    @Autowired
    private EsuRepo repo;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EventService eventService;
//EvtOrder = esuVO    
    public EsuVO findById(int id) {
        return repo.findById(id).orElse(null);
    }
    
    public List<EsuVO> findAllEvtOrders() {
        return repo.findAll();
    }

    public EsuVO saveEsuVO(EsuVO esuVO) {
        return repo.save(esuVO); 
    }
//	以controller實現
//    public EsuVO saveEsuVO(EsuVO esuVO, Integer memberId, Integer eventId) {
//
//    	MemberVO member = memberService.getOneMember(memberId);
//        EventVO event = eventService.findById(eventId);
//
//        EsuVO newEsuVO = new EsuVO();
//        newEsuVO.setMemID(member);
//        newEsuVO.setEveID(event);
//        newEsuVO.setEsuStat(esuVO.getEsuStat()); 
//        
//        
//        return repo.save(newEsuVO);
//    }
    
    public EsuVO updateStatus(EsuVO EsuID) {
        EsuVO eo = findById(EsuID.getEsuID());
        eo.setEsuStat(EsuID.getEsuStat());
        return repo.save(eo);

    }
}

