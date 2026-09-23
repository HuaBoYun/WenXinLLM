<template>
  <div class="risk-maturity-chart">
    <div class="chart-title">风险成熟度评价</div>
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'RiskMaturityChart',
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
            type: 'gauge',
            radius: '80%',
            center: ['50%', '60%'],
            startAngle: 200,
            endAngle: -20,
            min: 0,
            max: 100,
            splitNumber: 10,
            itemStyle: {
              color: '#409eff'
            },
            progress: {
              show: true,
              width: 20
            },
            pointer: {
              show: true,
              length: '60%',
              width: 8
            },
            axisLine: {
              lineStyle: {
                width: 20,
                color: [
                  [0.3, '#f56c6c'],
                  [0.7, '#e6a23c'],
                  [1, '#67c23a']
                ]
              }
            },
            axisTick: {
              distance: -25,
              splitNumber: 5,
              lineStyle: {
                width: 2,
                color: '#fff'
              }
            },
            splitLine: {
              distance: -30,
              length: 14,
              lineStyle: {
                width: 3,
                color: '#fff'
              }
            },
            axisLabel: {
              distance: -50,
              color: '#fff',
              fontSize: 12
            },
            anchor: {
              show: false
            },
            title: {
              show: false
            },
            detail: {
              valueAnimation: true,
              width: '60%',
              lineHeight: 40,
              borderRadius: 8,
              offsetCenter: [0, '80%'],
              fontSize: 24,
              fontWeight: 'bold',
              formatter: '{value}分',
              color: '#67c23a'
            },
            data: [
              {
                value: 75
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
.risk-maturity-chart {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  min-height: 0;

  .chart-title {
    font-size: 14px;
    font-weight: bold;
    color: #409eff;
    text-align: center;
    padding: 5px 0;
    flex-shrink: 0;
  }

  .chart-container {
    flex: 1;
    min-height: 0;
    width: 100%;
  }
}
</style>

