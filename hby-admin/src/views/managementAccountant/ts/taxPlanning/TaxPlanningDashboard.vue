<template>
  <div class="tax-planning-dashboard" v-loading="loading">
    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card total">
            <div class="card-icon">
              <i class="el-icon-s-marketing"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ overviewData.totalCount || 0 }}</div>
              <div class="card-label">总筹划数</div>
              <div class="card-trend" :class="getTrendClass(overviewData.totalTrend)">
                <i :class="getTrendIcon(overviewData.totalTrend)"></i>
                {{ Math.abs(overviewData.totalTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="overview-card executing">
            <div class="card-icon">
              <i class="el-icon-loading"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ overviewData.executingCount || 0 }}</div>
              <div class="card-label">执行中</div>
              <div class="card-trend" :class="getTrendClass(overviewData.executingTrend)">
                <i :class="getTrendIcon(overviewData.executingTrend)"></i>
                {{ Math.abs(overviewData.executingTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="overview-card saving">
            <div class="card-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ formatAmount(overviewData.totalTaxSaving) }}</div>
              <div class="card-label">总节税金额(万元)</div>
              <div class="card-trend" :class="getTrendClass(overviewData.savingTrend)">
                <i :class="getTrendIcon(overviewData.savingTrend)"></i>
                {{ Math.abs(overviewData.savingTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="overview-card roi">
            <div class="card-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ formatPercentage(overviewData.avgRoi) }}</div>
              <div class="card-label">平均投资回报率</div>
              <div class="card-trend" :class="getTrendClass(overviewData.roiTrend)">
                <i :class="getTrendIcon(overviewData.roiTrend)"></i>
                {{ Math.abs(overviewData.roiTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 筹划趋势图 -->
        <el-col :span="12">
          <el-card header="筹划趋势分析" class="chart-card">
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="loadTrendData">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
                <el-radio-button label="quarter">近3个月</el-radio-button>
              </el-radio-group>
            </div>
            <div ref="trendChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 节税趋势图 -->
        <el-col :span="12">
          <el-card header="节税趋势分析" class="chart-card">
            <div class="chart-controls">
              <el-radio-group v-model="savingPeriod" size="small" @change="loadSavingTrendData">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
                <el-radio-button label="quarter">近3个月</el-radio-button>
              </el-radio-group>
            </div>
            <div ref="savingChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px">
        <!-- 筹划类型分布 -->
        <el-col :span="8">
          <el-card header="筹划类型分布" class="chart-card">
            <div ref="typeChart" class="chart-container small"></div>
          </el-card>
        </el-col>
        
        <!-- 风险等级分布 -->
        <el-col :span="8">
          <el-card header="风险等级分布" class="chart-card">
            <div ref="riskChart" class="chart-container small"></div>
          </el-card>
        </el-col>
        
        <!-- 执行状态分布 -->
        <el-col :span="8">
          <el-card header="执行状态分布" class="chart-card">
            <div ref="statusChart" class="chart-container small"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜和效率统计 -->
    <div class="ranking-section">
      <el-row :gutter="20">
        <!-- 筹划排行榜 -->
        <el-col :span="12">
          <el-card header="筹划排行榜" class="ranking-card">
            <div class="ranking-controls">
              <el-select v-model="rankingType" size="small" @change="loadRankingData">
                <el-option label="按节税金额" value="tax_saving" />
                <el-option label="按净收益" value="net_benefit" />
                <el-option label="按投资回报率" value="roi" />
              </el-select>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in rankingData"
                :key="item.planningId"
                class="ranking-item"
                @click="handleViewPlanning(item)"
              >
                <div class="ranking-number" :class="getRankingClass(index)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="planning-name">{{ item.planningName }}</div>
                  <div class="planning-meta">
                    <span class="planning-type">{{ getPlanningTypeLabel(item.planningType) }}</span>
                    <span class="planning-value">{{ formatRankingValue(item, rankingType) }}</span>
                  </div>
                </div>
                <div class="ranking-progress">
                  <el-progress
                    :percentage="Number(item.executionProgress || 0)"
                    :stroke-width="6"
                    :show-text="false"
                  />
                </div>
              </div>
              
              <div v-if="!rankingData.length" class="empty-ranking">
                <i class="el-icon-document"></i>
                <p>暂无数据</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 效率统计 -->
        <el-col :span="12">
          <el-card header="效率统计" class="efficiency-card">
            <div class="efficiency-metrics">
              <div class="metric-item">
                <div class="metric-label">平均执行周期</div>
                <div class="metric-value">{{ efficiencyData.avgExecutionDays || 0 }}天</div>
              </div>
              
              <div class="metric-item">
                <div class="metric-label">按时完成率</div>
                <div class="metric-value success">{{ formatPercentage(efficiencyData.onTimeRate) }}</div>
              </div>
              
              <div class="metric-item">
                <div class="metric-label">平均风险评分</div>
                <div class="metric-value" :class="getRiskScoreClass(efficiencyData.avgRiskScore)">
                  {{ efficiencyData.avgRiskScore || 0 }}分
                </div>
              </div>
              
              <div class="metric-item">
                <div class="metric-label">成功执行率</div>
                <div class="metric-value success">{{ formatPercentage(efficiencyData.successRate) }}</div>
              </div>
            </div>
            
            <div class="efficiency-chart">
              <div ref="efficiencyChart" class="chart-container small"></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 待处理提醒 -->
    <div class="alerts-section">
      <el-card header="待处理提醒" class="alerts-card">
        <div class="alert-tabs">
          <el-tabs v-model="alertTab">
            <el-tab-pane label="即将到期" name="expiring">
              <div class="alert-list">
                <div
                  v-for="item in expiringPlannings"
                  :key="item.planningId"
                  class="alert-item expiring"
                  @click="handleViewPlanning(item)"
                >
                  <div class="alert-icon">
                    <i class="el-icon-warning"></i>
                  </div>
                  <div class="alert-content">
                    <div class="alert-title">{{ item.planningName }}</div>
                    <div class="alert-desc">将在 {{ getDaysUntilExpiry(item.endTime) }} 天后到期</div>
                  </div>
                  <div class="alert-time">{{ formatDate(item.endTime) }}</div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="高风险筹划" name="high-risk">
              <div class="alert-list">
                <div
                  v-for="item in highRiskPlannings"
                  :key="item.planningId"
                  class="alert-item high-risk"
                  @click="handleViewPlanning(item)"
                >
                  <div class="alert-icon">
                    <i class="el-icon-warning-outline"></i>
                  </div>
                  <div class="alert-content">
                    <div class="alert-title">{{ item.planningName }}</div>
                    <div class="alert-desc">风险等级：{{ getRiskLevelLabel(item.riskLevel) }}</div>
                  </div>
                  <div class="alert-status">
                    <el-tag type="danger" size="mini">{{ getRiskLevelLabel(item.riskLevel) }}</el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="执行异常" name="execution-issues">
              <div class="alert-list">
                <div
                  v-for="item in executionIssues"
                  :key="item.planningId"
                  class="alert-item execution-issue"
                  @click="handleViewPlanning(item)"
                >
                  <div class="alert-icon">
                    <i class="el-icon-circle-close"></i>
                  </div>
                  <div class="alert-content">
                    <div class="alert-title">{{ item.planningName }}</div>
                    <div class="alert-desc">执行状态：{{ getExecutionStatusLabel(item.executionStatus) }}</div>
                  </div>
                  <div class="alert-status">
                    <el-tag type="danger" size="mini">{{ getExecutionStatusLabel(item.executionStatus) }}</el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getPlanningOverview,
  getPlanningTrend,
  getTaxSavingTrend,
  getBenefitTrend,
  countPlanningsByType,
  countPlanningsByRiskLevel,
  countPlanningsByExecutionStatus,
  getPlanningRanking,
  getPlanningEfficiencyStats,
  getExpiringSoonPlannings,
  getHighRiskPlannings,
  getPlanningsByExecutionStatus,
  getPlanningTypeLabel,
  getRiskLevelLabel,
  getExecutionStatusLabel,
  formatAmount,
  formatPercentage
} from '@/api/managementAccountant/ts/taxPlanning'

export default {
  name: 'TaxPlanningDashboard',
  data() {
    return {
      loading: false,
      
      // 概览数据
      overviewData: {},
      
      // 图表控制
      trendPeriod: 'month',
      savingPeriod: 'month',
      
      // 排行榜
      rankingType: 'tax_saving',
      rankingData: [],
      
      // 效率统计
      efficiencyData: {},
      
      // 提醒数据
      alertTab: 'expiring',
      expiringPlannings: [],
      highRiskPlannings: [],
      executionIssues: [],
      
      // 图表实例
      charts: {}
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
    
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
    
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadOverviewData(),
          this.loadTrendData(),
          this.loadSavingTrendData(),
          this.loadDistributionData(),
          this.loadRankingData(),
          this.loadEfficiencyData(),
          this.loadAlertData()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getPlanningOverview(this.$store.getters.tenantId)
        if (response.success) {
          this.overviewData = response.data || {}
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    
    // 加载趋势数据
    async loadTrendData() {
      try {
        const endDate = new Date()
        const startDate = new Date()
        
        switch (this.trendPeriod) {
          case 'week':
            startDate.setDate(endDate.getDate() - 7)
            break
          case 'month':
            startDate.setMonth(endDate.getMonth() - 1)
            break
          case 'quarter':
            startDate.setMonth(endDate.getMonth() - 3)
            break
        }
        
        const response = await getPlanningTrend(
          this.$store.getters.tenantId,
          startDate.toISOString(),
          endDate.toISOString(),
          'day'
        )
        
        if (response.success && this.charts.trendChart) {
          this.updateTrendChart(response.data || [])
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },
    
    // 加载节税趋势数据
    async loadSavingTrendData() {
      try {
        const endDate = new Date()
        const startDate = new Date()
        
        switch (this.savingPeriod) {
          case 'week':
            startDate.setDate(endDate.getDate() - 7)
            break
          case 'month':
            startDate.setMonth(endDate.getMonth() - 1)
            break
          case 'quarter':
            startDate.setMonth(endDate.getMonth() - 3)
            break
        }
        
        const response = await getTaxSavingTrend(
          this.$store.getters.tenantId,
          startDate.toISOString(),
          endDate.toISOString(),
          'day'
        )
        
        if (response.success && this.charts.savingChart) {
          this.updateSavingChart(response.data || [])
        }
      } catch (error) {
        console.error('加载节税趋势数据失败:', error)
      }
    },
    
    // 加载分布数据
    async loadDistributionData() {
      try {
        const [typeRes, riskRes, statusRes] = await Promise.all([
          countPlanningsByType(this.$store.getters.tenantId),
          countPlanningsByRiskLevel(this.$store.getters.tenantId),
          countPlanningsByExecutionStatus(this.$store.getters.tenantId)
        ])
        
        if (typeRes.success && this.charts.typeChart) {
          this.updateTypeChart(typeRes.data || [])
        }
        
        if (riskRes.success && this.charts.riskChart) {
          this.updateRiskChart(riskRes.data || [])
        }
        
        if (statusRes.success && this.charts.statusChart) {
          this.updateStatusChart(statusRes.data || [])
        }
      } catch (error) {
        console.error('加载分布数据失败:', error)
      }
    },
    
    // 加载排行榜数据
    async loadRankingData() {
      try {
        const response = await getPlanningRanking(this.$store.getters.tenantId, this.rankingType, 10)
        if (response.success) {
          this.rankingData = response.data || []
        }
      } catch (error) {
        console.error('加载排行榜数据失败:', error)
      }
    },
    
    // 加载效率数据
    async loadEfficiencyData() {
      try {
        const response = await getPlanningEfficiencyStats(this.$store.getters.tenantId)
        if (response.success) {
          this.efficiencyData = response.data || {}
          if (this.charts.efficiencyChart) {
            this.updateEfficiencyChart()
          }
        }
      } catch (error) {
        console.error('加载效率数据失败:', error)
      }
    },
    
    // 加载提醒数据
    async loadAlertData() {
      try {
        const [expiringRes, highRiskRes, issuesRes] = await Promise.all([
          getExpiringSoonPlannings(this.$store.getters.tenantId, 7),
          getHighRiskPlannings(this.$store.getters.tenantId),
          getPlanningsByExecutionStatus(this.$store.getters.tenantId, 'FAILED')
        ])
        
        if (expiringRes.success) {
          this.expiringPlannings = expiringRes.data || []
        }
        
        if (highRiskRes.success) {
          this.highRiskPlannings = highRiskRes.data || []
        }
        
        if (issuesRes.success) {
          this.executionIssues = issuesRes.data || []
        }
      } catch (error) {
        console.error('加载提醒数据失败:', error)
      }
    },
    
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.charts.trendChart = echarts.init(this.$refs.trendChart)
        this.charts.savingChart = echarts.init(this.$refs.savingChart)
        this.charts.typeChart = echarts.init(this.$refs.typeChart)
        this.charts.riskChart = echarts.init(this.$refs.riskChart)
        this.charts.statusChart = echarts.init(this.$refs.statusChart)
        this.charts.efficiencyChart = echarts.init(this.$refs.efficiencyChart)
      })
    },
    
    // 更新趋势图表
    updateTrendChart(data) {
      const option = {
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
          name: '筹划数量',
          type: 'line',
          data: data.map(item => item.count),
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.charts.trendChart.setOption(option)
    },
    
    // 更新节税图表
    updateSavingChart(data) {
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: {c}万元'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '节税金额',
          type: 'bar',
          data: data.map(item => item.amount),
          itemStyle: {
            color: '#67C23A'
          }
        }]
      }
      this.charts.savingChart.setOption(option)
    },
    
    // 更新类型分布图表
    updateTypeChart(data) {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: '70%',
          data: data.map(item => ({
            name: getPlanningTypeLabel(item.type),
            value: item.count
          }))
        }]
      }
      this.charts.typeChart.setOption(option)
    },
    
    // 更新风险分布图表
    updateRiskChart(data) {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: '70%',
          data: data.map(item => ({
            name: getRiskLevelLabel(item.level),
            value: item.count
          }))
        }]
      }
      this.charts.riskChart.setOption(option)
    },
    
    // 更新状态分布图表
    updateStatusChart(data) {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: '70%',
          data: data.map(item => ({
            name: getExecutionStatusLabel(item.status),
            value: item.count
          }))
        }]
      }
      this.charts.statusChart.setOption(option)
    },
    
    // 更新效率图表
    updateEfficiencyChart() {
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        radar: {
          indicator: [
            { name: '执行效率', max: 100 },
            { name: '按时完成', max: 100 },
            { name: '成功率', max: 100 },
            { name: '风险控制', max: 100 },
            { name: '收益率', max: 100 }
          ]
        },
        series: [{
          type: 'radar',
          data: [{
            value: [
              this.efficiencyData.executionEfficiency || 0,
              (this.efficiencyData.onTimeRate || 0) * 100,
              (this.efficiencyData.successRate || 0) * 100,
              100 - (this.efficiencyData.avgRiskScore || 0),
              (this.efficiencyData.avgRoi || 0) * 100
            ],
            name: '综合效率'
          }]
        }]
      }
      this.charts.efficiencyChart.setOption(option)
    },
    
    // 窗口大小变化处理
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) {
          chart.resize()
        }
      })
    },
    
    // 刷新数据
    refreshData() {
      this.loadData()
    },
    
    // 查看筹划详情
    handleViewPlanning(planning) {
      this.$emit('view-planning', planning)
    },
    
    // 工具方法
    getTrendClass(trend) {
      if (!trend) return ''
      return trend > 0 ? 'trend-up' : 'trend-down'
    },
    
    getTrendIcon(trend) {
      if (!trend) return 'el-icon-minus'
      return trend > 0 ? 'el-icon-top' : 'el-icon-bottom'
    },
    
    getRankingClass(index) {
      if (index === 0) return 'first'
      if (index === 1) return 'second'
      if (index === 2) return 'third'
      return ''
    },
    
    getRiskScoreClass(score) {
      if (!score) return ''
      if (score >= 80) return 'danger'
      if (score >= 60) return 'warning'
      return 'success'
    },
    
    formatRankingValue(item, type) {
      switch (type) {
        case 'tax_saving':
          return formatAmount(item.taxSavingAmount) + '万元'
        case 'net_benefit':
          return formatAmount(item.netBenefit) + '万元'
        case 'roi':
          return formatPercentage(item.roi)
        default:
          return '-'
      }
    },
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleDateString('zh-CN')
    },
    
    getDaysUntilExpiry(endTime) {
      if (!endTime) return 0
      const now = new Date()
      const end = new Date(endTime)
      const diffTime = end - now
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return Math.max(0, diffDays)
    },
    
    // 导入的工具方法
    getPlanningTypeLabel,
    getRiskLevelLabel,
    getExecutionStatusLabel,
    formatAmount,
    formatPercentage
  }
}
</script>

<style lang="scss" scoped>
.tax-planning-dashboard {
  .overview-section {
    margin-bottom: 20px;
    
    .overview-card {
      display: flex;
      align-items: center;
      padding: 24px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      position: relative;
      overflow: hidden;
      
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
      }
      
      &.total::before {
        background: linear-gradient(90deg, #667eea, #764ba2);
      }
      
      &.executing::before {
        background: linear-gradient(90deg, #f093fb, #f5576c);
      }
      
      &.saving::before {
        background: linear-gradient(90deg, #4facfe, #00f2fe);
      }
      
      &.roi::before {
        background: linear-gradient(90deg, #43e97b, #38f9d7);
      }
      
      .card-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
          font-size: 28px;
          color: white;
        }
      }
      
      .total .card-icon {
        background: linear-gradient(135deg, #667eea, #764ba2);
      }
      
      .executing .card-icon {
        background: linear-gradient(135deg, #f093fb, #f5576c);
      }
      
      .saving .card-icon {
        background: linear-gradient(135deg, #4facfe, #00f2fe);
      }
      
      .roi .card-icon {
        background: linear-gradient(135deg, #43e97b, #38f9d7);
      }
      
      .card-content {
        flex: 1;
        
        .card-number {
          font-size: 32px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 8px;
        }
        
        .card-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }
        
        .card-trend {
          font-size: 12px;
          
          &.trend-up {
            color: #67C23A;
          }
          
          &.trend-down {
            color: #F56C6C;
          }
          
          i {
            margin-right: 4px;
          }
        }
      }
    }
  }
  
  .charts-section {
    margin-bottom: 20px;
    
    .chart-card {
      .chart-controls {
        margin-bottom: 16px;
        text-align: right;
      }
      
      .chart-container {
        height: 300px;
        
        &.small {
          height: 250px;
        }
      }
    }
  }
  
  .ranking-section {
    margin-bottom: 20px;
    
    .ranking-card {
      .ranking-controls {
        margin-bottom: 16px;
        text-align: right;
      }
      
      .ranking-list {
        max-height: 400px;
        overflow-y: auto;
        
        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: background-color 0.3s ease;
          
          &:hover {
            background-color: #f5f7fa;
          }
          
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
            font-weight: 600;
            color: white;
            margin-right: 12px;
            background: #909399;
            
            &.first {
              background: #FFD700;
            }
            
            &.second {
              background: #C0C0C0;
            }
            
            &.third {
              background: #CD7F32;
            }
          }
          
          .ranking-content {
            flex: 1;
            
            .planning-name {
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            
            .planning-meta {
              display: flex;
              justify-content: space-between;
              align-items: center;
              
              .planning-type {
                font-size: 12px;
                color: #909399;
              }
              
              .planning-value {
                font-size: 12px;
                font-weight: 600;
                color: #67C23A;
              }
            }
          }
          
          .ranking-progress {
            width: 80px;
            margin-left: 12px;
          }
        }
        
        .empty-ranking {
          text-align: center;
          padding: 40px 20px;
          color: #909399;
          
          i {
            font-size: 48px;
            margin-bottom: 16px;
          }
          
          p {
            margin: 0;
            font-size: 14px;
          }
        }
      }
    }
    
    .efficiency-card {
      .efficiency-metrics {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
        margin-bottom: 20px;
        
        .metric-item {
          text-align: center;
          padding: 16px;
          background: #f5f7fa;
          border-radius: 4px;
          
          .metric-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 8px;
          }
          
          .metric-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            
            &.success {
              color: #67C23A;
            }
            
            &.warning {
              color: #E6A23C;
            }
            
            &.danger {
              color: #F56C6C;
            }
          }
        }
      }
      
      .efficiency-chart {
        .chart-container {
          height: 200px;
        }
      }
    }
  }
  
  .alerts-section {
    .alerts-card {
      .alert-tabs {
        .alert-list {
          max-height: 300px;
          overflow-y: auto;
          
          .alert-item {
            display: flex;
            align-items: center;
            padding: 12px;
            border-left: 4px solid transparent;
            border-bottom: 1px solid #f0f0f0;
            cursor: pointer;
            transition: all 0.3s ease;
            
            &:hover {
              background-color: #f5f7fa;
            }
            
            &:last-child {
              border-bottom: none;
            }
            
            &.expiring {
              border-left-color: #E6A23C;
            }
            
            &.high-risk {
              border-left-color: #F56C6C;
            }
            
            &.execution-issue {
              border-left-color: #F56C6C;
            }
            
            .alert-icon {
              width: 40px;
              height: 40px;
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              margin-right: 12px;
              
              i {
                font-size: 18px;
                color: white;
              }
            }
            
            .expiring .alert-icon {
              background: #E6A23C;
            }
            
            .high-risk .alert-icon,
            .execution-issue .alert-icon {
              background: #F56C6C;
            }
            
            .alert-content {
              flex: 1;
              
              .alert-title {
                font-weight: 500;
                color: #303133;
                margin-bottom: 4px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              
              .alert-desc {
                font-size: 12px;
                color: #909399;
              }
            }
            
            .alert-time,
            .alert-status {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }
    }
  }
}
</style>
