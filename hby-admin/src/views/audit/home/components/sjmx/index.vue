<template>
  <div class="system-log-container">
    <el-tabs type="border-card" v-model="activeName3">
      <el-tab-pane label="预警结果" name="first">
        <SJMXTable :showFooter="true"></SJMXTable>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import SJMXTable from './table.vue'

  export default {
    components: {
      SJMXTable,
    },
    data() {
      return {
        calendarValue: new Date(),
        activeName3: 'first',
      }
    },
  }
</script>

<style scoped>
  .container {
    background-color: #f6f8f9;
  }
  .is-selected {
    color: #1989fa;
  }
  .headTitle {
    font-size: 20px;
  }
</style>
