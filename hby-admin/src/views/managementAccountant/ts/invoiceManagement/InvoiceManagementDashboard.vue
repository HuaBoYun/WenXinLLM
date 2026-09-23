<template>
  <div class="invoice-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-cards">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon total">
              <i class="el-icon-document"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ overview.totalCount || 0 }}</div>
              <div class="stats-label">发票总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ formatAmount(overview.totalAmount) }}</div>
              <div class="stats-label">总金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon processing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ overview.processingCount || 0 }}</div>
              <div class="stats-label">处理中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ overview.highRiskCount || 0 }}</div>
              <div class="stats-label">高风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <!-- 发票状态分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>发票状态分布</span>
            <el-button type="text" @click="refreshStatusChart">刷新</el-button>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 发票类型分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>发票类型分布</span>
            <el-button type="text" @click="refreshTypeChart">刷新</el-button>
          </div>
          <div ref="typeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <!-- 月度金额趋势 -->
      <el-col :span="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>月度金额趋势</span>
            <div>
              <el-select v-model="trendYear" size="small" @change="refreshTrendChart">
                <el-option
                  v-for="year in yearOptions"
                  :key="year"
                  :label="year + '年'"
                  :value="year"
                />
              </el-select>
              <el-button type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container trend-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <!-- 风险等级分布 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>风险等级分布</span>
            <el-button type="text" @click="refreshRiskChart">刷新</el-button>
          </div>
          <div ref="riskChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 处理效率统计 -->
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>处理效率统计</span>
            <el-button type="text" @click="refreshEfficiencyChart">刷新</el-button>
          </div>
          <div ref="efficiencyChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待处理事项 -->
    <el-row :gutter="16" class="todo-row">
      <el-col :span="8">
        <el-card class="todo-card" shadow="never">
          <div slot="header" class="card-header">
            <span>待识别发票</span>
            <el-badge :value="pendingOcrCount" class="badge">
              <el-button type="text" @click="viewPendingOcr">查看全部</el-button>
            </el-badge>
          </div>
          <div class="todo-list">
            <div
              v-for="item in pendingOcrList"
              :key="item.invoiceId"
              class="todo-item"
              @click="viewInvoice(item)"
            >
              <div class="todo-info">
                <div class="todo-title">{{ item.invoiceCode }}-{{ item.invoiceNumber }}</div>
                <div class="todo-desc">{{ item.sellerName }}</div>
              </div>
              <div class="todo-time">{{ formatRelativeTime(item.createdTime) }}</div>
            </div>
            <div v-if="pendingOcrList.length === 0" class="empty-todo">
              <i class="el-icon-check"></i>
              <span>暂无待识别发票</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="todo-card" shadow="never">
          <div slot="header" class="card-header">
            <span>待验真发票</span>
            <el-badge :value="pendingVerificationCount" class="badge">
              <el-button type="text" @click="viewPendingVerification">查看全部</el-button>
            </el-badge>
          </div>
          <div class="todo-list">
            <div
              v-for="item in pendingVerificationList"
              :key="item.invoiceId"
              class="todo-item"
              @click="viewInvoice(item)"
            >
              <div class="todo-info">
                <div class="todo-title">{{ item.invoiceCode }}-{{ item.invoiceNumber }}</div>
                <div class="todo-desc">{{ item.sellerName }}</div>
              </div>
              <div class="todo-time">{{ formatRelativeTime(item.createdTime) }}</div>
            </div>
            <div v-if="pendingVerificationList.length === 0" class="empty-todo">
              <i class="el-icon-check"></i>
              <span>暂无待验真发票</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="todo-card" shadow="never">
          <div slot="header" class="card-header">
            <span>待归档发票</span>
            <el-badge :value="pendingArchiveCount" class="badge">
              <el-button type="text" @click="viewPendingArchive">查看全部</el-button>
            </el-badge>
          </div>
          <div class="todo-list">
            <div
              v-for="item in pendingArchiveList"
              :key="item.invoiceId"
              class="todo-item"
              @click="viewInvoice(item)"
            >
              <div class="todo-info">
                <div class="todo-title">{{ item.invoiceCode }}-{{ item.invoiceNumber }}</div>
                <div class="todo-desc">{{ item.sellerName }}</div>
              </div>
              <div class="todo-time">{{ formatRelativeTime(item.createdTime) }}</div>
            </div>
            <div v-if="pendingArchiveList.length === 0" class="empty-todo">
              <i class="el-icon-check"></i>
              <span>暂无待归档发票</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 发票详情对话框 -->
    <InvoiceManagementDetail
      :visible.sync="detailVisible"
      :invoice-id="currentInvoiceId"
      mode="view"
      @refresh="loadData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getInvoiceOverview,
  countInvoicesByStatus,
  countInvoicesByType,
  countInvoicesByRiskLevel,
  sumAmountByMonth,
  getProcessingEfficiencyStats,
  getPendingOcrInvoices,
  getPendingVerificationInvoices,
  getPendingArchiveInvoices,
  utils
} from '@/api/managementAccountant/ts/invoiceManagement'
import InvoiceManagementDetail from './InvoiceManagementDetail'

export default {
  name: 'InvoiceManagementDashboard',
  components: {
    InvoiceManagementDetail
  },
  data() {
    return {
      loading: false,
      overview: {},
      trendYear: new Date().getFullYear(),
      yearOptions: [],
      
      // 图表实例
      statusChart: null,
      typeChart: null,
      trendChart: null,
      riskChart: null,
      efficiencyChart: null,
      
      // 待处理数据
      pendingOcrList: [],
      pendingOcrCount: 0,
      pendingVerificationList: [],
      pendingVerificationCount: 0,
      pendingArchiveList: [],
      pendingArchiveCount: 0,
      
      // 详情对话框
      detailVisible: false,
      currentInvoiceId: null
    }
  },
  computed: {
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  mounted() {
    this.initYearOptions()
    this.loadData()
    this.initCharts()
    
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    // 销毁图表实例
    if (this.statusChart) this.statusChart.dispose()
    if (this.typeChart) this.typeChart.dispose()
    if (this.trendChart) this.trendChart.dispose()
    if (this.riskChart) this.riskChart.dispose()
    if (this.efficiencyChart) this.efficiencyChart.dispose()
    
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    // 初始化年份选项
    initYearOptions() {
      const currentYear = new Date().getFullYear()
      this.yearOptions = []
      for (let i = currentYear - 2; i <= currentYear; i++) {
        this.yearOptions.push(i)
      }
    },

    // 加载数据
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadOverview(),
          this.loadPendingTasks()
        ])
        
        // 加载图表数据
        await Promise.all([
          this.loadStatusChart(),
          this.loadTypeChart(),
          this.loadTrendChart(),
          this.loadRiskChart(),
          this.loadEfficiencyChart()
        ])
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 加载概览数据
    async loadOverview() {
      try {
        const response = await getInvoiceOverview(this.tenantId)
        if (response.success) {
          this.overview = response.data
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },

    // 加载待处理任务
    async loadPendingTasks() {
      try {
        const [ocrResponse, verificationResponse, archiveResponse] = await Promise.all([
          getPendingOcrInvoices(this.tenantId, 5),
          getPendingVerificationInvoices(this.tenantId, 5),
          getPendingArchiveInvoices(this.tenantId, 5)
        ])
        
        if (ocrResponse.success) {
          this.pendingOcrList = ocrResponse.data.slice(0, 5)
          this.pendingOcrCount = ocrResponse.data.length
        }
        
        if (verificationResponse.success) {
          this.pendingVerificationList = verificationResponse.data.slice(0, 5)
          this.pendingVerificationCount = verificationResponse.data.length
        }
        
        if (archiveResponse.success) {
          this.pendingArchiveList = archiveResponse.data.slice(0, 5)
          this.pendingArchiveCount = archiveResponse.data.length
        }
      } catch (error) {
        console.error('加载待处理任务失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.statusChart = echarts.init(this.$refs.statusChart)
        this.typeChart = echarts.init(this.$refs.typeChart)
        this.trendChart = echarts.init(this.$refs.trendChart)
        this.riskChart = echarts.init(this.$refs.riskChart)
        this.efficiencyChart = echarts.init(this.$refs.efficiencyChart)
      })
    },

    // 加载状态分布图表
    async loadStatusChart() {
      try {
        const response = await countInvoicesByStatus(this.tenantId)
        if (response.success && this.statusChart) {
          const data = response.data.map(item => ({
            name: utils.formatInvoiceStatus(item.status).text,
            value: item.count
          }))
          
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [{
              name: '发票状态',
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }]
          }
          
          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载状态分布图表失败:', error)
      }
    },

    // 加载类型分布图表
    async loadTypeChart() {
      try {
        const response = await countInvoicesByType(this.tenantId)
        if (response.success && this.typeChart) {
          const data = response.data.map(item => ({
            name: utils.formatInvoiceType(item.type),
            value: item.count
          }))
          
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [{
              name: '发票类型',
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }]
          }
          
          this.typeChart.setOption(option)
        }
      } catch (error) {
        console.error('加载类型分布图表失败:', error)
      }
    },

    // 加载趋势图表
    async loadTrendChart() {
      try {
        const response = await sumAmountByMonth(this.tenantId, this.trendYear)
        if (response.success && this.trendChart) {
          const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
          const data = new Array(12).fill(0)
          
          response.data.forEach(item => {
            const monthIndex = parseInt(item.month) - 1
            if (monthIndex >= 0 && monthIndex < 12) {
              data[monthIndex] = item.totalAmount || 0
            }
          })
          
          const option = {
            tooltip: {
              trigger: 'axis',
              formatter: function(params) {
                return params[0].name + '<br/>' + 
                       params[0].seriesName + ': ¥' + utils.formatAmount(params[0].value)
              }
            },
            xAxis: {
              type: 'category',
              data: months
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                formatter: function(value) {
                  return '¥' + utils.formatAmount(value)
                }
              }
            },
            series: [{
              name: '发票金额',
              type: 'line',
              data: data,
              smooth: true,
              itemStyle: {
                color: '#409EFF'
              },
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: 'rgba(64, 158, 255, 0.3)'
                  }, {
                    offset: 1, color: 'rgba(64, 158, 255, 0.1)'
                  }]
                }
              }
            }]
          }
          
          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('加载趋势图表失败:', error)
      }
    },

    // 加载风险分布图表
    async loadRiskChart() {
      try {
        const response = await countInvoicesByRiskLevel(this.tenantId)
        if (response.success && this.riskChart) {
          const data = response.data.map(item => ({
            name: utils.formatRiskLevel(item.riskLevel).text,
            value: item.count
          }))
          
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [{
              name: '风险等级',
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }]
          }
          
          this.riskChart.setOption(option)
        }
      } catch (error) {
        console.error('加载风险分布图表失败:', error)
      }
    },

    // 加载效率统计图表
    async loadEfficiencyChart() {
      try {
        const response = await getProcessingEfficiencyStats(this.tenantId)
        if (response.success && this.efficiencyChart) {
          const data = response.data
          
          const option = {
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            legend: {
              data: ['OCR识别率', '验真成功率', '归档成功率']
            },
            xAxis: {
              type: 'category',
              data: ['处理效率']
            },
            yAxis: {
              type: 'value',
              max: 100,
              axisLabel: {
                formatter: '{value}%'
              }
            },
            series: [
              {
                name: 'OCR识别率',
                type: 'bar',
                data: [data.ocrSuccessRate * 100],
                itemStyle: { color: '#67C23A' }
              },
              {
                name: '验真成功率',
                type: 'bar',
                data: [data.verificationSuccessRate * 100],
                itemStyle: { color: '#409EFF' }
              },
              {
                name: '归档成功率',
                type: 'bar',
                data: [data.archiveSuccessRate * 100],
                itemStyle: { color: '#E6A23C' }
              }
            ]
          }
          
          this.efficiencyChart.setOption(option)
        }
      } catch (error) {
        console.error('加载效率统计图表失败:', error)
      }
    },

    // 刷新图表
    refreshStatusChart() {
      this.loadStatusChart()
    },
    
    refreshTypeChart() {
      this.loadTypeChart()
    },
    
    refreshTrendChart() {
      this.loadTrendChart()
    },
    
    refreshRiskChart() {
      this.loadRiskChart()
    },
    
    refreshEfficiencyChart() {
      this.loadEfficiencyChart()
    },

    // 处理窗口大小变化
    handleResize() {
      this.$nextTick(() => {
        if (this.statusChart) this.statusChart.resize()
        if (this.typeChart) this.typeChart.resize()
        if (this.trendChart) this.trendChart.resize()
        if (this.riskChart) this.riskChart.resize()
        if (this.efficiencyChart) this.efficiencyChart.resize()
      })
    },

    // 查看发票详情
    viewInvoice(invoice) {
      this.currentInvoiceId = invoice.invoiceId
      this.detailVisible = true
    },

    // 查看待处理列表
    viewPendingOcr() {
      this.$router.push({
        path: '/managementAccountant/ts/invoiceManagement',
        query: { ocrStatus: 'PENDING' }
      })
    },
    
    viewPendingVerification() {
      this.$router.push({
        path: '/managementAccountant/ts/invoiceManagement',
        query: { verificationStatus: 'PENDING' }
      })
    },
    
    viewPendingArchive() {
      this.$router.push({
        path: '/managementAccountant/ts/invoiceManagement',
        query: { archiveStatus: 'PENDING' }
      })
    },

    // 格式化方法
    formatAmount: utils.formatAmount,
    
    // 格式化相对时间
    formatRelativeTime(time) {
      if (!time) return '-'
      return this.$moment(time).fromNow()
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-dashboard {
  .stats-cards {
    margin-bottom: 16px;
  }

  .stats-card {
    .stats-content {
      display: flex;
      align-items: center;
      
      .stats-icon {
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
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.amount {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.processing {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.risk {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
      }
      
      .stats-info {
        flex: 1;
        
        .stats-number {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1;
        }
        
        .stats-label {
          font-size: 14px;
          color: #909399;
          margin-top: 4px;
        }
      }
    }
  }

  .chart-row {
    margin-bottom: 16px;
  }

  .chart-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .chart-container {
      height: 300px;
      
      &.trend-chart {
        height: 400px;
      }
    }
  }

  .todo-row {
    margin-bottom: 16px;
  }

  .todo-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .badge {
        margin-left: 8px;
      }
    }
    
    .todo-list {
      max-height: 300px;
      overflow-y: auto;
      
      .todo-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        cursor: pointer;
        transition: background-color 0.3s;
        
        &:hover {
          background-color: #f5f7fa;
        }
        
        &:last-child {
          border-bottom: none;
        }
        
        .todo-info {
          flex: 1;
          
          .todo-title {
            font-size: 14px;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .todo-desc {
            font-size: 12px;
            color: #909399;
          }
        }
        
        .todo-time {
          font-size: 12px;
          color: #c0c4cc;
        }
      }
      
      .empty-todo {
        text-align: center;
        padding: 40px 0;
        color: #c0c4cc;
        
        i {
          font-size: 48px;
          margin-bottom: 16px;
          display: block;
        }
      }
    }
  }
}
</style>
