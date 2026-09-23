<template>
  <div class="credit-risk-chart">
    <div class="risk-list">
      <div class="risk-item" v-for="(item, index) in riskItems" :key="index">
        <span class="label">{{ item.label }}</span>
        <div class="progress-bar">
          <div class="progress" :style="{ width: item.value * 10 + '%', background: item.color }"></div>
        </div>
        <span class="value">{{ item.value }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CreditRiskChart',
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  computed: {
    riskItems() {
      return [
        { label: '信用风控', value: this.data?.creditMonitoring || 7, color: '#f56c6c' },
        { label: '债务预测', value: this.data?.debtPrediction || 5, color: '#e6a23c' },
        { label: '贷款违约', value: this.data?.loanDefault || 6, color: '#409eff' },
        { label: '对外借款', value: this.data?.externalLoan || 4, color: '#67c23a' },
        { label: '担保预测', value: this.data?.guarantee || 3, color: '#909399' }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.credit-risk-chart {
  height: 100%;
  width: 100%;
  padding: 5px;
  min-height: 0;

  .risk-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
    height: 100%;

    .risk-item {
      display: flex;
      align-items: center;
      gap: 8px;

      .label {
        width: 60px;
        font-size: 11px;
        color: #fff;
        flex-shrink: 0;
      }

      .progress-bar {
        flex: 1;
        height: 10px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 5px;
        overflow: hidden;
        min-width: 0;

        .progress {
          height: 100%;
          border-radius: 5px;
          transition: width 0.3s ease;
        }
      }

      .value {
        width: 25px;
        text-align: right;
        font-size: 12px;
        font-weight: bold;
        color: #409eff;
        flex-shrink: 0;
      }
    }
  }
}
</style>

