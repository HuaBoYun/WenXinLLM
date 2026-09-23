<template>
  <div class="service-portal-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="门户名称">
          <el-input
            v-model="searchForm.portalName"
            placeholder="请输入门户名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="门户编码">
          <el-input
            v-model="searchForm.portalCode"
            placeholder="请输入门户编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="门户类型">
          <el-select v-model="searchForm.portalType" placeholder="请选择门户类型" clearable style="width: 150px">
            <el-option label="员工门户" value="EMPLOYEE" />
            <el-option label="客户门户" value="CUSTOMER" />
            <el-option label="合作伙伴门户" value="PARTNER" />
            <el-option label="管理门户" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="门户状态">
          <el-select v-model="searchForm.portalStatus" placeholder="请选择门户状态" clearable style="width: 150px">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="维护中" value="MAINTENANCE" />
            <el-option label="已归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
        <el-form-item label="所有者">
          <el-input
            v-model="searchForm.portalOwnerName"
            placeholder="请输入所有者名称"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card" shadow="never">
      <div class="operation-bar">
        <div class="left-operations">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建门户</el-button>
          <el-button 
            type="success" 
            icon="el-icon-check" 
            :disabled="!hasSelection"
            @click="handleBatchActivate"
          >
            批量激活
          </el-button>
          <el-button 
            type="warning" 
            icon="el-icon-close" 
            :disabled="!hasSelection"
            @click="handleBatchDeactivate"
          >
            批量停用
          </el-button>
          <el-button 
            type="info" 
            icon="el-icon-box" 
            :disabled="!hasSelection"
            @click="handleBatchArchive"
          >
            批量归档
          </el-button>
          <el-button 
            type="danger" 
            icon="el-icon-delete" 
            :disabled="!hasSelection"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
        <div class="right-operations">
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
          <el-button icon="el-icon-upload2" @click="handleImport">导入</el-button>
          <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        </div>
      </div>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="never">
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
        <el-table-column prop="portalId" label="门户ID" width="80" sortable="custom" />
        <el-table-column prop="portalName" label="门户名称" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.portalName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="portalCode" label="门户编码" width="120" show-overflow-tooltip />
        <el-table-column prop="portalType" label="门户类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPortalTypeColor(scope.row.portalType)" size="small">
              {{ formatPortalType(scope.row.portalType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="portalStatus" label="门户状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPortalStatusColor(scope.row.portalStatus)" size="small">
              {{ formatPortalStatus(scope.row.portalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="portalOwnerName" label="所有者" width="120" show-overflow-tooltip />
        <el-table-column prop="accessCount" label="访问量" width="100" align="right" sortable="custom">
          <template slot-scope="scope">
            {{ formatAccessCount(scope.row.accessCount) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastAccessTime" label="最后访问" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastAccessTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="激活状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isActive"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" label="默认门户" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isDefault === 1" type="success" size="mini">是</el-tag>
            <el-tag v-else type="info" size="mini">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="configure">配置</el-dropdown-item>
                <el-dropdown-item command="healthCheck">健康检查</el-dropdown-item>
                <el-dropdown-item command="monitor">性能监控</el-dropdown-item>
                <el-dropdown-item command="backup">备份</el-dropdown-item>
                <el-dropdown-item command="maintenance" :divided="true">维护模式</el-dropdown-item>
                <el-dropdown-item command="setDefault">设为默认</el-dropdown-item>
                <el-dropdown-item command="archive" :divided="true">归档</el-dropdown-item>
                <el-dropdown-item command="delete">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
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

    <!-- 门户详情对话框 -->
    <ServicePortalDetail
      :visible.sync="detailVisible"
      :portal-id="currentPortalId"
      :mode="detailMode"
      @refresh="handleRefresh"
    />

    <!-- 批量操作确认对话框 -->
    <el-dialog
      :title="batchDialog.title"
      :visible.sync="batchDialog.visible"
      width="500px"
      @close="handleBatchDialogClose"
    >
      <el-form :model="batchDialog.form" label-width="80px">
        <el-form-item v-if="batchDialog.showReason" label="操作原因">
          <el-input
            v-model="batchDialog.form.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入操作原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getServicePortalPage,
  activateServicePortal,
  deactivateServicePortal,
  batchActivateServicePortal,
  batchDeactivateServicePortal,
  batchArchiveServicePortal,
  batchDeleteServicePortal,
  setDefaultPortal,
  performHealthCheck,
  monitorPortalPerformance,
  backupPortal,
  startMaintenance,
  archiveServicePortal,
  deleteServicePortal,
  utils
} from '@/api/managementAccountant/ss/servicePortal'
import ServicePortalDetail from './ServicePortalDetail'

export default {
  name: 'ServicePortalList',
  components: {
    ServicePortalDetail
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        portalName: '',
        portalCode: '',
        portalType: '',
        portalStatus: '',
        portalOwnerName: ''
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: '',
      detailVisible: false,
      currentPortalId: null,
      detailMode: 'view',
      batchDialog: {
        visible: false,
        title: '',
        action: '',
        showReason: false,
        form: {
          reason: ''
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
  mounted() {
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
        
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await getServicePortalPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
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
      this.searchForm = {
        portalName: '',
        portalCode: '',
        portalType: '',
        portalStatus: '',
        portalOwnerName: ''
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
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

    // 新建
    handleCreate() {
      this.currentPortalId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 查看
    handleView(row) {
      this.currentPortalId = row.portalId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentPortalId = row.portalId
      this.detailMode = 'edit'
      this.detailVisible = true
    },

    // 状态切换
    async handleStatusChange(row) {
      try {
        if (row.isActive === 1) {
          await activateServicePortal(row.portalId, this.tenantId)
          this.$message.success('激活成功')
        } else {
          await deactivateServicePortal(row.portalId, '手动停用', this.tenantId)
          this.$message.success('停用成功')
        }
        this.loadData()
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
        // 恢复原状态
        row.isActive = row.isActive === 1 ? 0 : 1
      }
    },

    // 更多操作
    handleCommand(command, row) {
      switch (command) {
        case 'configure':
          this.handleConfigure(row)
          break
        case 'healthCheck':
          this.handleHealthCheck(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'backup':
          this.handleBackup(row)
          break
        case 'maintenance':
          this.handleMaintenance(row)
          break
        case 'setDefault':
          this.handleSetDefault(row)
          break
        case 'archive':
          this.handleArchive(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 配置门户
    handleConfigure(row) {
      this.$router.push({
        name: 'ServicePortalConfig',
        params: { portalId: row.portalId }
      })
    },

    // 健康检查
    async handleHealthCheck(row) {
      try {
        const response = await performHealthCheck(row.portalId, this.tenantId)
        if (response.success) {
          this.$message.success('健康检查完成')
          // 可以显示检查结果
        }
      } catch (error) {
        this.$message.error('健康检查失败: ' + error.message)
      }
    },

    // 性能监控
    async handleMonitor(row) {
      try {
        const response = await monitorPortalPerformance(row.portalId, this.tenantId)
        if (response.success) {
          this.$message.success('性能监控完成')
          // 可以显示监控结果
        }
      } catch (error) {
        this.$message.error('性能监控失败: ' + error.message)
      }
    },

    // 备份
    async handleBackup(row) {
      try {
        await backupPortal(row.portalId, 'MANUAL', this.tenantId)
        this.$message.success('备份任务已启动')
      } catch (error) {
        this.$message.error('备份失败: ' + error.message)
      }
    },

    // 维护模式
    handleMaintenance(row) {
      this.$prompt('请输入维护原因', '启动维护模式', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          await startMaintenance(row.portalId, value, null, null, this.tenantId)
          this.$message.success('已启动维护模式')
          this.loadData()
        } catch (error) {
          this.$message.error('启动维护模式失败: ' + error.message)
        }
      })
    },

    // 设为默认
    async handleSetDefault(row) {
      try {
        await setDefaultPortal(row.portalId, this.tenantId)
        this.$message.success('设置默认门户成功')
        this.loadData()
      } catch (error) {
        this.$message.error('设置默认门户失败: ' + error.message)
      }
    },

    // 归档
    handleArchive(row) {
      this.$prompt('请输入归档原因', '归档门户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(async ({ value }) => {
        try {
          await archiveServicePortal(row.portalId, value, this.tenantId)
          this.$message.success('归档成功')
          this.loadData()
        } catch (error) {
          this.$message.error('归档失败: ' + error.message)
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除这个门户吗？', '确认删除', {
        type: 'warning'
      }).then(async () => {
        try {
          await deleteServicePortal(row.portalId, this.tenantId)
          this.$message.success('删除成功')
          this.loadData()
        } catch (error) {
          this.$message.error('删除失败: ' + error.message)
        }
      })
    },

    // 批量激活
    handleBatchActivate() {
      this.batchDialog = {
        visible: true,
        title: '批量激活门户',
        action: 'activate',
        showReason: false,
        form: { reason: '' }
      }
    },

    // 批量停用
    handleBatchDeactivate() {
      this.batchDialog = {
        visible: true,
        title: '批量停用门户',
        action: 'deactivate',
        showReason: true,
        form: { reason: '' }
      }
    },

    // 批量归档
    handleBatchArchive() {
      this.batchDialog = {
        visible: true,
        title: '批量归档门户',
        action: 'archive',
        showReason: true,
        form: { reason: '' }
      }
    },

    // 批量删除
    handleBatchDelete() {
      this.$confirm('确定要删除选中的门户吗？', '确认删除', {
        type: 'warning'
      }).then(() => {
        this.executeBatchDelete()
      })
    },

    // 批量操作确认
    async handleBatchConfirm() {
      const portalIds = this.selectedRows.map(row => row.portalId)
      const reason = this.batchDialog.form.reason

      try {
        switch (this.batchDialog.action) {
          case 'activate':
            await batchActivateServicePortal(portalIds, this.tenantId)
            this.$message.success('批量激活成功')
            break
          case 'deactivate':
            if (!reason) {
              this.$message.warning('请输入停用原因')
              return
            }
            await batchDeactivateServicePortal(portalIds, reason, this.tenantId)
            this.$message.success('批量停用成功')
            break
          case 'archive':
            if (!reason) {
              this.$message.warning('请输入归档原因')
              return
            }
            await batchArchiveServicePortal(portalIds, reason, this.tenantId)
            this.$message.success('批量归档成功')
            break
        }
        this.batchDialog.visible = false
        this.loadData()
      } catch (error) {
        this.$message.error('批量操作失败: ' + error.message)
      }
    },

    // 执行批量删除
    async executeBatchDelete() {
      try {
        const portalIds = this.selectedRows.map(row => row.portalId)
        await batchDeleteServicePortal(portalIds, this.tenantId)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        this.$message.error('批量删除失败: ' + error.message)
      }
    },

    // 批量对话框关闭
    handleBatchDialogClose() {
      this.batchDialog.form.reason = ''
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 导入
    handleImport() {
      this.$message.info('导入功能开发中...')
    },

    // 格式化门户类型
    formatPortalType(type) {
      return utils.formatPortalType(type)
    },

    // 格式化门户状态
    formatPortalStatus(status) {
      return utils.formatPortalStatus(status).text
    },

    // 获取门户类型颜色
    getPortalTypeColor(type) {
      const colorMap = {
        'EMPLOYEE': 'primary',
        'CUSTOMER': 'success',
        'PARTNER': 'warning',
        'ADMIN': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取门户状态颜色
    getPortalStatusColor(status) {
      return utils.formatPortalStatus(status).color
    },

    // 格式化访问量
    formatAccessCount(count) {
      return utils.formatAccessCount(count)
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style lang="scss" scoped>
.service-portal-list {
  .search-card, .operation-card, .table-card {
    margin-bottom: 16px;
  }

  .operation-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-container {
    margin-top: 16px;
    text-align: right;
  }
}
</style>
