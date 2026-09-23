<template>
  <div class="loan-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="借款单号" prop="loanNumber">
          <el-input
            v-model="searchForm.loanNumber"
            placeholder="请输入借款单号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-input
            v-model="searchForm.applicant"
            placeholder="请输入申请人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="借款类型" prop="loanType">
          <el-select
            v-model="searchForm.loanType"
            placeholder="请选择借款类型"
            clearable
            style="width: 150px"
          >
            <el-option label="差旅借款" value="TRAVEL" />
            <el-option label="业务借款" value="BUSINESS" />
            <el-option label="备用金" value="PETTY_CASH" />
            <el-option label="项目借款" value="PROJECT" />
            <el-option label="其他借款" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已放款" value="DISBURSED" />
            <el-option label="已还款" value="REPAID" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期" prop="dateRange">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增借款单</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="loanNumber" label="借款单号" width="180" />
        <el-table-column prop="loanTitle" label="借款标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="departmentName" label="部门" width="120" />
        <el-table-column prop="loanTypeName" label="借款类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getLoanTypeColor(scope.row.loanType)" size="small">
              {{ scope.row.loanTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="loanAmount" label="借款金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.loanAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="repaidAmount" label="已还金额" width="120">
          <template slot-scope="scope">
            <span class="amount repaid">¥{{ formatAmount(scope.row.repaidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="待还金额" width="120">
          <template slot-scope="scope">
            <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="expectedRepayDate" label="预计还款日期" width="120" />
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="mini" 
              type="primary" 
              @click="handleEdit(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              编辑
            </el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleApprove(scope.row)"
              v-if="scope.row.status === 'PENDING'"
            >
              审批
            </el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handleDisburse(scope.row)"
              v-if="scope.row.status === 'APPROVED'"
            >
              放款
            </el-button>
            <el-button 
              size="mini" 
              type="info" 
              @click="handleRepay(scope.row)"
              v-if="scope.row.status === 'DISBURSED' && scope.row.remainingAmount > 0"
            >
              还款
            </el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleDelete(scope.row)"
              v-if="scope.row.status === 'DRAFT'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 借款单详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="借款单号" prop="loanNumber">
              <el-input v-model="formData.loanNumber" :disabled="isViewMode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借款标题" prop="loanTitle">
              <el-input v-model="formData.loanTitle" :disabled="isViewMode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="借款类型" prop="loanType">
              <el-select v-model="formData.loanType" :disabled="isViewMode" style="width: 100%">
                <el-option label="差旅借款" value="TRAVEL" />
                <el-option label="业务借款" value="BUSINESS" />
                <el-option label="备用金" value="PETTY_CASH" />
                <el-option label="项目借款" value="PROJECT" />
                <el-option label="其他借款" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借款金额" prop="loanAmount">
              <el-input-number 
                v-model="formData.loanAmount" 
                :min="0" 
                :precision="2" 
                :disabled="isViewMode"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicantId">
              <el-select v-model="formData.applicantId" :disabled="isViewMode" style="width: 100%">
                <el-option 
                  v-for="user in userList" 
                  :key="user.userId" 
                  :label="user.userName" 
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门" prop="departmentId">
              <el-select v-model="formData.departmentId" :disabled="isViewMode" style="width: 100%">
                <el-option 
                  v-for="dept in departmentList" 
                  :key="dept.deptId" 
                  :label="dept.deptName" 
                  :value="dept.deptId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计还款日期" prop="expectedRepayDate">
              <el-date-picker
                v-model="formData.expectedRepayDate"
                type="date"
                :disabled="isViewMode"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借款用途" prop="loanPurpose">
              <el-select v-model="formData.loanPurpose" :disabled="isViewMode" style="width: 100%">
                <el-option label="出差费用" value="TRAVEL_EXPENSE" />
                <el-option label="业务招待" value="ENTERTAINMENT" />
                <el-option label="采购款项" value="PROCUREMENT" />
                <el-option label="项目支出" value="PROJECT_EXPENSE" />
                <el-option label="其他用途" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="借款原因" prop="loanReason">
          <el-input
            v-model="formData.loanReason"
            type="textarea"
            :rows="3"
            :disabled="isViewMode"
            placeholder="请详细说明借款原因"
          />
        </el-form-item>
        <el-form-item label="还款计划" prop="repaymentPlan">
          <el-input
            v-model="formData.repaymentPlan"
            type="textarea"
            :rows="2"
            :disabled="isViewMode"
            placeholder="请说明还款计划"
          />
        </el-form-item>

        <!-- 借款记录 -->
        <el-form-item label="借款记录" v-if="formData.loanRecords && formData.loanRecords.length > 0">
          <el-table :data="formData.loanRecords" border size="small">
            <el-table-column prop="recordType" label="记录类型" width="100">
              <template slot-scope="scope">
                <el-tag :type="getRecordTypeColor(scope.row.recordType)" size="mini">
                  {{ scope.row.recordTypeName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="120">
              <template slot-scope="scope">
                <span class="amount">¥{{ formatAmount(scope.row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="operationTime" label="操作时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.operationTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="operator" label="操作人" width="100" />
            <el-table-column prop="remark" label="备注" min-width="150" />
          </el-table>
        </el-form-item>

        <!-- 审批记录 -->
        <el-form-item label="审批记录" v-if="formData.approvalRecords && formData.approvalRecords.length > 0">
          <el-timeline size="small">
            <el-timeline-item
              v-for="record in formData.approvalRecords"
              :key="record.recordId"
              :timestamp="formatDate(record.approvalTime)"
              :type="getApprovalType(record.approvalResult)"
            >
              <div class="approval-record">
                <div class="approval-header">
                  <span class="approver">{{ record.approver }}</span>
                  <el-tag :type="getApprovalTagType(record.approvalResult)" size="mini">
                    {{ record.approvalResultName }}
                  </el-tag>
                </div>
                <div class="approval-comment" v-if="record.approvalComment">
                  {{ record.approvalComment }}
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="!isViewMode">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
        <el-button type="success" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 放款对话框 -->
    <el-dialog
      title="借款放款"
      :visible.sync="disburseDialogVisible"
      width="500px"
    >
      <el-form :model="disburseForm" :rules="disburseRules" ref="disburseFormRef" label-width="120px">
        <el-form-item label="放款金额" prop="disburseAmount">
          <el-input-number 
            v-model="disburseForm.disburseAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="放款方式" prop="disburseMethod">
          <el-select v-model="disburseForm.disburseMethod" style="width: 100%">
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="现金支付" value="CASH" />
            <el-option label="支票支付" value="CHECK" />
          </el-select>
        </el-form-item>
        <el-form-item label="银行账户" prop="bankAccount" v-if="disburseForm.disburseMethod === 'BANK_TRANSFER'">
          <el-input v-model="disburseForm.bankAccount" />
        </el-form-item>
        <el-form-item label="放款备注" prop="disburseRemark">
          <el-input
            v-model="disburseForm.disburseRemark"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="disburseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmDisburse" :loading="disburseLoading">确认放款</el-button>
      </div>
    </el-dialog>

    <!-- 还款对话框 -->
    <el-dialog
      title="借款还款"
      :visible.sync="repayDialogVisible"
      width="500px"
    >
      <el-form :model="repayForm" :rules="repayRules" ref="repayFormRef" label-width="120px">
        <el-form-item label="还款金额" prop="repayAmount">
          <el-input-number 
            v-model="repayForm.repayAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="还款方式" prop="repayMethod">
          <el-select v-model="repayForm.repayMethod" style="width: 100%" @change="handleRepayMethodChange">
            <el-option label="现金还款" value="CASH" />
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="报销冲抵" value="EXPENSE_OFFSET" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联报销单" prop="expenseReportId" v-if="repayForm.repayMethod === 'EXPENSE_OFFSET'">
          <el-select v-model="repayForm.expenseReportId" style="width: 100%" filterable placeholder="请选择关联报销单">
            <el-option
              v-for="item in expenseReportList"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="冲抵金额" prop="offsetAmount" v-if="repayForm.repayMethod === 'EXPENSE_OFFSET'">
          <el-input-number
            v-model="repayForm.offsetAmount"
            :min="0"
            :precision="2"
            :max="currentLoanRow ? currentLoanRow.remainingAmount : 0"
            style="width: 100%"
            placeholder="冲抵金额不能超过借款待还金额"
          />
        </el-form-item>
        <el-form-item label="还款凭证" prop="repayVoucher">
          <el-input v-model="repayForm.repayVoucher" />
        </el-form-item>
        <el-form-item label="还款备注" prop="repayRemark">
          <el-input
            v-model="repayForm.repayRemark"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="repayDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmRepay" :loading="repayLoading">确认还款</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { loanApi, expenseReportApi } from '@/api/financialSharing/coreBusiness'
import { budgetApi } from '@/api/financialSharing/advancedFeatures'
import { getUserDropdownList, getDepartmentDropdownList } from '@/api/financialSharing/common'

export default {
  name: 'Loan',
  data() {
    return {
      loading: false,
      saveLoading: false,
      submitLoading: false,
      disburseLoading: false,
      repayLoading: false,
      tableData: [],
      multipleSelection: [],
      userList: [],
      departmentList: [],
      expenseReportList: [],
      searchForm: {
        loanNumber: '',
        applicant: '',
        loanType: '',
        status: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增借款单',
      isViewMode: false,
      formData: {
        loanId: null,
        loanNumber: '',
        loanTitle: '',
        loanType: '',
        loanAmount: 0,
        applicantId: '',
        departmentId: '',
        expectedRepayDate: '',
        loanPurpose: '',
        loanReason: '',
        repaymentPlan: '',
        loanRecords: [],
        approvalRecords: []
      },
      formRules: {
        loanTitle: [
          { required: true, message: '请输入借款标题', trigger: 'blur' }
        ],
        loanType: [
          { required: true, message: '请选择借款类型', trigger: 'change' }
        ],
        loanAmount: [
          { required: true, message: '请输入借款金额', trigger: 'blur' }
        ],
        applicantId: [
          { required: true, message: '请选择申请人', trigger: 'change' }
        ],
        departmentId: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ],
        expectedRepayDate: [
          { required: true, message: '请选择预计还款日期', trigger: 'change' }
        ],
        loanReason: [
          { required: true, message: '请输入借款原因', trigger: 'blur' }
        ]
      },
      disburseDialogVisible: false,
      disburseForm: {
        loanId: '',
        disburseAmount: 0,
        disburseMethod: '',
        bankAccount: '',
        disburseRemark: ''
      },
      disburseRules: {
        disburseAmount: [
          { required: true, message: '请输入放款金额', trigger: 'blur' }
        ],
        disburseMethod: [
          { required: true, message: '请选择放款方式', trigger: 'change' }
        ]
      },
      repayDialogVisible: false,
      repayForm: {
        loanId: '',
        repayAmount: 0,
        repayMethod: '',
        expenseReportId: '',
        offsetAmount: 0,
        repayVoucher: '',
        repayRemark: ''
      },
      repayRules: {
        repayAmount: [
          { required: true, message: '请输入还款金额', trigger: 'blur' }
        ],
        repayMethod: [
          { required: true, message: '请选择还款方式', trigger: 'change' }
        ]
      },
      currentLoanRow: null
    }
  },
  mounted() {
    this.loadData()
    this.loadDropdownData()
  },
  methods: {
    async loadDropdownData() {
      try {
        const [userRes, deptRes] = await Promise.all([
          getUserDropdownList(),
          getDepartmentDropdownList()
        ])
        if (userRes.code === 1 && userRes.data) {
          this.userList = userRes.data
        }
        if (deptRes.code === 1 && deptRes.data) {
          this.departmentList = deptRes.data
        }
      } catch (error) {
        console.error('加载下拉数据失败:', error)
      }
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        const response = await loanApi.getList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    handleAdd() {
      this.dialogTitle = '新增借款单'
      this.isViewMode = false
      this.formData = {
        loanId: null,
        loanNumber: '',
        loanTitle: '',
        loanType: '',
        loanAmount: 0,
        applicantId: '',
        departmentId: '',
        expectedRepayDate: '',
        loanPurpose: '',
        loanReason: '',
        repaymentPlan: '',
        loanRecords: [],
        approvalRecords: []
      }
      this.dialogVisible = true
    },

    async handleView(row) {
      this.dialogTitle = '查看借款单'
      this.isViewMode = true
      try {
        const response = await loanApi.getDetail(row.loanId)
        if (response.code === 1) {
          this.formData = response.data
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      }
    },

    handleEdit(row) {
      this.dialogTitle = '编辑借款单'
      this.isViewMode = false
      this.formData = { ...row }
      this.dialogVisible = true
    },

    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await loanApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },

    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        // 预算校验
        try {
          const budgetCheck = await budgetApi.checkControl({
            businessType: 'LOAN',
            departmentId: this.formData.departmentId,
            amount: this.formData.loanAmount,
            currency: 'CNY'
          })
          if (budgetCheck.code === 1 && budgetCheck.data && !budgetCheck.data.passed) {
            await this.$confirm(
              `预算校验未通过：${budgetCheck.data.message || '超出预算额度'}，是否继续提交？`,
              '预算预警',
              { confirmButtonText: '继续提交', cancelButtonText: '取消', type: 'warning' }
            )
          }
        } catch (budgetError) {
          if (budgetError === 'cancel') return
          console.warn('预算校验失败，继续提交流程:', budgetError)
        }
        this.submitLoading = true
        const response = await loanApi.submit(this.formData.loanId, {
          submitComment: '提交借款申请'
        })
        if (response.code === 1) {
          this.$message.success('提交成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '提交失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    handleDisburse(row) {
      this.currentLoanRow = row
      this.disburseForm = {
        loanId: row.loanId,
        disburseAmount: row.loanAmount,
        disburseMethod: '',
        bankAccount: '',
        disburseRemark: ''
      }
      this.disburseDialogVisible = true
    },

    async handleConfirmDisburse() {
      try {
        await this.$refs.disburseFormRef.validate()
        this.disburseLoading = true
        const response = await loanApi.disburse(this.disburseForm.loanId, this.disburseForm)
        if (response.code === 1) {
          this.$message.success('放款成功')
          this.disburseDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '放款失败')
        }
      } catch (error) {
        this.$message.error('放款失败：' + error.message)
      } finally {
        this.disburseLoading = false
      }
    },

    handleRepay(row) {
      this.currentLoanRow = row
      this.repayForm = {
        loanId: row.loanId,
        repayAmount: row.remainingAmount,
        repayMethod: '',
        expenseReportId: '',
        offsetAmount: 0,
        repayVoucher: '',
        repayRemark: ''
      }
      this.expenseReportList = []
      this.repayDialogVisible = true
    },

    async handleRepayMethodChange(val) {
      this.repayForm.expenseReportId = ''
      this.repayForm.offsetAmount = 0
      if (val === 'EXPENSE_OFFSET') {
        try {
          const res = await expenseReportApi.getList({ pageNum: 0, size: 100, status: 'APPROVED' })
          if (res.code === 1 && res.data) {
            const list = res.data.tlist || []
            this.expenseReportList = list.map(item => ({
              id: item.reportId,
              title: item.reportNumber + ' - ' + (item.reportTitle || ''),
              amount: item.totalAmount || 0
            }))
          }
        } catch (error) {
          console.error('加载报销单列表失败:', error)
          this.$message.error('加载报销单列表失败')
        }
      }
    },

    async handleConfirmRepay() {
      try {
        await this.$refs.repayFormRef.validate()
        // 报销冲抵时校验
        if (this.repayForm.repayMethod === 'EXPENSE_OFFSET') {
          if (!this.repayForm.expenseReportId) {
            this.$message.warning('请选择关联报销单')
            return
          }
          if (!this.repayForm.offsetAmount || this.repayForm.offsetAmount <= 0) {
            this.$message.warning('请输入冲抵金额')
            return
          }
          if (this.currentLoanRow && this.repayForm.offsetAmount > this.currentLoanRow.remainingAmount) {
            this.$message.warning('冲抵金额不能超过借款待还金额')
            return
          }
        }
        this.repayLoading = true
        const response = await loanApi.repay(this.repayForm.loanId, this.repayForm)
        if (response.code === 1) {
          this.$message.success('还款成功')
          this.repayDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '还款失败')
        }
      } catch (error) {
        this.$message.error('还款失败：' + error.message)
      } finally {
        this.repayLoading = false
      }
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    },

    getLoanTypeColor(loanType) {
      const colors = {
        'TRAVEL': 'primary',
        'BUSINESS': 'success',
        'PETTY_CASH': 'warning',
        'PROJECT': 'info',
        'OTHER': 'default'
      }
      return colors[loanType] || 'default'
    },

    getStatusColor(status) {
      const colors = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'DISBURSED': 'primary',
        'REPAID': 'success',
        'REJECTED': 'danger'
      }
      return colors[status] || 'default'
    },

    getRecordTypeColor(recordType) {
      const colors = {
        'DISBURSE': 'success',
        'REPAY': 'primary',
        'MODIFY': 'warning'
      }
      return colors[recordType] || 'default'
    },

    getApprovalType(result) {
      return result === 'APPROVED' ? 'success' : result === 'REJECTED' ? 'danger' : 'primary'
    },

    getApprovalTagType(result) {
      return result === 'APPROVED' ? 'success' : result === 'REJECTED' ? 'danger' : 'primary'
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },

    handleDialogClose() {
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields()
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该借款单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await loanApi.delete(row.loanId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    handleApprove(row) {
      // 内联审批
      this.$prompt('请输入审批意见', '审批借款单', {
        confirmButtonText: '通过',
        cancelButtonText: '驳回',
        distinguishCancelAndClose: true,
        inputType: 'textarea'
      }).then(async ({ value }) => {
        const response = await loanApi.approve(row.loanId, {
          action: 'APPROVE',
          opinion: value || '审批通过'
        })
        if (response.code === 1) {
          // 审批通过 - 占用预算
          try {
            await budgetApi.occupy({
              businessType: 'LOAN',
              businessId: row.loanId,
              departmentId: row.departmentId,
              amount: row.loanAmount,
              currency: 'CNY'
            })
          } catch (budgetError) {
            console.warn('预算占用失败:', budgetError)
          }
          this.$message.success('审批通过')
          this.loadData()
        } else {
          this.$message.error(response.msg || '审批失败')
        }
      }).catch(async (action) => {
        if (action === 'cancel') {
          const response = await loanApi.approve(row.loanId, {
            action: 'REJECT',
            opinion: '审批驳回'
          })
          if (response.code === 1) {
            // 审批驳回 - 释放已占用预算
            try {
              await budgetApi.release({
                businessType: 'LOAN',
                businessId: row.loanId
              })
            } catch (budgetError) {
              console.warn('预算释放失败:', budgetError)
            }
            this.$message.info('已驳回')
            this.loadData()
          } else {
            this.$message.error(response.msg || '驳回失败')
          }
        }
      })
    },

    handleBatchApprove() {
      // 批量审批
      const loanIds = this.multipleSelection.map(item => item.loanId)
      this.$confirm(`确定要批量审批选中的 ${loanIds.length} 条借款单吗？`, '批量审批', {
        confirmButtonText: '通过',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const promises = loanIds.map(id => loanApi.approve(id, {
            action: 'APPROVE',
            opinion: '批量审批通过'
          }))
          const results = await Promise.allSettled(promises)
          const successCount = results.filter(r => r.status === 'fulfilled' && r.value && r.value.code === 1).length
          this.$message.success(`批量审批完成，成功 ${successCount} 条`)
          this.loadData()
        } catch (error) {
          this.$message.error('批量审批失败：' + error.message)
        }
      }).catch(() => {})
    },

    handleBatchDelete() {
      // 批量删除
      this.$confirm('确定要删除选中的借款单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const loanIds = this.multipleSelection.map(item => item.loanId)
        const response = await loanApi.batchDelete(loanIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      }).catch(() => {})
    },

    handleExport() {
      // 导出借款单
      const params = { ...this.searchForm }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      loanApi.export(params).then(response => {
        if (response.code === 1) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.loan-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount {
  font-weight: bold;
  color: #E6A23C;
}

.amount.repaid {
  color: #67C23A;
}

.amount.remaining {
  color: #F56C6C;
}

.approval-record {
  padding: 5px 0;
}

.approval-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.approver {
  font-weight: bold;
  color: #303133;
}

.approval-comment {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.dialog-footer {
  text-align: right;
}
</style>
