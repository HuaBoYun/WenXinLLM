<template>
  <div class="personal-analysis">
    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="分析时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="分析类型">
          <el-select v-model="searchForm.analysisType" style="width: 150px">
            <el-option label="费用类型" value="EXPENSE_TYPE" />
            <el-option label="时间趋势" value="TIME_TREND" />
            <el-option label="预算对比" value="BUDGET_COMPARE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="handleExport">导出报告</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 基础统计卡片 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon">
                <i class="el-icon-money" style="color: #409eff;"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">¥{{ formatAmount(analysisData.basicStats?.totalExpense || 0) }}</div>
                <div class="stat-label">总费用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon">
                <i class="el-icon-document" style="color: #67c23a;"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ analysisData.basicStats?.totalReports || 0 }}</div>
                <div class="stat-label">报销单数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon">
                <i class="el-icon-s-data" style="color: #e6a23c;"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">¥{{ formatAmount(analysisData.basicStats?.averageAmount || 0) }}</div>
                <div class="stat-label">平均金额</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon">
                <i class="el-icon-warning" style="color: #f56c6c;"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">¥{{ formatAmount(analysisData.basicStats?.pendingAmount || 0) }}</div>
                <div class="stat-label">待审批金额</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析区域 -->
    <div class="charts-container">
      <el-row :gutter="20">
        <!-- 费用类型分析 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>费用类型分析</span>
            </div>
            <div id="expenseTypeChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        
        <!-- 月度趋势分析 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>月度趋势分析</span>
            </div>
            <div id="monthlyTrendChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 预算执行情况 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>预算执行情况</span>
            </div>
            <div class="budget-execution">
              <div class="budget-progress">
                <el-progress 
                  :percentage="analysisData.budgetExecution?.usagePercentage || 0" 
                  :color="getBudgetColor(analysisData.budgetExecution?.usagePercentage || 0)"
                  :stroke-width="20"
                  text-inside
                />
              </div>
              <div class="budget-details">
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="budget-item">
                      <div class="budget-label">预算总额</div>
                      <div class="budget-value">¥{{ formatAmount(analysisData.budgetExecution?.budgetAmount || 0) }}</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="budget-item">
                      <div class="budget-label">已使用</div>
                      <div class="budget-value used">¥{{ formatAmount(analysisData.budgetExecution?.usedAmount || 0) }}</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="budget-item">
                      <div class="budget-label">剩余预算</div>
                      <div class="budget-value remaining">¥{{ formatAmount(analysisData.budgetExecution?.remainingAmount || 0) }}</div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 部门对比 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>部门对比</span>
            </div>
            <div class="department-comparison">
              <div class="comparison-item">
                <span class="comparison-label">我的费用</span>
                <span class="comparison-value">¥{{ formatAmount(analysisData.departmentComparison?.myExpense || 0) }}</span>
              </div>
              <div class="comparison-item">
                <span class="comparison-label">部门平均</span>
                <span class="comparison-value">¥{{ formatAmount(analysisData.departmentComparison?.departmentAverage || 0) }}</span>
              </div>
              <div class="comparison-item">
                <span class="comparison-label">公司平均</span>
                <span class="comparison-value">¥{{ formatAmount(analysisData.departmentComparison?.companyAverage || 0) }}</span>
              </div>
              <div class="comparison-item">
                <span class="comparison-label">部门排名</span>
                <span class="comparison-value">{{ analysisData.departmentComparison?.ranking || 0 }}/{{ analysisData.departmentComparison?.totalMembers || 0 }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细数据表格 -->
    <div class="table-container">
      <el-card class="table-card">
        <div slot="header" class="card-header">
          <span>费用类型详情</span>
        </div>
        <el-table :data="analysisData.expenseTypeAnalysis || []" border>
          <el-table-column prop="expenseType" label="费用类型" width="150" />
          <el-table-column prop="amount" label="金额" width="120">
            <template slot-scope="scope">
              <span class="amount">¥{{ formatAmount(scope.row.amount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="percentage" label="占比" width="100">
            <template slot-scope="scope">
              <span>{{ scope.row.percentage }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="count" label="单据数量" width="100" />
          <el-table-column label="占比图示" min-width="200">
            <template slot-scope="scope">
              <el-progress 
                :percentage="scope.row.percentage" 
                :show-text="false"
                :stroke-width="12"
                :color="getProgressColor(scope.$index)"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { expenseAnalysisApi } from '@/api/financialSharing/advancedFeatures'
import * as echarts from 'echarts'

export default {
  name: 'PersonalAnalysis',
  data() {
    return {
      loading: false,
      searchForm: {
        dateRange: [],
        analysisType: 'EXPENSE_TYPE'
      },
      analysisData: {},
      expenseTypeChart: null,
      monthlyTrendChart: null
    }
  },
  mounted() {
    this.loadData()
  },
  beforeDestroy() {
    if (this.expenseTypeChart) {
      this.expenseTypeChart.dispose()
    }
    if (this.monthlyTrendChart) {
      this.monthlyTrendChart.dispose()
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        
        const response = await expenseAnalysisApi.getPersonalAnalysis(params)
        if (response.code === 1) {
          this.analysisData = response.data
          this.$nextTick(() => {
            this.initCharts()
          })
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    initCharts() {
      this.initExpenseTypeChart()
      this.initMonthlyTrendChart()
    },

    initExpenseTypeChart() {
      const chartDom = document.getElementById('expenseTypeChart')
      if (!chartDom) return
      
      this.expenseTypeChart = echarts.init(chartDom)
      
      const data = this.analysisData.expenseTypeAnalysis || []
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: ¥{c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '费用类型',
            type: 'pie',
            radius: '50%',
            data: data.map(item => ({
              value: item.amount,
              name: item.expenseType
            })),
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
      
      this.expenseTypeChart.setOption(option)
    },

    initMonthlyTrendChart() {
      const chartDom = document.getElementById('monthlyTrendChart')
      if (!chartDom) return
      
      this.monthlyTrendChart = echarts.init(chartDom)
      
      const data = this.analysisData.monthlyTrend || []
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.month)
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '费用金额',
            data: data.map(item => item.amount),
            type: 'line',
            smooth: true,
            itemStyle: {
              color: '#409eff'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(64, 158, 255, 0.3)'
                }, {
                  offset: 1, color: 'rgba(64, 158, 255, 0.1)'
                }]
              }
            }
          }
        ]
      }
      
      this.monthlyTrendChart.setOption(option)
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    getBudgetColor(percentage) {
      if (percentage >= 90) return '#f56c6c'
      if (percentage >= 70) return '#e6a23c'
      return '#67c23a'
    },

    getProgressColor(index) {
      const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399']
      return colors[index % colors.length]
    },

    handleExport() {
      const params = {
        analysisType: 'personal',
        ...this.searchForm
      }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      
      expenseAnalysisApi.exportReport(params).then(response => {
        if (response.code === 1) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.personal-analysis {
  padding: 0;
}

.search-container {
  background: #f5f7fa;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.stats-container {
  margin-bottom: 20px;
}

.stat-card {
  height: 100px;
}

.stat-item {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.stat-icon {
  font-size: 32px;
  margin-right: 15px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.charts-container {
  margin-bottom: 20px;
}

.chart-card {
  height: 380px;
}

.card-header {
  font-weight: bold;
  color: #303133;
}

.budget-execution {
  padding: 20px 0;
}

.budget-progress {
  margin-bottom: 30px;
}

.budget-details {
  margin-top: 20px;
}

.budget-item {
  text-align: center;
}

.budget-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.budget-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.budget-value.used {
  color: #e6a23c;
}

.budget-value.remaining {
  color: #67c23a;
}

.department-comparison {
  padding: 20px;
}

.comparison-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.comparison-item:last-child {
  border-bottom: none;
}

.comparison-label {
  font-size: 14px;
  color: #606266;
}

.comparison-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.table-container {
  margin-top: 20px;
}

.table-card {
  min-height: 300px;
}

.amount {
  font-weight: bold;
  color: #e6a23c;
}
</style>
