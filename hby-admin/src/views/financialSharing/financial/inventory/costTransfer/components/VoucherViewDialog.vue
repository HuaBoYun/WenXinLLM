<template>
  <el-dialog
    title="凭证查看"
    :visible.sync="visible"
    width="90%"
    :before-close="handleClose"
    class="voucher-view-dialog"
  >
    <div class="dialog-content">
      <!-- 凭证列表 -->
      <div class="voucher-list">
        <el-table :data="voucherList" border @row-click="handleVoucherClick" class="voucher-table">
          <el-table-column prop="voucherNumber" label="凭证号" width="150" />
          <el-table-column prop="voucherDate" label="凭证日期" width="120" />
          <el-table-column prop="voucherType" label="凭证类型" width="100" />
          <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.debitAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.creditAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getVoucherStatusTagType(scope.row.status)">
                {{ getVoucherStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="creatorName" label="制单人" width="100" />
          <el-table-column label="操作" width="120" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click.stop="handleViewDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 凭证详情 -->
      <div v-if="selectedVoucher" class="voucher-detail">
        <h3>凭证详情 - {{ selectedVoucher.voucherNumber }}</h3>
        
        <!-- 凭证头信息 -->
        <div class="voucher-header">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="header-item">
                <label>凭证号：</label>
                <span>{{ selectedVoucher.voucherNumber }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="header-item">
                <label>凭证日期：</label>
                <span>{{ selectedVoucher.voucherDate }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="header-item">
                <label>凭证类型：</label>
                <span>{{ selectedVoucher.voucherType }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="header-item">
                <label>制单人：</label>
                <span>{{ selectedVoucher.creatorName }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 凭证分录 -->
        <div class="voucher-entries">
          <el-table :data="voucherEntries" border show-summary :summary-method="getSummaries">
            <el-table-column prop="entryNumber" label="分录号" width="80" align="center" />
            <el-table-column prop="subjectCode" label="科目编码" width="120" />
            <el-table-column prop="subjectName" label="科目名称" min-width="150" />
            <el-table-column prop="auxiliaryInfo" label="辅助信息" width="150" show-overflow-tooltip />
            <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
              <template slot-scope="scope">
                <span v-if="scope.row.debitAmount" class="amount debit">
                  {{ formatAmount(scope.row.debitAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
              <template slot-scope="scope">
                <span v-if="scope.row.creditAmount" class="amount credit">
                  {{ formatAmount(scope.row.creditAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
          </el-table>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="selectedVoucher" type="primary" @click="handlePrint">打印凭证</el-button>
      <el-button v-if="voucherList.length > 0" type="success" @click="handleExport">导出凭证</el-button>
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
    transferId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      voucherList: [],
      selectedVoucher: null,
      voucherEntries: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.transferId) {
        this.loadVoucherList()
      } else {
        this.selectedVoucher = null
        this.voucherEntries = []
      }
    }
  },
  methods: {
    async loadVoucherList() {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.voucherList = []
      } catch (error) {
        this.$message.error('加载凭证列表失败：' + error.message)
      }
    },

    handleVoucherClick(row) {
      this.selectedVoucher = row
      this.loadVoucherEntries(row.voucherId)
    },

    handleViewDetail(row) {
      this.selectedVoucher = row
      this.loadVoucherEntries(row.voucherId)
    },

    async loadVoucherEntries(voucherId) {
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.voucherEntries = []
      } catch (error) {
        this.$message.error('加载凭证分录失败：' + error.message)
      }
    },

    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'debitAmount' || column.property === 'creditAmount') {
          const values = data.map(item => Number(item[column.property] || 0))
          if (!values.every(value => isNaN(value))) {
            const sum = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
            sums[index] = this.formatAmount(sum)
          } else {
            sums[index] = ''
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },

    handleClose() {
      this.$emit('update:visible', false)
    },

    handlePrint() {
      window.print()
    },

    handleExport() {
      try {
        const data = this.voucherEntries || []
        if (data.length === 0) { this.$message.warning('暂无数据可导出'); return }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '凭证导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) { this.$message.error('导出失败') }
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getVoucherStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'POSTED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getVoucherStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'POSTED': '已过账',
        'CANCELLED': '已作废'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.voucher-view-dialog {
  .dialog-content {
    max-height: 700px;
    overflow-y: auto;

    .voucher-list {
      margin-bottom: 30px;

      .voucher-table {
        .el-table__row {
          cursor: pointer;

          &:hover {
            background-color: #f5f7fa;
          }
        }
      }
    }

    .voucher-detail {
      h3 {
        margin: 0 0 15px 0;
        color: #303133;
        font-size: 16px;
        border-bottom: 1px solid #e4e7ed;
        padding-bottom: 8px;
      }

      .voucher-header {
        margin-bottom: 20px;
        padding: 15px;
        background: #f5f7fa;
        border-radius: 4px;

        .header-item {
          margin-bottom: 10px;

          label {
            font-weight: 600;
            color: #606266;
            margin-right: 8px;
          }
        }
      }

      .voucher-entries {
        .amount {
          font-weight: 600;

          &.debit {
            color: #f56c6c;
          }

          &.credit {
            color: #67c23a;
          }
        }
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
}
</style>
