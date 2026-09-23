<template>
  <div class="budget-approval-container">
    <!-- 搜索筛选区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" ref="searchForm" size="small">
        <el-form-item label="预算名称">
          <el-input
            v-model="searchForm.budgetName"
            placeholder="请输入预算名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="申请人">
          <el-input
            v-model="searchForm.applicant"
            placeholder="请输入申请人"
            clearable
          />
        </el-form-item>
        <el-form-item label="申请部门">
          <el-select
            v-model="searchForm.department"
            placeholder="请选择部门"
            clearable
          >
            <el-option
              v-for="dept in departmentList"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-select
            v-model="searchForm.urgency"
            placeholder="请选择紧急程度"
            clearable
          >
            <el-option label="普通" value="normal" />
            <el-option label="重要" value="important" />
            <el-option label="紧急" value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="operation-card">
      <el-button
        type="primary"
        size="small"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchApprove"
      >
        批量审批
      </el-button>
      <el-button
        type="success"
        size="small"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchReject"
      >
        批量拒绝
      </el-button>
      <el-button type="info" size="small" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <span class="total-info">
        共 {{ pagination.total }} 条待审批记录
      </span>
    </el-card>

    <!-- 审批列表区域 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="approvalList"
        stripe
        border
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="60" label="序号" align="center" />

        <el-table-column prop="budgetName" label="预算名称" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewDetail(scope.row)">
              {{ scope.row.budgetName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="budgetType" label="预算类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBudgetTypeTag(scope.row.budgetType)">
              {{ scope.row.budgetType }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="applicant" label="申请人" width="100" align="center" />

        <el-table-column prop="department" label="申请部门" width="120" align="center" />

        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatCurrency(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="urgency" label="紧急程度" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getUrgencyTag(scope.row.urgency)">
              {{ getUrgencyText(scope.row.urgency) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="applyTime" label="申请时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.applyTime | formatDate }}
          </template>
        </el-table-column>

        <el-table-column prop="currentNode" label="当前节点" width="120" align="center" />

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-check"
              @click="handleApprove(scope.row)"
            >
              通过
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-close"
              @click="handleReject(scope.row)"
            >
              拒绝
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-s-operation"
              @click="handleViewProcess(scope.row)"
            >
              流程
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        class="pagination"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </el-card>

    <!-- 审批对话框 -->
    <el-dialog
      :title="approvalDialogTitle"
      :visible.sync="approvalDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleApprovalDialogClose"
    >
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="approvalForm.result">
            <el-radio label="approved">通过</el-radio>
            <el-radio label="rejected">拒绝</el-radio>
            <el-radio label="returned">退回修改</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" prop="comment">
          <el-input
            type="textarea"
            v-model="approvalForm.comment"
            :rows="4"
            placeholder="请输入审批意见"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="附件上传">
          <el-upload
            ref="upload"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :file-list="approvalForm.attachments"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :before-upload="beforeUpload"
            multiple
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              支持任意文件格式，单个文件不超过10MB
            </div>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitApproval" :loading="submitting">
          确认提交
        </el-button>
      </div>
    </el-dialog>

    <!-- 批量审批对话框 -->
    <el-dialog
      title="批量审批"
      :visible.sync="batchApprovalDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="batchApprovalForm"
        :model="batchApprovalForm"
        :rules="batchApprovalRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="batchApprovalForm.result">
            <el-radio label="approved">批量通过</el-radio>
            <el-radio label="rejected">批量拒绝</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="统一意见" prop="comment">
          <el-input
            type="textarea"
            v-model="batchApprovalForm.comment"
            :rows="3"
            placeholder="请输入批量审批意见"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="batchApprovalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitBatchApproval" :loading="batchSubmitting">
          确认提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatCurrency, formatDate } from '@/utils/index'

export default {
  name: 'BudgetApproval',
  data() {
    return {
      // 搜索表单
      searchForm: {
        budgetName: '',
        applicant: '',
        department: '',
        dateRange: [],
        urgency: ''
      },

      // 部门列表
      departmentList: [],

      // 审批列表数据
      approvalList: [],
      loading: false,

      // 分页参数
      pagination: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      },

      // 多选数据
      multipleSelection: [],

      // 审批对话框
      approvalDialogVisible: false,
      approvalDialogTitle: '预算审批',
      approvalForm: {
        budgetId: '',
        result: 'approved',
        comment: '',
        attachments: []
      },
      approvalRules: {
        result: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        comment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' },
          { min: 5, max: 500, message: '审批意见长度在 5 到 500 个字符', trigger: 'blur' }
        ]
      },

      // 批量审批对话框
      batchApprovalDialogVisible: false,
      batchApprovalForm: {
        result: 'approved',
        comment: ''
      },
      batchApprovalRules: {
        result: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        comment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' },
          { min: 5, max: 200, message: '审批意见长度在 5 到 200 个字符', trigger: 'blur' }
        ]
      },

      // 上传配置
      uploadUrl: process.env.VUE_APP_BASE_API + '/file/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + localStorage.getItem('token')
      },

      // 提交状态
      submitting: false,
      batchSubmitting: false
    }
  },

  created() {
    this.fetchApprovalList()
    this.fetchDepartmentList()
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  methods: {
    // 格式化货币
    formatCurrency,

    // 获取审批列表
    async fetchApprovalList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNo: this.pagination.pageNo,
          pageSize: this.pagination.pageSize
        }

        // 处理日期范围
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }

        const response = await budgetApi.getApprovalList(params)
        if (response.code === 1) {
          this.approvalList = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取审批列表失败')
        }
      } catch (error) {
        console.error('获取审批列表异常:', error)
        this.$message.error('获取审批列表失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 获取部门列表
    async fetchDepartmentList() {
      try {
        const response = await budgetApi.getDepartmentList()
        if (response.code === 1) {
          this.departmentList = response.data || []
        }
      } catch (error) {
        console.error('获取部门列表异常:', error)
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.pageNo = 1
      this.fetchApprovalList()
    },

    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm.dateRange = []
      this.pagination.pageNo = 1
      this.fetchApprovalList()
    },

    // 刷新
    handleRefresh() {
      this.fetchApprovalList()
    },

    // 表格多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 行点击
    handleRowClick(row) {
      this.handleViewDetail(row)
    },

    // 查看详情
    handleViewDetail(row) {
      this.$router.push({
        path: '/financialSharing/budget/approve/detail',
        query: { id: row.budgetId }
      })
    },

    // 单个审批
    handleApprove(row) {
      this.approvalDialogTitle = `审批 - ${row.budgetName}`
      this.approvalForm = {
        budgetId: row.budgetId,
        result: 'approved',
        comment: '',
        attachments: []
      }
      this.approvalDialogVisible = true
    },

    // 单个拒绝
    handleReject(row) {
      this.approvalDialogTitle = `审批 - ${row.budgetName}`
      this.approvalForm = {
        budgetId: row.budgetId,
        result: 'rejected',
        comment: '',
        attachments: []
      }
      this.approvalDialogVisible = true
    },

    // 批量审批
    handleBatchApprove() {
      this.batchApprovalForm = {
        result: 'approved',
        comment: ''
      }
      this.batchApprovalDialogVisible = true
    },

    // 批量拒绝
    handleBatchReject() {
      this.batchApprovalForm = {
        result: 'rejected',
        comment: ''
      }
      this.batchApprovalDialogVisible = true
    },

    // 查看流程
    handleViewProcess(row) {
      this.$router.push({
        path: '/financialSharing/budget/approve/process',
        query: { id: row.budgetId }
      })
    },

    // 提交审批
    async handleSubmitApproval() {
      try {
        await this.$refs.approvalForm.validate()

        this.submitting = true
        const response = await budgetApi.approveBudget(this.approvalForm)

        if (response.code === 1) {
          this.$message.success('审批提交成功')
          this.approvalDialogVisible = false
          this.fetchApprovalList()
        } else {
          this.$message.error(response.message || '审批提交失败')
        }
      } catch (error) {
        if (error !== false) { // 不是表单验证错误
          console.error('审批提交异常:', error)
          this.$message.error('审批提交失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 提交批量审批
    async handleSubmitBatchApproval() {
      try {
        await this.$refs.batchApprovalForm.validate()

        const budgetIds = this.multipleSelection.map(item => item.budgetId)

        this.batchSubmitting = true
        const response = await budgetApi.batchApprove({
          budgetIds,
          ...this.batchApprovalForm
        })

        if (response.code === 1) {
          this.$message.success(`批量审批成功，共处理 ${budgetIds.length} 条记录`)
          this.batchApprovalDialogVisible = false
          this.multipleSelection = []
          this.fetchApprovalList()
        } else {
          this.$message.error(response.message || '批量审批失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('批量审批异常:', error)
          this.$message.error('批量审批失败，请稍后重试')
        }
      } finally {
        this.batchSubmitting = false
      }
    },

    // 审批对话框关闭
    handleApprovalDialogClose() {
      this.$refs.approvalForm && this.$refs.approvalForm.resetFields()
      this.approvalForm.attachments = []
    },

    // 上传前校验
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
      }
      return isLt10M
    },

    // 上传成功
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 1) {
        this.$message.success('文件上传成功')
      } else {
        this.$message.error(response.message || '文件上传失败')
      }
    },

    // 移除文件
    handleUploadRemove(file, fileList) {
      this.approvalForm.attachments = fileList
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNo = 1
      this.fetchApprovalList()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.pageNo = page
      this.fetchApprovalList()
    },

    // 获取预算类型标签
    getBudgetTypeTag(type) {
      const tagMap = {
        '年度预算': '',
        '季度预算': 'success',
        '月度预算': 'warning',
        '项目预算': 'info',
        '部门预算': 'danger'
      }
      return tagMap[type] || ''
    },

    // 获取紧急程度标签
    getUrgencyTag(urgency) {
      const tagMap = {
        'normal': 'info',
        'important': 'warning',
        'urgent': 'danger'
      }
      return tagMap[urgency] || 'info'
    },

    // 获取紧急程度文本
    getUrgencyText(urgency) {
      const textMap = {
        'normal': '普通',
        'important': '重要',
        'urgent': '紧急'
      }
      return textMap[urgency] || '普通'
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'pending': 'warning',
        'approved': 'success',
        'rejected': 'danger',
        'returned': 'info'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'pending': '待审批',
        'approved': '已通过',
        'rejected': '已拒绝',
        'returned': '已退回'
      }
      return textMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-approval-container {
  padding: 20px;

  .search-card {
    margin-bottom: 16px;
  }

  .operation-card {
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    gap: 10px;

    .total-info {
      margin-left: auto;
      color: #909399;
      font-size: 14px;
    }
  }

  .table-card {
    .pagination {
      margin-top: 20px;
      text-align: right;
    }
  }

  .amount-text {
    font-weight: 600;
    color: #E6A23C;
  }

  .el-link {
    font-weight: 500;
  }
}

.dialog-footer {
  text-align: right;
}
</style>