<template>
  <div class="financial-leasing-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-truck"></i>
            融资租赁管理
          </h2>
          <p class="page-description">融资租赁申请、审批、合同签署和租金管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增租赁申请
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

    <!-- 租赁概览卡片 -->
    <div class="leasing-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">租赁申请总数</div>
                <div class="card-value">{{ totalApplications }}</div>
                <div class="card-change">笔</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在租余额</div>
                <div class="card-value">{{ outstandingAmount }}</div>
                <div class="card-change positive">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rate-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均利率</div>
                <div class="card-value">{{ averageRate }}</div>
                <div class="card-change">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon approval-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">审批通过率</div>
                <div class="card-value">{{ approvalRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 租赁分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>租赁类型分布</h3>
            <div class="chart-controls">
              <el-radio-group v-model="leasingTypeChartType" size="small" @change="handleLeasingTypeChartChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="leasingTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>租赁申请趋势</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="6M">6个月</el-radio-button>
                <el-radio-button label="1Y">1年</el-radio-button>
                <el-radio-button label="2Y">2年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="leasingTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="申请编号">
            <el-input
              v-model="listQuery.applicationNo"
              placeholder="请输入申请编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="租赁类型">
            <el-select
              v-model="listQuery.leasingType"
              placeholder="请选择租赁类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="融资租赁" value="FINANCE_LEASE" />
              <el-option label="经营租赁" value="OPERATING_LEASE" />
              <el-option label="售后回租" value="SALE_LEASEBACK" />
            </el-select>
          </el-form-item>
          <el-form-item label="租赁公司">
            <el-input
              v-model="listQuery.leasingCompany"
              placeholder="请输入租赁公司"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="申请状态">
            <el-select
              v-model="listQuery.applicationStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待提交" value="PENDING" />
              <el-option label="已提交" value="SUBMITTED" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请日期">
            <el-date-picker
              v-model="listQuery.applicationDateRange"
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

    <!-- 租赁申请表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="leasingList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="申请ID" prop="applicationId" width="80" align="center" />
        <el-table-column label="申请编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.applicationNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getLeasingTypeTagType(row.leasingType)" size="mini">
              {{ getLeasingTypeText(row.leasingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="租赁公司" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.leasingCompany }}</span>
          </template>
        </el-table-column>
        <el-table-column label="资产类型" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.assetType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="资产价值" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="asset-value">{{ formatCurrency(row.assetValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="leasing-amount">{{ formatCurrency(row.leasingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁期限" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.leasingTerm }}{{ getTermUnitText(row.termUnit) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.interestRate ? row.interestRate + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="月租金" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="monthly-rent">{{ formatCurrency(row.monthlyRent) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="残值" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.residualValue ? formatCurrency(row.residualValue) : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getApplicationStatusTagType(row.applicationStatus)" size="mini">
              {{ getApplicationStatusText(row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicationDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.applicationStatus === 'PENDING'" type="primary" size="mini" @click="handleSubmit(row)">
              提交
            </el-button>
            <el-button v-if="row.applicationStatus === 'SUBMITTED'" type="success" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.applicationStatus === 'APPROVED'" type="warning" size="mini" @click="handleContract(row)">
              签约
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
                <el-dropdown-item :command="{action: 'asset', row: row}">资产管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'payment', row: row}">租金管理</el-dropdown-item>
                <el-dropdown-item :command="{action: 'return', row: row}">退租管理</el-dropdown-item>
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
import { getFinancialLeasePage, createFinancialLease } from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancialLeasingManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        applicationNo: undefined,
        leasingType: undefined,
        leasingCompany: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
      },
      totalApplications: 32,
      outstandingAmount: 95800.6,
      averageRate: 5.25,
      approvalRate: 84.3,
      leasingTypeChartType: 'pie',
      trendPeriod: '1Y',
      leasingList: [],
      multipleSelection: [],
      leasingTypeChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.leasingTypeChart) {
      this.leasingTypeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.leasingList = [
          {
            applicationId: 1,
            applicationNo: 'FL20240925001',
            planId: 2,
            leasingCompany: '华融金融租赁有限公司',
            leasingType: 'FINANCE_LEASE',
            assetType: '生产设备',
            assetDescription: '高精度数控机床设备',
            assetValue: 20000000.00,
            leasingAmount: 18000000.00,
            currencyCode: 'CNY',
            leasingTerm: 36,
            termUnit: 'MONTH',
            interestRate: 5.2,
            monthlyRent: 550000.00,
            residualValue: 2000000.00,
            securityDeposit: 1000000.00,
            applicationStatus: 'APPROVED',
            applicationDate: '2024-09-25',
            createTime: '2024-09-25 10:00:00'
          },
          {
            applicationId: 2,
            applicationNo: 'FL20240920002',
            planId: 3,
            leasingCompany: '招银金融租赁有限公司',
            leasingType: 'SALE_LEASEBACK',
            assetType: '办公设备',
            assetDescription: '办公楼及配套设施',
            assetValue: 50000000.00,
            leasingAmount: 45000000.00,
            currencyCode: 'CNY',
            leasingTerm: 60,
            termUnit: 'MONTH',
            interestRate: 4.8,
            monthlyRent: 850000.00,
            residualValue: 5000000.00,
            securityDeposit: 2000000.00,
            applicationStatus: 'SUBMITTED',
            applicationDate: '2024-09-20',
            createTime: '2024-09-20 14:30:00'
          },
          {
            applicationId: 3,
            applicationNo: 'FL20240915003',
            planId: 4,
            leasingCompany: '平安国际融资租赁有限公司',
            leasingType: 'OPERATING_LEASE',
            assetType: '运输设备',
            assetDescription: '物流运输车辆',
            assetValue: 8000000.00,
            leasingAmount: 7200000.00,
            currencyCode: 'CNY',
            leasingTerm: 24,
            termUnit: 'MONTH',
            interestRate: 5.8,
            monthlyRent: 320000.00,
            residualValue: 800000.00,
            securityDeposit: 500000.00,
            applicationStatus: 'PENDING',
            applicationDate: '2024-09-15',
            createTime: '2024-09-15 16:45:00'
          }
        ]
        this.total = this.leasingList.length
        this.listLoading = false
      }, 1000)
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化租赁类型图表
      this.leasingTypeChart = echarts.init(document.getElementById('leasingTypeChart'))
      this.updateLeasingTypeChart()
      
      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('leasingTrendChart'))
      this.updateTrendChart()
    },
    updateLeasingTypeChart() {
      const data = [
        { name: '融资租赁', value: 20, itemStyle: { color: '#409EFF' } },
        { name: '售后回租', value: 8, itemStyle: { color: '#67C23A' } },
        { name: '经营租赁', value: 4, itemStyle: { color: '#E6A23C' } }
      ]
      
      let option = {}
      
      if (this.leasingTypeChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}笔 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '租赁类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data
            }
          ]
        }
      } else {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '申请数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }
      
      this.leasingTypeChart.setOption(option)
    },
    updateTrendChart() {
      const months = this.generateMonthLabels(12)
      const applicationData = this.generateMockData(12, 2, 8)
      const amountData = this.generateMockData(12, 6000, 18000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['申请数量', '租赁金额(万元)']
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
        yAxis: [
          {
            type: 'value',
            name: '数量(笔)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '申请数量',
            type: 'line',
            data: applicationData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '租赁金额(万元)',
            type: 'line',
            yAxisIndex: 1,
            data: amountData,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      
      this.trendChart.setOption(option)
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
    handleLeasingTypeChartChange() {
      this.updateLeasingTypeChart()
    },
    handleTrendPeriodChange() {
      this.updateTrendChart()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        applicationNo: undefined,
        leasingType: undefined,
        leasingCompany: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.$message({
        type: 'info',
        message: '新增租赁申请功能'
      })
    },
    handleViewDetail(row) {
      this.$message({
        type: 'info',
        message: `查看租赁申请详情: ${row.applicationNo}`
      })
    },
    handleSubmit(row) {
      this.$confirm('确认提交该租赁申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.applicationStatus = 'SUBMITTED'
        this.$message({
          type: 'success',
          message: '租赁申请提交成功!'
        })
      })
    },
    handleApprove(row) {
      this.$confirm('确认审批通过该租赁申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.applicationStatus = 'APPROVED'
        this.$message({
          type: 'success',
          message: '租赁申请审批通过!'
        })
      })
    },
    handleContract(row) {
      this.$message({
        type: 'info',
        message: '合同签署功能'
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
        case 'asset':
          this.$message({ type: 'info', message: '资产管理功能' })
          break
        case 'payment':
          this.$message({ type: 'info', message: '租金管理功能' })
          break
        case 'return':
          this.$message({ type: 'info', message: '退租管理功能' })
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
    getLeasingTypeTagType(type) {
      const typeMap = {
        'FINANCE_LEASE': 'primary',
        'OPERATING_LEASE': 'success',
        'SALE_LEASEBACK': 'warning'
      }
      return typeMap[type] || 'default'
    },
    getLeasingTypeText(type) {
      const textMap = {
        'FINANCE_LEASE': '融资租赁',
        'OPERATING_LEASE': '经营租赁',
        'SALE_LEASEBACK': '售后回租'
      }
      return textMap[type] || type
    },
    getTermUnitText(unit) {
      const textMap = {
        'MONTH': '月',
        'YEAR': '年'
      }
      return textMap[unit] || unit
    },
    getApplicationStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'primary',
        'REJECTED': 'danger',
        'CANCELLED': 'default'
      }
      return typeMap[status] || 'info'
    },
    getApplicationStatusText(status) {
      const textMap = {
        'PENDING': '待提交',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
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
.financial-leasing-manage {
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

  .leasing-overview {
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
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.rate-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.approval-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
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

  .asset-value, .leasing-amount, .monthly-rent {
    font-weight: 600;
    color: #409EFF;
  }
}
</style>
