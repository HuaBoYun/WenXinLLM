<template>
  <div class="invest-dashboard" :style="themeVars">
    <!-- 顶部 Banner -->
    <div class="dash-banner">
      <span class="dash-title"><i class="el-icon-odometer" /> 投资穿透监控驾驶舱</span>
      <div class="dash-btns">
        <el-select v-model="selectedYear" size="small" style="width:100px;margin-right:8px">
          <el-option v-for="y in yearList" :key="y" :label="y+'年'" :value="y" />
        </el-select>
        <el-select v-model="selectedCompany" size="small" placeholder="全部企业" style="width:120px">
          <el-option label="全部企业" value="" />
          <el-option v-for="c in companyList" :key="c" :label="c" :value="c" />
        </el-select>
      </div>
    </div>

    <!-- 6列统计卡片 -->
    <el-row :gutter="12" class="stat-row">
      <el-col :span="4" v-for="(card, i) in statCards" :key="i">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '14px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }">
              <i :class="card.icon" :style="{ color: card.color }"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value" :style="{ color: card.color }">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-change" :class="card.trend > 0 ? 'up' : 'down'">
                <i :class="card.trend > 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                {{ Math.abs(card.trend) }}% 较上月
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：投资规模趋势 + 投资类型分布 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>年度投资规模趋势</span></div>
          <div ref="trendChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>投资类型分布</span></div>
          <div ref="typeChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：项目进度 + 预警信息 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>重点项目进度</span>
            <el-tag size="mini" type="info">TOP 6</el-tag>
          </div>
          <el-table :data="projectProgress" size="small" style="width: 100%">
            <el-table-column label="项目名称" prop="name" min-width="150" show-overflow-tooltip />
            <el-table-column label="投资额(万)" prop="amount" width="100" align="right">
              <template slot-scope="s"><span class="amount-text">{{ s.row.amount.toLocaleString() }}</span></template>
            </el-table-column>
            <el-table-column label="进度" width="180">
              <template slot-scope="s">
                <el-progress :percentage="s.row.progress"
                  :color="s.row.progress >= 80 ? '#52C41A' : s.row.progress >= 50 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="16" :text-inside="true" />
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80" align="center">
              <template slot-scope="s"><el-tag :type="s.row.statusType" size="mini">{{ s.row.status }}</el-tag></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>风险预警</span>
            <el-badge :value="warnings.length" class="badge-item" />
          </div>
          <div class="warning-list">
            <div v-for="(w, i) in warnings" :key="i" class="warning-item">
              <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]" size="mini" class="warning-tag">
                {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
              </el-tag>
              <span class="warning-text">{{ w.title }}</span>
              <span class="warning-time">{{ w.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：收益分析 + 非主业投资占比 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>投资收益对比分析</span></div>
          <div ref="returnChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header"><span>非主业投资占比趋势</span></div>
          <div ref="nonMainChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getInvestDashboard, getInvestTypeDistribution, getNonMainBizTrend } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

export default {
  name: 'InvestDashboard',
  mixins: [investThemeMixin],
  data() {
    const currentYear = new Date().getFullYear()
    const yearList = []
    for (let y = currentYear; y >= currentYear - 7; y--) {
      yearList.push(y)
    }
    return {
      selectedYear: currentYear,
      selectedCompany: '',
      yearList,
      companyList: [],
      statCards: [],
      warnings: [],
      projectProgress: [],
      trendData: { years: [], amounts: [], counts: [] },
      typeChartData: [],
      nonMainTrendData: [],
    }
  },
  watch: {
    selectedYear() { this.loadData() },
    selectedCompany() { this.loadData() },
  },
  created() { this.loadData() },
  beforeDestroy() { this.disposeCharts() },
  methods: {
    async loadData() {
      try {
        const params = { year: this.selectedYear }
        if (this.selectedCompany) params.companyId = this.selectedCompany
        const res = await getInvestDashboard(params)
        if (res.result === 200 && res.data) {
          const kpi = res.data.kpi || {}
          this.companyList = res.data.companyList || []
          this.statCards = [
            { label: '在管投资项目', value: kpi.totalProjects || 0, icon: 'el-icon-document', color: this.ipPrimary, bg: `rgba(${this.ipPrimaryRgb},0.1)`, trend: 0 },
            { label: '投资总额(亿元)', value: ((kpi.totalInvestAmount || 0) / 10000).toFixed(1), icon: 'el-icon-money', color: '#FAAD14', bg: 'rgba(250,173,20,0.12)', trend: 0 },
            { label: '综合收益率', value: (kpi.avgReturnRate || 0) + '%', icon: 'el-icon-data-line', color: '#52C41A', bg: 'rgba(82,196,26,0.1)', trend: 0 },
            { label: '非主业占比', value: (kpi.nonMainBizRatio || 0) + '%', icon: 'el-icon-pie-chart', color: '#FA8C16', bg: 'rgba(250,140,22,0.1)', trend: 0 },
            { label: '活跃预警', value: kpi.activeWarnings || 0, icon: 'el-icon-bell', color: '#FF4D4F', bg: 'rgba(255,77,79,0.1)', trend: 0 },
            { label: '投后评价达标', value: (kpi.postEvalPassRate || 0) + '%', icon: 'el-icon-circle-check', color: this.ipPrimary, bg: `rgba(${this.ipPrimaryRgb},0.08)`, trend: 0 },
          ]
          // warnings
          const typeMap = { UNAUTHORIZED: '越权投资', RETURN_DEVIATION: '收益偏离', PROGRESS_DELAY: '进度滞后', NON_MAIN_EXCEED: '非主业超限', COMPLIANCE: '决策程序', EXIT_OVERDUE: '清退逾期' }
          this.warnings = (res.data.warnings || []).map(w => ({
            level: w.level,
            title: (w.companyName || '') + '：' + (w.projectName || '') + ' - ' + (typeMap[w.warningType] || ''),
            time: w.triggerTime ? w.triggerTime.substring(0, 10) : '',
          }))
          // project progress
          this.projectProgress = (res.data.projectProgress || []).map(p => ({
            name: p.name,
            amount: Number(p.amount) || 0,
            progress: 50,
            status: p.status === 'EXECUTING' ? '进行中' : p.status === 'COMPLETED' ? '已完成' : p.status === 'PAUSED' ? '暂停' : '进行中',
            statusType: p.status === 'COMPLETED' ? 'success' : p.status === 'PAUSED' ? 'warning' : '',
          }))
          // trend data for charts
          this.trendData = {
            years: (res.data.trend || []).map(t => t.period),
            amounts: (res.data.trend || []).map(t => Number(t.investAmount || 0) / 10000),
            counts: (res.data.trend || []).map(t => t.projectCount || 0),
          }
        }
      } catch (e) {
        console.error('加载驾驶舱数据失败', e)
      }
      // 加载趋势图（使用已有数据）
      this.$nextTick(() => { this.initTrendChart() })
      // 并行加载其他图表数据
      this.loadTypeChart()
      this.loadReturnChart()
      this.loadNonMainChart()
    },
    async loadTypeChart() {
      try {
        const params = { year: this.selectedYear }
        if (this.selectedCompany) params.companyId = this.selectedCompany
        const res = await getInvestTypeDistribution(params)
        if (res.result === 200 && res.data) {
          const typeLabels = { EQUITY: '股权投资', DEBT: '债权投资', FUND: '基金投资', MIXED: '混合型', OTHER: '其他' }
          this.typeChartData = (res.data || []).map(item => ({
            value: item.count,
            name: typeLabels[item.type] || item.type,
          }))
        }
      } catch (e) {
        console.error('加载投资类型分布失败', e)
      }
      this.$nextTick(() => { this.initTypeChart() })
    },
    async loadReturnChart() {
      // 收益对比使用 projectProgress 数据（已在 loadData 中加载）
      this.$nextTick(() => { this.initReturnChart() })
    },
    async loadNonMainChart() {
      try {
        const params = { year: this.selectedYear }
        if (this.selectedCompany) params.companyId = this.selectedCompany
        const res = await getNonMainBizTrend(params)
        if (res.result === 200 && res.data) {
          this.nonMainTrendData = res.data || []
        }
      } catch (e) {
        console.error('加载非主业趋势失败', e)
      }
      this.$nextTick(() => { this.initNonMainChart() })
    },
    disposeCharts() {
      ['trendChart', 'typeChart', 'returnChart', 'nonMainChart'].forEach(r => {
        if (this.$refs[r]) { const c = echarts.getInstanceByDom(this.$refs[r]); c && c.dispose() }
      })
      window.removeEventListener('resize', this.handleResize)
    },
    handleResize() {
      ['trendChart', 'typeChart', 'returnChart', 'nonMainChart'].forEach(r => {
        if (this.$refs[r]) { const c = echarts.getInstanceByDom(this.$refs[r]); c && c.resize() }
      })
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      const trend = (this.dashboardData && this.dashboardData.trend) || []
      const years = trend.map(t => t.period)
      const amounts = trend.map(t => t.investAmount || 0)
      const counts = trend.map(t => t.projectCount || 0)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['投资金额(亿元)', '项目数量'], top: 0 },
        grid: { top: 36, left: 50, right: 50, bottom: 30 },
        xAxis: { type: 'category', data: this.trendData.years },
        yAxis: [
          { type: 'value', name: '金额(亿元)', position: 'left' },
          { type: 'value', name: '项目数', position: 'right' },
        ],
        series: [
          { name: '投资金额(亿元)', type: 'bar', data: this.trendData.amounts, itemStyle: { color: this.ipPrimary, borderRadius: [4, 4, 0, 0] } },
          { name: '项目数量', type: 'line', yAxisIndex: 1, data: this.trendData.counts, smooth: true, itemStyle: { color: this.ipAccent }, areaStyle: { color: 'rgba(250,173,20,0.15)' } },
        ],
      })
      window.addEventListener('resize', this.handleResize)
    },
    initTypeChart() {
      if (!this.$refs.typeChart) return
      const chart = echarts.init(this.$refs.typeChart)
      const colors = [this.ipPrimary, this.ipSecondary, this.ipAccent, '#FA8C16', '#909399']
      const data = this.typeChartData.length > 0
        ? this.typeChartData.map((item, i) => ({ ...item, itemStyle: { color: colors[i % colors.length] } }))
        : [{ value: 0, name: '暂无数据' }]
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}个 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center' },
        series: [{
          type: 'pie', radius: ['40%', '70%'], center: ['35%', '50%'],
          label: { show: false },
          data: data,
        }],
      })
    },
    initReturnChart() {
      if (!this.$refs.returnChart) return
      const chart = echarts.init(this.$refs.returnChart)
      const projects = this.projectProgress.slice(0, 5)
      const names = projects.length > 0 ? projects.map(p => p.name ? p.name.substring(0, 6) : '') : ['暂无数据']
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['投资额(万元)'], top: 0 },
        grid: { top: 36, left: 50, right: 20, bottom: 30 },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 15 } },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: [
          { name: '投资额(万元)', type: 'bar', data: projects.map(p => p.amount || 0), itemStyle: { color: this.ipPrimary, borderRadius: [4, 4, 0, 0] } },
        ],
      })
    },
    initNonMainChart() {
      if (!this.$refs.nonMainChart) return
      const chart = echarts.init(this.$refs.nonMainChart)
      const years = this.nonMainTrendData.length > 0 ? this.nonMainTrendData.map(t => t.year) : ['2021', '2022', '2023', '2024', '2025']
      const ratios = this.nonMainTrendData.length > 0 ? this.nonMainTrendData.map(t => Number(t.nonMainRatio) || 0) : [0, 0, 0, 0, 0]
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['非主业占比', '预警线(20%)'], top: 0 },
        grid: { top: 36, left: 50, right: 20, bottom: 30 },
        xAxis: { type: 'category', data: years },
        yAxis: { type: 'value', name: '占比(%)', max: 40 },
        series: [
          { name: '非主业占比', type: 'line', data: ratios, smooth: true, itemStyle: { color: this.ipAccent }, areaStyle: { color: 'rgba(250,140,22,0.15)' } },
          { name: '预警线(20%)', type: 'line', data: Array(years.length).fill(20), lineStyle: { type: 'dashed', color: '#FF4D4F', width: 2 }, itemStyle: { color: '#FF4D4F' }, symbol: 'none' },
        ],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.invest-dashboard {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.dash-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px;
  padding: 14px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  .dash-title { font-size: 16px; font-weight: 700; color: #fff; i { margin-right: 6px; color: var(--ip-accent, #FAAD14); } }
}
.stat-row { margin-bottom: 0; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 10px; flex-shrink: 0; }
.stat-icon-wrap i { font-size: 22px; }
.stat-info { flex: 1; min-width: 0; }
.stat-value { font-size: 22px; font-weight: 700; line-height: 1.3; }
.stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
.stat-change { font-size: 12px; margin-top: 4px; }
.stat-change.up { color: #52C41A; }
.stat-change.down { color: #FF4D4F; }
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: var(--ip-primary, #1A3A6B);
  border-bottom: 2px solid var(--ip-accent, #FAAD14);
  padding-bottom: 6px;
}
.chart-card .chart-box { height: 280px; }
.amount-text { font-weight: 600; color: var(--ip-primary, #1A3A6B); }
.warning-list { max-height: 280px; overflow-y: auto; }
.warning-item { display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.warning-item:last-child { border-bottom: none; }
.warning-tag { flex-shrink: 0; }
.warning-text { flex: 1; margin: 0 10px; font-size: 13px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.warning-time { font-size: 12px; color: #c0c4cc; white-space: nowrap; }
</style>
