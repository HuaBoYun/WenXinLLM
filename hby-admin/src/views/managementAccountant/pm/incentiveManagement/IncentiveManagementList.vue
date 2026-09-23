<template>
  <div class="incentive-management-list">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="激励标题" prop="incentiveTitle">
          <el-input
            v-model="searchForm.incentiveTitle"
            placeholder="请输入激励标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="激励类型" prop="incentiveType">
          <el-select v-model="searchForm.incentiveType" placeholder="请选择激励类型" clearable style="width: 150px">
            <el-option label="绩效激励" value="PERFORMANCE" />
            <el-option label="成就激励" value="ACHIEVEMENT" />
            <el-option label="创新激励" value="INNOVATION" />
            <el-option label="团队激励" value="TEAM" />
            <el-option label="专项激励" value="SPECIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="激励状态" prop="incentiveStatus">
          <el-select v-model="searchForm.incentiveStatus" placeholder="请选择激励状态" clearable style="width: 150px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="生效中" value="ACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="激励年度" prop="incentiveYear">
          <el-date-picker
            v-model="searchForm.incentiveYear"
            type="year"
            placeholder="请选择年度"
            style="width: 150px"
            value-format="yyyy"
          />
        </el-form-item>
        <el-form-item label="目标部门" prop="targetDeptId">
          <el-select v-model="searchForm.targetDeptId" placeholder="请选择部门" clearable style="width: 150px">
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围" prop="timeRange">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            style="width: 300px"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card shadow="never" class="toolbar-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建激励</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport" :loading="exportLoading">导出数据</el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleBatchReminder" :disabled="!hasSelection">批量提醒</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="!hasSelection">批量删除</el-button>
        </div>
        <div class="toolbar-right">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" circle @click="loadData" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" circle @click="showColumnSetting = true" />
          </el-tooltip>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="incentiveCode" label="激励编码" width="150" sortable="custom" />
        <el-table-column prop="incentiveTitle" label="激励标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="incentiveType" label="激励类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.incentiveType)" size="small">
              {{ formatIncentiveType(scope.row.incentiveType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="incentiveStatus" label="激励状态" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.incentiveStatus)" size="small">
              {{ formatIncentiveStatus(scope.row.incentiveStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetDeptName" label="目标部门" width="150" show-overflow-tooltip />
        <el-table-column prop="incentiveOwnerId" label="负责人" width="120" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.incentiveOwnerName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalBudget" label="预算总额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.totalBudget) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="usedAmount" label="已使用" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.usedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.completionRate || 0"
              :stroke-width="6"
              :show-text="false"
              :color="getProgressColor(scope.row.completionRate)"
            />
            <div class="progress-text">{{ scope.row.completionRate || 0 }}%</div>
          </template>
        </el-table-column>
        <el-table-column prop="plannedStartTime" label="计划开始" width="150" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.plannedStartTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="plannedEndTime" label="计划结束" width="150" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.plannedEndTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="150" align="center" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)" v-if="canEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'start', row: scope.row}" v-if="canStart(scope.row)">启动</el-dropdown-item>
                <el-dropdown-item :command="{action: 'suspend', row: scope.row}" v-if="canSuspend(scope.row)">暂停</el-dropdown-item>
                <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="canComplete(scope.row)">完成</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="canCancel(scope.row)">取消</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided v-if="canDelete(scope.row)">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="showColumnSetting" width="600px">
      <el-checkbox-group v-model="visibleColumns">
        <el-checkbox v-for="column in allColumns" :key="column.prop" :label="column.prop">
          {{ column.label }}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showColumnSetting = false">取消</el-button>
        <el-button type="primary" @click="handleColumnSetting">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import incentiveManagementApi, { utils } from '@/api/managementAccountant/pm/incentiveManagement'

export default {
  name: 'IncentiveManagementList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        incentiveTitle: '',
        incentiveType: '',
        incentiveStatus: '',
        incentiveYear: new Date().getFullYear().toString(),
        targetDeptId: null,
        timeRange: []
      },
      
      // 表格数据
      tableData: [],
      loading: false,
      exportLoading: false,
      
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      
      // 选择
      selectedRows: [],
      
      // 排序
      sortField: '',
      sortOrder: '',
      
      // 部门选项
      deptOptions: [],
      
      // 列设置
      showColumnSetting: false,
      allColumns: [
        { prop: 'incentiveCode', label: '激励编码' },
        { prop: 'incentiveTitle', label: '激励标题' },
        { prop: 'incentiveType', label: '激励类型' },
        { prop: 'incentiveStatus', label: '激励状态' },
        { prop: 'targetDeptName', label: '目标部门' },
        { prop: 'incentiveOwnerName', label: '负责人' },
        { prop: 'totalBudget', label: '预算总额' },
        { prop: 'usedAmount', label: '已使用' },
        { prop: 'completionRate', label: '完成率' },
        { prop: 'plannedStartTime', label: '计划开始' },
        { prop: 'plannedEndTime', label: '计划结束' },
        { prop: 'createdTime', label: '创建时间' }
      ],
      visibleColumns: []
    }
  },
  
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  
  created() {
    this.initVisibleColumns()
    this.loadDeptOptions()
    this.loadData()
  },
  
  methods: {
    // 初始化可见列
    initVisibleColumns() {
      this.visibleColumns = this.allColumns.map(col => col.prop)
    },
    
    // 加载部门选项
    async loadDeptOptions() {
      try {
        // 这里应该调用部门API获取部门列表
        // 暂时使用模拟数据
        this.deptOptions = [
          { id: 1, name: '技术部' },
          { id: 2, name: '销售部' },
          { id: 3, name: '市场部' },
          { id: 4, name: '人事部' }
        ]
      } catch (error) {
        console.error('加载部门选项失败:', error)
      }
    },
    
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        // 处理时间范围
        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }
        
        // 处理排序
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await incentiveManagementApi.getIncentiveManagementPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm.incentiveYear = new Date().getFullYear().toString()
      this.pagination.current = 1
      this.loadData()
    },
    
    // 新建
    handleCreate() {
      this.$router.push({ name: 'IncentiveManagementCreate' })
    },
    
    // 查看
    handleView(row) {
      this.$router.push({ 
        name: 'IncentiveManagementDetail', 
        params: { id: row.incentiveId },
        query: { mode: 'view' }
      })
    },
    
    // 编辑
    handleEdit(row) {
      this.$router.push({ 
        name: 'IncentiveManagementDetail', 
        params: { id: row.incentiveId },
        query: { mode: 'edit' }
      })
    },
    
    // 格式化方法
    formatIncentiveStatus: utils.formatIncentiveStatus,
    formatIncentiveType: utils.formatIncentiveType,
    
    // 获取状态颜色
    getStatusColor: utils.getStatusColor,
    
    // 获取类型颜色
    getTypeColor(type) {
      const colorMap = {
        'PERFORMANCE': 'primary',
        'ACHIEVEMENT': 'success',
        'INNOVATION': 'warning',
        'TEAM': 'info',
        'SPECIAL': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取进度颜色
    getProgressColor(percentage) {
      if (percentage >= 80) return '#67c23a'
      if (percentage >= 60) return '#e6a23c'
      if (percentage >= 40) return '#f56c6c'
      return '#909399'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm')
    },
    
    // 权限判断方法
    canEdit(row) {
      return ['DRAFT', 'REJECTED'].includes(row.incentiveStatus)
    },
    
    canStart(row) {
      return row.incentiveStatus === 'APPROVED'
    },
    
    canSuspend(row) {
      return row.incentiveStatus === 'ACTIVE'
    },
    
    canComplete(row) {
      return row.incentiveStatus === 'ACTIVE'
    },
    
    canCancel(row) {
      return ['DRAFT', 'APPROVED', 'ACTIVE', 'SUSPENDED'].includes(row.incentiveStatus)
    },
    
    canDelete(row) {
      return ['DRAFT', 'CANCELLED'].includes(row.incentiveStatus)
    },

    // 处理命令
    async handleCommand({ action, row }) {
      switch (action) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'start':
          await this.handleStart(row)
          break
        case 'suspend':
          await this.handleSuspend(row)
          break
        case 'complete':
          await this.handleComplete(row)
          break
        case 'cancel':
          await this.handleCancel(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 复制激励方案
    async handleCopy(row) {
      try {
        const { value: newTitle } = await this.$prompt('请输入新激励方案标题', '复制激励方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: `${row.incentiveTitle}_副本`,
          inputValidator: (value) => {
            if (!value) {
              return '标题不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.copyIncentiveScheme(row.incentiveId, newTitle)
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制激励方案失败:', error)
          this.$message.error('复制失败')
        }
      }
    },

    // 启动激励方案
    async handleStart(row) {
      try {
        await this.$confirm('确认启动该激励方案吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await incentiveManagementApi.startIncentiveScheme(row.incentiveId)
        if (response.success) {
          this.$message.success('启动成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '启动失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('启动激励方案失败:', error)
          this.$message.error('启动失败')
        }
      }
    },

    // 暂停激励方案
    async handleSuspend(row) {
      try {
        const { value: reason } = await this.$prompt('请输入暂停原因', '暂停激励方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '暂停原因不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.suspendIncentiveScheme(row.incentiveId, reason)
        if (response.success) {
          this.$message.success('暂停成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '暂停失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('暂停激励方案失败:', error)
          this.$message.error('暂停失败')
        }
      }
    },

    // 完成激励方案
    async handleComplete(row) {
      try {
        await this.$confirm('确认完成该激励方案吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await incentiveManagementApi.completeIncentiveScheme(row.incentiveId)
        if (response.success) {
          this.$message.success('完成成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('完成激励方案失败:', error)
          this.$message.error('完成失败')
        }
      }
    },

    // 取消激励方案
    async handleCancel(row) {
      try {
        const { value: reason } = await this.$prompt('请输入取消原因', '取消激励方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '取消原因不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.cancelIncentiveScheme(row.incentiveId, reason)
        if (response.success) {
          this.$message.success('取消成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消激励方案失败:', error)
          this.$message.error('取消失败')
        }
      }
    },

    // 删除激励方案
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该激励方案吗？删除后无法恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await incentiveManagementApi.deleteIncentiveScheme(row.incentiveId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除激励方案失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 导出数据
    async handleExport() {
      this.exportLoading = true
      try {
        const params = {
          incentiveYear: this.searchForm.incentiveYear,
          incentiveType: this.searchForm.incentiveType,
          incentiveStatus: this.searchForm.incentiveStatus,
          targetDeptId: this.searchForm.targetDeptId
        }

        const response = await incentiveManagementApi.exportIncentiveData(params)
        if (response.success) {
          // 这里应该处理文件下载
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出数据失败:', error)
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
    },

    // 批量提醒
    async handleBatchReminder() {
      try {
        await this.$confirm(`确认向选中的 ${this.selectedRows.length} 个激励方案发送提醒通知吗？`, '批量提醒', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const incentiveIds = this.selectedRows.map(row => row.incentiveId)
        const response = await incentiveManagementApi.batchSendReminderNotifications(incentiveIds, 'GENERAL')

        if (response.success) {
          this.$message.success('批量提醒成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量提醒失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量提醒失败:', error)
          this.$message.error('批量提醒失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个激励方案吗？删除后无法恢复！`, '批量删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const incentiveIds = this.selectedRows.map(row => row.incentiveId)
        const response = await incentiveManagementApi.batchDeleteIncentiveSchemes(incentiveIds)

        if (response.success) {
          this.$message.success('批量删除成功')
          this.selectedRows = []
          this.loadData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 列设置
    handleColumnSetting() {
      // 这里可以保存用户的列设置偏好
      this.showColumnSetting = false
      this.$message.success('列设置已保存')
    }
  }
}
</script>

<style lang="scss" scoped>
.incentive-management-list {
  .search-card, .toolbar-card, .table-card {
    margin-bottom: 20px;
  }
  
  .search-form {
    .el-form-item {
      margin-bottom: 10px;
    }
  }
  
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .toolbar-left {
      .el-button {
        margin-right: 10px;
      }
    }
    
    .toolbar-right {
      .el-button {
        margin-left: 5px;
      }
    }
  }
  
  .amount {
    font-weight: 500;
    color: #e6a23c;
  }
  
  .progress-text {
    font-size: 12px;
    color: #606266;
    margin-top: 2px;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
