layui.use(["form", "layer"], function () {
    let form = layui.form;
    let layer = layui.layer;
    // 设置表单校验
    form.verify({
        content: function (value, item) {
            let contentRex = /^[\w\W\s\u4e00-\u9fcc]{2,255}$/;
            if (!contentRex.test(value)) {
                return "内容只能为2-255个字符";
            }
        }
    });
    // 监听提交事件
    form.on('submit(form-timeline-submit)', function (data) {
        let timeline = data.field;
        layer.confirm('确认提交？', {
            btn: ['确认', '取消']
        }, function (index) {
            $.ajax({
                url: "/timeline",
                data: {
                    content: timeline.content,
                    pushDate: timeline.pushDate
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