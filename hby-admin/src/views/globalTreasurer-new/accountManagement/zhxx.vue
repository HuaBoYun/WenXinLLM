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
  setDefaultAccount,
  exportAccountInfo
} from '@/api/globalTreasurer/zhgl'
import AccountEditDialog from './components/AccountEditDialog'
import AccountDetailDialog from './components/AccountDetailDialog'
import Pagination from '@/components/Pagination'

export default {
  name: 'AccountInfo',
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
        accountStatus: null
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
        // 从 localStorage 获取当前机构 ID
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const orgId = userInfo.currentOrg?.orgid

        // 构建请求参数，确保 orgId 存在
        const params = {
          pageNum: this.queryParams.pageNum,
          pageSize: this.queryParams.pageSize,
          accountNumber: this.queryParams.accountNumber || undefined,
          accountName: this.queryParams.accountName || undefined,
          accountType: this.queryParams.accountType || undefined,
          bankCode: this.queryParams.bankCode || undefined,
          currencyCode: this.queryParams.currencyCode || undefined,
          accountStatus: this.queryParams.accountStatus || undefined,
          orgId: orgId
        }

        const response = await getAccountInfoPage(params)
        console.log('账户信息API响应:', response)

        if (response && response.code === 1) {
          const data = response.data || {}
          this.accountList = data.tlist || data.records || data.list || []
          this.total = parseInt(data.totalRecord || data.total || 0) || this.accountList.length
        } else {
          console.warn('API返回状态异常:', response)
          this.accountList = []
          this.total = 0
        }
      } catch (error) {
        // API调用失败时，显示空数据
        console.error('账户信息API调用失败:', error)
        this.accountList = []
        this.total = 0

        // 显示错误提示
        this.$message({
          message: '获取账户信息失败: ' + (error.message || '网络错误'),
          type: 'error',
          duration: 3000
        })
      } finally {
        this.loading = false
      }
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
        accountStatus: null
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
      // 列表行按钮直接传 row；顶部按钮没有 row，从 accountList 中找选中行
      const targetRow = (row && row.accountId) ? row : this.accountList.find(item => item.accountId === this.ids[0])
      if (!targetRow) {
        this.$message.warning('请先选择要修改的账户')
        return
      }
      this.form = { ...targetRow }
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
    async handleConfirm(formData) {
      try {
        if (this.isEdit) {
          const response = await updateAccount(formData)
          if (response && response.code === 1) {
            this.$message.success('修改成功')
            this.open = false
            this.getList()
          } else {
            this.$message.error(response?.msg || '修改失败')
          }
        } else {
          const response = await createAccount(formData)
          if (response && response.code === 1) {
            this.$message.success('新增成功')
            this.open = false
            this.getList()
          } else {
            this.$message.error(response?.msg || '新增失败')
          }
        }
      } catch (error) {
        console.error('提交账户信息失败:', error)
        this.$modal.msgError(error.message || '操作失败，请稍后重试')
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const accountIds = row && row.accountId ? [row.accountId] : this.ids
      if (!accountIds || accountIds.length === 0) {
        this.$message.warning('请先选择要删除的账户')
        return
      }
      this.$confirm('是否确认删除选中的账户信息？', '提示', { type: 'warning' }).then(async () => {
        try {
          let response
          if (accountIds.length === 1) {
            response = await deleteAccount(accountIds[0])
          } else {
            response = await batchDeleteAccounts(accountIds)
          }
          if (response && response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response?.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败: ' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    async handleExport() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const params = {
          accountNumber: this.queryParams.accountNumber || undefined,
          accountName: this.queryParams.accountName || undefined,
          accountType: this.queryParams.accountType || undefined,
          bankCode: this.queryParams.bankCode || undefined,
          currencyCode: this.queryParams.currencyCode || undefined,
          accountStatus: this.queryParams.accountStatus || undefined,
          orgId: userInfo.currentOrg?.orgid
        }
        const res = await exportAccountInfo(params)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `账户信息_${new Date().toLocaleDateString('zh-CN').replace(/\//g, '')}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    /** 下拉菜单命令处理 */
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'freeze':
          this.handleFreeze(row)
          break
        case 'unfreeze':
          this.handleUnfreeze(row)
          break
        case 'activate':
          this.handleActivate(row)
          break
        case 'close':
          this.handleClose(row)
          break
        case 'setDefault':
          this.handleSetDefault(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    /** 冻结账户 */
    handleFreeze(row) {
      this.$confirm(`确认冻结账户"${row.accountName}"？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await freezeAccount(row.accountId)
          if (res && res.code === 1) {
            this.$message.success('冻结成功')
            this.getList()
          } else {
            this.$message.error(res?.msg || '冻结失败')
          }
        } catch (e) { this.$message.error('操作失败') }
      }).catch(() => {})
    },
    /** 解冻账户 */
    handleUnfreeze(row) {
      this.$confirm(`确认解冻账户"${row.accountName}"？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await unfreezeAccount(row.accountId)
          if (res && res.code === 1) {
            this.$message.success('解冻成功')
            this.getList()
          } else {
            this.$message.error(res?.msg || '解冻失败')
          }
        } catch (e) { this.$message.error('操作失败') }
      }).catch(() => {})
    },
    /** 激活账户 */
    handleActivate(row) {
      this.$confirm(`确认激活账户"${row.accountName}"？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await activateAccount(row.accountId)
          if (res && res.code === 1) {
            this.$message.success('激活成功')
            this.getList()
          } else {
            this.$message.error(res?.msg || '激活失败')
          }
        } catch (e) { this.$message.error('操作失败') }
      }).catch(() => {})
    },
    /** 关闭账户 */
    handleClose(row) {
      this.$confirm(`确认关闭账户"${row.accountName}"？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await closeAccount(row.accountId)
          if (res && res.code === 1) {
            this.$message.success('关闭成功')
            this.getList()
          } else {
            this.$message.error(res?.msg || '关闭失败')
          }
        } catch (e) { this.$message.error('操作失败') }
      }).catch(() => {})
    },
    /** 设置默认账户 */
    handleSetDefault(row) {
      this.$confirm(`确认将账户"${row.accountName}"设为默认账户？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await setDefaultAccount(row.accountId, row.currencyCode, row.orgId)
          if (res && res.code === 1) {
            this.$message.success('设置成功')
            this.getList()
          } else {
            this.$message.error(res?.msg || '设置失败')
          }
        } catch (e) { this.$message.error('操作失败') }
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
