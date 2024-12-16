package com.error.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController{

	@RequestMapping("/error")
	 public String handleError(HttpServletRequest request) {
        // 獲取錯誤狀態碼
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // 根據不同的錯誤狀態碼返回對應的錯誤頁面
        if (statusCode == 404) {
            return "error/404";  // 404錯誤頁面
        } else if (statusCode == 500) {
            return "error/500";  // 500錯誤頁面
        }

        // 如果是其他錯誤，可以返回通用錯誤頁面
        return "error/default";  // 默認錯誤頁面
    }

	
	
	

    public String getErrorPath() {
        return "/error";
    }
}