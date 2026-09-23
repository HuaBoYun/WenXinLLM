<template>
  <div class="finance-dashboard">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="(card, i) in statCards" :key="i">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }"><i :class="card.icon" :style="{ color: card.color }"></i></div>
            <div class="stat-info">
              <div class="stat-value" :style="{ color: card.color }">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-change" :class="card.trend > 0 ? 'up' : 'down'"><i :class="card.trend > 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i> {{ Math.abs(card.trend) }}% 较上月</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 第二行：收入利润趋势 + 资产负债率分布 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <el-card shadow="never" class="chart-card"><div slot="header" class="card-header"><span>收入利润趋势</span></div><div ref="trendChart" class="chart-box"></div></el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="chart-card"><div slot="header" class="card-header"><span>资产负债率分布</span></div><div ref="debtChart" class="chart-box"></div></el-card>
      </el-col>
    </el-row>
    <!-- 第三行：关联交易监控 + 预警信息 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>关联交易监控</span><el-tag size="mini" type="warning">{{ relatedTxList.length }}笔</el-tag></div>
          <el-table :data="relatedTxList" size="small" style="width: 100%">
            <el-table-column label="交易企业" prop="company" min-width="130" show-overflow-tooltip />
            <el-table-column label="关联方" prop="relatedParty" min-width="120" show-overflow-tooltip />
            <el-table-column label="交易金额(万)" width="110" align="right">
              <template slot-scope="s"><span class="amount-text">{{ s.row.amount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="占比" width="80" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.ratio > 10 ? '#F56C6C' : '#67C23A', fontWeight: 600 }">{{ s.row.ratio }}%</span></template>
            </el-table-column>
            <el-table-column label="风险" width="70" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskType" size="mini">{{ s.row.risk }}</el-tag></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>风险预警</span><el-badge :value="warnings.length" class="badge-item" /></div>
          <div class="warning-list">
            <div v-for="(w, i) in warnings" :key="i" class="warning-item">
              <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]" size="mini" class="warning-tag">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}</el-tag>
              <span class="warning-text">{{ w.title }}</span><span class="warning-time">{{ w.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 第四行：财务指标雷达图 + 异常检测 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card"><div slot="header" class="card-header"><span>核心财务指标对标</span></div><div ref="radarChart" class="chart-box"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="chart-card"><div slot="header" class="card-header"><span>财务异常检测分布</span></div><div ref="anomalyChart" class="chart-box"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getFinancialDashboard, getDebtRatioMonitor, getRelatedPartyList, getFinancialWarningList, getAnomalyList, getFinancialDashboardRadar } from '@/api/stateAssets/financialPenetration'
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
  name: 'FinanceDashboard',
  data() {
    return {
      statCards: [
        { label: '报表企业数', value: '--', icon: 'el-icon-office-building', color: '#722ed1', bg: 'rgba(114,46,209,0.1)', trend: 0 },
        { label: '营业收入(亿元)', value: '--', icon: 'el-icon-money', color: '#1890ff', bg: 'rgba(24,144,255,0.1)', trend: 0 },
        { label: '关联交易笔数', value: '--', icon: 'el-icon-connection', color: '#faad14', bg: 'rgba(250,173,20,0.1)', trend: 0 },
        { label: '异常指标数', value: '--', icon: 'el-icon-warning-outline', color: '#F56C6C', bg: 'rgba(245,108,108,0.1)', trend: 0 },
      ],
      warnings: [],
      relatedTxList: [],
      trendData: { periods: [], revenue: [], profit: [], profitRate: [] },
      debtRatioData: [],
      anomalyPieData: [],
      radarData: { indicators: [], groupAvg: [], benchmark: [] },
    }
  },
  mounted() { this.$nextTick(() => { this.loadAll() }) },
  beforeDestroy() { this.disposeCharts(); window.removeEventListener('resize', this.handleResize) },
  methods: {
    loadAll() {
      Promise.all([
        getFinancialDashboard(),
        getDebtRatioMonitor(),
        getRelatedPartyList({ pageNumber: 1, pageSize: 6 }),
        getFinancialWarningList({ pageNumber: 1, pageSize: 6 }),
        getAnomalyList({ pageNumber: 1, pageSize: 100 }),
        getFinancialDashboardRadar(),
      ]).then(([dashRes, debtRes, relRes, warnRes, anomalyRes, radarRes]) => {
        if (dashRes && dashRes.data) {
          const d = dashRes.data
          const kpi = d.kpi || d
          this.statCards[0].value = d.totalStatements !== undefined ? d.totalStatements : (kpi.totalStatements || '--')
          this.statCards[1].value = kpi.totalRevenue ? (parseFloat(kpi.totalRevenue) / 100000000).toFixed(1) : '--'
          this.statCards[2].value = d.totalRelatedParties !== undefined ? d.totalRelatedParties : '--'
          this.statCards[3].value = kpi.highRiskCount !== undefined ? kpi.highRiskCount : '--'
          // 环比趋势
          if (d.trendPercent) {
            this.statCards[0].trend = d.trendPercent.companyTrend || 0
            this.statCards[1].trend = d.trendPercent.revenueTrend || 0
            this.statCards[2].trend = d.trendPercent.relatedTrend || 0
            this.statCards[3].trend = d.trendPercent.anomalyTrend || 0
          }
          if (d.trend && d.trend.length) {
            this.trendData.periods = d.trend.map(t => t.period)
            this.trendData.revenue = d.trend.map(t => t.revenue ? (parseFloat(t.revenue) / 100000000).toFixed(2) : 0)
            this.trendData.profit = d.trend.map(t => t.netProfit ? (parseFloat(t.netProfit) / 100000000).toFixed(2) : 0)
            this.trendData.profitRate = d.trend.map(t => t.roe ? parseFloat(t.roe).toFixed(1) : (t.profitRate ? parseFloat(t.profitRate).toFixed(1) : 0))
          }
        }
        if (debtRes && debtRes.data && debtRes.data.trend) {
          this.debtRatioData = debtRes.data.trend.slice(0, 8)
        }
        if (relRes && relRes.data) {
          const rows = relRes.data.tlist || []
          this.relatedTxList = rows.map(r => ({
            company: r.companyName || '--',
            relatedParty: r.relatedParty || r.partyName || '--',
            amount: parseFloat(r.amount || r.transactionAmount || 0),
            ratio: parseFloat(r.ratio || 0),
            risk: r.riskLevel === 'HIGH' ? '高' : r.riskLevel === 'MEDIUM' ? '中' : '低',
            riskType: r.riskLevel === 'HIGH' ? 'danger' : r.riskLevel === 'MEDIUM' ? 'warning' : 'info',
          }))
        }
        if (warnRes && warnRes.data) {
          const rows = warnRes.data.tlist || []
          this.warnings = rows.map(r => ({
            level: r.level || 'LOW',
            title: r.alertTitle || r.alertType || r.triggerCondition || '预警信息',
            time: r.createTime || r.detectTime || '--',
          }))
        }
        if (anomalyRes && anomalyRes.data) {
          const rows = anomalyRes.data.tlist || []
          const typeMap = {}
          rows.forEach(r => { const t = r.anomalyType || '其他'; typeMap[t] = (typeMap[t] || 0) + 1 })
          const colors = ['#F56C6C', '#faad14', '#722ed1', '#1890ff', '#52c41a']
          this.anomalyPieData = Object.entries(typeMap).map(([name, value], i) => ({ value, name, itemStyle: { color: colors[i % colors.length] } }))
        }
        if (radarRes && radarRes.data) {
          this.radarData = radarRes.data
        }
        this.initCharts()
        window.addEventListener('resize', this.handleResize)
      }).catch(err => {
        console.error('驾驶舱数据加载失败', err)
        this.initCharts()
        window.addEventListener('resize', this.handleResize)
      })
    },
    disposeCharts() { ['trendChart', 'debtChart', 'radarChart', 'anomalyChart'].forEach(r => { if (this.$refs[r]) { const c = echarts.getInstanceByDom(this.$refs[r]); c && c.dispose() } }) },
    handleResize() { ['trendChart', 'debtChart', 'radarChart', 'anomalyChart'].forEach(r => { if (this.$refs[r]) { const c = echarts.getInstanceByDom(this.$refs[r]); c && c.resize() } }) },
    initCharts() { this.initTrendChart(); this.initDebtChart(); this.initRadarChart(); this.initAnomalyChart() },
    initTrendChart() {
      const c = echarts.init(this.$refs.trendChart)
      if (!this.trendData.periods.length) {
        c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#909399', fontSize: 14 } } })
        return
      }
      c.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['营业收入', '净利润', '利润率'], top: 0 }, grid: { top: 36, left: 60, right: 50, bottom: 30 },
        xAxis: { type: 'category', data: this.trendData.periods },
        yAxis: [{ type: 'value', name: '金额(亿元)' }, { type: 'value', name: '利润率(%)', position: 'right' }],
        series: [
          { name: '营业收入', type: 'bar', data: this.trendData.revenue, itemStyle: { color: '#722ed1', borderRadius: [4, 4, 0, 0] } },
          { name: '净利润', type: 'bar', data: this.trendData.profit, itemStyle: { color: '#1890ff', borderRadius: [4, 4, 0, 0] } },
          { name: '利润率', type: 'line', yAxisIndex: 1, data: this.trendData.profitRate, smooth: true, itemStyle: { color: '#faad14' }, areaStyle: { color: 'rgba(250,173,20,0.1)' } },
        ] })
    },
    initDebtChart() {
      const c = echarts.init(this.$refs.debtChart)
      if (!this.debtRatioData.length) {
        c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#909399', fontSize: 14 } } })
        return
      }
      const companies = this.debtRatioData.map(d => d.companyName || d.company || '')
      const values = this.debtRatioData.map(d => ({
        value: parseFloat(d.debtRatio || 0),
        itemStyle: { color: parseFloat(d.debtRatio || 0) >= 70 ? '#F56C6C' : parseFloat(d.debtRatio || 0) >= 60 ? '#faad14' : '#52c41a' }
      }))
      c.setOption({ tooltip: { trigger: 'axis' }, grid: { top: 10, left: 80, right: 30, bottom: 20 },
        xAxis: { type: 'value', name: '%', max: 100 },
        yAxis: { type: 'category', data: companies, axisLabel: { fontSize: 11 } },
        series: [{ type: 'bar', data: values, label: { show: true, position: 'right', formatter: '{c}%', fontSize: 11 }, barWidth: 18, itemStyle: { borderRadius: [0, 4, 4, 0] },
          markLine: { data: [{ xAxis: 70, label: { formatter: '预警70%' }, lineStyle: { color: '#F56C6C', type: 'dashed' } }], symbol: 'none' } }] })
    },
    initRadarChart() {
      const c = echarts.init(this.$refs.radarChart)
      const rd = this.radarData
      if (!rd.indicators || !rd.indicators.length || !rd.groupAvg || !rd.groupAvg.length) {
        c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#909399', fontSize: 14 } } })
        return
      }
      const indicators = rd.indicators.map(name => ({ name, max: 100 }))
      c.setOption({ tooltip: {}, legend: { data: ['集团均值', '行业标杆'], bottom: 0 },
        radar: { indicator: indicators, center: ['50%', '45%'], radius: '60%' },
        series: [{ type: 'radar', data: [
          { value: rd.groupAvg, name: '集团均值', areaStyle: { color: 'rgba(114,46,209,0.2)' }, lineStyle: { color: '#722ed1' }, itemStyle: { color: '#722ed1' } },
          { value: rd.benchmark, name: '行业标杆', areaStyle: { color: 'rgba(24,144,255,0.15)' }, lineStyle: { color: '#1890ff', type: 'dashed' }, itemStyle: { color: '#1890ff' } },
        ] }] })
    },
    initAnomalyChart() {
      const c = echarts.init(this.$refs.anomalyChart)
      if (!this.anomalyPieData.length) {
        c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#909399', fontSize: 14 } } })
        return
      }
      c.setOption({ tooltip: { trigger: 'item', formatter: '{b}: {c}项 ({d}%)' }, legend: { bottom: 0 },
        series: [{ type: 'pie', radius: ['30%', '60%'], center: ['50%', '42%'],
          data: this.anomalyPieData, label: { formatter: '{b}\n{d}%', fontSize: 11 } }] })
    },
  },
}
</script>

<style lang="scss" scoped>
.finance-dashboard { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 14px; i { font-size: 26px; } }
.stat-value { font-size: 26px; font-weight: 700; line-height: 1.3; }
.stat-label { font-size: 13px; color: #909399; margin-top: 2px; }
.stat-change { font-size: 12px; margin-top: 4px; &.up { color: #67C23A; } &.down { color: #F56C6C; } }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 600; }
.chart-card .chart-box { height: 280px; }
.amount-text { font-weight: 600; color: #722ed1; }
.warning-list { max-height: 280px; overflow-y: auto; }
.warning-item { display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #f0f0f0; &:last-child { border-bottom: none; } }
.warning-tag { flex-shrink: 0; }
.warning-text { flex: 1; margin: 0 10px; font-size: 13px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.warning-time { font-size: 12px; color: #c0c4cc; white-space: nowrap; }
</style>