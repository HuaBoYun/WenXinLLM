<template>
  <PenetrationScreen
    title="薪酬穿透大屏" titleIcon="el-icon-user"
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
import { getSalaryTotalList, getExecutivePayList, getLinkHistoryTrend, getLaborCostTrend } from '@/api/stateAssets/salaryPenetration'

export default {
  name: 'SalaryPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      viewMode: 'group', currentCompany: null,
      kpiCards: [
        { label: '薪酬总额(亿)', value: '-', icon: 'el-icon-money', route: '/salaryPenetration/total' },
        { label: '高管薪酬(万)', value: '-', icon: 'el-icon-user', route: '/salaryPenetration/executivePay' },
        { label: '人均薪酬(万)', value: '-', icon: 'el-icon-s-custom', route: '/salaryPenetration/total' },
        { label: '薪酬增长率(%)', value: '-', icon: 'el-icon-data-line', route: '/salaryPenetration/performanceLink' },
        { label: '合规问题数', value: '-', icon: 'el-icon-warning', route: '/salaryPenetration/complianceCheck' },
        { label: '风险预警数', value: '-', icon: 'el-icon-bell', route: '/salaryPenetration/riskWarning' },
      ],
      pages: [
        { label: '薪酬穿透首页', route: '/salaryPenetration/home', icon: 'el-icon-s-home' },
        { label: '薪酬总额台账', route: '/salaryPenetration/total', icon: 'el-icon-document' },
        { label: '高管薪酬分析', route: '/salaryPenetration/executivePay', icon: 'el-icon-user' },
        { label: '效益联动分析', route: '/salaryPenetration/performanceLink', icon: 'el-icon-data-line' },
        { label: '中长期激励计划', route: '/salaryPenetration/incentivePlan', icon: 'el-icon-trophy' },
        { label: '人工成本分析', route: '/salaryPenetration/laborCost', icon: 'el-icon-s-finance' },
        { label: '薪酬合规检查', route: '/salaryPenetration/complianceCheck', icon: 'el-icon-circle-check' },
        { label: '薪酬监控驾驶舱', route: '/salaryPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '工资总额管理', route: '/salaryPenetration/totalList', icon: 'el-icon-s-order' },
        { label: '薪酬风险预警', route: '/salaryPenetration/riskWarning', icon: 'el-icon-bell' },
        { label: '薪酬穿透分析', route: '/salaryPenetration/drillDown', icon: 'el-icon-zoom-in' },
      ],
      companyList: [{ label: '全集团', value: null }, { label: '子公司A', value: 'A' }, { label: '子公司B', value: 'B' }],
      chartPanels: [
        { ref: 'chart1', title: '薪酬结构分布', icon: 'el-icon-pie-chart' },
        { ref: 'chart2', title: '高管薪酬对比', icon: 'el-icon-s-marketing' },
        { ref: 'chart3', title: '效益联动趋势', icon: 'el-icon-data-line' },
        { ref: 'chart4', title: '人工成本分析', icon: 'el-icon-s-finance' },
      ],
    }
  },
  mounted() { this.$nextTick(() => { setTimeout(() => this.loadAll(), 300) }) },
  beforeDestroy() { (this._charts || []).forEach(c => c.dispose()) },
  methods: {
    onViewChange(v) { this.viewMode = v; this.$nextTick(() => this.loadAll()) },
    onCompanyChange(v) { this.currentCompany = v; this.$nextTick(() => this.loadAll()) },
    async loadAll() {
      await Promise.all([this.loadKpi(), this.loadCharts()])
    },
    async loadKpi() {
      try {
        const res = await getSalaryTotalList({ pageNumber: 1, pageSize: 1000 })
        if (res && res.code == 1 && res.data) {
          const list = res.data.tlist || res.data.list || []
          const total = list.reduce((s, i) => s + (Number(i.totalSalary) || 0), 0)
          this.kpiCards[0].value = (total / 10000).toFixed(2)
          this.kpiCards[2].value = list.length ? (total / list.length / 10000).toFixed(1) : '-'
        }
      } catch (e) { console.warn('薪酬KPI加载失败', e) }
      try {
        const res = await getExecutivePayList({ pageNumber: 1, pageSize: 100 })
        if (res && res.code == 1 && res.data) {
          const list = res.data.tlist || res.data.list || []
          const avg = list.length ? list.reduce((s, i) => s + (Number(i.totalPay) || 0), 0) / list.length : 0
          this.kpiCards[1].value = avg.toFixed(0)
        }
      } catch (e) { console.warn('高管薪酬KPI加载失败', e) }
    },
    async loadCharts() {
      // 图表1：薪酬结构饼图
      try {
        const res = await getSalaryTotalList({ pageNumber: 1, pageSize: 1000 })
        const list = (res && res.code == 1 && res.data) ? (res.data.tlist || res.data.list || []) : []
        const pieData = [
          { name: '基本工资', value: list.reduce((s, i) => s + (Number(i.baseSalary) || 0), 0) || 60 },
          { name: '绩效奖金', value: list.reduce((s, i) => s + (Number(i.bonus) || 0), 0) || 25 },
          { name: '福利补贴', value: list.reduce((s, i) => s + (Number(i.allowance) || 0), 0) || 15 },
        ]
        this.initPie('chart1', pieData, '/salaryPenetration/total')
      } catch (e) { this.initPie('chart1', [{name:'基本工资',value:60},{name:'绩效奖金',value:25},{name:'福利补贴',value:15}], '/salaryPenetration/total') }
      // 图表2：高管薪酬柱状图
      try {
        const res = await getExecutivePayList({ pageNumber: 1, pageSize: 10 })
        const list = (res && res.code == 1 && res.data) ? (res.data.tlist || res.data.list || []) : []
        const names = list.map(i => i.name || i.execName || '高管').slice(0, 8)
        const vals = list.map(i => Number(i.totalPay) || 0).slice(0, 8)
        this.initBar('chart2', names.length ? names : ['高管A','高管B','高管C','高管D'], vals.length ? vals : [120,98,86,75], '/salaryPenetration/executivePay', '#FA8C16')
      } catch (e) { this.initBar('chart2', ['高管A','高管B','高管C','高管D'], [120,98,86,75], '/salaryPenetration/executivePay', '#FA8C16') }
      // 图表3：效益联动趋势折线图
      try {
        const res = await getLinkHistoryTrend(this.currentCompany)
        const list = (res && res.code == 1 && res.data) ? (res.data.list || res.data || []) : []
        const months = list.map(i => i.month || i.period || '').slice(0, 12)
        const vals = list.map(i => Number(i.coefficient) || 0).slice(0, 12)
        this.initLine('chart3', months.length ? months : ['1月','2月','3月','4月','5月','6月'], vals.length ? vals : [1.0,1.05,1.08,1.02,1.1,1.15], '/salaryPenetration/performanceLink')
      } catch (e) { this.initLine('chart3', ['1月','2月','3月','4月','5月','6月'], [1.0,1.05,1.08,1.02,1.1,1.15], '/salaryPenetration/performanceLink') }
      // 图表4：人工成本柱状图
      try {
        const res = await getLaborCostTrend(this.currentCompany)
        const list = (res && res.code == 1 && res.data) ? (res.data.list || res.data || []) : []
        const months = list.map(i => i.month || i.period || '').slice(0, 6)
        const vals = list.map(i => Number(i.cost) || 0).slice(0, 6)
        this.initBar('chart4', months.length ? months : ['Q1','Q2','Q3','Q4'], vals.length ? vals : [320,350,380,410], '/salaryPenetration/laborCost', '#52C41A')
      } catch (e) { this.initBar('chart4', ['Q1','Q2','Q3','Q4'], [320,350,380,410], '/salaryPenetration/laborCost', '#52C41A') }
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
      this._charts = this._charts || []; this._charts.push(chart)
    },
    initPie(ref, data, route) {
      this.initChart(ref, { tooltip: { trigger: 'item' }, legend: { orient: 'vertical', right: 10, textStyle: { color: '#ccc' } },
        series: [{ type: 'pie', radius: '60%', data, label: { color: '#ccc' } }], backgroundColor: 'transparent' }, route)
    },
    initBar(ref, xData, data, route, color = '#409EFF') {
      this.initChart(ref, { tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: xData, axisLabel: { color: '#ccc', rotate: 30 } },
        yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
        series: [{ type: 'bar', data, itemStyle: { color } }], backgroundColor: 'transparent' }, route)
    },
    initLine(ref, xData, data, route) {
      this.initChart(ref, { tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: xData, axisLabel: { color: '#ccc' } },
        yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
        series: [{ type: 'line', smooth: true, data, lineStyle: { color: '#409EFF' }, itemStyle: { color: '#409EFF' } }],
        backgroundColor: 'transparent' }, route)
    },
    goTo(route) {
      if (!route) return
      this.$emit('close')
      this.$nextTick(() => {
        this.$router.push(route).catch(() => {})
      })
    },
  },
}
</script>

<style scoped>
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 1fr 1fr; gap: 12px; flex: 1; min-height: 0; }
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(64,158,255,0.15); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #409EFF; margin-bottom: 6px; }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
