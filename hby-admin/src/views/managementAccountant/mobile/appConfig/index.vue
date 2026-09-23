<template>
  <div class="app-config-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-mobile-phone"></i>
            移动应用配置管理
          </h1>
          <p class="page-description">
            管理移动应用的配置信息，包括应用类型、平台、版本、权限等设置
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
            创建应用
          </el-button>
          <el-button icon="el-icon-refresh" @click="loadOverviewData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <div class="overview-cards">
        <div class="stat-card">
          <div class="stat-icon total">
            <i class="el-icon-mobile-phone"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.totalApps || 0 }}</div>
            <div class="stat-label">应用总数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon published">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.publishedApps || 0 }}</div>
            <div class="stat-label">已发布应用</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon enabled">
            <i class="el-icon-success"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.enabledApps || 0 }}</div>
            <div class="stat-label">启用应用</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon draft">
            <i class="el-icon-edit-outline"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.draftApps || 0 }}</div>
            <div class="stat-label">草稿应用</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <div class="nav-cards">
        <div class="nav-card" @click="navigateToList">
          <div class="nav-icon">
            <i class="el-icon-menu"></i>
          </div>
          <div class="nav-content">
            <h3>应用列表管理</h3>
            <p>查看和管理所有移动应用配置</p>
          </div>
          <div class="nav-arrow">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
        <div class="nav-card" @click="navigateToDashboard">
          <div class="nav-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
          <div class="nav-content">
            <h3>数据分析仪表板</h3>
            <p>查看应用使用统计和性能分析</p>
          </div>
          <div class="nav-arrow">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
        <div class="nav-card" @click="navigateToTest">
          <div class="nav-icon">
            <i class="el-icon-cpu"></i>
          </div>
          <div class="nav-content">
            <h3>应用测试工具</h3>
            <p>测试应用配置和功能</p>
          </div>
          <div class="nav-arrow">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <div class="section-header">
        <h2>最近活动</h2>
        <el-button type="text" @click="loadRecentActivities">刷新</el-button>
      </div>
      <div class="activity-timeline">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-time">{{ formatTime(activity.time) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access-section">
      <div class="section-header">
        <h2>快速访问</h2>
      </div>
      <div class="quick-items">
        <div class="quick-item" @click="quickCreateNativeApp">
          <i class="el-icon-mobile-phone"></i>
          <span>创建原生应用</span>
        </div>
        <div class="quick-item" @click="quickImportApps">
          <i class="el-icon-upload"></i>
          <span>导入应用配置</span>
        </div>
        <div class="quick-item" @click="quickExportApps">
          <i class="el-icon-download"></i>
          <span>导出应用配置</span>
        </div>
        <div class="quick-item" @click="quickTestApps">
          <i class="el-icon-cpu"></i>
          <span>批量测试应用</span>
        </div>
        <div class="quick-item" @click="quickViewDocs">
          <i class="el-icon-document"></i>
          <span>查看文档</span>
        </div>
        <div class="quick-item" @click="quickViewStats">
          <i class="el-icon-data-line"></i>
          <span>查看统计</span>
        </div>
      </div>
    </div>

    <!-- 创建应用对话框 -->
    <el-dialog
      title="创建移动应用"
      :visible.sync="createDialogVisible"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="100px"
      >
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="createForm.appName" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="应用类型" prop="appType">
          <el-select v-model="createForm.appType" placeholder="请选择应用类型" style="width: 100%">
            <el-option label="原生应用" value="NATIVE" />
            <el-option label="混合应用" value="HYBRID" />
            <el-option label="Web应用" value="WEB" />
            <el-option label="渐进式Web应用" value="PWA" />
          </el-select>
        </el-form-item>
        <el-form-item label="应用平台" prop="appPlatform">
          <el-select v-model="createForm.appPlatform" placeholder="请选择应用平台" style="width: 100%">
            <el-option label="安卓" value="ANDROID" />
            <el-option label="苹果" value="IOS" />
            <el-option label="Windows" value="WINDOWS" />
            <el-option label="全平台" value="ALL" />
          </el-select>
        </el-form-item>
        <el-form-item label="应用分类" prop="appCategory">
          <el-select v-model="createForm.appCategory" placeholder="请选择应用分类" style="width: 100%">
            <el-option label="业务应用" value="BUSINESS" />
            <el-option label="工具应用" value="TOOL" />
            <el-option label="游戏应用" value="GAME" />
            <el-option label="教育应用" value="EDUCATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="应用描述" prop="appDescription">
          <el-input
            v-model="createForm.appDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入应用描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateApp" :loading="createLoading">
          创建
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAppConfigSystemOverview,
  createAppConfig,
  quickCreateNativeApp
} from '@/api/managementAccountant/mobile/appConfig'

export default {
  name: 'MobileAppConfigIndex',
  data() {
    return {
      // 概览数据
      overviewData: {},
      // 最近活动
      recentActivities: [
        {
          id: 1,
          type: 'create',
          title: '创建了新应用',
          description: '创建了移动办公应用 v1.0.0',
          time: new Date(Date.now() - 1000 * 60 * 30) // 30分钟前
        },
        {
          id: 2,
          type: 'publish',
          title: '发布了应用',
          description: '发布了客户管理应用 v2.1.0',
          time: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2小时前
        },
        {
          id: 3,
          type: 'update',
          title: '更新了配置',
          description: '更新了财务管理应用的权限配置',
          time: new Date(Date.now() - 1000 * 60 * 60 * 4) // 4小时前
        },
        {
          id: 4,
          type: 'test',
          title: '测试了应用',
          description: '测试了人事管理应用的连接性',
          time: new Date(Date.now() - 1000 * 60 * 60 * 6) // 6小时前
        }
      ],
      // 创建对话框
      createDialogVisible: false,
      createLoading: false,
      createForm: {
        appName: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        appDescription: ''
      },
      createRules: {
        appName: [
          { required: true, message: '请输入应用名称', trigger: 'blur' }
        ],
        appType: [
          { required: true, message: '请选择应用类型', trigger: 'change' }
        ],
        appPlatform: [
          { required: true, message: '请选择应用平台', trigger: 'change' }
        ],
        appCategory: [
          { required: true, message: '请选择应用分类', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadOverviewData()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getAppConfigSystemOverview()
        if (response.success) {
          this.overviewData = response.data || {}
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
        this.$message.error('加载概览数据失败')
      }
    },

    // 加载最近活动
    loadRecentActivities() {
      // 这里可以调用API获取最近活动
      this.$message.success('活动数据已刷新')
    },

    // 导航方法
    navigateToList() {
      this.$router.push('/management-accountant/mobile/app-config/list')
    },

    navigateToDashboard() {
      this.$router.push('/management-accountant/mobile/app-config/dashboard')
    },

    navigateToTest() {
      this.$router.push('/management-accountant/mobile/app-config/test')
    },

    // 显示创建对话框
    showCreateDialog() {
      this.createDialogVisible = true
    },

    // 重置创建表单
    resetCreateForm() {
      this.$refs.createForm.resetFields()
      this.createForm = {
        appName: '',
        appType: '',
        appPlatform: '',
        appCategory: '',
        appDescription: ''
      }
    },

    // 处理创建应用
    async handleCreateApp() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true

        const appConfig = {
          ...this.createForm,
          appCode: `APP_${Date.now()}`,
          appVersion: '1.0.0',
          isEnabled: true,
          isDefault: false,
          isForceUpdate: false,
          isOfflineSupport: false,
          status: 'DRAFT'
        }

        const response = await createAppConfig(appConfig)
        if (response.success) {
          this.$message.success('创建应用成功')
          this.createDialogVisible = false
          this.loadOverviewData()
        } else {
          this.$message.error(response.message || '创建应用失败')
        }
      } catch (error) {
        console.error('创建应用失败:', error)
        this.$message.error('创建应用失败')
      } finally {
        this.createLoading = false
      }
    },

    // 快速操作方法
    async quickCreateNativeApp() {
      try {
        const response = await quickCreateNativeApp('快速原生应用', 'ANDROID')
        if (response.success) {
          this.$message.success('快速创建原生应用成功')
          this.loadOverviewData()
        } else {
          this.$message.error(response.message || '快速创建失败')
        }
      } catch (error) {
        console.error('快速创建原生应用失败:', error)
        this.$message.error('快速创建失败')
      }
    },

    quickImportApps() {
      this.$message.info('导入应用配置功能开发中...')
    },

    quickExportApps() {
      this.$message.info('导出应用配置功能开发中...')
    },

    quickTestApps() {
      this.$message.info('批量测试应用功能开发中...')
    },

    quickViewDocs() {
      this.$message.info('查看文档功能开发中...')
    },

    quickViewStats() {
      this.navigateToDashboard()
    },

    // 工具方法
    getActivityIcon(type) {
      const iconMap = {
        create: 'el-icon-plus',
        publish: 'el-icon-upload2',
        update: 'el-icon-edit',
        test: 'el-icon-cpu',
        delete: 'el-icon-delete'
      }
      return iconMap[type] || 'el-icon-info'
    },

    formatTime(time) {
      const now = new Date()
      const diff = now - time
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))

      if (minutes < 60) {
        return `${minutes}分钟前`
      } else if (hours < 24) {
        return `${hours}小时前`
      } else {
        return `${days}天前`
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-config-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .page-title {
    margin: 0 0 8px 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;

    i {
      margin-right: 8px;
      color: #409eff;
    }
  }

  .page-description {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.overview-section {
  margin-bottom: 20px;

  .overview-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
  }

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;

    &:hover {
      transform: translateY(-2px);
    }

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

      &.published {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.enabled {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.draft {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .stat-content {
      .stat-number {
        font-size: 32px;
        font-weight: 700;
        color: #303133;
        line-height: 1;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.navigation-section {
  margin-bottom: 20px;

  .nav-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
  }

  .nav-card {
    background: white;
    border-radius: 8px;
    padding: 24px;
    display: flex;
    align-items: center;
    cursor: pointer;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.2s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }

    .nav-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      background: #409eff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .nav-content {
      flex: 1;

      h3 {
        margin: 0 0 4px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      p {
        margin: 0;
        font-size: 14px;
        color: #606266;
      }
    }

    .nav-arrow {
      i {
        font-size: 16px;
        color: #c0c4cc;
      }
    }
  }
}

.activity-section,
.quick-access-section {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
}

.activity-timeline {
  .activity-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }

    .activity-icon {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: #409eff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
      flex-shrink: 0;

      i {
        font-size: 14px;
        color: white;
      }
    }

    .activity-content {
      .activity-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .activity-description {
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
}

.quick-items {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      border-color: #409eff;
      background-color: #f0f9ff;
    }

    i {
      font-size: 24px;
      color: #409eff;
      margin-bottom: 8px;
    }

    span {
      font-size: 14px;
      color: #303133;
      text-align: center;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
