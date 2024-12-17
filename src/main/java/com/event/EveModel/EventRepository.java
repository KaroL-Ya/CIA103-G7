// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.event.EveModel;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<EventVO, Integer> {

//尚未import memberVO 
	@Transactional
	
	//查找
	@Query(value = "Select * from event where eve_ID=?1 and stat=1 order by dat", nativeQuery = true)
	List<EventVO> findMyHost(Integer eveID);
	
	

	// 外部:透過會員編號搜尋活動，尚未import
	@Query(value = "select * from event where mem_id=?1", nativeQuery = true)
	List<EventVO> findEveByMem(Integer memID);

	// 尋找自己的活動
	@Query(value = "Select * from act where mem_no=?1 and act_status=0 order by act_start", nativeQuery = true)
	List<EventVO> findMyEvent(Integer memNoA);

	//指定刪除
//	@Modifying
//	@Query(value = "delete from event where eve_ID =?1", nativeQuery = true)
//	void deleteByEve_ID(int eveID);
	
	
}
	