<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="approvalForm"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      size="small"
    >
      <el-form-item label="审批结果" prop="approvalResult">
        <el-radio-group v-model="formData.approvalResult">
          <el-radio label="APPROVED">通过</el-radio>
          <el-radio label="REJECTED">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审批意见" prop="approvalComment">
        <el-input
          v-model="formData.approvalComment"
          type="textarea"
          :rows="4"
          placeholder="请输入审批意见"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ApprovalDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '审批'
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        approvalResult: 'APPROVED',
        approvalComment: ''
      },
      formRules: {
        approvalResult: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
        approvalComment: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.approvalForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.approvalForm.resetFields()
      this.$emit('update:visible', false)
    }
  }
}
</script>

