<template>
  <div class="risk-warning-stats">
    <div class="stats-title">风险预警统计</div>
    <div class="stats-grid">
      <div class="stat-card critical">
        <div class="stat-icon">
          <i class="el-icon-warning"></i>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data?.criticalCount || 5 }}</div>
          <div class="stat-label">严重风险</div>
        </div>
      </div>
      <div class="stat-card high">
        <div class="stat-icon">
          <i class="el-icon-warning-outline"></i>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data?.highCount || 12 }}</div>
          <div class="stat-label">高风险</div>
        </div>
      </div>
      <div class="stat-card medium">
        <div class="stat-icon">
          <i class="el-icon-info"></i>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data?.mediumCount || 23 }}</div>
          <div class="stat-label">中风险</div>
        </div>
      </div>
      <div class="stat-card low">
        <div class="stat-icon">
          <i class="el-icon-success"></i>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data?.lowCount || 8 }}</div>
          <div class="stat-label">低风险</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RiskWarningStats',
  props: {
    data: {
      type: Object,
      default: null
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-warning-stats {
  height: 100%;
  display: flex;
  flex-direction: column;

  .stats-title {
    font-size: 14px;
    font-weight: bold;
    color: #409eff;
    text-align: center;
    margin-bottom: 10px;
    flex-shrink: 0;
  }

  .stats-grid {
    flex: 1;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
    min-height: 0;

    .stat-card {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px;
      border-radius: 6px;
      border: 2px solid;
      transition: all 0.3s ease;
      min-height: 0;

      &:hover {
        transform: scale(1.03);
        box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
      }

      &.critical {
        background: rgba(245, 108, 108, 0.1);
        border-color: #f56c6c;

        .stat-icon {
          color: #f56c6c;
        }
      }

      &.high {
        background: rgba(230, 162, 60, 0.1);
        border-color: #e6a23c;

        .stat-icon {
          color: #e6a23c;
        }
      }

      &.medium {
        background: rgba(64, 158, 255, 0.1);
        border-color: #409eff;

        .stat-icon {
          color: #409eff;
        }
      }

      &.low {
        background: rgba(103, 194, 58, 0.1);
        border-color: #67c23a;

        .stat-icon {
          color: #67c23a;
        }
      }

      .stat-icon {
        font-size: 28px;
        flex-shrink: 0;
      }

      .stat-info {
        flex: 1;
        min-width: 0;

        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #fff;
          line-height: 1;
          margin-bottom: 3px;
        }

        .stat-label {
          font-size: 11px;
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }
  }
}
</style>

