console.log("正在编辑链接，linkId = " + linkId);
layui.use(['form', 'layer'], function () {
    let form = layui.form;
    let layer = layui.layer;
    let link;
    let options = [];
    if (linkId) {
        // 设置表单校验
        form.verify({
            name: function (value, item) {
                let nameRex = /^[\w\W\s\u4e00-\u9fcc]{2,20}$/;
                if (!nameRex.test(value)) {
                    return "链接名只能为2-20个字符";
                }
            }
        });
        // 获取link相关信息
        $.ajax({
            url: "/link_type",
            method: "GET",
            dataType: "json",
            success: function (res) {
                if (res.request === "success" && res.status === 200 && res.data) {
                    $.each(res.data, function (index, item) {
                        $('#type').append(new Option(item.name, item.name));
                        options.push(item);
                    });
                    $.ajax({
                        url: "/link/" + linkId,
                        method: "GET",
                        dataType: "json",
                        success: function (res) {
                            if (res.request === "success" && res.status === 200 && res.data) {
                                link = res.data[0];
                                $("#type option").each(function (index, item) {
                                    if ($(item).text() === link.type) {
                                        $(item).attr("selected", "selected");
                                    }
                                });
                                form.val("form-link", {
                                    "id": link.id,
                                    "url": link.url,
                                    "name": link.name
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
        form.on('submit(form-link-submit)', function (data) {
            let link = data.field;
            layer.confirm('确认提交？', {
                btn: ['确认', '取消']
            }, function (index) {
                $.ajax({
                    url: "/link/" + linkId,
                    data: {
                        url: link.url,
                        name: link.name,
                        type: link.type
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