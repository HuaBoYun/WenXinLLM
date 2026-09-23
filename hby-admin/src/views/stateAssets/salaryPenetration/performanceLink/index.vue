<template>
  <div class="sal-perf">
    <!-- 绿色Banner -->
    <div class="sal-banner-green" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">效益联动分析</div>
      <div class="banner-sub">核查"两增两不超"执行情况 · 工资增长率与效益增长率联动关系监控 · 防范效益脱钩风险</div>
      <div class="rule-tags">
        <span class="rule-tag">工资增长率 ≤ 劳动生产率增长率</span>
        <span class="rule-tag">联动系数 0.8~1.2 为合理区间</span>
        <span class="rule-tag">效益下降时工资总额原则上相应下降</span>
      </div>
    </div>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="年度">
          <el-select v-model="queryForm.year" clearable placeholder="全部年度" style="width:110px">
            <el-option label="2025年" value="2025"></el-option>
            <el-option label="2024年" value="2024"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="企业">
          <el-select v-model="queryForm.companyName" clearable placeholder="全部企业" style="width:140px">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- KPI卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card"><div class="kpi-label">{{ k.label }}</div><div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div></div>
      </el-col>
    </el-row>

    <!-- 散点图 + 历史趋势 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header">
            <span class="card-title">效益联动散点图</span>
            <span class="chart-tip">X轴=利润增长率，Y轴=工资增长率，对角线=完美联动</span>
          </div>
          <div ref="scatterChart" style="height:280px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">联动系数分布</span></div>
          <div ref="coeffChart" style="height:280px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 明细表 -->
    <el-card shadow="never" class="table-card">
      <div slot="header"><span class="card-title">各企业效益联动明细</span></div>
      <el-table :data="tableData" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="companyName" label="企业名称" width="140"></el-table-column>
        <el-table-column prop="wageGrowth" label="工资增长率" align="center">
          <template slot-scope="{ row }"><span :style="{ color: '#1677FF' }">{{ row.wageGrowth }}%</span></template>
        </el-table-column>
        <el-table-column prop="profitGrowth" label="利润增长率" align="center">
          <template slot-scope="{ row }"><span>{{ row.profitGrowth }}%</span></template>
        </el-table-column>
        <el-table-column prop="revenueGrowth" label="营收增长率" align="center">
          <template slot-scope="{ row }">{{ row.revenueGrowth }}%</template>
        </el-table-column>
        <el-table-column prop="laborProductivity" label="劳动生产率增长率" align="center">
          <template slot-scope="{ row }">{{ row.laborProductivity }}%</template>
        </el-table-column>
        <el-table-column prop="linkCoeff" label="联动系数" align="center">
          <template slot-scope="{ row }">
            <span :style="linkCoeffStyle(row.linkCoeff).style">{{ row.linkCoeff }}</span>
            <el-tag :type="linkCoeffStyle(row.linkCoeff).tagType" size="mini" style="margin-left:6px">{{ linkCoeffStyle(row.linkCoeff).label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" align="center" width="100">
          <template slot-scope="{ row }">
            <el-tag :type="complianceTag(row).type" size="mini">{{ complianceTag(row).text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="效益联动详情" :visible.sync="drawerVisible" size="480px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="工资增长率">{{ currentRow.wageGrowth }}%</el-descriptions-item>
          <el-descriptions-item label="利润增长率">{{ currentRow.profitGrowth }}%</el-descriptions-item>
          <el-descriptions-item label="营收增长率">{{ currentRow.revenueGrowth }}%</el-descriptions-item>
          <el-descriptions-item label="劳动生产率增长率">{{ currentRow.laborProductivity }}%</el-descriptions-item>
          <el-descriptions-item label="效益联动系数" :span="2">
            <span :style="linkCoeffStyle(currentRow.linkCoeff).style">{{ currentRow.linkCoeff }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <div class="compliance-check-box">
          <div class="check-title">合规核查结果</div>
          <div class="check-item" :class="{ 'check-pass': currentRow.wageGrowth <= currentRow.laborProductivity, 'check-fail': currentRow.wageGrowth > currentRow.laborProductivity }">
            <i :class="currentRow.wageGrowth <= currentRow.laborProductivity ? 'el-icon-circle-check' : 'el-icon-circle-close'"></i>
            工资增长率 ≤ 劳动生产率增长率：{{ currentRow.wageGrowth <= currentRow.laborProductivity ? '通过' : `不满足（差${(currentRow.wageGrowth - currentRow.laborProductivity).toFixed(1)}个百分点）` }}
          </div>
          <div class="check-item" :class="{ 'check-pass': currentRow.linkCoeff >= 0.8 && currentRow.linkCoeff <= 1.2, 'check-warn': currentRow.linkCoeff >= 0.6 && (currentRow.linkCoeff < 0.8 || currentRow.linkCoeff > 1.2), 'check-fail': currentRow.linkCoeff < 0.6 || currentRow.linkCoeff > 1.5 }">
            <i class="el-icon-info"></i>
            联动系数合理区间（0.8~1.2）：当前 {{ currentRow.linkCoeff }}
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getPerformanceLinkList } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'SalaryPerformanceLink',
  data() {
    return {
      queryForm: { year: '', companyName: '' },
      tableData: [],
      drawerVisible: false,
      currentRow: null,
      charts: {},
      companyOptions: [],
    }
  },
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
    kpiCards() {
      const normal = this.tableData.filter(r => r.linkCoeff >= 0.8 && r.linkCoeff <= 1.2).length
      const low = this.tableData.filter(r => r.linkCoeff < 0.8).length
      const high = this.tableData.filter(r => r.linkCoeff > 1.2).length
      return [
        { key: 'normal', label: '联动合规企业', value: normal + '家', color: '#52C41A' },
        { key: 'low', label: '联动偏低企业', value: low + '家', color: '#FA8C16' },
        { key: 'high', label: '联动偏高企业', value: high + '家', color: '#FA8C16' },
        { key: 'total', label: '监控企业总数', value: this.tableData.length + '家', color: '#1677FF' },
      ]
    },
  },
  mounted() {
    this.loadData()
    this.$nextTick(() => {
      this.initScatterChart()
      this.initCoeffChart()
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    async loadData() {
      try {
        const res = await getPerformanceLinkList({ ...this.queryForm, pageSize: 100 })
        if (res.data && res.data.tlist) {
          this.tableData = res.data.tlist.map(r => ({
            ...r,
            companyName: r.companyName,
            wageGrowth: r.wageGrowthRate != null ? r.wageGrowthRate : (r.wageGrowth || 0),
            profitGrowth: r.profitGrowthRate != null ? r.profitGrowthRate : (r.profitGrowth || 0),
            revenueGrowth: r.revenueGrowthRate != null ? r.revenueGrowthRate : (r.revenueGrowth || 0),
            laborProductivity: r.laborProductivityRate != null ? r.laborProductivityRate : (r.laborProductivity || 0),
            linkCoeff: r.linkCoefficient != null ? r.linkCoefficient : (r.linkCoeff || 0),
          }))
          const companies = [...new Set(this.tableData.map(r => r.companyName).filter(Boolean))]
          if (companies.length) this.companyOptions = companies
        }
      } catch (e) {
        console.warn('加载效益联动数据失败', e)
      }
      this.$nextTick(() => {
        this.initScatterChart()
        this.initCoeffChart()
      })
    },
    handleReset() {
      this.queryForm = { year: '', companyName: '' }
      this.loadData()
    },
    rowClass({ row }) {
      if (row.linkCoeff > 1.5 || row.linkCoeff < 0.6) return 'row-danger'
      if (row.linkCoeff < 0.8 || row.linkCoeff > 1.2) return 'row-warning'
      return ''
    },
    linkCoeffStyle(v) {
      if (v < 0.6) return { style: { color: '#F5222D', fontWeight: '700' }, tagType: 'danger', label: '严重脱钩' }
      if (v < 0.8) return { style: { color: '#FA8C16', fontWeight: '700' }, tagType: 'warning', label: '偏低' }
      if (v > 1.5) return { style: { color: '#F5222D', fontWeight: '700' }, tagType: 'danger', label: '严重偏高' }
      if (v > 1.2) return { style: { color: '#FAAD14' }, tagType: 'warning', label: '略高' }
      return { style: { color: '#52C41A' }, tagType: 'success', label: '合理' }
    },
    complianceTag(row) {
      if (row.linkCoeff < 0.6 || row.linkCoeff > 1.5) return { type: 'danger', text: '不合规' }
      if (row.wageGrowth > row.laborProductivity + 2) return { type: 'warning', text: '待核查' }
      return { type: 'success', text: '合规' }
    },
    viewDetail(row) {
      this.currentRow = row
      this.drawerVisible = true
    },
    initScatterChart() {
      const ec = echarts
      if (!ec || !this.tableData.length) return
      const c = ec.init(this.$refs.scatterChart)
      this.charts.scatter = c
      const data = this.tableData.map(r => [r.profitGrowth || 0, r.wageGrowth || 0, r.companyName || ''])
      c.setOption({
        tooltip: {
          trigger: 'item',
          formatter: p => `${p.value[2]}<br/>利润增长率: ${p.value[0]}%<br/>工资增长率: ${p.value[1]}%`,
        },
        xAxis: { name: '利润增长率(%)', type: 'value', min: 'dataMin' },
        yAxis: { name: '工资增长率(%)', type: 'value', min: 'dataMin' },
        series: [
          {
            type: 'scatter',
            data: data.map(d => ({ value: d, symbolSize: 16, itemStyle: { color: d[1] > d[0] * 1.5 ? '#F5222D' : d[1] < d[0] * 0.6 ? '#FA8C16' : '#52C41A' } })),
            label: { show: true, formatter: p => p.value[2], position: 'right', fontSize: 11 },
          },
          { type: 'line', data: [[-10, -10], [20, 20]], lineStyle: { color: '#52C41A', type: 'dashed', width: 1 }, symbol: 'none' },
        ],
      })
    },
    initCoeffChart() {
      const ec = echarts
      if (!ec || !this.tableData.length) return
      const c = ec.init(this.$refs.coeffChart)
      this.charts.coeff = c
      const companies = this.tableData.map(r => (r.companyName || '').replace('集团', '').replace('科技', '').replace('服务', ''))
      const coeffs = this.tableData.map(r => r.linkCoeff || 0)
      const colors = coeffs.map(v => v < 0.6 || v > 1.5 ? '#F5222D' : v < 0.8 || v > 1.2 ? '#FA8C16' : '#52C41A')
      c.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'value', min: 0, max: 2.5, axisLabel: { formatter: '{value}' } },
        yAxis: { type: 'category', data: companies },
        series: [
          {
            type: 'bar',
            data: coeffs.map((v, i) => ({ value: v, itemStyle: { color: colors[i] } })),
            label: { show: true, position: 'right', formatter: '{c}' },
            barWidth: '50%',
          },
          { type: 'line', data: Array(this.tableData.length).fill(0.8), lineStyle: { color: '#FA8C16', type: 'dashed' }, symbol: 'none' },
          { type: 'line', data: Array(this.tableData.length).fill(1.2), lineStyle: { color: '#52C41A', type: 'dashed' }, symbol: 'none' },
        ],
      })
    },
  },
}
</script>

<style scoped>
.sal-perf { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner-green {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 12px; }
.rule-tags { display: flex; gap: 10px; flex-wrap: wrap; }
.rule-tag { background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3); padding: 3px 10px; border-radius: 4px; font-size: 12px; }
.query-card { margin-bottom: 12px; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 22px; font-weight: 700; }
.chart-row { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.chart-tip { font-size: 11px; color: #8c8c8c; margin-left: 10px; }
.table-card { margin-bottom: 16px; }
::v-deep .row-danger td { background: #fff1f0 !important; }
::v-deep .row-warning td { background: #fffbe6 !important; }
.drawer-content { padding: 20px; }
.compliance-check-box { margin-top: 20px; }
.check-title { font-weight: 600; margin-bottom: 12px; }
.check-item { padding: 8px 12px; border-radius: 4px; margin-bottom: 8px; font-size: 13px; }
.check-pass { background: #f6ffed; color: #52C41A; }
.check-warn { background: #fffbe6; color: #FAAD14; }
.check-fail { background: #fff1f0; color: #F5222D; }
</style>
