<template>
  <el-dialog
    title="处理预警"
    :visible.sync="dialogVisible"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="预警信息">
        <div class="alert-info">
          <p><strong>预警类型：</strong>{{ getAlertTypeText(alertData.alertType) }}</p>
          <p><strong>预警级别：</strong>
            <el-tag :type="getAlertLevelTag(alertData.alertLevel)" size="small">
              {{ getAlertLevelText(alertData.alertLevel) }}
            </el-tag>
          </p>
          <p><strong>预警内容：</strong>{{ alertData.alertMessage || '-' }}</p>
        </div>
      </el-form-item>
      <el-form-item label="处理方式" prop="handleType">
        <el-radio-group v-model="form.handleType">
          <el-radio label="HANDLED">标记已处理</el-radio>
          <el-radio label="CLOSED">关闭预警</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="处理人姓名" prop="handlerName">
        <el-input
          v-model="form.handlerName"
          placeholder="请输入处理人姓名"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="处理意见" prop="handleOpinion">
        <el-input
          v-model="form.handleOpinion"
          type="textarea"
          :rows="4"
          placeholder="请输入处理意见..."
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { processRiskAlert } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'AlertProcessDialog',
  props: {
    visible: { type: Boolean, default: false },
    alertData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      submitting: false,
      form: {
        handleType: 'HANDLED',
        handlerName: '',
        handleOpinion: ''
      },
      rules: {
        handleType: [{ required: true, message: '请选择处理方式', trigger: 'change' }],
        handlerName: [{ required: true, message: '请输入处理人姓名', trigger: 'blur' }],
        handleOpinion: [{ required: true, message: '请输入处理意见', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetForm()
      }
    }
  },
  methods: {
    resetForm() {
      this.form = { handleType: 'HANDLED', handlerName: '', handleOpinion: '' }
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
        const params = {
          monitoringId: this.alertData.monitoringId,
          alertStatus: this.form.handleType,
          handlerName: this.form.handlerName,
          handleOpinion: this.form.handleOpinion
        }
        processRiskAlert(params).then(response => {
          if (response && [1, 200, '1', '200'].includes(response.code)) {
            this.$message.success('预警处理成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(response?.msg || '处理失败')
          }
        }).catch(error => {
          console.error('处理预警失败:', error)
          this.$message.error('处理失败，请稍后重试')
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    getAlertTypeText(type) {
      const map = { 'RISK': '风险预警', 'COMPLIANCE': '合规预警', 'PAYMENT': '还款预警', 'MATURITY': '到期预警' }
      return map[type] || type || '-'
    },
    getAlertLevelText(level) {
      const map = { 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return map[level] || level || '-'
    },
    getAlertLevelTag(level) {
      const map = { 'HIGH': 'danger', 'MEDIUM': 'warning', 'LOW': 'success' }
      return map[level] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.alert-info {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  p { margin: 8px 0; line-height: 1.6; }
}
</style>

