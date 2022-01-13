package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;
    @RequestMapping("index")
    public String main3(){
        return "index";
    }
    @RequestMapping("main")
    public String main1(HttpServletRequest req){
        //获取当前用户的信息
        int id = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(id);
        //存储
        req.setAttribute("user",user);
        return "main";
    }
    @RequestMapping("welcome")
    public String main2(){

        return "welcome";
    }
}
