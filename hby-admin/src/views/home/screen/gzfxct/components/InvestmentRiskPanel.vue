<template>
  <div class="investment-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-coin"></i>
      <h4>投资风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <!-- 风险指标卡片 -->
      <div class="risk-indicators">
        <div class="indicator-card">
          <div class="indicator-icon" style="background: rgba(245, 108, 108, 0.1);">
            <i class="el-icon-warning" style="color: #f56c6c;"></i>
          </div>
          <div class="indicator-info">
            <div class="indicator-label">对外投资预测风险</div>
            <div class="indicator-value">{{ riskData?.externalInvestmentRisk || 0 }}</div>
            <div class="indicator-desc">项目</div>
          </div>
        </div>

        <div class="indicator-card">
          <div class="indicator-icon" style="background: rgba(230, 162, 60, 0.1);">
            <i class="el-icon-money" style="color: #e6a23c;"></i>
          </div>
          <div class="indicator-info">
            <div class="indicator-label">无关多元化风险</div>
            <div class="indicator-value">{{ riskData?.diversificationRisk || 0 }}</div>
            <div class="indicator-desc">项目</div>
          </div>
        </div>

        <div class="indicator-card">
          <div class="indicator-icon" style="background: rgba(64, 158, 255, 0.1);">
            <i class="el-icon-location-outline" style="color: #409eff;"></i>
          </div>
          <div class="indicator-info">
            <div class="indicator-label">境外投资风险</div>
            <div class="indicator-value">{{ riskData?.overseasRisk || 0 }}</div>
            <div class="indicator-desc">项目</div>
          </div>
        </div>

        <div class="indicator-card">
          <div class="indicator-icon" style="background: rgba(103, 194, 58, 0.1);">
            <i class="el-icon-box" style="color: #67c23a;"></i>
          </div>
          <div class="indicator-info">
            <div class="indicator-label">资产闲置风险</div>
            <div class="indicator-value">{{ riskData?.assetIdleRisk || 0 }}</div>
            <div class="indicator-desc">项目</div>
          </div>
        </div>
      </div>

      <!-- 风险详情列表 -->
      <div class="risk-details">
        <div class="details-header">
          <span>风险详情</span>
          <el-button type="text" size="small" @click="handleViewMore">查看更多 <i class="el-icon-arrow-right"></i></el-button>
        </div>
        <el-table
          :data="riskData?.details || mockDetails"
          size="small"
          :max-height="200"
          stripe
        >
          <el-table-column prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="riskType" label="风险类型" width="120" />
          <el-table-column prop="riskLevel" label="风险等级" width="100">
            <template slot-scope="scope">
              <el-tag :type="getLevelType(scope.row.riskLevel)" size="mini">
                {{ scope.row.riskLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="涉及金额(万元)" width="120" align="right">
            <template slot-scope="scope">
              <span style="color: #f56c6c; font-weight: bold;">{{ scope.row.amount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="mini">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InvestmentRiskPanel',
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
  data() {
    return {
      mockDetails: [
        {
          projectName: '某新能源项目投资',
          riskType: '对外投资',
          riskLevel: '高',
          amount: 5000,
          status: '待处理'
        },
        {
          projectName: '某房地产项目',
          riskType: '无关多元',
          riskLevel: '中',
          amount: 3200,
          status: '处理中'
        },
        {
          projectName: '某境外矿产投资',
          riskType: '境外投资',
          riskLevel: '严重',
          amount: 8000,
          status: '待处理'
        },
        {
          projectName: '某闲置厂房',
          riskType: '资产闲置',
          riskLevel: '中',
          amount: 1500,
          status: '已处理'
        }
      ]
    }
  },
  methods: {
    getRiskLevel() {
      const total = (this.riskData?.externalInvestmentRisk || 0) +
                   (this.riskData?.diversificationRisk || 0) +
                   (this.riskData?.overseasRisk || 0) +
                   (this.riskData?.assetIdleRisk || 0)
      
      if (total >= 15) return '高风险'
      if (total >= 8) return '中风险'
      return '低风险'
    },
    getRiskLevelType() {
      const level = this.getRiskLevel()
      if (level === '高风险') return 'danger'
      if (level === '中风险') return 'warning'
      return 'success'
    },
    getLevelType(level) {
      const map = {
        '严重': 'danger',
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return map[level] || 'info'
    },
    getStatusType(status) {
      const map = {
        '待处理': 'danger',
        '处理中': 'warning',
        '已处理': 'success'
      }
      return map[status] || 'info'
    },
    handleViewMore() {
      this.$emit('view-more', 'investment')
    }
  }
}
</script>

<style lang="scss" scoped>
.investment-risk-panel {
  height: 100%;
  display: flex;
  flex-direction: column;

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
    flex: 1;
    padding: 20px;
    overflow-y: auto;

    .risk-indicators {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;
      margin-bottom: 20px;

      .indicator-card {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 15px;
        background: #f5f7fa;
        border-radius: 6px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .indicator-icon {
          width: 50px;
          height: 50px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            font-size: 24px;
          }
        }

        .indicator-info {
          flex: 1;

          .indicator-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .indicator-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
          }

          .indicator-desc {
            font-size: 12px;
            color: #c0c4cc;
            margin-top: 2px;
          }
        }
      }
    }

    .risk-details {
      .details-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;

        span {
          font-size: 14px;
          font-weight: 600;
          color: #606266;
        }
      }
    }
  }
}
</style>

