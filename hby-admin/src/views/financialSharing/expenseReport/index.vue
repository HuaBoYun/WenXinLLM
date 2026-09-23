<template>
  <div class="expense-report-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="报销单号" prop="reportNumber">
          <el-input
            v-model="searchForm.reportNumber"
            placeholder="请输入报销单号"
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
        <el-form-item label="报销类型" prop="reportType">
          <el-select
            v-model="searchForm.reportType"
            placeholder="请选择报销类型"
            clearable
            style="width: 150px"
          >
            <el-option label="差旅费" value="TRAVEL" />
            <el-option label="办公费" value="OFFICE" />
            <el-option label="业务招待费" value="ENTERTAINMENT" />
            <el-option label="培训费" value="TRAINING" />
            <el-option label="其他费用" value="OTHER" />
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
            <el-option label="已付款" value="PAID" />
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
      <el-button type="primary" @click="handleAdd">新增报销单</el-button>
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
        <el-table-column prop="reportCode" label="报销单号" width="180" />
        <el-table-column prop="reportTitle" label="报销标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="applicantDeptName" label="部门" width="120" />
        <el-table-column prop="reportType" label="报销类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeColor(scope.row.reportType)" size="small">
              {{ getReportTypeName(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="报销金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reportStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.reportStatus)" size="small">
              {{ getStatusName(scope.row.reportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="approveTime" label="审批时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.approveTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
              v-if="scope.row.reportStatus === 'DRAFT'"
            >
              编辑
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleApprove(scope.row)"
              v-if="scope.row.reportStatus === 'PENDING'"
            >
              审批
            </el-button>
            <el-button
              size="mini"
              type="warning"
              @click="handlePrint(scope.row)"
              v-if="scope.row.reportStatus !== 'DRAFT'"
            >
              打印
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
              v-if="scope.row.reportStatus === 'DRAFT'"
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

    <!-- 报销单详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      @close="handleDialogClose"
    >
      <div class="expense-report-detail">
        <!-- 基本信息 -->
        <el-card class="detail-card" title="基本信息">
          <div slot="header" class="card-header">
            <span>基本信息</span>
          </div>
          <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="报销单号" prop="reportNumber">
                  <el-input v-model="formData.reportNumber" :disabled="isViewMode" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="报销标题" prop="reportTitle">
                  <el-input v-model="formData.reportTitle" :disabled="isViewMode" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="报销类型" prop="reportType">
                  <el-select v-model="formData.reportType" :disabled="isViewMode" style="width: 100%">
                    <el-option label="差旅费" value="TRAVEL" />
                    <el-option label="办公费" value="OFFICE" />
                    <el-option label="业务招待费" value="ENTERTAINMENT" />
                    <el-option label="培训费" value="TRAINING" />
                    <el-option label="其他费用" value="OTHER" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
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
              <el-col :span="8">
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
              <el-col :span="8">
                <el-form-item label="费用发生日期" prop="expenseDate">
                  <el-date-picker
                    v-model="formData.expenseDate"
                    type="date"
                    :disabled="isViewMode"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="报销说明" prop="description">
              <el-input
                v-model="formData.description"
                type="textarea"
                :rows="3"
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 费用明细 -->
        <el-card class="detail-card" title="费用明细">
          <div slot="header" class="card-header">
            <span>费用明细</span>
            <el-button 
              v-if="!isViewMode" 
              type="primary" 
              size="small" 
              @click="handleAddExpenseItem"
            >
              添加明细
            </el-button>
          </div>
          <el-table :data="formData.expenseItems" border>
            <el-table-column prop="expenseItemName" label="费用项目" width="150" />
            <el-table-column prop="expenseAmount" label="费用金额" width="120">
              <template slot-scope="scope">
                <span class="amount">¥{{ formatAmount(scope.row.expenseAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="expenseDate" label="费用日期" width="120" />
            <el-table-column prop="description" label="费用说明" min-width="200" />
            <el-table-column prop="receiptCount" label="发票数量" width="100" />
            <el-table-column label="操作" width="120" v-if="!isViewMode">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleEditExpenseItem(scope.row, scope.$index)">编辑</el-button>
                <el-button size="mini" type="danger" @click="handleDeleteExpenseItem(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="expense-summary">
            <span class="summary-label">费用合计：</span>
            <span class="summary-amount">¥{{ formatAmount(getTotalAmount()) }}</span>
          </div>
        </el-card>

        <!-- 审批记录 -->
        <el-card class="detail-card" title="审批记录" v-if="formData.approvalRecords && formData.approvalRecords.length > 0">
          <el-timeline>
            <el-timeline-item
              v-for="record in formData.approvalRecords"
              :key="record.recordId"
              :timestamp="formatDate(record.approvalTime)"
              :type="getApprovalType(record.approvalResult)"
            >
              <div class="approval-record">
                <div class="approval-header">
                  <span class="approver">{{ record.approver }}</span>
                  <el-tag :type="getApprovalTagType(record.approvalResult)" size="small">
                    {{ record.approvalResultName }}
                  </el-tag>
                </div>
                <div class="approval-comment" v-if="record.approvalComment">
                  {{ record.approvalComment }}
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>

      <div slot="footer" class="dialog-footer" v-if="!isViewMode">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
        <el-button type="success" @click="handleSubmit" :loading="submitLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 费用明细编辑对话框 -->
    <el-dialog
      title="费用明细"
      :visible.sync="expenseItemDialogVisible"
      width="600px"
    >
      <el-form :model="expenseItemForm" :rules="expenseItemRules" ref="expenseItemFormRef" label-width="120px">
        <el-form-item label="费用项目" prop="expenseItemId">
          <el-select v-model="expenseItemForm.expenseItemId" style="width: 100%">
            <el-option 
              v-for="item in expenseItemList" 
              :key="item.itemId" 
              :label="item.itemName" 
              :value="item.itemId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="费用金额" prop="expenseAmount">
          <el-input-number 
            v-model="expenseItemForm.expenseAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="费用日期" prop="expenseDate">
          <el-date-picker
            v-model="expenseItemForm.expenseDate"
            type="date"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="发票数量" prop="receiptCount">
          <el-input-number 
            v-model="expenseItemForm.receiptCount" 
            :min="0" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="费用说明" prop="description">
          <el-input
            v-model="expenseItemForm.description"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="expenseItemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveExpenseItem">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { expenseReportApi } from '@/api/financialSharing/coreBusiness'
import { budgetApi } from '@/api/financialSharing/advancedFeatures'
import { getUserDropdownList, getDepartmentDropdownList, getExpenseItemDropdownList } from '@/api/financialSharing/common'

export default {
  name: 'ExpenseReport',
  data() {
    return {
      loading: false,
      saveLoading: false,
      submitLoading: false,
      tableData: [],
      multipleSelection: [],
      userList: [],
      departmentList: [],
      expenseItemList: [],
      searchForm: {
        reportNumber: '',
        applicant: '',
        reportType: '',
        status: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增报销单',
      isViewMode: false,
      formData: {
        reportId: null,
        reportNumber: '',
        reportTitle: '',
        reportType: '',
        applicantId: '',
        departmentId: '',
        expenseDate: '',
        description: '',
        expenseItems: [],
        approvalRecords: []
      },
      formRules: {
        reportTitle: [
          { required: true, message: '请输入报销标题', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报销类型', trigger: 'change' }
        ],
        applicantId: [
          { required: true, message: '请选择申请人', trigger: 'change' }
        ],
        departmentId: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ],
        expenseDate: [
          { required: true, message: '请选择费用发生日期', trigger: 'change' }
        ]
      },
      expenseItemDialogVisible: false,
      expenseItemForm: {
        expenseItemId: '',
        expenseAmount: 0,
        expenseDate: '',
        receiptCount: 0,
        description: ''
      },
      expenseItemRules: {
        expenseItemId: [
          { required: true, message: '请选择费用项目', trigger: 'change' }
        ],
        expenseAmount: [
          { required: true, message: '请输入费用金额', trigger: 'blur' }
        ],
        expenseDate: [
          { required: true, message: '请选择费用日期', trigger: 'change' }
        ]
      },
      currentExpenseItemIndex: -1
    }
  },
  mounted() {
    this.loadData()
    this.loadDropdownData()
  },
  methods: {
    async loadDropdownData() {
      try {
        const [userRes, deptRes, itemRes] = await Promise.all([
          getUserDropdownList(),
          getDepartmentDropdownList(),
          getExpenseItemDropdownList()
        ])
        if (userRes.code === 1 && userRes.data) {
          this.userList = userRes.data
        }
        if (deptRes.code === 1 && deptRes.data) {
          this.departmentList = deptRes.data
        }
        if (itemRes.code === 1 && itemRes.data) {
          this.expenseItemList = itemRes.data
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
        const response = await expenseReportApi.getList(params)
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
      this.dialogTitle = '新增报销单'
      this.isViewMode = false
      this.formData = {
        reportId: null,
        reportNumber: '',
        reportTitle: '',
        reportType: '',
        applicantId: '',
        departmentId: '',
        expenseDate: '',
        description: '',
        expenseItems: [],
        approvalRecords: []
      }
      this.dialogVisible = true
    },

    async handleView(row) {
      this.dialogTitle = '查看报销单'
      this.isViewMode = true
      try {
        const response = await expenseReportApi.getDetail(row.reportId)
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

    async handleEdit(row) {
      this.dialogTitle = '编辑报销单'
      this.isViewMode = false
      try {
        const response = await expenseReportApi.getDetail(row.reportId)
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

    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await expenseReportApi.save(this.formData)
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
        if (this.formData.expenseItems.length === 0) {
          this.$message.error('请添加费用明细')
          return
        }
        // 预算校验
        const totalAmount = this.formData.expenseItems.reduce((sum, item) => sum + (item.expenseAmount || 0), 0)
        try {
          const budgetCheck = await budgetApi.checkControl({
            businessType: 'EXPENSE_REPORT',
            departmentId: this.formData.departmentId,
            projectId: this.formData.projectId,
            amount: totalAmount,
            currency: this.formData.currency || 'CNY'
          })
          if (budgetCheck.code === 1 && budgetCheck.data && !budgetCheck.data.passed) {
            this.$confirm(
              `预算校验未通过：${budgetCheck.data.message || '超出预算额度'}，是否继续提交？`,
              '预算预警',
              { confirmButtonText: '继续提交', cancelButtonText: '取消', type: 'warning' }
            ).catch(() => { return })
          }
        } catch (budgetError) {
          console.warn('预算校验失败，继续提交流程:', budgetError)
        }
        this.submitLoading = true
        const response = await expenseReportApi.submit(this.formData.reportId, {
          submitComment: '提交报销申请'
        })
        if (response.code === 1) {
          this.$message.success('提交成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '提交失败')
        }
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      } finally {
        this.submitLoading = false
      }
    },

    handleAddExpenseItem() {
      this.expenseItemForm = {
        expenseItemId: '',
        expenseAmount: 0,
        expenseDate: '',
        receiptCount: 0,
        description: ''
      }
      this.currentExpenseItemIndex = -1
      this.expenseItemDialogVisible = true
    },

    handleEditExpenseItem(item, index) {
      this.expenseItemForm = { ...item }
      this.currentExpenseItemIndex = index
      this.expenseItemDialogVisible = true
    },

    handleSaveExpenseItem() {
      this.$refs.expenseItemFormRef.validate((valid) => {
        if (valid) {
          const expenseItem = { ...this.expenseItemForm }
          const selectedItem = this.expenseItemList.find(item => item.itemId === expenseItem.expenseItemId)
          if (selectedItem) {
            expenseItem.expenseItemName = selectedItem.itemName
          }
          
          if (this.currentExpenseItemIndex >= 0) {
            this.$set(this.formData.expenseItems, this.currentExpenseItemIndex, expenseItem)
          } else {
            this.formData.expenseItems.push(expenseItem)
          }
          this.expenseItemDialogVisible = false
        }
      })
    },

    handleDeleteExpenseItem(index) {
      this.formData.expenseItems.splice(index, 1)
    },

    getTotalAmount() {
      return this.formData.expenseItems.reduce((total, item) => total + (item.expenseAmount || 0), 0)
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

    getReportTypeName(reportType) {
      const names = {
        'TRAVEL': '差旅费',
        'OFFICE': '办公费',
        'ENTERTAINMENT': '业务招待费',
        'TRAINING': '培训费',
        'OTHER': '其他费用'
      }
      return names[reportType] || reportType
    },

    getReportTypeColor(reportType) {
      const colors = {
        'TRAVEL': 'primary',
        'OFFICE': 'success',
        'ENTERTAINMENT': 'warning',
        'TRAINING': 'info',
        'OTHER': 'default'
      }
      return colors[reportType] || 'default'
    },

    getStatusName(status) {
      const names = {
        'DRAFT': '草稿',
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'PAID': '已付款',
        'REJECTED': '已拒绝'
      }
      return names[status] || status
    },

    getStatusColor(status) {
      const colors = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'PAID': 'primary',
        'REJECTED': 'danger'
      }
      return colors[status] || 'default'
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
        await this.$confirm('确定要删除该报销单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await expenseReportApi.delete(row.reportId)
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
      // 内联审批 - 直接调用审批API
      this.$prompt('请输入审批意见', '审批报销单', {
        confirmButtonText: '通过',
        cancelButtonText: '驳回',
        distinguishCancelAndClose: true,
        inputType: 'textarea'
      }).then(async ({ value }) => {
        const response = await expenseReportApi.approve(row.reportId, {
          action: 'APPROVE',
          opinion: value || '审批通过'
        })
        if (response.code === 1) {
          // 审批通过 - 占用预算
          try {
            await budgetApi.occupy({
              businessType: 'EXPENSE_REPORT',
              businessId: row.reportId,
              departmentId: row.departmentId,
              projectId: row.projectId,
              amount: row.totalAmount,
              currency: row.currency || 'CNY'
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
          const response = await expenseReportApi.approve(row.reportId, {
            action: 'REJECT',
            opinion: '审批驳回'
          })
          if (response.code === 1) {
            // 审批驳回 - 释放已占用预算
            try {
              await budgetApi.release({
                businessType: 'EXPENSE_REPORT',
                businessId: row.reportId
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

    async handlePrint(row) {
      try {
        const response = await expenseReportApi.getDetail(row.reportId)
        if (response.code === 1 && response.data) {
          const report = response.data
          const printWindow = window.open('', '_blank')
          const html = `
            <!DOCTYPE html>
            <html>
            <head>
              <title>报销单打印 - ${report.reportCode || ''}</title>
              <style>
                body { font-family: "Microsoft YaHei", sans-serif; padding: 40px; max-width: 800px; margin: 0 auto; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .header h1 { margin: 0; font-size: 24px; }
                .info-row { display: flex; justify-content: space-between; margin: 15px 0; border-bottom: 1px solid #eee; padding-bottom: 10px; }
                .label { font-weight: bold; color: #666; width: 120px; flex-shrink: 0; }
                .value { flex: 1; text-align: right; }
                .amount { font-size: 20px; color: #e6a23c; font-weight: bold; }
                .footer { margin-top: 60px; display: flex; justify-content: space-between; }
                .sign-line { border-bottom: 1px solid #333; width: 150px; display: inline-block; }
                @media print { .no-print { display: none !important; } }
              </style>
            </head>
            <body>
              <div class="header">
                <h1>费用报销单</h1>
                <p style="color: #999; margin-top: 8px;">报销单号：${report.reportCode || ''}</p>
              </div>
              <div class="info-row"><span class="label">报销标题</span><span class="value">${report.reportTitle || ''}</span></div>
              <div class="info-row"><span class="label">报销类型</span><span class="value">${this.getReportTypeName(report.reportType)}</span></div>
              <div class="info-row"><span class="label">申请人</span><span class="value">${report.applicantName || ''}</span></div>
              <div class="info-row"><span class="label">所属部门</span><span class="value">${report.applicantDeptName || ''}</span></div>
              <div class="info-row"><span class="label">报销金额</span><span class="value amount">¥${this.formatAmount(report.totalAmount)}</span></div>
              <div class="info-row"><span class="label">报销状态</span><span class="value">${this.getStatusName(report.reportStatus)}</span></div>
              <div class="info-row"><span class="label">报销事由</span><span class="value">${report.reportReason || ''}</span></div>
              <div class="info-row"><span class="label">审批人</span><span class="value">${report.approverName || ''}</span></div>
              <div class="info-row"><span class="label">审批时间</span><span class="value">${this.formatDate(report.approveTime)}</span></div>
              <div class="info-row"><span class="label">提交时间</span><span class="value">${this.formatDate(report.createTime)}</span></div>
              <div class="footer">
                <div><span class="label">申请人签字</span><span class="sign-line"></span></div>
                <div><span class="label">审批人签字</span><span class="sign-line"></span></div>
              </div>
              <div class="no-print" style="text-align: center; margin-top: 40px;">
                <button onclick="window.print()" style="padding: 10px 40px; font-size: 16px; cursor: pointer; background: #409eff; color: #fff; border: none; border-radius: 4px;">打印</button>
              </div>
            </body>
            </html>
          `
          printWindow.document.write(html)
          printWindow.document.close()
        } else {
          this.$message.error('获取报销单信息失败')
        }
      } catch (error) {
        this.$message.error('打印失败：' + error.message)
      }
    },

    handleBatchApprove() {
      // 批量审批 - 调用批量审批API
      const reportIds = this.multipleSelection.map(item => item.reportId)
      this.$confirm(`确定要批量审批选中的 ${reportIds.length} 条报销单吗？`, '批量审批', {
        confirmButtonText: '通过',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const promises = reportIds.map(id => expenseReportApi.approve(id, {
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
      this.$confirm('确定要删除选中的报销单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const reportIds = this.multipleSelection.map(item => item.reportId)
        const response = await expenseReportApi.batchDelete(reportIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      }).catch(() => {})
    },

    handleExport() {
      const params = { ...this.searchForm }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      expenseReportApi.export(params).then(response => {
        if (response.code === 1 && response.data) {
          const blob = new Blob([response.data.content], { type: 'text/csv;charset=utf-8' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = response.data.fileName
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      }).catch(error => {
        this.$message.error('导出失败：' + error.message)
      })
    }
  }
}
</script>

<style scoped>
.expense-report-container {
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

.expense-report-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-card {
  margin-bottom: 20px;
}

.detail-card:last-child {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.expense-summary {
  text-align: right;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #EBEEF5;
}

.summary-label {
  font-size: 16px;
  color: #303133;
}

.summary-amount {
  font-size: 18px;
  font-weight: bold;
  color: #E6A23C;
  margin-left: 10px;
}

.approval-record {
  padding: 10px 0;
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
