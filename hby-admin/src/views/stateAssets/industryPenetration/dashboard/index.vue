<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-data-analysis"></i><span>行业监控驾驶舱</span></div>
      <div class="page-header-desc">行业穿透综合态势总览 — 行业分布结构、经营对比、风险热力与主业偏离</div>
    </div>

    <!-- KPI -->
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

    <!-- 第二行：营收趋势（左60%）+ 利润占比饼图（右40%）-->
    <el-row :gutter="14" style="margin-bottom:14px">
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">各行业营收趋势（近6年）</div>
          <div ref="revenueTrendChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">行业利润贡献占比</div>
          <div ref="profitPieChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：五维雷达（左40%）+ 风险热力矩阵（右60%）-->
    <el-row :gutter="14" style="margin-bottom:14px">
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">行业综合实力五维雷达</div>
          <div ref="radarChart" style="height:280px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">行业风险热力矩阵</div>
          <el-table :data="riskMatrix" size="small" border :header-cell-style="{ background: ipLightBg, color: ipSecondary }">
            <el-table-column label="行业" prop="industry" width="100" align="center" />
            <el-table-column label="政策风险" prop="policy" align="center">
              <template slot-scope="s"><div class="heat-cell" :style="heatStyle(s.row.policy)">{{ s.row.policy }}</div></template>
            </el-table-column>
            <el-table-column label="市场风险" prop="market" align="center">
              <template slot-scope="s"><div class="heat-cell" :style="heatStyle(s.row.market)">{{ s.row.market }}</div></template>
            </el-table-column>
            <el-table-column label="技术风险" prop="tech" align="center">
              <template slot-scope="s"><div class="heat-cell" :style="heatStyle(s.row.tech)">{{ s.row.tech }}</div></template>
            </el-table-column>
            <el-table-column label="合规风险" prop="compliance" align="center">
              <template slot-scope="s"><div class="heat-cell" :style="heatStyle(s.row.compliance)">{{ s.row.compliance }}</div></template>
            </el-table-column>
            <el-table-column label="综合" align="center">
              <template slot-scope="s">
                <el-tag :type="riskTagType(s.row.overall)" size="mini">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[s.row.overall] }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：主业非主业趋势（左50%）+ 竞争力雷达（右50%）-->
    <el-row :gutter="14">
      <el-col :span="12">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">主业/非主业占比趋势（近12月）</div>
          <div ref="mainBizTrendChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title">各行业竞争力指数雷达</div>
          <div ref="competitivenessRadar" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getIndustryDashboard, getIndustryRevenueTrend, getIndustryDistribution, getIndustryRiskMatrix, getIndustryDashboardGauge, getIndustryDashboardMainBiz } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryDashboard',
  mixins: [investThemeMixin],
  data() {
    return {
      kpiCards: [],
      riskMatrix: [],
    }
  },
  created() {
    this.kpiCards = [
      { label: '覆盖行业数', value: '-', icon: 'el-icon-s-grid', bgColor: this.ipLightBg, color: this.ipSecondary },
      { label: '纳管企业总数', value: '-', icon: 'el-icon-office-building', bgColor: this.ipLightBg, color: this.ipSecondary },
      { label: '高风险行业', value: '-', icon: 'el-icon-warning-outline', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
      { label: '非主业占比', value: '-', icon: 'el-icon-pie-chart', bgColor: '#FFF7E6', color: '#FA8C16', valColor: '#FA8C16' },
    ]
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await getIndustryDashboard()
        if (res && res.result === 200 && res.data) {
          if (res.data.kpiCards) this.kpiCards = res.data.kpiCards
          if (res.data.riskMatrix) this.riskMatrix = res.data.riskMatrix
        }
      } catch (e) { /* ignore */ }
      this.$nextTick(() => {
        this.initRevenueTrend()
        this.initProfitPie()
        this.initRadar()
        this.initMainBizTrend()
        this.initCompetitivenessRadar()
      })
    },
    async initRevenueTrend() {
      let chartData = null
      try {
        const res = await getIndustryRevenueTrend()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.revenueTrendChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 12 } },
        grid: { top: 20, bottom: 40, left: 50, right: 20 },
        xAxis: { type: 'category', data: chartData ? chartData.xAxis : [], axisLine: { lineStyle: { color: '#d9d9d9' } } },
        yAxis: { type: 'value', name: '亿元', axisLine: { show: false }, splitLine: { lineStyle: { color: '#f0f2f5' } } },
        series: chartData ? chartData.series : [],
      })
    },
    async initProfitPie() {
      let chartData = null
      try {
        const res = await getIndustryDistribution()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.profitPieChart)
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}亿 ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie', radius: ['40%', '68%'], center: ['40%', '50%'],
          data: chartData ? chartData.pieData : [],
          label: { show: false },
        }],
      })
    },
    async initRadar() {
      let chartData = null
      try {
        const res = await getIndustryDashboardGauge()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.radarChart)
      c.setOption({
        tooltip: {},
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 11 } },
        radar: {
          indicator: chartData ? chartData.indicator : [
            { name: '营收规模', max: 100 }, { name: '利润能力', max: 100 },
            { name: '竞争实力', max: 100 }, { name: '增长潜力', max: 100 }, { name: '风险可控', max: 100 },
          ],
          center: ['50%', '48%'], radius: 90,
          axisName: { color: '#606266', fontSize: 12 },
          splitLine: { lineStyle: { color: '#e8e8e8' } },
        },
        series: [{
          type: 'radar',
          data: chartData ? chartData.series : [],
        }],
      })
    },
    async initMainBizTrend() {
      let chartData = null
      try {
        const res = await getIndustryDashboardMainBiz()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.mainBizTrendChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: chartData ? chartData.legend : ['主业占比', '非主业占比', '20%预警线'], bottom: 0, textStyle: { fontSize: 12 } },
        grid: { top: 20, bottom: 40, left: 55, right: 20 },
        xAxis: { type: 'category', data: chartData ? chartData.xAxis : [], axisLabel: { rotate: 30, fontSize: 11 }, axisLine: { lineStyle: { color: '#d9d9d9' } } },
        yAxis: { type: 'value', name: '%', axisLine: { show: false }, splitLine: { lineStyle: { color: '#f0f2f5' } } },
        series: chartData ? chartData.series : [],
      })
    },
    async initCompetitivenessRadar() {
      let chartData = null
      try {
        const res = await getIndustryDashboardGauge()
        if (res && res.result === 200 && res.data && res.data.competitiveness) chartData = res.data.competitiveness
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.competitivenessRadar)
      c.setOption({
        tooltip: {},
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 11 } },
        radar: {
          indicator: chartData ? chartData.indicator : [
            { name: '研发投入', max: 100 }, { name: '市场份额', max: 100 },
            { name: '盈利能力', max: 100 }, { name: '品牌价值', max: 100 }, { name: '创新能力', max: 100 },
          ],
          center: ['50%', '48%'], radius: 90,
          axisName: { color: '#606266', fontSize: 12 },
          splitLine: { lineStyle: { color: '#e8e8e8' } },
        },
        series: [{
          type: 'radar',
          data: chartData ? chartData.series : [],
        }],
      })
    },
    heatStyle(val) {
      const alpha = Math.min(val / 40, 1)
      return { background: `rgba(245,34,45,${alpha * 0.55})`, color: val > 25 ? '#fff' : '#303133', fontWeight: '600', borderRadius: '4px', padding: '2px 6px', display: 'inline-block', minWidth: '36px', textAlign: 'center' }
    },
    riskTagType(v) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[v] || 'info' },
  },
}
</script>

<style lang="scss" scoped>
.industry-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; }
}
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info .kpi-value { font-size: 28px; font-weight: bold; line-height: 1; }
.kpi-info .kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.card-header-title { font-size: 14px; font-weight: 600; color: var(--ip-secondary, #0050A0); }
.heat-cell { text-align: center; }
::v-deep .el-card { border-radius: 6px; }
</style>
