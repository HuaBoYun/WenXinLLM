<template>
  <el-dialog
    :title="isEdit ? '编辑预算' : '新增预算'"
    :visible.sync="dialogVisible"
    width="650px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="预算名称" prop="budgetName">
        <el-input v-model="form.budgetName" placeholder="请输入预算名称"></el-input>
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算类型" prop="budgetType">
            <el-select v-model="form.budgetType" placeholder="请选择" style="width: 100%;">
              <el-option label="年度预算" value="年度预算"></el-option>
              <el-option label="季度预算" value="季度预算"></el-option>
              <el-option label="月度预算" value="月度预算"></el-option>
              <el-option label="专项预算" value="专项预算"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算年度">
            <el-input-number v-model="form.budgetYear" :min="2020" :max="2030" style="width: 100%;"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算期间">
            <el-input v-model="form.budgetPeriod" placeholder="如：2025-Q2"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算总额" prop="totalBudgetAmount">
            <el-input-number v-model="form.totalBudgetAmount" :precision="2" :min="0" :controls="false" style="width: 100%;" placeholder="单位：元"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责部门" prop="budgetDepartment">
            <el-input v-model="form.budgetDepartment" placeholder="请输入负责部门"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="budgetManager">
            <el-input v-model="form.budgetManager" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="预算描述">
        <el-input v-model="form.budgetDescription" type="textarea" :rows="3" placeholder="请输入预算描述"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

const JSON_HEADERS = { 'Content-Type': 'application/json;charset=UTF-8' }

export default {
  name: 'BudgetDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' },
    enterpriseId: { type: String, default: '' }
  },
  data() {
    return {
      loading: false,
      form: {
        budgetName: '',
        budgetType: '',
        budgetYear: new Date().getFullYear(),
        budgetPeriod: '',
        totalBudgetAmount: 0,
        budgetDepartment: '',
        budgetManager: '',
        budgetDescription: ''
      },
      rules: {
        budgetName: [{ required: true, message: '请输入预算名称', trigger: 'blur' }],
        budgetType: [{ required: true, message: '请选择预算类型', trigger: 'change' }],
        totalBudgetAmount: [{ required: true, message: '请输入预算总额', trigger: 'blur' }],
        budgetDepartment: [{ required: true, message: '请输入负责部门', trigger: 'blur' }],
        budgetManager: [{ required: true, message: '请输入负责人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    isEdit() { return this.dialogType === 'edit' && this.formData && this.formData.budgetId }
  },
  watch: {
    visible(val) {
      if (val) this.initForm()
    }
  },
  methods: {
    initForm() {
      if (this.isEdit) {
        this.form = {
          budgetId: this.formData.budgetId,
          budgetName: this.formData.budgetName || '',
          budgetType: this.formData.budgetType || '',
          budgetYear: this.formData.budgetYear || new Date().getFullYear(),
          budgetPeriod: this.formData.budgetPeriod || '',
          totalBudgetAmount: this.formData.totalBudgetAmount || this.formData.budgetAmount || 0,
          budgetDepartment: this.formData.budgetDepartment || '',
          budgetManager: this.formData.budgetManager || this.formData.createPerson || '',
          budgetDescription: this.formData.budgetDescription || ''
        }
      } else {
        this.form = {
          budgetName: '',
          budgetType: '',
          budgetYear: new Date().getFullYear(),
          budgetPeriod: '',
          totalBudgetAmount: 0,
          budgetDepartment: '',
          budgetManager: '',
          budgetDescription: ''
        }
      }
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            ...this.form,
            enterpriseId: this.enterpriseId,
            budgetStatus: this.isEdit ? undefined : 'DRAFTING',
            approvalStatus: this.isEdit ? undefined : '未提交',
            executedAmount: 0,
            executionRate: 0,
            remainingBudget: this.form.totalBudgetAmount,
            createBy: 'admin',
            updateBy: 'admin'
          }
          if (this.isEdit) {
            await request({ url: '/monitor/v1/enterprise/financial/budget/update', method: 'put', headers: JSON_HEADERS, data })
            this.$message.success('编辑成功')
          } else {
            await request({ url: '/monitor/v1/enterprise/financial/budget/add', method: 'post', headers: JSON_HEADERS, data })
            this.$message.success('新增成功')
          }
          this.$emit('refresh')
          this.handleClose()
        } catch (error) {
          this.$message.error(this.isEdit ? '编辑失败' : '新增失败')
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
