<template>
  <div class="performance-interview-dashboard">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>绩效面谈仪表板</h2>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateInterview">新建面谈</el-button>
        <el-button icon="el-icon-refresh" @click="refreshData">刷新数据</el-button>
        <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <i class="el-icon-document"></i>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.totalInterviews || 0 }}</div>
                <div class="stat-label">总面谈数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon ongoing">
                <i class="el-icon-time"></i>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.ongoingInterviews || 0 }}</div>
                <div class="stat-label">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon completed">
                <i class="el-icon-check"></i>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.completedInterviews || 0 }}</div>
                <div class="stat-label">已完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon score">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.averageScore || 0 }}</div>
                <div class="stat-label">平均满意度</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 面谈完成趋势 -->
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>面谈完成趋势</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
            <div id="trendChart" style="height: 300px;"></div>
          </el-card>
        </el-col>

        <!-- 面谈类型分布 -->
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>面谈类型分布</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshTypeChart">刷新</el-button>
            </div>
            <div id="typeChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 面谈状态分布 -->
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>面谈状态分布</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshStatusChart">刷新</el-button>
            </div>
            <div id="statusChart" style="height: 300px;"></div>
          </el-card>
        </el-col>

        <!-- 满意度分布 -->
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>满意度分布</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshSatisfactionChart">刷新</el-button>
            </div>
            <div id="satisfactionChart" style="height: 300px;"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 待处理事项 -->
    <div class="pending-section">
      <el-row :gutter="20">
        <!-- 待处理面谈 -->
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>待处理面谈</span>
              <el-badge :value="pendingInterviews.length" class="item">
                <el-button style="float: right; padding: 3px 0" type="text" @click="refreshPendingInterviews">刷新</el-button>
              </el-badge>
            </div>
            <div class="pending-list">
              <div v-if="pendingInterviews.length === 0" class="empty-state">
                <i class="el-icon-document"></i>
                <p>暂无待处理面谈</p>
              </div>
              <div v-else>
                <div
                  v-for="interview in pendingInterviews"
                  :key="interview.interviewId"
                  class="pending-item"
                  @click="handleViewInterview(interview)"
                >
                  <div class="item-header">
                    <span class="item-title">{{ interview.interviewTitle }}</span>
                    <el-tag :type="getStatusTagType(interview.interviewStatus)" size="mini">
                      {{ formatInterviewStatus(interview.interviewStatus) }}
                    </el-tag>
                  </div>
                  <div class="item-content">
                    <p>被面谈人: {{ interview.intervieweeName }}</p>
                    <p>面谈官: {{ interview.interviewerName }}</p>
                    <p>计划时间: {{ formatDateTime(interview.plannedStartTime) }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 我的面谈任务 -->
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>我的面谈任务</span>
              <el-badge :value="myInterviews.length" class="item">
                <el-button style="float: right; padding: 3px 0" type="text" @click="refreshMyInterviews">刷新</el-button>
              </el-badge>
            </div>
            <div class="pending-list">
              <div v-if="myInterviews.length === 0" class="empty-state">
                <i class="el-icon-user"></i>
                <p>暂无面谈任务</p>
              </div>
              <div v-else>
                <div
                  v-for="interview in myInterviews"
                  :key="interview.interviewId"
                  class="pending-item"
                  @click="handleViewInterview(interview)"
                >
                  <div class="item-header">
                    <span class="item-title">{{ interview.interviewTitle }}</span>
                    <el-tag :type="getPriorityTagType(interview.priorityLevel)" size="mini">
                      {{ formatPriorityLevel(interview.priorityLevel) }}
                    </el-tag>
                  </div>
                  <div class="item-content">
                    <p>被面谈人: {{ interview.intervieweeName }}</p>
                    <p>面谈类型: {{ formatInterviewType(interview.interviewType) }}</p>
                    <p>计划时间: {{ formatDateTime(interview.plannedStartTime) }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 面谈排行榜 -->
        <el-col :span="8">
          <el-card>
            <div slot="header">
              <span>面谈排行榜</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="refreshRanking">刷新</el-button>
            </div>
            <div class="ranking-list">
              <div v-if="rankingList.length === 0" class="empty-state">
                <i class="el-icon-trophy"></i>
                <p>暂无排行数据</p>
              </div>
              <div v-else>
                <div
                  v-for="(item, index) in rankingList"
                  :key="item.userId"
                  class="ranking-item"
                >
                  <div class="ranking-number">
                    <span :class="['rank', `rank-${index + 1}`]">{{ index + 1 }}</span>
                  </div>
                  <div class="ranking-info">
                    <div class="ranking-name">{{ item.userName }}</div>
                    <div class="ranking-score">
                      <span>完成: {{ item.completedCount }}次</span>
                      <span>满意度: {{ item.averageRating }}分</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <el-card>
        <div slot="header">
          <span>快速操作</span>
        </div>
        <div class="action-buttons">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreateInterview">新建面谈</el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleBatchCreate">批量创建</el-button>
          <el-button type="warning" icon="el-icon-bell" @click="handleSendReminders">发送提醒</el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExportReport">导出报告</el-button>
          <el-button icon="el-icon-setting" @click="handleSettings">面谈设置</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  getInterviewStatistics,
  getInterviewCompletionTrend,
  getInterviewTypeDistribution,
  getInterviewStatusDistribution,
  getSatisfactionDistribution,
  getInterviewRanking,
  getPendingFollowUpInterviews,
  getUpcomingInterviews,
  getInterviewsByInterviewer,
  formatInterviewStatus,
  formatInterviewType,
  formatPriorityLevel,
  getStatusTagType,
  getPriorityTagType
} from '@/api/managementAccountant/pm/performanceInterview'

export default {
  name: 'PerformanceInterviewDashboard',
  data() {
    return {
      loading: false,
      
      // 统计数据
      statistics: {
        totalInterviews: 0,
        ongoingInterviews: 0,
        completedInterviews: 0,
        averageScore: 0
      },

      // 待处理面谈
      pendingInterviews: [],
      
      // 我的面谈任务
      myInterviews: [],
      
      // 排行榜
      rankingList: [],

      // 图表实例
      trendChart: null,
      typeChart: null,
      statusChart: null,
      satisfactionChart: null
    }
  },

  created() {
    this.loadDashboardData()
  },

  mounted() {
    this.initCharts()
  },

  beforeDestroy() {
    // 销毁图表实例
    if (this.trendChart) this.trendChart.dispose()
    if (this.typeChart) this.typeChart.dispose()
    if (this.statusChart) this.statusChart.dispose()
    if (this.satisfactionChart) this.satisfactionChart.dispose()
  },

  methods: {
    // 加载仪表板数据
    async loadDashboardData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStatistics(),
          this.loadPendingInterviews(),
          this.loadMyInterviews(),
          this.loadRanking()
        ])
      } catch (error) {
        this.$message.error('加载数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getInterviewStatistics({
          year: new Date().getFullYear()
        })
        
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },

    // 加载待处理面谈
    async loadPendingInterviews() {
      try {
        const response = await getUpcomingInterviews(
          this.$moment().add(7, 'days').format('YYYY-MM-DD HH:mm:ss'),
          10
        )
        
        if (response.success) {
          this.pendingInterviews = response.data
        }
      } catch (error) {
        console.error('加载待处理面谈失败:', error)
      }
    },

    // 加载我的面谈任务
    async loadMyInterviews() {
      try {
        // TODO: 获取当前用户ID
        const currentUserId = 1
        const response = await getInterviewsByInterviewer(currentUserId, 'SCHEDULED', 10)
        
        if (response.success) {
          this.myInterviews = response.data
        }
      } catch (error) {
        console.error('加载我的面谈任务失败:', error)
      }
    },

    // 加载排行榜
    async loadRanking() {
      try {
        const response = await getInterviewRanking(
          new Date().getFullYear(),
          'COMPLETION_COUNT',
          10
        )
        
        if (response.success) {
          this.rankingList = response.data
        }
      } catch (error) {
        console.error('加载排行榜失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.$nextTick(() => {
        this.initTrendChart()
        this.initTypeChart()
        this.initStatusChart()
        this.initSatisfactionChart()
      })
    },

    // 初始化趋势图表
    async initTrendChart() {
      const echarts = require('echarts')
      this.trendChart = echarts.init(document.getElementById('trendChart'))
      
      try {
        const response = await getInterviewCompletionTrend(
          this.$moment().subtract(6, 'months').format('YYYY-MM-DD'),
          this.$moment().format('YYYY-MM-DD')
        )
        
        if (response.success) {
          const option = {
            title: {
              text: '最近6个月面谈完成趋势'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: response.data.map(item => item.month)
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: response.data.map(item => item.count),
              type: 'line',
              smooth: true,
              itemStyle: {
                color: '#409EFF'
              }
            }]
          }
          
          this.trendChart.setOption(option)
        }
      } catch (error) {
        console.error('加载趋势图表失败:', error)
      }
    },

    // 初始化类型分布图表
    async initTypeChart() {
      const echarts = require('echarts')
      this.typeChart = echarts.init(document.getElementById('typeChart'))
      
      try {
        const response = await getInterviewTypeDistribution(new Date().getFullYear())
        
        if (response.success) {
          const option = {
            title: {
              text: '面谈类型分布',
              left: 'center'
            },
            tooltip: {
              trigger: 'item'
            },
            series: [{
              type: 'pie',
              radius: '50%',
              data: response.data.map(item => ({
                value: item.count,
                name: this.formatInterviewType(item.type)
              })),
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

    // 初始化状态分布图表
    async initStatusChart() {
      const echarts = require('echarts')
      this.statusChart = echarts.init(document.getElementById('statusChart'))
      
      try {
        const response = await getInterviewStatusDistribution(new Date().getFullYear())
        
        if (response.success) {
          const option = {
            title: {
              text: '面谈状态分布'
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            xAxis: {
              type: 'category',
              data: response.data.map(item => this.formatInterviewStatus(item.status))
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: response.data.map(item => item.count),
              type: 'bar',
              itemStyle: {
                color: '#67C23A'
              }
            }]
          }
          
          this.statusChart.setOption(option)
        }
      } catch (error) {
        console.error('加载状态分布图表失败:', error)
      }
    },

    // 初始化满意度分布图表
    async initSatisfactionChart() {
      const echarts = require('echarts')
      this.satisfactionChart = echarts.init(document.getElementById('satisfactionChart'))
      
      try {
        const response = await getSatisfactionDistribution(new Date().getFullYear())
        
        if (response.success) {
          const option = {
            title: {
              text: '满意度分布'
            },
            tooltip: {
              trigger: 'axis'
            },
            xAxis: {
              type: 'category',
              data: ['1分', '2分', '3分', '4分', '5分']
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: response.data.map(item => item.count),
              type: 'bar',
              itemStyle: {
                color: '#E6A23C'
              }
            }]
          }
          
          this.satisfactionChart.setOption(option)
        }
      } catch (error) {
        console.error('加载满意度分布图表失败:', error)
      }
    },

    // 刷新数据
    refreshData() {
      this.loadDashboardData()
      this.refreshCharts()
    },

    // 刷新图表
    refreshCharts() {
      this.refreshTrendChart()
      this.refreshTypeChart()
      this.refreshStatusChart()
      this.refreshSatisfactionChart()
    },

    // 刷新各个图表
    refreshTrendChart() {
      this.initTrendChart()
    },

    refreshTypeChart() {
      this.initTypeChart()
    },

    refreshStatusChart() {
      this.initStatusChart()
    },

    refreshSatisfactionChart() {
      this.initSatisfactionChart()
    },

    // 刷新待处理面谈
    refreshPendingInterviews() {
      this.loadPendingInterviews()
    },

    // 刷新我的面谈任务
    refreshMyInterviews() {
      this.loadMyInterviews()
    },

    // 刷新排行榜
    refreshRanking() {
      this.loadRanking()
    },

    // 查看面谈详情
    handleViewInterview(interview) {
      this.$router.push(`/pm/performance-interview/detail/${interview.interviewId}`)
    },

    // 新建面谈
    handleCreateInterview() {
      this.$router.push('/pm/performance-interview/create')
    },

    // 批量创建
    handleBatchCreate() {
      this.$message.info('批量创建功能开发中')
    },

    // 发送提醒
    handleSendReminders() {
      this.$message.info('发送提醒功能开发中')
    },

    // 导出报告
    handleExportReport() {
      this.$message.info('导出报告功能开发中')
    },

    // 设置
    handleSettings() {
      this.$message.info('设置功能开发中')
    },

    // 格式化方法
    formatInterviewStatus,
    formatInterviewType,
    formatPriorityLevel,
    getStatusTagType,
    getPriorityTagType,

    formatDateTime(dateTime) {
      if (!dateTime) return '--'
      return this.$moment(dateTime).format('MM-DD HH:mm')
    }
  }
}
</script>

<style scoped>
.performance-interview-dashboard {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.statistics-section,
.charts-section,
.pending-section,
.quick-actions {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.ongoing {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.score {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.pending-list,
.ranking-list {
  max-height: 400px;
  overflow-y: auto;
}

.pending-item,
.ranking-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pending-item:hover,
.ranking-item:hover {
  background-color: #f5f7fa;
}

.pending-item:last-child,
.ranking-item:last-child {
  border-bottom: none;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  font-weight: 500;
  color: #303133;
}

.item-content p {
  margin: 4px 0;
  font-size: 13px;
  color: #606266;
}

.ranking-item {
  display: flex;
  align-items: center;
}

.ranking-number {
  margin-right: 15px;
}

.rank {
  display: inline-block;
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 50%;
  font-weight: bold;
  color: white;
}

.rank-1 {
  background: #f56c6c;
}

.rank-2 {
  background: #e6a23c;
}

.rank-3 {
  background: #909399;
}

.rank:not(.rank-1):not(.rank-2):not(.rank-3) {
  background: #c0c4cc;
}

.ranking-info {
  flex: 1;
}

.ranking-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.ranking-score {
  font-size: 12px;
  color: #909399;
}

.ranking-score span {
  margin-right: 10px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 10px;
}

.empty-state p {
  margin: 0;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}
</style>
