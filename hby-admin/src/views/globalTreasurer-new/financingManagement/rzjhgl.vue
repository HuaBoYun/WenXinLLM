<template>
  <div class="financing-plan-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-add"></i>
            融资计划管理
          </h2>
          <p class="page-description">企业融资计划的制定、审批和执行管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增融资计划
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 融资计划概览卡片 -->
    <div class="plan-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">融资计划总数</div>
                <div class="card-value">{{ totalPlans }}</div>
                <div class="card-change">个</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon executing-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">执行中计划</div>
                <div class="card-value">{{ executingPlans }}</div>
                <div class="card-change positive">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">计划融资总额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon completion-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-info">
                <div class="card-title">完成率</div>
                <div class="card-value">{{ completionRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 融资计划分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资类型分布</h3>
            <div class="chart-controls">
              <el-radio-group v-model="typeChartType" size="small" @change="handleTypeChartTypeChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="financingTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>融资计划执行趋势</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="6M">6个月</el-radio-button>
                <el-radio-button label="1Y">1年</el-radio-button>
                <el-radio-button label="2Y">2年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="planTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="计划编号">
            <el-input
              v-model="listQuery.planNo"
              placeholder="请输入计划编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="融资类型">
            <el-select
              v-model="listQuery.financingType"
              placeholder="请选择融资类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="银行贷款" value="BANK_LOAN" />
              <el-option label="债券发行" value="BOND" />
              <el-option label="股权融资" value="EQUITY" />
              <el-option label="融资租赁" value="LEASING" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="计划状态">
            <el-select
              v-model="listQuery.planStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="待审批" value="PENDING" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="计划日期">
            <el-date-picker
              v-model="listQuery.planDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 融资计划表格 -->
    <el-card class="table-card" shadow="never">
      <!-- 表格工具栏 -->
      <div class="table-toolbar" style="margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
        <div class="toolbar-left">
          <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">
            批量删除
          </el-button>
          <span v-if="multipleSelection.length > 0" style="margin-left: 10px; color: #909399; font-size: 12px;">
            已选择 {{ multipleSelection.length }} 条
          </span>
        </div>
        <div class="toolbar-right">
          <el-button size="small" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
          <el-dropdown trigger="click" @command="handleTableSetting">
            <el-button size="small" icon="el-icon-setting">设置</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="density-default">默认密度</el-dropdown-item>
              <el-dropdown-item command="density-medium">中等密度</el-dropdown-item>
              <el-dropdown-item command="density-small">紧凑密度</el-dropdown-item>
              <el-dropdown-item divided command="column-setting">列设置</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <el-table
        :data="planList"
        border
        fit
        highlight-current-row
        :size="tableDensity === 'default' ? '' : tableDensity"
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column v-if="isColumnVisible('planNo')" label="计划编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.planNo }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('planName')" label="计划名称" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.planName }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('planType')" label="融资类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFinancingTypeTagType(row.planType)" size="mini">
              {{ getFinancingTypeText(row.planType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('planYear')" label="计划年度" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.planYear }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('planAmount')" label="计划金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="plan-amount">{{ formatCurrency(row.planAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('currencyCode')" label="币种" width="80px" align="center">
          <template slot-scope="{row}">
            <span>{{ getCurrencyText(row.currencyCode) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('startDate')" label="计划开始日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.startDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('endDate')" label="计划结束日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.endDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('planStatus')" label="计划状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getPlanStatusTagType(row.planStatus)" size="mini">
              {{ getPlanStatusText(row.planStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('companyName')" label="所属公司" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.companyName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('createdTime')" label="创建时间" width="160px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateTime(row.createdTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="isColumnVisible('createdBy')" label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createdBy }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.planStatus === 'DRAFT'" type="primary" size="mini" @click="handleSubmit(row)">
              提交
            </el-button>
            <el-button v-if="row.planStatus === 'PENDING'" type="success" size="mini" @click="handleApprove(row)">
              审批
            </el-button>
            <el-button v-if="row.planStatus === 'APPROVED'" type="warning" size="mini" @click="handleExecute(row)">
              执行
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: row}">取消</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">历史记录</el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: row}" divided style="color: #F56C6C;">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 融资计划创建/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="900px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计划编号" prop="planNo">
                  <el-input v-model="temp.planNo" placeholder="系统自动生成" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划名称" prop="planName">
                  <el-input v-model="temp.planName" placeholder="请输入计划名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计划类型" prop="planType">
                  <el-select v-model="temp.planType" placeholder="请选择计划类型" style="width: 100%;">
                    <el-option label="年度计划" value="1" />
                    <el-option label="季度计划" value="2" />
                    <el-option label="月度计划" value="3" />
                    <el-option label="临时计划" value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划年度" prop="planYear">
                  <el-input-number v-model="temp.planYear" :min="2020" :max="2050" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计划金额" prop="planAmount">
                  <el-input-number
                    v-model="temp.planAmount"
                    :precision="2"
                    :step="1000000"
                    :min="0"
                    :max="10000000000"
                    style="width: 100%;"
                    placeholder="请输入计划金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种">
                  <el-select v-model="temp.currencyCode" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计划开始日期" prop="startDate">
                  <el-date-picker
                    v-model="temp.startDate"
                    type="date"
                    placeholder="选择开始日期"
                    style="width: 100%;"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划结束日期" prop="endDate">
                  <el-date-picker
                    v-model="temp.endDate"
                    type="date"
                    placeholder="选择结束日期"
                    style="width: 100%;"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <el-tab-pane label="其他信息" name="purpose">
            <el-form-item label="公司名称">
              <el-input v-model="temp.companyName" placeholder="请输入公司名称" />
            </el-form-item>
            <el-form-item label="计划描述">
              <el-input v-model="temp.description" type="textarea" :rows="6" placeholder="请详细描述融资计划" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 融资计划详情对话框 -->
    <el-dialog title="融资计划详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentPlan" class="plan-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="计划编号">{{ currentPlan.planNo }}</el-descriptions-item>
          <el-descriptions-item label="计划名称">{{ currentPlan.planName }}</el-descriptions-item>
          <el-descriptions-item label="融资类型">{{ getFinancingTypeText(currentPlan.planType) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ getCurrencyText(currentPlan.currencyCode) }}</el-descriptions-item>
          <el-descriptions-item label="计划金额">{{ formatCurrency(currentPlan.planAmount) }}</el-descriptions-item>
          <el-descriptions-item label="计划年度">{{ currentPlan.planYear }}</el-descriptions-item>
          <el-descriptions-item label="计划状态">
            <el-tag :type="getPlanStatusTagType(currentPlan.planStatus)">
              {{ getPlanStatusText(currentPlan.planStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="公司名称">{{ currentPlan.companyName }}</el-descriptions-item>
          <el-descriptions-item label="计划开始日期">{{ formatDate(currentPlan.startDate) }}</el-descriptions-item>
          <el-descriptions-item label="计划结束日期">{{ formatDate(currentPlan.endDate) }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentPlan.createdBy }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentPlan.createdTime) }}</el-descriptions-item>
        </el-descriptions>

        <div class="plan-purpose">
          <h4>计划描述</h4>
          <p>{{ currentPlan.description || '无' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentPlan && currentPlan.planStatus === 'DRAFT'" type="primary" @click="handleSubmit(currentPlan)">
          提交审批
        </el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="融资计划审批" :visible.sync="dialogApprovalVisible" width="600px">
      <el-form ref="approvalForm" :model="approvalForm" label-width="100px">
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalOpinion">
          <el-input v-model="approvalForm.approvalOpinion" type="textarea" :rows="4" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogApprovalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">提交审批</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入融资计划" :visible.sync="dialogImportVisible" width="500px">
      <div class="import-content">
        <el-alert
          title="导入说明"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        >
          <template slot="default">
            <p>1. 请先下载导入模板，按模板格式填写数据</p>
            <p>2. 支持 .xlsx、.xls 格式文件</p>
            <p>3. 单次导入数据不超过500条</p>
          </template>
        </el-alert>

        <el-upload
          ref="importUpload"
          class="upload-area"
          drag
          action="#"
          :auto-upload="false"
          :limit="1"
          :on-change="handleImportFileChange"
          :on-exceed="handleImportExceed"
          :file-list="importFileList"
          accept=".xlsx,.xls"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传 xlsx/xls 文件</div>
        </el-upload>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDownloadTemplate">
          <i class="el-icon-download"></i> 下载模板
        </el-button>
        <el-button @click="dialogImportVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="submitImport">
          确认导入
        </el-button>
      </div>
    </el-dialog>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="dialogColumnVisible" width="400px">
      <div class="column-setting-content">
        <el-checkbox-group v-model="visibleColumns">
          <div v-for="col in allColumns" :key="col.prop" class="column-item">
            <el-checkbox :label="col.prop">{{ col.label }}</el-checkbox>
          </div>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="resetColumns">重置</el-button>
        <el-button size="small" @click="dialogColumnVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="applyColumnSetting">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFinancingPlanPage,
  createFinancingPlan,
  updateFinancingPlan,
  deleteFinancingPlan,
  submitFinancingPlan,
  approveFinancingPlan,
  rejectFinancingPlan,
  executeFinancingPlan,
  cancelFinancingPlan,
  copyFinancingPlan,
  getFinancingPlanStatistics,
  getFinancingTypeDistribution,
  getFinancingPlanTrend,
  importFinancingPlan,
  exportFinancingPlan,
  getFinancingPlanHistory
} from '@/api/globalTreasurer/rzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'FinancingPlanManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        planNo: undefined,
        financingType: undefined,
        planStatus: undefined,
        planDateRange: undefined
      },
      totalPlans: 0,
      executingPlans: 0,
      totalAmount: 0,
      completionRate: 0,
      typeChartType: 'pie',
      trendPeriod: '1Y',
      planList: [],
      multipleSelection: [],
      currentPlan: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogApprovalVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        planId: undefined,
        planNo: '',
        planName: '',
        planType: '',
        planYear: new Date().getFullYear(),
        planAmount: null,
        currencyCode: 'CNY',
        startDate: null,
        endDate: null,
        companyId: null,
        companyName: '',
        description: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalOpinion: ''
      },
      rules: {
        planName: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }],
        planType: [{ required: true, message: '请选择计划类型', trigger: 'change' }],
        planAmount: [{ required: true, message: '请输入计划金额', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
        financingPurpose: [{ required: true, message: '请输入融资用途', trigger: 'blur' }]
      },
      typeChart: null,
      trendChart: null,
      tableDensity: 'default', // 表格密度：default, medium, small
      // 导入相关
      dialogImportVisible: false,
      importLoading: false,
      importFileList: [],
      // 列设置相关
      dialogColumnVisible: false,
      allColumns: [
        { prop: 'planNo', label: '计划编号', visible: true },
        { prop: 'planName', label: '计划名称', visible: true },
        { prop: 'planType', label: '计划类型', visible: true },
        { prop: 'planYear', label: '计划年度', visible: true },
        { prop: 'planAmount', label: '计划金额', visible: true },
        { prop: 'currencyCode', label: '币种', visible: true },
        { prop: 'planStatus', label: '状态', visible: true },
        { prop: 'companyName', label: '所属公司', visible: true },
        { prop: 'startDate', label: '开始日期', visible: true },
        { prop: 'endDate', label: '结束日期', visible: true },
        { prop: 'createdTime', label: '创建时间', visible: false },
        { prop: 'createdBy', label: '创建人', visible: false }
      ],
      visibleColumns: ['planNo', 'planName', 'planType', 'planYear', 'planAmount', 'currencyCode', 'planStatus', 'companyName', 'startDate', 'endDate'],
      defaultVisibleColumns: ['planNo', 'planName', 'planType', 'planYear', 'planAmount', 'currencyCode', 'planStatus', 'companyName', 'startDate', 'endDate']
    }
  },
  mounted() {
    this.getList()
  },
  beforeDestroy() {
    if (this.typeChart) {
      this.typeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 响应码校验辅助方法
    isSuccessResponse(response) {
      return response && [1, 200, '1', '200'].includes(response.code)
    },
    getList() {
      this.listLoading = true
      // 调用真实API获取数据
      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        planNo: this.listQuery.planNo || undefined,
        financingType: this.listQuery.financingType || undefined,
        planStatus: this.listQuery.planStatus || undefined
      }

      getFinancingPlanPage(params).then(response => {
        if (this.isSuccessResponse(response)) {
          // 处理分页数据
          this.planList = response.data.tlist || response.data.list || response.data.records || []
          this.total = response.data.totalRecord || response.data.total || 0
        } else {
          this.planList = []
          this.total = 0
          if (response && response.msg) {
            this.$message.error(response.msg)
          }
        }
        this.listLoading = false
        // 先从列表数据计算基础统计值（作为fallback baseline）
        this.computeStatsFromList()
        // 加载统计数据
        this.loadStatistics()
        // 列表加载完成后初始化图表（确保DOM就绪且有fallback数据）
        this.$nextTick(() => {
          if (!this.typeChart) {
            this.initCharts()
          } else {
            this.updateTypeChart()
            this.updateTrendChart()
          }
        })
      }).catch(error => {
        console.error('查询融资计划数据异常:', error)
        this.planList = []
        this.total = 0
        this.listLoading = false
        this.$message.error('查询融资计划数据失败，请稍后重试')
        // 即使列表查询失败，也尝试加载统计数据和初始化图表
        this.loadStatistics()
        this.$nextTick(() => {
          if (!this.typeChart) {
            this.initCharts()
          }
        })
      })
    },
   computeStatsFromList() {
     // 从已加载的列表数据和分页total计算基础统计值
     if (this.total > 0) {
       this.totalPlans = this.total
     }
     if (this.planList && this.planList.length > 0) {
       const executingCount = this.planList.filter(item => item.planStatus === 'EXECUTING').length
       if (executingCount > 0) {
         this.executingPlans = executingCount
       }
       const totalAmt = this.planList.reduce((sum, item) => sum + (parseFloat(item.planAmount) || 0), 0)
       if (totalAmt > 0) {
         // 转换为万元
         this.totalAmount = (totalAmt / 10000).toFixed(2)
       }
       const completedCount = this.planList.filter(item => item.planStatus === 'COMPLETED').length
       if (this.planList.length > 0) {
         this.completionRate = ((completedCount / this.planList.length) * 100).toFixed(2)
       }
     }
   },
    loadStatistics() {
      // 加载统计数据，仅当API返回有效值(>0)时才覆盖baseline
      getFinancingPlanStatistics({}).then(response => {
        if (this.isSuccessResponse(response) && response.data) {
          const data = response.data
          const totalPlans = Number(data.totalPlans)
          const executingPlans = Number(data.executingPlans)
          const totalAmount = Number(data.totalAmount)
          const completionRate = Number(data.completionRate)
          // 只有API返回的值大于0时才覆盖baseline，避免API返回0覆盖掉已有的正确数据
          if (totalPlans > 0) {
            this.totalPlans = totalPlans
          }
          if (executingPlans > 0) {
            this.executingPlans = executingPlans
          }
          if (totalAmount > 0) {
            this.totalAmount = totalAmount > 10000 ? (totalAmount / 10000).toFixed(2) : totalAmount
          }
          if (completionRate > 0) {
            this.completionRate = completionRate
          }
        }
      }).catch(error => {
        console.error('加载统计数据失败:', error)
      })
    },
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化融资类型图表
      this.typeChart = echarts.init(document.getElementById('financingTypeChart'))
      this.updateTypeChart()
      
      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('planTrendChart'))
      this.updateTrendChart()
    },
    updateTypeChart() {
      // 调用后端API获取融资类型分布数据
      getFinancingTypeDistribution({}).then(response => {
        let data = []
        if (this.isSuccessResponse(response) && response.data) {
          const colorMap = {
            '草稿': '#909399',
            '待审批': '#E6A23C',
            '已审批': '#67C23A',
            '已拒绝': '#F56C6C',
            '执行中': '#409EFF',
            '已完成': '#67C23A',
            '已取消': '#909399'
          }
          data = response.data.map(item => ({
            name: item.name,
            value: item.value || 0,
            itemStyle: { color: colorMap[item.name] || '#409EFF' }
          }))
          // 过滤掉全为0的情况
          const hasValue = data.some(item => item.value > 0)
          if (hasValue) {
            this.renderTypeChart(data)
            return
          }
        }
        // API无有效数据时，从列表数据计算
        this.renderTypeChart(this.computeTypeDistributionFromList())
      }).catch(error => {
        console.error('获取融资类型分布数据失败:', error)
        this.renderTypeChart(this.computeTypeDistributionFromList())
      })
    },
    computeTypeDistributionFromList() {
      const colorMap = {
        '草稿': '#909399',
        '待审批': '#E6A23C',
        '已审批': '#67C23A',
        '已拒绝': '#F56C6C',
        '执行中': '#409EFF',
        '已完成': '#67C23A',
        '已取消': '#909399'
      }
      const statusMap = {
        'DRAFT': '草稿',
        'PENDING': '待审批',
        'SUBMITTED': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      const countMap = {}
      if (this.planList && this.planList.length > 0) {
        this.planList.forEach(item => {
          const name = statusMap[item.planStatus] || item.planStatus || '未知'
          countMap[name] = (countMap[name] || 0) + 1
        })
      }
      // 确保至少有基础分类
      const categories = ['草稿', '待审批', '已审批', '执行中', '已完成']
      categories.forEach(c => { if (!countMap[c]) countMap[c] = 0 })
      return Object.keys(countMap).map(name => ({
        name: name,
        value: countMap[name],
        itemStyle: { color: colorMap[name] || '#409EFF' }
      }))
    },
    renderTypeChart(data) {
      let option = {}

      if (this.typeChartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}个 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '融资类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              data: data
            }
          ]
        }
      } else {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '计划数量',
              type: 'bar',
              data: data.map(item => ({
                value: item.value,
                itemStyle: item.itemStyle
              }))
            }
          ]
        }
      }

      this.typeChart.setOption(option)
    },
    updateTrendChart() {
      const periodMonths = { '6M': 6, '1Y': 12, '2Y': 24 }
      const monthCount = periodMonths[this.trendPeriod] || 12
      // 调用后端API获取融资计划趋势数据
      getFinancingPlanTrend({ months: monthCount }).then(response => {
        let months = []
        let planData = []
        let amountData = []

        if (this.isSuccessResponse(response) && response.data && response.data.length > 0) {
          response.data.forEach(item => {
            // 后端返回 YYYY-MM 格式，转成可读标签
            const parts = (item.month || '').split('-')
            if (parts.length === 2) {
              months.push(parts[0].slice(2) + '/' + parseInt(parts[1]) + '月')
            } else {
              months.push(item.month + '月')
            }
            planData.push(item.planCount || 0)
            amountData.push(Math.round((item.totalAmount || 0) / 10000))
          })
          const hasValue = planData.some(v => v > 0) || amountData.some(v => v > 0)
          if (hasValue) {
            this.renderTrendChart(months, planData, amountData)
            return
          }
        }
        // API无有效数据时，从列表数据计算
        const fallback = this.computeTrendFromList()
        this.renderTrendChart(fallback.months, fallback.planData, fallback.amountData)
      }).catch(error => {
        console.error('获取融资计划趋势数据失败:', error)
        const fallback = this.computeTrendFromList()
        this.renderTrendChart(fallback.months, fallback.planData, fallback.amountData)
      })
    },
    computeTrendFromList() {
      const periodMonths = { '6M': 6, '1Y': 12, '2Y': 24 }
      const monthCount = periodMonths[this.trendPeriod] || 12
      const months = this.generateMonthLabels(monthCount)
      const planData = new Array(monthCount).fill(0)
      const amountData = new Array(monthCount).fill(0)
      if (this.planList && this.planList.length > 0) {
        const now = new Date()
        this.planList.forEach(item => {
          const dateStr = item.createdTime || item.startDate
          if (!dateStr) return
          const d = new Date(dateStr)
          if (isNaN(d.getTime())) return
          const diffMonths = (now.getFullYear() - d.getFullYear()) * 12 + (now.getMonth() - d.getMonth())
          const idx = monthCount - 1 - diffMonths
          if (idx >= 0 && idx < monthCount) {
            planData[idx] += 1
            amountData[idx] += Math.round((parseFloat(item.planAmount) || 0) / 10000)
          }
        })
      }
      return { months, planData, amountData }
    },
    renderTrendChart(months, planData, amountData) {
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['计划数量', '融资金额(万元)']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(个)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '计划数量',
            type: 'line',
            data: planData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '融资金额(万元)',
            type: 'line',
            yAxisIndex: 1,
            data: amountData,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }

      this.trendChart.setOption(option)
    },
    generateMonthLabels(count) {
      const labels = []
      for (let i = count - 1; i >= 0; i--) {
        const date = new Date()
        date.setMonth(date.getMonth() - i)
        labels.push((date.getMonth() + 1) + '月')
      }
      return labels
    },
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleTypeChartTypeChange() {
      this.updateTypeChart()
    },
    handleTrendPeriodChange() {
      this.updateTrendChart()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        planNo: undefined,
        financingType: undefined,
        planStatus: undefined,
        planDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增融资计划'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentPlan = row
      this.dialogDetailVisible = true
    },
    handleSubmit(row) {
      this.$confirm('确认提交该融资计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitFinancingPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('融资计划提交成功!')
            this.getList()
          } else {
            this.$message.error(response.msg || '提交失败')
          }
        }).catch(error => {
          console.error('提交融资计划失败:', error)
          this.$message.error('提交失败，请稍后重试')
        })
      })
    },
    handleApprove(row) {
      this.currentPlan = row
      this.approvalForm = {
        approvalResult: '',
        approvalOpinion: ''
      }
      this.dialogApprovalVisible = true
    },
    handleExecute(row) {
      this.$confirm('确认开始执行该融资计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeFinancingPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('融资计划开始执行!')
            this.getList()
          } else {
            this.$message.error(response.msg || '执行失败')
          }
        }).catch(error => {
          console.error('执行融资计划失败:', error)
          this.$message.error('执行失败，请稍后重试')
        })
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.handleEdit(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑融资计划'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleCopy(row) {
      this.$confirm('确认复制该融资计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        copyFinancingPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('融资计划复制成功!')
            this.getList()
          } else {
            this.$message.error(response.msg || '复制失败')
          }
        }).catch(error => {
          console.error('复制融资计划失败:', error)
          this.$message.error('复制失败，请稍后重试')
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认取消该融资计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelFinancingPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('融资计划已取消!')
            this.getList()
          } else {
            this.$message.error(response.msg || '取消失败')
          }
        }).catch(error => {
          console.error('取消融资计划失败:', error)
          this.$message.error('取消失败，请稍后重试')
        })
      })
    },
    handleDelete(row) {
      // 只有草稿状态的计划才能删除
      if (row.planStatus !== 'DRAFT') {
        this.$message.warning('只能删除草稿状态的融资计划')
        return
      }
      this.$confirm('确认删除该融资计划? 删除后不可恢复!', '警告', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        deleteFinancingPlan(row.planId).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('融资计划已删除!')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(error => {
          console.error('删除融资计划失败:', error)
          this.$message.error('删除失败，请稍后重试')
        })
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要删除的融资计划')
        return
      }
      // 检查是否都是草稿状态
      const nonDraftPlans = this.multipleSelection.filter(item => item.planStatus !== 'DRAFT')
      if (nonDraftPlans.length > 0) {
        this.$message.warning('只能删除草稿状态的融资计划，请重新选择')
        return
      }
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条融资计划? 删除后不可恢复!`, '警告', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }).then(() => {
        const planIds = this.multipleSelection.map(item => item.planId)
        // 逐个删除（如果后端有批量删除接口可以改为批量调用）
        const deletePromises = planIds.map(id => deleteFinancingPlan(id))
        Promise.all(deletePromises).then(() => {
          this.$message.success('批量删除成功!')
          this.getList()
        }).catch(error => {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败，请稍后重试')
        })
      })
    },
    handleRefresh() {
      this.getList()
      this.$message.success('数据已刷新')
    },
    handleTableSetting(command) {
      switch (command) {
        case 'density-default':
          this.tableDensity = 'default'
          this.$message.success('已切换为默认密度')
          break
        case 'density-medium':
          this.tableDensity = 'medium'
          this.$message.success('已切换为中等密度')
          break
        case 'density-small':
          this.tableDensity = 'small'
          this.$message.success('已切换为紧凑密度')
          break
        case 'column-setting':
          this.dialogColumnVisible = true
          break
      }
    },
    // 检查列是否可见
    isColumnVisible(prop) {
      return this.visibleColumns.includes(prop)
    },
    // 重置列设置
    resetColumns() {
      this.visibleColumns = [...this.defaultVisibleColumns]
      this.$message.success('已重置为默认列设置')
    },
    // 应用列设置
    applyColumnSetting() {
      if (this.visibleColumns.length === 0) {
        this.$message.warning('请至少选择一列显示')
        return
      }
      this.dialogColumnVisible = false
      this.$message.success('列设置已应用')
    },
    handleViewHistory(row) {
      // 调用后端API获取操作历史记录
      const loading = this.$loading({
        lock: true,
        text: '加载历史记录...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      getFinancingPlanHistory(row.planId).then(response => {
        loading.close()
        if (this.isSuccessResponse(response) && response.data) {
          const historyList = response.data || []
          let historyHtml = `
            <div style="text-align: left; max-height: 400px; overflow-y: auto;">
              <p><strong>计划编号：</strong>${row.planNo || '-'}</p>
              <p><strong>计划名称：</strong>${row.planName || '-'}</p>
              <p><strong>当前状态：</strong>${this.getPlanStatusText(row.planStatus)}</p>
              <hr style="margin: 10px 0; border-color: #eee;"/>
              <p><strong>操作历史：</strong></p>
          `
          if (historyList.length > 0) {
            historyList.forEach(item => {
              historyHtml += `
                <div style="padding: 8px; margin: 5px 0; background: #f5f7fa; border-radius: 4px;">
                  <p style="margin: 2px 0;"><strong>${item.operationType || '-'}</strong></p>
                  <p style="margin: 2px 0; font-size: 12px; color: #666;">操作人：${item.operatorName || '-'}</p>
                  <p style="margin: 2px 0; font-size: 12px; color: #666;">操作时间：${this.formatDate(item.operateTime) || '-'}</p>
                  ${item.comments ? `<p style="margin: 2px 0; font-size: 12px; color: #666;">备注：${item.comments}</p>` : ''}
                </div>
              `
            })
          } else {
            historyHtml += '<p style="color: #999;">暂无操作历史记录</p>'
          }
          historyHtml += '</div>'

          this.$alert(historyHtml, '计划历史信息', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定',
            customClass: 'history-dialog'
          })
        } else {
          // 如果接口返回失败，显示基本信息
          this.showBasicHistory(row)
        }
      }).catch(error => {
        loading.close()
        console.error('获取历史记录失败:', error)
        // 显示基本信息
        this.showBasicHistory(row)
      })
    },
    showBasicHistory(row) {
      const historyInfo = `
        <div style="text-align: left;">
          <p><strong>计划编号：</strong>${row.planNo || '-'}</p>
          <p><strong>计划名称：</strong>${row.planName || '-'}</p>
          <p><strong>当前状态：</strong>${this.getPlanStatusText(row.planStatus)}</p>
          <p><strong>创建人：</strong>${row.createdBy || '-'}</p>
          <p><strong>创建时间：</strong>${this.formatDate(row.createdTime) || '-'}</p>
          <p><strong>更新人：</strong>${row.updatedBy || '-'}</p>
          <p><strong>更新时间：</strong>${this.formatDate(row.updatedTime) || '-'}</p>
        </div>
      `
      this.$alert(historyInfo, '计划历史信息', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    handleImport() {
      this.dialogImportVisible = true
      this.importFileList = []
    },
    handleImportFileChange(file) {
      this.importFileList = [file]
    },
    handleImportExceed() {
      this.$message.warning('只能上传一个文件，请先删除已选文件')
    },
    handleDownloadTemplate() {
      // 下载导入模板
      const templateUrl = '/qqsk/financial/rzgl/financing-plan/template'
      window.open(templateUrl, '_blank')
      this.$message.info('正在下载模板...')
    },
    submitImport() {
      if (this.importFileList.length === 0) {
        this.$message.warning('请先选择要导入的文件')
        return
      }

      this.importLoading = true
      const formData = new FormData()
      formData.append('file', this.importFileList[0].raw)

      // 调用导入API
      importFinancingPlan(formData).then(response => {
        this.importLoading = false
        if (this.isSuccessResponse(response)) {
          const result = response.data || {}
          this.$message.success(`导入成功！成功 ${result.successCount || 0} 条，失败 ${result.failCount || 0} 条`)
          this.dialogImportVisible = false
          this.importFileList = []
          this.getList()
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      }).catch(error => {
        this.importLoading = false
        console.error('导入失败:', error)
        this.$message.error('导入失败，请稍后重试')
      })
    },
    handleExport() {
      this.$confirm('确认导出融资计划数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在导出数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        // 构建导出参数
        const params = {
          planNo: this.listQuery.planNo || undefined,
          financingType: this.listQuery.financingType || undefined,
          planStatus: this.listQuery.planStatus || undefined
        }

        exportFinancingPlan(params).then(response => {
          loading.close()
          // 创建下载链接
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `融资计划数据_${new Date().toISOString().slice(0, 10)}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('数据导出成功')
        }).catch(error => {
          loading.close()
          console.error('导出失败:', error)
          this.$message.error('导出失败，请稍后重试')
        })
      }).catch(() => {
        // 取消导出
      })
    },
    handleAttachmentChange(file) {
      this.$message({
        type: 'info',
        message: `已选择附件: ${file.name}`
      })
    },
    submitApproval() {
      if (!this.approvalForm.approvalResult) {
        this.$message({
          type: 'warning',
          message: '请选择审批结果'
        })
        return
      }

      const planId = this.currentPlan.planId
      const comments = this.approvalForm.approvalOpinion || ''

      if (this.approvalForm.approvalResult === 'APPROVED') {
        approveFinancingPlan(planId, comments).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('审批通过!')
            this.dialogApprovalVisible = false
            this.getList()
          } else {
            this.$message.error(response.msg || '审批失败')
          }
        }).catch(error => {
          console.error('审批失败:', error)
          this.$message.error('审批失败，请稍后重试')
        })
      } else {
        rejectFinancingPlan(planId, comments).then(response => {
          if (this.isSuccessResponse(response)) {
            this.$message.success('已拒绝!')
            this.dialogApprovalVisible = false
            this.getList()
          } else {
            this.$message.error(response.msg || '审批失败')
          }
        }).catch(error => {
          console.error('审批失败:', error)
          this.$message.error('审批失败，请稍后重试')
        })
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createFinancingPlan(this.temp).then(response => {
            if (this.isSuccessResponse(response)) {
              this.dialogFormVisible = false
              this.$message.success('融资计划创建成功')
              this.getList()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          }).catch(error => {
            console.error('创建融资计划失败:', error)
            this.$message.error('创建失败，请稍后重试')
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateFinancingPlan(this.temp).then(response => {
            if (this.isSuccessResponse(response)) {
              this.dialogFormVisible = false
              this.$message.success('融资计划更新成功')
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(error => {
            console.error('更新融资计划失败:', error)
            this.$message.error('更新失败，请稍后重试')
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        planId: undefined,
        planNo: '',
        planName: '',
        planType: '',
        planYear: new Date().getFullYear(),
        planAmount: null,
        currencyCode: 'CNY',
        startDate: null,
        endDate: null,
        companyId: null,
        companyName: '',
        description: ''
      }
    },
    getFinancingTypeTagType(type) {
      const typeMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return typeMap[type] || 'default'
    },
    getFinancingTypeText(type) {
      const textMap = {
        '1': '年度计划',
        '2': '季度计划',
        '3': '月度计划',
        '4': '临时计划'
      }
      return textMap[type] || type || '-'
    },
    getPlanStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'primary',
        'REJECTED': 'danger',
        'EXECUTING': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getPlanStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'PENDING': '待审批',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    getCurrencyText(code) {
      const textMap = {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'JPY': '日元',
        'GBP': '英镑',
        'HKD': '港币'
      }
      return textMap[code] || code || '人民币'
    },
    getExecutionProgress(plan) {
      if (plan.planStatus === 'COMPLETED') return 100
      if (plan.planStatus === 'EXECUTING') return Math.floor((plan.actualAmount / plan.plannedAmount) * 100)
      if (plan.planStatus === 'APPROVED') return 25
      if (plan.planStatus === 'PENDING') return 10
      return 0
    },
    getProgressStatus(plan) {
      if (plan.planStatus === 'COMPLETED') return 'success'
      if (plan.planStatus === 'EXECUTING') return 'active'
      if (plan.planStatus === 'CANCELLED') return 'exception'
      return 'normal'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      try {
        const date = new Date(dateStr)
        if (isNaN(date.getTime())) return dateStr
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      } catch (e) {
        return dateStr
      }
    },
    formatDateTime(dateStr) {
      if (!dateStr) return '-'
      try {
        const date = new Date(dateStr)
        if (isNaN(date.getTime())) return dateStr
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      } catch (e) {
        return dateStr
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.financing-plan-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .plan-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.executing-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.completion-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .plan-amount, .actual-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .plan-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    
    .plan-purpose, .plan-progress {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
      
      p {
        margin: 0;
        color: #606266;
        line-height: 1.6;
        padding: 8px 12px;
        background: #f8f9fa;
        border-radius: 4px;
      }
    }
  }

  .upload-demo {
    width: 100%;
  }

  // 列设置对话框样式
  .column-setting-content {
    max-height: 400px;
    overflow-y: auto;
    padding: 10px 0;

    .column-item {
      padding: 8px 12px;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        background-color: #f5f7fa;
      }
    }
  }
}
</style>
