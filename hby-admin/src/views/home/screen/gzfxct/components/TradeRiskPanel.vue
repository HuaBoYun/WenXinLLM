<template>
  <div class="trade-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-goods"></i>
      <h4>贸易风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-cards">
        <div class="card-item">
          <div class="card-header">
            <i class="el-icon-warning" style="color: #f56c6c;"></i>
            <span>虚假贸易（融资性）</span>
          </div>
          <div class="card-value">{{ riskData?.fakeTradeFin || 8 }}</div>
        </div>
        <div class="card-item">
          <div class="card-header">
            <i class="el-icon-warning-outline" style="color: #e6a23c;"></i>
            <span>虚假贸易（空转）</span>
          </div>
          <div class="card-value">{{ riskData?.fakeTradeEmpty || 6 }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TradeRiskPanel',
  props: {
    riskData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    getRiskLevel() {
      const total = (this.riskData?.fakeTradeFin || 8) +
                   (this.riskData?.fakeTradeEmpty || 6)
      
      if (total >= 12) return '高风险'
      if (total >= 6) return '中风险'
      return '低风险'
    },
    getRiskLevelType() {
      const level = this.getRiskLevel()
      if (level === '高风险') return 'danger'
      if (level === '中风险') return 'warning'
      return 'success'
    }
  }
}
</script>

<style lang="scss" scoped>
.trade-risk-panel {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
    color: #303133;
    border-radius: 8px 8px 0 0;

    i {
      font-size: 20px;
    }

    h4 {
      margin: 0;
      flex: 1;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .panel-content {
    padding: 20px;

    .risk-cards {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;

      .card-item {
        padding: 20px;
        background: #f5f7fa;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .card-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 15px;

          i {
            font-size: 20px;
          }

          span {
            font-size: 14px;
            color: #606266;
            font-weight: 500;
          }
        }

        .card-value {
          font-size: 36px;
          font-weight: bold;
          color: #303133;
          text-align: center;
        }
      }
    }
  }
}
</style>

