<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="performanceForm"
      :model="performanceForm"
      :rules="rules"
      label-width="120px"
      :disabled="dialogType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="performanceForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价期间" prop="evaluationPeriod">
            <el-date-picker
              v-model="performanceForm.evaluationPeriod"
              type="year"
              placeholder="请选择年份"
              value-format="yyyy"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评价类型" prop="evaluationType">
            <el-select v-model="performanceForm.evaluationType" placeholder="请选择评价类型" style="width: 100%;">
              <el-option label="年度绩效评价" value="ANNUAL"></el-option>
              <el-option label="季度绩效评价" value="QUARTERLY"></el-option>
              <el-option label="专项绩效评价" value="SPECIAL"></el-option>
              <el-option label="对标绩效评价" value="BENCHMARK"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="绩效等级" prop="performanceLevel">
            <el-select v-model="performanceForm.performanceLevel" placeholder="请选择绩效等级" style="width: 100%;">
              <el-option label="优秀" value="EXCELLENT"></el-option>
              <el-option label="良好" value="GOOD"></el-option>
              <el-option label="一般" value="AVERAGE"></el-option>
              <el-option label="较差" value="POOR"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="绩效评分" prop="performanceScore">
            <el-input-number
              v-model="performanceForm.performanceScore"
              :min="0"
              :max="100"
              placeholder="请输入绩效评分"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属行业" prop="industry">
            <el-input v-model="performanceForm.industry" placeholder="请输入所属行业" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="营业收入(万)" prop="revenue">
            <el-input-number
              v-model="performanceForm.revenue"
              :precision="2"
              :controls="false"
              placeholder="请输入营业收入"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净利润(万)" prop="netProfit">
            <el-input-number
              v-model="performanceForm.netProfit"
              :precision="2"
              :controls="false"
              placeholder="请输入净利润"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总资产(万)" prop="totalAssets">
            <el-input-number
              v-model="performanceForm.totalAssets"
              :precision="2"
              :controls="false"
              placeholder="请输入总资产"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总负债(万)" prop="totalLiabilities">
            <el-input-number
              v-model="performanceForm.totalLiabilities"
              :precision="2"
              :controls="false"
              placeholder="请输入总负债"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="净资产(万)" prop="netAssets">
            <el-input-number
              v-model="performanceForm.netAssets"
              :precision="2"
              :controls="false"
              placeholder="请输入净资产"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营现金流(万)" prop="operatingCashflow">
            <el-input-number
              v-model="performanceForm.operatingCashflow"
              :precision="2"
              :controls="false"
              placeholder="请输入经营现金流"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="评价状态" prop="evaluationStatus">
        <el-radio-group v-model="performanceForm.evaluationStatus">
          <el-radio label="DRAFT">草稿</el-radio>
          <el-radio label="SUBMITTED">已提交</el-radio>
          <el-radio label="APPROVED">已审核</el-radio>
          <el-radio label="PUBLISHED">已发布</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="评价说明" prop="evaluationNotes">
        <el-input
          v-model="performanceForm.evaluationNotes"
          type="textarea"
          :rows="3"
          placeholder="请输入评价说明"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        :loading="submitLoading"
        @click="handleSubmit"
      >确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createFinancialPerformance, updateFinancialPerformance } from '@/api/stateAssets/financialPerformance'

export default {
  name: 'FinancialPerformanceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    performanceData: {
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
      performanceForm: {
        enterpriseName: '',
        evaluationPeriod: '',
        evaluationType: '',
        performanceLevel: '',
        performanceScore: 0,
        industry: '',
        revenue: null,
        netProfit: null,
        totalAssets: null,
        totalLiabilities: null,
        netAssets: null,
        operatingCashflow: null,
        evaluationStatus: 'DRAFT',
        evaluationNotes: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        evaluationPeriod: [
          { required: true, message: '请选择评价期间', trigger: 'change' }
        ],
        evaluationType: [
          { required: true, message: '请选择评价类型', trigger: 'change' }
        ],
        performanceLevel: [
          { required: true, message: '请选择绩效等级', trigger: 'change' }
        ],
        performanceScore: [
          { required: true, message: '请输入绩效评分', trigger: 'blur' }
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
        add: '新增绩效评价',
        edit: '编辑绩效评价',
        view: '查看绩效评价'
      }
      return titleMap[this.dialogType] || '绩效评价'
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
        this.performanceForm = {
          enterpriseName: '',
          evaluationPeriod: '',
          evaluationType: '',
          performanceLevel: '',
          performanceScore: 0,
          industry: '',
          revenue: null,
          netProfit: null,
          totalAssets: null,
          totalLiabilities: null,
          netAssets: null,
          operatingCashflow: null,
          evaluationStatus: 'DRAFT',
          evaluationNotes: ''
        }
      } else {
        this.performanceForm = { ...this.performanceData }
      }
    },

    handleSubmit() {
      this.$refs.performanceForm.validate((valid) => {
        if (!valid) return
        this.submitLoading = true
        const formData = {
          enterpriseName: this.performanceForm.enterpriseName,
          evaluationPeriod: this.performanceForm.evaluationPeriod,
          evaluationType: this.performanceForm.evaluationType,
          performanceLevel: this.performanceForm.performanceLevel,
          performanceScore: this.performanceForm.performanceScore,
          industry: this.performanceForm.industry,
          revenue: this.performanceForm.revenue,
          netProfit: this.performanceForm.netProfit,
          totalAssets: this.performanceForm.totalAssets,
          totalLiabilities: this.performanceForm.totalLiabilities,
          netAssets: this.performanceForm.netAssets,
          operatingCashflow: this.performanceForm.operatingCashflow,
          evaluationStatus: this.performanceForm.evaluationStatus,
          evaluationNotes: this.performanceForm.evaluationNotes
        }

        const request = this.dialogType === 'edit'
          ? updateFinancialPerformance(this.performanceForm.performanceId, formData)
          : createFinancialPerformance(formData)

        request.then(res => {
          if (res && (res.result === 200 || res.code === 1 || res.data)) {
            this.$message.success(this.dialogType === 'edit' ? '编辑成功' : '新增成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(err => {
          this.$message.error(err.message || '请求失败，请稍后重试')
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.performanceForm.resetFields()
    }
  }
}
</script>
