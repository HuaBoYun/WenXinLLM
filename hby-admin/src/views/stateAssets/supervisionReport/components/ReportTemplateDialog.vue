<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="templateForm"
      :model="templateForm"
      :rules="templateRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模板名称" prop="templateName">
            <el-input
              v-model="templateForm.templateName"
              placeholder="请输入模板名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板类型" prop="templateType">
            <el-select
              v-model="templateForm.templateType"
              placeholder="请选择模板类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="监管报告模板" value="SUPERVISION"></el-option>
              <el-option label="风险报告模板" value="RISK"></el-option>
              <el-option label="财务报告模板" value="FINANCIAL"></el-option>
              <el-option label="合规报告模板" value="COMPLIANCE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="适用周期" prop="applicablePeriod">
            <el-select
              v-model="templateForm.applicablePeriod"
              placeholder="请选择适用周期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="日报" value="DAILY"></el-option>
              <el-option label="周报" value="WEEKLY"></el-option>
              <el-option label="月报" value="MONTHLY"></el-option>
              <el-option label="季报" value="QUARTERLY"></el-option>
              <el-option label="年报" value="YEARLY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板状态" prop="status">
            <el-select
              v-model="templateForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="启用" value="ACTIVE"></el-option>
              <el-option label="停用" value="INACTIVE"></el-option>
              <el-option label="草稿" value="DRAFT"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="模板描述" prop="description">
        <el-input
          v-model="templateForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入模板描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="模板内容" prop="templateContent">
        <el-input
          v-model="templateForm.templateContent"
          type="textarea"
          :rows="10"
          placeholder="请输入模板内容，支持变量替换，如：${企业名称}、${报告日期}等"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="变量说明" prop="variableDescription">
        <el-input
          v-model="templateForm.variableDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入变量说明，如：${企业名称} - 企业的完整名称"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="templateForm.creator"
              placeholder="请输入创建人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="版本号" prop="version">
            <el-input
              v-model="templateForm.version"
              placeholder="请输入版本号"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="标签" prop="tags">
        <el-input
          v-model="templateForm.tags"
          placeholder="请输入标签，多个标签用逗号分隔"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="templateForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        @click="handlePreview"
      >
        预览模板
      </el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        {{ dialogType === 'add' ? '创建模板' : '更新模板' }}
      </el-button>
    </div>

    <!-- 模板预览对话框 -->
    <el-dialog
      title="模板预览"
      :visible.sync="previewVisible"
      width="70%"
      append-to-body
    >
      <div class="template-preview">
        <div class="preview-header">
          <h3>{{ templateForm.templateName }}</h3>
          <p>{{ templateForm.description }}</p>
        </div>
        <div class="preview-content">
          <pre>{{ templateForm.templateContent }}</pre>
        </div>
        <div class="preview-variables" v-if="templateForm.variableDescription">
          <h4>变量说明：</h4>
          <pre>{{ templateForm.variableDescription }}</pre>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { saveReportTemplate } from '@/api/stateAssets/supervisionReport'

export default {
  name: 'ReportTemplateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    templateData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      previewVisible: false,
      templateForm: {
        id: '',
        templateName: '',
        templateType: '',
        applicablePeriod: '',
        status: 'ACTIVE',
        description: '',
        templateContent: '',
        variableDescription: '',
        creator: '',
        version: '1.0',
        tags: '',
        remarks: ''
      },
      templateRules: {
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        applicablePeriod: [
          { required: true, message: '请选择适用周期', trigger: 'change' }
        ],
        templateContent: [
          { required: true, message: '请输入模板内容', trigger: 'blur' }
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
    },
    dialogTitle() {
      const titleMap = {
        add: '新建报告模板',
        edit: '编辑报告模板',
        view: '查看报告模板'
      }
      return titleMap[this.dialogType] || '新建报告模板'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.templateForm = {
          id: '',
          templateName: '',
          templateType: '',
          applicablePeriod: '',
          status: 'ACTIVE',
          description: '',
          templateContent: '',
          variableDescription: '',
          creator: '',
          version: '1.0',
          tags: '',
          remarks: ''
        }
      } else {
        this.templateForm = { ...this.templateData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.templateForm) {
          this.$refs.templateForm.clearValidate()
        }
      })
    },

    handleSubmit() {
      this.$refs.templateForm.validate((valid) => {
        if (valid) {
          this.loading = true
          saveReportTemplate(this.templateForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存报告模板失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handlePreview() {
      if (!this.templateForm.templateContent) {
        this.$message.warning('请先输入模板内容')
        return
      }
      this.previewVisible = true
    },

    handleClose() {
      this.dialogVisible = false
      this.previewVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.template-preview {
  padding: 20px;
}

.preview-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
  padding-bottom: 15px;
}

.preview-header h3 {
  margin: 0 0 10px 0;
  color: #303133;
}

.preview-header p {
  margin: 0;
  color: #606266;
}

.preview-content {
  margin-bottom: 20px;
}

.preview-content pre {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
}

.preview-variables {
  border-top: 1px solid #e4e7ed;
  padding-top: 15px;
}

.preview-variables h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.preview-variables pre {
  background-color: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  padding: 10px;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
  color: #409eff;
}
</style>
