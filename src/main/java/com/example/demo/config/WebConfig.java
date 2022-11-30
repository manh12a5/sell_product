package com.example.demo.config;

import com.example.demo.service.interceptor.HttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public HttpRequestInterceptor requestHandler() {
        return new HttpRequestInterceptor();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestHandler())
                .excludePathPatterns("/js/**", "/css/**", "/images/**", "/webjars/**", "/webfonts/**");
    }
}
