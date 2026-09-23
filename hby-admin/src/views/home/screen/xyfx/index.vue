<template>
  <div class="xyfx-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="primary" icon="el-icon-full-screen" @click="openFullScreen">大屏</el-button>
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <i class="el-icon-office-building"></i>
          </div>
          <div class="card-content">
            <div class="card-title">集团公司总数</div>
            <div class="card-value">89<span class="unit">家</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 5.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="card-content">
            <div class="card-title">风险单位数</div>
            <div class="card-value">340<span class="unit">家</span></div>
            <div class="card-trend down">
              <i class="el-icon-bottom"></i> 2.8%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <i class="el-icon-data-analysis"></i>
          </div>
          <div class="card-content">
            <div class="card-title">信用评级AAA</div>
            <div class="card-value">470<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 8.1%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
            <i class="el-icon-connection"></i>
          </div>
          <div class="card-content">
            <div class="card-title">关联企业数</div>
            <div class="card-value">230<span class="unit">家</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 3.5%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第一行：集团公司信用排名 + 集团单位信用画像 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>集团公司信用排名</span>
          </div>
          <el-table :data="creditRankingList" height="300" stripe>
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="company" label="单位名称" min-width="150"></el-table-column>
            <el-table-column prop="level" label="信用级别" width="100"></el-table-column>
            <el-table-column prop="score" label="信用分" width="100"></el-table-column>
            <el-table-column prop="date" label="评定日期" width="150"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>集团单位信用画像</span>
          </div>
          <div ref="creditPortraitChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：二级单位信用评分 + 风险事件预警 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>二级单位信用评分</span>
          </div>
          <div ref="unitCreditScoreChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件预警</span>
          </div>
          <div class="warning-list">
            <div v-for="(item, index) in riskEventWarningList" :key="index" class="warning-item">
              <div class="warning-dot"></div>
              <div class="warning-text">{{ item.company }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：4个环形图 + 1个统计卡片 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件类型</span>
          </div>
          <div ref="riskEventTypeChart" style="height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>信用风险等级</span>
          </div>
          <div ref="creditRiskLevelChart" style="height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>负面信息分布</span>
          </div>
          <div ref="negativeInfoChart" style="height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件数量及金额查询</span>
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
        </el-card>
      </el-col>
    </el-row>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      custom-class="screen-dialog"
    >
      <screen-display v-if="screenVisible" :credit-data="allCreditData" @close="closeFullScreen" />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ScreenDisplay from './components/ScreenDisplay'
import { getCreditRiskData } from './api/creditRisk.js'

export default {
  name: 'CreditRisk',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      allCreditData: null,
      creditRankingList: [],
      riskEventWarningList: [],
      riskEventQuery: {
        eventCount: 0,
        eventAmount: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const data = await getCreditRiskData()
        this.allCreditData = data
        this.creditRankingList = data.creditRankingList || []
        this.riskEventWarningList = data.riskEventWarningList || []
        this.riskEventQuery = data.riskEventQuery || { eventCount: 0, eventAmount: 0 }

        // 数据加载完成后初始化图表
        this.$nextTick(() => {
          this.initCharts()
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts() {
      this.initCreditPortraitChart()
      this.initUnitCreditScoreChart()
      this.initRiskEventTypeChart()
      this.initCreditRiskLevelChart()
      this.initNegativeInfoChart()
    },
    initCreditPortraitChart() {
      if (!this.$refs.creditPortraitChart || !this.allCreditData) return

      const chart = echarts.init(this.$refs.creditPortraitChart)
      const data = this.allCreditData.creditPortraitGraph

      const option = {
        tooltip: {
          formatter: '{b}'
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
            fontSize: 10
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
    initUnitCreditScoreChart() {
      if (!this.$refs.unitCreditScoreChart || !this.allCreditData) return

      const chart = echarts.init(this.$refs.unitCreditScoreChart)
      const data = this.allCreditData.unitCreditScoreChart

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
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
            color: '#666',
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#666'
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
          barWidth: '60%'
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initRiskEventTypeChart() {
      if (!this.$refs.riskEventTypeChart || !this.allCreditData) return

      const chart = echarts.init(this.$refs.riskEventTypeChart)
      const data = this.allCreditData.riskEventTypeStats

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: true,
            formatter: '{b}: {d}%'
          },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa']
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initCreditRiskLevelChart() {
      if (!this.$refs.creditRiskLevelChart || !this.allCreditData) return

      const chart = echarts.init(this.$refs.creditRiskLevelChart)
      const data = this.allCreditData.creditRiskLevelStats

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: true,
            formatter: '{b}: {d}%'
          },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa']
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initNegativeInfoChart() {
      if (!this.$refs.negativeInfoChart || !this.allCreditData) return

      const chart = echarts.init(this.$refs.negativeInfoChart)
      const data = this.allCreditData.negativeInfoStats

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: true,
            formatter: '{b}: {d}%'
          },
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700']
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    }
  },
  beforeDestroy() {
    window.removeEventListener('resize', () => {})
  }
}
</script>

<style lang="scss" scoped>
.xyfx-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .overview-cards {
    .card-item {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      display: flex;
      align-items: center;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      }

      .card-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;

        i {
          font-size: 28px;
          color: #fff;
        }
      }

      .card-content {
        flex: 1;

        .card-title {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }

        .card-value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 5px;

          .unit {
            font-size: 14px;
            font-weight: normal;
            margin-left: 5px;
          }
        }

        .card-trend {
          font-size: 12px;
          color: #909399;

          &.up {
            color: #67c23a;
          }

          &.down {
            color: #f56c6c;
          }

          i {
            margin-right: 3px;
          }
        }
      }
    }
  }

  .warning-list {
    height: 300px;
    overflow-y: auto;

    .warning-item {
      display: flex;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #ebeef5;

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
        font-size: 14px;
        color: #303133;
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

  .query-stats {
    height: 250px;
    display: flex;
    flex-direction: column;
    gap: 15px;
    padding: 20px 0;

    .query-item {
      flex: 1;
      text-align: center;
      padding: 20px;
      border-radius: 8px;
      background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 212, 255, 0.05) 100%);
      border: 1px solid rgba(0, 212, 255, 0.3);

      .query-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .query-value {
        font-size: 32px;
        font-weight: bold;
        color: #00d4ff;

        .unit {
          font-size: 16px;
          margin-left: 5px;
          color: #00ff88;
        }
      }
    }
  }
}
</style>

<style lang="scss">
.screen-dialog {
  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    padding: 0;
    height: 100vh;
    overflow: hidden;
  }
}
</style>

