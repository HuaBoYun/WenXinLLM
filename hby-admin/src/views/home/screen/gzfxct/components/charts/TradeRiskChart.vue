<template>
  <div class="trade-risk-chart">
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'TradeRiskChart',
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
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: [
              { 
                value: this.data?.fakeTradeFin || 8, 
                name: '融资性贸易',
                itemStyle: { color: '#f56c6c' }
              },
              { 
                value: this.data?.fakeTradeEmpty || 6, 
                name: '空转走单',
                itemStyle: { color: '#e6a23c' }
              }
            ],
            label: {
              show: true,
              color: '#fff',
              fontSize: 12,
              formatter: '{b}\n{c}'
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
.trade-risk-chart {
  height: 100%;
  width: 100%;
  min-height: 0;

  .chart-container {
    height: 100%;
    width: 100%;
  }
}
</style>

