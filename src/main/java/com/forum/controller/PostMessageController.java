package com.forum.controller;

import com.forum.model.PostMessageVO;
import com.forum.model.PostVO;
import com.forum.service.PostMessageService;
import com.forum.service.PostService;

import oracle.jdbc.proxy.annotation.Post;

import com.mysql.cj.protocol.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostMessageController {
    
    @Autowired
    private PostMessageService postMessageService;
    
    @Autowired
    private PostService postService;


@GetMapping("/{postId}")
public String message(@PathVariable Integer postId, Model model, HttpSession session) {
    
	List<PostMessageVO> posts = postMessageService.getMessagesByPostId(postId); 
    model.addAttribute("posts", posts);
    
    PostVO post = postService.getPostById(postId);
    
    // 從會話中獲取當前使用者的 ID
    Integer currentMemberId = getCurrentMemberId(session);
    model.addAttribute("currentMemberId", currentMemberId);
    model.addAttribute("post",post);
    model.addAttribute("postId", postId);
    return "forum/postMessage"; // 返回貼文列表頁面
}

//private Integer getCurrentMemberId(HttpSession session) {
//    // 假設使用者 ID 存儲在會話中的 "userId" 屬性中
//    Object memId = session.getAttribute("mem_Id");
//    if (memId != null) {
//        return (Integer)memId; // 轉換為 Integer 類型
//    }
//    return null; // 如果沒有找到 ID，返回 null
//}
    
    
    

//    @PostMapping("/{postId}/message") // 創建留言
//    public String createPostMessage(@PathVariable Integer postId, 
//                                     @RequestParam Integer memId, 
//                                     @RequestParam String mgContent) {
//        try {
//            System.out.println(postId + memId + mgContent);
//            PostMessageVO postMessage = new PostMessageVO(postId, memId, getCurrentCafeId(), mgContent);
//            postMessageService.createPostMessage(postMessage);
//            return "redirect:/post/" + postId; // 重定向到相應的留言列表頁面
//        } catch (Exception e) {
//            e.printStackTrace(); // 打印錯誤信息
//            return "error"; // 返回錯誤頁面
//        }
//    }

    @PostMapping("/{postId}/message")
    public String createPostMessage(@PathVariable Integer postId, 
                                     @RequestParam String mgContent, 
                                     HttpSession session) {
        try {
            Integer memId = getCurrentMemberId(session); // 獲取 memId
            if (memId == null) {
                throw new IllegalArgumentException("memId is required");
            }
            System.out.println(postId + " " + memId + " " + mgContent);
            PostMessageVO postMessage = new PostMessageVO(postId, memId, getCurrentCafeId(), mgContent);
            postMessageService.createPostMessage(postMessage);
            return "redirect:/post/" + postId;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login"; // 返回
        }
    }


    @PostMapping("/{postId}/message/{messageId}/edit") // 編輯留言
    public String updatePostMessage(@PathVariable Integer postId, 
                                    @PathVariable Integer messageId, 
                                    @RequestParam String mgContent, 
                                    HttpSession session) {
        try {
            Integer memId = getCurrentMemberId(session); // 從會話中取得會員 ID
            if (memId == null) {
                throw new IllegalArgumentException("memId is required");
            }

            // 從資料庫查詢留言
            PostMessageVO postMessage = postMessageService.getPostMessageById(messageId)
                                                         .orElseThrow(() -> new IllegalArgumentException("留言未找到"));

            if (postMessage != null && postMessage.getMemId().equals(memId)) {
                postMessage.setMgContent(mgContent); // 更新留言內容
                postMessage.setMgUpdate(new Date()); // 更新時間戳
                postMessageService.updatePostMessage(postMessage);
                return "redirect:/post/" + postId;
            } else {
                throw new IllegalArgumentException("無權編輯此留言");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login"; // 返回錯誤頁面
        }
    }

    @PostMapping("/{postId}/message/{messageId}/delete") // 刪除留言
    public String deletePostMessage(@PathVariable Integer postId, 
                                    @PathVariable Integer messageId, 
                                    HttpSession session) {
        try {
            Integer memId = getCurrentMemberId(session); // 從會話中取得會員 ID
            if (memId == null) {
                throw new IllegalArgumentException("memId is required");
            }

            // 從資料庫查詢留言
            PostMessageVO postMessage = postMessageService.getPostMessageById(messageId)
                                                         .orElseThrow(() -> new IllegalArgumentException("留言未找到"));

            if (postMessage != null && postMessage.getMemId().equals(memId)) {
                postMessageService.deletePostMessage(messageId); // 刪除留言
                return "redirect:/post/" + postId;
            } else {
                throw new IllegalArgumentException("無權刪除此留言");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login"; // 返回錯誤頁面
        }
    }

    private Integer getCurrentMemberId(HttpSession session) {
        Object memId = session.getAttribute("mem_Id");
        if (memId != null) {
            return (Integer) memId; // 返回當前用戶 ID
        }
        return null; // 如果沒有找到 ID，返回 null
    }

    // 假設有方法獲取當前咖啡廳編號
    private int getCurrentCafeId() {
        return 1; // 假設返回 1
    }

}