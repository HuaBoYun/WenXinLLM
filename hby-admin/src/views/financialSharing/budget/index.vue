<template>
  <div class="budget-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="预算名称" prop="budgetName">
          <el-input
            v-model="searchForm.budgetName"
            placeholder="请输入预算名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="预算类型" prop="budgetType">
          <el-select
            v-model="searchForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option label="部门预算" value="DEPARTMENT" />
            <el-option label="项目预算" value="PROJECT" />
            <el-option label="费用类型预算" value="EXPENSE_TYPE" />
            <el-option label="年度预算" value="ANNUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度" prop="budgetYear">
          <el-select
            v-model="searchForm.budgetYear"
            placeholder="请选择年度"
            clearable
            style="width: 120px"
          >
            <el-option label="2024" value="2024" />
            <el-option label="2025" value="2025" />
            <el-option label="2026" value="2026" />
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
            <el-option label="执行中" value="EXECUTING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-select
            v-model="searchForm.departmentId"
            placeholder="请选择部门"
            clearable
            style="width: 150px"
          >
            <el-option 
              v-for="dept in departmentList" 
              :key="dept.deptId" 
              :label="dept.deptName" 
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增预算</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="info" @click="handleBudgetAnalysis">预算分析</el-button>
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
        <el-table-column prop="budgetName" label="预算名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="budgetTypeName" label="预算类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getBudgetTypeColor(scope.row.budgetType)" size="small">
              {{ scope.row.budgetTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="budgetYear" label="预算年度" width="100" />
        <el-table-column prop="departmentName" label="所属部门" width="120" />
        <el-table-column prop="budgetAmount" label="预算金额" width="120">
          <template slot-scope="scope">
            <span class="amount">¥{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="usedAmount" label="已使用" width="120">
          <template slot-scope="scope">
            <span class="amount used">¥{{ formatAmount(scope.row.usedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="剩余预算" width="120">
          <template slot-scope="scope">
            <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="usagePercentage" label="使用率" width="100">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.usagePercentage" 
              :color="getUsageColor(scope.row.usagePercentage)"
              :stroke-width="8"
              text-inside
            />
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="small">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
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
              type="info" 
              @click="handleDecompose(scope.row)"
              v-if="scope.row.status === 'APPROVED'"
            >
              分解
            </el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handleAdjust(scope.row)"
              v-if="scope.row.status === 'EXECUTING'"
            >
              调整
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

    <!-- 预算详情对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算名称" prop="budgetName">
              <el-input v-model="formData.budgetName" :disabled="isViewMode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算类型" prop="budgetType">
              <el-select v-model="formData.budgetType" :disabled="isViewMode" style="width: 100%">
                <el-option label="部门预算" value="DEPARTMENT" />
                <el-option label="项目预算" value="PROJECT" />
                <el-option label="费用类型预算" value="EXPENSE_TYPE" />
                <el-option label="年度预算" value="ANNUAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-select v-model="formData.budgetYear" :disabled="isViewMode" style="width: 100%">
                <el-option label="2024" value="2024" />
                <el-option label="2025" value="2025" />
                <el-option label="2026" value="2026" />
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
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number 
                v-model="formData.budgetAmount" 
                :min="0" 
                :precision="2" 
                :disabled="isViewMode"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="isViewMode">
            <el-form-item label="已使用金额">
              <el-input-number 
                v-model="formData.usedAmount" 
                :precision="2" 
                disabled
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预算描述" prop="budgetDescription">
          <el-input
            v-model="formData.budgetDescription"
            type="textarea"
            :rows="3"
            :disabled="isViewMode"
            placeholder="请输入预算描述"
          />
        </el-form-item>

        <!-- 预算明细 -->
        <el-form-item label="预算明细" v-if="!isViewMode || (formData.budgetDetails && formData.budgetDetails.length > 0)">
          <el-table :data="formData.budgetDetails || []" border size="small">
            <el-table-column prop="expenseType" label="费用类型" width="150">
              <template slot-scope="scope">
                <el-select v-model="scope.row.expenseType" :disabled="isViewMode" size="small" style="width: 100%">
                  <el-option label="差旅费" value="差旅费" />
                  <el-option label="业务招待费" value="业务招待费" />
                  <el-option label="办公费" value="办公费" />
                  <el-option label="培训费" value="培训费" />
                  <el-option label="其他" value="其他" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="budgetAmount" label="预算金额" width="150">
              <template slot-scope="scope">
                <el-input-number 
                  v-model="scope.row.budgetAmount" 
                  :min="0" 
                  :precision="2" 
                  :disabled="isViewMode"
                  size="small"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column prop="usedAmount" label="已使用" width="120" v-if="isViewMode">
              <template slot-scope="scope">
                <span class="amount used">¥{{ formatAmount(scope.row.usedAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="remainingAmount" label="剩余" width="120" v-if="isViewMode">
              <template slot-scope="scope">
                <span class="amount remaining">¥{{ formatAmount(scope.row.remainingAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="usagePercentage" label="使用率" width="120" v-if="isViewMode">
              <template slot-scope="scope">
                <el-progress 
                  :percentage="scope.row.usagePercentage" 
                  :color="getUsageColor(scope.row.usagePercentage)"
                  :stroke-width="6"
                  text-inside
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" v-if="!isViewMode">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="removeBudgetDetail(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 10px;" v-if="!isViewMode">
            <el-button size="small" @click="addBudgetDetail">添加明细</el-button>
          </div>
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

    <!-- 预算调整对话框 -->
    <el-dialog
      title="预算调整"
      :visible.sync="adjustDialogVisible"
      width="600px"
    >
      <el-form :model="adjustForm" :rules="adjustRules" ref="adjustFormRef" label-width="120px">
        <el-form-item label="调整类型" prop="adjustType">
          <el-select v-model="adjustForm.adjustType" style="width: 100%">
            <el-option label="增加预算" value="INCREASE" />
            <el-option label="减少预算" value="DECREASE" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整金额" prop="adjustAmount">
          <el-input-number 
            v-model="adjustForm.adjustAmount" 
            :min="0" 
            :precision="2" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="调整原因" prop="adjustReason">
          <el-input
            v-model="adjustForm.adjustReason"
            type="textarea"
            :rows="4"
            placeholder="请详细说明调整原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adjustDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAdjust" :loading="adjustLoading">确认调整</el-button>
      </div>
    </el-dialog>

    <!-- 统计信息卡片 -->
    <div class="statistics-container" style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.totalBudgets || 0 }}</div>
              <div class="stat-label">总预算数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">¥{{ formatAmount(statistics.totalBudgetAmount || 0) }}</div>
              <div class="stat-label">预算总额</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">¥{{ formatAmount(statistics.totalUsedAmount || 0) }}</div>
              <div class="stat-label">已使用</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ (statistics.averageUsageRate || 0).toFixed(1) }}%</div>
              <div class="stat-label">平均使用率</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/advancedFeatures'

export default {
  name: 'Budget',
  data() {
    return {
      loading: false,
      saveLoading: false,
      submitLoading: false,
      adjustLoading: false,
      tableData: [],
      multipleSelection: [],
      statistics: {},
      departmentList: [
        { deptId: 'DEPT001', deptName: '销售部' },
        { deptId: 'DEPT002', deptName: '市场部' },
        { deptId: 'DEPT003', deptName: '技术部' },
        { deptId: 'DEPT004', deptName: '管理部' },
        { deptId: 'DEPT005', deptName: '财务部' }
      ],
      searchForm: {
        budgetName: '',
        budgetType: '',
        budgetYear: '',
        status: '',
        departmentId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增预算',
      isViewMode: false,
      formData: {
        budgetId: null,
        budgetName: '',
        budgetType: '',
        budgetYear: '',
        departmentId: '',
        budgetAmount: 0,
        budgetDescription: '',
        budgetDetails: [],
        approvalRecords: []
      },
      formRules: {
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        departmentId: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      },
      adjustDialogVisible: false,
      adjustForm: {
        budgetId: '',
        adjustType: '',
        adjustAmount: 0,
        adjustReason: ''
      },
      adjustRules: {
        adjustType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        adjustAmount: [
          { required: true, message: '请输入调整金额', trigger: 'blur' }
        ],
        adjustReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ]
      },
      currentBudgetRow: null
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await budgetApi.getList(params)
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

    async loadStatistics() {
      try {
        const response = await budgetApi.getStatistics()
        if (response.code === 1) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
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
      this.dialogTitle = '新增预算'
      this.isViewMode = false
      this.formData = {
        budgetId: null,
        budgetName: '',
        budgetType: '',
        budgetYear: '',
        departmentId: '',
        budgetAmount: 0,
        budgetDescription: '',
        budgetDetails: [],
        approvalRecords: []
      }
      this.dialogVisible = true
    },

    async handleView(row) {
      this.dialogTitle = '查看预算'
      this.isViewMode = true
      try {
        const response = await budgetApi.getDetail(row.budgetId)
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
      this.dialogTitle = '编辑预算'
      this.isViewMode = false
      this.formData = { ...row, budgetDetails: [] }
      this.dialogVisible = true
    },

    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await budgetApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
          this.loadStatistics()
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
        this.submitLoading = true
        const response = await budgetApi.submit(this.formData.budgetId, {
          submitComment: '提交预算审批'
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

    handleAdjust(row) {
      this.currentBudgetRow = row
      this.adjustForm = {
        budgetId: row.budgetId,
        adjustType: '',
        adjustAmount: 0,
        adjustReason: ''
      }
      this.adjustDialogVisible = true
    },

    async handleConfirmAdjust() {
      try {
        await this.$refs.adjustFormRef.validate()
        this.adjustLoading = true
        const response = await budgetApi.adjust(this.adjustForm.budgetId, this.adjustForm)
        if (response.code === 1) {
          this.$message.success('预算调整申请成功')
          this.adjustDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '调整申请失败')
        }
      } catch (error) {
        this.$message.error('调整申请失败：' + error.message)
      } finally {
        this.adjustLoading = false
      }
    },

    addBudgetDetail() {
      if (!this.formData.budgetDetails) {
        this.formData.budgetDetails = []
      }
      this.formData.budgetDetails.push({
        expenseType: '',
        budgetAmount: 0,
        usedAmount: 0,
        remainingAmount: 0,
        usagePercentage: 0
      })
    },

    removeBudgetDetail(index) {
      this.formData.budgetDetails.splice(index, 1)
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

    getBudgetTypeColor(budgetType) {
      const colors = {
        'DEPARTMENT': 'primary',
        'PROJECT': 'success',
        'EXPENSE_TYPE': 'warning',
        'ANNUAL': 'info'
      }
      return colors[budgetType] || 'default'
    },

    getStatusColor(status) {
      const colors = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'EXECUTING': 'primary',
        'COMPLETED': 'success',
        'REJECTED': 'danger'
      }
      return colors[status] || 'default'
    },

    getUsageColor(percentage) {
      if (percentage >= 90) return '#f56c6c'
      if (percentage >= 70) return '#e6a23c'
      return '#67c23a'
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
        await this.$confirm('确定要删除该预算吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await budgetApi.delete(row.budgetId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
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
      // 跳转到审批页面
      this.$router.push({
        path: '/financialSharing/budget/approve',
        query: { id: row.budgetId }
      })
    },

    handleDecompose(row) {
      // 跳转到预算分解页面
      this.$router.push({
        path: '/financialSharing/budget/decompose',
        query: { id: row.budgetId }
      })
    },

    handleBudgetAnalysis() {
      // 跳转到预算分析页面
      this.$router.push({
        path: '/financialSharing/budget/analysis'
      })
    },

    handleBatchApprove() {
      // 批量审批
      const budgetIds = this.multipleSelection.map(item => item.budgetId)
      this.$router.push({
        path: '/financialSharing/batch/approve',
        query: { type: 'budget', ids: budgetIds.join(',') }
      })
    },

    handleBatchDelete() {
      this.$confirm('确定要删除选中的预算吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const budgetIds = this.multipleSelection.map(item => item.budgetId)
        // 调用批量删除API
        this.$message.success('批量删除成功')
        this.loadData()
        this.loadStatistics()
      }).catch(() => {})
    },

    handleExport() {
      const params = { ...this.searchForm }
      budgetApi.export(params).then(response => {
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
.budget-container {
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

.amount.used {
  color: #F56C6C;
}

.amount.remaining {
  color: #67C23A;
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

.statistics-container {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
