<template>
  <div class="cost-allocation-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">总分摊数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatAmount(statistics.totalAmount) }}</div>
              <div class="stat-label">总分摊金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 状态分布饼图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span>分摊状态分布</span>
            <el-button type="text" @click="refreshStatusChart">刷新</el-button>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 类型分布饼图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span>分摊类型分布</span>
            <el-button type="text" @click="refreshTypeChart">刷新</el-button>
          </div>
          <div ref="typeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 趋势图 -->
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span>分摊趋势分析</span>
            <div class="header-controls">
              <el-date-picker
                v-model="trendDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                size="small"
                @change="refreshTrendChart"
              />
              <el-button type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container trend-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 排行榜和待处理事项 -->
    <el-row :gutter="20" class="bottom-row">
      <!-- 分摊排行榜 -->
      <el-col :span="12">
        <el-card class="ranking-card">
          <div slot="header" class="card-header">
            <span>分摊金额排行榜</span>
            <el-select v-model="rankType" size="small" @change="refreshRanking">
              <el-option label="按金额" value="amount" />
              <el-option label="按数量" value="count" />
              <el-option label="按成功率" value="success_rate" />
            </el-select>
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in rankingData"
              :key="index"
              class="ranking-item"
            >
              <div class="rank-number" :class="getRankClass(index)">
                {{ index + 1 }}
              </div>
              <div class="rank-info">
                <div class="rank-name">{{ item.name }}</div>
                <div class="rank-value">{{ formatRankValue(item.value, rankType) }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 待处理事项 -->
      <el-col :span="12">
        <el-card class="todo-card">
          <div slot="header" class="card-header">
            <span>待处理事项</span>
            <el-button type="text" @click="refreshTodoList">刷新</el-button>
          </div>
          <div class="todo-list">
            <div
              v-for="item in todoList"
              :key="item.id"
              class="todo-item"
              @click="handleTodoClick(item)"
            >
              <div class="todo-icon" :class="getTodoIconClass(item.type)">
                <i :class="getTodoIcon(item.type)"></i>
              </div>
              <div class="todo-content">
                <div class="todo-title">{{ item.title }}</div>
                <div class="todo-desc">{{ item.description }}</div>
                <div class="todo-time">{{ formatTime(item.time) }}</div>
              </div>
              <div class="todo-action">
                <el-button type="text" size="mini">处理</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <el-row class="quick-actions">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>快速操作</span>
          </div>
          <div class="action-buttons">
            <el-button type="primary" icon="el-icon-plus" @click="createAllocation">
              新建分摊
            </el-button>
            <el-button type="success" icon="el-icon-video-play" @click="batchStart">
              批量开始
            </el-button>
            <el-button type="warning" icon="el-icon-video-pause" @click="batchStop">
              批量停止
            </el-button>
            <el-button type="info" icon="el-icon-check" @click="batchApprove">
              批量审批
            </el-button>
            <el-button type="primary" icon="el-icon-document" @click="generateReport">
              生成报告
            </el-button>
            <el-button type="success" icon="el-icon-download" @click="exportData">
              导出数据
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
  getCostAllocationStatistics,
  getAllocationStatusDistribution,
  getAllocationTypeDistribution,
  getCostAllocationTrend,
  getCostAllocationRanking,
  getPendingAllocation,
  getActiveAllocation,
  exportCostAllocationData,
  costAllocationUtils
} from '@/api/managementAccountant/ss/costAllocation'

export default {
  name: 'CostAllocationDashboard',
  data() {
    return {
      loading: false,
      statistics: {},
      statusChart: null,
      typeChart: null,
      trendChart: null,
      trendDateRange: [],
      rankType: 'amount',
      rankingData: [],
      todoList: []
    }
  },
  mounted() {
    this.initData()
    this.initCharts()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 初始化数据
    async initData() {
      await Promise.all([
        this.loadStatistics(),
        this.loadStatusDistribution(),
        this.loadTypeDistribution(),
        this.loadTrendData(),
        this.loadRankingData(),
        this.loadTodoList()
      ])
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initStatusChart()
        this.initTypeChart()
        this.initTrendChart()
      })
    },

    // 销毁图表
    destroyCharts() {
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

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getCostAllocationStatistics()
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
        const response = await getAllocationStatusDistribution()
        if (response.success) {
          this.updateStatusChart(response.data)
        }
      } catch (error) {
        console.error('加载状态分布数据失败:', error)
      }
    },

    // 加载类型分布数据
    async loadTypeDistribution() {
      try {
        const response = await getAllocationTypeDistribution()
        if (response.success) {
          this.updateTypeChart(response.data)
        }
      } catch (error) {
        console.error('加载类型分布数据失败:', error)
      }
    },

    // 加载趋势数据
    async loadTrendData() {
      try {
        const endDate = new Date()
        const startDate = new Date()
        startDate.setDate(endDate.getDate() - 30)

        const response = await getCostAllocationTrend(
          startDate.toISOString().split('T')[0],
          endDate.toISOString().split('T')[0]
        )
        if (response.success) {
          this.updateTrendChart(response.data)
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },

    // 加载排行榜数据
    async loadRankingData() {
      try {
        const response = await getCostAllocationRanking(this.rankType, 10)
        if (response.success) {
          this.rankingData = response.data
        }
      } catch (error) {
        console.error('加载排行榜数据失败:', error)
      }
    },

    // 加载待处理事项
    async loadTodoList() {
      try {
        const [pendingResponse, activeResponse] = await Promise.all([
          getPendingAllocation(),
          getActiveAllocation()
        ])

        const todoItems = []

        if (pendingResponse.success) {
          pendingResponse.data.forEach(item => {
            todoItems.push({
              id: item.allocationId,
              type: 'pending',
              title: `待分摊: ${item.allocationName}`,
              description: `金额: ${this.formatAmount(item.totalCostAmount)}`,
              time: item.createdTime,
              data: item
            })
          })
        }

        if (activeResponse.success) {
          activeResponse.data.forEach(item => {
            if (item.calculationStatus === 'RUNNING') {
              todoItems.push({
                id: item.allocationId,
                type: 'running',
                title: `计算中: ${item.allocationName}`,
                description: `进度: ${item.calculationProgress || 0}%`,
                time: item.calculationStartTime,
                data: item
              })
            }
          })
        }

        this.todoList = todoItems.slice(0, 10) // 只显示前10条
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },

    // 初始化状态图表
    initStatusChart() {
      this.statusChart = echarts.init(this.$refs.statusChart)
      const option = {
        title: {
          text: '状态分布',
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
          data: []
        },
        series: [
          {
            name: '分摊状态',
            type: 'pie',
            radius: '50%',
            data: [],
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

    // 初始化类型图表
    initTypeChart() {
      this.typeChart = echarts.init(this.$refs.typeChart)
      const option = {
        title: {
          text: '类型分布',
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
          data: []
        },
        series: [
          {
            name: '分摊类型',
            type: 'pie',
            radius: '50%',
            data: [],
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

    // 初始化趋势图表
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      const option = {
        title: {
          text: '分摊趋势',
          left: 'center',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['分摊数量', '分摊金额']
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: [
          {
            type: 'value',
            name: '数量',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额',
            position: 'right'
          }
        ],
        series: [
          {
            name: '分摊数量',
            type: 'line',
            data: []
          },
          {
            name: '分摊金额',
            type: 'bar',
            yAxisIndex: 1,
            data: []
          }
        ]
      }
      this.trendChart.setOption(option)
    },

    // 更新状态图表
    updateStatusChart(data) {
      if (!this.statusChart) return

      const chartData = data.map(item => ({
        name: costAllocationUtils.formatAllocationStatus(item.status),
        value: item.count
      }))

      this.statusChart.setOption({
        legend: {
          data: chartData.map(item => item.name)
        },
        series: [
          {
            data: chartData
          }
        ]
      })
    },

    // 更新类型图表
    updateTypeChart(data) {
      if (!this.typeChart) return

      const chartData = data.map(item => ({
        name: costAllocationUtils.formatAllocationType(item.type),
        value: item.count
      }))

      this.typeChart.setOption({
        legend: {
          data: chartData.map(item => item.name)
        },
        series: [
          {
            data: chartData
          }
        ]
      })
    },

    // 更新趋势图表
    updateTrendChart(data) {
      if (!this.trendChart) return

      const dates = data.map(item => item.date)
      const counts = data.map(item => item.count)
      const amounts = data.map(item => item.amount)

      this.trendChart.setOption({
        xAxis: {
          data: dates
        },
        series: [
          {
            data: counts
          },
          {
            data: amounts
          }
        ]
      })
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
    async refreshTrendChart() {
      if (this.trendDateRange && this.trendDateRange.length === 2) {
        try {
          const response = await getCostAllocationTrend(
            this.trendDateRange[0].toISOString().split('T')[0],
            this.trendDateRange[1].toISOString().split('T')[0]
          )
          if (response.success) {
            this.updateTrendChart(response.data)
          }
        } catch (error) {
          console.error('刷新趋势图表失败:', error)
        }
      } else {
        this.loadTrendData()
      }
    },

    // 刷新排行榜
    refreshRanking() {
      this.loadRankingData()
    },

    // 刷新待处理事项
    refreshTodoList() {
      this.loadTodoList()
    },

    // 处理待办事项点击
    handleTodoClick(item) {
      this.$emit('view-allocation', item.data.allocationId)
    },

    // 快速操作
    createAllocation() {
      this.$emit('create-allocation')
    },

    batchStart() {
      this.$emit('batch-start')
    },

    batchStop() {
      this.$emit('batch-stop')
    },

    batchApprove() {
      this.$emit('batch-approve')
    },

    generateReport() {
      this.$emit('generate-report')
    },

    async exportData() {
      try {
        const response = await exportCostAllocationData({})
        if (response.success) {
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 工具方法
    formatAmount: costAllocationUtils.formatAmount,

    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },

    getRankClass(index) {
      if (index === 0) return 'rank-first'
      if (index === 1) return 'rank-second'
      if (index === 2) return 'rank-third'
      return ''
    },

    formatRankValue(value, type) {
      switch (type) {
        case 'amount':
          return this.formatAmount(value)
        case 'count':
          return value + '个'
        case 'success_rate':
          return costAllocationUtils.formatPercentage(value)
        default:
          return value
      }
    },

    getTodoIconClass(type) {
      const classMap = {
        'pending': 'todo-pending',
        'running': 'todo-running',
        'failed': 'todo-failed'
      }
      return classMap[type] || 'todo-default'
    },

    getTodoIcon(type) {
      const iconMap = {
        'pending': 'el-icon-time',
        'running': 'el-icon-loading',
        'failed': 'el-icon-warning'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style scoped>
.cost-allocation-dashboard {
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
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.amount {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: 320px;
}

.trend-chart {
  height: 350px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bottom-row {
  margin-bottom: 20px;
}

.ranking-card,
.todo-card {
  height: 400px;
}

.ranking-list,
.todo-list {
  height: 320px;
  overflow-y: auto;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.rank-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-weight: bold;
  color: white;
  background-color: #909399;
}

.rank-number.rank-first {
  background-color: #ffd700;
}

.rank-number.rank-second {
  background-color: #c0c0c0;
}

.rank-number.rank-third {
  background-color: #cd7f32;
}

.rank-info {
  flex: 1;
}

.rank-name {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.rank-value {
  font-size: 12px;
  color: #909399;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.todo-item:hover {
  background-color: #f5f7fa;
}

.todo-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 16px;
  color: white;
}

.todo-icon.todo-pending {
  background-color: #e6a23c;
}

.todo-icon.todo-running {
  background-color: #409eff;
}

.todo-icon.todo-failed {
  background-color: #f56c6c;
}

.todo-icon.todo-default {
  background-color: #909399;
}

.todo-content {
  flex: 1;
}

.todo-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 5px;
}

.todo-desc {
  font-size: 12px;
  color: #606266;
  margin-bottom: 5px;
}

.todo-time {
  font-size: 12px;
  color: #909399;
}

.todo-action {
  margin-left: 10px;
}

.quick-actions {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}
</style>
