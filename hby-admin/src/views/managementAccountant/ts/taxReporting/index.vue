<template>
  <div class="tax-reporting-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>税务申报</h2>
      <p>税务申报管理与执行</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon report">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.reportCount || 0 }}</div>
              <div class="stat-label">申报总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.pendingCount || 0 }}</div>
              <div class="stat-label">待申报</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon submitted">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.submittedCount || 0 }}</div>
              <div class="stat-label">已申报</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.overdueCount || 0 }}</div>
              <div class="stat-label">逾期申报</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导航区域 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToReportingList">
            <div class="nav-icon report">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>申报管理</h3>
              <p>税务申报创建与管理</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToReportingSchedule">
            <div class="nav-icon schedule">
              <i class="el-icon-date"></i>
            </div>
            <div class="nav-content">
              <h3>申报计划</h3>
              <p>申报时间计划与提醒</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToReportingHistory">
            <div class="nav-icon history">
              <i class="el-icon-folder-opened"></i>
            </div>
            <div class="nav-content">
              <h3>申报历史</h3>
              <p>历史申报记录查询</p>
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
        <el-button type="primary" icon="el-icon-plus" @click="quickCreateReporting">创建申报</el-button>
        <el-button type="success" icon="el-icon-view" @click="quickViewPending">查看待办</el-button>
        <el-button type="info" icon="el-icon-date" @click="quickViewSchedule">查看计划</el-button>
        <el-button type="warning" icon="el-icon-warning" @click="quickViewOverdue">查看逾期</el-button>
        <el-button type="danger" icon="el-icon-download" @click="quickExportData">导出数据</el-button>
        <el-button icon="el-icon-setting" @click="quickSettings">申报设置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxReportingIndex',
  data() {
    return {
      overviewData: {
        reportCount: 234,
        pendingCount: 18,
        submittedCount: 210,
        overdueCount: 6
      },
      recentActivities: [
        {
          id: 1,
          content: '提交2024年1月增值税申报',
          timestamp: '2024-01-15 17:30',
          color: '#67C23A'
        },
        {
          id: 2,
          content: '企业所得税申报逾期提醒',
          timestamp: '2024-01-15 15:20',
          color: '#F56C6C'
        },
        {
          id: 3,
          content: '完成个人所得税代扣代缴申报',
          timestamp: '2024-01-14 14:45',
          color: '#409EFF'
        },
        {
          id: 4,
          content: '更新税务申报模板',
          timestamp: '2024-01-14 11:15',
          color: '#E6A23C'
        }
      ]
    }
  },
  methods: {
    navigateToReportingList() {
      this.$router.push('/ts/tax-reporting/list')
    },
    navigateToReportingSchedule() {
      this.$router.push('/ts/tax-reporting/schedule')
    },
    navigateToReportingHistory() {
      this.$router.push('/ts/tax-reporting/history')
    },
    quickCreateReporting() {
      this.$router.push('/ts/tax-reporting/list?action=create')
    },
    quickViewPending() {
      this.$router.push('/ts/tax-reporting/list?filter=pending')
    },
    quickViewSchedule() {
      this.$router.push('/ts/tax-reporting/schedule')
    },
    quickViewOverdue() {
      this.$router.push('/ts/tax-reporting/list?filter=overdue')
    },
    quickExportData() {
      this.$message.success('数据导出功能开发中...')
    },
    quickSettings() {
      this.$router.push('/ts/tax-reporting/settings')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-reporting-container {
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
  
  &.report {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.pending {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.submitted {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.overdue {
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
  
  &.report {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.schedule {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.history {
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
