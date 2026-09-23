<template>
  <div class="business-document-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          业务单据
        </h1>
        <p class="page-description">费用管理系统的业务单据中心，管理报销、借款、预付款、预提、预算、合同等业务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="quickCreate">
          快速创建
        </el-button>
        <el-button type="success" icon="el-icon-refresh" @click="refreshStats">
          刷新统计
        </el-button>
      </div>
    </div>

    <!-- 业务统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon expense-claim">
              <i class="el-icon-tickets"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.expenseClaimCount }}</div>
              <div class="stat-label">报销单</div>
              <div class="stat-trend">
                <span class="trend-label">待审批:</span>
                <span class="trend-value">{{ stats.pendingExpenseCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon loan">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.loanCount }}</div>
              <div class="stat-label">借款单</div>
              <div class="stat-trend">
                <span class="trend-label">待还款:</span>
                <span class="trend-value">{{ stats.pendingLoanCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon prepayment">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.prepaymentCount }}</div>
              <div class="stat-label">预付款</div>
              <div class="stat-trend">
                <span class="trend-label">待核销:</span>
                <span class="trend-value">{{ stats.pendingPrepaymentCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon contract">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.contractCount }}</div>
              <div class="stat-label">合同</div>
              <div class="stat-trend">
                <span class="trend-label">执行中:</span>
                <span class="trend-value">{{ stats.activeContractCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 报销单管理 -->
        <el-col :span="8">
          <div class="module-card expense-claim-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="card-title">
                <h3>报销单管理</h3>
                <span class="card-subtitle">费用报销全流程管理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>报销申请与审批</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>费用分摊管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>智能稽核校验</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>借款核销处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/billManagement')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 借款单管理 -->
        <el-col :span="8">
          <div class="module-card loan-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-title">
                <h3>借款单管理</h3>
                <span class="card-subtitle">员工借款申请、放款、还款、核销</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>借款申请审批</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>放款处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>还款管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>逾期提醒</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/loanManagement')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 预付款管理 -->
        <el-col :span="8">
          <div class="module-card prepayment-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-title">
                <h3>预付款管理</h3>
                <span class="card-subtitle">预付款申请、核销、退款</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预付款申请</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同关联</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>发票核销</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>退款处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/prepaymentManagement')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 费用预提管理 -->
        <el-col :span="8">
          <div class="module-card accrual-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="card-title">
                <h3>费用预提管理</h3>
                <span class="card-subtitle">费用预提、冲销</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预提申请</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>审批流程</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>会计处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>冲销管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/accrualManagement')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 预算管理 -->
        <el-col :span="8">
          <div class="module-card budget-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>预算管理</h3>
                <span class="card-subtitle">预算编制、调整、执行、分析</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算编制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算控制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算调整</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/budgetManagement')">
                  进入管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 合同管理 -->
        <el-col :span="8">
          <div class="module-card contract-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>合同管理</h3>
                <span class="card-subtitle">合同全生命周期管理、收付款计划</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同创建审批</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收付款计划</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同变更</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合同结算</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="navigateTo('/financialSharing/contractManagement')">
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
        <p>常用的业务单据操作</p>
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
  name: 'BusinessDocumentIndex',
  data() {
    return {
      stats: {
        expenseClaimCount: 0,
        pendingExpenseCount: 0,
        loanCount: 0,
        pendingLoanCount: 0,
        prepaymentCount: 0,
        pendingPrepaymentCount: 0,
        contractCount: 0,
        activeContractCount: 0
      },
      quickActions: [
        { key: 'expense', title: '报销单', desc: '创建报销单', icon: 'el-icon-tickets' },
        { key: 'loan', title: '借款单', desc: '创建借款单', icon: 'el-icon-wallet' },
        { key: 'prepayment', title: '预付款', desc: '创建预付款', icon: 'el-icon-money' },
        { key: 'accrual', title: '费用预提', desc: '创建预提单', icon: 'el-icon-edit-outline' },
        { key: 'budget', title: '预算', desc: '编制预算', icon: 'el-icon-data-analysis' },
        { key: 'contract', title: '合同', desc: '创建合同', icon: 'el-icon-document-copy' }
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
            expenseClaimCount: res.data.expenseClaimCount || 0,
            pendingExpenseCount: res.data.pendingExpenseCount || 0,
            loanCount: res.data.loanCount || 0,
            pendingLoanCount: res.data.pendingLoanCount || 0,
            prepaymentCount: res.data.prepaymentCount || 0,
            pendingPrepaymentCount: res.data.pendingPrepaymentCount || 0,
            contractCount: res.data.contractCount || 0,
            activeContractCount: res.data.activeContractCount || 0
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
    quickCreate() {
      this.$message.info('请选择要创建的单据类型')
    },
    handleQuickAction(action) {
      const routeMap = {
        'expense': '/financialSharing/billManagement',
        'loan': '/financialSharing/loanManagement',
        'prepayment': '/financialSharing/prepaymentManagement',
        'accrual': '/financialSharing/accrualManagement',
        'budget': '/financialSharing/budgetManagement',
        'contract': '/financialSharing/contractManagement'
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
.business-document-container {
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

      &.expense-claim {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.loan {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.prepayment {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.contract {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
          color: #e6a23c;
        }
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

    &.expense-claim-card {
      border-left: 4px solid #667eea;
    }

    &.loan-card {
      border-left: 4px solid #f093fb;
    }

    &.prepayment-card {
      border-left: 4px solid #43e97b;
    }

    &.accrual-card {
      border-left: 4px solid #e6a23c;
    }

    &.budget-card {
      border-left: 4px solid #4facfe;
    }

    &.contract-card {
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
