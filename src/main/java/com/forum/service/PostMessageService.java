package com.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.forum.model.PostMessageVO;
import com.forum.repository.PostMessageRepository;

import oracle.jdbc.proxy.annotation.Post;

@Service
public class PostMessageService {
    @Autowired
    private PostMessageRepository postMessageRepository;

    public PostMessageVO createPostMessage(PostMessageVO postMessage) {
        return postMessageRepository.save(postMessage);
    }

    public List<PostMessageVO> getMessagesByPostId(Integer postId) {
        return postMessageRepository.findByPostId(postId);
    }

    public Page<PostMessageVO> getMessagesByPostId(Integer postId, Pageable pageable) {
        return postMessageRepository.findByPostId(postId, pageable); // 使用分頁
    }

    public void deletePostMessage(Integer postmgId) {
        postMessageRepository.deleteById(postmgId);
    }

//    public Optional<PostMessageVO> findPostById(Integer postId) {
//        Optional<PostMessageVO> optionalPost = postMessageRepository.findById(postId);
//        return optionalPost; // 如果找不到，返回 null
//    }
 // 透過留言 ID 查詢留言
    public Optional<PostMessageVO> getPostMessageById(Integer postmgId) {
        return postMessageRepository.findById(postmgId); // 透過 ID 查詢單一留言
    }

    // 更新留言
    public PostMessageVO updatePostMessage(PostMessageVO postMessage) {
        return postMessageRepository.save(postMessage); // 保存更新過的留言
    }
}
