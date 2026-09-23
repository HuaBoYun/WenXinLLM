<template>
  <div class="data-collection-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input
            v-model="queryForm.taskName"
            placeholder="请输入任务名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="queryForm.taskType" placeholder="请选择任务类型" clearable style="width: 150px">
            <el-option label="定期报送" value="REGULAR" />
            <el-option label="专项报送" value="SPECIAL" />
            <el-option label="紧急报送" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态" prop="taskStatus">
          <el-select v-model="queryForm.taskStatus" placeholder="请选择任务状态" clearable style="width: 150px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="进行中" value="ACTIVE" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="报送周期" prop="submitCycle">
          <el-select v-model="queryForm.submitCycle" placeholder="请选择报送周期" clearable style="width: 150px">
            <el-option label="日报" value="DAILY" />
            <el-option label="周报" value="WEEKLY" />
            <el-option label="月报" value="MONTHLY" />
            <el-option label="季报" value="QUARTERLY" />
            <el-option label="年报" value="YEARLY" />
            <el-option label="一次性" value="ONCE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd" icon="el-icon-plus">新增任务</el-button>
      <el-button type="success" @click="handleBatchPublish" icon="el-icon-upload2" :disabled="!multipleSelection.length">批量发布</el-button>
      <el-button type="danger" @click="handleBatchDelete" icon="el-icon-delete" :disabled="!multipleSelection.length">批量删除</el-button>
      <el-button type="info" @click="handleExport" icon="el-icon-download">导出</el-button>
      <el-button type="warning" @click="handleImport" icon="el-icon-upload">导入</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="list"
      @selection-change="handleSelectionChange"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="taskName" label="任务名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="taskType" label="任务类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTaskTypeTagType(scope.row.taskType)">
            {{ getTaskTypeLabel(scope.row.taskType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="taskCategory" label="任务分类" width="100" align="center">
        <template slot-scope="scope">
          {{ getTaskCategoryLabel(scope.row.taskCategory) }}
        </template>
      </el-table-column>
      <el-table-column prop="taskStatus" label="任务状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTaskStatusTagType(scope.row.taskStatus)">
            {{ getTaskStatusLabel(scope.row.taskStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitCycle" label="报送周期" width="100" align="center">
        <template slot-scope="scope">
          {{ getSubmitCycleLabel(scope.row.submitCycle) }}
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始日期" width="120" align="center" />
      <el-table-column prop="endDate" label="结束日期" width="120" align="center" />
      <el-table-column prop="createBy" label="创建人" width="100" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleDetail(scope.row)" icon="el-icon-view">详情</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
          <el-dropdown @command="handleCommand" trigger="click">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'publish', row: scope.row}" v-if="scope.row.taskStatus === 'DRAFT'">
                <i class="el-icon-upload2"></i> 发布
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="scope.row.taskStatus === 'ACTIVE'">
                <i class="el-icon-close"></i> 取消
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="scope.row.taskStatus === 'ACTIVE'">
                <i class="el-icon-check"></i> 完成
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                <i class="el-icon-copy-document"></i> 复制
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                <i class="el-icon-delete"></i> 删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <!-- 任务表单对话框 -->
    <TaskForm
      :visible.sync="taskFormVisible"
      :form-data="taskFormData"
      :is-edit="isEdit"
      @success="handleFormSuccess"
    />

    <!-- 任务详情对话框 -->
    <TaskDetail
      :visible.sync="taskDetailVisible"
      :task-id="currentTaskId"
    />

    <!-- 导入对话框 -->
    <ImportDialog
      :visible.sync="importVisible"
      @success="handleImportSuccess"
    />
  </div>
</template>

<script>
import {
  getTaskList,
  deleteTask,
  batchDeleteTask,
  publishTask,
  cancelTask,
  completeTask,
  copyTask,
  exportTaskList
} from '@/api/stateAssets/dataCollection'
import TaskForm from './components/TaskForm'
import TaskDetail from './components/TaskDetail'
import ImportDialog from './components/ImportDialog'

export default {
  name: 'DataCollection',
  components: {
    TaskForm,
    TaskDetail,
    ImportDialog
  },
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        taskName: '',
        taskType: '',
        taskStatus: '',
        submitCycle: '',
        taskCategory: '',
        createBy: ''
      },
      taskFormVisible: false,
      taskFormData: {},
      isEdit: false,
      taskDetailVisible: false,
      currentTaskId: '',
      importVisible: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getTaskList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
      } catch (error) {
        this.$message.warning('获取数据暂未开放')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },

    // 多选改变
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 新增
    handleAdd() {
      this.taskFormData = {}
      this.isEdit = false
      this.taskFormVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.taskFormData = { ...row }
      this.isEdit = true
      this.taskFormVisible = true
    },

    // 详情
    handleDetail(row) {
      this.currentTaskId = row.taskId
      this.taskDetailVisible = true
    },

    // 表单成功回调
    handleFormSuccess() {
      this.fetchData()
    },

    // 下拉菜单命令处理
    async handleCommand({ action, row }) {
      switch (action) {
        case 'publish':
          await this.handlePublish(row)
          break
        case 'cancel':
          await this.handleCancel(row)
          break
        case 'complete':
          await this.handleComplete(row)
          break
        case 'copy':
          await this.handleCopy(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 发布任务
    async handlePublish(row) {
      try {
        await this.$confirm('确认发布该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await publishTask({ taskId: row.taskId })
        this.$message.success('发布成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.warning('发布暂未开放')
        }
      }
    },

    // 取消任务
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await cancelTask({ taskId: row.taskId })
        this.$message.success('取消成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.warning('取消暂未开放')
        }
      }
    },

    // 完成任务
    async handleComplete(row) {
      try {
        await this.$confirm('确认完成该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await completeTask({ taskId: row.taskId })
        this.$message.success('完成成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.warning('完成暂未开放')
        }
      }
    },

    // 复制任务
    async handleCopy(row) {
      try {
        await copyTask({ taskId: row.taskId })
        this.$message.success('复制成功')
        this.fetchData()
      } catch (error) {
        this.$message.warning('复制暂未开放')
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteTask({ taskId: row.taskId })
        this.$message.success('删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 批量发布
    async handleBatchPublish() {
      const draftTasks = this.multipleSelection.filter(item => item.taskStatus === 'DRAFT')
      if (draftTasks.length === 0) {
        this.$message.warning('请选择草稿状态的任务')
        return
      }

      try {
        await this.$confirm(`确认发布选中的 ${draftTasks.length} 个任务吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        for (const task of draftTasks) {
          await publishTask({ taskId: task.taskId })
        }
        
        this.$message.success('批量发布成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.warning('批量发布暂未开放')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 个任务吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const taskIds = this.multipleSelection.map(item => item.taskId)
        await batchDeleteTask({ taskIds })
        this.$message.success('批量删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportTaskList(this.queryForm)
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '数据报送任务列表.xls'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.warning('导出暂未开放')
      }
    },

    // 导入
    handleImport() {
      this.importVisible = true
    },

    // 导入成功回调
    handleImportSuccess() {
      this.fetchData()
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
.data-collection-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
