<template>
  <el-dialog
    title="推荐详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <!-- 推荐基本信息 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>推荐信息</span>
          <el-tag :type="getRecommendationTypeTag(recommendationData.type)">
            {{ getRecommendationTypeText(recommendationData.type) }}
          </el-tag>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="label">推荐标题：</span>
              <span class="value">{{ recommendationData.title || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="label">优先级：</span>
              <el-tag :type="getPriorityTag(recommendationData.priority)">
                {{ recommendationData.priority || '-' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-15">
          <el-col :span="12">
            <div class="info-item">
              <span class="label">置信度：</span>
              <span class="value">{{ recommendationData.confidence || 0 }}%</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="label">适用范围：</span>
              <span class="value">{{ recommendationData.applicableScope || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-15">
          <el-col :span="24">
            <div class="info-item">
              <span class="label">生成时间：</span>
              <span class="value">{{ recommendationData.generateTime || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 推荐内容 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>推荐内容</span>
        </div>
        <div class="recommendation-content">
          <div class="content-section">
            <h4>问题描述</h4>
            <p>{{ recommendationData.problemDescription || '基于当前数据分析，发现以下问题需要关注和改进。' }}</p>
          </div>
          
          <div class="content-section mt-20">
            <h4>推荐方案</h4>
            <div class="solution-list">
              <div v-for="(solution, index) in solutions" :key="index" class="solution-item">
                <div class="solution-header">
                  <span class="solution-title">{{ solution.title }}</span>
                  <el-tag size="mini" :type="getDifficultyTag(solution.difficulty)">
                    {{ solution.difficulty }}
                  </el-tag>
                </div>
                <div class="solution-description">{{ solution.description }}</div>
                <div class="solution-meta">
                  <span class="meta-item">预期效果: {{ solution.expectedEffect }}</span>
                  <span class="meta-item">实施周期: {{ solution.implementationPeriod }}</span>
                  <span class="meta-item">资源需求: {{ solution.resourceRequirement }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 实施计划 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>实施计划</span>
        </div>
        <div class="implementation-plan">
          <el-timeline>
            <el-timeline-item
              v-for="(step, index) in implementationSteps"
              :key="index"
              :type="getStepType(step.status)"
            >
              <div class="step-item">
                <h5>{{ step.title }}</h5>
                <p>{{ step.description }}</p>
                <div class="step-meta">
                  <el-tag size="mini" :type="getStatusTag(step.status)">
                    {{ step.status }}
                  </el-tag>
                  <span class="step-duration">预计用时: {{ step.duration }}</span>
                  <span class="step-responsible">负责人: {{ step.responsible }}</span>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-card>

      <!-- 风险评估 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>风险评估</span>
        </div>
        <div class="risk-assessment">
          <el-table :data="riskFactors" stripe border style="width: 100%">
            <el-table-column prop="riskType" label="风险类型" width="120"></el-table-column>
            <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
                  {{ scope.row.riskLevel }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="probability" label="发生概率" width="100" align="center">
              <template slot-scope="scope">
                {{ scope.row.probability }}%
              </template>
            </el-table-column>
            <el-table-column prop="impact" label="影响程度" width="100" align="center">
              <template slot-scope="scope">
                {{ scope.row.impact }}
              </template>
            </el-table-column>
            <el-table-column prop="mitigation" label="缓解措施" min-width="200"></el-table-column>
          </el-table>
        </div>
      </el-card>

      <!-- 效果预期 -->
      <el-card>
        <div slot="header" class="card-header">
          <span>效果预期</span>
        </div>
        <div class="expected-results">
          <el-row :gutter="20">
            <el-col :span="8" v-for="(result, index) in expectedResults" :key="index">
              <div class="result-item">
                <div class="result-icon">
                  <i :class="result.icon" :style="{ color: result.color }"></i>
                </div>
                <div class="result-content">
                  <div class="result-title">{{ result.title }}</div>
                  <div class="result-value">{{ result.value }}</div>
                  <div class="result-description">{{ result.description }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="success" @click="handleApply">采纳推荐</el-button>
      <el-button type="primary" @click="handleFeedback">提供反馈</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'RecommendationDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    recommendationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      solutions: [
        {
          title: '优化资金配置策略',
          difficulty: '中等',
          description: '通过重新配置资金结构，提高资金使用效率，降低财务成本',
          expectedEffect: '降低财务成本15-20%',
          implementationPeriod: '3个月',
          resourceRequirement: '财务团队、外部咨询'
        },
        {
          title: '建立风险预警机制',
          difficulty: '较难',
          description: '构建实时风险监控系统，及时发现和处理潜在风险',
          expectedEffect: '提升风险识别能力30%',
          implementationPeriod: '6个月',
          resourceRequirement: 'IT团队、风控专家'
        }
      ],
      implementationSteps: [
        {
          title: '需求分析与方案设计',
          description: '深入分析当前问题，制定详细的实施方案',
          status: '待开始',
          duration: '2周',
          responsible: '项目经理'
        },
        {
          title: '资源准备与团队组建',
          description: '准备必要的资源，组建实施团队',
          status: '待开始',
          duration: '1周',
          responsible: '人力资源部'
        },
        {
          title: '方案实施与监控',
          description: '按照计划执行方案，实时监控进展',
          status: '待开始',
          duration: '8周',
          responsible: '实施团队'
        },
        {
          title: '效果评估与优化',
          description: '评估实施效果，持续优化改进',
          status: '待开始',
          duration: '2周',
          responsible: '质量管理部'
        }
      ],
      riskFactors: [
        {
          riskType: '实施风险',
          riskLevel: '中',
          probability: 30,
          impact: '中等',
          mitigation: '制定详细的实施计划，加强项目管理'
        },
        {
          riskType: '技术风险',
          riskLevel: '低',
          probability: 15,
          impact: '较小',
          mitigation: '选择成熟的技术方案，做好技术储备'
        },
        {
          riskType: '人员风险',
          riskLevel: '中',
          probability: 25,
          impact: '中等',
          mitigation: '提前进行人员培训，建立激励机制'
        }
      ],
      expectedResults: [
        {
          title: '效率提升',
          value: '+25%',
          description: '预期提升整体运营效率',
          icon: 'el-icon-trend-charts',
          color: '#67c23a'
        },
        {
          title: '成本降低',
          value: '-18%',
          description: '预期降低运营成本',
          icon: 'el-icon-money',
          color: '#e6a23c'
        },
        {
          title: '风险控制',
          value: '+40%',
          description: '预期提升风险控制能力',
          icon: 'el-icon-shield',
          color: '#409eff'
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

    handleApply() {
      this.$message.success('推荐方案已采纳')
      this.handleClose()
    },

    handleFeedback() {
      this.$message.success('反馈已提交')
    },

    getRecommendationTypeTag(type) {
      const tagMap = {
        'SUPERVISION_MEASURES': 'primary',
        'RISK_DISPOSAL': 'danger',
        'INVESTMENT_DECISION': 'success',
        'COMPLIANCE_ADVICE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getRecommendationTypeText(type) {
      const textMap = {
        'SUPERVISION_MEASURES': '监管措施推荐',
        'RISK_DISPOSAL': '风险处置推荐',
        'INVESTMENT_DECISION': '投资决策推荐',
        'COMPLIANCE_ADVICE': '合规建议推荐'
      }
      return textMap[type] || type
    },

    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[priority] || 'info'
    },

    getDifficultyTag(difficulty) {
      const tagMap = {
        '简单': 'success',
        '中等': 'warning',
        '较难': 'danger'
      }
      return tagMap[difficulty] || 'info'
    },

    getStepType(status) {
      const typeMap = {
        '已完成': 'success',
        '进行中': 'primary',
        '待开始': 'info'
      }
      return typeMap[status] || 'info'
    },

    getStatusTag(status) {
      const tagMap = {
        '已完成': 'success',
        '进行中': 'primary',
        '待开始': 'info'
      }
      return tagMap[status] || 'info'
    },

    getRiskLevelTag(level) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[level] || 'info'
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

.content-section h4 {
  margin-bottom: 10px;
  color: #303133;
}

.solution-item {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
}

.solution-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.solution-title {
  font-weight: bold;
  color: #303133;
}

.solution-description {
  margin-bottom: 10px;
  color: #606266;
}

.solution-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #909399;
}

.step-item h5 {
  margin-bottom: 8px;
  color: #303133;
}

.step-item p {
  margin-bottom: 10px;
  color: #606266;
}

.step-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 12px;
}

.step-duration,
.step-responsible {
  color: #909399;
}

.result-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 15px;
}

.result-icon {
  font-size: 32px;
  margin-right: 15px;
}

.result-content {
  flex: 1;
}

.result-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.result-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.result-description {
  font-size: 12px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}
</style>
