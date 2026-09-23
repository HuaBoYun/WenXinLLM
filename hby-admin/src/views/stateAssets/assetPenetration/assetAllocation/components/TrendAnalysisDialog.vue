<template>
  <el-dialog
    title="趋势分析"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading" class="trend-analysis-container">
      <div class="charts-row">
        <div class="chart-wrapper">
          <h4>资产规模趋势</h4>
          <div ref="scaleTrendChart" class="chart-box"></div>
        </div>
        <div class="chart-wrapper">
          <h4>效率趋势</h4>
          <div ref="efficiencyTrendChart" class="chart-box"></div>
        </div>
      </div>
      <div class="table-section">
        <h4>趋势数据明细</h4>
        <el-table :data="trendData" border size="small" style="width: 100%">
          <el-table-column prop="period" label="时间" align="center" />
          <el-table-column prop="totalAmount" label="资产规模(万元)" align="center" />
          <el-table-column prop="growthRate" label="增长率(%)" align="center" />
          <el-table-column prop="efficiency" label="收益率(%)" align="center" />
          <el-table-column prop="riskLevel" label="资产类型" align="center" />
        </el-table>
        <div v-if="trendData.length === 0" class="empty-tip">暂无趋势数据</div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getAssetAllocationTrendAnalysis } from '@/api/stateAssets/assetAllocation'

export default {
  name: 'TrendAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    allocationData: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      loading: false,
      trendData: [],
      efficiencyTrend: [],
      scaleChart: null,
      efficiencyChart: null,
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
  },
  watch: {
    visible(val) {
      if (val) {
        this.fetchData()
      }
    },
  },
  beforeDestroy() {
    if (this.scaleChart) { this.scaleChart.dispose(); this.scaleChart = null }
    if (this.efficiencyChart) { this.efficiencyChart.dispose(); this.efficiencyChart = null }
  },
  methods: {
    getRiskLevelText(level) {
      const map = { 'LOW': '低风险', 'MEDIUM': '中风险', 'HIGH': '高风险' }
      return map[level] || level || '-'
    },
    getAssetTypeText(type) {
      const map = { 'FIXED_ASSETS': '固定资产', 'CURRENT_ASSETS': '流动资产', 'INTANGIBLE_ASSETS': '无形资产', 'INVESTMENT_ASSETS': '投资性资产', 'FINANCIAL_ASSETS': '金融资产' }
      return map[type] || type || '-'
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getAssetAllocationTrendAnalysis({ allocationId: this.allocationData.allocationId })
        if (res && res.result === 200 && res.data) {
          // 后端返回 trendData: [{date, assetAmount, yieldRate, allocationRatio, assetType}]
          // 后端返回 efficiencyTrend: [{date, avgYieldRate, totalAmount}]
          const rawTrend = res.data.trendData || []
          const rawEfficiency = res.data.efficiencyTrend || []

          // 转换为表格数据
          this.trendData = rawTrend.map((item, idx) => {
            const prev = idx > 0 ? rawTrend[idx - 1] : null
            const growthRate = prev && prev.assetAmount > 0
              ? (((item.assetAmount - prev.assetAmount) / prev.assetAmount) * 100).toFixed(2)
              : '-'
            return {
              period: item.date || '-',
              totalAmount: item.assetAmount || 0,
              growthRate: growthRate,
              efficiency: item.yieldRate || 0,
              riskLevel: this.getAssetTypeText(item.assetType)
            }
          })

          // 转换效率趋势数据
          this.efficiencyTrend = rawEfficiency.map(item => ({
            period: item.date || '-',
            efficiency: item.avgYieldRate || 0,
            totalAmount: item.totalAmount || 0
          }))

          this.$nextTick(() => { this.initCharts() })
        } else {
          this.trendData = []
          this.efficiencyTrend = []
          this.$nextTick(() => { this.initCharts() })
        }
      } catch (e) {
        console.error('获取趋势数据失败:', e)
        this.trendData = []
        this.efficiencyTrend = []
        this.$nextTick(() => { this.initCharts() })
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initScaleChart()
      this.initEfficiencyChart()
    },
    initScaleChart() {
      if (this.scaleChart) this.scaleChart.dispose()
      this.scaleChart = echarts.init(this.$refs.scaleTrendChart)
      const periods = this.trendData.map(i => i.period)
      const amounts = this.trendData.map(i => i.totalAmount)
      this.scaleChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: periods, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '万元' },
        series: [{ name: '资产规模', type: 'line', data: amounts, smooth: true, itemStyle: { color: '#409EFF' }, areaStyle: { color: 'rgba(64,158,255,0.1)' } }],
      })
    },
    initEfficiencyChart() {
      if (this.efficiencyChart) this.efficiencyChart.dispose()
      this.efficiencyChart = echarts.init(this.$refs.efficiencyTrendChart)
      const periods = this.efficiencyTrend.map(i => i.period)
      const values = this.efficiencyTrend.map(i => i.efficiency)
      this.efficiencyChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: periods, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '收益率(%)' },
        series: [{ name: '平均收益率', type: 'line', data: values, smooth: true, itemStyle: { color: '#67C23A' }, areaStyle: { color: 'rgba(103,194,58,0.1)' } }],
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
  },
}
</script>

<style lang="scss" scoped>
.trend-analysis-container {
  .charts-row {
    display: flex; gap: 16px; margin-bottom: 20px;
    .chart-wrapper {
      flex: 1;
      h4 { margin: 0 0 8px; font-size: 14px; color: #303133; }
      .chart-box { width: 100%; height: 280px; }
    }
  }
  .table-section {
    h4 { margin: 0 0 8px; font-size: 14px; color: #303133; }
    .empty-tip { text-align: center; color: #909399; padding: 20px 0; font-size: 14px; }
  }
}
</style>
