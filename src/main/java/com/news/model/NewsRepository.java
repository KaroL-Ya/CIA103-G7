// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.news.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NewsRepository extends JpaRepository<NewsVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from news where news_id =?1", nativeQuery = true)
	void deleteBynews_id(int news_id);

}