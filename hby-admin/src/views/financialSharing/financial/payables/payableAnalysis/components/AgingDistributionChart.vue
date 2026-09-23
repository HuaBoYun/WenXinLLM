<template>
  <div class="aging-chart-container">
    <div class="chart-header">
      <div class="chart-title">
        <span>账龄分布图</span>
        <el-radio-group v-model="chartType" size="mini" @change="handleChartTypeChange">
          <el-radio-button label="bar">柱状图</el-radio-button>
          <el-radio-button label="pie">饼图</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <div ref="chart" class="chart-content" v-loading="loading"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getAgingAnalysisChart } from '@/api/financialSharing/payables'

export default {
  name: 'AgingDistributionChart',
  data() {
    return {
      chart: null,
      chartType: 'bar',
      loading: false,
      agingData: []
    }
  },
  mounted() {
    this.initChart()
    this.loadAgingData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
    },
    async loadAgingData() {
      this.loading = true
      try {
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        // const response = await getAgingAnalysisChart({})
        // if (response.code === 1) {
        //   this.agingData = response.data || []
        //   this.renderChart()
        // }

        // 暂未对接 API，先以空状态展示
        this.agingData = []
        this.renderChart()
      } catch (error) {
        console.error('加载账龄数据失败:', error)
        this.$message.error('加载账龄数据失败')
      } finally {
        this.loading = false
      }
    },
    renderChart() {
      if (!this.chart || !this.agingData.length) {
        return
      }

      const colors = ['#67c23a', '#95d475', '#e6a23c', '#f56c6c', '#f89898']
      const chartData = this.agingData.map((item, index) => ({
        name: item.agingRange,
        value: item.amount,
        count: item.count,
        percentage: item.percentage,
        itemStyle: {
          color: colors[index % colors.length]
        }
      }))

      if (this.chartType === 'bar') {
        this.renderBarChart(chartData)
      } else {
        this.renderPieChart(chartData)
      }
    },
    renderBarChart(chartData) {
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: (params) => {
            const item = params[0]
            const data = item.data
            return `
              <div style="padding: 8px;">
                <div style="font-weight: bold; margin-bottom: 4px;">${data.name}</div>
                <div>金额: ${this.formatAmount(data.value)}</div>
                <div>笔数: ${data.count}</div>
                <div>占比: ${data.percentage.toFixed(2)}%</div>
              </div>
            `
          }
        },
        grid: {
          left: '50px',
          right: '50px',
          bottom: '50px',
          top: '40px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: chartData.map(item => item.name),
          axisLabel: {
            interval: 0,
            rotate: 0,
            color: '#606266'
          },
          axisLine: {
            lineStyle: {
              color: '#DCDFE6'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)',
          axisLabel: {
            formatter: (value) => (value / 10000).toFixed(0),
            color: '#606266'
          },
          axisLine: {
            lineStyle: {
              color: '#DCDFE6'
            }
          },
          splitLine: {
            lineStyle: {
              type: 'dashed',
              color: '#EBEEF5'
            }
          }
        },
        series: [
          {
            name: '应付金额',
            type: 'bar',
            data: chartData.map(item => ({
              value: item.value,
              ...item
            })),
            barWidth: '50%',
            itemStyle: {
              borderRadius: [4, 4, 0, 0]
            },
            label: {
              show: true,
              position: 'top',
              formatter: (params) => {
                return (params.value.percentage || 0).toFixed(1) + '%'
              },
              color: '#606266',
              fontSize: 12
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ],
        animationDuration: 1000,
        animationEasing: 'cubicOut'
      }

      this.chart.setOption(option, true)
    },
    renderPieChart(chartData) {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            return `
              <div style="padding: 8px;">
                <div style="font-weight: bold; margin-bottom: 4px;">${params.name}</div>
                <div>金额: ${this.formatAmount(params.value)}</div>
                <div>笔数: ${params.data.count}</div>
                <div>占比: ${params.percent}%</div>
              </div>
            `
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          textStyle: {
            color: '#606266'
          }
        },
        series: [
          {
            name: '账龄分布',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            data: chartData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
              color: '#606266'
            },
            labelLine: {
              show: true
            }
          }
        ],
        animationDuration: 1000,
        animationEasing: 'cubicOut'
      }

      this.chart.setOption(option, true)
    },
    handleChartTypeChange() {
      this.renderChart()
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    refresh() {
      this.loadAgingData()
    }
  }
}
</script>

<style lang="scss" scoped>
.aging-chart-container {
  width: 100%;
  height: 100%;

  .chart-header {
    margin-bottom: 16px;

    .chart-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .chart-content {
    width: 100%;
    height: 400px;
    background: #ffffff;
    border-radius: 8px;
  }
}
</style>
