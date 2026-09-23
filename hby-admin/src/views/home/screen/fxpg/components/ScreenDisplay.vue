<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <!-- 标题区域 -->
      <div class="header-content">
        <div class="header-left">
          <div class="current-time">{{ currentTime }}</div>
        </div>
        <div class="header-title">
          <div class="title-main">风险评估管理</div>
        </div>
        <div class="header-right">
          <el-button
            type="danger"
            icon="el-icon-close"
            circle
            size="small"
            @click="handleClose"
            class="close-btn"
          ></el-button>
        </div>
      </div>
    </div>

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧区域 -->
      <div class="left-area">
        <!-- 风险评估结果 -->
        <div class="panel-box panel-stats-box">
          <div class="panel-title">风险评估结果</div>
          <div class="stats-content">
            <div class="stat-item-large">
              <div class="stat-label">评估总数</div>
              <div class="stat-value-large">{{ queryStats.totalCount }}</div>
            </div>
          </div>
        </div>

        <!-- 风险评估数量 -->
        <div class="panel-box panel-chart-box">
          <div class="panel-title">风险评估数量</div>
          <div ref="assessmentCountChart" class="chart-container"></div>
        </div>

        <!-- 最终审议单位级次 -->
        <div class="panel-box panel-chart-box">
          <div class="panel-title">最终审议单位级次</div>
          <div ref="unitLevelChart" class="chart-container"></div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-area">
        <!-- 风险评估性质统计 -->
        <div class="panel-box panel-table-full">
          <div class="panel-title">风险评估性质统计</div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th width="60">序号</th>
                  <th>企业名称</th>
                  <th>评估事项</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in assessmentNatureList" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.company }}</td>
                  <td>{{ item.item }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 风险评估事项统计 -->
        <div class="panel-box panel-chart-middle">
          <div class="panel-title">风险评估事项统计</div>
          <div ref="assessmentStatsChart" class="chart-container"></div>
        </div>

        <!-- 高风险评估事项 -->
        <div class="panel-box panel-table-full">
          <div class="panel-title">高风险评估事项</div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th width="60">序号</th>
                  <th>企业名称</th>
                  <th>评估事项</th>
                  <th width="100">风险程度</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in highRiskList" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.company }}</td>
                  <td>{{ item.item }}</td>
                  <td>
                    <span class="risk-tag" :class="item.level">{{ item.level }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area">
        <!-- 事项类型 -->
        <div class="panel-box panel-chart-full">
          <div class="panel-title">事项类型</div>
          <div ref="itemTypeChart" class="chart-container"></div>
        </div>

        <!-- 事项风险程度 -->
        <div class="panel-box panel-chart-full">
          <div class="panel-title">事项风险程度</div>
          <div ref="riskLevelChart" class="chart-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getRiskAssessmentData } from '../api/riskAssessment'

export default {
  name: 'ScreenDisplay',
  props: {
    riskData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      currentTime: '',
      timer: null,
      assessmentNatureList: [],
      highRiskList: [],
      queryStats: {
        totalCount: 761,
        highRisk: 42,
        mediumRisk: 230,
        lowRisk: 489
      },
      charts: {}
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.loadData()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    async loadData() {
      try {
        const data = this.riskData || await getRiskAssessmentData()
        this.assessmentNatureList = data.assessmentNature || []
        this.highRiskList = data.highRiskItems || []

        this.$nextTick(() => {
          this.initCharts(data)
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts(data) {
      this.initAssessmentCountChart(data.assessmentCount)
      this.initItemTypeChart(data.itemType)
      this.initRiskLevelChart(data.riskLevel)
      this.initAssessmentStatsChart(data.assessmentStats)
      this.initUnitLevelChart(data.unitLevel)
    },
    handleClose() {
      this.$emit('close')
    },
    // 风险评估数量环形图
    initAssessmentCountChart(data) {
      if (!this.$refs.assessmentCountChart) return

      const chartData = data || [
        { name: '已评估', value: 470, itemStyle: { color: '#00d4ff' } },
        { name: '未评估', value: 300, itemStyle: { color: '#1a2744' } }
      ]

      const chart = echarts.init(this.$refs.assessmentCountChart)
      this.charts.assessmentCount = chart

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((chartData[0].value / total) * 100).toFixed(1)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '45%',
          style: {
            text: percentage + '%',
            textAlign: 'center',
            fill: '#00d4ff',
            fontSize: 28,
            fontWeight: 'bold'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: chartData,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    },
    // 事项类型环形图
    initItemTypeChart(data) {
      if (!this.$refs.itemTypeChart) return

      const chartData = data || [
        { name: '委托1', value: 32, itemStyle: { color: '#00d4ff' } },
        { name: '委托2', value: 12, itemStyle: { color: '#ffd700' } },
        { name: '委托3', value: 5, itemStyle: { color: '#ff6b6b' } },
        { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
      ]

      const chart = echarts.init(this.$refs.itemTypeChart)
      this.charts.itemType = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: { color: '#fff', fontSize: 12 }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['35%', '50%'],
          data: chartData,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    },
    // 事项风险程度环形图
    initRiskLevelChart(data) {
      if (!this.$refs.riskLevelChart) return

      const chartData = data || [
        { name: '高', value: 42, itemStyle: { color: '#ff6b6b' } },
        { name: '中', value: 230, itemStyle: { color: '#ffd700' } },
        { name: '低', value: 489, itemStyle: { color: '#00d4ff' } }
      ]

      const chart = echarts.init(this.$refs.riskLevelChart)
      this.charts.riskLevel = chart

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const highRiskPercentage = ((chartData[0].value / total) * 100).toFixed(1)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '42%',
          style: {
            text: '高风险',
            textAlign: 'center',
            fill: '#fff',
            fontSize: 14
          }
        }, {
          type: 'text',
          left: 'center',
          top: '52%',
          style: {
            text: highRiskPercentage + '%',
            textAlign: 'center',
            fill: '#ff6b6b',
            fontSize: 24,
            fontWeight: 'bold'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: chartData,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    },
    // 风险评估事项统计柱状图
    initAssessmentStatsChart(chartData) {
      if (!this.$refs.assessmentStatsChart) return

      const data = chartData || {
        categories: ['2018/1', '2018/2', '2018/3', '2018/4', '2018/5', '2018/6'],
        series: [
          { name: '低', values: [500, 600, 550, 650, 600, 700] },
          { name: '中', values: [300, 350, 320, 380, 340, 400] },
          { name: '高', values: [200, 250, 230, 270, 260, 300] }
        ]
      }

      const chart = echarts.init(this.$refs.assessmentStatsChart)
      this.charts.assessmentStats = chart

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: data.series.map(s => s.name),
          textStyle: { color: '#fff' },
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.categories,
          axisLabel: { color: '#fff' },
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#fff' },
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
        },
        series: data.series.map((item, index) => ({
          name: item.name,
          type: 'bar',
          stack: 'total',
          data: item.values,
          itemStyle: {
            color: ['#00d4ff', '#ffd700', '#ff6b6b'][index]
          }
        }))
      }

      chart.setOption(option)
    },
    // 最终审议单位级次环形图
    initUnitLevelChart(data) {
      if (!this.$refs.unitLevelChart) return

      const chartData = data || [
        { name: '委托1', value: 40, itemStyle: { color: '#00d4ff' } },
        { name: '委托2', value: 30, itemStyle: { color: '#ffd700' } },
        { name: '委托3', value: 15, itemStyle: { color: '#ff6b6b' } },
        { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
      ]

      const chart = echarts.init(this.$refs.unitLevelChart)
      this.charts.unitLevel = chart

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((chartData[0].value / total) * 100).toFixed(1)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: '30%',
          top: '48%',
          style: {
            text: percentage + '%',
            textAlign: 'center',
            fill: '#00d4ff',
            fontSize: 24,
            fontWeight: 'bold'
          }
        }],
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: { color: '#fff', fontSize: 12 },
          itemWidth: 12,
          itemHeight: 12,
          itemGap: 10
        },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['30%', '50%'],
          data: chartData,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.screen-display {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .screen-header {
    padding: 15px 30px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 1px solid rgba(64, 158, 255, 0.2);

    .header-content {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .header-left {
        flex: 1;

        .current-time {
          font-size: 14px;
          color: #00d4ff;
          font-family: 'Courier New', monospace;
        }
      }

      .header-title {
        flex: 2;
        text-align: center;

        .title-main {
          font-size: 28px;
          font-weight: bold;
          background: linear-gradient(90deg, #00d4ff 0%, #00ff88 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          letter-spacing: 4px;
          text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
        }
      }

      .header-right {
        flex: 1;
        display: flex;
        justify-content: flex-end;
      }
    }
  }

  .screen-body {
    flex: 1;
    display: flex;
    padding: 15px;
    gap: 15px;
    overflow: hidden;
    min-height: 0;

    .left-area,
    .center-area,
    .right-area {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 15px;
      overflow-y: auto;
      overflow-x: hidden;
      min-height: 0;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: rgba(0, 212, 255, 0.1);
        border-radius: 3px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 212, 255, 0.4);
        border-radius: 3px;

        &:hover {
          background: rgba(0, 212, 255, 0.6);
        }
      }
    }

    .panel-box {
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 12px;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      // 左侧面板高度
      &.panel-stats-box {
        height: 180px;
        flex-shrink: 0;
      }

      &.panel-chart-box {
        flex: 1;
        min-height: 0;
      }

      // 中间面板 - 占满整个高度
      &.panel-table-full {
        flex: 1;
        min-height: 0;
      }

      &.panel-chart-middle {
        height: 280px;
        flex-shrink: 0;
      }

      // 右侧面板 - 占满整个高度
      &.panel-chart-full {
        flex: 1;
        min-height: 0;
      }

      .panel-title {
        font-size: 16px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 12px;
        padding-left: 10px;
        border-left: 3px solid #00d4ff;
        flex-shrink: 0;
      }

      .stats-content {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;

        .stat-item-large {
          text-align: center;

          .stat-label {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.6);
            margin-bottom: 15px;
          }

          .stat-value-large {
            font-size: 48px;
            font-weight: bold;
            color: #00d4ff;
            font-family: 'Arial', sans-serif;
          }
        }
      }

      .chart-container {
        flex: 1;
        width: 100%;
        min-height: 0;
      }

      .table-container {
        flex: 1;
        overflow-y: auto;
        min-height: 0;

        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-thumb {
          background: rgba(0, 212, 255, 0.3);
          border-radius: 3px;
        }
      }

      .data-table {
        width: 100%;
        border-collapse: collapse;
        font-size: 12px;

        th {
          background: rgba(0, 212, 255, 0.2);
          color: #00d4ff;
          padding: 8px;
          text-align: left;
          font-weight: bold;
          border-bottom: 1px solid rgba(0, 212, 255, 0.3);
        }

        td {
          padding: 8px;
          border-bottom: 1px solid rgba(255, 255, 255, 0.1);
          color: rgba(255, 255, 255, 0.8);
        }

        tbody tr {
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 212, 255, 0.1);
          }
        }
      }

      .stats-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 15px;

        .stat-item {
          text-align: center;

          .stat-label {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.7);
            margin-bottom: 8px;
          }

          .stat-value {
            font-size: 24px;
            font-weight: bold;
            color: #00d4ff;

            .unit {
              font-size: 14px;
              margin-left: 5px;
            }
          }
        }
      }

      .risk-tag {
        padding: 2px 8px;
        border-radius: 4px;
        font-size: 11px;
        font-weight: bold;

        &.高 {
          background: rgba(255, 107, 107, 0.2);
          color: #ff6b6b;
          border: 1px solid #ff6b6b;
        }

        &.中 {
          background: rgba(255, 215, 0, 0.2);
          color: #ffd700;
          border: 1px solid #ffd700;
        }

        &.低 {
          background: rgba(0, 212, 255, 0.2);
          color: #00d4ff;
          border: 1px solid #00d4ff;
        }
      }

      .query-stats {
        flex: 1;
        padding: 8px 0;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        min-height: 0;

        .stat-row {
          display: flex;
          gap: 12px;

          &:last-child {
            margin-bottom: 0;
          }
        }

        .stat-item-inline {
          flex: 1;
          text-align: center;
          padding: 10px;
          background: rgba(0, 212, 255, 0.05);
          border-radius: 6px;
          border: 1px solid rgba(0, 212, 255, 0.2);

          .stat-label {
            font-size: 11px;
            color: rgba(255, 255, 255, 0.7);
            margin-bottom: 6px;
          }

          .stat-value {
            font-size: 22px;
            font-weight: bold;

            &.cyan {
              color: #00d4ff;
              text-shadow: 0 0 15px rgba(0, 212, 255, 0.5);
            }

            &.red {
              color: #ff6b6b;
              text-shadow: 0 0 15px rgba(255, 107, 107, 0.5);
            }

            &.yellow {
              color: #ffd700;
              text-shadow: 0 0 15px rgba(255, 215, 0, 0.5);
            }

            &.green {
              color: #00ff88;
              text-shadow: 0 0 15px rgba(0, 255, 136, 0.5);
            }

            .unit {
              font-size: 12px;
              margin-left: 4px;
            }
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.9;
  }
}
</style>

