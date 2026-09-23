<template>
  <el-dialog
    title="项目成本分析"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="analysis-container">
      <!-- 分析概览 -->
      <el-row :gutter="20" class="overview-row">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon budget">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-title">预算总额</div>
              <div class="card-value">{{ formatAmount(analysisData.budgetAmount) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon actual">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-title">实际成本</div>
              <div class="card-value">{{ formatAmount(analysisData.actualAmount) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon variance" :class="getVarianceType(analysisData.variance)">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="card-content">
              <div class="card-title">预算差异</div>
              <div class="card-value" :class="getVarianceClass(analysisData.variance)">
                {{ formatAmount(analysisData.variance) }}
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon rate">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-content">
              <div class="card-title">执行率</div>
              <div class="card-value">{{ analysisData.executionRate }}%</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表分析 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>成本构成分析</span>
            </div>
            <div id="costStructureChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>预算执行趋势</span>
            </div>
            <div id="budgetTrendChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细分析表格 -->
      <el-card shadow="never" class="table-card">
        <div slot="header" class="card-header">
          <span>成本明细分析</span>
        </div>
        <el-table
          :data="costDetailList"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="costType"
            label="成本类型"
            width="120"
          />
          <el-table-column
            prop="budgetAmount"
            label="预算金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.budgetAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="actualAmount"
            label="实际成本"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.actualAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="variance"
            label="差异金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.variance)">
                {{ formatAmount(scope.row.variance) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="varianceRate"
            label="差异率"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.variance)">
                {{ scope.row.varianceRate }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="percentage"
            label="占比"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              {{ scope.row.percentage }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag :type="getCostStatusTag(scope.row.status)">
                {{ getCostStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 分析结论 -->
      <el-card shadow="never" class="conclusion-card">
        <div slot="header" class="card-header">
          <span>分析结论</span>
        </div>
        <div class="conclusion-content">
          <el-alert
            :title="analysisData.conclusion"
            :type="getConclusionType(analysisData.variance)"
            :closable="false"
            show-icon
          />
          <div class="suggestions" v-if="analysisData.suggestions && analysisData.suggestions.length > 0">
            <h4>改进建议：</h4>
            <ul>
              <li v-for="(suggestion, index) in analysisData.suggestions" :key="index">
                {{ suggestion }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getProjectCostAnalysis } from '@/api/financialSharing/specialCost'
import * as echarts from 'echarts'

export default {
  name: 'ProjectCostAnalysis',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      analysisData: {},
      costDetailList: [],
      costStructureChart: null,
      budgetTrendChart: null
    }
  },
  methods: {
    async showAnalysis(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getProjectCostAnalysis({ projectId: row.id })
        if (code === 200) {
          this.analysisData = data
          this.costDetailList = data.costDetailList || []
          
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        this.$baseMessage('获取分析数据失败', 'error')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initCostStructureChart()
      this.initBudgetTrendChart()
    },
    initCostStructureChart() {
      const chartDom = document.getElementById('costStructureChart')
      this.costStructureChart = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '成本构成',
            type: 'pie',
            radius: '50%',
            data: this.costDetailList.map(item => ({
              value: item.actualAmount / 10000,
              name: item.costType
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
      
      this.costStructureChart.setOption(option)
    },
    initBudgetTrendChart() {
      const chartDom = document.getElementById('budgetTrendChart')
      this.budgetTrendChart = echarts.init(chartDom)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预算金额', '实际成本']
        },
        xAxis: {
          type: 'category',
          data: this.costDetailList.map(item => item.costType)
        },
        yAxis: {
          type: 'value',
          name: '金额(万)'
        },
        series: [
          {
            name: '预算金额',
            type: 'bar',
            data: this.costDetailList.map(item => (item.budgetAmount / 10000).toFixed(2))
          },
          {
            name: '实际成本',
            type: 'bar',
            data: this.costDetailList.map(item => (item.actualAmount / 10000).toFixed(2))
          }
        ]
      }
      
      this.budgetTrendChart.setOption(option)
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          if (column.property === 'budgetAmount' || column.property === 'actualAmount' || column.property === 'variance') {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = ''
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handleExport() {
      try {
        const data = this.costDetailList || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const exportData = { analysis: this.analysisData, details: data }
        const jsonStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '项目成本分析数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getVarianceClass(variance) {
      if (variance > 0) return 'text-danger'
      if (variance < 0) return 'text-success'
      return ''
    },
    getVarianceType(variance) {
      if (variance > 0) return 'danger'
      if (variance < 0) return 'success'
      return 'info'
    },
    getCostStatusName(status) {
      const statusMap = {
        '1': '正常',
        '2': '超预算',
        '3': '节约'
      }
      return statusMap[status] || '未知'
    },
    getCostStatusTag(status) {
      const tagMap = {
        '1': 'success',
        '2': 'danger',
        '3': 'primary'
      }
      return tagMap[status] || 'info'
    },
    getConclusionType(variance) {
      if (variance > 0) return 'warning'
      if (variance < 0) return 'success'
      return 'info'
    }
  },
  beforeDestroy() {
    if (this.costStructureChart) {
      this.costStructureChart.dispose()
    }
    if (this.budgetTrendChart) {
      this.budgetTrendChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.analysis-container {
  .overview-row {
    margin-bottom: 20px;
  }
  
  .overview-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: #fff;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    
    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      
      i {
        font-size: 24px;
        color: #fff;
      }
      
      &.budget {
        background: #409eff;
      }
      
      &.actual {
        background: #67c23a;
      }
      
      &.variance {
        &.success {
          background: #67c23a;
        }
        &.danger {
          background: #f56c6c;
        }
        &.info {
          background: #909399;
        }
      }
      
      &.rate {
        background: #e6a23c;
      }
    }
    
    .card-content {
      flex: 1;
      
      .card-title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 5px;
      }
      
      .card-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
      }
    }
  }
  
  .chart-card,
  .table-card,
  .conclusion-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    font-weight: bold;
    color: #303133;
  }
  
  .conclusion-content {
    .suggestions {
      margin-top: 20px;
      
      h4 {
        margin-bottom: 10px;
        color: #303133;
      }
      
      ul {
        margin: 0;
        padding-left: 20px;
        
        li {
          margin-bottom: 5px;
          color: #606266;
        }
      }
    }
  }
}

.text-danger {
  color: #f56c6c;
}

.text-success {
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
