<template>
  <el-dialog
    title="预付款退款"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预付款单号">
            <el-input v-model="prepaymentData.prepaymentNumber" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商">
            <el-input v-model="prepaymentData.supplierName" disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预付款金额">
            <el-input v-model="prepaymentData.prepaymentAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="可退款金额">
            <el-input v-model="prepaymentData.remainingAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="退款金额" prop="refundAmount">
            <el-input-number
              v-model="formData.refundAmount"
              :min="0"
              :max="prepaymentData.remainingAmount"
              :precision="2"
              style="width: 100%"
              placeholder="请输入退款金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="退款日期" prop="refundDate">
            <el-date-picker
              v-model="formData.refundDate"
              type="date"
              placeholder="请选择退款日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="退款原因" prop="refundReason">
            <el-select v-model="formData.refundReason" placeholder="请选择退款原因" style="width: 100%">
              <el-option label="合同终止" value="CONTRACT_TERMINATED" />
              <el-option label="供应商违约" value="SUPPLIER_BREACH" />
              <el-option label="项目取消" value="PROJECT_CANCELLED" />
              <el-option label="预付款金额错误" value="AMOUNT_ERROR" />
              <el-option label="其他原因" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="退款账户" prop="refundAccount">
            <el-input v-model="formData.refundAccount" placeholder="请输入退款账户" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="退款说明" prop="refundDesc">
            <el-input
              v-model="formData.refundDesc"
              type="textarea"
              :rows="3"
              placeholder="请输入退款说明"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-alert
        title="退款提示"
        type="warning"
        :closable="false"
        show-icon
        style="margin-top: 10px"
      >
        <div>1. 退款将原路退回至供应商账户</div>
        <div>2. 退款后该预付款金额将相应减少</div>
        <div>3. 退款操作需要财务审批</div>
      </el-alert>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定退款</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { prepaymentApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'RefundDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    prepaymentData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      formData: {
        refundAmount: 0,
        refundDate: new Date(),
        refundReason: '',
        refundAccount: '',
        refundDesc: ''
      },
      formRules: {
        refundAmount: [
          { required: true, message: '请输入退款金额', trigger: 'blur' },
          { 
            validator: (rule, value, callback) => {
              if (value <= 0) {
                callback(new Error('退款金额必须大于0'))
              } else if (value > this.prepaymentData.remainingAmount) {
                callback(new Error('退款金额不能超过可退款金额'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        refundDate: [{ required: true, message: '请选择退款日期', trigger: 'change' }],
        refundReason: [{ required: true, message: '请选择退款原因', trigger: 'change' }],
        refundAccount: [{ required: true, message: '请输入退款账户', trigger: 'blur' }],
        refundDesc: [{ required: true, message: '请输入退款说明', trigger: 'blur' }]
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
  methods: {
    handleConfirm() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.$confirm('确认执行退款操作?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            try {
              const res = await prepaymentApi.refund(this.prepaymentData.prepaymentId, this.formData)
              if (res.success) {
                this.$message.success('退款申请提交成功')
                this.$emit('success')
                this.handleClose()
              } else {
                this.$message.error(res.message || '退款失败')
              }
            } catch (error) {
              console.error('退款失败:', error)
              this.$message.error('退款失败')
            }
          })
        }
      })
    },

    handleClose() {
      this.resetForm()
      this.dialogVisible = false
    },

    resetForm() {
      this.formData = {
        refundAmount: 0,
        refundDate: new Date(),
        refundReason: '',
        refundAccount: '',
        refundDesc: ''
      }
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.dialog-footer {
  text-align: right;
}
</style>

