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
          <el-form-item label="第三方系统">
            <el-select
              v-model="listQuery.thirdPartySystem"
              placeholder="请选择第三方系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="银企直连系统" value="BANK_DIRECT" />
              <el-option label="支付宝" value="ALIPAY" />
              <el-option label="微信支付" value="WECHAT_PAY" />
              <el-option label="网银系统" value="ONLINE_BANKING" />
              <el-option label="第三方支付" value="THIRD_PARTY_PAY" />
              <el-option label="ERP系统" value="ERP_SYSTEM" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户类型">
            <el-select
              v-model="listQuery.accountType"
              placeholder="请选择账户类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行账户" value="BANK_ACCOUNT" />
              <el-option label="支付账户" value="PAYMENT_ACCOUNT" />
              <el-option label="API接口账户" value="API_ACCOUNT" />
              <el-option label="系统集成账户" value="SYSTEM_ACCOUNT" />
              <el-option label="虚拟账户" value="VIRTUAL_ACCOUNT" />
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

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="账户编码" prop="accountCode" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.accountCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账户名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.accountName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="第三方系统" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getThirdPartySystemColor(row.thirdPartySystem)" size="small">
            {{ getThirdPartySystemName(row.thirdPartySystem) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账户类型" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountTypeColor(row.accountType)" size="small">
            {{ getAccountTypeName(row.accountType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账户标识" min-width="140" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.accountIdentifier }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接状态" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getConnectionStatusColor(row.connectionStatus)" size="small">
            {{ getConnectionStatusName(row.connectionStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最后同步" min-width="140" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lastSyncTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="600px" @close="resetForm">
      <el-form ref="dataForm" :model="temp" :rules="rules" label-width="100px" class="dialog-form">
        <el-form-item label="账户编码" prop="accountCode">
          <el-input v-model="temp.accountCode" placeholder="请输入账户编码" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="temp.accountName" placeholder="请输入账户名称" />
        </el-form-item>
        <el-form-item label="第三方系统" prop="thirdPartySystem">
          <el-select v-model="temp.thirdPartySystem" placeholder="请选择第三方系统" style="width: 100%;">
            <el-option label="银企直连系统" value="BANK_DIRECT" />
            <el-option label="支付宝" value="ALIPAY" />
            <el-option label="微信支付" value="WECHAT_PAY" />
            <el-option label="网银系统" value="ONLINE_BANKING" />
            <el-option label="第三方支付" value="THIRD_PARTY_PAY" />
            <el-option label="ERP系统" value="ERP_SYSTEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="temp.accountType" placeholder="请选择账户类型" style="width: 100%;">
            <el-option label="银行账户" value="BANK_ACCOUNT" />
            <el-option label="支付账户" value="PAYMENT_ACCOUNT" />
            <el-option label="API接口账户" value="API_ACCOUNT" />
            <el-option label="系统集成账户" value="SYSTEM_ACCOUNT" />
            <el-option label="虚拟账户" value="VIRTUAL_ACCOUNT" />
          </el-select>
        </el-form-item>
        <el-form-item label="账户标识" prop="accountIdentifier">
          <el-input v-model="temp.accountIdentifier" placeholder="请输入账户标识" />
        </el-form-item>
        <el-form-item label="API地址">
          <el-input v-model="temp.apiUrl" placeholder="请输入API接口地址" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="temp.isEnabled" :active-value="1" :inactive-value="0" />
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
import { generateThirdPartyAccountData, handleApiError } from '@/utils/mockData'
import {
  getThirdPartyAccountPage,
  addThirdPartyAccount,
  updateThirdPartyAccount,
  deleteThirdPartyAccount,
  syncThirdPartyAccount
} from '@/api/globalTreasurer-new/basicConfig/securityAndAccount'

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
      listQuery: {
        page: 1,
        limit: 20,
        accountCode: undefined,
        accountName: undefined,
        thirdPartySystem: undefined,
        accountType: undefined,
        connectionStatus: undefined
      },
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: 'create',
      submitLoading: false,
      temp: {
        id: undefined,
        accountCode: '',
        accountName: '',
        thirdPartySystem: '',
        accountType: '',
        accountIdentifier: '',
        apiUrl: '',
        isEnabled: 1,
        remark: ''
      },
      rules: {
        accountCode: [{ required: true, message: '请输入账户编码', trigger: 'blur' }],
        accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
        thirdPartySystem: [{ required: true, message: '请选择第三方系统', trigger: 'change' }],
        accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogStatus === 'create' ? '新增第三方账户' : '编辑第三方账户'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getThirdPartyAccountPage(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('第三方账户管理API调用失败，使用模拟数据:', error)
        const mockResponse = handleApiError(error, generateThirdPartyAccountData, 4)
        this.list = mockResponse.data.tlist
        this.total = mockResponse.data.totalRecord
        this.$message({ message: '当前显示模拟数据，请检查网络连接或联系管理员', type: 'warning', duration: 3000 })
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = { page: 1, limit: 20, accountCode: undefined, accountName: undefined, thirdPartySystem: undefined, accountType: undefined, connectionStatus: undefined }
      this.getList()
    },
    resetTemp() {
      this.temp = { id: undefined, accountCode: '', accountName: '', thirdPartySystem: '', accountType: '', accountIdentifier: '', apiUrl: '', isEnabled: 1, remark: '' }
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
            const api = this.dialogStatus === 'create' ? addThirdPartyAccount : updateThirdPartyAccount
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
        const response = await deleteThirdPartyAccount(row.id)
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
    async handleSync() {
      try {
        const response = await syncThirdPartyAccount()
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.$message.success('同步成功')
          this.getList()
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        console.error('同步失败:', error)
        this.$message.error('同步失败，请稍后重试')
      }
    },
    handleExport() {
      try {
        // 导出当前筛选的数据
        const exportData = this.list.map(item => ({
          账户名称: item.accountName,
          第三方系统: this.getThirdPartySystemName(item.thirdPartySystem),
          账户类型: item.accountType,
          账户号码: item.accountNumber,
          开户行: item.bankName,
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
        link.download = `第三方账户配置_${new Date().getTime()}.json`
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
    // 连接状态名称映射
    getConnectionStatusName(status) {
      const statusMap = {
        'CONNECTED': '已连接',
        'DISCONNECTED': '未连接',
        'ERROR': '连接异常',
        'MAINTENANCE': '维护中'
      }
      return statusMap[status] || status
    },
    // 连接状态颜色映射
    getConnectionStatusColor(status) {
      const colorMap = {
        'CONNECTED': 'success',
        'DISCONNECTED': 'info',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      return colorMap[status] || 'info'
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
          i { margin-right: 12px; font-size: 28px; }
        }
        .page-description { margin: 0; opacity: 0.9; font-size: 14px; }
      }
      .header-right .el-button { margin-left: 12px; }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .dialog-form {
    .el-form-item { margin-bottom: 18px; }
  }
}

::v-deep .el-dialog {
  border-radius: 8px;
  .el-dialog__header {
    border-bottom: 1px solid #ebeef5;
    padding: 15px 20px;
  }
  .el-dialog__body { padding: 20px; }
  .el-dialog__footer {
    border-top: 1px solid #ebeef5;
    padding: 15px 20px;
  }
}
</style>
