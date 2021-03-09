<template>
  <div ref="dom" class="charts chart-pie"/>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'Pie',
  props: {
    value: Array,
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
    },
    init() {
      this.$nextTick(() => {
        let legend = this.value.map(v => v.name);
        let option = {
          color: ["#63b2ee", "#76da91", "#f8cb7f", "#f89588", "#7cd6cf", "#9192ab", "#7898e1", "#efa666", "#eddd86", "#9987ce", "#63b2ee", "#765005"],
          title: {
            text: this.text,
            subtext: this.subtext,
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b}:{c}'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: legend
          },
          series: [
            {
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: this.value,
              label: {
                normal: {
                  show: true,
                  textStyle: {
                    fontWeight: 400,
                    fontSize: 12
                  },
                  formatter: '{b} {c}'
                }
              },
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        this.dom = echarts.init(this.$refs.dom, 'tdTheme');
        this.dom.setOption(option);
      });
    },
    setOption() {
      let legend = this.value.map(v => v.name);
      let option = {
        legend: {
          orient: 'vertical',
          left: 'left',
          data: legend
        },
        series: [
          {
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: this.value,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      this.dom = echarts.init(this.$refs.dom, 'tdTheme');
      this.dom.setOption(option);
    }
  },
  watch: {
    value() {
      this.setOption();
    }
  },
  mounted() {
    this.init();
  },
  beforeDestroy() {

  }
}
</script>
