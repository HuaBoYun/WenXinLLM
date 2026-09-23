<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header" :style="{ background: `linear-gradient(135deg, ${ipPrimary} 0%, ${ipSecondary} 60%, ${ipBright} 100%)` }">
      <div class="page-header-left"><i class="el-icon-house"></i><span>基础设施监管</span></div>
      <div class="page-header-desc">资产回报率 · 债务风险 · 工程质量 · 安全生产监控</div>
    </div>

    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6" v-for="k in kpiCards" :key="k.label">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: k.bgColor }">
              <i :class="k.icon" :style="{ color: k.color, fontSize: '26px' }"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-value" :style="{ color: k.valColor || '#303133' }">{{ k.value }}</div>
              <div class="kpi-label">{{ k.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="14" style="margin-bottom:14px">
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-pie-chart" :style="{ color: ipSecondary }"></i> 基础设施分类资产规模分布</div>
          <div ref="infraDistChart" style="height:280px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-data-line" :style="{ color: ipSecondary }"></i> 近5年资产回报率趋势</div>
          <div ref="returnTrendChart" style="height:280px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" class="card-header-title">基础设施企业明细列表</div>
      <el-table :data="list" border size="small" v-loading="loading"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }" style="width:100%"
        :row-class-name="tableRowClass">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="基础设施类型" prop="infraType" width="120" align="center">
          <template slot-scope="s"><el-tag type="primary" size="small">{{ s.row.infraType }}</el-tag></template>
        </el-table-column>
        <el-table-column label="资产总额(亿元)" prop="totalAssets" width="130" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.totalAssets }}</span></template>
        </el-table-column>
        <el-table-column label="资产回报率%" prop="assetReturn" width="120" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.assetReturn < 4 ? '#FA8C16' : '#303133', fontWeight: 600 }">
              {{ s.row.assetReturn }}<i v-if="s.row.assetReturn < 4" class="el-icon-warning-outline" style="color:#FA8C16;margin-left:3px"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="资产负债率%" prop="debtRatio" width="120" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.debtRatio > 65 ? '#F5222D' : '#303133', fontWeight: 600 }">
              {{ s.row.debtRatio }}<i v-if="s.row.debtRatio > 65" class="el-icon-warning" style="color:#F5222D;margin-left:3px"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="安全生产评级" prop="safetyRating" width="120" align="center">
          <template slot-scope="s">
            <el-tag :type="{ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[s.row.safetyRating]" size="small">{{ s.row.safetyRating }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="重大安全隐患数" prop="majorRisks" width="130" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.majorRisks > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.majorRisks }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="85" align="center">
          <template slot-scope="s">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[s.row.riskLevel]" size="small">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[s.row.riskLevel] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getInfrastructureList, getInfrastructureStats } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryInfrastructure',
  mixins: [investThemeMixin],
  computed: {
    kpiCards() {
      const d = this.list
      const avgReturn = d.length ? (d.reduce((s, r) => s + r.assetReturn, 0) / d.length).toFixed(1) : 0
      const avgDebt = d.length ? (d.reduce((s, r) => s + r.debtRatio, 0) / d.length).toFixed(1) : 0
      return [
        { label: '基础设施企业数', value: d.length + '家', icon: 'el-icon-house', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '资产总规模', value: d.reduce((s, r) => s + r.totalAssets, 0).toFixed(1) + '亿', icon: 'el-icon-s-finance', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '平均资产回报率', value: avgReturn + '%', icon: 'el-icon-trend-charts', bgColor: avgReturn < 4 ? '#FFF7E6' : '#F6FFED', color: avgReturn < 4 ? '#FA8C16' : '#52C41A', valColor: avgReturn < 4 ? '#FA8C16' : '#52C41A' },
        { label: '平均资产负债率', value: avgDebt + '%', icon: 'el-icon-warning-outline', bgColor: avgDebt > 65 ? '#FFF1F0' : '#FFF7E6', color: avgDebt > 65 ? '#F5222D' : '#FA8C16', valColor: avgDebt > 65 ? '#F5222D' : '#FA8C16' },
      ]
    },
  },
  data() {
    return { loading: false, list: [] }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getInfrastructureList({})
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initDist(); this.initReturnTrend() })
      }
    },
    tableRowClass({ row }) {
      if (row.debtRatio > 65 || row.majorRisks > 1) return 'row-infra-risk'
      return ''
    },
    async initDist() {
      let chartData = null
      try {
        const res = await getInfrastructureStats()
        if (res && res.result === 200 && res.data && res.data.distData) chartData = res.data.distData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.infraDistChart)
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}亿 ({d}%)' },
        legend: { orient: 'vertical', right: 8, top: 'center' },
        series: [{
          type: 'pie', radius: ['35%', '65%'], center: ['40%', '50%'],
          data: chartData ? chartData.pieData : [],
          label: { show: false },
        }],
      })
    },
    async initReturnTrend() {
      let chartData = null
      try {
        const res = await getInfrastructureStats()
        if (res && res.result === 200 && res.data && res.data.trendData) chartData = res.data.trendData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.returnTrendChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 11 } },
        grid: { top: 20, bottom: 44, left: 45, right: 20 },
        xAxis: { type: 'category', data: chartData ? chartData.xAxis : [] },
        yAxis: { type: 'value', name: '%', splitLine: { lineStyle: { color: '#f0f2f5' } } },
        series: chartData ? chartData.series : [],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.industry-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px;
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info .kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-info .kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.card-header-title { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .row-infra-risk td { background: #FFF1F0 !important; }
</style>
