<template>
  <div class="invoice-verification-management">
    <div class="page-header">
      <h3>发票验证管理</h3>
      <div class="header-actions">
        <el-button type="primary" size="small" @click="handleBatchVerify">
          <i class="el-icon-check"></i> 批量验证
        </el-button>
        <el-button type="success" size="small" @click="handleExportReport">
          <i class="el-icon-download"></i> 导出报告
        </el-button>
        <el-button type="info" size="small" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
    </div>

    <!-- 验证统计 -->
    <div class="verification-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card total">
            <div class="stat-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.total || 0 }}</div>
              <div class="stat-label">验证总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card verified">
            <div class="stat-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.verified || 0 }}</div>
              <div class="stat-label">验证通过</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card failed">
            <div class="stat-icon">
              <i class="el-icon-close"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.failed || 0 }}</div>
              <div class="stat-label">验证失败</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card pending">
            <div class="stat-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.pending || 0 }}</div>
              <div class="stat-label">待验证</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索筛选 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="验证状态">
          <el-select v-model="searchForm.verifyStatus" placeholder="请选择验证状态" clearable>
            <el-option label="待验证" value="PENDING" />
            <el-option label="验证中" value="VERIFYING" />
            <el-option label="验证通过" value="VERIFIED" />
            <el-option label="验证失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="发票类型">
          <el-select v-model="searchForm.invoiceType" placeholder="请选择发票类型" clearable>
            <el-option label="增值税专用发票" value="VAT_SPECIAL" />
            <el-option label="增值税普通发票" value="VAT_ORDINARY" />
            <el-option label="电子发票" value="ELECTRONIC" />
          </el-select>
        </el-form-item>
        <el-form-item label="验证日期">
          <el-date-picker
            v-model="searchForm.verifyDateRange"
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
        <el-table-column prop="invoiceCode" label="发票代码" width="120" />
        <el-table-column prop="invoiceNumber" label="发票号码" width="120" />
        <el-table-column prop="invoiceType" label="发票类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getInvoiceTypeTagType(scope.row.invoiceType)">
              {{ formatInvoiceType(scope.row.invoiceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="verifyStatus" label="验证状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getVerifyStatusTagType(scope.row.verifyStatus)">
              {{ formatVerifyStatus(scope.row.verifyStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="invoiceAmount" label="发票金额" width="120">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.invoiceAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="开票日期" width="120" />
        <el-table-column prop="verifyDate" label="验证日期" width="150" />
        <el-table-column prop="verifyResult" label="验证结果" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.verifyStatus === 'PENDING'"
              type="primary"
              size="mini"
              @click="handleVerify(scope.row)"
            >
              验证
            </el-button>
            <el-button
              v-if="scope.row.verifyStatus === 'VERIFIED'"
              type="success"
              size="mini"
              @click="handleViewDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              v-if="scope.row.verifyStatus === 'FAILED'"
              type="warning"
              size="mini"
              @click="handleRetryVerify(scope.row)"
            >
              重试
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'history', row: scope.row}">验证历史</el-dropdown-item>
                <el-dropdown-item :command="{action: 'download', row: scope.row}">下载报告</el-dropdown-item>
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

    <!-- 验证详情对话框 -->
    <el-dialog
      title="验证详情"
      :visible.sync="detailDialogVisible"
      width="700px"
      @closed="handleDetailDialogClosed"
    >
      <div class="verification-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="发票代码">{{ currentDetail.invoiceCode }}</el-descriptions-item>
          <el-descriptions-item label="发票号码">{{ currentDetail.invoiceNumber }}</el-descriptions-item>
          <el-descriptions-item label="开票日期">{{ currentDetail.issueDate }}</el-descriptions-item>
          <el-descriptions-item label="验证日期">{{ currentDetail.verifyDate }}</el-descriptions-item>
          <el-descriptions-item label="验证状态">
            <el-tag :type="getVerifyStatusTagType(currentDetail.verifyStatus)">
              {{ formatVerifyStatus(currentDetail.verifyStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="验证方式">{{ currentDetail.verifyMethod }}</el-descriptions-item>
          <el-descriptions-item label="验证结果" :span="2">{{ currentDetail.verifyResult }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="verification-log" v-if="currentDetail.verifyLog">
          <h4>验证日志</h4>
          <el-input
            type="textarea"
            :rows="6"
            :value="currentDetail.verifyLog"
            readonly
          />
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InvoiceVerificationManagement',
  data() {
    return {
      loading: false,
      detailDialogVisible: false,
      selectedRows: [],
      searchForm: {
        verifyStatus: '',
        invoiceType: '',
        verifyDateRange: []
      },
      stats: {
        total: 1256,
        verified: 1089,
        failed: 67,
        pending: 100
      },
      tableData: [
        {
          id: 1,
          invoiceCode: '144031909110',
          invoiceNumber: '19134556',
          invoiceType: 'VAT_SPECIAL',
          verifyStatus: 'VERIFIED',
          invoiceAmount: 11800.00,
          issueDate: '2024-01-15',
          verifyDate: '2024-01-15 14:30:25',
          verifyResult: '验证通过，发票信息真实有效',
          verifyMethod: '税务局接口验证',
          verifyLog: '2024-01-15 14:30:20 开始验证\n2024-01-15 14:30:22 连接税务局接口\n2024-01-15 14:30:25 验证完成，结果：通过'
        },
        {
          id: 2,
          invoiceCode: '144031909111',
          invoiceNumber: '19134557',
          invoiceType: 'VAT_ORDINARY',
          verifyStatus: 'PENDING',
          invoiceAmount: 5600.00,
          issueDate: '2024-01-15',
          verifyDate: '',
          verifyResult: '',
          verifyMethod: '',
          verifyLog: ''
        },
        {
          id: 3,
          invoiceCode: '144031909112',
          invoiceNumber: '19134558',
          invoiceType: 'ELECTRONIC',
          verifyStatus: 'FAILED',
          invoiceAmount: 2300.00,
          issueDate: '2024-01-14',
          verifyDate: '2024-01-14 16:45:32',
          verifyResult: '验证失败，发票代码不存在',
          verifyMethod: '税务局接口验证',
          verifyLog: '2024-01-14 16:45:30 开始验证\n2024-01-14 16:45:31 连接税务局接口\n2024-01-14 16:45:32 验证失败，错误码：404'
        }
      ],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 1256
      },
      currentDetail: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
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
        verifyStatus: '',
        invoiceType: '',
        verifyDateRange: []
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
    handleBatchVerify() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要验证的发票')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedRows.length} 张发票进行验证?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量验证已启动')
        this.loadData()
      })
    },
    handleVerify(row) {
      this.$confirm('确认验证此发票?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('发票验证已启动')
        this.loadData()
      })
    },
    handleRetryVerify(row) {
      this.$confirm('确认重新验证此发票?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('重新验证已启动')
        this.loadData()
      })
    },
    handleViewDetail(row) {
      this.currentDetail = { ...row }
      this.detailDialogVisible = true
    },
    handleDetailDialogClosed() {
      this.currentDetail = {}
    },
    handleExportReport() {
      this.$message.success('验证报告导出功能开发中...')
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'history':
          this.$message.info(`查看验证历史: ${row.invoiceNumber}`)
          break
        case 'download':
          this.$message.info(`下载验证报告: ${row.invoiceNumber}`)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除此验证记录?', '提示', {
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
        'ELECTRONIC': '电子发票'
      }
      return typeMap[type] || type
    },
    getInvoiceTypeTagType(type) {
      const typeMap = {
        'VAT_SPECIAL': 'primary',
        'VAT_ORDINARY': 'success',
        'ELECTRONIC': 'info'
      }
      return typeMap[type] || ''
    },
    formatVerifyStatus(status) {
      const statusMap = {
        'PENDING': '待验证',
        'VERIFYING': '验证中',
        'VERIFIED': '验证通过',
        'FAILED': '验证失败'
      }
      return statusMap[status] || status
    },
    getVerifyStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'VERIFYING': 'warning',
        'VERIFIED': 'success',
        'FAILED': 'danger'
      }
      return statusMap[status] || ''
    },
    formatAmount(amount) {
      return amount ? amount.toLocaleString() : '0'
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-verification-management {
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

.verification-stats {
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

  &.total {
    border-left: 4px solid #409eff;
  }

  &.verified {
    border-left: 4px solid #67c23a;
  }

  &.failed {
    border-left: 4px solid #f56c6c;
  }

  &.pending {
    border-left: 4px solid #e6a23c;
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
  color: white;

  i {
    font-size: 20px;
  }

  .total & {
    background: #409eff;
  }

  .verified & {
    background: #67c23a;
  }

  .failed & {
    background: #f56c6c;
  }

  .pending & {
    background: #e6a23c;
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

.search-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
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

.verification-detail {
  .verification-log {
    margin-top: 20px;

    h4 {
      margin: 0 0 10px 0;
      color: #303133;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
