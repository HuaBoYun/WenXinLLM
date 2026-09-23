<template>
  <el-dialog
    :title="isEdit ? '编辑收款单' : '新增收款单'"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="客户" prop="customerId">
            <el-select
              v-model="formData.customerId"
              placeholder="请选择客户"
              filterable
              clearable
              style="width: 100%"
              @change="handleCustomerChange"
            >
              <el-option
                v-for="item in customerOptions"
                :key="item.customerId"
                :label="item.customerName"
                :value="item.customerId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款金额" prop="receiptAmount">
            <el-input-number
              v-model="formData.receiptAmount"
              :min="0"
              :precision="2"
              :step="1000"
              placeholder="请输入收款金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="收款方式" prop="paymentMethod">
            <el-select v-model="formData.paymentMethod" placeholder="请选择收款方式" style="width: 100%">
              <el-option label="现金" :value="1" />
              <el-option label="银行转账" :value="2" />
              <el-option label="支票" :value="3" />
              <el-option label="承兑汇票" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款日期" prop="receiptDate">
            <el-date-picker
              v-model="formData.receiptDate"
              type="date"
              placeholder="请选择收款日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开户银行" prop="bankName">
            <el-input v-model="formData.bankName" placeholder="请输入开户银行" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行账号" prop="bankAccount">
            <el-input v-model="formData.bankAccount" placeholder="请输入银行账号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        确 定
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveOrUpdatePaymentReceipt, getCustomerPage } from '@/api/financialSharing/receivables'

export default {
  name: 'CreateReceiptDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitting: false,
      isEdit: false,
      customerOptions: [],
      formData: {
        receiptId: '',
        customerId: '',
        customerName: '',
        receiptAmount: null,
        paymentMethod: 2,
        receiptDate: '',
        bankName: '',
        bankAccount: '',
        remarks: ''
      },
      formRules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        receiptAmount: [{ required: true, message: '请输入收款金额', trigger: 'blur' }],
        paymentMethod: [{ required: true, message: '请选择收款方式', trigger: 'change' }],
        receiptDate: [{ required: true, message: '请选择收款日期', trigger: 'change' }]
      }
    }
  },
  methods: {
    open(row = null) {
      this.dialogVisible = true
      this.loadCustomerOptions()
      if (row) {
        this.isEdit = true
        this.formData = { ...row }
      } else {
        this.isEdit = false
        this.resetForm()
      }
    },
    async loadCustomerOptions() {
      try {
        const res = await getCustomerPage({ pageNumber: 1, pageSize: 1000 })
        // 兼容 code=1 和 code=200 两种成功状态码
        if ((res.code === 1 || res.code === 200) && res.data) {
          // 兼容 tlist 和 records 两种数据格式
          this.customerOptions = res.data.tlist || res.data.records || []
        }
      } catch (e) {
        console.error('加载客户列表失败', e)
      }
    },
    handleCustomerChange(customerId) {
      const customer = this.customerOptions.find(c => c.customerId === customerId)
      if (customer) {
        this.formData.customerName = customer.customerName
      }
    },
    resetForm() {
      this.formData = {
        receiptId: '',
        customerId: '',
        customerName: '',
        receiptAmount: null,
        paymentMethod: 2,
        receiptDate: '',
        bankName: '',
        bankAccount: '',
        remarks: ''
      }
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        this.submitting = true
        const res = await saveOrUpdatePaymentReceipt(this.formData)
        // 兼容 code=1 和 code=200 两种成功状态码
        if (res.code === 1 || res.code === 200) {
          this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (e) {
        if (e !== false) {
          this.$message.error('操作失败: ' + (e.message || e))
        }
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>

