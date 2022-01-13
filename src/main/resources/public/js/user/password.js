layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(saveBtn)', function(data){
        var filedate=data.field;
        //发ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/updatePwd",
            data:{
                oldPassword:filedate.old_password,
                newPassword:filedate.new_password,
                confirmPwd:filedate.again_password
            },
            dataType:"json",
            success:function (result){
             if(result.code==200){
              layer.msg("修改成功了，系统3秒后退出",function (){
                //清空cookie信息
                  $.removeCookie("id",{domain:"localhost",path:"/crm"});
                  $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                  $.removeCookie("userPwd",{domain:"localhost",path:"/crm"});
               // 跳转页面
                  window.location.href=ctx+"/index";
              });
             }else{
                 layer.msg(result.msg);
             }
            }
        });
        return false;
    });
});