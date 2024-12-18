package com.event.Participate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.event.EveModel.EventVO;
import com.member.model.MemberVO;


public interface ParticipateRepository extends JpaRepository<ParticipateVO, Integer> {
//	@Transactional
//	@Modifying
//	@Query(value = "delete from event_register where esu_id =?1", nativeQuery = true)
//	void deleteByEsuID(int esu_ID);
//	 @Query("SELECT p.eveID FROM ParticipateVO p WHERE p.memID.mem_Id = :memberId")
//	    List<EventVO> findEventsByMemberId(@Param("memberId") Integer memberId);
	//獲取(這個)
	 @Query("SELECT p FROM ParticipateVO p WHERE p.memID.mem_Id = :memId")
	 List<ParticipateVO> findByMemID_MemId(@Param("memId") Integer memId);
	 
	 //查詢
	 @Query("SELECT e FROM ParticipateVO p JOIN p.eveID e WHERE p.memID.mem_Id = :memberId")
	    List<EventVO> findEventsByMemberId(Integer memberId);
	 
	 @Modifying	//刪除
	 @Query("DELETE FROM ParticipateVO p WHERE p.memID.mem_Id = :memId AND p.eveID.eveID = :eventId")
	 void cancelRegistration(@Param("memId") Integer memId, @Param("eventId") Integer eventId);
	 
	 //獲取重複報名
	 @Query("SELECT COUNT(p) > 0 FROM ParticipateVO p WHERE  p.memID.mem_Id = :memId AND p.eveID.eveID = :eveId")
	  boolean Repeat(@Param("memId") Integer memberId, @Param("eveId") Integer eventId);
	
	
	  
	  //● (自訂)條件查詢
}
