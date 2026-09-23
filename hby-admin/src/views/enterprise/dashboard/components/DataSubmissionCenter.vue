<template>
  <div class="data-submission-center">
    <!-- 数据报送概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ submissionStatistics.totalTasks || 0 }}</div>
              <div class="statistics-label">总任务数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ submissionStatistics.inProgressTasks || 0 }}</div>
              <div class="statistics-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ submissionStatistics.completedTasks || 0 }}</div>
              <div class="statistics-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-value">{{ submissionStatistics.overdueTasks || 0 }}</div>
              <div class="statistics-label">逾期任务</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能操作区 -->
    <el-card class="mb-20">
      <div slot="header" class="card-header">
        <span class="card-title">数据报送管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="handleAddTask" icon="el-icon-plus">新增任务</el-button>
          <el-button @click="handleBatchOperation" icon="el-icon-setting">批量操作</el-button>
          <el-button @click="handleExport" icon="el-icon-download">导出</el-button>
          <el-button @click="refreshData" icon="el-icon-refresh">刷新</el-button>
        </div>
      </div>

      <!-- 查询条件 -->
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="任务名称">
          <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select v-model="queryForm.taskStatus" placeholder="请选择任务状态" clearable style="width: 150px;">
            <el-option label="待开始" value="待开始"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="已暂停" value="已暂停"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="queryForm.priority" placeholder="请选择优先级" clearable style="width: 120px;">
            <el-option label="紧急" value="紧急"></el-option>
            <el-option label="重要" value="重要"></el-option>
            <el-option label="一般" value="一般"></el-option>
            <el-option label="低" value="低"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报送类型">
          <el-select v-model="queryForm.submissionType" placeholder="请选择报送类型" clearable style="width: 150px;">
            <el-option label="定期报送" value="定期报送"></el-option>
            <el-option label="临时报送" value="临时报送"></el-option>
            <el-option label="专项报送" value="专项报送"></el-option>
            <el-option label="应急报送" value="应急报送"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        :data="taskList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="taskName" label="任务名称" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="submissionType" label="报送类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getSubmissionTypeTag(scope.row.submissionType)">
              {{ scope.row.submissionType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityTag(scope.row.priority)" size="small">
              {{ scope.row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="taskStatus" label="任务状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.taskStatus)">
              {{ scope.row.taskStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completionProgress" label="完成进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="parseFloat(scope.row.completionProgress || 0)" :stroke-width="8"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="120"></el-table-column>
        <el-table-column prop="deadline" label="截止时间" width="180">
          <template slot-scope="scope">
            <span :class="getDeadlineClass(scope.row.deadline, scope.row.taskStatus)">
              {{ formatDateTime(scope.row.deadline) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="qualityScore" label="质量评分" width="120">
          <template slot-scope="scope">
            <el-rate
              v-if="scope.row.qualityScore"
              :value="parseFloat(scope.row.qualityScore) / 20"
              disabled
              show-score
              text-color="#ff9900">
            </el-rate>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button @click="handleView(scope.row)" type="text" size="small">查看</el-button>
            <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'start', row: scope.row}" v-if="scope.row.taskStatus === '待开始'">启动</el-dropdown-item>
                <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="scope.row.taskStatus === '进行中'">完成</el-dropdown-item>
                <el-dropdown-item :command="{action: 'pause', row: scope.row}" v-if="scope.row.taskStatus === '进行中'">暂停</el-dropdown-item>
                <el-dropdown-item :command="{action: 'restart', row: scope.row}" v-if="scope.row.taskStatus === '已暂停'">重启</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="scope.row.taskStatus !== '已完成' && scope.row.taskStatus !== '已取消'">取消</el-dropdown-item>
                <el-dropdown-item :command="{action: 'feedback', row: scope.row}">反馈</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        class="pagination">
      </el-pagination>
    </el-card>

    <!-- 即将到期任务提醒 -->
    <el-card class="mb-20" v-if="upcomingTasks.length > 0">
      <div slot="header" class="card-header">
        <span class="card-title">即将到期任务提醒</span>
        <el-tag type="warning" size="small">{{ upcomingTasks.length }}个任务</el-tag>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="task in upcomingTasks"
          :key="task.taskId"
          :timestamp="formatDateTime(task.deadline)"
          placement="top">
          <el-card>
            <h4>{{ task.taskName }}</h4>
            <p>负责人：{{ task.responsiblePerson }} | 优先级：{{ task.priority }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 任务详情对话框 -->
    <TaskDetailDialog
      :visible.sync="taskDetailVisible"
      :task-data="currentTask"
      @refresh="refreshData"
    />

    <!-- 任务编辑对话框 -->
    <TaskEditDialog
      :visible.sync="taskEditVisible"
      :task-data="currentTask"
      :is-edit="isEdit"
      @refresh="refreshData"
    />

    <!-- 任务完成对话框 -->
    <TaskCompleteDialog
      :visible.sync="taskCompleteVisible"
      :task-data="currentTask"
      @refresh="refreshData"
    />

    <!-- 反馈对话框 -->
    <TaskFeedbackDialog
      :visible.sync="taskFeedbackVisible"
      :task-data="currentTask"
      @refresh="refreshData"
    />
  </div>
</template>

<script>
import { dataSubmissionTaskApi } from '@/api/enterprise/dashboard'
import TaskDetailDialog from './TaskDetailDialog'
import TaskEditDialog from './TaskEditDialog'
import TaskCompleteDialog from './TaskCompleteDialog'
import TaskFeedbackDialog from './TaskFeedbackDialog'

export default {
  name: 'DataSubmissionCenter',
  components: {
    TaskDetailDialog,
    TaskEditDialog,
    TaskCompleteDialog,
    TaskFeedbackDialog
  },
  props: {
    enterpriseId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      submissionStatistics: {},
      taskList: [],
      upcomingTasks: [],
      selectedTasks: [],
      queryForm: {
        enterpriseId: '',
        taskName: '',
        taskStatus: '',
        priority: '',
        submissionType: ''
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      taskDetailVisible: false,
      taskEditVisible: false,
      taskCompleteVisible: false,
      taskFeedbackVisible: false,
      currentTask: {},
      isEdit: false
    }
  },
  watch: {
    enterpriseId: {
      handler(newVal) {
        if (newVal) {
          this.queryForm.enterpriseId = newVal
          this.loadData()
        }
      },
      immediate: true
    }
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadTaskList(),
        this.loadUpcomingTasks()
      ])
    },

    async loadStatistics() {
      try {
        const response = await dataSubmissionTaskApi.getStatistics(this.enterpriseId)
        this.submissionStatistics = response.data || {}
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    async loadTaskList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize
        }
        const response = await dataSubmissionTaskApi.getTaskList(params)
        this.taskList = response.data.records || []
        this.pagination.total = response.data.total || 0
      } catch (error) {
        console.error('加载任务列表失败:', error)
        this.$message.error('加载任务列表失败')
      } finally {
        this.loading = false
      }
    },

    async loadUpcomingTasks() {
      try {
        const response = await dataSubmissionTaskApi.getUpcomingTasks(this.enterpriseId, 7)
        this.upcomingTasks = response.data || []
      } catch (error) {
        console.error('加载即将到期任务失败:', error)
      }
    },

    handleQuery() {
      this.pagination.pageNumber = 1
      this.loadTaskList()
    },

    handleReset() {
      this.queryForm = {
        enterpriseId: this.enterpriseId,
        taskName: '',
        taskStatus: '',
        priority: '',
        submissionType: ''
      }
      this.handleQuery()
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadTaskList()
    },

    handleCurrentChange(val) {
      this.pagination.pageNumber = val
      this.loadTaskList()
    },

    handleSelectionChange(selection) {
      this.selectedTasks = selection
    },

    handleAddTask() {
      this.currentTask = { enterpriseId: this.enterpriseId }
      this.isEdit = false
      this.taskEditVisible = true
    },

    handleView(row) {
      this.currentTask = row
      this.taskDetailVisible = true
    },

    handleEdit(row) {
      this.currentTask = { ...row }
      this.isEdit = true
      this.taskEditVisible = true
    },

    async handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'start':
          await this.startTask(row)
          break
        case 'complete':
          this.currentTask = row
          this.taskCompleteVisible = true
          break
        case 'pause':
          await this.pauseTask(row)
          break
        case 'restart':
          await this.restartTask(row)
          break
        case 'cancel':
          await this.cancelTask(row)
          break
        case 'feedback':
          this.currentTask = row
          this.taskFeedbackVisible = true
          break
        case 'delete':
          await this.deleteTask(row)
          break
      }
    },

    async startTask(row) {
      try {
        await dataSubmissionTaskApi.startTask(row.taskId, 'current_user')
        this.$message.success('任务启动成功')
        this.refreshData()
      } catch (error) {
        this.$message.error('任务启动失败')
      }
    },

    async pauseTask(row) {
      try {
        await dataSubmissionTaskApi.pauseTask(row.taskId, 'current_user')
        this.$message.success('任务暂停成功')
        this.refreshData()
      } catch (error) {
        this.$message.error('任务暂停失败')
      }
    },

    async restartTask(row) {
      try {
        await dataSubmissionTaskApi.restartTask(row.taskId, 'current_user')
        this.$message.success('任务重启成功')
        this.refreshData()
      } catch (error) {
        this.$message.error('任务重启失败')
      }
    },

    async cancelTask(row) {
      try {
        await this.$confirm('确认取消该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await dataSubmissionTaskApi.cancelTask(row.taskId, 'current_user')
        this.$message.success('任务取消成功')
        this.refreshData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('任务取消失败')
        }
      }
    },

    async deleteTask(row) {
      try {
        await this.$confirm('确认删除该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await dataSubmissionTaskApi.deleteTask(row.taskId)
        this.$message.success('删除成功')
        this.refreshData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    handleBatchOperation() {
      if (this.selectedTasks.length === 0) {
        this.$message.warning('请选择要操作的任务')
        return
      }
      // 实现批量操作逻辑
    },

    handleExport() {
      // 实现导出逻辑
    },

    refreshData() {
      this.loadData()
    },

    getSubmissionTypeTag(type) {
      const tagMap = {
        '定期报送': '',
        '临时报送': 'warning',
        '专项报送': 'success',
        '应急报送': 'danger'
      }
      return tagMap[type] || ''
    },

    getPriorityTag(priority) {
      const tagMap = {
        '紧急': 'danger',
        '重要': 'warning',
        '一般': '',
        '低': 'info'
      }
      return tagMap[priority] || ''
    },

    getStatusTag(status) {
      const tagMap = {
        '待开始': 'info',
        '进行中': 'warning',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return tagMap[status] || ''
    },

    getDeadlineClass(deadline, status) {
      if (status === '已完成') return ''
      const now = new Date()
      const deadlineDate = new Date(deadline)
      const diffDays = Math.ceil((deadlineDate - now) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'text-danger' // 已逾期
      if (diffDays <= 3) return 'text-warning' // 即将到期
      return ''
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.data-submission-center {
  padding: 20px;
}

.statistics-card {
  height: 120px;
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.statistics-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.statistics-icon.progress {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.overdue {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.statistics-info {
  flex: 1;
}

.statistics-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.card-actions {
  display: flex;
  gap: 10px;
}

.query-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.text-danger {
  color: #f56c6c;
}

.text-warning {
  color: #e6a23c;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
