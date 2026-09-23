<template>
  <div class="data-portrait-back-buttons">
    <!-- 返回数据画像总览按钮 -->
    <el-button
      type="primary"
      icon="el-icon-s-home"
      size="small"
      class="back-button overview-button"
      @click="backToOverview"
    >
      返回数据画像总览
    </el-button>

    <!-- 返回数据画像大屏按钮 -->
    <el-button
      type="success"
      icon="el-icon-full-screen"
      size="small"
      class="back-button screen-button"
      @click="backToScreen"
    >
      打开数据画像大屏
    </el-button>
  </div>
</template>

<script>
export default {
  name: 'DataPortraitBackButtons',
  methods: {
    /**
     * 返回数据画像总览页面
     */
    backToOverview() {
      console.log('🏠 点击返回数据画像总览，跳转到: /ruleMonitor/HBSJCT')
      this.$router.push({
        path: '/ruleMonitor/HBSJCT'
      })
    },

    /**
     * 返回数据画像大屏
     * 跳转到总览页面并通过 query 参数告诉总览页面自动打开大屏
     */
    backToScreen() {
      console.log('🖥️ 点击打开数据画像大屏，跳转到: /ruleMonitor/HBSJCT?openScreen=true')
      this.$router.push({
        path: '/ruleMonitor/HBSJCT',
        query: {
          openScreen: 'true'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.data-portrait-back-buttons {
  display: inline-flex;
  gap: 12px;
  align-items: center;

  .back-button {
    border-radius: 4px;
    font-weight: 500;
    transition: all 0.3s ease;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }

    &:active {
      transform: translateY(0);
    }
  }

  .overview-button {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;

    &:hover {
      background: linear-gradient(135deg, #5568d3 0%, #63408b 100%);
    }
  }

  .screen-button {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    border: none;

    &:hover {
      background: linear-gradient(135deg, #d97ce4 0%, #de4555 100%);
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .data-portrait-back-buttons {
    flex-direction: column;
    width: 100%;

    .back-button {
      width: 100%;
    }
  }
}
</style>

