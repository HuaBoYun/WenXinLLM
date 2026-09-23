<template>
  <div class="revenue-analysis-chart">
    <h3 class="chart-title">收入分析</h3>
    
    <!-- 收入概览 -->
    <div class="revenue-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon total-revenue">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.totalRevenue) }}</div>
              <div class="card-label">总收入</div>
              <div class="card-change" :class="{ positive: data.totalRevenueChange > 0, negative: data.totalRevenueChange < 0 }">
                {{ data.totalRevenueChange > 0 ? '+' : '' }}{{ data.totalRevenueChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon product-revenue">
              <i class="el-icon-goods"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.productRevenue) }}</div>
              <div class="card-label">产品收入</div>
              <div class="card-change" :class="{ positive: data.productRevenueChange > 0, negative: data.productRevenueChange < 0 }">
                {{ data.productRevenueChange > 0 ? '+' : '' }}{{ data.productRevenueChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon service-revenue">
              <i class="el-icon-service"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(data.serviceRevenue) }}</div>
              <div class="card-label">服务收入</div>
              <div class="card-change" :class="{ positive: data.serviceRevenueChange > 0, negative: data.serviceRevenueChange < 0 }">
                {{ data.serviceRevenueChange > 0 ? '+' : '' }}{{ data.serviceRevenueChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon growth-rate">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.growthRate }}%</div>
              <div class="card-label">增长率</div>
              <div class="card-change" :class="{ positive: data.growthRateChange > 0, negative: data.growthRateChange < 0 }">
                {{ data.growthRateChange > 0 ? '+' : '' }}{{ data.growthRateChange }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <el-row :gutter="24">
        <!-- 收入趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">收入趋势分析</h4>
            <div ref="revenueTrendChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 收入结构图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">收入结构分析</h4>
            <div ref="revenueStructureChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 收入对比图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">收入同期对比</h4>
            <div ref="revenueComparisonChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 收入预测图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">收入预测分析</h4>
            <div ref="revenueForecastChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 收入分析表格 -->
    <div class="revenue-analysis-table">
      <h4 class="table-title">详细收入分析</h4>
      <el-table :data="data.revenueDetails" border stripe>
        <el-table-column prop="category" label="收入类别" width="150"></el-table-column>
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
        <el-table-column prop="varianceRate" label="增长率" width="100" align="right">
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
  name: 'RevenueAnalysisChart',
  props: {
    data: {
      type: Object,
      default: () => ({
        totalRevenue: 85680000,
        totalRevenueChange: 12.5,
        productRevenue: 68540000,
        productRevenueChange: 15.2,
        serviceRevenue: 17140000,
        serviceRevenueChange: 3.8,
        growthRate: 12.5,
        growthRateChange: 2.3,
        revenueDetails: [
          {
            category: '云计算服务',
            currentAmount: 35600000,
            previousAmount: 30200000,
            variance: 5400000,
            varianceRate: 17.9,
            proportion: 41.5,
            remark: '云服务需求增长强劲'
          },
          {
            category: '大数据分析',
            currentAmount: 22400000,
            previousAmount: 20800000,
            variance: 1600000,
            varianceRate: 7.7,
            proportion: 26.1,
            remark: '数据分析业务稳步增长'
          },
          {
            category: '人工智能',
            currentAmount: 18200000,
            previousAmount: 15600000,
            variance: 2600000,
            varianceRate: 16.7,
            proportion: 21.2,
            remark: 'AI技术应用快速发展'
          },
          {
            category: '技术咨询',
            currentAmount: 9480000,
            previousAmount: 9850000,
            variance: -370000,
            varianceRate: -3.8,
            proportion: 11.1,
            remark: '咨询业务略有下降'
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
      this.initRevenueTrendChart()
      this.initRevenueStructureChart()
      this.initRevenueComparisonChart()
      this.initRevenueForecastChart()
    },
    initRevenueTrendChart() {
      const chart = echarts.init(this.$refs.revenueTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总收入', '产品收入', '服务收入']
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
            name: '总收入',
            type: 'line',
            data: [7800, 8200, 8500, 8300, 8600, 8568],
            smooth: true,
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '产品收入',
            type: 'line',
            data: [6200, 6500, 6800, 6600, 6900, 6854],
            smooth: true,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '服务收入',
            type: 'line',
            data: [1600, 1700, 1700, 1700, 1700, 1714],
            smooth: true,
            itemStyle: { color: '#e6a23c' }
          }
        ]
      }
      chart.setOption(option)
    },
    initRevenueStructureChart() {
      const chart = echarts.init(this.$refs.revenueStructureChart)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: ['云计算服务', '大数据分析', '人工智能', '技术咨询']
        },
        series: [
          {
            name: '收入结构',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
            data: [
              { value: 3560, name: '云计算服务', itemStyle: { color: '#67c23a' } },
              { value: 2240, name: '大数据分析', itemStyle: { color: '#409eff' } },
              { value: 1820, name: '人工智能', itemStyle: { color: '#e6a23c' } },
              { value: 948, name: '技术咨询', itemStyle: { color: '#f56c6c' } }
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
    initRevenueComparisonChart() {
      const chart = echarts.init(this.$refs.revenueComparisonChart)
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
          data: ['云计算', '大数据', '人工智能', '技术咨询']
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
            data: [3560, 2240, 1820, 948],
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '上期',
            type: 'bar',
            data: [3020, 2080, 1560, 985],
            itemStyle: { color: '#409eff' }
          }
        ]
      }
      chart.setOption(option)
    },
    initRevenueForecastChart() {
      const chart = echarts.init(this.$refs.revenueForecastChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['实际收入', '预测收入', '目标收入']
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '实际收入',
            type: 'line',
            data: [7800, 8200, 8500, 8300, 8600, 8568, null, null, null],
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '预测收入',
            type: 'line',
            data: [null, null, null, null, null, 8568, 8800, 9000, 9200],
            itemStyle: { color: '#409eff' },
            lineStyle: { type: 'dashed' }
          },
          {
            name: '目标收入',
            type: 'line',
            data: [8000, 8300, 8600, 8500, 8800, 8700, 9000, 9200, 9500],
            itemStyle: { color: '#e6a23c' },
            lineStyle: { type: 'dotted' }
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
.revenue-analysis-chart {
  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 24px 0;
    padding-bottom: 12px;
    border-bottom: 2px solid #67c23a;
  }
}

.revenue-overview {
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

      &.total-revenue {
        background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
      }

      &.product-revenue {
        background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
      }

      &.service-revenue {
        background: linear-gradient(135deg, #e6a23c 0%, #d48806 100%);
      }

      &.growth-rate {
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
          color: #67c23a;
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

.revenue-analysis-table {
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
      color: #67c23a;
    }

    .negative {
      color: #f56c6c;
    }
  }
}
</style>
