// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.dept.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DeptRepository extends JpaRepository<DeptVO, Integer> {
    @Query(value = "select dname from dept",nativeQuery = true)
    List<String> findByDname();
}