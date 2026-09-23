<template>
  <div class="derivatives-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>衍生品管理</h2>
      <p>管理远期、期权、掉期等衍生品交易</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon forward-icon">
              <i class="el-icon-s-finance"></i>
            </div>
            <div class="stats-info">
              <h3>{{ overview.forwardCount || 0 }}</h3>
              <p>远期合约</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon option-icon">
              <i class="el-icon-s-opportunity"></i>
            </div>
            <div class="stats-info">
              <h3>{{ overview.optionCount || 0 }}</h3>
              <p>期权合约</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon swap-icon">
              <i class="el-icon-s-cooperation"></i>
            </div>
            <div class="stats-info">
              <h3>{{ overview.swapCount || 0 }}</h3>
              <p>掉期合约</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon total-icon">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stats-info">
              <h3>{{ overview.totalNotional || 0 }}</h3>
              <p>总名义本金(万)</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能导航 -->
    <el-card class="function-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="远期交易" name="forward">
          <forward-management ref="forwardRef" />
        </el-tab-pane>
        <el-tab-pane label="期权交易" name="option">
          <option-management ref="optionRef" />
        </el-tab-pane>
        <el-tab-pane label="掉期交易" name="swap">
          <swap-management ref="swapRef" />
        </el-tab-pane>
        <el-tab-pane label="风险监控" name="risk">
          <risk-monitoring ref="riskRef" />
        </el-tab-pane>
        <el-tab-pane label="估值管理" name="valuation">
          <valuation-management ref="valuationRef" />
        </el-tab-pane>
        <el-tab-pane label="报表分析" name="report">
          <report-analysis ref="reportRef" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 快速操作面板 -->
    <el-card class="quick-actions" v-if="activeTab === 'forward'">
      <div class="actions-header">
        <h3>快速操作</h3>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-button type="primary" icon="el-icon-plus" @click="createForwardContract" block>
            创建远期合约
          </el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="success" icon="el-icon-s-finance" @click="viewForwardRates" block>
            查看远期汇率
          </el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="warning" icon="el-icon-warning" @click="checkDeliveryAlerts" block>
            交割提醒
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="quick-actions" v-if="activeTab === 'option'">
      <div class="actions-header">
        <h3>快速操作</h3>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-button type="primary" icon="el-icon-plus" @click="createOptionContract" block>
            创建期权合约
          </el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="success" icon="el-icon-s-data" @click="calculateOptionPrice" block>
            期权定价
          </el-button>
        </el-col>
        <el-col :span="8">
          <el-button type="info" icon="el-icon-s-marketing" @click="viewGreeks" block>
            希腊字母
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="quick-actions" v-if="activeTab === 'risk'">
      <div class="actions-header">
        <h3>快速操作</h3>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="danger" icon="el-icon-warning" @click="checkRiskLimits" block>
            限额检查
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="warning" icon="el-icon-s-data" @click="calculateVaR" block>
            VaR计算
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="info" icon="el-icon-s-marketing" @click="performStressTest" block>
            压力测试
          </el-button>
        </el-col>
        <el-col :span="6">
          <el-button type="success" icon="el-icon-s-order" @click="generateRiskReport" block>
            风险报告
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预警信息 -->
    <el-card class="alerts-panel" v-if="alerts.length > 0">
      <div class="alerts-header">
        <h3>预警信息</h3>
        <el-badge :value="alerts.length" class="alert-badge" />
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="alert in alerts"
          :key="alert.id"
          :timestamp="alert.createTime"
          :type="getAlertType(alert.level)"
        >
          <h4>{{ alert.title }}</h4>
          <p>{{ alert.message }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script>
import { getDerivativesMonitoringDashboard, getMonitoringAlerts } from '@/api/globalTreasurer/yspx'
import ForwardManagement from './components/ForwardManagement'
import OptionManagement from './components/OptionManagement'
import SwapManagement from './components/SwapManagement'
import RiskMonitoring from './components/RiskMonitoring'
import ValuationManagement from './components/ValuationManagement'
import ReportAnalysis from './components/ReportAnalysis'

export default {
  name: 'DerivativesManagement',
  components: {
    ForwardManagement,
    OptionManagement,
    SwapManagement,
    RiskMonitoring,
    ValuationManagement,
    ReportAnalysis
  },
  data() {
    return {
      activeTab: 'forward',
      overview: {
        forwardCount: 0,
        optionCount: 0,
        swapCount: 0,
        totalNotional: 0
      },
      alerts: [],
      loading: false
    }
  },
  created() {
    this.loadDashboardData()
    this.loadAlerts()
  },
  methods: {
    // 加载仪表盘数据
    async loadDashboardData() {
      try {
        this.loading = true
        const response = await getDerivativesMonitoringDashboard({
          orgId: this.$store.getters.orgId
        })
        
        if (response.code === 200) {
          this.overview = response.data || {}
        }
      } catch (error) {
        console.error('加载仪表盘数据失败:', error)
        this.$message.error('加载仪表盘数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载预警信息
    async loadAlerts() {
      try {
        const response = await getMonitoringAlerts({
          orgId: this.$store.getters.orgId,
          limit: 10
        })
        
        if (response.code === 200) {
          this.alerts = response.data || []
        }
      } catch (error) {
        console.error('加载预警信息失败:', error)
      }
    },

    // 标签页切换
    handleTabClick(tab) {
      this.activeTab = tab.name
    },

    // 获取预警类型
    getAlertType(level) {
      const typeMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[level] || 'info'
    },

    // 远期合约操作
    createForwardContract() {
      if (this.$refs.forwardRef) {
        this.$refs.forwardRef.showCreateDialog()
      }
    },

    viewForwardRates() {
      if (this.$refs.forwardRef) {
        this.$refs.forwardRef.showRatesDialog()
      }
    },

    checkDeliveryAlerts() {
      if (this.$refs.forwardRef) {
        this.$refs.forwardRef.showDeliveryAlerts()
      }
    },

    // 期权合约操作
    createOptionContract() {
      if (this.$refs.optionRef) {
        this.$refs.optionRef.showCreateDialog()
      }
    },

    calculateOptionPrice() {
      if (this.$refs.optionRef) {
        this.$refs.optionRef.showPricingDialog()
      }
    },

    viewGreeks() {
      if (this.$refs.optionRef) {
        this.$refs.optionRef.showGreeksDialog()
      }
    },

    // 风险管理操作
    checkRiskLimits() {
      if (this.$refs.riskRef) {
        this.$refs.riskRef.checkLimits()
      }
    },

    calculateVaR() {
      if (this.$refs.riskRef) {
        this.$refs.riskRef.showVaRDialog()
      }
    },

    performStressTest() {
      if (this.$refs.riskRef) {
        this.$refs.riskRef.showStressTestDialog()
      }
    },

    generateRiskReport() {
      if (this.$refs.riskRef) {
        this.$refs.riskRef.generateReport()
      }
    }
  }
}
</script>

<style scoped>
.derivatives-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  height: 100px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stats-icon i {
  font-size: 24px;
  color: white;
}

.forward-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.option-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.swap-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.total-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stats-info p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.function-nav {
  margin-bottom: 20px;
}

.quick-actions {
  margin-bottom: 20px;
}

.actions-header {
  margin-bottom: 15px;
}

.actions-header h3 {
  margin: 0;
  color: #303133;
}

.alerts-panel {
  margin-bottom: 20px;
}

.alerts-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.alerts-header h3 {
  margin: 0;
  color: #303133;
}

.alert-badge {
  margin-left: 10px;
}

.el-timeline-item h4 {
  margin: 0 0 5px 0;
  color: #303133;
}

.el-timeline-item p {
  margin: 0;
  color: #606266;
}
</style>
