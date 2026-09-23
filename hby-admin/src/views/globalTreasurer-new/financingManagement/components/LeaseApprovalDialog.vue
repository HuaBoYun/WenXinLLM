<template>
  <el-dialog
    :title="isReject ? '拒绝租赁申请' : '审批租赁申请'"
    :visible.sync="dialogVisible"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="租赁编号">
        <span>{{ leaseData.leaseNo || leaseData.applicationNo || '-' }}</span>
      </el-form-item>
      <el-form-item label="租赁类型">
        <el-tag :type="getLeasingTypeTagType(leaseData.leasingType)">{{ getLeasingTypeText(leaseData.leasingType) }}</el-tag>
      </el-form-item>
      <el-form-item label="租赁金额">
        <span>{{ formatCurrency(leaseData.leaseAmount) }}</span>
      </el-form-item>
      <el-form-item label="审批意见" prop="comments">
        <el-input v-model="form.comments" type="textarea" :rows="4" :placeholder="isReject ? '请输入拒绝原因' : '请输入审批意见'" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="!isReject" type="primary" :loading="submitting" @click="handleApprove">通 过</el-button>
      <el-button v-if="isReject" type="danger" :loading="submitting" @click="handleReject">拒 绝</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { approveFinancialLease, rejectFinancialLease } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeaseApprovalDialog',
  props: {
    visible: { type: Boolean, default: false },
    leaseData: { type: Object, default: () => ({}) },
    isReject: { type: Boolean, default: false }
  },
  data() {
    return {
      dialogVisible: false,
      submitting: false,
      form: { comments: '' },
      rules: {
        comments: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.form.comments = ''
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$refs.form && this.$refs.form.resetFields()
    },
    async handleApprove() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        // 兼容两种字段名：后端返回 leaseId，前端可能使用 id
        const leaseId = this.leaseData.leaseId || this.leaseData.id
        if (!leaseId) {
          this.$message.error('租赁ID不能为空')
          return
        }
        this.submitting = true
        try {
          const res = await approveFinancialLease(leaseId, this.form.comments)
          if (res && (res.code === 1 || res.code === 200)) {
            this.$message.success('审批通过成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res?.msg || '审批失败')
          }
        } catch (e) {
          this.$message.error('审批失败')
        } finally {
          this.submitting = false
        }
      })
    },
    async handleReject() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        // 兼容两种字段名：后端返回 leaseId，前端可能使用 id
        const leaseId = this.leaseData.leaseId || this.leaseData.id
        if (!leaseId) {
          this.$message.error('租赁ID不能为空')
          return
        }
        this.submitting = true
        try {
          const res = await rejectFinancialLease(leaseId, this.form.comments)
          if (res && (res.code === 1 || res.code === 200)) {
            this.$message.success('已拒绝该申请')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res?.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitting = false
        }
      })
    },
    getLeasingTypeTagType(type) {
      const map = { 'FINANCE_LEASE': 'primary', 'OPERATING_LEASE': 'success', 'SALE_LEASEBACK': 'warning' }
      return map[type] || 'default'
    },
    getLeasingTypeText(type) {
      const map = { 'FINANCE_LEASE': '融资租赁', 'OPERATING_LEASE': '经营租赁', 'SALE_LEASEBACK': '售后回租' }
      return map[type] || type || '-'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '-'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY', minimumFractionDigits: 2 }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer { text-align: right; }
</style>

