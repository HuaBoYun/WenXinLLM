<template>
  <div class="financial-analysis-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>财务分析概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          刷新
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalAssets || 0 }}</div>
              <div class="stat-label">资产总额（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalLiabilities || 0 }}</div>
              <div class="stat-label">负债总额（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.netAssets || 0 }}</div>
              <div class="stat-label">净资产（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #F56C6C 0%, #f78989 100%);">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.debtRatio || 0 }}%</div>
              <div class="stat-label">资产负债率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>财务指标趋势</span>
          </div>
          <div ref="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>资产结构分析</span>
          </div>
          <div ref="assetChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="table-card">
      <div slot="header">
        <span>财务指标明细</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="generateReport">生成报告</el-button>
          <el-button type="success" size="small" @click="exportData">导出数据</el-button>
        </div>
      </div>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="分析类型">
          <el-select v-model="queryForm.analysisType" placeholder="请选择分析类型" clearable>
            <el-option label="盈利能力" value="profitability"></el-option>
            <el-option label="偿债能力" value="solvency"></el-option>
            <el-option label="营运能力" value="operational"></el-option>
            <el-option label="发展能力" value="growth"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="indicatorList" border v-loading="loading">
        <el-table-column prop="indicatorName" label="指标名称" width="200"></el-table-column>
        <el-table-column prop="currentValue" label="当期值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatValue(scope.row.currentValue, scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="previousValue" label="上期值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatValue(scope.row.previousValue, scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="changeRate" label="变动率" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getChangeClass(scope.row.changeRate)">
              {{ scope.row.changeRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="industryAverage" label="行业平均" width="120" align="right">
          <template slot-scope="scope">
            {{ formatValue(scope.row.industryAverage, scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRatingType(scope.row.rating)">
              {{ scope.row.rating }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="trend" label="趋势" width="100" align="center">
          <template slot-scope="scope">
            <i :class="getTrendIcon(scope.row.trend)" :style="getTrendColor(scope.row.trend)"></i>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">
              详情
            </el-button>
            <el-button type="text" size="small" @click="analyzeIndicator(scope.row)">
              分析
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 指标详情弹窗 -->
    <el-dialog title="指标详情" :visible.sync="detailDialogVisible" width="600px" append-to-body>
      <template v-if="detailRow">
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="指标名称">{{ detailRow.indicatorName }}</el-descriptions-item>
          <el-descriptions-item label="单位">{{ detailRow.unit }}</el-descriptions-item>
          <el-descriptions-item label="当期值">{{ formatValue(detailRow.currentValue, detailRow.unit) }}</el-descriptions-item>
          <el-descriptions-item label="上期值">{{ formatValue(detailRow.previousValue, detailRow.unit) }}</el-descriptions-item>
          <el-descriptions-item label="变动率">
            <span :class="getChangeClass(detailRow.changeRate)">
              {{ detailRow.changeRate !== '-' ? detailRow.changeRate + '%' : '-' }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="行业平均">{{ formatValue(detailRow.industryAverage, detailRow.unit) }}</el-descriptions-item>
          <el-descriptions-item label="评级">
            <el-tag :type="getRatingType(detailRow.rating)" size="small">{{ detailRow.rating }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="趋势">
            <i :class="getTrendIcon(detailRow.trend)" :style="getTrendColor(detailRow.trend)"></i>
            {{ detailRow.trend === 'up' ? '上升' : detailRow.trend === 'down' ? '下降' : '平稳' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detailRow.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
        <div class="detail-explanation">
          <h4>指标说明</h4>
          <p>{{ getIndicatorExplanation(detailRow) }}</p>
        </div>
      </template>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>

    <!-- 深度分析弹窗 -->
    <el-dialog title="深度分析" :visible.sync="analysisDialogVisible" width="650px" append-to-body>
      <template v-if="analysisRow">
        <div class="analysis-header">
          <h3>{{ analysisRow.indicatorName }}</h3>
          <span class="analysis-value">当前值：{{ formatValue(analysisRow.currentValue, analysisRow.unit) }}</span>
        </div>

        <div class="analysis-comparison">
          <h4>与行业平均对比</h4>
          <div class="comparison-bar-wrapper">
            <div class="comparison-item">
              <span class="comparison-label">当前值</span>
              <el-progress
                :percentage="getComparisonPercent(analysisRow.currentValue, analysisRow.industryAverage)"
                :color="getComparisonColor(analysisRow.currentValue, analysisRow.industryAverage)"
                :stroke-width="18"
                :text-inside="true"
                :format="() => formatValue(analysisRow.currentValue, analysisRow.unit)"
              ></el-progress>
            </div>
            <div class="comparison-item">
              <span class="comparison-label">行业平均</span>
              <el-progress
                :percentage="50"
                color="#909399"
                :stroke-width="18"
                :text-inside="true"
                :format="() => formatValue(analysisRow.industryAverage, analysisRow.unit)"
              ></el-progress>
            </div>
          </div>
        </div>

        <div class="analysis-rating">
          <h4>综合评级</h4>
          <el-tag :type="getRatingType(analysisRow.rating)" size="medium" effect="dark">
            {{ analysisRow.rating }}
          </el-tag>
          <span class="trend-display">
            趋势：
            <i :class="getTrendIcon(analysisRow.trend)" :style="getTrendColor(analysisRow.trend)"></i>
            {{ analysisRow.trend === 'up' ? '上升' : analysisRow.trend === 'down' ? '下降' : '平稳' }}
          </span>
        </div>

        <div class="analysis-text">
          <h4>分析结论</h4>
          <p>{{ getAnalysisText(analysisRow) }}</p>
        </div>
      </template>
      <span slot="footer" class="dialog-footer">
        <el-button @click="analysisDialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getFinancialIndicators,
  getHistoricalTrend,
  getProfitabilityAnalysis,
  getSolvencyAnalysis,
  getOperatingAnalysis,
  getDevelopmentAnalysis,
  generateFinancialReport
} from '@/api/enterprise/financial'

export default {
  name: 'FinancialAnalysisTab',
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      queryForm: {
        analysisType: '',
        dateRange: []
      },
      overview: {
        totalAssets: 0,
        totalLiabilities: 0,
        netAssets: 0,
        debtRatio: 0
      },
      indicatorList: [],
      trendChart: null,
      assetChart: null,
      detailDialogVisible: false,
      detailRow: null,
      analysisDialogVisible: false,
      analysisRow: null
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.loadData()
          this.loadCharts()
        }
      },
      immediate: true
    }
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
      this.trendChart = null
    }
    if (this.assetChart) {
      this.assetChart.dispose()
      this.assetChart = null
    }
    window.removeEventListener('resize', this.handleResize)
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.assetChart) this.assetChart.resize()
    },

    loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      const params = { enterpriseId: this.enterpriseId }
      // 传递时间范围参数
      if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
        params.startDate = this.queryForm.dateRange[0]
        params.endDate = this.queryForm.dateRange[1]
      }

      // 根据分析类型调用不同的API
      let api = getFinancialIndicators
      if (this.queryForm.analysisType === 'profitability') api = getProfitabilityAnalysis
      else if (this.queryForm.analysisType === 'solvency') api = getSolvencyAnalysis
      else if (this.queryForm.analysisType === 'operational') api = getOperatingAnalysis
      else if (this.queryForm.analysisType === 'growth') api = getDevelopmentAnalysis

      api(params).then(response => {
        const data = response.data || {}
        // 更新概览 - 后端返回totalProfit字段，前端需要netProfit语义
        if (data.totalAssets || data.totalRevenue || data.totalProfit) {
          const totalAssets = data.totalAssets || 0
          const totalLiabilities = data.totalLiabilities || 0
          this.overview = {
            totalAssets: totalAssets ? (totalAssets / 10000).toFixed(2) : 0,
            totalLiabilities: totalLiabilities ? (totalLiabilities / 10000).toFixed(2) : 0,
            netAssets: ((totalAssets - totalLiabilities) / 10000).toFixed(2),
            debtRatio: totalAssets > 0 ? ((totalLiabilities / totalAssets) * 100).toFixed(1) : 0
          }
        }
        // 构建指标列表 - 从后端返回数据中提取
        this.indicatorList = this.buildIndicatorList(data)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    buildIndicatorList(data) {
      if (!data) return []
      const list = []
      // 从统计数据构建指标列表 - 使用后端返回的上期数据
      if (data.totalRevenue || data.totalProfit || data.totalAssets) {
        const revenueGrowth = data.revenueGrowth || 0
        const profitGrowth = data.profitGrowth || 0
        const assetGrowth = data.assetGrowth || 0

        list.push({
          indicatorName: '营业收入',
          currentValue: data.totalRevenue ? (data.totalRevenue / 10000).toFixed(2) : '0',
          previousValue: data.prevRevenue ? (data.prevRevenue / 10000).toFixed(2) : '0',
          changeRate: revenueGrowth,
          industryAverage: '-',
          rating: revenueGrowth > 10 ? '优秀' : revenueGrowth > 0 ? '良好' : '较差',
          trend: revenueGrowth > 0 ? 'up' : revenueGrowth < 0 ? 'down' : 'stable',
          unit: '万元',
          remark: '主营业务收入'
        })
        list.push({
          indicatorName: '净利润',
          currentValue: data.totalProfit ? (data.totalProfit / 10000).toFixed(2) : '0',
          previousValue: data.prevProfit ? (data.prevProfit / 10000).toFixed(2) : '0',
          changeRate: profitGrowth,
          industryAverage: '-',
          rating: profitGrowth > 10 ? '优秀' : profitGrowth > 0 ? '良好' : '较差',
          trend: profitGrowth > 0 ? 'up' : profitGrowth < 0 ? 'down' : 'stable',
          unit: '万元',
          remark: '扣除各项费用后利润'
        })
        list.push({
          indicatorName: '总资产',
          currentValue: data.totalAssets ? (data.totalAssets / 10000).toFixed(2) : '0',
          previousValue: data.prevAssets ? (data.prevAssets / 10000).toFixed(2) : '0',
          changeRate: assetGrowth,
          industryAverage: '-',
          rating: '良好',
          trend: assetGrowth > 0 ? 'up' : assetGrowth < 0 ? 'down' : 'stable',
          unit: '万元',
          remark: '资产总计'
        })
        if (data.totalLiabilities) {
          const debtRatio = data.debtRatio || (data.totalAssets > 0 ? ((data.totalLiabilities / data.totalAssets) * 100).toFixed(1) : 0)
          const prevDebtRatio = data.prevDebtRatio || 0
          const debtRatioChange = prevDebtRatio > 0 ? ((debtRatio - prevDebtRatio) / prevDebtRatio * 100).toFixed(2) : 0
          list.push({
            indicatorName: '资产负债率',
            currentValue: typeof debtRatio === 'number' ? debtRatio.toFixed(1) : debtRatio,
            previousValue: prevDebtRatio ? (typeof prevDebtRatio === 'number' ? prevDebtRatio.toFixed(1) : prevDebtRatio) : '0',
            changeRate: Number(debtRatioChange),
            industryAverage: '50',
            rating: debtRatio < 50 ? '优秀' : debtRatio < 70 ? '良好' : '较差',
            trend: debtRatio > 70 ? 'up' : 'stable',
            unit: '%',
            remark: '负债总额/资产总额'
          })
        }
        if (data.roa || (data.totalProfit && data.totalAssets)) {
          const roa = data.roa || ((data.totalProfit / data.totalAssets) * 100).toFixed(2)
          const prevRoa = data.prevRoa || 0
          const roaChange = prevRoa > 0 ? ((roa - prevRoa) / Math.abs(prevRoa) * 100).toFixed(2) : 0
          list.push({
            indicatorName: '总资产收益率(ROA)',
            currentValue: typeof roa === 'number' ? roa.toFixed(2) : roa,
            previousValue: prevRoa ? (typeof prevRoa === 'number' ? prevRoa.toFixed(2) : prevRoa) : '0',
            changeRate: Number(roaChange),
            industryAverage: '5',
            rating: roa > 5 ? '优秀' : roa > 2 ? '良好' : '较差',
            trend: roa > 5 ? 'up' : 'stable',
            unit: '%',
            remark: '净利润/总资产'
          })
        }
        // ROE指标
        if (data.roe || (data.totalProfit && data.totalAssets && data.totalLiabilities)) {
          const roe = data.roe || 0
          const prevRoe = data.prevRoe || 0
          const roeChange = prevRoe > 0 ? ((roe - prevRoe) / Math.abs(prevRoe) * 100).toFixed(2) : 0
          list.push({
            indicatorName: '净资产收益率(ROE)',
            currentValue: typeof roe === 'number' ? roe.toFixed(2) : roe,
            previousValue: prevRoe ? (typeof prevRoe === 'number' ? prevRoe.toFixed(2) : prevRoe) : '0',
            changeRate: Number(roeChange),
            industryAverage: '10',
            rating: roe > 10 ? '优秀' : roe > 5 ? '良好' : '较差',
            trend: roe > 10 ? 'up' : 'stable',
            unit: '%',
            remark: '净利润/净资产'
          })
        }
        // 净利润率
        if (data.profitMargin || (data.totalProfit && data.totalRevenue)) {
          const profitMargin = data.profitMargin || (data.totalRevenue > 0 ? ((data.totalProfit / data.totalRevenue) * 100).toFixed(2) : 0)
          list.push({
            indicatorName: '净利润率',
            currentValue: typeof profitMargin === 'number' ? profitMargin.toFixed(2) : profitMargin,
            previousValue: '-',
            changeRate: 0,
            industryAverage: '8',
            rating: profitMargin > 8 ? '优秀' : profitMargin > 3 ? '良好' : '较差',
            trend: profitMargin > 8 ? 'up' : 'stable',
            unit: '%',
            remark: '净利润/营业收入'
          })
        }
      }
      return list
    },

    loadCharts() {
      if (!this.enterpriseId) return
      // 获取历史趋势数据绘制图表
      getHistoricalTrend({ enterpriseId: this.enterpriseId }).then(response => {
        const list = response.data || []
        this.initTrendChart(list)
        this.initAssetChart(list)
      }).catch(() => {
        this.initTrendChart([])
        this.initAssetChart([])
      })
    },

    initTrendChart(list) {
      if (!this.$refs.trendChart) return
      if (this.trendChart) this.trendChart.dispose()
      this.trendChart = echarts.init(this.$refs.trendChart)

      const periods = list.map(i => i.reportPeriod || '')
      const revenues = list.map(i => i.operatingRevenue ? (i.operatingRevenue / 10000) : 0)
      const profits = list.map(i => i.netProfit ? (i.netProfit / 10000) : 0)

      this.trendChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['营业收入', '净利润'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: periods, boundaryGap: false },
        yAxis: { type: 'value', name: '金额(万元)', axisLabel: { formatter: '{value}' } },
        series: [
          {
            name: '营业收入',
            type: 'line',
            data: revenues,
            smooth: true,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '净利润',
            type: 'line',
            data: profits,
            smooth: true,
            itemStyle: { color: '#67C23A' }
          }
        ]
      })
    },

    initAssetChart(list) {
      if (!this.$refs.assetChart) return
      if (this.assetChart) this.assetChart.dispose()
      this.assetChart = echarts.init(this.$refs.assetChart)

      // 取最新一期的数据做饼图 - 金额单位是元，转为万元
      const latest = list.length > 0 ? list[list.length - 1] : {}
      const totalAssets = latest.totalAssets || 0
      const totalLiabilities = latest.totalLiabilities || 0
      const netAssets = totalAssets - totalLiabilities

      this.assetChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} 万元 ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [
          {
            name: '资产结构',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
            label: { show: true, formatter: '{b}: {d}%' },
            data: [
              { value: parseFloat(((netAssets / 10000) || 0).toFixed(2)), name: '净资产', itemStyle: { color: '#67C23A' } },
              { value: parseFloat(((totalLiabilities / 10000) || 0).toFixed(2)), name: '负债', itemStyle: { color: '#F56C6C' } }
            ]
          }
        ]
      })
    },

    refreshData() {
      this.loadData()
      this.loadCharts()
      this.$message.success('数据刷新成功')
    },

    queryData() {
      this.loadData()
    },

    resetQuery() {
      this.queryForm = {
        analysisType: '',
        dateRange: []
      }
      this.loadData()
    },

    generateReport() {
      if (!this.enterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      generateFinancialReport({
        enterpriseId: this.enterpriseId,
        enterpriseName: this.enterpriseName,
        reportType: 'financial_analysis'
      }).then(() => {
        this.$message.success('财务分析报告生成中...')
      }).catch(() => {
        this.$message.error('报告生成失败')
      })
    },

    exportData() {
      if (!this.indicatorList || this.indicatorList.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = ['指标名称', '当期值', '上期值', '变动率(%)', '行业平均', '评级', '趋势', '单位', '备注']
      const rows = this.indicatorList.map(item => [
        item.indicatorName,
        item.currentValue,
        item.previousValue,
        item.changeRate,
        item.industryAverage,
        item.rating,
        item.trend === 'up' ? '上升' : item.trend === 'down' ? '下降' : '平稳',
        item.unit,
        item.remark || ''
      ])
      // BOM + CSV content
      const BOM = '\uFEFF'
      const csvContent = BOM + [headers.join(','), ...rows.map(r => r.map(v => '"' + String(v).replace(/"/g, '""') + '"').join(','))].join('\n')
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      const now = new Date()
      const dateStr = now.getFullYear() + String(now.getMonth() + 1).padStart(2, '0') + String(now.getDate()).padStart(2, '0')
      link.href = url
      link.download = '财务分析_' + (this.enterpriseName || '企业') + '_' + dateStr + '.csv'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      this.$message.success('数据导出成功')
    },

    viewDetail(row) {
      this.detailRow = row
      this.detailDialogVisible = true
    },

    analyzeIndicator(row) {
      this.analysisRow = row
      this.analysisDialogVisible = true
    },

    getIndicatorExplanation(row) {
      const explanations = {
        '营业收入': '营业收入是企业在一定期间内通过主营业务和其他业务活动所取得的收入总额，反映企业的经营规模和市场竞争力。',
        '净利润': '净利润是企业在一定期间内扣除所有成本、费用和税金后的最终盈利，是衡量企业盈利能力的核心指标。',
        '总资产': '总资产是企业拥有或控制的全部资产总额，包括流动资产和非流动资产，反映企业的资源规模。',
        '资产负债率': '资产负债率是负债总额与资产总额的比率，反映企业的长期偿债能力和财务风险水平。一般认为低于50%较为安全。',
        '总资产收益率(ROA)': '总资产收益率是净利润与总资产的比率，衡量企业利用全部资产获取利润的能力。数值越高说明资产利用效率越好。',
        '净资产收益率(ROE)': '净资产收益率是净利润与净资产的比率，反映股东权益的收益水平。是衡量企业自有资本获利能力的重要指标。'
      }
      return explanations[row.indicatorName] || '该指标用于衡量企业在特定维度的财务表现，结合行业平均值和历史趋势可综合评估企业财务状况。'
    },

    getComparisonPercent(currentValue, industryAverage) {
      const current = parseFloat(currentValue)
      const average = parseFloat(industryAverage)
      if (isNaN(current) || isNaN(average) || average === 0) return 50
      const ratio = (current / average) * 50
      return Math.min(Math.max(ratio, 5), 100)
    },

    getComparisonColor(currentValue, industryAverage) {
      const current = parseFloat(currentValue)
      const average = parseFloat(industryAverage)
      if (isNaN(current) || isNaN(average)) return '#909399'
      if (current >= average) return '#67C23A'
      if (current >= average * 0.7) return '#E6A23C'
      return '#F56C6C'
    },

    getAnalysisText(row) {
      const current = parseFloat(row.currentValue)
      const average = parseFloat(row.industryAverage)
      const name = row.indicatorName
      const rating = row.rating
      const trend = row.trend === 'up' ? '上升' : row.trend === 'down' ? '下降' : '平稳'

      let text = '【' + name + '】当前值为 ' + this.formatValue(row.currentValue, row.unit) + '，'

      if (!isNaN(current) && !isNaN(average) && average > 0) {
        const diff = ((current - average) / average * 100).toFixed(1)
        if (current > average) {
          text += '高于行业平均水平 ' + diff + '%，表现' + (rating === '优秀' ? '优异' : '良好') + '。'
        } else if (current < average) {
          text += '低于行业平均水平 ' + Math.abs(diff) + '%，需要关注改善。'
        } else {
          text += '与行业平均水平持平。'
        }
      } else {
        text += '行业对比数据暂不可用。'
      }

      text += ' 当前趋势为' + trend + '，综合评级为「' + rating + '」。'

      if (rating === '较差') {
        text += ' 建议深入分析原因，制定改善措施。'
      } else if (rating === '优秀') {
        text += ' 企业在该指标上表现突出，建议保持当前经营策略。'
      }

      return text
    },

    formatValue(value, unit) {
      if (!value || value === '-') return '-'
      if (unit === '%') return value + '%'
      if (unit === '万元') return value + '万元'
      return value
    },

    getChangeClass(rate) {
      if (rate > 0) return 'change-up'
      if (rate < 0) return 'change-down'
      return ''
    },

    getRatingType(rating) {
      const ratingMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return ratingMap[rating] || 'info'
    },

    getTrendIcon(trend) {
      const iconMap = {
        'up': 'el-icon-top',
        'down': 'el-icon-bottom',
        'stable': 'el-icon-minus'
      }
      return iconMap[trend] || 'el-icon-minus'
    },

    getTrendColor(trend) {
      const colorMap = {
        'up': { color: '#67C23A' },
        'down': { color: '#F56C6C' },
        'stable': { color: '#909399' }
      }
      return colorMap[trend] || { color: '#909399' }
    }
  }
}
</script>

<style scoped>
.financial-analysis-tab {
  padding: 20px;
}
.overview-card {
  margin-bottom: 20px;
}
.stat-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
  margin-right: 12px;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
  min-width: 0;
}
.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
  margin-bottom: 4px;
}
.stat-label {
  font-size: 13px;
  color: #909399;
}
.chart-card {
  margin-bottom: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.query-form {
  margin-bottom: 20px;
}
.change-up {
  color: #67C23A;
}
.change-down {
  color: #F56C6C;
}
.detail-explanation {
  margin-top: 20px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}
.detail-explanation h4 {
  margin: 0 0 8px 0;
  color: #303133;
}
.detail-explanation p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}
.analysis-header {
  text-align: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #EBEEF5;
}
.analysis-header h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #303133;
}
.analysis-value {
  font-size: 16px;
  color: #409EFF;
  font-weight: bold;
}
.analysis-comparison {
  margin-bottom: 20px;
}
.analysis-comparison h4 {
  margin: 0 0 12px 0;
  color: #303133;
}
.comparison-bar-wrapper {
  padding: 0 10px;
}
.comparison-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.comparison-label {
  width: 70px;
  font-size: 13px;
  color: #606266;
  flex-shrink: 0;
}
.comparison-item .el-progress {
  flex: 1;
}
.analysis-rating {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}
.analysis-rating h4 {
  margin: 0;
  color: #303133;
}
.trend-display {
  color: #606266;
  font-size: 14px;
}
.analysis-text {
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}
.analysis-text h4 {
  margin: 0 0 8px 0;
  color: #303133;
}
.analysis-text p {
  margin: 0;
  color: #606266;
  line-height: 1.8;
}
</style>
