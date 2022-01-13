layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url : ctx+'/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    $(".search_btn").click(function (){
        tableIns.reload({
            where:{
                userName:$("input[name=userName]").val(),
                email:$("input[name=email]").val(),
                phone:$("input[name=phone]").val()
            },
            page:{
                curr: 1
            }
        });
    });


//头部工具栏绑定
    table.on('toolbar(users)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                // layer.msg('添加');
                openAddOrUpdateUserPage();
                break;
            case 'del':
                // layer.msg('删除');
                deleteUser(checkStatus.data);
                break;

        };
    });
    //行内工具栏的绑定
    table.on('tool(users)', function(obj){
        var data = obj.data;
        if(obj.event === 'del'){
            layer.confirm('真的删除行么',{btn:["确定","取消"]}, function(index){
                layer.close(index);
                $.ajax({
                    type:"post",
                    url:ctx+"/user/delete",
                    data:{"ids":data.id},
                    dataType: "json",
                    success: function (result){
                        if(result.code==200){
                            layer.msg("删除成功");
                            tableIns.reload();
                        }else{
                            layer.msg(result.msg);
                        }
                    }
                });

            });
        } else if(obj.event === 'edit'){
             //layer.msg("修改");
            openAddOrUpdateUserPage(data.id);
        }
    });


    //弹框函数
    function  openAddOrUpdateUserPage(id){
        var title="<h2>用户模块的添加</h2>";
        var url=ctx+"/user/addOrUpdatePage";
        //判断是修改还是添加

        if(id){
            title="<h2>用户模块的更新</h2>";
            console.log(id+"--------");
            url=url+"?id="+id;
        }
        layer.open({
            title:title,
            content:url,
            area:["650px","400px"],
            maxmin:true,
            type: 2
        });
    }

    //批量删除
    function deleteUser(datas){
        if(datas.length==0){
            layer.msg("请选择要删除的数据");
            return;
        }
        layer.confirm("你确定要删除数据吗",{
            btn:["确认","取消"]
        },function (index){
            layer.close(index);
            var ids=[];
            for(var x in datas){
                ids.push(datas[x].id);
            }
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:{"ids":ids.toString()},
                dataType:"json",
                success:function (result){
                    if(result.code==200){
                        layer.msg("删除成功");
                        tableIns.reload();
                    }else{
                        layer.msg(result.msg);
                    }
                }
            });
        });

    }

});


