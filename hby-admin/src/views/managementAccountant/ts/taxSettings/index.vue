<template>
  <div class="tax-settings-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>税务设置</h2>
      <p>税务系统配置与参数设置</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon config">
              <i class="el-icon-setting"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.configCount || 0 }}</div>
              <div class="stat-label">配置项</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon template">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.templateCount || 0 }}</div>
              <div class="stat-label">模板数量</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon rule">
              <i class="el-icon-tickets"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.ruleCount || 0 }}</div>
              <div class="stat-label">业务规则</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon user">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.userCount || 0 }}</div>
              <div class="stat-label">用户权限</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导航区域 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToSystemConfig">
            <div class="nav-icon config">
              <i class="el-icon-setting"></i>
            </div>
            <div class="nav-content">
              <h3>系统配置</h3>
              <p>税务系统基础配置管理</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTemplateManage">
            <div class="nav-icon template">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="nav-content">
              <h3>模板管理</h3>
              <p>税务申报模板配置</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToRuleManage">
            <div class="nav-icon rule">
              <i class="el-icon-tickets"></i>
            </div>
            <div class="nav-content">
              <h3>规则管理</h3>
              <p>税务业务规则配置</p>
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
        <el-button type="primary" icon="el-icon-plus" @click="quickCreateConfig">创建配置</el-button>
        <el-button type="success" icon="el-icon-view" @click="quickViewTemplates">查看模板</el-button>
        <el-button type="info" icon="el-icon-tickets" @click="quickManageRules">管理规则</el-button>
        <el-button type="warning" icon="el-icon-user" @click="quickManageUsers">用户权限</el-button>
        <el-button type="danger" icon="el-icon-download" @click="quickExportConfig">导出配置</el-button>
        <el-button icon="el-icon-refresh" @click="quickReloadConfig">重载配置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaxSettingsIndex',
  data() {
    return {
      overviewData: {
        configCount: 67,
        templateCount: 23,
        ruleCount: 45,
        userCount: 156
      },
      recentActivities: [
        {
          id: 1,
          content: '更新增值税申报模板配置',
          timestamp: '2024-01-15 19:30',
          color: '#409EFF'
        },
        {
          id: 2,
          content: '新增税务风险评估规则',
          timestamp: '2024-01-15 17:20',
          color: '#67C23A'
        },
        {
          id: 3,
          content: '修改用户权限配置',
          timestamp: '2024-01-14 16:45',
          color: '#E6A23C'
        },
        {
          id: 4,
          content: '备份系统配置数据',
          timestamp: '2024-01-14 14:15',
          color: '#909399'
        }
      ]
    }
  },
  methods: {
    navigateToSystemConfig() {
      this.$router.push('/ts/tax-settings/config')
    },
    navigateToTemplateManage() {
      this.$router.push('/ts/tax-settings/template')
    },
    navigateToRuleManage() {
      this.$router.push('/ts/tax-settings/rule')
    },
    quickCreateConfig() {
      this.$router.push('/ts/tax-settings/config?action=create')
    },
    quickViewTemplates() {
      this.$router.push('/ts/tax-settings/template')
    },
    quickManageRules() {
      this.$router.push('/ts/tax-settings/rule')
    },
    quickManageUsers() {
      this.$router.push('/ts/tax-settings/user')
    },
    quickExportConfig() {
      this.$message.success('配置导出功能开发中...')
    },
    quickReloadConfig() {
      this.$message.success('配置重载功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-settings-container {
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
  
  &.config {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.template {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.rule {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }
  
  &.user {
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
  
  &.config {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }
  
  &.template {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }
  
  &.rule {
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
