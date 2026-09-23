<template>
  <div class="internal-settlement-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-cooperation"></i>
          内部结算管理
        </h1>
        <p class="page-description">管理内部交易、转移定价、利润中心等内部结算业务全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createTransaction">
          新建交易
        </el-button>
        <el-button type="success" icon="el-icon-s-finance" @click="executeSettlement">
          执行结算
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="goToAnalysis">
          结算分析
        </el-button>
      </div>
    </div>

    <!-- 内部结算统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-transactions">
              <i class="el-icon-s-cooperation"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalTransactions || 0 }}</div>
              <div class="stat-label">内部交易笔数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-amount">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAmount) }}</div>
              <div class="stat-label">交易总金额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon profit-centers">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.profitCenters }}</div>
              <div class="stat-label">利润中心数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon settlement-rate">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.settlementRate }}%</div>
              <div class="stat-label">结算完成率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 内部交易 -->
        <el-col :span="8">
          <div class="module-card internal-transaction">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-cooperation"></i>
              </div>
              <div class="card-title">
                <h3>内部交易</h3>
                <span class="card-subtitle">管理企业内部各单位间的交易</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>交易单据管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>交易审批流程</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>交易确认处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>交易对账管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageInternalTransaction">
                  管理内部交易
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 转移定价 -->
        <el-col :span="8">
          <div class="module-card transfer-pricing">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-price-tag"></i>
              </div>
              <div class="card-title">
                <h3>转移定价</h3>
                <span class="card-subtitle">制定和管理内部交易的定价策略</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>定价政策制定</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>定价方法选择</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>价格调整机制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>定价效果评估</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageTransferPricing">
                  管理转移定价
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 利润中心 -->
        <el-col :span="8">
          <div class="module-card profit-center">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="card-title">
                <h3>利润中心</h3>
                <span class="card-subtitle">管理利润中心的收入成本和绩效</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>利润中心设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入成本归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>利润计算分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>绩效考核评价</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageProfitCenter">
                  管理利润中心
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 内部结算 -->
        <el-col :span="8">
          <div class="module-card settlement-process">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-title">
                <h3>内部结算</h3>
                <span class="card-subtitle">执行内部交易的结算处理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结算规则设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>自动结算处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结算凭证生成</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结算结果确认</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageSettlementProcess">
                  管理内部结算
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 资金管理 -->
        <el-col :span="8">
          <div class="module-card fund-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-title">
                <h3>资金管理</h3>
                <span class="card-subtitle">管理内部资金的调配和结算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资金池管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资金调配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>利息计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资金效率分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageFundManagement">
                  管理资金管理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 结算分析 -->
        <el-col :span="8">
          <div class="module-card settlement-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>结算分析</h3>
                <span class="card-subtitle">分析内部结算的效果和效率</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结算效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>交易结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>利润贡献分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>优化建议报告</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageSettlementAnalysis">
                  管理结算分析
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
        <p>常用的内部结算管理操作</p>
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
import {
  getInternalSettlementStats,
  getInternalTransactionStats,
  getProfitCenterStats,
  getFundManagementStats,
  getInternalSettlementOverview,
  createInternalTransaction,
  executeSettlement
} from '@/api/financialSharing/internalSettlement'

export default {
  name: 'InternalSettlementIndex',
  data() {
    return {
      loading: false,
      stats: {
        totalTransactions: 0,
        totalAmount: 0,
        profitCenters: 0,
        settlementRate: 0
      },
      quickActions: [
        { key: 'transaction', title: '内部交易', desc: '管理内部交易流程', icon: 'el-icon-s-cooperation' },
        { key: 'pricing', title: '转移定价', desc: '管理转移定价策略', icon: 'el-icon-price-tag' },
        { key: 'profit', title: '利润中心', desc: '管理利润中心绩效', icon: 'el-icon-trophy' },
        { key: 'settlement', title: '内部结算', desc: '执行内部结算处理', icon: 'el-icon-s-finance' },
        { key: 'fund', title: '资金管理', desc: '管理资金调配分配', icon: 'el-icon-money' },
        { key: 'analysis', title: '结算分析', desc: '分析结算效果优化', icon: 'el-icon-data-analysis' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        this.loading = true
        const response = await getInternalSettlementOverview({})
        if (response.code === 1) {
          const data = response.data || {}
          this.stats = {
            totalTransactions: data.totalTransactions || 0,
            totalAmount: data.totalAmount || 0,
            profitCenters: data.profitCenters || 0,
            settlementRate: data.settlementRate || 0
          }
        } else {
          this.$message.error(response.msg || '加载统计数据失败')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败')
      } finally {
        this.loading = false
      }
    },
    formatAmount(amount) {
      if (!amount) return '0万'
      return (amount / 10000).toFixed(2) + '万'
    },

    // 创建新交易
    async createTransaction() {
      try {
        this.$router.push('/management/internalTransaction')
      } catch (error) {
        console.error('导航失败:', error)
        this.$message.error('页面跳转失败')
      }
    },

    // 执行结算
    async executeSettlement() {
      try {
        this.$confirm('确认执行内部结算？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行结算...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          try {
            const result = await executeSettlement({})
            loading.close()

            if (result.success) {
              this.$message.success('结算执行成功')
              this.loadStats() // 重新加载统计数据
            } else {
              this.$message.error(result.message || '结算执行失败')
            }
          } catch (error) {
            loading.close()
            console.error('执行结算失败:', error)
            this.$message.error('执行结算失败')
          }
        })
      } catch (error) {
        console.error('执行结算操作失败:', error)
      }
    },

    // 跳转到结算分析
    goToAnalysis() {
      this.$router.push('/financialSharing/management/internalSettlement/settlementAnalysis')
    },

    // 内部交易管理
    manageInternalTransaction() {
      this.$router.push('/management/internalTransaction')
    },

    // 转移定价管理
    manageTransferPricing() {
      this.$router.push('/management/transferPricing')
    },

    // 利润中心管理
    manageProfitCenter() {
      this.$router.push('/management/profitCenter')
    },

    // 结算处理管理
    manageSettlementProcess() {
      this.$router.push('/management/settlementProcess')
    },

    // 资金管理
    manageFundManagement() {
      this.$router.push('/management/fundManagement')
    },

    // 结算分析管理
    manageSettlementAnalysis() {
      this.$router.push('/management/settlementAnalysis')
    },

    // 处理快捷操作
    handleQuickAction(action) {
      const routeMap = {
        'transaction': '/financialSharing/management/internalSettlement/internalTransaction',
        'pricing': '/financialSharing/management/internalSettlement/transferPricing',
        'profit': '/financialSharing/management/internalSettlement/profitCenter',
        'settlement': '/financialSharing/management/internalSettlement/settlementProcess',
        'fund': '/financialSharing/management/internalSettlement/fundManagement',
        'analysis': '/financialSharing/management/internalSettlement/settlementAnalysis'
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
.internal-settlement-container {
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

      &.total-transactions {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-amount {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.profit-centers {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.settlement-rate {
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

    &.internal-transaction {
      border-left: 4px solid #409eff;
    }

    &.transfer-pricing {
      border-left: 4px solid #67c23a;
    }

    &.profit-center {
      border-left: 4px solid #e6a23c;
    }

    &.settlement-process {
      border-left: 4px solid #f56c6c;
    }

    &.fund-management {
      border-left: 4px solid #909399;
    }

    &.settlement-analysis {
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
