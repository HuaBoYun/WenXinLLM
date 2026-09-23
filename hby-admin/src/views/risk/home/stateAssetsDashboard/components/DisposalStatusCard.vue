<template>
  <div class="disposal-status-card">
    <div class="card-header">
      <span class="card-title"><i class="el-icon-s-check"></i> 处置状态统计</span>
    </div>
    <div class="card-body" v-loading="loading">
      <!-- 环形进度图（纯CSS实现） -->
      <div class="ring-chart">
        <svg viewBox="0 0 120 120" class="ring-svg">
          <circle cx="60" cy="60" r="50" fill="none" stroke="#eceff1" stroke-width="10" />
          <circle
            cx="60"
            cy="60"
            r="50"
            fill="none"
            stroke="#4caf50"
            stroke-width="10"
            stroke-linecap="round"
            :stroke-dasharray="processedDash"
            stroke-dashoffset="-78.54"
            transform="rotate(-90 60 60)"
          />
        </svg>
        <div class="ring-center">
          <div class="ring-percent">{{ processedRate }}%</div>
          <div class="ring-label">处置率</div>
        </div>
      </div>

      <!-- 状态分布列表 -->
      <div class="status-list">
        <div
          v-for="item in statusItems"
          :key="item.key"
          class="status-item"
        >
          <div class="status-dot" :style="{ background: item.color }"></div>
          <span class="status-name">{{ item.label }}</span>
          <div class="status-bar-wrap">
            <div
              class="status-bar"
              :style="{
                width: getBarWidth(item.key) + '%',
                background: item.color,
              }"
            ></div>
          </div>
          <span class="status-count">{{ getCount(item.key) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DisposalStatusCard',
  props: {
    stats: {
      type: Object,
      default: () => ({}),
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      statusItems: [
        { key: 'processedCount', label: '已处理', color: '#4caf50' },
        { key: 'pendingCount', label: '待处理', color: '#ff9800' },
        { key: 'processingCount', label: '处理中', color: '#2196f3' },
        { key: 'ignoredCount', label: '已忽略', color: '#9e9e9e' },
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
    processedDash() {
      const circumference = 2 * Math.PI * 50 // ≈ 314.16
      const filled = (this.processedRate / 100) * circumference
      return `${filled} ${circumference}`
    },
  },
  methods: {
    getCount(key) {
      return this.stats[key] || 0
    },
    getBarWidth(key) {
      if (!this.totalCount) return 0
      return Math.round(((this.stats[key] || 0) / this.totalCount) * 100)
    },
  },
}
</script>

<style lang="scss" scoped>
.disposal-status-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;

  .card-header {
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #e8f5e9;

    .card-title {
      font-size: 14px;
      font-weight: 600;
      color: #2e7d32;
      display: flex;
      align-items: center;
      gap: 6px;
    }
  }

  .card-body {
    flex: 1;
    padding: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16px;

    .ring-chart {
      position: relative;
      width: 120px;
      height: 120px;
      flex-shrink: 0;

      .ring-svg {
        width: 100%;
        height: 100%;

        circle {
          transition: stroke-dasharray 0.8s ease;
        }
      }

      .ring-center {
        position: absolute;
        inset: 0;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .ring-percent {
          font-size: 22px;
          font-weight: 700;
          color: #263238;
          line-height: 1.1;
        }

        .ring-label {
          font-size: 11px;
          color: #78909c;
          margin-top: 2px;
        }
      }
    }

    .status-list {
      width: 100%;

      .status-item {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 10px;

        .status-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          flex-shrink: 0;
        }

        .status-name {
          font-size: 12px;
          color: #546e7a;
          width: 50px;
          flex-shrink: 0;
        }

        .status-bar-wrap {
          flex: 1;
          height: 6px;
          background: #eceff1;
          border-radius: 3px;
          overflow: hidden;

          .status-bar {
            height: 100%;
            border-radius: 3px;
            transition: width 0.6s ease;
            min-width: 2px;
          }
        }

        .status-count {
          font-size: 12px;
          font-weight: 600;
          color: #37474f;
          min-width: 24px;
          text-align: right;
        }
      }
    }
  }
}
</style>
