<template>
  <div class="tax-analytics-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>税务分析</h2>
      <p>税务数据分析与洞察</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon analytics">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.analyticsCount || 0 }}</div>
              <div class="stat-label">分析报告</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon trend">
              <i class="el-icon-trend-charts"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.trendCount || 0 }}</div>
              <div class="stat-label">趋势分析</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon insight">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.insightCount || 0 }}</div>
              <div class="stat-label">洞察发现</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon alert">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.alertCount || 0 }}</div>
              <div class="stat-label">异常预警</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导航区域 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAnalyticsList">
            <div class="nav-icon analytics">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>分析报告管理</h3>
              <p>税务分析报告生成与管理</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTrendAnalysis">
            <div class="nav-icon trend">
              <i class="el-icon-trend-charts"></i>
            </div>
            <div class="nav-content">
              <h3>趋势分析</h3>
              <p>税务数据趋势分析与预测</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToInsightDashboard">
            <div class="nav-icon insight">
              <i class="el-icon-view"></i>
            </div>
            <div class="nav-content">
              <h3>洞察仪表板</h3>
              <p>税务洞察与智能分析</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <h3>最近活动</h3>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.timestamp"
          :color="activity.color"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access-section">
      <h3>快速访问</h3>
      <div class="quick-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="quickCreateAnalytics">创建分析</el-button>
        <el-button type="success" icon="el-icon-view" @click="quickViewTrends">查看趋势</el-button>
        <el-button type="info" icon="el-icon-data-analysis" @click="quickGenerateReport">生成报告</el-button>
        <el-button type="warning" icon="el-icon-warning" @click="quickViewAlerts">查看预警</el-button>
        <el-button type="danger" icon="el-icon-download" @click="quickExportData">导出数据</el-button>
        <el-button icon="el-icon-setting" @click="quickSettings">分析设置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxAnalyticsIndex',
  data() {
    return {
      overviewData: {
        analyticsCount: 156,
        trendCount: 89,
        insightCount: 234,
        alertCount: 12
      },
      recentActivities: [
        {
          id: 1,
          content: '生成了2024年第一季度税务分析报告',
          timestamp: '2024-01-15 14:30',
          color: '#409EFF'
        },
        {
          id: 2,
          content: '发现增值税异常趋势预警',
          timestamp: '2024-01-15 10:20',
          color: '#F56C6C'
        },
        {
          id: 3,
          content: '完成企业所得税洞察分析',
          timestamp: '2024-01-14 16:45',
          color: '#67C23A'
        },
        {
          id: 4,
          content: '更新税务分析模型参数',
          timestamp: '2024-01-14 09:15',
          color: '#E6A23C'
        }
      ]
    }
  },
  methods: {
    navigateToAnalyticsList() {
      this.$router.push('/ts/tax-analytics/list')
    },
    navigateToTrendAnalysis() {
      this.$router.push('/ts/tax-analytics/trend')
    },
    navigateToInsightDashboard() {
      this.$router.push('/ts/tax-analytics/insight')
    },
    quickCreateAnalytics() {
      this.$router.push('/ts/tax-analytics/list?action=create')
    },
    quickViewTrends() {
      this.$router.push('/ts/tax-analytics/trend')
    },
    quickGenerateReport() {
      this.$router.push('/ts/tax-analytics/report')
    },
    quickViewAlerts() {
      this.$router.push('/ts/tax-analytics/alert')
    },
    quickExportData() {
      this.$message.success('数据导出功能开发中...')
    },
    quickSettings() {
      this.$router.push('/ts/tax-analytics/settings')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-analytics-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
  
  h2 {
    color: #303133;
    margin-bottom: 10px;
  }
  
  p {
    color: #909399;
    font-size: 14px;
  }
}

.overview-section {
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  
  i {
    font-size: 24px;
    color: white;
  }
  
  &.analytics {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.trend {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.insight {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.alert {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.navigation-section {
  margin-bottom: 30px;
}

.nav-card {
  background: white;
  border-radius: 8px;
  padding: 25px;
  display: flex;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }
}

.nav-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  
  i {
    font-size: 20px;
    color: white;
  }
  
  &.analytics {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.trend {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.insight {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
}

.nav-content {
  flex: 1;
  
  h3 {
    color: #303133;
    margin-bottom: 8px;
    font-size: 16px;
  }
  
  p {
    color: #909399;
    font-size: 13px;
    margin: 0;
  }
}

.activity-section, .quick-access-section {
  background: white;
  border-radius: 8px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h3 {
    color: #303133;
    margin-bottom: 20px;
    font-size: 16px;
  }
}

.quick-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
