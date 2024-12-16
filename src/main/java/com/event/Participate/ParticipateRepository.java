package com.event.Participate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ParticipateRepository extends JpaRepository<ParticipateVO, Integer> {

//	@Transactional
//	@Modifying
//	@Query(value = "delete from event_register where esu_id =?1", nativeQuery = true)
//	void deleteByEsuID(int esu_ID);

	//● (自訂)條件查詢
}
