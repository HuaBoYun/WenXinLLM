<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">风险事件管理</div>
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

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧区域 -->
      <div class="left-area">
        <!-- 风险事件金额排名 -->
        <div class="panel-box">
          <div class="panel-title">风险事件金额排名</div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>企业名称</th>
                  <th>事件类型</th>
                  <th>金额(万元)</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in eventAmountRanking" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.company }}</td>
                  <td>{{ item.eventType }}</td>
                  <td class="amount-cell">{{ item.amount }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 风险评估数量 -->
        <div class="panel-box">
          <div class="panel-title">风险评估数量</div>
          <div ref="assessmentCountChart" class="chart-container"></div>
        </div>

        <!-- 风险事件类型 -->
        <div class="panel-box flex-1">
          <div class="panel-title">风险事件类型</div>
          <div ref="eventTypeChart" class="chart-container"></div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-area">
        <!-- 风险事件数量及金额 -->
        <div class="panel-box flex-1">
          <div class="panel-title">风险事件数量及金额</div>
          <div ref="eventTrendChart" class="chart-container large"></div>
        </div>

        <!-- 风险事件化解情况 -->
        <div class="panel-box">
          <div class="panel-title">风险事件化解情况</div>
          <div ref="eventResolutionChart" class="chart-container"></div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area">
        <!-- 高风险评估事项 -->
        <div class="panel-box flex-1">
          <div class="panel-title">高风险评估事项</div>
          <div class="table-container scrollable">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>企业名称</th>
                  <th>评估事项</th>
                  <th>风险程度</th>
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

        <!-- 事件诉讼情况占比 -->
        <div class="panel-box">
          <div class="panel-title">事件诉讼情况占比</div>
          <div ref="litigationChart" class="chart-container"></div>
        </div>

        <!-- 风险事件数量及金额查询 -->
        <div class="panel-box">
          <div class="panel-title">风险事件数量及金额查询</div>
          <div class="query-stats">
            <div class="stat-row">
              <div class="stat-item-inline">
                <div class="stat-label">风险事件数量</div>
                <div class="stat-value cyan">{{ queryStats.eventCount }}<span class="unit">件</span></div>
              </div>
              <div class="stat-item-inline">
                <div class="stat-label">涉及金额</div>
                <div class="stat-value red">{{ queryStats.totalAmount }}<span class="unit">亿元</span></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getRiskEventData } from '../api/riskEvent'

export default {
  name: 'ScreenDisplay',
  props: {
    eventData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      currentTime: '',
      timer: null,
      eventAmountRanking: [],
      highRiskList: [],
      queryStats: {
        eventCount: 761,
        totalAmount: 25
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
        const data = this.eventData || await getRiskEventData()
        this.eventAmountRanking = data.eventAmountRanking || []
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
      this.initEventTypeChart(data.eventType)
      this.initEventTrendChart(data.eventTrend)
      this.initEventResolutionChart(data.eventResolution)
      this.initLitigationChart(data.litigation)
    },
    handleClose() {
      this.$emit('close')
    },
    // 风险评估数量环形图
    initAssessmentCountChart(data) {
      if (!this.$refs.assessmentCountChart) return

      const chart = echarts.init(this.$refs.assessmentCountChart)
      this.charts.assessmentCount = chart

      const chartData = data || [
        { name: '已评估', value: 470, itemStyle: { color: '#00d4ff' } },
        { name: '未评估', value: 300, itemStyle: { color: '#1a2744' } }
      ]

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((chartData[0].value / total) * 100).toFixed(0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '40%',
          style: {
            text: `${percentage}%`,
            fontSize: 24,
            fontWeight: 'bold',
            fill: '#00d4ff'
          }
        }, {
          type: 'text',
          left: 'center',
          top: '55%',
          style: {
            text: `${chartData[0].value}件`,
            fontSize: 14,
            fill: '#fff'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: chartData,
          label: { show: false },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    },
    // 风险事件类型环形图
    initEventTypeChart(data) {
      if (!this.$refs.eventTypeChart) return

      const chart = echarts.init(this.$refs.eventTypeChart)
      this.charts.eventType = chart

      const chartData = data || [
        { name: '合同纠纷', value: 230, itemStyle: { color: '#00d4ff' } },
        { name: '债务违约', value: 180, itemStyle: { color: '#ffd700' } },
        { name: '担保诉讼', value: 150, itemStyle: { color: '#ff6b6b' } },
        { name: '劳动纠纷', value: 120, itemStyle: { color: '#a78bfa' } },
        { name: '知识产权', value: 81, itemStyle: { color: '#00ff88' } }
      ]

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: { color: '#fff', fontSize: 11 }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['35%', '50%'],
          data: chartData,
          label: {
            show: true,
            formatter: '{d}%',
            color: '#fff',
            fontSize: 10
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
    // 风险事件数量及金额趋势图
    initEventTrendChart(chartData) {
      if (!this.$refs.eventTrendChart) return

      const chart = echarts.init(this.$refs.eventTrendChart)
      this.charts.eventTrend = chart

      const data = chartData || {
        categories: ['2018/1', '2018/2', '2018/3', '2018/4', '2018/5', '2018/6', '2018/7', '2018/8', '2018/9', '2018/10', '2018/11', '2018/12'],
        eventCount: [120, 150, 130, 160, 140, 180, 170, 190, 160, 200, 180, 220],
        amount: [3000, 3500, 3200, 4000, 3800, 4500, 4200, 4800, 4300, 5000, 4600, 5500]
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['事件数量', '涉及金额'],
          textStyle: { color: '#fff', fontSize: 12 },
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
          axisLabel: {
            color: '#fff',
            fontSize: 10,
            rotate: 30
          },
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } }
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(件)',
            position: 'left',
            axisLabel: { color: '#fff', fontSize: 10 },
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right',
            axisLabel: { color: '#fff', fontSize: 10 },
            axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '事件数量',
            type: 'bar',
            data: data.eventCount,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#00d4ff' },
                { offset: 1, color: 'rgba(0, 212, 255, 0.3)' }
              ])
            }
          },
          {
            name: '涉及金额',
            type: 'line',
            yAxisIndex: 1,
            data: data.amount,
            itemStyle: { color: '#ffd700' },
            lineStyle: { width: 3 },
            smooth: true
          }
        ]
      }

      chart.setOption(option)
    },
    // 风险事件化解情况环形图
    initEventResolutionChart(data) {
      if (!this.$refs.eventResolutionChart) return

      const chart = echarts.init(this.$refs.eventResolutionChart)
      this.charts.eventResolution = chart

      const chartData = data || [
        { name: '已化解', value: 320, itemStyle: { color: '#00ff88' } },
        { name: '化解中', value: 230, itemStyle: { color: '#ffd700' } },
        { name: '未化解', value: 211, itemStyle: { color: '#ff6b6b' } }
      ]

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((chartData[0].value / total) * 100).toFixed(0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '40%',
          style: {
            text: `${percentage}%`,
            fontSize: 24,
            fontWeight: 'bold',
            fill: '#00ff88'
          }
        }, {
          type: 'text',
          left: 'center',
          top: '55%',
          style: {
            text: '已化解',
            fontSize: 14,
            fill: '#fff'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: chartData,
          label: { show: false },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#0a0e27',
            borderWidth: 2
          }
        }]
      }

      chart.setOption(option)
    },
    // 事件诉讼情况占比环形图
    initLitigationChart(data) {
      if (!this.$refs.litigationChart) return

      const chart = echarts.init(this.$refs.litigationChart)
      this.charts.litigation = chart

      const chartData = data || [
        { name: '进入诉讼', value: 42, itemStyle: { color: '#ff6b6b' } },
        { name: '未进入诉讼', value: 400, itemStyle: { color: '#00d4ff' } }
      ]

      const total = chartData.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((chartData[0].value / total) * 100).toFixed(0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        graphic: [{
          type: 'text',
          left: 'center',
          top: '40%',
          style: {
            text: `${percentage}%`,
            fontSize: 24,
            fontWeight: 'bold',
            fill: '#ff6b6b'
          }
        }, {
          type: 'text',
          left: 'center',
          top: '55%',
          style: {
            text: '进入诉讼',
            fontSize: 14,
            fill: '#fff'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: chartData,
          label: { show: false },
          itemStyle: {
            borderRadius: 10,
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
    border-bottom: 2px solid rgba(0, 212, 255, 0.3);
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

  .screen-body {
    flex: 1;
    display: flex;
    padding: 20px;
    gap: 20px;
    overflow: hidden;

    .left-area,
    .center-area,
    .right-area {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .left-area {
      flex: 1;
    }

    .center-area {
      flex: 1;
    }

    .right-area {
      flex: 1;
    }

    .panel-box {
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 15px;

      &.flex-1 {
        flex: 1;
        min-height: 0;
      }

      .panel-title {
        font-size: 16px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 15px;
        padding-left: 10px;
        border-left: 3px solid #00d4ff;
      }

      .chart-container {
        height: 100%;
        min-height: 200px;

        &.small {
          min-height: 150px;
        }

        &.large {
          min-height: 350px;
        }
      }

      .table-container {
        max-height: 300px;
        overflow-y: auto;

        &.small {
          max-height: 180px;
        }

        &.scrollable {
          max-height: 400px;
        }

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

          &.amount-cell {
            color: #ff6b6b;
            font-weight: bold;
            font-size: 13px;
          }
        }

        tbody tr {
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 212, 255, 0.1);
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
        padding: 15px 0;

        .stat-row {
          display: flex;
          gap: 20px;
          margin-bottom: 20px;

          &:last-child {
            margin-bottom: 0;
          }
        }

        .stat-item-inline {
          flex: 1;
          text-align: center;
          padding: 15px;
          background: rgba(0, 212, 255, 0.05);
          border-radius: 8px;
          border: 1px solid rgba(0, 212, 255, 0.2);

          .stat-label {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.7);
            margin-bottom: 10px;
          }

          .stat-value {
            font-size: 28px;
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
              font-size: 14px;
              margin-left: 5px;
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

