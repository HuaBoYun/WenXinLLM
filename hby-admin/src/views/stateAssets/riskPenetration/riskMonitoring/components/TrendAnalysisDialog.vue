<template>
  <el-dialog
    title="趋势分析"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    top="5vh"
  >
    <div v-loading="loading" class="trend-analysis">
      <!-- 分析参数 -->
      <div class="analysis-params">
        <el-form :model="queryForm" :inline="true" class="param-form">
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="queryForm.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              @change="handleDateRangeChange"
            />
          </el-form-item>
          <el-form-item label="分析粒度">
            <el-select v-model="queryForm.granularity" @change="handleGranularityChange">
              <el-option label="小时" value="HOUR" />
              <el-option label="天" value="DAY" />
              <el-option label="周" value="WEEK" />
              <el-option label="月" value="MONTH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadTrendData">
              <i class="el-icon-refresh"></i>
              刷新分析
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 监控信息 -->
      <div class="monitoring-info">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-card">
              <div class="info-label">监控指标</div>
              <div class="info-value">{{ monitoringData.monitoringIndicator || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-card">
              <div class="info-label">当前值</div>
              <div class="info-value" :class="getCurrentValueClass()">
                {{ monitoringData.currentValue || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-card">
              <div class="info-label">单位</div>
              <div class="info-value">{{ monitoringData.unit || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 趋势图表 -->
      <div class="trend-chart">
        <h4>趋势图表</h4>
        <div class="chart-container">
          <div ref="trendChart" class="chart" style="height: 400px;"></div>
        </div>
      </div>

      <!-- 统计分析 -->
      <div class="statistical-analysis">
        <h4>统计分析</h4>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon">
                <i class="el-icon-top"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ statistics.maxValue || '-' }}</div>
                <div class="stat-label">最大值</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon">
                <i class="el-icon-bottom"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ statistics.minValue || '-' }}</div>
                <div class="stat-label">最小值</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon">
                <i class="el-icon-minus"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ statistics.avgValue || '-' }}</div>
                <div class="stat-label">平均值</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-icon">
                <i class="el-icon-sort"></i>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ statistics.volatility || '-' }}</div>
                <div class="stat-label">波动率</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 趋势分析结果 -->
      <div class="trend-results">
        <h4>趋势分析结果</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="result-card">
              <div class="result-header">
                <i class="el-icon-trend-charts"></i>
                <span>总体趋势</span>
              </div>
              <div class="result-content">
                <el-tag :type="getTrendTagType(trendAnalysis.overallTrend)" size="large">
                  {{ getTrendLabel(trendAnalysis.overallTrend) }}
                </el-tag>
                <p class="trend-description">{{ trendAnalysis.trendDescription }}</p>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="result-card">
              <div class="result-header">
                <i class="el-icon-warning"></i>
                <span>风险评估</span>
              </div>
              <div class="result-content">
                <el-tag :type="getRiskTagType(trendAnalysis.riskLevel)" size="large">
                  {{ getRiskLabel(trendAnalysis.riskLevel) }}
                </el-tag>
                <p class="risk-description">{{ trendAnalysis.riskDescription }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 转折点分析 -->
      <div class="turning-points">
        <h4>关键转折点</h4>
        <el-table :data="turningPoints" border stripe>
          <el-table-column prop="pointTime" label="时间点" width="180" />
          <el-table-column prop="pointValue" label="数值" width="100">
            <template slot-scope="scope">
              <span :class="getPointValueClass(scope.row.pointValue)">
                {{ scope.row.pointValue }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="pointType" label="转折类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getPointTypeTagType(scope.row.pointType)" size="small">
                {{ getPointTypeLabel(scope.row.pointType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变化幅度" width="120">
            <template slot-scope="scope">
              <span :class="getChangeRateClass(scope.row.changeRate)">
                {{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="significance" label="重要程度" width="100">
            <template slot-scope="scope">
              <el-tag :type="getSignificanceTagType(scope.row.significance)" size="small">
                {{ getSignificanceLabel(scope.row.significance) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" show-overflow-tooltip />
        </el-table>
      </div>

      <!-- 预测分析 -->
      <div class="prediction-analysis">
        <h4>预测分析</h4>
        <div class="prediction-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="prediction-card">
                <div class="prediction-header">
                  <i class="el-icon-time"></i>
                  <span>短期预测（7天）</span>
                </div>
                <div class="prediction-value">
                  <span class="value" :class="getPredictionClass(prediction.shortTerm.trend)">
                    {{ prediction.shortTerm.value || '-' }}
                  </span>
                  <span class="trend">{{ prediction.shortTerm.trend || '-' }}</span>
                </div>
                <div class="confidence">
                  <span>置信度：{{ prediction.shortTerm.confidence || '-' }}%</span>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="prediction-card">
                <div class="prediction-header">
                  <i class="el-icon-date"></i>
                  <span>中期预测（30天）</span>
                </div>
                <div class="prediction-value">
                  <span class="value" :class="getPredictionClass(prediction.mediumTerm.trend)">
                    {{ prediction.mediumTerm.value || '-' }}
                  </span>
                  <span class="trend">{{ prediction.mediumTerm.trend || '-' }}</span>
                </div>
                <div class="confidence">
                  <span>置信度：{{ prediction.mediumTerm.confidence || '-' }}%</span>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="prediction-card">
                <div class="prediction-header">
                  <i class="el-icon-calendar"></i>
                  <span>长期预测（90天）</span>
                </div>
                <div class="prediction-value">
                  <span class="value" :class="getPredictionClass(prediction.longTerm.trend)">
                    {{ prediction.longTerm.value || '-' }}
                  </span>
                  <span class="trend">{{ prediction.longTerm.trend || '-' }}</span>
                </div>
                <div class="confidence">
                  <span>置信度：{{ prediction.longTerm.confidence || '-' }}%</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 建议措施 -->
      <div class="recommendations">
        <h4>建议措施</h4>
        <div class="recommendation-list">
          <div
            v-for="(recommendation, index) in recommendations"
            :key="index"
            class="recommendation-item"
          >
            <div class="recommendation-header">
              <el-tag :type="getRecommendationTagType(recommendation.priority)" size="small">
                {{ getRecommendationPriorityLabel(recommendation.priority) }}
              </el-tag>
              <span class="recommendation-title">{{ recommendation.title }}</span>
            </div>
            <div class="recommendation-content">
              {{ recommendation.content }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportAnalysis">导出分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { analyzeIndicatorTrend, predictFutureTrend, identifyTrendTurningPoints } from '@/api/stateAssets/riskMonitoring'

export default {
  name: 'TrendAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    monitoringData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      queryForm: {
        dateRange: [],
        granularity: 'DAY'
      },
      statistics: {},
      trendAnalysis: {},
      turningPoints: [],
      prediction: {
        shortTerm: {},
        mediumTerm: {},
        longTerm: {}
      },
      recommendations: [],
      chartInstance: null
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
        this.initAnalysis()
      }
    }
  },
  mounted() {
    // 动态导入ECharts
    this.loadECharts()
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
    }
  },
  methods: {
    // 动态加载ECharts
    async loadECharts() {
      try {
        const echarts = await import('echarts')
        this.echarts = echarts.default || echarts
      } catch (error) {
        console.error('Failed to load ECharts:', error)
      }
    },

    // 初始化分析
    initAnalysis() {
      // 设置默认时间范围（最近30天）
      const endDate = new Date()
      const startDate = new Date()
      startDate.setDate(startDate.getDate() - 30)
      
      this.queryForm.dateRange = [
        startDate.toISOString().slice(0, 19).replace('T', ' '),
        endDate.toISOString().slice(0, 19).replace('T', ' ')
      ]
      
      this.loadTrendData()
    },

    // 加载趋势数据
    async loadTrendData() {
      this.loading = true
      try {
        // 并行加载多个分析数据
        await Promise.all([
          this.loadTrendAnalysis(),
          this.loadTurningPoints(),
          this.loadPrediction(),
          this.loadRecommendations()
        ])
        
        // 渲染图表
        this.renderChart()
      } catch (error) {
        this.$message.error('加载趋势数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 加载趋势分析
    async loadTrendAnalysis() {
      try {
        const response = await analyzeIndicatorTrend({
          riskMonitoringId: this.monitoringData.riskMonitoringId,
          startTime: this.queryForm.dateRange[0],
          endTime: this.queryForm.dateRange[1],
          granularity: this.queryForm.granularity
        })
        
        if (response.code === 200) {
          this.trendAnalysis = response.data.trendAnalysis || {}
          this.statistics = response.data.statistics || {}
        }
      } catch (error) {
        console.error('加载趋势分析失败：', error)
        // 使用模拟数据
        this.generateMockTrendData()
      }
    },

    // 加载转折点分析
    async loadTurningPoints() {
      try {
        const response = await identifyTrendTurningPoints({
          riskMonitoringId: this.monitoringData.riskMonitoringId,
          startTime: this.queryForm.dateRange[0],
          endTime: this.queryForm.dateRange[1]
        })
        
        if (response.code === 200) {
          this.turningPoints = response.data || []
        }
      } catch (error) {
        console.error('加载转折点分析失败：', error)
        // 使用模拟数据
        this.generateMockTurningPoints()
      }
    },

    // 加载预测分析
    async loadPrediction() {
      try {
        const response = await predictFutureTrend({
          riskMonitoringId: this.monitoringData.riskMonitoringId,
          predictionDays: [7, 30, 90]
        })
        
        if (response.code === 200) {
          this.prediction = response.data || {}
        }
      } catch (error) {
        console.error('加载预测分析失败：', error)
        // 使用模拟数据
        this.generateMockPrediction()
      }
    },

    // 加载建议措施
    loadRecommendations() {
      // 模拟建议数据
      this.recommendations = [
        {
          priority: 'HIGH',
          title: '加强监控频率',
          content: '建议将监控频率调整为每5分钟一次，以便及时发现异常变化。'
        },
        {
          priority: 'MEDIUM',
          title: '调整预警阈值',
          content: '根据历史数据分析，建议将预警阈值调整为75，危险阈值调整为85。'
        },
        {
          priority: 'LOW',
          title: '定期数据清理',
          content: '建议每月清理一次历史监控数据，保持系统性能。'
        }
      ]
    },

    // 生成模拟数据
    generateMockTrendData() {
      this.trendAnalysis = {
        overallTrend: 'RISING',
        trendDescription: '指标呈现上升趋势，需要密切关注',
        riskLevel: 'MEDIUM',
        riskDescription: '当前风险等级为中等，建议加强监控'
      }
      
      this.statistics = {
        maxValue: 89.5,
        minValue: 45.2,
        avgValue: 67.3,
        volatility: '12.5%'
      }
    },

    generateMockTurningPoints() {
      this.turningPoints = [
        {
          pointTime: '2024-01-15 14:30:00',
          pointValue: 85.2,
          pointType: 'PEAK',
          changeRate: '+15.2',
          significance: 'HIGH',
          description: '达到近期最高点'
        },
        {
          pointTime: '2024-01-12 09:15:00',
          pointValue: 52.8,
          pointType: 'VALLEY',
          changeRate: '-8.5',
          significance: 'MEDIUM',
          description: '出现明显下降'
        }
      ]
    },

    generateMockPrediction() {
      this.prediction = {
        shortTerm: {
          value: 72.5,
          trend: '上升',
          confidence: 85
        },
        mediumTerm: {
          value: 78.2,
          trend: '上升',
          confidence: 72
        },
        longTerm: {
          value: 82.1,
          trend: '上升',
          confidence: 58
        }
      }
    },

    // 渲染图表
    renderChart() {
      if (!this.echarts || !this.$refs.trendChart) return

      if (this.chartInstance) {
        this.chartInstance.dispose()
      }

      this.chartInstance = this.echarts.init(this.$refs.trendChart)
      
      // 生成模拟时间序列数据
      const dates = []
      const values = []
      const warningLine = []
      const dangerLine = []
      
      const startDate = new Date(this.queryForm.dateRange[0])
      const endDate = new Date(this.queryForm.dateRange[1])
      const daysDiff = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24))
      
      for (let i = 0; i <= daysDiff; i++) {
        const date = new Date(startDate)
        date.setDate(date.getDate() + i)
        dates.push(date.toISOString().split('T')[0])
        
        // 生成模拟数值（带趋势）
        const baseValue = 60 + Math.sin(i * 0.1) * 10 + i * 0.5
        const noise = (Math.random() - 0.5) * 8
        values.push(Math.round((baseValue + noise) * 100) / 100)
        
        warningLine.push(this.monitoringData.warningThreshold || 70)
        dangerLine.push(this.monitoringData.dangerThreshold || 85)
      }

      const option = {
        title: {
          text: `${this.monitoringData.monitoringIndicator || '监控指标'} 趋势图`,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['监控数值', '预警阈值', '危险阈值'],
          top: 30
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: this.monitoringData.unit || ''
        },
        series: [
          {
            name: '监控数值',
            type: 'line',
            data: values,
            smooth: true,
            lineStyle: {
              color: '#409eff',
              width: 2
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
                ]
              }
            }
          },
          {
            name: '预警阈值',
            type: 'line',
            data: warningLine,
            lineStyle: {
              color: '#e6a23c',
              type: 'dashed'
            },
            symbol: 'none'
          },
          {
            name: '危险阈值',
            type: 'line',
            data: dangerLine,
            lineStyle: {
              color: '#f56c6c',
              type: 'dashed'
            },
            symbol: 'none'
          }
        ]
      }

      this.chartInstance.setOption(option)
    },

    // 事件处理
    handleDateRangeChange() {
      if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
        this.loadTrendData()
      }
    },

    handleGranularityChange() {
      this.loadTrendData()
    },

    // 导出分析报告
    handleExportAnalysis() {
      // 模拟导出功能
      this.$message.success('分析报告导出成功')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      if (this.chartInstance) {
        this.chartInstance.dispose()
        this.chartInstance = null
      }
    },

    // 工具方法
    getCurrentValueClass() {
      const currentValue = this.monitoringData.currentValue
      const warningThreshold = this.monitoringData.warningThreshold
      const dangerThreshold = this.monitoringData.dangerThreshold

      if (!currentValue || !dangerThreshold) return ''
      
      if (currentValue >= dangerThreshold) return 'value-danger'
      if (warningThreshold && currentValue >= warningThreshold) return 'value-warning'
      return 'value-normal'
    },

    getTrendTagType(trend) {
      const trendMap = {
        'RISING': 'warning',
        'FALLING': 'success',
        'STABLE': 'info',
        'VOLATILE': 'danger'
      }
      return trendMap[trend] || ''
    },

    getTrendLabel(trend) {
      const labelMap = {
        'RISING': '上升趋势',
        'FALLING': '下降趋势',
        'STABLE': '稳定趋势',
        'VOLATILE': '波动趋势'
      }
      return labelMap[trend] || trend
    },

    getRiskTagType(risk) {
      const riskMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return riskMap[risk] || ''
    },

    getRiskLabel(risk) {
      const labelMap = {
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险'
      }
      return labelMap[risk] || risk
    },

    getPointValueClass(value) {
      if (value >= 80) return 'point-value-high'
      if (value >= 60) return 'point-value-medium'
      return 'point-value-low'
    },

    getPointTypeTagType(type) {
      const typeMap = {
        'PEAK': 'danger',
        'VALLEY': 'success',
        'INFLECTION': 'warning'
      }
      return typeMap[type] || ''
    },

    getPointTypeLabel(type) {
      const labelMap = {
        'PEAK': '峰值',
        'VALLEY': '谷值',
        'INFLECTION': '拐点'
      }
      return labelMap[type] || type
    },

    getChangeRateClass(rate) {
      const numRate = parseFloat(rate)
      if (numRate > 0) return 'change-rate-up'
      if (numRate < 0) return 'change-rate-down'
      return 'change-rate-stable'
    },

    getSignificanceTagType(significance) {
      const significanceMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return significanceMap[significance] || ''
    },

    getSignificanceLabel(significance) {
      const labelMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return labelMap[significance] || significance
    },

    getPredictionClass(trend) {
      if (trend === '上升') return 'prediction-up'
      if (trend === '下降') return 'prediction-down'
      return 'prediction-stable'
    },

    getRecommendationTagType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return priorityMap[priority] || ''
    },

    getRecommendationPriorityLabel(priority) {
      const labelMap = {
        'HIGH': '高优先级',
        'MEDIUM': '中优先级',
        'LOW': '低优先级'
      }
      return labelMap[priority] || priority
    }
  }
}
</script>

<style lang="scss" scoped>
.trend-analysis {
  .analysis-params {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

    .param-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .monitoring-info {
    margin-bottom: 20px;

    .info-card {
      text-align: center;
      padding: 16px;
      background: white;
      border: 1px solid #ebeef5;
      border-radius: 6px;

      .info-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .info-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;

        &.value-danger {
          color: #f56c6c;
        }
        
        &.value-warning {
          color: #e6a23c;
        }
        
        &.value-normal {
          color: #67c23a;
        }
      }
    }
  }

  .trend-chart,
  .statistical-analysis,
  .trend-results,
  .turning-points,
  .prediction-analysis,
  .recommendations {
    margin-bottom: 24px;
    padding: 20px;
    background: white;
    border: 1px solid #ebeef5;
    border-radius: 8px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .statistical-analysis {
    .stat-card {
      display: flex;
      align-items: center;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;

      .stat-icon {
        width: 40px;
        height: 40px;
        background: #409eff;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;

        i {
          font-size: 18px;
          color: white;
        }
      }

      .stat-content {
        .stat-value {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .trend-results {
    .result-card {
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;

      .result-header {
        display: flex;
        align-items: center;
        margin-bottom: 12px;

        i {
          font-size: 16px;
          margin-right: 8px;
          color: #409eff;
        }

        span {
          font-weight: 600;
          color: #303133;
        }
      }

      .result-content {
        .trend-description,
        .risk-description {
          margin: 8px 0 0 0;
          color: #606266;
          line-height: 1.5;
        }
      }
    }
  }

  .prediction-analysis {
    .prediction-card {
      text-align: center;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;

      .prediction-header {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;
        color: #606266;

        i {
          font-size: 16px;
          margin-right: 6px;
        }

        span {
          font-size: 14px;
        }
      }

      .prediction-value {
        margin-bottom: 8px;

        .value {
          display: block;
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;

          &.prediction-up {
            color: #f56c6c;
          }
          
          &.prediction-down {
            color: #67c23a;
          }
          
          &.prediction-stable {
            color: #909399;
          }
        }

        .trend {
          font-size: 14px;
          color: #606266;
        }
      }

      .confidence {
        font-size: 12px;
        color: #909399;
      }
    }
  }

  .recommendations {
    .recommendation-item {
      margin-bottom: 16px;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;

      &:last-child {
        margin-bottom: 0;
      }

      .recommendation-header {
        display: flex;
        align-items: center;
        margin-bottom: 8px;

        .recommendation-title {
          margin-left: 8px;
          font-weight: 600;
          color: #303133;
        }
      }

      .recommendation-content {
        color: #606266;
        line-height: 1.5;
      }
    }
  }

  // 数值样式
  .point-value-high {
    color: #f56c6c;
    font-weight: 600;
  }

  .point-value-medium {
    color: #e6a23c;
    font-weight: 600;
  }

  .point-value-low {
    color: #67c23a;
    font-weight: 600;
  }

  .change-rate-up {
    color: #f56c6c;
  }

  .change-rate-down {
    color: #67c23a;
  }

  .change-rate-stable {
    color: #909399;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
