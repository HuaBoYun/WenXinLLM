<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg,' + ipPrimary + ' 0%,' + ipSecondary + ' 60%,#40a9ff 100%)' }">
      <div class="page-header-left"><i class="el-icon-bank"></i><span>金融行业监管</span></div>
      <div class="page-header-desc">金融风险防控 · 不良资产管理 · 资本充足率监控 · 防止脱实向虚</div>
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
          <div slot="header" class="card-header-title"><i class="el-icon-bank" :style="{ color: ipBright }"></i> 金融行业资产规模趋势（近5年）</div>
          <div ref="finTrendChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-warning" style="color:#F5222D"></i> 金融子行业不良率对比</div>
          <div ref="badDebtChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" style="display:flex;justify-content:space-between;align-items:center">
        <span class="card-header-title">金融企业明细列表</span>
        <el-form :model="queryForm" :inline="true" size="small" style="margin:0">
          <el-form-item label="企业名称" style="margin-bottom:0">
            <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:140px" />
          </el-form-item>
          <el-form-item label="金融子类" style="margin-bottom:0">
            <el-select v-model="queryForm.finType" placeholder="全部" clearable style="width:120px">
              <el-option label="商业银行" value="商业银行" />
              <el-option label="保险" value="保险" />
              <el-option label="证券" value="证券" />
              <el-option label="基金" value="基金" />
              <el-option label="融资租赁" value="融资租赁" />
            </el-select>
          </el-form-item>
          <el-form-item style="margin-bottom:0">
            <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" size="small" @click="queryForm = { companyName: '', finType: '' }">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="filteredList" border size="small" v-loading="loading"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }" style="width:100%"
        :row-class-name="tableRowClass">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="金融子类型" prop="finType" width="110" align="center">
          <template slot-scope="s">
            <el-tag type="primary" size="small">{{ s.row.finType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="总资产(亿元)" prop="totalAssets" width="120" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.totalAssets }}</span></template>
        </el-table-column>
        <el-table-column label="不良资产率%" prop="badDebtRatio" width="120" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.badDebtRatio > 3 ? '#F5222D' : '#303133', fontWeight: 600 }">
              {{ s.row.badDebtRatio }}
              <i v-if="s.row.badDebtRatio > 3" class="el-icon-warning" style="color:#F5222D"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="资本充足率%" prop="capitalRatio" width="120" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.capitalRatio < 10.5 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.capitalRatio }}</span>
          </template>
        </el-table-column>
        <el-table-column label="净利率%" prop="netMargin" width="90" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.netMargin < 0 ? '#F5222D' : '#303133' }">{{ s.row.netMargin != null ? s.row.netMargin : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" prop="complianceStatus" width="100" align="center">
          <template slot-scope="s">
            <el-tag :type="{ COMPLIANT: 'success', WARNING: 'warning', VIOLATION: 'danger', '合规': 'success', '整改中': 'warning', '违规': 'danger' }[s.row.complianceStatus] || 'info'" size="small">
              {{ { COMPLIANT: '合规', WARNING: '警示', VIOLATION: '违规' }[s.row.complianceStatus] || s.row.complianceStatus || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="85" align="center">
          <template slot-scope="s">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[s.row.riskLevel]" size="small">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[s.row.riskLevel] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-row style="margin-top:8px;color:#909399;font-size:12px">
      <el-col><i class="el-icon-warning" style="color:#F5222D;margin-right:4px"></i>不良资产率 > 3% 或 资本充足率 &lt; 10.5% 的行标红色高亮</el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getFinancialList, getFinancialRiskIndicators } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryFinancial',
  mixins: [investThemeMixin],
  computed: {
    filteredList() {
      let d = this.list
      if (this.queryForm.companyName) d = d.filter(r => r.companyName.includes(this.queryForm.companyName))
      if (this.queryForm.finType) d = d.filter(r => r.finType === this.queryForm.finType)
      return d
    },
    kpiCards() {
      const d = this.list
      const avgBad = d.length ? (d.reduce((s, r) => s + r.badDebtRatio, 0) / d.length).toFixed(2) : 0
      const avgCap = d.length ? (d.reduce((s, r) => s + r.capitalRatio, 0) / d.length).toFixed(1) : 0
      return [
        { label: '金融企业数', value: d.length + '家', icon: 'el-icon-bank', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '金融总资产', value: d.reduce((s, r) => s + r.totalAssets, 0).toFixed(1) + '亿', icon: 'el-icon-money', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '平均不良资产率', value: avgBad + '%', icon: 'el-icon-warning-outline', bgColor: avgBad > 3 ? '#FFF1F0' : '#FFF7E6', color: avgBad > 3 ? '#F5222D' : '#FA8C16', valColor: avgBad > 3 ? '#F5222D' : '#FA8C16' },
        { label: '平均资本充足率', value: avgCap + '%', icon: 'el-icon-shield', bgColor: '#F6FFED', color: '#52C41A', valColor: '#52C41A' },
      ]
    },
  },
  data() {
    return { loading: false, list: [], queryForm: { companyName: '', finType: '' } }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getFinancialList(this.queryForm)
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initTrend(); this.initBadDebt() })
      }
    },
    handleQuery() { this.$forceUpdate() },
    tableRowClass({ row }) {
      if (row.badDebtRatio > 3 || row.capitalRatio < 10.5) return 'row-fin-risk'
      return ''
    },
    async initTrend() {
      let chartData = null
      try {
        const res = await getFinancialRiskIndicators()
        if (res && res.result === 200 && res.data && res.data.trendData) chartData = res.data.trendData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.finTrendChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: chartData ? chartData.legend : [], bottom: 0, textStyle: { fontSize: 11 } },
        grid: { top: 20, bottom: 44, left: 55, right: 20 },
        xAxis: { type: 'category', data: chartData ? chartData.xAxis : [] },
        yAxis: { type: 'value', name: '亿元', splitLine: { lineStyle: { color: '#f0f2f5' } } },
        series: chartData ? chartData.series : [],
      })
    },
    async initBadDebt() {
      let chartData = null
      try {
        const res = await getFinancialRiskIndicators()
        if (res && res.result === 200 && res.data && res.data.badDebtData) chartData = res.data.badDebtData
      } catch (e) { /* ignore */ }
      const c = echarts.init(this.$refs.badDebtChart)
      c.setOption({
        tooltip: { trigger: 'axis', formatter: params => params.map(p => `${p.name}: ${p.value}%`).join('<br/>') },
        grid: { top: 20, bottom: 30, left: 70, right: 50 },
        xAxis: { type: 'value', name: '%', max: 5, splitLine: { lineStyle: { color: '#f0f2f5' } } },
        yAxis: { type: 'category', data: chartData ? chartData.yAxis : [] },
        series: chartData ? chartData.series : [{ type: 'bar', data: [] },
          { type: 'line', markLine: { silent: true, data: [{ xAxis: 3 }], lineStyle: { color: '#F5222D', type: 'dashed' }, label: { formatter: '3%预警线', position: 'end' } } },
        ],
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
::v-deep .row-fin-risk td { background: #FFF1F0 !important; }
</style>
