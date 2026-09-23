<template>
  <div class="account-change-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-edit"></i>
            账户变更申请管理
          </h2>
          <p class="page-description">管理银行账户信息变更申请，包括账户名称、联系信息、授权人员等变更申请的审批流程</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增申请
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove">
            批量审批
          </el-button>
          <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">
            批量删除
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
          <el-form-item label="变更类型">
            <el-select
              v-model="listQuery.changeType"
              placeholder="请选择变更类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="账户名称" value="ACCOUNT_NAME" />
              <el-option label="联系信息" value="CONTACT_INFO" />
              <el-option label="授权人员" value="AUTHORIZED_PERSON" />
              <el-option label="印鉴信息" value="SEAL_INFO" />
              <el-option label="其他信息" value="OTHER" />
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
        <el-table-column label="变更类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getChangeTypeTagType(row.changeType)" size="mini">
              {{ getChangeTypeText(row.changeType) }}
            </el-tag>
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
        <el-table-column label="变更原因" min-width="150px">
          <template slot-scope="{row}">
            <span class="reason-text">{{ row.changeReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.applicantName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
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
            <el-button v-if="row.applicationStatus !== 'PENDING'" size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑申请对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要变更的账户" style="width: 100%;" @change="handleAccountChange">
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
            <el-form-item label="当前账户名称">
              <el-input v-model="temp.currentAccountName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="变更类型" prop="changeType">
          <el-select v-model="temp.changeType" placeholder="请选择变更类型" style="width: 100%;" @change="handleChangeTypeChange">
            <el-option label="账户名称" value="ACCOUNT_NAME" />
            <el-option label="联系信息" value="CONTACT_INFO" />
            <el-option label="授权人员" value="AUTHORIZED_PERSON" />
            <el-option label="印鉴信息" value="SEAL_INFO" />
            <el-option label="其他信息" value="OTHER" />
          </el-select>
        </el-form-item>
        
        <!-- 账户名称变更 -->
        <div v-if="temp.changeType === 'ACCOUNT_NAME'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="新账户名称" prop="newAccountName">
                <el-input v-model="temp.newAccountName" placeholder="请输入新的账户名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="新英文名称">
                <el-input v-model="temp.newAccountNameEng" placeholder="请输入新的英文名称" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 联系信息变更 -->
        <div v-if="temp.changeType === 'CONTACT_INFO'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="新联系人" prop="newContactPerson">
                <el-input v-model="temp.newContactPerson" placeholder="请输入新联系人" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="新联系电话" prop="newContactPhone">
                <el-input v-model="temp.newContactPhone" placeholder="请输入新联系电话" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="新联系邮箱">
            <el-input v-model="temp.newContactEmail" placeholder="请输入新联系邮箱" />
          </el-form-item>
          <el-form-item label="新联系地址">
            <el-input v-model="temp.newContactAddress" type="textarea" :rows="2" placeholder="请输入新联系地址" />
          </el-form-item>
        </div>

        <!-- 授权人员变更 -->
        <div v-if="temp.changeType === 'AUTHORIZED_PERSON'">
          <el-form-item label="新授权人员" prop="newAuthorizedPerson">
            <el-input v-model="temp.newAuthorizedPerson" placeholder="请输入新授权人员姓名" />
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="身份证号" prop="newIdCard">
                <el-input v-model="temp.newIdCard" placeholder="请输入身份证号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务">
                <el-input v-model="temp.newPosition" placeholder="请输入职务" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-form-item label="变更原因" prop="changeReason">
          <el-input v-model="temp.changeReason" type="textarea" :rows="3" placeholder="请详细说明变更原因" />
        </el-form-item>
        <el-form-item label="变更详情" prop="changeDetails">
          <el-input v-model="temp.changeDetails" type="textarea" :rows="3" placeholder="请详细描述变更内容" />
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
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 申请详情对话框 -->
    <el-dialog title="变更申请详情" :visible.sync="dialogDetailVisible" width="800px">
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
          <el-descriptions-item label="变更类型">{{ getChangeTypeText(currentApplication.changeType) }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ currentApplication.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ currentApplication.applicationDate }}</el-descriptions-item>
          <el-descriptions-item label="审批日期">{{ currentApplication.approvalDate || '未审批' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>变更原因</h4>
          <p>{{ currentApplication.changeReason }}</p>
        </div>
        <div style="margin-top: 20px;">
          <h4>变更详情</h4>
          <p>{{ currentApplication.changeDetails }}</p>
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
    <el-dialog title="变更申请审批" :visible.sync="dialogApprovalVisible" width="600px">
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
        <el-form-item v-if="approvalForm.result === 'APPROVED'" label="生效日期">
          <el-date-picker
            v-model="approvalForm.effectiveDate"
            type="date"
            placeholder="选择变更生效日期"
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
import { getChangeApplicationPage, createChangeApplication, updateChangeApplication, approveChangeApplication, deleteChangeApplication, batchDeleteChangeApplications, getAccountInfoPage } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'AccountChangeManage',
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
        changeType: undefined,
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
      temp: {
        applicationId: undefined,
        accountId: undefined,
        accountNumber: '',
        currentAccountName: '',
        changeType: '',
        changeReason: '',
        changeDetails: '',
        newAccountName: '',
        newAccountNameEng: '',
        newContactPerson: '',
        newContactPhone: '',
        newContactEmail: '',
        newContactAddress: '',
        newAuthorizedPerson: '',
        newIdCard: '',
        newPosition: '',
        applicantName: '',
        applicantPhone: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑变更申请',
        create: '新增变更申请'
      },
      currentApplication: null,
      approvalForm: {
        result: '',
        opinion: '',
        effectiveDate: null
      },
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        changeType: [{ required: true, message: '请选择变更类型', trigger: 'change' }],
        changeReason: [{ required: true, message: '请输入变更原因', trigger: 'blur' }],
        changeDetails: [{ required: true, message: '请输入变更详情', trigger: 'blur' }],
        applicantName: [{ required: true, message: '申请人不能为空', trigger: 'blur' }],
        applicantPhone: [{ required: true, message: '联系电话不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          orgId: 1,
          applicationNo: this.listQuery.applicationNo,
          accountNumber: this.listQuery.accountNumber,
          changeType: this.listQuery.changeType,
          applicationStatus: this.listQuery.applicationStatus,
          applicationDateRange: this.listQuery.applicationDateRange
        }
        const response = await getChangeApplicationPage(params)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          const data = response.data
          this.list = data.records || data.tlist || data.list || []
          this.total = data.total || data.totalRecord || this.list.length
          // 更新统计卡片数据
          this.totalApplications = data.totalApplications || 0
          this.pendingApplications = data.pendingApplications || 0
          this.approvedApplications = data.approvedApplications || 0
          this.rejectedApplications = data.rejectedApplications || 0
          this.approvalRate = this.totalApplications > 0 ? Math.round((this.approvedApplications / this.totalApplications) * 100) : 0
          this.rejectionRate = this.totalApplications > 0 ? Math.round((this.rejectedApplications / this.totalApplications) * 100) : 0
        } else {
          this.list = []
          this.total = 0
          this.$message.warning(response.msg || response.message || '暂无数据')
        }
      } catch (error) {
        console.error('获取账户变更申请列表失败:', error)
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败：' + (error.message || '网络错误'))
      } finally {
        this.listLoading = false
      }
    },
    async loadAvailableAccounts() {
      try {
        console.log('开始加载账户列表...')
        const response = await getAccountInfoPage({
          pageNum: 1,
          pageSize: 1000
          // 后端会自动使用当前用户的orgId，不需要前端传递
        })

        console.log('账户列表API响应:', response)

        // 支持多种数据结构格式
        let accounts = []
        if (response.data && response.data.records !== undefined) {
          // MyBatis Plus分页格式
          accounts = response.data.records || []
          console.log('使用 records 格式, 总数:', response.data.total)
        } else if (response.data && response.data.tlist !== undefined) {
          // PageInfo格式
          accounts = response.data.tlist || []
          console.log('使用 tlist 格式, 总数:', response.data.totalRecord)
        } else if (response.data && Array.isArray(response.data)) {
          // 数组格式
          accounts = response.data || []
          console.log('使用数组格式, 总数:', accounts.length)
        }

        console.log('解析后的账户列表:', accounts)
        console.log('账户列表长度:', accounts.length)

        this.availableAccounts = accounts.map(acc => ({
          accountId: acc.accountId,
          accountNumber: acc.accountNumber,
          accountName: acc.accountName
        }))

        console.log('availableAccounts:', this.availableAccounts)
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
        applicationNo: undefined,
        accountNumber: undefined,
        changeType: undefined,
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
      this.loadAvailableAccounts()
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({
        newAccountName: '',
        newAccountNameEng: '',
        newContactPerson: '',
        newContactPhone: '',
        newContactEmail: '',
        newContactAddress: '',
        newAuthorizedPerson: '',
        newIdCard: '',
        newPosition: '',
        changeDetails: '',
        applicantPhone: '',
        remark: '',
        currentAccountName: ''
      }, row)
      // currentAccountName 用于页面展示，映射自 accountName
      this.temp.currentAccountName = row.accountName || ''
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.loadAvailableAccounts()
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentApplication = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      this.$message.info(`账户：${row.accountName || row.accountNumber || ''}`)
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
        this.temp.currentAccountName = account.accountName
      }
    },
    handleChangeTypeChange(changeType) {
      // 清空相关字段
      this.temp.newAccountName = ''
      this.temp.newAccountNameEng = ''
      this.temp.newContactPerson = ''
      this.temp.newContactPhone = ''
      this.temp.newContactEmail = ''
      this.temp.newContactAddress = ''
      this.temp.newAuthorizedPerson = ''
      this.temp.newIdCard = ''
      this.temp.newPosition = ''
    },
    handleApprove(row) {
      this.currentApplication = row
      this.approvalForm = {
        result: '',
        opinion: '',
        effectiveDate: null
      }
      this.dialogApprovalVisible = true
    },
    async handleCancel(row, index) {
      this.$confirm('确认取消该变更申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await approveChangeApplication(
            row.applicationId,
            'CANCELLED',
            null,
            '用户主动取消申请'
          )
          if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            this.getList()
            this.$message.success('申请已取消')
          } else {
            this.$message.error(response.msg || response.message || '取消失败')
          }
        } catch (error) {
          console.error('取消申请失败:', error)
          this.$message.error('取消失败：' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },
    async handleBatchApprove() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要审批的申请'
        })
        return
      }

      this.$prompt('请输入批量审批意见（默认：批量通过）', '批量审批', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: '批量通过'
      }).then(async ({ value }) => {
        try {
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

          // 批量审批
          for (const item of this.multipleSelection) {
            const response = await approveChangeApplication(
              item.applicationId,
              'APPROVED',
              userInfo.staffid || null,
              value
            )
            if (![200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
              this.$message.error(`审批申请 ${item.applicationNo} 失败`)
              return
            }
          }

          this.$message.success('批量审批成功')
          this.getList()
        } catch (error) {
          console.error('批量审批失败:', error)
          this.$message.error('批量审批失败：' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },
    handleExport() {
      try {
        // 构建导出参数
        const params = {
          applicationNo: this.listQuery.applicationNo || undefined,
          accountNumber: this.listQuery.accountNumber || undefined,
          changeType: this.listQuery.changeType || undefined,
          applicationStatus: this.listQuery.applicationStatus || undefined
        }

        // 构建查询字符串
        const queryString = Object.keys(params)
          .filter(key => params[key] !== undefined && params[key] !== null && params[key] !== '')
          .map(key => `${key}=${encodeURIComponent(params[key])}`)
          .join('&')

        // 发起导出请求
        const exportUrl = `/qqsk/financial/account-change/export${queryString ? '?' + queryString : ''}`

        // 创建隐藏的下载链接
        const link = document.createElement('a')
        link.href = exportUrl
        link.download = `账户变更申请表_${new Date().getTime()}.xls`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)

        this.$message.success('数据导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败：' + (error.message || '网络错误'))
      }
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await createChangeApplication(this.temp)
            if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
              this.dialogFormVisible = false
              this.getList()
              this.$message.success('申请创建成功')
            } else {
              this.$message.error(response.msg || response.message || '创建失败')
            }
          } catch (error) {
            console.error('创建变更申请失败:', error)
            this.$message.error('创建失败：' + (error.message || '网络错误'))
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await updateChangeApplication(this.temp)
            if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
              this.dialogFormVisible = false
              this.getList()
              this.$message.success('申请更新成功')
            } else {
              this.$message.error(response.msg || response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新变更申请失败:', error)
            this.$message.error('更新失败：' + (error.message || '网络错误'))
          }
        }
      })
    },
    async submitApproval() {
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

      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const response = await approveChangeApplication(
          this.currentApplication.applicationId,
          this.approvalForm.result,
          userInfo.staffid || null,
          this.approvalForm.opinion
        )
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.dialogApprovalVisible = false
          this.dialogDetailVisible = false
          this.getList()
          this.$message.success('审批完成')
        } else {
          this.$message.error(response.msg || response.message || '审批失败')
        }
      } catch (error) {
        console.error('审批失败:', error)
        this.$message.error('审批失败：' + (error.message || '网络错误'))
      }
    },
    async handleDelete(row, index) {
      this.$confirm('确认删除该变更申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteChangeApplication(row.applicationId)
          if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            this.getList()
            this.$message.success('删除成功')
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败：' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },
    async handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择要删除的申请'
        })
        return
      }

      this.$confirm(`确认批量删除选中的${this.multipleSelection.length}个申请?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const applicationIds = this.multipleSelection.map(item => item.applicationId)
          const response = await batchDeleteChangeApplications(applicationIds)
          if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
            this.getList()
            this.$message.success('批量删除成功')
          } else {
            this.$message.error(response.msg || response.message || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败：' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },
    resetTemp() {
      this.temp = {
        applicationId: undefined,
        accountId: undefined,
        accountNumber: '',
        currentAccountName: '',
        changeType: '',
        changeReason: '',
        changeDetails: '',
        newAccountName: '',
        newAccountNameEng: '',
        newContactPerson: '',
        newContactPhone: '',
        newContactEmail: '',
        newContactAddress: '',
        newAuthorizedPerson: '',
        newIdCard: '',
        newPosition: '',
        applicantName: '',
        applicantPhone: '',
        remark: '',
        orgId: 1
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
    getChangeTypeTagType(changeType) {
      const typeMap = {
        'ACCOUNT_NAME': 'primary',
        'CONTACT_INFO': 'success',
        'AUTHORIZED_PERSON': 'warning',
        'SEAL_INFO': 'danger',
        'OTHER': 'info'
      }
      return typeMap[changeType] || 'info'
    },
    getChangeTypeText(changeType) {
      const textMap = {
        'ACCOUNT_NAME': '账户名称',
        'CONTACT_INFO': '联系信息',
        'AUTHORIZED_PERSON': '授权人员',
        'SEAL_INFO': '印鉴信息',
        'OTHER': '其他信息'
      }
      return textMap[changeType] || changeType
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
.account-change-manage {
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
            color: #E6A23C;
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
