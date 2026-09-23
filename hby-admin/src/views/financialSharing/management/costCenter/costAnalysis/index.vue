<template>
  <div class="cost-analysis-container">
    <!-- 分析条件 -->
    <div class="analysis-conditions">
      <el-form :model="analysisForm" ref="analysisForm" :inline="true" class="analysis-form">
        <el-form-item label="分析期间" prop="analysisPeriod">
          <el-date-picker
            v-model="analysisForm.analysisPeriod"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="成本中心" prop="costCenterIds">
          <el-select
            v-model="analysisForm.costCenterIds"
            placeholder="请选择成本中心"
            multiple
            clearable
            style="width: 200px"
          >
            <!-- 动态加载成本中心列表 -->
          </el-select>
        </el-form-item>
        <el-form-item label="分析维度" prop="analysisDimension">
          <el-select
            v-model="analysisForm.analysisDimension"
            placeholder="请选择分析维度"
            style="width: 150px"
          >
            <el-option label="按成本中心" value="center" />
            <el-option label="按成本类型" value="type" />
            <el-option label="按时间" value="time" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAnalysis">开始分析</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleExport">导出报告</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 分析概览 -->
    <div class="analysis-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card total-cost">
            <div class="card-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-title">总成本</div>
              <div class="card-value">{{ formatAmount(overview.totalCost) }}</div>
              <div class="card-trend">
                <i :class="overview.totalCostTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'" 
                   :style="{ color: overview.totalCostTrend > 0 ? '#F56C6C' : '#67C23A' }"></i>
                <span>{{ Math.abs(overview.totalCostTrend) }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card avg-cost">
            <div class="card-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-content">
              <div class="card-title">平均成本</div>
              <div class="card-value">{{ formatAmount(overview.avgCost) }}</div>
              <div class="card-trend">
                <span>每中心平均</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card cost-variance">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-content">
              <div class="card-title">成本差异</div>
              <div class="card-value">{{ overview.costVariance }}%</div>
              <div class="card-trend">
                <span>预算差异率</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card efficiency-index">
            <div class="card-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-content">
              <div class="card-title">效率指数</div>
              <div class="card-value">{{ overview.efficiencyIndex }}</div>
              <div class="card-trend">
                <span>成本效率</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析图表 -->
    <div class="analysis-charts">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h4>成本结构分析</h4>
              <el-button-group>
                <el-button size="mini" :type="structureChartType === 'pie' ? 'primary' : ''" @click="structureChartType = 'pie'">饼图</el-button>
                <el-button size="mini" :type="structureChartType === 'bar' ? 'primary' : ''" @click="structureChartType = 'bar'">柱图</el-button>
              </el-button-group>
            </div>
            <div class="chart-content" ref="structureChart" style="height: 300px;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h4>成本趋势分析</h4>
              <el-button-group>
                <el-button size="mini" :type="trendChartType === 'line' ? 'primary' : ''" @click="trendChartType = 'line'">折线图</el-button>
                <el-button size="mini" :type="trendChartType === 'area' ? 'primary' : ''" @click="trendChartType = 'area'">面积图</el-button>
              </el-button-group>
            </div>
            <div class="chart-content" ref="trendChart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <div class="chart-card">
            <div class="chart-header">
              <h4>成本中心对比分析</h4>
              <el-button-group>
                <el-button size="mini" :type="compareChartType === 'bar' ? 'primary' : ''" @click="compareChartType = 'bar'">柱状图</el-button>
                <el-button size="mini" :type="compareChartType === 'radar' ? 'primary' : ''" @click="compareChartType = 'radar'">雷达图</el-button>
              </el-button-group>
            </div>
            <div class="chart-content" ref="compareChart" style="height: 400px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分析数据表格 -->
    <div class="analysis-table">
      <div class="table-header">
        <h4>详细分析数据</h4>
        <el-button-group>
          <el-button size="mini" :type="tableView === 'summary' ? 'primary' : ''" @click="tableView = 'summary'">汇总视图</el-button>
          <el-button size="mini" :type="tableView === 'detail' ? 'primary' : ''" @click="tableView = 'detail'">明细视图</el-button>
        </el-button-group>
      </div>
      
      <!-- 汇总视图 -->
      <el-table v-if="tableView === 'summary'" :data="summaryData" border stripe>
        <el-table-column prop="centerName" label="成本中心" min-width="180" />
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="variance" label="差异金额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.variance >= 0 ? 'positive-variance' : 'negative-variance'">
              {{ formatAmount(Math.abs(scope.row.variance)) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceRate" label="差异率" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVarianceRateTag(scope.row.varianceRate)">
              {{ scope.row.varianceRate }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="efficiency" label="效率指数" width="100" align="center">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.efficiency"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewDetail(scope.row)">查看明细</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 明细视图 -->
      <el-table v-if="tableView === 'detail'" :data="detailData" border stripe>
        <el-table-column prop="period" label="期间" width="100" />
        <el-table-column prop="centerName" label="成本中心" min-width="150" />
        <el-table-column prop="costType" label="成本类型" width="120" />
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="proportion" label="占比" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.proportion }}%
          </template>
        </el-table-column>
        <el-table-column prop="growthRate" label="增长率" width="100" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.growthRate >= 0 ? '#F56C6C' : '#67C23A' }">
              {{ scope.row.growthRate >= 0 ? '+' : '' }}{{ scope.row.growthRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container" v-if="tableView === 'detail'">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>
  </div>
</template>

<script>
import {
  getCostAnalysisData,
  getCostStructureAnalysis,
  getCostTrendAnalysis,
  getCostCompareAnalysis,
  exportCostAnalysisReport,
  getCostVarianceAnalysis
} from '@/api/financialSharing/costCenter'
import * as echarts from 'echarts'

export default {
  name: 'CostAnalysis',
  data() {
    return {
      analysisForm: {
        analysisPeriod: [],
        costCenterIds: [],
        analysisDimension: 'center'
      },
      overview: {
        totalCost: 0,
        totalCostTrend: 0,
        avgCost: 0,
        costVariance: 0,
        efficiencyIndex: 0
      },
      structureChartType: 'pie',
      trendChartType: 'line',
      compareChartType: 'bar',
      tableView: 'summary',
      summaryData: [],
      detailData: [],
      // ECharts 实例缓存，避免每次 setOption 都 init
      _structureChart: null,
      _trendChart: null,
      _compareChart: null,
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  mounted() {
    // 数据先到再画图，避免 ref DOM 还未挂载就 init
    this.$nextTick(() => {
      this.loadAnalysisData()
      this.initCharts()
    })
    // 容器尺寸变化时重绘，避免 echarts 在初始 0 宽度容器里画完看不到
    this._onResize = () => {
      ;[this._structureChart, this._trendChart, this._compareChart].forEach(ins => {
        if (ins && typeof ins.resize === 'function') ins.resize()
      })
    }
    window.addEventListener('resize', this._onResize)
  },
  beforeDestroy() {
    if (this._onResize) {
      window.removeEventListener('resize', this._onResize)
    }
    // ECharts 实例销毁，防止内存泄漏
    [this._structureChart, this._trendChart, this._compareChart].forEach(ins => {
      if (ins && typeof ins.dispose === 'function') ins.dispose()
    })
  },
  watch: {
    structureChartType() { this.initCharts() },
    trendChartType() { this.initCharts() },
    compareChartType() { this.renderCompareChart(this.summaryData) }
  },
  methods: {
    /**
     * 把 Map key 统一成小驼峰。
     * 兼容达梦 + MyBatis Map 类型返回的 3 种形态：
     *   - SNAKE_CASE  → camelCase（COST_CATEGORY → costCategory）
     *   - ALLCAPS     → lowercase（NAME → name / VALUE → value）
     *   - camelCase   → 原样保留
     */
    normalizeKeys(row) {
      if (!row || typeof row !== 'object') return row
      const out = {}
      Object.keys(row).forEach(key => {
        let camel = key
        if (key.includes('_')) {
          camel = key.toLowerCase().replace(/_([a-z0-9])/g, (_, c) => c.toUpperCase())
        } else if (key === key.toUpperCase() && key !== key.toLowerCase()) {
          // 全大写无下划线（NAME / VALUE / ID）→ 全小写
          camel = key.toLowerCase()
        }
        out[camel] = row[key]
      })
      return out
    },
    buildAnalysisParams(extra = {}) {
      const params = {
        analysisDimension: this.analysisForm.analysisDimension,
        costCenterIds: (this.analysisForm.costCenterIds || []).join(','),
        ...extra
      }
      const range = this.analysisForm.analysisPeriod
      if (range && range.length === 2) {
        const fmt = d => {
          if (!d) return ''
          if (typeof d === 'string') return d.slice(0, 7)
          // Date 对象 → YYYY-MM
          const y = d.getFullYear()
          const m = String(d.getMonth() + 1).padStart(2, '0')
          return `${y}-${m}`
        }
        params.startPeriod = fmt(range[0])
        params.endPeriod = fmt(range[1])
      }
      return params
    },
    async loadAnalysisData() {
      try {
        const params = this.buildAnalysisParams({
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        })
        const res = await getCostAnalysisData(params)
        if (res.code === 1) {
          const data = res.data || {}
          // 概览：兼容字段缺失，保底默认 0
          const ov = this.normalizeKeys(data.overview || {})
          this.overview = {
            totalCost: ov.totalCost || 0,
            totalCostTrend: ov.totalCostTrend || 0,
            avgCost: ov.avgCost || 0,
            costVariance: ov.costVariance || 0,
            efficiencyIndex: ov.efficiencyIndex || 0
          }
          this.summaryData = (data.summaryList || []).map(this.normalizeKeys)
          this.detailData = (data.detailList || []).map(this.normalizeKeys)
          this.pagination.total = data.total || 0

          // 列表数据回来后，重画对比图（依赖 summaryData）
          this.renderCompareChart(this.summaryData)
        } else {
          this.$message.error(res.msg || '加载分析数据失败')
        }
      } catch (error) {
        console.error('加载分析数据失败:', error)
        this.$message.error('加载分析数据失败，请稍后重试')
      }
    },
    async initCharts() {
      try {
        const params = this.buildAnalysisParams()
        // 趋势 + 结构并行拉
        const [trendRes, structureRes] = await Promise.all([
          getCostTrendAnalysis(params),
          getCostStructureAnalysis(params)
        ])
        if (trendRes && trendRes.code === 1) {
          this.renderTrendChart(trendRes.data)
        }
        if (structureRes && structureRes.code === 1) {
          this.renderStructureChart(structureRes.data)
        }
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },
    /** 安全获取 echarts 工厂；优先 import 的 echarts，再退到全局 / Vue.prototype */
    getEcharts() {
      if (echarts) return echarts
      if (this.$echarts) return this.$echarts
      if (typeof window !== 'undefined' && window.echarts) return window.echarts
      return null
    },
    renderStructureChart(data) {
      const dom = this.$refs.structureChart
      const echartsLib = this.getEcharts()
      if (!dom || !echartsLib) return
      // 后端返回 { items: [{name, value}, ...] }，老兼容也支持顶层 list
      const rawItems = (data && (data.items || data)) || []
      const items = (Array.isArray(rawItems) ? rawItems : []).map(this.normalizeKeys)
      const seriesData = items.map(it => ({
        name: it.name || it.costType || it.category || '未分类',
        value: Number(it.value || it.amount || 0)
      }))
      if (!this._structureChart || this._structureChart.isDisposed()) {
        this._structureChart = echartsLib.init(dom)
      }
      const option = this.structureChartType === 'pie'
        ? {
          tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
          legend: { bottom: 0 },
          series: [{ type: 'pie', radius: ['35%', '60%'], data: seriesData }]
        }
        : {
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: seriesData.map(d => d.name), axisLabel: { rotate: 30 } },
          yAxis: { type: 'value' },
          series: [{ type: 'bar', data: seriesData.map(d => d.value), barMaxWidth: 40 }]
        }
      this._structureChart.setOption(option, true)
      this.$nextTick(() => this._structureChart.resize())
    },
    renderTrendChart(data) {
      const dom = this.$refs.trendChart
      const echartsLib = this.getEcharts()
      if (!dom || !echartsLib) return
      // 后端返回的是 list（按 period 分组），每行 { period, actualAmount, budgetAmount }
      const items = (Array.isArray(data) ? data : (data && data.items) || [])
        .map(this.normalizeKeys)
      const periods = items.map(it => it.period || '')
      const actuals = items.map(it => Number(it.actualAmount || it.amount || 0))
      const budgets = items.map(it => Number(it.budgetAmount || 0))

      if (!this._trendChart || this._trendChart.isDisposed()) {
        this._trendChart = echartsLib.init(dom)
      }
      const isArea = this.trendChartType === 'area'
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['实际成本', '预算金额'], bottom: 0 },
        xAxis: { type: 'category', data: periods },
        yAxis: { type: 'value' },
        series: [
          {
            name: '实际成本',
            type: 'line',
            data: actuals,
            smooth: true,
            areaStyle: isArea ? {} : null,
            itemStyle: { color: '#F56C6C' }
          },
          {
            name: '预算金额',
            type: 'line',
            data: budgets,
            smooth: true,
            areaStyle: isArea ? { opacity: 0.2 } : null,
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      this._trendChart.setOption(option, true)
      this.$nextTick(() => this._trendChart.resize())
    },
    renderCompareChart(rows) {
      const dom = this.$refs.compareChart
      const echartsLib = this.getEcharts()
      if (!dom || !echartsLib) return
      const list = (rows || []).slice(0, 12) // 太多中心图会挤
      if (!this._compareChart || this._compareChart.isDisposed()) {
        this._compareChart = echartsLib.init(dom)
      }
      if (this.compareChartType === 'radar') {
        const indicator = list.map(r => ({
          name: r.centerName || '-',
          max: Math.max(Number(r.budgetAmount || 0), Number(r.actualAmount || 0), 1) * 1.2
        }))
        const option = {
          tooltip: {},
          legend: { data: ['预算金额', '实际金额'], bottom: 0 },
          radar: { indicator },
          series: [{
            type: 'radar',
            data: [
              { name: '预算金额', value: list.map(r => Number(r.budgetAmount || 0)) },
              { name: '实际金额', value: list.map(r => Number(r.actualAmount || 0)) }
            ]
          }]
        }
        this._compareChart.setOption(option, true)
      } else {
        const option = {
          tooltip: { trigger: 'axis' },
          legend: { data: ['预算金额', '实际金额'], bottom: 0 },
          xAxis: { type: 'category', data: list.map(r => r.centerName || '-'), axisLabel: { rotate: 25 } },
          yAxis: { type: 'value' },
          series: [
            { name: '预算金额', type: 'bar', data: list.map(r => Number(r.budgetAmount || 0)), itemStyle: { color: '#409EFF' } },
            { name: '实际金额', type: 'bar', data: list.map(r => Number(r.actualAmount || 0)), itemStyle: { color: '#E6A23C' } }
          ]
        }
        this._compareChart.setOption(option, true)
      }
      this.$nextTick(() => this._compareChart.resize())
    },
    handleAnalysis() {
      this.pagination.currentPage = 1
      this.loadAnalysisData()
      this.initCharts()
    },
    handleReset() {
      this.$refs.analysisForm.resetFields()
      this.analysisForm.costCenterIds = []
      this.analysisForm.analysisPeriod = []
      this.analysisForm.analysisDimension = 'center'
      this.pagination.currentPage = 1
      this.loadAnalysisData()
      this.initCharts()
    },
    async handleExport() {
      try {
        const params = this.buildAnalysisParams()
        const res = await exportCostAnalysisReport(params)
        // 后端当前实现为 JSON 占位（{exportId, fileName, status}），不是真二进制 Excel。
        // 兼容判断：如果返回是 Blob → 下载；如果是 JSON → 提示用户。
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res)
          const link = document.createElement('a')
          link.href = url
          link.download = `成本分析报告_${new Date().toISOString().slice(0, 10)}.xlsx`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else if (res && (res.code === 1 || res.status === 'completed')) {
          // 后端占位返回，告知用户导出任务已下发
          this.$message.success(res.msg || '导出任务已下发，请稍后查看')
        } else {
          this.$message.warning((res && res.msg) || '导出未完成')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    handleViewDetail(row) {
      const r = this.normalizeKeys(row || {})
      const safe = (v, def = '-') => (v === undefined || v === null || v === '' ? def : v)
      this.$alert(
        `<div style="line-height:2">
          <p><b>成本中心：</b>${safe(r.centerName)}</p>
          <p><b>预算金额：</b>${this.formatAmount(r.budgetAmount || 0)}</p>
          <p><b>实际金额：</b>${this.formatAmount(r.actualAmount || 0)}</p>
          <p><b>差异金额：</b>${this.formatAmount(r.variance || 0)}</p>
          <p><b>差异率：</b>${safe(r.varianceRate, 0)}%</p>
          <p><b>效率指数：</b>${safe(r.efficiency, 0)} / 5</p>
        </div>`,
        '成本明细',
        { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
      )
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadAnalysisData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadAnalysisData()
    },
    formatAmount(amount) {
      const n = Number(amount)
      if (!isFinite(n) || n === 0) return '0.00万'
      return (n / 10000).toFixed(2) + '万'
    },
    getVarianceRateTag(rate) {
      const n = Number(rate || 0)
      if (n < -10) return 'success'
      if (n < 0) return 'primary'
      if (n < 10) return 'warning'
      return 'danger'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-analysis-container {
  padding: 20px;
}

.analysis-conditions {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.analysis-overview {
  margin-bottom: 20px;
  
  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    
    .card-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
    }
    
    .card-content {
      .card-title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }
      
      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .card-trend {
        font-size: 12px;
        color: #C0C4CC;
        
        i {
          margin-right: 4px;
        }
      }
    }
    
    &.total-cost .card-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    &.avg-cost .card-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    
    &.cost-variance .card-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
    
    &.efficiency-index .card-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
  }
}

.analysis-charts {
  margin-bottom: 20px;

  .chart-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h4 {
        margin: 0;
        color: #303133;
        font-size: 16px;
      }
    }

    .chart-content {
      width: 100%;
    }
  }
}

.analysis-table {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h4 {
      margin: 0;
      color: #303133;
      font-size: 16px;
    }
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.positive-variance {
  color: #F56C6C;
  font-weight: 600;
}

.negative-variance {
  color: #67C23A;
  font-weight: 600;
}
</style>
