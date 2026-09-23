<template>
  <div class="risk-control-measure-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-shield"></i>
          风险控制措施管理
        </h2>
        <p class="page-description">国资国企穿透式监管 - 风险控制措施制定与实施跟踪</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增措施
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="statistics-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card total-measures">
            <div class="stat-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalMeasures || 0 }}</div>
              <div class="stat-label">总措施数</div>
            </div>
            <div class="stat-trend">
              <span class="trend-value">+{{ statistics.newMeasures || 0 }}</span>
              <span class="trend-label">本月新增</span>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card implementing-measures">
            <div class="stat-icon">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.implementingCount || 0 }}</div>
              <div class="stat-label">实施中</div>
            </div>
            <div class="stat-progress">
              <el-progress
                :percentage="calculateImplementingRate()"
                :show-text="false"
                stroke-width="4"
                color="#409eff"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card completed-measures">
            <div class="stat-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
            <div class="stat-progress">
              <el-progress
                :percentage="calculateCompletionRate()"
                :show-text="false"
                stroke-width="4"
                color="#67c23a"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card overdue-measures">
            <div class="stat-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.overdueCount || 0 }}</div>
              <div class="stat-label">逾期措施</div>
            </div>
            <div class="stat-alert">
              <el-tag v-if="statistics.overdueCount > 0" type="danger" size="small">
                需关注
              </el-tag>
              <el-tag v-else type="success" size="small">
                正常
              </el-tag>
            </div>
          </div>
        </el-col>
      </el-row>
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
        <el-form-item label="措施类型" prop="measureType">
          <el-select v-model="queryForm.measureType" placeholder="请选择措施类型" clearable>
            <el-option label="预防措施" value="PREVENTIVE" />
            <el-option label="纠正措施" value="CORRECTIVE" />
            <el-option label="改进措施" value="IMPROVEMENT" />
            <el-option label="应急措施" value="EMERGENCY" />
            <el-option label="监控措施" value="MONITORING" />
          </el-select>
        </el-form-item>
        <el-form-item label="实施状态" prop="implementationStatus">
          <el-select v-model="queryForm.implementationStatus" placeholder="请选择实施状态" clearable>
            <el-option label="待实施" value="PENDING" />
            <el-option label="实施中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="SUSPENDED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="queryForm.priority" placeholder="请选择优先级" clearable>
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-input v-model="queryForm.responsiblePerson" placeholder="请输入负责人" clearable />
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
        <el-table-column prop="measureName" label="措施名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="measureType" label="措施类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getMeasureTypeTagType(scope.row.measureType)" size="small">
              {{ getMeasureTypeLabel(scope.row.measureType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template slot-scope="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" size="small">
              {{ getPriorityLabel(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100" />
        <el-table-column prop="implementationStatus" label="实施状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getImplementationStatusTagType(scope.row.implementationStatus)" size="small">
              {{ getImplementationStatusLabel(scope.row.implementationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="implementationProgress" label="实施进度" width="120">
          <template slot-scope="scope">
            <div class="progress-container">
              <el-progress
                :percentage="scope.row.implementationProgress || 0"
                :color="getProgressColor(scope.row.implementationProgress)"
                :show-text="false"
                stroke-width="6"
              />
              <span class="progress-text">{{ scope.row.implementationProgress || 0 }}%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="plannedStartDate" label="计划开始" width="120">
          <template slot-scope="scope">
            {{ scope.row.plannedStartDate || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="plannedEndDate" label="计划完成" width="120">
          <template slot-scope="scope">
            <span :class="getDateClass(scope.row.plannedEndDate, scope.row.implementationStatus)">
              {{ scope.row.plannedEndDate || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="effectiveness" label="效果评估" width="100">
          <template slot-scope="scope">
            <el-rate
              v-if="scope.row.implementationStatus === 'COMPLETED'"
              v-model="scope.row.effectiveness"
              :max="5"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
            <span v-else>-</span>
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
            <el-button
              v-if="scope.row.implementationStatus === 'PENDING'"
              type="text"
              size="small"
              @click="handleStartImplementation(scope.row)"
            >
              开始实施
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'progress', row: scope.row}">
                  更新进度
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'evaluate', row: scope.row}">
                  效果评估
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'resources', row: scope.row}">
                  资源管理
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
          <el-button type="primary" size="small" @click="handleBatchStart">
            批量开始实施
          </el-button>
          <el-button type="success" size="small" @click="handleBatchComplete">
            批量完成
          </el-button>
          <el-button type="warning" size="small" @click="handleBatchUpdateProgress">
            批量更新进度
          </el-button>
          <el-button type="danger" size="small" @click="handleBatchDelete">
            批量删除
          </el-button>
        </div>
      </el-alert>
    </div>

    <!-- 新增/编辑对话框 -->
    <RiskControlMeasureDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @success="handleDialogSuccess"
    />

    <!-- 详情对话框 -->
    <RiskControlMeasureDetail
      :visible.sync="detailVisible"
      :measure-id="currentMeasureId"
    />

    <!-- 进度更新对话框 -->
    <ProgressUpdateDialog
      :visible.sync="progressDialogVisible"
      :measure-data="currentRow"
      @success="handleProgressSuccess"
    />

    <!-- 效果评估对话框 -->
    <EffectivenessEvaluationDialog
      :visible.sync="evaluationDialogVisible"
      :measure-data="currentRow"
      @success="handleEvaluationSuccess"
    />

    <!-- 资源管理对话框 -->
    <ResourceManagementDialog
      :visible.sync="resourceDialogVisible"
      :measure-data="currentRow"
      @success="handleResourceSuccess"
    />
  </div>
</template>

<script>
import {
  getRiskControlMeasureList,
  deleteRiskControlMeasure,
  startImplementation,
  getComprehensiveStatistics,
  exportMeasureData,
  batchUpdateMeasureStatus
} from '@/api/stateAssets/riskControlMeasure'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import RiskControlMeasureDialog from './components/RiskControlMeasureDialog'
import RiskControlMeasureDetail from './components/RiskControlMeasureDetail'
import ProgressUpdateDialog from './components/ProgressUpdateDialog'
import EffectivenessEvaluationDialog from './components/EffectivenessEvaluationDialog'
import ResourceManagementDialog from './components/ResourceManagementDialog'

export default {
  name: 'RiskControlMeasure',
  components: {
    CompanyTreeModal,
    RiskControlMeasureDialog,
    RiskControlMeasureDetail,
    ProgressUpdateDialog,
    EffectivenessEvaluationDialog,
    ResourceManagementDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      queryForm: {
        enterpriseId: '',
        enterpriseName: '',
        measureType: '',
        implementationStatus: '',
        priority: '',
        responsiblePerson: ''
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      statistics: {},
      dialogVisible: false,
      detailVisible: false,
      progressDialogVisible: false,
      evaluationDialogVisible: false,
      resourceDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      currentMeasureId: ''
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
        const response = await getRiskControlMeasureList(params)
        if (response && (response.code === 200 || response.code === 1)) {
          this.tableData = response.data?.tlist || []
          this.pagination.total = response.data?.totalRecord || 0
        }
      } catch (error) {
        console.error('加载数据异常：' + error.message)
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
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
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
        measureType: '',
        implementationStatus: '',
        priority: '',
        responsiblePerson: ''
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
      this.currentMeasureId = row.riskControlMeasureId
      this.detailVisible = true
    },

    // 开始实施
    async handleStartImplementation(row) {
      try {
        await this.$confirm('确定要开始实施这个控制措施吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startImplementation({
          riskControlMeasureId: row.riskControlMeasureId,
          startBy: this.$store.getters.userInfo.userName,
          actualStartDate: new Date().toISOString().split('T')[0]
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('措施实施已开始')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始实施失败：' + error.message)
        }
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      switch (action) {
        case 'progress':
          this.handleUpdateProgress(row)
          break
        case 'evaluate':
          this.handleEvaluateEffectiveness(row)
          break
        case 'resources':
          this.handleManageResources(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 更新进度
    handleUpdateProgress(row) {
      this.currentRow = { ...row }
      this.progressDialogVisible = true
    },

    // 效果评估
    handleEvaluateEffectiveness(row) {
      this.currentRow = { ...row }
      this.evaluationDialogVisible = true
    },

    // 资源管理
    handleManageResources(row) {
      this.currentRow = { ...row }
      this.resourceDialogVisible = true
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个控制措施吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteRiskControlMeasure(row.riskControlMeasureId)
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.warning('删除功能暂未开放：' + error.message)
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportMeasureData(this.queryForm)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险控制措施数据_${new Date().getTime()}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.warning('导出功能暂未开放：' + error.message)
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

    handleProgressSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    handleEvaluationSuccess() {
      this.loadData()
    },

    handleResourceSuccess() {
      this.loadData()
    },

    // 批量操作
    async handleBatchStart() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要开始实施的措施')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskControlMeasureId)
        const response = await batchUpdateMeasureStatus({
          riskControlMeasureIds: ids,
          implementationStatus: 'IN_PROGRESS',
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量开始实施成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量开始实施失败：' + error.message)
      }
    },

    async handleBatchComplete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要完成的措施')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskControlMeasureId)
        const response = await batchUpdateMeasureStatus({
          riskControlMeasureIds: ids,
          implementationStatus: 'COMPLETED',
          implementationProgress: 100,
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量完成成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量完成失败：' + error.message)
      }
    },

    // 计算统计比率
    calculateImplementingRate() {
      if (!this.statistics.totalMeasures || this.statistics.totalMeasures === 0) return 0
      return Math.round((this.statistics.implementingCount / this.statistics.totalMeasures) * 100)
    },

    calculateCompletionRate() {
      if (!this.statistics.totalMeasures || this.statistics.totalMeasures === 0) return 0
      return Math.round((this.statistics.completedCount / this.statistics.totalMeasures) * 100)
    },

    // 工具方法
    getMeasureTypeTagType(type) {
      const typeMap = {
        'PREVENTIVE': 'primary',
        'CORRECTIVE': 'warning',
        'IMPROVEMENT': 'success',
        'EMERGENCY': 'danger',
        'MONITORING': 'info'
      }
      return typeMap[type] || ''
    },

    getMeasureTypeLabel(type) {
      const labelMap = {
        'PREVENTIVE': '预防措施',
        'CORRECTIVE': '纠正措施',
        'IMPROVEMENT': '改进措施',
        'EMERGENCY': '应急措施',
        'MONITORING': '监控措施'
      }
      return labelMap[type] || type
    },

    getPriorityTagType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return priorityMap[priority] || ''
    },

    getPriorityLabel(priority) {
      const labelMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return labelMap[priority] || priority
    },

    getImplementationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getImplementationStatusLabel(status) {
      const labelMap = {
        'PENDING': '待实施',
        'IN_PROGRESS': '实施中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停',
        'CANCELLED': '已取消'
      }
      return labelMap[status] || status
    },

    getProgressColor(progress) {
      if (progress >= 80) return '#67c23a'
      if (progress >= 50) return '#e6a23c'
      return '#f56c6c'
    },

    getDateClass(date, status) {
      if (!date || status === 'COMPLETED') return ''
      
      const today = new Date()
      const endDate = new Date(date)
      const diffDays = Math.ceil((endDate - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'date-overdue'
      if (diffDays <= 7) return 'date-warning'
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-control-measure-container {
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

.statistics-overview {
  margin-bottom: 20px;

  .stat-card {
    display: flex;
    flex-direction: column;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: relative;
    overflow: hidden;

    &.total-measures {
      border-left: 4px solid #409eff;
      .stat-icon {
        background: linear-gradient(135deg, #409eff, #66b1ff);
      }
    }

    &.implementing-measures {
      border-left: 4px solid #e6a23c;
      .stat-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }
    }

    &.completed-measures {
      border-left: 4px solid #67c23a;
      .stat-icon {
        background: linear-gradient(135deg, #67c23a, #95d475);
      }
    }

    &.overdue-measures {
      border-left: 4px solid #f56c6c;
      .stat-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }
    }

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;
      align-self: flex-start;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .stat-content {
      .stat-number {
        font-size: 32px;
        font-weight: 600;
        color: #303133;
        line-height: 1;
        margin-bottom: 8px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 12px;
      }
    }

    .stat-trend {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;

      .trend-value {
        color: #67c23a;
        font-weight: 600;
      }

      .trend-label {
        color: #909399;
      }
    }

    .stat-progress {
      margin-top: 8px;
    }

    .stat-alert {
      margin-top: 8px;
      text-align: center;
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

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;

  .progress-container {
    display: flex;
    align-items: center;

    .el-progress {
      flex: 1;
      margin-right: 8px;
    }

    .progress-text {
      font-size: 12px;
      color: #606266;
      min-width: 35px;
    }
  }

  .date-overdue {
    color: #f56c6c;
    font-weight: 600;
  }

  .date-warning {
    color: #e6a23c;
    font-weight: 600;
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
