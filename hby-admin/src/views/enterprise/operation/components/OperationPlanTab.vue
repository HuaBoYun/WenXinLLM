<template>
  <div class="operation-plan-tab">
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ planStatistics.totalPlans || 0 }}</div>
              <div class="statistics-label">计划总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon formulation">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ planStatistics.formulationPlans || 0 }}</div>
              <div class="statistics-label">制定中计划</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon execution">
              <i class="el-icon-loading"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ planStatistics.executionPlans || 0 }}</div>
              <div class="statistics-label">执行中计划</div>
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
              <div class="statistics-number">{{ planStatistics.completedPlans || 0 }}</div>
              <div class="statistics-label">已完成计划</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="计划类型" prop="planType">
          <el-select v-model="queryForm.planType" placeholder="请选择计划类型" clearable style="width: 150px;">
            <el-option label="年度计划" value="年度计划"></el-option>
            <el-option label="季度计划" value="季度计划"></el-option>
            <el-option label="月度计划" value="月度计划"></el-option>
            <el-option label="专项计划" value="专项计划"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计划状态" prop="planStatus">
          <el-select v-model="queryForm.planStatus" placeholder="请选择计划状态" clearable style="width: 150px;">
            <el-option label="待制定" value="待制定"></el-option>
            <el-option label="制定中" value="制定中"></el-option>
            <el-option label="已制定" value="已制定"></el-option>
            <el-option label="已暂停" value="已暂停"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select v-model="queryForm.approvalStatus" placeholder="请选择审批状态" clearable style="width: 150px;">
            <el-option label="未提交" value="未提交"></el-option>
            <el-option label="待审批" value="待审批"></el-option>
            <el-option label="初审通过" value="初审通过"></el-option>
            <el-option label="终审通过" value="终审通过"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态" prop="executionStatus">
          <el-select v-model="queryForm.executionStatus" placeholder="请选择执行状态" clearable style="width: 150px;">
            <el-option label="未开始" value="未开始"></el-option>
            <el-option label="执行中" value="执行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="已暂停" value="已暂停"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增计划</el-button>
          <el-button type="warning" @click="handleBatchOperation" icon="el-icon-setting">批量操作</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <div slot="header" class="card-header">
        <span class="card-title">经营计划列表</span>
        <div class="card-actions">
          <el-button size="small" @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
          <el-button size="small" @click="handleExport" icon="el-icon-download">导出</el-button>
        </div>
      </div>

      <el-table
        :data="planData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="planName" label="计划名称" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="planType" label="计划类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPlanTypeTag(scope.row.planType)" size="small">
              {{ scope.row.planType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="planYear" label="计划年度" width="100" align="center"></el-table-column>
        <el-table-column prop="planStatus" label="计划状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPlanStatusTag(scope.row.planStatus)" size="small">
              {{ scope.row.planStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="formulationProgress" label="制定进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.formulationProgress || 0"
              :stroke-width="8"
              :show-text="false"
              :color="getProgressColor(scope.row.formulationProgress)"
            ></el-progress>
            <span style="margin-left: 5px; font-size: 12px;">{{ scope.row.formulationProgress || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getApprovalStatusTag(scope.row.approvalStatus)" size="small">
              {{ scope.row.approvalStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionStatus" label="执行状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getExecutionStatusTag(scope.row.executionStatus)" size="small">
              {{ scope.row.executionStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionProgress" label="执行进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.executionProgress || 0"
              :stroke-width="8"
              :show-text="false"
              :color="getProgressColor(scope.row.executionProgress)"
            ></el-progress>
            <span style="margin-left: 5px; font-size: 12px;">{{ scope.row.executionProgress || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="planManager" label="制定负责人" width="120" align="center"></el-table-column>
        <el-table-column prop="monitoringManager" label="执行负责人" width="120" align="center"></el-table-column>
        <el-table-column prop="formulationEndTime" label="计划完成时间" width="150" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.formulationEndTime) }}
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
                <el-dropdown-item :command="{action: 'startFormulation', row: scope.row}" v-if="scope.row.planStatus === '待制定'">
                  <i class="el-icon-video-play"></i> 启动制定
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'pauseFormulation', row: scope.row}" v-if="scope.row.planStatus === '制定中'">
                  <i class="el-icon-video-pause"></i> 暂停制定
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'resumeFormulation', row: scope.row}" v-if="scope.row.planStatus === '已暂停'">
                  <i class="el-icon-video-play"></i> 恢复制定
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'completeFormulation', row: scope.row}" v-if="scope.row.planStatus === '制定中'">
                  <i class="el-icon-circle-check"></i> 完成制定
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'submitApproval', row: scope.row}" v-if="scope.row.approvalStatus === '未提交'">
                  <i class="el-icon-s-check"></i> 提交审批
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'startExecution', row: scope.row}" v-if="scope.row.executionStatus === '未开始' && scope.row.approvalStatus === '终审通过'">
                  <i class="el-icon-video-play"></i> 启动执行
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'adjust', row: scope.row}">
                  <i class="el-icon-edit"></i> 计划调整
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'monitor', row: scope.row}">
                  <i class="el-icon-view"></i> 计划监控
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

    <!-- 计划详情对话框 -->
    <PlanDetailDialog
      :visible.sync="detailDialogVisible"
      :plan-data="currentPlan"
    />

    <!-- 计划编辑对话框 -->
    <PlanEditDialog
      :visible.sync="editDialogVisible"
      :plan-data="currentPlan"
      @refresh="loadPlanData"
    />

    <!-- 审批对话框 -->
    <PlanApprovalDialog
      :visible.sync="approvalDialogVisible"
      :plan-data="currentPlan"
      @refresh="loadPlanData"
    />

    <!-- 计划调整对话框 -->
    <PlanAdjustmentDialog
      :visible.sync="adjustmentDialogVisible"
      :plan-data="currentPlan"
      @refresh="loadPlanData"
    />

    <!-- 计划监控对话框 -->
    <PlanMonitoringDialog
      :visible.sync="monitoringDialogVisible"
      :plan-data="currentPlan"
      @refresh="loadPlanData"
    />

    <!-- 批量操作对话框 -->
    <BatchOperationDialog
      :visible.sync="batchDialogVisible"
      :selected-plans="selectedPlans"
      @refresh="loadPlanData"
    />
  </div>
</template>

<script>
import {
  startPlanFormulation,
  pausePlanFormulation,
  resumePlanFormulation,
  completePlanFormulation,
  submitForApproval,
  startPlanExecution,
  deleteOperationPlan
} from '@/api/enterprise/operationPlan'
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

import PlanDetailDialog from './PlanDetailDialog'
import PlanEditDialog from './PlanEditDialog'
import PlanApprovalDialog from './PlanApprovalDialog'
import PlanAdjustmentDialog from './PlanAdjustmentDialog'
import PlanMonitoringDialog from './PlanMonitoringDialog'
import BatchOperationDialog from './BatchOperationDialog'

export default {
  name: 'OperationPlanTab',
  components: {
    PlanDetailDialog,
    PlanEditDialog,
    PlanApprovalDialog,
    PlanAdjustmentDialog,
    PlanMonitoringDialog,
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
      planData: [],
      total: 0,
      planStatistics: {},
      selectedPlans: [],
      currentPlan: {},
      
      // 查询表单
      queryForm: {
        enterpriseId: '',
        planType: '',
        planName: '',
        planYear: '',
        planStatus: '',
        approvalStatus: '',
        executionStatus: '',
        pageNumber: 1,
        pageSize: 20
      },
      
      // 对话框状态
      detailDialogVisible: false,
      editDialogVisible: false,
      approvalDialogVisible: false,
      adjustmentDialogVisible: false,
      monitoringDialogVisible: false,
      batchDialogVisible: false
    }
  },
  watch: {
    enterpriseId: {
      handler(newVal) {
        if (newVal) {
          this.queryForm.enterpriseId = newVal
          this.loadPlanData()
          this.loadStatistics()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 兼容不同响应格式
    extractData(response) {
      if (!response) return null
      const resData = response.data || response
      // R对象格式: {result:200, data:{records:[...], total:X}}
      if (resData && resData.records) {
        return resData
      }
      return resData
    },

    // 加载计划数据
    async loadPlanData() {
      if (!this.enterpriseId) return
      
      this.loading = true
      try {
        const response = await request({
          url: '/monitor/v1/enterprise/operation/plan/list',
          method: 'post',
          data: transData(this.queryForm),
          headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        const pageData = this.extractData(response)
        this.planData = pageData && pageData.records ? pageData.records : []
        this.total = pageData && pageData.total ? pageData.total : 0
      } catch (error) {
        console.error('加载经营计划数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      if (!this.enterpriseId) return
      
      try {
        const response = await request({
          url: `/monitor/v1/enterprise/operation/plan/statistics/${this.enterpriseId}`,
          method: 'get'
        })
        const data = this.extractData(response)
        // 数据库返回的列名为大写，需转换为小驼峰以匹配模板
        if (data) {
          this.planStatistics = {
            totalPlans: data.TOTAL_PLANS || data.totalPlans || 0,
            formulationPlans: data.FORMULATION_PLANS || data.formulationPlans || 0,
            executionPlans: data.EXECUTION_PLANS || data.executionPlans || 0,
            completedPlans: data.COMPLETED_PLANS || data.completedPlans || 0
          }
        } else {
          this.planStatistics = {}
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.loadPlanData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm.enterpriseId = this.enterpriseId
      this.loadPlanData()
    },

    // 新增
    handleAdd() {
      this.currentPlan = { enterpriseId: this.enterpriseId }
      this.editDialogVisible = true
    },

    // 查看详情
    handleView(row) {
      this.currentPlan = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentPlan = { ...row }
      this.editDialogVisible = true
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedPlans = selection
    },

    // 批量操作
    handleBatchOperation() {
      if (this.selectedPlans.length === 0) {
        this.$message.warning('请先选择要操作的计划')
        return
      }
      this.batchDialogVisible = true
    },

    // 命令处理
    async handleCommand({ action, row }) {
      switch (action) {
        case 'startFormulation':
          await this.handleStartFormulation(row)
          break
        case 'pauseFormulation':
          await this.handlePauseFormulation(row)
          break
        case 'resumeFormulation':
          await this.handleResumeFormulation(row)
          break
        case 'completeFormulation':
          await this.handleCompleteFormulation(row)
          break
        case 'submitApproval':
          this.handleSubmitApproval(row)
          break
        case 'startExecution':
          await this.handleStartExecution(row)
          break
        case 'adjust':
          this.handleAdjust(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 启动制定
    async handleStartFormulation(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await startPlanFormulation(row.planId, updateBy)
        // 启动制定同时启动执行，简化流程
        await startPlanExecution(row.planId, updateBy)
        this.$message.success('启动制定成功，计划已进入执行状态')
        this.loadPlanData()
        this.loadStatistics()
      } catch (error) {
        // 即使启动执行失败，制定可能已成功，刷新列表
        this.loadPlanData()
        this.$message.error('启动制定失败')
      }
    },

    // 暂停制定
    async handlePauseFormulation(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await pausePlanFormulation(row.planId, updateBy)
        this.$message.success('暂停制定成功')
        this.loadPlanData()
      } catch (error) {
        this.$message.error('暂停制定失败')
      }
    },

    // 恢复制定
    async handleResumeFormulation(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await resumePlanFormulation(row.planId, updateBy)
        this.$message.success('恢复制定成功')
        this.loadPlanData()
      } catch (error) {
        this.$message.error('恢复制定失败')
      }
    },

    // 完成制定
    async handleCompleteFormulation(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await completePlanFormulation(row.planId, updateBy)
        this.$message.success('完成制定成功')
        this.loadPlanData()
      } catch (error) {
        this.$message.error('完成制定失败')
      }
    },

    // 提交审批
    handleSubmitApproval(row) {
      this.currentPlan = { ...row }
      this.approvalDialogVisible = true
    },

    // 启动执行
    async handleStartExecution(row) {
      try {
        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await startPlanExecution(row.planId, updateBy)
        this.$message.success('启动执行成功')
        this.loadPlanData()
      } catch (error) {
        this.$message.error('启动执行失败')
      }
    },

    // 计划调整
    handleAdjust(row) {
      this.currentPlan = { ...row }
      this.adjustmentDialogVisible = true
    },

    // 计划监控
    handleMonitor(row) {
      this.currentPlan = { ...row }
      this.monitoringDialogVisible = true
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该经营计划吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const updateBy = (this.$store && this.$store.getters && this.$store.getters.name) || 'admin'
        await deleteOperationPlan(row.planId, updateBy)
        this.$message.success('删除成功')
        this.loadPlanData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },

    // 刷新
    handleRefresh() {
      this.loadPlanData()
      this.loadStatistics()
    },

    // 导出
    async handleExport() {
      if (!this.enterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      try {
        const response = await request({
          url: '/monitor/v1/enterprise/operation/plan/export',
          method: 'post',
          data: transData({ enterpriseId: this.enterpriseId, ...this.queryForm }),
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          responseType: 'blob'
        })
        // 如果返回的是blob则下载
        if (response instanceof Blob || (response && response.size)) {
          const url = window.URL.createObjectURL(new Blob([response]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', `经营计划_${new Date().toLocaleDateString()}.xlsx`)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          // 如果后端返回JSON格式的数据，则手动生成CSV
          const data = this.planData
          if (!data || data.length === 0) {
            this.$message.warning('暂无数据可导出')
            return
          }
          const headers = ['计划名称', '计划类型', '计划年度', '计划状态', '审批状态', '执行状态', '制定负责人', '执行负责人']
          const fields = ['planName', 'planType', 'planYear', 'planStatus', 'approvalStatus', 'executionStatus', 'planManager', 'monitoringManager']
          let csv = '\uFEFF' + headers.join(',') + '\n'
          data.forEach(row => {
            csv += fields.map(f => '"' + (row[f] || '') + '"').join(',') + '\n'
          })
          const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', `经营计划_${new Date().toLocaleDateString()}.csv`)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.loadPlanData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadPlanData()
    },

    // 获取计划类型标签
    getPlanTypeTag(type) {
      const tagMap = {
        '年度计划': 'primary',
        '季度计划': 'success',
        '月度计划': 'warning',
        '专项计划': 'info'
      }
      return tagMap[type] || 'default'
    },

    // 获取计划状态标签
    getPlanStatusTag(status) {
      const tagMap = {
        '待制定': 'info',
        '制定中': 'warning',
        '已制定': 'success',
        '已暂停': 'danger',
        '已取消': 'danger'
      }
      return tagMap[status] || 'default'
    },

    // 获取审批状态标签
    getApprovalStatusTag(status) {
      const tagMap = {
        '未提交': 'info',
        '待审批': 'warning',
        '初审通过': 'primary',
        '终审通过': 'success'
      }
      return tagMap[status] || 'default'
    },

    // 获取执行状态标签
    getExecutionStatusTag(status) {
      const tagMap = {
        '未开始': 'info',
        '执行中': 'warning',
        '已完成': 'success',
        '已暂停': 'danger'
      }
      return tagMap[status] || 'default'
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
.operation-plan-tab {
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

.statistics-icon.formulation {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.execution {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.completed {
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
