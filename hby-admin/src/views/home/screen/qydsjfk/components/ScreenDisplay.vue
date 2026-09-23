<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">当前时间: {{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">法律案件穿透</div>
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

    <!-- 大屏主体内容 - 3行3列布局 -->
    <div class="screen-body">
      <!-- 法律案件模块 -->
      <div class="module-content">
        <!-- 第一行 -->
        <div class="chart-row">
          <div class="chart-box">
            <div class="chart-title">未结案件数量趋势分布</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="unsettledTrendChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">新增/存量案件数量</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="newStockCompareChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">办结案件数及挽回损失金额</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="settledRecoveryChart" class="chart-content"></div>
          </div>
        </div>

        <!-- 第二行 -->
        <div class="chart-row">
          <div class="chart-box">
            <div class="chart-title">未结案件数量及金额</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="unsettledAmountChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">未结重点案件</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="unsettledKeyChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">未结案件数量</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="unsettledCountChart" class="chart-content"></div>
          </div>
        </div>

        <!-- 第三行 -->
        <div class="chart-row">
          <div class="chart-box">
            <div class="chart-title">诉讼角色</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="litigationRoleChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">涉外案件</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="foreignCasesChart" class="chart-content"></div>
          </div>
          <div class="chart-box">
            <div class="chart-title">案件标的金额分布</div>
            <div class="chart-subtitle">截止时间至今共计数据: 2018-6-12 12:00:00</div>
            <div ref="amountDistributionChart" class="chart-content"></div>
          </div>
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
      charts: {}
    }
  },
  mounted() {
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.$nextTick(() => {
      this.initAllCharts()
    })
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
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
    handleClose() {
      this.$emit('close')
    },
    initAllCharts() {
      this.initCurrentModuleCharts()
    },
    initCurrentModuleCharts() {
      // 初始化企业数据监控模块的图表
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
          data: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 },
          splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
        },
        series: [{
          name: '未结案件数量',
          type: 'line',
          smooth: true,
          data: [800, 1500, 2200, 2800, 3200, 3800, 2500, 1800, 1200, 800, 400],
          lineStyle: { color: '#00d4ff', width: 2 },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
              { offset: 1, color: 'rgba(0, 212, 255, 0.05)' }
            ])
          },
          itemStyle: { color: '#00d4ff' }
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
        legend: {
          data: ['新增案件数', '存量案件数'],
          top: '5%',
          textStyle: { color: '#fff' }
        },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 }
        },
        yAxis: {
          type: 'value',
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 },
          splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
        },
        series: [
          {
            name: '新增案件数',
            type: 'bar',
            data: [400, 600, 900, 1200, 1500, 1700, 1200, 800, 600, 500, 300],
            itemStyle: { color: '#ffd700' }
          },
          {
            name: '存量案件数',
            type: 'bar',
            data: [400, 900, 1300, 1600, 1700, 2100, 1300, 1000, 600, 300, 100],
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
        legend: {
          data: ['办结案件数', '挽回损失金额'],
          top: '5%',
          textStyle: { color: '#fff' }
        },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['2016/1', '2016/7', '2017/1', '2017/7', '2018/1', '2018/7', '2019/1', '2019/7', '2019/10', '2019/11', '2019/12'],
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10 }
        },
        yAxis: [
          {
            type: 'value',
            name: '件数',
            axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
            axisLabel: { color: '#fff', fontSize: 10 },
            splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
          },
          {
            type: 'value',
            name: '金额(万元)',
            axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
            axisLabel: { color: '#fff', fontSize: 10 },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '办结案件数',
            type: 'bar',
            data: [40, 60, 80, 100, 120, 140, 160, 180, 200, 180, 160],
            itemStyle: { color: '#00d4ff' }
          },
          {
            name: '挽回损失金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: [200, 250, 300, 280, 320, 290, 310, 270, 260, 240, 220],
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
        legend: {
          data: ['未结案件数量', '未结案件金额'],
          top: '5%',
          textStyle: { color: '#fff' }
        },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: ['名词1', '名词2', '名词3', '名词4', '名词5', '名词6', '名词7', '名词8', '名词9'],
          axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
          axisLabel: { color: '#fff', fontSize: 10, rotate: 30 }
        },
        yAxis: [
          {
            type: 'value',
            name: '件数',
            axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
            axisLabel: { color: '#fff', fontSize: 10 },
            splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
          },
          {
            type: 'value',
            name: '金额(万元)',
            axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.3)' } },
            axisLabel: { color: '#fff', fontSize: 10 },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '未结案件数量',
            type: 'bar',
            data: [100, 150, 200, 180, 220, 190, 210, 170, 160],
            itemStyle: { color: '#00d4ff' }
          },
          {
            name: '未结案件金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: [240, 260, 280, 300, 320, 340, 360, 380, 400],
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
          data: ['名词1', '名词2', '名词3', '名词4', '名词5', '名词6', '名词7', '名词8', '名词9'],
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
          name: '未结重点案件',
          type: 'bar',
          data: [100, 200, 300, 400, 500, 400, 300, 200, 100],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}',
            color: '#fff',
            fontSize: 10
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
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff' }
        },
        series: [{
          name: '未结案件',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 398, name: '398件', itemStyle: { color: '#00d4ff' } }
          ],
          label: {
            show: true,
            formatter: '{b}',
            color: '#fff',
            fontSize: 16,
            fontWeight: 'bold'
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
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff' }
        },
        series: [{
          name: '诉讼角色',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 47, name: '原告 47%', itemStyle: { color: '#00d4ff' } },
            { value: 30, name: '被告 30%', itemStyle: { color: '#ffd700' } },
            { value: 23, name: '第三人 23%', itemStyle: { color: '#00ff88' } }
          ],
          label: {
            show: true,
            formatter: '{b}',
            color: '#fff',
            fontSize: 12
          }
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
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff' }
        },
        series: [{
          name: '涉外案件',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 80, name: '涉外案件 80%', itemStyle: { color: '#00d4ff' } },
            { value: 20, name: '非涉外案件 20%', itemStyle: { color: '#ffd700' } }
          ],
          label: {
            show: true,
            formatter: '{b}',
            color: '#fff',
            fontSize: 12
          }
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
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: '10%',
          textStyle: { color: '#fff' }
        },
        series: [{
          name: '案件标的金额',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 320, name: '320-359万', itemStyle: { color: '#00d4ff' } },
            { value: 280, name: '280-319万', itemStyle: { color: '#ffd700' } },
            { value: 240, name: '240-279万', itemStyle: { color: '#00ff88' } },
            { value: 200, name: '<240万', itemStyle: { color: '#ff6b6b' } }
          ],
          label: {
            show: true,
            formatter: '{b}: {d}%',
            color: '#fff'
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
  position: relative;
  overflow: hidden;

  // 大屏头部
  .screen-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 2px solid rgba(0, 212, 255, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #00d4ff, transparent);
    }

    .header-left {
      flex: 1;

      .current-time {
        font-size: 13px;
        color: #00d4ff;
        font-family: 'Courier New', monospace;
        letter-spacing: 1px;
        background: rgba(0, 212, 255, 0.1);
        padding: 5px 12px;
        border-radius: 4px;
        border: 1px solid rgba(0, 212, 255, 0.3);
        display: inline-block;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 28px;
        font-weight: bold;
        background: linear-gradient(90deg, #00d4ff, #00ff88);
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
      gap: 15px;

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

  // 大屏主体
  .screen-body {
    flex: 1;
    padding: 15px;
    display: flex;
    flex-direction: column;
    gap: 15px;
    overflow-y: auto;

    .module-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .chart-row {
      display: flex;
      gap: 15px;
      flex: 1;

      .chart-box {
        flex: 1;
        background: rgba(26, 31, 58, 0.6);
        border: 1px solid rgba(0, 212, 255, 0.3);
        border-radius: 8px;
        padding: 12px;
        backdrop-filter: blur(10px);
        display: flex;
        flex-direction: column;

        .chart-title {
          font-size: 14px;
          font-weight: bold;
          color: #00d4ff;
          margin-bottom: 5px;
          padding-left: 10px;
          border-left: 3px solid #00d4ff;
        }

        .chart-subtitle {
          font-size: 11px;
          color: rgba(255, 255, 255, 0.5);
          margin-bottom: 10px;
          padding-left: 10px;
        }

        .chart-content {
          flex: 1;
          min-height: 200px;
        }
      }
    }

    .placeholder-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: rgba(255, 255, 255, 0.5);

      i {
        font-size: 80px;
        margin-bottom: 20px;
        color: rgba(0, 212, 255, 0.3);
      }

      p {
        font-size: 18px;
        margin: 0;
      }
    }
  }
}
</style>

