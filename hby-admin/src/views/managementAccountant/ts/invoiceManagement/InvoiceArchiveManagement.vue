<template>
  <div class="invoice-archive-management">
    <!-- 页面标题和操作按钮 -->
    <div class="page-header">
      <h3>发票归档管理</h3>
      <div class="header-actions">
        <el-button type="primary" size="small" @click="handleBatchArchive">
          <i class="el-icon-folder-add"></i> 批量归档
        </el-button>
        <el-button type="success" size="small" @click="handleExportArchive">
          <i class="el-icon-download"></i> 导出归档
        </el-button>
        <el-button type="info" size="small" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="归档状态">
          <el-select v-model="searchForm.archiveStatus" placeholder="请选择归档状态" clearable>
            <el-option label="未归档" value="NOT_ARCHIVED" />
            <el-option label="已归档" value="ARCHIVED" />
            <el-option label="归档中" value="ARCHIVING" />
            <el-option label="归档失败" value="ARCHIVE_FAILED" />
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
        <el-form-item label="归档日期">
          <el-date-picker
            v-model="searchForm.archiveDateRange"
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
              <div class="stat-label">发票总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon archived">
              <i class="el-icon-folder-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.archivedCount || 0 }}</div>
              <div class="stat-label">已归档</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-folder-add"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.pendingCount || 0 }}</div>
              <div class="stat-label">待归档</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon failed">
              <i class="el-icon-folder-remove"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.failedCount || 0 }}</div>
              <div class="stat-label">归档失败</div>
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
        <el-table-column prop="invoiceCode" label="发票代码" width="120" />
        <el-table-column prop="invoiceNumber" label="发票号码" width="120" />
        <el-table-column prop="invoiceType" label="发票类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getInvoiceTypeTagType(scope.row.invoiceType)">
              {{ formatInvoiceType(scope.row.invoiceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="invoiceAmount" label="发票金额" width="120">
          <template slot-scope="scope">
            ¥{{ formatAmount(scope.row.invoiceAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="开票日期" width="120" />
        <el-table-column prop="archiveStatus" label="归档状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getArchiveStatusTagType(scope.row.archiveStatus)">
              {{ formatArchiveStatus(scope.row.archiveStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="archiveDate" label="归档日期" width="120" />
        <el-table-column prop="archivePath" label="归档路径" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.archiveStatus === 'NOT_ARCHIVED'"
              type="primary"
              size="mini"
              @click="handleArchive(scope.row)"
            >
              归档
            </el-button>
            <el-button
              v-if="scope.row.archiveStatus === 'ARCHIVED'"
              type="success"
              size="mini"
              @click="handleViewArchive(scope.row)"
            >
              查看
            </el-button>
            <el-button
              v-if="scope.row.archiveStatus === 'ARCHIVE_FAILED'"
              type="warning"
              size="mini"
              @click="handleRetryArchive(scope.row)"
            >
              重试
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'detail', row: scope.row}">详情</el-dropdown-item>
                <el-dropdown-item :command="{action: 'download', row: scope.row}">下载</el-dropdown-item>
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

    <!-- 归档配置对话框 -->
    <el-dialog
      title="归档配置"
      :visible.sync="archiveDialogVisible"
      width="600px"
      @closed="handleArchiveDialogClosed"
    >
      <el-form :model="archiveForm" :rules="archiveRules" ref="archiveForm" label-width="120px">
        <el-form-item label="归档路径" prop="archivePath">
          <el-input v-model="archiveForm.archivePath" placeholder="请输入归档路径" />
        </el-form-item>
        <el-form-item label="归档策略" prop="archiveStrategy">
          <el-select v-model="archiveForm.archiveStrategy" placeholder="请选择归档策略">
            <el-option label="按日期归档" value="BY_DATE" />
            <el-option label="按类型归档" value="BY_TYPE" />
            <el-option label="按金额归档" value="BY_AMOUNT" />
            <el-option label="自定义归档" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="压缩方式" prop="compressionType">
          <el-select v-model="archiveForm.compressionType" placeholder="请选择压缩方式">
            <el-option label="不压缩" value="NONE" />
            <el-option label="ZIP压缩" value="ZIP" />
            <el-option label="RAR压缩" value="RAR" />
            <el-option label="7Z压缩" value="7Z" />
          </el-select>
        </el-form-item>
        <el-form-item label="归档备注" prop="archiveRemark">
          <el-input
            v-model="archiveForm.archiveRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入归档备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="archiveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmArchive" :loading="archiving">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InvoiceArchiveManagement',
  data() {
    return {
      loading: false,
      archiving: false,
      archiveDialogVisible: false,
      selectedRows: [],
      searchForm: {
        archiveStatus: '',
        invoiceType: '',
        archiveDateRange: []
      },
      statistics: {
        totalCount: 1256,
        archivedCount: 980,
        pendingCount: 234,
        failedCount: 42
      },
      tableData: [
        {
          id: 1,
          invoiceCode: '144031909110',
          invoiceNumber: '19134556',
          invoiceType: 'VAT_SPECIAL',
          invoiceAmount: 11800.00,
          issueDate: '2024-01-15',
          archiveStatus: 'ARCHIVED',
          archiveDate: '2024-01-16',
          archivePath: '/archive/2024/01/VAT_SPECIAL/19134556.pdf'
        },
        {
          id: 2,
          invoiceCode: '144031909111',
          invoiceNumber: '19134557',
          invoiceType: 'VAT_ORDINARY',
          invoiceAmount: 5600.00,
          issueDate: '2024-01-15',
          archiveStatus: 'NOT_ARCHIVED',
          archiveDate: '',
          archivePath: ''
        },
        {
          id: 3,
          invoiceCode: '144031909112',
          invoiceNumber: '19134558',
          invoiceType: 'ELECTRONIC',
          invoiceAmount: 2300.00,
          issueDate: '2024-01-14',
          archiveStatus: 'ARCHIVE_FAILED',
          archiveDate: '',
          archivePath: ''
        }
      ],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 1256
      },
      archiveForm: {
        archivePath: '/archive/invoices',
        archiveStrategy: 'BY_DATE',
        compressionType: 'ZIP',
        archiveRemark: ''
      },
      archiveRules: {
        archivePath: [
          { required: true, message: '请输入归档路径', trigger: 'blur' }
        ],
        archiveStrategy: [
          { required: true, message: '请选择归档策略', trigger: 'change' }
        ],
        compressionType: [
          { required: true, message: '请选择压缩方式', trigger: 'change' }
        ]
      }
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
        archiveStatus: '',
        invoiceType: '',
        archiveDateRange: []
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
    handleBatchArchive() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要归档的发票')
        return
      }
      this.archiveDialogVisible = true
    },
    handleArchive(row) {
      this.selectedRows = [row]
      this.archiveDialogVisible = true
    },
    handleConfirmArchive() {
      this.$refs.archiveForm.validate((valid) => {
        if (valid) {
          this.archiving = true
          // 模拟归档操作
          setTimeout(() => {
            this.archiving = false
            this.archiveDialogVisible = false
            this.$message.success(`成功归档 ${this.selectedRows.length} 张发票`)
            this.loadData()
          }, 2000)
        }
      })
    },
    handleArchiveDialogClosed() {
      this.$refs.archiveForm.resetFields()
      this.selectedRows = []
    },
    handleViewArchive(row) {
      this.$message.info(`查看归档文件: ${row.archivePath}`)
    },
    handleRetryArchive(row) {
      this.$confirm('确认重新归档此发票?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('重新归档成功')
        this.loadData()
      })
    },
    handleExportArchive() {
      this.$message.success('归档数据导出功能开发中...')
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'detail':
          this.$message.info(`查看发票详情: ${row.invoiceNumber}`)
          break
        case 'download':
          this.$message.info(`下载发票: ${row.invoiceNumber}`)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除此发票归档记录?', '提示', {
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
    formatArchiveStatus(status) {
      const statusMap = {
        'NOT_ARCHIVED': '未归档',
        'ARCHIVED': '已归档',
        'ARCHIVING': '归档中',
        'ARCHIVE_FAILED': '归档失败'
      }
      return statusMap[status] || status
    },
    getArchiveStatusTagType(status) {
      const statusMap = {
        'NOT_ARCHIVED': 'info',
        'ARCHIVED': 'success',
        'ARCHIVING': 'warning',
        'ARCHIVE_FAILED': 'danger'
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
.invoice-archive-management {
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

  &.archived {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.pending {
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

.dialog-footer {
  text-align: right;
}
</style>
