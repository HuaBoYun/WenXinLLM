<template>
  <div class="system-config-list">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="配置名称">
          <el-input
            v-model="searchForm.configName"
            placeholder="请输入配置名称"
            clearable
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="系统类型">
          <el-select v-model="searchForm.systemType" placeholder="请选择系统类型" clearable style="width: 150px">
            <el-option
              v-for="(label, value) in systemTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="连接类型">
          <el-select v-model="searchForm.connectionType" placeholder="请选择连接类型" clearable style="width: 150px">
            <el-option
              v-for="(label, value) in connectionTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="配置状态">
          <el-select v-model="searchForm.configStatus" placeholder="请选择配置状态" clearable style="width: 120px">
            <el-option label="启用" value="ACTIVE"></el-option>
            <el-option label="禁用" value="INACTIVE"></el-option>
            <el-option label="测试中" value="TESTING"></el-option>
            <el-option label="错误" value="ERROR"></el-option>
            <el-option label="维护中" value="MAINTENANCE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="连接状态">
          <el-select v-model="searchForm.connectionStatus" placeholder="请选择连接状态" clearable style="width: 120px">
            <el-option label="已连接" value="CONNECTED"></el-option>
            <el-option label="未连接" value="DISCONNECTED"></el-option>
            <el-option label="连接中" value="CONNECTING"></el-option>
            <el-option label="连接错误" value="ERROR"></el-option>
            <el-option label="连接超时" value="TIMEOUT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">新建配置</el-button>
        <el-button type="success" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
        <el-button 
          type="warning" 
          icon="el-icon-connection" 
          :disabled="selectedRows.length === 0"
          @click="batchTestConnection"
        >
          批量测试连接
        </el-button>
        <el-button 
          type="info" 
          icon="el-icon-check" 
          :disabled="selectedRows.length === 0"
          @click="batchHealthCheck"
        >
          批量健康检查
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button-group>
          <el-button 
            :type="selectedRows.length > 0 ? 'success' : ''" 
            icon="el-icon-check" 
            :disabled="selectedRows.length === 0"
            @click="batchEnable"
          >
            批量启用
          </el-button>
          <el-button 
            :type="selectedRows.length > 0 ? 'warning' : ''" 
            icon="el-icon-close" 
            :disabled="selectedRows.length === 0"
            @click="batchDisable"
          >
            批量禁用
          </el-button>
          <el-button 
            :type="selectedRows.length > 0 ? 'danger' : ''" 
            icon="el-icon-delete" 
            :disabled="selectedRows.length === 0"
            @click="batchDelete"
          >
            批量删除
          </el-button>
        </el-button-group>
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
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="configCode" label="配置编码" width="150" sortable="custom">
          <template slot-scope="scope">
            <el-link type="primary" @click="viewDetail(scope.row.configId)">
              {{ scope.row.configCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="configName" label="配置名称" min-width="180" sortable="custom">
          <template slot-scope="scope">
            <div class="config-name-cell">
              <div class="name">{{ scope.row.configName }}</div>
              <div class="description" v-if="scope.row.configDescription">
                {{ scope.row.configDescription }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="systemType" label="系统类型" width="120" sortable="custom">
          <template slot-scope="scope">
            <el-tag size="small">{{ formatSystemTypeName(scope.row.systemType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionType" label="连接类型" width="120" sortable="custom">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ formatConnectionTypeName(scope.row.connectionType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionUrl" label="连接地址" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{ scope.row.connectionUrl }}</span>
            <span v-if="scope.row.connectionPort">:{{ scope.row.connectionPort }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="configStatus" label="配置状态" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.configStatus)" size="small">
              {{ formatStatusName(scope.row.configStatus, 'config') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionStatus" label="连接状态" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getConnectionStatusType(scope.row.connectionStatus)" size="small">
              {{ formatStatusName(scope.row.connectionStatus, 'connection') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="healthStatus" label="健康状态" width="100" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getHealthStatusType(scope.row.healthStatus)" size="small">
              {{ formatStatusName(scope.row.healthStatus, 'health') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastTestTime" label="最后测试时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.lastTestTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row.configId)">
              详情
            </el-button>
            <el-button size="mini" type="success" @click="testConnection(scope.row)">
              测试连接
            </el-button>
            <el-button size="mini" type="info" @click="performHealthCheck(scope.row)">
              健康检查
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: scope.row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
                <el-dropdown-item 
                  :command="{action: scope.row.configStatus === 'ACTIVE' ? 'disable' : 'enable', row: scope.row}"
                >
                  {{ scope.row.configStatus === 'ACTIVE' ? '禁用' : '启用' }}
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

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
      ></el-pagination>
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="formRules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="form.configName" placeholder="请输入配置名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置编码" prop="configCode">
              <el-input v-model="form.configCode" placeholder="自动生成" :disabled="isEdit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="系统类型" prop="systemType">
              <el-select v-model="form.systemType" placeholder="请选择系统类型" style="width: 100%">
                <el-option
                  v-for="(label, value) in systemTypeOptions"
                  :key="value"
                  :label="label"
                  :value="value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="连接类型" prop="connectionType">
              <el-select v-model="form.connectionType" placeholder="请选择连接类型" style="width: 100%">
                <el-option
                  v-for="(label, value) in connectionTypeOptions"
                  :key="value"
                  :label="label"
                  :value="value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="连接地址" prop="connectionUrl">
              <el-input v-model="form.connectionUrl" placeholder="请输入连接地址"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="端口" prop="connectionPort">
              <el-input-number v-model="form.connectionPort" :min="1" :max="65535" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据库名" prop="databaseName">
              <el-input v-model="form.databaseName" placeholder="请输入数据库名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="连接超时" prop="connectionTimeout">
              <el-input-number v-model="form.connectionTimeout" :min="1" :max="300" style="width: 100%">
                <template slot="append">秒</template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="配置描述">
          <el-input v-model="form.configDescription" type="textarea" :rows="3" placeholder="请输入配置描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSystemConfigPage,
  createSystemConfig,
  updateSystemConfig,
  deleteSystemConfig,
  testConnection,
  performHealthCheck,
  batchTestConnection,
  batchPerformHealthCheck,
  enableSystemConfig,
  disableSystemConfig,
  batchUpdateConfigStatus,
  formatSystemTypeName,
  formatConnectionTypeName,
  formatStatusName,
  SYSTEM_TYPES,
  CONNECTION_TYPES
} from '@/api/managementAccountant/intg/systemConfig'

export default {
  name: 'SystemConfigList',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        configName: '',
        systemType: '',
        connectionType: '',
        configStatus: '',
        connectionStatus: ''
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortInfo: {
        orderBy: 'created_time',
        orderDirection: 'DESC'
      },
      form: {
        configId: '',
        configName: '',
        configCode: '',
        systemType: '',
        connectionType: '',
        connectionUrl: '',
        connectionPort: null,
        username: '',
        password: '',
        databaseName: '',
        connectionTimeout: 30,
        configDescription: ''
      },
      formRules: {
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        systemType: [
          { required: true, message: '请选择系统类型', trigger: 'change' }
        ],
        connectionType: [
          { required: true, message: '请选择连接类型', trigger: 'change' }
        ],
        connectionUrl: [
          { required: true, message: '请输入连接地址', trigger: 'blur' }
        ]
      },
      systemTypeOptions: {},
      connectionTypeOptions: {}
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑系统配置' : '新建系统配置'
    }
  },
  created() {
    this.initOptions()
    this.loadData()
  },
  methods: {
    initOptions() {
      // 初始化系统类型选项
      Object.keys(SYSTEM_TYPES).forEach(key => {
        this.systemTypeOptions[SYSTEM_TYPES[key]] = formatSystemTypeName(SYSTEM_TYPES[key])
      })
      
      // 初始化连接类型选项
      Object.keys(CONNECTION_TYPES).forEach(key => {
        this.connectionTypeOptions[CONNECTION_TYPES[key]] = formatConnectionTypeName(CONNECTION_TYPES[key])
      })
    },
    
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm,
          ...this.sortInfo
        }
        
        const response = await getSystemConfigPage(params)
        
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    handleReset() {
      this.searchForm = {
        configName: '',
        systemType: '',
        connectionType: '',
        configStatus: '',
        connectionStatus: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    refreshData() {
      this.loadData()
      this.$message.success('数据已刷新')
    },
    
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    handleSortChange({ column, prop, order }) {
      if (order) {
        this.sortInfo.orderBy = prop
        this.sortInfo.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      } else {
        this.sortInfo.orderBy = 'created_time'
        this.sortInfo.orderDirection = 'DESC'
      }
      this.loadData()
    },
    
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },
    
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    
    showCreateDialog() {
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
        this.form = {
          configId: '',
          configName: '',
          configCode: '',
          systemType: '',
          connectionType: '',
          connectionUrl: '',
          connectionPort: null,
          username: '',
          password: '',
          databaseName: '',
          connectionTimeout: 30,
          configDescription: ''
        }
      })
    },
    
    showEditDialog(row) {
      this.isEdit = true
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
        this.form = { ...row }
      })
    },
    
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        if (this.isEdit) {
          await updateSystemConfig(this.form.configId, this.form)
          this.$message.success('配置更新成功')
        } else {
          await createSystemConfig(this.form)
          this.$message.success('配置创建成功')
        }
        
        this.dialogVisible = false
        this.loadData()
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.submitLoading = false
      }
    },
    
    viewDetail(configId) {
      this.$router.push(`/management-accountant/intg/system-config/detail/${configId}`)
    },
    
    async testConnection(config) {
      try {
        this.$message.info('正在测试连接...')
        const response = await testConnection(config.configId)
        
        if (response.success) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error('连接测试失败')
        }
        
        this.loadData()
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message.error('连接测试失败')
      }
    },
    
    async performHealthCheck(config) {
      try {
        this.$message.info('正在执行健康检查...')
        const response = await performHealthCheck(config.configId)
        
        if (response.success) {
          this.$message.success('健康检查完成')
        } else {
          this.$message.error('健康检查失败')
        }
        
        this.loadData()
      } catch (error) {
        console.error('健康检查失败:', error)
        this.$message.error('健康检查失败')
      }
    },
    
    async batchTestConnection() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要测试的配置')
        return
      }
      
      try {
        this.$message.info('正在批量测试连接...')
        const configIds = this.selectedRows.map(row => row.configId)
        const response = await batchTestConnection(configIds)
        
        if (response.success) {
          this.$message.success('批量连接测试完成')
        } else {
          this.$message.error('批量连接测试失败')
        }
        
        this.loadData()
      } catch (error) {
        console.error('批量连接测试失败:', error)
        this.$message.error('批量连接测试失败')
      }
    },
    
    async batchHealthCheck() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要检查的配置')
        return
      }
      
      try {
        this.$message.info('正在批量健康检查...')
        const configIds = this.selectedRows.map(row => row.configId)
        const response = await batchPerformHealthCheck(configIds)
        
        if (response.success) {
          this.$message.success('批量健康检查完成')
        } else {
          this.$message.error('批量健康检查失败')
        }
        
        this.loadData()
      } catch (error) {
        console.error('批量健康检查失败:', error)
        this.$message.error('批量健康检查失败')
      }
    },
    
    async batchEnable() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要启用的配置')
        return
      }
      
      try {
        const configIds = this.selectedRows.map(row => row.configId)
        await batchUpdateConfigStatus(configIds, 'ACTIVE')
        this.$message.success('批量启用成功')
        this.loadData()
      } catch (error) {
        console.error('批量启用失败:', error)
        this.$message.error('批量启用失败')
      }
    },
    
    async batchDisable() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要禁用的配置')
        return
      }
      
      try {
        const configIds = this.selectedRows.map(row => row.configId)
        await batchUpdateConfigStatus(configIds, 'INACTIVE')
        this.$message.success('批量禁用成功')
        this.loadData()
      } catch (error) {
        console.error('批量禁用失败:', error)
        this.$message.error('批量禁用失败')
      }
    },
    
    async batchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的配置')
        return
      }
      
      try {
        await this.$confirm('确定要删除选中的配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        for (const row of this.selectedRows) {
          await deleteSystemConfig(row.configId)
        }
        
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },
    
    async handleCommand({ action, row }) {
      switch (action) {
        case 'edit':
          this.showEditDialog(row)
          break
        case 'copy':
          this.copyConfig(row)
          break
        case 'enable':
          await this.enableConfig(row)
          break
        case 'disable':
          await this.disableConfig(row)
          break
        case 'delete':
          await this.deleteConfig(row)
          break
      }
    },
    
    copyConfig(row) {
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
        this.form = {
          ...row,
          configId: '',
          configCode: '',
          configName: row.configName + '_副本'
        }
      })
    },
    
    async enableConfig(row) {
      try {
        await enableSystemConfig(row.configId)
        this.$message.success('配置启用成功')
        this.loadData()
      } catch (error) {
        console.error('启用配置失败:', error)
        this.$message.error('启用配置失败')
      }
    },
    
    async disableConfig(row) {
      try {
        await disableSystemConfig(row.configId)
        this.$message.success('配置禁用成功')
        this.loadData()
      } catch (error) {
        console.error('禁用配置失败:', error)
        this.$message.error('禁用配置失败')
      }
    },
    
    async deleteConfig(row) {
      try {
        await this.$confirm('确定要删除该配置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteSystemConfig(row.configId)
        this.$message.success('配置删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除配置失败:', error)
          this.$message.error('删除配置失败')
        }
      }
    },
    
    formatSystemTypeName,
    formatConnectionTypeName,
    formatStatusName,
    
    getStatusType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TESTING': 'warning',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      return typeMap[status] || 'info'
    },
    
    getConnectionStatusType(status) {
      const typeMap = {
        'CONNECTED': 'success',
        'DISCONNECTED': 'info',
        'CONNECTING': 'warning',
        'ERROR': 'danger',
        'TIMEOUT': 'warning'
      }
      return typeMap[status] || 'info'
    },
    
    getHealthStatusType(status) {
      const typeMap = {
        'HEALTHY': 'success',
        'UNHEALTHY': 'danger',
        'WARNING': 'warning',
        'CRITICAL': 'danger',
        'UNKNOWN': 'info'
      }
      return typeMap[status] || 'info'
    },
    
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.system-config-list {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.search-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.toolbar {
  background: white;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.pagination-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: right;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.config-name-cell {
  .name {
    font-weight: 500;
    color: #303133;
    margin-bottom: 4px;
  }
  
  .description {
    font-size: 12px;
    color: #909399;
    line-height: 1.2;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
