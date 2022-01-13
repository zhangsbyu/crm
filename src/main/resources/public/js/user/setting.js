layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交按钮
    form.on('submit(saveBtn)',function(data){
     var fiedata=data.field;

        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",
            data:{
               userName:fiedata.userName,
                phone:fiedata.phone,
                email:fiedata.email,
                trueName:fiedata.trueName,
                id:fiedata.id
            },
            dataType:"json",
            success:function (result){
            if(result.code==200){
               layer.msg("修改成功了",function(){
                   $.removeCookie("id",{domain:"localhost",path:"/crm"});
                   $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                   $.removeCookie("userPwd",{domain:"localhost",path:"/crm"});
                   window.parent.location.href=ctx+"/index";
               });
            }else{
                layer.msg(result.msg);
            }
            }

        });

        return false;
    });

});