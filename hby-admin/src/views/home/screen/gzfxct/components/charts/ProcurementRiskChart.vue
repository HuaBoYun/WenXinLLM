<template>
  <div class="procurement-risk-chart">
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ProcurementRiskChart',
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
          top: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        yAxis: {
          type: 'category',
          data: ['招标采购', '违规招投标', '靠企吃企', '超合同支付', '公款消费'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          }
        },
        series: [
          {
            type: 'bar',
            data: [
              { value: this.data?.biddingRisk || 7, itemStyle: { color: '#f56c6c' } },
              { value: this.data?.irregularBidding || 5, itemStyle: { color: '#e6a23c' } },
              { value: this.data?.relatedParty || 4, itemStyle: { color: '#409eff' } },
              { value: this.data?.overPayment || 3, itemStyle: { color: '#67c23a' } },
              { value: this.data?.publicConsumption || 2, itemStyle: { color: '#909399' } }
            ],
            barWidth: '60%',
            label: {
              show: true,
              position: 'right',
              color: '#fff',
              fontSize: 11
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
.procurement-risk-chart {
  height: 100%;
  width: 100%;
  min-height: 0;

  .chart-container {
    height: 100%;
    width: 100%;
  }
}
</style>

