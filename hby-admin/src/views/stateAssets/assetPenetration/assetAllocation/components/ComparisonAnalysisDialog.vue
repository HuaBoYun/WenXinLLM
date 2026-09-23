<template>
  <el-dialog
    title="对比分析"
    :visible.sync="dialogVisible"
    width="950px"
    :before-close="handleClose"
    destroy-on-close
  >
    <div v-loading="loading" class="comparison-analysis-container">
      <div class="current-info">
        <h4>当前配置信息</h4>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="企业名称">{{ currentData.enterpriseName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="资产类型">{{ getAssetTypeText(currentData.assetType) }}</el-descriptions-item>
          <el-descriptions-item label="所属行业">{{ getIndustryText(currentData.industry) }}</el-descriptions-item>
          <el-descriptions-item label="资产规模(万元)">{{ currentData.assetAmount || '-' }}</el-descriptions-item>
          <el-descriptions-item label="配置比例(%)">{{ currentData.allocationRatio || '-' }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskTag(currentData.riskLevel)" size="mini">{{ getRiskText(currentData.riskLevel) }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="charts-row">
        <div class="chart-wrapper">
          <h4>资产类型对比</h4>
          <div ref="typeComparisonChart" class="chart-box"></div>
        </div>
        <div class="chart-wrapper">
          <h4>行业分布对比</h4>
          <div ref="industryComparisonChart" class="chart-box"></div>
        </div>
      </div>
      <div class="table-section">
        <h4>类型对比明细</h4>
        <el-table :data="typeComparison" border size="small" style="width: 100%">
          <el-table-column prop="typeName" label="资产类型" align="center" />
          <el-table-column prop="count" label="配置数量" align="center" />
          <el-table-column prop="avgAmount" label="平均规模(万元)" align="center" />
          <el-table-column prop="avgYieldRate" label="平均收益率(%)" align="center" />
        </el-table>
      </div>
      <div class="table-section" style="margin-top: 16px;">
        <h4>行业对比明细</h4>
        <el-table :data="industryComparison" border size="small" style="width: 100%">
          <el-table-column prop="industryName" label="所属行业" align="center" />
          <el-table-column prop="count" label="配置数量" align="center" />
          <el-table-column prop="totalAmount" label="资产总额(万元)" align="center" />
        </el-table>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getAssetAllocationComparisonAnalysis } from '@/api/stateAssets/assetAllocation'

export default {
  name: 'ComparisonAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    allocationData: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      loading: false,
      currentData: {},
      typeComparison: [],
      industryComparison: [],
      typeChart: null,
      industryChart: null,
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
      if (val) { this.fetchData() }
    },
  },
  beforeDestroy() {
    if (this.typeChart) { this.typeChart.dispose(); this.typeChart = null }
    if (this.industryChart) { this.industryChart.dispose(); this.industryChart = null }
  },
  methods: {
    getAssetTypeText(type) {
      const map = { 'FIXED_ASSETS': '固定资产', 'CURRENT_ASSETS': '流动资产', 'INTANGIBLE_ASSETS': '无形资产', 'INVESTMENT_ASSETS': '投资性资产', 'FINANCIAL_ASSETS': '金融资产' }
      return map[type] || type || '-'
    },
    getIndustryText(industry) {
      const map = { 'MANUFACTURING': '制造业', 'FINANCE': '金融业', 'REAL_ESTATE': '房地产业', 'CONSTRUCTION': '建筑业', 'TRANSPORTATION': '交通运输业', 'IT': '信息技术业', 'ENERGY': '能源业' }
      return map[industry] || industry || '-'
    },
    getRiskTag(level) {
      const map = { 'LOW': 'success', 'MEDIUM': 'warning', 'HIGH': 'danger' }
      return map[level] || 'info'
    },
    getRiskText(level) {
      const map = { 'LOW': '低风险', 'MEDIUM': '中风险', 'HIGH': '高风险' }
      return map[level] || level || '-'
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getAssetAllocationComparisonAnalysis({ allocationId: this.allocationData.allocationId })
        if (res && res.result === 200 && res.data) {
          // 当前配置信息
          this.currentData = res.data.currentData || {}

          // 类型对比: 后端返回 [{assetType, count, avgAmount, avgYieldRate}]
          const rawType = res.data.typeComparison || []
          this.typeComparison = rawType.map(item => ({
            typeName: this.getAssetTypeText(item.assetType),
            count: item.count || 0,
            avgAmount: item.avgAmount || 0,
            avgYieldRate: item.avgYieldRate || 0
          }))

          // 行业对比: 后端返回 [{industry, count, totalAmount}]
          const rawIndustry = res.data.industryComparison || []
          this.industryComparison = rawIndustry.map(item => ({
            industryName: this.getIndustryText(item.industry),
            count: item.count || 0,
            totalAmount: item.totalAmount || 0
          }))

          this.$nextTick(() => { this.initCharts() })
        } else {
          this.currentData = {}
          this.typeComparison = []
          this.industryComparison = []
          this.$nextTick(() => { this.initCharts() })
        }
      } catch (e) {
        console.error('获取对比数据失败:', e)
        this.currentData = {}
        this.typeComparison = []
        this.industryComparison = []
        this.$nextTick(() => { this.initCharts() })
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initTypeChart()
      this.initIndustryChart()
    },
    initTypeChart() {
      if (this.typeChart) this.typeChart.dispose()
      this.typeChart = echarts.init(this.$refs.typeComparisonChart)
      const names = this.typeComparison.map(i => i.typeName)
      const amounts = this.typeComparison.map(i => i.avgAmount)
      this.typeChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '平均规模(万元)' },
        series: [{ name: '平均规模', type: 'bar', data: amounts, itemStyle: { color: '#409EFF' }, barWidth: '40%' }],
      })
    },
    initIndustryChart() {
      if (this.industryChart) this.industryChart.dispose()
      this.industryChart = echarts.init(this.$refs.industryComparisonChart)
      const names = this.industryComparison.map(i => i.industryName)
      const amounts = this.industryComparison.map(i => i.totalAmount)
      this.industryChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '资产总额(万元)' },
        series: [{ name: '资产总额', type: 'bar', data: amounts, itemStyle: { color: '#E6A23C' }, barWidth: '40%' }],
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
  },
}
</script>

<style lang="scss" scoped>
.comparison-analysis-container {
  .current-info {
    margin-bottom: 20px;
    h4 { margin: 0 0 8px; font-size: 14px; color: #303133; }
  }
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
  }
}
</style>
