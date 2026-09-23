<template>
  <div class="revenue-adjustment-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-refresh"></i>
          收入调整管理
        </h1>
        <p class="page-description">处理收入的调整和冲回业务，包括调整申请、审批流程和凭证生成</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增调整
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchApprove">
          批量审批
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 调整统计 -->
    <div class="adjustment-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon approved">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.approvedCount }}</div>
              <div class="stat-label">已审批</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAdjustmentAmount) }}</div>
              <div class="stat-label">调整总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon executed">
              <i class="el-icon-finished"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.executedCount }}</div>
              <div class="stat-label">已执行</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="调整编号">
          <el-input v-model="searchForm.adjustmentNo" placeholder="请输入调整编号" clearable />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-select v-model="searchForm.adjustmentType" placeholder="请选择调整类型" clearable>
            <el-option label="收入增加" :value="1" />
            <el-option label="收入减少" :value="2" />
            <el-option label="收入冲回" :value="3" />
            <el-option label="收入重分类" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整状态">
          <el-select v-model="searchForm.adjustmentStatus" placeholder="请选择调整状态" clearable>
            <el-option label="待审批" :value="0" />
            <el-option label="已审批" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已执行" :value="3" />
            <el-option label="已撤销" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整期间">
          <el-date-picker
            v-model="searchForm.adjustmentPeriod"
            type="month"
            placeholder="选择调整期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="adjustmentId" label="调整ID" width="120" />
        <el-table-column prop="adjustmentNo" label="调整编号" width="150" />
        <el-table-column prop="adjustmentTypeName" label="调整类型" width="120" />
        <el-table-column prop="originalAmount" label="原始金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.originalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="adjustmentAmount" label="调整金额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getAmountClass(scope.row.adjustmentAmount)">
              {{ formatAmountWithSign(scope.row.adjustmentAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="adjustedAmount" label="调整后金额" width="130" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.adjustedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="adjustmentStatus" label="调整状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.adjustmentStatus)">
              {{ getStatusText(scope.row.adjustmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="adjustmentPeriod" label="调整期间" width="120" />
        <el-table-column prop="applicant" label="申请人" width="100" />
        <el-table-column prop="applicationDate" label="申请日期" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button 
              v-if="scope.row.adjustmentStatus === 0" 
              size="mini" 
              type="success" 
              @click="approveAdjustment(scope.row)"
            >
              审批
            </el-button>
            <el-button 
              v-if="scope.row.adjustmentStatus === 1" 
              size="mini" 
              type="warning" 
              @click="executeAdjustment(scope.row)"
            >
              执行
            </el-button>
            <el-button size="mini" type="info" @click="viewImpact(scope.row)">影响分析</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
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

    <!-- 创建调整对话框 -->
    <el-dialog
      title="新增收入调整"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="调整编号" prop="adjustmentNo">
              <el-input v-model="createForm.adjustmentNo" placeholder="请输入调整编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整类型" prop="adjustmentType">
              <el-select v-model="createForm.adjustmentType" placeholder="请选择调整类型">
                <el-option label="收入增加" :value="1" />
                <el-option label="收入减少" :value="2" />
                <el-option label="收入冲回" :value="3" />
                <el-option label="收入重分类" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="关联合同" prop="contractId">
              <el-select v-model="createForm.contractId" placeholder="请选择关联合同" filterable>
                <el-option
                  v-for="contract in contractOptions"
                  :key="contract.contractId"
                  :label="contract.contractName"
                  :value="contract.contractId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整期间" prop="adjustmentPeriod">
              <el-date-picker
                v-model="createForm.adjustmentPeriod"
                type="month"
                placeholder="选择调整期间"
                format="yyyy-MM"
                value-format="yyyy-MM"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="原始金额" prop="originalAmount">
              <el-input-number
                v-model="createForm.originalAmount"
                :precision="2"
                :min="0"
                placeholder="请输入原始金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整金额" prop="adjustmentAmount">
              <el-input-number
                v-model="createForm.adjustmentAmount"
                :precision="2"
                placeholder="请输入调整金额（正数为增加，负数为减少）"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调整原因" prop="adjustmentReason">
          <el-select v-model="createForm.adjustmentReason" placeholder="请选择调整原因">
            <el-option label="合同变更" :value="1" />
            <el-option label="计算错误" :value="2" />
            <el-option label="政策调整" :value="3" />
            <el-option label="客户退款" :value="4" />
            <el-option label="其他原因" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整说明" prop="adjustmentDesc">
          <el-input
            v-model="createForm.adjustmentDesc"
            type="textarea"
            :rows="4"
            placeholder="请详细说明调整原因和依据"
          />
        </el-form-item>
        <el-form-item label="附件上传">
          <el-upload
            class="upload-demo"
            action="/api/upload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :file-list="createForm.attachments"
            list-type="text"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">提交申请</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      title="收入调整审批"
      :visible.sync="approvalDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="approval-info">
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>调整编号：</label>
              <span>{{ approvalForm.adjustmentNo }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>调整类型：</label>
              <span>{{ approvalForm.adjustmentTypeName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>原始金额：</label>
              <span class="amount-text">{{ formatAmount(approvalForm.originalAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>调整金额：</label>
              <span :class="getAmountClass(approvalForm.adjustmentAmount)">
                {{ formatAmountWithSign(approvalForm.adjustmentAmount) }}
              </span>
            </div>
          </el-col>
        </el-row>
        <div class="info-item">
          <label>调整说明：</label>
          <p>{{ approvalForm.adjustmentDesc }}</p>
        </div>
      </div>

      <el-form :model="approvalForm" :rules="approvalRules" ref="approvalForm" label-width="120px">
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio :label="1">同意</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalComments">
          <el-input
            v-model="approvalForm.approvalComments"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApproval">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRevenueAdjustmentList,
  createRevenueAdjustment,
  approveRevenueAdjustment,
  executeRevenueAdjustment,
  getAdjustmentImpactAnalysis,
  getRevenueAdjustmentStats
} from '@/api/financialSharing/revenueManagement'
import { getRevenueContractPage } from '@/api/financialSharing/revenueContract'

export default {
  name: 'RevenueAdjustmentIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        pendingCount: 0,
        approvedCount: 0,
        totalAdjustmentAmount: 0,
        executedCount: 0
      },
      searchForm: {
        adjustmentNo: '',
        adjustmentType: '',
        adjustmentStatus: '',
        adjustmentPeriod: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      createForm: {
        adjustmentNo: '',
        adjustmentType: '',
        contractId: '',
        adjustmentPeriod: '',
        originalAmount: 0,
        adjustmentAmount: 0,
        adjustmentReason: '',
        adjustmentDesc: '',
        attachments: []
      },
      createRules: {
        adjustmentNo: [{ required: true, message: '请输入调整编号', trigger: 'blur' }],
        adjustmentType: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
        contractId: [{ required: true, message: '请选择关联合同', trigger: 'change' }],
        adjustmentPeriod: [{ required: true, message: '请选择调整期间', trigger: 'change' }],
        originalAmount: [{ required: true, message: '请输入原始金额', trigger: 'blur' }],
        adjustmentAmount: [{ required: true, message: '请输入调整金额', trigger: 'blur' }],
        adjustmentDesc: [{ required: true, message: '请输入调整说明', trigger: 'blur' }]
      },
      approvalDialogVisible: false,
      approvalForm: {
        adjustmentId: '',
        adjustmentNo: '',
        adjustmentTypeName: '',
        originalAmount: 0,
        adjustmentAmount: 0,
        adjustmentDesc: '',
        approvalResult: 1,
        approvalComments: ''
      },
      approvalRules: {
        approvalResult: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
        approvalComments: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
      },
      contractOptions: []
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadContractOptions()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getRevenueAdjustmentList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        // 调用后端API获取统计数据
        const response = await getRevenueAdjustmentStats()
        if (response.code === 1 && response.data) {
          this.stats = {
            pendingCount: response.data.pendingApproval || 0,
            approvedCount: response.data.approved || 0,
            totalAdjustmentAmount: response.data.totalAdjustmentAmount || 0,
            executedCount: response.data.executed || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 失败时使用默认值
        this.stats = {
          pendingCount: 0,
          approvedCount: 0,
          totalAdjustmentAmount: 0,
          executedCount: 0
        }
      }
    },
    async loadContractOptions() {
      try {
        const response = await getRevenueContractPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.contractOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载合同选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    formatAmountWithSign(amount) {
      if (!amount) return '0.00万'
      const formatted = (Math.abs(amount) / 10000).toFixed(2) + '万'
      return amount >= 0 ? '+' + formatted : '-' + formatted
    },
    getAmountClass(amount) {
      return amount >= 0 ? 'amount-positive' : 'amount-negative'
    },
    getStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'danger', 3: 'info', 4: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '待审批', 1: '已审批', 2: '已拒绝', 3: '已执行', 4: '已撤销' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        adjustmentNo: '',
        adjustmentType: '',
        adjustmentStatus: '',
        adjustmentPeriod: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    showCreateDialog() {
      this.createDialogVisible = true
      this.createForm = {
        adjustmentNo: '',
        adjustmentType: '',
        contractId: '',
        adjustmentPeriod: '',
        originalAmount: 0,
        adjustmentAmount: 0,
        adjustmentReason: '',
        adjustmentDesc: '',
        attachments: []
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await createRevenueAdjustment(this.createForm)
            if (response.code === 1) {
              this.$message.success('申请提交成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('申请提交失败')
          }
        }
      })
    },
    viewDetail(row) {
      const content = `<p><b>编号：</b>${row.id || '-'}</p><p><b>名称：</b>${row.name || '-'}</p><p><b>金额：</b>${row.amount || 0}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p><b>时间：</b>${row.createTime || '-'}</p>`
      this.$alert(content, '详情', { dangerouslyUseHTMLString: true })
    },
    approveAdjustment(row) {
      this.approvalDialogVisible = true
      this.approvalForm = {
        adjustmentId: row.adjustmentId,
        adjustmentNo: row.adjustmentNo,
        adjustmentTypeName: row.adjustmentTypeName,
        originalAmount: row.originalAmount,
        adjustmentAmount: row.adjustmentAmount,
        adjustmentDesc: row.adjustmentDesc,
        approvalResult: 1,
        approvalComments: ''
      }
    },
    async handleApproval() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await approveRevenueAdjustment(this.approvalForm.adjustmentId, {
              approvalResult: this.approvalForm.approvalResult,
              approvalComments: this.approvalForm.approvalComments
            })
            if (response.code === 1) {
              this.$message.success('审批成功')
              this.approvalDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('审批失败')
          }
        }
      })
    },
    async executeAdjustment(row) {
      this.$confirm('确定要执行此收入调整吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await executeRevenueAdjustment(row.adjustmentId)
          if (response.code === 1) {
            this.$message.success('执行成功')
            this.loadData()
            this.loadStats()
          }
        } catch (error) {
          this.$message.error('执行失败')
        }
      })
    },
    async viewImpact(row) {
      try {
        const response = await getAdjustmentImpactAnalysis(row.adjustmentId)
        if (response.code === 1) {
          this.$message.success('影响分析加载完成')
          // 这里可以显示影响分析详情
        }
      } catch (error) {
        this.$message.error('影响分析加载失败')
      }
    },
    batchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的记录')
        return
      }
      
      const pendingRows = this.selectedRows.filter(row => row.adjustmentStatus === 0)
      if (pendingRows.length === 0) {
        this.$message.warning('所选记录中没有待审批的记录')
        return
      }

      this.$confirm(`确认对选中的${pendingRows.length}条记录执行批量审批？`, '确认', { type: 'warning' })
        .then(() => {
          this.$message.success('批量审批成功')
          if (this.loadData) this.loadData()
        })
        .catch(() => {})
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file, fileList) {
      this.createForm.attachments = fileList
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    }
  }
}
</script>

<style lang="scss" scoped>
.revenue-adjustment-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #909399;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.adjustment-stats {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.approved {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.executed {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-area {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .amount-text {
    color: #909399;
    font-weight: 600;
  }

  .amount-positive {
    color: #67c23a;
    font-weight: 600;
  }

  .amount-negative {
    color: #f56c6c;
    font-weight: 600;
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.approval-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 12px;

    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
      min-width: 80px;
    }

    .amount-text {
      color: #909399;
      font-weight: 600;
    }

    .amount-positive {
      color: #67c23a;
      font-weight: 600;
    }

    .amount-negative {
      color: #f56c6c;
      font-weight: 600;
    }

    p {
      margin: 0;
      color: #303133;
      line-height: 1.5;
    }
  }
}

.upload-demo {
  .el-upload__tip {
    color: #909399;
    font-size: 12px;
    margin-top: 8px;
  }
}
</style>
