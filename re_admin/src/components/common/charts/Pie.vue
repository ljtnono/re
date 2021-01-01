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
          color: ["rgb(30,136,229)", "rgb(86,205,195)", "rgb(72,205,127)", "rgb(88,86,205)", "rgb(236,74,158)", "rgb(207,74,180)", "rgb(180,74,207)", "rgb(143,74,207)", "rgb(74,184,207)", "rgb(74,207,93)", "rgb(124,207,74)", "rgb(162,207,74)", "rgb(207,199,74)", "rgb(207,199,74)", "rgb(255,146,74)", "rgb(255,205,74)", "rgb(255,255,74)", "rgb(205,255,74)", "rgb(155,255,74)", "rgb(105,255,74)", "rgb(55,255,74)", "rgb(55,255,124)", "rgb(55,255,174)", "rgb(55,255,224)", "rgb(55,255,124)", "rgb(55,224,255)", "rgb(55,174,255)", "rgb(55,80,255)", "rgb(55,80,255)", "rgb(154,55,255)", "rgb(154,55,255)", "rgb(200,55,255)", "rgb(255,55,255)", "rgb(255,55,180)", "rgb(255,55,150)", "rgb(255,55,100)", "rgb(86,117,205)", "rgb(86,170,205)", "rgb(86,170,205)", "rgb(86,205,195)", "rgb(134,205,86)", "rgb(173,205,86)", "rgb(173,205,86)", "rgb(173,205,86)", "rgb(205,192,86)", "rgb(213,140,62)", "rgb(232,118,118)", "rgb(232,118,196)", "rgb(212,118,232)", "rgb(212,118,232)", "rgb(174,118,232)", "rgb(118,188,232)", "rgb(118,188,232)", "rgb(118,223,232)", "rgb(118,232,204)", "rgb(118,232,204)", "rgb(162,232,105)", "rgb(195,232,105)", "rgb(216,232,105)", "rgb(216,232,105)", "rgb(233,189,105)", "rgb(233,150,105)", "rgb(233,150,105)", "rgb(233,105,105)", "rgb(255,142,142)", "rgb(255,142,240)", "rgb(255,142,240)", "rgb(214,142,255)", "rgb(214,142,255)", "rgb(150,142,255)", "rgb(142,193,255)", "rgb(150,142,255)", "rgb(142,255,255)", "rgb(142,255,166)", "rgb(200,255,142)", "rgb(200,255,142)", "rgb(255,255,142)", "rgb(255,205,142)", "rgb(200,142,142)" ],
          title: {
            text: this.text,
            subtext: this.subtext,
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
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
        on(window, 'resize', this.resize)
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
      on(window, 'resize', this.resize);
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
    off(window, 'resize', this.resize);
  }
}
</script>
