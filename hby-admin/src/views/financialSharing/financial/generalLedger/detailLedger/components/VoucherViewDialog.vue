<template>
  <el-dialog
    title="凭证详情"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose">
    <div class="voucher-view-content" v-if="voucherData">
      <!-- 凭证头部信息 -->
      <div class="voucher-header">
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="header-item">
              <label>凭证字号：</label>
              <span class="voucher-no">{{ voucherData.voucherNo }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="header-item">
              <label>凭证日期：</label>
              <span>{{ voucherData.voucherDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="header-item">
              <label>制单人：</label>
              <span>{{ voucherDetail.creator || '张三' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="header-item">
              <label>审核人：</label>
              <span>{{ voucherDetail.reviewer || '李四' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="header-item">
              <label>记账人：</label>
              <span>{{ voucherDetail.bookkeeper || '王五' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="header-item">
              <label>凭证状态：</label>
              <el-tag :type="getStatusTag(voucherDetail.status)">
                {{ getStatusName(voucherDetail.status) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 凭证分录 -->
      <div class="voucher-entries">
        <h4>凭证分录</h4>
        <el-table :data="voucherDetail.entries" border size="small">
          <el-table-column prop="subjectCode" label="科目编码" width="100"></el-table-column>
          <el-table-column prop="subjectName" label="科目名称" min-width="150"></el-table-column>
          <el-table-column prop="summary" label="摘要" min-width="200"></el-table-column>
          <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.debitAmount > 0" class="debit-amount">
                {{ formatAmount(scope.row.debitAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.creditAmount > 0" class="credit-amount">
                {{ formatAmount(scope.row.creditAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="auxiliaryInfo" label="辅助核算" width="150">
            <template slot-scope="scope">
              <div v-if="scope.row.auxiliaryInfo">
                <div v-for="(aux, index) in scope.row.auxiliaryInfo" :key="index" class="auxiliary-item">
                  <el-tag size="mini" type="info">{{ aux.type }}：{{ aux.name }}</el-tag>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 合计行 -->
        <div class="entries-summary">
          <el-row :gutter="24">
            <el-col :span="12">
              <div class="summary-item">
                <label>借方合计：</label>
                <span class="debit-amount">{{ formatAmount(totalDebit) }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="summary-item">
                <label>贷方合计：</label>
                <span class="credit-amount">{{ formatAmount(totalCredit) }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="24">
              <div class="summary-item">
                <label>借贷平衡：</label>
                <el-tag :type="isBalanced ? 'success' : 'danger'" size="small">
                  {{ isBalanced ? '平衡' : '不平衡' }}
                </el-tag>
                <span v-if="!isBalanced" class="balance-diff">
                  差额：{{ formatAmount(Math.abs(totalDebit - totalCredit)) }}
                </span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 凭证附件 -->
      <div class="voucher-attachments" v-if="voucherDetail.attachments && voucherDetail.attachments.length > 0">
        <h4>凭证附件</h4>
        <div class="attachment-list">
          <div v-for="(attachment, index) in voucherDetail.attachments" :key="index" class="attachment-item">
            <i class="el-icon-document"></i>
            <span class="attachment-name">{{ attachment.name }}</span>
            <span class="attachment-size">{{ attachment.size }}</span>
            <el-button size="mini" type="text" @click="downloadAttachment(attachment)">下载</el-button>
            <el-button size="mini" type="text" @click="previewAttachment(attachment)">预览</el-button>
          </div>
        </div>
      </div>

      <!-- 凭证备注 -->
      <div class="voucher-remarks" v-if="voucherDetail.remarks">
        <h4>凭证备注</h4>
        <div class="remarks-content">
          {{ voucherDetail.remarks }}
        </div>
      </div>

      <!-- 操作记录 -->
      <div class="voucher-logs">
        <h4>操作记录</h4>
        <el-timeline>
          <el-timeline-item
            v-for="(log, index) in voucherDetail.operationLogs"
            :key="index"
            :timestamp="log.timestamp"
            :type="getLogType(log.operation)">
            <div class="log-content">
              <div class="log-operation">{{ log.operation }}</div>
              <div class="log-operator">操作人：{{ log.operator }}</div>
              <div class="log-remark" v-if="log.remark">备注：{{ log.remark }}</div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="printVoucher">打印凭证</el-button>
      <el-button type="success" @click="exportVoucher">导出凭证</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'VoucherViewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    voucherData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      voucherDetail: {}
    }
  },
  computed: {
    totalDebit() {
      if (!this.voucherDetail.entries) return 0
      return this.voucherDetail.entries.reduce((sum, entry) => sum + (entry.debitAmount || 0), 0)
    },
    totalCredit() {
      if (!this.voucherDetail.entries) return 0
      return this.voucherDetail.entries.reduce((sum, entry) => sum + (entry.creditAmount || 0), 0)
    },
    isBalanced() {
      return Math.abs(this.totalDebit - this.totalCredit) < 0.01
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.loadVoucherDetail()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    loadVoucherDetail() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.voucherDetail = {
        creator: '',
        reviewer: '',
        bookkeeper: '',
        status: '',
        entries: [],
        attachments: [],
        remarks: '',
        operationLogs: []
      }
    },
    getStatusTag(status) {
      const tags = {
        draft: 'info',
        reviewed: 'warning',
        posted: 'success',
        cancelled: 'danger'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        draft: '草稿',
        reviewed: '已审核',
        posted: '已记账',
        cancelled: '已作废'
      }
      return names[status] || status
    },
    getLogType(operation) {
      const types = {
        '制单': 'primary',
        '审核': 'warning',
        '记账': 'success',
        '作废': 'danger'
      }
      return types[operation] || 'info'
    },
    downloadAttachment(attachment) {
      const link = document.createElement('a')
      link.href = attachment.url || '#'
      link.download = attachment.name || '附件'
      link.click()
      this.$message.success('下载已开始')
    },
    previewAttachment(attachment) {
      if (attachment.url) {
        window.open(attachment.url, '_blank')
      } else {
        this.$message.warning('暂无可预览的附件地址')
      }
    },
    printVoucher() {
      window.print()
    },
    exportVoucher() {
      try {
        const data = this.voucherDetail || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '凭证导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.voucher-view-content {
  .voucher-header {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;

    .header-item {
      margin-bottom: 8px;
      font-size: 14px;

      label {
        color: #606266;
        margin-right: 8px;
        font-weight: 600;
      }

      .voucher-no {
        color: #409eff;
        font-weight: 600;
      }
    }
  }

  .voucher-entries {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .debit-amount {
      color: #409eff;
      font-weight: 600;
    }

    .credit-amount {
      color: #67c23a;
      font-weight: 600;
    }

    .auxiliary-item {
      margin-bottom: 4px;

      &:last-child {
        margin-bottom: 0;
      }
    }

    .entries-summary {
      margin-top: 16px;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 8px;

      .summary-item {
        margin-bottom: 8px;
        font-size: 14px;

        label {
          color: #606266;
          margin-right: 8px;
          font-weight: 600;
        }

        .balance-diff {
          margin-left: 8px;
          color: #f56c6c;
          font-weight: 600;
        }
      }
    }
  }

  .voucher-attachments {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .attachment-list {
      .attachment-item {
        display: flex;
        align-items: center;
        padding: 8px 0;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        i {
          margin-right: 8px;
          color: #409eff;
        }

        .attachment-name {
          flex: 1;
          margin-right: 8px;
        }

        .attachment-size {
          margin-right: 16px;
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }

  .voucher-remarks {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .remarks-content {
      padding: 16px;
      background: #f8f9fa;
      border-radius: 8px;
      color: #606266;
      line-height: 1.6;
    }
  }

  .voucher-logs {
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .log-content {
      .log-operation {
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .log-operator {
        font-size: 12px;
        color: #909399;
        margin-bottom: 2px;
      }

      .log-remark {
        font-size: 12px;
        color: #606266;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
