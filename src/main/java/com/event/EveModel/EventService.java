package com.event.EveModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.event.EveModel.EventVO;
import com.event.Participate.ParticipateVO;
import com.event.Participate.ParticipateService;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;

@Transactional
@Service
public class EventService {

    @Autowired
    private EventRepository eveRepo;

    // Get event by ID
    public EventVO findById(Integer eventID) {
        return eveRepo.findById(eventID).orElse(null);
    }
    
    // Create event
    public void addEvent(EventVO eventID) {
         eveRepo.save(eventID);
    }

    // Read all events
    public List<EventVO> getAllEvents() {
        return eveRepo.findAll();
    }
    
    // Update event status
    public EventVO updateEventStatus(EventVO eventID) {
    	EventVO e = findById(eventID.getEveID());
    	e.setStat(eventID.getStat());
    	eveRepo.save(e);
    	return eveRepo.save(eventID);
    }
    
    // Update event attendance
    public EventVO updateEvent(EventVO eventID) {
    	EventVO e = findById(eventID.getEveID());
    	e.setNum(eventID.getNum());
    	return eveRepo.save(eventID);
    }
  
    // Delete event by ID
    public void deleteEvent(Integer eventID) {
//    	eveRepo.existsById(event))
    	eveRepo.deleteById(eventID);
    }
    
    
    public void attendEvent(Integer eventID) {
    	
        EventVO event = findById(eventID);
        // 檢查報名人數
        int participant = event.getNum();
        if (participant > event.getLim()) {
            throw new IllegalArgumentException("名額已滿");
        }

        // 更新活動人數
        event.setNum(participant+1);
        updateEvent(event);        
    }
 
    // Search for Event(Condition)
//    public List<EventVO> searchEvents(LocalDateTime startDate, LocalDateTime endDate,Integer status) {
//        if (searchTerm == null || searchTerm.isEmpty()) {
//            return eveRepo.findAll();
//        } else {
//            return eveRepo.findByDateRangeAndStatus(searchTerm);
//        }
//    }
    
    

}
