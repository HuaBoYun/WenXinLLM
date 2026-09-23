<template>
  <div class="comparison-analysis">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>对比分析</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="exportData">导出</el-button>
      </div>
      
      <!-- 对比条件选择 -->
      <div class="filter-bar">
        <el-select v-model="comparisonType" placeholder="对比类型" @change="handleTypeChange">
          <el-option label="部门对比" value="department"></el-option>
          <el-option label="时间对比" value="time"></el-option>
          <el-option label="人员对比" value="person"></el-option>
          <el-option label="项目对比" value="project"></el-option>
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange">
        </el-date-picker>
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ comparisonStats.totalDifference | currency }}</div>
            <div class="stat-label">总差异</div>
            <div class="stat-change" :class="comparisonStats.totalDifference >= 0 ? 'positive' : 'negative'">
              {{ comparisonStats.totalDifference >= 0 ? '增加' : '减少' }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ comparisonStats.avgDifference | currency }}</div>
            <div class="stat-label">平均差异</div>
            <div class="stat-change" :class="comparisonStats.avgDifference >= 0 ? 'positive' : 'negative'">
              {{ comparisonStats.avgDifference >= 0 ? '增加' : '减少' }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ comparisonStats.maxDifference | currency }}</div>
            <div class="stat-label">最大差异</div>
            <div class="stat-change" :class="comparisonStats.maxDifference >= 0 ? 'positive' : 'negative'">
              {{ comparisonStats.maxDifference >= 0 ? '增加' : '减少' }}
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ comparisonStats.changeRate }}%</div>
            <div class="stat-label">变化率</div>
            <div class="stat-change" :class="comparisonStats.changeRate >= 0 ? 'positive' : 'negative'">
              {{ comparisonStats.changeRate >= 0 ? '上升' : '下降' }}
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="24">
          <div class="chart-container">
            <h4>对比分析图</h4>
            <div ref="comparisonChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>差异分布</h4>
            <div ref="differenceChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>变化趋势</h4>
            <div ref="trendChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <div class="table-container">
        <h4>对比数据明细</h4>
        <el-table :data="comparisonData" border style="width: 100%">
          <el-table-column prop="name" label="对比项目" width="150"></el-table-column>
          <el-table-column prop="currentAmount" label="当前金额" width="120">
            <template slot-scope="scope">
              {{ scope.row.currentAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="previousAmount" label="对比金额" width="120">
            <template slot-scope="scope">
              {{ scope.row.previousAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="difference" label="差异金额" width="120">
            <template slot-scope="scope">
              <span :class="scope.row.difference >= 0 ? 'positive' : 'negative'">
                {{ scope.row.difference >= 0 ? '+' : '' }}{{ scope.row.difference | currency }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变化率" width="100">
            <template slot-scope="scope">
              <span :class="scope.row.changeRate >= 0 ? 'positive' : 'negative'">
                {{ scope.row.changeRate >= 0 ? '+' : '' }}{{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="trend" label="趋势" width="100">
            <template slot-scope="scope">
              <i :class="getTrendIcon(scope.row.trend)" :style="{ color: getTrendColor(scope.row.trend) }"></i>
              {{ scope.row.trend }}
            </template>
          </el-table-column>
          <el-table-column prop="ranking" label="排名" width="80">
            <template slot-scope="scope">
              <el-tag :type="getRankingType(scope.row.ranking)">{{ scope.row.ranking }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
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
  name: 'ComparisonAnalysis',
  data() {
    return {
      comparisonType: 'department',
      dateRange: [],
      comparisonStats: {
        totalDifference: 0,
        avgDifference: 0,
        maxDifference: 0,
        changeRate: 0
      },
      comparisonData: [],
      comparisonChart: null,
      differenceChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.comparisonChart) this.comparisonChart.dispose()
    if (this.differenceChart) this.differenceChart.dispose()
    if (this.trendChart) this.trendChart.dispose()
  },
  methods: {
    loadData() {
      expenseAnalysisApi.getComparisonAnalysis({ type: this.comparisonType }).then(res => {
        if (res.code === 1 && res.data) {
          this.comparisonStats = res.data.stats || {}
          this.comparisonData = res.data.list || []
        }
      }).catch(error => {
        console.error('加载对比分析失败:', error)
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.initComparisonChart()
        this.initDifferenceChart()
        this.initTrendChart()
      })
    },
    initComparisonChart() {
      this.comparisonChart = echarts.init(this.$refs.comparisonChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['当前金额', '对比金额']
        },
        xAxis: {
          type: 'category',
          data: this.comparisonData.map(item => item.name),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [
          {
            name: '当前金额',
            type: 'bar',
            data: this.comparisonData.map(item => item.currentAmount),
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '对比金额',
            type: 'bar',
            data: this.comparisonData.map(item => item.previousAmount),
            itemStyle: {
              color: '#E6A23C'
            }
          }
        ]
      }
      this.comparisonChart.setOption(option)
    },
    initDifferenceChart() {
      this.differenceChart = echarts.init(this.$refs.differenceChart)
      const positiveData = this.comparisonData.filter(item => item.difference > 0)
      const negativeData = this.comparisonData.filter(item => item.difference < 0)
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          data: ['增加', '减少']
        },
        series: [{
          name: '差异分布',
          type: 'pie',
          radius: '50%',
          data: [
            { value: positiveData.length, name: '增加' },
            { value: negativeData.length, name: '减少' }
          ],
          itemStyle: {
            color: function(params) {
              return params.name === '增加' ? '#67C23A' : '#F56C6C'
            }
          }
        }]
      }
      this.differenceChart.setOption(option)
    },
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.comparisonData.map(item => item.name),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '变化率(%)'
        },
        series: [{
          name: '变化率',
          type: 'line',
          data: this.comparisonData.map(item => item.changeRate),
          itemStyle: {
            color: function(params) {
              return params.value >= 0 ? '#67C23A' : '#F56C6C'
            }
          },
          lineStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.trendChart.setOption(option)
    },
    handleTypeChange(value) {
      console.log('对比类型变化:', value)
      this.loadData()
    },
    handleDateChange(value) {
      console.log('日期范围变化:', value)
      this.loadData()
    },
    getTrendIcon(trend) {
      return trend === '上升' ? 'el-icon-top' : 'el-icon-bottom'
    },
    getTrendColor(trend) {
      return trend === '上升' ? '#67C23A' : '#F56C6C'
    },
    getRankingType(ranking) {
      if (ranking <= 3) return 'success'
      if (ranking <= 6) return 'warning'
      return 'info'
    },
    viewDetail(row) {
      this.$message.info(`查看${row.name}的详细对比数据`)
    },
    exportData() {
      expenseAnalysisApi.exportReport({ type: 'comparison' }).then(res => {
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
.comparison-analysis {
  padding: 20px;
}

.filter-bar {
  margin-bottom: 20px;
}

.filter-bar .el-select,
.filter-bar .el-date-editor,
.filter-bar .el-button {
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
