<template>
  <div class="archive-permission-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-key"></i>
            档案权限管理
          </h1>
          <p class="page-description">
            管理档案系统的权限配置、权限验证、权限审计和权限分析
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
            创建权限
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-files"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalPermissions || 0 }}</div>
              <div class="stat-label">权限总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.activePermissions || 0 }}</div>
              <div class="stat-label">激活权限</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.pendingApprovals || 0 }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.highRiskPermissions || 0 }}</div>
              <div class="stat-label">高风险权限</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-navigation">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="goToList">
            <div class="nav-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="nav-content">
              <h3>权限列表</h3>
              <p>查看和管理所有权限配置</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="goToAudit">
            <div class="nav-icon">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="nav-content">
              <h3>权限审计</h3>
              <p>查看权限使用记录和审计日志</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="goToDashboard">
            <div class="nav-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>数据分析</h3>
              <p>权限统计分析和可视化图表</p>
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
        <h2>最近活动</h2>
        <el-button type="text" @click="goToList">查看全部</el-button>
      </div>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-time">{{ formatTime(activity.time) }}</div>
          </div>
          <div class="activity-status">
            <el-tag :type="getActivityStatusType(activity.status)">
              {{ activity.status }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <div class="section-header">
        <h2>快速操作</h2>
      </div>
      <div class="action-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          创建权限
        </el-button>
        <el-button type="success" icon="el-icon-check" @click="batchApprove">
          批量审批
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="cleanupExpired">
          清理过期权限
        </el-button>
        <el-button type="info" icon="el-icon-download" @click="exportData">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 创建权限对话框 -->
    <el-dialog
      title="创建权限"
      :visible.sync="createDialogVisible"
      width="600px"
      :before-close="handleCreateDialogClose"
    >
      <el-form
        ref="createForm"
        :model="createForm"
        :rules="createRules"
        label-width="120px"
      >
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="createForm.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
          <el-select v-model="createForm.permissionType" placeholder="请选择权限类型">
            <el-option label="资源权限" value="RESOURCE" />
            <el-option label="操作权限" value="OPERATION" />
            <el-option label="数据权限" value="DATA" />
            <el-option label="功能权限" value="FUNCTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限级别" prop="permissionLevel">
          <el-select v-model="createForm.permissionLevel" placeholder="请选择权限级别">
            <el-option label="系统级" value="SYSTEM" />
            <el-option label="模块级" value="MODULE" />
            <el-option label="资源级" value="RESOURCE" />
            <el-option label="记录级" value="RECORD" />
          </el-select>
        </el-form-item>
        <el-form-item label="主体类型" prop="subjectType">
          <el-select v-model="createForm.subjectType" placeholder="请选择主体类型">
            <el-option label="用户" value="USER" />
            <el-option label="角色" value="ROLE" />
            <el-option label="用户组" value="GROUP" />
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="组织" value="ORGANIZATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="主体ID" prop="subjectId">
          <el-input v-model="createForm.subjectId" placeholder="请输入主体ID" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="createForm.resourceType" placeholder="请选择资源类型">
            <el-option label="档案" value="ARCHIVE" />
            <el-option label="文档" value="DOCUMENT" />
            <el-option label="文件夹" value="FOLDER" />
            <el-option label="分类" value="CATEGORY" />
            <el-option label="系统" value="SYSTEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="资源ID" prop="resourceId">
          <el-input v-model="createForm.resourceId" placeholder="请输入资源ID" />
        </el-form-item>
        <el-form-item label="操作权限" prop="operationPermissions">
          <el-checkbox-group v-model="createForm.operationPermissions">
            <el-checkbox label="READ">读取</el-checkbox>
            <el-checkbox label="WRITE">写入</el-checkbox>
            <el-checkbox label="DELETE">删除</el-checkbox>
            <el-checkbox label="EXECUTE">执行</el-checkbox>
            <el-checkbox label="ADMIN">管理</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="访问级别" prop="accessLevel">
          <el-select v-model="createForm.accessLevel" placeholder="请选择访问级别">
            <el-option label="公开" value="PUBLIC" />
            <el-option label="内部" value="INTERNAL" />
            <el-option label="机密" value="CONFIDENTIAL" />
            <el-option label="秘密" value="SECRET" />
            <el-option label="绝密" value="TOP_SECRET" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限描述" prop="permissionDescription">
          <el-input
            v-model="createForm.permissionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入权限描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate" :loading="createLoading">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSystemOverview,
  createPermission,
  cleanupExpiredPermissions
} from '@/api/managementAccountant/as/archivePermission'

export default {
  name: 'ArchivePermissionIndex',
  data() {
    return {
      // 统计数据
      statistics: {
        totalPermissions: 0,
        activePermissions: 0,
        pendingApprovals: 0,
        highRiskPermissions: 0
      },
      // 最近活动
      recentActivities: [],
      // 创建对话框
      createDialogVisible: false,
      createLoading: false,
      createForm: {
        permissionName: '',
        permissionType: '',
        permissionLevel: '',
        subjectType: '',
        subjectId: '',
        resourceType: '',
        resourceId: '',
        operationPermissions: [],
        accessLevel: 'INTERNAL',
        permissionDescription: ''
      },
      createRules: {
        permissionName: [
          { required: true, message: '请输入权限名称', trigger: 'blur' }
        ],
        permissionType: [
          { required: true, message: '请选择权限类型', trigger: 'change' }
        ],
        permissionLevel: [
          { required: true, message: '请选择权限级别', trigger: 'change' }
        ],
        subjectType: [
          { required: true, message: '请选择主体类型', trigger: 'change' }
        ],
        subjectId: [
          { required: true, message: '请输入主体ID', trigger: 'blur' }
        ],
        resourceType: [
          { required: true, message: '请选择资源类型', trigger: 'change' }
        ],
        resourceId: [
          { required: true, message: '请输入资源ID', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        await this.loadStatistics()
        await this.loadRecentActivities()
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      }
    },
    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getSystemOverview()
        if (response.success) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    // 加载最近活动
    async loadRecentActivities() {
      // TODO: 实现加载最近活动逻辑
      this.recentActivities = [
        {
          id: 1,
          type: 'create',
          title: '创建权限',
          description: '用户张三创建了档案读取权限',
          time: new Date(),
          status: '成功'
        },
        {
          id: 2,
          type: 'approve',
          title: '审批权限',
          description: '管理员审批通过了文档编辑权限',
          time: new Date(Date.now() - 3600000),
          status: '已审批'
        },
        {
          id: 3,
          type: 'revoke',
          title: '撤销权限',
          description: '撤销了用户李四的删除权限',
          time: new Date(Date.now() - 7200000),
          status: '已撤销'
        }
      ]
    },
    // 刷新数据
    refreshData() {
      this.loadData()
      this.$message.success('数据刷新成功')
    },
    // 导航到列表页面
    goToList() {
      this.$router.push('/managementAccountant/as/archivePermission/list')
    },
    // 导航到审计页面
    goToAudit() {
      this.$router.push('/managementAccountant/as/archivePermission/audit')
    },
    // 导航到仪表板页面
    goToDashboard() {
      this.$router.push('/managementAccountant/as/archivePermission/dashboard')
    },
    // 显示创建对话框
    showCreateDialog() {
      this.createDialogVisible = true
      this.resetCreateForm()
    },
    // 重置创建表单
    resetCreateForm() {
      this.createForm = {
        permissionName: '',
        permissionType: '',
        permissionLevel: '',
        subjectType: '',
        subjectId: '',
        resourceType: '',
        resourceId: '',
        operationPermissions: [],
        accessLevel: 'INTERNAL',
        permissionDescription: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    },
    // 处理创建
    handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          this.createLoading = true
          try {
            const formData = {
              ...this.createForm,
              operationPermissions: this.createForm.operationPermissions.join(',')
            }
            const response = await createPermission(formData)
            if (response.success) {
              this.$message.success('权限创建成功')
              this.createDialogVisible = false
              this.loadData()
            } else {
              this.$message.error(response.message || '权限创建失败')
            }
          } catch (error) {
            console.error('创建权限失败:', error)
            this.$message.error('创建权限失败')
          } finally {
            this.createLoading = false
          }
        }
      })
    },
    // 处理创建对话框关闭
    handleCreateDialogClose(done) {
      this.resetCreateForm()
      done()
    },
    // 批量审批
    batchApprove() {
      this.$router.push('/managementAccountant/as/archivePermission/list?tab=pending')
    },
    // 清理过期权限
    async cleanupExpired() {
      try {
        const response = await cleanupExpiredPermissions()
        if (response.success) {
          this.$message.success(`清理了 ${response.data} 个过期权限`)
          this.loadData()
        } else {
          this.$message.error(response.message || '清理过期权限失败')
        }
      } catch (error) {
        console.error('清理过期权限失败:', error)
        this.$message.error('清理过期权限失败')
      }
    },
    // 导出数据
    exportData() {
      this.$message.info('导出功能开发中...')
    },
    // 获取活动图标
    getActivityIcon(type) {
      const iconMap = {
        create: 'el-icon-plus',
        approve: 'el-icon-check',
        revoke: 'el-icon-close',
        update: 'el-icon-edit',
        delete: 'el-icon-delete'
      }
      return iconMap[type] || 'el-icon-info'
    },
    // 获取活动状态类型
    getActivityStatusType(status) {
      const typeMap = {
        '成功': 'success',
        '已审批': 'success',
        '已撤销': 'warning',
        '失败': 'danger',
        '待处理': 'info'
      }
      return typeMap[status] || 'info'
    },
    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const now = new Date()
      const diff = now - new Date(time)
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(diff / 3600000)
      const days = Math.floor(diff / 86400000)
      
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return `${minutes}分钟前`
      if (hours < 24) return `${hours}小时前`
      if (days < 7) return `${days}天前`
      return new Date(time).toLocaleDateString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-permission-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: white;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            color: #409eff;
          }
        }

        .page-description {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }

      .header-right {
        display: flex;
        gap: 12px;
      }
    }
  }

  .statistics-cards {
    margin-bottom: 20px;

    .stat-card {
      background: white;
      border-radius: 8px;
      padding: 24px;
      display: flex;
      align-items: center;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

        &.pending {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.risk {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-content {
        .stat-number {
          font-size: 28px;
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

  .function-navigation {
    margin-bottom: 20px;

    .nav-card {
      background: white;
      border-radius: 8px;
      padding: 24px;
      display: flex;
      align-items: center;
      cursor: pointer;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      transition: all 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      }

      .nav-icon {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
          color: #c0c4cc;
        }
      }
    }
  }

  .recent-activity {
    background: white;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

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

    .activity-list {
      .activity-item {
        display: flex;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .activity-icon {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: #f5f7fa;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 16px;
            color: #409eff;
          }
        }

        .activity-content {
          flex: 1;

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

        .activity-status {
          margin-left: 16px;
        }
      }
    }
  }

  .quick-actions {
    background: white;
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .section-header {
      margin-bottom: 20px;

      h2 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }

    .action-buttons {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
    }
  }
}

// 对话框样式
::v-deep .el-dialog {
  .el-dialog__header {
    background: #f5f7fa;
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;

    .el-dialog__title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .el-dialog__body {
    padding: 24px;
  }

  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #ebeef5;
    background: #f5f7fa;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .archive-permission-container {
    padding: 12px;

    .page-header .header-content {
      flex-direction: column;
      align-items: flex-start;
      gap: 16px;
    }

    .statistics-cards .el-col {
      margin-bottom: 12px;
    }

    .function-navigation .el-col {
      margin-bottom: 12px;
    }

    .quick-actions .action-buttons {
      flex-direction: column;

      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
