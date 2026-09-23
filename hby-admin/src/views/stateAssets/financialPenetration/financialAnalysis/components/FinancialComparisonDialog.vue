<template>
  <el-dialog
    title="财务数据对比分析"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <div class="comparison-content">
      <el-form ref="comparisonForm" :model="comparisonForm" :inline="true" label-width="100px" class="mb-20">
        <el-form-item label="对比维度">
          <el-select v-model="comparisonForm.dimension" placeholder="请选择对比维度" @change="handleDimensionChange">
            <el-option label="时间对比" value="TIME"></el-option>
            <el-option label="企业对比" value="ENTERPRISE"></el-option>
            <el-option label="行业对比" value="INDUSTRY"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item v-if="comparisonForm.dimension === 'TIME'" label="时间范围">
          <el-date-picker
            v-model="comparisonForm.timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleTimeChange"
          />
        </el-form-item>
        
        <el-form-item v-if="comparisonForm.dimension === 'ENTERPRISE'" label="对比企业">
          <el-select v-model="comparisonForm.enterprises" multiple placeholder="请选择对比企业" @change="handleEnterpriseChange">
            <el-option label="示例云科技" value="HBY001"></el-option>
            <el-option label="创新科技" value="CX002"></el-option>
            <el-option label="智能制造" value="ZN003"></el-option>
            <el-option label="数字化公司" value="SZ004"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item v-if="comparisonForm.dimension === 'INDUSTRY'" label="对比行业">
          <el-select v-model="comparisonForm.industries" multiple placeholder="请选择对比行业" @change="handleIndustryChange">
            <el-option label="软件和信息技术服务业" value="IT"></el-option>
            <el-option label="制造业" value="MANUFACTURING"></el-option>
            <el-option label="金融业" value="FINANCE"></el-option>
            <el-option label="房地产业" value="REAL_ESTATE"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="generateComparison">生成对比</el-button>
          <el-button @click="resetComparison">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div v-if="showComparison" class="comparison-results">
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">
                <span>营收利润对比</span>
              </div>
              <div ref="revenueProfitComparisonChart" class="chart-container"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">
                <span>财务指标对比</span>
              </div>
              <div ref="indicatorComparisonChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">
                <span>资产负债对比</span>
              </div>
              <div ref="assetLiabilityComparisonChart" class="chart-container"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">
                <span>现金流对比</span>
              </div>
              <div ref="cashFlowComparisonChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-card shadow="never">
          <div slot="header">
            <span>对比数据表</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="exportComparison"
            >
              导出数据
            </el-button>
          </div>
          <el-table :data="comparisonData" border>
            <el-table-column label="对比项" prop="item" width="150" />
            <el-table-column v-for="(column, index) in dynamicColumns" :key="index" :label="column.label" :prop="column.prop" align="center" />
            <el-table-column label="差异分析" prop="analysis" />
          </el-table>
        </el-card>
      </div>
      
      <div v-else class="no-comparison">
        <el-empty description="请选择对比维度并生成对比分析">
          <el-button type="primary" @click="generateComparison">开始对比</el-button>
        </el-empty>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="showComparison" type="primary" @click="saveComparison">保存对比</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getFinancialComparisonAnalysis, exportFinancialAnalysisData } from '@/api/stateAssets/financialAnalysis'

export default {
  name: 'FinancialComparisonDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      comparisonResult: null,
      comparisonForm: {
        dimension: '',
        timeRange: [],
        enterprises: [],
        industries: []
      },
      showComparison: false,
      comparisonData: [],
      dynamicColumns: [],
      // 图表实例
      revenueProfitComparisonChart: null,
      indicatorComparisonChart: null,
      assetLiabilityComparisonChart: null,
      cashFlowComparisonChart: null
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
      }
    }
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    initData() {
      this.resetComparison()
    },
    
    handleDimensionChange() {
      this.showComparison = false
      this.comparisonForm.timeRange = []
      this.comparisonForm.enterprises = []
      this.comparisonForm.industries = []
    },
    
    handleTimeChange() {
      // 时间范围变化处理
    },
    
    handleEnterpriseChange() {
      // 企业选择变化处理
    },
    
    handleIndustryChange() {
      // 行业选择变化处理
    },
    
    async generateComparison() {
      if (!this.comparisonForm.dimension) {
        this.$message.warning('请选择对比维度')
        return
      }
      this.loading = true
      try {
        const params = {
          dimension: this.comparisonForm.dimension,
          timeRange: this.comparisonForm.timeRange,
          enterprises: this.comparisonForm.enterprises,
          industries: this.comparisonForm.industries
        }
        if (this.analysisData && this.analysisData.batchList) {
          params.ids = this.analysisData.batchList.map(item => item.id || item.statementId)
        } else if (this.analysisData && this.analysisData.id) {
          params.ids = [this.analysisData.id]
        }
        const response = await getFinancialComparisonAnalysis(params)
        if (response && response.data) {
          this.comparisonResult = response.data
          this.comparisonData = response.data.comparisonTable || []
          this.dynamicColumns = response.data.dynamicColumns || []
          this.showComparison = true
          this.$nextTick(() => {
            this.initCharts()
            this.updateChartsFromData(response.data.chartData)
          })
        }
      } catch (error) {
        this.$message.error('对比分析失败：' + (error.message || '请稍后重试'))
      } finally {
        this.loading = false
      }
    },

    initCharts() {
      this.revenueProfitComparisonChart = echarts.init(this.$refs.revenueProfitComparisonChart)
      this.indicatorComparisonChart = echarts.init(this.$refs.indicatorComparisonChart)
      this.assetLiabilityComparisonChart = echarts.init(this.$refs.assetLiabilityComparisonChart)
      this.cashFlowComparisonChart = echarts.init(this.$refs.cashFlowComparisonChart)
      window.addEventListener('resize', this.handleResize)
    },

    updateChartsFromData(chartData) {
      if (!chartData) return
      const xAxis = chartData.xAxis || []
      // 营收利润对比图
      this.revenueProfitComparisonChart.setOption({
        title: { text: '营收利润对比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业收入', '净利润'], bottom: 10 },
        xAxis: { type: 'category', data: xAxis },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: [
          { name: '营业收入', type: 'bar', data: chartData.revenue || [], itemStyle: { color: '#409EFF' } },
          { name: '净利润', type: 'bar', data: chartData.profit || [], itemStyle: { color: '#67C23A' } }
        ]
      }, true)
      // 财务指标对比图
      this.indicatorComparisonChart.setOption({
        title: { text: '财务指标对比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['ROE', 'ROA'], bottom: 10 },
        xAxis: { type: 'category', data: xAxis },
        yAxis: { type: 'value', name: '百分比(%)' },
        series: [
          { name: 'ROE', type: 'line', data: chartData.roe || [], smooth: true, itemStyle: { color: '#E6A23C' } },
          { name: 'ROA', type: 'line', data: chartData.roa || [], smooth: true, itemStyle: { color: '#F56C6C' } }
        ]
      }, true)
      // 资产负债对比图
      this.assetLiabilityComparisonChart.setOption({
        title: { text: '资产负债对比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['总资产', '总负债'], bottom: 10 },
        xAxis: { type: 'category', data: xAxis },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: [
          { name: '总资产', type: 'bar', data: chartData.assets || [], itemStyle: { color: '#909399' } },
          { name: '总负债', type: 'bar', data: chartData.liabilities || [], itemStyle: { color: '#F56C6C' } }
        ]
      }, true)
      // 现金流对比图
      this.cashFlowComparisonChart.setOption({
        title: { text: '现金流对比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['经营现金流', '投资现金流', '筹资现金流'], bottom: 10 },
        xAxis: { type: 'category', data: xAxis },
        yAxis: { type: 'value', name: '金额(万元)' },
        series: [
          { name: '经营现金流', type: 'bar', data: chartData.operatingCashflow || [], itemStyle: { color: '#67C23A' } },
          { name: '投资现金流', type: 'bar', data: chartData.investingCashflow || [], itemStyle: { color: '#E6A23C' } },
          { name: '筹资现金流', type: 'bar', data: chartData.financingCashflow || [], itemStyle: { color: '#409EFF' } }
        ]
      }, true)
    },
    
    resetComparison() {
      this.comparisonForm = {
        dimension: '',
        timeRange: [],
        enterprises: [],
        industries: []
      }
      this.showComparison = false
      this.destroyCharts()
    },
    
    async exportComparison() {
      try {
        const response = await exportFinancialAnalysisData({ exportType: 'comparison', ...this.comparisonForm })
        if (response && response.data) {
          const blob = new Blob([response.data])
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '财务对比分析数据.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        }
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    
    async saveComparison() {
      if (!this.comparisonResult) {
        this.$message.warning('请先生成对比分析')
        return
      }
      try {
        const response = await exportFinancialAnalysisData({
          exportType: 'comparison',
          dimension: this.comparisonForm.dimension,
          ids: this.analysisData && this.analysisData.batchList
            ? this.analysisData.batchList.map(item => item.id || item.statementId)
            : (this.analysisData && this.analysisData.id ? [this.analysisData.id] : [])
        })
        if (response && response.data) {
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '财务对比分析_' + new Date().getTime() + '.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
        }
        this.$message.success('对比分析已导出保存')
      } catch (error) {
        this.$message.error('保存失败：' + (error.message || '请稍后重试'))
      }
    },
    
    handleClose() {
      this.$emit('update:visible', false)
      this.destroyCharts()
    },
    
    handleResize() {
      if (this.revenueProfitComparisonChart) this.revenueProfitComparisonChart.resize()
      if (this.indicatorComparisonChart) this.indicatorComparisonChart.resize()
      if (this.assetLiabilityComparisonChart) this.assetLiabilityComparisonChart.resize()
      if (this.cashFlowComparisonChart) this.cashFlowComparisonChart.resize()
    },
    
    destroyCharts() {
      if (this.revenueProfitComparisonChart) {
        this.revenueProfitComparisonChart.dispose()
        this.revenueProfitComparisonChart = null
      }
      if (this.indicatorComparisonChart) {
        this.indicatorComparisonChart.dispose()
        this.indicatorComparisonChart = null
      }
      if (this.assetLiabilityComparisonChart) {
        this.assetLiabilityComparisonChart.dispose()
        this.assetLiabilityComparisonChart = null
      }
      if (this.cashFlowComparisonChart) {
        this.cashFlowComparisonChart.dispose()
        this.cashFlowComparisonChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    }
  }
}
</script>

<style scoped>
.comparison-content {
  min-height: 600px;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.no-comparison {
  text-align: center;
  padding: 100px 0;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
