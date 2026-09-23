<template>
  <div class="cost-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2 class="page-title">报表管理分析</h2>
      <div class="header-actions">
        <el-date-picker
          v-model="currentPeriod"
          type="month"
          placeholder="选择查询期间"
          format="yyyy年MM月"
          value-format="yyyy-MM"
          size="small"
          @change="handlePeriodChange"
        />
        <el-button type="primary" size="small" icon="el-icon-refresh" @click="refreshData">
          刷新
        </el-button>
        <el-button type="success" size="small" icon="el-icon-download" @click="handleExport">
          导出报告
        </el-button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-loading="loading" element-loading-text="数据加载中...">
      <!-- 五大模块统计卡片 -->
      <el-row :gutter="24" class="stats-cards">
        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <div class="stat-card">
            <div class="stat-icon cost-center">
              <i class="el-icon-s-cooperation" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(overview.costCenters) }}</div>
              <div class="stat-label">成本中心数量</div>
              <div class="stat-trend" :class="getTrendClass(overview.costCentersTrend)">
                <i :class="getTrendIcon(overview.costCentersTrend)" />
                <span>{{ overview.costCentersTrend || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <div class="stat-card">
            <div class="stat-icon product-cost">
              <i class="el-icon-goods" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(overview.totalProductCost) }}</div>
              <div class="stat-label">产品成本总额</div>
              <div class="stat-trend" :class="getTrendClass(overview.productCostTrend)">
                <i :class="getTrendIcon(overview.productCostTrend)" />
                <span>{{ overview.productCostTrend || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <div class="stat-card">
            <div class="stat-icon cost-estimate">
              <i class="el-icon-data-analysis" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(overview.costEstimates) }}</div>
              <div class="stat-label">成本估算次数</div>
              <div class="stat-trend" :class="getTrendClass(overview.estimatesTrend)">
                <i :class="getTrendIcon(overview.estimatesTrend)" />
                <span>{{ overview.estimatesTrend || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <div class="stat-card">
            <div class="stat-icon special-cost">
              <i class="el-icon-s-flag" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(overview.specialProjects) }}</div>
              <div class="stat-label">专项成本项目</div>
              <div class="stat-trend" :class="getTrendClass(overview.specialTrend)">
                <i :class="getTrendIcon(overview.specialTrend)" />
                <span>{{ overview.specialTrend || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
          <div class="stat-card">
            <div class="stat-icon internal-settlement">
              <i class="el-icon-s-finance" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(overview.internalSettlements) }}</div>
              <div class="stat-label">内部结算次数</div>
              <div class="stat-trend" :class="getTrendClass(overview.settlementTrend)">
                <i :class="getTrendIcon(overview.settlementTrend)" />
                <span>{{ overview.settlementTrend || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="24" class="charts-row">
        <!-- 成本趋势分析 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
          <div class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">成本趋势分析</h3>
              <div class="chart-controls">
                <el-select
                  v-model="trendPeriod"
                  size="small"
                  style="width: 100px; margin-right: 12px"
                  @change="loadCostTrend"
                >
                  <el-option label="月度" value="month" />
                  <el-option label="季度" value="quarter" />
                  <el-option label="年度" value="year" />
                </el-select>
                <el-checkbox v-model="showYearOverYear" @change="loadCostTrend">
                  同比
                </el-checkbox>
              </div>
            </div>
            <div id="costTrendChart" class="chart-container" />
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" class="charts-row">
        <!-- 预算执行分析 -->
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
          <div class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">预算执行分析</h3>
              <div class="chart-controls">
                <el-select
                  v-model="budgetCenterType"
                  size="small"
                  style="width: 150px"
                  placeholder="选择中心类型"
                  clearable
                  @change="loadBudgetExecution"
                >
                  <el-option label="生产成本中心" value="production" />
                  <el-option label="研发成本中心" value="research" />
                  <el-option label="销售成本中心" value="sales" />
                  <el-option label="管理成本中心" value="management" />
                </el-select>
              </div>
            </div>
            <div id="budgetExecutionChart" class="chart-container" />
          </div>
        </el-col>
      </el-row>

      <!-- 异常预警列表 -->
      <el-row :gutter="24" class="alerts-row">
        <el-col :span="24">
          <div class="table-card">
            <div class="table-header">
              <h3 class="table-title">异常预警</h3>
              <div class="table-controls">
                <el-select
                  v-model="alertLevel"
                  size="small"
                  style="width: 120px; margin-right: 12px"
                  placeholder="预警级别"
                  clearable
                  @change="loadAlerts"
                >
                  <el-option label="高风险" value="high" />
                  <el-option label="中风险" value="medium" />
                  <el-option label="低风险" value="low" />
                </el-select>
                <el-select
                  v-model="alertStatus"
                  size="small"
                  style="width: 120px"
                  placeholder="处理状态"
                  clearable
                  @change="loadAlerts"
                >
                  <el-option label="未处理" value="pending" />
                  <el-option label="处理中" value="processing" />
                  <el-option label="已处理" value="resolved" />
                </el-select>
              </div>
            </div>

            <el-table
              :data="alerts"
              style="width: 100%"
              :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="alertType" label="预警类型" width="150">
                <template slot-scope="scope">
                  <el-tag :type="getAlertTypeTag(scope.row.alertType)" size="small">
                    {{ scope.row.alertType }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="预警描述" min-width="200" />
              <el-table-column prop="level" label="风险级别" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getLevelTag(scope.row.level)" size="small">
                    {{ getLevelText(scope.row.level) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="costCenter" label="成本中心" width="150" />
              <el-table-column prop="amount" label="涉及金额" width="150">
                <template slot-scope="scope">
                  <span style="color: #f56c6c; font-weight: 600">
                    {{ formatAmount(scope.row.amount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="预警时间" width="180" />
              <el-table-column prop="status" label="处理状态" width="120">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)" size="small">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180" fixed="right">
                <template slot-scope="scope">
                  <el-button
                    v-if="scope.row.status === 'pending'"
                    type="text"
                    size="small"
                    icon="el-icon-check"
                    @click="handleResolveAlert(scope.row)"
                  >
                    标记处理
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    icon="el-icon-view"
                    @click="handleViewDetail(scope.row)"
                  >
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="pagination-container">
              <el-pagination
                :current-page="alertQuery.pageNo"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="alertQuery.pageSize"
                :total="alertQuery.totalRecord"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import * as ManagementAPI from '@/api/financialSharing/reportsManagement'

export default {
  name: 'CostManagementAnalysis',
  data() {
    return {
      loading: false,
      currentPeriod: '',
      // 管理概览数据
      overview: {
        costCenters: 0,
        costCentersTrend: 0,
        totalProductCost: 0,
        productCostTrend: 0,
        costEstimates: 0,
        estimatesTrend: 0,
        specialProjects: 0,
        specialTrend: 0,
        internalSettlements: 0,
        settlementTrend: 0
      },
      // 成本趋势配置
      trendPeriod: 'month',
      showYearOverYear: false,
      costTrendChart: null,
      // 预算执行配置
      budgetCenterType: '',
      budgetExecutionChart: null,
      // 异常预警
      alertLevel: '',
      alertStatus: '',
      alerts: [],
      alertQuery: {
        pageNo: 1,
        pageSize: 10,
        totalRecord: 0
      },
      // 图表实例
      charts: []
    }
  },
  mounted() {
    // 初始化默认查询期间为当前月份
    const now = new Date()
    this.currentPeriod = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    // 加载所有数据
    this.loadAllData()
    // 窗口大小改变时重绘图表
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    // 销毁所有图表实例
    this.charts.forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
    this.charts = []
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    /**
     * 加载所有数据
     */
    async loadAllData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadOverview(),
          this.loadCostTrend(),
          this.loadBudgetExecution(),
          this.loadAlerts()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('数据加载失败,请稍后重试')
      } finally {
        this.loading = false
      }
    },

    /**
     * 加载管理概览统计
     */
    async loadOverview() {
      try {
        const res = await ManagementAPI.getManagementOverview({
          period: this.currentPeriod
        })
        if (res.code === 1) {
          this.overview = res.data || {}
        } else {
          this.overview = {}
        }
      } catch (error) {
        console.error('获取管理概览失败:', error)
        this.overview = {}
      }
    },

    /**
     * 加载成本趋势分析
     */
    async loadCostTrend() {
      try {
        const res = await ManagementAPI.getManagementCostTrend({
          period: this.currentPeriod,
          periodType: this.trendPeriod,
          showYearOverYear: this.showYearOverYear
        })
        if (res.code === 1) {
          this.renderCostTrendChart(res.data || { xAxis: [], actual: [], budget: [], lastYear: [] })
        } else {
          this.$message.warning('获取成本趋势数据失败')
        }
      } catch (error) {
        console.error('获取成本趋势失败:', error)
      }
    },

    /**
     * 加载预算执行分析
     */
    async loadBudgetExecution() {
      try {
        const res = await ManagementAPI.getManagementBudgetExecution({
          period: this.currentPeriod,
          centerType: this.budgetCenterType
        })
        if (res.code === 1) {
          this.renderBudgetExecutionChart(res.data || { xAxis: [], budget: [], actual: [], rate: [] })
        } else {
          this.$message.warning('获取预算执行数据失败')
        }
      } catch (error) {
        console.error('获取预算执行失败:', error)
      }
    },

    /**
     * 加载异常预警列表
     */
    async loadAlerts() {
      try {
        const res = await ManagementAPI.getManagementAlerts({
          period: this.currentPeriod,
          level: this.alertLevel,
          status: this.alertStatus,
          pageNo: this.alertQuery.pageNo,
          pageSize: this.alertQuery.pageSize
        })
        if (res.code === 1) {
          this.alerts = res.data.tlist || []
          this.alertQuery.totalRecord = res.data.totalRecord || 0
        } else {
          this.alerts = []
          this.alertQuery.totalRecord = 0
        }
      } catch (error) {
        console.error('获取异常预警失败:', error)
        this.alerts = []
        this.alertQuery.totalRecord = 0
      }
    },

    /**
     * 渲染成本趋势图表
     */
    renderCostTrendChart(data) {
      const chartDom = document.getElementById('costTrendChart')
      if (!chartDom) return

      if (this.costTrendChart) {
        this.costTrendChart.dispose()
      }

      this.costTrendChart = echarts.init(chartDom)
      this.charts.push(this.costTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['实际成本', '预算成本', '去年同期']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.xAxis || ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)'
        },
        series: [
          {
            name: '实际成本',
            type: 'line',
            smooth: true,
            data: data.actual || [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330],
            itemStyle: {
              color: '#409eff'
            }
          },
          {
            name: '预算成本',
            type: 'line',
            smooth: true,
            data: data.budget || [150, 150, 140, 150, 130, 250, 230, 200, 210, 250, 300, 350],
            itemStyle: {
              color: '#67c23a'
            }
          },
          {
            name: '去年同期',
            type: 'line',
            smooth: true,
            data: data.lastYear || [100, 120, 90, 120, 80, 200, 180, 160, 170, 210, 260, 300],
            itemStyle: {
              color: '#e6a23c'
            }
          }
        ]
      }

      this.costTrendChart.setOption(option)
    },

    /**
     * 渲染预算执行图表
     */
    renderBudgetExecutionChart(data) {
      const chartDom = document.getElementById('budgetExecutionChart')
      if (!chartDom) return

      if (this.budgetExecutionChart) {
        this.budgetExecutionChart.dispose()
      }

      this.budgetExecutionChart = echarts.init(chartDom)
      this.charts.push(this.budgetExecutionChart)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['预算金额', '实际执行', '执行率']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: data.xAxis || ['成本中心A', '成本中心B', '成本中心C', '成本中心D', '成本中心E']
        },
        yAxis: [
          {
            type: 'value',
            name: '金额(万元)',
            position: 'left'
          },
          {
            type: 'value',
            name: '执行率(%)',
            position: 'right',
            axisLabel: {
              formatter: '{value}%'
            }
          }
        ],
        series: [
          {
            name: '预算金额',
            type: 'bar',
            data: data.budget || [500, 600, 450, 700, 550],
            itemStyle: {
              color: '#409eff'
            }
          },
          {
            name: '实际执行',
            type: 'bar',
            data: data.actual || [480, 580, 430, 720, 530],
            itemStyle: {
              color: '#67c23a'
            }
          },
          {
            name: '执行率',
            type: 'line',
            yAxisIndex: 1,
            data: data.rate || [96, 96.7, 95.6, 102.9, 96.4],
            itemStyle: {
              color: '#e6a23c'
            }
          }
        ]
      }

      this.budgetExecutionChart.setOption(option)
    },

    /**
     * 处理期间变更
     */
    handlePeriodChange() {
      this.loadAllData()
    },

    /**
     * 刷新数据
     */
    refreshData() {
      this.loadAllData()
      this.$message.success('数据已刷新')
    },

    /**
     * 导出报告
     */
    async handleExport() {
      try {
        const res = await ManagementAPI.exportManagementReport({
          period: this.currentPeriod
        })
        // 处理文件下载
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `报表管理分析报告_${this.currentPeriod}.xlsx`
        link.click()
        this.$message.success('报告导出成功')
      } catch (error) {
        console.error('导出报告失败:', error)
        this.$message.error('导出报告失败,请稍后重试')
      }
    },

    /**
     * 标记预警为已处理
     */
    async handleResolveAlert(row) {
      try {
        const res = await ManagementAPI.resolveManagementAlert(row.id, {
          remark: '已手动标记为处理中'
        })
        if (res.code === 1) {
          this.$message.success('标记成功')
          this.loadAlerts()
        } else {
          this.$message.error(res.message || '标记失败')
        }
      } catch (error) {
        console.error('标记预警失败:', error)
        this.$message.error('标记失败,请稍后重试')
      }
    },

    /**
     * 查看详情
     */
    handleViewDetail(row) {
      this.$message.info(`查看预警详情: ${row.description}`)
      // TODO: 跳转到详情页面
    },

    /**
     * 分页大小变更
     */
    handleSizeChange(val) {
      this.alertQuery.pageSize = val
      this.alertQuery.pageNo = 1
      this.loadAlerts()
    },

    /**
     * 当前页变更
     */
    handleCurrentChange(val) {
      this.alertQuery.pageNo = val
      this.loadAlerts()
    },

    /**
     * 窗口大小改变处理
     */
    handleResize() {
      this.charts.forEach(chart => {
        if (chart) {
          chart.resize()
        }
      })
    },

    /**
     * 格式化金额
     */
    formatAmount(amount) {
      if (!amount) return '0'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    /**
     * 获取趋势样式类
     */
    getTrendClass(trend) {
      if (!trend) return ''
      return trend > 0 ? 'trend-up' : trend < 0 ? 'trend-down' : ''
    },

    /**
     * 获取趋势图标
     */
    getTrendIcon(trend) {
      if (!trend) return 'el-icon-minus'
      return trend > 0 ? 'el-icon-top' : 'el-icon-bottom'
    },

    /**
     * 获取预警类型标签
     */
    getAlertTypeTag(type) {
      const typeMap = {
        '超预算预警': 'danger',
        '成本异常': 'warning',
        '执行偏差': 'info'
      }
      return typeMap[type] || 'info'
    },

    /**
     * 获取级别标签
     */
    getLevelTag(level) {
      const levelMap = {
        'high': 'danger',
        'medium': 'warning',
        'low': 'success'
      }
      return levelMap[level] || 'info'
    },

    /**
     * 获取级别文本
     */
    getLevelText(level) {
      const levelMap = {
        'high': '高风险',
        'medium': '中风险',
        'low': '低风险'
      }
      return levelMap[level] || '未知'
    },

    /**
     * 获取状态标签
     */
    getStatusTag(status) {
      const statusMap = {
        'pending': 'danger',
        'processing': 'warning',
        'resolved': 'success'
      }
      return statusMap[status] || 'info'
    },

    /**
     * 获取状态文本
     */
    getStatusText(status) {
      const statusMap = {
        'pending': '未处理',
        'processing': '处理中',
        'resolved': '已处理'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-management-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

// 统计卡片样式
.stats-cards {
  margin-bottom: 24px;
}

.stat-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transform: translateY(-2px);
  }
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 28px;
  color: #fff;

  &.cost-center {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  &.product-cost {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }

  &.cost-estimate {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.special-cost {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }

  &.internal-settlement {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  }
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;

  &.trend-up {
    color: #f56c6c;
  }

  &.trend-down {
    color: #67c23a;
  }
}

// 图表卡片样式
.charts-row {
  margin-bottom: 24px;
}

.chart-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.chart-controls {
  display: flex;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 400px;
}

// 异常预警表格样式
.alerts-row {
  margin-bottom: 24px;
}

.table-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.table-controls {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

// 响应式布局
@media screen and (max-width: 1200px) {
  .stats-cards {
    ::v-deep .el-col-xl-8 {
      width: 50%;
    }
  }
}

@media screen and (max-width: 768px) {
  .cost-management-container {
    padding: 12px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;

    ::v-deep .el-date-editor {
      width: 150px !important;
    }
  }

  .stats-cards {
    ::v-deep .el-col {
      width: 100% !important;
    }
  }

  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .chart-controls {
      width: 100%;
      justify-content: space-between;
    }
  }

  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .table-controls {
      width: 100%;
      justify-content: space-between;
    }
  }

  .chart-container {
    height: 300px;
  }

  ::v-deep .el-table {
    font-size: 12px;
  }
}
</style>
