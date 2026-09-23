<template>
  <div class="system-monitor">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>系统监控</h2>
      <p>实时监控预算系统的运行状态、性能指标、资源使用情况和系统健康度</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="success" icon="el-icon-video-play" :disabled="!autoRefresh" @click="toggleAutoRefresh">
              {{ autoRefresh ? '停止自动刷新' : '开启自动刷新' }}
            </el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExport">导出报告</el-button>
            <el-button type="info" icon="el-icon-setting" @click="handleSettings">监控设置</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-warning" @click="handleAlerts">告警管理</el-button>
            <el-button icon="el-icon-data-analysis" @click="handleAnalysis">性能分析</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 系统状态概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card health-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ systemStats.healthScore }}%</div>
            <div class="stat-label">系统健康度</div>
            <div class="stat-description">综合健康评分</div>
            <div class="stat-trend">
              <i :class="getHealthIcon(systemStats.healthScore)"></i>
              <span>{{ getHealthText(systemStats.healthScore) }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-odometer"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card cpu-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ systemStats.cpuUsage }}%</div>
            <div class="stat-label">CPU使用率</div>
            <div class="stat-description">当前CPU占用</div>
            <div class="stat-trend">
              <i class="el-icon-cpu"></i>
              <span>{{ getCpuStatus(systemStats.cpuUsage) }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-cpu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card memory-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ systemStats.memoryUsage }}%</div>
            <div class="stat-label">内存使用率</div>
            <div class="stat-description">当前内存占用</div>
            <div class="stat-trend">
              <i class="el-icon-coin"></i>
              <span>{{ getMemoryStatus(systemStats.memoryUsage) }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-coin"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card disk-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ systemStats.diskUsage }}%</div>
            <div class="stat-label">磁盘使用率</div>
            <div class="stat-description">当前磁盘占用</div>
            <div class="stat-trend">
              <i class="el-icon-files"></i>
              <span>{{ getDiskStatus(systemStats.diskUsage) }}</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-files"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时监控图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>CPU & 内存使用率</span>
            <div class="header-tools">
              <el-button icon="el-icon-refresh" size="mini" @click="refreshPerformanceChart">刷新</el-button>
            </div>
          </div>
          <div id="performanceChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>网络流量</span>
            <div class="header-tools">
              <el-button icon="el-icon-refresh" size="mini" @click="refreshNetworkChart">刷新</el-button>
            </div>
          </div>
          <div id="networkChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 服务状态监控 -->
    <el-card class="service-status-card" shadow="never">
      <div slot="header" class="card-header">
        <span>服务状态</span>
        <div class="header-tools">
          <el-button icon="el-icon-refresh" size="mini" @click="getServiceStatus">刷新</el-button>
        </div>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="service in serviceStatus" :key="service.id">
          <el-card class="service-item" shadow="hover" :class="getServiceStatusClass(service.status)">
            <div class="service-icon">
              <i :class="service.icon"></i>
            </div>
            <div class="service-info">
              <div class="service-name">{{ service.serviceName }}</div>
              <div class="service-description">{{ service.description }}</div>
              <div class="service-stats">
                <span class="uptime">运行时间: {{ service.uptime }}</span>
                <span class="response-time">响应时间: {{ service.responseTime }}ms</span>
              </div>
            </div>
            <div class="service-status">
              <el-tag :type="getServiceStatusColor(service.status)" size="mini">
                {{ getServiceStatusText(service.status) }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据库监控 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="database-card" shadow="never">
          <div slot="header" class="card-header">
            <span>数据库监控</span>
            <div class="header-tools">
              <el-button icon="el-icon-refresh" size="mini" @click="getDatabaseStatus">刷新</el-button>
            </div>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="连接数">{{ databaseStatus.connections }}</el-descriptions-item>
            <el-descriptions-item label="活跃连接">{{ databaseStatus.activeConnections }}</el-descriptions-item>
            <el-descriptions-item label="查询/秒">{{ databaseStatus.queriesPerSecond }}</el-descriptions-item>
            <el-descriptions-item label="慢查询">{{ databaseStatus.slowQueries }}</el-descriptions-item>
            <el-descriptions-item label="缓存命中率">{{ databaseStatus.cacheHitRate }}%</el-descriptions-item>
            <el-descriptions-item label="锁等待">{{ databaseStatus.lockWaits }}</el-descriptions-item>
            <el-descriptions-item label="数据库大小">{{ databaseStatus.databaseSize }}</el-descriptions-item>
            <el-descriptions-item label="表空间使用">{{ databaseStatus.tablespaceUsage }}%</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="jvm-card" shadow="never">
          <div slot="header" class="card-header">
            <span>JVM监控</span>
            <div class="header-tools">
              <el-button icon="el-icon-refresh" size="mini" @click="getJvmStatus">刷新</el-button>
            </div>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="堆内存使用">{{ jvmStatus.heapMemoryUsed }}</el-descriptions-item>
            <el-descriptions-item label="堆内存总量">{{ jvmStatus.heapMemoryTotal }}</el-descriptions-item>
            <el-descriptions-item label="非堆内存">{{ jvmStatus.nonHeapMemoryUsed }}</el-descriptions-item>
            <el-descriptions-item label="GC次数">{{ jvmStatus.gcCount }}</el-descriptions-item>
            <el-descriptions-item label="GC时间">{{ jvmStatus.gcTime }}ms</el-descriptions-item>
            <el-descriptions-item label="线程数">{{ jvmStatus.threadCount }}</el-descriptions-item>
            <el-descriptions-item label="类加载数">{{ jvmStatus.classLoadedCount }}</el-descriptions-item>
            <el-descriptions-item label="运行时间">{{ jvmStatus.uptime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 告警信息 -->
    <el-card class="alert-card" shadow="never">
      <div slot="header" class="card-header">
        <span>告警信息</span>
        <div class="header-tools">
          <el-button icon="el-icon-refresh" size="mini" @click="getAlertList">刷新</el-button>
        </div>
      </div>
      <el-table :data="alertList" border size="mini">
        <el-table-column prop="monitorTime" label="告警时间" width="170" align="center">
          <template slot-scope="scope">
            {{ formatTime(scope.row.monitorTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="alertLevel" label="告警级别" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAlertLevelColor(scope.row.alertLevel)" size="mini">
              {{ getAlertLevelText(scope.row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertType" label="告警类型" width="120" align="center" />
        <el-table-column prop="alertMessage" label="告警信息" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'RESOLVED' ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === 'RESOLVED' ? '已解决' : '未解决' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              @click="handleResolveAlert(scope.row)"
              v-if="scope.row.status !== 'RESOLVED'"
            >标记解决</el-button>
            <el-button
              type="text"
              size="mini"
              @click="handleViewAlert(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 监控设置弹窗 -->
    <el-dialog title="监控设置" :visible.sync="settingsDialogVisible" width="500px" append-to-body>
      <el-form :model="settingsForm" label-width="120px">
        <el-form-item label="刷新间隔(秒)">
          <el-input-number v-model="settingsForm.refreshInterval" :min="5" :max="300" :step="5" />
        </el-form-item>
        <el-form-item label="CPU告警阈值">
          <el-slider v-model="settingsForm.cpuThreshold" :max="100" show-input />
        </el-form-item>
        <el-form-item label="内存告警阈值">
          <el-slider v-model="settingsForm.memoryThreshold" :max="100" show-input />
        </el-form-item>
        <el-form-item label="磁盘告警阈值">
          <el-slider v-model="settingsForm.diskThreshold" :max="100" show-input />
        </el-form-item>
        <el-form-item label="启用告警">
          <el-switch v-model="settingsForm.alertEnabled" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="settingsForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="settingsSaving" @click="saveSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 告警管理弹窗 -->
    <el-dialog title="告警管理" :visible.sync="alertManageDialogVisible" width="800px" append-to-body>
      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="8">
          <el-select v-model="alertFilter.alertLevel" placeholder="告警级别" clearable @change="loadAlertManageList">
            <el-option label="信息" value="INFO" />
            <el-option label="警告" value="WARNING" />
            <el-option label="错误" value="ERROR" />
            <el-option label="严重" value="CRITICAL" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-select v-model="alertFilter.status" placeholder="状态" clearable @change="loadAlertManageList">
            <el-option label="未解决" value="ACTIVE" />
            <el-option label="已解决" value="RESOLVED" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" icon="el-icon-refresh" @click="loadAlertManageList">刷新</el-button>
        </el-col>
      </el-row>
      <el-table :data="alertManageList" border size="mini" max-height="400">
        <el-table-column prop="monitorTime" label="告警时间" width="170" align="center">
          <template slot-scope="scope">{{ formatTime(scope.row.monitorTime) }}</template>
        </el-table-column>
        <el-table-column prop="alertLevel" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAlertLevelColor(scope.row.alertLevel)" size="mini">{{ getAlertLevelText(scope.row.alertLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertType" label="类型" width="100" align="center" />
        <el-table-column prop="alertMessage" label="信息" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'RESOLVED' ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === 'RESOLVED' ? '已解决' : '未解决' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleResolveAlert(scope.row)" v-if="scope.row.status !== 'RESOLVED'">解决</el-button>
            <el-button type="text" size="mini" @click="handleViewAlert(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 性能分析弹窗 -->
    <el-dialog title="性能分析" :visible.sync="analysisDialogVisible" width="900px" append-to-body>
      <el-row :gutter="16" style="margin-bottom: 16px;">
        <el-col :span="8">
          <el-select v-model="analysisHours" placeholder="时间范围" @change="loadAnalysisData">
            <el-option label="最近6小时" :value="6" />
            <el-option label="最近12小时" :value="12" />
            <el-option label="最近24小时" :value="24" />
            <el-option label="最近48小时" :value="48" />
          </el-select>
        </el-col>
      </el-row>
      <div id="analysisChart" style="height: 400px;"></div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="系统监控帮助" :visible.sync="helpDialogVisible" width="600px" append-to-body>
      <div class="help-content">
        <h4>功能说明</h4>
        <p>系统监控模块提供对预算系统运行状态的实时监控，包括以下功能：</p>
        <ul>
          <li><b>系统健康度</b>：综合CPU、内存、磁盘和错误数计算的健康评分</li>
          <li><b>CPU/内存/磁盘使用率</b>：实时采集服务器资源使用情况</li>
          <li><b>性能图表</b>：展示CPU和内存使用率的历史趋势</li>
          <li><b>网络流量</b>：展示入站和出站网络流量数据</li>
          <li><b>服务状态</b>：监控各微服务的运行状态和响应时间</li>
          <li><b>数据库监控</b>：展示数据库连接数、查询性能等指标</li>
          <li><b>JVM监控</b>：展示Java虚拟机的内存、GC、线程等信息</li>
          <li><b>告警管理</b>：查看和处理系统告警信息</li>
        </ul>
        <h4>操作说明</h4>
        <ul>
          <li><b>刷新</b>：手动刷新所有监控数据</li>
          <li><b>自动刷新</b>：开启/关闭定时自动刷新（默认30秒）</li>
          <li><b>导出报告</b>：将监控数据导出为CSV文件</li>
          <li><b>监控设置</b>：配置刷新间隔和告警阈值</li>
        </ul>
      </div>
    </el-dialog>

    <!-- 告警详情弹窗 -->
    <el-dialog title="告警详情" :visible.sync="alertDetailDialogVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="alertDetailData">
        <el-descriptions-item label="告警ID">{{ alertDetailData.monitorId }}</el-descriptions-item>
        <el-descriptions-item label="监控名称">{{ alertDetailData.monitorName }}</el-descriptions-item>
        <el-descriptions-item label="告警级别">
          <el-tag :type="getAlertLevelColor(alertDetailData.alertLevel)" size="mini">{{ getAlertLevelText(alertDetailData.alertLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="告警类型">{{ alertDetailData.alertType }}</el-descriptions-item>
        <el-descriptions-item label="告警时间">{{ formatTime(alertDetailData.monitorTime) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="alertDetailData.status === 'RESOLVED' ? 'success' : 'danger'" size="mini">
            {{ alertDetailData.status === 'RESOLVED' ? '已解决' : '未解决' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="告警信息" :span="2">{{ alertDetailData.alertMessage }}</el-descriptions-item>
        <el-descriptions-item label="服务名称">{{ alertDetailData.serviceName }}</el-descriptions-item>
        <el-descriptions-item label="监控类型">{{ alertDetailData.monitorType }}</el-descriptions-item>
        <el-descriptions-item label="CPU使用率">{{ alertDetailData.cpuUsage }}%</el-descriptions-item>
        <el-descriptions-item label="内存使用率">{{ alertDetailData.memoryUsage }}%</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ alertDetailData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" v-if="alertDetailData && alertDetailData.status !== 'RESOLVED'">
        <el-button type="primary" @click="handleResolveAlert(alertDetailData)">标记为已解决</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'
import * as echarts from 'echarts'

export default {
  name: 'SystemMonitor',
  data() {
    return {
      // 自动刷新
      autoRefresh: true,
      refreshTimer: null,
      
      // 系统统计
      systemStats: {
        healthScore: 0,
        cpuUsage: 0,
        memoryUsage: 0,
        diskUsage: 0
      },

      // 服务状态
      serviceStatus: [],

      // 数据库状态
      databaseStatus: {
        connections: 0,
        activeConnections: 0,
        queriesPerSecond: 0,
        slowQueries: 0,
        cacheHitRate: 0,
        lockWaits: 0,
        databaseSize: '0',
        tablespaceUsage: 0
      },

      // JVM状态
      jvmStatus: {
        heapMemoryUsed: '0',
        heapMemoryTotal: '0',
        nonHeapMemoryUsed: '0',
        gcCount: 0,
        gcTime: 0,
        threadCount: 0,
        classLoadedCount: 0,
        uptime: '0'
      },

      // 告警列表
      alertList: [],

      // 图表实例
      performanceChart: null,
      networkChart: null,

      // 弹窗控制
      settingsDialogVisible: false,
      settingsSaving: false,
      settingsForm: {
        refreshInterval: 30,
        cpuThreshold: 80,
        memoryThreshold: 80,
        diskThreshold: 90,
        alertEnabled: true,
        remark: ''
      },
      alertManageDialogVisible: false,
      alertManageList: [],
      alertFilter: {
        alertLevel: '',
        status: ''
      },
      analysisDialogVisible: false,
      analysisHours: 24,
      analysisChart: null,
      helpDialogVisible: false,
      alertDetailDialogVisible: false,
      alertDetailData: null
    }
  },
  
  mounted() {
    this.initCharts()
    this.loadMonitorData()
    this.startAutoRefresh()
  },
  
  beforeDestroy() {
    this.stopAutoRefresh()
    if (this.performanceChart) {
      this.performanceChart.dispose()
    }
    if (this.networkChart) {
      this.networkChart.dispose()
    }
    if (this.analysisChart) {
      this.analysisChart.dispose()
    }
  },
  
  methods: {
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initPerformanceChart()
        this.initNetworkChart()
      })
    },
    
    // 初始化性能图表
    initPerformanceChart() {
      const chartDom = document.getElementById('performanceChart')
      if (chartDom) {
        this.performanceChart = echarts.init(chartDom)
        this.updatePerformanceChart()
      }
    },
    
    // 初始化网络图表
    initNetworkChart() {
      const chartDom = document.getElementById('networkChart')
      if (chartDom) {
        this.networkChart = echarts.init(chartDom)
        this.updateNetworkChart()
      }
    },
    
    // 更新性能图表
    async updatePerformanceChart() {
      if (!this.performanceChart) return
      try {
        const response = await budgetSystemApi.getPerformanceData(24)
        let timeLabels = []
        let cpuData = []
        let memoryData = []
        if (response.code === 1 && response.data) {
          timeLabels = response.data.timeLabels || []
          cpuData = response.data.cpuData || []
          memoryData = response.data.memoryData || []
        }
        const option = {
          title: { text: 'CPU & 内存使用率', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'axis' },
          legend: { data: ['CPU使用率', '内存使用率'], bottom: 10 },
          xAxis: { type: 'category', data: timeLabels },
          yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
          series: [
            { name: 'CPU使用率', type: 'line', data: cpuData, smooth: true, itemStyle: { color: '#409EFF' } },
            { name: '内存使用率', type: 'line', data: memoryData, smooth: true, itemStyle: { color: '#67C23A' } }
          ]
        }
        this.performanceChart.setOption(option)
      } catch (error) {
        console.error('获取性能数据失败', error)
      }
    },

    // 更新网络图表
    async updateNetworkChart() {
      if (!this.networkChart) return
      try {
        const response = await budgetSystemApi.getNetworkTraffic(24)
        let timeLabels = []
        let inboundData = []
        let outboundData = []
        if (response.code === 1 && response.data) {
          timeLabels = response.data.timeLabels || []
          inboundData = response.data.inboundData || []
          outboundData = response.data.outboundData || []
        }
        const option = {
          title: { text: '网络流量', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'axis' },
          legend: { data: ['入站流量', '出站流量'], bottom: 10 },
          xAxis: { type: 'category', data: timeLabels },
          yAxis: { type: 'value', axisLabel: { formatter: '{value} KB/s' } },
          series: [
            { name: '入站流量', type: 'bar', data: inboundData, itemStyle: { color: '#E6A23C' } },
            { name: '出站流量', type: 'bar', data: outboundData, itemStyle: { color: '#F56C6C' } }
          ]
        }
        this.networkChart.setOption(option)
      } catch (error) {
        console.error('获取网络流量数据失败', error)
      }
    },
    
    // 加载监控数据
    async loadMonitorData() {
      await Promise.all([
        this.getSystemStats(),
        this.getServiceStatus(),
        this.getDatabaseStatus(),
        this.getJvmStatus(),
        this.getAlertList()
      ])
    },
    
    // 获取系统统计
    async getSystemStats() {
      try {
        const response = await budgetSystemApi.getSystemStats()
        if (response.code === 1 && response.data) {
          this.systemStats = response.data
        }
      } catch (error) {
        this.$message.error('获取系统统计失败')
      }
    },

    // 获取服务状态
    async getServiceStatus() {
      try {
        const response = await budgetSystemApi.getServiceStatus()
        if (response.code === 1 && response.data) {
          const data = response.data
          this.serviceStatus = data.tlist || data.list || (Array.isArray(data) ? data : [])
        }
      } catch (error) {
        this.$message.error('获取服务状态失败')
      }
    },

    // 获取数据库状态
    async getDatabaseStatus() {
      try {
        const response = await budgetSystemApi.getDatabaseStatus()
        if (response.code === 1 && response.data) {
          this.databaseStatus = response.data
        }
      } catch (error) {
        this.$message.error('获取数据库状态失败')
      }
    },

    // 获取JVM状态
    async getJvmStatus() {
      try {
        const response = await budgetSystemApi.getJvmStatus()
        if (response.code === 1 && response.data) {
          this.jvmStatus = response.data
        }
      } catch (error) {
        this.$message.error('获取JVM状态失败')
      }
    },

    // 获取告警列表
    async getAlertList() {
      try {
        const response = await budgetSystemApi.getAlertList()
        if (response.code === 1 && response.data) {
          const data = response.data
          this.alertList = data.tlist || data.list || (Array.isArray(data) ? data : [])
        }
      } catch (error) {
        this.$message.error('获取告警列表失败')
      }
    },
    
    // 开启自动刷新
    startAutoRefresh() {
      if (this.autoRefresh) {
        this.refreshTimer = setInterval(() => {
          this.loadMonitorData()
          this.updatePerformanceChart()
          this.updateNetworkChart()
        }, 30000) // 30秒刷新一次
      }
    },
    
    // 停止自动刷新
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },
    
    // 切换自动刷新
    toggleAutoRefresh() {
      this.autoRefresh = !this.autoRefresh
      if (this.autoRefresh) {
        this.startAutoRefresh()
      } else {
        this.stopAutoRefresh()
      }
    },
    
    // 刷新
    handleRefresh() {
      this.loadMonitorData()
      this.updatePerformanceChart()
      this.updateNetworkChart()
    },
    
    // 刷新性能图表
    refreshPerformanceChart() {
      this.updatePerformanceChart()
    },
    
    // 刷新网络图表
    refreshNetworkChart() {
      this.updateNetworkChart()
    },
    
    // 解决告警
    handleResolveAlert(alert) {
      this.$confirm('确认将此告警标记为已解决？', '确认操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetSystemApi.resolveAlert(alert.monitorId)
          if (response.code === 1) {
            this.$message.success('告警已标记为已解决')
            this.getAlertList()
            if (this.alertManageDialogVisible) {
              this.loadAlertManageList()
            }
            if (this.alertDetailDialogVisible) {
              this.alertDetailData.status = 'RESOLVED'
            }
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 查看告警详情
    async handleViewAlert(alert) {
      try {
        const response = await budgetSystemApi.getAlertDetail(alert.monitorId)
        if (response.code === 1 && response.data) {
          this.alertDetailData = response.data
          this.alertDetailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取告警详情失败')
        }
      } catch (error) {
        this.$message.error('获取告警详情失败')
      }
    },

    // 导出报告
    async handleExport() {
      try {
        const response = await budgetSystemApi.exportMonitorReport()
        if (response) {
          const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '系统监控报告.csv'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 监控设置
    async handleSettings() {
      try {
        const response = await budgetSystemApi.getMonitorSettings()
        if (response.code === 1 && response.data) {
          this.settingsForm = {
            refreshInterval: response.data.refreshInterval || 30,
            cpuThreshold: Number(response.data.cpuThreshold) || 80,
            memoryThreshold: Number(response.data.memoryThreshold) || 80,
            diskThreshold: Number(response.data.diskThreshold) || 90,
            alertEnabled: response.data.alertEnabled !== false,
            remark: response.data.remark || ''
          }
        }
      } catch (error) {
        console.error('获取监控设置失败', error)
      }
      this.settingsDialogVisible = true
    },

    // 保存监控设置
    async saveSettings() {
      this.settingsSaving = true
      try {
        const response = await budgetSystemApi.saveMonitorSettings(this.settingsForm)
        if (response.code === 1) {
          this.$message.success('设置保存成功')
          this.settingsDialogVisible = false
          // 更新自动刷新间隔
          this.stopAutoRefresh()
          if (this.autoRefresh) {
            this.refreshTimer = setInterval(() => {
              this.loadMonitorData()
              this.updatePerformanceChart()
              this.updateNetworkChart()
            }, this.settingsForm.refreshInterval * 1000)
          }
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存设置失败')
      } finally {
        this.settingsSaving = false
      }
    },

    // 告警管理
    handleAlerts() {
      this.alertManageDialogVisible = true
      this.loadAlertManageList()
    },

    // 加载告警管理列表
    async loadAlertManageList() {
      try {
        const params = { pageNum: 1, pageSize: 50 }
        if (this.alertFilter.alertLevel) {
          params.alertLevel = this.alertFilter.alertLevel
        }
        if (this.alertFilter.status) {
          params.status = this.alertFilter.status
        }
        const response = await budgetSystemApi.getAlertList(params)
        if (response.code === 1 && response.data) {
          const data = response.data
          this.alertManageList = data.tlist || data.list || (Array.isArray(data) ? data : [])
        }
      } catch (error) {
        this.$message.error('获取告警列表失败')
      }
    },

    // 性能分析
    handleAnalysis() {
      this.analysisDialogVisible = true
      this.$nextTick(() => {
        this.loadAnalysisData()
      })
    },

    // 加载性能分析数据
    async loadAnalysisData() {
      try {
        const response = await budgetSystemApi.getPerformanceData(this.analysisHours)
        if (response.code === 1 && response.data) {
          const chartDom = document.getElementById('analysisChart')
          if (chartDom) {
            if (this.analysisChart) {
              this.analysisChart.dispose()
            }
            this.analysisChart = echarts.init(chartDom)
            const option = {
              title: { text: 'CPU & 内存使用率趋势分析', left: 'center', textStyle: { fontSize: 14 } },
              tooltip: { trigger: 'axis' },
              legend: { data: ['CPU使用率', '内存使用率'], bottom: 10 },
              grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
              xAxis: { type: 'category', data: response.data.timeLabels || [] },
              yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
              dataZoom: [{ type: 'inside' }, { type: 'slider' }],
              series: [
                { name: 'CPU使用率', type: 'line', data: response.data.cpuData || [], smooth: true, areaStyle: { opacity: 0.3 }, itemStyle: { color: '#409EFF' } },
                { name: '内存使用率', type: 'line', data: response.data.memoryData || [], smooth: true, areaStyle: { opacity: 0.3 }, itemStyle: { color: '#67C23A' } }
              ]
            }
            this.analysisChart.setOption(option)
          }
        }
      } catch (error) {
        this.$message.error('获取性能分析数据失败')
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      if (isNaN(date.getTime())) return time
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      const h = String(date.getHours()).padStart(2, '0')
      const min = String(date.getMinutes()).padStart(2, '0')
      const s = String(date.getSeconds()).padStart(2, '0')
      return y + '-' + m + '-' + d + ' ' + h + ':' + min + ':' + s
    },
    
    // 获取健康度图标
    getHealthIcon(score) {
      if (score >= 90) return 'el-icon-success'
      if (score >= 70) return 'el-icon-warning'
      return 'el-icon-error'
    },
    
    // 获取健康度文本
    getHealthText(score) {
      if (score >= 90) return '优秀'
      if (score >= 70) return '良好'
      return '需要关注'
    },
    
    // 获取CPU状态
    getCpuStatus(usage) {
      if (usage < 50) return '正常'
      if (usage < 80) return '较高'
      return '过高'
    },
    
    // 获取内存状态
    getMemoryStatus(usage) {
      if (usage < 60) return '正常'
      if (usage < 80) return '较高'
      return '过高'
    },
    
    // 获取磁盘状态
    getDiskStatus(usage) {
      if (usage < 70) return '正常'
      if (usage < 90) return '较高'
      return '过高'
    },
    
    // 获取服务状态样式类
    getServiceStatusClass(status) {
      return {
        'service-running': status === 'RUNNING',
        'service-warning': status === 'WARNING',
        'service-error': status === 'ERROR',
        'service-stopped': status === 'STOPPED'
      }
    },
    
    // 获取服务状态颜色
    getServiceStatusColor(status) {
      const colorMap = {
        'RUNNING': 'success',
        'WARNING': 'warning',
        'ERROR': 'danger',
        'STOPPED': 'info'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取服务状态文本
    getServiceStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'WARNING': '警告',
        'ERROR': '错误',
        'STOPPED': '已停止'
      }
      return textMap[status] || status
    },
    
    // 获取告警级别颜色
    getAlertLevelColor(level) {
      const colorMap = {
        'INFO': 'info',
        'WARNING': 'warning',
        'ERROR': 'danger',
        'CRITICAL': 'danger'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取告警级别文本
    getAlertLevelText(level) {
      const textMap = {
        'INFO': '信息',
        'WARNING': '警告',
        'ERROR': '错误',
        'CRITICAL': '严重'
      }
      return textMap[level] || level
    }
  }
}
</script>
