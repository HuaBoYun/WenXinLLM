<template>
  <div class="overseas-dashboard" :style="themeVars">
    <div class="dash-header">
      <div class="dash-header-left"><i class="el-icon-place"></i><span>境外监控驾驶舱</span></div>
      <div class="dash-header-desc">境外投资与经营全球化穿透式监控，实时风险预警看板</div>
    </div>
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '16px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }"><i :class="card.icon" :style="{ color: card.color }"></i></div>
            <div class="stat-info">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-change" :style="{ color: card.trend > 0 ? '#cf1322' : '#389e0d' }">{{ card.trend > 0 ? '↑' : '↓' }} {{ Math.abs(card.trend) }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="hover"><div slot="header"><span>境外投资趋势</span></div><div ref="trendChart" class="chart-box"></div></el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover"><div slot="header"><span>国别分布</span></div><div ref="countryChart" class="chart-box"></div></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="hover"><div slot="header"><span>外汇敞口分布</span></div><div ref="forexChart" class="chart-box"></div></el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="hover"><div slot="header"><span>国别风险评级</span></div><div ref="riskChart" class="chart-box"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getOverseasDashboard, getOverseasDashboardTrend, getOverseasDashboardCountry, getOverseasDashboardForex, getOverseasDashboardRisk } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasDashboard',
  mixins: [investThemeMixin],
  data() {
    return {
      charts: [],
      statCards: [
        { label: '境外企业数', value: '-', icon: 'el-icon-office-building', color: '#0050A0', bg: '#EBF1FF', trend: 0 },
        { label: '涉及国家数', value: '-', icon: 'el-icon-location', color: '#1677FF', bg: '#EBF1FF', trend: 0 },
        { label: '投资总额(亿美元)', value: '-', icon: 'el-icon-money', color: '#FA8C16', bg: '#FFF7E6', trend: 0 },
        { label: '风险预警', value: '-', icon: 'el-icon-warning', color: '#F5222D', bg: '#FFF1F0', trend: 0 },
      ],
    }
  },
  created() {
    this.statCards[0].color = this.ipSecondary
    this.statCards[0].bg = this.ipLightBg
    this.statCards[1].color = this.ipBright
    this.statCards[1].bg = this.ipLightBg
  },
  mounted() { this.loadData(); window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async loadData() {
      try {
        const res = await getOverseasDashboard()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.companyCount != null) this.statCards[0].value = d.companyCount
          if (d.countryCount != null) this.statCards[1].value = d.countryCount
          if (d.investTotal != null) this.statCards[2].value = d.investTotal
          if (d.warningCount != null) this.statCards[3].value = d.warningCount
          if (d.companyTrend != null) this.statCards[0].trend = d.companyTrend
          if (d.countryTrend != null) this.statCards[1].trend = d.countryTrend
          if (d.investTrend != null) this.statCards[2].trend = d.investTrend
          if (d.warningTrend != null) this.statCards[3].trend = d.warningTrend
        }
      } catch (e) { console.warn('境外驾驶舱数据加载失败', e) }
      this.$nextTick(() => { this.initTrend(); this.initCountry(); this.initForex(); this.initRisk() })
    },
    async initTrend() {
      const c = echarts.init(this.$refs.trendChart); this.charts.push(c)
      try {
        const res = await getOverseasDashboardTrend()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          c.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['新增投资','累计投资','收益率'] }, grid: { left: 50, right: 50, bottom: 30, top: 40 },
            xAxis: { type: 'category', data: d.years || [] }, yAxis: [{ type: 'value', name: '金额(亿美元)' },{ type: 'value', name: '收益率(%)', max: 20 }],
            series: [
              { name: '新增投资', type: 'bar', data: d.newInvest || [], itemStyle: { color: this.ipPrimary } },
              { name: '累计投资', type: 'bar', data: d.totalInvest || [], itemStyle: { color: this.ipBright } },
              { name: '收益率', type: 'line', yAxisIndex: 1, data: d.returnRate || [], smooth: true, itemStyle: { color: '#FA8C16' } }
            ]
          })
          return
        }
      } catch (e) { console.warn('趋势图加载失败', e) }
      c.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['新增投资','累计投资','收益率'] }, grid: { left: 50, right: 50, bottom: 30, top: 40 },
        xAxis: { type: 'category', data: [] }, yAxis: [{ type: 'value', name: '金额(亿美元)' },{ type: 'value', name: '收益率(%)' }],
        series: [
          { name: '新增投资', type: 'bar', data: [] },
          { name: '累计投资', type: 'bar', data: [] },
          { name: '收益率', type: 'line', yAxisIndex: 1, data: [] }
        ]
      })
    },
    async initCountry() {
      const c = echarts.init(this.$refs.countryChart); this.charts.push(c)
      try {
        const res = await getOverseasDashboardCountry()
        if (res && res.result === 200 && res.data && res.data.length) {
          c.setOption({ tooltip: { trigger: 'item' }, legend: { bottom: 0 },
            series: [{ type: 'pie', radius: ['40%','70%'], center: ['50%','45%'], label: { formatter: '{b}\n{d}%' }, data: res.data }]
          })
          return
        }
      } catch (e) { console.warn('国别分布图加载失败', e) }
      c.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['40%','70%'], data: [] }] })
    },
    async initForex() {
      const c = echarts.init(this.$refs.forexChart); this.charts.push(c)
      try {
        const res = await getOverseasDashboardForex()
        if (res && res.result === 200 && res.data && res.data.length) {
          c.setOption({ tooltip: { trigger: 'item' }, legend: { bottom: 0 },
            series: [{ type: 'pie', radius: ['35%','65%'], center: ['50%','45%'], roseType: 'radius', label: { formatter: '{b}: {c}万' }, data: res.data }]
          })
          return
        }
      } catch (e) { console.warn('外汇敞口图加载失败', e) }
      c.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['35%','65%'], data: [] }] })
    },
    async initRisk() {
      const c = echarts.init(this.$refs.riskChart); this.charts.push(c)
      try {
        const res = await getOverseasDashboardRisk()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          c.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['政治风险','经济风险','汇率风险','合规风险'] }, grid: { left: 80, right: 20, bottom: 30, top: 40 },
            yAxis: { type: 'category', data: d.countries || [] },
            xAxis: { type: 'value', max: 100, name: '风险评分' },
            series: [
              { name: '政治风险', type: 'bar', stack: 'risk', data: d.political || [], itemStyle: { color: '#F5222D' } },
              { name: '经济风险', type: 'bar', stack: 'risk', data: d.economic || [], itemStyle: { color: '#FA8C16' } },
              { name: '汇率风险', type: 'bar', stack: 'risk', data: d.forex || [], itemStyle: { color: '#FADB14' } },
              { name: '合规风险', type: 'bar', stack: 'risk', data: d.compliance || [], itemStyle: { color: this.ipBright } },
            ]
          })
          return
        }
      } catch (e) { console.warn('风险评级图加载失败', e) }
      c.setOption({ tooltip: { trigger: 'axis' }, yAxis: { type: 'category', data: [] }, xAxis: { type: 'value' }, series: [] })
    },
  },
}
</script>
<style lang="scss" scoped>
.overseas-dashboard { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.dash-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
}
.dash-header-left { display: flex; align-items: center; font-size: 18px; font-weight: 600; i { font-size: 24px; margin-right: 10px; } }
.dash-header-desc { font-size: 13px; opacity: 0.85; }
.stat-row { margin-bottom: 16px; }
.chart-row { margin-bottom: 16px; }
.chart-box { height: 320px; width: 100%; }
::v-deep .el-card { border-radius: 6px; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; i { font-size: 24px; } }
.stat-info { flex: 1; }
.stat-value { font-size: 26px; font-weight: bold; color: #303133; line-height: 1; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.stat-change { font-size: 12px; margin-top: 2px; }
</style>