package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
//后台列表展示
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map=new HashMap<String,Object>();
        //实例化分页单位
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //开始分页
        PageInfo<SaleChance> plist=new PageInfo<>(selectByParams(saleChanceQuery));

        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        return map;
    }
   //后台添加数据
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){

        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //设置state（）0未分配 1 已分配
        //未分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }
        if(!StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //devresult默认值设定 0未开发 1开发中 2开发成功 3开发失败
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);

        //creatdate，updatadate,分配时间
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败");
    }
    //验证
    //客户名非空
    //联系人非空
    //联系电话非空，11位手机号
    private void checkSaleChanceParam(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法手机号");

    }
   //修改后台实现
   @Transactional(propagation = Propagation.REQUIRED)
    public void changeSaleChance(SaleChance saleChance){
        //验证
       SaleChance temp = selectByPrimaryKey(saleChance.getId());
       AssertUtil.isTrue(temp==null,"记录不存在");
       checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
       //未分配
       if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
          saleChance.setState(1);
          saleChance.setDevResult(1);
          saleChance.setAssignTime(new Date());
       }
       //已分配
       if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
           saleChance.setState(0);
           saleChance.setDevResult(0);
           saleChance.setAssignTime(null);
           saleChance.setAssignMan("");
       }
       //设置默认值
       saleChance.setUpdateDate(new Date());

       //修改是否成功
       AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败");
    }
    //批量删除后台
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSeleChanceIds(Integer [] ids){
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"批量删除失败");
    }
}
