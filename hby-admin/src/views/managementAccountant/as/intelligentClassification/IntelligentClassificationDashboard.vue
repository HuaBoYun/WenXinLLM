<template>
  <div class="intelligent-classification-dashboard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button icon="el-icon-arrow-left" @click="handleBack">返回</el-button>
          <h1 class="page-title">
            <i class="el-icon-data-analysis"></i>
            智能分类仪表板
          </h1>
        </div>
        <div class="header-right">
          <el-button icon="el-icon-refresh" @click="handleRefresh">刷新数据</el-button>
          <el-button type="primary" icon="el-icon-download" @click="handleExportReport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card total">
            <div class="card-icon">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ overviewData.totalClassifications || 0 }}</div>
              <div class="card-label">总分类数</div>
              <div class="card-trend">
                <i class="el-icon-top trend-up"></i>
                <span>+12%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card active">
            <div class="card-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ activeCount || 0 }}</div>
              <div class="card-label">活跃分类</div>
              <div class="card-trend">
                <i class="el-icon-top trend-up"></i>
                <span>+8%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card deployed">
            <div class="card-icon">
              <i class="el-icon-upload"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ deployedCount || 0 }}</div>
              <div class="card-label">已部署</div>
              <div class="card-trend">
                <i class="el-icon-top trend-up"></i>
                <span>+15%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card predictions">
            <div class="card-icon">
              <i class="el-icon-magic-stick"></i>
            </div>
            <div class="card-content">
              <div class="card-number">{{ totalPredictions || 0 }}</div>
              <div class="card-label">总预测次数</div>
              <div class="card-trend">
                <i class="el-icon-top trend-up"></i>
                <span>+25%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <!-- 分类状态分布 -->
      <el-col :span="8">
        <div class="chart-container">
          <h3 class="chart-title">分类状态分布</h3>
          <div ref="statusChart" class="chart" style="height: 300px;"></div>
        </div>
      </el-col>

      <!-- 分类类型分布 -->
      <el-col :span="8">
        <div class="chart-container">
          <h3 class="chart-title">分类类型分布</h3>
          <div ref="typeChart" class="chart" style="height: 300px;"></div>
        </div>
      </el-col>

      <!-- 算法分布 -->
      <el-col :span="8">
        <div class="chart-container">
          <h3 class="chart-title">算法分布</h3>
          <div ref="algorithmChart" class="chart" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 趋势图表 -->
    <el-row :gutter="20">
      <!-- 分类创建趋势 -->
      <el-col :span="12">
        <div class="chart-container">
          <h3 class="chart-title">分类创建趋势</h3>
          <div ref="trendChart" class="chart" style="height: 350px;"></div>
        </div>
      </el-col>

      <!-- 预测趋势 -->
      <el-col :span="12">
        <div class="chart-container">
          <h3 class="chart-title">预测趋势</h3>
          <div ref="predictionChart" class="chart" style="height: 350px;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 性能排行榜 -->
    <el-row :gutter="20">
      <!-- 准确率排行 -->
      <el-col :span="12">
        <div class="ranking-container">
          <h3 class="section-title">准确率排行榜</h3>
          <div class="ranking-list">
            <div
              v-for="(item, index) in accuracyRanking"
              :key="item.classificationId"
              class="ranking-item"
            >
              <div class="ranking-number" :class="getRankingClass(index)">
                {{ index + 1 }}
              </div>
              <div class="ranking-content">
                <div class="ranking-name">{{ item.classificationName }}</div>
                <div class="ranking-type">{{ getTypeLabel(item.classificationType) }}</div>
              </div>
              <div class="ranking-value">
                <span class="accuracy-value">{{ formatAccuracy(item.accuracy) }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 预测次数排行 -->
      <el-col :span="12">
        <div class="ranking-container">
          <h3 class="section-title">预测次数排行榜</h3>
          <div class="ranking-list">
            <div
              v-for="(item, index) in predictionRanking"
              :key="item.classificationId"
              class="ranking-item"
            >
              <div class="ranking-number" :class="getRankingClass(index)">
                {{ index + 1 }}
              </div>
              <div class="ranking-content">
                <div class="ranking-name">{{ item.classificationName }}</div>
                <div class="ranking-type">{{ getTypeLabel(item.classificationType) }}</div>
              </div>
              <div class="ranking-value">
                <span class="prediction-value">{{ item.predictionCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 最近活动 -->
    <div class="activity-container">
      <h3 class="section-title">最近活动</h3>
      <el-table :data="recentActivities" style="width: 100%">
        <el-table-column prop="classificationName" label="分类名称" width="200" />
        <el-table-column prop="action" label="操作" width="120">
          <template slot-scope="scope">
            <el-tag :type="getActionTagType(scope.row.action)">
              {{ scope.row.action }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accuracy" label="准确率" width="100">
          <template slot-scope="scope">
            {{ formatAccuracy(scope.row.accuracy) }}
          </template>
        </el-table-column>
        <el-table-column prop="predictionCount" label="预测次数" width="100" />
        <el-table-column prop="updatedTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleManage(scope.row)">管理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  quickGetClassificationOverview,
  getClassificationRanking,
  getClassificationPage,
  getClassificationTypeLabel,
  getClassificationStatusLabel,
  getClassificationStatusTagType,
  formatAccuracy
} from '@/api/managementAccountant/as/intelligentClassification'

export default {
  name: 'IntelligentClassificationDashboard',
  data() {
    return {
      overviewData: {},
      statusStats: [],
      typeStats: [],
      algorithmStats: [],
      accuracyRanking: [],
      predictionRanking: [],
      recentActivities: [],
      loading: false,
      // 图表实例
      statusChart: null,
      typeChart: null,
      algorithmChart: null,
      trendChart: null,
      predictionChart: null
    }
  },
  computed: {
    activeCount() {
      const activeStat = this.statusStats.find(item => item.status === 'ACTIVE')
      return activeStat ? activeStat.count : 0
    },
    deployedCount() {
      const deployedStat = this.statusStats.find(item => item.status === 'DEPLOYED')
      return deployedStat ? deployedStat.count : 0
    },
    totalPredictions() {
      return this.overviewData.performanceMetrics?.totalPredictions || 0
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        this.loading = true
        const tenantId = this.$store.getters.tenantId
        
        // 加载概览数据
        const overviewResult = await quickGetClassificationOverview(tenantId)
        this.overviewData = overviewResult.overview
        this.statusStats = overviewResult.statusStats
        this.typeStats = overviewResult.typeStats
        this.algorithmStats = overviewResult.algorithmStats
        
        // 加载排行榜数据
        const [accuracyResult, predictionResult] = await Promise.all([
          getClassificationRanking(tenantId, 'accuracy', 10),
          getClassificationRanking(tenantId, 'prediction_count', 10)
        ])
        
        this.accuracyRanking = accuracyResult.data || []
        this.predictionRanking = predictionResult.data || []
        
        // 加载最近活动
        const activityResult = await getClassificationPage({
          current: 1,
          size: 10,
          tenantId
        })
        this.recentActivities = activityResult.data.records || []
        
        // 更新图表
        this.updateCharts()
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initCharts() {
      this.statusChart = echarts.init(this.$refs.statusChart)
      this.typeChart = echarts.init(this.$refs.typeChart)
      this.algorithmChart = echarts.init(this.$refs.algorithmChart)
      this.trendChart = echarts.init(this.$refs.trendChart)
      this.predictionChart = echarts.init(this.$refs.predictionChart)
    },

    // 更新图表
    updateCharts() {
      this.updateStatusChart()
      this.updateTypeChart()
      this.updateAlgorithmChart()
      this.updateTrendChart()
      this.updatePredictionChart()
    },

    // 更新状态分布图
    updateStatusChart() {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '分类状态',
            type: 'pie',
            radius: '50%',
            data: this.statusStats.map(item => ({
              value: item.count,
              name: this.getStatusLabel(item.status)
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      this.statusChart.setOption(option)
    },

    // 更新类型分布图
    updateTypeChart() {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '分类类型',
            type: 'pie',
            radius: '50%',
            data: this.typeStats.map(item => ({
              value: item.count,
              name: this.getTypeLabel(item.type)
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      this.typeChart.setOption(option)
    },

    // 更新算法分布图
    updateAlgorithmChart() {
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: this.algorithmStats.map(item => item.algorithm),
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '数量',
            type: 'bar',
            barWidth: '60%',
            data: this.algorithmStats.map(item => item.count)
          }
        ]
      }
      this.algorithmChart.setOption(option)
    },

    // 更新趋势图
    updateTrendChart() {
      // 模拟趋势数据
      const dates = []
      const values = []
      for (let i = 29; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        dates.push(date.toISOString().split('T')[0])
        values.push(Math.floor(Math.random() * 10) + 1)
      }

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '创建数量',
            type: 'line',
            stack: 'Total',
            data: values,
            smooth: true,
            areaStyle: {}
          }
        ]
      }
      this.trendChart.setOption(option)
    },

    // 更新预测趋势图
    updatePredictionChart() {
      // 模拟预测趋势数据
      const dates = []
      const predictions = []
      const successes = []
      
      for (let i = 29; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        dates.push(date.toISOString().split('T')[0])
        const predictionCount = Math.floor(Math.random() * 100) + 50
        predictions.push(predictionCount)
        successes.push(Math.floor(predictionCount * (0.8 + Math.random() * 0.15)))
      }

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['总预测', '成功预测']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '总预测',
            type: 'line',
            data: predictions,
            smooth: true
          },
          {
            name: '成功预测',
            type: 'line',
            data: successes,
            smooth: true
          }
        ]
      }
      this.predictionChart.setOption(option)
    },

    // 销毁图表
    destroyCharts() {
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      if (this.typeChart) {
        this.typeChart.dispose()
      }
      if (this.algorithmChart) {
        this.algorithmChart.dispose()
      }
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      if (this.predictionChart) {
        this.predictionChart.dispose()
      }
    },

    // 刷新数据
    handleRefresh() {
      this.loadData()
    },

    // 导出报告
    handleExportReport() {
      this.$message.success('报告导出功能开发中...')
    },

    // 返回
    handleBack() {
      this.$router.go(-1)
    },

    // 查看详情
    handleViewDetail(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/detail/${row.classificationId}`)
    },

    // 管理
    handleManage(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/edit/${row.classificationId}`)
    },

    // 工具方法
    getTypeLabel(type) {
      return getClassificationTypeLabel(type)
    },

    getStatusLabel(status) {
      return getClassificationStatusLabel(status)
    },

    getStatusTagType(status) {
      return getClassificationStatusTagType(status)
    },

    getActionTagType(action) {
      const actionTagMap = {
        '创建': 'primary',
        '训练': 'warning',
        '部署': 'success',
        '预测': 'info'
      }
      return actionTagMap[action] || 'default'
    },

    formatAccuracy(accuracy) {
      return formatAccuracy(accuracy)
    },

    getRankingClass(index) {
      if (index === 0) return 'rank-first'
      if (index === 1) return 'rank-second'
      if (index === 2) return 'rank-third'
      return 'rank-normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-classification-dashboard {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .page-title {
    margin: 0 0 0 10px;
    font-size: 20px;
    color: #303133;
    
    i {
      margin-right: 8px;
      color: #409EFF;
    }
  }
}

.overview-section {
  margin-bottom: 20px;

  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
    }

    &.total::before {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.active::before {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.deployed::before {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.predictions::before {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
    }

    &.total .card-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.active .card-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.deployed .card-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.predictions .card-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    .card-content {
      flex: 1;

      .card-number {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        line-height: 1;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
        margin: 4px 0;
      }

      .card-trend {
        font-size: 12px;
        color: #67C23A;

        .trend-up {
          margin-right: 4px;
        }
      }
    }
  }
}

.chart-container, .ranking-container, .activity-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  .chart-title, .section-title {
    margin: 0 0 20px 0;
    font-size: 16px;
    color: #303133;
    border-bottom: 2px solid #409EFF;
    padding-bottom: 8px;
  }
}

.ranking-list {
  .ranking-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #EBEEF5;

    &:last-child {
      border-bottom: none;
    }

    .ranking-number {
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      color: white;
      margin-right: 12px;

      &.rank-first {
        background: #FFD700;
      }

      &.rank-second {
        background: #C0C0C0;
      }

      &.rank-third {
        background: #CD7F32;
      }

      &.rank-normal {
        background: #909399;
      }
    }

    .ranking-content {
      flex: 1;

      .ranking-name {
        font-size: 14px;
        color: #303133;
        font-weight: bold;
      }

      .ranking-type {
        font-size: 12px;
        color: #909399;
        margin-top: 2px;
      }
    }

    .ranking-value {
      .accuracy-value {
        font-size: 16px;
        font-weight: bold;
        color: #67C23A;
      }

      .prediction-value {
        font-size: 16px;
        font-weight: bold;
        color: #409EFF;
      }
    }
  }
}
</style>
