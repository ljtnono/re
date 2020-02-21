// 初始化评论系统
new Valine(VALINE_CONFIG);
const color = ["#49c085", "#f2b63c", "#f58a87", "#6f92ff", "#7782d1", "#d56464"];
let myChart = echarts.init($(".chart")[0]);
let option = {
    left: "100%",
    top: "100%",
    title: {
        text: "个人技能"
    },
    tooltip: {},
    legend: {
        show: true
    },
    yAxis: {
        name: "分数",
        min: 0,
        max: 100
    }
};
myChart.showLoading();
// 获取所有的技能列表
$.ajax({
    url: "/skill",
    method: "GET",
    success: function (res) {
        if (res.request === "success" && res.status === 200) {
            let data = [];
            let xAxis = [];
            for (let i = 0; i < res.data.length; i++) {
                if (res.data[i].status === 1) {
                    data.push({
                        name: res.data[i].name,
                        value: res.data[i].percent,
                        itemStyle: {
                            color: color[i % color.length]
                        }
                    });
                }
                xAxis.push(res.data[i].name);
            }
            myChart.hideLoading();
            option.xAxis = {
                name: "技能",
                position: 'bottom',
                data: xAxis
            };
            option.series = [{
                name: '技能',
                type: 'bar',
                label: {
                    show: true,
                    position: "top"
                },
                data: data
            }];
            myChart.setOption(option);
        }
    }
});
// 监听resize事件，实现echarts随着窗口大小发生变化而变化
$(window).resize(function () {
    myChart.resize();
});