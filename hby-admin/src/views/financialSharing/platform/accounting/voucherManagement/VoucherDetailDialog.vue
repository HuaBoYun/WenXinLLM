<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="凭证详情"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="handleClose"
    class="voucher-detail-dialog"
  >
    <div v-loading="loading" class="voucher-detail-container">
      <!-- 凭证头部信息 -->
      <div class="voucher-header">
        <div class="header-left">
          <el-tag :type="getStatusType(voucherData.voucherStatus)" size="medium">
            {{ voucherData.statusName || getStatusName(voucherData.voucherStatus) }}
          </el-tag>
          <el-tag :type="getVoucherTypeColor(voucherData.voucherTypeId)" size="medium" style="margin-left: 10px;">
            {{ getVoucherTypeName(voucherData.voucherTypeId) }}
          </el-tag>
        </div>
        <div class="header-right">
          <span>日期：{{ voucherData.voucherDate }}</span>
          <span style="margin-left: 20px">会计期间：{{ voucherData.accountingPeriod || '-' }}</span>
          <span style="margin-left: 20px">凭证号：{{ voucherData.voucherNo }}</span>
        </div>
      </div>

      <!-- 凭证摘要 -->
      <div class="voucher-summary" v-if="voucherData.voucherDesc">
        <span class="label">摘要：</span>
        <span class="value">{{ voucherData.voucherDesc }}</span>
      </div>

      <!-- 分录明细表格 -->
      <el-table :data="voucherData.entries || []" border stripe class="entries-table">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column label="摘要" prop="summary" min-width="180" show-overflow-tooltip />
        <el-table-column label="科目代码" prop="accountCode" width="120" align="center" />
        <el-table-column label="科目名称" prop="accountName" width="150" />
        <el-table-column label="借方金额" prop="debitAmount" width="140" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.debitAmount" class="amount debit">
              {{ formatAmount(scope.row.debitAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="贷方金额" prop="creditAmount" width="140" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.creditAmount" class="amount credit">
              {{ formatAmount(scope.row.creditAmount) }}
            </span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 金额合计 -->
      <div class="amount-total">
        <div class="total-row">
          <span class="label">借方合计：</span>
          <span class="amount debit">¥{{ formatAmount(voucherData.totalDebit || totalDebit) }}</span>
        </div>
        <div class="total-row">
          <span class="label">贷方合计：</span>
          <span class="amount credit">¥{{ formatAmount(voucherData.totalCredit || totalCredit) }}</span>
        </div>
        <div class="balance-status" :class="{ balanced: isBalanced }">
          {{ isBalanced ? '✓ 借贷平衡' : '✗ 借贷不平衡' }}
        </div>
      </div>

      <!-- 签章信息 -->
      <div class="signature-info">
        <span>制单人：{{ voucherData.preparerName || voucherData.creatorName || '-' }}</span>
        <span>审核人：{{ voucherData.reviewerName || '-' }}</span>
        <span>过账人：{{ voucherData.posterName || '-' }}</span>
      </div>

      <!-- 时间信息 -->
      <div class="time-info">
        <span>创建时间：{{ formatDateTime(voucherData.createTime) }}</span>
        <span v-if="voucherData.auditTime">审核时间：{{ formatDateTime(voucherData.auditTime) }}</span>
        <span v-if="voucherData.updateTime">更新时间：{{ formatDateTime(voucherData.updateTime) }}</span>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" icon="el-icon-printer" @click="handlePrint">打 印</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getVoucherById } from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherDetailDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      voucherData: {
        voucherId: '',
        voucherNo: '',
        voucherType: '',
        voucherDate: '',
        summary: '',
        attachmentCount: 0,
        status: '',
        entries: [],
        creator: '',
        auditor: '',
        bookkeeper: '',
        financialManager: '',
        createTime: '',
        auditTime: '',
        postTime: ''
      }
    }
  },

  computed: {
    totalDebit() {
      return (this.voucherData.entries || []).reduce((sum, entry) => sum + (entry.debitAmount || 0), 0)
    },
    totalCredit() {
      return (this.voucherData.entries || []).reduce((sum, entry) => sum + (entry.creditAmount || 0), 0)
    },
    isBalanced() {
      return Math.abs(this.totalDebit - this.totalCredit) < 0.01
    }
  },

  methods: {
    // 显示弹窗
    show(row) {
      this.dialogVisible = true
      if (row && row.voucherId) {
        this.fetchVoucherData(row.voucherId)
      } else {
        this.voucherData = { ...row, entries: row.entries || [] }
      }
    },

    // 获取凭证数据
    async fetchVoucherData(voucherId) {
      this.loading = true
      try {
        const response = await getVoucherById(voucherId)
        if (response.code === 1) {
          this.voucherData = response.data || {}
        } else {
          this.$message.error(response.msg || '获取凭证详情失败')
        }
      } catch (error) {
        console.error('获取凭证详情失败:', error)
        this.$message.error('获取凭证详情失败')
      } finally {
        this.loading = false
      }
    },

    handleClose() {
      this.dialogVisible = false
      this.voucherData = { entries: [] }
    },

    handlePrint() {
      window.print()
    },

    getStatusType(status) {
      // 后端状态：1草稿 2已保存 3已提交 4已审核 5已过账 6已取消
      const map = { 1: 'info', 2: 'warning', 3: 'primary', 4: 'primary', 5: 'success', 6: 'danger' }
      return map[status] || 'info'
    },

    getStatusName(status) {
      const map = { 1: '草稿', 2: '已保存', 3: '已提交', 4: '已审核', 5: '已过账', 6: '已取消' }
      return map[status] || status
    },

    getVoucherTypeName(typeId) {
      // 凭证类型ID映射
      const map = { 1: '记账凭证', 2: '收款凭证', 3: '付款凭证', 4: '转账凭证' }
      return map[typeId] || typeId || '-'
    },

    getVoucherTypeColor(typeId) {
      const map = { 1: 'primary', 2: 'success', 3: 'warning', 4: 'info' }
      return map[typeId] || 'info'
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped lang="scss">
.voucher-detail-dialog {
  .voucher-detail-container {
    padding: 0 10px;
  }

  .voucher-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;
  }

  .voucher-summary {
    margin-bottom: 15px;
    padding: 10px 15px;
    background: #f5f7fa;
    border-radius: 4px;
    .label { color: #606266; font-weight: bold; }
    .value { color: #303133; }
  }

  .entries-table { margin-bottom: 15px; }

  .amount {
    font-weight: bold;
    &.debit { color: #e6a23c; }
    &.credit { color: #409eff; }
  }

  .amount-total {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 30px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
    margin-bottom: 15px;
    .total-row { .label { margin-right: 10px; font-weight: bold; } }
    .balance-status {
      padding: 5px 15px;
      border-radius: 4px;
      font-weight: bold;
      color: #f56c6c;
      background: #fef0f0;
      &.balanced { color: #67c23a; background: #f0f9eb; }
    }
  }

  .signature-info, .time-info {
    display: flex;
    gap: 40px;
    padding: 10px 0;
    color: #606266;
    font-size: 14px;
    border-top: 1px dashed #ebeef5;
  }

  .dialog-footer { text-align: center; }
}
</style>

