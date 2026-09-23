<template>
  <el-dialog
    :title="form.id ? '编辑标书模板' : '新建标书模板'"
    :visible.sync="dialogVisible"
    width="70%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模板编号" prop="templateNo">
                <el-input
                  v-model="form.templateNo"
                  placeholder="请输入模板编号"
                  :disabled="!!form.id"
                >
                  <el-button
                    slot="append"
                    icon="el-icon-refresh"
                    @click="generateTemplateNo"
                    v-if="!form.id"
                  >
                    生成
                  </el-button>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="模板名称" prop="templateName">
                <el-input
                  v-model="form.templateName"
                  placeholder="请输入模板名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模板类型" prop="templateType">
                <el-select
                  v-model="form.templateType"
                  placeholder="请选择模板类型"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in templateTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="模板分类" prop="templateCategory">
                <el-input
                  v-model="form.templateCategory"
                  placeholder="请输入模板分类"
                  maxlength="100"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模板版本" prop="templateVersion">
                <el-input
                  v-model="form.templateVersion"
                  placeholder="请输入模板版本"
                  maxlength="20"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="复杂度等级" prop="complexityLevel">
                <el-select
                  v-model="form.complexityLevel"
                  placeholder="请选择复杂度等级"
                  style="width: 100%"
                >
                  <el-option label="简单" :value="1" />
                  <el-option label="中等" :value="2" />
                  <el-option label="复杂" :value="3" />
                  <el-option label="非常复杂" :value="4" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预计完成时间" prop="estimatedCompletionTime">
                <el-input-number
                  v-model="form.estimatedCompletionTime"
                  placeholder="请输入预计完成时间"
                  :min="1"
                  :max="999"
                  style="width: 100%"
                  controls-position="right"
                >
                  <template slot="append">小时</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="行业范围" prop="industryScope">
                <el-input
                  v-model="form.industryScope"
                  placeholder="请输入行业范围"
                  maxlength="200"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="模板描述" prop="templateDescription">
                <el-input
                  v-model="form.templateDescription"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入模板描述"
                  maxlength="1000"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="使用说明" prop="usageInstructions">
                <el-input
                  v-model="form.usageInstructions"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入使用说明"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 模板结构 -->
        <el-tab-pane label="模板结构" name="structure">
          <el-row>
            <el-col :span="12">
              <el-form-item label="必需章节" prop="requiredSections">
                <el-input
                  v-model="form.requiredSections"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入必需章节，每行一个"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="可选章节" prop="optionalSections">
                <el-input
                  v-model="form.optionalSections"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入可选章节，每行一个"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="模板结构" prop="templateStructure">
                <el-input
                  v-model="form.templateStructure"
                  type="textarea"
                  :rows="6"
                  placeholder="请输入模板结构描述"
                  maxlength="5000"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 文件信息 -->
        <el-tab-pane label="文件信息" name="file">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模板格式" prop="templateFormat">
                <el-select
                  v-model="form.templateFormat"
                  placeholder="请选择模板格式"
                  style="width: 100%"
                >
                  <el-option label="Word文档(.docx)" value="docx" />
                  <el-option label="PDF文档(.pdf)" value="pdf" />
                  <el-option label="Excel表格(.xlsx)" value="xlsx" />
                  <el-option label="PowerPoint(.pptx)" value="pptx" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="文件大小" prop="fileSize">
                <el-input
                  v-model="form.fileSize"
                  placeholder="文件大小（KB）"
                  readonly
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="文件上传">
                <el-upload
                  ref="upload"
                  :action="uploadUrl"
                  :headers="uploadHeaders"
                  :on-success="handleUploadSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeUpload"
                  :file-list="fileList"
                  :limit="1"
                  :on-exceed="handleExceed"
                  drag
                >
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                  <div class="el-upload__tip" slot="tip">
                    只能上传一个文件，且不超过 50MB
                  </div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 其他信息 -->
        <el-tab-pane label="其他信息" name="other">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="适用项目" prop="applicableProjects">
                <el-input
                  v-model="form.applicableProjects"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入适用项目类型"
                  maxlength="1000"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="关键词" prop="keywords">
                <el-input
                  v-model="form.keywords"
                  placeholder="请输入关键词，用逗号分隔"
                  maxlength="500"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模板标签" prop="templateTags">
                <el-input
                  v-model="form.templateTags"
                  placeholder="请输入模板标签，用逗号分隔"
                  maxlength="500"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="关联模板" prop="relatedTemplates">
                <el-input
                  v-model="form.relatedTemplates"
                  placeholder="请输入关联模板ID，用逗号分隔"
                  maxlength="500"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="是否默认模板">
                <el-switch
                  v-model="form.isDefault"
                  :active-value="1"
                  :inactive-value="0"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否启用">
                <el-switch
                  v-model="form.isEnabled"
                  :active-value="1"
                  :inactive-value="0"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input
                  v-model="form.remarks"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注信息"
                  maxlength="1000"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="saveLoading" @click="handleSave">
        {{ form.id ? '更新' : '保存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  createBidTemplate,
  updateBidTemplate,
  generateTemplateNo
} from '@/api/contract/bidding'
import { getToken } from '@/utils/token'

export default {
  name: 'BidTemplateForm',
  data() {
    return {
      dialogVisible: false,
      saveLoading: false,
      activeTab: 'basic',
      form: {
        id: null,
        templateNo: '',
        templateName: '',
        templateType: null,
        templateCategory: '',
        templateVersion: '1.0',
        templateDescription: '',
        templateContent: '',
        templateStructure: '',
        requiredSections: '',
        optionalSections: '',
        templateFormat: 'docx',
        fileSize: null,
        filePath: '',
        previewPath: '',
        templateStatus: 1,
        isDefault: 0,
        isEnabled: 1,
        applicableProjects: '',
        industryScope: '',
        complexityLevel: 1,
        estimatedCompletionTime: null,
        templateTags: '',
        keywords: '',
        relatedTemplates: '',
        usageInstructions: '',
        remarks: ''
      },
      rules: {
        templateNo: [
          { required: true, message: '请输入模板编号', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' },
          { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        templateVersion: [
          { required: true, message: '请输入模板版本', trigger: 'blur' }
        ]
      },
      templateTypeOptions: [
        { label: '技术标模板', value: 1 },
        { label: '商务标模板', value: 2 },
        { label: '综合标模板', value: 3 },
        { label: '资格预审模板', value: 4 }
      ],
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/contract/bidding/template/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  methods: {
    showEdit(row) {
      this.dialogVisible = true
      this.activeTab = 'basic'
      if (row) {
        this.form = { ...row }
        if (row.filePath) {
          this.fileList = [{
            name: row.templateName + '.' + row.templateFormat,
            url: row.filePath
          }]
        }
      } else {
        this.resetForm()
      }
    },

    resetForm() {
      this.form = {
        id: null,
        templateNo: '',
        templateName: '',
        templateType: null,
        templateCategory: '',
        templateVersion: '1.0',
        templateDescription: '',
        templateContent: '',
        templateStructure: '',
        requiredSections: '',
        optionalSections: '',
        templateFormat: 'docx',
        fileSize: null,
        filePath: '',
        previewPath: '',
        templateStatus: 1,
        isDefault: 0,
        isEnabled: 1,
        applicableProjects: '',
        industryScope: '',
        complexityLevel: 1,
        estimatedCompletionTime: null,
        templateTags: '',
        keywords: '',
        relatedTemplates: '',
        usageInstructions: '',
        remarks: ''
      }
      this.fileList = []
    },

    async generateTemplateNo() {
      try {
        const response = await generateTemplateNo()
        if (response.code === 200) {
          this.form.templateNo = response.data
        } else {
          this.$message.error(response.message || '生成模板编号失败')
        }
      } catch (error) {
        this.$message.error('生成模板编号失败：' + error.message)
      }
    },

    beforeUpload(file) {
      const isValidType = [
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'application/pdf',
        'application/vnd.ms-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'application/vnd.ms-powerpoint',
        'application/vnd.openxmlformats-officedocument.presentationml.presentation'
      ].includes(file.type)
      const isLt50M = file.size / 1024 / 1024 < 50

      if (!isValidType) {
        this.$message.error('只能上传 DOC/DOCX/PDF/XLS/XLSX/PPT/PPTX 格式的文件!')
        return false
      }
      if (!isLt50M) {
        this.$message.error('上传文件大小不能超过 50MB!')
        return false
      }

      // 设置文件大小
      this.form.fileSize = Math.round(file.size / 1024)
      return true
    },

    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.form.filePath = response.data.filePath
        this.form.previewPath = response.data.previewPath
        this.$message.success('文件上传成功')
      } else {
        this.$message.error('文件上传失败：' + response.message)
      }
    },

    handleUploadError(err, file, fileList) {
      this.$message.error('文件上传失败')
    },

    handleExceed(files, fileList) {
      this.$message.warning('只能上传一个文件')
    },

    async handleSave() {
      try {
        await this.$refs.form.validate()

        this.saveLoading = true

        let response
        if (this.form.id) {
          response = await updateBidTemplate(this.form.id, this.form)
        } else {
          response = await createBidTemplate(this.form)
        }

        if (response.code === 200) {
          this.$message.success(this.form.id ? '更新成功' : '创建成功')
          this.handleClose()
          this.$emit('fetch-data')
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        if (error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    handleClose() {
      this.dialogVisible = false
      this.saveLoading = false
      this.activeTab = 'basic'
      this.$refs.form.resetFields()
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.el-upload__tip {
  color: #606266;
  font-size: 12px;
  margin-top: 7px;
}
</style>
