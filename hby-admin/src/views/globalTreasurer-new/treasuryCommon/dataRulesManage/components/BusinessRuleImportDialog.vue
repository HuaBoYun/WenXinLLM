<template>
  <el-dialog
    title="批量导入业务规则"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="import-content">
      <el-steps :active="currentStep" finish-status="success" simple>
        <el-step title="下载模板" />
        <el-step title="上传文件" />
        <el-step title="导入数据" />
      </el-steps>

      <!-- 步骤1：下载模板 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="template-download">
          <el-alert
            title="请先下载模板文件，按照模板格式填写数据后再上传"
            type="info"
            show-icon
            :closable="false"
          />
          <div class="template-actions">
            <el-button type="primary" icon="el-icon-download" @click="downloadTemplate">
              下载Excel模板
            </el-button>
            <el-button type="success" icon="el-icon-download" @click="downloadSample">
              下载示例数据
            </el-button>
          </div>
        </div>
      </div>

      <!-- 步骤2：上传文件 -->
      <div v-if="currentStep === 1" class="step-content">
        <el-upload
          ref="upload"
          class="upload-demo"
          drag
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :file-list="fileList"
          :limit="1"
          accept=".xlsx,.xls"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <div class="el-upload__tip" slot="tip">
            只能上传 Excel 文件，且不超过 10MB
          </div>
        </el-upload>

        <div class="upload-tips">
          <h4>导入说明：</h4>
          <ul>
            <li>支持 .xlsx 和 .xls 格式的Excel文件</li>
            <li>请确保数据格式与模板一致</li>
            <li>规则编码不能重复</li>
            <li>必填字段不能为空</li>
          </ul>
        </div>
      </div>

      <!-- 步骤3：导入数据 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="import-result">
          <el-progress
            :percentage="importProgress"
            :status="importStatus"
          />
          <div class="result-info">
            <p v-if="importStatus === 'success'">
              <i class="el-icon-success"></i>
              导入成功！共导入 {{ successCount }} 条数据
            </p>
            <p v-else-if="importStatus === 'exception'">
              <i class="el-icon-error"></i>
              导入失败：{{ errorMessage }}
            </p>
            <p v-else>
              <i class="el-icon-loading"></i>
              正在导入数据，请稍候...
            </p>
          </div>
        </div>

        <!-- 错误信息展示 -->
        <div v-if="errorList.length > 0" class="error-list">
          <h4>错误详情：</h4>
          <el-table :data="errorList" border style="width: 100%">
            <el-table-column label="行号" prop="row" width="80" />
            <el-table-column label="错误信息" prop="message" />
          </el-table>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="currentStep > 0" @click="previousStep">上一步</el-button>
      <el-button
        v-if="currentStep < 2"
        type="primary"
        @click="nextStep"
        :disabled="currentStep === 1 && fileList.length === 0"
      >
        下一步
      </el-button>
      <el-button
        v-if="currentStep === 1"
        type="success"
        @click="startImport"
        :loading="importing"
      >
        开始导入
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BusinessRuleImportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      currentStep: 0,
      fileList: [],
      importing: false,
      importProgress: 0,
      importStatus: '',
      successCount: 0,
      errorMessage: '',
      errorList: []
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetImport()
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    resetImport() {
      this.currentStep = 0
      this.fileList = []
      this.importProgress = 0
      this.importStatus = ''
      this.successCount = 0
      this.errorMessage = ''
      this.errorList = []
    },
    nextStep() {
      if (this.currentStep < 2) {
        this.currentStep++
      }
    },
    previousStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    downloadTemplate() {
      // 模拟下载模板
      const link = document.createElement('a')
      link.href = 'data:application/vnd.ms-excel;base64,UEsDBBQAAAgIAAAAAAAAAAAAAAAAAAAAAAAQAAAAZGF0YS54bWxccVJBjoMwDH3vg9g66BB7SJ4rTbIVh2t+g3ZsybZtJvsQm4B9qCIfz7+gKGktPnZHOXP59nZ2dkVEokEgUDgdDrFYhGDwQAVCoVCoVBqNpvNZnM6nWaz2Xg8Ho1Gw+EIw+E4nU6nUq1WazabTqfTarU6ne7xeDweD0ej0Wg0Gk1Go9FwOBwOh0Oh0Og0Oo1Go9Hr9VqtVqvVarVardfrtVqtVrvd7nQ6nU6n1+v1+oVCoVCoVCqVCoVCoVAoFAqFQqFQKBQKhUIhEIpFIJBKJRCIRiUQiEYlEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRUEsBAj8AIAAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAABAAUAAAAAAAAAAAAAAABQSwECLQAUAACACAAAAAAAAAAAAAAAAAAAAAAAFAAAAGRhdGEueG1sUEsFBgAAAAABAAEAQQAAAAAAIQAAAAAA'
      link.download = '业务规则导入模板.xlsx'
      link.click()

      this.$message.success('模板下载成功')
    },
    downloadSample() {
      // 模拟下载示例数据
      const link = document.createElement('a')
      link.href = 'data:application/vnd.ms-excel;base64,UEsDBBQAAAgIAAAAAAAAAAAAAAAAAAAAAAAQAAAAZGF0YS54bWx7Y1BDoMwDAX3v0ihq3hD0SJ0u1SJdEm6i706qHYyh1+o3NbdsJJvsAn4B9qCI/x79hKGktPnZHOXP59nZ2dkVEokEgUDgdDrFYhGDwQAVCoVCoVBqNpvNZnM6nWaz2Xg8Ho1Gw+EIw+E4nU6nUq1WazabTqfTarU6ne7xeDweD0ej0Wg0Gk1Go9FwOBwOh0Oh0Og0Oo1Go9Hr9VqtVqvVarVardfrtVqtVrvd7nQ6nU6n1+v1+oVCoVCoVCqVCoVCoVAoFAqFQqFQKBQKhUIhEIpFIJBKJRCIRiUQiEYlEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRSCQSiUQikUgkEolEIpFIJBKJRCKRUEsBAj8AIAAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAABAAUAAAAAAAAAAAAAAABQSwECLQAUAACACAAAAAAAAAAAAAAAAAAAAAAAFAAAAGRhdGEueG1sUEsFBgAAAAABAAEAQQAAAAAAIQAAAAAA'
      link.download = '业务规则示例数据.xlsx'
      link.click()

      this.$message.success('示例数据下载成功')
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
      if (file.raw.size > 10 * 1024 * 1024) {
        this.$message.error('文件大小不能超过10MB')
        this.fileList = []
        return false
      }

      const validTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet']
      if (!validTypes.includes(file.raw.type)) {
        this.$message.error('请上传Excel文件')
        this.fileList = []
        return false
      }
    },
    handleFileRemove() {
      this.fileList = []
    },
    async startImport() {
      if (this.fileList.length === 0) {
        this.$message.error('请选择要导入的文件')
        return
      }

      this.currentStep = 2
      this.importing = true
      this.importProgress = 0
      this.importStatus = ''
      this.errorList = []

      try {
        // 模拟导入过程
        await this.simulateImport()
      } catch (error) {
        this.importStatus = 'exception'
        this.errorMessage = error.message
      } finally {
        this.importing = false
      }
    },
    simulateImport() {
      return new Promise((resolve, reject) => {
        const duration = 3000
        const steps = 20
        const stepDuration = duration / steps

        let currentStep = 0

        const interval = setInterval(() => {
          currentStep++
          this.importProgress = Math.floor((currentStep / steps) * 100)

          if (currentStep >= steps) {
            clearInterval(interval)

            // 模拟随机成功或失败
            if (Math.random() > 0.2) {
              // 成功
              this.importStatus = 'success'
              this.successCount = Math.floor(Math.random() * 50) + 10
              this.$emit('import-success', this.successCount)
              resolve()
            } else {
              // 失败，生成错误信息
              this.importStatus = 'exception'
              this.errorMessage = '数据验证失败'
              this.errorList = [
                { row: 3, message: '规则编码不能为空' },
                { row: 5, message: '规则类型不正确' },
                { row: 7, message: '生效时间格式错误' }
              ]
              reject(new Error('数据验证失败'))
            }
          }
        }, stepDuration)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.import-content {
  .step-content {
    margin-top: 20px;
    padding: 20px 0;
  }

  .template-download {
    text-align: center;

    .el-alert {
      margin-bottom: 20px;
      text-align: left;
    }

    .template-actions {
      .el-button {
        margin: 0 10px;
      }
    }
  }

  .upload-tips {
    margin-top: 20px;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 4px;

    h4 {
      margin: 0 0 10px 0;
      color: #303133;
    }

    ul {
      margin: 0;
      padding-left: 20px;

      li {
        margin-bottom: 5px;
        color: #606266;
      }
    }
  }

  .import-result {
    text-align: center;
    margin-bottom: 20px;

    .el-progress {
      margin-bottom: 20px;
    }

    .result-info {
      p {
        margin: 0;
        font-size: 16px;

        i {
          margin-right: 8px;
          font-size: 18px;
        }
      }
    }
  }

  .error-list {
    margin-top: 20px;

    h4 {
      margin: 0 0 10px 0;
      color: #f56c6c;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>