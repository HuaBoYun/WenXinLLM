<template>
  <div class="accounting-center-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-order"></i>
          会计中心模块
        </h1>
        <p class="page-description">管理会计凭证生成、审核、过账等核心会计业务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createVoucher">
          新建凭证
        </el-button>
        <el-button type="success" icon="el-icon-magic-stick" @click="batchGenerate">
          批量生成
        </el-button>
        <el-button type="warning" icon="el-icon-s-promotion" @click="batchPost">
          批量过账
        </el-button>
      </div>
    </div>

    <!-- 凭证统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalVouchers }}</div>
              <div class="stat-label">凭证总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon draft">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.draftVouchers }}</div>
              <div class="stat-label">草稿凭证</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon approved">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.approvedVouchers }}</div>
              <div class="stat-label">已审核凭证</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon posted">
              <i class="el-icon-s-promotion"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.postedVouchers }}</div>
              <div class="stat-label">已过账凭证</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 核心功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 凭证管理 -->
        <el-col :span="8">
          <div class="module-card voucher-management">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>凭证管理</h3>
                <span class="card-subtitle">管理会计凭证的全生命周期</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证修改</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证删除</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageVouchers">
                  管理凭证
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 凭证生成 -->
        <el-col :span="8">
          <div class="module-card voucher-generation">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-magic-stick"></i>
              </div>
              <div class="card-title">
                <h3>凭证生成</h3>
                <span class="card-subtitle">基于业务事项自动生成会计凭证</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>自动生成凭证</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量生成凭证</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>生成预览</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>生成进度监控</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="generateVouchers">
                  生成凭证
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 凭证审核 -->
        <el-col :span="8">
          <div class="module-card voucher-approval">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-finished"></i>
              </div>
              <div class="card-title">
                <h3>凭证审核</h3>
                <span class="card-subtitle">审核凭证的准确性和合规性</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证审核</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量审核</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>审核退回</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>审核历史</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="approveVouchers">
                  审核凭证
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 凭证过账 -->
        <el-col :span="8">
          <div class="module-card voucher-posting">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="card-title">
                <h3>凭证过账</h3>
                <span class="card-subtitle">将审核通过的凭证过账到总账</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>单张过账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量过账</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账撤销</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账监控</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="postVouchers">
                  过账凭证
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 凭证模板 -->
        <el-col :span="8">
          <div class="module-card voucher-template">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>凭证模板</h3>
                <span class="card-subtitle">管理凭证生成的模板和规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板设计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板测试</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板版本管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageTemplates">
                  管理模板
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 凭证分析 -->
        <el-col :span="8">
          <div class="module-card voucher-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>凭证分析</h3>
                <span class="card-subtitle">分析凭证数据和处理效率</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证统计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>处理效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>异常凭证分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>趋势分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="analyzeVouchers">
                  分析凭证
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
        <p>常用的凭证处理操作</p>
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
  name: 'AccountingCenterIndex',
  data() {
    return {
      stats: {
        totalVouchers: 0,
        draftVouchers: 0,
        approvedVouchers: 0,
        postedVouchers: 0
      },
      quickActions: [
        { key: 'create', title: '新建凭证', desc: '手工录入凭证', icon: 'el-icon-plus' },
        { key: 'generate', title: '生成凭证', desc: '从事项生成', icon: 'el-icon-magic-stick' },
        { key: 'approve', title: '审核凭证', desc: '审核待审凭证', icon: 'el-icon-check' },
        { key: 'post', title: '过账凭证', desc: '过账已审凭证', icon: 'el-icon-s-promotion' },
        { key: 'query', title: '查询凭证', desc: '查询凭证信息', icon: 'el-icon-search' },
        { key: 'report', title: '凭证报表', desc: '生成凭证报表', icon: 'el-icon-data-line' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接凭证统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalVouchers: 0,
          draftVouchers: 0,
          approvedVouchers: 0,
          postedVouchers: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    createVoucher() {
      this.$router.push('/financial/voucherManagement')
    },
    batchGenerate() {
      this.$router.push('/financial/voucherGeneration')
    },
    batchPost() {
      this.$router.push('/financial/voucherPosting')
    },
    manageVouchers() {
      this.$router.push('/financial/voucherManagement')
    },
    generateVouchers() {
      this.$router.push('/financial/voucherGeneration')
    },
    approveVouchers() {
      this.$router.push('/financial/voucherApproval')
    },
    postVouchers() {
      this.$router.push('/financial/voucherPosting')
    },
    manageTemplates() {
      this.$router.push('/financial/voucherTemplate')
    },
    analyzeVouchers() {
      this.$router.push('/financial/voucherAnalysis')
    },
    handleQuickAction(action) {
      this.$message.warning(`${action.title}功能暂未开放，请从左侧菜单进入对应模块`)
    }
  }
}
</script>

<style lang="scss" scoped>
.accounting-center-container {
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

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.draft {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.approved {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.posted {
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

    &.voucher-management {
      border-left: 4px solid #409eff;
    }

    &.voucher-generation {
      border-left: 4px solid #67c23a;
    }

    &.voucher-approval {
      border-left: 4px solid #e6a23c;
    }

    &.voucher-posting {
      border-left: 4px solid #f56c6c;
    }

    &.voucher-template {
      border-left: 4px solid #909399;
    }

    &.voucher-analysis {
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
