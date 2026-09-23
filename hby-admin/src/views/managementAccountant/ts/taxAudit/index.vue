<template>
  <div class="tax-audit-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>税务审计</h2>
      <p>税务审计管理与执行</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon audit">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.auditCount || 0 }}</div>
              <div class="stat-label">审计项目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon progress">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.progressCount || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon issue">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.issueCount || 0 }}</div>
              <div class="stat-label">发现问题</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导航区域 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAuditList">
            <div class="nav-icon audit">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="nav-content">
              <h3>审计项目管理</h3>
              <p>税务审计项目创建与管理</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAuditPlan">
            <div class="nav-icon plan">
              <i class="el-icon-date"></i>
            </div>
            <div class="nav-content">
              <h3>审计计划</h3>
              <p>审计计划制定与执行</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAuditReport">
            <div class="nav-icon report">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>审计报告</h3>
              <p>审计报告生成与管理</p>
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
        <el-button type="primary" icon="el-icon-plus" @click="quickCreateAudit">创建审计</el-button>
        <el-button type="success" icon="el-icon-view" @click="quickViewProgress">查看进度</el-button>
        <el-button type="info" icon="el-icon-document" @click="quickGenerateReport">生成报告</el-button>
        <el-button type="warning" icon="el-icon-warning" @click="quickViewIssues">查看问题</el-button>
        <el-button type="danger" icon="el-icon-download" @click="quickExportData">导出数据</el-button>
        <el-button icon="el-icon-setting" @click="quickSettings">审计设置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxAuditIndex',
  data() {
    return {
      overviewData: {
        auditCount: 45,
        progressCount: 12,
        completedCount: 28,
        issueCount: 5
      },
      recentActivities: [
        {
          id: 1,
          content: '完成ABC公司增值税审计项目',
          timestamp: '2024-01-15 16:30',
          color: '#67C23A'
        },
        {
          id: 2,
          content: '发现XYZ企业所得税申报异常',
          timestamp: '2024-01-15 14:20',
          color: '#F56C6C'
        },
        {
          id: 3,
          content: '启动DEF公司年度税务审计',
          timestamp: '2024-01-14 10:45',
          color: '#409EFF'
        },
        {
          id: 4,
          content: '提交GHI公司审计报告',
          timestamp: '2024-01-14 08:15',
          color: '#E6A23C'
        }
      ]
    }
  },
  methods: {
    navigateToAuditList() {
      this.$router.push('/ts/tax-audit/list')
    },
    navigateToAuditPlan() {
      this.$router.push('/ts/tax-audit/plan')
    },
    navigateToAuditReport() {
      this.$router.push('/ts/tax-audit/report')
    },
    quickCreateAudit() {
      this.$router.push('/ts/tax-audit/list?action=create')
    },
    quickViewProgress() {
      this.$router.push('/ts/tax-audit/list?filter=progress')
    },
    quickGenerateReport() {
      this.$router.push('/ts/tax-audit/report')
    },
    quickViewIssues() {
      this.$router.push('/ts/tax-audit/list?filter=issues')
    },
    quickExportData() {
      this.$message.success('数据导出功能开发中...')
    },
    quickSettings() {
      this.$router.push('/ts/tax-audit/settings')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-audit-container {
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
  
  &.audit {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.progress {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.completed {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.issue {
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
  
  &.audit {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.plan {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.report {
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
