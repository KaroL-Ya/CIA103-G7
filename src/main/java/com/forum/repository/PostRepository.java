package com.forum.repository;

import com.forum.model.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostVO, Integer> {
    List<PostVO> findByCafeId(Integer cafeId); // 根據咖啡廳編號查詢貼文
}