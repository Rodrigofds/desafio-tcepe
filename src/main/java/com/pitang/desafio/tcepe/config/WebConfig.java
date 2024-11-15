package com.pitang.desafio.tcepe.config;

import com.pitang.desafio.tcepe.interceptor.JsonRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JsonRequestInterceptor jsonRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jsonRequestInterceptor)
                .addPathPatterns("/**");
    }
}

