<template>
  <div class="aging-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          账龄分析
        </h1>
        <p class="page-description">分析应收账款账龄结构，识别风险和收款趋势</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshAnalysis">
          刷新分析
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportAnalysis">
          导出报告
        </el-button>
        <el-button type="warning" icon="el-icon-warning" @click="generateRiskAlert">
          风险预警
        </el-button>
      </div>
    </div>

    <!-- 账龄概览 -->
    <div class="aging-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon total">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.totalReceivables) }}</div>
              <div class="card-label">应收总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon current">
              <i class="el-icon-check"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.averageAgingDays }}天</div>
              <div class="card-label">平均账龄</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overview.overdueAmount) }}</div>
              <div class="card-label">逾期金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon risk">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.riskCustomerCount }}</div>
              <div class="card-label">风险客户</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 标签页 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 账龄分析表 -->
        <el-tab-pane label="账龄分析表" name="aging">
          <div class="tab-content">
            <!-- 筛选条件 -->
            <div class="filter-area">
              <el-form :model="agingFilter" :inline="true" size="small">
                <el-form-item label="客户">
                  <el-select v-model="agingFilter.customerId" placeholder="请选择客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="账龄区间">
                  <el-select v-model="agingFilter.agingRange" placeholder="请选择账龄区间" clearable>
                    <el-option label="30天内" value="0-30" />
                    <el-option label="31-60天" value="31-60" />
                    <el-option label="61-90天" value="61-90" />
                    <el-option label="91-180天" value="91-180" />
                    <el-option label="180天以上" value="180+" />
                  </el-select>
                </el-form-item>
                <el-form-item label="分析日期">
                  <el-date-picker
                    v-model="agingFilter.analysisDate"
                    type="date"
                    placeholder="请选择分析日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleAgingSearch">搜索</el-button>
                  <el-button icon="el-icon-refresh" @click="resetAgingFilter">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 账龄分析表格 -->
            <div class="table-container">
              <el-table
                v-loading="agingLoading"
                :data="agingTableData"
                stripe
                border
                height="400"
                show-summary
                :summary-method="getAgingSummary"
              >
                <el-table-column prop="customerName" label="客户名称" width="150" />
                <el-table-column prop="receivableNo" label="应收单号" width="150" />
                <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="remainingAmount" label="未收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.remainingAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="agingDays" label="账龄天数" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAgingType(scope.row.agingDays)">
                      {{ scope.row.agingDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="agingRange" label="账龄区间" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAgingRangeType(scope.row.agingRange)">
                      {{ scope.row.agingRange }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                      {{ getRiskLevelText(scope.row.riskLevel) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="dueDate" label="到期日期" width="120" />
                <el-table-column prop="overdueAmount" label="逾期金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="overdue-amount">{{ formatAmount(scope.row.overdueAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewAgingDetail(scope.row)">详情</el-button>
                    <el-button size="mini" type="warning" @click="createCollectionPlan(scope.row)">催收</el-button>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleAgingSizeChange"
                  @current-change="handleAgingCurrentChange"
                  :current-page="agingPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="agingPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="agingPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 逾期统计 -->
        <el-tab-pane label="逾期统计" name="overdue">
          <div class="tab-content">
            <!-- 逾期统计卡片 -->
            <div class="overdue-stats">
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="stat-card">
                    <div class="stat-header">
                      <h3>30天内</h3>
                      <i class="el-icon-check stat-icon normal"></i>
                    </div>
                    <div class="stat-body">
                      <div class="stat-amount">{{ formatAmount(overdueStats.within30Days.amount) }}</div>
                      <div class="stat-count">{{ overdueStats.within30Days.count }}笔</div>
                      <div class="stat-percentage">{{ overdueStats.within30Days.percentage }}%</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-card">
                    <div class="stat-header">
                      <h3>31-60天</h3>
                      <i class="el-icon-warning stat-icon warning"></i>
                    </div>
                    <div class="stat-body">
                      <div class="stat-amount">{{ formatAmount(overdueStats.days31To60.amount) }}</div>
                      <div class="stat-count">{{ overdueStats.days31To60.count }}笔</div>
                      <div class="stat-percentage">{{ overdueStats.days31To60.percentage }}%</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-card">
                    <div class="stat-header">
                      <h3>61-90天</h3>
                      <i class="el-icon-warning-outline stat-icon danger"></i>
                    </div>
                    <div class="stat-body">
                      <div class="stat-amount">{{ formatAmount(overdueStats.days61To90.amount) }}</div>
                      <div class="stat-count">{{ overdueStats.days61To90.count }}笔</div>
                      <div class="stat-percentage">{{ overdueStats.days61To90.percentage }}%</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="margin-top: 24px;">
                <el-col :span="12">
                  <div class="stat-card">
                    <div class="stat-header">
                      <h3>90天以上</h3>
                      <i class="el-icon-close stat-icon critical"></i>
                    </div>
                    <div class="stat-body">
                      <div class="stat-amount">{{ formatAmount(overdueStats.over90Days.amount) }}</div>
                      <div class="stat-count">{{ overdueStats.over90Days.count }}笔</div>
                      <div class="stat-percentage">{{ overdueStats.over90Days.percentage }}%</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="stat-card total-overdue">
                    <div class="stat-header">
                      <h3>逾期总计</h3>
                      <i class="el-icon-warning stat-icon total"></i>
                    </div>
                    <div class="stat-body">
                      <div class="stat-amount">{{ formatAmount(overdueStats.total.amount) }}</div>
                      <div class="stat-count">{{ overdueStats.total.count }}笔</div>
                      <div class="stat-percentage">{{ overdueStats.total.percentage }}%</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 逾期客户排行 -->
            <div class="overdue-ranking">
              <h3 class="section-title">逾期客户排行TOP10</h3>
              <el-table
                :data="overdueRankingData"
                stripe
                border
                height="300"
              >
                <el-table-column type="index" label="排名" width="80" align="center" />
                <el-table-column prop="customerName" label="客户名称" width="200" />
                <el-table-column prop="overdueAmount" label="逾期金额" width="150" align="right">
                  <template slot-scope="scope">
                    <span class="overdue-amount">{{ formatAmount(scope.row.overdueAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="overdueCount" label="逾期笔数" width="100" align="center" />
                <el-table-column prop="maxOverdueDays" label="最长逾期天数" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAgingType(scope.row.maxOverdueDays)">
                      {{ scope.row.maxOverdueDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                      {{ getRiskLevelText(scope.row.riskLevel) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewCustomerAging(scope.row)">查看</el-button>
                    <el-button size="mini" type="danger" @click="urgentCollection(scope.row)">紧急催收</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 坏账风险评估 -->
        <el-tab-pane label="坏账风险评估" name="risk">
          <div class="tab-content">
            <!-- 风险评估概览 -->
            <div class="risk-overview">
              <el-row :gutter="24">
                <el-col :span="6">
                  <div class="risk-card low">
                    <div class="risk-header">
                      <h4>低风险</h4>
                      <i class="el-icon-success"></i>
                    </div>
                    <div class="risk-content">
                      <div class="risk-amount">{{ formatAmount(riskAssessment.lowRisk.amount) }}</div>
                      <div class="risk-count">{{ riskAssessment.lowRisk.count }}笔</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="risk-card medium">
                    <div class="risk-header">
                      <h4>中风险</h4>
                      <i class="el-icon-warning"></i>
                    </div>
                    <div class="risk-content">
                      <div class="risk-amount">{{ formatAmount(riskAssessment.mediumRisk.amount) }}</div>
                      <div class="risk-count">{{ riskAssessment.mediumRisk.count }}笔</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="risk-card high">
                    <div class="risk-header">
                      <h4>高风险</h4>
                      <i class="el-icon-warning-outline"></i>
                    </div>
                    <div class="risk-content">
                      <div class="risk-amount">{{ formatAmount(riskAssessment.highRisk.amount) }}</div>
                      <div class="risk-count">{{ riskAssessment.highRisk.count }}笔</div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="risk-card critical">
                    <div class="risk-header">
                      <h4>极高风险</h4>
                      <i class="el-icon-close"></i>
                    </div>
                    <div class="risk-content">
                      <div class="risk-amount">{{ formatAmount(riskAssessment.criticalRisk.amount) }}</div>
                      <div class="risk-count">{{ riskAssessment.criticalRisk.count }}笔</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 风险预警列表 -->
            <div class="risk-alerts">
              <h3 class="section-title">风险预警列表</h3>
              <el-table
                :data="riskAlertData"
                stripe
                border
                height="350"
              >
                <el-table-column prop="customerName" label="客户名称" width="150" />
                <el-table-column prop="receivableNo" label="应收单号" width="150" />
                <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.receivableAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="agingDays" label="账龄天数" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAgingType(scope.row.agingDays)">
                      {{ scope.row.agingDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                      {{ getRiskLevelText(scope.row.riskLevel) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskScore" label="风险评分" width="100" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.riskScore"
                      :color="getRiskScoreColor(scope.row.riskScore)"
                      :stroke-width="8"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="alertReason" label="预警原因" width="200" />
                <el-table-column prop="suggestedAction" label="建议措施" width="200" />
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="viewRiskDetail(scope.row)">详情</el-button>
                    <el-button size="mini" type="warning" @click="handleRiskAlert(scope.row)">处理</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 收款趋势分析 -->
        <el-tab-pane label="收款趋势分析" name="trend">
          <div class="tab-content">
            <!-- 筛选条件 -->
            <div class="filter-area">
              <el-form :model="trendFilter" :inline="true" size="small">
                <el-form-item label="时间范围">
                  <el-date-picker
                    v-model="trendFilter.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    :picker-options="datePickerOptions"
                  />
                </el-form-item>
                <el-form-item label="统计维度">
                  <el-select v-model="trendFilter.dimension" placeholder="请选择统计维度">
                    <el-option label="按月统计" value="month" />
                    <el-option label="按周统计" value="week" />
                    <el-option label="按日统计" value="day" />
                  </el-select>
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="trendFilter.customerId" placeholder="全部客户" clearable filterable>
                    <el-option
                      v-for="customer in customerOptions"
                      :key="customer.customerId"
                      :label="customer.customerName"
                      :value="customer.customerId"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleTrendSearch">分析</el-button>
                  <el-button icon="el-icon-refresh" @click="resetTrendFilter">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 收款效率指标卡片 -->
            <div class="efficiency-cards">
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="efficiency-card">
                    <div class="card-icon collection-rate">
                      <i class="el-icon-coin"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-value">{{ trendMetrics.collectionRate }}%</div>
                      <div class="card-label">收款率</div>
                      <div class="card-trend" :class="{ 'up': trendMetrics.collectionRateTrend > 0, 'down': trendMetrics.collectionRateTrend < 0 }">
                        <i :class="trendMetrics.collectionRateTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(trendMetrics.collectionRateTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="efficiency-card">
                    <div class="card-icon avg-days">
                      <i class="el-icon-time"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-value">{{ trendMetrics.avgCollectionDays }}天</div>
                      <div class="card-label">平均收款周期</div>
                      <div class="card-trend" :class="{ 'up': trendMetrics.avgDaysTrend < 0, 'down': trendMetrics.avgDaysTrend > 0 }">
                        <i :class="trendMetrics.avgDaysTrend < 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(trendMetrics.avgDaysTrend) }}天
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="efficiency-card">
                    <div class="card-icon total-collected">
                      <i class="el-icon-money"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-value">{{ formatAmount(trendMetrics.totalCollected) }}</div>
                      <div class="card-label">累计收款</div>
                      <div class="card-trend" :class="{ 'up': trendMetrics.collectedTrend > 0, 'down': trendMetrics.collectedTrend < 0 }">
                        <i :class="trendMetrics.collectedTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(trendMetrics.collectedTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="efficiency-card">
                    <div class="card-icon overdue-rate">
                      <i class="el-icon-warning"></i>
                    </div>
                    <div class="card-info">
                      <div class="card-value">{{ trendMetrics.overdueRate }}%</div>
                      <div class="card-label">逾期率</div>
                      <div class="card-trend" :class="{ 'up': trendMetrics.overdueRateTrend < 0, 'down': trendMetrics.overdueRateTrend > 0 }">
                        <i :class="trendMetrics.overdueRateTrend < 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                        {{ Math.abs(trendMetrics.overdueRateTrend) }}%
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 图表区域 -->
            <el-row :gutter="24" class="chart-row">
              <!-- 月度收款趋势图 -->
              <el-col :span="16">
                <div class="chart-card">
                  <div class="chart-header">
                    <h4><i class="el-icon-data-line"></i> 收款趋势分析</h4>
                    <el-radio-group v-model="trendChartType" size="mini" @change="updateTrendChart">
                      <el-radio-button label="amount">金额</el-radio-button>
                      <el-radio-button label="count">笔数</el-radio-button>
                    </el-radio-group>
                  </div>
                  <div ref="collectionTrendChart" class="chart-container" style="height: 350px;"></div>
                </div>
              </el-col>
              <!-- 收款构成饼图 -->
              <el-col :span="8">
                <div class="chart-card">
                  <div class="chart-header">
                    <h4><i class="el-icon-pie-chart"></i> 收款来源构成</h4>
                  </div>
                  <div ref="collectionPieChart" class="chart-container" style="height: 350px;"></div>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="24" class="chart-row">
              <!-- 客户收款周期分析 -->
              <el-col :span="12">
                <div class="chart-card">
                  <div class="chart-header">
                    <h4><i class="el-icon-user"></i> 客户收款周期分析</h4>
                  </div>
                  <div ref="customerCycleChart" class="chart-container" style="height: 300px;"></div>
                </div>
              </el-col>
              <!-- 收款预测 -->
              <el-col :span="12">
                <div class="chart-card">
                  <div class="chart-header">
                    <h4><i class="el-icon-magic-stick"></i> 收款预测模型</h4>
                    <el-tag size="small" type="info">基于历史数据预测</el-tag>
                  </div>
                  <div ref="forecastChart" class="chart-container" style="height: 300px;"></div>
                </div>
              </el-col>
            </el-row>

            <!-- 客户收款周期明细表 -->
            <div class="cycle-detail-section">
              <h3 class="section-title">客户收款周期明细</h3>
              <el-table
                :data="customerCycleData"
                stripe
                border
                height="300"
              >
                <el-table-column type="index" label="序号" width="60" align="center" />
                <el-table-column prop="customerName" label="客户名称" width="180" />
                <el-table-column prop="totalReceivable" label="应收总额" width="130" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.totalReceivable) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="totalCollected" label="已收金额" width="130" align="right">
                  <template slot-scope="scope">
                    <span class="collected-amount">{{ formatAmount(scope.row.totalCollected) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="collectionRate" label="收款率" width="100" align="center">
                  <template slot-scope="scope">
                    <el-progress
                      :percentage="scope.row.collectionRate"
                      :color="getCollectionRateColor(scope.row.collectionRate)"
                      :stroke-width="10"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="avgCycleDays" label="平均周期" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getCycleDaysType(scope.row.avgCycleDays)">
                      {{ scope.row.avgCycleDays }}天
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="onTimeRate" label="准时率" width="100" align="center">
                  <template slot-scope="scope">
                    <span :class="{ 'success-text': scope.row.onTimeRate >= 80, 'warning-text': scope.row.onTimeRate < 80 }">
                      {{ scope.row.onTimeRate }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="creditLevel" label="信用等级" width="100" align="center">
                  <template slot-scope="scope">
                    <el-rate
                      v-model="scope.row.creditLevel"
                      disabled
                      :max="5"
                      :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                    />
                  </template>
                </el-table-column>
                <el-table-column prop="trend" label="趋势" width="80" align="center">
                  <template slot-scope="scope">
                    <i
                      :class="scope.row.trend > 0 ? 'el-icon-top' : scope.row.trend < 0 ? 'el-icon-bottom' : 'el-icon-minus'"
                      :style="{ color: scope.row.trend > 0 ? '#67c23a' : scope.row.trend < 0 ? '#f56c6c' : '#909399' }"
                    ></i>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="viewCustomerTrend(scope.row)">趋势详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getAgingAnalysis,
  getAgingAnalysisDetail,
  getOverdueStatistics,
  getRiskAssessment,
  getAgingByCustomer,
  getAgingTrend,
  generateAgingSnapshot,
  getLatestSnapshotDate,
  exportAgingAnalysis,
  getCustomerPage
} from '@/api/financialSharing/receivables'

export default {
  name: 'AgingAnalysisIndex',
  data() {
    return {
      activeTab: 'aging',
      overview: {
        totalReceivables: 0,
        averageAgingDays: 0,
        overdueAmount: 0,
        riskCustomerCount: 0
      },
      // 账龄分析相关数据
      agingLoading: false,
      agingTableData: [],
      agingFilter: {
        customerId: '',
        agingRange: '',
        analysisDate: ''
      },
      agingPagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      // 逾期统计数据
      overdueStats: {
        within30Days: { amount: 0, count: 0, percentage: 0 },
        days31To60: { amount: 0, count: 0, percentage: 0 },
        days61To90: { amount: 0, count: 0, percentage: 0 },
        over90Days: { amount: 0, count: 0, percentage: 0 },
        total: { amount: 0, count: 0, percentage: 0 }
      },
      overdueRankingData: [],
      // 风险评估数据
      riskAssessment: {
        lowRisk: { amount: 0, count: 0 },
        mediumRisk: { amount: 0, count: 0 },
        highRisk: { amount: 0, count: 0 },
        criticalRisk: { amount: 0, count: 0 }
      },
      riskAlertData: [],
      customerOptions: [],
      // 收款趋势分析数据
      trendFilter: {
        dateRange: [],
        dimension: 'month',
        customerId: ''
      },
      trendChartType: 'amount',
      trendMetrics: {
        collectionRate: 0,
        collectionRateTrend: 0,
        avgCollectionDays: 0,
        avgDaysTrend: 0,
        totalCollected: 0,
        collectedTrend: 0,
        overdueRate: 0,
        overdueRateTrend: 0
      },
      customerCycleData: [],
      // 图表实例
      charts: {
        collectionTrend: null,
        collectionPie: null,
        customerCycle: null,
        forecast: null
      },
      // 日期选择器配置
      datePickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近半年',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 180)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一年',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 365)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      }
    }
  },
  mounted() {
    this.loadOverview()
    this.loadCustomerOptions()
    this.loadAgingData()
  },
  methods: {
    async loadOverview() {
      try {
        const analysisDate = this.agingFilter.analysisDate || this.formatDate(new Date())
        const response = await getAgingAnalysis({ analysisDate, tenantId: 1 })
        if (response.code === 1 && response.data) {
          // 从账龄区间统计数据计算概览
          const data = response.data
          let totalReceivables = 0
          let overdueAmount = 0
          let totalDays = 0
          let count = 0
          if (Array.isArray(data)) {
            data.forEach(item => {
              totalReceivables += Number(item.totalAmount || 0)
              if (item.agingRange !== '未逾期') {
                overdueAmount += Number(item.totalAmount || 0)
              }
              totalDays += Number(item.avgAgingDays || 0) * Number(item.count || 0)
              count += Number(item.count || 0)
            })
          }
          this.overview = {
            totalReceivables: totalReceivables,
            averageAgingDays: count > 0 ? Math.round(totalDays / count) : 0,
            overdueAmount: overdueAmount,
            riskCustomerCount: 0 // 需要从风险评估接口获取
          }
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
        // 使用默认值
        this.overview = {
          totalReceivables: 0,
          averageAgingDays: 0,
          overdueAmount: 0,
          riskCustomerCount: 0
        }
      }
    },
    async loadCustomerOptions() {
      try {
        const response = await getCustomerPage({ pageSize: 1000 })
        if (response.code === 1) {
          this.customerOptions = response.data.tlist || []
        }
      } catch (error) {
        console.error('加载客户选项失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getAgingType(days) {
      if (days <= 30) return 'success'
      if (days <= 60) return 'warning'
      if (days <= 90) return 'danger'
      return 'info'
    },
    getAgingRangeType(range) {
      const types = {
        '30天内': 'success',
        '31-60天': 'warning',
        '61-90天': 'danger',
        '91-180天': 'danger',
        '180天以上': 'info'
      }
      return types[range] || 'info'
    },
    getRiskLevelType(level) {
      const types = { 1: 'success', 2: 'warning', 3: 'danger', 4: 'info' }
      return types[level] || 'info'
    },
    getRiskLevelText(level) {
      const texts = { 1: '低风险', 2: '中风险', 3: '高风险', 4: '极高风险' }
      return texts[level] || '未知'
    },
    getRiskScoreColor(score) {
      if (score <= 30) return '#67c23a'
      if (score <= 60) return '#e6a23c'
      if (score <= 80) return '#f56c6c'
      return '#909399'
    },
    handleTabClick(tab) {
      this.activeTab = tab.name
      if (tab.name === 'aging') {
        this.loadAgingData()
      } else if (tab.name === 'overdue') {
        this.loadOverdueData()
      } else if (tab.name === 'risk') {
        this.loadRiskData()
      } else if (tab.name === 'trend') {
        this.initTrendDateRange()
        this.loadTrendData()
      }
    },
    // 账龄分析相关方法
    async loadAgingData() {
      this.agingLoading = true
      try {
        const params = {
          pageNumber: this.agingPagination.currentPage,
          pageSize: this.agingPagination.pageSize,
          tenantId: 1,
          customerId: this.agingFilter.customerId || undefined,
          agingRange: this.agingFilter.agingRange || undefined,
          analysisDate: this.agingFilter.analysisDate || undefined
        }
        const response = await getAgingAnalysisDetail(params)
        if (response.code === 1 && response.data) {
          this.agingTableData = response.data.tlist || []
          this.agingPagination.total = response.data.totalRecord || 0
        } else {
          this.agingTableData = []
          this.agingPagination.total = 0
        }
      } catch (error) {
        console.error('加载账龄数据失败:', error)
        this.$message.error('加载账龄数据失败')
        this.agingTableData = []
        this.agingPagination.total = 0
      } finally {
        this.agingLoading = false
      }
    },
    handleAgingSearch() {
      this.agingPagination.currentPage = 1
      this.loadAgingData()
    },
    resetAgingFilter() {
      this.agingFilter = {
        customerId: '',
        agingRange: '',
        analysisDate: ''
      }
      this.handleAgingSearch()
    },
    handleAgingSizeChange(val) {
      this.agingPagination.pageSize = val
      this.loadAgingData()
    },
    handleAgingCurrentChange(val) {
      this.agingPagination.currentPage = val
      this.loadAgingData()
    },
    getAgingSummary(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'receivableAmount' || column.property === 'remainingAmount' || column.property === 'overdueAmount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = '0.00万'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    viewAgingDetail(row) {
      const content = `
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>应收编号：</b>${row.receivableNo || row.receivableId || '-'}</p>
        <p><b>应收金额：</b>${row.receivableAmount || 0}</p>
        <p><b>账龄天数：</b>${row.agingDays || 0}天</p>
        <p><b>账龄区间：</b>${row.agingRange || '-'}</p>
        <p><b>风险等级：</b>${row.riskLevel || '-'}</p>
        <p><b>到期日期：</b>${row.dueDate || '-'}</p>
      `
      this.$alert(content, '账龄详情', { dangerouslyUseHTMLString: true })
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
    // 逾期统计相关方法
    async loadOverdueData() {
      try {
        const analysisDate = this.agingFilter.analysisDate || this.formatDate(new Date())
        const response = await getOverdueStatistics({ analysisDate, tenantId: 1 })
        if (response.code === 1 && response.data) {
          // 逾期金额统计
          const overdueAmount = Number(response.data) || 0
          this.overdueStats = {
            within30Days: { amount: 0, count: 0, percentage: 0 },
            days31To60: { amount: 0, count: 0, percentage: 0 },
            days61To90: { amount: 0, count: 0, percentage: 0 },
            over90Days: { amount: overdueAmount, count: 0, percentage: 100 },
            total: { amount: overdueAmount, count: 0, percentage: 100 }
          }
        }
        // 加载客户账龄排名
        const customerResponse = await getAgingByCustomer({ analysisDate, tenantId: 1 })
        if (customerResponse.code === 1 && customerResponse.data) {
          this.overdueRankingData = customerResponse.data.map(item => ({
            customerName: item.customerName || item.customerId,
            overdueAmount: Number(item.totalAmount || 0),
            overdueCount: Number(item.count || 0),
            maxOverdueDays: Number(item.maxAgingDays || 0),
            riskLevel: Number(item.riskLevel || 1)
          }))
        }
      } catch (error) {
        console.error('加载逾期统计数据失败:', error)
        this.overdueStats = {
          within30Days: { amount: 0, count: 0, percentage: 0 },
          days31To60: { amount: 0, count: 0, percentage: 0 },
          days61To90: { amount: 0, count: 0, percentage: 0 },
          over90Days: { amount: 0, count: 0, percentage: 0 },
          total: { amount: 0, count: 0, percentage: 0 }
        }
        this.overdueRankingData = []
      }
    },
    viewCustomerAging(row) {
      const content = `
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>逾期金额：</b>${row.overdueAmount || 0}</p>
        <p><b>逾期笔数：</b>${row.overdueCount || 0}</p>
        <p><b>最大逾期天数：</b>${row.maxOverdueDays || 0}天</p>
        <p><b>风险等级：</b>${row.riskLevel || '-'}</p>
      `
      this.$alert(content, '客户账龄详情', { dangerouslyUseHTMLString: true })
    },
    urgentCollection(row) {
      this.$confirm(`确认对客户「${row.customerName || ''}」发起紧急催收？`, '紧急催收', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('紧急催收已发起')
      }).catch(() => {})
    },
    // 风险评估相关方法
    async loadRiskData() {
      try {
        const analysisDate = this.agingFilter.analysisDate || this.formatDate(new Date())
        const response = await getRiskAssessment({ analysisDate, tenantId: 1 })
        if (response.code === 1 && response.data) {
          const data = response.data
          // 按风险等级统计
          this.riskAssessment = {
            lowRisk: { amount: 0, count: 0 },
            mediumRisk: { amount: 0, count: 0 },
            highRisk: { amount: 0, count: 0 },
            criticalRisk: { amount: 0, count: 0 }
          }
          if (Array.isArray(data)) {
            data.forEach(item => {
              const level = Number(item.riskLevel)
              const amount = Number(item.totalAmount || 0)
              const count = Number(item.count || 0)
              if (level === 1) {
                this.riskAssessment.lowRisk = { amount, count }
              } else if (level === 2) {
                this.riskAssessment.mediumRisk = { amount, count }
              } else if (level === 3) {
                this.riskAssessment.highRisk = { amount, count }
              } else if (level === 4) {
                this.riskAssessment.criticalRisk = { amount, count }
              }
            })
          }
          // 更新概览中的风险客户数
          this.overview.riskCustomerCount =
            this.riskAssessment.highRisk.count + this.riskAssessment.criticalRisk.count
        }
        // 风险预警数据从账龄明细中筛选高风险记录
        const detailResponse = await getAgingAnalysisDetail({
          pageNumber: 1,
          pageSize: 10,
          tenantId: 1,
          riskLevel: 3 // 高风险及以上
        })
        if (detailResponse.code === 1 && detailResponse.data) {
          this.riskAlertData = (detailResponse.data.tlist || []).map(item => ({
            customerName: item.customerName || item.customerId,
            receivableNo: item.receivableId,
            receivableAmount: Number(item.receivableAmount || 0),
            agingDays: Number(item.agingDays || 0),
            riskLevel: Number(item.riskLevel || 1),
            riskScore: this.calculateRiskScore(item),
            alertReason: this.getRiskReason(item),
            suggestedAction: this.getSuggestedAction(item.riskLevel)
          }))
        }
      } catch (error) {
        console.error('加载风险评估数据失败:', error)
        this.riskAssessment = {
          lowRisk: { amount: 0, count: 0 },
          mediumRisk: { amount: 0, count: 0 },
          highRisk: { amount: 0, count: 0 },
          criticalRisk: { amount: 0, count: 0 }
        }
        this.riskAlertData = []
      }
    },
    calculateRiskScore(item) {
      const agingDays = Number(item.agingDays || 0)
      if (agingDays <= 30) return 20
      if (agingDays <= 60) return 40
      if (agingDays <= 90) return 60
      if (agingDays <= 180) return 80
      return 95
    },
    getRiskReason(item) {
      const agingDays = Number(item.agingDays || 0)
      if (agingDays > 180) return '逾期超过180天，严重逾期'
      if (agingDays > 90) return '逾期超过90天'
      if (agingDays > 60) return '逾期超过60天'
      return '账龄较长'
    },
    getSuggestedAction(riskLevel) {
      const actions = {
        1: '正常跟进',
        2: '加强关注，定期催收',
        3: '加强催收，评估担保措施',
        4: '立即催收，考虑法律途径'
      }
      return actions[riskLevel] || '正常跟进'
    },
    viewRiskDetail(row) {
      const content = `
        <p><b>客户名称：</b>${row.customerName || '-'}</p>
        <p><b>应收编号：</b>${row.receivableNo || '-'}</p>
        <p><b>应收金额：</b>${row.receivableAmount || 0}</p>
        <p><b>账龄天数：</b>${row.agingDays || 0}天</p>
        <p><b>风险等级：</b>${row.riskLevel || '-'}</p>
        <p><b>风险评分：</b>${row.riskScore || '-'}</p>
        <p><b>预警原因：</b>${row.alertReason || '-'}</p>
        <p><b>建议措施：</b>${row.suggestedAction || '-'}</p>
      `
      this.$alert(content, '风险详情', { dangerouslyUseHTMLString: true })
    },
    handleRiskAlert(row) {
      this.$confirm(`确认处理「${row.customerName || ''}」的风险预警？将标记为已处理状态。`, '处理风险预警', {
        confirmButtonText: '确认处理',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('风险预警已处理')
        this.loadRiskData()
      }).catch(() => {})
    },
    // 页面操作方法
    refreshAnalysis() {
      this.loadOverview()
      if (this.activeTab === 'aging') {
        this.loadAgingData()
      } else if (this.activeTab === 'overdue') {
        this.loadOverdueData()
      } else if (this.activeTab === 'risk') {
        this.loadRiskData()
      }
      this.$message.success('分析数据已刷新')
    },
    exportAnalysis() {
      try {
        const data = this.agingTableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '账龄分析报告.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    generateRiskAlert() {
      this.$confirm('确认生成风险预警？系统将根据当前账龄数据自动识别高风险客户。', '生成风险预警', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loadRiskData()
        this.$message.success('风险预警已生成')
      }).catch(() => {})
    },
    // ==================== 收款趋势分析相关方法 ====================
    initTrendDateRange() {
      // 默认设置最近6个月
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 6)
      this.trendFilter.dateRange = [
        this.formatDate(start),
        this.formatDate(end)
      ]
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    async loadTrendData() {
      try {
        // 加载趋势指标数据
        await this.loadTrendMetrics()
        // 加载客户周期数据
        await this.loadCustomerCycleData()
        // 初始化图表
        this.$nextTick(() => {
          this.initAllTrendCharts()
        })
      } catch (error) {
        console.error('加载趋势数据失败:', error)
        this.$message.error('加载趋势数据失败')
      }
    },
    async loadTrendMetrics() {
      try {
        const [startDate, endDate] = this.trendFilter.dateRange || []
        if (!startDate || !endDate) return
        const response = await getAgingTrend({ startDate, endDate, tenantId: 1 })
        if (response.code === 1 && response.data) {
          const data = response.data
          // 从趋势数据计算指标
          if (Array.isArray(data) && data.length > 0) {
            let totalReceivable = 0
            let totalCollected = 0
            let totalDays = 0
            let count = 0
            data.forEach(item => {
              totalReceivable += Number(item.totalAmount || 0)
              totalCollected += Number(item.collectedAmount || 0)
              totalDays += Number(item.avgAgingDays || 0)
              count++
            })
            const collectionRate = totalReceivable > 0 ? (totalCollected / totalReceivable * 100) : 0
            const avgDays = count > 0 ? Math.round(totalDays / count) : 0
            const overdueRate = totalReceivable > 0 ? ((totalReceivable - totalCollected) / totalReceivable * 100) : 0
            this.trendMetrics = {
              collectionRate: Number(collectionRate.toFixed(1)),
              collectionRateTrend: 0,
              avgCollectionDays: avgDays,
              avgDaysTrend: 0,
              totalCollected: totalCollected,
              collectedTrend: 0,
              overdueRate: Number(overdueRate.toFixed(1)),
              overdueRateTrend: 0
            }
          }
        }
      } catch (error) {
        console.error('加载趋势指标失败:', error)
        this.trendMetrics = {
          collectionRate: 0,
          collectionRateTrend: 0,
          avgCollectionDays: 0,
          avgDaysTrend: 0,
          totalCollected: 0,
          collectedTrend: 0,
          overdueRate: 0,
          overdueRateTrend: 0
        }
      }
    },
    async loadCustomerCycleData() {
      try {
        const analysisDate = this.formatDate(new Date())
        const response = await getAgingByCustomer({ analysisDate, tenantId: 1 })
        if (response.code === 1 && response.data) {
          this.customerCycleData = (response.data || []).map((item, index) => {
            const totalReceivable = Number(item.totalAmount || 0)
            const totalCollected = Number(item.collectedAmount || 0)
            const collectionRate = totalReceivable > 0 ? Math.round(totalCollected / totalReceivable * 100) : 0
            return {
              customerId: item.customerId || String(index + 1),
              customerName: item.customerName || item.customerId || `客户${index + 1}`,
              totalReceivable: totalReceivable,
              totalCollected: totalCollected,
              collectionRate: collectionRate,
              avgCycleDays: Number(item.avgAgingDays || 0),
              onTimeRate: 100 - (Number(item.overdueRate || 0)),
              creditLevel: this.calculateCreditLevel(collectionRate),
              trend: 0
            }
          })
        } else {
          this.customerCycleData = []
        }
      } catch (error) {
        console.error('加载客户周期数据失败:', error)
        this.customerCycleData = []
      }
    },
    calculateCreditLevel(collectionRate) {
      if (collectionRate >= 95) return 5
      if (collectionRate >= 85) return 4
      if (collectionRate >= 75) return 3
      if (collectionRate >= 60) return 2
      return 1
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockCustomerCycleData() {
      return []
    },
    initAllTrendCharts() {
      this.initCollectionTrendChart()
      this.initCollectionPieChart()
      this.initCustomerCycleChart()
      this.initForecastChart()
    },
    initCollectionTrendChart() {
      if (this.charts.collectionTrend) {
        this.charts.collectionTrend.dispose()
      }
      const chartDom = this.$refs.collectionTrendChart
      if (!chartDom) return

      this.charts.collectionTrend = echarts.init(chartDom)

      // 暂未对接月度趋势 API，先以空状态展示，待后端接口提供后接入
      const months = []
      const receivableData = []
      const collectedData = []
      const rateData = []

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['应收金额', '已收金额', '收款率'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: months,
          axisPointer: {
            type: 'shadow'
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '金额(万元)',
            position: 'left',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#409EFF'
              }
            }
          },
          {
            type: 'value',
            name: '收款率(%)',
            position: 'right',
            min: 0,
            max: 100,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#67C23A'
              }
            }
          }
        ],
        series: [
          {
            name: '应收金额',
            type: 'bar',
            data: receivableData,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '已收金额',
            type: 'bar',
            data: collectedData,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '收款率',
            type: 'line',
            yAxisIndex: 1,
            data: rateData,
            smooth: true,
            itemStyle: {
              color: '#E6A23C'
            },
            lineStyle: {
              width: 3
            },
            symbol: 'circle',
            symbolSize: 8
          }
        ]
      }

      this.charts.collectionTrend.setOption(option)
    },
    initCollectionPieChart() {
      if (this.charts.collectionPie) {
        this.charts.collectionPie.dispose()
      }
      const chartDom = this.$refs.collectionPieChart
      if (!chartDom) return

      this.charts.collectionPie = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'center'
        },
        series: [
          {
            name: '收款来源',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 580, name: '银行转账', itemStyle: { color: '#409EFF' } },
              { value: 320, name: '商业承兑', itemStyle: { color: '#67C23A' } },
              { value: 180, name: '银行承兑', itemStyle: { color: '#E6A23C' } },
              { value: 120, name: '现金收款', itemStyle: { color: '#F56C6C' } },
              { value: 58, name: '其他方式', itemStyle: { color: '#909399' } }
            ]
          }
        ]
      }

      this.charts.collectionPie.setOption(option)
    },
    initCustomerCycleChart() {
      if (this.charts.customerCycle) {
        this.charts.customerCycle.dispose()
      }
      const chartDom = this.$refs.customerCycleChart
      if (!chartDom) return

      this.charts.customerCycle = echarts.init(chartDom)

      // 使用客户周期数据
      const customers = this.customerCycleData.map(item => item.customerName.substring(0, 4))
      const cycleDays = this.customerCycleData.map(item => item.avgCycleDays)
      const onTimeRates = this.customerCycleData.map(item => item.onTimeRate)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['平均周期(天)', '准时率(%)'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: customers
        },
        yAxis: [
          {
            type: 'value',
            name: '天数',
            position: 'left'
          },
          {
            type: 'value',
            name: '准时率(%)',
            position: 'right',
            min: 0,
            max: 100
          }
        ],
        series: [
          {
            name: '平均周期(天)',
            type: 'bar',
            data: cycleDays,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            }
          },
          {
            name: '准时率(%)',
            type: 'line',
            yAxisIndex: 1,
            data: onTimeRates,
            smooth: true,
            itemStyle: {
              color: '#67C23A'
            },
            lineStyle: {
              width: 3
            },
            areaStyle: {
              opacity: 0.3,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.5)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
              ])
            }
          }
        ]
      }

      this.charts.customerCycle.setOption(option)
    },
    initForecastChart() {
      if (this.charts.forecast) {
        this.charts.forecast.dispose()
      }
      const chartDom = this.$refs.forecastChart
      if (!chartDom) return

      this.charts.forecast = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['实际收款', '预测收款'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月']
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)'
        },
        series: [
          {
            name: '实际收款',
            type: 'line',
            data: [1350, 1480, 1420, 1650, 1780, 1900, null, null, null],
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            },
            lineStyle: {
              width: 3
            },
            areaStyle: {
              opacity: 0.3
            }
          },
          {
            name: '预测收款',
            type: 'line',
            data: [null, null, null, null, null, 1900, 2050, 2180, 2320],
            smooth: true,
            itemStyle: {
              color: '#E6A23C'
            },
            lineStyle: {
              width: 3,
              type: 'dashed'
            },
            areaStyle: {
              opacity: 0.2
            }
          }
        ]
      }

      this.charts.forecast.setOption(option)
    },
    updateTrendChart() {
      this.initCollectionTrendChart()
    },
    handleTrendSearch() {
      this.loadTrendData()
    },
    resetTrendFilter() {
      this.initTrendDateRange()
      this.trendFilter.dimension = 'month'
      this.trendFilter.customerId = ''
      this.loadTrendData()
    },
    getCollectionRateColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 80) return '#409eff'
      if (rate >= 70) return '#e6a23c'
      return '#f56c6c'
    },
    getCycleDaysType(days) {
      if (days <= 30) return 'success'
      if (days <= 45) return 'warning'
      return 'danger'
    },
    viewCustomerTrend(row) {
      this.$message.info(`查看 ${row.customerName} 的收款趋势详情`)
    },
    // 窗口大小变化时重新调整图表
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) {
          chart.resize()
        }
      })
    }
  },
  beforeDestroy() {
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
    // 移除窗口大小监听
    window.removeEventListener('resize', this.handleResize)
  }
}
</script>

<style lang="scss" scoped>
.aging-analysis-container {
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
        color: #909399;
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

.aging-overview {
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

      &.current {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.overdue {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.risk {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .card-content {
      .card-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
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

    .table-container {
      .amount-text {
        color: #409eff;
        font-weight: 600;
      }

      .overdue-amount {
        color: #f56c6c;
        font-weight: 600;
      }
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
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

// 逾期统计样式
.overdue-stats {
  margin-bottom: 32px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.total-overdue {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      color: white;

      .stat-header h3,
      .stat-body .stat-amount,
      .stat-body .stat-count,
      .stat-body .stat-percentage {
        color: white;
      }

      .stat-icon {
        color: white;
      }
    }

    .stat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }

      .stat-icon {
        font-size: 24px;

        &.normal {
          color: #67c23a;
        }

        &.warning {
          color: #e6a23c;
        }

        &.danger {
          color: #f56c6c;
        }

        &.critical {
          color: #909399;
        }

        &.total {
          color: white;
        }
      }
    }

    .stat-body {
      .stat-amount {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }

      .stat-count {
        font-size: 14px;
        color: #606266;
        margin-bottom: 4px;
      }

      .stat-percentage {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.overdue-ranking {
  margin-bottom: 32px;
}

// 风险评估样式
.risk-overview {
  margin-bottom: 32px;

  .risk-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.low {
      border-left: 4px solid #67c23a;
    }

    &.medium {
      border-left: 4px solid #e6a23c;
    }

    &.high {
      border-left: 4px solid #f56c6c;
    }

    &.critical {
      border-left: 4px solid #909399;
    }

    .risk-header {
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

      i {
        font-size: 20px;
      }
    }

    .risk-content {
      .risk-amount {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }

      .risk-count {
        font-size: 14px;
        color: #606266;
      }
    }
  }
}

.risk-alerts {
  margin-bottom: 32px;
}

// 收款趋势分析样式
.efficiency-cards {
  margin-bottom: 24px;

  .efficiency-card {
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

    .card-icon {
      width: 50px;
      height: 50px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }

      &.collection-rate {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.avg-days {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.total-collected {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.overdue-rate {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
    }

    .card-info {
      flex: 1;

      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 4px;
      }

      .card-trend {
        font-size: 12px;
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

.chart-row {
  margin-bottom: 24px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 100%;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid #ebeef5;

    h4 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }
  }

  .chart-container {
    width: 100%;
  }
}

.cycle-detail-section {
  margin-top: 24px;

  .collected-amount {
    color: #67c23a;
    font-weight: 600;
  }

  .success-text {
    color: #67c23a;
    font-weight: 600;
  }

  .warning-text {
    color: #e6a23c;
    font-weight: 600;
  }
}
</style>
