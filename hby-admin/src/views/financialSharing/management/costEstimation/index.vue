<template>
  <div class="cost-estimation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-line"></i>
          成本估算管理
        </h1>
        <p class="page-description">管理成本预算编制、成本预测、差异分析等成本估算全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="navigateToSchemeManagement">
          估算方案
        </el-button>
        <el-button type="success" icon="el-icon-line-chart" @click="navigateToCostSimulation">
          成本模拟
        </el-button>
        <el-button type="warning" icon="el-icon-bar-chart" @click="navigateToVarianceAnalysis">
          差异分析
        </el-button>
      </div>
    </div>

    <!-- 成本估算统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon budget-projects">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.budgetProjects }}</div>
              <div class="stat-label">预算项目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-budget">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalBudget) }}</div>
              <div class="stat-label">总预算</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon actual-cost">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.actualCost) }}</div>
              <div class="stat-label">实际成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon variance-rate">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.varianceRate }}%</div>
              <div class="stat-label">差异率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 估算方案管理 -->
        <el-col :span="8">
          <div class="module-card scheme-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>估算方案管理</h3>
                <span class="card-subtitle">管理成本估算方案和模板</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>方案创建与编辑</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>方案模板管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>方案版本控制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>方案审批流程</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToSchemeManagement">
                  管理估算方案
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本模拟 -->
        <el-col :span="8">
          <div class="module-card cost-simulation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-title">
                <h3>成本模拟</h3>
                <span class="card-subtitle">基于模型进行成本模拟计算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>多场景模拟</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>参数敏感性分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>蒙特卡洛模拟</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结果可视化</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToCostSimulation">
                  管理成本模拟
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 差异分析 -->
        <el-col :span="8">
          <div class="module-card variance-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-bar-chart"></i>
              </div>
              <div class="card-title">
                <h3>差异分析</h3>
                <span class="card-subtitle">分析预算与实际成本的差异原因</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算差异分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>标准差异分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>差异原因分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>改进措施建议</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToVarianceAnalysis">
                  管理差异分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 预算编制 -->
        <el-col :span="8">
          <div class="module-card budget-preparation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-title">
                <h3>预算编制</h3>
                <span class="card-subtitle">制定和管理各类成本预算计划</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>年度预算编制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>季度预算编制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目预算编制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算审批流程</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToBudgetPreparation">
                  管理预算编制
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本模型 -->
        <el-col :span="8">
          <div class="module-card cost-model">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-title">
                <h3>成本模型</h3>
                <span class="card-subtitle">建立和维护成本估算模型</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本驱动因子</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本函数建模</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模型参数校准</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模型有效性验证</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToCostModel">
                  管理成本模型
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 估算报告 -->
        <el-col :span="8">
          <div class="module-card estimation-reports">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>估算报告</h3>
                <span class="card-subtitle">生成各类成本估算分析报告</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算执行报告</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本预测报告</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>差异分析报告</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本趋势报告</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateToEstimationReport">
                  管理估算报告
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
        <p>常用的成本估算管理操作</p>
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

    <!-- 子路由渲染区域 -->
    <router-view />
  </div>
</template>

<script>
import { getCostEstimateSummary } from '@/api/financialSharing/costEstimation'

export default {
  name: 'CostEstimationIndex',
  data() {
    return {
      stats: {
        budgetProjects: 0,
        totalBudget: 0,
        actualCost: 0,
        varianceRate: 0
      },
      quickActions: [
        { key: 'budget', title: '新建预算', desc: '创建预算项目', icon: 'el-icon-plus' },
        { key: 'forecast', title: '成本预测', desc: '执行成本预测', icon: 'el-icon-view' },
        { key: 'variance', title: '差异分析', desc: '分析预算差异', icon: 'el-icon-warning' },
        { key: 'control', title: '预算控制', desc: '监控预算执行', icon: 'el-icon-finished' },
        { key: 'model', title: '成本模型', desc: '管理成本模型', icon: 'el-icon-cpu' },
        { key: 'report', title: '估算报告', desc: '生成分析报告', icon: 'el-icon-document-copy' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const response = await getCostEstimateSummary({})
        if (response && response.code === 1) {
          const data = response.data || {}
          this.stats = {
            budgetProjects: data.budgetProjects || data.BUDGETPROJECTS || 0,
            totalBudget: Number(data.totalBudget || data.TOTALBUDGET || 0),
            actualCost: Number(data.actualCost || data.ACTUALCOST || 0),
            varianceRate: Number(data.varianceRate || data.VARIANCERATE || 0)
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    // 导航到各个子模块
    navigateToSchemeManagement() {
      this.$router.push('/management/schemeManagement')
    },
    navigateToCostSimulation() {
      this.$router.push('/management/costSimulation')
    },
    navigateToVarianceAnalysis() {
      this.$router.push('/management/varianceAnalysis')
    },
    navigateToBudgetPreparation() {
      this.$router.push('/management/budgetPreparation')
    },
    navigateToCostModel() {
      this.$router.push('/management/costModel')
    },
    navigateToEstimationReport() {
      this.$router.push('/management/estimationReport')
    },

    // 兼容旧的方法名
    createBudget() {
      this.navigateToBudgetPreparation()
    },
    costForecast() {
      this.navigateToCostSimulation()
    },
    varianceAnalysis() {
      this.navigateToVarianceAnalysis()
    },
    manageBudgetPreparation() {
      this.navigateToBudgetPreparation()
    },
    manageCostForecast() {
      this.navigateToCostSimulation()
    },
    manageVarianceAnalysis() {
      this.navigateToVarianceAnalysis()
    },
    manageBudgetControl() {
      this.navigateToBudgetPreparation()
    },
    manageCostModel() {
      this.navigateToCostModel()
    },
    manageEstimationReports() {
      this.navigateToEstimationReport()
    },
    handleQuickAction(action) {
      // 根据action.key导航到对应页面
      switch (action.key) {
        case 'scheme':
          this.navigateToSchemeManagement()
          break
        case 'simulation':
          this.navigateToCostSimulation()
          break
        case 'variance':
          this.navigateToVarianceAnalysis()
          break
        case 'budget':
          this.navigateToBudgetPreparation()
          break
        case 'model':
          this.navigateToCostModel()
          break
        case 'report':
          this.navigateToEstimationReport()
          break
        default:
          this.$message.warning(`暂无"${action.title}"对应的页面路由`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-estimation-container {
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

      &.budget-projects {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-budget {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.actual-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.variance-rate {
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

    &.budget-preparation {
      border-left: 4px solid #409eff;
    }

    &.cost-forecast {
      border-left: 4px solid #67c23a;
    }

    &.variance-analysis {
      border-left: 4px solid #e6a23c;
    }

    &.budget-control {
      border-left: 4px solid #f56c6c;
    }

    &.cost-model {
      border-left: 4px solid #909399;
    }

    &.estimation-reports {
      border-left: 4px solid #9c27b0;
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
