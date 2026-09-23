<template>
  <PenetrationScreen
    title="产权穿透大屏" titleIcon="el-icon-s-home"
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
import { getPropertyStatistics, getPropertyTransactionList } from '@/api/stateAssets/propertyPenetration'

// 交易类型中文映射
const TRANS_TYPE_CN = { TRANSFER: '股权转让', INCREASE: '增资', DECREASE: '减资', CAPITAL_DECREASE: '减资' }
// 合规状态中文映射
const COMPLIANCE_CN = { VIOLATION: '违规', ISSUE: '问题', COMPLIANT: '合规', NORMAL: '正常' }

export default {
  name: 'PropertyPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '产权登记总数', value: '-', icon: 'el-icon-s-home', route: '/propertyPenetration/right' },
        { label: '正常登记数', value: '-', icon: 'el-icon-circle-check', route: '/propertyPenetration/right' },
        { label: '差异登记数', value: '-', icon: 'el-icon-warning-outline', route: '/propertyPenetration/right' },
        { label: '产权交易笔数', value: '-', icon: 'el-icon-money', route: '/propertyPenetration/transaction' },
        { label: '合规问题数', value: '-', icon: 'el-icon-warning', route: '/propertyPenetration/tradeReview' },
        { label: '注销登记数', value: '-', icon: 'el-icon-remove-outline', route: '/propertyPenetration/right' },
      ],
      pages: [
        { label: '产权穿透首页', route: '/propertyPenetration/home', icon: 'el-icon-s-home' },
        { label: '产权登记台账', route: '/propertyPenetration/right', icon: 'el-icon-document' },
        { label: '产权交易台账', route: '/propertyPenetration/transaction', icon: 'el-icon-s-order' },
        { label: '股权结构分析', route: '/equityPenetration/equityStructure', icon: 'el-icon-share' },
        { label: '控制链分析', route: '/equityPenetration/controlChain', icon: 'el-icon-connection' },
        { label: '股权变动记录', route: '/equityPenetration/equityChanges', icon: 'el-icon-refresh' },
        { label: '受益所有人', route: '/equityPenetration/beneficialOwner', icon: 'el-icon-user' },
        { label: '三表比对看板', route: '/propertyPenetration/threeTableCompare', icon: 'el-icon-s-grid' },
        { label: '股权穿透图', route: '/propertyPenetration/equityChart', icon: 'el-icon-picture-outline' },
        { label: '产权变动登记', route: '/propertyPenetration/changeRecord', icon: 'el-icon-edit-outline' },
        { label: '产权交易合规审查', route: '/propertyPenetration/tradeReview', icon: 'el-icon-circle-check' },
        { label: '参股企业经营分析', route: '/propertyPenetration/shareholding', icon: 'el-icon-s-data' },
        { label: '产权监控驾驶舱', route: '/propertyPenetration/dashboard', icon: 'el-icon-odometer' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '产权登记状态分布', icon: 'el-icon-share' },
        { ref: 'chart2', title: '产权交易金额趋势', icon: 'el-icon-data-line' },
        { ref: 'chart3', title: '参股企业经营分布', icon: 'el-icon-pie-chart' },
        { ref: 'chart4', title: '产权合规风险分布', icon: 'el-icon-warning' },
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
      // 四个图表完全独立，互不阻塞
      await Promise.allSettled([
        this.loadStatisticsCharts(),
        this.loadTransactionCharts(),
      ])
    },

    // chart1（登记状态分布）+ chart3（参股企业经营分布）+ KPI：均来自 right/statistics
    async loadStatisticsCharts() {
      // 默认静态数据
      let normalCount = 22, lossCount = 9, differentCount = 4, cancelledCount = 2, totalCount = 33
      try {
        const res = await getPropertyStatistics()
        if (res && res.data) {
          const d = res.data
          // 接口字段：totalCount / normalCount / differentCount / cancelledCount / lossCount
          totalCount = d.totalCount || totalCount
          normalCount = d.normalCount || normalCount
          lossCount = d.lossCount || lossCount
          differentCount = d.differentCount || differentCount
          cancelledCount = d.cancelledCount || cancelledCount
          // KPI 字段与接口对齐
          this.kpiCards[0].value = totalCount
          this.kpiCards[1].value = normalCount
          this.kpiCards[2].value = differentCount
          this.kpiCards[5].value = cancelledCount
        }
      } catch (e) { console.error('[property screen statistics]', e) }

      // chart1：产权登记状态分布（饼图）
      const registryData = [
        { name: '正常', value: normalCount },
        { name: '差异', value: differentCount },
        { name: '亏损', value: lossCount },
        { name: '注销', value: cancelledCount },
      ].filter(d => d.value > 0)

      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}条 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{
            type: 'pie', radius: ['35%', '65%'], data: registryData,
            label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
            color: ['#52C41A', '#FA8C16', '#FF4D4F', '#909399'],
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(39,174,96,0.5)' } },
          }],
        }, '/propertyPenetration/right')
      })

      // chart3：参股企业经营分布（用登记状态数据反映经营情况）
      // 正常→盈利，差异→微利，亏损→亏损，注销→退出
      const operationData = [
        { name: '正常经营', value: normalCount },
        { name: '差异关注', value: differentCount },
        { name: '亏损预警', value: lossCount },
        { name: '已注销', value: cancelledCount },
      ].filter(d => d.value > 0)

      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}家 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{
            type: 'pie', radius: '60%', data: operationData,
            label: { color: '#ccc' },
            color: ['#52C41A', '#FA8C16', '#FF4D4F', '#909399'],
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
          }],
        }, '/propertyPenetration/shareholding')
      })
    },

    // chart2（交易金额趋势）+ chart4（合规风险分布）+ KPI：均来自 transaction/list
    async loadTransactionCharts() {
      let months = ['03月', '04月', '05月', '06月', '07月', '08月', '09月', '10月', '11月', '12月']
      let amounts = [0.8, 4.5, 2.5, 5.0, 1.2, 2.0, 3.0, 0.6, 1.5, 1.8]
      let complianceData = [{ name: '违规', value: 8 }, { name: '问题', value: 2 }]
      let transTotal = 10, complianceIssueCount = 10

      try {
        const res = await getPropertyTransactionList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        if (list.length) {
          transTotal = res.data.totalRecord || list.length
          // 按月统计交易金额（字段是 transAmount，不是 amount）
          const monthMap = {}
          list.forEach(t => {
            const m = (t.transactionDate || t.transDate || '').substring(0, 7)
            if (m) monthMap[m] = (monthMap[m] || 0) + (t.transAmount || t.transactionAmount || 0)
          })
          const sortedMonths = Object.keys(monthMap).sort()
          if (sortedMonths.length) {
            months = sortedMonths.map(m => m.substring(5) + '月')
            amounts = sortedMonths.map(m => Math.round(monthMap[m] / 10000 * 10) / 10) // 万→亿
          }
          // 统计合规状态分布（complianceStatus: VIOLATION/ISSUE/COMPLIANT）
          const compMap = {}
          list.forEach(t => {
            const label = COMPLIANCE_CN[t.complianceStatus] || t.complianceStatus || '未知'
            compMap[label] = (compMap[label] || 0) + 1
          })
          complianceData = Object.entries(compMap).map(([name, value]) => ({ name, value }))
          complianceIssueCount = list.filter(t => t.complianceStatus === 'VIOLATION' || t.complianceStatus === 'ISSUE').length
        }
      } catch (e) { console.error('[property screen transactions]', e) }

      // KPI 更新
      this.kpiCards[3].value = transTotal
      this.kpiCards[4].value = complianceIssueCount

      // chart2：交易金额趋势（独立 nextTick）
      this.$nextTick(() => {
        this.initChart('chart2', {
          tooltip: { trigger: 'axis', formatter: params => `${params[0].name}<br/>交易金额: ${params[0].value}亿` },
          xAxis: { type: 'category', data: months, axisLabel: { color: '#aaa', rotate: 30 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}亿' }, name: '亿元', nameTextStyle: { color: '#aaa' } },
          series: [{
            type: 'bar', data: amounts,
            itemStyle: { color: '#27AE60', borderRadius: [4, 4, 0, 0] },
            label: { show: true, position: 'top', color: '#ccc', fontSize: 10, formatter: '{c}亿' },
          }],
          grid: { left: 50, right: 20, bottom: 50, top: 35 },
        }, '/propertyPenetration/transaction')
      })

      // chart4：合规风险分布（独立 nextTick，与 chart2 分开）
      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}笔 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{
            type: 'pie', radius: ['35%', '65%'],
            data: complianceData.length ? complianceData : [{ name: '暂无数据', value: 1 }],
            label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
            color: ['#FF4D4F', '#FA8C16', '#52C41A', '#409EFF'],
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
          }],
        }, '/propertyPenetration/tradeReview')
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
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(39,174,96,0.2); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #27AE60; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
