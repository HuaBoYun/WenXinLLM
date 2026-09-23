<template>
  <div class="intelligent-audit-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.totalAudits || 0 }}</div>
              <div class="stat-label">总审核数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon in-progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.inProgressAudits || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.completedAudits || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon high-risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.highRiskAudits || 0 }}</div>
              <div class="stat-label">高风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 审核状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>审核状态分布</span>
            <el-button type="text" @click="refreshStatusChart">刷新</el-button>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 风险等级分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>风险等级分布</span>
            <el-button type="text" @click="refreshRiskChart">刷新</el-button>
          </div>
          <div ref="riskChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 审核趋势 -->
      <el-col :span="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>审核趋势</span>
            <div class="header-controls">
              <el-date-picker
                v-model="trendTimeRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                @change="refreshTrendChart"
                style="margin-right: 10px"
              />
              <el-button type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container trend-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待处理事项和排行榜 -->
    <el-row :gutter="20" class="info-row">
      <!-- 待处理事项 -->
      <el-col :span="12">
        <el-card class="info-card" shadow="never">
          <div slot="header" class="card-header">
            <span>待处理事项</span>
            <el-button type="text" @click="refreshPendingItems">刷新</el-button>
          </div>
          <div v-loading="pendingLoading" class="pending-items">
            <div v-if="pendingItems.length === 0" class="empty-state">
              <i class="el-icon-document"></i>
              <p>暂无待处理事项</p>
            </div>
            <div v-else>
              <div v-for="item in pendingItems" :key="item.type" class="pending-item">
                <div class="item-icon">
                  <i :class="getPendingItemIcon(item.type)"></i>
                </div>
                <div class="item-content">
                  <div class="item-title">{{ getPendingItemTitle(item.type) }}</div>
                  <div class="item-count">{{ item.count }} 项</div>
                </div>
                <div class="item-action">
                  <el-button size="mini" type="text" @click="handlePendingItemClick(item.type)">
                    查看
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 审核人员排行榜 -->
      <el-col :span="12">
        <el-card class="info-card" shadow="never">
          <div slot="header" class="card-header">
            <span>审核人员排行榜</span>
            <el-button type="text" @click="refreshRanking">刷新</el-button>
          </div>
          <div v-loading="rankingLoading" class="ranking-list">
            <div v-if="rankingList.length === 0" class="empty-state">
              <i class="el-icon-trophy"></i>
              <p>暂无排行数据</p>
            </div>
            <div v-else>
              <div v-for="(item, index) in rankingList" :key="item.auditorId" class="ranking-item">
                <div class="ranking-number" :class="getRankingClass(index)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="ranking-name">{{ item.auditorName }}</div>
                  <div class="ranking-dept">{{ item.auditDeptName }}</div>
                </div>
                <div class="ranking-score">
                  {{ item.completedCount }} 项
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="20" class="action-row">
      <el-col :span="24">
        <el-card class="action-card" shadow="never">
          <div slot="header" class="card-header">
            <span>快捷操作</span>
          </div>
          <div class="quick-actions">
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAudit">
              新建审核
            </el-button>
            <el-button type="success" icon="el-icon-cpu" @click="handleBatchIntelligentAudit">
              批量智能审核
            </el-button>
            <el-button type="warning" icon="el-icon-warning" @click="handleViewHighRisk">
              查看高风险审核
            </el-button>
            <el-button type="info" icon="el-icon-document" @click="handleGenerateReport">
              生成报告
            </el-button>
            <el-button type="primary" icon="el-icon-setting" @click="handleAuditSettings">
              审核设置
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getAuditStatistics,
  getAuditStatusDistribution,
  getRiskLevelDistribution,
  getAuditTrend,
  getPendingAudits,
  getNeedReview,
  getNeedFollowUp,
  getHighRiskAudits,
  getAnomalyAudits,
  getAuditRanking,
  generateAuditReport
} from '@/api/managementAccountant/ss/intelligentAudit'

export default {
  name: 'IntelligentAuditDashboard',
  data() {
    return {
      loading: false,
      pendingLoading: false,
      rankingLoading: false,
      statistics: {},
      statusChart: null,
      riskChart: null,
      trendChart: null,
      trendTimeRange: [],
      pendingItems: [],
      rankingList: []
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    },
    defaultTimeRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30) // 30天前
      return [
        start.toISOString().split('T')[0],
        end.toISOString().split('T')[0]
      ]
    }
  },
  mounted() {
    this.trendTimeRange = this.defaultTimeRange
    this.initCharts()
    this.loadData()
  },
  beforeDestroy() {
    if (this.statusChart) {
      this.statusChart.dispose()
    }
    if (this.riskChart) {
      this.riskChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.riskChart = echarts.init(this.$refs.riskChart)
        this.trendChart = echarts.init(this.$refs.trendChart)
        
        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },

    // 处理窗口大小变化
    handleResize() {
      if (this.statusChart) this.statusChart.resize()
      if (this.riskChart) this.riskChart.resize()
      if (this.trendChart) this.trendChart.resize()
    },

    // 加载数据
    async loadData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadStatusDistribution(),
        this.loadRiskDistribution(),
        this.loadTrendData(),
        this.loadPendingItems(),
        this.loadRanking()
      ])
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const params = {
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          tenantId: this.tenantId
        }
        const response = await getAuditStatistics(params)
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 加载状态分布数据
    async loadStatusDistribution() {
      try {
        const params = {
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          tenantId: this.tenantId
        }
        const response = await getAuditStatusDistribution(params)
        if (response.success) {
          this.renderStatusChart(response.data)
        }
      } catch (error) {
        console.error('加载状态分布数据失败:', error)
      }
    },

    // 加载风险分布数据
    async loadRiskDistribution() {
      try {
        const params = {
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          tenantId: this.tenantId
        }
        const response = await getRiskLevelDistribution(params)
        if (response.success) {
          this.renderRiskChart(response.data)
        }
      } catch (error) {
        console.error('加载风险分布数据失败:', error)
      }
    },

    // 加载趋势数据
    async loadTrendData() {
      try {
        const params = {
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          tenantId: this.tenantId
        }
        const response = await getAuditTrend(params)
        if (response.success) {
          this.renderTrendChart(response.data)
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },

    // 加载待处理事项
    async loadPendingItems() {
      this.pendingLoading = true
      try {
        const [pendingRes, reviewRes, followUpRes, highRiskRes, anomalyRes] = await Promise.all([
          getPendingAudits(this.tenantId),
          getNeedReview(this.tenantId),
          getNeedFollowUp(this.tenantId),
          getHighRiskAudits(this.tenantId),
          getAnomalyAudits(this.tenantId)
        ])

        this.pendingItems = [
          { type: 'pending', count: pendingRes.success ? pendingRes.data.length : 0 },
          { type: 'review', count: reviewRes.success ? reviewRes.data.length : 0 },
          { type: 'followUp', count: followUpRes.success ? followUpRes.data.length : 0 },
          { type: 'highRisk', count: highRiskRes.success ? highRiskRes.data.length : 0 },
          { type: 'anomaly', count: anomalyRes.success ? anomalyRes.data.length : 0 }
        ].filter(item => item.count > 0)
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      } finally {
        this.pendingLoading = false
      }
    },

    // 加载排行榜
    async loadRanking() {
      this.rankingLoading = true
      try {
        const params = {
          rankingType: 'COMPLETED_COUNT',
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          limit: 10,
          tenantId: this.tenantId
        }
        const response = await getAuditRanking(params)
        if (response.success) {
          this.rankingList = response.data
        }
      } catch (error) {
        console.error('加载排行榜失败:', error)
      } finally {
        this.rankingLoading = false
      }
    },

    // 渲染状态分布图表
    renderStatusChart(data) {
      if (!this.statusChart) return

      const statusMap = {
        'DRAFT': '草稿',
        'IN_PROGRESS': '进行中',
        'PAUSED': '已暂停',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REVIEWED': '已复核'
      }

      const chartData = data.map(item => ({
        name: statusMap[item.status] || item.status,
        value: item.count
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '审核状态',
            type: 'pie',
            radius: '50%',
            data: chartData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      this.statusChart.setOption(option)
    },

    // 渲染风险分布图表
    renderRiskChart(data) {
      if (!this.riskChart) return

      const riskMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }

      const chartData = data.map(item => ({
        name: riskMap[item.riskLevel] || item.riskLevel,
        value: item.count
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '风险等级',
            type: 'pie',
            radius: '50%',
            data: chartData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            color: ['#67C23A', '#E6A23C', '#F56C6C', '#F56C6C']
          }
        ]
      }

      this.riskChart.setOption(option)
    },

    // 渲染趋势图表
    renderTrendChart(data) {
      if (!this.trendChart) return

      const dates = data.map(item => item.date)
      const totalCounts = data.map(item => item.totalCount)
      const completedCounts = data.map(item => item.completedCount)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总审核数', '已完成数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '总审核数',
            type: 'line',
            stack: 'Total',
            data: totalCounts,
            smooth: true
          },
          {
            name: '已完成数',
            type: 'line',
            stack: 'Total',
            data: completedCounts,
            smooth: true
          }
        ]
      }

      this.trendChart.setOption(option)
    },

    // 获取待处理事项图标
    getPendingItemIcon(type) {
      const iconMap = {
        'pending': 'el-icon-time',
        'review': 'el-icon-view',
        'followUp': 'el-icon-phone',
        'highRisk': 'el-icon-warning',
        'anomaly': 'el-icon-warning-outline'
      }
      return iconMap[type] || 'el-icon-document'
    },

    // 获取待处理事项标题
    getPendingItemTitle(type) {
      const titleMap = {
        'pending': '待处理审核',
        'review': '待复核审核',
        'followUp': '待跟进审核',
        'highRisk': '高风险审核',
        'anomaly': '异常审核'
      }
      return titleMap[type] || '未知类型'
    },

    // 获取排行榜样式类
    getRankingClass(index) {
      if (index === 0) return 'top1'
      if (index === 1) return 'top2'
      if (index === 2) return 'top3'
      return ''
    },

    // 刷新图表
    refreshStatusChart() {
      this.loadStatusDistribution()
    },

    refreshRiskChart() {
      this.loadRiskDistribution()
    },

    refreshTrendChart() {
      this.loadTrendData()
      this.loadStatistics()
    },

    refreshPendingItems() {
      this.loadPendingItems()
    },

    refreshRanking() {
      this.loadRanking()
    },

    // 处理待处理事项点击
    handlePendingItemClick(type) {
      const routeMap = {
        'pending': { name: 'IntelligentAuditList', query: { auditStatus: 'DRAFT' } },
        'review': { name: 'IntelligentAuditList', query: { auditStatus: 'COMPLETED' } },
        'followUp': { name: 'IntelligentAuditList', query: { followUpStatus: 'REQUIRED' } },
        'highRisk': { name: 'IntelligentAuditList', query: { riskLevel: 'HIGH' } },
        'anomaly': { name: 'IntelligentAuditList', query: { hasAnomaly: true } }
      }

      const route = routeMap[type]
      if (route) {
        this.$router.push(route)
      }
    },

    // 快捷操作
    handleCreateAudit() {
      this.$router.push({ name: 'IntelligentAuditCreate' })
    },

    handleBatchIntelligentAudit() {
      this.$router.push({ name: 'IntelligentAuditList' })
    },

    handleViewHighRisk() {
      this.$router.push({
        name: 'IntelligentAuditList',
        query: { riskLevel: 'HIGH' }
      })
    },

    async handleGenerateReport() {
      try {
        this.loading = true
        const params = {
          startTime: this.trendTimeRange[0] + ' 00:00:00',
          endTime: this.trendTimeRange[1] + ' 23:59:59',
          reportType: 'COMPREHENSIVE',
          tenantId: this.tenantId
        }

        const response = await generateAuditReport(params)
        if (response.success) {
          this.$message.success('报告生成成功')
          // 这里可以处理报告下载或显示
        }
      } catch (error) {
        this.$message.error('生成报告失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleAuditSettings() {
      this.$router.push({ name: 'IntelligentAuditSettings' })
    }
  }
}
</script>

<style scoped>
.intelligent-audit-dashboard {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.in-progress {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.high-risk {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.chart-row,
.info-row,
.action-row {
  margin-bottom: 20px;
}

.chart-card,
.info-card,
.action-card {
  height: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-controls {
  display: flex;
  align-items: center;
}

.chart-container {
  height: 320px;
}

.trend-chart {
  height: 320px;
}

.pending-items,
.ranking-list {
  height: 320px;
  overflow-y: auto;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 10px;
}

.pending-item,
.ranking-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.pending-item:last-child,
.ranking-item:last-child {
  border-bottom: none;
}

.item-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 18px;
  color: #409eff;
}

.item-content {
  flex: 1;
}

.item-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.item-count {
  font-size: 12px;
  color: #909399;
}

.ranking-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-weight: bold;
  color: white;
  background: #909399;
}

.ranking-number.top1 {
  background: #ffd700;
}

.ranking-number.top2 {
  background: #c0c0c0;
}

.ranking-number.top3 {
  background: #cd7f32;
}

.ranking-content {
  flex: 1;
}

.ranking-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.ranking-dept {
  font-size: 12px;
  color: #909399;
}

.ranking-score {
  font-weight: bold;
  color: #409eff;
}

.quick-actions {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.quick-actions .el-button {
  flex: 1;
  min-width: 120px;
}
</style>
