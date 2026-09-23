<template>
  <div class="fin-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-odometer"></i><span>流动性风险分析</span></div>
      <div class="page-header-desc">核心特色页面：偿债能力仪表盘 · 到期融资分布 · 流动性压力测试</div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }"><i :class="s.icon"></i></div>
            <div class="stat-info">
              <div class="stat-value" :style="{ color: s.valueColor || '#303133' }">{{ s.value }}<span v-if="s.unit" class="stat-unit">{{ s.unit }}</span></div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="mb-16">
      <!-- 偿债能力仪表盘 -->
      <el-col :span="10">
        <el-card shadow="hover">
          <div slot="header"><span>集团综合流动性评分仪表盘</span></div>
          <div ref="gaugeChart" style="height:240px"></div>
        </el-card>
      </el-col>
      <!-- 到期融资分布 -->
      <el-col :span="14">
        <el-card shadow="hover">
          <div slot="header"><span>融资到期分布（按季度，亿元）</span></div>
          <div ref="maturityChart" style="height:240px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 企业明细 + 压力测试 -->
    <el-row :gutter="16" class="mb-16">
      <!-- 企业明细表 -->
      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header">
            <span>企业流动性指标明细</span>
            <el-tag type="danger" size="mini" style="margin-left:10px">红色 = 高风险（流动比率 &lt; 1.0）</el-tag>
          </div>
          <el-table :data="liquidityList" border size="small" style="width:100%"
            :row-class-name="getRowClass">
            <el-table-column label="企业名称" prop="companyName" min-width="150" show-overflow-tooltip />
            <el-table-column label="流动比率" prop="currentRatio" width="90" align="center">
              <template slot-scope="scope">
                <span :style="{ color: scope.row.currentRatio < 1.0 ? '#CF1322' : scope.row.currentRatio < 1.5 ? '#FA8C16' : '#52C41A', fontWeight: 600 }">
                  {{ scope.row.currentRatio }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="速动比率" prop="quickRatio" width="90" align="center">
              <template slot-scope="scope">
                <span :style="{ color: scope.row.quickRatio < 0.5 ? '#CF1322' : '#303133' }">{{ scope.row.quickRatio }}</span>
              </template>
            </el-table-column>
            <el-table-column label="现金等价物(万)" prop="cashEquivalent" width="120" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.cashEquivalent ? Number(scope.row.cashEquivalent).toLocaleString() : '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="短期借款(万)" prop="shortTermDebt" width="110" align="right">
              <template slot-scope="scope">
                <span>{{ scope.row.shortTermDebt ? Number(scope.row.shortTermDebt).toLocaleString() : '--' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="1年内到期(万)" prop="maturity01y" width="120" align="right">
              <template slot-scope="scope">
                <span :style="{ color: scope.row.maturity01y > scope.row.cashEquivalent ? '#CF1322' : '' }">
                  {{ scope.row.maturity01y ? Number(scope.row.maturity01y).toLocaleString() : '--' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="评分" prop="liquidityScore" width="70" align="center">
              <template slot-scope="scope">
                <el-tag :type="getScoreTag(scope.row.liquidityScore)" size="mini">{{ scope.row.liquidityScore || '--' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="mini">{{ scope.row.riskLevel }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 压力测试 -->
      <el-col :span="8">
        <el-card shadow="never" style="height:100%">
          <div slot="header">
            <span>流动性压力测试场景</span>
          </div>
          <div v-for="sc in stressScenarios" :key="sc.name" class="stress-card" :class="'stress-' + sc.level.toLowerCase()">
            <div class="stress-head">
              <span class="stress-name">{{ sc.name }}</span>
              <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[sc.level]" size="mini">{{ sc.result }}</el-tag>
            </div>
            <div class="stress-desc">{{ sc.desc }}</div>
            <div class="stress-gap">
              流动性缺口：<span :style="{ color: sc.liquidityGap.startsWith('-') ? '#CF1322' : '#52C41A', fontWeight: 600 }">{{ sc.liquidityGap }} 万元</span>
            </div>
          </div>
          <div style="margin-top:16px">
            <el-button type="primary" size="small" style="width:100%" @click="runTest">
              <i class="el-icon-video-play"></i> 执行压力测试
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getLiquidityRiskList, getLiquidityRiskScore, getLiquidityStressTest, getFinancingMaturityDistribution } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'FinancialRiskLiquidity',
  mixins: [investThemeMixin],
  data() {
    return {
      charts: [],
      statCards: [
        { label: '集团平均流动比率', value: '--', icon: 'el-icon-data-line', color: this.ipSecondary },
        { label: '集团平均速动比率', value: '--', icon: 'el-icon-s-finance', color: this.ipBright },
        { label: '现金流覆盖率', value: '--', unit: '%', icon: 'el-icon-coin', color: '#52C41A' },
        { label: '流动性综合评级', value: '--', icon: 'el-icon-medal', color: '#FA8C16', valueColor: '#FA8C16' }
      ],
      stressScenarios: [],
      liquidityList: [],
      scoreData: { score: 0, level: '' },
      maturityData: { categories: [], bankLoan: [], bond: [], entrustLoan: [] }
    }
  },
  mounted() {
    this.fetchLiquidityList()
    this.fetchLiquidityScore()
    this.fetchStressTest()
    this.$nextTick(() => {
      this.initGaugeChart()
      this.initMaturityChart()
    })
  },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    /** 获取企业流动性指标明细列表 */
    async fetchLiquidityList() {
      try {
        const res = await getLiquidityRiskList({})
        if (res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || res.data.records || res.data
          this.liquidityList = Array.isArray(list) ? list : []
          this.computeStatCards()
        }
      } catch (e) {
        this.$message.error('获取流动性指标列表失败')
      }
    },
    /** 获取综合流动性评分并更新仪表盘 */
    async fetchLiquidityScore() {
      try {
        const res = await getLiquidityRiskScore('')
        if (res.result === 200 && res.data) {
          this.scoreData = res.data
          this.updateGaugeChart(res.data)
        }
      } catch (e) {
        console.error('获取流动性评分失败', e)
      }
    },
    /** 获取压力测试场景数据 */
    async fetchStressTest() {
      try {
        const res = await getLiquidityStressTest({})
        if (res.result === 200 && res.data) {
          this.stressScenarios = Array.isArray(res.data) ? res.data : (res.data.list || [])
        }
      } catch (e) {
        console.error('获取压力测试数据失败', e)
      }
    },
    /** 获取融资到期分布并更新图表 */
    async fetchMaturityDistribution() {
      try {
        const res = await getFinancingMaturityDistribution('')
        if (res.result === 200 && res.data) {
          this.maturityData = res.data
          this.updateMaturityChart(res.data)
        }
      } catch (e) {
        console.error('获取融资到期分布失败', e)
      }
    },
    /** 根据列表数据计算统计卡片 */
    computeStatCards() {
      const list = this.liquidityList
      if (!list.length) return
      const avgCurrent = (list.reduce((s, r) => s + (Number(r.currentRatio) || 0), 0) / list.length).toFixed(2)
      const avgQuick = (list.reduce((s, r) => s + (Number(r.quickRatio) || 0), 0) / list.length).toFixed(2)
      const totalCash = list.reduce((s, r) => s + (Number(r.cashEquivalent) || 0), 0)
      const totalShortDebt = list.reduce((s, r) => s + (Number(r.shortTermDebt) || 0), 0)
      const cashCoverage = totalShortDebt > 0 ? ((totalCash / totalShortDebt) * 100).toFixed(1) : '0.0'
      const level = this.scoreData.level || '--'
      this.statCards = [
        { label: '集团平均流动比率', value: avgCurrent, icon: 'el-icon-data-line', color: this.ipSecondary },
        { label: '集团平均速动比率', value: avgQuick, icon: 'el-icon-s-finance', color: this.ipBright },
        { label: '现金流覆盖率', value: cashCoverage, unit: '%', icon: 'el-icon-coin', color: '#52C41A' },
        { label: '流动性综合评级', value: level, icon: 'el-icon-medal', color: '#FA8C16', valueColor: '#FA8C16' }
      ]
    },
    getRowClass({ row }) {
      if (row.riskLevel === 'HIGH') return 'high-risk-row'
      return ''
    },
    getScoreTag(score) {
      if (!score) return 'info'
      const s = Number(score)
      if (s >= 80) return 'success'
      if (s >= 60) return 'warning'
      return 'danger'
    },
    getRatingTag(rating) {
      if (!rating) return 'info'
      if (rating.startsWith('A')) return 'success'
      if (rating.startsWith('B')) return 'warning'
      return 'danger'
    },
    /** 执行压力测试 */
    async runTest() {
      const loading = this.$loading({ lock: true, text: '正在执行流动性压力测试...', background: 'rgba(0,0,0,0.7)' })
      try {
        const res = await getLiquidityStressTest({})
        if (res.result === 200 && res.data) {
          this.stressScenarios = Array.isArray(res.data) ? res.data : (res.data.list || [])
          this.$message.success('压力测试完成，结果已更新')
        } else {
          this.$message.warning(res.msg || '压力测试执行异常')
        }
      } catch (e) {
        this.$message.error('压力测试执行失败')
      } finally {
        loading.close()
      }
    },
    /** 初始化仪表盘（空数据） */
    initGaugeChart() {
      const c = echarts.init(this.$refs.gaugeChart)
      this.charts.push(c)
      c.setOption({
        series: [{
          type: 'gauge',
          center: ['50%', '65%'],
          startAngle: 200, endAngle: -20,
          min: 0, max: 100,
          splitNumber: 5,
          axisLine: {
            lineStyle: { width: 20, color: [[0.3, '#CF1322'], [0.6, '#FA8C16'], [1, '#52C41A']] }
          },
          pointer: { itemStyle: { color: 'auto' } },
          axisTick: { distance: -25, length: 8, lineStyle: { color: '#fff', width: 2 } },
          splitLine: { distance: -30, length: 20, lineStyle: { color: '#fff', width: 3 } },
          axisLabel: { color: 'inherit', distance: 35, fontSize: 12 },
          detail: { valueAnimation: true, formatter: '{value}分', color: 'inherit', fontSize: 18, fontWeight: 'bold', offsetCenter: [0, '25%'] },
          data: [{ value: 0, name: '综合流动性评分' }]
        }]
      })
      this.fetchLiquidityScore()
    },
    /** 更新仪表盘数据 */
    updateGaugeChart(data) {
      const c = this.charts[0]
      if (!c) return
      const score = data.score || 0
      const level = data.level || ''
      c.setOption({
        series: [{
          detail: { formatter: '{value}分\n' + level + '级' },
          data: [{ value: score, name: '综合流动性评分' }]
        }]
      })
      this.computeStatCards()
    },
    /** 初始化到期分布图表（空数据） */
    initMaturityChart() {
      const c = echarts.init(this.$refs.maturityChart)
      this.charts.push(c)
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['银行贷款到期', '债券到期', '委托贷款到期'] },
        grid: { left: 50, right: 20, top: 40, bottom: 30 },
        xAxis: { type: 'category', data: [] },
        yAxis: { type: 'value', name: '亿元' },
        series: [
          { name: '银行贷款到期', type: 'bar', stack: 'total', data: [], itemStyle: { color: this.ipSecondary } },
          { name: '债券到期', type: 'bar', stack: 'total', data: [], itemStyle: { color: this.ipBright } },
          { name: '委托贷款到期', type: 'bar', stack: 'total', data: [], itemStyle: { color: '#FA8C16' } }
        ]
      })
      this.fetchMaturityDistribution()
    },
    /** 更新到期分布图表数据 */
    updateMaturityChart(data) {
      const c = this.charts[1]
      if (!c) return
      c.setOption({
        xAxis: { data: data.categories || [] },
        series: [
          { name: '银行贷款到期', data: data.bankLoan || [] },
          { name: '债券到期', data: data.bond || [] },
          { name: '委托贷款到期', data: data.entrustLoan || [] }
        ]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 22px; font-weight: 700; line-height: 1; .stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

// 压力测试卡
.stress-card {
  padding: 12px 14px; border-radius: 6px; border-left: 3px solid; margin-bottom: 10px;
  &.stress-low { background: #F6FFED; border-left-color: #52C41A; }
  &.stress-medium { background: #FFF7E6; border-left-color: #FA8C16; }
  &.stress-high { background: #FFF1F0; border-left-color: #CF1322; }
}
.stress-head { display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px; }
.stress-name { font-size: 13px; font-weight: 600; color: #303133; }
.stress-desc { font-size: 12px; color: #606266; margin-bottom: 4px; }
.stress-gap { font-size: 12px; color: #909399; }

::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-table .high-risk-row { background: #FFF1F0 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
