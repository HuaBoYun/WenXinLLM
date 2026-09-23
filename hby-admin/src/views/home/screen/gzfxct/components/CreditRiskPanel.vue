<template>
  <div class="credit-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-bank-card"></i>
      <h4>信用风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-items">
        <div class="risk-item">
          <div class="item-label">信用风控监测</div>
          <div class="item-value danger">{{ riskData?.creditMonitoring || 7 }}</div>
        </div>
        <div class="risk-item">
          <div class="item-label">债务风险预测</div>
          <div class="item-value warning">{{ riskData?.debtPrediction || 5 }}</div>
        </div>
        <div class="risk-item">
          <div class="item-label">贷款违约风险</div>
          <div class="item-value danger">{{ riskData?.loanDefault || 6 }}</div>
        </div>
        <div class="risk-item">
          <div class="item-label">对外借款预测</div>
          <div class="item-value warning">{{ riskData?.externalLoan || 4 }}</div>
        </div>
        <div class="risk-item">
          <div class="item-label">担保预测</div>
          <div class="item-value success">{{ riskData?.guarantee || 3 }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CreditRiskPanel',
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
      const total = (this.riskData?.creditMonitoring || 7) +
                   (this.riskData?.debtPrediction || 5) +
                   (this.riskData?.loanDefault || 6) +
                   (this.riskData?.externalLoan || 4) +
                   (this.riskData?.guarantee || 3)
      
      if (total >= 20) return '高风险'
      if (total >= 10) return '中风险'
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
.credit-risk-panel {
  height: 100%;
  display: flex;
  flex-direction: column;

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    color: white;
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
    flex: 1;
    padding: 20px;

    .risk-items {
      display: flex;
      flex-direction: column;
      gap: 15px;

      .risk-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px;
        background: #f5f7fa;
        border-radius: 6px;
        border-left: 4px solid #409eff;
        transition: all 0.3s ease;

        &:hover {
          transform: translateX(5px);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .item-label {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }

        .item-value {
          font-size: 24px;
          font-weight: bold;

          &.danger {
            color: #f56c6c;
          }

          &.warning {
            color: #e6a23c;
          }

          &.success {
            color: #67c23a;
          }
        }
      }
    }
  }
}
</style>

