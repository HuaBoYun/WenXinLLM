<template>
  <el-dialog
    title="绩效趋势分析"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div class="trend-toolbar">
      <el-select v-model="trendMetric" size="small" placeholder="选择指标" @change="renderChart">
        <el-option label="绩效评分" value="score" />
        <el-option label="ROE" value="roe" />
        <el-option label="ROA" value="roa" />
        <el-option label="营收增长率" value="revenueGrowth" />
      </el-select>
      <el-select v-model="trendRange" size="small" placeholder="时间范围" @change="renderChart">
        <el-option label="近1年" value="1y" />
        <el-option label="近3年" value="3y" />
        <el-option label="近5年" value="5y" />
      </el-select>
    </div>
    <div ref="trendChart" style="height:350px;"></div>
    <el-divider>趋势数据</el-divider>
    <el-table :data="trendData" size="small" border stripe max-height="200">
      <el-table-column label="期间" prop="period" width="120" />
      <el-table-column label="绩效评分" prop="score" width="100" align="center" />
      <el-table-column label="ROE(%)" prop="roe" width="100" align="center" />
      <el-table-column label="ROA(%)" prop="roa" width="100" align="center" />
      <el-table-column label="营收增长率(%)" prop="revenueGrowth" width="120" align="center" />
      <el-table-column label="同比变化" prop="yoyChange" width="100" align="center">
        <template slot-scope="{row}">
          <span :style="{color: row.yoyChange >= 0 ? '#52C41A' : '#F5222D'}">{{ row.yoyChange >= 0 ? '+' : '' }}{{ row.yoyChange }}%</span>
        </template>
      </el-table-column>
    </el-table>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceTrendDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      trendMetric: 'score',
      trendRange: '3y',
      trendData: []
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
        this.generateData()
        this.$nextTick(() => this.renderChart())
      }
    }
  },
  methods: {
    generateData() {
      this.trendData = [
        { period: '2024-Q1', score: 72, roe: 8.5, roa: 4.2, revenueGrowth: 3.2, yoyChange: 1.5 },
        { period: '2024-Q2', score: 75, roe: 9.1, roa: 4.5, revenueGrowth: 5.8, yoyChange: 2.1 },
        { period: '2024-Q3', score: 78, roe: 10.2, roa: 5.1, revenueGrowth: 7.3, yoyChange: 3.0 },
        { period: '2024-Q4', score: 80, roe: 11.0, roa: 5.5, revenueGrowth: 8.1, yoyChange: 2.6 },
        { period: '2025-Q1', score: 82, roe: 11.5, roa: 5.8, revenueGrowth: 9.2, yoyChange: 2.5 },
        { period: '2025-Q2', score: 85, roe: 12.3, roa: 6.2, revenueGrowth: 10.5, yoyChange: 3.0 },
        { period: '2025-Q3', score: 83, roe: 11.8, roa: 5.9, revenueGrowth: 8.8, yoyChange: -2.3 },
        { period: '2025-Q4', score: 86, roe: 12.8, roa: 6.4, revenueGrowth: 11.2, yoyChange: 3.6 },
        { period: '2026-Q1', score: 88, roe: 13.5, roa: 6.8, revenueGrowth: 12.0, yoyChange: 2.3 }
      ]
    },
    renderChart() {
      const echarts = window.echarts || this.$echarts
      if (!echarts || !this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      const metricMap = { score: '绩效评分', roe: 'ROE(%)', roa: 'ROA(%)', revenueGrowth: '营收增长率(%)' }
      const rangeMap = { '1y': 4, '3y': 12, '5y': 20 }
      const count = rangeMap[this.trendRange] || 9
      const data = this.trendData.slice(-count)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: [metricMap[this.trendMetric]] },
        xAxis: { type: 'category', data: data.map(d => d.period) },
        yAxis: { type: 'value', name: metricMap[this.trendMetric] },
        series: [{
          name: metricMap[this.trendMetric],
          type: 'line',
          data: data.map(d => d[this.trendMetric]),
          smooth: true,
          areaStyle: { opacity: 0.2 },
          itemStyle: { color: '#1677FF' },
          markPoint: { data: [{ type: 'max', name: '最大值' }, { type: 'min', name: '最小值' }] },
          markLine: { data: [{ type: 'average', name: '平均值' }] }
        }]
      })
    },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.trend-toolbar { display: flex; gap: 12px; margin-bottom: 12px; }
</style>
