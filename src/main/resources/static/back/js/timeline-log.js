layui.use(['util'], function () {
    let util = layui.util;
    $.ajax({
        url: "/timeline/listUpdateLogTimeline",
        method: "GET",
        success: function (res) {
            if (res.request === "success" && res.status === 200) {
                for (let i = 0; i < res.data.length; i++) {
                    if (res.data[i].status !== 0) {
                        let item = "<li class=\"layui-timeline-item\">" +
                            "                            <i class=\"layui-icon layui-timeline-axis\">&#xe63f;</i>" +
                            "                            <div class=\"layui-timeline-content layui-text\">" +
                            "                                <h3 class=\"layui-timeline-title\">" + util.toDateString(res.data[i].pushDate, "yyyy-MM-dd") + "</h3>" +
                            "                                <p>" +
                            res.data[i].content +
                            "                                </p>" +
                            "                            </div>" +
                            "                        </li>";
                        $("ul.layui-timeline").append(item);
                    }
                }
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
});