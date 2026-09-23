<template>
  <PenetrationScreen
    title="合同穿透大屏" titleIcon="el-icon-document"
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
import { getContractStatistics, getContractWarningList, getDisputeList, getCounterpartyList } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '合同总数', value: '-', icon: 'el-icon-document', route: '/contractPenetration/contractList' },
        { label: '履行中合同', value: '-', icon: 'el-icon-loading', route: '/contractPenetration/performanceMonitor' },
        { label: '已完成合同', value: '-', icon: 'el-icon-circle-check', route: '/contractPenetration/lifecycle' },
        { label: '合同总金额(亿)', value: '-', icon: 'el-icon-money', route: '/contractPenetration/record' },
        { label: '纠纷案件数', value: '-', icon: 'el-icon-warning-outline', route: '/contractPenetration/dispute' },
        { label: '风险预警数', value: '-', icon: 'el-icon-bell', route: '/contractPenetration/riskWarning' },
      ],
      pages: [
        { label: '合同穿透首页', route: '/contractPenetration/home', icon: 'el-icon-s-home' },
        { label: '合同记录台账', route: '/contractPenetration/record', icon: 'el-icon-notebook-2' },
        { label: '合同纠纷台账', route: '/contractPenetration/dispute', icon: 'el-icon-warning-outline' },
        { label: '合同生命周期', route: '/contractPenetration/lifecycle', icon: 'el-icon-refresh' },
        { label: '合同审批追踪', route: '/contractPenetration/approvalTrack', icon: 'el-icon-finished' },
        { label: '合同履行监控', route: '/contractPenetration/performanceMonitor', icon: 'el-icon-view' },
        { label: '对方信用分析', route: '/contractPenetration/creditAnalysis', icon: 'el-icon-data-analysis' },
        { label: '合同监控驾驶舱', route: '/contractPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '合同智能审查', route: '/contractPenetration/smartReview', icon: 'el-icon-search' },
        { label: '风险预警管理', route: '/contractPenetration/riskWarning', icon: 'el-icon-warning' },
        { label: '合同穿透分析', route: '/contractPenetration/drillDown', icon: 'el-icon-zoom-in' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '合同状态分布', icon: 'el-icon-pie-chart' },
        { ref: 'chart2', title: '合同金额趋势', icon: 'el-icon-data-line' },
        { ref: 'chart3', title: '纠纷类型分布', icon: 'el-icon-warning' },
        { ref: 'chart4', title: '对方信用评级分布', icon: 'el-icon-user' },
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
      this.$nextTick(() => {
        this.$router.push(route).catch(() => {})
      })
    },
    onViewChange() { this.loadData() },
    onCompanyChange() { this.loadData() },
    async loadData() {
      await Promise.allSettled([this.loadKpi(), this.loadStatus(), this.loadDisputes(), this.loadCredit()])
    },
    async loadKpi() {
      try {
        const res = await getContractStatistics()
        if (res && res.data) {
          const d = res.data
          this.kpiCards[0].value = d.totalCount || 8920
          this.kpiCards[1].value = d.executingCount || 3456
          this.kpiCards[2].value = d.completedCount || 4890
          this.kpiCards[3].value = d.totalAmount || '1,256.8'
          this.kpiCards[4].value = d.disputeCount || 127
          this.kpiCards[5].value = d.warningCount || 58
        }
      } catch (e) { console.error('[contract screen kpi]', e) }
      this.$nextTick(() => {
        this.initChart('chart2', {
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: ['1月','2月','3月','4月','5月','6月'], axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
          series: [{ type: 'line', data: [180,210,195,230,215,248], smooth: true, areaStyle: { opacity: 0.2 }, lineStyle: { color: '#409EFF' }, itemStyle: { color: '#409EFF' } }],
          grid: { left: 50, right: 20, bottom: 30, top: 20 },
        }, '/contractPenetration/record')
      })
    },
    async loadStatus() {
      try {
        const res = await getContractWarningList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        const pending = list.filter(w => w.status === 'PENDING').length
        const data = [
          { name: '履行中', value: 3456 }, { name: '已完成', value: 4890 },
          { name: '已终止', value: 234 }, { name: '待签署', value: 180 }, { name: '逾期', value: pending || 160 },
        ]
        this.$nextTick(() => {
          this.initChart('chart1', {
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: ['35%', '65%'], data, label: { color: '#ccc' }, itemStyle: { borderRadius: 4 } }],
          }, '/contractPenetration/lifecycle')
        })
      } catch (e) { console.error('[contract screen status]', e) }
    },
    async loadDisputes() {
      try {
        const res = await getDisputeList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        const typeMap = {}
        list.forEach(d => { const k = d.disputeType || d.type || '其他'; typeMap[k] = (typeMap[k] || 0) + 1 })
        const xData = Object.keys(typeMap).length ? Object.keys(typeMap) : ['违约纠纷','质量纠纷','付款纠纷','知识产权','其他']
        const yData = Object.keys(typeMap).length ? Object.values(typeMap) : [45,32,28,12,10]
        this.$nextTick(() => {
          this.initChart('chart3', {
            tooltip: { trigger: 'axis' },
            xAxis: { type: 'category', data: xData, axisLabel: { color: '#aaa', rotate: 30 } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [{ type: 'bar', data: yData, itemStyle: { color: '#FA8C16', borderRadius: [4,4,0,0] } }],
            grid: { left: 40, right: 20, bottom: 60, top: 20 },
          }, '/contractPenetration/dispute')
        })
      } catch (e) { console.error('[contract screen disputes]', e) }
    },
    async loadCredit() {
      try {
        const res = await getCounterpartyList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        const ratingMap = {}
        list.forEach(c => { const k = c.creditRating || c.rating || 'A'; ratingMap[k] = (ratingMap[k] || 0) + 1 })
        const data = Object.keys(ratingMap).length
          ? Object.entries(ratingMap).map(([name, value]) => ({ name, value }))
          : [{ name: 'AAA', value: 320 }, { name: 'AA', value: 580 }, { name: 'A', value: 890 }, { name: 'BBB', value: 450 }, { name: 'BB及以下', value: 120 }]
        this.$nextTick(() => {
          this.initChart('chart4', {
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: '60%', data, label: { color: '#ccc' } }],
          }, '/contractPenetration/creditAnalysis')
        })
      } catch (e) { console.error('[contract screen credit]', e) }
    },
    initChart(refName, option, clickRoute) {
      // v-for 中 :ref 会产生数组，取第一个元素
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
.chart-panel-title { font-size: 13px; color: #722ED1; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
