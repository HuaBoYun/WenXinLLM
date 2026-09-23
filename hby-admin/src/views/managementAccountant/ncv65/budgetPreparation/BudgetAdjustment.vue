<template>
  <div class="budget-adjustment">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算调整管理</h2>
      <p>管理预算调整申请，支持预算增加、减少、科目间调拨等多种调整类型</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAdjustment">新增调整</el-button>
            <el-button type="success" icon="el-icon-check" @click="handleBatchApprove">批量审批</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
            <el-button type="info" icon="el-icon-document-copy" @click="handleTemplate">下载模板</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card pending-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ adjustmentStats.pendingCount }}</div>
            <div class="stat-label">待审批</div>
            <div class="stat-amount">{{ formatAmount(adjustmentStats.pendingAmount) }}</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card approved-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ adjustmentStats.approvedCount }}</div>
            <div class="stat-label">已批准</div>
            <div class="stat-amount">{{ formatAmount(adjustmentStats.approvedAmount) }}</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card rejected-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ adjustmentStats.rejectedCount }}</div>
            <div class="stat-label">已拒绝</div>
            <div class="stat-amount">{{ formatAmount(adjustmentStats.rejectedAmount) }}</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-close"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ adjustmentStats.totalCount }}</div>
            <div class="stat-label">总计</div>
            <div class="stat-amount">{{ formatAmount(adjustmentStats.totalAmount) }}</div>
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
        <el-form-item label="调整编号">
          <el-input
            v-model="queryForm.adjustmentCode"
            placeholder="请输入调整编号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-select
            v-model="queryForm.adjustmentType"
            placeholder="请选择调整类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in adjustmentTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="调整状态">
          <el-select
            v-model="queryForm.adjustmentStatus"
            placeholder="请选择调整状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in adjustmentStatusOptions"
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
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="组织机构">
          <el-cascader
            v-model="queryForm.organizationId"
            :options="organizationOptions"
            :props="{ checkStrictly: true, value: 'id', label: 'name' }"
            placeholder="请选择组织机构"
            clearable
            style="width: 200px"
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

    <!-- 调整列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算调整列表</span>
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
        :data="adjustmentList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="adjustmentCode" label="调整编号" width="150" show-overflow-tooltip />
        
        <el-table-column prop="adjustmentType" label="调整类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAdjustmentTypeColor(scope.row.adjustmentType)" size="mini">
              {{ getAdjustmentTypeText(scope.row.adjustmentType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="adjustmentReason" label="调整原因" width="200" show-overflow-tooltip />
        
        <el-table-column prop="organizationName" label="组织机构" width="150" show-overflow-tooltip />
        
        <el-table-column prop="adjustmentAmount" label="调整金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getAmountClass(scope.row.adjustmentAmount)">
              {{ formatAmount(scope.row.adjustmentAmount) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column prop="adjustmentStatus" label="调整状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAdjustmentStatusType(scope.row.adjustmentStatus)" size="mini">
              {{ getAdjustmentStatusText(scope.row.adjustmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="applicantName" label="申请人" width="100" show-overflow-tooltip />
        <el-table-column label="申请时间" width="170" align="center">
          <template slot-scope="scope">
            {{ scope.row.applyDate ? new Date(scope.row.applyDate).toLocaleString('zh-CN') : '' }}
          </template>
        </el-table-column>

        <el-table-column prop="approverName" label="当前审批人" width="120" show-overflow-tooltip />
        
        <el-table-column label="操作" width="260" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canApprove(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click="handleApprove(scope.row)"
            >审批</el-button>
            <el-button
              v-if="scope.row.adjustmentStatus === 'APPROVED'"
              type="text"
              size="mini"
              icon="el-icon-video-play"
              class="success-text"
              @click="handleExecute(scope.row)"
            >执行</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="cancel" icon="el-icon-close">取消</el-dropdown-item>
                <el-dropdown-item command="print" icon="el-icon-printer">打印</el-dropdown-item>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="adjustmentForm"
        :model="adjustmentForm"
        :rules="adjustmentRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调整类型" prop="adjustmentType">
              <el-select
                v-model="adjustmentForm.adjustmentType"
                placeholder="请选择调整类型"
                style="width: 100%"
                @change="handleAdjustmentTypeChange"
              >
                <el-option
                  v-for="item in adjustmentTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织机构" prop="organizationId">
              <el-cascader
                v-model="adjustmentForm.organizationId"
                :options="organizationOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                placeholder="请选择组织机构"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算年度" prop="fiscalYear">
              <el-date-picker
                v-model="adjustmentForm.fiscalYear"
                type="year"
                placeholder="选择预算年度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算期间" prop="budgetPeriod">
              <el-select
                v-model="adjustmentForm.budgetPeriod"
                placeholder="请选择预算期间"
                style="width: 100%"
              >
                <el-option
                  v-for="item in budgetPeriodOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="调整原因" prop="adjustmentReason">
          <el-input
            v-model="adjustmentForm.adjustmentReason"
            placeholder="请输入调整原因"
          />
        </el-form-item>
        
        <!-- 调整明细 -->
        <el-form-item label="调整明细" prop="adjustmentDetails">
          <div class="adjustment-details">
            <div class="details-header">
              <el-button type="primary" size="mini" @click="handleAddDetail">添加明细</el-button>
              <el-button type="danger" size="mini" @click="handleRemoveSelectedDetails">删除选中</el-button>
            </div>
            
            <el-table
              :data="adjustmentForm.adjustmentDetails"
              border
              size="mini"
              @selection-change="handleDetailSelectionChange"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="预算科目" width="200">
                <template slot-scope="scope">
                  <el-cascader
                    v-model="scope.row.budgetAccountId"
                    :options="budgetAccountOptions"
                    :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                    placeholder="请选择预算科目"
                    size="mini"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="调整前金额" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.beforeAmount"
                    placeholder="调整前金额"
                    size="mini"
                    type="number"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="调整金额" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.adjustmentAmount"
                    placeholder="调整金额"
                    size="mini"
                    type="number"
                    @input="calculateAfterAmount(scope.row)"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="调整后金额" width="120">
                <template slot-scope="scope">
                  <span class="amount-text">{{ formatAmount(scope.row.afterAmount) }}</span>
                </template>
              </el-table-column>
              
              <el-table-column label="备注" min-width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remark"
                    placeholder="请输入备注"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveDetail(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
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
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="adjustmentForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handleSubmitForm">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 调整详情对话框 -->
    <el-dialog title="调整详情" :visible.sync="detailDialogVisible" width="750px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="调整编号">{{ adjustmentDetail.adjustmentCode }}</el-descriptions-item>
        <el-descriptions-item label="调整类型">
          <el-tag :type="getAdjustmentTypeColor(adjustmentDetail.adjustmentType)" size="mini">
            {{ getAdjustmentTypeText(adjustmentDetail.adjustmentType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="组织机构">{{ adjustmentDetail.organizationName || adjustmentDetail.organizationId }}</el-descriptions-item>
        <el-descriptions-item label="预算年度">{{ adjustmentDetail.budgetYear }}</el-descriptions-item>
        <el-descriptions-item label="预算期间">{{ adjustmentDetail.budgetPeriod }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getAdjustmentStatusType(adjustmentDetail.adjustmentStatus)" size="mini">
            {{ getAdjustmentStatusText(adjustmentDetail.adjustmentStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="调整金额">
          <span :class="getAmountClass(adjustmentDetail.adjustmentAmount)" style="font-weight:bold">
            {{ formatAmount(adjustmentDetail.adjustmentAmount) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="调整前金额">{{ formatAmount(adjustmentDetail.originalAmount) }}</el-descriptions-item>
        <el-descriptions-item label="调整后金额">{{ formatAmount(adjustmentDetail.adjustedAmount) }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ adjustmentDetail.applicantName || adjustmentDetail.applicant }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ adjustmentDetail.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ adjustmentDetail.approverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批日期">{{ adjustmentDetail.approveDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批意见">{{ adjustmentDetail.approveComment || '-' }}</el-descriptions-item>
        <el-descriptions-item label="调整原因" :span="2">{{ adjustmentDetail.adjustmentReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ adjustmentDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 调整设置对话框 -->
    <el-dialog title="调整设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="允许超预算调整">
          <el-switch v-model="settingsForm.allowOverBudget" />
        </el-form-item>
        <el-form-item label="需要审批流程">
          <el-switch v-model="settingsForm.requireApproval" />
        </el-form-item>
        <el-form-item label="调整上限比例(%)">
          <el-input-number v-model="settingsForm.maxAdjustRate" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 审批弹窗 -->
    <el-dialog title="审批预算调整" :visible.sync="approveDialogVisible" width="750px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="调整编号">{{ approveRow.adjustmentCode }}</el-descriptions-item>
        <el-descriptions-item label="调整类型">
          <el-tag :type="getAdjustmentTypeColor(approveRow.adjustmentType)" size="mini">
            {{ getAdjustmentTypeText(approveRow.adjustmentType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="组织机构">{{ approveRow.organizationName || approveRow.organizationId }}</el-descriptions-item>
        <el-descriptions-item label="预算年度">{{ approveRow.budgetYear }}</el-descriptions-item>
        <el-descriptions-item label="预算期间">{{ approveRow.budgetPeriod }}</el-descriptions-item>
        <el-descriptions-item label="调整金额">
          <span :class="getAmountClass(approveRow.adjustmentAmount)">{{ formatAmount(approveRow.adjustmentAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">{{ approveRow.applicantName || approveRow.applicant }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ approveRow.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getAdjustmentStatusType(approveRow.adjustmentStatus)" size="mini">
            {{ getAdjustmentStatusText(approveRow.adjustmentStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ approveRow.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="调整原因" :span="2">{{ approveRow.adjustmentReason || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-form label-width="80px" size="small" style="margin-top: 16px">
        <el-form-item label="审批意见">
          <el-input
            v-model="approveComment"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见（驳回时必填）"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleReject">驳回</el-button>
        <el-button type="success" @click="handleApproveConfirm">通过</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { budgetAdjustmentApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetAdjustment',
  data() {
    return {
      // 查询参数
      queryForm: {
        adjustmentCode: '',
        adjustmentType: '',
        adjustmentStatus: '',
        applicant: '',
        organizationId: [],
        applyDateRange: []
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      adjustmentList: [],
      total: 0,
      selectedRows: [],
      selectedDetails: [],
      
      // 统计数据
      adjustmentStats: {
        pendingCount: 0,
        pendingAmount: 0,
        approvedCount: 0,
        approvedAmount: 0,
        rejectedCount: 0,
        rejectedAmount: 0,
        totalCount: 0,
        totalAmount: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      adjustmentForm: {
        id: null,
        adjustmentType: '',
        organizationId: [],
        fiscalYear: null,
        budgetPeriod: '',
        adjustmentReason: '',
        adjustmentDetails: [],
        attachments: [],
        remark: ''
      },
      adjustmentRules: {
        adjustmentType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        organizationId: [
          { required: true, message: '请选择组织机构', trigger: 'change' }
        ],
        fiscalYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        budgetPeriod: [
          { required: true, message: '请选择预算期间', trigger: 'change' }
        ],
        adjustmentReason: [
          { required: true, message: '请输入调整原因', trigger: 'blur' }
        ],
        adjustmentDetails: [
          { required: true, message: '请添加调整明细', trigger: 'change' }
        ]
      },
      
      // 上传相关
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/accountant/ncv65/budget-adjustment/upload',
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
      },
      
      // 选项数据
      adjustmentTypeOptions: [
        { value: 'INCREASE', label: '预算增加' },
        { value: 'DECREASE', label: '预算减少' },
        { value: 'TRANSFER', label: '科目调拨' },
        { value: 'REALLOCATION', label: '重新分配' }
      ],
      adjustmentStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'SUBMITTED', label: '已提交' },
        { value: 'APPROVED', label: '已批准' },
        { value: 'REJECTED', label: '已拒绝' },
        { value: 'CANCELLED', label: '已取消' }
      ],
      budgetPeriodOptions: [
        { value: 'Q1', label: '第一季度' },
        { value: 'Q2', label: '第二季度' },
        { value: 'Q3', label: '第三季度' },
        { value: 'Q4', label: '第四季度' },
        { value: 'M01', label: '1月' },
        { value: 'M02', label: '2月' },
        { value: 'M03', label: '3月' },
        { value: 'M04', label: '4月' },
        { value: 'M05', label: '5月' },
        { value: 'M06', label: '6月' },
        { value: 'M07', label: '7月' },
        { value: 'M08', label: '8月' },
        { value: 'M09', label: '9月' },
        { value: 'M10', label: '10月' },
        { value: 'M11', label: '11月' },
        { value: 'M12', label: '12月' }
      ],
      organizationOptions: [],
      budgetAccountOptions: [],

      detailDialogVisible: false,
      adjustmentDetail: {},

      settingsDialogVisible: false,
      settingsForm: {
        allowOverBudget: false,
        requireApproval: true,
        maxAdjustRate: 30
      },

      approveDialogVisible: false,
      approveRow: {},
      approveComment: '',

      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyRow: {}
    }
  },
  
  created() {
    this.getList()
    this.getAdjustmentStats()
    this.loadOrganizations()
    this.loadBudgetAccounts()
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
        const response = await budgetAdjustmentApi.getPage(params)
        this.adjustmentList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 获取统计数据
    async getAdjustmentStats() {
      try {
        const response = await budgetAdjustmentApi.getStats()
        if (response.code === 1 && response.data) {
          this.adjustmentStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 加载组织机构选项
    async loadOrganizations() {
      try {
        const response = await budgetAdjustmentApi.getOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织机构失败：', error)
      }
    },
    
    // 加载预算科目选项
    async loadBudgetAccounts() {
      try {
        const response = await budgetAdjustmentApi.getBudgetAccounts()
        this.budgetAccountOptions = response.data
      } catch (error) {
        console.error('加载预算科目失败：', error)
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
        adjustmentCode: '',
        adjustmentType: '',
        adjustmentStatus: '',
        applicant: '',
        organizationId: [],
        applyDateRange: []
      }
      this.handleQuery()
    },
    
    // 新增调整
    handleCreateAdjustment() {
      this.dialogTitle = '新增预算调整'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 将后端行数据映射为前端表单模型
    mapRowToForm(row) {
      return {
        id: row.adjustmentId || null,
        adjustmentType: row.adjustmentType || '',
        organizationId: row.organizationId ? [row.organizationId] : [],
        fiscalYear: row.budgetYear ? new Date(row.budgetYear, 0, 1) : null,
        budgetPeriod: row.budgetPeriod || '',
        adjustmentReason: row.adjustmentReason || '',
        adjustmentDetails: [],
        attachments: [],
        remark: row.remark || ''
      }
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算调整'
      this.adjustmentForm = this.mapRowToForm(row)
      this.dialogVisible = true
    },
    
    // 查看
    handleView(row) {
      this.adjustmentDetail = { ...row }
      this.detailDialogVisible = true
    },

    // 审批
    handleApprove(row) {
      this.approveRow = { ...row }
      this.approveComment = ''
      this.approveDialogVisible = true
    },

    // 审批通过
    async handleApproveConfirm() {
      try {
        await budgetAdjustmentApi.approve({
          adjustmentId: this.approveRow.adjustmentId,
          action: 'approve',
          comment: this.approveComment
        })
        this.$message.success('审批通过')
        this.approveDialogVisible = false
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        this.$message.error('审批失败：' + error.message)
      }
    },

    // 审批驳回
    async handleReject() {
      if (!this.approveComment) {
        this.$message.warning('驳回时请填写审批意见')
        return
      }
      try {
        await budgetAdjustmentApi.reject(this.approveRow.adjustmentId, {
          reason: this.approveComment
        })
        this.$message.success('已驳回')
        this.approveDialogVisible = false
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        this.$message.error('驳回失败：' + error.message)
      }
    },
    
    // 调整类型改变
    handleAdjustmentTypeChange(value) {
      // 根据调整类型初始化明细
      if (value && this.adjustmentForm.adjustmentDetails.length === 0) {
        this.handleAddDetail()
      }
    },
    
    // 添加明细
    handleAddDetail() {
      this.adjustmentForm.adjustmentDetails.push({
        budgetAccountId: [],
        beforeAmount: 0,
        adjustmentAmount: 0,
        afterAmount: 0,
        remark: ''
      })
    },
    
    // 删除明细
    handleRemoveDetail(index) {
      this.adjustmentForm.adjustmentDetails.splice(index, 1)
    },
    
    // 删除选中明细
    handleRemoveSelectedDetails() {
      if (this.selectedDetails.length === 0) {
        this.$message.warning('请选择要删除的明细')
        return
      }
      
      // 从后往前删除，避免索引变化
      const indices = this.selectedDetails.map(detail => 
        this.adjustmentForm.adjustmentDetails.indexOf(detail)
      ).sort((a, b) => b - a)
      
      indices.forEach(index => {
        this.adjustmentForm.adjustmentDetails.splice(index, 1)
      })
      
      this.selectedDetails = []
    },
    
    // 计算调整后金额
    calculateAfterAmount(row) {
      const beforeAmount = parseFloat(row.beforeAmount) || 0
      const adjustmentAmount = parseFloat(row.adjustmentAmount) || 0
      row.afterAmount = beforeAmount + adjustmentAmount
    },
    
    // 明细选择改变
    handleDetailSelectionChange(selection) {
      this.selectedDetails = selection
    },
    
    // 保存草稿
    // 构建后端请求参数，将前端表单字段映射为后端实体字段
    buildRequestParams(status) {
      const form = this.adjustmentForm
      // organizationId: 级联选择器返回数组，取最后一个值
      const orgId = Array.isArray(form.organizationId)
        ? form.organizationId[form.organizationId.length - 1]
        : form.organizationId
      // fiscalYear: 日期对象 → 提取年份整数作为 budgetYear
      let budgetYear = null
      if (form.fiscalYear) {
        budgetYear = new Date(form.fiscalYear).getFullYear()
      }
      // adjustmentAmount: 从明细行汇总
      let adjustmentAmount = 0
      if (form.adjustmentDetails && form.adjustmentDetails.length > 0) {
        adjustmentAmount = form.adjustmentDetails.reduce((sum, item) => {
          return sum + (parseFloat(item.adjustmentAmount) || 0)
        }, 0)
      }
      return {
        adjustmentId: form.id || null,
        adjustmentType: form.adjustmentType,
        organizationId: orgId,
        budgetYear: budgetYear,
        budgetPeriod: form.budgetPeriod,
        adjustmentReason: form.adjustmentReason,
        adjustmentAmount: adjustmentAmount,
        adjustmentStatus: status,
        remark: form.remark
      }
    },

    async handleSaveDraft() {
      try {
        const params = this.buildRequestParams('DRAFT')
        if (this.adjustmentForm.id) {
          await budgetAdjustmentApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetAdjustmentApi.create(params)
          this.$message.success('创建成功')
        }
        this.dialogVisible = false
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.adjustmentForm.validate()

        if (this.adjustmentForm.adjustmentDetails.length === 0) {
          this.$message.warning('请添加调整明细')
          return
        }

        const params = this.buildRequestParams('SUBMITTED')

        if (this.adjustmentForm.id) {
          await budgetAdjustmentApi.update(params)
          this.$message.success('提交成功')
        } else {
          await budgetAdjustmentApi.create(params)
          this.$message.success('创建并提交成功')
        }

        this.dialogVisible = false
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.adjustmentForm = {
        id: null,
        adjustmentType: '',
        organizationId: [],
        fiscalYear: null,
        budgetPeriod: '',
        adjustmentReason: '',
        adjustmentDetails: [],
        attachments: [],
        remark: ''
      }
      this.fileList = []
      this.$nextTick(() => {
        this.$refs.adjustmentForm && this.$refs.adjustmentForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 批量审批
    async handleBatchApprove() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审批的调整')
        return
      }
      
      try {
        await this.$confirm('确认批量审批选中的调整吗？', '提示', {
          type: 'warning'
        })
        const ids = this.selectedRows.map(row => row.adjustmentId)
        await budgetAdjustmentApi.batchApprove(ids)
        this.$message.success('批量审批成功')
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量审批失败：' + error.message)
        }
      }
    },
    
    // 导出数据
    async handleExport() {
      try {
        const params = { ...this.queryForm }
        const res = await budgetAdjustmentApi.export(params)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算调整数据_' + new Date().toLocaleDateString('zh-CN').replace(/\//g, '') + '.xlsx'
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 下载模板
    async handleTemplate() {
      try {
        const res = await budgetAdjustmentApi.downloadTemplate()
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算调整模板.xlsx'
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getAdjustmentStats()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
        case 'print':
          this.handlePrint(row)
          break
      }
    },
    
    // 执行调整
    async handleExecute(row) {
      try {
        await this.$confirm('确认执行该调整吗？执行后将正式生效。', '提示', { type: 'warning' })
        await budgetAdjustmentApi.execute(row.adjustmentId)
        this.$message.success('执行成功')
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('执行失败：' + error.message)
        }
      }
    },

    // 删除调整
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该调整申请吗？', '提示', { type: 'warning' })
        await budgetAdjustmentApi.delete(row.adjustmentId)
        this.$message.success('删除成功')
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算调整'
      const form = this.mapRowToForm(row)
      form.id = null
      this.adjustmentForm = form
      this.dialogVisible = true
    },
    
    // 历史记录
    async handleHistory(row) {
      this.historyRow = row
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        this.historyList = [
          { operateTime: row.createTime, operateType: '创建', operator: row.applicant || '系统', remark: '创建调整单' },
          { operateTime: row.updateTime || row.createTime, operateType: '更新', operator: row.applicant || '管理员', remark: '更新调整信息' }
        ]
      } finally {
        this.historyLoading = false
      }
    },
    
    // 取消
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该调整吗？', '提示', {
          type: 'warning'
        })
        await budgetAdjustmentApi.cancel(row.adjustmentId)
        this.$message.success('取消成功')
        this.getList()
        this.getAdjustmentStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消失败：' + error.message)
        }
      }
    },
    
    // 打印
    handlePrint(row) {
      window.open(`/print/budgetAdjustment/${row.adjustmentId}`, '_blank')
    },
    
    // 上传成功
    handleUploadSuccess(response, file) {
      if (response.code === 1) {
        this.adjustmentForm.attachments.push(response.data)
      } else {
        this.$message.error('上传失败：' + response.message)
      }
    },
    
    // 移除文件
    handleUploadRemove(file) {
      const index = this.adjustmentForm.attachments.findIndex(item => item.fileName === file.name)
      if (index > -1) {
        this.adjustmentForm.attachments.splice(index, 1)
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
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.adjustmentStatus === 'DRAFT' && row.applicant === this.$store.getters.name
    },
    
    // 判断是否可以审批
    canApprove(row) {
      return row.adjustmentStatus === 'SUBMITTED' && row.currentApprover === this.$store.getters.name
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取调整类型颜色
    getAdjustmentTypeColor(type) {
      const colorMap = {
        'INCREASE': 'success',
        'DECREASE': 'danger',
        'TRANSFER': 'warning',
        'REALLOCATION': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取调整类型文本
    getAdjustmentTypeText(type) {
      const textMap = {
        'INCREASE': '预算增加',
        'DECREASE': '预算减少',
        'TRANSFER': '科目调拨',
        'REALLOCATION': '重新分配'
      }
      return textMap[type] || type
    },
    
    // 获取调整状态类型
    getAdjustmentStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取调整状态文本
    getAdjustmentStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    
    // 获取金额样式
    getAmountClass(amount) {
      if (!amount) return ''
      return parseFloat(amount) >= 0 ? 'success-text' : 'danger-text'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-adjustment {
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
          font-size: 28px;
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 4px;
        }
        
        .stat-amount {
          font-size: 12px;
          opacity: 0.8;
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
  
  .adjustment-details {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .details-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
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
  
  .text-right {
    text-align: right;
  }
}
</style>
