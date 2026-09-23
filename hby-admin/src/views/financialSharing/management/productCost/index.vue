<template>
  <div class="product-cost-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-goods"></i>
          产品成本管理
        </h1>
        <p class="page-description">管理产品成本核算、成本分析、成本控制等产品成本管理全流程</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addProduct">
          新增产品
        </el-button>
        <el-button type="success" icon="el-icon-cpu" @click="calculateCost">
          成本计算
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="costAnalysis">
          成本分析
        </el-button>
      </div>
    </div>

    <!-- 产品成本统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-products">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalProducts }}</div>
              <div class="stat-label">产品总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-cost">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalCost) }}</div>
              <div class="stat-label">总成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon unit-cost">
              <i class="el-icon-price-tag"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.avgUnitCost) }}</div>
              <div class="stat-label">平均单位成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon cost-variance">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.costVariance }}%</div>
              <div class="stat-label">成本差异率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 产品信息管理 -->
        <el-col :span="8">
          <div class="module-card product-info">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-title">
                <h3>产品信息管理</h3>
                <span class="card-subtitle">管理产品基础信息和成本结构</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>产品档案管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>BOM清单维护</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>工艺路线设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本要素配置</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageProductInfo">
                  管理产品信息
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本计算 -->
        <el-col :span="8">
          <div class="module-card cost-calculation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-title">
                <h3>成本计算</h3>
                <span class="card-subtitle">执行产品成本的自动计算和分配</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>标准成本计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>实际成本计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本分摊计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量成本计算</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostCalculation">
                  管理成本计算
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本分析 -->
        <el-col :span="8">
          <div class="module-card cost-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>成本分析</h3>
                <span class="card-subtitle">分析产品成本构成和变化趋势</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本构成分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本趋势分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本对比分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>盈利能力分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostAnalysis">
                  管理成本分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 成本控制 -->
        <el-col :span="8">
          <div class="module-card cost-control">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-title">
                <h3>成本控制</h3>
                <span class="card-subtitle">监控和控制产品成本的合理性</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本标准设定</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本差异监控</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本预警机制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本优化建议</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostControl">
                  管理成本控制
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本核算 -->
        <el-col :span="8">
          <div class="module-card cost-accounting">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-title">
                <h3>成本核算</h3>
                <span class="card-subtitle">执行产品成本的会计核算处理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本归集核算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本分配核算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>在产品核算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>完工产品核算</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostAccounting">
                  管理成本核算
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本报表 -->
        <el-col :span="8">
          <div class="module-card cost-reports">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>成本报表</h3>
                <span class="card-subtitle">生成各类产品成本报表和分析</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>产品成本明细表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本汇总报表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本差异报表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本分析报告</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostReports">
                  管理成本报表
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
        <p>常用的产品成本管理操作</p>
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
import { getCostStatisticsOverview } from '@/api/financialSharing/productCost'

export default {
  name: 'ProductCostIndex',
  data() {
    return {
      loading: false,
      stats: {
        totalProducts: 0,
        totalCost: 0,
        avgUnitCost: 0,
        costVariance: 0
      },
      quickActions: [
        { key: 'add', title: '新增产品', desc: '创建产品档案', icon: 'el-icon-plus' },
        { key: 'calculate', title: '成本计算', desc: '执行成本计算', icon: 'el-icon-cpu' },
        { key: 'analyze', title: '成本分析', desc: '分析成本数据', icon: 'el-icon-data-analysis' },
        { key: 'control', title: '成本控制', desc: '监控成本差异', icon: 'el-icon-lock' },
        { key: 'accounting', title: '成本核算', desc: '执行会计核算', icon: 'el-icon-s-finance' },
        { key: 'report', title: '成本报表', desc: '生成成本报表', icon: 'el-icon-document-copy' },
        { key: 'center', title: '成本中心', desc: '管理成本中心', icon: 'el-icon-s-grid' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      this.loading = true
      try {
        const response = await getCostStatisticsOverview({
          costingPeriod: this.getCurrentPeriod()
        })
        if (response && response.code === 1) {
          const data = response.data || {}
          this.stats = {
            totalProducts: data.totalProducts || 0,
            totalCost: data.totalCost || 0,
            avgUnitCost: data.avgUnitCost || 0,
            costVariance: data.costVariance || 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 获取当前期间
    getCurrentPeriod() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      return `${year}-${month}`
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    },
    addProduct() {
      this.$router.push('/management/productInfo')
    },
    calculateCost() {
      this.$router.push('/management/productCostAccounting')
    },
    costAnalysis() {
      this.$router.push('/management/productCostAnalysis')
    },
    manageProductInfo() {
      this.$router.push('/management/productInfo')
    },
    manageCostCalculation() {
      this.$router.push('/management/productCostAccounting')
    },
    manageCostAnalysis() {
      this.$router.push('/management/productCostAnalysis')
    },
    manageCostControl() {
      this.$router.push('/management/productCostControl')
    },
    manageCostAccounting() {
      this.$router.push('/management/productCostAccounting')
    },
    manageCostReports() {
      this.$router.push('/management/productCostReport')
    },
    handleQuickAction(action) {
      switch (action.key) {
        case 'add':
          this.addProduct()
          break
        case 'calculate':
          this.calculateCost()
          break
        case 'analyze':
          this.costAnalysis()
          break
        case 'control':
          this.$router.push('/financialSharing/management/productCost/costControl')
          break
        case 'accounting':
          this.$router.push('/financialSharing/management/productCost/costAccounting')
          break
        case 'report':
          this.$router.push('/financialSharing/management/productCost/costReport')
          break
        case 'center':
          this.$router.push('/financialSharing/management/costCenter')
          break
        default:
          this.$message.warning(`暂无"${action.title}"对应的页面路由`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.product-cost-container {
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

      &.total-products {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.unit-cost {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.cost-variance {
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

    &.product-info {
      border-left: 4px solid #409eff;
    }

    &.cost-calculation {
      border-left: 4px solid #67c23a;
    }

    &.cost-analysis {
      border-left: 4px solid #e6a23c;
    }

    &.cost-control {
      border-left: 4px solid #f56c6c;
    }

    &.cost-accounting {
      border-left: 4px solid #909399;
    }

    &.cost-reports {
      border-left: 4px solid #9c27b0;
    }

    &.cost-center {
      border-left: 4px solid #ff9800;
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
