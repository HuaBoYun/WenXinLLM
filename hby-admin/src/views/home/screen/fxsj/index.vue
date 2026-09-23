<template>
  <div class="fxsj-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="primary" icon="el-icon-full-screen" @click="openFullScreen">大屏</el-button>
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="card-content">
            <div class="card-title">风险事件总数</div>
            <div class="card-value">761<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 5.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <i class="el-icon-coin"></i>
          </div>
          <div class="card-content">
            <div class="card-title">风险事件金额</div>
            <div class="card-value">25<span class="unit">亿元</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 3.5%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <i class="el-icon-circle-check"></i>
          </div>
          <div class="card-content">
            <div class="card-title">已化解事件</div>
            <div class="card-value">320<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 8.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="card-content">
            <div class="card-title">诉讼事件</div>
            <div class="card-value">42<span class="unit">件</span></div>
            <div class="card-trend down">
              <i class="el-icon-bottom"></i> 1.2%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第一行：表格和趋势图 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件金额排名</span>
          </div>
          <el-table :data="eventAmountRanking" height="300" stripe>
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="company" label="企业名称" min-width="150"></el-table-column>
            <el-table-column prop="eventType" label="事件类型" min-width="120"></el-table-column>
            <el-table-column prop="amount" label="金额(万元)" width="120" align="right">
              <template slot-scope="scope">
                <span style="color: #f56c6c; font-weight: bold;">{{ scope.row.amount }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件数量及金额趋势</span>
          </div>
          <div ref="eventTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：环形图和表格 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险评估数量</span>
          </div>
          <div ref="assessmentCountChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件类型</span>
          </div>
          <div ref="eventTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>高风险评估事项</span>
          </div>
          <el-table :data="highRiskItems" height="300" stripe>
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="company" label="企业名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="item" label="评估事项" width="100"></el-table-column>
            <el-table-column prop="level" label="风险" width="70">
              <template slot-scope="scope">
                <el-tag :type="scope.row.level === '高' ? 'danger' : 'warning'" size="small">
                  {{ scope.row.level }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：化解情况、诉讼情况、查询统计 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件化解情况</span>
          </div>
          <div ref="eventResolutionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>事件诉讼情况占比</span>
          </div>
          <div ref="litigationChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险事件数量及金额查询</span>
          </div>
          <div class="query-stats-card">
            <div class="stat-item">
              <div class="stat-label">风险事件数量</div>
              <div class="stat-value" style="color: #409EFF;">761<span class="unit">件</span></div>
            </div>
            <el-divider></el-divider>
            <div class="stat-item">
              <div class="stat-label">涉及金额</div>
              <div class="stat-value" style="color: #F56C6C;">25<span class="unit">亿元</span></div>
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
      <screen-display v-if="screenVisible" :event-data="allEventData" @close="closeFullScreen" />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getRiskEventData } from './api/riskEvent'

export default {
  name: 'RiskEvent',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      allEventData: null,
      eventAmountRanking: [
        { company: 'xxxx建设控股有限公司', eventType: '合同纠纷', amount: 1000 },
        { company: 'xxxx汽车有限公司', eventType: '债务违约', amount: 900 },
        { company: 'xxxx投资发展有限公司', eventType: '担保诉讼', amount: 800 },
        { company: 'xxxx汽车集团有限公司', eventType: '劳动纠纷', amount: 700 },
        { company: 'xxxx集团有限公司', eventType: '知识产权', amount: 600 },
        { company: 'xxxx建设开发有限公司', eventType: '合同纠纷', amount: 500 },
        { company: 'xxxx集团有限公司', eventType: '债务违约', amount: 400 },
        { company: 'xxxx汽车集团有限公司', eventType: '担保诉讼', amount: 300 }
      ],
      highRiskItems: [
        { company: 'xxxx建设控股有限公司', item: '重大投资决策', level: '高' },
        { company: 'xxxx汽车有限公司', item: '对外担保事项', level: '高' },
        { company: 'xxxx投资发展有限公司', item: '资产处置', level: '高' },
        { company: 'xxxx汽车集团有限公司', item: '重大合同签订', level: '中' },
        { company: 'xxxx集团有限公司', item: '关联交易', level: '高' },
        { company: 'xxxx建设开发有限公司', item: '融资决策', level: '中' }
      ]
    }
  },
  mounted() {
    this.initCharts()
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const data = await getRiskEventData()
        this.allEventData = data
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts() {
      this.initEventTrendChart()
      this.initAssessmentCountChart()
      this.initEventTypeChart()
      this.initEventResolutionChart()
      this.initLitigationChart()
    },
    initEventTrendChart() {
      if (!this.$refs.eventTrendChart) return

      const chart = echarts.init(this.$refs.eventTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['事件数量', '涉及金额'],
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
          data: ['2018/1', '2018/2', '2018/3', '2018/4', '2018/5', '2018/6'],
          axisLabel: {
            color: '#666',
            rotate: 30
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(件)',
            position: 'left',
            axisLabel: {
              color: '#666'
            }
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right',
            axisLabel: {
              color: '#666'
            }
          }
        ],
        series: [
          {
            name: '事件数量',
            type: 'bar',
            data: [120, 150, 130, 160, 140, 180],
            itemStyle: {
              color: '#00d4ff'
            }
          },
          {
            name: '涉及金额',
            type: 'line',
            yAxisIndex: 1,
            data: [3000, 3500, 3200, 4000, 3800, 4500],
            itemStyle: {
              color: '#ffd700'
            },
            smooth: true
          }
        ]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 风险评估数量环形图
    initAssessmentCountChart() {
      if (!this.$refs.assessmentCountChart) return

      const chart = echarts.init(this.$refs.assessmentCountChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['40%', '50%'],
          data: [
            { name: '已评估', value: 761, itemStyle: { color: '#67C23A' } },
            { name: '未评估', value: 239, itemStyle: { color: '#E6A23C' } }
          ],
          label: {
            show: true,
            formatter: '{d}%'
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 风险事件类型环形图
    initEventTypeChart() {
      if (!this.$refs.eventTypeChart) return

      const chart = echarts.init(this.$refs.eventTypeChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['40%', '50%'],
          data: [
            { name: '合同纠纷', value: 230, itemStyle: { color: '#409EFF' } },
            { name: '债务违约', value: 180, itemStyle: { color: '#E6A23C' } },
            { name: '担保诉讼', value: 150, itemStyle: { color: '#F56C6C' } },
            { name: '劳动纠纷', value: 120, itemStyle: { color: '#909399' } },
            { name: '知识产权', value: 81, itemStyle: { color: '#67C23A' } }
          ],
          label: {
            show: true,
            formatter: '{d}%'
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 风险事件化解情况环形图
    initEventResolutionChart() {
      if (!this.$refs.eventResolutionChart) return

      const chart = echarts.init(this.$refs.eventResolutionChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['40%', '50%'],
          data: [
            { name: '已化解', value: 320, itemStyle: { color: '#67C23A' } },
            { name: '化解中', value: 230, itemStyle: { color: '#E6A23C' } },
            { name: '未化解', value: 211, itemStyle: { color: '#F56C6C' } }
          ],
          label: {
            show: true,
            formatter: '{d}%'
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 事件诉讼情况占比环形图
    initLitigationChart() {
      if (!this.$refs.litigationChart) return

      const chart = echarts.init(this.$refs.litigationChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}件 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['40%', '50%'],
          data: [
            { name: '进入诉讼', value: 42, itemStyle: { color: '#F56C6C' } },
            { name: '未进入诉讼', value: 400, itemStyle: { color: '#409EFF' } }
          ],
          label: {
            show: true,
            formatter: '{d}%'
          }
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
.fxsj-container {
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

  .query-stats-card {
    padding: 20px;

    .stat-item {
      text-align: center;
      padding: 10px 0;

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .stat-value {
        font-size: 32px;
        font-weight: bold;

        .unit {
          font-size: 16px;
          font-weight: normal;
          margin-left: 5px;
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

