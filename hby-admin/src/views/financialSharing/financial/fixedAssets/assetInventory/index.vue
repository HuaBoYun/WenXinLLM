<template>
  <div class="asset-inventory-container">
    <div class="page-header">
      <h2>资产盘点管理</h2>
      <p>管理固定资产盘点任务、盘点结果、差异处理等</p>
    </div>
    
    <!-- 盘点统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalTasks }}</div>
              <div class="stat-label">盘点任务</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completedTasks }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon difference">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.differenceCount }}</div>
              <div class="stat-label">盘点差异</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.progressRate }}%</div>
              <div class="stat-label">完成率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 操作区域 -->
    <div class="operation-section">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="盘点任务" name="tasks">
          <div class="tasks-panel">
            <!-- 搜索条件 -->
            <div class="search-bar">
              <el-form :model="searchForm" :inline="true" size="small">
                <el-form-item label="任务编号">
                  <el-input v-model="searchForm.taskNumber" placeholder="请输入任务编号" style="width: 150px" />
                </el-form-item>
                <el-form-item label="盘点类型">
                  <el-select v-model="searchForm.inventoryType" placeholder="请选择类型" style="width: 120px">
                    <el-option label="全盘" value="FULL" />
                    <el-option label="抽盘" value="SAMPLE" />
                    <el-option label="循环盘点" value="CYCLE" />
                  </el-select>
                </el-form-item>
                <el-form-item label="任务状态">
                  <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px">
                    <el-option label="待执行" value="PENDING" />
                    <el-option label="进行中" value="IN_PROGRESS" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSearchTasks">查询</el-button>
                  <el-button @click="handleResetSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 工具栏 -->
            <div class="toolbar">
              <el-button type="primary" @click="handleCreateTask">创建盘点任务</el-button>
              <el-button type="success" @click="handleBatchExecute" :disabled="!multipleSelection.length">
                批量执行
              </el-button>
              <el-button type="warning" @click="handleExportTasks">导出任务</el-button>
            </div>

            <!-- 任务表格 -->
            <div class="tasks-table">
              <el-table
                :data="tasksData"
                v-loading="tasksLoading"
                @selection-change="handleSelectionChange"
                border
              >
                <el-table-column type="selection" width="55" />
                <el-table-column prop="taskNumber" label="任务编号" width="140" />
                <el-table-column prop="taskName" label="任务名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="inventoryType" label="盘点类型" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getInventoryTypeTagType(scope.row.inventoryType)" size="small">
                      {{ getInventoryTypeText(scope.row.inventoryType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="assetCount" label="资产数量" width="100" align="right" />
                <el-table-column prop="completedCount" label="已盘点" width="100" align="right" />
                <el-table-column prop="differenceCount" label="差异数" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'difference-count': scope.row.differenceCount > 0 }">
                      {{ scope.row.differenceCount }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="progress" label="进度" width="120">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.progress"
                      :status="scope.row.progress === 100 ? 'success' : null"
                      :stroke-width="6"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="90">
                  <template slot-scope="scope">
                    <el-tag :type="getTaskStatusTagType(scope.row.status)" size="small">
                      {{ getTaskStatusText(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="planDate" label="计划日期" width="120" />
                <el-table-column prop="operator" label="负责人" width="100" />
                <el-table-column label="操作" width="200" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="handleViewTask(scope.row)">
                      查看
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'PENDING'"
                      type="text"
                      size="small"
                      @click="handleExecuteTask(scope.row)"
                    >
                      执行
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'IN_PROGRESS'"
                      type="text"
                      size="small"
                      @click="handleViewResult(scope.row)"
                    >
                      盘点结果
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'COMPLETED'"
                      type="text"
                      size="small"
                      @click="handleViewReport(scope.row)"
                    >
                      盘点报告
                    </el-button>
                    <el-button
                      v-if="scope.row.status === 'PENDING'"
                      type="text"
                      size="small"
                      class="danger-text"
                      @click="handleCancelTask(scope.row)"
                    >
                      取消
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="pagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="pagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="pagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="盘点结果" name="results">
          <div class="results-panel">
            <!-- 结果搜索条件 -->
            <div class="search-bar">
              <el-form :model="resultSearchForm" :inline="true" size="small">
                <el-form-item label="资产编码">
                  <el-input v-model="resultSearchForm.assetCode" placeholder="请输入资产编码" style="width: 150px" />
                </el-form-item>
                <el-form-item label="盘点结果">
                  <el-select v-model="resultSearchForm.result" placeholder="请选择结果" style="width: 120px">
                    <el-option label="盘盈" value="SURPLUS" />
                    <el-option label="盘亏" value="SHORTAGE" />
                    <el-option label="正常" value="NORMAL" />
                  </el-select>
                </el-form-item>
                <el-form-item label="处理状态">
                  <el-select v-model="resultSearchForm.handleStatus" placeholder="请选择状态" style="width: 120px">
                    <el-option label="待处理" value="PENDING" />
                    <el-option label="已处理" value="HANDLED" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleSearchResults">查询</el-button>
                  <el-button @click="handleResetResultSearch">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 结果表格 -->
            <div class="results-table">
              <el-table :data="resultsData" v-loading="resultsLoading" border>
                <el-table-column prop="assetCode" label="资产编码" width="120" />
                <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
                <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="right" />
                <el-table-column prop="differenceQuantity" label="差异数量" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="getDifferenceClass(scope.row.differenceQuantity)">
                      {{ scope.row.differenceQuantity > 0 ? '+' : '' }}{{ scope.row.differenceQuantity }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="result" label="盘点结果" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getResultTagType(scope.row.result)" size="small">
                      {{ getResultText(scope.row.result) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="location" label="存放地点" width="120" show-overflow-tooltip />
                <el-table-column prop="inventoryDate" label="盘点日期" width="120" />
                <el-table-column prop="inventoryPerson" label="盘点人" width="100" />
                <el-table-column prop="handleStatus" label="处理状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getHandleStatusTagType(scope.row.handleStatus)" size="small">
                      {{ getHandleStatusText(scope.row.handleStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="handleViewResultDetail(scope.row)">
                      查看
                    </el-button>
                    <el-button
                      v-if="scope.row.handleStatus === 'PENDING' && scope.row.result !== 'NORMAL'"
                      type="text"
                      size="small"
                      @click="handleProcessDifference(scope.row)"
                    >
                      处理差异
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleResultSizeChange"
                  @current-change="handleResultCurrentChange"
                  :current-page="resultPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="resultPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="resultPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="盘点统计" name="statistics">
          <div class="statistics-panel">
            <div class="chart-container">
              <div class="chart-item">
                <h4>盘点完成情况</h4>
                <div id="completionChart" style="width: 100%; height: 300px;"></div>
              </div>
              <div class="chart-item">
                <h4>盘点差异分析</h4>
                <div id="differenceChart" style="width: 100%; height: 300px;"></div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 创建盘点任务对话框 -->
    <create-inventory-task-dialog
      :visible.sync="createTaskDialogVisible"
      @submit="handleCreateTaskSubmit"
    />

    <!-- 盘点结果录入对话框 -->
    <inventory-result-dialog
      :visible.sync="resultDialogVisible"
      :task-data="currentTask"
      @submit="handleResultSubmit"
    />

    <!-- 任务详情对话框 -->
    <task-detail-dialog
      :visible.sync="taskDetailDialogVisible"
      :task-data="currentTask"
      @start-task="handleExecuteTask"
    />

    <!-- 差异处理对话框 -->
    <difference-process-dialog
      :visible.sync="differenceProcessDialogVisible"
      :difference-data="currentDifference"
      @submit="handleDifferenceSubmit"
    />

    <!-- 盘点报告对话框 -->
    <inventory-report-dialog
      :visible.sync="reportDialogVisible"
      :report-id="currentReportId"
    />
  </div>
</template>

<script>
import CreateInventoryTaskDialog from './components/CreateInventoryTaskDialog.vue'
import InventoryResultDialog from './components/InventoryResultDialog.vue'
import TaskDetailDialog from './components/TaskDetailDialog.vue'
import DifferenceProcessDialog from './components/DifferenceProcessDialog.vue'
import InventoryReportDialog from './components/InventoryReportDialog.vue'

export default {
  name: 'AssetInventory',
  components: {
    CreateInventoryTaskDialog,
    InventoryResultDialog,
    TaskDetailDialog,
    DifferenceProcessDialog,
    InventoryReportDialog
  },
  data() {
    return {
      activeTab: 'tasks',
      tasksLoading: false,
      resultsLoading: false,
      tasksData: [],
      resultsData: [],
      multipleSelection: [],
      createTaskDialogVisible: false,
      resultDialogVisible: false,
      taskDetailDialogVisible: false,
      differenceProcessDialogVisible: false,
      reportDialogVisible: false,
      currentTask: {},
      currentDifference: {},
      currentReportId: '',
      searchForm: {
        taskNumber: '',
        inventoryType: '',
        status: ''
      },
      resultSearchForm: {
        assetCode: '',
        result: '',
        handleStatus: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      resultPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      stats: {
        totalTasks: 0,
        completedTasks: 0,
        differenceCount: 0,
        progressRate: 0
      }
    }
  },
  mounted() {
    this.loadStats()
    this.loadTasksData()
    this.loadResultsData()
  },
  methods: {
    async loadStats() {
      try {
        this.stats = {
          totalTasks: 25,
          completedTasks: 18,
          differenceCount: 12,
          progressRate: 72
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    async loadTasksData() {
      this.tasksLoading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.tasksData = []
        this.pagination.total = 0
      } catch (error) {
        this.$message.error('加载盘点任务失败：' + error.message)
      } finally {
        this.tasksLoading = false
      }
    },

    async loadResultsData() {
      this.resultsLoading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.resultsData = []
        this.resultPagination.total = 0
      } catch (error) {
        this.$message.error('加载盘点结果失败：' + error.message)
      } finally {
        this.resultsLoading = false
      }
    },

    handleTabClick(tab) {
      console.log('切换到标签页：', tab.name)
    },

    handleSearchTasks() {
      this.pagination.currentPage = 1
      this.loadTasksData()
    },

    handleResetSearch() {
      this.searchForm = {
        taskNumber: '',
        inventoryType: '',
        status: ''
      }
      this.handleSearchTasks()
    },

    handleSearchResults() {
      this.resultPagination.currentPage = 1
      this.loadResultsData()
    },

    handleResetResultSearch() {
      this.resultSearchForm = {
        assetCode: '',
        result: '',
        handleStatus: ''
      }
      this.handleSearchResults()
    },

    handleCreateTask() {
      this.createTaskDialogVisible = true
    },

    async handleCreateTaskSubmit(formData) {
      try {
        // TODO: 调用API创建任务
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('创建盘点任务成功')
        this.createTaskDialogVisible = false
        this.loadTasksData()
      } catch (error) {
        this.$message.error('创建任务失败：' + error.message)
      }
    },

    handleViewTask(row) {
      this.currentTask = row
      this.taskDetailDialogVisible = true
    },

    handleExecuteTask(row) {
      this.currentTask = { ...row }
      this.resultDialogVisible = true
    },

    async handleResultSubmit(formData) {
      try {
        // TODO: 调用API提交盘点结果
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('盘点结果提交成功')
        this.resultDialogVisible = false
        this.loadTasksData()
        this.loadResultsData()
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      }
    },

    handleViewResult(row) {
      this.currentDifference = row
      this.differenceProcessDialogVisible = true
    },

    handleViewReport(row) {
      this.currentReportId = row.id || 'RPT202412001'
      this.reportDialogVisible = true
    },

    handleCancelTask(row) {
      this.$confirm('确认取消该盘点任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // TODO: 调用API取消任务
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.$message.success('取消成功')
          this.loadTasksData()
        } catch (error) {
          this.$message.error('取消失败：' + error.message)
        }
      }).catch(() => {})
    },

    handleBatchExecute() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要执行的任务')
        return
      }
      this.$confirm(`确认对选中的${this.multipleSelection.length}条任务执行盘点操作？`, '确认', {
        type: 'warning'
      }).then(() => {
        this.$message.success('批量执行成功')
        this.loadTasksData()
      }).catch(() => {})
    },

    handleExportTasks() {
      try {
        const data = this.tasksData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '盘点任务导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    handleViewResultDetail(row) {
      this.currentDifference = row
      this.differenceProcessDialogVisible = true
    },

    handleProcessDifference(row) {
      this.currentDifference = row
      this.differenceProcessDialogVisible = true
    },

    async handleDifferenceSubmit(formData) {
      try {
        // TODO: 调用API处理差异
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('差异处理提交成功')
        this.differenceProcessDialogVisible = false
        this.loadResultsData()
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      }
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadTasksData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadTasksData()
    },

    handleResultSizeChange(size) {
      this.resultPagination.pageSize = size
      this.loadResultsData()
    },

    handleResultCurrentChange(page) {
      this.resultPagination.currentPage = page
      this.loadResultsData()
    },

    getInventoryTypeTagType(type) {
      const typeMap = {
        'FULL': 'primary',
        'SAMPLE': 'success',
        'CYCLE': 'warning'
      }
      return typeMap[type] || 'default'
    },

    getInventoryTypeText(type) {
      const textMap = {
        'FULL': '全盘',
        'SAMPLE': '抽盘',
        'CYCLE': '循环盘点'
      }
      return textMap[type] || type
    },

    getTaskStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'default'
    },

    getTaskStatusText(status) {
      const textMap = {
        'PENDING': '待执行',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    getResultTagType(result) {
      const typeMap = {
        'SURPLUS': 'success',
        'SHORTAGE': 'danger',
        'NORMAL': 'info'
      }
      return typeMap[result] || 'default'
    },

    getResultText(result) {
      const textMap = {
        'SURPLUS': '盘盈',
        'SHORTAGE': '盘亏',
        'NORMAL': '正常'
      }
      return textMap[result] || result
    },

    getHandleStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'HANDLED': 'success'
      }
      return typeMap[status] || 'default'
    },

    getHandleStatusText(status) {
      const textMap = {
        'PENDING': '待处理',
        'HANDLED': '已处理'
      }
      return textMap[status] || status
    },

    getDifferenceClass(quantity) {
      if (quantity > 0) return 'surplus'
      if (quantity < 0) return 'shortage'
      return 'normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-inventory-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.stats-cards {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.completed {
        background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
      }

      &.difference {
        background: linear-gradient(135deg, #e6a23c 0%, #f7ba2a 100%);
      }

      &.progress {
        background: linear-gradient(135deg, #409eff 0%, #36cfc9 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.operation-section {
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .search-bar {
    background: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .statistics-panel {
    .chart-container {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;

      .chart-item {
        h4 {
          margin: 0 0 15px 0;
          color: #303133;
          text-align: center;
        }
      }
    }
  }
}

.difference-count {
  color: #f56c6c;
  font-weight: 600;
}

.surplus {
  color: #67c23a;
  font-weight: 600;
}

.shortage {
  color: #f56c6c;
  font-weight: 600;
}

.normal {
  color: #909399;
}

.danger-text {
  color: #f56c6c;
}
</style>
