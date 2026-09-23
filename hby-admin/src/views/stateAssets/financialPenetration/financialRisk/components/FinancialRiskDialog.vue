<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="riskForm"
      :model="riskForm"
      :rules="isViewMode ? {} : rules"
      :disabled="isViewMode"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="riskForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险类型" prop="riskType">
            <el-select v-model="riskForm.riskType" placeholder="请选择风险类型" style="width: 100%;">
              <el-option label="流动性风险" value="LIQUIDITY_RISK" />
              <el-option label="偿债风险" value="SOLVENCY_RISK" />
              <el-option label="盈利风险" value="PROFITABILITY_RISK" />
              <el-option label="营运风险" value="OPERATIONAL_RISK" />
              <el-option label="市场风险" value="MARKET_RISK" />
              <el-option label="信用风险" value="CREDIT_RISK" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险指标" prop="indicatorName">
            <el-input v-model="riskForm.indicatorName" placeholder="请输入风险指标名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检测方法" prop="detectionMethod">
            <el-select v-model="riskForm.detectionMethod" placeholder="请选择检测方法" style="width: 100%;">
              <el-option label="比率分析" value="比率分析" />
              <el-option label="趋势分析" value="趋势分析" />
              <el-option label="同业对比" value="同业对比" />
              <el-option label="模型预测" value="模型预测" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="期望值" prop="expectedValue">
            <el-input-number
              v-model="riskForm.expectedValue"
              :precision="2"
              placeholder="请输入期望值"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际值" prop="actualValue">
            <el-input-number
              v-model="riskForm.actualValue"
              :precision="2"
              placeholder="请输入实际值"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险评分" prop="riskScore">
            <el-input-number
              v-model="riskForm.riskScore"
              :min="0"
              :max="100"
              placeholder="请输入风险评分(0-100)"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级">
            <el-input :value="computedRiskLevel" disabled placeholder="由评分自动计算" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告期间" prop="period">
            <el-input v-model="riskForm.period" placeholder="如: 2024-Q4" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="风险描述" prop="riskDescription">
        <el-input
          v-model="riskForm.riskDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入风险描述"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ isViewMode ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isViewMode" type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addFinancialRisk, updateFinancialRisk, getFinancialRiskDetail } from '@/api/stateAssets/financialRisk'

export default {
  name: 'FinancialRiskDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    riskData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      submitLoading: false,
      riskForm: {
        enterpriseName: '',
        riskType: '',
        riskScore: 0,
        indicatorName: '',
        detectionMethod: '',
        expectedValue: undefined,
        actualValue: undefined,
        period: '',
        riskDescription: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        riskType: [
          { required: true, message: '请选择风险类型', trigger: 'change' }
        ],
        riskScore: [
          { required: true, message: '请输入风险评分', trigger: 'blur' }
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
      const titleMap = {
        add: '新增财务风险',
        edit: '编辑财务风险',
        view: '查看财务风险'
      }
      return titleMap[this.dialogType] || '财务风险'
    },
    isViewMode() {
      return this.dialogType === 'view'
    },
    computedRiskLevel() {
      const score = this.riskForm.riskScore
      if (score === undefined || score === null) return ''
      if (score >= 80) return '极高风险'
      if (score >= 60) return '高风险'
      if (score >= 40) return '中风险'
      return '低风险'
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
    initForm() {
      if (this.dialogType === 'add') {
        this.riskForm = {
          enterpriseName: '',
          riskType: '',
          riskScore: 0,
          indicatorName: '',
          detectionMethod: '',
          expectedValue: undefined,
          actualValue: undefined,
          period: '',
          riskDescription: ''
        }
      } else {
        // 编辑或查看模式，回填数据
        this.riskForm = {
          id: this.riskData.id || this.riskData.anomalyId || '',
          companyId: this.riskData.companyId || '',
          enterpriseName: this.riskData.enterpriseName || this.riskData.companyName || '',
          riskType: this.riskData.riskType || this.riskData.anomalyType || '',
          riskScore: this.riskData.riskScore != null ? this.riskData.riskScore : (this.riskData.deviationRate != null ? this.riskData.deviationRate : 0),
          indicatorName: this.riskData.indicatorName || '',
          detectionMethod: this.riskData.detectionMethod || '',
          expectedValue: this.riskData.expectedValue,
          actualValue: this.riskData.actualValue,
          period: this.riskData.period || '',
          riskDescription: this.riskData.riskDescription || this.riskData.description || ''
        }
      }
    },

    handleSubmit() {
      this.$refs.riskForm.validate(async(valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          // 映射前端字段到后端字段
          const params = {
            companyName: this.riskForm.enterpriseName,
            anomalyType: this.riskForm.riskType,
            deviationRate: this.riskForm.riskScore,
            indicatorName: this.riskForm.indicatorName,
            detectionMethod: this.riskForm.detectionMethod,
            expectedValue: this.riskForm.expectedValue,
            actualValue: this.riskForm.actualValue,
            period: this.riskForm.period,
            description: this.riskForm.riskDescription
          }
          if (this.riskForm.companyId) {
            params.companyId = this.riskForm.companyId
          }

          if (this.dialogType === 'edit') {
            params.anomalyId = this.riskForm.id
            await updateFinancialRisk(params)
          } else {
            await addFinancialRisk(params)
          }

          this.$message.success(this.dialogType === 'edit' ? '编辑成功' : '新增成功')
          this.$emit('refresh')
          this.handleClose()
        } catch (error) {
          this.$message.error(error.message || '操作失败，请稍后重试')
        } finally {
          this.submitLoading = false
        }
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      if (this.$refs.riskForm) {
        this.$refs.riskForm.resetFields()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
