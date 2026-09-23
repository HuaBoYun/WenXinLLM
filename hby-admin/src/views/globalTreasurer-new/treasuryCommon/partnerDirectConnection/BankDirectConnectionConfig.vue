<template>
  <div class="bank-direct-connection-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-link"></i>
            银行直连配置
          </h2>
          <p class="page-description">管理银行直连接口配置，包括连接参数、认证信息和通信协议设置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增配置
          </el-button>
          <el-button type="success" icon="el-icon-connection" @click="handleTestAll">
            批量测试
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 连接状态概览 -->
    <div class="connection-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-link"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总连接数</div>
                <div class="card-value">{{ totalConnections }}</div>
                <div class="card-change">已配置连接</div>
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
                <div class="card-title">正常连接</div>
                <div class="card-value">{{ activeConnections }}</div>
                <div class="card-change positive">运行正常</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon error-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">异常连接</div>
                <div class="card-value">{{ errorConnections }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon check-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后检查</div>
                <div class="card-value">{{ lastCheckTime }}</div>
                <div class="card-change">连接状态</div>
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
          <el-form-item label="银行账号">
            <el-input
              v-model="listQuery.accountId"
              placeholder="请输入银行账号"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="连接类型">
            <el-select
              v-model="listQuery.connectionType"
              placeholder="请选择连接类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="EBPP协议" value="EBPP" />
              <el-option label="SWIFT协议" value="SWIFT" />
              <el-option label="专有协议" value="PROPRIETARY" />
              <el-option label="HTTP接口" value="HTTP" />
              <el-option label="WebService" value="WEBSERVICE" />
              <el-option label="FTP传输" value="FTP" />
              <el-option label="专线连接" value="DEDICATED" />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.connectionStatus"
              placeholder="请选择连接状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="异常" value="ERROR" />
              <el-option label="维护中" value="MAINTENANCE" />
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
          <span class="title-text">银行直连配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
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
        
        <el-table-column label="银行信息" prop="bankName" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="bank-info">
              <div class="bank-logo">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="bank-details">
                <div class="bank-name">{{ getBankName(row) }}</div>
                <div class="bank-code">账号: {{ row.accountId }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="连接类型" prop="connectionType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getConnectionTypeColor(row.connectionType)" size="small">
              <i :class="getConnectionTypeIcon(row.connectionType)"></i>
              {{ getConnectionTypeText(row.connectionType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="连接地址" prop="interfaceUrl" min-width="250" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="connection-url">
              <i class="el-icon-link"></i>
              <span>{{ row.interfaceUrl || '未配置' }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="连接状态" prop="connectionStatus" align="center" width="120">
          <template slot-scope="{row}">
            <div class="connection-status">
              <el-badge :status="getConnectionStatusType(row.connectionStatus)" />
              <span :class="'status-text ' + row.connectionStatus.toLowerCase()">
                {{ getConnectionStatusText(row.connectionStatus) }}
              </span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="最后测试" prop="lastTestTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="test-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.lastTestTime) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="状态" class-name="status-col" width="80" align="center">
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
              <el-button type="success" size="mini" icon="el-icon-connection" @click="handleTest(row)">
                测试
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                详情
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
        <el-pagination
          background
          :current-page="listQuery.pageNum"
          :layout="layout"
          :page-size="listQuery.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑配置对话框 -->
    <el-dialog
      :title="dialogType === 'create' ? '新增银行直连配置' : '编辑银行直连配置'"
      :visible.sync="dialogFormVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        v-if="dialogFormVisible"
        ref="configForm"
        :model="currentConfig"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="银行账号" prop="accountId">
          <el-input v-model="currentConfig.accountId" placeholder="请输入银行账号" />
        </el-form-item>
        <el-form-item label="连接类型" prop="connectionType">
          <el-select v-model="currentConfig.connectionType" placeholder="请选择连接类型" style="width: 100%">
            <el-option label="EBPP协议" value="EBPP" />
            <el-option label="SWIFT协议" value="SWIFT" />
            <el-option label="专有协议" value="PROPRIETARY" />
            <el-option label="HTTP接口" value="HTTP" />
            <el-option label="WebService" value="WEBSERVICE" />
            <el-option label="FTP传输" value="FTP" />
            <el-option label="专线连接" value="DEDICATED" />
          </el-select>
        </el-form-item>
        <el-form-item label="连接地址" prop="interfaceUrl">
          <el-input v-model="currentConfig.interfaceUrl" placeholder="请输入连接地址" />
        </el-form-item>
        <el-form-item label="接口版本">
          <el-input v-model="currentConfig.interfaceVersion" placeholder="请输入接口版本" />
        </el-form-item>
        <el-form-item label="超时时间(秒)">
          <el-input-number v-model="currentConfig.timeoutSeconds" :min="1" :max="300" placeholder="超时时间" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="currentConfig.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="currentConfig.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看配置详情对话框 -->
    <el-dialog
      title="配置详情"
      :visible.sync="dialogViewVisible"
      width="600px"
    >
      <div v-if="currentConfig" class="config-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="配置ID">{{ currentConfig.id }}</el-descriptions-item>
          <el-descriptions-item label="银行名称">{{ getBankName(currentConfig) }}</el-descriptions-item>
          <el-descriptions-item label="银行账号">{{ currentConfig.accountId }}</el-descriptions-item>
          <el-descriptions-item label="连接类型">
            <el-tag :type="getConnectionTypeColor(currentConfig.connectionType)" size="small">
              {{ getConnectionTypeText(currentConfig.connectionType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="连接地址" :span="2">{{ currentConfig.interfaceUrl || '-' }}</el-descriptions-item>
          <el-descriptions-item label="接口版本">{{ currentConfig.interfaceVersion || '-' }}</el-descriptions-item>
          <el-descriptions-item label="超时时间">{{ currentConfig.timeoutSeconds || 30 }}秒</el-descriptions-item>
          <el-descriptions-item label="连接状态">
            <el-badge :status="getConnectionStatusType(currentConfig.connectionStatus)" />
            {{ getConnectionStatusText(currentConfig.connectionStatus) }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentConfig.status === 1 ? 'success' : 'danger'">
              {{ currentConfig.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最后测试时间">{{ formatTime(currentConfig.lastTestTime) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(currentConfig.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentConfig.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleTest(currentConfig)">测试连接</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getBankDirectConnectionConfigPage,
  getBankDirectConnectionConfigDetail,
  addBankDirectConnectionConfig,
  updateBankDirectConnectionConfig,
  deleteBankDirectConnectionConfig,
  testBankDirectConnectionConfig,
  batchTestBankDirectConnectionConfig,
  getBankDirectConnectionConfigStatistics,
  exportBankDirectConnectionConfig
} from '@/api/globalTreasurer-new/partnerDirectConnection/bank'

export default {
  name: 'BankDirectConnectionConfig',
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
      layout: 'total, sizes, prev, pager, next, jumper',
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        accountId: undefined,
        connectionType: undefined,
        connectionStatus: undefined,
        status: undefined
      },
      multipleSelection: [],
      totalConnections: 12,
      activeConnections: 10,
      errorConnections: 2,
      lastCheckTime: new Date().toLocaleString(),
      // 对话框相关
      dialogFormVisible: false,
      dialogViewVisible: false,
      dialogType: 'create', // create or update
      currentConfig: null,
      // 表单验证规则
      rules: {
        accountId: [
          { required: true, message: '请输入银行账号', trigger: 'blur' }
        ],
        connectionType: [
          { required: true, message: '请选择连接类型', trigger: 'change' }
        ],
        interfaceUrl: [
          { required: true, message: '请输入连接地址', trigger: 'blur' },
          { type: 'url', message: '请输入有效的URL地址', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.listLoading = true
      try {
        const params = {
          pageNum: this.listQuery.pageNum,
          pageSize: this.listQuery.pageSize,
          accountId: this.listQuery.accountId,
          connectionType: this.listQuery.connectionType,
          connectionStatus: this.listQuery.connectionStatus,
          status: this.listQuery.status
        }
        const response = await getBankDirectConnectionConfigPage(params)
        if (response && response.code === 1) {
          this.list = response.data?.tlist || []
          this.total = parseInt(response.data?.totalRecord || 0, 10)
          this.updateStatistics()
        } else {
          this.$message.error(response?.message || '获取配置列表失败')
        }
      } catch (error) {
        console.error('获取配置列表失败:', error)
        this.$message.error('获取配置列表失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    handleReset() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        accountId: undefined,
        connectionType: undefined,
        connectionStatus: undefined,
        status: undefined
      }
      this.fetchData()
    },
  async handleTestAll() {
      try {
        this.$message({
          type: 'info',
          message: '正在批量测试所有连接，请稍候...'
        })

        // 检查是否有数据可测试
        if (this.list.length === 0) {
          this.$message.warning('没有可测试的配置项')
          return
        }

        // 提取id数组
        const ids = this.list.map(item => item.id)

        // 发送批量测试请求
        const response = await batchTestBankDirectConnectionConfig(ids)
        if (response && response.code === 1) {
          const successCount = response.data?.successCount || 0
          const totalCount = response.data?.totalCount || this.list.length
          const failCount = totalCount - successCount

          this.$message({
            type: 'success',
            message: `批量连接测试完成：成功 ${successCount} 个，失败 ${failCount} 个`,
            duration: 3000
          })
          this.fetchData() // 刷新数据
          this.updateStatistics() // 更新统计
        } else {
          this.$message({
            type: 'error',
            message: response?.msg || response?.message || '批量连接测试失败'
          })
        }
      } catch (error) {
        console.error('批量连接测试失败:', error)
        this.$message({
          type: 'error',
          message: error.message || '批量连接测试失败，请稍后重试'
        })
      }
    },
    async handleExport() {
      try {
        this.$message({
          type: 'info',
          message: '正在导出配置数据，请稍候...'
        })

        const response = await exportBankDirectConnectionConfig(this.listQuery)
        if (response && response.code === 1) {
          // 创建下载链接
          const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `银行直连配置_${new Date().toISOString().slice(0, 10)}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)

          this.$message({
            type: 'success',
            message: '配置导出成功'
          })
        } else {
          this.$message({
            type: 'error',
            message: response?.message || '配置导出失败'
          })
        }
      } catch (error) {
        console.error('导出配置失败:', error)
        this.$message({
          type: 'error',
          message: '配置导出失败，请稍后重试'
        })
      }
    },
    handleTableSetting() {
      this.$message.info('表格设置功能开发中...')
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      if (row.connectionStatus === 'ERROR') {
        return 'error-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getConnectionTypeColor(type) {
      const colorMap = {
        'EBPP': 'success',
        'SWIFT': 'primary',
        'PROPRIETARY': 'warning',
        'HTTP': 'success',
        'WEBSERVICE': 'primary',
        'FTP': 'warning',
        'DEDICATED': 'danger'
      }
      return colorMap[type] || 'info'
    },
    getConnectionTypeIcon(type) {
      const iconMap = {
        'EBPP': 'el-icon-link',
        'SWIFT': 'el-icon-connection',
        'PROPRIETARY': 'el-icon-setting',
        'HTTP': 'el-icon-link',
        'WEBSERVICE': 'el-icon-service',
        'FTP': 'el-icon-upload',
        'DEDICATED': 'el-icon-connection'
      }
      return iconMap[type] || 'el-icon-link'
    },
    getConnectionTypeText(type) {
      const textMap = {
        'EBPP': 'EBPP协议',
        'SWIFT': 'SWIFT协议',
        'PROPRIETARY': '专有协议',
        'HTTP': 'HTTP接口',
        'WEBSERVICE': 'WebService',
        'FTP': 'FTP传输',
        'DEDICATED': '专线连接'
      }
      return textMap[type] || '未知类型'
    },
    getBankName(row) {
      // 从remark字段中提取银行名称，如果没有则使用accountId
      if (row.remark) {
        // remark格式类似：工商银行EBPP直连配置
        const match = row.remark.match(/(工商银行|中国银行|建设银行|农业银行|交通银行|邮储银行|中信银行|兴业银行|招商银行)/)
        if (match) {
          return match[1]
        }
        // 如果没有匹配到银行名称，返回remark的前几个字
        return row.remark.substring(0, 8)
      }
      return `银行-${row.accountId}`
    },
    getConnectionStatusType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getConnectionStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'ERROR': '异常',
        'MAINTENANCE': '维护中'
      }
      return textMap[status] || '未知状态'
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    handleStatusChange(row) {
      this.$message.success('状态更新成功')
    },
    async handleView(row) {
      this.currentConfig = row
      this.dialogViewVisible = true
    },
    async handleTest(row) {
      try {
        this.$message({
          type: 'info',
          message: `正在测试 ${this.getBankName(row)} 的连接，请稍候...`
        })

        const response = await testBankDirectConnectionConfig(row.id)
        if (response && response.code === 1) {
          const testResult = response.data
          if (testResult.success) {
            this.$message({
              type: 'success',
              message: `${this.getBankName(row)} 连接测试成功，响应时间：${testResult.responseTime || '-'}`
            })
          } else {
            this.$message({
              type: 'error',
              message: `${this.getBankName(row)} 连接测试失败：${testResult.message || '未知错误'}`
            })
          }
          // 更新该行状态
          row.connectionStatus = testResult.success ? 'ACTIVE' : 'ERROR'
          row.lastTestTime = new Date()
        } else {
          this.$message({
            type: 'error',
            message: response?.message || '连接测试失败'
          })
        }
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message({
          type: 'error',
          message: '连接测试失败，请稍后重试'
        })
      }
    },
    handleCreate() {
      this.dialogType = 'create'
      this.currentConfig = this.getDefaultConfig()
      this.dialogFormVisible = true
    },
    handleUpdate(row) {
      this.dialogType = 'update'
      this.currentConfig = { ...row }
      this.dialogFormVisible = true
    },
    async handleDelete(row, index) {
      try {
        this.$confirm('此操作将永久删除该配置记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const response = await deleteBankDirectConnectionConfig(row.id)
          if (response && response.code === 1) {
            this.$message.success('删除成功')
            this.fetchData()
            this.updateStatistics()
          } else {
            this.$message.error(response?.message || '删除失败')
          }
        })
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除配置失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.fetchData()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.listQuery.pageNum = 1 // 改变每页条数时重置到第一页
      this.fetchData()
    },
    async handleSave() {
      this.$refs.configForm.validate(async (valid) => {
        if (valid) {
          try {
            const isUpdate = this.dialogType === 'update'
            let response
            if (isUpdate) {
              response = await updateBankDirectConnectionConfig(this.currentConfig)
            } else {
              response = await addBankDirectConnectionConfig(this.currentConfig)
            }

            if (response && response.code === 1) {
              this.$message.success(isUpdate ? '更新成功' : '创建成功')
              this.dialogFormVisible = false
              this.fetchData()
              this.updateStatistics()
            } else {
              this.$message.error(response?.message || (isUpdate ? '更新失败' : '创建失败'))
            }
          } catch (error) {
            console.error('保存配置失败:', error)
            this.$message.error('保存失败，请稍后重试')
          }
        }
      })
    },
    // 工具方法
    getDefaultConfig() {
      return {
        id: null,
        accountId: '',
        connectionType: '',
        interfaceUrl: '',
        interfaceVersion: '',
        timeoutSeconds: 30,
        status: 1,
        connectionStatus: 'INACTIVE',
        remark: '',
        lastTestTime: null,
        createTime: new Date()
      }
    },
    updateStatistics() {
      this.totalConnections = this.list.length
      this.activeConnections = this.list.filter(item => item.connectionStatus === 'NORMAL').length
      this.errorConnections = this.list.filter(item => item.connectionStatus === 'ERROR').length
      this.lastCheckTime = new Date().toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-direct-connection-config {
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

  .connection-overview {
    margin-bottom: 20px;
    
    .overview-card {
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }
      
      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;
        
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
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
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
          
          &.error-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.check-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }
        
        .card-info {
          flex: 1;
          
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }
          
          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .card-change {
            font-size: 12px;
            
            &.positive {
              color: #67c23a;
            }
            
            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
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
    
    .bank-info {
      display: flex;
      align-items: center;
      
      .bank-logo {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        
        i {
          color: white;
          font-size: 18px;
        }
      }
      
      .bank-details {
        .bank-name {
          font-weight: 600;
          color: #303133;
        }
        
        .bank-code {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }
    
    .connection-url {
      display: flex;
      align-items: center;
      
      i {
        margin-right: 8px;
        color: #409eff;
      }
      
      span {
        color: #606266;
        font-size: 13px;
      }
    }
    
    .connection-status {
      display: flex;
      align-items: center;
      
      .status-text {
        margin-left: 8px;
        font-size: 12px;
        
        &.normal {
          color: #67c23a;
        }
        
        &.error {
          color: #f56c6c;
        }
        
        &.maintenance {
          color: #e6a23c;
        }
      }
    }
    
    .test-time {
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
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }
  
  .error-row {
    background-color: #fef0f0;
  }
  
  .el-table__row:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}
</style>
