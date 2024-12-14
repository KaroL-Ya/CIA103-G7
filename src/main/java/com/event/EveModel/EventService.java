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

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Create or update event
    public void addEvent(EventVO event) {
         eventRepository.save(event);
    }
    
    public EventVO updateEvent(EventVO event) {
        return eventRepository.save(event);
    }

    // Read all events
    public List<EventVO> getAllEvents() {
        return eventRepository.findAll();
    }
    
    // Get event by ID
    public Optional<EventVO> getEventById(Integer event) {
        return eventRepository.findById(event);
    }
    
//    // Search for Event(Condition)
//    public List<EventVO> searchEvents(LocalDateTime startDate, LocalDateTime endDate,Integer status) {
//        if (searchTerm == null || searchTerm.isEmpty()) {
//            return eventRepository.findAll();
//        } else {
//            return eventRepository.findByDateRangeAndStatus(searchTerm);
//        }
        
    
    

    // Delete event by ID
    public void deleteEvent(Integer event) {
//    	eventRepository.existsById(event))
			eventRepository.deleteById(event);
    }
}

/*
@Transactional
@Service("eventService")
public class EventService {

	@Autowired
	EventRepository repository;

	@Autowired
    private SessionFactory sessionFactory;
	

	
	//新增
	public void addEvent(EventVO eventVO) {
		repository.save(eventVO);
	}
	//修改
	public void updateEvent(EventVO eventVO) {
		repository.save(eventVO);
	}
	//刪除
	public void deleteEvent(Integer eveID) {
		if (repository.existsById(eveID))
			repository.deleteByEve_ID(eveID);

	}
	//查一
	public EventVO getOneEvent(Integer eveID) {
		Optional<EventVO> optional = repository.findById(eveID);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值，簡化if語法
	}
	
	
	public List<EventVO> getAll() {
		return repository.findAll();
	}

	//依會員編號查詢活動(包含已舉辦)
	public List<EventVO> getActsByMem(Integer memID) {
		return repository.findEveByMem(memID);
	}
	//萬用複合查詢
//	public List<EventVO> getAll(Map<String, String[]> map) {
//		return HiberUtil.getAllC(map,sessionFactory.openSession());
//	}
	
	//查找自己正在舉辦的活動rm6
	public List<EventVO> findMyEvent(Integer memID) {
		return repository.findMyEvent(memID);
	}
	//修改活動狀態
	public void updateActStatus(Integer eveID, Integer newStatus) {
		  repository.updateEveState(eveID, newStatus);
	}
	
}
*/