<template>
  <el-dialog
    title="票据兑付处理"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <!-- 票据基本信息 -->
    <el-alert
      title="票据信息"
      type="info"
      :closable="false"
      style="margin-bottom: 20px"
    >
      <div style="font-size: 14px">
        <div>票据号：{{ billData.billNo || '-' }}</div>
        <div>票据金额：<span class="amount-text">{{ formatAmount(billData.amount) }}</span> 元</div>
        <div>到期日期：{{ billData.dueDate || '-' }}</div>
      </div>
    </el-alert>

    <!-- 兑付表单 -->
    <el-form :model="form" :rules="rules" ref="paymentForm" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="兑付日期" prop="paymentDate">
            <el-date-picker
              v-model="form.paymentDate"
              type="date"
              placeholder="请选择兑付日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="兑付金额" prop="paymentAmount">
            <el-input-number v-model="form.paymentAmount" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="实际到账金额" prop="actualAmount">
            <el-input-number v-model="form.actualAmount" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手续费" prop="fee">
            <el-input-number v-model="form.fee" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="兑付银行" prop="paymentBank">
            <el-input v-model="form.paymentBank" placeholder="请输入兑付银行" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="兑付账号" prop="paymentAccount">
            <el-input v-model="form.paymentAccount" placeholder="请输入兑付账号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="兑付方式" prop="paymentType">
            <el-radio-group v-model="form.paymentType">
              <el-radio :label="1">现金</el-radio>
              <el-radio :label="2">转账</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="兑付说明" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入兑付说明"
        />
      </el-form-item>

      <el-form-item label="备注">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定兑付</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'PaymentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    billData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        paymentDate: new Date().toISOString().split('T')[0],
        paymentAmount: 0,
        actualAmount: 0,
        fee: 0,
        paymentBank: '',
        paymentAccount: '',
        paymentType: 2,
        description: '',
        remarks: ''
      },
      rules: {
        paymentDate: [{ required: true, message: '请选择兑付日期', trigger: 'change' }],
        paymentAmount: [
          { required: true, message: '请输入兑付金额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '兑付金额必须大于0', trigger: 'blur' }
        ]
      }
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
      if (val && this.billData.billId) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      // 初始化兑付金额为票据金额
      this.form.paymentAmount = this.billData.amount || 0
      // 实际到账金额默认等于兑付金额
      this.form.actualAmount = this.billData.amount || 0
      // 手续费默认为0
      this.form.fee = 0
      // 重置其他字段
      this.form.paymentDate = new Date().toISOString().split('T')[0]
      this.form.paymentBank = ''
      this.form.paymentAccount = ''
      this.form.paymentType = 2
      this.form.description = ''
      this.form.remarks = ''
    },
    handleClose() {
      this.$refs.paymentForm.resetFields()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.paymentForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.payBill(this.billData.billId, this.form)
          this.$message.success('兑付成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('兑付失败：' + error.message)
        } finally {
          this.loading = false
        }
      })
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.amount-text {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
</style>
