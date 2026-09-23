<template>
  <el-dialog
    title="定时任务配置"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="scheduleForm" :model="formData" :rules="rules" label-width="120px">
      <!-- 执行频率 -->
      <el-form-item label="执行频率" prop="frequency">
        <el-radio-group v-model="formData.frequency" @change="handleFrequencyChange">
          <el-radio label="daily">每天</el-radio>
          <el-radio label="weekly">每周</el-radio>
          <el-radio label="monthly">每月</el-radio>
          <el-radio label="custom">自定义</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 每天执行 -->
      <el-form-item v-if="formData.frequency === 'daily'" label="执行时间" prop="dailyTime">
        <el-time-picker
          v-model="formData.dailyTime"
          format="HH:mm"
          value-format="HH:mm"
          placeholder="选择时间"
          style="width: 100%"
        />
        <div class="form-tip">每天在指定时间执行一次</div>
      </el-form-item>

      <!-- 每周执行 -->
      <div v-if="formData.frequency === 'weekly'">
        <el-form-item label="星期" prop="weekDays">
          <el-checkbox-group v-model="formData.weekDays">
            <el-checkbox label="1">星期一</el-checkbox>
            <el-checkbox label="2">星期二</el-checkbox>
            <el-checkbox label="3">星期三</el-checkbox>
            <el-checkbox label="4">星期四</el-checkbox>
            <el-checkbox label="5">星期五</el-checkbox>
            <el-checkbox label="6">星期六</el-checkbox>
            <el-checkbox label="0">星期日</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="执行时间" prop="weeklyTime">
          <el-time-picker
            v-model="formData.weeklyTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 100%"
          />
          <div class="form-tip">在选定的星期几的指定时间执行</div>
        </el-form-item>
      </div>

      <!-- 每月执行 -->
      <div v-if="formData.frequency === 'monthly'">
        <el-form-item label="日期" prop="monthDay">
          <el-select v-model="formData.monthDay" placeholder="请选择日期" style="width: 100%">
            <el-option
              v-for="day in 31"
              :key="day"
              :label="`每月${day}号`"
              :value="day"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间" prop="monthlyTime">
          <el-time-picker
            v-model="formData.monthlyTime"
            format="HH:mm"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 100%"
          />
          <div class="form-tip">每月指定日期的指定时间执行</div>
        </el-form-item>
      </div>

      <!-- 自定义Cron表达式 -->
      <el-form-item v-if="formData.frequency === 'custom'" label="Cron表达式" prop="cronExpression">
        <el-input
          v-model="formData.cronExpression"
          placeholder="请输入Cron表达式，例如：0 0 2 * * ?"
        />
        <div class="form-tip">
          <div>Cron表达式格式：秒 分 时 日 月 周</div>
          <div>示例：</div>
          <div>• 0 0 2 * * ? - 每天凌晨2点执行</div>
          <div>• 0 0 12 * * ? - 每天中午12点执行</div>
          <div>• 0 0 9 ? * MON-FRI - 每周一到周五上午9点执行</div>
        </div>
      </el-form-item>

      <!-- 是否立即执行一次 -->
      <el-form-item label="立即执行">
        <el-switch v-model="formData.executeImmediate" />
        <span class="form-tip" style="margin-left: 10px">启动后立即执行一次评估</span>
      </el-form-item>

      <!-- 任务描述 -->
      <el-form-item label="任务描述">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入任务描述（可选）"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <!-- 预览Cron表达式 -->
      <el-form-item label="Cron表达式">
        <el-input :value="generatedCron" readonly>
          <template slot="append">
            <el-button @click="copyCron" icon="el-icon-document-copy">复制</el-button>
          </template>
        </el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定启动</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ScheduleConfigDialog',
  data() {
    return {
      dialogVisible: false,
      formData: {
        frequency: 'daily',
        dailyTime: '02:00',
        weekDays: [],
        weeklyTime: '02:00',
        monthDay: 1,
        monthlyTime: '02:00',
        cronExpression: '0 0 2 * * ?',
        executeImmediate: false,
        description: ''
      },
      rules: {
        frequency: [
          { required: true, message: '请选择执行频率', trigger: 'change' }
        ],
        dailyTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ],
        weekDays: [
          { required: true, type: 'array', min: 1, message: '请至少选择一个星期', trigger: 'change' }
        ],
        weeklyTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ],
        monthDay: [
          { required: true, message: '请选择日期', trigger: 'change' }
        ],
        monthlyTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ],
        cronExpression: [
          { required: true, message: '请输入Cron表达式', trigger: 'blur' },
          { pattern: /^(\S+\s+){5}\S+$/, message: 'Cron表达式格式不正确', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    generatedCron() {
      const { frequency, dailyTime, weekDays, weeklyTime, monthDay, monthlyTime, cronExpression } = this.formData

      if (frequency === 'daily' && dailyTime) {
        const [hour, minute] = dailyTime.split(':')
        return `0 ${minute} ${hour} * * ?`
      } else if (frequency === 'weekly' && weekDays.length > 0 && weeklyTime) {
        const [hour, minute] = weeklyTime.split(':')
        const days = weekDays.sort((a, b) => a - b).join(',')
        return `0 ${minute} ${hour} ? * ${days}`
      } else if (frequency === 'monthly' && monthDay && monthlyTime) {
        const [hour, minute] = monthlyTime.split(':')
        return `0 ${minute} ${hour} ${monthDay} * ?`
      } else if (frequency === 'custom' && cronExpression) {
        return cronExpression
      }

      return '0 0 2 * * ?'
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.resetForm()
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.formData = {
        frequency: 'daily',
        dailyTime: '02:00',
        weekDays: [],
        weeklyTime: '02:00',
        monthDay: 1,
        monthlyTime: '02:00',
        cronExpression: '0 0 2 * * ?',
        executeImmediate: false,
        description: ''
      }
      if (this.$refs.scheduleForm) {
        this.$refs.scheduleForm.clearValidate()
      }
    },
    handleFrequencyChange() {
      // 清除验证错误
      if (this.$refs.scheduleForm) {
        this.$refs.scheduleForm.clearValidate()
      }
    },
    copyCron() {
      const input = document.createElement('input')
      input.value = this.generatedCron
      document.body.appendChild(input)
      input.select()
      document.execCommand('copy')
      document.body.removeChild(input)
      this.$message.success('Cron表达式已复制到剪贴板')
    },
    handleSubmit() {
      this.$refs.scheduleForm.validate((valid) => {
        if (valid) {
          const scheduleConfig = {
            cronExpression: this.generatedCron,
            executeImmediate: this.formData.executeImmediate,
            description: this.formData.description || '定时风险评估任务',
            frequency: this.formData.frequency,
            config: {
              dailyTime: this.formData.dailyTime,
              weekDays: this.formData.weekDays,
              weeklyTime: this.formData.weeklyTime,
              monthDay: this.formData.monthDay,
              monthlyTime: this.formData.monthlyTime
            }
          }

          this.$emit('submit', scheduleConfig)
          this.handleClose()
        } else {
          this.$message.warning('请完善定时任务配置')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  line-height: 1.5;
}

.dialog-footer {
  text-align: right;
}
</style>

