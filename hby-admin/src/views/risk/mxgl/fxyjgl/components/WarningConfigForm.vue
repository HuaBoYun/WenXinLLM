<template>
  <div class="warning-config-form">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="150px"
      size="medium"
    >
      <el-card header="基础配置">
        <el-form-item label="自动处理启用" prop="autoProcessEnabled">
          <el-switch
            v-model="form.autoProcessEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="通知推送启用" prop="notificationEnabled">
          <el-switch
            v-model="form.notificationEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="预警升级启用" prop="escalationEnabled">
          <el-switch
            v-model="form.escalationEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-card>

      <el-card header="风险阈值配置" style="margin-top: 20px;">
        <el-form-item label="高风险阈值" prop="highRiskThreshold">
          <el-input-number
            v-model="form.highRiskThreshold"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="高风险阈值"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">分</span>
        </el-form-item>

        <el-form-item label="中风险阈值" prop="mediumRiskThreshold">
          <el-input-number
            v-model="form.mediumRiskThreshold"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="中风险阈值"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">分</span>
        </el-form-item>

        <el-form-item label="低风险阈值" prop="lowRiskThreshold">
          <el-input-number
            v-model="form.lowRiskThreshold"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="低风险阈值"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">分</span>
        </el-form-item>
      </el-card>

      <el-card header="时间配置" style="margin-top: 20px;">
        <el-form-item label="自动处理延迟" prop="autoProcessDelay">
          <el-input-number
            v-model="form.autoProcessDelay"
            :min="1"
            :max="168"
            placeholder="自动处理延迟"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">小时</span>
        </el-form-item>

        <el-form-item label="预警升级延迟" prop="escalationDelay">
          <el-input-number
            v-model="form.escalationDelay"
            :min="1"
            :max="168"
            placeholder="预警升级延迟"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">小时</span>
        </el-form-item>

        <el-form-item label="预警保留时间" prop="warningRetentionDays">
          <el-input-number
            v-model="form.warningRetentionDays"
            :min="30"
            :max="365"
            placeholder="预警保留时间"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">天</span>
        </el-form-item>
      </el-card>

      <el-card header="通知配置" style="margin-top: 20px;">
        <el-form-item label="通知方式">
          <el-checkbox-group v-model="form.notificationMethods">
            <el-checkbox label="EMAIL">邮件通知</el-checkbox>
            <el-checkbox label="SMS">短信通知</el-checkbox>
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
            <el-checkbox label="WEBHOOK">Webhook通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="通知频率" prop="notificationFrequency">
          <el-select v-model="form.notificationFrequency" placeholder="请选择通知频率" style="width: 200px">
            <el-option label="立即通知" value="IMMEDIATE" />
            <el-option label="每小时汇总" value="HOURLY" />
            <el-option label="每日汇总" value="DAILY" />
            <el-option label="每周汇总" value="WEEKLY" />
          </el-select>
        </el-form-item>

        <el-form-item label="通知接收人">
          <el-input
            v-model="form.notificationRecipients"
            type="textarea"
            :rows="3"
            placeholder="请输入通知接收人邮箱，多个邮箱用逗号分隔"
          />
        </el-form-item>
      </el-card>

      <el-card header="高级配置" style="margin-top: 20px;">
        <el-form-item label="重复预警过滤" prop="duplicateFilterEnabled">
          <el-switch
            v-model="form.duplicateFilterEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="重复时间窗口" prop="duplicateTimeWindow" v-if="form.duplicateFilterEnabled">
          <el-input-number
            v-model="form.duplicateTimeWindow"
            :min="1"
            :max="24"
            placeholder="重复时间窗口"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #666;">小时</span>
        </el-form-item>

        <el-form-item label="智能分析启用" prop="intelligentAnalysisEnabled">
          <el-switch
            v-model="form.intelligentAnalysisEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="机器学习优化" prop="mlOptimizationEnabled">
          <el-switch
            v-model="form.mlOptimizationEnabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="自定义规则">
          <el-input
            v-model="form.customRules"
            type="textarea"
            :rows="4"
            placeholder="请输入自定义预警规则（JSON格式）"
          />
        </el-form-item>
      </el-card>
    </el-form>

    <div class="form-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        保存配置
      </el-button>
    </div>
  </div>
</template>

<script>
import { getWarningConfig } from '@/api/mxgl'

export default {
  name: 'WarningConfigForm',
  data() {
    return {
      submitting: false,
      form: {
        autoProcessEnabled: true,
        notificationEnabled: true,
        escalationEnabled: true,
        highRiskThreshold: 80,
        mediumRiskThreshold: 60,
        lowRiskThreshold: 40,
        autoProcessDelay: 24,
        escalationDelay: 72,
        warningRetentionDays: 90,
        notificationMethods: ['EMAIL', 'SYSTEM'],
        notificationFrequency: 'IMMEDIATE',
        notificationRecipients: '',
        duplicateFilterEnabled: true,
        duplicateTimeWindow: 2,
        intelligentAnalysisEnabled: false,
        mlOptimizationEnabled: false,
        customRules: ''
      },
      rules: {
        highRiskThreshold: [
          { required: true, message: '请输入高风险阈值', trigger: 'blur' }
        ],
        mediumRiskThreshold: [
          { required: true, message: '请输入中风险阈值', trigger: 'blur' }
        ],
        lowRiskThreshold: [
          { required: true, message: '请输入低风险阈值', trigger: 'blur' }
        ],
        autoProcessDelay: [
          { required: true, message: '请输入自动处理延迟时间', trigger: 'blur' }
        ],
        escalationDelay: [
          { required: true, message: '请输入预警升级延迟时间', trigger: 'blur' }
        ],
        notificationFrequency: [
          { required: true, message: '请选择通知频率', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadConfig()
  },
  methods: {
    // 加载配置
    async loadConfig() {
      try {
        const response = await getWarningConfig()
        if (response.code === 1) {
          this.form = { ...this.form, ...response.data }
        }
      } catch (error) {
        console.error('加载配置失败', error)
      }
    },
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 验证阈值设置的合理性
          if (this.form.highRiskThreshold <= this.form.mediumRiskThreshold) {
            this.$message.error('高风险阈值必须大于中风险阈值')
            return
          }
          if (this.form.mediumRiskThreshold <= this.form.lowRiskThreshold) {
            this.$message.error('中风险阈值必须大于低风险阈值')
            return
          }

          // 验证自定义规则JSON格式
          if (this.form.customRules) {
            try {
              JSON.parse(this.form.customRules)
            } catch (error) {
              this.$message.error('自定义规则格式不正确，请输入有效的JSON格式')
              return
            }
          }

          this.submitting = true
          this.$emit('submit', { ...this.form })
          this.submitting = false
        } else {
          this.$message.error('请完善表单信息')
        }
      })
    },
    // 取消
    handleCancel() {
      this.$emit('cancel')
    },
    // 重置
    handleReset() {
      this.$refs.form.resetFields()
      this.loadConfig()
    }
  }
}
</script>

<style lang="scss" scoped>
.warning-config-form {
  .form-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    
    .el-button {
      margin-left: 10px;
    }
  }
}
</style>
