console.log("正在编辑博客类型，blogTypeId = " + blogTypeId);
layui.use(['form', 'layer'], function () {
    let form = layui.form;
    let layer = layui.layer;
    let blogType;
    if (blogTypeId) {
        // 设置表单校验
        form.verify({
            name: function (value, item) {
                let nameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
                if (!nameRex.test(value)) {
                    return "博客类型名只能为2-20个字符";
                }
            },
            description: function (value, item) {
                // 验证只能4-30个字符 包括中文、字母、数字下划线
                let descriptionRex = /^[\w\W\s\u4e00-\u9fcc]{0,255}$/;
                if (!descriptionRex.test(value)) {
                    return "博客类型简介只能为0-255个字符";
                }
            }
        });
        // 获取blogType相关信息
        $.ajax({
            url: "/blog_type/" + blogTypeId,
            method: "GET",
            dataType: "json",
            success: function (res) {
                if (res.request === "success" && res.status === 200 && res.data) {
                    blogType = res.data[0];
                    form.val("form-blogType", {
                        "id": blogType.id,
                        "name": blogType.name,
                        "description": blogType.description
                    });
                    form.render();
                } else {
                    layer.msg(res.message + ", 错误码: " + res.status , {icon: 2});
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
        // 监听提交事件
        form.on('submit(form-blogType-submit)', function (data) {
            let blogType = data.field;
            layer.confirm('确认提交？', {
                btn: ['确认', '取消']
            }, function (index) {
                $.ajax({
                    url: "/blog_type/" + blogTypeId,
                    data: {
                        id: blogType.id,
                        name: blogType.name,
                        description: blogType.description
                    },
                    method: "PUT",
                    dateType: "json",
                    success: function (res) {
                        if (res.request === "success" && res.status === 200) {
                            layer.msg(res.message , {icon: 1});
                        } else {
                            layer.msg(res.message + ", 错误码: " + res.status , {icon: 2});
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
            return false;
        });
    } else {
        layer.msg("参数错误", {icon: 2});
    }
});

