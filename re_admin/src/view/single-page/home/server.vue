<template>
    <div ref="dom" class="charts charts-bar"/>
</template>

<script>
import echarts from "echarts";
import {on, off} from "@/libs/tools";

export default {
    name: "server",
    data() {
        return {
            dom: null,
            // 可用内存
            memoryAvailableData: [],
            // 总内存
            memoryTotalData: [],
            // 系统名字
            systemName: "",
            // 横轴时间
            timeStamp: []
        };
    },
    methods: {
        resize() {
            this.dom.resize();
        }
    },
    mounted() {
        this.memoryAvailableData.push(this.serverMonitorBarData.memoryAvailable / 1024 / 1024);
        this.memoryTotalData.push(this.serverMonitorBarData.memoryTotal / 1024 / 1024);
        this.timeStamp.push(new Date(this.serverMonitorBarData.timeStamp));
        this.systemName = this.serverMonitorBarData.systemName;
        let totalData = [];
        let availableData = [];
        let totalLength = this.memoryTotalData.length;
        let availableLength = this.memoryAvailableData.length;
        for (let i = 0; i < availableLength; i++) {
            availableData.push([
                new Date(this.timeStamp[i]), this.memoryAvailableData[i]
            ]);
        }
        for (let i = 0; i < totalLength; i++) {
            totalData.push([
                new Date(this.timeStamp[i]), this.memoryTotalData[i]
            ]);
        }
        // 配置项
        let option = {
            title: {
                text: this.systemName
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    animation: false
                }
            },
            xAxis: {
                name: "时间",
                type: "time",
                splitLine: {
                    show: false
                }
            },
            yAxis: {
                name: "MB",
                type: "value",
                minInterval: 3000,
                boundaryGap: [0, '100%'],
                splitLine: {
                    show: false
                }
            },
            series: [{
                name: "可用内存",
                type: "line",
                showSymbol: true,
                hoverAnimation: true,
                data: availableData
            }, {
                name: "总内存",
                type: "line",
                showSymbol: true,
                hoverAnimation: true,
                data: totalData
            }]
        }
        this.$nextTick(() => {
            this.dom = echarts.init(this.$refs.dom);
            this.dom.setOption(option);
            on(window, "resize", this.resize);
        });
    },
    watch: {
        serverMonitorBarData() {
            if (this.memoryAvailableData.length > 100) {
                this.memoryAvailableData.shift();
            }
            if (this.memoryTotalData.length > 100) {
                this.memoryTotalData.shift();
            }
            if (this.timeStamp.length > 100) {
                this.timeStamp.shift();
            }
            this.memoryAvailableData.push(this.serverMonitorBarData.memoryAvailable / 1024 / 1024);
            this.memoryTotalData.push(this.serverMonitorBarData.memoryTotal / 1024 / 1024);
            this.timeStamp.push(new Date(this.serverMonitorBarData.timeStamp));
            this.systemName = this.serverMonitorBarData.systemName;
            let totalData = [];
            let availableData = [];
            let totalLength = this.memoryTotalData.length;
            let availableLength = this.memoryAvailableData.length;
            for (let i = 0; i < availableLength; i++) {
                availableData.push([
                    new Date(this.timeStamp[i]), this.memoryAvailableData[i]
                ]);
            }
            for (let i = 0; i < totalLength; i++) {
                totalData.push([
                    new Date(this.timeStamp[i]), this.memoryTotalData[i]
                ]);
            }
            let option = {
                series: [{
                    name: "可用内存",
                    type: "line",
                    showSymbol: true,
                    hoverAnimation: true,
                    data: availableData
                }, {
                    name: "总内存",
                    type: "line",
                    showSymbol: true,
                    hoverAnimation: true,
                    data: totalData
                }]
            }
            this.dom.setOption(option);
        }
    },
    beforeDestroy() {
        off(window, "resize", this.resize);
    },
    props: {
        serverMonitorBarData: {
            type: Object,
            default: null
        }
    }
};
</script>
