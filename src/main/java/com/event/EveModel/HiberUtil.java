package com.event.EveModel;
/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */


import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//import hibernate.util.HibernateUtil;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面

import com.event.EveModel.EventVO;

public class HiberUtil {
	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<EventVO> root, String columnName, String value) {

		Predicate predicate = null;

		
		if ("eve_name".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("loc".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("detail".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("stat".equals(columnName))
            predicate = builder.equal(root.get(columnName), Byte.valueOf(value));
        
        

		return predicate;
	}
/*
	@SuppressWarnings("unchecked")
	public static List<EventVO> getAllC(Map<String, String[]> map, Session session) {

//		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		List<EventVO> list = null;
		try {
			// 【●創建 CriteriaBuilder】
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 【●創建 CriteriaQuery】
			CriteriaQuery<EventVO> criteriaQuery = builder.createQuery(EventVO.class);
			// 【●創建 Root】
			Root<EventVO> root = criteriaQuery.from(EventVO.class);

			List<Predicate> predicateList = new ArrayList<Predicate>();
			
		 Set<String> keys = map.keySet();
	          for (String key : keys) {
	              String value = map.get(key)[0];
	              if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                   Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
                  if (predicate != null) {
                      predicateList.add(predicate);
                  }
              }
          }
			System.out.println("predicateList.size()="+predicateList.size());
			criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));		//where 條件
			criteriaQuery.orderBy(builder.desc(root.get("eve_ID")));
			// 【●最後完成創建 javax.persistence.Query●】
			Query query = session.createQuery(criteriaQuery); 
			list = query.getResultList();

			tx.commit();
		} catch (RuntimeException ex) {
			if (tx != null)
				tx.rollback();
			throw ex; // System.out.println(ex.getMessage());
		} finally {
			session.close();
			// HibernateUtil.getSessionFactory().close();
		}

		return list;
	}
	*/
}
