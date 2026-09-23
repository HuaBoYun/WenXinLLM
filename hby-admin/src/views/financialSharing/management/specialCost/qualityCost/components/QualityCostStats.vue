<template>
  <el-dialog
    title="质量成本分类统计"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="stats-container">
      <!-- 统计概览 -->
      <el-row :gutter="20" class="overview-row">
        <el-col :span="6">
          <div class="stats-card prevention">
            <div class="stats-icon">
              <i class="el-icon-shield"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">预防成本</div>
              <div class="stats-value">{{ formatAmount(statsData.preventionCost) }}</div>
              <div class="stats-percent">{{ statsData.preventionPercent }}%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card appraisal">
            <div class="stats-icon">
              <i class="el-icon-search"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">鉴定成本</div>
              <div class="stats-value">{{ formatAmount(statsData.appraisalCost) }}</div>
              <div class="stats-percent">{{ statsData.appraisalPercent }}%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card internal">
            <div class="stats-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">内部失败成本</div>
              <div class="stats-value">{{ formatAmount(statsData.internalFailureCost) }}</div>
              <div class="stats-percent">{{ statsData.internalFailurePercent }}%</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card external">
            <div class="stats-icon">
              <i class="el-icon-error"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">外部失败成本</div>
              <div class="stats-value">{{ formatAmount(statsData.externalFailureCost) }}</div>
              <div class="stats-percent">{{ statsData.externalFailurePercent }}%</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表分析 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>质量成本分布</span>
            </div>
            <div id="qualityCostPieChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>质量成本趋势</span>
            </div>
            <div id="qualityCostTrendChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细统计表格 -->
      <el-card shadow="never" class="table-card">
        <div slot="header" class="card-header">
          <span>分类明细统计</span>
        </div>
        <el-table
          :data="detailStats"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="qualityType"
            label="质量成本类型"
            width="150"
          >
            <template slot-scope="scope">
              <el-tag :type="getQualityTypeTag(scope.row.qualityType)">
                {{ getQualityTypeName(scope.row.qualityType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="count"
            label="数量"
            width="100"
            align="right"
          />
          <el-table-column
            prop="totalAmount"
            label="总金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.totalAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="avgAmount"
            label="平均金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.avgAmount) }}
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
            prop="remark"
            label="备注"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getQualityCostCategoryStats } from '@/api/financialSharing/specialCost'
import * as echarts from 'echarts'

export default {
  name: 'QualityCostStats',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      statsData: {
        preventionCost: 0,
        preventionPercent: 0,
        appraisalCost: 0,
        appraisalPercent: 0,
        internalFailureCost: 0,
        internalFailurePercent: 0,
        externalFailureCost: 0,
        externalFailurePercent: 0
      },
      detailStats: [],
      trendData: null,
      pieChart: null,
      trendChart: null
    }
  },
  methods: {
    async showStats() {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getQualityCostCategoryStats()
        if (code === 200) {
          this.statsData = data.overview
          this.detailStats = data.details
          this.trendData = data.trendData || null

          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        this.$baseMessage('获取统计数据失败', 'error')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initPieChart()
      this.initTrendChart()
    },
    initPieChart() {
      const chartDom = document.getElementById('qualityCostPieChart')
      this.pieChart = echarts.init(chartDom)
      
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
            name: '质量成本分布',
            type: 'pie',
            radius: '50%',
            data: [
              { value: this.statsData.preventionCost / 10000, name: '预防成本' },
              { value: this.statsData.appraisalCost / 10000, name: '鉴定成本' },
              { value: this.statsData.internalFailureCost / 10000, name: '内部失败成本' },
              { value: this.statsData.externalFailureCost / 10000, name: '外部失败成本' }
            ],
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
      
      this.pieChart.setOption(option)
    },
    initTrendChart() {
      const chartDom = document.getElementById('qualityCostTrendChart')
      this.trendChart = echarts.init(chartDom)

      // 使用从API获取的趋势数据
      const trend = this.trendData || { months: [], prevention: [], appraisal: [], internalFailure: [], externalFailure: [] }
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预防成本', '鉴定成本', '内部失败成本', '外部失败成本']
        },
        xAxis: {
          type: 'category',
          data: trend.months
        },
        yAxis: {
          type: 'value',
          name: '金额(万)'
        },
        series: [
          {
            name: '预防成本',
            type: 'line',
            data: trend.prevention
          },
          {
            name: '鉴定成本',
            type: 'line',
            data: trend.appraisal
          },
          {
            name: '内部失败成本',
            type: 'line',
            data: trend.internalFailure
          },
          {
            name: '外部失败成本',
            type: 'line',
            data: trend.externalFailure
          }
        ]
      }

      this.trendChart.setOption(option)
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'count') {
          const values = data.map(item => Number(item[column.property]))
          sums[index] = values.reduce((prev, curr) => prev + curr, 0)
        } else if (column.property === 'totalAmount' || column.property === 'avgAmount') {
          const values = data.map(item => Number(item[column.property]))
          if (column.property === 'totalAmount') {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => prev + curr, 0))
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
        const data = this.detailStats || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const exportData = { overview: this.statsData, details: data }
        const jsonStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '质量成本统计数据.json'
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
    getQualityTypeName(type) {
      const typeMap = {
        'prevention': '预防成本',
        'appraisal': '鉴定成本',
        'internal_failure': '内部失败成本',
        'external_failure': '外部失败成本'
      }
      return typeMap[type] || '未知'
    },
    getQualityTypeTag(type) {
      const tagMap = {
        'prevention': 'success',
        'appraisal': 'primary',
        'internal_failure': 'warning',
        'external_failure': 'danger'
      }
      return tagMap[type] || 'info'
    }
  },
  beforeDestroy() {
    if (this.pieChart) {
      this.pieChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.stats-container {
  .overview-row {
    margin-bottom: 20px;
  }
  
  .stats-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: white;
    
    &.prevention {
      background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    }
    
    &.appraisal {
      background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    }
    
    &.internal {
      background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
    }
    
    &.external {
      background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    }
    
    .stats-icon {
      font-size: 40px;
      margin-right: 15px;
    }
    
    .stats-content {
      flex: 1;
      
      .stats-title {
        font-size: 14px;
        margin-bottom: 5px;
        opacity: 0.9;
      }
      
      .stats-value {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .stats-percent {
        font-size: 12px;
        opacity: 0.8;
      }
    }
  }
  
  .chart-card,
  .table-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    font-weight: bold;
    color: #303133;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
