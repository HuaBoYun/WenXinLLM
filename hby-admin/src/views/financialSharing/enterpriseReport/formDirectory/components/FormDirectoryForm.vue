<template>
  <el-dialog
    :title="formData.directoryId ? '修改表单目录' : '新增表单目录'"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="目录编码" prop="directoryCode">
        <el-input v-model="formData.directoryCode" placeholder="请输入目录编码" maxlength="50" />
      </el-form-item>
      <el-form-item label="目录名称" prop="directoryName">
        <el-input v-model="formData.directoryName" placeholder="请输入目录名称" maxlength="100" />
      </el-form-item>
      <el-form-item label="父目录">
        <el-select
          v-model="formData.parentDirectoryId"
          placeholder="请选择父目录"
          clearable
          style="width: 100%"
        >
          <el-option label="无" value="" />
          <el-option
            v-for="item in directoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled || item.value === formData.directoryId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排序号" prop="sortNo">
        <el-input-number
          v-model="formData.sortNo"
          :min="0"
          :max="9999"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio label="ACTIVE">启用</el-radio>
          <el-radio label="INACTIVE">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveDirectory } from '@/api/financialSharing/enterpriseReport/formDirectory'

export default {
  name: 'FormDirectoryForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    directoryOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      rules: {
        directoryCode: [
          { required: true, message: '目录编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '目录编码长度在1到50个字符', trigger: 'blur' }
        ],
        directoryName: [
          { required: true, message: '目录名称不能为空', trigger: 'blur' },
          { min: 1, max: 100, message: '目录名称长度在1到100个字符', trigger: 'blur' }
        ],
        sortNo: [
          { required: true, message: '排序号不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
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
          saveDirectory(this.formData).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '保存成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '保存失败')
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

