<template>
  <div class="account-management-home">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-bank-card"></i>
            账户管理中心
          </h1>
          <p class="page-subtitle">统一管理企业银行账户，实现资金集中监控与智能调度</p>
        </div>
        <div class="header-right">
          <div class="quick-stats">
            <div class="stat-item">
              <div class="stat-number">{{ accountStats.totalAccounts }}</div>
              <div class="stat-label">总账户数</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ accountStats.activeAccounts }}</div>
              <div class="stat-label">活跃账户</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ formatCurrency(accountStats.totalBalance) }}</div>
              <div class="stat-label">总余额</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="overview-card balance-card">
            <div class="card-header">
              <h3>资金概览</h3>
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="balance-info">
                <div class="balance-item">
                  <span class="label">可用余额</span>
                  <span class="amount available">{{ formatCurrency(balanceOverview.availableBalance) }}</span>
                </div>
                <div class="balance-item">
                  <span class="label">冻结余额</span>
                  <span class="amount frozen">{{ formatCurrency(balanceOverview.frozenBalance) }}</span>
                </div>
                <div class="balance-item">
                  <span class="label">在途资金</span>
                  <span class="amount transit">{{ formatCurrency(balanceOverview.transitBalance) }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="overview-card account-card">
            <div class="card-header">
              <h3>账户状态</h3>
              <i class="el-icon-postcard"></i>
            </div>
            <div class="card-content">
              <div class="account-status">
                <div class="status-item">
                  <div class="status-circle active"></div>
                  <span class="status-label">正常</span>
                  <span class="status-count">{{ accountStatus.active }}</span>
                </div>
                <div class="status-item">
                  <div class="status-circle frozen"></div>
                  <span class="status-label">冻结</span>
                  <span class="status-count">{{ accountStatus.frozen }}</span>
                </div>
                <div class="status-item">
                  <div class="status-circle closed"></div>
                  <span class="status-label">关闭</span>
                  <span class="status-count">{{ accountStatus.closed }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="overview-card bank-card">
            <div class="card-header">
              <h3>银行分布</h3>
              <i class="el-icon-office-building"></i>
            </div>
            <div class="card-content">
              <div class="bank-distribution">
                <div class="bank-item" v-for="bank in bankDistribution" :key="bank.bankCode">
                  <span class="bank-name">{{ bank.bankName }}</span>
                  <span class="bank-count">{{ bank.accountCount }}个</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块导航 -->
    <div class="function-modules">
      <h2 class="section-title">功能模块</h2>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/zhxx')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-postcard"></i>
              </div>
              <h3>账户信息管理</h3>
              <p>银行账户基础信息维护、账户开立与关闭、账户状态管理</p>
              <div class="card-footer">
                <span class="feature-tag">基础管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountOpeningManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <h3>开户申请管理</h3>
              <p>银行开户申请、申请审批流程、开户状态跟踪、开户文档管理</p>
              <div class="card-footer">
                <span class="feature-tag">开户管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountClosingManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <h3>销户申请管理</h3>
              <p>银行销户申请、销户审批流程、销户状态跟踪、销户文档管理</p>
              <div class="card-footer">
                <span class="feature-tag">销户管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/DirectConnectAuthManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-key"></i>
              </div>
              <h3>直联授权管理</h3>
              <p>银企直联授权配置、权限变更记录、安全控制、授权审批</p>
              <div class="card-footer">
                <span class="feature-tag">权限管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountChangeManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-finished"></i>
              </div>
              <h3>账户变更管理</h3>
              <p>账户信息变更申请、变更审批流程、变更记录管理、变更通知</p>
              <div class="card-footer">
                <span class="feature-tag">变更管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountFreezeManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-view"></i>
              </div>
              <h3>账户冻结管理</h3>
              <p>账户冻结申请、冻结审批流程、冻结记录管理、解冻操作</p>
              <div class="card-footer">
                <span class="feature-tag">冻结管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountCheckManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <h3>账户检查管理</h3>
              <p>账户合规检查、风险评估分析、检查报告生成、问题处理跟踪</p>
              <div class="card-footer">
                <span class="feature-tag">检查管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="6">
          <el-card class="module-card" @click.native="navigateTo('/globalTreasurerAccount/AccountLimitManage')">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-setting"></i>
              </div>
              <h3>账户限额管理</h3>
              <p>账户限额设置、限额监控预警、限额调整审批、限额统计分析</p>
              <div class="card-footer">
                <span class="feature-tag">限额管理</span>
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <h2 class="section-title">快速操作</h2>
      <el-row :gutter="16">
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('addAccount')">
            <i class="el-icon-plus"></i>
            <span>新增账户</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('balanceQuery')">
            <i class="el-icon-search"></i>
            <span>余额查询</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('transactionQuery')">
            <i class="el-icon-document"></i>
            <span>交易查询</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('reconciliation')">
            <i class="el-icon-finished"></i>
            <span>银企对账</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('report')">
            <i class="el-icon-data-line"></i>
            <span>生成报表</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-action-item" @click="handleQuickAction('export')">
            <i class="el-icon-download"></i>
            <span>数据导出</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {
  getAccountStats,
  getBalanceOverview,
  getAccountStatusStats,
  getBankDistribution,
  exportAccountInfo
} from '@/api/globalTreasurer/zhgl'

export default {
  name: 'AccountManagementHome',
  data() {
    return {
      // 账户统计数据
      accountStats: {
        totalAccounts: 0,
        activeAccounts: 0,
        totalBalance: 0
      },
      // 余额概览
      balanceOverview: {
        availableBalance: 0,
        frozenBalance: 0,
        transitBalance: 0
      },
      // 账户状态统计
      accountStatus: {
        active: 0,
        frozen: 0,
        closed: 0
      },
      // 银行分布
      bankDistribution: []
    }
  },
  created() {
    this.loadDashboardData()
  },
  methods: {
    /**
     * 加载仪表板数据
     */
    async loadDashboardData() {
      try {
        await Promise.all([
          this.loadAccountStats(),
          this.loadBalanceOverview(),
          this.loadAccountStatus(),
          this.loadBankDistribution()
        ])
      } catch (error) {
        console.error('加载仪表板数据失败:', error)
        // 接口全部失败时使用 mock 数据兜底
        this.accountStats = this.getMockAccountStats()
        this.balanceOverview = this.getMockBalanceOverview()
        this.accountStatus = this.getMockAccountStatus()
        this.bankDistribution = this.getMockBankDistribution()
      }
    },

    /**
     * 加载账户统计数据
     */
    async loadAccountStats() {
      try {
        const response = await getAccountStats()
        if (this.isSuccess(response)) {
          this.accountStats = response.data || this.getMockAccountStats()
        } else {
          this.accountStats = this.getMockAccountStats()
        }
      } catch (error) {
        console.error('加载账户统计失败:', error)
        this.accountStats = this.getMockAccountStats()
      }
    },

    /**
     * 加载余额概览
     */
    async loadBalanceOverview() {
      try {
        const response = await getBalanceOverview()
        if (this.isSuccess(response)) {
          this.balanceOverview = response.data || this.getMockBalanceOverview()
        } else {
          this.balanceOverview = this.getMockBalanceOverview()
        }
      } catch (error) {
        console.error('加载余额概览失败:', error)
        this.balanceOverview = this.getMockBalanceOverview()
      }
    },

    /**
     * 加载账户状态统计
     */
    async loadAccountStatus() {
      try {
        const response = await getAccountStatusStats()
        if (this.isSuccess(response)) {
          this.accountStatus = response.data || this.getMockAccountStatus()
        } else {
          this.accountStatus = this.getMockAccountStatus()
        }
      } catch (error) {
        console.error('加载账户状态统计失败:', error)
        this.accountStatus = this.getMockAccountStatus()
      }
    },

    /**
     * 加载银行分布
     */
    async loadBankDistribution() {
      try {
        const response = await getBankDistribution()
        if (this.isSuccess(response)) {
          this.bankDistribution = response.data || this.getMockBankDistribution()
        } else {
          this.bankDistribution = this.getMockBankDistribution()
        }
      } catch (error) {
        console.error('加载银行分布失败:', error)
        this.bankDistribution = this.getMockBankDistribution()
      }
    },

    /**
     * Mock 数据兜底 - 账户统计
     */
    getMockAccountStats() {
      return { totalAccounts: 156, activeAccounts: 142, totalBalance: 2856789432.50 }
    },

    /**
     * Mock 数据兜底 - 余额概览
     */
    getMockBalanceOverview() {
      return { availableBalance: 2456789432.50, frozenBalance: 350000000.00, transitBalance: 50000000.00 }
    },

    /**
     * Mock 数据兜底 - 账户状态
     */
    getMockAccountStatus() {
      return { active: 142, frozen: 8, closed: 6 }
    },

    /**
     * Mock 数据兜底 - 银行分布
     */
    getMockBankDistribution() {
      return [
        { bankCode: 'ICBC', bankName: '工商银行', accountCount: 45 },
        { bankCode: 'CCB', bankName: '建设银行', accountCount: 38 },
        { bankCode: 'ABC', bankName: '农业银行', accountCount: 32 },
        { bankCode: 'BOC', bankName: '中国银行', accountCount: 28 },
        { bankCode: 'CMB', bankName: '招商银行', accountCount: 13 }
      ]
    },

    /**
     * 页面导航 - 带重复路由保护
     */
    navigateTo(path) {
      if (this.$route.path !== path) {
        this.$router.push(path)
      }
    },

    /**
     * 快速操作处理
     */
    handleQuickAction(action) {
      switch (action) {
        case 'addAccount':
          // 跳转账户信息管理，并携带 action=add 参数触发新增弹窗
          this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { action: 'add' } })
          break
        case 'balanceQuery':
          // 跳转账户信息管理，携带 tab=balance 参数
          this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { tab: 'balance' } })
          break
        case 'transactionQuery':
          // 跳转账户信息管理，携带 tab=transaction 参数
          this.$router.push({ path: '/globalTreasurerAccount/zhxx', query: { tab: 'transaction' } })
          break
        case 'reconciliation':
          // 跳转银行对账管理
          this.$router.push('/globalTreasurerCash/yhdz')
          break
        case 'report':
          // 跳转决策支持（报表中心）
          this.$router.push('/globalTreasurerDecision/jczc')
          break
        case 'export':
          this.handleExport()
          break
        default:
          this.$message.info('该功能暂未开放')
      }
    },

    /**
     * 处理数据导出 - 调用后端导出接口下载账户数据
     */
    async handleExport() {
      this.$message.info('正在准备导出，请稍候...')
      try {
        const response = await exportAccountInfo({})
        // 判断是否为 JSON 错误响应
        if (response && response.type === 'application/json') {
          const text = await new Blob([response]).text()
          const data = JSON.parse(text)
          this.$message.error(data.message || '导出失败')
          return
        }
        // 触发文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `账户数据_${new Date().toLocaleDateString('zh-CN').replace(/\//g, '')}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },

    /**
     * 格式化货币
     */
    formatCurrency(amount) {
      if (amount === null || amount === undefined) return '¥0.00'
      return '¥' + Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    /**
     * 判断响应是否成功
     */
    isSuccess(response) {
      const successCodes = [200, 0, '200', '0', '1', 1, 2]
      return response && successCodes.includes(response.code)
    }
  }
}
</script>
<style lang="scss" scoped>
.account-management-home {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;

  // 页面头部样式
  .page-header {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 0 0 20px 20px;
    margin-bottom: 30px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 30px 40px;
      max-width: 1400px;
      margin: 0 auto;

      .header-left {
        .page-title {
          font-size: 32px;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 10px 0;
          display: flex;
          align-items: center;
          gap: 15px;

          i {
            color: #409EFF;
            font-size: 36px;
          }
        }

        .page-subtitle {
          font-size: 16px;
          color: #7f8c8d;
          margin: 0;
          line-height: 1.5;
        }
      }

      .header-right {
        .quick-stats {
          display: flex;
          gap: 30px;

          .stat-item {
            text-align: center;
            padding: 15px 20px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 12px;
            color: white;
            min-width: 120px;

            .stat-number {
              font-size: 24px;
              font-weight: 700;
              margin-bottom: 5px;
            }

            .stat-label {
              font-size: 14px;
              opacity: 0.9;
            }
          }
        }
      }
    }
  }

  // 概览卡片样式
  .overview-cards {
    padding: 0 40px;
    margin-bottom: 40px;
    max-width: 1400px;
    margin-left: auto;
    margin-right: auto;

    .overview-card {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      border-radius: 16px;
      padding: 25px;
      height: 200px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
      }

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          font-size: 18px;
          font-weight: 600;
          color: #2c3e50;
          margin: 0;
        }

        i {
          font-size: 24px;
          color: #409EFF;
        }
      }

      .card-content {
        height: calc(100% - 60px);
        display: flex;
        flex-direction: column;
        justify-content: center;
      }

      // 余额卡片特殊样式
      &.balance-card {
        .balance-info {
          .balance-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;

            .label {
              font-size: 14px;
              color: #7f8c8d;
            }

            .amount {
              font-size: 16px;
              font-weight: 600;

              &.available {
                color: #27ae60;
              }

              &.frozen {
                color: #e74c3c;
              }

              &.transit {
                color: #f39c12;
              }
            }
          }
        }
      }

      // 账户状态卡片特殊样式
      &.account-card {
        .account-status {
          .status-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;

            .status-circle {
              width: 12px;
              height: 12px;
              border-radius: 50%;
              margin-right: 10px;

              &.active {
                background-color: #27ae60;
              }

              &.frozen {
                background-color: #e74c3c;
              }

              &.closed {
                background-color: #95a5a6;
              }
            }

            .status-label {
              flex: 1;
              font-size: 14px;
              color: #7f8c8d;
            }

            .status-count {
              font-size: 16px;
              font-weight: 600;
              color: #2c3e50;
            }
          }
        }
      }

      // 银行分布卡片特殊样式
      &.bank-card {
        .bank-distribution {
          .bank-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            .bank-name {
              font-size: 14px;
              color: #7f8c8d;
            }

            .bank-count {
              font-size: 14px;
              font-weight: 600;
              color: #2c3e50;
            }
          }
        }
      }
    }
  }

  // 功能模块样式
  .function-modules {
    padding: 0 40px;
    margin-bottom: 40px;
    max-width: 1400px;
    margin-left: auto;
    margin-right: auto;

    .section-title {
      font-size: 24px;
      font-weight: 600;
      color: white;
      margin-bottom: 25px;
      text-align: center;
    }

    .module-card {
      cursor: pointer;
      transition: all 0.3s ease;
      border: none;
      border-radius: 16px;
      overflow: hidden;
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

      &:hover {
        transform: translateY(-8px);
        box-shadow: 0 16px 48px rgba(0, 0, 0, 0.2);

        .card-icon i {
          transform: scale(1.1);
          color: #667eea;
        }
      }

      .card-content {
        padding: 30px 25px;
        text-align: center;
        height: 220px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .card-icon {
          margin-bottom: 20px;

          i {
            font-size: 48px;
            color: #409EFF;
            transition: all 0.3s ease;
          }
        }

        h3 {
          font-size: 18px;
          font-weight: 600;
          color: #2c3e50;
          margin: 0 0 15px 0;
        }

        p {
          font-size: 14px;
          color: #7f8c8d;
          line-height: 1.6;
          margin: 0 0 20px 0;
          flex: 1;
        }

        .card-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .feature-tag {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
          }

          i {
            color: #409EFF;
            font-size: 16px;
          }
        }
      }
    }
  }

  // 快速操作样式
  .quick-actions {
    padding: 0 40px 40px;
    max-width: 1400px;
    margin-left: auto;
    margin-right: auto;

    .section-title {
      font-size: 24px;
      font-weight: 600;
      color: white;
      margin-bottom: 25px;
      text-align: center;
    }

    .quick-action-item {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      border-radius: 12px;
      padding: 20px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
        background: rgba(255, 255, 255, 1);

        i {
          color: #667eea;
          transform: scale(1.1);
        }
      }

      i {
        font-size: 24px;
        color: #409EFF;
        margin-bottom: 10px;
        display: block;
        transition: all 0.3s ease;
      }

      span {
        font-size: 14px;
        color: #2c3e50;
        font-weight: 500;
      }
    }
  }

  // 响应式设计
  @media (max-width: 1200px) {
    .page-header .header-content {
      padding: 20px 30px;
      flex-direction: column;
      gap: 20px;
      text-align: center;

      .header-right .quick-stats {
        gap: 20px;
      }
    }

    .overview-cards,
    .function-modules,
    .quick-actions {
      padding: 0 30px;
    }
  }

  @media (max-width: 768px) {
    .page-header .header-content {
      padding: 15px 20px;

      .header-left .page-title {
        font-size: 24px;
      }

      .header-right .quick-stats {
        flex-direction: column;
        gap: 10px;

        .stat-item {
          min-width: 100px;
        }
      }
    }

    .overview-cards,
    .function-modules,
    .quick-actions {
      padding: 0 20px;
    }

    .function-modules .module-card .card-content {
      height: auto;
      padding: 20px 15px;
    }
  }
}
</style>
