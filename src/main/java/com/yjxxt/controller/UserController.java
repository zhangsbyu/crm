package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.exceptions.ParamsException;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private ResultInfo resultInfo;
//后端验证登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo index(User user){
//        try{
            UserModel userModel = userService.userLogin(user.getUserName(), user.getUserPwd());
            resultInfo.setCode(200);
            resultInfo.setMsg("登录成功");
            resultInfo.setResult(userModel);
//        }catch (ParamsException pe){
//            pe.printStackTrace();
//            resultInfo.setCode(pe.getCode());
//            resultInfo.setMsg(pe.getMsg());
//        }
//        catch (Exception ex){
//               ex.printStackTrace();
//               resultInfo.setCode(500);
//               resultInfo.setMsg("操作失败");
//        }

        return resultInfo;
    }

//后端修改密码
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPwd){
       //从cookie中获取userId
        int id = LoginUserUtil.releaseUserIdFromCookie(req);

//        try {
            //x修改密码操作
           userService.changUserPwd(id,oldPassword,newPassword,confirmPwd);
//        } catch (ParamsException e) { // 自定义异常
//            e.printStackTrace();
//      // 设置状态码和提示信息
//            resultInfo.setCode(e.getCode());
//            resultInfo.setMsg(e.getMsg());
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultInfo.setCode(500);
//            resultInfo.setMsg("操作失败！");
//        }

        return resultInfo;
    }
//前段修改密码
    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }
    //前段展示信息
    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        int id = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(id);
        req.setAttribute("user",user);

        return "user/setting";
    }
   //后端修改信息
   @RequestMapping("setting")
   @ResponseBody
    public ResultInfo set(User user){
        userService.updateByPrimaryKeySelective(user);
        return resultInfo;
   }

   //查所有销售人员
   @RequestMapping("sales")
   @ResponseBody
    public List<Map<String, Object>> findSales(){
       List<Map<String, Object>> list = userService.querySales();
       return list;
   }
   //后端展示用户列表
   @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return userService.findUserByParams(userQuery);
    }
    //前段展示用户列表
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }
  //后端用户添加
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        userService.addUser(user);
        return success("添加成功");
    }
    //后端用户修改
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user){
        userService.changeUser(user);
        return success("修改成功");
    }
    //添加修改的弹出层位置
    @RequestMapping("addOrUpdatePage")
    public String  addOrUpdatePage(Integer id, Model model){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            System.out.println(user);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }
    //批量删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[]ids){
        System.out.println(ids);
     userService.removeUserIds(ids);
     return success("批量删除ok");
    }

}

