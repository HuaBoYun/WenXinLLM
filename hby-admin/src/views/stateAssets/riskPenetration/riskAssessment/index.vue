<template>
  <div class="risk-assessment-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-data-analysis"></i>
          风险评估管理
        </h2>
        <p class="page-description">国资国企穿透式监管 - 风险评估与分析</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增评估
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="企业名称" prop="enterpriseId">
          <CompanyTreeModal
            v-model="queryForm.enterpriseId"
            :company-name.sync="queryForm.enterpriseName"
            placeholder="请选择企业"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="评估类型" prop="assessmentType">
          <el-select v-model="queryForm.assessmentType" placeholder="请选择评估类型" clearable>
            <el-option label="综合评估" value="COMPREHENSIVE" />
            <el-option label="财务风险评估" value="FINANCIAL" />
            <el-option label="经营风险评估" value="OPERATIONAL" />
            <el-option label="合规风险评估" value="COMPLIANCE" />
            <el-option label="治理风险评估" value="GOVERNANCE" />
            <el-option label="外部风险评估" value="EXTERNAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="overallRiskLevel">
          <el-select v-model="queryForm.overallRiskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="极低风险" value="VERY_LOW" />
            <el-option label="低风险" value="LOW" />
            <el-option label="中等风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="极高风险" value="VERY_HIGH" />
            <el-option label="临界风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估年度" prop="assessmentYear">
          <el-date-picker
            v-model="queryForm.assessmentYear"
            type="year"
            placeholder="请选择评估年度"
            value-format="yyyy"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预警状态" prop="isWarningTriggered">
          <el-select v-model="queryForm.isWarningTriggered" placeholder="请选择预警状态" clearable>
            <el-option label="已触发预警" :value="true" />
            <el-option label="未触发预警" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-container">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card stat-card-primary">
            <div class="stat-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalAssessments || 0 }}</div>
              <div class="stat-label">总评估数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-warning">
            <div class="stat-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.warningCount || 0 }}</div>
              <div class="stat-label">预警数量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-danger">
            <div class="stat-icon">
              <i class="el-icon-error"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.highRiskCount || 0 }}</div>
              <div class="stat-label">高风险数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card stat-card-success">
            <div class="stat-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.completedCount || 0 }}</div>
              <div class="stat-label">已完成数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="enterpriseName" label="企业名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="assessmentType" label="评估类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAssessmentTypeTagType(scope.row.assessmentType)">
              {{ getAssessmentTypeLabel(scope.row.assessmentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentYear" label="评估年度" width="100" />
        <el-table-column prop="overallRiskScore" label="综合风险评分" width="120">
          <template slot-scope="scope">
            <span :class="getRiskScoreClass(scope.row.overallRiskScore)">
              {{ scope.row.overallRiskScore || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="overallRiskLevel" label="风险等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.overallRiskLevel)">
              {{ getRiskLevelLabel(scope.row.overallRiskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isWarningTriggered" label="预警状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isWarningTriggered ? 'danger' : 'success'">
              {{ scope.row.isWarningTriggered ? '已预警' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningLevel" label="预警等级" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.warningLevel" :type="getWarningLevelTagType(scope.row.warningLevel)">
              {{ getWarningLevelLabel(scope.row.warningLevel) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentDate" label="评估日期" width="120">
          <template slot-scope="scope">
            {{ scope.row.assessmentDate || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="assessmentStatus" label="评估状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAssessmentStatusTagType(scope.row.assessmentStatus)">
              {{ getAssessmentStatusLabel(scope.row.assessmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handlePerformAssessment(scope.row)">
              执行评估
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'report', row: scope.row}">
                  生成报告
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'review', row: scope.row}">
                  提交审核
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
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
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 批量操作 -->
    <div v-if="selectedRows.length > 0" class="batch-actions">
      <el-alert
        :title="`已选择 ${selectedRows.length} 项`"
        type="info"
        show-icon
        :closable="false"
      >
        <div slot="default">
          <el-button type="primary" size="small" @click="handleBatchReview">
            批量审核
          </el-button>
          <el-button type="warning" size="small" @click="handleBatchExport">
            批量导出
          </el-button>
          <el-button type="danger" size="small" @click="handleBatchDelete">
            批量删除
          </el-button>
        </div>
      </el-alert>
    </div>

    <!-- 新增/编辑对话框 -->
    <RiskAssessmentDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @success="handleDialogSuccess"
    />

    <!-- 详情对话框 -->
    <RiskAssessmentDetail
      :visible.sync="detailVisible"
      :assessment-id="currentAssessmentId"
    />

    <!-- 执行评估对话框 -->
    <PerformAssessmentDialog
      :visible.sync="performDialogVisible"
      :assessment-data="currentRow"
      @success="handlePerformSuccess"
    />
  </div>
</template>

<script>
import {
  getRiskAssessmentList,
  deleteRiskAssessment,
  performRiskAssessment,
  getComprehensiveStatistics,
  exportAssessmentData,
  batchUpdateStatus,
  batchReview,
  batchDelete
} from '@/api/stateAssets/riskAssessment'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import RiskAssessmentDialog from './components/RiskAssessmentDialog'
import RiskAssessmentDetail from './components/RiskAssessmentDetail'
import PerformAssessmentDialog from './components/PerformAssessmentDialog'

export default {
  name: 'RiskAssessment',
  components: {
    CompanyTreeModal,
    RiskAssessmentDialog,
    RiskAssessmentDetail,
    PerformAssessmentDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      queryForm: {
        enterpriseId: '',
        enterpriseName: '',
        assessmentType: '',
        overallRiskLevel: '',
        assessmentYear: '',
        isWarningTriggered: null
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      statistics: {},
      dialogVisible: false,
      detailVisible: false,
      performDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      currentAssessmentId: ''
    }
  },
  created() {
    this.loadData()
    this.loadStatistics()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.pagination
        }
        const response = await getRiskAssessmentList(params)
        if (response && (response.code === 200 || response.code === 1)) {
          this.tableData = response.data?.tlist || response.data?.list || []
          this.pagination.total = response.data?.totalRecord || response.data?.total || 0
        } else {
          // 接口失败时显示空数据
          this.tableData = []
          this.pagination.total = 0
          console.warn('加载数据失败:', response)
        }
      } catch (error) {
        console.error('加载数据异常:', error)
        // 发生异常时显示空数据，不弹出错误提示
        this.tableData = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getComprehensiveStatistics()
        if (response && (response.code === 200 || response.code === 1)) {
          this.statistics = response.data || {}
        } else {
          this.statistics = {}
          console.warn('加载统计数据失败:', response)
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
        this.statistics = {}
      }
    },

    // 查询
    handleSearch() {
      this.pagination.pageNumber = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        enterpriseId: '',
        enterpriseName: '',
        assessmentType: '',
        overallRiskLevel: '',
        assessmentYear: '',
        isWarningTriggered: null
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogType = 'add'
      this.currentRow = {}
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogType = 'edit'
      this.currentRow = { ...row }
      this.dialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.currentAssessmentId = row.riskAssessmentId
      this.detailVisible = true
    },

    // 执行评估
    handlePerformAssessment(row) {
      this.currentRow = { ...row }
      this.performDialogVisible = true
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      switch (action) {
        case 'report':
          this.handleGenerateReport(row)
          break
        case 'review':
          this.handleSubmitReview(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这条风险评估记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteRiskAssessment(row.riskAssessmentId)
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.warning('删除功能暂未开放')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.warning('删除功能暂未开放')
        }
      }
    },

    // 导出
    async handleExport() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        const response = await exportAssessmentData(this.queryForm)
        if (response && response.data) {
          // 处理文件下载
          const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `风险评估数据_${new Date().getTime()}.xlsx`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.warning('导出功能暂未开放')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.warning('导出功能暂未开放')
      } finally {
        loading.close()
      }
    },

    // 分页
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageNumber = 1
      this.loadData()
    },

    handleCurrentChange(val) {
      this.pagination.pageNumber = val
      this.loadData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 对话框成功回调
    handleDialogSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    // 执行评估成功回调
    handlePerformSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    // 批量操作
    async handleBatchReview() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审核的记录')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskAssessmentId)
        const response = await batchReview({
          riskAssessmentIds: ids,
          reviewer: this.$store.getters.userInfo.userName,
          reviewStatus: 'APPROVED',
          reviewComments: '批量审核通过'
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量审核成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('批量审核失败：' + error.message)
      }
    },

    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条记录吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const ids = this.selectedRows.map(row => row.riskAssessmentId)
        const response = await batchDelete({
          riskAssessmentIds: ids,
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    // 标签类型和标签文本方法
    getAssessmentTypeTagType(type) {
      const typeMap = {
        'COMPREHENSIVE': 'primary',
        'FINANCIAL': 'success',
        'OPERATIONAL': 'warning',
        'COMPLIANCE': 'info',
        'GOVERNANCE': 'danger',
        'EXTERNAL': ''
      }
      return typeMap[type] || ''
    },

    getAssessmentTypeLabel(type) {
      const labelMap = {
        'COMPREHENSIVE': '综合评估',
        'FINANCIAL': '财务风险评估',
        'OPERATIONAL': '经营风险评估',
        'COMPLIANCE': '合规风险评估',
        'GOVERNANCE': '治理风险评估',
        'EXTERNAL': '外部风险评估'
      }
      return labelMap[type] || type
    },

    getRiskLevelTagType(level) {
      const levelMap = {
        'VERY_LOW': 'success',
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'VERY_HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'VERY_LOW': '极低风险',
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险',
        'VERY_HIGH': '极高风险',
        'CRITICAL': '临界风险'
      }
      return labelMap[level] || level
    },

    getWarningLevelTagType(level) {
      const levelMap = {
        'GREEN': 'success',
        'YELLOW': 'warning',
        'ORANGE': 'warning',
        'RED': 'danger'
      }
      return levelMap[level] || ''
    },

    getWarningLevelLabel(level) {
      const labelMap = {
        'GREEN': '绿色预警',
        'YELLOW': '黄色预警',
        'ORANGE': '橙色预警',
        'RED': '红色预警'
      }
      return labelMap[level] || level
    },

    getAssessmentStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'REVIEWED': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || ''
    },

    getAssessmentStatusLabel(status) {
      const labelMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '评估中',
        'COMPLETED': '已完成',
        'REVIEWED': '已审核',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝'
      }
      return labelMap[status] || status
    },

    getRiskScoreClass(score) {
      if (!score) return ''
      if (score >= 80) return 'risk-score-high'
      if (score >= 60) return 'risk-score-medium'
      return 'risk-score-low'
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-assessment-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.statistics-container {
  margin-bottom: 20px;

  .stat-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-left: 4px solid;

    &.stat-card-primary {
      border-left-color: #409eff;
      .stat-icon {
        background: linear-gradient(135deg, #409eff, #66b1ff);
      }
    }

    &.stat-card-warning {
      border-left-color: #e6a23c;
      .stat-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }
    }

    &.stat-card-danger {
      border-left-color: #f56c6c;
      .stat-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }
    }

    &.stat-card-success {
      border-left-color: #67c23a;
      .stat-icon {
        background: linear-gradient(135deg, #67c23a, #95d475);
      }
    }

    .stat-icon {
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
    }

    .stat-content {
      .stat-number {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        line-height: 1;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;

  .el-table {
    .risk-score-high {
      color: #f56c6c;
      font-weight: 600;
    }

    .risk-score-medium {
      color: #e6a23c;
      font-weight: 600;
    }

    .risk-score-low {
      color: #67c23a;
      font-weight: 600;
    }
  }
}

.pagination-container {
  padding: 20px;
  text-align: right;
  border-top: 1px solid #ebeef5;
}

.batch-actions {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  min-width: 400px;

  .el-alert {
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
