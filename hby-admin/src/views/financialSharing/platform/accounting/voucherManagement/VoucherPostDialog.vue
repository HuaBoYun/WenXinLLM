<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="凭证过账"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
    class="voucher-post-dialog"
  >
    <div v-loading="loading" class="voucher-post-container">
      <!-- 凭证基本信息 -->
      <div class="voucher-info-card">
        <h4>待过账凭证信息</h4>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="凭证编号">{{ voucherData.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">
            <el-tag :type="getVoucherTypeColor(voucherData.voucherTypeId)" size="small">
              {{ getVoucherTypeName(voucherData.voucherTypeId) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="制单日期">{{ voucherData.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="凭证状态">
            <el-tag :type="getStatusType(voucherData.voucherStatus)" size="small">
              {{ voucherData.statusName || getStatusName(voucherData.voucherStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="摘要" :span="2">{{ voucherData.voucherDesc || '-' }}</el-descriptions-item>
          <el-descriptions-item label="借方合计">
            <span class="amount debit">¥{{ formatAmount(voucherData.totalDebit || totalDebit) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="贷方合计">
            <span class="amount credit">¥{{ formatAmount(voucherData.totalCredit || totalCredit) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 过账前验证 -->
      <div class="validation-card">
        <h4>过账前验证</h4>
        <div class="validation-list">
          <div class="validation-item" :class="{ success: isBalanced, error: !isBalanced }">
            <i :class="isBalanced ? 'el-icon-success' : 'el-icon-error'"></i>
            <span>借贷平衡检查：{{ isBalanced ? '通过' : '不通过（借贷不平衡）' }}</span>
          </div>
          <div class="validation-item" :class="{ success: isApproved, error: !isApproved }">
            <i :class="isApproved ? 'el-icon-success' : 'el-icon-error'"></i>
            <span>审核状态检查：{{ isApproved ? '已审核' : '未审核（需先审核凭证）' }}</span>
          </div>
          <div class="validation-item" :class="{ success: hasEntries, error: !hasEntries }">
            <i :class="hasEntries ? 'el-icon-success' : 'el-icon-error'"></i>
            <span>分录完整性检查：{{ hasEntries ? '通过' : '不通过（无分录数据）' }}</span>
          </div>
        </div>
      </div>

      <!-- 过账参数 -->
      <div class="post-params-card">
        <h4>过账参数</h4>
        <el-form :model="postForm" :rules="postRules" ref="postForm" label-width="100px">
          <el-form-item label="过账日期" prop="postDate">
            <el-date-picker
              v-model="postForm.postDate"
              type="date"
              placeholder="请选择过账日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="过账备注" prop="postRemark">
            <el-input
              v-model="postForm.postRemark"
              type="textarea"
              :rows="3"
              placeholder="请输入过账备注（选填）"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 警告提示 -->
      <el-alert
        v-if="canPost"
        title="过账后凭证将不可修改，请确认凭证信息无误后再进行过账操作。"
        type="warning"
        :closable="false"
        show-icon
      />
      <el-alert
        v-else
        title="当前凭证不满足过账条件，请先完成上述验证项。"
        type="error"
        :closable="false"
        show-icon
      />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" :disabled="!canPost" @click="handlePost">
        确认过账
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getVoucherById, postVouchers } from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherPostDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitting: false,
      voucherData: { entries: [] },
      postForm: {
        postDate: new Date().toISOString().split('T')[0],
        postRemark: ''
      },
      postRules: {
        postDate: [{ required: true, message: '请选择过账日期', trigger: 'change' }]
      }
    }
  },

  computed: {
    totalDebit() {
      return (this.voucherData.entries || []).reduce((sum, e) => sum + (e.debitAmount || 0), 0)
    },
    totalCredit() {
      return (this.voucherData.entries || []).reduce((sum, e) => sum + (e.creditAmount || 0), 0)
    },
    isBalanced() {
      const debit = this.voucherData.totalDebit || this.totalDebit
      const credit = this.voucherData.totalCredit || this.totalCredit
      return Math.abs(debit - credit) < 0.01
    },
    // 后端状态：4已审核 才能过账
    isApproved() { return this.voucherData.voucherStatus === 4 || this.voucherData.auditStatus === 1 },
    hasEntries() { return (this.voucherData.entries || []).length >= 2 },
    canPost() { return this.isBalanced && this.isApproved && this.hasEntries }
  },

  methods: {
    show(row) {
      this.dialogVisible = true
      this.postForm = { postDate: new Date().toISOString().split('T')[0], postRemark: '' }
      if (row && row.voucherId) {
        this.fetchVoucherData(row.voucherId)
      } else {
        this.voucherData = { ...row, entries: row.entries || [] }
      }
    },

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

    async handlePost() {
      this.$refs.postForm.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const response = await postVouchers([this.voucherData.voucherId])
          if (response.code === 1) {
            this.$message.success('凭证过账成功')
            this.dialogVisible = false
            this.$emit('success')
          } else {
            this.$message.error(response.msg || '过账失败')
          }
        } catch (error) {
          console.error('过账失败:', error)
          this.$message.error('过账失败')
        } finally {
          this.submitting = false
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.voucherData = { entries: [] }
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
    }
  }
}
</script>

<style scoped lang="scss">
.voucher-post-dialog {
  .voucher-post-container { padding: 0 10px; }

  .voucher-info-card, .validation-card, .post-params-card {
    margin-bottom: 20px;
    h4 {
      margin: 0 0 15px 0;
      padding-bottom: 10px;
      border-bottom: 1px solid #ebeef5;
      color: #303133;
      font-size: 15px;
    }
  }

  .amount {
    font-weight: bold;
    &.debit { color: #e6a23c; }
    &.credit { color: #409eff; }
  }

  .validation-list {
    .validation-item {
      display: flex;
      align-items: center;
      padding: 10px 15px;
      margin-bottom: 8px;
      border-radius: 4px;
      background: #f5f7fa;
      i { font-size: 18px; margin-right: 10px; }
      &.success { background: #f0f9eb; color: #67c23a; }
      &.error { background: #fef0f0; color: #f56c6c; }
    }
  }

  .dialog-footer { text-align: center; }
}
</style>

