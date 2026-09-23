<template>
  <div class="rate-container">
    <el-card shadow="hover">
      <template #header>
        <span>基础用法</span>
      </template>
      <el-rate v-model="value1" />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>辅助文字</span>
      </template>
      <el-rate v-model="value2" show-text />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>只读</span>
      </template>
      <el-rate
        v-model="value3"
        disabled
        score-template="{value}"
        show-score
        text-color="#ff9900"
      />
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Rate',
    data() {
      return {
        value1: null,
        value2: null,
        value3: 3.7,
      }
    },
  }
</script>

<style lang="scss" scoped>
  .rate-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
