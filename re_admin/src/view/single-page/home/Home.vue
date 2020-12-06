<template>
    <div>
        <Row :gutter="20">
            <i-col :xs="12" :md="8" :lg="4" v-for="(infor, i) in inforCardData" :key="`infor-${i}`"
                   style="height: 120px;padding-bottom: 10px;">
                <infor-card shadow :color="infor.color" :icon="infor.icon" :icon-size="36">
                    <count-to :end="infor.count" count-class="count-style"/>
                    <p>{{ infor.title }}</p>
                </infor-card>
            </i-col>
        </Row>
        <Row :gutter="20" style="margin-top: 10px;">
            <i-col :md="24" :lg="8" style="margin-bottom: 20px;">
                <Card shadow>
                    <chart-pie style="height: 300px;" :value="pieData" text="用户访问来源"></chart-pie>
                </Card>
            </i-col>
            <i-col :md="24" :lg="16" style="margin-bottom: 20px;">
                <Card shadow>
                    <chart-bar style="height: 300px;" :value="barData" text="每周用户活跃量"/>
                </Card>
            </i-col>
        </Row>
        <Row :gutter="20" style="margin-top: 10px;">
            <!-- 内存监控图 -->
            <i-col :md="24" :lg="8" style="margin-bottom: 20px;">
                <Card shadow>
                    <memory-monitor style="height: 310px;" :memory-data="memoryData"/>
                </Card>
            </i-col>
            <!-- 系统信息 -->
            <i-col :md="24" :lg="16" style="margin-bottom: 20px;">
                <span>hello</span>
            </i-col>
        </Row>
    </div>
</template>

<script>
import InforCard from '_c/info-card'
import CountTo from '_c/count-to'
import {ChartPie, ChartBar} from '_c/common/charts'
import MemoryMonitor from '_c/echarts/MemoryMonitor.vue'
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import Cookies from "js-cookie";

export default {
    name: 'Home',
    components: {
        InforCard,
        CountTo,
        ChartPie,
        ChartBar,
        MemoryMonitor
    },
    data() {
        return {
            inforCardData: [
                {title: '总浏览量', icon: 'ios-eye-outline', count: 803, color: '#2d8cf0'},
                {title: '总文章数', icon: 'ios-book', count: 232, color: '#19be6b'},
                {title: '总评论数', icon: 'md-chatboxes', count: 142, color: '#ff9900'},
                {title: '友情链接', icon: 'md-link', count: 657, color: '#ed3f14'},
                {title: '文章类型', icon: 'ios-keypad', count: 12, color: '#E46CBB'},
                {title: '用户人数', icon: 'md-person', count: 3, color: '#9A66E4'}
            ],
            pieData: [
                {value: 335, name: '直接访问'},
                {value: 310, name: '邮件营销'},
                {value: 234, name: '联盟广告'},
                {value: 135, name: '视频广告'},
                {value: 1548, name: '搜索引擎'}
            ],
            barData: {
                Mon: 13253,
                Tue: 34235,
                Wed: 26321,
                Thu: 12340,
                Fri: 24643,
                Sat: 1322,
                Sun: 1324
            },
            memoryData: {}
        }
    },
    methods: {
        message() {
            let socket = new SockJS("http://localhost:8001/re/message");
            let client = Stomp.over(socket, {
                debug: false
            });
            let userInfo = Cookies.getJSON("userInfo");
            let token = userInfo.token;
            let headers = {
                Authorization: token
            }
            client.connect(headers, (frame) => {
                // 订阅系统监控消息
                client.subscribe("/topic/server/monitor", message => {
                    // 处理订阅消息
                    this.memoryData = JSON.parse(message.body);
                }, headers);
            });
        },
    },
    mounted() {
        this.message();
    }
}
</script>

<style lang="less">
.count-style {
    font-size: 50px;
}
</style>
