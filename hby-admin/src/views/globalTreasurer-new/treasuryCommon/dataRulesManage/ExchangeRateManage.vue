<template>
  <div class="exchange-rate-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            汇率管理
          </h2>
          <p class="page-description">管理外汇汇率信息，包括实时汇率、历史数据、汇率预警和趋势分析</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleSyncRates">
            同步汇率
          </el-button>
          <el-button type="success" icon="el-icon-data-line" @click="handleTrendAnalysis">
            趋势分析
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 汇率统计卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">支持币种</div>
                <div class="card-value">{{ totalCurrencies }}</div>
                <div class="card-change">种货币</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usd-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">美元汇率</div>
                <div class="card-value">{{ usdRate }}</div>
                <div class="card-change positive">CNY/USD</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon eur-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-info">
                <div class="card-title">欧元汇率</div>
                <div class="card-value">{{ eurRate }}</div>
                <div class="card-change">CNY/EUR</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后更新</div>
                <div class="card-value">{{ lastUpdateTime }}</div>
                <div class="card-change">分钟前</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="listQuery" class="search-form">
        <el-row :gutter="16">
          <el-col :span="5">
            <el-form-item label="源币种">
              <el-select v-model="listQuery.fromCurrency" placeholder="请选择源币种" clearable style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="目标币种">
              <el-select v-model="listQuery.toCurrency" placeholder="请选择目标币种" clearable style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="汇率来源">
              <el-select v-model="listQuery.rateSource" placeholder="请选择汇率来源" clearable style="width: 100%;">
                <el-option label="央行" value="PBOC" />
                <el-option label="外汇管理局" value="SAFE" />
                <el-option label="银行" value="BANK" />
                <el-option label="第三方" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="汇率日期">
              <el-date-picker
                v-model="listQuery.effectiveDate"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label=" " label-width="16px">
              <el-button type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
              <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%;"
    >
      <el-table-column label="源币种" prop="fromCurrency" align="center" min-width="80">
        <template slot-scope="scope">
          <span>{{ currencyMap[scope.row.fromCurrency] || scope.row.fromCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标币种" prop="toCurrency" align="center" min-width="80">
        <template slot-scope="scope">
          <span>{{ currencyMap[scope.row.toCurrency] || scope.row.toCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率日期" align="center" min-width="110">
        <template slot-scope="scope">
          <span>{{ formatRateDate(scope.row.effectiveDate || scope.row.rateDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="中间价" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.middleRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="买入价" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.buyRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="卖出价" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.sellRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现钞买入价" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.cashBuyRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现钞卖出价" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.cashSellRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率来源" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ rateSourceMap[scope.row.rateSource] || scope.row.rateSource }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" align="center" min-width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isActive === 1 ? 'success' : 'danger'">
            {{ scope.row.isActive === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 编辑汇率对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增汇率' : '编辑汇率'"
      :visible.sync="exchangeRateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form ref="exchangeRateForm" :model="currentExchangeRate" :rules="formRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源币种" prop="fromCurrency">
              <el-select v-model="currentExchangeRate.fromCurrency" placeholder="请选择源币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标币种" prop="toCurrency">
              <el-select v-model="currentExchangeRate.toCurrency" placeholder="请选择目标币种" style="width: 100%;">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
                <el-option label="英镑" value="GBP" />
                <el-option label="港币" value="HKD" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="汇率日期" prop="effectiveDate">
              <el-date-picker
                v-model="currentExchangeRate.effectiveDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%;"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="汇率来源" prop="rateSource">
              <el-select v-model="currentExchangeRate.rateSource" placeholder="请选择汇率来源" style="width: 100%;">
                <el-option label="央行" value="PBOC" />
                <el-option label="外汇管理局" value="SAFE" />
                <el-option label="银行" value="BANK" />
                <el-option label="第三方" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="中间价" prop="middleRate">
              <el-input-number v-model="currentExchangeRate.middleRate" :precision="6" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买入价" prop="buyRate">
              <el-input-number v-model="currentExchangeRate.buyRate" :precision="6" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="卖出价" prop="sellRate">
              <el-input-number v-model="currentExchangeRate.sellRate" :precision="6" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="现钞买入价" prop="cashBuyRate">
              <el-input-number v-model="currentExchangeRate.cashBuyRate" :precision="6" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="现钞卖出价" prop="cashSellRate">
              <el-input-number v-model="currentExchangeRate.cashSellRate" :precision="6" :step="0.0001" :min="0" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isActive">
              <el-select v-model="currentExchangeRate.isActive" placeholder="请选择状态" style="width: 100%;">
                <el-option label="启用" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDialogClose">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSaveExchangeRate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 趋势分析对话框 -->
    <el-dialog
      title="汇率趋势分析"
      :visible.sync="trendAnalysisVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleTrendAnalysisClose"
    >
      <div class="trend-analysis-content">
        <div class="analysis-header">
          <div class="currency-selector">
            <el-form :inline="true" :model="trendForm" class="trend-form">
              <el-form-item label="基准货币">
                <el-select v-model="trendForm.baseCurrency" placeholder="选择基准货币" style="width: 120px;">
                  <el-option label="USD" value="USD" />
                  <el-option label="EUR" value="EUR" />
                  <el-option label="CNY" value="CNY" />
                  <el-option label="JPY" value="JPY" />
                  <el-option label="GBP" value="GBP" />
                </el-select>
              </el-form-item>
              <el-form-item label="目标货币">
                <el-select v-model="trendForm.targetCurrency" placeholder="选择目标货币" style="width: 120px;">
                  <el-option label="USD" value="USD" />
                  <el-option label="EUR" value="EUR" />
                  <el-option label="CNY" value="CNY" />
                  <el-option label="JPY" value="JPY" />
                  <el-option label="GBP" value="GBP" />
                </el-select>
              </el-form-item>
              <el-form-item label="时间范围">
                <el-select v-model="trendForm.timeRange" placeholder="选择时间范围" style="width: 120px;">
                  <el-option label="最近7天" value="7d" />
                  <el-option label="最近30天" value="30d" />
                  <el-option label="最近90天" value="90d" />
                  <el-option label="最近1年" value="1y" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="generateTrendAnalysis">生成分析</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="analysis-body" v-if="trendData.length > 0">
          <!-- 趋势图表 -->
          <div class="chart-container">
            <div class="chart-placeholder">
              <div class="chart-info">
                <h4>汇率走势图</h4>
                <div class="trend-stats">
                  <div class="stat-item">
                    <span class="label">当前汇率:</span>
                    <span class="value">{{ currentRate }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">期间最高:</span>
                    <span class="value high">{{ highestRate }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">期间最低:</span>
                    <span class="value low">{{ lowestRate }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">波动幅度:</span>
                    <span class="value" :class="getFluctuationClass()">{{ fluctuationRate }}%</span>
                  </div>
                </div>
              </div>
              <div class="chart-visual">
                <div class="simple-chart">
                  <div class="chart-line">
                    <div v-for="(point, index) in chartPoints" :key="index"
                         class="chart-point"
                         :style="{
                           left: `${(index / (chartPoints.length - 1)) * 100}%`,
                           bottom: `${((point.value - lowestRate) / (highestRate - lowestRate)) * 80}%`
                         }">
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 分析结果 -->
          <div class="analysis-results">
            <el-tabs v-model="activeTab">
              <el-tab-pane label="趋势分析" name="trend">
                <div class="trend-summary">
                  <h4>趋势概述</h4>
                  <p>{{ trendSummary }}</p>
                  <div class="trend-indicators">
                    <div class="indicator" :class="trendDirection">
                      <i :class="trendIcon"></i>
                      <span>{{ trendText }}</span>
                    </div>
                    <div class="indicator">
                      <i class="el-icon-warning"></i>
                      <span>风险等级: {{ riskLevel }}</span>
                    </div>
                  </div>
                </div>
              </el-tab-pane>

              <el-tab-pane label="预测建议" name="forecast">
                <div class="forecast-content">
                  <h4>汇率预测</h4>
                  <div class="forecast-items">
                    <div class="forecast-item">
                      <span class="period">短期(1-7天)</span>
                      <span class="prediction">{{ forecastShortTerm }}</span>
                    </div>
                    <div class="forecast-item">
                      <span class="period">中期(1-4周)</span>
                      <span class="prediction">{{ forecastMediumTerm }}</span>
                    </div>
                    <div class="forecast-item">
                      <span class="period">长期(1-3月)</span>
                      <span class="prediction">{{ forecastLongTerm }}</span>
                    </div>
                  </div>

                  <div class="suggestions">
                    <h5>操作建议</h5>
                    <ul>
                      <li v-for="suggestion in suggestions" :key="suggestion">{{ suggestion }}</li>
                    </ul>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>

        <div class="empty-state" v-else>
          <el-empty description="请选择货币对和时间范围进行分析">
            <el-button type="primary" @click="generateTrendAnalysis">开始分析</el-button>
          </el-empty>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleTrendAnalysisClose">关闭</el-button>
        <el-button type="primary" @click="exportAnalysisReport">导出报告</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getExchangeRateList,
  createExchangeRate,
  updateExchangeRate,
  deleteExchangeRate,
  batchDeleteExchangeRate,
  getExchangeRateDetail,
  syncExchangeRate,
  exportExchangeRate,
  updateExchangeRateStatus,
  getExchangeRateStatistics,
  getExchangeRateTrendAnalysis,
  exportTrendAnalysisReport
} from '@/api/globalTreasurer/xjgl/dataRulesManage/rate'

export default {
  name: 'ExchangeRateManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      currencyMap: {
        CNY: '人民币',
        USD: '美元',
        EUR: '欧元',
        JPY: '日元',
        GBP: '英镑',
        HKD: '港币'
      },
      rateSourceMap: {
        PBOC: '央行',
        SAFE: '外汇管理局',
        BANK: '银行',
        THIRD_PARTY: '第三方'
      },
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        fromCurrency: undefined,
        toCurrency: undefined,
        rateSource: undefined,
        effectiveDate: undefined
      },
      totalCurrencies: 0,
      usdRate: '--',
      eurRate: '--',
      lastUpdateTime: '--',
      exchangeRateDialogVisible: false,
      trendAnalysisVisible: false,
      dialogStatus: 'create',
      currentExchangeRate: {},
      submitLoading: false,
      formRules: {
        fromCurrency: [{ required: true, message: '请选择源币种', trigger: 'change' }],
        toCurrency: [{ required: true, message: '请选择目标币种', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择汇率日期', trigger: 'change' }],
        middleRate: [{ required: true, message: '请输入中间价', trigger: 'blur' }],
        rateSource: [{ required: true, message: '请选择汇率来源', trigger: 'change' }]
      },
      activeTab: 'trend',
      trendForm: {
        baseCurrency: 'USD',
        targetCurrency: 'CNY',
        timeRange: '30d'
      },
      trendData: [],
      currentRate: 0,
      highestRate: 0,
      lowestRate: 0,
      fluctuationRate: 0,
      trendSummary: '',
      trendDirection: '',
      trendIcon: '',
      trendText: '',
      riskLevel: '',
      forecastShortTerm: '',
      forecastMediumTerm: '',
      forecastLongTerm: '',
      suggestions: [],
      chartPoints: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /**
     * 获取汇率数据列表
     */
    async fetchData() {
      this.listLoading = true
      try {
        // 构建查询参数，处理日期范围
        const query = {
          page: this.listQuery.page,
          limit: this.listQuery.limit
        }
        if (this.listQuery.fromCurrency) {
          query.fromCurrency = this.listQuery.fromCurrency
        }
        if (this.listQuery.toCurrency) {
          query.toCurrency = this.listQuery.toCurrency
        }
        if (this.listQuery.rateSource) {
          query.rateSource = this.listQuery.rateSource
        }
        // 日期范围拆分为 startDate / endDate
        if (this.listQuery.effectiveDate && this.listQuery.effectiveDate.length === 2) {
          query.startDate = this.listQuery.effectiveDate[0]
          query.endDate = this.listQuery.effectiveDate[1]
        }
        const response = await getExchangeRateList(query)

        if (response.code === 1) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(response.msg || response.message || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取汇率数据列表失败:', error)
        // 不重复显示错误消息，request.js已经处理过了
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }

      // 获取统计信息
      this.fetchStatistics()
    },

    /**
     * 获取汇率统计信息
     */
    async fetchStatistics() {
      try {
        const response = await getExchangeRateStatistics()
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          const stats = response.data
          this.totalCurrencies = stats.totalCurrencies || 6
          this.usdRate = stats.usdRate || '7.2456'
          this.eurRate = stats.eurRate || '7.8923'
          this.lastUpdateTime = stats.lastUpdateTime || new Date().getMinutes() + '分钟前'
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
        // 使用默认值
        this.totalCurrencies = 6
        this.usdRate = '7.2456'
        this.eurRate = '7.8923'
        this.lastUpdateTime = new Date().getMinutes() + '分钟前'
      }
    },

    getList(paginationPayload) {
      if (paginationPayload) {
        this.listQuery.page = paginationPayload.page
        this.listQuery.limit = paginationPayload.limit
      }
      this.fetchData()
    },

    formatRateDate(val) {
      if (!val) return ''
      // LocalDate 可能被 Jackson 序列化为数组 [2024, 1, 1]
      if (Array.isArray(val) && val.length >= 3) {
        const y = val[0]
        const m = String(val[1]).padStart(2, '0')
        const d = String(val[2]).padStart(2, '0')
        return `${y}-${m}-${d}`
      }
      // 已经是 yyyy-MM-dd 格式字符串
      if (typeof val === 'string' && /^[0-9]{4}-[0-9]{2}-[0-9]{2}/.test(val)) {
        return val.substring(0, 10)
      }
      // 时间戳或其他格式，走 parseTime
      return this.parseTime(val, '{y}-{m}-{d}') || ''
    },

    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },

    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        fromCurrency: undefined,
        toCurrency: undefined,
        rateSource: undefined,
        effectiveDate: undefined
      }
      this.fetchData()
    },

    handleCreate() {
      this.dialogStatus = 'create'
      this.currentExchangeRate = {}
      this.exchangeRateDialogVisible = true
    },

    handleUpdate(row) {
      this.dialogStatus = 'edit'
      this.currentExchangeRate = { ...row }
      this.exchangeRateDialogVisible = true
    },

    /**
     * 关闭编辑对话框
     */
    handleDialogClose() {
      this.exchangeRateDialogVisible = false
      this.currentExchangeRate = {}
      if (this.$refs.exchangeRateForm) {
        this.$refs.exchangeRateForm.resetFields()
      }
    },

    /**
     * 保存汇率（新增或编辑）
     */
    async handleSaveExchangeRate() {
      this.$refs.exchangeRateForm.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.submitLoading = true
        try {
          let response
          const successCodes = [200, '200', '1', 1, 2]

          if (this.dialogStatus === 'create') {
            // 新增
            response = await createExchangeRate(this.currentExchangeRate)
          } else {
            // 编辑
            response = await updateExchangeRate(this.currentExchangeRate)
          }

          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: this.dialogStatus === 'create' ? '新增成功' : '编辑成功',
              type: 'success',
              duration: 2000
            })
            this.handleDialogClose()
            this.fetchData()
          } else {
            this.$message.error(response.msg || response.message || '操作失败')
          }
        } catch (error) {
          console.error('保存汇率数据失败:', error)
          this.$message.error('保存失败，请稍后重试')
        } finally {
          this.submitLoading = false
        }
      })
    },

    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteExchangeRate({ rateId: row.rateId })
          const successCodes = [200, '200', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取数据
            this.fetchData()
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除汇率数据失败:', error)
        }
      })
    },
    async handleSyncRates() {
      const loading = this.$loading({
        lock: true,
        text: '正在同步汇率数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        const response = await syncExchangeRate({ sourceType: 'PBOC' })
        const successCodes = [200, '200', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.lastUpdateTime = new Date().getMinutes() + '分钟前'
          this.getList()
          this.$message.success('汇率同步成功')
        } else {
          this.$message.error(response.msg || response.message || '同步失败')
        }
      } catch (error) {
        console.error('同步汇率数据失败:', error)
      } finally {
        loading.close()
      }
    },
    handleTrendAnalysis() {
      this.trendAnalysisVisible = true
      this.activeTab = 'trend'
      this.generateTrendAnalysis()
    },
    handleExport() {
      this.$message.success('导出成功')
      // 模拟文件下载
      const dataStr = JSON.stringify(this.list, null, 2)
      const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
      const exportFileDefaultName = `汇率管理数据_${new Date().toISOString().split('T')[0]}.json`
      const linkElement = document.createElement('a')
      linkElement.setAttribute('href', dataUri)
      linkElement.setAttribute('download', exportFileDefaultName)
      linkElement.click()
    },
    // 趋势分析相关方法
    handleTrendAnalysisClose() {
      this.trendAnalysisVisible = false
    },
    async generateTrendAnalysis() {
      try {
        // 生成模拟数据
        const days = this.getTimeRangeDays()
        const baseRate = this.getBaseRate()

        this.trendData = []
        let currentDayRate = baseRate

        for (let i = days; i >= 0; i--) {
          const date = new Date()
          date.setDate(date.getDate() - i)

          // 模拟汇率波动
          const change = (Math.random() - 0.5) * 0.02 * baseRate
          currentDayRate = Math.max(baseRate * 0.9, Math.min(baseRate * 1.1, currentDayRate + change))

          this.trendData.push({
            date: date.toISOString().split('T')[0],
            rate: parseFloat(currentDayRate.toFixed(6))
          })
        }

        this.calculateTrendMetrics()
        this.generateAnalysisResults()

      } catch (error) {
        this.$message.error('趋势分析生成失败')
      }
    },
    getTimeRangeDays() {
      const rangeMap = {
        '7d': 7,
        '30d': 30,
        '90d': 90,
        '1y': 365
      }
      return rangeMap[this.trendForm.timeRange] || 30
    },
    getBaseRate() {
      // 根据货币对返回基础汇率
      const pair = `${this.trendForm.baseCurrency}/${this.trendForm.targetCurrency}`
      const rateMap = {
        'USD/CNY': 7.2456,
        'EUR/CNY': 7.8923,
        'JPY/CNY': 0.0489,
        'GBP/CNY': 9.1234,
        'CNY/USD': 0.1380,
        'EUR/USD': 1.0892,
        'JPY/USD': 0.0068,
        'GBP/USD': 1.2593
      }
      return rateMap[pair] || 7.0
    },
    calculateTrendMetrics() {
      if (this.trendData.length === 0) return

      const rates = this.trendData.map(d => d.rate)
      this.currentRate = rates[rates.length - 1]
      this.highestRate = Math.max(...rates)
      this.lowestRate = Math.min(...rates)
      this.fluctuationRate = ((this.highestRate - this.lowestRate) / this.lowestRate * 100).toFixed(2)

      // 生成图表点
      this.chartPoints = this.trendData.map((d, index) => ({
        index,
        value: d.rate,
        date: d.date
      }))
    },
    generateAnalysisResults() {
      const firstRate = this.trendData[0]?.rate || 0
      const lastRate = this.currentRate
      const change = lastRate - firstRate
      const changePercent = (change / firstRate * 100).toFixed(2)

      // 判断趋势方向
      if (change > 0) {
        this.trendDirection = 'upward'
        this.trendIcon = 'el-icon-top'
        this.trendText = `上涨趋势 (+${changePercent}%)`
      } else if (change < 0) {
        this.trendDirection = 'downward'
        this.trendIcon = 'el-icon-bottom'
        this.trendText = `下跌趋势 (${changePercent}%)`
      } else {
        this.trendDirection = 'stable'
        this.trendIcon = 'el-icon-minus'
        this.trendText = '保持稳定'
      }

      // 生成趋势总结
      this.trendSummary = `在过去的${this.getTimeRangeDays()}天中，${this.trendForm.baseCurrency}/${this.trendForm.targetCurrency}汇率${this.trendText}。期间最高${this.highestRate.toFixed(4)}，最低${this.lowestRate.toFixed(4)}，波动幅度为${this.fluctuationRate}%。`

      // 评估风险等级
      if (parseFloat(this.fluctuationRate) > 5) {
        this.riskLevel = '高'
      } else if (parseFloat(this.fluctuationRate) > 2) {
        this.riskLevel = '中'
      } else {
        this.riskLevel = '低'
      }

      // 生成预测建议
      this.generateForecasts()
    },
    generateForecasts() {
      const currentRate = this.currentRate
      const volatility = parseFloat(this.fluctuationRate) / 100

      // 短期预测
      const shortTermChange = (Math.random() - 0.5) * currentRate * volatility * 0.3
      const shortTermRate = currentRate + shortTermChange
      const shortTermDirection = shortTermChange > 0 ? '看涨' : '看跌'
      this.forecastShortTerm = `${shortTermRate.toFixed(4)} (${shortTermDirection})`

      // 中期预测
      const mediumTermChange = (Math.random() - 0.5) * currentRate * volatility * 0.8
      const mediumTermRate = currentRate + mediumTermChange
      const mediumTermDirection = mediumTermChange > 0 ? '看涨' : '看跌'
      this.forecastMediumTerm = `${mediumTermRate.toFixed(4)} (${mediumTermDirection})`

      // 长期预测
      const longTermChange = (Math.random() - 0.5) * currentRate * volatility * 1.5
      const longTermRate = currentRate + longTermChange
      const longTermDirection = longTermChange > 0 ? '看涨' : '看跌'
      this.forecastLongTerm = `${longTermRate.toFixed(4)} (${longTermDirection})`

      // 生成操作建议
      this.suggestions = [
        `当前${this.riskLevel}风险等级，建议${this.riskLevel === '高' ? '密切关注' : '保持关注'}汇率变化`,
        `根据趋势分析，建议采用${this.trendDirection === 'upward' ? '分批买入' : this.trendDirection === 'downward' ? '分批卖出' : '持有观望'}策略`,
        `波动幅度为${this.fluctuationRate}%，建议设置${(parseFloat(this.fluctuationRate) * 0.5).toFixed(2)}%的止损点`,
        '关注重要经济数据发布对汇率的潜在影响',
        '建议定期更新分析，及时调整交易策略'
      ]
    },
    getFluctuationClass() {
      const rate = parseFloat(this.fluctuationRate)
      if (rate > 5) return 'high'
      if (rate > 2) return 'medium'
      return 'low'
    },
    exportAnalysisReport() {
      const report = {
        currencyPair: `${this.trendForm.baseCurrency}/${this.trendForm.targetCurrency}`,
        timeRange: this.trendForm.timeRange,
        analysisDate: new Date().toISOString(),
        currentRate: this.currentRate,
        highestRate: this.highestRate,
        lowestRate: this.lowestRate,
        fluctuationRate: this.fluctuationRate,
        trendDirection: this.trendDirection,
        riskLevel: this.riskLevel,
        forecastShortTerm: this.forecastShortTerm,
        forecastMediumTerm: this.forecastMediumTerm,
        forecastLongTerm: this.forecastLongTerm,
        suggestions: this.suggestions,
        trendData: this.trendData
      }

      const dataStr = JSON.stringify(report, null, 2)
      const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
      const exportFileDefaultName = `汇率趋势分析报告_${this.trendForm.baseCurrency}_${this.trendForm.targetCurrency}_${new Date().toISOString().split('T')[0]}.json`

      const linkElement = document.createElement('a')
      linkElement.setAttribute('href', dataUri)
      linkElement.setAttribute('download', exportFileDefaultName)
      linkElement.click()

      this.$message.success('分析报告导出成功')
    }
  }
}
</script>

<style lang="scss" scoped>
// 趋势分析对话框样式
.trend-analysis-content {
  .analysis-header {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 4px;

    .trend-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .analysis-body {
    .chart-container {
      margin-bottom: 20px;

      .chart-placeholder {
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        padding: 20px;
        background-color: #fff;

        .chart-info {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;

          h4 {
            margin: 0;
            color: #303133;
          }

          .trend-stats {
            display: flex;
            gap: 20px;

            .stat-item {
              display: flex;
              align-items: center;
              font-size: 14px;

              .label {
                color: #909399;
                margin-right: 8px;
              }

              .value {
                font-weight: 600;
                color: #303133;

                &.high {
                  color: #67c23a;
                }

                &.low {
                  color: #f56c6c;
                }

                &.medium {
                  color: #e6a23c;
                }
              }
            }
          }
        }

        .chart-visual {
          .simple-chart {
            height: 200px;
            position: relative;
            background: linear-gradient(to bottom, #f0f9ff 0%, #e6f7ff 100%);
            border-radius: 4px;

            .chart-line {
              position: relative;
              height: 100%;

              &::before {
                content: '';
                position: absolute;
                left: 0;
                right: 0;
                bottom: 0;
                height: 1px;
                background-color: #e4e7ed;
              }

              .chart-point {
                position: absolute;
                width: 6px;
                height: 6px;
                background-color: #409eff;
                border-radius: 50%;
                transform: translateX(-50%);
                box-shadow: 0 2px 4px rgba(64, 158, 255, 0.3);

                &::after {
                  content: '';
                  position: absolute;
                  top: -1px;
                  left: -1px;
                  right: -1px;
                  bottom: -1px;
                  background-color: rgba(64, 158, 255, 0.2);
                  border-radius: 50%;
                  animation: pulse 2s infinite;
                }
              }
            }
          }
        }
      }
    }

    .analysis-results {
      .trend-summary {
        h4 {
          margin: 0 0 10px 0;
          color: #303133;
        }

        p {
          margin: 0 0 15px 0;
          line-height: 1.6;
          color: #606266;
        }

        .trend-indicators {
          display: flex;
          gap: 20px;

          .indicator {
            display: flex;
            align-items: center;
            padding: 8px 12px;
            border-radius: 4px;
            font-size: 14px;

            i {
              margin-right: 8px;
              font-size: 16px;
            }

            &.upward {
              background-color: #f0f9ff;
              color: #67c23a;
              border: 1px solid #b3e19d;
            }

            &.downward {
              background-color: #fef0f0;
              color: #f56c6c;
              border: 1px solid #fbc4c4;
            }

            &.stable {
              background-color: #f9f9f9;
              color: #909399;
              border: 1px solid #dcdfe6;
            }
          }
        }
      }

      .forecast-content {
        h4 {
          margin: 0 0 15px 0;
          color: #303133;
        }

        .forecast-items {
          margin-bottom: 20px;

          .forecast-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .period {
              font-weight: 500;
              color: #303133;
            }

            .prediction {
              color: #409eff;
              font-weight: 600;
            }
          }
        }

        .suggestions {
          h5 {
            margin: 0 0 10px 0;
            color: #303133;
            font-size: 14px;
          }

          ul {
            margin: 0;
            padding-left: 20px;

            li {
              margin-bottom: 8px;
              line-height: 1.5;
              color: #606266;

              &:last-child {
                margin-bottom: 0;
              }
            }
          }
        }
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: 40px 0;
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.5;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
