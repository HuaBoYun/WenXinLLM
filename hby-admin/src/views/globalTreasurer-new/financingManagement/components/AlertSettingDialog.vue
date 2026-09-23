<template>
  <el-dialog
    title="预警设置"
    :visible.sync="dialogVisible"
    width="650px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-divider content-position="left">基本信息</el-divider>
      <el-form-item label="监控项目">
        <el-input :value="monitoringData.companyName || '-'" disabled />
      </el-form-item>
      <el-form-item label="当前预警级别">
        <el-tag :type="getAlertLevelTag(monitoringData.alertLevel)">
          {{ getAlertLevelText(monitoringData.alertLevel) }}
        </el-tag>
      </el-form-item>
      
      <el-divider content-position="left">预警规则配置</el-divider>
      <el-form-item label="启用预警" prop="enableAlert">
        <el-switch v-model="form.enableAlert" active-text="开启" inactive-text="关闭" />
      </el-form-item>
      <el-form-item label="预警阈值" prop="alertThreshold">
        <el-input-number v-model="form.alertThreshold" :min="0" :max="100" :step="5" />
        <span class="threshold-hint">%（超过此阈值触发预警）</span>
      </el-form-item>
      <el-form-item label="预警级别规则">
        <div class="level-rules">
          <div class="rule-item">
            <span class="level-tag high">高风险</span>
            <span>阈值 ≥</span>
            <el-input-number v-model="form.highThreshold" :min="0" :max="100" size="small" />
            <span>%</span>
          </div>
          <div class="rule-item">
            <span class="level-tag medium">中风险</span>
            <span>阈值 ≥</span>
            <el-input-number v-model="form.mediumThreshold" :min="0" :max="100" size="small" />
            <span>%</span>
          </div>
          <div class="rule-item">
            <span class="level-tag low">低风险</span>
            <span>阈值 ≥</span>
            <el-input-number v-model="form.lowThreshold" :min="0" :max="100" size="small" />
            <span>%</span>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="通知方式" prop="notifyMethods">
        <el-checkbox-group v-model="form.notifyMethods">
          <el-checkbox label="SYSTEM">系统消息</el-checkbox>
          <el-checkbox label="EMAIL">邮件通知</el-checkbox>
          <el-checkbox label="SMS">短信通知</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="通知人员">
        <el-input v-model="form.notifyUsers" placeholder="请输入通知人员，多人用逗号分隔" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">保存设置</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'AlertSettingDialog',
  props: {
    visible: { type: Boolean, default: false },
    monitoringData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      submitting: false,
      form: {
        enableAlert: true,
        alertThreshold: 80,
        highThreshold: 90,
        mediumThreshold: 70,
        lowThreshold: 50,
        notifyMethods: ['SYSTEM'],
        notifyUsers: ''
      },
      rules: {
        alertThreshold: [{ required: true, message: '请设置预警阈值', trigger: 'blur' }]
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
      this.form = {
        enableAlert: true, alertThreshold: 80, highThreshold: 90,
        mediumThreshold: 70, lowThreshold: 50, notifyMethods: ['SYSTEM'], notifyUsers: ''
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.submitting = true
        // 模拟保存设置
        setTimeout(() => {
          this.$message.success('预警设置保存成功')
          this.$emit('success')
          this.handleClose()
          this.submitting = false
        }, 500)
      })
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
.threshold-hint { margin-left: 10px; color: #909399; font-size: 12px; }
.level-rules {
  .rule-item {
    display: flex; align-items: center; margin-bottom: 10px; gap: 10px;
    .level-tag {
      display: inline-block; width: 60px; text-align: center; padding: 2px 8px;
      border-radius: 4px; font-size: 12px; color: #fff;
      &.high { background: #F56C6C; }
      &.medium { background: #E6A23C; }
      &.low { background: #67C23A; }
    }
  }
}
</style>

