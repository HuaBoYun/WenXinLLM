<template>
  <div class="incentive-management-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon total">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ statistics.totalIncentives || 0 }}</div>
              <div class="stats-label">激励方案总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon active">
              <i class="el-icon-s-check"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ statistics.activeIncentives || 0 }}</div>
              <div class="stats-label">生效中方案</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon budget">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ formatAmount(statistics.totalBudget) }}</div>
              <div class="stats-label">预算总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon used">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ formatAmount(statistics.usedAmount) }}</div>
              <div class="stats-label">已使用金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 激励状态分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>激励状态分布</span>
            <el-button type="text" @click="refreshStatusChart">刷新</el-button>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 激励类型分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>激励类型分布</span>
            <el-button type="text" @click="refreshTypeChart">刷新</el-button>
          </div>
          <div ref="typeChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 趋势图表 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>激励完成趋势</span>
            <div class="header-controls">
              <el-date-picker
                v-model="trendTimeRange"
                type="monthrange"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                value-format="yyyy-MM"
                @change="refreshTrendChart"
                style="width: 240px; margin-right: 10px"
              />
              <el-button type="text" @click="refreshTrendChart">刷新</el-button>
            </div>
          </div>
          <div ref="trendChart" class="chart-container trend-chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 排行榜和待处理事项 -->
    <el-row :gutter="20" class="bottom-row">
      <!-- 激励排行榜 -->
      <el-col :span="12">
        <el-card shadow="never" class="ranking-card">
          <div slot="header" class="card-header">
            <span>激励排行榜</span>
            <el-select v-model="rankingType" @change="loadRanking" size="small" style="width: 120px">
              <el-option label="预算金额" value="BUDGET" />
              <el-option label="使用金额" value="USED" />
              <el-option label="完成率" value="COMPLETION" />
              <el-option label="满意度" value="SATISFACTION" />
            </el-select>
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in rankingList"
              :key="item.incentiveId"
              class="ranking-item"
              @click="viewIncentive(item)"
            >
              <div class="ranking-number" :class="getRankingClass(index)">
                {{ index + 1 }}
              </div>
              <div class="ranking-info">
                <div class="ranking-title">{{ item.incentiveTitle }}</div>
                <div class="ranking-subtitle">{{ item.targetDeptName }}</div>
              </div>
              <div class="ranking-value">
                {{ formatRankingValue(item, rankingType) }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 待处理事项 -->
      <el-col :span="12">
        <el-card shadow="never" class="todo-card">
          <div slot="header" class="card-header">
            <span>待处理事项</span>
            <el-button type="text" @click="loadTodoList">刷新</el-button>
          </div>
          <el-tabs v-model="todoActiveTab" class="todo-tabs">
            <el-tab-pane label="待审批" name="approval">
              <div class="todo-list">
                <div
                  v-for="item in todoList.approval"
                  :key="item.incentiveId"
                  class="todo-item"
                  @click="viewIncentive(item)"
                >
                  <div class="todo-icon approval">
                    <i class="el-icon-s-check"></i>
                  </div>
                  <div class="todo-info">
                    <div class="todo-title">{{ item.incentiveTitle }}</div>
                    <div class="todo-time">{{ formatDateTime(item.createdTime) }}</div>
                  </div>
                  <div class="todo-action">
                    <el-button type="text" size="small" @click.stop="quickApprove(item)">
                      快速审批
                    </el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="待发放" name="distribution">
              <div class="todo-list">
                <div
                  v-for="item in todoList.distribution"
                  :key="item.incentiveId"
                  class="todo-item"
                  @click="viewIncentive(item)"
                >
                  <div class="todo-icon distribution">
                    <i class="el-icon-coin"></i>
                  </div>
                  <div class="todo-info">
                    <div class="todo-title">{{ item.incentiveTitle }}</div>
                    <div class="todo-time">{{ formatDateTime(item.approvalTime) }}</div>
                  </div>
                  <div class="todo-action">
                    <el-button type="text" size="small" @click.stop="quickDistribute(item)">
                      快速发放
                    </el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="需跟进" name="followup">
              <div class="todo-list">
                <div
                  v-for="item in todoList.followup"
                  :key="item.incentiveId"
                  class="todo-item"
                  @click="viewIncentive(item)"
                >
                  <div class="todo-icon followup">
                    <i class="el-icon-warning"></i>
                  </div>
                  <div class="todo-info">
                    <div class="todo-title">{{ item.incentiveTitle }}</div>
                    <div class="todo-time">{{ formatDateTime(item.lastFollowUpTime) }}</div>
                  </div>
                  <div class="todo-action">
                    <el-button type="text" size="small" @click.stop="addFollowUp(item)">
                      添加跟进
                    </el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import incentiveManagementApi, { statisticsApi, quickApi } from '@/api/managementAccountant/pm/incentiveManagement'

export default {
  name: 'IncentiveManagementDashboard',
  data() {
    return {
      // 统计数据
      statistics: {
        totalIncentives: 0,
        activeIncentives: 0,
        totalBudget: 0,
        usedAmount: 0
      },
      
      // 图表实例
      statusChart: null,
      typeChart: null,
      trendChart: null,
      
      // 时间范围
      trendTimeRange: [],
      
      // 排行榜
      rankingType: 'BUDGET',
      rankingList: [],
      
      // 待处理事项
      todoActiveTab: 'approval',
      todoList: {
        approval: [],
        distribution: [],
        followup: []
      }
    }
  },
  
  mounted() {
    this.initTimeRange()
    this.loadStatistics()
    this.loadCharts()
    this.loadRanking()
    this.loadTodoList()
  },
  
  beforeDestroy() {
    // 销毁图表实例
    if (this.statusChart) {
      this.statusChart.dispose()
    }
    if (this.typeChart) {
      this.typeChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  
  methods: {
    // 初始化时间范围
    initTimeRange() {
      const now = new Date()
      const startMonth = new Date(now.getFullYear(), now.getMonth() - 5, 1)
      this.trendTimeRange = [
        this.formatMonth(startMonth),
        this.formatMonth(now)
      ]
    },
    
    // 格式化月份
    formatMonth(date) {
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        const currentYear = new Date().getFullYear()
        const response = await statisticsApi.getIncentiveStatistics(currentYear)
        if (response.success) {
          this.statistics = response.data
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 加载图表
    async loadCharts() {
      await Promise.all([
        this.loadStatusChart(),
        this.loadTypeChart(),
        this.loadTrendChart()
      ])
    },
    
    // 加载状态分布图表
    async loadStatusChart() {
      try {
        const currentYear = new Date().getFullYear()
        const response = await statisticsApi.getIncentiveStatusDistribution(currentYear)
        if (response.success) {
          this.renderStatusChart(response.data)
        }
      } catch (error) {
        console.error('加载状态分布图表失败:', error)
      }
    },
    
    // 渲染状态分布图表
    renderStatusChart(data) {
      if (!this.$refs.statusChart) return
      
      if (this.statusChart) {
        this.statusChart.dispose()
      }
      
      this.statusChart = echarts.init(this.$refs.statusChart)
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '激励状态',
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
            data: data
          }
        ]
      }
      
      this.statusChart.setOption(option)
    },
    
    // 加载类型分布图表
    async loadTypeChart() {
      try {
        const currentYear = new Date().getFullYear()
        const response = await statisticsApi.getIncentiveTypeDistribution(currentYear)
        if (response.success) {
          this.renderTypeChart(response.data)
        }
      } catch (error) {
        console.error('加载类型分布图表失败:', error)
      }
    },
    
    // 渲染类型分布图表
    renderTypeChart(data) {
      if (!this.$refs.typeChart) return
      
      if (this.typeChart) {
        this.typeChart.dispose()
      }
      
      this.typeChart = echarts.init(this.$refs.typeChart)
      
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
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      
      this.typeChart.setOption(option)
    },
    
    // 加载趋势图表
    async loadTrendChart() {
      try {
        if (!this.trendTimeRange || this.trendTimeRange.length !== 2) return
        
        const [startTime, endTime] = this.trendTimeRange
        const response = await statisticsApi.getIncentiveCompletionTrend(
          `${startTime}-01`,
          `${endTime}-31`
        )
        if (response.success) {
          this.renderTrendChart(response.data)
        }
      } catch (error) {
        console.error('加载趋势图表失败:', error)
      }
    },
    
    // 渲染趋势图表
    renderTrendChart(data) {
      if (!this.$refs.trendChart) return
      
      if (this.trendChart) {
        this.trendChart.dispose()
      }
      
      this.trendChart = echarts.init(this.$refs.trendChart)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新建', '完成', '取消']
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
          data: data.map(item => item.month)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新建',
            type: 'line',
            stack: 'Total',
            data: data.map(item => item.created)
          },
          {
            name: '完成',
            type: 'line',
            stack: 'Total',
            data: data.map(item => item.completed)
          },
          {
            name: '取消',
            type: 'line',
            stack: 'Total',
            data: data.map(item => item.cancelled)
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    
    // 加载排行榜
    async loadRanking() {
      try {
        const currentYear = new Date().getFullYear()
        const response = await statisticsApi.getIncentiveRanking(currentYear, this.rankingType, 10)
        if (response.success) {
          this.rankingList = response.data
        }
      } catch (error) {
        console.error('加载排行榜失败:', error)
      }
    },
    
    // 加载待处理事项
    async loadTodoList() {
      try {
        const [approvalResponse, distributionResponse, followupResponse] = await Promise.all([
          incentiveManagementApi.getPendingApproval(),
          incentiveManagementApi.getPendingDistribution(),
          incentiveManagementApi.getNeedFollowUp()
        ])
        
        if (approvalResponse.success) {
          this.todoList.approval = approvalResponse.data
        }
        if (distributionResponse.success) {
          this.todoList.distribution = distributionResponse.data
        }
        if (followupResponse.success) {
          this.todoList.followup = followupResponse.data
        }
      } catch (error) {
        console.error('加载待处理事项失败:', error)
      }
    },

    // 刷新状态图表
    refreshStatusChart() {
      this.loadStatusChart()
    },

    // 刷新类型图表
    refreshTypeChart() {
      this.loadTypeChart()
    },

    // 刷新趋势图表
    refreshTrendChart() {
      this.loadTrendChart()
    },

    // 查看激励方案
    viewIncentive(item) {
      this.$router.push({
        name: 'IncentiveManagementDetail',
        params: { id: item.incentiveId },
        query: { mode: 'view' }
      })
    },

    // 快速审批
    async quickApprove(item) {
      try {
        await this.$confirm('确认快速审批通过该激励方案吗？', '快速审批', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 这里需要获取当前用户信息
        const currentUser = { id: 1, name: '当前用户' } // 实际应该从store或API获取

        const response = await quickApi.quickApprove(item.incentiveId, currentUser.id, currentUser.name)
        if (response.success) {
          this.$message.success('审批成功')
          this.loadTodoList()
          this.loadStatistics()
        } else {
          this.$message.error(response.message || '审批失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('快速审批失败:', error)
          this.$message.error('审批失败')
        }
      }
    },

    // 快速发放
    async quickDistribute(item) {
      try {
        await this.$confirm('确认快速发放该激励方案吗？', '快速发放', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await quickApi.quickDistribute(item.incentiveId)
        if (response.success) {
          this.$message.success('发放成功')
          this.loadTodoList()
          this.loadStatistics()
        } else {
          this.$message.error(response.message || '发放失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('快速发放失败:', error)
          this.$message.error('发放失败')
        }
      }
    },

    // 添加跟进
    async addFollowUp(item) {
      try {
        const { value: followUpRecord } = await this.$prompt('请输入跟进记录', '添加跟进', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value) => {
            if (!value) {
              return '跟进记录不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.addFollowUpRecord(item.incentiveId, followUpRecord)
        if (response.success) {
          this.$message.success('添加跟进成功')
          this.loadTodoList()
        } else {
          this.$message.error(response.message || '添加跟进失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('添加跟进失败:', error)
          this.$message.error('添加跟进失败')
        }
      }
    },

    // 获取排行榜样式类
    getRankingClass(index) {
      if (index === 0) return 'top1'
      if (index === 1) return 'top2'
      if (index === 2) return 'top3'
      return 'other'
    },

    // 格式化排行榜值
    formatRankingValue(item, type) {
      switch (type) {
        case 'BUDGET':
          return this.formatAmount(item.totalBudget)
        case 'USED':
          return this.formatAmount(item.usedAmount)
        case 'COMPLETION':
          return `${item.completionRate || 0}%`
        case 'SATISFACTION':
          return `${item.satisfactionScore || 0}分`
        default:
          return '-'
      }
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('MM-DD HH:mm')
    }
  }
}
</script>

<style lang="scss" scoped>
.incentive-management-dashboard {
  .stats-row {
    margin-bottom: 20px;
    
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
          margin-right: 15px;
          
          i {
            font-size: 24px;
            color: white;
          }
          
          &.total {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          
          &.active {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.budget {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          
          &.used {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }
        
        .stats-info {
          .stats-number {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            line-height: 1;
          }
          
          .stats-label {
            font-size: 14px;
            color: #909399;
            margin-top: 5px;
          }
        }
      }
    }
  }
  
  .charts-row {
    margin-bottom: 20px;
    
    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .header-controls {
          display: flex;
          align-items: center;
        }
      }
      
      .chart-container {
        height: 300px;
        
        &.trend-chart {
          height: 400px;
        }
      }
    }
  }
  
  .bottom-row {
    .ranking-card, .todo-card {
      height: 500px;
      
      .ranking-list, .todo-list {
        max-height: 400px;
        overflow-y: auto;
      }
      
      .ranking-item, .todo-item {
        display: flex;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        cursor: pointer;
        
        &:hover {
          background-color: #f5f7fa;
        }
        
        &:last-child {
          border-bottom: none;
        }
      }
      
      .ranking-item {
        .ranking-number {
          width: 30px;
          height: 30px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: bold;
          color: white;
          margin-right: 15px;
          
          &.top1 {
            background: #f56c6c;
          }
          
          &.top2 {
            background: #e6a23c;
          }
          
          &.top3 {
            background: #67c23a;
          }
          
          &.other {
            background: #909399;
          }
        }
        
        .ranking-info {
          flex: 1;
          
          .ranking-title {
            font-size: 14px;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .ranking-subtitle {
            font-size: 12px;
            color: #909399;
          }
        }
        
        .ranking-value {
          font-size: 16px;
          font-weight: bold;
          color: #409EFF;
        }
      }
      
      .todo-item {
        .todo-icon {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 15px;
          
          i {
            font-size: 18px;
            color: white;
          }
          
          &.approval {
            background: #e6a23c;
          }
          
          &.distribution {
            background: #67c23a;
          }
          
          &.followup {
            background: #f56c6c;
          }
        }
        
        .todo-info {
          flex: 1;
          
          .todo-title {
            font-size: 14px;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .todo-time {
            font-size: 12px;
            color: #909399;
          }
        }
        
        .todo-action {
          .el-button {
            padding: 0;
          }
        }
      }
    }
    
    .todo-tabs {
      ::v-deep .el-tabs__content {
        padding: 0;
      }
    }
  }
}
</style>
