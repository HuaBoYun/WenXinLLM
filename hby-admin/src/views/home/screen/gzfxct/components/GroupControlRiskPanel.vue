<template>
  <div class="group-control-risk-panel" v-loading="loading">
    <div class="panel-header">
      <i class="el-icon-office-building"></i>
      <h4>集团管控风险</h4>
      <el-tag :type="getRiskLevelType()" size="small">{{ getRiskLevel() }}</el-tag>
    </div>

    <div class="panel-content">
      <!-- 风险指标 -->
      <div class="risk-indicators">
        <div class="indicator-item">
          <div class="indicator-label">控股不控权</div>
          <div class="indicator-value danger">{{ riskData?.controlRisk || 3 }}</div>
        </div>
        <div class="indicator-item">
          <div class="indicator-label">多层架构</div>
          <div class="indicator-value warning">{{ riskData?.hierarchyRisk || 5 }}</div>
        </div>
        <div class="indicator-item">
          <div class="indicator-label">超股比担保</div>
          <div class="indicator-value danger">{{ riskData?.guaranteeRisk || 4 }}</div>
        </div>
        <div class="indicator-item">
          <div class="indicator-label">违规挂靠</div>
          <div class="indicator-value warning">{{ riskData?.affiliationRisk || 2 }}</div>
        </div>
      </div>

      <!-- 风险图表 -->
      <div class="risk-chart" ref="riskChart"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'GroupControlRiskPanel',
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
      const total = (this.riskData?.controlRisk || 3) +
                   (this.riskData?.hierarchyRisk || 5) +
                   (this.riskData?.guaranteeRisk || 4) +
                   (this.riskData?.affiliationRisk || 2)
      
      if (total >= 12) return '高风险'
      if (total >= 6) return '中风险'
      return '低风险'
    },
    getRiskLevelType() {
      const level = this.getRiskLevel()
      if (level === '高风险') return 'danger'
      if (level === '中风险') return 'warning'
      return 'success'
    },
    initChart() {
      if (!this.$refs.riskChart) return
      
      if (this.chart) {
        this.chart.dispose()
      }
      
      this.chart = echarts.init(this.$refs.riskChart)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        radar: {
          indicator: [
            { name: '控股不控权', max: 10 },
            { name: '多层架构', max: 10 },
            { name: '超股比担保', max: 10 },
            { name: '违规挂靠', max: 10 }
          ],
          radius: '60%',
          splitNumber: 4,
          axisName: {
            color: '#606266'
          },
          splitLine: {
            lineStyle: {
              color: '#dcdfe6'
            }
          },
          splitArea: {
            areaStyle: {
              color: ['rgba(64, 158, 255, 0.05)', 'rgba(64, 158, 255, 0.1)']
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [
                  this.riskData?.controlRisk || 3,
                  this.riskData?.hierarchyRisk || 5,
                  this.riskData?.guaranteeRisk || 4,
                  this.riskData?.affiliationRisk || 2
                ],
                name: '风险值',
                areaStyle: {
                  color: 'rgba(245, 108, 108, 0.3)'
                },
                lineStyle: {
                  color: '#f56c6c',
                  width: 2
                },
                itemStyle: {
                  color: '#f56c6c'
                }
              }
            ]
          }
        ]
      }
      
      this.chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.group-control-risk-panel {
  height: 100%;
  display: flex;
  flex-direction: column;

  .panel-header {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 20px;
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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

    .risk-indicators {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
      margin-bottom: 20px;

      .indicator-item {
        padding: 12px;
        background: #f5f7fa;
        border-radius: 6px;
        text-align: center;

        .indicator-label {
          font-size: 12px;
          color: #909399;
          margin-bottom: 8px;
        }

        .indicator-value {
          font-size: 28px;
          font-weight: bold;

          &.danger {
            color: #f56c6c;
          }

          &.warning {
            color: #e6a23c;
          }

          &.success {
            color: #67c23a;
          }
        }
      }
    }

    .risk-chart {
      height: 200px;
    }
  }
}
</style>

