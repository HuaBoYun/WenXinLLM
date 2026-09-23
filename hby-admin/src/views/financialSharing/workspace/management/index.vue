<template>
  <div class="management-workspace">
    <!-- 关键指标 -->
    <el-row :gutter="20" class="kpi-cards">
      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon expense">
              <i class="el-icon-money"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">¥{{ formatAmount(kpiData.totalExpense) }}</div>
              <div class="kpi-label">本月总支出</div>
              <div class="kpi-trend" :class="kpiData.expenseTrend > 0 ? 'trend-up' : 'trend-down'">
                <i :class="kpiData.expenseTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(kpiData.expenseTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon budget">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ kpiData.budgetUsage }}%</div>
              <div class="kpi-label">预算执行率</div>
              <div class="kpi-trend" :class="kpiData.budgetTrend > 0 ? 'trend-up' : 'trend-down'">
                <i :class="kpiData.budgetTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(kpiData.budgetTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon efficiency">
              <i class="el-icon-timer"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ kpiData.avgProcessTime }}天</div>
              <div class="kpi-label">平均处理时间</div>
              <div class="kpi-trend" :class="kpiData.timeTrend < 0 ? 'trend-up' : 'trend-down'">
                <i :class="kpiData.timeTrend < 0 ? 'el-icon-bottom' : 'el-icon-top'"></i>
                {{ Math.abs(kpiData.timeTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="kpi-card">
          <div class="kpi-content">
            <div class="kpi-icon compliance">
              <i class="el-icon-shield"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-number">{{ kpiData.complianceRate }}%</div>
              <div class="kpi-label">合规率</div>
              <div class="kpi-trend" :class="kpiData.complianceTrend > 0 ? 'trend-up' : 'trend-down'">
                <i :class="kpiData.complianceTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(kpiData.complianceTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="main-content">
      <!-- 左侧内容 -->
      <el-col :span="16">
        <!-- 部门费用排行 -->
        <el-card class="department-ranking" title="部门费用排行">
          <div slot="header" class="card-header">
            <span>部门费用排行</span>
            <el-date-picker
              v-model="rankingPeriod"
              type="month"
              placeholder="选择月份"
              size="small"
              @change="loadDepartmentRanking"
            />
          </div>
          <div class="ranking-list">
            <div 
              v-for="(dept, index) in departmentRanking" 
              :key="dept.deptId"
              class="ranking-item"
            >
              <div class="ranking-number" :class="'rank-' + (index + 1)">
                {{ index + 1 }}
              </div>
              <div class="ranking-info">
                <div class="dept-name">{{ dept.deptName }}</div>
                <div class="dept-details">
                  <span class="expense-amount">¥{{ formatAmount(dept.totalExpense) }}</span>
                  <span class="bill-count">{{ dept.billCount }}笔</span>
                </div>
              </div>
              <div class="ranking-progress">
                <el-progress 
                  :percentage="(dept.totalExpense / departmentRanking[0].totalExpense) * 100"
                  :stroke-width="8"
                  :show-text="false"
                  :color="getProgressColor(index)"
                />
              </div>
            </div>
          </div>
        </el-card>

        <!-- 费用趋势分析 -->
        <el-card class="expense-trend" title="费用趋势分析">
          <div slot="header" class="card-header">
            <span>费用趋势分析</span>
            <el-radio-group v-model="trendPeriod" size="small" @change="loadExpenseTrend">
              <el-radio-button label="7">近7天</el-radio-button>
              <el-radio-button label="30">近30天</el-radio-button>
              <el-radio-button label="90">近90天</el-radio-button>
            </el-radio-group>
          </div>
          <div class="trend-chart" ref="trendChart" style="height: 300px;"></div>
        </el-card>

        <!-- 异常单据监控 -->
        <el-card class="exception-monitor" title="异常单据监控">
          <div slot="header" class="card-header">
            <span>异常单据监控</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllExceptions">查看全部</el-button>
          </div>
          <el-table :data="exceptionBills" style="width: 100%">
            <el-table-column prop="billNumber" label="单据号" width="150" />
            <el-table-column prop="billTypeName" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag :type="getBillTypeColor(scope.row.billType)" size="small">
                  {{ scope.row.billTypeName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="applicant" label="申请人" width="100" />
            <el-table-column prop="amount" label="金额" width="120">
              <template slot-scope="scope">
                <span class="amount">¥{{ formatAmount(scope.row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="exceptionTypeName" label="异常类型" width="120">
              <template slot-scope="scope">
                <el-tag :type="getExceptionColor(scope.row.exceptionType)" size="small">
                  {{ scope.row.exceptionTypeName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="exceptionReason" label="异常原因" min-width="200" show-overflow-tooltip />
            <el-table-column prop="createTime" label="发现时间" width="160">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleException(scope.row)">处理</el-button>
                <el-button size="mini" type="text" @click="viewExceptionDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :span="8">
        <!-- 预算执行情况 -->
        <el-card class="budget-execution" title="预算执行情况">
          <div class="budget-overview">
            <div class="budget-item">
              <div class="budget-label">年度预算</div>
              <div class="budget-value">¥{{ formatAmount(budgetData.annualBudget) }}</div>
            </div>
            <div class="budget-item">
              <div class="budget-label">已使用</div>
              <div class="budget-value used">¥{{ formatAmount(budgetData.usedBudget) }}</div>
            </div>
            <div class="budget-item">
              <div class="budget-label">剩余预算</div>
              <div class="budget-value remaining">¥{{ formatAmount(budgetData.remainingBudget) }}</div>
            </div>
          </div>
          <div class="budget-progress">
            <el-progress 
              :percentage="budgetData.usagePercentage"
              :stroke-width="12"
              :color="getBudgetProgressColor(budgetData.usagePercentage)"
            />
          </div>
          <div class="budget-categories">
            <div 
              v-for="category in budgetCategories" 
              :key="category.categoryId"
              class="category-item"
            >
              <div class="category-info">
                <span class="category-name">{{ category.categoryName }}</span>
                <span class="category-percentage">{{ category.usagePercentage }}%</span>
              </div>
              <el-progress 
                :percentage="category.usagePercentage"
                :stroke-width="6"
                :show-text="false"
                :color="getBudgetProgressColor(category.usagePercentage)"
              />
            </div>
          </div>
        </el-card>

        <!-- 审批效率 -->
        <el-card class="approval-efficiency" title="审批效率">
          <div class="efficiency-stats">
            <div class="efficiency-item">
              <div class="efficiency-icon">
                <i class="el-icon-check"></i>
              </div>
              <div class="efficiency-info">
                <div class="efficiency-value">{{ approvalStats.avgApprovalTime }}小时</div>
                <div class="efficiency-label">平均审批时间</div>
              </div>
            </div>
            <div class="efficiency-item">
              <div class="efficiency-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="efficiency-info">
                <div class="efficiency-value">{{ approvalStats.approvalRate }}%</div>
                <div class="efficiency-label">一次通过率</div>
              </div>
            </div>
            <div class="efficiency-item">
              <div class="efficiency-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="efficiency-info">
                <div class="efficiency-value">{{ approvalStats.overtimeCount }}</div>
                <div class="efficiency-label">超时单据</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 风险提醒 -->
        <el-card class="risk-alerts" title="风险提醒">
          <div v-if="riskAlerts.length === 0" class="empty-risks">
            <i class="el-icon-success"></i>
            <p>暂无风险提醒</p>
          </div>
          <div v-else class="risk-items">
            <div 
              v-for="risk in riskAlerts" 
              :key="risk.riskId"
              class="risk-item"
              :class="'risk-' + risk.riskLevel.toLowerCase()"
              @click="handleRisk(risk)"
            >
              <div class="risk-icon">
                <i :class="getRiskIcon(risk.riskLevel)"></i>
              </div>
              <div class="risk-content">
                <div class="risk-title">{{ risk.riskTitle }}</div>
                <div class="risk-description">{{ risk.riskDescription }}</div>
                <div class="risk-time">{{ formatDate(risk.createTime) }}</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 快速决策 -->
        <el-card class="quick-decisions" title="快速决策">
          <div class="decision-grid">
            <div class="decision-item" @click="viewBudgetAnalysis">
              <div class="decision-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="decision-name">预算分析</div>
            </div>
            <div class="decision-item" @click="viewCostControl">
              <div class="decision-icon">
                <i class="el-icon-price-tag"></i>
              </div>
              <div class="decision-name">成本控制</div>
            </div>
            <div class="decision-item" @click="viewPerformanceReport">
              <div class="decision-icon">
                <i class="el-icon-data-board"></i>
              </div>
              <div class="decision-name">绩效报告</div>
            </div>
            <div class="decision-item" @click="viewRiskAssessment">
              <div class="decision-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="decision-name">风险评估</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { workspaceApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'ManagementWorkspace',
  data() {
    return {
      loading: false,
      kpiData: {},
      departmentRanking: [],
      exceptionBills: [],
      budgetData: {},
      budgetCategories: [],
      approvalStats: {},
      riskAlerts: [],
      rankingPeriod: new Date(),
      trendPeriod: '30'
    }
  },
  mounted() {
    this.loadWorkspaceData()
    this.initTrendChart()
  },
  methods: {
    async loadWorkspaceData() {
      this.loading = true
      try {
        const userId = this.$store.getters.userId || 'USER001'
        const response = await workspaceApi.getManagementWorkspace(userId)
        if (response.code === 1) {
          const data = response.data
          this.kpiData = data.kpiData || {}
          this.departmentRanking = data.departmentRanking || []
          this.exceptionBills = data.exceptionBills || []
          this.budgetData = data.budgetData || {}
          this.budgetCategories = data.budgetCategories || []
          this.approvalStats = data.approvalStats || {}
          this.riskAlerts = data.riskAlerts || []
        } else {
          this.$message.error(response.msg || '加载工作台数据失败')
        }
      } catch (error) {
        this.$message.error('加载工作台数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadDepartmentRanking() {
      // 加载部门排行数据
      console.log('加载部门排行数据', this.rankingPeriod)
    },

    async loadExpenseTrend() {
      // 加载费用趋势数据
      console.log('加载费用趋势数据', this.trendPeriod)
      this.updateTrendChart()
    },

    initTrendChart() {
      // 初始化趋势图表
      // 这里可以使用 ECharts 或其他图表库
      console.log('初始化趋势图表')
    },

    updateTrendChart() {
      // 更新趋势图表
      console.log('更新趋势图表')
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },

    getBillTypeColor(billType) {
      const colors = {
        'EXPENSE': 'primary',
        'LOAN': 'success',
        'PREPAYMENT': 'warning',
        'PROVISION': 'info'
      }
      return colors[billType] || 'default'
    },

    getExceptionColor(exceptionType) {
      const colors = {
        'AMOUNT_EXCEED': 'danger',
        'DUPLICATE_BILL': 'warning',
        'INVALID_RECEIPT': 'info',
        'POLICY_VIOLATION': 'danger'
      }
      return colors[exceptionType] || 'default'
    },

    getProgressColor(index) {
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      return colors[index] || '#909399'
    },

    getBudgetProgressColor(percentage) {
      if (percentage >= 90) return '#F56C6C'
      if (percentage >= 80) return '#E6A23C'
      return '#67C23A'
    },

    getRiskIcon(level) {
      const icons = {
        'HIGH': 'el-icon-warning',
        'MEDIUM': 'el-icon-info',
        'LOW': 'el-icon-question'
      }
      return icons[level] || 'el-icon-info'
    },

    handleException(exception) {
      // 处理异常单据
      this.$router.push({
        path: '/financialSharing/exception/handle',
        query: { id: exception.billId, type: exception.exceptionType }
      })
    },

    viewExceptionDetail(exception) {
      // 查看异常详情
      this.$router.push({
        path: '/financialSharing/exception/detail',
        query: { id: exception.billId }
      })
    },

    handleRisk(risk) {
      // 处理风险提醒
      this.$alert(risk.riskDescription, risk.riskTitle, {
        confirmButtonText: '确定',
        type: risk.riskLevel === 'HIGH' ? 'error' : 'warning'
      })
    },

    viewBudgetAnalysis() {
      this.$router.push('/financialSharing/analysis/budget')
    },

    viewCostControl() {
      this.$router.push('/financialSharing/analysis/cost')
    },

    viewPerformanceReport() {
      this.$router.push('/financialSharing/reports/performance')
    },

    viewRiskAssessment() {
      this.$router.push('/financialSharing/analysis/risk')
    },

    viewAllExceptions() {
      this.$router.push('/financialSharing/exception/list')
    }
  }
}
</script>

<style scoped>
.management-workspace {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.kpi-cards {
  margin-bottom: 20px;
}

.kpi-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.kpi-content {
  display: flex;
  align-items: center;
}

.kpi-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.kpi-icon.expense {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.kpi-icon.budget {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.kpi-icon.efficiency {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.kpi-icon.compliance {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.kpi-info {
  flex: 1;
}

.kpi-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.kpi-label {
  font-size: 14px;
  color: #909399;
  margin: 5px 0;
}

.kpi-trend {
  font-size: 12px;
  font-weight: bold;
}

.kpi-trend.trend-up {
  color: #67C23A;
}

.kpi-trend.trend-down {
  color: #F56C6C;
}

.main-content {
  margin-top: 20px;
}

.department-ranking,
.expense-trend,
.exception-monitor,
.budget-execution,
.approval-efficiency,
.risk-alerts,
.quick-decisions {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ranking-list {
  max-height: 400px;
  overflow-y: auto;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #EBEEF5;
}

.ranking-item:last-child {
  border-bottom: none;
}

.ranking-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: white;
  margin-right: 15px;
}

.ranking-number.rank-1 {
  background: linear-gradient(135deg, #FFD700, #FFA500);
}

.ranking-number.rank-2 {
  background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
}

.ranking-number.rank-3 {
  background: linear-gradient(135deg, #CD7F32, #B8860B);
}

.ranking-number:not(.rank-1):not(.rank-2):not(.rank-3) {
  background: #909399;
}

.ranking-info {
  flex: 1;
  margin-right: 15px;
}

.dept-name {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.dept-details {
  font-size: 14px;
  color: #606266;
}

.expense-amount {
  font-weight: bold;
  color: #E6A23C;
  margin-right: 15px;
}

.bill-count {
  color: #909399;
}

.ranking-progress {
  width: 120px;
}

.amount {
  font-weight: bold;
  color: #E6A23C;
}

.budget-overview {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.budget-item {
  text-align: center;
}

.budget-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.budget-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.budget-value.used {
  color: #E6A23C;
}

.budget-value.remaining {
  color: #67C23A;
}

.budget-progress {
  margin-bottom: 20px;
}

.budget-categories {
  max-height: 200px;
  overflow-y: auto;
}

.category-item {
  margin-bottom: 15px;
}

.category-item:last-child {
  margin-bottom: 0;
}

.category-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.category-name {
  font-size: 14px;
  color: #303133;
}

.category-percentage {
  font-size: 14px;
  font-weight: bold;
  color: #606266;
}

.efficiency-stats {
  padding: 20px 0;
}

.efficiency-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.efficiency-item:last-child {
  margin-bottom: 0;
}

.efficiency-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409EFF;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin-right: 15px;
}

.efficiency-info {
  flex: 1;
}

.efficiency-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.efficiency-label {
  font-size: 14px;
  color: #909399;
}

.empty-risks {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-risks i {
  font-size: 48px;
  margin-bottom: 10px;
  color: #67C23A;
}

.risk-items {
  max-height: 300px;
  overflow-y: auto;
}

.risk-item {
  display: flex;
  align-items: flex-start;
  padding: 15px;
  border-left: 4px solid #E6A23C;
  margin-bottom: 10px;
  background-color: #fdf6ec;
  cursor: pointer;
  transition: all 0.3s;
}

.risk-item:hover {
  background-color: #faecd8;
}

.risk-item.risk-high {
  border-left-color: #F56C6C;
  background-color: #fef0f0;
}

.risk-item.risk-high:hover {
  background-color: #fde2e2;
}

.risk-icon {
  margin-right: 15px;
  font-size: 20px;
  color: #E6A23C;
  margin-top: 2px;
}

.risk-high .risk-icon {
  color: #F56C6C;
}

.risk-content {
  flex: 1;
}

.risk-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.risk-description {
  font-size: 13px;
  color: #606266;
  margin-bottom: 5px;
  line-height: 1.4;
}

.risk-time {
  font-size: 12px;
  color: #909399;
}

.decision-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.decision-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.decision-item:hover {
  border-color: #409EFF;
  background-color: #f0f9ff;
}

.decision-icon {
  font-size: 32px;
  color: #409EFF;
  margin-bottom: 10px;
}

.decision-name {
  font-size: 14px;
  color: #303133;
}
</style>
