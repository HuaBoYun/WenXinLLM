<template>
  <div class="property-dashboard">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">产权监控驾驶舱</h2>
        <p class="banner-sub">产权领域综合大屏 · 层级分布 · 交易趋势 · 合规率 · 预警分布</p>
      </div>
      <div class="banner-right">
        <el-button type="primary" size="small" icon="el-icon-download" @click="handleExport">导出报告</el-button>
        <el-button type="warning" size="small" icon="el-icon-data-analysis" @click="handleAnalysis">综合分析</el-button>
        <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen">大屏展示</el-button>
      </div>
    </div>

    <!-- 顶部KPI -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}{{ kpi.unit }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：层级分布图 + 交易趋势图 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>股权层级分布</span>
            <el-tag size="mini" type="danger">超5级=超限</el-tag>
          </div>
          <div ref="levelChart" style="height:240px;"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>近5年产权交易趋势</span>
          </div>
          <div ref="trendChart" style="height:240px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：合规仪表盘 + 预警分布 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>合规率仪表盘</span></div>
          <el-row :gutter="8">
            <el-col :span="8" v-for="item in complianceMetrics" :key="item.key">
              <div class="gauge-wrap">
                <div ref="gaugeChart" :data-key="item.key" style="height:160px;"></div>
                <div class="gauge-label">{{ item.label }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>产权预警类型分布</span></div>
          <div ref="warningChart" style="height:220px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：异常企业TOP10 + 最新预警列表 -->
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>产权问题企业 TOP10</span>
          </div>
          <el-table :data="top10List" size="small">
            <el-table-column label="排名" width="50" align="center">
              <template slot-scope="{$index}">
                <span :class="['rank-num', $index < 3 ? 'rank-top' : '']">{{ $index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="企业名称" prop="companyName" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column label="问题类型" prop="issueType" min-width="130">
              <template slot-scope="{row}">
                <el-tag :type="row.issueLevel === 'HIGH' ? 'danger' : 'warning'" size="mini">{{ row.issueType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="问题数" prop="issueCount" width="70" align="center">
              <template slot-scope="{row}">
                <b :style="{ color: row.issueCount >= 3 ? '#F5222D' : '#FA8C16' }">{{ row.issueCount }}</b>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>最新预警记录</span>
            <el-button type="text" size="mini" @click="$router.push('/assetPenetration/CqctIndex')">查看全部</el-button>
          </div>
          <div class="warning-list">
            <div v-for="w in latestWarnings" :key="w.id" class="warning-item" :class="w.level === 'HIGH' ? 'w-high' : w.level === 'MEDIUM' ? 'w-medium' : ''">
              <el-tag :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : ''" size="mini">{{ w.levelLabel }}</el-tag>
              <span class="w-name">{{ w.companyName }}</span>
              <span class="w-desc">{{ w.desc }}</span>
              <span class="w-date">{{ w.date }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <PropertyScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getPropertyDashboard, getPropertyWarningList } from '@/api/stateAssets/propertyRight'
import request from '@/utils/request'
import PropertyScreen from '../screen/index.vue'

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
  name: 'PropertyDashboard',
  components: { PropertyScreen },
  data() {
    return {
      kpiList: [],
      complianceMetrics: [],
      top10List: [],
      latestWarnings: [],
      screenVisible: false,
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    openScreen() {
      this.screenVisible = true
      this.$nextTick(() => {
        const el = this.$refs.screenContainer
        if (el && el.requestFullscreen) el.requestFullscreen()
        else if (el && el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      })
    },
    closeScreen() {
      this.screenVisible = false
      if (document.exitFullscreen) document.exitFullscreen().catch(() => {})
    },
    async loadData() {
      try {
        const res = await getPropertyDashboard()
        const d = res.data || {}
        this.buildKpi(d)
        this.complianceMetrics = d.complianceMetrics || [
          { key: 'exchange', label: '进场交易率', value: d.exchangeTradeRate || 0, color: '#1677FF' },
          { key: 'appraisal', label: '评估覆盖率', value: d.appraisalRate || 0, color: '#52C41A' },
          { key: 'overall', label: '整体合规率', value: d.complianceRate || 0, color: '#722ED1' },
        ]
        this.top10List = d.top10List || []
        this.$nextTick(() => {
          this.initLevelChart(d.levelDistribution || [])
          this.initTrendChart(d.yearlyTrend || [])
          this.initGaugeCharts()
          this.initWarningChart()
        })
      } catch {
        this.buildKpi({})
      }
      try {
        const warnRes = await getPropertyWarningList({ pageSize: 6 })
        const rawWarnings = (warnRes.data && (warnRes.data.tlist || warnRes.data.list)) || []
        this.latestWarnings = rawWarnings.map(w => ({
          id: w.warningId || w.id,
          level: w.warningLevel || w.level || 'LOW',
          levelLabel: (w.warningLevel || w.level) === 'HIGH' ? '高危' : (w.warningLevel || w.level) === 'MEDIUM' ? '中危' : '低危',
          companyName: w.companyName || '',
          desc: w.warningContent || w.desc || '',
          date: w.createTime ? w.createTime.substring(0, 10) : (w.date || ''),
        }))
        this.$nextTick(() => this.initWarningChart())
      } catch {
        this.latestWarnings = []
      }
    },
    buildKpi(d) {
      this.kpiList = [
        { key: 'total', label: '登记企业总数', value: d.registeredCount || 0, unit: '家', color: '#1677FF' },
        { key: 'exchange', label: '进场交易率', value: d.exchangeTradeRate || 0, unit: '%', color: (d.exchangeTradeRate || 0) < 80 ? '#F5222D' : '#52C41A' },
        { key: 'share', label: '参股企业盈利率', value: d.shareholdingProfitRate || 0, unit: '%', color: '#52C41A' },
        { key: 'warnings', label: '活跃预警数', value: d.activeWarnings || 0, unit: '条', color: '#F5222D' },
      ]
    },
    /** 导出驾驶舱报告 */
    handleExport() {
      this.$confirm('确认导出产权监控驾驶舱报告？', '提示', { type: 'info' }).then(() => {
        request({
          url: '/monitor/v1/supervision/property/dashboard/export',
          method: 'get',
          responseType: 'blob',
        }).then(res => {
          // blob响应拦截器返回完整response对象，取res.data
          const blobData = res.data || res
          const blob = new Blob([blobData], { type: 'application/vnd.ms-excel;charset=UTF-8' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '产权监控驾驶舱报告.csv'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }).catch(() => {
          this.$message.error('导出失败，请稍后重试')
        })
      }).catch(() => {})
    },
    /** 综合分析 - 跳转到产权穿透首页查看完整分析 */
    handleAnalysis() {
      this.$router.push('/assetPenetration/CqctIndex')
    },
    initLevelChart(data) {
      if (!this.$refs.levelChart) return
      const chart = echarts.init(this.$refs.levelChart)
      const chartData = (data && data.length > 0) ? data : []
      if (chartData.length === 0) {
        chart.setOption({
          title: { text: '暂无层级分布数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } },
          xAxis: { show: false }, yAxis: { show: false }, series: []
        })
        return
      }
      const colors = chartData.map(d => d.level > 5 ? '#F5222D' : '#1677FF')
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: chartData.map(d => `第${d.level}级`) },
        yAxis: { type: 'value', name: '企业数' },
        series: [{
          type: 'bar', data: chartData.map((d, i) => ({ value: d.count, itemStyle: { color: colors[i] } })),
          label: { show: true, position: 'top' }
        }]
      })
    },
    initTrendChart(data) {
      if (!this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      const chartData = (data && data.length > 0) ? data : []
      if (chartData.length === 0) {
        chart.setOption({
          title: { text: '暂无交易趋势数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } },
          xAxis: { show: false }, yAxis: { show: false }, series: []
        })
        return
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['交易总数', '合规交易数'] },
        xAxis: { type: 'category', data: chartData.map(d => d.year) },
        yAxis: { type: 'value', name: '笔' },
        series: [
          { name: '交易总数', type: 'bar', data: chartData.map(d => d.count), itemStyle: { color: '#1677FF' } },
          { name: '合规交易数', type: 'line', data: chartData.map(d => d.compliant), smooth: true, itemStyle: { color: '#52C41A' } },
        ]
      })
    },
    initGaugeCharts() {
      const refs = this.$refs.gaugeChart
      if (!refs) return
      const arr = Array.isArray(refs) ? refs : [refs]
      this.complianceMetrics.forEach((metric, i) => {
        if (!arr[i]) return
        const chart = echarts.init(arr[i])
        chart.setOption({
          series: [{
            type: 'gauge', radius: '85%', startAngle: 200, endAngle: -20, min: 0, max: 100,
            axisLine: { lineStyle: { width: 10, color: [[metric.value / 100, metric.color], [1, '#E0E0E0']] } },
            pointer: { length: '70%', width: 4 },
            detail: { valueAnimation: true, formatter: '{value}%', fontSize: 14, fontWeight: '700', color: metric.color, offsetCenter: [0, '50%'] },
            data: [{ value: metric.value }],
            axisTick: { show: false }, splitLine: { show: false }, axisLabel: { show: false },
          }]
        })
      })
    },
    initWarningChart() {
      if (!this.$refs.warningChart) return
      const chart = echarts.init(this.$refs.warningChart)
      const high = this.latestWarnings.filter(w => w.level === 'HIGH').length
      const medium = this.latestWarnings.filter(w => w.level === 'MEDIUM').length
      const low = this.latestWarnings.filter(w => w.level === 'LOW').length
      const chartData = high > 0 || medium > 0 || low > 0
        ? [
            { value: high, name: '高危（HIGH）', itemStyle: { color: '#F5222D' } },
            { value: medium, name: '中危（MEDIUM）', itemStyle: { color: '#FA8C16' } },
            { value: low, name: '低危（LOW）', itemStyle: { color: '#FAAD14' } },
          ]
        : [{ value: 0, name: '暂无预警', itemStyle: { color: '#D9D9D9' } }]
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { fontSize: 11 } },
        series: [{
          type: 'pie', radius: ['35%', '60%'], center: ['50%', '45%'],
          data: chartData,
          label: { formatter: '{b}\n{c}条' },
        }]
      })
    },
  }
}
</script>

<style scoped>
.property-dashboard { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
  display: flex; align-items: center; justify-content: space-between;
}
.banner-right { display: flex; gap: 8px; }
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.card-header { display: flex; align-items: center; justify-content: space-between; font-size: 14px; font-weight: 600; }
.gauge-wrap { text-align: center; }
.gauge-label { font-size: 12px; color: #555; text-align: center; margin-top: -10px; }
.rank-num { font-weight: 700; font-size: 14px; color: #888; }
.rank-top { color: #F5222D !important; }
.warning-list { }
.warning-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid #f5f5f5; font-size: 12px; }
.warning-item.w-high { background: #fff7f7; }
.warning-item.w-medium { background: #fffdf0; }
.w-name { font-weight: 600; color: #333; min-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.w-desc { color: #888; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.w-date { color: #aaa; white-space: nowrap; }
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
</style>
