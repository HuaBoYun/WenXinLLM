<template>
  <div class="receivables-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-money"></i>
          应收管理
        </h1>
        <p class="page-description">管理应收账款登记、收款处理、账龄分析等应收业务全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addReceivable">
          新增应收
        </el-button>
        <el-button type="success" icon="el-icon-coin" @click="recordPayment">
          登记收款
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="ageAnalysis">
          账龄分析
        </el-button>
      </div>
    </div>

    <!-- 应收统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-receivables">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalReceivables) }}</div>
              <div class="stat-label">应收总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.overdueAmount) }}</div>
              <div class="stat-label">逾期金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon collected">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.collectedAmount) }}</div>
              <div class="stat-label">本月收款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon collection-rate">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.collectionRate }}%</div>
              <div class="stat-label">回款率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 应收登记 -->
        <el-col :span="8">
          <div class="module-card receivable-register">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-edit"></i>
              </div>
              <div class="card-title">
                <h3>应收登记</h3>
                <span class="card-subtitle">登记和管理应收账款信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应收单据录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应收信息修改</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应收单据审核</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量导入应收</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageReceivableRegister">
                  管理应收登记
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 收款管理 -->
        <el-col :span="8">
          <div class="module-card payment-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-title">
                <h3>收款管理</h3>
                <span class="card-subtitle">处理客户收款和核销业务</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收款单录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收款核销</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预收款管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收款计划</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="managePayments">
                  管理收款
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 客户管理 -->
        <el-col :span="8">
          <div class="module-card customer-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-title">
                <h3>客户管理</h3>
                <span class="card-subtitle">管理客户档案和信用信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>客户档案管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>信用额度设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>客户分类管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>客户信用评估</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCustomers">
                  管理客户
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 账龄分析 -->
        <el-col :span="8">
          <div class="module-card aging-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>账龄分析</h3>
                <span class="card-subtitle">分析应收账款的账龄结构和风险</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>账龄分析表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>逾期应收统计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>坏账风险评估</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>回款趋势分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="viewAgingAnalysis">
                  查看账龄分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 坏账管理 -->
        <el-col :span="8">
          <div class="module-card bad-debt">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="card-title">
                <h3>坏账管理</h3>
                <span class="card-subtitle">管理坏账准备和坏账处理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>坏账准备计提</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>坏账核销</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>坏账回收</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>坏账政策设置</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageBadDebt">
                  管理坏账
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 应收分析 -->
        <el-col :span="8">
          <div class="module-card receivable-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>应收分析</h3>
                <span class="card-subtitle">分析应收账款的结构和变化趋势</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应收结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>客户应收排名</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>回款效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应收预警提醒</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeReceivables">
                  应收分析
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
        <p>常用的应收管理操作</p>
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
export default {
  name: 'ReceivablesIndex',
  data() {
    return {
      stats: {
        totalReceivables: 0,
        overdueAmount: 0,
        collectedAmount: 0,
        collectionRate: 0
      },
      quickActions: [
        { key: 'add', title: '新增应收', desc: '登记应收账款', icon: 'el-icon-plus' },
        { key: 'payment', title: '登记收款', desc: '处理客户收款', icon: 'el-icon-coin' },
        { key: 'aging', title: '账龄分析', desc: '查看账龄结构', icon: 'el-icon-data-analysis' },
        { key: 'customer', title: '客户管理', desc: '管理客户档案', icon: 'el-icon-user' },
        { key: 'baddebt', title: '坏账管理', desc: '处理坏账业务', icon: 'el-icon-warning-outline' },
        { key: 'report', title: '应收报表', desc: '生成应收报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接应收统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalReceivables: 0,
          overdueAmount: 0,
          collectedAmount: 0,
          collectionRate: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    addReceivable() {
      this.$router.push('/ysgl/receivableRegister')
    },
    recordPayment() {
      this.$router.push('/ysgl/collectionManagement')
    },
    ageAnalysis() {
      this.$router.push('/ysgl/agingAnalysis')
    },
    manageReceivableRegister() {
      this.$router.push('/ysgl/receivableRegister')
    },
    managePayments() {
      this.$router.push('/ysgl/collectionManagement')
    },
    manageCustomers() {
      this.$router.push('/ysgl/customerManagement')
    },
    viewAgingAnalysis() {
      this.$router.push('/ysgl/agingAnalysis')
    },
    manageBadDebt() {
      this.$router.push('/ysgl/badDebtManagement')
    },
    analyzeReceivables() {
      this.$router.push('/ysgl/receivablesAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        add: '/ysgl/receivableRegister',
        payment: '/ysgl/collectionManagement',
        aging: '/ysgl/agingAnalysis',
        customer: '/ysgl/customerManagement',
        baddebt: '/ysgl/badDebtManagement',
        report: '/ysgl/receivablesAnalysis'
      }
      const route = routeMap[action.key]
      if (route) {
        this.$router.push(route)
      } else {
        this.$message.warning(`暂无"${action.title}"对应的页面路由`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.receivables-container {
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

      &.total-receivables {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.overdue {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.collected {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.collection-rate {
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

    &.receivable-register {
      border-left: 4px solid #409eff;
    }

    &.payment-management {
      border-left: 4px solid #67c23a;
    }

    &.customer-management {
      border-left: 4px solid #e6a23c;
    }

    &.aging-analysis {
      border-left: 4px solid #f56c6c;
    }

    &.bad-debt {
      border-left: 4px solid #909399;
    }

    &.receivable-analysis {
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
