<template>
  <PenetrationScreen
    title="境外穿透大屏" titleIcon="el-icon-place"
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
import { getOverseasUnitList, getOverseasKPI, getOverseasForexStats, getOverseasOperationStats } from '@/api/stateAssets/overseasPenetration'

// 运营状态中文映射
const STATUS_CN = { NORMAL: '正常运营', WARNING: '预警', ABNORMAL: '异常', CLOSED: '已关闭' }

export default {
  name: 'OverseasPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '境外单位数', value: '-', icon: 'el-icon-office-building', route: '/overseasPenetration/unit' },
        { label: '投资总额(亿$)', value: '-', icon: 'el-icon-coin', route: '/overseasPenetration/investMgmt' },
        { label: '境外人员数', value: '-', icon: 'el-icon-user', route: '/overseasPenetration/personnelSafety' },
        { label: '高风险国家数', value: '-', icon: 'el-icon-warning', route: '/overseasPenetration/countryRiskMap' },
        { label: '外汇敞口(亿$)', value: '-', icon: 'el-icon-data-line', route: '/overseasPenetration/forexAnalysis' },
        { label: '合规预警数', value: '-', icon: 'el-icon-bell', route: '/overseasPenetration/riskWarning' },
      ],
      pages: [
        { label: '境外穿透首页', route: '/overseasPenetration/home', icon: 'el-icon-s-home' },
        { label: '境外单位台账', route: '/overseasPenetration/unit', icon: 'el-icon-office-building' },
        { label: '境外投资管理', route: '/overseasPenetration/investMgmt', icon: 'el-icon-money' },
        { label: '国别风险地图', route: '/overseasPenetration/countryRiskMap', icon: 'el-icon-map-location' },
        { label: '外汇风险分析', route: '/overseasPenetration/forexAnalysis', icon: 'el-icon-data-line' },
        { label: '境外合规管理', route: '/overseasPenetration/complianceMgmt', icon: 'el-icon-document-checked' },
        { label: '境外经营分析', route: '/overseasPenetration/operationAnalysis', icon: 'el-icon-data-analysis' },
        { label: '境外监控驾驶舱', route: '/overseasPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '境外人员安全管理', route: '/overseasPenetration/personnelSafety', icon: 'el-icon-user' },
        { label: '境外应急指挥中心', route: '/overseasPenetration/emergencyCommand', icon: 'el-icon-phone-outline' },
        { label: '境外风险预警管理', route: '/overseasPenetration/riskWarning', icon: 'el-icon-bell' },
        { label: '境外负责人管理', route: '/overseasPenetration/leader', icon: 'el-icon-s-custom' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '境外单位地区分布', icon: 'el-icon-pie-chart' },
        { ref: 'chart2', title: '境外单位状态分布', icon: 'el-icon-bar-chart' },
        { ref: 'chart3', title: '主要货币汇率趋势', icon: 'el-icon-data-line' },
        { ref: 'chart4', title: '境外经营收益分布', icon: 'el-icon-s-finance' },
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
      this.$nextTick(() => { this.$router.push(route).catch(() => {}) })
    },
    onViewChange() { this.loadData() },
    onCompanyChange() { this.loadData() },
    async loadData() {
      // 四个图表完全独立，互不阻塞
      await Promise.allSettled([
        this.loadKpiAndUnitCharts(),
        this.loadForexChart(),
        this.loadOperationChart(),
      ])
    },

    // KPI + chart1（地区分布）+ chart2（状态分布）
    async loadKpiAndUnitCharts() {
      try {
        const [kpiRes, unitRes] = await Promise.allSettled([
          getOverseasKPI(),
          getOverseasUnitList({ pageNumber: 1, pageSize: 200 }),
        ])

        // --- KPI 填充（字段名与接口对齐）---
        if (kpiRes.status === 'fulfilled' && kpiRes.value && kpiRes.value.data) {
          const k = kpiRes.value.data
          this.kpiCards[0].value = k.unitCount || 0
          this.kpiCards[1].value = k.investTotal || 0          // 接口字段：investTotal
          this.kpiCards[2].value = k.personnelCount || 0
          this.kpiCards[3].value = k.highRiskCountries || 0
          this.kpiCards[4].value = k.forexExposure || 0
          this.kpiCards[5].value = k.complianceWarning || 0    // 接口字段：complianceWarning
          // 填充公司下拉列表
          if (Array.isArray(k.recentWarnings)) {
            const names = [...new Set(k.recentWarnings.map(w => w.unitName || w.companyName).filter(Boolean))]
            if (names.length) this.companyList = names
          }
        }

        // --- 单位列表 → chart1 地区分布 + chart2 状态分布 ---
        const list = (unitRes.status === 'fulfilled' && unitRes.value && unitRes.value.data && unitRes.value.data.tlist)
          ? unitRes.value.data.tlist : []

        if (this.kpiCards[0].value === '-' || this.kpiCards[0].value === 0) {
          this.kpiCards[0].value = list.length
        }

        // 统计地区分布
        const countryMap = {}
        // 统计状态分布（字段名是 operationStatus，不是 status）
        const statusMap = {}
        list.forEach(u => {
          const country = u.country || '未知'
          countryMap[country] = (countryMap[country] || 0) + 1
          const rawStatus = u.operationStatus || u.status || 'UNKNOWN'
          const statusLabel = STATUS_CN[rawStatus] || rawStatus
          statusMap[statusLabel] = (statusMap[statusLabel] || 0) + 1
        })

        const countryData = Object.entries(countryMap).map(([name, value]) => ({ name, value }))
        const statusNames = Object.keys(statusMap)
        const statusValues = Object.values(statusMap)

        // chart1 和 chart2 分开独立 $nextTick，避免同一 tick 内 ref 未就绪
        this.$nextTick(() => {
          this.initChart('chart1', {
            tooltip: { trigger: 'item', formatter: '{b}: {c}家 ({d}%)' },
            legend: { bottom: 0, textStyle: { color: '#aaa' }, type: 'scroll' },
            series: [{
              type: 'pie', radius: ['35%', '65%'],
              data: countryData.length ? countryData : [{ name: '暂无数据', value: 1 }],
              label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
              emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(64,158,255,0.5)' } },
            }],
          }, '/overseasPenetration/unit')
        })

        // chart2 单独一个 nextTick，确保 DOM 已就绪
        this.$nextTick(() => {
          this.initChart('chart2', {
            tooltip: { trigger: 'axis', formatter: params => `${params[0].name}: ${params[0].value}家` },
            xAxis: { type: 'category', data: statusNames, axisLabel: { color: '#aaa' } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa' }, name: '家数', nameTextStyle: { color: '#aaa' } },
            series: [{
              type: 'bar', data: statusValues,
              itemStyle: {
                color: p => ({ '正常运营': '#52C41A', '预警': '#FA8C16', '异常': '#FF4D4F', '已关闭': '#909399' }[p.name] || '#409EFF'),
                borderRadius: [4, 4, 0, 0],
              },
            }],
            grid: { left: 45, right: 20, bottom: 30, top: 30 },
          }, '/overseasPenetration/unit')
        })
      } catch (e) { console.error('[overseas screen unit]', e) }
    },

    // chart3：主要货币汇率趋势（使用 rateData.series 多线图）
    async loadForexChart() {
      // 默认静态数据
      let months = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
      let seriesList = [
        { name: 'USD/CNY', data: [7.18,7.20,7.22,7.24,7.21,7.25,7.27,7.24,7.22,7.20,7.23,7.24], color: '#1677FF' },
        { name: 'EUR/CNY', data: [7.80,7.82,7.85,7.83,7.81,7.86,7.88,7.85,7.83,7.80,7.84,7.86], color: '#52C41A' },
        { name: 'GBP/CNY', data: [9.10,9.12,9.15,9.18,9.14,9.20,9.22,9.18,9.15,9.12,9.16,9.18], color: '#FA8C16' },
      ]
      try {
        const res = await getOverseasForexStats()
        if (res && res.data && res.data.rateData) {
          const rd = res.data.rateData
          if (Array.isArray(rd.months) && rd.months.length) months = rd.months
          if (Array.isArray(rd.series) && rd.series.length) {
            seriesList = rd.series.map(s => ({ name: s.name, data: s.data, color: s.color }))
          }
        }
      } catch (e) { /* 使用默认数据 */ }

      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'axis' },
          legend: { data: seriesList.map(s => s.name), textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: months, axisLabel: { color: '#aaa', rotate: 30 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' }, scale: true },
          series: seriesList.map(s => ({
            name: s.name, type: 'line', data: s.data, smooth: true,
            lineStyle: { color: s.color }, itemStyle: { color: s.color },
            symbol: 'none',
          })),
          grid: { left: 50, right: 20, bottom: 50, top: 20 },
        }, '/overseasPenetration/forexAnalysis')
      })
    },

    // chart4：境外经营收益分布（使用 pieData 各地区收益）
    async loadOperationChart() {
      // 默认静态数据
      let pieData = [
        { name: '新加坡', value: 23.5 }, { name: '阿联酋', value: 18.3 },
        { name: '澳大利亚', value: 15.2 }, { name: '加拿大', value: 12.8 },
        { name: '德国', value: 8.6 }, { name: '其他', value: 21.6 },
      ]
      try {
        const res = await getOverseasOperationStats()
        if (res && res.data) {
          const d = res.data
          // 优先使用 pieData（各地区收益分布）
          if (Array.isArray(d.pieData) && d.pieData.length) {
            pieData = d.pieData
          }
          // 更新 KPI 中的投资总额（如果 KPI 接口没有返回）
          if (d.revenue && this.kpiCards[1].value === '-') {
            this.kpiCards[1].value = d.revenue
          }
        }
      } catch (e) { /* 使用默认数据 */ }

      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}亿$ ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' }, type: 'scroll' },
          series: [{
            type: 'pie', radius: ['30%', '60%'],
            data: pieData,
            label: { color: '#ccc', formatter: '{b}\n{d}%' },
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
          }],
        }, '/overseasPenetration/operationAnalysis')
      })
    },

    initChart(refName, option, clickRoute) {
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
