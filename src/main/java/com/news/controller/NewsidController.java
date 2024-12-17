package com.news.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.news.model.NewsVO;
import com.cafe.model.CafeVO;
import com.news.model.NewsService;

@Controller
@Validated
@RequestMapping("/news")
public class NewsidController {

    @Autowired
    NewsService newsService;

    /*
     * This method will be called on selectNews.html form submission, handling POST
     * request. It also validates the user input.
     */
    @PostMapping("/getOne_For_Display")
	public String getOneForDisplay(@RequestParam("newsId") Integer newsId, Model model) {
		NewsVO newsVO = newsService.getOneNews(newsId);
		if (newsVO == null) {
			model.addAttribute("errorMessage", "找不到對應的商家資料！");
			return "back-end/news/selectNews"; // 返回選擇頁面
		}
		model.addAttribute("newsVO", newsVO);
		return "/back-end/news/listOneNews"; // 返回顯示商家詳情的頁面
	}

    
    @ExceptionHandler(value = { ConstraintViolationException.class })
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "<br>");
        }

        //==== 以下第80~85行是當前面第69行返回 /src/main/resources/templates/back-end/news/select_page.html 第97行 與 第109行 用的 ====
        List<NewsVO> list = newsService.getAll();
        model.addAttribute("newsListData", list); // for select_page.html 第97 109行用
        
        String message = strBuilder.toString();
        return new ModelAndView("back-end/news/selectNews", "errorMessage", "請修正以下錯誤:<br>" + message);
    }
}
