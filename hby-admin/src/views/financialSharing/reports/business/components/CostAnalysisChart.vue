<template>
  <div class="cost-analysis-chart">
    <h3 class="chart-title">成本分析</h3>
    
    <!-- 成本概览 -->
    <div class="cost-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon total-cost">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.totalCost) }}</div>
              <div class="card-label">总成本</div>
              <div class="card-change" :class="{ positive: data.totalCostChange > 0, negative: data.totalCostChange < 0 }">
                {{ data.totalCostChange > 0 ? '+' : '' }}{{ data.totalCostChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon direct-cost">
              <i class="el-icon-s-goods"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.directCost) }}</div>
              <div class="card-label">直接成本</div>
              <div class="card-change" :class="{ positive: data.directCostChange > 0, negative: data.directCostChange < 0 }">
                {{ data.directCostChange > 0 ? '+' : '' }}{{ data.directCostChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon indirect-cost">
              <i class="el-icon-s-operation"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.indirectCost) }}</div>
              <div class="card-label">间接成本</div>
              <div class="card-change" :class="{ positive: data.indirectCostChange > 0, negative: data.indirectCostChange < 0 }">
                {{ data.indirectCostChange > 0 ? '+' : '' }}{{ data.indirectCostChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon cost-ratio">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.costRatio }}%</div>
              <div class="card-label">成本率</div>
              <div class="card-change" :class="{ positive: data.costRatioChange > 0, negative: data.costRatioChange < 0 }">
                {{ data.costRatioChange > 0 ? '+' : '' }}{{ data.costRatioChange }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <el-row :gutter="24">
        <!-- 成本趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">成本趋势分析</h4>
            <div ref="costTrendChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 成本结构图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">成本结构分析</h4>
            <div ref="costStructureChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 成本对比图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">成本同期对比</h4>
            <div ref="costComparisonChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 成本控制效果图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">成本控制效果</h4>
            <div ref="costControlChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 成本分析表格 -->
    <div class="cost-analysis-table">
      <h4 class="table-title">详细成本分析</h4>
      <el-table :data="data.costDetails" border stripe>
        <el-table-column prop="category" label="成本类别" width="150"></el-table-column>
        <el-table-column prop="currentAmount" label="本期金额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.currentAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="previousAmount" label="上期金额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.previousAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="variance" label="差异金额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="{ positive: scope.row.variance > 0, negative: scope.row.variance < 0 }">
              {{ scope.row.variance > 0 ? '+' : '' }}{{ formatAmount(scope.row.variance) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceRate" label="差异率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="{ positive: scope.row.varianceRate > 0, negative: scope.row.varianceRate < 0 }">
              {{ scope.row.varianceRate > 0 ? '+' : '' }}{{ scope.row.varianceRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="proportion" label="占比" width="100" align="right">
          <template slot-scope="scope">
            {{ scope.row.proportion }}%
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'CostAnalysisChart',
  props: {
    data: {
      type: Object,
      default: () => ({
        totalCost: 62450000,
        totalCostChange: -2.3,
        directCost: 45230000,
        directCostChange: -1.8,
        indirectCost: 17220000,
        indirectCostChange: -3.5,
        costRatio: 72.9,
        costRatioChange: -0.8,
        costDetails: [
          {
            category: '原材料成本',
            currentAmount: 28500000,
            previousAmount: 29200000,
            variance: -700000,
            varianceRate: -2.4,
            proportion: 45.6,
            remark: '原材料价格下降'
          },
          {
            category: '人工成本',
            currentAmount: 16730000,
            previousAmount: 16100000,
            variance: 630000,
            varianceRate: 3.9,
            proportion: 26.8,
            remark: '人员增加及薪资调整'
          },
          {
            category: '制造费用',
            currentAmount: 12450000,
            previousAmount: 12800000,
            variance: -350000,
            varianceRate: -2.7,
            proportion: 19.9,
            remark: '设备效率提升'
          },
          {
            category: '管理费用',
            currentAmount: 4770000,
            previousAmount: 4650000,
            variance: 120000,
            varianceRate: 2.6,
            proportion: 7.6,
            remark: '管理成本略有上升'
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
      this.initCostTrendChart()
      this.initCostStructureChart()
      this.initCostComparisonChart()
      this.initCostControlChart()
    },
    initCostTrendChart() {
      const chart = echarts.init(this.$refs.costTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总成本', '直接成本', '间接成本']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '总成本',
            type: 'line',
            data: [6800, 6500, 6200, 6400, 6300, 6245],
            smooth: true,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '直接成本',
            type: 'line',
            data: [4900, 4700, 4500, 4600, 4550, 4523],
            smooth: true,
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '间接成本',
            type: 'line',
            data: [1900, 1800, 1700, 1800, 1750, 1722],
            smooth: true,
            itemStyle: { color: '#e6a23c' }
          }
        ]
      }
      chart.setOption(option)
    },
    initCostStructureChart() {
      const chart = echarts.init(this.$refs.costStructureChart)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: ['原材料成本', '人工成本', '制造费用', '管理费用']
        },
        series: [
          {
            name: '成本结构',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
            data: [
              { value: 2850, name: '原材料成本', itemStyle: { color: '#409eff' } },
              { value: 1673, name: '人工成本', itemStyle: { color: '#67c23a' } },
              { value: 1245, name: '制造费用', itemStyle: { color: '#e6a23c' } },
              { value: 477, name: '管理费用', itemStyle: { color: '#f56c6c' } }
            ],
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
      chart.setOption(option)
    },
    initCostComparisonChart() {
      const chart = echarts.init(this.$refs.costComparisonChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['本期', '上期']
        },
        xAxis: {
          type: 'category',
          data: ['原材料', '人工', '制造费用', '管理费用']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '本期',
            type: 'bar',
            data: [2850, 1673, 1245, 477],
            itemStyle: { color: '#409eff' }
          },
          {
            name: '上期',
            type: 'bar',
            data: [2920, 1610, 1280, 465],
            itemStyle: { color: '#67c23a' }
          }
        ]
      }
      chart.setOption(option)
    },
    initCostControlChart() {
      const chart = echarts.init(this.$refs.costControlChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预算成本', '实际成本', '节约金额']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: [
          {
            type: 'value',
            name: '成本(万元)',
            axisLabel: {
              formatter: '{value}万'
            }
          },
          {
            type: 'value',
            name: '节约金额(万元)',
            axisLabel: {
              formatter: '{value}万'
            }
          }
        ],
        series: [
          {
            name: '预算成本',
            type: 'bar',
            data: [7000, 6800, 6500, 6600, 6500, 6400],
            itemStyle: { color: '#e6a23c' }
          },
          {
            name: '实际成本',
            type: 'bar',
            data: [6800, 6500, 6200, 6400, 6300, 6245],
            itemStyle: { color: '#409eff' }
          },
          {
            name: '节约金额',
            type: 'line',
            yAxisIndex: 1,
            data: [200, 300, 300, 200, 200, 155],
            itemStyle: { color: '#67c23a' }
          }
        ]
      }
      chart.setOption(option)
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(1) + '万'
      }
      return amount.toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-analysis-chart {
  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 24px 0;
    padding-bottom: 12px;
    border-bottom: 2px solid #409eff;
  }
}

.cost-overview {
  margin-bottom: 32px;

  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    }

    .card-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }

      &.total-cost {
        background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
      }

      &.direct-cost {
        background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
      }

      &.indirect-cost {
        background: linear-gradient(135deg, #e6a23c 0%, #d48806 100%);
      }

      &.cost-ratio {
        background: linear-gradient(135deg, #f56c6c 0%, #f04864 100%);
      }
    }

    .card-content {
      .card-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 12px;
        color: #909399;
        margin-bottom: 4px;
      }

      .card-change {
        font-size: 12px;
        font-weight: 600;

        &.positive {
          color: #f56c6c;
        }

        &.negative {
          color: #67c23a;
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

.cost-analysis-table {
  .table-title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
  }

  .el-table {
    border-radius: 4px;
    overflow: hidden;

    .positive {
      color: #f56c6c;
    }

    .negative {
      color: #67c23a;
    }
  }
}
</style>
