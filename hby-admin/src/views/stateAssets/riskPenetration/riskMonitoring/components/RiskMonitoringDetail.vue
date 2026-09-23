<template>
  <el-dialog
    title="风险监控详情"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    top="5vh"
  >
    <div v-loading="loading" class="monitoring-detail">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="detail-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>企业名称：</label>
                  <span>{{ monitoringData.enterpriseName || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>监控名称：</label>
                  <span>{{ monitoringData.monitoringName || '-' }}</span>
                </div>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>监控类型：</label>
                  <el-tag :type="getMonitoringTypeTagType(monitoringData.monitoringType)">
                    {{ getMonitoringTypeLabel(monitoringData.monitoringType) }}
                  </el-tag>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>监控指标：</label>
                  <span>{{ monitoringData.monitoringIndicator || '-' }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>数据源类型：</label>
                  <span>{{ getDataSourceTypeLabel(monitoringData.dataSourceType) }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>监控频率：</label>
                  <span>{{ getMonitoringFrequencyLabel(monitoringData.monitoringFrequency) }}</span>
                </div>
              </el-col>
            </el-row>

            <div class="detail-item">
              <label>数据源配置：</label>
              <pre class="config-content">{{ monitoringData.dataSourceConfig || '-' }}</pre>
            </div>

            <div class="detail-item">
              <label>监控描述：</label>
              <p>{{ monitoringData.monitoringDescription || '-' }}</p>
            </div>
          </div>
        </el-tab-pane>

        <!-- 监控状态 -->
        <el-tab-pane label="监控状态" name="status">
          <div class="detail-section">
            <!-- 状态概览卡片 -->
            <div class="status-overview">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-video-play"></i>
                      <span>监控状态</span>
                    </div>
                    <div class="status-content">
                      <el-tag :type="getMonitoringStatusTagType(monitoringData.monitoringStatus)" size="large">
                        {{ getMonitoringStatusLabel(monitoringData.monitoringStatus) }}
                      </el-tag>
                    </div>
                  </div>
                </el-col>
                
                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-warning"></i>
                      <span>预警状态</span>
                    </div>
                    <div class="status-content">
                      <el-tag :type="getAlertStatusTagType(monitoringData.alertStatus)" size="large">
                        {{ getAlertStatusLabel(monitoringData.alertStatus) }}
                      </el-tag>
                    </div>
                  </div>
                </el-col>

                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-time"></i>
                      <span>最后更新</span>
                    </div>
                    <div class="status-content">
                      <span class="update-time">{{ monitoringData.lastUpdateTime || '-' }}</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 运行信息 -->
            <div class="running-info">
              <h4>运行信息</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>启动时间：</label>
                    <span>{{ monitoringData.startTime || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>运行时长：</label>
                    <span>{{ calculateRunningDuration(monitoringData.startTime) }}</span>
                  </div>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>执行次数：</label>
                    <span>{{ monitoringData.executionCount || 0 }} 次</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>成功率：</label>
                    <span class="success-rate">{{ calculateSuccessRate(monitoringData) }}%</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>

        <!-- 当前数值 -->
        <el-tab-pane label="当前数值" name="current">
          <div class="detail-section">
            <!-- 数值展示卡片 -->
            <div class="value-display">
              <div class="current-value-card">
                <div class="value-header">
                  <h3>{{ monitoringData.monitoringIndicator || '监控指标' }}</h3>
                  <div class="value-unit">{{ monitoringData.unit || '' }}</div>
                </div>
                <div class="value-content">
                  <div class="current-value" :class="getValueClass(monitoringData)">
                    {{ monitoringData.currentValue || '-' }}
                  </div>
                  <div class="value-status">
                    <el-tag :type="getValueStatusTagType(monitoringData)" size="large">
                      {{ getValueStatusLabel(monitoringData) }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>

            <!-- 阈值对比 -->
            <div class="threshold-comparison">
              <h4>阈值对比</h4>
              <div class="threshold-chart">
                <div class="threshold-bar">
                  <div class="bar-section normal">
                    <span class="section-label">正常区间</span>
                    <span class="section-range">0 - {{ monitoringData.warningThreshold || 0 }}</span>
                  </div>
                  <div class="bar-section warning">
                    <span class="section-label">预警区间</span>
                    <span class="section-range">{{ monitoringData.warningThreshold || 0 }} - {{ monitoringData.dangerThreshold || 0 }}</span>
                  </div>
                  <div class="bar-section danger">
                    <span class="section-label">危险区间</span>
                    <span class="section-range">> {{ monitoringData.dangerThreshold || 0 }}</span>
                  </div>
                </div>
                <div class="current-position" :style="getCurrentPositionStyle(monitoringData)">
                  <div class="position-marker">
                    <span>当前值</span>
                    <div class="marker-value">{{ monitoringData.currentValue || 0 }}</div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 数值历史 -->
            <div class="value-history">
              <h4>最近数值变化</h4>
              <el-table :data="valueHistory" border stripe>
                <el-table-column prop="recordTime" label="记录时间" width="180" />
                <el-table-column prop="value" label="数值" width="120">
                  <template slot-scope="scope">
                    <span :class="getHistoryValueClass(scope.row.value)">
                      {{ scope.row.value }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getHistoryStatusTagType(scope.row.status)" size="small">
                      {{ getHistoryStatusLabel(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="changeRate" label="变化率" width="120">
                  <template slot-scope="scope">
                    <span :class="getChangeRateClass(scope.row.changeRate)">
                      {{ scope.row.changeRate }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 预警配置 -->
        <el-tab-pane label="预警配置" name="alert">
          <div class="detail-section">
            <div class="alert-config">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="config-item">
                    <label>预警启用状态：</label>
                    <el-tag :type="monitoringData.alertEnabled ? 'success' : 'info'">
                      {{ monitoringData.alertEnabled ? '已启用' : '已禁用' }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="config-item">
                    <label>阈值比较方式：</label>
                    <span>{{ getThresholdComparisonLabel(monitoringData.thresholdComparison) }}</span>
                  </div>
                </el-col>
              </el-row>

              <div class="config-item">
                <label>预警方式：</label>
                <div class="alert-methods">
                  <el-tag
                    v-for="method in getAlertMethods(monitoringData.alertMethod)"
                    :key="method"
                    type="primary"
                    class="method-tag"
                  >
                    {{ getAlertMethodLabel(method) }}
                  </el-tag>
                </div>
              </div>

              <div class="config-item">
                <label>预警接收人：</label>
                <p>{{ monitoringData.alertRecipients || '-' }}</p>
              </div>

              <div class="config-item">
                <label>预警消息模板：</label>
                <div class="message-template">
                  {{ monitoringData.alertMessageTemplate || '-' }}
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 异常检测 -->
        <el-tab-pane label="异常检测" name="anomaly">
          <div class="detail-section">
            <div class="anomaly-config">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="config-item">
                    <label>异常检测状态：</label>
                    <el-tag :type="monitoringData.anomalyDetectionEnabled ? 'success' : 'info'">
                      {{ monitoringData.anomalyDetectionEnabled ? '已启用' : '已禁用' }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="config-item">
                    <label>检测算法：</label>
                    <span>{{ getAnomalyAlgorithmLabel(monitoringData.anomalyDetectionAlgorithm) }}</span>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="config-item">
                    <label>历史数据窗口：</label>
                    <span>{{ monitoringData.historicalDataWindow || 0 }} 天</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="config-item">
                    <label>异常敏感度：</label>
                    <span>{{ getAnomalySensitivityLabel(monitoringData.anomalySensitivity) }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 异常检测结果 -->
            <div v-if="monitoringData.anomalyDetectionEnabled" class="anomaly-results">
              <h4>最近异常检测结果</h4>
              <el-table :data="anomalyResults" border stripe>
                <el-table-column prop="detectionTime" label="检测时间" width="180" />
                <el-table-column prop="anomalyType" label="异常类型" width="120">
                  <template slot-scope="scope">
                    <el-tag :type="getAnomalyTypeTagType(scope.row.anomalyType)" size="small">
                      {{ getAnomalyTypeLabel(scope.row.anomalyType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="anomalyScore" label="异常评分" width="120">
                  <template slot-scope="scope">
                    <span :class="getAnomalyScoreClass(scope.row.anomalyScore)">
                      {{ scope.row.anomalyScore }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="异常描述" show-overflow-tooltip />
                <el-table-column prop="status" label="处理状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getProcessStatusTagType(scope.row.status)" size="small">
                      {{ getProcessStatusLabel(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportReport">导出报告</el-button>
      <el-button v-if="monitoringData.monitoringStatus === 'STOPPED'" type="success" @click="handleStartMonitoring">
        启动监控
      </el-button>
      <el-button v-if="monitoringData.monitoringStatus === 'RUNNING'" type="warning" @click="handleStopMonitoring">
        停止监控
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { 
  getRiskMonitoringById, 
  startRealTimeMonitoring, 
  stopRealTimeMonitoring,
  exportMonitoringReport 
} from '@/api/stateAssets/riskMonitoring'

export default {
  name: 'RiskMonitoringDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    monitoringId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      monitoringData: {},
      valueHistory: [],
      anomalyResults: []
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.monitoringId) {
        this.loadMonitoringDetail()
      }
    }
  },
  methods: {
    // 加载监控详情
    async loadMonitoringDetail() {
      this.loading = true
      try {
        const response = await getRiskMonitoringById(this.monitoringId)
        if (response.code === 200) {
          this.monitoringData = response.data || {}
          // 模拟历史数据
          this.generateMockData()
        }
      } catch (error) {
        this.$message.error('加载详情失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 生成模拟数据
    generateMockData() {
      // 模拟数值历史
      this.valueHistory = [
        { recordTime: '2024-01-15 14:30:00', value: 85.2, status: 'DANGER', changeRate: '+5.2', remarks: '超过危险阈值' },
        { recordTime: '2024-01-15 14:25:00', value: 78.5, status: 'WARNING', changeRate: '+2.1', remarks: '接近危险阈值' },
        { recordTime: '2024-01-15 14:20:00', value: 72.3, status: 'WARNING', changeRate: '-1.5', remarks: '预警状态' },
        { recordTime: '2024-01-15 14:15:00', value: 65.8, status: 'NORMAL', changeRate: '+0.8', remarks: '正常范围' },
        { recordTime: '2024-01-15 14:10:00', value: 63.2, status: 'NORMAL', changeRate: '-2.3', remarks: '正常范围' }
      ]

      // 模拟异常检测结果
      if (this.monitoringData.anomalyDetectionEnabled) {
        this.anomalyResults = [
          { detectionTime: '2024-01-15 14:30:00', anomalyType: 'SPIKE', anomalyScore: 0.95, description: '数值突然上升', status: 'PENDING' },
          { detectionTime: '2024-01-15 13:45:00', anomalyType: 'TREND', anomalyScore: 0.78, description: '持续上升趋势', status: 'PROCESSED' },
          { detectionTime: '2024-01-15 12:20:00', anomalyType: 'OUTLIER', anomalyScore: 0.82, description: '异常值检测', status: 'PROCESSED' }
        ]
      }
    },

    // 启动监控
    async handleStartMonitoring() {
      try {
        const response = await startRealTimeMonitoring({
          riskMonitoringId: this.monitoringId,
          startBy: this.$store.getters.userInfo.userName
        })
        
        if (response.code === 200) {
          this.$message.success('监控启动成功')
          this.loadMonitoringDetail()
          this.$emit('success')
        }
      } catch (error) {
        this.$message.error('启动监控失败：' + error.message)
      }
    },

    // 停止监控
    async handleStopMonitoring() {
      try {
        const response = await stopRealTimeMonitoring(this.monitoringId)
        
        if (response.code === 200) {
          this.$message.success('监控已停止')
          this.loadMonitoringDetail()
          this.$emit('success')
        }
      } catch (error) {
        this.$message.error('停止监控失败：' + error.message)
      }
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await exportMonitoringReport({
          riskMonitoringId: this.monitoringId
        })
        
        const blob = new Blob([response], { type: 'application/pdf' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `风险监控报告_${this.monitoringData.monitoringName}_${new Date().getTime()}.pdf`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告导出成功')
      } catch (error) {
        this.$message.error('导出报告失败：' + error.message)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.activeTab = 'basic'
      this.monitoringData = {}
      this.valueHistory = []
      this.anomalyResults = []
    },

    // 计算运行时长
    calculateRunningDuration(startTime) {
      if (!startTime) return '-'
      const start = new Date(startTime)
      const now = new Date()
      const diff = now - start
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
      return `${hours}小时${minutes}分钟`
    },

    // 计算成功率
    calculateSuccessRate(data) {
      if (!data.executionCount || data.executionCount === 0) return 0
      const successCount = data.successCount || data.executionCount - (data.errorCount || 0)
      return Math.round((successCount / data.executionCount) * 100)
    },

    // 获取当前位置样式
    getCurrentPositionStyle(data) {
      if (!data.currentValue || !data.dangerThreshold) return { left: '0%' }
      const maxValue = data.dangerThreshold * 1.2
      const position = Math.min((data.currentValue / maxValue) * 100, 100)
      return { left: `${position}%` }
    },

    // 获取预警方式数组
    getAlertMethods(alertMethod) {
      if (!alertMethod) return []
      return typeof alertMethod === 'string' ? alertMethod.split(',') : alertMethod
    },

    // 工具方法 - 标签类型和文本
    getMonitoringTypeTagType(type) {
      const typeMap = {
        'REAL_TIME': 'primary',
        'PERIODIC': 'success',
        'EVENT_DRIVEN': 'warning',
        'THRESHOLD': 'info',
        'TREND': 'danger'
      }
      return typeMap[type] || ''
    },

    getMonitoringTypeLabel(type) {
      const labelMap = {
        'REAL_TIME': '实时监控',
        'PERIODIC': '定期监控',
        'EVENT_DRIVEN': '事件驱动',
        'THRESHOLD': '阈值监控',
        'TREND': '趋势监控'
      }
      return labelMap[type] || type
    },

    getDataSourceTypeLabel(type) {
      const labelMap = {
        'DATABASE': '数据库',
        'API': 'API接口',
        'FILE': '文件系统',
        'MESSAGE_QUEUE': '消息队列',
        'EXTERNAL_SYSTEM': '外部系统'
      }
      return labelMap[type] || type
    },

    getMonitoringFrequencyLabel(frequency) {
      const labelMap = {
        'REAL_TIME': '实时',
        'EVERY_MINUTE': '每分钟',
        'EVERY_5_MINUTES': '每5分钟',
        'EVERY_15_MINUTES': '每15分钟',
        'HOURLY': '每小时',
        'DAILY': '每日'
      }
      return labelMap[frequency] || frequency
    },

    getMonitoringStatusTagType(status) {
      const statusMap = {
        'RUNNING': 'success',
        'STOPPED': 'info',
        'PAUSED': 'warning',
        'ERROR': 'danger'
      }
      return statusMap[status] || ''
    },

    getMonitoringStatusLabel(status) {
      const labelMap = {
        'RUNNING': '运行中',
        'STOPPED': '已停止',
        'PAUSED': '已暂停',
        'ERROR': '异常'
      }
      return labelMap[status] || status
    },

    getAlertStatusTagType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'ALERT': 'warning',
        'CRITICAL': 'danger'
      }
      return statusMap[status] || ''
    },

    getAlertStatusLabel(status) {
      const labelMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'ALERT': '告警',
        'CRITICAL': '严重'
      }
      return labelMap[status] || status
    },

    getValueClass(data) {
      if (!data.currentValue || !data.dangerThreshold) return ''
      
      if (data.currentValue >= data.dangerThreshold) return 'value-danger'
      if (data.warningThreshold && data.currentValue >= data.warningThreshold) return 'value-warning'
      return 'value-normal'
    },

    getValueStatusTagType(data) {
      if (!data.currentValue || !data.dangerThreshold) return 'info'
      
      if (data.currentValue >= data.dangerThreshold) return 'danger'
      if (data.warningThreshold && data.currentValue >= data.warningThreshold) return 'warning'
      return 'success'
    },

    getValueStatusLabel(data) {
      if (!data.currentValue || !data.dangerThreshold) return '未知'
      
      if (data.currentValue >= data.dangerThreshold) return '危险'
      if (data.warningThreshold && data.currentValue >= data.warningThreshold) return '预警'
      return '正常'
    },

    getThresholdComparisonLabel(comparison) {
      const labelMap = {
        'GREATER_THAN_OR_EQUAL': '大于等于',
        'GREATER_THAN': '大于',
        'LESS_THAN_OR_EQUAL': '小于等于',
        'LESS_THAN': '小于',
        'EQUAL': '等于',
        'NOT_EQUAL': '不等于'
      }
      return labelMap[comparison] || comparison
    },

    getAlertMethodLabel(method) {
      const labelMap = {
        'SYSTEM_NOTIFICATION': '系统通知',
        'EMAIL': '邮件通知',
        'SMS': '短信通知',
        'WECHAT': '微信通知',
        'DINGTALK': '钉钉通知'
      }
      return labelMap[method] || method
    },

    getAnomalyAlgorithmLabel(algorithm) {
      const labelMap = {
        'STATISTICAL': '统计方法',
        'MOVING_AVERAGE': '移动平均',
        'STANDARD_DEVIATION': '标准差',
        'MACHINE_LEARNING': '机器学习'
      }
      return labelMap[algorithm] || algorithm
    },

    getAnomalySensitivityLabel(sensitivity) {
      const labelMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高'
      }
      return labelMap[sensitivity] || sensitivity
    },

    // 历史数据相关方法
    getHistoryValueClass(value) {
      if (value >= 80) return 'history-value-danger'
      if (value >= 70) return 'history-value-warning'
      return 'history-value-normal'
    },

    getHistoryStatusTagType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'DANGER': 'danger'
      }
      return statusMap[status] || ''
    },

    getHistoryStatusLabel(status) {
      const labelMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'DANGER': '危险'
      }
      return labelMap[status] || status
    },

    getChangeRateClass(rate) {
      const numRate = parseFloat(rate)
      if (numRate > 0) return 'change-rate-up'
      if (numRate < 0) return 'change-rate-down'
      return 'change-rate-stable'
    },

    // 异常检测相关方法
    getAnomalyTypeTagType(type) {
      const typeMap = {
        'SPIKE': 'danger',
        'TREND': 'warning',
        'OUTLIER': 'info'
      }
      return typeMap[type] || ''
    },

    getAnomalyTypeLabel(type) {
      const labelMap = {
        'SPIKE': '突变',
        'TREND': '趋势',
        'OUTLIER': '异常值'
      }
      return labelMap[type] || type
    },

    getAnomalyScoreClass(score) {
      if (score >= 0.8) return 'anomaly-score-high'
      if (score >= 0.6) return 'anomaly-score-medium'
      return 'anomaly-score-low'
    },

    getProcessStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'PROCESSED': 'success',
        'IGNORED': 'info'
      }
      return statusMap[status] || ''
    },

    getProcessStatusLabel(status) {
      const labelMap = {
        'PENDING': '待处理',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return labelMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.monitoring-detail {
  .detail-section {
    padding: 20px;
  }

  .detail-item {
    margin-bottom: 16px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
    
    p {
      margin: 8px 0 0 0;
      color: #303133;
      line-height: 1.6;
    }
  }

  .config-content {
    background: #f5f7fa;
    padding: 12px;
    border-radius: 4px;
    margin: 8px 0 0 0;
    font-size: 12px;
    color: #606266;
    white-space: pre-wrap;
    max-height: 200px;
    overflow-y: auto;
  }

  // 状态概览样式
  .status-overview {
    margin-bottom: 24px;

    .status-card {
      background: white;
      border: 1px solid #ebeef5;
      border-radius: 8px;
      padding: 20px;
      text-align: center;

      .status-header {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;
        color: #606266;

        i {
          font-size: 18px;
          margin-right: 8px;
        }
      }

      .status-content {
        .update-time {
          font-size: 14px;
          color: #303133;
        }
      }
    }
  }

  .running-info {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
    }

    .info-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }

      .success-rate {
        font-weight: 600;
        color: #67c23a;
      }
    }
  }

  // 数值展示样式
  .value-display {
    margin-bottom: 24px;

    .current-value-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 30px;
      border-radius: 12px;
      text-align: center;

      .value-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 20px;
        }

        .value-unit {
          font-size: 16px;
          opacity: 0.8;
        }
      }

      .value-content {
        .current-value {
          font-size: 48px;
          font-weight: 700;
          margin-bottom: 16px;
        }
      }
    }
  }

  .threshold-comparison {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
    }

    .threshold-chart {
      position: relative;
      margin-bottom: 20px;

      .threshold-bar {
        display: flex;
        height: 60px;
        border-radius: 8px;
        overflow: hidden;

        .bar-section {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          color: white;
          font-size: 12px;

          &.normal {
            background: #67c23a;
          }

          &.warning {
            background: #e6a23c;
          }

          &.danger {
            background: #f56c6c;
          }

          .section-label {
            font-weight: 600;
            margin-bottom: 4px;
          }

          .section-range {
            font-size: 10px;
            opacity: 0.9;
          }
        }
      }

      .current-position {
        position: absolute;
        top: -10px;
        transform: translateX(-50%);

        .position-marker {
          background: #303133;
          color: white;
          padding: 8px 12px;
          border-radius: 6px;
          text-align: center;
          font-size: 12px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);

          &::after {
            content: '';
            position: absolute;
            top: 100%;
            left: 50%;
            transform: translateX(-50%);
            border: 6px solid transparent;
            border-top-color: #303133;
          }

          .marker-value {
            font-weight: 600;
            margin-top: 4px;
          }
        }
      }
    }
  }

  .value-history {
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
    }
  }

  // 预警配置样式
  .alert-config {
    .config-item {
      margin-bottom: 16px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }

      .alert-methods {
        margin-top: 8px;

        .method-tag {
          margin-right: 8px;
          margin-bottom: 8px;
        }
      }

      .message-template {
        background: #f5f7fa;
        padding: 12px;
        border-radius: 4px;
        margin-top: 8px;
        font-size: 14px;
        color: #606266;
        line-height: 1.6;
      }
    }
  }

  // 异常检测样式
  .anomaly-config {
    margin-bottom: 24px;

    .config-item {
      margin-bottom: 16px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
    }
  }

  .anomaly-results {
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
    }
  }

  // 数值样式
  .value-danger {
    color: #f56c6c;
    font-weight: 600;
  }

  .value-warning {
    color: #e6a23c;
    font-weight: 600;
  }

  .value-normal {
    color: #67c23a;
    font-weight: 600;
  }

  .history-value-danger {
    color: #f56c6c;
    font-weight: 600;
  }

  .history-value-warning {
    color: #e6a23c;
    font-weight: 600;
  }

  .history-value-normal {
    color: #67c23a;
    font-weight: 600;
  }

  .change-rate-up {
    color: #f56c6c;
  }

  .change-rate-down {
    color: #67c23a;
  }

  .change-rate-stable {
    color: #909399;
  }

  .anomaly-score-high {
    color: #f56c6c;
    font-weight: 600;
  }

  .anomaly-score-medium {
    color: #e6a23c;
    font-weight: 600;
  }

  .anomaly-score-low {
    color: #67c23a;
    font-weight: 600;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
