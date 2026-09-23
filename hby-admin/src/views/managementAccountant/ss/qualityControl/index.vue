<template>
  <div class="quality-control-container">
    <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
      <el-tab-pane label="仪表板" name="dashboard">
        <QualityControlDashboard />
      </el-tab-pane>
      <el-tab-pane label="质量管控列表" name="list">
        <QualityControlList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import QualityControlDashboard from './QualityControlDashboard'
import QualityControlList from './QualityControlList'

export default {
  name: 'QualityControlIndex',
  components: {
    QualityControlDashboard,
    QualityControlList
  },
  data() {
    return {
      activeTab: 'dashboard'
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
.quality-control-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .el-tabs--border-card {
    border: none;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 8px;

    .el-tabs__header {
      background-color: #fff;
      border-bottom: 1px solid #e4e7ed;
      margin: 0;
      border-radius: 8px 8px 0 0;

      .el-tabs__nav {
        .el-tabs__item {
          border: none;
          padding: 0 30px;
          height: 50px;
          line-height: 50px;
          font-size: 14px;
          font-weight: 500;
          color: #606266;
          transition: all 0.3s;

          &:hover {
            color: #409eff;
          }

          &.is-active {
            background-color: #409eff;
            color: #fff;
            border-radius: 4px 4px 0 0;
          }
        }
      }
    }

    .el-tabs__content {
      padding: 0;
      background-color: #fff;
      border-radius: 0 0 8px 8px;
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    padding: 10px;

    .el-tabs--border-card {
      .el-tabs__header {
        .el-tabs__nav {
          .el-tabs__item {
            padding: 0 15px;
            height: 40px;
            line-height: 40px;
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
