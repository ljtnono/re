// 初始化评论系统
new Valine(VALINE_CONFIG);
// var gitalk = new Gitalk(GITTALK_CONFIG);
// gitalk.render('gitalk-container');
editormd.markdownToHTML("detail-content", {
    htmlDecode: "style,script,iframe",
    emoji: true,
    taskList: true,
    tocm: true,
    tex: true,
    flowChart: true,
    sequenceDiagram: true,
    codeFold: true
});