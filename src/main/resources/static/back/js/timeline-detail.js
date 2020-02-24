console.log("正在编辑博客类型，timelineId = " + timelineId);
layui.use(['form', 'layer', 'util'], function () {
    let form = layui.form;
    let layer = layui.layer;
    let util = layui.util;
    let timeline;
    if (timelineId) {
        // 获取timeline相关信息
        // 设置表单校验
        form.verify({
            content: function (value, item) {
                let contentRex = /^[\w\W\s\u4e00-\u9fcc]{2,255}$/;
                if (!contentRex.test(value)) {
                    return "内容只能为2-255个字符";
                }
            }
        });
        $.ajax({
            url: "/timeline/" + timelineId,
            method: "GET",
            dataType: "json",
            success: function (res) {
                if (res.request === "success" && res.status === 200 && res.data) {
                    timeline = res.data[0];
                    form.val("form-timeline", {
                        "id": timeline.id,
                        "content": timeline.content,
                        "pushDate": util.toDateString(timeline.pushDate, "yyyy-MM-dd")
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
        form.on('submit(form-timeline-submit)', function (data) {
            let timeline = data.field;
            layer.confirm('确认提交？', {
                btn: ['确认', '取消']
            }, function (index) {
                $.ajax({
                    url: "/timeline/" + timelineId,
                    data: {
                        content: timeline.content,
                        pushDate: timeline.pushDate
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
