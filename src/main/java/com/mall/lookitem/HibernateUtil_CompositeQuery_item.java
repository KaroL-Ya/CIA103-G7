package com.mall.lookitem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query;


public class HibernateUtil_CompositeQuery_item {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<LookItemVO> root, String columnName, String value) {
        Predicate predicate = null;

        // 根據欄位名來生成條件
        if ("itemId".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } 
//            else if ("memId".equals(columnName)) {
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        } 
        else if ("name".equals(columnName)) {
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        } 
//            else if ("time".equals(columnName)) {
//            predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
//        }   else if ("status".equals(columnName)) {
//            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//        }
        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<LookItemVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<LookItemVO> list = null;
        try {
            // 創建 CriteriaBuilder
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 創建 CriteriaQuery
            CriteriaQuery<LookItemVO> criteriaQuery = builder.createQuery(LookItemVO.class);
            // 創建 Root
            Root<LookItemVO> root = criteriaQuery.from(LookItemVO.class);

            List<Predicate> predicateList = new ArrayList<Predicate>();
           
            Set<String> keys = map.keySet();
            int count = 0;
            // 動態構建查詢條件
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
                    System.out.println("有送出查詢資料的欄位數count = " + count);
                }
            }
            System.out.println("predicateList.size()="+predicateList.size());
            // 設置條件
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQuery.orderBy(builder.asc(root.get("itemId"))); // 或其他字段作為排序依據

            // 執行查詢
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return list;
    }
}
