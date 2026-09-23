<template>
  <div class="base-config-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-setting"></i>
          基础配置
        </h1>
        <p class="page-description">费用管理系统的基础配置中心，管理项目、标准、规则等核心配置</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshStats">
          刷新统计
        </el-button>
      </div>
    </div>

    <!-- 配置统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon project-config">
              <i class="el-icon-folder-opened"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.projectCount }}</div>
              <div class="stat-label">项目配置</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon travel-standard">
              <i class="el-icon-guide"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.travelStandardCount }}</div>
              <div class="stat-label">差旅标准</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon audit-rule">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.auditRuleCount }}</div>
              <div class="stat-label">稽核规则</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon budget-rule">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.budgetRuleCount }}</div>
              <div class="stat-label">预算规则</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 项目配置 -->
        <el-col :span="8">
          <div class="module-card project-config-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-folder-opened"></i>
              </div>
              <div class="card-title">
                <h3>项目配置</h3>
                <span class="card-subtitle">管理项目信息、成员和预算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目基本信息</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目成员管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目预算管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目结算处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/projectConfig')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 差旅标准 -->
        <el-col :span="8">
          <div class="module-card travel-standard-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-guide"></i>
              </div>
              <div class="card-title">
                <h3>差旅标准</h3>
                <span class="card-subtitle">按城市和职位级别设定差旅标准</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>城市级别标准</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>职位级别标准</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>费用类型配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>标准复制功能</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/travelStandard')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 稽核规则 -->
        <el-col :span="8">
          <div class="module-card audit-rule-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-checked"></i>
              </div>
              <div class="card-title">
                <h3>稽核规则</h3>
                <span class="card-subtitle">配置费用报销的稽核规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则条件配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则优先级</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则测试验证</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则绑定管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/auditRule')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 预算控制规则 -->
        <el-col :span="8">
          <div class="module-card budget-rule-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-title">
                <h3>预算控制规则</h3>
                <span class="card-subtitle">配置预算管控规则和策略</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算控制策略</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>超预算处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算预警设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则测试功能</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/budgetControlRule')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 单据配置 -->
        <el-col :span="8">
          <div class="module-card bill-config-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="card-title">
                <h3>单据配置</h3>
                <span class="card-subtitle">配置各类账单的识别和处理规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>单据类型配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>识别规则设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>稽核规则绑定</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>单据模板管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/billConfig')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 差旅归档 -->
        <el-col :span="8">
          <div class="module-card travel-archive-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-files"></i>
              </div>
              <div class="card-title">
                <h3>差旅归档</h3>
                <span class="card-subtitle">管理商旅服务商档案信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>服务商档案</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>价格信息管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合作协议管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>服务评价记录</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/travelArchive')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 通用标准 -->
        <el-col :span="8">
          <div class="module-card general-standard-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-board"></i>
              </div>
              <div class="card-title">
                <h3>通用标准</h3>
                <span class="card-subtitle">管理通用的费用标准配置</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>标准类型配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>级别配置管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>标准明细设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>生效期管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/generalStandard')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 费用参数 -->
        <el-col :span="8">
          <div class="module-card expense-parameter-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-tools"></i>
              </div>
              <div class="card-title">
                <h3>费用参数</h3>
                <span class="card-subtitle">配置系统运行参数</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>系统参数配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>业务参数配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>参数选项管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>参数权限控制</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/expenseParameter')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 代理委托 -->
        <el-col :span="8">
          <div class="module-card proxy-delegation-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-title">
                <h3>代理委托</h3>
                <span class="card-subtitle">管理权限代理和委托关系</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>代理关系配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>委托权限设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>生效期管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>代理记录查询</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/proxyDelegation')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 移动设置 -->
        <el-col :span="8">
          <div class="module-card mobile-settings-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-mobile-phone"></i>
              </div>
              <div class="card-title">
                <h3>移动设置</h3>
                <span class="card-subtitle">配置移动端功能和界面</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>移动端功能配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>界面布局设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>权限控制配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>消息推送设置</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/mobileSettings')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 会计规则 -->
        <el-col :span="8">
          <div class="module-card accounting-rule-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <div class="card-title">
                <h3>会计规则</h3>
                <span class="card-subtitle">配置会计核算规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>科目映射规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证生成规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>核算维度配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>规则测试验证</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/baseConfig/accountingRule')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions-panel">
      <div class="panel-header">
        <h3>
          <i class="el-icon-lightning"></i>
          快速操作
        </h3>
        <p>常用的基础配置操作</p>
      </div>

      <el-row :gutter="16">
        <el-col :span="4" v-for="action in quickActions" :key="action.key">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.desc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getBusinessDocumentStats } from '@/api/financialSharing/common'
export default {
  name: 'BaseConfigIndex',
  data() {
    return {
      stats: {
        projectCount: 0,
        travelStandardCount: 0,
        auditRuleCount: 0,
        budgetRuleCount: 0
      },
      quickActions: [
        { key: 'project', title: '项目配置', desc: '管理项目信息', icon: 'el-icon-folder-opened' },
        { key: 'travel', title: '差旅标准', desc: '设置差旅标准', icon: 'el-icon-guide' },
        { key: 'audit', title: '稽核规则', desc: '配置稽核规则', icon: 'el-icon-document-checked' },
        { key: 'budget', title: '预算规则', desc: '设置预算规则', icon: 'el-icon-coin' },
        { key: 'bill', title: '单据配置', desc: '配置单据类型', icon: 'el-icon-tickets' },
        { key: 'parameter', title: '费用参数', desc: '系统参数配置', icon: 'el-icon-s-tools' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const res = await getBusinessDocumentStats()
        if (res.code === 1 && res.data) {
          this.stats = {
            projectCount: res.data.projectCount || 0,
            travelStandardCount: res.data.travelStandardCount || 0,
            auditRuleCount: res.data.auditRuleCount || 0,
            budgetRuleCount: res.data.budgetRuleCount || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    refreshStats() {
      this.loadStats()
      this.$message.success('统计数据已刷新')
    },
    navigateTo(path) {
      this.$router.push(path)
    },
    handleQuickAction(action) {
      const routeMap = {
        'project': '/financialSharing/baseConfig/projectConfig',
        'travel': '/financialSharing/baseConfig/travelStandard',
        'audit': '/financialSharing/baseConfig/auditRule',
        'budget': '/financialSharing/baseConfig/budgetControlRule',
        'bill': '/financialSharing/baseConfig/billConfig',
        'parameter': '/financialSharing/baseConfig/expenseParameter'
      }

      const route = routeMap[action.key]
      if (route) {
        this.$router.push(route)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.base-config-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.project-config {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.travel-standard {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.audit-rule {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.budget-rule {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.function-modules {
  margin-bottom: 32px;

  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    height: 280px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.project-config-card {
      border-left: 4px solid #667eea;
    }

    &.travel-standard-card {
      border-left: 4px solid #f093fb;
    }

    &.audit-rule-card {
      border-left: 4px solid #43e97b;
    }

    &.budget-rule-card {
      border-left: 4px solid #4facfe;
    }

    &.bill-config-card {
      border-left: 4px solid #e6a23c;
    }

    &.travel-archive-card {
      border-left: 4px solid #f56c6c;
    }

    &.general-standard-card {
      border-left: 4px solid #909399;
    }

    &.expense-parameter-card {
      border-left: 4px solid #9c27b0;
    }

    &.proxy-delegation-card {
      border-left: 4px solid #67c23a;
    }

    &.mobile-settings-card {
      border-left: 4px solid #ff9800;
    }

    &.accounting-rule-card {
      border-left: 4px solid #00bcd4;
    }
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    i {
      font-size: 24px;
      color: white;
    }
  }

  .card-title {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    .card-subtitle {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-list {
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;

    i {
      color: #67c23a;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.card-actions {
  margin-top: 16px;
}

.quick-actions-panel {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    margin-bottom: 24px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .quick-action-item {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;

    &:hover {
      background: #e9ecef;
      transform: translateY(-2px);
    }

    .action-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .action-content {
      flex: 1;

      .action-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .action-desc {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>


