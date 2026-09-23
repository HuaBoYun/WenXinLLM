<template>
  <div class="risk-incident-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-warning"></i>
          风险事件管理
        </h2>
        <p class="page-description">国资国企穿透式监管 - 风险事件报告与应急响应处理</p>
      </div>
      <div class="header-right">
        <el-button type="danger" icon="el-icon-plus" @click="handleAdd">
          报告事件
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
          <div class="stat-card total-incidents">
            <div class="stat-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalIncidents || 0 }}</div>
              <div class="stat-label">总事件数</div>
            </div>
            <div class="stat-trend">
              <span class="trend-value">+{{ statistics.newIncidents || 0 }}</span>
              <span class="trend-label">本月新增</span>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card high-risk-incidents">
            <div class="stat-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.highRiskCount || 0 }}</div>
              <div class="stat-label">高风险事件</div>
            </div>
            <div class="stat-alert">
              <el-tag v-if="statistics.highRiskCount > 0" type="danger" size="small">
                需关注
              </el-tag>
              <el-tag v-else type="success" size="small">
                正常
              </el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card processing-incidents">
            <div class="stat-icon">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.processingCount || 0 }}</div>
              <div class="stat-label">处理中</div>
            </div>
            <div class="stat-progress">
              <el-progress
                :percentage="calculateProcessingRate()"
                :show-text="false"
                stroke-width="4"
                color="#e6a23c"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card resolved-incidents">
            <div class="stat-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.resolvedCount || 0 }}</div>
              <div class="stat-label">已解决</div>
            </div>
            <div class="stat-progress">
              <el-progress
                :percentage="calculateResolutionRate()"
                :show-text="false"
                stroke-width="4"
                color="#67c23a"
              />
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
        <el-form-item label="事件类型" prop="incidentType">
          <el-select v-model="queryForm.incidentType" placeholder="请选择事件类型" clearable>
            <el-option label="财务风险" value="FINANCIAL" />
            <el-option label="运营风险" value="OPERATIONAL" />
            <el-option label="合规风险" value="COMPLIANCE" />
            <el-option label="信息安全" value="INFORMATION_SECURITY" />
            <el-option label="市场风险" value="MARKET" />
            <el-option label="信用风险" value="CREDIT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="极高风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态" prop="processingStatus">
          <el-select v-model="queryForm.processingStatus" placeholder="请选择处理状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="IN_PROGRESS" />
            <el-option label="已解决" value="RESOLVED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="发生时间" prop="dateRange">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          />
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
        <el-table-column prop="incidentNumber" label="事件编号" width="150" show-overflow-tooltip />
        <el-table-column prop="enterpriseName" label="企业名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="incidentTitle" label="事件标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="incidentType" label="事件类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getIncidentTypeTagType(scope.row.incidentType)" size="small">
              {{ getIncidentTypeLabel(scope.row.incidentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)" size="small">
              {{ getRiskLevelLabel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="processingStatus" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getProcessingStatusTagType(scope.row.processingStatus)" size="small">
              {{ getProcessingStatusLabel(scope.row.processingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="economicLoss" label="经济损失" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.economicLoss" class="loss-amount">
              ¥{{ formatAmount(scope.row.economicLoss) }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="occurrenceTime" label="发生时间" width="120">
          <template slot-scope="scope">
            {{ scope.row.occurrenceTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="reportTime" label="报告时间" width="120">
          <template slot-scope="scope">
            {{ scope.row.reportTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button
              v-if="scope.row.processingStatus === 'PENDING'"
              type="text"
              size="small"
              @click="handleStartProcessing(scope.row)"
            >
              开始处理
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'emergency', row: scope.row}">
                  应急响应
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'assessment', row: scope.row}">
                  损失评估
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: scope.row}">
                  生成报告
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
          <el-button type="warning" size="small" @click="handleBatchProcess">
            批量处理
          </el-button>
          <el-button type="success" size="small" @click="handleBatchResolve">
            批量解决
          </el-button>
          <el-button type="info" size="small" @click="handleBatchExport">
            批量导出
          </el-button>
          <el-button type="danger" size="small" @click="handleBatchDelete">
            批量删除
          </el-button>
        </div>
      </el-alert>
    </div>

    <!-- 新增/编辑对话框 -->
    <RiskIncidentDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @success="handleDialogSuccess"
    />

    <!-- 详情对话框 -->
    <RiskIncidentDetail
      :visible.sync="detailVisible"
      :incident-id="currentIncidentId"
    />

    <!-- 应急响应对话框 -->
    <EmergencyResponseDialog
      :visible.sync="emergencyDialogVisible"
      :incident-data="currentRow"
      @success="handleEmergencySuccess"
    />
  </div>
</template>

<script>
import {
  getRiskIncidentList,
  deleteRiskIncident,
  startProcessing,
  getComprehensiveStatistics,
  exportIncidentData,
  batchUpdateIncidentStatus
} from '@/api/stateAssets/riskIncident'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import RiskIncidentDialog from './components/RiskIncidentDialog'
import RiskIncidentDetail from './components/RiskIncidentDetail'
import EmergencyResponseDialog from './components/EmergencyResponseDialog'

export default {
  name: 'RiskIncident',
  components: {
    CompanyTreeModal,
    RiskIncidentDialog,
    RiskIncidentDetail,
    EmergencyResponseDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      queryForm: {
        enterpriseId: '',
        enterpriseName: '',
        incidentType: '',
        riskLevel: '',
        processingStatus: '',
        dateRange: []
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      statistics: {},
      dialogVisible: false,
      detailVisible: false,
      emergencyDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      currentIncidentId: ''
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
          startDate: this.queryForm.dateRange?.[0] || '',
          endDate: this.queryForm.dateRange?.[1] || '',
          ...this.pagination
        }
        delete params.dateRange
        
        const response = await getRiskIncidentList(params)
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
        incidentType: '',
        riskLevel: '',
        processingStatus: '',
        dateRange: []
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
      this.currentIncidentId = row.riskIncidentId
      this.detailVisible = true
    },

    // 开始处理
    async handleStartProcessing(row) {
      try {
        await this.$confirm('确定要开始处理这个风险事件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startProcessing({
          riskIncidentId: row.riskIncidentId,
          startBy: this.$store.getters.userInfo.userName,
          processingStartTime: new Date().toISOString().split('T')[0]
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('事件处理已开始')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始处理失败：' + error.message)
        }
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      switch (action) {
        case 'emergency':
          this.handleEmergencyResponse(row)
          break
        case 'assessment':
          this.handleLossAssessment(row)
          break
        case 'report':
          this.handleGenerateReport(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 应急响应
    handleEmergencyResponse(row) {
      this.currentRow = { ...row }
      this.emergencyDialogVisible = true
    },

    // 损失评估
    handleLossAssessment(row) {
      this.$message.info('损失评估功能开发中...')
    },

    // 生成报告
    handleGenerateReport(row) {
      this.$message.info('报告生成功能开发中...')
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个风险事件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteRiskIncident(row.riskIncidentId)
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
        const response = await exportIncidentData(this.queryForm)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险事件数据_${new Date().getTime()}.xlsx`
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

    handleEmergencySuccess() {
      this.loadData()
      this.loadStatistics()
    },

    // 批量操作
    async handleBatchProcess() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要处理的事件')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskIncidentId)
        const response = await batchUpdateIncidentStatus({
          riskIncidentIds: ids,
          processingStatus: 'IN_PROGRESS',
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量处理成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量处理失败：' + error.message)
      }
    },

    async handleBatchResolve() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要解决的事件')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.riskIncidentId)
        const response = await batchUpdateIncidentStatus({
          riskIncidentIds: ids,
          processingStatus: 'RESOLVED',
          updateBy: this.$store.getters.userInfo.userName
        })

        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量解决成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量解决失败：' + error.message)
      }
    },

    async handleBatchExport() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要导出的事件')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.riskIncidentId)
        const response = await exportIncidentData({ riskIncidentIds: ids })
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险事件数据_${new Date().getTime()}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('批量导出成功')
      } catch (error) {
        this.$message.error('批量导出失败：' + error.message)
      }
    },

    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的事件')
        return
      }

      try {
        await this.$confirm('确定要删除选中的风险事件吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedRows.map(row => row.riskIncidentId)
        const deletePromises = ids.map(id => deleteRiskIncident(id))
        await Promise.all(deletePromises)

        this.$message.success('批量删除成功')
        this.loadData()
        this.loadStatistics()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    // 计算统计比率
    calculateProcessingRate() {
      if (!this.statistics.totalIncidents || this.statistics.totalIncidents === 0) return 0
      return Math.round((this.statistics.processingCount / this.statistics.totalIncidents) * 100)
    },

    calculateResolutionRate() {
      if (!this.statistics.totalIncidents || this.statistics.totalIncidents === 0) return 0
      return Math.round((this.statistics.resolvedCount / this.statistics.totalIncidents) * 100)
    },

    // 工具方法
    formatAmount(amount) {
      if (!amount) return '0'
      return amount.toLocaleString()
    },

    getIncidentTypeTagType(type) {
      const typeMap = {
        'FINANCIAL': 'danger',
        'OPERATIONAL': 'warning',
        'COMPLIANCE': 'primary',
        'INFORMATION_SECURITY': 'danger',
        'MARKET': 'warning',
        'CREDIT': 'info',
        'OTHER': 'info'
      }
      return typeMap[type] || ''
    },

    getIncidentTypeLabel(type) {
      const labelMap = {
        'FINANCIAL': '财务风险',
        'OPERATIONAL': '运营风险',
        'COMPLIANCE': '合规风险',
        'INFORMATION_SECURITY': '信息安全',
        'MARKET': '市场风险',
        'CREDIT': '信用风险',
        'OTHER': '其他'
      }
      return labelMap[type] || type
    },

    getRiskLevelTagType(level) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return levelMap[level] || ''
    },

    getRiskLevelLabel(level) {
      const labelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return labelMap[level] || level
    },

    getProcessingStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'RESOLVED': 'success',
        'CLOSED': 'info'
      }
      return statusMap[status] || ''
    },

    getProcessingStatusLabel(status) {
      const labelMap = {
        'PENDING': '待处理',
        'IN_PROGRESS': '处理中',
        'RESOLVED': '已解决',
        'CLOSED': '已关闭'
      }
      return labelMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-incident-container {
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
        color: #f56c6c;
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

    &.total-incidents {
      border-left: 4px solid #409eff;
      .stat-icon {
        background: linear-gradient(135deg, #409eff, #66b1ff);
      }
    }

    &.high-risk-incidents {
      border-left: 4px solid #f56c6c;
      .stat-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }
    }

    &.processing-incidents {
      border-left: 4px solid #e6a23c;
      .stat-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }
    }

    &.resolved-incidents {
      border-left: 4px solid #67c23a;
      .stat-icon {
        background: linear-gradient(135deg, #67c23a, #95d475);
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
        color: #f56c6c;
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

  .loss-amount {
    color: #f56c6c;
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
