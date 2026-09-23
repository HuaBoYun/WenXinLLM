<template>
  <div class="fund-transfer-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-sort"></i>
            资金调拨管理
          </h2>
          <p class="page-description">管理企业内部资金调拨业务，包括调拨申请、审批、执行和监控</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增调拨
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove">
            批量审批
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 调拨统计卡片 -->
    <div class="transfer-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总调拨数</div>
                <div class="card-value">{{ totalTransfers }}</div>
                <div class="card-change">笔调拨</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待审批</div>
                <div class="card-value">{{ pendingTransfers }}</div>
                <div class="card-change warning">待处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon completed-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已完成</div>
                <div class="card-value">{{ completedTransfers }}</div>
                <div class="card-change positive">成功率{{ successRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">调拨金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
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
          <el-form-item label="调拨单号">
            <el-input
              v-model="listQuery.transferNo"
              placeholder="请输入调拨单号"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="调出账户">
            <el-input
              v-model="listQuery.fromAccount"
              placeholder="请输入调出账户"
              style="width: 180px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="调入账户">
            <el-input
              v-model="listQuery.toAccount"
              placeholder="请输入调入账户"
              style="width: 180px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="调拨状态">
            <el-select
              v-model="listQuery.transferStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待审批" value="PENDING" />
              <el-option label="审批中" value="APPROVING" />
              <el-option label="待执行" value="APPROVED" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请日期">
            <el-date-picker
              v-model="listQuery.applyDateRange"
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

    <!-- 调拨表格 -->
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
        <el-table-column label="调拨ID" prop="transferId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.transferId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调拨单号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.transferNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调出账户" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row.fromAccountId)">{{ row.fromAccountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调入账户" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row.toAccountId)">{{ row.toAccountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调拨金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="transfer-amount">{{ formatCurrency(row.transferAmount, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调拨状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.transferStatus)">
              {{ getStatusText(row.transferStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applyDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.executeDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicantName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="调拨原因" min-width="150px">
          <template slot-scope="{row}">
            <span class="reason-text">{{ row.transferReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.transferStatus === 'PENDING'" size="mini" type="success" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.transferStatus === 'APPROVED'" size="mini" type="warning" @click="handleExecute(row)">
              执行
            </el-button>
            <el-button v-if="['PENDING', 'APPROVED'].includes(row.transferStatus)" size="mini" type="danger" @click="handleCancel(row,$index)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增调拨对话框 -->
    <el-dialog title="新增资金调拨" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调出账户" prop="fromAccountId">
              <el-select v-model="temp.fromAccountId" placeholder="请选择调出账户" style="width: 100%;" @change="handleFromAccountChange">
                <el-option
                  v-for="account in availableAccounts"
                  :key="account.accountId"
                  :label="`${account.accountNumber} - ${account.accountName}`"
                  :value="account.accountId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调入账户" prop="toAccountId">
              <el-select v-model="temp.toAccountId" placeholder="请选择调入账户" style="width: 100%;" @change="handleToAccountChange">
                <el-option
                  v-for="account in availableAccounts"
                  :key="account.accountId"
                  :label="`${account.accountNumber} - ${account.accountName}`"
                  :value="account.accountId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="temp.fromAccountId">
          <el-col :span="12">
            <el-form-item label="调出账户余额">
              <el-input v-model="temp.fromAccountBalanceDisplay" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种">
              <el-input v-model="temp.currencyCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调拨金额" prop="transferAmount">
              <el-input-number
                v-model="temp.transferAmount"
                :precision="2"
                :min="0"
                :max="temp.fromAccountBalance"
                style="width: 100%;"
                placeholder="请输入调拨金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预期到账日期">
              <el-date-picker
                v-model="temp.expectedDate"
                type="date"
                placeholder="选择预期到账日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调拨原因" prop="transferReason">
          <el-input v-model="temp.transferReason" type="textarea" :rows="3" placeholder="请详细说明调拨原因" />
        </el-form-item>
        <el-form-item label="申请人" prop="applicantName">
          <el-input v-model="temp.applicantName" placeholder="请输入申请人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="applicantPhone">
          <el-input v-model="temp.applicantPhone" placeholder="请输入申请人联系电话" />
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

    <!-- 调拨详情对话框 -->
    <el-dialog title="调拨详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentTransfer" class="transfer-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="调拨单号">{{ currentTransfer.transferNo }}</el-descriptions-item>
          <el-descriptions-item label="调拨状态">
            <el-tag :type="getStatusTagType(currentTransfer.transferStatus)">
              {{ getStatusText(currentTransfer.transferStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="调出账户">{{ currentTransfer.fromAccountNumber }}</el-descriptions-item>
          <el-descriptions-item label="调入账户">{{ currentTransfer.toAccountNumber }}</el-descriptions-item>
          <el-descriptions-item label="调拨金额">{{ formatCurrency(currentTransfer.transferAmount, currentTransfer.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentTransfer.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentTransfer.applyDate }}</el-descriptions-item>
          <el-descriptions-item label="执行日期">{{ currentTransfer.executeDate || '未执行' }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ currentTransfer.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="审批人">{{ currentTransfer.approverName || '未审批' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>调拨原因</h4>
          <p>{{ currentTransfer.transferReason }}</p>
        </div>
        <div v-if="currentTransfer.approvalOpinion" style="margin-top: 20px;">
          <h4>审批意见</h4>
          <p>{{ currentTransfer.approvalOpinion }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentTransfer && currentTransfer.transferStatus === 'PENDING'" type="success" @click="handleApprove(currentTransfer)">
          审批
        </el-button>
        <el-button v-if="currentTransfer && currentTransfer.transferStatus === 'APPROVED'" type="warning" @click="handleExecute(currentTransfer)">
          执行
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="调拨审批" :visible.sync="dialogApprovalVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" label-width="120px">
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="approvalForm.result">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="opinion">
          <el-input v-model="approvalForm.opinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogApprovalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">确认审批</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundTransferPage, createFundTransfer, approveFundTransfer, executeFundTransfer } from '@/api/globalTreasurer/xjgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'FundTransferManage',
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
        transferNo: undefined,
        fromAccount: undefined,
        toAccount: undefined,
        transferStatus: undefined,
        applyDateRange: undefined
      },
      totalTransfers: 0,
      pendingTransfers: 0,
      completedTransfers: 0,
      successRate: 0,
      totalAmount: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        transferId: undefined,
        fromAccountId: undefined,
        toAccountId: undefined,
        fromAccountNumber: '',
        toAccountNumber: '',
        fromAccountBalance: 0,
        fromAccountBalanceDisplay: '',
        currencyCode: '',
        transferAmount: 0,
        expectedDate: null,
        transferReason: '',
        applicantName: '',
        applicantPhone: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      currentTransfer: null,
      approvalForm: {
        result: '',
        opinion: ''
      },
      rules: {
        fromAccountId: [{ required: true, message: '请选择调出账户', trigger: 'change' }],
        toAccountId: [{ required: true, message: '请选择调入账户', trigger: 'change' }],
        transferAmount: [{ required: true, message: '请输入调拨金额', trigger: 'blur' }],
        transferReason: [{ required: true, message: '请输入调拨原因', trigger: 'blur' }],
        applicantName: [{ required: true, message: '申请人不能为空', trigger: 'blur' }],
        applicantPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }]
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
            transferId: 1,
            transferNo: 'TF20240925001',
            fromAccountId: 1,
            fromAccountNumber: '1234567890123456789',
            fromAccountName: '示例云科技有限公司',
            toAccountId: 2,
            toAccountNumber: '9876543210987654321',
            toAccountName: '示例云投资管理有限公司',
            transferAmount: 500000.00,
            currencyCode: 'CNY',
            transferStatus: 'PENDING',
            applyDate: '2024-09-25',
            executeDate: null,
            expectedDate: '2024-09-26',
            transferReason: '投资项目资金需求',
            applicantName: '张三',
            applicantPhone: '13800138000',
            approverName: null,
            approvalOpinion: null,
            remark: '紧急调拨',
            createTime: '2024-09-25 09:30:00'
          },
          {
            transferId: 2,
            transferNo: 'TF20240924002',
            fromAccountId: 2,
            fromAccountNumber: '9876543210987654321',
            fromAccountName: '示例云投资管理有限公司',
            toAccountId: 3,
            toAccountNumber: '5555666677778888999',
            toAccountName: '示例云贸易有限公司',
            transferAmount: 300000.00,
            currencyCode: 'CNY',
            transferStatus: 'COMPLETED',
            applyDate: '2024-09-24',
            executeDate: '2024-09-25',
            expectedDate: '2024-09-25',
            transferReason: '贸易业务资金补充',
            applicantName: '李四',
            applicantPhone: '13900139000',
            approverName: '王五',
            approvalOpinion: '符合调拨条件，同意执行',
            remark: '常规调拨',
            createTime: '2024-09-24 14:20:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalTransfers = 234
      this.pendingTransfers = 23
      this.completedTransfers = 189
      this.successRate = Math.round((this.completedTransfers / this.totalTransfers) * 100)
      this.totalAmount = 12580.5
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountNumber: '1234567890123456789', accountName: '示例云科技有限公司', balance: 5000000, currencyCode: 'CNY' },
        { accountId: 2, accountNumber: '9876543210987654321', accountName: '示例云投资管理有限公司', balance: 3000000, currencyCode: 'CNY' },
        { accountId: 3, accountNumber: '5555666677778888999', accountName: '示例云贸易有限公司', balance: 2000000, currencyCode: 'CNY' }
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
        transferNo: undefined,
        fromAccount: undefined,
        toAccount: undefined,
        transferStatus: undefined,
        applyDateRange: undefined
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
      this.currentTransfer = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(accountId) {
      // 跳转到账户详情页面
      this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { accountId: accountId } })
    },
    handleFromAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.fromAccountNumber = account.accountNumber
        this.temp.fromAccountBalance = account.balance
        this.temp.fromAccountBalanceDisplay = this.formatCurrency(account.balance, account.currencyCode)
        this.temp.currencyCode = account.currencyCode
      }
    },
    handleToAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.toAccountNumber = account.accountNumber
      }
    },
    handleApprove(row) {
      this.currentTransfer = row
      this.approvalForm = {
        result: '',
        opinion: ''
      }
      this.dialogApprovalVisible = true
    },
    handleExecute(row) {
      this.$confirm('确认执行该调拨申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.transferStatus = 'EXECUTING'
        setTimeout(() => {
          row.transferStatus = 'COMPLETED'
          row.executeDate = new Date().toISOString().slice(0, 10)
        }, 2000)
        this.$message({
          type: 'success',
          message: '调拨执行成功!'
        })
      })
    },
    handleCancel(row, index) {
      this.$confirm('确认取消该调拨申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.transferStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '调拨已取消!'
        })
      })
    },
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要审批的调拨申请'
        })
        return
      }
      this.$confirm(`确认批量审批选中的${this.multipleSelection.length}个调拨申请?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '批量审批成功!'
        })
        this.getList()
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.fromAccountId === this.temp.toAccountId) {
            this.$message({
              type: 'error',
              message: '调出账户和调入账户不能相同'
            })
            return
          }
          this.temp.transferId = parseInt(Math.random() * 100) + 1024
          this.temp.transferNo = 'TF' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + String(Math.floor(Math.random() * 1000)).padStart(3, '0')
          this.temp.transferStatus = 'PENDING'
          this.temp.applyDate = new Date().toISOString().slice(0, 10)
          this.temp.createTime = new Date().toLocaleString()
          if (this.temp.expectedDate) {
            this.temp.expectedDate = this.temp.expectedDate.toISOString().slice(0, 10)
          }
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '调拨申请创建成功'
          })
        }
      })
    },
    submitApproval() {
      if (!this.approvalForm.result) {
        this.$message({
          type: 'warning',
          message: '请选择审批结果'
        })
        return
      }
      if (!this.approvalForm.opinion) {
        this.$message({
          type: 'warning',
          message: '请输入审批意见'
        })
        return
      }
      
      // 更新申请状态
      const index = this.list.findIndex(v => v.transferId === this.currentTransfer.transferId)
      if (index !== -1) {
        this.list[index].transferStatus = this.approvalForm.result
        this.list[index].approverName = '当前用户'
        this.list[index].approvalOpinion = this.approvalForm.opinion
      }
      
      this.dialogApprovalVisible = false
      this.dialogDetailVisible = false
      this.$message({
        type: 'success',
        message: '审批完成'
      })
    },
    resetTemp() {
      this.temp = {
        transferId: undefined,
        fromAccountId: undefined,
        toAccountId: undefined,
        fromAccountNumber: '',
        toAccountNumber: '',
        fromAccountBalance: 0,
        fromAccountBalanceDisplay: '',
        currencyCode: '',
        transferAmount: 0,
        expectedDate: null,
        transferReason: '',
        applicantName: '',
        applicantPhone: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'transferId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.transferId - b.transferId)
      } else {
        this.list.sort((a, b) => b.transferId - a.transferId)
      }
    },
    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVING': 'primary',
        'APPROVED': 'info',
        'EXECUTING': 'warning',
        'COMPLETED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVING': '审批中',
        'APPROVED': '待执行',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    formatCurrency(amount, currency) {
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: currency || 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.fund-transfer-manage {
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

  .transfer-overview {
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
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.completed-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
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

  .transfer-amount {
    font-weight: 600;
    color: #E6A23C;
  }

  .reason-text {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
  }

  .transfer-detail {
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
