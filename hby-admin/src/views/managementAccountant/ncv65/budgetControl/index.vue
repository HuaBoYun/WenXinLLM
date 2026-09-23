<template>
  <div class="budget-control-index">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算控制管理</h2>
      <p>全面的预算控制体系，实现预算执行监控、控制规则、预警机制和限额管理</p>
    </div>

    <!-- 控制概览统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card execution-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.executionRate }}%</div>
            <div class="stat-label">预算执行率</div>
            <div class="stat-description">当前期间整体执行情况</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="controlStats.executionRate" 
                :show-text="false" 
                stroke-width="6"
                :color="getExecutionColor(controlStats.executionRate)"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-line"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card control-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.controlRules }}</div>
            <div class="stat-label">控制规则</div>
            <div class="stat-description">已配置的控制规则数量</div>
            <div class="stat-progress">
              <el-progress :percentage="85" :show-text="false" stroke-width="6" color="#409EFF" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card warning-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.warningCount }}</div>
            <div class="stat-label">预警事项</div>
            <div class="stat-description">需要关注的预警信息</div>
            <div class="stat-progress">
              <el-progress :percentage="65" :show-text="false" stroke-width="6" color="#E6A23C" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card limit-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ controlStats.limitExceeded }}</div>
            <div class="stat-label">超限事项</div>
            <div class="stat-description">超出限额的预算项目</div>
            <div class="stat-progress">
              <el-progress :percentage="25" :show-text="false" stroke-width="6" color="#F56C6C" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 控制功能模块 -->
    <el-row :gutter="20" class="modules-row">
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('execution')">
          <div class="module-header">
            <div class="module-icon execution-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="module-info">
              <h3>预算执行监控</h3>
              <p>实时监控预算执行情况</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">监控项目</span>
              <span class="stat-value">{{ moduleStats.execution.projects }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">执行率</span>
              <span class="stat-value success-text">{{ moduleStats.execution.rate }}%</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.execution.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('control')">
          <div class="module-header">
            <div class="module-icon control-icon">
              <i class="el-icon-s-check"></i>
            </div>
            <div class="module-info">
              <h3>预算控制管理</h3>
              <p>配置和管理控制规则</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">控制规则</span>
              <span class="stat-value">{{ moduleStats.control.rules }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">生效规则</span>
              <span class="stat-value primary-text">{{ moduleStats.control.activeRules }}</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.control.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('warning')">
          <div class="module-header">
            <div class="module-icon warning-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="module-info">
              <h3>预算预警管理</h3>
              <p>预警规则和预警处理</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">预警规则</span>
              <span class="stat-value">{{ moduleStats.warning.rules }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">待处理</span>
              <span class="stat-value warning-text">{{ moduleStats.warning.pending }}</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.warning.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="modules-row">
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('alert')">
          <div class="module-header">
            <div class="module-icon alert-icon">
              <i class="el-icon-bell"></i>
            </div>
            <div class="module-info">
              <h3>预算警报管理</h3>
              <p>警报配置和通知管理</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">警报规则</span>
              <span class="stat-value">{{ moduleStats.alert.rules }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">今日警报</span>
              <span class="stat-value danger-text">{{ moduleStats.alert.today }}</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.alert.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('limit')">
          <div class="module-header">
            <div class="module-icon limit-icon">
              <i class="el-icon-s-operation"></i>
            </div>
            <div class="module-info">
              <h3>预算限额管理</h3>
              <p>限额设置和超限控制</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">限额项目</span>
              <span class="stat-value">{{ moduleStats.limit.items }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">超限项目</span>
              <span class="stat-value danger-text">{{ moduleStats.limit.exceeded }}</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.limit.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="module-card" shadow="hover" @click.native="navigateToModule('quota')">
          <div class="module-header">
            <div class="module-icon quota-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="module-info">
              <h3>预算配额管理</h3>
              <p>配额分配和使用监控</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">配额项目</span>
              <span class="stat-value">{{ moduleStats.quota.items }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">使用率</span>
              <span class="stat-value primary-text">{{ moduleStats.quota.usage }}%</span>
            </div>
          </div>
          <div class="module-footer">
            <span class="update-time">更新时间：{{ moduleStats.quota.updateTime }}</span>
            <el-button type="text" size="mini">进入管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 控制趋势图表 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="card-header">
            <h3>执行趋势分析</h3>
            <el-button-group size="mini">
              <el-button :type="trendPeriod === 'month' ? 'primary' : ''" @click="changeTrendPeriod('month')">月度</el-button>
              <el-button :type="trendPeriod === 'quarter' ? 'primary' : ''" @click="changeTrendPeriod('quarter')">季度</el-button>
              <el-button :type="trendPeriod === 'year' ? 'primary' : ''" @click="changeTrendPeriod('year')">年度</el-button>
            </el-button-group>
          </div>
          <div id="executionTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="card-header">
            <h3>控制效果分析</h3>
            <el-button-group size="mini">
              <el-button :type="controlPeriod === 'week' ? 'primary' : ''" @click="changeControlPeriod('week')">周度</el-button>
              <el-button :type="controlPeriod === 'month' ? 'primary' : ''" @click="changeControlPeriod('month')">月度</el-button>
            </el-button-group>
          </div>
          <div id="controlEffectChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 控制健康度评估 -->
    <el-card class="health-card" shadow="never">
      <div class="card-header">
        <h3>控制健康度评估</h3>
        <div class="health-score">
          <span class="score-label">综合评分</span>
          <span class="score-value" :class="getHealthScoreClass(healthScore)">{{ healthScore }}</span>
          <span class="score-total">/100</span>
        </div>
      </div>
      <el-row :gutter="20" class="health-metrics">
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">执行合规性</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.compliance" :show-text="false" stroke-width="8" />
              <span class="metric-score">{{ healthMetrics.compliance }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">控制有效性</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.effectiveness" :show-text="false" stroke-width="8" color="#67C23A" />
              <span class="metric-score">{{ healthMetrics.effectiveness }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">预警及时性</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.timeliness" :show-text="false" stroke-width="8" color="#E6A23C" />
              <span class="metric-score">{{ healthMetrics.timeliness }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">限额管理</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.limitManagement" :show-text="false" stroke-width="8" color="#409EFF" />
              <span class="metric-score">{{ healthMetrics.limitManagement }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">异常处理</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.exceptionHandling" :show-text="false" stroke-width="8" color="#F56C6C" />
              <span class="metric-score">{{ healthMetrics.exceptionHandling }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="metric-item">
            <div class="metric-label">系统稳定性</div>
            <div class="metric-value">
              <el-progress :percentage="healthMetrics.stability" :show-text="false" stroke-width="8" color="#909399" />
              <span class="metric-score">{{ healthMetrics.stability }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  budgetControlApi,
  budgetExecutionApi
} from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetControlIndex',
  data() {
    return {
      // 控制统计数据
      controlStats: {
        executionRate: 0,
        controlRules: 0,
        warningCount: 0,
        limitExceeded: 0
      },

      // 模块统计数据
      moduleStats: {
        execution: {
          projects: 0,
          rate: 0,
          updateTime: '--'
        },
        control: {
          rules: 0,
          activeRules: 0,
          updateTime: '--'
        },
        warning: {
          rules: 0,
          pending: 0,
          updateTime: '--'
        },
        alert: {
          rules: 0,
          today: 0,
          updateTime: '--'
        },
        limit: {
          items: 0,
          exceeded: 0,
          updateTime: '--'
        },
        quota: {
          items: 0,
          usage: 0,
          updateTime: '--'
        }
      },

      // 图表周期
      trendPeriod: 'month',
      controlPeriod: 'week',

      // 健康度评估
      healthScore: 0,
      healthMetrics: {
        compliance: 0,
        effectiveness: 0,
        timeliness: 0,
        limitManagement: 0,
        exceptionHandling: 0,
        stability: 0
      }
    }
  },

  mounted() {
    this.loadAllData()
  },

  beforeDestroy() {
    if (this.executionTrendChart) this.executionTrendChart.dispose()
    if (this.controlEffectChart) this.controlEffectChart.dispose()
  },

  methods: {
    // 加载所有数据（统计 + 健康度 + 图表）
    async loadAllData() {
      await Promise.all([
        this.loadControlStats(),
        this.loadHealthMetrics()
      ])
      // 统计数据加载完成后初始化图表
      this.$nextTick(() => {
        this.initCharts()
      })
    },

    // 加载统计数据 - 使用后端统一接口，数据来源于数据库
    async loadControlStats() {
      const now = new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
      try {
        const res = await budgetControlApi.getControlStats()
        if (res.code === 1 && res.data) {
          const d = res.data
          // 顶部4个统计卡片
          this.controlStats.executionRate = d.controlEffectiveness || 0
          this.controlStats.controlRules = d.totalRules || 0
          this.controlStats.warningCount = d.pendingWarnings || 0
          this.controlStats.limitExceeded = d.overBudgetExecutions || 0

          // 6个模块卡片
          this.moduleStats.execution = {
            projects: d.totalExecutions || 0,
            rate: d.controlEffectiveness || 0,
            updateTime: now
          }
          this.moduleStats.control = {
            rules: d.totalRules || 0,
            activeRules: d.activeRules || 0,
            updateTime: now
          }
          this.moduleStats.warning = {
            rules: d.totalWarnings || 0,
            pending: d.pendingWarnings || 0,
            updateTime: now
          }
          this.moduleStats.alert = {
            rules: d.totalWarnings || 0,
            today: d.pendingWarnings || 0,
            updateTime: now
          }
          this.moduleStats.limit = {
            items: d.budgetLimits || 0,
            exceeded: d.overBudgetExecutions || 0,
            updateTime: now
          }
          this.moduleStats.quota = {
            items: d.budgetLimits || 0,
            usage: d.warningResponseRate || 0,
            updateTime: now
          }
        }
      } catch (error) {
        console.error('加载预算控制统计数据失败', error)
      }
    },

    // 加载健康度数据 - 使用后端统一接口，数据来源于数据库
    async loadHealthMetrics() {
      try {
        const res = await budgetControlApi.getControlHealth()
        if (res.code === 1 && res.data) {
          const d = res.data
          this.healthScore = d.overallScore || 0

          // 将后端返回的metrics数组映射到前端6个指标
          const metricsMap = {}
          if (d.metrics && d.metrics.length) {
            d.metrics.forEach(m => { metricsMap[m.key] = m.value || 0 })
          }
          this.healthMetrics.compliance = metricsMap.controlExecution || metricsMap.ruleCoverage || 0
          this.healthMetrics.effectiveness = metricsMap.ruleCoverage || metricsMap.controlExecution || 0
          this.healthMetrics.timeliness = metricsMap.warningTimeliness || 0
          this.healthMetrics.limitManagement = metricsMap.frozenRate || 0
          // 异常处理和系统稳定性从总体评分推导
          this.healthMetrics.exceptionHandling = metricsMap.warningTimeliness
            ? Math.min(Math.round(metricsMap.warningTimeliness * 0.9), 100)
            : 0
          this.healthMetrics.stability = d.overallScore
            ? Math.min(Math.round(d.overallScore * 1.05), 100)
            : 0
        }
      } catch (error) {
        console.error('加载预算控制健康度失败', error)
      }
    },

    // 导航到模块 - 使用 /qmys/ 路由
    navigateToModule(module) {
      const routeMap = {
        execution: '/qmys/BudgetExecution',
        control: '/qmys/BudgetControl',
        warning: '/qmys/BudgetWarning',
        alert: '/qmys/BudgetAlert',
        limit: '/qmys/BudgetLimit',
        quota: '/qmys/BudgetQuota'
      }
      this.$router.push(routeMap[module])
    },

    // 改变趋势周期
    changeTrendPeriod(period) {
      this.trendPeriod = period
      this.loadTrendChartData()
    },

    // 改变控制周期
    changeControlPeriod(period) {
      this.controlPeriod = period
      this.loadControlEffectData()
    },

    // 获取执行率颜色
    getExecutionColor(rate) {
      if (rate >= 80) return '#67C23A'
      if (rate >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取健康评分样式类
    getHealthScoreClass(score) {
      if (score >= 90) return 'excellent'
      if (score >= 80) return 'good'
      if (score >= 70) return 'fair'
      return 'poor'
    },

    // 初始化图表
    initCharts() {
      this.initExecutionTrendChart()
      this.initControlEffectChart()
      // 加载图表数据
      this.loadTrendChartData()
      this.loadControlEffectData()
    },

    // 初始化执行趋势图表
    initExecutionTrendChart() {
      const chartDom = document.getElementById('executionTrendChart')
      if (!chartDom) return
      const chart = echarts.init(chartDom)
      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['预算执行率', '控制达标率'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: [] },
        yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
        series: [
          { name: '预算执行率', type: 'line', data: [], smooth: true, itemStyle: { color: '#409EFF' }, areaStyle: { color: 'rgba(64,158,255,0.1)' } },
          { name: '控制达标率', type: 'line', data: [], smooth: true, itemStyle: { color: '#67C23A' }, areaStyle: { color: 'rgba(103,194,58,0.1)' } }
        ]
      }
      chart.setOption(option)
      this.executionTrendChart = chart
    },

    // 初始化控制效果图表
    initControlEffectChart() {
      const chartDom = document.getElementById('controlEffectChart')
      if (!chartDom) return
      const chart = echarts.init(chartDom)
      const option = {
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [{
          name: '控制效果',
          type: 'pie',
          radius: ['40%', '65%'],
          data: [],
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } },
          label: { formatter: '{b}\n{d}%' }
        }]
      }
      chart.setOption(option)
      this.controlEffectChart = chart
    },

    // 加载执行趋势图表数据 - 从后端API获取
    async loadTrendChartData() {
      if (!this.executionTrendChart) return
      try {
        const res = await budgetExecutionApi.getTrendAnalysis({ period: this.trendPeriod })
        if (res.code === 1 && res.data) {
          const d = res.data
          const categories = d.categories || d.labels || d.months || []
          const executionRates = d.executionRates || d.executionRate || []
          const complianceRates = d.complianceRates || d.controlRate || []
          this.executionTrendChart.setOption({
            xAxis: { data: categories },
            series: [
              { name: '预算执行率', data: executionRates },
              { name: '控制达标率', data: complianceRates }
            ]
          })
        }
      } catch (error) {
        console.error('加载执行趋势数据失败', error)
      }
    },

    // 加载控制效果图表数据 - 从统计数据构建
    async loadControlEffectData() {
      if (!this.controlEffectChart) return
      const stats = this.controlStats
      const pieData = [
        { value: Math.max(stats.controlRules, 1), name: '控制有效', itemStyle: { color: '#67C23A' } },
        { value: Math.max(stats.warningCount, 1), name: '预警处理', itemStyle: { color: '#E6A23C' } },
        { value: Math.max(stats.limitExceeded, 1), name: '超限控制', itemStyle: { color: '#F56C6C' } },
        { value: Math.max(Math.round(stats.executionRate * 0.1) || 1, 1), name: '异常处理', itemStyle: { color: '#909399' } }
      ]
      this.controlEffectChart.setOption({
        series: [{ data: pieData }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-control-index {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }
    
    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
  
  .stats-row,
  .modules-row,
  .charts-row {
    margin-bottom: 20px;
  }
  
  .stat-card {
    border: none;
    border-radius: 8px;
    position: relative;
    overflow: hidden;
    cursor: pointer;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
    }
    
    &.execution-card {
      background: linear-gradient(135deg, #409EFF, #66B1FF);
      color: white;
    }
    
    &.control-card {
      background: linear-gradient(135deg, #67C23A, #85CE61);
      color: white;
    }
    
    &.warning-card {
      background: linear-gradient(135deg, #E6A23C, #EEBE77);
      color: white;
    }
    
    &.limit-card {
      background: linear-gradient(135deg, #F56C6C, #F78989);
      color: white;
    }
    
    .stat-content {
      position: relative;
      z-index: 2;
      
      .stat-number {
        font-size: 28px;
        font-weight: 600;
        margin-bottom: 4px;
      }
      
      .stat-label {
        font-size: 16px;
        opacity: 0.9;
        margin-bottom: 4px;
      }
      
      .stat-description {
        font-size: 12px;
        opacity: 0.8;
        margin-bottom: 8px;
      }
      
      .stat-progress {
        margin-top: 8px;
      }
    }
    
    .stat-icon {
      position: absolute;
      top: 20px;
      right: 20px;
      font-size: 48px;
      opacity: 0.3;
    }
  }
  
  .module-card {
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }
    
    .module-header {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
      
      .module-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        font-size: 24px;
        
        &.execution-icon {
          background: linear-gradient(135deg, #409EFF, #66B1FF);
          color: white;
        }
        
        &.control-icon {
          background: linear-gradient(135deg, #67C23A, #85CE61);
          color: white;
        }
        
        &.warning-icon {
          background: linear-gradient(135deg, #E6A23C, #EEBE77);
          color: white;
        }
        
        &.alert-icon {
          background: linear-gradient(135deg, #F56C6C, #F78989);
          color: white;
        }
        
        &.limit-icon {
          background: linear-gradient(135deg, #909399, #B1B3B8);
          color: white;
        }
        
        &.quota-icon {
          background: linear-gradient(135deg, #409EFF, #66B1FF);
          color: white;
        }
      }
      
      .module-info {
        flex: 1;
        
        h3 {
          color: #303133;
          font-size: 16px;
          margin: 0 0 4px 0;
        }
        
        p {
          color: #606266;
          font-size: 12px;
          margin: 0;
        }
      }
    }
    
    .module-stats {
      display: flex;
      justify-content: space-between;
      margin-bottom: 15px;
      
      .stat-item {
        text-align: center;
        
        .stat-label {
          display: block;
          color: #909399;
          font-size: 12px;
          margin-bottom: 4px;
        }
        
        .stat-value {
          font-size: 18px;
          font-weight: 600;
          
          &.success-text {
            color: #67C23A;
          }
          
          &.primary-text {
            color: #409EFF;
          }
          
          &.warning-text {
            color: #E6A23C;
          }
          
          &.danger-text {
            color: #F56C6C;
          }
        }
      }
    }
    
    .module-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 15px;
      border-top: 1px solid #EBEEF5;
      
      .update-time {
        color: #909399;
        font-size: 12px;
      }
    }
  }
  

  .chart-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        font-size: 16px;
        margin: 0;
      }
    }
    
    .chart-container {
      height: 300px;
    }
  }
  
  .health-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        font-size: 18px;
        margin: 0;
      }
      
      .health-score {
        display: flex;
        align-items: baseline;
        
        .score-label {
          color: #606266;
          font-size: 14px;
          margin-right: 8px;
        }
        
        .score-value {
          font-size: 32px;
          font-weight: 600;
          margin-right: 4px;
          
          &.excellent {
            color: #67C23A;
          }
          
          &.good {
            color: #409EFF;
          }
          
          &.fair {
            color: #E6A23C;
          }
          
          &.poor {
            color: #F56C6C;
          }
        }
        
        .score-total {
          color: #909399;
          font-size: 16px;
        }
      }
    }
    
    .health-metrics {
      .metric-item {
        text-align: center;
        
        .metric-label {
          color: #606266;
          font-size: 14px;
          margin-bottom: 8px;
        }
        
        .metric-value {
          position: relative;
          
          .metric-score {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-size: 12px;
            font-weight: 600;
            color: #303133;
          }
        }
      }
    }
  }
}
</style>
