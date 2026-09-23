<template>
  <div class="approval-history-container">
    <!-- 数据信息卡片 -->
    <el-card shadow="never" class="info-card">
      <div slot="header" class="card-header">
        <span>预算数据信息</span>
        <el-button size="small" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="预算模型">{{ dataInfo.modelName }}</el-descriptions-item>
        <el-descriptions-item label="预算期间">{{ dataInfo.period }}</el-descriptions-item>
        <el-descriptions-item label="预算版本">{{ dataInfo.version }}</el-descriptions-item>
        <el-descriptions-item label="提交人">{{ dataInfo.submitUserName }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ dataInfo.submitTime }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag v-if="dataInfo.status === 'DRAFT'" type="info" size="small">草稿</el-tag>
          <el-tag v-else-if="dataInfo.status === 'SUBMITTED'" type="warning" size="small">已提交</el-tag>
          <el-tag v-else-if="dataInfo.status === 'APPROVING'" type="primary" size="small">审批中</el-tag>
          <el-tag v-else-if="dataInfo.status === 'APPROVED'" type="success" size="small">已审批</el-tag>
          <el-tag v-else-if="dataInfo.status === 'REJECTED'" type="danger" size="small">已驳回</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 审批流程时间线 -->
    <el-card shadow="never" class="timeline-card">
      <div slot="header">
        <span>审批流程</span>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in approvalHistory"
          :key="index"
          :timestamp="item.approvalTime || item.createTime"
          placement="top"
          :type="getTimelineType(item.approvalStatus)"
          :icon="getTimelineIcon(item.approvalStatus)"
        >
          <el-card>
            <div class="timeline-content">
              <div class="timeline-header">
                <span class="node-name">{{ item.nodeName }}</span>
                <el-tag :type="getStatusType(item.approvalStatus)" size="small">
                  {{ getStatusText(item.approvalStatus) }}
                </el-tag>
              </div>
              <div class="timeline-body">
                <p><strong>审批人:</strong> {{ item.approverName || item.approverId }}</p>
                <p v-if="item.approvalComment"><strong>审批意见:</strong> {{ item.approvalComment }}</p>
                <p v-if="item.approvalTime"><strong>审批时间:</strong> {{ item.approvalTime }}</p>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <el-empty v-if="approvalHistory.length === 0" description="暂无审批记录" />
    </el-card>
  </div>
</template>

<script>
import { getApprovalHistory } from '@/api/financialSharing/budgetPlanning/budgetApproval'

export default {
  name: 'ApprovalHistory',
  data() {
    return {
      loading: false,
      dataId: '',
      dataInfo: {},
      approvalHistory: []
    }
  },
  created() {
    this.dataId = this.$route.query.dataId
    if (this.dataId) {
      this.loadData()
    } else {
      this.$message.error('缺少数据ID参数')
      this.handleBack()
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const res = await getApprovalHistory({ dataId: this.dataId })
        if (res.code === 1) {
          this.approvalHistory = res.data || []
          // 从第一条记录中获取数据信息
          if (this.approvalHistory.length > 0) {
            const first = this.approvalHistory[0]
            this.dataInfo = {
              modelName: first.modelName,
              period: first.period,
              version: first.version,
              submitUserName: first.submitUserName,
              submitTime: first.createTime,
              status: this.getDataStatus()
            }
          }
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 获取数据状态
    getDataStatus() {
      if (this.approvalHistory.length === 0) return 'DRAFT'
      
      const hasRejected = this.approvalHistory.some(item => item.approvalStatus === 'REJECTED')
      if (hasRejected) return 'REJECTED'
      
      const allApproved = this.approvalHistory.every(item => 
        item.approvalStatus === 'APPROVED' || item.approvalStatus === 'CANCELLED'
      )
      if (allApproved) return 'APPROVED'
      
      const hasPending = this.approvalHistory.some(item => item.approvalStatus === 'PENDING')
      if (hasPending) return 'APPROVING'
      
      return 'SUBMITTED'
    },
    // 获取时间线类型
    getTimelineType(status) {
      const typeMap = {
        'PENDING': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info',
        'WAITING': 'warning'
      }
      return typeMap[status] || 'info'
    },
    // 获取时间线图标
    getTimelineIcon(status) {
      const iconMap = {
        'PENDING': 'el-icon-time',
        'APPROVED': 'el-icon-circle-check',
        'REJECTED': 'el-icon-circle-close',
        'CANCELLED': 'el-icon-remove-outline',
        'WAITING': 'el-icon-loading'
      }
      return iconMap[status] || 'el-icon-info'
    },
    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info',
        'WAITING': ''
      }
      return typeMap[status] || 'info'
    },
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVED': '已通过',
        'REJECTED': '已驳回',
        'CANCELLED': '已撤销',
        'WAITING': '等待中'
      }
      return textMap[status] || status
    },
    // 返回
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.approval-history-container {
  padding: 20px;

  .info-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .timeline-card {
    .timeline-content {
      .timeline-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;

        .node-name {
          font-size: 16px;
          font-weight: bold;
        }
      }

      .timeline-body {
        p {
          margin: 5px 0;
          color: #606266;
        }
      }
    }
  }
}
</style>

