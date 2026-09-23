<template>
  <div class="tax-compliance-dashboard" v-loading="loading">
    <!-- 概览统计 -->
    <div class="overview-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card total">
            <div class="stat-icon">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.totalCount || 0 }}</div>
              <div class="stat-label">总检查数</div>
              <div class="stat-trend">
                <span class="trend-text">较上月</span>
                <span class="trend-value positive">+12%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card completed">
            <div class="stat-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
              <div class="stat-trend">
                <span class="trend-text">完成率</span>
                <span class="trend-value">{{ getCompletionRate() }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card compliant">
            <div class="stat-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.compliantCount || 0 }}</div>
              <div class="stat-label">合规检查</div>
              <div class="stat-trend">
                <span class="trend-text">合规率</span>
                <span class="trend-value">{{ getComplianceRate() }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card high-risk">
            <div class="stat-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewStats.highRiskCount || 0 }}</div>
              <div class="stat-label">高风险</div>
              <div class="stat-trend">
                <span class="trend-text">风险率</span>
                <span class="trend-value negative">{{ getRiskRate() }}%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析 -->
    <div class="chart-section">
      <el-row :gutter="20">
        <!-- 检查状态分布 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>检查状态分布</span>
              <el-button type="text" @click="refreshCheckStatusChart">刷新</el-button>
            </div>
            <div ref="checkStatusChart" class="chart-container"></div>
          </el-card>
        </el-col>

        <!-- 合规状态分布 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>合规状态分布</span>
              <el-button type="text" @click="refreshComplianceStatusChart">刷新</el-button>
            </div>
            <div ref="complianceStatusChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 检查趋势 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>检查趋势</span>
              <div class="header-controls">
                <el-radio-group v-model="trendPeriod" size="mini" @change="refreshTrendChart">
                  <el-radio-button label="week">近7天</el-radio-button>
                  <el-radio-button label="month">近30天</el-radio-button>
                  <el-radio-button label="quarter">近3个月</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <div ref="trendChart" class="chart-container"></div>
          </el-card>
        </el-col>

        <!-- 风险分布 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>风险等级分布</span>
              <el-button type="text" @click="refreshRiskChart">刷新</el-button>
            </div>
            <div ref="riskChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 合规评分趋势 -->
        <el-col :span="24">
          <el-card class="chart-card">
            <div slot="header" class="card-header">
              <span>合规评分趋势</span>
              <div class="header-controls">
                <el-date-picker
                  v-model="scoreTrendDateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="mini"
                  @change="refreshScoreTrendChart"
                />
              </div>
            </div>
            <div ref="scoreTrendChart" class="chart-container large"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜和效率统计 -->
    <div class="ranking-section">
      <el-row :gutter="20">
        <!-- 检查排行榜 -->
        <el-col :span="12">
          <el-card class="ranking-card">
            <div slot="header" class="card-header">
              <span>检查排行榜</span>
              <el-select v-model="rankingType" size="mini" @change="refreshRanking">
                <el-option label="按合规评分" value="complianceScore" />
                <el-option label="按检查数量" value="checkCount" />
                <el-option label="按完成率" value="completionRate" />
              </el-select>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in rankingData"
                :key="index"
                class="ranking-item"
              >
                <div class="ranking-number" :class="getRankingClass(index)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-info">
                  <div class="ranking-name">{{ item.name }}</div>
                  <div class="ranking-meta">{{ item.department }}</div>
                </div>
                <div class="ranking-value">
                  {{ formatRankingValue(item.value, rankingType) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 效率统计 -->
        <el-col :span="12">
          <el-card class="efficiency-card">
            <div slot="header" class="card-header">
              <span>效率统计</span>
              <el-button type="text" @click="refreshEfficiencyStats">刷新</el-button>
            </div>
            <div class="efficiency-stats">
              <div class="efficiency-item">
                <div class="efficiency-label">平均检查时长</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.avgCheckDuration || '-' }}
                  <span class="efficiency-unit">小时</span>
                </div>
              </div>
              <div class="efficiency-item">
                <div class="efficiency-label">平均整改时长</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.avgRectificationDuration || '-' }}
                  <span class="efficiency-unit">天</span>
                </div>
              </div>
              <div class="efficiency-item">
                <div class="efficiency-label">检查效率</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.checkEfficiency || '-' }}
                  <span class="efficiency-unit">%</span>
                </div>
              </div>
              <div class="efficiency-item">
                <div class="efficiency-label">整改效率</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.rectificationEfficiency || '-' }}
                  <span class="efficiency-unit">%</span>
                </div>
              </div>
              <div class="efficiency-item">
                <div class="efficiency-label">问题发现率</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.issueDiscoveryRate || '-' }}
                  <span class="efficiency-unit">%</span>
                </div>
              </div>
              <div class="efficiency-item">
                <div class="efficiency-label">复查通过率</div>
                <div class="efficiency-value">
                  {{ efficiencyStats.recheckPassRate || '-' }}
                  <span class="efficiency-unit">%</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 告警通知 -->
    <div class="alert-section">
      <el-card class="alert-card">
        <div slot="header" class="card-header">
          <span>告警通知</span>
          <el-badge :value="alertList.length" class="item">
            <el-button type="text" @click="viewAllAlerts">查看全部</el-button>
          </el-badge>
        </div>
        <div class="alert-list">
          <div
            v-for="alert in alertList.slice(0, 5)"
            :key="alert.id"
            class="alert-item"
            :class="alert.level"
          >
            <div class="alert-icon">
              <i :class="getAlertIcon(alert.level)"></i>
            </div>
            <div class="alert-content">
              <div class="alert-title">{{ alert.title }}</div>
              <div class="alert-description">{{ alert.description }}</div>
              <div class="alert-time">{{ formatDate(alert.createTime) }}</div>
            </div>
            <div class="alert-actions">
              <el-button type="text" size="mini" @click="handleAlert(alert)">
                处理
              </el-button>
            </div>
          </div>
          <div v-if="alertList.length === 0" class="no-alerts">
            暂无告警信息
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getComplianceOverview,
  countCompliancesByCheckStatus,
  countCompliancesByComplianceStatus,
  countCompliancesByRiskLevel,
  getCheckTrend,
  getComplianceScoreTrend,
  getCheckRanking,
  getCheckEfficiencyStats
} from '@/api/managementAccountant/ts/taxCompliance'

export default {
  name: 'TaxComplianceDashboard',
  data() {
    return {
      loading: false,
      overviewStats: {},
      trendPeriod: 'month',
      scoreTrendDateRange: [],
      rankingType: 'complianceScore',
      rankingData: [],
      efficiencyStats: {},
      alertList: [
        {
          id: 1,
          level: 'high',
          title: '高风险检查超期',
          description: '税务合规检查TC202501270001已超期3天未完成',
          createTime: new Date()
        },
        {
          id: 2,
          level: 'medium',
          title: '整改计划即将到期',
          description: '5个整改计划将在3天内到期，请及时跟进',
          createTime: new Date()
        },
        {
          id: 3,
          level: 'low',
          title: '合规评分下降',
          description: '本月平均合规评分较上月下降5分',
          createTime: new Date()
        }
      ],
      // 图表实例
      checkStatusChart: null,
      complianceStatusChart: null,
      trendChart: null,
      riskChart: null,
      scoreTrendChart: null
    }
  },
  mounted() {
    this.initCharts()
    this.loadData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadOverviewStats(),
          this.loadChartData(),
          this.loadRankingData(),
          this.loadEfficiencyStats()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    async loadOverviewStats() {
      try {
        const response = await getComplianceOverview()
        if (response.success) {
          this.overviewStats = response.data || {}
        }
      } catch (error) {
        console.error('加载概览统计失败:', error)
      }
    },

    async loadChartData() {
      try {
        const [checkStatus, complianceStatus, riskLevel, trend, scoreTrend] = await Promise.all([
          countCompliancesByCheckStatus(),
          countCompliancesByComplianceStatus(),
          countCompliancesByRiskLevel(),
          getCheckTrend(this.getTrendStartDate(), new Date(), 'day'),
          getComplianceScoreTrend(this.getScoreTrendStartDate(), new Date(), 'day')
        ])

        if (checkStatus.success) {
          this.renderCheckStatusChart(checkStatus.data)
        }
        if (complianceStatus.success) {
          this.renderComplianceStatusChart(complianceStatus.data)
        }
        if (riskLevel.success) {
          this.renderRiskChart(riskLevel.data)
        }
        if (trend.success) {
          this.renderTrendChart(trend.data)
        }
        if (scoreTrend.success) {
          this.renderScoreTrendChart(scoreTrend.data)
        }
      } catch (error) {
        console.error('加载图表数据失败:', error)
      }
    },

    async loadRankingData() {
      try {
        const response = await getCheckRanking(this.rankingType, 10)
        if (response.success) {
          this.rankingData = response.data || []
        }
      } catch (error) {
        console.error('加载排行榜数据失败:', error)
      }
    },

    async loadEfficiencyStats() {
      try {
        const response = await getCheckEfficiencyStats()
        if (response.success) {
          this.efficiencyStats = response.data || {}
        }
      } catch (error) {
        console.error('加载效率统计失败:', error)
      }
    },

    initCharts() {
      this.$nextTick(() => {
        this.checkStatusChart = echarts.init(this.$refs.checkStatusChart)
        this.complianceStatusChart = echarts.init(this.$refs.complianceStatusChart)
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.riskChart = echarts.init(this.$refs.riskChart)
        this.scoreTrendChart = echarts.init(this.$refs.scoreTrendChart)

        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },

    destroyCharts() {
      if (this.checkStatusChart) {
        this.checkStatusChart.dispose()
      }
      if (this.complianceStatusChart) {
        this.complianceStatusChart.dispose()
      }
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      if (this.riskChart) {
        this.riskChart.dispose()
      }
      if (this.scoreTrendChart) {
        this.scoreTrendChart.dispose()
      }
      window.removeEventListener('resize', this.handleResize)
    },

    handleResize() {
      this.$nextTick(() => {
        if (this.checkStatusChart) this.checkStatusChart.resize()
        if (this.complianceStatusChart) this.complianceStatusChart.resize()
        if (this.trendChart) this.trendChart.resize()
        if (this.riskChart) this.riskChart.resize()
        if (this.scoreTrendChart) this.scoreTrendChart.resize()
      })
    },

    renderCheckStatusChart(data) {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '检查状态',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data.map(item => ({
              value: item.count,
              name: this.formatCheckStatusName(item.status)
            }))
          }
        ],
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      }
      this.checkStatusChart.setOption(option)
    },

    renderComplianceStatusChart(data) {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '合规状态',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
            data: data.map(item => ({
              value: item.count,
              name: this.formatComplianceStatusName(item.status)
            }))
          }
        ],
        color: ['#909399', '#67C23A', '#E6A23C', '#F56C6C']
      }
      this.complianceStatusChart.setOption(option)
    },

    renderTrendChart(data) {
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增检查', '完成检查']
        },
        xAxis: {
          type: 'category',
          data: data.map(item => this.formatTrendDate(item.date))
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增检查',
            type: 'line',
            data: data.map(item => item.newCount || 0),
            smooth: true
          },
          {
            name: '完成检查',
            type: 'line',
            data: data.map(item => item.completedCount || 0),
            smooth: true
          }
        ],
        color: ['#409EFF', '#67C23A']
      }
      this.trendChart.setOption(option)
    },

    renderRiskChart(data) {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '风险等级',
            type: 'pie',
            radius: '70%',
            center: ['60%', '50%'],
            data: data.map(item => ({
              value: item.count,
              name: this.formatRiskLevelName(item.level)
            }))
          }
        ],
        color: ['#67C23A', '#E6A23C', '#F56C6C', '#FF4D4F']
      }
      this.riskChart.setOption(option)
    },

    renderScoreTrendChart(data) {
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['平均合规评分', '平均风险评分']
        },
        xAxis: {
          type: 'category',
          data: data.map(item => this.formatTrendDate(item.date))
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100
        },
        series: [
          {
            name: '平均合规评分',
            type: 'line',
            data: data.map(item => item.avgComplianceScore || 0),
            smooth: true,
            areaStyle: {
              opacity: 0.3
            }
          },
          {
            name: '平均风险评分',
            type: 'line',
            data: data.map(item => item.avgRiskScore || 0),
            smooth: true,
            areaStyle: {
              opacity: 0.3
            }
          }
        ],
        color: ['#409EFF', '#F56C6C']
      }
      this.scoreTrendChart.setOption(option)
    },

    // 刷新方法
    refreshCharts() {
      this.loadChartData()
    },

    refreshCheckStatusChart() {
      this.loadChartData()
    },

    refreshComplianceStatusChart() {
      this.loadChartData()
    },

    refreshTrendChart() {
      this.loadChartData()
    },

    refreshRiskChart() {
      this.loadChartData()
    },

    refreshScoreTrendChart() {
      this.loadChartData()
    },

    refreshRanking() {
      this.loadRankingData()
    },

    refreshEfficiencyStats() {
      this.loadEfficiencyStats()
    },

    // 工具方法
    getCompletionRate() {
      const total = this.overviewStats.totalCount || 0
      const completed = this.overviewStats.completedCount || 0
      return total > 0 ? Math.round((completed / total) * 100) : 0
    },

    getComplianceRate() {
      const total = this.overviewStats.totalCount || 0
      const compliant = this.overviewStats.compliantCount || 0
      return total > 0 ? Math.round((compliant / total) * 100) : 0
    },

    getRiskRate() {
      const total = this.overviewStats.totalCount || 0
      const highRisk = this.overviewStats.highRiskCount || 0
      return total > 0 ? Math.round((highRisk / total) * 100) : 0
    },

    getTrendStartDate() {
      const now = new Date()
      switch (this.trendPeriod) {
        case 'week':
          return new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        case 'month':
          return new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
        case 'quarter':
          return new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
        default:
          return new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
      }
    },

    getScoreTrendStartDate() {
      if (this.scoreTrendDateRange && this.scoreTrendDateRange.length === 2) {
        return this.scoreTrendDateRange[0]
      }
      return new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)
    },

    formatCheckStatusName(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '进行中',
        'PAUSED': '已暂停',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'FAILED': '失败'
      }
      return statusMap[status] || status
    },

    formatComplianceStatusName(status) {
      const statusMap = {
        'PENDING': '待检查',
        'COMPLIANT': '合规',
        'PARTIALLY_COMPLIANT': '部分合规',
        'NON_COMPLIANT': '不合规'
      }
      return statusMap[status] || status
    },

    formatRiskLevelName(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中等风险',
        'HIGH': '高风险',
        'CRITICAL': '严重风险'
      }
      return levelMap[level] || level
    },

    formatTrendDate(date) {
      return this.$moment(date).format('MM-DD')
    },

    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return ''
    },

    formatRankingValue(value, type) {
      switch (type) {
        case 'complianceScore':
          return `${value}分`
        case 'checkCount':
          return `${value}个`
        case 'completionRate':
          return `${value}%`
        default:
          return value
      }
    },

    getAlertIcon(level) {
      const iconMap = {
        'high': 'el-icon-warning',
        'medium': 'el-icon-info',
        'low': 'el-icon-question'
      }
      return iconMap[level] || 'el-icon-info'
    },

    viewAllAlerts() {
      this.$message.info('查看全部告警功能开发中...')
    },

    handleAlert(alert) {
      this.$message.info(`处理告警: ${alert.title}`)
    },

    formatDate(date) {
      if (!date) return '-'
      return this.$moment(date).format('MM-DD HH:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-compliance-dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .overview-stats {
    margin-bottom: 20px;

    .stat-card {
      background: white;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      transition: all 0.3s;
      position: relative;
      overflow: hidden;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
      }

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
      }

      &.total::before {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.completed::before {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.compliant::before {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.high-risk::before {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
      }

      .stat-icon {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;

        i {
          font-size: 28px;
          color: white;
        }
      }

      &.total .stat-icon {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.completed .stat-icon {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.compliant .stat-icon {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.high-risk .stat-icon {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
      }

      .stat-content {
        flex: 1;

        .stat-number {
          font-size: 32px;
          font-weight: 700;
          color: #303133;
          line-height: 1;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 16px;
          color: #606266;
          margin-bottom: 4px;
        }

        .stat-trend {
          display: flex;
          align-items: center;
          font-size: 12px;

          .trend-text {
            color: #909399;
            margin-right: 4px;
          }

          .trend-value {
            font-weight: 600;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .chart-section {
    margin-bottom: 20px;

    .chart-card {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border-radius: 8px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-weight: 600;

        .header-controls {
          display: flex;
          align-items: center;
          gap: 8px;
        }
      }

      .chart-container {
        height: 300px;
        width: 100%;

        &.large {
          height: 400px;
        }
      }
    }
  }

  .ranking-section {
    margin-bottom: 20px;

    .ranking-card {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border-radius: 8px;

      .ranking-list {
        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          .ranking-number {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            font-size: 14px;
            margin-right: 12px;
            background: #f5f7fa;
            color: #909399;

            &.first {
              background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
              color: white;
            }

            &.second {
              background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
              color: white;
            }

            &.third {
              background: linear-gradient(135deg, #cd7f32 0%, #daa520 100%);
              color: white;
            }
          }

          .ranking-info {
            flex: 1;

            .ranking-name {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 2px;
            }

            .ranking-meta {
              font-size: 12px;
              color: #909399;
            }
          }

          .ranking-value {
            font-size: 16px;
            font-weight: 600;
            color: #409eff;
          }
        }
      }
    }

    .efficiency-card {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border-radius: 8px;

      .efficiency-stats {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;

        .efficiency-item {
          text-align: center;
          padding: 16px;
          background: #f8f9fa;
          border-radius: 8px;

          .efficiency-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 8px;
          }

          .efficiency-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;

            .efficiency-unit {
              font-size: 12px;
              color: #909399;
              margin-left: 2px;
            }
          }
        }
      }
    }
  }

  .alert-section {
    .alert-card {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border-radius: 8px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-weight: 600;
      }

      .alert-list {
        .alert-item {
          display: flex;
          align-items: flex-start;
          padding: 16px 0;
          border-bottom: 1px solid #f0f0f0;

          &:last-child {
            border-bottom: none;
          }

          .alert-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 12px;

            i {
              font-size: 18px;
              color: white;
            }
          }

          &.high .alert-icon {
            background: #f56c6c;
          }

          &.medium .alert-icon {
            background: #e6a23c;
          }

          &.low .alert-icon {
            background: #909399;
          }

          .alert-content {
            flex: 1;

            .alert-title {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            .alert-description {
              font-size: 12px;
              color: #606266;
              line-height: 1.4;
              margin-bottom: 4px;
            }

            .alert-time {
              font-size: 11px;
              color: #c0c4cc;
            }
          }

          .alert-actions {
            margin-left: 12px;
          }
        }

        .no-alerts {
          text-align: center;
          color: #c0c4cc;
          font-size: 14px;
          padding: 40px 0;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .tax-compliance-dashboard {
    .overview-stats {
      .el-col {
        margin-bottom: 16px;
      }
    }

    .chart-section {
      .el-col {
        margin-bottom: 16px;
      }
    }

    .ranking-section {
      .el-col {
        margin-bottom: 16px;
      }

      .efficiency-stats {
        grid-template-columns: 1fr;
      }
    }
  }
}

@media (max-width: 768px) {
  .tax-compliance-dashboard {
    padding: 10px;

    .overview-stats {
      .stat-card {
        padding: 16px;

        .stat-icon {
          width: 48px;
          height: 48px;
          margin-right: 12px;

          i {
            font-size: 20px;
          }
        }

        .stat-content {
          .stat-number {
            font-size: 24px;
          }

          .stat-label {
            font-size: 14px;
          }
        }
      }
    }

    .chart-section {
      .chart-container {
        height: 250px;

        &.large {
          height: 300px;
        }
      }
    }
  }
}
</style>
