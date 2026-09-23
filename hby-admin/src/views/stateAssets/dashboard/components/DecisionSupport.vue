<template>
  <div class="decision-support">
    <!-- 决策支持导航 -->
    <div class="support-nav">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="政策影响分析" name="policy">
          <div class="tab-content">
            <PolicyImpactAnalysis :enterprise-id="enterpriseId" :enterprise-name="enterpriseName" :module-stats-data="moduleStatsData" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="风险预测模型" name="risk">
          <div class="tab-content">
            <RiskPredictionModel :enterprise-id="enterpriseId" :enterprise-name="enterpriseName" :module-stats-data="moduleStatsData" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="投资决策支持" name="investment">
          <div class="tab-content">
            <InvestmentDecisionSupport :enterprise-id="enterpriseId" :enterprise-name="enterpriseName" :module-stats-data="moduleStatsData" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="绩效评估分析" name="performance">
          <div class="tab-content">
            <PerformanceEvaluation :enterprise-id="enterpriseId" :enterprise-name="enterpriseName" :module-stats-data="moduleStatsData" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="智能推荐" name="recommendation">
          <div class="tab-content">
            <IntelligentRecommendation :enterprise-id="enterpriseId" :enterprise-name="enterpriseName" :module-stats-data="moduleStatsData" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import PolicyImpactAnalysis from './PolicyImpactAnalysis.vue'
import RiskPredictionModel from './RiskPredictionModel.vue'
import InvestmentDecisionSupport from './InvestmentDecisionSupport.vue'
import PerformanceEvaluation from './PerformanceEvaluation.vue'
import IntelligentRecommendation from './IntelligentRecommendation.vue'

export default {
  name: 'DecisionSupport',
  components: {
    PolicyImpactAnalysis,
    RiskPredictionModel,
    InvestmentDecisionSupport,
    PerformanceEvaluation,
    IntelligentRecommendation
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    },
    moduleStatsData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'policy'
    }
  },
  methods: {
    handleTabClick(tab) {
      console.log('切换到标签页:', tab.name)
    }
  }
}
</script>

<style lang="scss" scoped>
.decision-support {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .support-nav {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .tab-content {
      margin-top: 20px;
      min-height: 600px;
    }
  }
}

::v-deep .el-tabs__header {
  margin-bottom: 0;
}

::v-deep .el-tabs__nav-wrap::after {
  display: none;
}

::v-deep .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
  padding: 0 30px;
  height: 50px;
  line-height: 50px;
}

::v-deep .el-tabs__item.is-active {
  color: #409EFF;
  font-weight: bold;
}

::v-deep .el-tabs__active-bar {
  height: 3px;
  background-color: #409EFF;
}
</style>
