<template>
  <div class="procurement-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-shopping-cart-2"></i>
      <h4>采购风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-list">
        <div class="list-item">
          <div class="item-left">
            <i class="el-icon-warning" style="color: #f56c6c;"></i>
            <span>招标采购风险</span>
          </div>
          <div class="item-right">
            <span class="value">{{ riskData?.biddingRisk || 7 }}</span>
          </div>
        </div>
        <div class="list-item">
          <div class="item-left">
            <i class="el-icon-warning-outline" style="color: #e6a23c;"></i>
            <span>违规招投标</span>
          </div>
          <div class="item-right">
            <span class="value">{{ riskData?.irregularBidding || 5 }}</span>
          </div>
        </div>
        <div class="list-item">
          <div class="item-left">
            <i class="el-icon-info" style="color: #409eff;"></i>
            <span>靠企吃企</span>
          </div>
          <div class="item-right">
            <span class="value">{{ riskData?.relatedParty || 4 }}</span>
          </div>
        </div>
        <div class="list-item">
          <div class="item-left">
            <i class="el-icon-money" style="color: #67c23a;"></i>
            <span>超合同支付</span>
          </div>
          <div class="item-right">
            <span class="value">{{ riskData?.overPayment || 3 }}</span>
          </div>
        </div>
        <div class="list-item">
          <div class="item-left">
            <i class="el-icon-shopping-bag-1" style="color: #909399;"></i>
            <span>违规公款消费</span>
          </div>
          <div class="item-right">
            <span class="value">{{ riskData?.publicConsumption || 2 }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProcurementRiskPanel',
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
      const total = (this.riskData?.biddingRisk || 7) +
                   (this.riskData?.irregularBidding || 5) +
                   (this.riskData?.relatedParty || 4) +
                   (this.riskData?.overPayment || 3) +
                   (this.riskData?.publicConsumption || 2)
      
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
.procurement-risk-panel {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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

    .risk-list {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .list-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px;
        background: #f5f7fa;
        border-radius: 6px;
        border-left: 4px solid #409eff;
        transition: all 0.3s ease;

        &:hover {
          transform: translateX(5px);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .item-left {
          display: flex;
          align-items: center;
          gap: 10px;

          i {
            font-size: 20px;
          }

          span {
            font-size: 14px;
            color: #606266;
            font-weight: 500;
          }
        }

        .item-right {
          .value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
          }
        }
      }
    }
  }
}
</style>

