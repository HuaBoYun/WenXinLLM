<template>
  <el-dialog
    :title="isEdit ? '编辑保证金' : '新增保证金'"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <el-form
      ref="depositForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              placeholder="请输入项目名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保证金类型" prop="depositType">
            <el-select
              v-model="formData.depositType"
              placeholder="请选择保证金类型"
              style="width: 100%"
            >
              <el-option label="投标保证金" value="BIDDING" />
              <el-option label="履约保证金" value="PERFORMANCE" />
              <el-option label="质量保证金" value="QUALITY" />
              <el-option label="其他保证金" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="保证金金额" prop="depositAmount">
            <el-input-number
              v-model="formData.depositAmount"
              :precision="2"
              :min="0"
              :max="999999999.99"
              controls-position="right"
              style="width: 100%"
              placeholder="请输入保证金金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缴纳方式" prop="paymentMethod">
            <el-select
              v-model="formData.paymentMethod"
              placeholder="请选择缴纳方式"
              style="width: 100%"
            >
              <el-option label="银行转账" value="BANK_TRANSFER" />
              <el-option label="现金缴纳" value="CASH" />
              <el-option label="银行保函" value="BANK_GUARANTEE" />
              <el-option label="保险保函" value="INSURANCE_GUARANTEE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="缴纳日期" prop="paymentDate">
            <el-date-picker
              v-model="formData.paymentDate"
              type="date"
              placeholder="请选择缴纳日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="到期日期" prop="expiryDate">
            <el-date-picker
              v-model="formData.expiryDate"
              type="date"
              placeholder="请选择到期日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="缴纳单位" prop="payerName">
            <el-input
              v-model="formData.payerName"
              placeholder="请输入缴纳单位"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactPerson">
            <el-input
              v-model="formData.contactPerson"
              placeholder="请输入联系人"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              placeholder="请输入联系电话"
              maxlength="20"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="formData.status"
              placeholder="请选择状态"
              style="width: 100%"
            >
              <el-option label="待缴纳" value="PENDING" />
              <el-option label="已缴纳" value="PAID" />
              <el-option label="已退还" value="REFUNDED" />
              <el-option label="已没收" value="FORFEITED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

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
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        {{ isEdit ? '更 新' : '保 存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveDeposit, updateDeposit } from '@/api/contract/bidding'

export default {
  name: 'DepositForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    editData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitLoading: false,
      formData: {
        projectName: '',
        depositType: '',
        depositAmount: null,
        paymentMethod: '',
        paymentDate: '',
        expiryDate: '',
        payerName: '',
        contactPerson: '',
        contactPhone: '',
        status: 'PENDING',
        remarks: ''
      },
      formRules: {
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        depositType: [
          { required: true, message: '请选择保证金类型', trigger: 'change' }
        ],
        depositAmount: [
          { required: true, message: '请输入保证金金额', trigger: 'blur' }
        ],
        paymentMethod: [
          { required: true, message: '请选择缴纳方式', trigger: 'change' }
        ],
        paymentDate: [
          { required: true, message: '请选择缴纳日期', trigger: 'change' }
        ],
        payerName: [
          { required: true, message: '请输入缴纳单位', trigger: 'blur' }
        ],
        contactPerson: [
          { required: true, message: '请输入联系人', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    editData: {
      handler(val) {
        if (val && Object.keys(val).length > 0) {
          this.formData = { ...val }
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    initForm() {
      if (this.isEdit && this.editData) {
        this.formData = { ...this.editData }
      } else {
        this.resetForm()
      }
    },
    resetForm() {
      this.formData = {
        projectName: '',
        depositType: '',
        depositAmount: null,
        paymentMethod: '',
        paymentDate: '',
        expiryDate: '',
        payerName: '',
        contactPerson: '',
        contactPhone: '',
        status: 'PENDING',
        remarks: ''
      }
      this.$nextTick(() => {
        this.$refs.depositForm && this.$refs.depositForm.clearValidate()
      })
    },
    handleSubmit() {
      this.$refs.depositForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          if (this.isEdit) {
            await updateDeposit(this.formData)
            this.$message.success('更新成功')
          } else {
            await saveDeposit(this.formData)
            this.$message.success('保存成功')
          }
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error(error.message || '保存失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
