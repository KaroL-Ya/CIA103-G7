package com.news.controller;

import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cafe.model.CafeVO;
import com.news.model.NewsService;
import com.news.model.NewsVO;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsSvc;
    
    
    @GetMapping("/news")
    public String showPage(ModelMap model) {
    	// 假設這裡是查詢新聞列表的邏輯
    	List<NewsVO> newsList = newsSvc.getAll();
    	model.addAttribute("newsList", newsList);
    	return "back-end/news/listAllNews";
    }
    
    @GetMapping("/back-end/news/selectNews")
    public String selectNews(ModelMap model) {
    	List<NewsVO> newsList = newsSvc.getAll();
        model.addAttribute("newsListData", newsList);
        return "back-end/news/selectNews";
    }
    

 // 顯示新增新聞頁面
    @GetMapping("/news/addNews")
    public String addNews(ModelMap model) {
        model.addAttribute("newsVO", new NewsVO()); // 初始化表單物件
        return "back-end/news/addNews"; // 返回新增新聞的頁面
    }

    // 新增新聞
    @PostMapping("/news/insert")
    public String insert(@Valid NewsVO newsVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/news/addNews";
        }

        // 如果 status 為 null，給它一個預設值
        if (newsVO.getStatus() == null) {
            newsVO.setStatus((byte) 0); // 假設 0 表示預設狀態
        }

        newsSvc.addNews(newsVO);
        return "redirect:/news/listAllNews";
    }

    // 顯示所有新聞
    @GetMapping("/news/listAllNews")
    public String listAllNews(ModelMap model) {
        List<NewsVO> newsList = newsSvc.getAll();
        model.addAttribute("newsListData", newsList);
        return "back-end/news/listAllNews";
    }
    
    // 提供所有新聞資料以供使用
    @ModelAttribute("/news/NewsListData")
    public List<NewsVO> referenceNewsListData(Model model) {
    	List<NewsVO> list = newsSvc.getAll();
    	model.addAttribute("newsListData", list);
    	return list;
    }

    // 查詢單筆新聞資料以供更新
    @PostMapping("/news/getOne_For_Update")
    public String getOneForUpdate(@RequestParam("newsId") Integer newsId, ModelMap model) {
        NewsVO newsVO = newsSvc.getOneNews(newsId); // 呼叫 Service 查詢單筆資料
        model.addAttribute("newsVO", newsVO);
        return "back-end/news/updateNews"; // 返回更新頁面
    }
    
    

    // 更新新聞資料
    @PostMapping("/news/update")
    public String update(@Valid NewsVO newsVO, BindingResult result, ModelMap model) {

        // 若驗證失敗，返回更新頁面
        if (result.hasErrors()) {
            return "back-end/news/updateNews";
        }

        // 更新資料
        newsSvc.updateNews(newsVO);
        return "redirect:/news/listAllNews"; // 更新後重導至列表頁
    }

    // 刪除新聞
    @PostMapping("/news/delete")
    public String delete(@RequestParam("newsId") Integer newsId) {
        newsSvc.deleteNews(newsId); // 呼叫 Service 刪除新聞
        return "redirect:/news/listAllNews"; // 刪除後重導至列表頁
    }
}