<template>
  <div class="invoice-ocr-management">
    <!-- 页面标题和操作按钮 -->
    <div class="page-header">
      <h3>发票OCR识别管理</h3>
      <div class="header-actions">
        <el-button type="primary" size="small" @click="handleBatchOcr">
          <i class="el-icon-view"></i> 批量识别
        </el-button>
        <el-button type="success" size="small" @click="handleUploadOcr">
          <i class="el-icon-upload"></i> 上传识别
        </el-button>
        <el-button type="info" size="small" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="识别状态">
          <el-select v-model="searchForm.ocrStatus" placeholder="请选择识别状态" clearable>
            <el-option label="待识别" value="PENDING" />
            <el-option label="识别中" value="PROCESSING" />
            <el-option label="识别成功" value="SUCCESS" />
            <el-option label="识别失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="发票类型">
          <el-select v-model="searchForm.invoiceType" placeholder="请选择发票类型" clearable>
            <el-option label="增值税专用发票" value="VAT_SPECIAL" />
            <el-option label="增值税普通发票" value="VAT_ORDINARY" />
            <el-option label="电子发票" value="ELECTRONIC" />
            <el-option label="其他发票" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="识别日期">
          <el-date-picker
            v-model="searchForm.ocrDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计信息 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">识别总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon success">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.successCount || 0 }}</div>
              <div class="stat-label">识别成功</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon processing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.processingCount || 0 }}</div>
              <div class="stat-label">识别中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon failed">
              <i class="el-icon-close"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.failedCount || 0 }}</div>
              <div class="stat-label">识别失败</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        ref="table"
        :data="tableData"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="fileName" label="文件名" width="200" show-overflow-tooltip />
        <el-table-column prop="invoiceType" label="发票类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getInvoiceTypeTagType(scope.row.invoiceType)">
              {{ formatInvoiceType(scope.row.invoiceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ocrStatus" label="识别状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getOcrStatusTagType(scope.row.ocrStatus)">
              {{ formatOcrStatus(scope.row.ocrStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="识别置信度" width="120">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.confidence"
              :status="getConfidenceStatus(scope.row.confidence)"
              :stroke-width="6"
            />
          </template>
        </el-table-column>
        <el-table-column prop="invoiceCode" label="发票代码" width="120" />
        <el-table-column prop="invoiceNumber" label="发票号码" width="120" />
        <el-table-column prop="invoiceAmount" label="发票金额" width="120">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.invoiceAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="ocrDate" label="识别时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.ocrStatus === 'PENDING'"
              type="primary"
              size="mini"
              @click="handleOcr(scope.row)"
            >
              识别
            </el-button>
            <el-button
              v-if="scope.row.ocrStatus === 'SUCCESS'"
              type="success"
              size="mini"
              @click="handleViewResult(scope.row)"
            >
              查看结果
            </el-button>
            <el-button
              v-if="scope.row.ocrStatus === 'FAILED'"
              type="warning"
              size="mini"
              @click="handleRetryOcr(scope.row)"
            >
              重试
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'preview', row: scope.row}">预览</el-dropdown-item>
                <el-dropdown-item :command="{action: 'download', row: scope.row}">下载</el-dropdown-item>
                <el-dropdown-item :command="{action: 'edit', row: scope.row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
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

    <!-- OCR结果查看对话框 -->
    <el-dialog
      title="OCR识别结果"
      :visible.sync="resultDialogVisible"
      width="800px"
      @closed="handleResultDialogClosed"
    >
      <div class="ocr-result-container">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="image-preview">
              <h4>原始图片</h4>
              <img :src="currentResult.imageUrl" alt="发票图片" class="invoice-image" />
            </div>
          </el-col>
          <el-col :span="12">
            <div class="result-details">
              <h4>识别结果</h4>
              <el-form :model="currentResult" label-width="100px" size="small">
                <el-form-item label="发票代码">
                  <el-input v-model="currentResult.invoiceCode" readonly />
                </el-form-item>
                <el-form-item label="发票号码">
                  <el-input v-model="currentResult.invoiceNumber" readonly />
                </el-form-item>
                <el-form-item label="开票日期">
                  <el-input v-model="currentResult.issueDate" readonly />
                </el-form-item>
                <el-form-item label="发票金额">
                  <el-input v-model="currentResult.invoiceAmount" readonly />
                </el-form-item>
                <el-form-item label="销售方名称">
                  <el-input v-model="currentResult.sellerName" readonly />
                </el-form-item>
                <el-form-item label="购买方名称">
                  <el-input v-model="currentResult.buyerName" readonly />
                </el-form-item>
                <el-form-item label="识别置信度">
                  <el-progress
                    :percentage="currentResult.confidence"
                    :status="getConfidenceStatus(currentResult.confidence)"
                  />
                </el-form-item>
              </el-form>
            </div>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resultDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleSaveResult">保存结果</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InvoiceOcrManagement',
  data() {
    return {
      loading: false,
      resultDialogVisible: false,
      selectedRows: [],
      searchForm: {
        ocrStatus: '',
        invoiceType: '',
        ocrDateRange: []
      },
      statistics: {
        totalCount: 856,
        successCount: 720,
        processingCount: 45,
        failedCount: 91
      },
      tableData: [
        {
          id: 1,
          fileName: 'invoice_001.pdf',
          invoiceType: 'VAT_SPECIAL',
          ocrStatus: 'SUCCESS',
          confidence: 95,
          invoiceCode: '144031909110',
          invoiceNumber: '19134556',
          invoiceAmount: 11800.00,
          ocrDate: '2024-01-15 14:30:25',
          imageUrl: '/static/images/invoice_sample.jpg',
          issueDate: '2024-01-15',
          sellerName: '北京科技有限公司',
          buyerName: '上海贸易有限公司'
        },
        {
          id: 2,
          fileName: 'invoice_002.jpg',
          invoiceType: 'VAT_ORDINARY',
          ocrStatus: 'PENDING',
          confidence: 0,
          invoiceCode: '',
          invoiceNumber: '',
          invoiceAmount: 0,
          ocrDate: '',
          imageUrl: '/static/images/invoice_sample2.jpg',
          issueDate: '',
          sellerName: '',
          buyerName: ''
        },
        {
          id: 3,
          fileName: 'invoice_003.png',
          invoiceType: 'ELECTRONIC',
          ocrStatus: 'FAILED',
          confidence: 0,
          invoiceCode: '',
          invoiceNumber: '',
          invoiceAmount: 0,
          ocrDate: '2024-01-14 16:45:12',
          imageUrl: '/static/images/invoice_sample3.jpg',
          issueDate: '',
          sellerName: '',
          buyerName: ''
        }
      ],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 856
      },
      currentResult: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      // 模拟API调用
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        ocrStatus: '',
        invoiceType: '',
        ocrDateRange: []
      }
      this.handleSearch()
    },
    refreshData() {
      this.loadData()
      this.$message.success('数据刷新成功')
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleBatchOcr() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要识别的发票')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedRows.length} 张发票进行OCR识别?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量OCR识别已启动')
        this.loadData()
      })
    },
    handleUploadOcr() {
      this.$message.info('上传OCR识别功能开发中...')
    },
    handleOcr(row) {
      this.$confirm('确认对此发票进行OCR识别?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('OCR识别已启动')
        this.loadData()
      })
    },
    handleRetryOcr(row) {
      this.$confirm('确认重新识别此发票?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('重新识别已启动')
        this.loadData()
      })
    },
    handleViewResult(row) {
      this.currentResult = { ...row }
      this.resultDialogVisible = true
    },
    handleSaveResult() {
      this.$message.success('识别结果保存成功')
      this.resultDialogVisible = false
      this.loadData()
    },
    handleResultDialogClosed() {
      this.currentResult = {}
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'preview':
          this.$message.info(`预览发票: ${row.fileName}`)
          break
        case 'download':
          this.$message.info(`下载发票: ${row.fileName}`)
          break
        case 'edit':
          this.$message.info(`编辑发票: ${row.fileName}`)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除此发票记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadData()
      })
    },
    formatInvoiceType(type) {
      const typeMap = {
        'VAT_SPECIAL': '增值税专用发票',
        'VAT_ORDINARY': '增值税普通发票',
        'ELECTRONIC': '电子发票',
        'OTHER': '其他发票'
      }
      return typeMap[type] || type
    },
    getInvoiceTypeTagType(type) {
      const typeMap = {
        'VAT_SPECIAL': 'primary',
        'VAT_ORDINARY': 'success',
        'ELECTRONIC': 'info',
        'OTHER': 'warning'
      }
      return typeMap[type] || ''
    },
    formatOcrStatus(status) {
      const statusMap = {
        'PENDING': '待识别',
        'PROCESSING': '识别中',
        'SUCCESS': '识别成功',
        'FAILED': '识别失败'
      }
      return statusMap[status] || status
    },
    getOcrStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'SUCCESS': 'success',
        'FAILED': 'danger'
      }
      return statusMap[status] || ''
    },
    getConfidenceStatus(confidence) {
      if (confidence >= 90) return 'success'
      if (confidence >= 70) return 'warning'
      if (confidence > 0) return 'exception'
      return ''
    },
    formatAmount(amount) {
      return amount ? amount.toLocaleString() : '0'
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-ocr-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h3 {
    margin: 0;
    color: #303133;
  }

  .header-actions {
    display: flex;
    gap: 10px;
  }
}

.search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;

  i {
    font-size: 20px;
    color: white;
  }

  &.total {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  &.success {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.processing {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }

  &.failed {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.table-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.ocr-result-container {
  .image-preview {
    h4 {
      margin: 0 0 15px 0;
      color: #303133;
    }

    .invoice-image {
      width: 100%;
      max-height: 400px;
      object-fit: contain;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
    }
  }

  .result-details {
    h4 {
      margin: 0 0 15px 0;
      color: #303133;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
