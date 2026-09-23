<template>
  <div class="direct-connect-auth-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-key"></i>
            银企直联授权管理
          </h2>
          <p class="page-description">管理银企直联接口的授权配置，包括授权类型、权限范围、证书管理和限额设置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增授权
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleBatchRefresh">
            批量刷新
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 授权统计卡片 -->
    <div class="auth-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总授权数</div>
                <div class="card-value">{{ totalAuths }}</div>
                <div class="card-change">个授权</div>
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
                <div class="card-title">有效授权</div>
                <div class="card-value">{{ activeAuths }}</div>
                <div class="card-change positive">{{ activeRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expired-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将过期</div>
                <div class="card-value">{{ expiringAuths }}</div>
                <div class="card-change warning">30天内</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon suspended-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">暂停授权</div>
                <div class="card-value">{{ suspendedAuths }}</div>
                <div class="card-change negative">需处理</div>
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
          <el-form-item label="授权ID">
            <el-input
              v-model="listQuery.authId"
              placeholder="请输入授权ID"
              style="width: 120px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="账户号码">
            <el-input
              v-model="listQuery.accountNumber"
              placeholder="请输入账户号码"
              style="width: 180px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="授权类型">
            <el-select
              v-model="listQuery.authType"
              placeholder="请选择授权类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="查询" value="QUERY" />
              <el-option label="支付" value="PAYMENT" />
              <el-option label="收款" value="RECEIPT" />
              <el-option label="转账" value="TRANSFER" />
              <el-option label="全部权限" value="ALL" />
            </el-select>
          </el-form-item>
          <el-form-item label="授权状态">
            <el-select
              v-model="listQuery.authStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="有效" value="ACTIVE" />
              <el-option label="无效" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="过期" value="EXPIRED" />
            </el-select>
          </el-form-item>
          <el-form-item label="生效日期">
            <el-date-picker
              v-model="listQuery.effectiveDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button v-waves class="filter-item" type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 授权表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="授权ID" prop="authId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.authId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户号码" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row)">{{ row.accountNo || row.accountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="授权类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAuthTypeTagType(row.authType)" size="mini">
              {{ getAuthTypeText(row.authType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="授权状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getAuthStatusTagType(row.authStatus)">
              {{ getAuthStatusText(row.authStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="授权限额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="auth-limit">{{ formatCurrency(row.authLimit) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="币种" width="80px" align="center">
          <template slot-scope="{row}">
            <span>{{ getCurrencyText(row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="生效日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.effectiveDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getExpireDateClass(row.expiryDate)">{{ row.expiryDate || '永久' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="证书状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getCertStatusTagType(row)" size="mini">
              {{ getCertStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.authStatus === 'ACTIVE'" size="mini" type="warning" @click="handleSuspend(row)">
              暂停
            </el-button>
            <el-button v-if="row.authStatus === 'SUSPENDED'" size="mini" type="success" @click="handleActivate(row)">
              激活
            </el-button>
            <el-button size="mini" type="info" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑授权对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要授权的账户" style="width: 100%;" @change="handleAccountChange">
            <el-option
              v-for="account in availableAccounts"
              :key="account.accountId"
              :label="`${account.accountNumber} - ${account.accountName}`"
              :value="account.accountId"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20" v-if="temp.accountId">
          <el-col :span="12">
            <el-form-item label="账户号码">
              <el-input v-model="temp.accountNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户名称">
              <el-input v-model="temp.accountName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授权类型" prop="authType">
              <el-select v-model="temp.authType" placeholder="请选择授权类型" style="width: 100%;">
                <el-option label="查询" value="QUERY" />
                <el-option label="支付" value="PAYMENT" />
                <el-option label="收款" value="RECEIPT" />
                <el-option label="转账" value="TRANSFER" />
                <el-option label="全部权限" value="ALL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授权状态" prop="authStatus">
              <el-select v-model="temp.authStatus" placeholder="请选择授权状态" style="width: 100%;">
                <el-option label="有效" value="ACTIVE" />
                <el-option label="无效" value="INACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker
                v-model="temp.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期">
              <el-date-picker
                v-model="temp.expiryDate"
                type="date"
                placeholder="选择到期日期（可选）"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授权限额">
              <el-input-number
                v-model="temp.authLimit"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入授权限额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种">
              <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                <el-option label="人民币 (CNY)" value="CNY" />
                <el-option label="美元 (USD)" value="USD" />
                <el-option label="欧元 (EUR)" value="EUR" />
                <el-option label="英镑 (GBP)" value="GBP" />
                <el-option label="日元 (JPY)" value="JPY" />
                <el-option label="港币 (HKD)" value="HKD" />
                <el-option label="新加坡元 (SGD)" value="SGD" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证书路径">
              <el-input v-model="temp.certificatePath" placeholder="请输入证书文件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="证书密码">
              <el-input v-model="temp.certificatePassword" placeholder="请输入证书密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
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

    <!-- 授权详情对话框 -->
    <el-dialog title="授权详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentAuth" class="auth-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="授权ID">{{ currentAuth.authId }}</el-descriptions-item>
          <el-descriptions-item label="授权状态">
            <el-tag :type="getAuthStatusTagType(currentAuth.authStatus)">
              {{ getAuthStatusText(currentAuth.authStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentAuth.accountNo }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentAuth.accountName }}</el-descriptions-item>
          <el-descriptions-item label="授权类型">{{ getAuthTypeText(currentAuth.authType) }}</el-descriptions-item>
          <el-descriptions-item label="授权限额">{{ formatCurrency(currentAuth.authLimit) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ getCurrencyText(currentAuth.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ currentAuth.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentAuth.expiryDate || '永久有效' }}</el-descriptions-item>
          <el-descriptions-item label="证书路径">{{ currentAuth.certificatePath || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentAuth.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentAuth.remark" style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentAuth.remark }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentAuth && currentAuth.authStatus === 'ACTIVE'" type="warning" @click="handleSuspend(currentAuth)">
          暂停授权
        </el-button>
        <el-button v-if="currentAuth && currentAuth.authStatus === 'SUSPENDED'" type="success" @click="handleActivate(currentAuth)">
          激活授权
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDirectConnectAuthPage, createDirectConnectAuth, updateDirectConnectAuth, deleteDirectConnectAuth, suspendDirectConnectAuth, activateDirectConnectAuth, batchDeleteDirectConnectAuth, exportDirectConnectAuth } from '@/api/globalTreasurer/zhgl'
import { getAccountInfoPage } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'DirectConnectAuthManage',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        authId: undefined,
        accountNumber: undefined,
        authType: undefined,
        authStatus: undefined,
        effectiveDateRange: undefined
      },
      totalAuths: 0,
      activeAuths: 0,
      expiringAuths: 0,
      suspendedAuths: 0,
      activeRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        authId: undefined,
        accountId: undefined,
        accountNo: '',
        accountName: '',
        authType: '',
        authStatus: 'ACTIVE',
        effectiveDate: null,
        expiryDate: null,
        authLimit: 0,
        currencyCode: '',
        certificatePath: '',
        certificatePassword: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑授权配置',
        create: '新增授权配置'
      },
      currentAuth: null,
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        authType: [{ required: true, message: '请选择授权类型', trigger: 'change' }],
        authStatus: [{ required: true, message: '请选择授权状态', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择生效日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
    this.loadAvailableAccounts()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getDirectConnectAuthPage(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          const data = response.data
          this.list = data.records || data.tlist || data.list || []
          this.total = data.total || data.totalRecord || this.list.length
          // 更新统计卡片
          this.totalAuths = data.totalAuths || 0
          this.activeAuths = data.activeAuths || 0
          this.expiringAuths = data.expiringAuths || 0
          this.suspendedAuths = data.suspendedAuths || 0
          this.activeRate = this.totalAuths > 0 ? Math.round((this.activeAuths / this.totalAuths) * 100) : 0
        } else {
          this.list = []
          this.total = 0
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取直联授权列表失败:', error)
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    async updateStatistics() {
      // 统计数据已在 getList 响应中获取，此方法保留供兼容调用
    },
    async loadAvailableAccounts() {
      try {
        const response = await getAccountInfoPage({ pageNum: 1, pageSize: 1000 })
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          const data = response.data
          this.availableAccounts = data.records || data.tlist || data.list || []
        } else {
          this.availableAccounts = []
        }
      } catch (error) {
        console.error('获取可用账户失败:', error)
        this.availableAccounts = []
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        authId: undefined,
        accountNumber: undefined,
        authType: undefined,
        authStatus: undefined,
        effectiveDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      if (row.effectiveDate) this.temp.effectiveDate = new Date(row.effectiveDate)
      if (row.expiryDate) this.temp.expiryDate = new Date(row.expiryDate)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentAuth = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      this.$message.info(`账户：${row.accountName || row.accountNumber || ''}`)
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNo = account.accountNo || account.accountNumber
        this.temp.accountName = account.accountName
      }
    },
    handleSuspend(row) {
      this.$confirm('确认暂停该授权?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await suspendDirectConnectAuth(row.authId)
          if (response && response.code === 1) {
            row.authStatus = 'SUSPENDED'
            this.$message({ type: 'success', message: '授权已暂停!' })
            this.getList()
          } else {
            this.$message.error(response.msg || '暂停失败')
          }
        } catch (error) {
          console.error('暂停失败:', error)
          this.$message.error('暂停失败，请稍后重试')
        }
      })
    },
    handleActivate(row) {
      this.$confirm('确认激活该授权?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await activateDirectConnectAuth(row.authId)
          if (response && response.code === 1) {
            row.authStatus = 'ACTIVE'
            this.$message({ type: 'success', message: '授权已激活!' })
            this.getList()
          } else {
            this.$message.error(response.msg || '激活失败')
          }
        } catch (error) {
          console.error('激活失败:', error)
          this.$message.error('激活失败，请稍后重试')
        }
      })
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('确认删除该授权配置?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteDirectConnectAuth(row.authId)
        if (response && response.code === 1) {
          this.$message.success('删除成功!')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    handleBatchRefresh() {
      if (this.multipleSelection.length === 0) {
        this.$message({ type: 'warning', message: '请选择要批量删除的授权' })
        return
      }
      this.$confirm(`确认批量删除选中的${this.multipleSelection.length}个授权配置?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const authIds = this.multipleSelection.map(item => item.authId)
          const response = await batchDeleteDirectConnectAuth(authIds)
          if (response && response.code === 1) {
            this.$message({ type: 'success', message: '批量删除成功!' })
            this.getList()
          } else {
            this.$message.error(response.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败，请稍后重试')
        }
      })
    },
    async handleExport() {
      try {
        this.$message.info('正在导出数据...')
        const params = {
          accountNumber: this.listQuery.accountNumber,
          authType: this.listQuery.authType,
          authStatus: this.listQuery.authStatus
        }
        const response = await exportDirectConnectAuth(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '银企直联授权列表.xlsx'
        a.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await createDirectConnectAuth(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('授权配置创建成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '创建失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('创建失败:', error)
          this.$message.error('创建失败，请稍后重试')
        }
      }
    },
    async updateData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await updateDirectConnectAuth(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('授权配置更新成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '更新失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('更新失败:', error)
          this.$message.error('更新失败，请稍后重试')
        }
      }
    },
    resetTemp() {
      this.temp = {
        authId: undefined,
        accountId: undefined,
        accountNo: '',
        accountName: '',
        authType: '',
        authStatus: 'ACTIVE',
        effectiveDate: null,
        expiryDate: null,
        authLimit: 0,
        currencyCode: '',
        certificatePath: '',
        certificatePassword: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'authId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.authId - b.authId)
      } else {
        this.list.sort((a, b) => b.authId - a.authId)
      }
    },
    getAuthTypeTagType(authType) {
      const typeMap = {
        'QUERY': 'primary',
        'PAYMENT': 'danger',
        'RECEIPT': 'success',
        'TRANSFER': 'warning',
        'ALL': 'info'
      }
      return typeMap[authType] || 'info'
    },
    getAuthTypeText(authType) {
      const textMap = {
        'QUERY': '查询',
        'PAYMENT': '支付',
        'RECEIPT': '收款',
        'TRANSFER': '转账',
        'ALL': '全部权限'
      }
      return textMap[authType] || authType
    },
    getCurrencyText(code) {
      const map = {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'GBP': '英镑',
        'JPY': '日元',
        'HKD': '港币',
        'SGD': '新加坡元'
      }
      return map[code] || code || '-'
    },
    getAuthStatusTagType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'EXPIRED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getAuthStatusText(status) {
      const textMap = {
        'ACTIVE': '有效',
        'INACTIVE': '无效',
        'SUSPENDED': '暂停',
        'EXPIRED': '过期'
      }
      return textMap[status] || status
    },
    getCertStatusTagType(row) {
      if (row.certificatePath && row.privateKeyPath && row.publicKeyPath) {
        return 'success'
      } else if (row.certificatePath || row.privateKeyPath || row.publicKeyPath) {
        return 'warning'
      } else {
        return 'danger'
      }
    },
    getCertStatusText(row) {
      if (row.certificatePath && row.privateKeyPath && row.publicKeyPath) {
        return '完整'
      } else if (row.certificatePath || row.privateKeyPath || row.publicKeyPath) {
        return '不完整'
      } else {
        return '未配置'
      }
    },
    getExpireDateClass(expireDate) {
      if (!expireDate) return ''
      const today = new Date()
      const expire = new Date(expireDate)
      const diffTime = expire - today
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) {
        return 'expired-date'
      } else if (diffDays <= 30) {
        return 'expiring-date'
      } else {
        return 'normal-date'
      }
    },
    formatCurrency(amount) {
      if (amount === 0) return '无限制'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.direct-connect-auth-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .auth-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
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
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.expired-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.suspended-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .auth-limit, .daily-limit {
    font-weight: 600;
    color: #E6A23C;
  }

  .expired-date {
    color: #F56C6C;
    font-weight: 600;
  }

  .expiring-date {
    color: #E6A23C;
    font-weight: 600;
  }

  .normal-date {
    color: #67C23A;
  }

  .auth-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
