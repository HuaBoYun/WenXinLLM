<template>
  <el-dialog
    title="预测详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <!-- 基本信息 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">企业名称：</span>
              <span class="value">{{ predictionData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">预测类型：</span>
              <el-tag :type="getPredictionTypeTag(predictionData.predictionType)">
                {{ getPredictionTypeText(predictionData.predictionType) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">风险等级：</span>
              <el-tag :type="getRiskLevelTag(predictionData.riskLevel)">
                {{ predictionData.riskLevel || '-' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-15">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">风险概率：</span>
              <span class="value">{{ predictionData.probability || 0 }}%</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">置信度：</span>
              <span class="value">{{ predictionData.confidence || 0 }}%</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">预测时间：</span>
              <span class="value">{{ predictionData.predictionTime || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 预测结果 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>预测结果</span>
        </div>
        <div class="prediction-result">
          <div class="result-summary">
            <h4>预测摘要</h4>
            <p>{{ predictionData.summary || '基于AI模型分析，该企业存在一定程度的风险，建议加强监控和管理。' }}</p>
          </div>
          
          <div class="result-details mt-20">
            <h4>详细分析</h4>
            <el-table :data="analysisDetails" stripe border style="width: 100%">
              <el-table-column prop="indicator" label="分析指标" width="150"></el-table-column>
              <el-table-column prop="currentValue" label="当前值" width="120" align="center"></el-table-column>
              <el-table-column prop="predictedValue" label="预测值" width="120" align="center"></el-table-column>
              <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
                    {{ scope.row.riskLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="说明" min-width="200"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-card>

      <!-- 影响因素 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>关键影响因素</span>
        </div>
        <div class="influence-factors">
          <el-row :gutter="20">
            <el-col :span="12" v-for="(factor, index) in influenceFactors" :key="index">
              <div class="factor-item">
                <div class="factor-header">
                  <span class="factor-name">{{ factor.name }}</span>
                  <span class="factor-weight">权重: {{ factor.weight }}%</span>
                </div>
                <div class="factor-impact">
                  <el-progress
                    :percentage="factor.impact"
                    :color="getImpactColor(factor.impact)"
                    :show-text="false"
                  ></el-progress>
                  <span class="impact-value">{{ factor.impact }}%</span>
                </div>
                <div class="factor-description">{{ factor.description }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 建议措施 -->
      <el-card>
        <div slot="header" class="card-header">
          <span>建议措施</span>
        </div>
        <div class="recommendations">
          <el-timeline>
            <el-timeline-item
              v-for="(recommendation, index) in recommendations"
              :key="index"
              :type="getRecommendationPriority(recommendation.priority)"
            >
              <div class="recommendation-item">
                <h5>{{ recommendation.title }}</h5>
                <p>{{ recommendation.description }}</p>
                <div class="recommendation-meta">
                  <el-tag size="mini" :type="getPriorityTag(recommendation.priority)">
                    {{ recommendation.priority }}优先级
                  </el-tag>
                  <span class="expected-effect">预期效果: {{ recommendation.expectedEffect }}</span>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PredictionDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    predictionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      analysisDetails: [
        {
          indicator: '资产负债率',
          currentValue: '65%',
          predictedValue: '72%',
          riskLevel: '中',
          description: '负债水平可能上升，需关注偿债能力'
        },
        {
          indicator: '流动比率',
          currentValue: '1.2',
          predictedValue: '1.0',
          riskLevel: '高',
          description: '流动性可能恶化，存在短期偿债风险'
        },
        {
          indicator: '净利润率',
          currentValue: '8%',
          predictedValue: '5%',
          riskLevel: '中',
          description: '盈利能力可能下降，需优化经营策略'
        }
      ],
      influenceFactors: [
        {
          name: '市场环境',
          weight: 25,
          impact: 75,
          description: '行业竞争加剧，市场需求波动较大'
        },
        {
          name: '政策变化',
          weight: 20,
          impact: 60,
          description: '监管政策趋严，合规成本上升'
        },
        {
          name: '财务状况',
          weight: 30,
          impact: 80,
          description: '现金流紧张，债务压力增大'
        },
        {
          name: '经营能力',
          weight: 25,
          impact: 65,
          description: '管理效率有待提升，成本控制需加强'
        }
      ],
      recommendations: [
        {
          title: '加强现金流管理',
          description: '优化应收账款管理，加快资金回笼，确保充足的流动性',
          priority: '高',
          expectedEffect: '改善流动性指标15-20%'
        },
        {
          title: '优化债务结构',
          description: '合理安排债务期限结构，降低短期偿债压力',
          priority: '中',
          expectedEffect: '降低财务风险10-15%'
        },
        {
          title: '提升运营效率',
          description: '通过数字化转型和流程优化，提高经营效率',
          priority: '中',
          expectedEffect: '提升盈利能力5-10%'
        }
      ]
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
  methods: {
    handleClose() {
      this.dialogVisible = false
    },

    handleExport() {
      this.$message.success('正在导出预测报告...')
    },

    getPredictionTypeTag(type) {
      const tagMap = {
        'FINANCIAL_RISK': 'primary',
        'OPERATIONAL_RISK': 'warning',
        'MARKET_RISK': 'success',
        'CREDIT_RISK': 'danger'
      }
      return tagMap[type] || 'info'
    },

    getPredictionTypeText(type) {
      const textMap = {
        'FINANCIAL_RISK': '财务风险',
        'OPERATIONAL_RISK': '经营风险',
        'MARKET_RISK': '市场风险',
        'CREDIT_RISK': '信用风险'
      }
      return textMap[type] || type
    },

    getRiskLevelTag(level) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[level] || 'info'
    },

    getImpactColor(impact) {
      if (impact >= 80) return '#f56c6c'
      if (impact >= 60) return '#e6a23c'
      return '#67c23a'
    },

    getRecommendationPriority(priority) {
      const priorityMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return priorityMap[priority] || 'primary'
    },

    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[priority] || 'info'
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.mt-15 {
  margin-top: 15px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 10px;
}

.info-item .label {
  font-weight: bold;
  color: #606266;
}

.info-item .value {
  color: #303133;
}

.prediction-result .result-summary h4,
.prediction-result .result-details h4 {
  margin-bottom: 10px;
  color: #303133;
}

.factor-item {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
}

.factor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.factor-name {
  font-weight: bold;
  color: #303133;
}

.factor-weight {
  font-size: 12px;
  color: #909399;
}

.factor-impact {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.factor-impact .el-progress {
  flex: 1;
  margin-right: 10px;
}

.impact-value {
  font-weight: bold;
  color: #303133;
}

.factor-description {
  font-size: 12px;
  color: #606266;
}

.recommendation-item h5 {
  margin-bottom: 8px;
  color: #303133;
}

.recommendation-item p {
  margin-bottom: 10px;
  color: #606266;
}

.recommendation-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.expected-effect {
  font-size: 12px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}
</style>
