<template>
  <el-dialog
    title="任务详情"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <div v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务名称">
          {{ taskDetail.taskName }}
        </el-descriptions-item>
        <el-descriptions-item label="任务类型">
          <el-tag :type="getTaskTypeTagType(taskDetail.taskType)">
            {{ getTaskTypeLabel(taskDetail.taskType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="任务分类">
          {{ getTaskCategoryLabel(taskDetail.taskCategory) }}
        </el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getTaskStatusTagType(taskDetail.taskStatus)">
            {{ getTaskStatusLabel(taskDetail.taskStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报送周期">
          {{ getSubmitCycleLabel(taskDetail.submitCycle) }}
        </el-descriptions-item>
        <el-descriptions-item label="开始日期">
          {{ taskDetail.startDate }}
        </el-descriptions-item>
        <el-descriptions-item label="结束日期">
          {{ taskDetail.endDate || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">
          {{ taskDetail.publishTime || '未发布' }}
        </el-descriptions-item>
        <el-descriptions-item label="数据模板" :span="2">
          {{ taskDetail.dataTemplate || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="任务描述" :span="2">
          <div class="description-content">
            {{ taskDetail.taskDescription || '无' }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          <div class="description-content">
            {{ taskDetail.remark || '无' }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ taskDetail.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ taskDetail.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="更新人">
          {{ taskDetail.updateBy || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ taskDetail.updateTime }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 操作按钮 -->
      <div class="action-buttons" v-if="taskDetail.taskId">
        <el-button
          v-if="taskDetail.taskStatus === 'DRAFT'"
          type="primary"
          @click="handlePublish"
          icon="el-icon-upload2"
        >
          发布任务
        </el-button>
        <el-button
          v-if="taskDetail.taskStatus === 'ACTIVE'"
          type="warning"
          @click="handleCancel"
          icon="el-icon-close"
        >
          取消任务
        </el-button>
        <el-button
          v-if="taskDetail.taskStatus === 'ACTIVE'"
          type="success"
          @click="handleComplete"
          icon="el-icon-check"
        >
          完成任务
        </el-button>
        <el-button
          type="info"
          @click="handleCopy"
          icon="el-icon-copy-document"
        >
          复制任务
        </el-button>
      </div>

      <!-- 报送记录统计 -->
      <div class="statistics-section" v-if="taskDetail.taskId">
        <h3>报送记录统计</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ recordStatistics.totalCount || 0 }}</div>
              <div class="stat-label">总记录数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ recordStatistics.submittedCount || 0 }}</div>
              <div class="stat-label">已提交</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ recordStatistics.approvedCount || 0 }}</div>
              <div class="stat-label">已审核</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ recordStatistics.rejectedCount || 0 }}</div>
              <div class="stat-label">已拒绝</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getTaskDetail,
  publishTask,
  cancelTask,
  completeTask,
  copyTask
} from '@/api/stateAssets/dataCollection'

export default {
  name: 'TaskDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    taskId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      taskDetail: {},
      recordStatistics: {}
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
  watch: {
    visible(val) {
      if (val && this.taskId) {
        this.fetchTaskDetail()
      }
    }
  },
  methods: {
    // 获取任务详情
    async fetchTaskDetail() {
      this.loading = true
      try {
        const { data } = await getTaskDetail({ taskId: this.taskId })
        this.taskDetail = data || {}
        
        // 模拟获取报送记录统计（实际应该调用相应的API）
        this.recordStatistics = {
          totalCount: Math.floor(Math.random() * 100),
          submittedCount: Math.floor(Math.random() * 80),
          approvedCount: Math.floor(Math.random() * 60),
          rejectedCount: Math.floor(Math.random() * 10)
        }
      } catch (error) {
        this.$message.error('获取任务详情失败')
      } finally {
        this.loading = false
      }
    },

    // 发布任务
    async handlePublish() {
      try {
        await this.$confirm('确认发布该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await publishTask({ taskId: this.taskId })
        this.$message.success('发布成功')
        this.fetchTaskDetail()
        this.$emit('success')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('发布失败')
        }
      }
    },

    // 取消任务
    async handleCancel() {
      try {
        await this.$confirm('确认取消该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await cancelTask({ taskId: this.taskId })
        this.$message.success('取消成功')
        this.fetchTaskDetail()
        this.$emit('success')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消失败')
        }
      }
    },

    // 完成任务
    async handleComplete() {
      try {
        await this.$confirm('确认完成该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await completeTask({ taskId: this.taskId })
        this.$message.success('完成成功')
        this.fetchTaskDetail()
        this.$emit('success')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成失败')
        }
      }
    },

    // 复制任务
    async handleCopy() {
      try {
        await copyTask({ taskId: this.taskId })
        this.$message.success('复制成功')
        this.$emit('success')
      } catch (error) {
        this.$message.error('复制失败')
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    },

    // 获取任务类型标签
    getTaskTypeLabel(taskType) {
      const typeMap = {
        'REGULAR': '定期报送',
        'SPECIAL': '专项报送',
        'URGENT': '紧急报送'
      }
      return typeMap[taskType] || taskType
    },

    // 获取任务类型标签类型
    getTaskTypeTagType(taskType) {
      const typeMap = {
        'REGULAR': '',
        'SPECIAL': 'warning',
        'URGENT': 'danger'
      }
      return typeMap[taskType] || ''
    },

    // 获取任务状态标签
    getTaskStatusLabel(taskStatus) {
      const statusMap = {
        'DRAFT': '草稿',
        'PUBLISHED': '已发布',
        'ACTIVE': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[taskStatus] || taskStatus
    },

    // 获取任务状态标签类型
    getTaskStatusTagType(taskStatus) {
      const statusMap = {
        'DRAFT': 'info',
        'PUBLISHED': 'warning',
        'ACTIVE': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[taskStatus] || ''
    },

    // 获取报送周期标签
    getSubmitCycleLabel(submitCycle) {
      const cycleMap = {
        'DAILY': '日报',
        'WEEKLY': '周报',
        'MONTHLY': '月报',
        'QUARTERLY': '季报',
        'YEARLY': '年报',
        'ONCE': '一次性'
      }
      return cycleMap[submitCycle] || submitCycle
    },

    // 获取任务分类标签
    getTaskCategoryLabel(taskCategory) {
      const categoryMap = {
        'FINANCIAL': '财务数据',
        'OPERATIONAL': '经营数据',
        'GOVERNANCE': '治理数据',
        'RISK': '风险数据',
        'COMPLIANCE': '合规数据',
        'PERFORMANCE': '绩效数据'
      }
      return categoryMap[taskCategory] || taskCategory
    }
  }
}
</script>

<style scoped>
.description-content {
  max-height: 100px;
  overflow-y: auto;
  word-break: break-all;
}

.action-buttons {
  margin: 20px 0;
  text-align: center;
}

.action-buttons .el-button {
  margin: 0 5px;
}

.statistics-section {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.statistics-section h3 {
  margin-bottom: 20px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.stat-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
