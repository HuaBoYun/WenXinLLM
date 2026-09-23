<template>
  <div class="operational-analysis-chart">
    <h3 class="chart-title">经营分析</h3>
    
    <!-- 经营概览 -->
    <div class="operational-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon market-share">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.marketShare }}%</div>
              <div class="card-label">市场份额</div>
              <div class="card-change" :class="{ positive: data.marketShareChange > 0, negative: data.marketShareChange < 0 }">
                {{ data.marketShareChange > 0 ? '+' : '' }}{{ data.marketShareChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon customer-satisfaction">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.customerSatisfaction }}%</div>
              <div class="card-label">客户满意度</div>
              <div class="card-change" :class="{ positive: data.customerSatisfactionChange > 0, negative: data.customerSatisfactionChange < 0 }">
                {{ data.customerSatisfactionChange > 0 ? '+' : '' }}{{ data.customerSatisfactionChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon operational-efficiency">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.operationalEfficiency }}%</div>
              <div class="card-label">运营效率</div>
              <div class="card-change" :class="{ positive: data.operationalEfficiencyChange > 0, negative: data.operationalEfficiencyChange < 0 }">
                {{ data.operationalEfficiencyChange > 0 ? '+' : '' }}{{ data.operationalEfficiencyChange }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon innovation-index">
              <i class="el-icon-magic-stick"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ data.innovationIndex }}</div>
              <div class="card-label">创新指数</div>
              <div class="card-change" :class="{ positive: data.innovationIndexChange > 0, negative: data.innovationIndexChange < 0 }">
                {{ data.innovationIndexChange > 0 ? '+' : '' }}{{ data.innovationIndexChange }}
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <el-row :gutter="24">
        <!-- 经营效率趋势图 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">经营效率趋势</h4>
            <div ref="efficiencyTrendChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 市场竞争力分析 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">市场竞争力分析</h4>
            <div ref="competitivenessChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 客户分析 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">客户分析</h4>
            <div ref="customerAnalysisChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
        
        <!-- 业务发展分析 -->
        <el-col :span="12">
          <div class="chart-card">
            <h4 class="chart-card-title">业务发展分析</h4>
            <div ref="businessDevelopmentChart" class="chart" style="height: 300px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 经营分析表格 -->
    <div class="operational-analysis-table">
      <h4 class="table-title">详细经营分析</h4>
      <el-table :data="data.operationalDetails" border stripe>
        <el-table-column prop="dimension" label="分析维度" width="120"></el-table-column>
        <el-table-column prop="indicator" label="关键指标" width="150"></el-table-column>
        <el-table-column prop="currentValue" label="当前值" width="100" align="right"></el-table-column>
        <el-table-column prop="targetValue" label="目标值" width="100" align="right"></el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="100" align="right">
          <template slot-scope="scope">
            <span :class="{ positive: scope.row.completionRate >= 100, warning: scope.row.completionRate < 100 }">
              {{ scope.row.completionRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="trend" label="趋势" width="100" align="center">
          <template slot-scope="scope">
            <i :class="getTrendIcon(scope.row.trend)" :style="{ color: getTrendColor(scope.row.trend) }"></i>
            {{ scope.row.trend }}
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
  name: 'OperationalAnalysisChart',
  props: {
    data: {
      type: Object,
      default: () => ({
        marketShare: 15.8,
        marketShareChange: 2.3,
        customerSatisfaction: 92.5,
        customerSatisfactionChange: 1.8,
        operationalEfficiency: 88.2,
        operationalEfficiencyChange: 3.5,
        innovationIndex: 7.8,
        innovationIndexChange: 0.5,
        operationalDetails: [
          {
            dimension: '市场表现',
            indicator: '市场份额',
            currentValue: '15.8%',
            targetValue: '18.0%',
            completionRate: 87.8,
            trend: '上升',
            analysis: '市场份额稳步提升，但仍需加强市场拓展'
          },
          {
            dimension: '客户服务',
            indicator: '客户满意度',
            currentValue: '92.5%',
            targetValue: '95.0%',
            completionRate: 97.4,
            trend: '上升',
            analysis: '客户满意度持续改善，服务质量不断提升'
          },
          {
            dimension: '运营效率',
            indicator: '运营效率指数',
            currentValue: '88.2%',
            targetValue: '90.0%',
            completionRate: 98.0,
            trend: '上升',
            analysis: '运营效率显著提升，流程优化效果明显'
          },
          {
            dimension: '创新能力',
            indicator: '创新指数',
            currentValue: '7.8',
            targetValue: '8.5',
            completionRate: 91.8,
            trend: '上升',
            analysis: '创新投入增加，研发能力持续增强'
          },
          {
            dimension: '人力资源',
            indicator: '员工满意度',
            currentValue: '85.6%',
            targetValue: '88.0%',
            completionRate: 97.3,
            trend: '稳定',
            analysis: '员工满意度保持较高水平，团队稳定性好'
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
      this.initEfficiencyTrendChart()
      this.initCompetitivenessChart()
      this.initCustomerAnalysisChart()
      this.initBusinessDevelopmentChart()
    },
    initEfficiencyTrendChart() {
      const chart = echarts.init(this.$refs.efficiencyTrendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['运营效率', '生产效率', '管理效率', '创新效率']
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
            name: '运营效率',
            type: 'line',
            data: [82.5, 84.2, 85.8, 86.5, 87.2, 88.2],
            smooth: true,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '生产效率',
            type: 'line',
            data: [78.2, 79.5, 81.2, 82.8, 84.1, 85.6],
            smooth: true,
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '管理效率',
            type: 'line',
            data: [85.6, 86.2, 87.1, 87.8, 88.5, 89.2],
            smooth: true,
            itemStyle: { color: '#e6a23c' }
          },
          {
            name: '创新效率',
            type: 'line',
            data: [72.8, 74.5, 76.2, 77.8, 78.9, 80.1],
            smooth: true,
            itemStyle: { color: '#f56c6c' }
          }
        ]
      }
      chart.setOption(option)
    },
    initCompetitivenessChart() {
      const chart = echarts.init(this.$refs.competitivenessChart)
      const option = {
        tooltip: {},
        radar: {
          indicator: [
            { name: '市场份额', max: 100 },
            { name: '品牌影响力', max: 100 },
            { name: '技术实力', max: 100 },
            { name: '服务质量', max: 100 },
            { name: '价格竞争力', max: 100 },
            { name: '创新能力', max: 100 }
          ]
        },
        series: [{
          name: '竞争力分析',
          type: 'radar',
          data: [
            {
              value: [78, 85, 92, 88, 75, 82],
              name: '本公司',
              itemStyle: { color: '#409eff' }
            },
            {
              value: [82, 78, 85, 82, 80, 75],
              name: '主要竞争对手',
              itemStyle: { color: '#67c23a' }
            },
            {
              value: [85, 90, 95, 92, 85, 88],
              name: '行业领先者',
              itemStyle: { color: '#e6a23c' }
            }
          ]
        }]
      }
      chart.setOption(option)
    },
    initCustomerAnalysisChart() {
      const chart = echarts.init(this.$refs.customerAnalysisChart)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: ['新客户', '老客户', '重点客户', '潜在客户']
        },
        series: [
          {
            name: '客户结构',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
            data: [
              { value: 25, name: '新客户', itemStyle: { color: '#409eff' } },
              { value: 45, name: '老客户', itemStyle: { color: '#67c23a' } },
              { value: 20, name: '重点客户', itemStyle: { color: '#e6a23c' } },
              { value: 10, name: '潜在客户', itemStyle: { color: '#f56c6c' } }
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
    initBusinessDevelopmentChart() {
      const chart = echarts.init(this.$refs.businessDevelopmentChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['收入增长', '利润增长', '市场扩张', '技术创新']
        },
        xAxis: {
          type: 'category',
          data: ['Q1', 'Q2', 'Q3', 'Q4']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}%'
          }
        },
        series: [
          {
            name: '收入增长',
            type: 'bar',
            data: [12.5, 15.2, 18.6, 22.1],
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '利润增长',
            type: 'bar',
            data: [8.2, 11.5, 14.8, 18.3],
            itemStyle: { color: '#409eff' }
          },
          {
            name: '市场扩张',
            type: 'bar',
            data: [5.8, 8.2, 12.5, 15.8],
            itemStyle: { color: '#e6a23c' }
          },
          {
            name: '技术创新',
            type: 'bar',
            data: [6.5, 7.2, 7.8, 8.5],
            itemStyle: { color: '#f56c6c' }
          }
        ]
      }
      chart.setOption(option)
    },
    getTrendIcon(trend) {
      const icons = {
        '上升': 'el-icon-top',
        '下降': 'el-icon-bottom',
        '稳定': 'el-icon-minus'
      }
      return icons[trend] || 'el-icon-minus'
    },
    getTrendColor(trend) {
      const colors = {
        '上升': '#67c23a',
        '下降': '#f56c6c',
        '稳定': '#e6a23c'
      }
      return colors[trend] || '#909399'
    }
  }
}
</script>

<style lang="scss" scoped>
.operational-analysis-chart {
  .chart-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 24px 0;
    padding-bottom: 12px;
    border-bottom: 2px solid #f56c6c;
  }
}

.operational-overview {
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

      &.market-share {
        background: linear-gradient(135deg, #f56c6c 0%, #f04864 100%);
      }

      &.customer-satisfaction {
        background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
      }

      &.operational-efficiency {
        background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
      }

      &.innovation-index {
        background: linear-gradient(135deg, #e6a23c 0%, #d48806 100%);
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

.operational-analysis-table {
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

    .warning {
      color: #e6a23c;
    }

    .negative {
      color: #f56c6c;
    }
  }
}
</style>
