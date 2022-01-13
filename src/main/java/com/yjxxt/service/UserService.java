package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.mapper.UserRoleMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;
    public UserModel userLogin(String userName,String userPwd){
//        //用户非空
//        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
//        //密码非空
//        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
        //将上面两个判断抽离出来判断用户信息非空
        checkUserLoginParam(userName,userPwd);
        //用户是否存在
        User temp=userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp==null,"用户不存在");
        //用户密码是否正确
        checkUserPwd(userPwd,temp.getUserPwd());
        //构建返回对象
        return builderUserIndo(temp);
    }
    //创建返回目标对象
    private UserModel builderUserIndo(User temp) {
        UserModel userModel=new UserModel();
        userModel.setUserName(temp.getUserName());
        userModel.setId(UserIDBase64.encoderUserID(temp.getId()));
        userModel.setTrueName(temp.getTrueName());
        return userModel;
    }
    //判断用户信息非空
    private void checkUserLoginParam(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }
    //用户密码是否正确
    private void checkUserPwd(String userPwd, String userPwd1) {
        //将输入密码转为加密模式
        userPwd= Md5Util.encode(userPwd);
        //与数据局密码进行比对
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");
    }
   //更改用户密码
    public void changUserPwd(Integer id,String oldPassword,String newPassword,String confirmPwd){
    //通过id获取对象
        User user = userMapper.selectByPrimaryKey(id);
        //调用验证旧密码与新密码的方法
        checkPasswordParams( user,oldPassword,newPassword, confirmPwd);
        //  设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //  执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"操作失败");


    }

    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPwd) {
        //user对象非空验证
        AssertUtil.isTrue(user==null,"用户不存在");
        //原始密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原密码不存在");
        //原始密码要与数据库中的密文密码保持一致
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原密码不正确");
        //新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不存在");
        //新密码不能和原密码一致
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码不能与原密码一致");
        // 确认密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd), "请输入确认密码！");
// 新密码要与确认密码保持一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPwd)), "新密码与确认密码不致！");
    }

//查询所有销售人员
    public List<Map<String,Object>> querySales(){
        return  userMapper.selectSales();
    }

   //查询所有用户信息
    public Map<String,Object> findUserByParams(UserQuery userQuery){
        Map<String,Object> map=new HashMap<String,Object>();
        //初始化分页信息
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //开始分页
        PageInfo<User> pageInfo=new PageInfo<User>(userMapper.selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    //添加用户信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //用户名非空且唯一
        //邮箱非空
        //手机号非空格式正确
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
       //添加默认值 is_valid=1 creatdate updatedate均为系统时间
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //判断添加是否成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");
        //添加角色信息
        relaionUserRole(user.getId(),user.getRoleIds());
    }
   //
    private void relaionUserRole(Integer id, String roleIds) {
        List<UserRole> ulist=new ArrayList<UserRole>();
        AssertUtil.isTrue(StringUtils.isBlank(roleIds),"请添加角色");
        //统计有多少个角色
        int count=userRoleMapper.countUserRoleNum(id);
        //删除当前角色
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleById(id)!=count,"删除失败");
        }
        String[] roleids=roleIds.split(",");
        for (String x: roleids) {
            UserRole userRole=new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(Integer.parseInt(x));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            ulist.add(userRole);
        }
       AssertUtil.isTrue(userRoleMapper.insertBatch(ulist)!=ulist.size(),"用户角色添加失败");
    }

    private void checkUser(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号不合法");
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp!=null,"用户名已存在");
    }
    //修改用户信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUser(User user){
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null,"待修改记录不存在");
//        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        user.setUpdateDate(new Date());
        relaionUserRole(user.getId(),user.getRoleIds());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    }
    //批量删除
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeUserIds(Integer[] ids){
        for (Integer userId: ids) {
            int count=userRoleMapper.countUserRoleNum(userId);
            //删除当前角色
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleById(userId)!=count,"删除失败");
            }

        }
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择删除数据");
       AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"删除失败");

    }



}
