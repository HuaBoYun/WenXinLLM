<template>
  <PenetrationScreen
    title="军品穿透大屏" titleIcon="el-icon-s-flag"
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
import { getMilitaryKPI, getMilitaryDashboard, getExpiringQualifications, getSupplyChainStatistics } from '@/api/stateAssets/militaryPenetration'

export default {
  name: 'MilitaryPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '任务总数', value: '-', icon: 'el-icon-s-order', route: '/militaryPenetration/task' },
        { label: '在执行任务数', value: '-', icon: 'el-icon-loading', route: '/militaryPenetration/task' },
        { label: '资质证书数', value: '-', icon: 'el-icon-medal', route: '/militaryPenetration/qualification' },
        { label: '保密事项数', value: '-', icon: 'el-icon-lock', route: '/militaryPenetration/securityMgmt' },
        { label: '质量问题数', value: '-', icon: 'el-icon-warning', route: '/militaryPenetration/qualityMgmt' },
        { label: '合规风险数', value: '-', icon: 'el-icon-s-flag', route: '/militaryPenetration/drillDown' },
      ],
      pages: [
        { label: '军品穿透首页', route: '/militaryPenetration/home', icon: 'el-icon-s-home' },
        { label: '军品任务台账', route: '/militaryPenetration/task', icon: 'el-icon-document' },
        { label: '军品资质管理', route: '/militaryPenetration/qualification', icon: 'el-icon-medal' },
        { label: '军品保密管理', route: '/militaryPenetration/securityMgmt', icon: 'el-icon-lock' },
        { label: '军品质量管理', route: '/militaryPenetration/qualityMgmt', icon: 'el-icon-s-check' },
        { label: '军品资产管理', route: '/militaryPenetration/assetMgmt', icon: 'el-icon-office-building' },
        { label: '监控驾驶舱', route: '/militaryPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '供应链安全', route: '/militaryPenetration/supplyChainSecurity', icon: 'el-icon-connection' },
        { label: '分包合规', route: '/militaryPenetration/subcontractCompliance', icon: 'el-icon-s-check' },
        { label: '合同履约追踪', route: '/militaryPenetration/contractExecution', icon: 'el-icon-aim' },
        { label: '军品风险穿透', route: '/militaryPenetration/drillDown', icon: 'el-icon-zoom-in' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '军品任务完成情况', icon: 'el-icon-s-order' },
        { ref: 'chart2', title: '资质到期预警', icon: 'el-icon-medal' },
        { ref: 'chart3', title: '质量问题分类', icon: 'el-icon-warning' },
        { ref: 'chart4', title: '供应链安全评级', icon: 'el-icon-connection' },
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
      await Promise.allSettled([this.loadKpi(), this.loadQualification(), this.loadSupplyChain()])
      this.loadQualityChart()
    },
    async loadKpi() {
      try {
        const res = await getMilitaryKPI()
        if (res && res.data) {
          const d = res.data
          this.kpiCards[0].value = d.totalTasks || 86
          this.kpiCards[1].value = d.executingTasks || 34
          this.kpiCards[2].value = d.qualificationCount || 28
          this.kpiCards[3].value = d.secretCount || 156
          this.kpiCards[4].value = d.qualityIssueCount || 9
          this.kpiCards[5].value = d.complianceRiskCount || 4
        }
      } catch (e) { console.error('[military screen kpi]', e) }
      try {
        const res = await getMilitaryDashboard()
        const d = (res && res.data) ? res.data : null
        const planned = d && d.taskTrend ? d.taskTrend.map(i => i.planned) : [22,25,20,19]
        const completed = d && d.taskTrend ? d.taskTrend.map(i => i.completed) : [20,24,18,24]
        this.$nextTick(() => {
          this.initChart('chart1', {
            tooltip: { trigger: 'axis' },
            legend: { data: ['计划','完成'], textStyle: { color: '#aaa' }, bottom: 0 },
            xAxis: { type: 'category', data: ['Q1','Q2','Q3','Q4'], axisLabel: { color: '#aaa' } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [
              { name:'计划', type:'bar', data: planned, barGap: 0, itemStyle: { color: '#409EFF', borderRadius: [4,4,0,0] } },
              { name:'完成', type:'bar', data: completed, itemStyle: { color: '#67C23A', borderRadius: [4,4,0,0] } },
            ],
            grid: { left: 40, right: 20, bottom: 40, top: 20 },
          }, '/militaryPenetration/task')
        })
      } catch (e) { console.error('[military screen task]', e) }
    },
    async loadQualification() {
      try {
        const res = await getExpiringQualifications(365)
        const d = (res && res.data) ? res.data : null
        const data = d && d.distribution
          ? d.distribution.map(i => i.count)
          : [2,3,5,8,10]
        this.$nextTick(() => {
          this.initChart('chart2', {
            tooltip: { trigger: 'axis' },
            xAxis: { type: 'category', data: ['30天内','60天内','90天内','180天内','1年内'], axisLabel: { color: '#aaa' } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [{ type: 'line', data, smooth: true, lineStyle: { color: '#FF4D4F' }, itemStyle: { color: '#FF4D4F' }, areaStyle: { opacity: 0.2 } }],
            grid: { left: 40, right: 20, bottom: 30, top: 20 },
          }, '/militaryPenetration/qualification')
        })
      } catch (e) { console.error('[military screen qualification]', e) }
    },
    async loadSupplyChain() {
      try {
        const res = await getSupplyChainStatistics()
        const d = (res && res.data) ? res.data : null
        const data = d && d.ratingDistribution
          ? d.ratingDistribution.map(i => i.count)
          : [12,18,8,4]
        this.$nextTick(() => {
          this.initChart('chart4', {
            tooltip: { trigger: 'axis' },
            xAxis: { type: 'category', data: ['A级','B级','C级','D级'], axisLabel: { color: '#aaa' } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [{ type: 'bar', data,
              itemStyle: { color: (p) => ['#52C41A','#409EFF','#FA8C16','#FF4D4F'][p.dataIndex], borderRadius: [4,4,0,0] } }],
            grid: { left: 40, right: 20, bottom: 30, top: 20 },
          }, '/militaryPenetration/supplyChainSecurity')
        })
      } catch (e) { console.error('[military screen supply]', e) }
    },
    loadQualityChart() {
      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'item' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{ type: 'pie', radius: '60%',
            data: [{ name:'原材料问题', value:3 },{ name:'工艺问题', value:2 },{ name:'检验问题', value:2 },{ name:'文件问题', value:2 }],
            label: { color: '#ccc' } }],
        }, '/militaryPenetration/qualityMgmt')
      })
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
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(245,34,45,0.2); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #FF4D4F; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
