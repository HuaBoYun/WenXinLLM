<template>
  <div class="month-end-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>智能月结管理</h2>
      <p>自动化月结任务管理，提高财务工作效率</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        创建月结任务
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="info" icon="el-icon-setting" @click="handleConfig">
        月结配置
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="任务名称">
          <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable />
        </el-form-item>
        <el-form-item label="会计期间">
          <el-date-picker
            v-model="queryForm.accountingPeriod"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable
          />
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select v-model="queryForm.taskStatus" placeholder="请选择任务状态" clearable>
            <el-option label="待执行" :value="0" />
            <el-option label="执行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已暂停" :value="3" />
            <el-option label="已取消" :value="4" />
            <el-option label="执行失败" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="taskId" label="任务ID" width="120" />
        <el-table-column prop="taskName" label="任务名称" min-width="150" />
        <el-table-column prop="accountingPeriod" label="会计期间" width="120" />
        <el-table-column prop="taskStatusName" label="任务状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.taskStatus)">
              {{ scope.row.taskStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="执行进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :status="getProgressStatus(scope.row.taskStatus)" />
          </template>
        </el-table-column>
        <el-table-column prop="totalSteps" label="总步骤数" width="100" />
        <el-table-column prop="completedSteps" label="已完成步骤" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleExecute(scope.row)"
              v-if="scope.row.taskStatus === 0"
            >
              执行
            </el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handlePause(scope.row)"
              v-if="scope.row.taskStatus === 1"
            >
              暂停
            </el-button>
            <el-button 
              size="mini" 
              type="info" 
              @click="handleResume(scope.row)"
              v-if="scope.row.taskStatus === 3"
            >
              恢复
            </el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleCancel(scope.row)"
              v-if="[0, 1, 3].includes(scope.row.taskStatus)"
            >
              取消
            </el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleDelete(scope.row)"
              v-if="[2, 4, 5].includes(scope.row.taskStatus)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
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

    <!-- 创建任务对话框 -->
    <el-dialog
      title="创建月结任务"
      :visible.sync="createDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="createForm.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="会计期间" prop="accountingPeriod">
          <el-date-picker
            v-model="createForm.accountingPeriod"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="账簿" prop="bookId">
          <el-select v-model="createForm.bookId" placeholder="请选择账簿" style="width: 100%">
            <el-option label="主账簿" :value="1001" />
            <el-option label="分账簿A" :value="1002" />
            <el-option label="分账簿B" :value="1003" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input
            v-model="createForm.taskDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入任务描述"
          />
        </el-form-item>
        <el-form-item label="执行步骤">
          <el-checkbox-group v-model="createForm.taskSteps">
            <el-checkbox value="期初余额检查">期初余额检查</el-checkbox>
            <el-checkbox value="凭证审核">凭证审核</el-checkbox>
            <el-checkbox value="期末调整">期末调整</el-checkbox>
            <el-checkbox value="结转损益">结转损益</el-checkbox>
            <el-checkbox value="生成报表">生成报表</el-checkbox>
            <el-checkbox value="数据备份">数据备份</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateConfirm" :loading="createLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 任务详情对话框 -->
    <el-dialog
      title="任务详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentTask">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ currentTask.taskId }}</el-descriptions-item>
          <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
          <el-descriptions-item label="会计期间">{{ currentTask.accountingPeriod }}</el-descriptions-item>
          <el-descriptions-item label="任务状态">
            <el-tag :type="getStatusType(currentTask.taskStatus)">
              {{ currentTask.taskStatusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行进度">
            <el-progress :percentage="currentTask.progress" :status="getProgressStatus(currentTask.taskStatus)" />
          </el-descriptions-item>
          <el-descriptions-item label="步骤进度">
            {{ currentTask.completedSteps }} / {{ currentTask.totalSteps }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentTask.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDate(currentTask.updateTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 执行步骤详情 -->
        <div style="margin-top: 20px;">
          <h4>执行步骤</h4>
          <el-steps :active="currentTask.completedSteps" finish-status="success">
            <el-step title="期初余额检查" description="检查期初余额数据完整性"></el-step>
            <el-step title="凭证审核" description="审核本期所有凭证"></el-step>
            <el-step title="期末调整" description="执行期末调整分录"></el-step>
            <el-step title="结转损益" description="结转本期损益"></el-step>
            <el-step title="生成报表" description="生成财务报表"></el-step>
            <el-step title="数据备份" description="备份月结数据"></el-step>
          </el-steps>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getMonthEndTaskPage, 
  createMonthEndTask, 
  executeMonthEndTask,
  pauseMonthEndTask,
  resumeMonthEndTask,
  cancelMonthEndTask,
  deleteMonthEndTask,
  getMonthEndTaskById
} from '@/api/financialSharing/monthEnd'

export default {
  name: 'MonthEndManagement',
  data() {
    return {
      loading: false,
      createLoading: false,
      tableData: [],
      selectedRows: [],
      
      // 查询表单
      queryForm: {
        taskName: '',
        accountingPeriod: '',
        taskStatus: null
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 创建任务对话框
      createDialogVisible: false,
      createForm: {
        taskName: '',
        accountingPeriod: '',
        bookId: null,
        taskDesc: '',
        taskSteps: []
      },
      createRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        accountingPeriod: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        bookId: [
          { required: true, message: '请选择账簿', trigger: 'change' }
        ]
      },
      
      // 任务详情对话框
      detailDialogVisible: false,
      currentTask: null
    }
  },
  
  mounted() {
    this.loadData()
  },
  
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        
        const response = await getMonthEndTaskPage(params)
        if (response.code === 200) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 刷新
    handleRefresh() {
      this.loadData()
    },
    
    // 创建任务
    handleCreate() {
      this.createDialogVisible = true
      this.$nextTick(() => {
        this.$refs.createForm.resetFields()
      })
    },
    
    // 确认创建
    async handleCreateConfirm() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true
        
        const response = await createMonthEndTask(this.createForm)
        if (response.code === 200) {
          this.$message.success('创建成功')
          this.createDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '创建失败')
        }
      } catch (error) {
        console.error('创建失败:', error)
        this.$message.error('创建失败')
      } finally {
        this.createLoading = false
      }
    },
    
    // 查看详情
    async handleView(row) {
      try {
        const response = await getMonthEndTaskById(row.taskId)
        if (response.code === 200) {
          this.currentTask = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    
    // 执行任务
    async handleExecute(row) {
      try {
        await this.$confirm('确认执行该月结任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await executeMonthEndTask({ taskId: row.taskId })
        if (response.code === 200) {
          this.$message.success('任务已开始执行')
          this.loadData()
        } else {
          this.$message.error(response.msg || '执行失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('执行失败:', error)
          this.$message.error('执行失败')
        }
      }
    },
    
    // 暂停任务
    async handlePause(row) {
      try {
        await this.$confirm('确认暂停该月结任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await pauseMonthEndTask(row.taskId)
        if (response.code === 200) {
          this.$message.success('任务已暂停')
          this.loadData()
        } else {
          this.$message.error(response.msg || '暂停失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('暂停失败:', error)
          this.$message.error('暂停失败')
        }
      }
    },
    
    // 恢复任务
    async handleResume(row) {
      try {
        await this.$confirm('确认恢复该月结任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await resumeMonthEndTask(row.taskId)
        if (response.code === 200) {
          this.$message.success('任务已恢复')
          this.loadData()
        } else {
          this.$message.error(response.msg || '恢复失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('恢复失败:', error)
          this.$message.error('恢复失败')
        }
      }
    },
    
    // 取消任务
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该月结任务？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await cancelMonthEndTask(row.taskId)
        if (response.code === 200) {
          this.$message.success('任务已取消')
          this.loadData()
        } else {
          this.$message.error(response.msg || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消失败:', error)
          this.$message.error('取消失败')
        }
      }
    },
    
    // 删除任务
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该月结任务？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteMonthEndTask(row.taskId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 配置
    handleConfig() {
      this.$message.info('请使用左侧菜单进入"月结配置"页面进行参数配置')
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        0: 'info',     // 待执行
        1: 'warning',  // 执行中
        2: 'success',  // 已完成
        3: 'warning',  // 已暂停
        4: 'info',     // 已取消
        5: 'danger'    // 执行失败
      }
      return statusMap[status] || 'info'
    },
    
    // 获取进度状态
    getProgressStatus(status) {
      if (status === 2) return 'success'
      if (status === 5) return 'exception'
      return null
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.month-end-container {
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

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
