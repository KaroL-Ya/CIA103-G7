 package com.member.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig2 {

    @Bean
    public FilterRegistrationBean<MemberLoginFilter> memberFilter() {
        FilterRegistrationBean<MemberLoginFilter> login = new FilterRegistrationBean<>();
        login.setFilter(new MemberLoginFilter());  // 註冊自定義的 Filter
        login.setName("MemberFilter");
        login.addUrlPatterns("/member/*"); 
        login.setOrder(1);
        return login;
    }
    
}
