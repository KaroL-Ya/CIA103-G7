package com.admin.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 註冊 Filter，適用於所有請求

public class AdminLoginFilter implements Filter {

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
        if (uri.endsWith("/back-end")) {
            chain.doFilter(request, response);  // 直接放行
            return;
        }

        // 取得 session 並檢查是否已登入
        HttpSession session = httpRequest.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            // 如果沒有登入，重定向到登入頁面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/back-end");
            return;
        } 
//        else {
//            chain.doFilter(request, response);  // 放行請求，繼續處理
//        }
     // 檢查該管理員是否有相應的功能權限
        Integer requiredFuncId = getRequiredFuncId(uri); // 根據 URI 獲取所需的功能 ID
        if (requiredFuncId != null) {
        	// 檢查 session 是否包含該功能 ID (即 pm{funcId})
            String funcSessionAttribute = "pm" + requiredFuncId;
            if (session.getAttribute(funcSessionAttribute) == null) {
                // 如果沒有權限，重定向到未授權頁面
//                httpResponse.sendRedirect(httpRequest.getContextPath() + "/back-end/admin/backend_index");
            	 httpResponse.setContentType("text/html;charset=UTF-8");
                 httpResponse.getWriter().write("<script>alert('您沒有權限訪問該功能！'); history.back();</script>");
                return ;
            }
        }
        
        // 放行請求，繼續處理
        chain.doFilter(request, response);
        
        
    }

    @Override
    public void destroy() {
        // 清理資源
    }
    
 // 根據 URI 獲取所需的功能 ID（這裡可以根據實際需求進行修改）
    private Integer getRequiredFuncId(String uri) {
        // 使用正則表達式匹配 URL，對應功能 ID
        
        // 管理員管理頁面
        if (uri.matches(".*/back-end/admin/.*")) {
            return 1; // 管理員管理
        } 
        // 會員管理頁面
        else if (uri.matches(".*/back-end/member/.*")) {
            return 2; // 會員管理
        }
        // 商家管理頁面
        else if (uri.matches(".*/back-end/cafe/.*")) {
            return 3; // 商家管理
        }
        // 商城管理頁面
        else if (uri.matches(".*/back-end/shop/.*")) {
            return 4; // 商城管理
        }
        // 活動管理頁面
        else if (uri.matches(".*/back-end/event/.*")) {
            return 5; // 活動管理
        }
        // 論壇管理頁面
        else if (uri.matches(".*/back-end/news/.*")) {
            return 6; // 論壇管理
        }
        
        // 若沒有對應功能 ID，則返回 null
        return null;
    }
}