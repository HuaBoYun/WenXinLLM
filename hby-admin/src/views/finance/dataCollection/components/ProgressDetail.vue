<template>
  <el-dialog
    title="采集进度详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <!-- 总体进度 -->
      <el-card class="progress-card">
        <div slot="header" class="clearfix">
          <span>总体进度</span>
          <el-tag :type="getStatusType(progressData.status)" style="float: right">
            {{ getStatusText(progressData.status) }}
          </el-tag>
        </div>
        <div class="progress-info">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="label">任务名称:</span>
                <span class="value">{{ progressData.taskName }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="label">开始时间:</span>
                <span class="value">{{ progressData.startTime }}</span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 15px">
            <el-col :span="24">
              <div class="info-item">
                <span class="label">总体进度:</span>
                <el-progress
                  :percentage="progressData.progress"
                  :color="getProgressColor(progressData.progress)"
                  :status="progressData.progress === 100 ? 'success' : null"
                  style="width: 80%; display: inline-block; margin-left: 10px"
                />
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top: 15px">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">已采集:</span>
                <span class="value highlight">{{ progressData.collectedCount | formatNumber }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">总记录数:</span>
                <span class="value">{{ progressData.totalCount | formatNumber }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">失败数:</span>
                <span class="value error">{{ progressData.failedCount | formatNumber }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 详细步骤 -->
      <el-card class="steps-card" style="margin-top: 20px">
        <div slot="header" class="clearfix">
          <span>执行步骤</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(step, index) in progressData.steps"
            :key="index"
            :timestamp="step.timestamp"
            :type="getStepType(step.status)"
            :icon="getStepIcon(step.status)"
          >
            <div class="step-content">
              <div class="step-title">{{ step.stepName }}</div>
              <div class="step-desc">{{ step.description }}</div>
              <el-progress
                v-if="step.progress !== undefined"
                :percentage="step.progress"
                :status="step.status === 'SUCCESS' ? 'success' : step.status === 'FAILED' ? 'exception' : null"
                style="margin-top: 10px"
              />
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>

      <!-- 错误信息 -->
      <el-card v-if="progressData.errorMessage" class="error-card" style="margin-top: 20px">
        <div slot="header" class="clearfix">
          <span>错误信息</span>
        </div>
        <el-alert
          :title="progressData.errorMessage"
          type="error"
          :closable="false"
          show-icon
        />
      </el-card>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleRefresh" :loading="loading">刷新</el-button>
      <el-button type="primary" @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCollectionProgress } from '@/api/finance/dataCollection'

export default {
  name: 'ProgressDetail',
  filters: {
    formatNumber(value) {
      if (!value) return '0'
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      taskId: '',
      progressData: {
        taskName: '',
        status: '',
        progress: 0,
        startTime: '',
        collectedCount: 0,
        totalCount: 0,
        failedCount: 0,
        steps: [],
        errorMessage: ''
      },
      refreshTimer: null
    }
  },
  beforeDestroy() {
    this.clearRefreshTimer()
  },
  methods: {
    async show(row) {
      this.dialogVisible = true
      this.taskId = row.taskId
      await this.loadProgress()
      
      // 如果任务正在执行,启动自动刷新
      if (row.status === 'RUNNING') {
        this.startAutoRefresh()
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.clearRefreshTimer()
      this.resetData()
    },
    resetData() {
      this.progressData = {
        taskName: '',
        status: '',
        progress: 0,
        startTime: '',
        collectedCount: 0,
        totalCount: 0,
        failedCount: 0,
        steps: [],
        errorMessage: ''
      }
    },
    async loadProgress() {
      this.loading = true
      try {
        const res = await getCollectionProgress(this.taskId)
        if (res.code === 1 && res.data) {
          this.progressData = res.data
        } else {
          this.$message.error(res.msg || '加载进度失败')
        }
      } catch (error) {
        this.$message.error('加载进度失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    handleRefresh() {
      this.loadProgress()
    },
    startAutoRefresh() {
      this.clearRefreshTimer()
      this.refreshTimer = setInterval(() => {
        this.loadProgress()
      }, 3000) // 每3秒刷新一次
    },
    clearRefreshTimer() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'info',
        'RUNNING': 'warning',
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待执行',
        'RUNNING': '执行中',
        'SUCCESS': '已完成',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    getProgressColor(percentage) {
      if (percentage >= 100) return '#67C23A'
      if (percentage >= 75) return '#409EFF'
      if (percentage >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    getStepType(status) {
      const typeMap = {
        'SUCCESS': 'success',
        'RUNNING': 'primary',
        'FAILED': 'danger',
        'PENDING': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStepIcon(status) {
      const iconMap = {
        'SUCCESS': 'el-icon-check',
        'RUNNING': 'el-icon-loading',
        'FAILED': 'el-icon-close',
        'PENDING': 'el-icon-time'
      }
      return iconMap[status] || 'el-icon-time'
    }
  }
}
</script>

<style scoped>
.progress-card,
.steps-card,
.error-card {
  margin-bottom: 20px;
}

.progress-info {
  padding: 10px 0;
}

.info-item {
  margin-bottom: 10px;
}

.info-item .label {
  font-weight: 500;
  color: #606266;
  margin-right: 10px;
}

.info-item .value {
  color: #303133;
}

.info-item .value.highlight {
  color: #409EFF;
  font-weight: 600;
}

.info-item .value.error {
  color: #F56C6C;
  font-weight: 600;
}

.step-content {
  padding: 10px 0;
}

.step-title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.step-desc {
  font-size: 13px;
  color: #909399;
}
</style>

