<template>
  <el-dialog
    title="环境效益评估"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="assessment-container">
      <!-- 评估概览 -->
      <el-row :gutter="20" class="overview-row">
        <el-col :span="6">
          <div class="stats-card total-cost">
            <div class="stats-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">总投入成本</div>
              <div class="stats-value">{{ formatAmount(assessmentData.totalCost) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card total-benefit">
            <div class="stats-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">总环境效益</div>
              <div class="stats-value">{{ formatAmount(assessmentData.totalBenefit) }}</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card net-benefit">
            <div class="stats-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">净效益</div>
              <div class="stats-value" :class="getNetBenefitClass()">
                {{ formatAmount(assessmentData.netBenefit) }}
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card benefit-ratio">
            <div class="stats-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="stats-content">
              <div class="stats-title">效益比</div>
              <div class="stats-value" :class="getBenefitRatioClass()">
                {{ assessmentData.benefitRatio }}
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
              <span>环境成本类型分布</span>
            </div>
            <div id="environmentCostTypeChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <span>投入产出对比</span>
            </div>
            <div id="costBenefitCompareChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 效益评估表格 -->
      <el-card shadow="never" class="table-card">
        <div slot="header" class="card-header">
          <span>分类效益评估</span>
        </div>
        <el-table
          :data="assessmentDetails"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="environmentType"
            label="环境成本类型"
            width="150"
          >
            <template slot-scope="scope">
              <el-tag :type="getEnvironmentTypeTag(scope.row.environmentType)">
                {{ getEnvironmentTypeName(scope.row.environmentType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="count"
            label="项目数量"
            width="100"
            align="right"
          />
          <el-table-column
            prop="totalCost"
            label="总成本"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.totalCost) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="totalBenefit"
            label="总效益"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.totalBenefit) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="netBenefit"
            label="净效益"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getNetBenefitClass(scope.row.netBenefit)">
                {{ formatAmount(scope.row.netBenefit) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="benefitRatio"
            label="效益比"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getBenefitRatioClass(scope.row.benefitRatio)">
                {{ scope.row.benefitRatio }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="assessment"
            label="效益评估"
            width="120"
          >
            <template slot-scope="scope">
              <el-tag :type="getAssessmentTag(scope.row.benefitRatio)">
                {{ getAssessmentText(scope.row.benefitRatio) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="suggestion"
            label="改进建议"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 评估结论 -->
      <el-card shadow="never" class="conclusion-card">
        <div slot="header" class="card-header">
          <span>评估结论与建议</span>
        </div>
        <div class="conclusion-content">
          <div class="conclusion-section">
            <h4>总体评估</h4>
            <p>{{ assessmentData.overallAssessment }}</p>
          </div>
          <div class="conclusion-section">
            <h4>主要发现</h4>
            <ul>
              <li v-for="finding in assessmentData.keyFindings" :key="finding">
                {{ finding }}
              </li>
            </ul>
          </div>
          <div class="conclusion-section">
            <h4>改进建议</h4>
            <ul>
              <li v-for="suggestion in assessmentData.suggestions" :key="suggestion">
                {{ suggestion }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出评估报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getEnvironmentBenefitAssessment } from '@/api/financialSharing/specialCost'
import * as echarts from 'echarts'

export default {
  name: 'EnvironmentBenefitAssessment',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      assessmentData: {
        totalCost: 0,
        totalBenefit: 0,
        netBenefit: 0,
        benefitRatio: '0.00',
        overallAssessment: '',
        keyFindings: [],
        suggestions: []
      },
      assessmentDetails: [],
      typeChart: null,
      compareChart: null
    }
  },
  methods: {
    async showAssessment() {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getEnvironmentBenefitAssessment()
        if (code === 200) {
          this.assessmentData = data.overview
          this.assessmentDetails = data.details
          
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        this.$baseMessage('获取效益评估数据失败', 'error')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initTypeChart()
      this.initCompareChart()
    },
    initTypeChart() {
      const chartDom = document.getElementById('environmentCostTypeChart')
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
            name: '环境成本类型',
            type: 'pie',
            radius: '50%',
            data: this.assessmentDetails.map(item => ({
              value: item.totalCost / 10000,
              name: this.getEnvironmentTypeName(item.environmentType)
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
    initCompareChart() {
      const chartDom = document.getElementById('costBenefitCompareChart')
      this.compareChart = echarts.init(chartDom)
      
      const categories = this.assessmentDetails.map(item => 
        this.getEnvironmentTypeName(item.environmentType)
      )
      const costData = this.assessmentDetails.map(item => item.totalCost / 10000)
      const benefitData = this.assessmentDetails.map(item => item.totalBenefit / 10000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['投入成本', '环境效益']
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
            name: '投入成本',
            type: 'bar',
            data: costData,
            itemStyle: {
              color: '#f56c6c'
            }
          },
          {
            name: '环境效益',
            type: 'bar',
            data: benefitData,
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }
      
      this.compareChart.setOption(option)
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
        } else if (['totalCost', 'totalBenefit', 'netBenefit'].includes(column.property)) {
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
        const data = this.assessmentDetails || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const exportData = { overview: this.assessmentData, details: data }
        const jsonStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '环境效益评估数据.json'
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
    getEnvironmentTypeName(type) {
      const typeMap = {
        'protection': '环境保护成本',
        'treatment': '环境治理成本',
        'monitoring': '环境监测成本',
        'loss': '环境损失成本'
      }
      return typeMap[type] || '未知'
    },
    getEnvironmentTypeTag(type) {
      const tagMap = {
        'protection': 'success',
        'treatment': 'primary',
        'monitoring': 'warning',
        'loss': 'danger'
      }
      return tagMap[type] || 'info'
    },
    getNetBenefitClass(netBenefit) {
      const benefit = netBenefit || this.assessmentData.netBenefit
      if (benefit > 0) return 'text-success'
      if (benefit === 0) return 'text-warning'
      return 'text-danger'
    },
    getBenefitRatioClass(ratio) {
      const ratioNum = parseFloat(ratio || this.assessmentData.benefitRatio)
      if (ratioNum >= 1) return 'text-success'
      if (ratioNum >= 0.5) return 'text-warning'
      return 'text-danger'
    },
    getAssessmentTag(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 1.5) return 'success'
      if (ratioNum >= 1) return 'primary'
      if (ratioNum >= 0.5) return 'warning'
      return 'danger'
    },
    getAssessmentText(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 1.5) return '优秀'
      if (ratioNum >= 1) return '良好'
      if (ratioNum >= 0.5) return '一般'
      return '较差'
    }
  },
  beforeDestroy() {
    if (this.typeChart) {
      this.typeChart.dispose()
    }
    if (this.compareChart) {
      this.compareChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment-container {
  .overview-row {
    margin-bottom: 20px;
  }
  
  .stats-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: white;
    
    &.total-cost {
      background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
    }
    
    &.total-benefit {
      background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    }
    
    &.net-benefit {
      background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
    }
    
    &.benefit-ratio {
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
