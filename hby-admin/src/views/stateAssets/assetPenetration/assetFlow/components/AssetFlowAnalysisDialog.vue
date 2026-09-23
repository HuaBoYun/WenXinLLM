<template>
  <el-dialog
    title="资产流向分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="analysis-dialog-container">
      <!-- 分析维度选择 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>分析维度</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-checkbox v-model="analysisOptions.byAmount">按金额分析</el-checkbox>
          </el-col>
          <el-col :span="6">
            <el-checkbox v-model="analysisOptions.byTime">按时间分析</el-checkbox>
          </el-col>
          <el-col :span="6">
            <el-checkbox v-model="analysisOptions.byPath">按路径分析</el-checkbox>
          </el-col>
          <el-col :span="6">
            <el-checkbox v-model="analysisOptions.byRisk">按风险分析</el-checkbox>
          </el-col>
        </el-row>
      </el-card>

      <!-- 分析结果 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>分析结果</span>
        </div>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div ref="amountChart" style="height: 300px;"></div>
          </el-col>
          <el-col :span="12">
            <div ref="timeChart" style="height: 300px;"></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div ref="pathChart" style="height: 300px;"></div>
          </el-col>
          <el-col :span="12">
            <div ref="riskChart" style="height: 300px;"></div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 分析建议 -->
      <el-card>
        <div slot="header">
          <span>分析建议</span>
        </div>
        <el-alert
          v-for="(suggestion, index) in suggestions"
          :key="index"
          :title="suggestion.title"
          :description="suggestion.description"
          :type="suggestion.type"
          :closable="false"
          class="mb-10"
        />
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="executeAnalysis">执行分析</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'AssetFlowAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      analysisOptions: {
        byAmount: true,
        byTime: true,
        byPath: false,
        byRisk: false
      },
      suggestions: [],
      charts: {}
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
        this.initCharts()
        this.loadAnalysisData()
      }
    }
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    initCharts() {
      this.charts.amountChart = echarts.init(this.$refs.amountChart)
      this.charts.timeChart = echarts.init(this.$refs.timeChart)
      this.charts.pathChart = echarts.init(this.$refs.pathChart)
      this.charts.riskChart = echarts.init(this.$refs.riskChart)
    },
    loadAnalysisData() {
      this.loading = true
      // 模拟分析数据
      this.drawAmountChart()
      this.drawTimeChart()
      this.drawPathChart()
      this.drawRiskChart()

      this.suggestions = [
        { title: '金额分析', description: '资产流转金额呈上升趋势，建议加强监管', type: 'warning' },
        { title: '时间分析', description: '流转周期较长，建议优化流程', type: 'info' },
        { title: '路径分析', description: '流转路径复杂度较高，建议简化流程', type: 'warning' },
        { title: '风险分析', description: '未发现明显风险，继续监控', type: 'success' }
      ]

      this.loading = false
    },
    drawAmountChart() {
      const option = {
        title: { text: '金额分析', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月'] },
        yAxis: { type: 'value' },
        series: [{
          data: [500, 600, 700, 800, 900],
          type: 'line',
          smooth: true
        }]
      }
      this.charts.amountChart.setOption(option)
    },
    drawTimeChart() {
      const option = {
        title: { text: '时间分析', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五'] },
        yAxis: { type: 'value' },
        series: [{
          data: [10, 15, 12, 18, 20],
          type: 'bar'
        }]
      }
      this.charts.timeChart.setOption(option)
    },
    drawPathChart() {
      const option = {
        title: { text: '路径分析', left: 'center' },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          data: [
            { value: 40, name: '2层路径' },
            { value: 35, name: '3层路径' },
            { value: 25, name: '4层以上' }
          ]
        }]
      }
      this.charts.pathChart.setOption(option)
    },
    drawRiskChart() {
      const option = {
        title: { text: '风险分析', left: 'center' },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'gauge',
          min: 0,
          max: 100,
          splitNumber: 10,
          axisLine: {
            lineStyle: {
              color: [[0.3, '#67C23A'], [0.7, '#E6A23C'], [1, '#F56C6C']]
            }
          },
          data: [{ value: 35, name: '风险指数' }]
        }]
      }
      this.charts.riskChart.setOption(option)
    },
    executeAnalysis() {
      this.$message.success('分析已执行')
      this.loadAnalysisData()
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.analysis-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.mb-10 {
  margin-bottom: 10px;
}
.dialog-footer {
  text-align: right;
}
</style>