<template>
  <div class="archive-search-dashboard">
    <!-- 概览统计 -->
    <div class="overview-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon total">
              <i class="el-icon-files"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.totalSearches || 0 }}</div>
              <div class="stats-label">总检索配置</div>
              <div class="stats-trend" :class="getTrendClass(overview.searchesTrend)">
                <i :class="getTrendIcon(overview.searchesTrend)"></i>
                {{ Math.abs(overview.searchesTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon active">
              <i class="el-icon-success"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.activeSearches || 0 }}</div>
              <div class="stats-label">活跃配置</div>
              <div class="stats-trend" :class="getTrendClass(overview.activeTrend)">
                <i :class="getTrendIcon(overview.activeTrend)"></i>
                {{ Math.abs(overview.activeTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon performance">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.avgResponseTime || 0 }}ms</div>
              <div class="stats-label">平均响应时间</div>
              <div class="stats-trend" :class="getTrendClass(-overview.responseTrend)">
                <i :class="getTrendIcon(-overview.responseTrend)"></i>
                {{ Math.abs(overview.responseTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon accuracy">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ formatAccuracy(overview.avgAccuracy) }}</div>
              <div class="stats-label">平均准确率</div>
              <div class="stats-trend" :class="getTrendClass(overview.accuracyTrend)">
                <i :class="getTrendIcon(overview.accuracyTrend)"></i>
                {{ Math.abs(overview.accuracyTrend || 0) }}%
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-area">
      <el-row :gutter="20">
        <!-- 检索状态分布 -->
        <el-col :span="8">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <i class="el-icon-pie-chart"></i>
              <span>检索状态分布</span>
            </div>
            <div ref="statusChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 检索类型分布 -->
        <el-col :span="8">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <i class="el-icon-pie-chart"></i>
              <span>检索类型分布</span>
            </div>
            <div ref="typeChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 检索引擎分布 -->
        <el-col :span="8">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <i class="el-icon-s-data"></i>
              <span>检索引擎分布</span>
            </div>
            <div ref="engineChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px">
        <!-- 检索趋势 -->
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <i class="el-icon-data-line"></i>
              <span>检索趋势</span>
              <div class="header-actions">
                <el-radio-group v-model="trendPeriod" size="mini" @change="loadTrendData">
                  <el-radio-button label="7d">7天</el-radio-button>
                  <el-radio-button label="30d">30天</el-radio-button>
                  <el-radio-button label="90d">90天</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <div ref="trendChart" class="chart-container-large"></div>
          </el-card>
        </el-col>
        
        <!-- 性能趋势 -->
        <el-col :span="12">
          <el-card shadow="never" class="chart-card">
            <div slot="header" class="card-header">
              <i class="el-icon-data-line"></i>
              <span>性能趋势</span>
              <div class="header-actions">
                <el-radio-group v-model="performancePeriod" size="mini" @change="loadPerformanceData">
                  <el-radio-button label="7d">7天</el-radio-button>
                  <el-radio-button label="30d">30天</el-radio-button>
                  <el-radio-button label="90d">90天</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <div ref="performanceChart" class="chart-container-large"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜和热门关键词 -->
    <div class="ranking-area">
      <el-row :gutter="20">
        <!-- 准确率排行榜 -->
        <el-col :span="8">
          <el-card shadow="never" class="ranking-card">
            <div slot="header" class="card-header">
              <i class="el-icon-trophy"></i>
              <span>准确率排行榜</span>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in accuracyRanking"
                :key="item.searchId"
                class="ranking-item"
                :class="{ 'top-three': index < 3 }"
              >
                <div class="ranking-number" :class="`rank-${index + 1}`">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="ranking-name">{{ item.searchName }}</div>
                  <div class="ranking-type">{{ formatSearchType(item.searchType) }}</div>
                </div>
                <div class="ranking-value">
                  {{ formatAccuracy(item.searchAccuracy) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 检索次数排行榜 -->
        <el-col :span="8">
          <el-card shadow="never" class="ranking-card">
            <div slot="header" class="card-header">
              <i class="el-icon-trophy"></i>
              <span>检索次数排行榜</span>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in countRanking"
                :key="item.searchId"
                class="ranking-item"
                :class="{ 'top-three': index < 3 }"
              >
                <div class="ranking-number" :class="`rank-${index + 1}`">
                  {{ index + 1 }}
                </div>
                <div class="ranking-content">
                  <div class="ranking-name">{{ item.searchName }}</div>
                  <div class="ranking-type">{{ formatSearchType(item.searchType) }}</div>
                </div>
                <div class="ranking-value">
                  {{ item.totalCount }}次
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 热门关键词 -->
        <el-col :span="8">
          <el-card shadow="never" class="ranking-card">
            <div slot="header" class="card-header">
              <i class="el-icon-star-on"></i>
              <span>热门关键词</span>
            </div>
            <div class="keywords-list">
              <el-tag
                v-for="keyword in popularKeywords"
                :key="keyword.keyword"
                class="keyword-tag"
                :type="getKeywordTagType(keyword.count)"
                effect="plain"
              >
                {{ keyword.keyword }}
                <span class="keyword-count">({{ keyword.count }})</span>
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activities">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <i class="el-icon-time"></i>
          <span>最近活动</span>
          <div class="header-actions">
            <el-button type="text" @click="loadRecentActivities">刷新</el-button>
          </div>
        </div>
        
        <el-table :data="recentActivities" style="width: 100%">
          <el-table-column prop="searchName" label="检索配置" width="200" />
          <el-table-column prop="operation" label="操作类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getOperationTagType(scope.row.operation)">
                {{ formatOperation(scope.row.operation) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="keywords" label="检索关键词" width="200" show-overflow-tooltip />
          <el-table-column prop="resultCount" label="结果数量" width="100" />
          <el-table-column prop="responseTime" label="响应时间" width="120">
            <template slot-scope="scope">
              {{ formatResponseTime(scope.row.responseTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="accuracy" label="准确率" width="100">
            <template slot-scope="scope">
              {{ formatAccuracy(scope.row.accuracy) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleViewDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  quickGetSearchOverview,
  countBySearchStatus,
  countBySearchType,
  countBySearchEngine,
  getSearchTrend,
  getPerformanceTrend,
  getSearchRanking,
  getPopularKeywords,
  formatSearchType,
  formatAccuracy,
  formatResponseTime
} from '@/api/managementAccountant/as/archiveSearch'

export default {
  name: 'ArchiveSearchDashboard',
  data() {
    return {
      overview: {},
      trendPeriod: '30d',
      performancePeriod: '30d',
      accuracyRanking: [],
      countRanking: [],
      popularKeywords: [],
      recentActivities: [],
      statusChart: null,
      typeChart: null,
      engineChart: null,
      trendChart: null,
      performanceChart: null
    }
  },
  created() {
    this.loadData()
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadOverview(),
        this.loadDistributionData(),
        this.loadTrendData(),
        this.loadPerformanceData(),
        this.loadRankingData(),
        this.loadPopularKeywords(),
        this.loadRecentActivities()
      ])
    },
    
    async loadOverview() {
      try {
        const tenantId = this.$store.getters.tenantId
        const result = await quickGetSearchOverview(tenantId)
        this.overview = result.overview || {}
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    
    async loadDistributionData() {
      try {
        const tenantId = this.$store.getters.tenantId
        const [statusData, typeData, engineData] = await Promise.all([
          countBySearchStatus(tenantId),
          countBySearchType(tenantId),
          countBySearchEngine(tenantId)
        ])
        
        this.updateStatusChart(statusData.data || [])
        this.updateTypeChart(typeData.data || [])
        this.updateEngineChart(engineData.data || [])
      } catch (error) {
        console.error('加载分布数据失败:', error)
      }
    },
    
    async loadTrendData() {
      try {
        const tenantId = this.$store.getters.tenantId
        const days = parseInt(this.trendPeriod.replace('d', ''))
        const endDate = new Date()
        const startDate = new Date(endDate.getTime() - days * 24 * 60 * 60 * 1000)
        
        const response = await getSearchTrend(
          tenantId,
          startDate.toISOString(),
          endDate.toISOString(),
          'day'
        )
        
        this.updateTrendChart(response.data || [])
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },
    
    async loadPerformanceData() {
      try {
        const tenantId = this.$store.getters.tenantId
        const days = parseInt(this.performancePeriod.replace('d', ''))
        const endDate = new Date()
        const startDate = new Date(endDate.getTime() - days * 24 * 60 * 60 * 1000)
        
        const response = await getPerformanceTrend(
          tenantId,
          startDate.toISOString(),
          endDate.toISOString(),
          'day'
        )
        
        this.updatePerformanceChart(response.data || [])
      } catch (error) {
        console.error('加载性能数据失败:', error)
      }
    },
    
    async loadRankingData() {
      try {
        const tenantId = this.$store.getters.tenantId
        const [accuracyResponse, countResponse] = await Promise.all([
          getSearchRanking(tenantId, 'accuracy', 10),
          getSearchRanking(tenantId, 'count', 10)
        ])
        
        this.accuracyRanking = accuracyResponse.data || []
        this.countRanking = countResponse.data || []
      } catch (error) {
        console.error('加载排行榜数据失败:', error)
      }
    },
    
    async loadPopularKeywords() {
      try {
        const tenantId = this.$store.getters.tenantId
        const response = await getPopularKeywords(tenantId, 20)
        this.popularKeywords = response.data || []
      } catch (error) {
        console.error('加载热门关键词失败:', error)
      }
    },
    
    loadRecentActivities() {
      // 模拟最近活动数据
      this.recentActivities = [
        {
          id: 1,
          searchName: '财务档案检索',
          operation: 'SEARCH',
          keywords: '财务报表',
          resultCount: 156,
          responseTime: 245,
          accuracy: 0.95,
          createTime: new Date()
        },
        {
          id: 2,
          searchName: '合同档案检索',
          operation: 'INDEX',
          keywords: '-',
          resultCount: 0,
          responseTime: 0,
          accuracy: 0,
          createTime: new Date(Date.now() - 3600000)
        }
      ]
    },
    
    initCharts() {
      this.statusChart = echarts.init(this.$refs.statusChart)
      this.typeChart = echarts.init(this.$refs.typeChart)
      this.engineChart = echarts.init(this.$refs.engineChart)
      this.trendChart = echarts.init(this.$refs.trendChart)
      this.performanceChart = echarts.init(this.$refs.performanceChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },
    
    destroyCharts() {
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      if (this.typeChart) {
        this.typeChart.dispose()
      }
      if (this.engineChart) {
        this.engineChart.dispose()
      }
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      if (this.performanceChart) {
        this.performanceChart.dispose()
      }
      
      window.removeEventListener('resize', this.handleResize)
    },
    
    handleResize() {
      this.$nextTick(() => {
        if (this.statusChart) this.statusChart.resize()
        if (this.typeChart) this.typeChart.resize()
        if (this.engineChart) this.engineChart.resize()
        if (this.trendChart) this.trendChart.resize()
        if (this.performanceChart) this.performanceChart.resize()
      })
    },
    
    updateStatusChart(data) {
      if (!this.statusChart) return
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => item.status)
        },
        series: [
          {
            name: '检索状态',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data.map(item => ({
              value: item.count,
              name: item.status
            }))
          }
        ]
      }
      
      this.statusChart.setOption(option)
    },
    
    updateTypeChart(data) {
      if (!this.typeChart) return
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => formatSearchType(item.type))
        },
        series: [
          {
            name: '检索类型',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data.map(item => ({
              value: item.count,
              name: formatSearchType(item.type)
            }))
          }
        ]
      }
      
      this.typeChart.setOption(option)
    },
    
    updateEngineChart(data) {
      if (!this.engineChart) return
      
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
            data: data.map(item => item.engine),
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
            data: data.map(item => item.count)
          }
        ]
      }
      
      this.engineChart.setOption(option)
    },
    
    updateTrendChart(data) {
      if (!this.trendChart) return
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['检索次数', '成功次数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '检索次数',
            type: 'line',
            stack: 'Total',
            data: data.map(item => item.searchCount)
          },
          {
            name: '成功次数',
            type: 'line',
            stack: 'Total',
            data: data.map(item => item.successCount)
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    
    updatePerformanceChart(data) {
      if (!this.performanceChart) return
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['平均响应时间', '准确率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.map(item => item.date)
        },
        yAxis: [
          {
            type: 'value',
            name: '响应时间(ms)',
            position: 'left'
          },
          {
            type: 'value',
            name: '准确率(%)',
            position: 'right',
            min: 0,
            max: 100
          }
        ],
        series: [
          {
            name: '平均响应时间',
            type: 'line',
            yAxisIndex: 0,
            data: data.map(item => item.avgResponseTime)
          },
          {
            name: '准确率',
            type: 'line',
            yAxisIndex: 1,
            data: data.map(item => (item.accuracy * 100).toFixed(2))
          }
        ]
      }
      
      this.performanceChart.setOption(option)
    },
    
    getTrendClass(trend) {
      if (trend > 0) return 'trend-up'
      if (trend < 0) return 'trend-down'
      return 'trend-stable'
    },
    
    getTrendIcon(trend) {
      if (trend > 0) return 'el-icon-top'
      if (trend < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },
    
    getKeywordTagType(count) {
      if (count >= 100) return 'danger'
      if (count >= 50) return 'warning'
      if (count >= 20) return 'success'
      return 'info'
    },
    
    getOperationTagType(operation) {
      const typeMap = {
        'SEARCH': 'success',
        'INDEX': 'warning',
        'CREATE': 'primary',
        'UPDATE': 'info',
        'DELETE': 'danger'
      }
      return typeMap[operation] || 'info'
    },
    
    formatOperation(operation) {
      const operationMap = {
        'SEARCH': '检索',
        'INDEX': '索引',
        'CREATE': '创建',
        'UPDATE': '更新',
        'DELETE': '删除'
      }
      return operationMap[operation] || operation
    },
    
    handleViewDetail(row) {
      this.$router.push(`/managementAccountant/as/archiveSearch/detail/${row.searchId}`)
    },
    
    formatDateTime(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },
    
    // 导入格式化方法
    formatSearchType,
    formatAccuracy,
    formatResponseTime
  }
}
</script>

<style lang="scss" scoped>
.archive-search-dashboard {
  padding: 20px;
  
  .overview-stats {
    margin-bottom: 24px;
    
    .stats-card {
      display: flex;
      align-items: center;
      padding: 24px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      
      .stats-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
          font-size: 28px;
          color: white;
        }
        
        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.performance {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.accuracy {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stats-content {
        flex: 1;
        
        .stats-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .stats-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }
        
        .stats-trend {
          font-size: 12px;
          display: flex;
          align-items: center;
          
          i {
            margin-right: 4px;
          }
          
          &.trend-up {
            color: #67C23A;
          }
          
          &.trend-down {
            color: #F56C6C;
          }
          
          &.trend-stable {
            color: #909399;
          }
        }
      }
    }
  }
  
  .charts-area {
    margin-bottom: 24px;
    
    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        > span {
          display: flex;
          align-items: center;
          
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
      }
      
      .chart-container {
        height: 300px;
      }
      
      .chart-container-large {
        height: 400px;
      }
    }
  }
  
  .ranking-area {
    margin-bottom: 24px;
    
    .ranking-card {
      .ranking-list {
        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          
          &:last-child {
            border-bottom: none;
          }
          
          &.top-three {
            .ranking-number {
              color: white;
              
              &.rank-1 {
                background: linear-gradient(135deg, #FFD700, #FFA500);
              }
              
              &.rank-2 {
                background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
              }
              
              &.rank-3 {
                background: linear-gradient(135deg, #CD7F32, #B8860B);
              }
            }
          }
          
          .ranking-number {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
            margin-right: 12px;
            background: #f5f7fa;
            color: #909399;
          }
          
          .ranking-content {
            flex: 1;
            
            .ranking-name {
              font-size: 14px;
              color: #303133;
              margin-bottom: 2px;
            }
            
            .ranking-type {
              font-size: 12px;
              color: #909399;
            }
          }
          
          .ranking-value {
            font-size: 14px;
            font-weight: 600;
            color: #409EFF;
          }
        }
      }
      
      .keywords-list {
        .keyword-tag {
          margin: 4px 8px 4px 0;
          
          .keyword-count {
            margin-left: 4px;
            font-size: 12px;
          }
        }
      }
    }
  }
  
  .recent-activities {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      > span {
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }
    }
  }
}
</style>
