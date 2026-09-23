<template>
  <el-dialog
    title="兑付详情"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <div v-if="paymentData.paymentId">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="兑付编号">{{ paymentData.paymentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="票据号">{{ paymentData.billNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="兑付金额">
          <span class="amount-text">{{ formatAmount(paymentData.paymentAmount) }}</span> 元
        </el-descriptions-item>
        <el-descriptions-item label="实际到账金额">
          <span class="amount-text">{{ formatAmount(paymentData.actualAmount) }}</span> 元
        </el-descriptions-item>
        <el-descriptions-item label="手续费">{{ formatAmount(paymentData.fee) }} 元</el-descriptions-item>
        <el-descriptions-item label="兑付日期">{{ paymentData.paymentDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="兑付银行" :span="2">
          {{ paymentData.bankName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="银行账号" :span="2">
          {{ paymentData.bankAccount || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="兑付方式">
          {{ getPaymentTypeText(paymentData.paymentType) }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(paymentData.status)">
            {{ paymentData.statusName || getStatusText(paymentData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="确认人" v-if="paymentData.confirmBy">
          {{ paymentData.confirmBy }}
        </el-descriptions-item>
        <el-descriptions-item label="确认时间" v-if="paymentData.confirmTime">
          {{ formatDateTime(paymentData.confirmTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="兑付说明" :span="2">
          {{ paymentData.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ paymentData.remarks || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(paymentData.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ paymentData.createBy || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PaymentDetailDialog',
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
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$emit('close')
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    getPaymentTypeText(type) {
      const typeMap = {
        1: '现金',
        2: '转账'
      }
      return typeMap[type] || '-'
    },
    getStatusType(status) {
      const statusMap = {
        processing: 'warning',
        confirmed: 'success',
        failed: 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        processing: '处理中',
        confirmed: '已确认',
        failed: '失败'
      }
      return statusMap[status] || status
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
