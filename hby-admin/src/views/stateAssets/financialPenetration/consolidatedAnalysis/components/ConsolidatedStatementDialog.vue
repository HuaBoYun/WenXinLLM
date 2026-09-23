<template>
  <el-dialog
    title="合并报表详情"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <el-descriptions title="财务数据概览" :column="3" border>
      <el-descriptions-item label="企业名称">{{ analysisData.companyName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="报表类型">{{ analysisData.statementType || '-' }}</el-descriptions-item>
      <el-descriptions-item label="报告期间">{{ analysisData.period || '-' }}</el-descriptions-item>
      <el-descriptions-item label="营业总收入">{{ formatAmount(analysisData.totalRevenue) }}</el-descriptions-item>
      <el-descriptions-item label="资产总额">{{ formatAmount(analysisData.totalAssets) }}</el-descriptions-item>
      <el-descriptions-item label="负债总额">{{ formatAmount(analysisData.totalLiabilities) }}</el-descriptions-item>
      <el-descriptions-item label="净资产">{{ formatAmount(analysisData.netAssets) }}</el-descriptions-item>
      <el-descriptions-item label="净利润">{{ formatAmount(analysisData.netProfit) }}</el-descriptions-item>
      <el-descriptions-item label="经营现金流">{{ formatAmount(analysisData.operatingCashflow) }}</el-descriptions-item>
      <el-descriptions-item label="资产负债率">{{ formatPercent(analysisData.assetLiabilityRatio) }}</el-descriptions-item>
      <el-descriptions-item label="销售净利率">{{ formatPercent(analysisData.netProfitMargin) }}</el-descriptions-item>
      <el-descriptions-item label="现金收入比">{{ formatPercent(analysisData.cashRevenueRatio) }}</el-descriptions-item>
      <el-descriptions-item label="审计状态">
        <el-tag :type="analysisData.auditStatus === 'APPROVED' || analysisData.auditStatus === '已审计' ? 'success' : 'warning'">
          {{ formatAuditStatus(analysisData.auditStatus) }}
        </el-tag>
      </el-descriptions-item>
    </el-descriptions>

    <div class="chart-section">
      <h4>资产负债结构</h4>
      <div ref="barChart" class="chart-container"></div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ConsolidatedStatementDialog',
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
    formatAmount(val) {
      if (val === undefined || val === null) return '-'
      return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) + ' 万元'
    },
    formatPercent(val) {
      if (val === undefined || val === null) return '-'
      return Number(val).toFixed(2) + '%'
    },
    formatAuditStatus(status) {
      const map = { APPROVED: '已审计', UNAUDITED: '未审计', PENDING: '待审计', REJECTED: '已驳回' }
      return map[status] || status || '-'
    },
    initChart() {
      if (!this.$refs.barChart) return
      this.chart = echarts.init(this.$refs.barChart)
      const data = this.analysisData
      this.chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['金额(万元)'] },
        xAxis: {
          type: 'category',
          data: ['资产总额', '负债总额', '净资产', '营业收入', '净利润', '经营现金流']
        },
        yAxis: { type: 'value', name: '万元' },
        series: [{
          name: '金额(万元)',
          type: 'bar',
          data: [
            data.totalAssets || 0,
            data.totalLiabilities || 0,
            data.netAssets || 0,
            data.totalRevenue || 0,
            data.netProfit || 0,
            data.operatingCashflow || 0
          ],
          itemStyle: {
            color: function(params) {
              const colors = ['#409EFF', '#F56C6C', '#67C23A', '#E6A23C', '#909399', '#5470c6']
              return colors[params.dataIndex]
            }
          }
        }]
      })
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
.chart-section { margin-top: 20px; }
.chart-section h4 { margin-bottom: 10px; color: #303133; }
.chart-container { width: 100%; height: 350px; }
</style>