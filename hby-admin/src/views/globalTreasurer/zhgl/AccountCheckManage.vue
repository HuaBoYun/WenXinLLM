<template>
  <div class="account-check-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-search"></i>
            账户检查管理
          </h2>
          <p class="page-description">管理银行账户的定期检查和异常监控，包括余额核对、交易验证、状态检查等功能</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增检查
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleBatchCheck">
            批量检查
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 检查统计卡片 -->
    <div class="check-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总检查数</div>
                <div class="card-value">{{ totalChecks }}</div>
                <div class="card-change">次检查</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon success-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">检查通过</div>
                <div class="card-value">{{ passedChecks }}</div>
                <div class="card-change positive">通过率{{ passRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon warning-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">异常检查</div>
                <div class="card-value">{{ abnormalChecks }}</div>
                <div class="card-change warning">需处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon processing-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">检查中</div>
                <div class="card-value">{{ processingChecks }}</div>
                <div class="card-change">进行中</div>
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
          <el-form-item label="检查ID">
            <el-input
              v-model="listQuery.checkId"
              placeholder="请输入检查ID"
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
          <el-form-item label="检查类型">
            <el-select
              v-model="listQuery.checkType"
              placeholder="请选择检查类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="余额核对" value="BALANCE_CHECK" />
              <el-option label="交易验证" value="TRANSACTION_CHECK" />
              <el-option label="状态检查" value="STATUS_CHECK" />
              <el-option label="权限检查" value="PERMISSION_CHECK" />
              <el-option label="合规检查" value="COMPLIANCE_CHECK" />
            </el-select>
          </el-form-item>
          <el-form-item label="检查状态">
            <el-select
              v-model="listQuery.checkStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="检查中" value="PROCESSING" />
              <el-option label="通过" value="PASSED" />
              <el-option label="异常" value="ABNORMAL" />
              <el-option label="失败" value="FAILED" />
            </el-select>
          </el-form-item>
          <el-form-item label="检查日期">
            <el-date-picker
              v-model="listQuery.checkDateRange"
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

    <!-- 检查记录表格 -->
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
        <el-table-column label="检查ID" prop="checkId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.checkId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户号码" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row)">{{ row.accountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getCheckTypeTagType(row.checkType)" size="mini">
              {{ getCheckTypeText(row.checkType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getCheckStatusTagType(row.checkStatus)">
              {{ getCheckStatusText(row.checkStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.checkDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="完成时间" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.completeTime || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查结果" min-width="150px">
          <template slot-scope="{row}">
            <span class="result-text" :class="getResultClass(row.checkStatus)">{{ row.checkResult }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.checkerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.checkStatus === 'PROCESSING'" size="mini" type="warning" @click="handleStop(row)">
              停止
            </el-button>
            <el-button v-if="row.checkStatus === 'ABNORMAL'" size="mini" type="success" @click="handleRecheck(row)">
              重检
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增检查对话框 -->
    <el-dialog title="新增账户检查" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要检查的账户" style="width: 100%;" @change="handleAccountChange">
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
              <el-input v-model="temp.accountNumber" disabled />
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
            <el-form-item label="检查类型" prop="checkType">
              <el-select v-model="temp.checkType" placeholder="请选择检查类型" style="width: 100%;">
                <el-option label="余额核对" value="BALANCE_CHECK" />
                <el-option label="交易验证" value="TRANSACTION_CHECK" />
                <el-option label="状态检查" value="STATUS_CHECK" />
                <el-option label="权限检查" value="PERMISSION_CHECK" />
                <el-option label="合规检查" value="COMPLIANCE_CHECK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查优先级">
              <el-select v-model="temp.checkPriority" placeholder="请选择优先级" style="width: 100%;">
                <el-option label="高" value="HIGH" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="低" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检查描述" prop="checkDescription">
          <el-input v-model="temp.checkDescription" type="textarea" :rows="3" placeholder="请详细描述检查内容和目的" />
        </el-form-item>
        <el-form-item label="检查参数">
          <el-input v-model="temp.checkParameters" type="textarea" :rows="2" placeholder="请输入检查参数（JSON格式）" />
        </el-form-item>
        <el-form-item label="预期完成时间">
          <el-date-picker
            v-model="temp.expectedCompleteTime"
            type="datetime"
            placeholder="选择预期完成时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createData">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 检查详情对话框 -->
    <el-dialog title="检查详情" :visible.sync="dialogDetailVisible" width="900px">
      <div v-if="currentCheck" class="check-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="检查ID">{{ currentCheck.checkId }}</el-descriptions-item>
          <el-descriptions-item label="检查状态">
            <el-tag :type="getCheckStatusTagType(currentCheck.checkStatus)">
              {{ getCheckStatusText(currentCheck.checkStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentCheck.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentCheck.accountName }}</el-descriptions-item>
          <el-descriptions-item label="检查类型">{{ getCheckTypeText(currentCheck.checkType) }}</el-descriptions-item>
          <el-descriptions-item label="检查优先级">{{ getPriorityText(currentCheck.checkPriority) }}</el-descriptions-item>
          <el-descriptions-item label="检查日期">{{ currentCheck.checkDate }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ currentCheck.completeTime || '未完成' }}</el-descriptions-item>
          <el-descriptions-item label="预期完成时间">{{ currentCheck.expectedCompleteTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="检查人">{{ currentCheck.checkerName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentCheck.createTime }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ currentCheck.updateTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>检查描述</h4>
          <p>{{ currentCheck.checkDescription }}</p>
        </div>
        <div v-if="currentCheck.checkParameters" style="margin-top: 20px;">
          <h4>检查参数</h4>
          <pre class="check-parameters">{{ currentCheck.checkParameters }}</pre>
        </div>
        <div style="margin-top: 20px;">
          <h4>检查结果</h4>
          <p :class="getResultClass(currentCheck.checkStatus)">{{ currentCheck.checkResult }}</p>
        </div>
        <div v-if="currentCheck.checkDetails" style="margin-top: 20px;">
          <h4>详细信息</h4>
          <pre class="check-details">{{ currentCheck.checkDetails }}</pre>
        </div>
        <div v-if="currentCheck.remark" style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentCheck.remark }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentCheck && currentCheck.checkStatus === 'PROCESSING'" type="warning" @click="handleStop(currentCheck)">
          停止检查
        </el-button>
        <el-button v-if="currentCheck && currentCheck.checkStatus === 'ABNORMAL'" type="success" @click="handleRecheck(currentCheck)">
          重新检查
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAccountCheckPage, createAccountCheck, stopAccountCheck, recheckAccount, deleteAccountCheck } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountCheckManage',
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
        checkId: undefined,
        accountNumber: undefined,
        checkType: undefined,
        checkStatus: undefined,
        checkDateRange: undefined
      },
      totalChecks: 0,
      passedChecks: 0,
      abnormalChecks: 0,
      processingChecks: 0,
      passRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        checkId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        checkType: '',
        checkPriority: 'MEDIUM',
        checkDescription: '',
        checkParameters: '',
        expectedCompleteTime: null,
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      currentCheck: null,
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        checkType: [{ required: true, message: '请选择检查类型', trigger: 'change' }],
        checkDescription: [{ required: true, message: '请输入检查描述', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
    this.loadAvailableAccounts()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 使用模拟数据
      setTimeout(() => {
        this.list = [
          {
            checkId: 1,
            accountId: 1,
            accountNumber: '1234567890123456789',
            accountName: '示例云科技有限公司',
            checkType: 'BALANCE_CHECK',
            checkStatus: 'PASSED',
            checkPriority: 'HIGH',
            checkDate: '2024-09-25',
            completeTime: '2024-09-25 10:30:00',
            expectedCompleteTime: '2024-09-25 12:00:00',
            checkDescription: '月末余额核对检查',
            checkParameters: '{"checkDate":"2024-09-25","balanceThreshold":1000000}',
            checkResult: '余额核对正常，账户余额与银行系统一致',
            checkDetails: '系统余额：¥5,234,567.89\n银行余额：¥5,234,567.89\n差异：¥0.00',
            checkerName: '系统自动',
            remark: '定期余额核对',
            createTime: '2024-09-25 09:00:00',
            updateTime: '2024-09-25 10:30:00'
          },
          {
            checkId: 2,
            accountId: 2,
            accountNumber: '9876543210987654321',
            accountName: '示例云投资管理有限公司',
            checkType: 'TRANSACTION_CHECK',
            checkStatus: 'ABNORMAL',
            checkPriority: 'HIGH',
            checkDate: '2024-09-24',
            completeTime: '2024-09-24 16:45:00',
            expectedCompleteTime: '2024-09-24 18:00:00',
            checkDescription: '大额交易验证检查',
            checkParameters: '{"transactionAmount":500000,"checkPeriod":"24h"}',
            checkResult: '发现异常交易，存在未授权的大额转账',
            checkDetails: '异常交易ID：TXN20240924001\n交易金额：¥500,000.00\n交易时间：2024-09-24 14:30:00\n状态：未授权',
            checkerName: '风控系统',
            remark: '需要人工审核',
            createTime: '2024-09-24 15:00:00',
            updateTime: '2024-09-24 16:45:00'
          },
          {
            checkId: 3,
            accountId: 3,
            accountNumber: '5555666677778888999',
            accountName: '示例云贸易有限公司',
            checkType: 'STATUS_CHECK',
            checkStatus: 'PROCESSING',
            checkPriority: 'MEDIUM',
            checkDate: '2024-09-25',
            completeTime: null,
            expectedCompleteTime: '2024-09-25 18:00:00',
            checkDescription: '账户状态定期检查',
            checkParameters: '{"checkItems":["accountStatus","permissions","limits"]}',
            checkResult: '检查进行中...',
            checkDetails: null,
            checkerName: '系统自动',
            remark: '定期状态检查',
            createTime: '2024-09-25 16:00:00',
            updateTime: null
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalChecks = 1245
      this.passedChecks = 1089
      this.abnormalChecks = 67
      this.processingChecks = 89
      this.passRate = Math.round((this.passedChecks / this.totalChecks) * 100)
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountNumber: '1234567890123456789', accountName: '示例云科技有限公司' },
        { accountId: 2, accountNumber: '9876543210987654321', accountName: '示例云投资管理有限公司' },
        { accountId: 3, accountNumber: '5555666677778888999', accountName: '示例云贸易有限公司' }
      ]
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        checkId: undefined,
        accountNumber: undefined,
        checkType: undefined,
        checkStatus: undefined,
        checkDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentCheck = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      // 跳转到账户详情页面
      this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { accountId: row.accountId } })
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
      }
    },
    handleStop(row) {
      this.$confirm('确认停止该检查任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.checkStatus = 'FAILED'
        row.completeTime = new Date().toLocaleString()
        row.checkResult = '检查已被手动停止'
        this.$message({
          type: 'success',
          message: '检查已停止!'
        })
      })
    },
    handleRecheck(row) {
      this.$confirm('确认重新检查该账户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.checkStatus = 'PROCESSING'
        row.completeTime = null
        row.checkResult = '重新检查中...'
        row.updateTime = new Date().toLocaleString()
        this.$message({
          type: 'success',
          message: '重新检查已启动!'
        })
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该检查记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.total = this.list.length
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    handleBatchCheck() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要检查的账户'
        })
        return
      }
      this.$confirm(`确认对选中的${this.multipleSelection.length}个账户执行批量检查?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '批量检查已启动!'
        })
        this.getList()
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '检查报告导出成功'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.checkId = parseInt(Math.random() * 100) + 1024
          this.temp.checkStatus = 'PROCESSING'
          this.temp.checkDate = new Date().toISOString().slice(0, 10)
          this.temp.checkResult = '检查进行中...'
          this.temp.checkerName = '当前用户'
          this.temp.createTime = new Date().toLocaleString()
          if (this.temp.expectedCompleteTime) {
            this.temp.expectedCompleteTime = this.temp.expectedCompleteTime.toLocaleString()
          }
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '检查任务创建成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        checkId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        checkType: '',
        checkPriority: 'MEDIUM',
        checkDescription: '',
        checkParameters: '',
        expectedCompleteTime: null,
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'checkId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.checkId - b.checkId)
      } else {
        this.list.sort((a, b) => b.checkId - a.checkId)
      }
    },
    getCheckTypeTagType(checkType) {
      const typeMap = {
        'BALANCE_CHECK': 'primary',
        'TRANSACTION_CHECK': 'danger',
        'STATUS_CHECK': 'success',
        'PERMISSION_CHECK': 'warning',
        'COMPLIANCE_CHECK': 'info'
      }
      return typeMap[checkType] || 'info'
    },
    getCheckTypeText(checkType) {
      const textMap = {
        'BALANCE_CHECK': '余额核对',
        'TRANSACTION_CHECK': '交易验证',
        'STATUS_CHECK': '状态检查',
        'PERMISSION_CHECK': '权限检查',
        'COMPLIANCE_CHECK': '合规检查'
      }
      return textMap[checkType] || checkType
    },
    getCheckStatusTagType(status) {
      const statusMap = {
        'PROCESSING': 'warning',
        'PASSED': 'success',
        'ABNORMAL': 'danger',
        'FAILED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getCheckStatusText(status) {
      const textMap = {
        'PROCESSING': '检查中',
        'PASSED': '通过',
        'ABNORMAL': '异常',
        'FAILED': '失败'
      }
      return textMap[status] || status
    },
    getPriorityText(priority) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[priority] || priority
    },
    getResultClass(status) {
      const classMap = {
        'PASSED': 'result-success',
        'ABNORMAL': 'result-error',
        'FAILED': 'result-warning',
        'PROCESSING': 'result-info'
      }
      return classMap[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.account-check-manage {
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
            color: #909399;
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

  .check-overview {
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
          &.success-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.warning-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.processing-icon {
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

  .result-text {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
    
    &.result-success {
      color: #67C23A;
    }
    &.result-error {
      color: #F56C6C;
    }
    &.result-warning {
      color: #E6A23C;
    }
    &.result-info {
      color: #909399;
    }
  }

  .check-detail {
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
      
      &.result-success {
        color: #67C23A;
      }
      &.result-error {
        color: #F56C6C;
      }
      &.result-warning {
        color: #E6A23C;
      }
      &.result-info {
        color: #909399;
      }
    }
    .check-parameters, .check-details {
      background: #f5f7fa;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 12px;
      font-size: 12px;
      color: #606266;
      white-space: pre-wrap;
      word-break: break-all;
      margin: 0;
    }
  }
}
</style>
