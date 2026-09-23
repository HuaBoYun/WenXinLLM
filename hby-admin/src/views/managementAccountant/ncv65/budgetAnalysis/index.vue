<template>
  <div class="budget-analysis-index">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>预算分析管理</h1>
      <p>全方位预算分析平台，提供差异分析、趋势分析、对比分析等多维度分析能力</p>
    </div>

    <!-- 分析概览统计 -->
    <el-row :gutter="20" class="analysis-overview">
      <el-col :span="6">
        <el-card class="stat-card variance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <h3>{{ analysisStats.varianceCount }}</h3>
              <p>差异分析</p>
              <div class="stat-detail">
                <span>{{ analysisStats.significantVariances }} 个重大差异</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend" :class="analysisStats.varianceTrend > 0 ? 'up' : 'down'">
              <i :class="analysisStats.varianceTrend > 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
              {{ Math.abs(analysisStats.varianceTrend) }}%
            </span>
            <span class="stat-label">较上期</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card trend-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-trend-charts"></i>
            </div>
            <div class="stat-info">
              <h3>{{ analysisStats.trendReports }}</h3>
              <p>趋势报告</p>
              <div class="stat-detail">
                <span>{{ analysisStats.activeTrends }} 个活跃趋势</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ analysisStats.trendGrowth }}%
            </span>
            <span class="stat-label">增长率</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card forecast-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-info">
              <h3>{{ analysisStats.forecastAccuracy }}%</h3>
              <p>预测准确率</p>
              <div class="stat-detail">
                <span>{{ analysisStats.forecastModels }} 个预测模型</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ analysisStats.accuracyImprovement }}%
            </span>
            <span class="stat-label">较上月</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card dashboard-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="stat-info">
              <h3>{{ analysisStats.dashboardCount }}</h3>
              <p>分析仪表板</p>
              <div class="stat-detail">
                <span>{{ analysisStats.activeDashboards }} 个活跃</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend">
              <i class="el-icon-minus"></i>
              {{ analysisStats.dashboardUsage }}%
            </span>
            <span class="stat-label">使用率</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析模块导航 -->
    <el-card class="analysis-modules-card" shadow="never">
      <div class="modules-header">
        <h3>分析模块</h3>
        <p>选择相应的分析模块进行深度预算分析</p>
      </div>
      
      <el-row :gutter="24" class="modules-grid">
        <el-col :span="8" v-for="module in analysisModules" :key="module.key">
          <div class="module-item" @click="navigateToModule(module)">
            <div class="module-header">
              <div class="module-icon">
                <i :class="module.icon"></i>
              </div>
              <div class="module-badge" v-if="module.isNew">
                <el-tag type="success" size="mini">新功能</el-tag>
              </div>
            </div>
            <div class="module-content">
              <h4>{{ module.title }}</h4>
              <p>{{ module.description }}</p>
              <div class="module-stats">
                <div class="stat-item">
                  <i class="el-icon-document"></i>
                  <span>{{ module.reportCount }} 个报告</span>
                </div>
                <div class="stat-item">
                  <i class="el-icon-time"></i>
                  <span>{{ module.lastUpdate }}</span>
                </div>
              </div>
            </div>
            <div class="module-action">
              <el-button type="primary" size="small">开始分析</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 分析工作台 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="analysis-workspace-card" shadow="never">
          <div class="card-header">
            <h3>分析工作台</h3>
            <div class="workspace-controls">
              <el-button-group size="small">
                <el-button :type="workspaceView === 'chart' ? 'primary' : ''" @click="workspaceView = 'chart'">
                  <i class="el-icon-pie-chart"></i> 图表视图
                </el-button>
                <el-button :type="workspaceView === 'table' ? 'primary' : ''" @click="workspaceView = 'table'">
                  <i class="el-icon-s-grid"></i> 表格视图
                </el-button>
                <el-button :type="workspaceView === 'dashboard' ? 'primary' : ''" @click="workspaceView = 'dashboard'">
                  <i class="el-icon-monitor"></i> 仪表板
                </el-button>
              </el-button-group>
            </div>
          </div>
          
          <div class="workspace-content">
            <!-- 图表视图 -->
            <div v-if="workspaceView === 'chart'" class="chart-view">
              <div class="chart-container">
                <div class="chart-item">
                  <h5>预算执行趋势</h5>
                  <div class="chart" ref="executionTrendChart" style="height: 200px;"></div>
                </div>
                <div class="chart-item">
                  <h5>部门预算对比</h5>
                  <div class="chart" ref="departmentComparisonChart" style="height: 200px;"></div>
                </div>
              </div>
            </div>
            
            <!-- 表格视图 -->
            <div v-else-if="workspaceView === 'table'" class="table-view">
              <el-table :data="analysisTableData" border size="small">
                <el-table-column prop="department" label="部门" width="120" />
                <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="variance" label="差异金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text" :class="getVarianceClass(scope.row.variance)">
                      {{ formatAmount(scope.row.variance) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="varianceRate" label="差异率" width="100" align="center">
                  <template slot-scope="scope">
                    <span :class="getVarianceClass(scope.row.variance)">
                      {{ scope.row.varianceRate }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="executionRate" label="执行率" width="120" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.executionRate"
                      :stroke-width="6"
                      :text-inside="true"
                      :color="getExecutionColor(scope.row.executionRate)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </div>
            
            <!-- 仪表板视图 -->
            <div v-else class="dashboard-view">
              <div class="dashboard-grid">
                <div class="dashboard-item">
                  <div class="dashboard-title">预算完成度</div>
                  <div class="dashboard-chart" ref="completionGauge" style="height: 150px;"></div>
                </div>
                <div class="dashboard-item">
                  <div class="dashboard-title">费用结构</div>
                  <div class="dashboard-chart" ref="expenseStructure" style="height: 150px;"></div>
                </div>
                <div class="dashboard-item">
                  <div class="dashboard-title">月度趋势</div>
                  <div class="dashboard-chart" ref="monthlyTrend" style="height: 150px;"></div>
                </div>
                <div class="dashboard-item">
                  <div class="dashboard-title">部门排名</div>
                  <div class="dashboard-chart" ref="departmentRanking" style="height: 150px;"></div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="analysis-insights-card" shadow="never">
          <div class="card-header">
            <h3>分析洞察</h3>
            <el-button type="text" @click="viewAllInsights">查看全部</el-button>
          </div>
          
          <div class="insights-content">
            <div class="insight-item" v-for="insight in analysisInsights" :key="insight.id">
              <div class="insight-header">
                <div class="insight-type">
                  <el-tag :type="getInsightType(insight.type)" size="mini">
                    {{ insight.type }}
                  </el-tag>
                </div>
                <div class="insight-time">{{ insight.time }}</div>
              </div>
              <div class="insight-content-text">
                <h5>{{ insight.title }}</h5>
                <p>{{ insight.description }}</p>
              </div>
              <div class="insight-actions">
                <el-button type="text" size="mini" @click="viewInsightDetail(insight)">
                  查看详情
                </el-button>
                <el-button type="text" size="mini" @click="createAlert(insight)">
                  创建预警
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速分析和最近报告 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="quick-analysis-card" shadow="never">
          <div class="card-header">
            <h3>快速分析</h3>
            <el-button type="text" @click="viewAnalysisTemplates">分析模板</el-button>
          </div>
          
          <div class="quick-analysis-content">
            <el-form :model="quickAnalysisForm" label-width="80px" size="small">
              <el-form-item label="分析类型">
                <el-select v-model="quickAnalysisForm.analysisType" placeholder="请选择分析类型" style="width: 100%">
                  <el-option
                    v-for="type in analysisTypes"
                    :key="type.value"
                    :label="type.label"
                    :value="type.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="分析维度">
                <el-select v-model="quickAnalysisForm.dimension" placeholder="请选择分析维度" style="width: 100%">
                  <el-option
                    v-for="dim in analysisDimensions"
                    :key="dim.value"
                    :label="dim.label"
                    :value="dim.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="quickAnalysisForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 100%"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuickAnalysis">开始分析</el-button>
                <el-button @click="resetQuickAnalysis">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="recent-reports-card" shadow="never">
          <div class="card-header">
            <h3>最近报告</h3>
            <el-button type="text" @click="viewAllReports">查看全部</el-button>
          </div>
          
          <div class="reports-list">
            <div class="report-item" v-for="report in recentReports" :key="report.id" @click="viewReport(report)">
              <div class="report-icon">
                <i :class="getReportIcon(report.type)"></i>
              </div>
              <div class="report-content">
                <h5>{{ report.title }}</h5>
                <p>{{ report.description }}</p>
                <div class="report-meta">
                  <span class="report-author">{{ report.author }}</span>
                  <span class="report-time">{{ report.createTime }}</span>
                </div>
              </div>
              <div class="report-status">
                <el-tag :type="getReportStatusType(report.status)" size="mini">
                  {{ report.status }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
 
     <!-- 分析模块弹窗 -->
     <el-dialog
       :title="currentModuleTitle"
       :visible.sync="moduleDialogVisible"
       width="90%"
       top="5vh"
       :close-on-click-modal="false"
       :destroy-on-close="true"
     >
       <component :is="currentModuleComponent" v-if="moduleDialogVisible" />
     </el-dialog>
 
     <!-- 快速分析弹窗 -->
     <el-dialog
       :title="quickAnalysisTitle"
       :visible.sync="quickAnalysisDialogVisible"
       width="90%"
       top="5vh"
       :close-on-click-modal="false"
       :destroy-on-close="true"
     >
       <component :is="quickAnalysisComponent" v-if="quickAnalysisDialogVisible" />
     </el-dialog>
   </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'
import VarianceAnalysis from './VarianceAnalysis.vue'
import TrendAnalysis from './TrendAnalysis.vue'
import ComparisonAnalysis from './ComparisonAnalysis.vue'
import PerformanceAnalysis from './PerformanceAnalysis.vue'
import ForecastAnalysis from './ForecastAnalysis.vue'
import RollingForecast from './RollingForecast.vue'

export default {
  name: 'BudgetAnalysisIndex',
  components: {
    VarianceAnalysis,
    TrendAnalysis,
    ComparisonAnalysis,
    PerformanceAnalysis,
    ForecastAnalysis,
    RollingForecast
  },
  data() {
    return {
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      // 分析统计数据
      analysisStats: {
        varianceCount: 0,
        significantVariances: 0,
        varianceTrend: 0,
        trendReports: 0,
        activeTrends: 0,
        trendGrowth: 0,
        forecastAccuracy: 0,
        forecastModels: 0,
        accuracyImprovement: 0,
        dashboardCount: 0,
        activeDashboards: 0,
        dashboardUsage: 0
      },
      
      // 分析模块
      analysisModules: [
        {
          key: 'varianceAnalysis',
          title: '差异分析',
          description: '预算与实际的差异分析，识别重大偏差',
          icon: 'el-icon-data-analysis',
          reportCount: 0,
          lastUpdate: '',
          path: '/ncv65/budget-analysis/variance-analysis'
        },
        {
          key: 'trendAnalysis',
          title: '趋势分析',
          description: '预算执行趋势分析，预测未来走向',
          icon: 'el-icon-trend-charts',
          reportCount: 0,
          lastUpdate: '',
          path: '/ncv65/budget-analysis/trend-analysis'
        },
        {
          key: 'comparisonAnalysis',
          title: '对比分析',
          description: '多维度预算对比分析，横向纵向比较',
          icon: 'el-icon-s-data',
          reportCount: 0,
          lastUpdate: '',
          path: '/ncv65/budget-analysis/comparison-analysis'
        },
        {
          key: 'performanceAnalysis',
          title: '绩效分析',
          description: '预算绩效评估分析，KPI达成情况',
          icon: 'el-icon-trophy',
          reportCount: 0,
          lastUpdate: '',
          path: '/ncv65/budget-analysis/performance-analysis'
        },
        {
          key: 'forecastAnalysis',
          title: '预测分析',
          description: '基于历史数据的预算预测分析',
          icon: 'el-icon-view',
          reportCount: 0,
          lastUpdate: '',
          isNew: true,
          path: '/ncv65/budget-analysis/forecast-analysis'
        },
        {
          key: 'rollingForecast',
          title: '滚动预测',
          description: '动态滚动预测，持续更新预算预期',
          icon: 'el-icon-refresh',
          reportCount: 0,
          lastUpdate: '',
          isNew: true,
          path: '/ncv65/budget-analysis/rolling-forecast'
        }
      ],
      
      // 工作台视图
      workspaceView: 'chart',
      
      // 图表实例
      executionTrendChart: null,
      departmentComparisonChart: null,
      completionGauge: null,
      expenseStructure: null,
      monthlyTrend: null,
      departmentRanking: null,
      
      // 表格数据
      analysisTableData: [],

      // 分析洞察
      analysisInsights: [],
      
      // 快速分析表单
      quickAnalysisForm: {
        analysisType: '',
        dimension: '',
        dateRange: null
      },
      
      // 分析类型选项
      analysisTypes: [
        { value: 'VARIANCE', label: '差异分析' },
        { value: 'TREND', label: '趋势分析' },
        { value: 'COMPARISON', label: '对比分析' },
        { value: 'PERFORMANCE', label: '绩效分析' }
      ],
      
      // 分析维度选项
      analysisDimensions: [
        { value: 'DEPARTMENT', label: '部门维度' },
        { value: 'PROJECT', label: '项目维度' },
        { value: 'PRODUCT', label: '产品维度' },
        { value: 'TIME', label: '时间维度' }
      ],
      
      // 最近报告
     recentReports: [],
 
     // 弹窗相关
     moduleDialogVisible: false,
     currentModuleTitle: '',
     currentModuleComponent: null,
     quickAnalysisDialogVisible: false,
     quickAnalysisTitle: '',
     quickAnalysisComponent: null,
 
     // 缓存后端图表数据
     cachedChartData: null
    }
  },
  
  watch: {
    workspaceView(newVal) {
      this.$nextTick(() => {
        if (newVal === 'chart') {
          this.initExecutionTrendChart()
          this.initDepartmentComparisonChart()
        } else if (newVal === 'dashboard') {
          this.initCompletionGauge()
          this.initExpenseStructure()
          this.initMonthlyTrend()
          this.initDepartmentRanking()
        }
        // 用缓存的后端数据刷新图表
        if (this.cachedChartData) {
          this.updateChartsWithData(this.cachedChartData)
        }
      })
    }
  },
 
  mounted() {
    this.loadDashboardData()
    this.initCharts()
  },
  
  beforeDestroy() {
    this.disposeCharts()
  },
  
  methods: {
    // 加载仪表盘数据
    async loadDashboardData() {
      try {
        const response = await budgetAnalysisApi.getDashboardStats()
        if (response.code === 1 && response.data) {
          this.analysisStats = { ...this.analysisStats, ...response.data.stats }
          this.analysisTableData = response.data.tableData || []
          this.analysisInsights = response.data.insights || []
          this.recentReports = response.data.recentReports || []
          // 更新模块统计
          if (response.data.moduleStats) {
            this.analysisModules.forEach(m => {
              const stat = response.data.moduleStats[m.key]
              if (stat) {
                m.reportCount = stat.reportCount || 0
                m.lastUpdate = stat.lastUpdate || ''
              }
            })
          }
          // 用后端数据刷新图表
          this.$nextTick(() => {
            if (response.data.chartData) {
             this.cachedChartData = response.data.chartData
              this.updateChartsWithData(response.data.chartData)
            }
          })
        }
      } catch (error) {
        console.error('加载仪表盘数据失败：', error)
      }
    },

    // 用后端数据更新图表
    updateChartsWithData(chartData) {
      if (chartData.executionTrend && this.executionTrendChart) {
        this.executionTrendChart.setOption({
          graphic: { invisible: true },
          xAxis: { data: chartData.executionTrend.xAxis || [] },
          series: [{ data: chartData.executionTrend.data || [] }]
        })
      }
      if (chartData.departmentComparison && this.departmentComparisonChart) {
        this.departmentComparisonChart.setOption({
          graphic: { invisible: true },
          xAxis: { data: chartData.departmentComparison.xAxis || [] },
          series: [{ data: chartData.departmentComparison.data || [] }]
        })
      }
      if (chartData.completionRate !== undefined && this.completionGauge) {
        this.completionGauge.setOption({
          series: [{ data: [{ value: chartData.completionRate, name: '完成度' }] }]
        })
      }
      if (chartData.expenseStructure && this.expenseStructure) {
        this.expenseStructure.setOption({
          graphic: { invisible: true },
          series: [{ data: chartData.expenseStructure || [] }]
        })
      }
      if (chartData.monthlyTrend && this.monthlyTrend) {
        this.monthlyTrend.setOption({
          graphic: { invisible: true },
          xAxis: { data: chartData.monthlyTrend.xAxis || [] },
          series: [
            { data: chartData.monthlyTrend.actual || [] }
          ]
        })
      }
      if (chartData.departmentRanking && this.departmentRanking) {
        this.departmentRanking.setOption({
          graphic: { invisible: true },
          yAxis: { data: chartData.departmentRanking.yAxis || [] },
          series: [{ data: chartData.departmentRanking.data || [] }]
        })
      }
    },

    // 导航到模块
    navigateToModule(module) {
     const componentMap = {
       varianceAnalysis: 'VarianceAnalysis',
       trendAnalysis: 'TrendAnalysis',
       comparisonAnalysis: 'ComparisonAnalysis',
       performanceAnalysis: 'PerformanceAnalysis',
       forecastAnalysis: 'ForecastAnalysis',
       rollingForecast: 'RollingForecast'
     }
     this.currentModuleTitle = module.title
     this.currentModuleComponent = componentMap[module.key] || null
     if (this.currentModuleComponent) {
       this.moduleDialogVisible = true
     } else {
       this.$message.warning('该模块暂未开放')
     }
    },
    
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initExecutionTrendChart()
        this.initDepartmentComparisonChart()
        this.initCompletionGauge()
        this.initExpenseStructure()
        this.initMonthlyTrend()
        this.initDepartmentRanking()
      })
    },
    
    // 销毁图表
    disposeCharts() {
      const charts = [
        this.executionTrendChart,
        this.departmentComparisonChart,
        this.completionGauge,
        this.expenseStructure,
        this.monthlyTrend,
        this.departmentRanking
      ]
      
      charts.forEach(chart => {
        if (chart) {
          chart.dispose()
        }
      })
    },
    
    // 初始化执行趋势图表
    initExecutionTrendChart() {
      if (this.$refs.executionTrendChart) {
        this.executionTrendChart = echarts.init(this.$refs.executionTrendChart)
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: { type: 'value', name: '执行率(%)' },
          series: [{
            data: [],
            type: 'line',
            smooth: true,
            itemStyle: { color: '#409EFF' }
          }],
          graphic: {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无数据', fontSize: 14, fill: '#999' }
          }
        }
        this.executionTrendChart.setOption(option)
      }
    },

    // 初始化部门对比图表
    initDepartmentComparisonChart() {
      if (this.$refs.departmentComparisonChart) {
        this.departmentComparisonChart = echarts.init(this.$refs.departmentComparisonChart)
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: { type: 'value', name: '执行率(%)' },
          series: [{
            data: [],
            type: 'bar',
            itemStyle: { color: '#67C23A' }
          }],
          graphic: {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无数据', fontSize: 14, fill: '#999' }
          }
        }
        this.departmentComparisonChart.setOption(option)
      }
    },

    // 初始化完成度仪表盘
    initCompletionGauge() {
      if (this.$refs.completionGauge) {
        this.completionGauge = echarts.init(this.$refs.completionGauge)
        const option = {
          series: [{
            type: 'gauge',
            data: [{ value: 0, name: '完成度' }],
            detail: { fontSize: 14 }
          }]
        }
        this.completionGauge.setOption(option)
      }
    },

    // 初始化费用结构图表
    initExpenseStructure() {
      if (this.$refs.expenseStructure) {
        this.expenseStructure = echarts.init(this.$refs.expenseStructure)
        const option = {
          tooltip: { trigger: 'item' },
          series: [{
            type: 'pie',
            radius: '60%',
            data: []
          }],
          graphic: {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无数据', fontSize: 14, fill: '#999' }
          }
        }
        this.expenseStructure.setOption(option)
      }
    },

    // 初始化月度趋势图表
    initMonthlyTrend() {
      if (this.$refs.monthlyTrend) {
        this.monthlyTrend = echarts.init(this.$refs.monthlyTrend)
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: { type: 'value' },
          series: [{
            data: [],
            type: 'line',
            areaStyle: {},
            itemStyle: { color: '#E6A23C' }
          }],
          graphic: {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无数据', fontSize: 14, fill: '#999' }
          }
        }
        this.monthlyTrend.setOption(option)
      }
    },

    // 初始化部门排名图表
    initDepartmentRanking() {
      if (this.$refs.departmentRanking) {
        this.departmentRanking = echarts.init(this.$refs.departmentRanking)
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'value' },
          yAxis: {
            type: 'category',
            data: []
          },
          series: [{
            data: [],
            type: 'bar',
            itemStyle: { color: '#F56C6C' }
          }],
          graphic: {
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无数据', fontSize: 14, fill: '#999' }
          }
        }
        this.departmentRanking.setOption(option)
      }
    },
    
    // 快速分析
   async handleQuickAnalysis() {
     if (!this.quickAnalysisForm.analysisType) {
       this.$message.warning('请选择分析类型')
       return
     }
     
     const loading = this.$loading({ lock: true, text: '正在执行分析...', background: 'rgba(0, 0, 0, 0.7)' })
     try {
       const params = {
         analysisType: this.quickAnalysisForm.analysisType,
         dimension: this.quickAnalysisForm.dimension || 'DEPARTMENT'
       }
       if (this.quickAnalysisForm.dateRange && this.quickAnalysisForm.dateRange.length === 2) {
         params.startDate = this.quickAnalysisForm.dateRange[0]
         params.endDate = this.quickAnalysisForm.dateRange[1]
       }
       const response = await budgetAnalysisApi.quickAnalysis(params)
       loading.close()
       if (response.code === 1) {
         this.$message.success('分析完成')
         const componentMap = {
           'VARIANCE': { component: 'VarianceAnalysis', title: '差异分析' },
           'TREND': { component: 'TrendAnalysis', title: '趋势分析' },
           'COMPARISON': { component: 'ComparisonAnalysis', title: '对比分析' },
           'PERFORMANCE': { component: 'PerformanceAnalysis', title: '绩效分析' }
         }
         const target = componentMap[this.quickAnalysisForm.analysisType]
         if (target) {
           this.quickAnalysisTitle = target.title
           this.quickAnalysisComponent = target.component
           this.quickAnalysisDialogVisible = true
         }
       } else {
         this.$message.error(response.msg || '分析失败')
       }
     } catch (error) {
       loading.close()
       console.error('快速分析失败：', error)
       this.$message.error('分析请求失败，请稍后重试')
     }
   },
    
    // 重置快速分析
    resetQuickAnalysis() {
      this.quickAnalysisForm = {
        analysisType: '',
        dimension: '',
        dateRange: null
      }
    },
    
    // 查看洞察详情
    viewInsightDetail(insight) {
      this.$message.info('详情查看功能开发中')
    },

    // 创建预警
    createAlert(insight) {
      this.$message.info('详情查看功能开发中')
    },

    // 查看报告
    viewReport(report) {
      this.$message.info('详情查看功能开发中')
    },

    // 查看全部洞察
    viewAllInsights() {
      this.$message.info('功能开发中')
    },

    // 查看分析模板
    viewAnalysisTemplates() {
      this.$message.info('报告功能开发中')
    },

    // 查看全部报告
    viewAllReports() {
      this.$message.info('报告功能开发中')
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Math.abs(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取差异样式类
    getVarianceClass(variance) {
      if (variance > 0) return 'danger-text'
      if (variance < 0) return 'success-text'
      return ''
    },
    
    // 获取执行率颜色
    getExecutionColor(rate) {
      if (rate >= 95 && rate <= 105) return '#67C23A'
      if (rate >= 90 && rate <= 110) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取洞察类型
    getInsightType(type) {
      const typeMap = {
        '异常': 'danger',
        '趋势': 'primary',
        '机会': 'success',
        '风险': 'warning'
      }
      return typeMap[type] || 'info'
    },
    
    // 获取报告图标
    getReportIcon(type) {
      const iconMap = {
        'VARIANCE': 'el-icon-data-analysis',
        'TREND': 'el-icon-trend-charts',
        'COMPARISON': 'el-icon-s-data',
        'PERFORMANCE': 'el-icon-trophy'
      }
      return iconMap[type] || 'el-icon-document'
    },
    
    // 获取报告状态类型
    getReportStatusType(status) {
      const statusMap = {
        '已完成': 'success',
        '进行中': 'primary',
        '待审核': 'warning',
        '已拒绝': 'danger'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-analysis-index {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
  
  .page-header {
    margin-bottom: 24px;
    
    h1 {
      color: #303133;
      font-size: 28px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }
    
    p {
      color: #606266;
      font-size: 16px;
      margin: 0;
    }
  }
  
  .analysis-overview {
    margin-bottom: 24px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }
      
      .stat-content {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          i {
            font-size: 24px;
            color: white;
          }
        }
        
        .stat-info {
          flex: 1;
          
          h3 {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 14px;
            color: #909399;
            margin: 0 0 4px 0;
          }
          
          .stat-detail {
            font-size: 12px;
            color: #C0C4CC;
          }
        }
      }
      
      .stat-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .stat-trend {
          font-size: 12px;
          font-weight: 500;
          
          &.up {
            color: #67C23A;
          }
          
          &.down {
            color: #F56C6C;
          }
          
          i {
            margin-right: 2px;
          }
        }
        
        .stat-label {
          font-size: 12px;
          color: #C0C4CC;
        }
      }
      
      &.variance-card .stat-icon {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      
      &.trend-card .stat-icon {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
      
      &.forecast-card .stat-icon {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
      
      &.dashboard-card .stat-icon {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }
  }
  
  .analysis-modules-card,
  .analysis-workspace-card,
  .analysis-insights-card,
  .quick-analysis-card,
  .recent-reports-card {
    margin-bottom: 24px;
    border: none;
    border-radius: 8px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }
    }
  }
  
  .analysis-modules-card {
    .modules-header {
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }
      
      p {
        font-size: 14px;
        color: #606266;
        margin: 0;
      }
    }
    
    .modules-grid {
      .module-item {
        background: white;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        padding: 20px;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-bottom: 16px;
        
        &:hover {
          border-color: #409EFF;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          transform: translateY(-1px);
        }
        
        .module-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 16px;
          
          .module-icon {
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #409EFF, #36CFC9);
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            
            i {
              font-size: 20px;
              color: white;
            }
          }
        }
        
        .module-content {
          margin-bottom: 16px;
          
          h4 {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 8px 0;
          }
          
          p {
            font-size: 14px;
            color: #606266;
            margin: 0 0 12px 0;
            line-height: 1.5;
          }
          
          .module-stats {
            display: flex;
            gap: 16px;
            
            .stat-item {
              font-size: 12px;
              color: #909399;
              display: flex;
              align-items: center;
              
              i {
                margin-right: 4px;
              }
            }
          }
        }
        
        .module-action {
          text-align: right;
        }
      }
    }
  }
  
  .analysis-workspace-card {
    .workspace-content {
      .chart-view {
        .chart-container {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 20px;
          
          .chart-item {
            h5 {
              color: #303133;
              margin: 0 0 12px 0;
              font-size: 14px;
            }
            
            .chart {
              border: 1px solid #EBEEF5;
              border-radius: 4px;
            }
          }
        }
      }
      
      .dashboard-view {
        .dashboard-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
          
          .dashboard-item {
            .dashboard-title {
              font-size: 14px;
              color: #303133;
              margin-bottom: 8px;
              text-align: center;
            }
            
            .dashboard-chart {
              border: 1px solid #EBEEF5;
              border-radius: 4px;
            }
          }
        }
      }
    }
  }
  
  .analysis-insights-card {
    .insights-content {
      .insight-item {
        padding: 16px;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        margin-bottom: 12px;
        
        .insight-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
          
          .insight-time {
            font-size: 12px;
            color: #909399;
          }
        }
        
        .insight-content-text {
          margin-bottom: 12px;
          
          h5 {
            font-size: 14px;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 12px;
            color: #606266;
            margin: 0;
            line-height: 1.4;
          }
        }
        
        .insight-actions {
          text-align: right;
        }
      }
    }
  }
  
  .recent-reports-card {
    .reports-list {
      .report-item {
        display: flex;
        align-items: center;
        padding: 12px;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        margin-bottom: 12px;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          border-color: #409EFF;
          background-color: #F0F9FF;
        }
        
        .report-icon {
          width: 36px;
          height: 36px;
          background: #409EFF;
          border-radius: 6px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          
          i {
            font-size: 16px;
            color: white;
          }
        }
        
        .report-content {
          flex: 1;
          
          h5 {
            font-size: 14px;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 12px;
            color: #606266;
            margin: 0 0 4px 0;
          }
          
          .report-meta {
            font-size: 12px;
            color: #909399;
            
            .report-author {
              margin-right: 12px;
            }
          }
        }
        
        .report-status {
          margin-left: 12px;
        }
      }
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
}
</style>
