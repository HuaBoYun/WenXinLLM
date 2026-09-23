<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-document"></i> 招投标合规分析</h2>
        <p class="page-desc">分析各级企业招标方式合规性，识别违规使用单一来源、未招标行为，预警围标串标风险</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <div class="stat-card" :style="{ borderLeft: '4px solid ' + s.color }">
          <div class="stat-val" :style="{ color: s.color }">{{ s.value }}</div>
          <div class="stat-lb">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd">招投标方式合规分布（堆叠柱图）</div>
          <div ref="stackChart" class="chart-sm"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd">企业合规率雷达图</div>
          <div ref="radarChart" class="chart-sm"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 违规预警 -->
    <el-alert
      v-if="violationCount > 0"
      :title="`当前有 ${violationCount} 个项目存在招投标违规，${pendingCount} 个项目待核查`"
      type="error" show-icon :closable="false" style="margin-bottom:12px"
    />

    <!-- 查询区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" inline size="small">
        <el-form-item label="采购企业" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="招标方式" prop="biddingMethod">
          <el-select v-model="queryForm.biddingMethod" placeholder="请选择" clearable style="width:120px">
            <el-option label="公开招标" value="OPEN_BIDDING" />
            <el-option label="竞争性谈判" value="NEGOTIATION" />
            <el-option label="单一来源" value="SINGLE_SOURCE" />
            <el-option label="询价采购" value="INQUIRY" />
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态" prop="complianceStatus">
          <el-select v-model="queryForm.complianceStatus" placeholder="请选择" clearable style="width:110px">
            <el-option label="合规" value="COMPLIANT" />
            <el-option label="违规" value="VIOLATION" />
            <el-option label="待核查" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border :row-class-name="rowClassName">
        <el-table-column label="项目编号" prop="projectNo" width="130" />
        <el-table-column label="项目名称" prop="projectName" min-width="180" show-overflow-tooltip />
        <el-table-column label="采购企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="项目类型" width="85" align="center">
          <template slot-scope="{ row }">
            <el-tag size="mini" type="info">{{ typeMap[row.projectType] || row.projectType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="招标方式" width="110" align="center">
          <template slot-scope="{ row }">{{ methodMap[row.biddingMethod] || row.biddingMethod }}</template>
        </el-table-column>
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right">
          <template slot-scope="{ row }">{{ row.contractAmount ? row.contractAmount.toLocaleString() : '—' }}</template>
        </el-table-column>
        <el-table-column label="是否达招标限额" width="120" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.isAboveThreshold ? 'danger' : 'success'" size="mini">{{ row.isAboveThreshold ? '达到' : '未达到' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ COMPLIANT: 'success', VIOLATION: 'danger', PENDING: 'warning' }[row.complianceStatus]" size="mini">
              {{ { COMPLIANT: '合规', VIOLATION: '违规', PENDING: '待核查' }[row.complianceStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="违规类型" prop="violationType" min-width="140" show-overflow-tooltip>
          <template slot-scope="{ row }">
            <span v-if="row.violationType" style="color:#CF1322;font-size:12px">{{ row.violationType }}</span>
            <span v-else style="color:#8C8C8C;font-size:12px">—</span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[row.riskLevel]" size="mini">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="v => { queryForm.pageSize = v; fetchData() }" @current-change="v => { queryForm.pageNumber = v; fetchData() }" />
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getBiddingComplianceList, getBiddingComplianceStatistics } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'BiddingCompliance',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', biddingMethod: '', complianceStatus: '' },
      typeMap: { ENGINEERING: '工程', GOODS: '货物', SERVICE: '服务', IT: 'IT' },
      methodMap: { OPEN_BIDDING: '公开招标', NEGOTIATION: '竞争性谈判', SINGLE_SOURCE: '单一来源', INQUIRY: '询价采购' },
      statCards: [
        { label: '招标项目总数', value: '—', color: '#0050A0' },
        { label: '公开招标比', value: '—', color: '#52C41A' },
        { label: '违规项目数', value: '—', color: '#CF1322' },
        { label: '违规金额(万元)', value: '—', color: '#CF1322' },
      ],
      charts: [],
    }
  },
  computed: {
    violationCount() { return this.list.filter(r => r.complianceStatus === 'VIOLATION').length },
    pendingCount() { return this.list.filter(r => r.complianceStatus === 'PENDING').length },
  },
  watch: {
    ipSecondary() {
      this.$nextTick(() => this.initCharts())
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.fetchData()
      this.fetchStatistics()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getBiddingComplianceList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    async fetchStatistics() {
      try {
        const res = await getBiddingComplianceStatistics()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.statCards = [
            { label: '招标项目总数', value: d.totalCount != null ? String(d.totalCount) : '—', color: this.ipSecondary },
            { label: '公开招标比', value: d.openBiddingRate != null ? d.openBiddingRate + '%' : '—', color: '#52C41A' },
            { label: '违规项目数', value: d.violationCount != null ? d.violationCount + '项' : '—', color: '#CF1322' },
            { label: '违规金额(万元)', value: d.violationAmount != null ? Number(d.violationAmount).toLocaleString() : '—', color: '#CF1322' },
          ]
          // Update charts with real data if available
          if (d.chartData) { this.updateCharts(d.chartData) }
          if (d.radarData) { this.updateRadar(d.radarData) }
        }
      } catch (e) {
        // Keep default values on error
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    rowClassName({ row }) {
      if (row.complianceStatus === 'VIOLATION') return 'row-violation'
      if (row.complianceStatus === 'PENDING') return 'row-warning'
      return ''
    },
    handleResize() { this.charts.forEach(c => c.resize()) },
    initCharts() {
      this.charts.forEach(c => c.dispose())
      this.charts = []
      // stack bar: method distribution by company
      const companyMethodMap = {}
      const methodSet = new Set()
      this.list.forEach(r => {
        if (!companyMethodMap[r.companyName]) companyMethodMap[r.companyName] = {}
        const method = r.biddingMethod || 'OTHER'
        methodSet.add(method)
        companyMethodMap[r.companyName][method] = (companyMethodMap[r.companyName][method] || 0) + 1
      })
      const companies = Object.keys(companyMethodMap)
      const methodArr = [...methodSet]
      const methodNames = { OPEN_BIDDING: '公开招标', NEGOTIATION: '竞争性谈判', SINGLE_SOURCE: '单一来源', INQUIRY: '询价采购' }
      const methodColors = { OPEN_BIDDING: '#52C41A', NEGOTIATION: '#1677FF', SINGLE_SOURCE: '#FA8C16', INQUIRY: '#FAAD14' }
      const series = methodArr.length
        ? methodArr.map(m => ({
          name: methodNames[m] || m, type: 'bar', stack: 'total',
          data: companies.map(c => companyMethodMap[c][m] || 0),
          itemStyle: { color: methodColors[m] || '#D9D9D9' },
        }))
        : [
          { name: '公开招标', type: 'bar', stack: 'total', data: [], itemStyle: { color: '#52C41A' } },
          { name: '竞争性谈判', type: 'bar', stack: 'total', data: [], itemStyle: { color: '#1677FF' } },
          { name: '单一来源', type: 'bar', stack: 'total', data: [], itemStyle: { color: '#FA8C16' } },
          { name: '询价采购', type: 'bar', stack: 'total', data: [], itemStyle: { color: '#FAAD14' } },
        ]
      const c1 = echarts.init(this.$refs.stackChart)
      this.charts.push(c1)
      c1.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['公开招标', '竞争性谈判', '单一来源', '询价采购'], top: 4 },
        grid: { left: 80, right: 20, top: 36, bottom: 30 },
        xAxis: { type: 'value', name: '项目数' },
        yAxis: { type: 'category', data: companies },
        series: series,
      })
      // radar chart
      const c2 = echarts.init(this.$refs.radarChart)
      this.charts.push(c2)
      c2.setOption({
        tooltip: {},
        legend: { data: ['采购合规率'], top: 4 },
        radar: {
          indicator: [
            { name: '招标合规', max: 100 }, { name: '关联交易合规', max: 100 }, { name: '供应商合规', max: 100 },
            { name: '合同履约率', max: 100 }, { name: '预算执行率', max: 100 },
          ],
          center: ['50%', '55%'], radius: '65%',
        },
        series: [{
          type: 'radar', name: '采购合规率',
          data: [{ value: [0, 0, 0, 0, 0], name: '集团整体', areaStyle: { color: 'rgba(' + this.ipPrimaryRgb + ',0.15)' }, lineStyle: { color: this.ipSecondary }, itemStyle: { color: this.ipSecondary } }],
        }],
      })
    },
    updateCharts(chartData) {
      if (this.charts[0] && chartData.companies && chartData.series) {
        this.charts[0].setOption({
          yAxis: { data: chartData.companies },
          series: chartData.series.map((s, i) => ({ data: s.data })),
        })
      }
    },
    updateRadar(radarData) {
      if (this.charts[1] && radarData.values) {
        this.charts[1].setOption({
          series: [{ data: [{ value: radarData.values, name: '集团整体' }] }],
        })
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.page-header {
  padding: 18px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 60%, var(--ip-bright) 100%);
  border-radius: 8px; color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card { background: #fff; border-radius: 6px; padding: 14px 16px; box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .stat-val { font-size: 22px; font-weight: 700; }
  .stat-lb { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-sm { height: 220px; }
.search-card { margin-bottom: 12px; }
.card-hd { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-table .row-violation td { background: #FFF1F0 !important; }
::v-deep .el-table .row-warning td { background: #FFF7E6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
