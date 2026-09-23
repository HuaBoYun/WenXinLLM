<template>
  <el-dialog
    title="新增兑付"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form :model="form" :rules="rules" ref="paymentForm" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="选择票据" prop="billId">
            <el-select
              v-model="form.billId"
              placeholder="请选择票据"
              filterable
              style="width: 100%"
              @change="handleBillChange"
              :loading="billLoading"
            >
              <el-option
                v-for="bill in activeBillList"
                :key="bill.billId"
                :label="`${bill.billNo} - ${formatAmount(bill.amount)}元`"
                :value="bill.billId"
              >
                <span style="float: left">{{ bill.billNo }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ formatAmount(bill.amount) }}元
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
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
      </el-row>

      <!-- 票据信息展示 -->
      <div v-if="selectedBill.billId" class="bill-info">
        <el-alert
          title="票据信息"
          type="info"
          :closable="false"
          style="margin-bottom: 20px"
        >
          <div style="font-size: 14px">
            <div>票据号：{{ selectedBill.billNo || '-' }}</div>
            <div>票据金额：<span class="amount-text">{{ formatAmount(selectedBill.amount) }}</span> 元</div>
            <div>到期日期：{{ selectedBill.dueDate || '-' }}</div>
          </div>
        </el-alert>
      </div>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="兑付金额" prop="paymentAmount">
            <el-input-number
              v-model="form.paymentAmount"
              :precision="2"
              :min="0"
              :max="selectedBill.amount || 999999999"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际到账金额" prop="actualAmount">
            <el-input-number
              v-model="form.actualAmount"
              :precision="2"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="手续费" prop="fee">
            <el-input-number
              v-model="form.fee"
              :precision="2"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="兑付方式" prop="paymentType">
            <el-radio-group v-model="form.paymentType">
              <el-radio :label="1">现金</el-radio>
              <el-radio :label="2">转账</el-radio>
            </el-radio-group>
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
      <el-button type="primary" @click="handleSubmit" :loading="loading">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'PaymentFormDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      billLoading: false,
      activeBillList: [],
      selectedBill: {},
      form: {
        billId: '',
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
        billId: [{ required: true, message: '请选择票据', trigger: 'change' }],
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
      if (val) {
        this.loadActiveBills()
        this.initForm()
      }
    }
  },
  methods: {
    async loadActiveBills() {
      this.billLoading = true
      try {
        const response = await payablesApi.getPayableBillsPage({
          page: 0,
          size: 1000,
          status: 'active'
        })
        if (response.code === 1) {
          this.activeBillList = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载票据列表失败：', error)
        this.$message.error('加载票据列表失败')
      } finally {
        this.billLoading = false
      }
    },
    handleBillChange(billId) {
      const bill = this.activeBillList.find(b => b.billId === billId)
      if (bill) {
        this.selectedBill = { ...bill }
        this.form.paymentAmount = bill.amount || 0
        this.form.actualAmount = bill.amount || 0
      }
    },
    initForm() {
      this.form = {
        billId: '',
        paymentDate: new Date().toISOString().split('T')[0],
        paymentAmount: 0,
        actualAmount: 0,
        fee: 0,
        paymentBank: '',
        paymentAccount: '',
        paymentType: 2,
        description: '',
        remarks: ''
      }
      this.selectedBill = {}
      if (this.$refs.paymentForm) {
        this.$refs.paymentForm.resetFields()
      }
    },
    handleClose() {
      this.initForm()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.paymentForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.payBill(this.form.billId, this.form)
          this.$message.success('兑付成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('兑付失败：' + (error.message || '未知错误'))
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

.bill-info {
  margin-bottom: 20px;
}

.amount-text {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
</style>
