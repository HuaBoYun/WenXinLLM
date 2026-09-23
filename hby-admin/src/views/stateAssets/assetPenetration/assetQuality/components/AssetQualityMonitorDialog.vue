<template>
  <el-dialog
    title="资产质量监控"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <div class="monitor-content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="监控配置" name="config">
          <el-form ref="monitorForm" :model="monitorForm" :rules="rules" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="监控名称" prop="monitorName">
                  <el-input v-model="monitorForm.monitorName" placeholder="请输入监控名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="监控类型" prop="monitorType">
                  <el-select v-model="monitorForm.monitorType" placeholder="请选择监控类型" style="width: 100%;">
                    <el-option label="实时监控" value="REAL_TIME"></el-option>
                    <el-option label="定期监控" value="SCHEDULED"></el-option>
                    <el-option label="事件触发" value="EVENT_DRIVEN"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="监控频率" prop="monitorFrequency">
                  <el-select v-model="monitorForm.monitorFrequency" placeholder="请选择监控频率" style="width: 100%;">
                    <el-option label="每小时" value="HOURLY"></el-option>
                    <el-option label="每日" value="DAILY"></el-option>
                    <el-option label="每周" value="WEEKLY"></el-option>
                    <el-option label="每月" value="MONTHLY"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="预警阈值" prop="alertThreshold">
                  <el-input-number
                    v-model="monitorForm.alertThreshold"
                    :min="0"
                    :max="100"
                    placeholder="请输入预警阈值"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="监控指标" prop="monitorIndicators">
              <el-checkbox-group v-model="monitorForm.monitorIndicators">
                <el-checkbox label="COMPLETENESS">完整性</el-checkbox>
                <el-checkbox label="ACCURACY">准确性</el-checkbox>
                <el-checkbox label="TIMELINESS">时效性</el-checkbox>
                <el-checkbox label="AVAILABILITY">可用性</el-checkbox>
                <el-checkbox label="SECURITY">安全性</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="通知方式" prop="notificationMethods">
              <el-checkbox-group v-model="monitorForm.notificationMethods">
                <el-checkbox label="EMAIL">邮件通知</el-checkbox>
                <el-checkbox label="SMS">短信通知</el-checkbox>
                <el-checkbox label="SYSTEM">系统通知</el-checkbox>
                <el-checkbox label="WEBHOOK">Webhook</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="监控描述" prop="monitorDescription">
              <el-input
                v-model="monitorForm.monitorDescription"
                type="textarea"
                :rows="3"
                placeholder="请输入监控描述"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="监控状态" name="status">
          <div class="status-panel">
            <el-row :gutter="20" class="mb-20">
              <el-col :span="6">
                <el-card shadow="hover" class="status-card">
                  <div class="status-item">
                    <div class="status-value">{{ monitorStatus.totalMonitors }}</div>
                    <div class="status-label">总监控数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="status-card">
                  <div class="status-item">
                    <div class="status-value active">{{ monitorStatus.activeMonitors }}</div>
                    <div class="status-label">活跃监控</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="status-card">
                  <div class="status-item">
                    <div class="status-value warning">{{ monitorStatus.alertCount }}</div>
                    <div class="status-label">预警数量</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="status-card">
                  <div class="status-item">
                    <div class="status-value">{{ monitorStatus.avgQualityScore }}%</div>
                    <div class="status-label">平均质量分</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>质量趋势</span>
                  </div>
                  <div ref="qualityTrendChart" class="chart-container"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never">
                  <div slot="header">
                    <span>预警分布</span>
                  </div>
                  <div ref="alertDistributionChart" class="chart-container"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="预警记录" name="alerts">
          <div class="alerts-panel">
            <el-table :data="alertRecords" border>
              <el-table-column label="预警时间" prop="alertTime" width="180" />
              <el-table-column label="预警类型" prop="alertType" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getAlertTypeTag(scope.row.alertType)" size="mini">
                    {{ getAlertTypeText(scope.row.alertType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="预警级别" prop="alertLevel" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getAlertLevelTag(scope.row.alertLevel)" size="mini">
                    {{ scope.row.alertLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="资产名称" prop="assetName" width="150" />
              <el-table-column label="预警内容" prop="alertContent" />
              <el-table-column label="处理状态" prop="handleStatus" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getHandleStatusTag(scope.row.handleStatus)" size="mini">
                    {{ getHandleStatusText(scope.row.handleStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="handleAlert(scope.row)">
                    处理
                  </el-button>
                  <el-button type="text" size="mini" @click="viewAlert(scope.row)">
                    查看
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="activeTab === 'config'" type="primary" @click="handleSave">保存配置</el-button>
      <el-button v-if="activeTab === 'status'" type="primary" @click="handleRefresh">刷新状态</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { setAssetQualityMonitoring, getAssetQualityMonitoring, getAssetQualityAlerts, handleAssetQualityAlert, getAssetQualityAlertDetail } from '@/api/stateAssets/assetQuality'

export default {
  name: 'AssetQualityMonitorDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'config',
      monitorForm: {
        monitorName: '',
        monitorType: '',
        monitorFrequency: '',
        alertThreshold: 80,
        monitorIndicators: [],
        notificationMethods: [],
        monitorDescription: ''
      },
      rules: {
        monitorName: [
          { required: true, message: '请输入监控名称', trigger: 'blur' }
        ],
        monitorType: [
          { required: true, message: '请选择监控类型', trigger: 'change' }
        ],
        monitorFrequency: [
          { required: true, message: '请选择监控频率', trigger: 'change' }
        ]
      },
      monitorStatus: {
        totalMonitors: 0,
        activeMonitors: 0,
        alertCount: 0,
        avgQualityScore: 0
      },
      alertRecords: [],
      // 图表实例
      qualityTrendChart: null,
      alertDistributionChart: null
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
      if (val) {
        this.initData()
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    initData() {
      this.loadMonitorStatus()
      this.loadAlertRecords()
      if (this.assetData && Object.keys(this.assetData).length > 0) {
        this.monitorForm.monitorName = (this.assetData.enterpriseName || '') + '资产质量监控'
      }
    },

    async loadMonitorStatus() {
      try {
        const response = await getAssetQualityMonitoring({ assetQualityId: this.assetData.assetQualityId })
        const data = response.data
        if (data) {
          this.monitorStatus = {
            totalMonitors: data.totalMonitors || 0,
            activeMonitors: data.activeMonitors || 0,
            alertCount: data.alertCount || 0,
            avgQualityScore: data.avgQualityScore || 0
          }
        }
      } catch (error) {
        console.error('加载监控状态失败:', error)
      }
    },

    async loadAlertRecords() {
      try {
        const response = await getAssetQualityAlerts({ assetQualityId: this.assetData.assetQualityId })
        const data = response.data
        if (data && Array.isArray(data)) {
          this.alertRecords = data.map(item => ({
            alertId: item.alertId,
            alertTime: item.alertTime || '-',
            alertType: item.alertType || 'WARNING',
            alertLevel: item.alertLevel || '中',
            assetName: item.enterpriseName || this.assetData.enterpriseName || '-',
            alertContent: item.alertContent || '-',
            handleStatus: item.handleStatus || 'PENDING'
          }))
        } else {
          this.alertRecords = []
        }
      } catch (error) {
        console.error('加载预警记录失败:', error)
        this.alertRecords = []
      }
    },
    
    initCharts() {
      this.qualityTrendChart = echarts.init(this.$refs.qualityTrendChart)
      this.alertDistributionChart = echarts.init(this.$refs.alertDistributionChart)
      
      this.updateQualityTrendChart()
      this.updateAlertDistributionChart()
      
      window.addEventListener('resize', this.handleResize)
    },
    
    updateQualityTrendChart() {
      const score = this.monitorStatus.avgQualityScore || 0
      const option = {
        title: {
          text: '质量趋势',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['当前']
        },
        yAxis: {
          type: 'value',
          name: '质量评分',
          min: 0,
          max: 100
        },
        series: [
          {
            name: '平均质量评分',
            type: 'bar',
            data: [score],
            itemStyle: { color: score >= 80 ? '#67C23A' : score >= 60 ? '#E6A23C' : '#F56C6C' }
          }
        ]
      }
      this.qualityTrendChart.setOption(option)
    },

    updateAlertDistributionChart() {
      const alertCount = this.monitorStatus.alertCount || 0
      const total = this.monitorStatus.totalMonitors || 1
      const option = {
        title: {
          text: '预警分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
          {
            name: '监控状态',
            type: 'pie',
            radius: '60%',
            data: [
              { name: '正常', value: total - alertCount },
              { name: '预警', value: alertCount }
            ],
            itemStyle: {
              color: function(params) {
                const colors = ['#67C23A', '#F56C6C']
                return colors[params.dataIndex]
              }
            }
          }
        ]
      }
      this.alertDistributionChart.setOption(option)
    },
    
    handleSave() {
      this.$refs.monitorForm.validate(async (valid) => {
        if (valid) {
          try {
            await setAssetQualityMonitoring({
              assetQualityId: this.assetData.assetQualityId,
              ...this.monitorForm
            })
            this.$message.success('监控配置保存成功')
            // 保存后刷新监控状态和预警数据
            await this.loadMonitorStatus()
            await this.loadAlertRecords()
            this.updateQualityTrendChart()
            this.updateAlertDistributionChart()
            this.$emit('refresh')
          } catch (error) {
            this.$message.error('保存失败：' + (error.message || '网络错误'))
          }
        }
      })
    },
    
    async handleRefresh() {
      await this.loadMonitorStatus()
      await this.loadAlertRecords()
      this.updateQualityTrendChart()
      this.updateAlertDistributionChart()
      this.$message.success('状态刷新成功')
    },
    
    handleAlert(row) {
      this.$confirm(
        `确认处理该预警？<br/><br/>企业：${row.enterpriseName || row.assetName}<br/>预警内容：${row.alertContent}`,
        '处理预警',
        { confirmButtonText: '确认处理', cancelButtonText: '取消', type: 'warning', dangerouslyUseHTMLString: true }
      ).then(async () => {
        try {
          await handleAssetQualityAlert({ alertId: row.alertId })
          this.$message.success('预警处理成功')
          // 刷新预警列表和监控状态
          await this.loadAlertRecords()
          await this.loadMonitorStatus()
          this.updateAlertDistributionChart()
        } catch (error) {
          this.$message.error('处理失败：' + (error.message || '网络错误'))
        }
      }).catch(() => {})
    },

    async viewAlert(row) {
      try {
        const response = await getAssetQualityAlertDetail({ alertId: row.alertId })
        const data = response.data || {}
        this.$alert(
          `<div style="padding:10px;">
            <table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;">
              <tr><td style="width:100px;background:#f5f7fa;font-weight:bold;">企业名称</td><td>${data.enterpriseName || '-'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">资产名称</td><td>${data.assetName || '-'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">资产类别</td><td>${data.assetCategory || '-'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">资产价值</td><td>${data.assetValue || 0}万元</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">质量等级</td><td>${data.qualityLevel || '-'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">质量评分</td><td><span style="color:${(data.qualityScore || 0) < 60 ? '#F56C6C' : '#E6A23C'};font-weight:bold;">${data.qualityScore || 0}分</span></td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">风险等级</td><td><span style="color:${data.riskLevel === 'CRITICAL' ? '#F56C6C' : '#E6A23C'};font-weight:bold;">${data.riskLevel === 'CRITICAL' ? '极高风险' : data.riskLevel === 'HIGH' ? '高风险' : data.riskLevel || '-'}</span></td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">减值金额</td><td>${data.impairmentAmount || 0}万元</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">收益率</td><td>${data.returnRate || 0}%</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">评估状态</td><td>${data.assessmentStatus || '-'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">质量描述</td><td>${data.qualityDescription || '暂无描述'}</td></tr>
              <tr><td style="background:#f5f7fa;font-weight:bold;">评估时间</td><td>${data.assessmentDate || '-'}</td></tr>
            </table>
          </div>`,
          '预警详情',
          { dangerouslyUseHTMLString: true, customClass: 'alert-detail-dialog' }
        )
      } catch (error) {
        this.$message.error('查看详情失败：' + (error.message || '网络错误'))
      }
    },
    
    handleClose() {
      this.$emit('update:visible', false)
      this.destroyCharts()
    },
    
    handleResize() {
      if (this.qualityTrendChart) this.qualityTrendChart.resize()
      if (this.alertDistributionChart) this.alertDistributionChart.resize()
    },
    
    destroyCharts() {
      if (this.qualityTrendChart) {
        this.qualityTrendChart.dispose()
        this.qualityTrendChart = null
      }
      if (this.alertDistributionChart) {
        this.alertDistributionChart.dispose()
        this.alertDistributionChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },
    
    getAlertTypeText(type) {
      const typeMap = {
        'CRITICAL': '严重预警',
        'WARNING': '一般预警',
        'QUALITY_DECLINE': '质量下降',
        'DATA_MISSING': '数据缺失',
        'SECURITY_RISK': '安全风险'
      }
      return typeMap[type] || type
    },

    getAlertTypeTag(type) {
      const tagMap = {
        'CRITICAL': 'danger',
        'WARNING': 'warning',
        'QUALITY_DECLINE': 'danger',
        'DATA_MISSING': 'warning',
        'SECURITY_RISK': 'danger'
      }
      return tagMap[type] || 'info'
    },
    
    getAlertLevelTag(level) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'info'
      }
      return tagMap[level] || 'info'
    },
    
    getHandleStatusText(status) {
      const statusMap = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'RESOLVED': '已解决'
      }
      return statusMap[status] || status
    },
    
    getHandleStatusTag(status) {
      const tagMap = {
        'PENDING': 'danger',
        'PROCESSING': 'warning',
        'RESOLVED': 'success'
      }
      return tagMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.monitor-content {
  min-height: 500px;
}

.status-panel {
  padding: 20px 0;
}

.status-card {
  text-align: center;
}

.status-item {
  padding: 20px;
}

.status-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.status-value.active {
  color: #67C23A;
}

.status-value.warning {
  color: #E6A23C;
}

.status-label {
  color: #606266;
  font-size: 14px;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.alerts-panel {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
