<template>
  <el-dialog title="展期申请" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="extension-form">
      <el-form-item label="还款编号">
        <el-input :value="repaymentData.repaymentNo" disabled />
      </el-form-item>
      <el-form-item label="原还款日期">
        <el-input :value="repaymentData.planDate" disabled />
      </el-form-item>
      <el-form-item label="应还金额">
        <el-input :value="formatCurrency(repaymentData.repaymentAmount)" disabled />
      </el-form-item>
      <el-form-item label="展期天数" prop="extensionDays">
        <el-input-number v-model="form.extensionDays" :min="1" :max="365" placeholder="请输入展期天数" style="width: 100%;" @change="calculateNewDate" />
      </el-form-item>
      <el-form-item label="新还款日期">
        <el-input :value="newPlanDate" disabled />
      </el-form-item>
      <el-form-item label="展期原因" prop="reason">
        <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入展期原因" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="saving" @click="handleConfirm">提交申请</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { applyExtension } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'ExtensionApplyDialog',
  props: {
    visible: { type: Boolean, default: false },
    repaymentData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      saving: false,
      newPlanDate: '',
      form: { extensionDays: 30, reason: '' },
      rules: {
        extensionDays: [{ required: true, message: '请输入展期天数', trigger: 'blur' }],
        reason: [{ required: true, message: '请输入展期原因', trigger: 'blur' }, { min: 10, max: 500, message: '展期原因长度在10到500个字符', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.form = { extensionDays: 30, reason: '' }
        this.calculateNewDate()
        this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
      }
    }
  },
  methods: {
    calculateNewDate() {
      if (!this.repaymentData.planDate) { this.newPlanDate = '-'; return }
      const date = new Date(this.repaymentData.planDate)
      date.setDate(date.getDate() + (this.form.extensionDays || 0))
      this.newPlanDate = date.toISOString().slice(0, 10)
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
          // 后端期望 newPlanDate 而不是 extensionDays
          const res = await applyExtension({
            repaymentId: this.repaymentData.repaymentId,
            newPlanDate: this.newPlanDate,
            reason: this.form.reason
          })
          if (res && res.code === 1) {
            this.$message.success('展期申请提交成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res?.msg || '展期申请失败')
          }
        } catch (e) {
          this.$message.error(e.message || '展期申请失败')
        } finally {
          this.saving = false
        }
      })
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY', minimumFractionDigits: 2 }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.extension-form { .el-form-item { margin-bottom: 18px; } }
.dialog-footer { text-align: right; }
</style>

