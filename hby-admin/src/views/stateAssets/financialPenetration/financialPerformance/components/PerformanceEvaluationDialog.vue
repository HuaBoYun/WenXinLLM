<template>
  <el-dialog
    title="绩效评价分析"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <div v-loading="loading" element-loading-text="加载中...">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基础信息 -->
        <el-tab-pane label="基础信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="企业名称">{{ evaluationData.enterpriseName }}</el-descriptions-item>
            <el-descriptions-item label="评价期间">{{ evaluationData.evaluationPeriod }}</el-descriptions-item>
            <el-descriptions-item label="评价类型">{{ getEvaluationTypeText(evaluationData.evaluationType) }}</el-descriptions-item>
            <el-descriptions-item label="绩效等级">
              <el-tag :type="getPerformanceLevelType(evaluationData.performanceLevel)">
                {{ getPerformanceLevelText(evaluationData.performanceLevel) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="绩效评分">
              <el-progress :percentage="evaluationData.performanceScore || 0" :color="getScoreColor(evaluationData.performanceScore)"></el-progress>
            </el-descriptions-item>
            <el-descriptions-item label="评价状态">
              <el-tag :type="getStatusType(evaluationData.evaluationStatus)">
                {{ getStatusText(evaluationData.evaluationStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 绩效指标 -->
        <el-tab-pane label="绩效指标" name="indicators">
          <div v-loading="indicatorsLoading" element-loading-text="加载指标数据...">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="hover">
                  <div slot="header">盈利能力指标</div>
                  <el-table :data="profitabilityIndicators" size="small">
                    <el-table-column prop="indicator" label="指标名称" width="120"></el-table-column>
                    <el-table-column prop="value" label="指标值" width="80" align="center"></el-table-column>
                    <el-table-column prop="benchmark" label="行业基准" width="80" align="center"></el-table-column>
                    <el-table-column prop="rating" label="评级" align="center">
                      <template slot-scope="scope">
                        <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="hover">
                  <div slot="header">成长能力指标</div>
                  <el-table :data="growthIndicators" size="small">
                    <el-table-column prop="indicator" label="指标名称" width="120"></el-table-column>
                    <el-table-column prop="value" label="指标值" width="80" align="center"></el-table-column>
                    <el-table-column prop="benchmark" label="行业基准" width="80" align="center"></el-table-column>
                    <el-table-column prop="rating" label="评级" align="center">
                      <template slot-scope="scope">
                        <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
            </el-row>

            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card shadow="hover">
                  <div slot="header">运营效率指标</div>
                  <el-table :data="efficiencyIndicators" size="small">
                    <el-table-column prop="indicator" label="指标名称" width="120"></el-table-column>
                    <el-table-column prop="value" label="指标值" width="80" align="center"></el-table-column>
                    <el-table-column prop="benchmark" label="行业基准" width="80" align="center"></el-table-column>
                    <el-table-column prop="rating" label="评级" align="center">
                      <template slot-scope="scope">
                        <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="hover">
                  <div slot="header">创新能力指标</div>
                  <el-table :data="innovationIndicators" size="small">
                    <el-table-column prop="indicator" label="指标名称" width="120"></el-table-column>
                    <el-table-column prop="value" label="指标值" width="80" align="center"></el-table-column>
                    <el-table-column prop="benchmark" label="行业基准" width="80" align="center"></el-table-column>
                    <el-table-column prop="rating" label="评级" align="center">
                      <template slot-scope="scope">
                        <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 对标分析 -->
        <el-tab-pane label="对标分析" name="benchmark">
          <el-row :gutter="20">
            <el-col :span="12">
              <div ref="benchmarkRadarChart" style="height: 400px;"></div>
            </el-col>
            <el-col :span="12">
              <div ref="benchmarkBarChart" style="height: 400px;"></div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 评价报告 -->
        <el-tab-pane label="评价报告" name="report">
          <el-card shadow="hover">
            <div slot="header">绩效评价报告</div>
            <div class="report-content">
              <h4>一、评价概述</h4>
              <p>{{ evaluationData.enterpriseName }}{{ evaluationData.evaluationPeriod }}年度绩效评价结果为{{ getPerformanceLevelText(evaluationData.performanceLevel) }}，综合评分{{ evaluationData.performanceScore }}分。</p>

              <h4>二、主要指标表现</h4>
              <ul>
                <li v-if="evaluationData.roe != null">净资产收益率(ROE)：{{ evaluationData.roe }}%</li>
                <li v-if="evaluationData.roa != null">总资产收益率(ROA)：{{ evaluationData.roa }}%</li>
                <li v-if="evaluationData.revenue != null">营业收入：{{ formatAmount(evaluationData.revenue) }}万元</li>
                <li v-if="evaluationData.netProfit != null">净利润：{{ formatAmount(evaluationData.netProfit) }}万元</li>
                <li v-if="evaluationData.revenueGrowthRate != null">营收增长率：{{ evaluationData.revenueGrowthRate }}%</li>
                <li v-if="evaluationData.netProfitGrowthRate != null">净利润增长率：{{ evaluationData.netProfitGrowthRate }}%</li>
              </ul>

              <h4>三、评价结论</h4>
              <p v-if="evaluationResult">
                盈利能力评分：{{ evaluationResult.profitabilityScore || '--' }}分，
                成长能力评分：{{ evaluationResult.growthScore || '--' }}分，
                运营效率评分：{{ evaluationResult.efficiencyScore || '--' }}分。
              </p>
              <p v-else>暂无评价结论，请执行绩效评价。</p>

              <h4>四、改进建议</h4>
              <ul>
                <li v-if="evaluationData.roe < 10">净资产收益率偏低，建议优化资本结构，提升盈利能力</li>
                <li v-if="evaluationData.roa < 5">总资产收益率偏低，建议提升资产利用效率</li>
                <li v-if="evaluationData.revenueGrowthRate < 10">营收增长率偏低，建议拓展市场渠道</li>
                <li v-if="!evaluationData.roe || evaluationData.roe >= 10">持续保持盈利能力优势</li>
                <li>完善绩效考核体系，持续跟踪改进效果</li>
              </ul>
            </div>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button
        type="primary"
        :loading="evaluating"
        @click="handleExecuteEvaluation"
      >执行评价</el-button>
      <el-button :loading="exporting" @click="handleExport">导出报告</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { executePerformanceEvaluation, exportPerformanceData } from '@/api/stateAssets/financialPerformance'

export default {
  name: 'PerformanceEvaluationDialog',
  props: {
    visible: { type: Boolean, default: false },
    evaluationData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      activeTab: 'basic',
      loading: false,
      indicatorsLoading: false,
      evaluating: false,
      exporting: false,
      evaluationResult: null,
      profitabilityIndicators: [],
      growthIndicators: [],
      efficiencyIndicators: [],
      innovationIndicators: []
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.buildIndicators()
        this.$nextTick(() => { if (this.activeTab === 'benchmark') this.initCharts() })
      } else {
        this.evaluationResult = null
      }
    },
    activeTab(val) {
      if (val === 'benchmark') this.$nextTick(() => { this.initCharts() })
    }
  },
  methods: {
    buildIndicators() {
      const d = this.evaluationData || {}
      const roe = Number(d.roe) || 0
      const roa = Number(d.roa) || 0
      const score = Number(d.performanceScore) || 0
      const revenue = Number(d.revenue) || 0
      const netProfit = Number(d.netProfit) || 0
      const totalAssets = Number(d.totalAssets) || 0
      const netAssets = Number(d.netAssets) || 0
      const totalLiabilities = Number(d.totalLiabilities) || 0
      const profitMargin = revenue > 0 ? ((netProfit / revenue) * 100).toFixed(2) : '0'
      const debtRatio = totalAssets > 0 ? ((totalLiabilities / totalAssets) * 100).toFixed(2) : '0'
      this.profitabilityIndicators = [
        { indicator: 'ROE(%)', value: roe.toFixed(2), benchmark: '-', rating: roe > 15 ? 5 : roe > 10 ? 4 : roe > 5 ? 3 : 2 },
        { indicator: 'ROA(%)', value: roa.toFixed(2), benchmark: '-', rating: roa > 8 ? 5 : roa > 5 ? 4 : roa > 2 ? 3 : 2 },
        { indicator: '净利润率(%)', value: profitMargin, benchmark: '-', rating: Number(profitMargin) > 10 ? 5 : Number(profitMargin) > 5 ? 4 : 3 },
        { indicator: '绩效评分', value: score.toFixed(2), benchmark: '-', rating: score > 10 ? 5 : score > 5 ? 4 : score > 1 ? 3 : 2 }
      ]
      this.growthIndicators = [
        { indicator: '营业收入', value: this.formatAmount(revenue) + '万', benchmark: '-', rating: revenue > 100000 ? 5 : revenue > 50000 ? 4 : 3 },
        { indicator: '净利润', value: this.formatAmount(netProfit) + '万', benchmark: '-', rating: netProfit > 10000 ? 5 : netProfit > 5000 ? 4 : 3 }
      ]
      this.efficiencyIndicators = [
        { indicator: '总资产', value: this.formatAmount(totalAssets) + '万', benchmark: '-', rating: 4 },
        { indicator: '净资产', value: this.formatAmount(netAssets) + '万', benchmark: '-', rating: 4 },
        { indicator: '资产负债率(%)', value: debtRatio, benchmark: '-', rating: Number(debtRatio) < 50 ? 5 : Number(debtRatio) < 70 ? 4 : 3 }
      ]
      this.innovationIndicators = [
        { indicator: '经营现金流', value: this.formatAmount(Number(d.operatingCashflow) || 0) + '万', benchmark: '-', rating: (Number(d.operatingCashflow) || 0) > 0 ? 4 : 2 }
      ]
    },
    initCharts() {
      this.initBenchmarkRadarChart()
      this.initBenchmarkBarChart()
    },
    initBenchmarkRadarChart() {
      if (!this.$refs.benchmarkRadarChart) return
      const chart = echarts.init(this.$refs.benchmarkRadarChart)
      const d = this.evaluationData || {}
      const roe = Math.min(Number(d.roe) || 0, 100)
      const roa = Math.min(Number(d.roa) || 0, 100)
      const score = Math.min(Number(d.performanceScore) || 0, 100)
      chart.setOption({
        title: { text: '绩效指标雷达图', left: 'center' },
        radar: { indicator: [{ name: '盈利能力', max: 100 }, { name: 'ROE', max: 100 }, { name: 'ROA', max: 100 }, { name: '绩效评分', max: 100 }, { name: '综合', max: 100 }] },
        series: [{ type: 'radar', data: [{ value: [score * 5, roe, roa, score, (roe + roa + score) / 3], name: '本企业', itemStyle: { color: '#409EFF' } }] }]
      })
    },
    initBenchmarkBarChart() {
      if (!this.$refs.benchmarkBarChart) return
      const chart = echarts.init(this.$refs.benchmarkBarChart)
      const d = this.evaluationData || {}
      chart.setOption({
        title: { text: '关键指标', left: 'center' },
        xAxis: { type: 'category', data: ['ROE(%)', 'ROA(%)', '绩效评分', '净利润率(%)'] },
        yAxis: { type: 'value' },
        series: [{ name: '本企业', type: 'bar', data: [Number(d.roe) || 0, Number(d.roa) || 0, Number(d.performanceScore) || 0, Number(d.revenue) > 0 ? (Number(d.netProfit) / Number(d.revenue) * 100).toFixed(2) : 0], itemStyle: { color: '#409EFF' } }]
      })
    },
    async handleExecuteEvaluation() {
      if (!this.evaluationData || (!this.evaluationData.id && !this.evaluationData.performanceId)) {
        this.$message.warning('缺少绩效记录ID，无法执行评价')
        return
      }
      this.evaluating = true
      try {
        const res = await executePerformanceEvaluation({
          performanceId: this.evaluationData.id || this.evaluationData.performanceId
        })
        if (res && (res.result === 200 || res.code === 200) && res.data) {
          this.evaluationResult = res.data
          this.$message.success('评价执行成功')
          this.$emit('refresh')
        } else {
          this.$message.error((res && res.msg) || '评价执行失败')
        }
      } catch (error) {
        console.error('执行评价失败:', error)
        this.$message.error('评价执行失败：' + (error.message || '网络错误'))
      } finally {
        this.evaluating = false
      }
    },
    async handleExport() {
      this.exporting = true
      try {
        const res = await exportPerformanceData({ enterpriseName: this.evaluationData.enterpriseName })
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `绩效评价报告_${this.evaluationData.enterpriseName || ''}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) { this.$message.error('导出失败') }
      finally { this.exporting = false }
    },
    formatAmount(val) {
      if (!val && val !== 0) return '0'
      const num = Number(val)
      if (num >= 100000000) return (num / 100000000).toFixed(2) + '亿'
      if (num >= 10000) return (num / 10000).toFixed(1) + '万'
      return num.toLocaleString()
    },
    getEvaluationTypeText(type) { return { 'ANNUAL': '年度绩效评价', 'QUARTERLY': '季度绩效评价', 'SPECIAL': '专项绩效评价', 'BENCHMARK': '对标绩效评价' }[type] || type || '-' },
    getPerformanceLevelText(level) { return { 'EXCELLENT': '优秀', 'GOOD': '良好', 'AVERAGE': '一般', 'POOR': '较差' }[level] || level || '-' },
    getPerformanceLevelType(level) { return { 'EXCELLENT': 'success', 'GOOD': 'primary', 'AVERAGE': 'warning', 'POOR': 'danger' }[level] || 'info' },
    getStatusText(status) { return { 'PENDING': '待评价', 'EVALUATING': '评价中', 'EVALUATED': '已评价', 'DRAFT': '草稿', 'SUBMITTED': '已提交', 'APPROVED': '已审核', 'PUBLISHED': '已发布' }[status] || status || '-' },
    getStatusType(status) { return { 'PENDING': 'info', 'EVALUATING': 'warning', 'EVALUATED': 'success', 'DRAFT': 'info', 'SUBMITTED': 'warning', 'APPROVED': 'primary', 'PUBLISHED': 'success' }[status] || 'info' },
    getScoreColor(score) { if (score >= 90) return '#67C23A'; if (score >= 80) return '#409EFF'; if (score >= 70) return '#E6A23C'; return '#F56C6C' },
    handleClose() { this.$emit('update:visible', false) }
  }
}
</script>

<style scoped>
.report-content {
  line-height: 1.8;
}

.report-content h4 {
  color: #409EFF;
  margin: 20px 0 10px 0;
}

.report-content ul {
  margin: 10px 0;
  padding-left: 20px;
}

.report-content li {
  margin: 5px 0;
}
</style>
