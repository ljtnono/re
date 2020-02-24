layui.use(["form", "layer"], function () {
    let form = layui.form;
    let layer = layui.layer;
    // 设置表单校验
    form.verify({
        name: function (value, item) {
            let nameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
            if (!nameRex.test(value)) {
                return "链接类型名只能为2-20个字符";
            }
        }
    });
    // 监听提交事件
    form.on('submit(form-linkType-submit)', function (data) {
        let link = data.field;
        layer.confirm('确认提交？', {
            btn: ['确认', '取消']
        }, function (index) {
            $.ajax({
                url: "/link_type",
                data: {
                    name: link.name
                },
                method: "POST",
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
});