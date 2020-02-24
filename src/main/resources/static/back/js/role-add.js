layui.use(["form", "layer"], function () {
    let form = layui.form;
    let layer = layui.layer;
    // 设置表单校验
    form.verify({
        name: function (value, item) {
            let nameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
            if (!nameRex.test(value)) {
                return "角色名只能为2-20个字符";
            }
        },
        description: function (value, item) {
            let descriptionRex = /^[\w\W\s\u4e00-\u9fcc]{0,255}$/;
            if (!descriptionRex.test(value)) {
                return "角色描述不能超过255个字符";
            }
        }
    });
    // 监听提交事件
    form.on('submit(form-role-submit)', function (data) {
        let role = data.field;
        layer.confirm('确认提交？', {
            btn: ['确认', '取消']
        }, function (index) {
            $.ajax({
                url: "/role",
                data: {
                    "name": role.name,
                    "description": role.description
                },
                method: "POST",
                dateType: "json",
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
        return false;
    });
});