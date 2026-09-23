<template>
  <div class="financial-risk-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-warning"></i><span>财务风险</span></div>
      <div class="page-header-desc">识别与评估企业财务风险等级、预警指标与应对措施</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-warning" style="color: #eb2f96;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalRisks }}</div>
              <div class="stats-label">风险识别总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-error" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.highRisks }}</div>
              <div class="stats-label">高风险企业</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-bell" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.activeAlerts }}</div>
              <div class="stats-label">活跃预警</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-data-analysis" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.avgRiskScore }}%</div>
              <div class="stats-label">平均风险评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 风险分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>风险等级分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshRiskChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="riskDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>风险趋势分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTrendChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="riskTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>风险类型分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTypeChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="riskTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>行业风险对比</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshIndustryChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="industryRiskChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="mb-20">
      <div slot="header">
        <span>查询条件</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="resetSearchForm"
        >
          重置
        </el-button>
      </div>
      
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业名称">
          <el-input
            v-model="searchForm.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>
        
        <el-form-item label="风险类型">
          <el-select
            v-model="searchForm.riskType"
            placeholder="请选择风险类型"
            clearable
            style="width: 150px;"
          >
            <el-option label="流动性风险" value="LIQUIDITY_RISK"></el-option>
            <el-option label="偿债风险" value="SOLVENCY_RISK"></el-option>
            <el-option label="盈利风险" value="PROFITABILITY_RISK"></el-option>
            <el-option label="营运风险" value="OPERATIONAL_RISK"></el-option>
            <el-option label="市场风险" value="MARKET_RISK"></el-option>
            <el-option label="信用风险" value="CREDIT_RISK"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
            <el-option label="极高风险" value="CRITICAL"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="识别状态">
          <el-select
            v-model="searchForm.identificationStatus"
            placeholder="请选择识别状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="待识别" value="PENDING"></el-option>
            <el-option label="识别中" value="IDENTIFYING"></el-option>
            <el-option label="已识别" value="IDENTIFIED"></el-option>
            <el-option label="识别失败" value="FAILED"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="预警状态">
          <el-select
            v-model="searchForm.alertStatus"
            placeholder="请选择预警状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="预警" value="WARNING"></el-option>
            <el-option label="警报" value="ALERT"></el-option>
            <el-option label="紧急" value="EMERGENCY"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险评分">
          <el-input
            v-model="searchForm.minRiskScore"
            placeholder="最小评分"
            style="width: 100px;"
          />
          <span style="margin: 0 10px;">-</span>
          <el-input
            v-model="searchForm.maxRiskScore"
            placeholder="最大评分"
            style="width: 100px;"
          />
        </el-form-item>
        
        <el-form-item label="识别时间">
          <el-date-picker
            v-model="searchForm.identificationDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px;"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="resetSearchForm">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="mb-20">
      <el-button type="primary" @click="handleAdd">
        <i class="el-icon-plus"></i> 新增识别
      </el-button>
      <el-button
        type="success"
        :disabled="selectedRisks.length === 0"
        @click="handleBatchIdentify"
      >
        <i class="el-icon-search"></i> 批量识别
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedRisks.length === 0"
        @click="handleBatchAssess"
      >
        <i class="el-icon-warning"></i> 批量评估
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 风险报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>财务风险识别列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="risksList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column label="企业名称" prop="enterpriseName" min-width="180">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.enterpriseName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column label="报告期间" prop="period" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="success" size="mini">{{ scope.row.period || '-' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="风险类型" prop="riskType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="danger" size="mini">{{ scope.row.riskType || '-' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="风险指标" prop="indicatorName" min-width="160">
          <template slot-scope="scope">
            {{ scope.row.indicatorName || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="检测方法" prop="detectionMethod" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ scope.row.detectionMethod || '-' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTag(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="风险评分" prop="riskScore" width="100" align="center">
          <template slot-scope="scope">
            <span :style="{ color: getRiskScoreColor(scope.row.riskScore), fontWeight: 'bold' }">
              {{ scope.row.riskScore != null ? scope.row.riskScore : '-' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="创建时间" prop="createTime" width="160" align="center">
          <template slot-scope="scope">
            {{ scope.row.createTime ? scope.row.createTime.substring(0, 16) : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleIdentify(scope.row)">识别</el-button>
            <el-button size="mini" type="warning" @click="handleAssess(scope.row)">评估</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="predict">风险预测</el-dropdown-item>
                <el-dropdown-item command="monitor">风险监控</el-dropdown-item>
                <el-dropdown-item command="mitigation">风险缓解</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <Pagination
          v-show="total > 0"
          :total="total"
          :page.sync="searchForm.pageNum"
          :limit.sync="searchForm.pageSize"
          @pagination="getRisksList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <FinancialRiskDialog
      :visible.sync="riskDialogVisible"
      :risk-data="currentRisk"
      :dialog-type="dialogType"
      @refresh="getRisksList"
    />
    
    <FinancialRiskIdentificationDialog
      :visible.sync="identificationDialogVisible"
      :risk-data="currentRisk"
      @refresh="getRisksList"
    />

    <FinancialRiskAssessmentDialog
      :visible.sync="assessmentDialogVisible"
      :risk-data="currentRisk"
      @refresh="getRisksList"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import FinancialRiskDialog from './components/FinancialRiskDialog'
import FinancialRiskIdentificationDialog from './components/FinancialRiskIdentificationDialog'
import FinancialRiskAssessmentDialog from './components/FinancialRiskAssessmentDialog'
import {
  getFinancialRiskList,
  getFinancialRiskStatistics,
  getFinancialRiskCharts,
  deleteFinancialRisk,
  exportFinancialRiskData,
  identifyFinancialRisk,
  assessFinancialRisk,
  batchIdentifyFinancialRisk,
  batchAssessFinancialRisk,
  generateFinancialRiskReport,
  predictFinancialRisk,
  monitorFinancialRisk,
  mitigateFinancialRisk
} from '@/api/stateAssets/financialRisk'

export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'FinancialRisk',
  components: {
    Pagination,
    FinancialRiskDialog,
    FinancialRiskIdentificationDialog,
    FinancialRiskAssessmentDialog
  },
  data() {
    return {
      loading: false,
      risksList: [],
      selectedRisks: [],
      total: 0,
      statistics: {
        totalRisks: 0,
        highRisks: 0,
        activeAlerts: 0,
        avgRiskScore: 0
      },
      searchForm: {
        enterpriseName: '',
        riskType: '',
        riskLevel: '',
        identificationStatus: '',
        alertStatus: '',
        minRiskScore: '',
        maxRiskScore: '',
        identificationDateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      riskDialogVisible: false,
      identificationDialogVisible: false,
      assessmentDialogVisible: false,
      currentRisk: {},
      dialogType: 'add',
      // 图表实例
      riskDistributionChart: null,
      riskTrendChart: null,
      riskTypeChart: null,
      industryRiskChart: null
    }
  },
  created() {
    this.getStatistics()
    this.getRisksList()
  },
  mounted() {
    this.initCharts()
    this.loadChartData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getFinancialRiskStatistics()
        this.statistics = response.data
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 获取风险列表
    async getRisksList() {
      this.loading = true
      try {
        const params = { ...this.searchForm }
        // 格式化日期范围
        if (params.identificationDateRange && params.identificationDateRange.length === 2) {
          const formatDate = (d) => {
            if (!d) return ''
            const date = new Date(d)
            return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0')
          }
          params.identificationDateRange = [
            formatDate(params.identificationDateRange[0]),
            formatDate(params.identificationDateRange[1])
          ]
        } else {
          delete params.identificationDateRange
        }
        const response = await getFinancialRiskList(params)
        this.risksList = response.data.list
        this.total = response.data.total
      } catch (error) {
        console.error('获取风险列表失败:', error)
        this.risksList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initCharts() {
      this.riskDistributionChart = echarts.init(this.$refs.riskDistributionChart)
      this.riskTrendChart = echarts.init(this.$refs.riskTrendChart)
      this.riskTypeChart = echarts.init(this.$refs.riskTypeChart)
      this.industryRiskChart = echarts.init(this.$refs.industryRiskChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    async loadChartData() {
      try {
        const response = await getFinancialRiskCharts()
        const chartData = response.data
        
        this.updateRiskDistributionChart(chartData.riskDistribution)
        this.updateRiskTrendChart(chartData.riskTrend)
        this.updateRiskTypeChart(chartData.riskType)
        this.updateIndustryRiskChart(chartData.industryRisk)
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },

    // 更新风险等级分布图表
    updateRiskDistributionChart(data) {
      const option = {
        title: {
          text: '风险等级分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '风险等级',
          type: 'pie',
          radius: '60%',
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.riskDistributionChart.setOption(option)
    },

    // 更新风险趋势图表
    updateRiskTrendChart(data) {
      const option = {
        title: {
          text: '风险趋势分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['高风险', '中风险', '低风险'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value',
          name: '风险数量'
        },
        series: [
          {
            name: '高风险',
            type: 'line',
            data: data.map(item => item.high),
            smooth: true,
            itemStyle: { color: '#F56C6C' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
                { offset: 1, color: 'rgba(245, 108, 108, 0.1)' }
              ])
            }
          },
          {
            name: '中风险',
            type: 'line',
            data: data.map(item => item.medium),
            smooth: true,
            itemStyle: { color: '#E6A23C' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
                { offset: 1, color: 'rgba(230, 162, 60, 0.1)' }
              ])
            }
          },
          {
            name: '低风险',
            type: 'line',
            data: data.map(item => item.low),
            smooth: true,
            itemStyle: { color: '#67C23A' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
              ])
            }
          }
        ]
      }
      this.riskTrendChart.setOption(option)
    },

    // 更新风险类型图表
    updateRiskTypeChart(data) {
      const option = {
        title: {
          text: '风险类型分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name),
          axisLabel: { rotate: 45 }
        },
        yAxis: {
          type: 'value',
          name: '风险数量'
        },
        series: [{
          name: '风险数量',
          type: 'bar',
          data: data.map(item => item.value),
          itemStyle: {
            color: (params) => {
              const colors = ['#F56C6C', '#E6A23C', '#409EFF', '#67C23A', '#909399', '#C0C4CC']
              return colors[params.dataIndex % colors.length]
            }
          }
        }]
      }
      this.riskTypeChart.setOption(option)
    },

    // 更新行业风险对比图表
    updateIndustryRiskChart(data) {
      const option = {
        title: {
          text: '行业风险对比',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.industry)
        },
        yAxis: {
          type: 'value',
          name: '平均风险评分'
        },
        series: [{
          name: '风险评分',
          type: 'bar',
          data: data.map(item => item.score),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      }
      this.industryRiskChart.setOption(option)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getRisksList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        riskType: '',
        riskLevel: '',
        identificationStatus: '',
        alertStatus: '',
        minRiskScore: '',
        maxRiskScore: '',
        identificationDateRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getRisksList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRisks = selection
    },

    // 新增识别
    handleAdd() {
      this.currentRisk = {}
      this.dialogType = 'add'
      this.riskDialogVisible = true
    },

    // 编辑风险
    handleEdit(row) {
      this.currentRisk = { ...row }
      this.dialogType = 'edit'
      this.riskDialogVisible = true
    },

    // 查看风险
    handleView(row) {
      this.currentRisk = { ...row }
      this.dialogType = 'view'
      this.riskDialogVisible = true
    },

    // 识别风险
    handleIdentify(row) {
      this.currentRisk = { ...row }
      this.identificationDialogVisible = true
    },

    // 评估风险
    handleAssess(row) {
      this.currentRisk = { ...row }
      this.assessmentDialogVisible = true
    },

    // 批量识别
    handleBatchIdentify() {
      if (this.selectedRisks.length === 0) {
        this.$message.warning('请选择要识别的风险')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedRisks.length} 条风险进行批量识别？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedRisks.map(item => item.id)
          const response = await batchIdentifyFinancialRisk({ ids, status: 'CONFIRMED' })
          const data = response.data
          this.$message.success(`批量识别完成：成功 ${data.successCount} 条，失败 ${data.failCount} 条`)
          this.getRisksList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('批量识别失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 批量评估
    handleBatchAssess() {
      if (this.selectedRisks.length === 0) {
        this.$message.warning('请选择要评估的风险')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedRisks.length} 条风险进行批量评估？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedRisks.map(item => item.id)
          const response = await batchAssessFinancialRisk({ ids })
          const data = response.data
          this.$message.success(`批量评估完成：成功 ${data.successCount} 条，失败 ${data.failCount} 条`)
          this.getRisksList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('批量评估失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 导出数据
    async handleExport() {
      try {
        const response = await exportFinancialRiskData(this.searchForm)
        const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '财务风险识别数据_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败: ' + (error.message || '请稍后重试'))
      }
    },

    // 生成报告
    handleGenerateReport() {
      this.$confirm('确认生成本期财务风险识别报告？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const response = await generateFinancialRiskReport(this.searchForm)
          const report = response.data
          this.$alert(
            `<div style="line-height:2">
              <p><b>报告ID：</b>${report.reportId}</p>
              <p><b>生成时间：</b>${report.generateTime}</p>
              <p><b>风险总数：</b>${report.totalRisks}</p>
              <p><b>高风险：</b>${report.highRisks} | <b>中风险：</b>${report.mediumRisks} | <b>低风险：</b>${report.lowRisks}</p>
              <p><b>已确认：</b>${report.confirmedCount} | <b>待处理：</b>${report.pendingCount}</p>
              <p><b>平均风险评分：</b>${report.avgRiskScore}</p>
            </div>`,
            '风险报告',
            { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
          )
        } catch (error) {
          this.$message.error('报告生成失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'predict':
          this.handlePredict(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'mitigation':
          this.handleMitigation(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 风险预测
    async handlePredict(row) {
      this.$confirm('确认对该风险进行预测分析？', '风险预测', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const response = await predictFinancialRisk({ companyId: row.companyId, id: row.id })
          const prediction = response.data
          const trendText = { INCREASING: '上升趋势', DECREASING: '下降趋势', STABLE: '平稳' }
          this.$alert(
            `<div style="line-height:2">
              <p><b>趋势方向：</b>${trendText[prediction.trendDirection] || prediction.trendDirection}</p>
              <p><b>预测评分：</b>${prediction.predictedScore}</p>
              <p><b>预测等级：</b>${prediction.predictedLevel || '-'}</p>
              <p><b>置信度：</b>${prediction.confidence}%</p>
              <p><b>历史数据点：</b>${prediction.historicalDataPoints || 0}</p>
            </div>`,
            '风险预测结果',
            { dangerouslyUseHTMLString: true, confirmButtonText: '确定' }
          )
        } catch (error) {
          this.$message.error('风险预测失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 风险监控
    async handleMonitor(row) {
      this.$confirm('确认启动该风险的持续监控？', '风险监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          await monitorFinancialRisk({ id: row.id, action: 'START' })
          this.$message.success('风险监控已启动')
          this.getRisksList()
        } catch (error) {
          this.$message.error('启动监控失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 风险缓解
    async handleMitigation(row) {
      this.$prompt('请输入风险缓解措施说明', '风险缓解', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请描述缓解措施...'
      }).then(async ({ value }) => {
        try {
          await mitigateFinancialRisk({ id: row.id, notes: value || '' })
          this.$message.success('风险缓解措施已启动')
          this.getRisksList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('风险缓解失败：' + (error.message || '请稍后重试'))
        }
      }).catch(() => {})
    },

    // 删除风险
    handleDelete(row) {
      this.$confirm('确认删除该财务风险记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFinancialRisk({ riskId: row.id, id: row.id })
          this.$message.success('删除成功')
          this.getRisksList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 刷新图表
    refreshRiskChart() {
      this.loadChartData()
    },

    refreshTrendChart() {
      this.loadChartData()
    },

    refreshTypeChart() {
      this.loadChartData()
    },

    refreshIndustryChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.riskDistributionChart) this.riskDistributionChart.resize()
      if (this.riskTrendChart) this.riskTrendChart.resize()
      if (this.riskTypeChart) this.riskTypeChart.resize()
      if (this.industryRiskChart) this.industryRiskChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.riskDistributionChart) {
        this.riskDistributionChart.dispose()
        this.riskDistributionChart = null
      }
      if (this.riskTrendChart) {
        this.riskTrendChart.dispose()
        this.riskTrendChart = null
      }
      if (this.riskTypeChart) {
        this.riskTypeChart.dispose()
        this.riskTypeChart = null
      }
      if (this.industryRiskChart) {
        this.industryRiskChart.dispose()
        this.industryRiskChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 获取风险等级标签
    getRiskLevelTag(level) {
      const tagMap = { 'LOW': 'success', 'MEDIUM': 'warning', 'HIGH': 'danger', 'CRITICAL': 'danger' }
      return tagMap[level] || 'info'
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const textMap = { 'LOW': '低风险', 'MEDIUM': '中风险', 'HIGH': '高风险', 'CRITICAL': '极高风险' }
      return textMap[level] || level
    },

    // 获取风险评分颜色
    getRiskScoreColor(score) {
      if (score <= 30) return '#67C23A'
      if (score <= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = { 'PENDING': 'info', 'CONFIRMED': 'danger', 'IGNORED': 'success', 'MONITORING': 'warning', 'MITIGATED': 'success' }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = { 'PENDING': '待确认', 'CONFIRMED': '已确认', 'IGNORED': '已忽略', 'MONITORING': '监控中', 'MITIGATED': '已缓解' }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-ext-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff0f6 !important; }
::v-deep .el-card { border-radius: 6px; }
.mb-20 { margin-bottom: 20px; }
.stats-card {
  .stats-content { display: flex; align-items: center;
    .stats-icon { font-size: 40px; margin-right: 20px; }
    .stats-info {
      .stats-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
      .stats-label { font-size: 14px; color: #909399; margin-top: 5px; }
    }
  }
}
.chart-container { height: 300px; width: 100%; }
.table-count { color: #909399; font-size: 14px; }
.pagination-container { margin-top: 20px; text-align: right; }
</style>
