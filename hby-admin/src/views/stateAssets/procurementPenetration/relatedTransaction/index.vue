<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-connection"></i> 关联交易监控</h2>
        <p class="page-desc">穿透识别关联方采购，统计关联交易金额，超限预警和审批追踪</p>
      </div>
    </div>

    <!-- 超限预警 -->
    <el-alert
      v-if="overLimitCount > 0"
      :title="`当前有 ${overLimitCount} 笔关联交易超限，共 ${undisclosedCount} 笔未披露，请立即核查！`"
      type="error" show-icon :closable="false" style="margin-bottom:12px"
    />

    <!-- 统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <div class="stat-card" :style="{ borderLeft: '4px solid ' + s.color }">
          <div class="stat-val" :style="{ color: s.color }">{{ s.value }}</div>
          <div class="stat-lb">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd">关联交易占比（集团加权平均）</div>
          <div ref="gaugeChart" class="chart-sm"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd">各企业关联交易占比对比</div>
          <div ref="barChart" class="chart-sm"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="采购企业">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="关联类型">
          <el-select v-model="queryForm.relationshipType" placeholder="请选择" clearable style="width:130px">
            <el-option label="股权关联" value="EQUITY" />
            <el-option label="人员关联" value="PERSONNEL" />
            <el-option label="资金关联" value="FUND" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否超限">
          <el-select v-model="queryForm.isOverLimit" placeholder="请选择" clearable style="width:100px">
            <el-option label="是" value="Y" /><el-option label="否" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否披露">
          <el-select v-model="queryForm.isDisclosed" placeholder="请选择" clearable style="width:100px">
            <el-option label="已披露" value="Y" /><el-option label="未披露" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border :row-class-name="rowClassName">
        <el-table-column label="交易编号" prop="transNo" width="130" />
        <el-table-column label="采购企业" prop="companyName" min-width="170" show-overflow-tooltip />
        <el-table-column label="关联供应商" prop="supplierName" min-width="160" show-overflow-tooltip />
        <el-table-column label="关联类型" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag size="mini" color="#F9F0FF" style="color:#722ED1;border-color:#722ED1">
              {{ { EQUITY: '股权关联', PERSONNEL: '人员关联', FUND: '资金关联' }[row.relationshipType] || row.relationshipType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易金额(万元)" prop="transAmount" width="130" align="right">
          <template slot-scope="{ row }">{{ row.transAmount.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="关联占比" width="90" align="center">
          <template slot-scope="{ row }">
            <span :style="{ color: row.isOverLimit === 'Y' ? '#CF1322' : '#52C41A', fontWeight: '600' }">{{ row.relatedRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="规定上限" width="80" align="center">
          <template slot-scope="{ row }">{{ row.limitRatio }}%</template>
        </el-table-column>
        <el-table-column label="是否超限" width="85" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.isOverLimit === 'Y' ? 'danger' : 'success'" size="mini">{{ row.isOverLimit === 'Y' ? '超限' : '达标' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="披露状态" width="85" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.isDisclosed === 'Y' ? 'success' : 'danger'" size="mini">{{ row.isDisclosed === 'Y' ? '已披露' : '未披露' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ APPROVED: 'success', PENDING: 'warning', NONE: 'danger' }[row.approvalStatus]" size="mini">
              {{ { APPROVED: '已审批', PENDING: '待审批', NONE: '未审批' }[row.approvalStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[row.riskLevel]" size="mini">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button v-if="row.isDisclosed !== 'Y'" type="text" size="mini" @click="handleDisclose(row)">披露</el-button>
            <span v-else class="disclosed-text">已披露</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:12px;text-align:right"
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getRelatedTransactionList, getRelatedTransactionStatistics, submitRelatedTransactionDisclosure } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'RelatedTransaction',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: {
        companyName: '',
        relationshipType: '',
        isOverLimit: '',
        isDisclosed: '',
        pageNumber: 1,
        pageSize: 10,
      },
      statCards: [
        { label: '关联交易总额(万元)', value: '--', color: '#722ED1' },
        { label: '集团关联交易比', value: '--', color: '#CF1322' },
        { label: '超限企业数', value: '--', color: '#CF1322' },
        { label: '未披露笔数', value: '--', color: '#FA8C16' },
      ],
      charts: [],
    }
  },
  computed: {
    overLimitCount() { return this.list.filter(r => r.isOverLimit === 'Y').length },
    undisclosedCount() { return this.list.filter(r => r.isDisclosed === 'N').length },
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.fetchData()
      this.fetchStatistics()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getRelatedTransactionList(this.queryForm)
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
    async fetchStatistics() {
      try {
        const res = await getRelatedTransactionStatistics()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.statCards = [
            { label: '关联交易总额(万元)', value: d.totalAmount ? d.totalAmount.toLocaleString() : '0', color: '#722ED1' },
            { label: '集团关联交易比', value: d.groupRatio ? d.groupRatio + '%' : '0%', color: '#CF1322' },
            { label: '超限企业数', value: d.overLimitCount ? d.overLimitCount + '家' : '0家', color: '#CF1322' },
            { label: '未披露笔数', value: d.undisclosedCount ? d.undisclosedCount + '笔' : '0笔', color: '#FA8C16' },
          ]
          // 更新仪表盘图表
          this.updateGaugeChart(d.groupRatio || 0)
          // 更新柱状图
          if (d.companyRatioList && d.companyRatioList.length > 0) {
            this.updateBarChart(d.companyRatioList)
          }
        }
      } catch (e) {
        console.error('获取统计数据失败', e)
      }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        companyName: '',
        relationshipType: '',
        isOverLimit: '',
        isDisclosed: '',
        pageNumber: 1,
        pageSize: 10,
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    async handleDisclose(row) {
      try {
        await this.$confirm('确认对该笔关联交易进行披露操作？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
        const res = await submitRelatedTransactionDisclosure({ id: row.id })
        if (res && res.result === 200) {
          this.$message.success('披露成功')
          this.fetchData()
          this.fetchStatistics()
        } else {
          this.$message.error((res && res.msg) || '披露失败')
        }
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },
    rowClassName({ row }) {
      if (row.isOverLimit === 'Y' && row.isDisclosed === 'N') return 'row-violation'
      if (row.isOverLimit === 'Y') return 'row-related'
      return ''
    },
    handleResize() { this.charts.forEach(c => c.resize()) },
    updateGaugeChart(value) {
      if (this.charts[0]) {
        this.charts[0].setOption({
          series: [{ data: [{ value: value, name: '集团关联交易比' }] }],
        })
      }
    },
    updateBarChart(companyRatioList) {
      if (this.charts[1]) {
        const names = companyRatioList.map(item => item.companyName)
        const values = companyRatioList.map(item => item.relatedRatio)
        this.charts[1].setOption({
          yAxis: { data: names },
          series: [{ data: values }],
        })
      }
    },
    initCharts() {
      this.charts.forEach(c => c.dispose())
      this.charts = []
      // gauge: group related ratio
      const c1 = echarts.init(this.$refs.gaugeChart)
      this.charts.push(c1)
      c1.setOption({
        series: [{
          type: 'gauge',
          center: ['50%', '55%'], radius: '80%',
          startAngle: 200, endAngle: -20,
          min: 0, max: 60,
          splitNumber: 6,
          axisLine: {
            lineStyle: {
              width: 12,
              color: [[0.2 / 0.6, '#52C41A'], [0.4 / 0.6, '#FA8C16'], [1, '#CF1322']],
            },
          },
          pointer: { length: '65%', width: 6, itemStyle: { color: 'auto' } },
          axisTick: { show: false },
          splitLine: { length: 15, lineStyle: { color: 'auto', width: 2 } },
          axisLabel: { color: '#464646', fontSize: 12, distance: 18, formatter: (v) => v + '%' },
          detail: { valueAnimation: true, formatter: '{value}%', color: '#CF1322', fontSize: 22, fontWeight: 'bold', offsetCenter: [0, '60%'] },
          data: [{ value: 0, name: '集团关联交易比' }],
          title: { fontSize: 13, color: '#666', offsetCenter: [0, '88%'] },
        }],
      })
      // bar: company comparison
      const companyMap = {}
      this.list.forEach(r => {
        companyMap[r.companyName] = r.relatedRatio || 0
      })
      const companyNames = Object.keys(companyMap).sort((a, b) => companyMap[a] - companyMap[b])
      const companyVals = companyNames.map(n => companyMap[n])
      const c2 = echarts.init(this.$refs.barChart)
      this.charts.push(c2)
      c2.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 140, right: 60, top: 10, bottom: 30 },
        xAxis: { type: 'value', name: '%', max: 80 },
        yAxis: { type: 'category', data: companyNames },
        series: [{
          type: 'bar',
          data: companyVals,
          itemStyle: { color: (p) => p.data > 30 ? '#CF1322' : (p.data > 15 ? '#FA8C16' : '#52C41A') },
          label: { show: true, position: 'right', formatter: (p) => p.value + '%' },
          markLine: { data: [{ xAxis: 30, name: '上限30%', lineStyle: { color: '#CF1322', type: 'dashed', width: 2 } }] },
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.page-header {
  padding: 18px 24px; margin-bottom: 12px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px; color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card { background: #fff; border-radius: 6px; padding: 14px 16px; box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .stat-val { font-size: 22px; font-weight: 700; }
  .stat-lb { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-sm { height: 220px; }
.search-card { margin-bottom: 12px; }
.card-hd { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-table .row-violation td { background: #FFF1F0 !important; }
::v-deep .el-table .row-related td { background: #F9F0FF !important; }
::v-deep .el-card { border-radius: 6px; }
.disclosed-text { font-size: 12px; color: #8C8C8C; }
</style>
