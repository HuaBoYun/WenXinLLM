<template>
  <div class="contract-dashboard">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title"><i class="el-icon-odometer" style="margin-right:8px;"/>合同监控驾驶舱</div>
      <div class="banner-sub">合同综合态势总览 · 规模 · 合规 · 履行 · 预警</div>
    </div>
    <!-- KPI卡 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="6" v-for="c in kpiCards" :key="c.label">
        <el-card class="kpi-card" shadow="never" :style="{'border-left':'4px solid '+c.color}">
          <div class="kpi-inner">
            <div>
              <div class="kpi-num" :style="{color:c.color}">{{ c.value }}<span class="kpi-unit">{{ c.unit }}</span></div>
              <div class="kpi-lbl">{{ c.label }}</div>
            </div>
            <i :class="c.icon" :style="{fontSize:'28px',color:c.color,opacity:.3}"/>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表行1 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">合同金额月度趋势（亿元）</div>
          <div ref="trendChart" style="height:240px;"/>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">合同类型分布</div>
          <div ref="typeChart" style="height:240px;"/>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表行2 -->
    <el-row :gutter="16">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">履行状态分布</div>
          <div ref="execChart" style="height:220px;"/>
          <div class="exec-legend">
            <div class="exec-item" v-for="e in execStatus" :key="e.name">
              <span class="exec-dot" :style="{background:e.color}"/>
              <span>{{ e.name }}</span>
              <span style="margin-left:auto;font-weight:600;">{{ e.value }}份</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">审批合规率趋势（%）</div>
          <div ref="compChart" style="height:220px;"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getContractDashboard, getContractDashboardTrends } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractDashboard',
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
  data() {
    return {
      kpiCards: [
        { label: '合同总量', value: '--', unit: '份', color: '#1677FF', icon: 'el-icon-document' },
        { label: '审批合规率', value: '--', unit: '%', color: '#52C41A', icon: 'el-icon-finished' },
        { label: '履行滞后', value: '--', unit: '份', color: '#FA8C16', icon: 'el-icon-timer' },
        { label: '活跃预警', value: '--', unit: '条', color: '#F5222D', icon: 'el-icon-warning' },
      ],
      execStatus: [],
      typeDistribution: [],
      trendData: { months: [], amountSeries: [], complianceRates: [] },
    }
  },
  mounted() {
    this.fetchDashboard()
  },
  methods: {
    async fetchDashboard() {
      try {
        const [overviewRes, trendsRes] = await Promise.all([
          getContractDashboard(),
          getContractDashboardTrends(),
        ])
        // 处理概览数据
        if (overviewRes && overviewRes.result === 200 && overviewRes.data) {
          const d = overviewRes.data
          this.kpiCards = [
            { label: '合同总量', value: d.totalContracts || '--', unit: '份', color: '#1677FF', icon: 'el-icon-document' },
            { label: '审批合规率', value: d.complianceRate != null ? d.complianceRate.toFixed(1) : '--', unit: '%', color: '#52C41A', icon: 'el-icon-finished' },
            { label: '履行滞后', value: d.delayCount != null ? d.delayCount : '--', unit: '份', color: '#FA8C16', icon: 'el-icon-timer' },
            { label: '活跃预警', value: d.warningCount != null ? d.warningCount : '--', unit: '条', color: '#F5222D', icon: 'el-icon-warning' },
          ]
          // 履行状态分布
          const execDist = d.execDistribution || []
          if (Array.isArray(execDist)) {
            this.execStatus = execDist.map(item => ({ name: item.name || '', value: item.value || 0, color: item.color || '#ccc' }))
          }
          // 合同类型分布（前端兜底翻译）
          const typeDist = d.typeDistribution || []
          const typeColorArr = ['#1677FF', '#52C41A', '#FA8C16', '#722ED1', '#F5222D', '#0050A0', '#389E0D']
          const typeNameMap = { PURCHASE: '采购合同', SALES: '销售合同', ENGINEERING: '工程合同', SERVICE: '服务合同', OTHER: '其他' }
          if (Array.isArray(typeDist)) {
            this.typeDistribution = typeDist.map((item, i) => ({
              name: typeNameMap[item.name] || item.name || '',
              value: item.value || 0,
              color: item.color || typeColorArr[i % typeColorArr.length],
            }))
          }
        }
        // 处理趋势数据
        if (trendsRes && trendsRes.result === 200 && trendsRes.data) {
          this.trendData = trendsRes.data
        }
      } catch (e) {
        console.error('驾驶舱数据加载失败', e)
      }
      this.$nextTick(this.initCharts)
    },
    initCharts() {
      if (typeof echarts === 'undefined') return
      this.initTrend(); this.initType(); this.initExec(); this.initComp()
    },
    initTrend() {
      const c = echarts.init(this.$refs.trendChart)
      const months = this.trendData.months || []
      const seriesData = this.trendData.amountSeries || []
      const colors = ['#1677FF', '#52C41A', '#FA8C16', '#722ED1', '#F5222D', '#0050A0']
      const series = seriesData.map((s, i) => ({
        name: s.name,
        type: 'line',
        smooth: true,
        data: s.data,
        lineStyle: { color: colors[i % colors.length] },
        itemStyle: { color: colors[i % colors.length] },
      }))
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: seriesData.map(s => s.name), top: 0, right: 0, textStyle: { fontSize: 12 } },
        grid: { left: 40, right: 10, top: 30, bottom: 30 },
        xAxis: { type: 'category', data: months, axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value', axisLabel: { fontSize: 11 } },
        series: series.length > 0 ? series : [{ name: '暂无数据', type: 'line', data: [] }],
      }
      if (series.length === 0) {
        option.title = { text: '暂无趋势数据', left: 'center', top: 'middle', textStyle: { color: '#bfbfbf', fontSize: 14, fontWeight: 'normal' } }
      }
      c.setOption(option)
    },
    initType() {
      const c = echarts.init(this.$refs.typeChart)
      const data = this.typeDistribution.length > 0
        ? this.typeDistribution.map(t => ({ name: t.name, value: t.value, itemStyle: { color: t.color } }))
        : [{ name: '暂无数据', value: 1, itemStyle: { color: '#f0f0f0' } }]
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {d}%' },
        legend: { orient: 'vertical', right: 10, top: 'middle', textStyle: { fontSize: 12 } },
        series: [{ type: 'pie', radius: ['45%', '70%'], center: ['38%', '50%'], label: { show: false }, data }],
      })
    },
    initExec() {
      const c = echarts.init(this.$refs.execChart)
      const pieData = this.execStatus.length > 0
        ? this.execStatus.map(e => ({ name: e.name, value: e.value, itemStyle: { color: e.color } }))
        : [{ name: '暂无数据', value: 1, itemStyle: { color: '#f0f0f0' } }]
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}份 ({d}%)' },
        series: [{ type: 'pie', radius: ['50%', '72%'], center: ['50%', '48%'], label: { show: false }, data: pieData }],
      })
    },
    initComp() {
      const c = echarts.init(this.$refs.compChart)
      const months = this.trendData.months || []
      const data = this.trendData.complianceRates || []
      c.setOption({
        tooltip: { trigger: 'axis', formatter: '{b}: {c}%' },
        grid: { left: 40, right: 10, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: months, axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value', min: function(value) { return Math.max(0, Math.floor(value.min - 5)) }, max: 100, axisLabel: { formatter: '{value}%', fontSize: 11 } },
        series: [{
          type: 'line', smooth: true, data, lineStyle: { color: '#52C41A' }, itemStyle: { color: '#52C41A' },
          areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(82,196,26,0.25)' }, { offset: 1, color: 'rgba(82,196,26,0)' }] } },
          markLine: { silent: true, data: [{ yAxis: 90, lineStyle: { color: '#FA8C16', type: 'dashed' }, label: { formatter: '90%基准线', position: 'end', color: '#FA8C16' } }] },
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.contract-dashboard { padding: 16px; background: #f5f7fa; min-height: 100%; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px;
  .banner-title { font-size: 20px; font-weight: 700; color: #fff; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 4px; }
}
.kpi-card { border-radius: 6px; }
.kpi-inner { display: flex; align-items: center; justify-content: space-between; padding: 4px 0; }
.kpi-num { font-size: 26px; font-weight: 700; }
.kpi-unit { font-size: 12px; font-weight: 400; margin-left: 4px; }
.kpi-lbl { font-size: 12px; color: #8c8c8c; margin-top: 4px; }
.exec-legend { display: flex; flex-direction: column; gap: 4px; margin-top: 8px; padding: 0 8px; }
.exec-item { display: flex; align-items: center; gap: 6px; font-size: 12px; color: #595959; }
.exec-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
</style>
