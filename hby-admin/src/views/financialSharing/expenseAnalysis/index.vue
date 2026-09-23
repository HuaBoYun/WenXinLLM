<template>
  <div class="expense-analysis-container">
    <!-- 分析类型选择 -->
    <div class="analysis-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="个人费用分析" name="personal">
          <PersonalAnalysis ref="personalAnalysis" />
        </el-tab-pane>
        <el-tab-pane label="组织费用分析" name="organization">
          <OrganizationAnalysis ref="organizationAnalysis" />
        </el-tab-pane>
        <el-tab-pane label="差旅费用分析" name="travel">
          <TravelAnalysis ref="travelAnalysis" />
        </el-tab-pane>
        <el-tab-pane label="项目费用分析" name="project">
          <ProjectAnalysis ref="projectAnalysis" />
        </el-tab-pane>
        <el-tab-pane label="费用趋势分析" name="trend">
          <TrendAnalysis ref="trendAnalysis" />
        </el-tab-pane>
        <el-tab-pane label="费用对比分析" name="comparison">
          <ComparisonAnalysis ref="comparisonAnalysis" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import PersonalAnalysis from './components/PersonalAnalysis'
import OrganizationAnalysis from './components/OrganizationAnalysis'
import TravelAnalysis from './components/TravelAnalysis'
import ProjectAnalysis from './components/ProjectAnalysis'
import TrendAnalysis from './components/TrendAnalysis'
import ComparisonAnalysis from './components/ComparisonAnalysis'

export default {
  name: 'ExpenseAnalysis',
  components: {
    PersonalAnalysis,
    OrganizationAnalysis,
    TravelAnalysis,
    ProjectAnalysis,
    TrendAnalysis,
    ComparisonAnalysis
  },
  data() {
    return {
      activeTab: 'personal'
    }
  },
  methods: {
    handleTabClick(tab) {
      // 切换标签页时刷新对应组件的数据
      const componentRef = this.$refs[tab.name + 'Analysis']
      if (componentRef && componentRef.loadData) {
        componentRef.loadData()
      }
    }
  }
}
</script>

<style scoped>
.expense-analysis-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.analysis-tabs {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.analysis-tabs .el-tabs__content {
  padding: 20px;
}
</style>
