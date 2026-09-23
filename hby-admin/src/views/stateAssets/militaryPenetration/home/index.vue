<template>
  <div class="military-home" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2><i class="el-icon-aim"></i> 军品穿透监管平台</h2>
        <p>军工科研生产全链条穿透式监管 · 任务·资质·供应链·合规·交付</p>
      </div>
      <div class="header-right">
        <el-tag type="danger" effect="dark" size="small"><i class="el-icon-warning"></i> 活跃预警 {{ kpiCards[4].value }} 条</el-tag>
      </div>
    </div>
    <!-- 6 KPI -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="kpi in kpiCards" :key="kpi.label">
        <div class="kpi-card" :style="{ borderLeft: '3px solid ' + resolveColor(kpi) }">
          <div class="kpi-icon"><i :class="kpi.icon" :style="{ color: resolveColor(kpi) }"></i></div>
          <div class="kpi-body">
            <div class="kpi-val" :style="{ color: resolveColor(kpi) }">{{ kpi.value }}</div>
            <div class="kpi-label">{{ kpi.label }}</div>
          </div>
          <div class="kpi-trend" :style="{ color: kpi.trendColor }">{{ kpi.trend }}</div>
        </div>
      </el-col>
    </el-row>
    <!-- 穿透树 + 预警 -->
    <el-row :gutter="16" class="main-row">
      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header" class="card-header-row">
            <span>军品穿透链路图</span>
            <el-radio-group v-model="treeDepth" size="mini" @change="renderTree">
              <el-radio-button label="2">2层</el-radio-button>
              <el-radio-button label="3">3层</el-radio-button>
              <el-radio-button label="all">全量</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="treeChart" class="tree-chart"></div>
          <div class="legend-bar">
            <span class="legend-dot" :style="{background: ipPrimary}"></span><span>集团层</span>
            <span class="legend-dot" :style="{background: ipSecondary}"></span><span>一级子企业</span>
            <span class="legend-dot" :style="{background: ipBright}"></span><span>二级子企业</span>
            <span class="legend-dot" style="background:#CF1322"></span><span>高风险</span>
            <span class="legend-dot" style="background:#722ED1"></span><span>境外依赖超限</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" class="warn-card">
          <div slot="header" class="card-header-row"><span>军品活跃预警</span><el-tag size="mini" type="danger">{{ warnings.length }} 条</el-tag></div>
          <div class="warn-list">
            <div v-for="w in warnings" :key="w.alertId" class="warn-item" :class="'warn-' + (w.level || '').toLowerCase()">
              <el-tag :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ w.level }}</el-tag>
              <span class="warn-title" :title="w.alertContent">{{ w.alertContent }}</span>
              <span class="warn-company" :title="w.companyName">{{ w.companyName && w.companyName.length > 6 ? w.companyName.slice(0,6)+'...' : w.companyName }}</span>
              <span class="warn-time">{{ w.createTime ? String(w.createTime).slice(5, 10) : '' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 8 导航卡 -->
    <el-row :gutter="16" class="nav-row">
      <el-col :span="6" v-for="nav in navCards" :key="nav.title">
        <div class="nav-card" @click="goTo(nav.route)">
          <div class="nav-icon" :style="{ background: resolveColor(nav) }"><i :class="nav.icon"></i></div>
          <div class="nav-body">
            <div class="nav-title">{{ nav.title }}</div>
            <div class="nav-desc">{{ nav.desc }}</div>
          </div>
          <i class="el-icon-arrow-right nav-arrow"></i>
        </div>
      </el-col>
    </el-row>
    <!-- 军品风险地图 -->
    <el-card shadow="never" class="risk-map-card">
      <div slot="header"><span>军品风险热力地图</span><span style="font-size:12px;color:#8C8C8C;margin-left:12px">按企业级次展示资质与交付风险</span></div>
      <div ref="riskMapChart" class="risk-map-chart"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getMilitaryKPI, getMilitaryTree, getMilitaryWarnings, getMilitaryRiskMap } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

// getColor is now handled inside the component via mixin properties

export default {
  name: 'MilitaryHome',
  mixins: [investThemeMixin],
  data() {
    return {
      treeDepth: '2',
      treeChart: null,
      riskMapChart: null,
      treeData: null,
      kpiCards: [
        { label: '军品任务总数', value: '-', icon: 'el-icon-s-flag', colorKey: 'ipSecondary', trend: '', trendColor: '#389E0D' },
        { label: '有效资质数', value: '-', icon: 'el-icon-circle-check', color: '#237804', trend: '', trendColor: '#389E0D' },
        { label: '境外依赖比', value: '-', icon: 'el-icon-connection', color: '#722ED1', trend: '', trendColor: '#CF1322' },
        { label: '资质合规率', value: '-', icon: 'el-icon-trophy', color: '#52C41A', trend: '', trendColor: '#389E0D' },
        { label: '活跃预警', value: '-', icon: 'el-icon-warning', color: '#CF1322', trend: '', trendColor: '#CF1322' },
        { label: '合同总数', value: '-', icon: 'el-icon-document-checked', color: '#FA8C16', trend: '', trendColor: '#FA8C16' },
      ],
      warnings: [],
      treeData: null,
      navCards: [
        { title: '军品驾驶舱', desc: '集团军品综合大屏', icon: 'el-icon-data-analysis', colorKey: 'ipSecondary', route: '/riskPenetration/Jpjkjsc' },
        { title: '任务台账', desc: '科研生产任务查询', icon: 'el-icon-s-flag', color: '#237804', route: '/riskPenetration/Jprwtz' },
        { title: '资质档案', desc: '保密资质与生产许可', icon: 'el-icon-document', color: '#52C41A', route: '/riskPenetration/qualificationProfile' },
        { title: '供应链安全', desc: '境外依赖与单点风险', icon: 'el-icon-connection', color: '#722ED1', route: '/riskPenetration/supplyChainSecurity' },
        { title: '分包合规', desc: '分包审批与保密合规', icon: 'el-icon-document-checked', color: '#FAAD14', route: '/riskPenetration/subcontractCompliance' },
        { title: '合同履约追踪', desc: '军品合同全程监控', icon: 'el-icon-s-order', color: '#FA8C16', route: '/riskPenetration/contractExecution' },
        { title: '军品风险穿透', desc: '多层穿透下钻分析', icon: 'el-icon-share', color: '#CF1322', route: '/riskPenetration/drillDown' },
      ],
    }
  },
  mounted() {
    this.$nextTick(() => { this.loadData() })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.treeChart) { this.treeChart.dispose(); this.treeChart = null }
    if (this.riskMapChart) { this.riskMapChart.dispose(); this.riskMapChart = null }
  },
  methods: {
    handleResize() { if (this.treeChart) this.treeChart.resize(); if (this.riskMapChart) this.riskMapChart.resize() },
    async loadData() {
      await Promise.all([this.loadKPI(), this.loadWarnings(), this.loadTree(), this.loadRiskMap()])
    },
    async loadKPI() {
      try {
        const res = await getMilitaryKPI()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.kpiCards[0].value = String(d.totalTasks || 0)
          this.kpiCards[1].value = String(d.activeQualifications || 0)
          this.kpiCards[2].value = (d.foreignDependencyRatio || 0) + '%'
          this.kpiCards[3].value = (d.complianceRate || 0) + '%'
          this.kpiCards[4].value = String(d.activeAlerts || 0)
          this.kpiCards[5].value = String(d.totalContracts || 0)
        }
      } catch (e) { /* ignore */ }
    },
    async loadWarnings() {
      try {
        const res = await getMilitaryWarnings()
        if (res && res.result === 200) this.warnings = res.data || []
      } catch (e) { this.warnings = [] }
    },
    async loadTree() {
      try {
        const res = await getMilitaryTree()
        if (res && res.result === 200 && res.data) {
          this.treeData = res.data
          this.renderTree()
        }
      } catch (e) { this.renderTree() }
    },
    async loadRiskMap() {
      try {
        const res = await getMilitaryRiskMap()
        if (res && res.result === 200) this.renderRiskMap(res.data || [])
        else this.renderRiskMap([])
      } catch (e) { this.renderRiskMap([]) }
    },
    renderTree() {
      if (!this.treeData) return
      if (this.treeChart) this.treeChart.dispose()
      this.treeChart = echarts.init(this.$refs.treeChart)
      const nodes = Array.isArray(this.treeData) ? this.treeData : []
      const treeNode = {
        name: '集团总部', id: 'root',
        itemStyle: { color: this.ipPrimary },
        label: { color: '#fff', backgroundColor: this.ipPrimary, padding: [3, 7], borderRadius: 4, fontSize: 12 },
        children: nodes.map(n => ({
          name: n.name || n.companyName || '未知',
          itemStyle: { color: n.riskLevel === 'HIGH' ? '#CF1322' : this.ipSecondary },
          label: { color: '#fff', backgroundColor: n.riskLevel === 'HIGH' ? '#CF1322' : this.ipSecondary, padding: [3, 7], borderRadius: 4, fontSize: 12 },
          children: [],
        })),
      }
      this.treeChart.setOption({
        tooltip: { trigger: 'item', formatter: p => p.data ? `<b>${p.data.name}</b>` : '' },
        series: [{ type: 'tree', data: [treeNode], top: '5%', left: '12%', bottom: '5%', right: '22%',
          symbolSize: 9, orient: 'LR', label: { position: 'left', verticalAlign: 'middle', align: 'right', fontSize: 12 },
          leaves: { label: { position: 'right', align: 'left' } },
          lineStyle: { color: '#C0C4CC', width: 1.5 }, expandAndCollapse: true, animationDuration: 550 }],
      })
    },
    renderRiskMap(data) {
      if (this.riskMapChart) this.riskMapChart.dispose()
      this.riskMapChart = echarts.init(this.$refs.riskMapChart)
      const companies = data.map(d => d.company || d.companyName || '').filter(Boolean)
      const alertCounts = data.map(d => d.alertCount || 0)
      this.riskMapChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 120, right: 30, bottom: 30, top: 40 },
        xAxis: { type: 'value', name: '预警数' },
        yAxis: { type: 'category', data: companies },
        series: [{ type: 'bar', data: alertCounts.map((v, i) => ({ value: v, itemStyle: { color: v >= 3 ? '#CF1322' : v >= 1 ? '#FA8C16' : '#52C41A' } })), label: { show: true, position: 'right' } }],
      })
    },
    resolveColor(item) {
      if (item.colorKey) return this[item.colorKey]
      return item.color
    },
    goTo(route) { this.$router.push(route).catch(() => {}) },
  },
}
</script>

<style lang="scss" scoped>
.military-home { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 16px; padding: 18px 24px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
  h2 { font-size: 20px; margin: 0 0 4px; i { margin-right: 8px; } }
  p { font-size: 13px; opacity: 0.85; margin: 0; }
  .header-right { display: flex; align-items: center; }
}
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  background: #fff; border-radius: 6px; padding: 14px 12px; display: flex; align-items: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: transform 0.2s;
  &:hover { transform: translateY(-2px); }
  .kpi-icon { width: 36px; height: 36px; border-radius: 8px; background: #F0F5FF; display: flex; align-items: center; justify-content: center; margin-right: 10px; i { font-size: 18px; } }
  .kpi-body { flex: 1; }
  .kpi-val { font-size: 20px; font-weight: 700; line-height: 1.2; }
  .kpi-label { font-size: 12px; color: #8C8C8C; margin-top: 2px; }
  .kpi-trend { font-size: 11px; font-weight: 600; white-space: nowrap; }
}
.main-row { margin-bottom: 16px; }
.card-header-row { display: flex; align-items: center; justify-content: space-between; }
.tree-chart { height: 360px; width: 100%; }
.legend-bar { display: flex; align-items: center; gap: 14px; padding-top: 8px; font-size: 12px; color: #8C8C8C;
  .legend-dot { width: 10px; height: 10px; border-radius: 2px; display: inline-block; margin-right: 4px; }
}
.warn-card ::v-deep .el-card__body { padding: 8px 12px; }
.warn-list { max-height: 340px; overflow-y: auto; }
.warn-item {
  display: flex; align-items: center; gap: 8px; padding: 10px 8px; border-bottom: 1px solid #F0F0F0; font-size: 13px; cursor: pointer;
  &:hover { background: #FAFAFA; }
  .warn-title { flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .warn-company { font-size: 11px; color: #8C8C8C; white-space: nowrap; max-width: 70px; overflow: hidden; text-overflow: ellipsis; }
  .warn-time { color: #BFBFBF; font-size: 11px; white-space: nowrap; }
  &.warn-high .warn-title { color: #CF1322; }
  &.warn-medium .warn-title { color: #FA8C16; }
}
.nav-row { margin-bottom: 16px; }
.nav-card {
  background: #fff; border-radius: 6px; padding: 16px; display: flex; align-items: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08); cursor: pointer; transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.12); }
  .nav-icon { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 20px; color: #fff; } }
  .nav-body { flex: 1; }
  .nav-title { font-size: 14px; font-weight: 600; color: #303133; }
  .nav-desc { font-size: 12px; color: #8C8C8C; margin-top: 2px; }
  .nav-arrow { color: #C0C4CC; font-size: 16px; }
}
.risk-map-card { margin-bottom: 16px; }
.risk-map-chart { height: 320px; width: 100%; }
</style>
