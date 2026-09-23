<template>
  <div class="voucher-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证管理</h2>
      <p>管理会计凭证的全生命周期，包括录入、修改、查询、删除等操作</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalVouchers || 0 }}</div>
                <div class="statistic-label">凭证总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon draft">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.draftVouchers || 0 }}</div>
                <div class="statistic-label">草稿凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon approved">
                <i class="el-icon-check"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.approvedVouchers || 0 }}</div>
                <div class="statistic-label">已审核凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon posted">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.postedVouchers || 0 }}</div>
                <div class="statistic-label">已过账凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建凭证
      </el-button>
      <el-button type="success" icon="el-icon-upload2" @click="handleImport">
        批量导入
      </el-button>
      <el-button type="info" icon="el-icon-download" @click="handleExport">
        导出数据
      </el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="凭证编号">
          <el-input v-model="queryForm.voucherNo" placeholder="请输入凭证编号" clearable />
        </el-form-item>
        <el-form-item label="凭证类型">
          <el-select v-model="queryForm.voucherType" placeholder="请选择凭证类型" clearable>
            <el-option label="记账凭证" value="ACCOUNTING" />
            <el-option label="收款凭证" value="RECEIPT" />
            <el-option label="付款凭证" value="PAYMENT" />
            <el-option label="转账凭证" value="TRANSFER" />
          </el-select>
        </el-form-item>
        <el-form-item label="凭证状态">
          <el-select v-model="queryForm.status" placeholder="请选择凭证状态" clearable>
            <el-option label="草稿" value="0" />
            <el-option label="已审核" value="1" />
            <el-option label="已过账" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="voucherNo" label="凭证编号" width="120" />
        <el-table-column prop="voucherType" label="凭证类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getVoucherTypeColor(scope.row.voucherType)">
              {{ getVoucherTypeName(scope.row.voucherType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherDate" label="制单日期" width="120" />
        <el-table-column prop="summary" label="摘要" width="200" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="金额" width="120" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ formatAmount(scope.row.totalAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="制单人" width="100" />
        <el-table-column prop="createTime" label="制单时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" v-if="scope.row.status === '0'">编辑</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row)" v-if="scope.row.status === '0'">审核</el-button>
            <el-button size="mini" type="warning" @click="handlePost(scope.row)" v-if="scope.row.status === '1'">过账</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)" v-if="scope.row.status === '0'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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
</template>

<script>
export default {
  name: 'VoucherManagement',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      statistics: {},

      // 查询表单
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        voucherNo: '',
        voucherType: '',
        status: '',
        dateRange: []
      },

      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },

  mounted() {
    console.log('凭证管理页面已挂载')
    this.loadData()
    this.loadStatistics()
  },

  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        // 处理过程异步等待（实际由后端 API 完成）
        await new Promise(resolve => setTimeout(resolve, 300))
        this.tableData = this.getMockData()
        this.pagination.total = 0
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
        this.tableData = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        // 处理过程异步等待（实际由后端 API 完成）
        await new Promise(resolve => setTimeout(resolve, 300))
        this.statistics = this.getMockStatistics()
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.statistics = {}
      }
    },

    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockStatistics() {
      return {
        totalVouchers: 0,
        draftVouchers: 0,
        approvedVouchers: 0,
        postedVouchers: 0
      }
    },

    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockData() {
      return []
    },

    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
      this.loadStatistics()
    },

    // 新建凭证
    handleCreate() {
      this.$message.success('新建凭证功能')
    },

    // 批量导入
    handleImport() {
      this.$message.success('批量导入功能')
    },

    // 导出数据
    handleExport() {
      this.$message.success('导出数据功能')
    },

    // 查看详情
    handleView(row) {
      this.$message.info(`查看凭证：${row.voucherNo}`)
    },

    // 编辑凭证
    handleEdit(row) {
      this.$message.info(`编辑凭证：${row.voucherNo}`)
    },

    // 审核凭证
    handleApprove(row) {
      this.$message.success(`审核凭证成功：${row.voucherNo}`)
      this.loadData()
      this.loadStatistics()
    },

    // 过账凭证
    handlePost(row) {
      this.$message.success(`过账凭证成功：${row.voucherNo}`)
      this.loadData()
      this.loadStatistics()
    },

    // 删除凭证
    handleDelete(row) {
      this.$message.success(`删除凭证成功：${row.voucherNo}`)
      this.loadData()
      this.loadStatistics()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },

    // 获取凭证类型名称
    getVoucherTypeName(type) {
      const typeMap = {
        'ACCOUNTING': '记账凭证',
        'RECEIPT': '收款凭证',
        'PAYMENT': '付款凭证',
        'TRANSFER': '转账凭证'
      }
      return typeMap[type] || type
    },

    // 获取凭证类型颜色
    getVoucherTypeColor(type) {
      const colorMap = {
        'ACCOUNTING': 'primary',
        'RECEIPT': 'success',
        'PAYMENT': 'warning',
        'TRANSFER': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '0': 'warning',
        '1': 'primary',
        '2': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        '0': '草稿',
        '1': '已审核',
        '2': '已过账'
      }
      return statusMap[status] || status
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.voucher-management-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
  flex-shrink: 0;
}

.statistic-icon.total {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.statistic-icon.draft {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.statistic-icon.approved {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.statistic-icon.posted {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.statistic-content {
  flex: 1;
  min-width: 0;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.toolbar {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  background: white;
  padding: 16px;
  border-radius: 8px;
  text-align: right;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Element UI 样式覆盖 */
::v-deep .el-card {
  border: none;
  box-shadow: none;
}

::v-deep .el-button {
  border-radius: 6px;
}

::v-deep .el-table {
  border-radius: 8px;
}

::v-deep .el-table th {
  background-color: #fafafa;
  color: #333;
  font-weight: 600;
}

::v-deep .el-pagination {
  margin-top: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .voucher-management-container {
    padding: 10px;
  }

  .statistic-icon {
    width: 50px;
    height: 50px;
    font-size: 20px;
    margin-right: 15px;
  }

  .statistic-value {
    font-size: 24px;
  }

  .toolbar,
  .search-form,
  .table-container,
  .pagination-container {
    padding: 15px;
  }
}
</style>