<template>
  <div class="account-opening-manage">
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon pending">
                <i class="el-icon-time"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ stats.pendingCount || 0 }}</div>
                <div class="stats-label">待审批</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon approved">
                <i class="el-icon-check"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ stats.approvedCount || 0 }}</div>
                <div class="stats-label">已批准</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon rejected">
                <i class="el-icon-close"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ stats.rejectedCount || 0 }}</div>
                <div class="stats-label">已拒绝</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon total">
                <i class="el-icon-document"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ stats.totalCount || 0 }}</div>
                <div class="stats-label">总申请</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="申请编号" prop="applicationNo">
          <el-input
            v-model="queryForm.applicationNo"
            placeholder="请输入申请编号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="账户名称" prop="accountName">
          <el-input
            v-model="queryForm.accountName"
            placeholder="请输入账户名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="银行编码" prop="bankCode">
          <el-select
            v-model="queryForm.bankCode"
            placeholder="请选择银行"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="bank in BANK_OPTIONS"
              :key="bank.value"
              :label="bank.label"
              :value="bank.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请状态" prop="applicationStatus">
          <el-select
            v-model="queryForm.applicationStatus"
            placeholder="请选择状态"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="status in APPLICATION_STATUS_OPTIONS"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增申请</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="list"
        empty-text="暂无数据"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="applicationNo" label="申请编号" width="180" show-overflow-tooltip />
        <el-table-column prop="accountName" label="账户名称" width="200" show-overflow-tooltip />
        <el-table-column prop="accountType" label="账户类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAccountTypeTagType(scope.row.accountType)">
              {{ getAccountTypeLabel(scope.row.accountType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bankName" label="银行名称" width="180" show-overflow-tooltip />
        <el-table-column prop="currencyCode" label="币种" width="100" align="center">
          <template slot-scope="scope">
            {{ CURRENCY_OPTIONS.find(c => c.value === scope.row.currencyCode) ? CURRENCY_OPTIONS.find(c => c.value === scope.row.currencyCode).label : scope.row.currencyCode }}
          </template>
        </el-table-column>
        <el-table-column prop="applicationStatus" label="申请状态" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.applicationStatus)">
              {{ getStatusLabel(scope.row.applicationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicationDate" label="申请日期" width="120" align="center" />
        <el-table-column prop="contactPerson" label="联系人" width="120" show-overflow-tooltip />
        <el-table-column prop="contactPhone" label="联系电话" width="140" show-overflow-tooltip />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              v-if="scope.row.applicationStatus === 'PENDING'"
              size="mini" 
              type="text" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="scope.row.applicationStatus === 'PENDING'"
              size="mini" 
              type="text" 
              @click="handleApprove(scope.row)"
            >
              审批
            </el-button>
            <el-button 
              v-if="scope.row.applicationStatus === 'PENDING'"
              size="mini" 
              type="text" 
              style="color: #f56c6c"
              @click="handleCancel(scope.row)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="PAGINATION_CONFIG.pageSizes"
          :page-size="pagination.size"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="开户申请详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border v-if="currentDetail">
        <el-descriptions-item label="申请编号">{{ currentDetail.applicationNo }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">
          <el-tag :type="getStatusTagType(currentDetail.applicationStatus)" size="small">
            {{ getStatusLabel(currentDetail.applicationStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账户名称">{{ currentDetail.accountName }}</el-descriptions-item>
        <el-descriptions-item label="账户类型">
          <el-tag :type="getAccountTypeTagType(currentDetail.accountType)" size="small">
            {{ getAccountTypeLabel(currentDetail.accountType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="银行编码">{{ currentDetail.bankCode }}</el-descriptions-item>
        <el-descriptions-item label="银行名称">{{ currentDetail.bankName }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ getCurrencyLabel(currentDetail.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ currentDetail.applicationDate }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentDetail.contactPerson || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentDetail.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请原因" :span="2">{{ currentDetail.applicationReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批人ID" v-if="currentDetail.approverId">{{ currentDetail.approverId }}</el-descriptions-item>
        <el-descriptions-item label="审批日期" v-if="currentDetail.approvalDate">{{ currentDetail.approvalDate }}</el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2" v-if="currentDetail.approvalOpinion">{{ currentDetail.approvalOpinion }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="审批开户申请" :visible.sync="approvalDialogVisible" width="600px" :close-on-click-modal="false">
      <div v-if="currentApprovalRow" style="margin-bottom: 16px;">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="申请编号">{{ currentApprovalRow.applicationNo }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentApprovalRow.accountName }}</el-descriptions-item>
          <el-descriptions-item label="银行">{{ currentApprovalRow.bankName }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ getCurrencyLabel(currentApprovalRow.currencyCode) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-form :model="approvalForm" :rules="approvalRules" ref="approvalForm" label-width="100px">
        <el-form-item label="审批结果" prop="applicationStatus">
          <el-radio-group v-model="approvalForm.applicationStatus">
            <el-radio label="APPROVED">批准</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalOpinion">
          <el-input
            v-model="approvalForm.approvalOpinion"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApprovalSubmit" :loading="approvalLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="form"
        :rules="formRules"
        ref="form"
        label-width="120px"
        class="dialog-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户名称" prop="accountName">
              <el-input v-model="form.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="form.accountType" placeholder="请选择账户类型" style="width: 100%">
                <el-option
                  v-for="type in ACCOUNT_TYPE_OPTIONS"
                  :key="type.value"
                  :label="type.label"
                  :value="type.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行编码" prop="bankCode">
              <el-select v-model="form.bankCode" placeholder="请选择银行" style="width: 100%" @change="handleBankChange">
                <el-option
                  v-for="bank in BANK_OPTIONS"
                  :key="bank.value"
                  :label="bank.label"
                  :value="bank.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种代码" prop="currencyCode">
              <el-select v-model="form.currencyCode" placeholder="请选择币种" style="width: 100%">
                <el-option
                  v-for="currency in CURRENCY_OPTIONS"
                  :key="currency.value"
                  :label="currency.label"
                  :value="currency.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="申请原因" prop="applicationReason">
          <el-input
            v-model="form.applicationReason"
            type="textarea"
            :rows="3"
            placeholder="请输入申请原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOpeningApplicationPage, getOpeningApplication, createOpeningApplication, updateOpeningApplication, approveOpeningApplication, cancelOpeningApplication, getOpeningApplicationStatistics } from '@/api/globalTreasurer/zhgl'
import { isResponseSuccess, handleResponseData, getErrorMessage } from '../utils'
import { SUCCESS_CODE, APPLICATION_STATUS_OPTIONS, ACCOUNT_TYPE_OPTIONS, CURRENCY_OPTIONS, PAGINATION_CONFIG, BANK_OPTIONS } from '../consts'

export default {
  name: 'AccountOpeningManage',
  data() {
    return {
      // 常量
      SUCCESS_CODE,
      APPLICATION_STATUS_OPTIONS,
      ACCOUNT_TYPE_OPTIONS,
      CURRENCY_OPTIONS,
      PAGINATION_CONFIG,
      BANK_OPTIONS,

      // 数据列表
      list: [],
      total: 0,
      loading: false,

      // 查询表单
      queryForm: {
        applicationNo: '',
        accountName: '',
        bankCode: '',
        applicationStatus: ''
      },

      // 分页
      pagination: {
        current: 1,
        size: 10
      },

      // 统计数据
      stats: {
        totalCount: 0,
        pendingCount: 0,
        approvedCount: 0,
        rejectedCount: 0,
        cancelledCount: 0
      },

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      submitLoading: false,

      // 查看详情
      detailDialogVisible: false,
      currentDetail: null,

      // 审批
      approvalDialogVisible: false,
      approvalLoading: false,
      currentApprovalRow: null,
      approvalForm: {
        applicationStatus: 'APPROVED',
        approvalOpinion: ''
      },
      approvalRules: {
        applicationStatus: [{ required: true, message: '请选择审批结果', trigger: 'change' }]
      },

      // 表单数据
      form: {
        applicationId: null,
        accountName: '',
        accountType: '',
        bankCode: '',
        currencyCode: '',
        contactPerson: '',
        contactPhone: '',
        applicationReason: ''
      },

      // 表单验证规则
      formRules: {
        accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
        accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }],
        bankCode: [{ required: true, message: '请选择银行', trigger: 'change' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }],
        contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
      },

      // 选中的行
      selectedRows: []
    }
  },

  created() {
    this.fetchData()
    this.fetchStats()
  },

  methods: {
    normalizeApiResponse(response) {
      if (typeof response === 'string') {
        try {
          return JSON.parse(response)
        } catch (error) {
          return response
        }
      }
      return response
    },

    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.queryForm
        }

        const response = this.normalizeApiResponse(await getOpeningApplicationPage(params))
        if (isResponseSuccess(response)) {
          const { list, total } = handleResponseData(response)
          this.list = list || []
          this.total = total || 0
        } else {
          // 后端返回错误时，显示空数据但不影响页面正常展示
          this.list = []
          this.total = 0
          this.$message.error(getErrorMessage(response) || '获取数据失败')
        }
      } catch (error) {
        console.error('获取开户申请列表失败:', error)
        // 网络错误或其他异常时，显示空数据但不影响页面正常展示
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败，请检查网络连接或稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async fetchStats() {
      try {
        const response = this.normalizeApiResponse(await getOpeningApplicationStatistics())
        if (isResponseSuccess(response)) {
          const data = response.data || {}
          this.stats = {
            totalCount: Number(data.TOTALCOUNT || data.totalCount) || 0,
            pendingCount: Number(data.PENDINGCOUNT || data.pendingCount) || 0,
            approvedCount: Number(data.APPROVEDCOUNT || data.approvedCount) || 0,
            rejectedCount: Number(data.REJECTEDCOUNT || data.rejectedCount) || 0,
            cancelledCount: Number(data.CANCELLEDCOUNT || data.cancelledCount) || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
        this.stats = {
          totalCount: 0,
          pendingCount: 0,
          approvedCount: 0,
          rejectedCount: 0,
          cancelledCount: 0
        }
      }
    },

    // 查询
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        applicationNo: '',
        accountName: '',
        bankCode: '',
        applicationStatus: ''
      }
      this.pagination.current = 1
      this.fetchData()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.fetchData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增开户申请'
      this.isEdit = false
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑开户申请'
      this.isEdit = true
      this.dialogVisible = true
      this.form = { ...row }
    },

    // 查看
    async handleView(row) {
      try {
        const response = await getOpeningApplication(row.applicationId)
        if (isResponseSuccess(response)) {
          this.currentDetail = response.data
        } else {
          this.currentDetail = { ...row }
        }
      } catch (e) {
        this.currentDetail = { ...row }
      }
      this.detailDialogVisible = true
    },

    // 审批
    handleApprove(row) {
      this.currentApprovalRow = row
      this.approvalForm = { applicationStatus: 'APPROVED', approvalOpinion: '' }
      this.$nextTick(() => {
        this.$refs.approvalForm && this.$refs.approvalForm.clearValidate()
      })
      this.approvalDialogVisible = true
    },

    // 提交审批
    async handleApprovalSubmit() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (!valid) return
        this.approvalLoading = true
        try {
          const response = await approveOpeningApplication(
            this.currentApprovalRow.applicationId,
            this.approvalForm.approvalOpinion,
            this.approvalForm.applicationStatus
          )
          if (isResponseSuccess(response)) {
            this.$message.success('审批成功')
            this.approvalDialogVisible = false
            this.fetchData()
            this.fetchStats()
          } else {
            this.$message.error(getErrorMessage(response) || '审批失败')
          }
        } catch (error) {
          console.error('审批失败:', error)
          this.$message.error('审批失败，请稍后重试')
        } finally {
          this.approvalLoading = false
        }
      })
    },

    // 取消申请
    async handleCancel(row) {
      try {
        await this.$confirm('确定要取消该开户申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await cancelOpeningApplication(row.applicationId)
        if (isResponseSuccess(response)) {
          this.$message.success('取消成功')
          this.fetchData()
          this.fetchStats()
        } else {
          this.$message.error(getErrorMessage(response) || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消开户申请失败:', error)
          this.$message.error('取消失败，请稍后重试')
        }
      }
    },

    // 银行选择改变
    handleBankChange(bankCode) {
      const bank = this.BANK_OPTIONS.find(item => item.value === bankCode)
      if (bank) {
        this.form.bankName = bank.label
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true

        const formData = { ...this.form }
        let response

        if (this.isEdit) {
          response = await updateOpeningApplication(formData)
        } else {
          response = await createOpeningApplication(formData)
        }

        if (isResponseSuccess(response)) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.fetchData()
          this.fetchStats()
        } else {
          this.$message.error(getErrorMessage(response) || (this.isEdit ? '更新失败' : '创建失败'))
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时error为false
          console.error('提交表单失败:', error)
          this.$message.error(this.isEdit ? '更新失败，请稍后重试' : '创建失败，请稍后重试')
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.form = {
        applicationId: null,
        accountName: '',
        accountType: '',
        bankCode: '',
        currencyCode: '',
        contactPerson: '',
        contactPhone: '',
        applicationReason: ''
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态标签文本
    getStatusLabel(status) {
      const option = this.APPLICATION_STATUS_OPTIONS.find(item => item.value === status)
      return option ? option.label : status
    },

    // 获取账户类型标签类型
    getAccountTypeTagType(type) {
      const typeMap = {
        'CURRENT': 'primary',
        'FIXED': 'success',
        'SAVINGS': 'warning',
        'BASIC': 'primary',
        'GENERAL': 'success',
        'SPECIAL': 'warning',
        'TEMPORARY': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取账户类型标签文本
    getAccountTypeLabel(type) {
      const option = this.ACCOUNT_TYPE_OPTIONS.find(item => item.value === type)
      return option ? option.label : type
    },

    // 获取币种标签文本
    getCurrencyLabel(code) {
      const option = this.CURRENCY_OPTIONS.find(item => item.value === code)
      return option ? option.label : code
    }
  }
}
</script>

<style scoped>
.account-opening-manage {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stats-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stats-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stats-icon.pending {
  background: linear-gradient(135deg, #f39c12, #e67e22);
}

.stats-icon.approved {
  background: linear-gradient(135deg, #27ae60, #2ecc71);
}

.stats-icon.rejected {
  background: linear-gradient(135deg, #e74c3c, #c0392b);
}

.stats-icon.total {
  background: linear-gradient(135deg, #3498db, #2980b9);
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 5px;
}

.stats-label {
  font-size: 14px;
  color: #7f8c8d;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.dialog-form {
  padding: 0 20px;
}

.dialog-footer {
  text-align: right;
  padding: 20px 0 0;
}
</style>
