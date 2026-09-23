<template>
  <div class="business-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-s-shop"></i>
      <h4>经营风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-grid">
        <div class="grid-item">
          <div class="item-icon" style="background: rgba(245, 108, 108, 0.1); color: #f56c6c;">
            <i class="el-icon-warning"></i>
          </div>
          <div class="item-label">经营潜亏风险</div>
          <div class="item-value">{{ riskData?.operatingLoss || 8 }}</div>
        </div>
        <div class="grid-item">
          <div class="item-icon" style="background: rgba(230, 162, 60, 0.1); color: #e6a23c;">
            <i class="el-icon-document"></i>
          </div>
          <div class="item-label">合同经营风险</div>
          <div class="item-value">{{ riskData?.contractRisk || 6 }}</div>
        </div>
        <div class="grid-item">
          <div class="item-icon" style="background: rgba(64, 158, 255, 0.1); color: #409eff;">
            <i class="el-icon-money"></i>
          </div>
          <div class="item-label">资金头寸预警</div>
          <div class="item-value">{{ riskData?.cashPosition || 5 }}</div>
        </div>
        <div class="grid-item">
          <div class="item-icon" style="background: rgba(103, 194, 58, 0.1); color: #67c23a;">
            <i class="el-icon-user"></i>
          </div>
          <div class="item-label">薪酬乱象</div>
          <div class="item-value">{{ riskData?.salaryIssues || 3 }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BusinessRiskPanel',
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
      const total = (this.riskData?.operatingLoss || 8) +
                   (this.riskData?.contractRisk || 6) +
                   (this.riskData?.cashPosition || 5) +
                   (this.riskData?.salaryIssues || 3)
      
      if (total >= 18) return '高风险'
      if (total >= 10) return '中风险'
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
.business-risk-panel {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
    color: white;
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

    .risk-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 15px;

      .grid-item {
        text-align: center;
        padding: 20px;
        background: #f5f7fa;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .item-icon {
          width: 60px;
          height: 60px;
          margin: 0 auto 12px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            font-size: 28px;
          }
        }

        .item-label {
          font-size: 14px;
          color: #606266;
          margin-bottom: 8px;
        }

        .item-value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
        }
      }
    }
  }
}

@media (max-width: 1600px) {
  .business-risk-panel {
    .panel-content {
      .risk-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }
  }
}
</style>

