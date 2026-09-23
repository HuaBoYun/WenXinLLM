<template>
  <div class="fixed-assets-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-office-building"></i>
          固定资产管理
        </h1>
        <p class="page-description">管理固定资产卡片、折旧计提、资产处置等固定资产全生命周期</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="addAsset">
          新增资产
        </el-button>
        <el-button type="success" icon="el-icon-cpu" @click="calculateDepreciation">
          计提折旧
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="exportAssets">
          导出资产
        </el-button>
      </div>
    </div>

    <!-- 资产统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-assets">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalAssets }}</div>
              <div class="stat-label">资产总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon original-value">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.originalValue) }}</div>
              <div class="stat-label">原值总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon accumulated-depreciation">
              <i class="el-icon-remove-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.accumulatedDepreciation) }}</div>
              <div class="stat-label">累计折旧</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon net-value">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.netValue) }}</div>
              <div class="stat-label">净值总额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 资产卡片管理 -->
        <el-col :span="8">
          <div class="module-card asset-card">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-postcard"></i>
              </div>
              <div class="card-title">
                <h3>资产卡片管理</h3>
                <span class="card-subtitle">管理固定资产的基础信息和档案</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产卡片录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产信息修改</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产查询统计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产批量导入</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageAssetCards">
                  管理资产卡片
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 折旧管理 -->
        <el-col :span="8">
          <div class="module-card depreciation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-title">
                <h3>折旧管理</h3>
                <span class="card-subtitle">管理资产折旧计提和折旧政策</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>折旧政策设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>月度折旧计提</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>折旧凭证生成</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>折旧明细查询</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageDepreciation">
                  管理折旧
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 资产变动 -->
        <el-col :span="8">
          <div class="module-card asset-change">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-refresh"></i>
              </div>
              <div class="card-title">
                <h3>资产变动</h3>
                <span class="card-subtitle">处理资产的增加、减少、变更等业务</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产增加</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产减少</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产转移</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>变动审批</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageAssetChange">
                  管理资产变动
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 资产处置 -->
        <el-col :span="8">
          <div class="module-card asset-disposal">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-delete"></i>
              </div>
              <div class="card-title">
                <h3>资产处置</h3>
                <span class="card-subtitle">处理资产的报废、出售、转让等处置业务</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产报废</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产出售</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产转让</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>处置损益核算</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageAssetDisposal">
                  管理资产处置
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 资产盘点 -->
        <el-col :span="8">
          <div class="module-card asset-inventory">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-view"></i>
              </div>
              <div class="card-title">
                <h3>资产盘点</h3>
                <span class="card-subtitle">定期盘点资产实物与账面的一致性</span>
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
                <el-button type="primary" size="small" @click="manageAssetInventory">
                  管理资产盘点
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 资产分析 -->
        <el-col :span="8">
          <div class="module-card asset-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>资产分析</h3>
                <span class="card-subtitle">分析资产结构、使用效率和价值变化</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产结构分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>折旧分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>使用效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>价值变化趋势</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeAssets">
                  资产分析
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
        <p>常用的资产管理操作</p>
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
  name: 'FixedAssetsIndex',
  data() {
    return {
      stats: {
        totalAssets: 0,
        originalValue: 0,
        accumulatedDepreciation: 0,
        netValue: 0
      },
      quickActions: [
        { key: 'add', title: '新增资产', desc: '录入新资产', icon: 'el-icon-plus' },
        { key: 'depreciation', title: '计提折旧', desc: '月度折旧计提', icon: 'el-icon-cpu' },
        { key: 'change', title: '资产变动', desc: '处理资产变动', icon: 'el-icon-refresh' },
        { key: 'disposal', title: '资产处置', desc: '处理资产处置', icon: 'el-icon-delete' },
        { key: 'inventory', title: '资产盘点', desc: '执行资产盘点', icon: 'el-icon-view' },
        { key: 'report', title: '资产报表', desc: '生成资产报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接固定资产统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalAssets: 0,
          originalValue: 0,
          accumulatedDepreciation: 0,
          netValue: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    addAsset() {
      this.$router.push('/gdzc/assetCards')
    },
    calculateDepreciation() {
      this.$router.push('/gdzc/depreciation')
    },
    exportAssets() {
      try {
        const data = this.assetStats || {}
        if (Object.keys(data).length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '固定资产数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    manageAssetCards() {
      this.$router.push('/gdzc/assetCards')
    },
    manageDepreciation() {
      this.$router.push('/gdzc/depreciation')
    },
    manageAssetChange() {
      this.$router.push('/gdzc/assetChange')
    },
    manageAssetDisposal() {
      this.$router.push('/gdzc/assetDisposal')
    },
    manageAssetInventory() {
      this.$router.push('/gdzc/assetInventory')
    },
    analyzeAssets() {
      this.$router.push('/gdzc/assetAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        '资产卡片': '/gdzc/assetCards',
        '折旧计提': '/gdzc/depreciation',
        '资产处置': '/gdzc/assetDisposal',
        '资产盘点': '/gdzc/assetInventory',
        '资产变动': '/gdzc/assetChange',
        '资产分析': '/gdzc/assetAnalysis'
      }
      const route = routeMap[action.title]
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
.fixed-assets-container {
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

      &.total-assets {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.original-value {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.accumulated-depreciation {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.net-value {
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

    &.asset-card {
      border-left: 4px solid #409eff;
    }

    &.depreciation {
      border-left: 4px solid #67c23a;
    }

    &.asset-change {
      border-left: 4px solid #e6a23c;
    }

    &.asset-disposal {
      border-left: 4px solid #f56c6c;
    }

    &.asset-inventory {
      border-left: 4px solid #909399;
    }

    &.asset-analysis {
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
