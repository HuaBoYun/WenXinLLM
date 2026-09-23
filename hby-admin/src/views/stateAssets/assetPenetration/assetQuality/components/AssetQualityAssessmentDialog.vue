<template>
  <el-dialog
    title="资产质量评估"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <div class="assessment-content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基础信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="企业名称">{{ assessmentData.enterpriseName }}</el-descriptions-item>
            <el-descriptions-item label="资产类别">{{ getCategoryText(assessmentData.assetCategory) }}</el-descriptions-item>
            <el-descriptions-item label="资产价值">{{ formatNumber(assessmentData.assetValue) }}万元</el-descriptions-item>
            <el-descriptions-item label="质量等级">
              <el-tag :type="getLevelTag(assessmentData.qualityLevel)">
                {{ getLevelText(assessmentData.qualityLevel) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="质量评分">{{ assessmentData.qualityScore }}分</el-descriptions-item>
            <el-descriptions-item label="风险等级">
              <el-tag :type="getRiskTag(assessmentData.riskLevel)">
                {{ getRiskText(assessmentData.riskLevel) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="评估时间">{{ assessmentData.assessmentTime }}</el-descriptions-item>
            <el-descriptions-item label="评估状态">
              <el-tag :type="getStatusTag(assessmentData.assessmentStatus)">
                {{ getStatusText(assessmentData.assessmentStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        
        <el-tab-pane label="质量指标" name="indicators">
          <div class="indicators-panel">
            <el-table :data="qualityIndicators" border>
              <el-table-column label="指标名称" prop="name" width="180" />
              <el-table-column label="当前值" prop="currentValue" align="center" width="100" />
              <el-table-column label="标准值" prop="standardValue" align="center" width="100" />
              <el-table-column label="评估结果" prop="result" align="center" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getResultTag(scope.row.result)" size="mini">
                    {{ scope.row.result }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="权重" prop="weight" align="center" width="80" />
              <el-table-column label="得分" prop="score" align="center" width="80" />
              <el-table-column label="说明" prop="description" />
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="评估结果" name="result">
          <div class="result-panel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>综合评分</span>
                  </div>
                  <div class="score-display">
                    <div class="score-circle">
                      <el-progress
                        type="circle"
                        :percentage="assessmentResult.totalScore"
                        :color="getScoreColor(assessmentResult.totalScore)"
                        :width="120"
                      />
                    </div>
                    <div class="score-info">
                      <h3>{{ assessmentResult.totalScore }}分</h3>
                      <p>{{ getScoreLevel(assessmentResult.totalScore) }}</p>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>质量分布</span>
                  </div>
                  <div ref="qualityDistributionChart" class="chart-container"></div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-card shadow="never" class="mt-20">
              <div slot="header">
                <span>改进建议</span>
              </div>
              <div class="suggestions">
                <el-timeline>
                  <el-timeline-item
                    v-for="(suggestion, index) in assessmentResult.suggestions"
                    :key="index"
                    :color="getSuggestionColor(suggestion.priority)"
                  >
                    <h4>{{ suggestion.title }}</h4>
                    <p>{{ suggestion.content }}</p>
                    <el-tag size="mini" :type="getPriorityTag(suggestion.priority)">
                      {{ suggestion.priority }}
                    </el-tag>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getAssetQualityDetail, getAssetQualityIndicatorMonitoring } from '@/api/stateAssets/assetQuality'

export default {
  name: 'AssetQualityAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      qualityIndicators: [],
      assessmentResult: {
        totalScore: 0,
        suggestions: []
      },
      qualityDistributionChart: null
    }
  },
  computed: {
    assessmentData() {
      return this.assetData
    },
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
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    initData() {
      this.loadQualityIndicators()
      this.loadAssessmentResult()
    },

    async loadQualityIndicators() {
      try {
        const response = await getAssetQualityIndicatorMonitoring({ assetQualityId: this.assetData.assetQualityId })
        const data = response.data
        if (data && data.indicators) {
          this.qualityIndicators = data.indicators
        } else {
          // 基于当前资产数据构建指标
          const score = this.assetData.qualityScore || 0
          this.qualityIndicators = [
            { name: '资产完整性', currentValue: Math.min(score + 5, 100) + '%', standardValue: '90%', result: score >= 85 ? '优秀' : score >= 70 ? '良好' : '一般', weight: '20%', score: Math.min(score + 5, 100), description: '资产记录完整度评估' },
            { name: '资产准确性', currentValue: score + '%', standardValue: '85%', result: score >= 85 ? '优秀' : score >= 70 ? '良好' : '一般', weight: '25%', score: score, description: '资产信息准确度评估' },
            { name: '资产时效性', currentValue: Math.max(score - 3, 0) + '%', standardValue: '80%', result: score >= 83 ? '优秀' : score >= 70 ? '良好' : '一般', weight: '15%', score: Math.max(score - 3, 0), description: '资产信息更新及时性' },
            { name: '资产可用性', currentValue: Math.min(score + 2, 100) + '%', standardValue: '85%', result: score >= 83 ? '优秀' : score >= 70 ? '良好' : '一般', weight: '20%', score: Math.min(score + 2, 100), description: '资产可用性评估' },
            { name: '资产安全性', currentValue: Math.max(score - 8, 0) + '%', standardValue: '80%', result: score >= 88 ? '优秀' : score >= 72 ? '良好' : '一般', weight: '20%', score: Math.max(score - 8, 0), description: '资产安全性评估' }
          ]
        }
      } catch (error) {
        console.error('加载质量指标失败:', error)
        this.qualityIndicators = []
      }
    },

    async loadAssessmentResult() {
      try {
        const response = await getAssetQualityDetail({ indicatorId: this.assetData.assetQualityId })
        const data = response.data
        const score = data ? (data.comprehensiveScore || this.assetData.qualityScore || 0) : (this.assetData.qualityScore || 0)
        this.assessmentResult = {
          totalScore: score,
          suggestions: this.generateSuggestions(score)
        }
      } catch (error) {
        const score = this.assetData.qualityScore || 0
        this.assessmentResult = {
          totalScore: score,
          suggestions: this.generateSuggestions(score)
        }
      }
    },

    generateSuggestions(score) {
      const suggestions = []
      if (score < 60) {
        suggestions.push({ title: '紧急处置风险资产', content: '资产质量评分严重偏低，建议立即启动风险处置程序，评估减值准备。', priority: '高' })
      }
      if (score < 80) {
        suggestions.push({ title: '加强资产安全管理', content: '建立完善的资产安全管理制度，定期进行安全检查和风险评估。', priority: '高' })
        suggestions.push({ title: '优化资产信息系统', content: '升级资产管理信息系统，提高数据准确性和实时性。', priority: '中' })
      }
      suggestions.push({ title: '建立质量监控机制', content: '建立资产质量持续监控机制，及时发现和处理质量问题。', priority: '中' })
      suggestions.push({ title: '加强人员培训', content: '定期对资产管理人员进行培训，提高专业技能和管理水平。', priority: '低' })
      return suggestions
    },
    
    initCharts() {
      this.qualityDistributionChart = echarts.init(this.$refs.qualityDistributionChart)
      this.updateQualityDistributionChart()
      
      window.addEventListener('resize', this.handleResize)
    },
    
    updateQualityDistributionChart() {
      // 基于加载的指标数据构建图表
      const excellent = this.qualityIndicators.filter(i => i.score >= 90).length
      const good = this.qualityIndicators.filter(i => i.score >= 75 && i.score < 90).length
      const average = this.qualityIndicators.filter(i => i.score >= 60 && i.score < 75).length
      const poor = this.qualityIndicators.filter(i => i.score < 60).length
      const option = {
        title: {
          text: '质量指标分布',
          left: 'center',
          textStyle: { fontSize: 12 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
          {
            name: '质量指标',
            type: 'pie',
            radius: '60%',
            data: [
              { name: '优秀', value: excellent },
              { name: '良好', value: good },
              { name: '一般', value: average },
              { name: '较差', value: poor }
            ],
            itemStyle: {
              color: function(params) {
                const colors = ['#67C23A', '#409EFF', '#E6A23C', '#F56C6C']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      this.qualityDistributionChart.setOption(option)
    },
    
    handleExport() {
      this.$message.success('正在导出评估报告...')
      // 构建报告内容并触发下载
      const reportContent = `资产质量评估报告\n企业：${this.assessmentData.enterpriseName}\n评分：${this.assessmentResult.totalScore}分\n等级：${this.getScoreLevel(this.assessmentResult.totalScore)}`
      const blob = new Blob([reportContent], { type: 'text/plain;charset=utf-8' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `${this.assessmentData.enterpriseName || '资产'}_质量评估报告.txt`
      link.click()
      URL.revokeObjectURL(link.href)
    },
    
    handleClose() {
      this.$emit('update:visible', false)
      this.destroyCharts()
    },
    
    handleResize() {
      if (this.qualityDistributionChart) this.qualityDistributionChart.resize()
    },
    
    destroyCharts() {
      if (this.qualityDistributionChart) {
        this.qualityDistributionChart.dispose()
        this.qualityDistributionChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },
    
    formatNumber(num) {
      if (!num) return '0'
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    
    getCategoryText(category) {
      const categoryMap = {
        'CURRENT_ASSETS': '流动资产',
        'FIXED_ASSETS': '固定资产',
        'INTANGIBLE_ASSETS': '无形资产',
        'INVESTMENT_ASSETS': '投资性资产'
      }
      return categoryMap[category] || category
    },
    
    getLevelText(level) {
      const levelMap = {
        'EXCELLENT': '优质',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return levelMap[level] || level
    },
    
    getLevelTag(level) {
      const tagMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return tagMap[level] || 'info'
    },
    
    getRiskText(risk) {
      const riskMap = {
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险',
        'VERY_HIGH': '极高风险'
      }
      return riskMap[risk] || risk
    },
    
    getRiskTag(risk) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'VERY_HIGH': 'danger'
      }
      return tagMap[risk] || 'info'
    },
    
    getStatusTag(status) {
      const tagMap = {
        'COMPLETED': 'success',
        'IN_PROGRESS': 'warning',
        'PENDING': 'info'
      }
      return tagMap[status] || 'info'
    },
    
    getStatusText(status) {
      const textMap = {
        'COMPLETED': '已完成',
        'IN_PROGRESS': '进行中',
        'PENDING': '待评估'
      }
      return textMap[status] || status
    },
    
    getResultTag(result) {
      const tagMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return tagMap[result] || 'info'
    },
    
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    getScoreLevel(score) {
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '一般'
      return '较差'
    },
    
    getSuggestionColor(priority) {
      const colorMap = {
        '高': '#F56C6C',
        '中': '#E6A23C',
        '低': '#909399'
      }
      return colorMap[priority] || '#909399'
    },
    
    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return tagMap[priority] || 'info'
    }
  }
}
</script>

<style scoped>
.assessment-content {
  min-height: 500px;
}

.indicators-panel {
  padding: 20px 0;
}

.result-panel {
  padding: 20px 0;
}

.score-display {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.score-circle {
  margin-right: 30px;
}

.score-info h3 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.score-info p {
  margin: 5px 0 0 0;
  color: #606266;
}

.chart-container {
  height: 200px;
  width: 100%;
}

.suggestions {
  padding: 20px;
}

.suggestions h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.suggestions p {
  margin: 0 0 10px 0;
  color: #606266;
  line-height: 1.6;
}

.mt-20 {
  margin-top: 20px;
}
</style>
