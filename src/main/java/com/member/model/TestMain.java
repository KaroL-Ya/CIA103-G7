package com.member.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class TestMain {
    public static void main(String[] args) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            SessionFactory factory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = factory.openSession(); // like JDBC connection 要先取得
            Transaction tx = session.beginTransaction();
            
            //新增一筆 save後 -> SQL指令呢？ -> hibernate 就醬
            MemberVO member = new MemberVO();
            member.setAc("testac123");
            member.setPw("testpw123");
            member.setEmail("testmail123@gmail.com");
            member.setStatus(0);
            member.setName("測試用");
            member.setBirth(java.sql.Date.valueOf("2024-11-26"));
            member.setSex("男");
            member.setPhone("0900123456");
            member.setCity("桃園市");
            member.setDisc("中壢區");
            member.setAddress("復興路");
            session.save(member);
            
            // 執行完畢 關閉資源
            tx.commit();
            session.close();
            factory.close();
        
    }
}