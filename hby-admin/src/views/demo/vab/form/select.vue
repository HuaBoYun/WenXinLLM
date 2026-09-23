<template>
  <div class="select-container">
    <el-card shadow="hover">
      <template #header>
        <span>基础用法</span>
      </template>
      <el-select v-model="value1" placeholder="请选择">
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>有禁用选项</span>
      </template>
      <el-select v-model="value2" placeholder="请选择">
        <el-option
          v-for="item in options2"
          :key="item.value"
          :disabled="item.disabled"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>禁用状态</span>
      </template>
      <el-select v-model="value1" disabled placeholder="请选择">
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>可清空单选</span>
      </template>
      <el-select v-model="value1" clearable placeholder="请选择">
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>基础多选</span>
      </template>
      <el-select v-model="value3" multiple placeholder="请选择">
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>可搜索</span>
      </template>
      <el-select v-model="value1" filterable placeholder="请选择">
        <el-option
          v-for="item in options1"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Select',
    data() {
      return {
        options1: [
          { value: '选项1', label: '黄金糕' },
          { value: '选项2', label: '双皮奶' },
          { value: '选项3', label: '蚵仔煎' },
          { value: '选项4', label: '龙须面' },
          { value: '选项5', label: '北京烤鸭' },
        ],
        value1: '',
        options2: [
          { value: '选项1', label: '黄金糕' },
          { value: '选项2', label: '双皮奶', disabled: true },
          { value: '选项3', label: '蚵仔煎' },
          { value: '选项4', label: '龙须面' },
          { value: '选项5', label: '北京烤鸭' },
        ],
        value2: '',
        value3: [],
      }
    },
  }
</script>

<style lang="scss" scoped>
  .select-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
