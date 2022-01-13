layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交按钮
    form.on('submit(login)', function(data){// 这里的formDemo是lay-filter的值。
        // layer.msg(JSON.stringify(data.field));//json格式化
        // data.field.username;
        // data.field.password;
        // layer.msg(data.field.username);
         var fieloData=data.field;
         if(fieloData.username=='undefined' || fieloData.username.trim()==''){
              layer.msg("用户名不能为空");
              return ;
         }
        if(fieloData.password=='undefined' || fieloData.password.trim()==''){
            layer.msg("密码不能为空");
            return ;
        }
        //发送ajax

        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                "userName":fieloData.username,
                "userPwd":fieloData.password
            },
            dataType:"json",
            success:function (result){
                //resultInfo
                if(result.code==200){
                    //成功了
                    layer.msg("登录成功了",{icon:5});
                    //跳转
                    // window.location.href=ctx + "/main";
                    layer.msg("登录成功了",function (){
                        //将用户存储到cookie
                        $.cookie("id",result.result.id);
                        $.cookie("userName",result.result.userName);
                        $.cookie("trueName",result.result.trueName);
                      //判断是否选择记住我

                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("id",result.result.id,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }
                        window.location.href=ctx + "/main";
                    });
                }else{
                    //失败了
                    layer.msg(result.msg);
                }
            }
        });
        return false;
    });

});