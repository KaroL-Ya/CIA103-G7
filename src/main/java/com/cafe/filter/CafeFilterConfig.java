
package com.cafe.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CafeFilterConfig {

    @Bean
    public FilterRegistrationBean<CafeLoginFilter> cafeFilter() {
        FilterRegistrationBean<CafeLoginFilter> login = new FilterRegistrationBean<>();
        login.setFilter(new CafeLoginFilter());  // 註冊自定義的 Filter
        login.setName("CafeFilter");
        login.addUrlPatterns("/front-end/cafe/*"); 
        login.setOrder(3);
        return login;
    }
    
}
