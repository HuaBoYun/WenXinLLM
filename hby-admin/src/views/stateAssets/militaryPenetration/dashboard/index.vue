<template>
  <div class="military-dashboard" :style="themeVars">
    <div class="page-header">
      <h2><i class="el-icon-s-flag"></i> 军品驾驶舱</h2>
      <p>集团军工科研生产全链条穿透式监控</p>
    </div>
    <el-row :gutter="16" class="stat-row">
      <el-col :span="4" v-for="c in statCards" :key="c.label">
        <div class="stat-card" :style="{ borderLeft: '3px solid ' + c.color }">
          <div class="stat-val" :style="{ color: c.color }">{{ c.value }}</div>
          <div class="stat-label">{{ c.label }}</div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14"><el-card shadow="never"><div slot="header">军品任务交付趋势</div><div ref="trendChart" class="chart-box"></div></el-card></el-col>
      <el-col :span="10"><el-card shadow="never"><div slot="header">军品任务类型分布</div><div ref="typeChart" class="chart-box"></div></el-card></el-col>
    </el-row>
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10"><el-card shadow="never"><div slot="header">供应链境外依赖比</div><div ref="gaugeChart" class="chart-box"></div></el-card></el-col>
      <el-col :span="14"><el-card shadow="never"><div slot="header">密级分布</div><div ref="secretChart" class="chart-box"></div></el-card></el-col>
    </el-row>
    <!-- 预警清单 -->
    <el-card shadow="never" class="warn-card">
      <div slot="header"><span>军品预警清单</span></div>
      <el-table :data="warningList" border size="small" style="width:100%">
        <el-table-column label="预警编号" prop="alertId" width="110" />
        <el-table-column label="风险等级" prop="level" width="90" align="center">
          <template slot-scope="{row}"><el-tag :type="row.level==='HIGH'?'danger':row.level==='MEDIUM'?'warning':'info'" size="mini">{{ row.level==='HIGH'?'高风险':row.level==='MEDIUM'?'中风险':'低风险' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="预警内容" prop="alertContent" min-width="260" show-overflow-tooltip />
        <el-table-column label="关联企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="触发时间" prop="createTime" width="140" align="center" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="row.status==='PENDING'?'danger':'success'" size="mini" effect="plain">{{ row.status==='PENDING'?'待处理':'已处理' }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getMilitaryDashboard, getMilitaryDashboardTypeData, getMilitaryDashboardGaugeData, getMilitaryDashboardSecretData, getMilitaryAlertList } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'MilitaryDashboard',
  mixins: [investThemeMixin],
  data() {
    return {
      charts: [],
      statCards: [
        { label: '军品任务总数', value: '-', color: '#0050A0' },
        { label: '有效资质数', value: '-', color: '#237804' },
        { label: '境外依赖比', value: '-', color: '#722ED1' },
        { label: '资质合规率', value: '-', color: '#52C41A' },
        { label: '高风险预警', value: '-', color: '#CF1322' },
        { label: '合同总数', value: '-', color: '#FF7A45' },
      ],
      warningList: [],
    }
  },
  created() {
    this.statCards[0].color = this.ipSecondary
  },
  mounted() {
    this.$nextTick(() => this.loadAll())
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async loadAll() {
      await Promise.all([this.loadOverview(), this.loadWarnings()])
      this.initTrend(); this.initType(); this.initGauge(); this.initSecret()
    },
    async loadOverview() {
      try {
        const res = await getMilitaryDashboard()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.statCards[0].value = String(d.totalTasks || 0)
          this.statCards[1].value = String(d.totalQualifications || 0)
          this.statCards[2].value = '-'
          this.statCards[3].value = '-'
          this.statCards[4].value = String(d.highRiskAlerts || 0)
          this.statCards[5].value = String(d.totalContracts || 0)
        }
      } catch (e) { /* ignore */ }
    },
    async loadWarnings() {
      try {
        const res = await getMilitaryAlertList({ pageNumber: 1, pageSize: 10, status: 'PENDING' })
        if (res && res.result === 200) this.warningList = (res.data && res.data.tlist) || []
      } catch (e) { this.warningList = [] }
    },
    async initTrend() {
      const c = echarts.init(this.$refs.trendChart); this.charts.push(c)
      try {
        const res = await getMilitaryDashboardTypeData()
        const data = (res && res.result === 200 && res.data) ? res.data : []
        const names = data.map(d => d.name || d.type || '')
        const values = data.map(d => d.value || d.count || 0)
        c.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 50, right: 40, bottom: 30, top: 40 },
          xAxis: { type: 'category', data: names.length ? names : ['科研', '生产', '技改', '维修'] },
          yAxis: { type: 'value', name: '数量' },
          series: [{ type: 'bar', data: values.length ? values : [0, 0, 0, 0], itemStyle: { color: this.ipSecondary } }] })
      } catch (e) {
        c.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 50, right: 40, bottom: 30, top: 40 },
          xAxis: { type: 'category', data: ['科研', '生产', '技改', '维修'] }, yAxis: { type: 'value' },
          series: [{ type: 'bar', data: [0, 0, 0, 0], itemStyle: { color: this.ipSecondary } }] })
      }
    },
    async initType() {
      const c = echarts.init(this.$refs.typeChart); this.charts.push(c)
      try {
        const res = await getMilitaryDashboardTypeData()
        const data = (res && res.result === 200 && res.data) ? res.data : []
        c.setOption({ tooltip: { trigger: 'item' }, legend: { bottom: 0 },
          series: [{ type: 'pie', radius: ['40%', '70%'], center: ['50%', '45%'], label: { formatter: '{b}\n{d}%' },
            data: data.length ? data.map((d, i) => ({ value: d.value || d.count || 0, name: d.name || d.type || '其他', itemStyle: { color: [this.ipSecondary, '#237804', this.ipBright, '#FAAD14', '#D9D9D9'][i % 5] } })) : [{ value: 0, name: '暂无数据' }] }] })
      } catch (e) {
        c.setOption({ series: [{ type: 'pie', radius: ['40%', '70%'], data: [{ value: 0, name: '暂无数据' }] }] })
      }
    },
    async initGauge() {
      const c = echarts.init(this.$refs.gaugeChart); this.charts.push(c)
      try {
        const res = await getMilitaryDashboardGaugeData()
        const val = (res && res.result === 200 && res.data) ? (res.data.value || 0) : 0
        c.setOption({ series: [{ type: 'gauge', startAngle: 200, endAngle: -20, min: 0, max: 60,
          axisLine: { lineStyle: { width: 12, color: [[0.25, '#52C41A'], [0.5, '#FA8C16'], [1, '#CF1322']] } },
          pointer: { itemStyle: { color: this.ipSecondary } }, axisTick: { show: false }, splitLine: { length: 10, lineStyle: { color: '#fff' } },
          axisLabel: { color: '#8C8C8C', fontSize: 11, formatter: '{value}%' },
          detail: { formatter: '{value}%', fontSize: 18, color: this.ipSecondary, offsetCenter: [0, '60%'] },
          data: [{ value: val, name: '集团境外依赖比' }] }] })
      } catch (e) {
        c.setOption({ series: [{ type: 'gauge', data: [{ value: 0, name: '集团境外依赖比' }] }] })
      }
    },
    async initSecret() {
      const c = echarts.init(this.$refs.secretChart); this.charts.push(c)
      try {
        const res = await getMilitaryDashboardSecretData()
        const data = (res && res.result === 200 && res.data) ? res.data : []
        const companies = data.map(d => d.company || '')
        const topSecret = data.map(d => (d.levels && d.levels.TOP_SECRET) || 0)
        const secret = data.map(d => (d.levels && d.levels.SECRET) || 0)
        const confidential = data.map(d => (d.levels && d.levels.CONFIDENTIAL) || 0)
        const internal = data.map(d => (d.levels && d.levels.INTERNAL) || 0)
        c.setOption({ tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } }, legend: { data: ['绝密', '机密', '秘密', '内部'] },
          grid: { left: 120, right: 30, bottom: 30, top: 40 },
          yAxis: { type: 'category', data: companies.length ? companies : ['暂无数据'] },
          xAxis: { type: 'value', name: '任务数' },
          series: [{ name: '绝密', type: 'bar', stack: 'total', data: topSecret, itemStyle: { color: '#CF1322' } }, { name: '机密', type: 'bar', stack: 'total', data: secret, itemStyle: { color: '#FA8C16' } }, { name: '秘密', type: 'bar', stack: 'total', data: confidential, itemStyle: { color: '#FAAD14' } }, { name: '内部', type: 'bar', stack: 'total', data: internal, itemStyle: { color: '#52C41A' } }] })
      } catch (e) {
        c.setOption({ series: [{ type: 'bar', data: [] }] })
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.military-dashboard { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 16px; padding: 18px 24px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
  h2 { font-size: 20px; margin: 0 0 4px; i { margin-right: 8px; } }
  p { font-size: 13px; opacity: 0.85; margin: 0; }
}
.stat-row { margin-bottom: 16px; }
.stat-card {
  background: #fff; border-radius: 6px; padding: 16px 14px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  .stat-val { font-size: 22px; font-weight: 700; line-height: 1.2; }
  .stat-label { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-box { height: 320px; width: 100%; }
::v-deep .el-card { border-radius: 6px; }
.warn-card { margin-bottom: 16px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
</style>
