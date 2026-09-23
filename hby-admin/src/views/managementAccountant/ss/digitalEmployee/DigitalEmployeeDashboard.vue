<template>
  <div class="digital-employee-dashboard">
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">数字员工总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
              <div class="stat-label">活跃数字员工</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon execution">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalExecutions || 0 }}</div>
              <div class="stat-label">总执行次数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon success">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.avgSuccessRate || 0 }}%</div>
              <div class="stat-label">平均成功率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 状态分布饼图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>状态分布</h3>
              <el-button @click="refreshStatusChart" icon="el-icon-refresh" size="mini" circle></el-button>
            </div>
            <div class="chart-content">
              <div ref="statusChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>

        <!-- 类型分布饼图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>类型分布</h3>
              <el-button @click="refreshTypeChart" icon="el-icon-refresh" size="mini" circle></el-button>
            </div>
            <div class="chart-content">
              <div ref="typeChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 执行趋势图 -->
        <el-col :span="24">
          <div class="chart-card">
            <div class="chart-header">
              <h3>执行趋势</h3>
              <div class="chart-controls">
                <el-date-picker
                  v-model="trendDateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="mini"
                  @change="refreshTrendChart"
                />
                <el-button @click="refreshTrendChart" icon="el-icon-refresh" size="mini" circle style="margin-left: 10px;"></el-button>
              </div>
            </div>
            <div class="chart-content">
              <div ref="trendChart" class="chart" style="height: 400px;"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜和告警 -->
    <div class="ranking-alerts-section">
      <el-row :gutter="20">
        <!-- 性能排行榜 -->
        <el-col :span="12">
          <div class="ranking-card">
            <div class="ranking-header">
              <h3>性能排行榜</h3>
              <el-select v-model="rankingType" size="mini" @change="refreshRanking" style="width: 120px;">
                <el-option label="成功率" value="successRate" />
                <el-option label="执行次数" value="executionCount" />
                <el-option label="响应时间" value="responseTime" />
                <el-option label="综合评分" value="overallScore" />
              </el-select>
            </div>
            <div class="ranking-content">
              <div v-if="ranking.length === 0" class="no-data">暂无数据</div>
              <div v-else class="ranking-list">
                <div 
                  v-for="(item, index) in ranking" 
                  :key="item.robotId"
                  class="ranking-item"
                  :class="{ 'top-three': index < 3 }"
                >
                  <div class="ranking-number">
                    <span :class="getRankingClass(index)">{{ index + 1 }}</span>
                  </div>
                  <div class="ranking-info">
                    <div class="robot-name">{{ item.robotName }}</div>
                    <div class="robot-type">{{ formatRobotType(item.robotType) }}</div>
                  </div>
                  <div class="ranking-value">
                    {{ formatRankingValue(item, rankingType) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 告警信息 -->
        <el-col :span="12">
          <div class="alerts-card">
            <div class="alerts-header">
              <h3>告警信息</h3>
              <el-badge :value="alerts.length" :max="99" class="alerts-badge">
                <el-button @click="refreshAlerts" icon="el-icon-refresh" size="mini" circle></el-button>
              </el-badge>
            </div>
            <div class="alerts-content">
              <div v-if="alerts.length === 0" class="no-data">暂无告警</div>
              <div v-else class="alerts-list">
                <div 
                  v-for="alert in alerts" 
                  :key="alert.id"
                  class="alert-item"
                  :class="getAlertClass(alert.level)"
                >
                  <div class="alert-icon">
                    <i :class="getAlertIcon(alert.level)"></i>
                  </div>
                  <div class="alert-content">
                    <div class="alert-title">{{ alert.title }}</div>
                    <div class="alert-message">{{ alert.message }}</div>
                    <div class="alert-time">{{ formatDateTime(alert.createTime) }}</div>
                  </div>
                  <div class="alert-actions">
                    <el-button @click="handleAlert(alert)" size="mini" type="text">处理</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 待处理事项 -->
    <div class="pending-section">
      <div class="pending-card">
        <div class="pending-header">
          <h3>待处理事项</h3>
          <el-badge :value="pendingItems.length" :max="99" class="pending-badge">
            <el-button @click="refreshPendingItems" icon="el-icon-refresh" size="mini" circle></el-button>
          </el-badge>
        </div>
        <div class="pending-content">
          <div v-if="pendingItems.length === 0" class="no-data">暂无待处理事项</div>
          <div v-else class="pending-list">
            <el-table :data="pendingItems" style="width: 100%" size="mini">
              <el-table-column prop="robotName" label="数字员工" width="150" />
              <el-table-column prop="taskType" label="任务类型" width="120" />
              <el-table-column prop="priority" label="优先级" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getPriorityType(scope.row.priority)" size="mini">
                    {{ getPriorityText(scope.row.priority) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="描述" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="150">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button @click="handlePendingItem(scope.row)" size="mini" type="primary">处理</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getStatistics,
  getStatusDistribution,
  getTypeDistribution,
  getRanking,
  getAlerts,
  getPendingItems,
  formatRobotType
} from '@/api/managementAccountant/ss/digitalEmployee'

export default {
  name: 'DigitalEmployeeDashboard',
  data() {
    return {
      loading: false,
      // 统计数据
      statistics: {
        totalCount: 0,
        activeCount: 0,
        inactiveCount: 0,
        maintenanceCount: 0,
        retiredCount: 0,
        totalExecutions: 0,
        totalSuccesses: 0,
        totalFailures: 0,
        avgSuccessRate: 0,
        avgResponseTime: 0
      },
      // 图表实例
      statusChart: null,
      typeChart: null,
      trendChart: null,
      // 图表数据
      statusDistribution: [],
      typeDistribution: [],
      trendData: [],
      // 排行榜
      rankingType: 'successRate',
      ranking: [],
      // 告警信息
      alerts: [],
      // 待处理事项
      pendingItems: [],
      // 趋势图日期范围
      trendDateRange: []
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  mounted() {
    this.initDateRange()
    this.loadData()
    this.initCharts()
    // 设置定时刷新
    this.timer = setInterval(() => {
      this.loadData()
    }, 30000) // 30秒刷新一次
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    // 销毁图表实例
    if (this.statusChart) {
      this.statusChart.dispose()
    }
    if (this.typeChart) {
      this.typeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    // 初始化日期范围
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7) // 最近7天
      this.trendDateRange = [start, end]
    },

    // 加载数据
    async loadData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadStatusDistribution(),
        this.loadTypeDistribution(),
        this.loadRanking(),
        this.loadAlerts(),
        this.loadPendingItems()
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
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 加载状态分布
    async loadStatusDistribution() {
      try {
        const response = await getStatusDistribution(this.tenantId)
        if (response.success) {
          this.statusDistribution = response.data || []
          this.updateStatusChart()
        }
      } catch (error) {
        console.error('加载状态分布失败:', error)
      }
    },

    // 加载类型分布
    async loadTypeDistribution() {
      try {
        const response = await getTypeDistribution(this.tenantId)
        if (response.success) {
          this.typeDistribution = response.data || []
          this.updateTypeChart()
        }
      } catch (error) {
        console.error('加载类型分布失败:', error)
      }
    },

    // 加载排行榜
    async loadRanking() {
      try {
        const response = await getRanking(this.rankingType, 10, this.tenantId)
        if (response.success) {
          this.ranking = response.data || []
        }
      } catch (error) {
        console.error('加载排行榜失败:', error)
      }
    },

    // 加载告警信息
    async loadAlerts() {
      try {
        const response = await getAlerts(null, this.tenantId)
        if (response.success) {
          this.alerts = response.data || []
        }
      } catch (error) {
        console.error('加载告警信息失败:', error)
      }
    },

    // 加载待处理事项
    async loadPendingItems() {
      try {
        const response = await getPendingItems(this.tenantId)
        if (response.success) {
          this.pendingItems = response.data || []
        }
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.typeChart = echarts.init(this.$refs.typeChart)
        this.trendChart = echarts.init(this.$refs.trendChart)
        
        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },

    // 更新状态分布图表
    updateStatusChart() {
      if (!this.statusChart) return

      const option = {
        title: {
          text: '数字员工状态分布',
          left: 'center',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: this.statusDistribution.map(item => item.name)
        },
        series: [
          {
            name: '状态分布',
            type: 'pie',
            radius: '50%',
            data: this.statusDistribution.map(item => ({
              value: item.count,
              name: item.name
            })),
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

    // 更新类型分布图表
    updateTypeChart() {
      if (!this.typeChart) return

      const option = {
        title: {
          text: '数字员工类型分布',
          left: 'center',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: this.typeDistribution.map(item => item.name)
        },
        series: [
          {
            name: '类型分布',
            type: 'pie',
            radius: '50%',
            data: this.typeDistribution.map(item => ({
              value: item.count,
              name: item.name
            })),
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
      this.typeChart.setOption(option)
    },

    // 更新趋势图表
    updateTrendChart() {
      if (!this.trendChart) return

      // 这里应该根据实际的趋势数据来更新图表
      // 暂时使用模拟数据
      const dates = []
      const executions = []
      const successes = []
      
      for (let i = 6; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        dates.push(date.toLocaleDateString())
        executions.push(Math.floor(Math.random() * 100) + 50)
        successes.push(Math.floor(Math.random() * 80) + 40)
      }

      const option = {
        title: {
          text: '执行趋势',
          left: 'center',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['执行次数', '成功次数']
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
            name: '执行次数',
            type: 'line',
            stack: 'Total',
            data: executions
          },
          {
            name: '成功次数',
            type: 'line',
            stack: 'Total',
            data: successes
          }
        ]
      }
      this.trendChart.setOption(option)
    },

    // 处理窗口大小变化
    handleResize() {
      if (this.statusChart) {
        this.statusChart.resize()
      }
      if (this.typeChart) {
        this.typeChart.resize()
      }
      if (this.trendChart) {
        this.trendChart.resize()
      }
    },

    // 刷新状态图表
    refreshStatusChart() {
      this.loadStatusDistribution()
    },

    // 刷新类型图表
    refreshTypeChart() {
      this.loadTypeDistribution()
    },

    // 刷新趋势图表
    refreshTrendChart() {
      this.updateTrendChart()
    },

    // 刷新排行榜
    refreshRanking() {
      this.loadRanking()
    },

    // 刷新告警
    refreshAlerts() {
      this.loadAlerts()
    },

    // 刷新待处理事项
    refreshPendingItems() {
      this.loadPendingItems()
    },

    // 获取排行榜样式类
    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return 'normal'
    },

    // 格式化排行榜值
    formatRankingValue(item, type) {
      switch (type) {
        case 'successRate':
          return (item.successRate || 0) + '%'
        case 'executionCount':
          return item.executionCount || 0
        case 'responseTime':
          return (item.responseTime || 0) + 'ms'
        case 'overallScore':
          return item.overallScore || 0
        default:
          return '-'
      }
    },

    // 获取告警样式类
    getAlertClass(level) {
      const classMap = {
        'HIGH': 'alert-high',
        'MEDIUM': 'alert-medium',
        'LOW': 'alert-low'
      }
      return classMap[level] || 'alert-low'
    },

    // 获取告警图标
    getAlertIcon(level) {
      const iconMap = {
        'HIGH': 'el-icon-warning',
        'MEDIUM': 'el-icon-info',
        'LOW': 'el-icon-question'
      }
      return iconMap[level] || 'el-icon-info'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      if (priority <= 3) return 'danger'
      if (priority <= 6) return 'warning'
      return 'info'
    },

    // 获取优先级文本
    getPriorityText(priority) {
      if (priority <= 3) return '高'
      if (priority <= 6) return '中'
      return '低'
    },

    // 处理告警
    handleAlert(alert) {
      this.$message.info('告警处理功能开发中...')
    },

    // 处理待处理事项
    handlePendingItem(item) {
      this.$message.info('待处理事项处理功能开发中...')
    },

    // 格式化方法
    formatRobotType,

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style scoped>
.digital-employee-dashboard {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: #fff;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.execution {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.success {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.chart-controls {
  display: flex;
  align-items: center;
}

.chart-content {
  padding: 20px;
}

.ranking-alerts-section {
  margin-bottom: 20px;
}

.ranking-card,
.alerts-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  height: 400px;
  display: flex;
  flex-direction: column;
}

.ranking-header,
.alerts-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.ranking-header h3,
.alerts-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.ranking-content,
.alerts-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.no-data {
  text-align: center;
  color: #909399;
  padding: 50px 0;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.ranking-item:last-child {
  border-bottom: none;
}

.ranking-number {
  width: 30px;
  text-align: center;
}

.ranking-number .first {
  color: #f56c6c;
  font-weight: bold;
}

.ranking-number .second {
  color: #e6a23c;
  font-weight: bold;
}

.ranking-number .third {
  color: #909399;
  font-weight: bold;
}

.ranking-info {
  flex: 1;
  margin-left: 10px;
}

.robot-name {
  font-weight: bold;
  color: #303133;
}

.robot-type {
  font-size: 12px;
  color: #909399;
}

.ranking-value {
  font-weight: bold;
  color: #409eff;
}

.alert-item {
  display: flex;
  align-items: flex-start;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.alert-item:last-child {
  border-bottom: none;
}

.alert-icon {
  width: 20px;
  margin-right: 10px;
  margin-top: 2px;
}

.alert-high .alert-icon {
  color: #f56c6c;
}

.alert-medium .alert-icon {
  color: #e6a23c;
}

.alert-low .alert-icon {
  color: #909399;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.alert-message {
  color: #606266;
  font-size: 12px;
  margin-bottom: 5px;
}

.alert-time {
  color: #909399;
  font-size: 11px;
}

.alert-actions {
  margin-left: 10px;
}

.pending-section {
  margin-bottom: 20px;
}

.pending-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.pending-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.pending-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.pending-content {
  padding: 20px;
}
</style>
