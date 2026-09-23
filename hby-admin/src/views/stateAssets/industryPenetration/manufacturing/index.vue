<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + ipPrimary + ' 0%, ' + ipSecondary + ' 60%, ' + ipBright + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-tools"></i><span>制造行业监管</span></div>
      <div class="page-header-desc">制造竞争力 · 研发投入强度 · 技术自主化 · 智能制造转型进度监控</div>
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
          <div slot="header" class="card-header-title"><i class="el-icon-s-tools" style="color:#52C41A"></i> 制造细分行业营收对比</div>
          <div ref="mfgBarChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-data-line" style="color:#52C41A"></i> 研发投入强度趋势（近5年）</div>
          <div ref="rdTrendChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" style="display:flex;justify-content:space-between;align-items:center">
        <span class="card-header-title">制造企业明细列表</span>
        <el-form :model="queryForm" :inline="true" size="small" style="margin:0">
          <el-form-item label="企业名称" style="margin-bottom:0">
            <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:140px" />
          </el-form-item>
          <el-form-item label="制造类型" style="margin-bottom:0">
            <el-select v-model="queryForm.mfgType" placeholder="全部" clearable style="width:120px">
              <el-option label="精密制造" value="精密制造" />
              <el-option label="装备制造" value="装备制造" />
              <el-option label="化工材料" value="化工材料" />
              <el-option label="电子信息" value="电子信息" />
            </el-select>
          </el-form-item>
          <el-form-item style="margin-bottom:0">
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="small" @click="queryForm = { companyName: '', mfgType: '' }">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="filteredList" border size="small" v-loading="loading"
        :header-cell-style="{ background: '#F6FFED', color: '#237804' }" style="width:100%">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="制造类型" prop="mfgType" width="105" align="center">
          <template slot-scope="s"><el-tag type="success" size="small">{{ s.row.mfgType }}</el-tag></template>
        </el-table-column>
        <el-table-column label="年营收(亿元)" prop="revenue" width="120" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.revenue }}</span></template>
        </el-table-column>
        <el-table-column label="研发投入强度%" prop="rdIntensity" width="130" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.rdIntensity < 3 ? '#FA8C16' : '#303133', fontWeight: 600 }">
              {{ s.row.rdIntensity }}
              <i v-if="s.row.rdIntensity < 3" class="el-icon-warning-outline" style="color:#FA8C16"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="智能制造等级" prop="smartLevel" width="115" align="center">
          <template slot-scope="s">
            <el-tag :type="{ L4: 'success', L3: 'primary', L2: 'warning', L1: 'info' }[s.row.smartLevel] || 'info'" size="small">{{ s.row.smartLevel }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="核心技术自主率%" prop="techSelfRate" width="135" align="center">
          <template slot-scope="s">
            <el-progress :percentage="s.row.techSelfRate || 0" :stroke-width="8"
              :color="(s.row.techSelfRate || 0) >= 70 ? '#52C41A' : (s.row.techSelfRate || 0) >= 50 ? '#FA8C16' : '#F5222D'" />
          </template>
        </el-table-column>
        <el-table-column label="竞争力" prop="competitiveness" width="85" align="center">
          <template slot-scope="s">
            <el-tag v-if="s.row.competitiveness" :type="{ HIGH: 'success', MEDIUM: 'warning', LOW: 'danger' }[s.row.competitiveness]" size="small">{{ { HIGH: '强', MEDIUM: '中', LOW: '弱' }[s.row.competitiveness] }}</el-tag>
            <span v-else style="color:#C0C4CC">-</span>
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
import { getManufacturingList, getManufacturingRdStats } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryManufacturing',
  mixins: [investThemeMixin],
  computed: {
    filteredList() {
      let d = this.list
      if (this.queryForm.companyName) d = d.filter(r => r.companyName.includes(this.queryForm.companyName))
      if (this.queryForm.mfgType) d = d.filter(r => r.mfgType === this.queryForm.mfgType)
      return d
    },
    kpiCards() {
      const d = this.list
      const avgRd = d.length ? (d.reduce((s, r) => s + r.rdIntensity, 0) / d.length).toFixed(1) : 0
      const smartHigh = d.filter(r => r.smartLevel === 'L4' || r.smartLevel === 'L3').length
      return [
        { label: '制造企业数', value: d.length + '家', icon: 'el-icon-s-tools', bgColor: '#F6FFED', color: '#52C41A' },
        { label: '制造总营收', value: d.reduce((s, r) => s + r.revenue, 0).toFixed(1) + '亿', icon: 'el-icon-money', bgColor: '#F6FFED', color: '#52C41A' },
        { label: '平均研发投入强度', value: avgRd + '%', icon: 'el-icon-cpu', bgColor: avgRd < 3 ? '#FFF7E6' : '#F6FFED', color: avgRd < 3 ? '#FA8C16' : '#52C41A', valColor: avgRd < 3 ? '#FA8C16' : '#52C41A' },
        { label: 'L3+级智能制造', value: smartHigh + '家', icon: 'el-icon-magic-stick', bgColor: this.ipLightBg, color: this.ipSecondary },
      ]
    },
  },
  data() {
    return { loading: false, list: [], queryForm: { companyName: '', mfgType: '' } }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getManufacturingList(this.queryForm)
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initBarChart(); this.initRdTrend() })
      }
    },
    handleQuery() { this.$forceUpdate() },
    async initBarChart() {
      let chartData = null
      try {
        const res = await getManufacturingRdStats()
        if (res && res.result === 200 && res.data && res.data.barData) chartData = res.data.barData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.mfgBarChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        grid: { top: 20, bottom: 30, left: 80, right: 20 },
        xAxis: { type: 'value', name: '亿元', splitLine: { lineStyle: { color: '#f0f2f5' } } },
        yAxis: { type: 'category', data: chartData ? chartData.yAxis : [] },
        series: chartData ? chartData.series : [{ type: 'bar', barMaxWidth: 28, label: { show: true, position: 'right', formatter: '{c}亿' }, data: [] }],
      })
    },
    async initRdTrend() {
      let chartData = null
      try {
        const res = await getManufacturingRdStats()
        if (res && res.result === 200 && res.data && res.data.trendData) chartData = res.data.trendData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.rdTrendChart)
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
</style>
