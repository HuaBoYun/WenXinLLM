<template>
  <div class="inventory-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-goods"></i>
          存货核算
        </h1>
        <p class="page-description">管理存货计价、成本结转、存货盘点等存货核算全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addInventory">
          新增存货
        </el-button>
        <el-button type="success" icon="el-icon-cpu" @click="calculateCost">
          成本计算
        </el-button>
        <el-button type="warning" icon="el-icon-view" @click="inventoryCheck">
          存货盘点
        </el-button>
      </div>
    </div>

    <!-- 存货统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-inventory">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalInventoryValue) }}</div>
              <div class="stat-label">存货总值</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon inventory-types">
              <i class="el-icon-menu"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.inventoryTypes }}</div>
              <div class="stat-label">存货品种</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon cost-transferred">
              <i class="el-icon-s-promotion"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.costTransferred) }}</div>
              <div class="stat-label">本月结转成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon turnover-rate">
              <i class="el-icon-refresh"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.turnoverRate }}</div>
              <div class="stat-label">存货周转率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 存货计价 -->
        <el-col :span="8">
          <div class="module-card inventory-valuation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-title">
                <h3>存货计价</h3>
                <span class="card-subtitle">管理存货的计价方法和价值核算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>计价方法设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>移动平均法</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>先进先出法</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>加权平均法</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageInventoryValuation">
                  管理存货计价
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本结转 -->
        <el-col :span="8">
          <div class="module-card cost-transfer">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="card-title">
                <h3>成本结转</h3>
                <span class="card-subtitle">处理存货成本的结转和分配</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>销售成本结转</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>生产成本结转</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>期间费用分摊</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本差异分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostTransfer">
                  管理成本结转
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 存货盘点 -->
        <el-col :span="8">
          <div class="module-card inventory-check">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-view"></i>
              </div>
              <div class="card-title">
                <h3>存货盘点</h3>
                <span class="card-subtitle">定期盘点存货实物与账面的一致性</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>盘点计划制定</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>盘点单生成</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>盘点结果录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>盘盈盘亏处理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageInventoryCheck">
                  管理存货盘点
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 存货分类 -->
        <el-col :span="8">
          <div class="module-card inventory-category">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-title">
                <h3>存货分类</h3>
                <span class="card-subtitle">管理存货的分类和属性设置</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>存货分类设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>存货属性管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>计量单位设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>存货编码规则</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageInventoryCategory">
                  管理存货分类
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 库存预警 -->
        <el-col :span="8">
          <div class="module-card inventory-alert">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-title">
                <h3>库存预警</h3>
                <span class="card-subtitle">设置库存预警规则和监控</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预警规则设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>库存上下限</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预警消息推送</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预警处理跟踪</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageInventoryAlert">
                  管理库存预警
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 存货分析 -->
        <el-col :span="8">
          <div class="module-card inventory-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>存货分析</h3>
                <span class="card-subtitle">分析存货结构、周转率和效益</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>存货结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>周转率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>ABC分类分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>呆滞库存分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeInventory">
                  存货分析
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
        <p>常用的存货核算操作</p>
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
  name: 'InventoryIndex',
  data() {
    return {
      stats: {
        totalInventoryValue: 0,
        inventoryTypes: 0,
        costTransferred: 0,
        turnoverRate: 0
      },
      quickActions: [
        { key: 'add', title: '新增存货', desc: '录入存货信息', icon: 'el-icon-plus' },
        { key: 'valuation', title: '存货计价', desc: '设置计价方法', icon: 'el-icon-coin' },
        { key: 'transfer', title: '成本结转', desc: '处理成本结转', icon: 'el-icon-s-promotion' },
        { key: 'check', title: '存货盘点', desc: '执行存货盘点', icon: 'el-icon-view' },
        { key: 'alert', title: '库存预警', desc: '查看库存预警', icon: 'el-icon-warning' },
        { key: 'report', title: '存货报表', desc: '生成存货报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接库存统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalInventoryValue: 0,
          inventoryTypes: 0,
          costTransferred: 0,
          turnoverRate: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    addInventory() {
      this.$router.push('/chhs/inventoryValuation')
    },
    calculateCost() {
      this.$router.push('/management/financial/inventory/valuation')
    },
    inventoryCheck() {
      this.$router.push('/management/financial/inventory/check')
    },
    manageInventoryValuation() {
      this.$router.push('/chhs/inventoryValuation')
    },
    manageCostTransfer() {
      this.$router.push('/chhs/costTransfer')
    },
    manageInventoryCheck() {
      this.$router.push('/chhs/inventoryCheck')
    },
    manageInventoryCategory() {
      this.$router.push('/chhs/inventoryCategory')
    },
    manageInventoryAlert() {
      this.$router.push('/chhs/inventoryAlert')
    },
    analyzeInventory() {
      this.$router.push('/chhs/inventoryAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        'add': '/management/financial/inventory/master',
        'valuation': '/management/financial/inventory/valuation',
        'transfer': '/management/financial/inventory/costTransfer',
        'check': '/management/financial/inventory/check',
        'alert': '/management/financial/inventory/alert',
        'report': '/management/financial/inventory/analysis'
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
.inventory-container {
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

      &.total-inventory {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.inventory-types {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.cost-transferred {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.turnover-rate {
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

    &.inventory-valuation {
      border-left: 4px solid #409eff;
    }

    &.cost-transfer {
      border-left: 4px solid #67c23a;
    }

    &.inventory-check {
      border-left: 4px solid #e6a23c;
    }

    &.inventory-category {
      border-left: 4px solid #f56c6c;
    }

    &.inventory-alert {
      border-left: 4px solid #909399;
    }

    &.inventory-analysis {
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
