<template>
  <div class="budget-analysis-container">
    <!-- 头部筛选区域 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" ref="filterForm" size="small">
        <el-form-item label="预算期间">
          <el-select
            v-model="filterForm.period"
            placeholder="请选择期间"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="period in periodList"
              :key="period.value"
              :label="period.label"
              :value="period.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="部门">
          <el-select
            v-model="filterForm.department"
            placeholder="请选择部门"
            clearable
            filterable
            style="width: 150px"
          >
            <el-option
              v-for="dept in departmentList"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="预算类型">
          <el-select
            v-model="filterForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option label="年度预算" value="annual" />
            <el-option label="季度预算" value="quarterly" />
            <el-option label="月度预算" value="monthly" />
            <el-option label="项目预算" value="project" />
          </el-select>
        </el-form-item>

        <el-form-item label="对比期间">
          <el-date-picker
            v-model="filterForm.comparePeriod"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 240px"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleFilter">
            查询分析
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExportReport">
            导出报告
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- KPI 指标卡 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiData" :key="kpi.key">
        <el-card class="kpi-card" :body-style="{ padding: '20px' }">
          <div class="kpi-content">
            <div class="kpi-icon" :style="{ backgroundColor: kpi.color }">
              <i :class="kpi.icon"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-value">{{ kpi.value }}</div>
              <div class="kpi-label">{{ kpi.label }}</div>
              <div class="kpi-trend" :class="kpi.trend">
                <i :class="getTrendIcon(kpi.trend)"></i>
                <span>{{ kpi.change }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表分析区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 预算执行趋势图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>预算执行趋势</span>
            <el-button-group size="mini">
              <el-button
                :type="trendChartType === 'line' ? 'primary' : ''"
                @click="trendChartType = 'line'"
              >
                折线图
              </el-button>
              <el-button
                :type="trendChartType === 'bar' ? 'primary' : ''"
                @click="trendChartType = 'bar'"
              >
                柱状图
              </el-button>
            </el-button-group>
          </div>
          <div ref="trendChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 预算分布图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>预算分布分析</span>
            <el-button-group size="mini">
              <el-button
                :type="distributionChartType === 'pie' ? 'primary' : ''"
                @click="distributionChartType = 'pie'"
              >
                饼图
              </el-button>
              <el-button
                :type="distributionChartType === 'funnel' ? 'primary' : ''"
                @click="distributionChartType = 'funnel'"
              >
                漏斗图
              </el-button>
            </el-button-group>
          </div>
          <div ref="distributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 对比分析区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 预算对比分析 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>预算对比分析</span>
            <el-select v-model="comparisonType" size="mini" style="width: 120px">
              <el-option label="同比" value="year" />
              <el-option label="环比" value="month" />
              <el-option label="预算vs实际" value="budget_vs_actual" />
            </el-select>
          </div>
          <div ref="comparisonChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 执行进度分析 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>执行进度分析</span>
            <el-button size="mini" icon="el-icon-refresh" @click="refreshProgressChart">
              刷新
            </el-button>
          </div>
          <div ref="progressChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预警信息和详细数据 -->
    <el-row :gutter="20" class="data-row">
      <!-- 预警信息 -->
      <el-col :span="8">
        <el-card class="warning-card">
          <div slot="header" class="warning-header">
            <span>预算预警</span>
            <el-badge :value="warningData.length" class="warning-badge" type="danger">
              <el-button size="mini" icon="el-icon-bell" @click="handleRefreshWarnings">
                刷新
              </el-button>
            </el-badge>
          </div>
          <div class="warning-list">
            <div
              v-for="warning in warningData"
              :key="warning.id"
              class="warning-item"
              :class="warning.level"
            >
              <div class="warning-icon">
                <i :class="getWarningIcon(warning.level)"></i>
              </div>
              <div class="warning-content">
                <div class="warning-title">{{ warning.title }}</div>
                <div class="warning-desc">{{ warning.description }}</div>
                <div class="warning-time">{{ warning.time | formatDate }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 详细数据表格 -->
      <el-col :span="16">
        <el-card class="data-card">
          <div slot="header" class="data-header">
            <span>预算执行明细</span>
            <el-button-group size="mini">
              <el-button icon="el-icon-download" @click="exportDetailData">
                导出数据
              </el-button>
              <el-button icon="el-icon-printer" @click="printDetailData">
                打印
              </el-button>
            </el-button-group>
          </div>
          <el-table
            :data="detailData"
            border
            stripe
            height="400"
            @sort-change="handleSortChange"
          >
            <el-table-column type="index" width="60" label="序号" align="center" />
            <el-table-column prop="budgetName" label="预算名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="department" label="部门" width="120" align="center" />
            <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right" sortable>
              <template slot-scope="scope">
                <span class="amount-text">{{ formatCurrency(scope.row.budgetAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="actualAmount" label="实际金额" width="120" align="right" sortable>
              <template slot-scope="scope">
                <span class="amount-text">{{ formatCurrency(scope.row.actualAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="executionRate" label="执行率" width="100" align="right" sortable>
              <template slot-scope="scope">
                <el-progress
                  :percentage="scope.row.executionRate"
                  :color="getExecutionRateColor(scope.row.executionRate)"
                  :stroke-width="8"
                />
              </template>
            </el-table-column>
            <el-table-column prop="variance" label="差异" width="100" align="right" sortable>
              <template slot-scope="scope">
                <span :class="getVarianceClass(scope.row.variance)">
                  {{ formatCurrency(scope.row.variance) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusTag(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-view"
                  @click="handleViewDetail(scope.row)"
                >
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination
            class="pagination"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.pageNo"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 预算详情对话框 -->
    <el-dialog
      title="预算详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedBudget">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="预算名称">{{ selectedBudget.budgetName }}</el-descriptions-item>
          <el-descriptions-item label="预算部门">{{ selectedBudget.department }}</el-descriptions-item>
          <el-descriptions-item label="预算类型">{{ selectedBudget.budgetType }}</el-descriptions-item>
          <el-descriptions-item label="预算期间">{{ selectedBudget.period }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ formatCurrency(selectedBudget.budgetAmount) }}</el-descriptions-item>
          <el-descriptions-item label="实际金额">{{ formatCurrency(selectedBudget.actualAmount) }}</el-descriptions-item>
          <el-descriptions-item label="执行率">{{ selectedBudget.executionRate }}%</el-descriptions-item>
          <el-descriptions-item label="差异金额">{{ formatCurrency(selectedBudget.variance) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 执行趋势图 -->
        <div style="margin-top: 20px;">
          <h4>执行趋势</h4>
          <div ref="detailTrendChart" style="width: 100%; height: 300px;"></div>
        </div>

        <!-- 明细数据 -->
        <div style="margin-top: 20px;">
          <h4>执行明细</h4>
          <el-table :data="selectedBudget.details" border size="small">
            <el-table-column prop="date" label="日期" width="120" />
            <el-table-column prop="item" label="项目" min-width="150" />
            <el-table-column prop="amount" label="金额" width="120" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="200" />
          </el-table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportDetail">导出详情</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatCurrency, formatDate } from '@/utils/index'
import * as echarts from 'echarts'

export default {
  name: 'BudgetAnalysis',
  data() {
    return {
      // 筛选表单
      filterForm: {
        period: '',
        department: '',
        budgetType: '',
        comparePeriod: []
      },

      // 基础数据
      periodList: [],
      departmentList: [],

      // KPI 数据
      kpiData: [
        {
          key: 'totalBudget',
          label: '预算总额',
          value: '0',
          change: 0,
          trend: 'up',
          color: '#409EFF',
          icon: 'el-icon-money'
        },
        {
          key: 'executionRate',
          label: '平均执行率',
          value: '0%',
          change: 0,
          trend: 'up',
          color: '#67C23A',
          icon: 'el-icon-s-finance'
        },
        {
          key: 'varianceAmount',
          label: '预算差异',
          value: '0',
          change: 0,
          trend: 'down',
          color: '#E6A23C',
          icon: 'el-icon-s-data'
        },
        {
          key: 'warningCount',
          label: '预警数量',
          value: '0',
          change: 0,
          trend: 'down',
          color: '#F56C6C',
          icon: 'el-icon-warning'
        }
      ],

      // 图表类型
      trendChartType: 'line',
      distributionChartType: 'pie',
      comparisonType: 'year',

      // 预警数据
      warningData: [],

      // 详细数据
      detailData: [],
      pagination: {
        pageNo: 1,
        pageSize: 20,
        total: 0
      },

      // 选中的预算详情
      selectedBudget: null,
      detailDialogVisible: false,

      // 图表实例
      charts: {
        trend: null,
        distribution: null,
        comparison: null,
        progress: null,
        detailTrend: null
      },

      loading: false
    }
  },

  created() {
    this.fetchBasicData()
    this.fetchAnalysisData()
  },

  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.windowResizeHandler()
    })
  },

  beforeDestroy() {
    // 清理图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
    // 移除窗口监听
    window.removeEventListener('resize', this.windowResizeHandler)
  },

  methods: {
    // 格式化货币
    formatCurrency,

    // 获取基础数据
    async fetchBasicData() {
      try {
        // 获取期间列表
        this.periodList = [
          { label: '2024年', value: '2024' },
          { label: '2023年', value: '2023' },
          { label: '2024年Q1', value: '2024Q1' },
          { label: '2024年Q2', value: '2024Q2' }
        ]

        // 获取部门列表
        const deptResponse = await budgetApi.getDepartmentList()
        if (deptResponse.code === 1) {
          this.departmentList = deptResponse.data || []
        }
      } catch (error) {
        console.error('获取基础数据异常:', error)
      }
    },

    // 获取分析数据
    async fetchAnalysisData() {
      this.loading = true
      try {
        // 获取仪表板数据
        const dashboardResponse = await budgetApi.getDashboardData(this.filterForm)
        if (dashboardResponse.code === 1) {
          this.updateKpiData(dashboardResponse.data.kpiData || {})
        }

        // 获取图表数据
        await this.fetchChartData()

        // 获取预警数据
        await this.fetchWarningData()

        // 获取详细数据
        await this.fetchDetailData()
      } catch (error) {
        console.error('获取分析数据异常:', error)
        this.$message.error('获取分析数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 更新KPI数据
    updateKpiData(data) {
      this.kpiData[0].value = formatCurrency(data.totalBudget || 0)
      this.kpiData[0].change = data.totalBudgetChange || 0

      this.kpiData[1].value = (data.executionRate || 0) + '%'
      this.kpiData[1].change = data.executionRateChange || 0

      this.kpiData[2].value = formatCurrency(data.varianceAmount || 0)
      this.kpiData[2].change = data.varianceChange || 0

      this.kpiData[3].value = data.warningCount || 0
      this.kpiData[3].change = data.warningChange || 0
    },

    // 获取图表数据
    async fetchChartData() {
      try {
        // 趋势图数据
        const trendResponse = await budgetApi.getTrendAnalysis(this.filterForm)
        if (trendResponse.code === 1) {
          this.renderTrendChart(trendResponse.data || [])
        }

        // 分布图数据
        const distributionResponse = await budgetApi.getChartData({
          ...this.filterForm,
          chartType: 'distribution'
        })
        if (distributionResponse.code === 1) {
          this.renderDistributionChart(distributionResponse.data || [])
        }

        // 对比图数据
        const comparisonResponse = await budgetApi.getComparisonData({
          ...this.filterForm,
          type: this.comparisonType
        })
        if (comparisonResponse.code === 1) {
          this.renderComparisonChart(comparisonResponse.data || [])
        }

        // 进度图数据
        const progressResponse = await budgetApi.getExecutionStats(this.filterForm)
        if (progressResponse.code === 1) {
          this.renderProgressChart(progressResponse.data || [])
        }
      } catch (error) {
        console.error('获取图表数据异常:', error)
      }
    },

    // 获取预警数据
    async fetchWarningData() {
      try {
        const response = await budgetApi.getWarningList({
          ...this.filterForm,
          pageNo: 1,
          pageSize: 10
        })
        if (response.code === 1) {
          this.warningData = response.data.tlist || []
        }
      } catch (error) {
        console.error('获取预警数据异常:', error)
      }
    },

    // 获取详细数据
    async fetchDetailData() {
      try {
        const params = {
          ...this.filterForm,
          pageNo: this.pagination.pageNo,
          pageSize: this.pagination.pageSize
        }

        const response = await budgetApi.getExecutionStats(params)
        if (response.code === 1) {
          this.detailData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        console.error('获取详细数据异常:', error)
      }
    },

    // 初始化图表
    initCharts() {
      // 趋势图
      this.charts.trend = echarts.init(this.$refs.trendChart)
      this.renderTrendChart([])

      // 分布图
      this.charts.distribution = echarts.init(this.$refs.distributionChart)
      this.renderDistributionChart([])

      // 对比图
      this.charts.comparison = echarts.init(this.$refs.comparisonChart)
      this.renderComparisonChart([])

      // 进度图
      this.charts.progress = echarts.init(this.$refs.progressChart)
      this.renderProgressChart([])
    },

    // 渲染趋势图
    renderTrendChart(data) {
      if (!this.charts.trend) return

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['预算金额', '实际金额', '执行率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.period) || []
        },
        yAxis: [
          {
            type: 'value',
            name: '金额',
            axisLabel: {
              formatter: value => formatCurrency(value)
            }
          },
          {
            type: 'value',
            name: '执行率',
            max: 100,
            axisLabel: {
              formatter: value => value + '%'
            }
          }
        ],
        series: [
          {
            name: '预算金额',
            type: this.trendChartType,
            data: data.map(item => item.budgetAmount) || []
          },
          {
            name: '实际金额',
            type: this.trendChartType,
            data: data.map(item => item.actualAmount) || []
          },
          {
            name: '执行率',
            type: 'line',
            yAxisIndex: 1,
            data: data.map(item => item.executionRate) || []
          }
        ]
      }

      this.charts.trend.setOption(option)
    },

    // 渲染分布图
    renderDistributionChart(data) {
      if (!this.charts.distribution) return

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '预算分布',
            type: this.distributionChartType,
            data: data.map(item => ({
              value: item.amount,
              name: item.name
            })) || []
          }
        ]
      }

      this.charts.distribution.setOption(option)
    },

    // 渲染对比图
    renderComparisonChart(data) {
      if (!this.charts.comparison) return

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['本期', '上期']
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.category) || []
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: value => formatCurrency(value)
          }
        },
        series: [
          {
            name: '本期',
            type: 'bar',
            data: data.map(item => item.current) || []
          },
          {
            name: '上期',
            type: 'bar',
            data: data.map(item => item.previous) || []
          }
        ]
      }

      this.charts.comparison.setOption(option)
    },

    // 渲染进度图
    renderProgressChart(data) {
      if (!this.charts.progress) return

      const option = {
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
          type: 'value',
          max: 100
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name) || []
        },
        series: [
          {
            name: '执行率',
            type: 'bar',
            data: data.map(item => ({
              value: item.rate,
              itemStyle: {
                color: this.getExecutionRateColor(item.rate)
              }
            })) || []
          }
        ]
      }

      this.charts.progress.setOption(option)
    },

    // 窗口大小变化处理
    windowResizeHandler() {
      window.addEventListener('resize', () => {
        Object.values(this.charts).forEach(chart => {
          if (chart) {
            chart.resize()
          }
        })
      })
    },

    // 刷新进度图
    refreshProgressChart() {
      this.fetchChartData()
    },

    // 刷新预警
    handleRefreshWarnings() {
      this.fetchWarningData()
    },

    // 筛选
    handleFilter() {
      this.pagination.pageNo = 1
      this.fetchAnalysisData()
    },

    // 重置
    handleReset() {
      this.$refs.filterForm.resetFields()
      this.filterForm.comparePeriod = []
      this.pagination.pageNo = 1
      this.fetchAnalysisData()
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await budgetApi.exportAnalysisReport(this.filterForm)
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `预算分析报告_${new Date().getTime()}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告导出成功')
      } catch (error) {
        console.error('导出报告异常:', error)
        this.$message.error('导出报告失败，请稍后重试')
      }
    },

    // 导出详细数据
    exportDetailData() {
      this.handleExportReport()
    },

    // 打印详细数据
    printDetailData() {
      window.print()
    },

    // 查看详情
    handleViewDetail(row) {
      this.selectedBudget = { ...row }
      this.detailDialogVisible = true

      this.$nextTick(() => {
        this.renderDetailTrendChart(row.details || [])
      })
    },

    // 渲染详情趋势图
    renderDetailTrendChart(data) {
      if (!this.$refs.detailTrendChart) return

      if (this.charts.detailTrend) {
        this.charts.detailTrend.dispose()
      }

      this.charts.detailTrend = echarts.init(this.$refs.detailTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date) || []
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: value => formatCurrency(value)
          }
        },
        series: [
          {
            type: 'line',
            data: data.map(item => item.amount) || [],
            smooth: true,
            areaStyle: {
              opacity: 0.3
            }
          }
        ]
      }

      this.charts.detailTrend.setOption(option)
    },

    // 导出详情
    handleExportDetail() {
      const data = this.tableData || this.list || this.detailData || []
      if ((!Array.isArray(data) && !data) || (Array.isArray(data) && !data.length)) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '数据导出.json'
      link.click()
      window.URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNo = 1
      this.fetchDetailData()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.pageNo = page
      this.fetchDetailData()
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      // 实现排序逻辑
      console.log('排序变化:', { column, prop, order })
    },

    // 获取趋势图标
    getTrendIcon(trend) {
      const iconMap = {
        'up': 'el-icon-top',
        'down': 'el-icon-bottom',
        'flat': 'el-icon-minus'
      }
      return iconMap[trend] || 'el-icon-minus'
    },

    // 获取预警图标
    getWarningIcon(level) {
      const iconMap = {
        'high': 'el-icon-warning',
        'medium': 'el-icon-info',
        'low': 'el-icon-message'
      }
      return iconMap[level] || 'el-icon-info'
    },

    // 获取执行率颜色
    getExecutionRateColor(rate) {
      if (rate < 50) return '#F56C6C'
      if (rate < 80) return '#E6A23C'
      return '#67C23A'
    },

    // 获取差异样式类
    getVarianceClass(variance) {
      return variance < 0 ? 'negative-variance' : 'positive-variance'
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'normal': 'success',
        'warning': 'warning',
        'danger': 'danger',
        'completed': 'info'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'normal': '正常',
        'warning': '预警',
        'danger': '超支',
        'completed': '已完成'
      }
      return textMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-analysis-container {
  padding: 20px;

  .filter-card {
    margin-bottom: 20px;
  }

  // KPI 指标卡样式
  .kpi-row {
    margin-bottom: 20px;

    .kpi-card {
      .kpi-content {
        display: flex;
        align-items: center;

        .kpi-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: white;
          }
        }

        .kpi-info {
          flex: 1;

          .kpi-value {
            font-size: 28px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .kpi-label {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }

          .kpi-trend {
            display: flex;
            align-items: center;
            font-size: 12px;

            &.up {
              color: #67C23A;
            }

            &.down {
              color: #F56C6C;
            }

            &.flat {
              color: #909399;
            }

            i {
              margin-right: 4px;
            }
          }
        }
      }
    }
  }

  // 图表样式
  .chart-row {
    margin-bottom: 20px;

    .chart-card {
      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .chart-container {
        width: 100%;
        height: 300px;
      }
    }
  }

  // 数据区域样式
  .data-row {
    .warning-card {
      .warning-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .warning-badge {
          margin-right: 8px;
        }
      }

      .warning-list {
        max-height: 400px;
        overflow-y: auto;

        .warning-item {
          display: flex;
          padding: 12px;
          margin-bottom: 8px;
          border-radius: 6px;
          border-left: 4px solid;

          &.high {
            background: #FEF0F0;
            border-left-color: #F56C6C;
          }

          &.medium {
            background: #FDF6EC;
            border-left-color: #E6A23C;
          }

          &.low {
            background: #F0F9FF;
            border-left-color: #409EFF;
          }

          .warning-icon {
            margin-right: 12px;
            font-size: 16px;
          }

          .warning-content {
            flex: 1;

            .warning-title {
              font-weight: 600;
              color: #303133;
              margin-bottom: 4px;
            }

            .warning-desc {
              font-size: 12px;
              color: #606266;
              margin-bottom: 4px;
            }

            .warning-time {
              font-size: 11px;
              color: #909399;
            }
          }
        }
      }
    }

    .data-card {
      .data-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .amount-text {
        font-weight: 600;
        color: #E6A23C;
      }

      .positive-variance {
        color: #67C23A;
        font-weight: 600;
      }

      .negative-variance {
        color: #F56C6C;
        font-weight: 600;
      }

      .pagination {
        margin-top: 20px;
        text-align: right;
      }
    }
  }
}

// 打印样式
@media print {
  .filter-card,
  .kpi-row,
  .chart-row,
  .warning-card,
  .data-header,
  .pagination {
    display: none !important;
  }

  .data-card {
    .el-card__header {
      display: none !important;
    }
  }
}
</style>