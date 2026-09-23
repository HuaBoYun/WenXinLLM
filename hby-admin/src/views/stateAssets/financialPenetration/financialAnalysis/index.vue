<template>
  <div class="financial-analysis-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-money"></i><span>财务分析</span></div>
      <div class="page-header-desc">穿透分析企业营收、利润、资产负债等核心财务指标</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-money" style="color: #eb2f96;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalRevenue }}</div>
              <div class="stats-label">总营收(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-coin" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalProfit }}</div>
              <div class="stats-label">总利润(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-wallet" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalAssets }}</div>
              <div class="stats-label">总资产(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-data-line" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.avgROE }}%</div>
              <div class="stats-label">平均ROE</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>营收利润趋势</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshRevenueChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="revenueProfitChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>财务指标分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshIndicatorChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="financialIndicatorChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>资产负债结构</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshAssetChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="assetLiabilityChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>现金流分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshCashFlowChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="cashFlowChart" class="chart-container"></div>
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

        <el-form-item label="报告期间">
          <el-input
            v-model="searchForm.period"
            placeholder="如 2024-01"
            clearable
            style="width: 130px;"
          />
        </el-form-item>

        <el-form-item label="分析类型">
          <el-select
            v-model="searchForm.statementType"
            placeholder="请选择类型"
            clearable
            style="width: 130px;"
          >
            <el-option label="合并报表" value="合并报表"></el-option>
            <el-option label="单体报表" value="单体报表"></el-option>
            <el-option label="年度报表" value="年度报表"></el-option>
            <el-option label="季度报表" value="季度报表"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="审计状态">
          <el-select
            v-model="searchForm.auditStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="已审计" value="已审计"></el-option>
            <el-option label="未审计" value="未审计"></el-option>
            <el-option label="已分析" value="已分析"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="营收范围">
          <el-input
            v-model="searchForm.minRevenue"
            placeholder="最小"
            style="width: 90px;"
          />
          <span style="margin: 0 5px;">-</span>
          <el-input
            v-model="searchForm.maxRevenue"
            placeholder="最大"
            style="width: 90px;"
          />
        </el-form-item>

        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.createTimeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
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
        <i class="el-icon-plus"></i> 新增分析
      </el-button>
      <el-button
        type="success"
        :disabled="selectedAnalyses.length === 0"
        @click="handleBatchAnalyze"
      >
        <i class="el-icon-data-analysis"></i> 批量分析
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedAnalyses.length === 0"
        @click="handleBatchCompare"
      >
        <i class="el-icon-scale-to-original"></i> 批量对比
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 财务报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>财务穿透分析列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="analysesList"
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
            <el-tag type="success" size="mini">
              {{ scope.row.period || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="营业收入" prop="revenue" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.revenue) }}万元
          </template>
        </el-table-column>
        
        <el-table-column label="净利润" prop="netProfit" width="120" align="center">
          <template slot-scope="scope">
            <span :class="getProfitClass(scope.row.netProfit)">
              {{ formatNumber(scope.row.netProfit) }}万元
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="总资产" prop="totalAssets" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.totalAssets) }}万元
          </template>
        </el-table-column>
        
        <el-table-column label="ROE" prop="roe" width="80" align="center">
          <template slot-scope="scope">
            <span :class="getROEClass(scope.row.roe)">
              {{ scope.row.roe }}%
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="净资产" prop="netAssets" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.netAssets) }}万元
          </template>
        </el-table-column>
        
        <el-table-column label="资产负债率" width="110" align="center">
          <template slot-scope="scope">
            <span :style="{ color: getDebtRatioColor(calcDebtRatio(scope.row)) }">
              {{ calcDebtRatio(scope.row) }}%
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="总负债" prop="totalLiabilities" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.totalLiabilities) }}万元
          </template>
        </el-table-column>

        <el-table-column label="分析类型" prop="analysisType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="primary" size="mini">
              {{ scope.row.analysisType || '-' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="审计状态" prop="auditStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.auditStatus === '已审计' ? 'success' : 'warning'" size="mini">
              {{ scope.row.auditStatus || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleAnalyze(scope.row)">分析</el-button>
            <el-button size="mini" type="success" @click="handleCompare(scope.row)">对比</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="trend">趋势分析</el-dropdown-item>
                <el-dropdown-item command="predict">预测分析</el-dropdown-item>
                <el-dropdown-item command="risk">风险评估</el-dropdown-item>
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
          @pagination="getAnalysesList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <FinancialAnalysisDialog
      :visible.sync="analysisDialogVisible"
      :analysis-data="currentAnalysis"
      :dialog-type="dialogType"
      @refresh="getAnalysesList"
    />
    
    <FinancialAnalysisDetailDialog
      :visible.sync="detailDialogVisible"
      :analysis-data="currentAnalysis"
    />
    
    <FinancialComparisonDialog
      :visible.sync="comparisonDialogVisible"
      :analysis-data="currentAnalysis"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import FinancialAnalysisDialog from './components/FinancialAnalysisDialog'
import FinancialAnalysisDetailDialog from './components/FinancialAnalysisDetailDialog'
import FinancialComparisonDialog from './components/FinancialComparisonDialog'
import {
  getFinancialAnalysisList,
  getFinancialAnalysisStatistics,
  getFinancialAnalysisCharts,
  deleteFinancialAnalysis,
  exportFinancialAnalysisData,
  batchAnalyzeFinancialData,
  generateFinancialAnalysisReport,
  getFinancialTrendAnalysis,
  getFinancialForecastAnalysis,
  getFinancialHealthAssessment
} from '@/api/stateAssets/financialAnalysis'

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
  name: 'FinancialAnalysis',
  components: {
    Pagination,
    FinancialAnalysisDialog,
    FinancialAnalysisDetailDialog,
    FinancialComparisonDialog
  },
  data() {
    return {
      loading: false,
      analysesList: [],
      selectedAnalyses: [],
      total: 0,
      statistics: {
        totalRevenue: 0,
        totalProfit: 0,
        totalAssets: 0,
        avgROE: 0
      },
      searchForm: {
        enterpriseName: '',
        period: '',
        statementType: '',
        auditStatus: '',
        minRevenue: '',
        maxRevenue: '',
        createTimeRange: [],
        pageNum: 1,
        pageSize: 10
      },
      analysisDialogVisible: false,
      detailDialogVisible: false,
      comparisonDialogVisible: false,
      currentAnalysis: {},
      dialogType: 'add',
      // 图表实例
      revenueProfitChart: null,
      financialIndicatorChart: null,
      assetLiabilityChart: null,
      cashFlowChart: null
    }
  },
  created() {
    this.getStatistics()
    this.getAnalysesList()
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
        const response = await getFinancialAnalysisStatistics({})
        if (response && response.data) {
          const data = response.data
          this.statistics = {
            totalRevenue: data.totalRevenue || 0,
            totalProfit: data.totalProfit || 0,
            totalAssets: data.totalAssets || 0,
            avgROE: data.avgROE || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 获取分析列表
    async getAnalysesList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNumber: this.searchForm.pageNum
        }
        const response = await getFinancialAnalysisList(params)
        this.analysesList = response.data.tlist || response.data.list || []
        this.total = response.data.total || 0
      } catch (error) {
        console.error('获取分析列表失败:', error)
        this.analysesList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initCharts() {
      this.revenueProfitChart = echarts.init(this.$refs.revenueProfitChart)
      this.financialIndicatorChart = echarts.init(this.$refs.financialIndicatorChart)
      this.assetLiabilityChart = echarts.init(this.$refs.assetLiabilityChart)
      this.cashFlowChart = echarts.init(this.$refs.cashFlowChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    async loadChartData() {
      try {
        const response = await getFinancialAnalysisCharts()
        const chartData = response.data
        
        this.updateRevenueProfitChart(chartData.revenueProfit)
        this.updateFinancialIndicatorChart(chartData.financialIndicator)
        this.updateAssetLiabilityChart(chartData.assetLiability)
        this.updateCashFlowChart(chartData.cashFlow)
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },

    // 更新营收利润趋势图表
    updateRevenueProfitChart(data) {
      if (!data || !data.xAxis) return
      const option = {
        title: {
          text: '营收利润趋势',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['营业收入', '净利润'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: data.xAxis
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)'
        },
        series: [
          {
            name: '营业收入',
            type: 'bar',
            data: data.revenue,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '净利润',
            type: 'line',
            data: data.profit,
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
      this.revenueProfitChart.setOption(option)
    },

    // 更新财务指标图表
    updateFinancialIndicatorChart(data) {
      if (!data || !data.xAxis || data.xAxis.length === 0) {
        const option = {
          title: { text: '财务指标分析', left: 'center', textStyle: { fontSize: 14 } },
          graphic: { type: 'text', left: 'center', top: 'middle', style: { text: '暂无数据', fontSize: 16, fill: '#999' } }
        }
        this.financialIndicatorChart.setOption(option, true)
        return
      }
      const option = {
        title: { text: '财务指标分析', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['利润率', '资产负债率', 'ROE'], bottom: 10 },
        xAxis: { type: 'category', data: data.xAxis },
        yAxis: { type: 'value', name: '比率(%)' },
        series: [
          {
            name: '利润率', type: 'line', data: data.profitRate, smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '资产负债率', type: 'line', data: data.debtRatio, smooth: true,
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: 'ROE', type: 'line', data: data.roe, smooth: true,
            itemStyle: { color: '#409EFF' }
          }
        ]
      }
      this.financialIndicatorChart.setOption(option, true)
    },

    // 更新资产负债结构图表
    updateAssetLiabilityChart(data) {
      if (!data || !data.xAxis) return
      const option = {
        title: {
          text: '资产负债结构',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总资产', '总负债'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: data.xAxis
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)'
        },
        series: [
          {
            name: '总资产',
            type: 'bar',
            data: data.assets,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '总负债',
            type: 'bar',
            data: data.liabilities,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      this.assetLiabilityChart.setOption(option, true)
    },

    // 更新现金流图表
    updateCashFlowChart(data) {
      if (!data || !data.xAxis || data.xAxis.length === 0) {
        const option = {
          title: { text: '现金流分析', left: 'center', textStyle: { fontSize: 14 } },
          graphic: { type: 'text', left: 'center', top: 'middle', style: { text: '暂无数据', fontSize: 16, fill: '#999' } }
        }
        this.cashFlowChart.setOption(option, true)
        return
      }
      const option = {
        title: { text: '现金流分析', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['经营现金流', '投资现金流', '筹资现金流'], bottom: 10 },
        xAxis: { type: 'category', data: data.xAxis },
        yAxis: { type: 'value', name: '现金流(万元)' },
        series: [
          { name: '经营现金流', type: 'bar', data: data.operating, itemStyle: { color: '#67C23A' } },
          { name: '投资现金流', type: 'bar', data: data.investing, itemStyle: { color: '#E6A23C' } },
          { name: '筹资现金流', type: 'bar', data: data.financing, itemStyle: { color: '#F56C6C' } }
        ]
      }
      this.cashFlowChart.setOption(option, true)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getAnalysesList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        period: '',
        statementType: '',
        auditStatus: '',
        minRevenue: '',
        maxRevenue: '',
        createTimeRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getAnalysesList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedAnalyses = selection
    },

    // 新增分析
    handleAdd() {
      this.currentAnalysis = {}
      this.dialogType = 'add'
      this.analysisDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentAnalysis = { ...row, statementId: row.id }
      this.dialogType = 'edit'
      this.analysisDialogVisible = true
    },

    // 查看分析
    handleView(row) {
      this.currentAnalysis = { ...row }
      this.detailDialogVisible = true
    },

    // 分析财务
    async handleAnalyze(row) {
      this.$confirm('确认对该条数据执行财务分析？', '执行分析', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        const loadingInstance = this.$loading({ lock: true, text: '正在执行分析...', background: 'rgba(0, 0, 0, 0.7)' })
        try {
          await batchAnalyzeFinancialData({ ids: [row.id] })
          this.$message.success('分析完成')
          this.getAnalysesList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('分析失败：' + (error.message || '请稍后重试'))
        } finally {
          loadingInstance.close()
        }
      }).catch(() => {})
    },

    // 对比分析
    handleCompare(row) {
      this.currentAnalysis = { ...row }
      this.comparisonDialogVisible = true
    },

    // 批量分析
    handleBatchAnalyze() {
      if (this.selectedAnalyses.length === 0) {
        this.$message.warning('请选择要分析的财务数据')
        return
      }
      this.$confirm(
        `确认对选中的 ${this.selectedAnalyses.length} 条数据进行批量分析？`,
        '批量分析确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }
      ).then(async () => {
        this.loading = true
        try {
          const ids = this.selectedAnalyses.map(item => item.id || item.analysisId)
          await batchAnalyzeFinancialData({ ids: ids })
          this.$message.success('批量分析完成')
          this.getAnalysesList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('批量分析失败：' + (error.message || '请稍后重试'))
        } finally {
          this.loading = false
        }
      }).catch(() => {})
    },

    // 批量对比
    handleBatchCompare() {
      if (this.selectedAnalyses.length === 0) {
        this.$message.warning('请选择要对比的财务数据')
        return
      }
      if (this.selectedAnalyses.length < 2) {
        this.$message.warning('请至少选择两条数据进行对比')
        return
      }
      this.currentAnalysis = { batchList: this.selectedAnalyses }
      this.comparisonDialogVisible = true
    },

    // 导出数据
    async handleExport() {
      try {
        const response = await exportFinancialAnalysisData(this.searchForm)
        if (response && response.data) {
          const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '财务穿透分析数据.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        }
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败: ' + (error.message || '请稍后重试'))
      }
    },

    // 生成报告
    handleGenerateReport() {
      this.$confirm(
        '确认根据当前筛选条件生成财务分析报告？',
        '生成报告确认',
        {
          confirmButtonText: '确定生成',
          cancelButtonText: '取消',
          type: 'info'
        }
      ).then(async () => {
        const loadingInstance = this.$loading({
          lock: true,
          text: '正在生成财务报告...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        try {
          const response = await generateFinancialAnalysisReport(this.searchForm)
          if (response && response.data) {
            const blob = new Blob([response.data], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            })
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = `财务穿透分析报告_${new Date().getTime()}.xlsx`
            link.click()
            URL.revokeObjectURL(link.href)
          }
          this.$message.success('报告生成成功')
        } catch (error) {
          this.$message.error('报告生成失败：' + (error.message || '请稍后重试'))
        } finally {
          loadingInstance.close()
        }
      }).catch(() => {})
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'trend':
          this.handleTrendAnalysis(row)
          break
        case 'predict':
          this.handleForecastAnalysis(row)
          break
        case 'risk':
          this.handleRiskAssessment(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 趋势分析
    async handleTrendAnalysis(row) {
      const loadingInstance = this.$loading({ lock: true, text: '正在加载趋势分析...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const response = await getFinancialTrendAnalysis({ id: row.id, companyId: row.companyId, enterpriseName: row.enterpriseName })
        if (response && response.data && response.result === 200) {
          this.currentAnalysis = { ...row, viewType: 'trend', trendData: response.data }
          this.detailDialogVisible = true
        } else {
          this.$message.warning(response.msg || '暂无趋势数据')
        }
      } catch (error) {
        this.$message.error('趋势分析失败：' + (error.message || '请稍后重试'))
      } finally {
        loadingInstance.close()
      }
    },

    // 预测分析
    async handleForecastAnalysis(row) {
      const loadingInstance = this.$loading({ lock: true, text: '正在加载预测分析...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const response = await getFinancialForecastAnalysis({ id: row.id, companyId: row.companyId, enterpriseName: row.enterpriseName })
        if (response && response.data && response.result === 200) {
          this.currentAnalysis = { ...row, viewType: 'predict', forecastData: response.data }
          this.detailDialogVisible = true
        } else {
          this.$message.warning(response.msg || '暂无预测数据，需要至少2期历史数据')
        }
      } catch (error) {
        this.$message.error('预测分析失败：' + (error.message || '请稍后重试'))
      } finally {
        loadingInstance.close()
      }
    },

    // 风险评估
    async handleRiskAssessment(row) {
      const loadingInstance = this.$loading({ lock: true, text: '正在加载风险评估...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const response = await getFinancialHealthAssessment({ id: row.id, companyId: row.companyId, enterpriseName: row.enterpriseName })
        if (response && response.data && response.result === 200) {
          this.currentAnalysis = { ...row, viewType: 'risk', riskData: response.data }
          this.detailDialogVisible = true
        } else {
          this.$message.warning(response.msg || '暂无风险评估数据')
        }
      } catch (error) {
        this.$message.error('风险评估失败：' + (error.message || '请稍后重试'))
      } finally {
        loadingInstance.close()
      }
    },

    // 删除分析
    handleDelete(row) {
      this.$confirm('确认删除该财务分析记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFinancialAnalysis({ analysisId: row.id || row.analysisId })
          this.$message.success('删除成功')
          this.getAnalysesList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 刷新图表
    refreshRevenueChart() {
      this.loadChartData()
    },

    refreshIndicatorChart() {
      this.loadChartData()
    },

    refreshAssetChart() {
      this.loadChartData()
    },

    refreshCashFlowChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.revenueProfitChart) this.revenueProfitChart.resize()
      if (this.financialIndicatorChart) this.financialIndicatorChart.resize()
      if (this.assetLiabilityChart) this.assetLiabilityChart.resize()
      if (this.cashFlowChart) this.cashFlowChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.revenueProfitChart) {
        this.revenueProfitChart.dispose()
        this.revenueProfitChart = null
      }
      if (this.financialIndicatorChart) {
        this.financialIndicatorChart.dispose()
        this.financialIndicatorChart = null
      }
      if (this.assetLiabilityChart) {
        this.assetLiabilityChart.dispose()
        this.assetLiabilityChart = null
      }
      if (this.cashFlowChart) {
        this.cashFlowChart.dispose()
        this.cashFlowChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 格式化数字
    formatNumber(num) {
      if (!num && num !== 0) return '0'
      if (num >= 100000000) {
        return (num / 100000000).toFixed(2) + '亿'
      }
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      }
      return num.toLocaleString()
    },

    // 计算资产负债率
    calcDebtRatio(row) {
      if (!row.totalAssets || row.totalAssets === 0) return 0
      return (row.totalLiabilities / row.totalAssets * 100).toFixed(1)
    },

    // 获取利润样式类
    getProfitClass(profit) {
      if (profit > 0) return 'positive-profit'
      if (profit < 0) return 'negative-profit'
      return 'zero-profit'
    },

    // 获取ROE样式类
    getROEClass(roe) {
      if (roe >= 15) return 'excellent-roe'
      if (roe >= 10) return 'good-roe'
      if (roe >= 5) return 'average-roe'
      return 'poor-roe'
    },

    // 获取资产负债率颜色
    getDebtRatioColor(ratio) {
      const val = parseFloat(ratio)
      if (val <= 40) return '#67C23A'
      if (val <= 60) return '#409EFF'
      if (val <= 80) return '#E6A23C'
      return '#F56C6C'
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

.positive-profit {
  color: #67C23A;
  font-weight: bold;
}

.negative-profit {
  color: #F56C6C;
  font-weight: bold;
}

.zero-profit {
  color: #909399;
}

.excellent-roe {
  color: #67C23A;
  font-weight: bold;
}

.good-roe {
  color: #409EFF;
  font-weight: bold;
}

.average-roe {
  color: #E6A23C;
}

.poor-roe {
  color: #F56C6C;
}
</style>
