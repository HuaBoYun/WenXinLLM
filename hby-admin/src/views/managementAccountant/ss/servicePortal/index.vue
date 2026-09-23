<template>
  <div class="service-portal-index">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-s-grid"></i>
            服务门户管理
          </h1>
          <p class="page-description">统一管理企业服务门户，提供个性化门户服务</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreatePortal">
            新建门户
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 快速操作区域 -->
    <el-card class="quick-actions-card" shadow="never">
      <div slot="header" class="card-header">
        <span>快速操作</span>
      </div>
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="quick-action-item" @click="navigateToList">
            <div class="action-icon list">
              <i class="el-icon-menu"></i>
            </div>
            <div class="action-content">
              <div class="action-title">门户列表</div>
              <div class="action-desc">查看和管理所有门户</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="quick-action-item" @click="navigateToDashboard">
            <div class="action-icon dashboard">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="action-content">
              <div class="action-title">数据仪表板</div>
              <div class="action-desc">查看门户统计分析</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="quick-action-item" @click="handleBatchManage">
            <div class="action-icon batch">
              <i class="el-icon-setting"></i>
            </div>
            <div class="action-content">
              <div class="action-title">批量管理</div>
              <div class="action-desc">批量操作门户状态</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="quick-action-item" @click="handleSystemConfig">
            <div class="action-icon config">
              <i class="el-icon-tools"></i>
            </div>
            <div class="action-content">
              <div class="action-title">系统配置</div>
              <div class="action-desc">配置门户系统参数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 概览统计 -->
    <el-row :gutter="24" class="overview-stats">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.totalCount || 0 }}</div>
              <div class="stat-label">门户总数</div>
              <div class="stat-change positive">
                <i class="el-icon-top"></i>
                +{{ overviewStats.totalGrowth || 0 }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overviewStats.activeCount || 0 }}</div>
              <div class="stat-label">活跃门户</div>
              <div class="stat-change positive">
                <i class="el-icon-top"></i>
                +{{ overviewStats.activeGrowth || 0 }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon access">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatAccessCount(overviewStats.totalAccess) }}</div>
              <div class="stat-label">总访问量</div>
              <div class="stat-change positive">
                <i class="el-icon-top"></i>
                +{{ formatAccessCount(overviewStats.accessGrowth) }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon users">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatAccessCount(overviewStats.totalUsers) }}</div>
              <div class="stat-label">总用户数</div>
              <div class="stat-change positive">
                <i class="el-icon-top"></i>
                +{{ formatAccessCount(overviewStats.usersGrowth) }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <el-row :gutter="24" class="recent-activities">
      <el-col :span="12">
        <el-card class="activity-card">
          <div slot="header" class="card-header">
            <span>最近活动</span>
            <el-button type="text" @click="viewAllActivities">查看全部</el-button>
          </div>
          <div class="activity-list">
            <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
              <div class="activity-icon" :class="activity.type">
                <i :class="activity.icon"></i>
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-desc">{{ activity.description }}</div>
                <div class="activity-time">{{ formatRelativeTime(activity.time) }}</div>
              </div>
            </div>
            <div v-if="recentActivities.length === 0" class="no-activities">
              <i class="el-icon-info"></i>
              <span>暂无最近活动</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="quick-links-card">
          <div slot="header" class="card-header">
            <span>快速链接</span>
          </div>
          <div class="quick-links">
            <div class="link-group">
              <div class="group-title">门户管理</div>
              <div class="link-list">
                <el-link type="primary" @click="navigateToList">门户列表</el-link>
                <el-link type="primary" @click="handleCreatePortal">新建门户</el-link>
                <el-link type="primary" @click="navigateToDashboard">数据分析</el-link>
              </div>
            </div>
            <div class="link-group">
              <div class="group-title">系统管理</div>
              <div class="link-list">
                <el-link type="primary" @click="handleSystemConfig">系统配置</el-link>
                <el-link type="primary" @click="handleUserManage">用户管理</el-link>
                <el-link type="primary" @click="handlePermissionManage">权限管理</el-link>
              </div>
            </div>
            <div class="link-group">
              <div class="group-title">监控运维</div>
              <div class="link-list">
                <el-link type="primary" @click="handleHealthCheck">健康检查</el-link>
                <el-link type="primary" @click="handlePerformanceMonitor">性能监控</el-link>
                <el-link type="primary" @click="handleLogAnalysis">日志分析</el-link>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 门户详情对话框 -->
    <ServicePortalDetail
      :visible.sync="detailVisible"
      :portal-id="currentPortalId"
      :mode="detailMode"
      @refresh="handleRefresh"
    />
  </div>
</template>

<script>
import {
  getStatistics,
  getPortalAccessRanking,
  utils
} from '@/api/managementAccountant/ss/servicePortal'
import ServicePortalDetail from './ServicePortalDetail'

export default {
  name: 'ServicePortalIndex',
  components: {
    ServicePortalDetail
  },
  data() {
    return {
      loading: false,
      overviewStats: {
        totalCount: 0,
        activeCount: 0,
        totalAccess: 0,
        totalUsers: 0,
        totalGrowth: 0,
        activeGrowth: 0,
        accessGrowth: 0,
        usersGrowth: 0
      },
      recentActivities: [
        {
          id: 1,
          type: 'create',
          icon: 'el-icon-plus',
          title: '新建门户',
          description: '创建了员工门户"HR自助服务"',
          time: new Date(Date.now() - 2 * 60 * 60 * 1000) // 2小时前
        },
        {
          id: 2,
          type: 'update',
          icon: 'el-icon-edit',
          title: '更新配置',
          description: '更新了客户门户"在线服务中心"的主题配置',
          time: new Date(Date.now() - 4 * 60 * 60 * 1000) // 4小时前
        },
        {
          id: 3,
          type: 'activate',
          icon: 'el-icon-check',
          title: '激活门户',
          description: '激活了合作伙伴门户"供应商平台"',
          time: new Date(Date.now() - 6 * 60 * 60 * 1000) // 6小时前
        },
        {
          id: 4,
          type: 'maintenance',
          icon: 'el-icon-setting',
          title: '维护完成',
          description: '管理门户"系统控制台"维护完成',
          time: new Date(Date.now() - 8 * 60 * 60 * 1000) // 8小时前
        }
      ],
      detailVisible: false,
      currentPortalId: null,
      detailMode: 'create'
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  mounted() {
    this.loadOverviewData()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        const endTime = new Date()
        const startTime = new Date(endTime.getTime() - 30 * 24 * 60 * 60 * 1000) // 30天前
        
        const response = await getStatistics(
          startTime.toISOString(),
          endTime.toISOString(),
          this.tenantId
        )
        
        if (response.success) {
          this.overviewStats = {
            ...this.overviewStats,
            ...response.data,
            // 模拟增长数据
            totalGrowth: Math.floor(Math.random() * 10) + 1,
            activeGrowth: Math.floor(Math.random() * 5) + 1,
            accessGrowth: Math.floor(Math.random() * 1000) + 100,
            usersGrowth: Math.floor(Math.random() * 50) + 10
          }
        }
      } catch (error) {
        console.warn('加载概览数据失败:', error)
      }
    },

    // 刷新数据
    handleRefresh() {
      this.loadOverviewData()
      this.$message.success('数据已刷新')
    },

    // 创建门户
    handleCreatePortal() {
      this.currentPortalId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 导航到列表页
    navigateToList() {
      this.$router.push({ name: 'ServicePortalList' })
    },

    // 导航到仪表板
    navigateToDashboard() {
      this.$router.push({ name: 'ServicePortalDashboard' })
    },

    // 批量管理
    handleBatchManage() {
      this.$router.push({ 
        name: 'ServicePortalList',
        query: { tab: 'batch' }
      })
    },

    // 系统配置
    handleSystemConfig() {
      this.$message.info('系统配置功能开发中...')
    },

    // 用户管理
    handleUserManage() {
      this.$message.info('用户管理功能开发中...')
    },

    // 权限管理
    handlePermissionManage() {
      this.$message.info('权限管理功能开发中...')
    },

    // 健康检查
    handleHealthCheck() {
      this.$message.info('健康检查功能开发中...')
    },

    // 性能监控
    handlePerformanceMonitor() {
      this.$message.info('性能监控功能开发中...')
    },

    // 日志分析
    handleLogAnalysis() {
      this.$message.info('日志分析功能开发中...')
    },

    // 查看所有活动
    viewAllActivities() {
      this.$message.info('活动日志功能开发中...')
    },

    // 格式化访问量
    formatAccessCount(count) {
      return utils.formatAccessCount(count || 0)
    },

    // 格式化相对时间
    formatRelativeTime(time) {
      return this.$moment(time).fromNow()
    }
  }
}
</script>

<style lang="scss" scoped>
.service-portal-index {
  .page-header {
    margin-bottom: 24px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .header-left {
        .page-title {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin: 0 0 8px 0;
          
          i {
            margin-right: 8px;
            color: #409eff;
          }
        }
        
        .page-description {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  .quick-actions-card {
    margin-bottom: 24px;
    
    .quick-action-item {
      display: flex;
      align-items: center;
      padding: 16px;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: #f5f7fa;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
      
      .action-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        
        i {
          font-size: 20px;
          color: white;
        }
        
        &.list {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.dashboard {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.batch {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.config {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .action-content {
        .action-title {
          font-size: 16px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .action-desc {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .overview-stats {
    margin-bottom: 24px;
    
    .stat-card {
      .stat-content {
        display: flex;
        align-items: center;
        
        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          i {
            font-size: 24px;
            color: white;
          }
          
          &.total {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          
          &.active {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.access {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          
          &.users {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }
        
        .stat-info {
          flex: 1;
          
          .stat-value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
            margin-bottom: 4px;
          }
          
          .stat-label {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }
          
          .stat-change {
            font-size: 12px;
            
            &.positive {
              color: #67c23a;
            }
            
            &.negative {
              color: #f56c6c;
            }
            
            i {
              margin-right: 2px;
            }
          }
        }
      }
    }
  }

  .recent-activities {
    .activity-card {
      .activity-list {
        .activity-item {
          display: flex;
          align-items: flex-start;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          
          &:last-child {
            border-bottom: none;
          }
          
          .activity-icon {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 12px;
            flex-shrink: 0;
            
            i {
              font-size: 14px;
              color: white;
            }
            
            &.create {
              background: #67c23a;
            }
            
            &.update {
              background: #409eff;
            }
            
            &.activate {
              background: #e6a23c;
            }
            
            &.maintenance {
              background: #909399;
            }
          }
          
          .activity-content {
            flex: 1;
            
            .activity-title {
              font-size: 14px;
              font-weight: bold;
              color: #303133;
              margin-bottom: 4px;
            }
            
            .activity-desc {
              font-size: 13px;
              color: #606266;
              margin-bottom: 4px;
            }
            
            .activity-time {
              font-size: 12px;
              color: #909399;
            }
          }
        }
        
        .no-activities {
          text-align: center;
          padding: 40px 0;
          color: #909399;
          
          i {
            font-size: 48px;
            margin-bottom: 8px;
            display: block;
          }
        }
      }
    }
    
    .quick-links-card {
      .quick-links {
        .link-group {
          margin-bottom: 24px;
          
          &:last-child {
            margin-bottom: 0;
          }
          
          .group-title {
            font-size: 14px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 12px;
            padding-bottom: 8px;
            border-bottom: 1px solid #f0f0f0;
          }
          
          .link-list {
            display: flex;
            flex-direction: column;
            gap: 8px;
            
            .el-link {
              justify-content: flex-start;
            }
          }
        }
      }
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
