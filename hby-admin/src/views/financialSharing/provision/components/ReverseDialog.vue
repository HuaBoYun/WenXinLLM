<template>
  <el-dialog
    title="费用预提冲销"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预提单号">
            <el-input v-model="provisionData.provisionNumber" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预提期间">
            <el-input v-model="provisionData.provisionPeriod" disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预提金额">
            <el-input v-model="provisionData.provisionAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="待冲销金额">
            <el-input v-model="provisionData.remainingAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="冲销金额" prop="reverseAmount">
            <el-input-number
              v-model="formData.reverseAmount"
              :min="0"
              :max="provisionData.remainingAmount"
              :precision="2"
              style="width: 100%"
              placeholder="请输入冲销金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="冲销日期" prop="reverseDate">
            <el-date-picker
              v-model="formData.reverseDate"
              type="date"
              placeholder="请选择冲销日期"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="实际发生单据" prop="actualDocType">
            <el-select v-model="formData.actualDocType" placeholder="请选择实际发生单据类型" style="width: 100%">
              <el-option label="工资单" value="SALARY_DOC" />
              <el-option label="奖金单" value="BONUS_DOC" />
              <el-option label="租金发票" value="RENT_INVOICE" />
              <el-option label="利息单据" value="INTEREST_DOC" />
              <el-option label="其他单据" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="实际单据号" prop="actualDocNumber">
            <el-input v-model="formData.actualDocNumber" placeholder="请输入实际发生单据号" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="冲销说明" prop="reverseDesc">
            <el-input
              v-model="formData.reverseDesc"
              type="textarea"
              :rows="3"
              placeholder="请输入冲销说明"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-alert
        title="冲销提示"
        type="info"
        :closable="false"
        show-icon
        style="margin-top: 10px"
      >
        <div>1. 冲销将自动生成冲销凭证</div>
        <div>2. 冲销金额将抵消预提金额</div>
        <div>3. 完全冲销后预提状态将变为已冲销</div>
      </el-alert>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定冲销</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { provisionApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'ReverseDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    provisionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      formData: {
        reverseAmount: 0,
        reverseDate: new Date(),
        actualDocType: '',
        actualDocNumber: '',
        reverseDesc: ''
      },
      formRules: {
        reverseAmount: [
          { required: true, message: '请输入冲销金额', trigger: 'blur' },
          { 
            validator: (rule, value, callback) => {
              if (value <= 0) {
                callback(new Error('冲销金额必须大于0'))
              } else if (value > this.provisionData.remainingAmount) {
                callback(new Error('冲销金额不能超过待冲销金额'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        reverseDate: [{ required: true, message: '请选择冲销日期', trigger: 'change' }],
        actualDocType: [{ required: true, message: '请选择实际发生单据类型', trigger: 'change' }],
        actualDocNumber: [{ required: true, message: '请输入实际单据号', trigger: 'blur' }],
        reverseDesc: [{ required: true, message: '请输入冲销说明', trigger: 'blur' }]
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
          this.$confirm('确认执行冲销操作?冲销将自动生成会计凭证。', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            try {
              const res = await provisionApi.reverse(this.provisionData.provisionId, this.formData)
              if (res.success) {
                this.$message.success('冲销成功,已自动生成冲销凭证')
                this.$emit('success')
                this.handleClose()
              } else {
                this.$message.error(res.message || '冲销失败')
              }
            } catch (error) {
              console.error('冲销失败:', error)
              this.$message.error('冲销失败')
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
        reverseAmount: 0,
        reverseDate: new Date(),
        actualDocType: '',
        actualDocNumber: '',
        reverseDesc: ''
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

