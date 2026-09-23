<template>
  <div class="offline-data-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-download"></i>
            离线数据管理
          </h1>
          <p class="page-description">
            管理移动应用的离线数据，包括缓存数据、备份数据、同步数据等
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
            创建离线数据
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
            <i class="el-icon-download"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.totalOfflineData || 0 }}</div>
            <div class="stat-label">离线数据总数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon active">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.activeOfflineData || 0 }}</div>
            <div class="stat-label">活跃数据</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon syncing">
            <i class="el-icon-loading"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.syncingOfflineData || 0 }}</div>
            <div class="stat-label">同步中数据</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon expired">
            <i class="el-icon-warning"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ overviewData.expiredOfflineData || 0 }}</div>
            <div class="stat-label">过期数据</div>
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
            <h3>数据列表管理</h3>
            <p>查看和管理所有离线数据</p>
          </div>
          <div class="nav-arrow">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
        <div class="nav-card" @click="navigateToSync">
          <div class="nav-icon">
            <i class="el-icon-refresh"></i>
          </div>
          <div class="nav-content">
            <h3>数据同步管理</h3>
            <p>管理数据同步任务和状态</p>
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
            <p>查看数据使用统计和分析</p>
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
        <div class="quick-item" @click="quickCreateCacheData">
          <i class="el-icon-download"></i>
          <span>创建缓存数据</span>
        </div>
        <div class="quick-item" @click="quickSyncAllData">
          <i class="el-icon-refresh"></i>
          <span>同步所有数据</span>
        </div>
        <div class="quick-item" @click="quickCleanExpiredData">
          <i class="el-icon-delete"></i>
          <span>清理过期数据</span>
        </div>
        <div class="quick-item" @click="quickExportData">
          <i class="el-icon-upload2"></i>
          <span>导出数据</span>
        </div>
        <div class="quick-item" @click="quickViewStats">
          <i class="el-icon-data-line"></i>
          <span>查看统计</span>
        </div>
        <div class="quick-item" @click="quickViewDocs">
          <i class="el-icon-document"></i>
          <span>查看文档</span>
        </div>
      </div>
    </div>

    <!-- 创建离线数据对话框 -->
    <el-dialog
      title="创建离线数据"
      :visible.sync="createDialogVisible"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
      >
        <el-form-item label="数据名称" prop="offlineDataName">
          <el-input v-model="createForm.offlineDataName" placeholder="请输入数据名称" />
        </el-form-item>
        <el-form-item label="数据类型" prop="offlineDataType">
          <el-select v-model="createForm.offlineDataType" placeholder="请选择数据类型" style="width: 100%">
            <el-option label="缓存数据" value="CACHE" />
            <el-option label="备份数据" value="BACKUP" />
            <el-option label="同步数据" value="SYNC" />
            <el-option label="临时数据" value="TEMP" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据分类" prop="offlineDataCategory">
          <el-select v-model="createForm.offlineDataCategory" placeholder="请选择数据分类" style="width: 100%">
            <el-option label="业务数据" value="BUSINESS" />
            <el-option label="系统数据" value="SYSTEM" />
            <el-option label="用户数据" value="USER" />
            <el-option label="配置数据" value="CONFIG" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据模块" prop="offlineDataModule">
          <el-input v-model="createForm.offlineDataModule" placeholder="请输入数据模块" />
        </el-form-item>
        <el-form-item label="数据内容" prop="dataContent">
          <el-input
            v-model="createForm.dataContent"
            type="textarea"
            :rows="4"
            placeholder="请输入数据内容"
          />
        </el-form-item>
        <el-form-item label="数据描述">
          <el-input
            v-model="createForm.dataDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入数据描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateOfflineData" :loading="createLoading">
          创建
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getOfflineDataSystemOverview,
  createOfflineData,
  quickCreateCacheData
} from '@/api/managementAccountant/mobile/offlineData'

export default {
  name: 'MobileOfflineDataIndex',
  data() {
    return {
      // 概览数据
      overviewData: {},
      // 最近活动
      recentActivities: [
        {
          id: 1,
          type: 'create',
          title: '创建了新的离线数据',
          description: '创建了用户缓存数据 v1.0.0',
          time: new Date(Date.now() - 1000 * 60 * 30) // 30分钟前
        },
        {
          id: 2,
          type: 'sync',
          title: '同步了数据',
          description: '同步了业务数据到本地存储',
          time: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2小时前
        },
        {
          id: 3,
          type: 'clean',
          title: '清理了过期数据',
          description: '清理了30天前的临时数据',
          time: new Date(Date.now() - 1000 * 60 * 60 * 4) // 4小时前
        },
        {
          id: 4,
          type: 'backup',
          title: '备份了数据',
          description: '备份了重要业务数据',
          time: new Date(Date.now() - 1000 * 60 * 60 * 6) // 6小时前
        }
      ],
      // 创建对话框
      createDialogVisible: false,
      createLoading: false,
      createForm: {
        offlineDataName: '',
        offlineDataType: '',
        offlineDataCategory: '',
        offlineDataModule: '',
        dataContent: '',
        dataDescription: ''
      },
      createRules: {
        offlineDataName: [
          { required: true, message: '请输入数据名称', trigger: 'blur' }
        ],
        offlineDataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        offlineDataCategory: [
          { required: true, message: '请选择数据分类', trigger: 'change' }
        ],
        dataContent: [
          { required: true, message: '请输入数据内容', trigger: 'blur' }
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
        const response = await getOfflineDataSystemOverview()
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
      this.$router.push('/management-accountant/mobile/offline-data/list')
    },

    navigateToSync() {
      this.$router.push('/management-accountant/mobile/offline-data/sync')
    },

    navigateToDashboard() {
      this.$router.push('/management-accountant/mobile/offline-data/dashboard')
    },

    // 显示创建对话框
    showCreateDialog() {
      this.createDialogVisible = true
    },

    // 重置创建表单
    resetCreateForm() {
      this.$refs.createForm.resetFields()
      this.createForm = {
        offlineDataName: '',
        offlineDataType: '',
        offlineDataCategory: '',
        offlineDataModule: '',
        dataContent: '',
        dataDescription: ''
      }
    },

    // 处理创建离线数据
    async handleCreateOfflineData() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true

        const offlineData = {
          ...this.createForm,
          offlineDataCode: `OFFLINE_${Date.now()}`,
          dataVersion: '1.0.0',
          isAvailable: true,
          isExpired: false,
          dataStatus: 'ACTIVE',
          syncStatus: 'PENDING'
        }

        const response = await createOfflineData(offlineData)
        if (response.success) {
          this.$message.success('创建离线数据成功')
          this.createDialogVisible = false
          this.loadOverviewData()
        } else {
          this.$message.error(response.message || '创建离线数据失败')
        }
      } catch (error) {
        console.error('创建离线数据失败:', error)
        this.$message.error('创建离线数据失败')
      } finally {
        this.createLoading = false
      }
    },

    // 快速操作方法
    async quickCreateCacheData() {
      try {
        const response = await quickCreateCacheData('快速缓存数据', '{"type":"cache","data":"sample"}')
        if (response.success) {
          this.$message.success('快速创建缓存数据成功')
          this.loadOverviewData()
        } else {
          this.$message.error(response.message || '快速创建失败')
        }
      } catch (error) {
        console.error('快速创建缓存数据失败:', error)
        this.$message.error('快速创建失败')
      }
    },

    quickSyncAllData() {
      this.$message.info('同步所有数据功能开发中...')
    },

    quickCleanExpiredData() {
      this.$message.info('清理过期数据功能开发中...')
    },

    quickExportData() {
      this.$message.info('导出数据功能开发中...')
    },

    quickViewStats() {
      this.navigateToDashboard()
    },

    quickViewDocs() {
      this.$message.info('查看文档功能开发中...')
    },

    // 工具方法
    getActivityIcon(type) {
      const iconMap = {
        create: 'el-icon-plus',
        sync: 'el-icon-refresh',
        clean: 'el-icon-delete',
        backup: 'el-icon-download',
        update: 'el-icon-edit'
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
.offline-data-container {
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

      &.active {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.syncing {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.expired {
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
