<template>
    <div ref="dom" class="charts charts-bar"/>
</template>

<script>
import echarts from "echarts";
import {on, off} from "@/libs/tools";

export default {
    name: "MemoryMonitor",
    data() {
        return {
            dom: null,
            // 可用内存
            memoryAvailableData: [],
            // 总内存
            memoryTotalData: [],
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
        this.memoryAvailableData.push(this.memoryData.memoryAvailable / 1024 / 1024);
        this.memoryTotalData.push(this.memoryData.memoryTotal / 1024 / 1024);
        this.timeStamp.push(new Date(this.memoryData.timeStamp));
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
            color: ["#319AFF", "#19BE6B", "#FF9900", "#FA76CD", "#9A66E4"],
            legend: {
            },
            tooltip: {
                trigger: 'axis'
            },
            xAxis: {
                name: "时间",
                type: "time",
                splitLine: {
                    show: false
                },
                nameGap: 10
            },
            yAxis: {
                name: "空间/MB",
                type: "value",
                interval: 3000,
                nameGap: 30
            },
            series: [
                {
                    name: "可用内存",
                    type: "line",
                    showSymbol: true,
                    symbolSize: 10,
                    hoverAnimation: true,
                    data: availableData
                },
                {
                    name: "总内存",
                    type: "line",
                    showSymbol: true,
                    symbolSize: 10,
                    hoverAnimation: true,
                    data: totalData
                }
            ]
        }
        this.$nextTick(() => {
            this.dom = echarts.init(this.$refs.dom);
            this.dom.setOption(option);
            on(window, "resize", this.resize);
        });
    },
    watch: {
        memoryData() {
            if (this.memoryAvailableData.length > 5) {
                this.memoryAvailableData.shift();
            }
            if (this.memoryTotalData.length > 5) {
                this.memoryTotalData.shift();
            }
            if (this.timeStamp.length > 5) {
                this.timeStamp.shift();
            }
            this.memoryAvailableData.push(this.memoryData.memoryAvailable / 1024 / 1024);
            this.memoryTotalData.push(this.memoryData.memoryTotal / 1024 / 1024);
            this.timeStamp.push(new Date(this.memoryData.timeStamp));
            this.systemName = this.memoryData.systemName;
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
        memoryData: {
            type: Object,
            default: null
        }
    }
};
</script>

<style lang="less">

</style>
