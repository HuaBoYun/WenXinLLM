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
              <el-option label="已提交" value="SUBMITTED" />
              <el-option label="已审批" value="APPROVED" />
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
      <el-table
        :data="planList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="计划ID" prop="planId" width="80" align="center" />
        <el-table-column label="计划编号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.planNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划名称" width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.planName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="融资类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getFinancingTypeTagType(row.financingType)" size="mini">
              {{ getFinancingTypeText(row.financingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="计划金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="plan-amount">{{ formatCurrency(row.plannedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际金额" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="actual-amount">{{ formatCurrency(row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.interestRate ? row.interestRate + '%' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划开始日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.planStartDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划结束日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.planEndDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getPlanStatusTagType(row.planStatus)" size="mini">
              {{ getPlanStatusText(row.planStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createUserName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="row.planStatus === 'DRAFT'" type="primary" size="mini" @click="handleSubmit(row)">
              提交
            </el-button>
            <el-button v-if="row.planStatus === 'SUBMITTED'" type="success" size="mini" @click="handleApprove(row)">
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
                  <el-input v-model="temp.planNo" placeholder="系统自动生成" :disabled="dialogStatus==='update'" />
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
                <el-form-item label="融资类型" prop="financingType">
                  <el-select v-model="temp.financingType" placeholder="请选择融资类型" style="width: 100%;">
                    <el-option label="银行贷款" value="BANK_LOAN" />
                    <el-option label="债券发行" value="BOND" />
                    <el-option label="股权融资" value="EQUITY" />
                    <el-option label="融资租赁" value="LEASING" />
                    <el-option label="其他" value="OTHER" />
                  </el-select>
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
                <el-form-item label="计划金额" prop="plannedAmount">
                  <el-input-number
                    v-model="temp.plannedAmount"
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
                <el-form-item label="预期利率">
                  <el-input-number
                    v-model="temp.interestRate"
                    :precision="4"
                    :step="0.1"
                    :min="0"
                    :max="20"
                    style="width: 100%;"
                    placeholder="请输入预期利率"
                  />
                  <span style="margin-left: 8px;">%</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="计划开始日期" prop="planStartDate">
                  <el-date-picker
                    v-model="temp.planStartDate"
                    type="date"
                    placeholder="选择开始日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划结束日期" prop="planEndDate">
                  <el-date-picker
                    v-model="temp.planEndDate"
                    type="date"
                    placeholder="选择结束日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="融资用途" name="purpose">
            <el-form-item label="融资用途" prop="financingPurpose">
              <el-input v-model="temp.financingPurpose" type="textarea" :rows="6" placeholder="请详细描述融资用途" />
            </el-form-item>
            <el-form-item label="预期成本">
              <el-input-number
                v-model="temp.plannedCost"
                :precision="2"
                :step="10000"
                :min="0"
                style="width: 100%;"
                placeholder="请输入预期成本"
              />
            </el-form-item>
            <el-form-item label="风险评估">
              <el-input v-model="temp.riskAssessment" type="textarea" :rows="4" placeholder="请输入风险评估" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="other">
            <el-form-item label="备注信息">
              <el-input v-model="temp.remark" type="textarea" :rows="4" placeholder="请输入备注信息" />
            </el-form-item>
            <el-form-item label="附件上传">
              <el-upload
                class="upload-demo"
                action="#"
                :auto-upload="false"
                :on-change="handleAttachmentChange"
                multiple
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">支持上传多个文件</div>
              </el-upload>
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
          <el-descriptions-item label="融资类型">{{ getFinancingTypeText(currentPlan.financingType) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentPlan.currencyCode }}</el-descriptions-item>
          <el-descriptions-item label="计划金额">{{ formatCurrency(currentPlan.plannedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="实际金额">{{ formatCurrency(currentPlan.actualAmount) }}</el-descriptions-item>
          <el-descriptions-item label="预期利率">{{ currentPlan.interestRate ? currentPlan.interestRate + '%' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="计划状态">
            <el-tag :type="getPlanStatusTagType(currentPlan.planStatus)">
              {{ getPlanStatusText(currentPlan.planStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="计划开始日期">{{ currentPlan.planStartDate }}</el-descriptions-item>
          <el-descriptions-item label="计划结束日期">{{ currentPlan.planEndDate }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentPlan.createUserName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentPlan.createTime }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="plan-purpose">
          <h4>融资用途</h4>
          <p>{{ currentPlan.financingPurpose || '无' }}</p>
        </div>
        
        <div class="plan-progress">
          <h4>执行进度</h4>
          <el-progress
            :percentage="getExecutionProgress(currentPlan)"
            :status="getProgressStatus(currentPlan)"
            :stroke-width="20"
            text-inside
          />
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
  </div>
</template>

<script>
import { getFinancingPlanPage, createFinancingPlan, updateFinancingPlan } from '@/api/globalTreasurer/rzgl'
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
      totalPlans: 28,
      executingPlans: 12,
      totalAmount: 85600.5,
      completionRate: 68.5,
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
        financingType: '',
        plannedAmount: null,
        currencyCode: 'CNY',
        planStartDate: null,
        planEndDate: null,
        interestRate: null,
        financingPurpose: '',
        plannedCost: null,
        riskAssessment: '',
        remark: ''
      },
      approvalForm: {
        approvalResult: '',
        approvalOpinion: ''
      },
      rules: {
        planNo: [{ required: true, message: '计划编号不能为空', trigger: 'blur' }],
        planName: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }],
        financingType: [{ required: true, message: '请选择融资类型', trigger: 'change' }],
        plannedAmount: [{ required: true, message: '请输入计划金额', trigger: 'blur' }],
        planStartDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        planEndDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
        financingPurpose: [{ required: true, message: '请输入融资用途', trigger: 'blur' }]
      },
      typeChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.initCharts()
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
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.planList = [
          {
            planId: 1,
            planNo: 'FP20240925001',
            planName: '流动资金贷款计划',
            financingType: 'BANK_LOAN',
            plannedAmount: 50000000.00,
            actualAmount: 50000000.00,
            currencyCode: 'CNY',
            interestRate: 4.35,
            planStartDate: '2024-10-01',
            planEndDate: '2025-09-30',
            planStatus: 'EXECUTING',
            createUserName: '张三',
            createTime: '2024-09-25 10:00:00',
            financingPurpose: '用于补充企业流动资金，支持日常经营活动'
          },
          {
            planId: 2,
            planNo: 'FP20240920002',
            planName: '设备采购融资租赁',
            financingType: 'LEASING',
            plannedAmount: 20000000.00,
            actualAmount: 0.00,
            currencyCode: 'CNY',
            interestRate: 5.2,
            planStartDate: '2024-11-01',
            planEndDate: '2027-10-31',
            planStatus: 'APPROVED',
            createUserName: '李四',
            createTime: '2024-09-20 14:30:00',
            financingPurpose: '用于采购生产设备，提升产能'
          },
          {
            planId: 3,
            planNo: 'FP20240915003',
            planName: '企业债券发行',
            financingType: 'BOND',
            plannedAmount: 100000000.00,
            actualAmount: 0.00,
            currencyCode: 'CNY',
            interestRate: 4.8,
            planStartDate: '2025-01-01',
            planEndDate: '2028-12-31',
            planStatus: 'SUBMITTED',
            createUserName: '王五',
            createTime: '2024-09-15 16:45:00',
            financingPurpose: '用于项目建设和偿还到期债务'
          }
        ]
        this.total = this.planList.length
        this.listLoading = false
      }, 1000)
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
      const data = [
        { name: '银行贷款', value: 15, itemStyle: { color: '#409EFF' } },
        { name: '债券发行', value: 6, itemStyle: { color: '#67C23A' } },
        { name: '融资租赁', value: 4, itemStyle: { color: '#E6A23C' } },
        { name: '股权融资', value: 2, itemStyle: { color: '#F56C6C' } },
        { name: '其他', value: 1, itemStyle: { color: '#909399' } }
      ]
      
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
      const months = this.generateMonthLabels(12)
      const planData = this.generateMockData(12, 2, 8)
      const amountData = this.generateMockData(12, 5000, 20000)
      
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
        row.planStatus = 'SUBMITTED'
        this.$message({
          type: 'success',
          message: '融资计划提交成功!'
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
        row.planStatus = 'EXECUTING'
        this.$message({
          type: 'success',
          message: '融资计划开始执行!'
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
      this.temp = Object.assign({}, row)
      this.temp.planId = undefined
      this.temp.planNo = ''
      this.temp.planName = row.planName + ' (副本)'
      this.dialogStatus = 'create'
      this.dialogTitle = '复制融资计划'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleCancel(row) {
      this.$confirm('确认取消该融资计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.planStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '融资计划已取消!'
        })
      })
    },
    handleViewHistory(row) {
      this.$message({
        type: 'info',
        message: '查看历史记录功能'
      })
    },
    handleImport() {
      this.$message({
        type: 'info',
        message: '批量导入功能'
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '数据导出成功'
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
      
      if (this.approvalForm.approvalResult === 'APPROVED') {
        this.currentPlan.planStatus = 'APPROVED'
      } else {
        this.currentPlan.planStatus = 'DRAFT'
      }
      
      this.dialogApprovalVisible = false
      this.$message({
        type: 'success',
        message: '审批完成!'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.planId = parseInt(Math.random() * 100) + 1024
          this.temp.planNo = 'FP' + new Date().getTime()
          this.temp.planStatus = 'DRAFT'
          this.temp.actualAmount = 0
          this.temp.createUserName = '当前用户'
          this.temp.createTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
          if (this.temp.planStartDate) {
            this.temp.planStartDate = this.temp.planStartDate.toISOString().slice(0, 10)
          }
          if (this.temp.planEndDate) {
            this.temp.planEndDate = this.temp.planEndDate.toISOString().slice(0, 10)
          }
          this.planList.unshift(this.temp)
          this.total = this.planList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '融资计划创建成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.planStartDate && typeof this.temp.planStartDate === 'object') {
            this.temp.planStartDate = this.temp.planStartDate.toISOString().slice(0, 10)
          }
          if (this.temp.planEndDate && typeof this.temp.planEndDate === 'object') {
            this.temp.planEndDate = this.temp.planEndDate.toISOString().slice(0, 10)
          }
          const index = this.planList.findIndex(v => v.planId === this.temp.planId)
          this.planList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '融资计划更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        planId: undefined,
        planNo: '',
        planName: '',
        financingType: '',
        plannedAmount: null,
        currencyCode: 'CNY',
        planStartDate: null,
        planEndDate: null,
        interestRate: null,
        financingPurpose: '',
        plannedCost: null,
        riskAssessment: '',
        remark: ''
      }
    },
    getFinancingTypeTagType(type) {
      const typeMap = {
        'BANK_LOAN': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'LEASING': 'info',
        'OTHER': 'default'
      }
      return typeMap[type] || 'default'
    },
    getFinancingTypeText(type) {
      const textMap = {
        'BANK_LOAN': '银行贷款',
        'BOND': '债券发行',
        'EQUITY': '股权融资',
        'LEASING': '融资租赁',
        'OTHER': '其他'
      }
      return textMap[type] || type
    },
    getPlanStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'primary',
        'EXECUTING': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getPlanStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    getExecutionProgress(plan) {
      if (plan.planStatus === 'COMPLETED') return 100
      if (plan.planStatus === 'EXECUTING') return Math.floor((plan.actualAmount / plan.plannedAmount) * 100)
      if (plan.planStatus === 'APPROVED') return 25
      if (plan.planStatus === 'SUBMITTED') return 10
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
}
</style>
