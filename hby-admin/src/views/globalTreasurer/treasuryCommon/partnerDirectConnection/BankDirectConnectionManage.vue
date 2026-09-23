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
              v-model="listQuery.configName"
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
          <el-form-item label="接口类型">
            <el-select
              v-model="listQuery.interfaceType"
              placeholder="请选择接口类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="余额查询" value="BALANCE_QUERY" />
              <el-option label="交易查询" value="TRANSACTION_QUERY" />
              <el-option label="支付" value="PAYMENT" />
              <el-option label="回单下载" value="RECEIPT_DOWNLOAD" />
            </el-select>
          </el-form-item>
          <el-form-item label="认证方式">
            <el-select
              v-model="listQuery.authType"
              placeholder="请选择认证方式"
              clearable
              style="width: 120px;"
            >
              <el-option label="证书" value="CERTIFICATE" />
              <el-option label="令牌" value="TOKEN" />
              <el-option label="签名" value="SIGNATURE" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
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
    >
      <el-table-column label="配置名称" prop="configName" align="center" min-width="150" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.configName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="银行代码" prop="bankCode" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.bankCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接口类型" prop="interfaceType" align="center" min-width="100">
        <template slot-scope="{row}">
          <el-tag :type="getInterfaceTypeColor(row.interfaceType)" size="small">
            {{ getInterfaceTypeName(row.interfaceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="接口地址" prop="endpointUrl" align="center" min-width="200" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.endpointUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="认证方式" prop="authType" align="center" min-width="90">
        <template slot-scope="{row}">
          <el-tag :type="getAuthTypeColor(row.authType)" size="small">
            {{ getAuthTypeName(row.authType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="超时时间" prop="timeoutSeconds" align="center" min-width="90">
        <template slot-scope="{row}">
          <span>{{ row.timeoutSeconds }}秒</span>
        </template>
      </el-table-column>
      <el-table-column label="重试次数" prop="retryTimes" align="center" min-width="80">
        <template slot-scope="{row}">
          <span>{{ row.retryTimes }}次</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="80" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'BankDirectConnectionManage',
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
      listQuery: {
        page: 1,
        limit: 20,
        configName: undefined,
        bankCode: undefined,
        interfaceType: undefined,
        authType: undefined,
        isEnabled: undefined
      },
      totalConfigs: 0,
      enabledConfigs: 0,
      onlineConfigs: 0,
      errorConfigs: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 尝试调用真实API
        const response = await this.getBankDirectConnectionList(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
          this.updateStatistics()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('银企直连管理API调用失败，使用模拟数据:', error)
        const { generateBankDirectConnectionData, handleApiError } = await import('@/utils/mockData')
        const mockResponse = handleApiError(error, generateBankDirectConnectionData, 5)
        this.list = mockResponse.data.tlist
        this.total = mockResponse.data.totalRecord
        this.updateStatistics()

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.listLoading = false
      }
    },
    async getBankDirectConnectionList(params) {
      // 模拟API调用
      throw new Error('API未实现')
    },
    updateStatistics() {
      this.totalConfigs = this.list.length
      this.enabledConfigs = this.list.filter(item => item.isEnabled === 1).length
      this.onlineConfigs = this.list.filter(item => item.isEnabled === 1).length
      this.errorConfigs = this.list.filter(item => item.isEnabled === 0).length
    },
    getInterfaceTypeName(type) {
      const typeMap = {
        'BALANCE_QUERY': '余额查询',
        'TRANSACTION_QUERY': '交易查询',
        'PAYMENT': '支付',
        'RECEIPT_DOWNLOAD': '回单下载'
      }
      return typeMap[type] || type
    },
    getInterfaceTypeColor(type) {
      const colorMap = {
        'BALANCE_QUERY': 'primary',
        'TRANSACTION_QUERY': 'success',
        'PAYMENT': 'warning',
        'RECEIPT_DOWNLOAD': 'info'
      }
      return colorMap[type] || 'default'
    },
    getAuthTypeName(type) {
      const typeMap = {
        'CERTIFICATE': '证书',
        'TOKEN': '令牌',
        'SIGNATURE': '签名'
      }
      return typeMap[type] || type
    },
    getAuthTypeColor(type) {
      const colorMap = {
        'CERTIFICATE': 'success',
        'TOKEN': 'primary',
        'SIGNATURE': 'warning'
      }
      return colorMap[type] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            configId: 1,
            configName: '工商银行余额查询接口',
            bankCode: 'ICBC',
            interfaceType: 'BALANCE_QUERY',
            endpointUrl: 'https://api.icbc.com.cn/balance/query',
            authType: 'CERTIFICATE',
            certificatePath: '/certs/icbc.crt',
            privatekeyPath: '/certs/icbc.key',
            appId: 'HBY_ICBC_001',
            appSecret: '***',
            timeoutSeconds: 30,
            retryTimes: 3,
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00'
          },
          {
            configId: 2,
            configName: '建设银行支付接口',
            bankCode: 'CCB',
            interfaceType: 'PAYMENT',
            endpointUrl: 'https://api.ccb.com/payment/transfer',
            authType: 'TOKEN',
            certificatePath: '',
            privatekeyPath: '',
            appId: 'HBY_CCB_002',
            appSecret: '***',
            timeoutSeconds: 60,
            retryTimes: 2,
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00'
          }
        ]
        this.total = 2
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
