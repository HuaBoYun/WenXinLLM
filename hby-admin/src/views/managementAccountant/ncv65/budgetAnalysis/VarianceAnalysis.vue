<template>
  <div class="variance-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算差异分析</h2>
      <p>深度分析预算与实际执行的差异，提供多维度差异分析和原因追踪</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-data-analysis" @click="handleCreateAnalysis">创建分析</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshAnalysis">刷新分析</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportAnalysis">导出分析</el-button>
            <el-button type="info" icon="el-icon-printer" @click="handlePrintReport">打印报告</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">分析设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 差异分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(analysisStats.totalVariance) }}</div>
            <div class="stat-label">总差异金额</div>
            <div class="stat-description">预算与实际的总差异</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长12.5%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card positive-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(analysisStats.positiveVariance) }}</div>
            <div class="stat-label">正差异</div>
            <div class="stat-description">实际超出预算的金额</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长8.3%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-top"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card negative-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(analysisStats.negativeVariance) }}</div>
            <div class="stat-label">负差异</div>
            <div class="stat-description">实际低于预算的金额</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-down trend-down"></i>
              <span>较上期下降5.2%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bottom"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ analysisStats.varianceRate || 0 }}%</div>
            <div class="stat-label">差异率</div>
            <div class="stat-description">差异占预算的比例</div>
            <div class="stat-trend">
              <i class="el-icon-arrow-up trend-up"></i>
              <span>较上期增长2.1%</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析条件 -->
    <el-card class="search-card" shadow="never">
      <div class="search-header">
        <span class="search-title">分析条件</span>
        <el-button type="text" @click="handleResetConditions">重置条件</el-button>
      </div>
      <el-form :model="analysisForm" :inline="true" size="small">
        <el-form-item label="分析期间">
          <el-date-picker
            v-model="analysisForm.analysisDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="组织单元">
          <el-select
            v-model="analysisForm.organizationPath"
            placeholder="请选择组织单元"
            clearable
            filterable
            style="width: 200px"
          >
            <template v-for="org in organizationOptions">
              <el-option :key="org.id" :label="org.name" :value="org.id" />
              <template v-if="org.children && org.children.length">
                <el-option
                  v-for="child in org.children"
                  :key="child.id"
                  :label="'　' + child.name"
                  :value="child.id"
                />
              </template>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select
            v-model="analysisForm.budgetAccount"
            placeholder="请选择预算科目"
            clearable
            filterable
            style="width: 180px"
          >
            <el-option
              v-for="item in budgetAccountOptions"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="差异类型">
          <el-select
            v-model="analysisForm.varianceType"
            placeholder="请选择差异类型"
            clearable
            style="width: 150px"
          >
            <el-option value="POSITIVE" label="正差异" />
            <el-option value="NEGATIVE" label="负差异" />
            <el-option value="ALL" label="全部差异" />
          </el-select>
        </el-form-item>
        <el-form-item label="差异阈值">
          <el-input-number
            v-model="analysisForm.varianceThreshold"
            :precision="2"
            :min="0"
            placeholder="差异阈值"
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleAnalyze">开始分析</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 差异分析图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>差异趋势分析</span>
            <div class="header-tools">
              <el-radio-group v-model="trendChartType" size="mini" @change="handleTrendChartTypeChange">
                <el-radio-button label="monthly">月度</el-radio-button>
                <el-radio-button label="quarterly">季度</el-radio-button>
                <el-radio-button label="yearly">年度</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="varianceTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>差异分布分析</span>
            <div class="header-tools">
              <el-radio-group v-model="distributionChartType" size="mini" @change="handleDistributionChartTypeChange">
                <el-radio-button label="department">部门</el-radio-button>
                <el-radio-button label="account">科目</el-radio-button>
                <el-radio-button label="project">项目</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="varianceDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 差异分析结果 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">差异分析结果</span>
        <div class="table-tools">
          <el-tooltip content="自动刷新" placement="top">
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="handleAutoRefreshChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getAnalysisResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="analysisResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="organizationName" label="组织单元" width="150" show-overflow-tooltip />
        <el-table-column prop="accountName" label="预算科目" width="150" show-overflow-tooltip />

        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceAmountClass(scope.row.varianceAmount)">
              {{ formatVarianceAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="varianceRate" label="差异率" width="100" align="right" sortable="custom">
          <template slot-scope="scope">
            <span :class="getVarianceRateClass(scope.row.varianceRate)">
              {{ formatVarianceRate(scope.row.varianceRate) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="varianceType" label="差异类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVarianceTypeColor(scope.row.varianceType)" size="mini">
              {{ getVarianceTypeText(scope.row.varianceType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="varianceReason" label="差异原因" min-width="200" show-overflow-tooltip />

        <el-table-column prop="analysisDate" label="分析日期" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.analysisDate || formatDateStr(scope.row.createTime) || '-' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit-outline"
              @click.stop="handleEditReason(scope.row)"
            >编辑原因</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              style="color: #F56C6C;"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
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

    <!-- 差异原因编辑对话框 -->
    <el-dialog
      title="编辑差异原因"
      :visible.sync="reasonDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="reasonForm"
        :model="reasonForm"
        :rules="reasonRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="差异原因" prop="varianceReason">
          <el-input
            v-model="reasonForm.varianceReason"
            type="textarea"
            :rows="4"
            placeholder="请输入差异原因分析"
          />
        </el-form-item>
        <el-form-item label="改进措施" prop="improvementMeasures">
          <el-input
            v-model="reasonForm.improvementMeasures"
            type="textarea"
            :rows="3"
            placeholder="请输入改进措施建议"
          />
        </el-form-item>
        <el-form-item label="责任人" prop="responsiblePerson">
          <el-select
            v-model="reasonForm.responsiblePerson"
            placeholder="请选择责任人"
            style="width: 100%"
          >
            <el-option
              v-for="user in userOptions"
              :key="user.id"
              :label="user.name"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="reasonDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveReason">保存</el-button>
      </div>
    </el-dialog>

    <!-- 创建/编辑差异分析对话框 -->
    <el-dialog :title="createForm.id ? '编辑差异分析' : '创建差异分析'" :visible.sync="createDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="createForm" :model="createForm" :rules="createRules" label-width="110px" size="small">
        <el-form-item label="分析名称" prop="analysisName">
          <el-input v-model="createForm.analysisName" placeholder="请输入分析名称" />
        </el-form-item>
        <el-form-item label="分析周期" prop="analysisPeriod">
          <el-date-picker v-model="createForm.analysisPeriod" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="组织单元" prop="organizationId">
          <el-select v-model="createForm.organizationId" placeholder="请选择组织单元" filterable style="width: 100%">
            <el-option
              v-for="org in organizationOptions"
              :key="org.id"
              :label="org.name"
              :value="org.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目" prop="budgetAccountId">
          <el-select v-model="createForm.budgetAccountId" placeholder="请选择预算科目" filterable style="width: 100%">
            <el-option
              v-for="item in budgetAccountOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算金额">
          <el-input-number v-model="createForm.budgetAmount" :min="0" :precision="2" :step="1000" style="width: 100%" placeholder="请输入预算金额" />
        </el-form-item>
        <el-form-item label="实际金额">
          <el-input-number v-model="createForm.actualAmount" :min="0" :precision="2" :step="1000" style="width: 100%" placeholder="请输入实际金额" />
        </el-form-item>
        <el-form-item label="差异原因">
          <el-input v-model="createForm.varianceReason" type="textarea" :rows="3" placeholder="请输入差异原因" />
        </el-form-item>
        <el-form-item label="改进措施">
          <el-input v-model="createForm.improvementMeasures" type="textarea" :rows="3" placeholder="请输入改进措施" />
        </el-form-item>
        <el-form-item label="责任人">
          <el-select v-model="createForm.responsiblePerson" placeholder="请选择责任人" clearable filterable style="width: 100%">
            <el-option
              v-for="user in userOptions"
              :key="user.id"
              :label="user.name"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分析说明">
          <el-input v-model="createForm.description" type="textarea" :rows="2" placeholder="请输入分析说明" />
        </el-form-item>
        <el-form-item label="差异阈值">
          <el-input-number v-model="createForm.varianceThreshold" :min="0" :max="100" :step="5" placeholder="差异阈值百分比" />
          <span class="form-tip">%</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="createLoading" @click="handleSubmitCreate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 设置对话框 -->
    <el-dialog title="差异分析设置" :visible.sync="settingsDialogVisible" width="550px">
      <el-form :model="settingsForm" label-width="120px" size="small">
        <el-form-item label="默认差异阈值">
          <el-input-number v-model="settingsForm.defaultThreshold" :min="0" :max="100" :step="5" />
          <span class="form-tip">%</span>
        </el-form-item>
        <el-form-item label="自动刷新间隔">
          <el-select v-model="settingsForm.refreshInterval" style="width: 100%">
            <el-option label="不自动刷新" :value="0" />
            <el-option label="30秒" :value="30" />
            <el-option label="1分钟" :value="60" />
            <el-option label="5分钟" :value="300" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认显示详情">
          <el-switch v-model="settingsForm.showDetails" />
        </el-form-item>
        <el-form-item label="每页显示条数">
          <el-select v-model="settingsForm.pageSize" style="width: 100%">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
            <el-option label="100条" :value="100" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="差异分析帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>差异分析用于比较预算与实际执行之间的差异，帮助管理者识别偏差并采取纠正措施。</p>
        <h4>操作指南</h4>
        <el-collapse>
          <el-collapse-item title="1. 创建差异分析" name="1">
            <p>点击"创建分析"按钮，填写分析名称、周期、组织单元等信息，提交后系统将自动计算差异数据。</p>
          </el-collapse-item>
          <el-collapse-item title="2. 查看分析结果" name="2">
            <p>在表格中查看各项差异数据，点击行可查看详情。支持按组织单元、科目等条件筛选。</p>
          </el-collapse-item>
          <el-collapse-item title="3. 编辑差异原因" name="3">
            <p>点击表格中的"编辑原因"按钮，可以填写差异原因、改进措施和责任人信息。</p>
          </el-collapse-item>
          <el-collapse-item title="4. 导出报告" name="4">
            <p>点击"导出分析"按钮可以导出当前分析结果为Excel文件。</p>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button>
      </div>
    </el-dialog>

    <!-- 钻取分析抽屉和对比分析抽屉已移除，使用编辑和删除功能 -->
  </div>
</template>

<script>
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'
import * as echarts from 'echarts'

export default {
  name: 'VarianceAnalysis',
  data() {
    return {
      // 分析条件
      analysisForm: {
        analysisDateRange: [],
        organizationPath: '',
        budgetAccount: '',
        varianceType: 'ALL',
        varianceThreshold: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      analysisResults: [],
      total: 0,
      
      // 图表类型
      trendChartType: 'monthly',
      distributionChartType: 'department',
      
      // 控制开关
      autoRefresh: false,
      showDetails: true,
      refreshTimer: null,
      
      // 统计数据
      analysisStats: {
        totalVariance: 0,
        positiveVariance: 0,
        negativeVariance: 0,
        varianceRate: 0
      },
      
      // 对话框
      reasonDialogVisible: false,
      detailDrawerVisible: false,
      createDialogVisible: false,
      settingsDialogVisible: false,
      helpDialogVisible: false,
      createLoading: false,
      reasonForm: {
        id: null,
        varianceReason: '',
        improvementMeasures: '',
        responsiblePerson: ''
      },
      createForm: {
        id: null,
        analysisName: '',
        analysisPeriod: [],
        organizationId: '',
        budgetAccountId: '',
        budgetAmount: 0,
        actualAmount: 0,
        varianceReason: '',
        improvementMeasures: '',
        responsiblePerson: null,
        description: '',
        varianceThreshold: 10
      },
      createRules: {
        analysisName: [{ required: true, message: '请输入分析名称', trigger: 'blur' }],
        analysisPeriod: [{ required: true, message: '请选择分析周期', trigger: 'change' }]
      },
      settingsForm: {
        defaultThreshold: 10,
        refreshInterval: 0,
        showDetails: true,
        pageSize: 20
      },
      reasonRules: {
        varianceReason: [
          { required: true, message: '请输入差异原因', trigger: 'blur' }
        ]
      },
      currentVarianceDetail: null,

      // 多选
      selectedRows: [],

      // 选项数据
      organizationOptions: [],
      budgetAccountOptions: [],
      userOptions: [],

      // 图表实例
      trendChart: null,
      distributionChart: null
    }
  },

  mounted() {
    this.getAnalysisResults()
    this.loadOrganizationOptions()
    this.loadBudgetAccountOptions()
    this.loadUserOptions()
    this.loadVarianceStats()
    // 用 setTimeout 确保 el-card 完成布局后再初始化图表
    setTimeout(() => {
      this.initTrendChart()
      this.initDistributionChart()
      // 图表初始化完成后再请求数据
      this.loadVarianceChartData()
      // 监听窗口变化，自动 resize 图表
      this._chartResizeHandler = () => {
        this.trendChart && this.trendChart.resize()
        this.distributionChart && this.distributionChart.resize()
      }
      window.addEventListener('resize', this._chartResizeHandler)
    }, 500)
  },

  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
    if (this._chartResizeHandler) {
      window.removeEventListener('resize', this._chartResizeHandler)
    }
    if (this.trendChart) {
      this.trendChart.dispose()
      this.trendChart = null
    }
    if (this.distributionChart) {
      this.distributionChart.dispose()
      this.distributionChart = null
    }
  },
  
  methods: {
    // 获取分析结果
    async getAnalysisResults() {
      this.loading = true
      try {
        const params = {
          ...this.analysisForm,
          ...this.queryParams
        }
        const response = await budgetAnalysisApi.getVarianceAnalysisPage(params)
        this.analysisResults = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载组织选项
    async loadOrganizationOptions() {
      try {
        const response = await budgetAnalysisApi.getVarianceAnalysisOrganizations()
        this.organizationOptions = response.data
      } catch (error) {
        console.error('加载组织选项失败：', error)
      }
    },
    
    // 加载预算科目选项
    async loadBudgetAccountOptions() {
      try {
        const response = await budgetAnalysisApi.getVarianceAnalysisBudgetAccounts()
        this.budgetAccountOptions = response.data
      } catch (error) {
        console.error('加载预算科目选项失败：', error)
      }
    },
    
    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetAnalysisApi.getVarianceAnalysisUsers()
        this.userOptions = response.data
      } catch (error) {
        console.error('加载用户选项失败：', error)
      }
    },
    
    // 加载统计数据
    async loadVarianceStats() {
      try {
        const response = await budgetAnalysisApi.getVarianceStats()
        if (response && response.data) {
          this.analysisStats = {
            totalVariance: response.data.totalVariance || 0,
            positiveVariance: response.data.positiveVariance || 0,
            negativeVariance: response.data.negativeVariance || 0,
            varianceRate: response.data.varianceRate || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    // 加载图表数据并更新图表
    async loadVarianceChartData() {
      try {
        const params = {
          ...this.analysisForm,
          trendChartType: this.trendChartType,
          distributionChartType: this.distributionChartType
        }
        const response = await budgetAnalysisApi.getVarianceChartData(params)
        if (response && response.data) {
          this.updateChartsWithData(response.data)
        }
      } catch (error) {
        console.error('加载图表数据失败：', error)
      }
    },

    // 趋势图表类型切换
    handleTrendChartTypeChange() {
      this.loadVarianceChartData()
    },

    // 分布图表类型切换
    handleDistributionChartTypeChange() {
      this.loadVarianceChartData()
    },

    // 用后端数据更新图表（完整替换 option，不合并，避免旧配置残留）
    updateChartsWithData(chartData) {
      if (chartData.trendChart && this.trendChart) {
        const trendData = chartData.trendChart
        const seriesList = trendData.series || []
        this.trendChart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['正差异', '负差异', '差异率'], top: 10 },
          grid: { left: '3%', right: '8%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: trendData.xAxis || [] },
          yAxis: [
            { type: 'value', name: '差异金额(元)', position: 'left', axisLabel: { formatter: (v) => (v / 10000).toFixed(0) + '万' } },
            { type: 'value', name: '差异率(%)', position: 'right', axisLabel: { formatter: '{value}%' } }
          ],
          series: [
            { name: '正差异', type: 'bar', data: seriesList[0] ? seriesList[0].data : [], itemStyle: { color: '#F56C6C' } },
            { name: '负差异', type: 'bar', data: seriesList[1] ? seriesList[1].data : [], itemStyle: { color: '#67C23A' } },
            { name: '差异率', type: 'line', yAxisIndex: 1, smooth: true, data: seriesList[2] ? seriesList[2].data : [], itemStyle: { color: '#409EFF' } }
          ]
        }, false)
        this.trendChart.resize()
      }
      if (chartData.distributionChart && chartData.distributionChart.length > 0 && this.distributionChart) {
        this.distributionChart.setOption({
          tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
          legend: { orient: 'vertical', left: 'left', top: 'middle' },
          series: [{
            name: '差异分布',
            type: 'pie',
            radius: ['40%', '65%'],
            center: ['60%', '50%'],
            data: chartData.distributionChart,
            emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } },
            label: { formatter: '{b}\n{d}%' }
          }]
        }, false)
        this.distributionChart.resize()
      }
    },

    // 初始化趋势图表
    initTrendChart() {
      const chartDom = document.getElementById('varianceTrendChart')
      if (!chartDom) return
      if (this.trendChart) {
        this.trendChart.dispose()
        this.trendChart = null
      }
      // 强制设置高度，防止容器 clientHeight=0 导致 ECharts 画布为空
      chartDom.style.height = '350px'
      this.trendChart = echarts.init(chartDom)
      this.trendChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['正差异', '负差异', '差异率'], top: 10 },
        grid: { left: '3%', right: '8%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: [] },
        yAxis: [
          { type: 'value', name: '差异金额(元)', position: 'left', axisLabel: { formatter: (v) => (v / 10000).toFixed(0) + '万' } },
          { type: 'value', name: '差异率(%)', position: 'right', axisLabel: { formatter: '{value}%' } }
        ],
        series: [
          { name: '正差异', type: 'bar', data: [], itemStyle: { color: '#F56C6C' } },
          { name: '负差异', type: 'bar', data: [], itemStyle: { color: '#67C23A' } },
          { name: '差异率', type: 'line', yAxisIndex: 1, data: [], itemStyle: { color: '#409EFF' }, smooth: true }
        ]
      })
    },

    // 初始化分布图表
    initDistributionChart() {
      const chartDom = document.getElementById('varianceDistributionChart')
      if (!chartDom) return
      if (this.distributionChart) {
        this.distributionChart.dispose()
        this.distributionChart = null
      }
      // 强制设置高度，防止容器 clientHeight=0 导致 ECharts 画布为空
      chartDom.style.height = '350px'
      this.distributionChart = echarts.init(chartDom)
      this.distributionChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', left: 'left', top: 'middle' },
        series: [{
          name: '差异分布',
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['60%', '50%'],
          data: [],
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } },
          label: { formatter: '{b}\n{d}%' }
        }]
      })
    },
    
    // 开始分析
    handleAnalyze() {
      this.queryParams.pageNum = 1
      this.getAnalysisResults()
    },
    
    // 重置条件
    handleReset() {
      this.analysisForm = {
        analysisDateRange: [],
        organizationPath: '',
        budgetAccount: '',
        varianceType: 'ALL',
        varianceThreshold: 0
      }
      this.handleAnalyze()
    },
    
    // 重置条件
    handleResetConditions() {
      this.handleReset()
    },
    
    // 自动刷新切换
    handleAutoRefreshChange(value) {
      if (value) {
        this.refreshTimer = setInterval(() => {
          this.getAnalysisResults()
        }, 60000) // 1分钟刷新一次
        this.$message.success('已开启自动刷新')
      } else {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
        }
        this.$message.info('已关闭自动刷新')
      }
    },
    
    // 显示详情切换
    handleShowDetailsChange(value) {
      this.$message.info(value ? '已显示详情' : '已隐藏详情')
    },
    
    // 创建分析
    handleCreateAnalysis() {
      this.createForm = {
        id: null,
        analysisName: '',
        analysisPeriod: [],
        organizationId: '',
        budgetAccountId: '',
        budgetAmount: 0,
        actualAmount: 0,
        varianceReason: '',
        improvementMeasures: '',
        responsiblePerson: null,
        description: '',
        varianceThreshold: 10
      }
      this.createDialogVisible = true
    },
    // 提交创建/更新分析
    async handleSubmitCreate() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true

        const data = {
          analysisName: this.createForm.analysisName,
          budgetYear: this.createForm.budgetYear || new Date().getFullYear(),
          budgetPeriod: this.getBudgetPeriodFromDate(this.createForm.analysisPeriod),
          analysisPeriod: this.createForm.analysisPeriod || [],
          organizationId: this.createForm.organizationId,
          accountId: this.createForm.budgetAccountId,
          budgetAmount: this.createForm.budgetAmount || 0,
          actualAmount: this.createForm.actualAmount || 0,
          varianceReason: this.createForm.varianceReason,
          improvementMeasures: this.createForm.improvementMeasures,
          reviewedBy: this.createForm.responsiblePerson
        }

        // 计算差异
        if (data.budgetAmount > 0 && data.actualAmount > 0) {
          data.varianceAmount = data.actualAmount - data.budgetAmount
          data.varianceRate = (data.varianceAmount / data.budgetAmount * 100).toFixed(2)
          data.varianceType = data.varianceAmount > 0 ? 'POSITIVE' : (data.varianceAmount < 0 ? 'NEGATIVE' : 'ZERO')
        }

        // 判断是创建还是更新
        let response
        if (this.createForm.id) {
          // 更新
          data.id = this.createForm.id
          response = await budgetAnalysisApi.variance.update(data)
          if (response && response.code === 1) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(response?.msg || '更新失败')
            return
          }
        } else {
          // 创建
          data.analysisStatus = 'draft'
          response = await budgetAnalysisApi.variance.create(data)
          if (response && response.code === 1) {
            this.$message.success('创建成功')
          } else {
            this.$message.error(response?.msg || '创建失败')
            return
          }
        }

        this.createDialogVisible = false
        this.getAnalysisResults()
        this.loadVarianceStats()
      } catch (error) {
        if (error !== false) {
          console.error('操作失败：', error)
          this.$message.error('操作失败：' + (error.message || error.response?.data?.msg || '未知错误'))
        }
      } finally {
        this.createLoading = false
      }
    },

    // 从日期范围获取预算期间
    getBudgetPeriodFromDate(dateRange) {
      if (!dateRange || dateRange.length < 2) return ''
      const start = new Date(dateRange[0])
      const end = new Date(dateRange[1])
      return `${start.getFullYear()}Q${Math.ceil((start.getMonth() + 1) / 3)}`
    },
    
    // 刷新分析
    handleRefreshAnalysis() {
      this.getAnalysisResults()
      this.loadVarianceStats()
      this.loadVarianceChartData()
    },

    // 表格多选
    handleSelectionChange(rows) {
      this.selectedRows = rows
    },

    // 导出分析：有选中则导出选中，否则导出当前页
    async handleExportAnalysis() {
      try {
        let params
        if (this.selectedRows && this.selectedRows.length > 0) {
          // 导出已选中数据
          params = {
            ids: this.selectedRows.map(r => r.id),
            exportType: 'selected'
          }
          this.$message.info(`正在导出已选中的 ${this.selectedRows.length} 条数据...`)
        } else {
          // 导出当前页数据
          params = {
            ...this.analysisForm,
            pageNum: this.queryParams.pageNum,
            pageSize: this.queryParams.pageSize,
            exportType: 'page'
          }
          this.$message.info(`正在导出当前页 ${this.analysisResults.length} 条数据...`)
        }

        const response = await budgetAnalysisApi.exportVarianceAnalysisReport(params)

        // 处理文件下载
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response)
          const link = document.createElement('a')
          link.href = url
          link.download = `预算差异分析_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败：返回数据格式错误')
        }
      } catch (error) {
        console.error('导出失败：', error)
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    
    // 打印报告
    handlePrintReport() {
      window.print()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    // 保存设置
    handleSaveSettings() {
      this.queryParams.pageSize = this.settingsForm.pageSize
      this.showDetails = this.settingsForm.showDetails
      this.analysisForm.varianceThreshold = this.settingsForm.defaultThreshold
      this.settingsDialogVisible = false
      this.$message.success('设置已保存')
      this.getAnalysisResults()
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 编辑原因（直接打开编辑弹窗，不打开详情弹窗）
    handleEditReason(row) {
      this.reasonForm = {
        id: row.id,
        varianceReason: row.varianceReason || '',
        improvementMeasures: row.improvementMeasures || '',
        responsiblePerson: row.responsiblePerson || null
      }
      this.reasonDialogVisible = true
    },

    // 编辑记录
    handleEdit(row) {
      // 将 analysisDate 还原为日期范围数组（用于日期选择器）
      const dateRange = row.analysisDate ? [row.analysisDate, row.analysisDate] : []
      this.createForm = {
        id: row.id,
        analysisName: row.analysisName,
        analysisPeriod: dateRange,
        budgetYear: row.budgetYear,
        budgetPeriod: row.budgetPeriod,
        organizationId: row.organizationId,
        budgetAccountId: row.accountId || row.budgetAccountId,
        budgetAmount: row.budgetAmount || 0,
        actualAmount: row.actualAmount || 0,
        varianceReason: row.varianceReason || '',
        improvementMeasures: row.improvementMeasures || '',
        responsiblePerson: row.responsiblePerson || null,
        description: row.analysisDescription || '',
        varianceThreshold: 10
      }
      this.createDialogVisible = true
    },

    // 删除记录
    handleDelete(row) {
      this.$confirm(`确定要删除差异分析【${row.analysisName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetAnalysisApi.variance.delete(row.id)
          if (response && response.code === 1) {
            this.$message.success('删除成功')
            this.getAnalysisResults()
            this.loadVarianceStats()
          } else {
            this.$message.error(response?.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败：', error)
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 保存原因
    async handleSaveReason() {
      try {
        await this.$refs.reasonForm.validate()
        await budgetAnalysisApi.updateVarianceAnalysisReason(this.reasonForm)
        this.$message.success('保存成功')
        this.reasonDialogVisible = false
        this.getAnalysisResults()
      } catch (error) {
        if (error !== false) this.$message.error('保存失败：' + (error.message || '未知错误'))
      }
    },
    
    // 初始化详情图表
    initDetailChart() {
      this.$nextTick(() => {
        const chartDom = document.getElementById('detailVarianceChart')
        if (chartDom) {
          const myChart = echarts.init(chartDom)
          const option = {
            title: {
              text: '差异趋势',
              left: 'center'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: ['1月', '2月', '3月', '4月', '5月', '6月']
            },
            yAxis: {
              type: 'value',
              name: '差异金额(万元)'
            },
            series: [
              {
                name: '差异金额',
                type: 'line',
                data: [12, -8, 15, -5, 20, -10],
                itemStyle: { color: '#409EFF' }
              }
            ]
          }
          myChart.setOption(option)
        }
      })
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetAnalysisApi.exportVarianceAnalysisSingle(row.id)

        // 处理文件下载
        if (response instanceof Blob) {
          const url = window.URL.createObjectURL(response)
          const link = document.createElement('a')
          link.href = url
          link.download = `差异分析_${row.analysisName}_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败：返回数据格式错误')
        }
      } catch (error) {
        console.error('导出失败：', error)
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    
    // 行点击（不再触发详情弹窗，已去除）
    handleRowClick() {
      // 行点击不做任何操作，避免意外触发弹窗
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getAnalysisResults()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getAnalysisResults()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getAnalysisResults()
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化差异金额
    formatVarianceAmount(amount) {
      if (!amount) return '0.00'
      const formatted = parseFloat(Math.abs(amount)).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
      return amount >= 0 ? `+${formatted}` : `-${formatted}`
    },
    
    // 格式化差异率
    formatVarianceRate(rate) {
      if (!rate) return '0.00%'
      return `${parseFloat(rate).toFixed(2)}%`
    },
    
    // 获取差异金额样式类
    getVarianceAmountClass(amount) {
      if (amount > 0) return 'positive-variance'
      if (amount < 0) return 'negative-variance'
      return 'zero-variance'
    },
    
    // 获取差异率样式类
    getVarianceRateClass(rate) {
      if (Math.abs(rate) > 10) return 'high-variance'
      if (Math.abs(rate) > 5) return 'medium-variance'
      return 'low-variance'
    },
    
    // 获取差异类型颜色
    getVarianceTypeColor(type) {
      const colorMap = {
        'POSITIVE': 'danger', 'positive': 'danger', 'favorable': 'success', 'FAVORABLE': 'success',
        'NEGATIVE': 'success', 'negative': 'success', 'unfavorable': 'danger', 'UNFAVORABLE': 'danger',
        'ZERO': 'info', 'zero': 'info', 'NONE': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取差异类型文本
    getVarianceTypeText(type) {
      const textMap = {
        'POSITIVE': '正差异', 'positive': '正差异', 'favorable': '有利差异', 'FAVORABLE': '有利差异',
        'NEGATIVE': '负差异', 'negative': '负差异', 'unfavorable': '不利差异', 'UNFAVORABLE': '不利差异',
        'ZERO': '无差异', 'zero': '无差异', 'NONE': '无差异'
      }
      return textMap[type] || type || '-'
    },

    // 格式化日期字符串
    formatDateStr(dateVal) {
      if (!dateVal) return null
      try {
        const d = new Date(dateVal)
        if (isNaN(d.getTime())) return null
        return d.toISOString().substring(0, 10)
      } catch (e) {
        return null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.variance-analysis {
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
      
      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.positive-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }
      
      &.negative-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.rate-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      .stat-content {
        position: relative;
        z-index: 2;
        
        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }
        
        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }
        
        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
          
          .trend-up {
            color: #F56C6C;
          }
          
          .trend-down {
            color: #67C23A;
          }
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
  
  .search-card {
    .search-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .search-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }
    }
  }
  
  .chart-row {
    margin-bottom: 20px;
    
    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .header-tools {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }
      
      .chart-container {
        height: 350px;
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
      align-items: center;
      gap: 12px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .positive-variance {
    color: #F56C6C;
    font-weight: 500;
  }
  
  .negative-variance {
    color: #67C23A;
    font-weight: 500;
  }
  
  .zero-variance {
    color: #909399;
    font-weight: 500;
  }
  
  .high-variance {
    color: #F56C6C;
    font-weight: 600;
  }
  
  .medium-variance {
    color: #E6A23C;
    font-weight: 500;
  }
  
  .low-variance {
    color: #67C23A;
    font-weight: 500;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-right {
    text-align: right;
  }
  
  .detail-content {
    padding: 20px;
    
    .detail-chart,
    .detail-analysis {
      margin-top: 20px;
      
      h4 {
        color: #303133;
        margin-bottom: 16px;
      }
      
      p {
        margin-bottom: 8px;
        line-height: 1.6;
      }
    }
  }
}
</style>
