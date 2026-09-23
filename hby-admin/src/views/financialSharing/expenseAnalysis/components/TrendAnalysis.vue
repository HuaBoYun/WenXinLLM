<template>
  <div class="trend-analysis">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>费用趋势分析</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="exportData">导出</el-button>
      </div>
      
      <!-- 时间范围选择 -->
      <div class="filter-bar">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange">
        </el-date-picker>
        <el-select v-model="timeGranularity" placeholder="时间粒度" @change="handleGranularityChange">
          <el-option label="按月" value="month"></el-option>
          <el-option label="按季度" value="quarter"></el-option>
          <el-option label="按年" value="year"></el-option>
        </el-select>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ trendStats.totalAmount | currency }}</div>
            <div class="stat-label">总费用</div>
            <div class="stat-change" :class="trendStats.totalChange >= 0 ? 'positive' : 'negative'">
              {{ trendStats.totalChange >= 0 ? '+' : '' }}{{ trendStats.totalChange }}%
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ trendStats.avgAmount | currency }}</div>
            <div class="stat-label">平均费用</div>
            <div class="stat-change" :class="trendStats.avgChange >= 0 ? 'positive' : 'negative'">
              {{ trendStats.avgChange >= 0 ? '+' : '' }}{{ trendStats.avgChange }}%
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ trendStats.maxAmount | currency }}</div>
            <div class="stat-label">最高费用</div>
            <div class="stat-change" :class="trendStats.maxChange >= 0 ? 'positive' : 'negative'">
              {{ trendStats.maxChange >= 0 ? '+' : '' }}{{ trendStats.maxChange }}%
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ trendStats.growthRate }}%</div>
            <div class="stat-label">增长率</div>
            <div class="stat-change" :class="trendStats.growthRate >= 0 ? 'positive' : 'negative'">
              {{ trendStats.growthRate >= 0 ? '上升' : '下降' }}
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="24">
          <div class="chart-container">
            <h4>费用趋势图</h4>
            <div ref="trendChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>同比分析</h4>
            <div ref="compareChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>预测分析</h4>
            <div ref="forecastChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <div class="table-container">
        <h4>趋势数据明细</h4>
        <el-table :data="trendData" border style="width: 100%">
          <el-table-column prop="period" label="时间周期" width="120"></el-table-column>
          <el-table-column prop="totalAmount" label="总费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.totalAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="expenseCount" label="费用笔数" width="100"></el-table-column>
          <el-table-column prop="avgAmount" label="平均费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.avgAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="growthRate" label="增长率" width="100">
            <template slot-scope="scope">
              <span :class="scope.row.growthRate >= 0 ? 'positive' : 'negative'">
                {{ scope.row.growthRate >= 0 ? '+' : '' }}{{ scope.row.growthRate }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="budgetUsage" label="预算使用率" width="120">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.budgetUsage" :color="getProgressColor(scope.row.budgetUsage)"></el-progress>
            </template>
          </el-table-column>
          <el-table-column prop="trend" label="趋势" width="100">
            <template slot-scope="scope">
              <i :class="getTrendIcon(scope.row.trend)" :style="{ color: getTrendColor(scope.row.trend) }"></i>
              {{ scope.row.trend }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { expenseAnalysisApi } from '@/api/financialSharing/advancedFeatures'

export default {
  name: 'TrendAnalysis',
  data() {
    return {
      dateRange: [],
      timeGranularity: 'month',
      trendStats: {
        totalAmount: 0,
        avgAmount: 0,
        maxAmount: 0,
        growthRate: 0,
        totalChange: 0,
        avgChange: 0,
        maxChange: 0
      },
      trendData: [],
      trendChart: null,
      compareChart: null,
      forecastChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.trendChart) this.trendChart.dispose()
    if (this.compareChart) this.compareChart.dispose()
    if (this.forecastChart) this.forecastChart.dispose()
  },
  methods: {
    loadData() {
      expenseAnalysisApi.getTrendAnalysis({
        startDate: this.dateRange ? this.dateRange[0] : '',
        endDate: this.dateRange ? this.dateRange[1] : '',
        granularity: this.timeGranularity
      }).then(res => {
        if (res.code === 1 && res.data) {
          this.trendStats = res.data.stats || {}
          this.trendData = res.data.list || []
        }
      }).catch(error => {
        console.error('加载费用趋势分析失败:', error)
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.initTrendChart()
        this.initCompareChart()
        this.initForecastChart()
      })
    },
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总费用', '费用笔数']
        },
        xAxis: {
          type: 'category',
          data: this.trendData.map(item => item.period)
        },
        yAxis: [
          {
            type: 'value',
            name: '费用金额(元)',
            position: 'left'
          },
          {
            type: 'value',
            name: '费用笔数',
            position: 'right'
          }
        ],
        series: [
          {
            name: '总费用',
            type: 'line',
            yAxisIndex: 0,
            data: this.trendData.map(item => item.totalAmount),
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '费用笔数',
            type: 'bar',
            yAxisIndex: 1,
            data: this.trendData.map(item => item.expenseCount),
            itemStyle: {
              color: '#67C23A'
            }
          }
        ]
      }
      this.trendChart.setOption(option)
    },
    initCompareChart() {
      this.compareChart = echarts.init(this.$refs.compareChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['今年', '去年']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [
          {
            name: '今年',
            type: 'line',
            data: [180000, 195000, 175000, 210000, 225000, 265000],
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '去年',
            type: 'line',
            data: [165000, 178000, 185000, 192000, 205000, 220000],
            itemStyle: {
              color: '#E6A23C'
            }
          }
        ]
      }
      this.compareChart.setOption(option)
    },
    initForecastChart() {
      this.forecastChart = echarts.init(this.$refs.forecastChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['实际费用', '预测费用']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月']
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [
          {
            name: '实际费用',
            type: 'line',
            data: [180000, 195000, 175000, 210000, 225000, 265000, null, null, null],
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '预测费用',
            type: 'line',
            data: [null, null, null, null, null, 265000, 280000, 295000, 310000],
            itemStyle: {
              color: '#F56C6C'
            },
            lineStyle: {
              type: 'dashed'
            }
          }
        ]
      }
      this.forecastChart.setOption(option)
    },
    handleDateChange(value) {
      console.log('日期范围变化:', value)
      this.loadData()
    },
    handleGranularityChange(value) {
      console.log('时间粒度变化:', value)
      this.loadData()
    },
    getProgressColor(percentage) {
      if (percentage < 70) return '#67C23A'
      if (percentage < 90) return '#E6A23C'
      return '#F56C6C'
    },
    getTrendIcon(trend) {
      return trend === '上升' ? 'el-icon-top' : 'el-icon-bottom'
    },
    getTrendColor(trend) {
      return trend === '上升' ? '#67C23A' : '#F56C6C'
    },
    exportData() {
      expenseAnalysisApi.exportReport({ type: 'trend' }).then(res => {
        if (res.code === 1) {
          this.$message.success('导出成功')
          if (res.data && res.data.url) {
            window.open(res.data.url)
          }
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(() => {
        this.$message.error('导出失败')
      })
    }
  },
  filters: {
    currency(value) {
      if (!value) return '¥0.00'
      return '¥' + value.toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    }
  }
}
</script>

<style scoped>
.trend-analysis {
  padding: 20px;
}

.filter-bar {
  margin-bottom: 20px;
}

.filter-bar .el-date-editor,
.filter-bar .el-select {
  margin-right: 15px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.stat-change {
  font-size: 12px;
  font-weight: bold;
}

.stat-change.positive {
  color: #67C23A;
}

.stat-change.negative {
  color: #F56C6C;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.chart {
  width: 100%;
  height: 400px;
}

.table-container {
  margin-top: 20px;
}

.table-container h4 {
  margin-bottom: 15px;
  color: #333;
}

.positive {
  color: #67C23A;
}

.negative {
  color: #F56C6C;
}
</style>
