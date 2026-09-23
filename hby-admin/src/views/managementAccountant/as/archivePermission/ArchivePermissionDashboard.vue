<template>
  <div class="archive-permission-dashboard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-data-analysis"></i>
            权限数据分析
          </h1>
          <p class="page-description">
            档案权限管理的统计分析和可视化图表
          </p>
        </div>
        <div class="header-right">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="handleDateRangeChange"
          />
          <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-files"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalPermissions || 0 }}</div>
              <div class="stat-label">权限总数</div>
              <div class="stat-change positive">
                <i class="el-icon-arrow-up"></i>
                +12.5%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.activePermissions || 0 }}</div>
              <div class="stat-label">激活权限</div>
              <div class="stat-change positive">
                <i class="el-icon-arrow-up"></i>
                +8.3%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.pendingApprovals || 0 }}</div>
              <div class="stat-label">待审批</div>
              <div class="stat-change negative">
                <i class="el-icon-arrow-down"></i>
                -3.2%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.highRiskPermissions || 0 }}</div>
              <div class="stat-label">高风险权限</div>
              <div class="stat-change negative">
                <i class="el-icon-arrow-down"></i>
                -5.1%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 权限趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>权限趋势分析</h3>
              <el-select v-model="trendGranularity" size="small" @change="loadTrendData">
                <el-option label="按天" value="day" />
                <el-option label="按周" value="week" />
                <el-option label="按月" value="month" />
              </el-select>
            </div>
            <div class="chart-content">
              <div ref="trendChart" class="chart" v-loading="trendLoading"></div>
            </div>
          </div>
        </el-col>

        <!-- 权限类型分布 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>权限类型分布</h3>
            </div>
            <div class="chart-content">
              <div ref="typeChart" class="chart" v-loading="typeLoading"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <!-- 权限状态分布 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>权限状态分布</h3>
            </div>
            <div class="chart-content">
              <div ref="statusChart" class="chart" v-loading="statusLoading"></div>
            </div>
          </div>
        </el-col>

        <!-- 风险等级分布 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>风险等级分布</h3>
            </div>
            <div class="chart-content">
              <div ref="riskChart" class="chart" v-loading="riskLoading"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <!-- 用户活动统计 -->
        <el-col :span="24">
          <div class="chart-card">
            <div class="chart-header">
              <h3>用户活动统计</h3>
            </div>
            <div class="chart-content">
              <div ref="activityChart" class="chart large" v-loading="activityLoading"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜区域 -->
    <div class="rankings-section">
      <el-row :gutter="20">
        <!-- 热门权限排行 -->
        <el-col :span="12">
          <div class="ranking-card">
            <div class="card-header">
              <h3>热门权限排行</h3>
              <el-button type="text" @click="viewAllPopular">查看全部</el-button>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in popularPermissions"
                :key="item.id"
                class="ranking-item"
              >
                <div class="ranking-number" :class="getRankingClass(index)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="ranking-title">{{ item.name }}</div>
                  <div class="ranking-subtitle">{{ item.type }}</div>
                </div>
                <div class="ranking-value">
                  {{ item.count }}次
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 活跃用户排行 -->
        <el-col :span="12">
          <div class="ranking-card">
            <div class="card-header">
              <h3>活跃用户排行</h3>
              <el-button type="text" @click="viewAllActive">查看全部</el-button>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in activeUsers"
                :key="item.id"
                class="ranking-item"
              >
                <div class="ranking-number" :class="getRankingClass(index)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="ranking-title">{{ item.name }}</div>
                  <div class="ranking-subtitle">{{ item.department }}</div>
                </div>
                <div class="ranking-value">
                  {{ item.count }}次
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统健康监控 -->
    <div class="health-section">
      <div class="health-card">
        <div class="card-header">
          <h3>系统健康监控</h3>
          <el-tag :type="getHealthStatusType(healthData.status)">
            {{ healthData.status || '正常' }}
          </el-tag>
        </div>
        <div class="health-content">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="health-item">
                <div class="health-label">权限质量评分</div>
                <div class="health-value">
                  <el-progress
                    :percentage="healthData.qualityScore || 85"
                    :color="getQualityColor(healthData.qualityScore)"
                  />
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-label">合规率</div>
                <div class="health-value">
                  <el-progress
                    :percentage="healthData.complianceRate || 92"
                    color="#67c23a"
                  />
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-label">响应时间</div>
                <div class="health-value">
                  <span class="metric-value">{{ healthData.responseTime || 120 }}ms</span>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="health-item">
                <div class="health-label">错误率</div>
                <div class="health-value">
                  <span class="metric-value error">{{ healthData.errorRate || 0.5 }}%</span>
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
  getPermissionTrend,
  countByPermissionType,
  countByPermissionStatus,
  countByRiskLevel,
  getUserActivityStatistics,
  getPopularPermissions,
  getActiveUsers,
  checkSystemHealth
} from '@/api/managementAccountant/as/archivePermission'

export default {
  name: 'ArchivePermissionDashboard',
  data() {
    return {
      // 日期范围
      dateRange: [],
      // 趋势图粒度
      trendGranularity: 'day',
      // 数据
      overviewData: {},
      healthData: {},
      popularPermissions: [],
      activeUsers: [],
      // 加载状态
      trendLoading: false,
      typeLoading: false,
      statusLoading: false,
      riskLoading: false,
      activityLoading: false,
      // 图表实例
      trendChart: null,
      typeChart: null,
      statusChart: null,
      riskChart: null,
      activityChart: null
    }
  },
  created() {
    this.initDateRange()
    this.loadData()
  },
  mounted() {
    this.initCharts()
    this.loadChartData()
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
      this.dateRange = [
        start.toISOString().split('T')[0],
        end.toISOString().split('T')[0]
      ]
    },
    // 加载数据
    async loadData() {
      try {
        await Promise.all([
          this.loadOverviewData(),
          this.loadHealthData(),
          this.loadRankingData()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getSystemOverview()
        if (response.success) {
          this.overviewData = response.data || {}
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 加载健康数据
    async loadHealthData() {
      try {
        const response = await checkSystemHealth()
        if (response.success) {
          this.healthData = response.data || {}
        }
      } catch (error) {
        console.error('加载健康数据失败:', error)
      }
    },
    // 加载排行数据
    async loadRankingData() {
      try {
        const [popularResponse, activeResponse] = await Promise.all([
          getPopularPermissions(10),
          getActiveUsers(10)
        ])
        
        if (popularResponse.success) {
          this.popularPermissions = popularResponse.data || []
        }
        if (activeResponse.success) {
          this.activeUsers = activeResponse.data || []
        }
      } catch (error) {
        console.error('加载排行数据失败:', error)
      }
    },
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.typeChart = echarts.init(this.$refs.typeChart)
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.riskChart = echarts.init(this.$refs.riskChart)
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
      if (this.typeChart) {
        this.typeChart.dispose()
      }
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      if (this.riskChart) {
        this.riskChart.dispose()
      }
      if (this.activityChart) {
        this.activityChart.dispose()
      }
      window.removeEventListener('resize', this.handleResize)
    },
    // 处理窗口大小变化
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.typeChart) this.typeChart.resize()
      if (this.statusChart) this.statusChart.resize()
      if (this.riskChart) this.riskChart.resize()
      if (this.activityChart) this.activityChart.resize()
    },
    // 加载图表数据
    async loadChartData() {
      await Promise.all([
        this.loadTrendData(),
        this.loadTypeData(),
        this.loadStatusData(),
        this.loadRiskData(),
        this.loadActivityData()
      ])
    },
    // 加载趋势数据
    async loadTrendData() {
      this.trendLoading = true
      try {
        const [startDate, endDate] = this.dateRange
        const response = await getPermissionTrend(startDate, endDate, this.trendGranularity)
        if (response.success && this.trendChart) {
          const data = response.data || []
          const option = {
            title: {
              text: '权限创建趋势',
              left: 'center',
              textStyle: { fontSize: 14 }
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
              name: '权限数量',
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
      } finally {
        this.trendLoading = false
      }
    },
    // 加载类型数据
    async loadTypeData() {
      this.typeLoading = true
      try {
        const response = await countByPermissionType()
        if (response.success && this.typeChart) {
          const data = response.data || []
          const option = {
            title: {
              text: '权限类型分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'item'
            },
            series: [{
              type: 'pie',
              radius: '60%',
              data: data.map(item => ({
                name: item.type,
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
          this.typeChart.setOption(option)
        }
      } catch (error) {
        console.error('加载类型数据失败:', error)
      } finally {
        this.typeLoading = false
      }
    },
    // 加载状态数据
    async loadStatusData() {
      this.statusLoading = true
      try {
        const response = await countByPermissionStatus()
        if (response.success && this.statusChart) {
          const data = response.data || []
          const option = {
            title: {
              text: '权限状态分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'item'
            },
            series: [{
              type: 'doughnut',
              radius: ['40%', '70%'],
              data: data.map(item => ({
                name: item.status,
                value: item.count
              }))
            }]
          }
          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载状态数据失败:', error)
      } finally {
        this.statusLoading = false
      }
    },
    // 加载风险数据
    async loadRiskData() {
      this.riskLoading = true
      try {
        const response = await countByRiskLevel()
        if (response.success && this.riskChart) {
          const data = response.data || []
          const option = {
            title: {
              text: '风险等级分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item.level)
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              type: 'bar',
              data: data.map(item => ({
                value: item.count,
                itemStyle: {
                  color: this.getRiskColor(item.level)
                }
              }))
            }]
          }
          this.riskChart.setOption(option)
        }
      } catch (error) {
        console.error('加载风险数据失败:', error)
      } finally {
        this.riskLoading = false
      }
    },
    // 加载活动数据
    async loadActivityData() {
      this.activityLoading = true
      try {
        const [startDate, endDate] = this.dateRange
        const response = await getUserActivityStatistics(startDate, endDate)
        if (response.success && this.activityChart) {
          const data = response.data || []
          const option = {
            title: {
              text: '用户活动统计',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['权限申请', '权限使用', '权限审批'],
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
                name: '权限申请',
                type: 'bar',
                data: data.map(item => item.applications)
              },
              {
                name: '权限使用',
                type: 'bar',
                data: data.map(item => item.usages)
              },
              {
                name: '权限审批',
                type: 'bar',
                data: data.map(item => item.approvals)
              }
            ]
          }
          this.activityChart.setOption(option)
        }
      } catch (error) {
        console.error('加载活动数据失败:', error)
      } finally {
        this.activityLoading = false
      }
    },
    // 刷新数据
    refreshData() {
      this.loadData()
      this.loadChartData()
      this.$message.success('数据刷新成功')
    },
    // 日期范围变化
    handleDateRangeChange() {
      this.loadChartData()
    },
    // 查看全部热门权限
    viewAllPopular() {
      this.$message.info('功能开发中...')
    },
    // 查看全部活跃用户
    viewAllActive() {
      this.$message.info('功能开发中...')
    },
    // 获取排行样式
    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return ''
    },
    // 获取健康状态类型
    getHealthStatusType(status) {
      const typeMap = {
        '正常': 'success',
        '警告': 'warning',
        '异常': 'danger'
      }
      return typeMap[status] || 'success'
    },
    // 获取质量颜色
    getQualityColor(score) {
      if (score >= 90) return '#67c23a'
      if (score >= 70) return '#e6a23c'
      return '#f56c6c'
    },
    // 获取风险颜色
    getRiskColor(level) {
      const colorMap = {
        'LOW': '#67c23a',
        'MEDIUM': '#e6a23c',
        'HIGH': '#f56c6c',
        'CRITICAL': '#f56c6c'
      }
      return colorMap[level] || '#409eff'
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-permission-dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: white;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            color: #409eff;
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
        gap: 12px;
        align-items: center;
      }
    }
  }

  .statistics-section {
    margin-bottom: 20px;

    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 24px;
      display: flex;
      align-items: center;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      transition: transform 0.2s;

      &:hover {
        transform: translateY(-2px);
      }

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

        &.pending {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.risk {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-content {
        flex: 1;

        .stat-number {
          font-size: 28px;
          font-weight: 700;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 4px;
        }

        .stat-change {
          font-size: 12px;
          display: flex;
          align-items: center;

          i {
            margin-right: 4px;
          }

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

  .charts-section {
    margin-bottom: 20px;

    .chart-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .chart-content {
        .chart {
          width: 100%;
          height: 300px;

          &.large {
            height: 400px;
          }
        }
      }
    }
  }

  .rankings-section {
    margin-bottom: 20px;

    .ranking-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

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
            font-size: 14px;
            font-weight: 600;
            margin-right: 16px;
            background: #f5f7fa;
            color: #909399;

            &.first {
              background: #ffd700;
              color: white;
            }

            &.second {
              background: #c0c0c0;
              color: white;
            }

            &.third {
              background: #cd7f32;
              color: white;
            }
          }

          .ranking-content {
            flex: 1;

            .ranking-title {
              font-size: 14px;
              font-weight: 600;
              color: #303133;
              margin-bottom: 2px;
            }

            .ranking-subtitle {
              font-size: 12px;
              color: #909399;
            }
          }

          .ranking-value {
            font-size: 14px;
            font-weight: 600;
            color: #409eff;
          }
        }
      }
    }
  }

  .health-section {
    .health-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .health-content {
        .health-item {
          text-align: center;

          .health-label {
            font-size: 14px;
            color: #909399;
            margin-bottom: 12px;
          }

          .health-value {
            .metric-value {
              font-size: 18px;
              font-weight: 600;
              color: #409eff;

              &.error {
                color: #f56c6c;
              }
            }
          }
        }
      }
    }
  }
}

// 进度条样式
::v-deep .el-progress {
  .el-progress-bar {
    .el-progress-bar__outer {
      background-color: #ebeef5;
      border-radius: 4px;
      height: 8px;

      .el-progress-bar__inner {
        border-radius: 4px;
        transition: width 0.3s ease;
      }
    }
  }

  .el-progress__text {
    font-size: 12px;
    color: #606266;
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .archive-permission-dashboard {
    .charts-section {
      .el-col {
        margin-bottom: 20px;
      }
    }
  }
}

@media (max-width: 768px) {
  .archive-permission-dashboard {
    padding: 12px;

    .page-header {
      padding: 16px;

      .header-content {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;

        .header-right {
          width: 100%;
          justify-content: flex-end;
        }
      }
    }

    .statistics-section {
      .el-col {
        margin-bottom: 12px;
      }

      .stat-card {
        padding: 16px;

        .stat-icon {
          width: 48px;
          height: 48px;

          i {
            font-size: 20px;
          }
        }

        .stat-content {
          .stat-number {
            font-size: 24px;
          }
        }
      }
    }

    .charts-section {
      .chart-card {
        padding: 16px;

        .chart-content {
          .chart {
            height: 250px;

            &.large {
              height: 300px;
            }
          }
        }
      }
    }

    .rankings-section {
      .el-col {
        margin-bottom: 20px;
      }

      .ranking-card {
        padding: 16px;
      }
    }

    .health-section {
      .health-card {
        padding: 16px;

        .health-content {
          .el-col {
            margin-bottom: 16px;
          }
        }
      }
    }
  }
}

// 加载状态样式
.el-loading-mask {
  border-radius: 8px;
}
</style>
