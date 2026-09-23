<template>
  <div class="service-portal-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="24" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">门户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
              <div class="stat-label">活跃门户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon maintenance">
              <i class="el-icon-setting"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.maintenanceCount || 0 }}</div>
              <div class="stat-label">维护中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon archived">
              <i class="el-icon-box"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.archivedCount || 0 }}</div>
              <div class="stat-label">已归档</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="24" class="charts-row">
      <el-col :span="12">
        <el-card title="门户类型分布" class="chart-card">
          <div id="typeDistributionChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="门户状态分布" class="chart-card">
          <div id="statusDistributionChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="charts-row">
      <el-col :span="24">
        <el-card title="门户使用趋势" class="chart-card">
          <div class="chart-toolbar">
            <el-date-picker
              v-model="trendDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              size="small"
              @change="handleTrendDateChange"
            />
            <el-button size="small" @click="refreshTrendChart">刷新</el-button>
          </div>
          <div id="usageTrendChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 排行榜和待处理事项 -->
    <el-row :gutter="24" class="info-row">
      <el-col :span="12">
        <el-card title="访问量排行榜" class="ranking-card">
          <el-table :data="accessRanking" size="small" :show-header="false">
            <el-table-column width="50" align="center">
              <template slot-scope="scope">
                <div class="ranking-number" :class="getRankingClass(scope.$index)">
                  {{ scope.$index + 1 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="portalName" label="门户名称" show-overflow-tooltip />
            <el-table-column prop="accessCount" label="访问量" width="80" align="right">
              <template slot-scope="scope">
                <span class="access-count">{{ formatAccessCount(scope.row.accessCount) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="待处理事项" class="pending-card">
          <div class="pending-list">
            <div v-for="item in pendingItems" :key="item.id" class="pending-item">
              <div class="pending-icon" :class="item.type">
                <i :class="item.icon"></i>
              </div>
              <div class="pending-content">
                <div class="pending-title">{{ item.title }}</div>
                <div class="pending-desc">{{ item.description }}</div>
              </div>
              <div class="pending-action">
                <el-button type="text" size="small" @click="handlePendingAction(item)">
                  处理
                </el-button>
              </div>
            </div>
            <div v-if="pendingItems.length === 0" class="no-pending">
              <i class="el-icon-check"></i>
              <span>暂无待处理事项</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 性能监控 -->
    <el-row :gutter="24" class="performance-row">
      <el-col :span="24">
        <el-card title="性能监控" class="performance-card">
          <el-row :gutter="24">
            <el-col :span="6">
              <div class="performance-metric">
                <div class="metric-value">{{ performanceMetrics.avgResponseTime || 0 }}ms</div>
                <div class="metric-label">平均响应时间</div>
                <div class="metric-trend" :class="getPerformanceTrend('responseTime')">
                  <i :class="getPerformanceTrendIcon('responseTime')"></i>
                  {{ getPerformanceTrendText('responseTime') }}
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="performance-metric">
                <div class="metric-value">{{ performanceMetrics.throughput || 0 }}/s</div>
                <div class="metric-label">吞吐量</div>
                <div class="metric-trend" :class="getPerformanceTrend('throughput')">
                  <i :class="getPerformanceTrendIcon('throughput')"></i>
                  {{ getPerformanceTrendText('throughput') }}
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="performance-metric">
                <div class="metric-value">{{ (performanceMetrics.errorRate * 100).toFixed(2) }}%</div>
                <div class="metric-label">错误率</div>
                <div class="metric-trend" :class="getPerformanceTrend('errorRate')">
                  <i :class="getPerformanceTrendIcon('errorRate')"></i>
                  {{ getPerformanceTrendText('errorRate') }}
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="performance-metric">
                <div class="metric-value">{{ performanceMetrics.availability || 0 }}%</div>
                <div class="metric-label">可用性</div>
                <div class="metric-trend" :class="getPerformanceTrend('availability')">
                  <i :class="getPerformanceTrendIcon('availability')"></i>
                  {{ getPerformanceTrendText('availability') }}
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 告警信息 -->
    <el-row :gutter="24" class="alerts-row">
      <el-col :span="24">
        <el-card title="告警信息" class="alerts-card">
          <el-table :data="alerts" size="small">
            <el-table-column prop="alertTime" label="告警时间" width="160">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.alertTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="alertLevel" label="告警级别" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getAlertLevelColor(scope.row.alertLevel)" size="small">
                  {{ formatAlertLevel(scope.row.alertLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="portalName" label="门户名称" width="150" show-overflow-tooltip />
            <el-table-column prop="alertType" label="告警类型" width="120">
              <template slot-scope="scope">
                <el-tag size="small">{{ formatAlertType(scope.row.alertType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="alertMessage" label="告警信息" show-overflow-tooltip />
            <el-table-column prop="alertStatus" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.alertStatus === 'RESOLVED' ? 'success' : 'warning'" size="small">
                  {{ scope.row.alertStatus === 'RESOLVED' ? '已解决' : '待处理' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.alertStatus !== 'RESOLVED'"
                  type="text"
                  size="small"
                  @click="handleResolveAlert(scope.row)"
                >
                  标记解决
                </el-button>
                <el-button type="text" size="small" @click="handleViewAlert(scope.row)">
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getStatistics,
  getPortalTypeDistribution,
  getPortalStatusDistribution,
  getPortalUsageTrend,
  getPortalAccessRanking,
  getPortalPendingItems,
  getPortalPerformanceMetrics,
  getPortalAlerts,
  utils
} from '@/api/managementAccountant/ss/servicePortal'
import * as echarts from 'echarts'

export default {
  name: 'ServicePortalDashboard',
  data() {
    return {
      loading: false,
      statistics: {
        totalCount: 0,
        activeCount: 0,
        inactiveCount: 0,
        maintenanceCount: 0,
        archivedCount: 0
      },
      typeDistribution: [],
      statusDistribution: [],
      usageTrend: [],
      accessRanking: [],
      pendingItems: [],
      performanceMetrics: {
        avgResponseTime: 0,
        throughput: 0,
        errorRate: 0,
        availability: 0
      },
      alerts: [],
      trendDateRange: [],
      charts: {
        typeDistribution: null,
        statusDistribution: null,
        usageTrend: null
      }
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  mounted() {
    this.initDateRange()
    this.loadDashboardData()
    this.initCharts()
    
    // 设置定时刷新
    this.refreshTimer = setInterval(() => {
      this.loadDashboardData()
    }, 30000) // 30秒刷新一次
  },
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
    // 销毁图表
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    // 初始化日期范围
    initDateRange() {
      const endDate = new Date()
      const startDate = new Date(endDate.getTime() - 30 * 24 * 60 * 60 * 1000) // 30天前
      this.trendDateRange = [startDate, endDate]
    },

    // 加载仪表板数据
    async loadDashboardData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadTypeDistribution(),
        this.loadStatusDistribution(),
        this.loadUsageTrend(),
        this.loadAccessRanking(),
        this.loadPendingItems(),
        this.loadPerformanceMetrics(),
        this.loadAlerts()
      ])
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const [startTime, endTime] = this.trendDateRange
        const response = await getStatistics(
          startTime.toISOString(),
          endTime.toISOString(),
          this.tenantId
        )
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.warn('加载统计数据失败:', error)
      }
    },

    // 加载类型分布
    async loadTypeDistribution() {
      try {
        const response = await getPortalTypeDistribution(this.tenantId)
        if (response.success) {
          this.typeDistribution = response.data
          this.updateTypeDistributionChart()
        }
      } catch (error) {
        console.warn('加载类型分布失败:', error)
      }
    },

    // 加载状态分布
    async loadStatusDistribution() {
      try {
        const response = await getPortalStatusDistribution(this.tenantId)
        if (response.success) {
          this.statusDistribution = response.data
          this.updateStatusDistributionChart()
        }
      } catch (error) {
        console.warn('加载状态分布失败:', error)
      }
    },

    // 加载使用趋势
    async loadUsageTrend() {
      try {
        const [startTime, endTime] = this.trendDateRange
        const response = await getPortalUsageTrend(
          startTime.toISOString(),
          endTime.toISOString(),
          this.tenantId
        )
        if (response.success) {
          this.usageTrend = response.data
          this.updateUsageTrendChart()
        }
      } catch (error) {
        console.warn('加载使用趋势失败:', error)
      }
    },

    // 加载访问排行
    async loadAccessRanking() {
      try {
        const response = await getPortalAccessRanking(10, this.tenantId)
        if (response.success) {
          this.accessRanking = response.data
        }
      } catch (error) {
        console.warn('加载访问排行失败:', error)
      }
    },

    // 加载待处理事项
    async loadPendingItems() {
      try {
        const response = await getPortalPendingItems(this.tenantId)
        if (response.success) {
          this.pendingItems = response.data.map(item => ({
            ...item,
            icon: this.getPendingItemIcon(item.type),
            type: this.getPendingItemType(item.type)
          }))
        }
      } catch (error) {
        console.warn('加载待处理事项失败:', error)
      }
    },

    // 加载性能指标
    async loadPerformanceMetrics() {
      try {
        const response = await getPortalPerformanceMetrics(this.tenantId)
        if (response.success && response.data.length > 0) {
          const metrics = response.data[0]
          this.performanceMetrics = {
            avgResponseTime: metrics.avgResponseTime || 0,
            throughput: metrics.throughput || 0,
            errorRate: metrics.errorRate || 0,
            availability: metrics.availability || 0
          }
        }
      } catch (error) {
        console.warn('加载性能指标失败:', error)
      }
    },

    // 加载告警信息
    async loadAlerts() {
      try {
        const response = await getPortalAlerts('ALL', this.tenantId)
        if (response.success) {
          this.alerts = response.data.slice(0, 10) // 只显示最近10条
        }
      } catch (error) {
        console.warn('加载告警信息失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.charts.typeDistribution = echarts.init(document.getElementById('typeDistributionChart'))
        this.charts.statusDistribution = echarts.init(document.getElementById('statusDistributionChart'))
        this.charts.usageTrend = echarts.init(document.getElementById('usageTrendChart'))
      })
    },

    // 更新类型分布图表
    updateTypeDistributionChart() {
      if (!this.charts.typeDistribution) return

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '门户类型',
          type: 'pie',
          radius: '60%',
          data: this.typeDistribution.map(item => ({
            name: utils.formatPortalType(item.portalType),
            value: item.count
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.charts.typeDistribution.setOption(option)
    },

    // 更新状态分布图表
    updateStatusDistributionChart() {
      if (!this.charts.statusDistribution) return

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '门户状态',
          type: 'pie',
          radius: ['40%', '70%'],
          data: this.statusDistribution.map(item => ({
            name: utils.formatPortalStatus(item.portalStatus).text,
            value: item.count
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.charts.statusDistribution.setOption(option)
    },

    // 更新使用趋势图表
    updateUsageTrendChart() {
      if (!this.charts.usageTrend) return

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['访问量', '独立用户']
        },
        xAxis: {
          type: 'category',
          data: this.usageTrend.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '访问量',
            type: 'line',
            data: this.usageTrend.map(item => item.accessCount),
            smooth: true
          },
          {
            name: '独立用户',
            type: 'line',
            data: this.usageTrend.map(item => item.uniqueUsers),
            smooth: true
          }
        ]
      }
      this.charts.usageTrend.setOption(option)
    },

    // 趋势日期变化
    handleTrendDateChange() {
      this.loadUsageTrend()
      this.loadStatistics()
    },

    // 刷新趋势图表
    refreshTrendChart() {
      this.loadUsageTrend()
    },

    // 处理待处理事项
    handlePendingAction(item) {
      this.$router.push({
        name: 'ServicePortalList',
        query: { portalId: item.portalId }
      })
    },

    // 解决告警
    handleResolveAlert(alert) {
      this.$confirm('确定要标记此告警为已解决吗？', '确认操作', {
        type: 'warning'
      }).then(() => {
        // 这里应该调用API标记告警为已解决
        alert.alertStatus = 'RESOLVED'
        this.$message.success('告警已标记为解决')
      })
    },

    // 查看告警详情
    handleViewAlert(alert) {
      this.$alert(alert.alertMessage, '告警详情', {
        confirmButtonText: '确定'
      })
    },

    // 获取排行榜样式
    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return ''
    },

    // 获取待处理事项图标
    getPendingItemIcon(type) {
      const iconMap = {
        'MAINTENANCE': 'el-icon-setting',
        'ALERT': 'el-icon-warning',
        'BACKUP': 'el-icon-download',
        'UPDATE': 'el-icon-refresh'
      }
      return iconMap[type] || 'el-icon-info'
    },

    // 获取待处理事项类型
    getPendingItemType(type) {
      const typeMap = {
        'MAINTENANCE': 'warning',
        'ALERT': 'danger',
        'BACKUP': 'info',
        'UPDATE': 'primary'
      }
      return typeMap[type] || 'info'
    },

    // 获取性能趋势
    getPerformanceTrend(metric) {
      // 这里应该根据历史数据计算趋势
      return 'up' // 示例返回
    },

    // 获取性能趋势图标
    getPerformanceTrendIcon(metric) {
      const trend = this.getPerformanceTrend(metric)
      return trend === 'up' ? 'el-icon-top' : 'el-icon-bottom'
    },

    // 获取性能趋势文本
    getPerformanceTrendText(metric) {
      const trend = this.getPerformanceTrend(metric)
      return trend === 'up' ? '上升' : '下降'
    },

    // 获取告警级别颜色
    getAlertLevelColor(level) {
      const colorMap = {
        'CRITICAL': 'danger',
        'HIGH': 'warning',
        'MEDIUM': 'primary',
        'LOW': 'info'
      }
      return colorMap[level] || 'info'
    },

    // 格式化告警级别
    formatAlertLevel(level) {
      const levelMap = {
        'CRITICAL': '严重',
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return levelMap[level] || level
    },

    // 格式化告警类型
    formatAlertType(type) {
      const typeMap = {
        'PERFORMANCE': '性能',
        'SECURITY': '安全',
        'AVAILABILITY': '可用性',
        'ERROR': '错误'
      }
      return typeMap[type] || type
    },

    // 格式化访问量
    formatAccessCount(count) {
      return utils.formatAccessCount(count)
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('MM-DD HH:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
.service-portal-dashboard {
  .stats-row, .charts-row, .info-row, .performance-row, .alerts-row {
    margin-bottom: 24px;
  }

  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        
        i {
          font-size: 24px;
          color: white;
        }
        
        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.maintenance {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.archived {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stat-info {
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
          line-height: 1;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 4px;
        }
      }
    }
  }

  .chart-card {
    .chart-toolbar {
      margin-bottom: 16px;
      display: flex;
      justify-content: flex-end;
      gap: 8px;
    }
  }

  .ranking-card {
    .ranking-number {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      font-weight: bold;
      color: white;
      background: #909399;
      
      &.first {
        background: #f56c6c;
      }
      
      &.second {
        background: #e6a23c;
      }
      
      &.third {
        background: #67c23a;
      }
    }
    
    .access-count {
      font-weight: bold;
      color: #409eff;
    }
  }

  .pending-card {
    .pending-list {
      .pending-item {
        display: flex;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
        }
        
        .pending-icon {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          
          i {
            font-size: 16px;
            color: white;
          }
          
          &.warning {
            background: #e6a23c;
          }
          
          &.danger {
            background: #f56c6c;
          }
          
          &.info {
            background: #909399;
          }
          
          &.primary {
            background: #409eff;
          }
        }
        
        .pending-content {
          flex: 1;
          
          .pending-title {
            font-size: 14px;
            font-weight: bold;
            color: #303133;
          }
          
          .pending-desc {
            font-size: 12px;
            color: #909399;
            margin-top: 2px;
          }
        }
      }
      
      .no-pending {
        text-align: center;
        padding: 40px 0;
        color: #909399;
        
        i {
          font-size: 48px;
          margin-bottom: 8px;
          display: block;
        }
      }
    }
  }

  .performance-card {
    .performance-metric {
      text-align: center;
      
      .metric-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 8px;
      }
      
      .metric-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 4px;
      }
      
      .metric-trend {
        font-size: 12px;
        
        &.up {
          color: #67c23a;
        }
        
        &.down {
          color: #f56c6c;
        }
        
        i {
          margin-right: 2px;
        }
      }
    }
  }
}
</style>
