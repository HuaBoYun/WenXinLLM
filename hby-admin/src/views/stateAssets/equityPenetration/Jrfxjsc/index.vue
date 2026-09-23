<template>
  <div class="fin-page">
    <div class="page-header mb-16">
      <div class="page-header-left"><i class="el-icon-odometer"></i>金融风险驾驶舱</div>
      <div class="page-header-desc">金融风险综合监控大屏，实时掌握融资、担保、风险全貌</div>
    </div>
    <el-row :gutter="16" class="mb-16">
      <el-col :span="5" v-for="(item, idx) in kpiList" :key="idx">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-value">{{ item.value }}</div>
          <div class="kpi-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12"><el-card shadow="hover"><div ref="trendChart" style="height:320px"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="hover"><div ref="riskChart" style="height:320px"></div></el-card></el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :span="12"><el-card shadow="hover"><div ref="guaranteeChart" style="height:320px"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="hover"><div ref="maturityChart" style="height:320px"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getFinancialRiskDashboard } from '@/api/stateAssets/financialRiskPenetration'

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
  name: 'Jrfxjsc',
  data() {
    return {
      kpiList: [
        { label: '融资总额(亿)', value: '--' },
        { label: '担保总额(亿)', value: '--' },
        { label: '高风险企业', value: '--' },
        { label: '活跃预警', value: '--' },
        { label: '逾期委贷', value: '--' }
      ],
      charts: []
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c && c.dispose())
  },
  methods: {
    async fetchData() {
      try {
        const res = await getFinancialRiskDashboard()
        if (res.code === 1 && res.data) {
          const { kpi, trendData, riskOverview, guaranteeChain, maturityDistribution } = res.data
          this.kpiList[0].value = kpi.totalFinancing
          this.kpiList[1].value = kpi.totalGuarantee
          this.kpiList[2].value = kpi.highRiskCount
          this.kpiList[3].value = kpi.activeAlertCount
          this.kpiList[4].value = kpi.overdueEntrustedLoan
          this.$nextTick(() => {
            this.renderTrend(trendData)
            this.renderRisk(riskOverview)
            this.renderGuarantee(guaranteeChain)
            this.renderMaturity(maturityDistribution)
          })
        }
      } catch (e) { this.$message.error('数据加载失败') }
    },
    initChart(ref) {
      const chart = echarts.init(this.$refs[ref])
      this.charts.push(chart)
      return chart
    },
    renderTrend(d) {
      const chart = this.initChart('trendChart')
      chart.setOption({
        title: { text: '融资规模趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' }, legend: { bottom: 0 },
        xAxis: { type: 'category', data: d.months },
        yAxis: { type: 'value' },
        series: [
          { name: '融资规模', type: 'line', data: d.financingScale, smooth: true, itemStyle: { color: '#1677FF' } },
          { name: '担保余额', type: 'line', data: d.guaranteeBalance, smooth: true, itemStyle: { color: '#0050A0' } },
          { name: '风险敞口', type: 'line', data: d.riskExposure, smooth: true, itemStyle: { color: '#F5222D' } }
        ]
      })
    },
    renderRisk(d) {
      const chart = this.initChart('riskChart')
      chart.setOption({
        title: { text: '风险等级分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item' }, legend: { bottom: 0 },
        series: [{ type: 'pie', radius: ['40%', '65%'], data: d, itemStyle: { borderRadius: 4 } }]
      })
    },
    renderGuarantee(d) {
      const chart = this.initChart('guaranteeChart')
      chart.setOption({
        title: { text: '担保集中度TOP8', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' }, grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: d.companies },
        series: [{ type: 'bar', data: d.amounts, itemStyle: { color: '#0050A0', borderRadius: [0, 4, 4, 0] } }]
      })
    },
    renderMaturity(d) {
      const chart = this.initChart('maturityChart')
      chart.setOption({
        title: { text: '到期融资分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' }, legend: { bottom: 0 },
        xAxis: { type: 'category', data: d.periods },
        yAxis: [{ type: 'value', name: '金额' }, { type: 'value', name: '累计%', max: 100 }],
        series: [
          { name: '到期金额', type: 'bar', data: d.amounts, itemStyle: { color: '#003A6C', borderRadius: [4, 4, 0, 0] } },
          { name: '累计占比', type: 'line', yAxisIndex: 1, data: d.cumulative, itemStyle: { color: '#F5222D' } }
        ]
      })
    },
    handleResize() { this.charts.forEach(c => c && c.resize()) }
  }
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.page-header { border-radius: 8px; padding: 16px 24px; color: #fff; }
.page-header-left { display: flex; align-items: center; gap: 10px; font-size: 18px; font-weight: 700; i { font-size: 24px; } }
.page-header-desc { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 4px; }
.kpi-card { text-align: center; .kpi-value { font-size: 24px; font-weight: 700; color: #003A6C; } .kpi-label { font-size: 13px; color: #666; margin-top: 4px; } }
</style>
