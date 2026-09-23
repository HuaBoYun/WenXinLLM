<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-map-location"></i><span>国别风险地图</span></div>
      <div class="page-header-desc">全球国别风险评估、政治经济汇率合规多维度分析</div>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="6" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" :body-style="{ padding: '16px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }"><i :class="card.icon" :style="{ color: card.color }"></i></div>
            <div class="stat-info"><div class="stat-value" :style="card.danger ? 'color:#F5222D' : ''">{{ card.value }}</div><div class="stat-label">{{ card.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-bottom: 16px">
      <div slot="header">
        <span>全球国别风险分布图</span>
        <span style="float:right;font-size:12px;color:#909399">
          <i style="color:#52C41A">●</i> 低风险 &nbsp;
          <i style="color:#FAAD14">●</i> 中风险 &nbsp;
          <i style="color:#F5222D">●</i> 高风险
        </span>
      </div>
      <div ref="riskChart" class="risk-chart"></div>
    </el-card>

    <el-card shadow="never">
      <div slot="header"><span>国别风险明细</span></div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="国家/地区" prop="country" min-width="140" />
        <el-table-column label="风险等级" min-width="110" align="center">
          <template slot-scope="scope"><el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="small">{{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[scope.row.riskLevel] || '-' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="企业数量" prop="investUnitCount" min-width="100" align="center" />
        <el-table-column label="投资金额(万$)" prop="investAmount" min-width="140" align="right" />
        <el-table-column label="政治风险" prop="politicalRisk" min-width="130" align="center">
          <template slot-scope="scope"><el-progress :percentage="scope.row.politicalRisk" :color="riskColor(scope.row.politicalRisk)" :show-text="false" style="width:60px;display:inline-block" /><span style="margin-left:4px;font-size:12px">{{ scope.row.politicalRisk }}</span></template>
        </el-table-column>
        <el-table-column label="经济风险" prop="economicRisk" min-width="130" align="center">
          <template slot-scope="scope"><el-progress :percentage="scope.row.economicRisk" :color="riskColor(scope.row.economicRisk)" :show-text="false" style="width:60px;display:inline-block" /><span style="margin-left:4px;font-size:12px">{{ scope.row.economicRisk }}</span></template>
        </el-table-column>
        <el-table-column label="法律风险" prop="legalRisk" min-width="100" align="center">
          <template slot-scope="scope"><span>{{ scope.row.legalRisk }}</span></template>
        </el-table-column>
        <el-table-column label="安全风险" prop="securityRisk" min-width="100" align="center">
          <template slot-scope="scope"><span>{{ scope.row.securityRisk }}</span></template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; loadData() }" @current-change="val => { queryForm.pageNumber = val; loadData() }" />
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCountryRiskList, getCountryRiskStats } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasCountryRiskMap',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, charts: [], list: [], total: 0,
      queryForm: { pageNumber: 1, pageSize: 10 },
      statCards: [
        { label: '涉及国家数', value: '-', icon: 'el-icon-location', color: null, bg: null },
        { label: '高风险国家', value: '-', icon: 'el-icon-warning', color: '#F5222D', bg: '#FFF1F0', danger: true },
        { label: '境外企业数', value: '-', icon: 'el-icon-office-building', color: '#FA8C16', bg: '#FFF7E6' },
        { label: '投资总额(亿$)', value: '-', icon: 'el-icon-coin', color: '#52C41A', bg: '#F6FFED' }
      ]
    }
  },
  created() {
    this.statCards[0].color = this.ipBright
    this.statCards[0].bg = this.ipLightBg
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async loadData() {
      try {
        const [statsRes, listRes] = await Promise.all([getCountryRiskStats(), getCountryRiskList(this.queryForm)])
        if (statsRes && statsRes.result === 200 && statsRes.data) {
          const d = statsRes.data
          if (d.countryCount != null) this.statCards[0].value = d.countryCount
          if (d.highRiskCount != null) this.statCards[1].value = d.highRiskCount
          if (d.investUnitCount != null) this.statCards[2].value = d.investUnitCount
          if (d.investTotal != null) this.statCards[3].value = d.investTotal
        }
        if (listRes && listRes.result === 200) {
          this.list = (listRes.data && listRes.data.tlist) || []
          this.total = (listRes.data && listRes.data.totalRecord) || 0
        }
        // Fallback: compute stats from list data if stats endpoint didn't populate them
        if (this.list.length > 0) {
          if (this.statCards[0].value === '-') {
            const countries = new Set(this.list.map(r => r.country))
            this.statCards[0].value = countries.size
          }
          if (this.statCards[1].value === '-') {
            this.statCards[1].value = this.list.filter(r => r.riskLevel === 'HIGH').length
          }
          if (this.statCards[2].value === '-') {
            this.statCards[2].value = this.list.reduce((sum, r) => sum + (parseInt(r.investUnitCount) || 0), 0)
          }
          if (this.statCards[3].value === '-') {
            const investTotalRaw = this.list.reduce((sum, r) => sum + (parseFloat(r.investAmount) || 0), 0)
            this.statCards[3].value = (investTotalRaw / 10000).toFixed(2)
          }
        }
      } catch (e) { console.warn('国别风险数据加载失败', e) }
      this.$nextTick(() => { this.initRiskChart() })
    },
    initRiskChart() {
      const c = echarts.init(this.$refs.riskChart); this.charts.push(c)
      const countries = this.list.map(d => d.country).reverse()
      const reversedData = this.list.slice().reverse()
      c.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['政治风险', '经济风险', '法律风险', '安全风险'], top: 10 },
        grid: { left: 80, right: 20, bottom: 30, top: 50 },
        xAxis: { type: 'value', max: 100, name: '风险评分(0-100)' },
        yAxis: { type: 'category', data: countries },
        series: [
          { name: '政治风险', type: 'bar', stack: 'risk', data: reversedData.map(d => d.politicalRisk), itemStyle: { color: '#F5222D' } },
          { name: '经济风险', type: 'bar', stack: 'risk', data: reversedData.map(d => d.economicRisk), itemStyle: { color: '#FA8C16' } },
          { name: '法律风险', type: 'bar', stack: 'risk', data: reversedData.map(d => d.legalRisk), itemStyle: { color: '#FAAD14' } },
          { name: '安全风险', type: 'bar', stack: 'risk', data: reversedData.map(d => d.securityRisk), itemStyle: { color: this.ipBright } }
        ]
      })
    },
    riskColor(v) { return v >= 20 ? '#F5222D' : v >= 10 ? '#FAAD14' : '#52C41A' }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
.risk-chart { height: 360px; width: 100%; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-card { border-radius: 6px; }
</style>
