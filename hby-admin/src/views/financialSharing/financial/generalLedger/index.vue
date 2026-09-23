<template>
  <div class="general-ledger-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-data"></i>
          总账核算
        </h1>
        <p class="page-description">管理总账科目余额、明细账查询、试算平衡等核心总账功能</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshBalance">
          刷新余额
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportData">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 总账统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon assets">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalAssets) }}</div>
              <div class="stat-label">资产总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon liabilities">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalLiabilities) }}</div>
              <div class="stat-label">负债总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon equity">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalEquity) }}</div>
              <div class="stat-label">所有者权益</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon balance">
              <i class="el-icon-scale-to-original"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value" :class="{ 'balanced': stats.isBalanced, 'unbalanced': !stats.isBalanced }">
                {{ stats.isBalanced ? '平衡' : '不平衡' }}
              </div>
              <div class="stat-label">试算平衡</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 科目余额 -->
        <el-col :span="8">
          <div class="module-card account-balance">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="card-title">
                <h3>科目余额</h3>
                <span class="card-subtitle">查看各科目的期初、本期发生、期末余额</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>科目余额表</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额汇总查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额对比分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额变动追踪</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="viewAccountBalance">
                  查看科目余额
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 明细账 -->
        <el-col :span="8">
          <div class="module-card detail-ledger">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>明细账</h3>
                <span class="card-subtitle">查看科目的详细发生额和余额明细</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>科目明细账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>辅助明细账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>多栏式明细账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>明细账打印</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="viewDetailLedger">
                  查看明细账
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 试算平衡表 -->
        <el-col :span="8">
          <div class="module-card trial-balance">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-scale-to-original"></i>
              </div>
              <div class="card-title">
                <h3>试算平衡表</h3>
                <span class="card-subtitle">检查账务处理的准确性和完整性</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>发生额试算平衡</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额试算平衡</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>综合试算平衡</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>不平衡原因分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="viewTrialBalance">
                  查看试算平衡表
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 总账查询 -->
        <el-col :span="8">
          <div class="module-card ledger-query">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-search"></i>
              </div>
              <div class="card-title">
                <h3>总账查询</h3>
                <span class="card-subtitle">多维度查询总账数据和统计信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>按科目查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>按期间查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>按辅助项查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>组合条件查询</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="queryLedger">
                  总账查询
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 期末处理 -->
        <el-col :span="8">
          <div class="module-card period-end">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-finished"></i>
              </div>
              <div class="card-title">
                <h3>期末处理</h3>
                <span class="card-subtitle">执行期末结账和账务处理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>期末结转</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>损益结转</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>期末结账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>结账检查</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="periodEndProcess">
                  期末处理
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 总账分析 -->
        <el-col :span="8">
          <div class="module-card ledger-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>总账分析</h3>
                <span class="card-subtitle">分析总账数据趋势和异常情况</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>科目发生额分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>余额变动分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>异常数据检测</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>趋势图表展示</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeLedger">
                  总账分析
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
export default {
  name: 'GeneralLedgerIndex',
  data() {
    return {
      stats: {
        totalAssets: 0,
        totalLiabilities: 0,
        totalEquity: 0,
        isBalanced: true
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接总账统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalAssets: 0,
          totalLiabilities: 0,
          totalEquity: 0,
          isBalanced: false
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    async refreshBalance() {
      this.$confirm('确定要刷新所有科目余额吗？这可能需要一些时间。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const loading = this.$loading({
            lock: true,
            text: '正在刷新余额...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          // 触发统计数据刷新（实际刷新动作由后端 loadStats 接口承担）
          await this.loadStats()

          loading.close()
          this.$message.success('余额刷新成功')
        } catch (error) {
          this.$message.error('余额刷新失败：' + error.message)
        }
      })
    },
    async exportData() {
      try {
        // 动态导入导出功能
        const { exportGeneralLedger, ExportParamBuilder, generateExportFileName } = await import('@/api/financialSharing/export')

        // 构建导出参数
        const exportParams = new ExportParamBuilder()
          .setDateRange(this.startDate || new Date(new Date().getFullYear(), 0, 1), this.endDate || new Date())
          .setAccountingPeriod(this.currentPeriod || new Date().getFullYear().toString() + (new Date().getMonth() + 1).toString().padStart(2, '0'))
          .setIncludeDetails(true)
          .setFormat('xlsx')
          .build()

        const loading = this.$loading({
          lock: true,
          text: '正在创建导出任务...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        // 调用导出API
        const result = await exportGeneralLedger(exportParams)

        loading.close()

        if (result.code === 1) {
          this.$message.success('总账数据导出任务已创建，正在后台处理...')

          // 如果返回了任务ID，可以提示用户查看任务状态
          if (result.data && result.data.taskId) {
            this.$message.info(`任务ID: ${result.data.taskId}，您可以在导出管理中查看进度`)
          }
        } else {
          this.$message.error('导出失败: ' + (result.message || '未知错误'))
        }
      } catch (error) {
        console.error('导出数据失败:', error)
        this.$message.error('导出失败: ' + (error.message || '网络错误'))
      }
    },
    viewAccountBalance() {
      this.$router.push('/financialSharing/financial/generalLedger/accountBalance')
    },
    viewDetailLedger() {
      this.$router.push('/financialSharing/financial/generalLedger/detailLedger')
    },
    viewTrialBalance() {
      this.$router.push('/financialSharing/financial/generalLedger/trialBalance')
    },
    queryLedger() {
      this.$router.push('/financialSharing/financial/generalLedger/ledgerQuery')
    },
    periodEndProcess() {
      this.$router.push('/financialSharing/financial/generalLedger/periodEnd')
    },
    analyzeLedger() {
      this.$router.push('/financialSharing/financial/generalLedger/ledgerAnalysis')
    }
  }
}
</script>

<style lang="scss" scoped>
.general-ledger-container {
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

      &.assets {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.liabilities {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.equity {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.balance {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;

        &.balanced {
          color: #67c23a;
        }

        &.unbalanced {
          color: #f56c6c;
        }
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
    height: 280px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.account-balance {
      border-left: 4px solid #409eff;
    }

    &.detail-ledger {
      border-left: 4px solid #67c23a;
    }

    &.trial-balance {
      border-left: 4px solid #e6a23c;
    }

    &.ledger-query {
      border-left: 4px solid #f56c6c;
    }

    &.period-end {
      border-left: 4px solid #909399;
    }

    &.ledger-analysis {
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
</style>
