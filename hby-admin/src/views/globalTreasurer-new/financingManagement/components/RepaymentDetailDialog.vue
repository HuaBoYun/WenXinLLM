<template>
  <el-dialog title="还款详情" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false" @close="handleClose">
    <el-descriptions :column="2" border class="detail-descriptions">
      <el-descriptions-item label="还款编号">{{ detail.repaymentNo || '-' }}</el-descriptions-item>
      <el-descriptions-item label="融资类型">
        <el-tag :type="getFinancingTypeTagType(detail.financingType)" size="small">{{ getFinancingTypeText(detail.financingType) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="还款类型">
        <el-tag :type="getRepaymentTypeTagType(detail.repaymentType)" size="small">{{ getRepaymentTypeText(detail.repaymentType) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="还款状态">
        <el-tag :type="getRepaymentStatusTagType(detail.repaymentStatus)" size="small">{{ getRepaymentStatusText(detail.repaymentStatus) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="应还金额">{{ formatCurrency(detail.repaymentAmount) }}</el-descriptions-item>
      <el-descriptions-item label="已还金额">{{ formatCurrency(detail.actualAmount) }}</el-descriptions-item>
      <el-descriptions-item label="本金金额">{{ formatCurrency(detail.principalAmount) }}</el-descriptions-item>
      <el-descriptions-item label="利息金额">{{ formatCurrency(detail.interestAmount) }}</el-descriptions-item>
      <el-descriptions-item label="罚息金额">{{ formatCurrency(detail.penaltyAmount) }}</el-descriptions-item>
      <el-descriptions-item label="币种">{{ detail.currencyCode || 'CNY' }}</el-descriptions-item>
      <el-descriptions-item label="计划还款日">{{ detail.planDate || '-' }}</el-descriptions-item>
      <el-descriptions-item label="实际还款日">{{ detail.actualRepaymentDate || '-' }}</el-descriptions-item>
      <el-descriptions-item label="支付方式">{{ getPaymentMethodText(detail.paymentMethod) }}</el-descriptions-item>
      <el-descriptions-item label="支付账户">{{ detail.paymentAccount || '-' }}</el-descriptions-item>
      <el-descriptions-item label="所属公司" :span="2">{{ detail.companyName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ detail.createdTime || '-' }}</el-descriptions-item>
      <el-descriptions-item label="更新时间">{{ detail.updatedTime || '-' }}</el-descriptions-item>
    </el-descriptions>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="detail.repaymentStatus === 'PENDING'" type="primary" @click="handleExecute">执行还款</el-button>
      <el-button v-if="detail.repaymentStatus === 'PENDING'" type="warning" @click="handlePartial">部分还款</el-button>
      <el-button v-if="detail.repaymentStatus === 'PENDING' || detail.repaymentStatus === 'OVERDUE'" type="info" @click="handleExtension">展期申请</el-button>
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getFinancingRepaymentDetail } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'RepaymentDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    repaymentId: { type: [String, Number], default: null }
  },
  data() {
    return { dialogVisible: false, loading: false, detail: {} }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.repaymentId) this.fetchDetail()
    }
  },
  methods: {
    async fetchDetail() {
      this.loading = true
      try {
        const res = await getFinancingRepaymentDetail(this.repaymentId)
        if (res && res.code === 1) {
          this.detail = res.data || {}
        } else {
          this.$message.error(res?.msg || '获取详情失败')
        }
      } catch (e) {
        this.$message.error('获取详情失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleExecute() { this.$emit('execute', this.detail) },
    handlePartial() { this.$emit('partial', this.detail) },
    handleExtension() { this.$emit('extension', this.detail) },
    getFinancingTypeTagType(type) {
      return { 'BANK_LOAN': 'primary', 'BOND': 'success', 'LEASING': 'warning' }[type] || 'default'
    },
    getFinancingTypeText(type) {
      return { 'BANK_LOAN': '银行贷款', 'BOND': '债券发行', 'LEASING': '融资租赁' }[type] || type || '-'
    },
    getRepaymentTypeTagType(type) {
      return { 'PRINCIPAL': 'primary', 'INTEREST': 'success', 'PRINCIPAL_INTEREST': 'warning', 'FEE': 'info' }[type] || 'default'
    },
    getRepaymentTypeText(type) {
      return { 'PRINCIPAL': '本金', 'INTEREST': '利息', 'PRINCIPAL_INTEREST': '本息', 'FEE': '费用' }[type] || type || '-'
    },
    getRepaymentStatusTagType(status) {
      return { 'PENDING': 'warning', 'COMPLETED': 'success', 'OVERDUE': 'danger', 'PARTIAL': 'info' }[status] || 'default'
    },
    getRepaymentStatusText(status) {
      return { 'PENDING': '待还款', 'COMPLETED': '已还款', 'OVERDUE': '逾期', 'PARTIAL': '部分还款' }[status] || status || '-'
    },
    getPaymentMethodText(method) {
      return { 'BANK_TRANSFER': '银行转账', 'CASH': '现金', 'CHECK': '支票' }[method] || method || '-'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY', minimumFractionDigits: 2 }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-descriptions { margin-bottom: 20px; }
.dialog-footer { text-align: right; }
</style>

