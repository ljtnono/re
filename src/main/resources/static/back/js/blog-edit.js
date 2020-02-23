// 发表博客弹出层
let publishLayerHtml = $("#publishLayerHtml").html();
// 初始化markdown编辑器
let blogEditor = editormd("blog-editormd", EDITOR_CONFIG);
// 提取markdown中图片内容作为封面
function getImgUrlFromMarkDown(markDown) {
    let reg = /!\[.*?\]\((.*?)\)/im;
    let matches = reg.exec(markDown);
    // 如果没有的话，那么返回默认图片src
    if (matches === null || matches === undefined || matches.length === 0) {
        return IMG_DEFAULT;
    } else {
        return matches[1];
    }
}
layui.use(['layer', 'form'], function () {
    let layer = layui.layer;
    let form = layui.form;
    let publishLayer;
    let options = [];
    $("#confirm").on("click", function () {
        publishLayer = layer.open({
            type: 1,
            area: ["60%"],
            shadeClose: true,
            title: "发布博客",
            content: publishLayerHtml,
            success: function () {
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
            }
        });
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
        }
    });
    form.on("submit(form-blog-submit)", function (data) {
        let blog = data.field;
        layer.confirm('确认发表？', {
            btn: ['确认', '取消']
        }, function (index) {
            let coverImage = getImgUrlFromMarkDown(blogEditor.getMarkdown());
            let contentHtml = blogEditor.getHTML();
            let contentMarkdown = blogEditor.getMarkdown();
            let blogObject = {
                title: blog.title,
                author: blog.author,
                summary: blog.summary,
                type: blog.type,
                contentMarkdown: contentMarkdown,
                contentHtml: contentHtml,
                coverImage: coverImage
            };
            $.ajax({
                url: "/blog",
                data: blogObject,
                method: "POST",
                dateType: "json",
                success: function (res) {
                    if (res.request === "success" && res.status === 200) {
                        layer.msg(res.message, {icon: 1});
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