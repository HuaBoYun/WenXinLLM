<template>
  <div class="audit-rectification-chart">
    <div class="chart-title">审计问题整改</div>
    <div class="chart-container" ref="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'AuditRectificationChart',
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
              { value: 761, name: '已整改', itemStyle: { color: '#67c23a' } },
              { value: 257, name: '整改中', itemStyle: { color: '#e6a23c' } },
              { value: 56, name: '未整改', itemStyle: { color: '#f56c6c' } }
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
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
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
.audit-rectification-chart {
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

