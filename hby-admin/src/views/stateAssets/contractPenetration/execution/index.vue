<template>
  <div class="execution-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同履行监控</div>
        <div class="banner-sub">实时监控集团各级企业合同履行状态，精准识别付款超期、交付滞后、违约风险合同</div>
      </div>
      <div class="banner-right">
        <div v-for="item in bannerStats" :key="item.label" class="banner-stat">
          <span class="stat-num">{{ item.value }}</span>
          <span class="stat-label">{{ item.label }}</span>
        </div>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col v-for="card in statCards" :key="card.label" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-card-inner">
            <i :class="card.icon" :style="{color:card.color,fontSize:'28px'}"></i>
            <div class="stat-info">
              <div class="stat-value" :style="{color:card.color}">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <el-form :inline="true" :model="query" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="query.companyName" placeholder="请输入" clearable style="width:140px" prefix-icon="el-icon-office-building"/>
        </el-form-item>
        <el-form-item label="合同编号">
          <el-input v-model="query.contractNo" placeholder="请输入" clearable style="width:140px"/>
        </el-form-item>
        <el-form-item label="合同名称">
          <el-input v-model="query.contractName" placeholder="请输入" clearable style="width:140px"/>
        </el-form-item>
        <el-form-item label="合同类型">
          <el-select v-model="query.contractType" placeholder="全部" clearable style="width:120px">
            <el-option v-for="t in contractTypes" :key="t" :label="t" :value="t"/>
          </el-select>
        </el-form-item>
        <el-form-item label="履行状态">
          <el-select v-model="query.execStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="正常履行" value="NORMAL"/>
            <el-option label="轻微滞后" value="LIGHT_DELAY"/>
            <el-option label="严重滞后" value="SEVERE_DELAY"/>
            <el-option label="已违约" value="BREACH"/>
            <el-option label="已完成" value="DONE"/>
          </el-select>
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker
            v-model="query.expireDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width:240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图表区 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>履行状态分布</span></div>
          <div ref="statusChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>近6月付款进度趋势（万元）</span></div>
          <div ref="payChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 履行明细列表 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <div slot="header" class="card-header">
        <span>合同履行明细</span>
        <el-tag size="small" style="margin-left:8px">共 {{ total }} 条</el-tag>
      </div>
      <el-table
        :data="list"
        :row-class-name="tableRowClass"
        v-loading="loading"
        border size="small" style="width:100%">
        <el-table-column label="合同编号" prop="contractNo" width="160" fixed/>
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip/>
        <el-table-column label="类型" prop="contractType" width="100">
          <template slot-scope="{row}">
            <span class="tag-pill" :style="contractTypeTagStyle(row.contractType)">{{ row.contractType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="企业" prop="companyName" width="130" show-overflow-tooltip/>
        <el-table-column label="合同金额(万)" prop="totalAmount" width="120" align="right">
          <template slot-scope="{row}"><span style="font-weight:600;color:#0050A0">{{ row.totalAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="已付金额(万)" prop="paidAmount" width="120" align="right">
          <template slot-scope="{row}"><span style="color:#52C41A">{{ row.paidAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="付款进度" width="160">
          <template slot-scope="{row}">
            <el-progress
              :percentage="row.totalAmount > 0 ? Math.round(row.paidAmount/row.totalAmount*100) : 0"
              :color="row.execStatus==='BREACH'?'#F5222D':row.execStatus==='SEVERE_DELAY'?'#FA8C16':'#1677FF'"
              :stroke-width="8" style="width:140px"/>
          </template>
        </el-table-column>
        <el-table-column label="计划进度" prop="plannedProgress" width="90" align="center">
          <template slot-scope="{row}">{{ row.plannedProgress }}%</template>
        </el-table-column>
        <el-table-column label="实际进度" prop="actualProgress" width="90" align="center">
          <template slot-scope="{row}">{{ row.actualProgress }}%</template>
        </el-table-column>
        <el-table-column label="到期日期" prop="expireDate" width="110"/>
        <el-table-column label="履行状态" prop="execStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="execStatusType(row.execStatus)" size="mini">{{ execStatusLabel(row.execStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="即将到期" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.expiringSoon" type="warning" size="mini">⚠ 30天</el-tag>
            <span v-else style="color:#ccc">—</span>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px;text-align:right;">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-sizes="[15, 30, 50, 100]"
          :page-size="pageSize"
          :current-page="pageNumber"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 即将到期提醒 -->
    <el-card shadow="never" v-if="expiringList.length">
      <div slot="header" class="card-header">
        <i class="el-icon-bell" style="color:#FA8C16;margin-right:6px"></i>
        <span style="color:#FA8C16">即将到期合同提醒（30天内）</span>
        <el-tag type="warning" size="small" style="margin-left:8px">{{ expiringList.length }} 份</el-tag>
      </div>
      <el-table :data="expiringList" size="small" border>
        <el-table-column label="合同编号" prop="contractNo" width="160"/>
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip/>
        <el-table-column label="企业" prop="companyName" width="140"/>
        <el-table-column label="到期日期" prop="expireDate" width="120"/>
        <el-table-column label="合同金额(万)" prop="totalAmount" width="120" align="right">
          <template slot-scope="{row}"><span style="font-weight:600;color:#FA8C16">{{ row.totalAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="履行状态" prop="execStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="execStatusType(row.execStatus)" size="mini">{{ execStatusLabel(row.execStatus) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getExecutionList } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractExecution',
  data() {
    return {
      list: [],
      total: 0,
      pageNumber: 1,
      pageSize: 15,
      loading: false,
      query: { companyName: '', contractNo: '', contractName: '', contractType: '', execStatus: '', expireDateRange: null },
      contractTypes: ['采购合同', '销售合同', '工程合同', '租赁合同', '金融合同', '劳务合同', '服务合同'],
      statusChart: null,
      payChart: null,
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
    expiringList() { return this.list.filter(r => r.expiringSoon) },
    statCards() {
      const d = this.list
      return [
        { label: '正常履行', value: d.filter(r => r.execStatus === 'NORMAL').length, color: '#52C41A', icon: 'el-icon-success' },
        { label: '轻微滞后', value: d.filter(r => r.execStatus === 'LIGHT_DELAY').length, color: '#FA8C16', icon: 'el-icon-warning' },
        { label: '严重滞后', value: d.filter(r => r.execStatus === 'SEVERE_DELAY').length, color: '#F5222D', icon: 'el-icon-warning-outline' },
        { label: '已违约', value: d.filter(r => r.execStatus === 'BREACH').length, color: '#820014', icon: 'el-icon-close' },
      ]
    },
    bannerStats() {
      const d = this.list
      const totalPaid = (d.reduce((s, r) => s + r.paidAmount, 0) / 10000).toFixed(1)
      return [
        { label: '监控合同总量', value: this.total },
        { label: '已付总额(亿)', value: totalPaid },
        { label: '即将到期', value: this.expiringList.length },
      ]
    },
  },
  mounted() {
    this.fetchData()
    this.$nextTick(() => {
      this.initStatusChart()
      this.initPayChart()
    })
  },
  beforeDestroy() {
    if (this.statusChart) this.statusChart.dispose()
    if (this.payChart) this.payChart.dispose()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        // 构建请求参数，只传非空字段
        const params = {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
        }
        if (this.query.companyName) params.companyName = this.query.companyName
        if (this.query.contractNo) params.contractNo = this.query.contractNo
        if (this.query.contractName) params.contractName = this.query.contractName
        if (this.query.contractType) params.contractType = this.query.contractType
        if (this.query.execStatus) params.execStatus = this.query.execStatus
        if (this.query.expireDateRange && this.query.expireDateRange.length === 2) {
          params.expireDateStart = this.query.expireDateRange[0]
          params.expireDateEnd = this.query.expireDateRange[1]
        }
        const res = await getExecutionList(params)
        if (res && res.result === 200 && res.data) {
          const rawList = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          // 字段映射：后端字段 -> 前端展示字段
          this.list = rawList.map(item => ({
            ...item,
            totalAmount: parseFloat(item.totalAmount || item.amount || item.contractAmount) || 0,
            paidAmount: parseFloat(item.paidAmount) || 0,
            plannedProgress: parseFloat(item.plannedProgress || item.planProgress) || 0,
            actualProgress: parseFloat(item.actualProgress) || 0,
            contractNo: item.contractNo || item.contractCode || '',
            contractName: item.contractName || '',
            contractType: item.contractType || '',
            companyName: item.companyName || '',
            expireDate: item.expireDate || item.expiryDate || '',
            execStatus: item.execStatus || 'NORMAL',
            expiringSoon: !!item.expiringSoon
          }))
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
      this.$nextTick(() => {
        this.initStatusChart()
        this.initPayChart()
      })
    },
    handleSearch() { this.pageNumber = 1; this.fetchData() },
    handleReset() {
      this.query = { companyName: '', contractNo: '', contractName: '', contractType: '', execStatus: '', expireDateRange: null }
      this.pageNumber = 1
      this.fetchData()
    },
    handleSizeChange(val) { this.pageSize = val; this.pageNumber = 1; this.fetchData() },
    handlePageChange(val) { this.pageNumber = val; this.fetchData() },
    execStatusLabel(s) {
      const m = { NORMAL: '正常履行', LIGHT_DELAY: '轻微滞后', SEVERE_DELAY: '严重滞后', BREACH: '已违约', DONE: '已完成' }
      return m[s] || s
    },
    execStatusType(s) {
      const m = { NORMAL: 'success', LIGHT_DELAY: 'warning', SEVERE_DELAY: 'danger', BREACH: 'danger', DONE: 'primary' }
      return m[s] || ''
    },
    tableRowClass({ row }) {
      if (row.execStatus === 'BREACH' || row.execStatus === 'SEVERE_DELAY') return 'row-exec-danger'
      if (row.execStatus === 'LIGHT_DELAY') return 'row-exec-warning'
      if (row.expiringSoon) return 'row-exec-expiring'
      return ''
    },
    contractTypeTagStyle(v) {
      const map = {
        '采购合同': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' },
        '工程合同': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '金融合同': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '服务合同': { background: '#F0FFF4', color: '#389E0D', border: '1px solid #95DE64' },
        '租赁合同': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
      }
      return map[v] || {}
    },
    initStatusChart() {
      const el = this.$refs.statusChart
      if (!el) return
      if (this.statusChart) this.statusChart.dispose()
      this.statusChart = echarts.init(el)
      const statusCounts = {}
      this.list.forEach(r => { if (r.execStatus) statusCounts[r.execStatus] = (statusCounts[r.execStatus] || 0) + 1 })
      const colorMap = { NORMAL: '#52C41A', LIGHT_DELAY: '#FA8C16', SEVERE_DELAY: '#F5222D', BREACH: '#820014', DONE: '#1677FF' }
      const labelMap = { NORMAL: '正常履行', LIGHT_DELAY: '轻微滞后', SEVERE_DELAY: '严重滞后', BREACH: '已违约', DONE: '已完成' }
      const chartData = Object.keys(statusCounts).length > 0
        ? Object.entries(statusCounts).map(([k, v]) => ({ name: labelMap[k] || k, value: v, itemStyle: { color: colorMap[k] || '#ccc' } }))
        : [{ name: '暂无数据', value: 1, itemStyle: { color: '#f0f0f0' } }]
      this.statusChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, type: 'scroll' },
        series: [{
          type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'],
          data: chartData,
          label: { formatter: '{b}\n{d}%', fontSize: 11 },
        }],
      })
    },
    initPayChart() {
      const el = this.$refs.payChart
      if (!el) return
      if (this.payChart) this.payChart.dispose()
      this.payChart = echarts.init(el)
      // 按企业统计已付/待付金额
      const companyMap = {}
      this.list.forEach(r => {
        const name = r.companyName || '其他'
        const total = Number(r.totalAmount) || 0
        const paid = Number(r.paidAmount) || 0
        if (!companyMap[name]) companyMap[name] = { paid: 0, pending: 0 }
        companyMap[name].paid += paid
        companyMap[name].pending += (total - paid)
      })
      const companies = Object.keys(companyMap)
      if (companies.length === 0) {
        this.payChart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#ccc', fontSize: 14 } } })
        return
      }
      this.payChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['已付金额', '待付金额'], bottom: 0 },
        grid: { left: 80, right: 20, top: 20, bottom: 40 },
        xAxis: { type: 'value', name: '万元' },
        yAxis: { type: 'category', data: companies },
        series: [
          { name: '已付金额', type: 'bar', stack: 'total', data: companies.map(c => companyMap[c].paid), itemStyle: { color: '#1677FF' } },
          { name: '待付金额', type: 'bar', stack: 'total', data: companies.map(c => companyMap[c].pending), itemStyle: { color: '#E6F0FF' } },
        ],
      })
    },
  },
}
</script>

<style scoped lang="scss">
.execution-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.75); }
  .banner-right { display: flex; gap: 32px; }
  .banner-stat { text-align: center; color: #fff;
    .stat-num { display: block; font-size: 26px; font-weight: 700; }
    .stat-label { font-size: 12px; opacity: 0.8; }
  }
}

.stat-card { border-radius: 8px; }
.stat-card-inner { display: flex; align-items: center; gap: 14px;
  .stat-info { .stat-value { font-size: 26px; font-weight: 700; } .stat-label { font-size: 13px; color: #666; margin-top: 2px; } }
}

.card-header { display: flex; align-items: center; font-weight: 600; }
.tag-pill { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; }

::v-deep .row-exec-danger td { background: #FFF1F0 !important; }
::v-deep .row-exec-warning td { background: #FFFBE6 !important; }
::v-deep .row-exec-expiring td { background: #FFF7E6 !important; }
</style>
