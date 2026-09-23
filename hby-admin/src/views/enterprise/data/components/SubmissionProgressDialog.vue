<template>
  <el-dialog
    title="提交进度"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-alert
      :title="`当前进度：${progressData.currentStep || '准备中'}`"
      :type="getAlertType(progressData.status)"
      :closable="false"
      style="margin-bottom: 20px;"
    ></el-alert>
    
    <el-steps :active="currentStepIndex" :status="stepStatus" align-center>
      <el-step
        v-for="(step, index) in steps"
        :key="index"
        :title="step.title"
        :description="step.description"
        :status="step.status"
      ></el-step>
    </el-steps>
    
    <div class="progress-content">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="progress-card">
            <h4>总体进度</h4>
            <el-progress
              :percentage="progressData.overallProgress || 0"
              :color="getProgressColor(progressData.overallProgress)"
              :stroke-width="20"
              text-inside
            ></el-progress>
            <p style="margin-top: 10px; color: #606266;">
              {{ progressData.progressDescription || '正在处理中...' }}
            </p>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="progress-card">
            <h4>当前任务</h4>
            <div class="current-task">
              <div class="task-info">
                <span class="task-name">{{ currentTask.name || '无当前任务' }}</span>
                <span class="task-progress">{{ currentTask.progress || 0 }}%</span>
              </div>
              <el-progress
                :percentage="currentTask.progress || 0"
                :show-text="false"
                :stroke-width="8"
              ></el-progress>
              <p class="task-description">{{ currentTask.description || '' }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <el-divider content-position="left">详细进度</el-divider>
      <el-table :data="taskDetails" border>
        <el-table-column label="序号" width="60">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="taskName" label="任务名称" width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag size="small" :type="getTaskStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="150">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.progress"
              :show-text="false"
              :stroke-width="6"
            ></el-progress>
            <span style="margin-left: 10px;">{{ scope.row.progress }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="150"></el-table-column>
        <el-table-column prop="endTime" label="完成时间" width="150"></el-table-column>
        <el-table-column prop="duration" label="耗时" width="100"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
      </el-table>
      
      <el-divider content-position="left">实时日志</el-divider>
      <div class="log-container">
        <div
          v-for="(log, index) in logs"
          :key="index"
          class="log-item"
          :class="getLogClass(log.level)"
        >
          <span class="log-time">{{ log.timestamp }}</span>
          <span class="log-level">{{ log.level }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="refreshProgress">刷新进度</el-button>
      <el-button type="success" @click="downloadLog">下载日志</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getSubmissionProgress, getSubmissionLogs, downloadSubmissionLog } from '@/api/enterprise/data'

export default {
  name: 'SubmissionProgressDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    submissionId: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      refreshTimer: null,
      loading: false,
      progressData: {},
      taskDetails: [],
      logs: []
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
    steps() {
      return [
        {
          title: '数据准备',
          description: '准备提交数据',
          status: this.getStepStatus(0)
        },
        {
          title: '数据验证',
          description: '验证数据格式和完整性',
          status: this.getStepStatus(1)
        },
        {
          title: '数据上传',
          description: '上传数据到服务器',
          status: this.getStepStatus(2)
        },
        {
          title: '审核处理',
          description: '等待审核处理',
          status: this.getStepStatus(3)
        },
        {
          title: '完成',
          description: '提交完成',
          status: this.getStepStatus(4)
        }
      ]
    },
    currentStepIndex() {
      return this.progressData.currentStepIndex || 0
    },
    stepStatus() {
      if (this.progressData.status === 'error') return 'error'
      if (this.progressData.status === 'success') return 'success'
      return 'process'
    },
    currentTask() {
      return this.progressData.currentTask || {}
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.fetchProgressData()
        this.startRefresh()
      } else {
        this.stopRefresh()
      }
    }
  },
  methods: {
    async fetchProgressData() {
      if (!this.submissionId) return
      this.loading = true
      try {
        const res = await getSubmissionProgress({ id: this.submissionId })
        if (res.code === 1) {
          this.progressData = res.data || {}
          this.taskDetails = res.data.taskDetails || []
        } else {
          this.$message.error(res.msg || '获取进度信息失败')
        }
      } catch (error) {
        this.$message.error('获取进度信息失败')
      }
      try {
        const logRes = await getSubmissionLogs({ id: this.submissionId })
        if (logRes.code === 1) {
          this.logs = logRes.data || []
        }
      } catch (error) {
        console.error('获取日志失败', error)
      }
      this.loading = false
    },
    getAlertType(status) {
      const typeMap = {
        'running': 'info',
        'success': 'success',
        'error': 'error',
        'warning': 'warning'
      }
      return typeMap[status] || 'info'
    },
    getStepStatus(index) {
      if (index < this.currentStepIndex) return 'finish'
      if (index === this.currentStepIndex) {
        if (this.progressData.status === 'error') return 'error'
        return 'process'
      }
      return 'wait'
    },
    getProgressColor(percentage) {
      if (percentage >= 80) return '#67C23A'
      if (percentage >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    getTaskStatusType(status) {
      const statusMap = {
        '已完成': 'success',
        '进行中': 'primary',
        '等待中': 'info',
        '失败': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getLogClass(level) {
      return `log-${level.toLowerCase()}`
    },
    startRefresh() {
      this.refreshTimer = setInterval(() => {
        this.refreshProgress()
      }, 5000)
    },
    stopRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },
    refreshProgress() {
      this.fetchProgressData()
    },
    async downloadLog() {
      if (!this.submissionId) return
      try {
        const res = await downloadSubmissionLog(this.submissionId)
        const blob = new Blob([res], { type: 'application/octet-stream' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `submission_log_${this.submissionId}.txt`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('日志下载成功')
      } catch (error) {
        this.$message.error('日志下载失败')
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  },
  beforeDestroy() {
    this.stopRefresh()
  }
}
</script>

<style scoped>
.progress-content {
  margin-top: 30px;
}

.progress-card {
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  height: 100%;
}

.progress-card h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.current-task {
  margin-top: 15px;
}

.task-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.task-name {
  font-weight: bold;
  color: #303133;
}

.task-progress {
  color: #409EFF;
  font-weight: bold;
}

.task-description {
  margin: 10px 0 0 0;
  color: #606266;
  font-size: 14px;
}

.log-container {
  max-height: 200px;
  overflow-y: auto;
  background-color: #f5f7fa;
  border-radius: 4px;
  padding: 10px;
}

.log-item {
  display: flex;
  margin-bottom: 5px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.log-time {
  color: #909399;
  margin-right: 10px;
  min-width: 150px;
}

.log-level {
  margin-right: 10px;
  min-width: 60px;
  font-weight: bold;
}

.log-message {
  flex: 1;
}

.log-info .log-level {
  color: #409EFF;
}

.log-success .log-level {
  color: #67C23A;
}

.log-warning .log-level {
  color: #E6A23C;
}

.log-error .log-level {
  color: #F56C6C;
}
</style>
