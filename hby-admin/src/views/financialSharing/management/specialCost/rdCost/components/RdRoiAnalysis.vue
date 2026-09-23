<template>
  <el-dialog
    title="研发投资回报分析"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="roi-analysis-container">
      <!-- 分析概览 -->
      <el-row :gutter="20" class="overview-row">
        <el-col :span="6">
          <div class="stats-card total-investment">
            <div class="stats-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">总投资</div>
              <div class="stats-value">{{ formatAmount(analysisData.totalInvestment) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card expected-return">
            <div class="stats-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">预期回报</div>
              <div class="stats-value">{{ formatAmount(analysisData.expectedReturn) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card net-return">
            <div class="stats-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">净回报</div>
              <div class="stats-value" :class="getNetReturnClass()">
                {{ formatAmount(analysisData.netReturn) }}
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card avg-roi">
            <div class="stats-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">平均ROI</div>
              <div class="stats-value" :class="getAvgRoiClass()">
                {{ analysisData.avgRoi }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表分析 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>研发类型投资分布</span>
            </div>
            <div id="rdTypeInvestmentChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>投资回报率对比</span>
            </div>
            <div id="roiCompareChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- ROI分析表格 -->
      <el-card shadow="never" class="table-card">
        <div slot="header" class="card-header">
          <span>分类投资回报分析</span>
        </div>
        <el-table
          :data="roiDetails"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="rdType"
            label="研发类型"
            width="150"
          >
            <template slot-scope="scope">
              <el-tag :type="getRdTypeTag(scope.row.rdType)">
                {{ getRdTypeName(scope.row.rdType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="projectCount"
            label="项目数量"
            width="100"
            align="right"
          />
          <el-table-column
            prop="totalInvestment"
            label="总投资"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.totalInvestment) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="expectedReturn"
            label="预期回报"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.expectedReturn) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="netReturn"
            label="净回报"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getNetReturnClass(scope.row.netReturn)">
                {{ formatAmount(scope.row.netReturn) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="avgRoi"
            label="平均ROI"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getAvgRoiClass(scope.row.avgRoi)">
                {{ scope.row.avgRoi }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="riskLevel"
            label="风险等级"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
                {{ getRiskLevelText(scope.row.riskLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="recommendation"
            label="投资建议"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 分析结论 -->
      <el-card shadow="never" class="conclusion-card">
        <div slot="header" class="card-header">
          <span>投资回报分析结论</span>
        </div>
        <div class="conclusion-content">
          <div class="conclusion-section">
            <h4>整体评估</h4>
            <p>{{ analysisData.overallAssessment }}</p>
          </div>
          <div class="conclusion-section">
            <h4>关键发现</h4>
            <ul>
              <li v-for="finding in analysisData.keyFindings" :key="finding">
                {{ finding }}
              </li>
            </ul>
          </div>
          <div class="conclusion-section">
            <h4>投资建议</h4>
            <ul>
              <li v-for="recommendation in analysisData.recommendations" :key="recommendation">
                {{ recommendation }}
              </li>
            </ul>
          </div>
          <div class="conclusion-section">
            <h4>风险提示</h4>
            <ul>
              <li v-for="risk in analysisData.riskWarnings" :key="risk">
                {{ risk }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getRdRoiAnalysis } from '@/api/financialSharing/specialCost'
import * as echarts from 'echarts'

export default {
  name: 'RdRoiAnalysis',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      analysisData: {
        totalInvestment: 0,
        expectedReturn: 0,
        netReturn: 0,
        avgRoi: '0.00',
        overallAssessment: '',
        keyFindings: [],
        recommendations: [],
        riskWarnings: []
      },
      roiDetails: [],
      typeChart: null,
      roiChart: null
    }
  },
  methods: {
    async showAnalysis() {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getRdRoiAnalysis()
        if (code === 200) {
          this.analysisData = data.overview
          this.roiDetails = data.details
          
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        this.$baseMessage('获取ROI分析数据失败', 'error')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initTypeChart()
      this.initRoiChart()
    },
    initTypeChart() {
      const chartDom = document.getElementById('rdTypeInvestmentChart')
      this.typeChart = echarts.init(chartDom)
      
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
            name: '研发类型投资',
            type: 'pie',
            radius: '50%',
            data: this.roiDetails.map(item => ({
              value: item.totalInvestment / 10000,
              name: this.getRdTypeName(item.rdType)
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
      
      this.typeChart.setOption(option)
    },
    initRoiChart() {
      const chartDom = document.getElementById('roiCompareChart')
      this.roiChart = echarts.init(chartDom)
      
      const categories = this.roiDetails.map(item => 
        this.getRdTypeName(item.rdType)
      )
      const investmentData = this.roiDetails.map(item => item.totalInvestment / 10000)
      const returnData = this.roiDetails.map(item => item.expectedReturn / 10000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['投资金额', '预期回报']
        },
        xAxis: {
          type: 'category',
          data: categories
        },
        yAxis: {
          type: 'value',
          name: '金额(万)'
        },
        series: [
          {
            name: '投资金额',
            type: 'bar',
            data: investmentData,
            itemStyle: {
              color: '#f56c6c'
            }
          },
          {
            name: '预期回报',
            type: 'bar',
            data: returnData,
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }
      
      this.roiChart.setOption(option)
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'projectCount') {
          const values = data.map(item => Number(item[column.property]))
          sums[index] = values.reduce((prev, curr) => prev + curr, 0)
        } else if (['totalInvestment', 'expectedReturn', 'netReturn'].includes(column.property)) {
          const values = data.map(item => Number(item[column.property]))
          sums[index] = this.formatAmount(values.reduce((prev, curr) => prev + curr, 0))
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handleExport() {
      try {
        const data = this.roiDetails || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const exportData = { overview: this.analysisData, details: data }
        const jsonStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '研发ROI分析数据.json'
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
    getRdTypeName(type) {
      const typeMap = {
        'basic_research': '基础研究',
        'applied_research': '应用研究',
        'experimental_development': '试验发展',
        'technology_improvement': '技术改进'
      }
      return typeMap[type] || '未知'
    },
    getRdTypeTag(type) {
      const tagMap = {
        'basic_research': 'primary',
        'applied_research': 'success',
        'experimental_development': 'warning',
        'technology_improvement': 'info'
      }
      return tagMap[type] || 'info'
    },
    getNetReturnClass(netReturn) {
      const returnValue = netReturn || this.analysisData.netReturn
      if (returnValue > 0) return 'text-success'
      if (returnValue === 0) return 'text-warning'
      return 'text-danger'
    },
    getAvgRoiClass(avgRoi) {
      const roiNum = parseFloat(avgRoi || this.analysisData.avgRoi)
      if (roiNum >= 20) return 'text-success'
      if (roiNum >= 10) return 'text-warning'
      return 'text-danger'
    },
    getRiskLevelTag(level) {
      const tagMap = {
        'low': 'success',
        'medium': 'warning',
        'high': 'danger'
      }
      return tagMap[level] || 'info'
    },
    getRiskLevelText(level) {
      const textMap = {
        'low': '低风险',
        'medium': '中风险',
        'high': '高风险'
      }
      return textMap[level] || '未知'
    }
  },
  beforeDestroy() {
    if (this.typeChart) {
      this.typeChart.dispose()
    }
    if (this.roiChart) {
      this.roiChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.roi-analysis-container {
  .overview-row {
    margin-bottom: 20px;
  }
  
  .stats-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: white;
    
    &.total-investment {
      background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    }
    
    &.expected-return {
      background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    }
    
    &.net-return {
      background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    }
    
    &.avg-roi {
      background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
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
    .conclusion-section {
      margin-bottom: 20px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      h4 {
        color: #303133;
        margin-bottom: 10px;
      }
      
      p {
        line-height: 1.6;
        color: #606266;
      }
      
      ul {
        margin: 0;
        padding-left: 20px;
        
        li {
          line-height: 1.6;
          color: #606266;
          margin-bottom: 5px;
        }
      }
    }
  }
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}
</style>
