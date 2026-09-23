<template>
  <div class="legal-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-document-checked"></i>
      <h4>法律风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-stats">
        <div class="stat-item">
          <div class="stat-label">合规内控风险</div>
          <div class="stat-value danger">{{ riskData?.complianceRisk || 6 }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-label">捞偏门风险</div>
          <div class="stat-value warning">{{ riskData?.illegalBusiness || 4 }}</div>
        </div>
        <div class="stat-item">
          <div class="stat-label">法律诉讼</div>
          <div class="stat-value info">{{ riskData?.litigation || 3 }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LegalRiskPanel',
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
      const total = (this.riskData?.complianceRisk || 6) +
                   (this.riskData?.illegalBusiness || 4) +
                   (this.riskData?.litigation || 3)
      
      if (total >= 12) return '高风险'
      if (total >= 6) return '中风险'
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
.legal-risk-panel {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
    color: #303133;
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
    padding: 20px;

    .risk-stats {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;

      .stat-item {
        text-align: center;
        padding: 20px;
        background: #f5f7fa;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .stat-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 12px;
        }

        .stat-value {
          font-size: 32px;
          font-weight: bold;

          &.danger {
            color: #f56c6c;
          }

          &.warning {
            color: #e6a23c;
          }

          &.info {
            color: #409eff;
          }
        }
      }
    }
  }
}
</style>

