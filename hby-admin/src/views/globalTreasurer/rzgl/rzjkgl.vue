<template>
  <div class="financing-monitoring-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-monitor"></i>
            融资监控管理
          </h2>
          <p class="page-description">融资项目实时监控、风险预警和合规管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
          <el-button type="success" icon="el-icon-bell" @click="handleAlerts">
            预警管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 监控概览卡片 -->
    <div class="monitoring-overview">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">监控项目总数</div>
                <div class="card-value">{{ totalFinancing }}</div>
                <div class="card-change">个</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">活跃项目</div>
                <div class="card-value">{{ activeFinancing }}</div>
                <div class="card-change positive">个</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon outstanding-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">未偿余额</div>
                <div class="card-value">{{ totalOutstanding }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon alert-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">风险预警</div>
                <div class="card-value">{{ riskAlerts }}</div>
                <div class="card-change negative">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon payment-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将到期</div>
                <div class="card-value">{{ upcomingPayments }}</div>
                <div class="card-change warning">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon compliance-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">合规率</div>
                <div class="card-value">{{ complianceRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 监控分析图表 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资类型监控</h3>
          </div>
          <div id="financingTypeMonitorChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险等级分布</h3>
          </div>
          <div id="riskLevelChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>合规状态分布</h3>
          </div>
          <div id="complianceChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 到期提醒和预警信息 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="alert-card" shadow="never">
          <div class="card-header">
            <h3>
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
              风险预警
            </h3>
            <el-button type="text" @click="handleViewAllAlerts">查看全部</el-button>
          </div>
          <div class="alert-list">
            <div v-for="alert in riskAlertList" :key="alert.id" class="alert-item">
              <div class="alert-icon">
                <i :class="getAlertIcon(alert.level)" :style="{color: getAlertColor(alert.level)}"></i>
              </div>
              <div class="alert-content">
                <div class="alert-title">{{ alert.title }}</div>
                <div class="alert-desc">{{ alert.description }}</div>
                <div class="alert-time">{{ alert.createTime }}</div>
              </div>
              <div class="alert-actions">
                <el-button type="text" size="mini" @click="handleAlertDetail(alert)">详情</el-button>
                <el-button type="text" size="mini" @click="handleProcessAlert(alert)">处理</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="reminder-card" shadow="never">
          <div class="card-header">
            <h3>
              <i class="el-icon-time" style="color: #409EFF;"></i>
              到期提醒
            </h3>
            <el-button type="text" @click="handleViewAllReminders">查看全部</el-button>
          </div>
          <div class="reminder-list">
            <div v-for="reminder in upcomingPaymentList" :key="reminder.id" class="reminder-item">
              <div class="reminder-icon">
                <i class="el-icon-time" style="color: #409EFF;"></i>
              </div>
              <div class="reminder-content">
                <div class="reminder-title">{{ reminder.title }}</div>
                <div class="reminder-desc">{{ reminder.description }}</div>
                <div class="reminder-time">到期时间：{{ reminder.dueDate }}</div>
              </div>
              <div class="reminder-actions">
                <el-button type="text" size="mini" @click="handleReminderDetail(reminder)">详情</el-button>
                <el-button type="text" size="mini" @click="handleProcessReminder(reminder)">处理</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="融资类型">
            <el-select
              v-model="listQuery.financingType"
              placeholder="请选择融资类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="银行贷款" value="BANK_LOAN" />
              <el-option label="债券发行" value="BOND" />
              <el-option label="融资租赁" value="LEASING" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskRating"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item label="合规状态">
            <el-select
              v-model="listQuery.covenantCompliance"
              placeholder="请选择合规状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="合规" value="COMPLIANT" />
              <el-option label="预警" value="WARNING" />
              <el-option label="违约" value="BREACH" />
            </el-select>
          </el-form-item>
          <el-form-item label="预警状态">
            <el-select
              v-model="listQuery.alertStatus"
              placeholder="请选择预警状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="预警" value="WARNING" />
              <el-option label="严重" value="CRITICAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="监控日期">
            <el-date-picker
              v-model="listQuery.monitoringDateRange"
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

    <!-- 融资监控表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="monitoringList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="监控ID" prop="monitoringId" width="80" align="center" />
        <el-table-column label="融资类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFinancingTypeTagType(row.financingType)" size="mini">
              {{ getFinancingTypeText(row.financingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="未偿余额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="outstanding-amount">{{ formatCurrency(row.outstandingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.interestRate ? row.interestRate + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下次付款日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getPaymentDateClass(row.nextPaymentDate)">{{ row.nextPaymentDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="下次付款金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="payment-amount">{{ formatCurrency(row.nextPaymentAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getMaturityDateClass(row.maturityDate)">{{ row.maturityDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="距到期天数" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getDaysToMaturityClass(row.daystomaturity)">{{ row.daystomaturity || '-' }}天</span>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getComplianceTagType(row.covenantCompliance)" size="mini">
              {{ getComplianceText(row.covenantCompliance) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskRatingTagType(row.riskRating)" size="mini">
              {{ getRiskRatingText(row.riskRating) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertStatusTagType(row.alertStatus)" size="mini">
              {{ getAlertStatusText(row.alertStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="监控日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.monitoringDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-button type="success" size="mini" @click="handleUpdateMonitoring(row)">
              更新
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'alert', row: row}">预警设置</el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: row}">生成报告</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">历史记录</el-dropdown-item>
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
import { getFinancingMonitoringPage, getFinancingMonitoringOverview } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancingMonitoringManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        financingType: undefined,
        riskRating: undefined,
        covenantCompliance: undefined,
        alertStatus: undefined,
        monitoringDateRange: undefined
      },
      totalFinancing: 28,
      activeFinancing: 12,
      totalOutstanding: 856005.0,
      riskAlerts: 3,
      upcomingPayments: 5,
      complianceRate: 96.4,
      monitoringList: [],
      multipleSelection: [],
      riskAlertList: [],
      upcomingPaymentList: [],
      financingTypeChart: null,
      riskLevelChart: null,
      complianceChart: null
    }
  },
  mounted() {
    this.getList()
    this.getOverviewData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.financingTypeChart) {
      this.financingTypeChart.dispose()
    }
    if (this.riskLevelChart) {
      this.riskLevelChart.dispose()
    }
    if (this.complianceChart) {
      this.complianceChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.monitoringList = [
          {
            monitoringId: 1,
            planId: 1,
            financingId: 1,
            financingType: 'BANK_LOAN',
            monitoringDate: '2024-09-25',
            outstandingAmount: 45000000.00,
            interestRate: 4.35,
            nextPaymentDate: '2024-10-25',
            nextPaymentAmount: 4500000.00,
            maturityDate: '2025-09-30',
            daystomaturity: 370,
            covenantCompliance: 'COMPLIANT',
            riskRating: 'LOW',
            alertStatus: 'NORMAL',
            monitoringNotes: '正常监控，无异常',
            createTime: '2024-09-25 10:00:00'
          },
          {
            monitoringId: 2,
            planId: 2,
            financingId: 2,
            financingType: 'LEASING',
            monitoringDate: '2024-09-25',
            outstandingAmount: 16200000.00,
            interestRate: 5.2,
            nextPaymentDate: '2024-10-01',
            nextPaymentAmount: 550000.00,
            maturityDate: '2027-09-20',
            daystomaturity: 1090,
            covenantCompliance: 'WARNING',
            riskRating: 'MEDIUM',
            alertStatus: 'WARNING',
            monitoringNotes: '资产价值下降，需要关注',
            createTime: '2024-09-25 10:00:00'
          },
          {
            monitoringId: 3,
            planId: 3,
            financingId: 3,
            financingType: 'BOND',
            monitoringDate: '2024-09-25',
            outstandingAmount: 100000000.00,
            interestRate: 4.8,
            nextPaymentDate: '2025-01-15',
            nextPaymentAmount: 4800000.00,
            maturityDate: '2027-01-15',
            daystomaturity: 842,
            covenantCompliance: 'COMPLIANT',
            riskRating: 'LOW',
            alertStatus: 'NORMAL',
            monitoringNotes: '债券运行正常',
            createTime: '2024-09-25 10:00:00'
          }
        ]
        this.total = this.monitoringList.length
        this.listLoading = false
      }, 1000)
    },
    getOverviewData() {
      // 模拟预警数据
      this.riskAlertList = [
        {
          id: 1,
          level: 'HIGH',
          title: '融资租赁资产价值下降',
          description: '设备资产评估价值较上期下降15%，需要关注',
          createTime: '2024-09-25 09:30:00'
        },
        {
          id: 2,
          level: 'MEDIUM',
          title: '银行贷款利率上调',
          description: '浮动利率贷款利率上调0.25个百分点',
          createTime: '2024-09-24 14:20:00'
        },
        {
          id: 3,
          level: 'LOW',
          title: '债券信用评级调整',
          description: '信用评级机构将评级展望调整为稳定',
          createTime: '2024-09-23 16:45:00'
        }
      ]
      
      // 模拟到期提醒数据
      this.upcomingPaymentList = [
        {
          id: 1,
          title: '银行贷款还款',
          description: '工商银行流动资金贷款月度还款',
          dueDate: '2024-10-25',
          amount: 4500000.00
        },
        {
          id: 2,
          title: '融资租赁租金',
          description: '华融租赁设备租金支付',
          dueDate: '2024-10-01',
          amount: 550000.00
        },
        {
          id: 3,
          title: '债券付息',
          description: '企业债券半年度付息',
          dueDate: '2025-01-15',
          amount: 4800000.00
        }
      ]
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化融资类型监控图表
      this.financingTypeChart = echarts.init(document.getElementById('financingTypeMonitorChart'))
      this.updateFinancingTypeChart()
      
      // 初始化风险等级图表
      this.riskLevelChart = echarts.init(document.getElementById('riskLevelChart'))
      this.updateRiskLevelChart()
      
      // 初始化合规状态图表
      this.complianceChart = echarts.init(document.getElementById('complianceChart'))
      this.updateComplianceChart()
    },
    updateFinancingTypeChart() {
      const data = [
        { name: '银行贷款', value: 15, itemStyle: { color: '#409EFF' } },
        { name: '债券发行', value: 6, itemStyle: { color: '#67C23A' } },
        { name: '融资租赁', value: 7, itemStyle: { color: '#E6A23C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}个 ({d}%)'
        },
        series: [
          {
            name: '融资类型',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.financingTypeChart.setOption(option)
    },
    updateRiskLevelChart() {
      const data = [
        { name: '低风险', value: 18, itemStyle: { color: '#67C23A' } },
        { name: '中风险', value: 8, itemStyle: { color: '#E6A23C' } },
        { name: '高风险', value: 2, itemStyle: { color: '#F56C6C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}个 ({d}%)'
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
    updateComplianceChart() {
      const data = [
        { name: '合规', value: 24, itemStyle: { color: '#67C23A' } },
        { name: '预警', value: 3, itemStyle: { color: '#E6A23C' } },
        { name: '违约', value: 1, itemStyle: { color: '#F56C6C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}个 ({d}%)'
        },
        series: [
          {
            name: '合规状态',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.complianceChart.setOption(option)
    },
    refreshData() {
      this.getList()
      this.getOverviewData()
      this.$message({
        type: 'success',
        message: '数据刷新成功'
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        financingType: undefined,
        riskRating: undefined,
        covenantCompliance: undefined,
        alertStatus: undefined,
        monitoringDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleViewDetail(row) {
      this.$message({
        type: 'info',
        message: `查看监控详情: ${row.monitoringId}`
      })
    },
    handleUpdateMonitoring(row) {
      this.$message({
        type: 'info',
        message: '更新监控数据功能'
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'alert':
          this.$message({ type: 'info', message: '预警设置功能' })
          break
        case 'report':
          this.$message({ type: 'info', message: '生成报告功能' })
          break
        case 'history':
          this.$message({ type: 'info', message: '历史记录功能' })
          break
      }
    },
    handleAlerts() {
      this.$message({
        type: 'info',
        message: '预警管理功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '监控报告导出成功'
      })
    },
    handleViewAllAlerts() {
      this.$message({
        type: 'info',
        message: '查看全部预警功能'
      })
    },
    handleViewAllReminders() {
      this.$message({
        type: 'info',
        message: '查看全部提醒功能'
      })
    },
    handleAlertDetail(alert) {
      this.$message({
        type: 'info',
        message: `查看预警详情: ${alert.title}`
      })
    },
    handleProcessAlert(alert) {
      this.$message({
        type: 'info',
        message: `处理预警: ${alert.title}`
      })
    },
    handleReminderDetail(reminder) {
      this.$message({
        type: 'info',
        message: `查看提醒详情: ${reminder.title}`
      })
    },
    handleProcessReminder(reminder) {
      this.$message({
        type: 'info',
        message: `处理提醒: ${reminder.title}`
      })
    },
    getAlertIcon(level) {
      const iconMap = {
        'HIGH': 'el-icon-warning',
        'MEDIUM': 'el-icon-info',
        'LOW': 'el-icon-question'
      }
      return iconMap[level] || 'el-icon-info'
    },
    getAlertColor(level) {
      const colorMap = {
        'HIGH': '#F56C6C',
        'MEDIUM': '#E6A23C',
        'LOW': '#909399'
      }
      return colorMap[level] || '#909399'
    },
    getPaymentDateClass(date) {
      if (!date) return ''
      const today = new Date()
      const paymentDate = new Date(date)
      const diffDays = Math.ceil((paymentDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays <= 7) return 'urgent-payment'
      if (diffDays <= 30) return 'upcoming-payment'
      return ''
    },
    getMaturityDateClass(date) {
      if (!date) return ''
      const today = new Date()
      const maturityDate = new Date(date)
      const diffDays = Math.ceil((maturityDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays <= 90) return 'urgent-maturity'
      if (diffDays <= 365) return 'upcoming-maturity'
      return ''
    },
    getDaysToMaturityClass(days) {
      if (!days) return ''
      if (days <= 90) return 'urgent-days'
      if (days <= 365) return 'upcoming-days'
      return ''
    },
    getFinancingTypeTagType(type) {
      const typeMap = {
        'BANK_LOAN': 'primary',
        'BOND': 'success',
        'LEASING': 'warning'
      }
      return typeMap[type] || 'default'
    },
    getFinancingTypeText(type) {
      const textMap = {
        'BANK_LOAN': '银行贷款',
        'BOND': '债券发行',
        'LEASING': '融资租赁'
      }
      return textMap[type] || type
    },
    getComplianceTagType(compliance) {
      const typeMap = {
        'COMPLIANT': 'success',
        'WARNING': 'warning',
        'BREACH': 'danger'
      }
      return typeMap[compliance] || 'default'
    },
    getComplianceText(compliance) {
      const textMap = {
        'COMPLIANT': '合规',
        'WARNING': '预警',
        'BREACH': '违约'
      }
      return textMap[compliance] || compliance
    },
    getRiskRatingTagType(rating) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return typeMap[rating] || 'default'
    },
    getRiskRatingText(rating) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return textMap[rating] || rating
    },
    getAlertStatusTagType(status) {
      const typeMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger'
      }
      return typeMap[status] || 'default'
    },
    getAlertStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'CRITICAL': '严重'
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
.financing-monitoring-manage {
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

  .monitoring-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 50px;
          height: 50px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          i {
            font-size: 20px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.outstanding-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.alert-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.payment-icon {
            background: linear-gradient(135deg, #fbc2eb 0%, #a6c1ee 100%);
          }
          &.compliance-icon {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }
          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 2px;
          }
          .card-change {
            font-size: 10px;
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

  .chart-card, .alert-card, .reminder-card, .search-card, .table-card {
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
    height: 250px;
    width: 100%;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
      display: flex;
      align-items: center;
      
      i {
        margin-right: 8px;
      }
    }
  }

  .alert-list, .reminder-list {
    max-height: 300px;
    overflow-y: auto;
  }

  .alert-item, .reminder-item {
    display: flex;
    align-items: flex-start;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .alert-icon, .reminder-icon {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
      background: #f8f9fa;
    }
    
    .alert-content, .reminder-content {
      flex: 1;
      
      .alert-title, .reminder-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .alert-desc, .reminder-desc {
        font-size: 12px;
        color: #606266;
        margin-bottom: 4px;
      }
      
      .alert-time, .reminder-time {
        font-size: 11px;
        color: #909399;
      }
    }
    
    .alert-actions, .reminder-actions {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
  }

  .outstanding-amount, .payment-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .urgent-payment, .urgent-maturity, .urgent-days {
    color: #F56C6C;
    font-weight: 600;
  }

  .upcoming-payment, .upcoming-maturity, .upcoming-days {
    color: #E6A23C;
    font-weight: 600;
  }
}
</style>
