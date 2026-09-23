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
        <el-table-column label="监控ID" prop="monitoringId" width="180" align="center" show-overflow-tooltip />
        <el-table-column label="公司名称" prop="companyName" width="150" align="center" show-overflow-tooltip />
        <el-table-column label="预警类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertTypeTagType(row.alertType)" size="mini">
              {{ getAlertTypeText(row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警级别" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertLevelTagType(row.alertLevel)" size="mini">
              {{ getAlertLevelText(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警内容" prop="alertMessage" width="200" align="center" show-overflow-tooltip />
        <el-table-column label="预警状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertStatusTagType(row.alertStatus)" size="mini">
              {{ getAlertStatusText(row.alertStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警日期" width="160px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateTime(row.alertDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理人" prop="handlerName" width="100" align="center">
          <template slot-scope="{row}">
            <span>{{ row.handlerName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处理意见" prop="handleOpinion" width="150" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.handleOpinion || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" width="150" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.remark || '-' }}</span>
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

    <!-- 监控详情弹窗 -->
    <monitoring-detail-dialog
      :visible.sync="detailDialogVisible"
      :monitoring-id="currentMonitoringId"
      @process="handleProcessFromDetail"
    />

    <!-- 预警处理弹窗 -->
    <alert-process-dialog
      :visible.sync="processDialogVisible"
      :alert-data="currentAlertData"
      @success="handleProcessSuccess"
    />

    <!-- 更新监控弹窗 -->
    <monitoring-update-dialog
      :visible.sync="updateDialogVisible"
      :monitoring-data="currentMonitoringData"
      @success="handleUpdateSuccess"
    />

    <!-- 预警设置弹窗 -->
    <alert-setting-dialog
      :visible.sync="settingDialogVisible"
      :monitoring-data="currentMonitoringData"
      @success="handleSettingSuccess"
    />

    <!-- 提醒处理弹窗 -->
    <reminder-process-dialog
      :visible.sync="reminderDialogVisible"
      :reminder-data="currentReminderData"
      @success="handleReminderSuccess"
    />

    <!-- 历史记录弹窗 -->
    <monitoring-history-dialog
      :visible.sync="historyDialogVisible"
      :monitoring-data="currentMonitoringData"
    />
  </div>
</template>

<script>
import {
  getFinancingMonitoringPage,
  getFinancingMonitoringOverview,
  getRiskAlertList,
  getUpcomingPaymentList,
  getFinancingTypeChartData,
  getRiskLevelChartData,
  getComplianceChartData,
  exportFinancingMonitoringReport
} from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'
import MonitoringDetailDialog from './components/MonitoringDetailDialog.vue'
import AlertProcessDialog from './components/AlertProcessDialog.vue'
import MonitoringUpdateDialog from './components/MonitoringUpdateDialog.vue'
import AlertSettingDialog from './components/AlertSettingDialog.vue'
import ReminderProcessDialog from './components/ReminderProcessDialog.vue'
import MonitoringHistoryDialog from './components/MonitoringHistoryDialog.vue'

export default {
  name: 'FinancingMonitoringManage',
  components: {
    Pagination,
    MonitoringDetailDialog,
    AlertProcessDialog,
    MonitoringUpdateDialog,
    AlertSettingDialog,
    ReminderProcessDialog,
    MonitoringHistoryDialog
  },
  data() {
    return {
      listLoading: false,
      total: 0,
      detailDialogVisible: false,
      processDialogVisible: false,
      updateDialogVisible: false,
      settingDialogVisible: false,
      reminderDialogVisible: false,
      historyDialogVisible: false,
      currentMonitoringId: null,
      currentAlertData: {},
      currentMonitoringData: {},
      currentReminderData: {},
      listQuery: {
        page: 1,
        limit: 20,
        financingType: undefined,
        riskRating: undefined,
        covenantCompliance: undefined,
        alertStatus: undefined,
        monitoringDateRange: undefined
      },
      // 融资监控概览数据 - 将通过API获取
      totalFinancing: 0,
      activeFinancing: 0,
      totalOutstanding: 0,
      riskAlerts: 0,
      upcomingPayments: 0,
      complianceRate: 0,
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
    this.getMonitoringOverview()
    // 使用 $nextTick 确保 DOM 渲染完成后再初始化图表
    this.$nextTick(() => {
      this.initCharts()
    })
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
      // 调用真实API获取数据 - 使用正确的listQuery参数
      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        financingType: this.listQuery.financingType || undefined,
        alertStatus: this.listQuery.alertStatus || undefined,
        riskRating: this.listQuery.riskRating || undefined,
        covenantCompliance: this.listQuery.covenantCompliance || undefined
      }

      // 处理日期范围
      if (this.listQuery.monitoringDateRange && this.listQuery.monitoringDateRange.length === 2) {
        params.startDate = this.listQuery.monitoringDateRange[0]
        params.endDate = this.listQuery.monitoringDateRange[1]
      }

      getFinancingMonitoringPage(params).then(response => {
        // 兼容多种响应格式 code: 1, 200, '1', '200'
        if (response && [1, 200, '1', '200'].includes(response.code)) {
          // 处理分页数据 - 兼容多种数据结构: rows, tlist, list, records
          this.monitoringList = response.data.rows || response.data.tlist || response.data.list || response.data.records || []
          this.total = parseInt(response.data.total) || response.data.totalRecord || 0
        } else {
          this.monitoringList = []
          this.total = 0
          this.$message.error(response?.msg || response?.message || '查询融资监控数据失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('查询融资监控数据异常:', error)
        this.monitoringList = []
        this.total = 0
        this.listLoading = false
        this.$message.error('查询融资监控数据失败，请稍后重试')
      })
    },
    getOverviewData() {
      // 调用真实API获取预警数据
      getRiskAlertList({ pageSize: 10 }).then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code)) {
          const data = response.data?.rows || response.data?.tlist || response.data?.list || response.data?.records || response.data || []
          this.riskAlertList = Array.isArray(data) ? data : []
        } else {
          this.riskAlertList = []
          console.warn('获取风险预警数据失败:', response?.msg || response?.message)
        }
      }).catch(error => {
        console.error('获取风险预警数据异常:', error)
        this.riskAlertList = []
      })

      // 调用真实API获取到期提醒数据
      getUpcomingPaymentList({ pageSize: 10 }).then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code)) {
          const data = response.data?.rows || response.data?.tlist || response.data?.list || response.data?.records || response.data || []
          this.upcomingPaymentList = Array.isArray(data) ? data : []
        } else {
          this.upcomingPaymentList = []
          console.warn('获取到期提醒数据失败:', response?.msg || response?.message)
        }
      }).catch(error => {
        console.error('获取到期提醒数据异常:', error)
        this.upcomingPaymentList = []
      })
    },
    // 获取监控概览数据
    getMonitoringOverview() {
      getFinancingMonitoringOverview().then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code) && response.data) {
          const overviewData = response.data
          this.totalFinancing = overviewData.TOTALFINANCING || overviewData.totalFinancing || overviewData.total || 0
          this.activeFinancing = overviewData.ACTIVEFINANCING || overviewData.activeFinancing || overviewData.active || 0
          this.totalOutstanding = overviewData.TOTALOUTSTANDING || overviewData.totalOutstanding || overviewData.outstanding || 0
          this.riskAlerts = overviewData.RISKALERTS || overviewData.riskAlerts || overviewData.alerts || 0
          this.upcomingPayments = overviewData.UPCOMINGPAYMENTS || overviewData.upcomingPayments || overviewData.upcoming || 0
          this.complianceRate = overviewData.COMPLIANCERATE || overviewData.complianceRate || overviewData.compliance || 0
        } else {
          console.warn('获取监控概览数据失败:', response?.msg || response?.message)
        }
      }).catch(error => {
        console.error('获取监控概览数据异常:', error)
      })
    },

    initCharts() {
      const echarts = require('echarts')

      // 初始化融资类型监控图表
      const financingTypeEl = document.getElementById('financingTypeMonitorChart')
      if (financingTypeEl) {
        this.financingTypeChart = echarts.init(financingTypeEl)
        this.updateFinancingTypeChart()
      }

      // 初始化风险等级图表
      const riskLevelEl = document.getElementById('riskLevelChart')
      if (riskLevelEl) {
        this.riskLevelChart = echarts.init(riskLevelEl)
        this.updateRiskLevelChart()
      }

      // 初始化合规状态图表
      const complianceEl = document.getElementById('complianceChart')
      if (complianceEl) {
        this.complianceChart = echarts.init(complianceEl)
        this.updateComplianceChart()
      }
    },
    // 兼容Oracle大写和小写key，统一取值
    normalizeChartItem(item) {
      return {
        name: item.name || item.NAME || item.typeName || item.TYPENAME || item.label || item.LABEL || '未知',
        value: parseInt(item.value || item.VALUE || item.count || item.COUNT || item.amount || item.AMOUNT || 0),
        code: item.code || item.CODE || item.type || item.TYPE || item.typeCode || item.TYPECODE || ''
      }
    },
    updateFinancingTypeChart() {
      getFinancingTypeChartData().then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code) && response.data) {
          const list = Array.isArray(response.data) ? response.data : []
          this.renderFinancingTypeChart(list)
        } else {
          this.renderFinancingTypeChart([])
        }
      }).catch(error => {
        console.error('获取融资类型图表数据失败:', error)
        this.renderFinancingTypeChart([])
      })
    },
    renderFinancingTypeChart(apiData) {
      if (!this.financingTypeChart) return
      const colorMap = {
        'BANK_LOAN': '#409EFF', 'BOND': '#67C23A', 'LEASING': '#E6A23C',
        'COMMERCIAL_PAPER': '#F56C6C', 'TRUST': '#909399',
        '信用': '#409EFF', '抵押': '#67C23A', '质押': '#E6A23C',
        '保证': '#F56C6C', '组合': '#909399'
      }
      const defaultColors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9B59B6']
      const data = (apiData && apiData.length > 0 ? apiData : [{ name: '暂无数据', value: 1, code: '' }]).map((item, idx) => {
        const norm = this.normalizeChartItem(item)
        return {
          name: norm.name,
          value: norm.value,
          itemStyle: { color: colorMap[norm.code] || defaultColors[idx % defaultColors.length] }
        }
      })
      this.financingTypeChart.setOption({
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c}个 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 12 } },
        series: [{ name: '融资类型', type: 'pie', radius: ['40%', '70%'], center: ['40%', '50%'], label: { show: false }, data: data }]
      })
    },
    updateRiskLevelChart() {
      getRiskLevelChartData().then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code) && response.data) {
          const list = Array.isArray(response.data) ? response.data : []
          this.renderRiskLevelChart(list)
        } else {
          this.renderRiskLevelChart([])
        }
      }).catch(error => {
        console.error('获取风险等级图表数据失败:', error)
        this.renderRiskLevelChart([])
      })
    },
    renderRiskLevelChart(apiData) {
      if (!this.riskLevelChart) return
      const colorMap = { 'LOW': '#67C23A', 'MEDIUM': '#E6A23C', 'HIGH': '#F56C6C' }
      const defaultColors = ['#67C23A', '#E6A23C', '#F56C6C']
      const data = (apiData && apiData.length > 0 ? apiData : [{ name: '暂无数据', value: 1, code: '' }]).map((item, idx) => {
        const norm = this.normalizeChartItem(item)
        return {
          name: norm.name,
          value: norm.value,
          itemStyle: { color: colorMap[norm.code] || defaultColors[idx % defaultColors.length] }
        }
      })
      this.riskLevelChart.setOption({
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c}个 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 12 } },
        series: [{ name: '风险等级', type: 'pie', radius: ['40%', '70%'], center: ['40%', '50%'], label: { show: false }, data: data }]
      })
    },
    updateComplianceChart() {
      getComplianceChartData().then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code) && response.data) {
          const list = Array.isArray(response.data) ? response.data : []
          this.renderComplianceChart(list)
        } else {
          this.renderComplianceChart([])
        }
      }).catch(error => {
        console.error('获取合规状态图表数据失败:', error)
        this.renderComplianceChart([])
      })
    },
    renderComplianceChart(apiData) {
      if (!this.complianceChart) return
      const colorMap = {
        'COMPLIANT': '#67C23A', 'CLOSED': '#67C23A',
        'WARNING': '#E6A23C', 'PENDING': '#E6A23C',
        'BREACH': '#F56C6C', 'HANDLED': '#F56C6C'
      }
      const defaultColors = ['#67C23A', '#E6A23C', '#F56C6C']
      const data = (apiData && apiData.length > 0 ? apiData : [{ name: '暂无数据', value: 1, code: '' }]).map((item, idx) => {
        const norm = this.normalizeChartItem(item)
        return {
          name: norm.name,
          value: norm.value,
          itemStyle: { color: colorMap[norm.code] || defaultColors[idx % defaultColors.length] }
        }
      })
      this.complianceChart.setOption({
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c}个 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 12 } },
        series: [{ name: '合规状态', type: 'pie', radius: ['40%', '70%'], center: ['40%', '50%'], label: { show: false }, data: data }]
      })
    },
    refreshData() {
      this.getList()
      this.getOverviewData()
      this.getMonitoringOverview()
      // 刷新图表数据
      this.updateFinancingTypeChart()
      this.updateRiskLevelChart()
      this.updateComplianceChart()
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
      // 使用弹窗组件显示监控详情
      this.currentMonitoringId = row.monitoringId
      this.detailDialogVisible = true
    },
    handleUpdateMonitoring(row) {
      // 打开预警处理弹窗
      this.currentAlertData = row
      this.processDialogVisible = true
    },
    handleProcessFromDetail(alertData) {
      // 从详情弹窗触发处理
      this.detailDialogVisible = false
      this.currentAlertData = alertData
      this.processDialogVisible = true
    },
    handleProcessSuccess() {
      // 处理成功后刷新列表
      this.getList()
      this.getOverviewData()
      this.$message.success('预警处理成功')
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'alert':
          // 打开预警设置弹窗
          this.currentMonitoringData = row
          this.settingDialogVisible = true
          break
        case 'report':
          this.$message({ type: 'info', message: `生成报告功能 - 监控ID: ${row.monitoringId}` })
          break
        case 'history':
          // 打开历史记录弹窗
          this.currentMonitoringData = row
          this.historyDialogVisible = true
          break
      }
    },
    // 更新监控成功回调
    handleUpdateSuccess() {
      this.getList()
      this.getOverviewData()
      this.$message.success('监控数据更新成功')
    },
    // 预警设置成功回调
    handleSettingSuccess() {
      this.$message.success('预警设置保存成功')
    },
    // 提醒处理成功回调
    handleReminderSuccess() {
      this.getOverviewData()
      this.$message.success('提醒处理成功')
    },
    handleAlerts() {
      // 跳转到预警管理页面或打开预警管理弹窗
      this.$router.push('/globalTreasurer-new/financingManagement/riskAlert').catch(() => {
        // 如果路由不存在，显示提示
        this.$message({
          type: 'info',
          message: '预警管理功能开发中，敬请期待'
        })
      })
    },
    handleExport() {
      // 导出监控报告
      this.$confirm('确定要导出融资监控报告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在导出报告...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        const params = {
          financingType: this.listQuery.financingType,
          riskRating: this.listQuery.riskRating,
          alertStatus: this.listQuery.alertStatus,
          covenantCompliance: this.listQuery.covenantCompliance
        }

        exportFinancingMonitoringReport(params).then(response => {
          loading.close()
          // 处理blob响应
          const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
          const fileName = `融资监控报告_${new Date().toISOString().slice(0, 10)}.xlsx`
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          URL.revokeObjectURL(link.href)
          this.$message({
            type: 'success',
            message: '监控报告导出成功'
          })
        }).catch(error => {
          loading.close()
          console.error('导出报告失败:', error)
          this.$message({
            type: 'error',
            message: '导出报告失败，请稍后重试'
          })
        })
      }).catch(() => {
        // 取消导出
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
    // 预警类型标签样式
    getAlertTypeTagType(type) {
      const typeMap = {
        'INTEREST_RATE': 'warning',
        'EXPIRATION': 'danger',
        'COMPLIANCE': 'info',
        'PAYMENT': 'primary',
        'RISK': 'danger'
      }
      return typeMap[type] || 'default'
    },
    // 预警类型文本
    getAlertTypeText(type) {
      const textMap = {
        'INTEREST_RATE': '利率预警',
        'EXPIRATION': '到期预警',
        'COMPLIANCE': '合规预警',
        'PAYMENT': '付款预警',
        'RISK': '风险预警'
      }
      return textMap[type] || type || '-'
    },
    // 预警级别标签样式
    getAlertLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return typeMap[level] || 'info'
    },
    // 预警级别文本
    getAlertLevelText(level) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高'
      }
      return textMap[level] || level || '-'
    },
    // 预警状态标签样式
    getAlertStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'HANDLED': 'success',
        'CLOSED': 'info',
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger'
      }
      return typeMap[status] || 'default'
    },
    // 预警状态文本
    getAlertStatusText(status) {
      const textMap = {
        'PENDING': '待处理',
        'HANDLED': '已处理',
        'CLOSED': '已关闭',
        'NORMAL': '正常',
        'WARNING': '预警',
        'CRITICAL': '严重'
      }
      return textMap[status] || status || '-'
    },
    // 格式化日期时间
    formatDateTime(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
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

  .empty-data {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40px 20px;
    color: #909399;

    i {
      font-size: 48px;
      margin-bottom: 12px;
      color: #dcdfe6;
    }

    p {
      margin: 0;
      font-size: 14px;
    }
  }
}
</style>
