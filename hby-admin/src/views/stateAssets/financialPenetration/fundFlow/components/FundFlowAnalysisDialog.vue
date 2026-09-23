<template>
  <el-dialog
    title="资金流向分析"
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
import { analyzeFundFlow } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowAnalysisDialog',
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
        this.$nextTick(() => {
          this.initCharts()
          this.loadAnalysisData()
        })
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
    async loadAnalysisData() {
      this.loading = true
      try {
        const fundFlowId = this.data.fundFlowId || this.data.id || this.data.partyId
        const res = await analyzeFundFlow({ fundFlowId })
        const result = res.data || {}
        this.$nextTick(() => {
          this.drawAmountChart(result.monthlyAmount || {})
          this.drawTimeChart(result.monthlyCount || {})
          this.drawPathChart(result.typeDistribution || {})
          this.drawRiskChart(result.riskScore || 0)
        })
        this.suggestions = result.suggestions || []
      } catch (e) {
        console.error('资金流向分析请求失败', e)
        this.$nextTick(() => {
          this.drawAmountChart({})
          this.drawTimeChart({})
          this.drawPathChart({})
          this.drawRiskChart(0)
        })
        this.suggestions = []
      } finally {
        this.loading = false
      }
    },
    drawAmountChart(monthlyAmount) {
      const keys = Object.keys(monthlyAmount)
      const values = Object.values(monthlyAmount)
      const option = {
        title: { text: '金额分析', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: keys },
        yAxis: { type: 'value' },
        series: [{
          data: values,
          type: 'line',
          smooth: true
        }]
      }
      if (this.charts.amountChart) {
        this.charts.amountChart.setOption(option, true)
      }
    },
    drawTimeChart(monthlyCount) {
      const keys = Object.keys(monthlyCount)
      const values = Object.values(monthlyCount)
      const option = {
        title: { text: '时间分析', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: keys },
        yAxis: { type: 'value' },
        series: [{
          data: values,
          type: 'bar'
        }]
      }
      if (this.charts.timeChart) {
        this.charts.timeChart.setOption(option, true)
      }
    },
    drawPathChart(typeDistribution) {
      const pieData = Object.entries(typeDistribution).map(([name, value]) => ({ name, value }))
      const option = {
        title: { text: '路径分析', left: 'center' },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          data: pieData
        }]
      }
      if (this.charts.pathChart) {
        this.charts.pathChart.setOption(option, true)
      }
    },
    drawRiskChart(riskScore) {
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
          data: [{ value: riskScore, name: '风险指数' }]
        }]
      }
      if (this.charts.riskChart) {
        this.charts.riskChart.setOption(option, true)
      }
    },
    async executeAnalysis() {
      this.$message.info('正在执行分析...')
      await this.loadAnalysisData()
      this.$message.success('分析完成')
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