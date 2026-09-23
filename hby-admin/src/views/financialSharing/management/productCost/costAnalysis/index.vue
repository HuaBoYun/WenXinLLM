<template>
  <div class="cost-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          成本分析
        </h1>
        <p class="page-description">分析产品成本构成、趋势变化、对比分析等成本分析业务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshAnalysis">
          刷新分析
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportAnalysis">
          导出报告
        </el-button>
        <el-button type="warning" icon="el-icon-setting" @click="handleAnalysisConfig">
          分析配置
        </el-button>
      </div>
    </div>

    <!-- 分析条件 -->
    <div class="analysis-conditions">
      <el-card title="分析条件">
        <div slot="header">
          <span>分析条件</span>
        </div>
        <el-form :model="analysisForm" ref="analysisForm" :inline="true" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="分析类型" prop="analysisType">
                <el-select v-model="analysisForm.analysisType" placeholder="请选择分析类型" style="width: 100%" @change="handleAnalysisTypeChange">
                  <el-option label="成本构成分析" value="composition" />
                  <el-option label="成本趋势分析" value="trend" />
                  <el-option label="成本对比分析" value="comparison" />
                  <el-option label="差异分析" value="variance" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="分析期间" prop="analysisPeriod">
                <el-date-picker
                  v-model="analysisForm.analysisPeriod"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="产品范围" prop="productScope">
                <el-select v-model="analysisForm.productScope" placeholder="请选择产品范围" style="width: 100%">
                  <el-option label="全部产品" value="all" />
                  <el-option label="指定产品" value="selected" />
                  <el-option label="产品分类" value="category" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-button type="primary" @click="executeAnalysis" :loading="analyzing">
                  开始分析
                </el-button>
                <el-button @click="resetAnalysisForm">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>

    <!-- 分析结果 -->
    <div class="analysis-results">
      <!-- 成本构成分析 -->
      <el-card v-if="analysisForm.analysisType === 'composition'" title="成本构成分析">
        <div slot="header">
          <span>成本构成分析</span>
          <div style="float: right">
            <el-radio-group v-model="compositionViewType" size="small">
              <el-radio-button label="chart">图表</el-radio-button>
              <el-radio-button label="table">表格</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        
        <div v-if="compositionViewType === 'chart'" class="chart-container">
          <div ref="compositionChart" style="width: 100%; height: 400px;"></div>
        </div>
        
        <div v-else>
          <el-table :data="compositionData" border stripe>
            <el-table-column label="产品名称" prop="productName" min-width="150" />
            <el-table-column label="总成本" prop="totalCost" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.totalCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="直接材料" prop="directMaterial" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.directMaterial) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="直接人工" prop="directLabor" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.directLabor) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="制造费用" prop="manufacturingOverhead" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.manufacturingOverhead) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="材料占比" prop="materialRatio" width="100" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.materialRatio }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="人工占比" prop="laborRatio" width="100" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.laborRatio }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="费用占比" prop="overheadRatio" width="100" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.overheadRatio }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>

      <!-- 成本趋势分析 -->
      <el-card v-if="analysisForm.analysisType === 'trend'" title="成本趋势分析">
        <div slot="header">
          <span>成本趋势分析</span>
          <div style="float: right">
            <el-radio-group v-model="trendViewType" size="small">
              <el-radio-button label="line">折线图</el-radio-button>
              <el-radio-button label="bar">柱状图</el-radio-button>
              <el-radio-button label="table">表格</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        
        <div v-if="trendViewType !== 'table'" class="chart-container">
          <div ref="trendChart" style="width: 100%; height: 400px;"></div>
        </div>
        
        <div v-else>
          <el-table :data="trendData" border stripe>
            <el-table-column label="期间" prop="period" width="100" />
            <el-table-column label="产品名称" prop="productName" min-width="150" />
            <el-table-column label="总成本" prop="totalCost" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.totalCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="单位成本" prop="unitCost" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.unitCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="环比变化" prop="monthOnMonthChange" width="100" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.monthOnMonthChange >= 0 ? 'text-success' : 'text-danger'">
                  {{ scope.row.monthOnMonthChange >= 0 ? '+' : '' }}{{ scope.row.monthOnMonthChange }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column label="同比变化" prop="yearOnYearChange" width="100" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.yearOnYearChange >= 0 ? 'text-success' : 'text-danger'">
                  {{ scope.row.yearOnYearChange >= 0 ? '+' : '' }}{{ scope.row.yearOnYearChange }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>

      <!-- 成本对比分析 -->
      <el-card v-if="analysisForm.analysisType === 'comparison'" title="成本对比分析">
        <div slot="header">
          <span>成本对比分析</span>
          <div style="float: right">
            <el-select v-model="comparisonType" size="small" style="width: 120px">
              <el-option label="产品对比" value="product" />
              <el-option label="期间对比" value="period" />
              <el-option label="预算对比" value="budget" />
            </el-select>
          </div>
        </div>
        
        <div class="comparison-container">
          <el-table :data="comparisonData" border stripe>
            <el-table-column label="对比项目" prop="comparisonItem" min-width="150" />
            <el-table-column label="基准值" prop="baseValue" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.baseValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="对比值" prop="compareValue" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.compareValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="差异金额" prop="varianceAmount" width="120" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.varianceAmount >= 0 ? 'text-success' : 'text-danger'">
                  {{ formatAmount(scope.row.varianceAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="差异率" prop="varianceRate" width="100" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.varianceRate >= 0 ? 'text-success' : 'text-danger'">
                  {{ scope.row.varianceRate >= 0 ? '+' : '' }}{{ scope.row.varianceRate }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column label="分析说明" prop="analysisNote" min-width="200" />
          </el-table>
        </div>
      </el-card>

      <!-- 差异分析 -->
      <el-card v-if="analysisForm.analysisType === 'variance'" title="差异分析">
        <div slot="header">
          <span>差异分析</span>
          <div style="float: right">
            <el-select v-model="varianceType" size="small" style="width: 120px">
              <el-option label="价格差异" value="price" />
              <el-option label="用量差异" value="quantity" />
              <el-option label="效率差异" value="efficiency" />
            </el-select>
          </div>
        </div>
        
        <div class="variance-container">
          <el-table :data="varianceData" border stripe>
            <el-table-column label="差异项目" prop="varianceItem" min-width="150" />
            <el-table-column label="标准成本" prop="standardCost" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.standardCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="实际成本" prop="actualCost" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.actualCost) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="差异金额" prop="varianceAmount" width="120" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.varianceAmount >= 0 ? 'text-danger' : 'text-success'">
                  {{ formatAmount(scope.row.varianceAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="差异性质" prop="varianceNature" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.varianceNature === '有利' ? 'success' : 'danger'">
                  {{ scope.row.varianceNature }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="差异原因" prop="varianceReason" min-width="200" />
            <el-table-column label="改进建议" prop="improvementSuggestion" min-width="200" />
          </el-table>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCostCompositionAnalysis, getCostTrendAnalysis, getCostComparisonAnalysis, getCostVarianceAnalysis, generateCostAnalysisReport, exportProductCostDataBlob } from '@/api/financialSharing/productCost'

export default {
  name: 'CostAnalysis',
  data() {
    return {
      analyzing: false,
      analysisForm: {
        analysisType: 'composition',
        analysisPeriod: [],
        productScope: 'all',
        productIds: []
      },
      compositionViewType: 'table',
      trendViewType: 'table',
      comparisonType: 'product',
      varianceType: 'price',
      compositionData: [],
      trendData: [],
      comparisonData: [],
      varianceData: [],
      compositionChartInstance: null,
      trendChartInstance: null
    }
  },
  watch: {
    compositionViewType(val) {
      if (val === 'chart') {
        this.$nextTick(() => {
          this.renderCompositionChart()
        })
      }
    },
    trendViewType(val) {
      if (val !== 'table') {
        this.$nextTick(() => {
          this.renderTrendChart()
        })
      }
    },
    compositionData() {
      if (this.compositionViewType === 'chart') {
        this.$nextTick(() => {
          this.renderCompositionChart()
        })
      }
    },
    trendData() {
      if (this.trendViewType !== 'table') {
        this.$nextTick(() => {
          this.renderTrendChart()
        })
      }
    }
  },
  mounted() {
    this._resizeHandler = () => {
      if (this.compositionChartInstance) this.compositionChartInstance.resize()
      if (this.trendChartInstance) this.trendChartInstance.resize()
    }
    window.addEventListener('resize', this._resizeHandler)
  },
  created() {
    this.initAnalysisPeriod()
    this.executeAnalysis()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this._resizeHandler)
    if (this.compositionChartInstance) {
      this.compositionChartInstance.dispose()
      this.compositionChartInstance = null
    }
    if (this.trendChartInstance) {
      this.trendChartInstance.dispose()
      this.trendChartInstance = null
    }
  },
  methods: {
    // 初始化分析期间
    initAnalysisPeriod() {
      const now = new Date()
      const currentMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
      const lastMonth = `${now.getFullYear()}-${String(now.getMonth()).padStart(2, '0')}`
      this.analysisForm.analysisPeriod = [lastMonth, currentMonth]
    },

    // 加载成本构成分析数据
    async loadCompositionData() {
      try {
        const response = await getCostCompositionAnalysis(this.analysisForm)
        if (response.code === 1) {
          this.compositionData = response.data || []
        }
      } catch (error) {
        console.error('获取成本构成分析失败：', error)
      }
    },

    // 加载成本趋势分析数据
    async loadTrendData() {
      try {
        const response = await getCostTrendAnalysis(this.analysisForm)
        if (response.code === 1) {
          this.trendData = response.data || []
        }
      } catch (error) {
        console.error('获取成本趋势分析失败：', error)
      }
    },

    // 加载成本对比分析数据
    async loadComparisonData() {
      try {
        const response = await getCostComparisonAnalysis(this.analysisForm)
        if (response.code === 1) {
          this.comparisonData = response.data || []
        }
      } catch (error) {
        console.error('获取成本对比分析失败：', error)
      }
    },

    // 加载成本差异分析数据
    async loadVarianceData() {
      try {
        const response = await getCostVarianceAnalysis(this.analysisForm)
        if (response.code === 1) {
          this.varianceData = response.data || []
        }
      } catch (error) {
        console.error('获取成本差异分析失败：', error)
      }
    },

    // 执行分析
    async executeAnalysis() {
      this.analyzing = true
      try {
        switch (this.analysisForm.analysisType) {
          case 'composition':
            await this.loadCompositionData()
            break
          case 'trend':
            await this.loadTrendData()
            break
          case 'comparison':
            await this.loadComparisonData()
            break
          case 'variance':
            await this.loadVarianceData()
            break
        }
      } finally {
        this.analyzing = false
      }
    },

    // 分析类型变化
    handleAnalysisTypeChange() {
      // 根据分析类型调整界面
    },

    // 重置分析表单
    resetAnalysisForm() {
      this.$refs.analysisForm.resetFields()
      this.initAnalysisPeriod()
    },

    // 刷新分析
    refreshAnalysis() {
      this.executeAnalysis()
    },

    // 导出分析报告
    async exportAnalysis() {
      try {
        const response = await exportProductCostDataBlob({ type: 'analysis', ...this.analysisForm })
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '成本分析报告.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出分析报告失败')
      }
    },

    // 分析配置
    handleAnalysisConfig() {
      this.$message.info('分析配置功能包含：成本分类规则、分摊比例设置、分析周期配置、预警阈值设定，详细配置请联系管理员')
    },

    // 渲染成本构成饼图
    renderCompositionChart() {
      if (!this.$refs.compositionChart) return
      if (this.compositionChartInstance) {
        this.compositionChartInstance.dispose()
      }
      this.compositionChartInstance = echarts.init(this.$refs.compositionChart)
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      // 聚合所有产品的成本构成
      let totalMaterial = 0
      let totalLabor = 0
      let totalOverhead = 0
      this.compositionData.forEach(item => {
        totalMaterial += Number(item.directMaterial) || 0
        totalLabor += Number(item.directLabor) || 0
        totalOverhead += Number(item.manufacturingOverhead) || 0
      })
      const option = {
        color: colors,
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['直接材料', '直接人工', '制造费用']
        },
        series: [
          {
            name: '成本构成',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: true,
            label: {
              show: true,
              formatter: '{b}\n{d}%'
            },
            data: [
              { value: totalMaterial, name: '直接材料' },
              { value: totalLabor, name: '直接人工' },
              { value: totalOverhead, name: '制造费用' }
            ]
          }
        ]
      }
      this.compositionChartInstance.setOption(option)
    },

    // 渲染成本趋势图表
    renderTrendChart() {
      if (!this.$refs.trendChart) return
      if (this.trendChartInstance) {
        this.trendChartInstance.dispose()
      }
      this.trendChartInstance = echarts.init(this.$refs.trendChart)
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      const periods = [...new Set(this.trendData.map(item => item.period))]
      const totalCosts = periods.map(period => {
        const items = this.trendData.filter(item => item.period === period)
        return items.reduce((sum, item) => sum + (Number(item.totalCost) || 0), 0)
      })
      const chartType = this.trendViewType === 'bar' ? 'bar' : 'line'
      const option = {
        color: colors,
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总成本']
        },
        xAxis: {
          type: 'category',
          data: periods,
          axisLabel: { rotate: periods.length > 6 ? 30 : 0 }
        },
        yAxis: {
          type: 'value',
          name: '金额'
        },
        series: [
          {
            name: '总成本',
            type: chartType,
            data: totalCosts,
            smooth: chartType === 'line'
          }
        ]
      }
      this.trendChartInstance.setOption(option)
    },

    // 格式化金额
    formatAmount(amount) {
      if (amount == null || amount === '') return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-analysis-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}

.analysis-conditions {
  margin-bottom: 20px;
}

.analysis-results {
  .chart-container {
    min-height: 400px;
    border: 1px dashed #dcdfe6;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}
</style>
