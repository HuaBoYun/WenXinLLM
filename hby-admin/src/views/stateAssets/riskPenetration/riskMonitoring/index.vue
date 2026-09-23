<template>
  <div class="risk-monitoring-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-monitor"></i>
          风险监控管理
        </h2>
        <p class="page-description">国资国企穿透式监管 - 实时风险监控与预警</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增监控
        </el-button>
        <el-button type="success" icon="el-icon-video-play" @click="handleStartBatchMonitoring">
          批量启动
        </el-button>
        <el-button type="warning" icon="el-icon-video-pause" @click="handleStopBatchMonitoring">
          批量停止
        </el-button>
      </div>
    </div>

    <!-- 实时监控概览 -->
    <div class="monitoring-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card active-monitoring">
            <div class="card-icon">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statistics.activeCount || 0 }}</div>
              <div class="card-label">活跃监控</div>
            </div>
            <div class="card-status">
              <el-tag type="success" size="small">运行中</el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card warning-alerts">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statistics.warningCount || 0 }}</div>
              <div class="card-label">预警数量</div>
            </div>
            <div class="card-status">
              <el-tag type="warning" size="small">需关注</el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card threshold-exceeded">
            <div class="card-icon">
              <i class="el-icon-error"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statistics.exceededCount || 0 }}</div>
              <div class="card-label">超阈值数</div>
            </div>
            <div class="card-status">
              <el-tag type="danger" size="small">紧急</el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card anomaly-detected">
            <div class="card-icon">
              <i class="el-icon-question"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ statistics.anomalyCount || 0 }}</div>
              <div class="card-label">异常检测</div>
            </div>
            <div class="card-status">
              <el-tag type="info" size="small">待处理</el-tag>
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
        <el-form-item label="监控类型" prop="monitoringType">
          <el-select v-model="queryForm.monitoringType" placeholder="请选择监控类型" clearable>
            <el-option label="实时监控" value="REAL_TIME" />
            <el-option label="定期监控" value="PERIODIC" />
            <el-option label="事件驱动" value="EVENT_DRIVEN" />
            <el-option label="阈值监控" value="THRESHOLD" />
            <el-option label="趋势监控" value="TREND" />
          </el-select>
        </el-form-item>
        <el-form-item label="监控状态" prop="monitoringStatus">
          <el-select v-model="queryForm.monitoringStatus" placeholder="请选择监控状态" clearable>
            <el-option label="运行中" value="RUNNING" />
            <el-option label="已停止" value="STOPPED" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="异常" value="ERROR" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态" prop="alertStatus">
          <el-select v-model="queryForm.alertStatus" placeholder="请选择预警状态" clearable>
            <el-option label="正常" value="NORMAL" />
            <el-option label="预警" value="WARNING" />
            <el-option label="告警" value="ALERT" />
            <el-option label="严重" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
          <el-button type="info" icon="el-icon-refresh" @click="handleRefreshData">
            刷新数据
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
        <el-table-column prop="monitoringName" label="监控名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="monitoringType" label="监控类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getMonitoringTypeTagType(scope.row.monitoringType)" size="small">
              {{ getMonitoringTypeLabel(scope.row.monitoringType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="monitoringIndicator" label="监控指标" min-width="120" show-overflow-tooltip />
        <el-table-column prop="currentValue" label="当前值" width="100">
          <template slot-scope="scope">
            <span :class="getValueClass(scope.row)">
              {{ scope.row.currentValue || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="warningThreshold" label="预警阈值" width="100" />
        <el-table-column prop="dangerThreshold" label="危险阈值" width="100" />
        <el-table-column prop="monitoringStatus" label="监控状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getMonitoringStatusTagType(scope.row.monitoringStatus)" size="small">
              {{ getMonitoringStatusLabel(scope.row.monitoringStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertStatus" label="预警状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAlertStatusTagType(scope.row.alertStatus)" size="small">
              {{ getAlertStatusLabel(scope.row.alertStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="最后更新" width="150">
          <template slot-scope="scope">
            {{ scope.row.lastUpdateTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.monitoringStatus === 'STOPPED'"
              type="text"
              size="small"
              @click="handleStartMonitoring(scope.row)"
            >
              启动
            </el-button>
            <el-button
              v-if="scope.row.monitoringStatus === 'RUNNING'"
              type="text"
              size="small"
              @click="handleStopMonitoring(scope.row)"
            >
              停止
            </el-button>
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-dropdown @command="handleMoreAction">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'config', row: scope.row}">
                  配置阈值
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'trend', row: scope.row}">
                  趋势分析
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'alert', row: scope.row}">
                  预警记录
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
          <el-button type="success" size="small" @click="handleBatchStart">
            批量启动
          </el-button>
          <el-button type="warning" size="small" @click="handleBatchStop">
            批量停止
          </el-button>
          <el-button type="info" size="small" @click="handleBatchConfig">
            批量配置
          </el-button>
          <el-button type="danger" size="small" @click="handleBatchDelete">
            批量删除
          </el-button>
        </div>
      </el-alert>
    </div>

    <!-- 新增/编辑对话框 -->
    <RiskMonitoringDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @success="handleDialogSuccess"
    />

    <!-- 详情对话框 -->
    <RiskMonitoringDetail
      :visible.sync="detailVisible"
      :monitoring-id="currentMonitoringId"
    />

    <!-- 阈值配置对话框 -->
    <ThresholdConfigDialog
      :visible.sync="thresholdDialogVisible"
      :monitoring-data="currentRow"
      @success="handleThresholdSuccess"
    />

    <!-- 趋势分析对话框 -->
    <TrendAnalysisDialog
      :visible.sync="trendDialogVisible"
      :monitoring-data="currentRow"
    />
  </div>
</template>

<script>
import {
  getRiskMonitoringList,
  deleteRiskMonitoring,
  startRealTimeMonitoring,
  stopRealTimeMonitoring,
  getMonitoringStatistics,
  batchUpdateMonitoringStatus,
  updateMonitoringData
} from '@/api/stateAssets/riskMonitoring'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import RiskMonitoringDialog from './components/RiskMonitoringDialog'
import RiskMonitoringDetail from './components/RiskMonitoringDetail'
import ThresholdConfigDialog from './components/ThresholdConfigDialog'
import TrendAnalysisDialog from './components/TrendAnalysisDialog'

export default {
  name: 'RiskMonitoring',
  components: {
    CompanyTreeModal,
    RiskMonitoringDialog,
    RiskMonitoringDetail,
    ThresholdConfigDialog,
    TrendAnalysisDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      queryForm: {
        enterpriseId: '',
        enterpriseName: '',
        monitoringType: '',
        monitoringStatus: '',
        alertStatus: ''
      },
      pagination: {
        pageNumber: 1,
        pageSize: 20,
        total: 0
      },
      statistics: {},
      dialogVisible: false,
      detailVisible: false,
      thresholdDialogVisible: false,
      trendDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      currentMonitoringId: '',
      refreshTimer: null
    }
  },
  created() {
    this.loadData()
    this.loadStatistics()
    this.startAutoRefresh()
  },
  beforeDestroy() {
    this.stopAutoRefresh()
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
        const response = await getRiskMonitoringList(params)
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
        const response = await getMonitoringStatistics()
        if (response && (response.code === 200 || response.code === 1)) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    // 启动自动刷新
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        this.loadData()
        this.loadStatistics()
      }, 30000) // 30秒刷新一次
    },

    // 停止自动刷新
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },

    // 手动刷新数据
    handleRefreshData() {
      this.loadData()
      this.loadStatistics()
      this.$message.success('数据已刷新')
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
        monitoringType: '',
        monitoringStatus: '',
        alertStatus: ''
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
      this.currentMonitoringId = row.riskMonitoringId
      this.detailVisible = true
    },

    // 启动监控
    async handleStartMonitoring(row) {
      try {
        const response = await startRealTimeMonitoring({
          riskMonitoringId: row.riskMonitoringId,
          startBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('监控启动成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('启动监控失败：' + error.message)
      }
    },

    // 停止监控
    async handleStopMonitoring(row) {
      try {
        const response = await stopRealTimeMonitoring(row.riskMonitoringId)
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('监控已停止')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('停止监控失败：' + error.message)
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command
      switch (action) {
        case 'config':
          this.handleConfigThreshold(row)
          break
        case 'trend':
          this.handleTrendAnalysis(row)
          break
        case 'alert':
          this.handleViewAlerts(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 配置阈值
    handleConfigThreshold(row) {
      this.currentRow = { ...row }
      this.thresholdDialogVisible = true
    },

    // 趋势分析
    handleTrendAnalysis(row) {
      this.currentRow = { ...row }
      this.trendDialogVisible = true
    },

    // 查看预警记录
    handleViewAlerts(row) {
      this.$router.push({
        path: '/stateAssets/riskPenetration/alerts',
        query: { monitoringId: row.riskMonitoringId }
      })
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个监控项吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteRiskMonitoring(row.riskMonitoringId)
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

    // 阈值配置成功回调
    handleThresholdSuccess() {
      this.loadData()
    },

    // 批量操作
    async handleBatchStart() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要启动的监控项')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskMonitoringId)
        const response = await batchUpdateMonitoringStatus({
          riskMonitoringIds: ids,
          monitoringStatus: 'RUNNING',
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量启动成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量启动失败：' + error.message)
      }
    },

    async handleBatchStop() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要停止的监控项')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.riskMonitoringId)
        const response = await batchUpdateMonitoringStatus({
          riskMonitoringIds: ids,
          monitoringStatus: 'STOPPED',
          updateBy: this.$store.getters.userInfo.userName
        })
        
        if (response && (response.code === 200 || response.code === 1)) {
          this.$message.success('批量停止成功')
          this.loadData()
          this.loadStatistics()
        }
      } catch (error) {
        this.$message.error('批量停止失败：' + error.message)
      }
    },

    // 标签类型和标签文本方法
    getMonitoringTypeTagType(type) {
      const typeMap = {
        'REAL_TIME': 'primary',
        'PERIODIC': 'success',
        'EVENT_DRIVEN': 'warning',
        'THRESHOLD': 'info',
        'TREND': 'danger'
      }
      return typeMap[type] || ''
    },

    getMonitoringTypeLabel(type) {
      const labelMap = {
        'REAL_TIME': '实时监控',
        'PERIODIC': '定期监控',
        'EVENT_DRIVEN': '事件驱动',
        'THRESHOLD': '阈值监控',
        'TREND': '趋势监控'
      }
      return labelMap[type] || type
    },

    getMonitoringStatusTagType(status) {
      const statusMap = {
        'RUNNING': 'success',
        'STOPPED': 'info',
        'PAUSED': 'warning',
        'ERROR': 'danger'
      }
      return statusMap[status] || ''
    },

    getMonitoringStatusLabel(status) {
      const labelMap = {
        'RUNNING': '运行中',
        'STOPPED': '已停止',
        'PAUSED': '已暂停',
        'ERROR': '异常'
      }
      return labelMap[status] || status
    },

    getAlertStatusTagType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'ALERT': 'warning',
        'CRITICAL': 'danger'
      }
      return statusMap[status] || ''
    },

    getAlertStatusLabel(status) {
      const labelMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'ALERT': '告警',
        'CRITICAL': '严重'
      }
      return labelMap[status] || status
    },

    getValueClass(row) {
      if (!row.currentValue || !row.dangerThreshold) return ''
      
      if (row.currentValue >= row.dangerThreshold) return 'value-danger'
      if (row.warningThreshold && row.currentValue >= row.warningThreshold) return 'value-warning'
      return 'value-normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-monitoring-container {
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

.monitoring-overview {
  margin-bottom: 20px;

  .overview-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: relative;
    overflow: hidden;

    &.active-monitoring {
      border-left: 4px solid #67c23a;
      .card-icon {
        background: linear-gradient(135deg, #67c23a, #95d475);
      }
    }

    &.warning-alerts {
      border-left: 4px solid #e6a23c;
      .card-icon {
        background: linear-gradient(135deg, #e6a23c, #f0c78a);
      }
    }

    &.threshold-exceeded {
      border-left: 4px solid #f56c6c;
      .card-icon {
        background: linear-gradient(135deg, #f56c6c, #f89898);
      }
    }

    &.anomaly-detected {
      border-left: 4px solid #909399;
      .card-icon {
        background: linear-gradient(135deg, #909399, #b4bccc);
      }
    }

    .card-icon {
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

    .card-content {
      flex: 1;

      .card-number {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        line-height: 1;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
      }
    }

    .card-status {
      position: absolute;
      top: 12px;
      right: 12px;
    }
  }
}

.invest-penetration-section {
  margin-bottom: 20px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
    color: #303133;

    > span {
      display: flex;
      align-items: center;
      gap: 6px;
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

  .el-table {
    .value-danger {
      color: #f56c6c;
      font-weight: 600;
    }

    .value-warning {
      color: #e6a23c;
      font-weight: 600;
    }

    .value-normal {
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
