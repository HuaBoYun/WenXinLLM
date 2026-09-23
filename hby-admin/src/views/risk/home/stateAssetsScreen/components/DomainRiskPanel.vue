<template>
  <div class="domain-risk-panel">
    <div class="panel-title">
      <span class="title-bar"></span>
      <span>12大监管领域</span>
    </div>
    <div class="domain-list">
      <div
        v-for="domain in domainWithStats"
        :key="domain.key"
        class="domain-item"
        :class="'domain-item--' + domain.riskLevel"
      >
        <!-- 状态指示灯 -->
        <div class="indicator-lamp" :class="'lamp--' + domain.riskLevel">
          <div class="lamp-inner"></div>
        </div>
        <!-- 领域图标 -->
        <div class="domain-emoji">{{ domain.icon }}</div>
        <!-- 信息 -->
        <div class="domain-info">
          <div class="domain-name">{{ domain.name }}</div>
          <div class="domain-stats">
            <span class="stat-models">{{ domain.modelCount }}模型</span>
            <span class="stat-warnings" :class="domain.warningCount > 0 ? 'has-warning' : ''">
              {{ domain.warningCount }}预警
            </span>
          </div>
        </div>
        <!-- 风险条 -->
        <div class="risk-bar-wrap">
          <div
            class="risk-bar"
            :class="'risk-bar--' + domain.riskLevel"
            :style="{ width: getRiskBarWidth(domain) + '%' }"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DomainRiskPanel',
  props: {
    domainList: { type: Array, default: () => [] },
    modelList: { type: Array, default: () => [] },
    warningList: { type: Array, default: () => [] },
  },
  computed: {
    maxWarning() {
      return Math.max(1, ...this.domainWithStats.map((d) => d.warningCount))
    },
    domainWithStats() {
      return this.domainList.map((domain) => {
        const modelCount = this.modelList.filter((m) =>
          (m.category || m.combinationName || '').includes(domain.name.replace('穿透', ''))
        ).length
        const warningCount = this.warningList.filter((w) =>
          (w.warningType || '').toLowerCase().includes(domain.key)
        ).length
        let riskLevel = 'normal'
        if (warningCount >= 10) riskLevel = 'high'
        else if (warningCount >= 3) riskLevel = 'medium'
        else if (warningCount >= 1) riskLevel = 'low'
        return { ...domain, modelCount, warningCount, riskLevel }
      })
    },
  },
  methods: {
    getRiskBarWidth(domain) {
      if (!this.maxWarning) return 5
      return Math.max(5, Math.min(100, (domain.warningCount / this.maxWarning) * 100))
    },
  },
}
</script>

<style lang="scss" scoped>
.domain-risk-panel {
  background: rgba(0, 20, 60, 0.7);
  border: 1px solid rgba(0, 200, 255, 0.2);
  border-radius: 6px;
  padding: 10px;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  backdrop-filter: blur(4px);

  .panel-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;
    font-weight: 600;
    color: rgba(0, 200, 255, 0.9);
    margin-bottom: 10px;
    letter-spacing: 1px;
    flex-shrink: 0;

    .title-bar {
      display: inline-block;
      width: 3px;
      height: 14px;
      background: rgba(0, 200, 255, 0.8);
      border-radius: 2px;
    }
  }

  .domain-list {
    flex: 1;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 6px;

    &::-webkit-scrollbar {
      width: 3px;
    }
    &::-webkit-scrollbar-track { background: transparent; }
    &::-webkit-scrollbar-thumb { background: rgba(0, 200, 255, 0.2); border-radius: 2px; }
  }

  .domain-item {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 8px;
    border-radius: 4px;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.04);
    transition: all 0.2s;

    &:hover {
      background: rgba(0, 200, 255, 0.08);
      border-color: rgba(0, 200, 255, 0.15);
    }

    &--high { border-left: 2px solid rgba(255, 60, 60, 0.7); }
    &--medium { border-left: 2px solid rgba(255, 160, 0, 0.7); }
    &--low { border-left: 2px solid rgba(0, 150, 255, 0.7); }
    &--normal { border-left: 2px solid rgba(0, 200, 100, 0.7); }

    .indicator-lamp {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      flex-shrink: 0;
      display: flex;
      align-items: center;
      justify-content: center;

      &--high { background: rgba(255, 60, 60, 0.2); border: 1px solid rgba(255, 60, 60, 0.6); }
      &--medium { background: rgba(255, 160, 0, 0.2); border: 1px solid rgba(255, 160, 0, 0.6); }
      &--low { background: rgba(0, 150, 255, 0.2); border: 1px solid rgba(0, 150, 255, 0.6); }
      &--normal { background: rgba(0, 200, 100, 0.2); border: 1px solid rgba(0, 200, 100, 0.6); }

      .lamp-inner {
        width: 5px;
        height: 5px;
        border-radius: 50%;
      }

      &--high .lamp-inner { background: rgba(255, 60, 60, 0.9); animation: lampBlink 1s infinite; }
      &--medium .lamp-inner { background: rgba(255, 160, 0, 0.9); animation: lampBlink 2s infinite; }
      &--low .lamp-inner { background: rgba(0, 150, 255, 0.9); }
      &--normal .lamp-inner { background: rgba(0, 200, 100, 0.9); }
    }

    .domain-emoji {
      font-size: 14px;
      flex-shrink: 0;
      width: 20px;
      text-align: center;
    }

    .domain-info {
      flex: 1;
      min-width: 0;

      .domain-name {
        font-size: 11px;
        color: rgba(255, 255, 255, 0.85);
        font-weight: 500;
        white-space: nowrap;
      }

      .domain-stats {
        display: flex;
        gap: 6px;
        margin-top: 1px;

        .stat-models {
          font-size: 9px;
          color: rgba(0, 200, 255, 0.5);
        }

        .stat-warnings {
          font-size: 9px;
          color: rgba(255, 255, 255, 0.3);

          &.has-warning {
            color: rgba(255, 160, 0, 0.8);
            font-weight: 600;
          }
        }
      }
    }

    .risk-bar-wrap {
      width: 50px;
      height: 4px;
      background: rgba(255, 255, 255, 0.07);
      border-radius: 2px;
      overflow: hidden;
      flex-shrink: 0;

      .risk-bar {
        height: 100%;
        border-radius: 2px;
        transition: width 0.8s ease;

        &--high { background: linear-gradient(90deg, rgba(255, 60, 60, 0.6), rgba(255, 100, 100, 0.9)); }
        &--medium { background: linear-gradient(90deg, rgba(255, 160, 0, 0.6), rgba(255, 200, 0, 0.9)); }
        &--low { background: linear-gradient(90deg, rgba(0, 150, 255, 0.6), rgba(0, 200, 255, 0.9)); }
        &--normal { background: linear-gradient(90deg, rgba(0, 200, 100, 0.4), rgba(0, 230, 120, 0.7)); }
      }
    }
  }
}

@keyframes lampBlink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}
</style>
