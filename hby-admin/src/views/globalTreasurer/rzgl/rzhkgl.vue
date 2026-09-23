<template>
  <div class="financing-repayment-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            融资还款管理
          </h2>
          <p class="page-description">融资还款计划、还款执行和逾期管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateRepayment">
            新增还款
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

    <!-- 还款概览卡片 -->
    <div class="repayment-overview">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">还款计划总数</div>
                <div class="card-value">{{ totalRepayments }}</div>
                <div class="card-change">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待还款</div>
                <div class="card-value">{{ pendingRepayments }}</div>
                <div class="card-change warning">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待还金额</div>
                <div class="card-value">{{ pendingAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon overdue-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">逾期笔数</div>
                <div class="card-value">{{ overdueRepayments }}</div>
                <div class="card-change negative">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon completed-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已完成</div>
                <div class="card-value">{{ completedRepayments }}</div>
                <div class="card-change positive">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rate-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">按时还款率</div>
                <div class="card-value">{{ onTimeRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 还款分析图表 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>还款状态分布</h3>
          </div>
          <div id="repaymentStatusChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>月度还款趋势</h3>
          </div>
          <div id="monthlyTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资类型还款分布</h3>
          </div>
          <div id="financingTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 即将到期提醒 -->
    <el-card class="reminder-card" shadow="never">
      <div class="card-header">
        <h3>
          <i class="el-icon-bell" style="color: #E6A23C;"></i>
          即将到期还款提醒
        </h3>
        <el-button type="text" @click="handleViewAllReminders">查看全部</el-button>
      </div>
      <div class="reminder-list">
        <el-row :gutter="20">
          <el-col :span="8" v-for="reminder in upcomingRepayments" :key="reminder.id">
            <div class="reminder-item">
              <div class="reminder-header">
                <div class="reminder-title">{{ reminder.title }}</div>
                <div class="reminder-days" :class="getDaysClass(reminder.daysLeft)">
                  {{ reminder.daysLeft }}天后到期
                </div>
              </div>
              <div class="reminder-content">
                <div class="reminder-amount">{{ formatCurrency(reminder.amount) }}</div>
                <div class="reminder-date">到期日期：{{ reminder.dueDate }}</div>
              </div>
              <div class="reminder-actions">
                <el-button type="primary" size="mini" @click="handleRepayNow(reminder)">立即还款</el-button>
                <el-button type="text" size="mini" @click="handleReminderDetail(reminder)">详情</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="还款编号">
            <el-input
              v-model="listQuery.repaymentNo"
              placeholder="请输入还款编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
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
          <el-form-item label="还款状态">
            <el-select
              v-model="listQuery.repaymentStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待还款" value="PENDING" />
              <el-option label="已还款" value="COMPLETED" />
              <el-option label="逾期" value="OVERDUE" />
              <el-option label="部分还款" value="PARTIAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="还款类型">
            <el-select
              v-model="listQuery.repaymentType"
              placeholder="请选择类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="本金" value="PRINCIPAL" />
              <el-option label="利息" value="INTEREST" />
              <el-option label="本息" value="PRINCIPAL_INTEREST" />
              <el-option label="费用" value="FEE" />
            </el-select>
          </el-form-item>
          <el-form-item label="到期日期">
            <el-date-picker
              v-model="listQuery.dueDateRange"
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

    <!-- 还款计划表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="repaymentList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="还款ID" prop="repaymentId" width="80" align="center" />
        <el-table-column label="还款编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.repaymentNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="融资类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFinancingTypeTagType(row.financingType)" size="mini">
              {{ getFinancingTypeText(row.financingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="还款类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRepaymentTypeTagType(row.repaymentType)" size="mini">
              {{ getRepaymentTypeText(row.repaymentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="应还金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="due-amount">{{ formatCurrency(row.dueAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已还金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="paid-amount">{{ formatCurrency(row.paidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="剩余金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="remaining-amount">{{ formatCurrency(row.dueAmount - row.paidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getDueDateClass(row.dueDate)">{{ row.dueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际还款日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.actualRepaymentDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="逾期天数" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getOverdueDaysClass(row.overdueDays)">
              {{ row.overdueDays > 0 ? row.overdueDays + '天' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="还款状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRepaymentStatusTagType(row.repaymentStatus)" size="mini">
              {{ getRepaymentStatusText(row.repaymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.repaymentStatus === 'PENDING'" type="primary" size="mini" @click="handleRepay(row)">
              还款
            </el-button>
            <el-button v-if="row.repaymentStatus === 'PARTIAL'" type="warning" size="mini" @click="handlePartialRepay(row)">
              补还
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
                <el-dropdown-item :command="{action: 'history', row: row}">还款历史</el-dropdown-item>
                <el-dropdown-item :command="{action: 'receipt', row: row}">还款凭证</el-dropdown-item>
                <el-dropdown-item :command="{action: 'extend', row: row}">展期申请</el-dropdown-item>
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
import { getFinancingRepaymentPage, createFinancingRepayment } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancingRepaymentManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        repaymentNo: undefined,
        financingType: undefined,
        repaymentStatus: undefined,
        repaymentType: undefined,
        dueDateRange: undefined
      },
      totalRepayments: 156,
      pendingRepayments: 23,
      pendingAmount: 45600.8,
      overdueRepayments: 2,
      completedRepayments: 131,
      onTimeRate: 94.2,
      repaymentList: [],
      multipleSelection: [],
      upcomingRepayments: [],
      repaymentStatusChart: null,
      monthlyTrendChart: null,
      financingTypeChart: null
    }
  },
  mounted() {
    this.getList()
    this.getUpcomingRepayments()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.repaymentStatusChart) {
      this.repaymentStatusChart.dispose()
    }
    if (this.monthlyTrendChart) {
      this.monthlyTrendChart.dispose()
    }
    if (this.financingTypeChart) {
      this.financingTypeChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.repaymentList = [
          {
            repaymentId: 1,
            repaymentNo: 'RP20240925001',
            financingId: 1,
            financingType: 'BANK_LOAN',
            repaymentType: 'PRINCIPAL_INTEREST',
            dueAmount: 4500000.00,
            paidAmount: 0.00,
            dueDate: '2024-10-25',
            actualRepaymentDate: null,
            overdueDays: 0,
            repaymentStatus: 'PENDING',
            repaymentMethod: 'BANK_TRANSFER',
            createTime: '2024-09-25 10:00:00'
          },
          {
            repaymentId: 2,
            repaymentNo: 'RP20240920002',
            financingId: 2,
            financingType: 'LEASING',
            repaymentType: 'INTEREST',
            dueAmount: 550000.00,
            paidAmount: 550000.00,
            dueDate: '2024-09-20',
            actualRepaymentDate: '2024-09-20',
            overdueDays: 0,
            repaymentStatus: 'COMPLETED',
            repaymentMethod: 'BANK_TRANSFER',
            createTime: '2024-09-20 14:30:00'
          },
          {
            repaymentId: 3,
            repaymentNo: 'RP20240915003',
            financingId: 3,
            financingType: 'BOND',
            repaymentType: 'INTEREST',
            dueAmount: 4800000.00,
            paidAmount: 2400000.00,
            dueDate: '2024-09-15',
            actualRepaymentDate: null,
            overdueDays: 10,
            repaymentStatus: 'PARTIAL',
            repaymentMethod: 'BANK_TRANSFER',
            createTime: '2024-09-15 16:45:00'
          }
        ]
        this.total = this.repaymentList.length
        this.listLoading = false
      }, 1000)
    },
    getUpcomingRepayments() {
      // 模拟即将到期的还款数据
      this.upcomingRepayments = [
        {
          id: 1,
          title: '工商银行流动资金贷款',
          amount: 4500000.00,
          dueDate: '2024-10-25',
          daysLeft: 30
        },
        {
          id: 2,
          title: '华融租赁设备租金',
          amount: 550000.00,
          dueDate: '2024-10-01',
          daysLeft: 6
        },
        {
          id: 3,
          title: '企业债券付息',
          amount: 4800000.00,
          dueDate: '2025-01-15',
          daysLeft: 112
        }
      ]
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化还款状态图表
      this.repaymentStatusChart = echarts.init(document.getElementById('repaymentStatusChart'))
      this.updateRepaymentStatusChart()
      
      // 初始化月度趋势图表
      this.monthlyTrendChart = echarts.init(document.getElementById('monthlyTrendChart'))
      this.updateMonthlyTrendChart()
      
      // 初始化融资类型图表
      this.financingTypeChart = echarts.init(document.getElementById('financingTypeChart'))
      this.updateFinancingTypeChart()
    },
    updateRepaymentStatusChart() {
      const data = [
        { name: '已完成', value: 131, itemStyle: { color: '#67C23A' } },
        { name: '待还款', value: 23, itemStyle: { color: '#E6A23C' } },
        { name: '逾期', value: 2, itemStyle: { color: '#F56C6C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
        },
        series: [
          {
            name: '还款状态',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }
      
      this.repaymentStatusChart.setOption(option)
    },
    updateMonthlyTrendChart() {
      const months = this.generateMonthLabels(6)
      const repaymentData = this.generateMockData(6, 15, 35)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '还款笔数',
            type: 'line',
            data: repaymentData,
            itemStyle: { color: '#409EFF' },
            smooth: true
          }
        ]
      }
      
      this.monthlyTrendChart.setOption(option)
    },
    updateFinancingTypeChart() {
      const data = [
        { name: '银行贷款', value: 85, itemStyle: { color: '#409EFF' } },
        { name: '债券发行', value: 42, itemStyle: { color: '#67C23A' } },
        { name: '融资租赁', value: 29, itemStyle: { color: '#E6A23C' } }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
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
    generateMonthLabels(count) {
      const labels = []
      for (let i = count - 1; i >= 0; i--) {
        const date = new Date()
        date.setMonth(date.getMonth() - i)
        labels.push((date.getMonth() + 1) + '月')
      }
      return labels
    },
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        repaymentNo: undefined,
        financingType: undefined,
        repaymentStatus: undefined,
        repaymentType: undefined,
        dueDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreateRepayment() {
      this.$message({
        type: 'info',
        message: '新增还款功能'
      })
    },
    handleViewDetail(row) {
      this.$message({
        type: 'info',
        message: `查看还款详情: ${row.repaymentNo}`
      })
    },
    handleRepay(row) {
      this.$confirm('确认执行还款操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.repaymentStatus = 'COMPLETED'
        row.paidAmount = row.dueAmount
        row.actualRepaymentDate = new Date().toISOString().slice(0, 10)
        this.$message({
          type: 'success',
          message: '还款成功!'
        })
      })
    },
    handlePartialRepay(row) {
      this.$message({
        type: 'info',
        message: '部分还款功能'
      })
    },
    handleRepayNow(reminder) {
      this.$message({
        type: 'info',
        message: `立即还款: ${reminder.title}`
      })
    },
    handleReminderDetail(reminder) {
      this.$message({
        type: 'info',
        message: `查看提醒详情: ${reminder.title}`
      })
    },
    handleViewAllReminders() {
      this.$message({
        type: 'info',
        message: '查看全部提醒功能'
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.$message({ type: 'info', message: '编辑功能' })
          break
        case 'history':
          this.$message({ type: 'info', message: '还款历史功能' })
          break
        case 'receipt':
          this.$message({ type: 'info', message: '还款凭证功能' })
          break
        case 'extend':
          this.$message({ type: 'info', message: '展期申请功能' })
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
    getDaysClass(days) {
      if (days <= 7) return 'urgent-days'
      if (days <= 30) return 'warning-days'
      return 'normal-days'
    },
    getDueDateClass(date) {
      if (!date) return ''
      const today = new Date()
      const dueDate = new Date(date)
      const diffDays = Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'overdue-date'
      if (diffDays <= 7) return 'urgent-date'
      if (diffDays <= 30) return 'warning-date'
      return ''
    },
    getOverdueDaysClass(days) {
      if (!days || days <= 0) return ''
      if (days <= 7) return 'overdue-warning'
      return 'overdue-danger'
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
    getRepaymentTypeTagType(type) {
      const typeMap = {
        'PRINCIPAL': 'primary',
        'INTEREST': 'success',
        'PRINCIPAL_INTEREST': 'warning',
        'FEE': 'info'
      }
      return typeMap[type] || 'default'
    },
    getRepaymentTypeText(type) {
      const textMap = {
        'PRINCIPAL': '本金',
        'INTEREST': '利息',
        'PRINCIPAL_INTEREST': '本息',
        'FEE': '费用'
      }
      return textMap[type] || type
    },
    getRepaymentStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'OVERDUE': 'danger',
        'PARTIAL': 'info'
      }
      return typeMap[status] || 'default'
    },
    getRepaymentStatusText(status) {
      const textMap = {
        'PENDING': '待还款',
        'COMPLETED': '已还款',
        'OVERDUE': '逾期',
        'PARTIAL': '部分还款'
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
.financing-repayment-manage {
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

  .repayment-overview {
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
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.overdue-icon {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
          }
          &.completed-icon {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
          }
          &.rate-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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

  .chart-card, .reminder-card, .search-card, .table-card {
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

  .reminder-list {
    .reminder-item {
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 16px;
      background: #fafafa;
      
      .reminder-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        
        .reminder-title {
          font-size: 14px;
          font-weight: 600;
          color: #303133;
        }
        
        .reminder-days {
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 4px;
          
          &.urgent-days {
            background: #fef0f0;
            color: #f56c6c;
          }
          &.warning-days {
            background: #fdf6ec;
            color: #e6a23c;
          }
          &.normal-days {
            background: #f0f9ff;
            color: #409eff;
          }
        }
      }
      
      .reminder-content {
        margin-bottom: 12px;
        
        .reminder-amount {
          font-size: 18px;
          font-weight: 600;
          color: #409EFF;
          margin-bottom: 4px;
        }
        
        .reminder-date {
          font-size: 12px;
          color: #606266;
        }
      }
      
      .reminder-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .due-amount, .paid-amount, .remaining-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .overdue-date, .overdue-danger {
    color: #F56C6C;
    font-weight: 600;
  }

  .urgent-date, .overdue-warning {
    color: #E6A23C;
    font-weight: 600;
  }

  .warning-date {
    color: #E6A23C;
  }
}
</style>
