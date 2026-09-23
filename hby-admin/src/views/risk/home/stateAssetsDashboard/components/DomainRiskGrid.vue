<template>
  <div class="domain-risk-grid">
    <div class="grid-header">
      <i class="el-icon-s-flag"></i>
      <span>12大领域风险概览</span>
    </div>
    <div class="grid-body">
      <div
        v-for="domain in domainWithStats"
        :key="domain.key"
        class="domain-card"
        :class="'domain-card--' + domain.riskLevel"
        @click="handleClick(domain)"
      >
        <div class="domain-indicator" :class="'indicator--' + domain.riskLevel"></div>
        <div class="domain-icon">
          <i :class="domain.icon"></i>
        </div>
        <div class="domain-info">
          <div class="domain-name">{{ domain.name }}</div>
          <div class="domain-meta">
            <span class="meta-item">
              <i class="el-icon-s-grid"></i>{{ domain.modelCount }}个模型
            </span>
            <span class="meta-item" :class="'warning-count--' + domain.riskLevel">
              <i class="el-icon-warning-outline"></i>{{ domain.warningCount }}条预警
            </span>
          </div>
        </div>
        <div class="risk-badge" :class="'risk-badge--' + domain.riskLevel">
          {{ riskLevelText(domain.riskLevel) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DomainRiskGrid',
  props: {
    domainList: {
      type: Array,
      default: () => [],
    },
    modelList: {
      type: Array,
      default: () => [],
    },
    warningList: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
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
    riskLevelText(level) {
      const map = { high: '高风险', medium: '中风险', low: '低风险', normal: '正常' }
      return map[level] || '正常'
    },
    handleClick(domain) {
      if (domain.path) {
        this.$router.push(domain.path).catch(() => {})
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.domain-risk-grid {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;

  .grid-header {
    padding: 10px 16px;
    background: linear-gradient(90deg, #f5f7fa 0%, #ffffff 100%);
    border-bottom: 2px solid #e3f2fd;
    font-size: 14px;
    font-weight: 600;
    color: #1565c0;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .grid-body {
    flex: 1;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 8px;
    padding: 10px;
    overflow: auto;

    @media (max-width: 1400px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  .domain-card {
    position: relative;
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 10px 10px 14px;
    border-radius: 6px;
    border: 1px solid #e8edf2;
    background: #f8fafe;
    cursor: pointer;
    transition: all 0.2s;
    overflow: hidden;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(21, 101, 192, 0.15);
      border-color: #90caf9;
    }

    &--high {
      background: #fff5f5;
      border-color: #ffcdd2;
      &:hover { border-color: #ef9a9a; }
    }
    &--medium {
      background: #fffbf0;
      border-color: #ffe082;
    }
    &--low {
      background: #f0f8ff;
      border-color: #b3e5fc;
    }

    .domain-indicator {
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 4px;
      &--high { background: #f44336; }
      &--medium { background: #ff9800; }
      &--low { background: #2196f3; }
      &--normal { background: #4caf50; }
    }

    .domain-icon {
      width: 32px;
      height: 32px;
      border-radius: 6px;
      background: linear-gradient(135deg, #1565c0, #42a5f5);
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 16px;
      flex-shrink: 0;
    }

    .domain-info {
      flex: 1;
      min-width: 0;

      .domain-name {
        font-size: 13px;
        font-weight: 600;
        color: #263238;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .domain-meta {
        display: flex;
        gap: 8px;
        margin-top: 2px;

        .meta-item {
          font-size: 11px;
          color: #78909c;
          display: flex;
          align-items: center;
          gap: 2px;
        }

        .warning-count--high { color: #f44336; font-weight: 600; }
        .warning-count--medium { color: #ff9800; font-weight: 600; }
        .warning-count--low { color: #2196f3; }
      }
    }

    .risk-badge {
      position: absolute;
      top: 4px;
      right: 4px;
      font-size: 10px;
      padding: 1px 5px;
      border-radius: 8px;
      font-weight: 600;

      &--high { background: #ffebee; color: #c62828; }
      &--medium { background: #fff8e1; color: #e65100; }
      &--low { background: #e3f2fd; color: #1565c0; }
      &--normal { background: #e8f5e9; color: #2e7d32; }
    }
  }
}
</style>
