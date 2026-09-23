<template>
  <el-dialog
    title="批量导入任务"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <div class="import-container">
      <!-- 步骤条 -->
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="下载模板" description="下载导入模板"></el-step>
        <el-step title="上传文件" description="选择并上传文件"></el-step>
        <el-step title="导入结果" description="查看导入结果"></el-step>
      </el-steps>

      <!-- 步骤1：下载模板 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="template-info">
          <el-alert
            title="导入说明"
            type="info"
            :closable="false"
            show-icon
          >
            <div slot="description">
              <p>1. 请先下载导入模板，按照模板格式填写数据</p>
              <p>2. 带*号的字段为必填项，请确保数据完整</p>
              <p>3. 任务名称不能重复</p>
              <p>4. 日期格式为：YYYY-MM-DD</p>
              <p>5. 支持的文件格式：.xls, .xlsx</p>
            </div>
          </el-alert>
        </div>
        
        <div class="template-download">
          <el-button
            type="primary"
            @click="handleDownloadTemplate"
            :loading="downloadLoading"
            icon="el-icon-download"
            size="large"
          >
            下载导入模板
          </el-button>
        </div>
      </div>

      <!-- 步骤2：上传文件 -->
      <div v-if="currentStep === 1" class="step-content">
        <el-upload
          ref="upload"
          class="upload-demo"
          drag
          :action="uploadAction"
          :before-upload="beforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :file-list="fileList"
          :auto-upload="false"
          accept=".xls,.xlsx"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件，且不超过10MB</div>
        </el-upload>

        <div class="upload-actions">
          <el-button @click="handleClearFiles">清空文件</el-button>
          <el-button
            type="primary"
            @click="handleStartUpload"
            :loading="uploadLoading"
            :disabled="fileList.length === 0"
          >
            开始导入
          </el-button>
        </div>
      </div>

      <!-- 步骤3：导入结果 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="import-result">
          <el-result
            :icon="importResult.successCount > 0 ? 'success' : 'error'"
            :title="importResult.successCount > 0 ? '导入完成' : '导入失败'"
            :sub-title="getResultSubTitle()"
          >
            <template slot="extra">
              <div class="result-stats">
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-number">{{ importResult.totalCount || 0 }}</div>
                      <div class="stat-label">总数量</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="stat-item success">
                      <div class="stat-number">{{ importResult.successCount || 0 }}</div>
                      <div class="stat-label">成功</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="stat-item error">
                      <div class="stat-number">{{ importResult.failCount || 0 }}</div>
                      <div class="stat-label">失败</div>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <!-- 错误信息 -->
              <div v-if="importResult.errorMessages && importResult.errorMessages.length > 0" class="error-messages">
                <h4>错误详情：</h4>
                <el-scrollbar style="height: 200px;">
                  <ul>
                    <li v-for="(error, index) in importResult.errorMessages" :key="index" class="error-item">
                      {{ error }}
                    </li>
                  </ul>
                </el-scrollbar>
              </div>
            </template>
          </el-result>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button v-if="currentStep > 0" @click="handlePrevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" @click="handleNextStep" :disabled="!canNextStep">下一步</el-button>
      <el-button v-if="currentStep === 2" type="primary" @click="handleFinish">完成</el-button>
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { downloadTaskTemplate, importTaskList } from '@/api/stateAssets/dataCollection'

export default {
  name: 'ImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStep: 0,
      downloadLoading: false,
      uploadLoading: false,
      fileList: [],
      uploadAction: '', // 实际上不会用到，因为我们使用自定义上传
      importResult: {
        totalCount: 0,
        successCount: 0,
        failCount: 0,
        errorMessages: []
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
    canNextStep() {
      if (this.currentStep === 0) {
        return true // 第一步总是可以进入下一步
      } else if (this.currentStep === 1) {
        return this.fileList.length > 0 // 第二步需要有文件
      }
      return false
    }
  },
  methods: {
    // 下载模板
    async handleDownloadTemplate() {
      this.downloadLoading = true
      try {
        const response = await downloadTaskTemplate()
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '数据报送任务导入模板.xls'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败')
      } finally {
        this.downloadLoading = false
      }
    },

    // 上传前检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.ms-excel' || 
                     file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return false // 阻止自动上传
    },

    // 开始上传
    async handleStartUpload() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要上传的文件')
        return
      }

      this.uploadLoading = true
      try {
        const file = this.fileList[0].raw
        const formData = new FormData()
        formData.append('file', file)

        const { data } = await importTaskList(formData)
        this.importResult = data
        this.currentStep = 2
        
        if (data.successCount > 0) {
          this.$message.success('导入完成')
        } else {
          this.$message.error('导入失败')
        }
      } catch (error) {
        this.$message.error('导入失败')
        this.importResult = {
          totalCount: 0,
          successCount: 0,
          failCount: 1,
          errorMessages: ['导入过程中发生错误，请检查文件格式和内容']
        }
        this.currentStep = 2
      } finally {
        this.uploadLoading = false
      }
    },

    // 上传成功（实际不会触发，因为我们阻止了自动上传）
    handleUploadSuccess(response, file, fileList) {
      // 这个方法实际不会被调用
    },

    // 上传失败
    handleUploadError(err, file, fileList) {
      this.$message.error('文件上传失败')
    },

    // 清空文件
    handleClearFiles() {
      this.$refs.upload.clearFiles()
      this.fileList = []
    },

    // 上一步
    handlePrevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },

    // 下一步
    handleNextStep() {
      if (this.currentStep < 2) {
        this.currentStep++
      }
    },

    // 完成
    handleFinish() {
      this.$emit('success')
      this.handleClose()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    },

    // 对话框关闭后的回调
    handleClosed() {
      // 重置状态
      this.currentStep = 0
      this.fileList = []
      this.importResult = {
        totalCount: 0,
        successCount: 0,
        failCount: 0,
        errorMessages: []
      }
      this.downloadLoading = false
      this.uploadLoading = false
      
      // 清空上传组件
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles()
      }
    },

    // 获取结果副标题
    getResultSubTitle() {
      const { totalCount, successCount, failCount } = this.importResult
      if (totalCount === 0) {
        return '没有数据被处理'
      }
      return `共处理 ${totalCount} 条数据，成功 ${successCount} 条，失败 ${failCount} 条`
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.currentStep = 0
      }
    }
  }
}
</script>

<style scoped>
.import-container {
  padding: 20px 0;
}

.step-content {
  margin-top: 30px;
  min-height: 300px;
}

.template-info {
  margin-bottom: 30px;
}

.template-download {
  text-align: center;
  padding: 50px 0;
}

.upload-demo {
  margin-bottom: 20px;
}

.upload-actions {
  text-align: center;
}

.import-result {
  text-align: center;
}

.result-stats {
  margin: 20px 0;
}

.stat-item {
  text-align: center;
  padding: 15px;
  border-radius: 4px;
  background: #f8f9fa;
}

.stat-item.success {
  background: #f0f9ff;
  color: #67c23a;
}

.stat-item.error {
  background: #fef0f0;
  color: #f56c6c;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
}

.error-messages {
  margin-top: 20px;
  text-align: left;
}

.error-messages h4 {
  margin-bottom: 10px;
  color: #f56c6c;
}

.error-messages ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.error-item {
  padding: 5px 0;
  border-bottom: 1px solid #ebeef5;
  color: #f56c6c;
  font-size: 12px;
}

.dialog-footer {
  text-align: right;
}
</style>
