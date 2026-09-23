<template>
  <div class="radio-container">
    <el-card shadow="hover">
      <template #header>
        <span>基础用法</span>
      </template>
      <el-radio v-model="radio" label="1">备选项</el-radio>
      <el-radio v-model="radio" label="2">备选项</el-radio>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>禁用状态</span>
      </template>
      <el-radio v-model="radio2" disabled label="禁用">备选项</el-radio>
      <el-radio v-model="radio2" disabled label="选中且禁用">备选项</el-radio>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>单选框组</span>
      </template>
      <el-radio-group v-model="radio3">
        <el-radio :label="3">备选项</el-radio>
        <el-radio :label="6">备选项</el-radio>
        <el-radio :label="9">备选项</el-radio>
      </el-radio-group>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>按钮样式</span>
      </template>
      <el-radio-group v-model="radio4">
        <el-radio-button label="上海" />
        <el-radio-button label="北京" />
        <el-radio-button label="广州" />
        <el-radio-button label="深圳" />
      </el-radio-group>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>带有边框</span>
      </template>
      <el-radio v-model="radio5" border label="1">备选项1</el-radio>
      <el-radio v-model="radio5" border label="2">备选项2</el-radio>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Radio',
    data() {
      return {
        radio: '1',
        radio2: '选中且禁用',
        radio3: 3,
        radio4: '上海',
        radio5: '1',
      }
    },
  }
</script>

<style lang="scss" scoped>
  .radio-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
