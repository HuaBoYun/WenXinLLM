<template>
  <el-dialog
    title="复制表单模板"
    :visible.sync="dialogVisible"
    width="500px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item label="新模板名称" prop="newTemplateName">
        <el-input
          v-model="formData.newTemplateName"
          placeholder="请输入新模板名称"
          maxlength="200"
        />
      </el-form-item>
      <el-form-item label="新版本号" prop="newVersionNo">
        <el-input
          v-model="formData.newVersionNo"
          placeholder="请输入新版本号,如:2.0"
          maxlength="20"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { copyFormTemplate } from '@/api/financialSharing/enterpriseReport/formTemplate'

export default {
  name: 'CopyTemplateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    templateId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      formData: {
        newTemplateName: '',
        newVersionNo: ''
      },
      rules: {
        newTemplateName: [
          { required: true, message: '新模板名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '模板名称长度在1到200个字符', trigger: 'blur' }
        ],
        newVersionNo: [
          { required: true, message: '新版本号不能为空', trigger: 'blur' },
          { min: 1, max: 20, message: '版本号长度在1到20个字符', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        // 重置表单
        this.formData = {
          newTemplateName: '',
          newVersionNo: ''
        }
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          copyFormTemplate(
            this.templateId,
            this.formData.newTemplateName,
            this.formData.newVersionNo
          ).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '复制成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          }).catch(() => {
            this.submitLoading = false
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
</style>

