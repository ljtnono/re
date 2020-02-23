layui.use(["form", "layer"], function () {
    let form = layui.form;
    let layer = layui.layer;
    // 获取下拉选择框的值
    $.ajax({
        url: "/link_type",
        method: "GET",
        dataType: "json",
        success: function (res) {
            // 请求成功，将option拼接到下拉框中去
            if (res.request === "success" && res.status === 200) {
                $.each(res.data, function (index, item) {
                    $('#type').append(new Option(item.name, item.name));
                });
                form.render();
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
    // 设置表单校验
    form.verify({
        name: function (value, item) {
            let nameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
            if (!nameRex.test(value)) {
                return "链接名只能为2-20个字符";
            }
        }
    });
    // 监听提交事件
    form.on('submit(form-link-submit)', function (data) {
        let link = data.field;
        layer.confirm('确认提交？', {
            btn: ['确认', '取消']
        }, function (index) {
            $.ajax({
                url: "/link",
                data: {
                    url: link.url,
                    name: link.name,
                    type: link.type
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