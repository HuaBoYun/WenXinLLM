<template>
  <div class="tax-compliance-list">
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="检查名称">
          <el-input
            v-model="searchForm.complianceName"
            placeholder="请输入检查名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="检查类型">
          <el-select v-model="searchForm.complianceType" placeholder="请选择检查类型" clearable style="width: 150px">
            <el-option label="税务合规" value="TAX_COMPLIANCE" />
            <el-option label="财务合规" value="FINANCIAL_COMPLIANCE" />
            <el-option label="监管合规" value="REGULATORY_COMPLIANCE" />
            <el-option label="内部合规" value="INTERNAL_COMPLIANCE" />
            <el-option label="外部合规" value="EXTERNAL_COMPLIANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查状态">
          <el-select v-model="searchForm.checkStatus" placeholder="请选择检查状态" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
            <el-option label="失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="searchForm.complianceStatus" placeholder="请选择合规状态" clearable style="width: 120px">
            <el-option label="待检查" value="PENDING" />
            <el-option label="合规" value="COMPLIANT" />
            <el-option label="部分合规" value="PARTIALLY_COMPLIANT" />
            <el-option label="不合规" value="NON_COMPLIANT" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable style="width: 120px">
            <el-option label="低风险" value="LOW" />
            <el-option label="中等风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="严重风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority" placeholder="请选择优先级" clearable style="width: 100px">
            <el-option label="低" value="LOW" />
            <el-option label="普通" value="NORMAL" />
            <el-option label="高" value="HIGH" />
            <el-option label="紧急" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查人员">
          <el-input
            v-model="searchForm.checker"
            placeholder="请输入检查人员"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.createTimeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建检查</el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-download"
          :disabled="selectedRows.length === 0"
          @click="handleBatchExport"
        >
          批量导出
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button-group>
          <el-button
            :type="viewMode === 'table' ? 'primary' : 'default'"
            icon="el-icon-s-grid"
            @click="viewMode = 'table'"
          />
          <el-button
            :type="viewMode === 'card' ? 'primary' : 'default'"
            icon="el-icon-menu"
            @click="viewMode = 'card'"
          />
        </el-button-group>
        <el-button icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>
    </div>

    <!-- 表格视图 -->
    <div v-if="viewMode === 'table'" class="table-container">
      <el-table
        ref="complianceTable"
        v-loading="loading"
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="complianceCode" label="检查编号" width="160" sortable="custom">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewDetail(scope.row)">
              {{ scope.row.complianceCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="complianceName" label="检查名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="complianceType" label="检查类型" width="120">
          <template slot-scope="scope">
            {{ formatComplianceType(scope.row.complianceType) }}
          </template>
        </el-table-column>
        <el-table-column prop="checkStatus" label="检查状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCheckStatusColor(scope.row.checkStatus)" size="mini">
              {{ formatCheckStatus(scope.row.checkStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complianceStatus" label="合规状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getComplianceStatusColor(scope.row.complianceStatus)" size="mini">
              {{ formatComplianceStatus(scope.row.complianceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="mini">
              {{ formatRiskLevel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80">
          <template slot-scope="scope">
            {{ formatPriority(scope.row.priority) }}
          </template>
        </el-table-column>
        <el-table-column prop="complianceScore" label="合规评分" width="100" sortable="custom">
          <template slot-scope="scope">
            <span v-if="scope.row.complianceScore">
              {{ scope.row.complianceScore }}分
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="checkProgress" label="检查进度" width="120">
          <template slot-scope="scope">
            <el-progress
              :percentage="Math.round(scope.row.checkProgress || 0)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ Math.round(scope.row.checkProgress || 0) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="checker" label="检查人员" width="100" show-overflow-tooltip />
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleViewDetail(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="mini" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-if="scope.row.checkStatus === 'DRAFT'"
                  command="start"
                  icon="el-icon-video-play"
                >
                  启动检查
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.checkStatus === 'IN_PROGRESS'"
                  command="pause"
                  icon="el-icon-video-pause"
                >
                  暂停检查
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.checkStatus === 'PAUSED'"
                  command="resume"
                  icon="el-icon-video-play"
                >
                  恢复检查
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="['IN_PROGRESS', 'PAUSED'].includes(scope.row.checkStatus)"
                  command="complete"
                  icon="el-icon-circle-check"
                >
                  完成检查
                </el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">
                  复制检查
                </el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">
                  导出数据
                </el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided>
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 卡片视图 -->
    <div v-if="viewMode === 'card'" class="card-container">
      <el-row :gutter="20">
        <el-col
          v-for="item in tableData"
          :key="item.complianceId"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          :xl="6"
        >
          <div class="compliance-card" @click="handleViewDetail(item)">
            <div class="card-header">
              <div class="card-title">{{ item.complianceName }}</div>
              <div class="card-code">{{ item.complianceCode }}</div>
            </div>
            <div class="card-content">
              <div class="card-row">
                <span class="label">检查类型：</span>
                <span class="value">{{ formatComplianceType(item.complianceType) }}</span>
              </div>
              <div class="card-row">
                <span class="label">检查状态：</span>
                <el-tag :type="getCheckStatusColor(item.checkStatus)" size="mini">
                  {{ formatCheckStatus(item.checkStatus) }}
                </el-tag>
              </div>
              <div class="card-row">
                <span class="label">合规状态：</span>
                <el-tag :type="getComplianceStatusColor(item.complianceStatus)" size="mini">
                  {{ formatComplianceStatus(item.complianceStatus) }}
                </el-tag>
              </div>
              <div class="card-row">
                <span class="label">风险等级：</span>
                <el-tag :type="getRiskLevelColor(item.riskLevel)" size="mini">
                  {{ formatRiskLevel(item.riskLevel) }}
                </el-tag>
              </div>
              <div class="card-row">
                <span class="label">检查进度：</span>
                <el-progress
                  :percentage="Math.round(item.checkProgress || 0)"
                  :stroke-width="4"
                  :show-text="false"
                  style="width: 80px; display: inline-block;"
                />
                <span class="progress-text">{{ Math.round(item.checkProgress || 0) }}%</span>
              </div>
              <div class="card-row">
                <span class="label">检查人员：</span>
                <span class="value">{{ item.checker || '-' }}</span>
              </div>
            </div>
            <div class="card-footer">
              <span class="create-time">{{ formatDate(item.createdTime) }}</span>
              <div class="card-actions" @click.stop>
                <el-button type="text" size="mini" @click="handleEdit(item)">
                  编辑
                </el-button>
                <el-button type="text" size="mini" @click="handleDelete(item)">
                  删除
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import {
  getCompliancePage,
  deleteCompliance,
  startComplianceCheck,
  pauseComplianceCheck,
  resumeComplianceCheck,
  completeComplianceCheck,
  copyCompliance,
  batchDeleteCompliances,
  batchExportCompliances,
  formatCheckStatus,
  formatComplianceStatus,
  formatRiskLevel,
  formatPriority,
  formatComplianceType,
  getCheckStatusColor,
  getComplianceStatusColor,
  getRiskLevelColor
} from '@/api/managementAccountant/ts/taxCompliance'

export default {
  name: 'TaxComplianceList',
  data() {
    return {
      loading: false,
      viewMode: 'table', // table | card
      tableData: [],
      selectedRows: [],
      searchForm: {
        complianceName: '',
        complianceType: '',
        checkStatus: '',
        complianceStatus: '',
        riskLevel: '',
        priority: '',
        checker: '',
        createTimeRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: ''
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }

        // 处理时间范围
        if (this.searchForm.createTimeRange && this.searchForm.createTimeRange.length === 2) {
          params.startTime = this.searchForm.createTimeRange[0]
          params.endTime = this.searchForm.createTimeRange[1]
        }

        // 处理排序
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }

        const response = await getCompliancePage(params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    handleReset() {
      this.searchForm = {
        complianceName: '',
        complianceType: '',
        checkStatus: '',
        complianceStatus: '',
        riskLevel: '',
        priority: '',
        checker: '',
        createTimeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    handleCreate() {
      this.$emit('create')
    },

    handleEdit(row) {
      this.$emit('edit', row)
    },

    handleViewDetail(row) {
      this.$emit('view-detail', row)
    },

    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个合规检查吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteCompliance(row.complianceId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    async handleBatchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 个合规检查吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedRows.map(row => row.complianceId)
        const response = await batchDeleteCompliances(ids)
        
        if (response.success) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    async handleBatchExport() {
      try {
        const ids = this.selectedRows.map(row => row.complianceId)
        const response = await batchExportCompliances(ids)
        
        if (response.success) {
          // 这里可以处理导出逻辑，比如下载文件
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    async handleCommand(command, row) {
      try {
        switch (command) {
          case 'start':
            await this.handleStart(row)
            break
          case 'pause':
            await this.handlePause(row)
            break
          case 'resume':
            await this.handleResume(row)
            break
          case 'complete':
            await this.handleComplete(row)
            break
          case 'copy':
            await this.handleCopy(row)
            break
          case 'export':
            await this.handleExport(row)
            break
          case 'delete':
            await this.handleDelete(row)
            break
        }
      } catch (error) {
        console.error('操作失败:', error)
      }
    },

    async handleStart(row) {
      const response = await startComplianceCheck(row.complianceId)
      if (response.success) {
        this.$message.success('启动检查成功')
        this.loadData()
      } else {
        this.$message.error(response.message || '启动检查失败')
      }
    },

    async handlePause(row) {
      const response = await pauseComplianceCheck(row.complianceId)
      if (response.success) {
        this.$message.success('暂停检查成功')
        this.loadData()
      } else {
        this.$message.error(response.message || '暂停检查失败')
      }
    },

    async handleResume(row) {
      const response = await resumeComplianceCheck(row.complianceId)
      if (response.success) {
        this.$message.success('恢复检查成功')
        this.loadData()
      } else {
        this.$message.error(response.message || '恢复检查失败')
      }
    },

    async handleComplete(row) {
      const response = await completeComplianceCheck(row.complianceId, {})
      if (response.success) {
        this.$message.success('完成检查成功')
        this.loadData()
      } else {
        this.$message.error(response.message || '完成检查失败')
      }
    },

    async handleCopy(row) {
      try {
        const newName = await this.$prompt('请输入新检查名称', '复制检查', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.complianceName + '_副本'
        })

        const response = await copyCompliance(row.complianceId, newName.value)
        if (response.success) {
          this.$message.success('复制检查成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制检查失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制检查失败:', error)
          this.$message.error('复制检查失败')
        }
      }
    },

    async handleExport(row) {
      const response = await batchExportCompliances([row.complianceId])
      if (response.success) {
        this.$message.success('导出成功')
      } else {
        this.$message.error(response.message || '导出失败')
      }
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    refreshData() {
      this.loadData()
    },

    // 过滤方法
    filterByExpiringSoon() {
      // 这里可以设置特定的搜索条件
      this.handleReset()
      // 可以添加特定的过滤逻辑
    },

    filterByRiskLevel(riskLevel) {
      this.searchForm.riskLevel = riskLevel
      this.handleSearch()
    },

    filterByNeedRectification() {
      // 设置需要整改的过滤条件
      this.searchForm.complianceStatus = 'NON_COMPLIANT'
      this.handleSearch()
    },

    filterByOverdue() {
      // 设置逾期的过滤条件
      this.handleReset()
      // 可以添加特定的过滤逻辑
    },

    formatDate(date) {
      if (!date) return '-'
      return this.$moment(date).format('YYYY-MM-DD HH:mm')
    },

    // 导入格式化函数
    formatCheckStatus,
    formatComplianceStatus,
    formatRiskLevel,
    formatPriority,
    formatComplianceType,
    getCheckStatusColor,
    getComplianceStatusColor,
    getRiskLevelColor
  }
}
</script>

<style lang="scss" scoped>
.tax-compliance-list {
  .search-form {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .el-form-item {
      margin-bottom: 16px;
    }
  }

  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: white;
    border-radius: 8px;
    margin-bottom: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .toolbar-left {
      .el-button {
        margin-right: 8px;
      }
    }

    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .table-container {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    .el-table {
      .progress-text {
        margin-left: 8px;
        font-size: 12px;
        color: #909399;
      }

      .text-muted {
        color: #c0c4cc;
      }
    }
  }

  .card-container {
    .compliance-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      margin-bottom: 20px;
      cursor: pointer;
      transition: all 0.3s;
      overflow: hidden;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
      }

      .card-header {
        padding: 16px 20px 12px;
        border-bottom: 1px solid #f0f0f0;

        .card-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
          line-height: 1.4;
        }

        .card-code {
          font-size: 12px;
          color: #909399;
          background: #f5f7fa;
          padding: 2px 8px;
          border-radius: 4px;
          display: inline-block;
        }
      }

      .card-content {
        padding: 16px 20px;

        .card-row {
          display: flex;
          align-items: center;
          margin-bottom: 8px;
          font-size: 14px;

          &:last-child {
            margin-bottom: 0;
          }

          .label {
            color: #909399;
            width: 80px;
            flex-shrink: 0;
          }

          .value {
            color: #303133;
            flex: 1;
          }

          .progress-text {
            margin-left: 8px;
            font-size: 12px;
            color: #909399;
          }
        }
      }

      .card-footer {
        padding: 12px 20px;
        background: #fafbfc;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .create-time {
          font-size: 12px;
          color: #909399;
        }

        .card-actions {
          .el-button {
            padding: 0;
            margin-left: 8px;
          }
        }
      }
    }
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    margin-top: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .tax-compliance-list {
    .search-form {
      .el-form-item {
        margin-bottom: 12px;
      }

      .el-input,
      .el-select,
      .el-date-picker {
        width: 100% !important;
      }
    }

    .toolbar {
      flex-direction: column;
      align-items: stretch;
      gap: 12px;

      .toolbar-left,
      .toolbar-right {
        justify-content: center;
      }
    }

    .table-container {
      overflow-x: auto;
    }
  }
}
</style>
