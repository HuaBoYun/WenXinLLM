<template>
  <div class="quality-control-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">总质量管控数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon active">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
              <div class="stat-label">活跃质量管控</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon detection">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.inDetectionCount || 0 }}</div>
              <div class="stat-label">检测中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon completed">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 质量状态分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">质量状态分布</span>
            <el-button type="text" @click="refreshStatusChart">刷新</el-button>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 质量类型分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">质量类型分布</span>
            <el-button type="text" @click="refreshTypeChart">刷新</el-button>
          </div>
          <div ref="typeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 质量等级分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">质量等级分布</span>
            <el-button type="text" @click="refreshLevelChart">刷新</el-button>
          </div>
          <div ref="levelChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 检测状态分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">检测状态分布</span>
            <el-button type="text" @click="refreshDetectionChart">刷新</el-button>
          </div>
          <div ref="detectionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势图 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">质量管控趋势</span>
            <div class="header-controls">
              <el-date-picker
                v-model="trendDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                size="small"
                @change="refreshTrendChart"
              />
              <el-button type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container trend-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-row :gutter="20" class="table-row">
      <!-- 待处理事项 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="table-card">
          <div slot="header" class="card-header">
            <span class="card-title">待处理事项</span>
            <el-button type="text" @click="refreshPendingList">刷新</el-button>
          </div>
          <el-table :data="pendingList" size="small" max-height="300">
            <el-table-column prop="qualityName" label="质量管控名称" show-overflow-tooltip />
            <el-table-column prop="qualityType" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getQualityTypeTagType(scope.row.qualityType)">
                  {{ formatQualityType(scope.row.qualityType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="80" />
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleStart(scope.row)">开始</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 高风险项目 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="table-card">
          <div slot="header" class="card-header">
            <span class="card-title">高风险项目</span>
            <el-button type="text" @click="refreshHighRiskList">刷新</el-button>
          </div>
          <el-table :data="highRiskList" size="small" max-height="300">
            <el-table-column prop="qualityName" label="质量管控名称" show-overflow-tooltip />
            <el-table-column prop="riskLevel" label="风险等级" width="100">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getRiskLevelTagType(scope.row.riskLevel)">
                  {{ formatRiskLevel(scope.row.riskLevel) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="qualityScore" label="质量评分" width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleView(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近完成 -->
    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card class="table-card">
          <div slot="header" class="card-header">
            <span class="card-title">最近完成</span>
            <el-button type="text" @click="refreshRecentList">刷新</el-button>
          </div>
          <el-table :data="recentCompletedList" size="small" max-height="300">
            <el-table-column prop="qualityCode" label="编码" width="150" />
            <el-table-column prop="qualityName" label="质量管控名称" show-overflow-tooltip />
            <el-table-column prop="qualityType" label="类型" width="120">
              <template slot-scope="scope">
                <el-tag size="mini" :type="getQualityTypeTagType(scope.row.qualityType)">
                  {{ formatQualityType(scope.row.qualityType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="qualityLevel" label="质量等级" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.qualityLevel" size="mini" :type="getQualityLevelTagType(scope.row.qualityLevel)">
                  {{ formatQualityLevel(scope.row.qualityLevel) }}
                </el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="qualityScore" label="质量评分" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="passRate" label="合格率" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.passRate">{{ scope.row.passRate }}%</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="detectionEndTime" label="完成时间" width="160">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.detectionEndTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleView(scope.row)">查看</el-button>
                <el-button type="text" size="mini" @click="handleGenerateReport(scope.row)">报告</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 质量管控详情对话框 -->
    <QualityControlDetail
      :visible.sync="detailVisible"
      :quality-control-id="currentQualityControlId"
      :mode="detailMode"
      @refresh="loadData"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getQualityControlStatistics,
  getQualityStatusDistribution,
  getQualityTypeDistribution,
  getQualityLevelDistribution,
  getDetectionStatusDistribution,
  getQualityControlTrend,
  getPendingDetection,
  getHighRisk,
  getCompletedDetection,
  startDetection,
  generateQualityReport,
  formatQualityStatus,
  formatDetectionStatus,
  formatQualityLevel,
  formatQualityType
} from '@/api/managementAccountant/ss/qualityControl'
import QualityControlDetail from './QualityControlDetail'

export default {
  name: 'QualityControlDashboard',
  components: {
    QualityControlDetail
  },
  data() {
    return {
      loading: false,
      statistics: {},
      trendDateRange: [],
      // 图表实例
      statusChart: null,
      typeChart: null,
      levelChart: null,
      detectionChart: null,
      trendChart: null,
      // 列表数据
      pendingList: [],
      highRiskList: [],
      recentCompletedList: [],
      // 对话框控制
      detailVisible: false,
      detailMode: 'view',
      currentQualityControlId: null
    }
  },
  mounted() {
    this.initDateRange()
    this.loadData()
    this.initCharts()
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    // 销毁图表实例
    this.destroyCharts()
    // 移除事件监听
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    // 初始化日期范围
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30) // 30天前
      this.trendDateRange = [start, end]
    },

    // 加载数据
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStatistics(),
          this.loadPendingList(),
          this.loadHighRiskList(),
          this.loadRecentCompletedList()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getQualityControlStatistics()
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 加载待处理列表
    async loadPendingList() {
      try {
        const response = await getPendingDetection()
        if (response.success) {
          this.pendingList = response.data.slice(0, 10) // 只显示前10条
        }
      } catch (error) {
        console.error('加载待处理列表失败:', error)
      }
    },

    // 加载高风险列表
    async loadHighRiskList() {
      try {
        const response = await getHighRisk()
        if (response.success) {
          this.highRiskList = response.data.slice(0, 10) // 只显示前10条
        }
      } catch (error) {
        console.error('加载高风险列表失败:', error)
      }
    },

    // 加载最近完成列表
    async loadRecentCompletedList() {
      try {
        const response = await getCompletedDetection()
        if (response.success) {
          // 按完成时间排序，取最近的10条
          this.recentCompletedList = response.data
            .sort((a, b) => new Date(b.detectionEndTime) - new Date(a.detectionEndTime))
            .slice(0, 10)
        }
      } catch (error) {
        console.error('加载最近完成列表失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initStatusChart()
        this.initTypeChart()
        this.initLevelChart()
        this.initDetectionChart()
        this.initTrendChart()
      })
    },

    // 初始化状态分布图表
    async initStatusChart() {
      if (!this.$refs.statusChart) return

      this.statusChart = echarts.init(this.$refs.statusChart)

      try {
        const response = await getQualityStatusDistribution()
        if (response.success) {
          const data = response.data.map(item => ({
            name: formatQualityStatus(item.status),
            value: item.count
          }))

          const option = {
            title: {
              text: '质量状态分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              textStyle: {
                fontSize: 12
              }
            },
            series: [
              {
                name: '质量状态',
                type: 'pie',
                radius: ['40%', '70%'],
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
                data: data
              }
            ],
            color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
          }

          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('初始化状态分布图表失败:', error)
      }
    },

    // 初始化类型分布图表
    async initTypeChart() {
      if (!this.$refs.typeChart) return

      this.typeChart = echarts.init(this.$refs.typeChart)

      try {
        const response = await getQualityTypeDistribution()
        if (response.success) {
          const data = response.data.map(item => ({
            name: formatQualityType(item.type),
            value: item.count
          }))

          const option = {
            title: {
              text: '质量类型分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              textStyle: {
                fontSize: 12
              }
            },
            series: [
              {
                name: '质量类型',
                type: 'pie',
                radius: '70%',
                center: ['60%', '50%'],
                data: data,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ],
            color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#C45656']
          }

          this.typeChart.setOption(option)
        }
      } catch (error) {
        console.error('初始化类型分布图表失败:', error)
      }
    },

    // 初始化等级分布图表
    async initLevelChart() {
      if (!this.$refs.levelChart) return

      this.levelChart = echarts.init(this.$refs.levelChart)

      try {
        const response = await getQualityLevelDistribution()
        if (response.success) {
          const categories = response.data.map(item => formatQualityLevel(item.level))
          const values = response.data.map(item => item.count)

          const option = {
            title: {
              text: '质量等级分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
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
            xAxis: {
              type: 'category',
              data: categories,
              axisLabel: {
                fontSize: 12
              }
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                fontSize: 12
              }
            },
            series: [
              {
                name: '数量',
                type: 'bar',
                data: values,
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#409EFF' },
                    { offset: 1, color: '#67C23A' }
                  ])
                }
              }
            ]
          }

          this.levelChart.setOption(option)
        }
      } catch (error) {
        console.error('初始化等级分布图表失败:', error)
      }
    },

    // 初始化检测状态分布图表
    async initDetectionChart() {
      if (!this.$refs.detectionChart) return

      this.detectionChart = echarts.init(this.$refs.detectionChart)

      try {
        const response = await getDetectionStatusDistribution()
        if (response.success) {
          const categories = response.data.map(item => formatDetectionStatus(item.status))
          const values = response.data.map(item => item.count)

          const option = {
            title: {
              text: '检测状态分布',
              left: 'center',
              textStyle: {
                fontSize: 14,
                fontWeight: 'normal'
              }
            },
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
            xAxis: {
              type: 'category',
              data: categories,
              axisLabel: {
                fontSize: 12,
                rotate: 45
              }
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                fontSize: 12
              }
            },
            series: [
              {
                name: '数量',
                type: 'bar',
                data: values,
                itemStyle: {
                  color: function(params) {
                    const colors = ['#909399', '#409EFF', '#E6A23C', '#67C23A', '#F56C6C', '#F56C6C']
                    return colors[params.dataIndex] || '#409EFF'
                  }
                }
              }
            ]
          }

          this.detectionChart.setOption(option)
        }
      } catch (error) {
        console.error('初始化检测状态分布图表失败:', error)
      }
    },

    // 初始化趋势图表
    async initTrendChart() {
      if (!this.$refs.trendChart) return

      this.trendChart = echarts.init(this.$refs.trendChart)

      try {
        const params = {
          startDate: this.formatDate(this.trendDateRange[0]),
          endDate: this.formatDate(this.trendDateRange[1]),
          tenantId: 1
        }

        const response = await getQualityControlTrend(params)
        if (response.success) {
          const dates = response.data.map(item => item.date)
          const totalCounts = response.data.map(item => item.totalCount)
          const completedCounts = response.data.map(item => item.completedCount)
          const activeCounts = response.data.map(item => item.activeCount)

          const option = {
            title: {
              text: '质量管控趋势',
              left: 'center',
              textStyle: {
                fontSize: 16,
                fontWeight: 'normal'
              }
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['总数', '已完成', '活跃'],
              top: 30
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              top: '15%',
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
              data: dates
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '总数',
                type: 'line',
                stack: 'Total',
                data: totalCounts,
                smooth: true,
                itemStyle: {
                  color: '#409EFF'
                }
              },
              {
                name: '已完成',
                type: 'line',
                stack: 'Total',
                data: completedCounts,
                smooth: true,
                itemStyle: {
                  color: '#67C23A'
                }
              },
              {
                name: '活跃',
                type: 'line',
                stack: 'Total',
                data: activeCounts,
                smooth: true,
                itemStyle: {
                  color: '#E6A23C'
                }
              }
            ]
          }

          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('初始化趋势图表失败:', error)
      }
    },

    // 刷新图表
    refreshStatusChart() {
      this.initStatusChart()
    },

    refreshTypeChart() {
      this.initTypeChart()
    },

    refreshLevelChart() {
      this.initLevelChart()
    },

    refreshDetectionChart() {
      this.initDetectionChart()
    },

    refreshTrendChart() {
      this.initTrendChart()
    },

    // 刷新列表
    refreshPendingList() {
      this.loadPendingList()
    },

    refreshHighRiskList() {
      this.loadHighRiskList()
    },

    refreshRecentList() {
      this.loadRecentCompletedList()
    },

    // 处理窗口大小变化
    handleResize() {
      this.$nextTick(() => {
        if (this.statusChart) this.statusChart.resize()
        if (this.typeChart) this.typeChart.resize()
        if (this.levelChart) this.levelChart.resize()
        if (this.detectionChart) this.detectionChart.resize()
        if (this.trendChart) this.trendChart.resize()
      })
    },

    // 销毁图表
    destroyCharts() {
      if (this.statusChart) {
        this.statusChart.dispose()
        this.statusChart = null
      }
      if (this.typeChart) {
        this.typeChart.dispose()
        this.typeChart = null
      }
      if (this.levelChart) {
        this.levelChart.dispose()
        this.levelChart = null
      }
      if (this.detectionChart) {
        this.detectionChart.dispose()
        this.detectionChart = null
      }
      if (this.trendChart) {
        this.trendChart.dispose()
        this.trendChart = null
      }
    },

    // 开始检测
    async handleStart(row) {
      try {
        const response = await startDetection(row.qualityId)
        if (response.success) {
          this.$message.success('开始检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始检测失败')
        }
      } catch (error) {
        console.error('开始检测失败:', error)
        this.$message.error('开始检测失败')
      }
    },

    // 查看详情
    handleView(row) {
      this.currentQualityControlId = row.qualityId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 生成报告
    async handleGenerateReport(row) {
      try {
        const response = await generateQualityReport(row.qualityId)
        if (response.success) {
          this.$message.success('生成报告成功')
          console.log('报告内容:', response.data)
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 格式化方法
    formatQualityStatus,
    formatDetectionStatus,
    formatQualityLevel,
    formatQualityType,

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    },

    // 格式化风险等级
    formatRiskLevel(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'VERY_HIGH': '极高风险'
      }
      return levelMap[level] || level
    },

    // 获取标签类型
    getQualityTypeTagType(type) {
      const typeMap = {
        'PRODUCT_QUALITY': 'primary',
        'SERVICE_QUALITY': 'success',
        'PROCESS_QUALITY': 'warning',
        'SYSTEM_QUALITY': 'info',
        'DATA_QUALITY': 'danger',
        'ENVIRONMENT_QUALITY': 'primary'
      }
      return typeMap[type] || 'info'
    },

    getQualityLevelTagType(level) {
      const typeMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger',
        'VERY_POOR': 'danger'
      }
      return typeMap[level] || 'info'
    },

    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'VERY_HIGH': 'danger'
      }
      return typeMap[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-control-dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .stats-cards,
  .charts-row,
  .table-row {
    margin-bottom: 20px;
  }

  // 统计卡片样式
  .stat-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      transform: translateY(-2px);
    }

    .stat-content {
      display: flex;
      align-items: center;
      padding: 10px 0;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        font-size: 24px;
        color: white;

        &.total {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &.active {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &.detection {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &.completed {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
          margin-bottom: 5px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
          line-height: 1;
        }
      }
    }
  }

  // 图表卡片样式
  .chart-card,
  .table-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .header-controls {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }

    .chart-container {
      height: 300px;
      width: 100%;

      &.trend-chart {
        height: 400px;
      }
    }
  }

  // 表格样式
  .el-table {
    .el-table__header {
      th {
        background-color: #f5f7fa;
        color: #606266;
        font-weight: 600;
        border-bottom: 1px solid #ebeef5;
      }
    }

    .el-table__row {
      &:hover {
        background-color: #f5f7fa;
      }
    }

    .el-table__empty-text {
      color: #909399;
      font-size: 14px;
    }
  }

  // 标签样式
  .el-tag {
    border-radius: 4px;
    font-size: 12px;
    padding: 0 8px;
    height: 24px;
    line-height: 22px;
  }

  // 按钮样式
  .el-button--text {
    color: #409eff;
    font-size: 12px;
    padding: 0;

    &:hover {
      color: #66b1ff;
    }
  }

  // 日期选择器样式
  .el-date-editor {
    width: 240px;
  }

  // 响应式设计
  @media (max-width: 1200px) {
    .stats-cards {
      .el-col {
        margin-bottom: 15px;
      }
    }

    .charts-row {
      .el-col {
        margin-bottom: 20px;
      }
    }
  }

  @media (max-width: 768px) {
    padding: 10px;

    .stat-card {
      .stat-content {
        .stat-icon {
          width: 50px;
          height: 50px;
          font-size: 20px;
          margin-right: 15px;
        }

        .stat-info {
          .stat-value {
            font-size: 24px;
          }

          .stat-label {
            font-size: 12px;
          }
        }
      }
    }

    .chart-container {
      height: 250px !important;

      &.trend-chart {
        height: 300px !important;
      }
    }

    .card-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      .header-controls {
        width: 100%;
        justify-content: flex-end;

        .el-date-editor {
          width: 200px;
        }
      }
    }

    .el-table {
      font-size: 12px;

      .el-table__header th,
      .el-table__body td {
        padding: 8px 5px;
      }
    }
  }

  @media (max-width: 480px) {
    .stat-card {
      .stat-content {
        flex-direction: column;
        text-align: center;

        .stat-icon {
          margin-right: 0;
          margin-bottom: 10px;
        }
      }
    }

    .chart-container {
      height: 200px !important;

      &.trend-chart {
        height: 250px !important;
      }
    }

    .header-controls {
      .el-date-editor {
        width: 180px;
      }
    }
  }
}

// 全局样式覆盖
::v-deep {
  .el-card__header {
    padding: 18px 20px;
    border-bottom: 1px solid #ebeef5;
    background-color: #fafafa;
  }

  .el-card__body {
    padding: 20px;
  }

  .el-table__empty-block {
    min-height: 100px;
  }

  .el-loading-text {
    color: #409eff;
    font-size: 14px;
  }

  // ECharts 工具提示样式
  .echarts-tooltip {
    background-color: rgba(50, 50, 50, 0.9) !important;
    border: none !important;
    border-radius: 4px !important;
    color: #fff !important;
    font-size: 12px !important;
    padding: 8px 12px !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15) !important;
  }
}
</style>
