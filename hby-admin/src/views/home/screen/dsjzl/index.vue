<template>
  <div class="dsjzl-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="primary" icon="el-icon-full-screen" @click="openFullScreen">大屏</el-button>
    </div>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <i class="el-icon-data-line"></i>
          </div>
          <div class="card-content">
            <div class="card-title">监测事件总数</div>
            <div class="card-value">26581<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 12.5%
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
            <div class="card-title">预警事件数</div>
            <div class="card-value">15489<span class="unit">件</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 8.3%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <i class="el-icon-document"></i>
          </div>
          <div class="card-content">
            <div class="card-title">法规数量</div>
            <div class="card-value">761<span class="unit">条</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 5.2%
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <div class="card-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
            <i class="el-icon-pie-chart"></i>
          </div>
          <div class="card-content">
            <div class="card-title">自动预警占比</div>
            <div class="card-value">42<span class="unit">%</span></div>
            <div class="card-trend up">
              <i class="el-icon-top"></i> 3.8%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 第一行：成熟度评价等级 + 重大经营风险事件总体情况 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>成熟度评价等级</span>
          </div>
          <div ref="maturityChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>重大经营风险事件总体情况</span>
          </div>
          <div class="stats-grid">
            <div class="stats-item">
              <div class="stats-label">监测事件总数</div>
              <div class="stats-value">26581<span class="unit">件</span></div>
            </div>
            <div class="stats-item">
              <div class="stats-label">预警事件数</div>
              <div class="stats-value">15489<span class="unit">件</span></div>
            </div>
            <div class="stats-item">
              <div class="stats-label">涉及风险金额</div>
              <div class="stats-value">165836<span class="unit">万元</span></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：知识图谱 + 法律案件 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>知识图谱</span>
          </div>
          <div ref="knowledgeGraphChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>法律案件</span>
          </div>
          <div ref="legalCasesChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：各单位划分图 + 重大风险事件 + 信用等级占比 + 审计问题整改情况 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>各单位划分图</span>
          </div>
          <div class="unit-list">
            <div v-for="item in unitDivisionList" :key="item.id" class="unit-item">
              <div class="item-dot"></div>
              <div class="item-text">{{ item.company }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>重大风险事件</span>
          </div>
          <div class="risk-events-list">
            <div v-for="item in riskEventsList" :key="item.id" class="risk-event-item">
              <div class="event-text">{{ item.event }}</div>
              <div class="event-time">{{ item.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>信用等级占比</span>
          </div>
          <div ref="creditLevelChart" style="height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header">
            <span>审计问题整改情况</span>
          </div>
          <el-table :data="auditRectificationList" height="250" stripe>
            <el-table-column prop="type" label="类型" width="120"></el-table-column>
            <el-table-column prop="count" label="数量" width="80"></el-table-column>
            <el-table-column prop="percent" label="占比" width="80">
              <template slot-scope="scope">
                {{ scope.row.percent }}%
              </template>
            </el-table-column>
          </el-table>
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
      <screen-display v-if="screenVisible" :monitor-data="allMonitorData" @close="closeFullScreen" />
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import ScreenDisplay from './components/ScreenDisplay'
import { getDataMonitorData } from './api/dataMonitor.js'

export default {
  name: 'DataMonitor',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      allMonitorData: null,
      unitDivisionList: [],
      riskEventsList: [],
      auditRectificationList: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const data = await getDataMonitorData()
        this.allMonitorData = data
        this.unitDivisionList = data.unitDivision || []
        this.riskEventsList = data.majorRiskEvents || []
        this.auditRectificationList = data.auditRectification || []

        this.$nextTick(() => {
          this.initCharts()
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts() {
      this.initMaturityChart()
      this.initKnowledgeGraphChart()
      this.initLegalCasesChart()
      this.initCreditLevelChart()
    },
    initMaturityChart() {
      if (!this.$refs.maturityChart || !this.allMonitorData) return

      const chart = echarts.init(this.$refs.maturityChart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          data: this.allMonitorData.maturityLevels || [],
          itemStyle: {
            borderRadius: 5,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}'
          },
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initKnowledgeGraphChart() {
      if (!this.$refs.knowledgeGraphChart || !this.allMonitorData) return

      const chart = echarts.init(this.$refs.knowledgeGraphChart)
      const data = this.allMonitorData.knowledgeGraph

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}'
        },
        legend: {
          data: data.categories.map(c => c.name),
          top: 10
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
            formatter: '{b}',
            fontSize: 10
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
          }
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initLegalCasesChart() {
      if (!this.$refs.legalCasesChart || !this.allMonitorData) return

      const chart = echarts.init(this.$refs.legalCasesChart)
      const data = this.allMonitorData.legalCases || []

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
          data: data.map(item => item.name)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'bar',
          data: data.map((item, index) => ({
            value: item.value,
            itemStyle: {
              color: ['#409EFF', '#67C23A', '#E6A23C'][index]
            }
          })),
          barWidth: '40%'
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initCreditLevelChart() {
      if (!this.$refs.creditLevelChart || !this.allMonitorData) return

      const chart = echarts.init(this.$refs.creditLevelChart)
      const data = this.allMonitorData.creditLevelRatio || []

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
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
            borderColor: '#fff',
            borderWidth: 2
          },
          emphasis: {
            scale: true,
            scaleSize: 10
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
.dsjzl-container {
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

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    padding: 20px 0;

    .stats-item {
      text-align: center;

      .stats-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .stats-value {
        font-size: 32px;
        font-weight: bold;
        color: #409EFF;

        .unit {
          font-size: 14px;
          font-weight: normal;
          margin-left: 5px;
          color: #909399;
        }
      }
    }
  }

  .unit-list {
    max-height: 250px;
    overflow-y: auto;

    .unit-item {
      display: flex;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #EBEEF5;

      &:last-child {
        border-bottom: none;
      }

      .item-dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #409EFF;
        margin-right: 10px;
      }

      .item-text {
        flex: 1;
        font-size: 13px;
        color: #303133;
      }
    }
  }

  .risk-events-list {
    max-height: 250px;
    overflow-y: auto;

    .risk-event-item {
      padding: 10px;
      margin-bottom: 10px;
      background: #F5F7FA;
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: #E4E7ED;
        transform: translateX(5px);
      }

      .event-text {
        font-size: 13px;
        color: #303133;
        margin-bottom: 5px;
      }

      .event-time {
        font-size: 12px;
        color: #909399;
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
