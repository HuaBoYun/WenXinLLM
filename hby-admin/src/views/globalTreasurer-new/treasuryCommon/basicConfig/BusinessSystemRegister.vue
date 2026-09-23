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

        <el-table-column v-if="isColumnVisible('systemName')" label="系统信息" prop="systemName" align="center" width="200" show-overflow-tooltip>
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

        <el-table-column v-if="isColumnVisible('systemType')" label="系统类型" prop="systemType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getSystemTypeColor(row.systemType)" size="small">
              {{ getSystemTypeText(row.systemType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('connectionStatus')" label="连接状态" prop="connectionStatus" align="center" width="120">
          <template slot-scope="{row}">
            <div class="connection-status">
              <el-badge :status="getConnectionStatusType(row.connectionStatus)" />
              <span :class="'status-text ' + row.connectionStatus.toLowerCase()">
                {{ getConnectionStatusText(row.connectionStatus) }}
              </span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('apiUrl')" label="接口地址" prop="apiUrl" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="api-url">
              <i class="el-icon-link"></i>
              <span>{{ row.apiUrl || '未配置' }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('authType')" label="认证方式" prop="authType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag size="mini" :type="getAuthTypeColor(row.authType)">
              {{ getAuthTypeText(row.authType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('lastHeartbeat')" label="最后心跳" prop="lastHeartbeat" align="center" width="160">
          <template slot-scope="{row}">
            <span class="heartbeat-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.lastHeartbeat) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('status')" label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="'1'"
              :inactive-value="'0'"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('actions')" label="操作" align="center" width="240" class-name="small-padding fixed-width">
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="padding: 0 20px;">
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="temp.systemName" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="系统编码" prop="systemCode">
          <el-input v-model="temp.systemCode" placeholder="请输入系统编码" />
        </el-form-item>
        <el-form-item label="系统类型" prop="systemType">
          <el-select v-model="temp.systemType" placeholder="请选择系统类型" style="width: 100%;">
            <el-option v-for="item in systemTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="接口地址" prop="apiUrl">
          <el-input v-model="temp.apiUrl" placeholder="请输入API接口地址" />
        </el-form-item>
        <el-form-item label="认证方式" prop="authType">
          <el-select v-model="temp.authType" placeholder="请选择认证方式" style="width: 100%;">
            <el-option v-for="item in authTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="认证配置">
          <el-input v-model="temp.authConfig" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入认证配置信息（JSON格式）" />
        </el-form-item>
        <el-form-item label="系统描述">
          <el-input v-model="temp.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入系统描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="temp.status" placeholder="请选择状态" style="width: 100%;">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="业务系统详情" :visible.sync="viewDialogVisible" width="600px">
      <div v-if="currentViewData" class="view-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="view-item">
              <label>系统名称：</label>
              <span>{{ currentViewData.systemName }}</span>
            </div>
            <div class="view-item">
              <label>系统编码：</label>
              <span>{{ currentViewData.systemCode }}</span>
            </div>
            <div class="view-item">
              <label>系统类型：</label>
              <el-tag :type="getSystemTypeColor(currentViewData.systemType)" size="small">
                {{ getSystemTypeText(currentViewData.systemType) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>接口地址：</label>
              <span>{{ currentViewData.apiUrl || '未配置' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="view-item">
              <label>认证方式：</label>
              <el-tag :type="getAuthTypeColor(currentViewData.authType)" size="small">
                {{ getAuthTypeText(currentViewData.authType) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>连接状态：</label>
              <el-badge :status="getConnectionStatusType(currentViewData.connectionStatus)" />
              <span>{{ getConnectionStatusText(currentViewData.connectionStatus) }}</span>
            </div>
            <div class="view-item">
              <label>最后心跳：</label>
              <span>{{ formatTime(currentViewData.lastHeartbeat) }}</span>
            </div>
            <div class="view-item">
              <label>状态：</label>
              <el-tag :type="currentViewData.status == '1' ? 'success' : 'danger'" size="small">
                {{ currentViewData.status == '1' ? '启用' : '禁用' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <div class="view-item full-width">
          <label>系统描述：</label>
          <p>{{ currentViewData.description || '暂无描述' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 表格设置对话框 -->
    <el-dialog title="表格列设置" :visible.sync="tableSettingDialogVisible" width="500px">
      <div class="table-setting-content">
        <el-checkbox-group v-model="visibleColumns">
          <div v-for="column in tableColumns" :key="column.prop" class="column-item">
            <el-checkbox :label="column.prop" :disabled="column.required">
              {{ column.label }}
            </el-checkbox>
          </div>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="tableSettingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSetting">应用</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBusinessSystemPage as getBusinessSystemList, getBusinessSystemDetail as getBusinessSystem, createBusinessSystem, updateBusinessSystem, deleteBusinessSystem, testSystemConnection, syncSystemStatus, getSystemTypes, getAuthTypes, exportBusinessSystem } from '@/api/globalTreasurer-new/basicConfig/businessSystem'
import waves from '@/directive/waves'
import { isResponseSuccess, handleResponseData, getErrorMessage, initQueryForm, resetQueryForm, handleSizeChange, handleCurrentChange, formatDate } from '../../utils'
import { PAGINATION_CONFIG } from '../../consts'
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
      totalSystems: 0,
      activeSystems: 0,
      offlineSystems: 0,
      lastCheckTime: '',
      listLoading: true,
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
      systemTypeOptions: [
        { value: 'ERP', label: 'ERP系统' },
        { value: 'CRM', label: 'CRM系统' },
        { value: 'OA', label: 'OA系统' },
        { value: 'HR', label: 'HR系统' },
        { value: 'FINANCE', label: '财务系统' },
        { value: 'OTHER', label: '其他系统' },
        { value: 'BANK', label: '银行系统' },
        { value: 'THIRD_PARTY', label: '第三方系统' }
      ],
      authTypeOptions: [
        { value: 'NONE', label: '无认证' },
        { value: 'BASIC', label: 'Basic认证' },
        { value: 'TOKEN', label: 'Token认证' },
        { value: 'OAUTH2', label: 'OAuth2认证' },
        { value: 'CERTIFICATE', label: '证书认证' },
        { value: 'API_KEY', label: 'API Key认证' },
        { value: 'JWT', label: 'JWT认证' }
      ],
      connectionStatusOptions: [
        { label: '在线', value: 'ONLINE', type: 'success' },
        { label: '离线', value: 'OFFLINE', type: 'warning' },
        { label: '异常', value: 'ERROR', type: 'danger' }
      ],
      temp: {
        id: undefined,
        systemName: '',
        systemCode: '',
        systemType: '',
        description: '',
        apiUrl: '',
        authType: '',
        authConfig: '',
        connectionStatus: 'OFFLINE',
        lastHeartbeat: null,
        status: '1'
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑业务系统',
        create: '新增业务系统',
        view: '查看业务系统详情'
      },
      rules: {
        systemName: [{ required: true, message: '系统名称是必填项', trigger: 'blur' }],
        systemCode: [{ required: true, message: '系统编码是必填项', trigger: 'blur' }],
        systemType: [{ required: true, message: '系统类型是必填项', trigger: 'change' }],
        apiUrl: [{ required: true, message: '接口地址是必填项', trigger: 'blur' }],
        authType: [{ required: true, message: '认证方式是必填项', trigger: 'change' }]
      },
      selectedRows: [],
      viewDialogVisible: false,
      currentViewData: null,
      tableSettingDialogVisible: false,
      tableColumns: [
        { prop: 'systemName', label: '系统信息', visible: true, fixed: false },
        { prop: 'systemType', label: '系统类型', visible: true, fixed: false },
        { prop: 'connectionStatus', label: '连接状态', visible: true, fixed: false },
        { prop: 'apiUrl', label: '接口地址', visible: true, fixed: false },
        { prop: 'authType', label: '认证方式', visible: true, fixed: false },
        { prop: 'lastHeartbeat', label: '最后心跳', visible: true, fixed: false },
        { prop: 'status', label: '状态', visible: true, fixed: false },
        { prop: 'actions', label: '操作', visible: true, fixed: 'right' }
      ]
    }
  },
  created() {
    this.getList()
    // 系统类型和认证方式数据将在点击"注册系统"按钮时才加载
  },
  computed: {
    visibleColumns: {
      get() {
        return this.tableColumns.filter(col => col.visible).map(col => col.prop)
      },
      set(value) {
        this.tableColumns.forEach(col => {
          col.visible = value.includes(col.prop)
        })
      }
    }
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getBusinessSystemList(this.listQuery)
        if (isResponseSuccess(response)) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
          this.updateStatistics()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('获取业务系统列表失败:', error)
        this.$message.error('获取业务系统列表失败,请检查网络连接或联系管理员')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    /**
     * 执行搜索操作
     * 支持单条件或多条件搜索,未填写的搜索条件将被忽略
     */
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    /**
     * 重置搜索条件
     * 清空所有搜索字段并重新加载数据
     */
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        systemName: undefined,
        systemType: undefined,
        connectionStatus: undefined,
        status: undefined
      }
      this.getList()
    },
    updateStatistics() {
      this.totalSystems = this.list.length
      this.activeSystems = this.list.filter(item => item.connectionStatus === 'ONLINE').length
      this.offlineSystems = this.list.filter(item => item.connectionStatus === 'OFFLINE').length
      this.lastCheckTime = this.formatTime(new Date())
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        systemName: '',
        systemCode: '',
        systemType: '',
        description: '',
        apiUrl: '',
        authType: '',
        authConfig: '',
        connectionStatus: 'OFFLINE',
        lastHeartbeat: null,
        status: '1'
      }
    },
    async handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      // 点击注册系统按钮时,查询系统类型和认证方式数据
      await this.getSystemTypes()
      await this.getAuthTypes()
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()

        // 处理认证配置:如果是对象,转换为JSON字符串
        let authConfig = this.temp.authConfig
        if (authConfig && typeof authConfig === 'object') {
          authConfig = JSON.stringify(authConfig)
        } else if (!authConfig) {
          authConfig = ''
        }

        // 构建提交数据,确保字段类型与后端一致
        const submitData = {
          systemName: this.temp.systemName,
          systemCode: this.temp.systemCode,
          systemType: this.temp.systemType,
          apiUrl: this.temp.apiUrl,
          authType: this.temp.authType,
          authConfig: authConfig, // JSON字符串
          description: this.temp.description || '',
          status: String(this.temp.status || '1') // 转换为字符串
        }

        const response = await createBusinessSystem(submitData)
        if (isResponseSuccess(response)) {
          this.$message.success('创建成功')
          this.dialogFormVisible = false
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error.message) {
          console.error('创建业务系统失败:', error)
        }
      }
    },
    async handleUpdate(row) {
      try {
        // 深拷贝行数据,避免直接修改列表数据
        this.temp = Object.assign({}, row)

        // 处理认证配置:如果是JSON字符串,解析为对象
        if (this.temp.authConfig && typeof this.temp.authConfig === 'string') {
          try {
            this.temp.authConfig = JSON.parse(this.temp.authConfig)
          } catch (e) {
            console.warn('解析authConfig失败,使用原始值', e)
            this.temp.authConfig = {}
          }
        } else if (!this.temp.authConfig) {
          this.temp.authConfig = {}
        }

        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      } catch (error) {
        console.error('打开编辑对话框失败:', error)
        this.$message.error('打开编辑对话框失败')
      }
    },
    async updateData() {
      try {
        await this.$refs['dataForm'].validate()

        // 处理认证配置:如果是对象,转换为JSON字符串
        let authConfig = this.temp.authConfig
        if (authConfig && typeof authConfig === 'object') {
          authConfig = JSON.stringify(authConfig)
        } else if (!authConfig) {
          authConfig = ''
        }

        // 构建提交数据,确保字段类型与后端一致
        const submitData = {
          id: this.temp.id,
          systemName: this.temp.systemName,
          systemCode: this.temp.systemCode,
          systemType: this.temp.systemType,
          apiUrl: this.temp.apiUrl,
          authType: this.temp.authType,
          authConfig: authConfig, // JSON字符串
          description: this.temp.description || '',
          status: String(this.temp.status || '1') // 转换为字符串
        }

        const response = await updateBusinessSystem(submitData)
        if (isResponseSuccess(response)) {
          this.$message.success('更新成功')
          this.dialogFormVisible = false
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error.message) {
          console.error('更新业务系统失败:', error)
        }
      }
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteBusinessSystem(row.id)
        if (isResponseSuccess(response)) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除业务系统失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    // 新增方法
    async getSystemTypes() {
      try {
        const response = await getSystemTypes()
        console.log('系统类型响应:', response)
        if (isResponseSuccess(response)) {
          // 下拉框数据直接使用response.data,不需要使用handleResponseData处理
          const data = response.data || []
          this.$set(this, 'systemTypeOptions', data)
          console.log('系统类型选项已赋值:', this.systemTypeOptions)
          console.log('systemTypeOptions length:', this.systemTypeOptions.length)
        } else {
          console.warn('系统类型响应失败:', response)
        }
      } catch (error) {
        console.error('获取系统类型失败:', error)
        this.$message.warning('获取系统类型失败,将无法筛选系统类型')
        this.$set(this, 'systemTypeOptions', [])
      }
    },
    async getAuthTypes() {
      try {
        const response = await getAuthTypes()
        console.log('认证方式响应:', response)
        if (isResponseSuccess(response)) {
          // 下拉框数据直接使用response.data,不需要使用handleResponseData处理
          const data = response.data || []
          this.$set(this, 'authTypeOptions', data)
          console.log('认证方式选项已赋值:', this.authTypeOptions)
          console.log('authTypeOptions length:', this.authTypeOptions.length)
        } else {
          console.warn('认证方式响应失败:', response)
        }
      } catch (error) {
        console.error('获取认证方式失败:', error)
        this.$message.warning('获取认证方式失败,将无法筛选认证方式')
        this.$set(this, 'authTypeOptions', [])
      }
    },
    async handleSync() {
      try {
        this.listLoading = true
        const response = await syncSystemStatus()
        if (isResponseSuccess(response)) {
          this.$message.success('同步状态成功')
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('同步系统状态失败:', error)
        this.$message.error('同步状态失败')
      } finally {
        this.listLoading = false
      }
    },
    async handleExport() {
      try {
        const response = await exportBusinessSystem(this.listQuery)
        // 当 responseType 为 'blob' 时，request.js 拦截器会直接返回 data (blob 数据本身)
        if (response) {
          const blob = response instanceof Blob ? response : new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = `业务系统数据_${this.formatTime(new Date())}.xlsx`
          link.click()
          window.URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败：未收到响应数据')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    async handleTest(row) {
      if (!row || !row.id) {
        this.$message.error('无法测试:系统信息不完整')
        return
      }
      try {
        this.$message.info('正在测试连接...')
        const response = await testSystemConnection({ id: row.id })
        if (isResponseSuccess(response)) {
          this.$message.success('连接测试成功')
          // 测试成功后刷新列表,获取最新的连接状态
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response) || '连接测试失败')
          // 测试失败也刷新列表,更新连接状态
          this.getList()
        }
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message.error('连接测试失败')
        // 发生异常也刷新列表
        this.getList()
      }
    },
    async handleStatusChange(row) {
      try {
        const response = await updateBusinessSystem({
          id: row.id,
          status: row.status
        })
        if (isResponseSuccess(response)) {
          this.$message.success('状态更新成功')
        } else {
          row.status = row.status === '1' ? '0' : '1' // 恢复原状态
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        row.status = row.status === '1' ? '0' : '1' // 恢复原状态
        this.$message.error('状态更新失败')
      }
    },
    handleView(row) {
      this.currentViewData = row
      this.viewDialogVisible = true
    },
    handleSelectionChange(val) {
      this.selectedRows = val
    },
    handleTableSetting() {
      this.tableSettingDialogVisible = true
    },
    isColumnVisible(prop) {
      const column = this.tableColumns.find(col => col.prop === prop)
      return column ? column.visible : false
    },
    formatTime(time) {
      if (!time) return ''
      return formatDate(time, 'yyyy-MM-dd HH:mm:ss')
    },
    getSystemIcon(systemType) {
      const iconMap = {
        'FINANCE': 'el-icon-coin',
        'BANK': 'el-icon-bank-card',
        'ERP': 'el-icon-office-building',
        'CRM': 'el-icon-user',
        'OA': 'el-icon-document',
        'HR': 'el-icon-user',
        'OTHER': 'el-icon-setting',
        'THIRD_PARTY': 'el-icon-connection'
      }
      return iconMap[systemType] || 'el-icon-monitor'
    },
    getSystemTypeText(systemType) {
      const option = this.systemTypeOptions.find(opt => opt.value === systemType)
      return option ? option.label : systemType
    },
    getSystemTypeColor(systemType) {
      const colorMap = {
        'FINANCE': 'primary',
        'BANK': 'success',
        'ERP': 'warning',
        'CRM': 'info',
        'OA': 'info',
        'HR': 'primary',
        'OTHER': '',
        'THIRD_PARTY': ''
      }
      return colorMap[systemType] || ''
    },
    getConnectionStatusText(status) {
      const option = this.connectionStatusOptions.find(opt => opt.value === status)
      return option ? option.label : status
    },
    getConnectionStatusType(status) {
      const option = this.connectionStatusOptions.find(opt => opt.value === status)
      return option ? option.type : 'info'
    },
    getAuthTypeText(authType) {
      const option = this.authTypeOptions.find(opt => opt.value === authType)
      return option ? option.label : authType
    },
    getAuthTypeColor(authType) {
      const colorMap = {
        'OAUTH2': 'primary',
        'API_KEY': 'success',
        'BASIC_AUTH': 'warning',
        'JWT': 'info'
      }
      return colorMap[authType] || ''
    },
    tableRowClassName({ row }) {
      if (row.connectionStatus === 'ERROR') {
        return 'error-row'
      }
      return ''
    },
    applyTableSetting() {
      this.tableKey = Date.now() // 强制重新渲染表格
      this.tableSettingDialogVisible = false
      this.$message.success('表格设置已应用')
    }
  }
}
</script>

<style lang="scss" scoped>
.business-system-register {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 50px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: white;
      padding: 20px 24px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 500;
          color: #1f2937;
          display: flex;
          align-items: center;

          i {
            margin-right: 8px;
            color: #3b82f6;
          }
        }

        .page-description {
          margin: 0;
          color: #6b7280;
          font-size: 14px;
        }
      }
    }
  }

  .system-overview {
    margin-bottom: 20px;

    .overview-card {
      .card-content {
        display: flex;
        align-items: center;

        .card-icon {
          width: 50px;
          height: 50px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 24px;
            color: white;
          }

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.active-icon {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
          }

          &.offline-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.update-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;

            &.positive {
              color: #10b981;
            }

            &.negative {
              color: #ef4444;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;

    .table-title {
      display: flex;
      align-items: center;

      .title-text {
        font-size: 16px;
        font-weight: 500;
        color: #1f2937;
        margin-right: 8px;
      }

      .title-count {
        color: #6b7280;
        font-size: 14px;
      }
    }
  }

  .system-info {
    display: flex;
    align-items: center;

    .system-icon {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: #f3f4f6;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 8px;

      i {
        color: #3b82f6;
        font-size: 16px;
      }
    }

    .system-details {
      .system-name {
        font-weight: 500;
        color: #1f2937;
        margin-bottom: 2px;
      }

      .system-code {
        font-size: 12px;
        color: #6b7280;
      }
    }
  }

  .connection-status {
    display: flex;
    align-items: center;

    .status-text {
      margin-left: 4px;
      font-size: 12px;

      &.online {
        color: #10b981;
      }

      &.offline {
        color: #f59e0b;
      }

      &.error {
        color: #ef4444;
      }
    }
  }

  .api-url {
    display: flex;
    align-items: center;

    i {
      margin-right: 4px;
      color: #3b82f6;
    }

    span {
      font-size: 12px;
      color: #6b7280;
    }
  }

  .heartbeat-time {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #6b7280;

    i {
      margin-right: 4px;
      color: #9ca3af;
    }
  }

  .pagination-wrapper {
    padding: 16px 0;
    text-align: center;
  }

  .view-content {
    .view-item {
      margin-bottom: 16px;

      label {
        font-weight: 500;
        color: #1f2937;
        margin-right: 8px;
        display: inline-block;
        min-width: 80px;
      }

      span {
        color: #6b7280;
      }

      &.full-width {
        width: 100%;

        p {
          margin: 8px 0 0 0;
          color: #6b7280;
          line-height: 1.5;
        }
      }
    }
  }

  ::v-deep .error-row {
    background-color: #fef2f2;
  }

  .table-setting-content {
    max-height: 400px;
    overflow-y: auto;

    .column-item {
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }
    }
  }
}

.el-form-item {
  margin-bottom: 18px;
}

.dialog-footer {
  text-align: right;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}
</style>
