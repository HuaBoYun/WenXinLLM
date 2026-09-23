<template>
  <div class="financial-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-s-finance"></i>
      <h4>财务风险监控</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <div class="risk-cards">
        <div class="risk-card">
          <div class="card-icon" style="background: rgba(245, 108, 108, 0.1); color: #f56c6c;">
            <i class="el-icon-warning"></i>
          </div>
          <div class="card-info">
            <div class="card-label">过度负债</div>
            <div class="card-value">{{ riskData?.debtRisk || 6 }}</div>
            <div class="card-rate">资产负债率: {{ riskData?.debtRatio || '78%' }}</div>
          </div>
        </div>

        <div class="risk-card">
          <div class="card-icon" style="background: rgba(230, 162, 60, 0.1); color: #e6a23c;">
            <i class="el-icon-coin"></i>
          </div>
          <div class="card-info">
            <div class="card-label">财务金融风险</div>
            <div class="card-value">{{ riskData?.financialRisk || 4 }}</div>
            <div class="card-rate">融资成本: {{ riskData?.financingCost || '6.5%' }}</div>
          </div>
        </div>

        <div class="risk-card">
          <div class="card-icon" style="background: rgba(64, 158, 255, 0.1); color: #409eff;">
            <i class="el-icon-money"></i>
          </div>
          <div class="card-info">
            <div class="card-label">应付账款敞口</div>
            <div class="card-value">{{ riskData?.payableRisk || 8 }}</div>
            <div class="card-rate">逾期金额: {{ riskData?.overdueAmount || '2.3亿' }}</div>
          </div>
        </div>

        <div class="risk-card">
          <div class="card-icon" style="background: rgba(103, 194, 58, 0.1); color: #67c23a;">
            <i class="el-icon-document"></i>
          </div>
          <div class="card-info">
            <div class="card-label">应收账款风险</div>
            <div class="card-value">{{ riskData?.receivableRisk || 5 }}</div>
            <div class="card-rate">账龄超期: {{ riskData?.ageingDays || '180天' }}</div>
          </div>
        </div>
      </div>

      <div class="risk-trend" ref="trendChart"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinancialRiskPanel',
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
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    getRiskLevel() {
      const total = (this.riskData?.debtRisk || 6) +
                   (this.riskData?.financialRisk || 4) +
                   (this.riskData?.payableRisk || 8) +
                   (this.riskData?.receivableRisk || 5)
      
      if (total >= 20) return '高风险'
      if (total >= 10) return '中风险'
      return '低风险'
    },
    getRiskLevelType() {
      const level = this.getRiskLevel()
      if (level === '高风险') return 'danger'
      if (level === '中风险') return 'warning'
      return 'success'
    },
    initChart() {
      if (!this.$refs.trendChart) return
      
      if (this.chart) {
        this.chart.dispose()
      }
      
      this.chart = echarts.init(this.$refs.trendChart)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['过度负债', '财务金融', '应付账款', '应收账款'],
          textStyle: {
            color: '#606266',
            fontSize: 12
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月'],
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 11
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          axisLabel: {
            color: '#606266',
            fontSize: 11
          },
          splitLine: {
            lineStyle: {
              color: '#ebeef5'
            }
          }
        },
        series: [
          {
            name: '过度负债',
            type: 'line',
            data: [5, 6, 5, 7, 6, 6],
            smooth: true,
            itemStyle: {
              color: '#f56c6c'
            }
          },
          {
            name: '财务金融',
            type: 'line',
            data: [3, 4, 4, 5, 4, 4],
            smooth: true,
            itemStyle: {
              color: '#e6a23c'
            }
          },
          {
            name: '应付账款',
            type: 'line',
            data: [7, 8, 9, 8, 9, 8],
            smooth: true,
            itemStyle: {
              color: '#409eff'
            }
          },
          {
            name: '应收账款',
            type: 'line',
            data: [4, 5, 5, 6, 5, 5],
            smooth: true,
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }
      
      this.chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-risk-panel {
  height: 100%;
  display: flex;
  flex-direction: column;

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

    .risk-cards {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
      margin-bottom: 20px;

      .risk-card {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 6px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .card-icon {
          width: 45px;
          height: 45px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            font-size: 22px;
          }
        }

        .card-info {
          flex: 1;

          .card-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 22px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
          }

          .card-rate {
            font-size: 11px;
            color: #c0c4cc;
            margin-top: 4px;
          }
        }
      }
    }

    .risk-trend {
      height: 180px;
    }
  }
}
</style>

