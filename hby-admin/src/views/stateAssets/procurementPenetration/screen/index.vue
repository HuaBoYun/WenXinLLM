<template>
  <PenetrationScreen
    title="采购穿透大屏" titleIcon="el-icon-shopping-cart-full"
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
import { getProcurementKPI, getProcurementWarnings, getSupplyChainRiskStatistics, getBiddingComplianceStatistics } from '@/api/stateAssets/procurementPenetration'

export default {
  name: 'ProcurementPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '采购项目总数', value: '-', icon: 'el-icon-s-order', route: '/procurementPenetration/project' },
        { label: '供应商总数', value: '-', icon: 'el-icon-office-building', route: '/procurementPenetration/supplier' },
        { label: '招标项目数', value: '-', icon: 'el-icon-tickets', route: '/procurementPenetration/biddingMonitor' },
        { label: '采购总额(亿)', value: '-', icon: 'el-icon-shopping-cart-full', route: '/procurementPenetration/purchaseRecord' },
        { label: '风险供应商数', value: '-', icon: 'el-icon-warning', route: '/procurementPenetration/supplyChainRisk' },
        { label: '合规问题数', value: '-', icon: 'el-icon-s-flag', route: '/procurementPenetration/biddingCompliance' },
      ],
      pages: [
        { label: '采购穿透首页', route: '/procurementPenetration/home', icon: 'el-icon-s-home' },
        { label: '采购项目台账', route: '/procurementPenetration/project', icon: 'el-icon-document' },
        { label: '供应商台账', route: '/procurementPenetration/supplier', icon: 'el-icon-office-building' },
        { label: '招标过程监控', route: '/procurementPenetration/biddingMonitor', icon: 'el-icon-view' },
        { label: '采购价格对标', route: '/procurementPenetration/priceBenchmark', icon: 'el-icon-data-line' },
        { label: '供应链风险分析', route: '/procurementPenetration/supplyChainRisk', icon: 'el-icon-warning-outline' },
        { label: '采购监控驾驶舱', route: '/procurementPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '虚假贸易核查', route: '/procurementPenetration/fakeTrade', icon: 'el-icon-search' },
        { label: '采购台账', route: '/procurementPenetration/purchaseRecord', icon: 'el-icon-notebook-1' },
        { label: '招投标合规', route: '/procurementPenetration/biddingCompliance', icon: 'el-icon-s-check' },
        { label: '合同履约追踪', route: '/procurementPenetration/contractExecution', icon: 'el-icon-aim' },
        { label: '采购风险穿透', route: '/procurementPenetration/drillDown', icon: 'el-icon-zoom-in' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '采购金额分类分布', icon: 'el-icon-pie-chart' },
        { ref: 'chart2', title: '供应商风险等级分布', icon: 'el-icon-warning' },
        { ref: 'chart3', title: '采购价格对标趋势', icon: 'el-icon-data-line' },
        { ref: 'chart4', title: '招标合规情况', icon: 'el-icon-s-check' },
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
      await Promise.allSettled([this.loadKpi(), this.loadSupplyRisk(), this.loadBidding()])
      this.loadPriceTrend()
    },
    async loadKpi() {
      try {
        const res = await getProcurementKPI()
        if (res && res.data) {
          const d = res.data
          this.kpiCards[0].value = d.projectCount || 256
          this.kpiCards[1].value = d.supplierCount || 312
          this.kpiCards[2].value = d.biddingCount || 89
          this.kpiCards[3].value = d.totalAmount || '34.7'
          this.kpiCards[4].value = d.riskSupplierCount || 12
          this.kpiCards[5].value = d.complianceIssueCount || 6
        }
      } catch (e) { console.error('[procurement screen kpi]', e) }
      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'item' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{ type: 'pie', radius: ['35%','65%'],
            data: [{ name:'设备采购', value:38 },{ name:'材料采购', value:45 },{ name:'服务采购', value:28 },{ name:'工程采购', value:19 },{ name:'其他', value:8 }],
            label: { color: '#ccc' }, itemStyle: { borderRadius: 4 } }],
        }, '/procurementPenetration/project')
      })
    },
    async loadSupplyRisk() {
      try {
        const res = await getSupplyChainRiskStatistics()
        const d = (res && res.data) ? res.data : null
        const data = d && d.riskDistribution
          ? d.riskDistribution.map(i => i.count)
          : [198, 82, 23, 9]
        this.$nextTick(() => {
          this.initChart('chart2', {
            tooltip: { trigger: 'axis' },
            xAxis: { type: 'category', data: ['低风险','中风险','高风险','极高风险'], axisLabel: { color: '#aaa' } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
            series: [{ type: 'bar', data,
              itemStyle: { color: (p) => ['#52C41A','#FA8C16','#FF4D4F','#722ED1'][p.dataIndex], borderRadius: [4,4,0,0] } }],
            grid: { left: 40, right: 20, bottom: 30, top: 20 },
          }, '/procurementPenetration/supplyChainRisk')
        })
      } catch (e) { console.error('[procurement screen supply]', e) }
    },
    async loadBidding() {
      try {
        const res = await getBiddingComplianceStatistics()
        const d = (res && res.data) ? res.data : null
        const data = d
          ? [{ name:'合规', value: d.compliantCount || 76 }, { name:'轻微违规', value: d.minorViolationCount || 10 }, { name:'严重违规', value: d.seriousViolationCount || 3 }]
          : [{ name:'合规', value:76 }, { name:'轻微违规', value:10 }, { name:'严重违规', value:3 }]
        this.$nextTick(() => {
          this.initChart('chart4', {
            tooltip: { trigger: 'item' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: ['40%','65%'], data, label: { color: '#ccc' }, color: ['#52C41A','#FA8C16','#FF4D4F'] }],
          }, '/procurementPenetration/biddingCompliance')
        })
      } catch (e) { console.error('[procurement screen bidding]', e) }
    },
    loadPriceTrend() {
      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'axis' },
          legend: { data: ['采购价格','市场基准价'], textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: ['Q1','Q2','Q3','Q4','Q1','Q2'], axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' } },
          series: [
            { name:'采购价格', type:'line', data:[100,103,98,102,105,101], smooth:true, lineStyle:{color:'#409EFF'}, itemStyle:{color:'#409EFF'} },
            { name:'市场基准价', type:'line', data:[100,100,100,100,100,100], smooth:true, lineStyle:{color:'#aaa',type:'dashed'}, itemStyle:{color:'#aaa'} },
          ],
          grid: { left: 50, right: 20, bottom: 40, top: 20 },
        }, '/procurementPenetration/priceBenchmark')
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
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(64,158,255,0.15); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #52C41A; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
