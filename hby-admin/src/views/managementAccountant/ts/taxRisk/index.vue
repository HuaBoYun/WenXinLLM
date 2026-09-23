<template>
  <div class="tax-risk-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>税务风险</h2>
      <p>税务风险识别与管控</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon risk">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.riskCount || 0 }}</div>
              <div class="stat-label">风险总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon high">
              <i class="el-icon-close"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.highRiskCount || 0 }}</div>
              <div class="stat-label">高风险</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon medium">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.mediumRiskCount || 0 }}</div>
              <div class="stat-label">中风险</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon low">
              <i class="el-icon-info"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.lowRiskCount || 0 }}</div>
              <div class="stat-label">低风险</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导航区域 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToRiskList">
            <div class="nav-icon risk">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="nav-content">
              <h3>风险管理</h3>
              <p>税务风险识别与处理</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToRiskAssessment">
            <div class="nav-icon assessment">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>风险评估</h3>
              <p>税务风险评估与分析</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToRiskMonitor">
            <div class="nav-icon monitor">
              <i class="el-icon-view"></i>
            </div>
            <div class="nav-content">
              <h3>风险监控</h3>
              <p>实时风险监控与预警</p>
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
        <el-button type="primary" icon="el-icon-plus" @click="quickCreateRisk">创建风险</el-button>
        <el-button type="success" icon="el-icon-view" @click="quickViewHighRisk">查看高风险</el-button>
        <el-button type="info" icon="el-icon-data-analysis" @click="quickAssessment">风险评估</el-button>
        <el-button type="warning" icon="el-icon-warning" @click="quickViewAlerts">查看预警</el-button>
        <el-button type="danger" icon="el-icon-download" @click="quickExportData">导出数据</el-button>
        <el-button icon="el-icon-setting" @click="quickSettings">风险设置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxRiskIndex',
  data() {
    return {
      overviewData: {
        riskCount: 89,
        highRiskCount: 12,
        mediumRiskCount: 34,
        lowRiskCount: 43
      },
      recentActivities: [
        {
          id: 1,
          content: '发现ABC公司增值税申报异常风险',
          timestamp: '2024-01-15 18:30',
          color: '#F56C6C'
        },
        {
          id: 2,
          content: '完成XYZ企业税务风险评估',
          timestamp: '2024-01-15 16:20',
          color: '#67C23A'
        },
        {
          id: 3,
          content: '触发DEF公司高风险预警',
          timestamp: '2024-01-14 15:45',
          color: '#E6A23C'
        },
        {
          id: 4,
          content: '更新税务风险评估模型',
          timestamp: '2024-01-14 12:15',
          color: '#409EFF'
        }
      ]
    }
  },
  methods: {
    navigateToRiskList() {
      this.$router.push('/ts/tax-risk/list')
    },
    navigateToRiskAssessment() {
      this.$router.push('/ts/tax-risk/assessment')
    },
    navigateToRiskMonitor() {
      this.$router.push('/ts/tax-risk/monitor')
    },
    quickCreateRisk() {
      this.$router.push('/ts/tax-risk/list?action=create')
    },
    quickViewHighRisk() {
      this.$router.push('/ts/tax-risk/list?filter=high')
    },
    quickAssessment() {
      this.$router.push('/ts/tax-risk/assessment')
    },
    quickViewAlerts() {
      this.$router.push('/ts/tax-risk/list?filter=alerts')
    },
    quickExportData() {
      this.$message.success('数据导出功能开发中...')
    },
    quickSettings() {
      this.$router.push('/ts/tax-risk/settings')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-risk-container {
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
  
  &.risk {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.high {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.medium {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.low {
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
  
  &.risk {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.assessment {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.monitor {
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
