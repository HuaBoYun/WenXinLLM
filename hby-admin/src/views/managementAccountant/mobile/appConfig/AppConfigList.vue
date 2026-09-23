<template>
  <div class="app-config-list-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="应用名称">
          <el-input
            v-model="searchForm.appName"
            placeholder="请输入应用名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="应用类型">
          <el-select v-model="searchForm.appType" placeholder="请选择应用类型" clearable style="width: 150px">
            <el-option label="原生应用" value="NATIVE" />
            <el-option label="混合应用" value="HYBRID" />
            <el-option label="Web应用" value="WEB" />
            <el-option label="渐进式Web应用" value="PWA" />
          </el-select>
        </el-form-item>
        <el-form-item label="应用平台">
          <el-select v-model="searchForm.appPlatform" placeholder="请选择应用平台" clearable style="width: 150px">
            <el-option label="安卓" value="ANDROID" />
            <el-option label="苹果" value="IOS" />
            <el-option label="Windows" value="WINDOWS" />
            <el-option label="全平台" value="ALL" />
          </el-select>
        </el-form-item>
        <el-form-item label="应用分类">
          <el-select v-model="searchForm.appCategory" placeholder="请选择应用分类" clearable style="width: 150px">
            <el-option label="业务应用" value="BUSINESS" />
            <el-option label="工具应用" value="TOOL" />
            <el-option label="游戏应用" value="GAME" />
            <el-option label="教育应用" value="EDUCATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已下架" value="UNPUBLISHED" />
            <el-option label="已归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作区域 -->
    <div class="action-section">
      <div class="action-left">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          创建应用
        </el-button>
        <el-button
          type="success"
          icon="el-icon-check"
          :disabled="selectedRows.length === 0"
          @click="handleBatchEnable"
        >
          批量启用
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-close"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDisable"
        >
          批量禁用
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      <div class="action-right">
        <el-button icon="el-icon-download" @click="handleExport">
          导出
        </el-button>
        <el-button icon="el-icon-refresh" @click="loadTableData">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        ref="dataTable"
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="appCode" label="应用编码" width="150" sortable="custom">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewDetail(scope.row)">
              {{ scope.row.appCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="appName" label="应用名称" min-width="150" sortable="custom">
          <template slot-scope="scope">
            <div class="app-info">
              <div class="app-name">{{ scope.row.appName }}</div>
              <div class="app-description">{{ scope.row.appDescription || '暂无描述' }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="appType" label="应用类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAppTypeTagType(scope.row.appType)" size="small">
              {{ formatAppType(scope.row.appType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appPlatform" label="应用平台" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAppPlatformTagType(scope.row.appPlatform)" size="small">
              {{ formatAppPlatform(scope.row.appPlatform) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appCategory" label="应用分类" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="small">
              {{ formatAppCategory(scope.row.appCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appVersion" label="版本" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ scope.row.appVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAppStatusTagType(scope.row.status)" size="small">
              {{ formatAppStatus(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="handleToggleEnable(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" label="默认应用" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isDefault" type="success" size="mini">默认</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleViewDetail(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleTest(scope.row)">
              测试
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'publish', row: scope.row}">
                  发布
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'unpublish', row: scope.row}">
                  下架
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'setDefault', row: scope.row}">
                  设为默认
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'version', row: scope.row}">
                  版本管理
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                  复制应用
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'export', row: scope.row}">
                  导出配置
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

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

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="dataForm"
        :model="form"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用名称" prop="appName">
              <el-input v-model="form.appName" placeholder="请输入应用名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用编码" prop="appCode">
              <el-input v-model="form.appCode" placeholder="请输入应用编码" :disabled="isEdit" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用类型" prop="appType">
              <el-select v-model="form.appType" placeholder="请选择应用类型" style="width: 100%">
                <el-option label="原生应用" value="NATIVE" />
                <el-option label="混合应用" value="HYBRID" />
                <el-option label="Web应用" value="WEB" />
                <el-option label="渐进式Web应用" value="PWA" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用平台" prop="appPlatform">
              <el-select v-model="form.appPlatform" placeholder="请选择应用平台" style="width: 100%">
                <el-option label="安卓" value="ANDROID" />
                <el-option label="苹果" value="IOS" />
                <el-option label="Windows" value="WINDOWS" />
                <el-option label="全平台" value="ALL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用分类" prop="appCategory">
              <el-select v-model="form.appCategory" placeholder="请选择应用分类" style="width: 100%">
                <el-option label="业务应用" value="BUSINESS" />
                <el-option label="工具应用" value="TOOL" />
                <el-option label="游戏应用" value="GAME" />
                <el-option label="教育应用" value="EDUCATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用版本" prop="appVersion">
              <el-input v-model="form.appVersion" placeholder="请输入应用版本" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用包名">
              <el-input v-model="form.appPackage" placeholder="请输入应用包名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用图标URL">
              <el-input v-model="form.appIconUrl" placeholder="请输入应用图标URL" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="应用描述">
          <el-input
            v-model="form.appDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入应用描述"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="启用状态">
              <el-switch v-model="form.isEnabled" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="默认应用">
              <el-switch v-model="form.isDefault" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="强制更新">
              <el-switch v-model="form.isForceUpdate" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="离线支持">
              <el-switch v-model="form.isOfflineSupport" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAppConfigPage,
  createAppConfig,
  updateAppConfig,
  deleteAppConfig,
  enableAppConfig,
  disableAppConfig,
  publishAppConfig,
  unpublishAppConfig,
  setDefaultAppConfig,
  batchEnableAppConfigs,
  batchDisableAppConfigs,
  formatAppType,
  formatAppPlatform,
  formatAppCategory,
  formatAppStatus,
  getAppTypeTagType,
  getAppPlatformTagType,
  getAppStatusTagType
} from '@/api/managementAccountant/mobile/appConfig'

export default {
  name: 'AppConfigList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        appName: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        status: ''
      },
      // 表格数据
      tableData: [],
      tableLoading: false,
      selectedRows: [],
      // 分页
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      // 排序
      sortField: '',
      sortOrder: '',
      // 对话框
      dialogVisible: false,
      dialogTitle: '创建应用',
      isEdit: false,
      submitLoading: false,
      // 表单
      form: {
        appConfigId: '',
        appName: '',
        appCode: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        appVersion: '1.0.0',
        appPackage: '',
        appIconUrl: '',
        appDescription: '',
        isEnabled: true,
        isDefault: false,
        isForceUpdate: false,
        isOfflineSupport: false,
        sortOrder: 0
      },
      formRules: {
        appName: [
          { required: true, message: '请输入应用名称', trigger: 'blur' }
        ],
        appCode: [
          { required: true, message: '请输入应用编码', trigger: 'blur' }
        ],
        appType: [
          { required: true, message: '请选择应用类型', trigger: 'change' }
        ],
        appPlatform: [
          { required: true, message: '请选择应用平台', trigger: 'change' }
        ],
        appCategory: [
          { required: true, message: '请选择应用分类', trigger: 'change' }
        ],
        appVersion: [
          { required: true, message: '请输入应用版本', trigger: 'blur' }
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
      this.tableLoading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }

        const response = await getAppConfigPage(params)
        if (response.success) {
          this.tableData = response.data || []
          this.pagination.total = response.total || 0
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载表格数据失败:', error)
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
    handleReset() {
      this.searchForm = {
        appName: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        status: ''
      }
      this.pagination.current = 1
      this.loadTableData()
    },

    // 显示创建对话框
    showCreateDialog() {
      this.dialogTitle = '创建应用'
      this.isEdit = false
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑应用'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 查看详情
    handleViewDetail(row) {
      this.$router.push(`/management-accountant/mobile/app-config/detail/${row.appConfigId}`)
    },

    // 测试
    handleTest(row) {
      this.$router.push(`/management-accountant/mobile/app-config/test?appConfigId=${row.appConfigId}`)
    },

    // 切换启用状态
    async handleToggleEnable(row) {
      try {
        const response = row.isEnabled 
          ? await enableAppConfig(row.appConfigId)
          : await disableAppConfig(row.appConfigId)
        
        if (response.success) {
          this.$message.success(row.isEnabled ? '启用成功' : '禁用成功')
        } else {
          // 恢复原状态
          row.isEnabled = !row.isEnabled
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        // 恢复原状态
        row.isEnabled = !row.isEnabled
        console.error('切换启用状态失败:', error)
        this.$message.error('操作失败')
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      
      switch (action) {
        case 'publish':
          await this.handlePublish(row)
          break
        case 'unpublish':
          await this.handleUnpublish(row)
          break
        case 'setDefault':
          await this.handleSetDefault(row)
          break
        case 'version':
          this.handleVersionManagement(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 发布应用
    async handlePublish(row) {
      try {
        const response = await publishAppConfig(row.appConfigId)
        if (response.success) {
          this.$message.success('发布成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '发布失败')
        }
      } catch (error) {
        console.error('发布应用失败:', error)
        this.$message.error('发布失败')
      }
    },

    // 下架应用
    async handleUnpublish(row) {
      try {
        const response = await unpublishAppConfig(row.appConfigId)
        if (response.success) {
          this.$message.success('下架成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '下架失败')
        }
      } catch (error) {
        console.error('下架应用失败:', error)
        this.$message.error('下架失败')
      }
    },

    // 设为默认
    async handleSetDefault(row) {
      try {
        const response = await setDefaultAppConfig(row.appConfigId)
        if (response.success) {
          this.$message.success('设置默认应用成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '设置失败')
        }
      } catch (error) {
        console.error('设置默认应用失败:', error)
        this.$message.error('设置失败')
      }
    },

    // 版本管理
    handleVersionManagement(row) {
      this.$message.info('版本管理功能开发中...')
    },

    // 复制应用
    handleCopy(row) {
      this.dialogTitle = '复制应用'
      this.isEdit = false
      this.form = { 
        ...row, 
        appConfigId: '',
        appCode: `${row.appCode}_COPY_${Date.now()}`,
        appName: `${row.appName}_副本`,
        isDefault: false
      }
      this.dialogVisible = true
    },

    // 导出单个
    handleExportSingle(row) {
      this.$message.info('导出单个应用配置功能开发中...')
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个应用配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteAppConfig(row.appConfigId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除应用失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 批量启用
    async handleBatchEnable() {
      try {
        const appConfigIds = this.selectedRows.map(row => row.appConfigId)
        const response = await batchEnableAppConfigs(appConfigIds)
        if (response.success) {
          this.$message.success('批量启用成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '批量启用失败')
        }
      } catch (error) {
        console.error('批量启用失败:', error)
        this.$message.error('批量启用失败')
      }
    },

    // 批量禁用
    async handleBatchDisable() {
      try {
        const appConfigIds = this.selectedRows.map(row => row.appConfigId)
        const response = await batchDisableAppConfigs(appConfigIds)
        if (response.success) {
          this.$message.success('批量禁用成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '批量禁用失败')
        }
      } catch (error) {
        console.error('批量禁用失败:', error)
        this.$message.error('批量禁用失败')
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的应用配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 这里应该调用批量删除API
        this.$message.info('批量删除功能开发中...')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.dataForm.validate()
        this.submitLoading = true

        const response = this.isEdit 
          ? await updateAppConfig(this.form)
          : await createAppConfig(this.form)

        if (response.success) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.loadTableData()
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('提交表单失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.submitLoading = false
      }
    },

    // 重置表单
    resetForm() {
      this.$refs.dataForm && this.$refs.dataForm.resetFields()
      this.form = {
        appConfigId: '',
        appName: '',
        appCode: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        appVersion: '1.0.0',
        appPackage: '',
        appIconUrl: '',
        appDescription: '',
        isEnabled: true,
        isDefault: false,
        isForceUpdate: false,
        isOfflineSupport: false,
        sortOrder: 0
      }
    },

    // 表格事件处理
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadTableData()
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadTableData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadTableData()
    },

    // 工具方法
    formatAppType,
    formatAppPlatform,
    formatAppCategory,
    formatAppStatus,
    getAppTypeTagType,
    getAppPlatformTagType,
    getAppStatusTagType,

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.app-config-list-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-section,
.action-section,
.table-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-form {
  .el-form-item {
    margin-bottom: 0;
  }
}

.action-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-info {
  .app-name {
    font-weight: 600;
    color: #303133;
    margin-bottom: 4px;
  }

  .app-description {
    font-size: 12px;
    color: #909399;
    line-height: 1.2;
  }
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
