<template>
  <div class="fxpg-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="primary" icon="el-icon-full-screen" @click="openFullScreen">大屏</el-button>
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <i class="el-icon-document-checked"></i>
          </div>
          <div class="card-content">
            <div class="card-title">风险评估结果</div>
            <div class="card-value">761<span class="unit">个</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 5.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <i class="el-icon-data-analysis"></i>
          </div>
          <div class="card-content">
            <div class="card-title">风险评估数量</div>
            <div class="card-value">470<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 3.5%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <i class="el-icon-warning-outline"></i>
          </div>
          <div class="card-content">
            <div class="card-title">高风险评估事项</div>
            <div class="card-value">42<span class="unit">项</span></div>
            <div class="card-trend down">
              <i class="el-icon-bottom"></i> 1.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
            <i class="el-icon-s-grid"></i>
          </div>
          <div class="card-content">
            <div class="card-title">事项类型</div>
            <div class="card-value">8<span class="unit">类</span></div>
            <div class="card-trend">
              <i class="el-icon-minus"></i> 0%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第一行：表格和图表 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险评估性质统计</span>
          </div>
          <el-table :data="assessmentNatureData" height="300" stripe>
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="company" label="企业名称" min-width="120"></el-table-column>
            <el-table-column prop="item" label="评估事项" min-width="100"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
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
            <span>事项类型</span>
          </div>
          <div ref="itemTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：图表 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>事项风险程度</span>
          </div>
          <div ref="riskLevelChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险评估事项统计</span>
          </div>
          <div ref="assessmentStatsChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>最终审议单位级次</span>
          </div>
          <div ref="unitLevelChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：表格和统计 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>高风险评估事项</span>
          </div>
          <el-table :data="highRiskItems" height="300" stripe>
            <el-table-column type="index" label="序号" width="60"></el-table-column>
            <el-table-column prop="company" label="企业名称" min-width="150"></el-table-column>
            <el-table-column prop="item" label="评估事项" min-width="120"></el-table-column>
            <el-table-column prop="level" label="风险程度" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.level === '高' ? 'danger' : 'warning'" size="small">
                  {{ scope.row.level }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>风险评估情况查询</span>
          </div>
          <div class="query-stats-container">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="stat-box">
                  <div class="stat-label">评估总数</div>
                  <div class="stat-value primary">761<span class="unit">个</span></div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-box">
                  <div class="stat-label">高风险</div>
                  <div class="stat-value danger">42<span class="unit">个</span></div>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <div class="stat-box">
                  <div class="stat-label">中风险</div>
                  <div class="stat-value warning">230<span class="unit">个</span></div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-box">
                  <div class="stat-label">低风险</div>
                  <div class="stat-value success">489<span class="unit">个</span></div>
                </div>
              </el-col>
            </el-row>
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
      <screen-display v-if="screenVisible" :risk-data="allRiskData" @close="closeFullScreen" />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getRiskAssessmentData } from './api/riskAssessment'

export default {
  name: 'RiskAssessment',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      allRiskData: null,
      assessmentNatureData: [
        { company: 'xxxx建设控股有限公司', item: '重大投资决策' },
        { company: 'xxxx汽车有限公司', item: '对外担保事项' },
        { company: 'xxxx投资发展有限公司', item: '资产处置' },
        { company: 'xxxx汽车集团有限公司', item: '重大合同签订' },
        { company: 'xxxx集团有限公司', item: '关联交易' },
        { company: 'xxxx建设开发有限公司', item: '融资决策' },
        { company: 'xxxx集团有限公司', item: '股权变动' },
        { company: 'xxxx汽车集团有限公司', item: '重大投资' }
      ],
      highRiskItems: [
        { company: 'xxxx建设控股有限公司', item: '重大投资决策', level: '高' },
        { company: 'xxxx汽车有限公司', item: '对外担保事项', level: '高' },
        { company: 'xxxx投资发展有限公司', item: '资产处置', level: '高' },
        { company: 'xxxx汽车集团有限公司', item: '重大合同签订', level: '中' },
        { company: 'xxxx集团有限公司', item: '关联交易', level: '高' },
        { company: 'xxxx建设开发有限公司', item: '融资决策', level: '中' },
        { company: 'xxxx集团有限公司', item: '股权变动', level: '高' },
        { company: 'xxxx汽车集团有限公司', item: '重大投资', level: '中' }
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
        const data = await getRiskAssessmentData()
        this.allRiskData = data
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts() {
      this.$nextTick(() => {
        this.initAssessmentCountChart()
        this.initItemTypeChart()
        this.initRiskLevelChart()
        this.initAssessmentStatsChart()
        this.initUnitLevelChart()
      })
    },
    // 风险评估数量环形图
    initAssessmentCountChart() {
      if (!this.$refs.assessmentCountChart) return

      const chart = echarts.init(this.$refs.assessmentCountChart)

      const data = [
        { name: '已评估', value: 470, itemStyle: { color: '#00d4ff' } },
        { name: '未评估', value: 300, itemStyle: { color: '#e0e0e0' } }
      ]

      const total = data.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((data[0].value / total) * 100).toFixed(1)

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
          data: data,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 事项类型环形图
    initItemTypeChart() {
      if (!this.$refs.itemTypeChart) return

      const chart = echarts.init(this.$refs.itemTypeChart)

      const data = [
        { name: '委托1', value: 32, itemStyle: { color: '#00d4ff' } },
        { name: '委托2', value: 12, itemStyle: { color: '#ffd700' } },
        { name: '委托3', value: 5, itemStyle: { color: '#ff6b6b' } },
        { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
      ]

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: { color: '#666', fontSize: 12 }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['35%', '50%'],
          data: data,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 事项风险程度环形图
    initRiskLevelChart() {
      if (!this.$refs.riskLevelChart) return

      const chart = echarts.init(this.$refs.riskLevelChart)

      const data = [
        { name: '高', value: 42, itemStyle: { color: '#ff6b6b' } },
        { name: '中', value: 230, itemStyle: { color: '#ffd700' } },
        { name: '低', value: 489, itemStyle: { color: '#00d4ff' } }
      ]

      const total = data.reduce((sum, item) => sum + item.value, 0)
      const highRiskPercentage = ((data[0].value / total) * 100).toFixed(1)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        graphic: [
          {
            type: 'text',
            left: 'center',
            top: '42%',
            style: {
              text: '高风险',
              textAlign: 'center',
              fill: '#666',
              fontSize: 14
            }
          },
          {
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
          }
        ],
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 风险评估事项统计柱状图
    initAssessmentStatsChart() {
      if (!this.$refs.assessmentStatsChart) return

      const chart = echarts.init(this.$refs.assessmentStatsChart)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['低', '中', '高'],
          top: 10,
          textStyle: {
            color: '#666'
          }
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
            color: '#666'
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#666'
          }
        },
        series: [
          {
            name: '低',
            type: 'bar',
            stack: 'total',
            data: [500, 600, 550, 650, 600, 700],
            itemStyle: {
              color: '#00d4ff'
            }
          },
          {
            name: '中',
            type: 'bar',
            stack: 'total',
            data: [300, 350, 320, 380, 340, 400],
            itemStyle: {
              color: '#ffd700'
            }
          },
          {
            name: '高',
            type: 'bar',
            stack: 'total',
            data: [200, 250, 230, 270, 260, 300],
            itemStyle: {
              color: '#ff6b6b'
            }
          }
        ]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 最终审议单位级次环形图
    initUnitLevelChart() {
      if (!this.$refs.unitLevelChart) return

      const chart = echarts.init(this.$refs.unitLevelChart)

      const data = [
        { name: '委托1', value: 40, itemStyle: { color: '#00d4ff' } },
        { name: '委托2', value: 30, itemStyle: { color: '#ffd700' } },
        { name: '委托3', value: 15, itemStyle: { color: '#ff6b6b' } },
        { name: '其他', value: 15, itemStyle: { color: '#a78bfa' } }
      ]

      const total = data.reduce((sum, item) => sum + item.value, 0)
      const percentage = ((data[0].value / total) * 100).toFixed(1)

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
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: { color: '#666', fontSize: 12 }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['35%', '50%'],
          data: data,
          label: {
            show: false
          },
          itemStyle: {
            borderRadius: 8
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
.fxpg-container {
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

  .query-stats-container {
    padding: 20px;

    .stat-box {
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      border-radius: 8px;
      padding: 20px;
      text-align: center;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .stat-label {
        font-size: 14px;
        color: #606266;
        margin-bottom: 10px;
      }

      .stat-value {
        font-size: 32px;
        font-weight: bold;

        &.primary {
          color: #409eff;
        }

        &.danger {
          color: #f56c6c;
        }

        &.warning {
          color: #e6a23c;
        }

        &.success {
          color: #67c23a;
        }

        .unit {
          font-size: 16px;
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

