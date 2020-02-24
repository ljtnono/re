console.log("正在编辑博客类型，userId = " + userId);
layui.use(['form', 'layer', 'util'], function () {
    let form = layui.form;
    let layer = layui.layer;
    let user;
    if (userId) {
        // 获取timeline相关信息
        $.ajax({
            url: "/user/" + userId,
            method: "GET",
            dataType: "json",
            success: function (res) {
                if (res.request === "success" && res.status === 200 && res.data) {
                    user = res.data[0];
                    form.val("form-user", {
                        "id": user.id,
                        "username": user.username,
                        "password": user.password,
                        "qq": user.qq,
                        "tel": user.tel,
                        "email": user.email
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
        form.on('submit(form-user-submit)', function (data) {
            let user = data.field;
            layer.confirm('确认提交？', {
                btn: ['确认', '取消']
            }, function (index) {
                $.ajax({
                    url: "/user/" + userId,
                    data: {
                        "id": user.id,
                        "username": user.username,
                        "password": user.password,
                        "qq": user.qq,
                        "tel": user.tel,
                        "email": user.email
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
        // 设置表单校验
        form.verify({
            username: function (value, item) {
                let usernameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
                if (!usernameRex.test(value)) {
                    return "用户名只能为2-20个字符";
                }
            },
            password: function (value, item) {
                let passwordRex = /^[\w\W\s\u4e00-\u9fcc]{6,32}$/;
                if (!passwordRex.test(value)) {
                    return "密码只能为6-32个字符";
                }
            }
        });
    } else {
        layer.msg("参数错误", {icon: 2});
    }
});