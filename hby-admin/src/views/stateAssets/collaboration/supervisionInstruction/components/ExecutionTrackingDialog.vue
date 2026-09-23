<template>
  <el-dialog
    title="执行跟踪"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="execution-tracking-container">
      <!-- 指令基本信息 -->
      <el-card class="instruction-info-card">
        <div slot="header">
          <span>指令基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">指令标题：</span>
              <span class="info-value">{{ instructionData.instructionTitle }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">目标企业：</span>
              <span class="info-value">{{ instructionData.targetEnterprise }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">下发日期：</span>
              <span class="info-value">{{ instructionData.issueDate }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">截止日期：</span>
              <span class="info-value">{{ instructionData.deadline }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 执行进度 -->
      <el-card class="progress-card">
        <div slot="header">
          <span>执行进度</span>
        </div>
        <el-steps :active="currentStep" finish-status="success">
          <el-step title="指令下发" description="指令已下发至目标企业"></el-step>
          <el-step title="企业接收" description="企业已确认接收指令"></el-step>
          <el-step title="执行中" description="企业正在执行指令要求"></el-step>
          <el-step title="提交报告" description="企业提交执行报告"></el-step>
          <el-step title="审核确认" description="监管部门审核确认"></el-step>
        </el-steps>
        
        <div class="progress-details">
          <el-progress
            :percentage="executionProgress"
            :status="progressStatus"
            :stroke-width="15"
            text-inside>
          </el-progress>
          <div class="progress-info">
            <span>当前状态：{{ currentStatusText }}</span>
            <span class="progress-time">剩余时间：{{ remainingTime }}</span>
          </div>
        </div>
      </el-card>
      
      <!-- 执行记录 -->
      <el-card class="records-card">
        <div slot="header">
          <span>执行记录</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshRecords">刷新</el-button>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(record, index) in executionRecords"
            :key="index"
            :timestamp="record.timestamp"
            :type="getRecordType(record.type)">
            <div class="record-content">
              <div class="record-title">{{ record.title }}</div>
              <div class="record-description">{{ record.description }}</div>
              <div v-if="record.attachments" class="record-attachments">
                <span>附件：</span>
                <el-link
                  v-for="(attachment, idx) in record.attachments"
                  :key="idx"
                  type="primary"
                  @click="downloadAttachment(attachment)">
                  {{ attachment.name }}
                </el-link>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
      
      <!-- 执行报告 -->
      <el-card class="report-card">
        <div slot="header">
          <span>执行报告</span>
        </div>
        <el-table :data="executionReports" style="width: 100%">
          <el-table-column prop="reportType" label="报告类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getReportTypeTagType(scope.row.reportType)">
                {{ getReportTypeText(scope.row.reportType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reportTitle" label="报告标题"></el-table-column>
          <el-table-column prop="submitTime" label="提交时间" width="180"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="viewReport(scope.row)">查看</el-button>
              <el-button size="mini" type="text" @click="downloadReport(scope.row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleUpdateProgress">更新进度</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExecutionTrackingDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    instructionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentStep: 2,
      executionProgress: 60,
      executionRecords: [
        {
          timestamp: '2024-01-15 09:00:00',
          type: 'issue',
          title: '指令下发',
          description: '监管指令已下发至示例云科技有限公司'
        },
        {
          timestamp: '2024-01-15 10:30:00',
          type: 'receive',
          title: '企业确认接收',
          description: '企业已确认接收指令，负责人：张三'
        },
        {
          timestamp: '2024-01-16 14:00:00',
          type: 'progress',
          title: '开始执行',
          description: '企业开始执行指令要求，预计完成时间：2024-01-20'
        },
        {
          timestamp: '2024-01-18 16:30:00',
          type: 'progress',
          title: '进度更新',
          description: '已完成60%的工作内容，提交了阶段性报告',
          attachments: [
            { name: '阶段性报告.pdf', url: '/files/stage-report.pdf' }
          ]
        }
      ],
      executionReports: [
        {
          reportType: 'stage',
          reportTitle: '阶段性执行报告',
          submitTime: '2024-01-18 16:30:00',
          status: 'approved'
        },
        {
          reportType: 'final',
          reportTitle: '最终执行报告',
          submitTime: '',
          status: 'pending'
        }
      ]
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
    progressStatus() {
      if (this.executionProgress === 100) return 'success'
      if (this.executionProgress >= 80) return null
      return null
    },
    currentStatusText() {
      const statusMap = {
        0: '待下发',
        1: '已下发',
        2: '执行中',
        3: '待审核',
        4: '已完成'
      }
      return statusMap[this.currentStep] || '未知状态'
    },
    remainingTime() {
      // 计算剩余时间的逻辑
      return '2天3小时'
    }
  },
  methods: {
    // 获取记录类型
    getRecordType(type) {
      const typeMap = {
        issue: 'primary',
        receive: 'success',
        progress: 'info',
        complete: 'success',
        warning: 'warning',
        error: 'danger'
      }
      return typeMap[type] || 'info'
    },
    
    // 获取报告类型标签类型
    getReportTypeTagType(type) {
      const typeMap = {
        stage: 'warning',
        final: 'success',
        special: 'info'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取报告类型文本
    getReportTypeText(type) {
      const typeMap = {
        stage: '阶段报告',
        final: '最终报告',
        special: '专项报告'
      }
      return typeMap[type] || type
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        pending: 'warning',
        approved: 'success',
        rejected: 'danger',
        draft: 'info'
      }
      return statusMap[status] || 'default'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        pending: '待审核',
        approved: '已通过',
        rejected: '已拒绝',
        draft: '草稿'
      }
      return statusMap[status] || status
    },
    
    // 刷新记录
    refreshRecords() {
      this.$message.info('正在刷新执行记录...')
      // 这里应该调用API刷新数据
    },
    
    // 下载附件
    downloadAttachment(attachment) {
      this.$message.success(`正在下载：${attachment.name}`)
      // 这里应该调用下载API
    },
    
    // 查看报告
    viewReport(report) {
      this.$message.info(`查看报告：${report.reportTitle}`)
      // 这里应该打开报告查看页面
    },
    
    // 下载报告
    downloadReport(report) {
      this.$message.success(`正在下载：${report.reportTitle}`)
      // 这里应该调用下载API
    },
    
    // 更新进度
    handleUpdateProgress() {
      this.$prompt('请输入进度更新说明', '更新执行进度', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(({ value }) => {
        this.$message.success('执行进度已更新')
        // 这里应该调用更新进度的API
      })
    },
    
    // 关闭
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.execution-tracking-container {
  max-height: 600px;
  overflow-y: auto;
}

.instruction-info-card,
.progress-card,
.records-card,
.report-card {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 10px;
}

.info-label {
  font-weight: bold;
  color: #606266;
}

.info-value {
  color: #303133;
}

.progress-details {
  margin-top: 20px;
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

.record-content {
  padding-left: 10px;
}

.record-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.record-description {
  color: #606266;
  margin-bottom: 5px;
}

.record-attachments {
  color: #909399;
  font-size: 12px;
}

.record-attachments .el-link {
  margin-left: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
