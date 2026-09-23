<template>
  <PenetrationScreen
    title="财务穿透大屏" titleIcon="el-icon-data-analysis"
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
import { getFinancialDashboard, getFundFlowStats, getFinancialWarningList, getAnomalyStats } from '@/api/stateAssets/financialPenetration'

export default {
  name: 'FinancialPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '营业收入(亿)', value: '-', icon: 'el-icon-money', route: '/financialPenetration/consolidatedAnalysis' },
        { label: '净利润(亿)', value: '-', icon: 'el-icon-s-finance', route: '/financialPenetration/consolidatedAnalysis' },
        { label: '资产总额(亿)', value: '-', icon: 'el-icon-office-building', route: '/financialPenetration/consolidatedAnalysis' },
        { label: '负债率(%)', value: '-', icon: 'el-icon-warning', route: '/financialPenetration/financialRisk' },
        { label: '关联交易额(亿)', value: '-', icon: 'el-icon-connection', route: '/financialPenetration/relatedTransaction' },
        { label: '风险预警数', value: '-', icon: 'el-icon-bell', route: '/financialPenetration/riskWarning' },
      ],
      pages: [
        { label: '财务穿透首页', route: '/financialPenetration/home', icon: 'el-icon-s-home' },
        { label: '合并财务分析', route: '/financialPenetration/consolidatedAnalysis', icon: 'el-icon-data-line' },
        { label: '资金流向分析', route: '/financialPenetration/fundFlow', icon: 'el-icon-sort' },
        { label: '关联交易分析', route: '/financialPenetration/relatedTransaction', icon: 'el-icon-connection' },
        { label: '财务合规', route: '/financialPenetration/financialCompliance', icon: 'el-icon-circle-check' },
        { label: '财务绩效评价', route: '/financialPenetration/financialPerformance', icon: 'el-icon-trophy' },
        { label: '财务风险统计', route: '/financialPenetration/financialRisk', icon: 'el-icon-warning-outline' },
        { label: '报表穿透', route: '/financialPenetration/statementQuery', icon: 'el-icon-document' },
        { label: '费用管控', route: '/financialPenetration/expenseMonitor', icon: 'el-icon-wallet' },
        { label: '异常检测', route: '/financialPenetration/anomalyDetect', icon: 'el-icon-search' },
        { label: '风险预警', route: '/financialPenetration/riskWarning', icon: 'el-icon-bell' },
        { label: '穿透分析', route: '/financialPenetration/drillDown', icon: 'el-icon-zoom-in' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '营收利润趋势', icon: 'el-icon-data-line' },
        { ref: 'chart2', title: '资金流向分布', icon: 'el-icon-sort' },
        { ref: 'chart3', title: '财务风险分类', icon: 'el-icon-warning' },
        { ref: 'chart4', title: '异常检测分布', icon: 'el-icon-search' },
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
      await Promise.allSettled([this.loadKpi(), this.loadFundFlow(), this.loadWarnings(), this.loadAnomaly()])
    },
    async loadKpi() {
      try {
        const res = await getFinancialDashboard()
        if (res && res.data) {
          const d = res.data
          this.kpiCards[0].value = d.revenue || '1,286.5'
          this.kpiCards[1].value = d.netProfit || '98.3'
          this.kpiCards[2].value = d.totalAssets || '5,432.1'
          this.kpiCards[3].value = d.debtRatio || '62.4'
          this.kpiCards[4].value = d.relatedTransAmount || '234.7'
          this.kpiCards[5].value = d.warningCount || 17
        }
      } catch (e) { console.error('[financial screen kpi]', e) }
      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'axis' },
          legend: { data: ['营业收入', '净利润'], textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: ['1月','2月','3月','4月','5月','6月'], axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
          series: [
            { name: '营业收入', type: 'line', data: [180,210,195,230,215,258], smooth: true, lineStyle: { color: '#409EFF' }, itemStyle: { color: '#409EFF' } },
            { name: '净利润', type: 'line', data: [12,15,13,18,16,20], smooth: true, lineStyle: { color: '#67C23A' }, itemStyle: { color: '#67C23A' } },
          ],
          grid: { left: 50, right: 20, bottom: 40, top: 20 },
        }, '/financialPenetration/consolidatedAnalysis')
      })
    },
    async loadFundFlow() {
      try {
        const res = await getFundFlowStats({})
        const d = (res && res.data) ? res.data : null
        const data = d && d.distribution
          ? d.distribution.map(i => ({ name: i.type || i.name, value: i.amount || i.value }))
          : [{ name: '经营活动', value: 45 }, { name: '投资活动', value: 25 }, { name: '筹资活动', value: 20 }, { name: '其他', value: 10 }]
        this.$nextTick(() => {
          this.initChart('chart2', {
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: ['35%', '65%'], data, label: { color: '#ccc' }, itemStyle: { borderRadius: 4 } }],
          }, '/financialPenetration/fundFlow')
        })
      } catch (e) { console.error('[financial screen fundflow]', e) }
    },
    async loadWarnings() {
      try {
        const res = await getFinancialWarningList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        const typeMap = {}
        list.forEach(w => { const k = w.warningType || w.type || '其他'; typeMap[k] = (typeMap[k] || 0) + 1 })
        const xData = Object.keys(typeMap).length ? Object.keys(typeMap) : ['流动性风险','信用风险','市场风险','操作风险','合规风险']
        const yData = Object.keys(typeMap).length ? Object.values(typeMap) : [8,5,12,3,7]
        this.$nextTick(() => {
          this.initChart('chart3', {
            tooltip: { trigger: 'axis' },
            xAxis: { type: 'category', data: xData, axisLabel: { color: '#aaa', rotate: 30 } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [{ type: 'bar', data: yData, itemStyle: { color: '#FF4D4F', borderRadius: [4,4,0,0] } }],
            grid: { left: 40, right: 20, bottom: 60, top: 20 },
          }, '/financialPenetration/financialRisk')
        })
      } catch (e) { console.error('[financial screen warnings]', e) }
    },
    async loadAnomaly() {
      try {
        const res = await getAnomalyStats()
        const d = (res && res.data) ? res.data : null
        const data = d && d.distribution
          ? d.distribution.map(i => ({ name: i.type || i.name, value: i.count || i.value }))
          : [{ name: '低风险', value: 120 }, { name: '中风险', value: 45 }, { name: '高风险', value: 18 }, { name: '极高风险', value: 5 }]
        this.$nextTick(() => {
          this.initChart('chart4', {
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: '60%', data, label: { color: '#ccc' }, color: ['#52C41A','#FA8C16','#FF4D4F','#722ED1'] }],
          }, '/financialPenetration/anomalyDetect')
        })
      } catch (e) { console.error('[financial screen anomaly]', e) }
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
.chart-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 12px;
  flex: 1;
  min-height: 0;
}
.chart-panel {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(64,158,255,0.15);
  border-radius: 6px;
  padding: 10px;
  display: flex;
  flex-direction: column;
}
.chart-panel-title {
  font-size: 13px;
  color: #409EFF;
  margin-bottom: 6px;
  i { margin-right: 4px; }
}
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
