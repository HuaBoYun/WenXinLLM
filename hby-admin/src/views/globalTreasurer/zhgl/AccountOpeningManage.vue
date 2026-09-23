<template>
  <div class="account-opening-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-plus"></i>
            开户申请管理
          </h2>
          <p class="page-description">管理银行账户开户申请，包括申请提交、审批流程、开户办理和状态跟踪</p>
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
          <el-form-item label="账户名称">
            <el-input
              v-model="listQuery.accountName"
              placeholder="请输入账户名称"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="银行">
            <el-select
              v-model="listQuery.bankCode"
              placeholder="请选择银行"
              clearable
              style="width: 150px;"
            >
              <el-option label="工商银行" value="ICBC" />
              <el-option label="建设银行" value="CCB" />
              <el-option label="农业银行" value="ABC" />
              <el-option label="中国银行" value="BOC" />
              <el-option label="交通银行" value="BOCOM" />
            </el-select>
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
        <el-table-column label="账户名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户类型" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAccountTypeTagType(row.accountType)" size="mini">
              {{ getAccountTypeText(row.accountType) }}
            </el-tag>
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
        <el-table-column label="联系人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.contactPerson }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系电话" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.contactPhone }}</span>
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="1000px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户名称" prop="accountName">
              <el-input v-model="temp.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户英文名称">
              <el-input v-model="temp.accountNameEng" placeholder="请输入账户英文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="temp.accountType" placeholder="请选择账户类型" style="width: 100%;">
                <el-option label="基本账户" value="BASIC" />
                <el-option label="一般账户" value="GENERAL" />
                <el-option label="专用账户" value="SPECIAL" />
                <el-option label="临时账户" value="TEMPORARY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行" prop="bankCode">
              <el-select v-model="temp.bankCode" placeholder="请选择银行" style="width: 100%;" @change="handleBankChange">
                <el-option label="工商银行" value="ICBC" />
                <el-option label="建设银行" value="CCB" />
                <el-option label="农业银行" value="ABC" />
                <el-option label="中国银行" value="BOC" />
                <el-option label="交通银行" value="BOCOM" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支行">
              <el-select v-model="temp.branchCode" placeholder="请选择支行" style="width: 100%;">
                <el-option label="总行营业部" value="HEAD_OFFICE" />
                <el-option label="北京分行" value="BEIJING" />
                <el-option label="上海分行" value="SHANGHAI" />
                <el-option label="深圳分行" value="SHENZHEN" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表" prop="legalPerson">
              <el-input v-model="temp.legalPerson" placeholder="请输入法人代表姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="temp.contactPerson" placeholder="请输入联系人姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="temp.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系邮箱">
              <el-input v-model="temp.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="营业执照号">
              <el-input v-model="temp.businessLicense" placeholder="请输入营业执照号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织机构代码">
              <el-input v-model="temp.organizationCode" placeholder="请输入组织机构代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="申请原因">
          <el-input v-model="temp.applicationReason" type="textarea" :rows="3" placeholder="请输入申请原因" />
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
    <el-dialog title="申请详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentApplication" class="application-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请编号">{{ currentApplication.applicationNo }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getStatusTagType(currentApplication.applicationStatus)">
              {{ getStatusText(currentApplication.applicationStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentApplication.accountName }}</el-descriptions-item>
          <el-descriptions-item label="账户类型">{{ getAccountTypeText(currentApplication.accountType) }}</el-descriptions-item>
          <el-descriptions-item label="银行">{{ currentApplication.bankName }}</el-descriptions-item>
          <el-descriptions-item label="支行">{{ currentApplication.branchName }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentApplication.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="法人代表">{{ currentApplication.legalPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ currentApplication.contactPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentApplication.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentApplication.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="审批日期">{{ currentApplication.approvalDate || '未审批' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>申请原因</h4>
          <p>{{ currentApplication.applicationReason }}</p>
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
    <el-dialog title="申请审批" :visible.sync="dialogApprovalVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" label-width="100px">
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="approvalForm.result">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="opinion">
          <el-input v-model="approvalForm.opinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item v-if="approvalForm.result === 'APPROVED'" label="账户号码">
          <el-input v-model="approvalForm.accountNumber" placeholder="请输入分配的账户号码" />
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
import { getOpeningApplicationPage, createOpeningApplication, updateOpeningApplication, approveOpeningApplication } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountOpeningManage',
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
        accountName: undefined,
        bankCode: undefined,
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
      temp: {
        applicationId: undefined,
        accountName: '',
        accountNameEng: '',
        accountType: '',
        bankCode: '',
        bankName: '',
        branchCode: '',
        branchName: '',
        currencyCode: '',
        businessLicense: '',
        organizationCode: '',
        taxRegistration: '',
        legalPerson: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        applicationReason: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑开户申请',
        create: '新增开户申请'
      },
      currentApplication: null,
      approvalForm: {
        result: '',
        opinion: '',
        accountNumber: ''
      },
      rules: {
        accountName: [{ required: true, message: '账户名称不能为空', trigger: 'blur' }],
        accountType: [{ required: true, message: '账户类型不能为空', trigger: 'change' }],
        bankCode: [{ required: true, message: '银行不能为空', trigger: 'change' }],
        currencyCode: [{ required: true, message: '币种不能为空', trigger: 'change' }],
        legalPerson: [{ required: true, message: '法人代表不能为空', trigger: 'blur' }],
        contactPerson: [{ required: true, message: '联系人不能为空', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 使用模拟数据
      setTimeout(() => {
        this.list = [
          {
            applicationId: 1,
            applicationNo: 'OPEN_20240925_001',
            accountName: '示例云科技有限公司',
            accountNameEng: 'Huaboyun Technology Co., Ltd.',
            accountType: 'GENERAL',
            bankCode: 'ICBC',
            bankName: '工商银行',
            branchCode: 'BEIJING',
            branchName: '北京分行',
            currencyCode: 'CNY',
            businessLicense: '91110000123456789X',
            organizationCode: '12345678-9',
            legalPerson: '张三',
            contactPerson: '李四',
            contactPhone: '13800138000',
            contactEmail: 'contact@hbyun.com',
            applicationReason: '业务发展需要，申请开立一般存款账户',
            applicationStatus: 'PENDING',
            applicationDate: '2024-09-25',
            approvalDate: null,
            approvalUser: null,
            approvalOpinion: null,
            accountNumber: null,
            remark: '新业务开展需要',
            createTime: '2024-09-25 09:30:00'
          },
          {
            applicationId: 2,
            applicationNo: 'OPEN_20240924_002',
            accountName: '示例云投资管理有限公司',
            accountNameEng: 'Huaboyun Investment Management Co., Ltd.',
            accountType: 'SPECIAL',
            bankCode: 'CCB',
            bankName: '建设银行',
            branchCode: 'SHANGHAI',
            branchName: '上海分行',
            currencyCode: 'USD',
            businessLicense: '91310000987654321Y',
            organizationCode: '98765432-1',
            legalPerson: '王五',
            contactPerson: '赵六',
            contactPhone: '13900139000',
            contactEmail: 'investment@hbyun.com',
            applicationReason: '外汇投资业务需要，申请开立美元专用账户',
            applicationStatus: 'APPROVED',
            applicationDate: '2024-09-24',
            approvalDate: '2024-09-25',
            approvalUser: 1,
            approvalOpinion: '符合开户条件，同意开立',
            accountNumber: '1234567890123456789',
            remark: '投资专用账户',
            createTime: '2024-09-24 14:20:00'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalApplications = 156
      this.pendingApplications = 23
      this.approvedApplications = 108
      this.rejectedApplications = 25
      this.approvalRate = Math.round((this.approvedApplications / this.totalApplications) * 100)
      this.rejectionRate = Math.round((this.rejectedApplications / this.totalApplications) * 100)
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
        accountName: undefined,
        bankCode: undefined,
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
    handleApprove(row) {
      this.currentApplication = row
      this.approvalForm = {
        result: '',
        opinion: '',
        accountNumber: ''
      }
      this.dialogApprovalVisible = true
    },
    handleCancel(row, index) {
      this.$confirm('确认取消该开户申请?', '提示', {
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
          this.temp.applicationNo = 'OPEN_' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + '_' + String(Math.floor(Math.random() * 1000)).padStart(3, '0')
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
        if (this.approvalForm.result === 'APPROVED' && this.approvalForm.accountNumber) {
          this.list[index].accountNumber = this.approvalForm.accountNumber
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
        accountName: '',
        accountNameEng: '',
        accountType: '',
        bankCode: '',
        bankName: '',
        branchCode: '',
        branchName: '',
        currencyCode: '',
        businessLicense: '',
        organizationCode: '',
        taxRegistration: '',
        legalPerson: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        applicationReason: '',
        remark: ''
      }
    },
    handleBankChange(bankCode) {
      const bankMap = {
        'ICBC': '工商银行',
        'CCB': '建设银行',
        'ABC': '农业银行',
        'BOC': '中国银行',
        'BOCOM': '交通银行'
      }
      this.temp.bankName = bankMap[bankCode] || ''
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
    getAccountTypeTagType(accountType) {
      const typeMap = {
        'BASIC': 'primary',
        'GENERAL': 'success',
        'SPECIAL': 'warning',
        'TEMPORARY': 'info'
      }
      return typeMap[accountType] || 'info'
    },
    getAccountTypeText(accountType) {
      const textMap = {
        'BASIC': '基本账户',
        'GENERAL': '一般账户',
        'SPECIAL': '专用账户',
        'TEMPORARY': '临时账户'
      }
      return textMap[accountType] || accountType
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
    }
  }
}
</script>

<style lang="scss" scoped>
.account-opening-manage {
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
