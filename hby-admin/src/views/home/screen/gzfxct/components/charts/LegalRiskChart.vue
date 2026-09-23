<template>
  <div class="legal-risk-chart">
    <div class="risk-cards">
      <div class="card" v-for="(item, index) in riskItems" :key="index" :style="{ borderColor: item.color }">
        <div class="card-value" :style="{ color: item.color }">{{ item.value }}</div>
        <div class="card-label">{{ item.label }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LegalRiskChart',
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  computed: {
    riskItems() {
      return [
        { label: '合规内控', value: this.data?.complianceRisk || 6, color: '#f56c6c' },
        { label: '捞偏门', value: this.data?.illegalBusiness || 4, color: '#e6a23c' },
        { label: '法律诉讼', value: this.data?.litigation || 3, color: '#409eff' }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.legal-risk-chart {
  height: 100%;
  width: 100%;
  padding: 5px;
  min-height: 0;

  .risk-cards {
    display: flex;
    gap: 8px;
    height: 100%;

    .card {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: rgba(255, 255, 255, 0.05);
      border: 2px solid;
      border-radius: 6px;
      padding: 10px;
      transition: all 0.3s ease;
      min-width: 0;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
      }

      .card-value {
        font-size: 28px;
        font-weight: bold;
        margin-bottom: 5px;
      }

      .card-label {
        font-size: 11px;
        color: #fff;
      }
    }
  }
}
</style>

