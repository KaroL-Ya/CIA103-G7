package com.forum.repository;

import com.forum.model.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<PostVO, Integer> {
    // 查詢所有貼文並支持分頁
    Page<PostVO> findAll(Pageable pageable);
}
