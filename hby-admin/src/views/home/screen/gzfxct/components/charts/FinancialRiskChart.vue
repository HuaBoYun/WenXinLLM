<template>
  <div class="financial-risk-chart">
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinancialRiskChart',
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
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#409eff',
          textStyle: {
            color: '#fff'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['过度负债', '财务金融', '应付账款', '应收账款'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 11
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 11
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: [
          {
            type: 'bar',
            data: [
              {
                value: this.data?.debtRisk || 6,
                itemStyle: { color: '#f56c6c' }
              },
              {
                value: this.data?.financialRisk || 4,
                itemStyle: { color: '#e6a23c' }
              },
              {
                value: this.data?.payableRisk || 8,
                itemStyle: { color: '#409eff' }
              },
              {
                value: this.data?.receivableRisk || 5,
                itemStyle: { color: '#67c23a' }
              }
            ],
            barWidth: '50%',
            label: {
              show: true,
              position: 'top',
              color: '#fff',
              fontSize: 12
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
.financial-risk-chart {
  height: 100%;
  width: 100%;
  min-height: 0;

  .chart-container {
    height: 100%;
    width: 100%;
  }
}
</style>

