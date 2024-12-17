package com.lookcafe;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query;


public class HibernateUtil_CompositeQuery_cafe {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<LookCafeVO> root, String columnName, String value) {
        Predicate predicate = null;

        // 根據欄位名來生成條件
        if ("cafeId".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
        } 
            else if ("city".equals(columnName)) {
            predicate = builder.equal(root.get(columnName), String.valueOf(value));
        } 
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
    public static List<LookCafeVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<LookCafeVO> list = null;
        try {
            // 創建 CriteriaBuilder
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 創建 CriteriaQuery
            CriteriaQuery<LookCafeVO> criteriaQuery = builder.createQuery(LookCafeVO.class);
            // 創建 Root
            Root<LookCafeVO> root = criteriaQuery.from(LookCafeVO.class);

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
            criteriaQuery.orderBy(builder.asc(root.get("cafeId"))); // 或其他字段作為排序依據

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
