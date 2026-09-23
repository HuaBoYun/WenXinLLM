<template>
  <div class="pm-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>绩效管理</h2>
      <p>全面的绩效管理解决方案，包含目标管理、考核方案、360度评估、绩效面谈、绩效校准和激励管理</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon target">
              <i class="el-icon-aim"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.targetCount || 0 }}</div>
              <div class="stat-label">目标总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon assessment">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.assessmentCount || 0 }}</div>
              <div class="stat-label">考核方案</div>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon evaluation">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.evaluationCount || 0 }}</div>
              <div class="stat-label">360度评估</div>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon interview">
              <i class="el-icon-chat-dot-round"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.interviewCount || 0 }}</div>
              <div class="stat-label">绩效面谈</div>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon calibration">
              <i class="el-icon-scale-to-original"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.calibrationCount || 0 }}</div>
              <div class="stat-label">绩效校准</div>
            </div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="stat-card">
            <div class="stat-icon incentive">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.incentiveCount || 0 }}</div>
              <div class="stat-label">激励管理</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTargetManagement">
            <div class="nav-icon target">
              <i class="el-icon-aim"></i>
            </div>
            <div class="nav-content">
              <h3>目标管理</h3>
              <p>目标设定、分解、跟踪和评估</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAssessmentPlan">
            <div class="nav-icon assessment">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="nav-content">
              <h3>考核方案配置</h3>
              <p>考核模式、指标体系和评分规则</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAssessment360">
            <div class="nav-icon evaluation">
              <i class="el-icon-user"></i>
            </div>
            <div class="nav-content">
              <h3>360度评估</h3>
              <p>多维度评估和匿名评价</p>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToPerformanceInterview">
            <div class="nav-icon interview">
              <i class="el-icon-chat-dot-round"></i>
            </div>
            <div class="nav-content">
              <h3>绩效面谈管理</h3>
              <p>面谈计划、执行和跟踪</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToPerformanceCalibration">
            <div class="nav-icon calibration">
              <i class="el-icon-scale-to-original"></i>
            </div>
            <div class="nav-content">
              <h3>绩效校准管理</h3>
              <p>校准会议、结果调整和一致性</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToIncentiveManagement">
            <div class="nav-icon incentive">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="nav-content">
              <h3>激励管理</h3>
              <p>激励方案、发放和效果评估</p>
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
          <div class="quick-item" @click="quickCreateAssessment">
            <i class="el-icon-document-add"></i>
            <span>创建考核方案</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickStart360Evaluation">
            <i class="el-icon-user-solid"></i>
            <span>启动360评估</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickScheduleInterview">
            <i class="el-icon-date"></i>
            <span>安排面谈</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewReports">
            <i class="el-icon-data-analysis"></i>
            <span>绩效报告</span>
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
  name: 'PerformanceManagementIndex',
  data() {
    return {
      // 概览数据
      overviewData: {
        targetCount: 0,
        assessmentCount: 0,
        evaluationCount: 0,
        interviewCount: 0,
        calibrationCount: 0,
        incentiveCount: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '完成了Q4季度目标设定',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '启动了年度360度评估',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '创建了新的考核方案',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '安排了绩效面谈会议',
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
          targetCount: 156,
          assessmentCount: 23,
          evaluationCount: 89,
          interviewCount: 45,
          calibrationCount: 12,
          incentiveCount: 67
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 导航到目标管理
    navigateToTargetManagement() {
      this.$router.push('/pm/target-management')
    },
    // 导航到考核方案配置
    navigateToAssessmentPlan() {
      this.$router.push('/pm/assessment-plan')
    },
    // 导航到360度评估
    navigateToAssessment360() {
      this.$router.push('/pm/360-assessment')
    },
    // 导航到绩效面谈管理
    navigateToPerformanceInterview() {
      this.$router.push('/pm/performance-interview')
    },
    // 导航到绩效校准管理
    navigateToPerformanceCalibration() {
      this.$router.push('/pm/performance-calibration')
    },
    // 导航到激励管理
    navigateToIncentiveManagement() {
      this.$router.push('/pm/incentive-management')
    },
    // 快速创建目标
    quickCreateTarget() {
      this.$router.push('/pm/target-management/create')
    },
    // 快速创建考核方案
    quickCreateAssessment() {
      this.$router.push('/pm/assessment-plan/create')
    },
    // 快速启动360评估
    quickStart360Evaluation() {
      this.$router.push('/pm/assessment-360/create')
    },
    // 快速安排面谈
    quickScheduleInterview() {
      this.$router.push('/pm/performance-interview/create')
    },
    // 查看绩效报告
    quickViewReports() {
      this.$message.info('绩效报告功能开发中...')
    },
    // 查看帮助文档
    quickViewHelp() {
      this.$message.info('帮助文档功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.pm-container {
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
        
        &.target {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.assessment {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.evaluation {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.interview {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.calibration {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        
        &.incentive {
          background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
        
        &.target {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.assessment {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.evaluation {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.interview {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.calibration {
          background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        
        &.incentive {
          background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
