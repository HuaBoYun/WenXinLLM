<template>
  <div class="advanced-features-wrapper">
    <div class="advanced-features">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2>高级功能管理</h2>
        <p>提供企业级高级预算功能，包括滚动预算、公式追踪、催报管理、穿透查询、多币种管理等专业功能</p>
      </div>

    <!-- 功能统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card rolling-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.rollingBudgets }}</div>
            <div class="stat-label">滚动预算</div>
            <div class="stat-description">活跃的滚动预算计划</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>动态更新</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card formula-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.formulaTraces }}</div>
            <div class="stat-label">公式追踪</div>
            <div class="stat-description">公式追踪任务数</div>
            <div class="stat-trend">
              <i class="el-icon-share"></i>
              <span>智能分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-share"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card reminder-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.reminderTasks }}</div>
            <div class="stat-label">催报任务</div>
            <div class="stat-description">待处理催报任务</div>
            <div class="stat-trend">
              <i class="el-icon-bell"></i>
              <span>及时提醒</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bell"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card currency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.currencies }}</div>
            <div class="stat-label">支持币种</div>
            <div class="stat-description">系统支持的币种数</div>
            <div class="stat-trend">
              <i class="el-icon-money"></i>
              <span>多币种</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块导航 -->
    <el-row :gutter="20" class="feature-modules">
      <el-col :span="8" v-for="module in featureModules" :key="module.id">
        <el-card 
          class="feature-module-card" 
          shadow="hover" 
          @click.native="handleModuleClick(module)"
        >
          <div class="module-header">
            <div class="module-icon">
              <i :class="module.icon"></i>
            </div>
            <div class="module-info">
              <h3>{{ module.name }}</h3>
              <p>{{ module.description }}</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">任务数</span>
              <span class="stat-value">{{ module.taskCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">最后更新</span>
              <span class="stat-value">{{ module.lastUpdate }}</span>
            </div>
          </div>
          <div class="module-actions">
            <el-button type="primary" size="mini" @click.stop="handleQuickAccess(module)">
              快速访问
            </el-button>
            <el-button type="text" size="mini" @click.stop="handleViewDetails(module)">
              查看详情
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作面板 -->
    <el-card class="quick-actions-card" shadow="never">
      <div slot="header" class="card-header">
        <span>快速操作</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshQuickActions">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="action in quickActions" :key="action.id">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-description">{{ action.description }}</div>
            </div>
            <div class="action-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 最近活动 -->
    <el-card class="recent-activities-card" shadow="never">
      <div slot="header" class="card-header">
        <span>最近活动</span>
        <div class="header-tools">
          <el-button icon="el-icon-view" size="mini" @click="handleViewAllActivities">查看全部</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="getRecentActivities">刷新</el-button>
        </div>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.timestamp"
          :type="getActivityType(activity.type)"
        >
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-user">操作人：{{ activity.operator }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 系统通知 -->
    <el-card class="notifications-card" shadow="never">
      <div slot="header" class="card-header">
        <span>系统通知</span>
        <div class="header-tools">
          <el-badge :value="unreadNotifications" class="notification-badge">
            <el-button icon="el-icon-bell" size="mini" @click="handleViewNotifications">通知</el-button>
          </el-badge>
          <el-button icon="el-icon-setting" size="mini" @click="handleNotificationSettings">设置</el-button>
        </div>
      </div>
      <div class="notifications-list">
        <div 
          v-for="notification in notifications" 
          :key="notification.id"
          class="notification-item"
          :class="{ 'unread': !notification.isRead }"
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-icon">
            <i :class="getNotificationIcon(notification.type)"></i>
          </div>
          <div class="notification-content">
            <div class="notification-title">{{ notification.title }}</div>
            <div class="notification-message">{{ notification.message }}</div>
            <div class="notification-time">{{ notification.time }}</div>
          </div>
          <div class="notification-status" v-if="!notification.isRead">
            <el-badge is-dot />
          </div>
        </div>
      </div>
    </el-card>
  </div>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'AdvancedFeaturesHome',
  data() {
    return {
      // 功能统计
      featureStats: {
        rollingBudgets: 0,
        formulaTraces: 0,
        reminderTasks: 0,
        currencies: 0
      },
      
      // 功能模块
      featureModules: [
        {
          id: 1,
          name: '滚动预算',
          description: '动态滚动预算管理，支持多种滚动周期和预测模型',
          icon: 'el-icon-refresh',
          route: '/ncv65/advanced-features/rolling-budget',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 2,
          name: '公式追踪',
          description: '预算公式依赖关系追踪和计算路径分析',
          icon: 'el-icon-share',
          route: '/ncv65/advanced-features/formula-trace',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 3,
          name: '催报管理',
          description: '预算填报催报策略配置和执行管理',
          icon: 'el-icon-bell',
          route: '/ncv65/advanced-features/reminder-management',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 4,
          name: '穿透查询',
          description: '多维度数据穿透查询和钻取分析',
          icon: 'el-icon-search',
          route: '/ncv65/advanced-features/drill-through-query',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 5,
          name: '多币种管理',
          description: '多币种预算管理和汇率转换',
          icon: 'el-icon-money',
          route: '/ncv65/advanced-features/currency-management',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 6,
          name: '批量计算',
          description: '预算数据批量计算和公式执行',
          icon: 'el-icon-cpu',
          route: '/ncv65/advanced-features/batch-calculation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 7,
          name: '智能推荐',
          description: 'AI智能预算推荐和优化建议',
          icon: 'el-icon-magic-stick',
          route: '/ncv65/advanced-features/intelligent-recommendation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 8,
          name: '预算模拟',
          description: '预算场景模拟和敏感性分析',
          icon: 'el-icon-data-analysis',
          route: '/ncv65/advanced-features/budget-simulation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 9,
          name: '数据挖掘',
          description: '预算数据挖掘和模式识别',
          icon: 'el-icon-pie-chart',
          route: '/ncv65/advanced-features/data-mining',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 10,
          name: '预算优化',
          description: '预算方案优化和资源配置建议',
          icon: 'el-icon-s-opportunity',
          route: '/ncv65/advanced-features/budget-optimization',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 11,
          name: '风险评估',
          description: '预算风险识别和评估管理',
          icon: 'el-icon-warning',
          route: '/ncv65/advanced-features/risk-assessment',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 12,
          name: '协同编制',
          description: '多部门协同预算编制和审批',
          icon: 'el-icon-s-cooperation',
          route: '/ncv65/advanced-features/collaborative-budgeting',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 13,
          name: '版本对比',
          description: '预算版本对比分析和差异识别',
          icon: 'el-icon-document-copy',
          route: '/ncv65/advanced-features/version-comparison',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 14,
          name: '自动化流程',
          description: '预算流程自动化和智能调度',
          icon: 'el-icon-setting',
          route: '/ncv65/advanced-features/automation-workflow',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 15,
          name: '高级报表',
          description: '高级预算报表设计和生成',
          icon: 'el-icon-document',
          route: '/ncv65/advanced-features/advanced-reports',
          taskCount: 0,
          lastUpdate: '-'
        }
      ],
      
      // 快速操作
      quickActions: [
        {
          id: 1,
          title: '创建滚动预算',
          description: '快速创建新的滚动预算计划',
          icon: 'el-icon-plus',
          action: 'createRollingBudget'
        },
        {
          id: 2,
          title: '执行公式追踪',
          description: '对指定公式执行依赖关系追踪',
          icon: 'el-icon-share',
          action: 'executeFormulaTrace'
        },
        {
          id: 3,
          title: '发送催报通知',
          description: '向相关人员发送预算催报通知',
          icon: 'el-icon-message',
          action: 'sendReminder'
        },
        {
          id: 4,
          title: '汇率更新',
          description: '更新最新的汇率信息',
          icon: 'el-icon-refresh',
          action: 'updateExchangeRate'
        }
      ],
      
      // 最近活动
      recentActivities: [],
      
      // 系统通知
      notifications: [],
      unreadNotifications: 0
    }
  },
  
  created() {
    this.getFeatureStats()
    this.getRecentActivities()
    this.getNotifications()
  },

  mounted() {
  },

  methods: {
    // 获取功能统计
    async getFeatureStats() {
      try {
        const response = await advancedFeaturesApi.getFeatureStats()
        if (response && response.code === 1 && response.data) {
          this.featureStats = response.data
        }
      } catch (error) {
        console.error('获取功能统计失败：', error)
      }
    },

    // 获取最近活动
    async getRecentActivities() {
      try {
        const response = await advancedFeaturesApi.getRecentActivities()
        if (response && response.code === 1 && response.data) {
          this.recentActivities = response.data
        }
      } catch (error) {
        console.error('获取最近活动失败：', error)
      }
    },

    // 获取系统通知
    async getNotifications() {
      try {
        const response = await advancedFeaturesApi.getNotifications()
        if (response && response.code === 1 && response.data) {
          this.notifications = response.data
          this.unreadNotifications = this.notifications.filter(n => !n.isRead).length
        }
      } catch (error) {
        console.error('获取系统通知失败：', error)
      }
    },
    
    // 模块点击
    handleModuleClick(module) {
      this.$router.push(module.route)
    },
    
    // 快速访问
    handleQuickAccess(module) {
      this.$router.push(module.route)
    },
    
    // 查看详情
    handleViewDetails(module) {
      this.$router.push({
        path: module.route,
        query: { tab: 'details' }
      })
    },
    
    // 快速操作
    handleQuickAction(action) {
      switch (action.action) {
        case 'createRollingBudget':
          this.$router.push('/managementAccountant/ncv65/advancedFeatures/rollingBudget?action=create')
          break
        case 'executeFormulaTrace':
          this.$router.push('/managementAccountant/ncv65/advancedFeatures/formulaTrace?action=execute')
          break
        case 'sendReminder':
          this.$router.push('/managementAccountant/ncv65/advancedFeatures/reminderManagement?action=send')
          break
        case 'updateExchangeRate':
          this.$router.push('/managementAccountant/ncv65/advancedFeatures/currencyManagement?action=updateRate')
          break
      }
    },
    
    // 刷新快速操作
    refreshQuickActions() {
      this.getFeatureStats()
      this.$message.success('已刷新')
    },
    
    // 查看全部活动
    handleViewAllActivities() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/activities')
    },
    
    // 查看通知
    handleViewNotifications() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/notifications')
    },
    
    // 通知设置
    handleNotificationSettings() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/notificationSettings')
    },
    
    // 通知点击
    handleNotificationClick(notification) {
      if (!notification.isRead) {
        this.markNotificationAsRead(notification.id)
      }
      // 根据通知类型跳转到相应页面
      if (notification.link) {
        this.$router.push(notification.link)
      }
    },
    
    // 标记通知为已读
    async markNotificationAsRead(notificationId) {
      try {
        await advancedFeaturesApi.markNotificationAsRead(notificationId)
        const notification = this.notifications.find(n => n.id === notificationId)
        if (notification) {
          notification.isRead = true
          this.unreadNotifications--
        }
      } catch (error) {
        console.error('标记通知失败：', error)
      }
    },
    
    // 获取活动类型
    getActivityType(type) {
      const typeMap = {
        'create': 'primary',
        'update': 'warning',
        'delete': 'danger',
        'execute': 'success'
      }
      return typeMap[type] || 'primary'
    },
    
    // 获取通知图标
    getNotificationIcon(type) {
      const iconMap = {
        'info': 'el-icon-info',
        'warning': 'el-icon-warning',
        'error': 'el-icon-error',
        'success': 'el-icon-success'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style lang="scss" scoped>
// 外层包装器 - 确保页面内容可见
.advanced-features-wrapper {
  position: relative;
  z-index: 10 !important;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.advanced-features {
  padding: 20px;
  position: relative;
  z-index: 10;

  .page-header {
    margin-bottom: 20px;
    background: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
      line-height: 1.5;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      min-height: 120px;

      ::v-deep .el-card__body {
        padding: 20px;
      }

      &.rolling-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF) !important;
        color: white !important;
      }

      &.formula-card {
        background: linear-gradient(135deg, #67C23A, #85CE61) !important;
        color: white !important;
      }

      &.reminder-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77) !important;
        color: white !important;
      }

      &.currency-card {
        background: linear-gradient(135deg, #F56C6C, #F78989) !important;
        color: white !important;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 32px;
          font-weight: 700;
          margin-bottom: 8px;
          color: white;
        }

        .stat-label {
          font-size: 16px;
          opacity: 0.95;
          margin-bottom: 4px;
          font-weight: 500;
          color: white;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.85;
          margin-bottom: 8px;
          color: white;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
          color: white;

          i {
            margin-right: 4px;
          }
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
        z-index: 1;
        color: white;
      }
    }
  }

  .feature-modules {
    margin-bottom: 20px;

    .feature-module-card {
      margin-bottom: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      background: white !important;
      border: 1px solid #EBEEF5;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
        border-color: #409EFF;
      }

      .module-header {
        display: flex;
        align-items: flex-start;
        margin-bottom: 16px;

        .module-icon {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #409EFF, #66B1FF);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          flex-shrink: 0;

          i {
            font-size: 24px;
            color: white;
          }
        }

        .module-info {
          flex: 1;

          h3 {
            font-size: 16px;
            color: #303133;
            margin: 0 0 8px 0;
            font-weight: 600;
          }

          p {
            font-size: 13px;
            color: #909399;
            margin: 0;
            line-height: 1.5;
          }
        }
      }

      .module-stats {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
        padding: 12px 0;
        border-top: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;

        .stat-item {
          text-align: center;
          flex: 1;

          .stat-label {
            display: block;
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .stat-value {
            display: block;
            font-size: 18px;
            font-weight: 600;
            color: #409EFF;
          }
        }
      }

      .module-actions {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 8px;
      }
    }
  }

  .quick-actions-card,
  .recent-activities-card,
  .notifications-card {
    margin-bottom: 20px;
    background: white !important;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
      font-size: 16px;

      .header-tools {
        display: flex;
        gap: 8px;
        align-items: center;

        .notification-badge {
          margin-right: 8px;
        }
      }
    }
  }

  .quick-actions-card {
    .quick-action-item {
      display: flex;
      align-items: center;
      padding: 16px;
      border: 1px solid #EBEEF5;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .action-icon {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
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

      .action-content {
        flex: 1;

        .action-title {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .action-description {
          font-size: 12px;
          color: #606266;
        }
      }

      .action-arrow {
        color: #C0C4CC;
        font-size: 14px;
      }
    }
  }

  .recent-activities-card {
    .card-header {
      .header-tools {
        display: flex;
        gap: 8px;
      }
    }

    .activity-content {
      .activity-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
      }

      .activity-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 4px;
      }

      .activity-user {
        font-size: 11px;
        color: #909399;
      }
    }
  }

  .notifications-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        gap: 8px;
        align-items: center;

        .notification-badge {
          margin-right: 8px;
        }
      }
    }

    .notifications-list {
      .notification-item {
        display: flex;
        align-items: flex-start;
        padding: 12px;
        border-bottom: 1px solid #F5F7FA;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: #F5F7FA;
        }

        &.unread {
          background: #F0F8FF;
          border-left: 3px solid #409EFF;
        }

        .notification-icon {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          background: #F5F7FA;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;

          i {
            font-size: 16px;
            color: #409EFF;
          }
        }

        .notification-content {
          flex: 1;

          .notification-title {
            font-size: 14px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }

          .notification-message {
            font-size: 12px;
            color: #606266;
            margin-bottom: 4px;
            line-height: 1.4;
          }

          .notification-time {
            font-size: 11px;
            color: #909399;
          }
        }

        .notification-status {
          margin-left: 8px;
        }
      }
    }
  }
}
</style>
