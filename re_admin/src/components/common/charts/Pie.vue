<template>
  <div ref="dom" />
</template>

<script>
import * as echarts from 'echarts';
import {off, on} from "@/libs/tools";

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
          center: [50, 50],
          title: {
            text: this.text,
            x: 'center',
            textStyle: {
              fontSize: '14',
              color: "#63b2ee"
            }
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b}:{c}',
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            data: legend
          },
          series: [
            {
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              data: this.value,
              label: {
                normal: {
                  show: true,
                  textStyle: {
                    fontSize: '12'
                  }
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '14',
                    fontWeight: 'bold'
                  }
                }
              },
              labelLine: {
                show: true
              }
            }
          ]
        };
        this.dom = echarts.init(this.$refs.dom);
        this.dom.setOption(option);
      });
    },
    setOption() {
      let legend = this.value.map(v => v.name);
      let option = {
        color: ["#63b2ee", "#76da91", "#f8cb7f", "#f89588", "#7cd6cf", "#9192ab", "#7898e1", "#efa666", "#eddd86", "#9987ce", "#63b2ee", "#765005"],
        title: {
          text: this.text,
          x: 'center',
          textStyle: {
            fontSize: '14',
            color: "#63b2ee"
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}:{c}',
        },
        legend: {
          orient: 'vertical',
          right: 0,
          data: legend
        },
        series: {
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          top: 50,
          left: 0,
          data: this.value,
          label: {
            normal: {
              show: true,
              textStyle: {
                fontSize: '12'
              }
            },
            emphasis: {
              show: true,
              textStyle: {
                fontSize: '14',
                fontWeight: 'bold'
              }
            }
          },
          labelLine: {
            show: true
          },
        }
      };
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
    this.$nextTick(() => {
      this.dom = echarts.init(this.$refs.dom);
      this.dom.setOption(option);
    });
  },
  beforeDestroy() {
  }
}
</script>

<style lang="less" scoped>
</style>
