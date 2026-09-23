<template>
  <el-dialog
    title="审批背书"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <!-- 背书信息展示 -->
    <el-alert
      title="背书信息"
      type="info"
      :closable="false"
      style="margin-bottom: 20px"
    >
      <div style="font-size: 14px">
        <div>背书编号：{{ endorseData.endorseNo || '-' }}</div>
        <div>背书人：{{ endorseData.endorserName || '-' }}</div>
        <div>被背书人：{{ endorseData.endorseeName || '-' }}</div>
        <div>背书金额：<span class="amount-text">{{ formatAmount(endorseData.endorseAmount) }}</span> 元</div>
        <div>背书日期：{{ endorseData.endorseDate || '-' }}</div>
      </div>
    </el-alert>

    <!-- 审批表单 -->
    <el-form :model="form" :rules="rules" ref="approveForm" label-width="100px">
      <el-form-item label="审批结果" prop="approved">
        <el-radio-group v-model="form.approved">
          <el-radio :label="true">通过</el-radio>
          <el-radio :label="false">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="审批意见" prop="comments">
        <el-input
          v-model="form.comments"
          type="textarea"
          :rows="4"
          placeholder="请输入审批意见"
          :maxlength="500"
          show-word-limit
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
  name: 'ApproveEndorseDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    endorseData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        approved: true,
        comments: ''
      },
      rules: {
        approved: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
        comments: [
          { required: true, message: '请输入审批意见', trigger: 'blur' },
          { min: 2, max: 500, message: '长度在2到500个字符', trigger: 'blur' }
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
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        approved: true,
        comments: ''
      }
      if (this.$refs.approveForm) {
        this.$refs.approveForm.resetFields()
      }
    },
    handleClose() {
      this.initForm()
      this.dialogVisible = false
      this.$emit('close')
    },
    handleSubmit() {
      this.$refs.approveForm.validate(async valid => {
        if (!valid) return

        this.loading = true
        try {
          await payablesApi.approveEndorse(
            this.endorseData.endorseId,
            this.form.approved,
            this.form.comments
          )
          this.$message.success('审批成功')
          this.$emit('success')
          this.handleClose()
        } catch (error) {
          this.$message.error('审批失败：' + (error.message || '未知错误'))
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
