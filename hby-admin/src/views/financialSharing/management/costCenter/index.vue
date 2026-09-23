<template>
  <div class="cost-center-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-grid"></i>
          成本中心管理
        </h1>
        <p class="page-description">管理成本中心设置、成本归集、成本分摊等成本管理核心功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addCostCenter">
          新增成本中心
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport" :loading="exportLoading">
          导出数据
        </el-button>
        <el-button type="info" icon="el-icon-s-operation" @click="costAllocation">
          成本分摊
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="costAnalysis">
          成本分析
        </el-button>
      </div>
    </div>

    <!-- 成本中心统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-centers">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalCenters }}</div>
              <div class="stat-label">成本中心数量</div>
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
            <div class="stat-icon monthly-cost">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.monthlyCost) }}</div>
              <div class="stat-label">本月成本</div>
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
        <!-- 成本中心设置 -->
        <el-col :span="8">
          <div class="module-card center-setup">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-setting"></i>
              </div>
              <div class="card-title">
                <h3>成本中心设置</h3>
                <span class="card-subtitle">设置和管理成本中心的基础信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本中心创建</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>层级结构设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>责任人分配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本类型配置</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCenterSetup">
                  管理成本中心
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本归集 -->
        <el-col :span="8">
          <div class="module-card cost-collection">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-collection"></i>
              </div>
              <div class="card-title">
                <h3>成本归集</h3>
                <span class="card-subtitle">将各项成本费用归集到相应成本中心</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>直接成本归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>间接成本归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>自动归集规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>归集结果审核</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostCollection">
                  管理成本归集
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本分摊 -->
        <el-col :span="8">
          <div class="module-card cost-allocation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-title">
                <h3>成本分摊</h3>
                <span class="card-subtitle">按照分摊规则将成本分配到各成本对象</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分摊规则设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分摊基础维护</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>自动分摊计算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分摊结果调整</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostAllocation">
                  管理成本分摊
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 成本预算 -->
        <el-col :span="8">
          <div class="module-card cost-budget">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>成本预算</h3>
                <span class="card-subtitle">制定和管理成本中心的预算计划</span>
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
                  <span>预算审批</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算执行监控</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预算调整</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageCostBudget">
                  管理成本预算
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 成本控制 -->
        <el-col :span="8">
          <div class="module-card cost-control">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-lock"></i>
              </div>
              <div class="card-title">
                <h3>成本控制</h3>
                <span class="card-subtitle">监控和控制成本中心的成本支出</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本限额设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>超支预警</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本审批控制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>控制效果评估</span>
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

        <!-- 成本分析 -->
        <el-col :span="8">
          <div class="module-card cost-analysis-module">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-title">
                <h3>成本分析</h3>
                <span class="card-subtitle">分析成本中心的成本结构和变化趋势</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本趋势分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本差异分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本绩效评价</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeCostData">
                  成本分析
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
        <p>常用的成本中心管理操作</p>
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
import { getCostCenterStats, exportCostCenterList } from '@/api/financialSharing/costCenter'

export default {
  name: 'CostCenterIndex',
  data() {
    return {
      exportLoading: false,
      stats: {
        totalCenters: 0,
        totalCost: 0,
        monthlyCost: 0,
        costVariance: 0
      },
      quickActions: [
        { key: 'add', title: '新增成本中心', desc: '创建成本中心', icon: 'el-icon-plus' },
        { key: 'collect', title: '成本归集', desc: '归集成本费用', icon: 'el-icon-collection' },
        { key: 'allocate', title: '成本分摊', desc: '执行成本分摊', icon: 'el-icon-s-operation' },
        { key: 'budget', title: '成本预算', desc: '制定成本预算', icon: 'el-icon-document-copy' },
        { key: 'control', title: '成本控制', desc: '监控成本支出', icon: 'el-icon-lock' },
        { key: 'report', title: '成本报表', desc: '生成成本报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const response = await getCostCenterStats()
        if (response && response.code === 1) {
          const data = response.data || {}
          this.stats = {
            totalCenters: data.totalCenters || data.TOTALCENTERS || 0,
            totalCost: Number(data.totalCost || data.TOTALCOST || 0),
            monthlyCost: Number(data.monthlyCost || data.MONTHLYCOST || 0),
            costVariance: Number(data.costVariance || data.COSTVARIANCE || 0)
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    async handleExport() {
      this.exportLoading = true
      try {
        await exportCostCenterList({})
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      } finally {
        this.exportLoading = false
      }
    },
    addCostCenter() {
      // 跳转到中心设置页面并自动打开新增对话框
      this.$router.push({
        path: '/management/centerSetup',
        query: { action: 'add' }
      })
    },
    costAllocation() {
      this.$router.push('/management/costAllocation')
    },
    costAnalysis() {
      this.$router.push('/management/costAnalysis')
    },
    manageCenterSetup() {
      this.$router.push('/management/centerSetup')
    },
    manageCostCollection() {
      this.$router.push('/management/costCollection')
    },
    manageCostAllocation() {
      this.$router.push('/management/costAllocation')
    },
    manageCostBudget() {
      this.$router.push('/management/costBudget')
    },
    manageCostControl() {
      this.$router.push('/management/costControl')
    },
    analyzeCostData() {
      this.$router.push('/management/costAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        'add': '/management/centerSetup',
        'collect': '/management/costCenter/costCollection',
        'allocate': '/management/costCenter/costAllocation',
        'budget': '/management/costCenter/costBudget',
        'control': '/management/costCenter/costControl',
        'report': '/management/costCenter/costAnalysis'
      }

      if (routeMap[action.key]) {
        this.$router.push(routeMap[action.key])
      } else {
        this.$message.warning(`暂无"${action.title}"对应的页面路由`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-center-container {
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

      &.total-centers {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.monthly-cost {
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

    &.center-setup {
      border-left: 4px solid #409eff;
    }

    &.cost-collection {
      border-left: 4px solid #67c23a;
    }

    &.cost-allocation {
      border-left: 4px solid #e6a23c;
    }

    &.cost-budget {
      border-left: 4px solid #f56c6c;
    }

    &.cost-control {
      border-left: 4px solid #909399;
    }

    &.cost-analysis-module {
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
