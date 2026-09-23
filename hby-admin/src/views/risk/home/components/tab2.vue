<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="最热知识" name="first">最热知识</el-tab-pane>
      <el-tab-pane label="最新知识" name="second">最新知识</el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
  export default {
    data() {
      return {
        activeName: 'first',
      }
    },
  }
</script>