<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">当前时间：{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">信用风险穿透</div>
      </div>
      <div class="header-right">
        <div class="year-selector">
          <el-button size="mini" :type="selectedYear === '2018' ? 'primary' : ''" @click="selectedYear = '2018'">2018</el-button>
          <el-button size="mini" :type="selectedYear === '2017' ? 'primary' : ''" @click="selectedYear = '2017'">2017</el-button>
          <el-button size="mini" :type="selectedYear === '2016' ? 'primary' : ''" @click="selectedYear = '2016'">2016</el-button>
        </div>
        <el-button type="danger" icon="el-icon-close" circle size="small" @click="handleClose" class="close-btn"></el-button>
      </div>
    </div>

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧区域 -->
      <div class="left-area">
        <!-- 集团公司信用排名 -->
        <div class="panel-box table-panel">
          <div class="panel-title">
            <span>集团公司信用排名</span>
            <span class="subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</span>
          </div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>单位名称</th>
                  <th>信用级别</th>
                  <th>信用分</th>
                  <th>评定日期</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in creditRankingList" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.company }}</td>
                  <td>{{ item.level }}</td>
                  <td>{{ item.score }}</td>
                  <td>{{ item.date }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 二级单位信用评分 -->
        <div class="panel-box flex-1">
          <div class="panel-title">
            <span>二级单位信用评分</span>
            <div class="filter-group">
              <span>单位名称：</span>
              <el-select v-model="selectedUnit" size="mini" style="width: 150px;">
                <el-option label="全部" value="all"></el-option>
              </el-select>
              <span style="margin-left: 10px;">搜索：</span>
              <el-input v-model="searchUnit" size="mini" style="width: 150px;" placeholder="输入企业名称..."></el-input>
            </div>
          </div>
          <div ref="unitCreditScoreChart" class="chart-container"></div>
        </div>

        <!-- 风险事件类型 -->
        <div class="panel-box">
          <div class="panel-title">
            <span>风险事件类型</span>
            <span class="subtitle">按信用等级统计一个单位一个风险一四项指标 2018-6-12 12:00:00</span>
          </div>
          <div ref="riskEventTypeChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in riskEventTypeList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-area">
        <!-- 集团单位信用画像 -->
        <div class="panel-box flex-1">
          <div class="panel-title">
            <span>集团单位信用画像</span>
            <div class="filter-group">
              <span>单位名称：</span>
              <el-select v-model="selectedPortrait" size="mini" style="width: 150px;">
                <el-option label="全部" value="all"></el-option>
              </el-select>
              <span style="margin-left: 10px;">搜索：</span>
              <el-input v-model="searchPortrait" size="mini" style="width: 150px;" placeholder="输入企业名称..."></el-input>
            </div>
          </div>
          <div ref="creditPortraitChart" class="chart-container"></div>
          <div class="graph-subtitle">企业数量工商数据统计-单位信用评级-企业关联 2018-6-12 12:00:00</div>
        </div>

        <!-- 信用风险等级 -->
        <div class="panel-box">
          <div class="panel-title">
            <span>信用风险等级</span>
            <span class="subtitle">按风险类型统计一个单位一个风险一四项指标 2018-6-12 12:00:00</span>
          </div>
          <div ref="creditRiskLevelChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in creditRiskLevelList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area">
        <!-- 风险事件预警 -->
        <div class="panel-box">
          <div class="panel-title">
            <span>风险事件预警</span>
            <span class="subtitle">企业单位数量统计 2018-6-12 12:00:00</span>
          </div>
          <div class="warning-list">
            <div v-for="(item, index) in riskEventWarningList" :key="index" class="warning-item">
              <div class="warning-dot"></div>
              <div class="warning-text">{{ item.company }}</div>
            </div>
          </div>
        </div>

        <!-- 负面信息分布 -->
        <div class="panel-box">
          <div class="panel-title">
            <span>负面信息分布</span>
            <span class="subtitle">按风险类型统计一个单位一个风险一四项指标 2018-6-12 12:00:00</span>
          </div>
          <div ref="negativeInfoChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in negativeInfoList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 风险事件数量及金额查询 -->
        <div class="panel-box query-panel">
          <div class="panel-title">
            <span>风险事件数量及金额查询</span>
            <span class="subtitle">企业单位数量统计 2018-6-12 12:00:00</span>
          </div>
          <div class="query-stats">
            <div class="query-item">
              <div class="query-label">风险事件数量</div>
              <div class="query-value">{{ riskEventQuery.eventCount }}<span class="unit">件</span></div>
            </div>
            <div class="query-item">
              <div class="query-label">风险事件金额</div>
              <div class="query-value">{{ riskEventQuery.eventAmount }}<span class="unit">亿元</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getCreditRiskData } from '../api/creditRisk'

export default {
  name: 'ScreenDisplay',
  props: {
    creditData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      currentTime: '',
      timer: null,
      selectedYear: '2018',
      selectedUnit: 'all',
      searchUnit: '',
      selectedPortrait: 'all',
      searchPortrait: '',
      creditRankingList: [],
      riskEventWarningList: [],
      riskEventTypeList: [],
      creditRiskLevelList: [],
      negativeInfoList: [],
      riskEventQuery: {
        eventCount: 0,
        eventAmount: 0
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
        const data = this.creditData || await getCreditRiskData()
        this.creditRankingList = data.creditRankingList || []
        this.riskEventWarningList = data.riskEventWarningList || []
        this.riskEventTypeList = data.riskEventTypeStats || []
        this.creditRiskLevelList = data.creditRiskLevelStats || []
        this.negativeInfoList = data.negativeInfoStats || []
        this.riskEventQuery = data.riskEventQuery || { eventCount: 0, eventAmount: 0 }

        this.$nextTick(() => {
          this.initCharts(data)
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts(data) {
      this.initUnitCreditScoreChart(data.unitCreditScoreChart)
      this.initRiskEventTypeChart(data.riskEventTypeStats)
      this.initCreditPortraitChart(data.creditPortraitGraph)
      this.initCreditRiskLevelChart(data.creditRiskLevelStats)
      this.initNegativeInfoChart(data.negativeInfoStats)
    },
    handleClose() {
      this.$emit('close')
    },
    // 初始化二级单位信用评分图表
    initUnitCreditScoreChart(data) {
      if (!this.$refs.unitCreditScoreChart || !data) return

      const chart = echarts.init(this.$refs.unitCreditScoreChart)
      this.charts.unitCreditScore = chart

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.categories,
          axisLabel: {
            color: '#00d4ff',
            fontSize: 10,
            interval: 0,
            rotate: 45
          },
          axisLine: {
            lineStyle: { color: '#00d4ff' }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#00d4ff'
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(0, 212, 255, 0.2)'
            }
          }
        },
        series: [{
          type: 'bar',
          data: data.data,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00d4ff' },
              { offset: 1, color: '#00ff88' }
            ])
          },
          barWidth: '60%',
          emphasis: {
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#ffd700' },
                { offset: 1, color: '#ff9800' }
              ])
            }
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化风险事件类型图表
    initRiskEventTypeChart(data) {
      if (!this.$refs.riskEventTypeChart || !data) return

      const chart = echarts.init(this.$refs.riskEventTypeChart)
      this.charts.riskEventType = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa'],
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }, {
          type: 'pie',
          radius: ['0%', '40%'],
          center: ['50%', '50%'],
          label: {
            show: true,
            position: 'center',
            formatter: '50%',
            fontSize: 32,
            fontWeight: 'bold',
            color: '#00d4ff'
          },
          labelLine: {
            show: false
          },
          data: [{ value: 1, itemStyle: { color: 'transparent' } }],
          silent: true
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化集团单位信用画像图表
    initCreditPortraitChart(data) {
      if (!this.$refs.creditPortraitChart || !data) return

      const chart = echarts.init(this.$refs.creditPortraitChart)
      this.charts.creditPortrait = chart

      const option = {
        tooltip: {
          formatter: '{b}',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        series: [{
          type: 'graph',
          layout: 'force',
          data: data.nodes,
          links: data.links,
          categories: data.categories,
          roam: true,
          label: {
            show: true,
            position: 'right',
            fontSize: 10,
            color: '#fff'
          },
          labelLayout: {
            hideOverlap: true
          },
          lineStyle: {
            color: 'source',
            curveness: 0.3,
            width: 2
          },
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 1
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 4
            }
          },
          force: {
            repulsion: 100,
            edgeLength: [50, 100]
          },
          color: ['#ff6b6b', '#ffd700', '#00ff88', '#00d4ff']
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化信用风险等级图表
    initCreditRiskLevelChart(data) {
      if (!this.$refs.creditRiskLevelChart || !data) return

      const chart = echarts.init(this.$refs.creditRiskLevelChart)
      this.charts.creditRiskLevel = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa'],
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }, {
          type: 'pie',
          radius: ['0%', '40%'],
          center: ['50%', '50%'],
          label: {
            show: true,
            position: 'center',
            formatter: '40%',
            fontSize: 32,
            fontWeight: 'bold',
            color: '#00d4ff'
          },
          labelLine: {
            show: false
          },
          data: [{ value: 1, itemStyle: { color: 'transparent' } }],
          silent: true
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化负面信息分布图表
    initNegativeInfoChart(data) {
      if (!this.$refs.negativeInfoChart || !data) return

      const chart = echarts.init(this.$refs.negativeInfoChart)
      this.charts.negativeInfo = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700'],
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }, {
          type: 'pie',
          radius: ['0%', '40%'],
          center: ['50%', '50%'],
          label: {
            show: true,
            position: 'center',
            formatter: '30%',
            fontSize: 32,
            fontWeight: 'bold',
            color: '#00d4ff'
          },
          labelLine: {
            show: false
          },
          data: [{ value: 1, itemStyle: { color: 'transparent' } }],
          silent: true
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
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
    padding: 10px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .header-left {
      flex: 1;

      .current-time {
        font-size: 14px;
        color: #00d4ff;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 36px;
        font-weight: bold;
        background: linear-gradient(90deg, #00d4ff 0%, #00ff88 50%, #ffd700 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
      }
    }

    .header-right {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      gap: 15px;

      .year-selector {
        display: flex;
        gap: 5px;
      }

      .close-btn {
        background: #ff6b6b;
        border-color: #ff6b6b;

        &:hover {
          background: #ff5252;
          border-color: #ff5252;
        }
      }
    }
  }

  .screen-body {
    flex: 1;
    display: flex;
    gap: 15px;
    padding: 0 20px 20px;
    overflow: hidden;

    .left-area,
    .center-area,
    .right-area {
      display: flex;
      flex-direction: column;
      gap: 15px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 212, 255, 0.3);
        border-radius: 3px;

        &:hover {
          background: rgba(0, 212, 255, 0.5);
        }
      }
    }

    .left-area {
      flex: 1.2;
    }

    .center-area {
      flex: 1;
    }

    .right-area {
      flex: 1;
    }

    .panel-box {
      background: rgba(10, 30, 60, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 15px;
      backdrop-filter: blur(10px);

      &.flex-1 {
        flex: 1;
        display: flex;
        flex-direction: column;
      }

      .panel-title {
        font-size: 16px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid rgba(0, 212, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: space-between;

        .subtitle {
          font-size: 12px;
          font-weight: normal;
          color: #00ff88;
        }

        .filter-group {
          font-size: 12px;
          font-weight: normal;
          color: #00d4ff;
          display: flex;
          align-items: center;
        }
      }

      .chart-container {
        flex: 1;
        min-height: 200px;
      }
    }

    // 表格样式
    .table-panel {
      height: 300px;

      .table-container {
        height: calc(100% - 45px);
        overflow-y: auto;

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

        thead {
          position: sticky;
          top: 0;
          background: rgba(0, 212, 255, 0.1);
          z-index: 1;

          th {
            padding: 10px;
            font-size: 12px;
            color: #00d4ff;
            text-align: left;
            border-bottom: 1px solid rgba(0, 212, 255, 0.3);
          }
        }

        tbody {
          tr {
            transition: all 0.3s;

            &:hover {
              background: rgba(0, 212, 255, 0.1);
            }

            td {
              padding: 8px 10px;
              font-size: 12px;
              color: #fff;
              border-bottom: 1px solid rgba(0, 212, 255, 0.1);
            }
          }
        }
      }
    }

    // 图例列表
    .legend-list {
      margin-top: 15px;

      .legend-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px 0;
        font-size: 12px;
        color: #fff;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .legend-label {
          flex: 1;
          color: #00d4ff;
        }

        .legend-percent {
          flex: 0 0 60px;
          text-align: right;
          color: #ffd700;
        }

        .legend-value {
          flex: 0 0 60px;
          text-align: right;
          color: #00ff88;
        }
      }
    }

    // 查询面板样式
    .query-panel {
      .query-stats {
        display: flex;
        gap: 15px;
        padding: 20px 0;

        .query-item {
          flex: 1;
          text-align: center;
          padding: 20px;
          border-radius: 8px;
          background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(0, 212, 255, 0.05) 100%);
          border: 1px solid rgba(0, 212, 255, 0.3);

          .query-label {
            font-size: 14px;
            color: #00d4ff;
            margin-bottom: 10px;
          }

          .query-value {
            font-size: 32px;
            font-weight: bold;
            color: #ffd700;

            .unit {
              font-size: 16px;
              margin-left: 5px;
              color: #00ff88;
            }
          }
        }
      }
    }

    // 预警列表样式
    .warning-list {
      .warning-item {
        display: flex;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .warning-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background: #00d4ff;
          margin-right: 10px;
          animation: pulse 2s infinite;
        }

        .warning-text {
          flex: 1;
          font-size: 12px;
          color: #fff;
        }

        @keyframes pulse {
          0%, 100% {
            opacity: 1;
            transform: scale(1);
          }
          50% {
            opacity: 0.5;
            transform: scale(1.2);
          }
        }
      }
    }

    // 统计汇总样式
    .stats-summary {
      margin-top: 15px;
      display: flex;
      gap: 10px;

      .stats-item {
        flex: 1;
        padding: 15px;
        border-radius: 8px;
        text-align: center;

        &.blue {
          background: linear-gradient(135deg, rgba(0, 212, 255, 0.3) 0%, rgba(0, 212, 255, 0.1) 100%);
          border: 1px solid rgba(0, 212, 255, 0.5);
        }

        &.green {
          background: linear-gradient(135deg, rgba(0, 255, 136, 0.3) 0%, rgba(0, 255, 136, 0.1) 100%);
          border: 1px solid rgba(0, 255, 136, 0.5);
        }

        .stats-label {
          font-size: 12px;
          color: #fff;
          margin-bottom: 5px;
        }

        .stats-percent {
          font-size: 24px;
          font-weight: bold;
          color: #00d4ff;
          margin-bottom: 5px;
        }

        .stats-count {
          font-size: 14px;
          color: #00ff88;
        }
      }
    }

    // 图谱副标题
    .graph-subtitle {
      margin-top: 10px;
      font-size: 12px;
      color: #00ff88;
      text-align: center;
    }
  }
}
</style>

