<template>
  <el-dialog
    title="绩效对比分析"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <div class="compare-content">
      <el-row :gutter="20">
        <!-- 雷达图 -->
        <el-col :span="12">
          <h4 class="section-title">指标雷达图</h4>
          <div ref="radarChart" class="chart-container"></div>
        </el-col>
        <!-- 对比表格 -->
        <el-col :span="12">
          <h4 class="section-title">指标对比明细</h4>
          <el-table :data="compareData" border size="small" style="width: 100%;">
            <el-table-column prop="metric" label="指标" width="120" />
            <el-table-column prop="current" label="当前值" align="right" />
            <el-table-column prop="average" label="行业均值" align="right" />
            <el-table-column label="差异" align="center">
              <template slot-scope="{ row }">
                <span :style="{ color: row.diff >= 0 ? '#67C23A' : '#F56C6C' }">
                  {{ row.diff >= 0 ? '+' : '' }}{{ row.diff }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'PerformanceCompareDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
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
    compareData() {
      const d = this.data || {}
      const avgRevenue = 100000
      const avgProfit = 12000
      const avgAssets = 500000
      const avgContribution = 25
      const avgGrowth = 8
      return [
        {
          metric: '营业收入(万)',
          current: (d.totalRevenue || 0).toLocaleString(),
          average: avgRevenue.toLocaleString(),
          diff: d.totalRevenue ? (((d.totalRevenue - avgRevenue) / avgRevenue) * 100).toFixed(1) : 0
        },
        {
          metric: '净利润(万)',
          current: (d.netProfit || 0).toLocaleString(),
          average: avgProfit.toLocaleString(),
          diff: d.netProfit ? (((d.netProfit - avgProfit) / avgProfit) * 100).toFixed(1) : 0
        },
        {
          metric: '总资产(万)',
          current: (d.totalAssets || 0).toLocaleString(),
          average: avgAssets.toLocaleString(),
          diff: d.totalAssets ? (((d.totalAssets - avgAssets) / avgAssets) * 100).toFixed(1) : 0
        },
        {
          metric: '贡献率(%)',
          current: d.contribution || 0,
          average: avgContribution,
          diff: d.contribution ? ((d.contribution - avgContribution) / avgContribution * 100).toFixed(1) : 0
        },
        {
          metric: '增长率(%)',
          current: d.growthRate || 0,
          average: avgGrowth,
          diff: d.growthRate ? ((d.growthRate - avgGrowth) / avgGrowth * 100).toFixed(1) : 0
        }
      ]
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.$nextTick(() => { this.initChart() })
      } else if (this.chart) {
        this.chart.dispose()
        this.chart = null
      }
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    initChart() {
      if (!this.$refs.radarChart) return
      this.chart = echarts.init(this.$refs.radarChart)
      const d = this.data || {}
      const option = {
        legend: { data: ['当前企业', '行业均值'], bottom: 0 },
        radar: {
          indicator: [
            { name: '营业收入', max: 200000 },
            { name: '净利润', max: 30000 },
            { name: '总资产', max: 800000 },
            { name: '贡献率', max: 100 },
            { name: '增长率', max: 50 }
          ]
        },
        series: [{
          type: 'radar',
          data: [
            {
              value: [d.totalRevenue || 0, d.netProfit || 0, d.totalAssets || 0, d.contribution || 0, d.growthRate || 0],
              name: '当前企业',
              areaStyle: { opacity: 0.2 }
            },
            {
              value: [100000, 12000, 500000, 25, 8],
              name: '行业均值',
              areaStyle: { opacity: 0.1 }
            }
          ]
        }]
      }
      this.chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.compare-content { padding: 0 10px; }
.section-title {
  margin: 0 0 12px;
  font-size: 14px;
  color: #303133;
  border-left: 3px solid #409EFF;
  padding-left: 8px;
}
.chart-container {
  width: 100%;
  height: 320px;
}
</style>