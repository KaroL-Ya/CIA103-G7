package com.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.forum.model.PostVO;
import com.forum.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public PostVO createPost(PostVO post) {
        return postRepository.save(post);
    }

    public PostVO getPostById(Integer postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<PostVO> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

	
	public void savePost(PostVO post) {
	    postRepository.save(post); // 將更新的貼文保存到資料庫
	}

	public Page<PostVO> getPostsByMemId(Integer mem_Id, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
