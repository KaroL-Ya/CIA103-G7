package com.event.ESU;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface EsuRepo extends JpaRepository<EsuVO, Integer> {
	    List<EsuVO> findByMemberId(Integer memberId);
}
