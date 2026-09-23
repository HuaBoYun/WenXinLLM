<template>
  <div class="performance-calibration-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCalibrations || 0 }}</div>
              <div class="stat-label">总校准数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon ongoing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.ongoingCalibrations || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCalibrations || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon average">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.averageScore || 0 }}</div>
              <div class="stat-label">平均得分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <!-- 校准完成趋势 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>校准完成趋势</span>
            <el-button-group class="header-buttons">
              <el-button size="mini" @click="refreshTrendChart">刷新</el-button>
            </el-button-group>
          </div>
          <div class="chart-container" v-loading="trendChartLoading">
            <div ref="trendChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 校准类型分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>校准类型分布</span>
            <el-button-group class="header-buttons">
              <el-button size="mini" @click="refreshTypeChart">刷新</el-button>
            </el-button-group>
          </div>
          <div class="chart-container" v-loading="typeChartLoading">
            <div ref="typeChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-section">
      <!-- 校准状态分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>校准状态分布</span>
            <el-button-group class="header-buttons">
              <el-button size="mini" @click="refreshStatusChart">刷新</el-button>
            </el-button-group>
          </div>
          <div class="chart-container" v-loading="statusChartLoading">
            <div ref="statusChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 校准效果分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>校准效果分布</span>
            <el-button-group class="header-buttons">
              <el-button size="mini" @click="refreshEffectivenessChart">刷新</el-button>
            </el-button-group>
          </div>
          <div class="chart-container" v-loading="effectivenessChartLoading">
            <div ref="effectivenessChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待处理事项 -->
    <el-row :gutter="20" class="todo-section">
      <!-- 待处理校准 -->
      <el-col :span="8">
        <el-card shadow="never" class="todo-card">
          <div slot="header" class="card-header">
            <span>待处理校准</span>
            <el-badge :value="pendingCalibrations.length" class="header-badge" />
          </div>
          <div class="todo-list" v-loading="pendingLoading">
            <div 
              v-for="item in pendingCalibrations" 
              :key="item.calibrationId"
              class="todo-item"
              @click="handleViewCalibration(item)"
            >
              <div class="todo-title">{{ item.calibrationTitle }}</div>
              <div class="todo-meta">
                <el-tag :type="getStatusColor(item.calibrationStatus)" size="mini">
                  {{ formatCalibrationStatus(item.calibrationStatus) }}
                </el-tag>
                <span class="todo-time">{{ formatDateTime(item.plannedStartTime) }}</span>
              </div>
            </div>
            <div v-if="pendingCalibrations.length === 0" class="empty-state">
              <i class="el-icon-circle-check"></i>
              <p>暂无待处理校准</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 即将到期 -->
      <el-col :span="8">
        <el-card shadow="never" class="todo-card">
          <div slot="header" class="card-header">
            <span>即将到期</span>
            <el-badge :value="upcomingCalibrations.length" class="header-badge" type="warning" />
          </div>
          <div class="todo-list" v-loading="upcomingLoading">
            <div 
              v-for="item in upcomingCalibrations" 
              :key="item.calibrationId"
              class="todo-item urgent"
              @click="handleViewCalibration(item)"
            >
              <div class="todo-title">{{ item.calibrationTitle }}</div>
              <div class="todo-meta">
                <el-tag type="warning" size="mini">即将到期</el-tag>
                <span class="todo-time">{{ formatDateTime(item.plannedEndTime) }}</span>
              </div>
            </div>
            <div v-if="upcomingCalibrations.length === 0" class="empty-state">
              <i class="el-icon-time"></i>
              <p>暂无即将到期校准</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 校准排行榜 -->
      <el-col :span="8">
        <el-card shadow="never" class="todo-card">
          <div slot="header" class="card-header">
            <span>校准排行榜</span>
            <el-dropdown @command="handleRankingCommand">
              <el-button size="mini">
                {{ rankingType === 'effectiveness' ? '效果排行' : '完成排行' }}
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="effectiveness">效果排行</el-dropdown-item>
                <el-dropdown-item command="completion">完成排行</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="ranking-list" v-loading="rankingLoading">
            <div 
              v-for="(item, index) in rankingList" 
              :key="index"
              class="ranking-item"
            >
              <div class="ranking-number">{{ index + 1 }}</div>
              <div class="ranking-info">
                <div class="ranking-name">{{ item.name }}</div>
                <div class="ranking-score">{{ item.score }}</div>
              </div>
            </div>
            <div v-if="rankingList.length === 0" class="empty-state">
              <i class="el-icon-trophy"></i>
              <p>暂无排行数据</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <el-row :gutter="20" class="quick-actions">
      <el-col :span="24">
        <el-card shadow="never" class="action-card">
          <div slot="header">
            <span>快速操作</span>
          </div>
          <div class="action-buttons">
            <el-button type="primary" icon="el-icon-plus" @click="handleQuickCreate">
              新建校准
            </el-button>
            <el-button type="success" icon="el-icon-download" @click="handleQuickExport">
              导出报告
            </el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewAll">
              查看全部
            </el-button>
            <el-button type="warning" icon="el-icon-setting" @click="handleSettings">
              校准设置
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
  getCalibrationStatistics,
  getCalibrationStatusDistribution,
  getCalibrationTypeDistribution,
  getCalibrationCompletionTrend,
  getEffectivenessDistribution,
  getCalibrationRanking,
  getPendingFollowUpCalibrations,
  getUpcomingCalibrations,
  exportCalibrationData,
  formatCalibrationStatus,
  getStatusColor
} from '@/api/managementAccountant/pm/performanceCalibration'

export default {
  name: 'PerformanceCalibrationDashboard',
  data() {
    return {
      statistics: {},
      pendingCalibrations: [],
      upcomingCalibrations: [],
      rankingList: [],
      rankingType: 'effectiveness',
      
      // 加载状态
      statisticsLoading: false,
      trendChartLoading: false,
      typeChartLoading: false,
      statusChartLoading: false,
      effectivenessChartLoading: false,
      pendingLoading: false,
      upcomingLoading: false,
      rankingLoading: false,

      // 图表实例
      trendChart: null,
      typeChart: null,
      statusChart: null,
      effectivenessChart: null,

      // 当前年度
      currentYear: new Date().getFullYear()
    }
  },
  mounted() {
    this.initCharts()
    this.loadAllData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.typeChart = echarts.init(this.$refs.typeChart)
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.effectivenessChart = echarts.init(this.$refs.effectivenessChart)

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
      if (this.effectivenessChart) {
        this.effectivenessChart.dispose()
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 处理窗口大小变化
    handleResize() {
      this.$nextTick(() => {
        if (this.trendChart) this.trendChart.resize()
        if (this.typeChart) this.typeChart.resize()
        if (this.statusChart) this.statusChart.resize()
        if (this.effectivenessChart) this.effectivenessChart.resize()
      })
    },

    // 加载所有数据
    async loadAllData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadTrendChart(),
        this.loadTypeChart(),
        this.loadStatusChart(),
        this.loadEffectivenessChart(),
        this.loadPendingCalibrations(),
        this.loadUpcomingCalibrations(),
        this.loadRankingList()
      ])
    },

    // 加载统计数据
    async loadStatistics() {
      this.statisticsLoading = true
      try {
        const response = await getCalibrationStatistics({
          calibrationYear: this.currentYear
        })
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      } finally {
        this.statisticsLoading = false
      }
    },

    // 加载趋势图表
    async loadTrendChart() {
      this.trendChartLoading = true
      try {
        const startTime = `${this.currentYear}-01-01 00:00:00`
        const endTime = `${this.currentYear}-12-31 23:59:59`
        const response = await getCalibrationCompletionTrend(startTime, endTime)
        
        if (response.success && this.trendChart) {
          const data = response.data
          const option = {
            title: {
              text: '校准完成趋势',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item.month)
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name: '完成数量',
              type: 'line',
              data: data.map(item => item.count),
              smooth: true,
              itemStyle: { color: '#409EFF' }
            }]
          }
          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('加载趋势图表失败:', error)
      } finally {
        this.trendChartLoading = false
      }
    },

    // 加载类型分布图表
    async loadTypeChart() {
      this.typeChartLoading = true
      try {
        const response = await getCalibrationTypeDistribution(this.currentYear)
        
        if (response.success && this.typeChart) {
          const data = response.data
          const option = {
            title: {
              text: '校准类型分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            series: [{
              name: '校准类型',
              type: 'pie',
              radius: '60%',
              data: data.map(item => ({
                name: item.typeName,
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
        console.error('加载类型分布图表失败:', error)
      } finally {
        this.typeChartLoading = false
      }
    },

    // 加载状态分布图表
    async loadStatusChart() {
      this.statusChartLoading = true
      try {
        const response = await getCalibrationStatusDistribution(this.currentYear)
        
        if (response.success && this.statusChart) {
          const data = response.data
          const option = {
            title: {
              text: '校准状态分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: { type: 'shadow' }
            },
            xAxis: {
              type: 'category',
              data: data.map(item => item.statusName)
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              name: '数量',
              type: 'bar',
              data: data.map(item => item.count),
              itemStyle: {
                color: function(params) {
                  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']
                  return colors[params.dataIndex % colors.length]
                }
              }
            }]
          }
          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载状态分布图表失败:', error)
      } finally {
        this.statusChartLoading = false
      }
    },

    // 加载效果分布图表
    async loadEffectivenessChart() {
      this.effectivenessChartLoading = true
      try {
        const response = await getEffectivenessDistribution(this.currentYear)
        
        if (response.success && this.effectivenessChart) {
          const data = response.data
          const option = {
            title: {
              text: '校准效果分布',
              left: 'center',
              textStyle: { fontSize: 14 }
            },
            tooltip: {
              trigger: 'item'
            },
            radar: {
              indicator: data.map(item => ({
                name: item.effectivenessLevel,
                max: Math.max(...data.map(d => d.count))
              }))
            },
            series: [{
              name: '校准效果',
              type: 'radar',
              data: [{
                value: data.map(item => item.count),
                name: '效果分布'
              }]
            }]
          }
          this.effectivenessChart.setOption(option)
        }
      } catch (error) {
        console.error('加载效果分布图表失败:', error)
      } finally {
        this.effectivenessChartLoading = false
      }
    },

    // 加载待处理校准
    async loadPendingCalibrations() {
      this.pendingLoading = true
      try {
        const deadline = this.$moment().add(7, 'days').format('YYYY-MM-DD HH:mm:ss')
        const response = await getPendingFollowUpCalibrations(deadline, 5)
        if (response.success) {
          this.pendingCalibrations = response.data
        }
      } catch (error) {
        console.error('加载待处理校准失败:', error)
      } finally {
        this.pendingLoading = false
      }
    },

    // 加载即将到期校准
    async loadUpcomingCalibrations() {
      this.upcomingLoading = true
      try {
        const deadline = this.$moment().add(3, 'days').format('YYYY-MM-DD HH:mm:ss')
        const response = await getUpcomingCalibrations(deadline, 5)
        if (response.success) {
          this.upcomingCalibrations = response.data
        }
      } catch (error) {
        console.error('加载即将到期校准失败:', error)
      } finally {
        this.upcomingLoading = false
      }
    },

    // 加载排行榜
    async loadRankingList() {
      this.rankingLoading = true
      try {
        const response = await getCalibrationRanking(this.currentYear, this.rankingType, 5)
        if (response.success) {
          this.rankingList = response.data
        }
      } catch (error) {
        console.error('加载排行榜失败:', error)
      } finally {
        this.rankingLoading = false
      }
    },

    // 刷新图表
    async refreshTrendChart() {
      await this.loadTrendChart()
    },

    async refreshTypeChart() {
      await this.loadTypeChart()
    },

    async refreshStatusChart() {
      await this.loadStatusChart()
    },

    async refreshEffectivenessChart() {
      await this.loadEffectivenessChart()
    },

    // 排行榜命令
    async handleRankingCommand(command) {
      this.rankingType = command
      await this.loadRankingList()
    },

    // 查看校准详情
    handleViewCalibration(calibration) {
      this.$router.push({
        name: 'PerformanceCalibrationDetail',
        params: { id: calibration.calibrationId }
      })
    },

    // 快速操作
    handleQuickCreate() {
      this.$router.push({ name: 'PerformanceCalibrationCreate' })
    },

    async handleQuickExport() {
      try {
        const response = await exportCalibrationData({
          calibrationYear: this.currentYear
        })
        if (response.success) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    handleViewAll() {
      this.$router.push({ name: 'PerformanceCalibrationList' })
    },

    handleSettings() {
      this.$message.info('校准设置功能开发中...')
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('MM-DD HH:mm')
    },

    // 格式化方法
    formatCalibrationStatus,
    getStatusColor
  }
}
</script>

<style lang="scss" scoped>
.performance-calibration-dashboard {
  .stats-cards {
    margin-bottom: 20px;

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
          margin-right: 20px;

          i {
            font-size: 24px;
            color: #fff;
          }

          &.total {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.ongoing {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.completed {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.average {
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
            margin-top: 5px;
          }
        }
      }
    }
  }

  .chart-section {
    margin-bottom: 20px;

    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-buttons {
          .el-button {
            padding: 5px 10px;
          }
        }
      }

      .chart-container {
        height: 300px;

        .chart {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

  .todo-section {
    margin-bottom: 20px;

    .todo-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-badge {
          margin-left: 10px;
        }
      }

      .todo-list, .ranking-list {
        max-height: 300px;
        overflow-y: auto;

        .todo-item, .ranking-item {
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: background-color 0.3s;

          &:hover {
            background-color: #f8f9fa;
          }

          &:last-child {
            border-bottom: none;
          }

          &.urgent {
            border-left: 3px solid #e6a23c;
            padding-left: 10px;
          }
        }

        .todo-item {
          .todo-title {
            font-weight: 500;
            color: #303133;
            margin-bottom: 5px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .todo-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;

            .todo-time {
              font-size: 12px;
              color: #909399;
            }
          }
        }

        .ranking-item {
          display: flex;
          align-items: center;

          .ranking-number {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background: #409eff;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            margin-right: 15px;
          }

          .ranking-info {
            flex: 1;

            .ranking-name {
              font-weight: 500;
              color: #303133;
            }

            .ranking-score {
              font-size: 12px;
              color: #909399;
            }
          }
        }

        .empty-state {
          text-align: center;
          padding: 40px 0;
          color: #909399;

          i {
            font-size: 48px;
            margin-bottom: 10px;
          }

          p {
            margin: 0;
          }
        }
      }
    }
  }

  .quick-actions {
    .action-card {
      .action-buttons {
        display: flex;
        gap: 15px;
        justify-content: center;
      }
    }
  }
}
</style>
