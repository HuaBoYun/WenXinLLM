<template>
  <div class="disposal-stats-panel">
    <div class="panel-title">
      <span class="title-bar"></span>
      <span>处置统计</span>
    </div>

    <div class="stats-grid">
      <div class="stat-item" v-for="item in statusItems" :key="item.key">
        <div class="item-num" :style="{ color: item.color }">{{ getCount(item.key) }}</div>
        <div class="item-label">{{ item.label }}</div>
        <div class="item-bar">
          <div
            class="item-bar-fill"
            :style="{ width: getWidth(item.key) + '%', background: item.color }"
          ></div>
        </div>
      </div>
    </div>

    <div class="rate-row">
      <div class="rate-label">处置率</div>
      <div class="rate-track">
        <div
          class="rate-fill"
          :style="{ width: processedRate + '%' }"
        ></div>
        <span class="rate-value">{{ processedRate }}%</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DisposalStatsPanel',
  props: {
    stats: { type: Object, default: () => ({}) },
    loading: { type: Boolean, default: false },
  },
  data() {
    return {
      statusItems: [
        { key: 'processedCount', label: '已处理', color: 'rgba(80, 220, 130, 0.9)' },
        { key: 'pendingCount', label: '待处理', color: 'rgba(255, 180, 0, 0.9)' },
        { key: 'processingCount', label: '处理中', color: 'rgba(0, 150, 255, 0.9)' },
        { key: 'ignoredCount', label: '已忽略', color: 'rgba(150, 150, 150, 0.7)' },
      ],
    }
  },
  computed: {
    totalCount() {
      return this.stats.totalCount || 0
    },
    processedCount() {
      return this.stats.processedCount || 0
    },
    processedRate() {
      if (!this.totalCount) return 0
      return Math.round((this.processedCount / this.totalCount) * 100)
    },
  },
  methods: {
    getCount(key) {
      return this.stats[key] || 0
    },
    getWidth(key) {
      if (!this.totalCount) return 0
      return Math.round(((this.stats[key] || 0) / this.totalCount) * 100)
    },
  },
}
</script>

<style lang="scss" scoped>
.disposal-stats-panel {
  background: rgba(0, 20, 60, 0.7);
  border: 1px solid rgba(0, 200, 255, 0.15);
  border-radius: 6px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  backdrop-filter: blur(4px);

  .panel-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 12px;
    font-weight: 600;
    color: rgba(0, 200, 255, 0.8);
    letter-spacing: 1px;
    flex-shrink: 0;

    .title-bar {
      display: inline-block;
      width: 3px;
      height: 12px;
      background: rgba(0, 200, 255, 0.7);
      border-radius: 2px;
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 6px;

    .stat-item {
      padding: 6px 8px;
      border-radius: 4px;
      background: rgba(255, 255, 255, 0.03);

      .item-num {
        font-size: 20px;
        font-weight: 700;
        font-family: 'Courier New', monospace;
        line-height: 1;
      }

      .item-label {
        font-size: 9px;
        color: rgba(255, 255, 255, 0.35);
        margin-top: 2px;
        letter-spacing: 1px;
      }

      .item-bar {
        height: 3px;
        background: rgba(255, 255, 255, 0.07);
        border-radius: 2px;
        margin-top: 4px;
        overflow: hidden;

        .item-bar-fill {
          height: 100%;
          border-radius: 2px;
          transition: width 0.8s ease;
          min-width: 2px;
          opacity: 0.7;
        }
      }
    }
  }

  .rate-row {
    display: flex;
    align-items: center;
    gap: 8px;
    padding-top: 4px;
    border-top: 1px solid rgba(255, 255, 255, 0.06);

    .rate-label {
      font-size: 10px;
      color: rgba(255, 255, 255, 0.3);
      flex-shrink: 0;
      letter-spacing: 1px;
    }

    .rate-track {
      flex: 1;
      height: 8px;
      background: rgba(255, 255, 255, 0.08);
      border-radius: 4px;
      position: relative;
      overflow: visible;

      .rate-fill {
        height: 100%;
        background: linear-gradient(90deg, rgba(0, 200, 100, 0.5), rgba(80, 220, 130, 0.9));
        border-radius: 4px;
        transition: width 0.8s ease;
        min-width: 2px;
      }

      .rate-value {
        position: absolute;
        right: -28px;
        top: 50%;
        transform: translateY(-50%);
        font-size: 10px;
        color: rgba(80, 220, 130, 0.8);
        font-weight: 600;
        font-family: monospace;
      }
    }
  }
}
</style>
