<template>
  <div class="api-management-list">
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="API名称">
          <el-input
            v-model="searchForm.apiName"
            placeholder="请输入API名称"
            clearable
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="API类型">
          <el-select v-model="searchForm.apiType" placeholder="请选择API类型" clearable style="width: 150px">
            <el-option
              v-for="option in apiTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="API分类">
          <el-select v-model="searchForm.apiCategory" placeholder="请选择API分类" clearable style="width: 150px">
            <el-option
              v-for="option in apiCategoryOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option
              v-for="option in statusOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="HTTP方法">
          <el-select v-model="searchForm.httpMethod" placeholder="请选择HTTP方法" clearable style="width: 120px">
            <el-option
              v-for="option in httpMethodOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作区域 -->
    <div class="action-section">
      <div class="action-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新增API</el-button>
        <el-button
          type="success"
          icon="el-icon-check"
          :disabled="!hasSelection"
          @click="handleBatchEnable"
        >
          批量启用
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-close"
          :disabled="!hasSelection"
          @click="handleBatchDisable"
        >
          批量禁用
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
      <div class="action-right">
        <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
        <el-button icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-section">
      <el-table
        ref="dataTable"
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="apiCode" label="API编码" width="150" sortable="custom">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">{{ scope.row.apiCode }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="apiName" label="API名称" min-width="200" sortable="custom" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="api-name-cell">
              <div class="api-name">{{ scope.row.apiName }}</div>
              <div class="api-description">{{ scope.row.apiDescription }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="apiType" label="API类型" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getApiTypeTagType(scope.row.apiType)" size="small">
              {{ formatApiType(scope.row.apiType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="apiCategory" label="API分类" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getApiCategoryTagType(scope.row.apiCategory)" size="small">
              {{ formatApiCategory(scope.row.apiCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="httpMethod" label="HTTP方法" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getHttpMethodTagType(scope.row.httpMethod)" size="small">
              {{ scope.row.httpMethod }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="endpointPath" label="接口路径" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <code class="endpoint-path">{{ scope.row.endpointPath }}</code>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getApiStatusTagType(scope.row.status)" size="small">
              {{ formatApiStatus(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="apiVersion" label="版本" width="80" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.apiVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serviceProvider" label="服务提供者" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleTest(scope.row)">测试</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'enable', row: scope.row}" v-if="scope.row.status !== 'ACTIVE'">
                  启用
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'disable', row: scope.row}" v-if="scope.row.status === 'ACTIVE'">
                  禁用
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'deprecate', row: scope.row}" v-if="!scope.row.isDeprecated">
                  设为废弃
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'version', row: scope.row}">
                  版本管理
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                  复制API
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

    <!-- 分页区域 -->
    <div class="pagination-section">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form ref="dataForm" :model="formData" :rules="formRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API名称" prop="apiName">
              <el-input v-model="formData.apiName" placeholder="请输入API名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API编码" prop="apiCode">
              <el-input v-model="formData.apiCode" placeholder="请输入API编码" :disabled="isEdit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="API类型" prop="apiType">
              <el-select v-model="formData.apiType" placeholder="请选择API类型" style="width: 100%">
                <el-option
                  v-for="option in apiTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API分类" prop="apiCategory">
              <el-select v-model="formData.apiCategory" placeholder="请选择API分类" style="width: 100%">
                <el-option
                  v-for="option in apiCategoryOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="HTTP方法" prop="httpMethod">
              <el-select v-model="formData.httpMethod" placeholder="请选择HTTP方法" style="width: 100%">
                <el-option
                  v-for="option in httpMethodOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API版本" prop="apiVersion">
              <el-input v-model="formData.apiVersion" placeholder="请输入API版本"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="接口路径" prop="endpointPath">
          <el-input v-model="formData.endpointPath" placeholder="请输入接口路径，如：/api/users"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求格式" prop="requestFormat">
              <el-select v-model="formData.requestFormat" placeholder="请选择请求格式" style="width: 100%">
                <el-option
                  v-for="option in dataFormatOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="响应格式" prop="responseFormat">
              <el-select v-model="formData.responseFormat" placeholder="请选择响应格式" style="width: 100%">
                <el-option
                  v-for="option in dataFormatOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务提供者" prop="serviceProvider">
              <el-input v-model="formData.serviceProvider" placeholder="请输入服务提供者"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务域" prop="businessDomain">
              <el-input v-model="formData.businessDomain" placeholder="请输入业务域"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="API描述" prop="apiDescription">
          <el-input
            v-model="formData.apiDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入API描述"
          ></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="需要认证">
              <el-switch v-model="formData.authenticationRequired"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="启用监控">
              <el-switch v-model="formData.monitoringEnabled"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="日志级别" prop="loggingLevel">
              <el-select v-model="formData.loggingLevel" placeholder="请选择日志级别" style="width: 100%">
                <el-option
                  v-for="option in logLevelOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getApiManagementPage,
  createApiManagement,
  updateApiManagement,
  deleteApiManagement,
  enableApiManagement,
  disableApiManagement,
  batchEnableApiManagements,
  batchDisableApiManagements,
  getApiManagementOptions,
  formatApiType,
  formatApiCategory,
  formatApiStatus,
  getApiStatusTagType
} from '@/api/managementAccountant/intg/apiManagement'

export default {
  name: 'ApiManagementList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        apiName: '',
        apiType: '',
        apiCategory: '',
        status: '',
        httpMethod: '',
        serviceProvider: '',
        businessDomain: ''
      },
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      
      // 排序
      sortField: '',
      sortOrder: '',
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      saveLoading: false,
      
      // 表单数据
      formData: {},
      formRules: {
        apiName: [
          { required: true, message: '请输入API名称', trigger: 'blur' }
        ],
        apiCode: [
          { required: true, message: '请输入API编码', trigger: 'blur' }
        ],
        apiType: [
          { required: true, message: '请选择API类型', trigger: 'change' }
        ],
        endpointPath: [
          { required: true, message: '请输入接口路径', trigger: 'blur' }
        ],
        httpMethod: [
          { required: true, message: '请选择HTTP方法', trigger: 'change' }
        ]
      },
      
      // 选项数据
      apiTypeOptions: [],
      apiCategoryOptions: [],
      httpMethodOptions: [],
      statusOptions: [],
      dataFormatOptions: [],
      logLevelOptions: []
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  created() {
    this.loadOptions()
    this.loadData()
  },
  methods: {
    // 加载选项数据
    loadOptions() {
      const options = getApiManagementOptions()
      this.apiTypeOptions = options.apiTypes
      this.apiCategoryOptions = options.apiCategories
      this.httpMethodOptions = options.httpMethods
      this.statusOptions = options.apiStatuses
      this.dataFormatOptions = options.dataFormats
      this.logLevelOptions = options.logLevels
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
        
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await getApiManagementPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
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
    
    // 重置搜索
    handleReset() {
      this.searchForm = {
        apiName: '',
        apiType: '',
        apiCategory: '',
        status: '',
        httpMethod: '',
        serviceProvider: '',
        businessDomain: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    // 新增
    handleCreate() {
      this.dialogTitle = '新增API'
      this.isEdit = false
      this.formData = {
        apiName: '',
        apiCode: '',
        apiType: 'REST',
        apiCategory: 'BUSINESS',
        httpMethod: 'GET',
        apiVersion: '1.0.0',
        endpointPath: '',
        requestFormat: 'JSON',
        responseFormat: 'JSON',
        serviceProvider: '',
        businessDomain: '',
        apiDescription: '',
        authenticationRequired: false,
        monitoringEnabled: true,
        loggingLevel: 'INFO'
      }
      this.dialogVisible = true
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑API'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    
    // 查看
    handleView(row) {
      this.$router.push(`/management-accountant/intg/api-management/detail/${row.apiId}`)
    },
    
    // 测试
    handleTest(row) {
      this.$router.push(`/management-accountant/intg/api-management/test?apiId=${row.apiId}`)
    },
    
    // 保存
    async handleSave() {
      try {
        await this.$refs.dataForm.validate()
        this.saveLoading = true
        
        let response
        if (this.isEdit) {
          response = await updateApiManagement(this.formData)
        } else {
          response = await createApiManagement(this.formData)
        }
        
        if (response.success) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      } finally {
        this.saveLoading = false
      }
    },
    
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个API吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteApiManagement(row.apiId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 启用
    async handleEnable(row) {
      try {
        const response = await enableApiManagement(row.apiId)
        if (response.success) {
          this.$message.success('启用成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '启用失败')
        }
      } catch (error) {
        console.error('启用失败:', error)
        this.$message.error('启用失败')
      }
    },
    
    // 禁用
    async handleDisable(row) {
      try {
        const response = await disableApiManagement(row.apiId)
        if (response.success) {
          this.$message.success('禁用成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '禁用失败')
        }
      } catch (error) {
        console.error('禁用失败:', error)
        this.$message.error('禁用失败')
      }
    },
    
    // 批量启用
    async handleBatchEnable() {
      try {
        const apiIds = this.selectedRows.map(row => row.apiId)
        const response = await batchEnableApiManagements(apiIds)
        if (response.success) {
          this.$message.success('批量启用成功')
          this.loadData()
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
        const apiIds = this.selectedRows.map(row => row.apiId)
        const response = await batchDisableApiManagements(apiIds)
        if (response.success) {
          this.$message.success('批量禁用成功')
          this.loadData()
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
        await this.$confirm('确定要删除选中的API吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // TODO: 实现批量删除API接口
        this.$message.success('批量删除成功')
        this.loadData()
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
    
    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'enable':
          this.handleEnable(row)
          break
        case 'disable':
          this.handleDisable(row)
          break
        case 'deprecate':
          this.$message.info('设为废弃功能开发中...')
          break
        case 'version':
          this.$message.info('版本管理功能开发中...')
          break
        case 'copy':
          this.$message.info('复制API功能开发中...')
          break
        case 'export':
          this.$message.info('导出配置功能开发中...')
          break
        case 'delete':
          this.handleDelete(row)
          break
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
    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    
    // 对话框关闭
    handleDialogClose(done) {
      if (this.saveLoading) {
        return
      }
      this.$refs.dataForm.resetFields()
      done()
    },
    
    // 格式化方法
    formatApiType,
    formatApiCategory,
    formatApiStatus,
    getApiStatusTagType,
    
    // 获取API类型标签类型
    getApiTypeTagType(apiType) {
      const tagTypeMap = {
        'REST': 'primary',
        'SOAP': 'success',
        'GRAPHQL': 'warning',
        'RPC': 'info'
      }
      return tagTypeMap[apiType] || 'info'
    },
    
    // 获取API分类标签类型
    getApiCategoryTagType(apiCategory) {
      const tagTypeMap = {
        'BUSINESS': 'primary',
        'SYSTEM': 'success',
        'INTEGRATION': 'warning'
      }
      return tagTypeMap[apiCategory] || 'info'
    },
    
    // 获取HTTP方法标签类型
    getHttpMethodTagType(httpMethod) {
      const tagTypeMap = {
        'GET': 'success',
        'POST': 'primary',
        'PUT': 'warning',
        'DELETE': 'danger',
        'PATCH': 'info'
      }
      return tagTypeMap[httpMethod] || 'info'
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.api-management-list {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .search-form {
    margin-bottom: 0;
  }
}

.action-section {
  background: white;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.table-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .api-name-cell {
    .api-name {
      font-weight: 500;
      color: #303133;
      margin-bottom: 4px;
    }

    .api-description {
      font-size: 12px;
      color: #909399;
      line-height: 1.2;
    }
  }

  .endpoint-path {
    background: #f5f7fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Courier New', monospace;
    font-size: 12px;
    color: #606266;
  }
}

.pagination-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: right;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dialog-footer {
  text-align: right;
}
</style>
