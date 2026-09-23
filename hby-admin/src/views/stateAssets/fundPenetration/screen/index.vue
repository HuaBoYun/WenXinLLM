<template>
  <PenetrationScreen
    title="资金穿透大屏" titleIcon="el-icon-coin"
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
import { getFundFlowList, getFundFlowStats } from '@/api/stateAssets/financialPenetration'
import { getFinancialRiskAlertList } from '@/api/stateAssets/financialRiskPenetration'

// 资金用途中文映射
const PURPOSE_CN = {
  OUTFLOW: '资金流出', INFLOW: '资金流入', INTERNAL_TRANSFER: '内部划转',
  INVESTMENT_RECOVERY: '投资回收', LOAN: '贷款', TRANSFER: '转让',
  SALE: '销售', SERVICE: '服务', GUARANTEE: '担保',
}
// 预警类型中文映射
const ALERT_TYPE_CN = {
  ANOMALY_CONFIRMED: '异常确认', AMOUNT_ABNORMAL: '金额异常', FREQUENCY_ABNORMAL: '频率异常',
  PATH_ABNORMAL: '路径异常', '金额监控': '金额监控', '频率监控': '频率监控',
  '委托贷款违约预警': '委托贷款违约', '流动性关注预警': '流动性关注', '流动性风险预警': '流动性风险', '融资逾期预警': '融资逾期',
}

export default {
  name: 'FundPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      kpiCards: [
        { label: '资金流转笔数', value: '-', icon: 'el-icon-sort', route: '/fundPenetration/home' },
        { label: '异常资金笔数', value: '-', icon: 'el-icon-warning', route: '/mxgl/fxyjgl' },
        { label: '资金流转总额(万)', value: '-', icon: 'el-icon-coin', route: '/finance/tableQuery' },
        { label: '风险预警数', value: '-', icon: 'el-icon-bell', route: '/mxgl/fxyj' },
        { label: '高风险预警', value: '-', icon: 'el-icon-warning-outline', route: '/mxgl/fxyj' },
        { label: '中风险预警', value: '-', icon: 'el-icon-info', route: '/mxgl/fxyj' },
      ],
      pages: [
        { label: '资金穿透首页', route: '/fundPenetration/home', icon: 'el-icon-s-home' },
        { label: '资金穿透画像', route: '/enterpriseProfile', icon: 'el-icon-user' },
        { label: '司库数据', route: '/finance/tableQuery', icon: 'el-icon-s-grid' },
        { label: '财务数据', route: '/finance/tableQuery', icon: 'el-icon-s-finance' },
        { label: '合同数据', route: '/finance/tableQuery', icon: 'el-icon-document-copy' },
        { label: '发票数据', route: '/finance/tableQuery', icon: 'el-icon-tickets' },
        { label: '司法数据穿透', route: '/workbench/industryData/telescope', icon: 'el-icon-search' },
        { label: '数据模型管理', route: '/mxgl/sjmxgl', icon: 'el-icon-set-up' },
        { label: '评估模型', route: '/mxgl/pgmxgl', icon: 'el-icon-data-analysis' },
        { label: '风险预警', route: '/mxgl/fxyj', icon: 'el-icon-warning-outline' },
        { label: '风险预警数据', route: '/mxgl/fxyjgl', icon: 'el-icon-bell' },
        { label: '数据源管理', route: '/base/sjygl', icon: 'el-icon-connection' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '资金流向分布', icon: 'el-icon-sort' },
        { ref: 'chart2', title: '资金流转月度趋势', icon: 'el-icon-data-line' },
        { ref: 'chart3', title: '金融风险预警分布', icon: 'el-icon-warning' },
        { ref: 'chart4', title: '风险等级分布', icon: 'el-icon-data-analysis' },
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
      await Promise.allSettled([this.loadFundFlowCharts(), this.loadAlertCharts()])
    },

    // chart1（资金流向分布）+ chart2（月度趋势）：来自 fund-flow/list
    async loadFundFlowCharts() {
      // 默认静态数据
      let purposeData = [
        { name: '资金流出', value: 4 }, { name: '内部划转', value: 3 },
        { name: '投资回收', value: 2 }, { name: '其他', value: 6 },
      ]
      let months = ['2024-01', '2024-02', '2024-03', '2024-04']
      let outAmounts = [0, 0, 0, 0]
      let inAmounts = [0, 0, 0, 0]

      try {
        const [listRes, statsRes] = await Promise.allSettled([
          getFundFlowList({ pageSize: 100 }),
          getFundFlowStats(),
        ])

        if (listRes.status === 'fulfilled' && listRes.value && listRes.value.data) {
          const data = listRes.value.data
          const list = data.tlist || data.list || (Array.isArray(data) ? data : [])

          if (list.length) {
            // KPI 更新
            this.kpiCards[0].value = list.length
            this.kpiCards[1].value = list.filter(i => i.riskLevel === 'ABNORMAL').length
            const totalAmount = list.reduce((s, i) => s + (i.amount || 0), 0)
            this.kpiCards[2].value = Math.round(totalAmount / 100) / 10 + '万'

            // chart1：按 purpose 统计资金流向分布
            const purposeMap = {}
            list.forEach(i => {
              const label = PURPOSE_CN[i.purpose] || i.purpose || '其他'
              purposeMap[label] = (purposeMap[label] || 0) + 1
            })
            purposeData = Object.entries(purposeMap).map(([name, value]) => ({ name, value }))

            // chart2：按 occurTime 月份统计流入/流出金额
            const monthOutMap = {}, monthInMap = {}
            list.forEach(i => {
              const m = i.occurTime || ''
              const amt = (i.amount || 0) / 10000 // 元→万
              const isOut = ['OUTFLOW', 'LOAN', 'TRANSFER', 'GUARANTEE'].includes(i.purpose)
              if (m) {
                if (isOut) monthOutMap[m] = (monthOutMap[m] || 0) + amt
                else monthInMap[m] = (monthInMap[m] || 0) + amt
              }
            })
            const allMonths = [...new Set([...Object.keys(monthOutMap), ...Object.keys(monthInMap)])].sort()
            if (allMonths.length) {
              months = allMonths
              outAmounts = allMonths.map(m => Math.round((monthOutMap[m] || 0) * 10) / 10)
              inAmounts = allMonths.map(m => Math.round((monthInMap[m] || 0) * 10) / 10)
            }
          }
        }
      } catch (e) { console.error('[fund screen flow]', e) }

      // chart1：资金流向分布（独立 nextTick）
      this.$nextTick(() => {
        this.initChart('chart1', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}笔 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' }, type: 'scroll' },
          series: [{
            type: 'pie', radius: ['35%', '65%'], data: purposeData,
            label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(250,173,20,0.5)' } },
          }],
        }, '/finance/tableQuery')
      })

      // chart2：月度趋势（独立 nextTick）
      this.$nextTick(() => {
        this.initChart('chart2', {
          tooltip: { trigger: 'axis' },
          legend: { data: ['流出(万)', '流入(万)'], textStyle: { color: '#aaa' }, bottom: 0 },
          xAxis: { type: 'category', data: months, axisLabel: { color: '#aaa', rotate: 20 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}万' } },
          series: [
            { name: '流出(万)', type: 'bar', data: outAmounts, barGap: 0, itemStyle: { color: '#FF4D4F', borderRadius: [4, 4, 0, 0] } },
            { name: '流入(万)', type: 'bar', data: inAmounts, itemStyle: { color: '#52C41A', borderRadius: [4, 4, 0, 0] } },
          ],
          grid: { left: 50, right: 20, bottom: 50, top: 20 },
        }, '/finance/tableQuery')
      })
    },

    // chart3（风险预警类型分布）+ chart4（风险等级分布）：来自 financial-risk/alert/list
    async loadAlertCharts() {
      // 默认静态数据
      let alertTypeData = [
        { name: '金额异常', value: 3 }, { name: '频率异常', value: 2 },
        { name: '路径异常', value: 2 }, { name: '其他', value: 3 },
      ]
      let levelData = [
        { name: '高风险', value: 4 }, { name: '中风险', value: 4 }, { name: '低风险', value: 2 },
      ]

      try {
        const res = await getFinancialRiskAlertList({ pageNumber: 1, pageSize: 100 })
        const data = res && res.data ? res.data : {}
        const list = data.tlist || data.list || (Array.isArray(data) ? data : [])

        if (list.length) {
          // KPI 更新
          this.kpiCards[3].value = list.length
          this.kpiCards[4].value = list.filter(i => i.level === 'HIGH' || i.alertLevel === 'HIGH').length
          this.kpiCards[5].value = list.filter(i => i.level === 'MEDIUM' || i.alertLevel === 'MEDIUM').length

          // chart3：按 alertType 统计预警类型分布
          const typeMap = {}
          list.forEach(i => {
            const raw = i.alertType || i.alertName || '其他'
            const label = ALERT_TYPE_CN[raw] || raw
            typeMap[label] = (typeMap[label] || 0) + 1
          })
          alertTypeData = Object.entries(typeMap).map(([name, value]) => ({ name, value }))

          // chart4：按 level 统计风险等级分布
          const levelMap = { HIGH: 0, MEDIUM: 0, LOW: 0 }
          list.forEach(i => {
            const lv = i.level || i.alertLevel || ''
            if (levelMap[lv] !== undefined) levelMap[lv]++
          })
          levelData = [
            { name: '高风险', value: levelMap.HIGH },
            { name: '中风险', value: levelMap.MEDIUM },
            { name: '低风险', value: levelMap.LOW },
          ].filter(d => d.value > 0)
        }
      } catch (e) { console.error('[fund screen alert]', e) }

      // chart3：预警类型分布（独立 nextTick）
      this.$nextTick(() => {
        this.initChart('chart3', {
          tooltip: { trigger: 'axis', formatter: params => `${params[0].name}: ${params[0].value}条` },
          xAxis: { type: 'category', data: alertTypeData.map(i => i.name), axisLabel: { color: '#aaa', rotate: 25, fontSize: 10 } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa' }, name: '条数', nameTextStyle: { color: '#aaa' } },
          series: [{
            type: 'bar', data: alertTypeData.map(i => i.value),
            itemStyle: { color: '#FF4D4F', borderRadius: [4, 4, 0, 0] },
            label: { show: true, position: 'top', color: '#ccc', fontSize: 10 },
          }],
          grid: { left: 40, right: 20, bottom: 65, top: 35 },
        }, '/mxgl/fxyj')
      })

      // chart4：风险等级分布（独立 nextTick）
      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: { trigger: 'item', formatter: '{b}: {c}条 ({d}%)' },
          legend: { bottom: 0, textStyle: { color: '#aaa' } },
          series: [{
            type: 'pie', radius: '60%', data: levelData,
            label: { color: '#ccc' }, color: ['#FF4D4F', '#FA8C16', '#52C41A'],
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
          }],
        }, '/mxgl/pgmxgl')
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
.chart-grid { display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 1fr 1fr; gap: 12px; flex: 1; min-height: 0; }
.chart-panel { background: rgba(255,255,255,0.04); border: 1px solid rgba(250,173,20,0.2); border-radius: 6px; padding: 10px; display: flex; flex-direction: column; }
.chart-panel-title { font-size: 13px; color: #FAAD14; margin-bottom: 6px; i { margin-right: 4px; } }
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }
</style>
