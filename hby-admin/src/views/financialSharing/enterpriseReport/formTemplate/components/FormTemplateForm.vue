<template>
  <el-dialog
    :title="formData.templateId ? '修改表单模板' : '新增表单模板'"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item label="所属表单组" prop="groupId">
        <el-select
          v-model="formData.groupId"
          placeholder="请选择所属表单组"
          style="width: 100%"
        >
          <el-option
            v-for="item in groupOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编码" prop="templateCode">
        <el-input v-model="formData.templateCode" placeholder="请输入模板编码" maxlength="50" />
      </el-form-item>
      <el-form-item label="模板名称" prop="templateName">
        <el-input v-model="formData.templateName" placeholder="请输入模板名称" maxlength="200" />
      </el-form-item>
      <el-form-item label="模板类型" prop="templateType">
        <el-select v-model="formData.templateType" placeholder="请选择模板类型" style="width: 100%">
          <el-option label="固定表" value="FIXED" />
          <el-option label="浮动表" value="FLOATING" />
        </el-select>
      </el-form-item>
      <el-form-item label="版本号" prop="versionNo">
        <el-input v-model="formData.versionNo" placeholder="请输入版本号" maxlength="20" />
      </el-form-item>
      <el-form-item label="起始期间">
        <el-input v-model="formData.startPeriod" placeholder="请输入起始期间,如:202401" maxlength="20" />
      </el-form-item>
      <el-form-item label="终止期间">
        <el-input v-model="formData.endPeriod" placeholder="请输入终止期间,如:202412" maxlength="20" />
      </el-form-item>
      <el-form-item label="是否默认版本">
        <el-radio-group v-model="formData.isDefault">
          <el-radio label="Y">是</el-radio>
          <el-radio label="N">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="模板内容">
        <el-input
          v-model="formData.templateContent"
          type="textarea"
          :rows="6"
          placeholder="请输入模板内容(JSON格式)"
        />
        <span class="form-tip">模板内容使用JSON格式存储表样设计,后续可通过可视化设计器编辑</span>
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
import { saveFormTemplate } from '@/api/financialSharing/enterpriseReport/formTemplate'

export default {
  name: 'FormTemplateForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    groupOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      rules: {
        groupId: [
          { required: true, message: '所属表单组不能为空', trigger: 'change' }
        ],
        templateCode: [
          { required: true, message: '模板编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '模板编码长度在1到50个字符', trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '模板名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '模板名称长度在1到200个字符', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '模板类型不能为空', trigger: 'change' }
        ],
        versionNo: [
          { required: true, message: '版本号不能为空', trigger: 'blur' }
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
          if (this.formData.templateContent && this.formData.templateContent.trim()) {
            try {
              JSON.parse(this.formData.templateContent)
            } catch (e) {
              this.$message.error('模板内容格式不正确,请输入有效的JSON')
              return
            }
          }
          
          this.submitLoading = true
          saveFormTemplate(this.formData).then(response => {
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

