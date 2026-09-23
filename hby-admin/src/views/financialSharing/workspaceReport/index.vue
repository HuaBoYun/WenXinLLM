<template>
  <div class="workspace-report-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          工作台与报表
        </h1>
        <p class="page-description">费用管理系统的工作台与报表中心，提供个性化工作台、报表管理和费用分析功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshStats">
          刷新统计
        </el-button>
      </div>
    </div>

    <!-- 模块统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon workspace">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.workspaceCount }}</div>
              <div class="stat-label">工作台</div>
              <div class="stat-trend">
                <span class="trend-label">在线用户:</span>
                <span class="trend-value">{{ stats.onlineUserCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon report">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.reportTemplateCount }}</div>
              <div class="stat-label">报表模板</div>
              <div class="stat-trend">
                <span class="trend-label">已启用:</span>
                <span class="trend-value">{{ stats.activeTemplateCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card">
            <div class="stat-icon analysis">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.analysisCount }}</div>
              <div class="stat-label">费用分析</div>
              <div class="stat-trend">
                <span class="trend-label">本月分析:</span>
                <span class="trend-value">{{ stats.monthlyAnalysisCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <!-- 工作台模块 -->
      <div class="module-section">
        <div class="section-header">
          <h2>
            <i class="el-icon-monitor"></i>
            工作台模块
          </h2>
          <p>个性化工作台，提供待办事项、数据统计和快捷操作</p>
        </div>
        <el-row :gutter="24">
          <!-- 个人工作台 -->
          <el-col :span="8">
            <div class="module-card personal-workspace-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="card-title">
                  <h3>个人工作台</h3>
                  <span class="card-subtitle">待办事项、个人统计、快捷操作</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>待办事项集中展示</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>个人报销统计</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>快速操作入口</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>消息通知提醒</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/workspace/personal')">
                    进入工作台
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 财务工作台 -->
          <el-col :span="8">
            <div class="module-card finance-workspace-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-wallet"></i>
                </div>
                <div class="card-title">
                  <h3>财务工作台</h3>
                  <span class="card-subtitle">待审批、待支付、预算监控</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>待审批报销单</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>待支付列表</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>批量审批支付</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>预算执行监控</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/workspace/finance')">
                    进入工作台
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 管理工作台 -->
          <el-col :span="8">
            <div class="module-card management-workspace-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-data-line"></i>
                </div>
                <div class="card-title">
                  <h3>管理工作台</h3>
                  <span class="card-subtitle">财务概览、趋势分析、KPI指标</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>财务数据概览</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>趋势分析图表</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>关键指标KPI</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>风险预警列表</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/workspace/management')">
                    进入工作台
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 报表管理模块 -->
      <div class="module-section">
        <div class="section-header">
          <h2>
            <i class="el-icon-document"></i>
            报表管理模块
          </h2>
          <p>自定义报表模板、报表查询和仪表板管理</p>
        </div>
        <el-row :gutter="24">
          <!-- 报表模板管理 -->

          <el-col :span="8">
            <div class="module-card report-template-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-document-copy"></i>
                </div>
                <div class="card-title">
                  <h3>报表模板管理</h3>
                  <span class="card-subtitle">自定义报表模板配置</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>模板新增编辑</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>数据源配置</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>字段样式配置</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>模板预览发布</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/reportTemplate')">
                    进入管理
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 报表查询 -->
          <el-col :span="8">
            <div class="module-card report-query-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-search"></i>
                </div>
                <div class="card-title">
                  <h3>报表查询</h3>
                  <span class="card-subtitle">基于模板生成报表实例</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>选择报表模板</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>设置查询条件</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>生成报表数据</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>导出Excel/PDF</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/reportQuery')">
                    进入查询
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 仪表板管理 -->
          <el-col :span="8">
            <div class="module-card dashboard-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-data-board"></i>
                </div>
                <div class="card-title">
                  <h3>仪表板管理</h3>
                  <span class="card-subtitle">可视化数据仪表板</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>仪表板设计</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>图表组件配置</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>数据实时刷新</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>仪表板分享</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/dashboard')">
                    进入管理
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 费用分析模块 -->
      <div class="module-section">
        <div class="section-header">
          <h2>
            <i class="el-icon-pie-chart"></i>
            费用分析模块
          </h2>
          <p>多维度费用分析和费用报表生成</p>
        </div>
        <el-row :gutter="24">
          <!-- 费用分析 -->
          <el-col :span="12">
            <div class="module-card expense-analysis-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-data-analysis"></i>
                </div>
                <div class="card-title">
                  <h3>费用分析</h3>
                  <span class="card-subtitle">多维度费用数据分析</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>费用趋势分析</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>部门费用对比</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>费用类型占比</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>预算执行分析</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/expenseAnalysis')">
                    进入分析
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 费用报表 -->
          <el-col :span="12">
            <div class="module-card expense-report-card">
              <div class="card-header">
                <div class="card-icon">
                  <i class="el-icon-document"></i>
                </div>
                <div class="card-title">
                  <h3>费用报表</h3>
                  <span class="card-subtitle">标准费用报表生成</span>
                </div>
              </div>
              <div class="card-content">
                <div class="feature-list">
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>费用明细报表</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>费用汇总报表</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>预算对比报表</span>
                  </div>
                  <div class="feature-item">
                    <i class="el-icon-circle-check"></i>
                    <span>报表导出打印</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button type="primary" size="small" @click="navigateTo('/financialSharing/expenseReport')">
                    进入报表
                  </el-button>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions-panel">
      <div class="panel-header">
        <h3>
          <i class="el-icon-lightning"></i>
          快速操作
        </h3>
        <p>常用的工作台和报表操作</p>
      </div>

      <el-row :gutter="16">
        <el-col :span="3" v-for="action in quickActions" :key="action.key">
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
import { workspaceApi } from '@/api/financialSharing/coreBusiness'

export default {
  name: 'WorkspaceReportIndex',
  data() {
    return {
      stats: {
        workspaceCount: 0,
        onlineUserCount: 0,
        reportTemplateCount: 0,
        activeTemplateCount: 0,
        analysisCount: 0,
        monthlyAnalysisCount: 0
      },
      quickActions: [
        { key: 'personal', title: '个人工作台', desc: '我的待办', icon: 'el-icon-user' },
        { key: 'finance', title: '财务工作台', desc: '待审批支付', icon: 'el-icon-wallet' },
        { key: 'management', title: '管理工作台', desc: '数据分析', icon: 'el-icon-data-line' },
        { key: 'template', title: '报表模板', desc: '创建模板', icon: 'el-icon-document-copy' },
        { key: 'query', title: '报表查询', desc: '生成报表', icon: 'el-icon-search' },
        { key: 'dashboard', title: '仪表板', desc: '数据看板', icon: 'el-icon-data-board' },
        { key: 'analysis', title: '费用分析', desc: '分析数据', icon: 'el-icon-data-analysis' },
        { key: 'report', title: '费用报表', desc: '导出报表', icon: 'el-icon-document' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const userId = this.$store.getters.userId || ''
        const response = await workspaceApi.getWorkspaceStatistics(userId)
        if (response.code === 1 && response.data) {
          this.stats = {
            workspaceCount: response.data.workspaceCount || 0,
            onlineUserCount: response.data.onlineUserCount || 0,
            reportTemplateCount: response.data.reportTemplateCount || 0,
            activeTemplateCount: response.data.activeTemplateCount || 0,
            analysisCount: response.data.analysisCount || 0,
            monthlyAnalysisCount: response.data.monthlyAnalysisCount || 0
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
        'personal': '/financialSharing/workspace/personal',
        'finance': '/financialSharing/workspace/finance',
        'management': '/financialSharing/workspace/management',
        'template': '/financialSharing/reportTemplate',
        'query': '/financialSharing/reportQuery',
        'dashboard': '/financialSharing/dashboard',
        'analysis': '/financialSharing/expenseAnalysis',
        'report': '/financialSharing/expenseReport'
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
.workspace-report-container {
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

      &.workspace {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.report {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.analysis {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .stat-content {
      flex: 1;

      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .stat-trend {
        font-size: 12px;
        color: #606266;

        .trend-label {
          margin-right: 4px;
        }

        .trend-value {
          font-weight: 600;
          color: #409eff;
        }
      }
    }
  }
}


.function-modules {
  .module-section {
    margin-bottom: 32px;

    .section-header {
      margin-bottom: 20px;

      h2 {
        font-size: 20px;
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

      &.personal-workspace-card {
        border-left: 4px solid #667eea;
      }

      &.finance-workspace-card {
        border-left: 4px solid #f093fb;
      }

      &.management-workspace-card {
        border-left: 4px solid #43e97b;
      }

      &.report-template-card {
        border-left: 4px solid #e6a23c;
      }

      &.report-query-card {
        border-left: 4px solid #4facfe;
      }

      &.dashboard-card {
        border-left: 4px solid #9c27b0;
      }

      &.expense-analysis-card {
        border-left: 4px solid #67c23a;
      }

      &.expense-report-card {
        border-left: 4px solid #00bcd4;
      }
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
