<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
    @open="handleOpen"
  >
    <el-form
      ref="statementForm"
      :model="form"
      :rules="formRules"
      label-width="120px"
      :disabled="submitting"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报表类型" prop="reportType">
            <el-select v-model="form.reportType" placeholder="请选择报表类型" style="width: 100%;">
              <el-option label="资产负债表" value="BALANCE_SHEET"></el-option>
              <el-option label="利润表" value="INCOME_STATEMENT"></el-option>
              <el-option label="现金流量表" value="CASH_FLOW"></el-option>
              <el-option label="所有者权益变动表" value="EQUITY_CHANGE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告期间" prop="reportPeriod">
            <el-select v-model="form.reportPeriod" placeholder="请选择报告期间" style="width: 100%;">
              <el-option label="月报" value="MONTHLY"></el-option>
              <el-option label="季报" value="QUARTERLY"></el-option>
              <el-option label="半年报" value="SEMI_ANNUAL"></el-option>
              <el-option label="年报" value="ANNUAL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告年度" prop="reportYear">
            <el-date-picker
              v-model="form.reportYear"
              type="year"
              placeholder="选择年份"
              value-format="yyyy"
              style="width: 100%;"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="企业名称">
            <el-input v-model="form.enterpriseName" placeholder="自动填充" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">财务数据（单位：元）</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总资产" prop="totalAssets">
            <el-input-number v-model="form.totalAssets" :precision="2" :min="0" :controls="false" style="width: 100%;" placeholder="请输入总资产"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总负债" prop="totalLiabilities">
            <el-input-number v-model="form.totalLiabilities" :precision="2" :min="0" :controls="false" style="width: 100%;" placeholder="请输入总负债"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="净资产" prop="netAssets">
            <el-input-number v-model="form.netAssets" :precision="2" :controls="false" style="width: 100%;" placeholder="请输入净资产"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="营业收入" prop="operatingRevenue">
            <el-input-number v-model="form.operatingRevenue" :precision="2" :min="0" :controls="false" style="width: 100%;" placeholder="请输入营业收入"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="净利润" prop="netProfit">
            <el-input-number v-model="form.netProfit" :precision="2" :controls="false" style="width: 100%;" placeholder="请输入净利润"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现金流" prop="cashFlow">
            <el-input-number v-model="form.cashFlow" :precision="2" :controls="false" style="width: 100%;" placeholder="请输入现金流"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addFinancialStatement, updateFinancialStatement } from '@/api/enterprise/financial'

export default {
  name: 'FinancialStatementDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add'
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitting: false,
      form: {
        reportType: '',
        reportPeriod: '',
        reportYear: '',
        enterpriseName: '',
        totalAssets: 0,
        totalLiabilities: 0,
        netAssets: 0,
        operatingRevenue: 0,
        netProfit: 0,
        cashFlow: 0
      },
      formRules: {
        reportType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        reportPeriod: [{ required: true, message: '请选择报告期间', trigger: 'change' }],
        reportYear: [{ required: true, message: '请选择报告年度', trigger: 'change' }],
        totalAssets: [{ required: true, message: '请输入总资产', trigger: 'blur' }],
        operatingRevenue: [{ required: true, message: '请输入营业收入', trigger: 'blur' }],
        netProfit: [{ required: true, message: '请输入净利润', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      return this.dialogType === 'edit' ? '编辑财务报表' : '新增财务报表'
    }
  },
  methods: {
    handleOpen() {
      if (this.dialogType === 'edit' && this.formData && this.formData.id) {
        // 编辑模式：回填数据
        this.form = {
          id: this.formData.id,
          reportType: this.formData.reportType || '',
          reportPeriod: this.formData.reportPeriod || '',
          reportYear: this.formData.reportYear || '',
          enterpriseName: this.formData.enterpriseName || '',
          totalAssets: this.formData.totalAssets || null,
          totalLiabilities: this.formData.totalLiabilities || null,
          netAssets: this.formData.netAssets || null,
          operatingRevenue: this.formData.operatingRevenue || null,
          netProfit: this.formData.netProfit || null,
          cashFlow: this.formData.cashFlow || null
        }
      } else {
        // 新增模式：清空表单
        this.form = {
          reportType: '',
          reportPeriod: '',
          reportYear: '',
          enterpriseName: this.enterpriseName || '',
          totalAssets: 0,
          totalLiabilities: 0,
          netAssets: 0,
          operatingRevenue: 0,
          netProfit: 0,
          cashFlow: 0
        }
      }
      this.$nextTick(() => {
        if (this.$refs.statementForm) {
          this.$refs.statementForm.clearValidate()
        }
      })
    },
    handleSubmit() {
      this.$refs.statementForm.validate(valid => {
        if (!valid) return
        this.submitting = true
        const data = {
          ...this.form,
          enterpriseId: this.enterpriseId
        }
        const api = this.dialogType === 'edit' ? updateFinancialStatement : addFinancialStatement
        api(data).then(() => {
          this.$message.success(this.dialogType === 'edit' ? '编辑成功' : '新增成功')
          this.dialogVisible = false
          this.$emit('refresh')
        }).catch(() => {
          this.$message.error(this.dialogType === 'edit' ? '编辑失败' : '新增失败')
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
