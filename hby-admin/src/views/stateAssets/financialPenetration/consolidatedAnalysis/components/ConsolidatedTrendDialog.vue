<template>
  <el-dialog
    title="合并趋势分析"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <div class="trend-header">
      <span>企业: {{ analysisData.companyName || '-' }}</span>
      <span>期间: {{ analysisData.period || '-' }}</span>
    </div>
    <div ref="trendChart" class="chart-container"></div>

    <el-table :data="trendTableData" border style="width: 100%; margin-top: 16px;" size="small">
      <el-table-column prop="period" label="期间" align="center" />
      <el-table-column prop="totalRevenue" label="营业收入" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.totalRevenue) }}</template>
      </el-table-column>
      <el-table-column prop="netProfit" label="净利润" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.netProfit) }}</template>
      </el-table-column>
      <el-table-column prop="totalAssets" label="资产总额" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.totalAssets) }}</template>
      </el-table-column>
    </el-table>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ConsolidatedTrendDialog',
  props: {
    visible: { type: Boolean, default: false },
    analysisData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      chart: null
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    trendTableData() {
      return (this.analysisData && this.analysisData.trendRows) || []
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.$nextTick(() => this.initChart())
      } else {
        this.disposeChart()
      }
    }
  },
  beforeDestroy() {
    this.disposeChart()
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    initChart() {
      if (!this.$refs.trendChart) return
      this.chart = echarts.init(this.$refs.trendChart)
      const tableData = this.trendTableData
      const periods = tableData.map(r => r.period)
      const revenueData = tableData.map(r => r.totalRevenue)
      const profitData = tableData.map(r => r.netProfit)
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业收入', '净利润'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: periods },
        yAxis: { type: 'value', name: '万元' },
        series: [
          { name: '营业收入', type: 'line', data: revenueData, smooth: true, itemStyle: { color: '#409EFF' } },
          { name: '净利润', type: 'line', data: profitData, smooth: true, itemStyle: { color: '#67C23A' } }
        ]
      })
    },
    formatAmount(val) {
      if (val === undefined || val === null) return '-'
      return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    disposeChart() {
      if (this.chart) {
        this.chart.dispose()
        this.chart = null
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.trend-header { margin-bottom: 16px; color: #606266; }
.trend-header span { margin-right: 24px; font-size: 14px; }
.chart-container { width: 100%; height: 320px; }
</style>