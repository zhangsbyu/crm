package com.yjxxt.interceptors;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器
public class Nologininterceptor extends HandlerInterceptorAdapter {
   @Autowired
   private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //未登录拦截，拦截方式抛异常
        Integer id  = LoginUserUtil.releaseUserIdFromCookie(request);

        if(id==null || userService.selectByPrimaryKey(id)==null){
            throw new NoLoginException("未登录");
        }
        return true;

    }
}
