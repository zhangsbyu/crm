package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.mapper.RoleMapper;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired(required = false)
    private RoleService roleService;
    @RequestMapping("findRole")
    @ResponseBody
    public List<Map<String,Object>> findRole(Integer userId){
        return roleService.findRole(userId);
    }
    @RequestMapping("index")
    public String index(){
        return "/role/role";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(RoleQuery roleQuery){

        System.out.println(roleService.findRoleByParam(roleQuery));
        return roleService.findRoleByParam(roleQuery);

    }
    @RequestMapping("toRoleGrant")
    public String Grant(Integer roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

}
