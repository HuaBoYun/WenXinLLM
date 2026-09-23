<template>
  <el-dialog
    title="合并预测分析"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <div class="predict-header">
      <el-alert title="基于当前财务数据进行线性趋势预测，仅供参考" type="info" :closable="false" show-icon />
    </div>

    <div ref="predictChart" class="chart-container"></div>

    <el-table :data="predictTableData" border style="width: 100%; margin-top: 16px;" size="small">
      <el-table-column prop="period" label="期间" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.type === 'PREDICT' ? 'warning' : ''" size="small">{{ row.period }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalRevenue" label="营业收入" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.totalRevenue) }}</template>
      </el-table-column>
      <el-table-column prop="netProfit" label="净利润" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.netProfit) }}</template>
      </el-table-column>
      <el-table-column prop="totalAssets" label="资产总额" align="right">
        <template slot-scope="{ row }">{{ formatAmount(row.totalAssets) }}</template>
      </el-table-column>
      <el-table-column prop="type" label="数据类型" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.type === 'PREDICT' ? 'danger' : 'success'" size="mini">
            {{ row.type === 'PREDICT' ? '预测' : '历史' }}
          </el-tag>
        </template>
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
  name: 'ConsolidatedPredictDialog',
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
    predictTableData() {
      return (this.analysisData && this.analysisData.predictRows) || []
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
      if (!this.$refs.predictChart) return
      this.chart = echarts.init(this.$refs.predictChart)
      const tableData = this.predictTableData
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
          {
            name: '营业收入', type: 'line', data: revenueData, smooth: true,
            markArea: { silent: true, data: [[{ xAxis: '下一季度(预测)' }, { xAxis: '第四季度(预测)' }]], itemStyle: { color: 'rgba(230,162,60,0.08)' } }
          },
          { name: '净利润', type: 'line', data: profitData, smooth: true }
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
.predict-header { margin-bottom: 16px; }
.chart-container { width: 100%; height: 320px; }
</style>