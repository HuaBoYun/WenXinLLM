<template>
  <div class="procurement-dashboard" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title"><i class="el-icon-data-analysis"></i> 采购驾驶舱</h1>
        <p class="page-desc">集团采购综合大屏 · 实时监控采购风险与合规状态</p>
      </div>
    </div>

    <!-- 6个统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="4" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '14px 16px' }">
          <div class="stat-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg, color: card.color }">
              <i :class="card.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value" :style="{ color: card.color }">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区1：趋势 + 类型 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd"><i class="el-icon-trend-charts" :style="{color: ipSecondary}"></i> 采购规模月度趋势</div>
          <div ref="trendChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd"><i class="el-icon-pie-chart" :style="{color: ipSecondary}"></i> 采购类型分布</div>
          <div ref="typeChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区2：集中度 + 招投标方式 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd"><i class="el-icon-s-custom" :style="{color: ipSecondary}"></i> 供应商集中度TOP10</div>
          <div ref="supplierChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd"><i class="el-icon-document" :style="{color: ipSecondary}"></i> 招投标方式分布</div>
          <div ref="biddingChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预警清单 -->
    <el-card shadow="never" class="warning-table-card">
      <div slot="header" class="card-hd">
        <span><i class="el-icon-bell" style="color:#CF1322;margin-right:6px"></i>采购预警清单</span>
        <el-tag type="danger" size="mini">{{ warningList.length }} 条活跃预警</el-tag>
      </div>
      <el-table :data="warningList" border size="small">
        <el-table-column label="预警编号" prop="id" width="110" />
        <el-table-column label="风险等级" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[row.level]" size="mini">
              {{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[row.level] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警内容" prop="title" min-width="200" show-overflow-tooltip />
        <el-table-column label="涉及企业" prop="company" width="200" show-overflow-tooltip />
        <el-table-column label="预警时间" prop="time" width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getProcurementDashboard, getProcurementTrend, getProcurementWarnings } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'ProcurementDashboard',
  mixins: [investThemeMixin],
  data() {
    return {
      charts: [],
      dashboardData: null,
      trendData: [],
      statCards: [
        { label: '集团采购总额(亿元)', value: '--', icon: 'el-icon-shopping-cart-2', color: '#0050A0', bg: '#EBF1FF' },
        { label: '注册供应商', value: '--', icon: 'el-icon-s-custom', color: '#1677FF', bg: '#EBF1FF' },
        { label: '关联交易占比', value: '--', icon: 'el-icon-connection', color: '#722ED1', bg: '#F9F0FF' },
        { label: '招标合规率', value: '--', icon: 'el-icon-circle-check', color: '#52C41A', bg: '#F6FFED' },
        { label: '高风险企业', value: '--', icon: 'el-icon-warning', color: '#CF1322', bg: '#FFF1F0' },
        { label: '活跃预警', value: '--', icon: 'el-icon-bell', color: '#FF7A45', bg: '#FFF2E8' },
      ],
      warningList: [],
    }
  },
  created() {
    this.statCards[0].color = this.ipSecondary
    this.statCards[0].bg = this.ipLightBg
    this.statCards[1].color = this.ipBright
    this.statCards[1].bg = this.ipLightBg
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.fetchData()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      try {
        const [dashRes, trendRes, warnRes] = await Promise.all([
          getProcurementDashboard(),
          getProcurementTrend(),
          getProcurementWarnings(),
        ])
        // Overview stats
        if (dashRes && dashRes.result === 200 && dashRes.data) {
          const d = dashRes.data
          this.dashboardData = d
          this.statCards = [
            { label: '集团采购总额(亿元)', value: d.totalPurchaseAmount || '--', icon: 'el-icon-shopping-cart-2', color: this.ipSecondary, bg: this.ipLightBg },
            { label: '注册供应商', value: d.supplierCount ? d.supplierCount + '家' : '--', icon: 'el-icon-s-custom', color: this.ipBright, bg: this.ipLightBg },
            { label: '关联交易占比', value: d.relatedRatio || '--', icon: 'el-icon-connection', color: '#722ED1', bg: '#F9F0FF' },
            { label: '招标合规率', value: d.complianceRate || '--', icon: 'el-icon-circle-check', color: '#52C41A', bg: '#F6FFED' },
            { label: '高风险企业', value: d.highRiskCount ? d.highRiskCount + '家' : '--', icon: 'el-icon-warning', color: '#CF1322', bg: '#FFF1F0' },
            { label: '活跃预警', value: d.activeAlerts ? d.activeAlerts + '条' : '--', icon: 'el-icon-bell', color: '#FF7A45', bg: '#FFF2E8' },
          ]
          // Re-render charts with API data
          this.$nextTick(() => {
            if (d.typeDistribution) this.updateTypeChart(d.typeDistribution)
            if (d.biddingDistribution) this.updateBiddingChart(d.biddingDistribution)
            if (d.supplierConcentration) this.updateSupplierChart(d.supplierConcentration)
          })
        }
        // Trend
        if (trendRes && trendRes.result === 200 && trendRes.data) {
          this.trendData = trendRes.data
          this.$nextTick(() => this.updateTrendChart(this.trendData))
        }
        // Warnings
        if (warnRes && warnRes.result === 200 && warnRes.data) {
          this.warningList = (warnRes.data || []).map(w => ({
            id: w.warningNo || w.id,
            level: w.warningLevel || 'MEDIUM',
            title: w.title,
            time: w.warningTime,
            company: w.companyName,
          }))
        }
      } catch (e) {
        console.error('采购驾驶舱数据加载失败', e)
        this.$message.error('数据加载失败，请稍后重试')
      }
    },
    handleResize() { this.charts.forEach(c => c.resize()) },
    initCharts() {
      this.initTrend()
      this.initType()
      this.initSupplier()
      this.initBidding()
    },
    initTrend() {
      const c = echarts.init(this.$refs.trendChart)
      this.charts.push(c)
      const months = this.trendData.map(t => t.month || t.date)
      const totalVals = this.trendData.map(t => t.totalAmount || 0)
      const relatedVals = this.trendData.map(t => t.relatedAmount || 0)
      const compliantVals = this.trendData.map(t => t.compliantAmount || 0)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['采购总额', '关联采购', '合规采购'], top: 4 },
        grid: { left: 40, right: 20, top: 36, bottom: 30 },
        xAxis: { type: 'category', data: months },
        yAxis: { type: 'value', name: '亿元' },
        series: [
          { name: '采购总额', type: 'line', data: totalVals, itemStyle: { color: this.ipSecondary }, smooth: true },
          { name: '关联采购', type: 'line', data: relatedVals, itemStyle: { color: '#722ED1' }, smooth: true },
          { name: '合规采购', type: 'line', data: compliantVals, itemStyle: { color: '#52C41A' }, smooth: true },
        ],
      })
    },
    updateTrendChart(data) {
      const chart = this.charts[0]
      if (!chart || !data || !data.length) return
      const months = data.map(d => d.month)
      const totalAmounts = data.map(d => d.totalAmount)
      const relatedAmounts = data.map(d => d.relatedAmount)
      const compliantAmounts = data.map(d => d.compliantAmount)
      chart.setOption({
        xAxis: { data: months },
        series: [
          { name: '采购总额', data: totalAmounts },
          { name: '关联采购', data: relatedAmounts },
          { name: '合规采购', data: compliantAmounts },
        ],
      })
    },
    initType() {
      const c = echarts.init(this.$refs.typeChart)
      this.charts.push(c)
      const typeMap = { ENGINEERING: '工程类', GOODS: '货物类', SERVICE: '服务类', IT: 'IT类' }
      const colorMap = { ENGINEERING: this.ipSecondary, GOODS: this.ipBright, SERVICE: '#69B1FF', IT: '#FAAD14' }
      const dist = (this.dashboardData && this.dashboardData.typeDistribution) || []
      const pieData = dist.map(d => ({
        value: d.count || d.amount || 0,
        name: typeMap[d.type] || d.type,
        itemStyle: { color: colorMap[d.type] || '#D9D9D9' },
      }))
      c.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 4 },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '46%'],
          data: pieData,
          label: { formatter: '{b}\n{d}%' },
        }],
      })
    },
    updateTypeChart(data) {
      const chart = this.charts[1]
      if (!chart || !data) return
      const typeNameMap = { ENGINEERING: '工程类', GOODS: '货物类', SERVICE: '服务类', IT: 'IT类', engineering: '工程类', goods: '货物类', service: '服务类', it: 'IT类' }
      const colors = [this.ipSecondary, this.ipBright, '#69B1FF', '#FAAD14', '#D9D9D9']
      const pieData = data.map((item, idx) => ({
        value: item.value,
        name: typeNameMap[item.name] || item.name,
        itemStyle: { color: colors[idx % colors.length] },
      }))
      chart.setOption({ series: [{ data: pieData }] })
    },
    initSupplier() {
      const c = echarts.init(this.$refs.supplierChart)
      this.charts.push(c)
      c.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 130, right: 30, top: 10, bottom: 30 },
        xAxis: { type: 'value', name: '亿元' },
        yAxis: { type: 'category', data: [], axisLabel: { fontSize: 11 } },
        series: [{
          type: 'bar',
          data: [],
          itemStyle: {
            color: (params) => params.dataIndex >= 8 ? '#CF1322' : (params.dataIndex >= 6 ? '#FA8C16' : this.ipSecondary),
          },
          label: { show: true, position: 'right', formatter: '{c}' },
        }],
      })
    },
    updateSupplierChart(data) {
      const chart = this.charts[2]
      if (!chart || !data || !data.length) return
      const names = data.map(d => d.name).reverse()
      const values = data.map(d => d.value).reverse()
      chart.setOption({
        yAxis: { data: names },
        series: [{ data: values }],
      })
    },
    initBidding() {
      const c = echarts.init(this.$refs.biddingChart)
      this.charts.push(c)
      const methodDist = (this.dashboardData && (this.dashboardData.biddingDistribution || this.dashboardData.biddingMethodDistribution)) || []
      const colorMap = { '公开招标': '#52C41A', '竞争性谈判': '#1677FF', '竞争性磋商': '#FAAD14', '单一来源': '#FA8C16', '询价采购': '#CF1322' }
      const pieData = methodDist.map(d => ({
        value: d.count || d.value || 0,
        name: d.method || d.name,
        itemStyle: { color: colorMap[d.method || d.name] || '#D9D9D9' },
      }))
      c.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 4 },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '46%'],
          data: pieData,
          label: { formatter: '{b}\n{d}%' },
        }],
      })
    },
    updateBiddingChart(data) {
      const chart = this.charts[3]
      if (!chart || !data) return
      const biddingNameMap = { 'PUBLIC_BIDDING': '公开招标', 'COMPETITIVE_NEGOTIATION': '竞争性谈判', 'COMPETITIVE_CONSULTATION': '竞争性磋商', 'SINGLE_SOURCE': '单一来源', 'INQUIRY': '询价采购', 'public_bidding': '公开招标', 'competitive_negotiation': '竞争性谈判', 'competitive_consultation': '竞争性磋商', 'single_source': '单一来源', 'inquiry': '询价采购' }
      const colors = ['#52C41A', '#1677FF', '#FAAD14', '#FA8C16', '#CF1322']
      const pieData = data.map((item, idx) => ({
        value: item.value,
        name: biddingNameMap[item.name] || item.name,
        itemStyle: { color: colors[idx % colors.length] },
      }))
      chart.setOption({ series: [{ data: pieData }] })
    },
  },
}
</script>

<style lang="scss" scoped>
.procurement-dashboard {
  padding: 16px;
  background: #F0F2F5;
  min-height: calc(100vh - 84px);
}
.page-header {
  padding: 20px 28px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px;
  color: #fff;
  .page-title {
    margin: 0 0 6px 0;
    font-size: 20px;
    font-weight: 700;
    i { margin-right: 8px; }
  }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card { border-radius: 6px; }
.stat-inner {
  display: flex;
  align-items: center;
  gap: 10px;
  .stat-icon-wrap {
    width: 44px;
    height: 44px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 22px;
    flex-shrink: 0;
  }
  .stat-value { font-size: 20px; font-weight: 700; }
  .stat-label { font-size: 12px; color: #8C8C8C; margin-top: 2px; }
}
.chart-row { margin-bottom: 16px; }
.chart-box { height: 260px; }
.card-hd {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  i { margin-right: 6px; }
}
.warning-table-card { margin-top: 0; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .el-card__header { padding: 12px 16px; border-bottom: 1px solid #F0F2F5; }
</style>
