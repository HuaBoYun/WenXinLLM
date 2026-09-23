<template>
  <div class="financial-sharing-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <i class="el-icon-s-finance"></i>
            事项会计系统
            <el-tag type="success" size="small" class="version-tag">v1.0.0</el-tag>
          </h1>
          <p class="page-description">基于事项驱动的智能化财务共享平台</p>
          <div class="breadcrumb-nav">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>
                <i class="el-icon-s-home"></i>
                首页
              </el-breadcrumb-item>
              <el-breadcrumb-item>财务共享</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
        <div class="header-actions">
          <el-tooltip content="快速开始使用系统" placement="bottom">
            <el-button type="primary" icon="el-icon-video-play" @click="showQuickStart" circle></el-button>
          </el-tooltip>
          <el-tooltip content="查看系统帮助" placement="bottom">
            <el-button type="info" icon="el-icon-question" @click="showHelp" circle></el-button>
          </el-tooltip>
          <el-tooltip content="系统设置" placement="bottom">
            <el-button type="warning" icon="el-icon-setting" @click="showSettings" circle></el-button>
          </el-tooltip>
        </div>
      </div>

      <!-- 系统状态指示器 -->
      <div class="system-status">
        <el-tag :type="systemStatus.type" size="mini">
          <i :class="systemStatus.icon"></i>
          {{ systemStatus.text }}
        </el-tag>
        <span class="last-update">最后更新: {{ lastUpdateTime }}</span>
      </div>
    </div>

    <!-- 模块导航 -->
    <div class="module-navigation">
      <el-row :gutter="24">
        <!-- 事项会计中台 -->
        <el-col :span="6">
          <div class="module-card platform-module">
            <div class="module-header">
              <i class="el-icon-cpu"></i>
              <h3>事项会计中台</h3>
              <span class="module-badge">核心底座</span>
            </div>
            <div class="module-content">
              <p>提供事项会计的核心能力和基础服务</p>
              <div class="module-links">
                <router-link to="/financialSharing/platform/common" class="module-link">
                  <i class="el-icon-setting"></i>财务公共
                </router-link>
                <router-link to="/financialSharing/platform/matter" class="module-link">
                  <i class="el-icon-document"></i>事项中心
                </router-link>
                <router-link to="/financialSharing/platform/rules" class="module-link">
                  <i class="el-icon-s-operation"></i>会计规则
                </router-link>
                <router-link to="/financialSharing/platform/accounting" class="module-link">
                  <i class="el-icon-s-order"></i>会计中心
                </router-link>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 财务会计模块 -->
        <el-col :span="6">
          <div class="module-card financial-module">
            <div class="module-header">
              <i class="el-icon-s-finance"></i>
              <h3>财务会计</h3>
              <span class="module-badge">核算管理</span>
            </div>
            <div class="module-content">
              <p>提供完整的财务核算和管理功能</p>
              <div class="module-links">
                <router-link to="/financialSharing/financial/generalLedger" class="module-link">
                  <i class="el-icon-notebook-1"></i>总账
                </router-link>
                <router-link to="/financialSharing/financial/fixedAssets" class="module-link">
                  <i class="el-icon-office-building"></i>固定资产
                </router-link>
                <router-link to="/financialSharing/financial/receivables" class="module-link">
                  <i class="el-icon-money"></i>应收管理
                </router-link>
                <router-link to="/financialSharing/financial/payables" class="module-link">
                  <i class="el-icon-wallet"></i>应付管理
                </router-link>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 管理会计模块 -->
        <el-col :span="6">
          <div class="module-card management-module">
            <div class="module-header">
              <i class="el-icon-pie-chart"></i>
              <h3>管理会计</h3>
              <span class="module-badge">成本管控</span>
            </div>
            <div class="module-content">
              <p>提供成本核算和管理决策支持</p>
              <div class="module-links">
                <router-link to="/financialSharing/management" class="module-link">
                  <i class="el-icon-data-analysis"></i>管理会计总览
                </router-link>
                <router-link to="/financialSharing/management/costCenter" class="module-link">
                  <i class="el-icon-s-grid"></i>成本中心
                </router-link>
                <router-link to="/financialSharing/management/productCost" class="module-link">
                  <i class="el-icon-goods"></i>产品成本
                </router-link>
                <router-link to="/financialSharing/management/internalSettlement" class="module-link">
                  <i class="el-icon-s-cooperation"></i>内部结算
                </router-link>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 报表分析模块 -->
        <el-col :span="6">
          <div class="module-card reports-module">
            <div class="module-header">
              <i class="el-icon-data-line"></i>
              <h3>报表分析</h3>
              <span class="module-badge">决策支持</span>
            </div>
            <div class="module-content">
              <p>提供财务报表和业务分析功能</p>
              <div class="module-links">
                <router-link to="/financialSharing/reports" class="module-link">
                  <i class="el-icon-data-line"></i>报表分析总览
                </router-link>
                <router-link to="/financialSharing/reports/financial" class="module-link">
                  <i class="el-icon-document-copy"></i>财务报表
                </router-link>
                <router-link to="/financialSharing/reports/business" class="module-link">
                  <i class="el-icon-data-board"></i>业务分析
                </router-link>
                <router-link to="/financialSharing/reports/visualization" class="module-link">
                  <i class="el-icon-pie-chart"></i>数据可视化
                </router-link>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速入口 -->
    <div class="quick-access">
      <h3 class="section-title">
        <i class="el-icon-lightning"></i>
        快速入口
      </h3>
      <el-row :gutter="16">
        <el-col :span="4" v-for="item in quickAccessItems" :key="item.key">
          <div class="quick-item" @click="handleQuickAccess(item)">
            <i :class="item.icon"></i>
            <span>{{ item.label }}</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统状态 -->
    <div class="system-status">
      <h3 class="section-title">
        <i class="el-icon-monitor"></i>
        系统状态
      </h3>
      <el-row :gutter="16">
        <el-col :span="6">
          <div class="status-card">
            <div class="status-value">{{ systemStatus.todayTransactions }}</div>
            <div class="status-label">今日事项处理</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-card">
            <div class="status-value">{{ systemStatus.pendingVouchers }}</div>
            <div class="status-label">待生成凭证</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-card">
            <div class="status-value">{{ systemStatus.activeRules }}</div>
            <div class="status-label">活跃规则数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-card">
            <div class="status-value">{{ systemStatus.systemHealth }}</div>
            <div class="status-label">系统健康度</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FinancialSharingIndex',
  data() {
    return {
      quickAccessItems: [
        { key: 'matter', label: '事项录入', icon: 'el-icon-edit-outline', path: '/financialSharing/platform/matter' },
        { key: 'voucher', label: '凭证生成', icon: 'el-icon-document-add', path: '/financialSharing/platform/accounting' },
        { key: 'rules', label: '规则配置', icon: 'el-icon-s-operation', path: '/financialSharing/platform/rules' },
        { key: 'reports', label: '财务报表', icon: 'el-icon-data-line', path: '/financialSharing/reports/financial' },
        { key: 'analysis', label: '业务分析', icon: 'el-icon-data-board', path: '/financialSharing/reports/business' },
        { key: 'settings', label: '系统配置', icon: 'el-icon-setting', path: '/financialSharing/platform/common' }
      ],
      systemStatus: {
        type: 'success',
        icon: 'el-icon-success',
        text: '系统运行正常',
        todayTransactions: 0,
        pendingVouchers: 0,
        activeRules: 0,
        systemHealth: '良好'
      },
      lastUpdateTime: '',
      quickStartVisible: false,
      helpVisible: false,
      settingsVisible: false
    }
  },
  mounted() {
    this.loadSystemStatus()
    this.updateTime()
    // 每分钟更新一次时间
    setInterval(this.updateTime, 60000)
  },
  methods: {
    handleQuickAccess(item) {
      this.$router.push(item.path)
    },
    async loadSystemStatus() {
      try {
        // 暂未对接系统状态 API，先以空状态展示，待后端接口提供后接入
        this.systemStatus = {
          type: 'info',
          icon: 'el-icon-info',
          text: '暂无状态数据',
          todayTransactions: 0,
          pendingVouchers: 0,
          activeRules: 0,
          systemHealth: '-'
        }
      } catch (error) {
        console.error('加载系统状态失败:', error)
        this.systemStatus = {
          type: 'danger',
          icon: 'el-icon-error',
          text: '系统异常',
          todayTransactions: 0,
          pendingVouchers: 0,
          activeRules: 0,
          systemHealth: '异常'
        }
      }
    },
    updateTime() {
      const now = new Date()
      this.lastUpdateTime = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    showQuickStart() {
      this.$alert(`
        <div style="text-align: left;">
          <h3>快速开始指南</h3>
          <ol>
            <li>配置基础数据：会计科目、辅助核算项目等</li>
            <li>设置会计规则：确认规则、计量规则、凭证规则</li>
            <li>录入业务事项：销售、采购、费用等业务数据</li>
            <li>生成会计凭证：基于规则自动生成凭证</li>
            <li>查看财务报表：资产负债表、利润表等</li>
          </ol>
        </div>
      `, '快速开始', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '开始使用',
        callback: () => {
          this.$router.push('/financialSharing/platform/common')
        }
      })
    },
    showHelp() {
      this.$alert(`
        <div style="text-align: left;">
          <h3>系统帮助</h3>
          <p><strong>事项会计中台：</strong>提供财务公共配置、事项管理、会计规则和会计中心功能</p>
          <p><strong>财务会计：</strong>包含总账、固定资产、应收应付、存货核算、收入管理等模块</p>
          <p><strong>管理会计：</strong>提供成本中心、产品成本、成本估算、专项成本、内部结算功能</p>
          <p><strong>报表分析：</strong>生成各类财务报表和业务分析报告</p>
          <br>
          <p>如需更多帮助，请联系系统管理员。</p>
        </div>
      `, '系统帮助', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '我知道了'
      })
    },
    showSettings() {
      this.$router.push('/financialSharing/platform/common')
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-sharing-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 30px;
  color: white;
  position: relative;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20px;

    .title-section {
      flex: 1;

      .page-title {
        font-size: 28px;
        font-weight: 600;
        margin: 0 0 8px 0;
        display: flex;
        align-items: center;

        i {
          margin-right: 12px;
        }

        .version-tag {
          margin-left: 12px;
          background: rgba(255, 255, 255, 0.2);
          border: 1px solid rgba(255, 255, 255, 0.3);
          color: white;
        }
      }

      .page-description {
        font-size: 16px;
        opacity: 0.9;
        margin: 0 0 12px 0;
      }

      .breadcrumb-nav {
        .el-breadcrumb {
          color: rgba(255, 255, 255, 0.8);

          ::v-deep .el-breadcrumb__inner {
            color: rgba(255, 255, 255, 0.8);

            &:hover {
              color: white;
            }
          }

          ::v-deep .el-breadcrumb__separator {
            color: rgba(255, 255, 255, 0.6);
          }
        }
      }
    }

    .header-actions {
      display: flex;
      gap: 12px;
      align-items: center;

      .el-button {
        background: rgba(255, 255, 255, 0.1);
        border: 1px solid rgba(255, 255, 255, 0.2);
        color: white;

        &:hover {
          background: rgba(255, 255, 255, 0.2);
          border-color: rgba(255, 255, 255, 0.3);
        }
      }
    }
  }

  .system-status {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 20px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);

    .last-update {
      font-size: 12px;
      opacity: 0.7;
    }
  }
}

.module-navigation {
  margin-bottom: 40px;
}

.module-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  height: 280px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border-left: 4px solid;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }

  &.platform-module {
    border-left-color: #409eff;
  }

  &.financial-module {
    border-left-color: #67c23a;
  }

  &.management-module {
    border-left-color: #e6a23c;
  }

  &.reports-module {
    border-left-color: #f56c6c;
  }
}

.module-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  position: relative;

  i {
    font-size: 24px;
    margin-right: 12px;
    color: #409eff;
  }

  h3 {
    font-size: 18px;
    font-weight: 600;
    margin: 0;
    color: #303133;
  }

  .module-badge {
    position: absolute;
    right: 0;
    top: 0;
    background: #f0f9ff;
    color: #409eff;
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 500;
  }
}

.module-content {
  p {
    color: #606266;
    font-size: 14px;
    margin: 0 0 20px 0;
    line-height: 1.5;
  }
}

.module-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.module-link {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 6px;
  color: #606266;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s ease;

  &:hover {
    background: #f5f7fa;
    color: #409eff;
  }

  i {
    margin-right: 8px;
    font-size: 16px;
  }
}

.quick-access, .system-status {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;

  i {
    margin-right: 8px;
    color: #409eff;
  }
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background: #f5f7fa;
    color: #409eff;
  }

  i {
    font-size: 24px;
    margin-bottom: 8px;
  }

  span {
    font-size: 14px;
  }
}

.status-card {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  background: #f8f9fa;

  .status-value {
    font-size: 24px;
    font-weight: 600;
    color: #409eff;
    margin-bottom: 8px;
  }

  .status-label {
    font-size: 14px;
    color: #606266;
  }
}
</style>
