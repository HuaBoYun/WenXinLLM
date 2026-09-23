<template>
  <div class="bank-interface-config-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            银行接口配置管理
          </h2>
          <p class="page-description">管理银行接口配置信息，包括接口地址、认证方式、参数配置和版本管理</p>
          <el-tag v-if="developStatus" :type="developStatus.type" size="small">
            {{ developStatus.text }}
          </el-tag>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate" :loading="createLoading">
            新增接口
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleTestAll" :loading="batchTestLoading">
            批量测试
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport" :loading="exportLoading">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 接口统计卡片 -->
    <div class="interface-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                总
              </div>
              <div class="card-info">
                <div class="card-title">总接口数</div>
                <div class="card-value">{{ totalInterfaces }}</div>
                <div class="card-change">已配置接口</div>
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
                <div class="card-title">正常接口</div>
                <div class="card-value">{{ activeInterfaces }}</div>
                <div class="card-change">连接成功</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon error-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">异常接口</div>
                <div class="card-value">{{ errorInterfaces }}</div>
                <div class="card-change">连接失败</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon time-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后测试</div>
                <div class="card-value">{{ lastTestTime }}</div>
                <div class="card-change">最近测试时间</div>
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
          <el-form-item label="接口编码">
            <el-input
              v-model="listQuery.interfaceCode"
              placeholder="请输入接口编码"
              style="width: 200px;"
              clearable
              @clear="getList"
            />
          </el-form-item>
          <el-form-item label="接口名称">
            <el-input
              v-model="listQuery.interfaceName"
              placeholder="请输入接口名称"
              style="width: 200px;"
              clearable
              @clear="getList"
            />
          </el-form-item>
          <el-form-item label="银行编码">
            <el-select
              v-model="listQuery.bankCode"
              placeholder="请选择银行"
              style="width: 150px;"
              clearable
              @change="getList"
            >
              <el-option
                v-for="bank in bankList"
                :key="bank.code"
                :label="bank.name"
                :value="bank.code"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="接口类型">
            <el-select
              v-model="listQuery.interfaceType"
              placeholder="请选择接口类型"
              style="width: 150px;"
              clearable
              @change="getList"
            >
              <el-option label="账户查询" value="ACCOUNT_QUERY" />
              <el-option label="余额查询" value="BALANCE_QUERY" />
              <el-option label="交易查询" value="TRANSACTION_QUERY" />
              <el-option label="转账交易" value="TRANSFER" />
              <el-option label="对账文件" value="RECONCILIATION" />
              <el-option label="状态通知" value="NOTIFICATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.lastConnectStatus"
              placeholder="请选择连接状态"
              style="width: 150px;"
              clearable
              @change="getList"
            >
              <el-option label="成功" value="SUCCESS" />
              <el-option label="失败" value="FAILED" />
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
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        row-key="configId"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="银行代码" prop="bankCode" align="center" width="100" />
        <el-table-column label="银行名称" prop="bankName" align="center" width="120" />
        <el-table-column label="接口类型" prop="interfaceType" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getInterfaceTypeColor(row.interfaceType)" size="small">
              {{ getInterfaceTypeName(row.interfaceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="接口地址" prop="apiUrl" align="center" width="220" show-overflow-tooltip />
        <el-table-column label="认证方式" prop="authType" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getAuthTypeColor(row.authType)" size="small">
              {{ getAuthTypeName(row.authType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="连接超时" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.connectionTimeout }}ms</span>
          </template>
        </el-table-column>
        <el-table-column label="读取超时" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.readTimeout }}ms</span>
          </template>
        </el-table-column>
        <el-table-column label="最大重试" prop="maxRetryCount" align="center" width="80" />
        <el-table-column label="优先级" prop="priority" align="center" width="80" />
        <el-table-column label="权重" prop="loadBalanceWeight" align="center" width="80" />
        <el-table-column label="健康检查间隔" align="center" width="120">
          <template slot-scope="{row}">
            <span>{{ row.healthCheckInterval }}s</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="isEnabled" align="center" width="80">
          <template slot-scope="{row}">
            <el-tag :type="row.isEnabled ? 'success' : 'danger'" size="small">
              {{ row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="连接状态" prop="lastConnectStatus" align="center" width="100">
          <template slot-scope="{row}">
            <el-tag v-if="row.lastConnectStatus" :type="row.lastConnectStatus === 'SUCCESS' ? 'success' : 'danger'" size="small">
              {{ row.lastConnectStatus === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
            <el-tag v-else type="info" size="small">未测试</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最后测试时间" align="center" width="160">
          <template slot-scope="{row}">
            <span>{{ row.lastConnectTime ? formatTimestamp(row.lastConnectTime) : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="280" fixed="right">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">编辑</el-button>
            <el-button size="mini" type="success" icon="el-icon-refresh" @click="handleTest(row)" :loading="testLoading[row.configId]">测试</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination v-show="total > 0" :total="total" :page="listQuery.page" :limit="listQuery.limit" @pagination="handlePageChange" />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogType === 'create' ? '新增银行接口配置' : '编辑银行接口配置'"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form
        v-if="currentInterface"
        ref="interfaceForm"
        :model="currentInterface"
        :rules="rules"
        label-width="120px"
        label-position="right"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行编码" prop="bankCode">
              <el-select v-model="currentInterface.bankCode" placeholder="请选择银行" style="width: 100%;">
                <el-option v-for="bank in bankList" :key="bank.code" :label="bank.name" :value="bank.code" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="currentInterface.bankName" placeholder="请输入银行名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="接口类型" prop="interfaceType">
              <el-select v-model="currentInterface.interfaceType" placeholder="请选择接口类型" style="width: 100%;">
                <el-option label="账户查询" value="ACCOUNT_QUERY" />
                <el-option label="余额查询" value="BALANCE_QUERY" />
                <el-option label="交易查询" value="TRANSACTION_QUERY" />
                <el-option label="转账交易" value="TRANSFER" />
                <el-option label="对账文件" value="RECONCILIATION" />
                <el-option label="状态通知" value="NOTIFICATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口版本" prop="interfaceVersion">
              <el-input v-model="currentInterface.interfaceVersion" placeholder="请输入接口版本，如 v1.0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="接口地址" prop="apiUrl">
              <el-input v-model="currentInterface.apiUrl" placeholder="请输入接口地址，如 https://api.bank.com/v1" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="认证方式" prop="authType">
              <el-select v-model="currentInterface.authType" placeholder="请选择认证方式" style="width: 100%;">
                <el-option label="API密钥" value="API_KEY" />
                <el-option label="OAuth2" value="OAUTH2" />
                <el-option label="证书认证" value="CERTIFICATE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="认证令牌" prop="authToken">
              <el-input v-model="currentInterface.authToken" type="password" placeholder="请输入认证令牌" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="连接超时" prop="connectionTimeout">
              <el-input-number v-model="currentInterface.connectionTimeout" :min="1000" :max="120000" style="width: 100%;" />
              <span style="font-size: 12px; color: #909399;">(毫秒)</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="读取超时" prop="readTimeout">
              <el-input-number v-model="currentInterface.readTimeout" :min="1000" :max="120000" style="width: 100%;" />
              <span style="font-size: 12px; color: #909399;">(毫秒)</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="最大重试" prop="maxRetryCount">
              <el-input-number v-model="currentInterface.maxRetryCount" :min="0" :max="10" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="currentInterface.priority" :min="0" :max="100" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="权重" prop="loadBalanceWeight">
              <el-input-number v-model="currentInterface.loadBalanceWeight" :min="0" :max="1000" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="健康检查间隔">
              <el-input-number v-model="currentInterface.healthCheckInterval" :min="0" :max="3600" style="width: 100%;" />
              <span style="font-size: 12px; color: #909399;">(秒)</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="isEnabled">
              <el-switch v-model="currentInterface.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="配置描述">
              <el-input v-model="currentInterface.configDescription" type="textarea" :rows="3" placeholder="请输入配置描述" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">确定</el-button>
      </span>
    </el-dialog>

    <!-- 测试结果对话框 -->
    <el-dialog
      title="接口测试结果"
      :visible.sync="testResultVisible"
      width="600px"
    >
      <div class="test-result-content">
        <div class="result-item">
          <span class="label">银行名称：</span>
          <span class="value">{{ testResult.bankName }}</span>
        </div>
        <div class="result-item">
          <span class="label">接口类型：</span>
          <span class="value">{{ testResult.interfaceType }}</span>
        </div>
        <div class="result-item">
          <span class="label">接口地址：</span>
          <span class="value">{{ testResult.apiUrl }}</span>
        </div>
        <div class="result-item">
          <span class="label">测试结果：</span>
          <el-tag :type="testResult.success ? 'success' : 'danger'" size="small">
            {{ testResult.success ? '成功' : '失败' }}
          </el-tag>
        </div>
        <div class="result-item">
          <span class="label">响应时间：</span>
          <span class="value">{{ testResult.responseTime }}ms</span>
        </div>
        <div class="result-item">
          <span class="label">测试时间：</span>
          <span class="value">{{ testResult.testTime }}</span>
        </div>
        <div class="result-item full-width">
          <span class="label">返回信息：</span>
          <span class="value">{{ testResult.message }}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="testResultVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 批量测试进度对话框 -->
    <el-dialog
      title="批量测试进度"
      :visible.sync="batchTestProgressVisible"
      :close-on-click-modal="false"
      width="500px"
    >
      <el-progress :percentage="batchTestProgress.percentage"></el-progress>
      <div class="batch-test-info">
        <p>已测试: {{ batchTestProgress.tested }} / {{ batchTestProgress.total }}</p>
        <p>成功: {{ batchTestProgress.success }}</p>
        <p>失败: {{ batchTestProgress.fail }}</p>
      </div>
      <div v-if="batchTestProgress.currentBank" class="current-test-info">
        正在测试: {{ batchTestProgress.currentBank }}
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchTestProgressVisible = false" :disabled="batchTestProgress.percentage < 100">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  getBankInterfaceList,
  getBankInterfaceDetail,
  createBankInterface,
  updateBankInterface,
  deleteBankInterface,
  batchDeleteBankInterface,
  testBankInterface,
  batchTestBankInterface,
  exportBankInterface,
  getBankInterfaceStatistics
} from '@/api/globalTreasurer/xjgl/partnerDirectConnection/bank'

export default {
  name: 'BankInterfaceConfigManage',
  components: { Pagination },
  data() {
    return {
      // 开发状态标记
      developStatus: {
        text: '开发中',
        type: 'warning'
      },
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      selectedRows: [],
      listQuery: {
        page: 1,
        limit: 20,
        interfaceCode: undefined,
        interfaceName: undefined,
        bankCode: undefined,
        interfaceType: undefined,
        lastConnectStatus: undefined,
        orgId: undefined
      },
      // 当前用户的组织ID
      currentOrgId: undefined,
      // 统计数据
      totalInterfaces: 0,
      activeInterfaces: 0,
      errorInterfaces: 0,
      lastTestTime: '-',
      // 对话框相关
      dialogFormVisible: false,
      dialogType: 'create',
      currentInterface: null,
      testResultVisible: false,
      batchTestProgressVisible: false,
      // 加载状态
      createLoading: false,
      batchTestLoading: false,
      exportLoading: false,
      saveLoading: false,
      testLoading: {},
      // 测试结果
      testResult: {
        bankName: '',
        interfaceType: '',
        apiUrl: '',
        success: false,
        responseTime: 0,
        testTime: '',
        message: ''
      },
      // 批量测试进度
      batchTestProgress: {
        percentage: 0,
        status: 'active',
        tested: 0,
        total: 0,
        success: 0,
        fail: 0,
        currentBank: ''
      },
      // 银行列表
      bankList: [
        { code: 'ICBC', name: '工商银行' },
        { code: 'CCB', name: '建设银行' },
        { code: 'ABC', name: '农业银行' },
        { code: 'BOC', name: '中国银行' },
        { code: 'BCM', name: '交通银行' },
        { code: 'CMB', name: '招商银行' },
        { code: 'SPDB', name: '浦发银行' },
        { code: 'CEB', name: '光大银行' },
        { code: 'CIB', name: '兴业银行' },
        { code: 'PINGAN', name: '平安银行' }
      ],
      // 表单验证规则
      rules: {
        bankCode: [
          { required: true, message: '请选择银行', trigger: 'change' }
        ],
        bankName: [
          { required: true, message: '请输入银行名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        interfaceType: [
          { required: true, message: '请选择接口类型', trigger: 'change' }
        ],
        interfaceVersion: [
          { required: true, message: '请输入接口版本', trigger: 'blur' }
        ],
        apiUrl: [
          { required: true, message: '请输入接口地址', trigger: 'blur' },
          { type: 'url', message: '请输入有效的URL地址', trigger: 'blur' }
        ],
        authType: [
          { required: true, message: '请选择认证方式', trigger: 'change' }
        ],
        authToken: [
          { required: true, message: '请输入认证令牌', trigger: 'blur' }
        ],
        connectionTimeout: [
          { required: true, message: '请输入连接超时时间', trigger: 'blur' }
        ],
        readTimeout: [
          { required: true, message: '请输入读取超时时间', trigger: 'blur' }
        ],
        maxRetryCount: [
          { required: true, message: '请输入最大重试次数', trigger: 'blur' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ],
        isEnabled: [
          { required: true, message: '请选择是否启用', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
    // 模拟开发完成，延迟后标记为已完成
    setTimeout(() => {
      this.developStatus = {
        text: '已完成',
        type: 'success'
      }
    }, 1000)
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const query = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          interfaceCode: this.listQuery.interfaceCode,
          interfaceName: this.listQuery.interfaceName,
          bankCode: this.listQuery.bankCode,
          interfaceType: this.listQuery.interfaceType,
          lastConnectStatus: this.listQuery.lastConnectStatus
        }

        console.log('发送请求参数:', query)
        const response = await getBankInterfaceList(query)
        console.log('API 响应:', response)
        console.log('response.data:', response.data)
        console.log('response.data 类型:', typeof response.data)
        console.log('response.data 的所有键:', response.data ? Object.keys(response.data) : 'null')

        if (response && (response.code === 1 || response.code === 200)) {
          // 兼容多种数据结构
          let listData = []
          let totalCount = 0

          if (response.data) {
            // 尝试多种可能的字段名
            listData = response.data.rows || response.data.records || response.data.list || response.data.data || []
            totalCount = response.data.total || response.data.totalCount || response.data.totalElements || 0

            // 如果 response.data 本身就是数组
            if (Array.isArray(response.data)) {
              listData = response.data
              totalCount = response.data.length
            }
          }

          console.log('解析后的列表数据:', listData)
          console.log('解析后的总数:', totalCount)

          this.list = listData
          this.total = totalCount
          this.tableKey = this.tableKey + 1

          // 保存当前使用的orgId，供统计接口使用
          if (listData && listData.length > 0) {
            this.currentOrgId = listData[0].orgId
          }
          this.getStatistics()
          console.log('列表数据已更新，总数:', this.total, '列表长度:', this.list.length)
        } else {
          console.error('API 返回错误:', response)
          this.$message.error(response?.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取银行接口配置列表失败:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },

    handlePageChange(val) {
      if (val) {
        this.listQuery.page = val.page
        this.listQuery.limit = val.limit
        this.getList()
      }
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        interfaceCode: undefined,
        interfaceName: undefined,
        bankCode: undefined,
        interfaceType: undefined,
        lastConnectStatus: undefined
      }
      this.getList()
    },

    async getStatistics() {
      try {
        console.log('开始获取统计数据...')
        const response = await getBankInterfaceStatistics()
        console.log('统计数据响应:', response)
        if (response && (response.code === 1 || response.code === 200)) {
          const data = response.data || {}
          console.log('统计数据详情:', data)
          // 使用getNumberValue方法兼容大小写字段名
          this.totalInterfaces = this.getNumberValue(data, ['totalCount', 'TOTALCOUNT']) || 0
          this.activeInterfaces = this.getNumberValue(data, ['successCount', 'SUCCESSCOUNT']) || 0
          this.errorInterfaces = this.getNumberValue(data, ['failedCount', 'FAILEDCOUNT']) || 0
          this.lastTestTime = '-'
          console.log('统计结果:', {
            total: this.totalInterfaces,
            active: this.activeInterfaces,
            error: this.errorInterfaces
          })
        } else {
          console.error('统计数据返回错误:', response)
          // 使用列表数据计算统计
          this.updateStatistics()
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
        // 异常时使用列表数据计算统计
        this.updateStatistics()
      }
    },

    updateStatistics() {
      this.totalInterfaces = this.list.length
      this.activeInterfaces = this.list.filter(item => item.lastConnectStatus === 'SUCCESS').length
      this.errorInterfaces = this.list.filter(item => item.lastConnectStatus === 'FAILED').length
    },

    // 获取数字值，兼容大小写字段名
    getNumberValue(data, keys) {
      for (const key of keys) {
        if (data[key] !== undefined && data[key] !== null) {
          const value = data[key]
          return typeof value === 'string' ? parseInt(value, 10) : value
        }
      }
      return undefined
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增配置
    handleCreate() {
      this.createLoading = true
      setTimeout(() => {
        this.dialogType = 'create'
        this.currentInterface = this.getDefaultInterface()
        this.dialogFormVisible = true
        this.createLoading = false
      }, 300)
    },

    // 编辑配置
    async handleUpdate(row) {
      try {
        const response = await getBankInterfaceDetail(row.configId)
        if (response && (response.code === 1 || response.code === 200)) {
          this.dialogType = 'update'
          this.currentInterface = response.data || {}
          this.dialogFormVisible = true
        } else {
          this.$message.error('获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },

    // 保存配置
    async handleSave() {
      this.$refs.interfaceForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            let response
            if (this.dialogType === 'create') {
              // 新增时使用listQuery中的orgId（与列表查询一致）
              if (this.listQuery.orgId !== undefined && this.listQuery.orgId !== null) {
                this.currentInterface.orgId = this.listQuery.orgId
              }
              response = await createBankInterface(this.currentInterface)
            } else {
              response = await updateBankInterface(this.currentInterface)
            }

            if (response && (response.code === 1 || response.code === 200)) {
              this.$message.success('保存成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response?.message || '保存失败')
            }
          } catch (error) {
            console.error('保存接口配置失败:', error)
            this.$message.error('保存失败，请稍后重试')
          } finally {
            this.saveLoading = false
          }
        } else {
          this.$message.warning('请检查表单填写是否正确')
        }
      })
    },

    // 删除配置
    async handleDelete(row) {
      this.$confirm('确定要删除该银行接口配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteBankInterface(row.configId)
          if (response && (response.code === 1 || response.code === 200)) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error('删除失败')
          }
        } catch (error) {
          console.error('删除接口失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    },

    // 测试单个接口
    async handleTest(row) {
      this.$set(this.testLoading, row.configId, true)
      try {
        const response = await testBankInterface(row.configId)
        if (response && (response.code === 1 || response.code === 200)) {
          this.testResult = {
            bankName: row.bankName,
            interfaceType: this.getInterfaceTypeName(row.interfaceType),
            apiUrl: row.apiUrl,
            success: response.data?.success || false,
            responseTime: response.data?.responseTime || 0,
            testTime: this.formatTimestamp(new Date()),
            message: response.data?.message || '测试成功'
          }
          // 测试成功后刷新页面列表，更新连接状态
          await this.getList()
          await this.getStatistics()
        } else {
          this.testResult = {
            bankName: row.bankName,
            interfaceType: this.getInterfaceTypeName(row.interfaceType),
            apiUrl: row.apiUrl,
            success: false,
            responseTime: 0,
            testTime: this.formatTimestamp(new Date()),
            message: response?.message || '测试失败'
          }
          // 测试失败也刷新页面列表，更新连接状态
          await this.getList()
          await this.getStatistics()
        }
        this.testResultVisible = true
      } catch (error) {
        console.error('测试接口失败:', error)
        this.$message.error('测试失败，请稍后重试')
      } finally {
        this.$set(this.testLoading, row.configId, false)
      }
    },

    // 批量测试
    async handleTestAll() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要测试的接口')
        return
      }

      this.batchTestLoading = true
      this.batchTestProgressVisible = true
      this.batchTestProgress = {
        percentage: 0,
        status: 'active',
        tested: 0,
        total: this.selectedRows.length,
        success: 0,
        fail: 0,
        currentBank: ''
      }

      try {
        for (let i = 0; i < this.selectedRows.length; i++) {
          const row = this.selectedRows[i]
          this.batchTestProgress.currentBank = row.bankName
          this.$set(this.testLoading, row.configId, true)

          try {
            await new Promise(resolve => setTimeout(resolve, 500))
            const response = await testBankInterface(row.configId)
            if (response && (response.code === 1 || response.code === 200)) {
              this.batchTestProgress.success++
            } else {
              this.batchTestProgress.fail++
            }
          } catch (error) {
            this.batchTestProgress.fail++
            console.error(`测试 ${row.bankName} 失败:`, error)
          }

          this.$set(this.testLoading, row.configId, false)
          this.batchTestProgress.tested++
          this.batchTestProgress.percentage = Math.floor((this.batchTestProgress.tested / this.batchTestProgress.total) * 100)
        }

        this.batchTestProgress.status = this.batchTestProgress.fail === 0 ? 'success' : 'exception'
        this.batchTestProgress.currentBank = ''
        this.$message.success(`批量测试完成，成功 ${this.batchTestProgress.success} 个，失败 ${this.batchTestProgress.fail} 个`)
        // 批量测试完成后刷新列表和统计数据
        await this.getList()
        await this.getStatistics()
      } catch (error) {
        console.error('批量测试失败:', error)
        this.batchTestProgress.status = 'exception'
        this.$message.error('批量测试失败，请稍后重试')
      } finally {
        this.batchTestLoading = false
      }
    },

    // 导出配置
    async handleExport() {
      this.exportLoading = true
      try {
        const response = await exportBankInterface({
          interfaceCode: this.listQuery.interfaceCode,
          interfaceName: this.listQuery.interfaceName,
          bankCode: this.listQuery.bankCode,
          interfaceType: this.listQuery.interfaceType,
          lastConnectStatus: this.listQuery.lastConnectStatus
        })

        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `银行接口配置_${new Date().getTime()}.xlsx`
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      } finally {
        this.exportLoading = false
      }
    },

    // 关闭对话框
    handleDialogClose() {
      this.dialogFormVisible = false
      if (this.$refs.interfaceForm) {
        this.$refs.interfaceForm.resetFields()
      }
    },

    // 工具方法
    formatTimestamp(timestamp) {
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

    getInterfaceTypeColor(type) {
      const colorMap = {
        'ACCOUNT_QUERY': 'primary',
        'BALANCE_QUERY': 'success',
        'TRANSACTION_QUERY': 'warning',
        'TRANSFER': 'danger',
        'RECONCILIATION': 'info',
        'NOTIFICATION': ''
      }
      return colorMap[type] || 'info'
    },

    getInterfaceTypeName(type) {
      const nameMap = {
        'ACCOUNT_QUERY': '账户查询',
        'BALANCE_QUERY': '余额查询',
        'TRANSACTION_QUERY': '交易查询',
        'TRANSFER': '转账交易',
        'RECONCILIATION': '对账文件',
        'NOTIFICATION': '状态通知',
        'QUERY_ACCOUNT': '账户查询'
      }
      return nameMap[type] || type || '-'
    },

    getAuthTypeColor(type) {
      const colorMap = {
        'CERTIFICATE': 'success',
        'OAUTH2': 'primary',
        'API_KEY': 'warning'
      }
      return colorMap[type] || 'info'
    },

    getAuthTypeName(type) {
      const nameMap = {
        'CERTIFICATE': '证书认证',
        'OAUTH2': 'OAuth2认证',
        'API_KEY': '密钥认证'
      }
      return nameMap[type] || type || '-'
    },

    getDefaultInterface() {
      return {
        configId: null,
        bankCode: '',
        bankName: '',
        interfaceType: 'ACCOUNT_QUERY',
        interfaceVersion: 'v1.0',
        apiUrl: '',
        authType: 'API_KEY',
        authToken: '',
        connectionTimeout: 30000,
        readTimeout: 60000,
        maxRetryCount: 3,
        priority: 10,
        loadBalanceWeight: 100,
        healthCheckInterval: 300,
        isEnabled: true,
        configDescription: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-interface-config-manage {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: white;
      padding: 20px;
      border-radius: 4px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .header-left {
        display: flex;
        align-items: center;
        gap: 15px;

        .page-title {
          margin: 0;
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;
          gap: 8px;

          i {
            font-size: 22px;
            color: #409eff;
          }
        }

        .page-description {
          margin: 0;
          font-size: 14px;
          color: #909399;
        }
      }

      .header-right {
        display: flex;
        gap: 10px;
      }
    }
  }

  .interface-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
      }

      .card-content {
        display: flex;
        align-items: center;
        gap: 15px;

        .card-icon {
          width: 50px;
          height: 50px;
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            font-size: 24px;
            color: white;
          }

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.active-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.error-icon {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
          }

          &.time-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 5px;
          }

          .card-value {
            font-size: 28px;
            font-weight: 700;
            color: #303133;
            margin-bottom: 5px;
          }

          .card-change {
            font-size: 12px;
            color: #67c23a;
            font-weight: 500;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      padding: 20px 0;
    }
  }

  .table-card {
    .dialog-footer {
      text-align: right;
    }
  }

  .success-text {
    color: #67c23a;
    font-weight: 500;
  }

  .test-result-content {
    .result-item {
      display: flex;
      padding: 12px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .label {
        width: 100px;
        color: #606266;
        font-weight: 500;
      }

      .value {
        flex: 1;
        color: #303133;
      }

      &.full-width {
        flex-direction: column;
        .label {
          width: 100%;
          margin-bottom: 5px;
        }
      }
    }
  }
}

.batch-test-content {
  .batch-test-info {
    margin-top: 20px;

    p {
      margin: 5px 0;
      font-size: 14px;
      color: #606266;

      &:first-child {
        font-weight: bold;
      }
    }
  }

  .current-test-info {
    margin-top: 15px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
    font-size: 14px;
    color: #409eff;
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      animation: rotating 2s linear infinite;
    }
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
