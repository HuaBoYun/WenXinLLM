<template>
  <div class="assessment-360-evaluation">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>360度评估执行</h2>
        <p>{{ assessmentData.assessmentName }}</p>
      </div>
      <div class="header-info">
        <el-tag :type="getStatusTagType(assessmentData.assessmentStatus)">
          {{ formatAssessmentStatus(assessmentData.assessmentStatus) }}
        </el-tag>
        <span class="deadline">截止时间：{{ formatDateTime(assessmentData.endTime) }}</span>
      </div>
    </div>

    <!-- 评估信息卡片 -->
    <div class="assessment-info">
      <el-card>
        <div class="info-content">
          <div class="info-item">
            <span class="label">被评估人：</span>
            <span class="value">{{ assessmentData.assessedUserName }}</span>
          </div>
          <div class="info-item">
            <span class="label">评估类型：</span>
            <span class="value">{{ formatAssessmentType(assessmentData.assessmentType) }}</span>
          </div>
          <div class="info-item">
            <span class="label">评估周期：</span>
            <span class="value">{{ getAssessmentPeriod() }}</span>
          </div>
          <div class="info-item">
            <span class="label">整体进度：</span>
            <el-progress :percentage="assessmentData.completionRate || 0" :stroke-width="6" style="width: 200px;" />
          </div>
        </div>
      </el-card>
    </div>

    <!-- 评估维度选择 -->
    <div class="evaluation-tabs">
      <el-tabs v-model="activeEvaluationType" type="card" @tab-click="handleTabClick">
        <el-tab-pane 
          v-if="canEvaluate('SELF')" 
          label="自评" 
          name="SELF"
        >
          <div class="tab-badge" v-if="evaluationStatus.SELF">
            <el-badge :value="evaluationStatus.SELF === 'completed' ? '已完成' : '进行中'" :type="evaluationStatus.SELF === 'completed' ? 'success' : 'primary'">
            </el-badge>
          </div>
        </el-tab-pane>
        
        <el-tab-pane 
          v-if="canEvaluate('SUPERIOR')" 
          label="上级评价" 
          name="SUPERIOR"
        >
          <div class="tab-badge" v-if="evaluationStatus.SUPERIOR">
            <el-badge :value="evaluationStatus.SUPERIOR === 'completed' ? '已完成' : '进行中'" :type="evaluationStatus.SUPERIOR === 'completed' ? 'success' : 'primary'">
            </el-badge>
          </div>
        </el-tab-pane>
        
        <el-tab-pane 
          v-if="canEvaluate('PEER')" 
          label="同级评价" 
          name="PEER"
        >
          <div class="tab-badge" v-if="evaluationStatus.PEER">
            <el-badge :value="evaluationStatus.PEER === 'completed' ? '已完成' : '进行中'" :type="evaluationStatus.PEER === 'completed' ? 'success' : 'primary'">
            </el-badge>
          </div>
        </el-tab-pane>
        
        <el-tab-pane 
          v-if="canEvaluate('SUBORDINATE')" 
          label="下级评价" 
          name="SUBORDINATE"
        >
          <div class="tab-badge" v-if="evaluationStatus.SUBORDINATE">
            <el-badge :value="evaluationStatus.SUBORDINATE === 'completed' ? '已完成' : '进行中'" :type="evaluationStatus.SUBORDINATE === 'completed' ? 'success' : 'primary'">
            </el-badge>
          </div>
        </el-tab-pane>
        
        <el-tab-pane 
          v-if="canEvaluate('CUSTOMER')" 
          label="客户评价" 
          name="CUSTOMER"
        >
          <div class="tab-badge" v-if="evaluationStatus.CUSTOMER">
            <el-badge :value="evaluationStatus.CUSTOMER === 'completed' ? '已完成' : '进行中'" :type="evaluationStatus.CUSTOMER === 'completed' ? 'success' : 'primary'">
            </el-badge>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 评估表单 -->
    <div class="evaluation-form" v-if="activeEvaluationType">
      <el-card>
        <div slot="header">
          <span>{{ getEvaluationTitle() }}</span>
          <div style="float: right;">
            <el-button v-if="!isCompleted()" size="small" @click="saveDraft">保存草稿</el-button>
            <el-button v-if="!isCompleted()" type="primary" size="small" @click="submitEvaluation">提交评价</el-button>
            <el-tag v-else type="success">已完成</el-tag>
          </div>
        </div>

        <el-form ref="evaluationForm" :model="evaluationForm" :rules="evaluationRules" label-width="120px">
          <!-- 评分部分 -->
          <div class="score-section">
            <h4>综合评分</h4>
            <el-form-item label="总体评分" prop="overallScore">
              <div class="score-input">
                <el-input-number
                  v-model="evaluationForm.overallScore"
                  :min="0"
                  :max="100"
                  :precision="1"
                  :disabled="isCompleted()"
                  style="width: 150px;"
                />
                <span class="score-unit">分</span>
                <div class="score-level">
                  {{ getScoreLevel(evaluationForm.overallScore) }}
                </div>
              </div>
            </el-form-item>

            <!-- 维度评分 -->
            <div class="dimension-scores">
              <h5>分维度评分</h5>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="工作质量">
                    <el-rate
                      v-model="evaluationForm.qualityScore"
                      :max="5"
                      :disabled="isCompleted()"
                      show-score
                      text-color="#ff9900"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="工作效率">
                    <el-rate
                      v-model="evaluationForm.efficiencyScore"
                      :max="5"
                      :disabled="isCompleted()"
                      show-score
                      text-color="#ff9900"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="团队协作">
                    <el-rate
                      v-model="evaluationForm.teamworkScore"
                      :max="5"
                      :disabled="isCompleted()"
                      show-score
                      text-color="#ff9900"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创新能力">
                    <el-rate
                      v-model="evaluationForm.innovationScore"
                      :max="5"
                      :disabled="isCompleted()"
                      show-score
                      text-color="#ff9900"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 优点和不足 -->
          <div class="feedback-section">
            <h4>评价反馈</h4>
            <el-form-item label="主要优点" prop="strengths">
              <el-input
                v-model="evaluationForm.strengths"
                type="textarea"
                :rows="3"
                placeholder="请描述被评估人的主要优点和突出表现"
                :disabled="isCompleted()"
              />
            </el-form-item>

            <el-form-item label="改进建议" prop="improvements">
              <el-input
                v-model="evaluationForm.improvements"
                type="textarea"
                :rows="3"
                placeholder="请提出具体的改进建议和发展方向"
                :disabled="isCompleted()"
              />
            </el-form-item>

            <el-form-item label="整体评价" prop="overallComment">
              <el-input
                v-model="evaluationForm.overallComment"
                type="textarea"
                :rows="4"
                placeholder="请给出整体评价和综合意见"
                :disabled="isCompleted()"
              />
            </el-form-item>
          </div>

          <!-- 发展建议 -->
          <div class="development-section">
            <h4>发展建议</h4>
            <el-form-item label="培训需求">
              <el-checkbox-group v-model="evaluationForm.trainingNeeds" :disabled="isCompleted()">
                <el-checkbox label="leadership">领导力培训</el-checkbox>
                <el-checkbox label="communication">沟通技巧</el-checkbox>
                <el-checkbox label="technical">技术技能</el-checkbox>
                <el-checkbox label="management">管理能力</el-checkbox>
                <el-checkbox label="innovation">创新思维</el-checkbox>
                <el-checkbox label="teamwork">团队协作</el-checkbox>
              </el-checkbox-group>
            </el-form-item>

            <el-form-item label="发展方向">
              <el-radio-group v-model="evaluationForm.developmentDirection" :disabled="isCompleted()">
                <el-radio label="specialist">专业专家路线</el-radio>
                <el-radio label="manager">管理路线</el-radio>
                <el-radio label="mixed">复合型发展</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="职业规划建议">
              <el-input
                v-model="evaluationForm.careerAdvice"
                type="textarea"
                :rows="3"
                placeholder="请提供职业发展规划建议"
                :disabled="isCompleted()"
              />
            </el-form-item>
          </div>
        </el-form>
      </el-card>
    </div>

    <!-- 评估历史 -->
    <div class="evaluation-history" v-if="evaluationHistory.length > 0">
      <el-card>
        <div slot="header">
          <span>评估历史</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="item in evaluationHistory"
            :key="item.id"
            :timestamp="formatDateTime(item.createTime)"
            :type="item.type"
          >
            <div class="history-item">
              <div class="history-title">{{ item.title }}</div>
              <div class="history-content">{{ item.content }}</div>
              <div class="history-score" v-if="item.score">
                评分：<span :style="{ color: getScoreColor(item.score) }">{{ item.score }}</span>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  get360AssessmentById,
  submitSelfEvaluation,
  submitSuperiorEvaluation,
  submitPeerEvaluation,
  submitSubordinateEvaluation,
  submitCustomerEvaluation
} from '@/api/managementAccountant/pm/assessment360'

export default {
  name: 'Assessment360Evaluation',
  data() {
    return {
      loading: false,
      assessmentId: null,
      activeEvaluationType: '',
      
      assessmentData: {
        assessmentId: null,
        assessmentName: '',
        assessedUserName: '',
        assessmentType: '',
        assessmentStatus: '',
        endTime: null,
        completionRate: 0
      },

      evaluationStatus: {
        SELF: null,
        SUPERIOR: null,
        PEER: null,
        SUBORDINATE: null,
        CUSTOMER: null
      },

      evaluationForm: {
        overallScore: null,
        qualityScore: 0,
        efficiencyScore: 0,
        teamworkScore: 0,
        innovationScore: 0,
        strengths: '',
        improvements: '',
        overallComment: '',
        trainingNeeds: [],
        developmentDirection: '',
        careerAdvice: ''
      },

      evaluationRules: {
        overallScore: [
          { required: true, message: '请输入总体评分', trigger: 'blur' }
        ],
        strengths: [
          { required: true, message: '请描述主要优点', trigger: 'blur' }
        ],
        improvements: [
          { required: true, message: '请提出改进建议', trigger: 'blur' }
        ],
        overallComment: [
          { required: true, message: '请给出整体评价', trigger: 'blur' }
        ]
      },

      evaluationHistory: [],
      currentUserRole: 'SELF' // 当前用户角色，实际应该从用户信息获取
    }
  },

  created() {
    this.assessmentId = this.$route.params.id
    this.loadAssessmentData()
    this.loadEvaluationHistory()
    this.initEvaluationType()
  },

  methods: {
    // 加载评估数据
    async loadAssessmentData() {
      this.loading = true
      try {
        const response = await get360AssessmentById(this.assessmentId)
        if (response.success) {
          this.assessmentData = response.data
          this.updateEvaluationStatus()
        } else {
          this.$message.error(response.message || '加载失败')
        }
      } catch (error) {
        this.$message.error('加载失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 更新评估状态
    updateEvaluationStatus() {
      this.evaluationStatus.SELF = this.assessmentData.selfScore ? 'completed' : 'pending'
      this.evaluationStatus.SUPERIOR = this.assessmentData.superiorScore ? 'completed' : 'pending'
      this.evaluationStatus.PEER = this.assessmentData.peerScore ? 'completed' : 'pending'
      this.evaluationStatus.SUBORDINATE = this.assessmentData.subordinateScore ? 'completed' : 'pending'
      this.evaluationStatus.CUSTOMER = this.assessmentData.customerScore ? 'completed' : 'pending'
    },

    // 初始化评估类型
    initEvaluationType() {
      // 根据用户角色和权限确定可以进行的评估类型
      const availableTypes = ['SELF', 'SUPERIOR', 'PEER', 'SUBORDINATE', 'CUSTOMER']
      for (const type of availableTypes) {
        if (this.canEvaluate(type)) {
          this.activeEvaluationType = type
          break
        }
      }
    },

    // 检查是否可以评估
    canEvaluate(type) {
      // TODO: 实际应该根据用户权限和角色判断
      return true
    },

    // 检查是否已完成
    isCompleted() {
      return this.evaluationStatus[this.activeEvaluationType] === 'completed'
    },

    // 获取评估标题
    getEvaluationTitle() {
      const titleMap = {
        'SELF': '自我评估',
        'SUPERIOR': '上级评价',
        'PEER': '同级评价',
        'SUBORDINATE': '下级评价',
        'CUSTOMER': '客户评价'
      }
      return titleMap[this.activeEvaluationType] || '评估'
    },

    // 获取评估周期
    getAssessmentPeriod() {
      let period = `${this.assessmentData.assessmentYear}年`
      if (this.assessmentData.assessmentQuarter) {
        period += `第${this.assessmentData.assessmentQuarter}季度`
      }
      if (this.assessmentData.assessmentMonth) {
        period += `${this.assessmentData.assessmentMonth}月`
      }
      return period
    },

    // 标签页点击
    handleTabClick(tab) {
      this.activeEvaluationType = tab.name
      this.loadEvaluationData()
    },

    // 加载评估数据
    loadEvaluationData() {
      // TODO: 根据评估类型加载对应的评估数据
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.evaluationForm = {
        overallScore: null,
        qualityScore: 0,
        efficiencyScore: 0,
        teamworkScore: 0,
        innovationScore: 0,
        strengths: '',
        improvements: '',
        overallComment: '',
        trainingNeeds: [],
        developmentDirection: '',
        careerAdvice: ''
      }
    },

    // 保存草稿
    async saveDraft() {
      try {
        // TODO: 实现保存草稿逻辑
        this.$message.success('草稿保存成功')
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 提交评价
    async submitEvaluation() {
      try {
        await this.$refs.evaluationForm.validate()

        await this.$confirm('确认提交评价？提交后不可修改。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const evaluationData = {
          score: this.evaluationForm.overallScore,
          qualityScore: this.evaluationForm.qualityScore,
          efficiencyScore: this.evaluationForm.efficiencyScore,
          teamworkScore: this.evaluationForm.teamworkScore,
          innovationScore: this.evaluationForm.innovationScore,
          strengths: this.evaluationForm.strengths,
          improvements: this.evaluationForm.improvements,
          overallComment: this.evaluationForm.overallComment,
          trainingNeeds: this.evaluationForm.trainingNeeds,
          developmentDirection: this.evaluationForm.developmentDirection,
          careerAdvice: this.evaluationForm.careerAdvice,
          evaluationType: this.activeEvaluationType,
          evaluationTime: new Date()
        }

        let response
        switch (this.activeEvaluationType) {
          case 'SELF':
            response = await submitSelfEvaluation(this.assessmentId, evaluationData)
            break
          case 'SUPERIOR':
            response = await submitSuperiorEvaluation(this.assessmentId, evaluationData)
            break
          case 'PEER':
            response = await submitPeerEvaluation(this.assessmentId, evaluationData)
            break
          case 'SUBORDINATE':
            response = await submitSubordinateEvaluation(this.assessmentId, evaluationData)
            break
          case 'CUSTOMER':
            response = await submitCustomerEvaluation(this.assessmentId, evaluationData)
            break
        }

        if (response.success) {
          this.$message.success('提交成功')
          this.loadAssessmentData()
          this.loadEvaluationHistory()
        } else {
          this.$message.error(response.message || '提交失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败：' + error.message)
        }
      }
    },

    // 加载评估历史
    loadEvaluationHistory() {
      // TODO: 实际应该从API加载评估历史
      this.evaluationHistory = [
        {
          id: 1,
          title: '评估启动',
          content: '360度评估已启动，请相关人员及时完成评价',
          createTime: new Date(),
          type: 'primary'
        }
      ]
    },

    // 获取分数等级
    getScoreLevel(score) {
      if (!score) return ''
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '一般'
      if (score >= 60) return '及格'
      return '不及格'
    },

    // 格式化方法
    formatAssessmentStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    formatAssessmentType(type) {
      const typeMap = {
        'ANNUAL': '年度评估',
        'QUARTERLY': '季度评估',
        'MONTHLY': '月度评估',
        'PROJECT': '项目评估'
      }
      return typeMap[type] || type
    },

    formatDateTime(date) {
      if (!date) return ''
      return this.$moment(date).format('YYYY-MM-DD HH:mm')
    },

    // 标签类型
    getStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ONGOING': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    // 分数颜色
    getScoreColor(score) {
      if (!score) return '#c0c4cc'
      if (score >= 90) return '#67c23a'
      if (score >= 80) return '#409eff'
      if (score >= 70) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.assessment-360-evaluation {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.deadline {
  color: #e6a23c;
  font-size: 14px;
}

.assessment-info {
  margin-bottom: 20px;
}

.info-content {
  display: flex;
  align-items: center;
  gap: 30px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  color: #606266;
  margin-right: 8px;
}

.info-item .value {
  color: #303133;
  font-weight: 500;
}

.evaluation-tabs {
  margin-bottom: 20px;
}

.tab-badge {
  position: absolute;
  top: -5px;
  right: -5px;
}

.evaluation-form {
  margin-bottom: 20px;
}

.score-section,
.feedback-section,
.development-section {
  margin-bottom: 30px;
}

.score-section h4,
.feedback-section h4,
.development-section h4 {
  margin-bottom: 20px;
  color: #303133;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.dimension-scores h5 {
  margin: 20px 0 15px 0;
  color: #606266;
}

.score-input {
  display: flex;
  align-items: center;
  gap: 10px;
}

.score-unit {
  color: #909399;
}

.score-level {
  color: #409eff;
  font-weight: 500;
}

.evaluation-history {
  margin-top: 20px;
}

.history-item {
  padding: 10px 0;
}

.history-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.history-content {
  color: #606266;
  font-size: 14px;
  margin-bottom: 5px;
}

.history-score {
  font-size: 14px;
  color: #909399;
}
</style>
