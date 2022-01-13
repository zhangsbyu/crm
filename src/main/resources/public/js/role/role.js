layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
//角色列表展示
    var tableIns = table.render({

        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });
// 多条件搜索
    $(".search_btn").click(function (){
        tableIns.reload({
            where:{
                roleName:$("input[name='roleName']").val()
            },
            page:{
                curr: 1
            }
        });
    });

    table.on('toolbar(roles)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                // layer.msg('添加');
                //openAddOrUpdateUserPage();
                break;
            case 'grant':
                 addRoleGrant(checkStatus.data);
                break;

        };
    });
    function addRoleGrant(ids){
        if(ids.length==0){
            layer.msg("请选择角色",{icon:5});
        }
        if(ids.length>1){
            layer.msg("选择角色大于一");
        }
        var title="<h2>角色授权</h2>";
        var url=ctx+"/role/toRoleGrant?id="+ids[0].id;
        layer.open({
            title:title,
            type:2,
            maxmin:true,
            area:["600px","280px"],
            content:url,
        });
    }
});