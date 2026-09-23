<template>
  <div class="risk-control-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2 class="page-title">法律案件穿透</h2>
      <el-button type="primary" icon="el-icon-full-screen" @click="openFullScreen">
        大屏
      </el-button>
    </div>

    <!-- 数据概览卡片 -->
    <div class="overview-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon" style="background: rgba(64, 158, 255, 0.2);">
              <i class="el-icon-warning" style="color: #409eff;"></i>
            </div>
            <div class="card-content">
              <div class="card-label">未结案件数量</div>
              <div class="card-value">
                <span class="value">{{ overviewData.unsettledCount }}</span>
                <span class="unit">件</span>
              </div>
              <div class="card-trend" :class="overviewData.unsettledTrend > 0 ? 'up' : 'down'">
                <i :class="overviewData.unsettledTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.unsettledTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon" style="background: rgba(103, 194, 58, 0.2);">
              <i class="el-icon-document-checked" style="color: #67c23a;"></i>
            </div>
            <div class="card-content">
              <div class="card-label">办结案件数</div>
              <div class="card-value">
                <span class="value">{{ overviewData.settledCount }}</span>
                <span class="unit">件</span>
              </div>
              <div class="card-trend" :class="overviewData.settledTrend > 0 ? 'up' : 'down'">
                <i :class="overviewData.settledTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.settledTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon" style="background: rgba(230, 162, 60, 0.2);">
              <i class="el-icon-bell" style="color: #e6a23c;"></i>
            </div>
            <div class="card-content">
              <div class="card-label">挽回损失金额</div>
              <div class="card-value">
                <span class="value">{{ overviewData.recoveredAmount }}</span>
                <span class="unit">万元</span>
              </div>
              <div class="card-trend" :class="overviewData.recoveredTrend > 0 ? 'up' : 'down'">
                <i :class="overviewData.recoveredTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.recoveredTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card">
            <div class="card-icon" style="background: rgba(245, 108, 108, 0.2);">
              <i class="el-icon-coin" style="color: #f56c6c;"></i>
            </div>
            <div class="card-content">
              <div class="card-label">涉外案件数</div>
              <div class="card-value">
                <span class="value">{{ overviewData.foreignCases }}</span>
                <span class="unit">件</span>
              </div>
              <div class="card-trend" :class="overviewData.foreignCasesTrend > 0 ? 'up' : 'down'">
                <i :class="overviewData.foreignCasesTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.foreignCasesTrend) }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据图表展示 -->
    <div class="charts-section">
      <!-- 第一行 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">未结案件数量趋势分布</span>
            </div>
            <div class="chart-body">
              <div ref="unsettledTrendChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">新增/存量案件数量</span>
            </div>
            <div class="chart-body">
              <div ref="newStockCompareChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">办结案件数及挽回损失金额</span>
            </div>
            <div class="chart-body">
              <div ref="settledRecoveryChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 第二行 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">未结案件数量及金额</span>
            </div>
            <div class="chart-body">
              <div ref="unsettledAmountChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">未结重点案件</span>
            </div>
            <div class="chart-body">
              <div ref="unsettledKeyChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">未结案件数量</span>
            </div>
            <div class="chart-body">
              <div ref="unsettledCountChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 第三行 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">诉讼角色</span>
            </div>
            <div class="chart-body">
              <div ref="litigationRoleChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">涉外案件</span>
            </div>
            <div class="chart-body">
              <div ref="foreignCasesChart" class="chart"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="chart-header">
              <span class="chart-title">案件标的金额分布</span>
            </div>
            <div class="chart-body">
              <div ref="amountDistributionChart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <screen-display
        v-if="screenVisible"
        :risk-data="riskData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getRiskControlData } from './api/riskControl'

export default {
  name: 'RiskControlScreen',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      overviewData: {
        unsettledCount: 1250,
        unsettledTrend: -5.2,
        settledCount: 850,
        settledTrend: 3.5,
        recoveredAmount: 12500,
        recoveredTrend: 8.2,
        foreignCases: 45,
        foreignCasesTrend: -2.1
      },
      riskData: {},
      charts: {}
    }
  },
  mounted() {
    this.loadData()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    async loadData() {
      try {
        const res = await getRiskControlData()
        if (res.code === 200) {
          this.riskData = res.data
        }
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    },
    handleScreenClose() {
      this.screenVisible = false
    },
    initCharts() {
      this.initUnsettledTrendChart()
      this.initNewStockCompareChart()
      this.initSettledRecoveryChart()
      this.initUnsettledAmountChart()
      this.initUnsettledKeyChart()
      this.initUnsettledCountChart()
      this.initLitigationRoleChart()
      this.initForeignCasesChart()
      this.initAmountDistributionChart()
    },
    // 1. 未结案件数量趋势分布
    initUnsettledTrendChart() {
      if (!this.$refs.unsettledTrendChart) return
      const chart = echarts.init(this.$refs.unsettledTrendChart)
      this.charts.unsettledTrend = chart

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '未结案件数量',
          type: 'line',
          smooth: true,
          data: [120, 150, 180, 160, 200, 220, 190, 170, 150, 130, 110, 100],
          lineStyle: { color: '#00d4ff', width: 2 },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
              { offset: 1, color: 'rgba(0, 212, 255, 0.05)' }
            ])
          }
        }]
      }

      chart.setOption(option)
    },
    // 2. 新增/存量案件数量
    initNewStockCompareChart() {
      if (!this.$refs.newStockCompareChart) return
      const chart = echarts.init(this.$refs.newStockCompareChart)
      this.charts.newStockCompare = chart

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['新增案件', '存量案件'], top: '5%' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: { type: 'value' },
        series: [
          {
            name: '新增案件',
            type: 'bar',
            data: [50, 60, 70, 65, 80, 75],
            itemStyle: { color: '#ffd700' }
          },
          {
            name: '存量案件',
            type: 'bar',
            data: [120, 130, 140, 135, 150, 145],
            itemStyle: { color: '#00d4ff' }
          }
        ]
      }

      chart.setOption(option)
    },
    // 3. 办结案件数及挽回损失金额
    initSettledRecoveryChart() {
      if (!this.$refs.settledRecoveryChart) return
      const chart = echarts.init(this.$refs.settledRecoveryChart)
      this.charts.settledRecovery = chart

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['办结案件数', '挽回损失金额'], top: '5%' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: [
          {
            type: 'value',
            name: '案件数(件)',
            axisLabel: { formatter: '{value}' }
          },
          {
            type: 'value',
            name: '金额(万元)',
            axisLabel: { formatter: '{value}' }
          }
        ],
        series: [
          {
            name: '办结案件数',
            type: 'bar',
            data: [45, 52, 48, 55, 60, 58],
            itemStyle: { color: '#00d4ff' }
          },
          {
            name: '挽回损失金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: [1200, 1500, 1300, 1800, 2000, 1900],
            lineStyle: { color: '#ffd700', width: 2 },
            itemStyle: { color: '#ffd700' }
          }
        ]
      }

      chart.setOption(option)
    },
    // 4. 未结案件数量及金额
    initUnsettledAmountChart() {
      if (!this.$refs.unsettledAmountChart) return
      const chart = echarts.init(this.$refs.unsettledAmountChart)
      this.charts.unsettledAmount = chart

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['未结案件数', '涉及金额'], top: '5%' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: [
          {
            type: 'value',
            name: '案件数(件)'
          },
          {
            type: 'value',
            name: '金额(万元)'
          }
        ],
        series: [
          {
            name: '未结案件数',
            type: 'bar',
            data: [120, 130, 125, 140, 135, 130],
            itemStyle: { color: '#00d4ff' }
          },
          {
            name: '涉及金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: [5000, 5500, 5200, 6000, 5800, 5600],
            lineStyle: { color: '#ffd700', width: 2 },
            itemStyle: { color: '#ffd700' }
          }
        ]
      }

      chart.setOption(option)
    },
    // 5. 未结重点案件
    initUnsettledKeyChart() {
      if (!this.$refs.unsettledKeyChart) return
      const chart = echarts.init(this.$refs.unsettledKeyChart)
      this.charts.unsettledKey = chart

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['重大案件', '疑难案件', '涉外案件', '集团案件', '其他案件']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '未结重点案件',
          type: 'bar',
          data: [25, 18, 12, 30, 15],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ff6b6b' },
              { offset: 1, color: '#ff9999' }
            ])
          }
        }]
      }

      chart.setOption(option)
    },
    // 6. 未结案件数量
    initUnsettledCountChart() {
      if (!this.$refs.unsettledCountChart) return
      const chart = echarts.init(this.$refs.unsettledCountChart)
      this.charts.unsettledCount = chart

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['一审', '二审', '再审', '执行', '其他']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '未结案件数量',
          type: 'bar',
          data: [80, 45, 12, 60, 25],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
          }
        }]
      }

      chart.setOption(option)
    },
    // 7. 诉讼角色
    initLitigationRoleChart() {
      if (!this.$refs.litigationRoleChart) return
      const chart = echarts.init(this.$refs.litigationRoleChart)
      this.charts.litigationRole = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '诉讼角色',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 450, name: '原告', itemStyle: { color: '#00d4ff' } },
            { value: 350, name: '被告', itemStyle: { color: '#ffd700' } },
            { value: 200, name: '第三人', itemStyle: { color: '#00ff88' } }
          ],
          label: { show: true, formatter: '{b}: {d}%' }
        }]
      }

      chart.setOption(option)
    },
    // 8. 涉外案件
    initForeignCasesChart() {
      if (!this.$refs.foreignCasesChart) return
      const chart = echarts.init(this.$refs.foreignCasesChart)
      this.charts.foreignCases = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '涉外案件',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 45, name: '涉外案件', itemStyle: { color: '#ff6b6b' } },
            { value: 955, name: '非涉外案件', itemStyle: { color: '#00d4ff' } }
          ],
          label: { show: true, formatter: '{b}: {d}%' }
        }]
      }

      chart.setOption(option)
    },
    // 9. 案件标的金额分布
    initAmountDistributionChart() {
      if (!this.$refs.amountDistributionChart) return
      const chart = echarts.init(this.$refs.amountDistributionChart)
      this.charts.amountDistribution = chart

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['10万以下', '10-50万', '50-100万', '100-500万', '500-1000万', '1000万以上']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '案件数量',
          type: 'bar',
          data: [150, 200, 120, 80, 40, 20],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#a78bfa' },
              { offset: 1, color: '#00d4ff' }
            ])
          }
        }]
      }

      chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-control-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 15px 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .page-title {
      margin: 0;
      font-size: 20px;
      font-weight: bold;
      color: #303133;
    }
  }

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .card-icon {
        width: 60px;
        height: 60px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 8px;
        margin-right: 15px;

        i {
          font-size: 28px;
        }
      }

      .card-content {
        flex: 1;

        .card-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }

        .card-value {
          margin-bottom: 5px;

          .value {
            font-size: 28px;
            font-weight: bold;
            color: #303133;
          }

          .unit {
            font-size: 14px;
            color: #909399;
            margin-left: 5px;
          }
        }

        .card-trend {
          font-size: 12px;
          display: flex;
          align-items: center;
          gap: 4px;

          &.up {
            color: #f56c6c;
          }

          &.down {
            color: #67c23a;
          }
        }
      }
    }
  }

  .charts-section {
    .chart-card {
      background: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      overflow: hidden;

      .chart-header {
        padding: 15px 20px;
        border-bottom: 1px solid #ebeef5;

        .chart-title {
          font-size: 16px;
          font-weight: bold;
          color: #303133;
        }
      }

      .chart-body {
        padding: 20px;

        .chart {
          height: 300px;
        }
      }
    }
  }
}

::v-deep .screen-dialog {
  background: transparent;

  .el-dialog__body {
    padding: 0;
    height: 100vh;
  }
}
</style>

