<template>
  <div class="tax-declaration-dashboard" v-loading="loading">
    <!-- 时间筛选器 -->
    <div class="filter-section">
      <el-form :model="filterForm" :inline="true" size="small">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px"
            value-format="yyyy-MM-dd HH:mm:ss"
            @change="loadDashboardData"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadDashboardData">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="resetFilter">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 概览统计卡片 -->
    <div class="overview-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.totalCount || 0 }}</div>
              <div class="stat-label">总申报数</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-clock"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.pendingCount || 0 }}</div>
              <div class="stat-label">待处理</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overview.overdueCount || 0 }}</div>
              <div class="stat-label">逾期申报</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 申报状态分布 -->
        <el-col :span="12">
          <el-card header="申报状态分布" class="chart-card">
            <div ref="statusChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 税种分布 -->
        <el-col :span="12">
          <el-card header="税种分布" class="chart-card">
            <div ref="taxTypeChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <!-- 申报趋势 -->
        <el-col :span="24">
          <el-card header="申报趋势" class="chart-card">
            <div class="chart-toolbar">
              <el-radio-group v-model="trendGroupBy" size="small" @change="loadTrendData">
                <el-radio-button label="day">按天</el-radio-button>
                <el-radio-button label="week">按周</el-radio-button>
                <el-radio-button label="month">按月</el-radio-button>
              </el-radio-group>
            </div>
            <div ref="trendChart" class="chart-container large"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <!-- 税额统计 -->
        <el-col :span="12">
          <el-card header="税额统计" class="chart-card">
            <div ref="taxAmountChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 效率分析 -->
        <el-col :span="12">
          <el-card header="效率分析" class="chart-card">
            <div class="efficiency-metrics">
              <div class="metric-item">
                <div class="metric-label">申报及时率</div>
                <div class="metric-value success">{{ (efficiencyStats.timelyRate * 100).toFixed(1) }}%</div>
              </div>
              <div class="metric-item">
                <div class="metric-label">申报成功率</div>
                <div class="metric-value primary">{{ (efficiencyStats.successRate * 100).toFixed(1) }}%</div>
              </div>
              <div class="metric-item">
                <div class="metric-label">平均处理时长</div>
                <div class="metric-value warning">{{ efficiencyStats.avgProcessingTime }}小时</div>
              </div>
              <div class="metric-item">
                <div class="metric-label">合规率</div>
                <div class="metric-value info">{{ (efficiencyStats.complianceRate * 100).toFixed(1) }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细统计表格 -->
    <div class="table-section">
      <el-card header="详细统计" class="table-card">
        <el-tabs v-model="activeStatsTab" @tab-click="handleStatsTabClick">
          <el-tab-pane label="按状态统计" name="status">
            <el-table :data="statusStats" stripe>
              <el-table-column prop="status" label="申报状态" width="150">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTagType(scope.row.status)">
                    {{ getDeclarationStatusLabel(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="count" label="数量" width="100" align="right" />
              <el-table-column prop="percentage" label="占比" width="100" align="right">
                <template slot-scope="scope">
                  {{ (scope.row.percentage * 100).toFixed(1) }}%
                </template>
              </el-table-column>
              <el-table-column prop="taxAmount" label="税额合计" align="right">
                <template slot-scope="scope">
                  ¥{{ formatTaxAmount(scope.row.taxAmount) }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="按税种统计" name="taxType">
            <el-table :data="taxTypeStats" stripe>
              <el-table-column prop="taxType" label="税种" width="150">
                <template slot-scope="scope">
                  <el-tag type="info">
                    {{ getTaxTypeLabel(scope.row.taxType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="count" label="数量" width="100" align="right" />
              <el-table-column prop="percentage" label="占比" width="100" align="right">
                <template slot-scope="scope">
                  {{ (scope.row.percentage * 100).toFixed(1) }}%
                </template>
              </el-table-column>
              <el-table-column prop="taxAmount" label="税额合计" align="right">
                <template slot-scope="scope">
                  ¥{{ formatTaxAmount(scope.row.taxAmount) }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="按类型统计" name="declarationType">
            <el-table :data="typeStats" stripe>
              <el-table-column prop="declarationType" label="申报类型" width="150">
                <template slot-scope="scope">
                  {{ getDeclarationTypeLabel(scope.row.declarationType) }}
                </template>
              </el-table-column>
              <el-table-column prop="count" label="数量" width="100" align="right" />
              <el-table-column prop="percentage" label="占比" width="100" align="right">
                <template slot-scope="scope">
                  {{ (scope.row.percentage * 100).toFixed(1) }}%
                </template>
              </el-table-column>
              <el-table-column prop="taxAmount" label="税额合计" align="right">
                <template slot-scope="scope">
                  ¥{{ formatTaxAmount(scope.row.taxAmount) }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getDeclarationOverview,
  countDeclarationsByStatus,
  countDeclarationsByTaxType,
  countDeclarationsByType,
  getDeclarationTrend,
  getTaxAmountTrend,
  getDeclarationEfficiencyStats,
  getTaxTypeLabel,
  getDeclarationStatusLabel,
  getDeclarationTypeLabel,
  formatTaxAmount
} from '@/api/managementAccountant/ts/taxDeclaration'

export default {
  name: 'TaxDeclarationDashboard',
  data() {
    return {
      loading: false,
      
      // 筛选表单
      filterForm: {
        dateRange: null
      },
      
      // 概览数据
      overview: {},
      
      // 统计数据
      statusStats: [],
      taxTypeStats: [],
      typeStats: [],
      trendData: [],
      taxAmountData: [],
      efficiencyStats: {
        timelyRate: 0,
        successRate: 0,
        avgProcessingTime: 0,
        complianceRate: 0
      },
      
      // 图表实例
      statusChart: null,
      taxTypeChart: null,
      trendChart: null,
      taxAmountChart: null,
      
      // 界面状态
      activeStatsTab: 'status',
      trendGroupBy: 'month'
    }
  },
  mounted() {
    this.initCharts()
    this.loadDashboardData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        if (this.$refs.statusChart) {
          this.statusChart = echarts.init(this.$refs.statusChart)
        }
        if (this.$refs.taxTypeChart) {
          this.taxTypeChart = echarts.init(this.$refs.taxTypeChart)
        }
        if (this.$refs.trendChart) {
          this.trendChart = echarts.init(this.$refs.trendChart)
        }
        if (this.$refs.taxAmountChart) {
          this.taxAmountChart = echarts.init(this.$refs.taxAmountChart)
        }
        
        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize)
      })
    },
    
    // 销毁图表
    destroyCharts() {
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      if (this.taxTypeChart) {
        this.taxTypeChart.dispose()
      }
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      if (this.taxAmountChart) {
        this.taxAmountChart.dispose()
      }
      
      window.removeEventListener('resize', this.handleResize)
    },
    
    // 处理窗口大小变化
    handleResize() {
      if (this.statusChart) this.statusChart.resize()
      if (this.taxTypeChart) this.taxTypeChart.resize()
      if (this.trendChart) this.trendChart.resize()
      if (this.taxAmountChart) this.taxAmountChart.resize()
    },
    
    // 加载仪表板数据
    async loadDashboardData() {
      this.loading = true
      try {
        const params = this.getQueryParams()
        
        // 并行加载所有数据
        const [
          overviewResponse,
          statusResponse,
          taxTypeResponse,
          typeResponse,
          efficiencyResponse
        ] = await Promise.all([
          getDeclarationOverview(this.$store.getters.tenantId, params),
          countDeclarationsByStatus(this.$store.getters.tenantId, params),
          countDeclarationsByTaxType(this.$store.getters.tenantId, params),
          countDeclarationsByType(this.$store.getters.tenantId, params),
          getDeclarationEfficiencyStats(this.$store.getters.tenantId, params)
        ])
        
        // 处理响应数据
        if (overviewResponse.success) {
          this.overview = overviewResponse.data || {}
        }
        
        if (statusResponse.success) {
          this.statusStats = statusResponse.data || []
          this.renderStatusChart()
        }
        
        if (taxTypeResponse.success) {
          this.taxTypeStats = taxTypeResponse.data || []
          this.renderTaxTypeChart()
        }
        
        if (typeResponse.success) {
          this.typeStats = typeResponse.data || []
        }
        
        if (efficiencyResponse.success) {
          this.efficiencyStats = {
            timelyRate: efficiencyResponse.data.timelyRate || 0,
            successRate: efficiencyResponse.data.successRate || 0,
            avgProcessingTime: efficiencyResponse.data.avgProcessingTime || 0,
            complianceRate: efficiencyResponse.data.complianceRate || 0
          }
        }
        
        // 加载趋势数据
        await this.loadTrendData()
        
      } catch (error) {
        console.error('加载仪表板数据失败:', error)
        this.$message.error('加载仪表板数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载趋势数据
    async loadTrendData() {
      try {
        const params = {
          ...this.getQueryParams(),
          groupBy: this.trendGroupBy
        }
        
        const [trendResponse, taxAmountResponse] = await Promise.all([
          getDeclarationTrend(this.$store.getters.tenantId, params),
          getTaxAmountTrend(this.$store.getters.tenantId, params)
        ])
        
        if (trendResponse.success) {
          this.trendData = trendResponse.data || []
          this.renderTrendChart()
        }
        
        if (taxAmountResponse.success) {
          this.taxAmountData = taxAmountResponse.data || []
          this.renderTaxAmountChart()
        }
      } catch (error) {
        console.error('加载趋势数据失败:', error)
      }
    },
    
    // 获取查询参数
    getQueryParams() {
      const params = {}
      if (this.filterForm.dateRange && this.filterForm.dateRange.length === 2) {
        params.startDate = this.filterForm.dateRange[0]
        params.endDate = this.filterForm.dateRange[1]
      }
      return params
    },
    
    // 重置筛选器
    resetFilter() {
      this.filterForm.dateRange = null
      this.loadDashboardData()
    },
    
    // 渲染状态分布图表
    renderStatusChart() {
      if (!this.statusChart || !this.statusStats.length) return
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: this.statusStats.map(item => this.getDeclarationStatusLabel(item.status))
        },
        series: [
          {
            name: '申报状态',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['60%', '50%'],
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
            data: this.statusStats.map(item => ({
              value: item.count,
              name: this.getDeclarationStatusLabel(item.status)
            }))
          }
        ]
      }
      
      this.statusChart.setOption(option)
    },
    
    // 渲染税种分布图表
    renderTaxTypeChart() {
      if (!this.taxTypeChart || !this.taxTypeStats.length) return
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: this.taxTypeStats.map(item => this.getTaxTypeLabel(item.taxType))
        },
        series: [
          {
            name: '税种分布',
            type: 'pie',
            radius: '70%',
            center: ['60%', '50%'],
            data: this.taxTypeStats.map(item => ({
              value: item.count,
              name: this.getTaxTypeLabel(item.taxType)
            }))
          }
        ]
      }
      
      this.taxTypeChart.setOption(option)
    },
    
    // 渲染趋势图表
    renderTrendChart() {
      if (!this.trendChart || !this.trendData.length) return
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['申报数量']
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
          data: this.trendData.map(item => item.period)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '申报数量',
            type: 'line',
            stack: '总量',
            data: this.trendData.map(item => item.count)
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    
    // 渲染税额统计图表
    renderTaxAmountChart() {
      if (!this.taxAmountChart || !this.taxAmountData.length) return
      
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: function(params) {
            return params[0].name + '<br/>' +
                   params[0].seriesName + ': ¥' + 
                   Number(params[0].value).toLocaleString('zh-CN', {
                     minimumFractionDigits: 2,
                     maximumFractionDigits: 2
                   })
          }
        },
        legend: {
          data: ['税额']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.taxAmountData.map(item => item.period)
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: function(value) {
              return '¥' + (value / 10000).toFixed(0) + '万'
            }
          }
        },
        series: [
          {
            name: '税额',
            type: 'bar',
            data: this.taxAmountData.map(item => item.amount)
          }
        ]
      }
      
      this.taxAmountChart.setOption(option)
    },
    
    // 统计标签页点击
    handleStatsTabClick(tab) {
      // 可以在这里加载对应的数据
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'FILLING': 'warning',
        'FILLED': 'info',
        'SUBMITTING': 'warning',
        'SUBMITTED': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'COMPLETED': 'success',
        'WITHDRAWN': '',
        'FILL_FAILED': 'danger',
        'SUBMIT_FAILED': 'danger'
      }
      return typeMap[status] || ''
    },
    
    // 工具方法
    getTaxTypeLabel,
    getDeclarationStatusLabel,
    getDeclarationTypeLabel,
    formatTaxAmount
  }
}
</script>

<style lang="scss" scoped>
.tax-declaration-dashboard {
  padding: 20px;
  
  .filter-section {
    margin-bottom: 20px;
    padding: 16px;
    background: white;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .overview-cards {
    margin-bottom: 20px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      
      .stat-icon {
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
        
        &.total {
          background: linear-gradient(135deg, #667eea, #764ba2);
        }
        
        &.completed {
          background: linear-gradient(135deg, #67C23A, #85ce61);
        }
        
        &.pending {
          background: linear-gradient(135deg, #E6A23C, #f0b90b);
        }
        
        &.overdue {
          background: linear-gradient(135deg, #F56C6C, #f78989);
        }
      }
      
      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
  
  .charts-section {
    margin-bottom: 20px;
    
    .chart-card {
      margin-bottom: 20px;
      
      .chart-toolbar {
        text-align: right;
        margin-bottom: 16px;
      }
      
      .chart-container {
        height: 300px;
        
        &.large {
          height: 400px;
        }
      }
      
      .efficiency-metrics {
        display: flex;
        justify-content: space-around;
        align-items: center;
        height: 300px;
        
        .metric-item {
          text-align: center;
          
          .metric-label {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          
          .metric-value {
            font-size: 32px;
            font-weight: 600;
            
            &.success {
              color: #67C23A;
            }
            
            &.primary {
              color: #409EFF;
            }
            
            &.warning {
              color: #E6A23C;
            }
            
            &.info {
              color: #909399;
            }
          }
        }
      }
    }
  }
  
  .table-section {
    .table-card {
      .el-table {
        margin-top: 16px;
      }
    }
  }
}
</style>
