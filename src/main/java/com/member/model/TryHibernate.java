//package com.member.model;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import com.admin.model.AdminAuthVO;
//import com.admin.model.AdminFuncVO;
//import com.admin.model.AdminOldVO;
//
//import util.HibernateUtil;
//
//public class TryHibernate {
//	public static void main(String[] args) {
//		Session s1 = HibernateUtil.getSessionFactory().openSession();
//		Session s2 = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx1 = null, tx2 = null;
//		try {
//			tx1 = s1.beginTransaction();
//
//			MemberVO mem1 = s1.get(MemberVO.class, 1001);
//			System.out.println(mem1);
//
//			tx1.commit();
//			
////			tx1 = s1.beginTransaction();
////			MemberVO mem2 = s1.get(MemberVO.class,1002);
////			System.out.println(mem2);
////			tx1.commit();
//			
//			tx2 = s2.beginTransaction();
//			
//			AdminOldVO admin1 = s2.get(AdminOldVO.class, 7003);
//			System.out.println(admin1);
//
//			tx2.commit();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			s1.close();
//			s2.close();
//			HibernateUtil.shutdown();
//		}
//	}
//}
