<template>
  <div class="job-schedule-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.totalJobs || 0 }}</div>
              <div class="stat-label">总作业数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon running">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.runningJobs || 0 }}</div>
              <div class="stat-label">运行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon success">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.completedJobs || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon failed">
              <i class="el-icon-error"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ statistics.failedJobs || 0 }}</div>
              <div class="stat-label">失败</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 作业状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>作业状态分布</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshJobStatusChart">刷新</el-button>
          </div>
          <div ref="jobStatusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 作业类型分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>作业类型分布</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshJobTypeChart">刷新</el-button>
          </div>
          <div ref="jobTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <!-- 执行趋势 -->
      <el-col :span="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>作业执行趋势</span>
            <div style="float: right">
              <el-radio-group v-model="trendPeriod" size="small" @change="refreshTrendChart">
                <el-radio-button label="7">最近7天</el-radio-button>
                <el-radio-button label="30">最近30天</el-radio-button>
                <el-radio-button label="90">最近90天</el-radio-button>
              </el-radio-group>
              <el-button style="margin-left: 10px; padding: 3px 0" type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container-large"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-row :gutter="20" class="table-row">
      <!-- 待处理作业 -->
      <el-col :span="12">
        <el-card class="table-card" shadow="never">
          <div slot="header" class="card-header">
            <span>待处理作业</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshPendingJobs">刷新</el-button>
          </div>
          <el-table :data="pendingJobs" size="small" max-height="300">
            <el-table-column prop="jobName" label="作业名称" show-overflow-tooltip />
            <el-table-column prop="jobType" label="类型" width="80">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getJobTypeTagType(scope.row.jobType)">
                  {{ formatJobType(scope.row.jobType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="60">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getPriorityTagType(scope.row.priority)">
                  {{ formatPriority(scope.row.priority) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="nextExecutionTime" label="下次执行" width="120">
              <template slot-scope="scope">
                {{ scope.row.nextExecutionTime | formatDateTime }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleStartJob(scope.row)">启动</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 最近执行记录 -->
      <el-col :span="12">
        <el-card class="table-card" shadow="never">
          <div slot="header" class="card-header">
            <span>最近执行记录</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshRecentExecutions">刷新</el-button>
          </div>
          <el-table :data="recentExecutions" size="small" max-height="300">
            <el-table-column prop="jobName" label="作业名称" show-overflow-tooltip />
            <el-table-column prop="executionStatus" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getExecutionStatusTagType(scope.row.executionStatus)">
                  {{ formatExecutionStatus(scope.row.executionStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="耗时" width="80">
              <template slot-scope="scope">
                {{ formatDuration(scope.row.duration) }}
              </template>
            </el-table-column>
            <el-table-column prop="lastExecutionTime" label="执行时间" width="120">
              <template slot-scope="scope">
                {{ scope.row.lastExecutionTime | formatDateTime }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleViewJob(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 性能指标 -->
    <el-row :gutter="20" class="metrics-row">
      <el-col :span="8">
        <el-card class="metric-card" shadow="never">
          <div slot="header" class="card-header">
            <span>调度器负载</span>
          </div>
          <div class="metric-content">
            <div class="metric-item">
              <span class="metric-label">CPU使用率:</span>
              <span class="metric-value">{{ performanceMetrics.cpuUsage || '0%' }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">内存使用率:</span>
              <span class="metric-value">{{ performanceMetrics.memoryUsage || '0%' }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">活跃线程数:</span>
              <span class="metric-value">{{ performanceMetrics.activeThreads || 0 }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">队列长度:</span>
              <span class="metric-value">{{ performanceMetrics.queueLength || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="metric-card" shadow="never">
          <div slot="header" class="card-header">
            <span>SLA指标</span>
          </div>
          <div class="metric-content">
            <div class="metric-item">
              <span class="metric-label">SLA达成率:</span>
              <span class="metric-value">{{ slaMetrics.achievementRate || '0%' }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">平均响应时间:</span>
              <span class="metric-value">{{ formatDuration(slaMetrics.avgResponseTime) }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">超时作业数:</span>
              <span class="metric-value">{{ slaMetrics.timeoutJobs || 0 }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">违约作业数:</span>
              <span class="metric-value">{{ slaMetrics.violationJobs || 0 }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="metric-card" shadow="never">
          <div slot="header" class="card-header">
            <span>系统健康度</span>
          </div>
          <div class="metric-content">
            <div class="metric-item">
              <span class="metric-label">系统状态:</span>
              <span class="metric-value" :class="getSystemStatusClass(systemHealth.status)">
                {{ systemHealth.status || '正常' }}
              </span>
            </div>
            <div class="metric-item">
              <span class="metric-label">可用性:</span>
              <span class="metric-value">{{ systemHealth.availability || '99.9%' }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">错误率:</span>
              <span class="metric-value">{{ systemHealth.errorRate || '0.1%' }}</span>
            </div>
            <div class="metric-item">
              <span class="metric-label">最后检查:</span>
              <span class="metric-value">{{ systemHealth.lastCheckTime | formatDateTime }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 作业详情对话框 -->
    <JobScheduleDetail
      :visible.sync="detailVisible"
      :job-id="currentJobId"
      :readonly="true"
      @refresh="loadDashboardData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import jobScheduleApi from '@/api/managementAccountant/ss/jobSchedule'
import JobScheduleDetail from './JobScheduleDetail'

export default {
  name: 'JobScheduleDashboard',
  components: {
    JobScheduleDetail
  },
  data() {
    return {
      loading: false,
      trendPeriod: '7',
      
      // 统计数据
      statistics: {},
      pendingJobs: [],
      recentExecutions: [],
      performanceMetrics: {},
      slaMetrics: {},
      systemHealth: {},
      
      // 图表实例
      jobStatusChart: null,
      jobTypeChart: null,
      trendChart: null,
      
      // 对话框控制
      detailVisible: false,
      currentJobId: null
    }
  },
  mounted() {
    this.initCharts()
    this.loadDashboardData()
    
    // 设置定时刷新
    this.refreshTimer = setInterval(() => {
      this.loadDashboardData()
    }, 30000) // 30秒刷新一次
  },
  beforeDestroy() {
    // 清理定时器
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
    
    // 销毁图表实例
    if (this.jobStatusChart) {
      this.jobStatusChart.dispose()
    }
    if (this.jobTypeChart) {
      this.jobTypeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    /**
     * 初始化图表
     */
    initCharts() {
      this.$nextTick(() => {
        this.jobStatusChart = echarts.init(this.$refs.jobStatusChart)
        this.jobTypeChart = echarts.init(this.$refs.jobTypeChart)
        this.trendChart = echarts.init(this.$refs.trendChart)
        
        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },

    /**
     * 处理窗口大小变化
     */
    handleResize() {
      if (this.jobStatusChart) {
        this.jobStatusChart.resize()
      }
      if (this.jobTypeChart) {
        this.jobTypeChart.resize()
      }
      if (this.trendChart) {
        this.trendChart.resize()
      }
    },

    /**
     * 加载仪表板数据
     */
    async loadDashboardData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStatistics(),
          this.loadPendingJobs(),
          this.loadRecentExecutions(),
          this.loadPerformanceMetrics(),
          this.loadSlaMetrics(),
          this.loadSystemHealth(),
          this.loadJobStatusChart(),
          this.loadJobTypeChart(),
          this.loadTrendChart()
        ])
      } catch (error) {
        console.error('加载仪表板数据失败:', error)
      } finally {
        this.loading = false
      }
    },

    /**
     * 加载统计数据
     */
    async loadStatistics() {
      try {
        const response = await jobScheduleApi.getJobScheduleStatistics()
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    /**
     * 加载待处理作业
     */
    async loadPendingJobs() {
      try {
        const response = await jobScheduleApi.getPendingJobs()
        if (response.success) {
          this.pendingJobs = response.data.slice(0, 10) // 只显示前10条
        }
      } catch (error) {
        console.error('加载待处理作业失败:', error)
      }
    },

    /**
     * 加载最近执行记录
     */
    async loadRecentExecutions() {
      try {
        const response = await jobScheduleApi.getJobSchedulePage({
          current: 1,
          size: 10,
          sortField: 'lastExecutionTime',
          sortOrder: 'desc'
        })
        if (response.success) {
          this.recentExecutions = response.data.records
        }
      } catch (error) {
        console.error('加载最近执行记录失败:', error)
      }
    },

    /**
     * 加载性能指标
     */
    async loadPerformanceMetrics() {
      try {
        const response = await jobScheduleApi.getJobPerformanceMetrics()
        if (response.success) {
          this.performanceMetrics = response.data
        }
      } catch (error) {
        console.error('加载性能指标失败:', error)
      }
    },

    /**
     * 加载SLA指标
     */
    async loadSlaMetrics() {
      try {
        const response = await jobScheduleApi.getSlaMetrics()
        if (response.success) {
          this.slaMetrics = response.data
        }
      } catch (error) {
        console.error('加载SLA指标失败:', error)
      }
    },

    /**
     * 加载系统健康度
     */
    async loadSystemHealth() {
      try {
        // 模拟系统健康度数据
        this.systemHealth = {
          status: '正常',
          availability: '99.9%',
          errorRate: '0.1%',
          lastCheckTime: new Date().toISOString()
        }
      } catch (error) {
        console.error('加载系统健康度失败:', error)
      }
    },

    /**
     * 加载作业状态分布图表
     */
    async loadJobStatusChart() {
      try {
        const response = await jobScheduleApi.getJobStatusDistribution()
        if (response.success && this.jobStatusChart) {
          const option = {
            title: {
              text: '作业状态分布',
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
              data: response.data.map(item => item.name)
            },
            series: [
              {
                name: '作业状态',
                type: 'pie',
                radius: '50%',
                data: response.data,
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
          this.jobStatusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载作业状态分布图表失败:', error)
      }
    },

    /**
     * 加载作业类型分布图表
     */
    async loadJobTypeChart() {
      try {
        const response = await jobScheduleApi.getJobTypeDistribution()
        if (response.success && this.jobTypeChart) {
          const option = {
            title: {
              text: '作业类型分布',
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
              data: response.data.map(item => item.name)
            },
            series: [
              {
                name: '作业类型',
                type: 'pie',
                radius: ['40%', '70%'],
                data: response.data,
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
          this.jobTypeChart.setOption(option)
        }
      } catch (error) {
        console.error('加载作业类型分布图表失败:', error)
      }
    },

    /**
     * 加载趋势图表
     */
    async loadTrendChart() {
      try {
        const response = await jobScheduleApi.getJobScheduleTrend({
          period: this.trendPeriod
        })
        if (response.success && this.trendChart) {
          const option = {
            title: {
              text: '作业执行趋势',
              left: 'center',
              textStyle: {
                fontSize: 14
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['总执行数', '成功数', '失败数'],
              top: 30
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              top: '15%',
              containLabel: true
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: response.data.dates
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '总执行数',
                type: 'line',
                stack: 'Total',
                data: response.data.totalExecutions
              },
              {
                name: '成功数',
                type: 'line',
                stack: 'Total',
                data: response.data.successExecutions
              },
              {
                name: '失败数',
                type: 'line',
                stack: 'Total',
                data: response.data.failedExecutions
              }
            ]
          }
          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('加载趋势图表失败:', error)
      }
    },

    /**
     * 刷新作业状态图表
     */
    refreshJobStatusChart() {
      this.loadJobStatusChart()
    },

    /**
     * 刷新作业类型图表
     */
    refreshJobTypeChart() {
      this.loadJobTypeChart()
    },

    /**
     * 刷新趋势图表
     */
    refreshTrendChart() {
      this.loadTrendChart()
    },

    /**
     * 刷新待处理作业
     */
    refreshPendingJobs() {
      this.loadPendingJobs()
    },

    /**
     * 刷新最近执行记录
     */
    refreshRecentExecutions() {
      this.loadRecentExecutions()
    },

    /**
     * 启动作业
     */
    async handleStartJob(row) {
      try {
        const response = await jobScheduleApi.startJobSchedule(row.jobId)
        if (response.success) {
          this.$message.success('启动成功')
          this.loadDashboardData()
        }
      } catch (error) {
        this.$message.error('启动失败: ' + error.message)
      }
    },

    /**
     * 查看作业详情
     */
    handleViewJob(row) {
      this.currentJobId = row.jobId
      this.detailVisible = true
    },

    // 格式化方法
    formatJobType(type) {
      return jobScheduleApi.utils.formatJobType(type)
    },

    formatExecutionStatus(status) {
      return jobScheduleApi.utils.formatExecutionStatus(status)
    },

    formatPriority(priority) {
      return jobScheduleApi.utils.formatPriority(priority)
    },

    formatDuration(seconds) {
      return jobScheduleApi.utils.formatDuration(seconds)
    },

    // 标签类型方法
    getJobTypeTagType(type) {
      const typeMap = {
        'BATCH': 'primary',
        'REALTIME': 'success',
        'SCHEDULED': 'info',
        'TRIGGER': 'warning',
        'WORKFLOW': 'danger',
        'ETL': 'primary',
        'BACKUP': 'info',
        'MAINTENANCE': 'warning'
      }
      return typeMap[type] || 'info'
    },

    getExecutionStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'TIMEOUT': 'warning',
        'CANCELLED': 'info',
        'STOPPED': 'info'
      }
      return statusMap[status] || 'info'
    },

    getPriorityTagType(priority) {
      if (priority <= 2) return 'danger'
      if (priority <= 5) return 'warning'
      if (priority <= 8) return 'info'
      return 'success'
    },

    getSystemStatusClass(status) {
      const statusMap = {
        '正常': 'status-normal',
        '警告': 'status-warning',
        '异常': 'status-error'
      }
      return statusMap[status] || 'status-normal'
    }
  }
}
</script>

<style scoped>
.job-schedule-dashboard {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 100px;
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
  font-size: 24px;
  color: white;
  margin-right: 15px;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.running {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.success {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.failed {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
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

.chart-row,
.table-row,
.metrics-row {
  margin-bottom: 20px;
}

.chart-card,
.table-card,
.metric-card {
  height: 400px;
}

.chart-container {
  height: 300px;
}

.chart-container-large {
  height: 350px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.metric-content {
  padding: 20px 0;
}

.metric-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 0 10px;
}

.metric-label {
  font-size: 14px;
  color: #606266;
}

.metric-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.status-normal {
  color: #67c23a;
}

.status-warning {
  color: #e6a23c;
}

.status-error {
  color: #f56c6c;
}
</style>
</script>
