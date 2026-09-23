<template>
  <div class="payable-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-pie-chart"></i>
          应付分析
        </h1>
        <p class="page-description">分析应付账款的结构、趋势和供应商排名，提供现金流预测</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-download" @click="exportAnalysis">
          导出分析
        </el-button>
        <el-button type="success" icon="el-icon-data-line" @click="generateReport">
          生成报表
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 分析概览 -->
    <div class="analysis-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon payable">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.totalPayable) }}</div>
              <div class="card-label">应付总额</div>
              <div class="card-trend">
                <i :class="overview.payableTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.payableTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon aging">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.avgAgingDays }}</div>
              <div class="card-label">平均账龄(天)</div>
              <div class="card-trend">
                <i :class="overview.agingTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.agingTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon efficiency">
              <i class="el-icon-check"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.paymentEfficiency }}%</div>
              <div class="card-label">付款效率</div>
              <div class="card-trend">
                <i :class="overview.efficiencyTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.efficiencyTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon suppliers">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.activeSuppliers }}</div>
              <div class="card-label">活跃供应商</div>
              <div class="card-trend">
                <i :class="overview.supplierTrend >= 0 ? 'el-icon-top trend-up' : 'el-icon-bottom trend-down'"></i>
                <span>{{ Math.abs(overview.supplierTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析选项 -->
    <div class="analysis-options">
      <el-form :model="analysisForm" :inline="true" size="small">
        <el-form-item label="分析维度">
          <el-select v-model="analysisForm.dimension" placeholder="请选择分析维度" @change="onDimensionChange">
            <el-option label="供应商维度" :value="1" />
            <el-option label="时间维度" :value="2" />
            <el-option label="业务类型维度" :value="3" />
            <el-option label="账龄维度" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析期间">
          <el-date-picker
            v-model="analysisForm.dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
            @change="onDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="对比类型">
          <el-select v-model="analysisForm.comparisonType" placeholder="请选择对比类型" @change="onComparisonChange">
            <el-option label="同比" :value="1" />
            <el-option label="环比" :value="2" />
            <el-option label="预算对比" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="performAnalysis">分析</el-button>
          <el-button icon="el-icon-refresh" @click="resetAnalysis">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 分析结果 -->
    <div class="analysis-results">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 结构分析 -->
        <el-tab-pane label="结构分析" name="structure">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>应付结构分析</h3>
              <p>按不同维度分析应付账款构成和占比</p>
            </div>
            <div class="chart-container">
              <div ref="structurePieChart" class="pie-chart"></div>
            </div>
            <div class="structure-table">
              <el-table :data="structureData" border>
                <el-table-column prop="category" label="类别" width="150" />
                <el-table-column prop="amount" label="金额" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100">
                  <template slot-scope="scope">
                    <span>{{ scope.row.percentage }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="growth" label="增长率" width="100">
                  <template slot-scope="scope">
                    <span :class="scope.row.growth >= 0 ? 'growth-positive' : 'growth-negative'">
                      {{ scope.row.growth >= 0 ? '+' : '' }}{{ scope.row.growth }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="rank" label="排名" width="80" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 供应商排名 -->
        <el-tab-pane label="供应商排名" name="ranking">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>供应商应付排名</h3>
              <p>按应付金额对供应商进行排名分析</p>
            </div>
            <div class="ranking-summary">
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">TOP10占比</div>
                    <div class="summary-value">{{ rankingSummary.top10Percentage }}%</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">最大供应商</div>
                    <div class="summary-value">{{ rankingSummary.topSupplier }}</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-item">
                    <div class="summary-label">集中度指数</div>
                    <div class="summary-value">{{ rankingSummary.concentrationIndex }}</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="ranking-table">
              <el-table :data="rankingData" border>
                <el-table-column prop="rank" label="排名" width="80" />
                <el-table-column prop="supplierName" label="供应商名称" width="200" />
                <el-table-column prop="payableAmount" label="应付金额" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.payableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100">
                  <template slot-scope="scope">
                    <span>{{ scope.row.percentage }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="avgPaymentDays" label="平均付款天数" width="120" />
                <el-table-column prop="creditLevel" label="信用等级" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getCreditLevelType(scope.row.creditLevel)">
                      {{ scope.row.creditLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                      {{ scope.row.riskLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 账龄分析 -->
        <el-tab-pane label="账龄分析" name="aging">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>应付账龄分析</h3>
              <p>分析应付账款的账龄分布和逾期情况</p>
            </div>
            <div class="aging-summary">
              <el-row :gutter="24">
                <el-col :span="6">
                  <div class="aging-item">
                    <div class="aging-label">30天内</div>
                    <div class="aging-value">{{ formatAmount(agingSummary.within30Days) }}</div>
                    <div class="aging-percentage">{{ agingSummary.within30DaysPercentage }}%</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="aging-item">
                    <div class="aging-label">31-60天</div>
                    <div class="aging-value">{{ formatAmount(agingSummary.days31To60) }}</div>
                    <div class="aging-percentage">{{ agingSummary.days31To60Percentage }}%</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="aging-item">
                    <div class="aging-label">61-90天</div>
                    <div class="aging-value">{{ formatAmount(agingSummary.days61To90) }}</div>
                    <div class="aging-percentage">{{ agingSummary.days61To90Percentage }}%</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="aging-item overdue">
                    <div class="aging-label">90天以上</div>
                    <div class="aging-value">{{ formatAmount(agingSummary.over90Days) }}</div>
                    <div class="aging-percentage">{{ agingSummary.over90DaysPercentage }}%</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <aging-distribution-chart ref="agingChart" class="aging-chart-wrapper" />
          </div>
        </el-tab-pane>

        <!-- 现金流预测 -->
        <el-tab-pane label="现金流预测" name="cashflow">
          <div class="analysis-panel">
            <div class="panel-header">
              <h3>现金流预测</h3>
              <p>基于应付账款预测未来现金流出情况</p>
            </div>
            <div class="forecast-options">
              <el-form :inline="true" size="small">
                <el-form-item label="预测期间">
                  <el-select v-model="forecastPeriod" placeholder="请选择预测期间">
                    <el-option label="未来3个月" :value="3" />
                    <el-option label="未来6个月" :value="6" />
                    <el-option label="未来12个月" :value="12" />
                  </el-select>
                </el-form-item>
                <el-form-item label="预测模型">
                  <el-select v-model="forecastModel" placeholder="请选择预测模型">
                    <el-option label="历史平均" :value="1" />
                    <el-option label="趋势分析" :value="2" />
                    <el-option label="季节性调整" :value="3" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="runForecast">运行预测</el-button>
                </el-form-item>
              </el-form>
            </div>
            <cash-flow-forecast-chart
              ref="cashflowChart"
              :forecast-period="forecastPeriod"
              :forecast-model="forecastModel"
            />
            <div class="forecast-results">
              <el-table :data="forecastData" border>
                <el-table-column prop="period" label="预测期间" width="120" />
                <el-table-column prop="expectedPayment" label="预期付款" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.expectedPayment) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="confidence" label="置信度" width="100">
                  <template slot-scope="scope">
                    <span>{{ scope.row.confidence }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="upperBound" label="上限" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.upperBound) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="lowerBound" label="下限" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.lowerBound) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                      {{ scope.row.riskLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getPayableTrendAnalysis,
  getSupplierPayableRanking,
  getPaymentEfficiencyAnalysis,
  getPayableAgingAnalysis
} from '@/api/financialSharing/payables'
import AgingDistributionChart from './components/AgingDistributionChart.vue'
import CashFlowForecastChart from './components/CashFlowForecastChart.vue'

export default {
  name: 'PayableAnalysisIndex',
  components: {
    AgingDistributionChart,
    CashFlowForecastChart
  },
  data() {
    return {
      overview: {
        totalPayable: 0,
        payableTrend: 0,
        avgAgingDays: 0,
        agingTrend: 0,
        paymentEfficiency: 0,
        efficiencyTrend: 0,
        activeSuppliers: 0,
        supplierTrend: 0
      },
      analysisForm: {
        dimension: 1,
        dateRange: [],
        comparisonType: 1
      },
      activeTab: 'structure',
      structureData: [],
      rankingData: [],
      rankingSummary: {
        top10Percentage: 0,
        topSupplier: '',
        concentrationIndex: 0
      },
      agingSummary: {
        within30Days: 0,
        within30DaysPercentage: 0,
        days31To60: 0,
        days31To60Percentage: 0,
        days61To90: 0,
        days61To90Percentage: 0,
        over90Days: 0,
        over90DaysPercentage: 0
      },
      forecastPeriod: 6,
      forecastModel: 2,
      forecastData: [],
      structurePieChart: null
    }
  },
  mounted() {
    this.loadAnalysisData()
  },
  beforeDestroy() {
    if (this.structurePieChart) {
      this.structurePieChart.dispose()
      this.structurePieChart = null
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    async loadAnalysisData() {
      await this.loadStructureData()
      await this.loadRankingData()
      await this.loadForecastData()
    },
    async loadStructureData() {
      // 数据加载失败时的空状态降级（不再使用模拟数据）
      // try {
      //   const response = await getPayableTrendAnalysis({
      //     dimension: this.analysisForm.dimension,
      //     dateRange: this.analysisForm.dateRange
      //   })
      //   if (response.code === 1) {
      //     this.structureData = response.data.structureData || []
      //     this.$nextTick(() => {
      //       this.initStructurePieChart()
      //     })
      //   }
      // } catch (error) {
      //   console.error('获取结构分析数据失败:', error)

        // 暂未对接 API，先以空状态展示
        this.structureData = []
        this.$nextTick(() => {
          this.initStructurePieChart()
        })
      // }
    },
    initStructurePieChart() {
      if (!this.$refs.structurePieChart) {
        console.warn('structurePieChart ref not found')
        return
      }

      // 如果已存在实例,先销毁
      if (this.structurePieChart) {
        this.structurePieChart.dispose()
      }

      // 初始化ECharts实例
      this.structurePieChart = echarts.init(this.$refs.structurePieChart)

      // 转换数据格式为ECharts饼图所需格式
      const pieData = this.transformDataForPieChart(this.structureData)

      console.log('初始化结构分析饼图,数据:', pieData)

      // 配置项
      const option = {
        title: {
          text: '应付结构分析',
          left: 'center',
          top: 10,
          textStyle: {
            fontSize: 16,
            fontWeight: 600,
            color: '#303133'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            return `
              <div style="padding: 8px;">
                <div style="font-weight: bold; margin-bottom: 4px;">${params.name}</div>
                <div>金额: ${(params.value / 10000).toFixed(2)}万元</div>
                <div>占比: ${params.percent}%</div>
                ${params.data.growth !== undefined ? `<div>增长率: ${params.data.growth >= 0 ? '+' : ''}${params.data.growth}%</div>` : ''}
              </div>
            `
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          textStyle: {
            fontSize: 12,
            color: '#606266'
          }
        },
        series: [
          {
            name: '应付结构',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              position: 'outside',
              formatter: '{b}\n{d}%',
              fontSize: 13,
              fontWeight: 500,
              color: '#303133'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold',
                color: '#303133'
              },
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            labelLine: {
              show: true,
              length: 15,
              length2: 10
            },
            data: pieData
          }
        ],
        color: [
          '#409EFF', // Element-UI Primary Blue
          '#67C23A', // Success Green
          '#E6A23C', // Warning Orange
          '#F56C6C', // Danger Red
          '#909399', // Info Gray
          '#5DADE2',
          '#48C9B0',
          '#AF7AC5',
          '#F5B041',
          '#EC7063'
        ]
      }

      // 设置配置项
      this.structurePieChart.setOption(option)

      // 绑定点击事件
      this.structurePieChart.on('click', (params) => {
        this.handlePieChartClick(params)
      })

      // 添加窗口resize监听
      window.addEventListener('resize', this.handleResize)
    },
    transformDataForPieChart(structureData) {
      return structureData.map(item => ({
        name: item.category,
        value: item.amount,
        percentage: item.percentage,
        growth: item.growth,
        rank: item.rank
      }))
    },
    handlePieChartClick(params) {
      // 点击饼图扇区时高亮对应表格行
      console.log('点击了:', params.name, params.data)
      // 可以在这里添加高亮表格行的逻辑
      this.$message.success(`您选择了: ${params.name}`)
    },
    handleResize() {
      if (this.structurePieChart) {
        this.structurePieChart.resize()
      }
    },
    async loadRankingData() {
      // 数据加载失败时的空状态降级（不再使用模拟数据）
      // try {
      //   const response = await getSupplierPayableRanking()
      //   if (response.code === 1) {
      //     this.rankingData = response.data.rankingData || []
      //   }
      // } catch (error) {
      //   console.error('获取供应商排名数据失败:', error)

        // 暂未对接 API，先以空状态展示
        this.rankingData = []
      // }
    },
    async loadForecastData() {
      // 暂未对接 API，先以空状态展示
      this.forecastData = []
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getCreditLevelType(level) {
      const types = { 'AAA': 'success', 'AA': 'success', 'A': 'primary', 'BBB': 'warning', 'BB': 'warning', 'B': 'danger' }
      return types[level] || 'info'
    },
    getRiskLevelType(level) {
      const types = { '低': 'success', '中': 'warning', '高': 'danger' }
      return types[level] || 'info'
    },
    onDimensionChange() {
      // 切换维度时重新加载结构数据和饼图
      this.loadStructureData()
    },
    onDateRangeChange() {
      this.loadAnalysisData()
    },
    onComparisonChange() {
      this.loadAnalysisData()
    },
    performAnalysis() {
      this.loadAnalysisData()
    },
    resetAnalysis() {
      this.analysisForm = {
        dimension: 1,
        dateRange: [],
        comparisonType: 1
      }
      this.loadAnalysisData()
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'structure') {
        this.loadStructureData()
      } else if (tab.name === 'ranking') {
        this.loadRankingData()
      } else if (tab.name === 'cashflow') {
        this.loadForecastData()
      }
    },
    runForecast() {
      this.loadForecastData()
      if (this.$refs.cashflowChart) {
        this.$refs.cashflowChart.refresh()
      }
      this.$message.success('预测运行完成')
    },
    exportAnalysis() {
      try {
        const data = [...(this.structureData || []), ...(this.rankingData || []), ...(this.forecastData || [])]
        if (data.length === 0) { this.$message.warning('暂无数据可导出'); return }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '应付分析数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },
    generateReport() {
      this.$confirm('确认基于当前分析数据生成报表？', '生成报表', { type: 'info' }).then(() => {
        this.$message.success('报表生成成功')
        this.loadAnalysisData()
      }).catch(() => {})
    },
    refreshData() {
      this.loadAnalysisData()
      this.$message.success('数据刷新完成')
    }
  }
}
</script>

<style lang="scss" scoped>
.payable-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
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
      margin-left: 12px;
    }
  }
}

.analysis-overview {
  margin-bottom: 24px;

  .overview-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.payable {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.aging {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.efficiency {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.suppliers {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .card-content {
      flex: 1;

      .card-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .card-trend {
        display: flex;
        align-items: center;
        font-size: 12px;

        .trend-up {
          color: #67c23a;
        }

        .trend-down {
          color: #f56c6c;
        }

        span {
          margin-left: 4px;
        }
      }
    }
  }
}

.analysis-options {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.analysis-results {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .analysis-panel {
    .panel-header {
      margin-bottom: 24px;

      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }

      p {
        color: #606266;
        font-size: 14px;
        margin: 0;
      }
    }

    .chart-container {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 24px;
      margin-bottom: 24px;

      .pie-chart {
        width: 100%;
        height: 400px;
      }

      .chart-placeholder {
        i {
          font-size: 48px;
          color: #c0c4cc;
          margin-bottom: 16px;
        }

        p {
          font-size: 16px;
          color: #606266;
          margin: 0 0 8px 0;
        }

        .chart-desc {
          font-size: 14px;
          color: #909399;
        }
      }
    }

    .structure-table,
    .ranking-table,
    .forecast-results {
      .amount-text {
        color: #e6a23c;
        font-weight: 600;
      }

      .growth-positive {
        color: #67c23a;
      }

      .growth-negative {
        color: #f56c6c;
      }
    }

    .ranking-summary {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 24px;

      .summary-item {
        text-align: center;

        .summary-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }

        .summary-value {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
        }
      }
    }

    .aging-summary {
      margin-bottom: 24px;

      .aging-item {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 20px;
        text-align: center;
        transition: all 0.3s ease;

        &:hover {
          background: #e6f7ff;
        }

        &.overdue {
          background: #fff2f0;

          &:hover {
            background: #ffebe6;
          }
        }

        .aging-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 8px;
        }

        .aging-value {
          font-size: 18px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .aging-percentage {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .aging-chart-wrapper {
      margin-bottom: 24px;
    }

    .forecast-options {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 24px;
    }
  }
}
</style>
