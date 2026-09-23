<template>
  <div class="account-closing-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-close"></i>
            账户销户申请管理
          </h1>
          <p class="page-description">管理账户销户申请，包括申请创建、审批、取消等操作</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增申请
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchApprove" :disabled="selectedRows.length === 0">
            批量审批
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 申请概览 -->
    <el-row :gutter="20" class="application-overview">
      <el-col :span="6">
        <el-card class="overview-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon total-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总申请数</div>
              <div class="card-value">{{ stats.totalApplications || 0 }}</div>
              <div class="card-change">较上月 +12%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon pending-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-info">
              <div class="card-title">待审批</div>
              <div class="card-value">{{ stats.pendingApplications || 0 }}</div>
              <div class="card-change">需及时处理</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon approved-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="card-info">
              <div class="card-title">已批准</div>
              <div class="card-value">{{ stats.approvedApplications || 0 }}</div>
              <div class="card-change">审批通过率 85%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card" shadow="hover">
          <div class="card-content">
            <div class="card-icon rejected-icon">
              <i class="el-icon-close"></i>
            </div>
            <div class="card-info">
              <div class="card-title">已拒绝</div>
              <div class="card-value">{{ stats.rejectedApplications || 0 }}</div>
              <div class="card-change">拒绝率 15%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" label-width="100px">
        <el-form-item label="申请编号">
          <el-input v-model="queryForm.applicationNo" placeholder="请输入申请编号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="账户号码">
          <el-input v-model="queryForm.accountNumber" placeholder="请输入账户号码" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="申请状态">
          <el-select v-model="queryForm.applicationStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option
              v-for="status in APPLICATION_STATUS_OPTIONS"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 申请表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="申请编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.applicationNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.accountNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="销户原因" min-width="200px">
          <template slot-scope="{row}">
            <span>{{ row.closingReason }}</span>
          </template>
        </el-table-column>
        <el-table-column label="余额处理" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBalanceHandlingTagType(row.balanceHandling)" size="mini">
              {{ getBalanceHandlingText(row.balanceHandling) }}
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
        <el-table-column label="审批日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.approvalDate || '-' }}</span>
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
            <el-button v-if="row.applicationStatus === 'PENDING'" size="mini" type="warning" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="row.applicationStatus === 'PENDING'" size="mini" type="danger" @click="handleCancel(row,$index)">
              取消
            </el-button>
            <el-button v-if="row.applicationStatus === 'APPROVED'" size="mini" type="info" @click="handleComplete(row)">
              完成销户
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="fetchData" />
    </el-card>

    <!-- 新增/编辑申请对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="handleDialogClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账户号码" prop="accountNo">
              <el-input v-model="form.accountNo" placeholder="请输入账户号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户ID" prop="accountId">
              <el-input v-model="form.accountId" placeholder="请输入账户ID" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="销户原因" prop="closingReason">
          <el-input
            v-model="form.closingReason"
            type="textarea"
            :rows="3"
            placeholder="请输入销户原因"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="余额处理方式" prop="balanceHandling">
              <el-select v-model="form.balanceHandling" placeholder="请选择余额处理方式" style="width: 100%" @change="handleBalanceHandlingChange">
                <el-option
                  v-for="option in BALANCE_HANDLING_OPTIONS"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.balanceHandling === 'TRANSFER'">
            <el-form-item label="转账目标账户" prop="transferAccountNumber">
              <el-input v-model="form.transferAccountNumber" placeholder="请输入转账目标账户号码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="附件">
          <el-input
            v-model="form.attachments"
            type="textarea"
            :rows="2"
            placeholder="请输入附件信息"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog :title="isBatchApprove ? `批量审批（共 ${batchApproveRows.length} 条）` : '审批销户申请'" :visible.sync="approvalDialogVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" :rules="approvalRules" label-width="120px">
        <el-form-item label="申请编号">
          <el-input v-model="approvalForm.applicationNo" disabled />
        </el-form-item>
        <el-form-item v-if="!isBatchApprove" label="账户号码">
          <el-input v-model="approvalForm.accountNo" disabled />
        </el-form-item>
        <el-form-item v-if="!isBatchApprove" label="销户原因">
          <el-input v-model="approvalForm.closingReason" type="textarea" :rows="2" disabled />
        </el-form-item>
        <el-form-item label="审批结果" prop="approved">
          <el-radio-group v-model="approvalForm.approved">
            <el-radio :label="true">批准</el-radio>
            <el-radio :label="false">拒绝</el-radio>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApprovalSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="销户申请详情" :visible.sync="detailDialogVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请编号">{{ currentDetail.applicationNo }}</el-descriptions-item>
        <el-descriptions-item label="账户号码">{{ currentDetail.accountNo }}</el-descriptions-item>
        <el-descriptions-item label="账户ID">{{ currentDetail.accountId }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">
          <el-tag :type="getStatusTagType(currentDetail.applicationStatus)">
            {{ getStatusText(currentDetail.applicationStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="销户原因" :span="2">{{ currentDetail.closingReason }}</el-descriptions-item>
        <el-descriptions-item label="余额处理方式">{{ getBalanceHandlingText(currentDetail.balanceHandling) }}</el-descriptions-item>
        <el-descriptions-item label="转账目标账户">{{ currentDetail.transferAccountNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ currentDetail.applicationDate }}</el-descriptions-item>
        <el-descriptions-item label="审批日期">{{ currentDetail.approvalDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ currentDetail.approvalUser || '-' }}</el-descriptions-item>
        <el-descriptions-item label="销户日期">{{ currentDetail.closingDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ currentDetail.approvalOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="附件" :span="2">{{ currentDetail.attachments || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getClosingApplicationPage, createClosingApplication, updateClosingApplication, approveClosingApplication, batchApproveClosingApplication, cancelClosingApplication, completeClosing, getClosingApplicationStatistics } from '@/api/globalTreasurer/zhgl'
import { SUCCESS_CODE, APPLICATION_STATUS_OPTIONS, PAGINATION_CONFIG } from '../consts'
import Pagination from '@/components/Pagination'

export default {
  name: 'AccountClosingManage',
  components: {
    Pagination
  },
  data() {
    return {
      // 常量
      SUCCESS_CODE,
      APPLICATION_STATUS_OPTIONS,
      PAGINATION_CONFIG,

      // 余额处理方式选项
      BALANCE_HANDLING_OPTIONS: [
        { label: '转账', value: 'TRANSFER' },
        { label: '现金', value: 'CASH' },
        { label: '支票', value: 'CHECK' }
      ],

      // 数据列表
      list: [],
      total: 0,
      loading: false,

      // 查询表单
      queryForm: {
        applicationNo: '',
        accountNumber: '',
        applicationStatus: '',
        dateRange: []
      },

      // 分页
      listQuery: {
        pageNo: 1,
        pageSize: 20
      },

      // 统计数据
      stats: {
        totalApplications: 0,
        pendingApplications: 0,
        approvedApplications: 0,
        rejectedApplications: 0
      },

      // 选中行
      selectedRows: [],

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,

      // 表单数据
      form: {
        applicationId: null,
        accountId: null,
        accountNo: '',
        closingReason: '',
        balanceHandling: '',
        transferAccountId: null,
        transferAccountNumber: '',
        attachments: '',
        remark: ''
      },

      // 表单验证规则
      rules: {
        accountNo: [
          { required: true, message: '请输入账户号码', trigger: 'blur' }
        ],
        accountId: [
          { required: true, message: '请输入账户ID', trigger: 'blur' }
        ],
        closingReason: [
          { required: true, message: '请输入销户原因', trigger: 'blur' }
        ],
        balanceHandling: [
          { required: true, message: '请选择余额处理方式', trigger: 'change' }
        ],
        transferAccountNumber: [
          { required: true, message: '请输入转账目标账户号码', trigger: 'blur' }
        ]
      },

      // 审批对话框
      approvalDialogVisible: false,
      isBatchApprove: false,
      batchApproveRows: [],
      approvalForm: {
        applicationId: null,
        applicationNo: '',
        accountNo: '',
        closingReason: '',
        approved: true,
        approvalOpinion: ''
      },

      // 审批表单验证规则
      approvalRules: {
        approved: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalOpinion: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },

      // 详情对话框
      detailDialogVisible: false,
      currentDetail: {}
    }
  },

  created() {
    this.fetchData()
    this.fetchStats()
  },

  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNo: this.listQuery.pageNo,
          pageSize: this.listQuery.pageSize
        }
        if (this.queryForm.applicationNo) params.applicationNo = this.queryForm.applicationNo
        if (this.queryForm.accountNumber) params.accountNumber = this.queryForm.accountNumber
        if (this.queryForm.applicationStatus) params.applicationStatus = this.queryForm.applicationStatus
        if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
          params.startDate = this.queryForm.dateRange[0]
          params.endDate = this.queryForm.dateRange[1]
        }

        const response = await getClosingApplicationPage(params)
        if (response && response.code === 1) {
          const data = response.data || {}
          this.list = data.tlist || data.records || data.list || []
          this.total = parseInt(data.totalRecord || data.total || 0) || this.list.length
        } else {
          this.list = []
          this.total = 0
          this.$message.error(response.msg || response.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取销户申请列表失败:', error)
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async fetchStats() {
      try {
        const response = await getClosingApplicationStatistics()
        if (response && response.code === 1) {
          const data = response.data || {}
          this.stats = {
            totalApplications: data.totalCount || 0,
            pendingApplications: data.pendingCount || 0,
            approvedApplications: data.approvedCount || 0,
            rejectedApplications: data.rejectedCount || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 查询
    handleQuery() {
      this.listQuery.pageNo = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        applicationNo: '',
        accountNumber: '',
        applicationStatus: '',
        dateRange: []
      }
      this.listQuery.pageNo = 1
      this.fetchData()
    },

    // 选择行变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增
    handleCreate() {
      this.dialogTitle = '新增销户申请'
      this.isEdit = false
      this.form = {
        applicationId: null,
        accountId: null,
        accountNo: '',
        closingReason: '',
        balanceHandling: '',
        transferAccountId: null,
        transferAccountNumber: '',
        attachments: '',
        remark: ''
      }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑销户申请'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },

    // 审批
    handleApprove(row) {
      this.isBatchApprove = false
      this.batchApproveRows = []
      this.approvalForm = {
        applicationId: row.applicationId,
        applicationNo: row.applicationNo,
        accountNo: row.accountNo,
        closingReason: row.closingReason,
        approved: true,
        approvalOpinion: ''
      }
      this.approvalDialogVisible = true
    },

    // 取消申请
    async handleCancel(row, index) {
      try {
        await this.$confirm('确定要取消该销户申请吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await cancelClosingApplication(row.applicationId, this.$store.getters.name)
        if (response && response.code === 1) {
          this.$message.success('取消成功')
          this.fetchData()
          this.fetchStats()
        } else {
          this.$message.error(response && response.msg || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消销户申请失败:', error)
          this.$message.error('取消失败')
        }
      }
    },

    // 完成销户
    async handleComplete(row) {
      try {
        await this.$confirm('确定要完成该销户操作吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const closingDate = new Date().toISOString().split('T')[0]
        const response = await completeClosing(row.applicationId, closingDate, this.$store.getters.name)
        if (response && response.code === 1) {
          this.$message.success('销户完成')
          this.fetchData()
          this.fetchStats()
        } else {
          this.$message.error(response && response.msg || '操作失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('完成销户失败:', error)
          this.$message.error('操作失败')
        }
      }
    },

    // 查看详情
    handleViewDetail(row) {
      this.currentDetail = { ...row }
      this.detailDialogVisible = true
    },

    // 批量审批
    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的申请')
        return
      }
      const pendingRows = this.selectedRows.filter(r => r.applicationStatus === 'PENDING')
      if (pendingRows.length === 0) {
        this.$message.warning('所选申请中没有待审批状态的记录')
        return
      }
      this.isBatchApprove = true
      this.batchApproveRows = pendingRows
      this.approvalForm = {
        applicationId: null,
        applicationNo: `共 ${pendingRows.length} 条`,
        accountNo: '',
        closingReason: '',
        approved: true,
        approvalOpinion: ''
      }
      this.approvalDialogVisible = true
    },

    // 导出数据
    async handleExport() {
      try {
        if (!this.list || this.list.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const headers = ['申请编号', '账户号码', '账户名称', '销户原因', '余额处理方式', '申请状态', '申请日期', '审批日期']
        const rows = this.list.map(row => [
          row.applicationNo || '',
          row.accountNo || '',
          row.accountName || '',
          row.closingReason || '',
          row.balanceHandling || '',
          row.applicationStatus || '',
          row.applicationDate || '',
          row.approvalDate || ''
        ])
        this.downloadCsv(headers, rows, '销户申请列表.csv')
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    downloadCsv(headers, rows, filename) {
      const csvContent = [headers, ...rows].map(r => r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')).join('\n')
      const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = filename
      a.click()
      URL.revokeObjectURL(url)
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        const formData = {
          ...this.form,
          createUser: this.$store.getters.name,
          updateUser: this.$store.getters.name
        }

        let response
        if (this.isEdit) {
          response = await updateClosingApplication(formData)
        } else {
          response = await createClosingApplication(formData)
        }

        if (response && response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.fetchData()
          this.fetchStats()
        } else {
          this.$message.error(response && response.msg || '操作失败')
        }
      } catch (error) {
        console.error('提交表单失败:', error)
        this.$message.error('操作失败')
      }
    },

    // 提交审批
    async handleApprovalSubmit() {
      try {
        await this.$refs.approvalForm.validate()

        const { approved, approvalOpinion } = this.approvalForm

        if (this.isBatchApprove) {
          // 批量审批
          const applicationIds = this.batchApproveRows.map(r => r.applicationId)
          const response = await batchApproveClosingApplication(applicationIds, approved, approvalOpinion)
          if (response && response.code === 1) {
            const { successCount, totalCount } = response.data || {}
            this.$message.success(`批量审批完成，成功 ${successCount}/${totalCount} 条`)
            this.approvalDialogVisible = false
            this.fetchData()
            this.fetchStats()
          } else {
            this.$message.error(response && response.msg || '批量审批失败')
          }
        } else {
          // 单条审批
          const { applicationId } = this.approvalForm
          const response = await approveClosingApplication(
            applicationId,
            approved,
            this.$store.getters.name,
            approvalOpinion
          )
          if (response && response.code === 1) {
            this.$message.success('审批成功')
            this.approvalDialogVisible = false
            this.fetchData()
            this.fetchStats()
          } else {
            this.$message.error(response && response.msg || '审批失败')
          }
        }
      } catch (error) {
        console.error('审批失败:', error)
        this.$message.error('审批失败')
      }
    },

    // 余额处理方式变化
    handleBalanceHandlingChange(value) {
      if (value !== 'TRANSFER') {
        this.form.transferAccountNumber = ''
        this.form.transferAccountId = null
      }
    },

    // 关闭对话框
    handleDialogClose() {
      this.$refs.form && this.$refs.form.resetFields()
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info',
        'COMPLETED': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待审批',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || status
    },

    // 获取余额处理方式标签类型
    getBalanceHandlingTagType(handling) {
      const handlingMap = {
        'TRANSFER': 'primary',
        'CASH': 'success',
        'CHECK': 'warning'
      }
      return handlingMap[handling] || 'info'
    },

    // 获取余额处理方式文本
    getBalanceHandlingText(handling) {
      const handlingMap = {
        'TRANSFER': '转账',
        'CASH': '现金',
        'CHECK': '支票'
      }
      return handlingMap[handling] || handling
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
            color: #409EFF;
          }
        }

        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 10px;
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
        padding: 20px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
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
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.approved-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.rejected-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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
            font-size: 28px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #67C23A;
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
      text-decoration: underline;
    }
  }

  .dialog-footer {
    text-align: right;

    .el-button {
      margin-left: 10px;
    }
  }
}
</style>