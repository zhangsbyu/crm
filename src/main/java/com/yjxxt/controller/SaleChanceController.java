package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired(required = false)
    private SaleChanceService saleChanceService;
    @Autowired
    private UserService userService;
//后端营销机会的查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = saleChanceService.querySaleChanceByParams(saleChanceQuery);
        return map;
    }
    //前段营销机会跳转
    @RequestMapping("index")
    public String index(){

     return "/saleChance/sale_chance";
    }
    @RequestMapping("addOurUpdateDialog")
    public String addOurUpdate(Integer id, Model model){
        if(id!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "/saleChance/add_update";
    }
    //后台添加数据
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        int id = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(id).getTrueName();
        saleChance.setCreateMan(trueName);
        saleChanceService.addSaleChance(saleChance);
       return success("添加成功了");
    }
    //后台修改数据
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
     saleChanceService.changeSaleChance(saleChance);
     return success("修改成功");
    }
    //后台批量删除
    @RequestMapping("dels")
    @ResponseBody
     public ResultInfo deletes(Integer [] ids){
        saleChanceService.removeSeleChanceIds(ids);
        return  success("批量删除成功");
    }
}
