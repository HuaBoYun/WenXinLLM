<template>
  <el-dialog
    title="合并对比分析"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <div class="compare-content">
      <div ref="radarChart" class="chart-container"></div>

      <el-table :data="compareTableData" border style="width: 100%; margin-top: 16px;" size="small">
        <el-table-column prop="indicator" label="指标" align="center" />
        <el-table-column prop="current" label="当前值" align="right">
          <template slot-scope="{ row }">{{ formatAmount(row.current) }}</template>
        </el-table-column>
        <el-table-column prop="benchmark" label="行业基准" align="right">
          <template slot-scope="{ row }">{{ formatAmount(row.benchmark) }}</template>
        </el-table-column>
        <el-table-column prop="diff" label="差异" align="right">
          <template slot-scope="{ row }">
            <span :style="{ color: row.diff >= 0 ? '#67C23A' : '#F56C6C' }">
              {{ row.diff >= 0 ? '+' : '' }}{{ formatAmount(row.diff) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="ratio" label="达标率" align="center">
          <template slot-scope="{ row }">
            <el-progress :percentage="Math.min(row.ratio, 100)" :color="row.ratio >= 100 ? '#67C23A' : '#E6A23C'" :stroke-width="14" :text-inside="true" />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ConsolidatedCompareDialog',
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
    compareTableData() {
      const data = this.analysisData && this.analysisData.compareData
      const current = (data && data.current) || this.analysisData || {}
      const benchmark = (data && data.benchmark) || []
      const fields = [
        ['营业收入', 'totalRevenue', benchmark[0]],
        ['资产总额', 'totalAssets', benchmark[1]],
        ['净资产', 'netAssets', benchmark[2]],
        ['净利润', 'netProfit', benchmark[3]],
        ['经营现金流', 'operatingCashflow', benchmark[4]],
      ]
      return fields.map(([indicator, key, base]) => {
        const currentValue = Number(current[key] || 0)
        const benchmarkValue = Number(base || 0)
        const ratio = benchmarkValue === 0 ? 0 : Math.round(currentValue / benchmarkValue * 100)
        return { indicator, current: currentValue, benchmark: benchmarkValue, diff: currentValue - benchmarkValue, ratio }
      })
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
      if (!this.$refs.radarChart) return
      this.chart = echarts.init(this.$refs.radarChart)
      const tableData = this.compareTableData
      const currentValues = tableData.map(item => item.current)
      const benchmarkValues = tableData.map(item => item.benchmark)
      const max = Math.max(...currentValues, ...benchmarkValues, 1) * 1.3
      this.chart.setOption({
        tooltip: {},
        legend: { data: ['当前值', '行业基准'], bottom: 0 },
        radar: {
          indicator: [
            { name: '营业收入', max },
            { name: '资产总额', max },
            { name: '净资产', max },
            { name: '净利润', max },
            { name: '经营现金流', max }
          ]
        },
        series: [{
          type: 'radar',
          data: [
            { value: currentValues, name: '当前值', areaStyle: { opacity: 0.2 } },
            { value: benchmarkValues, name: '行业基准', areaStyle: { opacity: 0.2 } }
          ]
        }]
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
.chart-container { width: 100%; height: 320px; }
</style>