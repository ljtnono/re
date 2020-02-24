layui.use(["laydate", "table", "util", "form"], function () {
    let form = layui.form;
    let table = layui.table;
    let util = layui.util;
    table.render({
        elem: '.layui-table',
        height: 450,
        id: "userTable",
        url: "/user/listUserPage",
        page: true,
        method: "get",
        toolbar: true,
        defaultToolbar: ['filter', 'print', 'exports'],
        request: {
            pageName: "page",
            limitName: "count"
        },
        parseData: function (res) {
            return {
                "status": res.status,
                "count": res.fields.totalCount,
                "message": res.message,
                "data": res.data
            };
        },
        response: {
            statusName: "status",
            statusCode: 200,
            msgName: "message",
            countName: "count",
            dataName: "data"
        },
        cols: [
            [
                {type: 'checkbox', fixed: 'left'},
                {field: 'id', title: 'ID', width: 80, sort: true},
                {field: 'username', title: '用户名', width: 130},
                {field: 'password', title: '密码', width: 130},
                {field: 'qq', title: 'QQ', width: 130},
                {field: 'tel', title: '电话', width: 130},
                {field: 'email', title: '电子邮件', width: 130},
                {
                    field: 'createTime', title: '创建时间', width: 170,
                    templet: function (d) {
                        return util.toDateString(d.createTime, "yyyy-MM-dd HH:ss");
                    }
                },
                {
                    field: 'modifyTime', title: '最后修改时间', width: 170, sort: true,
                    templet: function (d) {
                        return util.toDateString(d.modifyTime, "yyyy-MM-dd HH:ss");
                    }
                },
                {
                    field: 'status', title: '状态', width: 80, sort: true,
                    templet: function (d) {
                        if (d.status === 1) {
                            return "正常";
                        } else if (d.status === 0) {
                            return "已删除";
                        }
                    }
                },
                {title: "操作", fixed: 'right', width: 180, align: 'left', toolbar: '#toolbar'}
            ]
        ]
    });
    // 监听表单提交事件
    form.on('submit(search)', function (data) {
        table.reload("userTable", {
            url: "/user/search",
            where: {
                username: data.field.username,
                tel: data.field.tel,
                email: data.field.email
            },
            method: "POST",
            parseData: function (res) {
                return {
                    "status": res.status,
                    "count": res.fields.totalCount,
                    "message": res.message,
                    "data": res.data
                };
            },
            response: {
                statusName: "status",
                statusCode: 200,
                msgName: "message",
                countName: "count",
                dataName: "data"
            }
        });
        return false;
    });
    // 监听操作栏点击事件
    table.on('tool(user)', function (obj) {
        let layEvent = obj.event;
        let data = obj.data;
        if ("edit" === layEvent) {
            window.parent.xadmin.add_tab(data.username, "/admin/user-detail?userId=" + data.id + "&token=" + localStorage.getItem("token"));
        } else if ("delete" === layEvent) {
            // 首先弹出询问窗口进行确认
            if (data.status === 0) {
                layer.msg("不能删除已经删除的项！", {icon: 2});
            } else {
                layer.confirm('确认删除？', {
                    btn: ['确认', '取消']
                }, function (index) {
                    $.ajax({
                        url: "/user/" + data.id,
                        method: "DELETE",
                        dataType: "json",
                        success: function (res) {
                            if (res.request === "success" && res.status === 200) {
                                layer.msg(res.message, {icon: 1});
                            } else {
                                layer.msg(res.message + ", 错误码: " + res.status, {icon: 2});
                            }
                        },
                        error: function (res) {
                            if (res) {
                                layer.msg(res);
                            } else {
                                layer.msg("请求错误，请检查网络", {icon: 2});
                            }
                        }
                    });
                    layer.close(index);
                });
            }
        } else if ("restore" === layEvent) {
            if (data.status === 1) {
                layer.msg("不能恢复正常的项！", {icon: 2});
            } else {
                // 恢复博客类型相关
                layer.confirm('确认恢复？', {
                    btn: ['确认', '取消']
                }, function (index) {
                    $.ajax({
                        url: "/user/restore/" + data.id,
                        method: "PUT",
                        dataTyoe: "json",
                        success: function (res) {
                            if (res.request === "success" && res.status === 200) {
                                layer.msg(res.message, {icon: 1});
                            } else {
                                layer.msg(res.message + ", 错误码: " + res.status, {icon: 2});
                            }
                        },
                        error: function (res) {
                            if (res) {
                                layer.msg(res);
                            } else {
                                layer.msg("请求错误，请检查网络", {icon: 2});
                            }
                        }
                    });
                    layer.close(index);
                });
            }
        }
    });
});