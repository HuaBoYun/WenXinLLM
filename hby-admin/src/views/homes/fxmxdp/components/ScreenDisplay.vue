<template>
  <div class="screen-display">
    <!-- 顶部标题栏 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="logo">
          <i class="el-icon-data-analysis"></i>
        </div>
        <div class="title-group">
          <h1 class="main-title">{{ showAllModels ? '风险模型监控大屏 - 全部模型' : '风险模型监控大屏' }}</h1>
          <p class="sub-title">{{ showAllModels ? 'All Models Monitoring Dashboard' : 'Risk Model Monitoring Dashboard' }}</p>
        </div>
      </div>
      <div class="header-center">
        <el-select
          v-if="!showAllModels"
          v-model="currentModelId"
          placeholder="选择模型"
          @change="handleModelChange"
          class="model-selector"
        >
          <el-option
            v-for="model in modelList"
            :key="model.evalModelId"
            :label="model.modelName"
            :value="model.evalModelId"
          />
        </el-select>
        <div v-else class="all-models-indicator">
          <i class="el-icon-s-data"></i>
          <span>全部模型统计</span>
        </div>
      </div>
      <div class="header-right">
        <div class="time-display">
          <div class="date">{{ currentDate }}</div>
          <div class="time">{{ currentTime }}</div>
        </div>
        <el-button
          type="danger"
          icon="el-icon-close"
          circle
          @click="handleClose"
          class="close-btn"
        />
      </div>
    </div>

    <!-- 主体内容区域 -->
    <div class="screen-body">
      <!-- 左侧区域 -->
      <div class="left-panel">
        <!-- 模型基本信息 -->
        <div class="panel-card" v-if="!showAllModels">
          <div class="card-header">
            <i class="el-icon-info"></i>
            <span>模型信息</span>
          </div>
          <div class="card-body">
            <div class="info-item">
              <span class="label">模型名称:</span>
              <span class="value">{{ currentModel.modelName || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">业务场景:</span>
              <span class="value">{{ getModelTypeName(currentModel.businessScenario) }}</span>
            </div>
            <div class="info-item">
              <span class="label">模型版本:</span>
              <span class="value">{{ currentModel.version || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">运行状态:</span>
              <el-tag :type="getStatusType(currentModel.status)" size="small">
                {{ getStatusText(currentModel.status) }}
              </el-tag>
            </div>
          </div>
        </div>

        <!-- 全部模型概览 -->
        <div class="panel-card" v-else>
          <div class="card-header">
            <i class="el-icon-s-data"></i>
            <span>模型概览</span>
          </div>
          <div class="card-body">
            <div class="info-item">
              <span class="label">在用模型:</span>
              <span class="value">{{ modelList.length }} 个</span>
            </div>
            <div class="info-item">
              <span class="label">总预警数:</span>
              <span class="value">{{ warningStats.total }} 条</span>
            </div>
            <div class="info-item">
              <span class="label">高风险:</span>
              <span class="value danger">{{ warningStats.high }} 条</span>
            </div>
            <div class="info-item">
              <span class="label">中风险:</span>
              <span class="value warning">{{ warningStats.medium }} 条</span>
            </div>
          </div>
        </div>

        <!-- 预警统计 -->
        <div class="panel-card">
          <div class="card-header">
            <i class="el-icon-warning"></i>
            <span>预警统计</span>
          </div>
          <div class="card-body">
            <div class="stat-grid">
              <div class="stat-item total">
                <div class="stat-value">{{ warningStats.total || 0 }}</div>
                <div class="stat-label">总预警数</div>
              </div>
              <div class="stat-item high">
                <div class="stat-value">{{ warningStats.high || 0 }}</div>
                <div class="stat-label">高风险</div>
              </div>
              <div class="stat-item medium">
                <div class="stat-value">{{ warningStats.medium || 0 }}</div>
                <div class="stat-label">中风险</div>
              </div>
              <div class="stat-item low">
                <div class="stat-value">{{ warningStats.low || 0 }}</div>
                <div class="stat-label">低风险</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 预警趋势图 -->
        <div class="panel-card flex-1">
          <div class="card-header">
            <i class="el-icon-data-line"></i>
            <span>预警趋势</span>
          </div>
          <div class="card-body">
            <div ref="trendChart" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-panel">
        <!-- 企业分布地图 -->
        <div class="panel-card map-card">
          <div class="card-header">
            <i class="el-icon-location"></i>
            <span>企业风险分布</span>
          </div>
          <div class="card-body">
            <div ref="mapChart" class="chart-container"></div>
          </div>
        </div>

        <!-- 企业风险列表 -->
        <div class="panel-card company-list-card">
          <div class="card-header">
            <i class="el-icon-office-building"></i>
            <span>企业风险排行</span>
            <el-button
              type="text"
              size="small"
              @click="handleRefreshData"
              style="color: #409eff; margin-left: auto;"
            >
              <i class="el-icon-refresh"></i> 刷新
            </el-button>
          </div>
          <div class="card-body">
            <div class="company-list">
              <div
                v-for="(company, index) in companyRiskList"
                :key="company.companyId"
                class="company-item"
                @click="handleCompanyClick(company)"
              >
                <div class="rank" :class="getRankClass(index)">{{ index + 1 }}</div>
                <div class="company-info">
                  <div class="company-name">{{ company.companyName }}</div>
                  <div class="risk-level">
                    <el-tag :type="getRiskLevelType(company.riskLevel)" size="mini">
                      {{ company.riskLevel }}
                    </el-tag>
                  </div>
                </div>
                <div class="warning-count">
                  <span class="count">{{ company.warningCount }}</span>
                  <span class="label">条预警</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-panel">
        <!-- 风险等级分布 -->
        <div class="panel-card">
          <div class="card-header">
            <i class="el-icon-pie-chart"></i>
            <span>风险等级分布</span>
          </div>
          <div class="card-body">
            <div ref="riskPieChart" class="chart-container"></div>
          </div>
        </div>

        <!-- 处理状态分布 -->
        <div class="panel-card">
          <div class="card-header">
            <i class="el-icon-s-data"></i>
            <span>处理状态</span>
          </div>
          <div class="card-body">
            <div ref="statusChart" class="chart-container"></div>
          </div>
        </div>

        <!-- 近期预警 -->
        <div class="panel-card flex-1">
          <div class="card-header">
            <i class="el-icon-bell"></i>
            <span>近期预警</span>
          </div>
          <div class="card-body">
            <div class="warning-list">
              <div
                v-for="warning in recentWarnings"
                :key="warning.warningId"
                class="warning-item"
                @click="handleWarningClick(warning)"
              >
                <div class="warning-time">{{ formatTime(warning.createTime) }}</div>
                <div class="warning-content">
                  <div class="warning-title">{{ warning.warningTitle }}</div>
                  <div class="warning-company">{{ warning.companyName }}</div>
                </div>
                <el-tag :type="getRiskLevelType(warning.warningLevel)" size="mini">
                  {{ warning.warningLevel }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 企业详情弹窗 -->
    <el-dialog
      :visible.sync="companyDetailVisible"
      :title="selectedCompany.companyName"
      width="80%"
      custom-class="company-detail-dialog"
      @close="handleCompanyDetailClose"
    >
      <company-detail
        v-if="companyDetailVisible"
        :company="selectedCompany"
        :model-id="currentModelId"
      />
    </el-dialog>

    <!-- 预警详情弹窗 -->
    <el-dialog
      :visible.sync="warningDetailVisible"
      :title="selectedWarning.warningTitle"
      width="70%"
      custom-class="warning-detail-dialog"
      @close="handleWarningDetailClose"
    >
      <warning-detail
        v-if="warningDetailVisible"
        :warning="selectedWarning"
      />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getModelWarningResults,
  getModelWarningCount,
  getRiskWarningList,
  getWarningStatistics
} from '@/api/mxgl'
import CompanyDetail from './CompanyDetail.vue'
import WarningDetail from './WarningDetail.vue'

export default {
  name: 'ScreenDisplay',
  components: {
    CompanyDetail,
    WarningDetail
  },
  props: {
    selectedModel: {
      type: Object,
      default: () => null
    },
    modelList: {
      type: Array,
      default: () => []
    },
    showAllModels: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentModelId: '',
      currentModel: {},
      currentDate: '',
      currentTime: '',
      timer: null,
      dataTimer: null,
      warningStats: {
        total: 0,
        high: 0,
        medium: 0,
        low: 0
      },
      companyRiskList: [],
      recentWarnings: [],
      companyDetailVisible: false,
      selectedCompany: {},
      warningDetailVisible: false,
      selectedWarning: {},
      // 图表实例
      trendChart: null,
      mapChart: null,
      riskPieChart: null,
      statusChart: null,
      // 模型类型映射
      modelTypeMap: {
        'PROCUREMENT': '采购风险',
        'FINANCIAL': '财务风险',
        'CREDIT': '信用风险',
        'COMPLIANCE': '合规风险',
        'INVESTMENT': '投资风险',
        'GROUP_CONTROL': '集团管控',
        'BUSINESS': '经营风险',
        'LEGAL': '法律风险',
        'TRADE': '贸易风险'
      }
    }
  },
  mounted() {
    this.initTime()
    this.initModel()
    this.initCharts()
    this.loadData()
    
    // 定时刷新数据
    this.dataTimer = setInterval(() => {
      this.loadData()
    }, 60000) // 每分钟刷新一次
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    if (this.dataTimer) {
      clearInterval(this.dataTimer)
    }
    this.disposeCharts()
  },
  methods: {
    initTime() {
      this.updateTime()
      this.timer = setInterval(() => {
        this.updateTime()
      }, 1000)
    },
    updateTime() {
      const now = new Date()
      this.currentDate = now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        weekday: 'long'
      })
      this.currentTime = now.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    initModel() {
      if (this.showAllModels) {
        // 展示所有模型统计,不需要选择特定模型
        this.currentModelId = ''
        this.currentModel = {}
      } else if (this.selectedModel && this.selectedModel.evalModelId) {
        this.currentModelId = this.selectedModel.evalModelId
        this.currentModel = this.selectedModel
      } else if (this.modelList.length > 0) {
        this.currentModelId = this.modelList[0].evalModelId
        this.currentModel = this.modelList[0]
      }
    },
    handleModelChange(modelId) {
      const model = this.modelList.find(m => m.evalModelId === modelId)
      if (model) {
        this.currentModel = model
        this.$emit('model-change', model)
        this.loadData()
      }
    },
    async loadData() {
      await Promise.all([
        this.loadWarningStats(),
        this.loadCompanyRiskList(),
        this.loadRecentWarnings()
      ])
      this.updateCharts()
    },
    async loadWarningStats() {
      try {
        // 使用真实API获取预警统计数据
        const params = this.showAllModels ? {} : { evalModelId: this.currentModelId }
        const response = await getWarningStatistics(params)

        if (response.code === 1 && response.data) {
          const data = response.data
          // 从API响应中提取风险等级统计
          const riskLevelStats = data.riskLevelStatistics || {}
          this.warningStats = {
            total: data.totalCount || 0,
            high: riskLevelStats.HIGH || 0,
            medium: riskLevelStats.MEDIUM || 0,
            low: riskLevelStats.LOW || 0
          }
        } else {
          console.error('获取预警统计失败:', response.msg)
          this.warningStats = { total: 0, high: 0, medium: 0, low: 0 }
        }
      } catch (error) {
        console.error('获取预警统计失败:', error)
        this.warningStats = { total: 0, high: 0, medium: 0, low: 0 }
      }
    },
    async loadCompanyRiskList() {
      try {
        // 使用真实API获取模型预警结果，按公司分组
        const params = {
          pageNum: 1,
          pageSize: 10,
          orderBy: 'warningCount',
          orderDirection: 'DESC'
        }

        // 如果不是展示所有模型,则添加模型ID过滤
        if (!this.showAllModels && this.currentModelId) {
          params.evalModelId = this.currentModelId
        }

        const response = await getModelWarningResults(params)

        if (response.code === 1 && response.data) {
          const list = response.data.list || []
          // 将预警结果转换为企业风险列表格式
          this.companyRiskList = list.map(item => ({
            companyId: item.companyId || item.id,
            companyName: item.companyName || '未知企业',
            riskLevel: this.mapRiskLevel(item.warningLevel || item.riskLevel),
            warningCount: item.warningCount || 1,
            warningLevel: item.warningLevel || item.riskLevel
          }))
        } else {
          console.error('获取企业风险列表失败:', response.msg)
          this.companyRiskList = []
        }
      } catch (error) {
        console.error('获取企业风险列表失败:', error)
        this.companyRiskList = []
      }
    },
    mapRiskLevel(level) {
      const levelMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险',
        '高': '高风险',
        '中': '中风险',
        '低': '低风险'
      }
      return levelMap[level] || level || '未知'
    },
    async loadRecentWarnings() {
      try {
        // 使用真实API获取近期预警列表
        const params = {
          pageNum: 1,
          pageSize: 10,
          orderBy: 'createTime',
          orderDirection: 'DESC'
        }

        // 如果不是展示所有模型,则添加模型ID过滤
        if (!this.showAllModels && this.currentModelId) {
          params.evalModelId = this.currentModelId
        }

        const response = await getRiskWarningList(params)

        if (response.code === 1 && response.data) {
          const list = response.data.list || []
          this.recentWarnings = list.map(item => ({
            warningId: item.warningId || item.id,
            warningTitle: item.warningTitle || item.warningType || '预警',
            companyName: item.companyName || '未知企业',
            warningLevel: this.mapRiskLevel(item.warningLevel || item.riskLevel),
            createTime: item.createTime || item.warningTime || new Date(),
            warningCode: item.warningCode,
            warningType: item.warningType,
            warningStatus: item.warningStatus
          }))
        } else {
          console.error('获取近期预警失败:', response.msg)
          this.recentWarnings = []
        }
      } catch (error) {
        console.error('获取近期预警失败:', error)
        this.recentWarnings = []
      }
    },
    initCharts() {
      this.$nextTick(() => {
        this.initTrendChart()
        this.initMapChart()
        this.initRiskPieChart()
        this.initStatusChart()
      })
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return
      this.trendChart = echarts.init(this.$refs.trendChart)

      const option = {
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '10%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月'],
          axisLine: { lineStyle: { color: '#409eff' } },
          axisLabel: { color: '#fff' }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: '#409eff' } },
          axisLabel: { color: '#fff' },
          splitLine: { lineStyle: { color: 'rgba(64, 158, 255, 0.2)' } }
        },
        series: [{
          name: '预警数量',
          type: 'line',
          smooth: true,
          data: [12, 18, 15, 22, 28, 25],
          itemStyle: { color: '#409eff' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ])
          }
        }]
      }

      this.trendChart.setOption(option)
    },
    initMapChart() {
      if (!this.$refs.mapChart) return
      this.mapChart = echarts.init(this.$refs.mapChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        geo: {
          map: 'china',
          roam: true,
          itemStyle: {
            areaColor: '#1e3a8a',
            borderColor: '#409eff'
          },
          emphasis: {
            itemStyle: {
              areaColor: '#2563eb'
            }
          }
        },
        series: [{
          type: 'scatter',
          coordinateSystem: 'geo',
          data: [
            { name: '北京', value: [116.4074, 39.9042, 15] },
            { name: '上海', value: [121.4737, 31.2304, 12] },
            { name: '广州', value: [113.2644, 23.1291, 8] }
          ],
          symbolSize: val => val[2] * 2,
          itemStyle: {
            color: '#f56c6c'
          }
        }]
      }

      this.mapChart.setOption(option)
    },
    initRiskPieChart() {
      if (!this.$refs.riskPieChart) return
      this.riskPieChart = echarts.init(this.$refs.riskPieChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: { color: '#fff' }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['35%', '50%'],
          data: [
            { value: this.warningStats.high, name: '高风险', itemStyle: { color: '#f56c6c' } },
            { value: this.warningStats.medium, name: '中风险', itemStyle: { color: '#e6a23c' } },
            { value: this.warningStats.low, name: '低风险', itemStyle: { color: '#67c23a' } }
          ],
          label: {
            color: '#fff',
            formatter: '{b}: {c} ({d}%)'
          }
        }]
      }

      this.riskPieChart.setOption(option)
    },
    initStatusChart() {
      if (!this.$refs.statusChart) return
      this.statusChart = echarts.init(this.$refs.statusChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        series: [{
          type: 'pie',
          radius: '70%',
          data: [
            { value: 35, name: '待处理', itemStyle: { color: '#f56c6c' } },
            { value: 45, name: '处理中', itemStyle: { color: '#e6a23c' } },
            { value: 65, name: '已处理', itemStyle: { color: '#67c23a' } }
          ],
          label: {
            color: '#fff',
            formatter: '{b}\n{c} ({d}%)'
          }
        }]
      }

      this.statusChart.setOption(option)
    },
    updateCharts() {
      if (this.riskPieChart) {
        this.riskPieChart.setOption({
          series: [{
            data: [
              { value: this.warningStats.high, name: '高风险' },
              { value: this.warningStats.medium, name: '中风险' },
              { value: this.warningStats.low, name: '低风险' }
            ]
          }]
        })
      }
    },
    getModelTypeName(businessScenario) {
      return this.modelTypeMap[businessScenario] || '其他'
    },
    getStatusType(status) {
      const typeMap = {
        'ENABLED': 'success',
        'DISABLED': 'info',
        'DRAFT': 'warning'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'ENABLED': '运行中',
        'DISABLED': '已停用',
        'DRAFT': '草稿'
      }
      return textMap[status] || status
    },
    getRankClass(index) {
      if (index === 0) return 'gold'
      if (index === 1) return 'silver'
      if (index === 2) return 'bronze'
      return ''
    },
    getRiskLevelType(level) {
      const typeMap = {
        '高风险': 'danger',
        '中风险': 'warning',
        '低风险': 'success',
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[level] || 'info'
    },
    formatTime(time) {
      if (!time) return '-'
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
      return date.toLocaleDateString()
    },
    handleRefreshData() {
      this.loadData()
      this.$message.success('数据已刷新')
    },
    handleCompanyClick(company) {
      this.selectedCompany = company
      this.companyDetailVisible = true
    },
    handleCompanyDetailClose() {
      this.companyDetailVisible = false
      this.selectedCompany = {}
    },
    handleWarningClick(warning) {
      this.selectedWarning = warning
      this.warningDetailVisible = true
    },
    handleWarningDetailClose() {
      this.warningDetailVisible = false
      this.selectedWarning = {}
    },
    handleClose() {
      this.$emit('close')
    },
    disposeCharts() {
      if (this.trendChart) this.trendChart.dispose()
      if (this.mapChart) this.mapChart.dispose()
      if (this.riskPieChart) this.riskPieChart.dispose()
      if (this.statusChart) this.statusChart.dispose()
    }
  }
}
</script>

<style lang="scss" scoped>
.screen-display {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #0a0e27 0%, #1a1f3a 100%);
  color: #fff;
  overflow: hidden;

  .screen-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 40px;
    background: rgba(255, 255, 255, 0.05);
    border-bottom: 2px solid rgba(64, 158, 255, 0.3);
    backdrop-filter: blur(10px);

    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;

      .logo {
        width: 60px;
        height: 60px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;

        i {
          font-size: 32px;
          color: #fff;
        }
      }

      .title-group {
        .main-title {
          font-size: 32px;
          font-weight: bold;
          margin: 0;
          background: linear-gradient(90deg, #409eff, #67c23a);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }

        .sub-title {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.6);
          margin: 5px 0 0 0;
        }
      }
    }

    .header-center {
      .model-selector {
        width: 300px;

        ::v-deep .el-input__inner {
          background: rgba(255, 255, 255, 0.1);
          border-color: rgba(64, 158, 255, 0.5);
          color: #fff;
          font-size: 16px;
        }
      }

      .all-models-indicator {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 10px 20px;
        background: rgba(64, 158, 255, 0.2);
        border: 1px solid rgba(64, 158, 255, 0.5);
        border-radius: 4px;
        font-size: 16px;
        color: #409eff;

        i {
          font-size: 20px;
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;

      .time-display {
        text-align: right;

        .date {
          font-size: 16px;
          color: rgba(255, 255, 255, 0.9);
        }

        .time {
          font-size: 24px;
          font-weight: bold;
          color: #409eff;
          margin-top: 5px;
        }
      }

      .close-btn {
        background: rgba(245, 108, 108, 0.2);
        border-color: #f56c6c;
        color: #f56c6c;

        &:hover {
          background: #f56c6c;
          color: #fff;
        }
      }
    }
  }

  .screen-body {
    display: flex;
    gap: 20px;
    padding: 20px;
    height: calc(100vh - 120px);

    .left-panel,
    .right-panel {
      flex: 0 0 25%;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .center-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .panel-card {
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      border: 1px solid rgba(64, 158, 255, 0.2);
      backdrop-filter: blur(10px);
      overflow: hidden;

      &.flex-1 {
        flex: 1;
        display: flex;
        flex-direction: column;
      }

      &.map-card {
        flex: 1;
      }

      &.company-list-card {
        flex: 0 0 300px;
      }

      .card-header {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 15px 20px;
        background: rgba(64, 158, 255, 0.1);
        border-bottom: 1px solid rgba(64, 158, 255, 0.2);
        font-size: 16px;
        font-weight: bold;

        i {
          font-size: 20px;
          color: #409eff;
        }
      }

      .card-body {
        padding: 20px;
        flex: 1;
        overflow: auto;

        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-thumb {
          background: rgba(64, 158, 255, 0.3);
          border-radius: 3px;
        }
      }
    }

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);

      &:last-child {
        border-bottom: none;
      }

      .label {
        color: rgba(255, 255, 255, 0.6);
        font-size: 14px;
      }

      .value {
        color: #fff;
        font-size: 14px;
        font-weight: bold;

        &.danger {
          color: #f56c6c;
        }

        &.warning {
          color: #e6a23c;
        }

        &.success {
          color: #67c23a;
        }
      }
    }

    .stat-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;

      .stat-item {
        text-align: center;
        padding: 20px;
        background: rgba(64, 158, 255, 0.1);
        border-radius: 8px;
        border: 1px solid rgba(64, 158, 255, 0.2);

        .stat-value {
          font-size: 32px;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.6);
        }

        &.total .stat-value {
          color: #409eff;
        }

        &.high .stat-value {
          color: #f56c6c;
        }

        &.medium .stat-value {
          color: #e6a23c;
        }

        &.low .stat-value {
          color: #67c23a;
        }
      }
    }

    .chart-container {
      width: 100%;
      height: 100%;
      min-height: 200px;
    }

    .company-list {
      .company-item {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 15px;
        margin-bottom: 10px;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 8px;
        border: 1px solid rgba(64, 158, 255, 0.2);
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background: rgba(64, 158, 255, 0.1);
          transform: translateX(5px);
        }

        .rank {
          width: 32px;
          height: 32px;
          background: rgba(255, 255, 255, 0.1);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          font-size: 16px;

          &.gold {
            background: linear-gradient(135deg, #ffd700, #ffed4e);
            color: #000;
          }

          &.silver {
            background: linear-gradient(135deg, #c0c0c0, #e8e8e8);
            color: #000;
          }

          &.bronze {
            background: linear-gradient(135deg, #cd7f32, #e8a87c);
            color: #000;
          }
        }

        .company-info {
          flex: 1;

          .company-name {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
          }
        }

        .warning-count {
          text-align: right;

          .count {
            font-size: 24px;
            font-weight: bold;
            color: #f56c6c;
          }

          .label {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.6);
            margin-left: 5px;
          }
        }
      }
    }

    .warning-list {
      .warning-item {
        padding: 15px;
        margin-bottom: 10px;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 8px;
        border-left: 3px solid #409eff;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background: rgba(64, 158, 255, 0.1);
        }

        .warning-time {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.5);
          margin-bottom: 8px;
        }

        .warning-content {
          margin-bottom: 8px;

          .warning-title {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
          }

          .warning-company {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.6);
          }
        }
      }
    }
  }
}

::v-deep .company-detail-dialog,
::v-deep .warning-detail-dialog {
  .el-dialog {
    background: rgba(10, 14, 39, 0.95);
    border: 1px solid rgba(64, 158, 255, 0.3);
  }

  .el-dialog__header {
    background: rgba(64, 158, 255, 0.1);
    border-bottom: 1px solid rgba(64, 158, 255, 0.3);
  }

  .el-dialog__title {
    color: #fff;
  }

  .el-dialog__body {
    color: #fff;
  }
}
</style>

