<template>
  <div class="business-risk-chart">
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'BusinessRiskChart',
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
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#409eff',
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            type: 'pie',
            radius: '60%',
            center: ['50%', '50%'],
            data: [
              { value: this.data?.operatingLoss || 8, name: '经营潜亏', itemStyle: { color: '#f56c6c' } },
              { value: this.data?.contractRisk || 6, name: '合同风险', itemStyle: { color: '#e6a23c' } },
              { value: this.data?.cashPosition || 5, name: '资金头寸', itemStyle: { color: '#409eff' } },
              { value: this.data?.salaryIssues || 3, name: '薪酬乱象', itemStyle: { color: '#67c23a' } }
            ],
            label: {
              show: true,
              color: '#fff',
              fontSize: 11
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              }
            }
          }
        ]
      }
      
      this.chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.business-risk-chart {
  height: 100%;
  width: 100%;
  min-height: 0;

  .chart-container {
    height: 100%;
    width: 100%;
  }
}
</style>

