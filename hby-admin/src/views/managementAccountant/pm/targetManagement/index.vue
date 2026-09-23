<template>
  <div class="target-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>目标管理</h2>
      <p>全面的目标管理解决方案，包含目标设定、分解、跟踪、调整和评估</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-aim"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalTargets || 0 }}</div>
              <div class="stat-label">目标总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.activeTargets || 0 }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.completedTargets || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon overdue">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.overdueTargets || 0 }}</div>
              <div class="stat-label">已逾期</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTargetList">
            <div class="nav-icon list">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>目标列表</h3>
              <p>查看和管理所有目标</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTargetDashboard">
            <div class="nav-icon dashboard">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>目标仪表板</h3>
              <p>目标统计和分析</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="createNewTarget">
            <div class="nav-icon create">
              <i class="el-icon-plus"></i>
            </div>
            <div class="nav-content">
              <h3>创建目标</h3>
              <p>设定新的目标</p>
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
          <div class="quick-item" @click="quickCreateTarget">
            <i class="el-icon-plus"></i>
            <span>创建目标</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewMyTargets">
            <i class="el-icon-user"></i>
            <span>我的目标</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewTeamTargets">
            <i class="el-icon-s-custom"></i>
            <span>团队目标</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewOverdueTargets">
            <i class="el-icon-warning-outline"></i>
            <span>逾期目标</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewReports">
            <i class="el-icon-document-copy"></i>
            <span>目标报告</span>
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
  name: 'TargetManagementIndex',
  data() {
    return {
      // 概览数据
      overviewData: {
        totalTargets: 0,
        activeTargets: 0,
        completedTargets: 0,
        overdueTargets: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '创建了Q4季度销售目标',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '完成了客户满意度目标',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '调整了产品开发目标',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '市场推广目标即将到期',
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
          totalTargets: 156,
          activeTargets: 89,
          completedTargets: 45,
          overdueTargets: 22
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 导航到目标列表
    navigateToTargetList() {
      this.$router.push('/pm/target-management/list')
    },
    // 导航到目标仪表板
    navigateToTargetDashboard() {
      this.$router.push('/pm/target-dashboard')
    },
    // 创建新目标
    createNewTarget() {
      this.$router.push('/pm/target-management/create')
    },
    // 快速创建目标
    quickCreateTarget() {
      this.$router.push('/pm/target-management/create')
    },
    // 查看我的目标
    quickViewMyTargets() {
      this.$router.push('/pm/target-management/list?filter=my')
    },
    // 查看团队目标
    quickViewTeamTargets() {
      this.$router.push('/pm/target-management/list?filter=team')
    },
    // 查看逾期目标
    quickViewOverdueTargets() {
      this.$router.push('/pm/target-management/list?filter=overdue')
    },
    // 查看目标报告
    quickViewReports() {
      this.$message.info('目标报告功能开发中...')
    },
    // 查看帮助文档
    quickViewHelp() {
      this.$message.info('帮助文档功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.target-management-container {
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
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.completed {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.overdue {
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
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
