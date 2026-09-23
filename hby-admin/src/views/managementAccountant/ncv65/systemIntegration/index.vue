<template>
  <div class="system-integration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>系统集成管理</h2>
      <p>企业级系统集成平台，支持ERP、BI、第三方系统的深度集成和数据同步</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateIntegration">创建集成</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestConnection">连接测试</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleIntegrationCenter">集成中心</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">集成设置</el-button>
            <el-button icon="el-icon-document" @click="handleDocuments">集成文档</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 集成统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.totalIntegrations }}</div>
            <div class="stat-label">集成总数</div>
            <div class="stat-description">已配置集成数量</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>系统集成</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.activeIntegrations }}</div>
            <div class="stat-label">活跃集成</div>
            <div class="stat-description">正在运行的集成</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>运行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card sync-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.todaySync }}</div>
            <div class="stat-label">今日同步</div>
            <div class="stat-description">今日数据同步次数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>数据同步</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ integrationStats.successRate }}%</div>
            <div class="stat-label">成功率</div>
            <div class="stat-description">集成成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高成功率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
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
          <el-card 
            class="quick-action-item" 
            shadow="hover" 
            @click.native="handleQuickAction(action)"
          >
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-title">{{ action.title }}</div>
            <div class="action-description">{{ action.description }}</div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 系统集成功能模块 -->
    <el-card class="integration-modules-card" shadow="never">
      <div slot="header" class="card-header">
        <span>集成功能模块</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索功能模块"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="refreshModules">刷新</el-button>
        </div>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8" v-for="module in filteredModules" :key="module.id">
          <el-card class="module-card" shadow="hover" @click.native="handleModuleClick(module)">
            <div class="module-header">
              <div class="module-icon">
                <i :class="module.icon"></i>
              </div>
              <div class="module-info">
                <h3>{{ module.name }}</h3>
                <p>{{ module.description }}</p>
              </div>
              <div class="module-status">
                <el-tag :type="getModuleStatusColor(module.status)" size="mini">
                  {{ getModuleStatusText(module.status) }}
                </el-tag>
              </div>
            </div>
            <div class="module-stats">
              <div class="stat-item">
                <span class="stat-label">集成数量</span>
                <span class="stat-value">{{ module.integrationCount }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">最近同步</span>
                <span class="stat-value">{{ module.lastSync }}</span>
              </div>
            </div>
            <div class="module-actions">
              <el-button type="text" size="mini" @click.stop="handleConfigModule(module)">
                配置
              </el-button>
              <el-button type="text" size="mini" @click.stop="handleViewModule(module)">
                查看
              </el-button>
              <el-button type="text" size="mini" @click.stop="handleTestModule(module)">
                测试
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 最近活动时间线 -->
    <el-card class="recent-activities-card" shadow="never">
      <div slot="header" class="card-header">
        <span>最近活动</span>
        <el-button icon="el-icon-refresh" size="mini" @click="getRecentActivities">刷新</el-button>
      </div>
      
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.timestamp"
          :type="getActivityType(activity.type)"
        >
          <el-card>
            <h4>{{ activity.title }}</h4>
            <p>{{ activity.description }}</p>
            <div class="activity-meta">
              <el-tag :type="getActivityStatusColor(activity.status)" size="mini">
                {{ activity.status }}
              </el-tag>
              <span class="activity-user">{{ activity.user }}</span>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 系统通知 -->
    <el-card class="system-notifications-card" shadow="never">
      <div slot="header" class="card-header">
        <span>系统通知</span>
        <el-badge :value="unreadNotifications" class="notification-badge">
          <el-button icon="el-icon-bell" size="mini" @click="handleViewAllNotifications">
            查看全部
          </el-button>
        </el-badge>
      </div>
      
      <div class="notification-list">
        <div v-for="notification in systemNotifications" :key="notification.id" class="notification-item">
          <div class="notification-icon">
            <i :class="getNotificationIcon(notification.type)"></i>
          </div>
          <div class="notification-content">
            <div class="notification-title">{{ notification.title }}</div>
            <div class="notification-message">{{ notification.message }}</div>
            <div class="notification-time">{{ notification.time }}</div>
          </div>
          <div class="notification-actions">
            <el-button type="text" size="mini" @click="handleMarkAsRead(notification)">
              标记已读
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 模块弹窗 -->
    <el-dialog
      :title="currentModuleTitle"
      :visible.sync="moduleDialogVisible"
      width="90%"
      top="3vh"
      :close-on-click-modal="false"
      :destroy-on-close="true"
      custom-class="module-dialog"
      @closed="closeModuleDialog"
    >
      <div v-loading="componentLoading" style="min-height: 200px;">
        <component
          v-if="currentComponent"
          :is="currentComponent"
          @navigate="handleChildNavigate"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'SystemIntegration',
  data() {
    return {
      // 统计数据
      integrationStats: {
        totalIntegrations: 0,
        activeIntegrations: 0,
        todaySync: 0,
        successRate: 0
      },

      // 快速操作 - 从API加载
      quickActions: [],

      // 功能模块 - 从API加载
      integrationModules: [],

      // 最近活动
      recentActivities: [],

      // 系统通知
      systemNotifications: [],

      searchKeyword: '',

      // 弹窗相关
      moduleDialogVisible: false,
      currentModuleTitle: '',
      currentComponent: null,
      componentLoading: false
    }
  },

  computed: {
    filteredModules() {
      if (!this.searchKeyword) {
        return this.integrationModules
      }
      return this.integrationModules.filter(module =>
        module.name.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        module.description.toLowerCase().includes(this.searchKeyword.toLowerCase())
      )
    },

    unreadNotifications() {
      return this.systemNotifications.filter(n => !n.read).length
    }
  },
  
  created() {
    this.getIntegrationStats()
    this.getRecentActivities()
    this.getSystemNotifications()
    this.getQuickActions()
    this.getIntegrationModules()
  },

  methods: {
    // 组件名映射表
    getComponentMap() {
      return {
        erpIntegration: () => import('./ErpIntegration.vue'),
        biIntegration: () => import('./BiIntegration.vue'),
        apiIntegration: () => import('./ApiIntegration.vue'),
        databaseIntegration: () => import('./DatabaseIntegration.vue'),
        fileIntegration: () => import('./FileIntegration.vue'),
        messageQueueIntegration: () => import('./MessageQueueIntegration.vue'),
        webServiceIntegration: () => import('./WebServiceIntegration.vue'),
        cloudIntegration: () => import('./CloudIntegration.vue'),
        dataStreamIntegration: () => import('./DataStreamIntegration.vue'),
        integrationMonitor: () => import('./IntegrationMonitor.vue'),
        dataMapping: () => import('./DataMapping.vue'),
        integrationConfig: () => import('./IntegrationConfig.vue')
      }
    },

    // 打开模块弹窗
    async openModuleDialog(title, componentKey) {
      const componentMap = this.getComponentMap()
      const loader = componentMap[componentKey]
      if (!loader) {
        this.$message.error('未找到对应的功能模块')
        return
      }
      this.currentModuleTitle = title
      this.moduleDialogVisible = true
      this.componentLoading = true
      try {
        const comp = await loader()
        this.currentComponent = comp.default || comp
      } catch (error) {
        this.$message.error('加载模块失败：' + error.message)
        this.moduleDialogVisible = false
      } finally {
        this.componentLoading = false
      }
    },

    // 弹窗关闭回调
    closeModuleDialog() {
      this.currentComponent = null
      this.currentModuleTitle = ''
    },

    // 子组件导航事件处理（子组件通过 $emit('navigate', key) 触发）
    handleChildNavigate(componentKey) {
      const titleMap = {
        integrationConfig: '集成配置',
        integrationMonitor: '集成监控',
        erpIntegration: 'ERP系统集成',
        biIntegration: 'BI系统集成',
        apiIntegration: 'API接口集成',
        databaseIntegration: '数据库集成',
        fileIntegration: '文件集成',
        messageQueueIntegration: '消息队列集成',
        webServiceIntegration: 'Web服务集成',
        cloudIntegration: '云平台集成',
        dataStreamIntegration: '数据流集成',
        dataMapping: '数据映射'
      }
      const title = titleMap[componentKey] || componentKey
      this.openModuleDialog(title, componentKey)
    },

    // 从route路径提取组件key
    extractComponentKey(route) {
      if (!route) return null
      const parts = route.split('/')
      return parts[parts.length - 1] || null
    },

    async getIntegrationStats() {
      try {
        const response = await systemIntegrationApi.getIntegrationStats()
        if (response.code === 1 && response.data) { this.integrationStats = response.data }
      } catch (error) { this.$message.error('获取统计数据失败') }
    },
    async getRecentActivities() {
      try {
        const response = await systemIntegrationApi.getRecentActivities()
        if (response.code === 1 && response.data) { this.recentActivities = response.data }
      } catch (error) { this.$message.error('获取最近活动失败') }
    },
    async getSystemNotifications() {
      try {
        const response = await systemIntegrationApi.getSystemNotifications()
        if (response.code === 1 && response.data) { this.systemNotifications = response.data }
      } catch (error) { this.$message.error('获取系统通知失败') }
    },
    async getQuickActions() {
      try {
        const response = await systemIntegrationApi.getQuickActions()
        if (response.code === 1 && response.data) { this.quickActions = response.data }
      } catch (error) { this.$message.error('获取快速操作失败') }
    },
    async getIntegrationModules() {
      try {
        const response = await systemIntegrationApi.getIntegrationModules()
        if (response.code === 1 && response.data) { this.integrationModules = response.data }
      } catch (error) { this.$message.error('获取集成模块失败') }
    },
    
    // 创建集成
    handleCreateIntegration() {
      this.openModuleDialog('创建集成', 'integrationConfig')
    },

    // 刷新
    handleRefresh() {
      this.getIntegrationStats()
      this.getRecentActivities()
      this.getSystemNotifications()
      this.getQuickActions()
      this.getIntegrationModules()
      this.$message.success('数据已刷新')
    },

    // 连接测试
    handleTestConnection() {
      this.openModuleDialog('连接测试', 'integrationMonitor')
    },

    // 集成中心
    handleIntegrationCenter() {
      this.openModuleDialog('集成中心', 'integrationMonitor')
    },

    // 集成设置
    handleSettings() {
      this.openModuleDialog('集成设置', 'integrationConfig')
    },

    // 集成文档
    handleDocuments() {
      this.$message.info('集成文档功能开发中')
    },

    // 帮助
    handleHelp() {
      this.$message.info('帮助文档功能开发中')
    },

    // 快速操作
    handleQuickAction(action) {
      const actionMap = {
        1: { title: action.title, key: action.componentKey || 'erpIntegration' },
        2: { title: action.title, key: action.componentKey || 'biIntegration' },
        3: { title: action.title, key: action.componentKey || 'integrationMonitor' },
        4: { title: action.title, key: action.componentKey || 'integrationMonitor' }
      }
      const actionItem = actionMap[action.id]
      if (actionItem) {
        this.openModuleDialog(actionItem.title, actionItem.key)
      }
    },

    // 刷新快速操作
    async refreshQuickActions() {
      await this.getQuickActions()
      this.$message.success('快速操作已刷新')
    },

    // 刷新模块
    async refreshModules() {
      await this.getIntegrationModules()
      this.$message.success('功能模块已刷新')
    },

    // 模块点击
    handleModuleClick(module) {
      const componentKey = this.extractComponentKey(module.route)
      if (componentKey) {
        this.openModuleDialog(module.name, componentKey)
      } else {
        this.$message.error('模块未配置对应页面')
      }
    },

    // 配置模块
    handleConfigModule(module) {
      const componentKey = this.extractComponentKey(module.route)
      if (componentKey) {
        this.openModuleDialog(module.name + ' - 配置', componentKey)
      } else {
        this.$message.error('模块未配置对应页面')
      }
    },

    // 查看模块
    handleViewModule(module) {
      this.handleModuleClick(module)
    },

    // 测试模块
    async handleTestModule(module) {
      try {
        const response = await systemIntegrationApi.testModuleConnection(module.id)
        if (response.code === 1) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error(response.msg || '连接测试失败')
        }
      } catch (error) {
        this.$message.error('连接测试失败：' + error.message)
      }
    },

    // 查看所有通知
    handleViewAllNotifications() {
      this.openModuleDialog('系统通知', 'integrationMonitor')
    },
    
    // 标记已读
    async handleMarkAsRead(notification) {
      try {
        const response = await systemIntegrationApi.markNotificationAsRead(notification.id)
        if (response.code === 1) {
          notification.read = true
          this.$message.success('已标记为已读')
        } else {
          this.$message.error(response.msg || '标记失败')
        }
      } catch (error) {
        this.$message.error('标记失败：' + error.message)
      }
    },
    
    // 获取模块状态颜色
    getModuleStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'ERROR': 'danger',
        'PENDING': 'info'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取模块状态文本
    getModuleStatusText(status) {
      const textMap = {
        'ACTIVE': '运行中',
        'INACTIVE': '已停止',
        'ERROR': '错误',
        'PENDING': '待启动'
      }
      return textMap[status] || status
    },
    
    // 获取活动类型
    getActivityType(type) {
      const typeMap = {
        'success': 'success',
        'warning': 'warning',
        'error': 'danger',
        'info': 'primary'
      }
      return typeMap[type] || 'primary'
    },
    
    // 获取活动状态颜色
    getActivityStatusColor(status) {
      const colorMap = {
        '成功': 'success',
        '完成': 'primary',
        '重试中': 'warning',
        '失败': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取通知图标
    getNotificationIcon(type) {
      const iconMap = {
        'warning': 'el-icon-warning',
        'success': 'el-icon-success',
        'error': 'el-icon-error',
        'info': 'el-icon-info'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style lang="scss" scoped>
.system-integration {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    color: #303133;
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
  }

  p {
    color: #606266;
    margin: 0;
    font-size: 14px;
  }
}

.toolbar-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  &.total-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }

  &.active-card {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: white;
  }

  &.sync-card {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: white;
  }

  &.success-rate-card {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    color: white;
  }

  .stat-content {
    position: relative;
    z-index: 2;

    .stat-number {
      font-size: 28px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      margin-bottom: 5px;
      opacity: 0.9;
    }

    .stat-description {
      font-size: 12px;
      opacity: 0.8;
      margin-bottom: 10px;
    }

    .stat-trend {
      display: flex;
      align-items: center;
      font-size: 12px;
      opacity: 0.9;

      i {
        margin-right: 4px;
      }
    }
  }

  .stat-icon {
    position: absolute;
    top: 20px;
    right: 20px;
    font-size: 40px;
    opacity: 0.3;
    z-index: 1;
  }
}

.quick-actions-card,
.integration-modules-card,
.recent-activities-card,
.system-notifications-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-tools {
    display: flex;
    align-items: center;
  }

  .notification-badge {
    margin-left: 10px;
  }
}

.quick-action-item {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    border-color: #409EFF;
  }

  .action-icon {
    font-size: 32px;
    color: #409EFF;
    margin-bottom: 10px;
  }

  .action-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 8px;
  }

  .action-description {
    font-size: 12px;
    color: #909399;
  }
}

.module-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    border-color: #409EFF;
  }

  .module-header {
    display: flex;
    align-items: flex-start;
    margin-bottom: 15px;

    .module-icon {
      font-size: 32px;
      color: #409EFF;
      margin-right: 15px;
      margin-top: 5px;
    }

    .module-info {
      flex: 1;

      h3 {
        margin: 0 0 5px 0;
        font-size: 16px;
        color: #303133;
      }

      p {
        margin: 0;
        font-size: 12px;
        color: #909399;
        line-height: 1.4;
      }
    }

    .module-status {
      margin-left: 10px;
    }
  }

  .module-stats {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
    padding: 10px;
    background-color: #f8f9fa;
    border-radius: 4px;

    .stat-item {
      text-align: center;

      .stat-label {
        display: block;
        font-size: 12px;
        color: #909399;
        margin-bottom: 2px;
      }

      .stat-value {
        display: block;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }
    }
  }

  .module-actions {
    text-align: right;
    border-top: 1px solid #ebeef5;
    padding-top: 10px;
  }
}

.notification-list {
  .notification-item {
    display: flex;
    align-items: flex-start;
    padding: 15px 0;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }

    .notification-icon {
      font-size: 20px;
      margin-right: 15px;
      margin-top: 2px;

      &.warning {
        color: #E6A23C;
      }

      &.success {
        color: #67C23A;
      }

      &.error {
        color: #F56C6C;
      }

      &.info {
        color: #409EFF;
      }
    }

    .notification-content {
      flex: 1;

      .notification-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 5px;
      }

      .notification-message {
        font-size: 12px;
        color: #606266;
        margin-bottom: 5px;
        line-height: 1.4;
      }

      .notification-time {
        font-size: 11px;
        color: #909399;
      }
    }

    .notification-actions {
      margin-left: 15px;
    }
  }
}

.activity-meta {
  display: flex;
  align-items: center;
  margin-top: 10px;

  .activity-user {
    margin-left: 10px;
    font-size: 12px;
    color: #909399;
  }
}

.text-right {
  text-align: right;
}

.test-result {
  margin-top: 16px;
}
</style>

<style lang="scss">
/* 弹窗相关样式 - 不加scoped，因为el-dialog渲染在body下 */
.module-dialog {
  .el-dialog__body {
    padding: 10px 20px;
    max-height: 80vh;
    overflow-y: auto;
  }
}
</style>
