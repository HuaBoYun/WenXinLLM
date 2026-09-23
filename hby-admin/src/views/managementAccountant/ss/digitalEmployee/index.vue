<template>
  <div class="digital-employee-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1>数字员工管理</h1>
        <p>智能化数字员工全生命周期管理平台</p>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="handleAdd" icon="el-icon-plus">新增数字员工</el-button>
        <el-button @click="handleImport" icon="el-icon-upload2">批量导入</el-button>
        <el-button @click="handleExport" icon="el-icon-download">导出数据</el-button>
      </div>
    </div>

    <!-- 快捷操作卡片 -->
    <div class="quick-actions">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="action-card" @click="goToList">
            <div class="action-icon list">
              <i class="el-icon-menu"></i>
            </div>
            <div class="action-content">
              <h3>数字员工列表</h3>
              <p>查看和管理所有数字员工</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="goToDashboard">
            <div class="action-icon dashboard">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="action-content">
              <h3>数据仪表板</h3>
              <p>查看数字员工统计分析</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="goToMonitor">
            <div class="action-icon monitor">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="action-content">
              <h3>实时监控</h3>
              <p>监控数字员工运行状态</p>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="action-card" @click="goToSettings">
            <div class="action-icon settings">
              <i class="el-icon-setting"></i>
            </div>
            <div class="action-content">
              <h3>系统设置</h3>
              <p>配置数字员工系统参数</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 概览统计 -->
    <div class="overview-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.totalCount || 0 }}</div>
              <div class="stat-label">数字员工总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon active">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.activeCount || 0 }}</div>
              <div class="stat-label">活跃数字员工</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon execution">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.todayExecutions || 0 }}</div>
              <div class="stat-label">今日执行次数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon success">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.avgSuccessRate || 0 }}%</div>
              <div class="stat-label">平均成功率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activities">
      <div class="section-header">
        <h2>最近活动</h2>
        <el-button @click="refreshActivities" icon="el-icon-refresh" size="mini" circle></el-button>
      </div>
      <div class="activities-content">
        <div v-if="recentActivities.length === 0" class="no-data">
          <i class="el-icon-info"></i>
          <p>暂无最近活动</p>
        </div>
        <div v-else class="activities-list">
          <div 
            v-for="activity in recentActivities" 
            :key="activity.id"
            class="activity-item"
          >
            <div class="activity-icon" :class="getActivityIconClass(activity.type)">
              <i :class="getActivityIcon(activity.type)"></i>
            </div>
            <div class="activity-content">
              <div class="activity-title">{{ activity.title }}</div>
              <div class="activity-description">{{ activity.description }}</div>
              <div class="activity-time">{{ formatDateTime(activity.createTime) }}</div>
            </div>
            <div class="activity-actions">
              <el-button @click="viewActivity(activity)" size="mini" type="text">查看</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速链接 -->
    <div class="quick-links">
      <div class="section-header">
        <h2>快速链接</h2>
      </div>
      <div class="links-content">
        <el-row :gutter="15">
          <el-col :span="4">
            <div class="link-item" @click="goToCreate">
              <i class="el-icon-plus"></i>
              <span>创建数字员工</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="link-item" @click="goToBatchManage">
              <i class="el-icon-s-operation"></i>
              <span>批量管理</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="link-item" @click="goToHealthCheck">
              <i class="el-icon-first-aid-kit"></i>
              <span>健康检查</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="link-item" @click="goToPerformance">
              <i class="el-icon-data-line"></i>
              <span>性能分析</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="link-item" @click="goToDeployment">
              <i class="el-icon-upload"></i>
              <span>部署管理</span>
            </div>
          </el-col>
          <el-col :span="4">
            <div class="link-item" @click="goToHelp">
              <i class="el-icon-question"></i>
              <span>帮助文档</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { getStatistics } from '@/api/managementAccountant/ss/digitalEmployee'

export default {
  name: 'DigitalEmployeeIndex',
  data() {
    return {
      loading: false,
      // 概览统计
      overviewStats: {
        totalCount: 0,
        activeCount: 0,
        inactiveCount: 0,
        todayExecutions: 0,
        avgSuccessRate: 0
      },
      // 最近活动
      recentActivities: []
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  created() {
    this.loadOverviewStats()
    this.loadRecentActivities()
  },
  methods: {
    // 加载概览统计
    async loadOverviewStats() {
      try {
        const endTime = new Date()
        const startTime = new Date()
        startTime.setHours(0, 0, 0, 0) // 今天开始时间

        const response = await getStatistics(
          startTime.toISOString(),
          endTime.toISOString(),
          this.tenantId
        )
        if (response.success) {
          this.overviewStats = response.data || {}
        }
      } catch (error) {
        console.error('加载概览统计失败:', error)
      }
    },

    // 加载最近活动
    loadRecentActivities() {
      // 模拟最近活动数据
      this.recentActivities = [
        {
          id: 1,
          type: 'CREATE',
          title: '创建数字员工',
          description: '创建了新的RPA数字员工"财务报表处理机器人"',
          createTime: new Date(Date.now() - 1000 * 60 * 30) // 30分钟前
        },
        {
          id: 2,
          type: 'ACTIVATE',
          title: '激活数字员工',
          description: '激活了数字员工"客户服务机器人"',
          createTime: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2小时前
        },
        {
          id: 3,
          type: 'HEALTH_CHECK',
          title: '健康检查',
          description: '对10个数字员工执行了健康检查',
          createTime: new Date(Date.now() - 1000 * 60 * 60 * 4) // 4小时前
        },
        {
          id: 4,
          type: 'DEPLOY',
          title: '部署数字员工',
          description: '将"数据录入机器人"部署到生产环境',
          createTime: new Date(Date.now() - 1000 * 60 * 60 * 8) // 8小时前
        }
      ]
    },

    // 刷新活动
    refreshActivities() {
      this.loadRecentActivities()
      this.$message.success('活动列表已刷新')
    },

    // 导航方法
    goToList() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/list')
    },

    goToDashboard() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/dashboard')
    },

    goToMonitor() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/monitor')
    },

    goToSettings() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/settings')
    },

    goToCreate() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/create')
    },

    goToBatchManage() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/batch')
    },

    goToHealthCheck() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/health')
    },

    goToPerformance() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/performance')
    },

    goToDeployment() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/deployment')
    },

    goToHelp() {
      this.$router.push('/managementAccountant/ss/digitalEmployee/help')
    },

    // 操作方法
    handleAdd() {
      this.goToCreate()
    },

    handleImport() {
      this.$message.info('批量导入功能开发中...')
    },

    handleExport() {
      this.$message.info('导出数据功能开发中...')
    },

    // 查看活动详情
    viewActivity(activity) {
      this.$message.info('查看活动详情功能开发中...')
    },

    // 获取活动图标类
    getActivityIconClass(type) {
      const classMap = {
        'CREATE': 'create',
        'ACTIVATE': 'activate',
        'DEACTIVATE': 'deactivate',
        'HEALTH_CHECK': 'health',
        'DEPLOY': 'deploy',
        'MAINTENANCE': 'maintenance'
      }
      return classMap[type] || 'default'
    },

    // 获取活动图标
    getActivityIcon(type) {
      const iconMap = {
        'CREATE': 'el-icon-plus',
        'ACTIVATE': 'el-icon-video-play',
        'DEACTIVATE': 'el-icon-video-pause',
        'HEALTH_CHECK': 'el-icon-first-aid-kit',
        'DEPLOY': 'el-icon-upload',
        'MAINTENANCE': 'el-icon-setting'
      }
      return iconMap[type] || 'el-icon-info'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const now = new Date()
      const time = new Date(dateTime)
      const diff = now - time

      if (diff < 1000 * 60) {
        return '刚刚'
      } else if (diff < 1000 * 60 * 60) {
        return Math.floor(diff / (1000 * 60)) + '分钟前'
      } else if (diff < 1000 * 60 * 60 * 24) {
        return Math.floor(diff / (1000 * 60 * 60)) + '小时前'
      } else {
        return time.toLocaleDateString()
      }
    }
  }
}
</script>

<style scoped>
.digital-employee-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.header-left h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
  font-weight: 600;
}

.header-left p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.header-right {
  display: flex;
  gap: 10px;
}

.quick-actions {
  margin-bottom: 30px;
}

.action-card {
  display: flex;
  align-items: center;
  padding: 25px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.action-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: #fff;
}

.action-icon.list {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-icon.dashboard {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.action-icon.monitor {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.action-icon.settings {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.action-content h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #303133;
}

.action-content p {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.overview-stats {
  margin-bottom: 30px;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 20px;
  color: #fff;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.execution {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.success {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.recent-activities,
.quick-links {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 1px solid #f0f0f0;
}

.section-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.activities-content,
.links-content {
  padding: 25px;
}

.no-data {
  text-align: center;
  padding: 50px 0;
  color: #909399;
}

.no-data i {
  font-size: 48px;
  margin-bottom: 15px;
  display: block;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 16px;
  color: #fff;
}

.activity-icon.create {
  background: #67c23a;
}

.activity-icon.activate {
  background: #409eff;
}

.activity-icon.deactivate {
  background: #e6a23c;
}

.activity-icon.health {
  background: #f56c6c;
}

.activity-icon.deploy {
  background: #909399;
}

.activity-icon.maintenance {
  background: #606266;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.activity-description {
  color: #606266;
  font-size: 14px;
  margin-bottom: 5px;
}

.activity-time {
  color: #909399;
  font-size: 12px;
}

.activity-actions {
  margin-left: 15px;
}

.link-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.link-item:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.link-item i {
  font-size: 24px;
  color: #409eff;
  margin-bottom: 10px;
}

.link-item span {
  font-size: 14px;
  color: #303133;
}
</style>
