<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form :model="form" :rules="rules" ref="billForm" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="票据号" prop="billNo">
            <el-input v-model="form.billNo" placeholder="自动生成" :disabled="true">
              <el-button
                v-if="!isEdit"
                slot="append"
                icon="el-icon-refresh"
                @click="generateBillNo"
                title="重新生成票据号"
              >
              </el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="票据类型" prop="billType">
            <el-select v-model="form.billType" placeholder="请选择票据类型" style="width: 100%">
              <el-option label="银行承兑汇票" value="bank_acceptance" />
              <el-option label="商业承兑汇票" value="commercial_acceptance" />
              <el-option label="支票" value="check" />
              <el-option label="本票" value="promissory_note" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="供应商" prop="supplierId">
            <el-select
              v-model="form.supplierId"
              placeholder="请选择供应商"
              filterable
              style="width: 100%"
              @change="handleSupplierChange"
            >
              <el-option
                v-for="supplier in supplierList"
                :key="supplier.supplierId"
                :label="supplier.supplierName"
                :value="supplier.supplierId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="票据金额" prop="amount">
            <el-input-number v-model="form.amount" :precision="2" :min="0" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="出票日期" prop="issueDate">
            <el-date-picker
              v-model="form.issueDate"
              type="date"
              placeholder="请选择出票日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="到期日期" prop="dueDate">
            <el-date-picker
              v-model="form.dueDate"
              type="date"
              placeholder="请选择到期日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="承兑银行" prop="bankName">
            <el-input v-model="form.bankName" placeholder="请输入承兑银行" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行账号" prop="bankAccount">
            <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="出票人" prop="drawer">
            <el-input v-model="form.drawer" placeholder="请输入出票人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款人" prop="payee">
            <el-input v-model="form.payee" placeholder="请输入收款人" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="票据描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入票据描述"
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
  name: 'BillForm',
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
      supplierList: [],
      form: {
        billId: '',
        billNo: '',
        billType: '',
        supplierId: '',
        supplierName: '',
        amount: 0,
        issueDate: '',
        dueDate: '',
        bankName: '',
        bankAccount: '',
        drawer: '',
        payee: '',
        description: '',
        remarks: ''
      },
      rules: {
        billNo: [{ required: true, message: '请输入票据号', trigger: 'blur' }],
        billType: [{ required: true, message: '请选择票据类型', trigger: 'change' }],
        supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
        amount: [
          { required: true, message: '请输入票据金额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '金额必须大于0', trigger: 'blur' }
        ],
        issueDate: [{ required: true, message: '请选择出票日期', trigger: 'change' }],
        dueDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑票据' : '新增票据'
    },
    isEdit() {
      return !!this.form.billId
    },
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
        this.loadSuppliers()
        this.initForm()
      }
    },
    billData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.form = { ...this.form, ...newVal }
        }
      },
      deep: true
    }
  },
  methods: {
    async loadSuppliers() {
      try {
        const response = await payablesApi.getSupplierPage({ page: 0, size: 1000 })
        if (response.code === 1) {
          this.supplierList = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载供应商列表失败：', error)
      }
    },
    generateBillNo() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
      this.form.billNo = `BILL${year}${month}${day}${random}`
    },
    handleSupplierChange(supplierId) {
      const supplier = this.supplierList.find(s => s.supplierId === supplierId)
      if (supplier) {
        this.form.supplierName = supplier.supplierName
      }
    },
    initForm() {
      if (this.billData && this.billData.billId) {
        this.form = { ...this.billData }
      } else {
        this.resetForm()
        this.generateBillNo()
      }
    },
    resetForm() {
      this.form = {
        billId: '',
        billNo: '',
        billType: '',
        supplierId: '',
        supplierName: '',
        amount: 0,
        issueDate: '',
        dueDate: '',
        bankName: '',
        bankAccount: '',
        drawer: '',
        payee: '',
        description: '',
        remarks: ''
      }
    },
    handleClose() {
      this.$refs.billForm.resetFields()
      this.resetForm()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.billForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.saveOrUpdatePayableBill(this.form)
          this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('操作失败：' + (error.message || '未知错误'))
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
