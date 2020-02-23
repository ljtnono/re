layui.use(["laydate", "table", "util", "form"], function () {
    let form = layui.form;
    let table = layui.table;
    let util = layui.util;
    table.render({
        elem: '.layui-table',
        id: "blogTable",
        height: 450,
        url: "/blog/listBlogPage",
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
                {field: 'title', title: '标题', width: 230},
                {field: 'author', title: '作者', width: 80, sort: true},
                {field: 'type', title: '分类', width: 80},
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
                {field: 'comment', title: '评论数', width: 80},
                {field: 'view', title: '浏览量', width: 90, sort: true},
                {title: "操作", fixed: 'right', width: 180, align: 'left', toolbar: '#toolbar'}
            ]
        ]
    });
    // 获取下拉选择框的值
    $.ajax({
        url: "/blog_type",
        method: "GET",
        dataType: "json",
        success: function (res) {
            // 请求成功，将option拼接到下拉框中去
            if (res.request === "success" && res.status === 200) {
                $.each(res.data, function (index, item) {
                    $('#type').append(new Option(item.name, item.name));
                });
                form.render();
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
    // 监听表单提交事件
    form.on('submit(search)', function (data) {
        let searchDTO = data.field;
        table.reload("blogTable", {
            url: "/blog/search",
            where: {
                title: searchDTO.title,
                type: searchDTO.type,
                author: searchDTO.author
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
    table.on('tool(blog)', function (obj) {
        let layEvent = obj.event;
        let data = obj.data;
        if (layEvent === "toBlog") {
            // 弹出博客显示的窗口
            window.parent.location.href = "/article/" + data.id;
        } else if (layEvent === "edit") {
            // 打开详情窗口进行编辑
            window.parent.xadmin.add_tab(data.title, "/admin/blog-detail?blogId=" + data.id + "&token=" + localStorage.getItem("token"));
        } else if (layEvent === "delete") {
            layer.confirm('确认删除此博客？', {
                btn: ['确认', '取消']
            }, function (index) {
                // 发送ajax请求删除
                $.ajax({
                    url: "/blog/" + data.id,
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
    });
});