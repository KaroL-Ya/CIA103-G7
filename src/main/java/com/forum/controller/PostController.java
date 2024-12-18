package com.forum.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.forum.model.PostMessageVO;
import com.forum.model.PostVO;
import com.forum.service.PostMessageService;
import com.forum.service.PostService;

@Controller
//@RequestMapping("/forum")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostMessageService postMessageService;
    
    @Autowired
    private HttpSession session;

    // GetMapping 用於顯示帖文更新頁面
    @GetMapping("/forum/updateStatus/{id}")
    public String showUpdateStatusPage(@PathVariable Integer id, Model model) {
        PostVO post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/error"; // 如果找不到貼文，重定向到錯誤頁面
        }

        model.addAttribute("post", post); // 將貼文對象添加到模型中，傳遞給視圖頁面
        model.addAttribute("currentMemberId", session.getAttribute("mem_Id")); // 當前用戶ID
        
        return "redirect:/forum"; // 返回更新頁面
    }
    
    // PostMapping 用於處理貼文狀態更新
    @PostMapping("/forum/updateStatus")
    public String updatePostStatus(@RequestParam Integer id, @RequestParam Byte status) {
        PostVO post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/error"; // 如果找不到貼文，重定向到錯誤頁面
        }

        // 更新貼文狀態
        post.setStatus(status);
        postService.savePost(post); // 假設有一個方法可以更新貼文
       
        return  "forward:/WEB-INF/views/postManage.jsp"; // 更新成功後重定向到貼文列表頁
    }

    @GetMapping("/forum")
    public String forum(Model model, 
                        @RequestParam(defaultValue = "0") int page, 
                        @RequestParam(defaultValue = "5") int size) {
        Integer memId = (Integer) session.getAttribute("mem_Id");
        
        // 檢查用戶是否已被停權
        if (memId != null) {
            // 假設有一個方法可以檢查用戶狀態
            boolean isBanned = postService.isUserBanned(memId);
            if (isBanned) {
                model.addAttribute("message", "您的帳號已被停權，無法進入論壇。");
                return "forum/banned"; // 返回停權頁面
            }
        }

        // 確保頁碼大於等於 0
        if (page < 0) { page = 0; } // 頁碼不能小於 0 
        
        Pageable pageable = PageRequest.of(page, size); // 設定分頁參數，頁數從 0 開始
        Page<PostVO> postPage = postService.getAllPosts(pageable);

        model.addAttribute("posts", postPage); // 將當前頁面的貼文傳遞給視圖
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages()); // 總頁數
        model.addAttribute("size", size); // 總條目數
        model.addAttribute("mem_Id", session.getAttribute("mem_Id")); // 將會員 ID 傳遞到模型
        
        return "forum/forum"; // 返回論壇頁面
    }


 
     

    @GetMapping("/back-end/forum/postManage") // 查詢論壇管理

    public String postManage(Model model) {
        List<PostVO> posts = postService.getAllPosts(); // 
        model.addAttribute("posts", posts);
        return "forward:/WEB-INF/views/postManage.jsp"; 
    }
   


    @GetMapping("/forum/create") // 顯示新增貼文頁面
    public String createPostPage(Model model) {
        Integer cafeId = (Integer) session.getAttribute("cafeId"); // 從 session 中獲取咖啡廳編號
        Integer memId =(Integer)session.getAttribute("mem_Id"); // 從 session 中獲取會員編號
    	
        if (memId == null) {
			return "redirect:/login";
		}
        
        
        model.addAttribute("memberId", memId);
        model.addAttribute("cafeId", cafeId);
        return "forward:/WEB-INF/views/createPost.jsp"; // 返回新增貼文頁面
    }
    
    @GetMapping("/forum/post")
    public String getPost(@RequestParam(required = false) Integer id, Model model) {
        if (id == null) {
            return "redirect:/error"; // 假設有一個錯誤頁面
        }
        PostVO p = postService.getPostById(id);
        model.addAttribute("post", p);

        // 獲取該貼文的留言
        List<PostMessageVO> messages = postMessageService.getMessagesByPostId(id);
        model.addAttribute("messages", messages);
        
        // 設置 postId 和 currentMemberId
        model.addAttribute("postId", id);
        model.addAttribute("currentMemberId", (Integer) session.getAttribute("mem_Id"));

     // 檢查是否該會員是此貼文的作者
        Integer currentMemberId = (Integer) session.getAttribute("mem_Id");
        model.addAttribute("isOwner", currentMemberId != null && currentMemberId.equals(p.getMemId()));

        return "forward:/forum";
    }
    
    @GetMapping("/forum/update/{id}")
    public String toUpdatePost(@PathVariable("id") Integer postId, Model model) {
    	PostVO post = postService.getPostById(postId);
    	model.addAttribute("post", post);
    	
    	
    	return "forward:/WEB-INF/views/postList.jsp";
    }
    
    

    
    @PostMapping("/forum/update")
    public String updatePost(@RequestParam Integer id, @RequestParam String title, @RequestParam String content) {
        PostVO post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/error"; // 如果找不到貼文，重定向到錯誤頁面
        }

        Integer currentMemberId = (Integer) session.getAttribute("mem_Id");
        Integer cafeId = (Integer) session.getAttribute("cafeId");

        // 檢查當前用戶是否是貼文的擁有者或商家
        if (!post.getMemId().equals(currentMemberId) && !post.getCafeId().equals(cafeId)) {
            return "redirect:/error"; // 如果不是擁有者，重定向到錯誤頁面
        }

        
        // 更新標題和內容
        post.setTitle(title);
        post.setContent(content);
        // 保存更新
        postService.savePost(post);
        
        return "redirect:/forum"; // 重定向到更新後的貼文頁面
    }
    
    @PostMapping("/forum/create") // 創建貼文
    public String createPost(@ModelAttribute PostVO post) {
    	System.out.println((Integer)session.getAttribute("mem_Id"));
    	System.out.println((Integer)session.getAttribute("cafe_Id"));
    	
    	   Integer memId = (Integer) session.getAttribute("mem_Id");
    	    Integer cafeId = (Integer) session.getAttribute("cafeId");
    	
    	    post.setMemId(memId); // 設置會員 ID
    	    post.setCafeId(cafeId); // 設置咖啡廳 ID
        
        postService.createPost(post);
        return "redirect:/forum"; // 重定向到首頁
    }

    @PostMapping("/forum/delete") // 刪除貼文
    public String deletePost(@RequestParam Integer id) {
        
        Integer memId = (Integer) session.getAttribute("mem_Id");// 取得當前會員 ID
       
         if (memId == null) {
            return "redirect:/login"; // 如果沒登入則跳轉到登入頁
        }

        PostVO post = postService.getPostById(id); // 根據 ID 獲取貼文
        if (post == null) {
            return "redirect:/error"; // 如果找不到貼文，重定向到錯誤頁面
        }

        // 檢查該貼文是否為當前會員所擁有
        if (!post.getMemId().equals(memId)) {
            return "redirect:/error"; // 如果不是該會員的貼文，重定向到錯誤頁面
        }

        // 刪除該貼文
        postService.deletePost(id);

        return "redirect:/forum"; // 刪除後重定向回論壇頁面
    }

    
    @PostMapping("/forum/like") // 按讚功能
    public String likePost(@RequestParam Integer postId) {
        // 取得目前的會員 ID
        Integer memId = (Integer) session.getAttribute("mem_Id");
        
        if (memId == null) {
            return "redirect:/login"; // 如果沒登入則跳轉到登入頁
        }

        PostVO post = postService.getPostById(postId);
        if (post != null) {
            // 更新按讚數量，這裡簡單地將 count 增加 1
            post.setCount(post.getCount() + 1);
            postService.savePost(post); // 保存更新
        }
        
        return "redirect:/forum" ; // 重新定向回該貼文頁面
    }
}
