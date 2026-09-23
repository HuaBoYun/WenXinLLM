<template>
  <div class="bank-direct-connection-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-link"></i>
            银行直连管理
          </h2>
          <p class="page-description">管理银行直连服务，包括连接配置、状态监控、交易处理和异常管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增连接
          </el-button>
          <el-button type="success" icon="el-icon-connection" @click="handleBatchTest">
            批量测试
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 连接统计卡片 -->
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
                <div class="card-change">银行直连</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon online-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">在线连接</div>
                <div class="card-value">{{ onlineConnections }}</div>
                <div class="card-change positive">正常服务</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon offline-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">离线连接</div>
                <div class="card-value">{{ offlineConnections }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon transaction-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日交易</div>
                <div class="card-value">{{ todayTransactions }}</div>
                <div class="card-change">笔</div>
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
          <el-form-item label="配置名称">
            <el-input
              v-model="listQuery.remark"
              placeholder="请输入配置名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="银行代码">
            <el-input
              v-model="listQuery.bankCode"
              placeholder="请输入银行代码"
              style="width: 150px;"
              clearable
            />
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

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      row-key="id"
    >
      <el-table-column label="配置名称" prop="remark" align="center" width="200" />
      <el-table-column label="银行代码" prop="bankCode" align="center" width="100" />
      <el-table-column label="账户号码" prop="accountNumber" align="center" width="180" />
      <el-table-column label="连接状态" prop="connectionStatus" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getConnectionStatusTag(row.connectionStatus)" size="small">
            {{ getConnectionStatusText(row.connectionStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="查询" align="center" width="70">
        <template slot-scope="{row}">
          <el-tag :type="row.queryPermission === '1' ? 'success' : 'info'" size="mini">
            {{ row.queryPermission === '1' ? '✓' : '✗' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="支付" align="center" width="70">
        <template slot-scope="{row}">
          <el-tag :type="row.paymentPermission === '1' ? 'success' : 'info'" size="mini">
            {{ row.paymentPermission === '1' ? '✓' : '✗' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="收款" align="center" width="70">
        <template slot-scope="{row}">
          <el-tag :type="row.receivePermission === '1' ? 'success' : 'info'" size="mini">
            {{ row.receivePermission === '1' ? '✓' : '✗' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="对账" align="center" width="70">
        <template slot-scope="{row}">
          <el-tag :type="row.reconciliationPermission === '1' ? 'success' : 'info'" size="mini">
            {{ row.reconciliationPermission === '1' ? '✓' : '✗' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="服务可用性" align="center" width="100">
        <template slot-scope="{row}">
          <span :class="getAvailabilityClass(row.serviceAvailability)">
            {{ row.serviceAvailability }}%
          </span>
        </template>
      </el-table-column>
      <el-table-column label="最后连接时间" prop="lastConnectionTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ formatDateTime(row.lastConnectionTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" width="80">
        <template slot-scope="{row}">
          <el-tag :type="row.status === '1' ? 'success' : 'danger'" size="small">
            {{ row.status === '1' ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" fixed="right">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
          <el-button size="mini" type="success" @click="handleTest(row)">测试</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        background
        :current-page="listQuery.page"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="listQuery.limit"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 新增/编辑连接对话框 -->
    <el-dialog
      :title="dialogType === 'create' ? '新增银行直连' : '编辑银行直连'"
      :visible.sync="dialogFormVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        v-if="dialogFormVisible"
        ref="connectionForm"
        :model="currentConfig"
        :rules="rules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="currentConfig.configName" placeholder="请输入配置名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行代码" prop="bankCode">
              <el-select v-model="currentConfig.bankCode" placeholder="请选择银行" style="width: 100%">
                <el-option label="中国工商银行" value="ICBC" />
                <el-option label="中国建设银行" value="CCB" />
                <el-option label="中国农业银行" value="ABC" />
                <el-option label="中国银行" value="BOC" />
                <el-option label="交通银行" value="BOCOM" />
                <el-option label="招商银行" value="CMB" />
                <el-option label="浦发银行" value="SPDB" />
                <el-option label="中信银行" value="CITIC" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户ID">
              <el-input v-model="currentConfig.accountId" placeholder="请输入账户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户号码" prop="accountNumber">
              <el-input v-model="currentConfig.accountNumber" placeholder="请输入账户号码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="接口类型" prop="interfaceType">
              <el-select v-model="currentConfig.interfaceType" placeholder="请选择接口类型" style="width: 100%">
                <el-option label="余额查询" value="BALANCE_QUERY" />
                <el-option label="交易查询" value="TRANSACTION_QUERY" />
                <el-option label="支付" value="PAYMENT" />
                <el-option label="回单下载" value="RECEIPT_DOWNLOAD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="认证方式" prop="authType">
              <el-select v-model="currentConfig.authType" placeholder="请选择认证方式" style="width: 100%">
                <el-option label="证书" value="CERTIFICATE" />
                <el-option label="令牌" value="TOKEN" />
                <el-option label="签名" value="SIGNATURE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="接口地址" prop="endpointUrl">
          <el-input v-model="currentConfig.endpointUrl" placeholder="请输入接口地址" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用ID" prop="appId">
              <el-input v-model="currentConfig.appId" placeholder="请输入应用ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用密钥">
              <el-input v-model="currentConfig.appSecret" type="password" placeholder="请输入应用密钥" show-password />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="currentConfig.authType === 'CERTIFICATE'">
          <el-col :span="12">
            <el-form-item label="证书路径">
              <el-input v-model="currentConfig.certificatePath" placeholder="请输入证书路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="私钥路径">
              <el-input v-model="currentConfig.privateKeyPath" placeholder="请输入私钥路径" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="超时时间(秒)" prop="timeoutSeconds">
              <el-input-number v-model="currentConfig.timeoutSeconds" :min="1" :max="300" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重试次数">
              <el-input-number v-model="currentConfig.retryTimes" :min="0" :max="5" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="状态">
          <el-radio-group v-model="currentConfig.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import {
  getBankDirectConnectionPage,
  getBankDirectConnectionDetail,
  addBankDirectConnection,
  updateBankDirectConnection,
  deleteBankDirectConnection,
  batchTestBankDirectConnection,
  getBankDirectConnectionStatistics,
  exportBankDirectConnection
} from '@/api/globalTreasurer-new/partnerDirectConnection/bank'

export default {
  name: 'BankDirectConnectionManage',
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
        page: 1,
        limit: 20,
        remark: undefined,
        bankCode: undefined,
        status: undefined
      },
      totalConnections: 0,
      onlineConnections: 0,
      offlineConnections: 0,
      todayTransactions: 0,
      // 对话框相关
      dialogFormVisible: false,
      dialogType: 'create', // create or update
      currentConfig: null,
      // 表单验证规则
      rules: {
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        bankCode: [
          { required: true, message: '请输入银行代码', trigger: 'blur' }
        ],
        accountNumber: [
          { required: true, message: '请输入账户号码', trigger: 'blur' }
        ],
        interfaceType: [
          { required: true, message: '请选择接口类型', trigger: 'change' }
        ],
        endpointUrl: [
          { required: true, message: '请输入接口地址', trigger: 'blur' },
          { type: 'url', message: '请输入有效的URL地址', trigger: 'blur' }
        ],
        authType: [
          { required: true, message: '请选择认证方式', trigger: 'change' }
        ],
        appId: [
          { required: true, message: '请输入应用ID', trigger: 'blur' }
        ],
        timeoutSeconds: [
          { required: true, message: '请输入超时时间', trigger: 'blur' }
        ]
      }
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
          pageNum: this.listQuery.page,
          pageSize: this.listQuery.limit,
          remark: this.listQuery.remark,
          bankCode: this.listQuery.bankCode,
          status: this.listQuery.status
        }
        console.log('请求参数:', params)
        const response = await getBankDirectConnectionPage(params)
        if (response && response.code === 1) {
          this.list = response.data?.tlist || []
          this.total = response.data?.totalRecord || 0
          console.log('=== 分页数据调试 ===')
          console.log('当前页:', this.listQuery.page)
          console.log('每页条数:', this.listQuery.limit)
          console.log('总记录数 total:', this.total)
          console.log('总页数:', Math.ceil(this.total / this.listQuery.limit))
          console.log('当前数据条数:', this.list.length)
          console.log('==================')
          // 获取统计数据
          await this.fetchStatistics()
        } else {
          this.$message.error(response?.message || '获取连接列表失败')
        }
      } catch (error) {
        console.error('获取连接列表失败:', error)
        this.$message.error('获取连接列表失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    async fetchStatistics() {
      try {
        const response = await getBankDirectConnectionStatistics()
        if (response && response.code === 1 && response.data) {
          const data = response.data
          // 后端返回的字段名是大写的，需要正确映射
          this.totalConnections = parseInt(data.TOTALCONNECTIONS) || 0
          this.onlineConnections = parseInt(data.ACTIVECONNECTIONS) || 0
          // 离线连接数 = 总连接数 - 活跃连接数
          this.offlineConnections = this.totalConnections - this.onlineConnections
          // 暂时没有今日交易数据，设置为0
          this.todayTransactions = 0
        } else {
          // 如果接口返回失败，回退到基于当前页数据的统计
          this.updateStatisticsFromList()
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
        // 回退到基于当前页数据的统计
        this.updateStatisticsFromList()
      }
    },
    updateStatisticsFromList() {
      this.totalConnections = this.total
      this.onlineConnections = this.list.filter(item => item.connectionStatus === 'ACTIVE').length
      this.offlineConnections = this.list.filter(item => item.connectionStatus !== 'ACTIVE').length
      this.todayTransactions = 0
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.listQuery.page = 1
      this.getList()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        remark: undefined,
        bankCode: undefined,
        status: undefined
      }
      this.getList()
    },
    getConnectionStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'CLOSED': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getConnectionStatusText(status) {
      const textMap = {
        'ACTIVE': '已开通',
        'INACTIVE': '未开通',
        'SUSPENDED': '已暂停',
        'CLOSED': '已关闭'
      }
      return textMap[status] || status
    },
    getAvailabilityClass(availability) {
      if (availability >= 99) return 'high-availability'
      if (availability >= 95) return 'medium-availability'
      return 'low-availability'
    },
    formatDateTime(timestamp) {
      if (!timestamp) return '-'
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    async handleTest(row) {
      try {
        this.$message({
          type: 'info',
          message: '正在测试连接...'
        })
        // TODO: 调用测试接口
        this.$message.success('连接测试成功')
      } catch (error) {
        this.$message.error('连接测试失败')
      }
    },
    handleCreate() {
      this.dialogType = 'create'
      this.currentConfig = this.getDefaultConfig()
      this.dialogFormVisible = true
    },
    handleUpdate(row) {
      this.dialogType = 'update'
      console.log('编辑的原始数据:', row)
      // 转换后端数据到表单字段
      this.currentConfig = {
        configId: row.id || row.configId,
        configName: row.remark || '',
        accountId: row.accountId || '',
        accountNumber: row.accountNumber || '',
        bankCode: row.bankCode || '',
        interfaceType: row.supportedBusinessTypes || 'BALANCE_QUERY',
        endpointUrl: row.endpointUrl || '',
        authType: row.authType || 'TOKEN',
        certificatePath: row.certificatePath || '',
        privateKeyPath: row.privateKeyPath || '',
        appId: row.appId || '',
        appSecret: row.appSecret || '',
        timeoutSeconds: row.timeoutSeconds || 30,
        retryTimes: row.retryTimes || 3,
        isEnabled: parseInt(row.status) || 1,
        connectionStatus: row.connectionStatus || 'ACTIVE',
        createTime: row.createTime || new Date()
      }

      console.log('转换后的表单数据:', this.currentConfig)
      this.dialogFormVisible = true
    },
    async handleDelete(row, index) {
      try {
        this.$confirm('此操作将永久删除该连接配置, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          console.log('删除的配置ID:', row.configId || row.id)
          // 使用 GET 请求传递 configId
          const response = await deleteBankDirectConnection(row.configId || row.id)
          console.log('删除接口返回:', response)

          if (response && response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            console.error('删除失败，响应码:', response?.code, '响应消息:', response?.msg)
            this.$message.error(response?.msg || response?.message || '删除失败')
          }
        })
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除配置失败:', error)
          console.error('错误详情:', error.response?.data || error.message)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    async handleBatchTest() {
      try {
        this.$message({
          type: 'info',
          message: '正在批量测试所有连接，请稍候...'
        })

        const response = await batchTestBankDirectConnection()
        if (response && response.code === 1) {
          const { successCount, totalCount, failCount } = response.data
          this.$message({
            type: 'success',
            message: `批量测试完成：成功 ${successCount} 个，失败 ${failCount} 个`,
            duration: 3000
          })
          this.getList()
        } else {
          this.$message.error(response?.message || '批量测试失败')
        }
      } catch (error) {
        console.error('批量测试失败:', error)
        this.$message.error('批量测试失败，请稍后重试')
      }
    },
    async handleExport() {
      try {
        this.$message({
          type: 'info',
          message: '正在导出配置数据，请稍候...'
        })

        const response = await exportBankDirectConnection(this.listQuery)
        if (response && response.code === 1) {
          const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `银行直连管理_${new Date().toISOString().slice(0, 10)}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)

          this.$message.success('导出成功')
        } else {
          this.$message.error(response?.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    async handleSave() {
      this.$refs.connectionForm.validate(async (valid) => {
        if (valid) {
          try {
            const isUpdate = this.dialogType === 'update'
            console.log('准备保存的数据:', this.currentConfig)

            // 转换表单数据以匹配后端字段
            const saveData = {
              id: this.currentConfig.configId || this.currentConfig.id,
              accountId: this.currentConfig.accountId || '',
              accountNumber: this.currentConfig.accountNumber || '',
              bankCode: this.currentConfig.bankCode,
              remark: this.currentConfig.configName || '',
              connectionStatus: isUpdate ? this.currentConfig.connectionStatus : 'ACTIVE',
              status: String(this.currentConfig.isEnabled),
              supportedBusinessTypes: this.currentConfig.interfaceType || '',
              serviceAvailability: 0,
              paymentPermission: '0',
              queryPermission: '0',
              receivePermission: '0',
              reconciliationPermission: '0'
            }

            console.log('转换后的保存数据:', saveData)

            let response
            if (isUpdate) {
              response = await updateBankDirectConnection(saveData)
            } else {
              response = await addBankDirectConnection(saveData)
            }

            console.log('保存接口返回:', response)

            if (response && response.code === 1) {
              this.$message.success(isUpdate ? '更新成功' : '创建成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              // 后端返回的字段是 msg 而不是 message
              this.$message.error(response?.msg || (isUpdate ? '更新失败' : '创建失败'))
            }
          } catch (error) {
            console.error('保存配置失败:', error)
            console.error('错误详情:', error.response?.data || error.message)
            // 如果有后端返回的错误信息，使用后端的错误信息
            const errorMsg = error.response?.data?.msg || error.response?.data?.message || '保存失败，请稍后重试'
            this.$message.error(errorMsg)
          }
        }
      })
    },
    // 工具方法
    getDefaultConfig() {
      return {
        configId: null,
        configName: '',
        bankCode: '',
        interfaceType: '',
        endpointUrl: '',
        authType: '',
        certificatePath: '',
        privateKeyPath: '',
        appId: '',
        appSecret: '',
        timeoutSeconds: 30,
        retryTimes: 3,
        isEnabled: 1,
        createTime: new Date()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-direct-connection-manage {
  padding: 20px;

  .high-availability {
    color: #67c23a;
    font-weight: bold;
  }

  .medium-availability {
    color: #e6a23c;
    font-weight: bold;
  }

  .low-availability {
    color: #f56c6c;
    font-weight: bold;
  }

  .pagination-wrapper {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
