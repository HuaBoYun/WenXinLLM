<template>
  <el-dialog
    title="收款核销"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="writeoff-container">
      <!-- 收款信息 -->
      <div class="info-section">
        <div class="section-title">
          <i class="el-icon-coin"></i>
          收款信息
        </div>
        <el-descriptions :column="4" border size="small">
          <el-descriptions-item label="收款单号">{{ paymentData.paymentId }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ paymentData.customerName }}</el-descriptions-item>
          <el-descriptions-item label="收款金额">
            <span class="amount-highlight">¥ {{ formatAmount(paymentData.amount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="可核销金额">
            <span class="amount-available">¥ {{ formatAmount(availableAmount) }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 待核销应收单列表 -->
      <div class="receivable-section">
        <div class="section-title">
          <i class="el-icon-document-checked"></i>
          选择待核销应收单
          <span class="tips">（勾选需要核销的应收单，并填写核销金额）</span>
        </div>
        <el-table
          ref="receivableTable"
          :data="receivableList"
          border
          size="small"
          max-height="300"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" />
          <el-table-column prop="receivableNo" label="应收单号" width="150" />
          <el-table-column prop="customerName" label="客户" width="150" />
          <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
            <template slot-scope="scope">
              <span>¥ {{ formatAmount(scope.row.receivableAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paidAmount" label="已收金额" width="120" align="right">
            <template slot-scope="scope">
              <span>¥ {{ formatAmount(scope.row.paidAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="unpaidAmount" label="未收金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-unpaid">¥ {{ formatAmount(scope.row.unpaidAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="本次核销金额" width="150">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.writeOffAmount"
                :min="0"
                :max="Math.min(scope.row.unpaidAmount, availableAmount)"
                :precision="2"
                :step="100"
                size="mini"
                :disabled="!isSelected(scope.row)"
                @change="calcTotalWriteOff"
              />
            </template>
          </el-table-column>
          <el-table-column prop="receivableDate" label="应收日期" width="110" />
        </el-table>
      </div>

      <!-- 核销汇总 -->
      <div class="summary-section">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="summary-item">
              <span class="label">已选应收单：</span>
              <span class="value">{{ selectedReceivables.length }} 笔</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span class="label">本次核销合计：</span>
              <span class="value amount-highlight">¥ {{ formatAmount(totalWriteOffAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span class="label">核销后剩余：</span>
              <span class="value amount-available">¥ {{ formatAmount(availableAmount - totalWriteOffAmount) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 备注 -->
      <div class="remark-section">
        <div class="section-title">
          <i class="el-icon-edit"></i>
          核销备注
        </div>
        <el-input
          v-model="remark"
          type="textarea"
          :rows="2"
          placeholder="请输入核销备注（选填）"
          maxlength="200"
          show-word-limit
        />
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" :disabled="!canSubmit" @click="handleSubmit">
        确认核销
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { writeOffPayment } from '@/api/financialSharing/receivables'

export default {
  name: 'WriteOffDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitting: false,
      paymentData: {},
      receivableList: [],
      selectedReceivables: [],
      totalWriteOffAmount: 0,
      remark: ''
    }
  },
  computed: {
    availableAmount() {
      return this.paymentData.amount || 0
    },
    canSubmit() {
      return this.selectedReceivables.length > 0 && this.totalWriteOffAmount > 0
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.paymentData = { ...row }
      this.loadReceivableList()
    },
    handleClose() {
      this.dialogVisible = false
      this.paymentData = {}
      this.receivableList = []
      this.selectedReceivables = []
      this.totalWriteOffAmount = 0
      this.remark = ''
    },
    async loadReceivableList() {
      this.loading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.receivableList = []
      } finally {
        this.loading = false
      }
    },
    handleSelectionChange(selection) {
      this.selectedReceivables = selection
      this.calcTotalWriteOff()
    },
    isSelected(row) {
      return this.selectedReceivables.some(item => item.id === row.id)
    },
    calcTotalWriteOff() {
      this.totalWriteOffAmount = this.selectedReceivables.reduce((sum, item) => {
        return sum + (item.writeOffAmount || 0)
      }, 0)
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    async handleSubmit() {
      if (this.totalWriteOffAmount > this.availableAmount) {
        this.$message.error('核销金额不能超过可核销金额')
        return
      }
      this.submitting = true
      try {
        const writeOffData = {
          paymentId: this.paymentData.paymentId,
          writeOffItems: this.selectedReceivables.map(item => ({
            receivableId: item.id,
            receivableNo: item.receivableNo,
            writeOffAmount: item.writeOffAmount
          })),
          totalAmount: this.totalWriteOffAmount,
          remark: this.remark
        }
        const res = await writeOffPayment(writeOffData)
        if (res.code === 1) {
          this.$message.success('核销成功')
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg || '核销失败')
        }
      } catch (error) {
        this.$message.error('核销失败，请稍后重试')
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.writeoff-container {
  .info-section,
  .receivable-section,
  .summary-section,
  .remark-section {
    margin-bottom: 20px;
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

    .tips {
      font-size: 12px;
      color: #909399;
      font-weight: normal;
      margin-left: 8px;
    }
  }

  .amount-highlight {
    color: #e6a23c;
    font-weight: 600;
  }

  .amount-available {
    color: #67c23a;
    font-weight: 600;
  }

  .amount-unpaid {
    color: #f56c6c;
    font-weight: 600;
  }

  .summary-section {
    background: #f5f7fa;
    border-radius: 8px;
    padding: 16px;

    .summary-item {
      display: flex;
      align-items: center;

      .label {
        color: #606266;
      }

      .value {
        font-size: 16px;
        font-weight: 600;
      }
    }
  }
}
</style>

