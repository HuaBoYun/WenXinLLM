<template>
  <div class="enterprise-dashboard" :style="themeVars">
    <!-- 企业选择和刷新控制 -->
    <el-card class="mb-20">
      <el-row :gutter="20" type="flex" justify="space-between" align="middle">
        <el-col :span="12">
          <div class="enterprise-selector">
            <el-input
              :value="selectedEnterpriseName"
              placeholder="请选择企业"
              readonly
              clearable
              style="width: 300px;"
              @click.native="openCompanyTree"
              @clear="handleClearEnterprise"
            >
              <el-button slot="append" icon="el-icon-search" @click="openCompanyTree"></el-button>
            </el-input>
            <CompanyTreeModal
              ref="companyTreeModal"
              @selected="handleCompanySelected"
            />
            <el-button type="primary" @click="refreshData" :loading="refreshing" style="margin-left: 10px;">
              <i class="el-icon-refresh"></i> 刷新数据
            </el-button>
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div class="dashboard-controls">
            <el-button @click="handleExport" icon="el-icon-download">导出报告</el-button>
            <el-button @click="handleConfig" icon="el-icon-setting">配置</el-button>
            <span class="last-update">最后更新：{{ lastUpdateTime }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 企业基本信息概览 -->
    <el-card class="mb-20" v-if="enterpriseOverview && enterpriseOverview.enterpriseName">
      <div slot="header" class="card-header">
        <span class="card-title">企业概览</span>
        <el-tag :type="getStatusTag(enterpriseOverview.operatingStatus)">
          {{ getStatusText(enterpriseOverview.operatingStatus) }}
        </el-tag>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-item">
            <div class="overview-label">企业名称</div>
            <div class="overview-value">{{ enterpriseOverview.enterpriseName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-item">
            <div class="overview-label">行业分类</div>
            <div class="overview-value">{{ enterpriseOverview.industryClassification }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-item">
            <div class="overview-label">企业规模</div>
            <div class="overview-value">{{ enterpriseOverview.enterpriseScale }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-item">
            <div class="overview-label">上市状态</div>
            <div class="overview-value">{{ enterpriseOverview.listingStatus }}</div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="overview-item">
            <div class="overview-label">主要业务</div>
            <div class="overview-value">{{ enterpriseOverview.mainBusinessDesc }}</div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="overview-item">
            <div class="overview-label">地域分布</div>
            <div class="overview-value">{{ enterpriseOverview.geographicalDistribution }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 关键指标卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="indicator-card">
          <div class="indicator-content">
            <div class="indicator-icon revenue">
              <i class="el-icon-money"></i>
            </div>
            <div class="indicator-info">
              <div class="indicator-value">{{ formatAmount(keyIndicators.operatingRevenue) }}</div>
              <div class="indicator-label">营业收入(万元)</div>
              <div class="indicator-trend" :class="getTrendClass(keyIndicators.revenueGrowthRate)">
                <i :class="getTrendIcon(keyIndicators.revenueGrowthRate)"></i>
                {{ keyIndicators.revenueGrowthRate }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="indicator-card">
          <div class="indicator-content">
            <div class="indicator-icon profit">
              <i class="el-icon-s-finance"></i>
            </div>
            <div class="indicator-info">
              <div class="indicator-value">{{ formatAmount(keyIndicators.netProfit) }}</div>
              <div class="indicator-label">净利润(万元)</div>
              <div class="indicator-trend" :class="getTrendClass(keyIndicators.profitGrowthRate)">
                <i :class="getTrendIcon(keyIndicators.profitGrowthRate)"></i>
                {{ keyIndicators.profitGrowthRate }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="indicator-card">
          <div class="indicator-content">
            <div class="indicator-icon assets">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="indicator-info">
              <div class="indicator-value">{{ formatAmount(keyIndicators.totalAssets) }}</div>
              <div class="indicator-label">总资产(万元)</div>
              <div class="indicator-trend">
                <span>负债率: {{ keyIndicators.assetLiabilityRatio }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="indicator-card">
          <div class="indicator-content">
            <div class="indicator-icon roe">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="indicator-info">
              <div class="indicator-value">{{ keyIndicators.roe }}%</div>
              <div class="indicator-label">净资产收益率</div>
              <div class="indicator-trend">
                <span>ROA: {{ keyIndicators.roa }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 业务状态和风险监控 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">业务状态监控</span>
          </div>
          <div class="business-status">
            <div class="status-item">
              <span class="status-label">子公司数量</span>
              <span class="status-value">{{ businessStatus.subsidiaryCount }}家</span>
            </div>
            <div class="status-item">
              <span class="status-label">业务板块</span>
              <span class="status-value">{{ businessStatus.businessSegmentCount }}个</span>
            </div>
            <div class="status-item">
              <span class="status-label">员工总数</span>
              <span class="status-value">{{ businessStatus.employeeCount }}人</span>
            </div>
            <div class="status-item">
              <span class="status-label">发展趋势</span>
              <el-tag :type="getTrendTag(businessStatus.developmentTrend)">
                {{ getTrendText(businessStatus.developmentTrend) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">风险监控</span>
            <el-tag :type="getRiskLevelTag(riskMonitoring.riskLevel)">
              {{ riskMonitoring.riskLevel }}
            </el-tag>
          </div>
          <div class="risk-monitoring">
            <div class="risk-score-circle">
              <el-progress
                type="circle"
                :percentage="riskMonitoring.riskScore"
                :color="getRiskScoreColor(riskMonitoring.riskScore)"
                :width="120"
              >
                <span class="risk-score-text">{{ riskMonitoring.riskScore }}</span>
              </el-progress>
            </div>
            <div class="risk-details">
              <div class="risk-item">
                <span class="risk-label">预警数量</span>
                <span class="risk-value warning">{{ riskMonitoring.warningCount }}个</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">合规评分</span>
                <span class="risk-value">{{ riskMonitoring.complianceScore }}分</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">数据报送状态</span>
          </div>
          <div class="submission-status">
            <div class="submission-progress">
              <el-progress
                :percentage="submissionStatus.completionRate"
                :color="getCompletionColor(submissionStatus.completionRate)"
                :show-text="false"
              ></el-progress>
              <div class="progress-text">
                <span class="completion-rate">{{ submissionStatus.completionRate }}%</span>
                <span class="completion-label">完成率</span>
              </div>
            </div>
            <div class="submission-details">
              <div class="submission-item">
                <span class="submission-label">数据质量评分</span>
                <span class="submission-value">{{ submissionStatus.dataQualityScore }}分</span>
              </div>
              <div class="submission-item">
                <span class="submission-label">最后报送时间</span>
                <span class="submission-value">{{ submissionStatus.lastSubmissionTime }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务指标趋势图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">财务指标趋势</span>
            <el-button-group size="mini">
              <el-button @click="changeTrendPeriod('6M')" :type="trendPeriod === '6M' ? 'primary' : ''">6个月</el-button>
              <el-button @click="changeTrendPeriod('1Y')" :type="trendPeriod === '1Y' ? 'primary' : ''">1年</el-button>
              <el-button @click="changeTrendPeriod('3Y')" :type="trendPeriod === '3Y' ? 'primary' : ''">3年</el-button>
            </el-button-group>
          </div>
          <div class="chart-container">
            <div ref="financialTrendChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">业务分布</span>
          </div>
          <div class="chart-container">
            <div ref="businessDistributionChart" style="width: 100%; height: 300px;"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 经营计划执行和预算执行 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">经营计划执行情况</span>
          </div>
          <div class="plan-execution">
            <div class="execution-item" v-for="item in planExecution" :key="item.indicator">
              <div class="execution-header">
                <span class="execution-label">{{ item.indicator }}</span>
                <span class="execution-rate" :class="getExecutionClass(item.completionRate)">
                  {{ item.completionRate }}%
                </span>
              </div>
              <el-progress
                :percentage="item.completionRate"
                :color="getExecutionColor(item.completionRate)"
                :show-text="false"
              ></el-progress>
              <div class="execution-details">
                <span>目标: {{ item.target }}</span>
                <span>实际: {{ item.actual }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">预算执行情况</span>
          </div>
          <div class="budget-execution">
            <div class="budget-item" v-for="item in budgetExecution" :key="item.category">
              <div class="budget-header">
                <span class="budget-label">{{ item.category }}</span>
                <span class="budget-rate" :class="getBudgetClass(item.executionRate)">
                  {{ item.executionRate }}%
                </span>
              </div>
              <el-progress
                :percentage="item.executionRate"
                :color="getBudgetColor(item.executionRate)"
                :show-text="false"
              ></el-progress>
              <div class="budget-details">
                <span>预算: {{ formatAmount(item.budget) }}万</span>
                <span>执行: {{ formatAmount(item.executed) }}万</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 配置对话框 -->
    <DashboardConfigDialog
      :visible.sync="configDialogVisible"
      :enterprise-id="selectedEnterpriseId"
      @refresh="loadDashboardData"
    />
  </div>
</template>

<script>
import {
  getEnterpriseDashboard,
  getEnterpriseOverview,
  getKeyIndicators,
  getBusinessStatus,
  getFinancialTrend,
  getRiskMonitoring,
  getDataSubmissionStatus,
  getOperatingPlanExecution,
  getBudgetExecution,
  getBusinessDistribution,
  refreshDashboardData,
  exportDashboardReport
} from '@/api/enterprise/dashboard'
import { findOrganization } from '@/api/setting/org'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import DashboardConfigDialog from './components/DashboardConfigDialog'
import * as echarts from 'echarts'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'EnterpriseDashboard',
  mixins: [investThemeMixin],
  components: {
    CompanyTreeModal,
    DashboardConfigDialog
  },
  data() {
    return {
      selectedEnterpriseId: '',
      selectedEnterpriseName: '',
      refreshing: false,
      configDialogVisible: false,
      lastUpdateTime: '',
      trendPeriod: '1Y',
      
      // 数据
      enterpriseOverview: null,
      keyIndicators: {},
      businessStatus: {},
      riskMonitoring: {},
      submissionStatus: {},
      planExecution: [],
      budgetExecution: [],
      
      // 图表实例
      financialTrendChart: null,
      businessDistributionChart: null
    }
  },
  mounted() {
    // 从路由参数获取企业ID
    if (this.$route.query.enterpriseId) {
      this.selectedEnterpriseId = this.$route.query.enterpriseId
      this.selectedEnterpriseName = this.$route.query.enterpriseName || ''
      this.$nextTick(() => {
        this.initCharts()
        this.loadDashboardData()
      })
    } else {
      // 无路由参数时，尝试自动加载第一个企业的数据
      this.$nextTick(() => {
        this.initCharts()
        this.loadFirstEnterprise()
      })
    }
  },
  beforeDestroy() {
    // 销毁图表实例
    if (this.financialTrendChart) {
      this.financialTrendChart.dispose()
    }
    if (this.businessDistributionChart) {
      this.businessDistributionChart.dispose()
    }
  },
  methods: {
    // 打开企业选择对话框（与 financial 等其他企业页面保持一致的交互）
    openCompanyTree() {
      this.$refs.companyTreeModal.show()
    },

    // 企业选择确认回调
    handleCompanySelected(node) {
      if (node && node.id != null) {
        this.selectedEnterpriseId = String(node.id)
        this.selectedEnterpriseName = node.label || node.name || ''
        this.loadDashboardData()
      }
    },

    // 清空企业选择
    handleClearEnterprise() {
      this.selectedEnterpriseId = ''
      this.selectedEnterpriseName = ''
      // 清空各模块数据，避免显示上一个企业的残留
      this.enterpriseOverview = null
      this.keyIndicators = {}
      this.businessStatus = {}
      this.riskMonitoring = {}
      this.submissionStatus = {}
      this.planExecution = []
      this.budgetExecution = []
      if (this.financialTrendChart) this.financialTrendChart.clear()
      if (this.businessDistributionChart) this.businessDistributionChart.clear()
    },

    // 自动加载第一个企业（与 financial / risk / hr 等子页面行为一致：从组织树取第一项）
    async loadFirstEnterprise() {
      try {
        const res = await findOrganization({})
        if (res && res.data && res.data.length > 0) {
          const first = res.data[0]
          this.selectedEnterpriseId = String(first.id)
          this.selectedEnterpriseName = first.name || first.label || ''
          this.loadDashboardData()
          return
        }
      } catch (e) {
        console.warn('自动加载默认企业失败，请手动选择企业:', e)
      }
      // 没有可用企业时，提示用户主动选择，不再硬编码兜底
      this.$message.info('请先选择需要查看的企业')
    },

    // 加载驾驶舱数据
    async loadDashboardData() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }

      const loading = this.$loading({
        lock: true,
        text: '正在加载驾驶舱数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.85)'
      })

      try {
        // 并行加载所有数据，使用 allSettled 避免单个失败影响全部
        const results = await Promise.allSettled([
          getEnterpriseOverview({ enterpriseId: this.selectedEnterpriseId }),
          getKeyIndicators({ enterpriseId: this.selectedEnterpriseId }),
          getBusinessStatus({ enterpriseId: this.selectedEnterpriseId }),
          getRiskMonitoring({ enterpriseId: this.selectedEnterpriseId }),
          getDataSubmissionStatus({ enterpriseId: this.selectedEnterpriseId }),
          getOperatingPlanExecution({ enterpriseId: this.selectedEnterpriseId }),
          getBudgetExecution({ enterpriseId: this.selectedEnterpriseId })
        ])

        // 安全取值：fulfilled 时取 data，rejected 时用默认值
        const getData = (r, fallback) => {
          if (r.status === 'fulfilled' && r.value) {
            const val = r.value
            // 兼容 { result: 200, data: {...} } 格式
            const raw = val.data !== undefined ? val.data : val
            if (raw === null || raw === undefined) return fallback
            return raw
          }
          return fallback
        }

        this.enterpriseOverview = getData(results[0], {})
        this.keyIndicators = getData(results[1], {})
        this.businessStatus = getData(results[2], {})
        this.riskMonitoring = getData(results[3], {})

        console.log('[Dashboard] keyIndicators:', this.keyIndicators)
        console.log('[Dashboard] businessStatus:', this.businessStatus)

        // Map submission status fields from backend format
        const submissionData = getData(results[4], {})
        this.submissionStatus = {
          completionRate: submissionData.submissionCompletionRate || submissionData.completionRate || submissionData.SUBMISSION_COMPLETION_RATE || 0,
          dataQualityScore: submissionData.dataQualityScore || submissionData.DATA_QUALITY_SCORE || 0,
          lastSubmissionTime: submissionData.lastSubmissionTime || submissionData.LAST_UPDATE_TIME || '-'
        }

        // Map plan execution fields from backend format
        const planData = getData(results[5], [])
        this.planExecution = (Array.isArray(planData) ? planData : []).map(item => ({
          indicator: item.planName || item.planname || item.PLAN_NAME || '',
          completionRate: Number(item.executionRate || item.executionrate || item.EXECUTION_RATE || 0),
          target: item.target || item.targetValue || item.TARGET_VALUE || '-',
          actual: item.actual || item.actualValue || item.ACTUAL_VALUE || '-'
        }))

        // Map budget execution fields from backend format
        const budgetData = getData(results[6], [])
        this.budgetExecution = (Array.isArray(budgetData) ? budgetData : []).map(item => ({
          category: item.budgetName || item.budgetname || item.BUDGET_NAME || '',
          executionRate: Number(item.executionRate || item.executionrate || item.EXECUTION_RATE || 0),
          budget: Number(item.budget || item.budgetAmount || item.BUDGET_AMOUNT || 0),
          executed: Number(item.executed || item.executedAmount || item.EXECUTED_AMOUNT || 0)
        }))

        this.lastUpdateTime = new Date().toLocaleString()

        // 加载图表数据
        this.loadFinancialTrendChart()
        this.loadBusinessDistributionChart()
      } catch (error) {
        console.error('加载驾驶舱数据失败:', error)
        this.$message.error('加载驾驶舱数据失败')
      } finally {
        loading.close()
      }
    },

    // 刷新数据
    async refreshData() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }

      this.refreshing = true
      try {
        await refreshDashboardData(this.selectedEnterpriseId)
        await this.loadDashboardData()
        this.$message.success('数据刷新成功')
      } catch (error) {
        console.error('数据刷新失败:', error)
        this.$message.error('数据刷新失败：' + (error.message || '未知错误'))
      } finally {
        this.refreshing = false
      }
    },

    // 导出报告
    async handleExport() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }

      const loading = this.$loading({
        lock: true,
        text: '正在生成报告...',
        spinner: 'el-icon-loading',
        background: 'rgba(255, 255, 255, 0.85)'
      })

      try {
        const response = await exportDashboardReport({
          enterpriseId: this.selectedEnterpriseId,
          enterpriseName: this.selectedEnterpriseName,
          reportType: 'excel'
        })

        const blob = response && response.data instanceof Blob ? response.data : null
        if (!blob) {
          this.$message.warning('暂无可导出的数据')
          return
        }

        // 关键：responseType 是 blob，所以即使后端返回 JSON 错误体也会被包成 Blob
        // 必须先按 type 判断是真文件还是 JSON 错误响应
        const isJsonError =
          blob.type && blob.type.toLowerCase().indexOf('application/json') !== -1
        if (isJsonError) {
          const errorText = await blob.text()
          let msg = '未知错误'
          try {
            const errorJson = JSON.parse(errorText)
            msg = errorJson.msg || errorJson.message || errorText
          } catch (e) {
            msg = errorText || msg
          }
          this.$message.error('报告导出失败：' + msg)
          return
        }

        // 真正的 xlsx 下载
        const fileName = (this.selectedEnterpriseName || '企业') + '管理驾驶舱报告.xlsx'
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(link.href)
        this.$message.success('报告导出成功')
      } catch (error) {
        console.error('报告导出失败:', error)
        // axios 异常分支：error.response.data 仍可能是 Blob
        if (error && error.response && error.response.data instanceof Blob) {
          try {
            const errorText = await error.response.data.text()
            const errorJson = JSON.parse(errorText)
            this.$message.error('报告导出失败：' + (errorJson.msg || errorJson.message || '未知错误'))
            return
          } catch (e) {
            // 不是 JSON 错误响应
          }
        }
        this.$message.error('报告导出失败：' + (error.message || '未知错误'))
      } finally {
        loading.close()
      }
    },

    // 配置
    handleConfig() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      this.configDialogVisible = true
    },

    // 初始化图表
    initCharts() {
      this.financialTrendChart = echarts.init(this.$refs.financialTrendChart)
      this.businessDistributionChart = echarts.init(this.$refs.businessDistributionChart)
    },

    // 加载财务趋势图表
    async loadFinancialTrendChart() {
      try {
        // Convert period string to months integer for backend
        const periodMap = { '6M': 6, '1Y': 12, '3Y': 36 }
        const months = periodMap[this.trendPeriod] || 12

        const response = await getFinancialTrend({
          enterpriseId: this.selectedEnterpriseId,
          months: months
        })

        // Backend returns list of maps, transform to arrays for chart
        const rawData = (response && response.data) || []
        const periods = []
        const revenue = []
        const profit = []
        const roe = []

        if (Array.isArray(rawData) && rawData.length > 0) {
          rawData.forEach(item => {
            periods.push(item.period || item.PERIOD || '')
            revenue.push(Number(item.revenue || item.REVENUE || 0))
            profit.push(Number(item.profit || item.PROFIT || 0))
            roe.push(Number(item.roe || item.ROE || 0))
          })
        } else {
          // 无数据时使用占位
          periods.push('暂无数据')
          revenue.push(0)
          profit.push(0)
          roe.push(0)
        }

        const option = {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['营业收入', '净利润', 'ROE']
          },
          xAxis: {
            type: 'category',
            data: periods
          },
          yAxis: [
            {
              type: 'value',
              name: '金额(万元)',
              position: 'left'
            },
            {
              type: 'value',
              name: 'ROE(%)',
              position: 'right'
            }
          ],
          series: [
            {
              name: '营业收入',
              type: 'line',
              data: revenue,
              yAxisIndex: 0,
              smooth: true
            },
            {
              name: '净利润',
              type: 'line',
              data: profit,
              yAxisIndex: 0,
              smooth: true
            },
            {
              name: 'ROE',
              type: 'line',
              data: roe,
              yAxisIndex: 1,
              smooth: true
            }
          ]
        }

        this.financialTrendChart.setOption(option)
      } catch (error) {
        console.error('加载财务趋势图表失败:', error)
      }
    },

    // 加载业务分布图表
    async loadBusinessDistributionChart() {
      try {
        const response = await getBusinessDistribution({
          enterpriseId: this.selectedEnterpriseId
        })

        let data = (response && response.data) || []
        // 兼容字段名大小写
        if (Array.isArray(data)) {
          data = data.map(item => ({
            name: item.name || item.NAME || item.businessName || item.BUSINESS_NAME || '未知',
            value: Number(item.value || item.VALUE || item.businessValue || item.BUSINESS_VALUE || 0)
          }))
        }
        if (!data.length) {
          data = [{ name: '暂无数据', value: 1 }]
        }
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '业务分布',
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }

        this.businessDistributionChart.setOption(option)
      } catch (error) {
        console.error('加载业务分布图表失败:', error)
        // 失败时显示空图表
        if (this.businessDistributionChart) {
          this.businessDistributionChart.setOption({
            series: [{ type: 'pie', radius: '50%', data: [{ name: '暂无数据', value: 1 }] }]
          })
        }
      }
    },

    // 改变趋势期间
    changeTrendPeriod(period) {
      this.trendPeriod = period
      this.loadFinancialTrendChart()
    },

    // 格式化金额（数据库已存储万元，直接格式化显示）
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0'
      const num = Number(amount)
      if (isNaN(num)) return '0'
      if (num >= 10000) return (num / 10000).toFixed(2) + '亿'
      return num.toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'NORMAL': 'success',
        'GOOD': 'success',
        'WARNING': 'warning',
        'ABNORMAL': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'GOOD': '良好',
        'WARNING': '预警',
        'ABNORMAL': '异常'
      }
      return textMap[status] || status
    },

    // 获取趋势样式类
    getTrendClass(rate) {
      if (rate > 0) return 'trend-up'
      if (rate < 0) return 'trend-down'
      return 'trend-flat'
    },

    // 获取趋势图标
    getTrendIcon(rate) {
      if (rate > 0) return 'el-icon-top'
      if (rate < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },

    // 获取趋势标签
    getTrendTag(trend) {
      const tagMap = {
        '上升': 'success',
        '稳定': 'primary',
        '下降': 'warning',
        'UP': 'success',
        'STABLE': 'primary',
        'DOWN': 'warning'
      }
      return tagMap[trend] || 'info'
    },

    // 趋势码值转中文（库存 UP/STABLE/DOWN，前端展示中文）
    getTrendText(trend) {
      const textMap = {
        'UP': '上升',
        'STABLE': '稳定',
        'DOWN': '下降',
        '上升': '上升',
        '稳定': '稳定',
        '下降': '下降'
      }
      return textMap[trend] || (trend || '-')
    },

    // 获取风险等级标签
    getRiskLevelTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取风险评分颜色
    getRiskScoreColor(score) {
      if (score >= 80) return '#67C23A'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取完成率颜色
    getCompletionColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取执行率样式类
    getExecutionClass(rate) {
      if (rate >= 90) return 'execution-good'
      if (rate >= 70) return 'execution-normal'
      return 'execution-poor'
    },

    // 获取执行率颜色
    getExecutionColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取预算执行样式类
    getBudgetClass(rate) {
      if (rate >= 80 && rate <= 100) return 'budget-good'
      if (rate > 100) return 'budget-over'
      return 'budget-poor'
    },

    // 获取预算执行颜色
    getBudgetColor(rate) {
      if (rate >= 80 && rate <= 100) return '#67C23A'
      if (rate > 100) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style scoped>
.enterprise-dashboard {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.enterprise-selector {
  display: flex;
  align-items: center;
}

.dashboard-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.last-update {
  color: #909399;
  font-size: 12px;
}

.overview-item {
  margin-bottom: 10px;
}

.overview-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.overview-value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.indicator-card {
  height: 120px;
}

.indicator-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.indicator-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  margin-right: 15px;
}

.indicator-icon.revenue {
  background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
}

.indicator-icon.profit {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.indicator-icon.assets {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.indicator-icon.roe {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.indicator-info {
  flex: 1;
}

.indicator-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.indicator-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.indicator-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.trend-up {
  color: #67C23A;
}

.trend-down {
  color: #F56C6C;
}

.trend-flat {
  color: #909399;
}

.business-status,
.risk-monitoring,
.submission-status {
  padding: 10px 0;
}

.status-item,
.risk-item,
.submission-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.status-label,
.risk-label,
.submission-label {
  font-size: 14px;
  color: #606266;
}

.status-value,
.risk-value,
.submission-value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.risk-value.warning {
  color: #E6A23C;
}

.risk-monitoring {
  display: flex;
  align-items: center;
  gap: 20px;
}

.risk-score-circle {
  flex-shrink: 0;
}

.risk-score-text {
  font-size: 16px;
  font-weight: bold;
}

.risk-details {
  flex: 1;
}

.submission-progress {
  margin-bottom: 15px;
}

.progress-text {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
}

.completion-rate {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.completion-label {
  font-size: 12px;
  color: #909399;
}

.plan-execution,
.budget-execution {
  padding: 10px 0;
}

.execution-item,
.budget-item {
  margin-bottom: 20px;
}

.execution-header,
.budget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.execution-label,
.budget-label {
  font-size: 14px;
  color: #606266;
}

.execution-rate,
.budget-rate {
  font-size: 14px;
  font-weight: bold;
}

.execution-good,
.budget-good {
  color: #67C23A;
}

.execution-normal {
  color: #E6A23C;
}

.execution-poor,
.budget-poor {
  color: #F56C6C;
}

.budget-over {
  color: #E6A23C;
}

.execution-details,
.budget-details {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.chart-container {
  padding: 10px 0;
}
</style>
