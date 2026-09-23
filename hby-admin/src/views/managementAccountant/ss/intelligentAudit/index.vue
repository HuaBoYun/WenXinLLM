<template>
  <div class="intelligent-audit-index">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-cpu"></i>
            智能审核
          </h2>
          <p class="page-description">基于AI技术的智能化审核管理系统，提供自动化审核流程和风险预测</p>
        </div>
        <div class="header-right">
          <el-button-group>
            <el-button
              :type="$route.name === 'IntelligentAuditDashboard' ? 'primary' : 'default'"
              icon="el-icon-data-analysis"
              @click="navigateTo('dashboard')"
            >
              仪表板
            </el-button>
            <el-button
              :type="$route.name === 'IntelligentAuditList' ? 'primary' : 'default'"
              icon="el-icon-document"
              @click="navigateTo('list')"
            >
              审核列表
            </el-button>
          </el-button-group>
        </div>
      </div>
    </div>

    <!-- 快捷操作栏 -->
    <div class="quick-actions" v-if="$route.name === 'IntelligentAuditDashboard' || $route.name === 'IntelligentAuditList'">
      <el-card shadow="never" class="action-card">
        <div class="actions-content">
          <div class="actions-left">
            <el-tag type="info" size="small">快捷操作</el-tag>
          </div>
          <div class="actions-right">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="small"
              @click="createAudit"
            >
              新建审核
            </el-button>
            <el-button
              type="success"
              icon="el-icon-cpu"
              size="small"
              @click="batchExecute"
            >
              批量执行
            </el-button>
            <el-button
              type="warning"
              icon="el-icon-magic-stick"
              size="small"
              @click="intelligentRecommend"
            >
              智能推荐
            </el-button>
            <el-button
              type="info"
              icon="el-icon-download"
              size="small"
              @click="exportData"
            >
              导出数据
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 面包屑导航 -->
    <div class="breadcrumb-container" v-if="showBreadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ name: 'IntelligentAuditDashboard' }">智能审核</el-breadcrumb-item>
        <el-breadcrumb-item v-if="$route.name === 'IntelligentAuditList'">审核列表</el-breadcrumb-item>
        <el-breadcrumb-item v-if="$route.name === 'IntelligentAuditDetail'">审核详情</el-breadcrumb-item>
        <el-breadcrumb-item v-if="$route.name === 'IntelligentAuditCreate'">新建审核</el-breadcrumb-item>
        <el-breadcrumb-item v-if="$route.name === 'IntelligentAuditEdit'">编辑审核</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'IntelligentAuditIndex',
  computed: {
    showBreadcrumb() {
      return ['IntelligentAuditList', 'IntelligentAuditDetail', 'IntelligentAuditCreate', 'IntelligentAuditEdit'].includes(this.$route.name)
    }
  },
  methods: {
    navigateTo(path) {
      this.$router.push(`/management-accountant/ss/intelligent-audit/${path}`)
    },
    createAudit() {
      this.$router.push('/management-accountant/ss/intelligent-audit/create')
    },
    batchExecute() {
      this.$message.info('批量执行功能开发中...')
    },
    intelligentRecommend() {
      this.$message.info('智能推荐功能开发中...')
    },
    exportData() {
      this.$message.info('数据导出功能开发中...')
    }
  }
}
</script>

<style scoped>
.intelligent-audit-index {
  height: 100%;
  background-color: #f5f7fa;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 24px;
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.header-left .page-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left .page-description {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
  line-height: 1.5;
}

.quick-actions {
  margin-bottom: 20px;
  padding: 0 24px;
}

.action-card {
  border-radius: 8px;
}

.actions-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.actions-right {
  display: flex;
  gap: 8px;
}

.breadcrumb-container {
  padding: 0 24px 16px;
}

.main-content {
  padding: 0 24px 24px;
  min-height: calc(100vh - 200px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .actions-content {
    flex-direction: column;
    gap: 12px;
  }

  .actions-right {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
