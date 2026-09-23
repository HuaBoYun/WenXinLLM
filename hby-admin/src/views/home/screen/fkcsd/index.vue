<template>
  <div class="fkcsd-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>风控成熟度</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreen"
          class="screen-btn"
        >
          大屏
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 数据概览卡片 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-title">成熟度平均分</div>
            <div class="card-value">{{ overviewData.maturityScore }}<span class="unit">分</span></div>
            <div class="card-trend" :class="overviewData.maturityTrend > 0 ? 'up' : 'down'">
              <i :class="overviewData.maturityTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(overviewData.maturityTrend) }}%
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-title">重大缺陷数</div>
            <div class="card-value">{{ overviewData.majorDefects }}<span class="unit">个</span></div>
            <div class="card-trend" :class="overviewData.majorDefectsTrend > 0 ? 'down' : 'up'">
              <i :class="overviewData.majorDefectsTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(overviewData.majorDefectsTrend) }}%
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-title">缺陷类型数</div>
            <div class="card-value">{{ overviewData.defectTypes }}<span class="unit">类</span></div>
            <div class="card-trend" :class="overviewData.defectTrend > 0 ? 'down' : 'up'">
              <i :class="overviewData.defectTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(overviewData.defectTrend) }}%
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-title">各单位缺陷总数</div>
            <div class="card-value">{{ overviewData.totalDefects }}<span class="unit">个</span></div>
            <div class="card-trend" :class="overviewData.totalDefectsTrend > 0 ? 'down' : 'up'">
              <i :class="overviewData.totalDefectsTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(overviewData.totalDefectsTrend) }}%
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第一行图表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>成熟度排名TOP10</span>
            </div>
            <el-table :data="maturityTop10" height="350" stripe>
              <el-table-column type="index" label="排名" width="60"></el-table-column>
              <el-table-column prop="companyName" label="单位名称" show-overflow-tooltip></el-table-column>
              <el-table-column prop="score" label="成熟度评分" width="100" align="center"></el-table-column>
              <el-table-column prop="level" label="等级" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag type="success" size="small">{{ scope.row.level }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>成熟度排名等级</span>
            </div>
            <div ref="maturityLevelChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>重大缺陷</span>
            </div>
            <div ref="majorDefectChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第二行图表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>各二级单位成熟度评分</span>
            </div>
            <div ref="unitMaturityChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>缺陷类型分布</span>
            </div>
            <div ref="defectTypeChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 第三行图表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>缺陷分布情况</span>
            </div>
            <el-table :data="defectDistribution" height="350" stripe>
              <el-table-column prop="domain" label="业务领域名称" show-overflow-tooltip></el-table-column>
              <el-table-column prop="company" label="公司" width="80" align="center"></el-table-column>
              <el-table-column prop="count1" label="数量" width="80" align="center"></el-table-column>
              <el-table-column prop="count2" label="数量" width="80" align="center"></el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>缺陷重要程度</span>
            </div>
            <div ref="defectSeverityChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>各单位缺陷数量</span>
            </div>
            <div ref="unitDefectChart" style="height: 350px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <screen-display
        v-if="screenVisible"
        :risk-data="allRiskData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getRiskControlData } from './api/riskControl'
import * as echarts from 'echarts'

export default {
  name: 'Fkcsd',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      screenVisible: false,
      loading: false,
      overviewData: {
        maturityScore: 320,
        maturityTrend: 3.5,
        majorDefects: 1000,
        majorDefectsTrend: -5.2,
        defectTypes: 5,
        defectTrend: -2.1,
        totalDefects: 7650,
        totalDefectsTrend: -1.8
      },
      maturityTop10: [],
      defectDistribution: [],
      allRiskData: {},
      charts: {}
    }
  },
  mounted() {
    this.loadData()
    this.$nextTick(() => {
      this.initAllCharts()
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 模拟数据加载
        this.maturityTop10 = [
          { companyName: 'XXXXXX建设集团有限公司', score: 356, level: '成熟级' },
          { companyName: 'XXXXXX有限公司', score: 343, level: '成熟级' },
          { companyName: 'XXXXXXXXXXXXXX控股有限公司', score: 340, level: '成熟级' },
          { companyName: 'XXXXXX汽车集团有限公司', score: 335, level: '成熟级' },
          { companyName: 'XXXXXX集团有限公司', score: 330, level: '成熟级' },
          { companyName: 'XXXXXXXXXXXXXX开发学院', score: 325, level: '成熟级' },
          { companyName: 'XXXXXX集团有限公司', score: 320, level: '成熟级' },
          { companyName: 'XXXXXX汽车集团有限公司', score: 315, level: '成熟级' },
          { companyName: 'XXXXXX集团有限公司', score: 310, level: '成熟级' },
          { companyName: 'XXXXXX江苏集团有限公司', score: 305, level: '成熟级' }
        ]

        this.defectDistribution = [
          { domain: '名词1', company: 35, count1: 15, count2: 5 },
          { domain: '名词2', company: 18, count1: 10, count2: 3 },
          { domain: '名词3', company: 5, count1: 5, count2: 2 },
          { domain: '名词4', company: 30, count1: 30, count2: 8 },
          { domain: '名词5', company: 15, count1: 15, count2: 4 }
        ]
      } catch (error) {
        this.$message.error('数据加载失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    initAllCharts() {
      this.initMaturityLevelChart()
      this.initMajorDefectChart()
      this.initUnitMaturityChart()
      this.initDefectTypeChart()
      this.initDefectSeverityChart()
      this.initUnitDefectChart()
    },
    initMaturityLevelChart() {
      if (!this.$refs.maturityLevelChart) return
      const chart = echarts.init(this.$refs.maturityLevelChart)
      this.charts.maturityLevel = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '成熟度等级',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 40, name: '初始级 40%', itemStyle: { color: '#ff6b6b' } },
            { value: 40, name: '规范级 40%', itemStyle: { color: '#ffd700' } },
            { value: 10, name: '已定义级 10%', itemStyle: { color: '#00d4ff' } },
            { value: 5, name: '量化管理级 5%', itemStyle: { color: '#00ff88' } },
            { value: 5, name: '优化级 5%', itemStyle: { color: '#a78bfa' } }
          ],
          label: { show: true, formatter: '{b}' }
        }]
      }

      chart.setOption(option)
    },
    initMajorDefectChart() {
      if (!this.$refs.majorDefectChart) return
      const chart = echarts.init(this.$refs.majorDefectChart)
      this.charts.majorDefect = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '重大缺陷',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 60, name: '已提交 60%', itemStyle: { color: '#00d4ff' } },
            { value: 40, name: '未提交 40%', itemStyle: { color: '#ffd700' } }
          ],
          label: { show: true, formatter: '{b}' }
        }]
      }

      chart.setOption(option)
    },
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
          axisLabel: { rotate: 30 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '成熟度评分',
          type: 'bar',
          data: yData,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
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
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '缺陷类型',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 32, name: '名词1 32%', itemStyle: { color: '#00d4ff' } },
            { value: 18, name: '名词2 18%', itemStyle: { color: '#ffd700' } },
            { value: 5, name: '名词3 5%', itemStyle: { color: '#00ff88' } },
            { value: 10, name: '名词4 10%', itemStyle: { color: '#ff6b6b' } },
            { value: 15, name: '名词5 15%', itemStyle: { color: '#a78bfa' } }
          ],
          label: { show: true, formatter: '{b}' }
        }]
      }

      chart.setOption(option)
    },
    initDefectSeverityChart() {
      if (!this.$refs.defectSeverityChart) return
      const chart = echarts.init(this.$refs.defectSeverityChart)
      this.charts.defectSeverity = chart

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', left: 'left', top: '10%' },
        series: [{
          name: '缺陷重要程度',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          data: [
            { value: 47, name: '重要缺陷 47%', itemStyle: { color: '#ff6b6b' } },
            { value: 30, name: '一般缺陷 30%', itemStyle: { color: '#ffd700' } },
            { value: 23, name: '轻微缺陷 23%', itemStyle: { color: '#00ff88' } }
          ],
          label: { show: true, formatter: '{b}' }
        }]
      }

      chart.setOption(option)
    },
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
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: xData,
          axisLabel: { rotate: 30 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '缺陷数量',
          type: 'bar',
          data: yData,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00ff88' },
              { offset: 1, color: '#00d4ff' }
            ])
          }
        }]
      }

      chart.setOption(option)
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    },
    handleScreenClose() {
      this.screenVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.fkcsd-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;

  .page-header {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        font-size: 28px;
        color: #409eff;
      }

      h2 {
        margin: 0;
        font-size: 24px;
        color: #303133;
      }
    }

    .screen-btn {
      font-size: 16px;
      padding: 12px 24px;
    }
  }

  .main-content {
    .overview-card {
      text-align: center;
      
      .card-title {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }

      .card-value {
        font-size: 32px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 10px;

        .unit {
          font-size: 16px;
          margin-left: 5px;
        }
      }

      .card-trend {
        font-size: 14px;

        &.up {
          color: #67c23a;
        }

        &.down {
          color: #f56c6c;
        }
      }
    }
  }
}

::v-deep .screen-dialog {
  margin: 0;
  padding: 0;
  background: transparent;

  .el-dialog__header {
    display: none;
  }

  .el-dialog__body {
    padding: 0;
    height: 100vh;
  }
}
</style>

