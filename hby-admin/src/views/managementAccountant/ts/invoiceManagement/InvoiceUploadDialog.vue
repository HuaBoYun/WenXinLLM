<template>
  <el-dialog
    title="发票上传"
    :visible.sync="visible"
    width="600px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <div class="upload-container">
      <!-- 上传区域 -->
      <el-upload
        ref="upload"
        class="upload-demo"
        drag
        :action="uploadUrl"
        :headers="uploadHeaders"
        :data="uploadData"
        :file-list="fileList"
        :before-upload="beforeUpload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :on-remove="handleRemove"
        :auto-upload="false"
        multiple
        accept=".pdf,.jpg,.jpeg,.png"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">
          支持 PDF、JPG、PNG 格式，单个文件不超过 10MB
        </div>
      </el-upload>

      <!-- 上传配置 -->
      <div class="upload-config">
        <el-form :model="uploadConfig" label-width="100px" size="small">
          <el-form-item label="发票类型">
            <el-select v-model="uploadConfig.invoiceType" placeholder="请选择发票类型">
              <el-option label="增值税专用发票" value="VAT_SPECIAL" />
              <el-option label="增值税普通发票" value="VAT_ORDINARY" />
              <el-option label="电子发票" value="ELECTRONIC" />
              <el-option label="其他发票" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="自动识别">
            <el-switch v-model="uploadConfig.autoRecognition" />
            <span class="config-tip">开启后将自动识别发票信息</span>
          </el-form-item>
          <el-form-item label="自动验证">
            <el-switch v-model="uploadConfig.autoVerification" />
            <span class="config-tip">开启后将自动验证发票真伪</span>
          </el-form-item>
        </el-form>
      </div>

      <!-- 上传进度 -->
      <div v-if="uploading" class="upload-progress">
        <el-progress
          :percentage="uploadProgress"
          :status="uploadStatus"
          :stroke-width="6"
        />
        <p class="progress-text">{{ progressText }}</p>
      </div>

      <!-- 上传结果 -->
      <div v-if="uploadResults.length > 0" class="upload-results">
        <h4>上传结果</h4>
        <el-table :data="uploadResults" size="small" border>
          <el-table-column prop="fileName" label="文件名" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.status === 'success' ? 'success' : 'danger'"
                size="mini"
              >
                {{ scope.row.status === 'success' ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="message" label="说明" />
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleUpload" :loading="uploading">
        {{ uploading ? '上传中...' : '开始上传' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'InvoiceUploadDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      fileList: [],
      uploading: false,
      uploadProgress: 0,
      uploadStatus: '',
      progressText: '',
      uploadResults: [],
      uploadConfig: {
        invoiceType: '',
        autoRecognition: true,
        autoVerification: true
      },
      uploadUrl: '/api/invoice/upload',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
      },
      uploadData: {}
    }
  },
  methods: {
    beforeUpload(file) {
      const isValidType = ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('只能上传 PDF、JPG、PNG 格式的文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },
    handleUpload() {
      if (this.fileList.length === 0) {
        this.$message.warning('请先选择要上传的文件')
        return
      }
      
      this.uploading = true
      this.uploadProgress = 0
      this.uploadStatus = ''
      this.progressText = '准备上传...'
      this.uploadResults = []
      
      // 设置上传参数
      this.uploadData = {
        invoiceType: this.uploadConfig.invoiceType,
        autoRecognition: this.uploadConfig.autoRecognition,
        autoVerification: this.uploadConfig.autoVerification
      }
      
      // 开始上传
      this.$refs.upload.submit()
    },
    handleUploadSuccess(response, file, fileList) {
      this.uploadResults.push({
        fileName: file.name,
        status: 'success',
        message: '上传成功'
      })
      this.updateProgress()
    },
    handleUploadError(err, file, fileList) {
      this.uploadResults.push({
        fileName: file.name,
        status: 'error',
        message: '上传失败: ' + (err.message || '未知错误')
      })
      this.updateProgress()
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    updateProgress() {
      const total = this.fileList.length
      const completed = this.uploadResults.length
      this.uploadProgress = Math.round((completed / total) * 100)
      this.progressText = `已处理 ${completed}/${total} 个文件`
      
      if (completed === total) {
        this.uploading = false
        const successCount = this.uploadResults.filter(r => r.status === 'success').length
        if (successCount === total) {
          this.uploadStatus = 'success'
          this.$message.success(`所有文件上传成功！`)
        } else {
          this.uploadStatus = 'exception'
          this.$message.warning(`${successCount}/${total} 个文件上传成功`)
        }
        this.$emit('upload-complete', this.uploadResults)
      }
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    handleClosed() {
      this.fileList = []
      this.uploading = false
      this.uploadProgress = 0
      this.uploadStatus = ''
      this.progressText = ''
      this.uploadResults = []
      this.uploadConfig = {
        invoiceType: '',
        autoRecognition: true,
        autoVerification: true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-container {
  .upload-demo {
    margin-bottom: 20px;
  }
  
  .upload-config {
    background: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    
    .config-tip {
      margin-left: 10px;
      font-size: 12px;
      color: #909399;
    }
  }
  
  .upload-progress {
    margin-bottom: 20px;
    
    .progress-text {
      text-align: center;
      margin-top: 10px;
      color: #606266;
      font-size: 14px;
    }
  }
  
  .upload-results {
    h4 {
      margin: 0 0 10px 0;
      color: #303133;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
