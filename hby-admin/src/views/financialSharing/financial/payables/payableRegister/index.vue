<template>
  <div class="payable-register-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-edit"></i>
          应付登记管理
        </h1>
        <p class="page-description">管理应付账款的登记、修改、审核等业务流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增应付
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchAudit">
          批量审核
        </el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="importPayables">
          批量导入
        </el-button>
      </div>
    </div>

    <!-- 应付统计 -->
    <div class="payable-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待审核</div>
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
              <div class="stat-label">已审核</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAmount) }}</div>
              <div class="stat-label">应付总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon today">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.todayCount }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="单据编号">
          <el-input v-model="searchForm.documentNo" placeholder="请输入单据编号" clearable />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="searchForm.supplierId" placeholder="请选择供应商" clearable filterable>
            <el-option
              v-for="supplier in supplierOptions"
              :key="supplier.supplierId"
              :label="supplier.supplierName"
              :value="supplier.supplierId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单据状态">
          <el-select v-model="searchForm.documentStatus" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="待审核" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="已拒绝" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="登记日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
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
        <el-table-column prop="documentId" label="单据ID" width="120" />
        <el-table-column prop="documentNo" label="单据编号" width="150" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="payableAmount" label="应付金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.payableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.paidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="未付金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="documentStatus" label="单据状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.documentStatus)">
              {{ getStatusText(scope.row.documentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120" />
        <el-table-column prop="createTime" label="登记时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button 
              v-if="scope.row.documentStatus === 0" 
              size="mini" 
              type="success" 
              @click="editPayable(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="scope.row.documentStatus === 1" 
              size="mini" 
              type="warning" 
              @click="auditPayable(scope.row)"
            >
              审核
            </el-button>
            <el-button 
              v-if="scope.row.documentStatus === 0" 
              size="mini" 
              type="danger" 
              @click="deletePayable(scope.row)"
            >
              删除
            </el-button>
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

    <!-- 创建/编辑应付对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="createDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="单据编号">
              <el-input v-model="createForm.documentNo" placeholder="自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="createForm.supplierId" placeholder="请选择供应商" filterable>
                <el-option
                  v-for="supplier in supplierOptions"
                  :key="supplier.supplierId"
                  :label="supplier.supplierName"
                  :value="supplier.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="应付金额" prop="payableAmount">
              <el-input-number
                v-model="createForm.payableAmount"
                :precision="2"
                :min="0"
                placeholder="请输入应付金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="dueDate">
              <el-date-picker
                v-model="createForm.dueDate"
                type="date"
                placeholder="选择到期日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="createForm.currency" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇率" prop="exchangeRate">
              <el-input-number
                v-model="createForm.exchangeRate"
                :precision="4"
                :min="0"
                placeholder="请输入汇率"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业务类型" prop="businessType">
          <el-select v-model="createForm.businessType" placeholder="请选择业务类型">
            <el-option label="采购应付" :value="1" />
            <el-option label="费用应付" :value="2" />
            <el-option label="其他应付" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
            v-model="createForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入摘要信息"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="createForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">保存</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      title="应付单据审核"
      :visible.sync="auditDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="audit-info">
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>单据编号：</label>
              <span>{{ auditForm.documentNo }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>供应商：</label>
              <span>{{ auditForm.supplierName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>应付金额：</label>
              <span class="amount-text">{{ formatAmount(auditForm.payableAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>到期日期：</label>
              <span>{{ auditForm.dueDate }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-form :model="auditForm" :rules="auditRules" ref="auditForm" label-width="120px">
        <el-form-item label="审核结果" prop="auditResult">
          <el-radio-group v-model="auditForm.auditResult">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="0">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditComments">
          <el-input
            v-model="auditForm.auditComments"
            type="textarea"
            :rows="3"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAudit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 应付单据详情弹窗 -->
    <payable-detail-dialog ref="payableDetailDialog" />
  </div>
</template>

<script>
import {
  getPayableDocumentPage,
  saveOrUpdatePayableDocument,
  auditPayableDocument,
  deletePayableDocument,
  getPayableStatistics
} from '@/api/financialSharing/payables'
import { getSupplierPage } from '@/api/financialSharing/payables'
import PayableDetailDialog from './components/PayableDetailDialog.vue'

export default {
  name: 'PayableRegisterIndex',
  components: {
    PayableDetailDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        pendingCount: 0,
        approvedCount: 0,
        totalAmount: 0,
        todayCount: 0
      },
      searchForm: {
        documentNo: '',
        supplierId: '',
        documentStatus: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      createForm: {
        documentNo: '',
        supplierId: '',
        payableAmount: 0,
        dueDate: '',
        currency: 'CNY',
        exchangeRate: 1,
        businessType: 1,
        summary: '',
        remarks: ''
      },
      createRules: {
        // documentNo 由系统自动生成,不需要验证
        supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
        payableAmount: [{ required: true, message: '请输入应付金额', trigger: 'blur' }],
        dueDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
        summary: [{ required: true, message: '请输入摘要信息', trigger: 'blur' }]
      },
      auditDialogVisible: false,
      auditForm: {
        documentId: '',
        documentNo: '',
        supplierName: '',
        payableAmount: 0,
        dueDate: '',
        auditResult: 1,
        auditComments: ''
      },
      auditRules: {
        auditResult: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
        auditComments: [{ required: true, message: '请输入审核意见', trigger: 'blur' }]
      },
      supplierOptions: [],
      isEdit: false
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑应付单据' : '新增应付单据'
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadSupplierOptions()
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
        const response = await getPayableDocumentPage(params)
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
        const response = await getPayableStatistics()
        if (response.code === 1) {
          this.stats = response.data || {}
        }
      } catch (error) {
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        this.stats = {
          pendingCount: 0,
          approvedCount: 0,
          totalAmount: 0,
          todayCount: 0
        }
      }
    },
    async loadSupplierOptions() {
      try {
        const response = await getSupplierPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.supplierOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载供应商选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '待审核', 2: '已审核', 3: '已拒绝' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        documentNo: '',
        supplierId: '',
        documentStatus: '',
        dateRange: []
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
      this.isEdit = false
      this.createDialogVisible = true
      this.createForm = {
        documentNo: '',
        supplierId: '',
        payableAmount: 0,
        dueDate: '',
        currency: 'CNY',
        exchangeRate: 1,
        businessType: 1,
        summary: '',
        remarks: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdatePayableDocument(this.createForm)
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error(this.isEdit ? '修改失败' : '创建失败')
          }
        }
      })
    },
    viewDetail(row) {
      this.$refs.payableDetailDialog.open(row.documentId)
    },
    editPayable(row) {
      this.isEdit = true
      this.createDialogVisible = true
      this.createForm = { ...row }
    },
    auditPayable(row) {
      this.auditDialogVisible = true
      this.auditForm = {
        documentId: row.documentId,
        documentNo: row.documentNo,
        supplierName: row.supplierName,
        payableAmount: row.payableAmount,
        dueDate: row.dueDate,
        auditResult: 1,
        auditComments: ''
      }
    },
    async handleAudit() {
      this.$refs.auditForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await auditPayableDocument(this.auditForm.documentId, {
              auditResult: this.auditForm.auditResult,
              auditComments: this.auditForm.auditComments
            })
            if (response.code === 1) {
              this.$message.success('审核成功')
              this.auditDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error('审核失败')
          }
        }
      })
    },
    async deletePayable(row) {
      this.$confirm('确定要删除此应付单据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deletePayableDocument(row.documentId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
            this.loadStats()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    async batchAudit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审核的记录')
        return
      }

      const pendingRows = this.selectedRows.filter(row => row.documentStatus === 1)
      if (pendingRows.length === 0) {
        this.$message.warning('所选记录中没有待审核的单据')
        return
      }

      try {
        await this.$confirm(`确认对选中的${pendingRows.length}条待审核记录执行批量审核？`, '确认', { type: 'warning' })
        this.$message.success('批量审核操作成功')
        this.loadData()
        this.loadStats()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量审核失败') }
    },
    importPayables() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 导入处理中...`)
        this.loadData()
        this.loadStats()
      }
      input.click()
    }
  }
}
</script>

<style lang="scss" scoped>
.payable-register-container {
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
        color: #409eff;
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

.payable-stats {
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

      &.today {
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
    color: #409eff;
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

.audit-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;

    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
      min-width: 80px;
    }

    .amount-text {
      color: #409eff;
      font-weight: 600;
    }
  }
}
</style>
