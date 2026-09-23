<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-trophy"></i><span>行业竞争力分析</span></div>
      <div class="page-header-desc">五维度综合竞争力评分 — 研发投入(25%) · 市场份额(20%) · 盈利能力(20%) · 品牌价值(15%) · 创新能力(20%)</div>
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
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-trophy" style="color:#FAAD14"></i> 综合竞争力TOP排名</div>
          <div ref="rankChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-data-analysis" style="color:#FAAD14"></i> 各行业竞争力五维雷达</div>
          <div ref="compRadar" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" style="display:flex;justify-content:space-between;align-items:center">
        <span class="card-header-title">企业竞争力明细表</span>
        <el-form :model="queryForm" :inline="true" size="small" style="margin:0">
          <el-form-item label="企业名称" style="margin-bottom:0">
            <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:140px" />
          </el-form-item>
          <el-form-item label="所属行业" style="margin-bottom:0">
            <el-select v-model="queryForm.industry" placeholder="全部" clearable style="width:120px">
              <el-option label="能源行业" value="能源行业" />
              <el-option label="金融行业" value="金融行业" />
              <el-option label="制造行业" value="制造行业" />
            </el-select>
          </el-form-item>
          <el-form-item style="margin-bottom:0">
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="filteredList" border size="small" v-loading="loading"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }" style="width:100%">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="所属行业" prop="industry" width="105" align="center">
          <template slot-scope="s">
            <el-tag size="small" :style="industryTagStyle(s.row.industry)">{{ s.row.industry }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="综合评分" prop="totalScore" width="90" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.totalScore < 40 ? '#F5222D' : s.row.totalScore < 60 ? '#FA8C16' : '#52C41A', fontWeight: 700, fontSize: '15px' }">{{ s.row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="研发(25%)" prop="rdScore" width="95" align="center" />
        <el-table-column label="市场(20%)" prop="marketScore" width="95" align="center" />
        <el-table-column label="盈利(20%)" prop="profitScore" width="95" align="center" />
        <el-table-column label="品牌(15%)" prop="brandScore" width="95" align="center" />
        <el-table-column label="创新(20%)" prop="innovScore" width="95" align="center" />
        <el-table-column label="行业排名" prop="industryRank" width="85" align="center">
          <template slot-scope="s">
            <el-tag :type="s.row.industryRank <= 2 ? 'warning' : 'info'" size="small">第{{ s.row.industryRank }}名</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="趋势" prop="trend" width="80" align="center">
          <template slot-scope="s">
            <span :style="{ color: { UP: '#52C41A', DOWN: '#F5222D', STABLE: '#909399' }[s.row.trend] }">
              {{ { UP: '↑上升', DOWN: '↓下降', STABLE: '→持平' }[s.row.trend] }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCompetitivenessList, getCompetitivenessRanking } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryCompetitiveness',
  mixins: [investThemeMixin],
  computed: {
    filteredList() {
      let d = this.list
      if (this.queryForm.companyName) d = d.filter(r => r.companyName.includes(this.queryForm.companyName))
      if (this.queryForm.industry) d = d.filter(r => r.industry === this.queryForm.industry)
      return d
    },
    kpiCards() {
      const d = this.list
      const avg = d.length ? (d.reduce((s, r) => s + r.totalScore, 0) / d.length).toFixed(1) : 0
      return [
        { label: '参评企业数', value: d.length + '家', icon: 'el-icon-office-building', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '平均综合评分', value: avg + '分', icon: 'el-icon-trophy', bgColor: '#FFFBE6', color: '#FAAD14', valColor: '#FAAD14' },
        { label: '高竞争力企业(≥70分)', value: d.filter(r => r.totalScore >= 70).length + '家', icon: 'el-icon-star-on', bgColor: '#F6FFED', color: '#52C41A', valColor: '#52C41A' },
        { label: '低竞争力预警(<40分)', value: d.filter(r => r.totalScore < 40).length + '家', icon: 'el-icon-warning', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
      ]
    },
  },
  data() {
    return { loading: false, list: [], queryForm: { companyName: '', industry: '' } }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getCompetitivenessList(this.queryForm)
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initRankChart(); this.initRadar() })
      }
    },
    handleQuery() {
      this.fetchData()
    },
    handleReset() {
      this.queryForm = { companyName: '', industry: '' }
      this.fetchData()
    },
    async initRankChart() {
      let chartData = null
      try {
        const res = await getCompetitivenessRanking()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const sorted = chartData ? (chartData.rankList || chartData) : [...this.list].sort((a, b) => b.totalScore - a.totalScore).slice(0, 6)
      const c = echarts.init(this.$refs.rankChart)
      c.setOption({
        tooltip: { trigger: 'axis', formatter: params => `${params[0].name}: ${params[0].value}分` },
        grid: { top: 10, bottom: 20, left: 130, right: 60 },
        xAxis: { type: 'value', max: 100, splitLine: { lineStyle: { color: '#f0f2f5' } } },
        yAxis: { type: 'category', data: sorted.map(r => (r.companyName || '').replace('有限公司', '').replace('集团', '')) },
        series: [{
          type: 'bar', barMaxWidth: 28, label: { show: true, position: 'right', formatter: '{c}分' },
          data: sorted.map(r => ({
            value: r.totalScore,
            itemStyle: { color: r.totalScore >= 80 ? '#52C41A' : r.totalScore >= 60 ? '#FAAD14' : r.totalScore >= 40 ? '#FA8C16' : '#F5222D' },
          })),
        }],
      })
    },
    async initRadar() {
      let chartData = null
      try {
        const res = await getCompetitivenessRanking()
        if (res && res.result === 200 && res.data && res.data.radarData) chartData = res.data.radarData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.compRadar)
      c.setOption({
        tooltip: {},
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 11 } },
        radar: {
          indicator: chartData ? chartData.indicator : [{ name: '研发投入', max: 100 }, { name: '市场份额', max: 100 }, { name: '盈利能力', max: 100 }, { name: '品牌价值', max: 100 }, { name: '创新能力', max: 100 }],
          center: ['50%', '48%'], radius: 90, axisName: { color: '#606266', fontSize: 11 }, splitLine: { lineStyle: { color: '#e8e8e8' } },
        },
        series: [{
          type: 'radar',
          data: chartData ? chartData.series : [],
        }],
      })
    },
    industryTagStyle(v) {
      const map = {
        '能源行业': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '金融行业': { background: this.ipLightBg, color: this.ipBright, border: '1px solid #ADC6FF' },
        '制造行业': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '基础设施': { background: '#E8F4FF', color: this.ipSecondary, border: '1px solid #91CAFF' },
        '公共服务': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
      }
      return map[v] || {}
    },
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
  i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info .kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-info .kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.card-header-title { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-card { border-radius: 6px; }
</style>
