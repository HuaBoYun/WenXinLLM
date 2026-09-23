<template>
  <div class="assessment360-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>360度评估</h2>
      <p>全方位多维度评估系统，包含自评、上级评价、同级评价、下级评价和客户评价</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-view"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalEvaluations || 0 }}</div>
              <div class="stat-label">评估总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.activeEvaluations || 0 }}</div>
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
              <div class="stat-number">{{ overviewData.completedEvaluations || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon participants">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalParticipants || 0 }}</div>
              <div class="stat-label">参与人数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToEvaluationList">
            <div class="nav-icon list">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>评估列表</h3>
              <p>查看和管理所有360度评估</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToEvaluationDashboard">
            <div class="nav-icon dashboard">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>评估仪表板</h3>
              <p>评估统计和分析</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="createNewEvaluation">
            <div class="nav-icon create">
              <i class="el-icon-plus"></i>
            </div>
            <div class="nav-content">
              <h3>创建评估</h3>
              <p>启动新的360度评估</p>
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
          <div class="quick-item" @click="quickCreateEvaluation">
            <i class="el-icon-plus"></i>
            <span>创建评估</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewMyEvaluations">
            <i class="el-icon-user"></i>
            <span>我的评估</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewPendingEvaluations">
            <i class="el-icon-time"></i>
            <span>待评估</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewEvaluationTemplates">
            <i class="el-icon-collection"></i>
            <span>评估模板</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewReports">
            <i class="el-icon-document-copy"></i>
            <span>评估报告</span>
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
  name: 'Assessment360Index',
  data() {
    return {
      // 概览数据
      overviewData: {
        totalEvaluations: 0,
        activeEvaluations: 0,
        completedEvaluations: 0,
        totalParticipants: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '启动了年度360度评估',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '完成了管理层360度评估',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '发送了评估邀请通知',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '生成了评估结果报告',
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
          totalEvaluations: 89,
          activeEvaluations: 23,
          completedEvaluations: 66,
          totalParticipants: 245
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 导航到评估列表
    navigateToEvaluationList() {
      this.$router.push('/pm/360-assessment/list')
    },
    // 导航到评估仪表板
    navigateToEvaluationDashboard() {
      this.$router.push('/pm/360-assessment/dashboard')
    },
    // 创建新评估
    createNewEvaluation() {
      this.$router.push('/pm/360-assessment/create')
    },
    // 快速创建评估
    quickCreateEvaluation() {
      this.$router.push('/pm/360-assessment/create')
    },
    // 查看我的评估
    quickViewMyEvaluations() {
      this.$router.push('/pm/360-assessment/list?filter=my')
    },
    // 查看待评估
    quickViewPendingEvaluations() {
      this.$router.push('/pm/360-assessment/list?filter=pending')
    },
    // 查看评估模板
    quickViewEvaluationTemplates() {
      this.$message.info('评估模板功能开发中...')
    },
    // 查看评估报告
    quickViewReports() {
      this.$message.info('评估报告功能开发中...')
    },
    // 查看帮助文档
    quickViewHelp() {
      this.$message.info('帮助文档功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment360-container {
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
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.completed {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.participants {
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
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.dashboard {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.create {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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
