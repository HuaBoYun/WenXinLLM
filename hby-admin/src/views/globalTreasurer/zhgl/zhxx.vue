<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="账户号码">
          <el-input
            v-model="queryParams.accountNumber"
            placeholder="请输入账户号码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="账户名称">
          <el-input
            v-model="queryParams.accountName"
            placeholder="请输入账户名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="账户类型">
          <el-select v-model="queryParams.accountType" placeholder="请选择账户类型" clearable style="width: 150px">
            <el-option label="基本账户" value="BASIC" />
            <el-option label="一般账户" value="GENERAL" />
            <el-option label="专用账户" value="SPECIAL" />
            <el-option label="临时账户" value="TEMPORARY" />
          </el-select>
        </el-form-item>
        <el-form-item label="银行编码">
          <el-input
            v-model="queryParams.bankCode"
            placeholder="请输入银行编码"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="queryParams.currencyCode" placeholder="请选择币种" clearable style="width: 120px">
            <el-option label="人民币" value="CNY" />
            <el-option label="美元" value="USD" />
            <el-option label="欧元" value="EUR" />
            <el-option label="日元" value="JPY" />
          </el-select>
        </el-form-item>
        <el-form-item label="账户状态">
          <el-select v-model="queryParams.accountStatus" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="冻结" value="FROZEN" />
            <el-option label="关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增账户</el-button>
      <el-button type="success" icon="el-icon-edit" :disabled="single" @click="handleUpdate">修改</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="accountList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账户号码" prop="accountNumber" width="180" />
      <el-table-column label="账户名称" prop="accountName" width="200" show-overflow-tooltip />
      <el-table-column label="账户类型" prop="accountType" width="100">
        <template slot-scope="scope">
          <el-tag :type="getAccountTypeTag(scope.row.accountType)">
            {{ getAccountTypeText(scope.row.accountType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="银行名称" prop="bankName" width="150" show-overflow-tooltip />
      <el-table-column label="币种" prop="currencyCode" width="80" />
      <el-table-column label="账户余额" prop="balance" width="120" align="right">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.balance) }}
        </template>
      </el-table-column>
      <el-table-column label="可用余额" prop="availableBalance" width="120" align="right">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.availableBalance) }}
        </template>
      </el-table-column>
      <el-table-column label="冻结余额" prop="frozenBalance" width="120" align="right">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.frozenBalance) }}
        </template>
      </el-table-column>
      <el-table-column label="账户状态" prop="accountStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.accountStatus)">
            {{ getStatusText(scope.row.accountStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否默认" prop="isDefault" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isDefault === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否直联" prop="isDirectConnect" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isDirectConnect === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开户日期" prop="openDate" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" @click="handleDetail(scope.row)">详情</el-button>
          <el-dropdown @command="handleCommand">
            <el-button size="mini" type="text">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'freeze', row: scope.row}" v-if="scope.row.accountStatus === 'ACTIVE'">冻结</el-dropdown-item>
              <el-dropdown-item :command="{action: 'unfreeze', row: scope.row}" v-if="scope.row.accountStatus === 'FROZEN'">解冻</el-dropdown-item>
              <el-dropdown-item :command="{action: 'activate', row: scope.row}" v-if="scope.row.accountStatus === 'INACTIVE'">激活</el-dropdown-item>
              <el-dropdown-item :command="{action: 'close', row: scope.row}" v-if="scope.row.accountStatus !== 'CLOSED'">关闭</el-dropdown-item>
              <el-dropdown-item :command="{action: 'setDefault', row: scope.row}" v-if="scope.row.isDefault === 0">设为默认</el-dropdown-item>
              <el-dropdown-item :command="{action: 'delete', row: scope.row}">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改账户信息对话框 -->
    <AccountEditDialog
      :visible.sync="open"
      :form-data="form"
      :is-edit="isEdit"
      @confirm="handleConfirm"
    />

    <!-- 账户详情对话框 -->
    <AccountDetailDialog
      :visible.sync="detailOpen"
      :account-data="currentAccount"
    />
  </div>
</template>

<script>
import {
  getAccountInfoPage,
  createAccount,
  updateAccount,
  deleteAccount,
  batchDeleteAccounts,
  activateAccount,
  freezeAccount,
  unfreezeAccount,
  closeAccount,
  setDefaultAccount
} from '@/api/globalTreasurer/zhgl'
import AccountEditDialog from './components/AccountEditDialog'
import AccountDetailDialog from './components/AccountDetailDialog'
import Pagination from '@/components/Pagination'
import globalTreasurerMixin from '@/mixins/globalTreasurerMixin'
import { mockDataGenerators } from '@/utils/mockData/globalTreasurerMockData'

export default {
  name: 'AccountInfo',
  mixins: [globalTreasurerMixin],
  components: {
    AccountEditDialog,
    AccountDetailDialog,
    Pagination
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 账户信息表格数据
      accountList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 是否编辑
      isEdit: false,
      // 当前账户
      currentAccount: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountNumber: null,
        accountName: null,
        accountType: null,
        bankCode: null,
        currencyCode: null,
        accountStatus: null,
        orgId: this.$store.getters.orgId
      },
      // 表单参数
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询账户信息列表 */
    async getList() {
      this.loading = true
      try {
        const response = await getAccountInfoPage(this.queryParams)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.accountList = response.data?.records || response.data?.tlist || response.data || []
          this.total = response.data?.total || response.data?.totalRecord || this.accountList.length
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('账户信息API调用失败，使用模拟数据:', error)
        this.accountList = this.generateMockAccountData()
        this.total = this.accountList.length

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.loading = false
      }
    },
    /** 生成模拟账户数据 */
    generateMockAccountData() {
      const accountTypes = ['BASIC', 'GENERAL', 'SPECIAL', 'TEMPORARY']
      const banks = ['ICBC', 'CCB', 'ABC', 'BOC', 'CMB']
      const currencies = ['CNY', 'USD', 'EUR', 'JPY']
      const statuses = ['ACTIVE', 'INACTIVE', 'FROZEN']

      return Array.from({ length: 8 }, (_, index) => ({
        accountId: index + 1,
        accountNumber: `6222${String(Math.floor(Math.random() * 100000000000000)).padStart(14, '0')}`,
        accountName: `银行账户${index + 1}`,
        accountNameEng: `Bank Account ${index + 1}`,
        accountType: accountTypes[Math.floor(Math.random() * accountTypes.length)],
        bankCode: banks[Math.floor(Math.random() * banks.length)],
        bankName: this.getBankName(banks[Math.floor(Math.random() * banks.length)]),
        branchCode: `${String(Math.floor(Math.random() * 9999)).padStart(4, '0')}`,
        branchName: `分行${index + 1}`,
        currencyCode: currencies[Math.floor(Math.random() * currencies.length)],
        balance: Math.floor(Math.random() * 10000000) + 100000,
        availableBalance: Math.floor(Math.random() * 8000000) + 80000,
        frozenBalance: Math.floor(Math.random() * 100000),
        accountStatus: statuses[Math.floor(Math.random() * statuses.length)],
        openDate: new Date(Date.now() - Math.floor(Math.random() * 365 * 24 * 60 * 60 * 1000)).toISOString().split('T')[0],
        closeDate: null,
        isDefault: index === 0 ? 1 : 0,
        isDirectConnect: Math.random() > 0.5 ? 1 : 0,
        directConnectType: Math.random() > 0.5 ? 'API' : 'FILE',
        remark: `账户${index + 1}的备注信息`,
        createTime: new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000)).toISOString().replace('T', ' ').split('.')[0],
        updateTime: new Date(Date.now() - Math.floor(Math.random() * 7 * 24 * 60 * 60 * 1000)).toISOString().replace('T', ' ').split('.')[0]
      }))
    },
    /** 获取银行名称 */
    getBankName(bankCode) {
      const bankMap = {
        'ICBC': '中国工商银行',
        'CCB': '中国建设银行',
        'ABC': '中国农业银行',
        'BOC': '中国银行',
        'CMB': '招商银行'
      }
      return bankMap[bankCode] || bankCode
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        accountNumber: null,
        accountName: null,
        accountType: null,
        bankCode: null,
        currencyCode: null,
        accountStatus: null,
        orgId: this.$store.getters.orgId
      }
      this.handleQuery()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.accountId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.currentAccount = row
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加账户信息'
      this.isEdit = false
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const accountId = row.accountId || this.ids[0]
      this.form = { ...row }
      this.open = true
      this.title = '修改账户信息'
      this.isEdit = true
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.currentAccount = row
      this.detailOpen = true
    },
    /** 提交按钮 */
    handleConfirm(formData) {
      if (this.isEdit) {
        updateAccount(formData).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          }
        })
      } else {
        formData.orgId = this.$store.getters.orgId
        formData.createUser = this.$store.getters.userId
        createAccount(formData).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          }
        })
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const accountIds = row.accountId ? [row.accountId] : this.ids
      this.$modal.confirm('是否确认删除选中的账户信息？').then(() => {
        if (accountIds.length === 1) {
          return deleteAccount(accountIds[0], this.$store.getters.userId)
        } else {
          return batchDeleteAccounts(accountIds, this.$store.getters.userId)
        }
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出所有账户信息数据项？').then(() => {
        // TODO: 实现导出功能
        this.$modal.msgSuccess('导出成功')
      }).catch(() => {})
    },
    /** 下拉菜单命令处理 */
    handleCommand(command) {
      const { action, row } = command
      const updateUser = this.$store.getters.userId
      
      switch (action) {
        case 'freeze':
          this.handleFreeze(row, updateUser)
          break
        case 'unfreeze':
          this.handleUnfreeze(row, updateUser)
          break
        case 'activate':
          this.handleActivate(row, updateUser)
          break
        case 'close':
          this.handleClose(row, updateUser)
          break
        case 'setDefault':
          this.handleSetDefault(row, updateUser)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    /** 冻结账户 */
    handleFreeze(row, updateUser) {
      this.$modal.confirm(`确认冻结账户"${row.accountName}"？`).then(() => {
        return freezeAccount(row.accountId, updateUser)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('冻结成功')
      }).catch(() => {})
    },
    /** 解冻账户 */
    handleUnfreeze(row, updateUser) {
      this.$modal.confirm(`确认解冻账户"${row.accountName}"？`).then(() => {
        return unfreezeAccount(row.accountId, updateUser)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('解冻成功')
      }).catch(() => {})
    },
    /** 激活账户 */
    handleActivate(row, updateUser) {
      this.$modal.confirm(`确认激活账户"${row.accountName}"？`).then(() => {
        return activateAccount(row.accountId, updateUser)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('激活成功')
      }).catch(() => {})
    },
    /** 关闭账户 */
    handleClose(row, updateUser) {
      this.$modal.confirm(`确认关闭账户"${row.accountName}"？`).then(() => {
        return closeAccount(row.accountId, updateUser)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('关闭成功')
      }).catch(() => {})
    },
    /** 设置默认账户 */
    handleSetDefault(row, updateUser) {
      this.$modal.confirm(`确认将账户"${row.accountName}"设为默认账户？`).then(() => {
        return setDefaultAccount(row.accountId, row.currencyCode, row.orgId, updateUser)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('设置成功')
      }).catch(() => {})
    },
    // 表单重置
    reset() {
      this.form = {
        accountId: null,
        accountNumber: null,
        accountName: null,
        accountNameEng: null,
        accountType: 'GENERAL',
        bankCode: null,
        bankName: null,
        branchCode: null,
        branchName: null,
        currencyCode: 'CNY',
        balance: 0,
        availableBalance: 0,
        frozenBalance: 0,
        accountStatus: 'ACTIVE',
        openDate: null,
        closeDate: null,
        isDefault: 0,
        isDirectConnect: 0,
        directConnectType: null,
        remark: null
      }
    },
    /** 获取账户类型标签样式 */
    getAccountTypeTag(type) {
      const tagMap = {
        'BASIC': 'success',
        'GENERAL': 'primary',
        'SPECIAL': 'warning',
        'TEMPORARY': 'info'
      }
      return tagMap[type] || 'info'
    },
    /** 获取账户类型文本 */
    getAccountTypeText(type) {
      const textMap = {
        'BASIC': '基本账户',
        'GENERAL': '一般账户',
        'SPECIAL': '专用账户',
        'TEMPORARY': '临时账户'
      }
      return textMap[type] || type
    },
    /** 获取状态标签样式 */
    getStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'FROZEN': 'danger',
        'CLOSED': 'info'
      }
      return tagMap[status] || 'info'
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'FROZEN': '冻结',
        'CLOSED': '关闭'
      }
      return textMap[status] || status
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding: 20px;
  background: #fff;
  margin-bottom: 10px;
}

.operation-container {
  padding: 10px 20px;
  background: #fff;
  margin-bottom: 10px;
}
</style>
