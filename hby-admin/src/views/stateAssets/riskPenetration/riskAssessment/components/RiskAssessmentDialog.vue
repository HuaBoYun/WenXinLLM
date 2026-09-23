<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseId">
            <CompanyTreeModal
              v-model="form.enterpriseId"
              :company-name.sync="form.enterpriseName"
              placeholder="请选择企业"
              :disabled="dialogType === 'edit'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估类型" prop="assessmentType">
            <el-select v-model="form.assessmentType" placeholder="请选择评估类型">
              <el-option label="综合评估" value="COMPREHENSIVE" />
              <el-option label="财务风险评估" value="FINANCIAL" />
              <el-option label="经营风险评估" value="OPERATIONAL" />
              <el-option label="合规风险评估" value="COMPLIANCE" />
              <el-option label="治理风险评估" value="GOVERNANCE" />
              <el-option label="外部风险评估" value="EXTERNAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估年度" prop="assessmentYear">
            <el-date-picker
              v-model="form.assessmentYear"
              type="year"
              placeholder="请选择评估年度"
              value-format="yyyy"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估季度" prop="assessmentQuarter">
            <el-select v-model="form.assessmentQuarter" placeholder="请选择评估季度">
              <el-option label="第一季度" value="Q1" />
              <el-option label="第二季度" value="Q2" />
              <el-option label="第三季度" value="Q3" />
              <el-option label="第四季度" value="Q4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估日期" prop="assessmentDate">
            <el-date-picker
              v-model="form.assessmentDate"
              type="date"
              placeholder="请选择评估日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估方法" prop="assessmentMethod">
            <el-select v-model="form.assessmentMethod" placeholder="请选择评估方法">
              <el-option label="定量分析" value="QUANTITATIVE" />
              <el-option label="定性分析" value="QUALITATIVE" />
              <el-option label="混合分析" value="MIXED" />
              <el-option label="专家评估" value="EXPERT" />
              <el-option label="模型评估" value="MODEL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="评估目标" prop="assessmentObjective">
        <el-input
          v-model="form.assessmentObjective"
          type="textarea"
          :rows="3"
          placeholder="请输入评估目标和目的"
        />
      </el-form-item>

      <el-form-item label="评估范围" prop="assessmentScope">
        <el-input
          v-model="form.assessmentScope"
          type="textarea"
          :rows="3"
          placeholder="请输入评估范围和边界"
        />
      </el-form-item>

      <!-- 风险评分设置 -->
      <el-divider content-position="left">风险评分设置</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="财务风险评分" prop="financialRiskScore">
            <el-input-number
              v-model="form.financialRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="0-100分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营风险评分" prop="operationalRiskScore">
            <el-input-number
              v-model="form.operationalRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="0-100分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合规风险评分" prop="complianceRiskScore">
            <el-input-number
              v-model="form.complianceRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="0-100分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="治理风险评分" prop="governanceRiskScore">
            <el-input-number
              v-model="form.governanceRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="0-100分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="外部风险评分" prop="externalRiskScore">
            <el-input-number
              v-model="form.externalRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="0-100分"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="综合风险评分" prop="overallRiskScore">
            <el-input-number
              v-model="form.overallRiskScore"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="系统自动计算"
              :disabled="true"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 预警设置 -->
      <el-divider content-position="left">预警设置</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预警阈值" prop="warningThreshold">
            <el-input-number
              v-model="form.warningThreshold"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="预警触发阈值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="危险阈值" prop="dangerThreshold">
            <el-input-number
              v-model="form.dangerThreshold"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="危险触发阈值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="风险因素" prop="riskFactors">
        <el-input
          v-model="form.riskFactors"
          type="textarea"
          :rows="3"
          placeholder="请输入主要风险因素，多个因素用分号分隔"
        />
      </el-form-item>

      <el-form-item label="评估说明" prop="assessmentDescription">
        <el-input
          v-model="form.assessmentDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入评估说明和备注信息"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ dialogType === 'add' ? '新增' : '更新' }}
      </el-button>
      <el-button v-if="dialogType === 'add'" type="success" @click="handleSubmitAndPerform" :loading="loading">
        保存并执行评估
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskAssessment, updateRiskAssessment, performRiskAssessment } from '@/api/stateAssets/riskAssessment'
import CompanyTreeModal from '@/components/CompanyTreeModal'

export default {
  name: 'RiskAssessmentDialog',
  components: {
    CompanyTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        enterpriseId: '',
        enterpriseName: '',
        assessmentType: '',
        assessmentYear: '',
        assessmentQuarter: '',
        assessmentDate: '',
        assessmentMethod: '',
        assessmentObjective: '',
        assessmentScope: '',
        financialRiskScore: null,
        operationalRiskScore: null,
        complianceRiskScore: null,
        governanceRiskScore: null,
        externalRiskScore: null,
        overallRiskScore: null,
        warningThreshold: 70,
        dangerThreshold: 85,
        riskFactors: '',
        assessmentDescription: ''
      },
      rules: {
        enterpriseId: [
          { required: true, message: '请选择企业', trigger: 'change' }
        ],
        assessmentType: [
          { required: true, message: '请选择评估类型', trigger: 'change' }
        ],
        assessmentYear: [
          { required: true, message: '请选择评估年度', trigger: 'change' }
        ],
        assessmentDate: [
          { required: true, message: '请选择评估日期', trigger: 'change' }
        ],
        assessmentMethod: [
          { required: true, message: '请选择评估方法', trigger: 'change' }
        ],
        assessmentObjective: [
          { required: true, message: '请输入评估目标', trigger: 'blur' }
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
    },
    dialogTitle() {
      return this.dialogType === 'add' ? '新增风险评估' : '编辑风险评估'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    // 监听各项风险评分变化，自动计算综合评分
    'form.financialRiskScore': 'calculateOverallScore',
    'form.operationalRiskScore': 'calculateOverallScore',
    'form.complianceRiskScore': 'calculateOverallScore',
    'form.governanceRiskScore': 'calculateOverallScore',
    'form.externalRiskScore': 'calculateOverallScore'
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.dialogType === 'edit' && this.formData) {
        this.form = { ...this.formData }
      } else {
        this.resetForm()
        // 设置默认值
        this.form.assessmentYear = new Date().getFullYear().toString()
        this.form.assessmentDate = new Date().toISOString().split('T')[0]
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        enterpriseId: '',
        enterpriseName: '',
        assessmentType: '',
        assessmentYear: '',
        assessmentQuarter: '',
        assessmentDate: '',
        assessmentMethod: '',
        assessmentObjective: '',
        assessmentScope: '',
        financialRiskScore: null,
        operationalRiskScore: null,
        complianceRiskScore: null,
        governanceRiskScore: null,
        externalRiskScore: null,
        overallRiskScore: null,
        warningThreshold: 70,
        dangerThreshold: 85,
        riskFactors: '',
        assessmentDescription: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    // 计算综合风险评分
    calculateOverallScore() {
      const scores = [
        this.form.financialRiskScore,
        this.form.operationalRiskScore,
        this.form.complianceRiskScore,
        this.form.governanceRiskScore,
        this.form.externalRiskScore
      ].filter(score => score !== null && score !== undefined)

      if (scores.length > 0) {
        // 权重配置：财务30%，经营25%，合规20%，治理15%，外部10%
        const weights = [0.30, 0.25, 0.20, 0.15, 0.10]
        let totalScore = 0
        let totalWeight = 0

        scores.forEach((score, index) => {
          if (score !== null && score !== undefined) {
            totalScore += score * weights[index]
            totalWeight += weights[index]
          }
        })

        if (totalWeight > 0) {
          this.form.overallRiskScore = Math.round((totalScore / totalWeight) * 100) / 100
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = { ...this.form }
        
        let response
        if (this.dialogType === 'add') {
          response = await addRiskAssessment(formData)
        } else {
          response = await updateRiskAssessment(formData)
        }

        if (response.code === 200) {
          this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 保存并执行评估
    async handleSubmitAndPerform() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = { ...this.form }
        
        // 先保存
        const saveResponse = await addRiskAssessment(formData)
        if (saveResponse.code === 200) {
          // 再执行评估
          const assessmentId = saveResponse.data.riskAssessmentId
          const performResponse = await performRiskAssessment({
            riskAssessmentId: assessmentId,
            performBy: this.$store.getters.userInfo.userName
          })
          
          if (performResponse.code === 200) {
            this.$message.success('保存成功并已执行评估')
            this.$emit('success')
            this.handleClose()
          }
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0;
}

.el-form {
  .el-form-item {
    margin-bottom: 18px;
  }
}
</style>
