<template>
  <el-dialog
    title="租赁申请详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-descriptions :column="2" border class="lease-detail">
      <el-descriptions-item label="租赁编号">{{ detail.leaseNo || '-' }}</el-descriptions-item>
      <el-descriptions-item label="租赁名称">{{ detail.leaseName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="租赁类型">
        <el-tag :type="getLeasingTypeTagType(detail.leaseType || detail.leasingType)">{{ getLeasingTypeText(detail.leaseType || detail.leasingType) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="租赁公司">{{ detail.lessorName || detail.leasingCompany || '-' }}</el-descriptions-item>
      <el-descriptions-item label="租赁金额">{{ formatCurrency(detail.leaseAmount) }}</el-descriptions-item>
      <el-descriptions-item label="未偿还金额">{{ formatCurrency(detail.outstandingAmount) }}</el-descriptions-item>
      <el-descriptions-item label="币种">{{ getCurrencyText(detail.currencyCode) }}</el-descriptions-item>
      <el-descriptions-item label="利率">{{ formatInterestRate(detail.interestRate, detail.leaseRate) }}</el-descriptions-item>
      <el-descriptions-item label="开始日期">{{ formatDate(detail.startDate) }}</el-descriptions-item>
      <el-descriptions-item label="结束日期">{{ formatDate(detail.endDate) }}</el-descriptions-item>
      <el-descriptions-item label="承租公司">{{ detail.companyName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="租赁状态">
        <el-tag :type="getStatusTagType(detail.leaseStatus || detail.applicationStatus)">{{ getStatusText(detail.leaseStatus || detail.applicationStatus) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建人">{{ detail.createdBy || '-' }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ formatDate(detail.createdTime) }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
    </el-descriptions>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button v-if="(detail.leaseStatus || detail.applicationStatus) === 'PENDING'" type="primary" @click="handleEdit">编 辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getFinancialLeaseDetail } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeaseDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    leaseId: { type: [String, Number], default: null }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detail: {}
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.leaseId) this.loadDetail()
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true
      try {
        const res = await getFinancialLeaseDetail(this.leaseId)
        if (res && (res.code === 1 || res.code === 200)) {
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
      this.detail = {}
    },
    handleEdit() {
      this.$emit('edit', this.detail)
      this.handleClose()
    },
    getLeasingTypeTagType(type) {
      const map = {
        'DIRECT': 'primary',
        'LEASEBACK': 'warning',
        'LEVERAGED': 'success',
        'OPERATING': 'info',
        'FINANCE_LEASE': 'primary',
        'OPERATING_LEASE': 'success',
        'SALE_LEASEBACK': 'warning'
      }
      return map[type] || 'default'
    },
    getLeasingTypeText(type) {
      const map = {
        'DIRECT': '直接租赁',
        'LEASEBACK': '售后回租',
        'LEVERAGED': '杠杆租赁',
        'OPERATING': '经营租赁',
        'FINANCE_LEASE': '融资租赁',
        'OPERATING_LEASE': '经营租赁',
        'SALE_LEASEBACK': '售后回租'
      }
      return map[type] || type || '-'
    },
    getStatusTagType(status) {
      const map = { 'PENDING': 'info', 'SUBMITTED': 'warning', 'APPROVED': 'success', 'REJECTED': 'danger', 'ACTIVE': 'primary', 'COMPLETED': 'success' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 'PENDING': '待提交', 'SUBMITTED': '已提交', 'APPROVED': '已审批', 'REJECTED': '已拒绝', 'ACTIVE': '执行中', 'COMPLETED': '已完成' }
      return map[status] || status || '-'
    },
    getCurrencyText(code) {
      const map = { 'CNY': '人民币', 'USD': '美元', 'EUR': '欧元' }
      return map[code] || code || '-'
    },
    formatInterestRate(interestRate, leaseRate) {
      // interestRate 是小数（如 0.055），leaseRate 是百分比（如 5.5）
      if (leaseRate != null) return leaseRate + '%'
      if (interestRate != null) return (interestRate * 100).toFixed(2) + '%'
      return '-'
    },
    formatDate(dateValue) {
      if (!dateValue) return '-'
      // 如果是时间戳
      if (typeof dateValue === 'number') {
        const date = new Date(dateValue)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      }
      // 如果已经是字符串
      return dateValue
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '-'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY', minimumFractionDigits: 2 }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.lease-detail { margin-bottom: 20px; }
.dialog-footer { text-align: right; }
</style>
