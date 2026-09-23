<template>
  <div class="payables-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-wallet"></i>
          应付管理
        </h1>
        <p class="page-description">管理应付账款登记、付款处理、供应商管理等应付业务全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addPayable">
          新增应付
        </el-button>
        <el-button type="success" icon="el-icon-money" @click="recordPayment">
          登记付款
        </el-button>
        <el-button type="warning" icon="el-icon-user" @click="manageSuppliers">
          供应商管理
        </el-button>
      </div>
    </div>

    <!-- 应付统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-payables">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalPayables) }}</div>
              <div class="stat-label">应付总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon due-soon">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.dueSoonAmount) }}</div>
              <div class="stat-label">即将到期</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon paid">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.paidAmount) }}</div>
              <div class="stat-label">本月付款</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon suppliers">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.supplierCount }}</div>
              <div class="stat-label">供应商数量</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 应付登记 -->
        <el-col :span="8">
          <div class="module-card payable-register">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-edit"></i>
              </div>
              <div class="card-title">
                <h3>应付登记</h3>
                <span class="card-subtitle">登记和管理应付账款信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应付单据录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应付信息修改</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应付单据审核</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量导入应付</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="managePayableRegister">
                  管理应付登记
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 付款管理 -->
        <el-col :span="8">
          <div class="module-card payment-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-title">
                <h3>付款管理</h3>
                <span class="card-subtitle">处理供应商付款和核销业务</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>付款单录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>付款核销</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预付款管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>付款计划</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="managePayments">
                  管理付款
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 供应商管理 -->
        <el-col :span="8">
          <div class="module-card supplier-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-title">
                <h3>供应商管理</h3>
                <span class="card-subtitle">管理供应商档案和合作信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>供应商档案管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>供应商分类管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>供应商评估</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>合作协议管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageSupplierInfo">
                  管理供应商
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 账期管理 -->
        <el-col :span="8">
          <div class="module-card payment-terms">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-date"></i>
              </div>
              <div class="card-title">
                <h3>账期管理</h3>
                <span class="card-subtitle">管理应付账款的账期和到期提醒</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>账期设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>到期提醒</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>逾期统计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>付款计划制定</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="managePaymentTerms">
                  管理账期
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 票据管理 -->
        <el-col :span="8">
          <div class="module-card bill-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>票据管理</h3>
                <span class="card-subtitle">管理应付票据和票据背书</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应付票据登记</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>票据到期管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>票据背书转让</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>票据兑付处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageBills">
                  管理票据
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 应付分析 -->
        <el-col :span="8">
          <div class="module-card payable-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>应付分析</h3>
                <span class="card-subtitle">分析应付账款的结构和变化趋势</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>应付结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>供应商应付排名</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>付款效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>现金流预测</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzePayables">
                  应付分析
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
        <p>常用的应付管理操作</p>
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
  name: 'PayablesIndex',
  data() {
    return {
      stats: {
        totalPayables: 0,
        dueSoonAmount: 0,
        paidAmount: 0,
        supplierCount: 0
      },
      quickActions: [
        { key: 'add', title: '新增应付', desc: '登记应付账款', icon: 'el-icon-plus' },
        { key: 'payment', title: '登记付款', desc: '处理供应商付款', icon: 'el-icon-money' },
        { key: 'supplier', title: '供应商管理', desc: '管理供应商档案', icon: 'el-icon-user' },
        { key: 'terms', title: '账期管理', desc: '管理付款账期', icon: 'el-icon-date' },
        { key: 'bill', title: '票据管理', desc: '管理应付票据', icon: 'el-icon-document-copy' },
        { key: 'report', title: '应付报表', desc: '生成应付报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接应付统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalPayables: 0,
          dueSoonAmount: 0,
          paidAmount: 0,
          supplierCount: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    addPayable() {
      this.$router.push('/yfgl/payableRegister')
    },
    recordPayment() {
      this.$router.push('/financialSharing/financial/payables/paymentManagement')
    },
    manageSuppliers() {
      this.$router.push('/financialSharing/financial/payables/supplierManagement')
    },
    managePayableRegister() {
      this.$router.push('/yfgl/payableRegister')
    },
    managePayments() {
      this.$router.push('/yfgl/paymentManagement')
    },
    manageSupplierInfo() {
      this.$router.push('/yfgl/supplierManagement')
    },
    managePaymentTerms() {
      this.$router.push('/yfgl/paymentTerms')
    },
    manageBills() {
      this.$router.push('/yfgl/billManagement')
    },
    analyzePayables() {
      this.$router.push('/yfgl/payableAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        add: '/yfgl/payableRegister',
        payment: '/financialSharing/financial/payables/paymentManagement',
        supplier: '/financialSharing/financial/payables/supplierManagement',
        terms: '/yfgl/paymentTerms',
        bill: '/yfgl/billManagement',
        report: '/yfgl/payableAnalysis'
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
.payables-container {
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

      &.total-payables {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.due-soon {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.paid {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.suppliers {
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

    &.payable-register {
      border-left: 4px solid #409eff;
    }

    &.payment-management {
      border-left: 4px solid #67c23a;
    }

    &.supplier-management {
      border-left: 4px solid #e6a23c;
    }

    &.payment-terms {
      border-left: 4px solid #f56c6c;
    }

    &.bill-management {
      border-left: 4px solid #909399;
    }

    &.payable-analysis {
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
