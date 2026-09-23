<template>
  <div class="sal-exec">
    <!-- 深紫色Banner -->
    <div class="sal-banner-purple" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">高管薪酬监控</div>
      <div class="banner-sub">国有企业负责人薪酬合规监控 · "限高令"自动核验 · 薪酬结构分析</div>
      <div class="rule-box">
        <span class="rule-item">最高薪酬 ≤ 央企均值 × <b>8倍</b></span>
        <span class="rule-item">绩效薪酬 ≤ 基本薪酬 × <b>2倍</b></span>
        <span class="rule-item">任期激励 ≤ 年薪总水平 × <b>30%</b></span>
      </div>
    </div>

    <!-- KPI卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card"><div class="kpi-label">{{ k.label }}</div><div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div></div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业">
          <el-select v-model="queryForm.companyName" clearable placeholder="全部企业" style="width:140px">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="queryForm.companyName = ''; loadData()">重置</el-button>
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">高管薪酬构成分布（环形图）</span></div>
          <div ref="pieChart" style="height:240px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">高管薪酬企业横向对比</span><span class="chart-tip">红色虚线 = 8倍上限</span></div>
          <div ref="barChart" style="height:240px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 高管明细台账 -->
    <el-card shadow="never" class="table-card">
      <div slot="header">
        <span class="card-title">高管薪酬明细台账</span>
        <div style="float:right">
          <el-select v-model="filterCompany" clearable placeholder="按企业筛选" size="small" style="width:140px">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </div>
      </div>
      <el-table :data="filteredList" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="name" label="姓名" width="80" align="center"></el-table-column>
        <el-table-column prop="position" label="职务" width="90" align="center"></el-table-column>
        <el-table-column prop="company" label="所属企业" width="130"></el-table-column>
        <el-table-column prop="baseSalary" label="基本年薪（万）" align="right">
          <template slot-scope="{ row }">{{ row.baseSalary }}</template>
        </el-table-column>
        <el-table-column prop="perfSalary" label="绩效年薪（万）" align="right">
          <template slot-scope="{ row }">
            <span :style="{ color: row.perfSalary > row.baseSalary * 2 ? '#F5222D' : 'inherit', fontWeight: row.perfSalary > row.baseSalary * 2 ? 700 : 400 }">
              {{ row.perfSalary }}
              <el-tag v-if="row.perfSalary > row.baseSalary * 2" type="danger" size="mini">超限</el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="termIncentive" label="任期激励（万）" align="right">
          <template slot-scope="{ row }">
            <span :style="{ color: row.termIncentive > row.totalSalary * 0.3 ? '#FA8C16' : 'inherit' }">{{ row.termIncentive }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalSalary" label="合计（万）" align="right">
          <template slot-scope="{ row }">
            <span :style="{ color: !row.compliant ? '#F5222D' : 'inherit', fontWeight: !row.compliant ? 700 : 400 }">{{ row.totalSalary }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="salaryMultiple" label="与均值倍数" align="center">
          <template slot-scope="{ row }">
            <span :style="{ color: row.salaryMultiple > 8 ? '#F5222D' : row.salaryMultiple > 6 ? '#FA8C16' : '#52C41A', fontWeight: row.salaryMultiple > 8 ? 700 : 400 }">
              {{ row.salaryMultiple }}倍
            </span>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" align="center" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="row.compliant ? 'success' : 'danger'" size="mini">{{ row.compliant ? '合规' : '超限' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="高管薪酬详情" :visible.sync="drawerVisible" size="460px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <div class="exec-header">
          <div class="exec-avatar">{{ currentRow.name[0] }}</div>
          <div>
            <div class="exec-name">{{ currentRow.name }}</div>
            <div class="exec-pos">{{ currentRow.position }} · {{ currentRow.company }}</div>
          </div>
        </div>
        <el-divider></el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="基本年薪">{{ currentRow.baseSalary }}万元</el-descriptions-item>
          <el-descriptions-item label="绩效年薪">{{ currentRow.perfSalary }}万元</el-descriptions-item>
          <el-descriptions-item label="任期激励">{{ currentRow.termIncentive }}万元</el-descriptions-item>
          <el-descriptions-item label="薪酬合计">
            <span :style="{ color: !currentRow.compliant ? '#F5222D' : '#52C41A', fontWeight: 700 }">{{ currentRow.totalSalary }}万元</span>
          </el-descriptions-item>
          <el-descriptions-item label="央企均值倍数" :span="2">
            <span :style="{ color: currentRow.salaryMultiple > 8 ? '#F5222D' : '#52C41A', fontWeight: 700 }">{{ currentRow.salaryMultiple }}倍</span>
            <el-tag :type="currentRow.salaryMultiple > 8 ? 'danger' : 'success'" size="mini" style="margin-left:8px">
              {{ currentRow.salaryMultiple > 8 ? '超8倍上限' : '合规' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <div class="compliance-result" :class="currentRow.compliant ? 'result-ok' : 'result-fail'">
          <i :class="currentRow.compliant ? 'el-icon-circle-check' : 'el-icon-circle-close'"></i>
          {{ currentRow.compliant ? '薪酬水平符合"限高令"规定，合规' : `薪酬超过央企均值8倍上限，超出${(currentRow.salaryMultiple - 8).toFixed(1)}倍，需整改` }}
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getExecutivePayList, exportExecutivePay } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'SalaryExecutivePay',
  data() {
    return {
      tableData: [],
      queryForm: { companyName: '' },
      filterCompany: '',
      drawerVisible: false,
      currentRow: null,
      charts: {},
      companyOptions: [],
    }
  },
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
    filteredList() {
      return this.filterCompany ? this.tableData.filter(r => r.company === this.filterCompany) : this.tableData
    },
    kpiCards() {
      const total = this.tableData.length
      const compliant = this.tableData.filter(r => r.isCompliant === '1').length
      const over = total - compliant
      return [
        { key: 'total', label: '监控高管人数', value: total + '人', color: '#1677FF' },
        { key: 'compliant', label: '薪酬合规人数', value: compliant + '人', color: '#52C41A' },
        { key: 'over', label: '薪酬超标人数', value: over + '人', color: over > 0 ? '#F5222D' : '#52C41A' },
        { key: 'rate', label: '合规率', value: total > 0 ? ((compliant / total) * 100).toFixed(1) + '%' : '0%', color: total > 0 && (compliant / total) < 0.9 ? '#FA8C16' : '#52C41A' },
      ]
    },
  },
  mounted() {
    this.loadData()
    this.$nextTick(() => {
      this.initPieChart()
      this.initBarChart()
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    async loadData() {
      try {
        const params = { pageSize: 100 }
        if (this.queryForm.companyName) {
          params.companyName = this.queryForm.companyName
        }
        const res = await getExecutivePayList(params)
        if (res.data && res.data.tlist) {
          this.tableData = res.data.tlist.map(r => ({
            ...r,
            name: r.execName || r.name,
            position: r.execPosition || r.position,
            company: r.companyName || r.company,
            baseSalary: r.baseSalary || 0,
            perfSalary: r.perfSalary || 0,
            termIncentive: r.termIncentive || 0,
            totalSalary: r.totalComp || r.totalSalary || 0,
            salaryMultiple: r.ratioToAvg || r.salaryMultiple || 0,
            compliant: r.isCompliant === '1' || r.isCompliant === true,
          }))
          const companies = [...new Set(this.tableData.map(r => r.company).filter(Boolean))]
          if (companies.length) this.companyOptions = companies
        }
      } catch (e) {
        console.warn('加载高管薪酬数据失败', e)
      }
      this.$nextTick(() => {
        this.initPieChart()
        this.initBarChart()
      })
    },
    rowClass({ row }) {
      return !row.compliant ? 'row-over' : ''
    },
    viewDetail(row) {
      this.currentRow = row
      this.drawerVisible = true
    },
    initPieChart() {
      const ec = echarts
      if (!ec || !this.tableData.length) return
      // 单例模式：只初始化一次，后续用 setOption 更新
      let c = this.charts.pie
      if (!c) {
        c = ec.init(this.$refs.pieChart)
        this.charts.pie = c
        window.addEventListener('resize', () => c && c.resize())
      }
      const totalBase = this.tableData.reduce((s, r) => s + (Number(r.baseSalary) || 0), 0)
      const totalPerf = this.tableData.reduce((s, r) => s + (Number(r.perfSalary) || 0), 0)
      const totalTerm = this.tableData.reduce((s, r) => s + (Number(r.termIncentive) || 0), 0)
      c.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{
          type: 'pie', radius: ['40%', '70%'],
          data: [
            { value: totalBase, name: '基本年薪', itemStyle: { color: '#1677FF' } },
            { value: totalPerf, name: '绩效年薪', itemStyle: { color: '#52C41A' } },
            { value: totalTerm, name: '任期激励', itemStyle: { color: '#FA8C16' } },
          ],
          label: { formatter: '{b}\n{d}%' },
        }],
      }, true)
    },
    async handleExport() {
      try {
        const res = await exportExecutivePay({ companyName: this.queryForm.companyName })
        if (res.data && res.data.length) {
          const headers = ['姓名', '职务', '企业', '基本年薪(万)', '绩效年薪(万)', '任期激励(万)', '合计(万)', '均值倍数', '合规状态']
          const rows = res.data.map(r => [
            r.execName, r.execPosition, r.companyName, r.baseSalary, r.perfSalary,
            r.termIncentive, r.totalComp, r.ratioToAvg, r.isCompliant === '1' ? '合规' : '超限'
          ])
          const csv = [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
          const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8;' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = `高管薪酬台账_${new Date().toISOString().slice(0, 10)}.csv`
          link.click()
          this.$message.success('导出成功')
        } else {
          this.$message.warning('暂无数据可导出')
        }
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    initBarChart() {
      const ec = echarts
      if (!ec || !this.tableData.length) return
      // 单例模式：只初始化一次，后续用 setOption 更新
      let c = this.charts.bar
      if (!c) {
        c = ec.init(this.$refs.barChart)
        this.charts.bar = c
        window.addEventListener('resize', () => c && c.resize())
      }
      const names = this.tableData.map(r => (r.name || '') + '\n' + (r.position || '').slice(0, 2))
      const totals = this.tableData.map(r => Number(r.totalSalary) || 0)
      const colors = this.tableData.map(r => !r.compliant ? '#F5222D' : '#1677FF')
      // 从数据计算央企员工平均薪酬：所有高管薪酬总和 / 高管人数 / 均值倍数的平均值
      const validRows = this.tableData.filter(r => r.salaryMultiple > 0 && r.totalSalary > 0)
      let avgSalary = 0
      if (validRows.length > 0) {
        const totalComp = validRows.reduce((s, r) => s + Number(r.totalSalary), 0)
        const avgMultiple = validRows.reduce((s, r) => s + Number(r.salaryMultiple), 0) / validRows.length
        avgSalary = avgMultiple > 0 ? (totalComp / validRows.length / avgMultiple) : 0
      }
      const limitLine = avgSalary * 8
      c.setOption({
        tooltip: { trigger: 'axis', formatter: p => `${this.tableData[p[0].dataIndex].name}: ${p[0].value}万元` },
        xAxis: { type: 'value', name: '薪酬（万元）' },
        yAxis: { type: 'category', data: names },
        series: [
          {
            type: 'bar', data: totals.map((v, i) => ({ value: v, itemStyle: { color: colors[i] } })),
            barWidth: '50%', label: { show: true, position: 'right', formatter: '{c}万' },
          },
          { type: 'line', data: Array(this.tableData.length).fill(limitLine), lineStyle: { color: '#F5222D', type: 'dashed', width: 2 }, symbol: 'none', name: '8倍上限' },
        ],
      }, true)
    },
  },
}
</script>

<style scoped>
.sal-exec { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner-purple {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 12px; }
.rule-box { display: flex; gap: 16px; }
.rule-item { background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3); padding: 4px 12px; border-radius: 4px; font-size: 12px; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 22px; font-weight: 700; }
.chart-row { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.chart-tip { font-size: 11px; color: #8c8c8c; margin-left: 10px; }
.table-card { margin-bottom: 16px; }
::v-deep .row-over td { background: #fff1f0 !important; }
.drawer-content { padding: 20px; }
.exec-header { display: flex; align-items: center; gap: 14px; margin-bottom: 12px; }
.exec-avatar { width: 50px; height: 50px; border-radius: 50%; background: #722ED1; color: #fff; font-size: 20px; font-weight: 700; display: flex; align-items: center; justify-content: center; }
.exec-name { font-size: 18px; font-weight: 700; }
.exec-pos { font-size: 13px; color: #8c8c8c; margin-top: 2px; }
.compliance-result { margin-top: 16px; padding: 12px 16px; border-radius: 6px; font-size: 14px; }
.result-ok { background: #f6ffed; color: #52C41A; border: 1px solid #b7eb8f; }
.result-fail { background: #fff1f0; color: #F5222D; border: 1px solid #ffa39e; font-weight: 600; }
.query-card { margin-bottom: 12px; }
</style>
