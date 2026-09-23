<template>
  <div class="target-management-dashboard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>目标管理仪表板</h2>
        <p>目标执行情况总览和分析</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateTarget">
          新建目标
        </el-button>
        <el-button icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon total">
                <i class="el-icon-aim"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.totalTargets || 0 }}</div>
                <div class="stats-label">总目标数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon completed">
                <i class="el-icon-check"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.completedTargets || 0 }}</div>
                <div class="stats-label">已完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon progress">
                <i class="el-icon-loading"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.inProgressTargets || 0 }}</div>
                <div class="stats-label">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon rate">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.completionRate || 0 }}%</div>
                <div class="stats-label">完成率</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表和列表区域 -->
    <div class="content-section">
      <el-row :gutter="20">
        <!-- 左侧图表区域 -->
        <el-col :span="16">
          <el-card class="chart-card">
            <div slot="header">
              <span>目标完成趋势</span>
              <el-button-group style="float: right;">
                <el-button size="mini" :type="chartPeriod === 'WEEK' ? 'primary' : ''" @click="changeChartPeriod('WEEK')">周</el-button>
                <el-button size="mini" :type="chartPeriod === 'MONTH' ? 'primary' : ''" @click="changeChartPeriod('MONTH')">月</el-button>
                <el-button size="mini" :type="chartPeriod === 'QUARTER' ? 'primary' : ''" @click="changeChartPeriod('QUARTER')">季</el-button>
              </el-button-group>
            </div>
            <div class="chart-container">
              <div id="targetTrendChart" style="width: 100%; height: 300px;"></div>
            </div>
          </el-card>

          <el-card class="chart-card" style="margin-top: 20px;">
            <div slot="header">
              <span>目标分类分布</span>
            </div>
            <div class="chart-container">
              <div id="targetCategoryChart" style="width: 100%; height: 300px;"></div>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧列表区域 -->
        <el-col :span="8">
          <el-card class="list-card">
            <div slot="header">
              <span>重点关注目标</span>
              <el-link type="primary" style="float: right;" @click="viewAllTargets">查看全部</el-link>
            </div>
            <div class="target-list">
              <div
                v-for="target in focusTargets"
                :key="target.targetId"
                class="target-item"
                @click="viewTargetDetail(target)"
              >
                <div class="target-info">
                  <div class="target-name">{{ target.targetName }}</div>
                  <div class="target-meta">
                    <el-tag :type="getTargetTypeTag(target.targetType)" size="mini">
                      {{ formatTargetType(target.targetType) }}
                    </el-tag>
                    <span class="target-owner">{{ target.targetOwnerName }}</span>
                  </div>
                </div>
                <div class="target-progress">
                  <el-progress
                    :percentage="target.completionRate || 0"
                    :stroke-width="6"
                    :color="getProgressColor(target.completionRate)"
                  />
                  <div class="progress-text">{{ target.completionRate || 0 }}%</div>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="list-card" style="margin-top: 20px;">
            <div slot="header">
              <span>风险预警</span>
            </div>
            <div class="alert-list">
              <div
                v-for="alert in riskAlerts"
                :key="alert.id"
                class="alert-item"
                :class="alert.level"
              >
                <div class="alert-icon">
                  <i :class="getAlertIcon(alert.level)"></i>
                </div>
                <div class="alert-content">
                  <div class="alert-title">{{ alert.title }}</div>
                  <div class="alert-desc">{{ alert.description }}</div>
                  <div class="alert-time">{{ formatTime(alert.createTime) }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
  getTargetDashboard,
  getTargetStatistics,
  queryTargetPage
} from '@/api/managementAccountant/pm/targetManagement'

export default {
  name: 'TargetManagementDashboard',
  data() {
    return {
      loading: false,
      chartPeriod: 'MONTH',
      dashboardData: {
        totalTargets: 0,
        completedTargets: 0,
        inProgressTargets: 0,
        completionRate: 0
      },
      focusTargets: [],
      riskAlerts: []
    }
  },
  created() {
    this.loadDashboardData()
    this.loadFocusTargets()
    this.loadRiskAlerts()
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    // 加载仪表板数据
    async loadDashboardData() {
      try {
        const response = await getTargetDashboard({
          dashboardType: 'OVERVIEW'
        })
        if (response.success) {
          this.dashboardData = response.data
        }
      } catch (error) {
        console.error('加载仪表板数据失败:', error)
      }
    },

    // 加载重点关注目标
    async loadFocusTargets() {
      try {
        const response = await queryTargetPage({
          current: 1,
          size: 5,
          priority: 'HIGH',
          targetStatus: 'ACTIVE'
        })
        if (response.success) {
          this.focusTargets = response.data.records
        }
      } catch (error) {
        console.error('加载重点目标失败:', error)
      }
    },

    // 加载风险预警
    async loadRiskAlerts() {
      // 模拟风险预警数据
      this.riskAlerts = [
        {
          id: 1,
          level: 'high',
          title: '销售目标完成率偏低',
          description: 'Q3销售目标完成率仅为45%，需要关注',
          createTime: new Date()
        },
        {
          id: 2,
          level: 'medium',
          title: '项目进度滞后',
          description: '产品开发项目进度落后于计划15天',
          createTime: new Date()
        },
        {
          id: 3,
          level: 'low',
          title: '团队目标调整',
          description: '市场部团队目标需要根据市场变化调整',
          createTime: new Date()
        }
      ]
    },

    // 初始化图表
    initCharts() {
      this.initTrendChart()
      this.initCategoryChart()
    },

    // 初始化趋势图表
    initTrendChart() {
      // 这里应该使用ECharts或其他图表库
      // 模拟图表初始化
      console.log('初始化趋势图表')
    },

    // 初始化分类图表
    initCategoryChart() {
      // 这里应该使用ECharts或其他图表库
      // 模拟图表初始化
      console.log('初始化分类图表')
    },

    // 改变图表周期
    changeChartPeriod(period) {
      this.chartPeriod = period
      this.initTrendChart()
    },

    // 刷新数据
    refreshData() {
      this.loadDashboardData()
      this.loadFocusTargets()
      this.loadRiskAlerts()
      this.$message.success('数据已刷新')
    },

    // 新建目标
    handleCreateTarget() {
      this.$router.push('/pm/target-management/create')
    },

    // 查看所有目标
    viewAllTargets() {
      this.$router.push('/pm/target-management')
    },

    // 查看目标详情
    viewTargetDetail(target) {
      this.$router.push(`/pm/target-management/detail/${target.targetId}`)
    },

    // 格式化目标类型
    formatTargetType(type) {
      const typeMap = {
        'STRATEGIC': '战略',
        'OPERATIONAL': '运营',
        'PERSONAL': '个人',
        'TEAM': '团队'
      }
      return typeMap[type] || type
    },

    // 获取目标类型标签
    getTargetTypeTag(type) {
      const tagMap = {
        'STRATEGIC': 'danger',
        'OPERATIONAL': 'primary',
        'PERSONAL': 'success',
        'TEAM': 'warning'
      }
      return tagMap[type] || ''
    },

    // 获取进度颜色
    getProgressColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 70) return '#e6a23c'
      if (rate >= 50) return '#409eff'
      return '#f56c6c'
    },

    // 获取预警图标
    getAlertIcon(level) {
      const iconMap = {
        'high': 'el-icon-warning',
        'medium': 'el-icon-info',
        'low': 'el-icon-question'
      }
      return iconMap[level] || 'el-icon-info'
    },

    // 格式化时间
    formatTime(time) {
      return this.$moment(time).format('MM-DD HH:mm')
    }
  }
}
</script>

<style scoped>
.target-management-dashboard {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-section {
  margin-bottom: 20px;
}

.stats-card {
  height: 100px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
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

.stats-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.completed {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.progress {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.rate {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.content-section {
  margin-top: 20px;
}

.chart-card,
.list-card {
  height: 380px;
}

.chart-container {
  height: 300px;
}

.target-list {
  max-height: 320px;
  overflow-y: auto;
}

.target-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.target-item:hover {
  background-color: #f5f7fa;
}

.target-item:last-child {
  border-bottom: none;
}

.target-info {
  flex: 1;
  margin-right: 15px;
}

.target-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.target-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.target-owner {
  font-size: 12px;
  color: #909399;
}

.target-progress {
  width: 100px;
  text-align: center;
}

.progress-text {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
}

.alert-list {
  max-height: 320px;
  overflow-y: auto;
}

.alert-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.alert-item:last-child {
  border-bottom: none;
}

.alert-icon {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  font-size: 14px;
  color: white;
}

.alert-item.high .alert-icon {
  background-color: #f56c6c;
}

.alert-item.medium .alert-icon {
  background-color: #e6a23c;
}

.alert-item.low .alert-icon {
  background-color: #409eff;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.alert-desc {
  font-size: 12px;
  color: #606266;
  margin-bottom: 5px;
}

.alert-time {
  font-size: 11px;
  color: #c0c4cc;
}
</style>
