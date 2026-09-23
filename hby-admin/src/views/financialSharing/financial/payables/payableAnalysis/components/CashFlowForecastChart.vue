<template>
  <div class="cashflow-chart-container">
    <div class="chart-header">
      <div class="chart-title">
        <span>现金流预测图</span>
        <el-tag size="mini" type="info">置信度: {{ avgConfidence }}%</el-tag>
      </div>
    </div>
    <div ref="chart" class="chart-content" v-loading="loading"></div>
    <div class="chart-legend">
      <div class="legend-item">
        <span class="legend-line" style="background: #409eff;"></span>
        <span>预期付款</span>
      </div>
      <div class="legend-item">
        <span class="legend-area" style="background: rgba(64,158,255,0.2); border: 1px solid #409eff;"></span>
        <span>预测区间(95%置信度)</span>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCashFlowForecastData } from '@/api/financialSharing/payables'

export default {
  name: 'CashFlowForecastChart',
  props: {
    forecastPeriod: {
      type: Number,
      default: 6
    },
    forecastModel: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      chart: null,
      loading: false,
      forecastData: [],
      avgConfidence: 0
    }
  },
  mounted() {
    this.initChart()
    this.loadForecastData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  watch: {
    forecastPeriod() {
      this.loadForecastData()
    },
    forecastModel() {
      this.loadForecastData()
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
    },
    async loadForecastData() {
      this.loading = true
      try {
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        // const response = await getCashFlowForecastData({
        //   months: this.forecastPeriod,
        //   forecastModel: this.forecastModel
        // })
        // if (response.code === 1) {
        //   this.forecastData = response.data || []
        //   // 计算平均置信度
        //   if (this.forecastData.length > 0) {
        //     const totalConfidence = this.forecastData.reduce((sum, item) => sum + (item.confidence || 0), 0)
        //     this.avgConfidence = Math.round(totalConfidence / this.forecastData.length)
        //   }
        //   this.renderChart()
        // }

        // 暂未对接 API，先以空状态展示
        this.forecastData = []
        this.avgConfidence = 0
        this.renderChart()
      } catch (error) {
        console.error('加载现金流预测数据失败:', error)
        this.$message.error('加载现金流预测数据失败')
      } finally {
        this.loading = false
      }
    },
    renderChart() {
      if (!this.chart || !this.forecastData.length) {
        return
      }

      const periods = this.forecastData.map(item => item.period)
      const expectedPayments = this.forecastData.map(item => item.expectedPayment)
      const upperBounds = this.forecastData.map(item => item.upperBound)
      const lowerBounds = this.forecastData.map(item => item.lowerBound)
      const confidences = this.forecastData.map(item => item.confidence)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          formatter: (params) => {
            let result = `<div style="padding: 8px;"><div style="font-weight: bold; margin-bottom: 8px;">${params[0].axisValue}</div>`
            params.forEach(param => {
              if (param.seriesName === '预期付款') {
                result += `<div style="margin: 4px 0;">
                  <span style="display: inline-block; width: 10px; height: 10px; background: ${param.color}; border-radius: 50%; margin-right: 8px;"></span>
                  ${param.seriesName}: ${this.formatAmount(param.value)}
                </div>`
              }
            })
            // 添加置信区间信息
            const index = params[0].dataIndex
            result += `<div style="margin-top: 8px; padding-top: 8px; border-top: 1px solid #eee;">
              <div style="color: #909399;">置信区间:</div>
              <div>上限: ${this.formatAmount(upperBounds[index])}</div>
              <div>下限: ${this.formatAmount(lowerBounds[index])}</div>
              <div>置信度: ${confidences[index]}%</div>
            </div>`
            result += '</div>'
            return result
          }
        },
        grid: {
          left: '50px',
          right: '50px',
          bottom: '80px',
          top: '40px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: periods,
          boundaryGap: false,
          axisLabel: {
            interval: 0,
            rotate: 30,
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
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100
          },
          {
            start: 0,
            end: 100,
            height: 20,
            bottom: 10
          }
        ],
        series: [
          {
            name: '置信区间',
            type: 'line',
            data: upperBounds,
            lineStyle: {
              opacity: 0
            },
            areaStyle: {
              color: 'rgba(64,158,255,0.2)'
            },
            stack: 'confidence-band',
            symbol: 'none'
          },
          {
            name: '置信区间',
            type: 'line',
            data: lowerBounds,
            lineStyle: {
              opacity: 0
            },
            areaStyle: {
              color: 'rgba(64,158,255,0.2)'
            },
            stack: 'confidence-band-2',
            symbol: 'none'
          },
          {
            name: '预期付款',
            type: 'line',
            data: expectedPayments,
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#409eff'
            },
            itemStyle: {
              color: '#409eff',
              borderColor: '#fff',
              borderWidth: 2
            },
            emphasis: {
              scale: true,
              itemStyle: {
                borderWidth: 3
              }
            },
            markPoint: {
              data: [
                { type: 'max', name: '最大值' },
                { type: 'min', name: '最小值' }
              ],
              label: {
                formatter: (params) => this.formatAmount(params.value)
              }
            }
          }
        ],
        animationDuration: 1000,
        animationEasing: 'cubicOut'
      }

      this.chart.setOption(option, true)
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
      this.loadForecastData()
    }
  }
}
</script>

<style lang="scss" scoped>
.cashflow-chart-container {
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
    height: 450px;
    background: #ffffff;
    border-radius: 8px;
  }

  .chart-legend {
    display: flex;
    justify-content: center;
    margin-top: 16px;
    gap: 24px;

    .legend-item {
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #606266;

      .legend-line {
        display: inline-block;
        width: 30px;
        height: 3px;
        margin-right: 8px;
        border-radius: 2px;
      }

      .legend-area {
        display: inline-block;
        width: 30px;
        height: 16px;
        margin-right: 8px;
        border-radius: 2px;
      }
    }
  }
}
</style>
