<template>
  <div class="third-party-account-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user"></i>
            第三方账户管理
          </h2>
          <p class="page-description">管理第三方系统账户信息，包括账户认证、权限配置和状态监控</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增账户
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSync">
            同步状态
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="账户编码">
            <el-input
              v-model="listQuery.accountCode"
              placeholder="请输入账户编码"
              style="width: 150px;"
              clearable
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="账户名称">
            <el-input
              v-model="listQuery.accountName"
              placeholder="请输入账户名称"
              style="width: 200px;"
              clearable
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="第三方系统">
            <el-select
              v-model="listQuery.thirdPartySystem"
              placeholder="请选择第三方系统"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="item in thirdPartySystems"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="账户类型">
            <el-select
              v-model="listQuery.accountType"
              placeholder="请选择账户类型"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="item in accountTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.connectionStatus"
              placeholder="请选择连接状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="已连接" value="CONNECTED" />
              <el-option label="未连接" value="DISCONNECTED" />
              <el-option label="连接异常" value="ERROR" />
              <el-option label="维护中" value="MAINTENANCE" />
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
          <span class="title-text">账户列表</span>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="isColumnVisible('accountCode')" label="账户编码" prop="accountCode" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <el-tag size="small" type="info">{{ row.accountCode }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('accountName')" label="账户名称" prop="accountName" align="center" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="account-name">
              <i :class="getAccountIcon(row.thirdPartySystem)"></i>
              <span>{{ row.accountName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('thirdPartySystem')" label="第三方系统" prop="thirdPartySystem" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getThirdPartySystemColor(row.thirdPartySystem)" size="small">
              {{ getThirdPartySystemName(row.thirdPartySystem) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('accountType')" label="账户类型" prop="accountType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getAccountTypeColor(row.accountType)" size="small">
              {{ getAccountTypeName(row.accountType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('accountIdentifier')" label="账户标识" prop="accountIdentifier" min-width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span class="account-identifier">{{ row.accountIdentifier }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('connectionStatus')" label="连接状态" prop="connectionStatus" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getConnectionStatusColor(row.connectionStatus)" size="small">
              {{ getConnectionStatusName(row.connectionStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('lastSyncTime')" label="最后同步" prop="lastSyncTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="sync-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.lastSyncTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="280" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-connection" @click="handleTestConnection(row)">
                测试连接
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-refresh" @click="handleSingleSync(row)">
                同步
              </el-button>
              <el-button
                v-if="row.status!='deleted'"
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

    <!-- 表格设置对话框 -->
    <el-dialog
      title="表格设置"
      :visible.sync="dialogTableSettingVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="table-setting-content">
        <el-form label-width="120px" size="small">
          <el-form-item label="显示列设置">
            <el-checkbox-group v-model="visibleColumns">
              <el-checkbox
                v-for="column in allColumns"
                :key="column.prop"
                :label="column.prop"
              >
                {{ column.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="每页显示条数">
            <el-select v-model="customPageSize" placeholder="选择每页显示条数" style="width: 120px;">
              <el-option label="10条" :value="10" />
              <el-option label="20条" :value="20" />
              <el-option label="50条" :value="50" />
              <el-option label="100条" :value="100" />
            </el-select>
          </el-form-item>
          <el-form-item label="表格高度">
            <el-radio-group v-model="tableHeight">
              <el-radio label="auto">自适应</el-radio>
              <el-radio label="400">固定400px</el-radio>
              <el-radio label="600">固定600px</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSettings">确定</el-button>
      </div>
    </el-dialog>

    <!-- 账户配置对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增账户' : '编辑账户'"
      :visible.sync="dialogFormVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 600px; margin-left:50px;"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户编码" prop="accountCode">
              <el-input v-model="temp.accountCode" placeholder="请输入账户编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户名称" prop="accountName">
              <el-input v-model="temp.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="第三方系统" prop="thirdPartySystem">
              <el-select v-model="temp.thirdPartySystem" placeholder="请选择第三方系统" style="width: 100%;">
                <el-option
                  v-for="item in thirdPartySystems"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="temp.accountType" placeholder="请选择账户类型" style="width: 100%;">
                <el-option
                  v-for="item in accountTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="账户标识" prop="accountIdentifier">
          <el-input v-model="temp.accountIdentifier" placeholder="请输入账户标识（如API Key、App ID等）" />
        </el-form-item>

        <el-form-item label="账户密钥" prop="accountSecret">
          <el-input
            v-model="temp.accountSecret"
            type="password"
            placeholder="请输入账户密钥"
            show-password
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="接口地址" prop="apiUrl">
              <el-input v-model="temp.apiUrl" placeholder="请输入API接口地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步频率" prop="syncFrequency">
              <el-select v-model="temp.syncFrequency" placeholder="请选择同步频率" style="width: 100%;">
                <el-option label="实时同步" value="REAL_TIME" />
                <el-option label="每小时" value="HOURLY" />
                <el-option label="每天" value="DAILY" />
                <el-option label="每周" value="WEEKLY" />
                <el-option label="手动同步" value="MANUAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="账户描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入账户描述"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-switch
            v-model="temp.isEnabled"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getThirdPartyAccountList,
  createThirdPartyAccount,
  updateThirdPartyAccount,
  deleteThirdPartyAccount,
  syncThirdPartyAccountStatus,
  batchSyncThirdPartyAccountStatus,
  testThirdPartyAccountConnection,
  exportThirdPartyAccount,
  getThirdPartySystems,
  getAccountTypes
} from '@/api/globalTreasurer/treasuryCommon'
import { isResponseSuccess, handleResponseData, getErrorMessage } from '../../utils'

export default {
  name: 'ThirdPartyAccountManage',
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
      multipleSelection: [],
      dialogFormVisible: false,
      dialogTableSettingVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        accountCode: '',
        accountName: '',
        thirdPartySystem: '',
        accountType: '',
        accountIdentifier: '',
        accountSecret: '',
        apiUrl: '',
        syncFrequency: '',
        description: '',
        isEnabled: 1
      },
      rules: {
        accountCode: [{ required: true, message: '账户编码不能为空', trigger: 'blur' }],
        accountName: [{ required: true, message: '账户名称不能为空', trigger: 'blur' }],
        thirdPartySystem: [{ required: true, message: '第三方系统不能为空', trigger: 'change' }],
        accountType: [{ required: true, message: '账户类型不能为空', trigger: 'change' }],
        accountIdentifier: [{ required: true, message: '账户标识不能为空', trigger: 'blur' }],
        accountSecret: [{ required: true, message: '账户密钥不能为空', trigger: 'blur' }]
      },
      thirdPartySystems: [
        { label: '银企直连系统', value: 'BANK_DIRECT' },
        { label: '支付宝', value: 'ALIPAY' },
        { label: '微信支付', value: 'WECHAT_PAY' },
        { label: '网银系统', value: 'ONLINE_BANKING' },
        { label: '第三方支付', value: 'THIRD_PARTY_PAY' },
        { label: 'ERP系统', value: 'ERP_SYSTEM' }
      ],
      accountTypes: [
        { label: '银行账户', value: 'BANK_ACCOUNT' },
        { label: '支付账户', value: 'PAYMENT_ACCOUNT' },
        { label: 'API接口账户', value: 'API_ACCOUNT' },
        { label: '系统集成账户', value: 'SYSTEM_ACCOUNT' },
        { label: '虚拟账户', value: 'VIRTUAL_ACCOUNT' }
      ],
      listQuery: {
        page: 1,
        limit: 20,
        accountCode: undefined,
        accountName: undefined,
        thirdPartySystem: undefined,
        accountType: undefined,
        connectionStatus: undefined
      },
      // 表格设置相关
      allColumns: [
        { prop: 'accountCode', label: '账户编码' },
        { prop: 'accountName', label: '账户名称' },
        { prop: 'thirdPartySystem', label: '第三方系统' },
        { prop: 'accountType', label: '账户类型' },
        { prop: 'accountIdentifier', label: '账户标识' },
        { prop: 'connectionStatus', label: '连接状态' },
        { prop: 'lastSyncTime', label: '最后同步' }
      ],
      visibleColumns: ['accountCode', 'accountName', 'thirdPartySystem', 'accountType', 'accountIdentifier', 'connectionStatus', 'lastSyncTime'],
      customPageSize: 10,
      tableHeight: 'auto'
    }
  },
  created() {
    this.loadTableSettings()
    this.getList()
    this.loadDropdownData()
  },
  methods: {
    /**
     * 加载下拉框数据
     */
    async loadDropdownData() {
      try {
        // 这里可以调用API获取下拉框数据
        // const systemsResponse = await getThirdPartySystems()
        // const typesResponse = await getAccountTypes()
      } catch (error) {
        console.warn('加载下拉框数据失败，使用默认数据:', error)
      }
    },

    /**
     * 获取数据列表
     */
    async getList() {
      this.listLoading = true
      try {
        const response = await getThirdPartyAccountList(this.listQuery)

        if (isResponseSuccess(response)) {
          const data = response.data || {}
          const rawList = data.tlist || data.list || []
          // 数据规范化
          const normalizedList = rawList.map(item => {
            if (!item.connectionStatus && item.connection_status) {
              item.connectionStatus = item.connection_status
            }
            if (!item.connectionStatus && item.CONNECTION_STATUS) {
              item.connectionStatus = item.CONNECTION_STATUS
            }
            if (item.connectionStatus !== undefined && item.connectionStatus !== null) {
              item.connectionStatus = this.normalizeConnectionStatus(item.connectionStatus)
            }
            return item
          })

          // 前端二次过滤（兼容后端未部署筛选逻辑的情况）
          const filtered = this.applyFrontendFilter(normalizedList)
          this.total = filtered.length
          // 前端分页
          const page = this.listQuery.page || 1
          const limit = this.listQuery.limit || 20
          const start = (page - 1) * limit
          this.list = filtered.slice(start, start + limit)
        } else {
          this.$message.error(getErrorMessage(response))
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取第三方账户列表失败:', error)
        this.$message.error('获取数据失败，请检查网络连接')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 前端过滤（后端未过滤时的兜底逻辑）
     */
    applyFrontendFilter(list) {
      const q = this.listQuery
      return list.filter(item => {
        if (q.accountCode && !(item.accountCode || '').includes(q.accountCode)) return false
        if (q.accountName && !(item.accountName || '').includes(q.accountName)) return false
        if (q.thirdPartySystem && item.thirdPartySystem !== q.thirdPartySystem) return false
        if (q.accountType && item.accountType !== q.accountType) return false
        if (q.connectionStatus && item.connectionStatus !== q.connectionStatus) return false
        return true
      })
    },

    /**
     * 搜索过滤
     */
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    /**
     * 重置搜索
     */
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        accountCode: undefined,
        accountName: undefined,
        thirdPartySystem: undefined,
        accountType: undefined,
        connectionStatus: undefined
      }
      this.getList()
    },

    /**
     * 多选变化
     */
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    /**
     * 表格设置
     */
    handleTableSetting() {
      this.dialogTableSettingVisible = true
    },

    /**
     * 应用表格设置
     */
    applyTableSettings() {
      // 应用每页显示条数
      this.listQuery.limit = this.customPageSize
      this.listQuery.page = 1

      // 刷新表格
      this.getList()

      // 保存设置到localStorage
      const settings = {
        visibleColumns: this.visibleColumns,
        customPageSize: this.customPageSize,
        tableHeight: this.tableHeight
      }
      localStorage.setItem('thirdPartyAccount_tableSettings', JSON.stringify(settings))

      this.dialogTableSettingVisible = false
      this.$message.success('表格设置已保存')
    },

    /**
     * 加载表格设置
     */
    loadTableSettings() {
      try {
        const settings = localStorage.getItem('thirdPartyAccount_tableSettings')
        if (settings) {
          const parsed = JSON.parse(settings)
          this.visibleColumns = parsed.visibleColumns || this.visibleColumns
          this.customPageSize = parsed.customPageSize || this.customPageSize
          this.tableHeight = parsed.tableHeight || this.tableHeight

          if (parsed.customPageSize) {
            this.listQuery.limit = parsed.customPageSize
          }
        }
      } catch (error) {
        console.warn('加载表格设置失败:', error)
      }
    },

    /**
     * 判断列是否显示
     */
    isColumnVisible(prop) {
      return this.visibleColumns.includes(prop)
    },

    /**
     * 创建新记录
     */
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 编辑记录
     */
    handleUpdate(row) {
      // 调试: 打印行数据，检查ID字段
      console.log('handleUpdate - row:', row)
      console.log('handleUpdate - row.id:', row.id)

      // 检查ID是否存在
      if (!row.id) {
        this.$message.error('无法获取记录ID，请刷新页面后重试')
        console.error('记录对象缺少ID字段:', row)
        return
      }

      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 重置临时对象
     */
    resetTemp() {
      this.temp = {
        id: undefined,
        accountCode: '',
        accountName: '',
        thirdPartySystem: '',
        accountType: '',
        accountIdentifier: '',
        accountSecret: '',
        apiUrl: '',
        syncFrequency: '',
        description: '',
        isEnabled: 1
      }
    },

    /**
     * 创建数据
     */
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await createThirdPartyAccount(this.temp)
            if (isResponseSuccess(response)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response.msg || response.message || '创建失败')
            }
          } catch (error) {
            this.$message.error('创建失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 更新数据
     */
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            // 调试: 打印更新数据
            console.log('updateData - temp:', this.temp)
            console.log('updateData - temp.id:', this.temp.id)

            // 检查ID是否存在
            if (!this.temp.id) {
              this.$message.error('记录ID丢失，无法更新。请重新选择记录编辑。')
              console.error('ID字段丢失! 完整的temp对象:', JSON.stringify(this.temp, null, 2))
              this.dialogFormVisible = false
              return
            }

            const response = await updateThirdPartyAccount(this.temp)
            if (isResponseSuccess(response)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(getErrorMessage(response))
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 删除记录
     */
    async handleDelete(row, index) {
      // 调试: 打印行数据
      console.log('handleDelete - row:', row)
      console.log('handleDelete - row.id:', row.id)

      // 检查ID是否存在
      if (!row.id) {
        this.$message.error('无法获取记录ID，删除失败')
        console.error('记录对象缺少ID字段:', row)
        return
      }

      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteThirdPartyAccount({ id: row.id })
        if (isResponseSuccess(response)) {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请检查网络连接')
        }
      }
    },

    /**
     * 同步状态
     */
    async handleSync() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请先选择要同步的账户')
        return
      }
      try {
        this.listLoading = true
        const ids = this.multipleSelection.map(item => item.id).join(',')
        const response = await batchSyncThirdPartyAccountStatus({ ids })
        if (isResponseSuccess(response)) {
          this.$message.success('批量同步成功')
          // 更新选中行的 lastSyncTime
          const now = new Date().toISOString()
          const selectedIds = this.multipleSelection.map(item => String(item.id))
          this.list.forEach(item => {
            if (selectedIds.includes(String(item.id))) {
              this.$set(item, 'lastSyncTime', now)
            }
          })
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 单个账户同步
     */
    async handleSingleSync(row) {
      try {
        const response = await syncThirdPartyAccountStatus(row.id)
        if (isResponseSuccess(response)) {
          this.$message.success('同步成功')
          // 更新当前行的 lastSyncTime
          this.$set(row, 'lastSyncTime', new Date().toISOString())
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败，请检查网络连接')
      }
    },

    /**
     * 测试连接
     */
    async handleTestConnection(row) {
      try {
        const response = await testThirdPartyAccountConnection(row.id)
        if (isResponseSuccess(response)) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.msg || response.message || '连接测试失败')
        }
      } catch (error) {
        this.$message.error('连接测试失败，请检查网络连接')
      }
    },

    /**
     * 导出数据
     */
    async handleExport() {
      try {
        this.listLoading = true
        const response = await exportThirdPartyAccount(this.listQuery)

        if (response) {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `第三方账户管理_${new Date().toLocaleDateString()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败，请稍后重试')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 状态变化
     */
    async handleStatusChange(row) {
      try {
        const response = await updateThirdPartyAccount({
          id: row.id,
          isEnabled: row.isEnabled
        })
        if (isResponseSuccess(response)) {
          this.$message.success('状态更新成功')
        } else {
          // 回滚状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
          this.$message.error(response.msg || response.message || '状态更新失败')
        }
      } catch (error) {
        // 回滚状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
        this.$message.error('状态更新失败，请检查网络连接')
      }
    },

    /**
     * 格式化时间
     */
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },

    /**
     * 获取账户图标
     */
    getAccountIcon(system) {
      const iconMap = {
        'BANK_DIRECT': 'el-icon-bank-card',
        'ALIPAY': 'el-icon-wallet',
        'WECHAT_PAY': 'el-icon-chat-dot-square',
        'ONLINE_BANKING': 'el-icon-monitor',
        'THIRD_PARTY_PAY': 'el-icon-credit-card',
        'ERP_SYSTEM': 'el-icon-office-building'
      }
      return iconMap[system] || 'el-icon-user'
    },

    // 第三方系统名称映射
    getThirdPartySystemName(system) {
      const systemMap = {
        'BANK_DIRECT': '银企直连系统',
        'ALIPAY': '支付宝',
        'WECHAT_PAY': '微信支付',
        'ONLINE_BANKING': '网银系统',
        'THIRD_PARTY_PAY': '第三方支付',
        'ERP_SYSTEM': 'ERP系统'
      }
      return systemMap[system] || system
    },

    // 第三方系统颜色映射
    getThirdPartySystemColor(system) {
      const colorMap = {
        'BANK_DIRECT': 'primary',
        'ALIPAY': 'success',
        'WECHAT_PAY': 'success',
        'ONLINE_BANKING': 'warning',
        'THIRD_PARTY_PAY': 'info',
        'ERP_SYSTEM': 'danger'
      }
      return colorMap[system] || 'info'
    },

    // 账户类型名称映射
    getAccountTypeName(type) {
      const typeMap = {
        'BANK_ACCOUNT': '银行账户',
        'PAYMENT_ACCOUNT': '支付账户',
        'API_ACCOUNT': 'API接口账户',
        'SYSTEM_ACCOUNT': '系统集成账户',
        'VIRTUAL_ACCOUNT': '虚拟账户'
      }
      return typeMap[type] || type
    },

    // 账户类型颜色映射
    getAccountTypeColor(type) {
      const colorMap = {
        'BANK_ACCOUNT': 'primary',
        'PAYMENT_ACCOUNT': 'success',
        'API_ACCOUNT': 'warning',
        'SYSTEM_ACCOUNT': 'info',
        'VIRTUAL_ACCOUNT': 'danger'
      }
      return colorMap[type] || 'info'
    },

    /**
     * 规范化连接状态值，将各种可能的后端返回值统一为标准枚举
     */
    normalizeConnectionStatus(status) {
      if (status === null || status === undefined || status === '') return null
      const val = String(status).toUpperCase().trim()
      // 数字状态码映射
      const numericMap = {
        '1': 'CONNECTED', '2': 'DISCONNECTED', '3': 'ERROR', '4': 'MAINTENANCE',
        '0': 'DISCONNECTED'
      }
      if (numericMap[val]) return numericMap[val]
      // 中文映射
      const chineseMap = {
        '已连接': 'CONNECTED', '未连接': 'DISCONNECTED', '连接异常': 'ERROR', '维护中': 'MAINTENANCE',
        '正常': 'CONNECTED', '异常': 'ERROR', '断开': 'DISCONNECTED'
      }
      if (chineseMap[String(status).trim()]) return chineseMap[String(status).trim()]
      // 其他英文别名映射
      const aliasMap = {
        'CONNECTED': 'CONNECTED', 'DISCONNECTED': 'DISCONNECTED', 'ERROR': 'ERROR', 'MAINTENANCE': 'MAINTENANCE',
        'ACTIVE': 'CONNECTED', 'INACTIVE': 'DISCONNECTED', 'NORMAL': 'CONNECTED',
        'ONLINE': 'CONNECTED', 'OFFLINE': 'DISCONNECTED',
        'OK': 'CONNECTED', 'FAIL': 'ERROR', 'FAILED': 'ERROR',
        'SUCCESS': 'CONNECTED', 'TIMEOUT': 'ERROR',
        'ENABLED': 'CONNECTED', 'DISABLED': 'DISCONNECTED',
        'UP': 'CONNECTED', 'DOWN': 'DISCONNECTED'
      }
      return aliasMap[val] || val
    },

    // 连接状态名称映射
    getConnectionStatusName(status) {
      if (!status) return '未连接'
      const statusMap = {
        'CONNECTED': '已连接',
        'DISCONNECTED': '未连接',
        'ERROR': '连接异常',
        'MAINTENANCE': '维护中'
      }
      const key = String(status).toUpperCase().trim()
      return statusMap[key] || String(status)
    },

    // 连接状态颜色映射
    getConnectionStatusColor(status) {
      if (!status) return 'info'
      const colorMap = {
        'CONNECTED': 'success',
        'DISCONNECTED': 'info',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      const key = String(status).toUpperCase().trim()
      return colorMap[key] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.third-party-account-manage {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .search-form {
      .demo-form-inline {
        .el-form-item {
          margin-bottom: 0;
        }
      }
    }
  }

  .table-card {
    border-radius: 8px;

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .account-name {
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .account-identifier {
      font-family: monospace;
      color: #606266;
    }

    .sync-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;

      i {
        margin-right: 4px;
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-dialog__body {
  padding: 20px 20px 10px 20px;
}

// 表格设置对话框样式
.table-setting-content {
  .el-form-item {
    margin-bottom: 15px;
  }

  .el-checkbox-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .el-checkbox {
    margin-right: 0;
  }
}
</style>
