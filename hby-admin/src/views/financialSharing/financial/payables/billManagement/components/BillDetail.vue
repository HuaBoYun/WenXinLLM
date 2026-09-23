<template>
  <el-dialog
    title="票据详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 基本信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-document"></i>
          基本信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="票据号">
            {{ detailData.billNo || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="票据类型">
            {{ getBillTypeName(detailData.billType) }}
          </el-descriptions-item>
          <el-descriptions-item label="供应商">
            {{ detailData.supplierName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="票据金额">
            <span class="amount-text">{{ formatAmount(detailData.amount) }} 元</span>
          </el-descriptions-item>
          <el-descriptions-item label="出票日期">
            {{ formatDate(detailData.issueDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="到期日期">
            {{ formatDate(detailData.dueDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="承兑银行">
            {{ detailData.bankName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="银行账号">
            {{ detailData.bankAccount || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="出票人">
            {{ detailData.drawer || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="收款人">
            {{ detailData.payee || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="票据状态">
            <el-tag :type="getStatusType(detailData.status)">
              {{ getStatusName(detailData.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(detailData.createTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 票据描述 -->
      <div v-if="detailData.description" class="detail-section">
        <div class="section-title">
          <i class="el-icon-edit-outline"></i>
          票据描述
        </div>
        <div class="remark-content">
          {{ detailData.description }}
        </div>
      </div>

      <!-- 备注信息 -->
      <div v-if="detailData.remarks" class="detail-section">
        <div class="section-title">
          <i class="el-icon-tickets"></i>
          备注信息
        </div>
        <div class="remark-content">
          {{ detailData.remarks }}
        </div>
      </div>

      <!-- 操作记录 -->
      <div v-if="detailData.operationList && detailData.operationList.length > 0" class="detail-section">
        <div class="section-title">
          <i class="el-icon-time"></i>
          操作记录
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in detailData.operationList"
            :key="index"
            :timestamp="formatDateTime(item.operationTime)"
            placement="top"
          >
            <el-card>
              <h4>{{ item.operationType }}</h4>
              <p>操作人：{{ item.operatorName || '-' }}</p>
              <p v-if="item.remarks">备注：{{ item.remarks }}</p>
              <p v-if="item.amount">金额：{{ formatAmount(item.amount) }} 元</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleEdit" type="primary" v-if="canEdit">编 辑</el-button>
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'BillDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detailData: {},
      currentBillId: null
    }
  },
  computed: {
    canEdit() {
      // 只有草稿状态才能编辑
      return this.detailData.status === 'draft'
    }
  },
  methods: {
    async open(billId) {
      this.dialogVisible = true
      this.currentBillId = billId
      this.loading = true
      try {
        const res = await payablesApi.getPayableBillDetail(billId)
        if (res.code === 1) {
          this.detailData = res.data || {}
        } else {
          this.$message.error(res.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取票据详情失败:', error)
        this.$message.error('获取详情失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.detailData = {}
      this.currentBillId = null
    },
    handleEdit() {
      this.$emit('edit', this.detailData)
      this.handleClose()
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    formatDate(date) {
      if (!date) return '-'
      if (Array.isArray(date)) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      if (typeof date === 'string') {
        return date.substring(0, 10)
      }
      return '-'
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      if (Array.isArray(dateTime)) {
        const [year, month, day, hour = 0, minute = 0, second = 0] = dateTime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
      }
      if (typeof dateTime === 'string') {
        return dateTime.replace('T', ' ').substring(0, 19)
      }
      return '-'
    },
    getBillTypeName(type) {
      const typeMap = {
        'bank_acceptance': '银行承兑汇票',
        'commercial_acceptance': '商业承兑汇票',
        'check': '支票',
        'promissory_note': '本票'
      }
      return typeMap[type] || '-'
    },
    getStatusName(status) {
      const statusMap = {
        'draft': '草稿',
        'pending': '待审核',
        'approved': '已审核',
        'endorsed': '已背书',
        'paid': '已兑付',
        'cancelled': '已作废',
        'overdue': '已逾期'
      }
      return statusMap[status] || '-'
    },
    getStatusType(status) {
      const typeMap = {
        'draft': 'info',
        'pending': 'warning',
        'approved': 'success',
        'endorsed': 'primary',
        'paid': 'success',
        'cancelled': 'danger',
        'overdue': 'danger'
      }
      return typeMap[status] || 'info'
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
      color: #f56c6c;
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
  }
}

::v-deep .el-descriptions-item__label {
  width: 100px;
  font-weight: 500;
}

::v-deep .el-timeline-item__wrapper {
  padding-left: 28px;
}

::v-deep .el-timeline-item__content {
  h4 {
    margin: 0 0 8px 0;
    font-size: 14px;
    color: #303133;
  }

  p {
    margin: 4px 0;
    font-size: 13px;
    color: #606266;
  }
}
</style>
