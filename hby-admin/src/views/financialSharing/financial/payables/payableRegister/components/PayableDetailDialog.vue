<template>
  <el-dialog
    title="应付单据详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 基本信息区块 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-document"></i>
          基本信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="单据编号">
            {{ detailData.documentNo || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="供应商名称">
            {{ detailData.supplierName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="业务类型">
            {{ detailData.businessTypeName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="单据状态">
            <el-tag v-if="detailData.documentStatus !== undefined" :type="getStatusType(detailData.documentStatus)">
              {{ detailData.documentStatusName || '-' }}
            </el-tag>
            <span v-else>-</span>
            <el-tag v-if="detailData.overdue" type="danger" size="mini" class="overdue-tag">
              逾期{{ detailData.overdueDays }}天
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="应付金额">
            <span class="amount-text">{{ detailData.currency || 'CNY' }} {{ formatAmount(detailData.payableAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="已付金额">
            <span class="amount-text">{{ detailData.currency || 'CNY' }} {{ formatAmount(detailData.paidAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="未付金额">
            <span class="amount-text">{{ detailData.currency || 'CNY' }} {{ formatAmount(detailData.remainingAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="到期日期">
            {{ formatDate(detailData.dueDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="币种">
            {{ detailData.currency || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="汇率">
            {{ detailData.exchangeRate || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建人">
            {{ detailData.createByName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(detailData.createTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 审核信息区块 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-circle-check"></i>
          审核信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="审核人">
            {{ detailData.auditUser || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="审核时间">
            {{ formatDateTime(detailData.auditTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" :span="2">
            {{ detailData.auditComments || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 备注信息区块 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-edit-outline"></i>
          备注信息
        </div>
        <div class="remark-content">
          {{ detailData.remarks || '暂无备注' }}
        </div>
      </div>

      <!-- 摘要信息区块 -->
      <div v-if="detailData.summary" class="detail-section">
        <div class="section-title">
          <i class="el-icon-tickets"></i>
          摘要信息
        </div>
        <div class="remark-content">
          {{ detailData.summary }}
        </div>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getPayableDocumentDetail } from '@/api/financialSharing/payables'

export default {
  name: 'PayableDetailDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detailData: {}
    }
  },
  methods: {
    // 打开弹窗并加载详情数据
    async open(documentId) {
      this.dialogVisible = true
      this.loading = true
      try {
        const res = await getPayableDocumentDetail(documentId)
        if (res.code === 1) {
          this.detailData = res.data || {}
        } else {
          this.$message.error(res.message || '获取详情失败')
        }
      } catch (error) {
        console.error('获取应付单据详情失败:', error)
        this.$message.error('获取详情失败,请稍后重试')
      } finally {
        this.loading = false
      }
    },
    // 关闭弹窗并清理数据
    handleClose() {
      this.dialogVisible = false
      this.detailData = {}
    },
    // 金额格式化
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    // 格式化日期 (处理数组格式 [2026, 1, 14])
    formatDate(date) {
      if (!date) return '-'
      // 如果是数组格式 [year, month, day]
      if (Array.isArray(date)) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      // 如果是字符串格式
      if (typeof date === 'string') {
        return date.substring(0, 10)
      }
      return '-'
    },
    // 格式化日期时间 (处理数组格式 [2026, 1, 13, 14, 39, 11])
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      // 如果是数组格式 [year, month, day, hour, minute, second]
      if (Array.isArray(dateTime)) {
        const [year, month, day, hour = 0, minute = 0, second = 0] = dateTime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
      }
      // 如果是字符串格式
      if (typeof dateTime === 'string') {
        return dateTime.replace('T', ' ').substring(0, 19)
      }
      return '-'
    },
    // 获取状态标签类型
    getStatusType(status) {
      const types = {
        0: 'info',      // 草稿
        1: 'warning',   // 待审核
        2: 'success',   // 已审核
        3: 'danger'     // 已拒绝
      }
      return types[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  .detail-section {
    margin-bottom: 24px;

    &:last-child {
      margin-bottom: 0;
    }

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .amount-text {
      color: #67c23a;
      font-weight: 600;
      font-size: 16px;
    }

    .remark-content {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 12px 16px;
      color: #606266;
      min-height: 60px;
      line-height: 1.6;
    }

    .overdue-tag {
      margin-left: 8px;
    }
  }
}

::v-deep .el-descriptions-item__label {
  width: 100px;
  font-weight: 500;
}
</style>
