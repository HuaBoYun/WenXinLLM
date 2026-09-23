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
              <el-option label="直接租赁" value="DIRECT" />
              <el-option label="售后回租" value="LEASEBACK" />
              <el-option label="杠杆租赁" value="LEVERAGED" />
              <el-option label="经营租赁" value="OPERATING" />
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
          <el-form-item label="租赁状态">
            <el-select
              v-model="listQuery.applicationStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待提交" value="PENDING" />
              <el-option label="已提交" value="SUBMITTED" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="执行中" value="ACTIVE" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="起租日期">
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
        <el-table-column label="租赁编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.leaseNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁名称" width="200px" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.leaseName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getLeasingTypeTagType(row.leaseType)" size="mini">
              {{ getLeasingTypeText(row.leaseType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="租赁公司" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.lessorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="承租公司" width="150px" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.companyName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="leasing-amount">{{ formatCurrency(row.leaseAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="未偿还金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="outstanding-amount">{{ formatCurrency(row.outstandingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率(%)" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.interestRate ? (row.interestRate * 100).toFixed(2) + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="起租日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.startDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.endDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租赁状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getLeaseStatusTagType(row.leaseStatus)" size="mini">
              {{ getLeaseStatusText(row.leaseStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.leaseStatus === 'PENDING'" type="primary" size="mini" @click="handleSubmit(row)">
              提交
            </el-button>
            <el-button v-if="row.leaseStatus === 'SUBMITTED'" type="success" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.leaseStatus === 'APPROVED'" type="warning" size="mini" @click="handleContract(row)">
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
                <el-dropdown-item v-if="row.leaseStatus === 'ACTIVE'" :command="{action: 'return', row: row}">退租管理</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 弹窗组件 -->
    <LeaseFormDialog
      :visible.sync="formDialogVisible"
      :form-data="currentLease"
      :is-edit="isEditMode"
      @success="handleFormSuccess"
    />
    <LeaseDetailDialog
      :visible.sync="detailDialogVisible"
      :lease-id="currentLeaseId"
      @edit="handleEditFromDetail"
    />
    <LeaseApprovalDialog
      :visible.sync="approvalDialogVisible"
      :lease-data="currentLease"
      :is-reject="isRejectMode"
      @success="handleApprovalSuccess"
    />
    <LeaseImportDialog
      :visible.sync="importDialogVisible"
      @success="handleImportSuccess"
    />
    <LeaseAssetDialog
      :visible.sync="assetDialogVisible"
      :lease-id="currentLeaseId"
    />
    <LeasePaymentDialog
      :visible.sync="paymentDialogVisible"
      :lease-id="currentLeaseId"
    />
    <LeaseReturnDialog
      :visible.sync="returnDialogVisible"
      :lease-id="currentLeaseId"
      @success="handleReturnSuccess"
    />
  </div>
</template>

<script>
import {
  getFinancialLeasePage,
  createFinancialLease,
  updateFinancialLease,
  deleteFinancialLease,
  getFinancialLeaseDetail,
  submitFinancialLease,
  approveFinancialLease,
  rejectFinancialLease,
  activateFinancialLease,
  getFinancialLeaseOverview,
  getFinancialLeaseTypeDistribution,
  getFinancialLeaseTrend,
  exportFinancialLease,
  downloadFinancialLeaseTemplate
} from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'
import LeaseFormDialog from './components/LeaseFormDialog.vue'
import LeaseDetailDialog from './components/LeaseDetailDialog.vue'
import LeaseApprovalDialog from './components/LeaseApprovalDialog.vue'
import LeaseImportDialog from './components/LeaseImportDialog.vue'
import LeaseAssetDialog from './components/LeaseAssetDialog.vue'
import LeasePaymentDialog from './components/LeasePaymentDialog.vue'
import LeaseReturnDialog from './components/LeaseReturnDialog.vue'

export default {
  name: 'FinancialLeasingManage',
  components: { Pagination, LeaseFormDialog, LeaseDetailDialog, LeaseApprovalDialog, LeaseImportDialog, LeaseAssetDialog, LeasePaymentDialog, LeaseReturnDialog },
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
      totalApplications: 0,
      outstandingAmount: 0,
      averageRate: 0,
      approvalRate: 0,
      leasingTypeChartType: 'pie',
      trendPeriod: '1Y',
      leasingList: [],
      multipleSelection: [],
      leasingTypeChart: null,
      trendChart: null,
      typeDistributionData: [],
      trendData: { months: [], applicationData: [], amountData: [] },
      // 弹窗控制
      formDialogVisible: false,
      detailDialogVisible: false,
      approvalDialogVisible: false,
      importDialogVisible: false,
      assetDialogVisible: false,
      paymentDialogVisible: false,
      returnDialogVisible: false,
      currentLease: {},
      currentLeaseId: null,
      isEditMode: false,
      isRejectMode: false
    }
  },
  mounted() {
    this.getList()
    this.loadOverviewStatistics()
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
      // 调用真实API获取数据
      const params = {
        page: this.listQuery.page,
        limit: this.listQuery.limit,
        leasingCompany: this.listQuery.leasingCompany || undefined,
        leasingType: this.listQuery.leasingType || undefined,
        applicationStatus: this.listQuery.applicationStatus || undefined,
        leaseNo: this.listQuery.applicationNo || undefined
      }

      getFinancialLeasePage(params).then(response => {
        if (response && (response.code === 1 || response.code === 200)) {
          // 处理分页数据
          this.leasingList = response.data?.tlist || response.data?.list || response.data?.records || []
          this.total = response.data?.totalRecord || response.data?.total || 0
        } else {
          this.leasingList = []
          this.total = 0
          if (response?.msg) {
            this.$message.error(response.msg)
          }
        }
        this.listLoading = false
      }).catch(error => {
        console.error('查询融资租赁数据异常:', error)
        this.leasingList = []
        this.total = 0
        this.listLoading = false
        this.$message.error('查询融资租赁数据失败，请稍后重试')
      })
    },
    loadOverviewStatistics() {
      // 加载统计概览数据
      getFinancialLeaseOverview().then(response => {
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          this.totalApplications = response.data.totalApplications || 0
          this.outstandingAmount = response.data.outstandingAmount || 0
          this.averageRate = response.data.averageRate || 0
          this.approvalRate = response.data.approvalRate || 0
        }
      }).catch(error => {
        console.error('获取统计概览数据失败:', error)
      })
    },
    initCharts() {
      const echarts = require('echarts')

      // 初始化租赁类型图表
      this.leasingTypeChart = echarts.init(document.getElementById('leasingTypeChart'))
      this.loadTypeDistribution()

      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('leasingTrendChart'))
      this.loadTrendData()
    },
    loadTypeDistribution() {
      getFinancialLeaseTypeDistribution().then(response => {
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          this.typeDistributionData = response.data.map((item, index) => {
            const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
            return {
              name: item.name,
              value: item.value,
              itemStyle: { color: colors[index % colors.length] }
            }
          })
        } else {
          // 使用默认数据
          this.typeDistributionData = [
            { name: '融资租赁', value: 0, itemStyle: { color: '#409EFF' } },
            { name: '售后回租', value: 0, itemStyle: { color: '#67C23A' } },
            { name: '经营租赁', value: 0, itemStyle: { color: '#E6A23C' } }
          ]
        }
        this.updateLeasingTypeChart()
      }).catch(error => {
        console.error('获取租赁类型分布数据失败:', error)
        this.typeDistributionData = [
          { name: '融资租赁', value: 0, itemStyle: { color: '#409EFF' } },
          { name: '售后回租', value: 0, itemStyle: { color: '#67C23A' } },
          { name: '经营租赁', value: 0, itemStyle: { color: '#E6A23C' } }
        ]
        this.updateLeasingTypeChart()
      })
    },
    loadTrendData() {
      getFinancialLeaseTrend(this.trendPeriod).then(response => {
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          this.trendData = {
            months: response.data.months || [],
            applicationData: response.data.applicationData || [],
            amountData: response.data.amountData || []
          }
        } else {
          // 使用默认空数据
          this.trendData = { months: [], applicationData: [], amountData: [] }
        }
        this.updateTrendChart()
      }).catch(error => {
        console.error('获取趋势数据失败:', error)
        this.trendData = { months: [], applicationData: [], amountData: [] }
        this.updateTrendChart()
      })
    },
    updateLeasingTypeChart() {
      const data = this.typeDistributionData.length > 0 ? this.typeDistributionData : [
        { name: '融资租赁', value: 0, itemStyle: { color: '#409EFF' } },
        { name: '售后回租', value: 0, itemStyle: { color: '#67C23A' } },
        { name: '经营租赁', value: 0, itemStyle: { color: '#E6A23C' } }
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
      const months = this.trendData.months.length > 0 ? this.trendData.months : this.generateMonthLabels(12)
      const applicationData = this.trendData.applicationData.length > 0 ? this.trendData.applicationData : []
      const amountData = this.trendData.amountData.length > 0 ? this.trendData.amountData : []

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
      // 切换时间周期时重新加载趋势数据
      this.loadTrendData()
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
      this.currentLease = {}
      this.isEditMode = false
      this.formDialogVisible = true
    },
    handleViewDetail(row) {
      // 使用 leaseId 或 id
      this.currentLeaseId = row.leaseId || row.id
      this.detailDialogVisible = true
    },
    handleSubmit(row) {
      const leaseId = row.leaseId || row.id
      if (!leaseId) {
        this.$message.error('租赁ID不能为空')
        return
      }
      this.$confirm('确认提交该租赁申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitFinancialLease(leaseId).then(response => {
          if (response && (response.code === 1 || response.code === 200)) {
            this.$message.success('租赁申请提交成功!')
            this.getList()
            this.loadOverviewStatistics()
          } else {
            this.$message.error(response?.msg || '提交失败')
          }
        }).catch(error => {
          console.error('提交失败:', error)
          this.$message.error('提交失败，请稍后重试')
        })
      }).catch(() => {})
    },
    handleApprove(row) {
      this.currentLease = row
      this.isRejectMode = false
      this.approvalDialogVisible = true
    },
    handleContract(row) {
      const leaseId = row.leaseId || row.id
      if (!leaseId) {
        this.$message.error('租赁ID不能为空')
        return
      }
      this.$confirm('确认签署该租赁合同?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        activateFinancialLease(leaseId).then(response => {
          if (response && (response.code === 1 || response.code === 200)) {
            this.$message.success('合同签署成功!')
            this.getList()
            this.loadOverviewStatistics()
          } else {
            this.$message.error(response?.msg || '签署失败')
          }
        }).catch(error => {
          console.error('签署失败:', error)
          this.$message.error('签署失败，请稍后重试')
        })
      }).catch(() => {})
    },
    // 将数据库字段名映射到前端表单字段名
    mapRowToFormData(row) {
      const formData = { ...row }
      // ID 映射：leaseId → id
      if (row.leaseId && !row.id) {
        formData.id = row.leaseId
      }
      // 租赁类型：leaseType → leasingType
      if (row.leaseType && !row.leasingType) {
        formData.leasingType = row.leaseType
      }
      // 租赁公司：lessorName → leasingCompany
      if (row.lessorName && !row.leasingCompany) {
        formData.leasingCompany = row.lessorName
      }
      // 租赁状态：leaseStatus → applicationStatus
      if (row.leaseStatus && !row.applicationStatus) {
        formData.applicationStatus = row.leaseStatus
      }
      // 利率：interestRate（小数）→ leaseRate（百分比）
      if (row.interestRate != null && !row.leaseRate) {
        formData.leaseRate = parseFloat((row.interestRate * 100).toFixed(2))
      }
      // 日期格式转换：时间戳 → yyyy-MM-dd
      if (row.startDate && typeof row.startDate === 'number') {
        formData.startDate = this.formatDateToString(row.startDate)
      }
      if (row.endDate && typeof row.endDate === 'number') {
        formData.endDate = this.formatDateToString(row.endDate)
      }
      return formData
    },
    // 时间戳转换为 yyyy-MM-dd 格式字符串
    formatDateToString(timestamp) {
      if (!timestamp) return ''
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          // 编辑时映射字段名
          this.currentLease = this.mapRowToFormData(row)
          this.isEditMode = true
          this.formDialogVisible = true
          break
        case 'copy':
          // 复制时清除ID，作为新增处理，同样需要映射字段
          this.currentLease = { ...this.mapRowToFormData(row), id: null, leaseNo: '' }
          this.isEditMode = false
          this.formDialogVisible = true
          break
        case 'asset':
          // 兼容两种字段名：后端返回 leaseId，前端可能使用 id
          this.currentLeaseId = row.leaseId || row.id
          this.assetDialogVisible = true
          break
        case 'payment':
          // 兼容两种字段名：后端返回 leaseId，前端可能使用 id
          this.currentLeaseId = row.leaseId || row.id
          this.paymentDialogVisible = true
          break
        case 'return':
          // 兼容两种字段名：后端返回 leaseId，前端可能使用 id
          this.currentLeaseId = row.leaseId || row.id
          this.returnDialogVisible = true
          break
      }
    },
    handleImport() {
      this.importDialogVisible = true
    },
    handleExport() {
      this.$confirm('确认导出融资租赁数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.listLoading = true
        const params = {
          leasingType: this.listQuery.leasingType,
          applicationStatus: this.listQuery.applicationStatus,
          leasingCompany: this.listQuery.leasingCompany,
          leaseNo: this.listQuery.applicationNo
        }
        exportFinancialLease(params).then(response => {
          // 处理blob响应并触发下载
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', '融资租赁数据_' + new Date().getTime() + '.xlsx')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }).catch(error => {
          console.error('导出失败:', error)
          this.$message.error('导出失败，请稍后重试')
        }).finally(() => {
          this.listLoading = false
        })
      }).catch(() => {
        // 用户取消
      })
    },
    handleDownloadTemplate() {
      downloadFinancialLeaseTemplate().then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '融资租赁导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      }).catch(error => {
        console.error('下载模板失败:', error)
        this.$message.error('下载模板失败，请稍后重试')
      })
    },
    // 弹窗回调方法
    handleFormSuccess() {
      this.getList()
      this.loadOverviewStatistics()
      this.loadTypeDistribution()
    },
    handleEditFromDetail(data) {
      // 从详情页编辑时也需要映射字段
      this.currentLease = this.mapRowToFormData(data)
      this.isEditMode = true
      this.formDialogVisible = true
    },
    handleApprovalSuccess() {
      this.getList()
      this.loadOverviewStatistics()
    },
    handleImportSuccess() {
      this.getList()
      this.loadOverviewStatistics()
      this.loadTypeDistribution()
    },
    handleReturnSuccess() {
      this.getList()
      this.loadOverviewStatistics()
      this.$message.success('退租申请已提交')
    },
    getLeasingTypeTagType(type) {
      const typeMap = {
        'DIRECT': 'primary',
        'LEASEBACK': 'success',
        'LEVERAGED': 'warning',
        'OPERATING': 'info',
        'FINANCE_LEASE': 'primary',
        'OPERATING_LEASE': 'success',
        'SALE_LEASEBACK': 'warning'
      }
      return typeMap[type] || 'default'
    },
    getLeasingTypeText(type) {
      const textMap = {
        'DIRECT': '直接租赁',
        'LEASEBACK': '售后回租',
        'LEVERAGED': '杠杆租赁',
        'OPERATING': '经营租赁',
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
    getLeaseStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'primary',
        'ACTIVE': 'success',
        'COMPLETED': '',
        'REJECTED': 'danger',
        'CANCELLED': 'default'
      }
      return typeMap[status] || 'info'
    },
    getLeaseStatusText(status) {
      const textMap = {
        'PENDING': '待提交',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'ACTIVE': '执行中',
        'COMPLETED': '已完成',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    // 保留旧方法以兼容其他地方的调用
    getApplicationStatusTagType(status) {
      return this.getLeaseStatusTagType(status)
    },
    getApplicationStatusText(status) {
      return this.getLeaseStatusText(status)
    },
    formatDate(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
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

  .asset-value, .leasing-amount, .monthly-rent, .outstanding-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .outstanding-amount {
    color: #E6A23C;
  }
}
</style>
