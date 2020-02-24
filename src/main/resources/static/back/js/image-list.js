layui.use(["laydate", "table", "util", "form"], function () {
    let form = layui.form;
    let table = layui.table;
    let util = layui.util;
    table.render({
        elem: '.layui-table',
        height: 450,
        id: "imageTable",
        url: "/image/listImagePage",
        page: true,
        method: "get",
        request: {
            pageName: "page",
            limitName: "count"
        },
        parseData: function (res) {
            return {
                "status": res.status,
                "count": res.fields.totalCount,
                "message": res.message,
                "data": res.data
            };
        },
        response: {
            statusName: "status",
            statusCode: 200,
            msgName: "message",
            countName: "count",
            dataName: "data"
        },
        cols: [
            [
                {type: 'checkbox', fixed: 'left'},
                {field: 'id', title: 'ID', width: 80, sort: true},
                {field: 'originName', title: '图片名', width: 130},
                {field: 'size', title: '图片大小（字节）', width: 130},
                {field: 'type', title: '图片类型', width: 130},
                {field: 'url', title: '图片url', width: 130},
                {field: 'owner', title: '上传者', width: 130},
                {
                    field: 'createTime', title: '创建时间', width: 170,
                    templet: function (d) {
                        return util.toDateString(d.createTime, "yyyy-MM-dd HH:ss");
                    }
                },
                {
                    field: 'modifyTime', title: '最后修改时间', width: 170, sort: true,
                    templet: function (d) {
                        return util.toDateString(d.modifyTime, "yyyy-MM-dd HH:ss");
                    }
                },
                {
                    field: 'status', title: '状态', width: 80, sort: true,
                    templet: function (d) {
                        if (d.status === 1) {
                            return "正常";
                        } else if (d.status === 0) {
                            return "已删除";
                        }
                    }
                },
                {title: "操作", fixed: 'right', width: 180, align: 'left', toolbar: '#toolbar'}
            ]
        ]
    });
    // 监听表单提交事件
    form.on('submit(search)', function (data) {
        table.reload("imageTable", {
            url: "/image/search",
            where: {
                originName: data.field.originName,
                type: data.field.type,
                owner: data.field.owner
            },
            method: "POST",
            parseData: function (res) {
                return {
                    "status": res.status,
                    "count": res.fields.totalCount,
                    "message": res.message,
                    "data": res.data
                };
            },
            response: {
                statusName: "status",
                statusCode: 200,
                msgName: "message",
                countName: "count",
                dataName: "data"
            }
        });
        return false;
    });
});