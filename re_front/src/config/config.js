const imgDefault = "https://ftp.ljtnono.cn/re/images/default_img.jpg";
const imgQrCodeWeChat = "https://ftp.ljtnono.cn/re/images/qrcode-wechat.png";
const githubAddr = "https://github.com/ljtnono/re";
const recordNum = "鄂ICP备18013706号";
const author = "最后的疼爱";
const authorAddr = "湖北-武汉";
const email = "935188400@qq.com";
const imgHuaWeiYun = "https://ftp.ljtnono.cn/re/images/huaweiyun.png";
const imgAliYun = "https://ftp.ljtnono.cn/re/images/aliyun.png";
const imgMysql = "https://ftp.ljtnono.cn/re/images/mysql.png";
const imgRedis = "https://ftp.ljtnono.cn/re/images/redis-white.png";
const imgNginx = "https://ftp.ljtnono.cn/re/images/nginx.png";
const imgTomcat = "https://ftp.ljtnono.cn/re/images/tomcat.png";
const imgAvatar = "https://ftp.ljtnono.cn/re/images/avatar.png";
const imgLogoWord = "https://ftp.ljtnono.cn/re/images/logo_word.png";
const imgQrCodeWeChatSk = "https://ftp.ljtnono.cn/re/images/qrcode-wechat-sk.png";
const imgQrCodeZfb = "https://ftp.ljtnono.cn/re/images/qrcode-zfb.jpg";
const swiperOption = {
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
const valineConfig = {
  el: '#comments',
  appId: 'iyTC528gHvHe09Qg8b5HPrvj-gzGzoHsz',
  appKey: '8ndLNYwjGS7FPLKdzgRKCfeL',
  notify: false,
  verify: false,
  avatar: 'wavatar',
  placeholder: '评论请留下您的邮箱和昵称....'
};
module.exports = {
  imgDefault: imgDefault,
  imgQrCodeWeChat: imgQrCodeWeChat,
  githubAddr: githubAddr,
  recordNum: recordNum,
  author: author,
  imgHuaWeiYun: imgHuaWeiYun,
  imgAliYun: imgAliYun,
  imgMysql: imgMysql,
  imgRedis: imgRedis,
  imgNginx: imgNginx,
  imgTomcat: imgTomcat,
  imgAvatar: imgAvatar,
  imgLogoWord: imgLogoWord,
  imgQrCodeWeChatSk: imgQrCodeWeChatSk,
  imgQrCodeZfb: imgQrCodeZfb,
  swiperOption: swiperOption,
  valineConfig: valineConfig,
  authorAddr,
  email
};
