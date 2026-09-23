<template>
  <div class="document-version-dashboard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-data-analysis"></i>
          文档版本数据分析
        </h2>
        <p class="page-description">版本统计、趋势分析和性能监控</p>
      </div>
      <div class="header-right">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleDateChange"
        ></el-date-picker>
        <el-button icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.totalVersions }}</div>
              <div class="stat-label">版本总数</div>
              <div class="stat-change" :class="overview.totalChange >= 0 ? 'positive' : 'negative'">
                <i :class="overview.totalChange >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                {{ Math.abs(overview.totalChange) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon published">
              <i class="el-icon-upload"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.publishedVersions }}</div>
              <div class="stat-label">已发布版本</div>
              <div class="stat-change" :class="overview.publishedChange >= 0 ? 'positive' : 'negative'">
                <i :class="overview.publishedChange >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                {{ Math.abs(overview.publishedChange) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.activeUsers }}</div>
              <div class="stat-label">活跃用户</div>
              <div class="stat-change" :class="overview.activeChange >= 0 ? 'positive' : 'negative'">
                <i :class="overview.activeChange >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                {{ Math.abs(overview.activeChange) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon storage">
              <i class="el-icon-folder"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ formatFileSize(overview.totalStorage) }}</div>
              <div class="stat-label">存储使用</div>
              <div class="stat-change" :class="overview.storageChange >= 0 ? 'positive' : 'negative'">
                <i :class="overview.storageChange >= 0 ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                {{ Math.abs(overview.storageChange) }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-area">
      <el-row :gutter="20">
        <!-- 版本趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>版本创建趋势</h3>
              <el-select v-model="trendGranularity" size="small" @change="loadTrendData">
                <el-option label="按天" value="day"></el-option>
                <el-option label="按周" value="week"></el-option>
                <el-option label="按月" value="month"></el-option>
              </el-select>
            </div>
            <div class="chart-content">
              <div ref="trendChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>

        <!-- 版本状态分布 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>版本状态分布</h3>
            </div>
            <div class="chart-content">
              <div ref="statusChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 版本类型分布 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>版本类型分布</h3>
            </div>
            <div class="chart-content">
              <div ref="typeChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>

        <!-- 用户活动统计 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>用户活动统计</h3>
            </div>
            <div class="chart-content">
              <div ref="activityChart" class="chart" style="height: 300px;"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜区域 -->
    <div class="ranking-area">
      <el-row :gutter="20">
        <!-- 热门文档排行 -->
        <el-col :span="12">
          <div class="ranking-card">
            <div class="ranking-header">
              <h3>热门文档排行</h3>
              <el-button type="text" @click="viewMore('documents')">查看更多</el-button>
            </div>
            <div class="ranking-content">
              <div v-for="(item, index) in popularDocuments" :key="item.documentId" class="ranking-item">
                <div class="ranking-number" :class="getRankingClass(index)">{{ index + 1 }}</div>
                <div class="ranking-info">
                  <div class="ranking-title">{{ item.documentName }}</div>
                  <div class="ranking-meta">
                    <span>版本数: {{ item.versionCount }}</span>
                    <span>访问量: {{ item.accessCount }}</span>
                  </div>
                </div>
                <div class="ranking-score">{{ item.score }}</div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 活跃用户排行 -->
        <el-col :span="12">
          <div class="ranking-card">
            <div class="ranking-header">
              <h3>活跃用户排行</h3>
              <el-button type="text" @click="viewMore('users')">查看更多</el-button>
            </div>
            <div class="ranking-content">
              <div v-for="(item, index) in activeUsers" :key="item.userId" class="ranking-item">
                <div class="ranking-number" :class="getRankingClass(index)">{{ index + 1 }}</div>
                <div class="ranking-info">
                  <div class="ranking-title">{{ item.userName }}</div>
                  <div class="ranking-meta">
                    <span>创建版本: {{ item.createCount }}</span>
                    <span>编辑次数: {{ item.editCount }}</span>
                  </div>
                </div>
                <div class="ranking-score">{{ item.activityScore }}</div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统健康状态 -->
    <div class="health-area">
      <div class="health-card">
        <div class="health-header">
          <h3>系统健康状态</h3>
          <el-button icon="el-icon-refresh" @click="checkSystemHealth">检查</el-button>
        </div>
        <div class="health-content">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="health-item">
                <div class="health-icon" :class="getHealthClass(healthStatus.storage)">
                  <i class="el-icon-folder"></i>
                </div>
                <div class="health-info">
                  <div class="health-title">存储健康</div>
                  <div class="health-status">{{ getHealthText(healthStatus.storage) }}</div>
                  <div class="health-detail">使用率: {{ healthStatus.storageUsage }}%</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-icon" :class="getHealthClass(healthStatus.performance)">
                  <i class="el-icon-odometer"></i>
                </div>
                <div class="health-info">
                  <div class="health-title">性能状态</div>
                  <div class="health-status">{{ getHealthText(healthStatus.performance) }}</div>
                  <div class="health-detail">响应时间: {{ healthStatus.responseTime }}ms</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-icon" :class="getHealthClass(healthStatus.quality)">
                  <i class="el-icon-medal"></i>
                </div>
                <div class="health-info">
                  <div class="health-title">质量评估</div>
                  <div class="health-status">{{ getHealthText(healthStatus.quality) }}</div>
                  <div class="health-detail">平均分: {{ healthStatus.qualityScore }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-icon" :class="getHealthClass(healthStatus.security)">
                  <i class="el-icon-lock"></i>
                </div>
                <div class="health-info">
                  <div class="health-title">安全状态</div>
                  <div class="health-status">{{ getHealthText(healthStatus.security) }}</div>
                  <div class="health-detail">风险等级: {{ healthStatus.riskLevel }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getSystemOverview,
  getVersionTrend,
  countByStatus,
  countByType,
  getUserActivityStats,
  getPopularDocuments,
  getActiveUsers,
  checkSystemHealth,
  formatFileSize
} from '@/api/managementAccountant/as/documentVersion'

export default {
  name: 'DocumentVersionDashboard',
  data() {
    return {
      // 日期范围
      dateRange: [],
      // 趋势粒度
      trendGranularity: 'day',
      // 概览数据
      overview: {
        totalVersions: 0,
        publishedVersions: 0,
        activeUsers: 0,
        totalStorage: 0,
        totalChange: 0,
        publishedChange: 0,
        activeChange: 0,
        storageChange: 0
      },
      // 图表实例
      trendChart: null,
      statusChart: null,
      typeChart: null,
      activityChart: null,
      // 排行榜数据
      popularDocuments: [],
      activeUsers: [],
      // 健康状态
      healthStatus: {
        storage: 'good',
        performance: 'good',
        quality: 'good',
        security: 'good',
        storageUsage: 0,
        responseTime: 0,
        qualityScore: 0,
        riskLevel: 'LOW'
      }
    }
  },
  mounted() {
    this.initDateRange()
    this.initCharts()
    this.loadAllData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 初始化日期范围
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30) // 30天前
      this.dateRange = [start, end]
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.typeChart = echarts.init(this.$refs.typeChart)
        this.activityChart = echarts.init(this.$refs.activityChart)

        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },

    // 销毁图表
    destroyCharts() {
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      if (this.typeChart) {
        this.typeChart.dispose()
      }
      if (this.activityChart) {
        this.activityChart.dispose()
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 处理窗口大小变化
    handleResize() {
      this.trendChart?.resize()
      this.statusChart?.resize()
      this.typeChart?.resize()
      this.activityChart?.resize()
    },

    // 加载所有数据
    async loadAllData() {
      await Promise.all([
        this.loadOverviewData(),
        this.loadTrendData(),
        this.loadStatusData(),
        this.loadTypeData(),
        this.loadActivityData(),
        this.loadRankingData(),
        this.loadHealthData()
      ])
    },

    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getSystemOverview()
        if (response.data) {
          this.overview = {
            ...this.overview,
            ...response.data
          }
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },

    // 加载趋势数据
    async loadTrendData() {
      try {
        const [startTime, endTime] = this.dateRange
        const response = await getVersionTrend(
          startTime.toISOString(),
          endTime.toISOString(),
          this.trendGranularity
        )
        
        if (response.data && this.trendChart) {
          const data = response.data
          const option = {
            title: {
              text: '版本创建趋势',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item.date)
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name: '版本数量',
              type: 'line',
              data: data.map(item => item.count),
              smooth: true,
              areaStyle: {
                opacity: 0.3
              }
            }]
          }
          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },

    // 加载状态分布数据
    async loadStatusData() {
      try {
        const response = await countByStatus()
        if (response.data && this.statusChart) {
          const data = response.data
          const option = {
            title: {
              text: '版本状态分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            series: [{
              name: '版本状态',
              type: 'pie',
              radius: '60%',
              data: data.map(item => ({
                name: this.formatStatusName(item.status),
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
          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载状态分布数据失败:', error)
      }
    },

    // 加载类型分布数据
    async loadTypeData() {
      try {
        const response = await countByType()
        if (response.data && this.typeChart) {
          const data = response.data
          const option = {
            title: {
              text: '版本类型分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            xAxis: {
              type: 'category',
              data: data.map(item => this.formatTypeName(item.type))
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name: '版本数量',
              type: 'bar',
              data: data.map(item => item.count),
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#83bff6' },
                  { offset: 0.5, color: '#188df0' },
                  { offset: 1, color: '#188df0' }
                ])
              }
            }]
          }
          this.typeChart.setOption(option)
        }
      } catch (error) {
        console.error('加载类型分布数据失败:', error)
      }
    },

    // 加载活动统计数据
    async loadActivityData() {
      try {
        const [startTime, endTime] = this.dateRange
        const response = await getUserActivityStats(
          startTime.toISOString(),
          endTime.toISOString()
        )
        
        if (response.data && this.activityChart) {
          const data = response.data
          const option = {
            title: {
              text: '用户活动统计',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['创建', '编辑', '发布'],
              bottom: 0
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item.date)
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '创建',
                type: 'bar',
                data: data.map(item => item.createCount)
              },
              {
                name: '编辑',
                type: 'bar',
                data: data.map(item => item.editCount)
              },
              {
                name: '发布',
                type: 'bar',
                data: data.map(item => item.publishCount)
              }
            ]
          }
          this.activityChart.setOption(option)
        }
      } catch (error) {
        console.error('加载活动统计数据失败:', error)
      }
    },

    // 加载排行榜数据
    async loadRankingData() {
      try {
        const [documentsRes, usersRes] = await Promise.all([
          getPopularDocuments(10),
          getActiveUsers(10)
        ])
        
        this.popularDocuments = documentsRes.data || []
        this.activeUsers = usersRes.data || []
      } catch (error) {
        console.error('加载排行榜数据失败:', error)
      }
    },

    // 加载健康数据
    async loadHealthData() {
      try {
        const response = await checkSystemHealth()
        if (response.data) {
          this.healthStatus = {
            ...this.healthStatus,
            ...response.data
          }
        }
      } catch (error) {
        console.error('加载健康数据失败:', error)
      }
    },

    // 日期变化处理
    handleDateChange() {
      this.loadTrendData()
      this.loadActivityData()
    },

    // 刷新数据
    refreshData() {
      this.loadAllData()
    },

    // 检查系统健康
    checkSystemHealth() {
      this.loadHealthData()
    },

    // 查看更多
    viewMore(type) {
      if (type === 'documents') {
        this.$message.info('查看更多热门文档功能开发中')
      } else if (type === 'users') {
        this.$message.info('查看更多活跃用户功能开发中')
      }
    },

    // 工具方法
    formatFileSize,

    formatStatusName(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'UNDER_REVIEW': '审核中',
        'APPROVED': '已审批',
        'PUBLISHED': '已发布',
        'ARCHIVED': '已归档',
        'DEPRECATED': '已废弃',
        'DELETED': '已删除'
      }
      return statusMap[status] || status
    },

    formatTypeName(type) {
      const typeMap = {
        'MAJOR': '主版本',
        'MINOR': '次版本',
        'PATCH': '补丁版本',
        'DRAFT': '草稿版本',
        'RELEASE': '发布版本',
        'HOTFIX': '热修复版本'
      }
      return typeMap[type] || type
    },

    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return 'normal'
    },

    getHealthClass(status) {
      const classMap = {
        'good': 'health-good',
        'warning': 'health-warning',
        'error': 'health-error'
      }
      return classMap[status] || 'health-good'
    },

    getHealthText(status) {
      const textMap = {
        'good': '良好',
        'warning': '警告',
        'error': '异常'
      }
      return textMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.document-version-dashboard {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      .page-title {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .page-description {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  }

  .stats-overview {
    margin-bottom: 20px;

    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

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

        &.published {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.active {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.storage {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-content {
        flex: 1;

        .stat-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
          margin: 4px 0;
        }

        .stat-change {
          font-size: 12px;
          display: flex;
          align-items: center;

          i {
            margin-right: 4px;
          }

          &.positive {
            color: #67C23A;
          }

          &.negative {
            color: #F56C6C;
          }
        }
      }
    }
  }

  .charts-area {
    margin-bottom: 20px;

    .chart-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      height: 100%;

      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 20px;
        border-bottom: 1px solid #EBEEF5;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .chart-content {
        padding: 20px;

        .chart {
          width: 100%;
        }
      }
    }
  }

  .ranking-area {
    margin-bottom: 20px;

    .ranking-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      height: 100%;

      .ranking-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 20px;
        border-bottom: 1px solid #EBEEF5;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .ranking-content {
        padding: 20px;

        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #EBEEF5;

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
            margin-right: 12px;
            font-weight: 600;
            color: white;

            &.first {
              background: #FFD700;
            }

            &.second {
              background: #C0C0C0;
            }

            &.third {
              background: #CD7F32;
            }

            &.normal {
              background: #909399;
            }
          }

          .ranking-info {
            flex: 1;

            .ranking-title {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            .ranking-meta {
              font-size: 12px;
              color: #909399;

              span {
                margin-right: 12px;
              }
            }
          }

          .ranking-score {
            font-size: 16px;
            font-weight: 600;
            color: #409EFF;
          }
        }
      }
    }
  }

  .health-area {
    .health-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

      .health-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 20px;
        border-bottom: 1px solid #EBEEF5;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .health-content {
        padding: 20px;

        .health-item {
          display: flex;
          align-items: center;
          padding: 16px;
          border-radius: 8px;
          background: #F5F7FA;

          .health-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 16px;

            i {
              font-size: 20px;
              color: white;
            }

            &.health-good {
              background: #67C23A;
            }

            &.health-warning {
              background: #E6A23C;
            }

            &.health-error {
              background: #F56C6C;
            }
          }

          .health-info {
            .health-title {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            .health-status {
              font-size: 16px;
              font-weight: 600;
              margin-bottom: 4px;

              &.good {
                color: #67C23A;
              }

              &.warning {
                color: #E6A23C;
              }

              &.error {
                color: #F56C6C;
              }
            }

            .health-detail {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }
    }
  }
}
</style>
