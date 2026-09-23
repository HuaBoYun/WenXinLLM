<template>
  <div class="ledger-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          总账分析
        </h1>
        <p class="page-description">分析总账数据趋势和异常情况</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportAnalysis">
          导出分析
        </el-button>
        <el-button type="warning" icon="el-icon-document" @click="generateReport">
          生成报告
        </el-button>
      </div>
    </div>

    <!-- 分析统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon subjects">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalSubjects }}</div>
              <div class="stat-label">分析科目数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAmount) }}</div>
              <div class="stat-label">分析金额合计</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon abnormal">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.abnormalCount }}</div>
              <div class="stat-label">异常数据数量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon trend">
              <i class="el-icon-trend-charts"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value" :class="{ 'up': stats.trendDirection > 0, 'down': stats.trendDirection < 0 }">
                {{ stats.trendDirection > 0 ? '↗' : stats.trendDirection < 0 ? '↘' : '→' }}
              </div>
              <div class="stat-label">总体趋势</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析功能标签页 -->
    <div class="analysis-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 科目余额分析 -->
        <el-tab-pane label="科目余额分析" name="balance">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="balanceForm" ref="balanceForm" :inline="true" size="small">
                <el-form-item label="科目类型" prop="subjectType">
                  <el-select v-model="balanceForm.subjectType" placeholder="请选择科目类型" clearable>
                    <el-option label="资产" :value="1" />
                    <el-option label="负债" :value="2" />
                    <el-option label="权益" :value="3" />
                    <el-option label="收入" :value="4" />
                    <el-option label="费用" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item label="期间范围" prop="periodRange">
                  <el-date-picker
                    v-model="balanceForm.periodRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始期间"
                    end-placeholder="结束期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="分析维度" prop="analysisDimension">
                  <el-select v-model="balanceForm.analysisDimension" placeholder="请选择分析维度">
                    <el-option label="按科目分析" value="subject" />
                    <el-option label="按类型分析" value="type" />
                    <el-option label="按级次分析" value="level" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleBalanceSearch">分析</el-button>
                  <el-button @click="handleBalanceReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 余额分析图表 -->
            <div class="chart-container">
              <div class="chart-title">科目余额趋势分析</div>
              <div id="balanceChart" style="width: 100%; height: 400px;"></div>
            </div>

            <!-- 余额分析表格 -->
            <div class="table-container">
              <el-table
                :data="balanceTableData"
                v-loading="balanceLoading"
                border
                stripe
                height="300"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="200" />
                <el-table-column prop="currentBalance" label="当前余额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.currentBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="previousBalance" label="上期余额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.previousBalance) }}
                  </template>
                </el-table-column>
                <el-table-column prop="changeAmount" label="变动金额" width="150" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'increase': scope.row.changeAmount > 0, 'decrease': scope.row.changeAmount < 0 }">
                      {{ formatAmount(scope.row.changeAmount) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="changeRate" label="变动率" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'increase': scope.row.changeRate > 0, 'decrease': scope.row.changeRate < 0 }">
                      {{ (scope.row.changeRate * 100).toFixed(2) }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="analysisResult" label="分析结果" width="120">
                  <template slot-scope="scope">
                    <el-tag :type="getAnalysisResultType(scope.row.analysisResult)">
                      {{ scope.row.analysisResult }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewBalanceDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleBalanceSizeChange"
                  @current-change="handleBalanceCurrentChange"
                  :current-page="balancePagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="balancePagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="balancePagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 发生额分析 -->
        <el-tab-pane label="发生额分析" name="occurrence">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="occurrenceForm" ref="occurrenceForm" :inline="true" size="small">
                <el-form-item label="科目编码" prop="subjectCode">
                  <el-input v-model="occurrenceForm.subjectCode" placeholder="请输入科目编码" clearable />
                </el-form-item>
                <el-form-item label="期间范围" prop="periodRange">
                  <el-date-picker
                    v-model="occurrenceForm.periodRange"
                    type="monthrange"
                    range-separator="至"
                    start-placeholder="开始期间"
                    end-placeholder="结束期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="发生额类型" prop="occurrenceType">
                  <el-select v-model="occurrenceForm.occurrenceType" placeholder="请选择发生额类型">
                    <el-option label="借方发生额" value="debit" />
                    <el-option label="贷方发生额" value="credit" />
                    <el-option label="净发生额" value="net" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleOccurrenceSearch">分析</el-button>
                  <el-button @click="handleOccurrenceReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 发生额分析图表 -->
            <div class="chart-container">
              <div class="chart-title">科目发生额趋势分析</div>
              <div id="occurrenceChart" style="width: 100%; height: 400px;"></div>
            </div>

            <!-- 发生额分析表格 -->
            <div class="table-container">
              <el-table
                :data="occurrenceTableData"
                v-loading="occurrenceLoading"
                border
                stripe
                height="300"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="200" />
                <el-table-column prop="period" label="期间" width="100" />
                <el-table-column prop="debitAmount" label="借方发生额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.debitAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="creditAmount" label="贷方发生额" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.creditAmount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="netAmount" label="净发生额" width="150" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'increase': scope.row.netAmount > 0, 'decrease': scope.row.netAmount < 0 }">
                      {{ formatAmount(scope.row.netAmount) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="frequency" label="发生频次" width="100" align="right" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewOccurrenceDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleOccurrenceSizeChange"
                  @current-change="handleOccurrenceCurrentChange"
                  :current-page="occurrencePagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="occurrencePagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="occurrencePagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 异常数据分析 -->
        <el-tab-pane label="异常数据分析" name="abnormal">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="abnormalForm" ref="abnormalForm" :inline="true" size="small">
                <el-form-item label="异常类型" prop="abnormalType">
                  <el-select v-model="abnormalForm.abnormalType" placeholder="请选择异常类型" clearable>
                    <el-option label="余额异常" value="balance_abnormal" />
                    <el-option label="发生额异常" value="occurrence_abnormal" />
                    <el-option label="科目不平衡" value="subject_unbalanced" />
                    <el-option label="数据缺失" value="data_missing" />
                  </el-select>
                </el-form-item>
                <el-form-item label="严重程度" prop="severity">
                  <el-select v-model="abnormalForm.severity" placeholder="请选择严重程度" clearable>
                    <el-option label="低" value="low" />
                    <el-option label="中" value="medium" />
                    <el-option label="高" value="high" />
                    <el-option label="严重" value="critical" />
                  </el-select>
                </el-form-item>
                <el-form-item label="检测期间" prop="detectionPeriod">
                  <el-date-picker
                    v-model="abnormalForm.detectionPeriod"
                    type="month"
                    placeholder="选择检测期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleAbnormalSearch">检测</el-button>
                  <el-button @click="handleAbnormalReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 异常数据表格 -->
            <div class="table-container">
              <el-table
                :data="abnormalTableData"
                v-loading="abnormalLoading"
                border
                stripe
                height="400"
              >
                <el-table-column prop="subjectCode" label="科目编码" width="120" />
                <el-table-column prop="subjectName" label="科目名称" width="200" />
                <el-table-column prop="abnormalType" label="异常类型" width="120">
                  <template slot-scope="scope">
                    <el-tag :type="getAbnormalTypeColor(scope.row.abnormalType)">
                      {{ getAbnormalTypeName(scope.row.abnormalType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="severity" label="严重程度" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getSeverityColor(scope.row.severity)">
                      {{ getSeverityName(scope.row.severity) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="abnormalValue" label="异常值" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.abnormalValue) }}
                  </template>
                </el-table-column>
                <el-table-column prop="expectedValue" label="预期值" width="150" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.expectedValue) }}
                  </template>
                </el-table-column>
                <el-table-column prop="deviation" label="偏差率" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'high-deviation': Math.abs(scope.row.deviation) > 0.2 }">
                      {{ (scope.row.deviation * 100).toFixed(2) }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="detectionTime" label="检测时间" width="180" />
                <el-table-column prop="status" label="处理状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.status === 'resolved' ? 'success' : 'warning'">
                      {{ scope.row.status === 'resolved' ? '已处理' : '待处理' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewAbnormalDetail(scope.row)">
                      查看详情
                    </el-button>
                    <el-button 
                      type="text" 
                      size="small" 
                      @click="markAsResolved(scope.row)"
                      v-if="scope.row.status !== 'resolved'"
                    >
                      标记已处理
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleAbnormalSizeChange"
                  @current-change="handleAbnormalCurrentChange"
                  :current-page="abnormalPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="abnormalPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="abnormalPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 财务指标分析 -->
        <el-tab-pane label="财务指标分析" name="ratio">
          <div class="tab-content">
            <!-- 查询条件 -->
            <div class="search-form">
              <el-form :model="ratioForm" ref="ratioForm" :inline="true" size="small">
                <el-form-item label="指标类型" prop="ratioType">
                  <el-select v-model="ratioForm.ratioType" placeholder="请选择指标类型" clearable>
                    <el-option label="偿债能力指标" value="solvency" />
                    <el-option label="营运能力指标" value="operational" />
                    <el-option label="盈利能力指标" value="profitability" />
                    <el-option label="发展能力指标" value="growth" />
                  </el-select>
                </el-form-item>
                <el-form-item label="分析期间" prop="analysisPeriod">
                  <el-date-picker
                    v-model="ratioForm.analysisPeriod"
                    type="month"
                    placeholder="选择分析期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                  />
                </el-form-item>
                <el-form-item label="对比方式" prop="comparisonType">
                  <el-select v-model="ratioForm.comparisonType" placeholder="请选择对比方式">
                    <el-option label="同比分析" value="year_over_year" />
                    <el-option label="环比分析" value="month_over_month" />
                    <el-option label="行业对比" value="industry_comparison" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleRatioSearch">分析</el-button>
                  <el-button @click="handleRatioReset">重置</el-button>
                </el-form-item>
              </el-form>
            </div>

            <!-- 财务指标图表 -->
            <div class="chart-container">
              <div class="chart-title">财务指标趋势分析</div>
              <div id="ratioChart" style="width: 100%; height: 400px;"></div>
            </div>

            <!-- 财务指标表格 -->
            <div class="table-container">
              <el-table
                :data="ratioTableData"
                v-loading="ratioLoading"
                border
                stripe
                height="300"
              >
                <el-table-column prop="ratioName" label="指标名称" width="200" />
                <el-table-column prop="currentValue" label="当前值" width="120" align="right">
                  <template slot-scope="scope">
                    {{ scope.row.currentValue.toFixed(4) }}
                  </template>
                </el-table-column>
                <el-table-column prop="previousValue" label="上期值" width="120" align="right">
                  <template slot-scope="scope">
                    {{ scope.row.previousValue.toFixed(4) }}
                  </template>
                </el-table-column>
                <el-table-column prop="changeRate" label="变动率" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="{ 'increase': scope.row.changeRate > 0, 'decrease': scope.row.changeRate < 0 }">
                      {{ (scope.row.changeRate * 100).toFixed(2) }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="industryAverage" label="行业平均" width="120" align="right">
                  <template slot-scope="scope">
                    {{ scope.row.industryAverage ? scope.row.industryAverage.toFixed(4) : '-' }}
                  </template>
                </el-table-column>
                <el-table-column prop="evaluation" label="评价" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getEvaluationType(scope.row.evaluation)">
                      {{ scope.row.evaluation }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="suggestion" label="建议" />
                <el-table-column label="操作" width="120" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="text" size="small" @click="viewRatioDetail(scope.row)">
                      查看详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div class="pagination-container">
                <el-pagination
                  @size-change="handleRatioSizeChange"
                  @current-change="handleRatioCurrentChange"
                  :current-page="ratioPagination.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="ratioPagination.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="ratioPagination.total"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import * as generalLedgerApi from '@/api/financialSharing/generalLedger'
import * as echarts from 'echarts'

export default {
  name: 'LedgerAnalysis',
  data() {
    return {
      activeTab: 'balance',
      stats: {
        totalSubjects: 0,
        totalAmount: 0,
        abnormalCount: 0,
        trendDirection: 0
      },
      // 科目余额分析
      balanceForm: {
        subjectType: '',
        periodRange: [],
        analysisDimension: 'subject'
      },
      balanceTableData: [],
      balanceLoading: false,
      balancePagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 发生额分析
      occurrenceForm: {
        subjectCode: '',
        periodRange: [],
        occurrenceType: 'net'
      },
      occurrenceTableData: [],
      occurrenceLoading: false,
      occurrencePagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 异常数据分析
      abnormalForm: {
        abnormalType: '',
        severity: '',
        detectionPeriod: ''
      },
      abnormalTableData: [],
      abnormalLoading: false,
      abnormalPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      // 财务指标分析
      ratioForm: {
        ratioType: '',
        analysisPeriod: '',
        comparisonType: 'year_over_year'
      },
      ratioTableData: [],
      ratioLoading: false,
      ratioPagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      }
    }
  },
  mounted() {
    this.loadStats()
    this.loadBalanceData()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接 API，先以空状态展示
        this.stats = {
          totalSubjects: 0,
          totalAmount: 0,
          abnormalCount: 0,
          trendDirection: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    async refreshData() {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在刷新分析数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        await this.loadStats()
        if (this.activeTab === 'balance') {
          await this.loadBalanceData()
        } else if (this.activeTab === 'occurrence') {
          await this.loadOccurrenceData()
        } else if (this.activeTab === 'abnormal') {
          await this.loadAbnormalData()
        } else if (this.activeTab === 'ratio') {
          await this.loadRatioData()
        }

        loading.close()
        this.$message.success('数据刷新成功')
      } catch (error) {
        this.$message.error('刷新失败：' + error.message)
      }
    },
    async exportAnalysis() {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在导出分析数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        const response = await exportAnalysisResult({
          analysisType: this.activeTab,
          period: this.currentPeriod,
          params: this.getExportParams()
        })

        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `总账分析_${this.getTabName()}_${new Date().getTime()}.xlsx`
        link.click()

        loading.close()
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    generateReport() {
      this.$router.push({
        path: '/financial/ledgerAnalysis/report',
        query: {
          analysisType: this.activeTab,
          period: this.currentPeriod
        }
      })
    },
    handleTabClick(tab) {
      if (tab.name === 'balance') {
        this.loadBalanceData()
      } else if (tab.name === 'occurrence') {
        this.loadOccurrenceData()
      } else if (tab.name === 'abnormal') {
        this.loadAbnormalData()
      } else if (tab.name === 'ratio') {
        this.loadRatioData()
      }
    },

    // 科目余额分析方法
    async loadBalanceData() {
      this.balanceLoading = true
      try {
        const params = {
          pageNumber: this.balancePagination.currentPage,
          pageSize: this.balancePagination.pageSize,
          ...this.balanceForm
        }
        const response = await generalLedgerApi.getSubjectBalanceAnalysis(params)
        if (response.code === 1) {
          const data = response.data || {}
          // 兼容两种数据结构：tlist/totalRecord 或 monthlyData/summary
          if (data.tlist) {
            // 标准分页结构
            this.balanceTableData = data.tlist
            this.balancePagination.total = data.totalRecord || 0
            this.renderBalanceChart(data.chartData || [])
          } else if (data.monthlyData) {
            // 月度数据结构 - 转换为表格数据
            this.balanceTableData = data.monthlyData.map(item => ({
              subjectCode: item.subjectCode || '',
              subjectName: item.subjectName || '',
              currentBalance: item.endingBalance,
              previousBalance: item.beginningBalance,
              changeAmount: item.endingBalance - item.beginningBalance,
              changeRate: item.changeRate ? item.changeRate / 100 : 0,
              analysisResult: item.trend === '上升' ? '正常' : (item.trend === '下降' ? '异常' : '正常'),
              period: item.period,
              trend: item.trend
            }))
            this.balancePagination.total = data.monthlyData.length
            // 使用月度数据渲染图表
            this.renderBalanceChart(data.monthlyData, data.summary)
          } else {
            this.balanceTableData = []
            this.balancePagination.total = 0
          }
        } else {
          this.$message.error(response.msg || '加载余额分析数据失败')
        }
      } catch (error) {
        this.$message.error('加载余额分析数据失败：' + error.message)
      } finally {
        this.balanceLoading = false
      }
    },

    handleBalanceSearch() {
      this.balancePagination.currentPage = 1
      this.loadBalanceData()
    },

    handleBalanceReset() {
      this.$refs.balanceForm.resetFields()
      this.balancePagination.currentPage = 1
      this.loadBalanceData()
    },

    handleBalanceSizeChange(size) {
      this.balancePagination.pageSize = size
      this.loadBalanceData()
    },

    handleBalanceCurrentChange(page) {
      this.balancePagination.currentPage = page
      this.loadBalanceData()
    },

    viewBalanceDetail(row) {
      this.$message.info(`查看科目 ${row.subjectName} 的余额分析详情`)
    },

    getAnalysisResultType(result) {
      const typeMap = {
        '正常': 'success',
        '异常': 'warning',
        '严重异常': 'danger'
      }
      return typeMap[result] || 'info'
    },

    renderBalanceChart(chartData, summary) {
      this.$nextTick(() => {
        const chartDom = document.getElementById('balanceChart')
        if (!chartDom) {
          console.warn('图表容器 balanceChart 未找到')
          return
        }
        // 销毁已有实例
        const existingChart = echarts.getInstanceByDom(chartDom)
        if (existingChart) {
          existingChart.dispose()
        }
        const chart = echarts.init(chartDom)

        // 处理数据
        const periods = chartData.map(item => item.period)
        const beginningBalances = chartData.map(item => item.beginningBalance || item.beginBalance || 0)
        const endingBalances = chartData.map(item => item.endingBalance || item.endBalance || 0)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'cross' }
          },
          legend: {
            data: ['期初余额', '期末余额'],
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
            data: periods,
            axisLabel: { rotate: 30 }
          },
          yAxis: {
            type: 'value',
            name: '金额',
            axisLabel: {
              formatter: (value) => {
                if (value >= 10000) {
                  return (value / 10000).toFixed(1) + '万'
                }
                return value
              }
            }
          },
          series: [
            {
              name: '期初余额',
              type: 'bar',
              data: beginningBalances,
              itemStyle: { color: '#409EFF' }
            },
            {
              name: '期末余额',
              type: 'line',
              data: endingBalances,
              smooth: true,
              itemStyle: { color: '#67C23A' },
              areaStyle: { opacity: 0.1 }
            }
          ]
        }
        chart.setOption(option)

        // 响应式调整
        window.addEventListener('resize', () => {
          chart.resize()
        })
      })
    },

    // 发生额分析方法
    async loadOccurrenceData() {
      this.occurrenceLoading = true
      try {
        const params = {
          pageNumber: this.occurrencePagination.currentPage,
          pageSize: this.occurrencePagination.pageSize,
          ...this.occurrenceForm
        }
        const response = await generalLedgerApi.getSubjectOccurrenceAnalysis(params)
        if (response.code === 1) {
          const data = response.data || {}
          // 兼容多种数据结构
          if (data.tlist) {
            this.occurrenceTableData = data.tlist
            this.occurrencePagination.total = data.totalRecord || 0
            this.renderOccurrenceChart(data.tlist)
          } else if (data.yearlyData) {
            // 实际接口返回的 yearlyData 结构
            const list = data.yearlyData
            this.occurrenceTableData = list.map(item => ({
              subjectCode: data.subjectCode || '',
              subjectName: item.subjectName || '',
              period: item.quarter || item.period || '',
              debitAmount: item.debitTotal || 0,
              creditAmount: item.creditTotal || 0,
              netAmount: item.netAmount || 0,
              frequency: item.transactionCount || 0
            }))
            this.occurrencePagination.total = list.length
            this.renderOccurrenceChart(list, data.trends)
          } else {
            this.occurrenceTableData = []
            this.occurrencePagination.total = 0
          }
        } else {
          this.$message.error(response.msg || '加载发生额分析数据失败')
        }
      } catch (error) {
        this.$message.error('加载发生额分析数据失败：' + error.message)
      } finally {
        this.occurrenceLoading = false
      }
    },

    handleOccurrenceSearch() {
      this.occurrencePagination.currentPage = 1
      this.loadOccurrenceData()
    },

    handleOccurrenceReset() {
      this.$refs.occurrenceForm.resetFields()
      this.occurrencePagination.currentPage = 1
      this.loadOccurrenceData()
    },

    handleOccurrenceSizeChange(size) {
      this.occurrencePagination.pageSize = size
      this.loadOccurrenceData()
    },

    handleOccurrenceCurrentChange(page) {
      this.occurrencePagination.currentPage = page
      this.loadOccurrenceData()
    },

    viewOccurrenceDetail(row) {
      this.$message.info(`查看科目 ${row.subjectName} 在 ${row.period} 期间的发生额详情`)
    },

    renderOccurrenceChart(chartData, trends) {
      this.$nextTick(() => {
        const chartDom = document.getElementById('occurrenceChart')
        if (!chartDom) {
          console.warn('图表容器 occurrenceChart 未找到')
          return
        }
        const existingChart = echarts.getInstanceByDom(chartDom)
        if (existingChart) {
          existingChart.dispose()
        }
        const chart = echarts.init(chartDom)

        // 适配 yearlyData 结构
        const periods = chartData.map(item => item.quarter || item.period || '')
        const debitAmounts = chartData.map(item => item.debitTotal || item.debitAmount || 0)
        const creditAmounts = chartData.map(item => item.creditTotal || item.creditAmount || 0)
        const netAmounts = chartData.map(item => item.netAmount || 0)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter: (params) => {
              let result = params[0].axisValue + '<br/>'
              params.forEach(param => {
                const value = Math.abs(param.value)
                result += `${param.marker}${param.seriesName}: ${(value / 10000).toFixed(2)}万<br/>`
              })
              return result
            }
          },
          legend: {
            data: ['借方发生额', '贷方发生额', '净发生额'],
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
            data: periods
          },
          yAxis: {
            type: 'value',
            name: '金额',
            axisLabel: {
              formatter: (value) => {
                if (Math.abs(value) >= 10000) {
                  return (value / 10000).toFixed(0) + '万'
                }
                return value
              }
            }
          },
          series: [
            {
              name: '借方发生额',
              type: 'bar',
              data: debitAmounts,
              itemStyle: { color: '#409EFF' }
            },
            {
              name: '贷方发生额',
              type: 'bar',
              data: creditAmounts,
              itemStyle: { color: '#F56C6C' }
            },
            {
              name: '净发生额',
              type: 'line',
              data: netAmounts,
              smooth: true,
              itemStyle: { color: '#67C23A' },
              lineStyle: { width: 3 }
            }
          ]
        }
        chart.setOption(option)

        window.addEventListener('resize', () => {
          chart.resize()
        })
      })
    },

    // 异常数据分析方法
    async loadAbnormalData() {
      this.abnormalLoading = true
      try {
        const params = {
          pageNumber: this.abnormalPagination.currentPage,
          pageSize: this.abnormalPagination.pageSize,
          ...this.abnormalForm
        }
        const response = await generalLedgerApi.getAbnormalDataAnalysis(params)
        if (response.code === 1) {
          const data = response.data || {}
          // 兼容多种数据结构
          if (data.tlist) {
            this.abnormalTableData = data.tlist
            this.abnormalPagination.total = data.totalRecord || 0
          } else if (data.anomalies) {
            // 实际接口返回的 anomalies 结构
            const list = data.anomalies
            this.abnormalTableData = list.map(item => ({
              subjectCode: item.subjectCode || '',
              subjectName: item.subjectName || '',
              abnormalType: this.mapAbnormalType(item.anomalyType),
              severity: this.mapSeverity(item.severity),
              abnormalValue: item.amount || item.frequency || 0,
              expectedValue: item.threshold || item.avgFrequency || item.avgAmount || 0,
              deviation: this.calculateDeviation(item),
              detectionTime: item.detectedTime ? new Date(item.detectedTime).toLocaleString() : '',
              status: item.status || 'pending',
              description: item.description || ''
            }))
            this.abnormalPagination.total = list.length
          } else {
            this.abnormalTableData = []
            this.abnormalPagination.total = 0
          }
        } else {
          this.$message.error(response.msg || '加载异常数据分析失败')
        }
      } catch (error) {
        this.$message.error('加载异常数据分析失败：' + error.message)
      } finally {
        this.abnormalLoading = false
      }
    },

    // 映射异常类型
    mapAbnormalType(type) {
      const typeMap = {
        '余额异常': 'balance_abnormal',
        '交易频率异常': 'occurrence_abnormal',
        '金额异常': 'balance_abnormal',
        '科目不平衡': 'subject_unbalanced',
        '数据缺失': 'data_missing'
      }
      return typeMap[type] || 'balance_abnormal'
    },

    // 映射严重程度
    mapSeverity(severity) {
      const severityMap = {
        '高': 'high',
        '中': 'medium',
        '低': 'low',
        '严重': 'critical'
      }
      return severityMap[severity] || 'medium'
    },

    // 计算偏差率
    calculateDeviation(item) {
      if (item.amount && item.threshold) {
        return (item.amount - item.threshold) / item.threshold
      }
      if (item.frequency && item.avgFrequency) {
        return (item.frequency - item.avgFrequency) / item.avgFrequency
      }
      if (item.amount && item.avgAmount) {
        return (item.amount - item.avgAmount) / item.avgAmount
      }
      return 0
    },

    handleAbnormalSearch() {
      this.abnormalPagination.currentPage = 1
      this.loadAbnormalData()
    },

    handleAbnormalReset() {
      this.$refs.abnormalForm.resetFields()
      this.abnormalPagination.currentPage = 1
      this.loadAbnormalData()
    },

    handleAbnormalSizeChange(size) {
      this.abnormalPagination.pageSize = size
      this.loadAbnormalData()
    },

    handleAbnormalCurrentChange(page) {
      this.abnormalPagination.currentPage = page
      this.loadAbnormalData()
    },

    viewAbnormalDetail(row) {
      this.$message.info(`查看科目 ${row.subjectName} 的异常数据详情`)
    },

    async markAsResolved(row) {
      try {
        // 这里调用标记已处理的API
        row.status = 'resolved'
        this.$message.success('已标记为已处理')
      } catch (error) {
        this.$message.error('标记失败：' + error.message)
      }
    },

    getAbnormalTypeColor(type) {
      const colorMap = {
        'balance_abnormal': 'warning',
        'occurrence_abnormal': 'danger',
        'subject_unbalanced': 'danger',
        'data_missing': 'info'
      }
      return colorMap[type] || 'info'
    },

    getAbnormalTypeName(type) {
      const nameMap = {
        'balance_abnormal': '余额异常',
        'occurrence_abnormal': '发生额异常',
        'subject_unbalanced': '科目不平衡',
        'data_missing': '数据缺失'
      }
      return nameMap[type] || type
    },

    getSeverityColor(severity) {
      const colorMap = {
        'low': 'success',
        'medium': 'warning',
        'high': 'danger',
        'critical': 'danger'
      }
      return colorMap[severity] || 'info'
    },

    getSeverityName(severity) {
      const nameMap = {
        'low': '低',
        'medium': '中',
        'high': '高',
        'critical': '严重'
      }
      return nameMap[severity] || severity
    },

    // 财务指标分析方法
    async loadRatioData() {
      this.ratioLoading = true
      try {
        const params = {
          pageNumber: this.ratioPagination.currentPage,
          pageSize: this.ratioPagination.pageSize,
          ...this.ratioForm
        }
        const response = await generalLedgerApi.getFinancialRatioAnalysis(params)
        if (response.code === 1) {
          const data = response.data || {}
          // 兼容多种数据结构
          if (data.tlist) {
            this.ratioTableData = data.tlist
            this.ratioPagination.total = data.totalRecord || 0
            this.renderRatioChart(data.tlist)
          } else if (data.ratios) {
            // 实际接口返回的 ratios 结构
            const list = data.ratios
            this.ratioTableData = list.map(item => ({
              ratioName: item.ratioName || '',
              currentValue: item.currentValue || 0,
              previousValue: item.previousValue || 0,
              changeRate: item.currentValue && item.previousValue ? (item.currentValue - item.previousValue) / item.previousValue : 0,
              industryAverage: item.industryAvg || null,
              evaluation: item.evaluation || '一般',
              suggestion: item.description || '',
              trend: item.trend || ''
            }))
            this.ratioPagination.total = list.length
            this.renderRatioChart(list)
          } else {
            this.ratioTableData = []
            this.ratioPagination.total = 0
          }
        } else {
          this.$message.error(response.msg || '加载财务指标分析数据失败')
        }
      } catch (error) {
        this.$message.error('加载财务指标分析数据失败：' + error.message)
      } finally {
        this.ratioLoading = false
      }
    },

    handleRatioSearch() {
      this.ratioPagination.currentPage = 1
      this.loadRatioData()
    },

    handleRatioReset() {
      this.$refs.ratioForm.resetFields()
      this.ratioPagination.currentPage = 1
      this.loadRatioData()
    },

    handleRatioSizeChange(size) {
      this.ratioPagination.pageSize = size
      this.loadRatioData()
    },

    handleRatioCurrentChange(page) {
      this.ratioPagination.currentPage = page
      this.loadRatioData()
    },

    viewRatioDetail(row) {
      this.$message.info(`查看财务指标 ${row.ratioName} 的详细分析`)
    },

    getEvaluationType(evaluation) {
      const typeMap = {
        '优秀': 'success',
        '良好': 'success',
        '一般': 'warning',
        '较差': 'danger',
        '差': 'danger'
      }
      return typeMap[evaluation] || 'info'
    },

    renderRatioChart(chartData) {
      this.$nextTick(() => {
        const chartDom = document.getElementById('ratioChart')
        if (!chartDom) {
          console.warn('图表容器 ratioChart 未找到')
          return
        }
        const existingChart = echarts.getInstanceByDom(chartDom)
        if (existingChart) {
          existingChart.dispose()
        }
        const chart = echarts.init(chartDom)

        // 适配 ratios 结构
        const ratioNames = chartData.map(item => item.ratioName || '')
        const currentValues = chartData.map(item => item.currentValue || 0)
        const previousValues = chartData.map(item => item.previousValue || 0)
        const industryAverages = chartData.map(item => item.industryAvg || item.industryAverage || null)

        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter: (params) => {
              let result = params[0].axisValue + '<br/>'
              params.forEach(param => {
                if (param.value !== null && param.value !== undefined) {
                  result += `${param.marker}${param.seriesName}: ${param.value.toFixed(2)}<br/>`
                }
              })
              return result
            }
          },
          legend: {
            data: ['当前值', '上期值', '行业平均'],
            top: 10
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ratioNames,
            axisLabel: {
              rotate: 15,
              interval: 0
            }
          },
          yAxis: {
            type: 'value',
            name: '指标值',
            axisLabel: {
              formatter: (value) => value.toFixed(2)
            }
          },
          series: [
            {
              name: '当前值',
              type: 'bar',
              data: currentValues,
              itemStyle: { color: '#409EFF' },
              barWidth: '20%'
            },
            {
              name: '上期值',
              type: 'bar',
              data: previousValues,
              itemStyle: { color: '#909399' },
              barWidth: '20%'
            },
            {
              name: '行业平均',
              type: 'line',
              data: industryAverages,
              smooth: true,
              itemStyle: { color: '#E6A23C' },
              lineStyle: { type: 'dashed', width: 2 },
              symbol: 'circle',
              symbolSize: 8
            }
          ]
        }
        chart.setOption(option)

        window.addEventListener('resize', () => {
          chart.resize()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.ledger-analysis-container {
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

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
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

    .stat-icon {
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

      &.subjects {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.abnormal {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.trend {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;

        &.up {
          color: #67c23a;
        }

        &.down {
          color: #f56c6c;
        }
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.analysis-tabs {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .tab-content {
    .search-form {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
    }

    .chart-container {
      margin-bottom: 20px;

      .chart-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 16px;
        text-align: center;
      }
    }

    .table-container {
      .increase {
        color: #67c23a;
      }

      .decrease {
        color: #f56c6c;
      }

      .high-deviation {
        color: #f56c6c;
        font-weight: 600;
      }

      .pagination-container {
        margin-top: 20px;
        text-align: right;
      }
    }
  }
}
</style>
