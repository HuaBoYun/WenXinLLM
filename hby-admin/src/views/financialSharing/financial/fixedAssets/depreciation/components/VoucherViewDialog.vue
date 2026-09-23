<template>
  <el-dialog
    title="查看凭证"
    :visible.sync="visible"
    width="900px"
    @close="handleClose"
  >
    <div class="voucher-container" v-loading="loading">
      <div class="voucher-header">
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="凭证号">{{ voucherData.voucherNumber }}</el-descriptions-item>
          <el-descriptions-item label="凭证日期">{{ voucherData.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="制单人">{{ voucherData.creator }}</el-descriptions-item>
          <el-descriptions-item label="业务类型">{{ voucherData.businessType }}</el-descriptions-item>
          <el-descriptions-item label="附件数">{{ voucherData.attachmentCount }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(voucherData.status)" size="small">
              {{ getStatusText(voucherData.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="voucher-body">
        <h4>会计分录</h4>
        <el-table
          :data="voucherData.entries"
          border
          stripe
          show-summary
          :summary-method="getSummaries"
        >
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="accountCode" label="科目编码" width="120" />
          <el-table-column prop="accountName" label="科目名称" min-width="200" />
          <el-table-column prop="debitAmount" label="借方金额" width="150" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.debitAmount > 0" class="amount-debit">
                {{ formatAmount(scope.row.debitAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="creditAmount" label="贷方金额" width="150" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.creditAmount > 0" class="amount-credit">
                {{ formatAmount(scope.row.creditAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
        </el-table>
      </div>

      <div class="voucher-footer" v-if="voucherData.remark">
        <h4>备注</h4>
        <p>{{ voucherData.remark }}</p>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handlePrint">打印</el-button>
      <el-button type="success" @click="handleExport">导出</el-button>
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
    voucherId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      voucherData: {
        voucherNumber: '',
        voucherDate: '',
        creator: '',
        businessType: '',
        attachmentCount: 0,
        status: '',
        entries: [],
        remark: ''
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.voucherId) {
        this.loadVoucherData()
      }
    }
  },
  methods: {
    async loadVoucherData() {
      this.loading = true
      try {
        // 暂未对接凭证数据 API，先以空状态展示，待后端接口提供后接入
        this.voucherData = {
          voucherNumber: '',
          voucherDate: '',
          creator: '',
          businessType: '',
          attachmentCount: 0,
          status: '',
          entries: [],
          remark: ''
        }
      } catch (error) {
        console.error('加载凭证数据失败：', error)
        this.$message.error('加载凭证数据失败')
      } finally {
        this.loading = false
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
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = ''
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handlePrint() {
      window.print()
    },
    handleExport() {
      try {
        const data = this.voucherData || {}
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
      this.$emit('update:visible', false)
    },
    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'POSTED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'default'
    },
    getStatusText(status) {
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
.voucher-container {
  padding: 10px 0;
}

.voucher-header {
  margin-bottom: 20px;
}

.voucher-body {
  margin-bottom: 20px;

  h4 {
    margin-bottom: 10px;
    color: #303133;
  }
}

.voucher-footer {
  h4 {
    margin-bottom: 10px;
    color: #303133;
  }

  p {
    color: #606266;
    line-height: 1.6;
  }
}

.amount-debit {
  color: #f56c6c;
  font-weight: 600;
}

.amount-credit {
  color: #67c23a;
  font-weight: 600;
}
</style>

