<template>
  <div class="risk-indicator-summarization">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <el-tab-pane label="分类检测指标" name="tab1">
        <risk-indicator-summarization-tab1 />
      </el-tab-pane>
      <el-tab-pane label="重大经营指标监测" name="tab2">
        <risk-indicator-summarization-tab2 />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import RiskIndicatorSummarizationTab1 from './riskIndicatorSummarizationTab1.vue'
  import RiskIndicatorSummarizationTab2 from './riskIndicatorSummarizationTab2.vue'

  export default {
    name: 'RiskIndicatorSummarization',
    components: {
      RiskIndicatorSummarizationTab1,
      RiskIndicatorSummarizationTab2,
    },
    data() {
      return {
        activeTab: 'tab1', // 默认显示第一个tab
      }
    },
    methods: {
      handleTabClick(tab) {
        console.log('切换到tab:', tab.name)
      },
    },
  }
</script>

<style scoped>
  .risk-indicator-summarization {
    padding: 10px;
  }

  .el-tabs {
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .el-tabs__header {
    margin: 0 0 15px 0;
  }

  .el-tabs__item {
    font-size: 16px;
    font-weight: 500;
  }

  .el-tabs__content {
    padding: 0;
  }
</style>
