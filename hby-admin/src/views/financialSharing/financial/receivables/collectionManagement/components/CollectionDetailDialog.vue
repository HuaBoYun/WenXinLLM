<template>
  <el-dialog
    title="收款单详情"
    :visible.sync="dialogVisible"
    width="700px"
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
          <el-descriptions-item label="收款单号">
            {{ detailData.paymentId }}
          </el-descriptions-item>
          <el-descriptions-item label="客户名称">
            {{ detailData.customerName }}
          </el-descriptions-item>
          <el-descriptions-item label="收款金额">
            <span class="amount-text">¥ {{ formatAmount(detailData.amount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="收款方式">
            {{ detailData.paymentMethod }}
          </el-descriptions-item>
          <el-descriptions-item label="收款状态">
            <el-tag :type="getStatusType(detailData.status)">
              {{ detailData.statusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="收款日期">
            {{ detailData.paymentDate }}
          </el-descriptions-item>
          <el-descriptions-item label="操作人">
            {{ detailData.operator }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ detailData.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 关联应收单 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-connection"></i>
          关联应收单
        </div>
        <el-table :data="relatedReceivables" border size="small" max-height="200">
          <el-table-column prop="receivableNo" label="应收单号" width="150" />
          <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
            <template slot-scope="scope">
              <span>¥ {{ formatAmount(scope.row.receivableAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="writeOffAmount" label="已核销金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-text">¥ {{ formatAmount(scope.row.writeOffAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="writeOffDate" label="核销日期" width="120" />
        </el-table>
        <el-empty v-if="relatedReceivables.length === 0" description="暂无关联应收单" :image-size="60" />
      </div>

      <!-- 备注信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-edit-outline"></i>
          备注信息
        </div>
        <div class="remark-content">
          {{ detailData.remark || '暂无备注' }}
        </div>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button v-if="detailData.status === 'CONFIRMED'" type="success" @click="handleWriteOff">
        去核销
      </el-button>
      <el-button type="primary" icon="el-icon-printer" @click="handlePrint">
        打印
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'CollectionDetailDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detailData: {},
      relatedReceivables: []
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.detailData = { ...row }
      this.loadRelatedReceivables()
    },
    handleClose() {
      this.dialogVisible = false
      this.detailData = {}
      this.relatedReceivables = []
    },
    async loadRelatedReceivables() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.relatedReceivables = []
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    getStatusType(status) {
      const types = { 'PENDING': 'warning', 'CONFIRMED': 'success', 'CANCELLED': 'danger' }
      return types[status] || 'info'
    },
    handleWriteOff() {
      this.handleClose()
      this.$emit('write-off', this.detailData)
    },
    handlePrint() {
      window.print()
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
  }
}

::v-deep .el-descriptions-item__label {
  width: 100px;
  font-weight: 500;
}
</style>

