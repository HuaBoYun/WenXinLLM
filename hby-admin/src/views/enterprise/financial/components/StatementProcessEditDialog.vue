<template>
  <el-dialog
    :title="processData && processData.processId ? '编辑编制流程' : '新增编制流程'"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    @open="handleOpen"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报表名称" prop="statementName">
            <el-input v-model="form.statementName" placeholder="请输入报表名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报表类型" prop="statementType">
            <el-select v-model="form.statementType" placeholder="请选择报表类型" style="width: 100%;">
              <el-option label="资产负债表" value="BALANCE_SHEET"></el-option>
              <el-option label="利润表" value="INCOME_STATEMENT"></el-option>
              <el-option label="现金流量表" value="CASH_FLOW"></el-option>
              <el-option label="所有者权益变动表" value="EQUITY_CHANGE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告期间" prop="reportingPeriod">
            <el-input v-model="form.reportingPeriod" placeholder="如：2025-Q2"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制负责人" prop="compilationManager">
            <el-input v-model="form.compilationManager" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="编制部门" prop="compilationDepartment">
            <el-input v-model="form.compilationDepartment" placeholder="请输入编制部门"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参与人员">
            <el-input v-model="form.participants" placeholder="多人用逗号分隔"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计划开始时间" prop="plannedStartTime">
            <el-date-picker v-model="form.plannedStartTime" type="datetime" placeholder="选择开始时间" style="width: 100%;" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划完成时间" prop="plannedEndTime">
            <el-date-picker v-model="form.plannedEndTime" type="datetime" placeholder="选择完成时间" style="width: 100%;" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="数据来源">
        <el-input v-model="form.dataSources" placeholder="如：ERP系统、财务系统"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remarks" type="textarea" :rows="3" placeholder="请输入备注"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addFinancialStatementProcess, updateFinancialStatementProcess } from '@/api/enterprise/financialStatementProcess'

export default {
  name: 'StatementProcessEditDialog',
  props: {
    visible: { type: Boolean, default: false },
    processData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      form: {
        statementName: '',
        statementType: '',
        reportingPeriod: '',
        compilationManager: '',
        compilationDepartment: '',
        participants: '',
        plannedStartTime: '',
        plannedEndTime: '',
        dataSources: '',
        remarks: ''
      },
      rules: {
        statementName: [{ required: true, message: '请输入报表名称', trigger: 'blur' }],
        statementType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        reportingPeriod: [{ required: true, message: '请输入报告期间', trigger: 'blur' }],
        compilationManager: [{ required: true, message: '请输入编制负责人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    isEdit() {
      return this.processData && this.processData.processId
    }
  },
  methods: {
    handleOpen() {
      if (this.isEdit) {
        this.form = {
          processId: this.processData.processId,
          statementName: this.processData.statementName || '',
          statementType: this.processData.statementType || '',
          reportingPeriod: this.processData.reportingPeriod || '',
          compilationManager: this.processData.compilationManager || '',
          compilationDepartment: this.processData.compilationDepartment || '',
          participants: this.processData.participants || '',
          plannedStartTime: this.processData.plannedStartTime || '',
          plannedEndTime: this.processData.plannedEndTime || '',
          dataSources: this.processData.dataSources || '',
          remarks: this.processData.remarks || ''
        }
      } else {
        this.form = {
          statementName: '',
          statementType: '',
          reportingPeriod: '',
          compilationManager: '',
          compilationDepartment: '',
          participants: '',
          plannedStartTime: '',
          plannedEndTime: '',
          dataSources: '',
          remarks: ''
        }
      }
      this.$nextTick(() => {
        if (this.$refs.form) this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            ...this.form,
            enterpriseId: this.processData.enterpriseId || '',
            compilationStatus: this.isEdit ? this.processData.compilationStatus : '待开始',
            auditStatus: this.isEdit ? this.processData.auditStatus : '未提交',
            compilationProgress: this.isEdit ? this.processData.compilationProgress : 0,
            createBy: 'admin',
            updateBy: 'admin'
          }
          if (this.isEdit) {
            await updateFinancialStatementProcess(data)
            this.$message.success('编辑成功')
          } else {
            await addFinancialStatementProcess(data)
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
