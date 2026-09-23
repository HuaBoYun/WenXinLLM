<template>
  <div class="app-container">
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span class="card-title">归集监控报表</span>
        <el-button style="float: right; padding: 3px 10px" type="text" icon="el-icon-refresh" @click="refreshData">刷新</el-button>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409EFF">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.totalTasks || 0 }}</div>
              <div class="stat-label">任务总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67C23A">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.enabledTasks || 0 }}</div>
              <div class="stat-label">启用任务</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon" style="background: #E6A23C">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.todayExecutions || 0 }}</div>
              <div class="stat-label">今日执行</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon" style="background: #F56C6C">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ statistics.todaySuccessRate || 0 }}%</div>
              <div class="stat-label">今日成功率</div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <!-- 执行趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-title">执行趋势（最近7天）</div>
            <div ref="trendChart" class="chart-container"></div>
          </div>
        </el-col>

        <!-- 任务状态分布图 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-title">任务状态分布</div>
            <div ref="statusChart" class="chart-container"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <!-- 失败任务TOP10 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-title">失败任务TOP10（最近30天）</div>
            <el-table :data="failedTasks" border max-height="350">
              <el-table-column label="序号" type="index" width="60" align="center" />
              <el-table-column label="任务编码" prop="taskCode" width="120" show-overflow-tooltip />
              <el-table-column label="任务名称" prop="taskName" show-overflow-tooltip />
              <el-table-column label="失败次数" prop="failedCount" width="90" align="center">
                <template slot-scope="scope">
                  <el-tag type="danger">{{ scope.row.failedCount }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>

        <!-- 执行时长统计 -->
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-title">执行时长TOP10</div>
            <div ref="durationChart" class="chart-container"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细统计 -->
      <el-row :gutter="20" class="detail-row">
        <el-col :span="24">
          <div class="detail-card">
            <div class="detail-title">详细统计</div>
            <el-descriptions :column="4" border>
              <el-descriptions-item label="本周执行次数">{{ statistics.weekExecutions || 0 }}</el-descriptions-item>
              <el-descriptions-item label="本月执行次数">{{ statistics.monthExecutions || 0 }}</el-descriptions-item>
              <el-descriptions-item label="累计归集记录">{{ formatNumber(statistics.totalRecords) }}</el-descriptions-item>
              <el-descriptions-item label="累计成功记录">{{ formatNumber(statistics.totalSuccessRecords) }}</el-descriptions-item>
              <el-descriptions-item label="累计失败记录">{{ formatNumber(statistics.totalFailedRecords) }}</el-descriptions-item>
              <el-descriptions-item label="平均执行时长">{{ formatDuration(statistics.avgExecutionDuration) }}</el-descriptions-item>
              <el-descriptions-item label="定时任务数">{{ statistics.scheduledTasks || 0 }}</el-descriptions-item>
              <el-descriptions-item label="今日成功次数">{{ statistics.todaySuccessCount || 0 }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  getOverallStatistics,
  getTaskExecutionTrend,
  getTaskStatusDistribution,
  getTopFailedTasks,
  getExecutionDurationStatistics
} from '@/api/financialSharing/dataCollection'
import * as echarts from 'echarts'

export default {
  name: 'CollectionMonitor',
  data() {
    return {
      loading: false,
      statistics: {},
      trendData: [],
      statusData: [],
      failedTasks: [],
      durationData: [],
      trendChart: null,
      statusChart: null,
      durationChart: null
    }
  },
  created() {
    this.loadData()
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
    if (this.statusChart) {
      this.statusChart.dispose()
    }
    if (this.durationChart) {
      this.durationChart.dispose()
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStatistics(),
          this.loadTrendData(),
          this.loadStatusData(),
          this.loadFailedTasks(),
          this.loadDurationData()
        ])
      } finally {
        this.loading = false
      }
    },
    // 加载统计数据
    async loadStatistics() {
      const response = await getOverallStatistics()
      if (response.code === 1) {
        this.statistics = response.data
      }
    },
    // 加载趋势数据
    async loadTrendData() {
      const response = await getTaskExecutionTrend({ days: 7 })
      if (response.code === 1) {
        this.trendData = response.data
        this.$nextTick(() => {
          this.initTrendChart()
        })
      }
    },
    // 加载状态分布数据
    async loadStatusData() {
      const response = await getTaskStatusDistribution()
      if (response.code === 1) {
        this.statusData = response.data
        this.$nextTick(() => {
          this.initStatusChart()
        })
      }
    },
    // 加载失败任务
    async loadFailedTasks() {
      const response = await getTopFailedTasks({ days: 30 })
      if (response.code === 1) {
        this.failedTasks = response.data
      }
    },
    // 加载执行时长数据
    async loadDurationData() {
      const response = await getExecutionDurationStatistics()
      if (response.code === 1) {
        this.durationData = response.data
        this.$nextTick(() => {
          this.initDurationChart()
        })
      }
    },
    // 初始化趋势图表
    initTrendChart() {
      if (!this.$refs.trendChart) return

      if (this.trendChart) {
        this.trendChart.dispose()
      }

      this.trendChart = echarts.init(this.$refs.trendChart)

      const dates = this.trendData.map(item => item.date)
      const successCounts = this.trendData.map(item => item.successCount)
      const failedCounts = this.trendData.map(item => item.failedCount)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['成功', '失败']
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
            name: '成功',
            type: 'line',
            data: successCounts,
            smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '失败',
            type: 'line',
            data: failedCounts,
            smooth: true,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }

      this.trendChart.setOption(option)
    },
    // 初始化状态分布图表
    initStatusChart() {
      if (!this.$refs.statusChart) return

      if (this.statusChart) {
        this.statusChart.dispose()
      }

      this.statusChart = echarts.init(this.$refs.statusChart)

      const data = this.statusData.map(item => ({
        name: item.statusName,
        value: item.taskCount
      }))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            type: 'pie',
            radius: '60%',
            data: data,
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
    // 初始化执行时长图表
    initDurationChart() {
      if (!this.$refs.durationChart) return

      if (this.durationChart) {
        this.durationChart.dispose()
      }

      this.durationChart = echarts.init(this.$refs.durationChart)

      const taskNames = this.durationData.map(item => item.date)
      const durations = this.durationData.map(item => (item.executionCount / 1000).toFixed(2))

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: '{b}: {c}秒'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          name: '时长(秒)'
        },
        yAxis: {
          type: 'category',
          data: taskNames
        },
        series: [
          {
            type: 'bar',
            data: durations,
            itemStyle: { color: '#409EFF' }
          }
        ]
      }

      this.durationChart.setOption(option)
    },
    // 刷新数据
    refreshData() {
      this.loadData()
      this.$message.success('刷新成功')
    },
    // 格式化数字
    formatNumber(num) {
      if (!num) return '0'
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    // 格式化执行时长
    formatDuration(duration) {
      if (!duration) return '-'
      if (duration < 1000) {
        return duration + 'ms'
      } else if (duration < 60000) {
        return (duration / 1000).toFixed(2) + 's'
      } else {
        const minutes = Math.floor(duration / 60000)
        const seconds = ((duration % 60000) / 1000).toFixed(0)
        return minutes + 'm' + seconds + 's'
      }
    }
  }
}
</script>

<style scoped>
.card-title {
  font-size: 18px;
  font-weight: bold;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-icon i {
  font-size: 28px;
  color: #fff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
}

.chart-container {
  width: 100%;
  height: 350px;
}

.detail-row {
  margin-top: 20px;
}

.detail-card {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.detail-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
}
</style>

