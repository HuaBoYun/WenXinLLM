<template>
  <el-dialog
    title="分发详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <!-- 分发基本信息 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>分发信息</span>
          <el-tag :type="getDistributionStatusTag(distributionData.status)">
            {{ getDistributionStatusText(distributionData.status) }}
          </el-tag>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="label">报告名称：</span>
              <span class="value">{{ distributionData.reportName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="label">分发方式：</span>
              <span class="value">{{ getDistributionTypeText(distributionData.distributionType) }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-15">
          <el-col :span="12">
            <div class="info-item">
              <span class="label">分发时间：</span>
              <span class="value">{{ distributionData.distributionTime || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="label">分发人：</span>
              <span class="value">{{ distributionData.distributor || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-15">
          <el-col :span="24">
            <div class="info-item">
              <span class="label">分发说明：</span>
              <span class="value">{{ distributionData.description || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 接收人列表 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>接收人列表</span>
          <span class="recipient-count">共 {{ recipients.length }} 人</span>
        </div>
        <el-table :data="recipients" stripe border style="width: 100%">
          <el-table-column prop="recipientName" label="接收人" width="120"></el-table-column>
          <el-table-column prop="department" label="部门" width="150"></el-table-column>
          <el-table-column prop="position" label="职位" width="120"></el-table-column>
          <el-table-column prop="contactInfo" label="联系方式" width="150"></el-table-column>
          <el-table-column prop="receiveStatus" label="接收状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getReceiveStatusTag(scope.row.receiveStatus)">
                {{ getReceiveStatusText(scope.row.receiveStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="receiveTime" label="接收时间" width="160" align="center"></el-table-column>
          <el-table-column prop="readStatus" label="阅读状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getReadStatusTag(scope.row.readStatus)">
                {{ getReadStatusText(scope.row.readStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleResend(scope.row)">重发</el-button>
              <el-button size="mini" type="text" @click="handleViewDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 分发统计 -->
      <el-card class="mb-20">
        <div slot="header" class="card-header">
          <span>分发统计</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value success">{{ statistics.totalRecipients }}</div>
              <div class="stat-label">总接收人数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value primary">{{ statistics.successCount }}</div>
              <div class="stat-label">成功接收</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value warning">{{ statistics.pendingCount }}</div>
              <div class="stat-label">待接收</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value danger">{{ statistics.failedCount }}</div>
              <div class="stat-label">接收失败</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mt-20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value info">{{ statistics.readCount }}</div>
              <div class="stat-label">已阅读</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.unreadCount }}</div>
              <div class="stat-label">未阅读</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.readRate }}%</div>
              <div class="stat-label">阅读率</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.avgReadTime }}</div>
              <div class="stat-label">平均阅读时长</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 分发日志 -->
      <el-card>
        <div slot="header" class="card-header">
          <span>分发日志</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(log, index) in distributionLogs"
            :key="index"
            :timestamp="log.timestamp"
            :type="getLogType(log.type)"
          >
            <div class="log-item">
              <div class="log-content">
                <span class="log-action">{{ log.action }}</span>
                <span class="log-description">{{ log.description }}</span>
              </div>
              <div class="log-meta" v-if="log.details">
                <span class="log-details">{{ log.details }}</span>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportLog">导出日志</el-button>
      <el-button type="success" @click="handleResendAll">批量重发</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DistributionDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    distributionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      recipients: [
        {
          recipientName: '张三',
          department: '财务部',
          position: '财务经理',
          contactInfo: 'zhangsan@company.com',
          receiveStatus: 'SUCCESS',
          receiveTime: '2025-01-21 09:30:00',
          readStatus: 'READ'
        },
        {
          recipientName: '李四',
          department: '风控部',
          position: '风控专员',
          contactInfo: 'lisi@company.com',
          receiveStatus: 'SUCCESS',
          receiveTime: '2025-01-21 09:32:00',
          readStatus: 'UNREAD'
        },
        {
          recipientName: '王五',
          department: '审计部',
          position: '审计主管',
          contactInfo: 'wangwu@company.com',
          receiveStatus: 'FAILED',
          receiveTime: '-',
          readStatus: 'UNREAD'
        }
      ],
      statistics: {
        totalRecipients: 15,
        successCount: 12,
        pendingCount: 1,
        failedCount: 2,
        readCount: 8,
        unreadCount: 7,
        readRate: 53.3,
        avgReadTime: '5分钟'
      },
      distributionLogs: [
        {
          timestamp: '2025-01-21 09:00:00',
          type: 'info',
          action: '开始分发',
          description: '开始向15个接收人分发报告',
          details: '分发方式：邮件'
        },
        {
          timestamp: '2025-01-21 09:05:00',
          type: 'success',
          action: '分发成功',
          description: '成功向12个接收人发送报告',
          details: '成功率：80%'
        },
        {
          timestamp: '2025-01-21 09:10:00',
          type: 'warning',
          action: '部分失败',
          description: '2个接收人分发失败',
          details: '失败原因：邮箱地址无效'
        },
        {
          timestamp: '2025-01-21 09:30:00',
          type: 'info',
          action: '首次阅读',
          description: '张三首次阅读报告',
          details: '阅读时长：8分钟'
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
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },

    handleResend(recipient) {
      this.$message.success(`正在重新发送给 ${recipient.recipientName}`)
    },

    handleViewDetail(recipient) {
      this.$message.success(`查看 ${recipient.recipientName} 的详细信息`)
    },

    handleExportLog() {
      this.$message.success('正在导出分发日志...')
    },

    handleResendAll() {
      this.$confirm('确定要重新发送给所有失败的接收人吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('正在批量重发...')
      })
    },

    getDistributionStatusTag(status) {
      const tagMap = {
        'SUCCESS': 'success',
        'PARTIAL': 'warning',
        'FAILED': 'danger',
        'PENDING': 'info'
      }
      return tagMap[status] || 'info'
    },

    getDistributionStatusText(status) {
      const textMap = {
        'SUCCESS': '分发成功',
        'PARTIAL': '部分成功',
        'FAILED': '分发失败',
        'PENDING': '分发中'
      }
      return textMap[status] || status
    },

    getDistributionTypeText(type) {
      const textMap = {
        'EMAIL': '邮件分发',
        'SYSTEM': '系统通知',
        'SMS': '短信分发',
        'DOWNLOAD': '下载链接'
      }
      return textMap[type] || type
    },

    getReceiveStatusTag(status) {
      const tagMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PENDING': 'warning'
      }
      return tagMap[status] || 'info'
    },

    getReceiveStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'PENDING': '待接收'
      }
      return textMap[status] || status
    },

    getReadStatusTag(status) {
      const tagMap = {
        'READ': 'success',
        'UNREAD': 'info'
      }
      return tagMap[status] || 'info'
    },

    getReadStatusText(status) {
      const textMap = {
        'READ': '已读',
        'UNREAD': '未读'
      }
      return textMap[status] || status
    },

    getLogType(type) {
      const typeMap = {
        'success': 'success',
        'warning': 'warning',
        'danger': 'danger',
        'info': 'primary'
      }
      return typeMap[type] || 'primary'
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.mt-15 {
  margin-top: 15px;
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.recipient-count {
  font-size: 14px;
  color: #909399;
}

.info-item {
  margin-bottom: 10px;
}

.info-item .label {
  font-weight: bold;
  color: #606266;
}

.info-item .value {
  color: #303133;
}

.stat-item {
  text-align: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-value.success {
  color: #67c23a;
}

.stat-value.primary {
  color: #409eff;
}

.stat-value.warning {
  color: #e6a23c;
}

.stat-value.danger {
  color: #f56c6c;
}

.stat-value.info {
  color: #909399;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.log-item {
  margin-bottom: 10px;
}

.log-content {
  margin-bottom: 5px;
}

.log-action {
  font-weight: bold;
  color: #303133;
  margin-right: 10px;
}

.log-description {
  color: #606266;
}

.log-meta {
  font-size: 12px;
}

.log-details {
  color: #909399;
}

.dialog-footer {
  text-align: right;
}
</style>
