<template>
  <div class="group-control-chart">
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'GroupControlChart',
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    initChart() {
      if (!this.$refs.chart) return
      
      if (this.chart) {
        this.chart.dispose()
      }
      
      this.chart = echarts.init(this.$refs.chart)
      
      const option = {
        radar: {
          indicator: [
            { name: '控股不控权', max: 10 },
            { name: '多层架构', max: 10 },
            { name: '超股比担保', max: 10 },
            { name: '违规挂靠', max: 10 }
          ],
          radius: '65%',
          axisName: {
            color: '#fff',
            fontSize: 11
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            }
          },
          splitArea: {
            areaStyle: {
              color: ['rgba(64, 158, 255, 0.05)', 'rgba(64, 158, 255, 0.1)']
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [
                  this.data?.controlRisk || 3,
                  this.data?.hierarchyRisk || 5,
                  this.data?.guaranteeRisk || 4,
                  this.data?.affiliationRisk || 2
                ],
                areaStyle: {
                  color: 'rgba(245, 108, 108, 0.4)'
                },
                lineStyle: {
                  color: '#f56c6c',
                  width: 2
                },
                itemStyle: {
                  color: '#f56c6c'
                }
              }
            ]
          }
        ]
      }
      
      this.chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.group-control-chart {
  height: 100%;
  width: 100%;
  min-height: 0;

  .chart-container {
    height: 100%;
    width: 100%;
  }
}
</style>

