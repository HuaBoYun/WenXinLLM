<template>
  <div class="budget-execution">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算执行监控</h2>
      <p>实时监控预算执行情况，提供执行分析和异常预警</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefreshData">刷新数据</el-button>
            <el-button type="success" icon="el-icon-s-data" @click="handleAnalyzeExecution">执行分析</el-button>
            <el-button type="warning" icon="el-icon-warning" @click="handleCheckWarnings">异常检查</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportReport">导出报告</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleMonitorSettings">监控设置</el-button>
            <el-button icon="el-icon-view" @click="handleDashboard">监控大屏</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 执行概览统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card execution-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ executionStats.totalCount }}</div>
            <div class="stat-label">执行记录数</div>
            <div class="stat-unit">条</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-order"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card actual-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(executionStats.totalAmount) }}</div>
            <div class="stat-label">执行总金额</div>
            <div class="stat-unit">元</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-finance"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ executionStats.typeCount }}</div>
            <div class="stat-label">预算类型数</div>
            <div class="stat-unit">种</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-pie-chart"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card variance-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ formatAmount(executionStats.avgAmount) }}</div>
            <div class="stat-label">平均执行金额</div>
            <div class="stat-unit">元</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-line"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预算类型">
          <el-select
            v-model="queryForm.budgetType"
            placeholder="请选择预算类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in budgetTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型">
          <el-select
            v-model="queryForm.businessType"
            placeholder="请选择业务类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in businessTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="执行人">
          <el-input
            v-model="queryForm.executionUserName"
            placeholder="请输入执行人"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="预算ID">
          <el-input
            v-model="queryForm.budgetId"
            placeholder="请输入预算ID"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 执行监控列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算执行监控列表</span>
        <div class="table-tools">
          <el-tooltip content="自动刷新" placement="top">
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="handleAutoRefreshChange"
            />
          </el-tooltip>
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
        :data="executionList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />

        <el-table-column v-if="columnVisible.executionId" prop="executionId" label="执行ID" width="120" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.ruleId" prop="ruleId" label="规则ID" width="120" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.budgetType" prop="budgetType" label="预算类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getBudgetTypeColor(scope.row.budgetType)">
              {{ getBudgetTypeText(scope.row.budgetType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.budgetId" prop="budgetId" label="预算ID" width="120" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.businessType" prop="businessType" label="业务类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">
              {{ getBusinessTypeText(scope.row.businessType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.businessId" prop="businessId" label="业务ID" width="120" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.amount" prop="amount" label="执行金额" width="130" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.executionTime" prop="executionTime" label="执行时间" width="170" align="center">
          <template slot-scope="scope">
            {{ formatTime(scope.row.executionTime) }}
          </template>
        </el-table-column>

        <el-table-column v-if="columnVisible.executionUserName" prop="executionUserName" label="执行人" width="100" align="center" />
        <el-table-column v-if="columnVisible.remark" prop="remark" label="备注" min-width="150" show-overflow-tooltip />

        <el-table-column label="操作" width="160" align="center" fixed="right">
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
              icon="el-icon-download"
              @click="handleExportSingle(scope.row)"
            >导出</el-button>
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

    <!-- 执行详情对话框 -->
    <el-dialog
      title="预算执行详情"
      :visible.sync="detailDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div class="execution-detail" v-if="currentExecution">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-section">
              <h4>基本信息</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="执行ID">{{ currentExecution.executionId }}</el-descriptions-item>
                <el-descriptions-item label="规则ID">{{ currentExecution.ruleId }}</el-descriptions-item>
                <el-descriptions-item label="预算类型">
                  <el-tag size="mini" :type="getBudgetTypeColor(currentExecution.budgetType)">
                    {{ getBudgetTypeText(currentExecution.budgetType) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="预算ID">{{ currentExecution.budgetId }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-section">
              <h4>执行数据</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="业务类型">{{ getBusinessTypeText(currentExecution.businessType) }}</el-descriptions-item>
                <el-descriptions-item label="业务ID">{{ currentExecution.businessId }}</el-descriptions-item>
                <el-descriptions-item label="执行金额">
                  <span class="amount-text">{{ formatAmount(currentExecution.amount) }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="执行时间">{{ formatTime(currentExecution.executionTime) }}</el-descriptions-item>
                <el-descriptions-item label="执行人">{{ currentExecution.executionUserName }}</el-descriptions-item>
                <el-descriptions-item label="备注">{{ currentExecution.remark || '-' }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportDetail">导出详情</el-button>
      </div>
    </el-dialog>

    <!-- 执行分析对话框 -->
    <el-dialog
      title="执行分析"
      :visible.sync="analysisDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
    >
      <div class="execution-analysis">
        <el-tabs v-model="analysisTab">
          <el-tab-pane label="趋势分析" name="trend">
            <div id="trendAnalysisChart" class="analysis-chart"></div>
          </el-tab-pane>
          <el-tab-pane label="对比分析" name="compare">
            <div v-if="varianceData" style="margin-bottom: 16px;">
              <el-descriptions :column="3" border size="small">
                <el-descriptions-item label="预算总额">{{ formatAmount(varianceData.budgetAmount) }}</el-descriptions-item>
                <el-descriptions-item label="实际金额">{{ formatAmount(varianceData.actualAmount) }}</el-descriptions-item>
                <el-descriptions-item label="偏差金额">
                  <span :style="{ color: varianceData.variance > 0 ? '#F56C6C' : '#67C23A' }">
                    {{ formatAmount(varianceData.variance) }}
                  </span>
                </el-descriptions-item>
                <el-descriptions-item label="偏差率">{{ varianceData.varianceRate }}%</el-descriptions-item>
                <el-descriptions-item label="偏差类型">
                  <el-tag :type="varianceData.varianceType === '超支' ? 'danger' : 'success'" size="mini">
                    {{ varianceData.varianceType }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
            <div id="compareAnalysisChart" class="analysis-chart"></div>
          </el-tab-pane>
          <el-tab-pane label="进度分析" name="progress">
            <div v-if="progressData">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="预算总额">{{ formatAmount(progressData.budgetTotal) }}</el-descriptions-item>
                <el-descriptions-item label="已执行金额">{{ formatAmount(progressData.totalExecuted) }}</el-descriptions-item>
                <el-descriptions-item label="执行率">
                  <el-progress :percentage="Number(progressData.executionRate) || 0" :color="Number(progressData.executionRate) > 100 ? '#F56C6C' : '#409EFF'" />
                </el-descriptions-item>
                <el-descriptions-item label="剩余预算">{{ formatAmount(progressData.remainingBudget) }}</el-descriptions-item>
                <el-descriptions-item label="执行笔数">{{ progressData.executionCount }} 笔</el-descriptions-item>
              </el-descriptions>
            </div>
            <el-empty v-else description="暂无进度数据" />
          </el-tab-pane>
        </el-tabs>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportAnalysis">导出分析</el-button>
      </div>
    </el-dialog>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="columnSettingVisible" width="400px">
      <el-checkbox-group v-model="columnChecked">
        <el-row :gutter="10">
          <el-col v-for="col in columnOptions" :key="col.key" :span="12">
            <el-checkbox :label="col.key" style="margin-bottom: 8px;">{{ col.label }}</el-checkbox>
          </el-col>
        </el-row>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleResetColumns">重置</el-button>
        <el-button @click="columnSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApplyColumns">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetExecutionApi } from '@/api/managementAccountant/ncv65/budgetControl'
import * as echarts from 'echarts'

export default {
  name: 'BudgetExecution',
  data() {
    return {
      // 查询参数
      queryForm: {
        budgetType: '',
        businessType: '',
        executionUserName: '',
        budgetId: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      executionList: [],
      total: 0,
      selectedRows: [],
      
      // 自动刷新
      autoRefresh: false,
      refreshTimer: null,
      
      // 统计数据
      executionStats: {
        totalCount: 0,
        totalAmount: 0,
        typeCount: 0,
        avgAmount: 0
      },
      
      // 对话框
      detailDialogVisible: false,
      analysisDialogVisible: false,
      currentExecution: null,
      analysisTab: 'trend',
      
      // 分析数据
      trendData: null,
      varianceData: null,
      progressData: null,
      
      // 选项数据
      budgetTypeOptions: [
        { value: 'DEPT_MONTH', label: '部门月度' },
        { value: 'DEPT_YEAR', label: '部门年度' },
        { value: 'PROJECT', label: '项目' },
        { value: 'EXPENSE_TYPE', label: '费用类型' }
      ],
      businessTypeOptions: [
        { value: 'PAYMENT', label: '付款' },
        { value: 'REIMBURSEMENT', label: '报销' },
        { value: 'CONTRACT', label: '合同' },
        { value: 'PURCHASE', label: '采购' }
      ],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'executionId', label: '执行ID' },
        { key: 'ruleId', label: '规则ID' },
        { key: 'budgetType', label: '预算类型' },
        { key: 'budgetId', label: '预算ID' },
        { key: 'businessType', label: '业务类型' },
        { key: 'businessId', label: '业务ID' },
        { key: 'amount', label: '执行金额' },
        { key: 'executionTime', label: '执行时间' },
        { key: 'executionUserName', label: '执行人' },
        { key: 'remark', label: '备注' }
      ],
      columnChecked: ['executionId', 'ruleId', 'budgetType', 'budgetId', 'businessType', 'businessId', 'amount', 'executionTime', 'executionUserName', 'remark'],
      columnVisible: {
        executionId: true, ruleId: true, budgetType: true, budgetId: true,
        businessType: true, businessId: true, amount: true,
        executionTime: true, executionUserName: true, remark: true
      }
    }
  },

  async created() {
    try {
      await this.getList()
      await this.loadStats()
    } catch (error) {
      console.error('页面初始化失败:', error)
      this.$message.warning('数据加载失败，请稍后重试')
    }
  },
  
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetExecutionApi.getStatistics()
        // request.js 将 code:1 转为 200 后 return data，所以 response 就是 {code:1, msg, data}
        const d = (response && response.data) ? response.data : response
        if (d) {
          const totalCount = d.totalCount || 0
          const totalAmount = parseFloat(d.totalAmount) || 0
          const typeStats = d.typeStatistics || []
          this.executionStats = {
            totalCount,
            totalAmount,
            typeCount: typeStats.length,
            avgAmount: totalCount > 0 ? Math.round(totalAmount / totalCount * 100) / 100 : 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetExecutionApi.getPage(params)
        this.executionList = response.data.tlist || response.data.list || []
        this.total = response.data.totalRecord || response.data.total || 0
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
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
        budgetType: '',
        businessType: '',
        executionUserName: '',
        budgetId: ''
      }
      this.handleQuery()
    },
    
    // 刷新数据
    handleRefreshData() {
      this.getList()
      this.$message.success('数据已刷新')
    },
    
    // 自动刷新切换
    handleAutoRefreshChange(value) {
      if (value) {
        this.refreshTimer = setInterval(() => {
          this.getList()
        }, 30000) // 30秒刷新一次
        this.$message.success('已开启自动刷新')
      } else {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
        }
        this.$message.info('已关闭自动刷新')
      }
    },
    
    // 查看详情
    async handleViewDetail(row) {
      this.detailDialogVisible = true
      this.currentExecution = row

      // 加载详细数据
      try {
        const response = await budgetExecutionApi.getDetail(row.executionId)
        if (response.code === 1 && response.data) {
          this.currentExecution = { ...row, ...response.data }
        }
      } catch (error) {
        console.error('获取详情失败：', error)
      }
    },
    
    // 执行分析
    async handleAnalyze(row) {
      this.analysisDialogVisible = true
      const params = { budgetId: row.budgetId }

      try {
        // 并行调用三个分析接口
        const [trendRes, varianceRes, progressRes] = await Promise.all([
          budgetExecutionApi.getTrendAnalysis(params),
          budgetExecutionApi.getVarianceAnalysis(params),
          budgetExecutionApi.getProgressAnalysis(params)
        ])

        if (trendRes.code === 1 && trendRes.data) {
          this.trendData = trendRes.data
        }
        if (varianceRes.code === 1 && varianceRes.data) {
          this.varianceData = varianceRes.data
        }
        if (progressRes.code === 1 && progressRes.data) {
          this.progressData = progressRes.data
        }

        // 初始化图表
        this.$nextTick(() => {
          this.initAnalysisCharts()
        })
      } catch (error) {
        this.$message.error('获取分析数据失败：' + error.message)
      }
    },

    // 执行分析（批量/无选择时分析全部）
    async handleAnalyzeExecution() {
      this.analysisDialogVisible = true
      const params = {}

      try {
        const [trendRes, varianceRes, progressRes] = await Promise.all([
          budgetExecutionApi.getTrendAnalysis(params),
          budgetExecutionApi.getVarianceAnalysis(params),
          budgetExecutionApi.getProgressAnalysis(params)
        ])

        if (trendRes.code === 1 && trendRes.data) {
          this.trendData = trendRes.data
        }
        if (varianceRes.code === 1 && varianceRes.data) {
          this.varianceData = varianceRes.data
        }
        if (progressRes.code === 1 && progressRes.data) {
          this.progressData = progressRes.data
        }

        this.$nextTick(() => {
          this.initAnalysisCharts()
        })
      } catch (error) {
        this.$message.error('执行分析失败：' + error.message)
      }
    },
    
    // 异常检查
    async handleCheckWarnings() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetExecutionApi.checkWarnings(params)
        const warnings = response.data || []

        if (warnings.length === 0) {
          this.$message.success('未发现异常情况')
        } else {
          this.$alert(`发现 ${warnings.length} 项异常，请及时处理`, '异常检查结果', {
            type: 'warning'
          })
        }

        this.getList() // 刷新列表
      } catch (error) {
        this.$message.error('异常检查失败：' + error.message)
      }
    },
    
    // 处理异常
    handleException(row) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/exception',
        query: { executionId: row.executionId }
      })
    },
    
    // 导出报告
    async handleExportReport() {
      try {
        const params = { ...this.queryForm }
        await budgetExecutionApi.exportReport(params)
        this.$message.success('报告导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 监控设置
    handleMonitorSettings() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/monitorSettings')
    },
    
    // 监控大屏
    handleDashboard() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/dashboard')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'monitor':
          this.handleMonitorSetting(row)
          break
        case 'warning':
          this.handleWarningSetting(row)
          break
        case 'history':
          this.handleExecutionHistory(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 监控设置
    handleMonitorSetting(row) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/monitorSetting',
        query: { executionId: row.executionId }
      })
    },

    // 预警设置
    handleWarningSetting(row) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/warningSetting',
        query: { executionId: row.executionId }
      })
    },

    // 执行历史
    handleExecutionHistory(row) {
      this.$router.push({
        path: '/managementAccountant/ncv65/budgetControl/executionHistory',
        query: { executionId: row.executionId }
      })
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetExecutionApi.exportSingle(row.executionId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导出详情
    async handleExportDetail() {
      try {
        await budgetExecutionApi.exportDetail(this.currentExecution.executionId)
        this.$message.success('详情导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导出分析
    async handleExportAnalysis() {
      try {
        await budgetExecutionApi.exportAnalysis(this.selectedRows.map(row => row.executionId))
        this.$message.success('分析报告导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleViewDetail(row)
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
      this.columnChecked = Object.keys(this.columnVisible).filter(k => this.columnVisible[k])
      this.columnSettingVisible = true
    },
    
    // 应用列设置
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },
    
    // 重置列设置
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 获取预算类型文本
    getBudgetTypeText(type) {
      const map = { 'DEPT_MONTH': '部门月度', 'DEPT_YEAR': '部门年度', 'PROJECT': '项目', 'EXPENSE_TYPE': '费用类型' }
      return map[type] || type || '-'
    },

    // 获取预算类型颜色
    getBudgetTypeColor(type) {
      const map = { 'DEPT_MONTH': '', 'DEPT_YEAR': 'success', 'PROJECT': 'warning', 'EXPENSE_TYPE': 'info' }
      return map[type] || 'info'
    },

    // 获取业务类型文本
    getBusinessTypeText(type) {
      const map = { 'PAYMENT': '付款', 'REIMBURSEMENT': '报销', 'CONTRACT': '合同', 'PURCHASE': '采购' }
      return map[type] || type || '-'
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      return time.replace('T', ' ')
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 初始化分析图表
    initAnalysisCharts() {
      this.initTrendAnalysisChart()
      this.initCompareAnalysisChart()
    },

    // 初始化趋势分析图表 — 使用 trendAnalysis 返回的 monthlyTrend
    initTrendAnalysisChart() {
      this.$nextTick(() => {
        const chartDom = document.getElementById('trendAnalysisChart')
        if (!chartDom) return
        const chart = echarts.init(chartDom)

        // 后端返回 { monthlyTrend: { "2026-04": 金额, ... }, totalMonths }
        const monthlyTrend = (this.trendData && this.trendData.monthlyTrend) ? this.trendData.monthlyTrend : {}
        const categories = Object.keys(monthlyTrend).sort()
        const amounts = categories.map(k => parseFloat(monthlyTrend[k]) || 0)

        const option = {
          tooltip: { trigger: 'axis' },
          legend: { data: ['月度执行金额'] },
          xAxis: { type: 'category', data: categories, axisLabel: { rotate: 30 } },
          yAxis: { type: 'value', name: '金额(元)' },
          series: [
            { name: '月度执行金额', type: 'bar', data: amounts, itemStyle: { color: '#409EFF' }, barWidth: '40%' }
          ]
        }
        chart.setOption(option)
      })
    },

    // 初始化对比分析图表 — 使用 varianceAnalysis 返回的 budgetAmount / actualAmount
    initCompareAnalysisChart() {
      this.$nextTick(() => {
        const chartDom = document.getElementById('compareAnalysisChart')
        if (!chartDom) return
        const chart = echarts.init(chartDom)

        const budget = this.varianceData ? parseFloat(this.varianceData.budgetAmount) || 0 : 0
        const actual = this.varianceData ? parseFloat(this.varianceData.actualAmount) || 0 : 0

        const option = {
          tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
          legend: { data: ['预算金额', '实际金额'] },
          xAxis: { type: 'category', data: ['预算 vs 实际'] },
          yAxis: { type: 'value', name: '金额(元)' },
          series: [
            { name: '预算金额', type: 'bar', data: [budget], itemStyle: { color: '#409EFF' }, barWidth: '30%' },
            { name: '实际金额', type: 'bar', data: [actual], itemStyle: { color: '#67C23A' }, barWidth: '30%' }
          ]
        }
        chart.setOption(option)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-execution {
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
      
      &.execution-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.actual-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.rate-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.variance-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
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
        
        .stat-unit {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }
        
        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }
        
        .stat-progress {
          margin-top: 8px;
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
      align-items: center;
      gap: 12px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .positive-variance {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .negative-variance {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #F56C6C;
  }
  
  .positive-text {
    color: #67C23A;
    font-weight: 500;
  }
  
  .negative-text {
    color: #F56C6C;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
    font-weight: 500;
  }
  
  .warning-text {
    color: #E6A23C;
    font-weight: 500;
  }
  
  .danger-text {
    color: #F56C6C;
    font-weight: 500;
  }
  
  .primary-text {
    color: #409EFF;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .execution-detail {
    .detail-section {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        font-size: 16px;
        margin: 0 0 12px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid #EBEEF5;
      }
    }
    
    .detail-chart {
      height: 300px;
    }
  }
  
  .execution-analysis {
    .analysis-chart {
      height: 400px;
    }
    
    .exception-analysis {
      padding: 20px 0;
    }
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
