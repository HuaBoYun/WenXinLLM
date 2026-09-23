<template>
  <el-dialog
    title="科目余额调整"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose">
    <div class="adjust-dialog-content">
      <!-- 科目信息 -->
      <div class="subject-info">
        <h4>科目信息</h4>
        <el-row :gutter="16">
          <el-col :span="12">
            <div class="info-item">
              <label>科目编码：</label>
              <span>{{ subjectData.subjectCode }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>科目名称：</label>
              <span>{{ subjectData.subjectName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <div class="info-item">
              <label>当前余额：</label>
              <span :class="{ 'debit-amount': subjectData.endingBalance > 0, 'credit-amount': subjectData.endingBalance < 0 }">
                {{ formatAmount(Math.abs(subjectData.endingBalance)) }}
                {{ subjectData.endingBalance > 0 ? '(借方)' : subjectData.endingBalance < 0 ? '(贷方)' : '(无余额)' }}
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>科目类别：</label>
              <span>{{ getSubjectCategoryName(subjectData.subjectCategory) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 调整表单 -->
      <div class="adjust-form">
        <h4>余额调整</h4>
        <el-form :model="adjustForm" :rules="adjustRules" ref="adjustForm" label-width="120px">
          <el-form-item label="调整类型" prop="adjustType">
            <el-radio-group v-model="adjustForm.adjustType">
              <el-radio label="amount">按金额调整</el-radio>
              <el-radio label="balance">设置余额</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="调整方向" prop="direction" v-if="adjustForm.adjustType === 'amount'">
            <el-radio-group v-model="adjustForm.direction">
              <el-radio label="debit">借方</el-radio>
              <el-radio label="credit">贷方</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item 
            :label="adjustForm.adjustType === 'amount' ? '调整金额' : '目标余额'" 
            prop="amount">
            <el-input
              v-model="adjustForm.amount"
              placeholder="请输入金额"
              type="number"
              :min="0"
              :precision="2">
              <template slot="append">元</template>
            </el-input>
          </el-form-item>
          
          <el-form-item 
            label="目标方向" 
            prop="targetDirection" 
            v-if="adjustForm.adjustType === 'balance'">
            <el-radio-group v-model="adjustForm.targetDirection">
              <el-radio label="debit">借方</el-radio>
              <el-radio label="credit">贷方</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="调整原因" prop="reason">
            <el-input
              v-model="adjustForm.reason"
              type="textarea"
              :rows="3"
              placeholder="请输入调整原因">
            </el-input>
          </el-form-item>
          
          <el-form-item label="生效日期" prop="effectiveDate">
            <el-date-picker
              v-model="adjustForm.effectiveDate"
              type="date"
              placeholder="选择生效日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>

      <!-- 调整预览 -->
      <div class="adjust-preview" v-if="showPreview">
        <h4>调整预览</h4>
        <el-table :data="previewData" border size="small">
          <el-table-column prop="item" label="项目" width="120"></el-table-column>
          <el-table-column prop="before" label="调整前" align="right">
            <template slot-scope="scope">
              <span :class="{ 'debit-amount': scope.row.beforeAmount > 0, 'credit-amount': scope.row.beforeAmount < 0 }">
                {{ formatAmount(Math.abs(scope.row.beforeAmount)) }}
                {{ scope.row.beforeAmount > 0 ? '(借)' : scope.row.beforeAmount < 0 ? '(贷)' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="after" label="调整后" align="right">
            <template slot-scope="scope">
              <span :class="{ 'debit-amount': scope.row.afterAmount > 0, 'credit-amount': scope.row.afterAmount < 0 }">
                {{ formatAmount(Math.abs(scope.row.afterAmount)) }}
                {{ scope.row.afterAmount > 0 ? '(借)' : scope.row.afterAmount < 0 ? '(贷)' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="change" label="变动金额" align="right">
            <template slot-scope="scope">
              <span :class="{ 'increase': scope.row.changeAmount > 0, 'decrease': scope.row.changeAmount < 0 }">
                {{ scope.row.changeAmount > 0 ? '+' : '' }}{{ formatAmount(scope.row.changeAmount) }}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="previewAdjust" v-if="!showPreview">预览调整</el-button>
      <el-button type="success" @click="confirmAdjust" v-if="showPreview" :loading="submitting">确认调整</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { recalculateSubjectBalance } from '@/api/financialSharing/generalLedger'

export default {
  name: 'BalanceAdjustDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    subjectData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      showPreview: false,
      submitting: false,
      adjustForm: {
        adjustType: 'amount',
        direction: 'debit',
        targetDirection: 'debit',
        amount: '',
        reason: '',
        effectiveDate: ''
      },
      adjustRules: {
        adjustType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        direction: [
          { required: true, message: '请选择调整方向', trigger: 'change' }
        ],
        targetDirection: [
          { required: true, message: '请选择目标方向', trigger: 'change' }
        ],
        amount: [
          { required: true, message: '请输入金额', trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: '请输入正确的金额格式', trigger: 'blur' }
        ],
        reason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ]
      },
      previewData: []
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetForm()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    resetForm() {
      this.showPreview = false
      this.adjustForm = {
        adjustType: 'amount',
        direction: 'debit',
        targetDirection: 'debit',
        amount: '',
        reason: '',
        effectiveDate: new Date().toISOString().split('T')[0]
      }
      this.previewData = []
      if (this.$refs.adjustForm) {
        this.$refs.adjustForm.clearValidate()
      }
    },
    previewAdjust() {
      this.$refs.adjustForm.validate((valid) => {
        if (valid) {
          this.calculatePreview()
          this.showPreview = true
        }
      })
    },
    calculatePreview() {
      const currentBalance = this.subjectData.endingBalance || 0
      let afterBalance = 0
      
      if (this.adjustForm.adjustType === 'amount') {
        const adjustAmount = parseFloat(this.adjustForm.amount)
        if (this.adjustForm.direction === 'debit') {
          afterBalance = currentBalance + adjustAmount
        } else {
          afterBalance = currentBalance - adjustAmount
        }
      } else {
        const targetAmount = parseFloat(this.adjustForm.amount)
        afterBalance = this.adjustForm.targetDirection === 'debit' ? targetAmount : -targetAmount
      }
      
      this.previewData = [{
        item: '科目余额',
        beforeAmount: currentBalance,
        afterAmount: afterBalance,
        changeAmount: afterBalance - currentBalance
      }]
    },
    async confirmAdjust() {
      this.submitting = true
      try {
        const adjustData = {
          subjectCode: this.subjectData.subjectCode,
          adjustType: this.adjustForm.adjustType,
          direction: this.adjustForm.direction,
          targetDirection: this.adjustForm.targetDirection,
          amount: parseFloat(this.adjustForm.amount),
          reason: this.adjustForm.reason,
          effectiveDate: this.adjustForm.effectiveDate
        }
        
        const response = await recalculateSubjectBalance(adjustData)
        if (response.code === 200) {
          this.$message.success('余额调整成功')
          this.$emit('confirm')
          this.handleClose()
        }
      } catch (error) {
        this.$message.error('余额调整失败：' + error.message)
      } finally {
        this.submitting = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    },
    getSubjectCategoryName(category) {
      const names = {
        assets: '资产类',
        liabilities: '负债类',
        equity: '权益类',
        cost: '成本类',
        profit_loss: '损益类'
      }
      return names[category] || category
    }
  }
}
</script>

<style lang="scss" scoped>
.adjust-dialog-content {
  .subject-info {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .info-item {
      margin-bottom: 8px;
      font-size: 14px;

      label {
        color: #606266;
        margin-right: 8px;
      }

      .debit-amount {
        color: #409eff;
        font-weight: 600;
      }

      .credit-amount {
        color: #67c23a;
        font-weight: 600;
      }
    }
  }

  .adjust-form {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
  }

  .adjust-preview {
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

    .increase {
      color: #67c23a;
      font-weight: 600;
    }

    .decrease {
      color: #f56c6c;
      font-weight: 600;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
