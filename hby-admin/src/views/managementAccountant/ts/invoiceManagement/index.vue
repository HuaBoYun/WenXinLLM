<template>
  <div class="invoice-management">
    <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
      <!-- 仪表板 -->
      <el-tab-pane label="仪表板" name="dashboard">
        <template slot="label">
          <i class="el-icon-data-analysis"></i>
          仪表板
        </template>
        <InvoiceManagementDashboard />
      </el-tab-pane>

      <!-- 发票列表 -->
      <el-tab-pane label="发票列表" name="list">
        <template slot="label">
          <i class="el-icon-document"></i>
          发票列表
        </template>
        <InvoiceManagementList />
      </el-tab-pane>

      <!-- OCR识别 -->
      <el-tab-pane label="OCR识别" name="ocr">
        <template slot="label">
          <i class="el-icon-view"></i>
          OCR识别
        </template>
        <div class="tab-content">
          <InvoiceOcrManagement />
        </div>
      </el-tab-pane>

      <!-- 发票验真 -->
      <el-tab-pane label="发票验真" name="verification">
        <template slot="label">
          <i class="el-icon-check"></i>
          发票验真
        </template>
        <div class="tab-content">
          <InvoiceVerificationManagement />
        </div>
      </el-tab-pane>

      <!-- 发票归档 -->
      <el-tab-pane label="发票归档" name="archive">
        <template slot="label">
          <i class="el-icon-box"></i>
          发票归档
        </template>
        <div class="tab-content">
          <InvoiceArchiveManagement />
        </div>
      </el-tab-pane>

      <!-- 风险管理 -->
      <el-tab-pane label="风险管理" name="risk">
        <template slot="label">
          <i class="el-icon-warning"></i>
          风险管理
        </template>
        <div class="tab-content">
          <InvoiceRiskManagement />
        </div>
      </el-tab-pane>

      <!-- 统计分析 -->
      <el-tab-pane label="统计分析" name="analytics">
        <template slot="label">
          <i class="el-icon-pie-chart"></i>
          统计分析
        </template>
        <div class="tab-content">
          <InvoiceAnalytics />
        </div>
      </el-tab-pane>

      <!-- 系统设置 -->
      <el-tab-pane label="系统设置" name="settings">
        <template slot="label">
          <i class="el-icon-setting"></i>
          系统设置
        </template>
        <div class="tab-content">
          <InvoiceSystemSettings />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import InvoiceManagementDashboard from './InvoiceManagementDashboard'
import InvoiceManagementList from './InvoiceManagementList'

export default {
  name: 'InvoiceManagement',
  components: {
    InvoiceManagementDashboard,
    InvoiceManagementList,
    // 懒加载其他组件
    InvoiceOcrManagement: () => import('./InvoiceOcrManagement'),
    InvoiceVerificationManagement: () => import('./InvoiceVerificationManagement'),
    InvoiceArchiveManagement: () => import('./InvoiceArchiveManagement'),
    InvoiceRiskManagement: () => import('./InvoiceRiskManagement'),
    InvoiceAnalytics: () => import('./InvoiceAnalytics'),
    InvoiceSystemSettings: () => import('./InvoiceSystemSettings')
  },
  data() {
    return {
      activeTab: 'dashboard'
    }
  },
  mounted() {
    // 根据路由参数设置默认标签页
    this.initActiveTab()
  },
  methods: {
    // 初始化活动标签页
    initActiveTab() {
      const { tab } = this.$route.query
      if (tab && this.isValidTab(tab)) {
        this.activeTab = tab
      }
    },

    // 验证标签页是否有效
    isValidTab(tab) {
      const validTabs = ['dashboard', 'list', 'ocr', 'verification', 'archive', 'risk', 'analytics', 'settings']
      return validTabs.includes(tab)
    },

    // 标签页点击事件
    handleTabClick(tab) {
      // 更新路由查询参数
      if (this.$route.query.tab !== tab.name) {
        this.$router.replace({
          query: { ...this.$route.query, tab: tab.name }
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-management {
  .tab-content {
    padding: 20px;
    min-height: 600px;
  }

  ::v-deep .el-tabs__header {
    margin: 0 0 15px 0;
  }

  ::v-deep .el-tabs__item {
    height: 50px;
    line-height: 50px;
    
    i {
      margin-right: 5px;
    }
  }

  ::v-deep .el-tabs__content {
    padding: 0;
  }
}
</style>
