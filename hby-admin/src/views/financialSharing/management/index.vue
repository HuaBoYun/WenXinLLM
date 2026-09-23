<template>
  <div class="management-accounting-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          管理会计模块
        </h1>
        <p class="page-description">提供成本管理、预算控制、绩效分析等管理会计核心功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportReport">
          导出报表
        </el-button>
      </div>
    </div>

    <!-- 管理会计统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon cost-centers">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.costCenters }}</div>
              <div class="stat-label">成本中心</div>
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
            <div class="stat-icon budget-variance">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.budgetVariance }}%</div>
              <div class="stat-label">预算差异</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 管理会计功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 成本中心管理 -->
        <el-col :span="12">
          <div class="module-card cost-center-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-grid"></i>
              </div>
              <div class="card-title">
                <h3>成本中心管理</h3>
                <span class="card-subtitle">管理成本中心设置、成本归集、成本分摊等功能</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.costCenters }}</span>
                <span class="stat-label">个成本中心</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-setting"></i>
                  <span>成本中心设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-collection"></i>
                  <span>成本归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-s-operation"></i>
                  <span>成本分摊</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-pie-chart"></i>
                  <span>成本分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoCostCenter">
                  进入成本中心
                </el-button>
                <el-button type="text" size="small" @click="viewCostCenterStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 产品成本管理 -->
        <el-col :span="12">
          <div class="module-card product-cost-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-title">
                <h3>产品成本管理</h3>
                <span class="card-subtitle">管理产品成本核算、成本分析、成本控制等功能</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.products }}</span>
                <span class="stat-label">个产品</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-cpu"></i>
                  <span>成本计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-data-analysis"></i>
                  <span>成本分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-lock"></i>
                  <span>成本控制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-document-copy"></i>
                  <span>成本报表</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoProductCost">
                  进入产品成本
                </el-button>
                <el-button type="text" size="small" @click="viewProductCostStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 成本估算管理 -->
        <el-col :span="12">
          <div class="module-card cost-estimation-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-title">
                <h3>成本估算管理</h3>
                <span class="card-subtitle">管理成本预算、成本预测、差异分析等功能</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.budgetProjects }}</span>
                <span class="stat-label">个预算项目</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-edit"></i>
                  <span>预算编制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-view"></i>
                  <span>成本预测</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-warning"></i>
                  <span>差异分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-finished"></i>
                  <span>预算控制</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoCostEstimation">
                  进入成本估算
                </el-button>
                <el-button type="text" size="small" @click="viewCostEstimationStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 专项成本管理 -->
        <el-col :span="12">
          <div class="module-card special-cost-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="card-title">
                <h3>专项成本管理</h3>
                <span class="card-subtitle">管理项目成本、作业成本、质量成本等专项成本</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.specialProjects }}</span>
                <span class="stat-label">个专项</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid">
                <div class="feature-item">
                  <i class="el-icon-folder-opened"></i>
                  <span>项目成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-s-operation"></i>
                  <span>作业成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-medal"></i>
                  <span>质量成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-data-analysis"></i>
                  <span>专项分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoSpecialCost">
                  进入专项成本
                </el-button>
                <el-button type="text" size="small" @click="viewSpecialCostStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 内部结算管理 -->
        <el-col :span="24">
          <div class="module-card internal-settlement-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-cooperation"></i>
              </div>
              <div class="card-title">
                <h3>内部结算管理</h3>
                <span class="card-subtitle">管理内部交易、转移定价、利润中心等内部结算业务</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.internalTransactions }}</span>
                <span class="stat-label">笔内部交易</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-grid-wide">
                <div class="feature-item">
                  <i class="el-icon-s-cooperation"></i>
                  <span>内部交易</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-price-tag"></i>
                  <span>转移定价</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-trophy"></i>
                  <span>利润中心</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-s-finance"></i>
                  <span>内部结算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-data-analysis"></i>
                  <span>结算分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-document-copy"></i>
                  <span>结算报表</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="gotoInternalSettlement">
                  进入内部结算
                </el-button>
                <el-button type="text" size="small" @click="viewInternalSettlementStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCostCenterStats } from '@/api/financialSharing/costCenter'
import { getCostStatisticsOverview } from '@/api/financialSharing/productCost'
import { getInternalSettlementOverview } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'ManagementAccountingIndex',
  data() {
    return {
      stats: {
        costCenters: 0,
        totalBudget: 0,
        actualCost: 0,
        budgetVariance: 0,
        products: 0,
        budgetProjects: 0,
        specialProjects: 0,
        internalTransactions: 0
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 并行调用多个子模块统计API
        const [centerRes, productRes, settlementRes] = await Promise.allSettled([
          getCostCenterStats({}),
          getCostStatisticsOverview({}),
          getInternalSettlementOverview({})
        ])

        // 成本中心统计
        if (centerRes.status === 'fulfilled' && centerRes.value && centerRes.value.code === 1) {
          const data = centerRes.value.data || {}
          this.stats.costCenters = data.totalCenters || 0
          this.stats.totalBudget = data.totalCost || 0
          this.stats.actualCost = data.monthlyCost || 0
          this.stats.budgetVariance = data.costVariance || 0
        }

        // 产品成本统计
        if (productRes.status === 'fulfilled' && productRes.value && productRes.value.code === 1) {
          const data = productRes.value.data || {}
          this.stats.products = data.totalProducts || 0
        }

        // 内部结算统计
        if (settlementRes.status === 'fulfilled' && settlementRes.value && settlementRes.value.code === 1) {
          const data = settlementRes.value.data || {}
          this.stats.internalTransactions = data.totalSettlements || 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    refreshData() {
      this.loadStats()
      this.$message.success('数据已刷新')
    },
    exportReport() {
      try {
        const data = {
          exportTime: new Date().toLocaleString(),
          stats: this.stats
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '成本管理报表汇总.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报表导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    gotoCostCenter() {
      this.$router.push('/financialSharing/management/costCenter')
    },
    viewCostCenterStats() {
      this.$router.push('/financialSharing/management/costCenter/costAnalysis')
    },
    gotoProductCost() {
      this.$router.push('/financialSharing/management/productCost')
    },
    viewProductCostStats() {
      this.$router.push('/financialSharing/management/productCost/costAnalysis')
    },
    gotoCostEstimation() {
      this.$router.push('/financialSharing/management/costEstimation')
    },
    viewCostEstimationStats() {
      this.$router.push('/financialSharing/management/costEstimation/varianceAnalysis')
    },
    gotoSpecialCost() {
      this.$router.push('/financialSharing/management/specialCost')
    },
    viewSpecialCostStats() {
      this.$router.push('/financialSharing/management/specialCost/specialAnalysis')
    },
    gotoInternalSettlement() {
      this.$router.push('/financialSharing/management/internalSettlement')
    },
    viewInternalSettlementStats() {
      this.$router.push('/financialSharing/management/internalSettlement/settlementAnalysis')
    }
  }
}
</script>

<style lang="scss" scoped>
.management-accounting-container {
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

      &.cost-centers {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-budget {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.actual-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.budget-variance {
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
  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
    min-height: 280px;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.cost-center-card {
      border-left: 4px solid #409eff;
    }

    &.product-cost-card {
      border-left: 4px solid #67c23a;
    }

    &.cost-estimation-card {
      border-left: 4px solid #e6a23c;
    }

    &.special-cost-card {
      border-left: 4px solid #f56c6c;
    }

    &.internal-settlement-card {
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

  .card-stats {
    text-align: right;

    .stat-number {
      display: block;
      font-size: 24px;
      font-weight: 600;
      color: #409eff;
    }

    .stat-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    padding: 8px;
    background: #f8f9fa;
    border-radius: 6px;
    font-size: 14px;
    color: #606266;
    transition: all 0.3s ease;

    &:hover {
      background: #e9ecef;
    }

    i {
      color: #409eff;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.feature-grid-wide {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    padding: 8px;
    background: #f8f9fa;
    border-radius: 6px;
    font-size: 14px;
    color: #606266;
    transition: all 0.3s ease;

    &:hover {
      background: #e9ecef;
    }

    i {
      color: #409eff;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.card-actions {
  margin-top: 16px;

  .el-button {
    margin-right: 12px;
  }
}
</style>
