<template>
  <div class="business-system-register">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-monitor"></i>
            业务系统注册
          </h2>
          <p class="page-description">管理接入的业务系统信息，包括系统认证、接口配置和权限管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            注册系统
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSync">
            同步状态
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 系统概览卡片 -->
    <div class="system-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-monitor"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总系统数</div>
                <div class="card-value">{{ totalSystems }}</div>
                <div class="card-change">已注册系统</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在线系统</div>
                <div class="card-value">{{ activeSystems }}</div>
                <div class="card-change positive">正常运行</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon offline-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">离线系统</div>
                <div class="card-value">{{ offlineSystems }}</div>
                <div class="card-change negative">需要检查</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后检查</div>
                <div class="card-value">{{ lastCheckTime }}</div>
                <div class="card-change">系统状态</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="系统名称">
            <el-input
              v-model="listQuery.systemName"
              placeholder="请输入系统名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="系统类型">
            <el-select
              v-model="listQuery.systemType"
              placeholder="请选择系统类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="财务系统" value="FINANCE" />
              <el-option label="银行系统" value="BANK" />
              <el-option label="ERP系统" value="ERP" />
              <el-option label="CRM系统" value="CRM" />
              <el-option label="第三方系统" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.connectionStatus"
              placeholder="请选择连接状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="在线" value="ONLINE" />
              <el-option label="离线" value="OFFLINE" />
              <el-option label="异常" value="ERROR" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">业务系统列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="visibleColumns.includes('systemName')" label="系统信息" prop="systemName" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="system-info">
              <div class="system-icon">
                <i :class="getSystemIcon(row.systemType)"></i>
              </div>
              <div class="system-details">
                <div class="system-name">{{ row.systemName }}</div>
                <div class="system-code">{{ row.systemCode }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('systemType')" label="系统类型" prop="systemType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getSystemTypeColor(row.systemType)" size="small">
              {{ getSystemTypeText(row.systemType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('connectionStatus')" label="连接状态" prop="connectionStatus" align="center" width="120">
          <template slot-scope="{row}">
            <div class="connection-status">
              <el-badge :status="getConnectionStatusType(row.connectionStatus)" />
              <span :class="'status-text ' + row.connectionStatus.toLowerCase()">
                {{ getConnectionStatusText(row.connectionStatus) }}
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('connectionUrl')" label="接口地址" prop="apiUrl" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="api-url">
              <i class="el-icon-link"></i>
              <span>{{ row.apiUrl || '未配置' }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('authType')" label="认证方式" prop="authType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag size="mini" :type="getAuthTypeColor(row.authType)">
              {{ getAuthTypeText(row.authType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('lastHeartbeat')" label="最后心跳" prop="lastHeartbeat" align="center" width="160">
          <template slot-scope="{row}">
            <span class="heartbeat-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.lastHeartbeat) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('status')" label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-connection" @click="handleTest(row)">
                测试
              </el-button>
              <el-button
                v-if="row.status !== 'deleted'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="temp.systemName" />
        </el-form-item>
        <el-form-item label="系统编码" prop="systemCode">
          <el-input v-model="temp.systemCode" />
        </el-form-item>
        <el-form-item label="系统描述">
          <el-input v-model="temp.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listBusinessSystem,
  addBusinessSystem,
  updateBusinessSystem,
  delBusinessSystem,
  getBusinessSystem,
  exportBusinessSystem,
  testConnection,
  batchDeleteBusinessSystem,
  toggleBusinessSystemStatus,
  syncBusinessSystemStatus,
  getSystemTypes,
  getAuthTypes,
  getSystemStatuses,
  getConnectionStatuses,
  validateBusinessSystemConfig,
  getBusinessSystemStatistics,
  batchImportBusinessSystem,
  getBusinessSystemLogs,
  cleanBusinessSystemLogs,
  resetBusinessSystemPassword
} from '@/api/treasuryCommon/basicConfigBusinessSystemRegister'
import waves from '@/directive/waves/waves'
import { parseTime } from '@/utils/index'
import Pagination from '@/components/Pagination'

export default {
  name: 'BusinessSystemRegister',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      totalSystems: 0,
      activeSystems: 0,
      offlineSystems: 0,
      lastCheckTime: new Date().toLocaleString(),
      listQuery: {
        page: 1,
        limit: 20,
        systemName: undefined,
        systemType: undefined,
        connectionStatus: undefined,
        status: undefined
      },
      statusOptions: [
        { key: 1, display_name: '启用' },
        { key: 0, display_name: '禁用' }
      ],
      temp: {
        id: undefined,
        systemName: '',
        systemCode: '',
        description: '',
        status: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      submitLoading: false,
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        systemName: [{ required: true, message: '系统名称是必填项', trigger: 'blur' }],
        systemCode: [{ required: true, message: '系统编码是必填项', trigger: 'blur' }]
      },
      // 表格列配置
      allColumns: [
        { key: 'systemCode', label: '系统编码' },
        { key: 'systemName', label: '系统名称' },
        { key: 'systemType', label: '系统类型' },
        { key: 'version', label: '版本号' },
        { key: 'connectionUrl', label: '连接地址' },
        { key: 'authType', label: '认证方式' },
        { key: 'connectionStatus', label: '连接状态' },
        { key: 'status', label: '状态' },
        { key: 'createTime', label: '创建时间' }
      ],
      visibleColumns: JSON.parse(localStorage.getItem('businessSystem_visibleColumns') || '["systemCode","systemName","systemType","version","connectionUrl","authType","status","createTime"]')
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          systemName: this.listQuery.systemName,
          systemType: this.listQuery.systemType,
          connectionStatus: this.listQuery.connectionStatus,
          status: this.listQuery.status
        }
        const response = await listBusinessSystem(params)
        if (response.code === 1 || response.code === 200) {
          this.list = response.data?.tlist || []
          this.total = response.data?.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取业务系统列表失败')
        }
      } catch (error) {
        console.error('获取业务系统列表失败:', error)
        this.$message.error('网络错误，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        systemName: '',
        systemCode: '',
        description: '',
        status: 1
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await addBusinessSystem(this.temp)
            if (response.code === 1 || response.code === 200) {
              this.$message.success('新增成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '新增失败')
            }
          } catch (error) {
            console.error('新增失败:', error)
            this.$message.error('新增失败，请稍后重试')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            this.submitLoading = true
            const response = await updateBusinessSystem(this.temp)
            if (response.code === 1 || response.code === 200) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败，请稍后重试')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await delBusinessSystem(row.id)
          if (response.code === 1 || response.code === 200 || response.success) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      })
    },
    async handleStatusChange(row) {
      try {
        const data = {
          id: row.id,
          status: row.status,
          updateUser: JSON.parse(localStorage.getItem('userInfo') || '{}').staffid || 1
        }
        const response = await toggleBusinessSystemStatus(data)
        if (response.code === 1 || response.code === 200) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.message || '状态更新失败')
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    },
    async handleSync() {
      try {
        const response = await syncBusinessSystemStatus()
        if (response.code === 1 || response.code === 200) {
          this.$message.success('同步状态成功')
          this.getList()
        } else {
          this.$message.error(response.message || '同步状态失败')
        }
      } catch (error) {
        console.error('同步状态失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    },
    async handleTest(row) {
      try {
        const response = await testConnection(row.id)
        if (response.code === 1 || response.code === 200) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.message || '连接测试失败')
        }
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    },
    async handleExport() {
      try {
        const params = {
          systemName: this.listQuery.systemName,
          systemType: this.listQuery.systemType,
          connectionStatus: this.listQuery.connectionStatus,
          status: this.listQuery.status
        }
        const response = await exportBusinessSystem(params)
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `业务系统注册_${new Date().toLocaleDateString()}.xlsx`
        link.click()
        window.URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    getSystemIcon(systemType) {
      const iconMap = {
        'FINANCE': 'el-icon-coin',
        'BANK': 'el-icon-bank-card',
        'ERP': 'el-icon-monitor',
        'CRM': 'el-icon-user',
        'THIRD_PARTY': 'el-icon-connection'
      }
      return iconMap[systemType] || 'el-icon-setting'
    },
    getSystemTypeColor(systemType) {
      const colorMap = {
        'FINANCE': 'primary',
        'BANK': 'success',
        'ERP': 'warning',
        'CRM': 'info',
        'THIRD_PARTY': 'danger'
      }
      return colorMap[systemType] || 'info'
    },
    getSystemTypeText(systemType) {
      const textMap = {
        'FINANCE': '财务系统',
        'BANK': '银行系统',
        'ERP': 'ERP系统',
        'CRM': 'CRM系统',
        'THIRD_PARTY': '第三方系统'
      }
      return textMap[systemType] || systemType
    },
    getConnectionStatusType(status) {
      const typeMap = {
        'ONLINE': 'success',
        'OFFLINE': 'info',
        'ERROR': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getConnectionStatusText(status) {
      const textMap = {
        'ONLINE': '在线',
        'OFFLINE': '离线',
        'ERROR': '异常'
      }
      return textMap[status] || status
    },
    getAuthTypeColor(authType) {
      const colorMap = {
        'OAUTH2': 'primary',
        'API_KEY': 'success',
        'BASIC_AUTH': 'warning',
        'JWT': 'info'
      }
      return colorMap[authType] || 'info'
    },
    getAuthTypeText(authType) {
      const textMap = {
        'OAUTH2': 'OAuth2',
        'API_KEY': 'API Key',
        'BASIC_AUTH': 'Basic Auth',
        'JWT': 'JWT'
      }
      return textMap[authType] || authType
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleTableSetting() {
      const h = this.$createElement
      this.$msgbox({
        title: '表格列设置',
        message: h('div', { class: 'table-setting-dialog' }, [
          h('p', { style: 'margin-bottom: 10px; color: #909399;' }, '请选择要显示的列:'),
          h('el-checkbox-group', {
            props: { value: this.visibleColumns },
            on: { input: val => { this.visibleColumns = val } }
          }, this.allColumns.map(col =>
            h('el-checkbox', { props: { label: col.key, border: true, style: 'margin-left: 0; margin-right: 10px; margin-bottom: 10px;' } }, col.label)
          ))
        ]),
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 保存到localStorage
            localStorage.setItem('businessSystem_visibleColumns', JSON.stringify(this.visibleColumns))
            this.$message.success('表格设置已保存')
            this.getList() // 刷新表格
          }
          done()
        }
      })
    }
  }
}
</script>
