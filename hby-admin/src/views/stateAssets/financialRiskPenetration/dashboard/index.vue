<template>
  <div class="fin-risk-dashboard" :style="themeVars">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="4" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.color + '20' }">
              <i :class="card.icon" :style="{ color: card.color, fontSize: '26px' }"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表区域 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover"><div slot="header"><span>融资规模趋势</span></div><div ref="trendChart" style="height: 300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><div slot="header"><span>风险等级分布</span></div><div ref="riskChart" style="height: 300px"></div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover"><div slot="header"><span>担保链关联分析</span></div><div ref="guaranteeChart" style="height: 300px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>风险预警</span></div>
          <div v-for="(w, i) in warnings" :key="i" class="warning-item">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[w.level]" size="mini">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}</el-tag>
            <span class="warning-title">{{ w.title }}</span>
            <span class="warning-time">{{ w.time }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="hover"><div slot="header"><span>到期融资预警</span></div><div ref="maturityChart" style="height: 280px"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getFinancialRiskDashboard, getFinancialRiskAlertList } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'FinancialRiskDashboard',
  mixins: [investThemeMixin],
  data() {
    return {
      statCards: [
        { label: '融资总额(亿元)', value: '--', icon: 'el-icon-coin', color: this.ipSecondary },
        { label: '担保总额(亿元)', value: '--', icon: 'el-icon-lock', color: '#FA8C16' },
        { label: '逾期委托贷款(亿元)', value: '--', icon: 'el-icon-warning-outline', color: '#CF1322' },
        { label: '衍生品敞口(亿元)', value: '--', icon: 'el-icon-s-data', color: '#722ED1' },
        { label: '高风险企业', value: '--', icon: 'el-icon-warning', color: '#CF1322' },
        { label: '活跃预警', value: '--', icon: 'el-icon-bell', color: '#FF7A45' },
      ],
      warnings: [],
      charts: [],
      dashboardData: null,
    }
  },
  created() {
    this.fetchDashboardData().then(() => this.fetchAlertList())
  },
  mounted() {
    this.$nextTick(() => this.initCharts())
  },
  beforeDestroy() {
    this.charts.forEach(c => c && c.dispose())
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    async fetchDashboardData() {
      try {
        const res = await getFinancialRiskDashboard()
        if (res.result === 200 && res.data) {
          this.dashboardData = res.data
          this.updateStatCards(res.data.kpi)
          // 用 dashboard 返回的预警数据填充风险预警列表
          if (res.data.activeWarnings && res.data.activeWarnings.length > 0) {
            this.warnings = res.data.activeWarnings.map(item => ({
              level: (item.level || 'medium').toUpperCase(),
              title: item.value || item.name || '',
              time: item.time || '',
            }))
          }
          this.$nextTick(() => {
            this.updateTrendChart(res.data)
            this.updateRiskChart(res.data.riskOverview)
            this.updateGuaranteeChart(res.data)
            this.updateMaturityChart(res.data.maturityDistribution)
          })
        }
      } catch (e) {
        console.error('获取仪表盘数据失败', e)
      }
    },
    async fetchAlertList() {
      try {
        // 如果 dashboard 已经填充了预警数据，则跳过
        if (this.warnings.length > 0) return
        const res = await getFinancialRiskAlertList({ pageNum: 1, pageSize: 10 })
        if (res.result === 200 && res.data) {
          const list = Array.isArray(res.data) ? res.data : (res.data.tlist || res.data.list || res.data.records || [])
          if (list.length > 0) {
            this.warnings = list.map(item => ({
              level: item.level || item.riskLevel || 'MEDIUM',
              title: item.alertContent || item.title || item.alertTitle || item.content || '',
              time: item.time || item.alertTime || item.createTime || '',
            }))
          }
        }
      } catch (e) {
        console.error('获取预警列表失败', e)
      }
    },
    updateStatCards(kpi) {
      if (!kpi) return
      this.statCards = [
        { label: '融资总额(亿元)', value: kpi.totalFinancing ?? '--', icon: 'el-icon-coin', color: this.ipSecondary },
        { label: '担保总额(亿元)', value: kpi.totalGuarantee ?? '--', icon: 'el-icon-lock', color: '#FA8C16' },
        { label: '逾期委托贷款(亿元)', value: kpi.overdueEntrustedLoan ?? '--', icon: 'el-icon-warning-outline', color: '#CF1322' },
        { label: '衍生品敞口(亿元)', value: kpi.derivativesExposure ?? '--', icon: 'el-icon-s-data', color: '#722ED1' },
        { label: '高风险企业', value: kpi.highRiskCount ?? '--', icon: 'el-icon-warning', color: '#CF1322' },
        { label: '活跃预警', value: kpi.activeAlertCount ?? '--', icon: 'el-icon-bell', color: '#FF7A45' },
      ]
    },
    initCharts() {
      this.initTrendChart()
      this.initRiskChart()
      this.initGuaranteeChart()
      this.initMaturityChart()
      window.addEventListener('resize', this.handleResize)
    },
    handleResize() {
      this.charts.forEach(c => c && c.resize())
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['融资规模', '担保余额', '风险敞口'] },
        grid: { left: 50, right: 20, top: 40, bottom: 30 },
        xAxis: { type: 'category', data: [] },
        yAxis: { type: 'value', name: '亿元' },
        series: [
          { name: '融资规模', type: 'bar', data: [], itemStyle: { color: this.ipSecondary } },
          { name: '担保余额', type: 'bar', data: [], itemStyle: { color: '#FA8C16' } },
          { name: '风险敞口', type: 'line', data: [], itemStyle: { color: '#CF1322' }, lineStyle: { width: 2 } },
        ],
      })
    },
    initRiskChart() {
      const chart = echarts.init(this.$refs.riskChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie', radius: ['40%', '70%'], center: ['50%', '55%'],
          data: [],
          label: { formatter: '{b}\n{c}项 ({d}%)' },
        }],
      })
    },
    initGuaranteeChart() {
      const chart = echarts.init(this.$refs.guaranteeChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 100, right: 20, top: 10, bottom: 30 },
        yAxis: { type: 'category', data: [] },
        xAxis: { type: 'value', name: '亿元' },
        series: [
          { type: 'bar', data: [], itemStyle: { color: this.ipSecondary }, barWidth: 16, label: { show: true, position: 'right' } },
        ],
      })
    },
    initMaturityChart() {
      const chart = echarts.init(this.$refs.maturityChart)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['到期金额', '累计到期'] },
        grid: { left: 50, right: 50, top: 40, bottom: 30 },
        xAxis: { type: 'category', data: [] },
        yAxis: [{ type: 'value', name: '亿元' }, { type: 'value', name: '累计亿元' }],
        series: [
          { name: '到期金额', type: 'bar', data: [], itemStyle: { color: this.ipBright } },
          { name: '累计到期', type: 'line', yAxisIndex: 1, data: [], itemStyle: { color: '#FA8C16' }, lineStyle: { width: 2, type: 'dashed' } },
        ],
      })
    },
    updateTrendChart(data) {
      const chart = this.charts[0]
      if (!chart || !data.trendData) return
      const trend = data.trendData
      chart.setOption({
        xAxis: { data: trend.months || trend.xAxis || [] },
        series: [
          { name: '融资规模', data: trend.financingScale || trend.financing || [] },
          { name: '担保余额', data: trend.guaranteeBalance || trend.guarantee || [] },
          { name: '风险敞口', data: trend.riskExposure || trend.exposure || [] },
        ],
      })
    },
    updateRiskChart(riskOverview) {
      const chart = this.charts[1]
      if (!chart || !riskOverview) return
      const colorMap = { '高风险': '#CF1322', '中风险': '#FA8C16', '低风险': '#52C41A', '已化解': '#D9D9D9' }
      const pieData = Array.isArray(riskOverview)
        ? riskOverview.map(item => ({
          value: item.value || item.count || 0,
          name: item.name || item.label || '',
          itemStyle: { color: colorMap[item.name || item.label] || '#999' },
        }))
        : []
      chart.setOption({ series: [{ data: pieData }] })
    },
    updateGuaranteeChart(data) {
      const chart = this.charts[2]
      if (!chart || !data.guaranteeChain) return
      const chain = data.guaranteeChain
      chart.setOption({
        yAxis: { data: chain.companies || chain.names || [] },
        series: [{ data: chain.amounts || chain.values || [] }],
      })
    },
    updateMaturityChart(maturity) {
      const chart = this.charts[3]
      if (!chart || !maturity) return
      chart.setOption({
        xAxis: { data: maturity.periods || maturity.xAxis || [] },
        series: [
          { name: '到期金额', data: maturity.amounts || maturity.maturityAmounts || [] },
          { name: '累计到期', data: maturity.cumulative || maturity.cumulativeAmounts || [] },
        ],
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fin-risk-dashboard { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 52px; height: 52px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; }
.stat-info { .stat-value { font-size: 26px; font-weight: bold; color: #303133; line-height: 1; } .stat-label { font-size: 13px; color: #909399; margin-top: 6px; } }
.warning-item { display: flex; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0; &:last-child { border-bottom: none; } }
.warning-title { flex: 1; margin-left: 10px; font-size: 13px; color: #303133; }
.warning-time { font-size: 12px; color: #c0c4cc; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; }
</style>

