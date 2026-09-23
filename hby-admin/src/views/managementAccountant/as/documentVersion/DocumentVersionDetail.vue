<template>
  <div class="document-version-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="title-info">
          <h2 class="page-title">{{ versionData.versionName }}</h2>
          <div class="version-meta">
            <el-tag :type="getVersionStatusColor(versionData.versionStatus)" size="small">
              {{ formatVersionStatus(versionData.versionStatus) }}
            </el-tag>
            <el-tag :type="getVersionTypeColor(versionData.versionType)" size="small">
              {{ formatVersionType(versionData.versionType) }}
            </el-tag>
            <span class="version-number">{{ versionData.versionNumber }}</span>
          </div>
        </div>
      </div>
      <div class="header-right">
        <el-button-group>
          <el-button icon="el-icon-edit" @click="handleEdit" :disabled="versionData.isLocked">编辑</el-button>
          <el-button icon="el-icon-copy-document" @click="handleCopy">复制</el-button>
          <el-button icon="el-icon-share" @click="handleCreateBranch">创建分支</el-button>
          <el-button icon="el-icon-price-tag" @click="handleCreateTag">创建标签</el-button>
        </el-button-group>
        <el-dropdown @command="handleCommand" trigger="click">
          <el-button type="primary">
            更多操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="publish" :disabled="versionData.versionStatus !== 'APPROVED'">
              <i class="el-icon-upload"></i> 发布版本
            </el-dropdown-item>
            <el-dropdown-item command="lock" :disabled="versionData.isLocked">
              <i class="el-icon-lock"></i> 锁定版本
            </el-dropdown-item>
            <el-dropdown-item command="unlock" :disabled="!versionData.isLocked">
              <i class="el-icon-unlock"></i> 解锁版本
            </el-dropdown-item>
            <el-dropdown-item command="archive">
              <i class="el-icon-folder"></i> 归档版本
            </el-dropdown-item>
            <el-dropdown-item command="rollback">
              <i class="el-icon-refresh-left"></i> 回滚版本
            </el-dropdown-item>
            <el-dropdown-item divided command="delete" :disabled="versionData.isCurrent || versionData.isDefault">
              <i class="el-icon-delete"></i> 删除版本
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 版本信息卡片 -->
    <div class="version-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-card">
            <div class="card-header">
              <i class="el-icon-info"></i>
              <span>基本信息</span>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>版本编号：</label>
                <span>{{ versionData.versionCode }}</span>
              </div>
              <div class="info-item">
                <label>版本名称：</label>
                <span>{{ versionData.versionName }}</span>
              </div>
              <div class="info-item">
                <label>版本号：</label>
                <span>{{ versionData.versionNumber }}</span>
              </div>
              <div class="info-item">
                <label>版本类型：</label>
                <el-tag :type="getVersionTypeColor(versionData.versionType)" size="mini">
                  {{ formatVersionType(versionData.versionType) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>版本状态：</label>
                <el-tag :type="getVersionStatusColor(versionData.versionStatus)" size="mini">
                  {{ formatVersionStatus(versionData.versionStatus) }}
                </el-tag>
              </div>
              <div class="info-item">
                <label>当前版本：</label>
                <el-tag v-if="versionData.isCurrent" type="success" size="mini">是</el-tag>
                <span v-else>否</span>
              </div>
              <div class="info-item">
                <label>默认版本：</label>
                <el-tag v-if="versionData.isDefault" type="primary" size="mini">是</el-tag>
                <span v-else>否</span>
              </div>
              <div class="info-item">
                <label>锁定状态：</label>
                <el-tag v-if="versionData.isLocked" type="warning" size="mini">已锁定</el-tag>
                <span v-else>未锁定</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-card">
            <div class="card-header">
              <i class="el-icon-document"></i>
              <span>文档信息</span>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>文档ID：</label>
                <span>{{ versionData.documentId }}</span>
              </div>
              <div class="info-item">
                <label>文档名称：</label>
                <span>{{ versionData.documentName }}</span>
              </div>
              <div class="info-item">
                <label>文档类型：</label>
                <span>{{ versionData.documentType }}</span>
              </div>
              <div class="info-item">
                <label>文件路径：</label>
                <span>{{ versionData.filePath || '-' }}</span>
              </div>
              <div class="info-item">
                <label>文件大小：</label>
                <span>{{ formatFileSize(versionData.fileSize) }}</span>
              </div>
              <div class="info-item">
                <label>文件格式：</label>
                <span>{{ versionData.fileFormat || '-' }}</span>
              </div>
              <div class="info-item">
                <label>存储位置：</label>
                <span>{{ versionData.storageLocation || '-' }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-card">
            <div class="card-header">
              <i class="el-icon-time"></i>
              <span>时间信息</span>
            </div>
            <div class="card-content">
              <div class="info-item">
                <label>创建时间：</label>
                <span>{{ formatTime(versionData.createdTime) }}</span>
              </div>
              <div class="info-item">
                <label>创建人：</label>
                <span>{{ versionData.createdBy }}</span>
              </div>
              <div class="info-item">
                <label>更新时间：</label>
                <span>{{ formatTime(versionData.updatedTime) }}</span>
              </div>
              <div class="info-item">
                <label>更新人：</label>
                <span>{{ versionData.updatedBy || '-' }}</span>
              </div>
              <div class="info-item">
                <label>发布时间：</label>
                <span>{{ formatTime(versionData.publishTime) }}</span>
              </div>
              <div class="info-item">
                <label>发布人：</label>
                <span>{{ versionData.publisherId || '-' }}</span>
              </div>
              <div class="info-item">
                <label>生效时间：</label>
                <span>{{ formatTime(versionData.effectiveTime) }}</span>
              </div>
              <div class="info-item">
                <label>过期时间：</label>
                <span>{{ formatTime(versionData.expiryTime) }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 详细信息 -->
    <div class="detail-content">
      <el-tabs v-model="activeTab" type="card">
        <!-- 版本描述 -->
        <el-tab-pane label="版本描述" name="description">
          <div class="tab-content">
            <div class="description-section">
              <h4>版本描述</h4>
              <div class="description-content">
                {{ versionData.versionDescription || '暂无描述' }}
              </div>
            </div>
            <div class="change-section">
              <h4>变更信息</h4>
              <div class="change-info">
                <div class="change-item">
                  <label>变更类型：</label>
                  <span>{{ versionData.changeType || '-' }}</span>
                </div>
                <div class="change-item">
                  <label>变更描述：</label>
                  <span>{{ versionData.changeDescription || '-' }}</span>
                </div>
                <div class="change-item">
                  <label>变更原因：</label>
                  <span>{{ versionData.changeReason || '-' }}</span>
                </div>
                <div class="change-item">
                  <label>提交信息：</label>
                  <span>{{ versionData.commitMessage || '-' }}</span>
                </div>
                <div class="change-item">
                  <label>提交哈希：</label>
                  <span>{{ versionData.commitHash || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 版本关系 -->
        <el-tab-pane label="版本关系" name="relations">
          <div class="tab-content">
            <div class="relation-section">
              <h4>版本层次</h4>
              <div class="relation-info">
                <div class="relation-item">
                  <label>父版本：</label>
                  <el-link v-if="parentVersion" type="primary" @click="viewVersion(parentVersion.versionId)">
                    {{ parentVersion.versionName }}
                  </el-link>
                  <span v-else>-</span>
                </div>
                <div class="relation-item">
                  <label>基础版本：</label>
                  <span>{{ versionData.baseVersionId || '-' }}</span>
                </div>
                <div class="relation-item">
                  <label>分支名称：</label>
                  <span>{{ versionData.branchName || '-' }}</span>
                </div>
                <div class="relation-item">
                  <label>标签名称：</label>
                  <span>{{ versionData.tagName || '-' }}</span>
                </div>
              </div>
            </div>
            <div class="children-section">
              <h4>子版本列表</h4>
              <div v-if="childVersions.length > 0" class="children-list">
                <div v-for="child in childVersions" :key="child.versionId" class="child-item">
                  <el-link type="primary" @click="viewVersion(child.versionId)">
                    {{ child.versionName }}
                  </el-link>
                  <el-tag :type="getVersionStatusColor(child.versionStatus)" size="mini">
                    {{ formatVersionStatus(child.versionStatus) }}
                  </el-tag>
                  <span class="child-time">{{ formatTime(child.createdTime) }}</span>
                </div>
              </div>
              <div v-else class="no-data">暂无子版本</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 审批信息 -->
        <el-tab-pane label="审批信息" name="approval">
          <div class="tab-content">
            <div class="approval-section">
              <h4>审批状态</h4>
              <div class="approval-info">
                <div class="approval-item">
                  <label>审批状态：</label>
                  <el-tag :type="getApprovalStatusColor(versionData.approvalStatus)" size="mini">
                    {{ formatApprovalStatus(versionData.approvalStatus) }}
                  </el-tag>
                </div>
                <div class="approval-item">
                  <label>审批流程：</label>
                  <span>{{ versionData.approvalProcessId || '-' }}</span>
                </div>
                <div class="approval-item">
                  <label>审批人：</label>
                  <span>{{ versionData.approverId || '-' }}</span>
                </div>
                <div class="approval-item">
                  <label>审批时间：</label>
                  <span>{{ formatTime(versionData.approvalTime) }}</span>
                </div>
                <div class="approval-item">
                  <label>审批意见：</label>
                  <span>{{ versionData.approvalComment || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 权限信息 -->
        <el-tab-pane label="权限信息" name="permissions">
          <div class="tab-content">
            <div class="permission-section">
              <h4>访问控制</h4>
              <div class="permission-info">
                <div class="permission-item">
                  <label>访问级别：</label>
                  <el-tag :type="getAccessLevelColor(versionData.accessLevel)" size="mini">
                    {{ formatAccessLevel(versionData.accessLevel) }}
                  </el-tag>
                </div>
                <div class="permission-item">
                  <label>机密级别：</label>
                  <span>{{ versionData.confidentialityLevel || '-' }}</span>
                </div>
                <div class="permission-item">
                  <label>权限配置：</label>
                  <span>{{ versionData.permissionConfig || '-' }}</span>
                </div>
              </div>
            </div>
            <div class="permission-list-section">
              <h4>权限列表</h4>
              <div v-if="permissions.length > 0" class="permission-list">
                <div v-for="permission in permissions" :key="permission.id" class="permission-list-item">
                  <span class="user-name">{{ permission.userName }}</span>
                  <el-tag size="mini">{{ permission.permission }}</el-tag>
                  <span class="grant-time">{{ formatTime(permission.grantTime) }}</span>
                </div>
              </div>
              <div v-else class="no-data">暂无权限配置</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 统计信息 -->
        <el-tab-pane label="统计信息" name="statistics">
          <div class="tab-content">
            <div class="stats-section">
              <h4>访问统计</h4>
              <div class="stats-info">
                <div class="stats-item">
                  <label>下载次数：</label>
                  <span>{{ versionData.downloadCount || 0 }}</span>
                </div>
                <div class="stats-item">
                  <label>查看次数：</label>
                  <span>{{ versionData.viewCount || 0 }}</span>
                </div>
                <div class="stats-item">
                  <label>编辑次数：</label>
                  <span>{{ versionData.editCount || 0 }}</span>
                </div>
                <div class="stats-item">
                  <label>最后访问：</label>
                  <span>{{ formatTime(versionData.lastAccessTime) }}</span>
                </div>
              </div>
            </div>
            <div class="quality-section">
              <h4>质量信息</h4>
              <div class="quality-info">
                <div class="quality-item">
                  <label>质量评分：</label>
                  <span>{{ versionData.qualityScore || '-' }}</span>
                </div>
                <div class="quality-item">
                  <label>质量等级：</label>
                  <span>{{ versionData.qualityGrade || '-' }}</span>
                </div>
                <div class="quality-item">
                  <label>合规状态：</label>
                  <span>{{ versionData.complianceStatus || '-' }}</span>
                </div>
                <div class="quality-item">
                  <label>风险级别：</label>
                  <span>{{ versionData.riskLevel || '-' }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getVersionById,
  getParentVersion,
  getChildVersions,
  getVersionPermissions,
  copyVersion,
  createBranch,
  createTag,
  publishVersion,
  lockVersion,
  unlockVersion,
  archiveVersion,
  rollbackToVersion,
  deleteVersion,
  formatVersionStatus,
  formatVersionType,
  formatFileSize,
  formatAccessLevel
} from '@/api/managementAccountant/as/documentVersion'

export default {
  name: 'DocumentVersionDetail',
  data() {
    return {
      versionId: '',
      versionData: {},
      parentVersion: null,
      childVersions: [],
      permissions: [],
      activeTab: 'description',
      loading: false
    }
  },
  created() {
    this.versionId = this.$route.params.id
    this.loadVersionDetail()
  },
  methods: {
    // 加载版本详情
    async loadVersionDetail() {
      this.loading = true
      try {
        const [versionRes, parentRes, childrenRes, permissionsRes] = await Promise.all([
          getVersionById(this.versionId),
          getParentVersion(this.versionId),
          getChildVersions(this.versionId),
          getVersionPermissions(this.versionId)
        ])

        this.versionData = versionRes.data || {}
        this.parentVersion = parentRes.data
        this.childVersions = childrenRes.data || []
        this.permissions = permissionsRes.data || []
      } catch (error) {
        this.$message.error('加载版本详情失败: ' + error.message)
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
      this.$router.push(`/managementAccountant/as/documentVersion/edit/${this.versionId}`)
    },

    // 复制版本
    async handleCopy() {
      try {
        const { value } = await this.$prompt('请输入新版本名称', '复制版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: this.versionData.versionName + '_copy',
          inputPattern: /.+/,
          inputErrorMessage: '版本名称不能为空'
        })
        
        await copyVersion(this.versionId, value)
        this.$message.success('复制版本成功')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复制版本失败: ' + error.message)
        }
      }
    },

    // 创建分支
    async handleCreateBranch() {
      try {
        const { value } = await this.$prompt('请输入分支名称', '创建分支', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '分支名称不能为空'
        })
        
        await createBranch(this.versionId, value, '从版本创建分支')
        this.$message.success('创建分支成功')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('创建分支失败: ' + error.message)
        }
      }
    },

    // 创建标签
    async handleCreateTag() {
      try {
        const { value } = await this.$prompt('请输入标签名称', '创建标签', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '标签名称不能为空'
        })
        
        await createTag(this.versionId, value, '版本标签')
        this.$message.success('创建标签成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('创建标签失败: ' + error.message)
        }
      }
    },

    // 处理更多操作
    async handleCommand(command) {
      switch (command) {
        case 'publish':
          await this.handlePublish()
          break
        case 'lock':
          await this.handleLock()
          break
        case 'unlock':
          await this.handleUnlock()
          break
        case 'archive':
          await this.handleArchive()
          break
        case 'rollback':
          await this.handleRollback()
          break
        case 'delete':
          await this.handleDelete()
          break
      }
    },

    // 发布版本
    async handlePublish() {
      try {
        await this.$confirm('确认发布此版本吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await publishVersion(this.versionId)
        this.$message.success('发布版本成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('发布版本失败: ' + error.message)
        }
      }
    },

    // 锁定版本
    async handleLock() {
      try {
        const { value } = await this.$prompt('请输入锁定原因', '锁定版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '锁定原因不能为空'
        })
        
        await lockVersion(this.versionId, value)
        this.$message.success('锁定版本成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('锁定版本失败: ' + error.message)
        }
      }
    },

    // 解锁版本
    async handleUnlock() {
      try {
        await this.$confirm('确认解锁此版本吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await unlockVersion(this.versionId)
        this.$message.success('解锁版本成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('解锁版本失败: ' + error.message)
        }
      }
    },

    // 归档版本
    async handleArchive() {
      try {
        const { value } = await this.$prompt('请输入归档原因', '归档版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '归档原因不能为空'
        })
        
        await archiveVersion(this.versionId, value)
        this.$message.success('归档版本成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('归档版本失败: ' + error.message)
        }
      }
    },

    // 回滚版本
    async handleRollback() {
      try {
        const { value } = await this.$prompt('请输入回滚原因', '回滚版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '回滚原因不能为空'
        })
        
        await rollbackToVersion(this.versionData.documentId, this.versionId, value)
        this.$message.success('回滚版本成功')
        this.loadVersionDetail()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('回滚版本失败: ' + error.message)
        }
      }
    },

    // 删除版本
    async handleDelete() {
      try {
        await this.$confirm('确认删除此版本吗？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteVersion(this.versionId)
        this.$message.success('删除版本成功')
        this.$router.push('/managementAccountant/as/documentVersion/list')
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除版本失败: ' + error.message)
        }
      }
    },

    // 查看版本
    viewVersion(versionId) {
      this.$router.push(`/managementAccountant/as/documentVersion/detail/${versionId}`)
    },

    // 工具方法
    formatTime(time) {
      if (!time) return '-'
      return this.$moment(time).format('YYYY-MM-DD HH:mm:ss')
    },

    formatVersionStatus,
    formatVersionType,
    formatFileSize,
    formatAccessLevel,

    getVersionStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'PUBLISHED': 'primary',
        'ARCHIVED': 'info',
        'DEPRECATED': 'danger',
        'DELETED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    getVersionTypeColor(type) {
      const colorMap = {
        'MAJOR': 'danger',
        'MINOR': 'warning',
        'PATCH': 'success',
        'DRAFT': 'info'
      }
      return colorMap[type] || 'info'
    },

    getApprovalStatusColor(status) {
      const colorMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return colorMap[status] || 'info'
    },

    getAccessLevelColor(level) {
      const colorMap = {
        'PUBLIC': 'success',
        'INTERNAL': 'primary',
        'CONFIDENTIAL': 'warning',
        'SECRET': 'danger',
        'TOP_SECRET': 'danger'
      }
      return colorMap[level] || 'info'
    },

    formatApprovalStatus(status) {
      const statusMap = {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.document-version-detail {
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
      display: flex;
      align-items: center;

      .title-info {
        margin-left: 16px;

        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
        }

        .version-meta {
          display: flex;
          align-items: center;
          gap: 8px;

          .version-number {
            color: #909399;
            font-size: 14px;
          }
        }
      }
    }
  }

  .version-cards {
    margin-bottom: 20px;

    .info-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      height: 100%;

      .card-header {
        padding: 16px 20px;
        border-bottom: 1px solid #EBEEF5;
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .card-content {
        padding: 20px;

        .info-item {
          display: flex;
          align-items: center;
          margin-bottom: 12px;

          &:last-child {
            margin-bottom: 0;
          }

          label {
            width: 80px;
            color: #909399;
            font-size: 14px;
            flex-shrink: 0;
          }

          span {
            color: #303133;
            font-size: 14px;
          }
        }
      }
    }
  }

  .detail-content {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .tab-content {
      padding: 20px;

      h4 {
        margin: 0 0 16px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        border-left: 3px solid #409EFF;
        padding-left: 12px;
      }

      .description-section,
      .change-section,
      .relation-section,
      .children-section,
      .approval-section,
      .permission-section,
      .permission-list-section,
      .stats-section,
      .quality-section {
        margin-bottom: 24px;

        &:last-child {
          margin-bottom: 0;
        }
      }

      .description-content {
        padding: 16px;
        background: #F5F7FA;
        border-radius: 4px;
        color: #303133;
        line-height: 1.6;
      }

      .change-info,
      .relation-info,
      .approval-info,
      .permission-info,
      .stats-info,
      .quality-info {
        .change-item,
        .relation-item,
        .approval-item,
        .permission-item,
        .stats-item,
        .quality-item {
          display: flex;
          align-items: center;
          margin-bottom: 12px;

          &:last-child {
            margin-bottom: 0;
          }

          label {
            width: 100px;
            color: #909399;
            font-size: 14px;
            flex-shrink: 0;
          }

          span {
            color: #303133;
            font-size: 14px;
          }
        }
      }

      .children-list {
        .child-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #EBEEF5;

          &:last-child {
            border-bottom: none;
          }

          .el-link {
            margin-right: 12px;
          }

          .el-tag {
            margin-right: 12px;
          }

          .child-time {
            color: #C0C4CC;
            font-size: 12px;
            margin-left: auto;
          }
        }
      }

      .permission-list {
        .permission-list-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #EBEEF5;

          &:last-child {
            border-bottom: none;
          }

          .user-name {
            margin-right: 12px;
            font-weight: 500;
          }

          .el-tag {
            margin-right: 12px;
          }

          .grant-time {
            color: #C0C4CC;
            font-size: 12px;
            margin-left: auto;
          }
        }
      }

      .no-data {
        text-align: center;
        color: #C0C4CC;
        padding: 40px 0;
      }
    }
  }
}
</style>
