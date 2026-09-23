<template>
  <div class="document-version-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-document-copy"></i>
          文档版本管理
        </h2>
        <p class="page-description">管理文档版本、版本对比、权限控制和生命周期管理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateVersion">
          创建版本
        </el-button>
        <el-button type="success" icon="el-icon-upload" @click="handleImportData">
          导入数据
        </el-button>
        <el-button type="info" icon="el-icon-download" @click="handleExportData">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalVersions }}</div>
              <div class="stat-label">版本总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon published">
              <i class="el-icon-upload"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.publishedVersions }}</div>
              <div class="stat-label">已发布版本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon draft">
              <i class="el-icon-edit"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.draftVersions }}</div>
              <div class="stat-label">草稿版本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon storage">
              <i class="el-icon-folder"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ formatFileSize(statistics.totalStorage) }}</div>
              <div class="stat-label">存储使用</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateTo('list')">
            <div class="nav-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>版本列表</h3>
              <p>查看和管理所有文档版本</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateTo('comparison')">
            <div class="nav-icon">
              <i class="el-icon-connection"></i>
            </div>
            <div class="nav-content">
              <h3>版本对比</h3>
              <p>比较不同版本之间的差异</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateTo('dashboard')">
            <div class="nav-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>数据分析</h3>
              <p>版本统计和趋势分析</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <div class="section-header">
        <h3>最近活动</h3>
        <el-button type="text" @click="navigateTo('list')">查看全部</el-button>
      </div>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-time">{{ formatTime(activity.createTime) }}</div>
          </div>
          <div class="activity-status">
            <el-tag :type="getActivityStatusType(activity.status)" size="mini">
              {{ formatActivityStatus(activity.status) }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <div class="section-header">
        <h3>快速操作</h3>
      </div>
      <div class="action-buttons">
        <el-button-group>
          <el-button icon="el-icon-plus" @click="handleCreateVersion">创建版本</el-button>
          <el-button icon="el-icon-copy-document" @click="handleCopyVersion">复制版本</el-button>
          <el-button icon="el-icon-share" @click="handleCreateBranch">创建分支</el-button>
          <el-button icon="el-icon-connection" @click="handleMergeBranch">合并分支</el-button>
        </el-button-group>
        <el-button-group>
          <el-button icon="el-icon-upload" @click="handlePublishVersion">发布版本</el-button>
          <el-button icon="el-icon-lock" @click="handleLockVersion">锁定版本</el-button>
          <el-button icon="el-icon-folder" @click="handleArchiveVersion">归档版本</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleRollbackVersion">回滚版本</el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 创建版本对话框 -->
    <el-dialog
      title="创建文档版本"
      :visible.sync="createVersionDialog"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form ref="createForm" :model="createForm" :rules="createRules" label-width="100px">
        <el-form-item label="文档ID" prop="documentId">
          <el-input v-model="createForm.documentId" placeholder="请输入文档ID"></el-input>
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="createForm.versionName" placeholder="请输入版本名称"></el-input>
        </el-form-item>
        <el-form-item label="版本类型" prop="versionType">
          <el-select v-model="createForm.versionType" placeholder="请选择版本类型">
            <el-option label="主版本" value="MAJOR"></el-option>
            <el-option label="次版本" value="MINOR"></el-option>
            <el-option label="补丁版本" value="PATCH"></el-option>
            <el-option label="草稿版本" value="DRAFT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="版本描述" prop="versionDescription">
          <el-input
            v-model="createForm.versionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入版本描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createVersionDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCreateVersion" :loading="createLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  countVersions,
  countByStatus,
  getStorageUsage,
  createVersion,
  getVersionPage,
  formatFileSize,
  formatVersionStatus,
  VERSION_TYPES,
  VERSION_STATUS
} from '@/api/managementAccountant/as/documentVersion'

export default {
  name: 'DocumentVersionIndex',
  data() {
    return {
      // 统计数据
      statistics: {
        totalVersions: 0,
        publishedVersions: 0,
        draftVersions: 0,
        totalStorage: 0
      },
      // 最近活动
      recentActivities: [],
      // 创建版本对话框
      createVersionDialog: false,
      createLoading: false,
      createForm: {
        documentId: '',
        versionName: '',
        versionType: '',
        versionDescription: ''
      },
      createRules: {
        documentId: [
          { required: true, message: '请输入文档ID', trigger: 'blur' }
        ],
        versionName: [
          { required: true, message: '请输入版本名称', trigger: 'blur' }
        ],
        versionType: [
          { required: true, message: '请选择版本类型', trigger: 'change' }
        ],
        versionDescription: [
          { required: true, message: '请输入版本描述', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadStatistics()
    this.loadRecentActivities()
  },
  methods: {
    // 加载统计数据
    async loadStatistics() {
      try {
        const [totalRes, statusRes, storageRes] = await Promise.all([
          countVersions(),
          countByStatus(),
          getStorageUsage()
        ])

        this.statistics.totalVersions = totalRes.data || 0
        
        if (statusRes.data && Array.isArray(statusRes.data)) {
          const statusMap = statusRes.data.reduce((map, item) => {
            map[item.status] = item.count
            return map
          }, {})
          
          this.statistics.publishedVersions = statusMap[VERSION_STATUS.PUBLISHED] || 0
          this.statistics.draftVersions = statusMap[VERSION_STATUS.DRAFT] || 0
        }
        
        this.statistics.totalStorage = storageRes.data?.totalSize || 0
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 加载最近活动
    async loadRecentActivities() {
      try {
        const response = await getVersionPage({
          current: 1,
          size: 10,
          sortField: 'created_time',
          sortOrder: 'desc'
        })
        
        if (response.data && response.data.records) {
          this.recentActivities = response.data.records.map(version => ({
            id: version.versionId,
            type: version.changeType || 'CREATE',
            title: version.versionName,
            description: version.versionDescription || '无描述',
            status: version.versionStatus,
            createTime: version.createdTime
          }))
        }
      } catch (error) {
        console.error('加载最近活动失败:', error)
      }
    },

    // 导航到指定页面
    navigateTo(page) {
      switch (page) {
        case 'list':
          this.$router.push('/managementAccountant/as/documentVersion/list')
          break
        case 'comparison':
          this.$router.push('/managementAccountant/as/documentVersion/comparison')
          break
        case 'dashboard':
          this.$router.push('/managementAccountant/as/documentVersion/dashboard')
          break
      }
    },

    // 处理创建版本
    handleCreateVersion() {
      this.createVersionDialog = true
    },

    // 确认创建版本
    confirmCreateVersion() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          this.createLoading = true
          try {
            await createVersion(this.createForm)
            this.$message.success('创建版本成功')
            this.createVersionDialog = false
            this.loadStatistics()
            this.loadRecentActivities()
          } catch (error) {
            this.$message.error('创建版本失败: ' + error.message)
          } finally {
            this.createLoading = false
          }
        }
      })
    },

    // 重置创建表单
    resetCreateForm() {
      this.$refs.createForm?.resetFields()
      this.createForm = {
        documentId: '',
        versionName: '',
        versionType: '',
        versionDescription: ''
      }
    },

    // 处理其他快速操作
    handleCopyVersion() {
      this.$message.info('请先选择要复制的版本')
      this.navigateTo('list')
    },

    handleCreateBranch() {
      this.$message.info('请先选择要创建分支的版本')
      this.navigateTo('list')
    },

    handleMergeBranch() {
      this.$message.info('请先选择要合并的分支版本')
      this.navigateTo('list')
    },

    handlePublishVersion() {
      this.$message.info('请先选择要发布的版本')
      this.navigateTo('list')
    },

    handleLockVersion() {
      this.$message.info('请先选择要锁定的版本')
      this.navigateTo('list')
    },

    handleArchiveVersion() {
      this.$message.info('请先选择要归档的版本')
      this.navigateTo('list')
    },

    handleRollbackVersion() {
      this.$message.info('请先选择要回滚的版本')
      this.navigateTo('list')
    },

    handleImportData() {
      this.$message.info('数据导入功能开发中')
    },

    handleExportData() {
      this.$message.info('数据导出功能开发中')
    },

    // 工具方法
    formatFileSize,
    
    formatTime(time) {
      if (!time) return '-'
      return this.$moment(time).format('YYYY-MM-DD HH:mm:ss')
    },

    getActivityIcon(type) {
      const iconMap = {
        CREATE: 'el-icon-plus',
        UPDATE: 'el-icon-edit',
        DELETE: 'el-icon-delete',
        COPY: 'el-icon-copy-document',
        BRANCH: 'el-icon-share',
        MERGE: 'el-icon-connection',
        ROLLBACK: 'el-icon-refresh-left'
      }
      return iconMap[type] || 'el-icon-document'
    },

    getActivityStatusType(status) {
      const typeMap = {
        [VERSION_STATUS.DRAFT]: 'info',
        [VERSION_STATUS.UNDER_REVIEW]: 'warning',
        [VERSION_STATUS.APPROVED]: 'success',
        [VERSION_STATUS.PUBLISHED]: 'primary',
        [VERSION_STATUS.ARCHIVED]: 'info',
        [VERSION_STATUS.DEPRECATED]: 'danger',
        [VERSION_STATUS.DELETED]: 'danger'
      }
      return typeMap[status] || 'info'
    },

    formatActivityStatus(status) {
      return formatVersionStatus(status)
    }
  }
}
</script>

<style lang="scss" scoped>
.document-version-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      .page-title {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .page-description {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .stats-cards {
    margin-bottom: 20px;

    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

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

        &.draft {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.storage {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 4px;
        }
      }
    }
  }

  .function-nav {
    margin-bottom: 20px;

    .nav-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
      }

      .nav-icon {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        background: #409EFF;
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
          color: #909399;
        }
      }

      .nav-arrow {
        i {
          font-size: 16px;
          color: #C0C4CC;
        }
      }
    }
  }

  .recent-activity,
  .quick-actions {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 20px 0 20px;

      h3 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }
  }

  .activity-list {
    padding: 20px;

    .activity-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #EBEEF5;

      &:last-child {
        border-bottom: none;
      }

      .activity-icon {
        width: 40px;
        height: 40px;
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

      .activity-content {
        flex: 1;

        .activity-title {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .activity-description {
          font-size: 12px;
          color: #909399;
          margin-bottom: 4px;
        }

        .activity-time {
          font-size: 12px;
          color: #C0C4CC;
        }
      }

      .activity-status {
        margin-left: 12px;
      }
    }
  }

  .action-buttons {
    padding: 20px;

    .el-button-group {
      margin-right: 16px;
      margin-bottom: 12px;
    }
  }
}
</style>
