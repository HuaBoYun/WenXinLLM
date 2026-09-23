<template>
  <div class="sal-dashboard">
    <!-- Banner + 顶部KPI -->
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">薪酬监控驾驶舱</div>
      <div class="banner-sub">集团薪酬分配全景 · 工资总额执行 · 效益联动 · 高管合规 · 人工成本四维态势</div>
      <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen" style="margin-bottom:8px;">大屏展示</el-button>
      <el-row :gutter="12" class="banner-kpi">
        <el-col :span="6" v-for="k in topKpis" :key="k.key">
          <div class="bkpi-card">
            <div class="bkpi-val" :style="{ color: k.color }">{{ k.value }}</div>
            <div class="bkpi-label">{{ k.label }}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表第一行 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">工资总额趋势（亿元）</span></div>
          <div ref="trendChart" style="height:240px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">效益联动率企业对比</span><span class="chart-tip">橙线=0.8警戒线，绿线=1.0合理基准</span></div>
          <div ref="linkChart" style="height:240px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表第二行 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">各企业预算执行率横向对比</span><span class="chart-tip">红线=100%上限</span></div>
          <div ref="execChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">高管薪酬合规雷达图</span></div>
          <div ref="radarChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 企业综合指标概览表 -->
    <el-card shadow="never" class="overview-card">
      <div slot="header"><span class="card-title">企业综合指标概览</span></div>
      <el-table :data="overviewList" stripe border size="small" :row-class-name="rowClass">
        <el-table-column prop="name" label="企业名称" width="140"></el-table-column>
        <el-table-column prop="totalSalary" label="工资总额（亿）" align="center">
          <template slot-scope="{ row }"><span>{{ row.totalSalary }}</span></template>
        </el-table-column>
        <el-table-column prop="budgetExecRate" label="预算执行率" align="center">
          <template slot-scope="{ row }">
            <span :style="{ color: row.budgetExecRate > 100 ? '#F5222D' : row.budgetExecRate > 95 ? '#FA8C16' : '#52C41A', fontWeight: row.budgetExecRate > 100 ? 700 : 400 }">
              {{ row.budgetExecRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="linkRatio" label="效益联动系数" align="center">
          <template slot-scope="{ row }">
            <span :style="linkStyle(row.linkRatio)">{{ row.linkRatio }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="laborCostRate" label="人工成本率" align="center">
          <template slot-scope="{ row }">
            <span :style="{ color: row.laborCostRate > 20 ? '#F5222D' : row.laborCostRate > 15 ? '#FA8C16' : '#52C41A' }">
              {{ row.laborCostRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="高管合规" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.execCompliance ? 'success' : 'danger'" size="mini">{{ row.execCompliance ? '合规' : '不合规' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.riskLevel === 'HIGH' ? 'danger' : row.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini">{{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[row.riskLevel] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <SalaryScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import { getSalaryDashboard, getSalaryTotalTrend, getPerformanceLinkList, getExecutivePayList, getLaborCostList, getSalaryWarningList } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'
import SalaryScreen from '../screen/index.vue'

export default {
  name: 'SalaryPenetrationDashboard',
  components: { SalaryScreen },
  data() {
    return {
      dashboardData: {},
      overviewList: [],
      trendData: [],
      linkData: [],
      executiveData: [],
      charts: {},
      screenVisible: false,
    }
  },
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
    topKpis() {
      const kpi = this.dashboardData.kpi || {}
      return [
        { key: 'total', label: '集团工资总额', value: kpi.totalSalary ? (Number(kpi.totalSalary) / 10000).toFixed(1) + '亿' : '—', color: '#fff' },
        { key: 'link', label: '平均效益联动率', value: kpi.avgLinkRatio || '—', color: '#95DE64' },
        { key: 'warn', label: '活跃预警', value: (kpi.activeWarnings || 0) + '条', color: '#FF7875' },
        { key: 'compliance', label: '高管薪酬合规率', value: kpi.execComplianceRate ? kpi.execComplianceRate + '%' : '—', color: '#FFD666' },
      ]
    },
  },
  mounted() {
    this.loadDashboard()
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => c && c.dispose())
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
    async loadDashboard() {
      try {
        const res = await getSalaryDashboard()
        if (res.data) {
          this.dashboardData = res.data
          this.overviewList = this.buildOverview(res.data)
          this.trendData = res.data.trend || []
          // 加载联动和警管数据
          const [linkRes, execRes] = await Promise.all([
            getPerformanceLinkList({ pageSize: 50 }),
            getExecutivePayList({ pageSize: 50 }),
          ])
          this.linkData = (linkRes.data && linkRes.data.tlist) || []
          this.executiveData = (execRes.data && execRes.data.tlist) || []
          this.$nextTick(() => {
            this.initTrendChart()
            this.initLinkChart()
            this.initExecChart()
            this.initRadarChart()
          })
        }
      } catch (e) { console.error('加载驾驶舱数据失败', e) }
    },
    buildOverview(data) {
      const overview = data.overview || []
      return overview.map(item => ({
        name: item.companyName,
        totalSalary: item.totalSalary ? (Number(item.totalSalary) / 10000).toFixed(1) : '0',
        budgetExecRate: Number(item.budgetExecRate || 0).toFixed(1),
        linkRatio: Number(item.linkRatio || 0).toFixed(2),
        laborCostRate: Number(item.laborCostRate || 0).toFixed(1),
        execCompliance: item.execCompliance,
        riskLevel: item.riskLevel,
      }))
    },
    rowClass({ row }) {
      return row.riskLevel === 'HIGH' ? 'row-high' : ''
    },
    linkStyle(v) {
      const n = Number(v)
      if (n < 0.6) return { color: '#F5222D', fontWeight: '700' }
      if (n < 0.8) return { color: '#FA8C16', fontWeight: '700' }
      if (n > 1.5) return { color: '#FA8C16', fontWeight: '700' }
      return { color: '#52C41A' }
    },
    initTrendChart() {
      const ec = echarts
      if (!ec) return
      const c = ec.init(this.$refs.trendChart)
      this.charts.trend = c
      const periods = this.trendData.map(t => t.period)
      const budgetData = this.trendData.map(t => Number(t.budgetTotal || 0) / 10000)
      const actualData = this.trendData.map(t => Number(t.actualTotal || 0) / 10000)
      const execRates = this.trendData.map(t => Number(t.execRate || 0))
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['工资总额（亿）', '执行率(%)'] },
        xAxis: { type: 'category', data: periods },
        yAxis: [
          { type: 'value', name: '亿元' },
          { type: 'value', name: '%', min: 80, max: 120 },
        ],
        series: [
          { name: '工资总额（亿）', type: 'bar', data: actualData, itemStyle: { color: '#1677FF' }, barWidth: '40%' },
          { name: '执行率(%)', type: 'line', yAxisIndex: 1, data: execRates, itemStyle: { color: '#52C41A' }, smooth: true },
        ],
      })
    },
    initLinkChart() {
      const ec = echarts
      if (!ec || !this.linkData.length) return
      const c = ec.init(this.$refs.linkChart)
      this.charts.link = c
      const companies = this.linkData.map(r => (r.companyName || '').replace('集团', '').replace('科技', '').replace('服务', ''))
      const wageGrowth = this.linkData.map(r => Number(r.wageGrowthRate || 0))
      const profitGrowth = this.linkData.map(r => Number(r.profitGrowthRate || 0))
      const series = [
        { name: '工资增长率%', type: 'bar', data: wageGrowth, itemStyle: { color: '#1677FF' }, barWidth: '30%' },
        { name: '利润增长率%', type: 'bar', data: profitGrowth, itemStyle: { color: '#52C41A' }, barWidth: '30%' },
      ]
      // 基准线用实际数据的利润增长率均值
      if (profitGrowth.length) {
        const avgProfitGrowth = profitGrowth.reduce((a, b) => a + b, 0) / profitGrowth.length
        series.push({ name: '利润均值基准', type: 'line', data: Array(companies.length).fill(avgProfitGrowth), lineStyle: { color: '#52C41A', type: 'dashed' }, symbol: 'none' })
      }
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['工资增长率%', '利润增长率%'] },
        xAxis: { type: 'category', data: companies },
        yAxis: { type: 'value', name: '%' },
        series: series,
      })
    },
    initExecChart() {
      const ec = echarts
      if (!ec) return
      const c = ec.init(this.$refs.execChart)
      this.charts.exec = c
      const companies = this.overviewList.map(r => (r.name || '').replace('集团', '').replace('科技', '').replace('服务', ''))
      const rates = this.overviewList.map(r => Number(r.budgetExecRate))
      const colors = rates.map(v => v > 100 ? '#F5222D' : v > 95 ? '#FA8C16' : '#52C41A')
      c.setOption({
        tooltip: { trigger: 'axis', formatter: '{b}: {c}%' },
        xAxis: { type: 'value', min: 80, max: 115, axisLabel: { formatter: '{value}%' } },
        yAxis: { type: 'category', data: companies },
        series: [
          {
            type: 'bar', data: rates.map((v, i) => ({ value: v, itemStyle: { color: colors[i] } })), barWidth: '50%',
            label: { show: true, position: 'right', formatter: '{c}%' },
          },
          { type: 'line', data: Array(companies.length).fill(100), lineStyle: { color: '#F5222D', type: 'dashed', width: 2 }, symbol: 'none' },
        ],
      })
    },
    initRadarChart() {
      const ec = echarts
      if (!ec || !this.executiveData.length) return
      const c = ec.init(this.$refs.radarChart)
      this.charts.radar = c
      // 从高管数据中按企业分组，计算四个维度的真实合规得分
      const companies = [...new Set(this.executiveData.map(r => r.companyName))].slice(0, 3)
      const radarData = companies.map(name => {
        const records = this.executiveData.filter(r => r.companyName === name)
        if (!records.length) return null

        // 维度1：薪酬上限合规 — 基于均值倍数是否超8倍
        const avgRatio = records.reduce((s, r) => s + Number(r.ratioToAvg || 0), 0) / records.length
        const limitScore = Math.max(0, Math.min(100, avgRatio <= 8 ? 100 : 100 - (avgRatio - 8) * 20))

        // 维度2：结构合规 — 绩效薪酬是否超基本薪酬2倍
        const structureViolations = records.filter(r => Number(r.perfSalary || 0) > Number(r.baseSalary || 0) * 2).length
        const structureScore = Math.max(0, Math.min(100, ((records.length - structureViolations) / records.length) * 100))

        // 维度3：任期激励合规 — 任期激励是否超年薪30%
        const incentiveViolations = records.filter(r => Number(r.termIncentive || 0) > Number(r.totalComp || 0) * 0.3).length
        const incentiveScore = Math.max(0, Math.min(100, ((records.length - incentiveViolations) / records.length) * 100))

        // 维度4：整体合规率 — isCompliant为1的比例
        const compliantCount = records.filter(r => r.isCompliant === '1').length
        const overallScore = Math.max(0, Math.min(100, (compliantCount / records.length) * 100))

        return {
          value: [Math.round(limitScore), Math.round(structureScore), Math.round(incentiveScore), Math.round(overallScore)],
          name: name.replace('集团', '').replace('科技', ''),
          areaStyle: { opacity: 0.2 },
        }
      }).filter(Boolean)

      if (!radarData.length) return
      c.setOption({
        tooltip: {},
        legend: { data: radarData.map(d => d.name), bottom: 0 },
        radar: {
          indicator: [
            { name: '薪酬上限合规', max: 100 },
            { name: '结构合规', max: 100 },
            { name: '任期激励合规', max: 100 },
            { name: '整体合规率', max: 100 },
          ],
        },
        series: [{ type: 'radar', data: radarData }],
      })
    },
  },
}
</script>

<style scoped>
.sal-dashboard { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 16px; }
.banner-kpi { margin-top: 8px; }
.bkpi-card { background: rgba(255,255,255,0.12); border-radius: 8px; padding: 12px 16px; text-align: center; }
.bkpi-val { font-size: 22px; font-weight: 700; }
.bkpi-label { font-size: 12px; opacity: 0.85; margin-top: 4px; }
.chart-row { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.chart-tip { font-size: 12px; color: #8c8c8c; margin-left: 12px; }
.overview-card { margin-bottom: 16px; }
::v-deep .row-high td { background: #fff1f0 !important; }
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
</style>
