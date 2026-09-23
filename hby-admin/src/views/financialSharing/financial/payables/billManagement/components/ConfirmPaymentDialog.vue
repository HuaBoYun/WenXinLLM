<template>
  <el-dialog
    title="确认兑付"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <!-- 兑付信息展示 -->
    <el-alert
      title="兑付信息"
      type="info"
      :closable="false"
      style="margin-bottom: 20px"
    >
      <div style="font-size: 14px">
        <div>兑付编号：{{ paymentData.paymentNo || '-' }}</div>
        <div>兑付金额：<span class="amount-text">{{ formatAmount(paymentData.paymentAmount) }}</span> 元</div>
        <div>兑付日期：{{ paymentData.paymentDate || '-' }}</div>
      </div>
    </el-alert>

    <!-- 确认表单 -->
    <el-form :model="form" :rules="rules" ref="confirmForm" label-width="120px">
      <el-form-item label="实际到账金额" prop="actualAmount">
        <el-input-number
          v-model="form.actualAmount"
          :precision="2"
          :min="0"
          :max="paymentData.paymentAmount || 999999999"
          style="width: 100%"
        />
        <div style="color: #909399; font-size: 12px; margin-top: 4px">
          兑付金额：{{ formatAmount(paymentData.paymentAmount) }} 元
        </div>
      </el-form-item>

      <el-form-item label="确认意见" prop="comments">
        <el-input
          v-model="form.comments"
          type="textarea"
          :rows="4"
          placeholder="请输入确认意见"
          :maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'ConfirmPaymentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    paymentData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        actualAmount: 0,
        comments: ''
      },
      rules: {
        actualAmount: [
          { required: true, message: '请输入实际到账金额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '实际到账金额必须大于0', trigger: 'blur' }
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
      // 默认实际到账金额等于兑付金额
      this.form.actualAmount = this.paymentData.paymentAmount || 0
      this.form.comments = ''
      if (this.$refs.confirmForm) {
        this.$refs.confirmForm.resetFields()
      }
    },
    handleClose() {
      this.initForm()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.confirmForm.validate(async valid => {
        if (!valid) return

        // 验证实际到账金额不能超过兑付金额
        if (this.form.actualAmount > this.paymentData.paymentAmount) {
          this.$message.warning('实际到账金额不能超过兑付金额')
          return
        }

        this.loading = true
        try {
          await payablesApi.confirmPayment(
            this.paymentData.paymentId,
            this.form.actualAmount
          )
          this.$message.success('确认兑付成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('确认失败：' + (error.message || '未知错误'))
        } finally {
          this.loading = false
        }
      })
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.amount-text {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
</style>
