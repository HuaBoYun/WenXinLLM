<template>
  <div class="assessment-plan-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>考核方案配置</h2>
      <p>全面的考核方案管理，包含考核模式、指标体系、评分规则和流程配置</p>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.totalPlans || 0 }}</div>
              <div class="stat-label">方案总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.activePlans || 0 }}</div>
              <div class="stat-label">启用中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon draft">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.draftPlans || 0 }}</div>
              <div class="stat-label">草稿</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon template">
              <i class="el-icon-collection"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ overviewData.templatePlans || 0 }}</div>
              <div class="stat-label">模板</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAssessmentList">
            <div class="nav-icon list">
              <i class="el-icon-document"></i>
            </div>
            <div class="nav-content">
              <h3>考核方案列表</h3>
              <p>查看和管理所有考核方案</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToAssessmentWizard">
            <div class="nav-icon wizard">
              <i class="el-icon-magic-stick"></i>
            </div>
            <div class="nav-content">
              <h3>配置向导</h3>
              <p>使用向导创建考核方案</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="createNewAssessment">
            <div class="nav-icon create">
              <i class="el-icon-plus"></i>
            </div>
            <div class="nav-content">
              <h3>创建方案</h3>
              <p>创建新的考核方案</p>
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
          <div class="quick-item" @click="quickCreateAssessment">
            <i class="el-icon-plus"></i>
            <span>创建方案</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewTemplates">
            <i class="el-icon-collection"></i>
            <span>方案模板</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewIndicators">
            <i class="el-icon-data-line"></i>
            <span>指标库</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewRules">
            <i class="el-icon-setting"></i>
            <span>评分规则</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewReports">
            <i class="el-icon-document-copy"></i>
            <span>考核报告</span>
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
  name: 'AssessmentPlanIndex',
  data() {
    return {
      // 概览数据
      overviewData: {
        totalPlans: 0,
        activePlans: 0,
        draftPlans: 0,
        templatePlans: 0
      },
      // 最近活动
      recentActivities: [
        {
          id: 1,
          description: '创建了年度绩效考核方案',
          time: '2024-01-20 10:30:00',
          type: 'primary'
        },
        {
          id: 2,
          description: '启用了季度考核方案',
          time: '2024-01-20 09:15:00',
          type: 'success'
        },
        {
          id: 3,
          description: '修改了销售部门考核指标',
          time: '2024-01-20 08:45:00',
          type: 'info'
        },
        {
          id: 4,
          description: '导入了新的考核模板',
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
          totalPlans: 23,
          activePlans: 15,
          draftPlans: 6,
          templatePlans: 8
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    // 导航到考核方案列表
    navigateToAssessmentList() {
      this.$router.push('/pm/assessment-plan/list')
    },
    // 导航到配置向导
    navigateToAssessmentWizard() {
      this.$router.push('/pm/assessment-plan/wizard')
    },
    // 创建新考核方案
    createNewAssessment() {
      this.$router.push('/pm/assessment-plan/create')
    },
    // 快速创建考核方案
    quickCreateAssessment() {
      this.$router.push('/pm/assessment-plan/create')
    },
    // 查看方案模板
    quickViewTemplates() {
      this.$router.push('/pm/assessment-plan/list?filter=template')
    },
    // 查看指标库
    quickViewIndicators() {
      this.$message.info('指标库功能开发中...')
    },
    // 查看评分规则
    quickViewRules() {
      this.$message.info('评分规则功能开发中...')
    },
    // 查看考核报告
    quickViewReports() {
      this.$message.info('考核报告功能开发中...')
    },
    // 查看帮助文档
    quickViewHelp() {
      this.$message.info('帮助文档功能开发中...')
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment-plan-container {
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
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.active {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
        
        &.draft {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.template {
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
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.wizard {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
