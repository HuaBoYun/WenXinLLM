<template>
  <div class="financial-indicators-chart">
    <h3 class="chart-title">财务指标分析</h3>
    
    <!-- 指标概览 -->
    <div class="indicators-overview">
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="indicator-group">
            <h4 class="group-title">盈利能力指标</h4>
            <div class="indicator-item">
              <span class="indicator-label">净利润率</span>
              <span class="indicator-value positive">{{ data.netProfitMargin }}%</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">总资产收益率</span>
              <span class="indicator-value positive">{{ data.roa }}%</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">净资产收益率</span>
              <span class="indicator-value positive">{{ data.roe }}%</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">毛利率</span>
              <span class="indicator-value positive">{{ data.grossProfitMargin }}%</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="indicator-group">
            <h4 class="group-title">偿债能力指标</h4>
            <div class="indicator-item">
              <span class="indicator-label">流动比率</span>
              <span class="indicator-value" :class="{ positive: data.currentRatio >= 2, warning: data.currentRatio < 2 }">
                {{ data.currentRatio }}
              </span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">速动比率</span>
              <span class="indicator-value" :class="{ positive: data.quickRatio >= 1, warning: data.quickRatio < 1 }">
                {{ data.quickRatio }}
              </span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">资产负债率</span>
              <span class="indicator-value" :class="{ positive: data.debtToEquityRatio <= 0.6, warning: data.debtToEquityRatio > 0.6 }">
                {{ data.debtToEquityRatio }}
              </span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">利息保障倍数</span>
              <span class="indicator-value positive">{{ data.interestCoverage }}</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="indicator-group">
            <h4 class="group-title">营运能力指标</h4>
            <div class="indicator-item">
              <span class="indicator-label">总资产周转率</span>
              <span class="indicator-value positive">{{ data.assetTurnover }}</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">应收账款周转率</span>
              <span class="indicator-value positive">{{ data.receivableTurnover }}</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">存货周转率</span>
              <span class="indicator-value positive">{{ data.inventoryTurnover }}</span>
            </div>
            <div class="indicator-item">
              <span class="indicator-label">营运资金周转率</span>
              <span class="indicator-value positive">{{ data.workingCapitalTurnover }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <el-row :gutter="24">
        <!-- 盈利能力趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">盈利能力趋势</h4>
            <div ref="profitabilityChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 偿债能力雷达图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">偿债能力分析</h4>
            <div ref="solvencyChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 营运能力对比图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">营运能力对比</h4>
            <div ref="operatingChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 综合指标仪表盘 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">综合财务健康度</h4>
            <div ref="healthGaugeChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 指标分析表格 -->
    <div class="indicators-analysis-table">
      <h4 class="table-title">财务指标详细分析</h4>
      <el-table :data="data.indicatorDetails" border stripe>
        <el-table-column prop="category" label="指标类别" width="120"></el-table-column>
        <el-table-column prop="indicator" label="指标名称" width="150"></el-table-column>
        <el-table-column prop="currentValue" label="当前值" width="100" align="right"></el-table-column>
        <el-table-column prop="previousValue" label="上期值" width="100" align="right"></el-table-column>
        <el-table-column prop="industryAverage" label="行业均值" width="100" align="right"></el-table-column>
        <el-table-column prop="benchmark" label="优秀标准" width="100" align="right"></el-table-column>
        <el-table-column prop="evaluation" label="评价" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getEvaluationTag(scope.row.evaluation)">
              {{ scope.row.evaluation }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="analysis" label="分析说明" min-width="200"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinancialIndicatorsChart',
  props: {
    data: {
      type: Object,
      default: () => ({
        netProfitMargin: 18.3,
        roa: 12.5,
        roe: 19.5,
        grossProfitMargin: 27.1,
        currentRatio: 2.35,
        quickRatio: 1.85,
        debtToEquityRatio: 0.56,
        interestCoverage: 8.5,
        assetTurnover: 1.2,
        receivableTurnover: 6.8,
        inventoryTurnover: 4.5,
        workingCapitalTurnover: 3.2,
        indicatorDetails: [
          {
            category: '盈利能力',
            indicator: '净利润率',
            currentValue: '18.3%',
            previousValue: '16.8%',
            industryAverage: '15.2%',
            benchmark: '20.0%',
            evaluation: '良好',
            analysis: '净利润率持续提升，盈利能力增强'
          },
          {
            category: '盈利能力',
            indicator: '总资产收益率',
            currentValue: '12.5%',
            previousValue: '11.2%',
            industryAverage: '10.8%',
            benchmark: '15.0%',
            evaluation: '良好',
            analysis: '资产使用效率较高，收益能力强'
          },
          {
            category: '偿债能力',
            indicator: '流动比率',
            currentValue: '2.35',
            previousValue: '2.18',
            industryAverage: '2.10',
            benchmark: '2.00',
            evaluation: '优秀',
            analysis: '短期偿债能力强，流动性充足'
          },
          {
            category: '偿债能力',
            indicator: '资产负债率',
            currentValue: '0.56',
            previousValue: '0.58',
            industryAverage: '0.62',
            benchmark: '0.50',
            evaluation: '良好',
            analysis: '负债水平合理，财务风险可控'
          },
          {
            category: '营运能力',
            indicator: '总资产周转率',
            currentValue: '1.2',
            previousValue: '1.1',
            industryAverage: '1.0',
            benchmark: '1.5',
            evaluation: '良好',
            analysis: '资产周转效率提升，营运能力增强'
          }
        ]
      })
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  methods: {
    initCharts() {
      this.initProfitabilityChart()
      this.initSolvencyChart()
      this.initOperatingChart()
      this.initHealthGaugeChart()
    },
    initProfitabilityChart() {
      const chart = echarts.init(this.$refs.profitabilityChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['净利润率', '总资产收益率', '净资产收益率', '毛利率']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}%'
          }
        },
        series: [
          {
            name: '净利润率',
            type: 'line',
            data: [16.8, 17.2, 17.8, 18.0, 18.1, 18.3],
            smooth: true,
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '总资产收益率',
            type: 'line',
            data: [11.2, 11.5, 11.8, 12.0, 12.2, 12.5],
            smooth: true,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '净资产收益率',
            type: 'line',
            data: [17.5, 18.0, 18.5, 19.0, 19.2, 19.5],
            smooth: true,
            itemStyle: { color: '#e6a23c' }
          },
          {
            name: '毛利率',
            type: 'line',
            data: [25.8, 26.2, 26.5, 26.8, 27.0, 27.1],
            smooth: true,
            itemStyle: { color: '#f56c6c' }
          }
        ]
      }
      chart.setOption(option)
    },
    initSolvencyChart() {
      const chart = echarts.init(this.$refs.solvencyChart)
      const option = {
        tooltip: {},
        radar: {
          indicator: [
            { name: '流动比率', max: 3 },
            { name: '速动比率', max: 2.5 },
            { name: '现金比率', max: 1.5 },
            { name: '利息保障倍数', max: 10 },
            { name: '债务保障率', max: 1 }
          ]
        },
        series: [{
          name: '偿债能力',
          type: 'radar',
          data: [
            {
              value: [2.35, 1.85, 0.85, 8.5, 0.44],
              name: '当前指标',
              itemStyle: { color: '#409eff' }
            },
            {
              value: [2.10, 1.60, 0.75, 7.2, 0.48],
              name: '行业平均',
              itemStyle: { color: '#67c23a' }
            }
          ]
        }]
      }
      chart.setOption(option)
    },
    initOperatingChart() {
      const chart = echarts.init(this.$refs.operatingChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['当前值', '行业均值', '优秀标准']
        },
        xAxis: {
          type: 'category',
          data: ['总资产周转率', '应收账款周转率', '存货周转率', '营运资金周转率']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '当前值',
            type: 'bar',
            data: [1.2, 6.8, 4.5, 3.2],
            itemStyle: { color: '#409eff' }
          },
          {
            name: '行业均值',
            type: 'bar',
            data: [1.0, 6.0, 4.0, 2.8],
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '优秀标准',
            type: 'bar',
            data: [1.5, 8.0, 6.0, 4.0],
            itemStyle: { color: '#e6a23c' }
          }
        ]
      }
      chart.setOption(option)
    },
    initHealthGaugeChart() {
      const chart = echarts.init(this.$refs.healthGaugeChart)
      const option = {
        series: [
          {
            name: '财务健康度',
            type: 'gauge',
            progress: {
              show: true
            },
            detail: {
              valueAnimation: true,
              formatter: '{value}分'
            },
            data: [
              {
                value: 85,
                name: '综合评分'
              }
            ],
            axisLine: {
              lineStyle: {
                width: 30,
                color: [
                  [0.3, '#fd666d'],
                  [0.7, '#37a2da'],
                  [1, '#67e0e3']
                ]
              }
            },
            pointer: {
              itemStyle: {
                color: 'auto'
              }
            },
            axisTick: {
              distance: -30,
              length: 8,
              lineStyle: {
                color: '#fff',
                width: 2
              }
            },
            splitLine: {
              distance: -30,
              length: 30,
              lineStyle: {
                color: '#fff',
                width: 4
              }
            },
            axisLabel: {
              color: 'auto',
              distance: 40,
              fontSize: 12
            },
            detail: {
              valueAnimation: true,
              formatter: '{value}分',
              color: 'auto',
              fontSize: 20
            }
          }
        ]
      }
      chart.setOption(option)
    },
    getEvaluationTag(evaluation) {
      const tags = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '较差': 'danger'
      }
      return tags[evaluation] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-indicators-chart {
  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 24px 0;
    padding-bottom: 12px;
    border-bottom: 2px solid #e6a23c;
  }
}

.indicators-overview {
  margin-bottom: 32px;

  .indicator-group {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .group-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 16px 0;
      padding-bottom: 8px;
      border-bottom: 1px solid #ebeef5;
    }

    .indicator-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      font-size: 14px;

      .indicator-label {
        color: #606266;
      }

      .indicator-value {
        font-weight: 600;

        &.positive {
          color: #67c23a;
        }

        &.warning {
          color: #e6a23c;
        }

        &.negative {
          color: #f56c6c;
        }
      }
    }
  }
}

.charts-container {
  margin-bottom: 32px;

  .chart-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .chart-card-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 16px 0;
    }

    .chart {
      width: 100%;
    }
  }
}

.indicators-analysis-table {
  .table-title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
  }

  .el-table {
    border-radius: 4px;
    overflow: hidden;
  }
}
</style>
