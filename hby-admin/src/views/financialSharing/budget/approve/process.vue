<template>
  <div class="budget-approval-process">
    <!-- 头部信息 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>{{ budgetDetail.budgetName || '审批流程图' }}</span>
        <el-button-group class="header-actions">
          <el-button
            size="small"
            icon="el-icon-back"
            @click="handleGoBack"
          >
            返回
          </el-button>
        </el-button-group>
      </div>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-item">
            <label>预算编号：</label>
            <span>{{ budgetDetail.budgetCode }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>当前状态：</label>
            <el-tag :type="getStatusTag(budgetDetail.status)">
              {{ getStatusText(budgetDetail.status) }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label>申请时间：</label>
            <span>{{ budgetDetail.applyTime | formatDate }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 流程图区域 -->
    <el-card class="process-card">
      <div slot="header">审批流程图</div>

      <!-- 流程图容器 -->
      <div class="process-container" ref="processContainer">
        <div class="process-flow" v-loading="loading">
          <!-- 开始节点 -->
          <div class="process-node start-node">
            <div class="node-content">
              <i class="el-icon-caret-right"></i>
              <span>开始</span>
            </div>
            <div class="node-status">
              <el-tag type="success" size="mini">已完成</el-tag>
            </div>
          </div>

          <!-- 流程连接线 -->
          <div class="process-line"></div>

          <!-- 动态流程节点 -->
          <div
            v-for="(node, index) in processNodes"
            :key="node.nodeId"
            class="process-node"
            :class="{
              'current-node': node.isCurrent,
              'completed-node': node.isCompleted,
              'pending-node': !node.isCompleted
            }"
          >
            <div class="node-content">
              <div class="node-icon">
                <i :class="getNodeIcon(node)"></i>
              </div>
              <div class="node-info">
                <div class="node-title">{{ node.nodeName }}</div>
                <div class="node-approver" v-if="node.approver">
                  处理人：{{ node.approver }}
                </div>
                <div class="node-time" v-if="node.approvalTime">
                  {{ node.approvalTime | formatDate }}
                </div>
              </div>
            </div>

            <div class="node-status">
              <el-tag
                :type="getNodeStatusType(node)"
                size="mini"
              >
                {{ getNodeStatusText(node) }}
              </el-tag>
            </div>

            <!-- 节点详情 -->
            <div
              class="node-detail"
              v-if="node.comment || (node.attachments && node.attachments.length > 0)"
            >
              <div class="detail-comment" v-if="node.comment">
                <strong>处理意见：</strong>{{ node.comment }}
              </div>
              <div class="detail-attachments" v-if="node.attachments && node.attachments.length > 0">
                <strong>附件：</strong>
                <el-link
                  v-for="(file, fileIndex) in node.attachments"
                  :key="fileIndex"
                  :href="file.url"
                  target="_blank"
                  type="primary"
                  class="attachment-link"
                >
                  {{ file.name }}
                </el-link>
              </div>
            </div>
          </div>

          <!-- 流程连接线 -->
          <div class="process-line" v-for="n in processNodes.length - 1" :key="'line-' + n"></div>

          <!-- 结束节点 -->
          <div class="process-node end-node">
            <div class="node-content">
              <i class="el-icon-circle-check"></i>
              <span>结束</span>
            </div>
            <div class="node-status">
              <el-tag
                :type="getEndNodeStatusType()"
                size="mini"
              >
                {{ getEndNodeStatusText() }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 流程统计信息 -->
      <div class="process-stats" v-if="processNodes.length > 0">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ completedNodes }}</div>
              <div class="stat-label">已完成节点</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ pendingNodes }}</div>
              <div class="stat-label">待处理节点</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ totalDays }}</div>
              <div class="stat-label">流程总时长(天)</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value">{{ processNodes.length }}</div>
              <div class="stat-label">总节点数</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 审批节点详情对话框 -->
    <el-dialog
      title="节点详情"
      :visible.sync="nodeDetailDialogVisible"
      width="600px"
    >
      <div v-if="selectedNode">
        <el-form label-width="100px" size="small">
          <el-form-item label="节点名称">
            <span>{{ selectedNode.nodeName }}</span>
          </el-form-item>
          <el-form-item label="处理状态">
            <el-tag :type="getNodeStatusType(selectedNode)">
              {{ getNodeStatusText(selectedNode) }}
            </el-tag>
          </el-form-item>
          <el-form-item label="处理人" v-if="selectedNode.approver">
            <span>{{ selectedNode.approver }} ({{ selectedNode.department }})</span>
          </el-form-item>
          <el-form-item label="处理时间" v-if="selectedNode.approvalTime">
            <span>{{ selectedNode.approvalTime | formatDate }}</span>
          </el-form-item>
          <el-form-item label="处理意见" v-if="selectedNode.comment">
            <div class="comment-content">{{ selectedNode.comment }}</div>
          </el-form-item>
          <el-form-item label="相关附件" v-if="selectedNode.attachments && selectedNode.attachments.length > 0">
            <div class="attachment-list">
              <el-link
                v-for="(file, index) in selectedNode.attachments"
                :key="index"
                :href="file.url"
                target="_blank"
                type="primary"
                class="attachment-item"
              >
                <i class="el-icon-document"></i>
                {{ file.name }}
              </el-link>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="nodeDetailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatDate } from '@/utils/index'

export default {
  name: 'BudgetApprovalProcess',
  data() {
    return {
      // 预算详情
      budgetDetail: {},
      loading: false,

      // 流程节点数据
      processNodes: [],

      // 选中的节点详情
      selectedNode: null,
      nodeDetailDialogVisible: false
    }
  },

  created() {
    this.budgetId = this.$route.query.id
    if (this.budgetId) {
      this.fetchBudgetDetail()
      this.fetchProcessFlow()
    } else {
      this.$message.error('缺少预算ID参数')
      this.handleGoBack()
    }
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  computed: {
    // 已完成节点数
    completedNodes() {
      return this.processNodes.filter(node => node.isCompleted).length
    },

    // 待处理节点数
    pendingNodes() {
      return this.processNodes.filter(node => !node.isCompleted).length
    },

    // 流程总时长(天)
    totalDays() {
      if (this.processNodes.length === 0) return 0

      const firstNode = this.processNodes[0]
      const lastCompletedNode = this.processNodes
        .filter(node => node.approvalTime)
        .pop()

      if (lastCompletedNode && firstNode.applyTime) {
        const start = new Date(firstNode.applyTime)
        const end = new Date(lastCompletedNode.approvalTime)
        const diff = Math.ceil((end - start) / (1000 * 60 * 60 * 24))
        return Math.max(1, diff)
      }

      return 0
    }
  },

  methods: {
    // 获取预算详情
    async fetchBudgetDetail() {
      try {
        const response = await budgetApi.getBudgetDetail(this.budgetId)
        if (response.code === 1) {
          this.budgetDetail = response.data || {}
        } else {
          this.$message.error(response.message || '获取预算详情失败')
        }
      } catch (error) {
        console.error('获取预算详情异常:', error)
        this.$message.error('获取预算详情失败，请稍后重试')
      }
    },

    // 获取流程图数据
    async fetchProcessFlow() {
      this.loading = true
      try {
        const response = await budgetApi.getProcessFlow(this.budgetId)
        if (response.code === 1) {
          this.processNodes = response.data || []
        } else {
          this.$message.error(response.message || '获取流程图数据失败')
        }
      } catch (error) {
        console.error('获取流程图数据异常:', error)
        this.$message.error('获取流程图数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 返回
    handleGoBack() {
      this.$router.go(-1)
    },

    // 获取节点图标
    getNodeIcon(node) {
      if (node.isCompleted) {
        if (node.result === 'approved') {
          return 'el-icon-circle-check'
        } else if (node.result === 'rejected') {
          return 'el-icon-circle-close'
        } else if (node.result === 'returned') {
          return 'el-icon-back'
        }
      } else if (node.isCurrent) {
        return 'el-icon-time'
      }
      return 'el-icon-document'
    },

    // 获取节点状态类型
    getNodeStatusType(node) {
      if (node.isCompleted) {
        if (node.result === 'approved') {
          return 'success'
        } else if (node.result === 'rejected') {
          return 'danger'
        } else if (node.result === 'returned') {
          return 'warning'
        }
      } else if (node.isCurrent) {
        return 'primary'
      }
      return 'info'
    },

    // 获取节点状态文本
    getNodeStatusText(node) {
      if (node.isCompleted) {
        if (node.result === 'approved') {
          return '已通过'
        } else if (node.result === 'rejected') {
          return '已拒绝'
        } else if (node.result === 'returned') {
          return '已退回'
        }
      } else if (node.isCurrent) {
        return '处理中'
      }
      return '待处理'
    },

    // 获取结束节点状态类型
    getEndNodeStatusType() {
      const status = this.budgetDetail.status
      if (status === 'approved') {
        return 'success'
      } else if (status === 'rejected') {
        return 'danger'
      } else if (status === 'returned') {
        return 'warning'
      }
      return 'info'
    },

    // 获取结束节点状态文本
    getEndNodeStatusText() {
      const status = this.budgetDetail.status
      if (status === 'approved') {
        return '流程完成'
      } else if (status === 'rejected') {
        return '已终止'
      } else if (status === 'returned') {
        return '已退回'
      }
      return '进行中'
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'pending': 'warning',
        'approved': 'success',
        'rejected': 'danger',
        'returned': 'info'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'pending': '待审批',
        'approved': '已通过',
        'rejected': '已拒绝',
        'returned': '已退回'
      }
      return textMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-approval-process {
  padding: 20px;

  .header-card {
    margin-bottom: 16px;

    .header-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-actions {
        display: flex;
        gap: 8px;
      }
    }

    .info-item {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      label {
        font-weight: 500;
        color: #606266;
        min-width: 80px;
      }
    }
  }

  .process-card {
    .process-container {
      padding: 20px 0;

      .process-flow {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 20px;
      }

      .process-node {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 280px;
        position: relative;

        .node-content {
          display: flex;
          align-items: center;
          gap: 12px;
          width: 100%;
          padding: 16px;
          border: 2px solid #E4E7ED;
          border-radius: 8px;
          background: #FAFAFA;
          transition: all 0.3s;

          .node-icon {
            font-size: 24px;
            color: #909399;
          }

          .node-info {
            flex: 1;

            .node-title {
              font-weight: 600;
              color: #303133;
              font-size: 14px;
              margin-bottom: 4px;
            }

            .node-approver,
            .node-time {
              font-size: 12px;
              color: #909399;
            }
          }
        }

        .node-status {
          margin-top: 8px;
        }

        .node-detail {
          width: 100%;
          margin-top: 8px;
          padding: 12px;
          background: #F5F7FA;
          border-radius: 4px;
          font-size: 12px;

          .detail-comment {
            margin-bottom: 8px;
            line-height: 1.4;

            strong {
              color: #303133;
            }
          }

          .detail-attachments {
            .attachment-link {
              margin-right: 8px;
            }
          }
        }

        // 当前节点样式
        &.current-node {
          .node-content {
            border-color: #409EFF;
            background: #ECF5FF;

            .node-icon {
              color: #409EFF;
              animation: pulse 2s infinite;
            }
          }
        }

        // 已完成节点样式
        &.completed-node {
          .node-content {
            border-color: #67C23A;
            background: #F0F9FF;

            .node-icon {
              color: #67C23A;
            }
          }
        }

        // 待处理节点样式
        &.pending-node {
          .node-content {
            opacity: 0.7;
            border-color: #E4E7ED;

            .node-icon {
              color: #C0C4CC;
            }
          }
        }

        // 开始节点特殊样式
        &.start-node {
          .node-content {
            border-color: #67C23A;
            background: #F0F9FF;
            justify-content: center;

            i {
              color: #67C23A;
              margin-right: 8px;
            }
          }
        }

        // 结束节点特殊样式
        &.end-node {
          .node-content {
            border-color: #909399;
            background: #F5F7FA;
            justify-content: center;

            i {
              color: #909399;
              margin-right: 8px;
            }
          }
        }

        // 悬停效果
        &:not(.start-node):not(.end-node):hover {
          .node-content {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            cursor: pointer;
          }
        }
      }

      .process-line {
        width: 2px;
        height: 30px;
        background: linear-gradient(to bottom, #409EFF, #67C23A);
        margin: -10px 0;
      }
    }

    .process-stats {
      margin-top: 20px;
      padding: 20px;
      background: #F8F9FA;
      border-radius: 8px;

      .stat-item {
        text-align: center;
        padding: 16px;
        background: white;
        border-radius: 6px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #409EFF;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .comment-content {
    background: #F5F7FA;
    padding: 12px;
    border-radius: 4px;
    line-height: 1.6;
    white-space: pre-wrap;
  }

  .attachment-list {
    .attachment-item {
      display: block;
      margin-bottom: 8px;

      i {
        margin-right: 8px;
      }
    }
  }
}

// 动画效果
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .budget-approval-process {
    padding: 10px;

    .process-node {
      width: 100% !important;

      .node-content {
        flex-direction: column;
        text-align: center;
      }
    }
  }
}
</style>