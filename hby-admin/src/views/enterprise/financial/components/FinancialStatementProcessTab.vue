<template>
  <div class="financial-statement-process-tab">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ processStatistics.totalProcesses || 0 }}</div>
              <div class="statistics-label">编制流程总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ processStatistics.inProgressProcesses || 0 }}</div>
              <div class="statistics-label">进行中流程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ processStatistics.completedProcesses || 0 }}</div>
              <div class="statistics-label">已完成流程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon quality">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ (processStatistics.avgOverallQualityScore || 0).toFixed(1) }}</div>
              <div class="statistics-label">平均质量评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="报表类型" prop="statementType">
          <el-select v-model="queryForm.statementType" placeholder="请选择报表类型" clearable style="width: 150px;">
            <el-option label="资产负债表" value="BALANCE_SHEET"></el-option>
            <el-option label="利润表" value="INCOME_STATEMENT"></el-option>
            <el-option label="现金流量表" value="CASH_FLOW"></el-option>
            <el-option label="所有者权益变动表" value="EQUITY_CHANGE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="编制状态" prop="compilationStatus">
          <el-select v-model="queryForm.compilationStatus" placeholder="请选择编制状态" clearable style="width: 150px;">
            <el-option label="待开始" value="待开始"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="已暂停" value="已暂停"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-select v-model="queryForm.auditStatus" placeholder="请选择审核状态" clearable style="width: 150px;">
            <el-option label="未提交" value="未提交"></el-option>
            <el-option label="待审核" value="待审核"></el-option>
            <el-option label="初审通过" value="初审通过"></el-option>
            <el-option label="复审通过" value="复审通过"></el-option>
            <el-option label="终审通过" value="终审通过"></el-option>
            <el-option label="已签字确认" value="已签字确认"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间" prop="reportingPeriod">
          <el-input v-model="queryForm.reportingPeriod" placeholder="请输入报告期间" clearable style="width: 150px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增流程</el-button>
          <el-button type="warning" @click="handleBatchOperation" icon="el-icon-setting">批量操作</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <div slot="header" class="card-header">
        <span class="card-title">财务报表编制流程列表</span>
        <div class="card-actions">
          <el-button size="small" @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
          <el-button size="small" @click="handleExport" icon="el-icon-download">导出</el-button>
        </div>
      </div>

      <el-table
        :data="processData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="statementName" label="报表名称" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="statementType" label="报表类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatementTypeTag(scope.row.statementType)" size="small">
              {{ getStatementTypeText(scope.row.statementType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportingPeriod" label="报告期间" width="120" align="center"></el-table-column>
        <el-table-column prop="compilationStatus" label="编制状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCompilationStatusTag(scope.row.compilationStatus)" size="small">
              {{ getCompilationStatusText(scope.row.compilationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="compilationProgress" label="编制进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.compilationProgress || 0"
              :stroke-width="8"
              :show-text="false"
              :color="getProgressColor(scope.row.compilationProgress)"
            ></el-progress>
            <span style="margin-left: 5px; font-size: 12px;">{{ scope.row.compilationProgress || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAuditStatusTag(scope.row.auditStatus)" size="small">
              {{ getAuditStatusText(scope.row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="overallQualityScore" label="质量评分" width="100" align="center">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.qualityStars"
              :max="5"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            ></el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="compilationManager" label="编制负责人" width="120" align="center"></el-table-column>
        <el-table-column prop="plannedEndTime" label="计划完成时间" width="150" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.plannedEndTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'start', row: scope.row}" v-if="scope.row.compilationStatus === 'not_started' || scope.row.compilationStatus === '待开始'">
                  <i class="el-icon-video-play"></i> 启动流程
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'pause', row: scope.row}" v-if="scope.row.compilationStatus === 'in_progress' || scope.row.compilationStatus === '进行中'">
                  <i class="el-icon-video-pause"></i> 暂停流程
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'resume', row: scope.row}" v-if="scope.row.compilationStatus === 'paused' || scope.row.compilationStatus === '已暂停'">
                  <i class="el-icon-video-play"></i> 恢复流程
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'complete', row: scope.row}" v-if="scope.row.compilationStatus === 'in_progress' || scope.row.compilationStatus === '进行中'">
                  <i class="el-icon-circle-check"></i> 完成流程
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'audit', row: scope.row}" v-if="scope.row.auditStatus === 'not_submitted' || scope.row.auditStatus === '未提交'">
                  <i class="el-icon-s-check"></i> 提交审核
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'quality', row: scope.row}">
                  <i class="el-icon-star-on"></i> 质量检查
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  <i class="el-icon-delete"></i> 删除
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
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 流程详情对话框 -->
    <StatementProcessDetailDialog
      :visible.sync="detailDialogVisible"
      :process-data="currentProcess"
    />

    <!-- 流程编辑对话框 -->
    <StatementProcessEditDialog
      :visible.sync="editDialogVisible"
      :process-data="currentProcess"
      @refresh="loadProcessData"
    />

    <!-- 审核对话框 -->
    <StatementAuditDialog
      :visible.sync="auditDialogVisible"
      :process-data="currentProcess"
      @refresh="loadProcessData"
    />

    <!-- 质量检查对话框 -->
    <StatementQualityCheckDialog
      :visible.sync="qualityDialogVisible"
      :process-data="currentProcess"
      @refresh="loadProcessData"
    />

    <!-- 批量操作对话框 -->
    <BatchOperationDialog
      :visible.sync="batchDialogVisible"
      :selected-processes="selectedProcesses"
      @refresh="loadProcessData"
    />
  </div>
</template>

<script>
import {
  getFinancialStatementProcessPage,
  getStatisticsByEnterpriseId,
  startCompilationProcess,
  pauseCompilationProcess,
  resumeCompilationProcess,
  completeCompilationProcess,
  submitForAudit,
  deleteFinancialStatementProcess
} from '@/api/enterprise/financialStatementProcess'

import StatementProcessDetailDialog from './StatementProcessDetailDialog'
import StatementProcessEditDialog from './StatementProcessEditDialog'
import StatementAuditDialog from './StatementAuditDialog'
import StatementQualityCheckDialog from './StatementQualityCheckDialog'
import BatchOperationDialog from './BatchOperationDialog'

export default {
  name: 'FinancialStatementProcessTab',
  components: {
    StatementProcessDetailDialog,
    StatementProcessEditDialog,
    StatementAuditDialog,
    StatementQualityCheckDialog,
    BatchOperationDialog
  },
  props: {
    enterpriseId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      processData: [],
      total: 0,
      processStatistics: {},
      selectedProcesses: [],
      currentProcess: {},
      
      // 查询表单
      queryForm: {
        enterpriseId: '',
        statementType: '',
        reportingPeriod: '',
        compilationStatus: '',
        auditStatus: '',
        pageNumber: 1,
        pageSize: 20
      },
      
      // 对话框状态
      detailDialogVisible: false,
      editDialogVisible: false,
      auditDialogVisible: false,
      qualityDialogVisible: false,
      batchDialogVisible: false
    }
  },
  watch: {
    enterpriseId: {
      handler(newVal) {
        if (newVal) {
          this.queryForm.enterpriseId = newVal
          this.loadProcessData()
          this.loadStatistics()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 加载流程数据
    async loadProcessData() {
      if (!this.enterpriseId) return
      
      this.loading = true
      try {
        const response = await getFinancialStatementProcessPage(this.queryForm)
        const pageData = response.data || {}
        // 兼容IPage(records/total)和PageResult(tlist/totalRecord)两种格式
        const records = pageData.records || pageData.tlist || []
        this.processData = records.map(item => ({
          ...item,
          qualityStars: Math.round((item.overallQualityScore || 0) / 20) // 转换为5星制
        }))
        this.total = pageData.total || pageData.totalRecord || 0
      } catch (error) {
        console.error('加载财务报表编制流程数据失败:', error)
        this.processData = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 从统计数据中安全提取值（兼容达梦数据库返回大写/小写/驼峰key，且不将0视为falsy）
    getStatValue(data, key, defaultValue = 0) {
      if (data[key] !== undefined && data[key] !== null) return Number(data[key]) || 0
      const upperKey = key.toUpperCase()
      if (data[upperKey] !== undefined && data[upperKey] !== null) return Number(data[upperKey]) || 0
      const lowerKey = key.toLowerCase()
      if (data[lowerKey] !== undefined && data[lowerKey] !== null) return Number(data[lowerKey]) || 0
      // 遍历所有key做大小写无关匹配（兜底）
      const targetLower = key.toLowerCase()
      for (const k of Object.keys(data)) {
        if (k.toLowerCase() === targetLower && data[k] !== undefined && data[k] !== null) {
          return Number(data[k]) || 0
        }
      }
      return defaultValue
    },

    // 加载统计数据
    async loadStatistics() {
      if (!this.enterpriseId) return

      try {
        const response = await getStatisticsByEnterpriseId(this.enterpriseId)
        const data = response.data || {}
        // 使用安全提取方法，兼容达梦数据库返回大写key
        this.processStatistics = {
          totalProcesses: this.getStatValue(data, 'totalProcesses'),
          inProgressProcesses: this.getStatValue(data, 'inProgressProcesses'),
          completedProcesses: this.getStatValue(data, 'completedProcesses'),
          avgOverallQualityScore: this.getStatValue(data, 'avgOverallQualityScore')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.processStatistics = {}
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.loadProcessData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm.enterpriseId = this.enterpriseId
      this.loadProcessData()
    },

    // 新增
    handleAdd() {
      this.currentProcess = { enterpriseId: this.enterpriseId }
      this.editDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.currentProcess = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentProcess = { ...row }
      this.editDialogVisible = true
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedProcesses = selection
    },

    // 批量操作
    handleBatchOperation() {
      if (this.selectedProcesses.length === 0) {
        this.$message.warning('请先选择要操作的流程')
        return
      }
      this.batchDialogVisible = true
    },

    // 命令处理
    async handleCommand({ action, row }) {
      switch (action) {
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
        case 'audit':
          this.handleAudit(row)
          break
        case 'quality':
          this.handleQuality(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 启动流程
    async handleStart(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await startCompilationProcess(row.processId, updateBy)
        this.$message.success('启动流程成功')
        this.loadProcessData()
      } catch (error) {
        this.$message.error('启动流程失败')
      }
    },

    // 暂停流程
    async handlePause(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await pauseCompilationProcess(row.processId, updateBy)
        this.$message.success('暂停流程成功')
        this.loadProcessData()
      } catch (error) {
        this.$message.error('暂停流程失败')
      }
    },

    // 恢复流程
    async handleResume(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await resumeCompilationProcess(row.processId, updateBy)
        this.$message.success('恢复流程成功')
        this.loadProcessData()
      } catch (error) {
        this.$message.error('恢复流程失败')
      }
    },

    // 完成流程
    async handleComplete(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await completeCompilationProcess(row.processId, updateBy)
        this.$message.success('完成流程成功')
        this.loadProcessData()
      } catch (error) {
        this.$message.error('完成流程失败')
      }
    },

    // 提交审核
    handleAudit(row) {
      this.currentProcess = { ...row }
      this.auditDialogVisible = true
    },

    // 质量检查
    handleQuality(row) {
      this.currentProcess = { ...row }
      this.qualityDialogVisible = true
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该编制流程吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await deleteFinancialStatementProcess(row.processId, updateBy)
        this.$message.success('删除成功')
        this.loadProcessData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 刷新
    handleRefresh() {
      this.loadProcessData()
      this.loadStatistics()
    },

    // 导出
    handleExport() {
      if (!this.processData || this.processData.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = ['报表名称', '报表类型', '报告期间', '编制状态', '编制进度', '审核状态', '质量评分', '编制负责人', '计划完成时间']
      const rows = this.processData.map(item => [
        item.statementName || '',
        this.getStatementTypeText(item.statementType),
        item.reportingPeriod || '',
        this.getCompilationStatusText(item.compilationStatus),
        (item.compilationProgress || 0) + '%',
        this.getAuditStatusText(item.auditStatus),
        item.overallQualityScore || 0,
        item.compilationManager || '',
        this.formatDateTime(item.plannedEndTime)
      ])
      const BOM = '\uFEFF'
      const csvContent = BOM + [headers.join(','), ...rows.map(r => r.map(v => '"' + String(v).replace(/"/g, '""') + '"').join(','))].join('\n')
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const now = new Date()
      const dateStr = now.getFullYear() + String(now.getMonth() + 1).padStart(2, '0') + String(now.getDate()).padStart(2, '0')
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = '报表编制流程_' + dateStr + '.csv'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(link.href)
      this.$message.success('导出成功')
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.loadProcessData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadProcessData()
    },

    // 获取报表类型标签
    getStatementTypeTag(type) {
      const tagMap = {
        '资产负债表': 'primary',
        '利润表': 'success',
        '现金流量表': 'warning',
        '所有者权益变动表': 'info',
        'BALANCE_SHEET': 'primary',
        'INCOME_STATEMENT': 'success',
        'CASH_FLOW': 'warning',
        'EQUITY_CHANGE': 'info',
        'balance_sheet': 'primary',
        'income_statement': 'success',
        'cash_flow': 'warning',
        'equity_change': 'info'
      }
      return tagMap[type] || 'default'
    },

    // 获取报表类型文本
    getStatementTypeText(type) {
      const textMap = {
        '资产负债表': '资产负债表',
        '利润表': '利润表',
        '现金流量表': '现金流量表',
        '所有者权益变动表': '所有者权益变动表',
        'BALANCE_SHEET': '资产负债表',
        'INCOME_STATEMENT': '利润表',
        'CASH_FLOW': '现金流量表',
        'EQUITY_CHANGE': '所有者权益变动表',
        'balance_sheet': '资产负债表',
        'income_statement': '利润表',
        'cash_flow': '现金流量表',
        'equity_change': '所有者权益变动表'
      }
      return textMap[type] || type
    },

    // 获取编制状态标签
    getCompilationStatusTag(status) {
      const tagMap = {
        '待开始': 'info',
        '进行中': 'warning',
        '已完成': 'success',
        '已暂停': 'danger',
        '已取消': 'danger',
        'NOT_STARTED': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'PAUSED': 'danger',
        'CANCELLED': 'danger',
        'not_started': 'info',
        'in_progress': 'warning',
        'completed': 'success',
        'paused': 'danger',
        'cancelled': 'danger'
      }
      return tagMap[status] || 'default'
    },

    // 获取编制状态文本
    getCompilationStatusText(status) {
      const textMap = {
        '待开始': '待开始',
        '进行中': '进行中',
        '已完成': '已完成',
        '已暂停': '已暂停',
        '已取消': '已取消',
        'NOT_STARTED': '待开始',
        'IN_PROGRESS': '进行中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停',
        'CANCELLED': '已取消',
        'not_started': '待开始',
        'in_progress': '进行中',
        'completed': '已完成',
        'paused': '已暂停',
        'cancelled': '已取消'
      }
      return textMap[status] || status
    },

    // 获取审核状态标签
    getAuditStatusTag(status) {
      const tagMap = {
        '未提交': 'info',
        '待审核': 'warning',
        '初审通过': 'primary',
        '复审通过': 'primary',
        '终审通过': 'success',
        '已签字确认': 'success',
        'NOT_SUBMITTED': 'info',
        'PENDING_AUDIT': 'warning',
        'FIRST_AUDIT_PASSED': 'primary',
        'SECOND_AUDIT_PASSED': 'primary',
        'FINAL_AUDIT_PASSED': 'success',
        'SIGNED_CONFIRMED': 'success',
        'not_submitted': 'info',
        'pending_audit': 'warning',
        'pending': 'warning',
        'approved': 'success',
        'rejected': 'danger',
        'first_audit_passed': 'primary',
        'second_audit_passed': 'primary',
        'final_audit_passed': 'success',
        'signed_confirmed': 'success'
      }
      return tagMap[status] || 'default'
    },

    // 获取审核状态文本
    getAuditStatusText(status) {
      const textMap = {
        '未提交': '未提交',
        '待审核': '待审核',
        '初审通过': '初审通过',
        '复审通过': '复审通过',
        '终审通过': '终审通过',
        '已签字确认': '已签字确认',
        'NOT_SUBMITTED': '未提交',
        'PENDING_AUDIT': '待审核',
        'FIRST_AUDIT_PASSED': '初审通过',
        'SECOND_AUDIT_PASSED': '复审通过',
        'FINAL_AUDIT_PASSED': '终审通过',
        'SIGNED_CONFIRMED': '已签字确认',
        'not_submitted': '未提交',
        'pending_audit': '待审核',
        'pending': '待审核',
        'approved': '已通过',
        'rejected': '已驳回',
        'first_audit_passed': '初审通过',
        'second_audit_passed': '复审通过',
        'final_audit_passed': '终审通过',
        'signed_confirmed': '已签字确认'
      }
      return textMap[status] || status
    },

    // 获取进度颜色
    getProgressColor(percentage) {
      if (percentage < 30) return '#f56c6c'
      if (percentage < 70) return '#e6a23c'
      return '#67c23a'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.financial-statement-process-tab {
  padding: 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.card-actions {
  display: flex;
  gap: 10px;
}

.statistics-card {
  height: 100px;
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
}

.statistics-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.statistics-icon.progress {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.quality {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.statistics-label {
  font-size: 12px;
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
