<template>
  <div v-loading="loading" class="expense-monitor-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">费用管控监控</div>
        <div class="banner-sub">穿透管理费用、销售费用等科目，识别费用率异常膨胀，防范利益输送和资源浪费</div>
      </div>
      <div class="banner-right">
        <div v-for="q in bannerQuadrants" :key="q.label" class="banner-quad">
          <div class="bq-value">{{ q.value }}</div>
          <div class="bq-label">{{ q.label }}</div>
        </div>
      </div>
    </div>

    <!-- 4个统计卡 -->
    <el-row :gutter="14" style="margin-bottom:16px">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-inner">
            <div class="stat-icon" :style="{background:s.color+'18'}"><i :class="s.icon" :style="{color:s.color,fontSize:'26px'}"/></div>
            <div><div class="stat-val" :style="{color:s.color}">{{ s.value }}</div><div class="stat-lbl">{{ s.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区：费用率趋势 + 费用率横向对比 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">各企业费用率趋势（%）</div>
          <div ref="trendChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">费用率横向对比（管理+销售+财务）</div>
          <div ref="compareChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索筛选 + 费用明细表格 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>企业费用明细</span>
        <el-tag type="danger" size="small" style="margin-left:10px">橙色=接近警戒线  红色=超警戒线</el-tag>
        <el-button type="primary" size="mini" icon="el-icon-download" style="margin-left:auto" :loading="exportLoading" @click="handleExport">导出</el-button>
      </div>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="queryForm" size="small" style="margin-bottom:12px">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width:180px" @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="费用类别">
          <el-select v-model="queryForm.expenseCategory" placeholder="全部" clearable style="width:130px">
            <el-option label="管理费用" value="管理费用" />
            <el-option label="销售费用" value="销售费用" />
            <el-option label="研发费用" value="研发费用" />
            <el-option label="财务费用" value="财务费用" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告期">
          <el-input v-model="queryForm.period" placeholder="如2025-06" clearable style="width:130px" @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="expenseList" size="small" border :row-class-name="rowClass">
        <el-table-column label="费用编号" prop="expenseId" width="80" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="费用类别" prop="expenseCategory" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :style="categoryStyle(row.expenseCategory)" size="mini">{{ row.expenseCategory }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预算金额(万元)" prop="budgetAmount" width="130" align="right">
          <template slot-scope="{row}">{{ formatNum(row.budgetAmount) }}</template>
        </el-table-column>
        <el-table-column label="实际金额(万元)" prop="actualAmount" width="130" align="right">
          <template slot-scope="{row}">{{ formatNum(row.actualAmount) }}</template>
        </el-table-column>
        <el-table-column label="执行率(%)" prop="executionRate" width="100" align="center">
          <template slot-scope="{row}">
            <span :style="{color: row.executionRate>100?'#F5222D':row.executionRate>95?'#FA8C16':'#52C41A', fontWeight:'700'}">{{ row.executionRate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="超支金额(万元)" width="120" align="right">
          <template slot-scope="{row}">
            <span v-if="row.actualAmount > row.budgetAmount" style="color:#F5222D;font-weight:700">+{{ formatNum(row.actualAmount - row.budgetAmount) }}</span>
            <span v-else style="color:#52C41A">0</span>
          </template>
        </el-table-column>
        <el-table-column label="异常" prop="isAbnormal" width="70" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.isAbnormal==='1'?'danger':'success'" size="mini">{{ row.isAbnormal==='1'?'异常':'正常' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报告期" prop="period" width="90" align="center" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" icon="el-icon-data-line" @click="openTrend(row)">趋势</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin-top:12px;text-align:right"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :current-page.sync="pagination.pageNumber"
        :page-size.sync="pagination.pageSize"
        :page-sizes="[10, 15, 30, 50]"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />

      <!-- 警戒线说明 -->
      <div class="guideline-tips">
        <el-alert type="info" :closable="false" style="margin-top:12px">
          <template slot="title">
            费用管控标准：执行率 > <b style="color:#F5222D">100%</b>为超标（红），> <b style="color:#FA8C16">95%</b>为接近警戒（橙），其余为正常（绿）
          </template>
        </el-alert>
      </div>
    </el-card>

    <!-- 费用趋势弹窗 -->
    <el-dialog :title="`${activeRow.companyName||''}费用趋势分析`" :visible.sync="trendVisible" width="700px">
      <div ref="detailTrendChart" style="height:300px"></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getExpenseMonitorList, getExpenseTrend, exportExpenseData } from '@/api/stateAssets/financialPenetration'

export default {
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
  },
  name: 'FinancialExpenseMonitor',
  data() {
    return {
      loading: false,
      exportLoading: false,
      bannerQuadrants: [],
      statCards: [],
      expenseList: [],
      trendData: [],
      trendVisible: false,
      activeRow: {},
      charts: [],
      queryForm: {
        companyName: '',
        expenseCategory: '',
        period: '',
      },
      pagination: {
        pageNumber: 1,
        pageSize: 15,
        total: 0,
      },
    }
  },
  async mounted() {
    await this.loadData()
  },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize,
          companyName: this.queryForm.companyName || undefined,
          expenseCategory: this.queryForm.expenseCategory || undefined,
          period: this.queryForm.period || undefined,
        }
        const [listRes, trendRes] = await Promise.all([
          getExpenseMonitorList(params).catch(() => ({})),
          getExpenseTrend().catch(() => ({}))
        ])
        if (listRes.data && listRes.data.tlist) {
          this.expenseList = listRes.data.tlist
          this.pagination.total = listRes.data.totalRecord || 0
        }
        if (trendRes.data && trendRes.data.length) {
          this.trendData = trendRes.data
        }
        this.buildStatCards()
        this.buildBanner()
      } catch (e) { console.error('加载费用监控数据失败', e) }
      this.loading = false
      this.$nextTick(() => { this.initTrendChart(); this.initCompareChart() })
    },
    handleSearch() {
      this.pagination.pageNumber = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = { companyName: '', expenseCategory: '', period: '' }
      this.pagination.pageNumber = 1
      this.loadData()
    },
    handlePageChange(page) {
      this.pagination.pageNumber = page
      this.loadData()
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNumber = 1
      this.loadData()
    },
    async handleExport() {
      this.exportLoading = true
      try {
        const res = await exportExpenseData({
          companyName: this.queryForm.companyName || undefined,
          expenseCategory: this.queryForm.expenseCategory || undefined,
          period: this.queryForm.period || undefined,
        })
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '费用监控数据_' + new Date().toISOString().slice(0, 10) + '.xlsx'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (e) {
        this.$message.error('导出失败')
        console.error('导出费用数据失败', e)
      }
      this.exportLoading = false
    },
    buildBanner() {
      const list = this.expenseList
      const abnormalCount = list.filter(r => r.isAbnormal === '1').length
      const totalBudget = list.reduce((a, r) => a + (r.budgetAmount || 0), 0)
      const totalActual = list.reduce((a, r) => a + (r.actualAmount || 0), 0)
      const avgRate = list.length ? (list.reduce((a, r) => a + (r.executionRate || 0), 0) / list.length).toFixed(1) : 0
      this.bannerQuadrants = [
        { label: '费用记录数', value: list.length + '条' },
        { label: '异常笔数', value: abnormalCount + '笔' },
        { label: '预算总额(万)', value: this.formatNumShort(totalBudget) },
        { label: '平均执行率', value: avgRate + '%' },
      ]
    },
    buildStatCards() {
      const list = this.expenseList
      const totalBudget = list.reduce((a, r) => a + (r.budgetAmount || 0), 0)
      const totalActual = list.reduce((a, r) => a + (r.actualAmount || 0), 0)
      const overBudget = list.filter(r => r.executionRate > 100).length
      const abnormalCount = list.filter(r => r.isAbnormal === '1').length
      this.statCards = [
        { label: '预算总额(万元)', value: this.formatNumShort(totalBudget), color: '#1677FF', icon: 'el-icon-money' },
        { label: '实际支出总额(万元)', value: this.formatNumShort(totalActual), color: '#FA8C16', icon: 'el-icon-shopping-cart-2' },
        { label: '超预算笔数', value: overBudget + '笔', color: '#722ED1', icon: 'el-icon-s-finance' },
        { label: '异常笔数', value: abnormalCount + '笔', color: '#F5222D', icon: 'el-icon-warning' },
      ]
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      const c = echarts.init(this.$refs.trendChart); this.charts.push(c)
      // 从API trend data构建
      const trend = this.trendData
      const periods = [...new Set(trend.map(t => t.period))]
      const companies = [...new Set(trend.map(t => t.companyName || t.category))]
      if (periods.length && companies.length) {
        c.setOption({
          tooltip: { trigger: 'axis' }, legend: { data: companies, top: 0 },
          xAxis: { type: 'category', data: periods },
          yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
          series: companies.map((name, idx) => {
            const colors = ['#1677FF', '#F5222D', '#FA8C16', '#52C41A', '#722ED1']
            return { name, type: 'line', data: periods.map(p => { const item = trend.find(t => t.period === p && (t.companyName || t.category) === name); return item ? item.totalExpRate || item.actualAmount || 0 : null }), smooth: true, itemStyle: { color: colors[idx % colors.length] } }
          }),
        })
      }
    },
    initCompareChart() {
      if (!this.$refs.compareChart) return
      const c = echarts.init(this.$refs.compareChart); this.charts.push(c)
      const list = this.expenseList
      // 按费用类别分组汇总
      const categoryMap = {}
      list.forEach(r => {
        const cat = r.expenseCategory || '其他'
        categoryMap[cat] = (categoryMap[cat] || 0) + (r.actualAmount || 0)
      })
      const barData = Object.keys(categoryMap).map(name => ({ name, value: categoryMap[name] }))
      c.setOption({
        tooltip: { trigger: 'axis', formatter: '{b}: {c}万元' },
        xAxis: { type: 'category', data: barData.map(d => d.name), axisLabel: { fontSize: 12 } },
        yAxis: { type: 'value', axisLabel: { formatter: '{value}' } },
        series: [{
          type: 'bar', data: barData.map(d => d.value),
          itemStyle: { color: '#1677FF' },
          label: { show: true, position: 'top', formatter: '{c}' },
        }],
      })
    },
    openTrend(row) {
      this.activeRow = row
      this.trendVisible = true
      this.$nextTick(async () => {
        try {
          const res = await getExpenseTrend(row.companyId || row.companyName)
          const trend = res.data && res.data.length ? res.data : []
          const periods = [...new Set(trend.map(t => t.period))]
          const c = echarts.init(this.$refs.detailTrendChart)
          c.setOption({
            tooltip: { trigger: 'axis' }, legend: { data: ['管理费用率', '销售费用率', '财务费用率'] },
            xAxis: { type: 'category', data: periods.length ? periods : [] },
            yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
            series: [
              { name: '管理费用率', type: 'line', data: trend.map(t => t.mgmtExpRate || t.budgetAmount || 0), smooth: true },
              { name: '销售费用率', type: 'line', data: trend.map(t => t.saleExpRate || t.actualAmount || 0), smooth: true },
              { name: '财务费用率', type: 'line', data: trend.map(t => t.finExpRate || 0), smooth: true },
            ],
          })
        } catch {
          const c = echarts.init(this.$refs.detailTrendChart)
          c.setOption({
            tooltip: { trigger: 'axis' }, legend: { data: ['管理费用率', '销售费用率', '财务费用率'] },
            xAxis: { type: 'category', data: [] },
            yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
            series: [
              { name: '管理费用率', type: 'line', data: [], smooth: true },
              { name: '销售费用率', type: 'line', data: [], smooth: true },
              { name: '财务费用率', type: 'line', data: [], smooth: true },
            ],
          })
        }
      })
    },
    rowClass({ row }) {
      if (row.isAbnormal === '1') return 'row-danger'
      if (row.executionRate > 95) return 'row-warning'
      return ''
    },
    categoryStyle(cat) {
      const map = {
        '管理费用': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '销售费用': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '研发费用': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '财务费用': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
      }
      return map[cat] || {}
    },
    formatNum(v) {
      if (v == null) return '-'
      return Number(v).toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },
    formatNumShort(v) {
      if (!v) return '0'
      if (v >= 10000) return (v / 10000).toFixed(1) + '亿'
      return v.toLocaleString('zh-CN', { maximumFractionDigits: 0 })
    },
  },
}
</script>

<style scoped lang="scss">
.expense-monitor-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); max-width: 520px; }
  .banner-right { display: flex; gap: 18px; }
  .banner-quad { text-align: center; background: rgba(255,255,255,0.12); border-radius: 8px; padding: 10px 14px; }
  .bq-value { font-size: 20px; font-weight: 700; color: #fff; }
  .bq-label { font-size: 11px; color: rgba(255,255,255,0.7); margin-top: 2px; }
}
.stat-card { border-radius: 8px; }
.stat-inner { display: flex; align-items: center; gap: 12px; }
.stat-icon { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.stat-val { font-size: 22px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: #666; margin-top: 2px; }
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; gap: 8px; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
