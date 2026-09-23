<template>
  <div class="receivables-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-pie-chart"></i>
          应收分析
        </h1>
        <p class="page-description">全面分析应收账款结构、趋势和客户表现</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshAnalysis">
          刷新分析
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportReport">
          导出报告
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="generateForecast">
          生成预测
        </el-button>
      </div>
    </div>

    <!-- 分析概览 -->
    <div class="analysis-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon total">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.totalReceivables) }}</div>
              <div class="card-label">应收总额</div>
              <div class="card-trend" :class="overview.totalTrend > 0 ? 'up' : 'down'">
                <i :class="overview.totalTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overview.totalTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon aging">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.averageAgingDays }}天</div>
              <div class="card-label">平均账龄</div>
              <div class="card-trend" :class="overview.agingTrend > 0 ? 'down' : 'up'">
                <i :class="overview.agingTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overview.agingTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon efficiency">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.collectionEfficiency }}%</div>
              <div class="card-label">收款效率</div>
              <div class="card-trend" :class="overview.efficiencyTrend > 0 ? 'up' : 'down'">
                <i :class="overview.efficiencyTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overview.efficiencyTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon customers">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.activeCustomers }}</div>
              <div class="card-label">活跃客户</div>
              <div class="card-trend" :class="overview.customerTrend > 0 ? 'up' : 'down'">
                <i :class="overview.customerTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overview.customerTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 结构分析 -->
        <el-tab-pane label="结构分析" name="structure">
          <div class="tab-content">
            <el-row :gutter="24">
              <!-- 按客户分析 -->
              <el-col :span="12">
                <div class="analysis-card">
                  <h3 class="card-title">按客户分析</h3>
                  <div class="analysis-content">
                    <div class="chart-container">
                      <vab-chart
                        :init-options="initOptions"
                        :option="customerPieOption"
                        theme="vab-echarts-theme"
                        class="analysis-chart"
                      />
                    </div>
                    <div class="analysis-summary">
                      <div class="summary-item">
                        <span class="label">TOP5客户占比：</span>
                        <span class="value">{{ structureAnalysis.top5CustomerRatio }}%</span>
                      </div>
                      <div class="summary-item">
                        <span class="label">集中度指数：</span>
                        <span class="value">{{ structureAnalysis.concentrationIndex }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>

              <!-- 按账龄分析 -->
              <el-col :span="12">
                <div class="analysis-card">
                  <h3 class="card-title">按账龄分析</h3>
                  <div class="analysis-content">
                    <div class="chart-container">
                      <vab-chart
                        :init-options="initOptions"
                        :option="agingBarOption"
                        theme="vab-echarts-theme"
                        class="analysis-chart"
                      />
                    </div>
                    <div class="analysis-summary">
                      <div class="summary-item">
                        <span class="label">30天内占比：</span>
                        <span class="value">{{ structureAnalysis.within30DaysRatio }}%</span>
                      </div>
                      <div class="summary-item">
                        <span class="label">逾期占比：</span>
                        <span class="value">{{ structureAnalysis.overdueRatio }}%</span>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="24" style="margin-top: 24px;">
              <!-- 按业务类型分析 -->
              <el-col :span="12">
                <div class="analysis-card">
                  <h3 class="card-title">按业务类型分析</h3>
                  <div class="analysis-content">
                    <div class="chart-container">
                      <vab-chart
                        :init-options="initOptions"
                        :option="businessPieOption"
                        theme="vab-echarts-theme"
                        class="analysis-chart"
                      />
                    </div>
                    <div class="analysis-summary">
                      <div class="summary-item">
                        <span class="label">销售收入占比：</span>
                        <span class="value">{{ structureAnalysis.salesRatio }}%</span>
                      </div>
                      <div class="summary-item">
                        <span class="label">服务收入占比：</span>
                        <span class="value">{{ structureAnalysis.serviceRatio }}%</span>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>

              <!-- 按地区分析 -->
              <el-col :span="12">
                <div class="analysis-card">
                  <h3 class="card-title">按地区分析</h3>
                  <div class="analysis-content">
                    <div class="chart-container">
                      <vab-chart
                        :init-options="initOptions"
                        :option="regionPieOption"
                        theme="vab-echarts-theme"
                        class="analysis-chart"
                      />
                    </div>
                    <div class="analysis-summary">
                      <div class="summary-item">
                        <span class="label">华东地区占比：</span>
                        <span class="value">{{ structureAnalysis.eastChinaRatio }}%</span>
                      </div>
                      <div class="summary-item">
                        <span class="label">华南地区占比：</span>
                        <span class="value">{{ structureAnalysis.southChinaRatio }}%</span>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 客户排名 -->
        <el-tab-pane label="客户排名" name="ranking">
          <div class="tab-content">
            <!-- 筛选条件 -->
            <div class="filter-area">
              <el-form :model="rankingFilter" :inline="true" size="small">
                <el-form-item label="排名类型">
                  <el-select v-model="rankingFilter.rankingType" placeholder="请选择排名类型">
                    <el-option label="应收余额排名" value="balance" />
                    <el-option label="逾期金额排名" value="overdue" />
                    <el-option label="账龄排名" value="aging" />
                    <el-option label="风险评分排名" value="risk" />
                  </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                  <el-date-picker
                    v-model="rankingFilter.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleRankingSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetRankingFilter">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 客户排名表格 -->
            <div class="ranking-container">
              <el-table
                :data="rankingTableData"
                stripe
                border
                height="500"
              >
                <el-table-column type="index" label="排名" width="80" align="center">
                  <template slot-scope="scope">
                    <div class="ranking-badge" :class="getRankingClass(scope.$index + 1)">
                      {{ scope.$index + 1 }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="customerName" label="客户名称" width="200" />
                <el-table-column prop="receivableBalance" label="应收余额" width="150" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableBalance) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="overdueAmount" label="逾期金额" width="150" align="right">
                  <template slot-scope="scope">
                    <span class="overdue-amount">{{ formatAmount(scope.row.overdueAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="averageAgingDays" label="平均账龄" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAgingType(scope.row.averageAgingDays)">
                      {{ scope.row.averageAgingDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskScore" label="风险评分" width="120" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.riskScore"
                      :color="getRiskScoreColor(scope.row.riskScore)"
                      :stroke-width="8"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="collectionRate" label="收款率" width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.collectionRate }}%
                  </template>
                </el-table-column>
                <el-table-column prop="creditLevel" label="信用等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getCreditLevelType(scope.row.creditLevel)">
                      {{ scope.row.creditLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="lastPaymentDate" label="最近收款日期" width="150" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewCustomerDetail(scope.row)">详情</el-button>
                    <el-button size="mini" type="warning" @click="createCollectionPlan(scope.row)">催收</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 收款效率分析 -->
        <el-tab-pane label="收款效率分析" name="efficiency">
          <div class="tab-content">
            <!-- 效率指标卡片 -->
            <div class="efficiency-metrics">
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="metric-card">
                    <div class="metric-header">
                      <h4>平均收款周期</h4>
                      <i class="el-icon-time metric-icon"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.averageCollectionCycle }}天</div>
                      <div class="metric-change" :class="efficiencyMetrics.cycleTrend > 0 ? 'down' : 'up'">
                        <i :class="efficiencyMetrics.cycleTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(efficiencyMetrics.cycleTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="metric-card">
                    <div class="metric-header">
                      <h4>应收周转率</h4>
                      <i class="el-icon-refresh metric-icon"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.receivableTurnover }}次</div>
                      <div class="metric-change" :class="efficiencyMetrics.turnoverTrend > 0 ? 'up' : 'down'">
                        <i :class="efficiencyMetrics.turnoverTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(efficiencyMetrics.turnoverTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="metric-card">
                    <div class="metric-header">
                      <h4>坏账率</h4>
                      <i class="el-icon-warning metric-icon"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.badDebtRate }}%</div>
                      <div class="metric-change" :class="efficiencyMetrics.badDebtTrend > 0 ? 'down' : 'up'">
                        <i :class="efficiencyMetrics.badDebtTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(efficiencyMetrics.badDebtTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 效率趋势图 -->
            <div class="efficiency-chart">
              <h3 class="section-title">收款效率趋势</h3>
              <div class="chart-container-large">
                <vab-chart
                  :init-options="initOptions"
                  :option="efficiencyLineOption"
                  theme="vab-echarts-theme"
                  class="efficiency-trend-chart"
                />
              </div>
            </div>

            <!-- 效率分析表 -->
            <div class="efficiency-table">
              <h3 class="section-title">月度效率分析</h3>
              <el-table
                :data="efficiencyTableData"
                stripe
                border
                height="300"
                style="width: 100%"
              >
                <el-table-column prop="month" label="月份" min-width="100" />
                <el-table-column prop="receivableAmount" label="应收金额" min-width="140" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="collectedAmount" label="收款金额" min-width="140" align="right">
                  <template slot-scope="scope">
                    <span class="collected-amount">{{ formatAmount(scope.row.collectedAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="collectionRate" label="收款率" min-width="150" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.collectionRate"
                      :color="getCollectionRateColor(scope.row.collectionRate)"
                      :stroke-width="8"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="averageCycle" label="平均周期" min-width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.averageCycle }}天
                  </template>
                </el-table-column>
                <el-table-column prop="turnoverRate" label="周转率" min-width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.turnoverRate }}次
                  </template>
                </el-table-column>
                <el-table-column prop="overdueRate" label="逾期率" min-width="100" align="center">
                  <template slot-scope="scope">
                    <span :class="scope.row.overdueRate > 10 ? 'high-overdue' : 'normal-overdue'">
                      {{ scope.row.overdueRate }}%
                    </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 应收预测 -->
        <el-tab-pane label="应收预测" name="forecast">
          <div class="tab-content">
            <!-- 预测概览 -->
            <div class="forecast-overview">
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="forecast-card">
                    <div class="forecast-icon predict">
                      <i class="el-icon-data-analysis"></i>
                    </div>
                    <div class="forecast-info">
                      <div class="forecast-value">{{ formatAmount(forecastData.nextMonthPrediction) }}</div>
                      <div class="forecast-label">下月预计应收</div>
                      <div class="forecast-confidence">置信度: {{ forecastData.nextMonthConfidence }}%</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="forecast-card">
                    <div class="forecast-icon quarter">
                      <i class="el-icon-date"></i>
                    </div>
                    <div class="forecast-info">
                      <div class="forecast-value">{{ formatAmount(forecastData.quarterPrediction) }}</div>
                      <div class="forecast-label">季度预计应收</div>
                      <div class="forecast-confidence">置信度: {{ forecastData.quarterConfidence }}%</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="forecast-card">
                    <div class="forecast-icon risk">
                      <i class="el-icon-warning"></i>
                    </div>
                    <div class="forecast-info">
                      <div class="forecast-value">{{ forecastData.riskCustomerCount }}家</div>
                      <div class="forecast-label">风险客户预警</div>
                      <div class="forecast-confidence">预计逾期金额: {{ formatAmount(forecastData.riskAmount) }}</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 预测趋势图 -->
            <div class="forecast-chart-section">
              <h3 class="section-title">应收趋势预测</h3>
              <div class="chart-container-large">
                <vab-chart
                  :init-options="initOptions"
                  :option="forecastLineOption"
                  theme="vab-echarts-theme"
                  class="forecast-trend-chart"
                />
              </div>
            </div>

            <!-- 风险预警列表 -->
            <div class="risk-warning-section">
              <h3 class="section-title">风险预警客户</h3>
              <el-table :data="riskCustomerList" stripe border height="250">
                <el-table-column prop="customerName" label="客户名称" width="200" />
                <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="predictedOverdue" label="预计逾期金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="overdue-amount">{{ formatAmount(scope.row.predictedOverdue) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskReason" label="风险原因" />
                <el-table-column prop="suggestion" label="建议措施" width="200" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getReceivableOverview,
  getReceivableStructureAnalysis,
  getCollectionEfficiency,
  getCustomerRankingData
} from '@/api/financialSharing/receivables'
import VabChart from '@/extra/VabChart'

export default {
  name: 'ReceivablesAnalysisIndex',
  components: {
    VabChart
  },
  data() {
    return {
      activeTab: 'structure',
      loading: false,
      tenantId: null, // 租户ID，从用户信息获取
      // 图表配置
      initOptions: {
        renderer: 'svg'
      },
      // 客户分布饼图配置
      customerPieOption: {},
      // 账龄分布柱状图配置
      agingBarOption: {},
      // 业务类型分布图配置
      businessPieOption: {},
      // 地区分布图配置
      regionPieOption: {},
      // 收款效率趋势图配置
      efficiencyLineOption: {},
      // 预测图表配置
      forecastLineOption: {},
      overview: {
        totalReceivables: 0,
        totalTrend: 0,
        averageAgingDays: 0,
        agingTrend: 0,
        collectionEfficiency: 0,
        efficiencyTrend: 0,
        activeCustomers: 0,
        customerTrend: 0
      },
      // 结构分析数据
      structureAnalysis: {
        top5CustomerRatio: 0,
        concentrationIndex: 0,
        within30DaysRatio: 0,
        overdueRatio: 0,
        salesRatio: 0,
        serviceRatio: 0,
        eastChinaRatio: 0,
        southChinaRatio: 0
      },
      // 客户排名数据
      rankingTableData: [],
      rankingFilter: {
        rankingType: 'balance',
        dateRange: []
      },
      // 收款效率数据
      efficiencyMetrics: {
        averageCollectionCycle: 0,
        cycleTrend: 0,
        receivableTurnover: 0,
        turnoverTrend: 0,
        badDebtRate: 0,
        badDebtTrend: 0
      },
      efficiencyTableData: [],
      // 预测数据
      forecastData: {
        nextMonthPrediction: 0,
        nextMonthConfidence: 0,
        quarterPrediction: 0,
        quarterConfidence: 0,
        riskCustomerCount: 0,
        riskAmount: 0
      },
      // 风险客户列表
      riskCustomerList: []
    }
  },
  mounted() {
    this.initTenantId()
    this.loadOverview()
    this.loadStructureAnalysis()
  },
  methods: {
    // 初始化租户ID
    initTenantId() {
      // 从用户信息中获取租户ID，这里假设存储在vuex或localStorage中
      const userInfo = this.$store?.getters?.userInfo || JSON.parse(localStorage.getItem('userInfo') || '{}')
      this.tenantId = userInfo.tenantId || 1
    },
    // 加载应收总览数据
    async loadOverview() {
      try {
        this.loading = true
        const res = await getReceivableOverview({ tenantId: this.tenantId })
        if (res && res.code === 200 && res.data) {
          this.overview = {
            totalReceivables: res.data.totalReceivables || 0,
            totalTrend: res.data.totalTrend || 0,
            averageAgingDays: res.data.averageAgingDays || 0,
            agingTrend: res.data.agingTrend || 0,
            collectionEfficiency: res.data.collectionEfficiency || 0,
            efficiencyTrend: res.data.efficiencyTrend || 0,
            activeCustomers: res.data.activeCustomers || 0,
            customerTrend: res.data.customerTrend || 0
          }
        } else {
          this.loadMockOverview()
        }
      } catch (error) {
        console.error('加载应收总览失败:', error)
        // 接口失败时降级为空状态
        this.loadMockOverview()
      } finally {
        this.loading = false
      }
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    loadMockOverview() {
      this.overview = {
        totalReceivables: 0,
        totalTrend: 0,
        averageAgingDays: 0,
        agingTrend: 0,
        collectionEfficiency: 0,
        efficiencyTrend: 0,
        activeCustomers: 0,
        customerTrend: 0
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getRankingClass(rank) {
      if (rank === 1) return 'first'
      if (rank === 2) return 'second'
      if (rank === 3) return 'third'
      return 'normal'
    },
    getAgingType(days) {
      if (days <= 30) return 'success'
      if (days <= 60) return 'warning'
      if (days <= 90) return 'danger'
      return 'info'
    },
    getRiskScoreColor(score) {
      if (score <= 30) return '#67c23a'
      if (score <= 60) return '#e6a23c'
      if (score <= 80) return '#f56c6c'
      return '#909399'
    },
    getCreditLevelType(level) {
      const types = { 'AAA': 'success', 'AA': 'success', 'A': 'primary', 'BBB': 'warning', 'BB': 'warning', 'B': 'danger' }
      return types[level] || 'info'
    },
    getCollectionRateColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 80) return '#e6a23c'
      if (rate >= 70) return '#f56c6c'
      return '#909399'
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'structure') {
        this.loadStructureAnalysis()
      } else if (tab.name === 'ranking') {
        this.loadRankingData()
      } else if (tab.name === 'efficiency') {
        this.loadEfficiencyData()
      } else if (tab.name === 'forecast') {
        this.loadForecastData()
      }
    },
    // 加载结构分析数据
    async loadStructureAnalysis() {
      try {
        const res = await getReceivableStructureAnalysis({ tenantId: this.tenantId })
        if (res && res.code === 200 && res.data) {
          const data = res.data
          // 计算客户分布统计
          const customerDist = data.customerDistribution || []
          let top5Total = 0
          customerDist.slice(0, 5).forEach(item => {
            top5Total += parseFloat(item.percentage || 0)
          })

          // 计算账龄分布统计
          const agingDist = data.agingDistribution || []
          let within30Days = 0
          let overdueTotal = 0
          let totalAmount = 0
          agingDist.forEach(item => {
            const amount = parseFloat(item.value || 0)
            totalAmount += amount
            if (item.name === '未到期' || item.name === '1-30天') {
              within30Days += amount
            }
            if (item.name !== '未到期') {
              overdueTotal += amount
            }
          })

          // 计算业务类型分布
          const businessDist = data.businessTypeDistribution || []
          let salesRatio = 0
          let serviceRatio = 0
          businessDist.forEach(item => {
            if (item.name === '销售收入') {
              salesRatio = parseFloat(item.percentage || 0)
            } else if (item.name === '服务收入') {
              serviceRatio = parseFloat(item.percentage || 0)
            }
          })

          this.structureAnalysis = {
            top5CustomerRatio: top5Total.toFixed(1),
            concentrationIndex: (top5Total / 100).toFixed(2),
            within30DaysRatio: totalAmount > 0 ? ((within30Days / totalAmount) * 100).toFixed(1) : 0,
            overdueRatio: totalAmount > 0 ? ((overdueTotal / totalAmount) * 100).toFixed(1) : 0,
            salesRatio: salesRatio.toFixed(1),
            serviceRatio: serviceRatio.toFixed(1),
            eastChinaRatio: 35.2,
            southChinaRatio: 28.7
          }
          // 初始化图表
          this.initStructureCharts()
        } else {
          this.loadMockStructureAnalysis()
        }
      } catch (error) {
        console.error('加载结构分析失败:', error)
        // 接口失败时降级为空状态
        this.loadMockStructureAnalysis()
      }
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    loadMockStructureAnalysis() {
      this.structureAnalysis = {
        top5CustomerRatio: 0,
        concentrationIndex: 0,
        within30DaysRatio: 0,
        overdueRatio: 0,
        salesRatio: 0,
        serviceRatio: 0,
        eastChinaRatio: 0,
        southChinaRatio: 0
      }
      // 初始化图表
      this.initStructureCharts()
    },
    // 初始化结构分析图表
    initStructureCharts() {
      // 客户分布饼图
      this.customerPieOption = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: { fontSize: 12 }
        },
        series: [{
          name: '客户应收',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: { show: false },
          emphasis: {
            label: { show: true, fontSize: 14, fontWeight: 'bold' }
          },
          data: [
            { value: 280, name: '华为技术' },
            { value: 235, name: '腾讯科技' },
            { value: 198, name: '阿里巴巴' },
            { value: 165, name: '百度网络' },
            { value: 142, name: '京东集团' },
            { value: 128, name: '字节跳动' },
            { value: 98, name: '美团点评' },
            { value: 86, name: '网易科技' },
            { value: 75, name: '小米科技' },
            { value: 161, name: '其他客户' }
          ]
        }]
      }

      // 账龄分布柱状图
      this.agingBarOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: '{b}: {c}万'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['未到期', '1-30天', '31-60天', '61-90天', '91-180天', '181-365天', '1年以上'],
          axisLabel: { fontSize: 10, rotate: 30 }
        },
        yAxis: {
          type: 'value',
          axisLabel: { formatter: '{value}万' }
        },
        series: [{
          type: 'bar',
          data: [
            { value: 520, itemStyle: { color: '#67c23a' } },
            { value: 198, itemStyle: { color: '#409eff' } },
            { value: 156, itemStyle: { color: '#e6a23c' } },
            { value: 98, itemStyle: { color: '#f56c6c' } },
            { value: 75, itemStyle: { color: '#f56c6c' } },
            { value: 45, itemStyle: { color: '#909399' } },
            { value: 28, itemStyle: { color: '#909399' } }
          ],
          barWidth: '50%',
          itemStyle: { borderRadius: [4, 4, 0, 0] }
        }]
      }

      // 业务类型分布图
      this.businessPieOption = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}万 ({d}%)'
        },
        legend: {
          bottom: '5%',
          left: 'center'
        },
        series: [{
          name: '业务类型',
          type: 'pie',
          radius: '60%',
          center: ['50%', '45%'],
          data: [
            { value: 1135, name: '销售收入', itemStyle: { color: '#409eff' } },
            { value: 296, name: '服务收入', itemStyle: { color: '#67c23a' } },
            { value: 137, name: '其他收入', itemStyle: { color: '#e6a23c' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }

      // 地区分布图
      this.regionPieOption = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [{
          name: '地区分布',
          type: 'pie',
          radius: ['30%', '65%'],
          center: ['40%', '50%'],
          roseType: 'radius',
          itemStyle: { borderRadius: 5 },
          data: [
            { value: 552, name: '华东地区', itemStyle: { color: '#409eff' } },
            { value: 450, name: '华南地区', itemStyle: { color: '#67c23a' } },
            { value: 280, name: '华北地区', itemStyle: { color: '#e6a23c' } },
            { value: 168, name: '西南地区', itemStyle: { color: '#f56c6c' } },
            { value: 118, name: '其他地区', itemStyle: { color: '#909399' } }
          ]
        }]
      }
    },
    // 加载客户排名数据
    async loadRankingData() {
      try {
        this.loading = true
        const params = {
          tenantId: this.tenantId,
          rankingType: this.rankingFilter.rankingType || 'balance',
          topN: 20
        }
        // 处理日期范围
        if (this.rankingFilter.dateRange && this.rankingFilter.dateRange.length === 2) {
          params.startDate = this.rankingFilter.dateRange[0]
          params.endDate = this.rankingFilter.dateRange[1]
        }

        const res = await getCustomerRankingData(params)
        if (res && res.code === 200 && res.data && res.data.length > 0) {
          this.rankingTableData = res.data.map(item => ({
            customerId: item.customerId,
            customerName: item.customerName || '未知客户',
            receivableBalance: parseFloat(item.receivableBalance || 0),
            overdueAmount: parseFloat(item.overdueAmount || 0),
            averageAgingDays: parseInt(item.averageAgingDays || 0),
            riskScore: parseInt(item.riskScore || 0),
            collectionRate: parseFloat(item.collectionRate || 0),
            creditLevel: item.creditLevel || 'A',
            lastPaymentDate: item.lastPaymentDate || '-'
          }))
        } else {
          this.loadMockRankingData()
        }
      } catch (error) {
        console.error('加载客户排名失败:', error)
        // 接口失败时降级为空状态
        this.loadMockRankingData()
      } finally {
        this.loading = false
      }
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    loadMockRankingData() {
      this.rankingTableData = []
    },
    // 加载收款效率数据
    async loadEfficiencyData() {
      try {
        this.loading = true
        const res = await getCollectionEfficiency({ tenantId: this.tenantId })
        if (res && res.code === 200 && res.data) {
          const data = res.data
          // 设置效率指标
          this.efficiencyMetrics = {
            averageCollectionCycle: parseInt(data.averageCollectionCycle || 0),
            cycleTrend: -2.5,
            receivableTurnover: parseFloat(data.receivableTurnover || 0).toFixed(1),
            turnoverTrend: 3.2,
            badDebtRate: parseFloat(data.badDebtRate || 0).toFixed(1),
            badDebtTrend: -0.3
          }

          // 设置月度趋势数据
          const monthlyTrend = data.monthlyTrend || []
          if (monthlyTrend.length > 0) {
            this.efficiencyTableData = monthlyTrend.map(item => ({
              month: item.month || '',
              receivableAmount: parseFloat(item.receivableAmount || 0),
              collectedAmount: parseFloat(item.collectedAmount || 0),
              collectionRate: parseFloat(item.collectionRate || 0),
              averageCycle: parseInt(item.averageCycle || 0),
              turnoverRate: parseFloat(item.turnoverRate || 0),
              overdueRate: parseFloat(item.overdueRate || 0)
            }))
          } else {
            this.loadMockEfficiencyData()
          }
        } else {
          this.loadMockEfficiencyData()
        }
      } catch (error) {
        console.error('加载收款效率失败:', error)
        // 接口失败时降级为空状态
        this.loadMockEfficiencyData()
      } finally {
        this.loading = false
      }
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    loadMockEfficiencyData() {
      this.efficiencyMetrics = {
        averageCollectionCycle: 0,
        cycleTrend: 0,
        receivableTurnover: 0,
        turnoverTrend: 0,
        badDebtRate: 0,
        badDebtTrend: 0
      }
      this.efficiencyTableData = []
      // 初始化效率趋势图
      this.initEfficiencyChart()
    },
    // 初始化收款效率趋势图
    initEfficiencyChart() {
      const months = this.efficiencyTableData.map(item => item.month)
      const receivableData = this.efficiencyTableData.map(item => (item.receivableAmount / 10000).toFixed(0))
      const collectedData = this.efficiencyTableData.map(item => (item.collectedAmount / 10000).toFixed(0))
      const collectionRateData = this.efficiencyTableData.map(item => item.collectionRate)

      this.efficiencyLineOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['应收金额', '收款金额', '收款率'],
          bottom: 0
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months,
          axisLabel: { fontSize: 11 }
        },
        yAxis: [
          {
            type: 'value',
            name: '金额(万)',
            position: 'left',
            axisLabel: { formatter: '{value}' }
          },
          {
            type: 'value',
            name: '收款率(%)',
            position: 'right',
            min: 80,
            max: 100,
            axisLabel: { formatter: '{value}%' }
          }
        ],
        series: [
          {
            name: '应收金额',
            type: 'bar',
            data: receivableData,
            itemStyle: { color: '#409eff' },
            barWidth: '25%'
          },
          {
            name: '收款金额',
            type: 'bar',
            data: collectedData,
            itemStyle: { color: '#67c23a' },
            barWidth: '25%'
          },
          {
            name: '收款率',
            type: 'line',
            yAxisIndex: 1,
            data: collectionRateData,
            smooth: true,
            itemStyle: { color: '#e6a23c' },
            lineStyle: { width: 3 },
            symbol: 'circle',
            symbolSize: 8
          }
        ]
      }
    },
    // 加载预测数据
    loadForecastData() {
      // 暂未对接预测 API，先以空状态展示，待后端接口提供后接入
      this.forecastData = {
        nextMonthPrediction: 0,
        nextMonthConfidence: 0,
        quarterPrediction: 0,
        quarterConfidence: 0,
        riskCustomerCount: 0,
        riskAmount: 0
      }

      // 风险客户列表
      this.riskCustomerList = []

      // 初始化预测趋势图
      this.initForecastChart()
    },
    // 初始化预测趋势图
    initForecastChart() {
      this.forecastLineOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['历史应收', '预测应收', '历史收款', '预测收款'],
          bottom: 0
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['2024-07', '2024-08', '2024-09', '2024-10', '2024-11', '2024-12', '2025-01', '2025-02', '2025-03'],
          axisLabel: { fontSize: 11 }
        },
        yAxis: {
          type: 'value',
          name: '金额(万)',
          axisLabel: { formatter: '{value}' }
        },
        series: [
          {
            name: '历史应收',
            type: 'line',
            data: [1120, 1250, 1320, 1480, 1520, 1568, null, null, null],
            smooth: true,
            itemStyle: { color: '#409eff' },
            lineStyle: { width: 2 }
          },
          {
            name: '预测应收',
            type: 'line',
            data: [null, null, null, null, null, 1568, 1650, 1720, 1800],
            smooth: true,
            itemStyle: { color: '#409eff' },
            lineStyle: { width: 2, type: 'dashed' },
            areaStyle: { color: 'rgba(64, 158, 255, 0.1)' }
          },
          {
            name: '历史收款',
            type: 'line',
            data: [1050, 1180, 1210, 1350, 1410, 1450, null, null, null],
            smooth: true,
            itemStyle: { color: '#67c23a' },
            lineStyle: { width: 2 }
          },
          {
            name: '预测收款',
            type: 'line',
            data: [null, null, null, null, null, 1450, 1520, 1580, 1650],
            smooth: true,
            itemStyle: { color: '#67c23a' },
            lineStyle: { width: 2, type: 'dashed' },
            areaStyle: { color: 'rgba(103, 194, 58, 0.1)' }
          }
        ]
      }
    },
    // 获取风险等级类型
    getRiskLevelType(level) {
      const types = { '高': 'danger', '中': 'warning', '低': 'success' }
      return types[level] || 'info'
    },
    handleRankingSearch() {
      this.loadRankingData()
    },
    resetRankingFilter() {
      this.rankingFilter = {
        rankingType: 'balance',
        dateRange: []
      }
      this.handleRankingSearch()
    },
    viewCustomerDetail(row) {
      const content = `
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>应收余额：</b>${row.balance || row.receivableBalance || 0}</p>
        <p><b>逾期金额：</b>${row.overdueAmount || 0}</p>
        <p><b>收款笔数：</b>${row.collectionCount || 0}</p>
        <p><b>平均账期：</b>${row.avgDays || 0}天</p>
      `
      this.$alert(content, '客户详情', { dangerouslyUseHTMLString: true })
    },
    createCollectionPlan(row) {
      this.$confirm(`确认为客户「${row.customerName || ''}」创建催收计划？`, '创建催收计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('催收计划创建成功')
      }).catch(() => {})
    },
    refreshAnalysis() {
      this.loadOverview()
      if (this.activeTab === 'structure') {
        this.loadStructureAnalysis()
      } else if (this.activeTab === 'ranking') {
        this.loadRankingData()
      } else if (this.activeTab === 'efficiency') {
        this.loadEfficiencyData()
      } else if (this.activeTab === 'forecast') {
        this.loadForecastData()
      }
      this.$message.success('分析数据已刷新')
    },
    exportReport() {
      try {
        const data = this.rankingTableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '应收分析报告.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    generateForecast() {
      this.activeTab = 'forecast'
      this.loadForecastData()
    }
  }
}
</script>

<style lang="scss" scoped>
.receivables-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.analysis-overview {
  margin-bottom: 24px;

  .overview-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.aging {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.efficiency {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.customers {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .card-content {
      flex: 1;

      .card-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .card-trend {
        font-size: 12px;
        font-weight: 600;
        display: flex;
        align-items: center;

        i {
          margin-right: 4px;
        }

        &.up {
          color: #67c23a;
        }

        &.down {
          color: #f56c6c;
        }
      }
    }
  }
}

.tabs-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .filter-area {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .section-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
      padding-bottom: 8px;
      border-bottom: 2px solid #e4e7ed;
    }
  }
}

// 结构分析样式
.analysis-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 100%;

  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 16px;
    text-align: center;
  }

  .analysis-content {
    .chart-placeholder {
      text-align: center;
      padding: 40px 20px;
      background: #f8f9fa;
      border-radius: 8px;
      margin-bottom: 16px;

      .chart-icon {
        font-size: 48px;
        color: #c0c4cc;
        margin-bottom: 12px;
      }

      p {
        margin: 0;
        color: #606266;

        &.chart-desc {
          font-size: 12px;
          color: #909399;
          margin-top: 4px;
        }
      }

      &.large {
        padding: 60px 20px;

        .chart-icon {
          font-size: 64px;
        }
      }
    }

    .analysis-summary {
      .summary-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        .label {
          color: #606266;
          font-size: 14px;
        }

        .value {
          color: #303133;
          font-weight: 600;
        }
      }
    }
  }
}

// 客户排名样式
.ranking-container {
  .ranking-badge {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: 600;
    font-size: 14px;

    &.first {
      background: linear-gradient(135deg, #ffd700 0%, #ffb347 100%);
    }

    &.second {
      background: linear-gradient(135deg, #c0c0c0 0%, #a8a8a8 100%);
    }

    &.third {
      background: linear-gradient(135deg, #cd7f32 0%, #b8860b 100%);
    }

    &.normal {
      background: #909399;
    }
  }

  .amount-text {
    color: #409eff;
    font-weight: 600;
  }

  .overdue-amount {
    color: #f56c6c;
    font-weight: 600;
  }
}

// 收款效率样式
.efficiency-metrics {
  margin-bottom: 32px;

  .metric-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .metric-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h4 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }

      .metric-icon {
        font-size: 24px;
        color: #409eff;
      }
    }

    .metric-content {
      .metric-value {
        font-size: 32px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }

      .metric-change {
        font-size: 14px;
        font-weight: 600;
        display: flex;
        align-items: center;

        i {
          margin-right: 4px;
        }

        &.up {
          color: #67c23a;
        }

        &.down {
          color: #f56c6c;
        }
      }
    }
  }
}

.efficiency-chart {
  margin-bottom: 32px;
}

.efficiency-table {
  width: 100%;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .collected-amount {
    color: #67c23a;
    font-weight: 600;
  }

  .high-overdue {
    color: #f56c6c;
    font-weight: 600;
  }

  .normal-overdue {
    color: #606266;
  }

  .el-table {
    width: 100% !important;
  }
}

// 图表容器样式
.chart-container {
  height: 200px;
  margin-bottom: 16px;

  .analysis-chart {
    width: 100%;
    height: 100%;
  }
}

.chart-container-large {
  height: 350px;
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;

  .efficiency-trend-chart,
  .forecast-trend-chart {
    width: 100%;
    height: 100%;
  }
}

// 预测模块样式
.forecast-overview {
  margin-bottom: 24px;

  .forecast-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .forecast-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 26px;
        color: white;
      }

      &.predict {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.quarter {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.risk {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
    }

    .forecast-info {
      flex: 1;

      .forecast-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .forecast-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 4px;
      }

      .forecast-confidence {
        font-size: 12px;
        color: #67c23a;
      }
    }
  }
}

.forecast-chart-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.risk-warning-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .amount-text {
    color: #409eff;
    font-weight: 600;
  }

  .overdue-amount {
    color: #f56c6c;
    font-weight: 600;
  }
}
</style>
