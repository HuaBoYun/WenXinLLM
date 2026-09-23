<template>
  <PenetrationScreen
    title="金融穿透大屏" titleIcon="el-icon-bank-card"
    :kpiCards="kpiCards" :pages="pages" :companyList="companyList"
    @goto="goTo" @close="$emit('close')" @view-change="onViewChange" @company-change="onCompanyChange"
  >
    <template #default>
      <div class="chart-grid">
        <div class="chart-panel" v-for="c in chartPanels" :key="c.ref">
          <div class="chart-panel-title"><i :class="c.icon"></i> {{ c.title }}</div>
          <div :ref="c.ref" class="chart-box"></div>
        </div>
      </div>
    </template>
  </PenetrationScreen>
</template>

<script>
import * as echarts from 'echarts'
import PenetrationScreen from '@/views/stateAssets/components/PenetrationScreen.vue'
import { getFinancialRiskDashboard } from '@/api/stateAssets/financialRiskPenetration'
import { getEquityPenetrationTree } from '@/api/stateAssets/equityStructure'
import { getBeneficialOwnerStatistics } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'EquityPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '受控企业数', value: '-', icon: 'el-icon-office-building', route: '/equityPenetration/controlChain' },
        { label: '实际控制人数', value: '-', icon: 'el-icon-user', route: '/equityPenetration/beneficialOwner' },
        { label: '融资总额(亿)', value: '-', icon: 'el-icon-bank-card', route: '/financialRiskPenetration/financing' },
        { label: '担保总额(亿)', value: '-', icon: 'el-icon-s-finance', route: '/financialRiskPenetration/guarantee' },
        { label: '高风险企业数', value: '-', icon: 'el-icon-warning', route: '/financialRiskPenetration/liquidity' },
        { label: '活跃预警数', value: '-', icon: 'el-icon-bell', route: '/financialRiskPenetration/dashboard' },
      ],
      pages: [
        { label: '金融穿透首页', route: '/equityPenetration/home', icon: 'el-icon-s-home' },
        { label: '实际控制人', route: '/equityPenetration/beneficialOwner', icon: 'el-icon-user' },
        { label: '控制链分析', route: '/equityPenetration/controlChain', icon: 'el-icon-share' },
        { label: '股权结构', route: '/equityPenetration/equityStructure', icon: 'el-icon-s-grid' },
        { label: '股东穿透分析', route: '/equityPenetration/shareholderAnalysis', icon: 'el-icon-zoom-in' },
        { label: '股权变动', route: '/equityPenetration/equityChanges', icon: 'el-icon-sort' },
        { label: '融资记录台账', route: '/financialRiskPenetration/financing', icon: 'el-icon-document' },
        { label: '担保记录台账', route: '/financialRiskPenetration/guarantee', icon: 'el-icon-tickets' },
        { label: '委托贷款监控', route: '/financialRiskPenetration/entrustedLoan', icon: 'el-icon-s-order' },
        { label: '衍生品业务监控', route: '/financialRiskPenetration/derivatives', icon: 'el-icon-data-line' },
        { label: '流动性风险分析', route: '/financialRiskPenetration/liquidity', icon: 'el-icon-warning-outline' },
        { label: '金融风险穿透分析', route: '/financialRiskPenetration/drillDown', icon: 'el-icon-aim' },
        { label: '金融风险驾驶舱', route: '/financialRiskPenetration/dashboard', icon: 'el-icon-odometer' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '股权层级企业分布', icon: 'el-icon-share' },
        { ref: 'chart2', title: '融资担保规模趋势', icon: 'el-icon-data-line' },
        { ref: 'chart3', title: '金融风险等级分布', icon: 'el-icon-warning' },
        { ref: 'chart4', title: '融资到期期限分布', icon: 'el-icon-bank-card' },
      ],
      _charts: [],
    }
  },
  mounted() { this.$nextTick(() => { setTimeout(() => this.loadData(), 300) }) },
  beforeDestroy() { this._charts.forEach(c => c.dispose()) },
  methods: {
    goTo(route) {
      if (!route) return
      this.$emit('close')
      this.$nextTick(() => { this.$router.push(route).catch(() => {}) })
    },
    onViewChange() { this.loadData() },
    onCompanyChange() { this.loadData() },
    async loadData() {
      // 并行加载，每个图表独立渲染，互不阻塞
      await Promise.allSettled([
        this.loadEquityChart(),
        this.loadFinancialRiskCharts(),
        this.loadKpi(),
      ])
    },

    // chart1：股权层级分布（调用穿透树接口，统计各层节点数）
    async loadEquityChart() {
      let levelData = [1, 4, 6, 8, 5, 2] // 默认静态数据
      try {
        const res = await getEquityPenetrationTree({})
        if (res && res.data) {
          const counts = {}
          const walk = (node, lv) => {
            counts[lv] = (counts[lv] || 0) + 1
            ;(node.children || []).forEach(c => walk(c, lv + 1))
          }
          walk(res.data, 0)
          const maxLv = Math.max(...Object.keys(counts).map(Number))
          levelData = Array.from({ length: maxLv + 1 }, (_, i) => counts[i] || 0)
        }
      } catch (e) { /* 使用默认数据 */ }
      const labels = levelData.map((_, i) => `第${i + 1}层`)
      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'axis', formatter: '{b}: {c}家企业' },
          xAxis: { type: 'category', data: labels, axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' }, name: '企业数', nameTextStyle: { color: '#aaa' } },
          series: [{ type: 'bar', data: levelData,
            itemStyle: { color: (p) => `rgba(74,144,217,${0.4 + p.dataIndex * 0.1})`, borderRadius: [4, 4, 0, 0] },
          }],
          grid: { left: 45, right: 20, bottom: 30, top: 30 },
        }, '/equityPenetration/equityStructure')
      })
    },

    // chart2/3/4：从 financial-risk/dashboard 接口获取数据
    async loadFinancialRiskCharts() {
      let trendMonths = ['2025-12', '2026-01', '2026-02', '2026-03', '2026-04', '2026-05']
      let financingScale = [0.02, 0.02, 0.02, 0.02, 0.02, 0.02]
      let guaranteeBalance = [0.01, 0.01, 0.01, 0.01, 0.01, 0.01]
      let riskOverview = [{ name: '高风险', value: 2 }, { name: '中风险', value: 2 }, { name: '低风险', value: 5 }]
      let maturityPeriods = ['1个月内', '1-3个月', '3-6个月', '6-12个月', '1年以上']
      let maturityAmounts = [0.01, 0, 0, 0, 0.01]
      try {
        const res = await getFinancialRiskDashboard()
        if (res && res.data) {
          const d = res.data
          if (d.trendData) {
            trendMonths = d.trendData.months || trendMonths
            financingScale = d.trendData.financingScale || financingScale
            guaranteeBalance = d.trendData.guaranteeBalance || guaranteeBalance
          }
          if (Array.isArray(d.riskOverview) && d.riskOverview.length) riskOverview = d.riskOverview
          if (d.maturityDistribution) {
            maturityPeriods = d.maturityDistribution.periods || maturityPeriods
            maturityAmounts = d.maturityDistribution.amounts || maturityAmounts
          }
        }
      } catch (e) { console.error('[equity screen financial]', e) }

      // chart2：融资担保趋势
      this.$nextTick(() => {
        this.initChart('chart2', {
          tooltip: { trigger: 'axis' },
          legend: { data: ['融资规模(亿)', '担保余额(亿)'], textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: trendMonths, axisLabel: { color: '#aaa', rotate: 30 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
          series: [
            { name: '融资规模(亿)', type: 'line', data: financingScale, smooth: true, lineStyle: { color: '#409EFF' }, itemStyle: { color: '#409EFF' } },
            { name: '担保余额(亿)', type: 'line', data: guaranteeBalance, smooth: true, lineStyle: { color: '#FA8C16' }, itemStyle: { color: '#FA8C16' } },
          ],
          grid: { left: 50, right: 20, bottom: 50, top: 20 },
        }, '/financialRiskPenetration/financing')
      })

      // chart3：风险等级分布
      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}家 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{ type: 'pie', radius: '60%', data: riskOverview,
            label: { color: '#ccc' }, color: ['#FF4D4F', '#FA8C16', '#52C41A', '#722ED1'],
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
          }],
        }, '/financialRiskPenetration/liquidity')
      })

      // chart4：融资到期期限分布
      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: { trigger: 'axis', formatter: params => `${params[0].name}<br/>到期金额: ${params[0].value}亿` },
          xAxis: { type: 'category', data: maturityPeriods, axisLabel: { color: '#aaa', rotate: 20 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}亿' } },
          series: [{ type: 'bar', data: maturityAmounts,
            itemStyle: { color: (p) => ['#52C41A', '#409EFF', '#FA8C16', '#FF7A45', '#FF4D4F'][p.dataIndex] || '#409EFF', borderRadius: [4, 4, 0, 0] },
          }],
          grid: { left: 50, right: 20, bottom: 50, top: 20 },
        }, '/financialRiskPenetration/derivatives')
      })
    },

    // KPI：从 beneficial-owner/statistics 和 financial-risk/dashboard 获取
    async loadKpi() {
      try {
        const [ownerRes, dashRes] = await Promise.allSettled([
          getBeneficialOwnerStatistics({}),
          getFinancialRiskDashboard(),
        ])
        if (ownerRes.status === 'fulfilled' && ownerRes.value && ownerRes.value.data) {
          const o = ownerRes.value.data
          this.kpiCards[0].value = o.totalEnterprises || o.enterpriseCount || '-'
          this.kpiCards[1].value = o.totalOwners || o.actualControllerCount || '-'
        }
        if (dashRes.status === 'fulfilled' && dashRes.value && dashRes.value.data && dashRes.value.data.kpi) {
          const k = dashRes.value.data.kpi
          this.kpiCards[2].value = k.totalFinancing != null ? k.totalFinancing : '-'
          this.kpiCards[3].value = k.totalGuarantee != null ? k.totalGuarantee : '-'
          this.kpiCards[4].value = k.highRiskCount != null ? k.highRiskCount : '-'
          this.kpiCards[5].value = k.activeAlertCount != null ? k.activeAlertCount : '-'
        }
      } catch (e) { console.error('[equity screen kpi]', e) }
    },

    initChart(refName, option, clickRoute) {
      let el = this.$refs[refName]
      if (Array.isArray(el)) el = el[0]
      if (!el) return
      const existing = echarts.getInstanceByDom(el)
      if (existing) existing.dispose()
      const chart = echarts.init(el)
      chart.setOption(option)
      chart.resize()
      if (clickRoute) chart.on('click', () => this.goTo(clickRoute))
      this._charts.push(chart)
    },
  },
}
</script>

<style lang="scss" scoped>
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 1fr 1fr; gap: 12px; flex: 1; min-height: 0; }
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(74,144,217,0.2); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #4A90D9; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
