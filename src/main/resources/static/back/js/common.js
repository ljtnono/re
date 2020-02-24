const IMG_DEFAULT = "https://ftp.ljtnono.cn/re/images/default_img.jpg";
const IMG_QRCODE_WECHAT = "https://ftp.ljtnono.cn/re/images/qrcode-wechat.png";
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