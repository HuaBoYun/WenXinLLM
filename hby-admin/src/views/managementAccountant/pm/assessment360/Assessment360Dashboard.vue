<template>
  <div class="assessment-360-dashboard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>360度评估仪表板</h2>
        <p>360度评估数据分析与统计</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateAssessment">
          新建评估
        </el-button>
        <el-button icon="el-icon-refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon total">
                <i class="el-icon-view"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.totalAssessments || 0 }}</div>
                <div class="stats-label">总评估数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon ongoing">
                <i class="el-icon-loading"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.ongoingAssessments || 0 }}</div>
                <div class="stats-label">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon completed">
                <i class="el-icon-check"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.completedAssessments || 0 }}</div>
                <div class="stats-label">已完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon rate">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ dashboardData.averageScore || 0 }}</div>
                <div class="stats-label">平均得分</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表和列表区域 -->
    <div class="content-section">
      <el-row :gutter="20">
        <!-- 左侧图表区域 -->
        <el-col :span="16">
          <el-card class="chart-card">
            <div slot="header">
              <span>评估完成趋势</span>
              <el-button-group style="float: right;">
                <el-button size="mini" :type="chartPeriod === 'WEEK' ? 'primary' : ''" @click="changeChartPeriod('WEEK')">周</el-button>
                <el-button size="mini" :type="chartPeriod === 'MONTH' ? 'primary' : ''" @click="changeChartPeriod('MONTH')">月</el-button>
                <el-button size="mini" :type="chartPeriod === 'QUARTER' ? 'primary' : ''" @click="changeChartPeriod('QUARTER')">季</el-button>
              </el-button-group>
            </div>
            <div class="chart-container">
              <div id="assessmentTrendChart" style="width: 100%; height: 300px;"></div>
            </div>
          </el-card>

          <el-card class="chart-card" style="margin-top: 20px;">
            <div slot="header">
              <span>评估类型分布</span>
            </div>
            <div class="chart-container">
              <div id="assessmentTypeChart" style="width: 100%; height: 300px;"></div>
            </div>
          </el-card>

          <el-card class="chart-card" style="margin-top: 20px;">
            <div slot="header">
              <span>评估得分分布</span>
            </div>
            <div class="chart-container">
              <div id="scoreDistributionChart" style="width: 100%; height: 300px;"></div>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧列表区域 -->
        <el-col :span="8">
          <el-card class="list-card">
            <div slot="header">
              <span>待处理评估</span>
              <el-link type="primary" style="float: right;" @click="viewAllPending">查看全部</el-link>
            </div>
            <div class="assessment-list">
              <div
                v-for="assessment in pendingAssessments"
                :key="assessment.assessmentId"
                class="assessment-item"
                @click="viewAssessmentDetail(assessment)"
              >
                <div class="assessment-info">
                  <div class="assessment-name">{{ assessment.assessmentName }}</div>
                  <div class="assessment-meta">
                    <el-tag :type="getTypeTag(assessment.assessmentType)" size="mini">
                      {{ formatAssessmentType(assessment.assessmentType) }}
                    </el-tag>
                    <span class="assessment-user">{{ assessment.assessedUserName }}</span>
                  </div>
                  <div class="assessment-deadline">
                    截止：{{ formatDate(assessment.endTime) }}
                  </div>
                </div>
                <div class="assessment-progress">
                  <el-progress
                    :percentage="assessment.completionRate || 0"
                    :stroke-width="6"
                    :color="getProgressColor(assessment.completionRate)"
                  />
                  <div class="progress-text">{{ assessment.completionRate || 0 }}%</div>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="list-card" style="margin-top: 20px;">
            <div slot="header">
              <span>我的评估任务</span>
            </div>
            <div class="task-list">
              <div
                v-for="task in myTasks"
                :key="task.id"
                class="task-item"
                @click="handleTask(task)"
              >
                <div class="task-info">
                  <div class="task-title">{{ task.title }}</div>
                  <div class="task-desc">{{ task.description }}</div>
                  <div class="task-time">{{ formatDateTime(task.createTime) }}</div>
                </div>
                <div class="task-status">
                  <el-tag :type="getTaskStatusTag(task.status)" size="mini">
                    {{ formatTaskStatus(task.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </el-card>

          <el-card class="list-card" style="margin-top: 20px;">
            <div slot="header">
              <span>评估排行榜</span>
            </div>
            <div class="ranking-list">
              <div
                v-for="(item, index) in rankingList"
                :key="item.userId"
                class="ranking-item"
              >
                <div class="ranking-number" :class="getRankingClass(index + 1)">
                  {{ index + 1 }}
                </div>
                <div class="ranking-info">
                  <div class="ranking-name">{{ item.userName }}</div>
                  <div class="ranking-dept">{{ item.departmentName }}</div>
                </div>
                <div class="ranking-score" :style="{ color: getScoreColor(item.averageScore) }">
                  {{ item.averageScore }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <el-card>
        <div slot="header">
          <span>快速操作</span>
        </div>
        <div class="action-buttons">
          <el-button-group>
            <el-button icon="el-icon-plus" @click="handleCreateAssessment">新建评估</el-button>
            <el-button icon="el-icon-view" @click="handleBatchEvaluation">批量评估</el-button>
            <el-button icon="el-icon-document" @click="handleExportReport">导出报告</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">评估设置</el-button>
          </el-button-group>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  getAssessmentStatistics,
  query360AssessmentPage
} from '@/api/managementAccountant/pm/assessment360'

export default {
  name: 'Assessment360Dashboard',
  data() {
    return {
      loading: false,
      chartPeriod: 'MONTH',
      dashboardData: {
        totalAssessments: 0,
        ongoingAssessments: 0,
        completedAssessments: 0,
        averageScore: 0
      },
      pendingAssessments: [],
      myTasks: [],
      rankingList: []
    }
  },
  created() {
    this.loadDashboardData()
    this.loadPendingAssessments()
    this.loadMyTasks()
    this.loadRankingList()
  },
  mounted() {
    this.initCharts()
  },
  methods: {
    // 加载仪表板数据
    async loadDashboardData() {
      try {
        const response = await getAssessmentStatistics({
          statisticsType: 'OVERVIEW'
        })
        if (response.success) {
          this.dashboardData = response.data
        }
      } catch (error) {
        console.error('加载仪表板数据失败:', error)
      }
    },

    // 加载待处理评估
    async loadPendingAssessments() {
      try {
        const response = await query360AssessmentPage({
          current: 1,
          size: 5,
          assessmentStatus: 'ONGOING'
        })
        if (response.success) {
          this.pendingAssessments = response.data.records
        }
      } catch (error) {
        console.error('加载待处理评估失败:', error)
      }
    },

    // 加载我的任务
    async loadMyTasks() {
      // 模拟我的评估任务数据
      this.myTasks = [
        {
          id: 1,
          title: '张三的年度评估',
          description: '需要完成上级评价',
          status: 'PENDING',
          createTime: new Date()
        },
        {
          id: 2,
          title: '李四的季度评估',
          description: '需要完成同级评价',
          status: 'PENDING',
          createTime: new Date()
        },
        {
          id: 3,
          title: '王五的项目评估',
          description: '自评已完成',
          status: 'COMPLETED',
          createTime: new Date()
        }
      ]
    },

    // 加载排行榜
    async loadRankingList() {
      // 模拟排行榜数据
      this.rankingList = [
        {
          userId: 1,
          userName: '张三',
          departmentName: '技术部',
          averageScore: 95.5
        },
        {
          userId: 2,
          userName: '李四',
          departmentName: '产品部',
          averageScore: 92.3
        },
        {
          userId: 3,
          userName: '王五',
          departmentName: '市场部',
          averageScore: 89.7
        },
        {
          userId: 4,
          userName: '赵六',
          departmentName: '销售部',
          averageScore: 87.2
        },
        {
          userId: 5,
          userName: '钱七',
          departmentName: '人事部',
          averageScore: 85.8
        }
      ]
    },

    // 初始化图表
    initCharts() {
      this.initTrendChart()
      this.initTypeChart()
      this.initScoreDistributionChart()
    },

    // 初始化趋势图表
    initTrendChart() {
      // 这里应该使用ECharts或其他图表库
      // 模拟图表初始化
      console.log('初始化评估趋势图表')
    },

    // 初始化类型图表
    initTypeChart() {
      // 这里应该使用ECharts或其他图表库
      // 模拟图表初始化
      console.log('初始化评估类型图表')
    },

    // 初始化得分分布图表
    initScoreDistributionChart() {
      // 这里应该使用ECharts或其他图表库
      // 模拟图表初始化
      console.log('初始化得分分布图表')
    },

    // 改变图表周期
    changeChartPeriod(period) {
      this.chartPeriod = period
      this.initTrendChart()
    },

    // 刷新数据
    refreshData() {
      this.loadDashboardData()
      this.loadPendingAssessments()
      this.loadMyTasks()
      this.loadRankingList()
      this.$message.success('数据已刷新')
    },

    // 新建评估
    handleCreateAssessment() {
      this.$router.push('/pm/360-assessment/create')
    },

    // 查看所有待处理
    viewAllPending() {
      this.$router.push('/pm/360-assessment?status=ONGOING')
    },

    // 查看评估详情
    viewAssessmentDetail(assessment) {
      this.$router.push(`/pm/360-assessment/detail/${assessment.assessmentId}`)
    },

    // 处理任务
    handleTask(task) {
      if (task.status === 'PENDING') {
        this.$router.push(`/pm/360-assessment/evaluation/${task.assessmentId}`)
      } else {
        this.$router.push(`/pm/360-assessment/detail/${task.assessmentId}`)
      }
    },

    // 批量评估
    handleBatchEvaluation() {
      this.$message.info('批量评估功能开发中')
    },

    // 导出报告
    handleExportReport() {
      this.$message.info('导出报告功能开发中')
    },

    // 评估设置
    handleSettings() {
      this.$message.info('评估设置功能开发中')
    },

    // 格式化方法
    formatAssessmentType(type) {
      const typeMap = {
        'ANNUAL': '年度',
        'QUARTERLY': '季度',
        'MONTHLY': '月度',
        'PROJECT': '项目'
      }
      return typeMap[type] || type
    },

    formatTaskStatus(status) {
      const statusMap = {
        'PENDING': '待处理',
        'COMPLETED': '已完成',
        'OVERDUE': '已逾期'
      }
      return statusMap[status] || status
    },

    formatDate(date) {
      if (!date) return ''
      return this.$moment(date).format('MM-DD')
    },

    formatDateTime(date) {
      if (!date) return ''
      return this.$moment(date).format('MM-DD HH:mm')
    },

    // 标签类型
    getTypeTag(type) {
      const tagMap = {
        'ANNUAL': 'danger',
        'QUARTERLY': 'primary',
        'MONTHLY': 'success',
        'PROJECT': 'warning'
      }
      return tagMap[type] || ''
    },

    getTaskStatusTag(status) {
      const tagMap = {
        'PENDING': 'warning',
        'COMPLETED': 'success',
        'OVERDUE': 'danger'
      }
      return tagMap[status] || ''
    },

    // 进度颜色
    getProgressColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 70) return '#e6a23c'
      if (rate >= 50) return '#409eff'
      return '#f56c6c'
    },

    // 分数颜色
    getScoreColor(score) {
      if (!score) return '#c0c4cc'
      if (score >= 90) return '#67c23a'
      if (score >= 80) return '#409eff'
      if (score >= 70) return '#e6a23c'
      return '#f56c6c'
    },

    // 排名样式
    getRankingClass(rank) {
      if (rank === 1) return 'first'
      if (rank === 2) return 'second'
      if (rank === 3) return 'third'
      return ''
    }
  }
}
</script>

<style scoped>
.assessment-360-dashboard {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-section {
  margin-bottom: 20px;
}

.stats-card {
  height: 100px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
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

.stats-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.ongoing {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.rate {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.content-section {
  margin-bottom: 20px;
}

.chart-card,
.list-card {
  height: 380px;
}

.chart-container {
  height: 300px;
}

.assessment-list,
.task-list {
  max-height: 320px;
  overflow-y: auto;
}

.assessment-item,
.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.assessment-item:hover,
.task-item:hover {
  background-color: #f5f7fa;
}

.assessment-item:last-child,
.task-item:last-child {
  border-bottom: none;
}

.assessment-info,
.task-info {
  flex: 1;
  margin-right: 15px;
}

.assessment-name,
.task-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
}

.assessment-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 5px;
}

.assessment-user {
  font-size: 12px;
  color: #909399;
}

.assessment-deadline {
  font-size: 12px;
  color: #e6a23c;
}

.task-desc {
  font-size: 12px;
  color: #606266;
  margin-bottom: 5px;
}

.task-time {
  font-size: 11px;
  color: #c0c4cc;
}

.assessment-progress {
  width: 100px;
  text-align: center;
}

.progress-text {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
}

.ranking-list {
  max-height: 320px;
  overflow-y: auto;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.ranking-item:last-child {
  border-bottom: none;
}

.ranking-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-weight: bold;
  color: white;
  background-color: #c0c4cc;
}

.ranking-number.first {
  background: linear-gradient(135deg, #ffd700 0%, #ffb347 100%);
}

.ranking-number.second {
  background: linear-gradient(135deg, #c0c0c0 0%, #a8a8a8 100%);
}

.ranking-number.third {
  background: linear-gradient(135deg, #cd7f32 0%, #b8860b 100%);
}

.ranking-info {
  flex: 1;
}

.ranking-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 3px;
}

.ranking-dept {
  font-size: 12px;
  color: #909399;
}

.ranking-score {
  font-size: 18px;
  font-weight: bold;
}

.quick-actions {
  margin-top: 20px;
}

.action-buttons {
  text-align: center;
}
</style>
