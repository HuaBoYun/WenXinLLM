<template>
  <div class="risk-assessment-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-analysis"></i>
            风险评估管理
          </h2>
          <p class="page-description">风险评估、风险分析和风险等级管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增评估
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险评估概览卡片 -->
    <div class="assessment-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">评估总数</div>
                <div class="card-value">{{ totalAssessments }}</div>
                <div class="card-change">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险项目</div>
                <div class="card-value">{{ highRiskCount }}</div>
                <div class="card-change negative">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待评估</div>
                <div class="card-value">{{ pendingAssessments }}</div>
                <div class="card-change warning">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon average-score-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均风险评分</div>
                <div class="card-value">{{ averageRiskScore }}</div>
                <div class="card-change">分</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 风险分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险等级分布</h3>
          </div>
          <div id="riskLevelChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险类别分布</h3>
          </div>
          <div id="riskCategoryChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="评估编号">
            <el-input
              v-model="listQuery.assessmentNo"
              placeholder="请输入评估编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="评估名称">
            <el-input
              v-model="listQuery.assessmentName"
              placeholder="请输入评估名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
              <el-option label="极高风险" value="CRITICAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估状态">
            <el-select
              v-model="listQuery.assessmentStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待评估" value="PENDING" />
              <el-option label="评估中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已审核" value="REVIEWED" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估日期">
            <el-date-picker
              v-model="listQuery.assessmentDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 风险评估表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="assessmentList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="评估ID" prop="assessmentId" width="80" align="center" />
        <el-table-column label="评估编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.assessmentNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估名称" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assessmentName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskTypeTagType(row.riskCategory)" size="mini">
              {{ getRiskCategoryText(row.riskCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="业务单元" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.businessUnit }}</span>
          </template>
        </el-table-column>
        <el-table-column label="资产价值" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="asset-value">{{ formatCurrency(row.assetValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险敞口" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="risk-exposure">{{ formatCurrency(row.riskExposure) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险评分" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getRiskScoreClass(row.riskScore)">{{ row.riskScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskLevelTagType(row.riskLevel)" size="mini">
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评估状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAssessmentStatusTagType(row.assessmentStatus)" size="mini">
              {{ getAssessmentStatusText(row.assessmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评估日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assessmentDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下次评估" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getNextAssessmentClass(row.nextAssessmentDate)">{{ row.nextAssessmentDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.assessmentStatus === 'PENDING'" type="primary" size="mini" @click="handleStartAssessment(row)">
              开始评估
            </el-button>
            <el-button v-if="row.assessmentStatus === 'COMPLETED'" type="success" size="mini" @click="handleReview(row)">
              审核
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: row}">生成报告</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">评估历史</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'RiskAssessmentManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        assessmentNo: undefined,
        assessmentName: undefined,
        riskLevel: undefined,
        assessmentStatus: undefined,
        assessmentDateRange: undefined
      },
      totalAssessments: 45,
      highRiskCount: 8,
      pendingAssessments: 12,
      averageRiskScore: 6.8,
      assessmentList: [],
      multipleSelection: [],
      riskLevelChart: null,
      riskCategoryChart: null
    }
  },
  mounted() {
    this.getList()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.riskLevelChart) {
      this.riskLevelChart.dispose()
    }
    if (this.riskCategoryChart) {
      this.riskCategoryChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.assessmentList = [
          {
            assessmentId: 1,
            assessmentNo: 'RA20240925001',
            assessmentName: '银行理财产品风险评估',
            riskCategory: 'MARKET',
            businessUnit: '投资部',
            assetValue: 50000000.00,
            riskExposure: 2500000.00,
            riskScore: 7.2,
            riskLevel: 'HIGH',
            assessmentStatus: 'COMPLETED',
            assessmentDate: '2024-09-25',
            nextAssessmentDate: '2024-12-25'
          },
          {
            assessmentId: 2,
            assessmentNo: 'RA20240920002',
            assessmentName: '债券投资信用风险评估',
            riskCategory: 'CREDIT',
            businessUnit: '债券部',
            assetValue: 100000000.00,
            riskExposure: 5000000.00,
            riskScore: 5.8,
            riskLevel: 'MEDIUM',
            assessmentStatus: 'REVIEWED',
            assessmentDate: '2024-09-20',
            nextAssessmentDate: '2024-12-20'
          },
          {
            assessmentId: 3,
            assessmentNo: 'RA20240915003',
            assessmentName: '流动性风险评估',
            riskCategory: 'LIQUIDITY',
            businessUnit: '资金部',
            assetValue: 200000000.00,
            riskExposure: 15000000.00,
            riskScore: 8.5,
            riskLevel: 'CRITICAL',
            assessmentStatus: 'PENDING',
            assessmentDate: '2024-09-15',
            nextAssessmentDate: '2024-10-15'
          }
        ]
        this.total = this.assessmentList.length
        this.listLoading = false
      }, 1000)
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化风险等级图表
      this.riskLevelChart = echarts.init(document.getElementById('riskLevelChart'))
      this.updateRiskLevelChart()
      
      // 初始化风险类别图表
      this.riskCategoryChart = echarts.init(document.getElementById('riskCategoryChart'))
      this.updateRiskCategoryChart()
    },
    updateRiskLevelChart() {
      const data = [
        { name: '低风险', value: 15, itemStyle: { color: '#67C23A' } },
        { name: '中风险', value: 22, itemStyle: { color: '#E6A23C' } },
        { name: '高风险', value: 6, itemStyle: { color: '#F56C6C' } },
        { name: '极高风险', value: 2, itemStyle: { color: '#909399' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}项 ({d}%)'
        },
        series: [
          {
            name: '风险等级',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.riskLevelChart.setOption(option)
    },
    updateRiskCategoryChart() {
      const data = [
        { name: '市场风险', value: 18, itemStyle: { color: '#409EFF' } },
        { name: '信用风险', value: 12, itemStyle: { color: '#67C23A' } },
        { name: '流动性风险', value: 8, itemStyle: { color: '#E6A23C' } },
        { name: '操作风险', value: 5, itemStyle: { color: '#F56C6C' } },
        { name: '合规风险', value: 2, itemStyle: { color: '#909399' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}项 ({d}%)'
        },
        series: [
          {
            name: '风险类别',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.riskCategoryChart.setOption(option)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        assessmentNo: undefined,
        assessmentName: undefined,
        riskLevel: undefined,
        assessmentStatus: undefined,
        assessmentDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.$message({
        type: 'info',
        message: '新增风险评估功能'
      })
    },
    handleViewDetail(row) {
      this.$message({
        type: 'info',
        message: `查看评估详情: ${row.assessmentNo}`
      })
    },
    handleStartAssessment(row) {
      this.$confirm('确认开始风险评估?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.assessmentStatus = 'IN_PROGRESS'
        this.$message({
          type: 'success',
          message: '风险评估已开始!'
        })
      })
    },
    handleReview(row) {
      this.$message({
        type: 'info',
        message: '风险评估审核功能'
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.$message({ type: 'info', message: '编辑功能' })
          break
        case 'copy':
          this.$message({ type: 'info', message: '复制功能' })
          break
        case 'report':
          this.$message({ type: 'info', message: '生成报告功能' })
          break
        case 'history':
          this.$message({ type: 'info', message: '评估历史功能' })
          break
      }
    },
    handleImport() {
      this.$message({
        type: 'info',
        message: '批量导入功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
    },
    getNextAssessmentClass(date) {
      if (!date) return ''
      const today = new Date()
      const nextDate = new Date(date)
      const diffDays = Math.ceil((nextDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays <= 7) return 'urgent-assessment'
      if (diffDays <= 30) return 'upcoming-assessment'
      return ''
    },
    getRiskScoreClass(score) {
      if (score >= 8) return 'high-risk-score'
      if (score >= 6) return 'medium-risk-score'
      return 'low-risk-score'
    },
    getRiskTypeTagType(category) {
      const typeMap = {
        'MARKET': 'primary',
        'CREDIT': 'success',
        'LIQUIDITY': 'warning',
        'OPERATIONAL': 'info',
        'COMPLIANCE': 'danger'
      }
      return typeMap[category] || 'default'
    },
    getRiskCategoryText(category) {
      const textMap = {
        'MARKET': '市场风险',
        'CREDIT': '信用风险',
        'LIQUIDITY': '流动性风险',
        'OPERATIONAL': '操作风险',
        'COMPLIANCE': '合规风险'
      }
      return textMap[category] || category
    },
    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'info'
      }
      return typeMap[level] || 'default'
    },
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return textMap[level] || level
    },
    getAssessmentStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'primary',
        'REVIEWED': 'success'
      }
      return typeMap[status] || 'default'
    },
    getAssessmentStatusText(status) {
      const textMap = {
        'PENDING': '待评估',
        'IN_PROGRESS': '评估中',
        'COMPLETED': '已完成',
        'REVIEWED': '已审核'
      }
      return textMap[status] || status
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-assessment-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .assessment-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.high-risk-icon {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
          }
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.average-score-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .asset-value, .risk-exposure {
    font-weight: 600;
    color: #409EFF;
  }

  .high-risk-score {
    color: #F56C6C;
    font-weight: 600;
  }

  .medium-risk-score {
    color: #E6A23C;
    font-weight: 600;
  }

  .low-risk-score {
    color: #67C23A;
    font-weight: 600;
  }

  .urgent-assessment {
    color: #F56C6C;
    font-weight: 600;
  }

  .upcoming-assessment {
    color: #E6A23C;
    font-weight: 600;
  }
}
</style>
