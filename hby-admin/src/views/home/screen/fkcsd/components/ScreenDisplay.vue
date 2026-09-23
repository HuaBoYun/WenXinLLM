<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">当前时间: {{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">风控成熟度</div>
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
      <!-- 第一行 -->
      <div class="row-area">
        <!-- 成熟度排名TOP10 -->
        <div class="panel-box">
          <div class="panel-title">成熟度排名TOP10</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th width="50">排名</th>
                  <th>单位名称</th>
                  <th width="80">成熟度评分</th>
                  <th width="80">成熟度等级</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in maturityTop10" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td class="company-name">{{ item.companyName }}</td>
                  <td class="score">{{ item.score }}</td>
                  <td class="level">{{ item.level }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- 各二级单位成熟度评分 -->
        <div class="panel-box">
          <div class="panel-title">各二级单位成熟度评分</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="unitMaturityChart" class="chart-content"></div>
        </div>

        <!-- 成熟度排名等级 -->
        <div class="panel-box">
          <div class="panel-title">成熟度排名等级</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="maturityLevelChart" class="chart-content"></div>
        </div>
      </div>

      <!-- 第二行 -->
      <div class="row-area">
        <!-- 重大缺陷 -->
        <div class="panel-box">
          <div class="panel-title">重大缺陷</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="majorDefectChart" class="chart-content"></div>
        </div>

        <!-- 各单位缺陷数量 -->
        <div class="panel-box">
          <div class="panel-title">各单位缺陷数量</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="unitDefectChart" class="chart-content"></div>
        </div>

        <!-- 缺陷分布情况 -->
        <div class="panel-box">
          <div class="panel-title">缺陷分布情况</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>业务领域名称</th>
                  <th width="60">公司</th>
                  <th width="60">数量</th>
                  <th width="60">数量</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in defectDistribution" :key="index">
                  <td class="domain-name">{{ item.domain }}</td>
                  <td>{{ item.company }}</td>
                  <td>{{ item.count1 }}</td>
                  <td>{{ item.count2 }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 第三行 -->
      <div class="row-area">
        <!-- 缺陷类型分布 -->
        <div class="panel-box">
          <div class="panel-title">缺陷类型分布</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="defectTypeChart" class="chart-content"></div>
        </div>

        <!-- 缺陷重要程度 -->
        <div class="panel-box">
          <div class="panel-title">缺陷重要程度</div>
          <div class="panel-subtitle">截止时间至今共计数据: {{ currentTime }}</div>
          <div ref="defectSeverityChart" class="chart-content"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ScreenDisplay',
  props: {
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentTime: '',
      timeTimer: null,
      charts: {},
      maturityTop10: [],
      defectDistribution: []
    }
  },
  mounted() {
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.initData()
    this.$nextTick(() => {
      this.initAllCharts()
    })
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
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
    initData() {
      // 成熟度排名TOP10
      this.maturityTop10 = [
        { companyName: 'XXXXXX建设集团有限公司', score: 156, level: '成熟级' },
        { companyName: 'XXXXXX有限公司', score: 143, level: '成熟级' },
        { companyName: 'XXXXXXXXXXXXXX控股有限公司', score: 143, level: '成熟级' },
        { companyName: 'XXXXXX汽车集团有限公司', score: 123, level: '成熟级' },
        { companyName: 'XXXXXX集团有限公司', score: 106, level: '成熟级' },
        { companyName: 'XXXXXXXXXXXXXX开发学院', score: 157, level: '成熟级' },
        { companyName: 'XXXXXX集团有限公司', score: 107, level: '成熟级' },
        { companyName: 'XXXXXX汽车集团有限公司', score: 123, level: '成熟级' },
        { companyName: 'XXXXXX集团有限公司', score: 156, level: '成熟级' },
        { companyName: 'XXXXXX江苏集团有限公司', score: 157, level: '成熟级' }
      ]

      // 缺陷分布情况
      this.defectDistribution = [
        { domain: '名词1', company: 35, count1: 15, count2: 5 },
        { domain: '名词1', company: 18, count1: 10, count2: 3 },
        { domain: '名词1', company: 5, count1: 5, count2: 2 },
        { domain: '名词1', company: 30, count1: 30, count2: 8 },
        { domain: '名词1', company: 15, count1: 15, count2: 4 }
      ]
    },
    initAllCharts() {
      this.initMajorDefectChart()
      this.initDefectTypeChart()
      this.initDefectSeverityChart()
      this.initUnitMaturityChart()
      this.initUnitDefectChart()
      this.initMaturityLevelChart()
    },
    initMajorDefectChart() {
      if (!this.$refs.majorDefectChart) return

      const chart = echarts.init(this.$refs.majorDefectChart)
      this.charts.majorDefect = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff', fontSize: 11 }
        },
        series: [{
          name: '重大缺陷',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 400, name: '已提交', itemStyle: { color: '#00d4ff' } },
            { value: 600, name: '未提交', itemStyle: { color: '#ffd700' } }
          ],
          label: {
            show: true,
            formatter: '{b}\n{d}%',
            color: '#fff',
            fontSize: 11
          }
        }]
      }

      chart.setOption(option)
    },
    initDefectTypeChart() {
      if (!this.$refs.defectTypeChart) return

      const chart = echarts.init(this.$refs.defectTypeChart)
      this.charts.defectType = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff', fontSize: 11 }
        },
        series: [{
          name: '缺陷类型',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 32, name: '名词1', itemStyle: { color: '#00d4ff' } },
            { value: 18, name: '名词2', itemStyle: { color: '#ffd700' } },
            { value: 5, name: '名词3', itemStyle: { color: '#00ff88' } },
            { value: 10, name: '名词4', itemStyle: { color: '#ff6b6b' } },
            { value: 15, name: '名词5', itemStyle: { color: '#a78bfa' } }
          ],
          label: {
            show: true,
            formatter: '{b}\n{d}%',
            color: '#fff',
            fontSize: 11
          }
        }]
      }

      chart.setOption(option)
    },
    // 3. 缺陷重要程度
    initDefectSeverityChart() {
      if (!this.$refs.defectSeverityChart) return

      const chart = echarts.init(this.$refs.defectSeverityChart)
      this.charts.defectSeverity = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff', fontSize: 11 }
        },
        series: [{
          name: '缺陷重要程度',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 470, name: '重要缺陷\n47%、470个', itemStyle: { color: '#ff6b6b' } },
            { value: 300, name: '一般缺陷\n30%、300个', itemStyle: { color: '#ffd700' } },
            { value: 230, name: '轻微缺陷\n23%、230个', itemStyle: { color: '#00ff88' } }
          ],
          label: {
            show: true,
            formatter: '{b}',
            color: '#fff',
            fontSize: 11
          }
        }]
      }

      chart.setOption(option)
    },
    // 4. 各二级单位成熟度评分
    initUnitMaturityChart() {
      if (!this.$refs.unitMaturityChart) return

      const chart = echarts.init(this.$refs.unitMaturityChart)
      this.charts.unitMaturity = chart

      const xData = ['单位1', '单位2', '单位3', '单位4', '单位5', '单位6', '单位7', '单位8', '单位9', '单位10',
                     '单位11', '单位12', '单位13', '单位14', '单位15', '单位16', '单位17', '单位18', '单位19', '单位20']
      const yData = [320, 350, 280, 310, 290, 330, 300, 340, 315, 325,
                     305, 335, 295, 320, 310, 330, 300, 315, 325, 310]

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: xData,
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10, rotate: 30 }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 },
          splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
        },
        series: [{
          name: '成熟度评分',
          type: 'bar',
          data: yData,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
          },
          barWidth: '60%'
        }]
      }

      chart.setOption(option)
    },
    // 6. 成熟度排名等级
    initMaturityLevelChart() {
      if (!this.$refs.maturityLevelChart) return

      const chart = echarts.init(this.$refs.maturityLevelChart)
      this.charts.maturityLevel = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff', fontSize: 11 }
        },
        series: [{
          name: '成熟度等级',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 40, name: '初始级\n40%、400个', itemStyle: { color: '#ff6b6b' } },
            { value: 40, name: '规范级\n40%、400个', itemStyle: { color: '#ffd700' } },
            { value: 10, name: '已定义级\n10%、100个', itemStyle: { color: '#00d4ff' } },
            { value: 5, name: '量化管理级\n5%、50个', itemStyle: { color: '#00ff88' } },
            { value: 5, name: '优化级\n5%、50个', itemStyle: { color: '#a78bfa' } }
          ],
          label: {
            show: true,
            formatter: '{b}',
            color: '#fff',
            fontSize: 11
          }
        }]
      }

      chart.setOption(option)
    },
    // 5. 各单位缺陷数量
    initUnitDefectChart() {
      if (!this.$refs.unitDefectChart) return

      const chart = echarts.init(this.$refs.unitDefectChart)
      this.charts.unitDefect = chart

      const xData = ['单位1', '单位2', '单位3', '单位4', '单位5', '单位6', '单位7', '单位8', '单位9', '单位10',
                     '单位11', '单位12', '单位13', '单位14', '单位15', '单位16', '单位17', '单位18', '单位19', '单位20']
      const yData = [350, 400, 300, 380, 320, 390, 310, 420, 360, 370,
                     340, 410, 330, 390, 350, 400, 320, 380, 370, 360]

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '5%', containLabel: true },
        xAxis: {
          type: 'category',
          data: xData,
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10, rotate: 30 }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 },
          splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
        },
        series: [{
          name: '缺陷数量',
          type: 'bar',
          data: yData,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
          },
          barWidth: '60%'
        }]
      }

      chart.setOption(option)
    },
    handleClose() {
      this.$emit('close')
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
  position: relative;
  overflow: hidden;

  .screen-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 1px solid rgba(0, 212, 255, 0.2);

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

      .close-btn {
        background: rgba(245, 108, 108, 0.2);
        border-color: #f56c6c;
        color: #f56c6c;

        &:hover {
          background: rgba(245, 108, 108, 0.4);
        }
      }
    }
  }

  .screen-body {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 15px;
    gap: 15px;
    overflow: hidden;

    .row-area {
      display: flex;
      gap: 15px;
      min-height: 0;

      &:nth-child(1),
      &:nth-child(2) {
        flex: 1.2;
      }

      &:nth-child(3) {
        flex: 1;
      }

      .panel-box {
        flex: 1;
      }
    }

    .panel-box {
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 12px;
      backdrop-filter: blur(10px);
      display: flex;
      flex-direction: column;
      flex: 1;

      .panel-title {
        font-size: 14px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 5px;
        padding-left: 10px;
        border-left: 3px solid #00d4ff;
      }

      .panel-subtitle {
        font-size: 11px;
        color: rgba(255, 255, 255, 0.5);
        margin-bottom: 10px;
        padding-left: 10px;
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

        .data-table {
          width: 100%;
          border-collapse: collapse;

          thead {
            tr {
              background: rgba(0, 212, 255, 0.1);

              th {
                padding: 8px;
                font-size: 12px;
                color: #00d4ff;
                text-align: left;
                border-bottom: 1px solid rgba(0, 212, 255, 0.3);
              }
            }
          }

          tbody {
            tr {
              &:hover {
                background: rgba(0, 212, 255, 0.05);
              }

              td {
                padding: 8px;
                font-size: 11px;
                color: rgba(255, 255, 255, 0.8);
                border-bottom: 1px solid rgba(255, 255, 255, 0.05);

                &.company-name,
                &.domain-name {
                  max-width: 200px;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  white-space: nowrap;
                }

                &.score,
                &.risk-count {
                  color: #ffd700;
                  font-weight: bold;
                }

                &.level {
                  color: #00ff88;
                  font-weight: bold;
                }
              }
            }
          }
        }
      }

      .chart-content {
        flex: 1;
        min-height: 200px;
      }
    }
  }
}
</style>

