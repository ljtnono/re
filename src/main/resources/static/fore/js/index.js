"use strict";
// 配置swiper
new Swiper(".swiper-container", SWIPER_CONFIG);
// 流加载
layui.use(["flow", "util"], function () {
    let flow = layui.flow;
    let util = layui.util;
    flow.load({
        elem: ".articles",
        isAuto: true,
        end: "没有更多了",
        done: function (page, next) {
            let lis = [];
            $.ajax({
                url: "/re/blog/listBlogPage",
                method: "GET",
                data: {
                    page: page,
                    count: 10
                },
                dataType: "json",
                success: function (res) {
                    if (res.request === "success" && res.status === 200) {
                        layui.each(res.data, function (index, item) {
                            if (item.status === 1) {
                                let text = '<article class="article mb10 p10">'+
                                    '    <header class="article-header mb10">'+
                                    '        <a href="/re/articles/' + item.type + '"> '+
                                    '            <span class="article-label f12 mr5">' + item.type + '<i class="label-arrow"></i></span>'+
                                    '        </a> '+
                                    '        <a href="/re/article/' + item.id + '">'+
                                    '            <span class="article-title f14">' + item.title + '</span>'+
                                    '        </a>'+
                                    '    </header>'+
                                    '    <section class="article-detail flex">'+
                                    '        <div class="mr10 article-thumb cursor-pointer">'+
                                    '            <img src="' + item.coverImage + '" alt="' + item.title + '" title="' + item.title + '">'+
                                    '        </div>'+
                                    '        <div class="article-summary flex flex-direction-column flex-justify-content-space-between">'+
                                    '            <a href="/re/article/' + item.id + '">'+
                                    item.summary+
                                    '            </a>'+
                                    '            <div class="article-info">'+
                                    '                <a href="#">'+
                                    '                    <i class="fa fa-user pr5" aria-hidden="true"></i>'+
                                    '                    <span style="color: #00A67C;">' + item.author + '</span></a>'+
                                    '                <a href="#">'+
                                    '                    <i class="fa fa-clock-o" aria-hidden="true"></i>'+
                                    '                    <span>' + util.toDateString(item.modifyTime, "yyyy-MM-dd") + '</span>'+
                                    '                </a> '+
                                    '                <a href="#">'+
                                    '                    <i class="fa fa-eye" aria-hidden="true"></i>'+
                                    '                    <span>' + item.view + '浏览</span>'+
                                    '                </a> '+
                                    '                <a href="#">'+
                                    '                    <i class="fa fa-comment" aria-hidden="true"></i>'+
                                    '                    <span>' + item.comment + '评论</span>'+
                                    '                </a>'+
                                    '            </div>'+
                                    '        </div>'+
                                    '    </section>'+
                                    '</article>';
                                lis.push(text);
                            }
                        });
                        next(lis.join(''), page < res.fields.totalPages);
                    }
                }
            });
        }
    });
});
