<template>
  <div class="bar-chart-wrapper">
    <div v-if="title" class="chart-title">{{ title }}</div>
    <div ref="barChart" class="chart-container"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'BarChart',
  props: {
    title: {
      type: String,
      default: '',
    },
    data: {
      type: Object,
      required: true,
      // data: { xAxis: ['类型A', '类型B', ...], series: [{ name: '数量', data: [100, 200, ...] }] }
    },
    height: {
      type: String,
      default: '300px',
    },
    horizontal: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      chart: null,
    }
  },
  watch: {
    data: {
      handler() {
        this.updateChart()
      },
      deep: true,
    },
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.barChart)
      this.updateChart()
    },
    updateChart() {
      if (!this.chart || !this.data) return

      const option = {
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true,
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          data: this.data.series.map((s) => s.name),
          top: 0,
        },
        xAxis: this.horizontal
          ? {
              type: 'value',
              axisLabel: { color: '#606266' },
              axisLine: { lineStyle: { color: '#DCDFE6' } },
              splitLine: { lineStyle: { color: '#EBEEF5' } },
            }
          : {
              type: 'category',
              data: this.data.xAxis || [],
              axisLabel: { color: '#606266' },
              axisLine: { lineStyle: { color: '#DCDFE6' } },
            },
        yAxis: this.horizontal
          ? {
              type: 'category',
              data: this.data.xAxis || [],
              axisLabel: { color: '#606266' },
              axisLine: { lineStyle: { color: '#DCDFE6' } },
            }
          : {
              type: 'value',
              axisLabel: { color: '#606266' },
              axisLine: { lineStyle: { color: '#DCDFE6' } },
              splitLine: { lineStyle: { color: '#EBEEF5' } },
            },
        series: this.data.series.map((s) => ({
          name: s.name,
          type: 'bar',
          data: s.data || [],
          barWidth: '60%',
          itemStyle: {
            color: s.color || undefined,
          },
          label: {
            show: true,
            position: this.horizontal ? 'right' : 'top',
            color: '#606266',
          },
        })),
      }

      this.chart.setOption(option, true)
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },
  },
}
</script>

<style scoped>
.bar-chart-wrapper {
  width: 100%;
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 16px;
  text-align: center;
}

.chart-container {
  width: 100%;
  height: 300px;
}
</style>

