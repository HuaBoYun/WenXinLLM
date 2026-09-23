<template>
  <div class="app-container overseas-page" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-money"></i><span>外汇风险分析</span></div>
      <div class="page-header-desc">监控外汇敞口、套保比例与汇兑损益风险，实现多币种穿透分析</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom: 14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-money" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info">
              <div class="kpi-value">{{ kpiData.forexExposure }}<span class="kpi-unit">亿$</span></div>
              <div class="kpi-label">外汇敞口总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-s-flag" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#52C41A">{{ kpiData.hedgeRatio }}<span class="kpi-unit">%</span></div>
              <div class="kpi-label">平均套保比例</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF1F0"><i class="el-icon-data-line" style="color:#F5222D;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#F5222D">{{ kpiData.exchangeGainLoss }}<span class="kpi-unit">万元</span></div>
              <div class="kpi-label">汇兑损益合计</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-coin" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#FA8C16">{{ kpiData.currencyCount }}<span class="kpi-unit">种</span></div>
              <div class="kpi-label">涉及币种数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="14" style="margin-bottom: 14px">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="chart-header"><i class="el-icon-pie-chart" :style="{ color: ipSecondary, marginRight: '6px' }"></i>外汇敞口币种分布</div>
          <div ref="forexRoseChart" style="height:300px;width:100%"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="chart-header"><i class="el-icon-data-line" :style="{ color: ipSecondary, marginRight: '6px' }"></i>汇率波动趋势（对人民币）</div>
          <div ref="rateLineChart" style="height:300px;width:100%"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询+明细表 -->
    <el-card shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small" style="margin-bottom:4px">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.unitName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="queryForm.currency" placeholder="全部" clearable style="width:110px">
            <el-option label="美元(USD)" value="USD" />
            <el-option label="欧元(EUR)" value="EUR" />
            <el-option label="英镑(GBP)" value="GBP" />
            <el-option label="新元(SGD)" value="SGD" />
            <el-option label="雷亚尔(BRL)" value="BRL" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险评级">
          <el-select v-model="queryForm.riskLevel" placeholder="全部" clearable style="width:110px">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="loading"
        :data="filteredList"
        border
        style="width:100%"
        :row-class-name="tableRowClass"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="币种" prop="currency" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="info">{{ { USD: '美元', EUR: '欧元', GBP: '英镑', SGD: '新元', BRL: '雷亚尔' }[scope.row.currency] || scope.row.currency }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="外汇敞口(万)" prop="exposureAmount" width="130" align="right" />
        <el-table-column label="套保金额(万)" prop="hedgeAmount" width="130" align="right" />
        <el-table-column label="套保比例(%)" prop="hedgeRatio" width="110" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.hedgeRatio < 50 ? '#FA8C16' : '#52C41A', fontWeight: 'bold' }">
              {{ scope.row.hedgeRatio }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="汇兑损益(万元)" prop="impactAmount" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.impactAmount < 0 ? '#F5222D' : '#52C41A', fontWeight: 'bold' }">
              {{ scope.row.impactAmount > 0 ? '+' : '' }}{{ scope.row.impactAmount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="风险评级" prop="riskLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="small">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[scope.row.riskLevel] || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" prop="updateTime" width="120" align="center" />
      </el-table>
      <el-pagination
        background
        style="margin-top:15px;text-align:right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; queryForm.pageNumber = 1 }"
        @current-change="val => { queryForm.pageNumber = val }"
      />
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getOverseasForexList, getOverseasForexStats } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'OverseasForexAnalysis',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, unitName: '', currency: '', riskLevel: '' },
      charts: [],
      kpiData: { forexExposure: '-', hedgeRatio: '-', exchangeGainLoss: '-', currencyCount: '-' }
    }
  },
  computed: {
    filteredList() {
      let data = this.list
      if (this.queryForm.unitName) data = data.filter(r => (r.companyName || '').includes(this.queryForm.unitName))
      if (this.queryForm.currency) data = data.filter(r => r.currency === this.queryForm.currency)
      if (this.queryForm.riskLevel) data = data.filter(r => r.riskLevel === this.queryForm.riskLevel)
      this.total = data.length
      const s = (this.queryForm.pageNumber - 1) * this.queryForm.pageSize
      return data.slice(s, s + this.queryForm.pageSize)
    }
  },
  created() { this.loadData() },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c && c.dispose())
  },
  methods: {
    async loadData() {
      await this.fetchData()
      try {
        const res = await getOverseasForexStats()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.forexExposure != null) this.kpiData.forexExposure = d.forexExposure
          if (d.hedgeRatio != null) this.kpiData.hedgeRatio = d.hedgeRatio
          if (d.exchangeGainLoss != null) this.kpiData.exchangeGainLoss = d.exchangeGainLoss
          if (d.currencyCount != null) this.kpiData.currencyCount = d.currencyCount
        }
      } catch (e) { console.warn('外汇统计数据加载失败', e) }
      this.$nextTick(() => { this.initRoseChart(); this.initRateLineChart() })
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasForexList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, unitName: '', currency: '', riskLevel: '' }
      this.$nextTick(() => { this.$refs.queryForm && this.$refs.queryForm.clearValidate() })
      this.fetchData()
    },
    tableRowClass({ row }) {
      return row.hedgeRatio < 50 ? 'row-low-hedge' : ''
    },
    handleResize() { this.charts.forEach(c => c && c.resize()) },
    async initRoseChart() {
      if (!this.$refs.forexRoseChart) return
      const c = echarts.init(this.$refs.forexRoseChart)
      this.charts.push(c)
      try {
        const res = await getOverseasForexStats()
        if (res && res.result === 200 && res.data && res.data.roseData) {
          const currencyMap = { USD: '美元', EUR: '欧元', GBP: '英镑', SGD: '新元', BRL: '雷亚尔' }
          c.setOption({
            tooltip: { trigger: 'item', formatter: '{b}: {c}万 ({d}%)' },
            legend: { bottom: 0 },
            series: [{
              type: 'pie', radius: ['30%', '65%'], center: ['50%', '45%'],
              roseType: 'radius', label: { formatter: '{b}\n{c}万' },
              data: (res.data.roseData || []).map(item => ({ ...item, name: currencyMap[item.name] || item.name }))
            }]
          })
          return
        }
      } catch (e) { console.warn('外汇故现图加载失败', e) }
      c.setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['30%', '65%'], data: [] }]
      })
    },
    async initRateLineChart() {
      if (!this.$refs.rateLineChart) return
      const c = echarts.init(this.$refs.rateLineChart)
      this.charts.push(c)
      try {
        const res = await getOverseasForexStats()
        if (res && res.result === 200 && res.data && res.data.rateData) {
          const d = res.data.rateData
          const rateMap = { 'USD/CNY': '美元/人民币', 'EUR/CNY': '欧元/人民币', 'GBP/CNY': '英镑/人民币' }
          c.setOption({
            tooltip: { trigger: 'axis' },
            legend: { data: (d.legends || ['USD/CNY', 'EUR/CNY', 'GBP/CNY']).map(l => rateMap[l] || l), top: 5 },
            grid: { left: 50, right: 20, bottom: 30, top: 45 },
            xAxis: { type: 'category', data: d.months || [], boundaryGap: false },
            yAxis: { type: 'value', name: '汇率', scale: true },
            series: (d.series || []).map(s => ({
              name: rateMap[s.name] || s.name, type: 'line', smooth: true, data: s.data,
              itemStyle: { color: s.color || this.ipBright }, lineStyle: { width: 2 }
            }))
          })
          return
        }
      } catch (e) { console.warn('汇率趋势图加载失败', e) }
      c.setOption({
        tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: [] }, yAxis: { type: 'value' }, series: []
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
  padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px;
  color: #fff;
}
.page-header-left {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  i { font-size: 22px; margin-right: 10px; }
}
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card {
  display: flex;
  align-items: center;
}
.kpi-icon-wrap {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
  flex-shrink: 0;
}
.kpi-info { flex: 1; }
.kpi-value {
  font-size: 26px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}
.kpi-unit { font-size: 13px; color: #909399; margin-left: 3px; }
.kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.chart-header { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .row-low-hedge td { background: #FFF7E6 !important; }
</style>
