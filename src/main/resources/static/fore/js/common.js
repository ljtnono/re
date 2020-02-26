const IMG_DEFAULT = "https://ftp.ljtnono.cn/re/images/default_img.jpg";
const IMG_QRCODE_WECHAT = "https://ftp.ljtnono.cn/re/images/qrcode-wechat.png";
// markdown编辑器配置
const EDITOR_CONFIG = {
    width: "100%",
    height: 730,
    path: '/static/fore/lib/editor.md-1.5.0/lib/',
    codeFold: true,
    saveHTMLToTextarea: true,
    searchReplace: true,
    htmlDecode: "style,script,iframe|on*",
    emoji: true,
    taskList: true,
    theme: "dark",
    editorTheme: "rubyblue",
    previewTheme: "dark",
    tocm: true,
    tex: true,
    flowChart: true,
    sequenceDiagram: true,
    imageUpload: true,
    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
    imageUploadURL: "/image/upload/md?token=" + localStorage.getItem("token"),
    onload: function (data) {

    }
};
// swiper配置
const SWIPER_CONFIG = {
    speed: 800,
    loop: true,
    grabCursor: true,
    init: true,
    zoom: false,
    autoplay: {
        autoplay: true,
        delay: 3000,
        stopOnLastSlide: false,
        disableOnInteraction: true
    },
    effect: "slide",
    pagination: {
        el: '.swiper-pagination',
        type: "bullets",
        hideOnClick: true,
        clickable: true,
        bulletClass: "swiper-pagination-bullet",
        bulletActiveClass: "swiper-pagination-bullet-active"
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev'
    }
};
// Valine配置
const VALINE_CONFIG = {
    el: '#comments',
    appId: 'iyTC528gHvHe09Qg8b5HPrvj-gzGzoHsz',
    appKey: '8ndLNYwjGS7FPLKdzgRKCfeL',
    notify: false,
    verify: false,
    avatar: 'wavatar',
    placeholder: '评论请留下您的邮箱和昵称....'
};
//git talk配置
const GITTALK_CONFIG = {
    clientID: '5cc717ab6cb59ddac50c',
    clientSecret: 'a0d53f27cde720cb65c570cb60843309697eab0d',
    repo: 're_comment',
    owner: 'ljtnono',
    admin: ['ljtnono'],
    id: location.href,
    distractionFreeMode: false
};
// 配置bootstrap提示
$('[data-toggle="tooltip"]').tooltip();
$('[data-toggle="popover"]').popover({
    html: true,
    content: '<img src="' + IMG_QRCODE_WECHAT + '" style="width: 100%; height: 100%" alt="我的微信"/>',
    trigger: "hover"
});
// 百度统计
var _hmt = _hmt || [];
(function() {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?25d4f481bf3b3c166a6240c7d7b1e21a";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})();

// 计算初始页面元素的offsetTop
let $scroll_fixed = $("[scroll-fixed]");
let scroll_fixed_offsetTop = [];
let scroll_fixed_top = [];
$scroll_fixed.each(function (index, obj) {
    scroll_fixed_offsetTop.push($(obj).offset().top);
    if (index === 0 || index === "0") {
        scroll_fixed_top.push(0);
    } else {
        scroll_fixed_top.push($scroll_fixed.eq(index - 1).outerHeight() + 20);
    }
});

// 简单的节流函数
function throttle(func, wait, mustRun) {
    let timeout, startTime = new Date();
    return function () {
        let context = this, args = arguments, curTime = new Date();
        clearTimeout(timeout);
        // 如果达到了规定的触发时间间隔，触发 handler
        if (curTime - startTime >= mustRun) {
            func.apply(context, args);
            startTime = curTime;
            // 没达到触发间隔，重新设定定时器
        } else {
            timeout = setTimeout(func, wait);
        }
    };
}

// 实际想绑定在 scroll 事件上的 handler
function realFunc() {
    let scrollTop = $("html, body").scrollTop();
    if (scrollTop > 0) {
        $(".roll-top").fadeIn(800);
    } else {
        $(".roll-top").fadeOut(800);
    }
    $scroll_fixed.each(function (index, obj) {
        if (scrollTop >= scroll_fixed_offsetTop[index]) {
            $(obj).addClass("pf");
            $(obj).css("top", scroll_fixed_top[index]);
            $(obj).css("width", "inherit");
        } else {
            $(obj).css("top", "inherit");
            $(obj).removeClass("pf");
        }
    });
}

$(window).on("resize", throttle(function () {
    // 计算初始页面元素的offsetTop
    scroll_fixed_offsetTop = [];
    scroll_fixed_top = [];
    $scroll_fixed.each(function (index, obj) {
        scroll_fixed_offsetTop.push($(obj).offset().top);
        if (index === 0 || index === "0") {
            scroll_fixed_top.push(0);
        } else {
            scroll_fixed_top.push($scroll_fixed.eq(index - 1).outerHeight() + 20);
        }
    });
},20, 1000));
// 采用了节流函数
$(window).on("scroll", throttle(realFunc, 20, 1000));

/**鼠标点击回到顶部事件*/
function rollTop() {
    $("html, body").clearQueue().animate({
        scrollTop: 0
    }, "ease");
}
/** 侧边栏导航点击事件 */
$("#header .side-nav-header .side-nav-bar").on("click", function () {
   // 直接toggle
    $("#header .nav-mini").slideToggle(400);
});

