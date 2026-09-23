<template>
  <div class="checkbox-container">
    <el-card shadow="hover">
      <template #header><span>基础用法</span></template>
      <el-checkbox v-model="checked">备选项</el-checkbox>
    </el-card>
    <el-card shadow="hover">
      <template #header><span>禁用状态</span></template>
      <el-checkbox v-model="checked1" disabled>备选项1</el-checkbox>
      <el-checkbox v-model="checked2" disabled>备选项</el-checkbox>
    </el-card>
    <el-card shadow="hover">
      <template #header><span>多选框组</span></template>
      <el-checkbox-group v-model="checkList">
        <el-checkbox label="复选框 A" />
        <el-checkbox label="复选框 B" />
        <el-checkbox label="复选框 C" />
        <el-checkbox disabled label="禁用" />
        <el-checkbox disabled label="选中且禁用" />
      </el-checkbox-group>
    </el-card>
    <el-card shadow="hover">
      <template #header><span>可选项目数量的限制</span></template>
      <el-checkbox-group v-model="checkedCities" :max="2" :min="1">
        <el-checkbox v-for="city in cities" :key="city" :label="city">
          {{ city }}
        </el-checkbox>
      </el-checkbox-group>
    </el-card>
    <el-card shadow="hover">
      <template #header><span>按钮样式</span></template>
      <el-checkbox-group v-model="checkboxGroup1">
        <el-checkbox-button v-for="city in cities" :key="city" :label="city">
          {{ city }}
        </el-checkbox-button>
      </el-checkbox-group>
    </el-card>
    <el-card shadow="hover">
      <template #header><span>带有边框</span></template>
      <el-checkbox v-model="checked3" border label="备选项1" />
      <el-checkbox v-model="checked4" border label="备选项2" />
    </el-card>
  </div>
</template>

<script>
  const cityOptions = ['上海', '北京', '广州', '深圳']

  export default {
    name: 'Checkbox',
    data() {
      return {
        checked: true,
        checked1: false,
        checked2: true,
        checkList: ['选中且禁用', '复选框 A'],
        checkedCities: ['上海', '北京'],
        cities: cityOptions,
        checkboxGroup1: ['上海'],
        checked3: true,
        checked4: false,
      }
    },
  }
</script>

<style lang="scss" scoped>
  .checkbox-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
