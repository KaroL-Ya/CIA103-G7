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
    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
        /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//        @NotEmpty(message="新聞編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "新聞編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "新聞編號: 不能小於{value}")
        @Max(value = 9999, message = "新聞編號: 不能超過{value}")
        @RequestParam("newsId") Integer newsId,
        ModelMap model) {
        
        /***************************2.開始查詢資料********************************************/
        NewsVO newsVO = newsService.getOneNews(newsId);
        
        List<NewsVO> list = newsService.getAll();
        model.addAttribute("newsListData", list); // for select_page.html 第97 109行用
        
        if (newsVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/news/selectNews";
        }

        /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
        model.addAttribute("newsVO", newsVO);
        model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第126行 -->
        
        // return "back-end/news/listOneNews";  // 查詢完成後轉交listOneNews.html
        return "back-end/news/selectNews"; // 查詢完成後轉交selectNews.html由其第128行insert listOneNews.html內的th:fragment="listOneNews-div"
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
