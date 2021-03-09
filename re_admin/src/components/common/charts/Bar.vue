<template>
  <div ref="dom" class="charts chart-bar"/>
</template>

<script>
import echarts from 'echarts'
import {on, off} from '@/libs/tools'

export default {
  name: 'ChartBar',
  props: {
    value: Object,
    text: String,
    subtext: String
  },
  data() {
    return {
      dom: null
    }
  },
  methods: {
    resize() {
      this.dom.resize()
    }
  },
  mounted() {
    this.$nextTick(() => {
      let xAxisData = Object.keys(this.value);
      let seriesData = Object.values(this.value);
      let option = {
        title: {
          text: this.text,
          subtext: this.subtext,
          x: 'center'
        },
        xAxis: {
          type: 'category',
          data: xAxisData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: seriesData,
          type: 'bar'
        }]
      };
      this.dom = echarts.init(this.$refs.dom, 'tdTheme');
      this.dom.setOption(option);
      on(window, 'resize', this.resize)
    })
  },
  beforeDestroy() {
    off(window, 'resize', this.resize)
  }
}
</script>
