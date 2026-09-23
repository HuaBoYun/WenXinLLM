<template>
  <div class="innovation-management" :style="themeVars">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-cpu"></i>
          技术创新管理
        </h1>
        <p class="page-description">研发项目管理、知识产权保护、技术合作、创新成果转化全流程管理</p>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon project">
              <i class="el-icon-folder-opened"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ innovationStats.activeProjects }}</div>
              <div class="stats-label">在研项目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon patent">
              <i class="el-icon-medal"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ innovationStats.totalPatents }}</div>
              <div class="stats-label">专利总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon investment">
              <i class="el-icon-money"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ innovationStats.rdInvestment }}</div>
              <div class="stats-label">研发投入(万)</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon achievement">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ innovationStats.achievements }}</div>
              <div class="stats-label">成果转化</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="研发项目" name="project">
          <RDProjectManagement ref="projectRef" />
        </el-tab-pane>
        <el-tab-pane label="知识产权" name="patent">
          <PatentManagement ref="patentRef" />
        </el-tab-pane>
        <el-tab-pane label="技术合作" name="cooperation">
          <TechCooperationManagement ref="cooperationRef" />
        </el-tab-pane>
        <el-tab-pane label="成果转化" name="transformation">
          <AchievementTransformation ref="transformationRef" />
        </el-tab-pane>
        <el-tab-pane label="创新团队" name="team">
          <InnovationTeamManagement ref="teamRef" />
        </el-tab-pane>
        <el-tab-pane label="技术评估" name="evaluation">
          <TechEvaluationManagement ref="evaluationRef" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import RDProjectManagement from './components/RDProjectManagement.vue'
import PatentManagement from './components/PatentManagement.vue'
import TechCooperationManagement from './components/TechCooperationManagement.vue'
import AchievementTransformation from './components/AchievementTransformation.vue'
import InnovationTeamManagement from './components/InnovationTeamManagement.vue'
import TechEvaluationManagement from './components/TechEvaluationManagement.vue'
import { getInnovationStatistics } from '@/api/enterprise/innovation'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'InnovationManagement',
  mixins: [investThemeMixin],
  components: {
    RDProjectManagement,
    PatentManagement,
    TechCooperationManagement,
    AchievementTransformation,
    InnovationTeamManagement,
    TechEvaluationManagement
  },
  data() {
    return {
      activeTab: 'project',
      innovationStats: {
        activeProjects: 0,
        totalPatents: 0,
        rdInvestment: 0,
        achievements: 0
      }
    }
  },
  mounted() {
    this.loadInnovationStats()
  },
  methods: {
    async loadInnovationStats() {
      try {
        const res = await getInnovationStatistics()
        if (res && res.data) {
          this.innovationStats = {
            activeProjects: res.data.activeProjects || 0,
            totalPatents: res.data.totalPatents || 0,
            rdInvestment: res.data.rdInvestment || 0,
            achievements: res.data.achievements || 0
          }
        }
      } catch (error) {
        console.error('加载技术创新统计数据失败:', error)
      }
    },

    handleTabClick(tab) {
      // 切换标签页时可刷新统计数据
    }
  }
}
</script>

<style lang="scss" scoped>
.innovation-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: linear-gradient(135deg, var(--ip-primary, #9c27b0) 0%, var(--ip-secondary, #673ab7) 100%);
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

        i {
          font-size: 24px;
          color: white;
        }

        &.project {
          background: linear-gradient(135deg, var(--ip-primary, #9c27b0) 0%, var(--ip-secondary, #673ab7) 100%);
        }

        &.patent {
          background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
        }

        &.investment {
          background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
        }

        &.achievement {
          background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
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
      color: #9c27b0;
    }

    :deep(.el-tabs__active-bar) {
      background-color: #9c27b0;
    }
  }
}
</style>
