<template>
  <div class="pie-chart-wrapper">
    <div v-if="title" class="chart-title">{{ title }}</div>
    <div ref="pieChart" class="chart-container"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'PieChart',
  props: {
    title: {
      type: String,
      default: '',
    },
    data: {
      type: Array,
      required: true,
      // data: [{ name: '类型A', value: 100 }, { name: '类型B', value: 200 }]
    },
    height: {
      type: String,
      default: '300px',
    },
    radius: {
      type: [String, Array],
      default: '60%',
    },
    showLegend: {
      type: Boolean,
      default: true,
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
      this.chart = echarts.init(this.$refs.pieChart)
      this.updateChart()
    },
    updateChart() {
      if (!this.chart || !this.data) return

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
        },
        legend: this.showLegend
          ? {
              orient: 'vertical',
              right: '10%',
              top: 'center',
              data: this.data.map((item) => item.name),
            }
          : undefined,
        series: [
          {
            name: this.title || '统计',
            type: 'pie',
            radius: this.radius,
            center: ['40%', '50%'],
            data: this.data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
            label: {
              formatter: '{b}: {d}%',
            },
          },
        ],
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
.pie-chart-wrapper {
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

