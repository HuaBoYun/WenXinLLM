<template>
  <el-dialog
    title="同步进度监控"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="sync-progress-container">
      <!-- 同步概览 -->
      <el-card class="overview-card">
        <div slot="header">
          <span>同步概览</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ syncData.totalRecords || 0 }}</div>
              <div class="overview-label">总记录数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ syncData.processedRecords || 0 }}</div>
              <div class="overview-label">已处理</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ syncData.successRecords || 0 }}</div>
              <div class="overview-label">成功</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ syncData.failedRecords || 0 }}</div>
              <div class="overview-label">失败</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 进度条 -->
      <el-card class="progress-card">
        <div slot="header">
          <span>同步进度</span>
        </div>
        <el-progress
          :percentage="syncProgress"
          :status="progressStatus"
          :stroke-width="20"
          text-inside>
        </el-progress>
        <div class="progress-info">
          <span>{{ syncData.currentStep || '准备中...' }}</span>
          <span class="progress-time">{{ formatTime(syncData.elapsedTime) }}</span>
        </div>
      </el-card>
      
      <!-- 同步日志 -->
      <el-card class="log-card">
        <div slot="header">
          <span>同步日志</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshLog">刷新</el-button>
        </div>
        <div class="log-container">
          <div
            v-for="(log, index) in syncLogs"
            :key="index"
            :class="['log-item', `log-${log.level}`]">
            <span class="log-time">{{ log.timestamp }}</span>
            <span class="log-level">{{ log.level.toUpperCase() }}</span>
            <span class="log-message">{{ log.message }}</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="canRetry" type="warning" @click="handleRetry">重试</el-button>
      <el-button v-if="canStop" type="danger" @click="handleStop">停止</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'SyncProgressDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    collaborationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      syncData: {
        totalRecords: 0,
        processedRecords: 0,
        successRecords: 0,
        failedRecords: 0,
        currentStep: '',
        elapsedTime: 0
      },
      syncLogs: [],
      timer: null
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
    syncProgress() {
      if (this.syncData.totalRecords === 0) return 0
      return Math.round((this.syncData.processedRecords / this.syncData.totalRecords) * 100)
    },
    progressStatus() {
      if (this.syncData.failedRecords > 0) return 'exception'
      if (this.syncProgress === 100) return 'success'
      return null
    },
    canRetry() {
      return this.syncData.failedRecords > 0
    },
    canStop() {
      return this.syncProgress < 100 && this.syncProgress > 0
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.startMonitoring()
      } else {
        this.stopMonitoring()
      }
    }
  },
  methods: {
    // 开始监控
    startMonitoring() {
      this.loadSyncData()
      this.timer = setInterval(() => {
        this.loadSyncData()
      }, 2000) // 每2秒刷新一次
    },
    
    // 停止监控
    stopMonitoring() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    
    // 加载同步数据
    loadSyncData() {
      // 模拟数据，实际应该调用API
      this.syncData = {
        totalRecords: 1000,
        processedRecords: Math.min(this.syncData.processedRecords + 50, 1000),
        successRecords: Math.min(this.syncData.successRecords + 45, 950),
        failedRecords: Math.min(this.syncData.failedRecords + 5, 50),
        currentStep: '正在同步数据...',
        elapsedTime: this.syncData.elapsedTime + 2000
      }
      
      // 添加日志
      this.addLog('info', `已处理 ${this.syncData.processedRecords} 条记录`)
    },
    
    // 添加日志
    addLog(level, message) {
      const log = {
        timestamp: new Date().toLocaleTimeString(),
        level: level,
        message: message
      }
      this.syncLogs.unshift(log)
      
      // 保持最多100条日志
      if (this.syncLogs.length > 100) {
        this.syncLogs = this.syncLogs.slice(0, 100)
      }
    },
    
    // 刷新日志
    refreshLog() {
      this.loadSyncData()
    },
    
    // 重试
    handleRetry() {
      this.$confirm('确认重新开始同步吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.syncData = {
          totalRecords: 1000,
          processedRecords: 0,
          successRecords: 0,
          failedRecords: 0,
          currentStep: '重新开始同步...',
          elapsedTime: 0
        }
        this.syncLogs = []
        this.addLog('info', '重新开始同步任务')
      })
    },
    
    // 停止
    handleStop() {
      this.$confirm('确认停止同步吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.stopMonitoring()
        this.addLog('warning', '同步任务已停止')
        this.$message.success('同步已停止')
      })
    },
    
    // 关闭
    handleClose() {
      this.stopMonitoring()
      this.dialogVisible = false
    },
    
    // 格式化时间
    formatTime(milliseconds) {
      const seconds = Math.floor(milliseconds / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)
      
      if (hours > 0) {
        return `${hours}:${(minutes % 60).toString().padStart(2, '0')}:${(seconds % 60).toString().padStart(2, '0')}`
      } else {
        return `${minutes}:${(seconds % 60).toString().padStart(2, '0')}`
      }
    }
  },
  beforeDestroy() {
    this.stopMonitoring()
  }
}
</script>

<style scoped>
.sync-progress-container {
  max-height: 600px;
  overflow-y: auto;
}

.overview-card {
  margin-bottom: 20px;
}

.overview-item {
  text-align: center;
}

.overview-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.overview-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.progress-card {
  margin-bottom: 20px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
}

.progress-time {
  color: #909399;
}

.log-card {
  margin-bottom: 20px;
}

.log-container {
  max-height: 300px;
  overflow-y: auto;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
}

.log-item {
  display: flex;
  margin-bottom: 5px;
  font-size: 12px;
  line-height: 1.5;
}

.log-time {
  width: 80px;
  color: #909399;
  margin-right: 10px;
}

.log-level {
  width: 50px;
  margin-right: 10px;
  font-weight: bold;
}

.log-message {
  flex: 1;
}

.log-info .log-level {
  color: #409EFF;
}

.log-warning .log-level {
  color: #E6A23C;
}

.log-error .log-level {
  color: #F56C6C;
}

.log-success .log-level {
  color: #67C23A;
}

.dialog-footer {
  text-align: right;
}
</style>
