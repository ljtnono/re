let util;
layui.use(['laypage', 'util'], function () {
    util = layui.util;
});
function getData(curr, limit, type) {
    $.ajax({
        url: "/blog/listBlogPageByType",
        data: {
            type: type,
            page: curr,
            count: limit
        },
        method: "GET",
        dataType: "json",
        success: function (res) {
            if (res.request === "success" && res.status === 200) {
                renderData(res.data);
            }
        }
    });
}
function renderData(data) {
    let lis = [];
    for (let i = 0; i < data.length; i++) {
        let item = "<div class=\"articles\">" +
            "            <article class=\"article mb10 p10\">" +
            "                <header class=\"article-header mb10\">" +
            "                    <span class=\"article-label f12 mr5\">" + data[i].type +
            "                       <i class=\"label-arrow\"></i>" +
            "                    </span>" +
            "                    <span class=\"article-title f14\">" + data[i].title + "</span>" +
            "                </header>" +
            "                <section class=\"article-detail flex\">" +
            "                    <div class=\"mr10 article-thumb cursor-pointer\">" +
            "                        <img src='" + data[i].coverImage + "'" +
            "                             alt='" + data[i].title + "'" +
            "                             title='" + data[i].title + "'/>" +
            "                    </div>" +
            "                    <div class=\"article-summary flex flex-direction-column flex-justify-content-space-between\">" +
            "                        <a href='/article/" + data[i].id + "'>" +
            data[i].summary +
            "                        </a>" +
            "                        <div class=\"article-info\">" +
            "                            <a href=\"#\">" +
            "                               <i class=\"fa fa-user pr5\" aria-hidden=\"true\"></i>" +
            "                               <span style='color: #00A67C;'>" + data[i].author + "</span>" +
            "                            </a>" +
            "                            <a href=\"#\">" +
            "                               <i class=\"fa fa-clock-o\" aria-hidden=\"true\"></i>" +
            "                               <span>" + util.toDateString(data[i].modifyTime, "yyyy-MM-dd") + "</span>" +
            "                            </a>" +
            "                            <a href=\"#\">" +
            "                               <i class=\"fa fa-eye\" aria-hidden=\"true\"></i>" +
            "                               <span>" + data[i].view + "浏览</span>" +
            "                            </a>" +
            "                            <a href=\"#\">" +
            "                               <i class=\"fa fa-comment\" aria-hidden=\"true\"></i>" +
            "                               <span>" + data[i].comment + "评论</span>" +
            "                            </a>" +
            "                        </div>" +
            "                    </div>" +
            "                </section>" +
            "            </article>" +
            "        </div>";
        lis.push(item);
        $(".articles").html(lis.join(''));
    }
}