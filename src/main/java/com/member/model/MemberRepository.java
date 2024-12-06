package com.member.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

        @Transactional
        @Modifying
        @Query(value = "delete from member where mem_Id =?1", nativeQuery = true)
        void deleteByMem_Id(int mem_Id);

        //● (自訂)條件查詢
        @Query(value = "from MemberVO where mem_Id=?1 and name like?2 and birth=?3 order by mem_Id")
        List<MemberVO> findByOthers(int mem_Id , String name , java.sql.Date birth);
        
        @Query(value = "select ac from member",nativeQuery = true)
        List<String> findByAc();
        
        @Query(value = "select pw from member where ac=?",nativeQuery = true)
        String findGetOneByPw(String ac);
        
        @Query(value = "from MemberVO where ac=?1")
        MemberVO useAcFindId(String ac);
        
        @Query(value = "from MemberVO where ac=?1 and pw=?2")
        MemberVO getOneByAP(String ac,String pw);
    }