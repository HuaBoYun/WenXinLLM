<template>
  <div class="invoice-management-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="发票代码">
          <el-input v-model="searchForm.invoiceCode" placeholder="请输入发票代码" clearable />
        </el-form-item>
        <el-form-item label="发票号码">
          <el-input v-model="searchForm.invoiceNumber" placeholder="请输入发票号码" clearable />
        </el-form-item>
        <el-form-item label="发票类型">
          <el-select v-model="searchForm.invoiceType" placeholder="请选择发票类型" clearable>
            <el-option
              v-for="item in INVOICE_TYPES"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="发票状态">
          <el-select v-model="searchForm.invoiceStatus" placeholder="请选择发票状态" clearable>
            <el-option
              v-for="item in INVOICE_STATUSES"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="销售方名称">
          <el-input v-model="searchForm.sellerName" placeholder="请输入销售方名称" clearable />
        </el-form-item>
        <el-form-item label="购买方名称">
          <el-input v-model="searchForm.buyerName" placeholder="请输入购买方名称" clearable />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option
              v-for="item in RISK_LEVELS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="业务分类">
          <el-select v-model="searchForm.businessCategory" placeholder="请选择业务分类" clearable>
            <el-option
              v-for="item in BUSINESS_CATEGORIES"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开票日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card" shadow="never">
      <div class="operation-bar">
        <div class="operation-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建发票</el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleUpload">上传发票</el-button>
          <el-button 
            type="warning" 
            icon="el-icon-view" 
            :disabled="!hasSelection"
            @click="handleBatchRecognize"
          >
            批量识别
          </el-button>
          <el-button 
            type="info" 
            icon="el-icon-check" 
            :disabled="!hasSelection"
            @click="handleBatchVerify"
          >
            批量验真
          </el-button>
          <el-button 
            type="primary" 
            icon="el-icon-box" 
            :disabled="!hasSelection"
            @click="handleBatchArchive"
          >
            批量归档
          </el-button>
          <el-button 
            type="danger" 
            icon="el-icon-delete" 
            :disabled="!hasSelection"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
        <div class="operation-right">
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
          <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        </div>
      </div>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="invoiceCode" label="发票代码" width="120" show-overflow-tooltip />
        <el-table-column prop="invoiceNumber" label="发票号码" width="100" show-overflow-tooltip />
        <el-table-column prop="invoiceType" label="发票类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="small">{{ formatInvoiceType(scope.row.invoiceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="invoiceStatus" label="发票状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getInvoiceStatusColor(scope.row.invoiceStatus)" size="small">
              {{ formatInvoiceStatus(scope.row.invoiceStatus).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sellerName" label="销售方" width="150" show-overflow-tooltip />
        <el-table-column prop="buyerName" label="购买方" width="150" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="价税合计" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="invoiceDate" label="开票日期" width="120" align="center">
          <template slot-scope="scope">
            {{ formatDate(scope.row.invoiceDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="ocrStatus" label="识别状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="formatOcrStatus(scope.row.ocrStatus).color" size="small">
              {{ formatOcrStatus(scope.row.ocrStatus).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="verificationStatus" label="验真状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="formatVerificationStatus(scope.row.verificationStatus).color" size="small">
              {{ formatVerificationStatus(scope.row.verificationStatus).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="small">
              {{ formatRiskLevel(scope.row.riskLevel).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="processingStatus" label="处理状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="formatProcessingStatus(scope.row.processingStatus).color" size="small">
              {{ formatProcessingStatus(scope.row.processingStatus).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="150" align="center" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown trigger="click" @command="handleCommand($event, scope.row)">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item 
                  command="recognize" 
                  :disabled="scope.row.ocrStatus === 'SUCCESS'"
                >
                  OCR识别
                </el-dropdown-item>
                <el-dropdown-item 
                  command="verify" 
                  :disabled="scope.row.verificationStatus === 'SUCCESS'"
                >
                  验真
                </el-dropdown-item>
                <el-dropdown-item 
                  command="archive" 
                  :disabled="scope.row.archiveStatus === 'ARCHIVED'"
                >
                  归档
                </el-dropdown-item>
                <el-dropdown-item 
                  command="assessRisk"
                >
                  风险评估
                </el-dropdown-item>
                <el-dropdown-item 
                  command="retry" 
                  :disabled="!needsRetry(scope.row)"
                >
                  重试处理
                </el-dropdown-item>
                <el-dropdown-item 
                  command="delete" 
                  divided
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 发票详情对话框 -->
    <InvoiceManagementDetail
      :visible.sync="detailVisible"
      :invoice-id="currentInvoiceId"
      :mode="detailMode"
      @refresh="handleRefresh"
    />

    <!-- 上传发票对话框 -->
    <InvoiceUploadDialog
      :visible.sync="uploadVisible"
      @success="handleUploadSuccess"
    />
  </div>
</template>

<script>
import {
  getInvoicePage,
  deleteInvoice,
  recognizeInvoice,
  verifyInvoice,
  archiveInvoice,
  assessInvoiceRisk,
  retryProcessInvoice,
  batchRecognizeInvoices,
  batchVerifyInvoices,
  batchArchiveInvoices,
  batchDeleteInvoices,
  INVOICE_TYPES,
  INVOICE_STATUSES,
  RISK_LEVELS,
  BUSINESS_CATEGORIES,
  utils
} from '@/api/managementAccountant/ts/invoiceManagement'
import InvoiceManagementDetail from './InvoiceManagementDetail'
import InvoiceUploadDialog from './InvoiceUploadDialog'

export default {
  name: 'InvoiceManagementList',
  components: {
    InvoiceManagementDetail,
    InvoiceUploadDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        invoiceCode: '',
        invoiceNumber: '',
        invoiceType: '',
        invoiceStatus: '',
        sellerName: '',
        buyerName: '',
        riskLevel: '',
        businessCategory: '',
        dateRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: '',
      detailVisible: false,
      currentInvoiceId: null,
      detailMode: 'view',
      uploadVisible: false,
      INVOICE_TYPES,
      INVOICE_STATUSES,
      RISK_LEVELS,
      BUSINESS_CATEGORIES
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    },
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          tenantId: this.tenantId,
          ...this.searchForm
        }
        
        // 处理日期范围
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        
        const response = await getInvoicePage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置搜索
    handleReset() {
      this.searchForm = {
        invoiceCode: '',
        invoiceNumber: '',
        invoiceType: '',
        invoiceStatus: '',
        sellerName: '',
        buyerName: '',
        riskLevel: '',
        businessCategory: '',
        dateRange: []
      }
      this.handleSearch()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.currentInvoiceId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 查看
    handleView(row) {
      this.currentInvoiceId = row.invoiceId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentInvoiceId = row.invoiceId
      this.detailMode = 'edit'
      this.detailVisible = true
    },

    // 上传
    handleUpload() {
      this.uploadVisible = true
    },

    // 上传成功
    handleUploadSuccess() {
      this.handleRefresh()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order
      this.loadData()
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 命令处理
    async handleCommand(command, row) {
      switch (command) {
        case 'recognize':
          await this.handleRecognize(row)
          break
        case 'verify':
          await this.handleVerify(row)
          break
        case 'archive':
          await this.handleArchive(row)
          break
        case 'assessRisk':
          await this.handleAssessRisk(row)
          break
        case 'retry':
          await this.handleRetry(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // OCR识别
    async handleRecognize(row) {
      try {
        this.$message.info('正在识别发票...')
        const response = await recognizeInvoice(this.tenantId, row.invoiceId)
        if (response.success) {
          this.$message.success('发票识别成功')
          this.handleRefresh()
        } else {
          this.$message.error('发票识别失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('发票识别失败: ' + error.message)
      }
    },

    // 验真
    async handleVerify(row) {
      try {
        this.$message.info('正在验真发票...')
        const response = await verifyInvoice(this.tenantId, row.invoiceId)
        if (response.success) {
          this.$message.success('发票验真成功')
          this.handleRefresh()
        } else {
          this.$message.error('发票验真失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('发票验真失败: ' + error.message)
      }
    },

    // 归档
    async handleArchive(row) {
      try {
        const archivePath = `/archive/${new Date().getFullYear()}/${row.invoiceId}`
        const response = await archiveInvoice(this.tenantId, row.invoiceId, archivePath)
        if (response.success) {
          this.$message.success('发票归档成功')
          this.handleRefresh()
        } else {
          this.$message.error('发票归档失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('发票归档失败: ' + error.message)
      }
    },

    // 风险评估
    async handleAssessRisk(row) {
      try {
        const response = await assessInvoiceRisk(this.tenantId, row.invoiceId)
        if (response.success) {
          this.$message.success('风险评估完成')
          this.handleRefresh()
        } else {
          this.$message.error('风险评估失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('风险评估失败: ' + error.message)
      }
    },

    // 重试处理
    async handleRetry(row) {
      try {
        const response = await retryProcessInvoice(this.tenantId, row.invoiceId)
        if (response.success) {
          this.$message.success('重试处理成功')
          this.handleRefresh()
        } else {
          this.$message.error('重试处理失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('重试处理失败: ' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这条发票记录吗？', '确认删除', {
          type: 'warning'
        })
        
        const response = await deleteInvoice(this.tenantId, row.invoiceId)
        if (response.success) {
          this.$message.success('删除成功')
          this.handleRefresh()
        } else {
          this.$message.error('删除失败: ' + response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },

    // 批量识别
    async handleBatchRecognize() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要识别的发票')
        return
      }
      
      try {
        const invoiceIds = this.selectedRows.map(row => row.invoiceId)
        this.$message.info('正在批量识别发票...')
        const response = await batchRecognizeInvoices(this.tenantId, invoiceIds)
        if (response.success) {
          this.$message.success(`批量识别完成，成功: ${response.data.successCount}，失败: ${response.data.failCount}`)
          this.handleRefresh()
        } else {
          this.$message.error('批量识别失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('批量识别失败: ' + error.message)
      }
    },

    // 批量验真
    async handleBatchVerify() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要验真的发票')
        return
      }
      
      try {
        const invoiceIds = this.selectedRows.map(row => row.invoiceId)
        this.$message.info('正在批量验真发票...')
        const response = await batchVerifyInvoices(this.tenantId, invoiceIds)
        if (response.success) {
          this.$message.success(`批量验真完成，成功: ${response.data.successCount}，失败: ${response.data.failCount}`)
          this.handleRefresh()
        } else {
          this.$message.error('批量验真失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('批量验真失败: ' + error.message)
      }
    },

    // 批量归档
    async handleBatchArchive() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要归档的发票')
        return
      }
      
      try {
        const invoiceIds = this.selectedRows.map(row => row.invoiceId)
        const archiveBasePath = `/archive/${new Date().getFullYear()}`
        const response = await batchArchiveInvoices(this.tenantId, invoiceIds, archiveBasePath)
        if (response.success) {
          this.$message.success(`批量归档完成，成功: ${response.data.successCount}，失败: ${response.data.failCount}`)
          this.handleRefresh()
        } else {
          this.$message.error('批量归档失败: ' + response.message)
        }
      } catch (error) {
        this.$message.error('批量归档失败: ' + error.message)
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (!this.hasSelection) {
        this.$message.warning('请先选择要删除的发票')
        return
      }
      
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条发票记录吗？`, '确认删除', {
          type: 'warning'
        })
        
        const invoiceIds = this.selectedRows.map(row => row.invoiceId)
        const response = await batchDeleteInvoices(this.tenantId, invoiceIds)
        if (response.success) {
          this.$message.success('批量删除成功')
          this.handleRefresh()
        } else {
          this.$message.error('批量删除失败: ' + response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 格式化方法
    formatInvoiceType: utils.formatInvoiceType,
    formatInvoiceStatus: utils.formatInvoiceStatus,
    formatOcrStatus: utils.formatOcrStatus,
    formatVerificationStatus: utils.formatVerificationStatus,
    formatRiskLevel: utils.formatRiskLevel,
    formatProcessingStatus: utils.formatProcessingStatus,
    formatAmount: utils.formatAmount,
    getInvoiceStatusColor: utils.getInvoiceStatusColor,
    getRiskLevelColor: utils.getRiskLevelColor,
    needsRetry: utils.needsRetry,

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return this.$moment(date).format('YYYY-MM-DD')
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-management-list {
  .search-card, .operation-card, .table-card {
    margin-bottom: 16px;
  }

  .operation-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .amount-text {
    font-weight: bold;
    color: #409eff;
  }

  .pagination-container {
    margin-top: 16px;
    text-align: right;
  }
}
</style>
