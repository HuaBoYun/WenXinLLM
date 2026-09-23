<template>
  <el-dialog
    title="审核考核结果"
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
      <el-form-item label="审核结果" prop="reviewResult">
        <el-radio-group v-model="form.reviewResult">
          <el-radio label="approved">通过</el-radio>
          <el-radio label="rejected">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="审核意见" prop="reviewComments">
        <el-input
          v-model="form.reviewComments"
          type="textarea"
          :rows="4"
          placeholder="请输入审核意见"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="审核人" prop="reviewerId">
        <el-input
          v-model="form.reviewerId"
          placeholder="请输入审核人ID"
          clearable
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        提交审核
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { reviewAssessment } from '@/api/contract/assessment'

export default {
  name: 'ReviewAssessmentDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assessmentId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      submitting: false,
      form: {
        reviewResult: 'approved',
        reviewComments: '',
        reviewerId: ''
      },
      rules: {
        reviewResult: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        reviewComments: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ],
        reviewerId: [
          { required: true, message: '请输入审核人ID', trigger: 'blur' }
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
  methods: {
    // 提交审核
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        this.submitting = true
        
        const response = await reviewAssessment(this.assessmentId, this.form)
        
        if (response.code === 1) {
          this.$message.success('审核提交成功')
          this.$emit('success', response.data)
          this.handleClose()
        } else {
          this.$message.error(response.msg || '审核提交失败')
        }
      } catch (error) {
        if (error.message) {
          // 表单验证错误
          return
        }
        console.error('审核提交失败:', error)
        this.$message.error('审核提交失败，请稍后重试')
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
        reviewResult: 'approved',
        reviewComments: '',
        reviewerId: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
