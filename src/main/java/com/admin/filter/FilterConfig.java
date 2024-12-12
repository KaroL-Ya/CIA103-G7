package com.admin.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.admin.filter.AdminLoginFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AdminLoginFilter> adminFilter() {
        FilterRegistrationBean<AdminLoginFilter> login = new FilterRegistrationBean<>();
        login.setFilter(new AdminLoginFilter());  // 註冊自定義的 Filter
        login.setName("AdminFilter");
        login.addUrlPatterns("/back-end/admin/*"); 
        login.setOrder(2);
        return login;
    }
    
}