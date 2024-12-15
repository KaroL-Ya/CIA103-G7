package com.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forum.model.PostMessageVO;

@Repository
public interface PostMessageRepository extends JpaRepository<PostMessageVO, Integer> {
	
	Optional<PostMessageVO> findById(Integer postmgId);
    List<PostMessageVO> findByPostId(Integer postId);
    Page<PostMessageVO> findByPostId(Integer postId, Pageable pageable); // 支持分頁
}



