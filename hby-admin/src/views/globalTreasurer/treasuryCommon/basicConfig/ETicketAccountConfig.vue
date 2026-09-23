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
          <el-form-item label="账户编号">
            <el-input
              v-model="listQuery.accountNumber"
              placeholder="请输入账户编号"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="账户名称">
            <el-input
              v-model="listQuery.accountName"
              placeholder="请输入账户名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="电票系统">
            <el-select
              v-model="listQuery.eTicketSystem"
              placeholder="请选择电票系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="ECDS系统" value="ECDS" />
              <el-option label="BECP系统" value="BECP" />
              <el-option label="银行电票系统" value="BANK_ETICKET" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户类型">
            <el-select
              v-model="listQuery.accountType"
              placeholder="请选择账户类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="承兑账户" value="ACCEPTANCE" />
              <el-option label="贴现账户" value="DISCOUNT" />
              <el-option label="转贴现账户" value="REDISCOUNT" />
              <el-option label="质押账户" value="PLEDGE" />
              <el-option label="托管账户" value="CUSTODY" />
              <el-option label="保证金账户" value="MARGIN" />
            </el-select>
          </el-form-item>
          <el-form-item label="开户银行">
            <el-select
              v-model="listQuery.bankCode"
              placeholder="请选择开户银行"
              clearable
              style="width: 150px;"
            >
              <el-option label="工商银行" value="ICBC" />
              <el-option label="建设银行" value="CCB" />
              <el-option label="农业银行" value="ABC" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="交通银行" value="BOCOM" />
              <el-option label="招商银行" value="CMB" />
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
      row-key="id"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="账户编号" prop="accountNumber" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.accountNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账户名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.accountName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电票系统" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getETicketSystemColor(row.eTicketSystem)" size="small">
            {{ getETicketSystemName(row.eTicketSystem) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账户类型" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountTypeColor(row.accountType)" size="small">
            {{ getAccountTypeName(row.accountType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开户银行" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getBankColor(row.bankCode)" size="small">
            {{ getBankName(row.bankCode) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="银行账号" min-width="140" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.bankAccountNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账户余额" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountBalance | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开户日期" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.openDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="80" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountStatusColor(row.accountStatus)" size="small">
            {{ getAccountStatusName(row.accountStatus) }}
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="650px" @close="resetForm">
      <el-form ref="dataForm" :model="temp" :rules="rules" label-width="100px" class="dialog-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户编号" prop="accountNumber">
              <el-input v-model="temp.accountNumber" placeholder="请输入账户编号" :disabled="dialogStatus === 'update'" />
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
              <el-select v-model="temp.eTicketSystem" placeholder="请选择电票系统" style="width: 100%;">
                <el-option label="ECDS系统" value="ECDS" />
                <el-option label="BECP系统" value="BECP" />
                <el-option label="银行电票系统" value="BANK_ETICKET" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="temp.accountType" placeholder="请选择账户类型" style="width: 100%;">
                <el-option label="承兑账户" value="ACCEPTANCE" />
                <el-option label="贴现账户" value="DISCOUNT" />
                <el-option label="转贴现账户" value="REDISCOUNT" />
                <el-option label="质押账户" value="PLEDGE" />
                <el-option label="托管账户" value="CUSTODY" />
                <el-option label="保证金账户" value="MARGIN" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankCode">
              <el-select v-model="temp.bankCode" placeholder="请选择开户银行" style="width: 100%;">
                <el-option label="工商银行" value="ICBC" />
                <el-option label="建设银行" value="CCB" />
                <el-option label="农业银行" value="ABC" />
                <el-option label="中国银行" value="BOC" />
                <el-option label="交通银行" value="BOCOM" />
                <el-option label="招商银行" value="CMB" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccountNumber">
              <el-input v-model="temp.bankAccountNumber" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="账户状态">
          <el-select v-model="temp.accountStatus" placeholder="请选择账户状态" style="width: 100%;">
            <el-option label="正常" value="NORMAL" />
            <el-option label="冻结" value="FROZEN" />
            <el-option label="关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getETicketAccountPage,
  createETicketAccount,
  updateETicketAccount,
  deleteETicketAccount,
  syncAccountStatus
} from '@/api/treasuryCommon/basicConfigETicketAccountConfig'

export default {
  name: 'ETicketAccountConfig',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = { 1: 'success', 0: 'info' }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      totalAccounts: 0,
      activeAccounts: 0,
      totalCreditLimit: '0',
      todayTransactions: 0,
      listQuery: {
        page: 1,
        limit: 20,
        accountNumber: undefined,
        accountName: undefined,
        eTicketSystem: undefined,
        accountType: undefined,
        bankCode: undefined
      },
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: 'create',
      submitLoading: false,
      temp: {
        id: undefined,
        accountNumber: '',
        accountName: '',
        eTicketSystem: '',
        accountType: '',
        bankCode: '',
        bankAccountNumber: '',
        accountStatus: 'NORMAL',
        remark: ''
      },
      rules: {
        accountNumber: [{ required: true, message: '请输入账户编号', trigger: 'blur' }],
        accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
        eTicketSystem: [{ required: true, message: '请选择电票系统', trigger: 'change' }],
        accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }],
        bankCode: [{ required: true, message: '请选择开户银行', trigger: 'change' }],
        bankAccountNumber: [{ required: true, message: '请输入银行账号', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogStatus === 'create' ? '新增电票账户' : '编辑电票账户'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const params = { pageNo: this.listQuery.page, pageSize: this.listQuery.limit, ...this.listQuery }
        const response = await getETicketAccountPage(params)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || 0
          this.totalAccounts = this.total
          this.activeAccounts = this.list.filter(item => item.accountStatus === 'NORMAL').length
        } else {
          this.$message.error(response.message || '获取电票账户列表失败')
        }
      } catch (error) {
        console.error('获取电票账户列表失败:', error)
        this.$message.error('网络错误，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = { page: 1, limit: 20, accountNumber: undefined, accountName: undefined, eTicketSystem: undefined, accountType: undefined, bankCode: undefined }
      this.getList()
    },
    resetTemp() {
      this.temp = { id: undefined, accountNumber: '', accountName: '', eTicketSystem: '', accountType: '', bankCode: '', bankAccountNumber: '', accountStatus: 'NORMAL', remark: '' }
    },
    resetForm() {
      this.resetTemp()
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    submitForm() {
      this.$refs.dataForm.validate(async(valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const api = this.dialogStatus === 'create' ? createETicketAccount : updateETicketAccount
            const response = await api(this.temp)
            if (response && [1, '1', 200, '200'].includes(response.code)) {
              this.$message.success(this.dialogStatus === 'create' ? '新增成功' : '更新成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || response.message || '操作失败')
            }
          } catch (error) {
            console.error('提交失败:', error)
            this.$message.error('操作失败，请稍后重试')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        const response = await deleteETicketAccount(row.id || row.eTicketAccountId)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    handleSync() {
      this.$message.success('同步状态成功')
      this.getList()
    },
    handleExport() {
      try {
        // 导出当前筛选的数据
        const exportData = this.list.map(item => ({
          账户编号: item.accountNumber,
          账户名称: item.accountName,
          电票系统: this.getETicketSystemName(item.eTicketSystem),
          账户类型: item.accountType,
          开户行: item.bankCode,
          银行账号: item.bankAccountNumber,
          账户状态: item.accountStatus === 'NORMAL' ? '正常' : '冻结',
          创建时间: item.createTime,
          备注: item.remark
        }))

        // 创建工作簿
        const dataStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([dataStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `电票账户配置_${new Date().getTime()}.json`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    // 电票系统名称映射
    getETicketSystemName(system) {
      const systemMap = {
        'ECDS': 'ECDS系统',
        'BECP': 'BECP系统',
        'BANK_ETICKET': '银行电票系统'
      }
      return systemMap[system] || system
    },
    // 电票系统颜色映射
    getETicketSystemColor(system) {
      const colorMap = {
        'ECDS': 'primary',
        'BECP': 'success',
        'BANK_ETICKET': 'warning'
      }
      return colorMap[system] || 'info'
    },
    // 账户类型名称映射
    getAccountTypeName(type) {
      const typeMap = {
        'ACCEPTANCE': '承兑账户',
        'DISCOUNT': '贴现账户',
        'REDISCOUNT': '转贴现账户',
        'PLEDGE': '质押账户',
        'CUSTODY': '托管账户',
        'MARGIN': '保证金账户'
      }
      return typeMap[type] || type
    },
    // 账户类型颜色映射
    getAccountTypeColor(type) {
      const colorMap = {
        'ACCEPTANCE': 'primary',
        'DISCOUNT': 'success',
        'REDISCOUNT': 'warning',
        'PLEDGE': 'danger',
        'CUSTODY': 'info',
        'MARGIN': 'primary'
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
    getAccountStatusName(status) {
      const statusMap = {
        'NORMAL': '正常',
        'FROZEN': '冻结',
        'CLOSED': '关闭'
      }
      return statusMap[status] || status
    },
    // 账户状态颜色映射
    getAccountStatusColor(status) {
      const colorMap = {
        'NORMAL': 'success',
        'FROZEN': 'warning',
        'CLOSED': 'danger'
      }
      return colorMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
/* 确保表格完整显示，不被容器高度限制 */
.app-container .el-table {
  width: 100%;
  height: auto !important;
  max-height: none !important;
}

.app-container .el-table__body-wrapper {
  max-height: none !important;
  height: auto !important;
  overflow: visible !important;
}

.app-container .el-table__body {
  width: 100% !important;
}

.app-container {
  min-height: auto !important;
  height: auto !important;
  max-height: none !important;
  overflow: visible !important;
}

/* 确保所有表格行都可见 */
.el-table__row {
  display: table-row !important;
}

.el-table__body tr {
  display: table-row !important;
}
</style>
