package com.yjxxt.config;

import com.yjxxt.interceptors.Nologininterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer{
    @Bean
    public Nologininterceptor nologininterceptor(){
      return new Nologininterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
     //拦截路径
        registry.addInterceptor(nologininterceptor())//配置拦截器
                .addPathPatterns("/**")//添加拦截路径
                .excludePathPatterns("/index","/user/login","/js/**","/css/**","/images/**","/lib/**");//添加放行路径
    }
}

