<template>
  <el-dialog
    title="保证金详情"
    :visible.sync="visible"
    width="900px"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading" class="deposit-detail">
      <!-- 基本信息 -->
      <el-card class="detail-card" shadow="never">
        <div slot="header" class="card-header">
          <span class="card-title">基本信息</span>
          <el-tag
            :type="getStatusType(detailData.status)"
            size="small"
          >
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>项目名称：</label>
              <span>{{ detailData.projectName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>保证金类型：</label>
              <span>{{ getDepositTypeText(detailData.depositType) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>保证金金额：</label>
              <span class="amount">{{ formatAmount(detailData.depositAmount) }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>缴纳方式：</label>
              <span>{{ getPaymentMethodText(detailData.paymentMethod) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>缴纳日期：</label>
              <span>{{ detailData.paymentDate || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>到期日期：</label>
              <span>{{ detailData.expiryDate || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 缴纳单位信息 -->
      <el-card class="detail-card" shadow="never">
        <div slot="header" class="card-header">
          <span class="card-title">缴纳单位信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>缴纳单位：</label>
              <span>{{ detailData.payerName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>联系人：</label>
              <span>{{ detailData.contactPerson || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>联系电话：</label>
              <span>{{ detailData.contactPhone || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 操作记录 -->
      <el-card class="detail-card" shadow="never">
        <div slot="header" class="card-header">
          <span class="card-title">操作记录</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(record, index) in operationRecords"
            :key="index"
            :timestamp="record.operationTime"
            placement="top"
          >
            <el-card>
              <h4>{{ record.operationType }}</h4>
              <p>操作人：{{ record.operator }}</p>
              <p v-if="record.amount">金额：{{ formatAmount(record.amount) }}</p>
              <p v-if="record.remarks">备注：{{ record.remarks }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>

      <!-- 备注信息 -->
      <el-card v-if="detailData.remarks" class="detail-card" shadow="never">
        <div slot="header" class="card-header">
          <span class="card-title">备注信息</span>
        </div>
        <div class="remarks-content">
          {{ detailData.remarks }}
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button
        v-if="detailData.status === 'PAID'"
        type="warning"
        @click="handleRefund"
      >
        退还保证金
      </el-button>
      <el-button
        v-if="detailData.status === 'PAID'"
        type="danger"
        @click="handleForfeit"
      >
        没收保证金
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDepositById, refundDeposit, forfeitDeposit } from '@/api/contract/bidding'

export default {
  name: 'DepositDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    depositId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      detailData: {},
      operationRecords: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.depositId) {
        this.loadDepositDetail()
      }
    }
  },
  methods: {
    async loadDepositDetail() {
      if (!this.depositId) return

      this.loading = true
      try {
        const response = await getDepositById(this.depositId)
        this.detailData = response.data || {}
        this.operationRecords = response.data.operationRecords || []
      } catch (error) {
        console.error('加载保证金详情失败:', error)
        this.$message.error('加载详情失败')
      } finally {
        this.loading = false
      }
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'PAID': 'success',
        'REFUNDED': 'info',
        'FORFEITED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待缴纳',
        'PAID': '已缴纳',
        'REFUNDED': '已退还',
        'FORFEITED': '已没收'
      }
      return statusMap[status] || '未知'
    },
    getDepositTypeText(type) {
      const typeMap = {
        'BIDDING': '投标保证金',
        'PERFORMANCE': '履约保证金',
        'QUALITY': '质量保证金',
        'OTHER': '其他保证金'
      }
      return typeMap[type] || '未知'
    },
    getPaymentMethodText(method) {
      const methodMap = {
        'BANK_TRANSFER': '银行转账',
        'CASH': '现金缴纳',
        'BANK_GUARANTEE': '银行保函',
        'INSURANCE_GUARANTEE': '保险保函'
      }
      return methodMap[method] || '未知'
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return `¥${Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    },
    async handleRefund() {
      try {
        await this.$confirm('确认要退还此保证金吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await refundDeposit(this.depositId)
        this.$message.success('退还成功')
        this.$emit('success')
        this.loadDepositDetail()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('退还失败:', error)
          this.$message.error('退还失败')
        }
      }
    },
    async handleForfeit() {
      try {
        await this.$confirm('确认要没收此保证金吗？此操作不可撤销！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })

        await forfeitDeposit(this.depositId)
        this.$message.success('没收成功')
        this.$emit('success')
        this.loadDepositDetail()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('没收失败:', error)
          this.$message.error('没收失败')
        }
      }
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.detailData = {}
      this.operationRecords = []
    }
  }
}
</script>

<style scoped>
.deposit-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-card {
  margin-bottom: 20px;
}

.detail-card:last-child {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-weight: bold;
  font-size: 16px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.detail-item label {
  font-weight: bold;
  color: #606266;
  min-width: 100px;
  margin-right: 10px;
}

.detail-item span {
  color: #303133;
}

.amount {
  font-weight: bold;
  color: #E6A23C;
  font-size: 16px;
}

.remarks-content {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}

.el-timeline {
  padding-left: 0;
}
</style>
