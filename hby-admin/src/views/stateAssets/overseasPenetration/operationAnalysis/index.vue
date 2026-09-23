<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-data-analysis"></i><span>境外经营分析</span></div>
      <div class="page-header-desc">分析境外企业营收利润、经营状况与盈亏对比</div>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="6" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" :body-style="{ padding: '16px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }"><i :class="card.icon" :style="{ color: card.color }"></i></div>
            <div class="stat-info"><div class="stat-value" :style="card.danger ? 'color:#F5222D' : ''">{{ card.value }}</div><div class="stat-label">{{ card.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="14">
        <el-card shadow="hover"><div slot="header"><span>营收利润趋势</span></div><div ref="trendChart" class="chart-box"></div></el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover"><div slot="header"><span>盈亏企业分布</span></div><div ref="pieChart" class="chart-box"></div></el-card>
      </el-col>
    </el-row>

    <el-card class="search-card" shadow="never" style="margin-bottom: 10px">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.unitName" placeholder="请输入" clearable style="width: 180px" /></el-form-item>
        <el-form-item label="所在国家"><el-input v-model="queryForm.country" placeholder="请输入" clearable style="width: 150px" /></el-form-item>
        <el-form-item label="经营状态"><el-select v-model="queryForm.operationStatus" placeholder="全部" clearable style="width: 120px"><el-option label="正常" value="NORMAL" /><el-option label="预警" value="WARNING" /><el-option label="异常" value="ABNORMAL" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="企业名称" prop="unitName" min-width="140" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="100" align="center" />
        <el-table-column label="营业收入(万元)" prop="revenue" width="130" align="right" />
        <el-table-column label="净利润(万元)" prop="netProfit" width="120" align="right">
          <template slot-scope="scope"><span :style="{ color: scope.row.netProfit < 0 ? '#F5222D' : '#52C41A', fontWeight: 'bold' }">{{ scope.row.netProfit }}</span></template>
        </el-table-column>
        <el-table-column label="资产总额(万元)" prop="totalAssets" width="130" align="right" />
        <el-table-column label="员工人数" prop="employeeCount" width="80" align="center" />
        <el-table-column label="经营状态" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="scope.row.operationStatus === 'NORMAL' ? 'success' : scope.row.operationStatus === 'WARNING' ? 'warning' : 'danger'" size="small">{{ { NORMAL: '正常', WARNING: '预警', ABNORMAL: '异常' }[scope.row.operationStatus] || scope.row.operationStatus }}</el-tag></template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getOverseasOperationList, getOverseasOperationStats } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasOperationAnalysis',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, list: [], total: 0, charts: [],
      queryForm: { pageNumber: 1, pageSize: 10, unitName: '', country: '', operationStatus: '' },
      statCards: [
        { label: '境外营收(亿元)', value: '-', icon: 'el-icon-coin', color: null, bg: null, useBright: true },
        { label: '境外利润(亿元)', value: '-', icon: 'el-icon-top', color: '#52C41A', bg: '#F6FFED' },
        { label: '盈利企业占比', value: '-', icon: 'el-icon-pie-chart', color: '#FA8C16', bg: '#FFF7E6' },
        { label: '亏损企业数', value: '-', icon: 'el-icon-bottom', color: '#F5222D', bg: '#FFF1F0', danger: true }
      ]
    }
  },
  mounted() { this.loadData(); window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  created() { this.fetchData() },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async loadData() {
      try {
        const res = await getOverseasOperationStats()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.revenue != null) this.statCards[0].value = d.revenue
          if (d.profit != null) this.statCards[1].value = d.profit
          if (d.profitRatio != null) this.statCards[2].value = d.profitRatio + '%'
          if (d.lossCount != null) this.statCards[3].value = d.lossCount
        }
      } catch (e) { console.warn('经营统计数据加载失败', e) }
      this.$nextTick(() => { this.initTrend(); this.initPie() })
    },
    async initTrend() {
      const c = echarts.init(this.$refs.trendChart); this.charts.push(c)
      try {
        const res = await getOverseasOperationStats()
        if (res && res.result === 200 && res.data && res.data.trendData) {
          const d = res.data.trendData
          c.setOption({
            tooltip: { trigger: 'axis' }, legend: { data: ['境外营收', '境外利润', '营收增长率'] },
            grid: { left: 50, right: 50, bottom: 30, top: 40 },
            xAxis: { type: 'category', data: d.years || [] },
            yAxis: [{ type: 'value', name: '金额(亿元)' }, { type: 'value', name: '增长率(%)', max: 25 }],
            series: [
              { name: '境外营收', type: 'bar', data: d.revenue || [], itemStyle: { color: this.ipBright } },
              { name: '境外利润', type: 'bar', data: d.profit || [], itemStyle: { color: '#52C41A' } },
              { name: '营收增长率', type: 'line', yAxisIndex: 1, data: d.growthRate || [], smooth: true, itemStyle: { color: '#FA8C16' } }
            ]
          })
          return
        }
      } catch (e) { console.warn('趋势图加载失败', e) }
      c.setOption({
        tooltip: { trigger: 'axis' }, legend: { data: ['境外营收', '境外利润', '营收增长率'] },
        grid: { left: 50, right: 50, bottom: 30, top: 40 },
        xAxis: { type: 'category', data: [] }, yAxis: [{ type: 'value', name: '金额(亿元)' }, { type: 'value', name: '增长率(%)' }],
        series: [
          { name: '境外营收', type: 'bar', data: [] },
          { name: '境外利润', type: 'bar', data: [] },
          { name: '营收增长率', type: 'line', yAxisIndex: 1, data: [] }
        ]
      })
    },
    async initPie() {
      const c = echarts.init(this.$refs.pieChart); this.charts.push(c)
      try {
        const res = await getOverseasOperationStats()
        if (res && res.result === 200 && res.data && res.data.pieData) {
          c.setOption({
            tooltip: { trigger: 'item' }, legend: { bottom: 0 },
            series: [{ type: 'pie', radius: ['40%', '70%'], center: ['50%', '45%'], label: { formatter: '{b}\n{d}%' }, data: res.data.pieData }]
          })
          return
        }
      } catch (e) { console.warn('饼图加载失败', e) }
      c.setOption({
        tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['40%', '70%'], data: [] }]
      })
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasOperationList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
.chart-box { height: 300px; width: 100%; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-card { border-radius: 6px; }
.search-card .el-form-item { margin-bottom: 0; }
</style>
