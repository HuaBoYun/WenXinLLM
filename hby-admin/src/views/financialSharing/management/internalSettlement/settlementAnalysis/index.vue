<template>
  <div class="settlement-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          结算分析
        </h1>
        <p class="page-description">分析内部结算的效果和效率，提供优化建议报告</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshAnalysis">
          刷新分析
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportReport">
          导出报告
        </el-button>
        <el-button type="warning" icon="el-icon-setting" @click="analysisSettings">
          分析设置
        </el-button>
      </div>
    </div>

    <!-- 分析维度选择 -->
    <el-card shadow="never" class="dimension-card">
      <div class="dimension-header">
        <h3>分析维度</h3>
        <p>选择不同的分析维度查看结算效果</p>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="分析期间">
            <el-date-picker
              v-model="analysisForm.period"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              format="yyyy-MM"
              value-format="yyyy-MM"
              @change="handlePeriodChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="分析类型">
            <el-select v-model="analysisForm.analysisType" @change="handleTypeChange">
              <el-option label="结算效率分析" :value="1" />
              <el-option label="交易结构分析" :value="2" />
              <el-option label="利润贡献分析" :value="3" />
              <el-option label="成本效益分析" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="分析对象">
            <el-select v-model="analysisForm.analysisObject" multiple collapse-tags>
              <el-option label="销售中心" :value="1" />
              <el-option label="生产中心" :value="2" />
              <el-option label="采购中心" :value="3" />
              <el-option label="财务中心" :value="4" />
              <el-option label="研发中心" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button type="primary" @click="performAnalysis">执行分析</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 分析结果展示 -->
    <el-row :gutter="20">
      <!-- 左侧图表区域 -->
      <el-col :span="16">
        <el-card shadow="never" class="chart-card">
          <div class="chart-header">
            <h3>{{ getAnalysisTitle() }}</h3>
            <div class="chart-controls">
              <el-radio-group v-model="chartType" size="small" @change="handleChartTypeChange">
                <el-radio-button label="line">趋势图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="pie">饼图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="chart-container" ref="chartContainer" v-loading="chartLoading">
            <!-- 这里应该集成图表组件，如ECharts -->
            <div class="chart-placeholder">
              <i class="el-icon-data-line chart-icon"></i>
              <p>图表数据加载中...</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧指标区域 -->
      <el-col :span="8">
        <el-card shadow="never" class="metrics-card">
          <div class="metrics-header">
            <h3>关键指标</h3>
          </div>
          <div class="metrics-list">
            <div class="metric-item" v-for="metric in keyMetrics" :key="metric.key">
              <div class="metric-label">{{ metric.label }}</div>
              <div class="metric-value" :class="metric.trend">
                {{ metric.value }}
                <i :class="getTrendIcon(metric.trend)" v-if="metric.trend"></i>
              </div>
              <div class="metric-change" v-if="metric.change">
                {{ metric.change }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细分析表格 -->
    <el-card shadow="never" class="analysis-table-card">
      <div class="table-header">
        <h3>详细分析数据</h3>
        <div class="table-controls">
          <el-button size="small" @click="exportTableData">导出数据</el-button>
        </div>
      </div>
      <el-table
        v-loading="tableLoading"
        :data="analysisData"
        stripe
        border
        max-height="400"
      >
        <el-table-column prop="centerName" label="中心名称" width="150" />
        <el-table-column prop="settlementCount" label="结算笔数" width="100" align="right" />
        <el-table-column prop="settlementAmount" label="结算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.settlementAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avgSettlementTime" label="平均结算时间(天)" width="140" align="right" />
        <el-table-column prop="settlementEfficiency" label="结算效率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getEfficiencyClass(scope.row.settlementEfficiency)">
              {{ scope.row.settlementEfficiency }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="profitContribution" label="利润贡献" width="120" align="right">
          <template slot-scope="scope">
            <span class="profit-text">{{ formatAmount(scope.row.profitContribution) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="costSavings" label="成本节约" width="120" align="right">
          <template slot-scope="scope">
            <span class="savings-text">{{ formatAmount(scope.row.costSavings) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskTag(scope.row.riskLevel)">
              {{ getRiskName(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 优化建议 -->
    <el-card shadow="never" class="suggestions-card">
      <div class="suggestions-header">
        <h3>
          <i class="el-icon-lightbulb"></i>
          优化建议
        </h3>
      </div>
      <div class="suggestions-list">
        <div class="suggestion-item" v-for="(suggestion, index) in optimizationSuggestions" :key="index">
          <div class="suggestion-priority" :class="suggestion.priority">
            {{ getPriorityName(suggestion.priority) }}
          </div>
          <div class="suggestion-content">
            <h4>{{ suggestion.title }}</h4>
            <p>{{ suggestion.description }}</p>
            <div class="suggestion-actions">
              <el-button size="mini" type="text" @click="viewSuggestionDetail(suggestion)">查看详情</el-button>
              <el-button size="mini" type="text" @click="implementSuggestion(suggestion)">实施建议</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getSettlementAnalysis, analyzeSettlementEfficiency, generateOptimizationSuggestions, exportAnalysisReport } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'SettlementAnalysis',
  data() {
    return {
      chartLoading: false,
      tableLoading: false,
      chartType: 'line',
      analysisForm: {
        period: [],
        analysisType: 1,
        analysisObject: []
      },
      keyMetrics: [],
      analysisData: [],
      optimizationSuggestions: []
    }
  },
  mounted() {
    this.initAnalysis()
  },
  methods: {
    initAnalysis() {
      // 初始化分析数据
      const currentDate = new Date()
      const currentMonth = currentDate.getFullYear() + '-' + String(currentDate.getMonth() + 1).padStart(2, '0')
      const lastMonth = currentDate.getFullYear() + '-' + String(currentDate.getMonth()).padStart(2, '0')

      this.analysisForm.period = [lastMonth, currentMonth]
      this.analysisForm.analysisObject = [1, 2, 3, 4, 5]

      this.performAnalysis()
      this.loadKeyMetrics()
      this.loadOptimizationSuggestions()
    },
    async loadKeyMetrics() {
      try {
        const params = {
          startPeriod: this.analysisForm.period[0],
          endPeriod: this.analysisForm.period[1],
          analysisObject: this.analysisForm.analysisObject
        }
        const response = await analyzeSettlementEfficiency(params)
        if (response.code === 1) {
          this.keyMetrics = response.data || []
        }
      } catch (error) {
        console.error('加载关键指标失败:', error)
      }
    },
    async loadOptimizationSuggestions() {
      try {
        const params = {
          startPeriod: this.analysisForm.period[0],
          endPeriod: this.analysisForm.period[1],
          analysisType: this.analysisForm.analysisType,
          analysisObject: this.analysisForm.analysisObject
        }
        const response = await generateOptimizationSuggestions(params)
        if (response.code === 1) {
          this.optimizationSuggestions = response.data || []
        }
      } catch (error) {
        console.error('加载优化建议失败:', error)
      }
    },
    async performAnalysis() {
      this.chartLoading = true
      this.tableLoading = true

      try {
        const params = {
          ...this.analysisForm,
          startPeriod: this.analysisForm.period[0],
          endPeriod: this.analysisForm.period[1]
        }

        const response = await getSettlementAnalysis(params)
        if (response.code === 1) {
          this.analysisData = response.data.analysisData || []
          this.updateChart(response.data.chartData)
        }
      } catch (error) {
        this.$message.error('分析数据加载失败')
      } finally {
        this.chartLoading = false
        this.tableLoading = false
      }
    },
    updateChart(chartData) {
      // 这里应该更新图表数据
      // 暂未对接图表 API，先以空状态展示
      console.log('更新图表数据:', chartData)
    },
    handlePeriodChange() {
      this.performAnalysis()
      this.loadKeyMetrics()
      this.loadOptimizationSuggestions()
    },
    handleTypeChange() {
      this.performAnalysis()
      this.loadOptimizationSuggestions()
    },
    handleChartTypeChange() {
      this.updateChart()
    },
    refreshAnalysis() {
      this.performAnalysis()
      this.loadKeyMetrics()
      this.loadOptimizationSuggestions()
    },
    async exportReport() {
      try {
        const response = await exportAnalysisReport(this.analysisForm)
        if (response.code === 1) {
          this.$message.success('报告导出成功')
        }
      } catch (error) {
        this.$message.error('报告导出失败')
      }
    },
    exportTableData() {
      try {
        const data = this.tableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '结算分析数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('数据导出成功')
      } catch (error) {
        this.$message.error('数据导出失败')
      }
    },
    analysisSettings() {
      const content = `
        <p><b>当前分析配置：</b></p>
        <p>分析类型：${this.analysisForm.analysisType || '综合分析'}</p>
        <p>分析对象：${this.analysisForm.analysisObject || '全部'}</p>
        <p>分析周期：${(this.analysisForm.period && this.analysisForm.period.join(' ~ ')) || '未设置'}</p>
        <p style="margin-top:10px;color:#909399;">提示：请通过上方筛选条件调整分析参数</p>
      `
      this.$alert(content, '分析设置', { dangerouslyUseHTMLString: true })
    },
    viewSuggestionDetail(suggestion) {
      this.$message.info(`查看建议详情: ${suggestion.title}`)
    },
    implementSuggestion(suggestion) {
      this.$message.info(`实施建议: ${suggestion.title}`)
    },
    getAnalysisTitle() {
      const titles = {
        1: '结算效率分析',
        2: '交易结构分析',
        3: '利润贡献分析',
        4: '成本效益分析'
      }
      return titles[this.analysisForm.analysisType] || '分析图表'
    },
    getTrendIcon(trend) {
      return trend === 'up' ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'
    },
    getEfficiencyClass(efficiency) {
      if (efficiency >= 90) return 'efficiency-high'
      if (efficiency >= 70) return 'efficiency-medium'
      return 'efficiency-low'
    },
    getRiskName(level) {
      const levels = { 1: '低', 2: '中', 3: '高' }
      return levels[level] || '未知'
    },
    getRiskTag(level) {
      const tags = { 1: 'success', 2: 'warning', 3: 'danger' }
      return tags[level] || ''
    },
    getPriorityName(priority) {
      const names = { high: '高', medium: '中', low: '低' }
      return names[priority] || '未知'
    },
    formatAmount(amount) {
      return amount ? `¥${(amount / 10000).toFixed(2)}万` : '¥0.00万'
    }
  }
}
</script>

<style lang="scss" scoped>
.settlement-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #9c27b0;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 8px;
    }
  }
}

.dimension-card {
  margin-bottom: 20px;

  .dimension-header {
    margin-bottom: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
}

.chart-card {
  margin-bottom: 20px;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .chart-container {
    height: 400px;
    display: flex;
    align-items: center;
    justify-content: center;

    .chart-placeholder {
      text-align: center;
      color: #909399;

      .chart-icon {
        font-size: 48px;
        margin-bottom: 16px;
      }

      p {
        margin: 0;
        font-size: 14px;
      }
    }
  }
}

.metrics-card {
  margin-bottom: 20px;

  .metrics-header {
    margin-bottom: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .metrics-list {
    .metric-item {
      padding: 16px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .metric-label {
        font-size: 14px;
        color: #606266;
        margin-bottom: 8px;
      }

      .metric-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
        display: flex;
        align-items: center;

        &.up {
          color: #67c23a;
        }

        &.down {
          color: #f56c6c;
        }

        i {
          margin-left: 8px;
          font-size: 16px;
        }
      }

      .metric-change {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.analysis-table-card {
  margin-bottom: 20px;

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }
}

.suggestions-card {
  .suggestions-header {
    margin-bottom: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #e6a23c;
      }
    }
  }

  .suggestions-list {
    .suggestion-item {
      display: flex;
      padding: 16px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .suggestion-priority {
        width: 60px;
        height: 24px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        color: white;
        margin-right: 16px;
        flex-shrink: 0;

        &.high {
          background: #f56c6c;
        }

        &.medium {
          background: #e6a23c;
        }

        &.low {
          background: #67c23a;
        }
      }

      .suggestion-content {
        flex: 1;

        h4 {
          font-size: 14px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;
        }

        p {
          font-size: 14px;
          color: #606266;
          line-height: 1.5;
          margin: 0 0 12px 0;
        }

        .suggestion-actions {
          .el-button {
            margin-right: 8px;
          }
        }
      }
    }
  }
}

.amount-text {
  font-weight: 600;
  color: #f56c6c;
}

.profit-text {
  font-weight: 600;
  color: #67c23a;
}

.savings-text {
  font-weight: 600;
  color: #409eff;
}

.efficiency-high {
  color: #67c23a;
  font-weight: 600;
}

.efficiency-medium {
  color: #e6a23c;
  font-weight: 600;
}

.efficiency-low {
  color: #f56c6c;
  font-weight: 600;
}

.trend-up {
  color: #67c23a;
}

.trend-down {
  color: #f56c6c;
}
</style>
