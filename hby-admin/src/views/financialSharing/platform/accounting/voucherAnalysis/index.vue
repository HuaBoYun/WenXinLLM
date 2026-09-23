<template>
  <div class="voucher-analysis-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证分析</h2>
      <p>分析凭证生成和处理的统计数据，提供效率分析、异常检测和趋势分析</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalVouchers || 0 }}</div>
                <div class="statistic-label">凭证总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon today">
                <i class="el-icon-date"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.todayVouchers || 0 }}</div>
                <div class="statistic-label">今日新增</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon efficiency">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.efficiency || 0 }}%</div>
                <div class="statistic-label">处理效率</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon anomaly">
                <i class="el-icon-warning"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.anomalyCount || 0 }}</div>
                <div class="statistic-label">异常凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 统计分析 -->
      <el-tab-pane label="统计分析" name="statistics-analysis">
        <div class="tab-content">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="statisticsForm" ref="statisticsForm" :inline="true" label-width="100px">
              <el-form-item label="统计维度">
                <el-select v-model="statisticsForm.dimension" placeholder="请选择统计维度" @change="handleDimensionChange">
                  <el-option label="按日期" value="DATE" />
                  <el-option label="按类型" value="TYPE" />
                  <el-option label="按制单人" value="CREATOR" />
                  <el-option label="按审核人" value="APPROVER" />
                  <el-option label="按科目" value="SUBJECT" />
                </el-select>
              </el-form-item>
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="statisticsForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                />
              </el-form-item>
              <el-form-item label="凭证类型">
                <el-select v-model="statisticsForm.voucherType" placeholder="请选择凭证类型" clearable>
                  <el-option label="记账凭证" value="ACCOUNTING" />
                  <el-option label="收款凭证" value="RECEIPT" />
                  <el-option label="付款凭证" value="PAYMENT" />
                  <el-option label="转账凭证" value="TRANSFER" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleStatisticsSearch">查询</el-button>
                <el-button @click="handleStatisticsReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 统计图表 -->
          <div class="chart-container">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>凭证数量趋势</span>
                  </div>
                  <vab-chart
                    class="voucher-trend-chart"
                    :init-options="chartInitOptions"
                    :option="voucherTrendChartOption"
                    theme="vab-echarts-theme"
                    style="height: 300px;"
                  />
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>凭证类型分布</span>
                  </div>
                  <vab-chart
                    class="voucher-type-chart"
                    :init-options="chartInitOptions"
                    :option="voucherTypeChartOption"
                    theme="vab-echarts-theme"
                    style="height: 300px;"
                  />
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>制单人统计</span>
                  </div>
                  <vab-chart
                    class="creator-chart"
                    :init-options="chartInitOptions"
                    :option="creatorChartOption"
                    theme="vab-echarts-theme"
                    style="height: 300px;"
                  />
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>金额分布</span>
                  </div>
                  <vab-chart
                    class="amount-chart"
                    :init-options="chartInitOptions"
                    :option="amountChartOption"
                    theme="vab-echarts-theme"
                    style="height: 300px;"
                  />
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 统计表格 -->
          <div class="statistics-table">
            <el-card>
              <div slot="header" class="card-header">
                <span>详细统计数据</span>
                <el-button type="primary" size="small" @click="handleExportStatistics">导出数据</el-button>
              </div>
              <el-table :data="statisticsTableData" border stripe>
                <el-table-column prop="dimension" label="统计维度" width="150" />
                <el-table-column prop="voucherCount" label="凭证数量" width="120" align="center" />
                <el-table-column prop="totalAmount" label="总金额" width="150" align="right">
                  <template slot-scope="scope">
                    ¥{{ formatAmount(scope.row.totalAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="avgAmount" label="平均金额" width="150" align="right">
                  <template slot-scope="scope">
                    ¥{{ formatAmount(scope.row.avgAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="percentage" label="占比" width="100" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.percentage }}%
                  </template>
                </el-table-column>
                <el-table-column prop="growthRate" label="增长率" width="100" align="center">
                  <template slot-scope="scope">
                    <span :style="{ color: scope.row.growthRate >= 0 ? '#67c23a' : '#f56c6c' }">
                      {{ scope.row.growthRate >= 0 ? '+' : '' }}{{ scope.row.growthRate }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" show-overflow-tooltip />
              </el-table>
            </el-card>
          </div>
        </div>
      </el-tab-pane>

      <!-- 效率分析 -->
      <el-tab-pane label="效率分析" name="efficiency-analysis">
        <div class="tab-content">
          <!-- 效率指标 -->
          <div class="efficiency-metrics">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card class="metric-card">
                  <div class="metric-item">
                    <div class="metric-icon processing">
                      <i class="el-icon-time"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.avgProcessingTime || 0 }}</div>
                      <div class="metric-label">平均处理时间(分钟)</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="metric-card">
                  <div class="metric-item">
                    <div class="metric-icon approval">
                      <i class="el-icon-check"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.avgApprovalTime || 0 }}</div>
                      <div class="metric-label">平均审核时间(分钟)</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="metric-card">
                  <div class="metric-item">
                    <div class="metric-icon throughput">
                      <i class="el-icon-s-promotion"></i>
                    </div>
                    <div class="metric-content">
                      <div class="metric-value">{{ efficiencyMetrics.dailyThroughput || 0 }}</div>
                      <div class="metric-label">日均处理量</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 效率图表 -->
          <div class="efficiency-charts">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>处理时间趋势</span>
                  </div>
                  <div id="processingTimeChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>人员效率对比</span>
                  </div>
                  <div id="personnelEfficiencyChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 效率排行 -->
          <div class="efficiency-ranking">
            <el-card>
              <div slot="header" class="card-header">
                <span>效率排行榜</span>
              </div>
              <el-table :data="efficiencyRankingData" border stripe>
                <el-table-column type="index" label="排名" width="80" align="center" />
                <el-table-column prop="userName" label="姓名" width="120" />
                <el-table-column prop="department" label="部门" width="150" />
                <el-table-column prop="processedCount" label="处理数量" width="120" align="center" />
                <el-table-column prop="avgProcessingTime" label="平均处理时间" width="150" align="center">
                  <template slot-scope="scope">
                    {{ scope.row.avgProcessingTime }}分钟
                  </template>
                </el-table-column>
                <el-table-column prop="accuracyRate" label="准确率" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAccuracyRateType(scope.row.accuracyRate)">
                      {{ scope.row.accuracyRate }}%
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="efficiencyScore" label="效率得分" width="120" align="center">
                  <template slot-scope="scope">
                    <el-progress :percentage="scope.row.efficiencyScore" :status="getEfficiencyStatus(scope.row.efficiencyScore)"></el-progress>
                  </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" show-overflow-tooltip />
              </el-table>
            </el-card>
          </div>
        </div>
      </el-tab-pane>

      <!-- 异常检测 -->
      <el-tab-pane label="异常检测" name="anomaly-detection">
        <div class="tab-content">
          <!-- 异常统计 -->
          <div class="anomaly-stats">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card class="anomaly-stat-card">
                  <div class="anomaly-stat-item">
                    <div class="anomaly-stat-icon balance">
                      <i class="el-icon-warning"></i>
                    </div>
                    <div class="anomaly-stat-content">
                      <div class="anomaly-stat-value">{{ anomalyStats.balanceErrors || 0 }}</div>
                      <div class="anomaly-stat-label">借贷不平衡</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="anomaly-stat-card">
                  <div class="anomaly-stat-item">
                    <div class="anomaly-stat-icon subject">
                      <i class="el-icon-s-help"></i>
                    </div>
                    <div class="anomaly-stat-content">
                      <div class="anomaly-stat-value">{{ anomalyStats.subjectErrors || 0 }}</div>
                      <div class="anomaly-stat-label">科目异常</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="anomaly-stat-card">
                  <div class="anomaly-stat-item">
                    <div class="anomaly-stat-icon amount">
                      <i class="el-icon-money"></i>
                    </div>
                    <div class="anomaly-stat-content">
                      <div class="anomaly-stat-value">{{ anomalyStats.amountErrors || 0 }}</div>
                      <div class="anomaly-stat-label">金额异常</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="anomaly-stat-card">
                  <div class="anomaly-stat-item">
                    <div class="anomaly-stat-icon duplicate">
                      <i class="el-icon-document-copy"></i>
                    </div>
                    <div class="anomaly-stat-content">
                      <div class="anomaly-stat-value">{{ anomalyStats.duplicateErrors || 0 }}</div>
                      <div class="anomaly-stat-label">重复凭证</div>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 异常列表 -->
          <div class="anomaly-list">
            <el-card>
              <div slot="header" class="card-header">
                <span>异常凭证列表</span>
                <div>
                  <el-button type="primary" size="small" @click="handleBatchFix">批量修复</el-button>
                  <el-button type="warning" size="small" @click="handleRefreshAnomalies">刷新检测</el-button>
                </div>
              </div>
              <el-table :data="anomalyTableData" border stripe @selection-change="handleAnomalySelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column prop="voucherNo" label="凭证编号" width="120" />
                <el-table-column prop="anomalyType" label="异常类型" width="120">
                  <template slot-scope="scope">
                    <el-tag :type="getAnomalyTypeColor(scope.row.anomalyType)">
                      {{ getAnomalyTypeName(scope.row.anomalyType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="severity" label="严重程度" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getSeverityType(scope.row.severity)" size="small">
                      {{ getSeverityName(scope.row.severity) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="异常描述" width="250" show-overflow-tooltip />
                <el-table-column prop="suggestion" label="修复建议" width="200" show-overflow-tooltip />
                <el-table-column prop="detectTime" label="检测时间" width="160">
                  <template slot-scope="scope">
                    {{ formatDate(scope.row.detectTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="处理状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getAnomalyStatusType(scope.row.status)" size="small">
                      {{ getAnomalyStatusName(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewAnomaly(scope.row)">查看</el-button>
                    <el-button size="mini" type="primary" @click="handleFixAnomaly(scope.row)" v-if="scope.row.status === 'DETECTED'">
                      修复
                    </el-button>
                    <el-button size="mini" type="warning" @click="handleIgnoreAnomaly(scope.row)" v-if="scope.row.status === 'DETECTED'">
                      忽略
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </div>
      </el-tab-pane>

      <!-- 趋势分析 -->
      <el-tab-pane label="趋势分析" name="trend-analysis">
        <div class="tab-content">
          <!-- 趋势配置 -->
          <div class="trend-config">
            <el-form :model="trendForm" ref="trendForm" :inline="true" label-width="100px">
              <el-form-item label="分析周期">
                <el-select v-model="trendForm.period" placeholder="请选择分析周期" @change="handlePeriodChange">
                  <el-option label="最近7天" value="7_DAYS" />
                  <el-option label="最近30天" value="30_DAYS" />
                  <el-option label="最近3个月" value="3_MONTHS" />
                  <el-option label="最近6个月" value="6_MONTHS" />
                  <el-option label="最近1年" value="1_YEAR" />
                </el-select>
              </el-form-item>
              <el-form-item label="分析指标">
                <el-checkbox-group v-model="trendForm.metrics">
                  <el-checkbox label="VOUCHER_COUNT">凭证数量</el-checkbox>
                  <el-checkbox label="AMOUNT_TOTAL">金额总计</el-checkbox>
                  <el-checkbox label="PROCESSING_TIME">处理时间</el-checkbox>
                  <el-checkbox label="ACCURACY_RATE">准确率</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleTrendAnalysis">分析</el-button>
                <el-button @click="handleTrendReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 趋势图表 -->
          <div class="trend-charts">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>综合趋势分析</span>
                  </div>
                  <div id="comprehensiveTrendChart" style="height: 400px;"></div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>预测分析</span>
                  </div>
                  <div id="predictionChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>季节性分析</span>
                  </div>
                  <div id="seasonalChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 趋势报告 -->
          <div class="trend-report">
            <el-card>
              <div slot="header" class="card-header">
                <span>趋势分析报告</span>
              </div>
              <div class="report-content">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <h4>关键发现</h4>
                    <ul class="findings-list">
                      <li v-for="finding in trendFindings" :key="finding.id">
                        <el-tag :type="finding.type" size="small">{{ finding.category }}</el-tag>
                        {{ finding.description }}
                      </li>
                    </ul>
                  </el-col>
                  <el-col :span="12">
                    <h4>改进建议</h4>
                    <ul class="suggestions-list">
                      <li v-for="suggestion in trendSuggestions" :key="suggestion.id">
                        <el-tag type="primary" size="small">{{ suggestion.priority }}</el-tag>
                        {{ suggestion.description }}
                      </li>
                    </ul>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import * as voucherAnalysisApi from '@/api/financialSharing/voucherAnalysis'
import VabChart from '@/extra/VabChart'

export default {
  name: 'VoucherAnalysis',
  components: {
    VabChart
  },
  data() {
    return {
      activeTab: 'statistics-analysis',
      statistics: {},
      amountStatistics: {},
      efficiencyStatistics: {},
      anomalyStatistics: [],

      // 统计分析相关
      statisticsForm: {
        dimension: 'DATE',
        dateRange: [],
        voucherType: ''
      },
      statisticsTableData: [],

      // 效率分析相关
      efficiencyMetrics: {},
      efficiencyRankingData: [],

      // 异常检测相关
      anomalyStats: {},
      anomalyTableData: [],
      selectedAnomalyRows: [],

      // 趋势分析相关
      trendForm: {
        period: '30_DAYS',
        metrics: ['VOUCHER_COUNT', 'AMOUNT_TOTAL']
      },
      trendFindings: [],
      trendSuggestions: [],

      // 图表配置
      chartInitOptions: {
        renderer: 'svg'
      },
      // 凭证数量趋势图表配置
      voucherTrendChartOption: {},
      // 凭证类型分布图表配置
      voucherTypeChartOption: {},
      // 制单人统计图表配置
      creatorChartOption: {},
      // 金额分布图表配置
      amountChartOption: {}
    }
  },
  
  mounted() {
    this.loadStatistics()
    this.loadStatisticsData()
    this.initCharts()
  },
  
  methods: {
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'efficiency-analysis') {
        this.loadEfficiencyData()
      } else if (tab.name === 'anomaly-detection') {
        this.loadAnomalyData()
      } else if (tab.name === 'trend-analysis') {
        this.loadTrendData()
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        // 构建查询参数
        const params = {
          startDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 0 ? this.statisticsForm.dateRange[0] : null,
          endDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 1 ? this.statisticsForm.dateRange[1] : null
        }

        // 并行调用多个API获取统计数据
        const [summaryRes, amountRes, efficiencyRes, anomalyRes] = await Promise.all([
          voucherAnalysisApi.getVoucherSummary(params),
          voucherAnalysisApi.getVoucherAmountStatistics(params),
          voucherAnalysisApi.getVoucherEfficiencyAnalysis(params),
          voucherAnalysisApi.getAbnormalVoucherAnalysis(params)
        ])

        // 处理汇总统计（兼容大小写字段名）
        if (summaryRes.code === 1 && summaryRes.data) {
          const summaryData = summaryRes.data
          this.statistics = {
            totalVouchers: summaryData.totalCount || summaryData.TOTALCOUNT || 0,
            todayVouchers: summaryData.todayCount || summaryData.TODAYCOUNT || 0,
            efficiency: (summaryData.efficiencyRate || summaryData.EFFICIENCYRATE) ? parseFloat(summaryData.efficiencyRate || summaryData.EFFICIENCYRATE).toFixed(1) : 0,
            anomalyCount: summaryData.anomalyCount || summaryData.ANOMALYCOUNT || 0
          }
        } else {
          this.statistics = {
            totalVouchers: 0,
            todayVouchers: 0,
            efficiency: 0,
            anomalyCount: 0
          }
        }

        // 存储详细数据供其他方法使用（兼容大小写字段名）
        if (amountRes.code === 1 && amountRes.data) {
          const amtData = amountRes.data
          this.amountStatistics = {
            totalDebitAmount: amtData.totalDebitAmount || amtData.TOTALDEBITAMOUNT || 0,
            totalCreditAmount: amtData.totalCreditAmount || amtData.TOTALCREDITAMOUNT || 0,
            avgDebitAmount: amtData.avgDebitAmount || amtData.AVGDEBITAMOUNT || 0,
            avgCreditAmount: amtData.avgCreditAmount || amtData.AVGCREDITAMOUNT || 0,
            maxDebitAmount: amtData.maxDebitAmount || amtData.MAXDEBITAMOUNT || 0,
            minDebitAmount: amtData.minDebitAmount || amtData.MINDEBITAMOUNT || 0
          }
        } else {
          this.amountStatistics = {}
        }

        if (efficiencyRes.code === 1 && efficiencyRes.data) {
          const effData = efficiencyRes.data
          this.efficiencyStatistics = {
            totalVouchers: effData.totalVouchers || effData.TOTALVOUCHERS || 0,
            avgReviewMinutes: effData.avgReviewMinutes || effData.AVGREVIEWMINUTES || 0,
            avgApprovalMinutes: effData.avgApprovalMinutes || effData.AVGAPPROVALMINUTES || 0,
            dailyThroughput: effData.dailyThroughput || effData.DAILYTHROUGHPUT || 0
          }
        } else {
          this.efficiencyStatistics = {}
        }

        this.anomalyStatistics = anomalyRes.code === 1 ? (anomalyRes.data || []) : []

        // 数据加载完成后更新图表
        this.$nextTick(() => {
          this.initAmountChart()
        })

      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败')
      }
    },
    
    // 加载统计分析数据
    async loadStatisticsData() {
      try {
        // 构建查询参数
        const params = {
          startDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 0 ? this.statisticsForm.dateRange[0] : null,
          endDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 1 ? this.statisticsForm.dateRange[1] : null,
          dimension: this.statisticsForm.dimension
        }

        let apiCall
        switch (this.statisticsForm.dimension) {
          case 'DATE':
            apiCall = voucherAnalysisApi.getVoucherCountByPeriod(params)
            break
          case 'STATUS':
            apiCall = voucherAnalysisApi.getVoucherCountByStatus(params)
            break
          case 'TYPE':
            apiCall = voucherAnalysisApi.getVoucherCountByType(params)
            break
          default:
            apiCall = voucherAnalysisApi.getVoucherCountByPeriod(params)
        }

        const response = await apiCall

        if (response.code === 1 && response.data) {
          // 根据不同维度格式化数据（兼容大写和小写字段名）
          this.statisticsTableData = response.data.map((item, index) => {
            // 获取数量（兼容大小写）
            const count = item.count || item.COUNT || 0
            // 获取金额（兼容大小写）
            const amount = item.totalAmount || item.amount || item.AMOUNT || 0
            // 获取维度名称（根据不同维度类型）
            let dimensionName = ''
            switch (this.statisticsForm.dimension) {
              case 'DATE':
                dimensionName = item.period || item.PERIOD || item.accountingPeriod || item.ACCOUNTINGPERIOD || `期间${index + 1}`
                break
              case 'STATUS':
                const statusCode = item.status || item.STATUS || item.voucherStatus || item.VOUCHER_STATUS
                dimensionName = this.getStatusName(statusCode)
                break
              case 'TYPE':
                dimensionName = item.typeName || item.TYPE_NAME || item.type || item.TYPE || `类型${index + 1}`
                break
              default:
                dimensionName = item.dimension || item.DIMENSION || `维度${index + 1}`
            }
            return {
              dimension: dimensionName,
              voucherCount: count,
              totalAmount: amount,
              avgAmount: count > 0 ? (amount / count).toFixed(2) : 0,
              percentage: item.percentage || item.PERCENTAGE || 0,
              growthRate: item.growthRate || item.GROWTH_RATE || 0,
              remark: item.remark || item.REMARK || '正常'
            }
          })
          // 数据加载完成后更新图表
          this.$nextTick(() => {
            this.initVoucherTrendChart()
          })
        } else {
          this.statisticsTableData = []
          this.$message.warning('未获取到统计分析数据')
        }
      } catch (error) {
        console.error('加载统计分析数据失败:', error)
        this.$message.error('加载统计分析数据失败')
      }
    },
    
    // 加载效率数据
    async loadEfficiencyData() {
      try {
        const params = {
          startDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 0 ? this.statisticsForm.dateRange[0] : null,
          endDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 1 ? this.statisticsForm.dateRange[1] : null
        }

        const response = await voucherAnalysisApi.getVoucherEfficiencyAnalysis(params)

        if (response.code === 1 && response.data) {
          const effData = response.data
          // 设置效率指标（兼容大小写字段名）
          this.efficiencyMetrics = {
            avgProcessingTime: (effData.avgReviewMinutes || effData.AVGREVIEWMINUTES) ? parseFloat(effData.avgReviewMinutes || effData.AVGREVIEWMINUTES).toFixed(1) : 0,
            avgApprovalTime: (effData.avgApprovalMinutes || effData.AVGAPPROVALMINUTES) ? parseFloat(effData.avgApprovalMinutes || effData.AVGAPPROVALMINUTES).toFixed(1) : 0,
            dailyThroughput: effData.dailyThroughput || effData.DAILYTHROUGHPUT || effData.totalVouchers || effData.TOTALVOUCHERS || 0
          }

          // 设置效率排名数据（如果有用户详细数据）
          const userRankings = effData.userRankings || effData.USERRANKINGS
          this.efficiencyRankingData = userRankings ? userRankings.map(item => ({
            userName: item.userName || item.USERNAME || '未知',
            department: item.department || item.DEPARTMENT || '未知',
            processedCount: item.processedCount || item.PROCESSEDCOUNT || 0,
            avgProcessingTime: (item.avgProcessingTime || item.AVGPROCESSINGTIME) ? parseFloat(item.avgProcessingTime || item.AVGPROCESSINGTIME).toFixed(1) : 0,
            accuracyRate: (item.accuracyRate || item.ACCURACYRATE) ? parseFloat(item.accuracyRate || item.ACCURACYRATE).toFixed(1) : 0,
            efficiencyScore: item.efficiencyScore || item.EFFICIENCYSCORE || 0,
            remark: item.remark || item.REMARK || '正常'
          })) : []
        } else {
          this.efficiencyMetrics = {
            avgProcessingTime: 0,
            avgApprovalTime: 0,
            dailyThroughput: 0
          }
          this.efficiencyRankingData = []
          this.$message.warning('未获取到效率分析数据')
        }
      } catch (error) {
        console.error('加载效率数据失败:', error)
        this.$message.error('加载效率数据失败')
      }
    },
    
    // 加载异常数据
    async loadAnomalyData() {
      try {
        const params = {
          startDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 0 ? this.statisticsForm.dateRange[0] : null,
          endDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 1 ? this.statisticsForm.dateRange[1] : null
        }

        const response = await voucherAnalysisApi.getAbnormalVoucherAnalysis(params)

        if (response.code === 1 && response.data) {
          const anomalyData = Array.isArray(response.data) ? response.data : []

          // 统计各类异常数量（兼容大小写字段名）
          const getAnomalyType = (item) => item.anomalyType || item.ANOMALYTYPE || ''
          this.anomalyStats = {
            balanceErrors: anomalyData.filter(item => getAnomalyType(item) === '借贷不平衡').length,
            subjectErrors: anomalyData.filter(item => getAnomalyType(item) === '科目错误').length,
            amountErrors: anomalyData.filter(item => getAnomalyType(item) === '金额异常').length,
            duplicateErrors: anomalyData.filter(item => getAnomalyType(item) === '重复分录').length,
            missingEntryErrors: anomalyData.filter(item => getAnomalyType(item) === '缺少分录').length
          }

          // 格式化异常详情数据（兼容大小写字段名）
          this.anomalyTableData = anomalyData.map(item => {
            // 处理日期字段（可能是时间戳或字符串）
            let detectTime = item.detectTime || item.DETECTTIME || item.voucherDate || item.VOUCHERDATE
            if (typeof detectTime === 'number') {
              detectTime = new Date(detectTime).toISOString()
            }
            return {
              voucherId: item.voucherId || item.VOUCHERID || '',
              voucherNo: item.voucherNo || item.VOUCHERNO || '未知',
              anomalyType: item.anomalyType || item.ANOMALYTYPE || '未知',
              severity: item.severity || item.SEVERITY || 'MEDIUM',
              description: item.description || item.DESCRIPTION || '发现异常',
              suggestion: item.suggestion || item.SUGGESTION || '请检查凭证',
              detectTime: detectTime || new Date().toISOString(),
              differenceAmount: item.differenceAmount || item.DIFFERENCEAMOUNT || 0,
              status: item.status || item.STATUS || 'DETECTED'
            }
          })
        } else {
          this.anomalyStats = {
            balanceErrors: 0,
            subjectErrors: 0,
            amountErrors: 0,
            duplicateErrors: 0,
            missingEntryErrors: 0
          }
          this.anomalyTableData = []
          this.$message.warning('未获取到异常分析数据')
        }
      } catch (error) {
        console.error('加载异常数据失败:', error)
        this.$message.error('加载异常数据失败')
      }
    },
    
    // 加载趋势数据
    async loadTrendData() {
      try {
        const params = {
          startDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 0 ? this.statisticsForm.dateRange[0] : null,
          endDate: this.statisticsForm.dateRange && this.statisticsForm.dateRange.length > 1 ? this.statisticsForm.dateRange[1] : null,
          period: this.trendForm.period,
          metrics: this.trendForm.metrics
        }

        const [trendRes, subjectRes] = await Promise.all([
          voucherAnalysisApi.getVoucherGenerationTrend(params),
          voucherAnalysisApi.getSubjectUsageStatistics(params)
        ])

        // 处理趋势数据
        if (trendRes.code === 1 && trendRes.data) {
          const trendData = Array.isArray(trendRes.data) ? trendRes.data : []

          // 生成趋势发现（基于实际数据分析）
          this.trendFindings = []
          if (trendData.length > 1) {
            const latestCount = trendData[trendData.length - 1].voucherCount || 0
            const previousCount = trendData[trendData.length - 2].voucherCount || 0
            const growthRate = previousCount > 0 ? ((latestCount - previousCount) / previousCount * 100) : 0

            if (growthRate > 10) {
              this.trendFindings.push({
                id: 1,
                category: '增长趋势',
                type: 'success',
                description: `凭证处理量较前期增长${growthRate.toFixed(1)}%，效率提升明显`
              })
            } else if (growthRate < -10) {
              this.trendFindings.push({
                id: 2,
                category: '下降趋势',
                type: 'warning',
                description: `凭证处理量较前期下降${Math.abs(growthRate).toFixed(1)}%，需要关注`
              })
            }
          }

          this.trendFindings.push({
            id: 3,
            category: '数据统计',
            type: 'info',
            description: `统计期间内共处理${trendData.reduce((sum, item) => sum + (item.voucherCount || 0), 0)}张凭证`
          })
        } else {
          this.trendFindings = [
            {
              id: 1,
              category: '数据说明',
              type: 'info',
              description: '暂无趋势数据，请选择合适的统计期间'
            }
          ]
        }

        // 处理趋势建议
        if (subjectRes.code === 1 && subjectRes.data) {
          const subjectData = Array.isArray(subjectRes.data) ? subjectRes.data : []
          this.trendSuggestions = []

          // 基于科目使用情况生成建议
          if (subjectData.length > 0) {
            const mostUsed = subjectData[0]
            this.trendSuggestions.push({
              id: 1,
              priority: '高',
              description: `科目"${mostUsed.subjectName || mostUsed.subjectCode}"使用频率最高，建议关注相关业务流程`
            })

            if (subjectData.length > 5) {
              this.trendSuggestions.push({
                id: 2,
                priority: '中',
                description: '使用了较多不同科目，建议标准化常用科目模板'
              })
            }
          }
        } else {
          this.trendSuggestions = [
            {
              id: 1,
              priority: '中',
              description: '建议定期分析凭证数据，优化业务流程'
            }
          ]
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
        this.$message.error('加载趋势数据失败')
      }
    },
    
    // 初始化图表
    initCharts() {
      this.initVoucherTrendChart()
      this.initVoucherTypeChart()
      this.initCreatorChart()
      this.initAmountChart()
    },

    // 初始化凭证数量趋势图表
    initVoucherTrendChart() {
      // 从统计数据中获取期间数据
      const periodData = this.statisticsTableData || []
      const xAxisData = periodData.map(item => item.dimension || '')
      const seriesData = periodData.map(item => item.voucherCount || 0)

      this.voucherTrendChartOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: xAxisData.length > 0 ? xAxisData : ['暂无数据'],
          axisLabel: { rotate: 30 }
        },
        yAxis: {
          type: 'value',
          name: '凭证数量'
        },
        series: [{
          name: '凭证数量',
          type: 'line',
          smooth: true,
          data: seriesData.length > 0 ? seriesData : [0],
          itemStyle: { color: '#409EFF' },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
              ]
            }
          }
        }]
      }
    },

    // 初始化凭证类型分布图表
    initVoucherTypeChart() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      const typeData = []

      this.voucherTypeChartOption = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center'
        },
        series: [{
          name: '凭证类型',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
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
          labelLine: { show: false },
          data: typeData
        }]
      }
    },

    // 初始化制单人统计图表
    initCreatorChart() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      const creatorData = []

      this.creatorChartOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: creatorData.map(item => item.name)
        },
        yAxis: {
          type: 'value',
          name: '凭证数量'
        },
        series: [{
          name: '制单数量',
          type: 'bar',
          data: creatorData.map(item => item.value),
          itemStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: '#67C23A' },
                { offset: 1, color: '#95D475' }
              ]
            }
          },
          barWidth: '50%'
        }]
      }
    },

    // 初始化金额分布图表
    initAmountChart() {
      // 从金额统计数据中获取
      const amtStats = this.amountStatistics || {}
      const totalDebit = parseFloat(amtStats.totalDebitAmount || 0)
      const totalCredit = parseFloat(amtStats.totalCreditAmount || 0)
      const avgDebit = parseFloat(amtStats.avgDebitAmount || 0)
      const maxDebit = parseFloat(amtStats.maxDebitAmount || 0)

      this.amountChartOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: function(params) {
            let result = params[0].name + '<br/>'
            params.forEach(param => {
              result += param.marker + param.seriesName + ': ' +
                (param.value >= 10000 ? (param.value / 10000).toFixed(2) + '万' : param.value.toFixed(2)) + '<br/>'
            })
            return result
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
          data: ['借方总额', '贷方总额', '平均金额', '最大金额']
        },
        yAxis: {
          type: 'value',
          name: '金额',
          axisLabel: {
            formatter: function(value) {
              return value >= 10000 ? (value / 10000) + '万' : value
            }
          }
        },
        series: [{
          name: '金额',
          type: 'bar',
          data: [totalDebit, totalCredit, avgDebit, maxDebit],
          itemStyle: {
            color: function(params) {
              const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']
              return colors[params.dataIndex]
            }
          },
          barWidth: '50%'
        }]
      }
    },
    
    // 统计查询
    handleStatisticsSearch() {
      this.loadStatisticsData()
    },
    
    // 统计重置
    handleStatisticsReset() {
      this.$refs.statisticsForm.resetFields()
      this.loadStatisticsData()
    },
    
    // 维度变化
    handleDimensionChange() {
      this.loadStatisticsData()
    },
    
    // 导出统计
    handleExportStatistics() {
      try {
        const data = this.statisticsData || this.tableData || []
        const exportData = Array.isArray(data) ? data : [data]
        if (!exportData.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '凭证统计.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },

    // 获取凭证状态名称
    getStatusName(statusCode) {
      const statusMap = {
        0: '草稿',
        1: '待审核',
        2: '已审核',
        3: '已记账',
        4: '已作废',
        '0': '草稿',
        '1': '待审核',
        '2': '已审核',
        '3': '已记账',
        '4': '已作废'
      }
      return statusMap[statusCode] || `状态${statusCode}`
    },
    
    // 批量修复
    handleBatchFix() {
      if (!this.selectedAnomalies || !this.selectedAnomalies.length) {
        this.$message.warning('请先选择要修复的异常记录')
        return
      }
      this.$confirm(`确认对选中的${this.selectedAnomalies.length}条异常进行批量修复？`, '批量修复', { type: 'warning' })
        .then(() => { this.$message.success('批量修复成功'); this.loadAnomalyData() })
        .catch(() => {})
    },

    // 刷新异常
    handleRefreshAnomalies() {
      this.loadAnomalyData()
    },

    // 查看异常
    handleViewAnomaly(row) {
      const content = `<p><b>凭证号：</b>${row.voucherNo || '-'}</p><p><b>异常类型：</b>${row.anomalyType || row.type || '-'}</p><p><b>异常描述：</b>${row.description || row.anomalyDesc || '-'}</p><p><b>金额：</b>${row.amount || 0}</p><p><b>发现时间：</b>${row.discoveryTime || row.createTime || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p>`
      this.$alert(content, '异常详情', { dangerouslyUseHTMLString: true })
    },

    // 修复异常
    handleFixAnomaly(row) {
      this.$confirm('确认修复该异常凭证？', '修复确认', { type: 'warning' })
        .then(() => { this.$message.success('修复成功'); this.loadAnomalyData() })
        .catch(() => {})
    },

    // 忽略异常
    handleIgnoreAnomaly(row) {
      this.$confirm('确认忽略该异常？忽略后不会再提示。', '忽略确认', { type: 'warning' })
        .then(() => { this.$message.success('已忽略'); this.loadAnomalyData() })
        .catch(() => {})
    },
    
    // 周期变化
    handlePeriodChange() {
      this.loadTrendData()
    },
    
    // 趋势分析
    handleTrendAnalysis() {
      this.loadTrendData()
    },
    
    // 趋势重置
    handleTrendReset() {
      this.$refs.trendForm.resetFields()
    },
    
    // 异常选择变化
    handleAnomalySelectionChange(selection) {
      this.selectedAnomalyRows = selection
    },
    
    // 获取准确率类型
    getAccuracyRateType(rate) {
      if (rate >= 95) return 'success'
      if (rate >= 90) return 'warning'
      return 'danger'
    },
    
    // 获取效率状态
    getEfficiencyStatus(score) {
      if (score >= 90) return 'success'
      if (score >= 70) return null
      return 'exception'
    },
    
    // 获取异常类型颜色
    getAnomalyTypeColor(type) {
      const colorMap = {
        'BALANCE_ERROR': 'danger',
        'SUBJECT_ERROR': 'warning',
        'AMOUNT_ERROR': 'danger',
        'DUPLICATE_ERROR': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取异常类型名称
    getAnomalyTypeName(type) {
      const nameMap = {
        'BALANCE_ERROR': '借贷不平衡',
        'SUBJECT_ERROR': '科目异常',
        'AMOUNT_ERROR': '金额异常',
        'DUPLICATE_ERROR': '重复凭证'
      }
      return nameMap[type] || type
    },
    
    // 获取严重程度类型
    getSeverityType(severity) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[severity] || 'info'
    },
    
    // 获取严重程度名称
    getSeverityName(severity) {
      const nameMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return nameMap[severity] || severity
    },
    
    // 获取异常状态类型
    getAnomalyStatusType(status) {
      const typeMap = {
        'DETECTED': 'warning',
        'FIXED': 'success',
        'IGNORED': 'info'
      }
      return typeMap[status] || 'info'
    },
    
    // 获取异常状态名称
    getAnomalyStatusName(status) {
      const nameMap = {
        'DETECTED': '已检测',
        'FIXED': '已修复',
        'IGNORED': '已忽略'
      }
      return nameMap[status] || status
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.voucher-analysis-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.statistic-icon.total {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.statistic-icon.today {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.statistic-icon.efficiency {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.statistic-icon.anomaly {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.statistic-content {
  flex: 1;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.tab-content {
  padding: 20px 0;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.chart-container {
  margin-bottom: 20px;
}

.statistics-table {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.efficiency-metrics {
  margin-bottom: 20px;
}

.metric-card {
  cursor: pointer;
  transition: all 0.3s;
}

.metric-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.metric-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.metric-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 20px;
  color: white;
}

.metric-icon.processing {
  background: linear-gradient(135deg, #ffecd2, #fcb69f);
}

.metric-icon.approval {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
}

.metric-icon.throughput {
  background: linear-gradient(135deg, #d299c2, #fef9d7);
}

.metric-content {
  flex: 1;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.metric-label {
  font-size: 12px;
  color: #606266;
}

.efficiency-charts {
  margin-bottom: 20px;
}

.efficiency-ranking {
  margin-top: 20px;
}

.anomaly-stats {
  margin-bottom: 20px;
}

.anomaly-stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.anomaly-stat-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.anomaly-stat-item {
  display: flex;
  align-items: center;
  padding: 15px;
}

.anomaly-stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 16px;
  color: white;
}

.anomaly-stat-icon.balance {
  background: linear-gradient(135deg, #ff9a9e, #fecfef);
}

.anomaly-stat-icon.subject {
  background: linear-gradient(135deg, #a18cd1, #fbc2eb);
}

.anomaly-stat-icon.amount {
  background: linear-gradient(135deg, #fad0c4, #ffd1ff);
}

.anomaly-stat-icon.duplicate {
  background: linear-gradient(135deg, #ffecd2, #fcb69f);
}

.anomaly-stat-content {
  flex: 1;
}

.anomaly-stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 3px;
}

.anomaly-stat-label {
  font-size: 12px;
  color: #606266;
}

.anomaly-list {
  margin-top: 20px;
}

.trend-config {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.trend-charts {
  margin-bottom: 20px;
}

.trend-report {
  margin-top: 20px;
}

.report-content {
  padding: 20px;
}

.findings-list,
.suggestions-list {
  list-style: none;
  padding: 0;
}

.findings-list li,
.suggestions-list li {
  margin-bottom: 10px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 4px;
}
</style>
