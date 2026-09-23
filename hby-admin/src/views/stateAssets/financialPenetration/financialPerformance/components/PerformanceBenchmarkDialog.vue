<template>
  <el-dialog
    title="绩效对标分析"
    :visible.sync="dialogVisible"
    width="1400px"
    :before-close="handleClose"
  >
    <el-form :model="benchmarkForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="对标维度">
            <el-select v-model="benchmarkForm.dimension" @change="handleDimensionChange" style="width: 100%;">
              <el-option label="行业对标" value="INDUSTRY"></el-option>
              <el-option label="同类企业对标" value="PEER"></el-option>
              <el-option label="历史对标" value="HISTORICAL"></el-option>
              <el-option label="标杆企业对标" value="BENCHMARK"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="对标期间">
            <el-select v-model="benchmarkForm.period" style="width: 100%;">
              <el-option label="2024年度" value="2024"></el-option>
              <el-option label="2023年度" value="2023"></el-option>
              <el-option label="近三年" value="3YEARS"></el-option>
              <el-option label="近五年" value="5YEARS"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="对标指标">
            <el-select v-model="benchmarkForm.indicators" multiple style="width: 100%;">
              <el-option label="盈利能力" value="PROFITABILITY"></el-option>
              <el-option label="成长能力" value="GROWTH"></el-option>
              <el-option label="运营效率" value="EFFICIENCY"></el-option>
              <el-option label="创新能力" value="INNOVATION"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-divider></el-divider>

    <el-tabs v-model="activeTab" type="border-card" v-loading="loading">
      <!-- 对标概览 -->
      <el-tab-pane label="对标概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="benchmark-card">
              <div class="benchmark-item">
                <div class="benchmark-label">综合排名</div>
                <div class="benchmark-value ranking">{{ overviewData.ranking || '--' }}</div>
                <div class="benchmark-desc">行业前{{ rankingPercent }}%</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="benchmark-card">
              <div class="benchmark-item">
                <div class="benchmark-label">优势指标</div>
                <div class="benchmark-value advantage">{{ advantageCount }}个</div>
                <div class="benchmark-desc">超越行业平均</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="benchmark-card">
              <div class="benchmark-item">
                <div class="benchmark-label">改进指标</div>
                <div class="benchmark-value improve">{{ improveCount }}个</div>
                <div class="benchmark-desc">需要重点关注</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="benchmark-card">
              <div class="benchmark-item">
                <div class="benchmark-label">综合评分</div>
                <div class="benchmark-value score">{{ overviewData.currentScore || '--' }}分</div>
                <div class="benchmark-desc">{{ scoreLevel }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <div ref="benchmarkRadarChart" style="height: 400px;"></div>
          </el-col>
          <el-col :span="12">
            <div ref="benchmarkTrendChart" style="height: 400px;"></div>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 详细对比 -->
      <el-tab-pane label="详细对比" name="detail">
        <el-table :data="benchmarkData" border size="small">
          <el-table-column prop="indicator" label="指标名称" width="150" fixed="left"></el-table-column>
          <el-table-column prop="currentValue" label="本企业" width="100" align="center">
            <template slot-scope="scope">
              <span :class="getValueClass(scope.row.currentValue, scope.row.industryAvg)">
                {{ scope.row.currentValue }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="industryAvg" label="行业平均" width="100" align="center"></el-table-column>
          <el-table-column prop="ranking" label="行业排名" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getRankingType(scope.row.ranking)">{{ scope.row.ranking }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="gap" label="与标杆差距" width="120" align="center">
            <template slot-scope="scope">
              <span :class="getGapClass(scope.row.gap)">{{ scope.row.gap }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="trend" label="趋势" width="100" align="center">
            <template slot-scope="scope">
              <i :class="getTrendIcon(scope.row.trend)" :style="getTrendColor(scope.row.trend)"></i>
              {{ scope.row.trend }}
            </template>
          </el-table-column>
          <el-table-column prop="suggestion" label="改进建议" min-width="200"></el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 改进建议 -->
      <el-tab-pane label="改进建议" name="suggestions">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover">
              <div slot="header">优势保持</div>
              <el-timeline>
                <el-timeline-item
                  v-for="item in advantageIndicators"
                  :key="item.indicator"
                  :timestamp="item.indicator"
                  placement="top"
                >
                  <el-card>
                    <h4>{{ item.indicator }}表现优异</h4>
                    <p>{{ item.suggestion }}</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item v-if="advantageIndicators.length === 0" timestamp="暂无数据" placement="top">
                  <el-card><p>暂无优势指标数据</p></el-card>
                </el-timeline-item>
              </el-timeline>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div slot="header">重点改进</div>
              <el-timeline>
                <el-timeline-item
                  v-for="item in improveIndicators"
                  :key="item.indicator"
                  :timestamp="item.indicator"
                  placement="top"
                  type="warning"
                >
                  <el-card>
                    <h4>{{ item.indicator }}需改进</h4>
                    <p>{{ item.suggestion }}</p>
                    <el-tag type="warning" size="small">差距：{{ item.gap }}</el-tag>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item v-if="improveIndicators.length === 0" timestamp="暂无数据" placement="top">
                  <el-card><p>暂无改进指标数据</p></el-card>
                </el-timeline-item>
              </el-timeline>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 行动计划 -->
      <el-tab-pane label="行动计划" name="action">
        <el-table :data="actionPlan" border size="small">
          <el-table-column prop="category" label="改进类别" width="120"></el-table-column>
          <el-table-column prop="action" label="具体措施" min-width="200"></el-table-column>
          <el-table-column prop="responsible" label="责任部门" width="120"></el-table-column>
          <el-table-column prop="deadline" label="完成期限" width="120"></el-table-column>
          <el-table-column prop="target" label="目标值" width="100"></el-table-column>
          <el-table-column prop="status" label="执行状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getActionStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="actionPlan.length === 0" description="暂无行动计划，请点击下方按钮生成"></el-empty>
      </el-tab-pane>
    </el-tabs>

    <div slot="footer" class="dialog-footer">
      <el-button :loading="exportLoading" @click="handleExport">导出分析报告</el-button>
      <el-button :loading="actionLoading" type="primary" @click="handleGenerateAction">生成行动计划</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { executeBenchmarkAnalysis, exportBenchmarkReport, generateBenchmarkActionPlan } from '@/api/stateAssets/financialPerformance'

export default {
  name: 'PerformanceBenchmarkDialog',
  props: {
    visible: { type: Boolean, default: false },
    benchmarkInfo: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      activeTab: 'overview',
      loading: false,
      benchmarkForm: {
        dimension: 'INDUSTRY',
        period: '2024',
        indicators: ['PROFITABILITY', 'GROWTH', 'EFFICIENCY', 'INNOVATION']
      },
      benchmarkResult: null,
      benchmarkData: [],
      actionPlan: [],
      exportLoading: false,
      actionLoading: false
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    overviewData() { return this.benchmarkResult || {} },
    rankingPercent() {
      if (!this.benchmarkResult || !this.benchmarkResult.ranking) return '--'
      const parts = String(this.benchmarkResult.ranking).split('/')
      if (parts.length === 2) return Math.round((parseInt(parts[0]) / parseInt(parts[1])) * 100)
      return '--'
    },
    advantageCount() {
      return this.advantageIndicators.length
    },
    improveCount() {
      return this.improveIndicators.length
    },
    advantageIndicators() {
      if (!this.benchmarkData || this.benchmarkData.length === 0) return []
      return this.benchmarkData.filter(i => parseFloat(i.gap) > 0)
    },
    improveIndicators() {
      if (!this.benchmarkData || this.benchmarkData.length === 0) return []
      return this.benchmarkData.filter(i => parseFloat(i.gap) < 0)
    },
    scoreLevel() {
      const score = Number(this.overviewData.currentScore) || 0
      if (score > 10) return '优秀水平'
      if (score > 5) return '良好水平'
      if (score > 1) return '一般水平'
      return '待改进'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadBenchmarkData()
      }
    }
  },
  methods: {
    async loadBenchmarkData() {
      if (!this.benchmarkInfo || (!this.benchmarkInfo.id && !this.benchmarkInfo.performanceId)) {
        this.$message.warning('缺少绩效记录ID，无法执行对标分析')
        return
      }
      this.loading = true
      try {
        const res = await executeBenchmarkAnalysis({
          performanceId: this.benchmarkInfo.id || this.benchmarkInfo.performanceId,
          dimension: this.benchmarkForm.dimension,
          period: this.benchmarkForm.period,
          indicators: this.benchmarkForm.indicators
        })
        if (res && (res.result === 200 || res.code === 200) && res.data) {
          this.benchmarkResult = res.data
          this.benchmarkData = res.data.indicators || []
          this.$nextTick(() => { if (this.activeTab === 'overview') this.initCharts() })
        } else {
          this.$message.error((res && res.msg) || '对标分析失败')
        }
      } catch (error) {
        console.error('对标分析失败:', error)
        this.$message.error('对标分析失败：' + (error.message || '网络错误'))
      } finally {
        this.loading = false
      }
    },
    handleDimensionChange() { this.loadBenchmarkData() },
    initCharts() { this.initBenchmarkRadarChart(); this.initBenchmarkTrendChart() },
    initBenchmarkRadarChart() {
      const el = this.$refs.benchmarkRadarChart
      if (!el) return
      const chart = echarts.init(el)
      const d = this.benchmarkInfo || {}
      const score = Math.min(Number(d.performanceScore) || 0, 100)
      const roe = Math.min(Number(d.roe) || 0, 100)
      const roa = Math.min(Number(d.roa) || 0, 100)
      chart.setOption({
        title: { text: '绩效对标雷达图', left: 'center' },
        legend: { data: ['本企业'], bottom: 10 },
        radar: { indicator: [{ name: '盈利能力', max: 100 }, { name: 'ROE', max: 100 }, { name: 'ROA', max: 100 }, { name: '绩效评分', max: 100 }, { name: '综合', max: 100 }] },
        series: [{ type: 'radar', data: [{ value: [score * 5, roe, roa, score, (roe + roa + score) / 3], name: '本企业', itemStyle: { color: '#409EFF' } }] }]
      })
    },
    initBenchmarkTrendChart() {
      const el = this.$refs.benchmarkTrendChart
      if (!el) return
      const chart = echarts.init(el)
      const d = this.benchmarkInfo || {}
      const r = this.benchmarkResult || {}
      chart.setOption({
        title: { text: '对标对比', left: 'center' },
        xAxis: { type: 'category', data: ['净利润率(%)', 'ROE(%)', 'ROA(%)'] },
        yAxis: { type: 'value' },
        legend: { data: ['本企业', '行业平均'], bottom: 10 },
        series: [
          { name: '本企业', type: 'bar', data: [Number(d.performanceScore) || 0, Number(d.roe) || 0, Number(d.roa) || 0], itemStyle: { color: '#409EFF' } },
          { name: '行业平均', type: 'bar', data: [Number(r.industryAvgProfit) > 0 && Number(r.industryAvgRevenue) > 0 ? (Number(r.industryAvgProfit) / Number(r.industryAvgRevenue) * 100).toFixed(2) : 0, 0, 0], itemStyle: { color: '#67C23A' } }
        ]
      })
    },
    getValueClass(current, avg) { return parseFloat(current) > parseFloat(avg) ? 'value-good' : 'value-poor' },
    getRankingType(ranking) {
      if (!ranking) return 'info'
      const rank = parseInt(ranking.split('/')[0])
      if (rank <= 3) return 'success'
      if (rank <= 8) return 'primary'
      return 'warning'
    },
    getGapClass(gap) { return parseFloat(gap) < 0 ? 'gap-negative' : 'gap-positive' },
    getTrendIcon(trend) { return trend === '上升' ? 'el-icon-top' : trend === '下降' ? 'el-icon-bottom' : 'el-icon-minus' },
    getTrendColor(trend) { return { color: trend === '上升' ? '#67C23A' : trend === '下降' ? '#F56C6C' : '#909399' } },
    getActionStatusType(status) { return { '计划中': 'info', '进行中': 'warning', '已完成': 'success', '已延期': 'danger' }[status] || 'info' },
    async handleExport() {
      if (!this.benchmarkInfo || (!this.benchmarkInfo.id && !this.benchmarkInfo.performanceId)) {
        this.$message.warning('缺少绩效记录，无法导出报告')
        return
      }
      this.exportLoading = true
      try {
        const res = await exportBenchmarkReport({
          performanceId: this.benchmarkInfo.id || this.benchmarkInfo.performanceId,
          dimension: this.benchmarkForm.dimension,
          period: this.benchmarkForm.period
        })
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `对标分析报告_${this.benchmarkInfo.enterpriseName || ''}_${new Date().toISOString().slice(0, 10)}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('分析报告导出成功')
        } else {
          this.$message.error('导出失败，未获取到文件数据')
        }
      } catch (error) {
        console.error('导出对标分析报告失败:', error)
        this.$message.error('导出失败：' + (error.message || '网络错误'))
      } finally {
        this.exportLoading = false
      }
    },
    async handleGenerateAction() {
      if (!this.benchmarkInfo || (!this.benchmarkInfo.id && !this.benchmarkInfo.performanceId)) {
        this.$message.warning('缺少绩效记录，无法生成行动计划')
        return
      }
      this.actionLoading = true
      try {
        const res = await generateBenchmarkActionPlan({
          performanceId: this.benchmarkInfo.id || this.benchmarkInfo.performanceId,
          dimension: this.benchmarkForm.dimension,
          period: this.benchmarkForm.period
        })
        if (res && (res.result === 200 || res.code === 200) && res.data) {
          this.actionPlan = res.data
          this.activeTab = 'action'
          this.$message.success('行动计划生成成功，共' + res.data.length + '项改进措施')
        } else {
          this.$message.error((res && res.msg) || '生成行动计划失败')
        }
      } catch (error) {
        console.error('生成行动计划失败:', error)
        this.$message.error('生成失败：' + (error.message || '网络错误'))
      } finally {
        this.actionLoading = false
      }
    },
    handleClose() { this.$emit('update:visible', false) }
  }
}
</script>

<style scoped>
.benchmark-card {
  text-align: center;
  height: 120px;
}

.benchmark-item {
  padding: 20px 0;
}

.benchmark-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.benchmark-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.benchmark-value.ranking {
  color: #409EFF;
}

.benchmark-value.advantage {
  color: #67C23A;
}

.benchmark-value.improve {
  color: #E6A23C;
}

.benchmark-value.score {
  color: #F56C6C;
}

.benchmark-desc {
  font-size: 12px;
  color: #999;
}

.value-good {
  color: #67C23A;
  font-weight: bold;
}

.value-poor {
  color: #F56C6C;
}

.gap-negative {
  color: #F56C6C;
}

.gap-positive {
  color: #67C23A;
}
</style>
