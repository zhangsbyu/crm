package com.yjxxt;

import com.alibaba.fastjson.JSON;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        if(ex instanceof NoLoginException){
            ModelAndView mav=new ModelAndView("redirect:/index");
            return mav;
        }

        //实例化对象
        ModelAndView mav=new ModelAndView("error");
        mav.addObject("code",300);
        mav.addObject("msg","参数异常");
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod= (HandlerMethod) handler;
            ResponseBody responseBody= handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if(responseBody==null){
                //返回视图
                if(ex instanceof ParamsException){
                    ParamsException pe=(ParamsException)ex;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }
                return mav;
            }else{
                //返回json resultinfo
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("参数异常");
                if(ex instanceof ParamsException){
                    ParamsException pe=(ParamsException)ex;
                   resultInfo.setCode(pe.getCode());
                   resultInfo.setMsg(pe.getMsg());
                }
             //响应resltinfo
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter pw= null;
                try {
                    pw = resp.getWriter();
                    //resulinfo变为json
                    pw.write(JSON.toJSONString(resultInfo));
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(pw!=null){
                        pw.close();
                    }
                }

            }
        return null;

        }
        return mav;
    }
}
