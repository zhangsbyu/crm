package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.Role;
import com.yjxxt.mapper.RoleMapper;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired(required = false)
   private RoleMapper roleMapper;

    public List<Map<String,Object>> findRole(Integer userId){
        return roleMapper.selectRole(userId);
    }

//角色条件查询
    public Map<String,Object> findRoleByParam(RoleQuery roleQuery){
        Map<String,Object> map=new HashMap<String,Object>();

        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo=new PageInfo<Role>(roleMapper.selectByParams(roleQuery));

        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    //角色添加
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
        //AssertUtil.isTrue(roleMapper.selectRoleByName(role.getRoleName()),"角色已存在");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
    }
}
