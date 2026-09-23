<template>
  <div class="archive-permission-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
          <div class="title-section">
            <h1 class="page-title">权限详情</h1>
            <p class="page-subtitle">{{ permissionData.permissionName || '加载中...' }}</p>
          </div>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-edit" @click="handleEdit">
            编辑权限
          </el-button>
          <el-dropdown @command="handleCommand">
            <el-button type="default">
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="approve" icon="el-icon-check">
                审批权限
              </el-dropdown-item>
              <el-dropdown-item command="suspend" icon="el-icon-warning">
                暂停权限
              </el-dropdown-item>
              <el-dropdown-item command="delegate" icon="el-icon-share">
                委托权限
              </el-dropdown-item>
              <el-dropdown-item command="copy" icon="el-icon-document-copy">
                复制权限
              </el-dropdown-item>
              <el-dropdown-item command="delete" icon="el-icon-delete" divided>
                删除权限
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content" v-loading="loading">
      <el-row :gutter="20">
        <!-- 左侧内容 -->
        <el-col :span="16">
          <!-- 基本信息 -->
          <div class="info-card">
            <div class="card-header">
              <h3>基本信息</h3>
              <el-tag :type="getPermissionStatusColor(permissionData.permissionStatus)">
                {{ formatPermissionStatus(permissionData.permissionStatus) }}
              </el-tag>
            </div>
            <div class="card-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限编码</label>
                    <span>{{ permissionData.permissionCode || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限名称</label>
                    <span>{{ permissionData.permissionName || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限类型</label>
                    <el-tag :type="getPermissionTypeColor(permissionData.permissionType)">
                      {{ formatPermissionType(permissionData.permissionType) }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限级别</label>
                    <span>{{ formatPermissionLevel(permissionData.permissionLevel) }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>访问级别</label>
                    <el-tag :type="getAccessLevelColor(permissionData.accessLevel)">
                      {{ formatAccessLevel(permissionData.accessLevel) }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>风险等级</label>
                    <el-tag :type="getRiskLevelColor(permissionData.riskLevel)">
                      {{ formatRiskLevel(permissionData.riskLevel) }}
                    </el-tag>
                  </div>
                </el-col>
              </el-row>
              <div class="info-item full-width">
                <label>权限描述</label>
                <p>{{ permissionData.permissionDescription || '暂无描述' }}</p>
              </div>
            </div>
          </div>

          <!-- 主体信息 -->
          <div class="info-card">
            <div class="card-header">
              <h3>主体信息</h3>
            </div>
            <div class="card-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>主体类型</label>
                    <span>{{ formatSubjectType(permissionData.subjectType) }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>主体ID</label>
                    <span>{{ permissionData.subjectId || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>主体名称</label>
                    <span>{{ permissionData.subjectName || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>主体描述</label>
                    <span>{{ permissionData.subjectDescription || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 资源信息 -->
          <div class="info-card">
            <div class="card-header">
              <h3>资源信息</h3>
            </div>
            <div class="card-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>资源类型</label>
                    <span>{{ formatResourceType(permissionData.resourceType) }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>资源ID</label>
                    <span>{{ permissionData.resourceId || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>资源名称</label>
                    <span>{{ permissionData.resourceName || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>资源路径</label>
                    <span>{{ permissionData.resourcePath || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 权限配置 -->
          <div class="info-card">
            <div class="card-header">
              <h3>权限配置</h3>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>操作权限</label>
                <div class="permission-tags">
                  <el-tag
                    v-for="permission in getOperationPermissions()"
                    :key="permission"
                    type="success"
                    size="small"
                  >
                    {{ formatOperationPermission(permission) }}
                  </el-tag>
                </div>
              </div>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>数据权限</label>
                    <span>{{ permissionData.dataPermissions || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限优先级</label>
                    <span>{{ formatPriority(permissionData.permissionPriority) }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限权重</label>
                    <span>{{ permissionData.permissionWeight || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>是否可继承</label>
                    <el-tag :type="permissionData.isInheritable ? 'success' : 'info'">
                      {{ permissionData.isInheritable ? '是' : '否' }}
                    </el-tag>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>

          <!-- 生效控制 -->
          <div class="info-card">
            <div class="card-header">
              <h3>生效控制</h3>
            </div>
            <div class="card-content">
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>生效时间</label>
                    <span>{{ formatDateTime(permissionData.effectiveTime) }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>失效时间</label>
                    <span>{{ formatDateTime(permissionData.expiryTime) }}</span>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="info-item">
                    <label>是否永久有效</label>
                    <el-tag :type="permissionData.isPermanent ? 'success' : 'warning'">
                      {{ permissionData.isPermanent ? '是' : '否' }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="info-item">
                    <label>权限状态</label>
                    <el-tag :type="getPermissionStatusColor(permissionData.permissionStatus)">
                      {{ formatPermissionStatus(permissionData.permissionStatus) }}
                    </el-tag>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>

        <!-- 右侧内容 -->
        <el-col :span="8">
          <!-- 审批信息 -->
          <div class="info-card">
            <div class="card-header">
              <h3>审批信息</h3>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>审批状态</label>
                <el-tag :type="getApprovalStatusColor(permissionData.approvalStatus)">
                  {{ formatApprovalStatus(permissionData.approvalStatus) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>审批人</label>
                <span>{{ permissionData.approverId || '-' }}</span>
              </div>
              <div class="info-item">
                <label>审批时间</label>
                <span>{{ formatDateTime(permissionData.approvalTime) }}</span>
              </div>
              <div class="info-item">
                <label>审批意见</label>
                <p>{{ permissionData.approvalComment || '暂无意见' }}</p>
              </div>
            </div>
          </div>

          <!-- 使用统计 -->
          <div class="info-card">
            <div class="card-header">
              <h3>使用统计</h3>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>使用次数</label>
                <span class="stat-number">{{ permissionData.usageCount || 0 }}</span>
              </div>
              <div class="info-item">
                <label>最后使用时间</label>
                <span>{{ formatDateTime(permissionData.lastUsedTime) }}</span>
              </div>
              <div class="info-item">
                <label>最后访问IP</label>
                <span>{{ permissionData.lastAccessIp || '-' }}</span>
              </div>
            </div>
          </div>

          <!-- 风险评估 -->
          <div class="info-card">
            <div class="card-header">
              <h3>风险评估</h3>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>风险等级</label>
                <el-tag :type="getRiskLevelColor(permissionData.riskLevel)">
                  {{ formatRiskLevel(permissionData.riskLevel) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>风险评分</label>
                <div class="risk-score">
                  <el-progress
                    :percentage="permissionData.riskScore || 0"
                    :color="getRiskScoreColor(permissionData.riskScore)"
                    :show-text="false"
                  />
                  <span class="score-text">{{ permissionData.riskScore || 0 }}/100</span>
                </div>
              </div>
              <div class="info-item">
                <label>风险描述</label>
                <p>{{ permissionData.riskDescription || '暂无描述' }}</p>
              </div>
              <div class="info-item">
                <label>合规状态</label>
                <el-tag :type="getComplianceStatusColor(permissionData.complianceStatus)">
                  {{ formatComplianceStatus(permissionData.complianceStatus) }}
                </el-tag>
              </div>
            </div>
          </div>

          <!-- 创建信息 -->
          <div class="info-card">
            <div class="card-header">
              <h3>创建信息</h3>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>创建人</label>
                <span>{{ permissionData.createdBy || '-' }}</span>
              </div>
              <div class="info-item">
                <label>创建时间</label>
                <span>{{ formatDateTime(permissionData.createdTime) }}</span>
              </div>
              <div class="info-item">
                <label>更新人</label>
                <span>{{ permissionData.updatedBy || '-' }}</span>
              </div>
              <div class="info-item">
                <label>更新时间</label>
                <span>{{ formatDateTime(permissionData.updatedTime) }}</span>
              </div>
              <div class="info-item">
                <label>版本号</label>
                <span>{{ permissionData.version || '-' }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {
  getPermissionById,
  updatePermissionStatus,
  deletePermission,
  formatPermissionType,
  formatPermissionLevel,
  formatPermissionStatus,
  formatSubjectType,
  formatResourceType,
  formatAccessLevel,
  formatRiskLevel,
  formatApprovalStatus,
  getPermissionStatusColor,
  getAccessLevelColor,
  getRiskLevelColor
} from '@/api/managementAccountant/as/archivePermission'

export default {
  name: 'ArchivePermissionDetail',
  data() {
    return {
      loading: false,
      permissionData: {}
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      const permissionId = this.$route.params.id
      if (!permissionId) {
        this.$message.error('权限ID不能为空')
        this.goBack()
        return
      }

      this.loading = true
      try {
        const response = await getPermissionById(permissionId)
        if (response.success) {
          this.permissionData = response.data || {}
        } else {
          this.$message.error(response.message || '获取权限详情失败')
        }
      } catch (error) {
        console.error('获取权限详情失败:', error)
        this.$message.error('获取权限详情失败')
      } finally {
        this.loading = false
      }
    },
    // 返回
    goBack() {
      this.$router.go(-1)
    },
    // 编辑
    handleEdit() {
      this.$router.push(`/managementAccountant/as/archivePermission/edit/${this.permissionData.permissionId}`)
    },
    // 命令处理
    async handleCommand(command) {
      switch (command) {
        case 'approve':
          await this.handleUpdateStatus('APPROVED')
          break
        case 'suspend':
          await this.handleUpdateStatus('SUSPENDED')
          break
        case 'delegate':
          this.$message.info('委托功能开发中...')
          break
        case 'copy':
          this.handleCopy()
          break
        case 'delete':
          await this.handleDelete()
          break
      }
    },
    // 更新状态
    async handleUpdateStatus(status) {
      try {
        const response = await updatePermissionStatus(this.permissionData.permissionId, status)
        if (response.success) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '状态更新失败')
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败')
      }
    },
    // 复制权限
    handleCopy() {
      this.$router.push({
        path: '/managementAccountant/as/archivePermission/create',
        query: { copyFrom: this.permissionData.permissionId }
      })
    },
    // 删除权限
    async handleDelete() {
      try {
        await this.$confirm('确定要删除这个权限吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deletePermission(this.permissionData.permissionId)
        if (response.success) {
          this.$message.success('删除成功')
          this.goBack()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    // 获取操作权限
    getOperationPermissions() {
      if (!this.permissionData.operationPermissions) return []
      return this.permissionData.operationPermissions.split(',').filter(p => p.trim())
    },
    // 格式化方法
    formatPermissionType,
    formatPermissionLevel,
    formatPermissionStatus,
    formatSubjectType,
    formatResourceType,
    formatAccessLevel,
    formatRiskLevel,
    formatApprovalStatus,
    getPermissionStatusColor,
    getAccessLevelColor,
    getRiskLevelColor,
    getPermissionTypeColor(type) {
      const colorMap = {
        RESOURCE: 'primary',
        OPERATION: 'success',
        DATA: 'warning',
        FUNCTION: 'info'
      }
      return colorMap[type] || 'info'
    },
    getApprovalStatusColor(status) {
      const colorMap = {
        PENDING: 'warning',
        APPROVED: 'success',
        REJECTED: 'danger',
        CANCELLED: 'info'
      }
      return colorMap[status] || 'info'
    },
    getComplianceStatusColor(status) {
      const colorMap = {
        COMPLIANT: 'success',
        NON_COMPLIANT: 'danger',
        UNDER_REVIEW: 'warning'
      }
      return colorMap[status] || 'info'
    },
    getRiskScoreColor(score) {
      if (score >= 80) return '#f56c6c'
      if (score >= 60) return '#e6a23c'
      if (score >= 40) return '#409eff'
      return '#67c23a'
    },
    formatOperationPermission(permission) {
      const permissionMap = {
        READ: '读取',
        WRITE: '写入',
        DELETE: '删除',
        EXECUTE: '执行',
        ADMIN: '管理'
      }
      return permissionMap[permission] || permission
    },
    formatPriority(priority) {
      const priorityMap = {
        1: '最高',
        2: '高',
        3: '中',
        4: '低',
        5: '最低'
      }
      return priorityMap[priority] || priority
    },
    formatComplianceStatus(status) {
      const statusMap = {
        COMPLIANT: '合规',
        NON_COMPLIANT: '不合规',
        UNDER_REVIEW: '审查中'
      }
      return statusMap[status] || status
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-permission-detail {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: white;
    border-radius: 8px;
    padding: 20px 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        display: flex;
        align-items: center;
        gap: 16px;

        .title-section {
          .page-title {
            margin: 0 0 4px 0;
            font-size: 20px;
            font-weight: 600;
            color: #303133;
          }

          .page-subtitle {
            margin: 0;
            font-size: 14px;
            color: #909399;
          }
        }
      }

      .header-right {
        display: flex;
        gap: 12px;
      }
    }
  }

  .main-content {
    .info-card {
      background: white;
      border-radius: 8px;
      margin-bottom: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .card-header {
        padding: 20px 24px 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #ebeef5;
        margin-bottom: 20px;

        h3 {
          margin: 0 0 20px 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
      }

      .card-content {
        padding: 0 24px 24px;

        .info-item {
          margin-bottom: 16px;

          &.full-width {
            grid-column: 1 / -1;
          }

          label {
            display: block;
            font-size: 13px;
            color: #909399;
            margin-bottom: 4px;
            font-weight: 500;
          }

          span {
            font-size: 14px;
            color: #303133;
            word-break: break-all;
          }

          p {
            margin: 0;
            font-size: 14px;
            color: #606266;
            line-height: 1.5;
            word-break: break-word;
          }

          .stat-number {
            font-size: 18px;
            font-weight: 600;
            color: #409eff;
          }

          .permission-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;

            .el-tag {
              margin: 0;
            }
          }

          .risk-score {
            display: flex;
            align-items: center;
            gap: 12px;

            .el-progress {
              flex: 1;
            }

            .score-text {
              font-size: 12px;
              color: #909399;
              white-space: nowrap;
            }
          }
        }
      }
    }
  }
}

// 标签样式
.el-tag {
  font-size: 12px;
  padding: 0 8px;
  height: 24px;
  line-height: 22px;
  border-radius: 4px;
}

// 下拉菜单样式
::v-deep .el-dropdown-menu {
  .el-dropdown-menu__item {
    padding: 8px 16px;

    &:hover {
      background-color: #f5f7fa;
    }

    i {
      margin-right: 8px;
    }
  }
}

// 进度条样式
::v-deep .el-progress {
  .el-progress-bar {
    .el-progress-bar__outer {
      background-color: #ebeef5;
      border-radius: 4px;
      height: 8px;

      .el-progress-bar__inner {
        border-radius: 4px;
        transition: width 0.3s ease;
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .archive-permission-detail {
    .main-content {
      .el-row {
        .el-col:first-child {
          margin-bottom: 20px;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .archive-permission-detail {
    padding: 12px;

    .page-header {
      padding: 16px;

      .header-content {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;

        .header-left {
          flex-direction: column;
          align-items: flex-start;
          gap: 12px;
        }

        .header-right {
          width: 100%;
          justify-content: flex-end;
        }
      }
    }

    .main-content {
      .info-card {
        .card-header {
          padding: 16px 16px 0;

          h3 {
            font-size: 14px;
          }
        }

        .card-content {
          padding: 0 16px 16px;

          .el-row {
            .el-col {
              margin-bottom: 12px;
            }
          }

          .info-item {
            margin-bottom: 12px;

            label {
              font-size: 12px;
            }

            span, p {
              font-size: 13px;
            }
          }
        }
      }
    }
  }
}

// 加载状态样式
.el-loading-mask {
  border-radius: 8px;
}

// 空状态样式
.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;

  i {
    font-size: 48px;
    margin-bottom: 16px;
    display: block;
  }

  p {
    margin: 0;
    font-size: 14px;
  }
}
</style>
