<template>
  <div class="budget-preparation-index">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>预算编制管理</h1>
      <p>全面的预算编制管理平台，支持多种预算编制方法和流程管理</p>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon budget-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <h3>{{ statistics.totalTasks }}</h3>
              <p>预算任务总数</p>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ statistics.taskGrowth }}%
            </span>
            <span class="stat-label">较上月</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon progress-icon">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-info">
              <h3>{{ statistics.inProgressTasks }}</h3>
              <p>进行中任务</p>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend">
              <i class="el-icon-minus"></i>
              {{ statistics.progressRate }}%
            </span>
            <span class="stat-label">完成率</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon amount-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <h3>{{ formatAmount(statistics.totalBudgetAmount) }}</h3>
              <p>预算总金额</p>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ statistics.amountGrowth }}%
            </span>
            <span class="stat-label">较上年</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon approval-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-info">
              <h3>{{ statistics.pendingApprovals }}</h3>
              <p>待审批任务</p>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend down">
              <i class="el-icon-arrow-down"></i>
              {{ statistics.approvalDecrease }}%
            </span>
            <span class="stat-label">较上周</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块导航 -->
    <el-card class="module-nav-card" shadow="never">
      <div class="module-nav-header">
        <h3>功能模块</h3>
        <p>选择相应的功能模块进行预算编制管理</p>
      </div>
      
      <el-row :gutter="24" class="module-grid">
        <el-col :span="8" v-for="module in modules" :key="module.key">
          <div class="module-item" @click="navigateToModule(module)">
            <div class="module-icon">
              <i :class="module.icon"></i>
            </div>
            <div class="module-content">
              <h4>{{ module.title }}</h4>
              <p>{{ module.description }}</p>
              <div class="module-stats">
                <span class="stat-item">
                  <i class="el-icon-document"></i>
                  {{ module.taskCount }} 个任务
                </span>
                <span class="stat-item">
                  <i class="el-icon-time"></i>
                  {{ module.lastUpdate }}
                </span>
              </div>
            </div>
            <div class="module-action">
              <el-button type="primary" size="small">进入模块</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 快速操作 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="quick-actions-card" shadow="never">
          <div class="card-header">
            <h3>快速操作</h3>
            <el-button type="text" @click="viewAllActions">查看全部</el-button>
          </div>
          
          <div class="quick-actions">
            <div class="action-item" v-for="action in quickActions" :key="action.key" @click="handleQuickAction(action)">
              <div class="action-icon">
                <i :class="action.icon"></i>
              </div>
              <div class="action-content">
                <h5>{{ action.title }}</h5>
                <p>{{ action.description }}</p>
              </div>
              <div class="action-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="recent-activities-card" shadow="never">
          <div class="card-header">
            <h3>最近活动</h3>
            <el-button type="text" @click="viewAllActivities">查看全部</el-button>
          </div>
          
          <div class="activities-list">
            <div class="activity-item" v-for="activity in recentActivities" :key="activity.id">
              <div class="activity-avatar">
                <el-avatar :size="32" :src="activity.userAvatar">
                  {{ (activity.userName || activity.operatorName || '?').charAt(0) }}
                </el-avatar>
              </div>
              <div class="activity-content">
                <p class="activity-text">
                  <strong>{{ activity.userName || activity.operatorName || '未知用户' }}</strong>
                  {{ activity.action }}
                  <span class="activity-target">{{ activity.target }}</span>
                </p>
                <p class="activity-time">{{ activity.time }}</p>
              </div>
              <div class="activity-status">
                <el-tag :type="getActivityStatusType(activity.status)" size="mini">
                  {{ activity.status }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 进度图表 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="chart-card" shadow="never">
          <div class="card-header">
            <h3>预算编制进度</h3>
            <div class="chart-controls">
              <el-radio-group v-model="chartPeriod" size="small" @change="updateChart">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="quarter">本季度</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          
          <div class="chart-container" ref="progressChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="progress-summary-card" shadow="never">
          <div class="card-header">
            <h3>完成情况</h3>
          </div>
          
          <div class="progress-summary">
            <div class="progress-item" v-for="item in progressSummary" :key="item.key">
              <div class="progress-label">
                <span class="label-text">{{ item.label }}</span>
                <span class="label-value">{{ item.value }}%</span>
              </div>
              <el-progress 
                :percentage="item.value" 
                :color="item.color"
                :stroke-width="8"
                :show-text="false"
              />
            </div>
          </div>
          
          <div class="summary-footer">
            <el-button type="primary" size="small" @click="viewDetailedProgress">查看详细进度</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项 -->
    <el-card class="todo-card" shadow="never">
      <div class="card-header">
        <h3>我的待办</h3>
        <div class="todo-controls">
          <el-button type="text" @click="refreshTodos">刷新</el-button>
          <el-button type="primary" size="small" @click="viewAllTodos">查看全部</el-button>
        </div>
      </div>
      
      <el-table
        :data="todoList"
        style="width: 100%"
        :show-header="true"
        @row-click="handleTodoClick"
      >
        <el-table-column prop="title" label="任务标题" min-width="200">
          <template slot-scope="scope">
            <div class="todo-title">
              <i :class="getTodoIcon(scope.row.type)"></i>
              <span>{{ scope.row.title }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="type" label="任务类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTodoTypeColor(scope.row.type)" size="mini">
              {{ getTodoTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityColor(scope.row.priority)" size="mini">
              {{ getPriorityText(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="dueDate" label="截止时间" width="150" />
        
        <el-table-column prop="assignee" label="负责人" width="100" />
        
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click.stop="handleTodoAction(scope.row, 'process')">
              处理
            </el-button>
            <el-button type="text" size="mini" @click.stop="handleTodoAction(scope.row, 'defer')">
              延期
            </el-button>
            <el-button type="text" size="mini" @click.stop="handleTodoAction(scope.row, 'delegate')">
              委派
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetPreparationApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetPreparationIndex',
  data() {
    return {
      // 统计数据
      statistics: {
        totalTasks: 0,
        taskGrowth: 0,
        inProgressTasks: 0,
        progressRate: 0,
        totalBudgetAmount: 0,
        amountGrowth: 0,
        pendingApprovals: 0,
        approvalDecrease: 0
      },
      
      // 功能模块
      modules: [
        {
          key: 'taskManagement',
          title: '预算任务管理',
          description: '创建、分配和跟踪预算编制任务',
          icon: 'el-icon-s-order',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/taskManagement'
        },
        {
          key: 'dataEntry',
          title: '预算数据录入',
          description: '录入和维护预算数据，支持批量导入',
          icon: 'el-icon-edit-outline',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/budgetDataEntry'
        },
        {
          key: 'approvalFlow',
          title: '审批流程管理',
          description: '配置和管理预算审批流程',
          icon: 'el-icon-s-check',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/approvalFlow'
        },
        {
          key: 'budgetAdjustment',
          title: '预算调整管理',
          description: '处理预算调整申请和审批',
          icon: 'el-icon-refresh',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/budgetAdjustment'
        },
        {
          key: 'versionControl',
          title: '版本控制管理',
          description: '管理预算版本和历史记录',
          icon: 'el-icon-folder-opened',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/budgetVersion'
        },
        {
          key: 'templateManagement',
          title: '模板管理',
          description: '创建和维护预算编制模板',
          icon: 'el-icon-document-copy',
          taskCount: 0,
          lastUpdate: '-',
          path: '/qmys/budgetTemplate'
        }
      ],
      
      // 快速操作
      quickActions: [
        {
          key: 'createTask',
          title: '创建预算任务',
          description: '快速创建新的预算编制任务',
          icon: 'el-icon-plus',
          action: 'create-task'
        },
        {
          key: 'importData',
          title: '导入预算数据',
          description: '从Excel文件批量导入预算数据',
          icon: 'el-icon-upload2',
          action: 'import-data'
        },
        {
          key: 'generateReport',
          title: '生成预算报告',
          description: '生成预算编制进度报告',
          icon: 'el-icon-document',
          action: 'generate-report'
        },
        {
          key: 'batchApproval',
          title: '批量审批',
          description: '批量处理待审批的预算任务',
          icon: 'el-icon-check',
          action: 'batch-approval'
        }
      ],
      
      // 最近活动
      recentActivities: [],

      // 图表相关
      chartPeriod: 'month',
      progressChart: null,
      chartData: null,

      // 进度汇总
      progressSummary: [
        { key: 'overall', label: '总体进度', value: 0, color: '#409EFF' },
        { key: 'dataEntry', label: '数据录入', value: 0, color: '#67C23A' },
        { key: 'approval', label: '审批进度', value: 0, color: '#E6A23C' },
        { key: 'adjustment', label: '调整处理', value: 0, color: '#F56C6C' }
      ],

      // 待办事项
      todoList: []
    }
  },

  created() {
    this.loadDashboardData()
  },
  
  beforeDestroy() {
    if (this.progressChart) {
      this.progressChart.dispose()
    }
  },
  
  methods: {
    // 加载首页数据
    async loadDashboardData() {
      try {
        const [statsRes, todosRes, progressRes, activitiesRes, chartRes] = await Promise.allSettled([
          budgetPreparationApi.getStats(),
          budgetPreparationApi.getTodos(),
          budgetPreparationApi.getProgress(),
          budgetPreparationApi.getActivities(),
          budgetPreparationApi.getChartData()
        ])
        const isOk = (res) => res.status === 'fulfilled' && res.value && (res.value.code === 1 || String(res.value.code) === '1')
        if (isOk(statsRes)) {
          this.statistics = { ...this.statistics, ...statsRes.value.data }
        }
        if (isOk(todosRes)) {
          this.todoList = todosRes.value.data || []
        }
        if (isOk(progressRes)) {
          const progressData = progressRes.value.data || {}
          if (progressData.summary) {
            this.progressSummary.forEach(item => {
              if (progressData.summary[item.key] !== undefined) {
                item.value = progressData.summary[item.key]
              }
            })
          }
          if (progressData.modules) {
            this.modules.forEach(m => {
              const moduleData = progressData.modules[m.key]
              if (moduleData) {
                m.taskCount = moduleData.taskCount || 0
                m.lastUpdate = moduleData.lastUpdate || '-'
              }
            })
          }
        }
        if (isOk(activitiesRes)) {
          this.recentActivities = activitiesRes.value.data || []
        }
        if (isOk(chartRes)) {
          this.chartData = chartRes.value.data
        }
      } catch (error) {
        console.error('加载首页数据失败：', error)
      }
      this.$nextTick(() => this.initChart())
    },

    // 导航到模块
    navigateToModule(module) {
      this.$router.push(module.path)
    },
    
    // 快速操作
    handleQuickAction(action) {
      switch (action.action) {
        case 'create-task':
          this.$router.push('/qmys/taskManagement?action=create')
          break
        case 'import-data':
          this.$router.push('/qmys/budgetDataEntry?action=import')
          break
        case 'generate-report':
          this.$router.push('/qmys/taskManagement')
          break
        case 'batch-approval':
          this.$router.push('/qmys/approvalFlow?action=batch')
          break
      }
    },
    
    // 处理待办事项点击
    handleTodoClick(row) {
      // 根据待办类型跳转到相应页面
      const routeMap = {
        'approval': '/qmys/approvalFlow',
        'dataEntry': '/qmys/budgetDataEntry',
        'adjustment': '/qmys/budgetAdjustment',
        'report': '/qmys/taskManagement'
      }
      
      const route = routeMap[row.type]
      if (route) {
        this.$router.push({
          path: route,
          query: { todoId: row.id }
        })
      }
    },
    
    // 处理待办操作
    async handleTodoAction(row, action) {
      switch (action) {
        case 'process':
          this.handleTodoClick(row)
          break
        case 'defer':
          this.handleDeferTodo(row)
          break
        case 'delegate':
          this.handleDelegateTodo(row)
          break
      }
    },
    
    // 延期待办
    async handleDeferTodo(row) {
      try {
        const { value } = await this.$prompt('请输入延期到的日期（格式：yyyy-MM-dd）', '延期任务', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'date',
          inputValidator: (val) => !!val || '请选择日期'
        })
        await budgetPreparationApi.deferTodo(row.taskId || row.todoId, value)
        this.$message.success('任务延期成功')
        this.refreshTodos()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('延期失败：' + (error.message || error))
        }
      }
    },

    // 委派待办
    async handleDelegateTodo(row) {
      try {
        const { value } = await this.$prompt('请输入委派给的人员姓名', '委派任务', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (val) => !!val || '请输入人员姓名'
        })
        await budgetPreparationApi.delegateTodo(row.taskId || row.todoId, value)
        this.$message.success('任务委派成功')
        this.refreshTodos()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('委派失败：' + (error.message || error))
        }
      }
    },
    
    // 刷新待办
    async refreshTodos() {
      try {
        const response = await budgetPreparationApi.getTodos()
        if (response.code === 1) {
          this.todoList = response.data || []
        }
        this.$message.success('待办列表已刷新')
      } catch (error) {
        this.$message.error('刷新待办失败')
      }
    },
    
    // 查看全部待办
    viewAllTodos() {
      this.$router.push('/qmys/taskManagement')
    },

    // 查看全部操作
    viewAllActions() {
      this.$router.push('/qmys/taskManagement')
    },

    // 查看全部活动
    viewAllActivities() {
      this.$router.push('/qmys/taskManagement')
    },

    // 查看详细进度
    viewDetailedProgress() {
      this.$router.push('/qmys/taskManagement')
    },
    
    // 初始化图表
    initChart() {
      this.$nextTick(() => {
        if (this.$refs.progressChart) {
          this.progressChart = echarts.init(this.$refs.progressChart)
          this.updateChart()
        }
      })
    },
    
    // 更新图表
    async updateChart() {
      if (!this.progressChart) return

      // 每次切换周期都重新获取数据
      try {
        const response = await budgetPreparationApi.getChartData(this.chartPeriod)
        if (response.code === 1) {
          this.chartData = response.data
        }
      } catch (error) {
        console.error('获取图表数据失败：', error)
      }

      const cd = this.chartData || {}
      const periodData = cd[this.chartPeriod] || {}

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['计划进度', '实际进度', '完成任务数']
        },
        xAxis: {
          type: 'category',
          data: periodData.xAxis || []
        },
        yAxis: [
          { type: 'value', name: '进度(%)', position: 'left' },
          { type: 'value', name: '任务数', position: 'right' }
        ],
        series: [
          {
            name: '计划进度',
            type: 'line',
            data: periodData.planProgress || [],
            smooth: true,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '实际进度',
            type: 'line',
            data: periodData.actualProgress || [],
            smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '完成任务数',
            type: 'bar',
            yAxisIndex: 1,
            data: periodData.completedTasks || [],
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }

      this.progressChart.setOption(option)
    },
    
    // 格式化金额
    formatAmount(amount) {
      return (amount / 10000).toFixed(1) + '万'
    },
    
    // 获取活动状态类型
    getActivityStatusType(status) {
      const statusMap = {
        '已提交': 'warning',
        '已批准': 'success',
        '进行中': 'primary',
        '已完成': 'success'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取待办图标
    getTodoIcon(type) {
      const iconMap = {
        'approval': 'el-icon-s-check',
        'dataEntry': 'el-icon-edit-outline',
        'adjustment': 'el-icon-refresh',
        'report': 'el-icon-document'
      }
      return iconMap[type] || 'el-icon-s-order'
    },
    
    // 获取待办类型颜色
    getTodoTypeColor(type) {
      const colorMap = {
        'approval': 'warning',
        'dataEntry': 'primary',
        'adjustment': 'danger',
        'report': 'success'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取待办类型文本
    getTodoTypeText(type) {
      const textMap = {
        'approval': '审批',
        'dataEntry': '录入',
        'adjustment': '调整',
        'report': '报告'
      }
      return textMap[type] || type
    },
    
    // 获取优先级颜色
    getPriorityColor(priority) {
      const colorMap = {
        'high': 'danger',
        'medium': 'warning',
        'low': 'success'
      }
      return colorMap[priority] || 'info'
    },
    
    // 获取优先级文本
    getPriorityText(priority) {
      const textMap = {
        'high': '高',
        'medium': '中',
        'low': '低'
      }
      return textMap[priority] || priority
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-preparation-index {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
  
  .page-header {
    margin-bottom: 24px;
    
    h1 {
      color: #303133;
      font-size: 28px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }
    
    p {
      color: #606266;
      font-size: 16px;
      margin: 0;
    }
  }
  
  .stats-cards {
    margin-bottom: 24px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }
      
      .stat-content {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          i {
            font-size: 24px;
            color: white;
          }
          
          &.budget-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          
          &.progress-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.amount-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          
          &.approval-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }
        
        .stat-info {
          flex: 1;
          
          h3 {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 14px;
            color: #909399;
            margin: 0;
          }
        }
      }
      
      .stat-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .stat-trend {
          font-size: 12px;
          font-weight: 500;
          
          &.up {
            color: #67C23A;
          }
          
          &.down {
            color: #F56C6C;
          }
          
          i {
            margin-right: 2px;
          }
        }
        
        .stat-label {
          font-size: 12px;
          color: #C0C4CC;
        }
      }
    }
  }
  
  .module-nav-card {
    margin-bottom: 24px;
    border: none;
    border-radius: 8px;
    
    .module-nav-header {
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }
      
      p {
        font-size: 14px;
        color: #606266;
        margin: 0;
      }
    }
    
    .module-grid {
      .module-item {
        background: white;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        padding: 20px;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-bottom: 16px;
        
        &:hover {
          border-color: #409EFF;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          transform: translateY(-1px);
        }
        
        .module-icon {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, #409EFF, #36CFC9);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 16px;
          
          i {
            font-size: 20px;
            color: white;
          }
        }
        
        .module-content {
          margin-bottom: 16px;
          
          h4 {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 8px 0;
          }
          
          p {
            font-size: 14px;
            color: #606266;
            margin: 0 0 12px 0;
            line-height: 1.5;
          }
          
          .module-stats {
            display: flex;
            gap: 16px;
            
            .stat-item {
              font-size: 12px;
              color: #909399;
              display: flex;
              align-items: center;
              
              i {
                margin-right: 4px;
              }
            }
          }
        }
        
        .module-action {
          text-align: right;
        }
      }
    }
  }
  
  .quick-actions-card,
  .recent-activities-card,
  .chart-card,
  .progress-summary-card,
  .todo-card {
    border: none;
    border-radius: 8px;
    margin-bottom: 24px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }
    }
  }
  
  .quick-actions {
    .action-item {
      display: flex;
      align-items: center;
      padding: 16px;
      border: 1px solid #EBEEF5;
      border-radius: 8px;
      margin-bottom: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        border-color: #409EFF;
        background-color: #F0F9FF;
      }
      
      .action-icon {
        width: 36px;
        height: 36px;
        background: #409EFF;
        border-radius: 6px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        
        i {
          font-size: 16px;
          color: white;
        }
      }
      
      .action-content {
        flex: 1;
        
        h5 {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          margin: 0 0 4px 0;
        }
        
        p {
          font-size: 12px;
          color: #909399;
          margin: 0;
        }
      }
      
      .action-arrow {
        color: #C0C4CC;
      }
    }
  }
  
  .activities-list {
    .activity-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #F5F7FA;
      
      &:last-child {
        border-bottom: none;
      }
      
      .activity-avatar {
        margin-right: 12px;
      }
      
      .activity-content {
        flex: 1;
        
        .activity-text {
          font-size: 14px;
          color: #303133;
          margin: 0 0 4px 0;
          
          .activity-target {
            color: #409EFF;
          }
        }
        
        .activity-time {
          font-size: 12px;
          color: #909399;
          margin: 0;
        }
      }
      
      .activity-status {
        margin-left: 12px;
      }
    }
  }
  
  .chart-container {
    width: 100%;
  }
  
  .chart-controls {
    display: flex;
    align-items: center;
  }
  
  .progress-summary {
    .progress-item {
      margin-bottom: 20px;
      
      .progress-label {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;
        
        .label-text {
          font-size: 14px;
          color: #303133;
        }
        
        .label-value {
          font-size: 14px;
          font-weight: 600;
          color: #409EFF;
        }
      }
    }
  }
  
  .summary-footer {
    text-align: center;
    margin-top: 20px;
  }
  
  .todo-card {
    .todo-controls {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    
    .todo-title {
      display: flex;
      align-items: center;
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
}
</style>
