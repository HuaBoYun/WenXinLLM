<template>
  <div class="revenue-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-pie-chart"></i>
          收入分析
        </h1>
        <p class="page-description">分析收入结构、趋势和绩效，提供收入质量分析和预测模型</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-download" @click="exportAnalysis">
          导出分析
        </el-button>
        <el-button type="success" icon="el-icon-data-line" @click="generateForecast">
          生成预测
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 分析概览 -->
    <div class="analysis-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon revenue">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.totalRevenue) }}</div>
              <div class="card-label">总收入</div>
              <div class="card-trend">
                <i :class="overview.revenueTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.revenueTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon growth">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.growthRate }}%</div>
              <div class="card-label">增长率</div>
              <div class="card-trend">
                <i :class="overview.growthTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.growthTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon quality">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.qualityScore }}</div>
              <div class="card-label">质量评分</div>
              <div class="card-trend">
                <i :class="overview.qualityTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.qualityTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon forecast">
              <i class="el-icon-view"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.forecastRevenue) }}</div>
              <div class="card-label">预测收入</div>
              <div class="card-trend">
                <i :class="overview.forecastTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.forecastTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析选项 -->
    <div class="analysis-options">
      <el-form :model="analysisForm" :inline="true" size="small">
        <el-form-item label="分析维度">
          <el-select v-model="analysisForm.dimension" placeholder="请选择分析维度" @change="onDimensionChange">
            <el-option label="时间维度" :value="1" />
            <el-option label="产品维度" :value="2" />
            <el-option label="客户维度" :value="3" />
            <el-option label="部门维度" :value="4" />
            <el-option label="地区维度" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析期间">
          <el-date-picker
            v-model="analysisForm.dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
            @change="onDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="对比类型">
          <el-select v-model="analysisForm.comparisonType" placeholder="请选择对比类型" @change="onComparisonChange">
            <el-option label="同比" :value="1" />
            <el-option label="环比" :value="2" />
            <el-option label="预算对比" :value="3" />
            <el-option label="目标对比" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="performAnalysis">分析</el-button>
          <el-button icon="el-icon-refresh" @click="resetAnalysis">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 分析结果 -->
    <div class="analysis-results">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 结构分析 -->
        <el-tab-pane label="结构分析" name="structure">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>收入结构分析</h3>
              <p>按不同维度分析收入构成和占比</p>
            </div>
            <div class="chart-container">
              <div class="chart-placeholder">
                <i class="el-icon-pie-chart"></i>
                <p>收入结构饼图</p>
                <p class="chart-desc">显示各维度收入占比分布</p>
              </div>
            </div>
            <div class="structure-table">
              <el-table :data="structureData" border>
                <el-table-column prop="category" label="类别" width="150" />
                <el-table-column prop="amount" label="金额" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100">
                  <template slot-scope="scope">
                    <span>{{ scope.row.percentage }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="growth" label="增长率" width="100">
                  <template slot-scope="scope">
                    <span :class="scope.row.growth >= 0 ? 'growth-positive' : 'growth-negative'">
                      {{ scope.row.growth >= 0 ? '+' : '' }}{{ scope.row.growth }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="rank" label="排名" width="80" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 趋势分析 -->
        <el-tab-pane label="趋势分析" name="trend">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>收入趋势分析</h3>
              <p>分析收入随时间的变化趋势和周期性</p>
            </div>
            <div class="chart-container">
              <div class="chart-placeholder">
                <i class="el-icon-data-line"></i>
                <p>收入趋势图</p>
                <p class="chart-desc">显示收入随时间变化趋势</p>
              </div>
            </div>
            <div class="trend-summary">
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">平均增长率</div>
                    <div class="summary-value growth-positive">+{{ trendSummary.avgGrowthRate }}%</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">最高增长月</div>
                    <div class="summary-value">{{ trendSummary.peakMonth }}</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">波动系数</div>
                    <div class="summary-value">{{ trendSummary.volatility }}</div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>

        <!-- 质量分析 -->
        <el-tab-pane label="质量分析" name="quality">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>收入质量分析</h3>
              <p>评估收入的可持续性、稳定性和风险水平</p>
            </div>
            <div class="quality-metrics">
              <el-row :gutter="24">
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-icon sustainability">
                      <i class="el-icon-refresh"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ qualityMetrics.sustainability }}%</div>
                      <div class="metric-label">可持续性</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-icon stability">
                      <i class="el-icon-lock"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ qualityMetrics.stability }}%</div>
                      <div class="metric-label">稳定性</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-icon predictability">
                      <i class="el-icon-view"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ qualityMetrics.predictability }}%</div>
                      <div class="metric-label">可预测性</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-icon risk">
                      <i class="el-icon-warning"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ qualityMetrics.riskLevel }}</div>
                      <div class="metric-label">风险等级</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="quality-details">
              <el-table :data="qualityDetails" border>
                <el-table-column prop="indicator" label="质量指标" width="150" />
                <el-table-column prop="score" label="得分" width="100">
                  <template slot-scope="scope">
                    <el-progress 
                      :percentage="scope.row.score" 
                      :color="getScoreColor(scope.row.score)"
                      :stroke-width="8"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="benchmark" label="基准值" width="100" />
                <el-table-column prop="status" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getStatusType(scope.row.status)">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="suggestion" label="改进建议" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 预测模型 -->
        <el-tab-pane label="预测模型" name="forecast">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>收入预测模型</h3>
              <p>基于历史数据和趋势分析预测未来收入</p>
            </div>
            <div class="forecast-options">
              <el-form :inline="true" size="small">
                <el-form-item label="预测模型">
                  <el-select v-model="forecastModel" placeholder="请选择预测模型">
                    <el-option label="线性回归" :value="1" />
                    <el-option label="时间序列" :value="2" />
                    <el-option label="指数平滑" :value="3" />
                    <el-option label="神经网络" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item label="预测期间">
                  <el-select v-model="forecastPeriod" placeholder="请选择预测期间">
                    <el-option label="未来3个月" :value="3" />
                    <el-option label="未来6个月" :value="6" />
                    <el-option label="未来12个月" :value="12" />
                    <el-option label="未来24个月" :value="24" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="runForecast">运行预测</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="chart-container">
              <div class="chart-placeholder">
                <i class="el-icon-data-analysis"></i>
                <p>收入预测图</p>
                <p class="chart-desc">显示历史数据和预测结果</p>
              </div>
            </div>
            <div class="forecast-results">
              <el-table :data="forecastData" border>
                <el-table-column prop="period" label="预测期间" width="120" />
                <el-table-column prop="forecast" label="预测收入" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.forecast) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="confidence" label="置信度" width="100">
                  <template slot-scope="scope">
                    <span>{{ scope.row.confidence }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="upperBound" label="上限" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.upperBound) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="lowerBound" label="下限" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.lowerBound) }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getRevenueStructureAnalysis,
  getRevenueTrendAnalysis,
  getRevenueQualityAnalysis,
  getRevenueForecastModel,
  getRevenueComparisonAnalysis,
  getRevenueContributionAnalysis,
  generateRevenueAnalysisReport,
  exportRevenueData
} from '@/api/financialSharing/revenueManagement'
import XLSX from 'xlsx'

export default {
  name: 'RevenueAnalysisIndex',
  data() {
    return {
      overview: {
        totalRevenue: 125600000,
        revenueTrend: 15.8,
        growthRate: 12.5,
        growthTrend: 2.3,
        qualityScore: 85,
        qualityTrend: 5.2,
        forecastRevenue: 145800000,
        forecastTrend: 16.1
      },
      analysisForm: {
        dimension: 1,
        dateRange: [],
        comparisonType: 1
      },
      activeTab: 'structure',
      structureData: [],
      trendSummary: {
        avgGrowthRate: 12.5,
        peakMonth: '2024-11',
        volatility: 0.15
      },
      qualityMetrics: {
        sustainability: 85,
        stability: 78,
        predictability: 82,
        riskLevel: '中等'
      },
      qualityDetails: [],
      forecastModel: 2,
      forecastPeriod: 6,
      forecastData: []
    }
  },
  mounted() {
    this.loadAnalysisData()
  },
  methods: {
    async loadAnalysisData() {
      await this.loadStructureData()
      await this.loadQualityDetails()
      await this.loadForecastData()
    },
    async loadStructureData() {
      try {
        const response = await getRevenueStructureAnalysis({
          dimension: this.analysisForm.dimension,
          dateRange: this.analysisForm.dateRange
        })
        if (response.code === 1) {
          this.structureData = response.data.structureData || []
        }
      } catch (error) {
        console.error('获取收入结构分析失败:', error)
        this.$message.error('获取收入结构分析失败: ' + (error.message || '未知错误'))
        this.structureData = []
      }
    },
    async loadQualityDetails() {
      try {
        const response = await getRevenueQualityAnalysis({})
        if (response.code === 1) {
          this.qualityDetails = response.data.qualityDetails || []
        }
      } catch (error) {
        console.error('获取收入质量分析失败:', error)
        this.$message.error('获取收入质量分析失败: ' + (error.message || '未知错误'))
        this.qualityDetails = []
      }
    },
    async loadForecastData() {
      try {
        const response = await getRevenueForecastModel({
          model: this.forecastModel,
          period: this.forecastPeriod
        })
        if (response.code === 1) {
          this.forecastData = response.data.forecastData || []
        }
      } catch (error) {
        console.error('获取收入预测模型失败:', error)
        this.$message.error('获取收入预测模型失败: ' + (error.message || '未知错误'))
        this.forecastData = []
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getScoreColor(score) {
      if (score >= 90) return '#67c23a'
      if (score >= 80) return '#e6a23c'
      if (score >= 70) return '#f56c6c'
      return '#909399'
    },
    getStatusType(status) {
      const types = { '优秀': 'success', '良好': 'primary', '一般': 'warning', '较差': 'danger' }
      return types[status] || 'info'
    },
    onDimensionChange() {
      this.loadStructureData()
    },
    onDateRangeChange() {
      this.loadAnalysisData()
    },
    onComparisonChange() {
      this.loadAnalysisData()
    },
    performAnalysis() {
      this.loadAnalysisData()
      this.$message.success('分析完成')
    },
    resetAnalysis() {
      this.analysisForm = {
        dimension: 1,
        dateRange: [],
        comparisonType: 1
      }
      this.loadAnalysisData()
    },
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    },
    runForecast() {
      this.loadForecastData()
      this.$message.success('预测模型运行完成')
    },
    async exportAnalysis() {
      try {
        this.$message.loading('正在导出分析数据...')

        // 创建工作簿
        const wb = XLSX.utils.book_new()

        // 1. 导出概览数据
        const overviewData = [
          ['指标', '数值', '趋势'],
          ['总收入', this.formatAmount(this.overview.totalRevenue), `${this.overview.revenueTrend >= 0 ? '+' : ''}${this.overview.revenueTrend}%`],
          ['增长率', `${this.overview.growthRate}%`, `${this.overview.growthTrend >= 0 ? '+' : ''}${this.overview.growthTrend}%`],
          ['质量评分', this.overview.qualityScore, `${this.overview.qualityTrend >= 0 ? '+' : ''}${this.overview.qualityTrend}%`],
          ['预测收入', this.formatAmount(this.overview.forecastRevenue), `${this.overview.forecastTrend >= 0 ? '+' : ''}${this.overview.forecastTrend}%`]
        ]
        const wsOverview = XLSX.utils.aoa_to_sheet(overviewData)
        XLSX.utils.book_append_sheet(wb, wsOverview, '分析概览')

        // 2. 导出收入结构数据
        if (this.structureData && this.structureData.length > 0) {
          const structureHeaders = [['维度', '收入金额', '占比', '同比增长']]
          const structureRows = this.structureData.map(item => [
            item.dimension || item.name,
            this.formatAmount(item.amount || item.revenue),
            `${item.percentage || item.ratio}%`,
            `${item.growth >= 0 ? '+' : ''}${item.growth}%`
          ])
          const wsStructure = XLSX.utils.aoa_to_sheet([...structureHeaders, ...structureRows])
          XLSX.utils.book_append_sheet(wb, wsStructure, '收入结构')
        }

        // 3. 导出质量分析数据
        if (this.qualityDetails && this.qualityDetails.length > 0) {
          const qualityHeaders = [['质量指标', '得分', '基准值', '状态', '改进建议']]
          const qualityRows = this.qualityDetails.map(item => [
            item.indicator,
            item.score,
            item.benchmark,
            item.status,
            item.suggestion
          ])
          const wsQuality = XLSX.utils.aoa_to_sheet([...qualityHeaders, ...qualityRows])
          XLSX.utils.book_append_sheet(wb, wsQuality, '质量分析')
        }

        // 4. 导出预测数据
        if (this.forecastData && this.forecastData.length > 0) {
          const forecastHeaders = [['预测期间', '预测收入', '置信度', '上限', '下限']]
          const forecastRows = this.forecastData.map(item => [
            item.period,
            this.formatAmount(item.forecast),
            `${item.confidence}%`,
            this.formatAmount(item.upperBound),
            this.formatAmount(item.lowerBound)
          ])
          const wsForecast = XLSX.utils.aoa_to_sheet([...forecastHeaders, ...forecastRows])
          XLSX.utils.book_append_sheet(wb, wsForecast, '收入预测')
        }

        // 5. 导出质量指标汇总
        const metricsData = [
          ['指标', '数值'],
          ['可持续性', `${this.qualityMetrics.sustainability}分`],
          ['稳定性', `${this.qualityMetrics.stability}分`],
          ['可预测性', `${this.qualityMetrics.predictability}分`],
          ['风险等级', this.qualityMetrics.riskLevel]
        ]
        const wsMetrics = XLSX.utils.aoa_to_sheet(metricsData)
        XLSX.utils.book_append_sheet(wb, wsMetrics, '质量指标')

        // 生成文件名
        const date = new Date()
        const dateStr = `${date.getFullYear()}${String(date.getMonth() + 1).padStart(2, '0')}${String(date.getDate()).padStart(2, '0')}`
        const fileName = `收入分析报告_${dateStr}.xlsx`

        // 导出文件
        XLSX.writeFile(wb, fileName)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      }
    },
    async generateForecast() {
      try {
        this.$confirm('是否生成收入预测分析报告？报告将包含详细的预测模型和分析结果。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(async () => {
          const loading = this.$loading({
            lock: true,
            text: '正在生成预测报告...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          try {
            const response = await generateRevenueAnalysisReport({
              reportType: 'forecast',
              dimension: this.analysisForm.dimension,
              dateRange: this.analysisForm.dateRange,
              forecastModel: this.forecastModel,
              forecastPeriod: this.forecastPeriod,
              includeCharts: true,
              includeDetails: true
            })

            if (response.code === 1) {
              // 如果后端返回文件URL，直接下载
              if (response.data && response.data.fileUrl) {
                window.open(response.data.fileUrl, '_blank')
                this.$message.success('预测报告生成成功')
              } else if (response.data && response.data.reportData) {
                // 如果返回报告数据，生成Excel
                this.exportForecastReport(response.data.reportData)
              } else {
                this.$message.success('预测报告已生成')
              }
            } else {
              this.$message.error(response.msg || '生成预测报告失败')
            }
          } catch (error) {
            console.error('生成预测报告失败:', error)
            this.$message.error('生成预测报告失败: ' + (error.message || '未知错误'))
          } finally {
            loading.close()
          }
        }).catch(() => {})
      } catch (error) {
        console.error('生成预测报告失败:', error)
      }
    },
    exportForecastReport(reportData) {
      try {
        const wb = XLSX.utils.book_new()

        // 报告摘要
        if (reportData.summary) {
          const summaryData = [
            ['收入预测分析报告'],
            [''],
            ['生成时间', reportData.summary.generateTime || new Date().toLocaleString()],
            ['预测模型', reportData.summary.modelName || '时间序列模型'],
            ['预测期间', reportData.summary.forecastPeriod || '未来6个月'],
            [''],
            ['预测总收入', this.formatAmount(reportData.summary.totalForecast || 0)],
            ['平均增长率', `${reportData.summary.avgGrowth || 0}%`],
            ['预测准确度', `${reportData.summary.accuracy || 0}%`]
          ]
          const wsSummary = XLSX.utils.aoa_to_sheet(summaryData)
          XLSX.utils.book_append_sheet(wb, wsSummary, '报告摘要')
        }

        // 预测明细
        if (reportData.forecastDetails && reportData.forecastDetails.length > 0) {
          const headers = [['期间', '预测收入', '置信度', '上限', '下限', '增长率']]
          const rows = reportData.forecastDetails.map(item => [
            item.period,
            this.formatAmount(item.forecast),
            `${item.confidence}%`,
            this.formatAmount(item.upperBound),
            this.formatAmount(item.lowerBound),
            `${item.growth >= 0 ? '+' : ''}${item.growth}%`
          ])
          const wsDetails = XLSX.utils.aoa_to_sheet([...headers, ...rows])
          XLSX.utils.book_append_sheet(wb, wsDetails, '预测明细')
        }

        const date = new Date()
        const dateStr = `${date.getFullYear()}${String(date.getMonth() + 1).padStart(2, '0')}${String(date.getDate()).padStart(2, '0')}`
        const fileName = `收入预测报告_${dateStr}.xlsx`

        XLSX.writeFile(wb, fileName)
        this.$message.success('预测报告导出成功')
      } catch (error) {
        console.error('导出预测报告失败:', error)
        this.$message.error('导出预测报告失败')
      }
    },
    refreshData() {
      this.loadAnalysisData()
      this.$message.success('数据已刷新')
    }
  }
}
</script>

<style lang="scss" scoped>
.revenue-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #9c27b0;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.analysis-overview {
  margin-bottom: 24px;

  .overview-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.revenue {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.growth {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.quality {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.forecast {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .card-content {
      flex: 1;

      .card-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .card-trend {
        display: flex;
        align-items: center;
        font-size: 12px;

        .trend-up {
          color: #67c23a;
        }

        .trend-down {
          color: #f56c6c;
        }

        span {
          margin-left: 4px;
        }
      }
    }
  }
}

.analysis-options {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.analysis-results {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .analysis-panel {
    .panel-header {
      margin-bottom: 24px;

      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }

      p {
        color: #606266;
        font-size: 14px;
        margin: 0;
      }
    }

    .chart-container {
      margin-bottom: 24px;

      .chart-placeholder {
        height: 300px;
        border: 2px dashed #e4e7ed;
        border-radius: 8px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }

        p {
          margin: 0;
          font-size: 16px;
          font-weight: 600;

          &.chart-desc {
            font-size: 14px;
            font-weight: normal;
            margin-top: 8px;
          }
        }
      }
    }

    .structure-table {
      .amount-text {
        color: #9c27b0;
        font-weight: 600;
      }

      .growth-positive {
        color: #67c23a;
        font-weight: 600;
      }

      .growth-negative {
        color: #f56c6c;
        font-weight: 600;
      }
    }

    .trend-summary {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;

      .summary-item {
        text-align: center;

        .summary-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 8px;
        }

        .summary-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;

          &.growth-positive {
            color: #67c23a;
          }
        }
      }
    }

    .quality-metrics {
      margin-bottom: 24px;

      .metric-card {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 20px;
        display: flex;
        align-items: center;

        .metric-icon {
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

          &.sustainability {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }

          &.stability {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.predictability {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.risk {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
        }

        .metric-content {
          .metric-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .metric-label {
            font-size: 14px;
            color: #606266;
          }
        }
      }
    }

    .forecast-options {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 24px;
    }

    .forecast-results {
      .amount-text {
        color: #9c27b0;
        font-weight: 600;
      }
    }
  }
}
</style>
