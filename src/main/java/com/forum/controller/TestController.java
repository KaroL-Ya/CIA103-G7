//package com.cia07.controller;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.cia07.model.PostVO;
//import com.cia07.service.PostService;
//
//@Controller
//public class PostController {
//   @Autowired
//   private PostService postService;
//   
//   @GetMapping("/") //查詢
//   public String enter(String id, String password,  Model model) {
//
//	   model.addAttribute("id", id);
//	   model.addAttribute("password", password);
//	   	   
//      return "forward:/WEB-INF/views/enter.jsp"; // 返回詳細頁面名稱
//  }
//
//   @PostMapping("/post")
//    public String listPosts(Integer id, Model model) {
//	   
//	   PostVO p = postService.getPostById(id);
//	   if(p == null){
//		   System.out.println("null");
//	   }
//	   model.addAttribute("post", p);
//	   
////       List<PostVO> posts = postService.getAllPosts(); // 獲取所有貼文
////       model.addAttribute("post", posts); // 將貼文添加到模型
//       return "forward:/WEB-INF/views/postList.jsp"; // 返回 JSP 頁面名稱
//   }
//
//
//    @PostMapping
//    public String createPost(@ModelAttribute PostVO post) {
//        postService.createPost(post);
//        return "redirect:/"; // 重定向到貼文列表
//   }
//    
//    
//    @GetMapping("testjsp")
//    public String test() {
//    	
//    	return "forward:/WEB-INF/views/test.jsp";
//    }
//    
//    @GetMapping("testthy")
//    public String test2() {
//    	
//    	
//    	return "testthy";
//    }
//}