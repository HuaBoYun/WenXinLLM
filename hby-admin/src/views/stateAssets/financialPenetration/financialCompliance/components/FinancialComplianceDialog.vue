<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <el-form
      ref="complianceForm"
      :model="complianceForm"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="complianceForm.enterpriseName" :disabled="disabled" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查类型" prop="checkType">
            <el-select v-model="complianceForm.checkType" :disabled="disabled" placeholder="请选择检查类型" style="width: 100%;">
              <el-option label="制度执行检查" value="SYSTEM_EXECUTION"></el-option>
              <el-option label="流程合规检查" value="PROCESS_COMPLIANCE"></el-option>
              <el-option label="违规行为检查" value="VIOLATION_CHECK"></el-option>
              <el-option label="整改措施检查" value="RECTIFICATION_CHECK"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查期间" prop="checkPeriod">
            <el-date-picker
              v-model="complianceForm.checkPeriod"
              :disabled="disabled"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合规状态" prop="complianceStatus">
            <el-select v-model="complianceForm.complianceStatus" :disabled="disabled" placeholder="请选择合规状态" style="width: 100%;">
              <el-option label="合规" value="COMPLIANT"></el-option>
              <el-option label="基本合规" value="BASICALLY_COMPLIANT"></el-option>
              <el-option label="不合规" value="NON_COMPLIANT"></el-option>
              <el-option label="待检查" value="PENDING"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="complianceForm.riskLevel" :disabled="disabled" placeholder="请选择风险等级" style="width: 100%;">
              <el-option label="低风险" value="LOW"></el-option>
              <el-option label="中风险" value="MEDIUM"></el-option>
              <el-option label="高风险" value="HIGH"></el-option>
              <el-option label="极高风险" value="CRITICAL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合规评分" prop="complianceScore">
            <el-input-number
              v-model="complianceForm.complianceScore"
              :disabled="disabled"
              :min="0"
              :max="100"
              placeholder="请输入合规评分"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="检查范围" prop="checkScope">
        <el-checkbox-group v-model="complianceForm.checkScope" :disabled="disabled">
          <el-checkbox label="FINANCIAL_SYSTEM">财务制度</el-checkbox>
          <el-checkbox label="INTERNAL_CONTROL">内控制度</el-checkbox>
          <el-checkbox label="BUDGET_MANAGEMENT">预算管理</el-checkbox>
          <el-checkbox label="FUND_MANAGEMENT">资金管理</el-checkbox>
          <el-checkbox label="ASSET_MANAGEMENT">资产管理</el-checkbox>
          <el-checkbox label="INVESTMENT_MANAGEMENT">投资管理</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="检查方式" prop="checkMethod">
        <el-radio-group v-model="complianceForm.checkMethod" :disabled="disabled">
          <el-radio label="ON_SITE">现场检查</el-radio>
          <el-radio label="REMOTE">远程检查</el-radio>
          <el-radio label="DOCUMENT">文档审查</el-radio>
          <el-radio label="SYSTEM">系统检查</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查人员" prop="inspector">
            <el-input v-model="complianceForm.inspector" :disabled="disabled" placeholder="请输入检查人员" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查日期" prop="checkDate">
            <el-date-picker
              v-model="complianceForm.checkDate"
              :disabled="disabled"
              type="date"
              placeholder="选择检查日期"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="发现问题" prop="issues">
        <el-input
          v-model="complianceForm.issues"
          :disabled="disabled"
          type="textarea"
          :rows="3"
          placeholder="请描述发现的问题"
        />
      </el-form-item>

      <el-form-item label="整改要求" prop="rectificationRequirements">
        <el-input
          v-model="complianceForm.rectificationRequirements"
          :disabled="disabled"
          type="textarea"
          :rows="3"
          placeholder="请输入整改要求"
        />
      </el-form-item>

      <el-form-item label="整改期限" prop="rectificationDeadline">
        <el-date-picker
          v-model="complianceForm.rectificationDeadline"
          :disabled="disabled"
          type="date"
          placeholder="选择整改期限"
          style="width: 100%;"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!disabled" type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  createFinancialCompliance,
  updateFinancialCompliance,
  getFinancialComplianceDetail
} from '@/api/stateAssets/financialCompliance'

export default {
  name: 'FinancialComplianceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    complianceData: {
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
      complianceForm: {
        enterpriseName: '',
        checkType: '',
        checkPeriod: [],
        complianceStatus: '',
        riskLevel: '',
        complianceScore: 0,
        checkScope: [],
        checkMethod: '',
        inspector: '',
        checkDate: '',
        issues: '',
        rectificationRequirements: '',
        rectificationDeadline: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        checkType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ],
        checkPeriod: [
          { required: true, message: '请选择检查期间', trigger: 'change' }
        ],
        complianceStatus: [
          { required: true, message: '请选择合规状态', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ],
        complianceScore: [
          { required: true, message: '请输入合规评分', trigger: 'blur' }
        ],
        checkScope: [
          { required: true, message: '请选择检查范围', trigger: 'change' }
        ],
        checkMethod: [
          { required: true, message: '请选择检查方式', trigger: 'change' }
        ],
        inspector: [
          { required: true, message: '请输入检查人员', trigger: 'blur' }
        ],
        checkDate: [
          { required: true, message: '请选择检查日期', trigger: 'change' }
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
        add: '新增合规检查',
        edit: '编辑合规检查',
        view: '查看合规检查'
      }
      return titleMap[this.dialogType] || '合规检查'
    },
    disabled() {
      return this.dialogType === 'view'
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
        this.complianceForm = {
          enterpriseName: '',
          checkType: '',
          checkPeriod: [],
          complianceStatus: '',
          riskLevel: '',
          complianceScore: 0,
          checkScope: [],
          checkMethod: '',
          inspector: '',
          checkDate: '',
          issues: '',
          rectificationRequirements: '',
          rectificationDeadline: ''
        }
      } else {
        this.loadComplianceData()
      }
    },

    /** 加载已有数据（编辑/查看模式） */
    loadComplianceData() {
      const data = { ...this.complianceData }
      // 将后端返回的 checkScope JSON字符串解析为数组
      if (data.checkScope && typeof data.checkScope === 'string') {
        try {
          data.checkScope = JSON.parse(data.checkScope)
        } catch (e) {
          data.checkScope = []
        }
      }
      if (!Array.isArray(data.checkScope)) {
        data.checkScope = []
      }
      // 将 checkDateStart/checkDateEnd 映射为 checkPeriod 数组
      if (data.checkDateStart || data.checkDateEnd) {
        data.checkPeriod = [data.checkDateStart || '', data.checkDateEnd || '']
      } else if (!data.checkPeriod) {
        data.checkPeriod = []
      }
      // 确保日期字段可被 el-date-picker 识别
      if (data.checkDate && typeof data.checkDate === 'string') {
        data.checkDate = data.checkDate.substring(0, 10)
      }
      if (data.rectificationDeadline && typeof data.rectificationDeadline === 'string') {
        data.rectificationDeadline = data.rectificationDeadline.substring(0, 10)
      }
      this.complianceForm = {
        enterpriseName: data.enterpriseName || '',
        checkType: data.checkType || '',
        checkPeriod: data.checkPeriod || [],
        complianceStatus: data.complianceStatus || '',
        riskLevel: data.riskLevel || '',
        complianceScore: data.complianceScore || 0,
        checkScope: data.checkScope || [],
        checkMethod: data.checkMethod || '',
        inspector: data.inspector || '',
        checkDate: data.checkDate || '',
        issues: data.issues || '',
        rectificationRequirements: data.rectificationRequirements || '',
        rectificationDeadline: data.rectificationDeadline || ''
      }
    },

    handleSubmit() {
      this.$refs.complianceForm.validate(async(valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          // 构造提交数据
          const submitData = { ...this.complianceForm }
          // checkScope 数组转为 JSON 字符串
          if (Array.isArray(submitData.checkScope)) {
            submitData.checkScope = JSON.stringify(submitData.checkScope)
          }
          // checkPeriod 拆分为 checkDateStart/checkDateEnd
          if (Array.isArray(submitData.checkPeriod) && submitData.checkPeriod.length === 2) {
            submitData.checkDateStart = submitData.checkPeriod[0]
            submitData.checkDateEnd = submitData.checkPeriod[1]
          }
          delete submitData.checkPeriod

          if (this.dialogType === 'add') {
            await createFinancialCompliance(submitData)
          } else if (this.dialogType === 'edit') {
            const id = this.complianceData.complianceId || this.complianceData.id
            await updateFinancialCompliance(id, submitData)
          }
          this.$message.success('操作成功')
          this.$emit('refresh')
          this.handleClose()
        } catch (error) {
          this.$message.error(error.message || '操作失败，请重试')
        } finally {
          this.submitLoading = false
        }
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.complianceForm.resetFields()
    }
  }
}
</script>
