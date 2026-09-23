<template>
  <el-dialog
    title="风险预警详情"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="warning-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="info-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
          <el-tag :type="getWarningTypeTagType(warningData.overallRiskLevel)">
            {{ warningData.warningType }}
          </el-tag>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>项目名称：</label>
              <span>{{ warningData.assessmentName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>风险评分：</label>
              <span :class="getRiskScoreClass(warningData.overallRiskScore)">
                {{ warningData.overallRiskScore?.toFixed(1) || '0.0' }}
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>风险等级：</label>
              <el-tag :type="getWarningTypeTagType(warningData.overallRiskLevel)">
                {{ getRiskLevelName(warningData.overallRiskLevel) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>预警时间：</label>
              <span>{{ formatDate(warningData.createTime) }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <label>预警信息：</label>
              <span>{{ warningData.warningMessage }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 风险详情 -->
      <el-card shadow="never" class="risk-details-card">
        <div slot="header" class="card-header">
          <span>风险详情分析</span>
          <el-button type="text" @click="refreshRiskDetails">
            <i class="el-icon-refresh"></i> 刷新
          </el-button>
        </div>
        
        <el-table
          :data="riskDetails"
          stripe
          border
          max-height="400"
        >
          <el-table-column
            prop="riskItem"
            label="风险项目"
            min-width="150"
            show-overflow-tooltip
          />
          <el-table-column
            prop="riskCategory"
            label="风险类别"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              {{ getRiskCategoryName(row.riskCategory) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="riskProbability"
            label="发生概率"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              {{ (row.riskProbability * 100).toFixed(1) }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="riskImpact"
            label="影响程度"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              {{ row.riskImpact?.toFixed(1) || '0.0' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="riskScore"
            label="风险评分"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <span :class="getRiskScoreClass(row.riskScore)">
                {{ row.riskScore?.toFixed(1) || '0.0' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="riskLevel"
            label="风险等级"
            width="100"
            align="center"
          >
            <template #default="{ row }">
              <el-tag :type="getWarningTypeTagType(row.riskLevel)" size="small">
                {{ getRiskLevelName(row.riskLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="riskDescription"
            label="风险描述"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="mitigationMeasures"
            label="缓解措施"
            min-width="200"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 风险统计图表 -->
      <el-card shadow="never" class="chart-card">
        <div slot="header" class="card-header">
          <span>风险分布统计</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div id="riskCategoryChart" style="height: 300px;"></div>
          </el-col>
          <el-col :span="12">
            <div id="riskLevelChart" style="height: 300px;"></div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 缓解建议 -->
      <el-card shadow="never" class="suggestions-card">
        <div slot="header" class="card-header">
          <span>风险缓解建议</span>
        </div>
        <div class="suggestions-content">
          <el-alert
            v-for="(suggestion, index) in mitigationSuggestions"
            :key="index"
            :title="suggestion"
            type="info"
            :closable="false"
            show-icon
            class="suggestion-item"
          />
          <div v-if="mitigationSuggestions.length === 0" class="no-suggestions">
            暂无缓解建议
          </div>
        </div>
      </el-card>

      <!-- 处理历史 -->
      <el-card shadow="never" class="history-card">
        <div slot="header" class="card-header">
          <span>处理历史</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in processHistory"
            :key="index"
            :timestamp="formatDate(item.processTime)"
            placement="top"
          >
            <el-card>
              <h4>{{ getProcessActionName(item.action) }}</h4>
              <p>处理人：{{ item.processorName }}</p>
              <p v-if="item.remarks">备注：{{ item.remarks }}</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            v-if="processHistory.length === 0"
            timestamp="暂无处理记录"
            placement="top"
          >
            <el-card>
              <p>该预警尚未被处理</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button
        v-if="warningData.status === 1"
        type="primary"
        @click="handleProcess"
      >
        立即处理
      </el-button>
      <el-button type="success" @click="generateReport">
        生成报告
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getRiskAssessmentDetail,
  getRiskMitigationSuggestions,
  generateRiskAssessmentReport
} from '@/api/contract/riskAssessment'

export default {
  name: 'RiskWarningDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      warningData: {},
      riskDetails: [],
      mitigationSuggestions: [],
      processHistory: []
    }
  },
  methods: {
    async showDetail(warningData) {
      this.warningData = warningData
      this.dialogVisible = true
      await this.fetchDetailData()
    },

    async fetchDetailData() {
      this.loading = true
      try {
        // 获取风险详情
        await this.fetchRiskDetails()
        // 获取缓解建议
        await this.fetchMitigationSuggestions()
        // 获取处理历史
        await this.fetchProcessHistory()
        // 渲染图表
        this.$nextTick(() => {
          this.renderCharts()
        })
      } catch (error) {
        this.$message.error('获取详情数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async fetchRiskDetails() {
      try {
        const response = await getRiskAssessmentDetail(this.warningData.id)
        if (response.code === 200) {
          this.riskDetails = response.data.riskDetails || []
        }
      } catch (error) {
        console.error('获取风险详情失败：', error)
      }
    },

    async fetchMitigationSuggestions() {
      try {
        // 根据高风险项目获取缓解建议
        const highRiskItems = this.riskDetails.filter(item => item.riskLevel >= 3)
        const suggestions = []
        
        for (const item of highRiskItems) {
          const response = await getRiskMitigationSuggestions(item.riskCategory, item.riskLevel)
          if (response.code === 200 && response.data) {
            suggestions.push(...response.data)
          }
        }
        
        this.mitigationSuggestions = [...new Set(suggestions)] // 去重
      } catch (error) {
        console.error('获取缓解建议失败：', error)
      }
    },

    async fetchProcessHistory() {
      // 模拟处理历史数据
      this.processHistory = [
        {
          action: 1,
          processorName: '张三',
          processTime: new Date(),
          remarks: '已确认风险，正在制定应对措施'
        }
      ]
    },

    refreshRiskDetails() {
      this.fetchRiskDetails()
    },

    renderCharts() {
      this.renderRiskCategoryChart()
      this.renderRiskLevelChart()
    },

    renderRiskCategoryChart() {
      // 使用 ECharts 渲染风险类别分布图
      const chartDom = document.getElementById('riskCategoryChart')
      if (!chartDom) return

      const myChart = this.$echarts.init(chartDom)
      
      // 统计风险类别分布
      const categoryStats = {}
      this.riskDetails.forEach(item => {
        const categoryName = this.getRiskCategoryName(item.riskCategory)
        categoryStats[categoryName] = (categoryStats[categoryName] || 0) + 1
      })

      const option = {
        title: {
          text: '风险类别分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '风险类别',
            type: 'pie',
            radius: '50%',
            data: Object.entries(categoryStats).map(([name, value]) => ({
              name,
              value
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      myChart.setOption(option)
    },

    renderRiskLevelChart() {
      // 使用 ECharts 渲染风险等级分布图
      const chartDom = document.getElementById('riskLevelChart')
      if (!chartDom) return

      const myChart = this.$echarts.init(chartDom)
      
      // 统计风险等级分布
      const levelStats = { 1: 0, 2: 0, 3: 0, 4: 0 }
      this.riskDetails.forEach(item => {
        levelStats[item.riskLevel] = (levelStats[item.riskLevel] || 0) + 1
      })

      const option = {
        title: {
          text: '风险等级分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['低风险', '中风险', '高风险', '极高风险']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            data: [
              { value: levelStats[1], itemStyle: { color: '#67c23a' } },
              { value: levelStats[2], itemStyle: { color: '#409eff' } },
              { value: levelStats[3], itemStyle: { color: '#e6a23c' } },
              { value: levelStats[4], itemStyle: { color: '#f56c6c' } }
            ]
          }
        ]
      }

      myChart.setOption(option)
    },

    handleProcess() {
      this.$emit('process', this.warningData)
      this.dialogVisible = false
    },

    async generateReport() {
      try {
        this.$message.info('正在生成风险评估报告...')
        const response = await generateRiskAssessmentReport(this.warningData.id)
        if (response.code === 200) {
          this.$message.success('报告生成成功')
        } else {
          this.$message.error(response.message || '报告生成失败')
        }
      } catch (error) {
        this.$message.error('报告生成失败：' + error.message)
      }
    },

    getWarningTypeTagType(riskLevel) {
      const typeMap = {
        1: 'info',
        2: 'warning',
        3: 'danger',
        4: 'danger'
      }
      return typeMap[riskLevel] || 'info'
    },

    getRiskScoreClass(score) {
      if (score >= 85) return 'risk-score-extreme'
      if (score >= 70) return 'risk-score-high'
      if (score >= 50) return 'risk-score-medium'
      return 'risk-score-low'
    },

    getRiskLevelName(level) {
      const levelMap = {
        1: '低风险',
        2: '中风险',
        3: '高风险',
        4: '极高风险'
      }
      return levelMap[level] || '未知'
    },

    getRiskCategoryName(category) {
      const categoryMap = {
        1: '相对方资格资信',
        2: '政策风险',
        3: '资金来源',
        4: '技术质量',
        5: '法律风险',
        6: '收款风险',
        7: '税务风险',
        8: '施工条件及环境'
      }
      return categoryMap[category] || '其他'
    },

    getProcessActionName(action) {
      const actionMap = {
        1: '开始处理',
        2: '处理中',
        3: '处理完成',
        4: '忽略预警'
      }
      return actionMap[action] || '未知操作'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.warning-detail {
  max-height: 70vh;
  overflow-y: auto;
}

.info-card,
.risk-details-card,
.chart-card,
.suggestions-card,
.history-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 15px;
}

.info-item label {
  font-weight: bold;
  color: #606266;
  margin-right: 10px;
}

.suggestions-content {
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  margin-bottom: 10px;
}

.no-suggestions {
  text-align: center;
  color: #909399;
  padding: 20px;
}

.risk-score-extreme {
  color: #f56c6c;
  font-weight: bold;
}

.risk-score-high {
  color: #e6a23c;
  font-weight: bold;
}

.risk-score-medium {
  color: #409eff;
}

.risk-score-low {
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
