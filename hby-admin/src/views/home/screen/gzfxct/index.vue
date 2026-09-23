<template>
  <div class="gzfxct-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>国资风险穿透</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreen"
          class="screen-btn"
        >
          大屏
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 风险概览卡片 -->
      <div class="overview-section">
        <risk-overview-card :overview-data="overviewData" :loading="loading" />
      </div>

      <!-- 风险分类监控 -->
      <div class="risk-categories">
        <!-- 投资风险 -->
        <div class="risk-category-card">
          <investment-risk-panel :risk-data="investmentRiskData" :loading="loading" />
        </div>

        <!-- 集团管控风险 -->
        <div class="risk-category-card">
          <group-control-risk-panel :risk-data="groupControlRiskData" :loading="loading" />
        </div>

        <!-- 财务风险 -->
        <div class="risk-category-card">
          <financial-risk-panel :risk-data="financialRiskData" :loading="loading" />
        </div>

        <!-- 信用风险 -->
        <div class="risk-category-card">
          <credit-risk-panel :risk-data="creditRiskData" :loading="loading" />
        </div>
      </div>

      <!-- 经营风险 -->
      <div class="business-risk-section">
        <business-risk-panel :risk-data="businessRiskData" :loading="loading" />
      </div>

      <!-- 采购风险 -->
      <div class="procurement-risk-section">
        <procurement-risk-panel :risk-data="procurementRiskData" :loading="loading" />
      </div>

      <!-- 法律风险 -->
      <div class="legal-risk-section">
        <legal-risk-panel :risk-data="legalRiskData" :loading="loading" />
      </div>

      <!-- 贸易风险 -->
      <div class="trade-risk-section">
        <trade-risk-panel :risk-data="tradeRiskData" :loading="loading" />
      </div>
    </div>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <screen-display
        v-if="screenVisible"
        :all-risk-data="allRiskData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
import RiskOverviewCard from './components/RiskOverviewCard.vue'
import InvestmentRiskPanel from './components/InvestmentRiskPanel.vue'
import GroupControlRiskPanel from './components/GroupControlRiskPanel.vue'
import FinancialRiskPanel from './components/FinancialRiskPanel.vue'
import CreditRiskPanel from './components/CreditRiskPanel.vue'
import BusinessRiskPanel from './components/BusinessRiskPanel.vue'
import ProcurementRiskPanel from './components/ProcurementRiskPanel.vue'
import LegalRiskPanel from './components/LegalRiskPanel.vue'
import TradeRiskPanel from './components/TradeRiskPanel.vue'
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getRiskMonitorData } from './api/riskMonitor'

export default {
  name: 'GzfxctIndex',
  components: {
    RiskOverviewCard,
    InvestmentRiskPanel,
    GroupControlRiskPanel,
    FinancialRiskPanel,
    CreditRiskPanel,
    BusinessRiskPanel,
    ProcurementRiskPanel,
    LegalRiskPanel,
    TradeRiskPanel,
    ScreenDisplay
  },
  data() {
    return {
      loading: false,
      screenVisible: false,
      overviewData: null,
      investmentRiskData: null,
      groupControlRiskData: null,
      financialRiskData: null,
      creditRiskData: null,
      businessRiskData: null,
      procurementRiskData: null,
      legalRiskData: null,
      tradeRiskData: null,
      refreshTimer: null
    }
  },
  computed: {
    allRiskData() {
      return {
        overview: this.overviewData,
        investment: this.investmentRiskData,
        groupControl: this.groupControlRiskData,
        financial: this.financialRiskData,
        credit: this.creditRiskData,
        business: this.businessRiskData,
        procurement: this.procurementRiskData,
        legal: this.legalRiskData,
        trade: this.tradeRiskData
      }
    }
  },
  mounted() {
    this.loadAllRiskData()
    // 每5分钟自动刷新数据
    this.refreshTimer = setInterval(() => {
      this.loadAllRiskData()
    }, 5 * 60 * 1000)
  },
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  methods: {
    async loadAllRiskData() {
      this.loading = true
      try {
        const data = await getRiskMonitorData()
        this.overviewData = data.overview
        this.investmentRiskData = data.investment
        this.groupControlRiskData = data.groupControl
        this.financialRiskData = data.financial
        this.creditRiskData = data.credit
        this.businessRiskData = data.business
        this.procurementRiskData = data.procurement
        this.legalRiskData = data.legal
        this.tradeRiskData = data.trade
      } catch (error) {
        console.error('加载风险数据失败:', error)
        this.$message.error('加载风险数据失败')
      } finally {
        this.loading = false
      }
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    },
    handleScreenClose() {
      this.screenVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.gzfxct-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    padding: 20px 30px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;

    .header-left {
      display: flex;
      align-items: center;
      gap: 12px;

      i {
        font-size: 28px;
        color: #409eff;
      }

      h2 {
        margin: 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }
    }

    .header-right {
      .screen-btn {
        font-size: 16px;
        padding: 12px 24px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        }
      }
    }
  }

  .main-content {
    .overview-section {
      margin-bottom: 20px;
    }

    .risk-categories {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20px;
      margin-bottom: 20px;
    }

    .business-risk-section,
    .procurement-risk-section,
    .legal-risk-section,
    .trade-risk-section {
      margin-bottom: 20px;
    }

    .risk-category-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      overflow: hidden;
    }
  }
}

::v-deep .screen-dialog {
  background: #0a0e27 !important;
  margin: 0 !important;
  padding: 0 !important;

  .el-dialog__header {
    display: none !important;
  }

  .el-dialog__body {
    padding: 0 !important;
    height: 100vh !important;
    overflow: hidden !important;
  }
}

@media (max-width: 1600px) {
  .gzfxct-container {
    .main-content {
      .risk-categories {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style>

