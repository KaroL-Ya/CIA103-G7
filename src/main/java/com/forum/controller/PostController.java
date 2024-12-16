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
@RequestMapping("/forum")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostMessageService postMessageService;
    
    @Autowired
    private HttpSession session;

//
    @GetMapping("")
    public String forum(Model model, 
                        @RequestParam(defaultValue = "1") int page, 
                        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size); // 設定分頁參數，頁數從 0 開始
        Page<PostVO> postPage = postService.getAllPosts(pageable);

        model.addAttribute("posts", postPage); // 將當前頁面的貼文傳遞給視圖
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages()); // 總頁數
        model.addAttribute("size", size); // 總條目數
        model.addAttribute("mem_Id", session.getAttribute("mem_Id"));// 將會員 ID 傳遞到模型
        return "forum/forum"; // 返回論壇頁面
    }

//    @GetMapping("")
//    public String forum(Model model) {
//        List<PostVO> posts = postService.getAllPosts(); // 獲取所有貼文
//        model.addAttribute("posts", posts);
//        model.addAttribute("mem_Id", session.getAttribute("mem_Id")); // 將會員 ID 傳遞到模型
//        return "forum/forum"; 
//    }

        
    @GetMapping("/postManage") // 查詢論壇管理
    public String postManage(Model model) {
        List<PostVO> posts = postService.getAllPosts(); // 
        model.addAttribute("posts", posts);
        return "forward:/WEB-INF/views/postManage.jsp"; 
    }
    

    @GetMapping("/postsByMemId")
    public String getPostsByMemId(@RequestParam Integer mem_Id, 
                                   @RequestParam(defaultValue = "1") int page, 
                                   Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5); // 每頁顯示 5 筆資料
        Page<PostVO> postPage = postService.getPostsByMemId(mem_Id, pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());  // 確保傳遞 totalPages
        model.addAttribute("mem_Id", mem_Id);
        
        return "forum/forum";  // 返回模板
    }





    @GetMapping("/create") // 顯示新增貼文頁面
    public String createPostPage(Model model) {
//        Integer cafeId = (Integer) session.getAttribute("cafeId"); // 從 session 中獲取咖啡廳編號
//        model.addAttribute("cafeId", cafeId);
        Integer memId =(Integer)session.getAttribute("mem_Id"); // 從 session 中獲取會員編號
    	
        if (memId == null) {
			return "redirect:/login";
		}
        
        
        model.addAttribute("memberId", memId);
        return "forward:/WEB-INF/views/createPost.jsp"; // 返回新增貼文頁面
    }
    
    @GetMapping("/post")
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
    
    @GetMapping("/update/{id}")
    public String toUpdatePost(@PathVariable("id") Integer postId, Model model) {
    	PostVO post = postService.getPostById(postId);
    	model.addAttribute("post", post);
    	
    	
    	return "forward:/WEB-INF/views/postList.jsp";
    }
    
    

    
    @PostMapping("/update")
    public String updatePost(@RequestParam Integer id, @RequestParam String title, @RequestParam String content) {
        PostVO post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/error"; // 如果找不到貼文，重定向到錯誤頁面
        }

        // 更新標題和內容
        post.setTitle(title);
        post.setContent(content);
        
        // 設置會員 ID
        post.setMemId((Integer) session.getAttribute("mem_Id"));
        
        // 保存更新
        postService.savePost(post);
        
        return "redirect:/forum"; // 重定向到更新後的貼文頁面
    }
    
    @PostMapping("/create") // 創建貼文
    public String createPost(@ModelAttribute PostVO post) {
    	System.out.println((Integer)session.getAttribute("mem_Id"));
//    	System.out.println((Integer)session.getAttribute("cafe_Id"));
    	
        post.setMemId((Integer) session.getAttribute("mem_Id")); // 設置會員 ID
//        post.setCafeId((Integer) session.getAttribute("cafeId"));
        postService.createPost(post);
        return "redirect:/forum"; // 重定向到首頁
    }

    @PostMapping("/delete") // 刪除貼文
    public String deletePost(Integer id) {
        postService.deletePost(id);
        return "redirect:/forum/postManage"; // 重定向到首頁
    }
    
    @PostMapping("/like") // 按讚功能
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
