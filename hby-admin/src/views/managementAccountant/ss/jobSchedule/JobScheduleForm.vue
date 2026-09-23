<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务编码" prop="jobCode">
            <el-input
              v-model="formData.jobCode"
              placeholder="请输入任务编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="jobName">
            <el-input
              v-model="formData.jobName"
              placeholder="请输入任务名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务类型" prop="jobType">
            <el-select
              v-model="formData.jobType"
              placeholder="请选择任务类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="数据同步" value="DATA_SYNC" />
              <el-option label="报表生成" value="REPORT_GENERATE" />
              <el-option label="数据清理" value="DATA_CLEANUP" />
              <el-option label="系统备份" value="SYSTEM_BACKUP" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务状态" prop="jobStatus">
            <el-select
              v-model="formData.jobStatus"
              placeholder="请选择任务状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="待执行" value="PENDING" />
              <el-option label="执行中" value="RUNNING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已失败" value="FAILED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="执行时间" prop="executeTime">
            <el-date-picker
              v-model="formData.executeTime"
              type="datetime"
              placeholder="请选择执行时间"
              :disabled="isView"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Cron表达式" prop="cronExpression">
            <el-input
              v-model="formData.cronExpression"
              placeholder="请输入Cron表达式"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="任务描述" prop="jobDescription">
            <el-input
              v-model="formData.jobDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入任务描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="执行类" prop="jobClass">
            <el-input
              v-model="formData.jobClass"
              placeholder="请输入执行类"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-input-number
              v-model="formData.priority"
              :min="1"
              :max="10"
              placeholder="请输入优先级"
              :disabled="isView"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'JobScheduleForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    mode: {
      type: String,
      default: 'create' // create, edit, view
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        create: '新增作业调度',
        edit: '编辑作业调度',
        view: '查看作业调度'
      }
      return titleMap[this.mode] || '作业调度'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        jobCode: [
          { required: true, message: '请输入任务编码', trigger: 'blur' }
        ],
        jobName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        jobType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        jobStatus: [
          { required: true, message: '请选择任务状态', trigger: 'change' }
        ],
        executeTime: [
          { required: true, message: '请选择执行时间', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    handleClosed() {
      this.$refs.form.resetFields()
      this.$emit('closed')
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
