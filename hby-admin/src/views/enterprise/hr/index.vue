<template>
  <div class="hr-management" :style="themeVars">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-user"></i>
          人力资源管理
        </h1>
        <p class="page-description">企业人力资源规划、员工管理、薪酬福利、人才发展全流程管理</p>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ hrStats.totalEmployees }}</div>
              <div class="stats-label">员工总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ hrStats.highPerformers }}</div>
              <div class="stats-label">优秀员工</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon">
              <i class="el-icon-reading"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ hrStats.trainingPrograms }}</div>
              <div class="stats-label">培训项目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ hrStats.avgSalary }}</div>
              <div class="stats-label">平均薪资(万)</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="员工管理" name="employee">
          <EmployeeManagement ref="employeeRef" />
        </el-tab-pane>
        <el-tab-pane label="组织架构" name="organization">
          <OrganizationStructure ref="organizationRef" />
        </el-tab-pane>
        <el-tab-pane label="薪酬福利" name="compensation">
          <CompensationManagement ref="compensationRef" />
        </el-tab-pane>
        <el-tab-pane label="绩效考核" name="performance">
          <PerformanceEvaluation ref="performanceRef" />
        </el-tab-pane>
        <el-tab-pane label="培训发展" name="training">
          <TrainingDevelopment ref="trainingRef" />
        </el-tab-pane>
        <el-tab-pane label="招聘管理" name="recruitment">
          <RecruitmentManagement ref="recruitmentRef" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import EmployeeManagement from './components/EmployeeManagement.vue'
import OrganizationStructure from './components/OrganizationStructure.vue'
import CompensationManagement from './components/CompensationManagement.vue'
import PerformanceEvaluation from './components/PerformanceEvaluation.vue'
import TrainingDevelopment from './components/TrainingDevelopment.vue'
import RecruitmentManagement from './components/RecruitmentManagement.vue'
import { hrStatisticsApi } from '@/api/enterprise/hr'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'HRManagement',
  mixins: [investThemeMixin],
  components: {
    EmployeeManagement,
    OrganizationStructure,
    CompensationManagement,
    PerformanceEvaluation,
    TrainingDevelopment,
    RecruitmentManagement
  },
  data() {
    return {
      activeTab: 'employee',
      hrStats: {
        totalEmployees: 0,
        highPerformers: 0,
        trainingPrograms: 0,
        avgSalary: 0
      }
    }
  },
  mounted() {
    this.loadHRStats()
  },
  methods: {
    // 加载人力资源统计数据
    async loadHRStats() {
      try {
        const res = await hrStatisticsApi.getHROverviewStats()
        if (res && res.data) {
          this.hrStats = {
            totalEmployees: res.data.totalEmployees || 0,
            highPerformers: res.data.highPerformers || 0,
            trainingPrograms: res.data.trainingPrograms || 0,
            avgSalary: res.data.avgSalary || 0
          }
        }
      } catch (error) {
        console.error('加载人力资源统计数据失败:', error)
      }
    },

    // 处理标签页切换
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    }
  }
}
</script>

<style lang="scss" scoped>
.hr-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
    border-radius: 12px;
    padding: 30px;
    margin-bottom: 20px;
    color: white;

    .header-content {
      .page-title {
        font-size: 28px;
        font-weight: 600;
        margin: 0 0 10px 0;
        display: flex;
        align-items: center;

        i {
          margin-right: 12px;
          font-size: 32px;
        }
      }

      .page-description {
        font-size: 16px;
        opacity: 0.9;
        margin: 0;
      }
    }
  }

  .stats-overview {
    margin-bottom: 20px;

    .stats-card {
      background: white;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-2px);
      }

      .stats-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);

        i {
          font-size: 24px;
          color: white;
        }
      }

      .stats-content {
        .stats-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }

        .stats-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

  .function-nav {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    :deep(.el-tabs__header) {
      margin-bottom: 20px;
    }

    :deep(.el-tabs__nav-wrap::after) {
      display: none;
    }

    :deep(.el-tabs__item) {
      font-size: 16px;
      font-weight: 500;
      padding: 0 20px;
    }

    :deep(.el-tabs__item.is-active) {
      color: #667eea;
    }

    :deep(.el-tabs__active-bar) {
      background-color: #667eea;
    }
  }
}
</style>
