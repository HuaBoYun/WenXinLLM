<template>
  <div class="eticket-account-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-tickets"></i>
            电票账户配置
          </h2>
          <p class="page-description">管理电子票据账户配置信息，包括账户开通、权限设置、额度管理和状态监控</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增账户
          </el-button>
          <el-button type="success" icon="el-icon-connection" @click="handleSync">
            同步状态
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 账户统计卡片 -->
    <div class="account-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总账户数</div>
                <div class="card-value">{{ totalAccounts }}</div>
                <div class="card-change">已开通账户</div>
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
                <div class="card-title">正常账户</div>
                <div class="card-value">{{ activeAccounts }}</div>
                <div class="card-change positive">状态正常</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon limit-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总授信额度</div>
                <div class="card-value">{{ totalCreditLimit }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日交易</div>
                <div class="card-value">{{ todayTransactions }}</div>
                <div class="card-change">交易笔数</div>
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
          <el-form-item label="账户编码">
            <el-input v-model="listQuery.accountCode" placeholder="请输入账户编码" style="width: 150px;" clearable />
          </el-form-item>
          <el-form-item label="账户名称">
            <el-input v-model="listQuery.accountName" placeholder="请输入账户名称" style="width: 180px;" clearable />
          </el-form-item>
          <el-form-item label="电票系统">
            <el-select v-model="listQuery.eTicketSystem" placeholder="请选择" clearable style="width: 120px;">
              <el-option label="电子商业汇票系统(ECDS)" value="ECDS" />
              <el-option label="银行承兑汇票系统(ACDS)" value="ACDS" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户类型">
            <el-select v-model="listQuery.accountType" placeholder="请选择" clearable style="width: 130px;">
              <el-option label="基本账户" value="BASIC" />
              <el-option label="一般账户" value="GENERAL" />
              <el-option label="专用账户" value="SPECIAL" />
              <el-option label="临时账户" value="TEMPORARY" />
            </el-select>
          </el-form-item>
          <el-form-item label="开户银行">
            <el-select v-model="listQuery.bankCode" placeholder="请选择" clearable style="width: 120px;">
              <el-option label="工商银行" value="ICBC" />
              <el-option label="建设银行" value="CCB" />
              <el-option label="农业银行" value="ABC" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="交通银行" value="BOCOM" />
              <el-option label="招商银行" value="CMB" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户状态">
            <el-select v-model="listQuery.accountStatus" placeholder="请选择" clearable style="width: 120px;">
              <el-option label="激活" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
              <el-option label="冻结" value="FROZEN" />
              <el-option label="关闭" value="CLOSED" />
            </el-select>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter" style="margin-left: 10px;">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset" style="margin-left: 10px;">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">电票账户配置列表</span>
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
        <el-table-column v-if="isColumnVisible('accountCode')" label="账户编码" prop="accountCode" align="center" min-width="130">
          <template slot-scope="{row}">
            <span class="account-number">{{ row.accountCode }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('accountName')" label="账户名称" min-width="160" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('eTicketSystem')" label="电票系统" min-width="90" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.eTicketSystem" :type="row.eTicketSystem === 'ECDS' ? 'primary' : 'success'" size="small">{{ getETicketSystemName(row.eTicketSystem) }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('accountType')" label="账户类型" min-width="110" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.accountType" :type="getAccountTypeColor(row.accountType)" size="small">{{ getAccountTypeName(row.accountType) }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('bankCode')" label="开户银行" min-width="110" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBankColor(row.bankCode)" size="small">{{ row.bankName || getBankName(row.bankCode) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('accountNumber')" label="账号" min-width="160" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.accountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('balanceLimit')" label="余额限制" min-width="120" align="center">
          <template slot-scope="{row}">
            <span class="balance-amount">{{ formatCurrency(row.balanceLimit) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('dailyLimit')" label="日限额" min-width="120" align="center">
          <template slot-scope="{row}">
            <span class="balance-amount">{{ formatCurrency(row.dailyLimit) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('createTime')" label="创建时间" min-width="110" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('accountStatus')" label="账户状态" min-width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusColor(row.accountStatus)" size="small">{{ getStatusName(row.accountStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('actions')" label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">编辑</el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">查看</el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleDelete(row,$index)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="padding: 0 20px;">
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
            <el-form-item label="电票系统" prop="eTicketSystem">
              <el-select v-model="temp.eTicketSystem" placeholder="请选择" style="width: 100%;">
                <el-option label="电子商业汇票系统(ECDS)" value="ECDS" />
                <el-option label="银行承兑汇票系统(ACDS)" value="ACDS" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="temp.accountType" placeholder="请选择" style="width: 100%;">
                <el-option label="基本账户" value="BASIC" />
                <el-option label="一般账户" value="GENERAL" />
                <el-option label="专用账户" value="SPECIAL" />
                <el-option label="临时账户" value="TEMPORARY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankCode">
              <el-select v-model="temp.bankCode" placeholder="请选择" style="width: 100%;" @change="onBankChange">
                <el-option label="工商银行" value="ICBC" />
                <el-option label="建设银行" value="CCB" />
                <el-option label="农业银行" value="ABC" />
                <el-option label="中国银行" value="BOC" />
                <el-option label="交通银行" value="BOCOM" />
                <el-option label="招商银行" value="CMB" />
                <el-option label="浦发银行" value="SPDB" />
                <el-option label="中信银行" value="CITIC" />
                <el-option label="光大银行" value="CEB" />
                <el-option label="民生银行" value="CMBC" />
                <el-option label="兴业银行" value="CIB" />
                <el-option label="平安银行" value="PAB" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号">
              <el-input v-model="temp.accountNumber" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="余额限制">
              <el-input-number v-model="temp.balanceLimit" :precision="2" :step="100000" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日限额">
              <el-input-number v-model="temp.dailyLimit" :precision="2" :step="10000" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户状态">
              <el-select v-model="temp.accountStatus" placeholder="请选择" style="width: 100%;">
                <el-option label="激活" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
                <el-option label="冻结" value="FROZEN" />
                <el-option label="关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API端点">
              <el-input v-model="temp.apiEndpoint" placeholder="请输入API地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="电票账户详情" :visible.sync="viewDialogVisible" width="700px">
      <div v-if="currentViewData" class="view-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="view-item"><label>账户编码：</label><span>{{ currentViewData.accountCode }}</span></div>
            <div class="view-item"><label>账户名称：</label><span>{{ currentViewData.accountName }}</span></div>
            <div class="view-item"><label>电票系统：</label><el-tag size="small">{{ getETicketSystemName(currentViewData.eTicketSystem) }}</el-tag></div>
            <div class="view-item"><label>账户类型：</label><el-tag :type="getAccountTypeColor(currentViewData.accountType)" size="small">{{ getAccountTypeName(currentViewData.accountType) }}</el-tag></div>
            <div class="view-item"><label>开户银行：</label><el-tag :type="getBankColor(currentViewData.bankCode)" size="small">{{ currentViewData.bankName || getBankName(currentViewData.bankCode) }}</el-tag></div>
          </el-col>
          <el-col :span="12">
            <div class="view-item"><label>账号：</label><span>{{ currentViewData.accountNumber || '-' }}</span></div>
            <div class="view-item"><label>余额限制：</label><span class="balance-highlight">{{ formatCurrency(currentViewData.balanceLimit) }}</span></div>
            <div class="view-item"><label>日限额：</label><span class="balance-highlight">{{ formatCurrency(currentViewData.dailyLimit) }}</span></div>
            <div class="view-item"><label>账户状态：</label><el-tag :type="getStatusColor(currentViewData.accountStatus)" size="small">{{ getStatusName(currentViewData.accountStatus) }}</el-tag></div>
            <div class="view-item"><label>创建时间：</label><span>{{ formatDate(currentViewData.createTime) }}</span></div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="view-item"><label>API端点：</label><span>{{ currentViewData.apiEndpoint || '未设置' }}</span></div>
          </el-col>
        </el-row>
        <div class="view-item full-width"><label>备注：</label><p>{{ currentViewData.remark || '暂无备注' }}</p></div>
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
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getETicketAccountPage as getETicketAccountList, saveOrUpdateETicketAccount, deleteETicketAccount, syncAccountStatus, getETicketAccountStatistics } from '@/api/globalTreasurer-new/basicConfig/eTicketAccount'
import { isResponseSuccess, handleResponseData, getErrorMessage } from '../../utils'

export default {
  name: 'ETicketAccountConfig',
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
      totalAccounts: 0,
      activeAccounts: 0,
      totalCreditLimit: 0,
      todayTransactions: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        accountCode: undefined,
        accountName: undefined,
        eTicketSystem: undefined,
        accountType: undefined,
        bankCode: undefined,
        accountStatus: undefined
      },
      temp: {
        id: undefined,
        accountCode: '',
        accountName: '',
        eTicketSystem: '',
        accountType: '',
        bankCode: '',
        bankName: '',
        accountNumber: '',
        currency: 'CNY',
        accountStatus: 'ACTIVE',
        balanceLimit: 0,
        dailyLimit: 0,
        apiEndpoint: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑电票账户',
        create: '新增电票账户'
      },
      rules: {
        accountCode: [{ required: true, message: '账户编码是必填项', trigger: 'blur' }],
        accountName: [{ required: true, message: '账户名称是必填项', trigger: 'blur' }],
        eTicketSystem: [{ required: true, message: '电票系统是必选项', trigger: 'change' }],
        bankCode: [{ required: true, message: '开户银行是必选项', trigger: 'change' }]
      },
      viewDialogVisible: false,
      currentViewData: null,
      selectedRows: [],
      tableSettingDialogVisible: false,
      tableColumns: [
        { prop: 'accountCode', label: '账户编码', visible: true, required: true },
        { prop: 'accountName', label: '账户名称', visible: true, required: true },
        { prop: 'eTicketSystem', label: '电票系统', visible: true, required: false },
        { prop: 'accountType', label: '账户类型', visible: true, required: false },
        { prop: 'bankCode', label: '开户银行', visible: true, required: false },
        { prop: 'accountNumber', label: '账号', visible: true, required: false },
        { prop: 'balanceLimit', label: '余额限制', visible: true, required: false },
        { prop: 'dailyLimit', label: '日限额', visible: true, required: false },
        { prop: 'createTime', label: '创建时间', visible: true, required: false },
        { prop: 'accountStatus', label: '账户状态', visible: true, required: false },
        { prop: 'actions', label: '操作', visible: true, required: true }
      ]
    }
  },
  created() {
    this.getList()
    this.loadStatistics()
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
        const response = await getETicketAccountList(this.listQuery)
        if (isResponseSuccess(response)) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('获取电票账户列表失败:', error)
        this.$message.error('获取电票账户列表失败,请检查网络连接或联系管理员')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    async loadStatistics() {
      try {
        const response = await getETicketAccountStatistics()
        if (isResponseSuccess(response)) {
          const stats = response.data || {}
          this.totalAccounts = stats.totalCount || 0
          this.activeAccounts = stats.normalCount || 0
          // totalCreditLimit 后端返回元，转换为万元显示
          this.totalCreditLimit = ((parseFloat(stats.totalCreditLimit) || 0) / 10000).toFixed(2)
          this.todayTransactions = stats.todayCount || 0
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
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
        accountCode: undefined,
        accountName: undefined,
        eTicketSystem: undefined,
        accountType: undefined,
        bankCode: undefined,
        accountStatus: undefined
      }
      this.getList()
    },
    updateStatistics() {
      // 已由 loadStatistics() 从后端统计接口获取，此方法保留兼容调用
      this.loadStatistics()
    },
    async handleSync() {
      try {
        this.listLoading = true
        this.$message.info('正在同步账户状态...')

        // 批量同步所有账户状态
        const ids = this.list.map(item => item.id)
        if (ids.length === 0) {
          this.$message.warning('没有可同步的账户')
          this.listLoading = false
          return
        }

        const response = await syncAccountStatus(ids)
        if (isResponseSuccess(response)) {
          this.$message.success('状态同步成功')
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('状态同步失败:', error)
        this.$message.error('状态同步失败')
      } finally {
        this.listLoading = false
      }
    },
    async handleExport() {
      try {
        const exportData = {
          accounts: this.list,
          exportTime: new Date().toISOString(),
          totalRecords: this.total,
          statistics: {
            totalAccounts: this.totalAccounts,
            activeAccounts: this.activeAccounts,
            totalCreditLimit: this.totalCreditLimit,
            todayTransactions: this.todayTransactions
          }
        }

        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `电票账户配置_${new Date().toLocaleDateString()}.json`
        link.click()
        window.URL.revokeObjectURL(link.href)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        accountCode: '',
        accountName: '',
        eTicketSystem: '',
        accountType: '',
        bankCode: '',
        bankName: '',
        accountNumber: '',
        currency: 'CNY',
        accountStatus: 'ACTIVE',
        balanceLimit: 0,
        dailyLimit: 0,
        apiEndpoint: '',
        remark: ''
      }
    },
    async handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await saveOrUpdateETicketAccount(this.temp)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.dialogFormVisible = false
          this.$message.success('创建成功')
          this.getList()
          this.loadStatistics()
        } else {
          throw new Error(response?.message || '创建失败')
        }
      } catch (error) {
        console.error('创建失败:', error)
        if (error !== 'cancel') {
          this.$message.error(error.message || '创建失败,请检查数据或联系管理员')
        }
      }
    },
    async handleUpdate(row) {
      // 深度复制对象,确保ID字段被正确保留
      this.temp = JSON.parse(JSON.stringify(row))

      // 后端使用 @JsonProperty("id") 注解,所以前端会接收到 id 字段
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async updateData() {
      try {
        if (!this.temp.id) {
          this.$message.error('记录ID丢失，无法更新，请重新选择记录编辑')
          this.dialogFormVisible = false
          return
        }
        await this.$refs['dataForm'].validate()
        const response = await saveOrUpdateETicketAccount(this.temp)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.dialogFormVisible = false
          this.$message.success('更新成功')
          this.getList()
          this.loadStatistics()
        } else {
          throw new Error(response?.message || '更新失败')
        }
      } catch (error) {
        console.error('更新失败:', error)
        if (error !== 'cancel') {
          this.$message.error(error.message || '更新失败，请检查数据或联系管理员')
        }
      }
    },
    async handleDelete(row, index) {
      try {
        if (!row.id) {
          this.$message.error('无法获取记录ID，删除失败')
          return
        }
        const accountId = row.id
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteETicketAccount(accountId)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
          this.loadStatistics()
        } else {
          throw new Error(response?.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error(error.message || '删除失败,请稍后重试')
        }
      }
    },
    // 电票系统名称映射
    getETicketSystemName(system) {
      const systemMap = {
        'ECDS': '电子商业汇票系统(ECDS)',
        'ACDS': '银行承兑汇票系统(ACDS)'
      }
      return systemMap[system] || system
    },
    // 账户类型名称映射
    getAccountTypeName(type) {
      const typeMap = {
        'BASIC': '基本账户',
        'GENERAL': '一般账户',
        'SPECIAL': '专用账户',
        'TEMPORARY': '临时账户'
      }
      return typeMap[type] || type
    },
    // 账户类型颜色映射
    getAccountTypeColor(type) {
      const colorMap = {
        'BASIC': 'primary',
        'GENERAL': 'success',
        'SPECIAL': 'warning',
        'TEMPORARY': 'info'
      }
      return colorMap[type] || 'info'
    },
    // 银行名称映射
    getBankName(bankCode) {
      const bankMap = {
        'ICBC': '工商银行',
        'CCB': '建设银行',
        'ABC': '农业银行',
        'BOC': '中国银行',
        'CMB': '招商银行',
        'SPDB': '浦发银行'
      }
      return bankMap[bankCode] || bankCode
    },
    // 银行颜色映射
    getBankColor(bankCode) {
      const colorMap = {
        'ICBC': 'danger',
        'CCB': 'primary',
        'ABC': 'success',
        'BOC': 'warning',
        'CMB': 'danger',
        'SPDB': 'info'
      }
      return colorMap[bankCode] || 'info'
    },
    // 账户状态名称映射
    getStatusName(status) {
      const statusMap = {
        'ACTIVE': '激活',
        'INACTIVE': '停用',
        'FROZEN': '冻结',
        'CLOSED': '关闭'
      }
      return statusMap[status] || status || '未知'
    },
    // 账户状态颜色映射
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'FROZEN': 'warning',
        'CLOSED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    // 选择银行时自动填充 bankName
    onBankChange(bankCode) {
      this.temp.bankName = this.getBankName(bankCode)
    },
    handleSelectionChange(val) {
      this.selectedRows = val
    },
    handleTableSetting() {
      this.tableSettingDialogVisible = true
    },
    applyTableSetting() {
      this.tableKey = Date.now()
      this.tableSettingDialogVisible = false
      this.$message.success('表格设置已应用')
    },
    formatCurrency(amount) {
      if (amount === null || amount === undefined || amount === '') {
        return '0.00'
      }
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },
    isColumnVisible(prop) {
      const column = this.tableColumns.find(col => col.prop === prop)
      return column ? column.visible : true
    }
  }
}
</script>

<style lang="scss" scoped>
.eticket-account-config {
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

  .account-overview {
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

          &.limit-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.usage-icon {
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
            color: #6b7280;
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .table-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

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

    .account-number {
      font-family: 'Monaco', 'Menlo', monospace;
      font-weight: 500;
      color: #3b82f6;
    }

    .balance-amount {
      font-family: 'Monaco', 'Menlo', monospace;
      font-weight: 600;
      color: #10b981;
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: center;
    }
  }
}

.dialog-footer {
  text-align: right;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}

::v-deep .el-card {
  border-radius: 8px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-table {
  border-radius: 8px;
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

    .balance-highlight {
      font-family: 'Monaco', 'Menlo', monospace;
      font-weight: 600;
      color: #10b981;
      font-size: 16px;
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

.test-content {
  .account-info {
    background-color: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 4px;
    padding: 12px;
    margin-bottom: 16px;

    p {
      margin: 4px 0;
      color: #495057;
    }
  }

  .test-result {
    margin-top: 20px;

    h4 {
      margin: 0 0 12px 0;
      color: #1f2937;
      font-size: 14px;
      font-weight: 500;
    }

    .result-content {
      border-radius: 4px;
      padding: 12px;

      pre {
        margin: 0;
        white-space: pre-wrap;
        word-break: break-all;
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 12px;
      }

      &.success {
        background-color: #f0f9ff;
        border: 1px solid #bfdbfe;
        color: #1e40af;
      }

      &.error {
        background-color: #fef2f2;
        border: 1px solid #fecaca;
        color: #dc2626;
      }
    }
  }
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
</style>
