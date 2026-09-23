<template>
  <div class="approval-flow">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算审批流程管理</h2>
      <p>管理预算审批流程，支持多级审批、并行审批、条件审批等多种审批模式</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateFlow">创建流程</el-button>
            <el-button type="success" icon="el-icon-check" @click="handleBatchApprove">批量审批</el-button>
            <el-button type="warning" icon="el-icon-close" @click="handleBatchReject">批量拒绝</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleExportFlow">导出流程</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleFlowSettings">流程设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 流程统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card pending-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ flowStats.pendingCount }}</div>
            <div class="stat-label">待审批</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card approved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ flowStats.approvedCount }}</div>
            <div class="stat-label">已审批</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card rejected-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ flowStats.rejectedCount }}</div>
            <div class="stat-label">已拒绝</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-close"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ flowStats.totalCount }}</div>
            <div class="stat-label">总计</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="流程名称">
          <el-input
            v-model="queryForm.flowName"
            placeholder="请输入流程名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="流程类型">
          <el-select
            v-model="queryForm.flowType"
            placeholder="请选择流程类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in flowTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select
            v-model="queryForm.approvalStatus"
            placeholder="请选择审批状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in approvalStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申请人">
          <el-input
            v-model="queryForm.applicant"
            placeholder="请输入申请人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="申请时间">
          <el-date-picker
            v-model="queryForm.applyDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 流程列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">审批流程列表</span>
        <div class="table-tools">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="flowList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="flowName" label="流程名称" width="200" show-overflow-tooltip />
        
        <el-table-column prop="flowType" label="流程类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getFlowTypeColor(scope.row.flowType)" size="mini">
              {{ getFlowTypeText(scope.row.flowType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="applicant" label="申请人" width="100" show-overflow-tooltip />
        
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="currentNode" label="当前节点" width="150" show-overflow-tooltip />
        
        <el-table-column prop="currentApprover" label="当前审批人" width="120" show-overflow-tooltip />
        
        <el-table-column prop="approvalStatus" label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getApprovalStatusType(scope.row.approvalStatus)" size="mini">
              {{ getApprovalStatusText(scope.row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="mini">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="applyTime" label="申请时间" width="150" align="center" />
        
        <el-table-column prop="deadline" label="截止时间" width="150" align="center">
          <template slot-scope="scope">
            <span :class="getDeadlineClass(scope.row.deadline)">
              {{ scope.row.deadline }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="320" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewFlow(scope.row)"
            >查看</el-button>

            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditFlow(scope.row)"
            >编辑</el-button>

            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click="handleDeleteFlow(scope.row)"
            >删除</el-button>

            <el-button
              v-if="canApprove(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click="handleApprove(scope.row)"
            >审批</el-button>

            <el-button
              v-if="canReject(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-close"
              class="danger-text"
              @click="handleReject(scope.row)"
            >拒绝</el-button>

            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="flowChart" icon="el-icon-share">流程图</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">审批历史</el-dropdown-item>
                <el-dropdown-item command="delegate" icon="el-icon-user">委派</el-dropdown-item>
                <el-dropdown-item command="recall" icon="el-icon-refresh-left">撤回</el-dropdown-item>
                <el-dropdown-item command="urge" icon="el-icon-message">催办</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审批对话框 -->
    <el-dialog
      :title="approvalDialogTitle"
      :visible.sync="approvalDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleApprovalDialogClose"
    >
      <div class="approval-content">
        <!-- 流程信息 -->
        <div class="flow-info">
          <h4>流程信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="流程名称">{{ currentFlow.flowName }}</el-descriptions-item>
            <el-descriptions-item label="申请人">{{ currentFlow.applicant }}</el-descriptions-item>
            <el-descriptions-item label="预算金额">{{ formatAmount(currentFlow.budgetAmount) }}</el-descriptions-item>
            <el-descriptions-item label="申请时间">{{ currentFlow.applyTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 审批表单 -->
        <div class="approval-form">
          <h4>审批意见</h4>
          <el-form
            ref="approvalForm"
            :model="approvalForm"
            :rules="approvalRules"
            label-width="100px"
            size="small"
          >
            <el-form-item label="审批结果" prop="approvalResult">
              <el-radio-group v-model="approvalForm.approvalResult">
                <el-radio label="APPROVED">同意</el-radio>
                <el-radio label="REJECTED">拒绝</el-radio>
                <el-radio label="RETURNED">退回</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="审批意见" prop="approvalComment">
              <el-input
                v-model="approvalForm.approvalComment"
                type="textarea"
                :rows="4"
                placeholder="请输入审批意见"
              />
            </el-form-item>
            
            <el-form-item v-if="approvalForm.approvalResult === 'APPROVED'" label="下一审批人" prop="nextApprover">
              <el-select
                v-model="approvalForm.nextApprover"
                placeholder="请选择下一审批人"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="user in approverOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="附件">
              <el-upload
                ref="upload"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :on-success="handleUploadSuccess"
                :on-remove="handleUploadRemove"
                :file-list="fileList"
                multiple
                :limit="5"
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过10MB</div>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitApproval">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 流程图对话框 -->
    <el-dialog
      title="流程图"
      :visible.sync="flowChartDialogVisible"
      width="80%"
      :close-on-click-modal="false"
    >
      <div class="flow-chart-container" v-if="currentFlowChart">
        <div class="flow-chart-info">
          <span>流程名称：{{ currentFlowChart.flowName }}</span>
          <span>流程类型：{{ getFlowTypeText(currentFlowChart.flowType) }}</span>
          <span>申请人：{{ currentFlowChart.applicant }}</span>
        </div>
        <div class="flow-chart-steps">
          <div
            v-for="(step, index) in flowChartSteps"
            :key="index"
            class="flow-step-wrapper"
          >
            <div class="flow-step" :class="'flow-step--' + step.status">
              <div class="flow-step-icon">
                <i :class="step.icon"></i>
              </div>
              <div class="flow-step-label">{{ step.name }}</div>
              <div class="flow-step-detail">{{ step.detail }}</div>
              <div class="flow-step-time" v-if="step.time">{{ step.time }}</div>
            </div>
            <div class="flow-step-arrow" v-if="index < flowChartSteps.length - 1">
              <i class="el-icon-right"></i>
            </div>
          </div>
        </div>
      </div>
      <div v-else style="text-align:center;color:#999;padding:40px 0;">暂无流程数据</div>
    </el-dialog>

    <!-- 审批历史对话框 -->
    <el-dialog
      title="审批历史"
      :visible.sync="historyDialogVisible"
      width="70%"
      :close-on-click-modal="false"
    >
      <el-timeline>
        <el-timeline-item
          v-for="(history, index) in approvalHistory"
          :key="index"
          :timestamp="history.approvalTime"
          :type="getHistoryType(history.result)"
        >
          <el-card>
            <div class="history-header">
              <span class="history-node">{{ history.nodeName }}</span>
              <el-tag :type="getHistoryTagType(history.result)" size="mini">
                {{ getHistoryResultText(history.result) }}
              </el-tag>
            </div>
            <div class="history-content">
              <p><strong>审批人：</strong>{{ history.approver }}</p>
              <p><strong>审批意见：</strong>{{ history.comment || '无' }}</p>
              <p v-if="history.duration"><strong>处理时长：</strong>{{ history.duration }}</p>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>

    <!-- 批量审批对话框 -->
    <el-dialog
      title="批量审批"
      :visible.sync="batchApprovalDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="batch-approval-content">
        <p>已选择 <strong>{{ selectedRows.length }}</strong> 个流程进行批量审批</p>
        
        <el-form
          ref="batchApprovalForm"
          :model="batchApprovalForm"
          :rules="batchApprovalRules"
          label-width="100px"
          size="small"
        >
          <el-form-item label="审批结果" prop="approvalResult">
            <el-radio-group v-model="batchApprovalForm.approvalResult">
              <el-radio label="APPROVED">批量同意</el-radio>
              <el-radio label="REJECTED">批量拒绝</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="审批意见" prop="approvalComment">
            <el-input
              v-model="batchApprovalForm.approvalComment"
              type="textarea"
              :rows="3"
              placeholder="请输入批量审批意见"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchApprovalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitBatchApproval">确认批量审批</el-button>
      </div>
    </el-dialog>

    <!-- 流程详情对话框 -->
    <el-dialog title="审批流程详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="流程名称">{{ flowDetail.flowName }}</el-descriptions-item>
        <el-descriptions-item label="流程类型">{{ flowDetail.flowType }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">{{ flowDetail.approvalStatus }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ flowDetail.applicant }}</el-descriptions-item>
        <el-descriptions-item label="当前审批人">{{ flowDetail.currentApprover }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ flowDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="说明" :span="2">{{ flowDetail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 流程设置对话框 -->
    <el-dialog title="审批流程设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="启用自动审批">
          <el-switch v-model="settingsForm.autoApproval" />
        </el-form-item>
        <el-form-item label="允许撤回">
          <el-switch v-model="settingsForm.allowWithdraw" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 创建/编辑流程对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="650px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="flowForm"
        :model="flowForm"
        :rules="flowFormRules"
        label-width="110px"
        size="small"
      >
        <el-form-item label="流程名称" prop="flowName">
          <el-input v-model="flowForm.flowName" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="流程类型" prop="approvalType">
          <el-select v-model="flowForm.approvalType" placeholder="请选择流程类型" style="width: 100%">
            <el-option
              v-for="item in flowTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算ID" prop="budgetId">
          <el-input v-model="flowForm.budgetId" placeholder="请输入预算ID" />
        </el-form-item>
        <el-form-item label="预算名称" prop="budgetName">
          <el-input v-model="flowForm.budgetName" placeholder="请输入预算名称" />
        </el-form-item>
        <el-form-item label="申请人" prop="submitterName">
          <el-input v-model="flowForm.submitterName" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="预算金额" prop="budgetAmount">
          <el-input-number v-model="flowForm.budgetAmount" :min="0" :precision="2" placeholder="请输入预算金额" style="width: 100%" />
        </el-form-item>
        <el-form-item label="当前节点" prop="currentNode">
          <el-input v-model="flowForm.currentNode" placeholder="请输入当前节点" />
        </el-form-item>
        <el-form-item label="当前审批人" prop="currentApproverName">
          <el-input v-model="flowForm.currentApproverName" placeholder="请输入当前审批人" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="flowForm.priority" placeholder="请选择优先级" style="width: 100%">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间" prop="dueDate">
          <el-date-picker
            v-model="flowForm.dueDate"
            type="datetime"
            placeholder="请选择截止时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input
            v-model="flowForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitFlow">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { approvalFlowApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'ApprovalFlow',
  data() {
    return {
      // 查询参数
      queryForm: {
        flowName: '',
        flowType: '',
        approvalStatus: '',
        applicant: '',
        applyDateRange: []
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      flowList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      flowStats: {
        pendingCount: 0,
        approvedCount: 0,
        rejectedCount: 0,
        totalCount: 0
      },
      
      // 审批对话框
      approvalDialogVisible: false,
      approvalDialogTitle: '',
      currentFlow: {},
      approvalForm: {
        approvalResult: 'APPROVED',
        approvalComment: '',
        nextApprover: '',
        attachments: []
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalComment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },
      
      // 批量审批对话框
      batchApprovalDialogVisible: false,
      batchApprovalForm: {
        approvalResult: 'APPROVED',
        approvalComment: ''
      },
      batchApprovalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalComment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },
      
      // 流程图对话框
      flowChartDialogVisible: false,
      currentFlowChart: null,
      
      // 审批历史对话框
      historyDialogVisible: false,
      approvalHistory: [],
      
      // 上传相关
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/accountant/ncv65/approval-flow/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
      },
      
      // 选项数据
      flowTypeOptions: [
        { value: 'BUDGET_PREPARATION', label: '预算编制' },
        { value: 'BUDGET_ADJUSTMENT', label: '预算调整' },
        { value: 'BUDGET_ALLOCATION', label: '预算分配' },
        { value: 'BUDGET_TRANSFER', label: '预算调拨' }
      ],
      approvalStatusOptions: [
        { value: 'PENDING', label: '待审批' },
        { value: 'APPROVED', label: '已批准' },
        { value: 'REJECTED', label: '已拒绝' },
        { value: 'RETURNED', label: '已退回' },
        { value: 'CANCELLED', label: '已取消' }
      ],
      approverOptions: [],

      detailDialogVisible: false,
      flowDetail: {},

      settingsDialogVisible: false,
      settingsForm: { autoApproval: false, allowWithdraw: true },

      // 创建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      isEdit: false,
      flowForm: {
        approvalId: '',
        flowName: '',
        approvalType: '',
        budgetId: '',
        budgetName: '',
        submitterName: '',
        budgetAmount: 0,
        currentNode: '',
        currentApproverName: '',
        priority: 'MEDIUM',
        dueDate: '',
        remark: ''
      },
      flowFormRules: {
        flowName: [
          { required: true, message: '请输入流程名称', trigger: 'blur' }
        ],
        approvalType: [
          { required: true, message: '请选择流程类型', trigger: 'change' }
        ],
        budgetId: [
          { required: true, message: '请输入预算ID', trigger: 'blur' }
        ]
      }
    }
  },

  computed: {
    flowChartSteps() {
      if (!this.currentFlowChart) return []
      const row = this.currentFlowChart
      const status = (row.approvalStatus || '').toUpperCase()
      const steps = [
        {
          name: '发起申请',
          icon: 'el-icon-edit-outline',
          detail: row.applicant || '申请人',
          time: row.applyTime || '',
          status: 'done'
        },
        {
          name: row.currentNode || '审批中',
          icon: 'el-icon-user',
          detail: row.currentApprover || '审批人',
          time: '',
          status: (status === 'APPROVED' || status === 'REJECTED') ? 'done' : 'active'
        },
        {
          name: '审批完成',
          icon: 'el-icon-circle-check',
          detail: status === 'APPROVED' ? '已批准' : status === 'REJECTED' ? '已拒绝' : '待完成',
          time: '',
          status: (status === 'APPROVED' || status === 'REJECTED') ? (status === 'APPROVED' ? 'done' : 'rejected') : 'pending'
        }
      ]
      return steps
    }
  },

  created() {
    this.getList()
    this.getFlowStats()
    this.loadApprovers()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await approvalFlowApi.getPage(params)
        if (response.code === 1) {
          this.flowList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 获取流程统计
    async getFlowStats() {
      try {
        const response = await approvalFlowApi.getStats()
        if (response.code === 1) {
          this.flowStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 加载审批人选项
    async loadApprovers() {
      try {
        const response = await approvalFlowApi.getApprovers()
        if (response.code === 1) {
          this.approverOptions = response.data
        }
      } catch (error) {
        console.error('加载审批人失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        flowName: '',
        flowType: '',
        approvalStatus: '',
        applicant: '',
        applyDateRange: []
      }
      this.handleQuery()
    },
    
    // 创建流程
    handleCreateFlow() {
      this.isEdit = false
      this.dialogTitle = '创建审批流程'
      this.resetFlowForm()
      this.dialogVisible = true
    },

    // 编辑流程
    handleEditFlow(row) {
      this.isEdit = true
      this.dialogTitle = '编辑审批流程'
      this.flowForm = {
        approvalId: row.flowId || row.approvalId,
        flowName: row.flowName || '',
        approvalType: row.flowType || '',
        budgetId: row.budgetId || '',
        budgetName: row.budgetName || '',
        submitterName: row.applicant || '',
        budgetAmount: row.budgetAmount || 0,
        currentNode: row.currentNode || '',
        currentApproverName: row.currentApprover || '',
        priority: row.priority || 'MEDIUM',
        dueDate: row.deadline || '',
        remark: row.description || ''
      }
      this.dialogVisible = true
    },

    // 删除流程
    handleDeleteFlow(row) {
      const id = row.flowId || row.approvalId
      this.$confirm('确认删除该审批流程吗？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await approvalFlowApi.delete(id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
            this.getFlowStats()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    // 提交创建/编辑表单
    async handleSubmitFlow() {
      try {
        await this.$refs.flowForm.validate()
        this.submitLoading = true
        let response
        if (this.isEdit) {
          response = await approvalFlowApi.update(this.flowForm)
        } else {
          response = await approvalFlowApi.create(this.flowForm)
        }
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '编辑成功' : '创建成功')
          this.dialogVisible = false
          this.getList()
          this.getFlowStats()
        } else {
          this.$message.error(response.msg || (this.isEdit ? '编辑失败' : '创建失败'))
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error((this.isEdit ? '编辑' : '创建') + '失败：' + (error.message || ''))
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 关闭创建/编辑对话框
    handleDialogClose() {
      this.resetFlowForm()
    },

    // 重置创建/编辑表单
    resetFlowForm() {
      this.flowForm = {
        approvalId: '',
        flowName: '',
        approvalType: '',
        budgetId: '',
        budgetName: '',
        submitterName: '',
        budgetAmount: 0,
        currentNode: '',
        currentApproverName: '',
        priority: 'MEDIUM',
        dueDate: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.flowForm && this.$refs.flowForm.clearValidate()
      })
    },

    // 查看流程
    handleViewFlow(row) {
      this.flowDetail = { ...row }
      this.detailDialogVisible = true
    },
    
    // 审批
    handleApprove(row) {
      this.approvalDialogTitle = '审批流程'
      this.approvalDialogVisible = true
      this.currentFlow = { ...row }
      this.resetApprovalForm()
    },
    
    // 拒绝
    handleReject(row) {
      this.approvalDialogTitle = '拒绝流程'
      this.approvalDialogVisible = true
      this.currentFlow = { ...row }
      this.approvalForm.approvalResult = 'REJECTED'
    },
    
    // 提交审批
    async handleSubmitApproval() {
      try {
        await this.$refs.approvalForm.validate()
        const params = {
          flowId: this.currentFlow.flowId,
          comment: this.approvalForm.approvalComment
        }
        let response
        if (this.approvalForm.approvalResult === 'APPROVED') {
          response = await approvalFlowApi.approve(params)
        } else if (this.approvalForm.approvalResult === 'REJECTED') {
          params.reason = this.approvalForm.approvalComment
          response = await approvalFlowApi.reject(params)
        }
        if (response && response.code === 1) {
          this.$message.success('审批提交成功')
          this.approvalDialogVisible = false
          this.getList()
          this.getFlowStats()
        }
      } catch (error) {
        this.$message.error('审批失败：' + error.message)
      }
    },
    
    // 批量审批
    handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的流程')
        return
      }
      this.batchApprovalDialogVisible = true
      this.batchApprovalForm.approvalResult = 'APPROVED'
    },
    
    // 批量拒绝
    handleBatchReject() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要拒绝的流程')
        return
      }
      this.batchApprovalDialogVisible = true
      this.batchApprovalForm.approvalResult = 'REJECTED'
    },
    
    // 提交批量审批
    async handleSubmitBatchApproval() {
      try {
        await this.$refs.batchApprovalForm.validate()
        const ids = this.selectedRows.map(row => row.flowId)
        const params = {
          ids,
          comment: this.batchApprovalForm.approvalComment
        }
        let response
        if (this.batchApprovalForm.approvalResult === 'APPROVED') {
          response = await approvalFlowApi.batchApprove(params)
        } else if (this.batchApprovalForm.approvalResult === 'REJECTED') {
          params.reason = this.batchApprovalForm.approvalComment
          response = await approvalFlowApi.batchReject(params)
        }
        if (response && response.code === 1) {
          this.$message.success('批量审批成功')
          this.batchApprovalDialogVisible = false
          this.getList()
          this.getFlowStats()
        }
      } catch (error) {
        this.$message.error('批量审批失败：' + error.message)
      }
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'flowChart':
          this.handleShowFlowChart(row)
          break
        case 'history':
          this.handleShowHistory(row)
          break
        case 'delegate':
          this.handleDelegate(row)
          break
        case 'recall':
          this.handleRecall(row)
          break
        case 'urge':
          this.handleUrge(row)
          break
      }
    },
    
    // 显示流程图
    handleShowFlowChart(row) {
      this.currentFlowChart = { ...row }
      this.flowChartDialogVisible = true
    },
    
    // 显示审批历史
    async handleShowHistory(row) {
      try {
        const response = await approvalFlowApi.getHistory(row.flowId)
        this.approvalHistory = response.data
        this.historyDialogVisible = true
      } catch (error) {
        this.$message.error('获取审批历史失败：' + error.message)
      }
    },
    
    // 委派
    async handleDelegate(row) {
      try {
        const { value } = await this.$prompt('请选择委派给的人员', '委派流程', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        
        await approvalFlowApi.delegate({
          flowId: row.flowId,
          delegateTo: value
        })
        this.$message.success('委派成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('委派失败：' + error.message)
        }
      }
    },
    
    // 撤回
    async handleRecall(row) {
      try {
        await this.$confirm('确认撤回该流程吗？', '提示', {
          type: 'warning'
        })
        await approvalFlowApi.recall(row.flowId)
        this.$message.success('撤回成功')
        this.getList()
        this.getFlowStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('撤回失败：' + error.message)
        }
      }
    },
    
    // 催办
    async handleUrge(row) {
      try {
        await approvalFlowApi.urge(row.flowId)
        this.$message.success('催办消息已发送')
      } catch (error) {
        this.$message.error('催办失败：' + error.message)
      }
    },
    
    // 导出流程
    async handleExportFlow() {
      try {
        this.$message.info('正在导出，请稍候...')
        const params = { ...this.queryForm }
        const response = await approvalFlowApi.export(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '审批流程_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || ''))
      }
    },
    
    // 流程设置
    handleFlowSettings() {
      this.settingsDialogVisible = true
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getFlowStats()
    },
    
    // 重置审批表单
    resetApprovalForm() {
      this.approvalForm = {
        approvalResult: 'APPROVED',
        approvalComment: '',
        nextApprover: '',
        attachments: []
      }
      this.fileList = []
      this.$nextTick(() => {
        this.$refs.approvalForm && this.$refs.approvalForm.clearValidate()
      })
    },
    
    // 关闭审批对话框
    handleApprovalDialogClose() {
      this.resetApprovalForm()
    },
    
    // 上传成功
    handleUploadSuccess(response, file) {
      if (response.code === 1) {
        this.approvalForm.attachments.push(response.data)
      } else {
        this.$message.error('上传失败：' + response.message)
      }
    },
    
    // 移除文件
    handleUploadRemove(file) {
      const index = this.approvalForm.attachments.findIndex(item => item.fileName === file.name)
      if (index > -1) {
        this.approvalForm.attachments.splice(index, 1)
      }
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 判断是否可以审批
    canApprove(row) {
      return row.approvalStatus === 'PENDING' && row.currentApprover === this.$store.getters.name
    },
    
    // 判断是否可以拒绝
    canReject(row) {
      return row.approvalStatus === 'PENDING' && row.currentApprover === this.$store.getters.name
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取流程类型颜色
    getFlowTypeColor(type) {
      const colorMap = {
        'BUDGET_PREPARATION': 'primary',
        'BUDGET_ADJUSTMENT': 'warning',
        'BUDGET_ALLOCATION': 'success',
        'BUDGET_TRANSFER': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取流程类型文本
    getFlowTypeText(type) {
      const textMap = {
        'BUDGET_PREPARATION': '预算编制',
        'BUDGET_ADJUSTMENT': '预算调整',
        'BUDGET_ALLOCATION': '预算分配',
        'BUDGET_TRANSFER': '预算调拨'
      }
      return textMap[type] || type
    },
    
    // 获取审批状态类型
    getApprovalStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'RETURNED': 'info',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取审批状态文本
    getApprovalStatusText(status) {
      const statusMap = {
        'PENDING': '待审批',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝',
        'RETURNED': '已退回',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    
    // 获取优先级类型
    getPriorityType(priority) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[priority] || 'info'
    },
    
    // 获取优先级文本
    getPriorityText(priority) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[priority] || priority
    },
    
    // 获取截止时间样式
    getDeadlineClass(deadline) {
      if (!deadline) return ''
      const now = new Date()
      const deadlineDate = new Date(deadline)
      const diff = deadlineDate.getTime() - now.getTime()
      const days = Math.ceil(diff / (1000 * 60 * 60 * 24))
      
      if (days < 0) return 'overdue-text'
      if (days <= 1) return 'urgent-text'
      if (days <= 3) return 'warning-text'
      return ''
    },
    
    // 获取历史类型
    getHistoryType(result) {
      const typeMap = {
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'RETURNED': 'warning',
        'PENDING': 'primary'
      }
      return typeMap[result] || 'primary'
    },
    
    // 获取历史标签类型
    getHistoryTagType(result) {
      const typeMap = {
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'RETURNED': 'warning',
        'PENDING': 'primary'
      }
      return typeMap[result] || 'primary'
    },
    
    // 获取历史结果文本
    getHistoryResultText(result) {
      const textMap = {
        'APPROVED': '同意',
        'REJECTED': '拒绝',
        'RETURNED': '退回',
        'PENDING': '待处理'
      }
      return textMap[result] || result
    }
  }
}
</script>

<style lang="scss" scoped>
.approval-flow {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }
    
    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
  
  .toolbar-card,
  .search-card,
  .table-card {
    margin-bottom: 20px;
  }
  
  .stats-row {
    margin-bottom: 20px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      
      &.pending-card {
        background: linear-gradient(135deg, #FFA726, #FF7043);
        color: white;
      }
      
      &.approved-card {
        background: linear-gradient(135deg, #66BB6A, #43A047);
        color: white;
      }
      
      &.rejected-card {
        background: linear-gradient(135deg, #EF5350, #E53935);
        color: white;
      }
      
      &.total-card {
        background: linear-gradient(135deg, #42A5F5, #1E88E5);
        color: white;
      }
      
      .stat-content {
        position: relative;
        z-index: 2;
        
        .stat-number {
          font-size: 32px;
          font-weight: 600;
          margin-bottom: 8px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
        }
      }
      
      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      gap: 8px;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .overdue-text {
    color: #F56C6C;
    font-weight: 500;
  }
  
  .urgent-text {
    color: #E6A23C;
    font-weight: 500;
  }
  
  .warning-text {
    color: #E6A23C;
  }
  
  .text-right {
    text-align: right;
  }
  
  .approval-content {
    .flow-info {
      margin-bottom: 24px;
      
      h4 {
        color: #303133;
        margin: 0 0 16px 0;
      }
    }
    
    .approval-form {
      h4 {
        color: #303133;
        margin: 0 0 16px 0;
      }
    }
  }
  
  .batch-approval-content {
    p {
      margin-bottom: 20px;
      color: #606266;
    }
  }
  
  .flow-chart-container {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    padding: 24px;

    .flow-chart-info {
      display: flex;
      gap: 24px;
      margin-bottom: 30px;
      font-size: 14px;
      color: #606266;
    }

    .flow-chart-steps {
      display: flex;
      align-items: flex-start;
      justify-content: center;
      padding: 20px 0;
    }

    .flow-step-wrapper {
      display: flex;
      align-items: center;
    }

    .flow-step {
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 140px;
      padding: 16px 12px;
      border-radius: 8px;
      background: #f5f7fa;
      border: 2px solid #dcdfe6;
      transition: all 0.3s;

      .flow-step-icon {
        width: 44px;
        height: 44px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        color: #fff;
        background: #c0c4cc;
        margin-bottom: 8px;
      }

      .flow-step-label {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .flow-step-detail {
        font-size: 12px;
        color: #909399;
      }

      .flow-step-time {
        font-size: 11px;
        color: #c0c4cc;
        margin-top: 4px;
      }

      &.flow-step--done {
        border-color: #67c23a;
        background: #f0f9eb;
        .flow-step-icon { background: #67c23a; }
      }

      &.flow-step--active {
        border-color: #409eff;
        background: #ecf5ff;
        .flow-step-icon { background: #409eff; }
      }

      &.flow-step--rejected {
        border-color: #f56c6c;
        background: #fef0f0;
        .flow-step-icon { background: #f56c6c; }
      }

      &.flow-step--pending {
        border-color: #dcdfe6;
        background: #f5f7fa;
        .flow-step-icon { background: #c0c4cc; }
      }
    }

    .flow-step-arrow {
      font-size: 24px;
      color: #c0c4cc;
      margin: 0 12px;
      padding-bottom: 30px;
    }
  }
  
  .history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .history-node {
      font-weight: 500;
      color: #303133;
    }
  }
  
  .history-content {
    p {
      margin: 8px 0;
      color: #606266;
      font-size: 14px;
    }
  }
}
</style>
