package com.aidiary.common.WebMvcConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.aidiary.common.Interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration 
@RequiredArgsConstructor 
public class WebMvcConfig implements WebMvcConfigurer{
    private final LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(loginInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/"
            ,"/user/login"
            , "/user/logout"
            , "/user/valMembership"
            , "/signup/goSignUp"
             ,"/css/**"
             ,"/javascript/**"
             ,"/images/**"
             , "/error"
        );

    }
}
