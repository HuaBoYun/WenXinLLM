<template>
  <el-dialog
    title="启动项目考核"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-form-item label="项目ID" prop="projectId">
        <el-input
          v-model="form.projectId"
          placeholder="请输入项目ID"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="考核期间" prop="assessmentPeriod">
        <el-date-picker
          v-model="form.assessmentPeriod"
          type="month"
          placeholder="选择考核期间"
          format="yyyy-MM"
          value-format="yyyy-MM"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="考核类型" prop="assessmentType">
        <el-select
          v-model="form.assessmentType"
          placeholder="请选择考核类型"
          style="width: 100%"
        >
          <el-option
            v-for="item in assessmentTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="考核人" prop="assessorId">
        <el-input
          v-model="form.assessorId"
          placeholder="请输入考核人ID"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="自动计算" prop="autoCalculate">
        <el-switch
          v-model="form.autoCalculate"
          active-text="是"
          inactive-text="否"
        />
        <div class="form-tip">
          开启后将自动从各业务模块同步数据并计算考核分数
        </div>
      </el-form-item>
      
      <el-form-item label="同步模块" prop="syncModules" v-if="form.autoCalculate">
        <el-checkbox-group v-model="form.syncModules">
          <el-checkbox label="cost">成本管理</el-checkbox>
          <el-checkbox label="quality">质量管理</el-checkbox>
          <el-checkbox label="safety">安全管理</el-checkbox>
          <el-checkbox label="progress">进度管理</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        启动考核
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { startAssessment, getAssessmentTypeOptions } from '@/api/contract/assessment'

export default {
  name: 'StartAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      submitting: false,
      form: {
        projectId: '',
        assessmentPeriod: '',
        assessmentType: null,
        assessorId: '',
        autoCalculate: true,
        syncModules: ['cost', 'quality', 'safety', 'progress'],
        remarks: ''
      },
      rules: {
        projectId: [
          { required: true, message: '请输入项目ID', trigger: 'blur' }
        ],
        assessmentPeriod: [
          { required: true, message: '请选择考核期间', trigger: 'change' }
        ],
        assessmentType: [
          { required: true, message: '请选择考核类型', trigger: 'change' }
        ],
        assessorId: [
          { required: true, message: '请输入考核人ID', trigger: 'blur' }
        ]
      },
      assessmentTypeOptions: getAssessmentTypeOptions()
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
    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        this.submitting = true
        
        const requestData = {
          ...this.form,
          syncModules: this.form.autoCalculate ? this.form.syncModules : []
        }
        
        const response = await startAssessment(requestData)
        
        if (response.code === 1) {
          this.$message.success('考核启动成功')
          this.$emit('success', response.data)
          this.handleClose()
        } else {
          this.$message.error(response.msg || '启动失败')
        }
      } catch (error) {
        if (error.message) {
          // 表单验证错误
          return
        }
        console.error('启动考核失败:', error)
        this.$message.error('启动失败，请稍后重试')
      } finally {
        this.submitting = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.$refs.form?.resetFields()
      this.form = {
        projectId: '',
        assessmentPeriod: '',
        assessmentType: null,
        assessorId: '',
        autoCalculate: true,
        syncModules: ['cost', 'quality', 'safety', 'progress'],
        remarks: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  line-height: 1.4;
}

.dialog-footer {
  text-align: right;
}
</style>
