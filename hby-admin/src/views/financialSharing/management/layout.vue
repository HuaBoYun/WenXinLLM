<template>
  <div class="management-layout">
    <!-- 子路由渲染区域 -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'ManagementLayout',
  data() {
    return {}
  }
}
</script>

<style lang="scss" scoped>
.management-layout {
  width: 100%;
  height: 100%;
}
</style>
