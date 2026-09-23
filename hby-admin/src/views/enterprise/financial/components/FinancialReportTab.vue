<template>
  <div class="financial-report-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>财务报告管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          刷新
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalReports || 0 }}</div>
              <div class="stat-label">报告总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.completedReports || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.inProgressReports || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #F56C6C 0%, #f78989 100%);">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.completionRate || 0 }}%</div>
              <div class="stat-label">完成率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card class="table-card">
      <div slot="header">
        <span>财务报告列表</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="createReport">新建报告</el-button>
          <el-button type="success" size="small" @click="batchExport">批量导出</el-button>
        </div>
      </div>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="报告类型">
          <el-select v-model="queryForm.reportType" placeholder="请选择报告类型" clearable>
            <el-option label="资产负债表" value="balance_sheet"></el-option>
            <el-option label="利润表" value="income_statement"></el-option>
            <el-option label="现金流量表" value="cash_flow"></el-option>
            <el-option label="财务分析报告" value="financial_analysis"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="draft"></el-option>
            <el-option label="待审核" value="pending"></el-option>
            <el-option label="已完成" value="completed"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="reportList" border v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="reportName" label="报告名称" width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reportType" label="报告类型" width="150">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeTag(scope.row.reportType)">
              {{ getReportTypeText(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="period" label="报告期间" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress || 0" :stroke-width="8"></el-progress>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewReport(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="editReport(scope.row)" v-if="scope.row.status === 'draft'">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="submitReport(scope.row)" v-if="scope.row.status === 'draft'">
              提交
            </el-button>
            <el-button type="text" size="small" @click="deleteReport(scope.row)" v-if="scope.row.status === 'draft'">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right;"
      ></el-pagination>
    </el-card>

    <!-- 新建/编辑报告对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新建报告' : '编辑报告'" :visible.sync="reportDialogVisible" width="600px">
      <el-form :model="reportForm" ref="reportForm" label-width="100px" :rules="reportRules">
        <el-form-item label="报告名称" prop="reportName">
          <el-input v-model="reportForm.reportName" placeholder="请输入报告名称"></el-input>
        </el-form-item>
        <el-form-item label="报告类型" prop="reportType">
          <el-select v-model="reportForm.reportType" placeholder="请选择报告类型" style="width: 100%;">
            <el-option label="资产负债表" value="balance_sheet"></el-option>
            <el-option label="利润表" value="income_statement"></el-option>
            <el-option label="现金流量表" value="cash_flow"></el-option>
            <el-option label="财务分析报告" value="financial_analysis"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间" prop="period">
          <el-date-picker
            v-model="reportForm.period"
            type="month"
            placeholder="请选择报告期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="reportForm.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReportForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 报告详情对话框 -->
    <el-dialog title="报告详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报告名称" :span="2">{{ currentRow.reportName }}</el-descriptions-item>
        <el-descriptions-item label="报告类型">{{ getReportTypeText(currentRow.reportType) }}</el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ currentRow.period }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusText(currentRow.status) }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ currentRow.creator }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ currentRow.auditStatus }}</el-descriptions-item>
        <el-descriptions-item label="审核人">{{ currentRow.auditor }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ formatDate(currentRow.auditTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核意见" :span="2">{{ currentRow.auditOpinion }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFinancialReportList,
  generateFinancialReport,
  updateFinancialReport,
  deleteFinancialReport,
  downloadFinancialReport
} from '@/api/enterprise/financial'

export default {
  name: 'FinancialReportTab',
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      reportList: [],
      total: 0,
      queryForm: {
        reportType: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      },
      overview: {
        totalReports: 0,
        completedReports: 0,
        inProgressReports: 0,
        completionRate: 0
      },
      // 对话框
      reportDialogVisible: false,
      detailDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      reportForm: {
        reportName: '',
        reportType: '',
        period: '',
        remark: ''
      },
      reportRules: {
        reportName: [{ required: true, message: '请输入报告名称', trigger: 'blur' }],
        reportType: [{ required: true, message: '请选择报告类型', trigger: 'change' }],
        period: [{ required: true, message: '请输入报告期间', trigger: 'blur' }]
      },
      multipleSelection: []
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.getList()
          this.loadOverview()
        }
      },
      immediate: true
    }
  },
  methods: {
    getList() {
      if (!this.enterpriseId) return
      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }
      getFinancialReportList(params).then(response => {
        const data = response.data || {}
        this.reportList = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    loadOverview() {
      if (!this.enterpriseId) return
      // 从列表数据中计算统计
      getFinancialReportList({ enterpriseId: this.enterpriseId, pageSize: 999 }).then(response => {
        const data = response.data || {}
        const list = data.tlist || data.records || []
        const completed = list.filter(r => r.status === 'completed' || r.status === 'published').length
        const inProgress = list.filter(r => r.status === 'draft' || r.status === 'pending').length
        this.overview = {
          totalReports: list.length,
          completedReports: completed,
          inProgressReports: inProgress,
          completionRate: list.length > 0 ? ((completed / list.length) * 100).toFixed(1) : 0
        }
      }).catch(() => {})
    },

    refreshData() {
      this.getList()
      this.loadOverview()
      this.$message.success('数据刷新成功')
    },

    queryData() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    resetQuery() {
      this.queryForm = {
        reportType: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },

    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.getList()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getList()
    },

    createReport() {
      this.dialogType = 'add'
      this.reportForm = {
        reportName: '',
        reportType: '',
        period: '',
        remark: ''
      }
      this.reportDialogVisible = true
    },

    editReport(row) {
      this.dialogType = 'edit'
      this.reportForm = { ...row }
      this.reportDialogVisible = true
    },

    submitReportForm() {
      this.$refs.reportForm.validate(valid => {
        if (!valid) return
        const data = {
          ...this.reportForm,
          enterpriseId: this.enterpriseId,
          enterpriseName: this.enterpriseName,
          creator: this.enterpriseName + '管理员'
        }
        const api = this.dialogType === 'edit' ? updateFinancialReport : generateFinancialReport
        api(data).then(() => {
          this.$message.success(this.dialogType === 'add' ? '报告创建成功' : '报告更新成功')
          this.reportDialogVisible = false
          this.getList()
          this.loadOverview()
          this.$emit('refresh')
        }).catch(() => {
          this.$message.error('操作失败')
        })
      })
    },

    submitReport(row) {
      this.$confirm('确认提交该报告进行审核？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        generateFinancialReport({
          reportId: row.reportId,
          status: 'pending'
        }).then(() => {
          this.$message.success('报告已提交审核')
          this.getList()
          this.loadOverview()
        }).catch(() => {
          this.$message.error('提交失败')
        })
      })
    },

    viewReport(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    deleteReport(row) {
      this.$confirm('确认删除该财务报告？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancialReport(row.reportId).then(() => {
          this.$message.success('删除成功')
          this.getList()
          this.loadOverview()
          this.$emit('refresh')
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },

    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    batchExport() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要导出的报告')
        return
      }
      const headers = ['报告名称', '报告类型', '报告期间', '状态', '创建人', '创建时间', '进度', '审核状态']
      const rows = this.multipleSelection.map(row => {
        return [
          row.reportName || '',
          this.getReportTypeText(row.reportType),
          row.period || '',
          this.getStatusText(row.status),
          row.creator || '',
          row.createTime || '',
          (row.progress || 0) + '%',
          row.auditStatus || ''
        ].map(cell => '"' + String(cell).replace(/"/g, '""') + '"').join(',')
      })
      const csvContent = '\uFEFF' + headers.join(',') + '\n' + rows.join('\n')
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const now = new Date()
      const dateStr = now.getFullYear() +
        String(now.getMonth() + 1).padStart(2, '0') +
        String(now.getDate()).padStart(2, '0')
      const fileName = '财务报告_' + (this.enterpriseName || '企业') + '_' + dateStr + '.csv'
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(link.href)
      this.$message.success('导出成功，共导出' + this.multipleSelection.length + '条报告')
    },

    formatDate(dateStr) {
      if (!dateStr) return '-'
      if (typeof dateStr === 'string') return dateStr.replace('T', ' ').substring(0, 19)
      return dateStr
    },

    getReportTypeTag(type) {
      const typeMap = {
        'balance_sheet': 'primary',
        'income_statement': 'success',
        'cash_flow': 'warning',
        'financial_analysis': 'info',
        'BALANCE_SHEET': 'primary',
        'INCOME_STATEMENT': 'success',
        'CASH_FLOW': 'warning',
        'FINANCIAL_ANALYSIS': 'info'
      }
      return typeMap[type] || 'info'
    },

    getReportTypeText(type) {
      const textMap = {
        'balance_sheet': '资产负债表',
        'income_statement': '利润表',
        'cash_flow': '现金流量表',
        'financial_analysis': '财务分析报告',
        'BALANCE_SHEET': '资产负债表',
        'INCOME_STATEMENT': '利润表',
        'CASH_FLOW': '现金流量表',
        'FINANCIAL_ANALYSIS': '财务分析报告'
      }
      return textMap[type] || type
    },

    getStatusType(status) {
      const statusMap = {
        'draft': 'info',
        'pending': 'warning',
        'completed': 'success',
        'published': 'primary',
        'DRAFT': 'info',
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'PUBLISHED': 'primary'
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'draft': '草稿',
        'pending': '待审核',
        'completed': '已完成',
        'published': '已发布',
        'DRAFT': '草稿',
        'PENDING': '待审核',
        'COMPLETED': '已完成',
        'PUBLISHED': '已发布'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style scoped>
.financial-report-tab {
  padding: 20px;
}
.overview-card {
  margin-bottom: 20px;
}
.stat-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
  margin-right: 12px;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
  min-width: 0;
}
.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
  margin-bottom: 4px;
}
.stat-label {
  font-size: 13px;
  color: #909399;
}
.table-card {
  margin-bottom: 20px;
}
.query-form {
  margin-bottom: 20px;
}
</style>
