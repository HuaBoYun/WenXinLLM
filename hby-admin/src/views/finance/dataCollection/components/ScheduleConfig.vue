<template>
  <el-dialog
    title="定时任务配置"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item label="任务名称">
        <el-input v-model="taskName" disabled />
      </el-form-item>
      <el-form-item label="执行类型" prop="scheduleType">
        <el-radio-group v-model="formData.scheduleType">
          <el-radio label="INTERVAL">间隔时间</el-radio>
          <el-radio label="WEEKLY">每周</el-radio>
          <el-radio label="MONTHLY">每月</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 间隔时间配置 -->
      <el-form-item
        v-if="formData.scheduleType === 'INTERVAL'"
        label="间隔时间"
        prop="intervalValue"
      >
        <el-input-number
          v-model="formData.intervalValue"
          :min="1"
          :max="999"
          style="width: 150px"
        />
        <el-select v-model="formData.intervalUnit" style="width: 100px; margin-left: 10px">
          <el-option label="分钟" value="MINUTE" />
          <el-option label="小时" value="HOUR" />
          <el-option label="天" value="DAY" />
        </el-select>
      </el-form-item>

      <!-- 每周配置 -->
      <el-form-item
        v-if="formData.scheduleType === 'WEEKLY'"
        label="星期"
        prop="weekDays"
      >
        <el-checkbox-group v-model="formData.weekDays">
          <el-checkbox label="1">周一</el-checkbox>
          <el-checkbox label="2">周二</el-checkbox>
          <el-checkbox label="3">周三</el-checkbox>
          <el-checkbox label="4">周四</el-checkbox>
          <el-checkbox label="5">周五</el-checkbox>
          <el-checkbox label="6">周六</el-checkbox>
          <el-checkbox label="7">周日</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 每月配置 -->
      <el-form-item
        v-if="formData.scheduleType === 'MONTHLY'"
        label="日期"
        prop="monthDays"
      >
        <el-select v-model="formData.monthDays" multiple placeholder="请选择日期">
          <el-option
            v-for="day in 31"
            :key="day"
            :label="`${day}日`"
            :value="day"
          />
        </el-select>
      </el-form-item>

      <!-- 执行时间 -->
      <el-form-item
        v-if="formData.scheduleType !== 'INTERVAL'"
        label="执行时间"
        prop="executeTime"
      >
        <el-time-picker
          v-model="formData.executeTime"
          format="HH:mm"
          value-format="HH:mm"
          placeholder="选择时间"
        />
      </el-form-item>

      <el-form-item label="是否启用" prop="enabled">
        <el-switch v-model="formData.enabled" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveScheduleConfig, getScheduleConfig } from '@/api/finance/dataCollection'

export default {
  name: 'ScheduleConfig',
  data() {
    return {
      dialogVisible: false,
      taskId: '',
      taskName: '',
      formData: {
        scheduleType: 'INTERVAL',
        intervalValue: 1,
        intervalUnit: 'HOUR',
        weekDays: [],
        monthDays: [],
        executeTime: '',
        enabled: true,
        remark: ''
      },
      rules: {
        scheduleType: [
          { required: true, message: '请选择执行类型', trigger: 'change' }
        ],
        intervalValue: [
          { required: true, message: '请输入间隔时间', trigger: 'blur' }
        ],
        weekDays: [
          { required: true, message: '请选择星期', trigger: 'change' }
        ],
        monthDays: [
          { required: true, message: '请选择日期', trigger: 'change' }
        ],
        executeTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    async show(row) {
      this.dialogVisible = true
      this.taskId = row.taskId
      this.taskName = row.taskName
      
      // 尝试加载已有配置
      try {
        const res = await getScheduleConfig(row.taskId)
        if (res.code === 1 && res.data) {
          Object.keys(this.formData).forEach(key => {
            if (res.data[key] !== undefined) {
              this.formData[key] = res.data[key]
            }
          })
        }
      } catch (error) {
        console.log('暂无定时任务配置')
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.formData = {
        scheduleType: 'INTERVAL',
        intervalValue: 1,
        intervalUnit: 'HOUR',
        weekDays: [],
        monthDays: [],
        executeTime: '',
        enabled: true,
        remark: ''
      }
      this.$refs.formRef && this.$refs.formRef.clearValidate()
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async valid => {
        if (valid) {
          try {
            const params = {
              taskId: this.taskId,
              ...this.formData
            }
            const res = await saveScheduleConfig(params)
            if (res.code === 1) {
              this.$message.success('定时任务配置成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(res.msg || '配置失败')
            }
          } catch (error) {
            this.$message.error('配置失败')
            console.error(error)
          }
        }
      })
    }
  }
}
</script>

