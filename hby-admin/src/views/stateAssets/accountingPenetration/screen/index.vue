<template>
  <PenetrationScreen
    title="会计穿透大屏" titleIcon="el-icon-s-finance"
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
import { getAccountingDashboard, getVoucherAnomalyStats, getBudgetKpi, getTwoGoldKpi } from '@/api/stateAssets/accountingPenetration'

// 造假风险维度标签
const FRAUD_DIMS = ['收入确认', '资产估值', '关联交易', '费用资本化', '表外负债']

export default {
  name: 'AccountingPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '凭证异常数', value: '-', icon: 'el-icon-warning', route: '/accountingPenetration/voucherPenetration' },
        { label: '预算超支企业', value: '-', icon: 'el-icon-data-line', route: '/accountingPenetration/budgetMonitor' },
        { label: '预算执行率', value: '-', icon: 'el-icon-s-flag', route: '/accountingPenetration/budgetMonitor' },
        { label: '两金预警数', value: '-', icon: 'el-icon-coin', route: '/accountingPenetration/twoGoldMonitor' },
        { label: '造假风险数', value: '-', icon: 'el-icon-s-opportunity', route: '/accountingPenetration/fraudDetection' },
        { label: '财务质量分', value: '-', icon: 'el-icon-circle-check', route: '/accountingPenetration/reportPenetration' },
      ],
      pages: [
        { label: '会计穿透首页', route: '/accountingPenetration/home', icon: 'el-icon-s-home' },
        { label: '监控驾驶舱', route: '/accountingPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '会计凭证穿透', route: '/accountingPenetration/voucherPenetration', icon: 'el-icon-document-copy' },
        { label: '账簿穿透查询', route: '/accountingPenetration/bookPenetration', icon: 'el-icon-notebook-1' },
        { label: '财务报表穿透', route: '/accountingPenetration/reportPenetration', icon: 'el-icon-data-analysis' },
        { label: '预算执行监管', route: '/accountingPenetration/budgetMonitor', icon: 'el-icon-data-line' },
        { label: '"两金"压降监控', route: '/accountingPenetration/twoGoldMonitor', icon: 'el-icon-coin' },
        { label: '财务造假识别', route: '/accountingPenetration/fraudDetection', icon: 'el-icon-s-opportunity' },
        { label: '会计政策与估计', route: '/accountingPenetration/policyAndEstimate', icon: 'el-icon-s-management' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '凭证异常类型分布', icon: 'el-icon-warning' },
        { ref: 'chart2', title: '预算执行情况', icon: 'el-icon-data-line' },
        { ref: 'chart3', title: '两金应收/存货趋势', icon: 'el-icon-coin' },
        { ref: 'chart4', title: '财务造假风险分布', icon: 'el-icon-s-opportunity' },
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
      await Promise.allSettled([
        this.loadKpiAndFraud(),
        this.loadVoucherAnomaly(),
        this.loadBudget(),
        this.loadTwoGold(),
      ])
    },

    // KPI + chart4（造假风险）：均来自 dashboard/overview
    async loadKpiAndFraud() {
      // 默认 chart4 静态数据
      let heatmapData = [
        { companyShort: '示例金融', scores: [92, 85, 78, 68, 82] },
        { companyShort: '示例矿业', scores: [75, 70, 78, 52, 95] },
        { companyShort: '示例数字', scores: [60, 45, 50, 85, 50] },
        { companyShort: '华能集团', scores: [35, 82, 40, 30, 45] },
        { companyShort: '中国华润', scores: [38, 42, 80, 30, 40] },
      ]
      try {
        const res = await getAccountingDashboard({})
        if (res && res.data) {
          const d = res.data
          // KPI 字段与接口对齐
          this.kpiCards[0].value = d.voucherAnomalyCount != null ? d.voucherAnomalyCount : '-'
          this.kpiCards[1].value = d.budgetStats ? (d.budgetStats.overCount != null ? d.budgetStats.overCount : '-') : '-'
          this.kpiCards[2].value = d.budgetStats ? (d.budgetStats.reachRate != null ? d.budgetStats.reachRate + '%' : '-') : '-'
          this.kpiCards[3].value = d.twoGoldWarnCount != null ? d.twoGoldWarnCount : '-'
          this.kpiCards[4].value = d.fraudRiskCount != null ? d.fraudRiskCount : '-'
          this.kpiCards[5].value = d.qualityScore != null ? d.qualityScore : '-'
          // 公司下拉列表
          if (Array.isArray(d.companyOptions) && d.companyOptions.length) {
            this.companyList = d.companyOptions.filter(c => c.value)
          }
          // chart4 数据：heatmapData 各公司造假风险评分
          if (Array.isArray(d.heatmapData) && d.heatmapData.length) {
            // 去重（接口有重复公司），取每家公司第一条
            const seen = new Set()
            heatmapData = d.heatmapData.filter(item => {
              if (seen.has(item.companyShort)) return false
              seen.add(item.companyShort)
              return true
            }).slice(0, 8)
          }
        }
      } catch (e) { console.error('[accounting screen kpi]', e) }

      // chart4：各公司造假风险雷达/柱图，用各维度最高分展示风险分布
      const companies = heatmapData.map(i => i.companyShort)
      // 取每家公司的平均风险分（scores 均值）作为综合风险指数
      const avgScores = heatmapData.map(i => {
        const s = i.scores || []
        return s.length ? Math.round(s.reduce((a, b) => a + b, 0) / s.length) : 0
      })
      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: {
            trigger: 'axis',
            formatter: params => `${params[0].name}<br/>综合风险指数: ${params[0].value}<br/><small style="color:#aaa">（${FRAUD_DIMS.join('/')}）</small>`,
          },
          xAxis: {
            type: 'category', data: companies,
            axisLabel: { color: '#aaa', rotate: 20, fontSize: 11 },
          },
          yAxis: {
            type: 'value', min: 0, max: 100,
            axisLabel: { color: '#aaa', formatter: '{value}分' },
            name: '风险指数', nameTextStyle: { color: '#aaa', fontSize: 11 },
          },
          series: [{
            type: 'bar', data: avgScores,
            itemStyle: {
              color: p => p.value >= 80 ? '#FF4D4F' : p.value >= 60 ? '#FA8C16' : '#52C41A',
              borderRadius: [4, 4, 0, 0],
            },
            label: { show: true, position: 'top', color: '#ccc', fontSize: 11, formatter: '{c}' },
          }],
          grid: { left: 50, right: 20, bottom: 50, top: 35 },
        }, '/accountingPenetration/fraudDetection')
      })
    },

    // chart1：凭证异常类型分布，接口返回 {endPeriodRush, pendingCheck, totalAnomaly, noOriginalVoucher}
    async loadVoucherAnomaly() {
      let xData = ['期末突击', '待核查', '无原始凭证', '其他']
      let yData = [4, 3, 4, 1]
      try {
        const res = await getVoucherAnomalyStats({})
        if (res && res.data) {
          const d = res.data
          // 接口返回具体分类字段，不是 types 数组
          xData = ['期末突击记账', '待核查凭证', '无原始凭证', '其他异常']
          yData = [
            d.endPeriodRush || 0,
            d.pendingCheck || 0,
            d.noOriginalVoucher || 0,
            Math.max(0, (d.totalAnomaly || 0) - (d.endPeriodRush || 0) - (d.pendingCheck || 0) - (d.noOriginalVoucher || 0)),
          ]
        }
      } catch (e) { console.error('[accounting screen voucher]', e) }
      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'axis', formatter: params => `${params[0].name}: ${params[0].value}条` },
          xAxis: { type: 'category', data: xData, axisLabel: { color: '#aaa', rotate: 15 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' }, name: '条数', nameTextStyle: { color: '#aaa' } },
          series: [{
            type: 'bar', data: yData,
            itemStyle: { color: '#FF4D4F', borderRadius: [4, 4, 0, 0] },
            label: { show: true, position: 'top', color: '#ccc', fontSize: 11 },
          }],
          grid: { left: 45, right: 20, bottom: 50, top: 35 },
        }, '/accountingPenetration/voucherPenetration')
      })
    },

    // chart2：预算执行情况，接口返回 {kpis: [{label, val}]}
    async loadBudget() {
      let labels = ['总预算', '已执行', '超支企业', '执行率']
      let values = [283700, 217780, 12, 76.8]
      let isRate = [false, false, false, true]
      try {
        const res = await getBudgetKpi({})
        if (res && res.data && Array.isArray(res.data.kpis) && res.data.kpis.length) {
          const kpis = res.data.kpis
          labels = kpis.map(k => k.label)
          values = kpis.map(k => {
            const v = k.val
            if (typeof v === 'string' && v.endsWith('%')) return parseFloat(v)
            return typeof v === 'number' ? (v > 10000 ? Math.round(v / 10000) : v) : 0
          })
          isRate = kpis.map(k => typeof k.val === 'string' && k.val.endsWith('%'))
        }
      } catch (e) { console.error('[accounting screen budget]', e) }
      this.$nextTick(() => {
        this.initChart('chart2', {
          tooltip: {
            trigger: 'axis',
            formatter: params => {
              const i = params[0].dataIndex
              return `${params[0].name}: ${params[0].value}${isRate[i] ? '%' : '万'}`
            },
          },
          xAxis: { type: 'category', data: labels, axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
          series: [{
            type: 'bar', data: values,
            itemStyle: {
              color: p => ['#409EFF', '#52C41A', '#FF4D4F', '#FA8C16'][p.dataIndex % 4],
              borderRadius: [4, 4, 0, 0],
            },
            label: { show: true, position: 'top', color: '#ccc', fontSize: 11 },
          }],
          grid: { left: 45, right: 20, bottom: 30, top: 35 },
        }, '/accountingPenetration/budgetMonitor')
      })
    },

    // chart3：两金趋势，接口返回 {kpis: [{label, val}]}，用 dashboard 的 twoGoldTrend
    async loadTwoGold() {
      // 默认静态数据
      let companies = ['公司1', '公司2', '公司3', '公司4', '公司5', '公司6']
      let receivable = [0, 0, 4100, 2200, 11200, 6200]
      let inventory = [0, 0, 4800, 800, 5100, 3800]
      try {
        // 从 dashboard 接口取 twoGoldTrend（各公司应收/存货数据）
        const res = await getAccountingDashboard({})
        if (res && res.data && Array.isArray(res.data.twoGoldTrend) && res.data.twoGoldTrend.length) {
          const trend = res.data.twoGoldTrend.slice(0, 8)
          companies = trend.map((_, i) => `企业${i + 1}`)
          receivable = trend.map(t => Math.round((t.receivable || 0) / 10000)) // 万→亿
          inventory = trend.map(t => Math.round((t.inventory || 0) / 10000))
        }
      } catch (e) { console.error('[accounting screen twogold]', e) }

      // chart3 独立 nextTick，与 chart4 完全分开
      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'axis' },
          legend: { data: ['应收账款(亿)', '存货(亿)'], textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: companies, axisLabel: { color: '#aaa', rotate: 20 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}亿' } },
          series: [
            { name: '应收账款(亿)', type: 'bar', data: receivable, barGap: 0, itemStyle: { color: '#FA8C16', borderRadius: [4, 4, 0, 0] } },
            { name: '存货(亿)', type: 'bar', data: inventory, itemStyle: { color: '#409EFF', borderRadius: [4, 4, 0, 0] } },
          ],
          grid: { left: 45, right: 20, bottom: 50, top: 20 },
        }, '/accountingPenetration/twoGoldMonitor')
      })
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
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(64,158,255,0.15); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #E6A23C; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
