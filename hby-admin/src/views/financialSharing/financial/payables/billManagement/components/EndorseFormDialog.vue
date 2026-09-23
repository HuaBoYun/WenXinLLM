<template>
  <el-dialog
    title="新增背书记录"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form :model="form" :rules="rules" ref="endorseForm" label-width="120px">
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
          <el-form-item label="背书日期" prop="endorseDate">
            <el-date-picker
              v-model="form.endorseDate"
              type="date"
              placeholder="请选择背书日期"
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
            <div>供应商：{{ selectedBill.supplierName || '-' }}</div>
            <div>到期日期：{{ selectedBill.dueDate || '-' }}</div>
          </div>
        </el-alert>
      </div>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="背书金额" prop="endorseAmount">
            <el-input-number
              v-model="form.endorseAmount"
              :precision="2"
              :min="0"
              :max="selectedBill.amount || 999999999"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="背书类型" prop="endorseType">
            <el-radio-group v-model="form.endorseType">
              <el-radio :label="1">转让</el-radio>
              <el-radio :label="2">质押</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="被背书人" prop="endorseeName">
            <el-input v-model="form.endorseeName" placeholder="请输入被背书人名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被背书人开户行" prop="endorseeBank">
            <el-input v-model="form.endorseeBank" placeholder="请输入开户行" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="被背书人账号" prop="endorseeAccount">
            <el-input v-model="form.endorseeAccount" placeholder="请输入银行账号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="背书说明" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入背书说明"
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
  name: 'EndorseFormDialog',
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
        endorseDate: new Date().toISOString().split('T')[0],
        endorseAmount: 0,
        endorseeName: '',
        endorseeBank: '',
        endorseeAccount: '',
        endorseType: 1,
        description: '',
        remarks: ''
      },
      rules: {
        billId: [{ required: true, message: '请选择票据', trigger: 'change' }],
        endorseDate: [{ required: true, message: '请选择背书日期', trigger: 'change' }],
        endorseAmount: [
          { required: true, message: '请输入背书金额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '背书金额必须大于0', trigger: 'blur' }
        ],
        endorseeName: [
          { required: true, message: '请输入被背书人', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在2到50个字符', trigger: 'blur' }
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
        this.form.endorseAmount = bill.amount || 0
      }
    },
    initForm() {
      this.form = {
        billId: '',
        endorseDate: new Date().toISOString().split('T')[0],
        endorseAmount: 0,
        endorseeName: '',
        endorseeBank: '',
        endorseeAccount: '',
        endorseType: 1,
        description: '',
        remarks: ''
      }
      this.selectedBill = {}
      if (this.$refs.endorseForm) {
        this.$refs.endorseForm.resetFields()
      }
    },
    handleClose() {
      this.initForm()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.endorseForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.endorseBill(this.form.billId, this.form)
          this.$message.success('背书成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('背书失败：' + (error.message || '未知错误'))
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
