<template>
  <el-dialog
    title="执行风险评估"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <div v-loading="loading" class="perform-assessment">
      <!-- 评估信息 -->
      <div class="assessment-info">
        <h4>评估基本信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ assessmentData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>评估类型：</label>
              <el-tag :type="getAssessmentTypeTagType(assessmentData.assessmentType)">
                {{ getAssessmentTypeLabel(assessmentData.assessmentType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>评估年度：</label>
              <span>{{ assessmentData.assessmentYear || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>评估方法：</label>
              <span>{{ getAssessmentMethodLabel(assessmentData.assessmentMethod) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 执行参数设置 -->
      <div class="execution-params">
        <h4>执行参数设置</h4>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="执行人员" prop="performBy">
                <el-input v-model="form.performBy" placeholder="请输入执行人员" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行时间" prop="performTime">
                <el-date-picker
                  v-model="form.performTime"
                  type="datetime"
                  placeholder="请选择执行时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="是否自动计算" prop="autoCalculate">
            <el-switch
              v-model="form.autoCalculate"
              active-text="自动计算风险评分"
              inactive-text="手动输入评分"
            />
          </el-form-item>

          <el-form-item label="是否触发预警" prop="enableWarning">
            <el-switch
              v-model="form.enableWarning"
              active-text="启用预警检查"
              inactive-text="禁用预警检查"
            />
          </el-form-item>

          <el-form-item label="执行说明" prop="performNotes">
            <el-input
              v-model="form.performNotes"
              type="textarea"
              :rows="3"
              placeholder="请输入执行说明和备注"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 评估进度 -->
      <div v-if="isPerforming" class="assessment-progress">
        <h4>评估执行进度</h4>
        <div class="progress-container">
          <el-steps :active="currentStep" finish-status="success" align-center>
            <el-step title="数据收集" description="收集企业基础数据" />
            <el-step title="风险识别" description="识别各类风险因素" />
            <el-step title="风险评估" description="计算风险评分" />
            <el-step title="预警检查" description="检查预警条件" />
            <el-step title="生成报告" description="生成评估报告" />
          </el-steps>
          
          <div class="progress-detail">
            <el-progress
              :percentage="progressPercentage"
              :status="progressStatus"
              :stroke-width="8"
            />
            <p class="progress-text">{{ progressText }}</p>
          </div>
        </div>
      </div>

      <!-- 执行结果 -->
      <div v-if="executionResult" class="execution-result">
        <h4>执行结果</h4>
        <el-alert
          :title="executionResult.success ? '评估执行成功' : '评估执行失败'"
          :type="executionResult.success ? 'success' : 'error'"
          :description="executionResult.message"
          show-icon
          :closable="false"
        />
        
        <div v-if="executionResult.success && executionResult.data" class="result-details">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="result-item">
                <label>综合风险评分：</label>
                <span class="score-value" :class="getRiskScoreClass(executionResult.data.overallRiskScore)">
                  {{ executionResult.data.overallRiskScore }}
                </span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="result-item">
                <label>风险等级：</label>
                <el-tag :type="getRiskLevelTagType(executionResult.data.overallRiskLevel)">
                  {{ getRiskLevelLabel(executionResult.data.overallRiskLevel) }}
                </el-tag>
              </div>
            </el-col>
          </el-row>
          
          <div v-if="executionResult.data.isWarningTriggered" class="warning-info">
            <el-alert
              title="触发风险预警"
              :type="getWarningLevelTagType(executionResult.data.warningLevel)"
              :description="executionResult.data.warningMessage"
              show-icon
              :closable="false"
            />
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose" :disabled="isPerforming">取消</el-button>
      <el-button
        type="primary"
        @click="handlePerform"
        :loading="isPerforming"
        :disabled="isPerforming"
      >
        {{ isPerforming ? '执行中...' : '开始执行' }}
      </el-button>
      <el-button
        v-if="executionResult && executionResult.success"
        type="success"
        @click="handleViewResult"
      >
        查看详情
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { performRiskAssessment, calculateOverallRiskScore } from '@/api/stateAssets/riskAssessment'

export default {
  name: 'PerformAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      isPerforming: false,
      currentStep: 0,
      progressPercentage: 0,
      progressStatus: '',
      progressText: '',
      executionResult: null,
      form: {
        performBy: '',
        performTime: '',
        autoCalculate: true,
        enableWarning: true,
        performNotes: ''
      },
      rules: {
        performBy: [
          { required: true, message: '请输入执行人员', trigger: 'blur' }
        ],
        performTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ]
      }
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
        this.initForm()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.form = {
        performBy: this.$store.getters.userInfo.userName || '',
        performTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
        autoCalculate: true,
        enableWarning: true,
        performNotes: ''
      }
      this.isPerforming = false
      this.currentStep = 0
      this.progressPercentage = 0
      this.progressStatus = ''
      this.progressText = ''
      this.executionResult = null
    },

    // 执行评估
    async handlePerform() {
      try {
        await this.$refs.form.validate()
        
        this.isPerforming = true
        this.executionResult = null
        
        // 模拟执行步骤
        await this.simulateExecution()
        
        // 调用后端API执行评估
        const params = {
          riskAssessmentId: this.assessmentData.riskAssessmentId,
          performBy: this.form.performBy,
          performTime: this.form.performTime,
          autoCalculate: this.form.autoCalculate,
          enableWarning: this.form.enableWarning,
          performNotes: this.form.performNotes
        }
        
        const response = await performRiskAssessment(params)
        
        if (response.code === 200) {
          this.executionResult = {
            success: true,
            message: '风险评估执行成功',
            data: response.data
          }
          this.progressStatus = 'success'
          this.progressText = '评估执行完成'
          this.$emit('success')
        } else {
          throw new Error(response.message || '执行失败')
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.executionResult = {
            success: false,
            message: error.message || '评估执行失败'
          }
          this.progressStatus = 'exception'
          this.progressText = '执行失败'
        }
      } finally {
        this.isPerforming = false
      }
    },

    // 模拟执行过程
    async simulateExecution() {
      const steps = [
        { step: 0, percentage: 20, text: '正在收集企业基础数据...' },
        { step: 1, percentage: 40, text: '正在识别风险因素...' },
        { step: 2, percentage: 60, text: '正在计算风险评分...' },
        { step: 3, percentage: 80, text: '正在检查预警条件...' },
        { step: 4, percentage: 100, text: '正在生成评估报告...' }
      ]

      for (const stepInfo of steps) {
        this.currentStep = stepInfo.step
        this.progressPercentage = stepInfo.percentage
        this.progressText = stepInfo.text
        
        // 模拟延时
        await new Promise(resolve => setTimeout(resolve, 1000))
      }
    },

    // 查看结果详情
    handleViewResult() {
      this.$emit('view-detail', this.assessmentData.riskAssessmentId)
      this.handleClose()
    },

    // 关闭对话框
    handleClose() {
      if (!this.isPerforming) {
        this.dialogVisible = false
        this.initForm()
      }
    },

    // 工具方法
    getAssessmentTypeTagType(type) {
      const typeMap = {
        'COMPREHENSIVE': 'primary',
        'FINANCIAL': 'success',
        'OPERATIONAL': 'warning',
        'COMPLIANCE': 'info',
        'GOVERNANCE': 'danger',
        'EXTERNAL': ''
      }
      return typeMap[type] || ''
    },

    getAssessmentTypeLabel(type) {
      const labelMap = {
        'COMPREHENSIVE': '综合评估',
        'FINANCIAL': '财务风险评估',
        'OPERATIONAL': '经营风险评估',
        'COMPLIANCE': '合规风险评估',
        'GOVERNANCE': '治理风险评估',
        'EXTERNAL': '外部风险评估'
      }
      return labelMap[type] || type
    },

    getAssessmentMethodLabel(method) {
      const labelMap = {
        'QUANTITATIVE': '定量分析',
        'QUALITATIVE': '定性分析',
        'MIXED': '混合分析',
        'EXPERT': '专家评估',
        'MODEL': '模型评估'
      }
      return labelMap[method] || method
    },

    getRiskLevelTagType(level) {
      const levelMap = {
        'VERY_LOW': 'success',
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'VERY_HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'VERY_LOW': '极低风险',
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险',
        'VERY_HIGH': '极高风险',
        'CRITICAL': '临界风险'
      }
      return labelMap[level] || level
    },

    getWarningLevelTagType(level) {
      const levelMap = {
        'GREEN': 'success',
        'YELLOW': 'warning',
        'ORANGE': 'warning',
        'RED': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskScoreClass(score) {
      if (!score) return ''
      if (score >= 80) return 'risk-score-high'
      if (score >= 60) return 'risk-score-medium'
      return 'risk-score-low'
    }
  }
}
</script>

<style lang="scss" scoped>
.perform-assessment {
  .assessment-info,
  .execution-params,
  .assessment-progress,
  .execution-result {
    margin-bottom: 24px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
  }

  .progress-container {
    .el-steps {
      margin-bottom: 24px;
    }

    .progress-detail {
      text-align: center;
      
      .progress-text {
        margin: 12px 0 0 0;
        color: #606266;
        font-size: 14px;
      }
    }
  }

  .result-details {
    margin-top: 16px;
    padding: 16px;
    background: white;
    border-radius: 6px;

    .result-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }

      .score-value {
        font-size: 18px;
        font-weight: 600;
      }
    }

    .warning-info {
      margin-top: 16px;
    }
  }

  .risk-score-high {
    color: #f56c6c;
  }

  .risk-score-medium {
    color: #e6a23c;
  }

  .risk-score-low {
    color: #67c23a;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
