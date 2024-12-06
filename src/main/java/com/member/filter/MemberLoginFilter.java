package com.member.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 註冊 Filter，適用於所有請求

public class MemberLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化時執行
    }

    @Override
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 檢查請求的 URI 是否需要檢查登入
        String uri = httpRequest.getRequestURI();
   
        // 設定不需要驗證登入的頁面，例如登入頁面和登出頁面
        if (uri.endsWith("/login") || uri.endsWith("/register") || uri.endsWith("/")) {
            chain.doFilter(request, response);  // 直接放行
            return;
        }

        // 取得 session 並檢查是否已登入
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            // 如果沒有登入，重定向到登入頁面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            chain.doFilter(request, response);  // 放行請求，繼續處理
        }
    }

    @Override
    public void destroy() {
        // 清理資源
    }
}
