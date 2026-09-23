<template>
  <el-dialog
    :title="isEdit ? '编辑还款计划' : '新增还款计划'"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="repayment-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="还款编号" prop="repaymentNo">
            <el-input v-model="form.repaymentNo" placeholder="系统自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="融资类型" prop="financingType">
            <el-select v-model="form.financingType" placeholder="请选择融资类型" style="width: 100%;">
              <el-option label="银行贷款" value="BANK_LOAN" />
              <el-option label="债券发行" value="BOND" />
              <el-option label="融资租赁" value="LEASING" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="还款类型" prop="repaymentType">
            <el-select v-model="form.repaymentType" placeholder="请选择还款类型" style="width: 100%;">
              <el-option label="本金" value="PRINCIPAL" />
              <el-option label="利息" value="INTEREST" />
              <el-option label="本息" value="PRINCIPAL_INTEREST" />
              <el-option label="费用" value="FEE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="currencyCode">
            <el-select v-model="form.currencyCode" placeholder="请选择币种" style="width: 100%;">
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="应还金额" prop="repaymentAmount">
            <el-input-number v-model="form.repaymentAmount" :precision="2" :min="0" :controls="false" placeholder="请输入应还金额" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本金金额" prop="principalAmount">
            <el-input-number v-model="form.principalAmount" :precision="2" :min="0" :controls="false" placeholder="请输入本金金额" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="利息金额" prop="interestAmount">
            <el-input-number v-model="form.interestAmount" :precision="2" :min="0" :controls="false" placeholder="请输入利息金额" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划还款日" prop="planDate">
            <el-date-picker v-model="form.planDate" type="date" placeholder="请选择计划还款日" value-format="yyyy-MM-dd" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="还款状态" prop="repaymentStatus">
            <el-select v-model="form.repaymentStatus" placeholder="请选择还款状态" style="width: 100%;">
              <el-option label="待还款" value="PENDING" />
              <el-option label="已还款" value="COMPLETED" />
              <el-option label="逾期" value="OVERDUE" />
              <el-option label="部分还款" value="PARTIAL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="支付方式" prop="paymentMethod">
            <el-select v-model="form.paymentMethod" placeholder="请选择支付方式" style="width: 100%;">
              <el-option label="银行转账" value="BANK_TRANSFER" />
              <el-option label="现金" value="CASH" />
              <el-option label="支票" value="CHECK" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="支付账户" prop="paymentAccount">
            <el-input v-model="form.paymentAccount" placeholder="请输入支付账户" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属公司" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入所属公司" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="saving" @click="handleConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createFinancingRepayment, updateFinancingRepayment } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'RepaymentFormDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    isEdit: { type: Boolean, default: false }
  },
  data() {
    return {
      dialogVisible: false,
      saving: false,
      form: this.getDefaultForm(),
      rules: {
        financingType: [{ required: true, message: '请选择融资类型', trigger: 'change' }],
        repaymentType: [{ required: true, message: '请选择还款类型', trigger: 'change' }],
        repaymentAmount: [{ required: true, message: '请输入应还金额', trigger: 'blur' }],
        planDate: [{ required: true, message: '请选择计划还款日', trigger: 'change' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.initForm()
    }
  },
  methods: {
    getDefaultForm() {
      return { repaymentId: null, repaymentNo: '', financingType: '', repaymentType: '', repaymentAmount: null, principalAmount: null, interestAmount: null, currencyCode: 'CNY', planDate: '', repaymentStatus: 'PENDING', paymentMethod: '', paymentAccount: '', companyId: '', companyName: '', remark: '' }
    },
    initForm() {
      this.form = this.isEdit && this.formData ? { ...this.formData } : this.getDefaultForm()
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$refs.form && this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          const api = this.isEdit ? updateFinancingRepayment : createFinancingRepayment
          const res = await api(this.form)
          if (res && res.code === 1) {
            this.$message.success(this.isEdit ? '更新成功' : '创建成功')
            this.$emit('success', this.form)
            this.handleClose()
          } else {
            this.$message.error(res?.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error(e.message || '操作失败')
        } finally {
          this.saving = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.repayment-form { .el-form-item { margin-bottom: 18px; } }
.dialog-footer { text-align: right; }
</style>

