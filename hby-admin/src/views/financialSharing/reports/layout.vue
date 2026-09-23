<template>
  <div class="reports-layout">
    <!-- 子路由渲染区域 -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'ReportsLayout',
  data() {
    return {}
  }
}
</script>

<style lang="scss" scoped>
.reports-layout {
  width: 100%;
  height: 100%;
}
</style>

