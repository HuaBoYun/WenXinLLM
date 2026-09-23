<template>
  <div class="incentive-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>激励管理</h2>
      <p>全面的激励管理系统，包含激励方案设计、发放管理、效果评估和激励分析</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-medal"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalIncentives || 0 }}</div>
              <div class="stat-label">激励总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.activeIncentives || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon distributed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.distributedIncentives || 0 }}</div>
              <div class="stat-label">已发放</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ formatAmount(overviewData.totalAmount) || '0' }}</div>
              <div class="stat-label">激励总额</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToIncentiveList">
            <div class="nav-icon list">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>激励列表</h3>
              <p>查看和管理所有激励方案</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToIncentiveDashboard">
            <div class="nav-icon dashboard">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>激励仪表板</h3>
              <p>激励统计和分析</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="createNewIncentive">
            <div class="nav-icon create">
              <i class="el-icon-plus"></i>
            </div>
            <div class="nav-content">
              <h3>创建激励</h3>
              <p>设计新的激励方案</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <h3>最近活动</h3>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.time"
          :type="activity.type"
        >
          {{ activity.description }}
        </el-timeline-item>
      </el-timeline>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access-section">
      <h3>快速访问</h3>
      <el-row :gutter="15">
        <el-col :span="4">
          <div class="quick-item" @click="quickCreateIncentive">
            <i class="el-icon-plus"></i>
            <span>创建激励</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewMyIncentives">
            <i class="el-icon-user"></i>
            <span>我的激励</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewPendingIncentives">
            <i class="el-icon-time"></i>
            <span>待审批</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewIncentiveTemplates">
            <i class="el-icon-collection"></i>
            <span>激励模板</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewReports">
            <i class="el-icon-document-copy"></i>
            <span>激励报告</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewHelp">
            <i class="el-icon-question"></i>
            <span>帮助文档</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'IncentiveManagementIndex',
  data() {
    return {
      // 概览数据
      overviewData: {
        totalIncentives: 0,
        activeIncentives: 0,
        distributedIncentives: 0,
        totalAmount: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '创建了Q4季度销售激励方案',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '发放了年度优秀员工奖励',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '审批了团队绩效奖金',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '生成了激励效果分析报告',
          time: '2024-01-19 16:20:00',
          type: 'warning'
        }
      ]
    }
  },
  created() {
    this.loadOverviewData()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        // 这里可以调用API获取实际数据
        // 暂时使用模拟数据
        this.overviewData = {
          totalIncentives: 67,
          activeIncentives: 23,
          distributedIncentives: 44,
          totalAmount: 1250000
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0'
      return (amount / 10000).toFixed(1) + '万'
    },
    // 导航到激励列表
    navigateToIncentiveList() {
      this.$router.push('/pm/incentive-management/list')
    },
    // 导航到激励仪表板
    navigateToIncentiveDashboard() {
      this.$router.push('/pm/incentive-management/dashboard')
    },
    // 创建新激励
    createNewIncentive() {
      this.$router.push('/pm/incentive-management/create')
    },
    // 快速创建激励
    quickCreateIncentive() {
      this.$router.push('/pm/incentive-management/create')
    },
    // 查看我的激励
    quickViewMyIncentives() {
      this.$router.push('/pm/incentive-management/list?filter=my')
    },
    // 查看待审批激励
    quickViewPendingIncentives() {
      this.$router.push('/pm/incentive-management/list?filter=pending')
    },
    // 查看激励模板
    quickViewIncentiveTemplates() {
      this.$message.info('激励模板功能开发中...')
    },
    // 查看激励报告
    quickViewReports() {
      this.$message.info('激励报告功能开发中...')
    },
    // 查看帮助文档
    quickViewHelp() {
      this.$message.info('帮助文档功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.incentive-management-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 30px;
    
    h2 {
      margin: 0 0 10px 0;
      color: #303133;
      font-size: 24px;
      font-weight: 600;
    }
    
    p {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }
  
  .overview-section {
    margin-bottom: 30px;
    
    .stat-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        
        i {
          font-size: 24px;
          color: #fff;
        }
        
        &.total {
          background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.distributed {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.amount {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
      }
      
      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          line-height: 1;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
  
  .navigation-section {
    margin-bottom: 30px;
    
    .nav-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      }
      
      .nav-icon {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        
        i {
          font-size: 20px;
          color: #fff;
        }
        
        &.list {
          background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
        }
        
        &.dashboard {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.create {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
      }
      
      .nav-content {
        h3 {
          margin: 0 0 5px 0;
          color: #303133;
          font-size: 16px;
          font-weight: 600;
        }
        
        p {
          margin: 0;
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }
  
  .activity-section {
    margin-bottom: 30px;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .quick-access-section {
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
    
    .quick-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409eff;
        background: #f0f9ff;
      }
      
      i {
        font-size: 24px;
        color: #409eff;
        margin-bottom: 10px;
      }
      
      span {
        font-size: 12px;
        color: #606266;
        text-align: center;
      }
    }
  }
}
</style>
