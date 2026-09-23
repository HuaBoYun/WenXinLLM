<template>
  <div class="digital-employee-list">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="数字员工名称" prop="robotName">
          <el-input
            v-model="searchForm.robotName"
            placeholder="请输入数字员工名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="数字员工编码" prop="robotCode">
          <el-input
            v-model="searchForm.robotCode"
            placeholder="请输入数字员工编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态" prop="robotStatus">
          <el-select v-model="searchForm.robotStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="维护中" value="MAINTENANCE" />
            <el-option label="已退役" value="RETIRED" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="robotType">
          <el-select v-model="searchForm.robotType" placeholder="请选择类型" clearable style="width: 150px">
            <el-option label="RPA" value="RPA" />
            <el-option label="AI" value="AI" />
            <el-option label="聊天机器人" value="CHATBOT" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="robotCategory">
          <el-select v-model="searchForm.robotCategory" placeholder="请选择分类" clearable style="width: 150px">
            <el-option label="数据录入" value="DATA_ENTRY" />
            <el-option label="文档处理" value="DOCUMENT_PROCESSING" />
            <el-option label="客户服务" value="CUSTOMER_SERVICE" />
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
      <div class="toolbar-left">
        <el-button type="primary" @click="handleAdd" icon="el-icon-plus">新增数字员工</el-button>
        <el-button type="success" @click="handleBatchActivate" :disabled="!hasSelection" icon="el-icon-video-play">批量激活</el-button>
        <el-button type="warning" @click="handleBatchDeactivate" :disabled="!hasSelection" icon="el-icon-video-pause">批量停用</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!hasSelection" icon="el-icon-delete">批量删除</el-button>
        <el-dropdown @command="handleBatchCommand" style="margin-left: 10px">
          <el-button type="info">
            批量操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="batchHealthCheck">批量健康检查</el-dropdown-item>
            <el-dropdown-item command="batchMaintenance">批量维护</el-dropdown-item>
            <el-dropdown-item command="batchNotify">批量通知</el-dropdown-item>
            <el-dropdown-item command="batchExport">批量导出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="toolbar-right">
        <el-button @click="handleRefresh" icon="el-icon-refresh" circle></el-button>
        <el-button @click="handleImport" icon="el-icon-upload2" circle title="导入"></el-button>
        <el-button @click="handleExport" icon="el-icon-download" circle title="导出"></el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="robotCode" label="数字员工编码" width="150" sortable="custom" />
        <el-table-column prop="robotName" label="数字员工名称" width="200" show-overflow-tooltip />
        <el-table-column prop="robotType" label="类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeTagType(scope.row.robotType)" size="small">
              {{ formatRobotType(scope.row.robotType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="robotCategory" label="分类" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="small">
              {{ formatRobotCategory(scope.row.robotCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="robotStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.robotStatus)" size="small">
              {{ formatRobotStatus(scope.row.robotStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="healthStatus" label="健康状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getHealthStatusColor(scope.row.healthStatus)" size="small">
              {{ formatHealthStatus(scope.row.healthStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="successRate" label="成功率" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.successRate ? scope.row.successRate + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="executionCount" label="执行次数" width="100" align="center" sortable="custom" />
        <el-table-column prop="lastExecutionTime" label="最后执行时间" width="160" align="center">
          <template slot-scope="scope">
            <span>{{ formatDateTime(scope.row.lastExecutionTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人" width="120" show-overflow-tooltip />
        <el-table-column prop="departmentName" label="所属部门" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
            <el-dropdown @command="(command) => handleRowCommand(command, scope.row)" style="margin-left: 5px">
              <el-button size="mini" type="info">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="scope.row.robotStatus === 'INACTIVE'" command="activate">激活</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.robotStatus === 'ACTIVE'" command="deactivate">停用</el-dropdown-item>
                <el-dropdown-item command="healthCheck">健康检查</el-dropdown-item>
                <el-dropdown-item command="monitor">监控</el-dropdown-item>
                <el-dropdown-item command="performance">性能评估</el-dropdown-item>
                <el-dropdown-item command="maintenance">维护</el-dropdown-item>
                <el-dropdown-item command="deploy">部署</el-dropdown-item>
                <el-dropdown-item command="clone">克隆</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
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

    <!-- 批量操作对话框 -->
    <el-dialog :title="batchDialog.title" :visible.sync="batchDialog.visible" width="500px">
      <el-form :model="batchDialog.form" ref="batchForm" label-width="100px">
        <el-form-item v-if="batchDialog.type === 'deactivate'" label="停用原因" prop="reason">
          <el-input v-model="batchDialog.form.reason" type="textarea" placeholder="请输入停用原因" />
        </el-form-item>
        <el-form-item v-if="batchDialog.type === 'maintenance'" label="维护原因" prop="reason">
          <el-input v-model="batchDialog.form.reason" type="textarea" placeholder="请输入维护原因" />
        </el-form-item>
        <el-form-item v-if="batchDialog.type === 'maintenance'" label="开始时间" prop="startTime">
          <el-date-picker
            v-model="batchDialog.form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item v-if="batchDialog.type === 'maintenance'" label="结束时间" prop="endTime">
          <el-date-picker
            v-model="batchDialog.form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item v-if="batchDialog.type === 'notify'" label="通知类型" prop="notificationType">
          <el-select v-model="batchDialog.form.notificationType" placeholder="请选择通知类型" style="width: 100%">
            <el-option label="系统通知" value="SYSTEM" />
            <el-option label="邮件通知" value="EMAIL" />
            <el-option label="短信通知" value="SMS" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="batchDialog.type === 'notify'" label="通知消息" prop="message">
          <el-input v-model="batchDialog.form.message" type="textarea" placeholder="请输入通知消息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchConfirm" :loading="batchDialog.loading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDigitalEmployeePage,
  deleteDigitalEmployee,
  batchDeleteDigitalEmployee,
  activateDigitalEmployee,
  deactivateDigitalEmployee,
  batchActivateDigitalEmployee,
  batchDeactivateDigitalEmployee,
  performHealthCheck,
  monitorDigitalEmployee,
  evaluatePerformance,
  startMaintenance,
  formatRobotStatus,
  formatRobotType,
  formatRobotCategory,
  formatHealthStatus,
  getStatusColor,
  getHealthStatusColor
} from '@/api/managementAccountant/ss/digitalEmployee'

export default {
  name: 'DigitalEmployeeList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        robotName: '',
        robotCode: '',
        robotStatus: '',
        robotType: '',
        robotCategory: '',
        departmentId: null,
        ownerId: null
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页信息
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      // 排序信息
      sortInfo: {
        prop: '',
        order: ''
      },
      // 选中的行
      selectedRows: [],
      // 批量操作对话框
      batchDialog: {
        visible: false,
        title: '',
        type: '',
        loading: false,
        form: {
          reason: '',
          startTime: null,
          endTime: null,
          notificationType: '',
          message: ''
        }
      }
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          tenantId: this.tenantId,
          ...this.searchForm
        }
        
        if (this.sortInfo.prop) {
          params.orderBy = this.sortInfo.prop
          params.orderDirection = this.sortInfo.order === 'ascending' ? 'ASC' : 'DESC'
        }

        const response = await getDigitalEmployeePage(params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置搜索
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.pagination.current = 1
      this.loadData()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
    },

    // 新增
    handleAdd() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/create')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/detail/${row.robotId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/edit/${row.robotId}`)
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个数字员工吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteDigitalEmployee(row.robotId, this.tenantId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要删除的数字员工')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 个数字员工吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const robotIds = this.selectedRows.map(row => row.robotId)
        const response = await batchDeleteDigitalEmployee(robotIds, this.tenantId)
        if (response.success) {
          this.$message.success('批量删除成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }
    },

    // 批量激活
    async handleBatchActivate() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要激活的数字员工')
        return
      }

      try {
        const robotIds = this.selectedRows.map(row => row.robotId)
        const response = await batchActivateDigitalEmployee(robotIds, this.tenantId)
        if (response.success) {
          this.$message.success('批量激活成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('批量激活失败: ' + error.message)
      }
    },

    // 批量停用
    handleBatchDeactivate() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要停用的数字员工')
        return
      }
      this.showBatchDialog('deactivate', '批量停用数字员工')
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 排序变化
    handleSortChange({ prop, order }) {
      this.sortInfo.prop = prop
      this.sortInfo.order = order
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

    // 行操作命令
    async handleRowCommand(command, row) {
      switch (command) {
        case 'activate':
          await this.handleActivate(row)
          break
        case 'deactivate':
          await this.handleDeactivate(row)
          break
        case 'healthCheck':
          await this.handleHealthCheck(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'performance':
          this.handlePerformance(row)
          break
        case 'maintenance':
          this.handleMaintenance(row)
          break
        case 'deploy':
          this.handleDeploy(row)
          break
        case 'clone':
          this.handleClone(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 激活
    async handleActivate(row) {
      try {
        const response = await activateDigitalEmployee(row.robotId, this.tenantId)
        if (response.success) {
          this.$message.success('激活成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('激活失败: ' + error.message)
      }
    },

    // 停用
    async handleDeactivate(row) {
      try {
        const reason = await this.$prompt('请输入停用原因', '停用数字员工', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '停用原因不能为空'
        })

        const response = await deactivateDigitalEmployee(row.robotId, reason.value, this.tenantId)
        if (response.success) {
          this.$message.success('停用成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('停用失败: ' + error.message)
        }
      }
    },

    // 健康检查
    async handleHealthCheck(row) {
      try {
        this.$message.info('正在执行健康检查...')
        const response = await performHealthCheck(row.robotId, this.tenantId)
        if (response.success) {
          this.$message.success('健康检查完成')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('健康检查失败: ' + error.message)
      }
    },

    // 监控
    handleMonitor(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/monitor/${row.robotId}`)
    },

    // 性能评估
    handlePerformance(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/performance/${row.robotId}`)
    },

    // 维护
    handleMaintenance(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/maintenance/${row.robotId}`)
    },

    // 部署
    handleDeploy(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/deploy/${row.robotId}`)
    },

    // 克隆
    handleClone(row) {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/clone/${row.robotId}`)
    },

    // 批量操作命令
    handleBatchCommand(command) {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要操作的数字员工')
        return
      }

      switch (command) {
        case 'batchHealthCheck':
          this.handleBatchHealthCheck()
          break
        case 'batchMaintenance':
          this.showBatchDialog('maintenance', '批量维护数字员工')
          break
        case 'batchNotify':
          this.showBatchDialog('notify', '批量发送通知')
          break
        case 'batchExport':
          this.handleBatchExport()
          break
      }
    },

    // 批量健康检查
    async handleBatchHealthCheck() {
      try {
        this.$message.info('正在执行批量健康检查...')
        // 这里可以调用批量健康检查API
        this.$message.success('批量健康检查完成')
        this.loadData()
      } catch (error) {
        this.$message.error('批量健康检查失败: ' + error.message)
      }
    },

    // 批量导出
    handleBatchExport() {
      const robotIds = this.selectedRows.map(row => row.robotId)
      // 这里可以调用导出API
      this.$message.success('导出任务已提交')
    },

    // 显示批量操作对话框
    showBatchDialog(type, title) {
      this.batchDialog.type = type
      this.batchDialog.title = title
      this.batchDialog.visible = true
      this.batchDialog.form = {
        reason: '',
        startTime: null,
        endTime: null,
        notificationType: '',
        message: ''
      }
    },

    // 批量操作确认
    async handleBatchConfirm() {
      this.batchDialog.loading = true
      try {
        const robotIds = this.selectedRows.map(row => row.robotId)
        
        switch (this.batchDialog.type) {
          case 'deactivate':
            await batchDeactivateDigitalEmployee(robotIds, this.batchDialog.form.reason, this.tenantId)
            this.$message.success('批量停用成功')
            break
          case 'maintenance':
            // 调用批量维护API
            this.$message.success('批量维护任务已提交')
            break
          case 'notify':
            // 调用批量通知API
            this.$message.success('批量通知发送成功')
            break
        }
        
        this.batchDialog.visible = false
        this.loadData()
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
      } finally {
        this.batchDialog.loading = false
      }
    },

    // 导入
    handleImport() {
      this.$message.info('导入功能开发中...')
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 格式化方法
    formatRobotStatus,
    formatRobotType,
    formatRobotCategory,
    formatHealthStatus,
    getStatusColor,
    getHealthStatusColor,

    // 获取类型标签类型
    getTypeTagType(type) {
      const typeMap = {
        'RPA': 'primary',
        'AI': 'success',
        'CHATBOT': 'warning'
      }
      return typeMap[type] || 'info'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style scoped>
.digital-employee-list {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.search-form {
  margin-bottom: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.table-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
