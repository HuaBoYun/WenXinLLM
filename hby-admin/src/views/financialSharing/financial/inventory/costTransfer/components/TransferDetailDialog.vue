<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="80%"
    :before-close="handleClose"
    class="transfer-detail-dialog"
  >
    <div class="dialog-content">
      <!-- 基本信息 -->
      <div class="info-section">
        <h3>结转信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>结转编号：</label>
              <span>{{ transferData.transferId }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>结转期间：</label>
              <span>{{ transferData.transferPeriod }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>结转类型：</label>
              <span>{{ transferData.transferTypeName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>结转金额：</label>
              <span class="amount">{{ formatAmount(transferData.transferAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>差异金额：</label>
              <span :class="['amount', transferData.varianceAmount >= 0 ? 'positive' : 'negative']">
                {{ formatAmount(transferData.varianceAmount) }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>状态：</label>
              <el-tag :type="getStatusTagType(transferData.status)">
                {{ getStatusText(transferData.status) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 结转明细 -->
      <div class="detail-section">
        <h3>结转明细</h3>
        <el-table :data="detailData" border>
          <el-table-column prop="inventoryCode" label="存货编码" width="120" />
          <el-table-column prop="inventoryName" label="存货名称" min-width="150" />
          <el-table-column prop="warehouseName" label="仓库" width="120" />
          <el-table-column prop="quantity" label="结转数量" width="100" align="right" />
          <el-table-column prop="unitCost" label="单位成本" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.unitCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="totalCost" label="结转金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.totalCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="variance" label="差异金额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="['amount', scope.row.variance >= 0 ? 'positive' : 'negative']">
                {{ formatAmount(scope.row.variance) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="voucherNumber" label="凭证号" width="120" />
        </el-table>
      </div>

      <!-- 操作记录 -->
      <div class="log-section">
        <h3>操作记录</h3>
        <el-timeline>
          <el-timeline-item
            v-for="(log, index) in operationLogs"
            :key="index"
            :timestamp="log.operateTime"
            :type="getLogType(log.operateType)"
          >
            <div class="log-content">
              <div class="log-title">{{ log.operateTypeName }}</div>
              <div class="log-desc">{{ log.description }}</div>
              <div class="log-operator">操作人：{{ log.operatorName }}</div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="transferData.status === 'TRANSFERRED'" type="warning" @click="handleRevoke">
        撤销结转
      </el-button>
      <el-button v-if="transferData.voucherCount > 0" type="primary" @click="handleViewVoucher">
        查看凭证
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TransferDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    transferData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      detailData: [],
      operationLogs: []
    }
  },
  computed: {
    dialogTitle() {
      return this.transferData.transferNo ? `成本结转详情 - ${this.transferData.transferNo}` : '成本结转详情'
    }
  },
  watch: {
    visible(val) {
      if (val && this.transferData.transferId) {
        this.loadTransferDetail()
      }
    }
  },
  methods: {
    async loadTransferDetail() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.detailData = []
        this.operationLogs = []
      } catch (error) {
        this.$message.error('加载结转详情失败：' + error.message)
      }
    },

    handleClose() {
      this.$emit('update:visible', false)
    },

    handleRevoke() {
      this.$confirm('确认要撤销此次成本结转吗？撤销后相关凭证将被删除。', '确认撤销', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('撤销成功')
        this.handleClose()
      }).catch(() => {
        // 用户取消
      })
    },

    handleViewVoucher() {
      this.$emit('view-voucher', this.transferData)
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'TRANSFERRED': 'success',
        'REVOKED': 'info'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PENDING': '待结转',
        'TRANSFERRED': '已结转',
        'REVOKED': '已撤销'
      }
      return textMap[status] || status
    },

    getLogType(operateType) {
      const typeMap = {
        'CREATE': 'primary',
        'EXECUTE': 'success',
        'REVOKE': 'warning'
      }
      return typeMap[operateType] || 'primary'
    }
  }
}
</script>

<style lang="scss" scoped>
.transfer-detail-dialog {
  .dialog-content {
    max-height: 600px;
    overflow-y: auto;

    .info-section,
    .detail-section,
    .log-section {
      margin-bottom: 30px;

      h3 {
        margin: 0 0 15px 0;
        color: #303133;
        font-size: 16px;
        border-bottom: 1px solid #e4e7ed;
        padding-bottom: 8px;
      }
    }

    .info-item {
      margin-bottom: 10px;

      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }

      .amount {
        font-weight: 600;

        &.positive {
          color: #67c23a;
        }

        &.negative {
          color: #f56c6c;
        }
      }
    }

    .log-content {
      .log-title {
        font-weight: 600;
        color: #303133;
        margin-bottom: 5px;
      }

      .log-desc {
        color: #606266;
        margin-bottom: 5px;
      }

      .log-operator {
        color: #909399;
        font-size: 12px;
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }
}

.amount {
  color: #f56c6c;
  font-weight: 600;

  &.positive {
    color: #67c23a;
  }

  &.negative {
    color: #f56c6c;
  }
}
</style>
