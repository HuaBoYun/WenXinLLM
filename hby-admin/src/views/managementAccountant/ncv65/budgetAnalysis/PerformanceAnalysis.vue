<template>
  <div class="performance-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算绩效分析</h2>
      <p>全面评估预算执行绩效，提供多维度绩效指标分析和绩效改进建议</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-medal" @click="handleCreatePerformanceAnalysis">创建绩效分析</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshPerformance">刷新绩效</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportPerformance">导出绩效</el-button>
            <el-button type="info" icon="el-icon-s-marketing" @click="handlePerformanceReport">绩效报告</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handlePerformanceSettings">绩效设置</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 绩效分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card overall-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ performanceStats.overallScore }}</div>
            <div class="stat-label">综合绩效</div>
            <div class="stat-description">整体绩效评分</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="performanceStats.overallScore" 
                :stroke-width="6"
                :color="getScoreColor(performanceStats.overallScore)"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-medal"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card efficiency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ performanceStats.efficiency }}%</div>
            <div class="stat-label">执行效率</div>
            <div class="stat-description">预算执行效率</div>
            <div class="stat-trend">
              <i :class="performanceStats.efficiency >= 80 ? 'el-icon-arrow-up trend-up' : 'el-icon-arrow-down trend-down'"></i>
              <span>{{ performanceStats.efficiency >= 90 ? '优秀' : performanceStats.efficiency >= 80 ? '良好' : performanceStats.efficiency >= 70 ? '一般' : '待改进' }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ performanceStats.accuracy }}%</div>
            <div class="stat-label">执行准确性</div>
            <div class="stat-description">预算执行准确性</div>
            <div class="stat-trend">
              <i :class="performanceStats.accuracy >= 80 ? 'el-icon-arrow-up trend-up' : 'el-icon-arrow-down trend-down'"></i>
              <span>{{ performanceStats.accuracy >= 90 ? '优秀' : performanceStats.accuracy >= 80 ? '良好' : performanceStats.accuracy >= 70 ? '一般' : '待改进' }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card improvement-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ performanceStats.improvementRate }}%</div>
            <div class="stat-label">改进率</div>
            <div class="stat-description">绩效改进率</div>
            <div class="stat-trend">
              <i :class="performanceStats.improvementRate > 0 ? 'el-icon-arrow-up trend-up' : 'el-icon-arrow-down trend-down'"></i>
              <span>{{ performanceStats.improvementRate > 0 ? '持续改进中' : '需关注' }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-top"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 绩效分析条件 -->
    <el-card class="search-card" shadow="never">
      <div class="search-header">
        <span class="search-title">绩效分析条件</span>
        <el-button type="text" @click="handleResetConditions">重置条件</el-button>
      </div>
      <el-form :model="performanceForm" :inline="true" size="small">
        <el-form-item label="预算类型">
          <el-select
            v-model="performanceForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option value="DEPT_MONTH" label="部门月度" />
            <el-option value="DEPT_YEAR" label="部门年度" />
            <el-option value="PROJECT" label="项目预算" />
            <el-option value="EXPENSE_TYPE" label="费用类型" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select
            v-model="performanceForm.businessType"
            placeholder="请选择业务类型"
            clearable
            style="width: 150px"
          >
            <el-option value="PAYMENT" label="付款" />
            <el-option value="REIMBURSEMENT" label="报销" />
            <el-option value="CONTRACT" label="合同" />
            <el-option value="PURCHASE" label="采购" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行人">
          <el-input
            v-model="performanceForm.executionUser"
            placeholder="请输入执行人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="执行时间">
          <el-date-picker
            v-model="performanceForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleAnalyzePerformance">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 绩效仪表板 -->
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="16">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>绩效雷达图</span>
            <div class="header-tools">
              <el-radio-group v-model="radarChartType" size="mini">
                <el-radio-button label="current">当期绩效</el-radio-button>
                <el-radio-button label="comparison">对比分析</el-radio-button>
                <el-radio-button label="trend">趋势分析</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="performanceRadarChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="ranking-card" shadow="never">
          <div slot="header" class="card-header">
            <span>绩效排名</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshRanking" />
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in performanceRanking"
              :key="item.id"
              class="ranking-item"
              :class="getRankingClass(index)"
            >
              <div class="ranking-number">{{ index + 1 }}</div>
              <div class="ranking-content">
                <div class="ranking-name">{{ item.organizationName }}</div>
                <div class="ranking-score">{{ item.score }}分</div>
              </div>
              <div class="ranking-badge">
                <el-tag :type="getScoreType(item.score)" size="mini">
                  {{ getScoreLevel(item.score) }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 绩效指标图表 -->
    <el-row :gutter="20" class="metrics-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>执行效率趋势</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshEfficiencyChart" />
          </div>
          <div id="efficiencyChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>准确性分析</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshAccuracyChart" />
          </div>
          <div id="accuracyChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>改进建议</span>
            <el-button icon="el-icon-edit" size="mini" @click="handleEditSuggestions" />
          </div>
          <div class="suggestions-content">
            <div
              v-for="suggestion in improvementSuggestions"
              :key="suggestion.id"
              class="suggestion-item"
            >
              <div class="suggestion-priority">
                <el-tag :type="getPriorityColor(suggestion.priority)" size="mini">
                  {{ getPriorityText(suggestion.priority) }}
                </el-tag>
              </div>
              <div class="suggestion-content">
                <div class="suggestion-title">{{ suggestion.title }}</div>
                <div class="suggestion-description">{{ suggestion.description }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 绩效分析结果 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">绩效分析结果</span>
        <div class="table-tools">
          <el-tooltip content="显示详细指标" placement="top">
            <el-switch
              v-model="showDetailedMetrics"
              active-text="详细指标"
              @change="handleShowDetailedMetricsChange"
            />
          </el-tooltip>
          <el-tooltip content="显示改进建议" placement="top">
            <el-switch
              v-model="showSuggestions"
              active-text="改进建议"
              @change="handleShowSuggestionsChange"
            />
          </el-tooltip>
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getPerformanceResults" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="performanceResults"
        border
        stripe
        highlight-current-row
        @sort-change="handleSortChange"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column prop="executionId" label="执行编号" width="120" show-overflow-tooltip />
        <el-table-column prop="executionUserName" label="执行人" width="100" align="center" />

        <el-table-column prop="budgetType" label="预算类型" width="130" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBudgetTypeColor(scope.row.budgetType)" size="mini">
              {{ getBudgetTypeText(scope.row.budgetType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="businessType" label="业务类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">
              {{ getBusinessTypeText(scope.row.businessType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="amount" label="执行金额" width="140" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="budgetId" label="预算编号" width="110" align="center" v-if="showDetailedMetrics" />

        <el-table-column prop="ruleId" label="规则编号" width="110" align="center" v-if="showDetailedMetrics" />

        <el-table-column prop="executionTime" label="执行时间" width="170" align="center" sortable="custom">
          <template slot-scope="scope">
            {{ formatTime(scope.row.executionTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip v-if="showSuggestions" />

        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >详情</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEditPerformance(scope.row)"
            >编辑</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="improvement" icon="el-icon-top">改进计划</el-dropdown-item>
                <el-dropdown-item command="benchmark" icon="el-icon-data-analysis">标杆对比</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
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

    <!-- 详情对话框 -->
    <el-dialog title="执行详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions v-if="currentPerformanceDetail" :column="2" border>
        <el-descriptions-item label="执行编号">{{ currentPerformanceDetail.executionId }}</el-descriptions-item>
        <el-descriptions-item label="执行人">{{ currentPerformanceDetail.executionUserName }}</el-descriptions-item>
        <el-descriptions-item label="预算类型">{{ getBudgetTypeText(currentPerformanceDetail.budgetType) }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">{{ getBusinessTypeText(currentPerformanceDetail.businessType) }}</el-descriptions-item>
        <el-descriptions-item label="执行金额">{{ formatAmount(currentPerformanceDetail.amount) }}</el-descriptions-item>
        <el-descriptions-item label="执行时间">{{ formatTime(currentPerformanceDetail.executionTime) }}</el-descriptions-item>
        <el-descriptions-item label="预算编号">{{ currentPerformanceDetail.budgetId }}</el-descriptions-item>
        <el-descriptions-item label="规则编号">{{ currentPerformanceDetail.ruleId }}</el-descriptions-item>
        <el-descriptions-item label="业务编号">{{ currentPerformanceDetail.businessId }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentPerformanceDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button type="primary" @click="detailDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog :title="editForm.executionId ? '编辑执行记录' : '创建执行记录'" :visible.sync="editDialogVisible" width="600px">
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="100px" size="small">
        <el-form-item label="预算类型" prop="budgetType">
          <el-select v-model="editForm.budgetType" placeholder="请选择" style="width: 100%">
            <el-option value="DEPT_MONTH" label="部门月度" />
            <el-option value="DEPT_YEAR" label="部门年度" />
            <el-option value="PROJECT" label="项目预算" />
            <el-option value="EXPENSE_TYPE" label="费用类型" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型" prop="businessType">
          <el-select v-model="editForm.businessType" placeholder="请选择" style="width: 100%">
            <el-option value="PAYMENT" label="付款" />
            <el-option value="REIMBURSEMENT" label="报销" />
            <el-option value="CONTRACT" label="合同" />
            <el-option value="PURCHASE" label="采购" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行金额" prop="amount">
          <el-input-number v-model="editForm.amount" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预算编号" prop="budgetId">
          <el-input v-model="editForm.budgetId" placeholder="请输入预算编号" />
        </el-form-item>
        <el-form-item label="执行人" prop="executionUserName">
          <el-input v-model="editForm.executionUserName" placeholder="请输入执行人" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitEditForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 绩效报告对话框 -->
    <el-dialog title="绩效报告" :visible.sync="reportDialogVisible" width="800px" top="5vh">
      <div v-loading="reportLoading">
        <template v-if="reportData">
          <el-descriptions title="报告概要" :column="2" border style="margin-bottom: 20px">
            <el-descriptions-item label="报告类型">{{ reportData.reportType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="生成时间">{{ formatTime(reportData.reportTime) }}</el-descriptions-item>
          </el-descriptions>
          <el-card shadow="never" style="margin-bottom: 16px" v-if="reportData.statistics">
            <div slot="header">统计信息</div>
            <el-row :gutter="16">
              <el-col :span="6" v-for="(val, key) in reportData.statistics" :key="key">
                <div style="text-align:center;padding:8px 0">
                  <div style="font-size:20px;font-weight:600;color:#409EFF">{{ val }}</div>
                  <div style="font-size:12px;color:#909399;margin-top:4px">{{ key }}</div>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </template>
        <el-empty v-else description="暂无报告数据" />
      </div>
      <div slot="footer"><el-button type="primary" @click="reportDialogVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 设置对话框 -->
    <el-dialog title="绩效分析设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="120px" size="small">
        <el-form-item label="每页显示条数">
          <el-select v-model="queryParams.pageSize" style="width: 100%">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="绩效分析帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>绩效分析用于评估预算执行的效果和效率，支持多维度绩效考核。</p>
        <h4>操作指南</h4>
        <p>1. 使用顶部工具栏的按钮进行创建、刷新和导出操作。</p>
        <p>2. 使用筛选条件缩小分析范围。</p>
        <p>3. 点击操作列按钮查看详情或编辑记录。</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'
import * as echarts from 'echarts'

export default {
  name: 'PerformanceAnalysis',
  data() {
    return {
      // 筛选条件
      performanceForm: {
        budgetType: '',
        businessType: '',
        executionUser: '',
        dateRange: []
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },

      // 表格数据
      loading: false,
      performanceResults: [],
      total: 0,

      // 图表类型
      radarChartType: 'current',
      // 缓存后端返回的图表数据
      cachedChartData: null,

      // 控制开关
      showDetailedMetrics: false,
      showSuggestions: true,

      // 统计数据
      performanceStats: {
        overallScore: 0,
        efficiency: 0,
        accuracy: 0,
        improvementRate: 0
      },

      // 绩效排名
      performanceRanking: [],

      // 改进建议
      improvementSuggestions: [],

      // 详情对话框
      detailDialogVisible: false,
      currentPerformanceDetail: null,

      // 编辑对话框
      editDialogVisible: false,
      editForm: {
        executionId: '',
        budgetType: '',
        businessType: '',
        amount: 0,
        budgetId: '',
        executionUserName: '',
        remark: ''
      },
      editFormRules: {
        budgetType: [{ required: true, message: '请选择预算类型', trigger: 'change' }],
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
        amount: [{ required: true, message: '请输入执行金额', trigger: 'blur' }]
      },
      submitLoading: false,

      // 报告对话框
      reportDialogVisible: false,
      reportData: null,
      reportLoading: false,

      // 其他对话框
      settingsDialogVisible: false,
      helpDialogVisible: false
    }
  },

  created() {
    this.getPerformanceResults()
    this.loadPerformanceStats()
    this.loadPerformanceRanking()
    this.loadImprovementSuggestions()
    this.initCharts()
  },

  watch: {
    radarChartType() {
      if (this.cachedChartData) {
        this.renderRadarChart(this.cachedChartData.radarChart || {})
      }
    }
  },

  beforeDestroy() {
    const ids = ['performanceRadarChart', 'efficiencyChart', 'accuracyChart']
    ids.forEach(id => {
      const dom = document.getElementById(id)
      if (dom) {
        const inst = echarts.getInstanceByDom(dom)
        if (inst) inst.dispose()
      }
    })
  },

  methods: {
    // 获取绩效结果
    async getPerformanceResults() {
      this.loading = true
      try {
        const params = {
          pageNum: this.queryParams.pageNum,
          pageSize: this.queryParams.pageSize,
          budgetType: this.performanceForm.budgetType || undefined,
          businessType: this.performanceForm.businessType || undefined,
          executionUser: this.performanceForm.executionUser || undefined,
          startDate: this.performanceForm.dateRange && this.performanceForm.dateRange[0] ? this.performanceForm.dateRange[0] : undefined,
          endDate: this.performanceForm.dateRange && this.performanceForm.dateRange[1] ? this.performanceForm.dateRange[1] : undefined
        }
        const response = await budgetAnalysisApi.getExecutionPage(params)
        if (response.code === 1) {
          this.performanceResults = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取绩效分析结果失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 初始化图表 —— 直接加载数据并渲染
    initCharts() {
      this.$nextTick(() => {
        this.loadPerformanceChartData()
      })
    },

    // 加载统计数据
    async loadPerformanceStats() {
      try {
        const response = await budgetAnalysisApi.getPerformanceStats()
        if (response.code === 1 && response.data) {
          this.performanceStats = { ...this.performanceStats, ...response.data }
        }
      } catch (error) {
        console.error('加载绩效统计数据失败：', error)
      }
    },

    // 加载绩效排名
    async loadPerformanceRanking() {
      try {
        const response = await budgetAnalysisApi.getPerformanceRanking()
        if (response.code === 1 && response.data) {
          this.performanceRanking = response.data.rankings || response.data || []
        }
      } catch (error) {
        console.error('加载绩效排名失败：', error)
      }
    },

    // 加载改进建议
    async loadImprovementSuggestions() {
      try {
        const response = await budgetAnalysisApi.getImprovementSuggestions()
        if (response.code === 1 && response.data) {
          this.improvementSuggestions = response.data.suggestions || response.data || []
        }
      } catch (error) {
        console.error('加载改进建议失败：', error)
      }
    },

    // 加载图表数据（缓存 + 渲染）
    async loadPerformanceChartData() {
      try {
        const response = await budgetAnalysisApi.getPerformanceChartData(this.performanceForm)
        if (response.code === 1 && response.data) {
          this.cachedChartData = response.data
          this.renderRadarChart(response.data.radarChart || {})
          this.renderEfficiencyChart(response.data.efficiencyChart || {})
          this.renderAccuracyChart(response.data.accuracyChart || {})
        }
      } catch (error) {
        console.error('加载绩效图表数据失败：', error)
      }
    },

    /**
     * 渲染雷达图 —— 根据 radarChartType 决定 current / comparison / trend
     */
    renderRadarChart(rd) {
      const dom = document.getElementById('performanceRadarChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const radarData = rd.data || []
      const currentSeries = radarData.find(s => s.name === '当期绩效') || {}
      const targetSeries = radarData.find(s => s.name === '目标绩效') || {}
      const currentValues = currentSeries.value || []
      const targetValues = targetSeries.value || []
      const indicators = [
        { name: '执行效率', max: 100 },
        { name: '执行准确性', max: 100 },
        { name: '及时性', max: 100 },
        { name: '合规性', max: 100 },
        { name: '成本控制', max: 100 },
        { name: '质量', max: 100 }
      ]
      let option = {}

      if (this.radarChartType === 'current') {
        option = {
          title: { text: '当期绩效雷达图', left: 'center' },
          tooltip: {},
          legend: { data: ['当期绩效', '目标绩效'], top: 30 },
          radar: { indicator: indicators },
          series: [{
            name: '绩效分析',
            type: 'radar',
            data: [
              { value: currentValues, name: '当期绩效', itemStyle: { color: '#409EFF' } },
              { value: targetValues, name: '目标绩效', itemStyle: { color: '#67C23A' }, lineStyle: { type: 'dashed' } }
            ]
          }]
        }
      } else if (this.radarChartType === 'comparison') {
        // 对比分析：当期绩效 vs 目标绩效 柱状图
        const dimNames = indicators.map(i => i.name)
        option = {
          title: { text: '绩效对比分析', left: 'center' },
          tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
          legend: { data: ['当期绩效', '目标绩效'], top: 30 },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: dimNames },
          yAxis: { type: 'value', name: '得分', max: 100 },
          series: [
            { name: '当期绩效', type: 'bar', data: currentValues, itemStyle: { color: '#409EFF' } },
            { name: '目标绩效', type: 'bar', data: targetValues, itemStyle: { color: '#67C23A' } }
          ]
        }
      } else if (this.radarChartType === 'trend') {
        // 趋势分析：用当期各维度值画折线，展示维度间的趋势走向
        const dimNames = indicators.map(i => i.name)
        option = {
          title: { text: '绩效趋势分析', left: 'center' },
          tooltip: { trigger: 'axis' },
          legend: { data: ['当期绩效', '目标绩效'], top: 30 },
          grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
          xAxis: { type: 'category', data: dimNames, boundaryGap: false },
          yAxis: { type: 'value', name: '得分', max: 100 },
          series: [
            { name: '当期绩效', type: 'line', data: currentValues, smooth: true, itemStyle: { color: '#409EFF' }, areaStyle: { opacity: 0.2 } },
            { name: '目标绩效', type: 'line', data: targetValues, smooth: true, itemStyle: { color: '#67C23A' }, lineStyle: { type: 'dashed' } }
          ]
        }
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染效率趋势图
     */
    renderEfficiencyChart(ed) {
      const dom = document.getElementById('efficiencyChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '执行效率趋势', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ed.xAxis || [] },
        yAxis: { type: 'value', name: '效率(%)', max: 100 },
        series: [{
          name: '执行效率',
          type: 'line',
          data: ed.data || [],
          itemStyle: { color: '#409EFF' },
          smooth: true,
          areaStyle: { opacity: 0.3 }
        }]
      }
      chart.setOption(option, true)
      chart.resize()
    },

    /**
     * 渲染准确性饼图
     */
    renderAccuracyChart(ad) {
      const dom = document.getElementById('accuracyChart')
      if (!dom) return
      const chart = echarts.getInstanceByDom(dom) || echarts.init(dom)
      const option = {
        title: { text: '准确性分析', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item' },
        series: [{
          name: '准确性分布',
          type: 'pie',
          radius: ['40%', '70%'],
          data: ad.data || [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      chart.setOption(option, true)
      chart.resize()
    },
    
    // 分析绩效
    handleAnalyzePerformance() {
      this.queryParams.pageNum = 1
      this.getPerformanceResults()
      this.initCharts()
    },
    
    // 重置
    handleReset() {
      this.performanceForm = {
        budgetType: '',
        businessType: '',
        executionUser: '',
        dateRange: []
      }
      this.handleAnalyzePerformance()
    },

    // 重置条件
    handleResetConditions() {
      this.handleReset()
    },

    // 显示详细指标切换
    handleShowDetailedMetricsChange(value) {
      this.$message.info(value ? '已显示详细指标' : '已隐藏详细指标')
    },

    // 显示改进建议切换
    handleShowSuggestionsChange(value) {
      this.$message.info(value ? '已显示改进建议' : '已隐藏改进建议')
    },

    // 创建绩效分析
    handleCreatePerformanceAnalysis() {
      this.editForm = {
        executionId: '',
        budgetType: '',
        businessType: '',
        amount: 0,
        budgetId: '',
        executionUserName: '',
        remark: ''
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editFormRef && this.$refs.editFormRef.clearValidate()
      })
    },

    // 刷新绩效
    handleRefreshPerformance() {
      this.getPerformanceResults()
      this.initCharts()
    },

    // 导出绩效
    async handleExportPerformance() {
      try {
        const params = {
          budgetType: this.performanceForm.budgetType || undefined,
          businessType: this.performanceForm.businessType || undefined,
          executionUser: this.performanceForm.executionUser || undefined,
          startDate: this.performanceForm.dateRange && this.performanceForm.dateRange[0] ? this.performanceForm.dateRange[0] : undefined,
          endDate: this.performanceForm.dateRange && this.performanceForm.dateRange[1] ? this.performanceForm.dateRange[1] : undefined
        }
        const response = await budgetAnalysisApi.exportPerformanceAnalysis(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '绩效分析_' + new Date().toISOString().slice(0, 10) + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 绩效报告
    async handlePerformanceReport() {
      this.reportDialogVisible = true
      this.reportLoading = true
      this.reportData = null
      try {
        const response = await budgetAnalysisApi.generateExecutionReport(this.performanceForm)
        if (response.code === 1 && response.data) {
          this.reportData = response.data
        } else {
          this.$message.warning(response.msg || '生成报告失败')
        }
      } catch (error) {
        this.$message.error('生成报告失败：' + error.message)
      } finally {
        this.reportLoading = false
      }
    },
    
    // 绩效设置
    handlePerformanceSettings() {
      this.settingsDialogVisible = true
    },
    
    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新排名
    refreshRanking() {
      this.loadPerformanceRanking()
    },
    
    // 刷新效率图表
    refreshEfficiencyChart() {
      this.loadPerformanceChartData()
    },

    // 刷新准确性图表
    refreshAccuracyChart() {
      this.loadPerformanceChartData()
    },
    
    // 编辑建议（刷新改进建议数据）
    handleEditSuggestions() {
      this.loadImprovementSuggestions()
      this.$message.success('改进建议已刷新')
    },
    
    // 查看详情
    handleViewDetail(row) {
      this.currentPerformanceDetail = row
      this.detailDialogVisible = true
    },

    // 编辑绩效
    handleEditPerformance(row) {
      this.editForm = {
        executionId: row.executionId || '',
        budgetType: row.budgetType || '',
        businessType: row.businessType || '',
        amount: row.amount || 0,
        budgetId: row.budgetId || '',
        executionUserName: row.executionUserName || '',
        remark: row.remark || ''
      }
      this.editDialogVisible = true
      this.$nextTick(() => {
        this.$refs.editFormRef && this.$refs.editFormRef.clearValidate()
      })
    },

    // 提交编辑/创建表单
    submitEditForm() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const isEdit = !!this.editForm.executionId
          const response = isEdit
            ? await budgetAnalysisApi.updateExecution(this.editForm)
            : await budgetAnalysisApi.createExecution(this.editForm)
          if (response.code === 1) {
            this.$message.success(isEdit ? '更新成功' : '创建成功')
            this.editDialogVisible = false
            this.getPerformanceResults()
            this.loadPerformanceStats()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'improvement':
          this.handleImprovementPlan(row)
          break
        case 'benchmark':
          this.handleBenchmarkComparison(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },

    // 改进计划 - 查看该记录的分析数据
    async handleImprovementPlan(row) {
      try {
        const response = await budgetAnalysisApi.getExecutionDetail(row.executionId)
        if (response.code === 1 && response.data) {
          this.currentPerformanceDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.warning('暂无改进计划数据')
        }
      } catch (error) {
        this.$message.error('获取改进计划失败：' + error.message)
      }
    },

    // 标杆对比 - 查看该记录详情
    async handleBenchmarkComparison(row) {
      try {
        const response = await budgetAnalysisApi.getExecutionDetail(row.executionId)
        if (response.code === 1 && response.data) {
          this.currentPerformanceDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.warning('暂无对比数据')
        }
      } catch (error) {
        this.$message.error('获取对比数据失败：' + error.message)
      }
    },

    // 导出单个
    async handleExportSingle(row) {
      try {
        const response = await budgetAnalysisApi.exportSinglePerformance(row.executionId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '执行记录_' + row.executionId + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 排序改变
    handleSortChange({ prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getPerformanceResults()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getPerformanceResults()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getPerformanceResults()
    },
    
    // 获取得分颜色
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取得分样式类
    getScoreClass(score) {
      if (score >= 90) return 'excellent-score'
      if (score >= 80) return 'good-score'
      if (score >= 70) return 'average-score'
      return 'poor-score'
    },
    
    // 获取得分类型
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'primary'
      if (score >= 70) return 'warning'
      return 'danger'
    },
    
    // 获取得分等级
    getScoreLevel(score) {
      if (score >= 90) return '优秀'
      if (score >= 80) return '良好'
      if (score >= 70) return '一般'
      return '较差'
    },
    
    // 获取指标样式类
    getMetricClass(value) {
      if (value >= 90) return 'excellent-metric'
      if (value >= 80) return 'good-metric'
      if (value >= 70) return 'average-metric'
      return 'poor-metric'
    },
    
    // 获取排名样式类
    getRankingClass(index) {
      if (index === 0) return 'first-place'
      if (index === 1) return 'second-place'
      if (index === 2) return 'third-place'
      return 'other-place'
    },
    
    // 获取排名文本样式类
    getRankingTextClass(ranking) {
      if (ranking <= 3) return 'top-ranking'
      if (ranking <= 10) return 'good-ranking'
      return 'normal-ranking'
    },
    
    // 获取绩效等级颜色
    getPerformanceLevelColor(level) {
      const colorMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取绩效等级文本
    getPerformanceLevelText(level) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return textMap[level] || level
    },
    
    // 获取优先级颜色
    getPriorityColor(priority) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[priority] || 'info'
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

    // 获取预算类型文本
    getBudgetTypeText(type) {
      const textMap = {
        'DEPT_MONTH': '部门月度',
        'DEPT_YEAR': '部门年度',
        'PROJECT': '项目预算',
        'EXPENSE_TYPE': '费用类型'
      }
      return textMap[type] || type || '-'
    },

    // 获取预算类型颜色
    getBudgetTypeColor(type) {
      const colorMap = {
        'DEPT_MONTH': 'primary',
        'DEPT_YEAR': 'success',
        'PROJECT': 'warning',
        'EXPENSE_TYPE': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取业务类型文本
    getBusinessTypeText(type) {
      const textMap = {
        'PAYMENT': '付款',
        'REIMBURSEMENT': '报销',
        'CONTRACT': '合同',
        'PURCHASE': '采购'
      }
      return textMap[type] || type || '-'
    },

    // 格式化金额
    formatAmount(amount) {
      if (amount == null) return '-'
      return '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      if (typeof time === 'string') {
        return time.replace('T', ' ').substring(0, 19)
      }
      return time
    }
  }
}
</script>

<style lang="scss" scoped>
.performance-analysis {
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
      
      &.overall-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.efficiency-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.accuracy-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.improvement-card {
        background: linear-gradient(135deg, #909399, #B3B6BC);
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
        
        .stat-progress {
          margin-top: 8px;
        }
        
        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
          
          .trend-up {
            color: #F56C6C;
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
  
  .dashboard-row,
  .metrics-row {
    margin-bottom: 20px;
    
    .chart-card,
    .ranking-card {
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
    
    .ranking-card {
      .ranking-list {
        max-height: 350px;
        overflow-y: auto;
        
        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px;
          margin-bottom: 8px;
          border-radius: 6px;
          transition: all 0.3s;
          
          &:hover {
            background-color: #f5f7fa;
          }
          
          &.first-place {
            background: linear-gradient(135deg, #FFD700, #FFA500);
            color: white;
          }
          
          &.second-place {
            background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
            color: white;
          }
          
          &.third-place {
            background: linear-gradient(135deg, #CD7F32, #B8860B);
            color: white;
          }
          
          .ranking-number {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background-color: rgba(255, 255, 255, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            margin-right: 12px;
          }
          
          .ranking-content {
            flex: 1;
            
            .ranking-name {
              font-size: 14px;
              font-weight: 500;
              margin-bottom: 4px;
            }
            
            .ranking-score {
              font-size: 12px;
              opacity: 0.8;
            }
          }
          
          .ranking-badge {
            margin-left: 12px;
          }
        }
      }
    }
    
    .suggestions-content {
      max-height: 350px;
      overflow-y: auto;
      
      .suggestion-item {
        padding: 12px;
        margin-bottom: 8px;
        border: 1px solid #EBEEF5;
        border-radius: 4px;
        
        .suggestion-priority {
          margin-bottom: 8px;
        }
        
        .suggestion-content {
          .suggestion-title {
            font-size: 14px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .suggestion-description {
            font-size: 12px;
            color: #606266;
            line-height: 1.5;
          }
        }
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
  
  .score-display {
    .score-number {
      display: block;
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 4px;
      
      &.excellent-score { color: #67C23A; }
      &.good-score { color: #409EFF; }
      &.average-score { color: #E6A23C; }
      &.poor-score { color: #F56C6C; }
    }
  }
  
  .excellent-metric { color: #67C23A; font-weight: 500; }
  .good-metric { color: #409EFF; font-weight: 500; }
  .average-metric { color: #E6A23C; font-weight: 500; }
  .poor-metric { color: #F56C6C; font-weight: 500; }
  
  .ranking-text {
    font-weight: 600;
    
    &.top-ranking { color: #67C23A; }
    &.good-ranking { color: #409EFF; }
    &.normal-ranking { color: #909399; }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .amount-text {
    color: #E6A23C;
    font-weight: 600;
  }

  .text-right {
    text-align: right;
  }
  
  .detail-content {
    padding: 20px;
    
    .metrics-detail,
    .improvement-content {
      margin-top: 20px;
    }
    
    .suggestion-list {
      .suggestion-detail-item {
        padding: 16px;
        margin-bottom: 12px;
        border: 1px solid #EBEEF5;
        border-radius: 6px;
        
        .suggestion-header {
          display: flex;
          align-items: center;
          margin-bottom: 8px;
          
          .suggestion-title {
            margin-left: 12px;
            font-size: 16px;
            font-weight: 500;
            color: #303133;
          }
        }
        
        .suggestion-description {
          color: #606266;
          line-height: 1.6;
          margin-bottom: 12px;
        }
        
        .suggestion-actions {
          display: flex;
          justify-content: space-between;
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
}
</style>
