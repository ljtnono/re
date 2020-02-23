// 百度统计
var _hmt = _hmt || [];
(function () {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?25d4f481bf3b3c166a6240c7d7b1e21a";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})();
layui.use(['form', 'layer'], function () {
    let form = layui.form;
    let layer = layui.layer;
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
    form.on('submit(login)', function (data) {
        let user = data.field;
        layer.msg('正在登陆...', {anim: 5}, function (index) {
            $.ajax({
                url: "/admin/login",
                data: {
                    username: user.username,
                    password: user.password
                },
                method: "POST",
                dataType: "json",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                },
                success: function (res) {
                    if (res.request === "success" && res.status === 200) {
                        // 拿到了token之后存到本地
                        let token = res.fields.token;
                        localStorage.setItem("token", token);
                        location.href = "/admin/index?token=" + token;
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