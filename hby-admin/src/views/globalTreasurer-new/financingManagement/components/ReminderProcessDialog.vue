<template>
  <el-dialog
    title="处理到期提醒"
    :visible.sync="dialogVisible"
    width="550px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="reminder-info" v-if="reminderData">
      <el-descriptions :column="1" border size="small">
        <el-descriptions-item label="提醒标题">{{ reminderData.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">
          <span :class="getDateClass(reminderData.dueDate)">{{ reminderData.dueDate || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="到期金额">
          <span class="amount">{{ formatAmount(reminderData.amount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="剩余天数">
          <el-tag :type="getDaysTag(reminderData.daysRemaining)" size="small">
            {{ reminderData.daysRemaining || 0 }} 天
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-divider content-position="left">处理操作</el-divider>
    
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="处理方式" prop="processType">
        <el-radio-group v-model="form.processType">
          <el-radio label="CONFIRM">确认还款</el-radio>
          <el-radio label="DELAY">申请延期</el-radio>
          <el-radio label="IGNORE">暂时忽略</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.processType === 'DELAY'" label="延期天数" prop="delayDays">
        <el-input-number v-model="form.delayDays" :min="1" :max="90" />
        <span class="hint">天</span>
      </el-form-item>
      <el-form-item label="处理备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入处理备注..." maxlength="300" show-word-limit />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认处理</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ReminderProcessDialog',
  props: {
    visible: { type: Boolean, default: false },
    reminderData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      submitting: false,
      form: {
        processType: 'CONFIRM',
        delayDays: 7,
        remark: ''
      },
      rules: {
        processType: [{ required: true, message: '请选择处理方式', trigger: 'change' }],
        delayDays: [{ required: true, message: '请输入延期天数', trigger: 'blur' }],
        remark: [{ required: true, message: '请输入处理备注', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.resetForm()
    }
  },
  methods: {
    resetForm() {
      this.form = { processType: 'CONFIRM', delayDays: 7, remark: '' }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.submitting = true
        // 模拟处理
        setTimeout(() => {
          const typeText = { 'CONFIRM': '确认还款', 'DELAY': '申请延期', 'IGNORE': '暂时忽略' }
          this.$message.success(`${typeText[this.form.processType]}处理成功`)
          this.$emit('success')
          this.handleClose()
          this.submitting = false
        }, 500)
      })
    },
    formatAmount(amount) {
      if (!amount) return '-'
      return '¥ ' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    getDateClass(date) {
      if (!date) return ''
      const days = Math.ceil((new Date(date) - new Date()) / (1000 * 60 * 60 * 24))
      if (days <= 7) return 'urgent'
      if (days <= 30) return 'warning'
      return ''
    },
    getDaysTag(days) {
      if (days <= 7) return 'danger'
      if (days <= 30) return 'warning'
      return 'success'
    }
  }
}
</script>

<style lang="scss" scoped>
.reminder-info { margin-bottom: 20px; }
.amount { font-weight: bold; color: #409EFF; font-size: 16px; }
.urgent { color: #F56C6C; font-weight: bold; }
.warning { color: #E6A23C; }
.hint { margin-left: 10px; color: #909399; }
</style>

