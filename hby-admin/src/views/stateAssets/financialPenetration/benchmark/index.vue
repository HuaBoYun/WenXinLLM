<template>
  <div class="benchmark-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务指标对标分析</div>
        <div class="banner-sub">多维财务指标横向对比与行业基准线对标，快速识别指标异常企业</div>
      </div>
    </div>

    <!-- 维度切换 + 查询 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="指标维度">
          <el-select v-model="queryForm.dimension" @change="onDimChange" style="width:140px">
            <el-option label="偿债能力" value="debt"/>
            <el-option label="盈利能力" value="profit"/>
            <el-option label="运营能力" value="operation"/>
            <el-option label="现金流质量" value="cashflow"/>
          </el-select>
        </el-form-item>
        <el-form-item label="报告年份">
          <el-date-picker
            v-model="queryForm.yearDate"
            type="year"
            placeholder="选择年份"
            value-format="yyyy"
            style="width:130px"
          />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width:160px"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 指标对标图表 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">{{ dimensionLabel }}——企业横向排名对比</div>
          <div ref="rankChart" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">{{ dimensionLabel }}——雷达图综合对比</div>
          <div ref="radarChart" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 指标明细表格 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>指标详细数据（{{ dimensionLabel }}）</span>
        <el-tag size="small" type="danger" style="margin-left:10px">红色=超行业警戒线  橙色=接近警戒线</el-tag>
      </div>
      <el-table :data="tableData" size="small" border :row-class-name="rowClass">
        <el-table-column label="企业名称" prop="companyName" min-width="140"/>
        <el-table-column v-for="col in colDef" :key="col.key" :label="col.label" :prop="col.key" width="120" align="center">
          <template slot-scope="{row}">
            <span :style="getValueStyle(row[col.key], col)">{{ row[col.key] }}{{ col.unit || '' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="综合评级" prop="rating" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.rating==='优'?'success':row.rating==='良'?'':row.rating==='中'?'warning':'danger'" size="mini">{{ row.rating }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="行业排名" prop="industryRank" width="90" align="center">
          <template slot-scope="{row}">
            <span :style="{color:row.industryRank<=2?'#52C41A':row.industryRank>=4?'#F5222D':'#333', fontWeight:'700'}">{{ row.industryRank }}名</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 行业基准线说明 -->
      <div class="benchmark-legend">
        <span class="legend-item" v-for="line in currentBenchmarkLines" :key="line.name">
          <span :style="{display:'inline-block',width:'20px',height:'3px',background:line.color,verticalAlign:'middle',marginRight:'4px'}"></span>
          {{ line.name }}：{{ line.value }}{{ line.unit || '' }}
        </span>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getBenchmarkList } from '@/api/stateAssets/financialPenetration'

/** 指标维度配置（仅含列定义、主键、基准线，不含数据） */
const BENCHMARK_CONFIG = {
  debt: {
    colDef: [
      { key: 'debtRatio', label: '资产负债率', unit: '%', warnHigh: 70, warnMed: 60, reverse: true },
      { key: 'currentRatio', label: '流动比率', unit: '', warnLow: 1.0, warnMed: 1.2, reverse: false },
      { key: 'quickRatio', label: '速动比率', unit: '', warnLow: 0.8, reverse: false },
      { key: 'interestCover', label: '利息保障倍数', unit: '×', warnLow: 2.0, reverse: false },
    ],
    primaryKey: 'debtRatio',
    benchmarkLines: [
      { name: '资产负债率红色警戒线', value: 70, unit: '%', color: '#F5222D' },
      { name: '资产负债率橙色警戒线', value: 60, unit: '%', color: '#FA8C16' },
    ],
  },
  profit: {
    colDef: [
      { key: 'roe', label: 'ROE', unit: '%', warnLow: 5, warnMed: 8, reverse: false },
      { key: 'roa', label: 'ROA', unit: '%', warnLow: 2, reverse: false },
      { key: 'netProfitRate', label: '净利润率', unit: '%', warnLow: 3, reverse: false },
      { key: 'opProfitRate', label: '营业利润率', unit: '%', warnLow: 5, reverse: false },
    ],
    primaryKey: 'roe',
    benchmarkLines: [
      { name: 'ROE行业均值', value: '8.5', unit: '%', color: '#1677FF' },
      { name: 'ROE橙色预警线', value: '5', unit: '%', color: '#FA8C16' },
    ],
  },
  operation: {
    colDef: [
      { key: 'receivablesTurn', label: '应收账款周转率', unit: '次', warnLow: 3, reverse: false },
      { key: 'inventoryTurn', label: '存货周转率', unit: '次', warnLow: 2, reverse: false },
      { key: 'assetTurn', label: '总资产周转率', unit: '次', warnLow: 0.5, reverse: false },
    ],
    primaryKey: 'receivablesTurn',
    benchmarkLines: [{ name: '应收账款周转率警戒线', value: '3', unit: '次', color: '#FA8C16' }],
  },
  cashflow: {
    colDef: [
      { key: 'cfNiRatio', label: '现金流/净利润', unit: '', warnLow: 0.5, reverse: false },
      { key: 'freeCashflow', label: '自由现金流比率', unit: '', warnLow: 0, reverse: false },
      { key: 'capexRatio', label: '资本支出比率', unit: '%', warnHigh: 80, reverse: true },
    ],
    primaryKey: 'cfNiRatio',
    benchmarkLines: [{ name: '利润质量系数正常值', value: '1', unit: '', color: '#1677FF' }],
  },
}

export default {
  name: 'FinancialBenchmark',
  data() {
    return {
      queryForm: { dimension: 'debt', yearDate: '', companyName: '' },
      benchmarkAPIData: null,
      charts: [],
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
    dimensionLabel() {
      const map = { debt: '偿债能力', profit: '盈利能力', operation: '运营能力', cashflow: '现金流质量' }
      return map[this.queryForm.dimension] || ''
    },
    currentConfig() { return BENCHMARK_CONFIG[this.queryForm.dimension] || BENCHMARK_CONFIG.debt },
    colDef() { return this.currentConfig.colDef },
    tableData() { return this.benchmarkAPIData || [] },
    currentBenchmarkLines() { return this.currentConfig.benchmarkLines || [] },
  },
  async mounted() {
    await this.loadData()
  },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    async loadData() {
      this.benchmarkAPIData = []
      try {
        const params = {
          dimension: this.queryForm.dimension,
          pageNumber: 1,
          pageSize: 15,
        }
        if (this.queryForm.yearDate) {
          params.year = parseInt(this.queryForm.yearDate)
        }
        if (this.queryForm.companyName) {
          params.companyName = this.queryForm.companyName
        }
        const res = await getBenchmarkList(params)
        if (res.data && res.data.tlist && res.data.tlist.length) {
          this.benchmarkAPIData = res.data.tlist
        }
      } catch (e) { console.error('加载对标数据失败', e) }
      this.$nextTick(() => { this.initCharts() })
    },
    resetQuery() {
      this.queryForm = { dimension: 'debt', yearDate: '', companyName: '' }
      this.loadData()
    },
    onDimChange() { this.loadData() },
    initCharts() {
      this.charts.forEach(c => c && c.dispose()); this.charts = []
      this.initRankChart()
      this.initRadarChart()
    },
    initRankChart() {
      const c = echarts.init(this.$refs.rankChart); this.charts.push(c)
      const config = this.currentConfig
      const primaryKey = config.primaryKey
      const data = this.tableData
      if (!data.length) { c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } }); return }
      const sorted = [...data].sort((a, b) => b[primaryKey] - a[primaryKey])
      c.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: sorted.map(r => r.companyName), axisLabel: { fontSize: 11 } },
        yAxis: { type: 'value' },
        series: [{
          type: 'bar', barWidth: 40,
          data: sorted.map(r => ({ value: r[primaryKey], itemStyle: { color: this.getBarColor(r[primaryKey], config.colDef.find(col => col.key === primaryKey)) } })),
          label: { show: true, position: 'top' },
        }],
      })
    },
    initRadarChart() {
      const c = echarts.init(this.$refs.radarChart); this.charts.push(c)
      const colDef = this.currentConfig.colDef
      const data = this.tableData
      if (!data.length) { c.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } }); return }
      const indicators = colDef.map(col => {
        const vals = data.map(r => r[col.key]).filter(v => v != null)
        return { name: col.label, max: vals.length ? Math.max(...vals) * 1.3 : 100 }
      })
      c.setOption({
        tooltip: { trigger: 'item' },
        legend: { data: data.map(r => r.companyName), top: 0, type: 'scroll' },
        radar: { indicator: indicators, radius: '60%', center: ['50%', '55%'] },
        series: [{
          type: 'radar',
          data: data.map((r) => ({
            name: r.companyName,
            value: colDef.map(col => r[col.key] || 0),
            areaStyle: { opacity: 0.1 },
          })),
        }],
      })
    },
    getBarColor(v, col) {
      if (!col) return '#1677FF'
      if (col.reverse) {
        if (col.warnHigh && v > col.warnHigh) return '#F5222D'
        if (col.warnMed && v > col.warnMed) return '#FA8C16'
        return '#52C41A'
      } else {
        if (col.warnLow && v < col.warnLow) return '#F5222D'
        if (col.warnMed && v < col.warnMed) return '#FA8C16'
        return '#52C41A'
      }
    },
    getValueStyle(v, col) {
      if (!v && v !== 0) return {}
      const color = this.getBarColor(v, col)
      const isRed = color === '#F5222D', isOrange = color === '#FA8C16'
      if (isRed || isOrange) return { color, fontWeight: '700' }
      return {}
    },
    rowClass({ row }) {
      if (row.rating === '差') return 'row-danger'
      if (row.rating === '中') return 'row-warning'
      return ''
    },
  },
}
</script>

<style scoped lang="scss">
.benchmark-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); }
}
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; gap: 8px; }
.benchmark-legend { margin-top: 12px; display: flex; gap: 20px; flex-wrap: wrap; }
.legend-item { font-size: 12px; color: #666; display: flex; align-items: center; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
