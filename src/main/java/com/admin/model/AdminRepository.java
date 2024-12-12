package com.admin.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AdminRepository extends JpaRepository<AdminVO, Integer> {
	
    @Transactional
    @Modifying
    @Query(value = "delete from admin where admin_Id =?1", nativeQuery = true)
    void deleteByAdmin_Id(int admin_Id);

    //● (自訂)條件查詢
    @Query(value = "from AdminVO where admin_Id=?1 and admin_Name like?2 order by admin_Id")
    List<AdminVO> findByOthers(int admin_Id , String name);
    
    @Query(value = "select admin_Ac from admin",nativeQuery = true)
    List<String> findByAdmin_Ac();
    
    @Query(value = "from AdminVO where admin_Ac=?1 and admin_Pw=?2")
    AdminVO getOneByAdmin_AP(String admin_Ac,String Admin_Pw);
    
    
//  @Query(value = "select admin_Pw from admin where admin_Ac=?",nativeQuery = true)
//  String findGetOneByAdmin_Pw(String admin_Ac);
    
//    @Query(value = "from AdminVO where admin_Ac=?1")
//    AdminVO useAdmin_AcFindAdmin_Id(String admin_Ac);
}