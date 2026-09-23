<template>
  <div class="account-closing-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-minus"></i>
            销户申请管理
          </h2>
          <p class="page-description">管理银行账户销户申请，包括申请提交、审批流程、销户办理和状态跟踪</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增申请
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

    <!-- 申请统计卡片 -->
    <div class="application-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总申请数</div>
                <div class="card-value">{{ totalApplications }}</div>
                <div class="card-change">份申请</div>
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
                <div class="card-value">{{ pendingApplications }}</div>
                <div class="card-change warning">待处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon approved-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已通过</div>
                <div class="card-value">{{ approvedApplications }}</div>
                <div class="card-change positive">通过率{{ approvalRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rejected-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已拒绝</div>
                <div class="card-value">{{ rejectedApplications }}</div>
                <div class="card-change negative">拒绝率{{ rejectionRate }}%</div>
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
          <el-form-item label="申请编号">
            <el-input
              v-model="listQuery.applicationNo"
              placeholder="请输入申请编号"
              style="width: 150px;"
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
          <el-form-item label="账户名称">
            <el-input
              v-model="listQuery.accountName"
              placeholder="请输入账户名称"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="申请状态">
            <el-select
              v-model="listQuery.applicationStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待审批" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请日期">
            <el-date-picker
              v-model="listQuery.applicationDateRange"
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

    <!-- 申请表格 -->
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
        <el-table-column label="申请ID" prop="applicationId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.applicationId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.applicationNo }}</span>
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
        <el-table-column label="银行" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.bankName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="币种" width="80px" align="center">
          <template slot-scope="{row}">
            <el-tag size="mini">{{ row.currencyCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="余额" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getBalanceClass(row.balance)">{{ formatCurrency(row.balance, row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.applicationStatus)">
              {{ getStatusText(row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicationDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="销户原因" min-width="150px">
          <template slot-scope="{row}">
            <span class="reason-text">{{ row.closingReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.applicationStatus === 'PENDING'" size="mini" type="success" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.applicationStatus === 'PENDING'" size="mini" type="warning" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-if="row.applicationStatus === 'PENDING'" size="mini" type="danger" @click="handleCancel(row,$index)">
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑申请对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要销户的账户" style="width: 100%;" @change="handleAccountChange">
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
        <el-row :gutter="20" v-if="temp.accountId">
          <el-col :span="12">
            <el-form-item label="银行">
              <el-input v-model="temp.bankName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前余额">
              <el-input v-model="temp.balanceDisplay" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="销户原因" prop="closingReason">
          <el-select v-model="temp.closingReason" placeholder="请选择销户原因" style="width: 100%;">
            <el-option label="业务调整" value="BUSINESS_ADJUSTMENT" />
            <el-option label="账户合并" value="ACCOUNT_MERGE" />
            <el-option label="公司注销" value="COMPANY_DISSOLUTION" />
            <el-option label="银行变更" value="BANK_CHANGE" />
            <el-option label="其他原因" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细说明" prop="closingDescription">
          <el-input v-model="temp.closingDescription" type="textarea" :rows="3" placeholder="请详细说明销户原因" />
        </el-form-item>
        <el-form-item label="余额处理方式" prop="balanceHandling" v-if="temp.balance > 0">
          <el-radio-group v-model="temp.balanceHandling">
            <el-radio label="TRANSFER">转入其他账户</el-radio>
            <el-radio label="CASH">提取现金</el-radio>
            <el-radio label="CHECK">开具支票</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="转入账户" v-if="temp.balanceHandling === 'TRANSFER'">
          <el-select v-model="temp.transferAccountId" placeholder="请选择转入账户" style="width: 100%;">
            <el-option
              v-for="account in transferAccounts"
              :key="account.accountId"
              :label="`${account.accountNumber} - ${account.accountName}`"
              :value="account.accountId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="temp.contactPerson" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="temp.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
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

    <!-- 申请详情对话框 -->
    <el-dialog title="销户申请详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentApplication" class="application-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请编号">{{ currentApplication.applicationNo }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getStatusTagType(currentApplication.applicationStatus)">
              {{ getStatusText(currentApplication.applicationStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentApplication.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentApplication.accountName }}</el-descriptions-item>
          <el-descriptions-item label="银行">{{ currentApplication.bankName }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentApplication.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="账户余额">{{ formatCurrency(currentApplication.balance, currentApplication.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="销户原因">{{ getClosingReasonText(currentApplication.closingReason) }}</el-descriptions-item>
          <el-descriptions-item label="余额处理">{{ getBalanceHandlingText(currentApplication.balanceHandling) }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ currentApplication.contactPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentApplication.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentApplication.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="审批日期">{{ currentApplication.approvalDate || '未审批' }}</el-descriptions-item>
          <el-descriptions-item label="预计销户日期">{{ currentApplication.expectedClosingDate || '待确定' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>详细说明</h4>
          <p>{{ currentApplication.closingDescription }}</p>
        </div>
        <div v-if="currentApplication.approvalOpinion" style="margin-top: 20px;">
          <h4>审批意见</h4>
          <p>{{ currentApplication.approvalOpinion }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentApplication && currentApplication.applicationStatus === 'PENDING'" type="success" @click="handleApprove(currentApplication)">
          审批
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="销户申请审批" :visible.sync="dialogApprovalVisible" width="600px">
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
        <el-form-item v-if="approvalForm.result === 'APPROVED'" label="预计销户日期">
          <el-date-picker
            v-model="approvalForm.expectedClosingDate"
            type="date"
            placeholder="选择预计销户日期"
            style="width: 100%;"
          />
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
import { getClosingApplicationPage, createClosingApplication, updateClosingApplication, approveClosingApplication } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountClosingManage',
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
        applicationNo: undefined,
        accountNumber: undefined,
        accountName: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
      },
      totalApplications: 0,
      pendingApplications: 0,
      approvedApplications: 0,
      rejectedApplications: 0,
      approvalRate: 0,
      rejectionRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      transferAccounts: [],
      temp: {
        applicationId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        bankName: '',
        currencyCode: '',
        balance: 0,
        balanceDisplay: '',
        closingReason: '',
        closingDescription: '',
        balanceHandling: '',
        transferAccountId: undefined,
        contactPerson: '',
        contactPhone: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑销户申请',
        create: '新增销户申请'
      },
      currentApplication: null,
      approvalForm: {
        result: '',
        opinion: '',
        expectedClosingDate: null
      },
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        closingReason: [{ required: true, message: '请选择销户原因', trigger: 'change' }],
        closingDescription: [{ required: true, message: '请输入详细说明', trigger: 'blur' }],
        contactPerson: [{ required: true, message: '联系人不能为空', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }]
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
            applicationId: 1,
            applicationNo: 'CLOSE_20240925_001',
            accountId: 1,
            accountNumber: '1234567890123456789',
            accountName: '示例云科技有限公司',
            bankCode: 'ICBC',
            bankName: '工商银行',
            branchName: '北京分行',
            currencyCode: 'CNY',
            balance: 1250.50,
            closingReason: 'BUSINESS_ADJUSTMENT',
            closingDescription: '业务调整，不再需要该账户',
            balanceHandling: 'TRANSFER',
            transferAccountId: 2,
            contactPerson: '张三',
            contactPhone: '13800138000',
            applicationStatus: 'PENDING',
            applicationDate: '2024-09-25',
            approvalDate: null,
            approvalUser: null,
            approvalOpinion: null,
            expectedClosingDate: null,
            actualClosingDate: null,
            remark: '业务调整销户',
            createTime: '2024-09-25 09:30:00'
          },
          {
            applicationId: 2,
            applicationNo: 'CLOSE_20240924_002',
            accountId: 3,
            accountNumber: '9876543210987654321',
            accountName: '示例云投资管理有限公司',
            bankCode: 'CCB',
            bankName: '建设银行',
            branchName: '上海分行',
            currencyCode: 'USD',
            balance: 0,
            closingReason: 'ACCOUNT_MERGE',
            closingDescription: '账户合并，该账户不再使用',
            balanceHandling: 'TRANSFER',
            transferAccountId: 4,
            contactPerson: '李四',
            contactPhone: '13900139000',
            applicationStatus: 'APPROVED',
            applicationDate: '2024-09-24',
            approvalDate: '2024-09-25',
            approvalUser: 1,
            approvalOpinion: '符合销户条件，同意销户',
            expectedClosingDate: '2024-09-30',
            actualClosingDate: null,
            remark: '账户合并销户',
            createTime: '2024-09-24 14:20:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalApplications = 89
      this.pendingApplications = 12
      this.approvedApplications = 65
      this.rejectedApplications = 12
      this.approvalRate = Math.round((this.approvedApplications / this.totalApplications) * 100)
      this.rejectionRate = Math.round((this.rejectedApplications / this.totalApplications) * 100)
    },
    loadAvailableAccounts() {
      // 模拟可用账户数据
      this.availableAccounts = [
        { accountId: 1, accountNumber: '1234567890123456789', accountName: '示例云科技有限公司', balance: 1250.50, currencyCode: 'CNY', bankName: '工商银行' },
        { accountId: 3, accountNumber: '9876543210987654321', accountName: '示例云投资管理有限公司', balance: 0, currencyCode: 'USD', bankName: '建设银行' },
        { accountId: 5, accountNumber: '5555666677778888999', accountName: '示例云贸易有限公司', balance: 50000, currencyCode: 'CNY', bankName: '农业银行' }
      ]
      this.transferAccounts = this.availableAccounts.filter(account => account.accountId !== this.temp.accountId)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        applicationNo: undefined,
        accountNumber: undefined,
        accountName: undefined,
        applicationStatus: undefined,
        applicationDateRange: undefined
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
      this.temp.balanceDisplay = this.formatCurrency(this.temp.balance, this.temp.currencyCode)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentApplication = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      // 跳转到账户详情页面
      this.$router.push({ path: '/globalTreasurer/zhgl/zhxx', query: { accountId: row.accountId } })
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
        this.temp.bankName = account.bankName
        this.temp.currencyCode = account.currencyCode
        this.temp.balance = account.balance
        this.temp.balanceDisplay = this.formatCurrency(account.balance, account.currencyCode)
        this.transferAccounts = this.availableAccounts.filter(acc => acc.accountId !== accountId)
      }
    },
    handleApprove(row) {
      this.currentApplication = row
      this.approvalForm = {
        result: '',
        opinion: '',
        expectedClosingDate: null
      }
      this.dialogApprovalVisible = true
    },
    handleCancel(row, index) {
      this.$confirm('确认取消该销户申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.applicationStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '申请已取消!'
        })
      })
    },
    handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要审批的申请'
        })
        return
      }
      this.$confirm(`确认批量审批选中的${this.multipleSelection.length}个申请?`, '提示', {
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
          this.temp.applicationId = parseInt(Math.random() * 100) + 1024
          this.temp.applicationNo = 'CLOSE_' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + '_' + String(Math.floor(Math.random() * 1000)).padStart(3, '0')
          this.temp.applicationStatus = 'PENDING'
          this.temp.applicationDate = new Date().toISOString().slice(0, 10)
          this.temp.createTime = new Date().toLocaleString()
          this.list.unshift(this.temp)
          this.total = this.list.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '申请创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          const index = this.list.findIndex(v => v.applicationId === this.temp.applicationId)
          this.list.splice(index, 1, tempData)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '申请更新成功'
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
      const index = this.list.findIndex(v => v.applicationId === this.currentApplication.applicationId)
      if (index !== -1) {
        this.list[index].applicationStatus = this.approvalForm.result
        this.list[index].approvalDate = new Date().toISOString().slice(0, 10)
        this.list[index].approvalOpinion = this.approvalForm.opinion
        if (this.approvalForm.result === 'APPROVED' && this.approvalForm.expectedClosingDate) {
          this.list[index].expectedClosingDate = this.approvalForm.expectedClosingDate.toISOString().slice(0, 10)
        }
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
        applicationId: undefined,
        accountId: undefined,
        accountNumber: '',
        accountName: '',
        bankName: '',
        currencyCode: '',
        balance: 0,
        balanceDisplay: '',
        closingReason: '',
        closingDescription: '',
        balanceHandling: '',
        transferAccountId: undefined,
        contactPerson: '',
        contactPhone: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'applicationId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.applicationId - b.applicationId)
      } else {
        this.list.sort((a, b) => b.applicationId - a.applicationId)
      }
    },
    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    getClosingReasonText(reason) {
      const textMap = {
        'BUSINESS_ADJUSTMENT': '业务调整',
        'ACCOUNT_MERGE': '账户合并',
        'COMPANY_DISSOLUTION': '公司注销',
        'BANK_CHANGE': '银行变更',
        'OTHER': '其他原因'
      }
      return textMap[reason] || reason
    },
    getBalanceHandlingText(handling) {
      const textMap = {
        'TRANSFER': '转入其他账户',
        'CASH': '提取现金',
        'CHECK': '开具支票'
      }
      return textMap[handling] || handling
    },
    getBalanceClass(balance) {
      if (balance > 0) {
        return 'positive-balance'
      } else if (balance < 0) {
        return 'negative-balance'
      } else {
        return 'zero-balance'
      }
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
.account-closing-manage {
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
            color: #F56C6C;
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

  .application-overview {
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
          &.approved-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.rejected-icon {
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

  .reason-text {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.4;
  }

  .positive-balance {
    color: #67C23A;
    font-weight: 600;
  }

  .negative-balance {
    color: #F56C6C;
    font-weight: 600;
  }

  .zero-balance {
    color: #909399;
  }

  .application-detail {
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
