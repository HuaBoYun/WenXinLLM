<template>
  <el-dialog
    title="导出进度"
    :visible.sync="dialogVisible"
    width="500px"
    :close-on-click-modal="false"
    :before-close="handleClose">

    <div class="export-progress-content">
      <!-- 导出信息 -->
      <div class="export-info">
        <div class="info-item">
          <span class="label">导出类型：</span>
          <span class="value">{{ exportInfo.type }}</span>
        </div>
        <div class="info-item">
          <span class="label">数据范围：</span>
          <span class="value">{{ exportInfo.range }}</span>
        </div>
        <div class="info-item">
          <span class="label">预计记录数：</span>
          <span class="value">{{ exportInfo.count }} 条</span>
        </div>
      </div>

      <!-- 进度条 -->
      <div class="progress-section">
        <el-progress
          :percentage="progressPercentage"
          :status="progressStatus"
          :stroke-width="10">
        </el-progress>
        <div class="progress-text">
          <span>{{ progressText }}</span>
          <span v-if="progressPercentage > 0 && progressPercentage < 100">
            {{ formatTime(remainingTime) }}
          </span>
        </div>
      </div>

      <!-- 导出选项 -->
      <div class="export-options" v-if="showOptions">
        <el-form :model="exportOptions" label-width="100px" size="small">
          <el-form-item label="文件格式">
            <el-select v-model="exportOptions.format" style="width: 100%">
              <el-option label="Excel (.xlsx)" value="xlsx"></el-option>
              <el-option label="Excel (.xls)" value="xls"></el-option>
              <el-option label="PDF" value="pdf" v-if="allowPDF"></el-option>
              <el-option label="CSV" value="csv"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="包含表头">
            <el-switch v-model="exportOptions.includeHeader"></el-switch>
          </el-form-item>
          <el-form-item label="合并单元格">
            <el-switch v-model="exportOptions.mergeCells" v-if="exportOptions.format !== 'csv'"></el-switch>
          </el-form-item>
          <el-form-item label="自动列宽">
            <el-switch v-model="exportOptions.autoWidth" v-if="exportOptions.format !== 'csv' && exportOptions.format !== 'pdf'"></el-switch>
          </el-form-item>
        </el-form>
      </div>

      <!-- 历史导出记录 -->
      <div class="export-history" v-if="historyList.length > 0">
        <h4>最近导出记录</h4>
        <el-table :data="historyList" size="small" max-height="200">
          <el-table-column prop="fileName" label="文件名" min-width="150"></el-table-column>
          <el-table-column prop="createTime" label="导出时间" width="150"></el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'success' ? 'success' : 'danger'" size="mini">
                {{ scope.row.status === 'success' ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="downloadFile(scope.row)"
                v-if="scope.row.status === 'success' && scope.row.downloadUrl">
                下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleCancel" v-if="progressPercentage < 100">取消导出</el-button>
      <el-button type="primary" @click="handleStartExport" v-if="showOptions && !exporting">开始导出</el-button>
      <el-button type="primary" @click="handleClose" v-if="progressPercentage === 100">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExportProgressDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    exportInfo: {
      type: Object,
      default: () => ({
        type: '',
        range: '',
        count: 0
      })
    },
    allowPDF: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      exporting: false,
      showOptions: true,
      progressPercentage: 0,
      progressStatus: '',
      progressText: '准备导出...',
      remainingTime: 0,
      exportOptions: {
        format: 'xlsx',
        includeHeader: true,
        mergeCells: true,
        autoWidth: true
      },
      historyList: [],
      exportTimer: null
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initExport()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    initExport() {
      this.showOptions = true
      this.progressPercentage = 0
      this.progressStatus = ''
      this.progressText = '准备导出...'
      this.remainingTime = 0
      this.exporting = false

      // 加载历史记录
      this.loadExportHistory()
    },
    async handleStartExport() {
      this.showOptions = false
      this.exporting = true
      this.progressText = '正在生成文件...'
      this.progressPercentage = 0
      this.progressStatus = ''

      try {
        // 通知父组件开始导出
        this.$emit('start-export', this.exportOptions)

        // 模拟导出进度
        await this.simulateExportProgress()

      } catch (error) {
        this.progressStatus = 'exception'
        this.progressText = '导出失败：' + error.message
        this.$message.error('导出失败：' + error.message)
      }
    },
    async simulateExportProgress() {
      const totalSteps = 10
      const stepTime = 500 // 每步500ms

      for (let i = 0; i <= totalSteps; i++) {
        await new Promise(resolve => setTimeout(resolve, stepTime))

        if (this.exporting) {
          this.progressPercentage = Math.floor((i / totalSteps) * 100)
          this.remainingTime = (totalSteps - i) * stepTime / 1000

          if (i < totalSteps * 0.3) {
            this.progressText = '正在查询数据...'
          } else if (i < totalSteps * 0.6) {
            this.progressText = '正在处理数据...'
          } else if (i < totalSteps * 0.9) {
            this.progressText = '正在生成文件...'
          } else {
            this.progressText = '正在保存文件...'
          }
        } else {
          break
        }
      }

      if (this.exporting) {
        this.progressPercentage = 100
        this.progressStatus = 'success'
        this.progressText = '导出完成'

        // 添加到历史记录
        this.addToHistory()

        this.$message.success('导出成功')
        this.$emit('export-success')
      }
    },
    handleCancel() {
      this.$confirm('确定要取消导出吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '继续导出',
        type: 'warning'
      }).then(() => {
        this.exporting = false
        this.progressText = '已取消导出'
        this.$emit('export-cancel')
      })
    },
    handleClose() {
      if (this.exporting && this.progressPercentage < 100) {
        this.handleCancel()
      } else {
        this.dialogVisible = false
      }
    },
    loadExportHistory() {
      // 从localStorage加载历史记录
      const history = localStorage.getItem('exportHistory')
      if (history) {
        this.historyList = JSON.parse(history).slice(0, 5) // 只显示最近5条
      } else {
        this.historyList = []
      }
    },
    addToHistory() {
      const record = {
        fileName: this.generateFileName(),
        createTime: new Date().toLocaleString(),
        status: 'success',
        downloadUrl: '#' // 实际应该是下载链接
      }

      this.historyList.unshift(record)
      if (this.historyList.length > 5) {
        this.historyList = this.historyList.slice(0, 5)
      }

      // 保存到localStorage
      localStorage.setItem('exportHistory', JSON.stringify(this.historyList))
    },
    generateFileName() {
      const timestamp = new Date().getTime()
      const extension = this.exportOptions.format
      return `${this.exportInfo.type}_${timestamp}.${extension}`
    },
    downloadFile(record) {
      // 触发文件下载
      const link = document.createElement('a')
      link.href = record.downloadUrl
      link.download = record.fileName
      link.click()
    },
    formatTime(seconds) {
      if (seconds <= 0) return ''

      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return `剩余 ${mins}:${secs.toString().padStart(2, '0')}`
    }
  },
  beforeDestroy() {
    if (this.exportTimer) {
      clearInterval(this.exportTimer)
    }
  }
}
</script>

<style lang="scss" scoped>
.export-progress-content {
  .export-info {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;

    .info-item {
      display: flex;
      margin-bottom: 8px;

      &:last-child {
        margin-bottom: 0;
      }

      .label {
        width: 80px;
        color: #909399;
        font-size: 14px;
      }

      .value {
        flex: 1;
        color: #303133;
        font-size: 14px;
        font-weight: 500;
      }
    }
  }

  .progress-section {
    margin-bottom: 20px;

    .progress-text {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
      color: #606266;
      font-size: 13px;
    }
  }

  .export-options {
    margin-bottom: 20px;
  }

  .export-history {
    h4 {
      margin: 0 0 12px 0;
      font-size: 14px;
      color: #303133;
      font-weight: 600;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>