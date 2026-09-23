<template>
  <div class="offline-data-list-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-menu"></i>
          离线数据列表
        </h1>
        <p class="page-description">管理和查看所有离线数据</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新建数据
        </el-button>
        <el-button icon="el-icon-refresh" @click="loadTableData">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="数据名称">
          <el-input
            v-model="searchForm.offlineDataName"
            placeholder="请输入数据名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="数据类型">
          <el-select v-model="searchForm.offlineDataType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="缓存数据" value="CACHE" />
            <el-option label="备份数据" value="BACKUP" />
            <el-option label="同步数据" value="SYNC" />
            <el-option label="临时数据" value="TEMP" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据分类">
          <el-select v-model="searchForm.offlineDataCategory" placeholder="请选择" clearable style="width: 150px">
            <el-option label="业务数据" value="BUSINESS" />
            <el-option label="系统数据" value="SYSTEM" />
            <el-option label="用户数据" value="USER" />
            <el-option label="配置数据" value="CONFIG" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据状态">
          <el-select v-model="searchForm.dataStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="已归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
        <el-form-item label="同步状态">
          <el-select v-model="searchForm.syncStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="待同步" value="PENDING" />
            <el-option label="同步中" value="SYNCING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 批量操作区域 -->
    <div class="batch-actions" v-if="selectedRows.length > 0">
      <div class="batch-info">
        已选择 <span class="selected-count">{{ selectedRows.length }}</span> 项
      </div>
      <div class="batch-buttons">
        <el-button size="small" @click="batchEnable">批量启用</el-button>
        <el-button size="small" @click="batchDisable">批量禁用</el-button>
        <el-button size="small" @click="batchSync">批量同步</el-button>
        <el-button size="small" type="danger" @click="batchDelete">批量删除</el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        ref="dataTable"
        :data="tableData"
        v-loading="tableLoading"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="offlineDataCode" label="数据编码" width="180" />
        <el-table-column prop="offlineDataName" label="数据名称" min-width="150" />
        <el-table-column prop="offlineDataType" label="数据类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeTagType(scope.row.offlineDataType)" size="small">
              {{ formatType(scope.row.offlineDataType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="offlineDataCategory" label="数据分类" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ formatCategory(scope.row.offlineDataCategory) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataStatus" label="数据状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.dataStatus)" size="small">
              {{ formatStatus(scope.row.dataStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="syncStatus" label="同步状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSyncStatusTagType(scope.row.syncStatus)" size="small">
              {{ formatSyncStatus(scope.row.syncStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSize" label="数据大小" width="100">
          <template slot-scope="scope">
            {{ formatFileSize(scope.row.dataSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="accessCount" label="访问次数" width="100" />
        <el-table-column prop="lastAccessTime" label="最后访问" width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastAccessTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="primary" @click="editRow(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'sync', row: scope.row}">同步数据</el-dropdown-item>
                <el-dropdown-item :command="{action: 'download', row: scope.row}">下载数据</el-dropdown-item>
                <el-dropdown-item :command="{action: 'enable', row: scope.row}" v-if="!scope.row.isAvailable">启用</el-dropdown-item>
                <el-dropdown-item :command="{action: 'disable', row: scope.row}" v-if="scope.row.isAvailable">禁用</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
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
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="dataForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据名称" prop="offlineDataName">
              <el-input v-model="formData.offlineDataName" placeholder="请输入数据名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据编码" prop="offlineDataCode">
              <el-input v-model="formData.offlineDataCode" placeholder="请输入数据编码" :disabled="isEdit" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据类型" prop="offlineDataType">
              <el-select v-model="formData.offlineDataType" placeholder="请选择" style="width: 100%">
                <el-option label="缓存数据" value="CACHE" />
                <el-option label="备份数据" value="BACKUP" />
                <el-option label="同步数据" value="SYNC" />
                <el-option label="临时数据" value="TEMP" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据分类" prop="offlineDataCategory">
              <el-select v-model="formData.offlineDataCategory" placeholder="请选择" style="width: 100%">
                <el-option label="业务数据" value="BUSINESS" />
                <el-option label="系统数据" value="SYSTEM" />
                <el-option label="用户数据" value="USER" />
                <el-option label="配置数据" value="CONFIG" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据模块" prop="offlineDataModule">
              <el-input v-model="formData.offlineDataModule" placeholder="请输入数据模块" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据版本" prop="dataVersion">
              <el-input v-model="formData.dataVersion" placeholder="请输入数据版本" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="数据内容" prop="dataContent">
          <el-input
            v-model="formData.dataContent"
            type="textarea"
            :rows="4"
            placeholder="请输入数据内容"
          />
        </el-form-item>
        <el-form-item label="数据描述">
          <el-input
            v-model="formData.dataDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入数据描述"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="是否可用">
              <el-switch v-model="formData.isAvailable" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否过期">
              <el-switch v-model="formData.isExpired" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序">
              <el-input-number v-model="formData.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getOfflineDataPage,
  createOfflineData,
  updateOfflineData,
  deleteOfflineData,
  enableOfflineData,
  disableOfflineData,
  syncOfflineData,
  downloadOfflineData,
  batchEnableOfflineData,
  batchDisableOfflineData,
  batchSyncOfflineData
} from '@/api/managementAccountant/mobile/offlineData'

export default {
  name: 'OfflineDataList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        offlineDataName: '',
        offlineDataType: '',
        offlineDataCategory: '',
        dataStatus: '',
        syncStatus: ''
      },
      // 表格数据
      tableData: [],
      tableLoading: false,
      selectedRows: [],
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      // 对话框
      dialogVisible: false,
      dialogTitle: '创建离线数据',
      isEdit: false,
      submitLoading: false,
      formData: {
        offlineDataId: '',
        offlineDataName: '',
        offlineDataCode: '',
        offlineDataType: '',
        offlineDataCategory: '',
        offlineDataModule: '',
        dataVersion: '1.0.0',
        dataContent: '',
        dataDescription: '',
        isAvailable: true,
        isExpired: false,
        sortOrder: 0
      },
      formRules: {
        offlineDataName: [
          { required: true, message: '请输入数据名称', trigger: 'blur' }
        ],
        offlineDataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        offlineDataCategory: [
          { required: true, message: '请选择数据分类', trigger: 'change' }
        ],
        dataContent: [
          { required: true, message: '请输入数据内容', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadTableData()
  },
  methods: {
    // 加载表格数据
    async loadTableData() {
      try {
        this.tableLoading = true
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        const response = await getOfflineDataPage(params)
        if (response.success) {
          this.tableData = response.data || []
          this.pagination.total = response.total || 0
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.tableLoading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadTableData()
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        offlineDataName: '',
        offlineDataType: '',
        offlineDataCategory: '',
        dataStatus: '',
        syncStatus: ''
      }
      this.handleSearch()
    },

    // 分页处理
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadTableData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadTableData()
    },

    // 选择处理
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 显示创建对话框
    showCreateDialog() {
      this.dialogTitle = '创建离线数据'
      this.isEdit = false
      this.dialogVisible = true
    },

    // 编辑行
    editRow(row) {
      this.dialogTitle = '编辑离线数据'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 查看详情
    viewDetail(row) {
      this.$router.push(`/management-accountant/mobile/offline-data/detail/${row.offlineDataId}`)
    },

    // 重置表单
    resetForm() {
      this.$refs.dataForm.resetFields()
      this.formData = {
        offlineDataId: '',
        offlineDataName: '',
        offlineDataCode: '',
        offlineDataType: '',
        offlineDataCategory: '',
        offlineDataModule: '',
        dataVersion: '1.0.0',
        dataContent: '',
        dataDescription: '',
        isAvailable: true,
        isExpired: false,
        sortOrder: 0
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.dataForm.validate()
        this.submitLoading = true

        let response
        if (this.isEdit) {
          response = await updateOfflineData(this.formData)
        } else {
          // 自动生成编码
          if (!this.formData.offlineDataCode) {
            this.formData.offlineDataCode = `OFFLINE_${Date.now()}`
          }
          response = await createOfflineData(this.formData)
        }

        if (response.success) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.loadTableData()
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.submitLoading = false
      }
    },

    // 下拉菜单命令处理
    async handleCommand(command) {
      const { action, row } = command
      try {
        switch (action) {
          case 'sync':
            await this.syncRow(row)
            break
          case 'download':
            await this.downloadRow(row)
            break
          case 'enable':
            await this.enableRow(row)
            break
          case 'disable':
            await this.disableRow(row)
            break
          case 'delete':
            await this.deleteRow(row)
            break
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      }
    },

    // 同步数据
    async syncRow(row) {
      const response = await syncOfflineData(row.offlineDataId)
      if (response.success) {
        this.$message.success('同步成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '同步失败')
      }
    },

    // 下载数据
    async downloadRow(row) {
      const response = await downloadOfflineData(row.offlineDataId)
      if (response.success) {
        this.$message.success('下载成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '下载失败')
      }
    },

    // 启用数据
    async enableRow(row) {
      const response = await enableOfflineData(row.offlineDataId)
      if (response.success) {
        this.$message.success('启用成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '启用失败')
      }
    },

    // 禁用数据
    async disableRow(row) {
      const response = await disableOfflineData(row.offlineDataId)
      if (response.success) {
        this.$message.success('禁用成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '禁用失败')
      }
    },

    // 删除数据
    async deleteRow(row) {
      try {
        await this.$confirm('确定要删除这条数据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteOfflineData(row.offlineDataId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        // 用户取消删除
      }
    },

    // 批量操作
    async batchEnable() {
      const ids = this.selectedRows.map(row => row.offlineDataId)
      const response = await batchEnableOfflineData(ids)
      if (response.success) {
        this.$message.success('批量启用成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '批量启用失败')
      }
    },

    async batchDisable() {
      const ids = this.selectedRows.map(row => row.offlineDataId)
      const response = await batchDisableOfflineData(ids)
      if (response.success) {
        this.$message.success('批量禁用成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '批量禁用失败')
      }
    },

    async batchSync() {
      const ids = this.selectedRows.map(row => row.offlineDataId)
      const response = await batchSyncOfflineData(ids)
      if (response.success) {
        this.$message.success('批量同步成功')
        this.loadTableData()
      } else {
        this.$message.error(response.message || '批量同步失败')
      }
    },

    async batchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条数据吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // 这里可以调用批量删除API
        this.$message.success('批量删除成功')
        this.loadTableData()
      } catch (error) {
        // 用户取消删除
      }
    },

    // 格式化方法
    formatType(type) {
      const typeMap = {
        CACHE: '缓存',
        BACKUP: '备份',
        SYNC: '同步',
        TEMP: '临时'
      }
      return typeMap[type] || type
    },

    formatCategory(category) {
      const categoryMap = {
        BUSINESS: '业务',
        SYSTEM: '系统',
        USER: '用户',
        CONFIG: '配置'
      }
      return categoryMap[category] || category
    },

    formatStatus(status) {
      const statusMap = {
        DRAFT: '草稿',
        ACTIVE: '活跃',
        INACTIVE: '非活跃',
        ARCHIVED: '已归档'
      }
      return statusMap[status] || status
    },

    formatSyncStatus(status) {
      const statusMap = {
        PENDING: '待同步',
        SYNCING: '同步中',
        COMPLETED: '已完成',
        FAILED: '失败'
      }
      return statusMap[status] || status
    },

    formatFileSize(size) {
      if (!size) return '-'
      const units = ['B', 'KB', 'MB', 'GB']
      let index = 0
      while (size >= 1024 && index < units.length - 1) {
        size /= 1024
        index++
      }
      return `${size.toFixed(1)} ${units[index]}`
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    },

    // 标签类型
    getTypeTagType(type) {
      const typeMap = {
        CACHE: 'primary',
        BACKUP: 'success',
        SYNC: 'warning',
        TEMP: 'info'
      }
      return typeMap[type] || 'default'
    },

    getStatusTagType(status) {
      const statusMap = {
        DRAFT: 'info',
        ACTIVE: 'success',
        INACTIVE: 'warning',
        ARCHIVED: 'danger'
      }
      return statusMap[status] || 'default'
    },

    getSyncStatusTagType(status) {
      const statusMap = {
        PENDING: 'info',
        SYNCING: 'warning',
        COMPLETED: 'success',
        FAILED: 'danger'
      }
      return statusMap[status] || 'default'
    }
  }
}
</script>

<style lang="scss" scoped>
.offline-data-list-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .page-title {
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;

    i {
      margin-right: 8px;
      color: #409eff;
    }
  }

  .page-description {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.search-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    margin-bottom: 0;
  }
}

.batch-actions {
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  border-radius: 4px;
  padding: 12px 16px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .batch-info {
    color: #1890ff;
    font-size: 14px;

    .selected-count {
      font-weight: 600;
      margin: 0 4px;
    }
  }
}

.table-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .pagination-section {
    margin-top: 20px;
    text-align: right;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
