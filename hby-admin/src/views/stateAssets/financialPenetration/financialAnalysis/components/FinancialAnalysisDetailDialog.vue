<template>
  <el-dialog
    title="财务分析详情"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <div class="analysis-detail-content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基础信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="企业名称">{{ analysisData.enterpriseName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="报告期间">{{ analysisData.period || '-' }}</el-descriptions-item>
            <el-descriptions-item label="营业收入">{{ formatNumber(analysisData.revenue) }}万元</el-descriptions-item>
            <el-descriptions-item label="净利润">{{ formatNumber(analysisData.netProfit) }}万元</el-descriptions-item>
            <el-descriptions-item label="总资产">{{ formatNumber(analysisData.totalAssets) }}万元</el-descriptions-item>
            <el-descriptions-item label="净资产">{{ formatNumber(analysisData.netAssets) }}万元</el-descriptions-item>
            <el-descriptions-item label="总负债">{{ formatNumber(analysisData.totalLiabilities) }}万元</el-descriptions-item>
            <el-descriptions-item label="ROE">{{ analysisData.roe || 0 }}%</el-descriptions-item>
            <el-descriptions-item label="分析类型">
              <el-tag type="primary" size="mini">{{ analysisData.analysisType || '-' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="审计状态">
              <el-tag :type="analysisData.auditStatus === '已审计' ? 'success' : 'warning'" size="mini">
                {{ analysisData.auditStatus || '-' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        
        <el-tab-pane label="财务指标" name="indicators">
          <div class="indicators-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never" class="indicator-card">
                  <div slot="header">
                    <span>盈利能力指标</span>
                  </div>
                  <el-table :data="profitabilityIndicators" border>
                    <el-table-column label="指标名称" prop="name" />
                    <el-table-column label="数值" prop="value" align="center" />
                    <el-table-column label="评级" prop="rating" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="getRatingTag(scope.row.rating)" size="mini">
                          {{ scope.row.rating }}
                        </el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never" class="indicator-card">
                  <div slot="header">
                    <span>偿债能力指标</span>
                  </div>
                  <el-table :data="solvencyIndicators" border>
                    <el-table-column label="指标名称" prop="name" />
                    <el-table-column label="数值" prop="value" align="center" />
                    <el-table-column label="评级" prop="rating" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="getRatingTag(scope.row.rating)" size="mini">
                          {{ scope.row.rating }}
                        </el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
            </el-row>
            
            <el-row :gutter="20" class="mt-20">
              <el-col :span="12">
                <el-card shadow="never" class="indicator-card">
                  <div slot="header">
                    <span>营运能力指标</span>
                  </div>
                  <el-table :data="operatingIndicators" border>
                    <el-table-column label="指标名称" prop="name" />
                    <el-table-column label="数值" prop="value" align="center" />
                    <el-table-column label="评级" prop="rating" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="getRatingTag(scope.row.rating)" size="mini">
                          {{ scope.row.rating }}
                        </el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never" class="indicator-card">
                  <div slot="header">
                    <span>发展能力指标</span>
                  </div>
                  <el-table :data="growthIndicators" border>
                    <el-table-column label="指标名称" prop="name" />
                    <el-table-column label="数值" prop="value" align="center" />
                    <el-table-column label="评级" prop="rating" align="center">
                      <template slot-scope="scope">
                        <el-tag :type="getRatingTag(scope.row.rating)" size="mini">
                          {{ scope.row.rating }}
                        </el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="趋势分析" name="trend">
          <div class="trend-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>营收利润趋势</span>
                  </div>
                  <div ref="revenueProfitChart" class="chart-container"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>财务指标趋势</span>
                  </div>
                  <div ref="indicatorTrendChart" class="chart-container"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="分析报告" name="report">
          <div class="report-panel">
            <el-card shadow="never">
              <div slot="header">
                <span>AI智能分析报告</span>
                <el-button
                  style="float: right; padding: 3px 0"
                  type="text"
                  @click="generateReport"
                >
                  重新生成
                </el-button>
              </div>
              <div class="report-content">
                <h4>财务状况综合评价</h4>
                <p>{{ analysisReport.overallAssessment }}</p>
                
                <h4>主要优势</h4>
                <ul>
                  <li v-for="(advantage, index) in analysisReport.advantages" :key="index">
                    {{ advantage }}
                  </li>
                </ul>
                
                <h4>存在问题</h4>
                <ul>
                  <li v-for="(issue, index) in analysisReport.issues" :key="index">
                    {{ issue }}
                  </li>
                </ul>
                
                <h4>改进建议</h4>
                <ul>
                  <li v-for="(suggestion, index) in analysisReport.suggestions" :key="index">
                    {{ suggestion }}
                  </li>
                </ul>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { generateFinancialAnalysisReport, exportFinancialAnalysisData } from '@/api/stateAssets/financialAnalysis'

export default {
  name: 'FinancialAnalysisDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      profitabilityIndicators: [],
      solvencyIndicators: [],
      operatingIndicators: [],
      growthIndicators: [],
      analysisReport: {
        overallAssessment: '',
        advantages: [],
        issues: [],
        suggestions: []
      },
      revenueProfitChart: null,
      indicatorTrendChart: null
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    initData() {
      const viewType = this.analysisData.viewType
      if (viewType === 'trend' || viewType === 'predict') {
        this.activeTab = 'trend'
      } else if (viewType === 'risk') {
        this.activeTab = 'report'
      } else {
        this.activeTab = 'basic'
      }
      this.loadIndicators()
      this.loadAnalysisReport()
    },

    loadIndicators() {
      const data = this.analysisData
      const revenue = data.revenue || 0
      const netProfit = data.netProfit || 0
      const totalAssets = data.totalAssets || 0
      const netAssets = data.netAssets || 0
      const totalLiabilities = data.totalLiabilities || 0

      const roe = netAssets > 0 ? (netProfit / netAssets * 100).toFixed(2) : 0
      const roa = totalAssets > 0 ? (netProfit / totalAssets * 100).toFixed(2) : 0
      const netProfitMargin = revenue > 0 ? (netProfit / revenue * 100).toFixed(2) : 0
      const debtRatio = totalAssets > 0 ? (totalLiabilities / totalAssets * 100).toFixed(2) : 0
      const currentRatio = totalLiabilities > 0 ? (totalAssets / totalLiabilities).toFixed(2) : 0

      const getRating = (val, thresholds) => {
        if (val >= thresholds[0]) return '优秀'
        if (val >= thresholds[1]) return '良好'
        if (val >= thresholds[2]) return '一般'
        return '较差'
      }

      this.profitabilityIndicators = [
        { name: '净资产收益率(ROE)', value: roe + '%', rating: getRating(parseFloat(roe), [15, 10, 5]) },
        { name: '总资产收益率(ROA)', value: roa + '%', rating: getRating(parseFloat(roa), [10, 6, 3]) },
        { name: '销售净利率', value: netProfitMargin + '%', rating: getRating(parseFloat(netProfitMargin), [15, 8, 3]) },
        { name: '营业收入', value: this.formatAmount(revenue) + '万元', rating: getRating(revenue, [100000, 50000, 10000]) }
      ]

      this.solvencyIndicators = [
        { name: '资产负债率', value: debtRatio + '%', rating: parseFloat(debtRatio) <= 50 ? '良好' : (parseFloat(debtRatio) <= 70 ? '一般' : '较差') },
        { name: '流动比率', value: currentRatio, rating: parseFloat(currentRatio) >= 2 ? '优秀' : (parseFloat(currentRatio) >= 1 ? '良好' : '较差') },
        { name: '净资产', value: this.formatAmount(netAssets) + '万元', rating: netAssets > 0 ? '良好' : '较差' },
        { name: '总负债', value: this.formatAmount(totalLiabilities) + '万元', rating: parseFloat(debtRatio) <= 60 ? '良好' : '一般' }
      ]

      this.operatingIndicators = [
        { name: '总资产', value: this.formatAmount(totalAssets) + '万元', rating: '良好' },
        { name: '营业收入', value: this.formatAmount(revenue) + '万元', rating: revenue > 0 ? '良好' : '一般' },
        { name: '净利润', value: this.formatAmount(netProfit) + '万元', rating: netProfit > 0 ? '良好' : '较差' },
        { name: '经营效率', value: revenue > 0 && totalAssets > 0 ? (revenue / totalAssets).toFixed(2) : '0', rating: '一般' }
      ]

      this.growthIndicators = [
        { name: '分析类型', value: data.analysisType || '-', rating: '良好' },
        { name: '审计状态', value: data.auditStatus || '-', rating: data.auditStatus === '已审计' ? '优秀' : '一般' },
        { name: '所属行业', value: data.industry || '-', rating: '良好' },
        { name: '财务状况', value: data.financialStatus || '-', rating: data.financialStatus === 'EXCELLENT' ? '优秀' : (data.financialStatus === 'GOOD' ? '良好' : '一般') }
      ]
    },

    loadAnalysisReport() {
      const data = this.analysisData
      // 如果有风险评估数据
      if (data.riskData) {
        this.analysisReport = {
          overallAssessment: '风险评分：' + (data.riskData.riskScore || 0) + '分，风险等级：' + (data.riskData.riskLevel || '未评估') + '。' + (data.riskData.assessment || ''),
          advantages: data.riskData.strengths || ['数据分析中...'],
          issues: data.riskData.riskFactors || ['数据分析中...'],
          suggestions: data.riskData.suggestions || ['数据分析中...']
        }
        return
      }

      const revenue = data.revenue || 0
      const netProfit = data.netProfit || 0
      const totalAssets = data.totalAssets || 0
      const netAssets = data.netAssets || 0
      const totalLiabilities = data.totalLiabilities || 0
      const roe = netAssets > 0 ? (netProfit / netAssets * 100).toFixed(2) : 0
      const debtRatio = totalAssets > 0 ? (totalLiabilities / totalAssets * 100).toFixed(2) : 0

      const advantages = []
      const issues = []
      const suggestions = []

      if (parseFloat(roe) >= 10) advantages.push('净资产收益率达到' + roe + '%，盈利能力较强')
      else issues.push('净资产收益率仅为' + roe + '%，盈利能力有待提升')

      if (parseFloat(debtRatio) <= 60) advantages.push('资产负债率为' + debtRatio + '%，财务风险可控')
      else issues.push('资产负债率达到' + debtRatio + '%，财务杠杆偏高')

      if (netProfit > 0) advantages.push('净利润为正值(' + this.formatAmount(netProfit) + '万元)，企业处于盈利状态')
      else issues.push('净利润为负值，企业处于亏损状态')

      if (revenue > 0) advantages.push('营业收入' + this.formatAmount(revenue) + '万元，具备一定经营规模')

      if (parseFloat(debtRatio) > 60) suggestions.push('建议优化资本结构，降低负债水平')
      if (parseFloat(roe) < 10) suggestions.push('建议提升资产运营效率，提高净资产收益率')
      suggestions.push('建议加强现金流管理，确保流动性安全')
      suggestions.push('建议持续关注行业动态，及时调整经营策略')

      this.analysisReport = {
        overallAssessment: (data.enterpriseName || '该企业') + (data.period || '') + '财务分析：营业收入' + this.formatAmount(revenue) + '万元，净利润' + this.formatAmount(netProfit) + '万元，总资产' + this.formatAmount(totalAssets) + '万元，资产负债率' + debtRatio + '%。',
        advantages: advantages.length > 0 ? advantages : ['暂无突出优势数据'],
        issues: issues.length > 0 ? issues : ['暂未发现明显问题'],
        suggestions: suggestions
      }
    },

    initCharts() {
      if (this.$refs.revenueProfitChart) {
        this.revenueProfitChart = echarts.init(this.$refs.revenueProfitChart)
      }
      if (this.$refs.indicatorTrendChart) {
        this.indicatorTrendChart = echarts.init(this.$refs.indicatorTrendChart)
      }

      this.updateRevenueProfitChart()
      this.updateIndicatorTrendChart()

      window.addEventListener('resize', this.handleResize)
    },

    updateRevenueProfitChart() {
      const data = this.analysisData
      var periods, revenues, profits

      if (data.trendData) {
        periods = data.trendData.periods || []
        revenues = data.trendData.revenue || []
        profits = data.trendData.profit || []
      } else if (data.forecastData) {
        periods = [].concat(data.forecastData.historicalPeriods || []).concat(data.forecastData.forecastPeriods || [])
        revenues = [].concat(data.forecastData.historicalRevenue || []).concat(data.forecastData.forecastRevenue || [])
        profits = [].concat(data.forecastData.historicalProfit || []).concat(data.forecastData.forecastProfit || [])
      } else {
        periods = [data.period || '当期']
        revenues = [data.revenue || 0]
        profits = [data.netProfit || 0]
      }

      if (!this.revenueProfitChart) return

      var series = [
        { name: '营业收入', type: 'line', data: revenues, smooth: true, itemStyle: { color: '#409EFF' } },
        { name: '净利润', type: 'line', data: profits, smooth: true, itemStyle: { color: '#67C23A' } }
      ]

      // 预测数据添加视觉区分
      if (data.forecastData && data.forecastData.historicalPeriods) {
        var histLen = data.forecastData.historicalPeriods.length
        series[0].markLine = { data: [{ xAxis: histLen - 1 }], label: { formatter: '预测起点' } }
      }

      var option = {
        title: { text: data.forecastData ? '营收利润预测' : '营收利润趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业收入', '净利润'], bottom: 10 },
        xAxis: { type: 'category', data: periods },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: series
      }
      this.revenueProfitChart.setOption(option, true)
    },

    updateIndicatorTrendChart() {
      const data = this.analysisData
      var periods, roeData, roaData, debtRatioData

      if (data.trendData) {
        periods = data.trendData.periods || []
        roeData = data.trendData.roe || []
        roaData = data.trendData.roa || []
        debtRatioData = data.trendData.debtRatio || []
      } else if (data.forecastData) {
        periods = [].concat(data.forecastData.historicalPeriods || []).concat(data.forecastData.forecastPeriods || [])
        roeData = []
        roaData = []
        debtRatioData = []
      } else {
        var roe = data.netAssets > 0 ? (data.netProfit / data.netAssets * 100).toFixed(2) : 0
        var roa = data.totalAssets > 0 ? (data.netProfit / data.totalAssets * 100).toFixed(2) : 0
        var dr = data.totalAssets > 0 ? (data.totalLiabilities / data.totalAssets * 100).toFixed(2) : 0
        periods = [data.period || '当期']
        roeData = [parseFloat(roe)]
        roaData = [parseFloat(roa)]
        debtRatioData = [parseFloat(dr)]
      }

      if (!this.indicatorTrendChart) return

      var option = {
        title: { text: '财务指标趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['ROE', 'ROA', '资产负债率'], bottom: 10 },
        xAxis: { type: 'category', data: periods },
        yAxis: { type: 'value', name: '百分比(%)' },
        series: [
          { name: 'ROE', type: 'line', data: roeData, smooth: true, itemStyle: { color: '#409EFF' } },
          { name: 'ROA', type: 'line', data: roaData, smooth: true, itemStyle: { color: '#67C23A' } },
          { name: '资产负债率', type: 'line', data: debtRatioData, smooth: true, itemStyle: { color: '#E6A23C' } }
        ]
      }
      this.indicatorTrendChart.setOption(option, true)
    },

    async generateReport() {
      try {
        this.$message.info('正在重新生成分析报告...')
        const response = await generateFinancialAnalysisReport({ id: this.analysisData.id, companyId: this.analysisData.companyId })
        if (response && response.data) {
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '财务分析报告_' + (this.analysisData.enterpriseName || '') + '.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        }
        this.$message.success('分析报告生成完成')
      } catch (error) {
        this.$message.error('报告生成失败：' + (error.message || '请稍后重试'))
      }
    },

    async handleExport() {
      try {
        const response = await exportFinancialAnalysisData({ ids: [this.analysisData.id] })
        if (response && response.data) {
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '财务分析_' + (this.analysisData.enterpriseName || '') + '.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        }
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '请稍后重试'))
      }
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.destroyCharts()
    },

    handleResize() {
      if (this.revenueProfitChart) this.revenueProfitChart.resize()
      if (this.indicatorTrendChart) this.indicatorTrendChart.resize()
    },

    destroyCharts() {
      if (this.revenueProfitChart) {
        this.revenueProfitChart.dispose()
        this.revenueProfitChart = null
      }
      if (this.indicatorTrendChart) {
        this.indicatorTrendChart.dispose()
        this.indicatorTrendChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    formatNumber(num) {
      if (!num) return '0'
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },

    formatAmount(num) {
      if (!num && num !== 0) return '0'
      if (num >= 10000) return (num / 10000).toFixed(1) + '亿'
      return num.toLocaleString()
    },

    getFinancialStatusTag(status) {
      const tagMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return tagMap[status] || 'info'
    },

    getFinancialStatusText(status) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return textMap[status] || status
    },

    getRatingTag(rating) {
      const tagMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return tagMap[rating] || 'info'
    }
  }
}
</script>

<style scoped>
.analysis-detail-content {
  min-height: 600px;
}

.indicators-panel .indicator-card {
  height: 280px;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.report-panel .report-content {
  padding: 20px;
  line-height: 1.8;
}

.report-content h4 {
  color: #303133;
  margin: 20px 0 10px 0;
  font-size: 16px;
}

.report-content ul {
  margin: 10px 0;
  padding-left: 20px;
}

.report-content li {
  margin: 8px 0;
  color: #606266;
}

.mt-20 {
  margin-top: 20px;
}
</style>
