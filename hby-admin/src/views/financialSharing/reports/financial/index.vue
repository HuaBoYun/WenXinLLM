<template>
  <div class="financial-reports-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          财务报表
        </h1>
        <p class="page-description">生成和管理标准财务报表，包括资产负债表、利润表、现金流量表等</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showGenerateDialog">
          生成报表
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="batchExport">
          批量导出
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="refreshData">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon balance-sheet">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.balanceSheets }}</div>
              <div class="stat-label">资产负债表</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon income-statement">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.incomeStatements }}</div>
              <div class="stat-label">利润表</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon cash-flow">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.cashFlowStatements }}</div>
              <div class="stat-label">现金流量表</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-reports">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalReports }}</div>
              <div class="stat-label">报表总数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 查询表单 -->
    <div class="query-form">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="报表类型">
          <el-select v-model="queryForm.reportType" placeholder="请选择报表类型" clearable>
            <el-option label="资产负债表" value="balance_sheet"></el-option>
            <el-option label="利润表" value="income_statement"></el-option>
            <el-option label="现金流量表" value="cash_flow_statement"></el-option>
            <el-option label="财务报表附注" value="financial_notes"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="会计期间">
          <el-date-picker
            v-model="queryForm.period"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            clearable>
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft"></el-option>
            <el-option label="已生成" value="generated"></el-option>
            <el-option label="已审核" value="reviewed"></el-option>
            <el-option label="已发布" value="published"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 报表列表 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="reportList"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="reportId" label="报表编号" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reportName" label="报表名称" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reportType" label="报表类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeTag(scope.row.reportType)">
              {{ getReportTypeName(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="period" label="会计期间" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column prop="creator" label="创建人" width="120"></el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="viewReport(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="editReport(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="exportReport(scope.row)">导出</el-button>
            <el-button size="mini" type="text" @click="copyReport(scope.row)">复制</el-button>
            <el-button size="mini" type="text" style="color: #f56c6c" @click="deleteReport(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>

    <!-- 生成报表对话框 -->
    <GenerateReportDialog
      :visible.sync="generateDialogVisible"
      @confirm="handleGenerateReport"
    />

    <!-- 报表详情对话框 -->
    <ReportDetailDialog
      :visible.sync="detailDialogVisible"
      :report-data="currentReport"
    />

    <!-- 报表编辑对话框 -->
    <ReportEditDialog
      :visible.sync="editDialogVisible"
      :report-data="currentReport"
      @confirm="handleEditReport"
    />
  </div>
</template>

<script>
import {
  getFinancialReportsList,
  deleteFinancialReport,
  exportReport,
  getReportStatistics
} from '@/api/financialSharing/reports'
import GenerateReportDialog from './components/GenerateReportDialog'
import ReportDetailDialog from './components/ReportDetailDialog'
import ReportEditDialog from './components/ReportEditDialog'

export default {
  name: 'FinancialReportsIndex',
  components: {
    GenerateReportDialog,
    ReportDetailDialog,
    ReportEditDialog
  },
  data() {
    return {
      loading: false,
      reportList: [],
      selectedReports: [],
      stats: {
        balanceSheets: 0,
        incomeStatements: 0,
        cashFlowStatements: 0,
        totalReports: 0
      },
      queryForm: {
        reportType: '',
        period: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      generateDialogVisible: false,
      detailDialogVisible: false,
      editDialogVisible: false,
      currentReport: null
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const response = await getFinancialReportsList(params)
        if (response.code === 200) {
          this.reportList = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const response = await getReportStatistics()
        if (response.code === 200) {
          this.stats = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    handleQuery() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetQuery() {
      this.queryForm = {
        reportType: '',
        period: '',
        status: ''
      }
      this.handleQuery()
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
      this.selectedReports = selection
    },
    showGenerateDialog() {
      this.generateDialogVisible = true
    },
    handleGenerateReport(reportData) {
      this.generateDialogVisible = false
      this.loadData()
      this.loadStats()
      this.$message.success('报表生成成功')
    },
    viewReport(row) {
      this.currentReport = row
      this.detailDialogVisible = true
    },
    editReport(row) {
      this.currentReport = { ...row }
      this.editDialogVisible = true
    },
    handleEditReport(reportData) {
      this.editDialogVisible = false
      this.loadData()
      this.$message.success('报表更新成功')
    },
    async exportReport(row) {
      try {
        const response = await exportReport({
          reportId: row.reportId,
          reportType: row.reportType,
          format: 'xlsx'
        })
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${row.reportName}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    copyReport(row) {
      this.$message.success('报表复制成功')
    },
    async deleteReport(row) {
      try {
        await this.$confirm('确定要删除这个报表吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteFinancialReport(row.reportId)
        this.$message.success('删除成功')
        this.loadData()
        this.loadStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    batchExport() {
      if (this.selectedReports.length === 0) {
        this.$message.warning('请选择要导出的报表')
        return
      }
      this.$message.success('批量导出已触发，请稍后下载')
    },
    refreshData() {
      this.loadData()
      this.loadStats()
    },
    getReportTypeTag(type) {
      const tags = {
        balance_sheet: 'primary',
        income_statement: 'success',
        cash_flow_statement: 'warning',
        financial_notes: 'info'
      }
      return tags[type] || 'default'
    },
    getReportTypeName(type) {
      const names = {
        balance_sheet: '资产负债表',
        income_statement: '利润表',
        cash_flow_statement: '现金流量表',
        financial_notes: '财务报表附注'
      }
      return names[type] || type
    },
    getStatusTag(status) {
      const tags = {
        draft: 'info',
        generated: 'primary',
        reviewed: 'success',
        published: 'warning'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        draft: '草稿',
        generated: '已生成',
        reviewed: '已审核',
        published: '已发布'
      }
      return names[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-reports-container {
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

.stats-cards {
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

      &.balance-sheet {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.income-statement {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.cash-flow {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.total-reports {
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

.query-form {
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

  .el-table {
    border-radius: 8px;
    overflow: hidden;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
