<template>
  <el-dialog
    :title="formData.groupId ? '修改表单组' : '新增表单组'"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item label="所属目录" prop="directoryId">
        <el-select
          v-model="formData.directoryId"
          placeholder="请选择所属目录"
          style="width: 100%"
        >
          <el-option
            v-for="item in directoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="表单组编码" prop="groupCode">
        <el-input v-model="formData.groupCode" placeholder="请输入表单组编码" maxlength="50" />
      </el-form-item>
      <el-form-item label="表单组名称" prop="groupName">
        <el-input v-model="formData.groupName" placeholder="请输入表单组名称" maxlength="200" />
      </el-form-item>
      <el-form-item label="固定维">
        <el-input
          v-model="formData.fixedDimensions"
          type="textarea"
          :rows="3"
          placeholder="请输入固定维(JSON数组格式)"
        />
        <span class="form-tip">格式示例: ["主体","期间"]</span>
      </el-form-item>
      <el-form-item label="参数维">
        <el-input
          v-model="formData.parameterDimensions"
          type="textarea"
          :rows="3"
          placeholder="请输入参数维(JSON数组格式)"
        />
        <span class="form-tip">格式示例: ["产品","地区"]</span>
      </el-form-item>
      <el-form-item label="周期类型" prop="periodType">
        <el-select v-model="formData.periodType" placeholder="请选择周期类型" style="width: 100%">
          <el-option label="年" value="YEAR" />
          <el-option label="半年" value="HALF_YEAR" />
          <el-option label="季度" value="QUARTER" />
          <el-option label="月" value="MONTH" />
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
import { saveFormGroup } from '@/api/financialSharing/enterpriseReport/formGroup'

export default {
  name: 'FormGroupForm',
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
        directoryId: [
          { required: true, message: '所属目录不能为空', trigger: 'change' }
        ],
        groupCode: [
          { required: true, message: '表单组编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '表单组编码长度在1到50个字符', trigger: 'blur' }
        ],
        groupName: [
          { required: true, message: '表单组名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '表单组名称长度在1到200个字符', trigger: 'blur' }
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
          // 验证JSON格式
          if (this.formData.fixedDimensions && this.formData.fixedDimensions.trim()) {
            try {
              JSON.parse(this.formData.fixedDimensions)
            } catch (e) {
              this.$message.error('固定维格式不正确,请输入有效的JSON数组')
              return
            }
          }
          if (this.formData.parameterDimensions && this.formData.parameterDimensions.trim()) {
            try {
              JSON.parse(this.formData.parameterDimensions)
            } catch (e) {
              this.$message.error('参数维格式不正确,请输入有效的JSON数组')
              return
            }
          }
          
          this.submitLoading = true
          saveFormGroup(this.formData).then(response => {
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

<style scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}
</style>

