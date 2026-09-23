<template>
  <div class="cssfx-container">
    <el-row :gutter="20">
      <el-col
        v-for="(item, index) in effects"
        :key="item.name"
        :lg="4"
        :md="4"
        :sm="12"
        :xl="3"
        :xs="12"
      >
        <div class="cssfx-container-card">
          <component :is="index" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { components, effects } from './components'

  export default {
    name: 'Cssfx',
    components: { ...components },
    data() {
      return {
        effects,
      }
    },
  }
</script>

<style lang="scss" scoped>
  .cssfx-container {
    &-card {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      min-height: 100px;
      margin-bottom: $base-margin;
      color: $base-color-white;
      background: #090821;
      border-radius: $base-border-radius;
    }
  }
</style>
