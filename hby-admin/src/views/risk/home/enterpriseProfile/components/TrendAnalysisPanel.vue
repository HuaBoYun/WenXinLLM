<template>
  <div class="trend-analysis-panel">
    <div class="section-title">
      <i class="el-icon-s-data"></i>
      趋势分析
    </div>
    
    <div class="panel-content" v-loading="loading">
      <!-- 图表区域 -->
      <div class="chart-area">
        <div ref="trendChart" class="chart-container"></div>
      </div>

      <!-- 如果没有数据，显示开发中提示 -->
      <div v-if="!trendData || trendData.message" class="development-notice">
        <div class="notice-text">
          <i class="el-icon-info"></i>
          <span>{{ trendData?.message || '趋势分析功能开发中' }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'TrendAnalysisPanel',
  props: {
    enterpriseId: {
      type: String,
      required: true
    },
    trendData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      trendChart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initPreviewChart()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    /**
     * 初始化预览图表
     */
    initPreviewChart() {
      if (!this.$refs.trendChart) return
      
      this.trendChart = echarts.init(this.$refs.trendChart)
      
      // 模拟趋势数据
      const option = {
        title: {
          text: '财务指标趋势（示例）',
          left: 'center',
          textStyle: {
            fontSize: 14,
            color: '#666'
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          bottom: 10,
          data: ['净资产收益率', '总资产报酬率', '销售利润率']
        },
        xAxis: {
          type: 'category',
          data: ['2022Q1', '2022Q2', '2022Q3', '2022Q4', '2023Q1', '2023Q2', '2023Q3', '2023Q4']
        },
        yAxis: {
          type: 'value',
          name: '百分比(%)'
        },
        series: [
          {
            name: '净资产收益率',
            type: 'line',
            data: [12.5, 13.2, 14.1, 15.3, 14.8, 15.6, 16.2, 15.9],
            smooth: true,
            lineStyle: {
              color: '#677eea'
            }
          },
          {
            name: '总资产报酬率',
            type: 'line',
            data: [8.2, 8.8, 9.1, 9.5, 9.2, 9.8, 10.1, 9.9],
            smooth: true,
            lineStyle: {
              color: '#f093fb'
            }
          },
          {
            name: '销售利润率',
            type: 'line',
            data: [6.8, 7.2, 7.5, 7.8, 7.6, 8.1, 8.3, 8.0],
            smooth: true,
            lineStyle: {
              color: '#4facfe'
            }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },

    /**
     * 处理窗口大小变化
     */
    handleResize() {
      if (this.trendChart) {
        this.trendChart.resize()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.trend-analysis-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-sizing: border-box;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: #1e3c72;
    margin-bottom: 15px;
    flex-shrink: 0;

    i {
      font-size: 18px;
    }
  }

  .panel-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-height: 0;

    .chart-area {
      flex: 1;
      min-height: 0;

      .chart-container {
        width: 100%;
        height: 100%;
        min-height: 300px;
      }
    }

    .development-notice {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;

      .notice-text {
        display: flex;
        align-items: center;
        gap: 8px;
        color: #666;
        font-size: 14px;

        i {
          font-size: 16px;
          color: #409EFF;
        }
      }
    }
  }
}
</style>
