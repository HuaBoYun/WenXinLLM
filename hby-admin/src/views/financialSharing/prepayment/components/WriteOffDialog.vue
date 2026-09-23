<template>
  <el-dialog
    title="预付款核销"
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
          <el-form-item label="待核销金额">
            <el-input v-model="prepaymentData.remainingAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="核销金额" prop="writeOffAmount">
            <el-input-number
              v-model="formData.writeOffAmount"
              :min="0"
              :max="prepaymentData.remainingAmount"
              :precision="2"
              style="width: 100%"
              placeholder="请输入核销金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核销日期" prop="writeOffDate">
            <el-date-picker
              v-model="formData.writeOffDate"
              type="date"
              placeholder="请选择核销日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="关联单据类型" prop="relatedDocType">
            <el-select v-model="formData.relatedDocType" placeholder="请选择关联单据类型" style="width: 100%">
              <el-option label="采购发票" value="PURCHASE_INVOICE" />
              <el-option label="服务发票" value="SERVICE_INVOICE" />
              <el-option label="应付单据" value="PAYABLE_DOC" />
              <el-option label="其他单据" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="关联单据号" prop="relatedDocNumber">
            <el-input v-model="formData.relatedDocNumber" placeholder="请输入关联单据号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="核销说明" prop="writeOffDesc">
            <el-input
              v-model="formData.writeOffDesc"
              type="textarea"
              :rows="3"
              placeholder="请输入核销说明"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定核销</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { prepaymentApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'WriteOffDialog',
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
        writeOffAmount: 0,
        writeOffDate: new Date(),
        relatedDocType: '',
        relatedDocNumber: '',
        writeOffDesc: ''
      },
      formRules: {
        writeOffAmount: [
          { required: true, message: '请输入核销金额', trigger: 'blur' },
          { 
            validator: (rule, value, callback) => {
              if (value <= 0) {
                callback(new Error('核销金额必须大于0'))
              } else if (value > this.prepaymentData.remainingAmount) {
                callback(new Error('核销金额不能超过待核销金额'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        writeOffDate: [{ required: true, message: '请选择核销日期', trigger: 'change' }],
        relatedDocType: [{ required: true, message: '请选择关联单据类型', trigger: 'change' }],
        relatedDocNumber: [{ required: true, message: '请输入关联单据号', trigger: 'blur' }],
        writeOffDesc: [{ required: true, message: '请输入核销说明', trigger: 'blur' }]
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
          try {
            const res = await prepaymentApi.writeOff(this.prepaymentData.prepaymentId, {
              ...this.formData,
              contractId: this.prepaymentData.contractId || ''
            })
            if (res.code === 1) {
              this.$message.success('核销成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(res.message || '核销失败')
            }
          } catch (error) {
            console.error('核销失败:', error)
            this.$message.error('核销失败')
          }
        }
      })
    },

    handleClose() {
      this.resetForm()
      this.dialogVisible = false
    },

    resetForm() {
      this.formData = {
        writeOffAmount: 0,
        writeOffDate: new Date(),
        relatedDocType: '',
        relatedDocNumber: '',
        writeOffDesc: ''
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

