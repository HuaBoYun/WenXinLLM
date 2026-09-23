<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + ipPrimary + ' 0%, ' + ipSecondary + ' 60%, ' + ipBright + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-lightning"></i><span>能源行业监管</span></div>
      <div class="page-header-desc">能源安全保障 · 碳达峰碳中和转型 · 新旧能源结构调整监控</div>
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
          <div slot="header" class="card-header-title"><i class="el-icon-lightning" style="color:#FA8C16"></i> 新旧能源营收结构趋势（近5年）</div>
          <div ref="energyTrendChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-pie-chart" style="color:#FA8C16"></i> 能源细分行业营收分布</div>
          <div ref="energyDistChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" style="display:flex;justify-content:space-between;align-items:center">
        <span class="card-header-title">能源企业明细列表</span>
        <el-form :model="queryForm" :inline="true" size="small" style="margin:0">
          <el-form-item label="企业名称" style="margin-bottom:0">
            <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:140px" />
          </el-form-item>
          <el-form-item label="能源类型" style="margin-bottom:0">
            <el-select v-model="queryForm.energyType" placeholder="全部" clearable style="width:110px">
              <el-option label="传统能源" value="传统能源" />
              <el-option label="新能源" value="新能源" />
            </el-select>
          </el-form-item>
          <el-form-item style="margin-bottom:0">
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="small" @click="queryForm = { companyName: '', energyType: '' }">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="filteredList" border size="small" v-loading="loading"
        :header-cell-style="{ background: '#FFF7E6', color: '#d46b08' }" style="width:100%">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="能源类型" prop="energyType" width="100" align="center">
          <template slot-scope="s">
            <el-tag :type="s.row.energyType === '新能源' ? 'success' : 'warning'" size="small">{{ s.row.energyType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="细分类型" prop="subType" width="120" align="center" />
        <el-table-column label="年营收(亿元)" prop="revenue" width="120" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.revenue }}</span></template>
        </el-table-column>
        <el-table-column label="净利率(%)" prop="netMargin" width="100" align="center">
          <template slot-scope="s"><span :style="{ color: s.row.netMargin < 0 ? '#F5222D' : '#303133' }">{{ s.row.netMargin != null ? s.row.netMargin : '-' }}</span></template>
        </el-table-column>
        <el-table-column label="新能源转型率%" prop="renewableRatio" width="120" align="center">
          <template slot-scope="s">
            <el-progress :percentage="s.row.renewableRatio" :stroke-width="8"
              :color="s.row.renewableRatio >= 50 ? '#52C41A' : s.row.renewableRatio >= 20 ? '#FA8C16' : '#F5222D'" />
          </template>
        </el-table-column>
        <el-table-column label="碳排放强度(吨/万元)" prop="carbonIntensity" width="155" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.carbonIntensity > 1 ? '#F5222D' : s.row.carbonIntensity > 0.5 ? '#FA8C16' : '#303133', fontWeight: 600 }">{{ s.row.carbonIntensity }}</span>
          </template>
        </el-table-column>
        <el-table-column label="竞争力" prop="competitiveness" width="85" align="center">
          <template slot-scope="s">
            <el-tag v-if="s.row.competitiveness" :type="{ HIGH: 'success', MEDIUM: 'warning', LOW: 'danger' }[s.row.competitiveness]" size="small">{{ { HIGH: '强', MEDIUM: '中', LOW: '弱' }[s.row.competitiveness] || '-' }}</el-tag>
            <span v-else>-</span>
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
import { getEnergyList, getEnergyTransitionStats } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryEnergy',
  mixins: [investThemeMixin],
  computed: {
    filteredList() {
      let d = this.list
      if (this.queryForm.companyName) d = d.filter(r => r.companyName.includes(this.queryForm.companyName))
      if (this.queryForm.energyType) d = d.filter(r => r.energyType === this.queryForm.energyType)
      return d
    },
    kpiCards() {
      const d = this.list
      const renewAvg = d.length ? (d.reduce((s, r) => s + r.renewableRatio, 0) / d.length).toFixed(1) : 0
      return [
        { label: '能源企业数', value: d.length + '家', icon: 'el-icon-lightning', bgColor: '#FFF7E6', color: '#FA8C16' },
        { label: '能源总营收', value: d.reduce((s, r) => s + r.revenue, 0).toFixed(1) + '亿', icon: 'el-icon-money', bgColor: '#FFF7E6', color: '#FA8C16' },
        { label: '新能源平均占比', value: renewAvg + '%', icon: 'el-icon-sunny', bgColor: '#F6FFED', color: '#52C41A', valColor: '#52C41A' },
        { label: '高风险企业', value: d.filter(r => r.riskLevel === 'HIGH').length + '家', icon: 'el-icon-warning', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
      ]
    },
  },
  data() {
    return { loading: false, list: [], queryForm: { companyName: '', energyType: '' } }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getEnergyList(this.queryForm)
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initTrend(); this.initDist() })
      }
    },
    handleQuery() { this.$forceUpdate() },
    async initTrend() {
      let chartData = null
      try {
        const res = await getEnergyTransitionStats()
        if (res && res.result === 200 && res.data) chartData = res.data
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.energyTrendChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: chartData ? chartData.legend : [], bottom: 0 },
        grid: { top: 20, bottom: 44, left: 55, right: 60 },
        xAxis: { type: 'category', data: chartData ? chartData.xAxis : [] },
        yAxis: [
          { type: 'value', name: '亿元', splitLine: { lineStyle: { color: '#f0f2f5' } } },
          { type: 'value', name: '%', min: 0, max: 100, splitLine: { show: false } },
        ],
        series: chartData ? chartData.series : [],
      })
    },
    async initDist() {
      let chartData = null
      try {
        const res = await getEnergyTransitionStats()
        if (res && res.result === 200 && res.data && res.data.distData) chartData = res.data.distData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.energyDistChart)
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
</style>
