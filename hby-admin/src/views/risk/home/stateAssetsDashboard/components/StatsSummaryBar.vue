<template>
  <div class="stats-summary-bar">
    <div
      v-for="item in statItems"
      :key="item.key"
      class="stat-card"
      :class="'stat-card--' + item.type"
    >
      <div class="stat-icon">
        <i :class="item.icon"></i>
      </div>
      <div class="stat-content">
        <div class="stat-value">
          <span v-if="loading">-</span>
          <span v-else>{{ getValue(item.key) }}</span>
        </div>
        <div class="stat-label">{{ item.label }}</div>
      </div>
      <div class="stat-badge" v-if="item.badge">{{ item.badge }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatsSummaryBar',
  props: {
    stats: {
      type: Object,
      default: () => ({}),
    },
    loading: {
      type: Boolean,
      default: false,
    },
    modelCount: {
      type: Number,
      default: 0,
    },
  },
  computed: {
    statItems() {
      return [
        { key: 'modelCount', label: '模型总数', icon: 'el-icon-s-grid', type: 'blue' },
        { key: 'totalCount', label: '预警总数', icon: 'el-icon-warning', type: 'orange' },
        { key: 'highRiskCount', label: '高风险预警', icon: 'el-icon-warning-outline', type: 'red' },
        { key: 'pendingCount', label: '待处理', icon: 'el-icon-time', type: 'yellow' },
        { key: 'processedCount', label: '已处理', icon: 'el-icon-success', type: 'green' },
        { key: 'todayCount', label: '今日新增', icon: 'el-icon-date', type: 'purple' },
      ]
    },
  },
  methods: {
    getValue(key) {
      if (key === 'modelCount') return this.stats.modelCount || this.modelCount || 0
      return this.stats[key] !== undefined ? this.stats[key] : 0
    },
  },
}
</script>

<style lang="scss" scoped>
.stats-summary-bar {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;
  margin-bottom: 12px;

  @media (max-width: 1400px) {
    grid-template-columns: repeat(3, 1fr);
  }
  @media (max-width: 900px) {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-card {
    position: relative;
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px 16px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    transition: transform 0.2s, box-shadow 0.2s;
    cursor: default;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 18px rgba(0, 0, 0, 0.12);
    }

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 4px;
      border-radius: 8px 0 0 8px;
    }

    &--blue::before { background: #1976d2; }
    &--orange::before { background: #f57c00; }
    &--red::before { background: #d32f2f; }
    &--yellow::before { background: #f9a825; }
    &--green::before { background: #388e3c; }
    &--purple::before { background: #7b1fa2; }

    .stat-icon {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      font-size: 20px;
    }

    &--blue .stat-icon { background: #e3f2fd; color: #1976d2; }
    &--orange .stat-icon { background: #fff3e0; color: #f57c00; }
    &--red .stat-icon { background: #ffebee; color: #d32f2f; }
    &--yellow .stat-icon { background: #fffde7; color: #f9a825; }
    &--green .stat-icon { background: #e8f5e9; color: #388e3c; }
    &--purple .stat-icon { background: #f3e5f5; color: #7b1fa2; }

    .stat-content {
      flex: 1;
      min-width: 0;

      .stat-value {
        font-size: 24px;
        font-weight: 700;
        color: #263238;
        line-height: 1.2;
      }

      .stat-label {
        font-size: 12px;
        color: #78909c;
        margin-top: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}
</style>
