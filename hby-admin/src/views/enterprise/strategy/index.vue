<template>
  <div class="strategy-management" :style="themeVars">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-aim"></i>
          战略管理
        </h1>
        <p class="page-description">企业战略规划、执行监控、绩效评价、战略调整全生命周期管理</p>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon plan">
              <i class="el-icon-document"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ strategyStats.activePlans }}</div>
              <div class="stats-label">在执行战略</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon target">
              <i class="el-icon-flag"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ strategyStats.achievementRate }}%</div>
              <div class="stats-label">目标达成率</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon milestone">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ strategyStats.completedMilestones }}</div>
              <div class="stats-label">完成里程碑</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon risk">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stats-content">
              <div class="stats-value">{{ strategyStats.riskItems }}</div>
              <div class="stats-label">风险事项</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="战略规划" name="planning">
          <StrategyPlanningManagement ref="planningRef" />
        </el-tab-pane>
        <el-tab-pane label="执行监控" name="execution">
          <StrategyExecutionMonitoring ref="executionRef" />
        </el-tab-pane>
        <el-tab-pane label="绩效评价" name="performance">
          <StrategyPerformanceEvaluation ref="performanceRef" />
        </el-tab-pane>
        <el-tab-pane label="战略调整" name="adjustment">
          <StrategyAdjustmentManagement ref="adjustmentRef" />
        </el-tab-pane>
        <el-tab-pane label="环境分析" name="environment">
          <EnvironmentAnalysisManagement ref="environmentRef" />
        </el-tab-pane>
        <el-tab-pane label="竞争分析" name="competition">
          <CompetitionAnalysisManagement ref="competitionRef" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { getStrategyStatistics } from '@/api/enterprise/strategy'
import StrategyPlanningManagement from './components/StrategyPlanningManagement.vue'
import StrategyExecutionMonitoring from './components/StrategyExecutionMonitoring.vue'
import StrategyPerformanceEvaluation from './components/StrategyPerformanceEvaluation.vue'
import StrategyAdjustmentManagement from './components/StrategyAdjustmentManagement.vue'
import EnvironmentAnalysisManagement from './components/EnvironmentAnalysisManagement.vue'
import CompetitionAnalysisManagement from './components/CompetitionAnalysisManagement.vue'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'StrategyManagement',
  mixins: [investThemeMixin],
  components: {
    StrategyPlanningManagement,
    StrategyExecutionMonitoring,
    StrategyPerformanceEvaluation,
    StrategyAdjustmentManagement,
    EnvironmentAnalysisManagement,
    CompetitionAnalysisManagement
  },
  data() {
    return {
      activeTab: 'planning',
      strategyStats: {
        activePlans: 0,
        achievementRate: 0,
        completedMilestones: 0,
        riskItems: 0
      }
    }
  },
  mounted() {
    this.loadStrategyStats()
  },
  methods: {
    // 加载战略管理统计数据
    async loadStrategyStats() {
      try {
        const res = await getStrategyStatistics()
        if (res && res.result === 200 && res.data) {
          this.strategyStats = res.data
        }
      } catch (error) {
        console.error('加载战略统计数据失败:', error)
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
.strategy-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 100%);
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

        &.plan {
          background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 100%);
        }

        &.target {
          background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
        }

        &.milestone {
          background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
        }

        &.risk {
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
      color: var(--ip-primary);
    }

    :deep(.el-tabs__active-bar) {
      background-color: var(--ip-primary);
    }
  }
}
</style>
