layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 监听submit事件
     * 实现营销机会的添加与更新
     */
    form.on("submit(addOrUpdateSaleChance)",function(obj){
        var url=ctx+"/sale_chance/save";
       if($("input[name=id]").val()){
           url=ctx+"/sale_chance/update"
       }
        $.ajax({
           type:"post",
           url:url,
           data:obj.field,
           dataType:"json",
           success:function (obj){
               if(obj.code==200){
                   layer.msg("添加成功",{icon:6});
                   window.parent.location.reload();
               }else{
                   layer.msg(obj.msg);
               }
           }
        });
       return false;
    });


    //取消功能
    $("#closeBtn").click(function (){
        //获取弹出层索引
        var index = parent.layer.getFrameIndex(window.name);
        //根据索引关闭
        parent.layer.close(index);
    });


   //添加下拉框
    var assignMan=$("input[name='man']").val();
    $.ajax({
       type: "post",
       url:ctx+"/user/sales",
        dataType: "json",
        success:function (data){
         for(var x in data){
             if(data[x].id==assignMan){
                 $("#assignMan").append("<option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
             }else{
                 $("#assignMan").append("<option value='"+data[x].id+"'>"+data[x].uname+"</option>");
             }

         }
         layui.form.render("select");
        }

    });
});