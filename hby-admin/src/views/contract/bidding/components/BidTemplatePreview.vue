<template>
  <el-dialog
    title="标书模板预览"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="template-preview">
      <div class="preview-header">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>模板编号：</label>
              <span>{{ templateData.templateNo }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>模板名称：</label>
              <span>{{ templateData.templateName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>模板版本：</label>
              <span>{{ templateData.templateVersion }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>模板类型：</label>
              <el-tag :type="getTemplateTypeTagType(templateData.templateType)">
                {{ getTemplateTypeName(templateData.templateType) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>模板分类：</label>
              <span>{{ templateData.templateCategory || '无' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>文件格式：</label>
              <span>{{ templateData.templateFormat?.toUpperCase() }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-divider />

      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="info-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="info-group">
                  <h4>模板信息</h4>
                  <div class="info-item">
                    <label>模板描述：</label>
                    <p>{{ templateData.templateDescription || '无' }}</p>
                  </div>
                  <div class="info-item">
                    <label>行业范围：</label>
                    <span>{{ templateData.industryScope || '无' }}</span>
                  </div>
                  <div class="info-item">
                    <label>复杂度等级：</label>
                    <el-rate
                      v-model="templateData.complexityLevel"
                      disabled
                      show-text
                      :texts="['简单', '中等', '复杂', '非常复杂']"
                    />
                  </div>
                  <div class="info-item">
                    <label>预计完成时间：</label>
                    <span>{{ templateData.estimatedCompletionTime || 0 }} 小时</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="info-group">
                  <h4>使用统计</h4>
                  <div class="info-item">
                    <label>使用次数：</label>
                    <span>{{ templateData.usageCount || 0 }} 次</span>
                  </div>
                  <div class="info-item">
                    <label>下载次数：</label>
                    <span>{{ templateData.downloadCount || 0 }} 次</span>
                  </div>
                  <div class="info-item">
                    <label>评分：</label>
                    <el-rate
                      v-model="templateData.ratingScore"
                      disabled
                      show-score
                      text-color="#ff9900"
                      score-template="{value} 分"
                    />
                  </div>
                  <div class="info-item">
                    <label>评分人数：</label>
                    <span>{{ templateData.ratingCount || 0 }} 人</span>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 模板结构 -->
        <el-tab-pane label="模板结构" name="structure">
          <div class="structure-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="structure-group">
                  <h4>必需章节</h4>
                  <div class="section-list">
                    <div
                      v-for="(section, index) in requiredSectionsList"
                      :key="'required-' + index"
                      class="section-item required"
                    >
                      <i class="el-icon-check"></i>
                      {{ section }}
                    </div>
                    <div v-if="requiredSectionsList.length === 0" class="empty-text">
                      暂无必需章节
                    </div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="structure-group">
                  <h4>可选章节</h4>
                  <div class="section-list">
                    <div
                      v-for="(section, index) in optionalSectionsList"
                      :key="'optional-' + index"
                      class="section-item optional"
                    >
                      <i class="el-icon-minus"></i>
                      {{ section }}
                    </div>
                    <div v-if="optionalSectionsList.length === 0" class="empty-text">
                      暂无可选章节
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="templateData.templateStructure">
              <el-col :span="24">
                <div class="structure-group">
                  <h4>模板结构说明</h4>
                  <div class="structure-description">
                    <pre>{{ templateData.templateStructure }}</pre>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 适用范围 -->
        <el-tab-pane label="适用范围" name="scope">
          <div class="scope-section">
            <div class="info-group">
              <h4>适用项目</h4>
              <div class="scope-content">
                <p>{{ templateData.applicableProjects || '无特定限制' }}</p>
              </div>
            </div>
            <div class="info-group">
              <h4>关键词</h4>
              <div class="keywords">
                <el-tag
                  v-for="keyword in keywordsList"
                  :key="keyword"
                  size="small"
                  style="margin-right: 8px; margin-bottom: 8px"
                >
                  {{ keyword }}
                </el-tag>
                <span v-if="keywordsList.length === 0" class="empty-text">暂无关键词</span>
              </div>
            </div>
            <div class="info-group">
              <h4>模板标签</h4>
              <div class="tags">
                <el-tag
                  v-for="tag in tagsList"
                  :key="tag"
                  type="success"
                  size="small"
                  style="margin-right: 8px; margin-bottom: 8px"
                >
                  {{ tag }}
                </el-tag>
                <span v-if="tagsList.length === 0" class="empty-text">暂无标签</span>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 使用说明 -->
        <el-tab-pane label="使用说明" name="instructions">
          <div class="instructions-section">
            <div class="info-group">
              <h4>使用说明</h4>
              <div class="instructions-content">
                <pre>{{ templateData.usageInstructions || '暂无使用说明' }}</pre>
              </div>
            </div>
            <div class="info-group" v-if="templateData.remarks">
              <h4>备注信息</h4>
              <div class="remarks-content">
                <p>{{ templateData.remarks }}</p>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 文件预览 -->
        <el-tab-pane label="文件预览" name="file">
          <div class="file-section">
            <div class="file-info">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="info-item">
                    <label>文件大小：</label>
                    <span>{{ formatFileSize(templateData.fileSize) }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <label>文件格式：</label>
                    <span>{{ templateData.templateFormat?.toUpperCase() }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="info-item">
                    <label>上传时间：</label>
                    <span>{{ formatDate(templateData.createTime) }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="file-preview">
              <div v-if="templateData.previewPath" class="preview-container">
                <iframe
                  :src="templateData.previewPath"
                  width="100%"
                  height="600px"
                  frameborder="0"
                />
              </div>
              <div v-else class="no-preview">
                <i class="el-icon-document"></i>
                <p>暂无预览</p>
                <el-button
                  v-if="templateData.filePath"
                  type="primary"
                  @click="downloadTemplate"
                >
                  下载查看
                </el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="downloadTemplate" v-if="templateData.filePath">
        下载模板
      </el-button>
      <el-button type="success" @click="useTemplate">
        使用模板
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { downloadBidTemplate } from '@/api/contract/bidding'

export default {
  name: 'BidTemplatePreview',
  data() {
    return {
      dialogVisible: false,
      activeTab: 'basic',
      templateData: {}
    }
  },
  computed: {
    requiredSectionsList() {
      if (!this.templateData.requiredSections) return []
      return this.templateData.requiredSections.split('\n').filter(item => item.trim())
    },
    optionalSectionsList() {
      if (!this.templateData.optionalSections) return []
      return this.templateData.optionalSections.split('\n').filter(item => item.trim())
    },
    keywordsList() {
      if (!this.templateData.keywords) return []
      return this.templateData.keywords.split(',').map(item => item.trim()).filter(item => item)
    },
    tagsList() {
      if (!this.templateData.templateTags) return []
      return this.templateData.templateTags.split(',').map(item => item.trim()).filter(item => item)
    }
  },
  methods: {
    showPreview(templateData) {
      this.dialogVisible = true
      this.activeTab = 'basic'
      this.templateData = { ...templateData }
    },

    handleClose() {
      this.dialogVisible = false
      this.templateData = {}
    },

    async downloadTemplate() {
      try {
        const response = await downloadBidTemplate(this.templateData.id)
        // 处理文件下载
        const blob = new Blob([response.data])
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = this.templateData.templateName + '.' + this.templateData.templateFormat
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('下载成功')
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    useTemplate() {
      this.$emit('use-template', this.templateData)
      this.handleClose()
      this.$message.success('已选择模板：' + this.templateData.templateName)
    },

    getTemplateTypeName(type) {
      const typeMap = {
        1: '技术标模板',
        2: '商务标模板',
        3: '综合标模板',
        4: '资格预审模板'
      }
      return typeMap[type] || '未知'
    },

    getTemplateTypeTagType(type) {
      const typeMap = {
        1: 'primary',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return typeMap[type] || 'info'
    },

    formatFileSize(size) {
      if (!size) return '0 KB'
      if (size < 1024) return size + ' KB'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' MB'
      return (size / (1024 * 1024)).toFixed(1) + ' GB'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.template-preview {
  padding: 20px;
}

.preview-header {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 10px;
}

.info-item label {
  font-weight: bold;
  color: #606266;
  margin-right: 8px;
}

.info-section,
.structure-section,
.scope-section,
.instructions-section,
.file-section {
  padding: 20px;
}

.info-group,
.structure-group {
  margin-bottom: 30px;
}

.info-group h4,
.structure-group h4 {
  color: #303133;
  margin-bottom: 15px;
  border-bottom: 2px solid #409eff;
  padding-bottom: 5px;
}

.section-list {
  max-height: 300px;
  overflow-y: auto;
}

.section-item {
  padding: 8px 12px;
  margin-bottom: 8px;
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.section-item.required {
  background-color: #f0f9ff;
  border-left: 4px solid #409eff;
}

.section-item.optional {
  background-color: #f5f7fa;
  border-left: 4px solid #909399;
}

.section-item i {
  margin-right: 8px;
  color: #409eff;
}

.section-item.optional i {
  color: #909399;
}

.structure-description {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.structure-description pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: inherit;
  line-height: 1.6;
}

.scope-content,
.instructions-content,
.remarks-content {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  line-height: 1.6;
}

.keywords,
.tags {
  margin-top: 10px;
}

.file-info {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.preview-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.no-preview {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
}

.no-preview i {
  font-size: 48px;
  margin-bottom: 20px;
}

.empty-text {
  color: #909399;
  font-style: italic;
}

.dialog-footer {
  text-align: right;
}
</style>
