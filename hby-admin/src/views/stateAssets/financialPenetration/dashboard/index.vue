<template>
  <div class="fin-dashboard-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务监控驾驶舱</div>
        <div class="banner-sub">实时掌握集团各级企业财务健康状况，多维指标综合呈现财务风险全景</div>
      </div>
      <div class="banner-right">
        <div v-for="q in bannerQuadrants" :key="q.label" class="banner-quad">
          <div class="bq-value">{{ q.value }}</div>
          <div class="bq-label">{{ q.label }}</div>
        </div>
        <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen">大屏展示</el-button>
      </div>
    </div>

    <!-- 第一行：4个统计卡 + 企业财务评分排名 -->
    <el-row :gutter="14" style="margin-bottom:16px">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-inner">
            <div class="stat-icon" :style="{background: s.color+'18'}"><i :class="s.icon" :style="{color:s.color,fontSize:'26px'}"/></div>
            <div><div class="stat-val" :style="{color:s.color}">{{ s.value }}</div><div class="stat-lbl">{{ s.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：营业收入趋势 + 净利润率趋势 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">营业收入与净利润趋势（亿元）</div>
          <div ref="revChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">各企业财务健康评分</div>
          <div ref="scoreChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：资产负债率分布 + 利润质量分析 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">资产负债率监控（行业警戒线：70%）</div>
          <div ref="debtChart" style="height:240px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">利润质量（经营现金流/净利润）</div>
          <div ref="profitChart" style="height:240px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：ROE对比 + 现金流健康度 -->
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">ROE对比分析（%）</div>
          <div ref="roeChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">企业财务综合指标概览</div>
          <el-table :data="overviewList" size="small" border>
            <el-table-column label="企业名称" prop="name" min-width="130"/>
            <el-table-column label="负债率" prop="debtRatio" width="80" align="center">
              <template slot-scope="{row}">
                <span :style="{color:row.debtRatio>70?'#F5222D':row.debtRatio>60?'#FA8C16':'#52C41A',fontWeight:'700'}">{{ row.debtRatio }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="ROE" prop="roe" width="70" align="center">
              <template slot-scope="{row}">
                <span :style="{color:row.roe<5?'#FA8C16':'#52C41A',fontWeight:'700'}">{{ row.roe }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="流动比率" prop="currentRatio" width="90" align="center"/>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.riskLevel==='HIGH'?'danger':row.riskLevel==='MEDIUM'?'warning':'success'" size="mini">
                  {{ row.riskLevel==='HIGH'?'高风险':row.riskLevel==='MEDIUM'?'中风险':'低风险' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <FinancialPenetrationScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getFinancialDashboard, getDebtRatioMonitor, getProfitQualityMonitor } from '@/api/stateAssets/financialPenetration'
import FinancialPenetrationScreen from '../screen/index.vue'

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
  name: 'FinancialDashboard',
  components: { FinancialPenetrationScreen },
  data() {
    return {
      bannerQuadrants: [],
      statCards: [],
      overviewList: [],
      dashboardData: {},
      charts: [],
      screenVisible: false,
    }
  },
  async mounted() {
    await this.loadData()
  },
  beforeDestroy() {
    this.charts.forEach(c => c && c.dispose())
  },
  methods: {
    openScreen() {
      this.screenVisible = true
      this.$nextTick(() => {
        const el = this.$refs.screenContainer
        if (el && el.requestFullscreen) el.requestFullscreen()
        else if (el && el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      })
    },
    closeScreen() {
      this.screenVisible = false
      if (document.exitFullscreen) document.exitFullscreen().catch(() => {})
    },
    async loadData() {
      try {
        const [dashRes, debtRes, profitRes] = await Promise.all([
          getFinancialDashboard().catch(() => ({})),
          getDebtRatioMonitor().catch(() => ({})),
          getProfitQualityMonitor().catch(() => ({}))
        ])
        this.dashboardData = dashRes.data || {}
        const kpi = this.dashboardData.kpi || {}
        // Banner quadrant
        this.bannerQuadrants = [
          { label: '监控企业数', value: (kpi.totalStatements || 0) + '' },
          { label: '活跃预警', value: (this.dashboardData.activeAlerts || 0) + '' },
          { label: '均资产负债率', value: (kpi.avgDebtRatio || 0) + '%' },
          { label: '综合ROE', value: (kpi.avgRoe || 0) + '%' },
        ]
        // Stat cards
        this.statCards = [
          { label: '集团营收（万元）', value: (kpi.totalRevenue || 0).toLocaleString(), color: '#1677FF', icon: 'el-icon-money' },
          { label: '净利润（万元）', value: (kpi.totalProfit || 0).toLocaleString(), color: '#52C41A', icon: 'el-icon-trend-charts' },
          { label: '平均负债率', value: (kpi.avgDebtRatio || 0) + '%', color: '#FA8C16', icon: 'el-icon-bank-card' },
          { label: '高风险企业', value: (kpi.highRiskCount || 0) + '家', color: '#F5222D', icon: 'el-icon-warning' },
        ]
        // Overview table - 使用按企业分组的companyOverview数据
        const companyOverview = this.dashboardData.companyOverview || []
        if (companyOverview.length) {
          this.overviewList = companyOverview.map(c => ({
            name: c.companyName, debtRatio: c.debtRatio || 0, roe: c.roe || 0,
            currentRatio: c.currentRatio || 0, riskLevel: c.riskLevel || 'LOW',
            healthScore: c.healthScore || 0, profitQuality: c.profitQuality || 0
          }))
        }
        this.debtData = debtRes.data || {}
        this.profitData = profitRes.data || {}
      } catch (e) { console.error('加载驾驶舱数据失败', e) }
      this.$nextTick(() => {
        this.initRevChart()
        this.initScoreChart()
        this.initDebtChart()
        this.initProfitChart()
        this.initRoeChart()
      })
    },
    async initCharts() {
      await this.loadData()
    },
    initRevChart() {
      const c = echarts.init(this.$refs.revChart)
      this.charts.push(c)
      const trend = this.dashboardData.trend || []
      const months = trend.map(t => t.period || '').slice(-6)
      const revenues = trend.map(t => t.revenue || 0).slice(-6)
      const profits = trend.map(t => t.netProfit || 0).slice(-6)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业收入', '净利润'], top: 0 },
        xAxis: { type: 'category', data: months.length ? months : [] },
        yAxis: { type: 'value' },
        series: [
          { name: '营业收入', type: 'bar', data: revenues, itemStyle: { color: '#1677FF' }, barWidth: 28 },
          { name: '净利润', type: 'line', data: profits, itemStyle: { color: '#52C41A' }, smooth: true, lineStyle: { width: 2 } },
        ],
      })
    },
    initScoreChart() {
      const c = echarts.init(this.$refs.scoreChart)
      this.charts.push(c)
      const list = this.overviewList
      const companies = list.map(r => r.name)
      const scores = list.map(r => r.healthScore || 0)
      c.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: companies },
        yAxis: { type: 'value', min: 0, max: 100 },
        series: [{
          type: 'bar', data: scores.map((v, i) => ({
            value: v,
            itemStyle: { color: v < 60 ? '#F5222D' : v < 75 ? '#FA8C16' : '#52C41A' },
          })),
          label: { show: true, position: 'top' }, barWidth: 36,
        }],
        markLine: { data: [{ yAxis: 60, lineStyle: { color: '#F5222D', type: 'dashed' } }] },
      })
    },
    initDebtChart() {
      const c = echarts.init(this.$refs.debtChart)
      this.charts.push(c)
      const list = this.overviewList
      const companies = list.map(r => r.name)
      const data = list.map(r => r.debtRatio || 0)
      c.setOption({
        tooltip: { trigger: 'axis', formatter: '{b}: {c}%' },
        xAxis: { type: 'category', data: companies, axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
        series: [{
          type: 'bar', data: data.map(v => ({ value: v, itemStyle: { color: v > 70 ? '#F5222D' : v > 60 ? '#FA8C16' : '#52C41A' } })),
          label: { show: true, position: 'top', formatter: '{c}%', fontSize: 11 }, barWidth: 40,
          markLine: { data: [{ yAxis: 70, name: '红色警戒线', lineStyle: { color: '#F5222D', type: 'dashed' } }, { yAxis: 60, name: '橙色警戒线', lineStyle: { color: '#FA8C16', type: 'dashed' } }] },
        }],
      })
    },
    initProfitChart() {
      const c = echarts.init(this.$refs.profitChart)
      this.charts.push(c)
      const list = this.overviewList
      const companies = list.map(r => r.name)
      const profitQualities = list.map(r => r.profitQuality || 0)
      c.setOption({
        tooltip: { trigger: 'axis', formatter: params => `${params[0].axisValue}<br/>利润质量系数: ${params[0].value}` },
        xAxis: { type: 'category', data: companies },
        yAxis: { type: 'value', name: '利润质量系数' },
        series: [{
          type: 'bar', data: profitQualities.map(v => ({ value: v, itemStyle: { color: v < 0.5 ? '#F5222D' : v < 0.8 ? '#FA8C16' : '#52C41A' } })),
          label: { show: true, position: 'top' }, barWidth: 40,
          markLine: { data: [{ yAxis: 1, name: '正常值', lineStyle: { color: '#1677FF', type: 'dashed' } }] },
        }],
      })
    },
    initRoeChart() {
      const c = echarts.init(this.$refs.roeChart)
      this.charts.push(c)
      const list = this.overviewList
      const companies = list.map(r => r.name)
      const roes = list.map(r => r.roe || 0)
      const avgRoe = roes.length ? (roes.reduce((a, b) => a + b, 0) / roes.length) : 0
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['ROE', '行业均值'], top: 0 },
        xAxis: { type: 'category', data: companies },
        yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
        series: [
          { name: 'ROE', type: 'bar', data: roes.map(v => ({ value: v, itemStyle: { color: v < 5 ? '#F5222D' : v < 8 ? '#FA8C16' : '#52C41A' } })), barWidth: 32, label: { show: true, position: 'top', formatter: '{c}%' } },
          { name: '行业均值', type: 'line', data: roes.map(() => avgRoe), lineStyle: { color: '#722ED1', type: 'dashed', width: 2 }, symbol: 'none' },
        ],
      })
    },
  },
}
</script>

<style scoped lang="scss">
.fin-dashboard-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); }
  .banner-right { display: flex; gap: 24px; }
  .banner-quad { text-align: center; background: rgba(255,255,255,0.12); border-radius: 8px; padding: 10px 18px; }
  .bq-value { font-size: 22px; font-weight: 700; color: #fff; }
  .bq-label { font-size: 11px; color: rgba(255,255,255,0.7); margin-top: 2px; }
}
.stat-card { border-radius: 8px; }
.stat-inner { display: flex; align-items: center; gap: 12px; }
.stat-icon { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.stat-val { font-size: 22px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: #666; margin-top: 2px; }
.card-header { font-weight: 600; color: #333; }
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
</style>
