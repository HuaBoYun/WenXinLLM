<template>
  <el-dialog
    title="会计凭证查看"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="handleClose"
  >
    <div class="voucher-container">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="预提凭证" name="provision">
          <div class="voucher-header">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <label>凭证字号:</label>
                  <span>记{{ provisionVoucher.voucherNumber }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <label>凭证日期:</label>
                  <span>{{ provisionVoucher.voucherDate }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <label>制单人:</label>
                  <span>{{ provisionVoucher.creator }}</span>
                </div>
              </el-col>
            </el-row>
          </div>

          <div class="voucher-body">
            <el-table
              :data="provisionVoucher.entries"
              border
              stripe
              show-summary
              :summary-method="getSummaryMethod"
            >
              <el-table-column prop="entryNo" label="分录号" width="80" align="center" />
              <el-table-column prop="accountSubject" label="会计科目" min-width="200" />
              <el-table-column prop="summary" label="摘要" min-width="200" />
              <el-table-column prop="debitAmount" label="借方金额" width="150" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.debitAmount) }}
                </template>
              </el-table-column>
              <el-table-column prop="creditAmount" label="贷方金额" width="150" align="right">
                <template slot-scope="scope">
                  {{ formatAmount(scope.row.creditAmount) }}
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="voucher-footer">
            <p class="voucher-remark">摘要: {{ provisionVoucher.remark }}</p>
          </div>
        </el-tab-pane>

        <el-tab-pane label="冲销凭证" name="reverse" v-if="reverseVouchers.length > 0">
          <div v-for="(voucher, index) in reverseVouchers" :key="index" class="voucher-item">
            <el-divider v-if="index > 0" />
            
            <div class="voucher-header">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="info-item">
                    <label>凭证字号:</label>
                    <span>记{{ voucher.voucherNumber }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <label>凭证日期:</label>
                    <span>{{ voucher.voucherDate }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <label>制单人:</label>
                    <span>{{ voucher.creator }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div class="voucher-body">
              <el-table
                :data="voucher.entries"
                border
                stripe
                show-summary
                :summary-method="getSummaryMethod"
              >
                <el-table-column prop="entryNo" label="分录号" width="80" align="center" />
                <el-table-column prop="accountSubject" label="会计科目" min-width="200" />
                <el-table-column prop="summary" label="摘要" min-width="200" />
                <el-table-column prop="debitAmount" label="借方金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方金额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div class="voucher-footer">
              <p class="voucher-remark">摘要: {{ voucher.remark }}</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handlePrint">打印凭证</el-button>
      <el-button type="success" @click="handleExport">导出凭证</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'VoucherDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    provisionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'provision',
      provisionVoucher: {
        voucherNumber: '001',
        voucherDate: '2024-12-31',
        creator: '张三',
        remark: '费用预提',
        entries: [
          {
            entryNo: 1,
            accountSubject: '6601-管理费用-工资',
            summary: '12月工资预提',
            debitAmount: 500000,
            creditAmount: 0
          },
          {
            entryNo: 2,
            accountSubject: '2241-应付职工薪酬',
            summary: '12月工资预提',
            debitAmount: 0,
            creditAmount: 500000
          }
        ]
      },
      reverseVouchers: [
        {
          voucherNumber: '045',
          voucherDate: '2025-01-05',
          creator: '李四',
          remark: '工资发放冲销预提',
          entries: [
            {
              entryNo: 1,
              accountSubject: '2241-应付职工薪酬',
              summary: '12月工资发放',
              debitAmount: 500000,
              creditAmount: 0
            },
            {
              entryNo: 2,
              accountSubject: '1002-银行存款',
              summary: '12月工资发放',
              debitAmount: 0,
              creditAmount: 500000
            }
          ]
        }
      ]
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadVoucherData()
      }
    }
  },
  methods: {
    // 加载凭证数据
    loadVoucherData() {
      // 这里应该根据provisionData调用API获取实际的凭证数据
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      console.log('加载凭证数据:', this.provisionData)
    },

    // 合计方法
    getSummaryMethod({ columns, data }) {
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (index === 1 || index === 2) {
          sums[index] = ''
          return
        }
        
        const values = data.map(item => Number(item[column.property]))
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
      })
      return sums
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount && amount !== 0) return ''
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 打印凭证
    handlePrint() {
      const provisionApi = require('@/api/financialSharing/coreBusiness').provisionApi
      provisionApi.export({ provisionId: this.provisionData.provisionId }).then(res => {
        if (res.code === 1) {
          this.$message.success('打印凭证成功')
        } else {
          this.$message.error(res.msg || '打印失败')
        }
      }).catch(() => {
        this.$message.error('打印请求失败')
      })
    },

    // 导出凭证
    handleExport() {
      const provisionApi = require('@/api/financialSharing/coreBusiness').provisionApi
      provisionApi.export({ provisionId: this.provisionData.provisionId }).then(res => {
        if (res.code === 1) {
          this.$message.success('导出凭证成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(() => {
        this.$message.error('导出请求失败')
      })
    },

    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped lang="scss">
.voucher-container {
  .voucher-item {
    margin-bottom: 20px;
  }

  .voucher-header {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;

    .info-item {
      label {
        font-weight: 600;
        margin-right: 8px;
        color: #606266;
      }
      
      span {
        color: #303133;
      }
    }
  }

  .voucher-body {
    margin-bottom: 20px;
  }

  .voucher-footer {
    padding: 10px 15px;
    background: #f5f7fa;
    border-radius: 4px;

    .voucher-remark {
      margin: 0;
      color: #606266;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>

