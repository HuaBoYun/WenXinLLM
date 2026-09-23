<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">当前时间：{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">审计整改跟踪</div>
      </div>
      <div class="header-right">
        <div class="year-selector">
          <el-button size="mini" :type="selectedYear === '2018' ? 'primary' : ''" @click="selectedYear = '2018'">2018</el-button>
          <el-button size="mini" :type="selectedYear === '2017' ? 'primary' : ''" @click="selectedYear = '2017'">2017</el-button>
          <el-button size="mini" :type="selectedYear === '2016' ? 'primary' : ''" @click="selectedYear = '2016'">2016</el-button>
        </div>
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
      <!-- 上半部分：表格 -->
      <div class="top-section">
        <div class="panel-box table-panel">
          <div class="panel-title">
            <span>审计问题整改迟缓事项</span>
            <span class="subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</span>
          </div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>审计项目</th>
                  <th>审计类型</th>
                  <th>计划完成时间</th>
                  <th>审批类型</th>
                  <th>责任人</th>
                  <th>责任部门</th>
                  <th>问题金额（亿元）</th>
                  <th>问题性质</th>
                  <th>问题描述</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in delayedItems" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.projectName }}</td>
                  <td>{{ item.auditType }}</td>
                  <td>{{ item.date }}</td>
                  <td>{{ item.type }}</td>
                  <td>{{ item.person }}</td>
                  <td>{{ item.dept }}</td>
                  <td class="amount-cell">{{ item.amount }}</td>
                  <td>{{ item.nature }}</td>
                  <td class="remark-cell">{{ item.remark }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 中间部分：4个环形图 -->
      <div class="middle-section">
        <!-- 审计项目数 -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <span>审计项目数</span>
            <span class="subtitle">成员部门审计事项数 2018-6-12 12:00:00</span>
          </div>
          <div ref="auditProjectChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in auditProjectList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 审计覆盖情况 -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <span>审计覆盖情况</span>
            <span class="subtitle">成员部门审计事项数 2018-6-12 12:00:00</span>
          </div>
          <div ref="auditCoverageChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in auditCoverageList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 审计项目计划完成情况 -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <span>审计项目计划完成情况</span>
            <span class="subtitle">成员部门审计事项数 2018-6-12 12:00:00</span>
          </div>
          <div ref="projectCompletionChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in projectCompletionList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 高风险评估事项 -->
        <div class="panel-box chart-panel">
          <div class="panel-title">
            <span>高风险评估事项</span>
            <span class="subtitle">成员部门审计事项数 2018-6-12 12:00:00</span>
          </div>
          <div ref="highRiskChart" class="chart-container"></div>
          <div class="legend-list">
            <div v-for="item in highRiskList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 下半部分：趋势图 -->
      <div class="bottom-section">
        <div class="panel-box trend-panel">
          <div class="panel-title">
            <span>集团审计问题数量变化趋势</span>
            <div class="filter-group">
              <span>审计部门：</span>
              <el-select v-model="selectedDept" size="mini" style="width: 150px;">
                <el-option label="全部" value="all"></el-option>
              </el-select>
              <span style="margin-left: 10px;">搜索：</span>
              <el-button size="mini" icon="el-icon-search"></el-button>
            </div>
          </div>
          <div ref="problemTrendChart" class="chart-container large"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getAuditProblemData } from '../api/auditProblem'

export default {
  name: 'ScreenDisplay',
  props: {
    auditData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      currentTime: '',
      timer: null,
      selectedYear: '2018',
      selectedDept: 'all',
      delayedItems: [],
      auditProjectList: [
        { name: '委托1', percent: 26, value: 26 },
        { name: '委托2', percent: 18, value: 18 },
        { name: '委托3', percent: 16, value: 16 },
        { name: '委托4', percent: 30, value: 30 },
        { name: '委托5', percent: 15, value: 15 }
      ],
      auditCoverageList: [
        { name: '已覆盖', percent: 60, value: 600 },
        { name: '未覆盖', percent: 40, value: 400 }
      ],
      projectCompletionList: [
        { name: '已完成', percent: 60, value: 60 },
        { name: '进行中', percent: 25, value: 25 },
        { name: '未开始', percent: 15, value: 15 }
      ],
      highRiskList: [
        { name: '委托1', percent: 32, value: 32 },
        { name: '委托2', percent: 12, value: 12 },
        { name: '委托3', percent: 5, value: 5 },
        { name: '其他', percent: 15, value: 15 }
      ],
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
        const data = this.auditData || await getAuditProblemData()
        this.delayedItems = data.delayedItems || []

        this.$nextTick(() => {
          this.initCharts(data)
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts(data) {
      this.initAuditProjectChart(data.auditProjectStats)
      this.initAuditCoverageChart(data.auditCoverageStats)
      this.initProjectCompletionChart(data.projectCompletionStats)
      this.initHighRiskChart(data.highRiskStats)
      this.initProblemTrendChart(data.problemTrendChart)
    },
    handleClose() {
      this.$emit('close')
    },
    // 审计项目数环形图
    initAuditProjectChart(data) {
      if (!this.$refs.auditProjectChart || !data) return

      const chart = echarts.init(this.$refs.auditProjectChart)
      this.charts.auditProject = chart

      const total = data.reduce((sum, item) => sum + item.value, 0)
      const mainItem = data[0]
      const percentage = ((mainItem.value / total) * 100).toFixed(0)

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
            fontSize: 28,
            fontWeight: 'bold',
            fill: '#00d4ff'
          }
        }],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa']
        }]
      }

      chart.setOption(option)
    },
    // 审计覆盖情况环形图
    initAuditCoverageChart(data) {
      if (!this.$refs.auditCoverageChart) return

      const chart = echarts.init(this.$refs.auditCoverageChart)
      this.charts.auditCoverage = chart

      const chartData = data || this.auditCoverageList
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
            fontSize: 28,
            fontWeight: 'bold',
            fill: '#ffd700'
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
          },
          color: ['#ffd700', '#00ff88']
        }]
      }

      chart.setOption(option)
    },
    // 审计项目计划完成情况环形图
    initProjectCompletionChart(data) {
      if (!this.$refs.projectCompletionChart) return

      const chart = echarts.init(this.$refs.projectCompletionChart)
      this.charts.projectCompletion = chart

      const chartData = data || this.projectCompletionList
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
            fontSize: 28,
            fontWeight: 'bold',
            fill: '#00ff88'
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
          },
          color: ['#00ff88', '#ffd700', '#ff9800']
        }]
      }

      chart.setOption(option)
    },
    // 高风险评估事项环形图
    initHighRiskChart(data) {
      if (!this.$refs.highRiskChart) return

      const chart = echarts.init(this.$refs.highRiskChart)
      this.charts.highRisk = chart

      const chartData = data || this.highRiskList
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
            fontSize: 28,
            fontWeight: 'bold',
            fill: '#ff9800'
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
          },
          color: ['#ff9800', '#00d4ff', '#ffd700', '#a78bfa']
        }]
      }

      chart.setOption(option)
    },
    // 集团审计问题数量变化趋势折线图
    initProblemTrendChart(chartData) {
      if (!this.$refs.problemTrendChart) return

      const chart = echarts.init(this.$refs.problemTrendChart)
      this.charts.problemTrend = chart

      const data = chartData || {
        categories: ['2018/2', '2018/3', '2018/4', '2018/5', '2018/6', '2018/7', '2018/8', '2018/9', '2018/10', '2018/11', '2018/12', '2019/1'],
        data: [
          { name: '审计问题数量', values: [2000, 4500, 3500, 5000, 4000, 3000, 4500, 3800, 4200, 3500, 4000, 3200] }
        ]
      }

      const option = {
        tooltip: {
          trigger: 'axis'
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
            color: '#fff',
            fontSize: 11
          },
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } }
        },
        yAxis: {
          type: 'value',
          name: '(件)',
          nameTextStyle: { color: '#fff' },
          axisLabel: { color: '#fff', fontSize: 11 },
          axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
          splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
        },
        series: [{
          type: 'line',
          data: data.data[0].values,
          smooth: true,
          itemStyle: {
            color: '#00d4ff'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(0, 212, 255, 0.4)' },
              { offset: 1, color: 'rgba(0, 212, 255, 0.05)' }
            ])
          }
        }]
      }

      chart.setOption(option)
    },

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
      align-items: center;
      gap: 10px;

      .year-selector {
        display: flex;
        gap: 5px;
      }
    }
  }

  .screen-body {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 20px;
    gap: 15px;
    overflow: hidden;

    .top-section {
      flex: 0 0 35%;
      display: flex;
    }

    .middle-section {
      flex: 0 0 30%;
      display: flex;
      gap: 15px;
    }

    .bottom-section {
      flex: 1;
      display: flex;
    }

    .panel-box {
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 15px;
      display: flex;
      flex-direction: column;

      &.table-panel {
        flex: 1;
      }

      &.chart-panel {
        flex: 1;
      }

      &.trend-panel {
        flex: 1;
      }

      .panel-title {
        font-size: 14px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 15px;
        padding-left: 10px;
        border-left: 3px solid #00d4ff;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .subtitle {
          font-size: 11px;
          color: rgba(255, 255, 255, 0.5);
          font-weight: normal;
        }

        .filter-group {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.7);
          font-weight: normal;
          display: flex;
          align-items: center;
        }
      }

      .chart-container {
        flex: 1;
        min-height: 0;

        &.large {
          height: 100%;
        }
      }

      .table-container {
        flex: 1;
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
        font-size: 11px;

        th {
          background: rgba(0, 212, 255, 0.2);
          color: #00d4ff;
          padding: 8px 5px;
          text-align: left;
          font-weight: bold;
          border-bottom: 1px solid rgba(0, 212, 255, 0.3);
          white-space: nowrap;
        }

        td {
          padding: 8px 5px;
          border-bottom: 1px solid rgba(255, 255, 255, 0.1);
          color: rgba(255, 255, 255, 0.8);

          &.remark-cell {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          &.amount-cell {
            color: #ffd700;
            font-weight: bold;
          }
        }

        tbody tr {
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 212, 255, 0.1);
          }
        }
      }

      .legend-list {
        margin-top: 10px;
        display: flex;
        flex-direction: column;
        gap: 5px;

        .legend-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 12px;
          padding: 5px 10px;
          background: rgba(0, 212, 255, 0.05);
          border-radius: 4px;

          .legend-label {
            color: rgba(255, 255, 255, 0.7);
          }

          .legend-percent {
            color: #00d4ff;
            font-weight: bold;
          }

          .legend-value {
            color: rgba(255, 255, 255, 0.9);
          }
        }
      }

    }
  }
}
</style>

