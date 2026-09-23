<template>
  <el-dialog
    title="票据背书转让"
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
        <div>供应商：{{ billData.supplierName || '-' }}</div>
      </div>
    </el-alert>

    <!-- 背书表单 -->
    <el-form :model="form" :rules="rules" ref="endorseForm" label-width="120px">
      <el-row :gutter="20">
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
        <el-col :span="12">
          <el-form-item label="背书金额" prop="endorseAmount">
            <el-input-number v-model="form.endorseAmount" :precision="2" :min="0" style="width: 100%" />
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
          <el-form-item label="被背书人开户行" prop="endorseeBank">
            <el-input v-model="form.endorseeBank" placeholder="请输入开户行" />
          </el-form-item>
        </el-col>
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
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定背书</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as payablesApi from '@/api/financialSharing/payables'

export default {
  name: 'EndorseDialog',
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
      if (val && this.billData.billId) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      // 初始化背书金额为票据金额
      this.form.endorseAmount = this.billData.amount || 0
      // 重置其他字段
      this.form.endorseDate = new Date().toISOString().split('T')[0]
      this.form.endorseeName = ''
      this.form.endorseeBank = ''
      this.form.endorseeAccount = ''
      this.form.endorseType = 1
      this.form.description = ''
      this.form.remarks = ''
    },
    handleClose() {
      this.$refs.endorseForm.resetFields()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.endorseForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.endorseBill(this.billData.billId, this.form)
          this.$message.success('背书成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('背书失败：' + error.message)
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
