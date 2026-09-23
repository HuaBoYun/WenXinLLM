<template>
  <div class="shareholding">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">参股企业经营分析</h2>
        <p class="banner-sub">参股企业盈亏监控 · 连续亏损识别 · 分红履行追踪 · 资产保值增值</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}{{ kpi.unit }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="输入企业名称" clearable style="width:180px;"></el-input>
        </el-form-item>
        <el-form-item label="经营状态">
          <el-select v-model="queryForm.businessStatus" placeholder="全部" clearable style="width:120px;">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="亏损" value="LOSS"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="综合评级">
          <el-select v-model="queryForm.rating" placeholder="全部" clearable style="width:100px;">
            <el-option label="A 良好" value="A"></el-option>
            <el-option label="B 一般" value="B"></el-option>
            <el-option label="C 关注" value="C"></el-option>
            <el-option label="D 高危" value="D"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16" style="margin-bottom:16px;">
      <!-- 数据表格 -->
      <el-col :span="16">
        <el-card shadow="never">
          <el-table :data="filteredData" size="small" border :row-class-name="rowClass" @row-click="showDetail">
            <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip></el-table-column>
            <el-table-column label="持股比例" prop="equityRatio" width="95" align="right">
              <template slot-scope="{row}">{{ row.equityRatio }}%</template>
            </el-table-column>
            <el-table-column label="出资额(万元)" prop="investAmount" width="115" align="right">
              <template slot-scope="{row}">{{ row.investAmount.toLocaleString() }}</template>
            </el-table-column>
            <el-table-column label="净利润(万元)" prop="netProfit" width="115" align="right">
              <template slot-scope="{row}">
                <span :style="{ color: row.netProfit > 0 ? '#52C41A' : '#F5222D', fontWeight:'700' }">
                  {{ row.netProfit > 0 ? '+' : '' }}{{ row.netProfit.toLocaleString() }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="连续亏损年" prop="consecutiveLossYears" width="105" align="center">
              <template slot-scope="{row}">
                <span :style="{ color: row.consecutiveLossYears >= 3 ? '#F5222D' : row.consecutiveLossYears >= 1 ? '#FA8C16' : '#52C41A', fontWeight:'700' }">
                  {{ row.consecutiveLossYears > 0 ? row.consecutiveLossYears + '年' : '无' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="未分红年" prop="noDividendYears" width="95" align="center">
              <template slot-scope="{row}">
                <span :style="{ color: row.noDividendYears >= 3 && row.netProfit > 0 ? '#FA8C16' : '#52C41A' }">
                  {{ row.noDividendYears > 0 ? row.noDividendYears + '年' : '正常' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="综合评级" prop="rating" width="90" align="center">
              <template slot-scope="{row}">
                <span class="rating-badge" :style="{ background: ratingBg(row.rating), color: ratingColor(row.rating) }">{{ row.rating }}</span>
                <span style="font-size:11px;color:#888;margin-left:4px;">{{ ratingLabel(row.rating) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="{row}">
                <el-button type="text" size="mini" @click.stop="showDetail(row)">分析</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <!-- 盈亏分布图 -->
      <el-col :span="8">
        <el-card shadow="never" style="height:100%;">
          <div slot="header" class="card-header"><span>盈亏状态分布</span></div>
          <div ref="pieChart" style="height:180px;"></div>
          <div slot="header" class="card-header" style="margin-top:12px;font-size:13px;"><span>持股比例分布</span></div>
          <div ref="barChart" style="height:160px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 经营分析抽屉 -->
    <el-drawer title="参股企业经营分析" :visible.sync="drawerVisible" direction="rtl" size="500px">
      <div class="detail-wrap" v-if="currentRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="持股比例">{{ currentRow.equityRatio }}%</el-descriptions-item>
          <el-descriptions-item label="出资额">{{ currentRow.investAmount.toLocaleString() }} 万元</el-descriptions-item>
          <el-descriptions-item label="净利润">
            <span :style="{ color: currentRow.netProfit > 0 ? '#52C41A' : '#F5222D', fontWeight:'700' }">
              {{ currentRow.netProfit > 0 ? '+' : '' }}{{ currentRow.netProfit.toLocaleString() }} 万元
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="综合评级">
            <span class="rating-badge" :style="{ background: ratingBg(currentRow.rating), color: ratingColor(currentRow.rating) }">{{ currentRow.rating }}</span>
            {{ ratingLabel(currentRow.rating) }}
          </el-descriptions-item>
          <el-descriptions-item label="连续亏损年数">{{ currentRow.consecutiveLossYears > 0 ? currentRow.consecutiveLossYears + '年' : '无' }}</el-descriptions-item>
          <el-descriptions-item label="最近分红年份">{{ currentRow.lastDividendYear || '从未分红' }}</el-descriptions-item>
        </el-descriptions>
        <div class="trend-title">近5年经营趋势</div>
        <div ref="trendChart" style="height:200px;"></div>
        <div v-if="currentRow.rating === 'D' || currentRow.rating === 'C'" class="warning-tip">
          <i class="el-icon-warning-outline" style="color:#FA8C16;"></i>
          <span v-if="currentRow.consecutiveLossYears >= 3">该企业已连续{{ currentRow.consecutiveLossYears }}年亏损，建议启动退出评估或重组方案。</span>
          <span v-else-if="currentRow.noDividendYears >= 3 && currentRow.netProfit > 0">该企业持续盈利但连续{{ currentRow.noDividendYears }}年未分红，建议核查分红原因。</span>
          <span v-else>该企业经营状况需重点关注，建议加强监控频次。</span>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getShareholdingList, getShareholdingAnalysis, exportPropertyShareholding } from '@/api/stateAssets/propertyRight'

export default {
  name: 'PropertyShareholding',
  data() {
    return {
      queryForm: { companyName: '', businessStatus: '', rating: '', pageNumber: 1, pageSize: 50 },
      tableData: [],
      kpiList: [],
      drawerVisible: false,
      currentRow: null,
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
    filteredData() {
      return this.tableData
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await getShareholdingList(this.queryForm)
        this.tableData = (res.data && (res.data.tlist || res.data.list)) || []
      } catch {
        this.tableData = []
      }
      this.buildKpi()
      this.$nextTick(() => {
        this.initPieChart()
        this.initBarChart()
      })
    },
    buildKpi() {
      const d = this.tableData
      const profit = d.filter(r => r.netProfit > 0).length
      const loss = d.filter(r => r.netProfit <= 0).length
      const divRate = d.filter(r => r.lastDividendYear && r.lastDividendYear >= '2024').length
      this.kpiList = [
        { key: 'total', label: '参股企业总数', value: d.length, unit: '家', color: '#1677FF' },
        { key: 'profit', label: '盈利企业数', value: profit, unit: '家', color: '#52C41A' },
        { key: 'loss', label: '亏损企业数', value: loss, unit: '家', color: '#F5222D' },
        { key: 'div', label: '近年分红覆盖率', value: d.length ? Math.round(divRate / d.length * 100) : 0, unit: '%', color: '#FA8C16' },
      ]
    },
    initPieChart() {
      if (!this.$refs.pieChart) return
      const chart = echarts.init(this.$refs.pieChart)
      const profit = this.tableData.filter(r => r.netProfit > 0).length
      const loss = this.tableData.filter(r => r.netProfit < 0).length
      const flat = this.tableData.filter(r => r.netProfit === 0).length
      chart.setOption({
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '50%'],
          data: [
            { value: profit, name: '盈利', itemStyle: { color: '#52C41A' } },
            { value: loss, name: '亏损', itemStyle: { color: '#F5222D' } },
            { value: flat, name: '持平', itemStyle: { color: '#FAAD14' } },
          ],
          label: { formatter: '{b}: {c}家' },
        }]
      })
    },
    initBarChart() {
      if (!this.$refs.barChart) return
      const chart = echarts.init(this.$refs.barChart)
      const ranges = [{ label: '<10%', min: 0, max: 10 }, { label: '10-33%', min: 10, max: 33 }, { label: '33-50%', min: 33, max: 50 }, { label: '>50%', min: 50, max: 100 }]
      const data = ranges.map(r => this.tableData.filter(c => c.equityRatio >= r.min && c.equityRatio < r.max).length)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: ranges.map(r => r.label) },
        series: [{ type: 'bar', data, itemStyle: { color: '#1677FF' }, label: { show: true, position: 'right' } }]
      })
    },
    async initTrendChart() {
      if (!this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      let years = []
      let trend = []
      try {
        const res = await getShareholdingAnalysis(this.currentRow.companyId)
        const d = res.data || {}
        years = d.trendYears || []
        trend = d.trendProfits || []
      } catch {
        years = []
        trend = []
      }
      if (years.length === 0) {
        chart.setOption({
          title: { text: '暂无历年数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } },
          xAxis: { show: false },
          yAxis: { show: false },
          series: []
        })
        return
      }
      const base = this.currentRow.netProfit || 0
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: years },
        yAxis: { type: 'value', name: '万元' },
        series: [{ name: '净利润', type: 'line', data: trend, smooth: true, itemStyle: { color: base >= 0 ? '#52C41A' : '#F5222D' }, areaStyle: { opacity: 0.1 } }]
      })
    },
    resetQuery() {
      this.queryForm = { companyName: '', businessStatus: '', rating: '', pageNumber: 1, pageSize: 50 }
      this.loadData()
    },
    async handleExport() {
      try {
        const res = await exportPropertyShareholding()
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '参股企业经营分析.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch {
        this.$message.error('导出失败')
      }
    },
    showDetail(row) {
      this.currentRow = row
      this.drawerVisible = true
      this.$nextTick(() => this.initTrendChart())
    },
    rowClass({ row }) {
      if (row.rating === 'D' || row.consecutiveLossYears >= 3) return 'row-danger'
      if (row.rating === 'C' || row.consecutiveLossYears >= 1 || (row.noDividendYears >= 3 && row.netProfit > 0)) return 'row-warning'
      return ''
    },
    ratingBg(r) { return { A: '#F6FFED', B: '#FFFBE6', C: '#FFF7E6', D: '#FFF1F0' }[r] || '#f0f0f0' },
    ratingColor(r) { return { A: '#52C41A', B: '#FAAD14', C: '#FA8C16', D: '#F5222D' }[r] || '#333' },
    ratingLabel(r) { return { A: '良好', B: '一般', C: '关注', D: '高危' }[r] || '' },
  }
}
</script>

<style scoped>
.shareholding { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
}
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.search-card { margin-bottom: 12px; }
.card-header { font-size: 14px; font-weight: 600; }
.rating-badge { padding: 2px 8px; border-radius: 4px; font-weight: 700; font-size: 13px; }
.detail-wrap { padding: 16px; }
.trend-title { font-size: 14px; font-weight: 600; color: #333; margin: 16px 0 8px; }
.warning-tip { margin-top: 16px; padding: 10px 12px; background: #FFF7E6; border-radius: 6px; border-left: 3px solid #FA8C16; font-size: 13px; color: #555; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
</style>
