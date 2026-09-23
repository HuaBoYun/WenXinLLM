<template>
  <div class="warning-scroll-panel">
    <div class="panel-left">
      <div class="panel-label">
        <i class="warn-icon">&#9888;</i>
        <span>风险预警播报</span>
      </div>
      <div class="total-badge">
        共 <strong>{{ warningList.length }}</strong> 条
      </div>
    </div>

    <div class="scroll-track">
      <!-- 渐变遮罩 -->
      <div class="mask-left"></div>
      <div class="scroll-inner" ref="scrollInner">
        <div class="scroll-items" :style="scrollStyle">
          <div
            v-for="(item, idx) in loopItems"
            :key="idx"
            class="warning-item"
            :class="'warning-item--' + getLevelKey(item.warningLevel)"
          >
            <span class="w-level-dot"></span>
            <span class="w-code">{{ item.warningCode || ('W' + String(idx + 1).padStart(4, '0')) }}</span>
            <span class="w-type">{{ getTypeShort(item.warningType) }}</span>
            <span class="w-company" :title="item.companyName">{{ item.companyName || '-' }}</span>
            <span class="w-desc" :title="item.warningDescription">{{ item.warningDescription || '无描述' }}</span>
            <span class="w-time">{{ formatTime(item.warningTime) }}</span>
          </div>
        </div>
      </div>
      <div class="mask-right"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WarningScrollPanel',
  props: {
    warningList: { type: Array, default: () => [] },
    loading: { type: Boolean, default: false },
  },
  data() {
    return {
      offset: 0,
      _animTimer: null,
      itemWidth: 280,
    }
  },
  computed: {
    loopItems() {
      // 复制两份以实现无缝滚动
      return this.warningList.length > 0
        ? [...this.warningList, ...this.warningList]
        : Array(5).fill({
            warningCode: 'W0001',
            warningType: 'FINANCIAL_RISK',
            warningLevel: 'MEDIUM',
            companyName: '暂无数据',
            warningDescription: '等待数据加载...',
            warningTime: '',
          })
    },
    scrollStyle() {
      return {
        transform: `translateX(-${this.offset}px)`,
        transition: this.offset === 0 ? 'none' : 'transform 0.4s linear',
      }
    },
    totalWidth() {
      return this.warningList.length * this.itemWidth
    },
  },
  mounted() {
    this.startScroll()
  },
  beforeDestroy() {
    if (this._animTimer) clearInterval(this._animTimer)
  },
  watch: {
    warningList() {
      this.offset = 0
    },
  },
  methods: {
    startScroll() {
      this._animTimer = setInterval(() => {
        if (!this.warningList.length) return
        this.offset += this.itemWidth
        if (this.offset >= this.totalWidth) {
          setTimeout(() => { this.offset = 0 }, 400)
        }
      }, 3000)
    },
    getLevelKey(level) {
      const map = { HIGH: 'high', MEDIUM: 'medium', LOW: 'low' }
      return map[level] || 'normal'
    },
    getTypeShort(type) {
      const map = {
        FINANCIAL_RISK: '财务',
        PROCUREMENT_RISK: '采购',
        CREDIT_RISK: '信用',
        COMPLIANCE_RISK: '合规',
        COMBINATION_EXECUTION: '组合',
        DATA_MODEL_EXECUTION: '模型',
        AUTO_GENERATED: '自动',
        MANUAL_CREATED: '手动',
        THRESHOLD: '阈值',
        TREND: '趋势',
        ANOMALY: '异常',
      }
      return map[type] || '其他'
    },
    formatTime(t) {
      if (!t) return '-'
      return String(t).substring(0, 16)
    },
  },
}
</script>

<style lang="scss" scoped>
.warning-scroll-panel {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 100%;
  background: rgba(0, 20, 60, 0.8);
  border: 1px solid rgba(255, 140, 0, 0.2);
  border-radius: 6px;
  padding: 0 12px;
  overflow: hidden;
  backdrop-filter: blur(4px);

  .panel-left {
    display: flex;
    flex-direction: column;
    gap: 4px;
    flex-shrink: 0;
    width: 100px;
    border-right: 1px solid rgba(255, 140, 0, 0.15);
    padding-right: 10px;

    .panel-label {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      font-weight: 600;
      color: rgba(255, 180, 0, 0.9);

      .warn-icon {
        font-size: 14px;
      }
    }

    .total-badge {
      font-size: 10px;
      color: rgba(255, 255, 255, 0.4);

      strong {
        color: rgba(255, 180, 0, 0.8);
        font-size: 14px;
      }
    }
  }

  .scroll-track {
    flex: 1;
    position: relative;
    overflow: hidden;
    height: 80px;
    display: flex;
    align-items: center;

    .mask-left,
    .mask-right {
      position: absolute;
      top: 0;
      bottom: 0;
      width: 40px;
      z-index: 2;
      pointer-events: none;
    }

    .mask-left {
      left: 0;
      background: linear-gradient(90deg, rgba(0, 20, 60, 0.9), transparent);
    }

    .mask-right {
      right: 0;
      background: linear-gradient(-90deg, rgba(0, 20, 60, 0.9), transparent);
    }

    .scroll-inner {
      overflow: hidden;
      width: 100%;
    }

    .scroll-items {
      display: flex;
      gap: 0;
      will-change: transform;
    }

    .warning-item {
      display: flex;
      align-items: center;
      gap: 8px;
      width: 280px;
      flex-shrink: 0;
      padding: 8px 10px;
      margin: 0 4px;
      border-radius: 4px;
      background: rgba(255, 255, 255, 0.04);
      border: 1px solid rgba(255, 255, 255, 0.06);

      &--high {
        border-color: rgba(255, 60, 60, 0.3);
        background: rgba(255, 30, 30, 0.08);
        .w-level-dot { background: rgba(255, 60, 60, 0.9); box-shadow: 0 0 4px rgba(255, 60, 60, 0.5); }
      }
      &--medium {
        border-color: rgba(255, 160, 0, 0.3);
        background: rgba(255, 140, 0, 0.06);
        .w-level-dot { background: rgba(255, 160, 0, 0.9); }
      }
      &--low {
        border-color: rgba(0, 150, 255, 0.3);
        .w-level-dot { background: rgba(0, 150, 255, 0.9); }
      }
      &--normal {
        .w-level-dot { background: rgba(0, 200, 100, 0.7); }
      }

      .w-level-dot {
        width: 6px;
        height: 6px;
        border-radius: 50%;
        flex-shrink: 0;
      }

      .w-code {
        font-size: 10px;
        color: rgba(0, 200, 255, 0.7);
        font-family: monospace;
        flex-shrink: 0;
        width: 54px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .w-type {
        font-size: 9px;
        padding: 1px 4px;
        border-radius: 3px;
        background: rgba(255, 255, 255, 0.07);
        color: rgba(255, 255, 255, 0.5);
        flex-shrink: 0;
      }

      .w-company {
        font-size: 10px;
        color: rgba(255, 255, 255, 0.6);
        width: 60px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        flex-shrink: 0;
      }

      .w-desc {
        flex: 1;
        font-size: 10px;
        color: rgba(255, 255, 255, 0.5);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .w-time {
        font-size: 9px;
        color: rgba(255, 255, 255, 0.25);
        flex-shrink: 0;
        font-family: monospace;
      }
    }
  }
}
</style>
