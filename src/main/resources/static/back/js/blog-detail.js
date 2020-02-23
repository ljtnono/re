console.log("正在编辑博客，blogId = " + blogId);
let options = [];
let blogEntity;
let blogEditor;
layui.use(['form', 'layer'], function () {
    let form = layui.form;
    let layer = layui.layer;
    // 判断是否存在blogId, 如果不存在就报错
    if (blogId) {
        // ajax请求获取文章类型
        $.ajax({
            url: "/blog_type",
            method: "GET",
            dataType: "json",
            success: function (res) {
                // 请求成功，将option拼接到下拉框中去
                if (res.request === "success" && res.status === 200) {
                    $.each(res.data, function (index, item) {
                        $('#type').append(new Option(item.name, item.name));
                        options.push(item);
                    });
                    $.ajax({
                        url: "/blog/" + blogId,
                        method: "GET",
                        dataType: "json",
                        success: function (res) {
                            if (res.request === "success" && res.status === 200 && res.data) {
                                //给表单的各项属性赋值
                                let blog = res.data[0];
                                blogEntity = blog;
                                $("#type option").each(function (index, item) {
                                    if ($(item).text() === blog.type) {
                                        $(item).attr("selected", "selected");
                                    }
                                });
                                form.val("form-blog", {
                                    "id": blog.id,
                                    "title": blog.title,
                                    "author": blog.author,
                                    "summary": blog.summary,
                                    "contentMarkdown": blog.contentMarkdown
                                });
                                // 初始化markdown编辑器
                                blogEditor = editormd("blog-editormd", EDITOR_CONFIG);
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
            title: function (value, item) {
                // 验证只能4-30个字符 包括中文、字母、数字下划线
                let titleRex = /^[\w\W\s\u4e00-\u9fcc]{4,30}$/;
                if (!titleRex.test(value)) {
                    return "标题只能为4-64个字符的字符串";
                }
            },
            author: function (value, item) {
                let titleRex = /^[\w\W\s\u4e00-\u9fcc]{2,10}$/;
                if (!titleRex.test(value)) {
                    return "作者只能为2-10个字符";
                }
            },
            summary: function (value, item) {
                let titleRex = /^[\w\W\s\u4e00-\u9fcc]{0,300}$/;
                if (!titleRex.test(value)) {
                    return "简介只能为0-300个字符";
                }
            }
        });
        form.on('submit(form-blog-submit)', function (data) {
            let blog = data.field;
            layer.confirm('确认提交？', {
                btn: ['确认', '取消']
            }, function (index) {
                $.ajax({
                    url: "/blog/" + blogId,
                    data: {
                        title: blog.title,
                        author: blog.author,
                        summary: blog.summary,
                        type: blog.type,
                        contentMarkdown: blogEditor.getMarkdown(),
                        contentHtml: blogEditor.getHTML(),
                        status: blogEntity.status,
                        coverImage: blogEntity.coverImage,
                        view: blogEntity.view,
                        comment: blogEntity.comment
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