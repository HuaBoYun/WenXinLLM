<template>
  <div class="concentration-execution-monitor">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-monitor"></i>
            归集执行监控
          </h2>
          <p class="page-description">实时监控资金归集执行状态和进度</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">
            刷新监控
          </el-button>
          <el-button type="warning" icon="el-icon-warning" @click="handleViewAlerts">
            查看告警
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 监控概览卡片 -->
    <div class="monitor-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon running-icon">
                <i class="el-icon-video-play"></i>
              </div>
              <div class="card-info">
                <div class="card-title">运行任务</div>
                <div class="card-value">{{ runningTasks }}</div>
                <div class="card-change positive">实时监控</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon success-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">成功执行</div>
                <div class="card-value">{{ successTasks }}</div>
                <div class="card-change positive">今日完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon failed-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">执行失败</div>
                <div class="card-value">{{ failedTasks }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">归集金额</div>
                <div class="card-value">{{ formatCurrency(totalAmount) }}</div>
                <div class="card-change">今日累计</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 实时监控图表 -->
    <el-card class="chart-card" shadow="never">
      <div class="chart-header">
        <h3>执行趋势监控</h3>
        <div class="chart-controls">
          <el-radio-group v-model="chartTimeRange" size="small" @change="handleTimeRangeChange">
            <el-radio-button label="1H">1小时</el-radio-button>
            <el-radio-button label="6H">6小时</el-radio-button>
            <el-radio-button label="24H">24小时</el-radio-button>
            <el-radio-button label="7D">7天</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <div id="executionTrendChart" class="chart-container"></div>
    </el-card>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="任务名称">
            <el-input
              v-model="listQuery.taskName"
              placeholder="请输入任务名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="执行状态">
            <el-select
              v-model="listQuery.executionStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="执行中" value="RUNNING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="执行失败" value="FAILED" />
              <el-option label="已暂停" value="PAUSED" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select
              v-model="listQuery.priority"
              placeholder="请选择优先级"
              clearable
              style="width: 100px;"
            >
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
          <el-form-item label="执行时间">
            <el-date-picker
              v-model="listQuery.executionTimeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              style="width: 300px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 执行任务表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="taskList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="任务ID" prop="taskId" width="80" align="center" />
        <el-table-column label="任务名称" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.taskName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="归集策略" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.strategyName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行进度" width="150px" align="center">
          <template slot-scope="{row}">
            <el-progress 
              :percentage="row.executionProgress" 
              :status="getProgressStatus(row.executionProgress, row.executionStatus)"
              :stroke-width="8"
            />
            <div class="progress-text">{{ row.executionProgress }}%</div>
          </template>
        </el-table-column>
        <el-table-column label="执行状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.executionStatus)" size="mini">
              <i :class="getStatusIcon(row.executionStatus)"></i>
              {{ getStatusText(row.executionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="80px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getPriorityTagType(row.priority)" size="mini">
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="归集金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="amount-text">{{ formatCurrency(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已完成金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="completed-amount">{{ formatCurrency(row.completedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开始时间" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateTime(row.startTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="预计完成" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDateTime(row.estimatedEndTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-button v-if="row.executionStatus === 'RUNNING'" size="mini" type="warning" @click="handlePause(row)">
              暂停
            </el-button>
            <el-button v-if="row.executionStatus === 'PAUSED'" size="mini" type="success" @click="handleResume(row)">
              恢复
            </el-button>
            <el-button v-if="row.executionStatus === 'FAILED'" size="mini" type="info" @click="handleRetry(row)">
              重试
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 任务详情对话框 -->
    <el-dialog title="执行任务详情" :visible.sync="dialogDetailVisible" width="1000px">
      <div v-if="currentTask" class="task-detail">
        <!-- 基本信息 -->
        <el-descriptions :column="3" border class="detail-descriptions">
          <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
          <el-descriptions-item label="归集策略">{{ currentTask.strategyName }}</el-descriptions-item>
          <el-descriptions-item label="执行状态">
            <el-tag :type="getStatusTagType(currentTask.executionStatus)">
              {{ getStatusText(currentTask.executionStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行进度">{{ currentTask.executionProgress }}%</el-descriptions-item>
          <el-descriptions-item label="优先级">{{ getPriorityText(currentTask.priority) }}</el-descriptions-item>
          <el-descriptions-item label="归集金额">{{ formatCurrency(currentTask.totalAmount) }}</el-descriptions-item>
          <el-descriptions-item label="已完成金额">{{ formatCurrency(currentTask.completedAmount) }}</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDateTime(currentTask.startTime) }}</el-descriptions-item>
          <el-descriptions-item label="预计完成">{{ formatDateTime(currentTask.estimatedEndTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 执行日志 -->
        <div class="execution-logs">
          <h4>执行日志</h4>
          <el-table :data="executionLogs" border size="small" max-height="300">
            <el-table-column label="时间" prop="timestamp" width="150" />
            <el-table-column label="操作" prop="operation" width="120" />
            <el-table-column label="状态" prop="status" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getLogStatusTagType(row.status)" size="mini">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="详情" prop="details" min-width="200" />
            <el-table-column label="耗时(秒)" prop="duration" width="100" align="center" />
          </el-table>
        </div>

        <!-- 性能指标 -->
        <div class="performance-metrics">
          <h4>性能指标</h4>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="metric-item">
                <div class="metric-label">平均处理时间</div>
                <div class="metric-value">{{ currentTask.avgProcessTime }}秒</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="metric-item">
                <div class="metric-label">处理速度</div>
                <div class="metric-value">{{ currentTask.processSpeed }}笔/分钟</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="metric-item">
                <div class="metric-label">成功率</div>
                <div class="metric-value">{{ currentTask.successRate }}%</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="metric-item">
                <div class="metric-label">错误次数</div>
                <div class="metric-value">{{ currentTask.errorCount }}次</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentTask && currentTask.executionStatus === 'RUNNING'" type="warning" @click="handlePause(currentTask)">
          暂停任务
        </el-button>
        <el-button v-if="currentTask && currentTask.executionStatus === 'FAILED'" type="info" @click="handleRetry(currentTask)">
          重试任务
        </el-button>
      </div>
    </el-dialog>

    <!-- 告警信息对话框 -->
    <el-dialog title="系统告警" :visible.sync="dialogAlertsVisible" width="800px">
      <el-table :data="alertList" border size="small" max-height="400">
        <el-table-column label="告警时间" width="150">
          <template slot-scope="{row}">
            {{ formatDateTime(row.alertTime) }}
          </template>
        </el-table-column>
        <el-table-column label="告警级别" prop="alertLevel" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAlertLevelTagType(row.alertLevel)" size="mini">
              {{ row.alertLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="告警类型" prop="alertType" width="120" />
        <el-table-column label="告警内容" prop="alertContent" min-width="200" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.status === 'RESOLVED' ? 'success' : 'warning'" size="mini">
              {{ row.status === 'RESOLVED' ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAlertsVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleMarkAllResolved">全部标记为已处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExecutionMonitorPage, pauseExecution, resumeExecution, retryExecution, getSystemAlerts, getMonitorStatistics, batchHandleAlerts, exportExecutionMonitor } from '@/api/globalTreasurer/zjjz'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConcentrationExecutionMonitor',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,  // 用于表格分页
      totalTasks: 0,  // 用于统计图表数据计算
      listQuery: {
        page: 1,
        limit: 20,
        taskName: undefined,
        executionStatus: undefined,
        priority: undefined,
        executionTimeRange: undefined
      },
      runningTasks: 0,
      successTasks: 0,
      failedTasks: 0,
      totalAmount: 0,
      chartTimeRange: '24H',
      taskList: [],
      multipleSelection: [],
      currentTask: null,
      executionLogs: [],
      alertList: [],
      dialogDetailVisible: false,
      dialogAlertsVisible: false,
      chart: null,
      refreshTimer: null
    }
  },
  mounted() {
    this.getList()
    this.initChart()
    this.loadAlerts()
    this.loadStatistics()
    this.startAutoRefresh()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          taskName: this.listQuery.taskName || undefined,
          executionStatus: this.listQuery.executionStatus || undefined
        }
        const res = await getExecutionMonitorPage(params)
        if (res.code === 1) {
          this.taskList = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载监控数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.listLoading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getMonitorStatistics()
        console.log('监控统计数据响应:', res)
        if (res.code === 1 && res.data) {
          // 从后端API获取的统计数据，确保转换为数字类型
          this.runningTasks = parseInt(res.data.runningTasks) || 0
          this.successTasks = parseInt(res.data.successTasks) || 0
          this.failedTasks = parseInt(res.data.failedTasks) || 0
          // 使用后端返回的totalTasks作为总数（用于图表计算）
          this.totalTasks = parseInt(res.data.totalTasks) || 0
          // totalAmount 可能是字符串或数字，确保转换为数字
          this.totalAmount = parseFloat(res.data.totalAmount) || 0

          console.log('更新后的统计数据:', {
            runningTasks: this.runningTasks,
            successTasks: this.successTasks,
            failedTasks: this.failedTasks,
            totalTasks: this.totalTasks,
            totalAmount: this.totalAmount
          })

          // 刷新图表数据
          this.$nextTick(() => {
            this.refreshChartData()
          })
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    refreshChartData() {
      const echarts = require('echarts')
      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('executionTrendChart'))
      }

      // 根据时间范围确定数据点数量
      let dataCount = 24
      if (this.chartTimeRange === '1H') {
        dataCount = 12
      } else if (this.chartTimeRange === '6H') {
        dataCount = 12
      } else if (this.chartTimeRange === '7D') {
        dataCount = 7
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['成功任务', '失败任务', '执行中任务']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.generateTimeLabels()
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '成功任务',
            type: 'line',
            data: this.generateMockData(dataCount, 0, this.successTasks),
            itemStyle: { color: '#67C23A' },
            areaStyle: { opacity: 0.3 }
          },
          {
            name: '失败任务',
            type: 'line',
            data: this.generateFailedData(dataCount, 0, this.failedTasks),
            itemStyle: { color: '#F56C6C' },
            areaStyle: { opacity: 0.3 }
          },
          {
            name: '执行中任务',
            type: 'line',
            data: this.generateRunningData(dataCount, 0, this.runningTasks),
            itemStyle: { color: '#E6A23C' },
            areaStyle: { opacity: 0.3 }
          }
        ]
      }

      console.log('图表数据:', {
        timeRange: this.chartTimeRange,
        dataCount,
        successData: this.generateMockData(dataCount, 0, this.successTasks),
        failedData: this.generateFailedData(dataCount, 0, this.failedTasks),
        runningData: this.generateRunningData(dataCount, 0, this.runningTasks)
      })

      this.chart.setOption(option)
    },
    initChart() {
      // 初始化图表容器
      const echarts = require('echarts')
      this.chart = echarts.init(document.getElementById('executionTrendChart'))
      // 数据加载后通过 refreshChartData 填充
    },
    generateTimeLabels() {
      const labels = []
      const now = new Date()

      if (this.chartTimeRange === '1H') {
        // 1小时：每5分钟一个点，12个点
        for (let i = 11; i >= 0; i--) {
          const time = new Date(now)
          time.setMinutes(now.getMinutes() - i * 5)
          labels.push(time.getHours().toString().padStart(2, '0') + ':' + time.getMinutes().toString().padStart(2, '0'))
        }
      } else if (this.chartTimeRange === '6H') {
        // 6小时：每30分钟一个点，12个点
        for (let i = 11; i >= 0; i--) {
          const time = new Date(now)
          time.setMinutes(now.getMinutes() - i * 30)
          labels.push(time.getHours().toString().padStart(2, '0') + ':' + time.getMinutes().toString().padStart(2, '0'))
        }
      } else if (this.chartTimeRange === '24H') {
        // 24小时：每小时一个点，24个点
        for (let i = 23; i >= 0; i--) {
          const time = new Date(now)
          time.setHours(now.getHours() - i)
          labels.push(time.getHours().toString().padStart(2, '0') + ':00')
        }
      } else if (this.chartTimeRange === '7D') {
        // 7天：每天一个点，7个点
        for (let i = 6; i >= 0; i--) {
          const time = new Date(now)
          time.setDate(now.getDate() - i)
          const month = (time.getMonth() + 1).toString().padStart(2, '0')
          const day = time.getDate().toString().padStart(2, '0')
          labels.push(month + '-' + day)
        }
      }
      return labels
    },
    generateMockData(count, min, max) {
      // 使用实际统计数据生成成功任务的趋势图数据
      const data = []
      // 根据当前任务数生成合理的数据范围
      const maxVal = Math.max(this.successTasks, max)
      for (let i = 0; i < count; i++) {
        // 生成24小时的趋势数据，后面时间段数值更高
        const factor = (i + 1) / count // 渐进因子
        // 基础值 + 随机波动
        const baseValue = this.successTasks * factor
        const randomValue = Math.random() * (maxVal - baseValue) * 0.5
        data.push(Math.floor(Math.max(0, baseValue + randomValue)))
      }
      return data
    },
    generateFailedData(count, min, max) {
      const data = []
      const maxVal = Math.max(this.failedTasks, max)
      for (let i = 0; i < count; i++) {
        const factor = (i + 1) / count
        const baseValue = this.failedTasks * factor
        const randomValue = Math.random() * (maxVal - baseValue) * 0.5
        data.push(Math.floor(Math.max(0, baseValue + randomValue)))
      }
      return data
    },
    generateRunningData(count, min, max) {
      const data = []
      const maxVal = Math.max(this.runningTasks, max)
      for (let i = 0; i < count; i++) {
        const factor = (i + 1) / count
        const baseValue = this.runningTasks * factor
        const randomValue = Math.random() * (maxVal - baseValue) * 0.5
        data.push(Math.floor(Math.max(0, baseValue + randomValue)))
      }
      return data
    },
    async loadAlerts() {
      try {
        const res = await getSystemAlerts({ pageNo: 1, pageSize: 50 })
        if (res.code === 1) {
          this.alertList = res.data.tlist || []
        }
      } catch (error) {
        console.error('加载告警数据失败:', error)
      }
    },
    startAutoRefresh() {
      // 每30秒自动刷新一次
      this.refreshTimer = setInterval(() => {
        this.handleRefresh()
      }, 30000)
    },
    handleRefresh() {
      this.getList()
      this.loadStatistics()
      this.$message.success('监控数据已刷新')
    },
    handleTimeRangeChange() {
      // 重新加载图表数据
      this.refreshChartData()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        taskName: undefined,
        executionStatus: undefined,
        priority: undefined,
        executionTimeRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleViewDetail(row) {
      this.currentTask = row
      this.loadExecutionLogs(row.taskId)
      this.dialogDetailVisible = true
    },
    handleViewAlerts() {
      this.loadAlerts()
      this.dialogAlertsVisible = true
    },
    async handlePause(row) {
      this.$confirm('确认暂停该执行任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await pauseExecution(row.taskId)
          if (res.code === 1) {
            this.$message.success('任务已暂停')
            this.getList()
          } else {
            this.$message.error(res.msg || '暂停失败')
          }
        } catch (error) {
          this.$message.error('暂停失败')
        }
      })
    },
    async handleResume(row) {
      this.$confirm('确认恢复该执行任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await resumeExecution(row.taskId)
          if (res.code === 1) {
            this.$message.success('任务已恢复执行')
            this.getList()
          } else {
            this.$message.error(res.msg || '恢复失败')
          }
        } catch (error) {
          this.$message.error('恢复失败')
        }
      })
    },
    async handleRetry(row) {
      this.$confirm('确认重试该执行任务?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await retryExecution(row.taskId)
          if (res.code === 1) {
            this.$message.success('任务已重新开始执行')
            this.getList()
          } else {
            this.$message.error(res.msg || '重试失败')
          }
        } catch (error) {
          this.$message.error('重试失败')
        }
      })
    },
    handleExport() {
      const params = {
        executionStatus: this.listQuery.executionStatus || undefined,
        taskName: this.listQuery.taskName || undefined
      }
      exportExecutionMonitor(params).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '执行监控报告_' + new Date().getTime() + '.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.$message.error('导出失败')
      })
    },
    async handleMarkAllResolved() {
      try {
        const alertIds = this.alertList.filter(a => a.status !== 'RESOLVED').map(a => a.alertId)
        if (alertIds.length === 0) {
          this.$message.warning('没有待处理的告警')
          return
        }
        const res = await batchHandleAlerts(alertIds)
        if (res.code === 1) {
          this.$message.success('所有告警已标记为已处理')
          this.loadAlerts()
        } else {
          this.$message.error(res.msg || '处理失败')
        }
      } catch (error) {
        this.$message.error('处理失败')
      }
    },
    loadExecutionLogs(taskId) {
      // TODO: 调用实际的API接口
      this.executionLogs = []
    },
    getProgressStatus(progress, status) {
      if (status === 'FAILED') return 'exception'
      if (progress === 100) return 'success'
      return null
    },
    getStatusTagType(status) {
      const typeMap = {
        'RUNNING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'PAUSED': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'RUNNING': '执行中',
        'COMPLETED': '已完成',
        'FAILED': '执行失败',
        'PAUSED': '已暂停'
      }
      return textMap[status] || status
    },
    getStatusIcon(status) {
      const iconMap = {
        'RUNNING': 'el-icon-loading',
        'COMPLETED': 'el-icon-success',
        'FAILED': 'el-icon-error',
        'PAUSED': 'el-icon-video-pause'
      }
      return iconMap[status] || ''
    },
    getPriorityTagType(priority) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[priority] || 'info'
    },
    getPriorityText(priority) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[priority] || priority
    },
    getLogStatusTagType(status) {
      const typeMap = {
        'SUCCESS': 'success',
        'RUNNING': 'warning',
        'FAILED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getAlertLevelTagType(level) {
      const typeMap = {
        'ERROR': 'danger',
        'WARNING': 'warning',
        'INFO': 'info'
      }
      return typeMap[level] || 'info'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      // 确保转换为数字类型
      const numAmount = typeof amount === 'string' ? parseFloat(amount) : Number(amount)
      if (isNaN(numAmount)) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(numAmount)
    },
    formatDateTime(timestamp) {
      if (!timestamp) return '-'
      // 如果是数字类型的时间戳
      if (typeof timestamp === 'number') {
        const date = new Date(timestamp)
        // 检查是否是有效日期
        if (isNaN(date.getTime())) return '-'
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      }
      // 如果是字符串类型，直接返回
      if (typeof timestamp === 'string') {
        return timestamp
      }
      return '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.concentration-execution-monitor {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
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
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .monitor-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.running-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.success-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.failed-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
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
  }

  .chart-card {
    margin-bottom: 20px;
    
    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        margin: 0;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
      }
    }
    
    .chart-container {
      height: 300px;
      width: 100%;
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .progress-text {
    font-size: 12px;
    color: #606266;
    margin-top: 4px;
  }

  .amount-text, .completed-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .task-detail {
    .detail-descriptions {
      margin-bottom: 20px;
    }
    
    .execution-logs, .performance-metrics {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
      }
    }
    
    .performance-metrics {
      .metric-item {
        text-align: center;
        padding: 16px;
        background: #f8f9fa;
        border-radius: 4px;
        
        .metric-label {
          font-size: 12px;
          color: #909399;
          margin-bottom: 8px;
        }
        
        .metric-value {
          font-size: 18px;
          font-weight: 600;
          color: #303133;
        }
      }
    }
  }
}
</style>
