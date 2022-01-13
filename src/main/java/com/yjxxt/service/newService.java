package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class newService extends BaseService<User,Integer> {
    @Autowired(required = false)
    UserMapper userMapper;
    public UserModel userlong(String userName,String passWord){
        checkNameandpass(userName, passWord);
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp==null,"");
        checkpassWord(passWord,temp.getUserPwd());
        return back(temp);
    }
    public void checkNameandpass(String userName,String passWord){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"");

    }
    public void checkpassWord(String passWord,String userpass){
        String s = Md5Util.encode(userpass);
        AssertUtil.isTrue(!s.equals(passWord),"");
    }
    public UserModel back(User user){
        UserModel userModel=new UserModel();
        String s = UserIDBase64.encoderUserID(user.getId());
        userModel.setTrueName(user.getTrueName());
        userModel.setId(s);
        return userModel;
    }
   public Map<String,Object> finduser(UserQuery userQuery){
      Map<String,Object> map=new HashMap<String,Object>();
      PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
      PageInfo<User> pageInfo=new PageInfo<User>(userMapper.selectByParams(userQuery));
      map.put("code",0);
      map.put("msg","success");
      map.put("count",pageInfo.getTotal());
      map.put("data",pageInfo.getList());
      return map;

   }
}
