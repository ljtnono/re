<template>
    <div ref="dom" />
</template>

<script>
import echarts from "echarts";
import {on, off} from "@/libs/tools";

export default {
    name: "server",
    data() {
        return {
            dom: null
        };
    },
    methods: {
        resize() {
            this.dom.resize();
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.dom = echarts.init(this.$refs.dom);
            this.dom.setOption(option);
            on(window, "resize", this.resize);
        });
    },
    beforeDestroy() {
        off(window, "resize", this.resize);
    },
    props: {
        options: {
            type: Object,
            default: null
        }
    }
};
</script>
