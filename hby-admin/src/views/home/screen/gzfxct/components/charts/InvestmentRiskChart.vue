<template>
  <div class="investment-risk-chart">
    <div class="chart-container" ref="chart"></div>
    <div class="risk-summary">
      <div class="summary-item">
        <span class="label">对外投资:</span>
        <span class="value">{{ data?.externalInvestmentRisk || 0 }}</span>
      </div>
      <div class="summary-item">
        <span class="label">无关多元:</span>
        <span class="value">{{ data?.diversificationRisk || 0 }}</span>
      </div>
      <div class="summary-item">
        <span class="label">境外投资:</span>
        <span class="value">{{ data?.overseasRisk || 0 }}</span>
      </div>
      <div class="summary-item">
        <span class="label">资产闲置:</span>
        <span class="value">{{ data?.assetIdleRisk || 0 }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'InvestmentRiskChart',
  props: {
    data: {
      type: Object,
      default: null
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
  watch: {
    data: {
      deep: true,
      handler() {
        this.initChart()
      }
    }
  },
  methods: {
    initChart() {
      if (!this.$refs.chart) return
      
      if (this.chart) {
        this.chart.dispose()
      }
      
      this.chart = echarts.init(this.$refs.chart)
      
      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#409eff',
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            data: [
              { 
                value: this.data?.externalInvestmentRisk || 5, 
                name: '对外投资',
                itemStyle: { color: '#f56c6c' }
              },
              { 
                value: this.data?.diversificationRisk || 3, 
                name: '无关多元',
                itemStyle: { color: '#e6a23c' }
              },
              { 
                value: this.data?.overseasRisk || 2, 
                name: '境外投资',
                itemStyle: { color: '#409eff' }
              },
              { 
                value: this.data?.assetIdleRisk || 4, 
                name: '资产闲置',
                itemStyle: { color: '#67c23a' }
              }
            ],
            label: {
              show: true,
              color: '#fff',
              fontSize: 12
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              }
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
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
.investment-risk-chart {
  height: 100%;
  display: flex;
  flex-direction: column;
  min-height: 0;

  .chart-container {
    flex: 1;
    min-height: 0;
    width: 100%;
  }

  .risk-summary {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 6px;
    margin-top: 8px;
    flex-shrink: 0;

    .summary-item {
      display: flex;
      justify-content: space-between;
      padding: 4px 8px;
      background: rgba(64, 158, 255, 0.1);
      border-radius: 4px;
      font-size: 11px;

      .label {
        color: #909399;
      }

      .value {
        color: #409eff;
        font-weight: bold;
      }
    }
  }
}
</style>

