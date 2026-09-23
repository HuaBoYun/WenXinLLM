<template>
  <div class="risk-overview-card" v-loading="loading">
    <div class="card-header">
      <i class="el-icon-pie-chart"></i>
      <h3>风险总览</h3>
    </div>
    
    <div class="overview-content">
      <!-- 风险等级分布 -->
      <div class="risk-level-section">
        <div class="section-title">风险等级分布</div>
        <div class="risk-levels">
          <div class="level-item critical">
            <div class="level-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="level-info">
              <div class="level-label">严重风险</div>
              <div class="level-value">{{ overviewData?.criticalCount || 0 }}</div>
            </div>
          </div>
          <div class="level-item high">
            <div class="level-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="level-info">
              <div class="level-label">高风险</div>
              <div class="level-value">{{ overviewData?.highCount || 0 }}</div>
            </div>
          </div>
          <div class="level-item medium">
            <div class="level-icon">
              <i class="el-icon-info"></i>
            </div>
            <div class="level-info">
              <div class="level-label">中风险</div>
              <div class="level-value">{{ overviewData?.mediumCount || 0 }}</div>
            </div>
          </div>
          <div class="level-item low">
            <div class="level-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="level-info">
              <div class="level-label">低风险</div>
              <div class="level-value">{{ overviewData?.lowCount || 0 }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 风险类别统计 -->
      <div class="risk-category-section">
        <div class="section-title">风险类别统计</div>
        <div class="category-chart" ref="categoryChart"></div>
      </div>

      <!-- 风险趋势 -->
      <div class="risk-trend-section">
        <div class="section-title">风险趋势（近6个月）</div>
        <div class="trend-chart" ref="trendChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'RiskOverviewCard',
  props: {
    overviewData: {
      type: Object,
      default: () => ({
        criticalCount: 5,
        highCount: 12,
        mediumCount: 23,
        lowCount: 8,
        categoryData: [
          { name: '投资风险', value: 8 },
          { name: '集团管控', value: 6 },
          { name: '财务风险', value: 10 },
          { name: '信用风险', value: 7 },
          { name: '经营风险', value: 9 },
          { name: '采购风险', value: 5 },
          { name: '法律风险', value: 3 },
          { name: '贸易风险', value: 4 }
        ],
        trendData: {
          months: ['7月', '8月', '9月', '10月', '11月', '12月'],
          critical: [3, 4, 5, 4, 6, 5],
          high: [8, 10, 12, 11, 13, 12],
          medium: [15, 18, 20, 22, 24, 23],
          low: [5, 6, 7, 8, 9, 8]
        }
      })
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      categoryChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initCategoryChart()
      this.initTrendChart()
    })
  },
  beforeDestroy() {
    if (this.categoryChart) {
      this.categoryChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  watch: {
    overviewData: {
      deep: true,
      handler() {
        this.$nextTick(() => {
          this.initCategoryChart()
          this.initTrendChart()
        })
      }
    }
  },
  methods: {
    initCategoryChart() {
      if (!this.$refs.categoryChart) return

      if (this.categoryChart) {
        this.categoryChart.dispose()
      }

      this.categoryChart = echarts.init(this.$refs.categoryChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: {
            color: '#606266',
            fontSize: 12
          },
          itemWidth: 12,
          itemHeight: 12,
          itemGap: 8
        },
        series: [
          {
            type: 'pie',
            radius: ['45%', '70%'],
            center: ['30%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 13,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.overviewData?.categoryData || []
          }
        ],
        color: ['#f56c6c', '#e6a23c', '#409eff', '#67c23a', '#909399', '#c45656', '#5470c6', '#91cc75']
      }

      this.categoryChart.setOption(option)
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      
      this.trendChart = echarts.init(this.$refs.trendChart)
      
      const trendData = this.overviewData?.trendData || {}
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['严重', '高风险', '中风险', '低风险'],
          textStyle: {
            color: '#606266'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: trendData.months || [],
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266'
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266'
          },
          splitLine: {
            lineStyle: {
              color: '#ebeef5'
            }
          }
        },
        series: [
          {
            name: '严重',
            type: 'bar',
            stack: 'total',
            data: trendData.critical || [],
            itemStyle: {
              color: '#f56c6c'
            }
          },
          {
            name: '高风险',
            type: 'bar',
            stack: 'total',
            data: trendData.high || [],
            itemStyle: {
              color: '#e6a23c'
            }
          },
          {
            name: '中风险',
            type: 'bar',
            stack: 'total',
            data: trendData.medium || [],
            itemStyle: {
              color: '#409eff'
            }
          },
          {
            name: '低风险',
            type: 'bar',
            stack: 'total',
            data: trendData.low || [],
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-overview-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .card-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 2px solid #f0f0f0;

    i {
      font-size: 24px;
      color: #409eff;
    }

    h3 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }
  }

  .overview-content {
    display: grid;
    grid-template-columns: 1fr 1fr 1.5fr;
    gap: 30px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #606266;
      margin-bottom: 15px;
    }

    .risk-level-section {
      .risk-levels {
        display: flex;
        flex-direction: column;
        gap: 15px;

        .level-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 12px;
          border-radius: 6px;
          transition: all 0.3s ease;

          &.critical {
            background: rgba(245, 108, 108, 0.1);
            border-left: 4px solid #f56c6c;

            .level-icon {
              color: #f56c6c;
            }
          }

          &.high {
            background: rgba(230, 162, 60, 0.1);
            border-left: 4px solid #e6a23c;

            .level-icon {
              color: #e6a23c;
            }
          }

          &.medium {
            background: rgba(64, 158, 255, 0.1);
            border-left: 4px solid #409eff;

            .level-icon {
              color: #409eff;
            }
          }

          &.low {
            background: rgba(103, 194, 58, 0.1);
            border-left: 4px solid #67c23a;

            .level-icon {
              color: #67c23a;
            }
          }

          &:hover {
            transform: translateX(5px);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          }

          .level-icon {
            font-size: 28px;
          }

          .level-info {
            flex: 1;

            .level-label {
              font-size: 14px;
              color: #909399;
              margin-bottom: 4px;
            }

            .level-value {
              font-size: 24px;
              font-weight: bold;
              color: #303133;
            }
          }
        }
      }
    }

    .risk-category-section {
      .category-chart {
        height: 250px;
      }
    }

    .risk-trend-section {
      .trend-chart {
        height: 250px;
      }
    }
  }
}
</style>

