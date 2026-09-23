<template>
  <el-dialog
    title="效果评估"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div class="effectiveness-evaluation">
      <!-- 措施信息 -->
      <div class="measure-info">
        <h4>措施信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ measureData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>措施名称：</label>
              <span>{{ measureData.measureName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>实施状态：</label>
              <el-tag :type="getImplementationStatusTagType(measureData.implementationStatus)">
                {{ getImplementationStatusLabel(measureData.implementationStatus) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>完成进度：</label>
              <span class="progress-value">{{ measureData.implementationProgress || 0 }}%</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 评估表单 -->
      <div class="evaluation-form">
        <h4>效果评估</h4>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="120px"
          v-loading="loading"
        >
          <el-form-item label="评估日期" prop="evaluationDate">
            <el-date-picker
              v-model="form.evaluationDate"
              type="date"
              placeholder="选择评估日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="整体效果评分" prop="overallEffectiveness">
            <div class="rating-container">
              <el-rate
                v-model="form.overallEffectiveness"
                :max="5"
                show-score
                text-color="#ff9900"
                score-template="{value}分"
                @change="handleRatingChange"
              />
              <div class="rating-description">
                <span class="rating-text">{{ getRatingDescription(form.overallEffectiveness) }}</span>
              </div>
            </div>
          </el-form-item>

          <el-form-item label="目标达成情况" prop="goalAchievement">
            <el-select v-model="form.goalAchievement" placeholder="请选择目标达成情况">
              <el-option label="完全达成" value="FULLY_ACHIEVED" />
              <el-option label="基本达成" value="MOSTLY_ACHIEVED" />
              <el-option label="部分达成" value="PARTIALLY_ACHIEVED" />
              <el-option label="未达成" value="NOT_ACHIEVED" />
            </el-select>
          </el-form-item>

          <el-form-item label="实际效果" prop="actualEffect">
            <el-select v-model="form.actualEffect" placeholder="请选择实际效果">
              <el-option label="显著改善" value="SIGNIFICANT" />
              <el-option label="明显改善" value="OBVIOUS" />
              <el-option label="一般改善" value="MODERATE" />
              <el-option label="轻微改善" value="SLIGHT" />
              <el-option label="无明显改善" value="NONE" />
            </el-select>
          </el-form-item>

          <!-- 分项评估 -->
          <el-form-item label="分项评估">
            <div class="detailed-evaluation">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="evaluation-item">
                    <label>实施质量：</label>
                    <el-rate
                      v-model="form.implementationQuality"
                      :max="5"
                      show-score
                      text-color="#ff9900"
                      score-template="{value}分"
                    />
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="evaluation-item">
                    <label>时间效率：</label>
                    <el-rate
                      v-model="form.timeEfficiency"
                      :max="5"
                      show-score
                      text-color="#ff9900"
                      score-template="{value}分"
                    />
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="evaluation-item">
                    <label>成本控制：</label>
                    <el-rate
                      v-model="form.costControl"
                      :max="5"
                      show-score
                      text-color="#ff9900"
                      score-template="{value}分"
                    />
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="evaluation-item">
                    <label>风险缓解：</label>
                    <el-rate
                      v-model="form.riskMitigation"
                      :max="5"
                      show-score
                      text-color="#ff9900"
                      score-template="{value}分"
                    />
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-form-item>

          <el-form-item label="主要成果" prop="mainAchievements">
            <el-input
              v-model="form.mainAchievements"
              type="textarea"
              :rows="3"
              placeholder="请描述措施实施的主要成果和收益"
            />
          </el-form-item>

          <el-form-item label="存在问题" prop="existingProblems">
            <el-input
              v-model="form.existingProblems"
              type="textarea"
              :rows="3"
              placeholder="请描述实施过程中发现的问题和不足"
            />
          </el-form-item>

          <el-form-item label="改进建议" prop="improvementSuggestions">
            <el-input
              v-model="form.improvementSuggestions"
              type="textarea"
              :rows="3"
              placeholder="请提出改进建议和优化方案"
            />
          </el-form-item>

          <el-form-item label="经验总结" prop="lessonsLearned">
            <el-input
              v-model="form.lessonsLearned"
              type="textarea"
              :rows="3"
              placeholder="请总结实施过程中的经验和教训"
            />
          </el-form-item>

          <!-- 量化指标 -->
          <el-form-item label="量化指标">
            <div class="quantitative-metrics">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="metric-item">
                    <label>预算使用率：</label>
                    <el-input-number
                      v-model="form.budgetUtilization"
                      :precision="1"
                      :min="0"
                      :max="200"
                      placeholder="预算使用率"
                      style="width: 100%"
                    />
                    <span class="metric-unit">%</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="metric-item">
                    <label>时间使用率：</label>
                    <el-input-number
                      v-model="form.timeUtilization"
                      :precision="1"
                      :min="0"
                      :max="200"
                      placeholder="时间使用率"
                      style="width: 100%"
                    />
                    <span class="metric-unit">%</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="metric-item">
                    <label>风险降低率：</label>
                    <el-input-number
                      v-model="form.riskReductionRate"
                      :precision="1"
                      :min="0"
                      :max="100"
                      placeholder="风险降低率"
                      style="width: 100%"
                    />
                    <span class="metric-unit">%</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="metric-item">
                    <label>满意度评分：</label>
                    <el-input-number
                      v-model="form.satisfactionScore"
                      :precision="1"
                      :min="0"
                      :max="10"
                      placeholder="满意度评分"
                      style="width: 100%"
                    />
                    <span class="metric-unit">分</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-form-item>

          <el-form-item label="后续建议" prop="followUpRecommendations">
            <el-checkbox-group v-model="form.followUpRecommendations">
              <el-checkbox label="CONTINUE_MONITORING">继续监控</el-checkbox>
              <el-checkbox label="EXPAND_SCOPE">扩大实施范围</el-checkbox>
              <el-checkbox label="OPTIMIZE_PROCESS">优化实施流程</el-checkbox>
              <el-checkbox label="ADDITIONAL_TRAINING">加强培训</el-checkbox>
              <el-checkbox label="RESOURCE_ADJUSTMENT">调整资源配置</el-checkbox>
              <el-checkbox label="REGULAR_REVIEW">定期回顾</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="评估人员" prop="evaluators">
            <el-input
              v-model="form.evaluators"
              placeholder="请输入参与评估的人员，多个用分号分隔"
            />
          </el-form-item>

          <el-form-item label="评估依据" prop="evaluationBasis">
            <el-input
              v-model="form.evaluationBasis"
              type="textarea"
              :rows="2"
              placeholder="请描述评估的依据和标准"
            />
          </el-form-item>

          <el-form-item label="附件上传" prop="attachments">
            <el-upload
              ref="upload"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemoveFile"
              :before-upload="beforeUpload"
              multiple
              :limit="5"
            >
              <el-button size="small" type="primary">
                <i class="el-icon-upload"></i>
                上传评估报告
              </el-button>
              <div slot="tip" class="el-upload__tip">
                支持上传评估报告、数据分析等文件，单个文件不超过10MB
              </div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <!-- 评估结果预览 -->
      <div class="evaluation-preview">
        <h4>评估结果预览</h4>
        <div class="preview-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">整体评分</div>
                <div class="card-content">
                  <div class="score-display">{{ form.overallEffectiveness || 0 }}</div>
                  <div class="score-description">{{ getRatingDescription(form.overallEffectiveness) }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">目标达成</div>
                <div class="card-content">
                  <el-tag :type="getGoalAchievementTagType(form.goalAchievement)">
                    {{ getGoalAchievementLabel(form.goalAchievement) }}
                  </el-tag>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="preview-card">
                <div class="card-header">实际效果</div>
                <div class="card-content">
                  <el-tag :type="getActualEffectTagType(form.actualEffect)">
                    {{ getActualEffectLabel(form.actualEffect) }}
                  </el-tag>
                </div>
              </div>
            </el-col>
          </el-row>
          
          <div class="metrics-summary">
            <h5>量化指标汇总</h5>
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="metric-summary">
                  <div class="metric-label">预算使用率</div>
                  <div class="metric-value">{{ form.budgetUtilization || 0 }}%</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-summary">
                  <div class="metric-label">时间使用率</div>
                  <div class="metric-value">{{ form.timeUtilization || 0 }}%</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-summary">
                  <div class="metric-label">风险降低率</div>
                  <div class="metric-value">{{ form.riskReductionRate || 0 }}%</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-summary">
                  <div class="metric-label">满意度评分</div>
                  <div class="metric-value">{{ form.satisfactionScore || 0 }}分</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        保存评估
      </el-button>
      <el-button type="success" @click="handleSubmitAndComplete" :loading="loading">
        保存并完成措施
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { evaluateEffectiveness, completeImplementation } from '@/api/stateAssets/riskControlMeasure'

export default {
  name: 'EffectivenessEvaluationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    measureData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        evaluationDate: '',
        overallEffectiveness: 0,
        goalAchievement: '',
        actualEffect: '',
        implementationQuality: 0,
        timeEfficiency: 0,
        costControl: 0,
        riskMitigation: 0,
        mainAchievements: '',
        existingProblems: '',
        improvementSuggestions: '',
        lessonsLearned: '',
        budgetUtilization: null,
        timeUtilization: null,
        riskReductionRate: null,
        satisfactionScore: null,
        followUpRecommendations: [],
        evaluators: '',
        evaluationBasis: '',
        attachments: []
      },
      rules: {
        evaluationDate: [
          { required: true, message: '请选择评估日期', trigger: 'change' }
        ],
        overallEffectiveness: [
          { required: true, message: '请进行整体效果评分', trigger: 'change' }
        ],
        goalAchievement: [
          { required: true, message: '请选择目标达成情况', trigger: 'change' }
        ],
        actualEffect: [
          { required: true, message: '请选择实际效果', trigger: 'change' }
        ],
        mainAchievements: [
          { required: true, message: '请描述主要成果', trigger: 'blur' }
        ],
        evaluators: [
          { required: true, message: '请输入评估人员', trigger: 'blur' }
        ]
      },
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
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
        evaluationDate: new Date().toISOString().split('T')[0],
        overallEffectiveness: this.measureData.effectiveness || 0,
        goalAchievement: this.measureData.goalAchievement || '',
        actualEffect: this.measureData.actualEffect || '',
        implementationQuality: 0,
        timeEfficiency: 0,
        costControl: 0,
        riskMitigation: 0,
        mainAchievements: this.measureData.mainAchievements || '',
        existingProblems: this.measureData.existingProblems || '',
        improvementSuggestions: this.measureData.improvementSuggestions || '',
        lessonsLearned: this.measureData.lessonsLearned || '',
        budgetUtilization: null,
        timeUtilization: null,
        riskReductionRate: null,
        satisfactionScore: null,
        followUpRecommendations: [],
        evaluators: this.$store.getters.userInfo.userName,
        evaluationBasis: '',
        attachments: []
      }
      this.fileList = []
    },

    // 评分变化处理
    handleRatingChange(value) {
      // 可以根据评分自动调整其他字段
      if (value >= 4) {
        this.form.actualEffect = 'SIGNIFICANT'
        this.form.goalAchievement = 'FULLY_ACHIEVED'
      } else if (value >= 3) {
        this.form.actualEffect = 'OBVIOUS'
        this.form.goalAchievement = 'MOSTLY_ACHIEVED'
      } else if (value >= 2) {
        this.form.actualEffect = 'MODERATE'
        this.form.goalAchievement = 'PARTIALLY_ACHIEVED'
      } else {
        this.form.actualEffect = 'SLIGHT'
        this.form.goalAchievement = 'NOT_ACHIEVED'
      }
    },

    // 提交评估
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const evaluationData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          ...this.form,
          followUpRecommendations: this.form.followUpRecommendations.join(','),
          attachments: this.form.attachments.join(','),
          evaluateBy: this.$store.getters.userInfo.userName
        }

        const response = await evaluateEffectiveness(evaluationData)
        
        if (response.code === 200) {
          this.$message.success('效果评估保存成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存评估失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 保存并完成措施
    async handleSubmitAndComplete() {
      try {
        await this.$refs.form.validate()
        
        await this.$confirm('确定要保存评估并将措施标记为完成吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true

        // 先保存评估
        const evaluationData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          ...this.form,
          followUpRecommendations: this.form.followUpRecommendations.join(','),
          attachments: this.form.attachments.join(','),
          evaluateBy: this.$store.getters.userInfo.userName
        }

        await evaluateEffectiveness(evaluationData)

        // 再完成措施
        const completeData = {
          riskControlMeasureId: this.measureData.riskControlMeasureId,
          actualEndDate: this.form.evaluationDate,
          completionSummary: this.form.mainAchievements,
          finalProblems: this.form.existingProblems,
          finalSolutions: this.form.improvementSuggestions,
          effectiveness: this.form.overallEffectiveness,
          completeBy: this.$store.getters.userInfo.userName
        }

        const response = await completeImplementation(completeData)
        
        if (response.code === 200) {
          this.$message.success('评估保存成功，措施已完成')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'cancel' && error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 文件上传处理
    beforeUpload(file) {
      const isValidType = ['image/jpeg', 'image/png', 'image/gif', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传图片、PDF、Word文档格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },

    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        this.form.attachments.push(response.data.url)
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败：' + response.message)
      }
    },

    handleRemoveFile(file, fileList) {
      const index = this.form.attachments.indexOf(file.response?.data?.url)
      if (index > -1) {
        this.form.attachments.splice(index, 1)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
      this.fileList = []
    },

    // 工具方法
    getRatingDescription(rating) {
      const descriptions = {
        1: '效果很差',
        2: '效果较差',
        3: '效果一般',
        4: '效果良好',
        5: '效果优秀'
      }
      return descriptions[rating] || '未评分'
    },

    getImplementationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getImplementationStatusLabel(status) {
      const labelMap = {
        'PENDING': '待实施',
        'IN_PROGRESS': '实施中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停',
        'CANCELLED': '已取消'
      }
      return labelMap[status] || status
    },

    getGoalAchievementTagType(achievement) {
      const achievementMap = {
        'FULLY_ACHIEVED': 'success',
        'MOSTLY_ACHIEVED': 'primary',
        'PARTIALLY_ACHIEVED': 'warning',
        'NOT_ACHIEVED': 'danger'
      }
      return achievementMap[achievement] || ''
    },

    getGoalAchievementLabel(achievement) {
      const labelMap = {
        'FULLY_ACHIEVED': '完全达成',
        'MOSTLY_ACHIEVED': '基本达成',
        'PARTIALLY_ACHIEVED': '部分达成',
        'NOT_ACHIEVED': '未达成'
      }
      return labelMap[achievement] || achievement
    },

    getActualEffectTagType(effect) {
      const effectMap = {
        'SIGNIFICANT': 'success',
        'OBVIOUS': 'primary',
        'MODERATE': 'warning',
        'SLIGHT': 'info',
        'NONE': 'danger'
      }
      return effectMap[effect] || ''
    },

    getActualEffectLabel(effect) {
      const labelMap = {
        'SIGNIFICANT': '显著改善',
        'OBVIOUS': '明显改善',
        'MODERATE': '一般改善',
        'SLIGHT': '轻微改善',
        'NONE': '无明显改善'
      }
      return labelMap[effect] || effect
    }
  }
}
</script>

<style lang="scss" scoped>
.effectiveness-evaluation {
  .measure-info,
  .evaluation-form,
  .evaluation-preview {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

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

    .progress-value {
      font-weight: 600;
      color: #409eff;
      font-size: 16px;
    }
  }

  .rating-container {
    display: flex;
    align-items: center;

    .rating-description {
      margin-left: 16px;

      .rating-text {
        font-size: 14px;
        color: #606266;
        font-weight: 600;
      }
    }
  }

  .detailed-evaluation {
    .evaluation-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      padding: 8px;
      background: white;
      border-radius: 4px;

      label {
        width: 80px;
        font-weight: 600;
        color: #606266;
        margin-right: 12px;
      }
    }
  }

  .quantitative-metrics {
    .metric-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      padding: 8px;
      background: white;
      border-radius: 4px;

      label {
        width: 100px;
        font-weight: 600;
        color: #606266;
        margin-right: 12px;
      }

      .metric-unit {
        margin-left: 8px;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .evaluation-preview {
    .preview-content {
      .preview-card {
        text-align: center;
        padding: 16px;
        background: white;
        border-radius: 6px;
        border: 1px solid #ebeef5;

        .card-header {
          font-size: 14px;
          color: #909399;
          margin-bottom: 12px;
        }

        .card-content {
          .score-display {
            font-size: 32px;
            font-weight: 600;
            color: #409eff;
            margin-bottom: 8px;
          }

          .score-description {
            font-size: 12px;
            color: #606266;
          }
        }
      }

      .metrics-summary {
        margin-top: 20px;
        padding: 16px;
        background: white;
        border-radius: 6px;

        h5 {
          margin: 0 0 16px 0;
          color: #303133;
          font-size: 14px;
          font-weight: 600;
        }

        .metric-summary {
          text-align: center;
          padding: 12px;
          background: #f8f9fa;
          border-radius: 4px;

          .metric-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 8px;
          }

          .metric-value {
            font-size: 18px;
            font-weight: 600;
            color: #303133;
          }
        }
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
