<template>
  <div class="task-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算任务管理</h2>
      <p>管理预算编制任务，支持任务完整生命周期管理</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ taskStats.total || 0 }}</div>
            <div class="stat-label">总任务数</div>
          </div>
          <i class="el-icon-s-order stat-icon" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ taskStats.inProgress || 0 }}</div>
            <div class="stat-label">进行中</div>
          </div>
          <i class="el-icon-loading stat-icon" style="color: #409EFF" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ taskStats.completed || 0 }}</div>
            <div class="stat-label">已完成</div>
          </div>
          <i class="el-icon-circle-check stat-icon" style="color: #67C23A" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ taskStats.overdue || 0 }}</div>
            <div class="stat-label">已超期</div>
          </div>
          <i class="el-icon-warning stat-icon" style="color: #F56C6C" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="任务名称">
          <el-input
            v-model="queryForm.taskName"
            placeholder="请输入任务名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="任务编码">
          <el-input
            v-model="queryForm.taskCode"
            placeholder="请输入任务编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select
            v-model="queryForm.taskType"
            placeholder="请选择任务类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in taskTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select
            v-model="queryForm.taskStatus"
            placeholder="请选择任务状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in taskStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度">
          <el-select
            v-model="queryForm.budgetYear"
            placeholder="请选择预算年度"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="year in budgetYearOptions"
              :key="year"
              :label="year + '年'"
              :value="year"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="toolbar-card" shadow="never">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            新增任务
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="!multipleSelection.length"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button
            type="success"
            icon="el-icon-video-play"
            :disabled="!multipleSelection.length"
            @click="handleBatchStart"
          >
            批量启动
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-circle-close"
            :disabled="!multipleSelection.length"
            @click="handleBatchCancel"
          >
            批量取消
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button
              :type="viewMode === 'all' ? 'primary' : 'default'"
              @click="viewMode = 'all'"
            >
              全部任务
            </el-button>
            <el-button
              :type="viewMode === 'my' ? 'primary' : 'default'"
              @click="viewMode = 'my'"
            >
              我的任务
            </el-button>
            <el-button
              :type="viewMode === 'assigned' ? 'primary' : 'default'"
              @click="viewMode = 'assigned'"
            >
              分配给我
            </el-button>
          </el-button-group>
          <el-button icon="el-icon-download" @click="handleExport">
            导出
          </el-button>
          <el-button icon="el-icon-upload2" @click="handleImport">
            导入
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="taskCode" label="任务编码" width="120" />
        <el-table-column prop="taskName" label="任务名称" min-width="150" />
        <el-table-column prop="taskType" label="任务类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTaskTypeTagType(scope.row.taskType)">
              {{ formatTaskType(scope.row.taskType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="taskStatus" label="任务状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTaskStatusTagType(scope.row.taskStatus)">
              {{ formatTaskStatus(scope.row.taskStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.progress || 0"
              :status="getProgressStatus(scope.row.progress)"
              :stroke-width="6"
            />
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" size="mini">
              {{ formatPriority(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assigneeName" label="分配人" width="100" />
        <el-table-column prop="budgetYear" label="预算年度" width="100" align="center" />
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="dueDate" label="结束日期" width="110">
          <template slot-scope="scope">
            <span :class="{ 'overdue-date': isOverdue(scope.row) }">
              {{ scope.row.dueDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-dropdown @command="handleTaskAction($event, scope.row)">
              <el-button type="text" size="small">
                操作<i class="el-icon-arrow-down el-icon--right" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-if="['draft', 'PENDING'].includes(scope.row.taskStatus)"
                  command="start"
                  icon="el-icon-video-play"
                >
                  启动
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="['in_progress', 'IN_PROGRESS'].includes(scope.row.taskStatus)"
                  command="complete"
                  icon="el-icon-circle-check"
                >
                  完成
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="['in_progress', 'IN_PROGRESS'].includes(scope.row.taskStatus)"
                  command="submit"
                  icon="el-icon-upload"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="['pending_approval'].includes(scope.row.taskStatus)"
                  command="approve"
                  icon="el-icon-check"
                >
                  审批通过
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="['pending_approval'].includes(scope.row.taskStatus)"
                  command="reject"
                  icon="el-icon-close"
                >
                  审批拒绝
                </el-dropdown-item>
                <el-dropdown-item command="assign" icon="el-icon-user">
                  分配任务
                </el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">
                  复制任务
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="!['completed', 'cancelled', 'COMPLETED', 'CANCELLED'].includes(scope.row.taskStatus)"
                  command="cancel"
                  icon="el-icon-circle-close"
                >
                  取消任务
                </el-dropdown-item>
                <el-dropdown-item command="reset" icon="el-icon-refresh">
                  重置任务
                </el-dropdown-item>
                <el-dropdown-item
                  command="delete"
                  icon="el-icon-delete"
                  style="color: #f56c6c"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="任务编码" prop="taskCode">
                  <el-input v-model="form.taskCode" placeholder="请输入任务编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="任务名称" prop="taskName">
                  <el-input v-model="form.taskName" placeholder="请输入任务名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="任务类型" prop="taskType">
                  <el-select v-model="form.taskType" placeholder="请选择任务类型" style="width: 100%">
                    <el-option
                      v-for="item in taskTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
                    <el-option
                      v-for="item in priorityOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预算年度" prop="budgetYear">
                  <el-select v-model="form.budgetYear" placeholder="请选择预算年度" style="width: 100%">
                    <el-option
                      v-for="year in budgetYearOptions"
                      :key="year"
                      :label="year + '年'"
                      :value="year"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否启用">
                  <el-switch v-model="form.isEnabled" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开始日期">
                  <el-date-picker
                    v-model="form.startDate"
                    type="date"
                    placeholder="选择开始日期"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结束日期">
                  <el-date-picker
                    v-model="form.dueDate"
                    type="date"
                    placeholder="选择结束日期"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="任务详情" name="detail">
            <el-form-item label="任务描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="4"
                placeholder="请输入任务描述"
              />
            </el-form-item>
            <el-form-item label="任务要求">
              <el-input
                v-model="form.requirements"
                type="textarea"
                :rows="3"
                placeholder="请输入任务要求"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  createTask,
  updateTask,
  deleteTask,
  batchDeleteTasks,
  getTask,
  getTaskPage,
  enableTask,
  disableTask,
  startTask,
  completeTask,
  cancelTask,
  resetTask,
  assignTask,
  submitTaskApproval,
  approveTaskAction,
  rejectTaskAction,
  copyTask,
  getMyCreatedTasks,
  getMyAssignedTasks,
  countByTaskStatus
} from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'TaskManagement',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      viewMode: 'all', // all | my | assigned
      activeTab: 'basic',
      tableData: [],
      multipleSelection: [],
      taskStats: {},
      queryForm: {
        taskName: '',
        taskCode: '',
        taskType: '',
        taskStatus: '',
        budgetYear: null
      },
      form: {
        taskId: '',
        taskCode: '',
        taskName: '',
        taskType: '',
        priority: 'MEDIUM',
        budgetYear: new Date().getFullYear(),
        startDate: '',
        dueDate: '',
        isEnabled: 1,
        description: '',
        requirements: '',
        remark: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      taskTypeOptions: [
        // 后端返回值
        { label: '年度预算', value: 'ANNUAL_BUDGET' },
        { label: '季度预算', value: 'QUARTERLY_BUDGET' },
        { label: '月度预算', value: 'MONTHLY_BUDGET' },
        { label: '预算调整', value: 'ADJUSTMENT' },
        { label: '预算预测', value: 'FORECAST' },
        // 兼容旧值
        { label: '预算编制', value: 'budget_preparation' },
        { label: '预算调整', value: 'budget_adjustment' },
        { label: '预算审批', value: 'budget_approval' },
        { label: '预算分析', value: 'budget_analysis' },
        { label: '预算控制', value: 'budget_control' },
        { label: '数据导入', value: 'data_import' },
        { label: '数据导出', value: 'data_export' }
      ],
      taskStatusOptions: [
        { label: '待开始', value: 'PENDING' },
        { label: '进行中', value: 'IN_PROGRESS' },
        { label: '已完成', value: 'COMPLETED' },
        { label: '已取消', value: 'CANCELLED' },
        { label: '已暂停', value: 'PAUSED' },
        // 兼容旧值
        { label: '草稿', value: 'draft' },
        { label: '已分配', value: 'assigned' },
        { label: '进行中', value: 'in_progress' },
        { label: '待审批', value: 'pending_approval' },
        { label: '已审批', value: 'approved' },
        { label: '已拒绝', value: 'rejected' },
        { label: '已完成', value: 'completed' },
        { label: '已取消', value: 'cancelled' }
      ],
      priorityOptions: [
        { label: '高', value: 'HIGH' },
        { label: '中', value: 'MEDIUM' },
        { label: '低', value: 'LOW' },
        // 兼容旧值
        { label: '高', value: 'high' },
        { label: '中', value: 'medium' },
        { label: '低', value: 'low' }
      ],
      budgetYearOptions: [],
      rules: {
        taskCode: [
          { required: true, message: '请输入任务编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        taskType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.initBudgetYearOptions()
    this.loadData()
    this.loadTaskStats()
  },
  methods: {
    // 初始化预算年度选项
    initBudgetYearOptions() {
      const currentYear = new Date().getFullYear()
      for (let i = currentYear - 2; i <= currentYear + 3; i++) {
        this.budgetYearOptions.push(i)
      }
    },

    // 加载数据
    async loadData() {
      this.loading = true
      try {
        let apiCall
        const params = { ...this.queryForm }
        
        if (this.viewMode === 'my') {
          // 查询我创建的任务
          apiCall = getMyCreatedTasks(params)
        } else if (this.viewMode === 'assigned') {
          // 查询分配给我的任务
          apiCall = getMyAssignedTasks(params)
        } else {
          // 查询所有任务
          apiCall = getTaskPage(this.pagination.current, this.pagination.size, params)
        }
        
        const response = await apiCall
        if (response.code === 1) {
          if (this.viewMode === 'all') {
            this.tableData = response.data.tlist || []
            this.pagination.total = response.data.totalRecord || 0
          } else {
            this.tableData = response.data || []
            this.pagination.total = this.tableData.length
          }
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载任务统计
    async loadTaskStats() {
      try {
        const response = await countByTaskStatus()
        if (response.code === 1) {
          // 后端返回 Map: { totalCount, inProgressCount, completedCount, pendingCount, pausedCount, cancelledCount }
          const stats = response.data || {}
          this.taskStats = {
            total: stats.totalCount || 0,
            inProgress: stats.inProgressCount || 0,
            completed: stats.completedCount || 0,
            overdue: 0
          }
        }
      } catch (error) {
        console.error('加载任务统计失败', error)
      }
    },

    // 查询
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        taskName: '',
        taskCode: '',
        taskType: '',
        taskStatus: '',
        budgetYear: null
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增任务'
      this.isEdit = false
      this.activeTab = 'basic'
      this.resetForm()
      this.dialogVisible = true
    },

    // 编辑
    async handleEdit(row) {
      this.dialogTitle = '编辑任务'
      this.isEdit = true
      this.activeTab = 'basic'
      try {
        const response = await getTask(row.taskId)
        if (response.code === 1) {
          this.form = { ...response.data }
          this.dialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      }
    },

    // 提交
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        if (this.isEdit) {
          const response = await updateTask(this.form.taskId, this.form)
          if (response.code === 1) {
            this.$message.success('更新成功')
            this.dialogVisible = false
            this.loadData()
            this.loadTaskStats()
          }
        } else {
          const response = await createTask(this.form)
          if (response.code === 1) {
            this.$message.success('创建成功')
            this.dialogVisible = false
            this.loadData()
            this.loadTaskStats()
          }
        }
      } catch (error) {
        // 表单验证失败或接口调用失败
      }
    },

    // 任务操作
    async handleTaskAction(command, row) {
      try {
        let response
        let message = ''
        
        switch (command) {
          case 'start':
            response = await startTask(row.taskId)
            message = '启动任务成功'
            break
          case 'complete':
            response = await completeTask(row.taskId)
            message = '完成任务成功'
            break
          case 'submit':
            response = await submitTaskApproval(row.taskId)
            message = '提交审批成功'
            break
          case 'approve':
            response = await approveTaskAction(row.taskId)
            message = '审批通过成功'
            break
          case 'reject':
            response = await rejectTaskAction(row.taskId)
            message = '审批拒绝成功'
            break
          case 'cancel':
            response = await cancelTask(row.taskId)
            message = '取消任务成功'
            break
          case 'reset':
            response = await resetTask(row.taskId)
            message = '重置任务成功'
            break
          case 'assign':
            this.handleAssignTask(row)
            return
          case 'copy':
            this.handleCopyTask(row)
            return
          case 'delete':
            this.handleDelete(row)
            return
        }
        
        if (response && response.code === 1) {
          this.$message.success(message)
          this.loadData()
          this.loadTaskStats()
        }
      } catch (error) {
        this.$message.error('操作失败')
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        taskId: '',
        taskCode: '',
        taskName: '',
        taskType: '',
        priority: 'MEDIUM',
        budgetYear: new Date().getFullYear(),
        startDate: '',
        dueDate: '',
        isEnabled: 1,
        description: '',
        requirements: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    // 格式化任务类型
    formatTaskType(type) {
      const option = this.taskTypeOptions.find(item => item.value === type)
      return option ? option.label : type
    },

    // 格式化任务状态
    formatTaskStatus(status) {
      const option = this.taskStatusOptions.find(item => item.value === status)
      return option ? option.label : status
    },

    // 格式化优先级
    formatPriority(priority) {
      const option = this.priorityOptions.find(item => item.value === priority)
      return option ? option.label : priority
    },

    // 获取任务类型标签类型
    getTaskTypeTagType(type) {
      const typeMap = {
        // 后端返回值
        ANNUAL_BUDGET: 'primary',
        QUARTERLY_BUDGET: 'success',
        MONTHLY_BUDGET: 'warning',
        ADJUSTMENT: 'danger',
        FORECAST: 'info',
        // 兼容旧值
        budget_preparation: 'primary',
        budget_adjustment: 'warning',
        budget_approval: 'success',
        budget_analysis: 'info',
        budget_control: 'danger',
        data_import: 'default',
        data_export: 'default'
      }
      return typeMap[type] || 'default'
    },

    // 获取任务状态标签类型
    getTaskStatusTagType(status) {
      const statusMap = {
        // 后端返回值
        PENDING: 'info',
        IN_PROGRESS: 'warning',
        COMPLETED: 'success',
        CANCELLED: 'info',
        PAUSED: 'default',
        // 兼容旧值
        draft: 'info',
        assigned: 'primary',
        in_progress: 'warning',
        pending_approval: 'primary',
        approved: 'success',
        rejected: 'danger',
        completed: 'success',
        cancelled: 'info'
      }
      return statusMap[status] || 'default'
    },

    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const priorityMap = {
        // 后端返回值
        HIGH: 'danger',
        MEDIUM: 'warning',
        LOW: 'success',
        // 兼容旧值
        high: 'danger',
        medium: 'warning',
        low: 'success'
      }
      return priorityMap[priority] || 'default'
    },

    // 获取进度状态
    getProgressStatus(progress) {
      if (progress >= 100) return 'success'
      if (progress >= 80) return 'warning'
      return null
    },

    // 检查是否超期
    isOverdue(row) {
      if (!row.dueDate || ['completed', 'cancelled', 'COMPLETED', 'CANCELLED'].includes(row.taskStatus)) {
        return false
      }
      return new Date(row.dueDate) < new Date()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 状态改变
    async handleStatusChange(row) {
      const previousValue = row.isEnabled
      try {
        if (row.isEnabled) {
          await enableTask(row.taskId)
          this.$message.success('启用成功')
        } else {
          await disableTask(row.taskId)
          this.$message.success('禁用成功')
        }
      } catch (error) {
        row.isEnabled = previousValue
        this.$message.error('操作失败')
      }
    },

    // 删除单个任务
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该任务吗？删除后不可恢复', '提示', { type: 'warning' })
        const response = await deleteTask(row.taskId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadTaskStats()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (!this.multipleSelection.length) return
      try {
        await this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 个任务吗？`, '提示', { type: 'warning' })
        const ids = this.multipleSelection.map(item => item.taskId)
        const response = await batchDeleteTasks(ids)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadTaskStats()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },

    // 批量启动
    async handleBatchStart() {
      if (!this.multipleSelection.length) return
      try {
        for (const row of this.multipleSelection) {
          await startTask(row.taskId)
        }
        this.$message.success('批量启动成功')
        this.loadData()
        this.loadTaskStats()
      } catch (error) {
        this.$message.error('批量启动失败')
      }
    },

    // 批量取消
    async handleBatchCancel() {
      if (!this.multipleSelection.length) return
      try {
        await this.$confirm(`确认取消选中的 ${this.multipleSelection.length} 个任务吗？`, '提示', { type: 'warning' })
        for (const row of this.multipleSelection) {
          await cancelTask(row.taskId)
        }
        this.$message.success('批量取消成功')
        this.loadData()
        this.loadTaskStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量取消失败')
        }
      }
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中')
    },

    // 导入
    handleImport() {
      this.$message.info('导入功能开发中')
    },

    // 分配任务
    async handleAssignTask(row) {
      try {
        const { value } = await this.$prompt('请输入被分配人ID', '分配任务', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S+/,
          inputErrorMessage: '请输入有效的用户ID'
        })
        const response = await assignTask(row.taskId, value)
        if (response.code === 1) {
          this.$message.success('分配成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('分配失败')
        }
      }
    },

    // 复制任务
    async handleCopyTask(row) {
      try {
        const response = await copyTask(row.taskId)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('复制失败')
      }
    }
  },
  watch: {
    viewMode() {
      this.pagination.current = 1
      this.loadData()
    }
  }
}
</script>

<style scoped>
.task-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.stat-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 48px;
  color: #DCDFE6;
}

.search-card,
.toolbar-card,
.table-card {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.overdue-date {
  color: #f56c6c;
  font-weight: bold;
}
</style>
